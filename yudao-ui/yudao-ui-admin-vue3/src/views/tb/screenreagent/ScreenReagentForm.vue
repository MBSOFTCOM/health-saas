<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" style="width: 40%">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >

      <el-row type="flex" justify="space-between">
        <el-col :span="10">
          <el-form-item label="试剂名称" prop="name">
            <el-input v-model="formData.name" clearable placeholder="请输入试剂名称"/>
          </el-form-item>
        </el-col>
        <el-col :span="14">
          <el-form-item label="转换系数(人次)" prop="reagentSpecsNum" label-width="120">
            <el-input v-model="formData.reagentSpecsNum" clearable placeholder="请输入每个品规(库存)使用人次数"/>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="试剂类型" prop="type">
        <el-select
          v-model="formData.type"
          placeholder="请选择试剂类型"
          clearable
          class="!w-162px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.DOSAGE_FORM)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-row type="flex" justify="space-between">
        <el-col :span="3">
          <el-form-item label="品规" style="margin-left: -25px"/>
        </el-col>
        <el-col :span="4">
          <el-input v-model="formData.titer" placeholder="请输入效价" clearable style="width: 95px;margin-left: 20px"/>
        </el-col>
        <el-col :span="4" style="margin-left: 10px">
          <el-select
            v-model="formData.potencyUnit"
            placeholder="效价单位"
            clearable
            class="!w-100px"
          >
            <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.TB_POTENCY_UNIT)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-input v-model="formData.specification" prop="specification" placeholder="请输入规格" clearable style="width: 95px"/>
        </el-col>
        <el-col :span="4">
          <el-select
            v-model="formData.specificationUnit"
            placeholder="规格单位"
            clearable
            class="!w-100px"
          >
            <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.TB_SPECIFICATION)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-select
            v-model="formData.packageUnit"
            placeholder="包装单位"
            clearable
            class="!w-100px"
          >
            <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.TB_PACKAGE)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-col>
      </el-row>

      <el-form-item label="库存不足预警" prop="threshold" label-width="130" style="margin-left: -30px">
        <el-input v-model="formData.threshold" placeholder="请输入品规库存不足预警数" style="width: 200px;"/>
      </el-form-item>

      <el-form-item label="供应商" prop="manufacturer">
        <el-input v-model="formData.manufacturer" placeholder="请输入试剂供应商" style="width: 200px;"/>
      </el-form-item>

    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import {ScreenReagentApi, ScreenReagentVO} from '@/api/tb/screenreagent'
import {getIntDictOptions, DICT_TYPE} from '@/utils/dict'


/** 试剂 表单 */
defineOptions({name: 'ScreenReagentForm'})

const {t} = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  name: undefined,
  type: undefined,
  reagentSpecsNum: undefined,
  usable: undefined,
  titer: undefined,
  potencyUnit: undefined,
  specification: undefined,
  specificationUnit: undefined,
  packageUnit: undefined,
  manufacturer: undefined,
  threshold: undefined,
  id: undefined,
})
// 验证转换系数
const checkReagentSpecsNum = (rule, value) => {
  if (!value) {
    return Promise.reject('请输入数字');
  }
  const trimmedValue = String(value).trim(); // 去除首尾空格
  const reagentSpecsNum = parseFloat(trimmedValue); // 使用浮点数转换

  if (isNaN(reagentSpecsNum)) {
    return Promise.reject('必须为数字');
  }
  if (!Number.isInteger(reagentSpecsNum)) {
    return Promise.reject('必须是整数');
  }
  if (reagentSpecsNum <= 0 || reagentSpecsNum > 999999) {
    return Promise.reject('必须是大于0且不超过999999的整数');
  }
  return Promise.resolve();
};

const formRules = reactive({
  name: [
    { required: true, message: '请填写试剂名称', trigger: 'blur' },
    { max: 20, message: '名称长度不能超过20个字符', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择试剂类型', trigger: 'change' },
  ],
  reagentSpecsNum: [
    { required: true, message: '请填写转换系数', trigger: 'blur' },
    {validator: checkReagentSpecsNum, trigger: 'blur'}
  ],
  manufacturer: [
    { required: true, message: '请填写试剂供应商', trigger: 'blur' },
    { max: 20, message: '剂供应商长度不能超过20个字符', trigger: 'blur' }
  ],
  threshold: [
    { required: true, message: '请填写库存预警值', trigger: 'blur' },
    {validator: checkReagentSpecsNum, trigger: 'blur'}
  ],
});
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
      formData.value = await ScreenReagentApi.getScreenReagent(id)
    } finally {
      formLoading.value = false
    }
  }
}
defineExpose({open}) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  await formRef.value.validate()
  if (formData.value.potencyUnit == null){
    console.log()
    return message.error("请选择效价单位！")
  }
  if (formData.value.specificationUnit == null){
    return message.error("请选择规格单位！")
  }
  if (formData.value.packageUnit == null){
    return message.error("请选择包装单位！")
  }
  // 校验效价和规格
  if (!validateInput(formData.value.titer)) {
    return;
  }
  if (!validateInput(formData.value.specification)) {
    return;
  }
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as ScreenReagentVO
    if (formType.value === 'create') {
      await ScreenReagentApi.createScreenReagent(data)
      message.success(t('common.createSuccess'))
    } else {
      await ScreenReagentApi.updateScreenReagent(data)
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
    name: undefined,
    type: undefined,
    reagentSpecsNum: undefined,
    usable: undefined,
    titer: undefined,
    potencyUnit: undefined,
    specification: undefined,
    specificationUnit: undefined,
    packageUnit: undefined,
    manufacturer: undefined,
    threshold: undefined,
    id: undefined,
  }
  formRef.value?.resetFields()
}

// 校验效价、规格
const validateInput = (value) => {
  // 空值检查
  if (value === null || value === undefined || value === '') {
    message.error("请输入效价和规格！");
    return false;
  }

  // 将值转换为数字
  const numericValue = parseFloat(value);

  // 数字有效性校验
  if (isNaN(numericValue) || numericValue <= 0) {
    message.error("效价和规格必须大于0！");
    return false;
  }

  // 格式校验
  const stringValue = numericValue.toString();
  const regex = /^\d+(\.\d{1,2})?$/;
  if (!regex.test(stringValue)) {
    message.error("效价和规格最多保留两位小数！");
    return false;
  }

  return true;
};
</script>
