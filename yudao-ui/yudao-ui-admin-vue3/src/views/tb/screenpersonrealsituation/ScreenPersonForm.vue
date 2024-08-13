<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-row type="flex" justify="space-between">
        <el-col :span="11">
          <el-form-item label="姓名" prop="name">
            <el-input v-model="formData.name" placeholder="请输入姓名"/>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="联系电话" prop="tel">
            <el-input v-model="formData.tel" placeholder="请输入联系电话"/>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row type="flex" justify="space-between">
        <el-col :span="11">
          <el-form-item label="身份证号" prop="idNum">
            <el-input v-model="formData.idNum" placeholder="请输入身份证号"/>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="民族" prop="nation" label-width="120">
            <el-select
              v-model="formData.nation"
              filterable
              :filter-method="PinyinMatchFun"
              placeholder="请选择民族"
              clearable
            >
              <el-option
                v-for="item in ethnicList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row type="flex" justify="space-between">
        <el-col :span="11">
          <el-form-item label="性别" prop="sex" >
            <el-select
              v-model="formData.sex"
              placeholder="根据身份证自动获取"
              clearable
              class="!w-200px"
              :disabled="formType == 'create' ? true : false"
            >
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.PATIENT_SEX)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"/>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="年龄" prop="age">
            <el-input v-model="formData.age" type="number" placeholder="根据身份证自动获取" :disabled="formType == 'create' ? true : false"/>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row type="flex" justify="space-between">
        <el-col :span="11">
          <el-form-item label="身高(cm)" prop="height">
            <el-input v-model="formData.height" placeholder="请输入身高"/>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="体重(kg)" prop="weight">
            <el-input v-model="formData.weight" placeholder="请输入体重"/>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row type="flex" justify="space-between">
        <el-col :span="11">
          <el-tooltip
            effect="dark"
            :content="formData.permanentAddress"
            placement="top"
            :disabled="formType == 'create' ? true : false"
          >
            <el-form-item label="户籍地址" prop="permanentAddress">
              <el-input v-model="formData.permanentAddress" placeholder="请输入户籍地址"/>
            </el-form-item>
          </el-tooltip>
        </el-col>
        <el-col :span="11">
          <el-tooltip
            effect="dark"
            :content="formData.address"
            placement="top"
            :disabled="formType == 'create' ? true : false"
          >
            <el-form-item label="现住址" prop="address">
              <el-input v-model="formData.address" placeholder="请输入现住址"/>
            </el-form-item>
          </el-tooltip>
        </el-col>
      </el-row>

      <el-row type="flex" justify="space-between">
        <el-col :span="11">
          <el-form-item label="户籍地址-省" prop="permanentAddressProvince">
            <el-select
              v-model="formData.permanentAddressProvince"
              filterable
              :filter-method="PinyinProvince"
              placeholder="请选择省"
              clearable
              class="!w-240px"
              @change="getCityList2(formData.permanentAddressProvince)"
            >
              <el-option
                v-for="item in provinceList"
                :key="item.code"
                :label="item.name"
                :value="item.code"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="现住址-省" prop="province">
            <el-select
              v-model="formData.province"
              filterable
              :filter-method="PinyinProvince"
              placeholder="请选择省"
              clearable
              class="!w-240px"
              @change="getCityList(formData.province)"
            >
              <el-option
                v-for="item in provinceList"
                :key="item.code"
                :label="item.name"
                :value="item.code"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row type="flex" justify="space-between">
        <el-col :span="11">
          <el-form-item label="户籍地址-市" prop="permanentAddressCity">
            <el-select
              v-model="formData.permanentAddressCity"
              filterable
              :filter-method="PinyinCity2"
              placeholder="请选择市"
              clearable
              class="!w-240px"
              @change="getCountyList2(formData.permanentAddressCity)"
            >
              <el-option
                v-for="item in cityList2"
                :key="item.code"
                :label="item.name"
                :value="item.code"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="现住址-市" prop="city">
            <el-select
              v-model="formData.city"
              filterable
              :filter-method="PinyinCity"
              placeholder="请选择市"
              clearable
              class="!w-240px"
              @change="getCountyList(formData.city)"
            >
              <el-option
                v-for="item in cityList"
                :key="item.code"
                :label="item.name"
                :value="item.code"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row type="flex" justify="space-between">
        <el-col :span="11">
          <el-form-item label="户籍地址-县" prop="permanentAddressCounty">
            <el-select
              v-model="formData.permanentAddressCounty"
              filterable
              :filter-method="PinyinCounty2"
              placeholder="请选择县"
              clearable
              class="!w-240px"
              @change="getTownList2(formData.permanentAddressCounty)"
            >
              <el-option
                v-for="item in countyList2"
                :key="item.code"
                :label="item.name"
                :value="item.code"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="现住址-县" prop="county">
            <el-select
              v-model="formData.county"
              filterable
              :filter-method="PinyinCounty"
              placeholder="请选择县"
              clearable
              class="!w-240px"
              @change="getTownList(formData.county)"
            >
              <el-option
                v-for="item in countyList"
                :key="item.code"
                :label="item.name"
                :value="item.code"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row type="flex" justify="space-between">
        <el-col :span="11">
          <el-form-item label="户籍地址-乡镇" prop="permanentAddressTown" label-width="115">
            <el-select
              v-model="formData.permanentAddressTown"
              filterable
              :filter-method="PinyinTown2"
              placeholder="请选择乡镇"
              clearable
              class="!w-240px"
            >
              <el-option
                v-for="item in townList2"
                :key="item.code"
                :label="item.name"
                :value="item.code"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="现住址-乡镇" prop="town">
            <el-select
              v-model="formData.town"
              filterable
              :filter-method="PinyinTown"
              placeholder="请选择乡镇"
              clearable
              class="!w-240px"
            >
              <el-option
                v-for="item in townList"
                :key="item.code"
                :label="item.name"
                :value="item.code"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row type="flex" justify="space-between">
        <el-col :span="11">
          <el-form-item label="第一人群分类" prop="firstType" label-width="120">
            <el-select v-model="formData.firstType" @change="changeMoreType">
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.FIRST_TYPE)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item
            label="多人群分类"
            prop="moreType"
            v-if="formData.firstType === 1"
          >
            <el-select
              v-model="formData.moreTempType"
              multiple
              :max-collapse-tags="2"
              collapse-tags
              collapse-tags-tooltip
            >
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.MORE_TYPE)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"

              />
            </el-select>
          </el-form-item>
          <el-form-item
            label="多人群分类"
            prop="moreType"
            v-if="formData.firstType === 4"
          >
            <el-select
              v-model="formData.moreTempType"
              multiple
              :max-collapse-tags="2"
              collapse-tags
              collapse-tags-tooltip
            >
              <el-option
                v-for="dict in newDict"
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
          <el-form-item
            label="是否为新生"
            prop="isNewStudent"
            v-if="formData.firstType == 1 && formData.moreTempType.includes(1)"
          >
            <el-radio-group v-model="formData.isNewStudent">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0" style="margin-left: -22px">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item
            label="班级"
            prop="classroom"
            v-if="formData.firstType == 1 && formData.moreTempType.includes(1)"
          >
            <el-input v-model="formData.classroom" placeholder="请输入班级"/>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row type="flex" justify="space-between">
        <el-col :span="11">
          <el-form-item label="监护人手机号" prop="guardianTel" label-width="120" v-if="formData.firstType == 1 && formData.moreTempType.includes(1)">
            <el-input v-model="formData.guardianTel" placeholder="请输入监护人手机号"/>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="学生类型" prop="studentType" label-width="120" v-if="formData.firstType == 1 && formData.moreTempType.includes(1)">
          <el-select
            v-model="formData.studentType"
            collapse-tags
            clearable
          >
            <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.STUDENT_TYPE)"
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
          <el-form-item
            label="单位"
            prop="schoolOrTemple"
          >
            <el-input v-model="formData.schoolOrTemple" placeholder="请输入单位"/>
          </el-form-item>
        </el-col>

        <el-col :span="11">
          <el-form-item label="是否需筛查" prop="isNew">
            <el-radio-group v-model="formData.isNew">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0" style="margin-left: -22px">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row type="flex" justify="space-between">
        <el-col :span="10">
          <el-form-item
            label="筛查点"
            prop="screenPoint"
          >
            <el-select v-model="formData.screenPoint"
                       :filter-method="PinyinScreenPoint"
                       clearable
                       filterable
                       placeholder="请选择筛查点"
            >
              <el-option
                v-for="item in pointList"
                :key="item"
                :label="item"
                :value="item"/>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="筛查时间" prop="timeRange" label-width="80">
            <el-date-picker
              v-model="formData.timeRange"
              type="daterange"
              range-separator="-"
              start-placeholder="开始"
              end-placeholder="结束"
              :size="'default'"
              @change="formatTime(formData.timeRange)"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row type="flex" justify="space-between">
        <el-col :span="11">
          <el-form-item
            label="工作年度"
            prop="year"
          >
            <el-input-number v-model="formData.year" clearable :disabled="formType == 'update' ? true : false" />
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="筛查类型" prop="screenType" label-width="120" >
            <el-select
              v-model="formData.screenType"
              placeholder="请选择筛查类型"
              clearable
              class="!w-180px"
              :disabled="formType == 'update' ? true : false"
            >
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.TB_SCREEN_TYPE)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="备注" prop="remark">
        <el-input v-model="formData.remark" placeholder="请输入备注" type="textarea"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import PinyinMatch from 'pinyin-match'
