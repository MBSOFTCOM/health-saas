<template>
  <div class="main">
    <div class="left_info">
      <el-form
        label-width="12px"
        :model="leftFormData"
      >
        <div style="margin: 2% 1% 1% 1% ; font-weight: bold">诊断结果</div>
        <el-form-item>
          <el-radio-group v-model="leftFormData.outcome">
            <el-radio :label="1">病原学阳性</el-radio>
            <el-radio :label="2">病原学阴性</el-radio>
            <el-radio :label="3">无病原学结果</el-radio>
            <el-radio :label="4">耐药</el-radio>
            <el-radio :label="5">陈旧性肺结核</el-radio>
            <el-radio :label="6">暂时排除结核病</el-radio>
            <el-radio :label="7">潜伏感染者</el-radio>
            <el-radio :label="8">肺结核</el-radio>
            <el-radio :label="9">肺外结核</el-radio>
          </el-radio-group>
        </el-form-item>
        <div style="margin: -2% 1% 1% 1% ; font-weight: bold">治疗方案</div>
        <el-form-item>
          <el-radio-group v-model="leftFormData.treatmentProgram">
            <el-radio :label="1">14天治疗管理</el-radio>
            <el-radio :label="2">住院治疗</el-radio>
            <el-radio :label="3">隔离治疗</el-radio>
            <el-radio :label="4">服药管理（复诊）</el-radio>
            <el-radio :label="5">服药管理（服药）</el-radio>
            <el-radio :label="6">服药管理（随访）</el-radio>
            <el-radio :label="7">停止治疗</el-radio>
          </el-radio-group>
        </el-form-item>
        <div style="margin: -2% 1% 1% 1% ; font-weight: bold">是否网报</div>
        <el-form-item>
          <el-radio-group v-model="leftFormData.report">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <div style="margin: -2% 1% 1% 1% ; font-weight: bold">是否进行预防性治疗</div>
        <el-form-item>
          <el-radio-group v-model="leftFormData.preventiveTreatment">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitDiagnosisResult()" plain>提交诊断结果</el-button>
        </el-form-item>

      </el-form>

    </div>
    <div class="center_info">
      <div
ref="physical_examination"
           class="physical_examination_list border_color center_div font_color"
           @click="clickItem(1)"> 体检单
      </div>
      <div
ref="dr" class="dr border_color center_div font_color none_border_top"
           @click="clickItem(2)"> DR
      </div>
      <div
ref="ct" class="ct border_color center_div font_color none_border_top"
           @click="clickItem(3)"> CT
      </div>
      <div
ref="experiment" class="experiment border_color center_div font_color none_border_top"
           @click="clickItem(4)"> 实验室
      </div>
      <div
