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
      <el-form-item label="行政区划" prop="nation" v-hasPermi="['tb:summary:district']">
        <el-select
          v-model="queryParams.districtCode"
          filterable
          :filter-method="PinyinMatchFun"
          placeholder="请选择区划"
          clearable
          class="!w-160px"
        >
          <el-option
            v-for="item in districtList"
            :key="item.code"
            :label="item.name"
            :value="item.code"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="工作年度" prop="year">
        <el-input
          v-model="queryParams.year"
          placeholder="请输入工作年度"
          clearable
          @keyup.enter="handleQuery"
          class="!w-160px"
        />
      </el-form-item>
<!--      <el-form-item label="筛查点" prop="screenPoint">
        <el-select v-model="queryParams.screenPoint"
                   :filter-method="PinyinScreenPoint"
                   clearable
                   filterable
                   placeholder="请选择筛查点"
                   class="!w-140px"
        >
          <el-option
            v-for="item in pointList"
            :key="item"
            :label="item"
            :value="item"/>
        </el-select>
      </el-form-item>-->

      <el-form-item>
        <el-button @click="handleQuery">
          <Icon icon="ep:search" class="mr-5px"/>
          搜索
        </el-button>
        <el-button @click="resetQuery">
          <Icon icon="ep:refresh" class="mr-5px"/>
          重置
        </el-button>
      </el-form-item>
      <br/>
      <el-form-item style="float: right">
        <el-button
          type="warning"
          plain
          @click="handleExport"
          :loading="exportLoading"
        >
          <Icon icon="ep:download" class="mr-5px"/>
          导出表格
        </el-button>
      </el-form-item>


    </el-form>
  </ContentWrap>


  <!-- 列表 -->
  <ContentWrap>
    <el-tabs
      v-model="activeName"
      type="card"
      class="demo-tabs"
      @tab-click="handleClick">

      <el-tab-pane label="学校肺结核筛查结果统计表" name="schoolStatistics">
        <el-table :data="list" v-loading="loading" show-header :stripe="true" :show-overflow-tooltip="true">
          <el-table-column label="序号" type="index" align="center" width="80"/>
          <el-table-column label="学校所在区划" align="center"  prop="districtName" width="130"/>
          <el-table-column label="学校全称" align="center" prop="schoolName" width="130"/>
          <el-table-column label="学生类别" align="center" prop="studentType" width="150">
            <template #default="scope">
              <dict-tag :type="DICT_TYPE.STUDENT_TYPE" :value="scope.row.studentType"/>
            </template>
          </el-table-column>
          <el-table-column label="实际招生总数" align="center" prop="actualStudentNumber"/>
          <el-table-column label="筛查情况" align="center">
            <el-table-column label="有肺结核可疑症状或密切接触史人数" align="center" prop="symptomsNumber"/>
            <el-table-column label="PPD皮试人数" align="center" prop="ppdNumber"/>
            <el-table-column label="PPD复验人数" align="center" prop="ppdReNumber"/>
            <el-table-column label="PPD试验结果" align="center">
              <el-table-column label="阴性" align="center" prop="feminine"/>
              <el-table-column label="阳性" align="center" prop="masculine"/>
            </el-table-column>
            <el-table-column label="不适宜PPD筛查人数（禁忌症）" align="center" prop="noPpdNumber"/>
            <el-table-column label="X线胸片检查人数" align="center">
              <el-table-column label="总数" align="center" prop="sumXrayNumber"/>
              <el-table-column label="未见异常" align="center" prop="normalXrayNumber"/>
              <el-table-column label="异常" align="center" prop="abnormalXrayNumber"/>
            </el-table-column>
<!--            <el-table-column label="活动性肺结核人数" align="center" />-->
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="医疗结构结核菌素皮肤试验开展情况统计表" name="agencyStatistics">
        <el-table :data="list2" :stripe="true" :show-overflow-tooltip="true">
          <el-table-column label="序号" type="index" align="center" width="80"/>
          <el-table-column label="筛查机构所在区划" align="center" prop="districtName"/>
          <el-table-column label="筛查机构全称" align="center" prop="deptName"/>
          <el-table-column label="PPD试验人数" align="center" prop="ppdNumber"/>
          <el-table-column label="PPD试验结果" align="center">
            <el-table-column label="复验结果人数" align="center" prop="ppdReNumber"/>
            <el-table-column label="阴性" align="center" prop="feminine"/>
            <el-table-column label="阳性" align="center" prop="masculine"/>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </ContentWrap>


</template>

<script setup lang="ts">
import download from '@/utils/download'
import {ScreenPersonApi, ScreenPersonVO} from '@/api/tb/screenpersonrealsituation'
import {onMounted, ref, reactive, nextTick} from 'vue'
import {TabsPaneContext} from "element-plus";
import * as UserApi from "@/api/system/user";
import {ScreenDistrictApi} from "@/api/tb/screendistrict";
import PinyinMatch from "pinyin-match";
import {ScreenPointApi} from "@/api/tb/screenpoint";
import {ReportApi} from "@/api/tb/report";
import {getIntDictOptions, DICT_TYPE} from '@/utils/dict';

