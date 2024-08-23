<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" style="width: 1400px"
          :close-on-click-modal="false">
    <!-- 搜索 -->
    <ContentWrap>
      <el-form
        class="-mb-15px"
        :model="queryParams"
        ref="queryFormRef"
        :inline="true"
        label-width="68px"
      >
        <el-form-item label="用户姓名" prop="nickname">
          <el-input
            v-model="queryParams.nickname"
            placeholder="请输入用户姓名"
            clearable
            @keyup.enter="handleQuery"
            class="!w-240px"
          />
        </el-form-item>
        <el-form-item label="手机号码" prop="mobile">
          <el-input
            v-model="queryParams.mobile"
            placeholder="请输入手机号码"
            clearable
            @keyup.enter="handleQuery"
            class="!w-240px"
          />
        </el-form-item>
        <el-form-item label="单位" prop="deptId">
          <el-select v-model="queryParams.deptId"
                     clearable
                     class="!w-200px"
          >
            <el-option
              v-for="item in deptList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button @click="handleQuery">
            <Icon icon="ep:search"/>
            搜索
          </el-button>
          <el-button @click="resetQuery">
            <Icon icon="ep:refresh"/>
            重置
          </el-button>
          <el-button
            type="success"
            plain
            :loading="distributeLoading"
            @click="handleDistriScreenPoint"
          >
            <Icon icon="ep:notification" class="mr-5px"/>
            分配
          </el-button>
        </el-form-item>

      </el-form>
    </ContentWrap>
    <ContentWrap>
      <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" fixed="left"/>
        <el-table-column label="是否已分配" align="center">
          <template #default="scope">
            <span :style="{ color: oldUserList.includes(scope.row.id.toString()) ? 'red' : 'blue' }">
              {{ oldUserList.includes(scope.row.id.toString()) ? "已分配至该筛查点" : "未分配至该筛查点" }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="编号" align="center" key="id" prop="id"/>
<!--        <el-table-column
          label="登录账号"
          align="center"
          prop="username"
          :show-overflow-tooltip="true"
        />-->
        <el-table-column
          label="姓名"
          align="center"
          prop="nickname"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="所在单位"
          align="center"
          key="deptName"
          prop="deptName"
          :show-overflow-tooltip="true"
        />
        <el-table-column label="手机号码" align="center" prop="mobile" width="120"/>
      </el-table>
      <Pagination
        :total="total"
        v-model:page="queryParams.pageNo"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    </ContentWrap>

    <!-- 分配角色 -->
    <UserAssignRoleForm ref="assignRoleFormRef" @success="makeValue"/>
  </Dialog>

</template>
<script lang="ts" setup>
import * as UserApi from '@/api/system/user'
import UserAssignRoleForm from '@/views/system/user/UserAssignRoleForm.vue'
import {onMounted, ref, reactive, inject, provide} from 'vue'
import Dialog from "@/components/Dialog/src/Dialog.vue"
import {ScreenPointApi} from "@/api/tb/screenpoint"
import * as PermissionApi from '@/api/system/permission'


defineOptions({name: 'DistributeRole'})

const message = useMessage() // 消息弹窗
const {t} = useI18n() // 国际化

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  username: undefined,
  nickname: undefined,
  mobile: undefined,
  status: undefined,
  deptId: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单

const workerList = inject('workerList'); // 使用 inject 获取父组件提供的数据
const workerRoleList = inject('workerRoleList'); // 使用 inject 获取父组件提供的数据

const workersList = ref()
provide('workersList', workersList); // 使用 inject 获取父组件提供的数据
const workersRoleList = ref()
provide('workersRoleList', workersRoleList); // 使用 inject 获取父组件提供的数据

const CapId = inject('capId')

const oneRoles = ref()
const deptList = ref([])
const userRole = ref()
const userId = ref()
const oldCapId = ref()

interface Ids {
  id: undefined,
}

const tempOldId = ref()
const nowPointId = ref()
const screenPointId = ref()
// 待 分配筛查点的人员id
const waitCapIds = ref([])
const workerYear = ref()
const oldUserList = ref([])

const props = defineProps({
  collectWorker: String,
  ppdWorker: String,
  drctWorker: String,
/*  sputumWorker: String,
  experimentWorker: String,
  electrocardiogramWorker: String,
  diagnosisWorker: String,*/
  worker: String
})

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ScreenPointApi.getUserPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

const Type = ref()
const open = async (type: number, capId: number, pointId: number, year: number) => {
  Type.value = type
  workerYear.value = year
  waitCapIds.value = []
  screenPointId.value = pointId
  oldCapId.value = capId
  tempOldId.value = capId
  switch (type) {
    case 1:
      dialogTitle.value = "分配队长"
      break
    case 2:
      dialogTitle.value = "分配PPD组人员"
      break
    case 3:
      dialogTitle.value = "分配CT/DR组人员"
      break
/*    case 4:
      dialogTitle.value = "分配痰检组人员"
      break
    case 5:
      dialogTitle.value = "分配实验组人员"
      break
    case 6:
      dialogTitle.value = "分配心电图组人员"
      break
    case 7:
      dialogTitle.value = "分配诊断组人员"
      break*/
    case 8:
      dialogTitle.value = "分配采集组人员"
      break
    default:
      dialogTitle.value = "分配工作人员"
  }
  /*let userStrId = props.collectWorker + ', ' + props.ppdWorker + ', '
    + props.drctWorker + ', ' + props.sputumWorker + ', ' + props.worker + ', '
    + props.experimentWorker + ', ' + props.electrocardiogramWorker + ', ' + props.diagnosisWorker;*/
  let userStrId = props.collectWorker + ', ' + props.ppdWorker + ', '
    + props.drctWorker +  ', ' + props.worker;
  let userIds = userStrId.split(', ').map(item => item.trim());
  oldUserList.value = [...new Set(userIds)];

  queryParams.nickname = undefined
  queryParams.mobile = undefined
  queryParams.deptId = undefined
  list.value = []
  total.value = 0
  dialogVisible.value = true
  loading.value = false
}

defineExpose({open}) // 提供 open 方法，用于打开弹窗


/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  if (queryParams.nickname || queryParams.mobile || queryParams.deptId) {
    getList()
  }
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryParams.nickname = undefined
  queryParams.mobile = undefined
  queryParams.deptId = undefined
  list.value = []
  total.value = 0
  // queryFormRef.value?.resetFields()
  // handleQuery()
}