ref="electrocardiogram"
           class="electrocardiogram border_color center_div font_color none_border_top"
           @click="clickItem(5)"> 心电图
      </div>
    </div>
    <div class="right_info border_color none_border">
      <el-select
        v-if="currentItemIndex != 1"
        v-model="value"
        placeholder="请选择患者筛查次序/时间"
        size="large"
        style="margin-left:3.7%; width: 240px"
        @change="change(value)"
      >
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <div v-show="currentItemIndex === 1" class="physical_examination_diagnosis">
        <div class="text_name">体检表</div>
        <div class="input_content">
          <div class="info_code">筛查编号：{{ tbHealthScreening.screeningNumber }}</div>
          <div class="info_idCard">身份证号：{{ tbHealthScreening.idNumber }}</div>
          <div class="info_name">姓名：{{ tbHealthScreening.name }}</div>
          <div class="info_age">年龄：{{ tbHealthScreening.age }} 岁</div>
          <div class="info_date">体检日期：{{ tbHealthScreening.examinationDate }}</div>
        </div>
        <div class="crowd_class">
          <div class="crowd_class_title">人群分类（可多选）</div>
        </div>
        <div class="crowd_class_content">
          <div class="content_one">
            <div class="content_one_head ">
              <div class="content_one_head_left center">
                活动性肺结核密切接触者
              </div>
              <div class="content_one_head_right center" style="font-size: 1.5rem;">
                {{ tbHealthScreening.closeContactWithActivePulmonaryTB ? '☑' : '□' }}
              </div>
            </div>

            <div class="content_one_content ">
              <div class="content_one_content_item_1 ">
                在校师生
              </div>
              <div class="content_one_content_item_2">
                <div class="content_one_content_item_2_item_1 center">
                  0-5岁学生
                </div>
                <div class="content_one_content_item_2_item_2 center">
                  6-14岁学生
                </div>
                <div class="content_one_content_item_2_item_3 center">
                  ≥15岁学生
                </div>
                <div class="content_one_content_item_2_item_4 center">
                  教职工
                </div>
              </div>
              <div class="content_one_content_item_3">
                <div class="content_one_content_item_3_item_1 center" style="font-size: 1.5rem;">
                  {{ tbHealthScreening.student0To5Years ? '☑' : '□' }}
                </div>
                <div class="content_one_content_item_3_item_2 center" style="font-size: 1.5rem;">
                  {{ tbHealthScreening.student6To14Years ? '☑' : '□' }}
                </div>
                <div class="content_one_content_item_3_item_3 center" style="font-size: 1.5rem;">
                  {{ tbHealthScreening.studentOver15Years ? '☑' : '□' }}
                </div>
                <div class="content_one_content_item_3_item_4 center" style="font-size: 1.5rem;">
                  {{ tbHealthScreening.schoolStaff ? '☑' : '□' }}
                </div>
              </div>
            </div>
          </div>
          <div class="content_tow">
            <div class="content_tow_item_column3_2">
              <div class="content_tow_item_column3_item_1">
                僧尼
              </div>
              <div class="content_tow_item_column3_item_1point5">
                <div class="content_tow_item_column3_item_1point5_item_col_1 center">
                  0-5岁
                </div>
                <div class="content_tow_item_column3_item_1point5_item_col_1 center">
                  6-14岁
                </div>
                <div
class="content_tow_item_column3_item_1point5_item_col_1 center"
                     style="border-bottom-style: none;">
                  ≥ 15岁
                </div>
              </div>
              <div class="content_tow_item_column3_item_1point5">
                <div class="content_tow_item_column3_item_1_item_col_1" style="font-size: 1.5rem;">
                  {{ tbHealthScreening.monkOrNun0To5Years ? '☑' : '□' }}
                </div>
                <div class="content_tow_item_column3_item_1_item_col_1" style="font-size: 1.5rem;">
                  {{ tbHealthScreening.monkOrNun6To14Years ? '☑' : '□' }}
                </div>
                <div
