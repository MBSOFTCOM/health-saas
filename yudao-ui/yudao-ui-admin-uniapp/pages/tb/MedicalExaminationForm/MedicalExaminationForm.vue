<template>
	<view class="show_span" style="">
		<view class="physical_examination">
			<view class="text_name">体检表</view>
			<view class="input_content">
				<view style="margin-bottom: -5px;">个人编号：{{personInfo.screenId}}</view>
				<view class="info_idCard">身份证号：{{personInfo.idNum}}</view>
				<view class="info_name">姓名：{{personInfo.name}}</view>
				<view class="info_age">年龄：{{personInfo.age}} 岁</view>
				<view class="info_date">体检日期：{{collectTime.slice(0,4)}}{{collectTime?年:''}}{{collectTime.slice(5,7)}}{{collectTime?月:''}}{{collectTime.slice(8,10)}}{{collectTime?日:''}}</view>
			</view>
			<view class="crowd_class">
				<view class="crowd_class_title">人群分类</view>
			</view>
			<view class="crowd_class_content">
				<view class="content_one">
					<view class="content_one_head">
						<view class="content_one_head_left">活动性肺结核密切接触者</view>
						<view class="content_one_head_right">
							<view class="custom-checkbox" :class="{ checked: person.closeContacts }"></view>
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
								<view class="custom-checkbox" :class="{ checked: person.belowFive }"></view>
							</view>
							<view class="content_one_content_item_3_item_2">
								<view class="custom-checkbox" :class="{ checked: person.studentUnderFourteen }"></view>
							</view>
							<view class="content_one_content_item_3_item_3">
								<view class="custom-checkbox" :class="{ checked: person.studentFourteenUpper }"></view>
							</view>
							<view class="content_one_content_item_3_item_4">
								<view class="custom-checkbox" :class="{ checked: person.facultyAndStaff }"></view>
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
							<view class="content_tow_item_column3_item_1point5_item_col_1" style="border-bottom-style: none">≥ 15岁</view>
						</view>
						<view class="content_tow_item_column3_item_1point5">
							<view class="content_tow_item_column3_item_1_item_col_1">
								<view class="custom-checkbox" :class="{ checked: person.monkBelowFive }"></view>
							</view>
							<view class="content_tow_item_column3_item_1_item_col_1">
								<view class="custom-checkbox" :class="{ checked: person.monkUnderFourteen }"></view>
							</view>
							<view class="content_tow_item_column3_item_1_item_col_1" style="border-bottom-style: none">
								<view class="custom-checkbox" :class="{ checked: person.monkFourteenUpper }"></view>
							</view>
						</view>
					</view>
					<view class="content_tow_item_column3_1">
						<view class="content_tow_item_column3_item_2point5">老年人</view>
						<view class="content_tow_item_column3_item_1">
							<view class="custom-checkbox" :class="{ checked: person.oldPeople }"></view>
						</view>
					</view>
					<view class="content_tow_item_column3_1" style="border-bottom-style: none">
						<view class="content_tow_item_column3_item_2point5">糖尿病患者</view>
						<view class="content_tow_item_column3_item_1">
							<view class="custom-checkbox" :class="{ checked: person.diabetes }"></view>
						</view>
					</view>
				</view>
				<view class="content_three">
					<view class="content_three_item_1">
						<view class="content_three_item_1_4">HIV/AIDS</view>
						<view class="content_three_item_1_1">
							<view class="custom-checkbox" :class="{ checked: person.hivAids }"></view>
						</view>
					</view>
					<view class="content_three_item_1">
						<view class="content_three_item_1_4">既往结核病患者</view>
						<view class="content_three_item_1_1">
							<view class="custom-checkbox" :class="{ checked: person.previousTuberculosisPatients }"></view>
						</view>
					</view>
					<view class="content_three_item_2">
						<view class="content_three_item_2_1" style="padding-top: 5vh; padding-left: 1vw">非重点人群</view>
						<view class="content_three_item_2_1">
							<view class="content_three_item_2_1_col_1">0-5岁</view>
							<view class="content_three_item_2_1_col_1">6-14岁</view>
							<view class="content_three_item_2_1_col_1" style="border-bottom-style: none">≥15岁</view>
						</view>
						<view class="content_three_item_2_0point5">
							<view class="content_three_item_2_1_col_1">
								<view class="custom-checkbox" :class="{ checked: person.nonpointBelowFive }"></view>
							</view>
							<view class="content_three_item_2_1_col_1">
								<view class="custom-checkbox" :class="{ checked: person.nonpointUnderFourteen }"></view>
							</view>
							<view class="content_three_item_2_1_col_1" style="border-bottom-style: none">
								<view class="custom-checkbox" :class="{ checked: person.nonpointFourteenUpper }"></view>
							</view>
						</view>
					</view>
				</view>
			</view>
			<view class="tips_content">
				<strong>活动性肺结核密切接触者：</strong>
				症状筛查+TST+胸片检查，异常或强阳性进行实验室检查。
				<br />
				<strong>0-5岁学生：</strong>
				症状筛查+查验卡痕，有症状做 TST，强阳性进一步检查；
				<strong>6-14 岁学生：</strong>
				症状筛查+TST+查验卡痕，有症状或强 阳性进一步检查；
				<strong>≥15 岁学生：</strong>
				症状筛查+TST+查验卡痕+胸片检查，有症状或强阳性或异常进一步检查；
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
				症状筛查+查验卡痕，有症状做 TST，强阳性进一步检查；
				<strong>6-14 岁非重点人群：</strong>
				症状筛查+TST+查验卡痕， 有症状或强阳性进一步检查；
				<strong>≥15 岁非重点人群：</strong>
				症状筛查+胸片检查，有症状或异常进一步检查。
			</view>
			<view class="symptom_input_content">
				<view class="symptom_input_content_item_1">
					<strong style="padding: 2vh; font-size: 1.5vw">您最近 1 个月内是否有以下症状？</strong>
				</view>
				<view class="symptom_input_content_item_1">
					<view class="symptom_input_content_item_1_item4_1" style="margin-left: 0.6vw;">1）咳嗽、咳痰（超过 1 周）</view>
					<view class="symptom_input_content_item_1_item4_1">
						<view style="display: flex; flex-direction: row">
							<span style="margin-top: 5px">有</span>
							<view class="custom-checkbox" :class="{ checked: person.cough }" style="margin-bottom: 15px; margin-left: 5px"></view>
						</view>

						<view style="display: flex; flex-direction: row; margin-left: 20px">
							<span style="margin-top: 5px">无</span>
							<view class="custom-checkbox" :class="{ checked: !person.cough && person.collect }" style="margin-bottom: 15px; margin-left: 5px"></view>
						</view>
					</view>
					<view class="symptom_input_content_item_1_item4_1" style="margin-right: 0.6vw">5）夜间盗汗</view>
					<view class="symptom_input_content_item_1_item4_1" style="border-right-style: none">
						<view style="display: flex; flex-direction: row">
							<span style="margin-top: 5px">有</span>
							<view class="custom-checkbox" :class="{ checked: person.nightSweat }" style="margin-bottom: 15px; margin-left: 5px"></view>
						</view>

						<view style="display: flex; flex-direction: row; margin-left: 20px">
							<span style="margin-top: 5px">无</span>
							<view class="custom-checkbox" :class="{ checked: !person.nightSweat && person.collect }" style="margin-bottom: 15px; margin-left: 5px"></view>
						</view>
					</view>
				</view>
				<view class="symptom_input_content_item_1">
					<view class="symptom_input_content_item_1_item4_1" style="margin-left: 0.6vw;">2）咯血或血痰</view>
					<view class="symptom_input_content_item_1_item4_1">
						<view style="display: flex; flex-direction: row">
							<span style="margin-top: 5px">有</span>
							<view class="custom-checkbox" :class="{ checked: person.hemoptysis }" style="margin-bottom: 15px; margin-left: 5px"></view>
						</view>

						<view style="display: flex; flex-direction: row; margin-left: 20px">
							<span style="margin-top: 5px">无</span>
							<view class="custom-checkbox" :class="{ checked: !person.hemoptysis && person.collect }" style="margin-bottom: 15px; margin-left: 5px"></view>
						</view>
					</view>
					<view class="symptom_input_content_item_1_item4_1" style="margin-right: 0.6vw">6）食欲不振</view>
					<view class="symptom_input_content_item_1_item4_1" style="border-right-style: none">
						<view style="display: flex; flex-direction: row">
							<span style="margin-top: 5px">有</span>
							<view class="custom-checkbox" :class="{ checked: person.anorexia }" style="margin-bottom: 15px; margin-left: 5px"></view>
						</view>
						<view style="display: flex; flex-direction: row; margin-left: 20px">
							<span style="margin-top: 5px">无</span>
							<view class="custom-checkbox" :class="{ checked: !person.anorexia && person.collect }" style="margin-bottom: 15px; margin-left: 5px"></view>
						</view>
					</view>
				</view>
				<view class="symptom_input_content_item_1">
					<view class="symptom_input_content_item_1_item4_1" style="margin-left: 0.6vw;">3）发热</view>
					<view class="symptom_input_content_item_1_item4_1" style="border-right-style: none">
						<view style="display: flex; flex-direction: row">
							<span style="margin-top: 5px">有</span>
							<view class="custom-checkbox" :class="{ checked: person.fever }" style="margin-bottom: 15px; margin-left: 5px"></view>
						</view>

						<view style="display: flex; flex-direction: row; margin-left: 20px">
							<span style="margin-top: 5px">无</span>
							<view class="custom-checkbox" :class="{ checked: !person.fever && person.collect }" style="margin-bottom: 15px; margin-left: 5px"></view>
						</view>
					</view>
					<view class="symptom_input_content_item_1_item4_1" style="border-left-style: solid;margin-right: 0.6vw">7）乏力</view>
					<view class="symptom_input_content_item_1_item4_1" style="border-right-style: none">
						<view style="display: flex; flex-direction: row">
							<span style="margin-top: 5px">有</span>
							<view class="custom-checkbox" :class="{ checked: person.weak }" style="margin-bottom: 15px; margin-left: 5px"></view>
						</view>

						<view style="display: flex; flex-direction: row; margin-left: 20px">
							<span style="margin-top: 5px">无</span>
							<view class="custom-checkbox" :class="{ checked: !person.weak && person.collect }" style="margin-bottom: 15px; margin-left: 5px"></view>
						</view>
					</view>
				</view>
				<view class="symptom_input_content_item_1">
					<view class="symptom_input_content_item_1_item4_1" style="margin-left: 0.6vw;">4）胸痛</view>
					<view class="symptom_input_content_item_1_item4_1" style="border-right-style: none">
						<view style="display: flex; flex-direction: row">
							<span style="margin-top: 5px">有</span>
							<view class="custom-checkbox" :class="{ checked: person.chestPain }" style="margin-bottom: 15px; margin-left: 5px"></view>
						</view>

						<view style="display: flex; flex-direction: row; margin-left: 20px">
							<span style="margin-top: 5px">无</span>
							<view class="custom-checkbox" :class="{ checked: !person.chestPain && person.collect }" style="margin-bottom: 15px; margin-left: 5px"></view>
						</view>
					</view>
					<view class="symptom_input_content_item_1_item4_1" style="border-left-style: solid;margin-right: 0.6vw">8）体重减轻（超过 6 斤）</view>
					<view class="symptom_input_content_item_1_item4_1" style="border-right-style: none">
						<view style="display: flex; flex-direction: row">
							<span style="margin-top: 5px">有</span>
							<view class="custom-checkbox" :class="{ checked: person.weightLoss }" style="margin-bottom: 15px; margin-left: 5px"></view>
						</view>

						<view style="display: flex; flex-direction: row; margin-left: 20px">
							<span style="margin-top: 5px">无</span>
							<view class="custom-checkbox" :class="{ checked: !person.weightLoss && person.collect }" style="margin-bottom: 15px; margin-left: 5px"></view>
						</view>
					</view>
				</view>
				<view class="symptom_input_content_item_1">
					<view class="symptom_input_content_item_1_item4_1">
						<view class="symptom_input_content_item_1_item4_1_1">
							<view class="custom-checkbox" :class="{ checked: person.collect }" style="margin-bottom: 15px; margin-left: 5px"></view>
						</view>
						<view class="symptom_input_content_item_1_item4_1_3" style="border-right-style: none">
							<strong>查验卡痕</strong>
						</view>
					</view>
					<view class="symptom_input_content_item_1_item4_2">
						<view style="display: flex; flex-direction: row">
							<view style="display: flex; flex-direction: row">
								<span style="margin-top: 3px">有</span>
								<view class="custom-checkbox" :class="{ checked: person.cardMark }" style="margin-bottom: 15px; margin-left: 5px"></view>
							</view>

							<view style="display: flex; flex-direction: row; margin-left: 20px">
								<span style="margin-top: 3px">无</span>
								<view class="custom-checkbox" :class="{ checked: !person.cardMark && person.collect }" style="margin-bottom: 15px; margin-left: 5px"></view>
							</view>
						</view>
					</view>
					<view class="symptom_input_content_item_1_item4_1" style="border-right-style: none; height: 10vh">
						<span style="width: 180rpx; height: 40rpx; margin-top: 20rpx">医生签字：</span>
						<image class="sign-image" style="margin: -50rpx 20rpx 20rpx 40rpx" v-if="person.cardMarkSign" :src="person.cardMarkSign" mode="widthFix"></image>
					</view>
				</view>
				<view class="symptom_input_content_item_1">
					<view class="symptom_input_content_item_1_item4_1">
						<view class="symptom_input_content_item_1_item4_1_1">□</view>
						<view class="symptom_input_content_item_1_item4_1_3" style="border-right-style: none">
							<strong>TST</strong>
						</view>
					</view>
					<view class="symptom_input_content_item_1_item4_2">注射时间： 年 月 日 时（24 小时制）</view>
					<view class="symptom_input_content_item_1_item4_1" style="border-right-style: none">医生签字：</view>
				</view>
				<view class="symptom_input_content_item_2">
					<view class="symptom_input_content_item_1_item4_1">
						<view class="symptom_input_content_item_1_item4_1_1">□</view>
						<view class="symptom_input_content_item_1_item4_1_3" style="border-right-style: none">
							<strong>胸部 X 线</strong>
						</view>
					</view>
					<view class="symptom_input_content_item_1_item4_2">
						□无结核相关异常 □疑似结核
						<br />
						<view style="padding-top: 2vh">机器中与患者对应的编码：</view>
					</view>
					<view class="symptom_input_content_item_1_item4_1" style="border-right-style: none">医生签字：</view>
				</view>
				<view class="symptom_input_content_item_1" style="border-top-style: solid; border-bottom-style: none">
					<view class="symptom_input_content_item_1_item4_1">
						<view class="symptom_input_content_item_1_item4_1_1">□</view>
						<view class="symptom_input_content_item_1_item4_1_3" style="border-right-style: none">
							<strong>痰标本</strong>
						</view>
					</view>
					<view class="symptom_input_content_item_1_item4_2">□即时痰 □发放晨痰、夜间痰盒 □无痰</view>
					<view class="symptom_input_content_item_1_item4_1" style="border-right-style: none">医生签字：</view>
				</view>
			</view>
			<view class="check_signature">质检人员签字:</view>
		</view>
	</view>
