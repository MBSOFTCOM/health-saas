<template>
  <Dialog :title="screenPointName + ' ' + '筛查人员' "
          v-model="dialogVisible"
          style="width: 1400px">

    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="身份证号" prop="idNum">
        <el-input
          v-model="queryParams.idNum"
          placeholder="请输入身份证号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-200px"
        />
      </el-form-item>
      <el-form-item label="姓名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入姓名"
          clearable
          @keyup.enter="handleQuery"
          class="!w-120px"
        />
      </el-form-item>
      <el-form-item label="年份" prop="year">
        <el-input
          v-model="queryParams.year"
          placeholder="请输入年份"
          clearable
          @keyup.enter="handleQuery"
          class="!w-140px"
        />
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
    </el-form>

    <!-- 列表 -->
    <ContentWrap>
      <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" fixed="left" v-if="titleType===2"/>
        <el-table-column type="index" label="序号" align="center" width="70"
                         :show-overflow-tooltip="false" fixed="left"/>
<!--        <el-table-column label="操作" align="center" fixed="right" width="130">
          <template #default="scope">
            <el-button link type="primary" @click="openForm('update', scope.row.id)"
                       v-hasPermi="['tb:screen-person:update']">
              修改
            </el-button>
            <el-button link type="primary"
                       @click="openNewForm('update', scope.row.id, scope.row.year)"
                       v-hasPermi="['tb:screen-person:update']">
              查看
            </el-button>
          </template>
        </el-table-column>-->
        <el-table-column label="姓名" align="center" prop="name" width="120" fixed="left"/>
        <el-table-column label="筛查点" align="center" prop="screenPoint" width="120" >
          <template #default="scope">
            <span :style="{ color: 'red' }">{{ scope.row.screenPoint }}</span>
          </template>
        </el-table-column>
        <el-table-column label="身份证号" align="center" prop="idNum" width="180"/>

        <el-table-column label="是否需筛查" align="center" prop="isNew" width="105">
          <template #default="scope">
            <dict-tag :type="DICT_TYPE.IS_NEW" :value="scope.row.isNew"/>
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
        <el-table-column
          label="计划筛查时间"
          align="center"
          prop="screenTime"
          :formatter="dateFormatter"
          width="180px"
        />
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

  </Dialog>
</template>
<script setup lang="ts">
import {onMounted, ref, reactive} from 'vue'
import {ScreenPersonApi, ScreenPersonVO} from "@/api/tb/screenpersonrealsituation";
import {dateFormatter} from '@/utils/formatTime'
import {getIntDictOptions, DICT_TYPE} from '@/utils/dict'

/** 筛查点 表单 */
defineOptions({ name: 'ViewScreenForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const formData = ref({
  name: undefined,
  remark: undefined,
  worker: undefined
})
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  year:undefined,
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
  moreTempType: []
})
const dialogVisible = ref(false) // 弹窗的是否展示
const loading = ref(true) // 列表的加载中
const list = ref<ScreenPersonVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryFormRef = ref() // 搜索的表单
const screenPointName = ref('')

/** 打开弹窗 */
const open = async (name: string) => {
  queryParams.idNum = undefined
  queryParams.name = undefined
  queryParams.year = undefined
  screenPointName.value = name
  dialogVisible.value = true
  getList()
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 搜索按钮操作 */
const handleQuery = () => {
    queryParams.pageNo = 1
    getList()
}

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    queryParams.screenPoint = screenPointName.value
    const data = await ScreenPersonApi.getScreenPersonPage(queryParams)
    list.value = data.list
    total.value = data.total

    // 处理 多人群分类 问题
    if(queryParams.moreTempType.length !== 0){
      list.value = list.value.filter(item => resolveMoreType(item.moreType).some(value => queryParams.moreTempType.includes(value)));
      total.value = list.value.length
    }

  } finally {
    loading.value = false
  }
}


/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  queryParams.year = undefined
  queryParams.moreTempType = []
  handleQuery()
}

const resolveMoreType = (value) => {
  const groups = {
    1: '学生',
    2: '老年人',
    4: '教职工',
    8: '密接者',
    16: '糖尿病',
    32: '僧尼',
    64: '既往患者'
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

</script>
