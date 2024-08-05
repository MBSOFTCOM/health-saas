<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" style="width: 1400px">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-row type="flex" justify="space-between">
        <el-col :span="8">
          <el-form-item label="筛查点名称" prop="name">
            <el-input v-model="formData.name" placeholder="请输入筛查点名称" style="width: 200px"/>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="筛查单位" prop="screenDept">
<!--            <el-input v-model="formData.screenDept" placeholder="请选择筛查单位名称" style="width: 200px"/>-->
            <el-select
              v-model="formData.screenDept"
              filterable
              :filter-method="PinyinTown"
              placeholder="请选择筛查单位"
              clearable
              class="!w-240px"
            >
              <el-option
                v-for="item in deptList"
                :key="item.id"
                :label="item.name"
                :value="item.name"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="工作年度" prop="year">
            <el-input type="number" v-model="formData.year" placeholder="请输入工作年度" style="width: 200px"/>
          </el-form-item>
        </el-col>
      </el-row>
      <ContentWrap>
        <el-tabs
          v-model="activeName"
          type="card"
          class="demo-tabs"
          @tab-click="handleClick"
        >
          <el-tab-pane label="队长" name="cap">
            <el-row type="flex" justify="space-between">
              <el-col :span="11">
                <el-form-item label="队长名字" prop="worker" style="width: 250px">
                  <el-input v-model="formData.workers" disabled/>
                </el-form-item>
              </el-col>
              <el-col :span="11">
                <el-button
                  link
                  type="success"
                  @click="distributeRole(1)"
                  v-if="isHavePower"
                >
                  分配队长
                </el-button>
              </el-col>
            </el-row>
            <el-row type="flex" justify="space-between">
              <el-col :span="11">
                <el-form-item label="队长单位" prop="deptName" style="width: 250px">
                  <el-input v-model="deptName" disabled/>
                </el-form-item>
              </el-col>
              <el-col :span="11">
                <el-form-item label="队长手机号码" prop="mobile" style="width: 250px">
                  <el-input v-model="mobile" disabled/>
                </el-form-item>
              </el-col>
            </el-row>


          </el-tab-pane>
          <el-tab-pane label="采集组" name="collectGroup" v-if="formType=='update'">
            <el-button
              type="primary"
              plain
              @click="distributeRole(8)"
            >
              <Icon icon="ep:plus" class="mr-5px" /> 新增人员
            </el-button>
            <el-table v-loading="loading" :data="collectList">
              <el-table-column label="用户编号" align="center" key="id" prop="id"/>
              <el-table-column label="用户名称" align="center" prop="username"/>
              <el-table-column label="用户昵称" align="center" prop="nickname"/>
              <el-table-column label="部门" align="center" key="deptName" prop="deptName"/>
              <el-table-column label="手机号码" align="center" prop="mobile" width="120"/>
              <el-table-column label="操作" align="center">
                <template #default="scope">
                  <el-button
                    link
                    type="danger"
                    @click="handleDelete(scope.row.id)"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="PPD组" name="ppdGroup" v-if="formType=='update'">
            <el-button
              type="primary"
              plain
              @click="distributeRole(2)"
            >
              <Icon icon="ep:plus" class="mr-5px" /> 新增人员
            </el-button>
            <el-table v-loading="loading" :data="PPDList">
              <el-table-column label="用户编号" align="center" key="id" prop="id"/>
              <el-table-column label="用户名称" align="center" prop="username"/>
              <el-table-column label="用户昵称" align="center" prop="nickname"/>
              <el-table-column label="部门" align="center" key="deptName" prop="deptName"/>
              <el-table-column label="手机号码" align="center" prop="mobile" width="120"/>
              <el-table-column label="操作" align="center">
                <template #default="scope">
                  <el-button
                    link
                    type="danger"
                    @click="handleDelete(scope.row.id)"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="CT/DR组" name="ctdrGroup" v-if="formType=='update'">
            <el-button
              type="primary"
              plain
              @click="distributeRole(3)"
            >
              <Icon icon="ep:plus" class="mr-5px" /> 新增人员
            </el-button>
            <el-table v-loading="loading" :data="ctdrList">
              <el-table-column label="用户编号" align="center" key="id" prop="id"/>
              <el-table-column label="用户名称" align="center" prop="username"/>
              <el-table-column label="用户昵称" align="center" prop="nickname"/>
              <el-table-column label="部门" align="center" key="deptName" prop="deptName"/>
              <el-table-column label="手机号码" align="center" prop="mobile" width="120"/>
              <el-table-column label="操作" align="center">
                <template #default="scope">
                  <el-button
                    link
                    type="danger"
                    @click="handleDelete(scope.row.id)"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="痰检组" name="sputumGroup" v-if="formType=='update'">
            <el-button
              type="primary"
              plain
              @click="distributeRole(4)"
            >
              <Icon icon="ep:plus" class="mr-5px" /> 新增人员
            </el-button>
            <el-table v-loading="loading" :data="sputumList">
              <el-table-column label="用户编号" align="center" key="id" prop="id"/>
              <el-table-column label="用户名称" align="center" prop="username"/>
              <el-table-column label="用户昵称" align="center" prop="nickname"/>
              <el-table-column label="部门" align="center" key="deptName" prop="deptName"/>
              <el-table-column label="手机号码" align="center" prop="mobile" width="120"/>
              <el-table-column label="操作" align="center">
                <template #default="scope">
                  <el-button
                    link
                    type="danger"
                    @click="handleDelete(scope.row.id)"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="实验组" name="experimentGroup" v-if="formType=='update'">
            <el-button
              type="primary"
              plain
              @click="distributeRole(5)"
            >
              <Icon icon="ep:plus" class="mr-5px" /> 新增人员
            </el-button>
            <el-table v-loading="loading" :data="experimentList">
              <el-table-column label="用户编号" align="center" key="id" prop="id"/>
              <el-table-column label="用户名称" align="center" prop="username"/>
              <el-table-column label="用户昵称" align="center" prop="nickname"/>
              <el-table-column label="部门" align="center" key="deptName" prop="deptName"/>
              <el-table-column label="手机号码" align="center" prop="mobile" width="120"/>
              <el-table-column label="操作" align="center">
                <template #default="scope">
                  <el-button
                    link
                    type="danger"
                    @click="handleDelete(scope.row.id)"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="心电图组" name="electrocardiogramGroup" v-if="formType=='update'">
            <el-button
              type="primary"
              plain
              @click="distributeRole(6)"
            >
              <Icon icon="ep:plus" class="mr-5px" /> 新增人员
            </el-button>
            <el-table v-loading="loading" :data="electrocardiogramList">
              <el-table-column label="用户编号" align="center" key="id" prop="id"/>
              <el-table-column label="用户名称" align="center" prop="username"/>
              <el-table-column label="用户昵称" align="center" prop="nickname"/>
              <el-table-column label="部门" align="center" key="deptName" prop="deptName"/>
              <el-table-column label="手机号码" align="center" prop="mobile" width="120"/>
              <el-table-column label="操作" align="center">
                <template #default="scope">
                  <el-button
                    link
                    type="danger"
                    @click="handleDelete(scope.row.id)"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="诊断组" name="diagnoGroup" v-if="formType=='update'">
            <el-button
              type="primary"
              plain
              @click="distributeRole(7)"
            >
              <Icon icon="ep:plus" class="mr-5px" /> 新增人员
            </el-button>
            <el-table v-loading="loading" :data="diagnoList">
              <el-table-column label="用户编号" align="center" key="id" prop="id"/>
              <el-table-column label="用户名称" align="center" prop="username"/>
              <el-table-column label="用户昵称" align="center" prop="nickname"/>
              <el-table-column label="部门" align="center" key="deptName" prop="deptName"/>
              <el-table-column label="手机号码" align="center" prop="mobile" width="120"/>
              <el-table-column label="操作" align="center">
                <template #default="scope">
                  <el-button
                    link
                    type="danger"
                    @click="handleDelete(scope.row.id)"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>

      </ContentWrap>
    </el-form>

    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>

  </Dialog>

  <DistributeRole ref="distributeRoleRef" :collect-worker="formData.collectWorker"
                  :ppd-worker="formData.ppdWorker"  :drct-worker="formData.drctWorker"
                  :sputum-worker="formData.sputumWorker" :experiment-worker="formData.experimentWorker"
                  :electrocardiogram-worker="formData.electrocardiogramWorker"
                  :diagnosis-worker="formData.diagnosisWorker" :worker="formData.worker" @close="handleClose" />