class="content_tow_item_column3_item_1_item_col_1"
                     style="border-bottom-style: none;font-size: 1.5rem;">
                  {{ tbHealthScreening.monkOrNunOver15Years ? '☑' : '□' }}
                </div>
              </div>
            </div>
            <div class="content_tow_item_column3_1">
              <div class="content_tow_item_column3_item_2point5">
                老年人
              </div>
              <div class="content_tow_item_column3_item_1" style="font-size: 1.5rem;">
                {{ tbHealthScreening.elderly ? '☑' : '□' }}
              </div>
            </div>
            <div class="content_tow_item_column3_1" style="border-bottom-style: none;">
              <div class="content_tow_item_column3_item_2point5">
                糖尿病患者
              </div>
              <div class="content_tow_item_column3_item_1" style="font-size: 1.5rem;">
                {{ tbHealthScreening.diabetesPatient ? '☑' : '□' }}
              </div>
            </div>
          </div>
          <div class="content_three">
            <div class="content_three_item_1">
              <div class="content_three_item_1_4">
                HIV/AIDS
              </div>
              <div class="content_three_item_1_1" style="font-size: 1.5rem;">
                {{ tbHealthScreening.HIVorAIDS ? '☑' : '□' }}
              </div>
            </div>
            <div class="content_three_item_1">
              <div class="content_three_item_1_4">
                既往结核病患者
              </div>
              <div class="content_three_item_1_1" style="font-size: 1.5rem;">
                {{ tbHealthScreening.pastTBPatient ? '☑' : '□' }}
              </div>
            </div>
            <div class="content_three_item_2">
              <div class="content_three_item_2_1" style="padding-top: 8vh;padding-left: 1vw;">
                非重点人群
              </div>
              <div class="content_three_item_2_1">
                <div class="content_three_item_2_1_col_1">
                  0-5岁
                </div>
                <div class="content_three_item_2_1_col_1">
                  6-14岁
                </div>
                <div class="content_three_item_2_1_col_1" style="border-bottom-style: none;">
                  ≥15岁
                </div>
              </div>
              <div class="content_three_item_2_0point5">
                <div class="content_three_item_2_1_col_1" style="font-size: 1.5rem;">
                  {{ tbHealthScreening.nonKeyPopulation0To5Years ? '☑' : '□' }}
                </div>
                <div class="content_three_item_2_1_col_1" style="font-size: 1.5rem;">
                  {{ tbHealthScreening.nonKeyPopulation6To14Years ? '☑' : '□' }}
                </div>
                <div class="content_three_item_2_1_col_1" style="border-bottom-style: none;font-size: 1.5rem;">
                  {{ tbHealthScreening.nonKeyPopulationOver15Years ? '☑' : '□' }}
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="tips_content">
          <strong>活动性肺结核密切接触者：</strong>
          <span style="font-style: italic ; ">
              症状筛查+ppd+胸片检查，异常或强阳性进行实验室检查。
          </span>
          <br/>
          <strong>0-5岁学生：</strong>
          <span style="font-style: italic ; ">
            症状筛查+查验卡痕，有症状做 ppd，强阳性进一步检查；
          </span>
          <strong>6-14 岁学生：</strong>
          <span style="font-style: italic ; ">
            症状筛查+ppd+查验卡痕，有症状或强阳性进一步检查；
          </span>
          <strong>≥15 岁学生：</strong>
          <span style="font-style: italic ; ">
            症状筛查+ppd+查验卡痕+胸片检查，有症状或强阳性或异常进一步检查；
          </span>
          <strong>教职工：</strong>
          <span style="font-style: italic ; ">
            症状筛查+胸片检查，有症状或异常进一步检查。
          </span>
          <br/>
          <strong>僧尼：</strong>
          <span style="font-style: italic ; ">
            0-5 岁、6-14 岁同学生；≥15 岁症状筛查+查验卡痕+胸片检查，有症状或强阳性或异常进一步检查。
          </span>
          <br/>
          <strong>老年人、糖尿病患者、HIV/AIDS 和既往结核病患者：</strong>
          <span style="font-style: italic ; ">
            症状筛查+胸片检查，有症状或异常进一步检查。
          </span>
          <br/>
          <strong>0-5 岁非重点人群：</strong>
          <span style="font-style: italic ; ">
            症状筛查+查验卡痕，有症状做 ppd，强阳性进一步检查；
          </span>
          <strong>6-14 岁非重点人群：</strong>
          <span style="font-style: italic ; ">
            症状筛查+ppd+查验卡痕，有症状或强阳性进一步检查；
          </span>
          <strong>≥15 岁非重点人群：</strong>
          <span style="font-style: italic ; ">
            症状筛查+胸片检查，有症状或异常进一步检查。
          </span>
        </div>
        <div class="symptom_input_content">
          <div class="symptom_input_content_item_1">
            <strong style="padding: 2vh;font-size: 1.5vw;">您最近 1 个月内是否有以下症状？</strong>
          </div>
          <div class="symptom_input_content_item_1">
            <div class="symptom_input_content_item_1_item4_1 " style="margin-left: 0.5vw;">
              1）咳嗽、咳痰（超过 1 周）
            </div>
            <div class="symptom_input_content_item_1_item4_1">
              {{ tbHealthScreening.coughOrSputumForMoreThanOneWeek ? '有' : '无' }}
            </div>
            <div class="symptom_input_content_item_1_item4_1" style="margin-right: 0.5vw;">
              5）夜间盗汗
            </div>
            <div class="symptom_input_content_item_1_item4_1" style="border-right-style: none;">
              {{ tbHealthScreening.nightSweats ? '有' : '无' }}
            </div>
          </div>
          <div class="symptom_input_content_item_1">
            <div class="symptom_input_content_item_1_item4_1" style="margin-left: 0.5vw;">
              2）咯血或血痰
            </div>
            <div class="symptom_input_content_item_1_item4_1">
              {{ tbHealthScreening.hemoptysisOrBloodSputum ? '有' : '无' }}
            </div>
            <div class="symptom_input_content_item_1_item4_1" style="margin-right: 0.5vw;">
              6）食欲不振
            </div>
            <div class="symptom_input_content_item_1_item4_1" style="border-right-style: none;">
              {{ tbHealthScreening.lossOfAppetite ? '有' : '无' }}
            </div>
          </div>
          <div class="symptom_input_content_item_1">
            <div class="symptom_input_content_item_1_item4_1" style="margin-left: 0.5vw;">
              3）发热
            </div>
            <div class="symptom_input_content_item_1_item4_1" style="border-right-style: none;">
              {{ tbHealthScreening.fever ? '有' : '无' }}
            </div>
            <div class="symptom_input_content_item_1_item4_1" style="border-left: 1px solid;margin-right: 0.5vw;">
              7）乏力
            </div>
            <div class="symptom_input_content_item_1_item4_1" style=" border-right-style: none;">
              {{ tbHealthScreening.fatigue ? '有' : '无' }}
            </div>
          </div>
          <div class="symptom_input_content_item_1">
            <div class="symptom_input_content_item_1_item4_1" style="margin-left: 0.5vw;">
              4）胸痛
            </div>
            <div class="symptom_input_content_item_1_item4_1" style="border-right-style: none;">
              {{ tbHealthScreening.chestPain ? '有' : '无' }}
            </div>
            <div class="symptom_input_content_item_1_item4_1" style="border-left: 1px solid;margin-right: 0.5vw;">
              8）体重减轻（超过 6 斤）
            </div>
            <div class="symptom_input_content_item_1_item4_1" style="border-right-style: none;">
              {{ tbHealthScreening.weightLossOverSixPounds ? '有' : '无' }}
            </div>
          </div>
          <div class="symptom_input_content_item_1">
            <div class="symptom_input_content_item_1_item4_1">
              <div class="symptom_input_content_item_1_item4_1_1" style="font-size: 1.5rem;">
                {{ tbHealthScreening.doneCheckMark ? '☑' : '□' }}
              </div>
              <div class="symptom_input_content_item_1_item4_1_3">
                <strong>查验卡痕</strong>
              </div>
            </div>
            <div class="symptom_input_content_item_1_item4_2" style="font-size: 1rem;">
              {{ tbHealthScreening.checkMark ? '☑有' : '□有' }}
              {{ (tbHealthScreening.doneCheckMark && !tbHealthScreening.checkMark) ? '☑无' : '□无' }}
              □无法判断
            </div>
            <div class="symptom_input_content_item_1_item4_1" style="border-right-style: none;">
              医生签字：
              <el-image
