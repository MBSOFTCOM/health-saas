<template>
	<view style="margin: 10px">
		<view>
			<yile-breadcrumb
				:nav="nav"
				color="rgba(153, 153, 153, 1)"
				actColor="rgba(36, 93, 209, 1)"/>
		</view>
		<view class="main-top">
			<view class="main-top-1">
				<view class="main-text">
					<view class="text-top">筛查编号</view>
					<view class="text-bom">
						{{ patient.screenId }}
					</view>
				</view>
				<view class="main-text" style="margin: 0 150px">
					<view class="text-top">姓名</view>
					<view class="text-bom">
						{{ patient.name }}
					</view>
				</view>
				<view class="main-text" style="margin-right: 150px">
					<view class="text-top">年龄</view>
					<view class="text-bom">
						{{ patient.age }}
					</view>
				</view>
				<view class="main-text">
					<view class="text-top">身份证</view>
					<view class="text-bom">
						{{ patient.idNum }}
					</view>
				</view>
			</view>
			<view class="main-top-1" v-if="stateFlag != '新增'">
				<view class="main-text">
					<view class="text-top">采集次序</view>
					<view class="text-bom" style="color: rgba(212, 48, 48, 1)">
						<span>第{{ formData.screenOrder }} 次</span>
					</view>
				</view>
				<view class="main-text" style="margin-left: 315px; margin-top: -5px">
					<view class="text-top">采集时间</view>
					<view class="text-bom">
						{{ formData.screenTime }}
					</view>
				</view>
			</view>
		</view>

		<view class="main-bottom">
			<view class="bom-left">
				<view class="bom-t">
					<view class="bom-ipt">
						<view>是否雾化</view>
						<uni-data-select
							class="set"
							v-model="formData.atomization"
							:localdata="radioItems"
							:disabled="stateFlag == '审核' || stateFlag == '详情'"/>
					</view>
				</view>
				<view class="bom-t" style="margin: 15px 0">
					<view style="width: 95px">痰检标本</view>
					<view class="bom-mup">
						<uni-data-checkbox
							multiple
							v-model="type"
							:disabled="stateFlag == '审核' || stateFlag == '详情'"
							@change="checkboxChange"
							:localdata="checkData"/>
					</view>
				</view>
				<view
					v-if="(stateFlag == '审核' || stateFlag == '详情') && showQualified"
					class="bom-t"
					style="margin: 15px 0">
					<view style="width: 95px">合格标本</view>
					<view class="bom-mup">
						<u-radio-group
							v-model="outcomeRadio"
							placement="row"
							@change="outcomeRadioChange"
							:disabled="stateFlag == '详情'">
							<u-radio
								:customStyle="{paddingRight: '20px'}"
								v-for="(item, index) in ['全部合格', '不合格']"
								:key="index"
								:label="item"
								:name="item"
								size="25px"
								labelSize="22px"
								iconSize="22px"/>
						</u-radio-group>
					</view>
				</view>
				<view class="bom-t" style="margin: 15px 0" v-if="outcomeRadio == '不合格'">
					<view style="width: 95px" v-if="stateFlag == '审核'">不合格标本</view>
					<view class="bom-mup">
						<uni-data-checkbox
							multiple
							v-model="checkOutcome"
							@change="checkChange"
							:disabled="stateFlag == '详情'"
							v-if="stateFlag == '审核'"
							:localdata="checkBox"/>
					</view>
				</view>
			</view>
			<view class="bom-right">
				<view style="padding: 5px 5px 10px 5px; font-size: 18px">采集图片</view>
				<view style="display: flex; align-items: flex-start; justify-content: space-evenly">
					<view>
						<u-album singleSize="160" :urls="[this.formData.forthwithSputum]"></u-album>
						<view
							:class="{ 'custom-style': !this.formData.forthwithSputum, 'custom-style2': this.formData.forthwithSputum}"
							@click="takePhoto(1)"
							v-if="type.includes(1) && (stateFlag == '修改' || stateFlag == '新增')"
						>
							<up-icon name="plus" color="rgba(36, 93, 209, 1)" :size="this.formData.forthwithSputum ? 20 : 40"></up-icon>
							<span v-if="!this.formData.forthwithSputum">采集及时痰照片</span>
							<span v-if="this.formData.forthwithSputum">修改及时痰照片</span>
						</view>
					</view>
					<view>
						<u-album singleSize="160" :urls="[this.formData.morningSputum]"></u-album>
						<view
							:class="{ 'custom-style': !this.formData.morningSputum, 'custom-style2': this.formData.morningSputum}"
							@click="takePhoto(2)"
							v-if="type.includes(2) && (stateFlag == '修改' || stateFlag == '新增')"
						>
							<up-icon name="plus" color="rgba(36, 93, 209, 1)" :size="this.formData.morningSputum ? 20 : 40"></up-icon>
							<span v-if="!this.formData.forthwithSputum">采集晨痰照片</span>
							<span v-if="this.formData.forthwithSputum">修改晨痰照片</span>
						</view>
					</view>
					<view>
						<u-album singleSize="160" :urls="[this.formData.eveningSputum]"></u-album>
						<view
							:class="{ 'custom-style': !this.formData.eveningSputum, 'custom-style2': this.formData.eveningSputum}"
							@click="takePhoto(3)"
							v-if="type.includes(3) && (stateFlag == '修改' || stateFlag == '新增')"
						>
							<up-icon name="plus" color="rgba(36, 93, 209, 1)" :size="this.formData.eveningSputum ? 20 : 40"></up-icon>
							<span v-if="!this.formData.forthwithSputum">采集夜痰照片</span>
							<span v-if="this.formData.forthwithSputum">修改夜痰照片</span>
						</view>
					</view>
				</view>
			</view>
		</view>
		<!--      签名-->
		<view class="sg-bom">
			<view class="bom-m">
				<view>医生签名</view>
				<view class="sign-bg" v-if="!this.formData.doctorSignature" @click="onSign">
					<view class="sign-text">
						<image class="sign-img" src="../../../static/images/tb/sign.png" mode=""/>
						<span>点击签名</span>
					</view>
				</view>
				<view
					class="sign-imgbg"
					v-if="this.formData.doctorSignature"
					:style="{
						background:
							stateFlag == '详情' || stateFlag == '审核'
								? 'rgba(245, 245, 245, 1)'
								: 'rgba(255, 246, 235, 1)',
						border: stateFlag == '详情' || stateFlag == '审核' ? 'none' : '1px solid rgba(255, 185, 20, 1)'
					}">
					<view style="margin-left: -50px">
						<image class="sign-image" :src="this.formData.doctorSignature" mode="widthFix"/>
					</view>
					<up-button
						@click="onSign"
						v-if="stateFlag == '新增' || stateFlag == '修改'"
						class="custom-reset"
						:plain="true"
						text="重写"/>
				</view>
			</view>
		</view>

		<view>
			<u-popup :show="showMark" @close="close" mode="center" @open="open" :closeOnClickOverlay="false">
				<view class="sign">
					<sp-sign-board
						v-if="showMark"
						ref="signBoardRef"
						:mark-text="markText"
						horizontal
						@reset="reset"
						@event="event"
						@firstTouchStart="firstTouchStart"/>
				</view>
			</u-popup>
		</view>
		<view style="display: flex; justify-content: space-evenly; align-items: center">
			<up-button class="cur-dy" text="打印二维码" @click="openPrint()"/>
			<up-button class="cur-add" text="保存" @click="submitForm" v-if="stateFlag != '详情'"/>
		</view>
		<!--    二维码-->
		<u-popup :show="showGather" :closeOnClickOverlay="false" mode="center" @close="showGather = false">
			<view class="gather-open">
				<uni-data-select
					v-model="selectType"
					:localdata="selectData"
					@change="print()"
					:clear="false"/>
				<view style="font-size: 18px; font-weight: 600; margin-top: 20rpx">{{ this.patient.encryptionIdNum+selectType+this.patient.screenType }}</view>
				<view style="display: flex; flex-direction: column; align-items: center; justify-content: center;margin-top: 20rpx;">
					<view v-for="(row, rowI) in modules" :key="rowI" style="display: flex; flex-direction: row">
						<view v-for="(col, colI) in row" :key="colI">
							<view v-if="col.isBlack" style="width: 6px; height: 6px; background-color: black">
								<!-- 黑色码点 -->
							</view>
							<view v-else style="width: 6px; height: 6px; background-color: white">
								<!-- 白色码点 -->
							</view>
						</view>
					</view>
					<view style="font-size: 18px; font-weight: 600; margin-top: 10px">
						{{this.patient.name}}--{{ getLabelByValue(sputumType(), selectType) }}
					</view>
				</view>
				<view class="scan-btn">
					<up-button style="width: 100px" text="取消" @click="showGather = false"></up-button>
					<up-button type="primary" style="width: 100px" text="打印"></up-button>
				</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
