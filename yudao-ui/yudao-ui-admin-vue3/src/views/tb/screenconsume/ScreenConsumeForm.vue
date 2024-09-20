<template>
  <Dialog title="新增批次" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="试剂" prop="reagentId" style="width: 500px">
        <el-select
          v-model="formData.reagentId"
          placeholder="请选择试剂"
          clearable
          class="!w-250px"

          @change="selectReagent(formData.reagentId)"
        >
          <el-option
            v-for="item in reagentList"
            :key="item.id"
            :label="showLabelForSelect(item)"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-row type="flex" justify="space-between">
        <el-col :span="11">
          <el-form-item label="试剂类型" prop="reagentType">
            <el-select
              v-model="formData.reagentType"
              placeholder="自动匹配所选试剂类型"
              clearable
              class="!w-190px"
              disabled
            >
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.DOSAGE_FORM)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="消耗序位" prop="consumeOrder">
            <el-input v-model="formData.consumeOrder" placeholder="请输入消耗序位" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row type="flex" justify="space-between">
        <el-col :span="11">
          <el-form-item label="批次号" prop="bathNumber">
            <el-input v-model="formData.bathNumber" placeholder="请输入批次号" />
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="入库量" prop="inboundNumber">
            <el-input v-model="formData.inboundNumber" placeholder="请输入入库量" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row type="flex" justify="space-between">
        <el-col :span="11">
          <el-form-item label="有效期" prop="indate">
            <el-input v-model="formData.indate" placeholder="请输入有效期" >
              <template #append>(天)</template>
            </el-input>

          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="生产日期" prop="manufactureDate">
            <el-date-picker
              v-model="formData.manufactureDate"
              type="date"
              value-format="x"
              placeholder="选择生产日期"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { ScreenConsumeApi, ScreenConsumeVO } from '@/api/tb/screenconsume'
import {getIntDictOptions, DICT_TYPE} from '@/utils/dict'
import {onMounted, ref, reactive} from 'vue'
import {DataLine} from "@element-plus/icons-vue";


/** 消耗管理 表单 */
defineOptions({ name: 'ScreenConsumeForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  reagentId: undefined,
  reagentName: undefined,
  reagentType: undefined,
  consumeOrder: undefined,
  bathNumber: undefined,
  inboundNumber: undefined,
  currentNumber: undefined,
  manufactureDate: undefined,
  reagentSpecsNum: undefined,
  threshold: undefined,
  indate: undefined,
})
const reagentList = ref([]) // 试剂列表
const reagentConsumeList = ref([]) // 消耗列表

// 验证入库量
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
// 处理品规的回显
const resolveDict = (value, dict) => {
  let list = getIntDictOptions(dict)
  const item = list.find(item => item.value === value);
  if (item) {
    return item.label; // 返回找到的对象的 label 属性
  } else {
    return ''; // 如果找不到对应的 value，
  }
};

const showLabelForSelect = (item) => {
  if (!item){
    return ''
  }
  let dictList=getIntDictOptions(DICT_TYPE.DOSAGE_FORM)
  console.log(dictList)
  let typeLabel=dictList.find(e=> item.type == e.value)?.label
  let potency=item.titer+resolveDict(item.potencyUnit, DICT_TYPE.TB_POTENCY_UNIT)
  return item.name+" - "+typeLabel+" - "+item.reagentSpecsNum+" 人份 - "+potency +" / "+item.specification +resolveDict(item.specificationUnit, DICT_TYPE.TB_SPECIFICATION)+" / "+resolveDict(item.packageUnit, DICT_TYPE.TB_PACKAGE)+" - "+(item.manufacturer??'')
}
const formRules = reactive({
  reagentId: [{ required: true, message: '试剂不能为空', trigger: 'change' }],
  reagentType: [{ required: true, message: '试剂类型不能为空', trigger: 'change' }],
  consumeOrder: [
    { required: true, message: '消耗序位不能为空', trigger: 'blur' },
    { pattern: /^[1-99]$/, message: '消耗序位只能是1到99的整数', trigger: 'blur' }
  ],
  bathNumber: [{required: true, message: '批次号不能为空', trigger: 'blur'},
    {max: 20, message: '批次号长度不能超过20个字符', trigger: 'blur'},
    { pattern: /^[^\u4e00-\u9fa5]*$/, message: '批次号不能包含汉字', trigger: 'blur' }
  ],
  inboundNumber: [{ required: true, message: '入库数量不能为空', trigger: 'blur' },
    {validator: checkInboundNumber, trigger: 'blur'}
  ],
  manufactureDate: [{ required: true, message: '生产日期不能为空', trigger: 'change' }],
  indate: [{ required: true, message: '有效期不能为空', trigger: 'blur' }],
});
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, list: any, id?: number) => {
  // 获取试剂列表
  await getReagentList()
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  reagentConsumeList.value = list
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await ScreenConsumeApi.getScreenConsume(id)
    } finally {
      formLoading.value = false
    }
  }
  console.log(reagentConsumeList.value)
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
    const data = formData.value as unknown as ScreenConsumeVO
    if (formType.value === 'create') {

      if (formData.value.consumeOrder !== undefined) {
        // 使用 some 方法来检查 consumeOrder 是否在数组中的对象里
        /*const exists = reagentConsumeList.value.some(item => item.consumeOrder == formData.value.consumeOrder);
        if (exists) {
          return message.error("列表中已存在该消耗次序！");
        }*/
      }

      data.currentNumber = data.inboundNumber
      await ScreenConsumeApi.createScreenConsume(data)
      message.success(t('common.createSuccess'))
    } else {
      await ScreenConsumeApi.updateScreenConsume(data)
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
    reagentId: undefined,
    reagentName: undefined,
    reagentType: undefined,
    consumeOrder: undefined,
    bathNumber: undefined,
    inboundNumber: undefined,
    currentNumber: undefined,
    manufactureDate: undefined,
    reagentSpecsNum: undefined,
    threshold: undefined,
    indate: undefined,
  }
  formRef.value?.resetFields()
}

// 获取试剂列表
const getReagentList = async () => {
  reagentList.value = await ScreenConsumeApi.getReagentList();
}

// 选择试剂填充信息
const selectReagent = (id) => {
  if (reagentList.value.length > 0){
    for (let i = 0; i < reagentList.value.length; i++) {
      if (id == reagentList.value[i].id){
        formData.value.reagentId = id
        formData.value.reagentName = reagentList.value[i].name
        formData.value.reagentType = reagentList.value[i].type
        formData.value.reagentSpecsNum = reagentList.value[i].reagentSpecsNum
        formData.value.threshold = reagentList.value[i].threshold
      }
    }
  }
}
</script>
