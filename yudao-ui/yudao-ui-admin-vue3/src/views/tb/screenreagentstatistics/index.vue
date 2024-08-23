<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="部门" prop="deptList">
        <el-select
          v-model="queryParams.deptList"
          placeholder="请选择"
          clearable
          class="!w-180px"
        >
          <el-option
            v-for="item in deptList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="试剂名称" prop="reagentName">
        <el-input
          v-model="queryParams.reagentName"
          placeholder="输入试剂名称"
          clearable
          @keyup.enter="handleQuery"
          class="!w-180px"
        />
      </el-form-item>
      <el-form-item label="统计周期" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
          @change="handleDateChange"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
      </el-form-item>
      <br/>
      <el-form-item style="float: right">
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :show-overflow-tooltip="true">
      <el-table-column type="index" label="序号" align="center"
                       :show-overflow-tooltip="false" fixed="left" width="80"/>
      <el-table-column label="试剂名称" align="center" prop="reagentName" />
      <el-table-column label="批次号" align="center" prop="bathNumber" />
      <el-table-column label="库存消耗量" align="center" prop="consumption" />
      <el-table-column label="消耗量同比" align="center" prop="consumptionPercentage">
        <template #default="scope">
          {{ (scope.row.consumptionPercentage).toFixed(2) }}%
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
    <el-button type="info" :v-model="flag" @click="flag = !flag" style="margin-top: 20px">
      <Icon icon="ep:reading" class="mr-5px"/>
      {{ flag == true ? "隐藏" : "显示" }}消耗量同比计算说明
    </el-button>
    <div v-if="flag">
      <p>消耗量同比计算：</p>
      <p>例如所选周期为5月1日~5月7日的库存消耗量(X)，那么同比计算周期为4月24日~4月30日的库存消耗量(Y)。</p>
      <p>消耗量同比 = ((X - Y) / Y) * 100%</p>
      <p>所选周期最长可选365天。</p>
    </div>
  </ContentWrap>
</template>

<script setup lang="ts">
import download from '@/utils/download'
import { ScreenConsumeApi, ScreenConsumeVO } from '@/api/tb/screenconsume'
import {onMounted, ref, reactive} from 'vue'
import * as DeptApi from "@/api/system/dept";

/** 消耗管理 列表 */
defineOptions({ name: 'ScreenReagentStatistics' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(false) // 列表的加载中
const list = ref<ScreenConsumeVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  reagentName: undefined,
  createTime: [],
  deptList: undefined,
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中
const flag = ref(false)
const deptList = ref([]) // 部门列表

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ScreenConsumeApi.getScreenConsumeStatistics(queryParams)
    list.value = data
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  if (queryParams.createTime.length < 1){
    return message.error("请选择统计周期！")
  }
  queryParams.pageNo = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  // handleQuery()
  list.value = []
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    if (queryParams.createTime.length < 1){
      return message.error("请选择统计周期！")
    }
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await ScreenConsumeApi.exportScreenConsumeStatistics(queryParams)

    // 格式化日期
    const formatDate = date => {
      const d = new Date(date);
      const year = d.getFullYear();
      const month = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    };

    const startDate = formatDate(queryParams.createTime[0]);
    const endDate = formatDate(queryParams.createTime[1]);

    // 设置文件名
    const fileName = `${startDate}-${endDate}消耗管理数据统计.xls`;

    download.excel(data, fileName);
    message.success("导出成功！")
  } catch {
  } finally {
    exportLoading.value = false
  }
}

const handleDateChange = (dates) => {
  if (dates && dates[0] && dates[1]) {
    const startDate = new Date(dates[0]);
    const endDate = new Date(dates[1]);
    const diffDays = (endDate - startDate) / (1000 * 60 * 60 * 24);

    if (diffDays > 365) {
      // 如果日期范围超过365天，重置选择并提示用户
      message.error('选择的日期范围不能超过365天，请重新选择统计周期！');
      queryParams.createTime = [];
    }
  }
};

const getDeptList = async () => {
  deptList.value = await DeptApi.getMyDeptList();
}

/** 初始化 **/
onMounted(() => {
  // getList()
  getDeptList()
})
</script>