import { getLabelByValue, getValueByKey, getItemByValue, sex, sputumType, sputumCheck } from '../../../utils/dict';
import { getCTById, insertToSum, updateSum } from '../../../api/screen/dr';
// import QRCode from 'qrcode';
import UQRCode from '../../../uni_modules/Sansnn-uQRCode/js_sdk/uqrcode/uqrcode';
import {
	dbName,
	getMaxScreenOrder,
	openTransaction,
	rollbackTransaction,
	commitTransaction,
	tbScreenChestRadiograph,
	tbScreenSputumExamination,
	tbScreenSum,
	user
} from '../../../utils/sqlite';
import { getByPersonId } from '../../../api/screen/sputumExamination';
import UniFormsItem from '../../../uni_modules/uni-forms/components/uni-forms-item/uni-forms-item.vue';
import { insertImg, updateImg } from '../../../api/screen/img';
import { toast } from '../../../utils/common';

export default {
	components: { UniFormsItem },
	data() {
		return {
			nav: [],
			showQualified: true,
			outcomeRadio: '',
			checkOutcome: [],
			checkBox: [],
			modalShow: false,
			checkData: [],
			// 单选值绑定
			radioVal: '',
			showMark: false,
			//页面状态
			markText: '',
			// 签名图片base64
			signBase64: '',
			// 签名
			signTempimg: '',
			radioItems: [
				{
					value: 1,
					text: '是'
				},
				{
					value: 0,
					text: '否'
				}
			],
			fileList1: [],
			show: false,
			// 绑定下拉框
			selectType: 1,
			// 下拉框
			selectData: [],
			// 复选框
			type: [],
			// 二维码
			modules: [],
			showGather: false,
			//页面状态
			stateFlag: '新增',
			// 患者信息
			patient: {},
			// 表单
			formData: {
				year: 2024,
				screenType: 1,
				creator: null,
				updater: null,
				updateTime: null,
				createTime: null,
				id: null,
				screenId: '',
				personId: '',
				screenTime: '',
				screenOrder: '',
				doctorSignature: '',
				atomization: '',
				type: '',
				forthwithSputum: '',
				forthwithSputumCode: '',
				eveningSputum: '',
				eveningSputumCode: '',
				morningSputum: '',
				morningSputumCode: '',
				forthwithSputumTime: null,
				morningSputumTime: null,
				eveningSputumTime: null
			}
		};
	},
	async onLoad(e) {
		let title = '痰标本';
		if (e.label === '详情') {
			title = title + e.label;
		} else {
			title = e.label + title;
		}
		uni.setNavigationBarTitle({
			title: title
		});
		this.getNavItems(uni.$screenType, e.label);
		this.getCheckData();
		this.refreshMark();
		this.patient = JSON.parse(e.val);
		this.formData.year = this.patient.year;
		this.formData.screenType = this.patient.screenType;
		// console.log('e=', e.label);
		if (!this.patient.age) {
			this.patient.age = '';
		}
    this.patient.encryptionIdNum=this.changeIdcard(this.patient.idNum)
		this.stateFlag = e.label;
		if (e.label != '新增') {
			let data = await getByPersonId(this.patient.id, this.patient.screenOrder, this.patient.screenType);
			this.formData = data;
			this.showRadioGroup();
			let checkInfo = [];
			let str = this.formData.type + '';
			for (let i = 0; i < str.length; i++) {
				const char = str[i];
				// console.log("char=",char)
				// console.log("int=",parseInt(char))
				checkInfo.push(parseInt(char));
			}
			this.type = checkInfo;
			if (this.type.includes(0)) {
				this.showQualified = false;
				this.formData.outcome = '';
			}
			// console.log(this.type)
			this.getPrintData();
			this.checkBox = this.getCheck();

			// console.log("info=",checkInfo)
		}
    // console.log(this.formData)
    console.log(this.patient)
  },
	methods: {
		getNavItems(screenType, label) {
			let drResult = '';
			switch (label) {
				case '修改':
					drResult = '修改痰标本';
					break;
				case '详情':
					drResult = '痰标本详情';
					break;
				case '采集':
					drResult = '新增痰标本';
					break;
				case '审核':
					drResult = '审核痰标本';
					break;
				default:
					drResult = '采集痰标本';
			}
			const screenNames = {
				1: '常规筛查',
				2: '新生入学筛查',
				3: '应急筛查'
			};
			this.nav = [{ value: screenNames[screenType] }, { value: '痰检组' }, { value: drResult, isActive: true }];
			return this.nav;
		},
		makeCode() {
			let partBefore =this.patient.encryptionIdNum
			if ((this.formData.type + '').includes('1')) {
				this.formData.forthwithSputumCode = partBefore + '1' + uni.$screenType;
			} else {
				this.formData.forthwithSputumCode = '';
			}
			if ((this.formData.type + '').includes('2')) {
				this.formData.morningSputumCode = partBefore + '3' + uni.$screenType;
			} else {
				this.formData.morningSputumCode = '';
			}
			if ((this.formData.type + '').includes('3')) {
				this.formData.eveningSputumCode = partBefore + '2' + uni.$screenType;
			} else {
				this.formData.eveningSputumCode = '';
			}
		},
		showRadioGroup() {
			if (this.formData.outcome + '' == '') {
				this.outcomeRadio = '全部合格';
			}
			if (this.formData.outcome) {
				this.outcomeRadio = '不合格';
			}
			let list = [];
			let str = this.formData.outcome + '';
			for (let i = 0; i < str.length; i++) {
				list.push(parseInt(str[i]));
			}
			this.checkOutcome = list;
			// console.log('list=', this.checkOutcome);
			// console.log('outcomeRadio=', this.outcomeRadio);
		},
		sputumType() {
			// console.log(sputumType);
			return sputumType;
		},
		sputumCheck() {
			return sputumCheck;
		},
		sex() {
			return sex;
		},
		getLabelByValue,
		getValueByKey,
		clickSpan(item) {
			// console.log(item);
		},
		showModal(data) {
			this.checkBox = this.getCheck();
			// console.log('box=', this.checkBox);
			this.modalShow = true;
		},
		// 签名
		onSign() {
			this.refreshMark();
			this.showMark = true;
			uni.$on('getSignImg', ({ base64, path }) => {
				this.signBase64 = base64;
				this.signTempimg = path;
				// console.log('==== base64 path :', base64, path);
				this.savePhoto(path, 4);
				// 之后取消监听，防止重复监听
				uni.$off('getSignImg');
			});
		},
		firstTouchStart() {
			// this.refreshMark();
			// 手动调用组件内绘制水印方法重新绘制
			this.$refs.signBoardRef.drawMark(this.refreshMark());
		},
		takePhoto(type) {
			uni.chooseImage({
				count: 1, // 最多可以选择的图片张数
				sourceType: ['camera', 'album'], // 指定拍照模式
				success: (res) => {
					const tempFilePaths = res.tempFilePaths;
					this.savePhoto(tempFilePaths[0], type); // 保存拍摄的照片
				}
			});
		},
		async savePhoto(tempFilePath, type) {
			uni.saveFile({
				tempFilePath: tempFilePath, // 需要保存的文件的临时路径
				success: async (res) => {
					// console.log('res=', res);
					const savedFilePath = res.savedFilePath;
					let time = this.refreshMark();
					switch (type) {
						case 1:
							this.formData.forthwithSputum = savedFilePath;
							this.formData.forthwithSputumTime = '' + time[0] + ' ' + time[1];
							break;
						case 2:
							this.formData.morningSputum = savedFilePath;
							this.formData.morningSputumTime = '' + time[0] + ' ' + time[1];
							break;
						case 3:
							this.formData.eveningSputum = savedFilePath;
							this.formData.eveningSputumTime = '' + time[0] + ' ' + time[1];
							break;
						case 4:
							this.formData.doctorSignature = savedFilePath;
							break;
					}
					// 将保存后的文件路径赋值给photoUrl以显示在页面上
					this.photoUrl = savedFilePath;
					uni.showToast({
						title: '照片保存成功',
						icon: 'success'
					});
				},
				fail: () => {
					uni.showToast({
						title: '照片保存失败',
						icon: 'none'
					});
				}
			});
		},
		reset() {
			this.refreshMark();
		},
		event() {
			this.showMark = false;
		},
		getCheckData() {
			this.checkData = this.sputumType().map((obj) => {
				return { text: obj.label, value: obj.value };
			});
			// console.log("this.data=",this.checkData)
		},
		getCheck() {
			let list = [];
			// console.log("data=",this.type)
			this.type.forEach((o) => {
				list.push(getItemByValue(this.sputumType(), o));
			});
			// console.log('itemList=', list);
			return list.map((obj) => {
				return { text: obj.label, value: obj.value };
			});
			/* return this.sputumCheck().map(obj => {
            return { text: obj.label, value: obj.value };
          })*/
		},
		//表单校验
		check() {
			if (!(this.formData.personId + '').trim()) {
				this.$modal.msgError('患者不存在');
				return false;
			}
			if (!(this.formData.screenTime + '').trim()) {
				this.refreshMark();
				this.check();
			}
			if (!this.formData.doctorSignature) {
				this.$modal.msgError('请签名');
				return false;
			}
			if (!(this.formData.type + '').trim()) {
				this.$modal.msgError('未选择痰检标本');
				return false;
			} else {
				if (!this.formData.type.includes(1)) {
					this.formData.forthwithSputum = '';
					this.formData.forthwithSputumTime = null;
				} else {
					if (!this.formData.forthwithSputum) {
						this.$modal.msgError('请拍即时痰标本');
						return false;
					}
				}
				if (!this.formData.type.includes(2)) {
					this.formData.morningSputum = '';
					this.formData.morningSputumTime = null;
				}
				if (!this.formData.type.includes(3)) {
					this.formData.eveningSputum = '';
					this.formData.eveningSputumTime = null;
				}
			}
			if (!(this.formData.atomization + '').trim()) {
				this.$modal.msgError('未选择是否雾化');
				return false;
			}
			return true;
		},
		initFormData() {
			let a = {
				id: '',
				year: '',
				personId: '',
				screenType: '',
				screenTime: '',
				screenOrder: '',
				doctorSignature: '',
				atomization: '',
				type: '',
				forthwithSputum: '',
				forthwithSputumCode: '',
				eveningSputum: '',
				eveningSputumCode: '',
				morningSputum: '',
				morningSputumCode: ''
			};
			this.formData = a;
		},
		// 提交
		async submitForm() {
			uni.showLoading({
				title: '保存中',
				mask: true
			});
			openTransaction()
				.then(async (r) => {
					if (r === 200) {
						try {
							// this.initFormData()
							this.markText = this.refreshMark();
							this.formData.personId = this.patient.id;
							this.formData.year = this.patient.year;
							this.formData.screenType = this.patient.screenType;
							this.formData.screenId = this.patient.screenId;
							this.formData.screenTime = this.markText[0];
							this.formData.updateTime = this.markText[0] + ' ' + this.markText[1];
							this.formData.creator = user.id;
							this.formData.updater = user.id;
							this.formData.type = '';
							this.type.forEach((e) => {
								// console.log("e=",e)
								this.formData.type += e;
							});
							// this.formData.type=parseInt(this.formData.type)
							this.makeCode();
							if (this.stateFlag == '新增') {
								this.formData.createTime = this.markText[0] + ' ' + this.markText[1];

								let data = await getMaxScreenOrder(
									tbScreenSputumExamination,
									this.formData.personId,
									this.patient.screenType
								);
								this.formData.screenOrder = parseInt(data) + 1;

								// console.log("screenOrder=",this.formData)
							}
							// console.log('form=', this.formData);
							if (this.check()) {
								let imgForm = {
									year: this.patient.year,
									screenId: this.patient.screenId,
									screenTime: this.formData.screenTime,
									screenOrder: this.formData.screenOrder,
									screenPoint: this.patient.screenPoint,
									personId: this.formData.personId,
									type: 5,
									screenType: this.patient.screenType,
									path: ''
								};

								if (this.stateFlag == '新增') {
									// console.log('插入数据');
									delete this.formData.id;
									this.insertItem();
									if (this.formData.forthwithSputum) {
										imgForm.path = this.formData.forthwithSputum;
										imgForm.type = 5;
										insertImg(this.$dbUtils, imgForm);
									}
									if (this.formData.eveningSputum) {
										imgForm.path = this.formData.eveningSputum;
										imgForm.type = 7;
										insertImg(this.$dbUtils, imgForm);
									}
									if (this.formData.morningSputum) {
										imgForm.path = this.formData.morningSputum;
										imgForm.type = 6;
										insertImg(this.$dbUtils, imgForm);
									}
									if (this.formData.doctorSignature) {
										imgForm.path = this.formData.doctorSignature;
										imgForm.type = 12;
										insertImg(this.$dbUtils, imgForm);
									}
								} else {
									if (this.formData.forthwithSputum) {
										imgForm.path = this.formData.forthwithSputum;
										imgForm.type = 5;
										updateImg(imgForm);
									}
									if (this.formData.eveningSputum) {
										imgForm.path = this.formData.eveningSputum;
										imgForm.type = 7;
										updateImg(imgForm);
									}
									if (this.formData.morningSputum) {
										imgForm.path = this.formData.morningSputum;
										imgForm.type = 6;
										updateImg(imgForm);
									}
									if (this.formData.doctorSignature) {
										imgForm.path = this.formData.doctorSignature;
										imgForm.type = 12;
										updateImg(imgForm);
									}

									let data = {};
									data.personId = this.formData.personId;
									data.outcome = this.formData.outcome;
									data.year = uni.$user.year;
									data.chestRadiograph = this.formData.chestRadiograph;
									data.chestRadiographCode = this.formData.chestRadiographCode;
									data.remark = this.formData.remark;
									this.$dbUtils.updateSQL(
										dbName,
										tbScreenSputumExamination,
										this.formData,
										'id',
										this.formData.id
									);
									// await updateDr(this.formData.id, data, this.$dbUtils);
								}
							} else {
								return;
							}
							commitTransaction().then((r) => {
								// console.log("提交")
								if (r != 200) {
									uni.hideLoading();
									throw new Error('事务错误');
								}
								toast('保存成功');
								this.back();
							});
						} catch (e) {
							uni.hideLoading();
							throw new Error('错误');
						}
					} else {
						uni.hideLoading();
						throw new Error('事务错误');
					}
				})
				.catch((e) => {
					rollbackTransaction().then((r) => {
						// console.log("回滚")
					});
				});
		},
		//   插入痰检表
		async insertItem() {
			try {
				// console.log(this.formData)
				await this.$dbUtils.addTabItem(dbName, tbScreenSputumExamination, this.formData);
				let param = {};
				param.personId = this.formData.personId;
				param.screenType = this.patient.screenType;
				param.screenId = this.patient.screenId;
				param.lastSputumExaminationTime = this.formData.screenTime;
				let last = await getByPersonId(this.formData.personId, null, this.patient.screenType);
				param.sputumExaminationNum = last.screenOrder;
				param.sputumExaminationId = last.id;
				param.year = this.patient.year;
				param.curFinish = '痰检组';
				// console.log('param=', param);
				let data = await this.$dbUtils.selectCount(dbName, tbScreenSum, {
					personId: this.formData.personId,
					screenType: this.patient.screenType
				});
				if (data[0].count == 0) {
					await insertToSum(param, this.$dbUtils);
				} else {
					let personId = param.personId;
					delete param.personId;
					await updateSum(personId, param, this.$dbUtils);
				}
				uni.hideLoading();
				await this.$modal.msgSuccess('保存成功');
				// this.back();
			} catch (e) {
				uni.hideLoading();
				this.$modal.msgError('添加结果失败');
				throw new Error(e);
			}
		},
		back() {
			uni.navigateBack({
				delta: 1
			});
		},
		// 关闭采集心电图模态框
		cancel() {
			this.show = false;
		},
		// 复选框
		checkboxChange: function (e) {
			// console.log('type=', this.type);
			if (this.type.includes(0)) {
				// console.log('无痰');
				this.type = [0];
			}
			this.getPrintData();
		},
		getPrintData() {
			let data = [];
			for (let i = 0; i < this.type.length; i++) {
				data.push(getItemByValue(this.sputumType(), this.type[i]));
			}
			this.selectData = data.map((obj) => {
				return { text: obj.label, value: obj.value };
			});
		},
		checkChange: function (e) {
			this.formData.outcome = '';
			this.checkOutcome.forEach((e) => {
				this.formData.outcome += '' + e;
			});
			// console.log(this.formData.outcome)
		},
		outcomeRadioChange() {
			if (this.outcomeRadio == '全部合格') {
				this.checkOutcome = '';
				this.formData.outcome = '';
			}
			// console.log('out', this.outcomeRadio);
		},
		// 打开打印
		openPrint() {
			this.showGather = true;
			if (this.type.length < 1) {
				this.$modal.msgError('未选择痰类型');
				this.showGather = false;
				return;
			} else {
				if (this.type.includes(0)) {
					this.$modal.msgError('选择了无痰');
					this.showGather = false;
					return;
				}
			}
			// console.log(`selectdata=${this.selectData}`);
			this.selectType = this.type[0];
			// console.log(`selectType=${this.selectType}`);
			this.print();
		},
		// 打印
		print() {
			// 生成信息的规则：身份证号、姓名、年度、筛查类型(1-常规,2-新生,3-应急)、痰类型(1-即时痰，2-晨痰，3-夜痰)，通过 ‘;’分割
			const qr = new UQRCode();
			// console.log('this.selectType', this.selectType);
			qr.data = `${this.patient.encryptionIdNum};${this.patient.name};${this.patient.year};${uni.$screenType};${this.selectType}`;
			qr.make();
			this.modules = qr.modules;
		},
    // 身份证号码加密
    changeIdcard(idcard){
      // 反转code
      let str = idcard.split('').reverse().join('');
      // 将str从左到右切割成5个不同的字符串，字符串位数分别是 4，4，4，3，3
      let str1 = str.substring(0, 4);
      let str2 = str.substring(4, 8);
      let str3 = str.substring(8, 12);
      let str4 = str.substring(12, 15);
      let str5 = str.substring(15, 18);
      // 将身份证片段打乱为13524重新组合
      console.log(str)
      // 将切割后的字符串组合成新的
      console.log(`${str1}-${str2}-${str3}-${str4}-${str5}`)
      console.log(`${str1}-${str3}-${str5}-${str2}-${str4}`)
      return `${str1}${str3}${str5}${str2}${str4}`
    },
		// 获取系统时间
		refreshMark() {
			const currentDate = new Date();
			const year = currentDate.getFullYear();
			const month = String(currentDate.getMonth() + 1).padStart(2, '0');
			const day = String(currentDate.getDate()).padStart(2, '0');
			const hours = String(currentDate.getHours()).padStart(2, '0');
			const minutes = String(currentDate.getMinutes()).padStart(2, '0');
			const seconds = String(currentDate.getSeconds()).padStart(2, '0');
			let time = [`${year}-${month}-${day}`, `${hours}:${minutes}:${seconds}`];
			// this.markText = time;
			return time;
		}
	}
};
</script>

