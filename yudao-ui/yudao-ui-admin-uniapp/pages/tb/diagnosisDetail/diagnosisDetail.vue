<template>
	<view class="show_span">
		<view style="margin: 10px">
			<yile-breadcrumb
				:nav="nav"
				color="rgba(153, 153, 153, 1)"
				actColor="rgba(36, 93, 209, 1)"
			></yile-breadcrumb>
		</view>
		<view style="background-color: #fff; margin: 0 10px">
			<u-row style="font-size: 22px; font-weight: 800; margin: 5px 5px 0 5px">诊断结果</u-row>
			<u-row justify="space-between" gutter="10">
				<u-radio-group v-model="outcome" placement="row" style="margin: 10px" @change="groupChange">
					<template v-for="(item, index) in radiolist_outcome" :key="index">
						<u-col span="3">
							<u-radio
								style="transform: scale(1.3); margin: 20rpx; margin-left: 15%"
								:label="item.name"
								:name="item.name"
							></u-radio>
						</u-col>
					</template>
				</u-radio-group>
			</u-row>
		</view>
		<view style="background-color: #fff; margin: 0 10px">
			<u-row style="font-size: 22px; font-weight: 800; margin: 5px 5px 0 5px">治疗方案</u-row>
			<u-row justify="space-between" gutter="10">
				<u-radio-group v-model="treatmentProgram" placement="row" style="margin: 10px" @change="groupChange">
					<template v-for="(item, index) in radiolist_treatmentProgram" :key="index">
						<u-col span="3">
							<u-radio
								style="transform: scale(1.3); margin: 20rpx; margin-left: 15%"
								:label="item.name"
								:name="item.name"
							></u-radio>
						</u-col>
					</template>
				</u-radio-group>
			</u-row>
		</view>
		<view style="background-color: #fff; margin: 0 10px">
			<u-row style="font-size: 22px; font-weight: 800; margin: 5px 5px 0 5px">是否网报</u-row>
			<u-row justify="space-between" gutter="10">
				<u-radio-group v-model="report" placement="row" style="margin: 10px" @change="groupChange_report">
					<template v-for="(item, index) in radiolist_report" :key="index">
						<u-col span="1">
							<u-radio
								style="transform: scale(1.3); margin: 20rpx; margin-left: 15%"
								:label="item.name"
								:name="item.name"
							></u-radio>
						</u-col>
					</template>
				</u-radio-group>
			</u-row>
		</view>
		<view style="background-color: #fff; margin: 0 10px">
			<u-row style="font-size: 22px; font-weight: 800; margin: 5px 5px 0 5px">是否进行预防性治疗</u-row>
			<u-row justify="space-between" gutter="10">
				<u-radio-group
					v-model="preventiveTreatment"
					placement="row"
					style="margin: 10px"
					@change="groupChange_report"
				>
					<template v-for="(item, index) in radiolist_preventiveTreatment" :key="index">
						<u-col span="1">
							<u-radio
								style="transform: scale(1.3); margin: 20rpx; margin-left: 15%"
								:label="item.name"
								:name="item.name"
							></u-radio>
						</u-col>
					</template>
				</u-radio-group>
			</u-row>
		</view>
		<u-row style="margin: 10px auto; width: 400px">
			<u-button type="primary" text="保存" @click="submit"></u-button>
		</u-row>
		<u-sticky bgColor="#fff">
			<u-tabs
				:list="tabs"
				itemStyle="padding-left: 70px; padding-right: 70px; height: 40px; font-size: 100px;"
				@click="click"
			></u-tabs>
		</u-sticky>

		<template v-show="this.currentItem === 1" class="physical_examination">
			<view class="text_name">体检表</view>
			<view class="input_content">
				<view class="info_code">筛查编号：{{ tbHealthScreening.screeningNumber }}</view>
				<view class="info_idCard">身份证号：{{ tbHealthScreening.idNumber }}</view>
				<view class="info_name">姓名：{{ tbHealthScreening.name }}</view>
				<view class="info_age">年龄：{{ tbHealthScreening.age }} 岁</view>
				<view class="info_date">体检日期：{{ tbHealthScreening.examinationDate }}</view>
			</view>
			<view class="crowd_class">
				<view class="crowd_class_title">人群分类（可多选）</view>
			</view>
			<view class="crowd_class_content">
				<view class="content_one">
					<view class="content_one_head">
						<view class="content_one_head_left">活动性肺结核密切接触者</view>
						<view class="content_one_head_right">
							{{ tbHealthScreening.closeContactWithActivePulmonaryTB ? '☑' : '□' }}
						</view>
					</view>

					<view class="content_one_content">
						<view class="content_one_content_item_1">在校师生</view>
						<view class="content_one_content_item_2">
							<view class="content_one_content_item_2_item_1">0-5岁学生</view>
							<view class="content_one_content_item_2_item_2">6-14岁学生</view>
							<view class="content_one_content_item_2_item_3">≥15岁学生</view>
							<view class="content_one_content_item_2_item_4">教职工</view>
						</view>
						<view class="content_one_content_item_3">
							<view class="content_one_content_item_3_item_1">
								{{ tbHealthScreening.student0To5Years ? '☑' : '□' }}
							</view>
							<view class="content_one_content_item_3_item_2">
								{{ tbHealthScreening.student6To14Years ? '☑' : '□' }}
							</view>
							<view class="content_one_content_item_3_item_3">
								{{ tbHealthScreening.studentOver15Years ? '☑' : '□' }}
							</view>
							<view class="content_one_content_item_3_item_4">
								{{ tbHealthScreening.schoolStaff ? '☑' : '□' }}
							</view>
						</view>
					</view>
				</view>
				<view class="content_tow">
					<view class="content_tow_item_column3_2">
						<view class="content_tow_item_column3_item_1">僧尼</view>
						<view class="content_tow_item_column3_item_1point5">
							<view class="content_tow_item_column3_item_1point5_item_col_1">0-5岁</view>
							<view class="content_tow_item_column3_item_1point5_item_col_1">6-14岁</view>
							<view
								class="content_tow_item_column3_item_1point5_item_col_1"
								style="border-bottom-style: none"
							>
								≥ 15岁
							</view>
						</view>
						<view class="content_tow_item_column3_item_1point5">
							<view class="content_tow_item_column3_item_1_item_col_1">
								{{ tbHealthScreening.monkOrNun0To5Years ? '☑' : '□' }}
							</view>
							<view class="content_tow_item_column3_item_1_item_col_1">
								{{ tbHealthScreening.monkOrNun6To14Years ? '☑' : '□' }}
							</view>
							<view class="content_tow_item_column3_item_1_item_col_1" style="border-bottom-style: none">
								{{ tbHealthScreening.monkOrNunOver15Years ? '☑' : '□' }}
							</view>
						</view>
					</view>
					<view class="content_tow_item_column3_1">
						<view class="content_tow_item_column3_item_2point5">老年人</view>
						<view class="content_tow_item_column3_item_1">{{ tbHealthScreening.elderly ? '☑' : '□' }}</view>
					</view>
					<view class="content_tow_item_column3_1" style="border-bottom-style: none">
						<view class="content_tow_item_column3_item_2point5">糖尿病患者</view>
						<view class="content_tow_item_column3_item_1">
							{{ tbHealthScreening.diabetesPatient ? '☑' : '□' }}
						</view>
					</view>
				</view>
				<view class="content_three">
					<view class="content_three_item_1">
						<view class="content_three_item_1_4">HIV/AIDS</view>
						<view class="content_three_item_1_1">{{ tbHealthScreening.HIVorAIDS ? '☑' : '□' }}</view>
					</view>
					<view class="content_three_item_1">
						<view class="content_three_item_1_4">既往结核病患者</view>
						<view class="content_three_item_1_1">{{ tbHealthScreening.pastTBPatient ? '☑' : '□' }}</view>
					</view>
					<view class="content_three_item_2">
						<view class="content_three_item_2_1" style="padding-top: 5vh; padding-left: 1vw">
							非重点人群
						</view>
						<view class="content_three_item_2_1">
							<view class="content_three_item_2_1_col_1">0-5岁</view>
							<view class="content_three_item_2_1_col_1">6-14岁</view>
							<view class="content_three_item_2_1_col_1" style="border-bottom-style: none">≥15岁</view>
						</view>
						<view class="content_three_item_2_0point5">
							<view class="content_three_item_2_1_col_1">
								{{ tbHealthScreening.nonKeyPopulation0To5Years ? '☑' : '□' }}
							</view>
							<view class="content_three_item_2_1_col_1">
								{{ tbHealthScreening.nonKeyPopulation6To14Years ? '☑' : '□' }}
							</view>
							<view class="content_three_item_2_1_col_1" style="border-bottom-style: none">
								{{ tbHealthScreening.nonKeyPopulationOver15Years ? '☑' : '□' }}
							</view>
						</view>
					</view>
				</view>
			</view>
			<view class="tips_content">
				<strong>活动性肺结核密切接触者：</strong>
				症状筛查+ppd+胸片检查，异常或强阳性进行实验室检查。
				<br />
				<strong>0-5岁学生：</strong>
				症状筛查+查验卡痕，有症状做 ppd，强阳性进一步检查；
				<strong>6-14 岁学生：</strong>
				症状筛查+ppd+查验卡痕，有症状或强 阳性进一步检查；
				<strong>≥15 岁学生：</strong>
				症状筛查+ppd+查验卡痕+胸片检查，有症状或强阳性或异常进一步检查；
				<strong>教职工：</strong>
				症状筛 查+胸片检查，有症状或异常进一步检查。
				<br />
				<strong>僧尼：</strong>
				0-5 岁、6-14 岁同学生；≥15 岁症状筛查+查验卡痕+胸片检查，有症状或强阳性或异常进一步检查。
				<br />
				<strong>老年人、糖尿病患者、HIV/AIDS 和既往结核病患者：</strong>
				症状筛查+胸片检查，有症状或异常进一步检查。
				<br />
				<strong>0-5 岁非重点人群：</strong>
				症状筛查+查验卡痕，有症状做 ppd，强阳性进一步检查；
				<strong>6-14 岁非重点人群：</strong>
				症状筛查+ppd+查验卡痕， 有症状或强阳性进一步检查；
				<strong>≥15 岁非重点人群：</strong>
				症状筛查+胸片检查，有症状或异常进一步检查。
			</view>
			<view class="symptom_input_content">
				<view class="symptom_input_content_item_1">
					<strong style="padding: 2vh; font-size: 1.5vw">您最近 1 个月内是否有以下症状？</strong>
				</view>
				<view class="symptom_input_content_item_1">
					<view class="symptom_input_content_item_1_item4_1" style="margin-left: 0.5vw">
						1）咳嗽、咳痰（超过 1 周）
					</view>
					<view class="symptom_input_content_item_1_item4_1">
						{{ tbHealthScreening.coughOrSputumForMoreThanOneWeek ? '有' : '无' }}
					</view>
					<view class="symptom_input_content_item_1_item4_1" style="margin-right: 0.5vw">5）夜间盗汗</view>
					<view class="symptom_input_content_item_1_item4_1" style="border-right-style: none">
						{{ tbHealthScreening.nightSweats ? '有' : '无' }}
					</view>
				</view>
				<view class="symptom_input_content_item_1">
					<view class="symptom_input_content_item_1_item4_1" style="margin-left: 0.5vw">2）咯血或血痰</view>
					<view class="symptom_input_content_item_1_item4_1">
						{{ tbHealthScreening.hemoptysisOrBloodSputum ? '有' : '无' }}
					</view>
					<view class="symptom_input_content_item_1_item4_1" style="margin-right: 0.5vw">6）食欲不振</view>
					<view class="symptom_input_content_item_1_item4_1" style="border-right-style: none">
						{{ tbHealthScreening.lossOfAppetite ? '有' : '无' }}
					</view>
				</view>
				<view class="symptom_input_content_item_1">
					<view class="symptom_input_content_item_1_item4_1" style="margin-left: 0.5vw">3）发热</view>
					<view class="symptom_input_content_item_1_item4_1" style="border-right-style: none">
						{{ tbHealthScreening.fever ? '有' : '无' }}
					</view>
					<view
						class="symptom_input_content_item_1_item4_1"
						style="border-left-style: solid; margin-right: 0.5vw"
					>
						7）乏力
					</view>
					<view class="symptom_input_content_item_1_item4_1" style="border-right-style: none">
						{{ tbHealthScreening.fatigue ? '有' : '无' }}
					</view>
				</view>
				<view class="symptom_input_content_item_1">
					<view class="symptom_input_content_item_1_item4_1" style="margin-left: 0.5vw">4）胸痛</view>
					<view class="symptom_input_content_item_1_item4_1" style="border-right-style: none">
						{{ tbHealthScreening.chestPain ? '有' : '无' }}
					</view>
					<view
						class="symptom_input_content_item_1_item4_1"
						style="border-left-style: solid; margin-right: 0.5vw"
					>
						8）体重减轻（超过 6 斤）
					</view>
					<view class="symptom_input_content_item_1_item4_1" style="border-right-style: none">
						{{ tbHealthScreening.weightLossOverSixPounds ? '有' : '无' }}
					</view>
				</view>
				<view class="symptom_input_content_item_1">
					<view class="symptom_input_content_item_1_item4_1">
						<view class="symptom_input_content_item_1_item4_1_1">
							{{ tbHealthScreening.doneCheckMark ? '☑' : '□' }}
						</view>
						<view class="symptom_input_content_item_1_item4_1_3" style="border-right-style: none">
							<strong>查验卡痕</strong>
						</view>
					</view>
					<view class="symptom_input_content_item_1_item4_2">
						{{ tbHealthScreening.checkMark ? '☑有' : '□有' }}
						{{ tbHealthScreening.doneCheckMark && !tbHealthScreening.checkMark ? '☑无' : '□无' }}
						□无法判断
					</view>
					<view class="symptom_input_content_item_1_item4_1" style="border-right-style: none">
						医生签字：
						<image style="width: 50px; height: 50px" :src="tbHealthScreening.collectDoctorSignature" />
					</view>
				</view>
				<view class="symptom_input_content_item_1">
					<view class="symptom_input_content_item_1_item4_1">
						<view class="symptom_input_content_item_1_item4_1_1">
							{{ tbHealthScreening.ppdTestDone ? '☑' : '□' }}
						</view>
						<view class="symptom_input_content_item_1_item4_1_3" style="border-right-style: none">
							<strong>ppd</strong>
						</view>
					</view>
					<view class="symptom_input_content_item_1_item4_2">
						注射时间： {{ tbHealthScreening.ppdInjectionTimeStr }}（24 小时制）
					</view>
					<view class="symptom_input_content_item_1_item4_1" style="border-right-style: none">
						医生签字：
						<image style="width: 50px; height: 50px" :src="tbHealthScreening.ppdDoctorSignature" />
					</view>
				</view>
				<view class="symptom_input_content_item_2">
					<view class="symptom_input_content_item_1_item4_1">
						<view class="symptom_input_content_item_1_item4_1_1">
							{{ tbHealthScreening.chestXRayDone ? '☑' : '□' }}
						</view>
						<view class="symptom_input_content_item_1_item4_1_3" style="border-right-style: none">
							<strong>胸部 X 线</strong>
						</view>
					</view>
					<view class="symptom_input_content_item_1_item4_2">
						{{ tbHealthScreening.noTBRelatedAbnormalities ? '☑无结核相关异常' : '□无结核相关异常' }}
						{{ tbHealthScreening.suspectedTB ? '☑疑似结核' : '□疑似结核' }}
						<br />
						<view style="padding-top: 2vh">
							机器中与患者对应的编码：{{ tbHealthScreening.chestXRayCode }}
						</view>
					</view>
					<view class="symptom_input_content_item_1_item4_1" style="border-right-style: none">
						医生签字：
						<image style="width: 50px; height: 50px" :src="tbHealthScreening.chestXRayDoctorSignature" />
					</view>
				</view>
				<view class="symptom_input_content_item_1" style="border-top-style: solid; border-bottom-style: none">
					<view class="symptom_input_content_item_1_item4_1">
						<view class="symptom_input_content_item_1_item4_1_1">
							{{ tbHealthScreening.sputumSpecimenDone ? '☑' : '□' }}
						</view>
						<view class="symptom_input_content_item_1_item4_1_3" style="border-right-style: none">
							<strong>痰标本</strong>
						</view>
					</view>
					<view class="symptom_input_content_item_1_item4_2">
						{{ tbHealthScreening.immediateSputum ? '☑即时痰' : '□即时痰' }}
						{{ tbHealthScreening.morningSputum ? '☑发放晨痰' : '□发放晨痰' }}
						{{ tbHealthScreening.nighttimeSputum ? '☑夜间痰盒' : '□夜间痰盒' }}
						{{ tbHealthScreening.noSputum ? '☑无痰' : '□无痰' }}
					</view>
					<view class="symptom_input_content_item_1_item4_1" style="border-right-style: none">
						医生签字：
						<image style="width: 50px; height: 50px" :src="tbHealthScreening.sputumDoctorSignature" />
					</view>
				</view>
			</view>
			<!-- <view class="check_signature">质检人员签字:</view> -->
		</template>

		<view v-show="this.currentItem === 2">
			<view class="image-container">
				<u-image
					v-for="(item, index) in imageUrls"
					:key="index"
					:src="item"
					width="200px"
					height="200px"
					style="margin: 10px"
				></u-image>
			</view>
		</view>
		<view v-show="this.currentItem === 3">
			<view class="image-container">
				<u-image
					v-for="(item, index) in imageUrls"
					:key="index"
					:src="item"
					width="200px"
					height="200px"
					style="margin: 10px"
				></u-image>
			</view>
		</view>
		<view v-show="this.currentItem === 4">
			<u-row style="font-size: 22px; font-weight: 800; margin: 5px">痰菌检查</u-row>
			<u-row justify="space-between" gutter="10">
				<view style="margin-left: 18px; font-size: 20px; margin-right: 25px">涂片结果</view>
				<u-radio-group v-model="formData.smearResult" placement="row" style="margin: 10px">
					<template v-for="(item, index) in laboratory_smearResult" :key="index">
						<u-col span="2">
							<u-radio
								style="transform: scale(1.2)"
								:label="item.name"
								:name="item.name"
								:disabled="true"
							></u-radio>
						</u-col>
					</template>
				</u-radio-group>
			</u-row>
			<u-row justify="space-between" gutter="10">
				<view style="margin-left: 18px; font-size: 20px; margin-right: 25px">培养结果</view>
				<u-radio-group v-model="formData.cultureResult" placement="row" style="margin: 10px">
					<template v-for="(item, index) in laboratory_cultureResult" :key="index">
						<u-col span="2">
							<u-radio
								style="transform: scale(1.2)"
								:label="item.name"
								:name="item.name"
								:disabled="true"
							></u-radio>
						</u-col>
					</template>
				</u-radio-group>
			</u-row>
			<u-row justify="space-between" gutter="10">
				<view style="margin-left: 18px; font-size: 20px; margin-right: 25px">分子生物学</view>
				<u-radio-group v-model="formData.molecularBiology" placement="row" style="margin: 10px">
					<template v-for="(item, index) in laboratory_molecularBiology" :key="index">
						<u-col span="3">
							<u-radio
								style="transform: scale(1.2)"
								:label="item.name"
								:name="item.name"
								:disabled="true"
							></u-radio>
						</u-col>
					</template>
				</u-radio-group>
			</u-row>
			<u-row style="font-size: 22px; font-weight: 800; margin: 5px">组织标本</u-row>
			<u-row justify="space-between" gutter="10">
				<view style="margin-left: 18px; font-size: 20px; margin-right: 25px">检测结果</view>
				<u-radio-group v-model="formData.tissueSpecimenResult" placement="row" style="margin: 10px">
					<template v-for="(item, index) in laboratory_tissueSpecimenResult" :key="index">
						<u-col span="3">
							<u-radio
								style="transform: scale(1.2)"
								:label="item.name"
								:name="item.name"
								:disabled="true"
							></u-radio>
						</u-col>
					</template>
				</u-radio-group>
			</u-row>
			<u-row style="font-size: 22px; font-weight: 800; margin: 5px">菌种鉴定</u-row>
			<u-row justify="space-between" gutter="10">
				<view style="margin-left: 18px; font-size: 20px; margin-right: 25px">检测结果</view>
				<u-radio-group v-model="formData.strainIdentificationResult" placement="row" style="margin: 10px">
					<template v-for="(item, index) in laboratory_strainIdentificationResult" :key="index">
						<u-col span="3">
							<u-radio
								style="transform: scale(1.2)"
								:label="item.name"
								:name="item.name"
								:disabled="true"
							></u-radio>
						</u-col>
					</template>
				</u-radio-group>
			</u-row>
			<u-row style="font-size: 22px; font-weight: 800; margin: 5px">结核分支杆菌药敏检查</u-row>
			<u-row justify="space-between" gutter="10">
				<view style="margin-left: 18px; font-size: 20px; margin-right: 25px">药敏检测方法</view>
				<u-radio-group v-model="formData.tbDrugSensitivityMethod" placement="row" style="margin: 10px">
					<template v-for="(item, index) in laboratory_tbDrugSensitivityMethod" :key="index">
						<u-col span="3">
							<u-radio
								style="transform: scale(1.2)"
								:label="item.name"
								:name="item.name"
								:disabled="true"
							></u-radio>
						</u-col>
					</template>
				</u-radio-group>
			</u-row>
			<u-row justify="space-between" gutter="10">
				<view style="margin-left: 18px; font-size: 20px; margin-right: 25px">耐药综合判定</view>
				<u-radio-group v-model="formData.drugResistanceResult" placement="row" style="margin: 10px">
					<template v-for="(item, index) in laboratory_drugResistanceResult" :key="index">
						<u-col span="3">
							<u-radio
								style="transform: scale(1.2)"
								:label="item.name"
								:name="item.name"
								:disabled="true"
							></u-radio>
						</u-col>
					</template>
				</u-radio-group>
			</u-row>
			<u-row style="font-size: 22px; font-weight: 800; margin: 5px">HIV抗体检测</u-row>
			<u-row justify="space-between" gutter="10">
				<view style="margin-left: 18px; font-size: 20px; margin-right: 25px">抗体检测结果</view>
				<u-radio-group v-model="formData.hivResult" placement="row" style="margin: 10px">
					<template v-for="(item, index) in laboratory_hivResult" :key="index">
						<u-col span="3">
							<u-radio
								style="transform: scale(1.2)"
								:label="item.name"
								:name="item.name"
								:disabled="true"
							></u-radio>
						</u-col>
					</template>
				</u-radio-group>
			</u-row>
		</view>
		<view v-show="this.currentItem === 5" style="height: 500px">
			<view class="image-container">
				<u-image
					v-for="(item, index) in imageUrls"
					:key="index"
					:src="item"
					width="200px"
					height="200px"
					style="margin: 10px"
				></u-image>
			</view>
		</view>
	</view>
