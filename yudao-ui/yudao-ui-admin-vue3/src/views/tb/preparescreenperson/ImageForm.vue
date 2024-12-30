<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-upload
      ref="upload"
      class="upload-demo"
      action=""
      :limit="1"
      :on-exceed="handleExceed"
      :auto-upload="false"
      :http-request="uploadImage"
      accept="image/*"
    >
      <template #trigger>
        <el-button type="primary" v-hasPermi="['tb:screen-person:upload-image']">
          <Icon icon="ep:edit" class="mr-5px"/>
          选择照片
        </el-button>
      </template>

      <el-button class="ml-3" type="success" @click="submitUpload" v-hasPermi="['tb:screen-person:upload-image']">
        <Icon icon="ep:files" class="mr-5px"/>
        上传照片
      </el-button>

      <el-button @click="handleDownload" style="margin-left: 20px" >
        <Icon icon="ep:download" class="mr-5px"/>
        下载照片
      </el-button>
      <template #tip>
        <div class="el-upload__tip text-red">
          只保留一个照片，新照片会覆盖旧照片。
        </div>
      </template>
    </el-upload>
    <ContentWrap>
      <div>
        <el-image :src="imageUrl" fit="contain" :preview-src-list="srcList"/>
      </div>
    </ContentWrap>
  </Dialog>
</template>

<script setup lang="ts">
import {ScreenPersonApi, ScreenPersonVO} from '@/api/tb/screenpersonrealsituation'
import {onMounted, ref, reactive} from 'vue'
import ContentWrap from "@/components/ContentWrap/src/ContentWrap.vue"
import {genFileId} from 'element-plus'
import type {UploadInstance, UploadProps, UploadRawFile} from 'element-plus'
import {timestamp} from "@vueuse/core";

/** 摸底 表单 */
defineOptions({name: 'ImageForm'})

const {t} = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formData = ref({
  id: undefined,
  idNum: undefined,
  name: undefined,
  age: undefined,
  tel: undefined,
  sex: undefined,
  height: undefined,
  weight: undefined,
  permanentAddress: undefined,
  permanentAddressProvince: undefined,
  permanentAddressCity: undefined,
  permanentAddressCounty: undefined,
  permanentAddressTown: undefined,
  address: undefined,
  province: undefined,
  city: undefined,
  county: undefined,
  town: undefined,
  nation: undefined,
  firstType: undefined,
  moreType: undefined,
  schoolOrTemple: undefined,
  class: undefined,
  isNew: undefined,
  isNewStudent: undefined,
  isScreen: undefined,
  screenPoint: undefined,
  screenTime: undefined,
  moreTempType: []
})
const imageUrl = ref()
const srcList = ref([]) //图片预览列表
const upload = ref<UploadInstance>()
const imageId = ref() // 照片id
const personid = ref() // 患者id
const screenorder = ref() //筛查次序
const screenid = ref() //筛查编号
const imageType = ref() //图片类型
const Year = ref()
const CreenType = ref()
const ScreenType = ref()
const idnum = ref()

/** 打开弹窗 */
const open = async (type: number, personId: number, screenOrder: number, screenId: string, year, screenType,idNum:string) => {
  personid.value = personId
  screenorder.value = screenOrder
  screenid.value = screenId
  imageType.value = type
  Year.value = year
  CreenType.value = screenType
  ScreenType.value = screenType
  idnum.value = idNum
  dialogVisible.value = true
  // 根据类型设置对话框标题
  switch (type) {
    case 2:
      dialogTitle.value = 'CT';
      break;
    case 1:
      dialogTitle.value = 'DR';
      break;
    case 5:
      dialogTitle.value = '即时痰照片';
      break;
    case 6:
      dialogTitle.value = '晨痰照片';
      break;
    case 7:
      dialogTitle.value = '夜痰照片';
      break;
    case 4:
      dialogTitle.value = '心电图';
      break;
    default:
      dialogTitle.value = '其他类型';
  }
  // 点击查看图片
  if (personId) {
    try {
      const data = await ScreenPersonApi.getScreenPersonImageUrl(type, personId, screenOrder, screenId, year, screenType)
      if (data) {
        imageUrl.value = data.url;
        imageId.value = data.id;
        srcList.value = [imageUrl.value];
        // console.log("图片链接：" + imageUrl.value);
        // console.log("图片id：" + imageId.value);
      } else {
        // 如果 data 为空，设置一个默认的图片 URL 或 null
        imageUrl.value = null; // 或者设置为一个默认的图片 URL
        imageId.value = null;
        srcList.value = []; // 清空预览图片列表
      }
    } finally {
    }
  }
}

defineExpose({open}) // 提供 open 方法，用于打开弹窗

const handleExceed: UploadProps['onExceed'] = (files) => {
  upload.value!.clearFiles()
  const file = files[0] as UploadRawFile
  file.uid = genFileId()
  upload.value!.handleStart(file)
}

//上传照片
const uploadImage = async (params) => {
  try {
    if (imageId.value){
      if (imageType.value==2){
        const res = await ScreenPersonApi.uploadCtImage(
          {
            file: params.file ,
            personId : personid.value,
            idNum : idnum.value,
            screenOrder : screenorder.value,
            screenId : screenid.value,
            imageType : imageType.value,
            year : Year.value,
            screenType : ScreenType.value
          }
        )
        message.success("照片上传成功！")
        imageUrl.value = res.data
        srcList.value = [res.data]
      }else {
        const res = await ScreenPersonApi.uploadImage({file: params.file, imageId: imageId.value})
        message.success("照片上传成功！")
        imageUrl.value = res.data
        srcList.value = [res.data]
      }
    }else {
      const res = await ScreenPersonApi.uploadImage2(
        {
          file: params.file,
          personId: personid.value,
          screenOrder: screenorder.value,
          screenId: screenid.value,
          imageType: imageType.value,
          year: Year.value,
          screenType: CreenType.value
        })
      message.success("照片上传成功！")
      imageUrl.value = res.data
      srcList.value = [res.data]
    }
  }catch (error){
    message.error("图片上传失败!")
    throw error;
  }finally {

  }
}

// 上传照片
const submitUpload = () => {
  upload.value!.submit()
}

// 下载照片
const handleDownload=  async ()=> {
  if (!imageUrl.value) {
    message.error('当前没有可下载的照片');
    return;
  }
  try {
    const response = await fetch(imageUrl.value);
    const blob = await response.blob();
    const filename = 'image.jpg'; // 设置文件名，你可以根据实际情况修改
    const url = window.URL.createObjectURL(blob);

    // 创建一个链接
    const link = document.createElement('a');
    link.href = url;
    link.download = filename;
    // 将链接添加到文档中
    document.body.appendChild(link);
    // 点击链接下载文件
    link.click();
    // 移除链接
    document.body.removeChild(link);
  } catch (error) {
    message.error('下载失败，请稍后重试!');
  }
}
</script>