import {getIntDictOptions, DICT_TYPE} from '@/utils/dict'
import {ScreenPersonApi, ScreenPersonVO} from '@/api/tb/screenpersonrealsituation'
import {onMounted, ref, reactive, watch} from 'vue'
import {ScreenDistrictApi} from '@/api/tb/screendistrict'
import {ScreenPointApi} from "@/api/tb/screenpoint";
import moment from "moment";
import * as DeptApi from "@/api/system/dept";

/** 摸底 表单 */
defineOptions({name: 'ScreenPersonForm'})

const {t} = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  idNum: undefined,
  name: undefined,
  age: undefined,
  tel: undefined,
  sex: undefined,
  year: undefined,
  screenType: undefined,
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
  classroom: undefined,
  isNew: undefined,
  isNewStudent: undefined,
  isScreened: undefined,
  screenPoint: undefined,
  screenTime: undefined,
  moreTempType: [],
  studentType: undefined,
  screenStartTime: undefined,
  screenEndTime: undefined,
  guardianTel: undefined,
  timeRange: undefined,
})
const provinceList = ref([]) //省列表
const cityList = ref([]) //市列表
const countyList = ref([]) // 县列表
const townList = ref([]) //乡镇列表

const cityList2 = ref([]) //户籍市列表
const countyList2 = ref([]) // 户籍县列表
const townList2 = ref([]) //户籍乡镇列表