</template>

<script>
import * as diagnosisApi from '@/api/screen/diagnosis/index';
import * as laboratosyApi from '@/api/screen/laboratory/index';

export default {
	data() {
		return {
			nav: [
				{
					value: '常规筛查'
				},
				{
					value: '诊断组',
				},
				{
					value: '诊断结果',
					isActive: true
				}
			],
			outcome: '',
			radiolist_outcome: [
				{
					name: '病原学阳性',
					disabled: false
				},
				{
					name: '病原学阴性',
					disabled: false
				},
				{
					name: '无病原学结果',
					disabled: false
				},
				{
					name: '耐药',
					disabled: false
				},
				{
					name: '陈旧性肺结核',
					disabled: false
				},
				{
					name: '暂时排除结核病',
					disabled: false
				},
				{
					name: '潜伏感染者',
					disabled: false
				},
				{
					name: '肺结核',
					disabled: false
				},
				{
					name: '肺外结核',
					disabled: false
				}
			],
			treatmentProgram: '',
			radiolist_treatmentProgram: [
				{
					name: '14天治疗管理',
					disabled: false
				},
				{
					name: '住院治疗',
					disabled: false
				},
				{
					name: '隔离治疗',
					disabled: false
				},
				{
					name: '服药管理（复诊）',
					disabled: false
				},
				{
					name: '服药管理（服药）',
					disabled: false
				},
				{
					name: '停止治疗',
					disabled: false
				},
				{
					name: '服药管理（随访）',
					disabled: false
				}
			],
			report: '',
			radiolist_report: [
				{
					name: '是',
					disabled: false
				},
				{
					name: '否',
					disabled: false
				}
			],
			preventiveTreatment: '',
			radiolist_preventiveTreatment: [
				{
					name: '是',
					disabled: false
				},
				{
					name: '否',
					disabled: false
				}
			],
			tabs: [
				{
					name: '体检单'
				},
				{
					name: 'DR'
				},
				{
					name: 'CT'
				},
				{
					name: '实验室'
				},
				{
					name: '心电图'
				}
			],
			formData: {
				smearResult: null,
				cultureResult: null,
				molecularBiology: null,
				tissueSpecimenResult: null,
				hivResult: null,
				strainIdentificationResult: null,
				tbDrugSensitivityMethod: null,
				drugResistanceResult: null
			},
			laboratory_smearResult: [
				{
					name: '阳性',
					disabled: false
				},
				{
					name: '阴性',
					disabled: false
				},
				{
					name: '未查',
					disabled: false
				}
			],
			laboratory_cultureResult: [
				{
					name: '阳性',
					disabled: false
				},
				{
					name: '阴性',
					disabled: false
				},
				{
					name: '污染',
					disabled: false
				},
				{
					name: '未查',
					disabled: false
				}
			],
			laboratory_molecularBiology: [
				{
					name: '结核分枝杆菌核酸阳性',
					disabled: false
				},
				{
					name: '未检出结核分枝杆菌',
					disabled: false
				},
				{
					name: '不确定',
					disabled: false
				},
				{
					name: '未查',
					disabled: false
				}
			],
			laboratory_tissueSpecimenResult: [
				{
					name: '组织学阳性',
					disabled: false
				},
				{
					name: '仅病理学阳性',
					disabled: false
				},
				{
					name: '阴性',
					disabled: false
				},
				{
					name: '未查',
					disabled: false
				}
			],
			laboratory_strainIdentificationResult: [
				{
					name: '结核分枝杆菌复合群',
					disabled: false
				},
				{
					name: '非结核分枝杆菌',
					disabled: false
				},
				{
					name: '未查',
					disabled: false
				}
			],
			laboratory_tbDrugSensitivityMethod: [
				{
					name: '分子生物学',
					disabled: false
				},
				{
					name: '传统药敏试验',
					disabled: false
				}
			],
			laboratory_drugResistanceResult: [
				{
					name: '单耐利福平',
					disabled: false
				},
				{
					name: '耐多药',
					disabled: false
				},
				{
					name: '广泛耐药',
					disabled: false
				},
				{
					name: '单耐异烟肼',
					disabled: false
				},
				{
					name: '利福平与异烟肼均敏感',
					disabled: false
				}
			],
			laboratory_hivResult: [
				{
					name: '已知阳性',
					disabled: false
				},
				{
					name: '新检测初筛阳性',
					disabled: false
				},
				{
					name: '新检测确认阳性',
					disabled: false
				},
				{
					name: '阴性',
					disabled: false
				},
				{
					name: '拒查',
					disabled: false
				},
				{
					name: '未提供',
					disabled: false
				}
			],
			currentItem: 1,
			// 患者信息对象
			patientInfo: {},
			tbHealthScreening: {},
			formData: {
				smearResult: null,
				cultureResult: null,
				molecularBiology: null,
				tissueSpecimenResult: null,
				hivResult: null,
				strainIdentificationResult: null,
				tbDrugSensitivityMethod: null,
				drugResistanceResult: null
			},
			imageUrls: []
		};
	},
	onLoad(option) {
		this.getNavItems(uni.$screenType);
		// 上一个页面传递的参数对象
		const item = JSON.parse(decodeURIComponent(option.item));
		this.patientInfo = item;

		// 获取缺省值
		diagnosisApi
			.getLastTime({
				personId: this.patientInfo.personId
			})
			.then((res) => {
				if (res.data) {
					this.outcome = diagnosisApi.outcomeMappings[res.data.outcome];
					this.treatmentProgram = diagnosisApi.treatmentProgramMappings[res.data.treatmentProgram];
					this.report = diagnosisApi.reportMappings[res.data.report];
					this.preventiveTreatment = diagnosisApi.preventiveTreatmentMappings[res.data.preventiveTreatment];
				}
			});

		// 缺省选中体检单
		this.getCheckSelectList(this.currentItem);
	},
	methods: {
		getNavItems(screenType) {
			switch (screenType) {
				case 1:
					this.nav = [{ value: '常规筛查' },{ value: '诊断组' }, { value: '诊断结果', isActive: true }];
					break;
				case 2:
					this.nav = [{ value: '新生入学筛查' },{ value: '诊断组' },{ value: '诊断结果', isActive: true }];
					break;
				case 3:
					this.nav = [{ value: '应急筛查' },{ value: '诊断组' },{ value: '诊断结果', isActive: true }];
					break;
				default:
					this.nav;
					break;
			}

			return this.nav;
		},
		// 保存
		submit() {
			// 提交前进行表单校验
			// 全部为空时，结果为ture
			let flag = !this.preventiveTreatment &&
				!this.report && !this.treatmentProgram
				&& !this.outcome
			if(flag){
				uni.$u.toast('诊断数据不能全部为空,请检查表单后提交!');
				return;
			}


			const reversedSmearResults = diagnosisApi.reverseObject(diagnosisApi.outcomeMappings, (value) => value);
			const reversedTreatmentProgramMappings = diagnosisApi.reverseObject(diagnosisApi.treatmentProgramMappings, (value) => value);
			const reversedPreventiveTreatmentMappings = diagnosisApi.reverseObject(diagnosisApi.preventiveTreatmentMappings, (value) => value);
			const reversedReportMappings = diagnosisApi.reverseObject(diagnosisApi.reportMappings, (value) => value);

			diagnosisApi
				.createScreenDiagnosis({
					personId: this.patientInfo.personId,
					outcome: reversedSmearResults[this.outcome],
					treatmentProgram: reversedTreatmentProgramMappings[this.treatmentProgram],
					preventiveTreatment: reversedPreventiveTreatmentMappings[this.preventiveTreatment],
					report: reversedReportMappings[this.report],
					screenId: this.patientInfo.screenId,
					screenOrder: this.patientInfo.screenOrder,
					screenPoint: this.patientInfo.screenPoint,
					screenTime: this.patientInfo.screenTime,

					// 筛查类型
					screenType: uni.$screenType
				})
				.then((res) => {
					if (res.data) {
						uni.$u.toast('提交成功！');
					}
				});
		},
		// tab标签切换
		click(item) {
			this.currentItem = item.index + 1;
			this.getCheckSelectList(this.currentItem);
		},
		getCheckSelectList(index) {
			diagnosisApi
				.getCheckSelectList({
					type: index,
					personId: this.patientInfo.personId
				})
				.then((res) => {
					if (res.data[0]) {
						if (res.data[0].tbHealthScreening) {
							this.tbHealthScreening = res.data[0].tbHealthScreening;
						}
						if (res.data[0].experimentGroup) {
							this.formData = res.data[0].experimentGroup;

							// 适配uniapp组件回显
							this.formData.hivResult = laboratosyApi.hivTestResults[this.formData.hivResult];
							this.formData.smearResult = laboratosyApi.smearResults[this.formData.smearResult];
							this.formData.cultureResult = laboratosyApi.cultureResults[this.formData.cultureResult];
							this.formData.molecularBiology = laboratosyApi.molecularBiologyResults[this.formData.molecularBiology];
							this.formData.tissueSpecimenResult = laboratosyApi.tissueTestResults[this.formData.tissueSpecimenResult];
							this.formData.strainIdentificationResult = laboratosyApi.speciesIdentificationResults[this.formData.strainIdentificationResult];
							this.formData.tbDrugSensitivityMethod = laboratosyApi.tbDrugSensitivityTestMethods[this.formData.tbDrugSensitivityMethod];
							this.formData.drugResistanceResult = laboratosyApi.drugResistanceMap[this.formData.drugResistanceResult];
						}
						if (res.data[0].imageUrls) {
							this.imageUrls = res.data[0].imageUrls;
						}
					}else{
						this.imageUrls = [];
					}
				});
		}
	}
};
</script>

