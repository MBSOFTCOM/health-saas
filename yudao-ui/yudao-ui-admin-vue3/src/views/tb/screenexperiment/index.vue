<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="100px"
    >
      <el-form-item label="筛查编号" prop="screenId">
        <el-input
          v-model="queryParams.screenId"
          placeholder="请输入筛查编号"
          clearable
          @keyup.enter="handleQuery"
          class="ipt-right !w-240px"
        />
      </el-form-item>
<!--      <el-form-item label="摸底表中id" prop="personId">-->
<!--        <el-input-->
<!--          v-model="queryParams.personId"-->
<!--          placeholder="请输入摸底表中id"-->
<!--          clearable-->
<!--          @keyup.enter="handleQuery"-->
<!--          class="!w-240px"-->
<!--        />-->
<!--      </el-form-item>-->
      <el-form-item label="即时痰标本号" prop="forthwithSputumCode">
        <el-input
          v-model="queryParams.forthwithSputumCode"
          placeholder="请输入即时痰标本号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="晨痰标本号" prop="morningSputumCode">
        <el-input
          v-model="queryParams.morningSputumCode"
          placeholder="请输入晨痰标本号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="夜间痰标本号" prop="eveningSputumCode">
        <el-input
          v-model="queryParams.eveningSputumCode"
          placeholder="请输入夜间痰标本号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="筛查时间" prop="screenTime">
        <el-date-picker
          v-model="queryParams.screenTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="筛查次序" prop="screenOrder">
        <el-input
          v-model="queryParams.screenOrder"
          placeholder="请输入筛查次序"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>

      <el-form-item>
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
      </el-form-item>

    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column label="筛查编号" align="center" prop="screenId" class="ipt-right" />
      <el-table-column label="第一人群分类" align="center" prop="firstType" >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.FIRST_TYPE" :value="scope.row.firstType"/>
        </template>
      </el-table-column>
      <el-table-column label="姓名" align="center" prop="name" />
      <el-table-column label="筛查次序/时间" align="center">
        <template #default="scope">
          <el-select
            v-model="scope.row.screenOrder"
            placeholder="请选中筛查次序/时间"
            size="large"
            style="width: 100%"
            @change="change(scope.row)"
          >
            <el-option
              v-for="item in scope.row.screenOrderValues"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="性别" align="center" prop="sex">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.PATIENT_SEX" :value="scope.row.sex"/>
        </template>
      </el-table-column>
      <el-table-column label="年龄" align="center" prop="age"/>
      <el-table-column label="民族" align="center" prop="nation" >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.NATION" :value="scope.row.nation"/>
        </template>
      </el-table-column>
      <el-table-column label="身份证" align="center" prop="idNum" />
      <el-table-column label="操作" align="center" fixed="right">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm(scope.row)"
            v-hasPermi="['tb:screen-experiment:update']"
          >
            实验结果
          </el-button>
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
  <ScreenExperimentForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import download from '@/utils/download'
import { ScreenExperimentApi, ScreenExperimentVO } from '@/api/tb/screenexperiment'
import ScreenExperimentForm from './ScreenExperimentForm.vue'
import { DICT_TYPE } from '@/utils/dict'

/** 实验室组 列表 */
defineOptions({ name: 'ScreenExperiment' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<ScreenExperimentVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  screenId: undefined,
  personId: undefined,
  result: undefined,
  screenTime: [],
  screenOrder: undefined,
  screenPoint: undefined,

  // 添加痰检组字段
  morningSputumCode: undefined,
  eveningSputumCode: undefined,
  forthwithSputumCode: undefined
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中


/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ScreenExperimentApi.getScreenExperimentPage(queryParams)
    console.log(data)
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

/** 添加/修改操作 */
const formRef = ref()
const openForm = (rowData) => {
  formRef.value.open(rowData)
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await ScreenExperimentApi.deleteScreenExperiment(id)
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
    const data = await ScreenExperimentApi.exportScreenExperiment(queryParams)
    download.excel(data, '实验室组.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 筛查选中 **/
const change = async (rowData) => {
  const screenOrderData = rowData.screenOrderValues.filter(item =>
    item.value === rowData.screenOrder)
  if(screenOrderData[0]){
    console.log(screenOrderData[0])

    rowData.screenPoint = screenOrderData[0].screenPoint
    rowData.result = screenOrderData[0].result
    rowData.screenId = screenOrderData[0].screenId
    rowData.screenOrder = screenOrderData[0].value
    rowData.id = screenOrderData[0].id
  }
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
<style>
.ipt-right{
  ::v-deep .el-input__inner{
    text-align: right
  }
}
</style>