style="width: 50px; height: 50px"
                        :src="tbHealthScreening.collectDoctorSignature"
                        :preview-src-list="[tbHealthScreening.collectDoctorSignature]"
              />
            </div>
          </div>
          <div class="symptom_input_content_item_1">
            <div class="symptom_input_content_item_1_item4_1">
              <div class="symptom_input_content_item_1_item4_1_1" style="font-size: 1.5rem;">
                {{ tbHealthScreening.ppdTestDone ? '☑' : '□' }}
              </div>
              <div class="symptom_input_content_item_1_item4_1_3" style="border-right-style: none;">
                <strong>ppd</strong>
              </div>
            </div>
            <div class="symptom_input_content_item_1_item4_2">
              注射时间：{{ tbHealthScreening.ppdInjectionTimeStr }}
            </div>
            <div class="symptom_input_content_item_1_item4_1" style="border-right-style: none;">
              医生签字：
              <el-image
style="width: 50px; height: 50px"
                        :src="tbHealthScreening.ppdDoctorSignature"
                        :preview-src-list="[tbHealthScreening.ppdDoctorSignature]"
              />
            </div>
          </div>
          <div class="symptom_input_content_item_2">
            <div class="symptom_input_content_item_1_item4_1">
              <div class="symptom_input_content_item_1_item4_1_1" style="font-size: 1.5rem;">
                {{ tbHealthScreening.chestXRayDone ? '☑' : '□' }}
              </div>
              <div class="symptom_input_content_item_1_item4_1_3" style="border-right-style: none;">
                <strong>胸部 X 线</strong>
              </div>
            </div>
            <div class="symptom_input_content_item_1_item4_2">
              {{
                tbHealthScreening.noTBRelatedAbnormalities ? '☑无结核相关异常' : '□无结核相关异常'
              }}
              {{ tbHealthScreening.suspectedTB ? '☑疑似结核' : '□疑似结核' }}
              <br/>
              <div style="padding-top: 2vh;">
                机器中与患者对应的编码：{{ tbHealthScreening.chestXRayCode }}
              </div>
            </div>
            <div class="symptom_input_content_item_1_item4_1" style="border-right-style: none;">
              医生签字：
              <el-image