<style lang="scss" scoped>
/* 设置图片容器的样式 */
.image-container {
	display: flex; /* 使用Flexbox布局 */
	flex-wrap: wrap; /* 允许换行 */
	justify-content: space-around; /* 图片之间平均分布 */
}

.show_span {
	width: 100%;
}
.physical_examination {
	display: flex;
	flex-direction: column;
	font-size: 1.5vw;
}

.text_name {
	/* border: 1px solid red; */
	align-items: center;
	justify-content: center;
	text-align: center;
	font-size: 2vw;
}

.input_content {
	position: relative;
	padding: 0 5vh;
}

.info_idCard {
	position: absolute;
	margin: -3vh 0 0 25vw;
}

.info_name {
}

.info_age {
	margin: -3.5vh 0 0 10vw;
}

.info_date {
	margin: -3.5vh 0 0 20vw;
}

.check_signature {
	margin-top: 5vh;
	margin-bottom: 2vh;
	margin-left: 80%;
}

.symptom_input_content {
	border: 2px solid;
	margin: 1vh 5vh 0 5vh;
	height: 100vh;

	display: flex;
	flex-direction: column;

	.symptom_input_content_item_1 {
		flex: 1;
		border-bottom-style: solid;

		display: flex;

		.symptom_input_content_item_1_item4_1 {
			flex: 1;
			// border: 1px solid;
			border-right-style: solid;
			padding-left: 1vw;

			display: flex;

			.symptom_input_content_item_1_item4_1_1 {
				flex: 1;
				border-right-style: solid;
			}
			.symptom_input_content_item_1_item4_1_3 {
				flex: 3;
				border-right-style: solid;
				padding-left: 1vw;
			}
		}
		.symptom_input_content_item_1_item4_2 {
			flex: 2;
			// border: 1px solid;
			border-right-style: solid;
			padding-left: 1vw;
		}
	}
	.symptom_input_content_item_2 {
		flex: 2;
		// border: 1px solid;

		display: flex;

		.symptom_input_content_item_1_item4_1 {
			flex: 1;
			// border: 1px solid;
			border-right-style: solid;
			padding-left: 1vw;

			display: flex;

			.symptom_input_content_item_1_item4_1_1 {
				flex: 1;
				border-right-style: solid;
			}
			.symptom_input_content_item_1_item4_1_3 {
				flex: 3;
				border-right-style: solid;
				padding-left: 1vw;
			}
		}

		.symptom_input_content_item_1_item4_1 {
			flex: 1;
			// border: 1px solid;
			border-right-style: solid;
			padding-left: 1vw;
		}
		.symptom_input_content_item_1_item4_2 {
			flex: 2;
			// border: 1px solid;
			border-right-style: solid;
			padding-left: 1vw;
		}
	}
}