const copyTown2 = reactive([])
const copyTown = reactive([])
const copyCounty = reactive([])
const copyCounty2 = reactive([])
const copyCity2 = reactive([])
const copyCity = reactive([])


const villageList = ref([]) //村列表
const ethnicList = ref([]) // 民族列表
const newDict = ref([{
  "value": 8,
  "label": "密接者"
}, {
  "value": 16,
  "label": "糖尿病"
}, {
  "value": 64,
  "label": "既往患者"
},{
  "value": 128,
  "label": "HIV/AIDS"
}
])
const pointList = ref([])


// 身高的验证规则
const checkHeight = (rule, value) => {
  if (!value) {
    return Promise.resolve();
    // return Promise.reject('请输入身高');
  }else {
    const trimmedValue = String(value).trim(); // 将值转换为字符串并去除首尾空格
    // 正则表达式验证身高格式
    const regex = /^\d{1,3}(\.\d{1,2})?$/;
    if (!regex.test(trimmedValue)) {
      return Promise.reject('整数最多三位,小数最多两位');
    }
    // 将字符串转换为数值，验证数值范围
    const height = parseFloat(trimmedValue);
    if (isNaN(height) || height <= 0) {
      return Promise.reject('身高必须为大于0的数字');
    }
    return Promise.resolve();
  }
};

