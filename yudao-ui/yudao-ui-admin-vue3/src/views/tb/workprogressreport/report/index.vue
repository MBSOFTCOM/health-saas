<template>
  <ContentWrap>
    <el-tabs type="border-card">
      <el-tab-pane label="按人数统计">
        <el-row>
          <el-select
            v-model="year"
            value-key="id"
            placeholder="请选择筛查年度"
            @change="changeYear()"
            style="width: 150px; margin-right: 20px"
          >
            <el-option
              v-for="item in optionsYear"
              :key="item.id"
              :label="item.label"
              :value="item.id"
            />
          </el-select>

          <el-select
            v-if="year"
            v-model="screenType"
            value-key="id"
            placeholder="请选择筛查类型"
            @change="changeScreenType()"
            style="width: 240px"
          >
            <el-option
              v-for="item in options"
              :key="item.id"
              :label="item.label"
              :value="item.id"
            />
          </el-select>
        </el-row>

        <el-divider />
        <el-table
          :data="tableData_128"
          v-loading="loading_128"
          style="width: 100%"
          :header-cell-style="headerCellStyle"
        >
          <el-table-column prop="date" label="HIV/AIDS患者" width="150" align="center" />
          <el-table-column label="本季度" align="center">
            <el-table-column prop="expectedCheckCount" width="90" label="应查人数" />
            <el-table-column prop="actualCheckedCount"  width="90" label="实查人数" />
            <el-table-column prop="actualXRayCount"  width="90" label="拍片人数" />
            <el-table-column prop="expectedSputumTestCount"  width="110" label="应查痰人数" />
            <el-table-column prop="actualSputumCheckCount" width="110" label="实查痰人数" />
            <el-table-column prop="activeTuberculosisCount" width="210" label="发现活动性肺结核患者数" />
            <el-table-column prop="pathogenPositiveCount" width="220" label="发现初治病原学阳性患者数" />
            <el-table-column prop="HIVPositiveCount" width="190" label="登记HIV/AIDS患者数" />
          </el-table-column>
          <el-table-column label="本年度" align="center">
            <el-table-column prop="expectedCheckCountY" width="90" label="应查人数" />
            <el-table-column prop="actualCheckedCountY" width="90" label="实查人数" />
            <el-table-column prop="actualXRayCountY" width="90" label="拍片人数" />
            <el-table-column prop="expectedSputumTestCountY"  width="110" label="应查痰人数" />
            <el-table-column prop="actualSputumCheckCountY"  width="110" label="实查痰人数" />
            <el-table-column
              prop="activeTuberculosisCountY"
              width="270"
              label="检查累计发现活动性肺结核患者数"
            />
            <el-table-column prop="pathogenPositiveCountY" width="250"  label="累计发现初治病原学阳性患者数" />
            <el-table-column prop="HIVPositiveCountY" width="190"  label="登记HIV/AIDS患者数" />
          </el-table-column>
        </el-table>
        <el-divider />
        <el-table
          :data="tableData_16"
          v-loading="loading_16"
          style="width: 100%"
          :header-cell-style="headerCellStyle"
        >
          <el-table-column prop="date" label="糖尿病患者" width="150" align="center" />
          <el-table-column label="本季度" align="center">
            <el-table-column prop="expectedCheckCount" width="90" label="应查人数" />
            <el-table-column prop="actualCheckedCount" width="90" label="实查人数" />
            <el-table-column prop="actualXRayCount" width="90" label="拍片人数" />
            <el-table-column prop="expectedSputumTestCount" width="110" label="应查痰人数" />
            <el-table-column prop="actualSputumCheckCount" width="110" label="实查痰人数" />
            <el-table-column prop="activeTuberculosisCount" width="210"  label="发现活动性肺结核患者数" />
            <el-table-column prop="pathogenPositiveCount" width="220"  label="发现初治病原学阳性患者数" />
            <el-table-column prop="diabeticPatientCount" width="160"  label="登记糖尿病患者数" />
          </el-table-column>
          <el-table-column label="本年度" align="center">
            <el-table-column prop="expectedCheckCountY" width="90" label="应查人数" />
            <el-table-column prop="actualCheckedCountY" width="90" label="实查人数" />
            <el-table-column prop="actualXRayCountY" width="90" label="拍片人数" />
            <el-table-column prop="expectedSputumTestCountY" width="110" label="应查痰人数" />
            <el-table-column prop="actualSputumCheckCountY" width="110" label="实查痰人数" />
            <el-table-column
              prop="activeTuberculosisCountY"
              label="检查累计发现活动性肺结核患者数"
              width="270"
            />
            <el-table-column prop="pathogenPositiveCountY" width="250" label="累计发现初治病原学阳性患者数" />
            <el-table-column prop="diabeticPatientCountY" width="210" label="登记糖尿病患者数" />
          </el-table-column>
        </el-table>
        <el-divider />
        <el-table
          :data="tableData_2"
          v-loading="loading_2"
          style="width: 100%"
          :header-cell-style="headerCellStyle1"
        >
          <el-table-column prop="date" label="65岁及以上人群" width="150" align="center" />
          <el-table-column label="本季度" align="center">
            <el-table-column prop="expectedCheckCount" width="90"  label="应查人数" />
            <el-table-column prop="actualCheckedCount" width="90"  label="实查人数" />
            <el-table-column prop="actualXRayCount" width="90"  label="拍片人数" />
            <el-table-column prop="expectedSputumTestCount" width="110"  label="应查痰人数" />
            <el-table-column prop="actualSputumCheckCount"  width="110" label="实查痰人数" />
            <el-table-column prop="activeTuberculosisCount" width="210" label="发现活动性肺结核患者数" />
            <el-table-column prop="pathogenPositiveCount"  width="220" label="发现初治病原学阳性患者数" />
          </el-table-column>
          <el-table-column label="本年度" align="center">
            <el-table-column prop="expectedCheckCountY" width="90" label="应查人数" />
            <el-table-column prop="actualCheckedCountY" width="90" label="实查人数" />
            <el-table-column prop="actualXRayCountY" width="90" label="拍片人数" />
            <el-table-column prop="expectedSputumTestCountY" width="110" label="应查痰人数" />
            <el-table-column prop="actualSputumCheckCountY" width="110" label="实查痰人数" />
            <el-table-column
              prop="activeTuberculosisCountY"
              label="检查累计发现活动性肺结核患者数"
              width="270"
            />
            <el-table-column prop="pathogenPositiveCountY" width="260" label="累计发现初治病原学阳性患者数" />
          </el-table-column>
        </el-table>
        <el-divider />
        <el-table
          :data="tableData_8"
          v-loading="loading_8"
          style="width: 100%"
          :header-cell-style="headerCellStyle2"
        >
          <el-table-column prop="date" label="活动性肺结核密接者" width="170" align="center" />
          <el-table-column label="本季度" align="center">
            <el-table-column prop="confirmedAndTreatingCount" width="170" label="确诊及治疗中患者数" />
            <el-table-column prop="name" width="170" label="确定密切接触者人数" />
            <el-table-column prop="actualCheckedCount" width="90" label="实查人数" />
            <el-table-column prop="actualXRayCount" width="90" label="拍片人数" />
            <el-table-column prop="expectedSputumTestCount" width="110" label="应查痰人数" />
            <el-table-column prop="actualSputumCheckCount" width="110" label="实查痰人数" />
            <el-table-column prop="activeTuberculosisCount" width="210" label="发现活动性肺结核患者数" />
            <el-table-column prop="pathogenPositiveCount" width="220" label="发现初治病原学阳性患者数" />
            <el-table-column prop="latentInfectionCount" width="170" label="发现潜伏感染者人数" />
          </el-table-column>
          <el-table-column label="本年度" align="center">
            <el-table-column prop="confirmedAndTreatingCountY"  width="170" label="确诊及治疗中患者数" />
            <el-table-column prop="name"  width="170" label="确定密切接触者人数" />
            <el-table-column prop="actualCheckedCountY"  width="90" label="实查人数" />
            <el-table-column prop="actualXRayCount" width="90" label="拍片人数" />
            <el-table-column prop="actualSputumCheckCountY" width="110" label="应查痰人数" />
            <el-table-column prop="actualSputumCheckCountY"  width="110" label="实查痰人数" />
            <el-table-column
              prop="activeTuberculosisCountY"
              label="检查累计发现活动性肺结核患者数"
               width="270"
            />
            <el-table-column prop="pathogenPositiveCountY" width="260" label="累计发现初治病原学阳性患者数" />
            <el-table-column prop="latentInfectionCountY" width="210" label="累计发现潜伏感染者人数" />
          </el-table-column>
        </el-table>
        <el-divider />
        <el-table
          :data="tableData_1"
          v-loading="loading_1"
          style="width: 100%"
          :header-cell-style="headerCellStyle2"
        >
          <el-table-column prop="date" label="学校" width="100" align="center" />
          <el-table-column label="本季度" align="center">
            <el-table-column prop="expectedCheckCount" width="90" label="应查人数" />
            <el-table-column prop="actualCheckedCount" width="90" label="实查人数" />
            <el-table-column prop="actualXRayCount" width="90" label="拍片人数" />
            <el-table-column prop="expectedXRayCount" width="110" label="应拍片人数" />
            <el-table-column prop="expectedSputumTestCount" width="110" label="应查痰人数" />
            <el-table-column prop="actualSputumCheckCount" width="110" label="实查痰人数" />
            <el-table-column prop="activeTuberculosisCount" width="210" label="发现活动性肺结核患者数" />
            <el-table-column prop="pathogenPositiveCount" width="220" label="发现初治病原学阳性患者数" />
            <el-table-column prop="latentInfectionCount" width="170" label="发现潜伏感染者人数" />
          </el-table-column>
          <el-table-column label="本年度" align="center">
            <el-table-column prop="expectedCheckCountY" width="90" label="应查人数" />
            <el-table-column prop="actualCheckedCountY" width="90" label="实查人数" />
            <el-table-column prop="actualXRayCountY" width="90" label="拍片人数" />
            <el-table-column prop="expectedXRayCountY" width="110" label="应拍片人数" />
            <el-table-column prop="expectedSputumTestCountY" width="110" label="应查痰人数" />
            <el-table-column prop="actualSputumCheckCountY" width="110" label="实查痰人数" />
            <el-table-column
              prop="activeTuberculosisCountY"
              label="检查累计发现活动性肺结核患者数"
              width="270"
            />
            <el-table-column prop="pathogenPositiveCountY" width="260" label="累计发现初治病原学阳性患者数" />
            <el-table-column prop="latentInfectionCountY"  width="210" label="累计发现潜伏感染者人数" />
          </el-table-column>
        </el-table>
        <el-divider />
        <el-table
          :data="tableData_32"
          v-loading="loading_32"
          style="width: 100%"
          :header-cell-style="headerCellStyle2"
        >
          <el-table-column prop="date" label="寺庙" width="150" align="center" />
          <el-table-column label="本季度" align="center">
            <el-table-column prop="expectedCheckCount" width="90" label="应查人数" />
            <el-table-column prop="actualCheckedCount" width="90" label="实查人数" />
            <el-table-column prop="actualXRayCount" width="90" label="拍片人数" />
            <el-table-column prop="expectedXRayCount" width="110" label="应拍片人数" />
            <el-table-column prop="expectedSputumTestCount" width="110" label="应查痰人数" />
            <el-table-column prop="actualSputumCheckCount" width="110" label="实查痰人数" />
            <el-table-column prop="activeTuberculosisCount" width="210" label="发现活动性肺结核患者数" />
            <el-table-column prop="pathogenPositiveCount" width="220" label="发现初治病原学阳性患者数" />
            <el-table-column prop="latentInfectionCount" width="170" label="发现潜伏感染者人数" />
          </el-table-column>
          <el-table-column label="本年度" align="center">
            <el-table-column prop="expectedCheckCountY" width="90" label="应查人数" />
            <el-table-column prop="actualCheckedCountY" width="90" label="实查人数" />
            <el-table-column prop="actualXRayCountY" width="90" label="拍片人数" />
            <el-table-column prop="expectedXRayCountY" width="110" label="应拍片人数" />
            <el-table-column prop="expectedSputumTestCountY" width="110" label="应查痰人数" />
            <el-table-column prop="actualSputumCheckCountY" width="110" label="实查痰人数" />
            <el-table-column
              prop="activeTuberculosisCountY"
              label="检查累计发现活动性肺结核患者数"
              width="270"
            />
            <el-table-column prop="pathogenPositiveCountY" width="270" label="累计发现初治病原学阳性患者数" />
            <el-table-column prop="latentInfectionCountY" width="240" label="累计发现潜伏感染者人数" />
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <!-- <el-tab-pane label="按人次统计" /> -->
    </el-tabs>
  </ContentWrap>
