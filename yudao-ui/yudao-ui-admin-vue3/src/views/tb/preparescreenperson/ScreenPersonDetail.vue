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

    <ContentWrap>
      <el-tabs
        v-model="activeName"
        type="card"
        class="demo-tabs"
        @tab-click="handleClick"
      >
        <el-tab-pane label="体检单" name="first">
          <div class="physical_examination_diagnosis">
            <div class="text_name">体检表</div>
            <div class="input_content">
              <div class="info_code" style="font-size: 18px">
                筛查编号：{{ tbHealthScreening.screeningNumber }}
              </div>
              <div class="info_idCard" style="font-size: 18px">
                身份证号：{{ tbHealthScreening.idNumber }}
              </div>
              <div class="info_name" style="font-size: 18px">姓名：{{ tbHealthScreening.name }}</div>
              <div class="info_age" style="font-size: 18px">年龄：{{ tbHealthScreening.age }} 岁</div>
              <div class="info_date" style="font-size: 18px">
                体检日期：{{ tbHealthScreening.examinationDate }}
              </div>
            </div>
            <div class="crowd_class">
              <div class="crowd_class_title">人群分类（可多选）</div>
            </div>
            <div class="crowd_class_content">
              <div class="content_one">
                <div class="content_one_head ">
                  <div class="content_one_head_left center" style="font-size: 18px">
                    活动性肺结核密切接触者
                  </div>
                  <div class="content_one_head_right center" style="font-size: 30px">
                    {{ tbHealthScreening.closeContactWithActivePulmonaryTB ? '☑' : '□' }}
                  </div>
                </div>

                <div class="content_one_content ">
                  <div class="content_one_content_item_1 " style="font-size: 18px">
                    在校师生
                  </div>
                  <div class="content_one_content_item_2" style="font-size: 18px">
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
                  <div class="content_one_content_item_3" style="font-size: 30px">
                    <div class="content_one_content_item_3_item_1 center">
                      {{ tbHealthScreening.student0To5Years ? '☑' : '□' }}
                    </div>
                    <div class="content_one_content_item_3_item_2 center">
                      {{ tbHealthScreening.student6To14Years ? '☑' : '□' }}
                    </div>
                    <div class="content_one_content_item_3_item_3 center">
                      {{ tbHealthScreening.studentOver15Years ? '☑' : '□' }}
                    </div>
                    <div class="content_one_content_item_3_item_4 center">
                      {{ tbHealthScreening.schoolStaff ? '☑' : '□' }}
                    </div>
                  </div>
                </div>
              </div>
              <div class="content_tow">
                <!--                <div class="content_tow_item_column3_2">
                                  <div class="content_tow_item_column3_item_1" style="font-size: 18px">
                                    僧尼
                                  </div>
                                  <div class="content_tow_item_column3_item_1point5" style="font-size: 18px">
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
                                  <div class="content_tow_item_column3_item_1point5" style="font-size: 30px">
                                    <div class="content_tow_item_column3_item_1_item_col_1">
                                      {{ tbHealthScreening.monkOrNun0To5Years ? '☑' : '□' }}
                                    </div>
                                    <div class="content_tow_item_column3_item_1_item_col_1">
                                      {{ tbHealthScreening.monkOrNun6To14Years ? '☑' : '□' }}
                                    </div>
                                    <div
                                      class="content_tow_item_column3_item_1_item_col_1"
                                      style="border-bottom-style: none;">
                                      {{ tbHealthScreening.monkOrNunOver15Years ? '☑' : '□' }}
                                    </div>
                                  </div>
                                </div>-->
                <div class="content_tow_item_column3_1">
                  <div class="content_tow_item_column3_item_2point5" style="font-size: 18px">
                    老年人
                  </div>
                  <div class="content_tow_item_column3_item_1" style="font-size: 30px">
                    {{ tbHealthScreening.elderly ? '☑' : '□' }}
                  </div>
                </div>
                <div class="content_tow_item_column3_1" style="border-bottom-style: none;">
                  <div class="content_tow_item_column3_item_2point5" style="font-size: 18px">
                    糖尿病患者
                  </div>
                  <div class="content_tow_item_column3_item_1" style="font-size: 30px">
                    {{ tbHealthScreening.diabetesPatient ? '☑' : '□' }}
                  </div>
                </div>
              </div>
              <div class="content_three">
                <div class="content_three_item_1">
                  <div class="content_three_item_1_4" style="font-size: 18px">
                    HIV/AIDS
                  </div>
                  <div class="content_three_item_1_1" style="font-size: 30px">
                    {{ tbHealthScreening.hivorAIDS ? '☑' : '□' }}
                  </div>
                </div>
                <div class="content_three_item_1">
                  <div class="content_three_item_1_4" style="font-size: 18px">
                    既往结核病患者
                  </div>
                  <div class="content_three_item_1_1" style="font-size: 30px">
                    {{ tbHealthScreening.pastTBPatient ? '☑' : '□' }}
                  </div>
                </div>
                <div class="content_three_item_2">
                  <div class="content_three_item_2_1"
                       style="padding-top: 6vh;padding-left: 1vw;font-size: 18px">
                    非重点人群
                  </div>
                  <div class="content_three_item_2_1" style="font-size: 18px">
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
                  <div class="content_three_item_2_0point5" style="font-size: 30px">
                    <div class="content_three_item_2_1_col_1">
                      {{ tbHealthScreening.nonKeyPopulation0To5Years ? '☑' : '□' }}
                    </div>
                    <div class="content_three_item_2_1_col_1">
                      {{ tbHealthScreening.nonKeyPopulation6To14Years ? '☑' : '□' }}
                    </div>
                    <div class="content_three_item_2_1_col_1" style="border-bottom-style: none;">
                      {{ tbHealthScreening.nonKeyPopulationOver15Years ? '☑' : '□' }}
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="tips_content" style="font-size: 18px">
              <strong>活动性肺结核密切接触者：</strong> 症状筛查+ppd+胸片检查，异常或强阳性进行实验室检查。
              <br/>
              <strong>0-5岁学生：</strong> 症状筛查，有症状做 ppd，强阳性进一步检查；<strong>6-14
              岁学生：</strong>症状筛查+ppd，有症状或强
              阳性进一步检查；<strong>≥15
              岁学生：</strong>症状筛查+ppd+胸片检查，有症状或强阳性或异常进一步检查；<strong>教职工：</strong>症状筛
              查+胸片检查，有症状或异常进一步检查。
              <br/>
              <!--              <strong>僧尼：</strong>0-5 岁、6-14 岁同学生；≥15 岁症状筛查+查验卡痕+胸片检查，有症状或强阳性或异常进一步检查。
                            <br/>-->
              <strong>老年人、糖尿病患者、HIV/AIDS 和既往结核病患者：</strong>症状筛查+胸片检查，有症状或异常进一步检查。
              <br/>
              <strong>0-5 岁非重点人群：</strong>症状筛查，有症状做 ppd，强阳性进一步检查；<strong>6-14
              岁非重点人群：</strong>症状筛查+ppd，
              有症状或强阳性进一步检查；<strong>≥15 岁非重点人群：</strong>症状筛查+胸片检查，有症状或异常进一步检查。
            </div>
            <div class="symptom_input_content">
              <div class="symptom_input_content_item_1">
                <strong style="padding: 2vh;font-size: 1.5vw;">您最近 1 个月内是否有以下症状？</strong>
              </div>
              <div class="symptom_input_content_item_1" style="font-size: 18px">
                <div class="symptom_input_content_item_1_item4_1 " style="margin-left: 0.5vw;">
                  1）咳嗽、咳痰（超过 2 周）
                </div>
                <div class="symptom_input_content_item_1_item4_1">
                  {{ tbHealthScreening.coughOrSputumForMoreThanOneWeek ? '有' : '无' }}
                </div>
                <div class="symptom_input_content_item_1_item4_1" style="margin-right: 0.5vw;">
                  5）乏力、盗汗
                </div>
                <div class="symptom_input_content_item_1_item4_1" style="border-right-style: none;">
                  {{ tbHealthScreening.nightSweats ? '有' : '无' }}
                </div>
              </div>
              <div class="symptom_input_content_item_1" style="font-size: 18px">
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
              <div class="symptom_input_content_item_1" style="font-size: 18px">
                <div class="symptom_input_content_item_1_item4_1" style="margin-left: 0.5vw;">
                  3）发热
                </div>
                <div class="symptom_input_content_item_1_item4_1" style="border-right-style: none;">
                  {{ tbHealthScreening.fever ? '有' : '无' }}
                </div>
                <div class="symptom_input_content_item_1_item4_1"
                     style="border-left: 1px solid;margin-right: 0.5vw;">
                  7）体重减轻（超过 6 斤）
                </div>
                <div class="symptom_input_content_item_1_item4_1"
                     style=" border-right-style: none;">
                  {{ tbHealthScreening.weightLossOverSixPounds ? '有' : '无' }}
                </div>
              </div>
              <div class="symptom_input_content_item_1" style="font-size: 18px">
                <div class="symptom_input_content_item_1_item4_1" style="margin-left: 0.5vw;">
                  4）胸痛
                </div>
                <div class="symptom_input_content_item_1_item4_1" style="border-right-style: none;">
                  {{ tbHealthScreening.chestPain ? '有' : '无' }}
                </div>
                <div class="symptom_input_content_item_1_item4_1"
                     style="border-left: 1px solid;margin-right: 0.5vw">
                  <!--                  8）乏力-->
                </div>
                <div class="symptom_input_content_item_1_item4_1" style="border-right-style: none;">
                  <!--                  {{ tbHealthScreening.fatigue ? '有' : '无' }}-->
                </div>
              </div>