// 体重的验证规则
const checkWeight = (rule, value) => {
  if (!value) {
    return Promise.resolve();
    // return Promise.reject('请输入体重');
  }else {
    const trimmedValue = String(value).trim(); // 将值转换为字符串并去除首尾空格
    // 正则表达式验证体重格式
    const regex = /^\d{1,3}(\.\d{1,2})?$/;
    if (!regex.test(trimmedValue)) {
      return Promise.reject('整数最多三位，小数最多两位');
    }
    // 将字符串转换为数值，验证数值范围
    const weight = parseFloat(trimmedValue);
    if (isNaN(weight) || weight <= 0) {
      return Promise.reject('体重必须为大于0的数字');
    }
    return Promise.resolve();
  }

};


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
    {required: true, message: '请输入姓名', trigger: 'blur'},
    {max: 20, message: '姓名长度不能超过20个字符', trigger: 'blur'},
    {pattern: /^[\u4e00-\u9fa5]+$/, message: '姓名只能输入汉字', trigger: 'blur'}
  ],
  idNum: [
    {required: true, message: '请输入身份证号', trigger: 'blur'},
    {max: 18, message: '身份证号长度不能超过18个', trigger: 'blur'},
    {pattern: /^[0-9A-Z]+$/, message: '身份证号只能输入数字和大写字母', trigger: 'blur'}
  ],
  tel: [
    {required: true, message: '请输入电话号码', trigger: 'blur'},
    {pattern: /^[0-9]{11}$/, message: '电话号码必须为11位数字', trigger: 'blur'}
  ],
  permanentAddress: [
    // {required: true, message: '请输入户籍地址', trigger: 'blur'},
    {max: 60, message: '地址长度不能超过60个字符', trigger: 'blur'}
  ],
  address: [
    // {required: true, message: '请输入地址', trigger: 'blur'},
    {max: 60, message: '地址长度不能超过60个字符', trigger: 'blur'}
  ],
  firstType: [{required: true, message: '请选择第一人群分类', trigger: 'change'}],
  schoolOrTemple: [{required: true, message: '请输入单位', trigger: 'blur'},
    {max: 60, message: '单位长度不能超过60个字符', trigger: 'blur'}
  ],
  classroom: [{required: true, message: '请输入班级', trigger: 'blur'},
    {max: 60, message: '班级长度不能超过60字符', trigger: 'blur'}
  ],
  // 暂时不用，另作处理了
  /*moreType: [
      {required: true, message: '请选择多人群分类', trigger: 'blur'},
    ],*/