</template>

<script>
import { getCollectOen } from '@/utils/sqlite.js';
export default {
	data() {
		return {
			//各项是否勾选
			person: {
				closeContacts: false,
				studentBelowFive: false,
				studentUnderFourteen: false,
				studentFourteenUpper: false,
				facultyAndStaff: false,
				monkBelowFive: false,
				monkUnderFourteen: false,
				monkFourteenUpper: false,
				oldPeople: false,
				diabetes: false,
				hivAids: false,
				previousTuberculosisPatients: false,
				nonpointBelowFive: false,
				nonpointUnderFourteen: false,
				nonpointFourteenUpper: false,
				collect: false,
				cough: false,
				hemoptysis: false,
				fever: false,
				chestPain: false,
				nightSweat: false,
				anorexia: false,
				weak: false,
				weightLoss: false,
				cardMark: false,
				cardMarkSign: ''
			},
			//采集时间
			collectTime: '',
			treatment_program: '',
			personInfo: {},
			crowdArr: [],
			items2: [
				{
					text: '学生',
					value: '1'
				},
				{
					text: '老年人',
					value: '2'
				},
				{
					text: '教职工',
					value: '4'
				},
				{
					text: '密接者',
					value: '8'
				},
				{
					text: '糖尿病',
					value: '16'
				},
				{
					text: '僧尼',
					value: '32'
				},
				{
					text: '既往患者',
					value: '64'
				},
				{
					text: 'HIV/AIDS',
					value: '128'
				}
			],
			//教职工
			items3: [
				{
					text: '密接者',
					value: '8'
				},
				{
					text: '糖尿病',
					value: '16'
				},
				{
					text: '既往患者',
					value: '64'
				},
				{
					text: 'HIV/AIDS',
					value: '128'
				}
			]
		};
	},
	onLoad(option) {
		this.personInfo = JSON.parse(option.val);

		this.displaySelected(this.personInfo.moreType);
		//处理数据
		this.getData();
		// /处理采集组数据
		this.getCollect();
	},
	methods: {
		//患者主要信息
		getData() {
			if (this.personInfo.firstType == 2) {
				if (this.personInfo.age <= 5) {
					this.person.nonpointBelowFive = true;
				} else if (this.personInfo.age <= 14) {
					this.person.nonpointUnderFourteen = true;
				} else {
					this.person.nonpointFourteenUpper = true;
				}
			} else if (this.personInfo.firstType == 4) {
				this.person.facultyAndStaff = true;

				//是否为密接着
				this.person.closeContacts = this.crowdArr.includes('8');

				//是否为糖尿病患者
				this.person.diabetes = this.crowdArr.includes('16');

				//是否为既往结核病患者
				this.person.previousTuberculosisPatients = this.crowdArr.includes('64');

				//是否为HIV/AIDS
				this.person.hivAids = this.crowdArr.includes('128');
			} else {
				//如果是学生
				if (this.crowdArr.includes('1')) {
					if (this.personInfo.age <= 5) {
						this.person.studentBelowFive = true;
					} else if (this.personInfo.age <= 14) {
						this.person.studentFourteenUpper = true;
					} else {
						this.person.studentUnderFourteen = true;
					}
				}
				//如果是僧尼
				if (this.crowdArr.includes('32')) {
					if (this.personInfo.age <= 5) {
						this.person.monkBelowFive = true;
					} else if (this.personInfo.age <= 14) {
						this.person.monkFourteenUpper = true;
					} else {
						this.person.monkUnderFourteen = true;
					}
				}
				//是否是老年人
				this.person.oldPeople = this.crowdArr.includes('2');

				//是否为糖尿病患者
				this.person.diabetes = this.crowdArr.includes('16');

				//是否为既往结核病患者
				this.person.previousTuberculosisPatients = this.crowdArr.includes('64');

				//是否为HIV/AIDS
				this.person.hivAids = this.crowdArr.includes('128');
			}
		},
		//患者采集组信息
		getCollect() {
			if (this.personInfo.orderVal == null) {
				return;
			}
			this.person.collect = true;
			getCollectOen(this.personInfo.id, this.personInfo.orderVal).then((res) => {
				const checkbox = res[0].outcome.toString().split('');

				//咳嗽
				this.person.cough = checkbox.includes('1');

				//
				this.person.hemoptysis = checkbox.includes('2');

				//
				this.person.fever = checkbox.includes('3');

				//
				this.person.chestPain = checkbox.includes('4');

				//
				this.person.nightSweat = checkbox.includes('5');

				//
				this.person.anorexia = checkbox.includes('6');

				//
				this.person.weak = checkbox.includes('7');

				//
				this.person.weightLoss = checkbox.includes('8');

				//有无卡痕
				this.person.cardMark = checkbox.includes('9');

				//保存下采集时间
				this.collectTime = res[0].screenTime;

				//签名
				this.person.cardMarkSign = res[0].doctorSignature;
			});
		},
		//人群选择回显方法
		displaySelected(savedValue) {
			let selectedOptions = [];
			if (this.personInfo.firstType == 1) {
				this.items2.forEach((item) => {
					if (savedValue & parseInt(item.value, 10)) {
						selectedOptions.push(item.value);
					}
				});
			}
			if (this.personInfo.firstType == 4) {
				this.items3.forEach((item) => {
					if (savedValue & parseInt(item.value, 10)) {
						selectedOptions.push(item.value);
					}
				});
			}

			selectedOptions.forEach((i) => {
				this.crowdArr.push(i.toString());
			});
		}
	}
};
</script>

