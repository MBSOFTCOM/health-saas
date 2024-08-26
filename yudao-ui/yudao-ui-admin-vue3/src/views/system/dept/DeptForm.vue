<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="80px"
    >
      <el-form-item label="上级单位" prop="parentId">
        <el-tree-select
          v-model="formData.parentId"
          :data="deptTree"
          :props="defaultProps"
          check-strictly
          default-expand-all
          placeholder="请选择上级单位"
          value-key="deptId"
          @change="getDistrictCode(formData.parentId)"
        />
      </el-form-item>

      <el-row type="flex" justify="space-between">
        <el-col :span="11">
          <el-form-item label="单位名称" prop="name">
            <el-input v-model="formData.name" placeholder="请输入单位名称" />
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="负责人" prop="leaderUserId">
            <el-select v-model="formData.leaderUserId" clearable placeholder="请输入负责人">
              <el-option
                v-for="item in userList"
                :key="item.id"
                :label="item.nickname"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row type="flex" justify="space-between">
        <el-col :span="11">
          <el-form-item label="显示排序" prop="sort">
            <el-input-number v-model="formData.sort" :min="0" controls-position="right" />
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="状态" prop="status">
            <el-select v-model="formData.status" clearable placeholder="请选择状态">
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row type="flex" justify="space-between">
        <el-col :span="11">
          <el-form-item label="联系电话" prop="phone">
            <el-input v-model="formData.phone" maxlength="11" placeholder="请输入联系电话" />
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="formData.email" maxlength="50" placeholder="请输入邮箱" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="区划级别" prop="regionLevel">
        <el-radio-group v-model="formData.regionLevel" @change="getDistrictList(formData.regionLevel, districtCode)">
          <el-radio :value="0">省级</el-radio>
          <el-radio :value="1">市、州级</el-radio>
          <el-radio :value="2">区、县级</el-radio>
          <el-radio :value="3">乡、镇级</el-radio>
        </el-radio-group>
      </el-form-item>


      <el-row type="flex" justify="space-between">
        <el-col :span="11">
          <el-form-item label="所在区划" prop="districtCode">
            <el-select
              v-model="formData.districtCode"
              filterable
              :filter-method="PinyinProvince"
              placeholder="请选择所在区划"
              clearable
              class="!w-240px"
            >
              <el-option
                v-for="item in districtList"
                :key="item.code"
                :label="item.name"
                :value="item.code"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="单位类型" prop="type" label-width="120">
            <el-select v-model="formData.type" placeholder="请选择单位类型">
              <el-option :value="1" label="学校"/>
              <el-option :value="2" label="医疗机构"/>
              <el-option :value="3" label="管理机构"/>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>


    </el-form>
    <template #footer>
      <el-button type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { defaultProps, handleTree } from '@/utils/tree'
import * as DeptApi from '@/api/system/dept'
import * as UserApi from '@/api/system/user'
import { CommonStatusEnum } from '@/utils/constants'
import { FormRules } from 'element-plus'
import {ScreenPointApi} from "@/api/tb/screenpoint";
import PinyinMatch from "pinyin-match";
import {reactive} from "vue";
import {ScreenDistrictApi} from "@/api/tb/screendistrict";

defineOptions({ name: 'SystemDeptForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  title: '',
  parentId: undefined,
  name: undefined,
  sort: undefined,
  leaderUserId: undefined,
  phone: undefined,
  email: undefined,
  status: CommonStatusEnum.ENABLE,

  regionLevel: undefined,
  districtCode: undefined,
  type: undefined
})
const districtList = ref([]) // 区划列表
const copyDistrictList = reactive([])
const districtCode = ref()

const formRules = reactive<FormRules>({
  parentId: [{required: true, message: '上级部门不能为空', trigger: 'blur'}],
  name: [{required: true, message: '部门名称不能为空', trigger: 'blur'}],
  sort: [{required: true, message: '显示排序不能为空', trigger: 'blur'}],
  email: [{type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change']}],
  phone: [
    {pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: '请输入正确的手机号码', trigger: 'blur'}
  ],
  status: [{required: true, message: '状态不能为空', trigger: 'blur'}],

  regionLevel: [{required: true, message: '区划级别不能为空', trigger: ['blur', 'change']}],
  districtCode: [{required: true, message: '所在区划不能为空', trigger: ['blur', 'change']}],
  type: [{required: true, message: '是否为学校不能为空', trigger: ['blur', 'change']}],
})
const formRef = ref() // 表单 Ref
const deptTree = ref() // 树形结构
const userList = ref<UserApi.UserVO[]>([]) // 用户列表

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  // 还原区划列表
  districtList.value = []
  copyDistrictList.length = 0

  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await DeptApi.getDept(id)
      // 开始
      const code = formData.value.districtCode
      await getDistrictCode(formData.value.parentId)
      await getDistrictList(formData.value.regionLevel, districtCode.value)
      formData.value.districtCode = code
      // 结束
    } finally {
      setTimeout(() => {
        // 在此处放置需要延迟执行的代码
        formLoading.value = false
      }, 100); // 1000 毫秒 = 1 秒
    }
  }
  // 获得用户列表
  userList.value = await UserApi.getSimpleUserList()
  // 获得部门树
  await getTree()
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as DeptApi.DeptVO
    if (formType.value === 'create') {
      await DeptApi.createDept(data)
      message.success(t('common.createSuccess'))
    } else {
      // await DeptApi.updateDept(data)
      await ScreenPointApi.updateDept(data)
      message.success(t('common.updateSuccess'))
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
    title: '',
    parentId: undefined,
    name: undefined,
    sort: undefined,
    leaderUserId: undefined,
    phone: undefined,
    email: undefined,
    status: CommonStatusEnum.ENABLE,
    regionLevel: undefined,
    districtCode: undefined,
    type: undefined
  }
  formRef.value?.resetFields()
}

/** 获得部门树 */
const getTree = async () => {
  deptTree.value = []
  const data = await DeptApi.getSimpleDeptList()
  let dept: Tree = { id: 0, name: '顶级部门', children: [] }
  dept.children = handleTree(data)
  deptTree.value.push(dept)
}


const getDistrictList = async (level, parentCode) => {
  formData.value.districtCode = undefined;
  districtList.value = await ScreenDistrictApi.getDistrictList(level, parentCode)
  copyDistrictList.splice(0, copyDistrictList.length, ...districtList.value);
}

const PinyinProvince = (val) => {
  if (val) {
    const result = []
    districtList.value.forEach((i) => {
      const m = PinyinMatch.match(i.name, val)
      if (m) {
        result.push(i)
      }
    })
    districtList.value.splice(0, districtList.value.length, ...result)
  } else {
    // 如果没有输入，则还原列表
    districtList.value.splice(0, districtList.value.length, ...copyDistrictList)
  }
}


const getDistrictCode = async (deptId) => {
  districtCode.value = await ScreenDistrictApi.getDistrictCode(deptId)
}
</script>
