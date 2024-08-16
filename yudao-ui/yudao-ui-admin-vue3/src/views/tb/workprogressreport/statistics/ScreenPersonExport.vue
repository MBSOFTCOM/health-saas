<template>
  <Dialog v-model="dialogVisible" style="width: 60%; " :show-overflow-tooltip="true">
    <template #title>
      <span style="font-weight: bold">导出表格</span>
    </template>
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-row>
        <el-col :span="11">
          共选中 <span style="color: #295ed1;margin-left: 10px;margin-right: 10px">{{ 45 }}</span>
          条人员数据
          <el-button @click="historyOptions" style="margin-left: 30px">
            <Icon icon="fa:history" class="mr-5px"/>
            历史选项
          </el-button>
          <el-button @click="clearForm">
            <Icon icon="fa:trash" class="mr-5px"/>
            清空已选
          </el-button>
          <div style="margin-top: 10px">
            <div style="color: #295ed1;">第一步 表格标题</div>
            <el-input v-model="input" style="width: 240px;margin-top: 10px" placeholder="输入内容"/>
          </div>
          <div style="margin-top: 20px">
            <div><span style="color: #295ed1;"> 第二步 基本数据 </span>
              <text>已选择 <span style="color: #295ed1;margin-left: 10px;margin-right: 10px">{{2}}/{{6}}</span>项基本数据</text>
            </div>
            <el-checkbox-group v-model="checkList" style="margin-top: 15px">
              <div class="form-list">
                <el-checkbox label="学校名称" value="Value 1" />
                <div class="flex flex-wrap gap-4 items-center" style="margin-left: 20px">
                  <el-select
                    v-model="value"
                    placeholder="请选择，不选则留空"
                    size="large"
                    style="width: 240px"
                  >
                    <el-option
                      v-for="item in options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </div>
              </div>
            </el-checkbox-group>
            <el-checkbox-group v-model="checkList" style="margin-top: 15px">
            <div class="form-list">
              <el-checkbox label="医院名称" value="Value 1" />
              <div class="flex flex-wrap gap-4 items-center" style="margin-left: 20px">
                <el-select
                  v-model="value"
                  placeholder="请选择，不选则留空"
                  size="large"
                  style="width: 240px"
                >
                  <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </div>
          </el-checkbox-group>
            <el-checkbox-group v-model="checkList" style="margin-top: 15px">
            <div class="form-list">
              <el-checkbox label="行政区划" value="Value 1" />
              <div class="flex flex-wrap gap-4 items-center" style="margin-left: 20px">
                <el-select
                  v-model="value"
                  placeholder="请选择，不选则留空"
                  size="large"
                  style="width: 240px"
                >
                  <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </div>
          </el-checkbox-group>
            <el-checkbox-group v-model="checkList" style="margin-top: 15px">
            <div class="form-list">
              <el-checkbox label="联系人" value="Value 1" />
              <div class="flex flex-wrap gap-4 items-center" style="margin-left: 32px">
                <el-select
                  v-model="value"
                  placeholder="请选择，不选则留空"
                  size="large"
                  style="width: 240px"
                >
                  <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </div>
          </el-checkbox-group>
            <el-checkbox-group v-model="checkList" style="margin-top: 15px">
            <div class="form-list">
              <el-checkbox label="联系电话" value="Value 1" />
              <div class="flex flex-wrap gap-4 items-center" style="margin-left: 20px">
                <el-select
                  v-model="value"
                  placeholder="请选择，不选则留空"
                  size="large"
                  style="width: 240px"
                >
                  <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </div>
          </el-checkbox-group>
            <el-checkbox-group v-model="checkList" style="margin-top: 15px">
              <div class="form-list">
                <el-checkbox label="注射人" value="Value 1" />
                <div class="flex flex-wrap gap-4 items-center" style="margin-left: 32px">
                  <el-select
                    v-model="value"
                    placeholder="请选择，不选则留空"
                    size="large"
                    style="width: 240px"
                  >
                    <el-option
                      v-for="item in options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </div>
              </div>
            </el-checkbox-group>
            <el-checkbox-group v-model="checkList" style="margin-top: 15px">
              <div class="form-list">
                <el-checkbox label="查验人" value="Value 1" />
                <div class="flex flex-wrap gap-4 items-center" style="margin-left: 32px">
                  <el-select
                    v-model="value"
                    placeholder="请选择，不选则留空"
                    size="large"
                    style="width: 240px"
                  >
                    <el-option
                      v-for="item in options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </div>
              </div>
            </el-checkbox-group>
          </div>

          <br/>

        </el-col>

        <el-col :span="12" style="margin-top: 15px;margin: 0 auto">
          <span style="color: #295ed1;">第三步 报表数据</span> 已选择 <span
          style="color: #295ed1;margin-left: 10px;margin-right: 10px">{{ 11 }}/{{ 20 }} </span>个报表数据项
          <div>
            <h5>基本信息</h5>
            <el-checkbox-group v-model="checkList">
              <el-checkbox label="序号" value="Value 1" />
              <el-checkbox label="单位(学校)" />
              <el-checkbox label="班级" value="Value 1" />
              <el-checkbox label="姓名" />
              <el-checkbox label="年龄" value="Value 1" />
              <el-checkbox label="身份证号码" />
            </el-checkbox-group>
          </div>
          <div>
            <h5>PPD/EC检查</h5>
            <el-checkbox-group v-model="checkList">
              <el-checkbox label="时间" value="Value 1" />
              <el-checkbox label="是否已做" />
              <el-checkbox label="横径mm" value="Value 1" />
              <el-checkbox label="纵径mm" />
              <el-checkbox label="硬结平均直径mm" value="Value 1" />
              <el-checkbox label="硬结平均直径是否<15mm" />
              <el-checkbox label="双拳、水泡、坏死或淋巴炎" />
              <el-checkbox label="PPD判读结果" />
            </el-checkbox-group>
          </div>
          <div>
            <h5>X光胸片</h5>
            <el-checkbox-group v-model="checkList">
              <el-checkbox label="时间" value="Value 1" />
              <el-checkbox label="是否已做" />
              <el-checkbox label="结果" value="Value 1" />
            </el-checkbox-group>
          </div>
          <div>
            <h5>最终结果</h5>
            <el-checkbox-group v-model="checkList">
              <el-checkbox label="是否确诊" value="Value 1" />
            </el-checkbox-group>
          </div>
          <div>
            <h5>备注</h5>
            <el-checkbox-group v-model="checkList">
              <el-checkbox label="未筛查原因：1.近1月打了疫苗；2.过敏体质；3：其它疾病(详述)" value="Value 1" />
              <el-checkbox label="其他备注" value="Value 1" />
            </el-checkbox-group>
          </div>
        </el-col>
      </el-row>

      <div style="display: flex;margin-top: 30px;margin-left: 42.5%">
        <el-button
          type="warning"
          plain
          @click="handleExport( )"
          v-hasPermi="['tb:screen-person:export']"
          size="large"
          style="width: 150px"
        >
          <Icon icon="ep:download" class="mr-5px"/>
          导出表格
        </el-button>
      </div>

    </el-form>

  </Dialog>