</template>
<script setup lang="ts">
import { ref } from 'vue'
import { onMounted } from 'vue'
import { ReportApi } from '@/api/tb/report'

type YearItem = {
  id: number
  label: string
}

const year = ref<number>()
const screenType = ref<number>()
const options = ref([
  { id: 1, label: '常规筛查' },
  { id: 2, label: '新生入学筛查' },
  { id: 3, label: '应急筛查' }
])
const optionsYear = ref([
  { id: 2024, label: '2024年' },
  { id: 2023, label: '2023年' },
  { id: 2022, label: '2022年' },
  { id: 2021, label: '2021年' },
  { id: 2020, label: '2020年' }
])
// 人群分类
const STUDENT=ref(1)
const STAFF=ref(4)
const SCHOOL=ref(STUDENT.value+STAFF.value)
const MONK=ref(32)
const CLOSER=ref(8)
const OLD=ref(2)
const DIABETES=ref(16)
const HIV=ref(128)

const tableData_128 = ref([])
const tableData_16 = ref([])
const tableData_2 = ref([])
const tableData_8 = ref([])
const tableData_1 = ref([])
const tableData_32 = ref([])

const loading_128 = ref(false)
const loading_16 = ref(false)
const loading_2 = ref(false)
const loading_8 = ref(false)
const loading_1 = ref(false)
const loading_32 = ref(false)

