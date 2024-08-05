<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="110px"
      v-loading="formLoading"
    >
      <!-- 此处写法仅支持 element-plus 2.6.0以下版本 2.6.0 以上版本查看官网修改-->
      <div style="margin: 0 1% 1% 1% ; font-weight: bold">（1）痰菌检查</div>
      <el-form-item label="涂片结果：" prop="smearResult" style="margin: 0">
        <el-radio-group v-model="formData.smearResult">
          <el-radio :label="1">阳性</el-radio>
          <el-radio :label="2">阴性</el-radio>
          <el-radio :label="3">未查</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="培养结果：" prop="cultureResult" style="margin: 0">
        <el-radio-group v-model="formData.cultureResult">
          <el-radio :label="1">阳性</el-radio>
          <el-radio :label="2">阴性</el-radio>
          <el-radio :label="3">污染</el-radio>
          <el-radio :label="4">未查</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="分子生物学：" prop="molecularBiology" style="margin: 0">
        <el-radio-group v-model="formData.molecularBiology">
          <el-radio :label="1">结核分枝杆菌核酸阳性</el-radio>
          <el-radio :label="2">未检出结核分枝杆菌</el-radio>
          <el-radio :label="3">不确定</el-radio>
          <el-radio :label="4">未查</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-divider style="margin: 1%"/>
      <div style="margin: 0 1% 1% 1% ; font-weight: bold">（2）组织标本</div>

      <el-form-item label="检测结果：" prop="tissueSpecimenResult" style="margin: 0">
        <el-radio-group v-model="formData.tissueSpecimenResult">
          <el-radio :label="1">组织学阳性</el-radio>
          <el-radio :label="2">仅病理学阳性</el-radio>
          <el-radio :label="3">阴性</el-radio>
          <el-radio :label="4">未查</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-divider style="margin: 1%"/>
      <div style="margin: 0 1% 1% 1% ; font-weight: bold">（3）菌种鉴定</div>

      <el-form-item label="检测结果：" prop="strainIdentificationResult" style="margin: 0">
        <el-radio-group v-model="formData.strainIdentificationResult">
          <el-radio :label="1">结核分枝杆菌复合群</el-radio>
          <el-radio :label="2">非结核分枝杆菌</el-radio>
          <el-radio :label="3">未查</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-divider style="margin: 1%"/>
      <div style="margin: 0 1% 1% 1% ; font-weight: bold">（4）结核分支杆菌药敏检查</div>
      <el-form-item label="药敏检测方法：" prop="tbDrugSensitivityMethod" style="margin: 0">
        <el-radio-group v-model="formData.tbDrugSensitivityMethod">
          <el-radio :label="1">分子生物学</el-radio>
          <el-radio :label="2">传统药敏试验</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="耐药综合判定：" prop="drugResistanceResult" style="margin: 0">
        <el-radio-group v-model="formData.drugResistanceResult">
          <el-radio :label="1">单耐利福平</el-radio>
          <el-radio :label="2">耐多药</el-radio>
          <el-radio :label="3">广泛耐药</el-radio>
          <el-radio :label="4">单耐异烟肼</el-radio>
          <el-radio :label="5">利福平与异烟肼均敏感</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-divider style="margin: 1%"/>
      <div style="margin: 0 1% 1% 1% ; font-weight: bold">（5）HIV抗体检测</div>
      <el-form-item label="抗体检测结果：" prop="hivResult" style="margin: 0">
        <el-radio-group v-model="formData.hivResult">
          <el-radio :label="1">已知阳性</el-radio>
          <el-radio :label="2">新检测初筛阳性</el-radio>
          <el-radio :label="3">新检测确认阳性</el-radio>
          <el-radio :label="4">阴性</el-radio>
          <el-radio :label="5">拒查</el-radio>
          <el-radio :label="6">未提供</el-radio>
        </el-radio-group>
      </el-form-item>

    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import {ScreenExperimentApi, ScreenExperimentVO} from '@/api/tb/screenexperiment'

/** 实验室组 表单 */
defineOptions({name: 'ScreenExperimentForm'})

const {t} = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formData = ref({
  id: undefined,
  syncId: undefined,
  personId: undefined,
  sputumExaminationId: undefined,
  smearResult: undefined,
  cultureResult: undefined,
  molecularBiology: undefined,
  tissueSpecimenResult: undefined,
  strainIdentificationResult: undefined,
  tbDrugSensitivityMethod: undefined,
  drugResistanceResult: undefined,
  hivResult: undefined,
  remark: undefined,

  screenOrder: undefined,
  screenTime: undefined,
  screenId: undefined
})
const formRules = reactive({})
const formRef = ref() // 表单 Ref
const sputumExaminationId = ref()

// 页面传递过来的参数
const rowInfo = ref()
/** 打开弹窗 */
const open = async (rowData) => {
  rowInfo.value = rowData
  dialogVisible.value = true
  dialogTitle.value = t('tb.screen.experiment.submit')
  sputumExaminationId.value = rowData.id
  // 痰检组id
  // 修改时，设置数据
  if (rowData.id) {
    formLoading.value = true
    try {
      formData.value = await ScreenExperimentApi.getScreenExperiment(rowData.id)
    } finally {
      formLoading.value = false
    }
  }
}
defineExpose({open}) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 提交请求
  formLoading.value = true
  try {
    // 页面传递过来的参数
    // 补充 实验室id,筛查次序,筛查时间
    formData.value.sputumExaminationId = sputumExaminationId.value
    formData.value.screenOrder = rowInfo.value.screenOrder
    formData.value.screenTime = rowInfo.value.screenTime
    formData.value.personId = rowInfo.value.personId
    formData.value.screenId = rowInfo.value.screenId

    const data = formData.value as unknown as ScreenExperimentVO

    await ScreenExperimentApi.createScreenExperiment(data)
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}
</script>
