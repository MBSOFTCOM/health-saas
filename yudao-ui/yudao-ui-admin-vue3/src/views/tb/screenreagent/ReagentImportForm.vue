<template>
  <Dialog v-model="dialogVisible" title="试剂导入" width="400">
    <el-upload
      ref="uploadRef"
      v-model:file-list="fileList"
      :action="importUrl"
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
      <Icon icon="ep:upload" />
      <div class="el-upload__text">将文件拖到此处，或<em style="font-size: 16px">点击上传</em></div>
      <template #tip>
        <div class="el-upload__tip text-center">
          <span>仅允许导入 xls、xlsx 格式文件。</span>
          <el-link
            :underline="false"
            style="font-size: 16px; vertical-align: baseline"
            type="primary"
            @click="importTemplate"
          >
            下载导入模板
          </el-link>
        </div>
      </template>
    </el-upload>

    <div v-if="isComplete">
      {{ resultData1 }}
      <br/>
      {{ resultData2 }}
    </div>
    <el-collapse v-if="resultData3">
      <el-collapse-item title="有误数据详情：">
        <ul class="scrollable-content" style="max-height: 150px; overflow-y: auto;">
          <li v-for="(value, key) in resultData3" :key="key">
            <p>第{{ parseInt(key)}}条</p>
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
import { getAccessToken, getTenantId } from '@/utils/auth'
import download from '@/utils/download'
import {ScreenReagentApi} from "@/api/tb/screenreagent";
import {onMounted, ref, reactive, nextTick} from 'vue'


defineOptions({ name: 'ReagentImportForm' })

const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const formLoading = ref(false) // 表单的加载中
const uploadRef = ref()
const importUrl =
  import.meta.env.VITE_BASE_URL + import.meta.env.VITE_API_URL + '/tb/screen-reagent/import-excel'
const uploadHeaders = ref() // 上传 Header 头
const fileList = ref([]) // 文件列表
const updateSupport = ref(0) // 是否更新已经存在的用户数据

const isComplete = ref(false) //导入完后显示
const resultData1 = ref('') //导入后反馈成功数据
const resultData2 = ref('') //导入后反馈失败数据
const resultData3 = ref('') //导入后反馈失败的哪几条数据

/** 打开弹窗 */
const open = () => {
  dialogVisible.value = true
  updateSupport.value = 0
  fileList.value = []
  resetForm()
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const submitForm = async () => {
  if (fileList.value.length == 0) {
    message.error('请上传文件')
    return
  }
  // 提交请求
  uploadHeaders.value = {
    Authorization: 'Bearer ' + getAccessToken(),
    'tenant-id': getTenantId()
  }
  formLoading.value = true
  uploadRef.value!.submit()
}

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

  resultData1.value = '成功导入试剂数量：' + data.createSpecification.length + '条。';
  resultData2.value = '导入失败数量：' + Object.keys(data.failureSpecification).length + '条。';
  resultData3.value = data.failureSpecification;

  // 发送操作成功的事件
  emits('success')
}

/** 上传错误提示 */
const submitFormError = (): void => {
  message.error('上传失败，请您重新上传！')
  formLoading.value = false
}

/** 重置表单 */
const resetForm = async (): Promise<void> => {
  // 重置上传状态和文件
  formLoading.value = false
  //重置文件列表
  fileList.value = []
  isComplete.value = false
  resultData1.value = ''
  resultData2.value = ''
  resultData3.value = ''
  await nextTick()
  uploadRef.value?.clearFiles()
}

/** 文件数超出提示 */
const handleExceed = (): void => {
  message.error('最多只能上传一个文件！')
}

/** 下载模板操作 */
const importTemplate = async () => {
  const res = await ScreenReagentApi.importUserTemplate()
  await download.excel(res, '试剂导入模版.xls')
  return message.success("成功下载试剂导入模板！")
}

/**
 * 把错误信息根据 '.' 分割分行显示
 * @param value
 */
function formatValue(value) {
  // 分割字符串并用<br>重新连接，移除最后一个空元素
  return value.split('.').filter(v => v).join('.<br>');
}
</script>
