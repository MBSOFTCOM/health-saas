<template>
  <Dialog v-model="dialogVisible" style="min-width: 1400px; " :show-overflow-tooltip="true">
    <template #title>
      <span style="font-weight: bold">患者信息</span>
    </template>
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-row type="flex" justify="start">
        <el-col :span="8">
          <el-form-item label="姓名" prop="name">
            <el-input v-model="formData.name" disabled style="width: auto"/>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="年龄" prop="age">
            <el-input v-model.number="formData.age" disabled style="width: auto"/>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="性别" prop="sex">
            <dict-tag :type="DICT_TYPE.PATIENT_SEX" :value="formData.sex"/>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row type="flex" justify="start">
        <el-col :span="8">
          <el-form-item label="联系电话" prop="tel">
            <el-input v-model="formData.tel" disabled style="width: auto"/>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="第一人群分类" prop="firstType">
            <dict-tag :type="DICT_TYPE.FIRST_TYPE" :value="formData.firstType" style="width: auto"/>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="多人群分类" prop="moreType" v-if="formData.firstType != 2">
            <div v-for="item in resolveMoreType(formData.moreType)" :key="item">
              <dict-tag :type="DICT_TYPE.MORE_TYPE" :value="item" style="width: auto"/>
            </div>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

  </Dialog>


</template>
<script setup lang="ts">
import {getIntDictOptions, DICT_TYPE} from '@/utils/dict'
import {ScreenPersonApi, ScreenPersonVO} from '@/api/tb/screenpersonrealsituation'
import {onMounted, ref, reactive} from 'vue'
import DictTag from "@/components/DictTag/src/DictTag.vue"



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

const checkList = ref([]) //采集组数据
const electList = ref([]) // 心电图组数据




/** 打开弹窗 */
const open = async (id: number, year: number, screenType: number) => {
  activeName.value = 'first'
  dialogVisible.value = true
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await ScreenPersonApi.getScreenPerson(id)
      const data = await ScreenPersonApi.getPatientInfoList(id, year, screenType)

    } finally {
      formLoading.value = false
    }
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









</script>
<style scoped lang="scss">
.main {
  width: 100%;
  height: 85vh;
  display: flex;
  font-size: 2rem;
}

.left_info {
  flex: 0.5;
}

.class_hover {
  background-color: #028077;
  color: white;
  cursor: grab;
}

.center_info {
  flex: 0.2;

  display: flex;
  flex-flow: column;

  .physical_examination_list {
    flex: 1;
  }

  .physical_examination_list:hover {
    background-color: #028077;
    color: white;
    cursor: grab;
  }

  .ct {
    flex: 1;
  }

  .ct:hover {
    background-color: #028077;
    color: white;
    cursor: grab;
  }

  .dr {
    flex: 1;
  }

  .dr:hover {
    background-color: #028077;
    color: white;
    cursor: grab;
  }

  .experiment {
    flex: 1;
  }

  .experiment:hover {
    background-color: #028077;
    color: white;
    cursor: grab;
  }

  .electrocardiogram {
    flex: 1;
  }

  .electrocardiogram:hover {
    background-color: #028077;
    color: white;
    cursor: grab;
  }
}

.right_info {
  flex: 2;
}

.border_color {
  border: 1px solid #000000;
}

.none_border_bottom {
  border-bottom: none;
}

.none_border_top {
  border-top: none;
}

.none_border_left {
  border-left: none;
}

.none_border_right {
  border-right: none;
}

.none_border {
  border-bottom-style: none;
  border-top-style: none;
  border-left-style: none;
  border-right-style: none;
}

.center_div {
  align-content: center;
  text-align: center;
}

.font_color {
}


.center {
  align-content: center;
}

.physical_examination_diagnosis {
  display: flex;
  flex-direction: column;
}

.text_name {
  /* border: 1px solid red; */
  align-items: center;
  justify-content: center;
  text-align: center;
  font-size: 2vw;
  font-weight: bold;
}

.input_content {
  position: relative;
  padding-left: 2.4vw;
}

.info_idCard {
  //position: absolute;
  //margin: -1.1vw 0 0 7vw;
}

.info_name {
  //margin-bottom: -25px;
}

.info_age {
  //margin: 0.2vw 0 0 7vw;
}

.info_date {
  //margin: -1.1vw 0 0 15vw;

}

.check_signature {
  margin-top: 5vh;
  margin-bottom: 2vh;
  margin-left: 80%;
}

.symptom_input_content {
  border: 1px solid;
  margin: 1vh 5vh 0 5vh;
  height: 60vh;

  display: flex;
  flex-direction: column;

  .symptom_input_content_item_1 {
    flex: 1;
    border-bottom: 1px solid;

    display: flex;

    .symptom_input_content_item_1_item4_1 {
      flex: 1;
      border-right: 1px solid;
      padding-left: 1vw;

      display: flex;

      .symptom_input_content_item_1_item4_1_1 {
        flex: 1;
        border-right: 1px solid;
      }

      .symptom_input_content_item_1_item4_1_3 {
        flex: 3;
        padding-left: 1vw;
      }
    }

    .symptom_input_content_item_1_item4_2 {
      flex: 2;
      border-right: 1px solid;
      padding-left: 1vw;
    }
  }

  .symptom_input_content_item_2 {
    flex: 2;

    display: flex;

    .symptom_input_content_item_1_item4_1 {
      flex: 1;
      border-right: 1px solid;
      padding-left: 1vw;

      display: flex;

      .symptom_input_content_item_1_item4_1_1 {
        flex: 1;
        border-right: 1px solid;

      }

      .symptom_input_content_item_1_item4_1_3 {
        flex: 3;
        border-right: 1px solid;
        padding-left: 1vw;
      }
    }

    .symptom_input_content_item_1_item4_1 {
      flex: 1;
      border-right: 1px solid;
      padding-left: 1vw;
    }

    .symptom_input_content_item_1_item4_2 {
      flex: 2;
      border-right: 1px solid;
      padding-left: 1vw;
    }
  }
}

