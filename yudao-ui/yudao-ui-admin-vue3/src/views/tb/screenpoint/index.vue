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
      <el-form-item label="筛查点名称" prop="name" label-width="120">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入筛查点名称"
          clearable
          @keyup.enter="handleQuery"
          class="!w-180px"
        />
      </el-form-item>
      <el-form-item label="工作年度" prop="year">
        <el-input
          v-model="queryParams.year"
          placeholder="请输入工作年度"
          clearable
          @keyup.enter="handleQuery"
          class="!w-150px"
        />
      </el-form-item>
      <el-form-item label="筛查单位" prop="screenDept">
        <el-select v-model="queryParams.screenDept"
                   clearable
                   class="!w-200px"
        >
          <el-option
            v-for="item in deptList"
            :key="item.id"
            :label="item.name"
            :value="item.name"
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
        <el-button
          type="primary"
          plain
          @click="openForm('create')"
          v-hasPermi="['tb:screen-point:create']"
        >
          <Icon icon="ep:plus" class="mr-5px"/>
          新增
        </el-button>
<!--        <el-button
          type="success"
          plain
          @click="handleExportTemplate"
          :loading="importTemplateLoading"
        >
          <Icon icon="ep:link" class="mr-5px"/>
          下载导入模板
        </el-button>-->
        <el-button
          type="warning"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['tb:screen-point:export']"
        >
          <Icon icon="ep:position" class="mr-5px"/>
          导出
        </el-button>
<!--        <el-button
          type="info"
          plain
          @click="handleImport"
          v-hasPermi="['tb:screen-point:create']"
          :loading="importLoading"
        >
          <Icon icon="ep:finished" class="mr-5px"/>
          导入
        </el-button>-->
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="false">
      <el-table-column type="index" label="序号" align="center" width="70"
                       :show-overflow-tooltip="false" fixed="left"/>
      <el-table-column label="筛查点名称" align="center" prop="name" />
      <el-table-column label="筛查单位" align="center" prop="screenDept" />
      <el-table-column label="工作年度" align="center" prop="year" />
      <el-table-column label="队长" align="center" prop="worker" >
        <template #default="scope">
          {{ resolveNickname(scope.row.worker) }}
        </template>
      </el-table-column>
      <el-table-column label="采集组人员" align="center" prop="collectWorker">
        <template #default="scope">
          {{ resolveNickname(scope.row.collectWorker) }}
        </template>
      </el-table-column>
      <el-table-column label="PPD组人员" align="center" prop="ppdWorker">
        <template #default="scope">
          {{ resolveNickname(scope.row.ppdWorker) }}
        </template>
      </el-table-column>
      <el-table-column label="CT/DR组人员" align="center" prop="drctWorker">
        <template #default="scope">
          {{ resolveNickname(scope.row.drctWorker) }}
        </template>
      </el-table-column>
<!--      <el-table-column label="痰检组人员" align="center" prop="sputumWorker" width="120">
        <template #default="scope">
          {{ resolveNickname(scope.row.sputumWorker) }}
        </template>
      </el-table-column>
      <el-table-column label="实验组人员" align="center" prop="experimentWorker" width="120">
        <template #default="scope">
          {{ resolveNickname(scope.row.experimentWorker) }}
        </template>
      </el-table-column>
      <el-table-column label="心电图组人员" align="center" prop="electrocardiogramWorker" width="120">
        <template #default="scope">
          {{ resolveNickname(scope.row.electrocardiogramWorker) }}
        </template>
      </el-table-column>
      <el-table-column label="诊断组人员" align="center" prop="diagnosisWorker" width="120">
        <template #default="scope">
          {{ resolveNickname(scope.row.diagnosisWorker) }}
        </template>
      </el-table-column>-->
      <el-table-column label="操作" align="center" width="150" fixed="right">
        <template #default="scope">

          <el-dropdown
            @command="(command) => handleCommand(command, scope.row)"
            v-hasPermi="[
                    'tb:screen-point:update',
                    'tb:screen-point:add-person',
                    'tb:screen-point:delete'
                  ]"
          >
            <el-button type="primary" link><Icon icon="ep:d-arrow-right" /> 操作筛查点</el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item
                  command="viewScreenForm"
                >
                  查看待筛查人员
                </el-dropdown-item>
                <el-dropdown-item
                  command="updateScreenForm"
                  v-if="checkPermi(['tb:screen-point:add-person'])"
                >
                  添加待筛查人员
                </el-dropdown-item>
                <el-dropdown-item
                  command="openForm"
                  v-if="checkPermi(['tb:screen-point:update'])"
                >
                  分配工作队员
                </el-dropdown-item>
                <el-dropdown-item
                  command="handleDelete"
                  v-if="checkPermi(['tb:screen-point:delete'])"
                >
                  删除筛查点
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>

