<template>
	<view style="margin: 10px">
		<view>
			<yile-breadcrumb
				:nav="nav"
				color="rgba(153, 153, 153, 1)"
				actColor="rgba(36, 93, 209, 1)"
			></yile-breadcrumb>
		</view>
		<view class="main-bg">
		<u-row style="font-size: 22px; font-weight: 800; margin: 5px">痰菌检查</u-row>
		<u-row justify="space-between" gutter="10">
			<view style="margin-left: 15px; font-size: 19px;">涂片结果：</view>
			<u-radio-group v-model="formData.smearResult" placement="row" style="margin: 10px">
				<template v-for="(item, index) in laboratory_smearResult" :key="index">
					<u-col span="2">
						<u-radio style="transform: scale(1.3); margin: 20rpx; margin-left: 15%" :label="item.name" :name="item.name"></u-radio>
					</u-col>
				</template>
			</u-radio-group>
		</u-row>
		
		<u-row justify="space-between" gutter="10">
			<view style="margin-left: 15px; font-size: 19px;">培养结果：</view>
			<u-radio-group v-model="formData.cultureResult" placement="row" style="margin: 10px">
				<template v-for="(item, index) in laboratory_cultureResult" :key="index">
					<u-col span="2">
						<u-radio style="transform: scale(1.3); margin: 20rpx; margin-left: 15%" :label="item.name" :name="item.name"></u-radio>
					</u-col>
				</template>
			</u-radio-group>
		</u-row>
		<u-row justify="space-between" gutter="10">
			<view style="margin-left: 15px; font-size: 19px;">分子生物学：</view>
			<u-radio-group v-model="formData.molecularBiology" placement="row" style="margin: 10px">
				<template v-for="(item, index) in laboratory_molecularBiology" :key="index">
					<u-col span="3">
						<u-radio style="transform: scale(1.3); margin: 20rpx; margin-left: 15%" :label="item.name" :name="item.name"></u-radio>
					</u-col>
				</template>
			</u-radio-group>
		</u-row>
		</view>
		<view class="main-bg">
		<u-row style="font-size: 22px; font-weight: 800; margin: 5px">组织标本</u-row>
		<u-row justify="space-between" gutter="10">
			<view style="margin-left: 15px; font-size: 19px;">检测结果：</view>
			<u-radio-group v-model="formData.tissueSpecimenResult" placement="row" style="margin: 10px">
				<template v-for="(item, index) in laboratory_tissueSpecimenResult" :key="index">
					<u-col span="3">
						<u-radio style="transform: scale(1.3); margin: 20rpx; margin-left: 15%" :label="item.name" :name="item.name"></u-radio>
					</u-col>
				</template>
			</u-radio-group>
		</u-row>
		</view>
		<view class="main-bg">
		<u-row style="font-size: 22px; font-weight: 800; margin: 5px">菌种鉴定</u-row>
		<u-row justify="space-between" gutter="10">
			<view style="margin-left: 15px; font-size: 19px;">检测结果：</view>
			<u-radio-group v-model="formData.strainIdentificationResult" placement="row" style="margin: 10px">
				<template v-for="(item, index) in laboratory_strainIdentificationResult" :key="index">
					<u-col span="3">
						<u-radio style="transform: scale(1.3); margin: 20rpx; margin-left: 15%" :label="item.name" :name="item.name"></u-radio>
					</u-col>
				</template>
			</u-radio-group>
		</u-row>
		</view>
		<view class="main-bg">
		<u-row style="font-size: 22px; font-weight: 800; margin: 5px">结核分支杆菌药敏检查</u-row>
		<u-row justify="space-between" gutter="10">
			<view style="margin-left: 15px; font-size: 19px;">药敏检测方法：</view>
			<u-radio-group v-model="formData.tbDrugSensitivityMethod" placement="row" style="margin: 10px">
				<template v-for="(item, index) in laboratory_tbDrugSensitivityMethod" :key="index">
					<u-col span="3">
						<u-radio style="transform: scale(1.3); margin: 20rpx; margin-left: 15%" :label="item.name" :name="item.name"></u-radio>
					</u-col>
				</template>
			</u-radio-group>
		</u-row>
		<u-row justify="space-between" gutter="10">
			<view style="margin-left: 15px; font-size: 19px;">耐药综合判定：</view>
			<u-radio-group v-model="formData.drugResistanceResult" placement="row" style="margin: 10px">
				<template v-for="(item, index) in laboratory_drugResistanceResult" :key="index">
					<u-col span="3">
						<u-radio style="transform: scale(1.3); margin: 20rpx; margin-left: 15%" :label="item.name" :name="item.name"></u-radio>
					</u-col>
				</template>
			</u-radio-group>
		</u-row>
		</view>
		<view class="main-bg">
		<u-row style="font-size: 22px; font-weight: 800; margin: 5px">HIV抗体检测</u-row>
		<u-row justify="space-between" gutter="10">
			<view style="margin-left: 15px; font-size: 19px;">抗体检测结果：</view>
			<u-radio-group v-model="formData.hivResult" placement="row" style="margin: 10px">
				<template v-for="(item, index) in laboratory_hivResult" :key="index">
					<u-col span="3">
						<u-radio style="transform: scale(1.3); margin: 20rpx; margin-left: 15%" :label="item.name" :name="item.name"></u-radio>
					</u-col>
				</template>
			</u-radio-group>
		</u-row>
		</view>

		<u-row  gutter="10">
			<u-button type="primary" text="保存" class="cur-add"  @click="submitForm"></u-button>
		</u-row>
	</view>
