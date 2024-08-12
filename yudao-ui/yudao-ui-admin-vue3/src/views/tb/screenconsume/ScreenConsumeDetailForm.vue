<template>
  <Dialog title="批次详情" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="试剂名称" prop="reagentId">
        <el-select
          v-model="formData.reagentId"
          placeholder="请选择试剂"
          clearable
          class="!w-250px"
          @change="selectReagent(formData.reagentId)"
          disabled
        >
          <el-option
            v-for="item in reagentList"
            :key="item.id"
            :label="item.name"
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
            <el-input v-model="formData.consumeOrder" placeholder="请输入消耗序位" disabled/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row type="flex" justify="space-between">
        <el-col :span="11">
          <el-form-item label="批次号" prop="bathNumber">
            <el-input v-model="formData.bathNumber" placeholder="请输入批次号" disabled/>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="入库量" prop="inboundNumber">
            <el-input v-model="formData.inboundNumber" placeholder="请输入入库量" disabled/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row type="flex" justify="space-between">
        <el-col :span="11">
          <el-form-item label="有效期" prop="indate">
            <el-input v-model="formData.indate" placeholder="请输入有效期" disabled>
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
              disabled
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div style="font-size: 16px;font-weight: bolder;margin-top: 30px;margin-bottom: 10px">库存记录</div>
    <el-table v-loading="loading" :data="list" :stripe="true" max-height="300px" >
      <el-table-column align="center" prop="changeNumber">
        <template #default="scope">
          库存{{scope.row.type == 2 || scope.row.type == 4 ?'+':'-'}}{{scope.row.changeNumber}}
        </template>
      </el-table-column>
      <el-table-column align="center" prop="type">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.STOCK_RECORD_TYPE" :value="scope.row.type"/>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="createTime" :formatter="dateFormatter2"/>
    </el-table>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
<!--      <el-button @click="dialogVisible = false">取 消</el-button>-->
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { ScreenConsumeApi, ScreenConsumeVO } from '@/api/tb/screenconsume'
import {getIntDictOptions, DICT_TYPE} from '@/utils/dict'
import {onMounted, ref, reactive} from 'vue'
import {ScreenConsumeRecordApi} from "@/api/tb/screenconsumerecord";
import { dateFormatter2 } from '@/utils/formatTime'



/** 消耗管理 表单 */
defineOptions({ name: 'ScreenConsumeDetailForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const list = ref([]) // 列表的数据

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

const formRules = reactive({
});
const formRef = ref() // 表单 Ref

interface ScreenConsumeRecordVO {
  id: number // 主键id
  changeNumber: number // 变化量
  type: number // 变化类型（1：筛查自动扣减，2：手动增加库存，3：手动减少库存）
  consumeId: number // 消耗管理表id
}

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  // 获取试剂列表
  await getReagentList()
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    loading.value = true
    try {
      formData.value = await ScreenConsumeApi.getScreenConsume(id)
      list.value = await ScreenConsumeRecordApi.getScreenConsumeRecordList(id)
    } finally {
      formLoading.value = false
      loading.value = false
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
    const data = formData.value as unknown as ScreenConsumeVO
    if (formType.value === 'create') {
      data.currentNumber = data.inboundNumber
      await ScreenConsumeApi.createScreenConsume(data)
      message.success(t('common.createSuccess'))
    } else {
      /*await ScreenConsumeApi.updateScreenConsume(data)
      message.success(t('common.updateSuccess'))*/
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