style="width: 50px; height: 50px"
                        :src="tbHealthScreening.chestXRayDoctorSignature"
                        :preview-src-list="[tbHealthScreening.chestXRayDoctorSignature]"
              />
            </div>
          </div>
          <div
class="symptom_input_content_item_1 none_border_bottom "
               style="border-top: 1px solid ; border-bottom-style: none">
            <div class="symptom_input_content_item_1_item4_1 ">
              <div class="symptom_input_content_item_1_item4_1_1" style="font-size: 1.5rem;">
                {{ tbHealthScreening.sputumSpecimenDone ? '☑' : '□' }}
              </div>
              <div class="symptom_input_content_item_1_item4_1_3 ">
                <strong>痰标本</strong>
              </div>
            </div>
            <div class="symptom_input_content_item_1_item4_2">
              {{ tbHealthScreening.immediateSputum ? '☑即时痰' : '□即时痰' }}
              {{ tbHealthScreening.morningSputum ? '☑发放晨痰' : '□发放晨痰' }}
              {{ tbHealthScreening.nighttimeSputum ? '☑夜间痰盒' : '□夜间痰盒' }}
              {{ tbHealthScreening.noSputum ? '☑无痰' : '□无痰' }}
            </div>
            <div class="symptom_input_content_item_1_item4_1" style="border-right-style: none;">
              医生签字：
              <el-image
style="width: 50px; height: 50px"
                        :src="tbHealthScreening.sputumDoctorSignature"
                        :preview-src-list="[tbHealthScreening.sputumDoctorSignature]"
              />

            </div>
          </div>
        </div>
        <div class="check_signature">
<!--          质检人员签字:-->
        </div>
      </div>
      <div v-show="currentItemIndex === 2 || currentItemIndex === 3 || currentItemIndex === 5">
        <ContentWrap style="height: 100%; width: 90%; margin-left: 5%; margin-top: 2%">
          <el-image