</template>

<script setup lang="ts">
import {ScreenPointApi, ScreenPointVO} from '@/api/tb/screenpoint'
import DistributeRole from './DistributeRole.vue'
import {ref, provide, reactive, onMounted, computed} from "vue";
import ContentWrap from "@/components/ContentWrap/src/ContentWrap.vue";
import * as UserApi from "@/api/system/user";
import Table from "@/components/Table/src/Table.vue";
import {inject} from "vue/dist/vue";
import PinyinMatch from "pinyin-match";
import {ScreenDistrictApi} from "@/api/tb/screendistrict";

/** 筛查点 表单 */
defineOptions({name: 'ScreenPointForm'})

const {t} = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  year: undefined,
  screenDept: undefined,
  id: undefined,
  name: undefined,
  remark: undefined,
  worker: undefined,
  workers: undefined,
  collectWorker: undefined,
  collectWorkers: undefined,
  ppdWorker: undefined,
  ppdWorkers: undefined,
  drctWorker: undefined,
  drctWorkers: undefined,
  sputumWorker: undefined,
  sputumWorkers: undefined,
  experimentWorker: undefined,
  experimentWorkers: undefined,
  electrocardiogramWorker: undefined,
  electrocardiogramWorkers: undefined,
  diagnosisWorker: undefined,
  diagnosisWorkes: undefined,
})