<!--              <div class="symptom_input_content_item_1">
                <div class="symptom_input_content_item_1_item4_1">
                  <div class="symptom_input_content_item_1_item4_1_1" style="font-size: 30px">
                    {{ tbHealthScreening.doneCheckMark ? '☑' : '□' }}
                  </div>
                  <div class="symptom_input_content_item_1_item4_1_3"
                       style="font-size: 18px; margin-top: 15px">
                    <strong>查验卡痕</strong>
                  </div>
                </div>
                <div class="symptom_input_content_item_1_item4_2"
                     style="font-size: 20px;margin-top: 15px; border-right-style: none;">
                  {{ tbHealthScreening.checkMark ? '☑有' : '□有' }}
                  {{
                    (tbHealthScreening.doneCheckMark && !tbHealthScreening.checkMark) ? '☑无' : '□无'
                  }}
                  □无法判断
                </div>
                <div class="symptom_input_content_item_1_item4_1"
                     style="border-right-style: none;border-left: 1px solid ;font-size: 18px">
                  医生签字：
                  <el-image
                    style="width: 50px; height: 50px"
                    :src="tbHealthScreening.collectDoctorSignature"
                    :preview-src-list="[tbHealthScreening.collectDoctorSignature]"
                  />
                </div>
              </div>-->
              <div class="symptom_input_content_item_1">
                <div class="symptom_input_content_item_1_item4_1">
                  <div class="symptom_input_content_item_1_item4_1_1" style="font-size: 30px">
                    {{ tbHealthScreening.ppdTestDone ? '☑' : '□' }}
                  </div>
                  <div class="symptom_input_content_item_1_item4_1_3"
                       style="border-right-style: none;font-size: 18px;margin-top: 15px">
                    <strong>ppd</strong>
                  </div>
                </div>
                <div class="symptom_input_content_item_1_item4_2"
                     style="font-size: 18px;margin-top: 15px; border-right-style: none; ">
                  注射时间：{{ tbHealthScreening.ppdInjectionTimeStr }}
                  <br/>
                  结果：{{tbHealthScreening.ppdOutcome == 1 ? "阳性":"阴性"}}
                </div>
                <div class="symptom_input_content_item_1_item4_1"
                     style="border-right-style: none;border-left: 1px solid ;font-size: 18px;">
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
                  <div class="symptom_input_content_item_1_item4_1_1" style="font-size: 30px">
                    {{ tbHealthScreening.chestXRayDone ? '☑' : '□' }}
                  </div>
                  <div class="symptom_input_content_item_1_item4_1_3"
                       style="border-right-style: none;font-size: 18px;margin-top: 45px">
                    <strong>胸部 X 线</strong>
                  </div>
                </div>
                <div class="symptom_input_content_item_1_item4_2"
                     style="font-size: 18px;margin-top: 15px;border-right-style: none; ">
                  {{ tbHealthScreening.noTBRelatedAbnormalities ? '☑无结核相关异常' : '□无结核相关异常' }}
                  {{ tbHealthScreening.suspectedTB ? '☑疑似结核' : '□疑似结核' }}
                  <br/>
                  <div style="padding-top: 2vh;">
                    机器中与患者对应的编码：{{ tbHealthScreening.chestXRayCode }}
                  </div>
                </div>
                <div class="symptom_input_content_item_1_item4_1"
                     style="border-right-style: none;border-left: 1px solid ;font-size: 18px;">
                  医生签字：
                  <el-image
                    style="width: 50px; height: 50px"
                    :src="tbHealthScreening.chestXRayDoctorSignature"
                    :preview-src-list="[tbHealthScreening.chestXRayDoctorSignature]"
                  />
                </div>
              </div>