v-for="(item, index) in srcImageList" :key="index"
                    style="width: 200px; height: 200px; margin-left: 1%"
                    :src="item"
                    :zoom-rate="1.2"
                    :max-scale="1.5"
                    :min-scale="0.2"
                    :preview-src-list="[item]"
                    :initial-index="3"
                    fit="cover"
          />

          <el-empty v-if="srcImageList && srcImageList.length == 0" description="暂时无影像数据"/>
        </ContentWrap>
      </div>
      <div v-show="currentItemIndex === 4" style="padding: 3%">
        <el-form
          ref="formRef"
          :model="formData"
          label-width="110px"
        >
          <!-- 此处写法仅支持 element-plus 2.6.0以下版本 2.6.0 以上版本查看官网修改-->
          <div style="margin: 0 1% 1% 1% ; font-weight: bold">（1）痰菌检查</div>
          <el-form-item label="涂片结果：" prop="smearResult" style="margin: 0">
            <el-radio-group v-model="formData.smearResult" disabled="true">
              <el-radio :label="1">阳性</el-radio>
              <el-radio :label="2">阴性</el-radio>
              <el-radio :label="3">未查</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="培养结果：" prop="cultureResult" style="margin: 0">
            <el-radio-group v-model="formData.cultureResult" disabled="true">
              <el-radio :label="1">阳性</el-radio>
              <el-radio :label="2">阴性</el-radio>
              <el-radio :label="3">污染</el-radio>
              <el-radio :label="4">未查</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="分子生物学：" prop="molecularBiology" style="margin: 0">
            <el-radio-group v-model="formData.molecularBiology" disabled="true">
              <el-radio :label="1">结核分枝杆菌核酸阳性</el-radio>
              <el-radio :label="2">未检出结核分枝杆菌</el-radio>
              <el-radio :label="3">不确定</el-radio>
              <el-radio :label="4">未查</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-divider style="margin: 1%"/>
          <div style="margin: 0 1% 1% 1% ; font-weight: bold">（2）组织标本</div>

          <el-form-item label="检测结果：" prop="tissueSpecimenResult" style="margin: 0">
            <el-radio-group v-model="formData.tissueSpecimenResult" disabled="true">
              <el-radio :label="1">组织学阳性</el-radio>
              <el-radio :label="2">仅病理学阳性</el-radio>
              <el-radio :label="3">阴性</el-radio>
              <el-radio :label="4">未查</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-divider style="margin: 1%"/>
          <div style="margin: 0 1% 1% 1% ; font-weight: bold">（3）菌种鉴定</div>

          <el-form-item label="检测结果：" prop="strainIdentificationResult" style="margin: 0">
            <el-radio-group v-model="formData.strainIdentificationResult" disabled="true">
              <el-radio :label="1">结核分枝杆菌复合群</el-radio>
              <el-radio :label="2">非结核分枝杆菌</el-radio>
              <el-radio :label="3">未查</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-divider style="margin: 1%"/>
          <div style="margin: 0 1% 1% 1% ; font-weight: bold">（4）结核分支杆菌药敏检查</div>
          <el-form-item label="药敏检测方法：" prop="tbDrugSensitivityMethod" style="margin: 0">
            <el-radio-group v-model="formData.tbDrugSensitivityMethod" disabled="true">
              <el-radio :label="1">分子生物学</el-radio>
              <el-radio :label="2">传统药敏试验</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="耐药综合判定：" prop="drugResistanceResult" style="margin: 0">
            <el-radio-group v-model="formData.drugResistanceResult" disabled="true">
              <el-radio :label="1">单耐利福平</el-radio>
              <el-radio :label="2">耐多药</el-radio>
              <el-radio :label="3">广泛耐药</el-radio>
              <el-radio :label="4">单耐异烟肼</el-radio>
              <el-radio :label="5">利福平与异烟肼均敏感</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-divider style="margin: 1%"/>
          <div style="margin: 0 1% 1% 1% ; font-weight: bold">（5）HIV抗体检测</div>
          <el-form-item label="抗体检测结果：" prop="hivResult" style="margin: 0">
            <el-radio-group v-model="formData.hivResult" disabled="true">
              <el-radio :label="1">已知阳性</el-radio>
              <el-radio :label="2">新检测初筛阳性</el-radio>
              <el-radio :label="3">新检测确认阳性</el-radio>
              <el-radio :label="4">阴性</el-radio>
              <el-radio :label="5">拒查</el-radio>
              <el-radio :label="6">未提供</el-radio>
            </el-radio-group>
          </el-form-item>

        </el-form>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import {ScreenDiagnosisApi} from '@/api/tb/screendiagnosis'
import ContentWrap from "@/components/ContentWrap/src/ContentWrap.vue";
import {Paperclip} from "@element-plus/icons-vue";

const {query} = useRoute() // 路由的查询
/** 诊断组 表单 */
defineOptions({name: 'ScreenDiagnosisForm'})

const {t} = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题


const value = ref('')
const options = ref([])

const srcImageList = ref([])


// 左侧 诊断结果、治疗方案、是否网报数据
const leftFormData = ref({
  preventiveTreatment: undefined,
  outcome: undefined,
  report: undefined,
  treatmentProgram: undefined
})

// 实验室检测结果 表单
const formData = ref({
  smearResult: undefined,
  cultureResult: undefined,
  molecularBiology: undefined,
  tissueSpecimenResult: undefined,
  strainIdentificationResult: undefined,
  tbDrugSensitivityMethod: undefined,
  drugResistanceResult: undefined,
  hivResult: undefined
})

// 当前选中的检查索引
const currentItemIndex = ref()
// 五个检查项的ref对象
const physical_examination = ref()
const dr = ref()
const ct = ref()
const experiment = ref()
const electrocardiogram = ref()