const checkYear = (rule, value) => {
  if (!value) {
    return Promise.reject('请输入年份');
  }
  const trimmedValue = String(value).trim(); // 将值转换为字符串并去除首尾空格
  // 正则表达式验证年份格式：必须为4位数字
  const regex = /^\d{4}$/;
  if (!regex.test(trimmedValue)) {
    return Promise.reject('年份必须为四位数字');
  }
  // 将字符串转换为数值，验证是否为整数
  const year = parseInt(trimmedValue, 10); // 使用十进制转换
  if (isNaN(year)) {
    return Promise.reject('年份必须为数字');
  }
  // 验证数值范围
  if (year <= 0) {
    return Promise.reject('年份必须大于0');
  }
  return Promise.resolve();
};

const formRules = reactive({
  name: [
    { required: true, message: '请输入筛查点名称', trigger: 'blur' },
    { min: 3, max: 20, message: '筛查点名称长度应在3到20个字符之间', trigger: 'blur' }
  ],
  worker: [
    { required: true, message: '请分配队长', trigger: 'blur' },
    { min: 1, max: 20, message: '队长名称长度应在3到20个字符之间', trigger: 'blur' }
  ],
  screenDept: [
    { required: true, message: '请选择筛查单位', trigger: 'blur' },
    { min: 2, max: 20, message: '筛查单位名称长度应在2到20个字符之间', trigger: 'blur' }
  ],
  year: [{validator: checkYear, trigger: 'blur'},
    {required: true, message: '请输入年度', trigger: 'blur'}],
});
const formRef = ref() // 表单 Ref
const userList = ref([])
// 采集组 人员
const collectList = ref([])
//PPD组 人员
const PPDList = ref([])
//CTDR组 人员
const ctdrList = ref([])
//痰检组 人员
const sputumList = ref([])
//实验组 人员
const experimentList = ref([])
//心电图组 人员
const electrocardiogramList = ref([])
//诊断组 人员
const diagnoList = ref([])
const loading = ref(true) // 列表的加载中
const activeName = ref('cap')
const nowPointId = ref()
const deptName = ref()
const mobile = ref()
const deptList = ref([])

const workerList = ref()
provide('workerList', workerList); // 使用 inject 获取父组件提供的数据
const workerRoleList = ref()
provide('workerRoleList', workerRoleList); // 使用 inject 获取父组件提供的数据
const capId = ref()
provide('capId', capId)