/*  permanentAddressProvince: [{required: true, message: '请选择户籍地址-省', trigger: 'blur'}],
  permanentAddressCity: [{required: true, message: '请选择户籍地址-市(州)', trigger: 'blur'}],
  permanentAddressCounty: [{required: true, message: '请选择户籍地址-县', trigger: 'blur'}],
  permanentAddressTown: [{required: true, message: '请选择户籍地址-乡', trigger: 'blur'}],*/
  province: [{required: true, message: '请选择现住址-省', trigger: 'blur'}],
  city: [{required: true, message: '请选择现住址-市(州)', trigger: 'blur'}],
  county: [{required: true, message: '请选择现住址-县', trigger: 'blur'}],
  town: [{required: true, message: '请选择现住址-乡', trigger: 'blur'}],
  // nation: [{required: true, message: '请选择民族', trigger: 'change'}],
  isNew: [{required: true, message: '请选择是否需筛查', trigger: 'change'}],
  isScreened: [{required: true, message: '请选择是否已筛查', trigger: 'change'}],
  // isNewStudent: [{required: true, message: '请选择是否为新生', trigger: 'change'}],
  // screenPoint:[{required: true, message: '请选择筛查点', trigger: 'blur'}],
  year: [{validator: checkYear, trigger: 'blur'},{required: true, message: '请输入年度', trigger: 'blur'}],
  screenType: [{required: true, message: '请选择筛查类型', trigger: 'blur'}],
  timeRange: [{required: true, message: '请选择筛查时间', trigger: 'blur'}],
  height: [
    {validator: checkHeight, trigger: 'change'}
  ],
  weight: [
    {validator: checkWeight, trigger: 'change'}
  ],
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, id?: number) => {

  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 新增 时给默认值
  formData.value.year = new Date().getFullYear();
  formData.value.screenType = 1;
  formData.value.isNew = 1;
  formData.value.screenTime = new Date();
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await ScreenPersonApi.getScreenPerson(id)
      getCityList(formData.value.province)
      getCityList2(formData.value.permanentAddressProvince)
      getCountyList(formData.value.city)
      getCountyList2(formData.value.permanentAddressCity)
      getTownList(formData.value.county)
      getTownList2(formData.value.permanentAddressCounty)
      formData.value.moreTempType = resolveMoreType(formData.value.moreType)
      if(formData.value.firstType == 4){
        formData.value.moreTempType = resolveMoreType(formData.value.moreType).filter(item => item != 4)
      }
      formatTime2()
    } finally {
      formLoading.value = false
    }
  }
}
defineExpose({open}) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  await formRef.value.validate()
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as ScreenPersonVO
    /*const birthYear = parseInt(formData.value.idNum.substr(6, 4))
    const currentYear = new Date().getFullYear()
    const age = currentYear - birthYear
    data.sex = getGenderCodeFromIdCard(data.idNum)
    data.age = age*/

    // 累加
    data.moreType =
      formData.value.moreTempType.reduce((acc, curr) => acc + curr, 0) !== 0
        ? formData.value.moreTempType.reduce((acc, curr) => acc + curr, 0)
        : 0

    if ( data.firstType === 1 && (data.moreType === 0 || formData.value.moreTempType.length === 0 )) {
      message.error('请选择多人群分类！')
      return
    }

    if (formType.value === 'create') {
      const birthYear = parseInt(formData.value.idNum.substr(6, 4))
      const currentYear = new Date().getFullYear()
      const age = currentYear - birthYear
      data.age = age
      data.sex = getGenderCodeFromIdCard(data.idNum)

      if (formData.value.firstType == 1 && formData.value.moreTempType.includes(1)){
        if (formData.value.studentType == undefined){
          message.error('请选择学生类别！')
          return
        }
      }

      await ScreenPersonApi.createScreenPerson(data)
      message.success(t('common.createSuccess'))
    } else {
      // 如果选择了非重点人群，则把多人群分类置零
      if (data.firstType == 2) {
        data.moreType = 0
      }
      if (formData.value.firstType == 1 && formData.value.moreTempType.includes(1)){
        if (formData.value.studentType == undefined){
          message.error('请选择学生类别！')
          return
        }
      }
      await ScreenPersonApi.updateScreenPerson(data)
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
    idNum: undefined,
    name: undefined,
    age: undefined,
    tel: undefined,
    sex: undefined,
    year: undefined,
    screenType: undefined,
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
    classroom: undefined,
    isNew: undefined,
    isNewStudent: undefined,
    isScreened: undefined,
    screenPoint: undefined,
    screenTime: undefined,
    moreTempType: [],
    studentType: undefined,
    screenStartTime: undefined,
    screenEndTime: undefined,
    guardianTel: undefined,
    timeRange: undefined,
  }
  formRef.value?.resetFields()
}
// 省
const copyProvince = reactive([])
const getProvinceList = () => {
  ScreenDistrictApi.getProvince().then(data => {
    provinceList.value = data;
    copyProvince.splice(0, copyProvince.length, ...data);
  })
}
const PinyinProvince = (val) => {
  if (val) {
    const result = []
    provinceList.value.forEach((i) => {
      const m = PinyinMatch.match(i.name, val)
      if (m) {
        result.push(i)
      }
    })
    provinceList.value.splice(0, provinceList.value.length, ...result)
  } else {
    // 如果没有输入，则还原列表
    provinceList.value.splice(0, provinceList.value.length, ...copyProvince)
  }
}
// 市
const getCityList = (provinceCode) => {
  ScreenDistrictApi.getCity(provinceCode).then(data => {
    cityList.value = data;
    copyCity.splice(0, copyCity.length, ...data);
  })
  formData.value.city = undefined;
  formData.value.county = undefined;
  formData.value.town = undefined;
}
const PinyinCity = (val) => {
  if (val) {
    const result = []
    cityList.value.forEach((i) => {
      const m = PinyinMatch.match(i.name, val)
      if (m) {
        result.push(i)
      }
    })
    cityList.value.splice(0, cityList.value.length, ...result)
  } else {
    // 如果没有输入，则还原列表
    cityList.value.splice(0, cityList.value.length, ...copyCity)
  }
}
// 户籍市

