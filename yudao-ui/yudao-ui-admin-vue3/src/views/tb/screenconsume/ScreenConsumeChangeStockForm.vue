<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="数量" prop="number" >
        <el-input :placeholder="text" style="width: 200px"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { ScreenConsumeApi, ScreenConsumeVO } from '@/api/tb/screenconsume'
import {ref, reactive} from 'vue'


/** 消耗管理 表单 */
defineOptions({ name: 'ScreenConsumeChangeStockForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  number: undefined,
})
const text = ref('')

// 验证数量
const checkInboundNumber = (rule, value) => {
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
  number: [{ required: true, message: '数量不能为空', trigger: 'blur' },
    {validator: checkInboundNumber, trigger: 'blur'}
  ],
});
const formRef = ref() // 表单 Ref
const tittle= ref('')

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  if (type == 'increase'){
    dialogTitle.value = '增加库存';
    text.value = '请输入增加库存数量';
  }else {
    dialogTitle.value = '减少库存';
    text.value = '请输入减少库存数量';
  }
  tittle.value = type;
  resetForm()
  formData.value.id = id;
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
    if (tittle.value == 'increase'){
      await ScreenConsumeApi.getScreenConsume(formData.value.id)

    }else {


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
    number: undefined,
  }
  formRef.value?.resetFields()
}


</script>