/** 打开弹窗 */
const open = async (type: string, userlist: [], id?: number) => {
  collectList.value = []
  PPDList.value = []
  ctdrList.value = []
  sputumList.value = []
  experimentList.value = []
  electrocardiogramList.value = []
  diagnoList.value = []
  activeName.value = 'cap'
  deptName.value = undefined
  mobile.value = undefined
  formData.value.id = id
  userList.value = userlist
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  formData.value.year = new Date().getFullYear()
  loading.value = true
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await ScreenPointApi.getScreenPoint(id)
      // 采集组人员 弹框回显
      await resolveWorkers(formData.value.collectWorker, 'collectList', collectList);

      // PPD组人员 弹框回显
      await resolveWorkers(formData.value.ppdWorker, 'PPDList', PPDList);

      // DR/CT组人员 弹框回显
      await resolveWorkers(formData.value.drctWorker, 'ctdrList', ctdrList);

      // 痰检组人员回显
      await resolveWorkers(formData.value.sputumWorker, 'sputumList', sputumList);

      // 实验组人员回显
      await resolveWorkers(formData.value.experimentWorker, 'experimentList', experimentList);

      // 心电图组人员回显
      await resolveWorkers(formData.value.electrocardiogramWorker, 'electrocardiogramList', electrocardiogramList);

      // 诊断组人员回显
      await resolveWorkers(formData.value.diagnosisWorker, 'diagnoList', diagnoList);

      // 队长回显
      if (formData.value.worker !== null) {
        const collectWorkerNickname = formData.value.worker.split(', ');
        // 队长名字
        formData.value.workers = collectWorkerNickname.map(id => {
          const user = userList.value.find(item => item.id.toString() === id);
          return user ? user.nickname : null; // 如果找到了对应的用户，则返回其昵称，否则返回 null
        }).filter(nickname => nickname !== null).join(', '); // 将找到的昵称拼接成一个字符串，以逗号分隔
        // 队长单位
        deptName.value = collectWorkerNickname.map(id => {
          const user = userList.value.find(item => item.id.toString() === id)
          return user ? user.deptName : null
        }).filter(deptName => deptName !== null)
        // 队长手机号码
        mobile.value = collectWorkerNickname.map(id => {
          const user = userList.value.find(item => item.id.toString() === id)
          return user ? user.mobile : null
        }).filter(mobile => mobile !== null)
      }
    } finally {
      formLoading.value = false
    }
  }
  loading.value = false
}

defineExpose({open}) // 提供 open 方法，用于打开弹窗