// 获取当前年份以及季度
function getCurrentYearAndQuarter(): { year: number; quarter: number } {
  const today = new Date()
  const year = today.getFullYear()
  const quarter = Math.floor((today.getMonth() + 1) / 3) // 获取当前是一年中的第几个季度

  return { year, quarter }
}

// 获取年份下拉数据列表
function generateYears(currentYear: number, count: number): YearItem[] {
  const years: YearItem[] = []
  for (let i = 0; i < count; i++) {
    years.push({
      id: currentYear - i,
      label: `${currentYear - i}年`
    })
  }
  return years
}

// 请求数据
const getData = (moreType: number, year: number, screenType: number) => {
  let params = {
    moreType: moreType,
    year: year,
    quarter: getCurrentYearAndQuarter().quarter,
    screenType: screenType
  }
  return ReportApi.getReportData(params)
}



// 切换年份或者筛查类型时候重新获取数据
const changeDataReq = () => {
  if (screenType.value && year.value) {
    loading_128.value = true
    const data_128 = getData(HIV.value, year.value, screenType.value)
    data_128.then((res) => {
      if (res && res[0]) {
        tableData_128.value = res
      } else {
        tableData_128.value = []
      }
      loading_128.value = false
    })

    loading_16.value = true
    const data_16 = getData(DIABETES.value, year.value, screenType.value)
    data_16.then((res) => {
      if (res && res[0]) {
        tableData_16.value = res
      } else {
        tableData_16.value = []
      }
      loading_16.value = false
    })

    loading_2.value = true
    const data_2 = getData(OLD.value, year.value, screenType.value)
    data_2.then((res) => {
      if (res && res[0]) {
        tableData_2.value = res
      } else {
        tableData_2.value = []
      }
      loading_2.value = false
    })

    loading_8.value = true
    const data_8 = getData(CLOSER.value, year.value, screenType.value)
    data_8.then((res) => {
      if (res && res[0]) {
        tableData_8.value = res
      } else {
        tableData_8.value = []
      }
      loading_8.value = false
    })

    loading_1.value = true
    const data_1 = getData(SCHOOL.value, year.value, screenType.value)
    data_1.then((res) => {
      if (res && res[0]) {
        tableData_1.value = res
      } else {
        tableData_1.value = []
      }
      loading_1.value = false
    })

    loading_32.value = true
    const data_32 = getData(MONK.value, year.value, screenType.value)
    data_32.then((res) => {
      if (res && res[0]) {
        tableData_32.value = res
      } else {
        tableData_32.value = []
      }
      loading_32.value = false
    })
  }
}

