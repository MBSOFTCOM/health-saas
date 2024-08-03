<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="规则编号" prop="ruleCode">
        <el-input v-model="formData.ruleCode" placeholder="请输入规则编号" />
      </el-form-item>
      <el-form-item label="英文名称" prop="englishName">
        <el-input v-model="formData.englishName" placeholder="请输入英文名称" />
      </el-form-item>
      <el-form-item label="中文名称" prop="chineseName">
        <el-input v-model="formData.chineseName" placeholder="请输入中文名称" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { EngCnReflectApi, EngCnReflectVO } from '@/api/cd/engcnreflect'

/** 中英文对照 表单 */
defineOptions({ name: 'EngCnReflectForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  ruleCode: undefined,
  englishName: undefined,
  chineseName: undefined,
})
const formRules = reactive({
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await EngCnReflectApi.getEngCnReflect(id)
    } finally {
      formLoading.value = false
    }
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  await formRef.value.validate()
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as EngCnReflectVO
    if (formType.value === 'create') {
      await EngCnReflectApi.createEngCnReflect(data)
      message.success(t('common.createSuccess'))
    } else {
      await EngCnReflectApi.updateEngCnReflect(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    ruleCode: undefined,
    englishName: undefined,
    chineseName: undefined,
  }
  formRef.value?.resetFields()
}
</script>