// 各组人员回显方法
const resolveWorkers = async (formDataValue, field, listToUpdate) => {
  if (formDataValue != null) {
    const workerArray = formDataValue.split(', ').map(Number);
    listToUpdate.value = await ScreenPointApi.resolveCollectList(workerArray);
  }
};

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  await formRef.value.validate()
  // 提交请求
  formLoading.value = true
  try {

    const data = formData.value as unknown as ScreenPointVO

    if (formType.value === 'create') {
      data.worker = capId.value.toString()
      await ScreenPointApi.createScreenPoint(data)
      message.success(t('common.createSuccess'))
    } else {
      await ScreenPointApi.updateScreenPoint(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}

const distributeRoleRef = ref()
const Type = ref()
// 新增人员的弹框
const distributeRole = (type: number) => {
  Type.value = type
  if (formData.value.name === undefined) {
    message.error("请输入筛查点名称！")
    return
  }
  if (formData.value.screenDept === undefined) {
    message.error("请输入筛查单位！")
    return
  }
  if (formData.value.year === undefined) {
    message.error("请输入工作年度！")
    return
  }
  distributeRoleRef.value.open(type, parseInt(formData.value.worker), formData.value.id, formData.value.year)
}

// 关闭 分配人员的窗口 的 后续处理
const handleClose = async () => {

  if (formType.value === 'update'){
    formData.value = await ScreenPointApi.getScreenPoint(formData.value.id)
  }

  if (formData.value.worker !== undefined) {
    const collectWorkerNickname = formData.value.worker.split(', ');
    formData.value.workers = collectWorkerNickname.map(id => {
      const user = userList.value.find(item => item.id.toString() === id);
      return user ? user.nickname : null; // 如果找到了对应的用户，则返回其昵称，否则返回 null
    }).filter(nickname => nickname !== null).join(', ');
    // 队长单位
    deptName.value = collectWorkerNickname.map(id => {
      const user = userList.value.find(item => item.id.toString() === id)
      return user ? user.deptName : null
    }).filter(deptName => deptName !== null)
    // 队长手机号码
    mobile.value = collectWorkerNickname.map(id => {
      const user = userList.value.find(item => item.id.toString() === id)
      return user ? user.mobile : null
    }).filter(mobile => mobile !== null)
  }

  if (formData.value.worker === undefined && capId.value !== undefined) {
    formData.value.worker = capId.value.toString()
    const WorkerNickname = capId.value.toString().split(', ');
    // 队长名字
    formData.value.workers = WorkerNickname.map(id => {
      const user = userList.value.find(item => item.id.toString() === id);
      return user ? user.nickname : null; // 如果找到了对应的用户，则返回其昵称，否则返回 null
    }).filter(nickname => nickname !== null).join(', '); // 将找到的昵称拼接成一个字符串，以逗号分隔
    // 队长单位
    deptName.value = WorkerNickname.map(id => {
      const user = userList.value.find(item => item.id.toString() === id)
      return user ? user.deptName : null
    }).filter(deptName => deptName !== null)
    // 队长手机号码
    mobile.value = WorkerNickname.map(id => {
      const user = userList.value.find(item => item.id.toString() === id)
      return user ? user.mobile : null
    }).filter(mobile => mobile !== null)
  }
  // 采集组
  if (formData.value.collectWorker != null) {
    loading.value = true
    try {
      const collectWorkerArray = formData.value.collectWorker.split(', ').map(Number)
      collectList.value = await ScreenPointApi.resolveCollectList(collectWorkerArray)
    } finally {
      loading.value = false
    }
  }

  // PPD组
  if (formData.value.ppdWorker != null) {
    loading.value = true
    try {
      const ppdWorkerArray = formData.value.ppdWorker.split(', ').map(Number)
      PPDList.value = await ScreenPointApi.resolveCollectList(ppdWorkerArray)
    } finally {
      loading.value = false
    }
  }

  // DRCT组
  if (formData.value.drctWorker != null) {
    loading.value = true
    try {
      const drctWorkerArray = formData.value.drctWorker.split(', ').map(Number)
      ctdrList.value = await ScreenPointApi.resolveCollectList(drctWorkerArray)
    } finally {
      loading.value = false
    }
  }

  // 痰检组
  if (formData.value.sputumWorker != null ) {
    loading.value = true
    try {
      const sputumWorkerArray = formData.value.sputumWorker.split(', ').map(Number)
      sputumList.value = await ScreenPointApi.resolveCollectList(sputumWorkerArray)
    } finally {
      loading.value = false
    }
  }

  // 实验组
  if (formData.value.experimentWorker != null ) {
    loading.value = true
    try {
      const experimentWorkerArray = formData.value.experimentWorker.split(', ').map(Number)
      experimentList.value = await ScreenPointApi.resolveCollectList(experimentWorkerArray)
    } finally {
      loading.value = false
    }
  }

  // 心电组
  if (formData.value.electrocardiogramWorker != null ) {
    loading.value = true
    try {
      const electrocardiogramWorkerArray = formData.value.electrocardiogramWorker.split(', ').map(Number)
      electrocardiogramList.value = await ScreenPointApi.resolveCollectList(electrocardiogramWorkerArray)
    } finally {
      loading.value = false
    }
  }

  // 诊断组
  if (formData.value.diagnosisWorker != null ) {
    loading.value = true
    try {
      const diagnosisWorkerArray = formData.value.diagnosisWorker.split(', ').map(Number)
      diagnoList.value = await ScreenPointApi.resolveCollectList(diagnosisWorkerArray)
    } finally {
      loading.value = false
    }
  }

}

const resolveWorkerList = async (workerValue, listToUpdate) => {
  if (workerValue != null) {
    loading.value = true;
    try {
      const workerArray = workerValue.split(', ').map(Number);
      listToUpdate.value = await ScreenPointApi.resolveCollectList(workerArray);
    } finally {
      loading.value = false;
    }
  }
}

const getIsHavePoint = async (userId) => {
  nowPointId.value = await ScreenPointApi.getIsDistribute(userId);
}

// 切换 Tab标签
const handleClick = (tab: TabsPaneContext, event: Event) => {
  activeName.value = tab.props.name
}


/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    let deleteId = id
    // 采集组
    if (activeName.value === 'collectGroup' ){
      let collectWorkerArray = formData.value.collectWorker.split(', ').map(Number)
      // 找到需要删除的数据
      let deletedData = collectWorkerArray.filter(workerId => workerId === deleteId);
      // 留下的数据
      collectWorkerArray = collectWorkerArray.filter(workerId => workerId !== deleteId);
      if (collectWorkerArray.length === 0) {
        // 如果没有剩余元素，可以设置一个默认值，或者做其他处理
        collectWorkerArray = "0"; // 或者其他操作
      } else {
        // 如果有剩余元素，继续拼接成字符串
        collectWorkerArray = collectWorkerArray.join(', ');
      }
      // 删除的二次确认
      await message.delConfirm()
      // 发起删除
      await ScreenPointApi.deleteScreenPointCollect(collectWorkerArray, formData.value.id, deletedData, 8)
      message.success(t('common.delSuccess'))
      // 刷新列表
      formData.value = await ScreenPointApi.getScreenPoint(formData.value.id)
      // 采集组
      if (formData.value.collectWorker != null) {
        loading.value = true
        try {
          const collectWorkerArray = formData.value.collectWorker.split(', ').map(Number)
          collectList.value = await ScreenPointApi.resolveCollectList(collectWorkerArray)
        } finally {
          loading.value = false
        }
      }
    }

    // PPD组
    if (activeName.value === 'ppdGroup'){
      let collectWorkerArray = formData.value.ppdWorker.split(', ').map(Number)
      // 找到需要删除的数据
      let deletedData = collectWorkerArray.filter(workerId => workerId === deleteId);
      // 留下的数据
      collectWorkerArray = collectWorkerArray.filter(workerId => workerId !== deleteId);
      if (collectWorkerArray.length === 0) {
        // 如果没有剩余元素，可以设置一个默认值，或者做其他处理
        collectWorkerArray = "0"; // 或者其他操作
      } else {
        // 如果有剩余元素，继续拼接成字符串
        collectWorkerArray = collectWorkerArray.join(', ');
      }
      // 删除的二次确认
      await message.delConfirm()
      // 发起删除
      await ScreenPointApi.deleteScreenPointCollect(collectWorkerArray, formData.value.id, deletedData, 2)
      message.success(t('common.delSuccess'))
      // 刷新列表
      formData.value = await ScreenPointApi.getScreenPoint(formData.value.id)
      // PPD组
      if (formData.value.ppdWorker != null) {
        loading.value = true
        try {
          const collectWorkerArray = formData.value.ppdWorker.split(', ').map(Number)
          PPDList.value = await ScreenPointApi.resolveCollectList(collectWorkerArray)
        } finally {
          loading.value = false
        }
      }
    }

    // DRCT组
    if (activeName.value === 'ctdrGroup'){
      let collectWorkerArray = formData.value.drctWorker.split(', ').map(Number)
      // 找到需要删除的数据
      let deletedData = collectWorkerArray.filter(workerId => workerId === deleteId);
      // 留下的数据
      collectWorkerArray = collectWorkerArray.filter(workerId => workerId !== deleteId);
      if (collectWorkerArray.length === 0) {
        // 如果没有剩余元素，可以设置一个默认值，或者做其他处理
        collectWorkerArray = "0"; // 或者其他操作
      } else {
        // 如果有剩余元素，继续拼接成字符串
        collectWorkerArray = collectWorkerArray.join(', ');
      }
      // 删除的二次确认
      await message.delConfirm()
      // 发起删除
      await ScreenPointApi.deleteScreenPointCollect(collectWorkerArray, formData.value.id, deletedData, 3)
      message.success(t('common.delSuccess'))
      // 刷新列表
      formData.value = await ScreenPointApi.getScreenPoint(formData.value.id)
      // DR/CT组
      if (formData.value.drctWorker != null) {
        loading.value = true
        try {
          const collectWorkerArray = formData.value.drctWorker.split(', ').map(Number)
          ctdrList.value = await ScreenPointApi.resolveCollectList(collectWorkerArray)
        } finally {
          loading.value = false
        }
      }
    }

    // 痰检组
    if (activeName.value === 'sputumGroup'){
      let collectWorkerArray = formData.value.sputumWorker.split(', ').map(Number)
      // 找到需要删除的数据
      let deletedData = collectWorkerArray.filter(workerId => workerId === deleteId);
      // 留下的数据
      collectWorkerArray = collectWorkerArray.filter(workerId => workerId !== deleteId);
      if (collectWorkerArray.length === 0) {
        // 如果没有剩余元素，可以设置一个默认值，或者做其他处理
        collectWorkerArray = "0"; // 或者其他操作
      } else {
        // 如果有剩余元素，继续拼接成字符串
        collectWorkerArray = collectWorkerArray.join(', ');
      }
      // 删除的二次确认
      await message.delConfirm()
      // 发起删除
      await ScreenPointApi.deleteScreenPointCollect(collectWorkerArray, formData.value.id, deletedData, 4)
      message.success(t('common.delSuccess'))
      // 刷新列表
      formData.value = await ScreenPointApi.getScreenPoint(formData.value.id)
      // 痰检组
      if (formData.value.sputumWorker != null) {
        loading.value = true
        try {
          const sputumWorkerArray = formData.value.sputumWorker.split(', ').map(Number)
          sputumList.value = await ScreenPointApi.resolveCollectList(sputumWorkerArray)
        } finally {
          loading.value = false
        }
      }
    }

    // 实验组
    if (activeName.value === 'experimentGroup'){
      let collectWorkerArray = formData.value.experimentWorker.split(', ').map(Number)
      // 找到需要删除的数据
      let deletedData = collectWorkerArray.filter(workerId => workerId === deleteId);
      // 留下的数据
      collectWorkerArray = collectWorkerArray.filter(workerId => workerId !== deleteId);
      if (collectWorkerArray.length === 0) {
        // 如果没有剩余元素，可以设置一个默认值，或者做其他处理
        collectWorkerArray = "0"; // 或者其他操作
      } else {
        // 如果有剩余元素，继续拼接成字符串
        collectWorkerArray = collectWorkerArray.join(', ');
      }
      // 删除的二次确认
      await message.delConfirm()
      // 发起删除
      await ScreenPointApi.deleteScreenPointCollect(collectWorkerArray, formData.value.id, deletedData, 5)
      message.success(t('common.delSuccess'))
      // 刷新列表
      formData.value = await ScreenPointApi.getScreenPoint(formData.value.id)
      // 实验组
      if (formData.value.experimentWorker != null) {
        loading.value = true
        try {
          const experimentWorkerArray = formData.value.experimentWorker.split(', ').map(Number)
          experimentList.value = await ScreenPointApi.resolveCollectList(experimentWorkerArray)
        } finally {
          loading.value = false
        }
      }
    }

    // 心电图组
    if (activeName.value === 'electrocardiogramGroup'){
      let collectWorkerArray = formData.value.electrocardiogramWorker.split(', ').map(Number)
      // 找到需要删除的数据
      let deletedData = collectWorkerArray.filter(workerId => workerId === deleteId);
      // 留下的数据
      collectWorkerArray = collectWorkerArray.filter(workerId => workerId !== deleteId);
      if (collectWorkerArray.length === 0) {
        // 如果没有剩余元素，可以设置一个默认值，或者做其他处理
        collectWorkerArray = "0"; // 或者其他操作
      } else {
        // 如果有剩余元素，继续拼接成字符串
        collectWorkerArray = collectWorkerArray.join(', ');
      }
      // 删除的二次确认
      await message.delConfirm()
      // 发起删除
      await ScreenPointApi.deleteScreenPointCollect(collectWorkerArray, formData.value.id, deletedData, 6)
      message.success(t('common.delSuccess'))
      // 刷新列表
      formData.value = await ScreenPointApi.getScreenPoint(formData.value.id)
      // 心电组
      if (formData.value.electrocardiogramWorker != null) {
        loading.value = true
        try {
          const experimentWorkerArray = formData.value.electrocardiogramWorker.split(', ').map(Number)
          electrocardiogramList.value = await ScreenPointApi.resolveCollectList(experimentWorkerArray)
        } finally {
          loading.value = false
        }
      }
    }

    // 诊断组
    if (activeName.value === 'diagnoGroup'){
      let collectWorkerArray = formData.value.diagnosisWorker.split(', ').map(Number)
      // 找到需要删除的数据
      let deletedData = collectWorkerArray.filter(workerId => workerId === deleteId);
      // 留下的数据
      collectWorkerArray = collectWorkerArray.filter(workerId => workerId !== deleteId);
      if (collectWorkerArray.length === 0) {
        // 如果没有剩余元素，可以设置一个默认值，或者做其他处理
        collectWorkerArray = "0"; // 或者其他操作
      } else {
        // 如果有剩余元素，继续拼接成字符串
        collectWorkerArray = collectWorkerArray.join(', ');
      }
      // 删除的二次确认
      await message.delConfirm()
      // 发起删除
      await ScreenPointApi.deleteScreenPointCollect(collectWorkerArray, formData.value.id, deletedData, 7)
      message.success(t('common.delSuccess'))
      // 刷新列表
      formData.value = await ScreenPointApi.getScreenPoint(formData.value.id)
      // 诊断组
      if (formData.value.diagnosisWorker != null) {
        loading.value = true
        try {
          const experimentWorkerArray = formData.value.diagnosisWorker.split(', ').map(Number)
          diagnoList.value = await ScreenPointApi.resolveCollectList(experimentWorkerArray)
        } finally {
          loading.value = false
        }
      }
    }

    // 队长回显
    if (formData.value.worker !== undefined) {
      const collectWorkerNickname = formData.value.worker.split(', ');
      formData.value.workers = collectWorkerNickname.map(id => {
        const user = userList.value.find(item => item.id.toString() === id);
        return user ? user.nickname : null; // 如果找到了对应的用户，则返回其昵称，否则返回 null
      }).filter(nickname => nickname !== null).join(', ');
      // 队长单位
      deptName.value = collectWorkerNickname.map(id => {
        const user = userList.value.find(item => item.id.toString() === id)
        return user ? user.deptName : null
      }).filter(deptName => deptName !== null)
      // 队长手机号码
      mobile.value = collectWorkerNickname.map(id => {
        const user = userList.value.find(item => item.id.toString() === id)
        return user ? user.mobile : null
      }).filter(mobile => mobile !== null)
    }

  } catch {}
}

