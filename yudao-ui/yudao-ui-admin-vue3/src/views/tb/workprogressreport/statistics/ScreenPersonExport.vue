<template>
  <Dialog v-model="dialogVisible" style="min-width: 60%;" :show-overflow-tooltip="true">
    <template #title>
      <span style="font-weight: bold">导出表格</span>
    </template>

    <el-form
        ref="formRef"
        :model="formData"
        label-width="100px"
        v-loading="formLoading"
        :rules="formRules"
      >
        <el-row>
          <el-col :span="11">
            共选中 <span style="color: #295ed1;margin-left: 10px;margin-right: 10px">{{
              personInfo.length
            }}</span>
            条人员数据
            <el-button @click="historyOptions" style="margin-left: 20px">
              <Icon icon="fa:history" class="mr-5px"/>
              历史选项
            </el-button>
            <el-button @click="resetFormData">
              <Icon icon="fa:trash" class="mr-5px"/>
              清空已选
            </el-button>
            <div style="margin-top: 10px">
              <div style="color: #295ed1;">第一步 表格标题</div>
              <el-form-item prop="tableTittle">
                <el-input v-model="formData.tableTittle" style="max-width: 300px;margin-top: 10px;margin-left: -100px"
                          placeholder="输入内容"/>
              </el-form-item>
            </div>
            <div style="margin-top: 20px">
              <div style="margin-bottom: 20px;"><span style="color: #295ed1;"> 第二步 基本数据 </span>
                <text>已选择 <span style="color: #295ed1;margin-left: 10px;margin-right: 10px">
                  {{combinedLength }}/{{ 7 }}</span>项基本数据
                </text>
              </div>
              <el-checkbox-group v-model="formData.selectSchool">
                <div class="form-list">
                  <el-checkbox label="学校名称" :value="1"/>
                  <div style="margin-left: 20px">
                    <el-form-item prop="school">
                      <el-select
                        v-model="formData.school"
                        placeholder="请选择，不选则留空"
                        size="large"
                        style="width: auto; min-width: 200px;margin-left: -100px"
                      >
                        <el-option
                          v-for="item in options"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value"
                        />
                      </el-select>
                    </el-form-item>
                  </div>
                </div>
              </el-checkbox-group>
              <el-checkbox-group v-model="formData.selectHospital">
                <div class="form-list">
                  <el-checkbox label="医院名称" :value="1"/>
                  <div style="margin-left: 20px">
                    <el-form-item prop="hospital">
                      <el-select
                        v-model="formData.hospital"
                        placeholder="请选择，不选则留空"
                        size="large"
                        style="width: auto; min-width: 200px;margin-left: -100px"
                      >
                        <el-option
                          v-for="item in options"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value"
                        />
                      </el-select>
                    </el-form-item>
                  </div>
                </div>
              </el-checkbox-group>
              <el-checkbox-group v-model="formData.selectDistrict">
                <div class="form-list">
                  <el-checkbox label="行政区划" :value="1"/>
                  <div style="margin-left: 20px">
                    <el-form-item prop="district">
                      <el-select
                        v-model="formData.district"
                        placeholder="请选择，不选则留空"
                        size="large"
                        style="width: auto; min-width: 200px;margin-left: -100px"
                      >
                        <el-option
                          v-for="item in options"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value"
                        />
                      </el-select>
                    </el-form-item>
                  </div>
                </div>
              </el-checkbox-group>
              <el-checkbox-group v-model="formData.selectContact" >
                <div class="form-list">
                  <el-checkbox label="联系人" :value="1"/>
                  <div style="margin-left: 32px">
                    <el-form-item prop="contact">
                      <el-select
                        v-model="formData.contact"
                        placeholder="请选择，不选则留空"
                        size="large"
                        style="width: auto; min-width: 200px;margin-left: -100px"
                      >
                        <el-option
                          v-for="item in options"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value"
                        />
                      </el-select>
                    </el-form-item>
                  </div>
                </div>
              </el-checkbox-group>
              <el-checkbox-group v-model="formData.selectContactPhone">
                <div class="form-list">
                  <el-checkbox label="联系电话" :value="1"/>
                  <div style="margin-left: 20px">
                    <el-form-item prop="contactPhone">
                      <el-select
                        v-model="formData.contactPhone"
                        placeholder="请选择，不选则留空"
                        size="large"
                        style="width: auto;min-width: 200px;margin-left: -100px;"
                      >
                        <el-option
                          v-for="item in options"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value"
                        />
                      </el-select>
                    </el-form-item>
                  </div>
                </div>
              </el-checkbox-group>
              <el-checkbox-group v-model="formData.selectInjectionPeople" >
                <div class="form-list">
                  <el-checkbox label="注射人" :value="1"/>
                  <div style="margin-left: 32px">
                    <el-form-item prop="injectionPeople">
                      <el-select
                        v-model="formData.injectionPeople"
                        placeholder="请选择，不选则留空"
                        size="large"
                        style="width: auto; min-width: 200px;margin-left: -100px;"
                      >
                        <el-option
                          v-for="item in options"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value"
                        />
                      </el-select>
                    </el-form-item>
                  </div>
                </div>
              </el-checkbox-group>
              <el-checkbox-group v-model="formData.selectCheckPeople">
                <div class="form-list">
                  <el-checkbox label="查验人" :value="1"/>
                  <div style="margin-left: 32px">
                    <el-form-item prop="checkPeople">
                      <el-input v-model="formData.checkPeople"
                                placeholder="输入查验人"
                                clearable
                                style="margin-left: -100px;min-width: 200px"/>
                    </el-form-item>

                  </div>
                </div>
              </el-checkbox-group>
            </div>
          </el-col>

          <el-col :span="12" style="margin-top: 15px;margin: 0 auto">
            <span style="color: #295ed1;">第三步 报表数据</span> 已选择 <span
            style="color: #295ed1;margin-left: 10px;margin-right: 10px">{{ combinedReportLength  }}/{{ 20 }} </span>个报表数据项
            <div>
              <h5>基本信息</h5>
              <el-checkbox-group v-model="formData.infoList">
                <el-checkbox label="序号" :value="1"/>
                <el-checkbox label="单位(学校)" :value="2"/>
                <el-checkbox label="班级" :value="3"/>
                <el-checkbox label="姓名" :value="4"/>
                <el-checkbox label="年龄" :value="5"/>
                <el-checkbox label="身份证号码" :value="6"/>
              </el-checkbox-group>
            </div>
            <div>
              <h5>PPD/EC检查</h5>
              <el-checkbox-group v-model="formData.ppdList">
                <el-checkbox label="时间" :value="1"/>
                <el-checkbox label="是否已做" :value="2"/>
                <el-checkbox label="横径mm" :value="3"/>
                <el-checkbox label="纵径mm" :value="4"/>
                <el-checkbox label="硬结平均直径mm" :value="5"/>
                <el-checkbox label="硬结平均直径是否<15mm" :value="6"/>
                <el-checkbox label="双拳、水泡、坏死或淋巴炎" :value="7"/>
                <el-checkbox label="PPD判读结果" :value="8"/>
              </el-checkbox-group>
            </div>
            <div>
              <h5>X光胸片</h5>
              <el-checkbox-group v-model="formData.xRayList">
                <el-checkbox label="时间" :value="1"/>
                <el-checkbox label="是否已做" :value="2"/>
                <el-checkbox label="结果" :value="3"/>
              </el-checkbox-group>
            </div>
            <div>
              <h5>最终结果</h5>
              <el-checkbox-group v-model="formData.result">
                <el-checkbox label="是否确诊" :value="1"/>
              </el-checkbox-group>
            </div>
            <div>
              <h5>备注</h5>
              <el-checkbox-group v-model="formData.remark">
                <el-checkbox label="未筛查原因：1.近1月打了疫苗；2.过敏体质；3：其它疾病(详述)" :value="1"/>
                <el-checkbox label="其他备注" :value="2"/>
              </el-checkbox-group>
            </div>
          </el-col>
        </el-row>

        <div style="display: flex;margin-top: 30px;margin-left: 42.5%">
          <el-button
            type="warning"
            plain
            @click="handleExport( )"
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
import {ScreenPersonApi} from '@/api/tb/screenpersonrealsituation'
import {onMounted, ref, reactive, computed} from 'vue'
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
  tableTittle: undefined,
  selectSchool: [],
  school: undefined,
  selectHospital: [],
  hospital: undefined,
  selectDistrict: [],
  district: undefined,
  selectContact: [],
  contact: undefined,
  selectContactPhone: [],
  contactPhone: undefined,
  selectInjectionPeople: [],
  injectionPeople: undefined,
  selectCheckPeople: [],
  checkPeople: undefined,
  infoList: [],
  ppdList: [],
  xRayList: [],
  result: [],
  remark: []
})
const personInfo = ref([]) // 所选人员列表

