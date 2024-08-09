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
      <el-form-item label="试剂名称" prop="reagentName">
        <el-input
          v-model="queryParams.reagentName"
          placeholder="输入试剂名称"
          clearable
          @keyup.enter="handleQuery"
          class="!w-140px"
        />
      </el-form-item>
      <el-form-item label="批次号" prop="bathNumber">
        <el-input
          v-model="queryParams.bathNumber"
          placeholder="输入批次号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-140px"
        />
      </el-form-item>
      <el-form-item label="试剂类型" prop="reagentType">
        <el-select
          v-model="queryParams.reagentType"
          placeholder="请选择"
          clearable
          class="!w-120px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.DOSAGE_FORM)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
        <el-button
          type="primary"
          plain
          @click="openForm('create')"
          v-hasPermi="['tb:screen-consume:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['tb:screen-consume:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true" :row-class-name="rowClassName">
      <el-table-column type="index" label="序号" align="center" width="70"
                       :show-overflow-tooltip="false" fixed="left"/>
      <el-table-column label="试剂名称" align="center" prop="reagentName" />
      <el-table-column label="试剂类型" align="center" prop="reagentType" width="130">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.DOSAGE_FORM" :value="scope.row.reagentType"/>
        </template>
      </el-table-column>
      <el-table-column label="消耗序位" align="center" prop="consumeOrder" width="120"/>
      <el-table-column label="批次号" align="center" prop="bathNumber" />
      <el-table-column label="入库量" align="center" prop="inboundNumber" width="120"/>
      <el-table-column label="当前库存" align="center" prop="currentNumber" width="120"/>
      <el-table-column label="失效日期" align="center" prop="manufactureDate" :formatter="dateFormatter2" width="180px">
        <template #default="scope">
          {{calculateExpiryDate(scope.row.manufactureDate, scope.row.indate)}}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm2('increase', scope.row.id)"
            v-hasPermi="['tb:screen-consume:update']"
          >
            增加
          </el-button>

          <el-button
            link
            type="danger"
            @click="openForm2('decrease', scope.row.id)"
            v-hasPermi="['tb:screen-consume:update']"
          >
            减少
          </el-button>
          <el-button
            link
            type="success"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['tb:screen-consume:delete']"
          >
            详情
          </el-button>
<!--          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['tb:screen-consume:delete']"
          >
            删除
          </el-button>-->
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
  </ContentWrap>

  <!-- 表单弹窗：添加/修改 -->
  <ScreenConsumeForm ref="formRef" @success="getList" />

  <!-- 增加、减少库存 -->
  <ScreenConsumeChangeStockForm ref="formRef2" @success="getList" />

</template>

<script setup lang="ts">
import { dateFormatter2 } from '@/utils/formatTime'
import download from '@/utils/download'
import { ScreenConsumeApi, ScreenConsumeVO } from '@/api/tb/screenconsume'
import ScreenConsumeForm from './ScreenConsumeForm.vue'
import ScreenConsumeChangeStockForm from './ScreenConsumeChangeStockForm.vue'
import {getIntDictOptions, DICT_TYPE} from '@/utils/dict'
import {onMounted, ref, reactive} from 'vue'

/** 消耗管理 列表 */
defineOptions({ name: 'ScreenConsume' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<ScreenConsumeVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  reagentId: undefined,
  reagentName: undefined,
  reagentType: undefined,
  consumeOrder: undefined,
  bathNumber: undefined,
  inboundNumber: undefined,
  currentNumber: undefined,
  manufactureDate: [],
  createTime: [],
  reagentSpecsNum: undefined,
  threshold: undefined,
  indate: [],
  usable: undefined,
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ScreenConsumeApi.getScreenConsumePage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

/** 详情操作 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

/** 增加、减少库存操作 */
const formRef2 = ref()
const openForm2 = (type: string, id: number) => {
  formRef2.value.open(type, id)
}



/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await ScreenConsumeApi.deleteScreenConsume(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await ScreenConsumeApi.exportScreenConsume(queryParams)
    download.excel(data, '消耗管理.xls')
    message.success("导出成功！")
  } catch {
  } finally {
    exportLoading.value = false
  }
}

// 根据 usable 字段返回行的类名
const rowClassName = ({ row }) => {
  return row.usable == 1 ? 'row-disabled' : '';
};

const calculateExpiryDate = (manufactureDateTimestamp, validityDays) => {
  // 将生产日期时间戳转换为 Date 对象
  const manufactureDate = new Date(manufactureDateTimestamp);

  // 创建一个新的 Date 对象用于计算失效日期
  const expiryDate = new Date(manufactureDate);

  // 添加有效期天数
  expiryDate.setDate(expiryDate.getDate() + parseInt(validityDays, 10));

  // 格式化失效日期为 'YYYY-MM-DD'
  const year = expiryDate.getFullYear();
  const month = String(expiryDate.getMonth() + 1).padStart(2, '0');
  const day = String(expiryDate.getDate()).padStart(2, '0');

  return `${year}-${month}-${day}`;
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
<style scoped lang="scss">
.el-table {
  .row-disabled {
    /* 设置背景颜色为红色 */
    background-color: grey;
  }
}
</style>