/** 摸底 列表 */
defineOptions({name: 'Summary'})

const message = useMessage() // 消息弹窗
const {t} = useI18n() // 国际化

const loading = ref(false) // 列表的加载中
const list = ref([]) // 列表的数据
const list2 = ref([]) // 列表的数据
const queryParams = reactive({
  year: undefined,
  districtCode: undefined,
  screenPoint: undefined,
  type: 1,
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中
const importLoading = ref(false) // 导入的加载中
const districtList = ref([]) // 行政区划列表
const activeName = ref('schoolStatistics')
const pointList = ref([])


/** 查询列表 */
const getList = async () => {

  if (queryParams.year == null){
    return message.error("请选择工作年度！")
  }
  loading.value = true
  try {
    if (activeName.value == 'schoolStatistics'){
      list.value = await ReportApi.getSchoolSummary(queryParams)
    }else if (activeName.value == 'agencyStatistics'){
      list2.value = await ReportApi.getAgencySummary(queryParams)
    }
  }finally {
    loading.value = false;
    /*setTimeout(() => {
      loading.value = false;
    }, 400);*/
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  getList()
}


/** 重置按钮操作 */
const resetQuery = () => {
  queryParams.year = undefined
  queryParams.districtCode = undefined
  queryParams.screenPoint = undefined
  // queryParams.type = 1
  list.value = []
  // activeName.value = 'schoolStatistics'
}


/** 导出按钮操作 */
const handleExport = async () => {
  exportLoading.value = true; // 设置加载状态
  try {
    // 导出的二次确认
    await message.exportConfirm();

    // 准备数据和文件名
    let data;
    let fileName;

    if (activeName.value === 'schoolStatistics') {
      data = await ReportApi.exportSchoolSummary(queryParams);
      fileName = '学校肺结核筛查结果统计表.xls';
    } else if (activeName.value === 'agencyStatistics') {
      data = await ReportApi.exportAgencySummary(queryParams);
      fileName = '医疗结构结核菌素皮肤试验开展情况统计表.xls';
    } else {
      message.error('无效的统计类型');
    }

    // 执行下载
    download.excel(data, fileName);
    message.success("导出成功！");
  } catch (error) {
    // 处理错误信息
    message.error("导出失败，请重试！");
  } finally {
    exportLoading.value = false; // 清除加载状态
  }
};


// 切换 Tab标签页
const handleClick = (tab: TabsPaneContext, event: Event) => {
  activeName.value = tab.props.name
  if (activeName.value == 'schoolStatistics'){
    queryParams.type = 1
    getList()
  }else if (activeName.value == 'agencyStatistics'){
    queryParams.type = 2
    getList()
  }
}

const userRole = ref([])
const getUserRole  = async () => {
  userRole.value = await UserApi.getUserRole();
}

const getEthnicList = async () => {
  districtList.value = await ScreenDistrictApi.getDistrictList2()
  copycommonAddr.splice(0, copycommonAddr.length, ...districtList.value)
}

const copycommonAddr = reactive([])
// 拼音插件
const PinyinMatchFun = (val) => {
  if (val) {
    const result = []
    districtList.value.forEach((i) => {
      const m = PinyinMatch.match(i.name, val)
      if (m) {
        result.push(i)
      }
    })
    districtList.value.splice(0, districtList.value.length, ...result)
  } else {
    // 如果没有输入，则还原列表
    districtList.value.splice(0, districtList.value.length, ...copycommonAddr)
  }
}

const copycommonAddrScreenPoint = reactive([])
const getScreenPoint = async () => {
  pointList.value = await ScreenPointApi.getScreenPointList()
  copycommonAddrScreenPoint.splice(0, copycommonAddrScreenPoint.length, ...pointList.value)
}
const PinyinScreenPoint = (val) => {
  if (val) {
    const result = []
    pointList.value.forEach((i) => {
      const m = PinyinMatch.match(i, val)
      if (m) {
        result.push(i)
      }
    })
    pointList.value.splice(0, pointList.value.length, ...result)
  } else {
    // 如果没有输入，则还原列表
    pointList.value.splice(0, pointList.value.length, ...copycommonAddrScreenPoint)
  }
}


/** 初始化 **/
onMounted(async () => {
  await getUserRole()
  await getEthnicList()
  await getScreenPoint()
})
</script>

<style scoped lang="scss">
/* 使用更明显的颜色和加粗的样式 */
::v-deep .el-table .el-table__header th {
  border: 1px solid #333; /* 更加明显的底部边框颜色 */
}
</style>