const formRef = ref() // 表单 Ref
const exportLoading = ref(false) // 导出的加载中


const formRules = reactive({

});

/** 打开弹窗 */
const open = async (ids: any) => {
  personInfo.value = ids
  dialogVisible.value = true
  resetFormData()
  // 修改时，设置数据
  try {


  } finally {
    formLoading.value = false
  }

}
defineExpose({open}) // 提供 open 方法，用于打开弹窗

/** 导出按钮操作 */
const handleExport = async () => {

  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 合并 formData 和 personInfo
    const params = {
      ...formData.value,
      personInfo: personInfo.value
    };
    console.log("params:", params)

    // 发起导出
    exportLoading.value = true
    const data = await ScreenPersonApi.exportStatistics(params)
    download.excel(data, '统计表.xls')
  } catch (error) {
    message.error(error);
  } finally {
    exportLoading.value = false
  }
}

// 基本数据、报表数据动态计算
const calculateLength = (type) => {
  let arrays = [];

  if (type === 'select') {
    arrays = [
      formData.value.selectHospital,
      formData.value.selectSchool,
      formData.value.selectDistrict,
      formData.value.selectContact,
      formData.value.selectContactPhone,
      formData.value.selectInjectionPeople,
      formData.value.selectCheckPeople
    ];
  } else if (type === 'report') {
    arrays = [
      formData.value.infoList,
      formData.value.ppdList,
      formData.value.xRayList,
      formData.value.remark,
      formData.value.result
    ];
  }

  return arrays.reduce((total, array) => total + (array ? array.length : 0), 0);
};

// 计算属性：计算选择项的总长度
const combinedLength = computed(() => calculateLength('select'));

// 计算属性：计算报告数据的总长度
const combinedReportLength = computed(() => calculateLength('report'));

// 重置表单
const resetFormData = () => {
  formData.value = {
    tableTittle: undefined,
    selectSchool: [],
    school: undefined,
    selectHospital: [],
    hospital: undefined,
    selectDistrict: [],
    district: undefined,
    selectContact: [],
    contact: undefined,
    selectContactPhone: [],
    contactPhone: undefined,
    selectInjectionPeople: [],
    injectionPeople: undefined,
    selectCheckPeople: [],
    checkPeople: undefined,
    infoList: [],
    ppdList: [],
    xRayList: [],
    result: [],
    remark: []
  };
}

/** 初始化 **/
onMounted(() => {

})
</script>
<style scoped lang="scss">
.form-list {
  display: flex;
  margin-top: 2px;
}

.el-checkbox-group {
  display: flex;
  flex-wrap: wrap;
}
</style>