<!--              <div
                class="symptom_input_content_item_1 none_border_bottom "
                style="border-top: 1px solid ; border-bottom-style: none">
                <div class="symptom_input_content_item_1_item4_1 ">
                  <div class="symptom_input_content_item_1_item4_1_1" style="font-size: 30px">
                    {{ tbHealthScreening.sputumSpecimenDone ? '☑' : '□' }}
                  </div>
                  <div class="symptom_input_content_item_1_item4_1_3 "
                       style="font-size: 18px;margin-top: 15px">
                    <strong>痰标本</strong>
                  </div>
                </div>
                <div class="symptom_input_content_item_1_item4_2"
                     style="font-size: 18px;margin-top: 15px;border-right-style: none; ">
                  {{ tbHealthScreening.immediateSputum ? '☑即时痰' : '□即时痰' }}
                  {{ tbHealthScreening.morningSputum ? '☑发放晨痰' : '□发放晨痰' }}
                  {{ tbHealthScreening.nighttimeSputum ? '☑夜间痰盒' : '□夜间痰盒' }}
                  {{ tbHealthScreening.noSputum ? '☑无痰' : '□无痰' }}
                </div>
                <div class="symptom_input_content_item_1_item4_1"
                     style="border-right-style: none;border-left: 1px solid ;font-size: 18px">
                  医生签字：
                  <el-image
                    style="width: 50px; height: 50px"
                    :src="tbHealthScreening.sputumDoctorSignature"
                    :preview-src-list="[tbHealthScreening.sputumDoctorSignature]"
                  />

                </div>
              </div>-->
            </div>