// 获取 左侧 诊断结果、治疗方案、是否网报、是否预防性治疗缺省数据
const getLeftInfo = async () => {
  const personId = query.personId !== undefined ? Number(query.personId) : -1;
  const data = await ScreenDiagnosisApi.getLastTimeDiagnosisResult(personId)
  if (data) {
    leftFormData.value = data
  }
}

// 体检表需要显示的数据项
const tbHealthScreening = ref({
  screeningNumber: undefined,
  idNumber: undefined,
  name: undefined,
  age: undefined,
  examinationDate: undefined,
  closeContactWithActivePulmonaryTB: undefined,
  monkOrNun0To5Years: undefined,
  monkOrNun6To14Years: undefined,
  monkOrNunOver15Years: undefined,
  pastTBPatient: undefined,
  student0To5Years: undefined,
  student6To14Years: undefined,
  studentOver15Years: undefined,
  schoolStaff: undefined,
  elderly: undefined,
  diabetesPatient: undefined,
  nonKeyPopulation0To5Years: undefined,
  nonKeyPopulation6To14Years: undefined,
  nonKeyPopulationOver15Years: undefined,
  coughOrSputumForMoreThanOneWeek: undefined,
  hemoptysisOrBloodSputum: undefined,
  HIVorAIDS: undefined,
  fever: undefined,
  persistentFever: undefined,
  lymphoidEnlargement: undefined,
  chestPain: undefined,
  nightSweats: undefined,
  lossOfAppetite: undefined,
  fatigue: undefined,
  weightLossOverSixPounds: undefined,
  checkMark: undefined,
  doneCheckMark: undefined,
  ppdTestDone: undefined,
  ppdInjectionTime: undefined,
  ppdInjectionTimeStr: undefined,
  ppdDoctorSignature: undefined,
  chestXRayDone: undefined,
  noTBRelatedAbnormalities: undefined,
  suspectedTB: undefined,
  chestXRayCode: undefined,
  outcome: undefined,
  chestXRayDoctorSignature: undefined,
  sputumSpecimenDone: undefined,
  immediateSputum: undefined,
  morningSputum: undefined,
  nighttimeSputum: undefined,
  noSputum: undefined,
  type: undefined,
  sputumDoctorSignature: undefined,
  collectDoctorSignature: undefined,
  hivorAIDS: undefined
})

// 选中体检项目
const clickItem = async (index) => {
  currentItemIndex.value = index
  const personId = query.personId ? Number(query.personId) : -1;

  // 给当前item添加选中样式
  switch (index) {
    case 1:
      dr.value.classList.remove('class_hover')
      ct.value.classList.remove('class_hover')
      experiment.value.classList.remove('class_hover')
      electrocardiogram.value.classList.remove('class_hover')

      physical_examination.value.classList.value += ' class_hover'

      // 获取选择器下拉列表
      const result_1 = await ScreenDiagnosisApi.getCheckSelectList(1, personId)
      if (result_1[0]) {
        tbHealthScreening.value = result_1[0].tbHealthScreening
        console.log(tbHealthScreening.value)
        console.log(tbHealthScreening.value.doneCheckMark)
        console.log(tbHealthScreening.checkMark === undefined)
      }

      break;
    case 2:
      physical_examination.value.classList.remove('class_hover')
      ct.value.classList.remove('class_hover')
      experiment.value.classList.remove('class_hover')
      electrocardiogram.value.classList.remove('class_hover')

      dr.value.classList.value += ' class_hover'

      // 获取选择器下拉列表
      const result_2 = await ScreenDiagnosisApi.getCheckSelectList(2, personId)
      options.value = result_2
      // 清空上一次的下拉选择器列表 图片列表
      value.value = ''
      srcImageList.value = []
      if (options.value.length !== 0) {
        // 清空选中的选择器
        value.value = options.value[0]
        srcImageList.value = options.value[0].imageUrls
      }

      break;
    case 3:
      physical_examination.value.classList.remove('class_hover')
      dr.value.classList.remove('class_hover')
      experiment.value.classList.remove('class_hover')
      electrocardiogram.value.classList.remove('class_hover')

      ct.value.classList.value += ' class_hover'

      // 获取选择器下拉列表
      const result_3 = await ScreenDiagnosisApi.getCheckSelectList(3, personId)
      options.value = result_3
      // 清空上一次的下拉选择器列表 图片列表
      value.value = ''
      srcImageList.value = []
      if (options.value.length !== 0) {
        // 清空选中的选择器
        value.value = options.value[0]
        srcImageList.value = options.value[0].imageUrls
      }

      break;
    case 4:
      physical_examination.value.classList.remove('class_hover')
      dr.value.classList.remove('class_hover')
      ct.value.classList.remove('class_hover')
      electrocardiogram.value.classList.remove('class_hover')

      experiment.value.classList.value += ' class_hover'

      // 获取选择器下拉列表
      const result_4 = await ScreenDiagnosisApi.getCheckSelectList(4, personId)
      options.value = result_4

      // 清空上一次的下拉选择器列表 图片列表
      value.value = ''
      srcImageList.value = []

      if (options.value.length !== 0) {
        // 清空选中的选择器
        value.value = options.value[0]

        // 获取第一条实验组数据
        formData.value = result_4[0].experimentGroup
      }

      break;
    case 5:
      physical_examination.value.classList.remove('class_hover')
      dr.value.classList.remove('class_hover')
      ct.value.classList.remove('class_hover')
      experiment.value.classList.remove('class_hover')

      electrocardiogram.value.classList.value += ' class_hover'

      // 获取选择器下拉列表
      const result_5 = await ScreenDiagnosisApi.getCheckSelectList(5, personId)
      options.value = result_5
      // 清空上一次的下拉选择器列表 图片列表
      value.value = ''
      srcImageList.value = []
      if (options.value.length !== 0) {
        // 清空选中的选择器
        value.value = options.value[0]
        srcImageList.value = options.value[0].imageUrls
      }

      break;
  }
}

