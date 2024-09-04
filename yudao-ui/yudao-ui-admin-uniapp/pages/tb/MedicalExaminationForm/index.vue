<template>
	<view class="content">
		<view class="main">
			<view class="title">体检表</view>
			<view style="font-size: 18px">
				<view class="dis">
					<view>筛查编号:{{ personInfo.screenId }}</view>
					<view style="margin-left: 30px">身份证号:{{ personInfo.idNum }}</view>
				</view>
				<view class="dis">
					<view>姓名:{{ personInfo.name }}</view>
					<view style="margin: 0 30px">年龄:{{ personInfo.age }}岁</view>
					<view>
						体检日期:{{ collectTime.slice(0, 4) }}{{ collectTime ? '年' : '' }}{{ collectTime.slice(5, 7)
						}}{{ collectTime ? '月' : '' }}{{ collectTime.slice(8, 10) }}{{ collectTime ? "日" : '' }}
					</view>
				</view>
			</view>
			<table>
				<tr>
					<th colspan="8">人群分类(可多选)</th>
				</tr>
				<tr>
					<td colspan="2">活动性肺结核密切接触者</td>
					<td>
						<view class="custom-checkbox" :class="{ checked: person.closeContacts }"></view>
					</td>
					<td rowspan="2">老年人</td>
					<td rowspan="2">
						<view class="custom-checkbox" :class="{ checked: person.oldPeople }"></view>
					</td>
					<td colspan="2">HIV/AIDS</td>
					<td>
						<view class="custom-checkbox" :class="{ checked: person.hivAids }"></view>
					</td>
				</tr>
				<tr>
					<td rowspan="4">在校师生</td>
					<td>0-5岁学生</td>
					<td>
						<view class="custom-checkbox" :class="{ checked: person.belowFive }"></view>
					</td>
					<td colspan="2">既往结核病患者</td>
					<td>
						<view class="custom-checkbox" :class="{ checked: person.previousTuberculosisPatients }"></view>
					</td>
				</tr>
				<tr>
					<td>6-14岁学生</td>
					<td><view class="custom-checkbox" :class="{ checked: person.studentUnderFourteen }"></view></td>
					<td rowspan="3">糖尿病患者</td>
					<td rowspan="3"><view class="custom-checkbox" :class="{ checked: person.diabetes }"></view></td>
					<td rowspan="3">非重点人群</td>
					<td>0-5岁</td>
					<td><view class="custom-checkbox" :class="{ checked: person.nonpointBelowFive }"></view></td>
				</tr>
				<tr>
					<td>≥15岁学生</td>
					<td><view class="custom-checkbox" :class="{ checked: person.studentFourteenUpper }"></view></td>
					<td>6-14岁</td>
					<td><view class="custom-checkbox" :class="{ checked: person.nonpointUnderFourteen }"></view></td>
				</tr>
				<tr>
					<td>教职工</td>
					<td><view class="custom-checkbox" :class="{ checked: person.facultyAndStaff }"></view></td>
					<td>≥15岁</td>
					<td><view class="custom-checkbox" :class="{ checked: person.nonpointFourteenUpper }"></view></td>
				</tr>
			</table>
			<table style="margin-top: 5px">
				<tr>
					<td class="text">
						<b>活动性肺结核密切接触者:</b>
						症状筛查+ppd+胸片检查,异常或强阳性进行实验室检查。
						<br />
						<b>0-5岁学生:</b>
						症状筛查,有症状做 ppd,强阳性进一步检查;
						<b>6-14岁学生:</b>
						症状查+ppd,有症状或强 阳性进一步检查;
						<b>≥15岁学生:</b>
						症状筛 査+ppd+胸片检查,有症状或强阳性或异常进一步检查;
						<br />
						<b>教职工:</b>
						症状筛查+胸片检查,有症状或异常进一步检查。
						<br />
						<b>老年人、糖尿病患者、HIV/AIDS和既往结核病患者:</b>
						症状筛査+胸片检查,有症状或异常进一步检查。
						<br />
						<b>0-5岁非重点人群:</b>
						症状筛查,有症状做ppd,强阳性进一步检查;
						<b>6-14岁非重点人群:</b>
						症状筛查+ppd,有症状或强阳性进一步检查;
						<b>≥15岁非重点人群:</b>
						症状筛查+胸片检查,有症状或异常进一步检查,
					</td>
				</tr>
			</table>
			<table style="margin-top: 5px">
				<tr>
					<th class="text" colspan="5">您最近1个月是否有一下症状?</th>
				</tr>
				<tr>
					<td class="text-left" colspan="2">1)咳嗽、咳痰(超过2周)</td>
					<td>{{ person.cough ? '有' : '无' }}</td>
					<td class="text-left">5)发热</td>
					<td>{{ person.fever ? '有' : '无' }}</td>
				</tr>
				<tr>
					<td colspan="2" class="text-left">2)咳血或血痰</td>
					<td>{{ person.hemoptysis ? '有' : '无' }}</td>
					<td class="text-left">6)食欲不振</td>
					<td>{{ person.anorexia ? '有' : '无' }}</td>
				</tr>
				<tr>
					<td class="text-left" colspan="2">3)乏力、盗汗</td>
					<td>{{ person.weak ? '有' : '无' }}</td>
					<td class="text-left">7)胸痛</td>
					<td>{{ person.chestPain ? '有' : '无' }}</td>
				</tr>
				<tr>
					<td colspan="2" class="text-left">4)体重减轻(超过6斤)</td>
					<td>{{ person.weightLoss ? '有' : '无' }}</td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td><view class="custom-checkbox"></view></td>
					<td><b>ppd</b></td>
					<td class="text-left" colspan="2">
						注射时间:2413
						<br />
						结果:阴性
					</td>
					<td>
						<view style="display: flex;align-items: center;">
							<span>医生签名:</span>
							<image
								class="sign-image"
								v-if="person.cardMarkSign"
								:src="person.cardMarkSign"
								mode="widthFix"
							></image>
						</view>
					</td>
				</tr>
				<tr>
					<td><view class="custom-checkbox"></view></td>
					<td><b>胸部X线</b></td>
					<td class="text-left" colspan="2">
						<view class="bom-dis">
							<view><view class="custom-checkbox"></view></view>
							<view style="margin: 0 5px">无结核相关异常</view>
							<view style="margin: 0 5px 0 20px">
								<view class="custom-checkbox"></view>
							</view>
							<view>疑似结核</view>
						</view>
						<view>机器中与患者对应的编码:786465-1</view>
					</td>
					<td class="text-left">医生签名:</td>
				</tr>
			</table>
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
		// console.log(this.personInfo);
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
						this.person.studentUnderFourteen = true;
					} else {
						this.person.studentFourteenUpper = true;
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
			getCollectOen(
				this.personInfo.id,
				this.personInfo.orderVal,
				this.personInfo.year,
				this.personInfo.screenType
			).then((res) => {
				console.log(res);
				const checkbox = res[0].outcome.toString().split('');
				//咳嗽
				this.person.cough = checkbox.includes('1');

				//
				this.person.hemoptysis = checkbox.includes('2');

				//
				this.person.weak = checkbox.includes('3');

				//
				this.person.weightLoss = checkbox.includes('4');

				//
				this.person.fever = checkbox.includes('5');

				//
				this.person.anorexia = checkbox.includes('6');

				//
				this.person.chestPain = checkbox.includes('7');

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

<style scoped lang="scss">
table {
	table-layout: fixed; /* 设置表格宽度固定 */
	border-collapse: collapse;
}
td,
th {
	word-break: break-all; /* 处理长单词换行 */
	white-space: normal; /* 允许文本换行 */
	border: 1px solid black;
	padding: 8px;
	text-align: center;
	width: 180px;
}
.content {
	font-size: 16px;
	width: 100%;
	padding: 10px;
	.title {
		text-align: center;
		font-size: 22px;
		font-weight: 600;
	}
	.text {
		text-align: left;
		width: 100vw;
	}
	.trs {
		transform: scale(1.5);
		width: 50px;
	}
	.text-left {
		text-align: left;
	}
	.bom-dis {
		display: flex;
		align-items: center;
	}
	.dis {
		display: flex;
	}
	.custom-checkbox {
		width: 20px;
		height: 20px;
		border: 1px solid #000;
		border-radius: 1px;
		cursor: pointer;
		position: relative;
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
			z-index: 1;
		}
		&.checked::after {
			opacity: 1;
		}
	}
}
.sign-image {
	margin-left: 40rpx;
	width: 90rpx;
	height: 40rpx;
	transform: rotate(90deg);
}
.position-top-left {
	position: absolute;
	top: 0;
	left: 0;
}
</style>
