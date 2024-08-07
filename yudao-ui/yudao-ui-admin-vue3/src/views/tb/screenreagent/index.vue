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
      <el-form-item label="试剂名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入试剂名称"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="试剂类型" prop="type">
        <el-select
          v-model="queryParams.type"
          placeholder="请选择试剂类型"
          clearable
          class="!w-180px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.DOSAGE_FORM)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="是否启用" prop="usable">
        <el-select
          v-model="queryParams.usable"
          placeholder="用户状态"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="供应商" prop="manufacturer">
        <el-input
          v-model="queryParams.manufacturer"
          placeholder="请输入供应商"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
        <el-button
          type="primary"
          plain
          @click="openForm('create')"
          v-hasPermi="['tb:screen-reagent:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['tb:screen-reagent:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column type="index" label="序号" align="center" width="70"
                       :show-overflow-tooltip="false" fixed="left"/>
      <el-table-column label="试剂名称" align="center" prop="name" />
      <el-table-column label="试剂类型" align="center" prop="type">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.DOSAGE_FORM" :value="scope.row.type"/>
        </template>
      </el-table-column>
      <el-table-column label="转换系数" align="center" prop="reagentSpecsNum" >
        <template #default="scope">
          {{scope.row.reagentSpecsNum}}人份
        </template>
      </el-table-column>

      <el-table-column label="品规" align="center" >
        <template #default="scope">
          {{scope.row.titer}}{{resolveDict(scope.row.potencyUnit, DICT_TYPE.TB_POTENCY_UNIT)}}/{{scope.row.specification}}{{resolveDict(scope.row.specificationUnit, DICT_TYPE.TB_SPECIFICATION)}}/{{resolveDict(scope.row.packageUnit, DICT_TYPE.TB_PACKAGE)}}
        </template>
      </el-table-column>
      <el-table-column label="供应商" align="center" prop="manufacturer" />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['tb:screen-reagent:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['tb:screen-reagent:delete']"
          >
            删除
          </el-button>
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
  <ScreenReagentForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import download from '@/utils/download'
import { ScreenReagentApi, ScreenReagentVO } from '@/api/tb/screenreagent'
import ScreenReagentForm from './ScreenReagentForm.vue'
import {getIntDictOptions, DICT_TYPE} from '@/utils/dict'
import {onMounted, ref, reactive} from 'vue'



/** 试剂 列表 */
defineOptions({ name: 'ScreenReagent' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<ScreenReagentVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  name: undefined,
  type: undefined,
  reagentSpecsNum: undefined,
  usable: undefined,
  titer: undefined,
  potencyUnit: undefined,
  specification: undefined,
  specificationUnit: undefined,
  packageUnit: undefined,
  manufacturer: undefined,
  threshold: undefined,
  createTime: [],
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ScreenReagentApi.getScreenReagentPage(queryParams)
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
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await ScreenReagentApi.deleteScreenReagent(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await ScreenReagentApi.exportScreenReagent(queryParams)
    download.excel(data, '试剂.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

// 处理品规的回显
const resolveDict = (value, dict) => {
  let list = getIntDictOptions(dict)
  const item = list.find(item => item.value === value);

  if (item) {
    return item.label; // 返回找到的对象的 label 属性
  } else {
    return undefined; // 如果找不到对应的 value，可以返回 undefined 或者其他标识
  }
};

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