/** 处理部门被点击 */
const handleDeptNodeClick = async (row) => {
  queryParams.deptId = row.id
  await getList()
}

/** 操作分发 */
const handleCommand = (command: string, row: UserApi.UserVO) => {
  switch (command) {
    case 'handleRole':
      handleRole(row)
      break
    default:
      break
  }
}

/** 分配角色 */
const assignRoleFormRef = ref()
const handleRole = (row: UserApi.UserVO) => {
  if (userId.value === row.id && !userRole.value.includes("super_admin")) {
    return message.error("不能给自己分配角色！")
  }
  assignRoleFormRef.value.open(row)
}


const makeValue = async () => {

}

const getUserId = async () => {
  userId.value = await UserApi.getUserId();
}

const getUserRole = async () => {
  userRole.value = await UserApi.getUserRole();
}


const getDeptList = async () => {
  const allDeptList = await ScreenPointApi.getDeptList();
  // 去掉学校类型
  deptList.value = allDeptList.filter(dept => dept.type != 1);
}

const handleSelectionChange = (val: Ids[]) => {
  // 提取每个对象的 id 属性
  waitCapIds.value = val.map(item => item.id);
}

const distributeLoading = ref(false)
const handleDistriScreenPoint = async () => {
  if (waitCapIds.value.length < 1) {
    message.error("请先选择需要被分配的人员！")
    return
  }
  let roleName, roleId, userIdStr, groupUserIdStr = '';
  //处理分配队长
  if (Type.value == 1) {

    if (waitCapIds.value.length > 1) {
      message.error("请选择一名人员作为队长！")
      return
    }

    if (waitCapIds.value[0] === oldCapId.value) {
      message.error("该人员已经是队长了！")
      return
    }

    await message.confirm("是否确认将你所选择的人员的筛查点设置为队长？")

    CapId.value = waitCapIds.value[0]
    if (!isNaN(tempOldId.value) && screenPointId.value != undefined) {
      // 分新的队长角色
      let res = await ScreenPointApi.getUserRoleList(waitCapIds.value[0]);
      roleId = await PermissionApi.getCapitalRoleId();
      let userRoleMap = reactive(new Map());
      userRoleMap.set(waitCapIds.value[0], res);
      for (const [key, value] of userRoleMap) {
        // 将 collectRoleId 添加到 value 数组中
        let numericValue = value.map(Number);
        numericValue.push(roleId);
        // 分配权限
        await ScreenPointApi.assignUserRole({
          userId: parseInt(waitCapIds.value[0]),
          roleIds: numericValue
        });
      }
      // 分新的队长用户筛查点 // 删除原来的队长角色
      await ScreenPointApi.giveCapRole(waitCapIds.value[0], oldCapId.value, screenPointId.value)
      // 在用户筛查点表中写入队长
      await ScreenPointApi.setWorker(waitCapIds.value[0], screenPointId.value)
    }
    message.success("分配成功！")
    oldCapId.value = waitCapIds.value[0]
    // 假设你的弹窗组件的引用是 dialog
    dialogVisible.value = false; // 将弹窗的显示状态设置为 false，关闭弹窗
  } else if (Type.value === 8) {
    roleName = '采集组成员';
    roleId = await PermissionApi.getCollectRoleId();
    userIdStr = props.collectWorker;
  } else if (Type.value === 2) {
    roleName = 'PPD组成员';
    roleId = await PermissionApi.getPPDRoleId();
    userIdStr = props.ppdWorker;
  } else if (Type.value === 3){
    roleName = 'DR/CT组成员';
    roleId = await PermissionApi.getDRCTRoleId();
    userIdStr = props.drctWorker;
  }/*else if (Type.value === 4){
    roleName = '痰检组成员';
    roleId = await PermissionApi.getSputumRoleId();
    userIdStr = props.sputumWorker;
  }else if (Type.value === 5){
    roleName = '实验组成员';
    roleId = await PermissionApi.getExperimentRoleId();
    userIdStr = props.experimentWorker;
  } else if (Type.value === 6){
    roleName = '心电图组成员';
    roleId = await PermissionApi.getElectrocardiogramRoleId();
    userIdStr = props.electrocardiogramWorker;
  } else if (Type.value === 7){
    roleName = '诊断组成员';
    roleId = await PermissionApi.getDiagnosisRoleId();
    userIdStr = props.diagnosisWorker;
  }*/
  if (Type.value !== 1){
    const confirmationMessage = '是否确认将你所选择的人员的筛查点设置为 '+ `${roleName}` + ' ？';
    await message.confirm(confirmationMessage);
    let userRoleMap = reactive(new Map());
    for (let i = 0; i < waitCapIds.value.length; i++) {
      let userId = waitCapIds.value[i];
      let res = await ScreenPointApi.getUserRoleList(userId);
      userRoleMap.set(userId, res);
      for (const [key, value] of userRoleMap) {
        // 将 key 值添加到 collectUserIdStr 中，并用 `,` 连接
        groupUserIdStr += `${key}, `;
        // 将 collectRoleId 添加到 value 数组中
        let numericValue = value.map(Number);
        numericValue.push(roleId);
        // 分配权限
        await ScreenPointApi.assignUserRole({
          userId: parseInt(userId),
          roleIds: numericValue
        });
      }
      await ScreenPointApi.setUserScreenPoint(userId, screenPointId.value, roleId);
    }
    if (userIdStr != null && userIdStr != '0') {
      groupUserIdStr += userIdStr;
    }
    let userIds = groupUserIdStr.split(', ').map(item => item.trim());
    let uniqueUserIds = [...new Set(userIds)];
    groupUserIdStr = uniqueUserIds.join(', ');
    if (!(userIdStr != null && userIdStr != '0')) {
      groupUserIdStr = groupUserIdStr.slice(0, -2);
    }
    await ScreenPointApi.setCollect(groupUserIdStr, screenPointId.value, Type.value);
    message.success("分配成功！");
  }
  dialogVisible.value = false;
}

/** 初始化 */
onMounted(() => {
  getDeptList()
  getUserId()
  getUserRole()
})
</script>