<!--            <div class="check_signature" style="font-size: 18px">质检人员签字:</div>-->
          </div>
        </el-tab-pane>
        <el-tab-pane label="采集" name="checkGroup">
          <el-table :data="checkList" :stripe="true" max-height="400px">
            <el-table-column label="筛查次序" prop="screenOrder" align="center" width="100"/>
            <el-table-column label="咳嗽、咳痰不小于2周" align="center">
              <template #default="scope">
                {{ scope.row.outcome.includes('1') ? '有' : '无' }}
              </template>
            </el-table-column>
            <el-table-column label="血痰、咯血" align="center">
              <template #default="scope">
                {{ scope.row.outcome.includes('2') ? '有' : '无' }}
              </template>
            </el-table-column>
            <el-table-column label="乏力、盗汗" align="center">
              <template #default="scope">
                {{ scope.row.outcome.includes('3') ? '有' : '无' }}
              </template>
            </el-table-column>
            <el-table-column label="体重减轻(超过6斤)" align="center" width="100">
              <template #default="scope">
                {{ scope.row.outcome.includes('4') ? '有' : '无' }}
              </template>
            </el-table-column>
            <el-table-column label="发热" align="center">
              <template #default="scope">
                {{ scope.row.outcome.includes('5') ? '有' : '无' }}
              </template>
            </el-table-column>
            <el-table-column label="食欲不振" align="center">
              <template #default="scope">
                {{ scope.row.outcome.includes('6') ? '有' : '无' }}
              </template>
            </el-table-column>
            <el-table-column label="胸痛" align="center">
              <template #default="scope">
                {{ scope.row.outcome.includes('7') ? '有' : '无' }}
              </template>
            </el-table-column>
            <el-table-column label="两年内是否有与结核病患者的接触史" align="center" prop="contacted">
              <template #default="scope">
                {{ scope.row.contacted ? '是' : '否' }}
              </template>
            </el-table-column>
            <el-table-column label="筛查单位" align="center" prop="screenAgency"/>
            <el-table-column label="筛查时间" align="center" prop="screenTime" :formatter="dateFormatter2"/>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="PPD" name="ppdGroup">
          <el-table :data="PPDList" :stripe="true" max-height="400px">
            <el-table-column label="筛查次序" prop="screenOrder" align="center" width="100"/>
            <el-table-column label="硬结横径(mm)" align="center" prop="transverseDiameter" width="100"/>
            <el-table-column label="硬结纵径(mm)" align="center" prop="longitudinalDiameter" width="100"/>
            <el-table-column label="红晕横径(mm)" align="center" prop="blushTransverseDiameter"  width="100"/>
            <el-table-column label="红晕纵径(mm)" align="center" prop="blushLongitudinalDiameter"  width="100"/>
            <el-table-column label="水泡" align="center" >
              <template #default="scope">
                {{ scope.row.bleb.toString().includes('1') ? '是' : '否' }}
              </template>
            </el-table-column>
            <el-table-column label="双圈" align="center">
              <template #default="scope">
                {{ scope.row.bleb.toString().includes('2') ? '是' : '否' }}
              </template>
            </el-table-column>
            <el-table-column label="坏死" align="center">
              <template #default="scope">
                {{ scope.row.bleb.toString().includes('3') ? '是' : '否' }}
              </template>
            </el-table-column>
            <el-table-column label="淋巴管炎" align="center" width="90">
              <template #default="scope">
                {{ scope.row.bleb.toString().includes('4') ? '是' : '否' }}
              </template>
            </el-table-column>
            <el-table-column label="注射" align="center" prop="injection">
              <template #default="scope">
                {{ scope.row.injection ? '是' : '否' }}
              </template>
            </el-table-column>
            <el-table-column label="注射方式" align="center" prop="injectionWay" width="90">
              <template #default="scope">
                <dict-tag :type="DICT_TYPE.TB_SCREEN_INJECTION_WAY"
                          :value="scope.row.injectionWay"/>
              </template>
            </el-table-column>
            <el-table-column label="结果" align="center" prop="outcome">
              <template #default="scope">
                {{ scope.row.outcome == 1 ? '阳性' : '阴性' }}
              </template>
            </el-table-column>
            <el-table-column label="注射单位" align="center" prop="injectionAgency" width="180"/>
            <el-table-column
              label="筛查时间" align="center" prop="screenTime"
              :formatter="dateFormatter2" width="110"/>
            <el-table-column label="操作" align="center" fixed="right" width="120">
              <template #default="scope">
                <el-dropdown
                  @command="(command) => handleCommand(command, scope.row)"
                >
                  <el-button type="primary" link><Icon icon="ep:d-arrow-right" /> 查看图片</el-button>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item
                        command="checkPPD"
                      >
                        查看PPD实拍图
                      </el-dropdown-item>
                      <el-dropdown-item
                        command="checkInduration"
                      >
                        查看硬结编辑图
                      </el-dropdown-item>
                      <el-dropdown-item
                        command="checkFlush"
                      >
                        查看红晕编辑图
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="DR">
          <el-table :data="DRList" :stripe="true" max-height="400px">
            <el-table-column label="筛查次序" prop="screenOrder" align="center" width="100"/>
            <el-table-column label="操作" align="center" fixed="right" width="200">
              <template #default="scope">
                <el-button
                  link type="primary"
                  @click="openImage(1, formData.id,scope.row.screenOrder,formData.screenId,formData.year,formData.screenType)">
                  查看DR
                </el-button>
              </template>
            </el-table-column>
            <el-table-column label="胸片编号" align="center" prop="chestRadiographCode"/>
            <el-table-column label="结果" align="center" prop="outcome">
              <template #default="scope">
                {{ scope.row.outcome ? '疑似结核' : '无异常' }}
              </template>
            </el-table-column>
            <el-table-column
              label="胸片采集时间" align="center" prop="screenTime"
              :formatter="dateFormatter2"/>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="CT">
          <el-table :data="CTList" :stripe="true" max-height="400px">
            <el-table-column label="筛查次序" prop="screenOrder" align="center" width="100"/>
            <el-table-column label="操作" align="center" fixed="right" width="200">
              <template #default="scope">
                <el-button
                  link type="primary"
                  @click="openImage(2,formData.id,scope.row.screenOrder,formData.screenId,formData.year,formData.screenType)">
                  查看CT
                </el-button>
              </template>
            </el-table-column>
            <el-table-column label="胸片编号" align="center" prop="chestRadiographCode"/>
            <el-table-column label="结果" align="center" prop="outcome">
              <template #default="scope">
                {{ scope.row.outcome ? '疑似结核' : '无异常' }}
              </template>
            </el-table-column>
            <el-table-column
              label="胸片采集时间" align="center" prop="screenTime"
              :formatter="dateFormatter2"/>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="知情同意书">
          <div style="padding: 10px; line-height: 1.6; font-size: 16px;">
            <div style="text-align: center;">
              <p style="font-size: 22px;"><strong>结核病筛查PPD皮肤试验知情告知书</strong></p>
            </div>
            <p>【疾病简介】结核病是由结核杆菌感染所致的慢性传染病，主要通过空气传播。感染后结核杆菌可经血液传播至全身各组织器官。</p>
            <p>【皮试禁忌】患急性疾病（如麻疹、湿疹、百日咳、流行性感冒、肺炎）、急性眼结膜炎、急性中耳炎、广泛皮肤病者及过敏体质者（对奶粉过敏）、一个月内接种过疫苗者暂不使用。</p>
            <p>【注意事项】</p>
            <ol>
              <li>PPD注射后请原地休息，观察30分钟，如无不适方可离开。如有不适应立即告知医生或护士。</li>
              <li>保持PPD注射部位清洁干燥，禁揉搓、抓挠、涂擦药物，腕部禁止佩戴手表及饰品。</li>
              <li>受试者于PPD注射后72小时查验反应结果。</li>
            </ol>
            <p><text><strong>【受试者姓名】</strong></text>______{{formData.name}}_____<text><strong>身份证号码</strong></text>_______{{formData.idNum}}_______</p>
            <p><text><strong>【学校班级】</strong></text>___________{{formData.schoolOrTemple}}___{{formData.classroom}}_________________________________</p>
            <p>请仔细阅读并理解以上内容，受试者健康状况良好，无皮试禁忌症，愿意接受PPD皮肤试验。<text><strong>如拒绝接受PPD皮肤试验，请说明原因</strong></text>_______________________________________</p>
            <div style="text-align: right;">
              <p><text><strong>家长确认签名：</strong></text>____________________</p>
              <p>年   月   日</p>
            </div>
          </div>

        </el-tab-pane>