<style scoped lang="scss">
.main-top {
	color: rgba(150, 150, 150, 1);
	background-color: #fff;
	font-size: 18px;
	.main-top-1 {
		margin: 10px 20px;
		display: flex;
		align-items: center;
		.main-text {
			.text-bom {
				font-size: 22px;
				color: #000;
			}
		}
	}
}
.main-bottom {
	display: flex;
	font-size: 18px;
	.bom-left {
		width: 580px;
		background-color: #fff;
		padding: 10px 20px 20px 20px;
		.bom-t {
			display: flex;
			align-items: center;
			.bom-ipt {
				display: flex;
				align-items: center;
				justify-content: center;
				.set {
					margin-left: 30px;
					width: 200px;
				}
			}
			.bom-mup {
				margin-left: 10px;
			}
			.bom-stu {
				margin-left: 10px;
			}
		}
	}
	.bom-right {
		padding: 10px;
		width: 530px;
		background-color: #fff;
		margin-left: 10px;
		.custom-style {
			padding: 5px;
			display: flex;
			flex-direction: column;
			align-items: center;
			justify-content: center;
			width: 160px;
			height: 160px;
			border: 1px dashed rgba(204, 204, 204, 1);
			color: rgba(36, 93, 209, 1);
			background-color: #fff;
		}
		.custom-style2{
			padding: 5px;
			display: flex;
			align-items: center;
			justify-content: center;
			width: 160px;
			height: 40px;
			border: 1px dashed rgba(204, 204, 204, 1);
			color: rgba(36, 93, 209, 1);
			background-color: #fff;
		}
	}
}
.sg-bom {
	background-color: #fff;
	margin-top: 10px;
	padding: 20px;
	font-size: 18px;
	.bom-m {
		display: flex;
		.sign-bg {
			background: rgba(255, 246, 235, 1);
			border: 1px solid rgba(255, 185, 20, 1);
			width: 90%;
			padding: 10px;
			border-radius: 5px;
			margin-left: 30px;
			.sign-img {
				width: 20px;
				height: 20px;
			}
			.sign-text {
				display: flex;
				align-items: center;
				justify-content: center;
				color: rgba(36, 93, 209, 1);
			}
		}
		.sign-imgbg {
			position: relative;
			display: flex;
			align-items: center;
			justify-content: center;
			background: rgba(255, 246, 235, 1);
			border: 1px solid rgba(255, 185, 20, 1);
			width: 90%;
			padding: 10px;
			border-radius: 5px;
			margin-left: 30px;
			.sign-image {
				width: 60px;
				transform: scaleX(3) scaleY(2.1) rotate(90deg);
			}
			.custom-reset {
				position: absolute;
				right: 20px;
				bottom: 20px;
				width: 80px;
				border: 1px solid rgba(204, 204, 204, 1);
				color: #474747;
			}
		}
	}
}
.cur-add {
	width: 28%;
	margin: 20px 0;
	background: rgba(36, 93, 209, 1);
	color: #fff;
}
.cur-dy {
	width: 28%;
	margin: 20px 0;
	color: rgba(36, 93, 209, 1);
	background: #fff;
	border: 1px solid rgba(36, 93, 209, 1);
}
.sign {
	width: 80vw;
	height: 90vh;
}

.gather-open {
	padding: 30px 60px;
	display: flex;
	flex-direction: column;
	justify-content: space-around;
	align-items: center;
	.name-top {
		font-weight: 600;
		color: #66c7e8;
		display: flex;
		align-items: center;
		justify-content: space-around;
	}
	.scan-btn {
		padding-top: 10px;
		display: flex;
		width: 22vw;
		align-items: center;
		justify-content: space-around;
		span {
			font-size: 16px;
		}
	}
}
</style>