const getCityList2 = (provinceCode) => {
  ScreenDistrictApi.getCity(provinceCode).then(data => {
    cityList2.value = data;
    copyCity2.splice(0, copyCity2.length, ...data);
  })
  formData.value.permanentAddressCity = undefined;
  formData.value.permanentAddressCounty = undefined;
  formData.value.permanentAddressTown = undefined;
}
const PinyinCity2 = (val) => {
  if (val) {
    const result = []
    cityList2.value.forEach((i) => {
      const m = PinyinMatch.match(i.name, val)
      if (m) {
        result.push(i)
      }
    })
    cityList2.value.splice(0, cityList2.value.length, ...result)
  } else {
    // 如果没有输入，则还原列表
    cityList2.value.splice(0, cityList2.value.length, ...copyCity2)
  }
}

// 县

const getCountyList = (cityCode) => {
  ScreenDistrictApi.getCounty(cityCode).then(data => {
    countyList.value = data;
    copyCounty.splice(0, copyCounty.length, ...data);
  })
  formData.value.county = undefined;
  formData.value.town = undefined;
}
const PinyinCounty = (val) => {
  if (val) {
    const result = []
    countyList.value.forEach((i) => {
      const m = PinyinMatch.match(i.name, val)
      if (m) {
        result.push(i)
      }
    })
    countyList.value.splice(0, countyList.value.length, ...result)
  } else {
    // 如果没有输入，则还原列表
    countyList.value.splice(0, countyList.value.length, ...copyCounty)
  }
}
// 户籍县
const getCountyList2 = (cityCode) => {
  ScreenDistrictApi.getCounty(cityCode).then(data => {
    countyList2.value = data;
    copyCounty2.splice(0, copyCounty2.length, ...data);
  })
  formData.value.permanentAddressCounty = undefined;
  formData.value.permanentAddressTown = undefined;
}
const PinyinCounty2 = (val) => {
  if (val) {
    const result = []
    countyList2.value.forEach((i) => {
      const m = PinyinMatch.match(i.name, val)
      if (m) {
        result.push(i)
      }
    })
    countyList2.value.splice(0, countyList2.value.length, ...result)
  } else {
    // 如果没有输入，则还原列表
    countyList2.value.splice(0, countyList2.value.length, ...copyCounty2)
  }
}

// 乡