<!--        <el-tab-pane label="痰检">
          <el-table :data="sputumList" :stripe="true" max-height="400px">
            <el-table-column label="筛查次序" prop="screenOrder" align="center" width="100"/>
            <el-table-column label="操作" align="center" fixed="right" width="200">
              <template #default="scope">
                <el-button
                  link type="primary"
                  @click="openImage(5,formData.id,scope.row.screenOrder,formData.screenId,formData.year,formData.screenType)">
                  即时痰照片
                </el-button>
                <el-button
                  link type="primary"
                  @click="openImage(6,formData.id,scope.row.screenOrder,formData.screenId,formData.year,formData.screenType)">
                  晨痰照片
                </el-button>
                <el-button
                  link type="primary"
                  @click="openImage(7,formData.id,scope.row.screenOrder,formData.screenId,formData.year,formData.screenType)">
                  夜痰照片
                </el-button>
              </template>
            </el-table-column>
            <el-table-column label="是否痰检" align="center" prop="sputumExamination" width="100">
              <template #default="scope">
                {{ scope.row.sputumExamination ? '是' : '否' }}
              </template>
            </el-table-column>
            <el-table-column label="痰标本类型" align="center" prop="type">
              <template #default="scope">
                {{ sputumExaminationType(scope.row.sputumExamination) }}
              </template>
            </el-table-column>
            <el-table-column label="即时痰标本号" align="center" prop="forthwithSputumCode"/>
            <el-table-column label="晨痰标本号" align="center" prop="morningSputumCode"/>
            <el-table-column label="夜痰标本号" align="center" prop="eveningSputumCode"/>
            <el-table-column label="结果" align="center" prop="outcome">
              <template #default="scope">
                {{ scope.row.sputumExamination ? '阳性' : '阴性' }}
              </template>
            </el-table-column>
            <el-table-column
              label="筛查时间" align="center" prop="screenTime"
              :formatter="dateFormatter"/>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="心电图">
          <el-table :data="electList" :stripe="true" max-height="400px">
            <el-table-column label="筛查次序" prop="screenOrder" align="center" width="100"/>
            <el-table-column label="操作" align="center" fixed="right" width="200">
              <template #default="scope">
                <el-button
                  link type="primary"
                  @click="openImage(4,formData.id,scope.row.screenOrder,formData.screenId,formData.year,formData.screenType)">
                  查看心电图
                </el-button>
              </template>
            </el-table-column>
            <el-table-column
              label="筛查时间" align="center" prop="screenTime"
              :formatter="dateFormatter"/>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="诊断">
          <el-table :data="diagnoList" :stripe="true" max-height="400px">
            <el-table-column label="筛查次序" prop="screenOrder" align="center" width="100"/>
            <el-table-column label="符合潜伏治疗条件者是否进行预防性治疗" align="center" prop="preventiveTreatment">
              <template #default="scope">
                <dict-tag :type="DICT_TYPE.TB_SCREEN_PREVENTIVE_TREATMENT"
                          :value="scope.row.preventiveTreatment"/>
              </template>
            </el-table-column>
            <el-table-column label="是否网报" align="center" prop="report">
              <template #default="scope">
                <dict-tag :type="DICT_TYPE.TB_SCREEN_DIAGNOSIS_REPORT" :value="scope.row.report"/>
              </template>
            </el-table-column>
            <el-table-column label="治疗方案" align="center" prop="treatmentProgram">
              <template #default="scope">
                <dict-tag :type="DICT_TYPE.TB_SCREEN_DIAGNOSIS_TREATMENT_PROGRAM"
                          :value="scope.row.treatmentProgram"/>
              </template>
            </el-table-column>

            <el-table-column label="结果" align="center" prop="outcome">
              <template #default="scope">
                <dict-tag :type="DICT_TYPE.TB_SCREEN_DIAGNOSIS_RESULT" :value="scope.row.outcome"/>
              </template>
            </el-table-column>
            <el-table-column
              label="筛查时间" align="center" prop="screenTime"
              :formatter="dateFormatter"/>
          </el-table>
        </el-tab-pane>-->

      </el-tabs>
    </ContentWrap>
  </Dialog>

  <!--   图片弹窗-->
  <ImageForm ref="imageRef"/>