.tips_content {
	border: 2px solid;
	margin: 1vh 5vh 0 5vh;
	height: 50%;
	padding: 1vw;
}

.crowd_class_title {
	font-weight: 700;
}
.crowd_class {
	border: 2px solid;
	display: flex;
	flex-direction: column;
	margin: 1vh 5vh 0 5vh;
	align-items: center;
	font-size: 1.5vw;
}

.crowd_class_content {
	// border: 1px solid ;
	margin: 0 5vh 0 5vh;
	height: 30vh;
	display: flex;

	.content_one {
		flex: 2;
		border: 2px solid;
		border-top-style: none;
		border-top-style: none;

		display: flex;
		flex-direction: column;

		.content_one_head {
			flex: 1;
			// border: 1px solid;

			display: flex;

			.content_one_head_left {
				// border: 1px solid;
				border-bottom-style: solid;
				border-right-style: solid;
				flex: 3.24;
				padding-left: 1vw;
			}
			.content_one_head_right {
				// border: 1px solid;
				border-bottom-style: solid;
				flex: 1;
				padding-left: 1vw;
			}
		}
		.content_one_content {
			flex: 4;

			display: flex;

			.content_one_content_item_1 {
				border-right-style: solid;
				flex: 1;
				padding-top: 8vh;
				text-align: center;
			}
			.content_one_content_item_2 {
				border-right-style: solid;
				flex: 2;

				display: flex;
				flex-direction: column;

				.content_one_content_item_2_item_1 {
					flex: 1;
					border-bottom-style: solid;
					padding-left: 1vw;
				}
				.content_one_content_item_2_item_2 {
					flex: 1;
					border-bottom-style: solid;
					padding-left: 1vw;
				}
				.content_one_content_item_2_item_3 {
					flex: 1;
					border-bottom-style: solid;
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
					border-bottom-style: solid;
					padding-left: 1vw;
				}
				.content_one_content_item_3_item_2 {
					flex: 1;
					border-bottom-style: solid;
					padding-left: 1vw;
				}
				.content_one_content_item_3_item_3 {
					flex: 1;
					border-bottom-style: solid;
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
		border-bottom-style: solid;
		border-right-style: solid;
		border-top-style: none;

		display: flex;
		flex-direction: column;

		.content_tow_item_column3_2 {
			flex: 2;
			border-bottom-style: solid;
			display: flex;

			.content_tow_item_column3_item_1 {
				flex: 1.9;
				border-right-style: solid;

				padding-top: 5vh;
				text-align: center;

				display: flex;
				flex-direction: column;
			}
			.content_tow_item_column3_item_1point5 {
				flex: 1.5;

				display: flex;
				flex-direction: column;

				.content_tow_item_column3_item_1point5_item_col_1 {
					flex: 1;
					border-bottom-style: solid;
					border-right-style: solid;
					padding-left: 1vw;
				}

				.content_tow_item_column3_item_1_item_col_1 {
					flex: 1;
					border-bottom-style: solid;
					padding-left: 1vw;
				}
			}
		}
		.content_tow_item_column3_1 {
			flex: 1;
			border-bottom-style: solid;

			display: flex;

			.content_tow_item_column3_item_2point5 {
				flex: 2.5;
				border-right-style: solid;
				padding-top: 1vh;
				padding-left: 1vw;
			}
			.content_tow_item_column3_item_1 {
				flex: 1;

				padding-top: 1vh;
				padding-left: 1vw;
			}
		}
	}

	.content_three {
		flex: 1.5;
		// border: 1px solid ;
		border-right-style: solid;
		border-bottom-style: solid;

		display: flex;
		flex-direction: column;

		.content_three_item_1 {
			flex: 1;
			// border: 1px solid ;
			border-bottom-style: solid;

			display: flex;

			.content_three_item_1_1 {
				flex: 1;
				// border: 1px solid ;
				border-left-style: solid;

				padding-top: 1vh;
				padding-left: 1vw;
			}
			.content_three_item_1_4 {
				flex: 4.8;
				// border: 1px solid ;
				padding-top: 1vh;
				padding-left: 1vw;
			}
		}
		.content_three_item_2 {
			flex: 2;
			// border: 1px solid ;

			display: flex;

			.content_three_item_2_1 {
				flex: 1;
				// border: 1px solid ;
				border-right-style: solid;

				display: flex;
				flex-direction: column;
				.content_three_item_2_1_col_1 {
					flex: 1;
					// border: 1px solid ;
					border-bottom-style: solid;
					padding-left: 1vw;
				}
			}
			.content_three_item_2_0point5 {
				flex: 0.5;
				// border: 1px solid ;

				display: flex;
				flex-direction: column;
				.content_three_item_2_1_col_1 {
					flex: 1;
					// border: 1px solid ;
					border-bottom-style: solid;
					padding-left: 1vw;
				}
			}
		}
	}
}
</style>