const copyDeptList = reactive([])
/**
 * 部门列表
 */
const getDeptList = () => {
  ScreenPointApi.getDeptList().then(data =>{
    deptList.value = data;
    copyDeptList.splice(0, copyDeptList.length, ...data)
  })
}

const PinyinTown = (val) => {
  if (val) {
    const result = []
    deptList.value.forEach((i) => {
      const m = PinyinMatch.match(i.name, val)
      if (m) {
        result.push(i)
      }
    })
    deptList.value.splice(0, deptList.value.length, ...result)
  } else {
    // 如果没有输入，则还原列表
    deptList.value.splice(0, deptList.value.length, ...copyDeptList)
  }
}


/** 重置表单 */
const resetForm = () => {
  formData.value = {
    name: undefined,
    year: undefined,
    screenDept: undefined,
    worker: undefined,
    collectWorker: undefined,
    collectWorkers: undefined,
    ppdWorker: undefined,
    ppdWorkers: undefined,
    drctWorker: undefined,
    drctWorkers: undefined,
    sputumWorker: undefined,
    sputumWorkers: undefined,
    experimentWorker: undefined,
    experimentWorkers: undefined,
    electrocardiogramWorker: undefined,
    electrocardiogramWorkers: undefined,
    diagnosisWorker: undefined,
    diagnosisWorkes: undefined,
  }
  formRef.value?.resetFields()
}

const userRole = ref([])
const getUserRole  = async () => {
  userRole.value = await UserApi.getUserRole();
}

// 使用 computed 计算属性来动态计算 isSuperAdmin
const isHavePower = computed(() => {
  // 确保 userRole.value 是数组，且包含 'super_admin' 或 'county_manager' 元素
  return userRole.value.includes('super_admin') || userRole.value.includes('county_manager')
    || userRole.value.includes('city_manager');
});


/** 初始化 */
onMounted(() => {
  getDeptList()
  getUserRole()
})
</script>