<!--          <el-button
            link type="success"
            @click="viewScreenForm(scope.row.name)"
          >
            查看待筛查人员
          </el-button>
          <el-button
            link type="warning"
            @click="updateScreenForm(scope.row.name)"
            v-hasPermi="['tb:screen-point:add-person']"
            style="margin-left: -0px"
          >
            添加待筛查人员
          </el-button>
          <el-button
            link type="primary"
            @click="openForm('update', userList, scope.row.id)"
            v-hasPermi="['tb:screen-point:update']"
            style="margin-left: -0px"
          >
            分配工作队员
          </el-button>
          <el-button
            link type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['tb:screen-point:delete']"
            style="margin-left: -0px"
          >
            删除筛查点
          </el-button>-->
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>

  <!-- 表单弹窗：添加/修改 -->
  <ScreenPointForm ref="formRef" @success="getList"/>
  <!-- 查看带筛查人员 -->
  <ViewScreenForm ref="viewScreenRef"/>
  <!-- 添加带筛查人员 -->
  <UpdateScreenForm ref="updateScreenRef"/>
  <!-- 筛查点导入 -->
  <ScreenPointImportForm ref="importFormRef" @success="getList" @close="getList"/>
</template>

<script setup lang="ts">
import download from '@/utils/download'
import {ScreenPointApi, ScreenPointVO} from '@/api/tb/screenpoint'
import ScreenPointForm from './ScreenPointForm.vue'
import ViewScreenForm from './ViewScreenForm.vue'
import UpdateScreenForm from './UpdateScreenForm.vue'
import {onMounted, ref, reactive} from 'vue'
import ScreenPointImportForm from './ScreenPointImportForm.vue'
import * as UserApi from "@/api/system/user";
import { checkPermi } from '@/utils/permission'


/** 筛查点 列表 */
defineOptions({name: 'ScreenPoint'})

const message = useMessage() // 消息弹窗
const {t} = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<ScreenPointVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  name: undefined,
  year: undefined,
  collectWorker: undefined,
  ppdWorker: undefined,
  drctWorker: undefined,
  sputumWorker: undefined,
  experimentWorker: undefined,
  electrocardiogramWorker: undefined,
  diagnosisWorker: undefined,
  screenDept: undefined,
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中
const importLoading = ref(false) // 导入的加载中
const importTemplateLoading = ref(false) // 下载模板的加载中
const userList = ref([])

const userId = ref()
const userRole = ref([])
const deptList = ref([])


/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ScreenPointApi.getScreenPointPage(queryParams)
    list.value = data.list
    total.value = data.total
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
  handleQuery()
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, userlist: [], id?: number) => {
  formRef.value.open(type, userList.value, id)
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await ScreenPointApi.deleteScreenPoint(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {
  }
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await ScreenPointApi.exportScreenPoint(queryParams)
    download.excel(data, '筛查点.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

const viewScreenRef = ref()
const viewScreenForm = (name: string) => {
  viewScreenRef.value.open(name)
}

const updateScreenRef = ref()
const updateScreenForm = (name: string) => {
  updateScreenRef.value.open(name)
}

/** 导入*/
const importFormRef = ref()
const handleImport = () => {
  try {
    importLoading.value = true
    importFormRef.value.open()
  } finally {
    importLoading.value = false
  }
}


/** 下载筛查点导入模板按钮操作 */
const handleExportTemplate = async () => {
  try {
    // 导出的二次确认
    await message.confirm("是否确认下载筛查点导入模板")
    // 发起导出
    importTemplateLoading.value = true
    const data = await ScreenPointApi.importScreenPersonTemplate()
    download.excel(data, '筛查点导入模板.xls')
  } catch {
  } finally {
    importTemplateLoading.value = false
  }
}


const getUserList = async () => {
  const res = await ScreenPointApi.getUserList();
  userList.value = res
}


const resolveNickname = (value) => {
  if (value !== null) {
    const collectWorkerNickname = value.split(', ');

    const nickname = collectWorkerNickname.map(id => {
      const user = userList.value.find(item => item.id.toString() === id);
      return user ? user.nickname : null; // 如果找到了对应的用户，则返回其昵称，否则返回 null
    }).filter(nickname => nickname !== null).join(', '); // 将找到的昵称拼接成一个字符串，以逗号分隔

    return nickname
  }

}

const getUserRole  = async () => {
  userRole.value = await UserApi.getUserRole();
}


const getUserId = async () => {
  userId.value = await UserApi.getUserId();
}

const getDeptList = async () => {
  deptList.value = await ScreenPointApi.getDeptList();
}

/** 操作分发 */
const handleCommand = (command: string, row: any) => {
  switch (command) {
    case 'viewScreenForm':
      viewScreenForm(row.name)
      break
    case 'updateScreenForm':
      updateScreenForm(row.name)
      break
    case 'openForm':
      openForm('update', userList.value, row.id)
      break
    case 'handleDelete':
    handleDelete(row.id)
      break
    default:
      break
  }
}

/** 初始化 **/
onMounted(async () => {
  await getUserRole()
  await getUserId()
  await getUserList()
  await getList()
  await getDeptList()
})
</script>
