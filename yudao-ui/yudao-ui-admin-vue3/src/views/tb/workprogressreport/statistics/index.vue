<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="民族" prop="nation">
        <el-select
          v-model="queryParams.nation"
          filterable
          :filter-method="PinyinMatchFun"
          placeholder="请选择民族"
          clearable
          class="!w-160px"
        >
          <el-option
            v-for="item in ethnicList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="第一人群分类" prop="firstType" label-width="97">
        <el-select
          v-model="queryParams.firstType"
          placeholder="请选择"
          clearable
          class="!w-160px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.FIRST_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="多人群分类" prop="moreType" label-width="85">
        <el-select
          v-model="queryParams.moreTempType"
          placeholder="请选择"
          clearable
          multiple
          :max-collapse-tags="2"
          collapse-tags
          collapse-tags-tooltip
          class="!w-160px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.MORE_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="学生类别" prop="studentType">
        <el-select
          v-model="queryParams.studentType"
          placeholder="请选择学生类别"
          clearable
          class="!w-160px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.STUDENT_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="工作年度" prop="year">
        <el-input
          v-model="queryParams.year"
          placeholder="请输入工作年度"
          clearable
          @keyup.enter="handleQuery"
          class="!w-160px"
        />
      </el-form-item>

      <el-form-item label="单位" prop="schoolOrTemple">
        <el-input
          v-model="queryParams.schoolOrTemple"
          placeholder="请输入单位"
          clearable
          @keyup.enter="handleQuery"
          class="!w-160px"
        />
      </el-form-item>
      <el-form-item label="班级" prop="classroom">
        <el-input
          v-model="queryParams.classroom"
          placeholder="请输入班级"
          clearable
          @keyup.enter="handleQuery"
          class="!w-160px"
        />
      </el-form-item>
      <el-form-item label="筛查点" prop="screenPoint">
        <el-input
          v-model="queryParams.screenPoint"
          placeholder="请输入筛查点"
          clearable
          @keyup.enter="handleQuery"
          class="!w-160px"
        />
      </el-form-item>
      <el-form-item label="筛查类型" prop="screenType" label-width="97">
        <el-select
          v-model="queryParams.screenType"
          placeholder="请选择筛查类型"
          clearable
          class="!w-160px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.TB_SCREEN_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="是否筛查" prop="isScreened" label-width="120">
        <el-select
          v-model="queryParams.isScreened"
          placeholder="请选择"
          clearable
          class="!w-160px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.IS_SCREEN)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button @click="handleQuery">
          <Icon icon="ep:search" class="mr-5px"/>
          搜索
        </el-button>
        <el-button @click="resetQuery">
          <Icon icon="ep:refresh" class="mr-5px"/>
          重置
        </el-button>
      </el-form-item>
      <br/>
      <el-form-item style="float: right">
        <el-button
          type="warning"
          plain
          @click="openExportForm(ids)"
          v-hasPermi="['tb:screen-person:export']"
        >
          <Icon icon="ep:download" class="mr-5px"/>
          导出表格
        </el-button>
        <el-button
          type="warning"
          plain
          @click="openExportArchivesForm(ids)"
          :loading="exportLoading"
          v-hasPermi="['tb:screen-person:export']"
        >

          <Icon icon="ep:tickets" class="mr-5px"/>
          导出档案
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true"
              @selection-change="handleSelectionChange"
              :show-overflow-tooltip="true">
      <el-table-column type="selection" width="55" />
      <el-table-column type="index" label="序号" align="center" width="65"
                       :show-overflow-tooltip="false" fixed="left"/>
      <el-table-column label="操作" align="center" fixed="right" width="120">
        <template #default="scope">
          <el-button link type="primary"
                     @click="openNewForm( scope.row.id, scope.row.year, scope.row.screenType)"
                     v-hasPermi="['tb:screen-person:update']">
            查看
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="姓名" align="center" prop="name" width="120" fixed="left"/>
      <el-table-column label="身份证号" align="center" prop="idNum" width="180"/>
      <el-table-column label="筛查类型" align="center" prop="screenType" width="100" :show-overflow-tooltip="false">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.TB_SCREEN_TYPE" :value="scope.row.screenType"/>
        </template>
      </el-table-column>
      <el-table-column label="是否已筛查" align="center" prop="isScreened" width="105">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.IS_SCREEN" :value="scope.row.isScreened"/>
        </template>
      </el-table-column>
      <el-table-column label="第一人群分类" align="center" prop="firstType" width="130">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.FIRST_TYPE" :value="scope.row.firstType"/>
        </template>
      </el-table-column>
      <el-table-column label="多人群分类" align="center" prop="moreType" width="150">
        <template #default="scope">
          <template v-for="item in resolveMoreType(scope.row.moreType)" :key="item">
            <dict-tag :type="DICT_TYPE.MORE_TYPE" :value="item" v-show="scope.row.moreType"/>
          </template>
        </template>
      </el-table-column>
      <el-table-column label="学生类别" align="center" prop="studentType" width="130">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.STUDENT_TYPE" :value="scope.row.studentType"/>
        </template>
      </el-table-column>
      <el-table-column label="筛查点" align="center" prop="screenPoint" width="120"/>
      <el-table-column label="筛查时间" align="center" width="180">
        <template #default="scope">
          {{ new Date(scope.row.screenStartTime).toLocaleDateString() }}-{{ new Date(scope.row.screenEndTime).toLocaleDateString() }}
        </template>
      </el-table-column>
      <el-table-column label="筛查编号" align="center" prop="screenId" width="190"/>
      <el-table-column label="联系电话" align="center" prop="tel" width="120"/>
      <el-table-column label="性别" align="center" prop="sex">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.PATIENT_SEX" :value="scope.row.sex"/>
        </template>
      </el-table-column>
      <el-table-column label="年龄" align="center" prop="age"/>
      <el-table-column label="民族" align="center" prop="nation" width="100">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.NATION" :value="scope.row.nation"/>
        </template>
      </el-table-column>
      <el-table-column label="身高(cm)" align="center" prop="height" width="95"/>
      <el-table-column label="体重(kg)" align="center" prop="weight" width="95"/>
      <el-table-column label="户籍地址" align="center" prop="permanentAddress" width="260"/>
      <el-table-column label="现住址" align="center" prop="address" width="260"/>
      <el-table-column label="单位" align="center" prop="schoolOrTemple" width="130"/>
      <el-table-column label="班级" align="center" prop="classroom" width="100"/>
      <el-table-column label="年份" align="center" prop="year" width="70"/>
      <el-table-column label="备注" align="center" prop="remark" width="200"/>
    </el-table>
    <!-- 分页 -->
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>

  <ScreenPersonDetail ref="newRef"/>
  <ScreenPersonExport ref="exportRef"/>
  <ScreenPersonExportArchives ref="exportArchivesRef"/>

