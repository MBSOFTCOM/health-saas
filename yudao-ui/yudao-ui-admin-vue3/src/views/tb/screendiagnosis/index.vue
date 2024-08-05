<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="80px"
    >
      <el-form-item label="筛查编号" prop="screenId">
        <el-input
          v-model="queryParams.screenId"
          placeholder="请输入筛查编号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="身份证号" prop="idNum">
        <el-input
          v-model="queryParams.idNum"
          placeholder="请输入身份证号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="姓名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入姓名"
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
<!--      <el-form-item label="摸底表id" prop="personId">-->
<!--        <el-input-->
<!--          v-model="queryParams.personId"-->
<!--          placeholder="请输入摸底表id"-->
<!--          clearable-->
<!--          @keyup.enter="handleQuery"-->
<!--          class="!w-240px"-->
<!--        />-->
<!--      </el-form-item>-->
      <el-form-item>
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column label="筛查点" align="center" prop="screenPoint" />
      <el-table-column label="筛查编号" align="center" prop="screenId" />
      <el-table-column label="第一人群分类" align="center" prop="firstType" >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.FIRST_TYPE" :value="scope.row.firstType"/>
        </template>
      </el-table-column>
      <el-table-column label="姓名" align="center" prop="name" />
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

      <el-table-column label="结果" align="center" prop="outcome">
        <template #default="scope">
          <ElTag v-if="scope.row.outcome">{{scope.row.outcomeStr}}</ElTag>
        </template>
      </el-table-column>
      <el-table-column label="治疗方案" align="center" prop="treatmentProgram">
        <template #default="scope">
          <dict-tag v-if="scope.row.treatmentProgram" :type="DICT_TYPE.TB_SCREEN_DIAGNOSIS_TREATMENT_PROGRAM" :value="scope.row.treatmentProgram" />
        </template>
      </el-table-column>
      <el-table-column label="是否网报" align="center" prop="report">
        <template #default="scope">
          <dict-tag v-if="scope.row.report  != undefined" :type="DICT_TYPE.TB_SCREEN_DIAGNOSIS_REPORT" :value="scope.row.report" />
        </template>
      </el-table-column>
      <el-table-column label="是否进行预防性治疗" align="center" prop="preventiveTreatment">
        <template #default="scope">
          <dict-tag v-if="scope.row.preventiveTreatment != undefined" :type="DICT_TYPE.TB_SCREEN_PREVENTIVE_TREATMENT" :value="scope.row.preventiveTreatment" />
        </template>
      </el-table-column>
      <el-table-column
        label="筛查时间"
        align="center"
        prop="screenTime"
        :formatter="dateFormatter2"
        width="150px"
      />
<!--      <el-table-column label="对应摸底表中id" align="center" prop="personId" />-->
      <el-table-column label="操作" align="center" fixed="right">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm(scope.row)"
            v-hasPermi="['tb:screen-diagnosis:update']"
          >
            诊断结果
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
</template>

<script setup lang="ts">
import { dateFormatter, dateFormatter2 } from '@/utils/formatTime'
import { ScreenDiagnosisApi, ScreenDiagnosisVO } from '@/api/tb/screendiagnosis'
import { DICT_TYPE } from '@/utils/dict'

/** 诊断组 列表 */
defineOptions({ name: 'ScreenDiagnosis' })

const { push, replace } = useRouter()
const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<ScreenDiagnosisVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  screenId: undefined,
  syncId: undefined,
  doctorSignature: undefined,
  screenTime: [],
  outcome: undefined,
  treatmentProgram: undefined,
  report: undefined,
  preventiveTreatment: undefined,
  screenOrder: undefined,
  personId: undefined,
  screenPoint: undefined,

  idNum: undefined,
  name: undefined
})
const queryFormRef = ref() // 搜索的表单

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ScreenDiagnosisApi.getScreenDiagnosisPage(queryParams)
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


const openForm = (rowData) => {
  console.log(rowData)
  // 跳转页面
  push({
    name: 'DiagnosisResult',
    query: rowData
  })
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>