const getTownList = (countyCode) => {
  ScreenDistrictApi.getTown(countyCode).then(data => {
    townList.value = data;
    copyTown.splice(0, copyTown.length, ...data);
  })
  formData.value.town = undefined;
}
const PinyinTown = (val) => {
  if (val) {
    const result = []
    townList.value.forEach((i) => {
      const m = PinyinMatch.match(i.name, val)
      if (m) {
        result.push(i)
      }
    })
    townList.value.splice(0, townList.value.length, ...result)
  } else {
    // 如果没有输入，则还原列表
    townList.value.splice(0, townList.value.length, ...copyTown)
  }
}

// 户籍乡
const getTownList2 = (countyCode) => {
  ScreenDistrictApi.getTown(countyCode).then(data => {
    townList2.value = data;
    copyTown2.splice(0, copyTown2.length, ...data);
  })
  formData.value.permanentAddressTown = undefined;
}
const PinyinTown2 = (val) => {
  if (val) {
    const result = []
    townList2.value.forEach((i) => {
      const m = PinyinMatch.match(i.name, val)
      if (m) {
        result.push(i)
      }
    })
    townList2.value.splice(0, townList2.value.length, ...result)
  } else {
    // 如果没有输入，则还原列表
    townList2.value.splice(0, townList2.value.length, ...copyTown2)
  }
}

// 民族拼音搜索
const copycommonAddr = reactive([])
const getEthnicList = () => {
  const data = getIntDictOptions(DICT_TYPE.NATION)
  ethnicList.value = data
  copycommonAddr.splice(0, copycommonAddr.length, ...data)
}
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
  const keys = Object.keys(groups)
    .map(Number)
    .sort((a, b) => b - a)
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

// 根据身份证号提取性别代码
const getGenderCodeFromIdCard = (idCard) => {
  const genderCode = idCard.charAt(idCard.length - 2)
  return genderCode % 2 === 0 ? 1 : 0 // 如果是奇数返回0（男），偶数返回1（女）
}

// 动态显示学校&寺庙
const labelText = computed(() => {
  if (formData.value.moreTempType.includes(1) && formData.value.moreTempType.includes(32)) {
    return '学校或寺庙'
  } else if (formData.value.moreTempType.includes(1)) {
    return '学校'
  } else if (formData.value.moreTempType.includes(32)) {
    return '寺庙'
  }
})

const changeMoreType = () => {
  formData.value.moreType = undefined
  formData.value.moreTempType = []; // 将 formData.moreTempType 变成空数组
}

const copycommonAddrScreenPoint = reactive([])
const getScreenPoint = async () => {
  pointList.value = await ScreenPointApi.getScreenPointList()
  copycommonAddrScreenPoint.splice(0, copycommonAddrScreenPoint.length, ...pointList.value)
}
const PinyinScreenPoint = (val) => {
  if (val) {
    const result = []
    pointList.value.forEach((i) => {
      const m = PinyinMatch.match(i, val)
      if (m) {
        result.push(i)
      }
    })
    pointList.value.splice(0, pointList.value.length, ...result)
  } else {
    // 如果没有输入，则还原列表
    pointList.value.splice(0, pointList.value.length, ...copycommonAddrScreenPoint)
  }
}

const formatTime = (time) =>{
  // 方法三：使用 moment.js
  const dateString1 = time[0];
  const dateString2 = time[1];

  formData.value.screenStartTime = moment(dateString1).valueOf();
  formData.value.screenEndTime = moment(dateString2).valueOf();
}

const formatTime2 = () =>{
  const timestamp1 = formData.value.screenStartTime; // 假设这是一个时间戳
  const timestamp2 = formData.value.screenEndTime; // 假设这是另一个时间戳

  const dateString1 = moment(timestamp1).toISOString(); // 转换为 ISO 8601 格式的日期时间字符串
  const dateString2 = moment(timestamp2).toISOString(); // 转换为 ISO 8601 格式的日期时间字符串
  formData.value.timeRange = [dateString1,dateString2];
}

/** 初始化 **/
onMounted(async () => {
  await getScreenPoint()
  getProvinceList()
/*  getCityList()
  getCountyList()
  getTownList()*/
  getEthnicList()
})
</script>