.tips_content {
  border: 1px solid;
  margin: 1vh 5vh 0 5vh;
  height: 40%;
  padding: 1vw;
}

.crowd_class_title {
  font-weight: 700;
}

.crowd_class {
  border: 1px solid;
  display: flex;
  flex-direction: column;
  margin: 1vh 5vh 0 5vh;
  align-items: center;
  font-size: 1.5vw;
}

.crowd_class_content {
  margin: 0 5vh 0 5vh;
  height: 40vh;
  display: flex;

  .content_one {
    flex: 2;
    border: 1px solid;
    border-top-style: none;

    display: flex;
    flex-direction: column;

    .content_one_head {
      flex: 1;

      display: flex;

      .content_one_head_left {
        border-bottom: 1px solid;
        border-right: 1px solid;
        flex: 10;
        padding-left: 1vw;
      }

      .content_one_head_right {
        border-bottom: 1px solid;
        flex: 3;
        padding-left: 1vw;
      }
    }

    .content_one_content {
      flex: 4;
      display: flex;

      .content_one_content_item_1 {
        border-right: 1px solid;
        flex: 1;
        padding-top: 10vh;
        text-align: center;
      }

      .content_one_content_item_2 {
        border-right: 1px solid;
        flex: 2;

        display: flex;
        flex-direction: column;

        .content_one_content_item_2_item_1 {
          flex: 1;
          border-bottom: 1px solid;
          padding-left: 1vw;
        }

        .content_one_content_item_2_item_2 {
          flex: 1;
          border-bottom: 1px solid;
          padding-left: 1vw;
        }

        .content_one_content_item_2_item_3 {
          flex: 1;
          border-bottom: 1px solid;
          padding-left: 1vw;
        }

        .content_one_content_item_2_item_4 {
          flex: 1;
          padding-left: 1vw;
        }
      }

      .content_one_content_item_3 {
        flex: 1;

        display: flex;
        flex-direction: column;

        .content_one_content_item_3_item_1 {
          flex: 1;
          border-bottom: 1px solid;
          padding-left: 1vw;
        }

        .content_one_content_item_3_item_2 {
          flex: 1;
          border-bottom: 1px solid;
          padding-left: 1vw;
        }

        .content_one_content_item_3_item_3 {
          flex: 1;
          border-bottom: 1px solid;
          padding-left: 1vw;
        }

        .content_one_content_item_3_item_4 {
          flex: 1;
          padding-left: 1vw;
        }
      }
    }
  }

  .content_tow {
    flex: 1;
    border-bottom: 1px solid;
    border-right: 1px solid;
    border-top: 1px none;

    display: flex;
    flex-direction: column;

    .content_tow_item_column3_2 {
      flex: 2;
      border-bottom: 1px solid;
      display: flex;

      .content_tow_item_column3_item_1 {
        flex: 1.2;
        border-right: 1px solid;

        padding-top: 6vh;
        text-align: center;

        display: flex;
        flex-direction: column;


      }

      .content_tow_item_column3_item_1point5 {
        flex: 1;

        display: flex;
        flex-direction: column;

        .content_tow_item_column3_item_1point5_item_col_1 {
          flex: 1;
          border-bottom: 1px solid;
          border-right: 1px solid;
          padding-left: 1vw;
        }

        .content_tow_item_column3_item_1_item_col_1 {
          flex: 1;
          border-bottom: 1px solid;
          padding-left: 1vw;
          padding-top: 1vh;
        }
      }
    }

    .content_tow_item_column3_1 {
      flex: 1;
      border-bottom: 1px solid;

      display: flex;

      .content_tow_item_column3_item_2point5 {
        flex: 2.5;
        border-right: 1px solid;
        padding-top: 2vh;
        padding-left: 1vw;
      }

      .content_tow_item_column3_item_1 {
        flex: 1;

        padding-top: 1.5vh;
        padding-left: 1vw;
      }
    }
  }

  .content_three {
    flex: 1.5;
    border-right: 1px solid;
    border-bottom: 1px solid;

    display: flex;
    flex-direction: column;

    .content_three_item_1 {
      flex: 1;
      border-bottom: 1px solid;

      display: flex;

      .content_three_item_1_1 {
        flex: 1;
        border-left: 1px solid;

        padding-top: 2.5vh;
        padding-left: 1vw;
      }

      .content_three_item_1_4 {
       flex: 5.1;
        padding-top: 2vh;
        padding-left: 1vw;
      }
    }

    .content_three_item_2 {
      flex: 2;

      display: flex;

      .content_three_item_2_1 {
        flex: 1;
        border-right: 1px solid;

        display: flex;
        flex-direction: column;

        .content_three_item_2_1_col_1 {
          flex: 1;
          border-bottom: 1px solid;
          padding-left: 1vw;
          padding-top: 1vh;
        }
      }

      .content_three_item_2_0point5 {
        flex: 0.5;

        display: flex;
        flex-direction: column;

        .content_three_item_2_1_col_1 {
          flex: 1;
          border-bottom: 1px solid;
          padding-left: 1vw;
          padding-top: 1vh;
        }
      }
    }
  }
}
</style>