</template>
<script setup lang="ts">
import {getIntDictOptions, DICT_TYPE} from '@/utils/dict'
import {ScreenPersonApi, ScreenPersonVO} from '@/api/tb/screenpersonrealsituation'
import {onMounted, ref, reactive} from 'vue'
import DictTag from "@/components/DictTag/src/DictTag.vue"
import download from "@/utils/download";


const activeName = ref('first')

/** 摸底患者信息 */
defineOptions({name: 'ScreenPersonDetail'})

const {t} = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const screenPersonData = ref([])
const formData = ref({
  id: undefined,
  idNum: undefined,
  name: undefined,
  age: undefined,
  tel: undefined,
  sex: undefined,
  firstType: undefined,
  moreType: undefined,
  isNewStudent: undefined,
  year: undefined,
  screenId: undefined,
  screenType: undefined
})

const formRef = ref() // 表单 Ref
const exportLoading = ref(false) // 导出的加载中



const checkList = ref([]) //采集组数据
const electList = ref([]) // 心电图组数据


/** 打开弹窗 */
const open = async () => {
  dialogVisible.value = true
  // 修改时，设置数据


  try {


  } finally {
    formLoading.value = false
  }

}
defineExpose({open}) // 提供 open 方法，用于打开弹窗

// 多人群分类
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

</script>
<style scoped lang="scss">
.form-list{
  display: flex;
  align-items: center;
}
.el-checkbox-group{
  display: flex;
  flex-wrap: wrap;
}
</style>