<style lang="scss" scoped>
.info_code {
	width: 20vw;
}
.custom-checkbox {
	width: 15px;
	height: 15px;
	border: 1px solid #787878;
	border-radius: 3px;
	cursor: pointer;
	position: relative;
	top: 25%;
	margin: auto;
	top: 23%;

	&::after {
		content: '\2713'; /* Unicode 编码中的勾号 */
		color: #ff0000;
		font-size: 18px;
		font-weight: bold;
		position: absolute;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
		opacity: 0;
	}
	&.checked::after {
		opacity: 1;
	}
}
.show_span {
	width: 100%;
	font-size: 1.1rem;
}
.physical_examination {
	display: flex;
	flex-direction: column;
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
	margin: -3.5vh 0 0 25vw;
}

.info_name {
	margin-top: 10px;
}

.info_age {
	margin: -4.5vh 0 0 10vw;
}

.info_date {
	margin: -4.5vh 0 0 20vw;
}

.check_signature {
	margin-top: 5vh;
	margin-bottom: 2vh;
	margin-left: 80%;
}

.symptom_input_content {
	border: 2px solid;
	margin: 1vh 5vh 0 5vh;
	height: 70vh;

	display: flex;
	flex-direction: column;

	.symptom_input_content_item_1 {
		flex: 1;
		// border: 1px solid;
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
	height: 40vh;
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
				flex: 3.025;
				padding-left: 1vw;
				padding: 0 1.5vw;
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
				flex: 1.1;
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
				flex: 2;
				border-right-style: solid;
				padding-top: 1.5vh;
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
				// width: 20vw;
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
.sign-image {
	// border: 2px solid #000;
	// margin-left: 200rpx;
	width: 100rpx;
	height: 40rpx;
	transform: rotate(90deg);
}
</style>
