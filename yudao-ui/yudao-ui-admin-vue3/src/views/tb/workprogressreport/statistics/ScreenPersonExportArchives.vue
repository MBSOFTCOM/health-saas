<template>
  <Dialog v-model="dialogVisible" :show-overflow-tooltip="true">
    <template #title>
      <span style="font-weight: bold">导出档案</span>
    </template>
    <el-form
      ref="formRef"
      :model="formData"
      label-width="100px"
      v-loading="formLoading"
    >
      <div style="margin-left: -100px; margin-top: 10px">
        <el-form-item v-model="formData.exportType">
          <el-radio-group v-model="formData.exportType">
            <el-radio value="1">导出体检表</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item style="margin-top: -20px">
          <el-radio-group v-model="formData.exportType">
            <el-radio value="2">导出知情同意书</el-radio>
          </el-radio-group>
        </el-form-item>
      </div>

    </el-form>

    <div style="margin-top: 30px">
<!--      <span style="color: #2A82E4">*批量导出档案将以压缩包形式下载所选档案图片。</span>-->
      <span style="color: #2A82E4">*批量导出档案将以压缩包形式下载。</span>
    </div>

    <div style="margin-top: 10px;margin-left: 45%">
      <el-button
        type="primary"
        size="default"
        style="width: 70px"
        @click="handleExport( )"
      >
        确定
      </el-button>
    </div>

  </Dialog>


</template>
<script setup lang="ts">
import {ScreenPersonApi, ScreenPersonVO} from '@/api/tb/screenpersonrealsituation'
import {onMounted, ref, reactive} from 'vue'
import download from "@/utils/download";



const activeName = ref('first')

/** 摸底患者信息 */
defineOptions({name: 'ScreenPersonExportArchives'})

const {t} = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const screenPersonData = ref([])
const formData = ref({
  exportType: undefined,
})

const formRef = ref() // 表单 Ref
const exportLoading = ref(false) // 导出的加载中
const personInfo = ref([]) // 所选人员列表


/** 打开弹窗 */
const open = async ( ids: any) => {
  personInfo.value = ids
  dialogVisible.value = true
  formData.value.exportType = undefined
  formLoading.value = false
}
defineExpose({open}) // 提供 open 方法，用于打开弹窗

/** 确认操作 */
const handleExport = async () => {
  // 检查导出类型是否被选择
  if (!formData.value.exportType) {
    return message.error("请选择导出类型！");
  }
  // 显示导出确认对话框
  try {
    // 确认导出操作
    await message.exportConfirm();
    // 设置导出状态为加载中
    exportLoading.value = true;
    // 准备导出参数
    const params = {
      ...formData.value,
      personInfo: personInfo.value,
    };
    if (formData.value.exportType == 2){
      // 执行导出操作
      const data = await ScreenPersonApi.exportArchives(params);
      // 下载导出文件
      download.zip(data, '知情同意书.zip')
      // 提示导出成功
      message.success("导出成功！");
    }else if (formData.value.exportType == 1){
      // 提示导出成功
      message.success("导出成功！");
    }
  } catch (error) {
    // 提示导出失败
    message.error(`导出失败: ${error.message}`);
  } finally {
    // 恢复导出状态
    exportLoading.value = false;
  }
}

</script>