// 提交诊断结果
const submitDiagnosisResult = async () => {
  // 提交前追加新属性数据
  leftFormData.value.screenId = query.screenId
  leftFormData.value.screenPoint = query.screenPoint
  leftFormData.value.personId = query.personId
  leftFormData.value.screenTime = query.screenTime

  await ScreenDiagnosisApi.createScreenDiagnosis(leftFormData.value)
  message.success('提交诊断结果成功')
}

// 监听下拉选择change事件
const change = (id) => {
  // 选中下拉筛查次序/时间 后显示对应的数据 DR,CT,心电图的数据
  if (currentItemIndex.value === 2 || currentItemIndex.value === 3 || currentItemIndex.value === 5) {
    const selectDataList = options.value.filter(item => item.value === id)
    if (selectDataList && selectDataList.length > 0) {
      srcImageList.value = selectDataList[0].imageUrls
    }
  }

  // 实验室
  if (currentItemIndex.value === 4) {
    const selectDataList = options.value.filter(item => item.value === id)
    if (selectDataList && selectDataList.length > 0) {
      formData.value = selectDataList[0].experimentGroup
    }
  }
}

onMounted(async () => {
  if (!query || !query.personId) {
    message.error('无法直接访问')
    return
  }
  // 默认选中第一个
  await clickItem(1)
  // 获取左侧 诊断结果、治疗方案、是否网报数据
  await getLeftInfo()
})
</script>
<style scoped lang="scss">
.main {
  width: 100%;
  height: 85vh;
  display: flex;
  font-size: 1.1rem;
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
  padding: 5px;
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
  height: 30vh;
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
        flex: 3.4;
        padding-left: 1vw;
      }

      .content_one_head_right {
        border-bottom: 1px solid;
        flex: 1;
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
        flex: 1;
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
          padding-left: 0.5vw;
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
        flex: 2.3;
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
      flex: 0.1;
      border-bottom: 1px solid;

      display: flex;

      .content_three_item_1_1 {
        flex: 1;
        border-left: 1px solid;

        padding-top: 1vh;
        padding-left: 1vw;
      }

      .content_three_item_1_4 {
        flex: 2.3;
        padding-top: 1vh;
        padding-left: 1.5vw;
      }
    }

    .content_three_item_2 {
      flex: 2;

      display: flex;

      .content_three_item_2_1 {
        flex: 0.5;
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