</template>

<script setup lang="ts">
import PinyinMatch from 'pinyin-match'
import {DICT_TYPE, getIntDictOptions} from '@/utils/dict'
import download from '@/utils/download'
import {ScreenPersonApi, ScreenPersonVO} from '@/api/tb/screenpersonrealsituation'
import ScreenPersonExport from './ScreenPersonExport.vue'
import ScreenPersonDetail from './ScreenPersonDetail.vue'
import ScreenPersonExportArchives from './ScreenPersonExportArchives.vue'
import {onMounted, reactive, ref, UnwrapRef} from 'vue'

/** 摸底 列表 */
defineOptions({name: 'Statistics'})

const message = useMessage() // 消息弹窗
const {t} = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<ScreenPersonVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  year: undefined,
  screenType: undefined,
  idNum: undefined,
  name: undefined,
  age: undefined,
  tel: undefined,
  sex: undefined,
  permanentAddress: undefined,
  address: undefined,
  nation: undefined,
  firstType: undefined,
  moreType: undefined,
  schoolOrTemple: undefined,
  classroom: undefined,
  isNew: undefined,
  isNewStudent: undefined,
  isScreened: undefined,
  screenPoint: undefined,
  screenTime: [],
  moreTempType: [],
  studentType: undefined
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中
const importLoading = ref(false) // 导入的加载中
const ethnicList = ref([]) // 民族
const copycommonAddr = reactive([])
const ids= ref([]) // 选中的待筛查人员id

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ScreenPersonApi.getScreenedPage(queryParams)
    list.value = data.list
    total.value = data.total
    // 处理 多人群分类 问题
    if (queryParams.moreTempType.length !== 0) {
      list.value = list.value.filter(item => resolveMoreType(item.moreType).some(value => queryParams.moreTempType.includes(value)));
      total.value = list.value.length
    }
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  queryParams.moreTempType = []
  handleQuery()
}

/** 查看 对话框*/
const newRef = ref()
const openNewForm = ( id: number, year: number, screenType: number) => {
  newRef.value.open( id, year, screenType)
}

/** 导出表格 对话框*/
const exportRef = ref()
const openExportForm = (ids: any ) => {
  if (ids.length < 1){
    return message.error("请先勾选人员！再进行导出。")
  }
  exportRef.value.open( ids )
}

/** 导出档案 对话框*/
const exportArchivesRef = ref()
const openExportArchivesForm = (ids: any) => {
  if (ids.length < 1){
    return message.error("请先勾选人员！再进行导出。")
  }
  exportArchivesRef.value.open( ids )
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await ScreenPersonApi.exportScreenedPerson(queryParams)
    download.excel(data, '统计表.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

// 解决多人群分类问题
const resolveMoreType = (value) => {
  const groups = {
    1: '学生',
    2: '老年人',
    4: '教职工',
    8: '密接者',
    16: '糖尿病',
    32: '僧尼',
    64: '既往患者',
    128: 'HIV/AIDS'
  }

  // 将分类编号进行排序
  const keys = Object.keys(groups).map(Number).sort((a, b) => b - a)
  const result = []

  for (let i = 0; i < keys.length; i++) {
    const key = keys[i]
    if ((value & key) === key) {
      result.push(key)
      value -= key
    }
  }
  return result
}

// 获取民族列表
const getEthnicList = () => {
  const data = getIntDictOptions(DICT_TYPE.NATION)
  ethnicList.value = data
  copycommonAddr.splice(0, copycommonAddr.length, ...data)
}

// 民族拼音
const PinyinMatchFun = (val) => {
  if (val) {
    const result = []
    ethnicList.value.forEach((i) => {
      const m = PinyinMatch.match(i.label, val)
      if (m) {
        result.push(i)
      }
    })
    ethnicList.value.splice(0, ethnicList.value.length, ...result)
  } else {
    // 如果没有输入，则还原列表
    ethnicList.value.splice(0, ethnicList.value.length, ...copycommonAddr)
  }
}

// 勾选
const handleSelectionChange = (val: any[]) => {
  ids.value = val.map(item => ({
    id: item.id,
    year: item.year,
    idNum: item.idNum,
    screenType: item.screenType,
    school: item.schoolOrTemple,
    classroom: item.classroom,
    name: item.name,
    age: item.age
  }));
}

/** 初始化 **/
onMounted(() => {
  getList()
  getEthnicList()
})
</script>
