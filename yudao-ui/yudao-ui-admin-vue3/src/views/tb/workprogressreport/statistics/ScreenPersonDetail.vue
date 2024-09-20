<template>
  <Dialog v-model="dialogVisible" style="min-width: 1200px; " :show-overflow-tooltip="true">
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
        <el-tab-pane label="体检表" name="first">
          <div class="content">
            <div class="main">
              <div class="title">体检表</div>
              <div class="info">
                <div>筛查编号：{{ tbHealthScreening.screeningNumber }}</div>
                <div>身份证号：{{ tbHealthScreening.idNumber }}</div>
                <div>姓名：{{ tbHealthScreening.name }}</div>
                <div>年龄:：{{ tbHealthScreening.age }} 岁</div>
                <div>体检日期：{{ tbHealthScreening.examinationDate }}</div>
              </div>
              <table>
                <tr>
                  <th colspan="8">人群分类(可多选)</th>
                </tr>
                <tr>
                  <td colspan="2">活动性肺结核密切接触者</td>
                  <td class="trs">{{ tbHealthScreening.closeContactWithActivePulmonaryTB ? '☑' : '□' }}</td>
                  <td rowspan="2">老年人</td>
                  <td class="trs" rowspan="2">{{ tbHealthScreening.elderly ? '☑' : '□' }}</td>
                  <td colspan="2">HIV/AIDS</td>
                  <td class="trs">{{ tbHealthScreening.hivorAIDS ? '☑' : '□' }}</td>
                </tr>
                <tr>
                  <td rowspan="4">在校师生</td>
                  <td>0-5岁学生</td>
                  <td class="trs">{{ tbHealthScreening.student0To5Years ? '☑' : '□' }}</td>
                  <td colspan="2">既往结核病患者</td>
                  <td class="trs">{{ tbHealthScreening.pastTBPatient ? '☑' : '□' }}</td>
                </tr>
                <tr>
                  <td>6-14岁学生</td>
                  <td class="trs">{{ tbHealthScreening.student6To14Years ? '☑' : '□' }}</td>
                  <td rowspan="3">糖尿病患者</td>
                  <td class="trs" rowspan="3">{{ tbHealthScreening.diabetesPatient ? '☑' : '□' }}</td>
                  <td rowspan="3">非重点人群</td>
                  <td>0-5岁</td>
                  <td class="trs">{{ tbHealthScreening.nonKeyPopulation0To5Years ? '☑' : '□' }}</td>
                </tr>
                <tr>
                  <td>≥15岁学生</td>
                  <td class="trs">{{ tbHealthScreening.studentOver15Years ? '☑' : '□' }}</td>
                  <td>6-14岁</td>
                  <td class="trs">{{ tbHealthScreening.nonKeyPopulation6To14Years ? '☑' : '□' }}</td>
                </tr>
                <tr>
                  <td>教职工</td>
                  <td class="trs">□</td>
                  <td>≥15岁</td>
                  <td class="trs">{{ tbHealthScreening.schoolStaff ? '☑' : '□' }}</td>
                </tr>
              </table>
              <table style="margin-top: 5px">
                <tr>
                  <td class="text">
                    <b>活动性肺结核密切接触者:</b>
                    症状筛查+ppd+胸片检查,异常或强阳性进行实验室检查。<br />
                    <b>0-5岁学生:</b>症状筛查,有症状做 ppd,强阳性进一步检查;
                    <b>6-14岁学生:</b> 症状查+ppd,有症状或强 阳性进一步检查;<br/>
                    <b>≥15岁学生:</b> 症状筛
                    査+ppd+胸片检查,有症状或强阳性或异常进一步检查;<br />
                    <b>教职工:</b> 症状筛 查+胸片检查,有症状或异常进一步检查。<br />
                    <b>老年人、糖尿病患者、HIV/AIDS和既往结核病患者:</b>
                    症状筛査+胸片检查,有症状或异常进一步检查。<br />
                    <b>0-5岁非重点人群:</b>症状筛查,有症状做ppd,强阳性进一步检查;
                    <b>6-14岁非重点人群:</b> 症状筛查+ppd,有症状或强阳性进一步检查;
                    <b>≥15岁非重点人群:</b> 症状筛查+胸片检查,有症状或异常进一步检查,
                  </td>
                </tr>
              </table>
              <table style="margin-top: 5px">
                <tr>
                  <th class="text" colspan="5">您最近1个月是否有一下症状?</th>
                </tr>
                <tr>
                  <td class="text-left" colspan="2">1)咳嗽、咳痰(超过2周)</td>
                  <td>{{ tbHealthScreening.coughOrSputumForMoreThanOneWeek ? '有' : '无' }}</td>
                  <td class="text-left">2)咳血或血痰</td>
                  <td>{{ tbHealthScreening.hemoptysisOrBloodSputum ? '有' : '无' }}</td>
                </tr>
                <tr>
                  <td class="text-left" colspan="2">3)发热</td>
                  <td>{{ tbHealthScreening.fever ? '有' : '无' }}</td>
                  <td class="text-left">4)胸痛</td>
                  <td>{{ tbHealthScreening.chestPain ? '有' : '无' }}</td>
                </tr>
                <tr>
                  <td class="text-left" colspan="2">5)乏力、盗汗</td>
                  <td>{{ tbHealthScreening.nightSweats ? '有' : '无' }}</td>
                  <td class="text-left">6)食欲不振</td>
                  <td>{{ tbHealthScreening.lossOfAppetite ? '有' : '无' }}</td>
                </tr>
                <tr>
                  <td class="text-left" colspan="2">7)体重减轻(超过6斤)</td>
                  <td>{{ tbHealthScreening.weightLossOverSixPounds ? '有' : '无' }}</td>
                  <td></td>
                  <td></td>
                </tr>
                <tr>
                  <td class="trs">{{ tbHealthScreening.ppdTestDone ? '☑' : '□' }}</td>
                  <td><b>ppd</b></td>
                  <td class="text-left" colspan="2">
                    注射时间：{{ tbHealthScreening.ppdInjectionTimeStr }} <br/>
                    结果：{{tbHealthScreening.ppdOutcome == 1 ? "阳性":"阴性"}}
                  </td>
                  <td class="text-left">
                    医生签名:
                    <el-image
                      style="width: 50px; height: 50px"
                      :src="tbHealthScreening.ppdDoctorSignature"
                      :preview-src-list="[tbHealthScreening.ppdDoctorSignature]"
                    />
                  </td>
                </tr>
                <tr>
                  <td class="trs">{{ tbHealthScreening.chestXRayDone ? '☑' : '□' }}</td>
                  <td><b>胸部X线</b></td>
                  <td class="text-left" colspan="2">
                    <div style="float: left; transform: scale(1.5)">{{ tbHealthScreening.noTBRelatedAbnormalities ? '☑' : '□' }}</div>
                    <div style="float: left; margin: 0 5px">无结核相关异常</div>
                    <div style="float: left; transform: scale(1.5); margin: 0 5px">
                      {{ tbHealthScreening.suspectedTB ? '☑' : '□' }}
                    </div>
                    <div style="float: left">疑似结核</div>
                    <br />
                    <div>机器中与患者对应的编码：{{ tbHealthScreening.chestXRayCode }}</div>
                  </td>
                  <td class="text-left">
                    医生签名:
                    <el-image
                      style="width: 50px; height: 50px;"
                      :src="tbHealthScreening.chestXRayDoctorSignature"
                      :preview-src-list="[tbHealthScreening.chestXRayDoctorSignature]"
                    />
                  </td>
                </tr>
              </table>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="采集" name="checkGroup">
          <el-table :data="checkList" :stripe="true" max-height="400px">
            <el-table-column label="筛查次序" prop="screenOrder" align="center" width="100"/>
            <el-table-column label="咳嗽、咳痰不小于2周" align="center" width="110">
              <template #default="scope">
                {{ scope.row.outcome.includes('1') ? '有' : '无' }}
              </template>
            </el-table-column>
            <el-table-column label="血痰、咯血" align="center" width="110">
              <template #default="scope">
                {{ scope.row.outcome.includes('2') ? '有' : '无' }}
              </template>
            </el-table-column>
            <el-table-column label="乏力、盗汗" align="center" width="110">
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
            <el-table-column label="食欲不振" align="center" width="100">
              <template #default="scope">
                {{ scope.row.outcome.includes('6') ? '有' : '无' }}
              </template>
            </el-table-column>
            <el-table-column label="胸痛" align="center">
              <template #default="scope">
                {{ scope.row.outcome.includes('7') ? '有' : '无' }}
              </template>
            </el-table-column>
            <el-table-column label="两年内是否有与结核病患者的接触史" align="center" prop="contacted" width="160">
              <template #default="scope">
                {{ scope.row.contacted ? '是' : '否' }}
              </template>
            </el-table-column>
            <el-table-column label="筛查单位" align="center" prop="screenAgency" width="180"/>
            <el-table-column label="筛查时间" align="center" prop="screenTime" :formatter="dateFormatter2" width="130"/>
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
                {{ scope.row.outcome==1 ? '阳性' : '阴性' }}
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
            <p><text><strong>【受试者姓名】</strong></text><u>&nbsp; &nbsp; &nbsp; &nbsp;{{informedConsentForm.name}}&nbsp; &nbsp; &nbsp; &nbsp;</u><text><strong>【身份证号码】</strong></text><u>&nbsp; &nbsp; &nbsp; &nbsp;{{informedConsentForm.idNum}}&nbsp; &nbsp; &nbsp; &nbsp;</u></p>
            <p><text><strong>【学校班级】</strong></text><u>&nbsp; &nbsp; &nbsp; &nbsp;{{informedConsentForm.schoolName}}&nbsp; &nbsp; &nbsp; &nbsp;</u><u>{{informedConsentForm.classroom}}&nbsp; &nbsp; &nbsp; &nbsp;</u></p>
            <p>请仔细阅读并理解以上内容，受试者健康状况良好，无皮试禁忌症，愿意接受PPD皮肤试验。<text><strong>如拒绝接受PPD皮肤试验，请说明原因</strong></text>
              <el-row><u>{{informedConsentForm.reason}}&nbsp; &nbsp; &nbsp; &nbsp; </u></el-row>
            </p>
            <div style="text-align: right;">
              <p><text><strong>家长确认签名：</strong></text> <el-image v-if="informedConsentForm.signature" style="width: 100px; height: 100px;margin-bottom: -40px;" :src="informedConsentForm.signature" fit="contain" /></p>
              <p v-if="informedConsentForm.createTime" style="margin-top: 40px;">{{new Date(informedConsentForm.createTime).toLocaleDateString('zh-CN',{ year: 'numeric', month: 'long', day: 'numeric' })}}</p>
              <p v-else>年&nbsp; &nbsp; 月&nbsp; &nbsp; 日</p>
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
import {ScreenInformedConsentFormApi} from "@/api/tb/screeninformedconsentform";


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
// 知情同意书
const informedConsentForm=ref({})
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
      const infomedForm=await ScreenInformedConsentFormApi.getLastInformedConsentForm(id)
      checkList.value = data.checkList //采集组数据
      for (let i = 0; i < checkList.value.length; i++) {
        if (!checkList.value[i].outcome){
          checkList.value[i].outcome=''
        }
      }
      PPDList.value = data.ppdlist // PPD组数据
      DRList.value = data.drlist // DR组数据
      CTList.value = data.ctlist // CT组数据