// 选择筛查年份
const changeScreenType = () => {
  changeDataReq()
}

// 选择年份
const changeYear = () => {
  changeDataReq()
}

onMounted(() => {
  // 初始化筛查年份数据
  const currentYear = new Date().getFullYear()
  const yearsData = generateYears(currentYear, 5)
  optionsYear.value = yearsData

  getData(128, 2024, 1)
})

// 表头背景颜色
const headerCellStyle = ({ column, rowIndex, columnIndex }) => {
  if (rowIndex == 0 && columnIndex == 0) {
    return {}
  }
  if (rowIndex == 0 && columnIndex == 2) {
    return { background: '#72c6d3' }
  }
  if (rowIndex == 0 || rowIndex == 1) {
    if (columnIndex >= 0 && columnIndex <= 7) {
      return { background: '#8ce0ed' }
    } else if (columnIndex >= 8 && columnIndex <= 15) {
      return { background: '#72c6d3' }
    }
  }
}
const headerCellStyle1 = ({ column, rowIndex, columnIndex }) => {
  if (rowIndex == 0 && columnIndex == 0) {
    return {}
  }
  if (rowIndex == 0 && columnIndex == 2) {
    return { background: '#72c6d3' }
  }
  if (rowIndex == 0 || rowIndex == 1) {
    if (columnIndex >= 0 && columnIndex <= 6) {
      return { background: '#8ce0ed' }
    } else if (columnIndex >= 6 && columnIndex <= 13) {
      return { background: '#72c6d3' }
    }
  }
}
const headerCellStyle2 = ({ column, rowIndex, columnIndex }) => {
  if (rowIndex == 0 && columnIndex == 0) {
    return {}
  }
  if (rowIndex == 0 && columnIndex == 2) {
    return { background: '#72c6d3' }
  }
  if (rowIndex == 0 || rowIndex == 1) {
    if (columnIndex >= 0 && columnIndex <= 8) {
      return { background: '#8ce0ed' }
    } else if (columnIndex >= 8 && columnIndex <= 19) {
      return { background: '#72c6d3' }
    }
  }
}
</script>
<style lang="scss" scoped>
.tabel1 {
  background-color: #72c6d3;
}
</style>
