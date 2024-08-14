<template>
  <Dialog v-model="dialogVisible" title="待筛查人员导入" width="500">
    <el-upload
      ref="uploadRef"
      v-model:file-list="fileList"
      :action="importUrl + '?year=' + formData.year +'&screenType='
      + formData.screenType + '&screenStartTime=' + formData.screenStartTime
      + '&screenEndTime=' + formData.screenEndTime  + '&deptId=' + formData.deptId"
      :auto-upload="false"
      :disabled="formLoading"
      :headers="uploadHeaders"
      :limit="1"
      :on-error="submitFormError"
      :on-exceed="handleExceed"
      :on-success="submitFormSuccess"
      accept=".xlsx, .xls"
      drag
    >
      <Icon icon="ep:upload"/>
      <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
      <template #tip>
        <div class="el-upload__tip text-center">
          <span>仅允许导入 xls、xlsx 格式文件。</span>
        </div>
      </template>
    </el-upload>

    <el-form :rules="formRules"
             ref="formRef"
             :model="formData">
      <el-form-item label="筛查类型:" label-width="100px" prop="screenType">
        <el-radio-group v-model="formData.screenType">
          <el-radio v-for="dict in getIntDictOptions(DICT_TYPE.TB_SCREEN_TYPE)"
                    :key="dict.value"
                    :label="dict.value">
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="工作年度:" label-width="100px" prop="year">
        <el-date-picker
          v-model="formData.year"
          type="year"
          value-format="YYYY"
          placeholder="请选择工作年度"
          class="!w-180px"
        />
      </el-form-item>

      <el-form-item label="计划筛查时间:" label-width="120px" prop="timeRange">
        <el-date-picker
          v-model="formData.timeRange"
          type="daterange"
          range-separator="-"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          :size="'default'"
          @change="formatTime(formData.timeRange)"
        />
      </el-form-item>

      <el-form-item label="所属管理部门:" label-width="120px" prop="deptId">
        <el-select
          v-model="formData.deptId"
          filterable
          placeholder="请选择"
          clearable
          class="!w-170px"
        >
          <el-option
            v-for="item in deptList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
    </el-form>


    <div v-if="isComplete">
      {{ resultData1 }}
      <br/>
      {{ resultData5 }}
      <br/>
      {{ resultData2 }}
    </div>
    <el-collapse v-if="resultData3">
      <el-collapse-item title="有误数据详情：">
        <ul class="scrollable-content" style="max-height: 150px; overflow-y: auto;">
          <li v-for="(value, key) in resultData3" :key="key">
            <p>第{{ parseInt(key) + parseInt(1) }}条</p>
            <p v-html="formatValue(value)"></p>
          </li>
        </ul>
        <span style="color: red">
            有误数据需要您手动修改或修正数据后删除已导入数据重新导入
          </span>
      </el-collapse-item>
    </el-collapse>

    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import {getAccessToken, getTenantId} from '@/utils/auth';
import {getIntDictOptions, DICT_TYPE} from '@/utils/dict';
import {reactive, ref, computed, onMounted} from "vue";
import { ScreenRepeatPersonApi} from '@/api/tb/screenrepeatperson'
import moment from 'moment';
import * as DeptApi from "@/api/system/dept";


defineOptions({name: 'ScreenPersonImportForm'})

const message = useMessage() // 消息弹窗

const isComplete = ref(false) //导入完后显示
const resultData1 = ref('') //导入后反馈成功数据
const resultData2 = ref('') //导入后反馈失败数据
const resultData5 = ref('') //导入后反馈重复成功的数据
const resultData3 = ref('') //导入后反馈失败的哪几条数据
const resultData4 = ref('') //导入后反馈重复的数据
const formData = ref({
  screenType: undefined,
  year: undefined,
  screenTime: undefined,
  timeRange: undefined,
  screenStartTime: undefined,
  screenEndTime: undefined,
  deptId: undefined,
})