</template>
<script setup lang="ts">
import {getIntDictOptions, DICT_TYPE} from '@/utils/dict'
import {ScreenPersonApi, ScreenPersonVO} from '@/api/tb/screenpersonrealsituation'
import {onMounted, ref, reactive} from 'vue'
import DictTag from "@/components/DictTag/src/DictTag.vue"
import ContentWrap from "@/components/ContentWrap/src/ContentWrap.vue"
import type {TabsPaneContext} from 'element-plus'
import {dateFormatter2} from '@/utils/formatTime'
import ImageForm from './ImageForm.vue'


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
  screenType: undefined,
  classroom: undefined,
  schoolOrTemple: undefined,
})

const formRef = ref() // 表单 Ref

const checkList = ref([]) //采集组数据
const PPDList = ref([]) // PPD组数据
const DRList = ref([]) // DR组数据
const CTList = ref([]) // CT组数据
const electList = ref([]) // 心电图组数据
const diagnoList = ref([]) // 诊断组数据
const patientInfoList = ref()
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
  hivorAIDS: undefined,
  ppdOutcome: undefined,
})

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
      checkList.value = data.checkList //采集组数据
      PPDList.value = data.ppdlist // PPD组数据
      DRList.value = data.drlist // DR组数据
      CTList.value = data.ctlist // CT组数据