</template>

<script>
import * as laboratosyApi from '@/api/screen/laboratory/index';
import {getSputumOrder} from "../../../api/screen/laboratory";
export default {
	data() {
		return {
			nav: [],
			patientInfo: {},
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
			]
		};
	},
	onLoad(option) {
		const item = JSON.parse(decodeURIComponent(option.item));
    console.log(item)
		this.patientInfo = item;
		this.getNavItems(uni.$screenType);
		this.initData();
	},
	methods: {
		getNavItems(screenType) {
			switch (screenType) {
				case 1:
					this.nav = [{ value: '常规筛查' },{ value: '实验室组' }, { value: '实验结果', isActive: true }];
					break;
				case 2:
					this.nav = [{ value: '新生入学筛查' }, { value: '实验室组' },{ value: '实验结果', isActive: true }];
					break;
				case 3:
					this.nav = [{ value: '应急筛查' },{ value: '实验室组' }, { value: '实验结果', isActive: true }];
					break;
				default:
					this.nav;
					break;
			}
		
			return this.nav;
		},
		initData() {
			laboratosyApi
				.getScreenExperimentDetail({
					id: this.patientInfo.sputumExaminationId != null ? this.patientInfo.sputumExaminationId : this.patientInfo.id
				})
				.then((res) => {
					if (res.data) {
						this.formData = res.data;
						this.formData.hivResult = laboratosyApi.hivTestResults[this.formData.hivResult];
						this.formData.smearResult = laboratosyApi.smearResults[this.formData.smearResult];
						this.formData.cultureResult = laboratosyApi.cultureResults[this.formData.cultureResult];
						this.formData.molecularBiology = laboratosyApi.molecularBiologyResults[this.formData.molecularBiology];
						this.formData.tissueSpecimenResult = laboratosyApi.tissueTestResults[this.formData.tissueSpecimenResult];
						this.formData.strainIdentificationResult = laboratosyApi.speciesIdentificationResults[this.formData.strainIdentificationResult];
						this.formData.tbDrugSensitivityMethod = laboratosyApi.tbDrugSensitivityTestMethods[this.formData.tbDrugSensitivityMethod];
						this.formData.drugResistanceResult = laboratosyApi.drugResistanceMap[this.formData.drugResistanceResult];

						this.formData.sputumExaminationId = this.patientInfo.sputumExaminationId != null ? this.patientInfo.sputumExaminationId : this.patientInfo.id;

            laboratosyApi.getSputumOrder({id:this.patientInfo.id}).then(res=>{
              this.formData.screenOrder = res.data
            })
						this.formData.screenTime = this.patientInfo.screenTime;
						this.formData.personId = this.patientInfo.personId;
						this.formData.screenId = this.patientInfo.screenId;
					}
				});
		},
		submitForm() {
			// 提交前校验表单
			// 全部为空时，结果为ture
			let flag =
				!this.formData.drugResistanceResult &&
				!this.formData.tbDrugSensitivityMethod &&
				!this.formData.strainIdentificationResult &&
				!this.formData.tissueSpecimenResult &&
				!this.formData.molecularBiology &&
				!this.formData.cultureResult &&
				!this.formData.hivResult &&
				!this.formData.smearResult;
			if (flag) {
				uni.$u.toast('实验结果数据不能全部为空,请检查表单后提交!');
				return;
			}

			const reversedSmearResults = laboratosyApi.reverseObject(laboratosyApi.smearResults, (value) => value);
			this.formData.smearResult = reversedSmearResults[this.formData.smearResult];

			const reversedHivTestResults = laboratosyApi.reverseObject(laboratosyApi.hivTestResults, (value) => value);
			this.formData.hivResult = reversedHivTestResults[this.formData.hivResult];

			const reversedCultureResults = laboratosyApi.reverseObject(laboratosyApi.cultureResults, (value) => value);
			this.formData.cultureResult = reversedCultureResults[this.formData.cultureResult];

			const reversedMolecularBiology = laboratosyApi.reverseObject(laboratosyApi.molecularBiologyResults, (value) => value);
			this.formData.molecularBiology = reversedMolecularBiology[this.formData.molecularBiology];

			const reversedTissueSpecimenResult = laboratosyApi.reverseObject(laboratosyApi.tissueTestResults, (value) => value);
			this.formData.tissueSpecimenResult = reversedTissueSpecimenResult[this.formData.tissueSpecimenResult];

			const reversedStrainIdentificationResult = laboratosyApi.reverseObject(laboratosyApi.speciesIdentificationResults, (value) => value);
			this.formData.strainIdentificationResult = reversedStrainIdentificationResult[this.formData.strainIdentificationResult];

			const reversedTbDrugSensitivityMethod = laboratosyApi.reverseObject(laboratosyApi.tbDrugSensitivityTestMethods, (value) => value);
			this.formData.tbDrugSensitivityMethod = reversedTbDrugSensitivityMethod[this.formData.tbDrugSensitivityMethod];

			const reversedDrugResistanceResult = laboratosyApi.reverseObject(laboratosyApi.drugResistanceMap, (value) => value);
			this.formData.drugResistanceResult = reversedDrugResistanceResult[this.formData.drugResistanceResult];

			// 筛查类型
			this.formData.screenType = !!this.patientInfo.screenType ? this.patientInfo.screenType : uni.$screenType;
			// 获取本年
			const now = new Date();
			const currentYear = now.getFullYear();
			// 筛查年份
			this.formData.year = currentYear;
			laboratosyApi.createScreenExperiment(this.formData).then((res) => {
				if (res.data) {
					uni.$u.toast('提交成功！');
					this.initData();
				}
			});
		},
		isTablet() {
			// 简单的判断逻辑，实际使用时可能需要更精确的判断
			return window.innerWidth >= 768;
		}
	}
};
</script>

<style lang="scss" scoped>
.main-bg{
	background-color: #fff;
}
.cur-add {
	width: 28%;
	margin: 40px auto;
	background: rgba(36, 93, 209, 1);
	color: #fff;
}
</style>