const dialogVisible = ref(false) // 弹窗的是否展示
const formLoading = ref(false) // 表单的加载中
const uploadRef = ref()
const importUrl =
  import.meta.env.VITE_BASE_URL
  + import.meta.env.VITE_API_URL
  + '/tb/screen-person/import-template'

const uploadHeaders = ref() // 上传 Header 头
const fileList = ref([]) // 文件列表
const formRef = ref() // 表单 Ref

const formRules = reactive({
  screenType: [
    {required: true, message: '请选择筛查类型', trigger: 'blur'}
  ],
  year: [
    {required: true, message: '请选择工作年度', trigger: 'blur'}
  ],
  timeRange:[
    {required: true, message: '请选择计划筛查时间', trigger: 'blur'}
  ],
  deptId:[
    {required: true, message: '请选择所属管理部门', trigger: 'change'}
  ]
})
const deptList = ref([]) // 部门列表

/** 打开弹窗 */
const open = () => {
  dialogVisible.value = true
  resetForm()
  fileList.value = [] // 清空文件列表
  formData.value.screenType = undefined
  formData.value.timeRange = undefined
  formData.value.deptId = undefined
  getDeptList()

}
defineExpose({open}) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const submitForm = async () => {
  let flag = await ScreenRepeatPersonApi.getIsRemainRepeatPerson()
  if (flag){
    message.error("重复人员管理中有未处理的记录！请先联系市级管理员处理后，再进行导入。")
    return
  }
  // 校验表单
  await formRef.value.validate()
  if (fileList.value.length == 0) {
    message.error('请上传文件')
    return
  }
  // 提交请求
  uploadHeaders.value = {
    Authorization: 'Bearer ' + getAccessToken(),
    'tenant-id': getTenantId(),
  }
  formLoading.value = true
  uploadRef.value!.submit()
}

// 计算属性来获取默认年份
const defaultYear = computed(() => {
  return new Date().getFullYear(); // 获取当前年份作为默认值
});



/** 文件上传成功 */
const emits = defineEmits(['success'])
const submitFormSuccess = (response: any) => {
  if (response.code !== 0) {
    message.error(response.msg)
    formLoading.value = false
    return
  }
  // 拼接提示语
  const data = response.data

  isComplete.value = true
  resultData1.value = '导入摸底人员成功数量：' + data.createSpecification.length + '条。'
  resultData5.value = '导入重复人员成功数量：' + data.repeateSpecification.length + '条。'
  resultData2.value = '导入失败数量：' + Object.keys(data.failureSpecification).length + '条。'
  resultData3.value = data.failureSpecification
  resultData4.value = data.repeateSpecification
  // 发送操作成功的事件
  emits('success')
}

/** 上传错误提示 */
const submitFormError = (): void => {
  message.error('上传失败，请您重新上传！')
  formLoading.value = false
}

/**
 * 把错误信息根据 '.' 分割分行显示
 * @param value
 */
function formatValue(value) {
  // 分割字符串并用<br>重新连接，移除最后一个空元素
  return value.split('.').filter(v => v).join('.<br>');
}

/** 重置表单 */
const resetForm = () => {
  // 重置上传状态和文件
  formLoading.value = false
  uploadRef.value?.clearFiles()

  //重置文件列表
  fileList.value = []
  isComplete.value = false
  resultData1.value = ''
  resultData2.value = ''
  resultData3.value = ''
}

/** 文件数超出提示 */
const handleExceed = (): void => {
  message.error('最多只能上传一个文件！')
}

const formatTime = (time) =>{
  // 方法三：使用 moment.js
  const dateString1 = time[0];
  const dateString2 = time[1];

  formData.value.screenStartTime = moment(dateString1).valueOf();
  formData.value.screenEndTime = moment(dateString2).valueOf();

}

const getDeptList = async () => {
  deptList.value = await DeptApi.getMyDeptList();
}

// 使用 onMounted 钩子来设置默认年份
onMounted(() => {
  formData.value.year = defaultYear.value.toString(); // 设置默认年份到表单数据
});
</script>