/*      sputumList.value = data.sputumList // 痰检组数据
      electList.value = data.electList // 心电图组数据
      diagnoList.value = data.diagnoList // 诊断组数据*/
      tbHealthScreening.value = data.tbHealthScreening //体检单数据
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

// 切换 Tab标签页
const handleClick = (tab: TabsPaneContext, event: Event) => {
  activeName.value = tab.props.name
}

// 痰检结果
const sputumExaminationType = (value) => {
  switch (value) {
    case 1:
      return '无痰';
    case 2:
      return '即时痰';
    case 3:
      return '发放晨痰';
    case 4:
      return '夜间痰盒';
    default:
      return '未知';
  }
}

// 诊断结果
const diagnoOutcome = (value) => {
  switch (value) {
    case 1:
      return '利福平耐药';
    case 2:
      return '病原学阳性';
    case 3:
      return '病原学阴性';
    case 4:
      return '无病原学结果';
    default:
      return '未知';
  }
}

/** 查看 对话框*/
const imageRef = ref()
const openImage = (type: number, personId: number, screenOrder: number, screenId: string, year: number, screenType:number) => {
  imageRef.value.open(type, personId, screenOrder, screenId, year, screenType)
}

/** 操作分发 */
const handleCommand = (command: string, row: any) => {
  switch (command) {
    case 'checkPPD':
      openImage(16, formData.value.id, row.screenOrder, formData.value.screenId, formData.value.year, formData.value.screenType)
      break
    case 'checkInduration':
      openImage(17, formData.value.id, row.screenOrder, formData.value.screenId, formData.value.year, formData.value.screenType)
      break
    case 'checkFlush':
      openImage(18, formData.value.id, row.screenOrder, formData.value.screenId, formData.value.year, formData.value.screenType)
      break
    default:
      break
  }
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