/*      sputumList.value = data.sputumList // 痰检组数据
      electList.value = data.electList // 心电图组数据
      diagnoList.value = data.diagnoList // 诊断组数据*/
      tbHealthScreening.value = data.tbHealthScreening //体检单数据
      informedConsentForm.value=infomedForm
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
<style scoped>
table {
  table-layout: fixed; /* 设置表格宽度固定 */
  border-collapse: collapse;
  margin-top: 10px; /* 增大表格上边距 */
  width: 100%;
}

td,
th {
  word-break: break-all; /* 处理长单词换行 */
  white-space: normal; /* 允许文本换行 */
  border: 1px solid black;
  padding: 12px; /* 增大内边距 */
  text-align: center;
  width: 105px; /* 调整单元格宽度 */
  font-size: 18px; /* 增大字体 */
}

.info {
  margin-top: 15px; /* 增大信息区域上边距 */
  font-size: 20px; /* 增大字体 */
}

.content {
  width: 1050px; /* 设置内容区域宽度 */
  padding: 15px; /* 增大内边距 */
}

.title {
  text-align: center;
  font-size: 28px; /* 增大标题字体 */
  font-weight: 700; /* 增大标题字体粗细 */
}

.text {
  text-align: left;
  width: calc(1050px - 36px); /* 计算宽度以适应内边距和边框 */
  font-size: 20px; /* 增大字体 */
}

.trs {
  transform: scale(1.5);
  width: 45px; /* 调整宽度 */
}

.text-left {
  text-align: left;
}
</style>
