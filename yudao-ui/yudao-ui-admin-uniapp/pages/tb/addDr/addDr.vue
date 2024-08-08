<template>
	<view style="margin: 10px">
		<view>
			<yile-breadcrumb
				:nav="nav"
				color="rgba(153, 153, 153, 1)"
				actColor="rgba(36, 93, 209, 1)"
			></yile-breadcrumb>
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
			<view class="main-top-1" v-if="stateFlag !== '新增'">
				<view class="main-text">
					<view class="text-top">采集次序</view>
					<view class="text-bom" style="color: rgba(212, 48, 48, 1)">
						<span>第{{ patient.screenOrder }} 次</span>
					</view>
				</view>
				<view class="main-text" style="margin-left: 315px; margin-top: -5px">
					<view class="text-top">采集时间</view>
					<view class="text-bom">
						{{ formatDate(patient.screenTime) }}
					</view>
				</view>
			</view>
		</view>
		<view class="main-bottom">
			<view class="bom-left">
				<view class="bom-t">
					<view class="bom-ipt">
						<view>胸片编号</view>
						<input
							class="left-ipu"
							type="text"
							v-model="formData.computedTomographyCode"
							confirm-type="search"
							placeholder="请输入胸片编号"
							:disabled="stateFlag == '详情'"
						/>
					</view>
				</view>
				<view class="bom-t" style="margin: 15px 0">
					<view style="width: 95px">选择症状</view>
					<view class="bom-mup">
						<uni-data-checkbox
							v-model="formData.outcome"
							:localdata="outcome"
							:disabled="stateFlag == '详情'"
						></uni-data-checkbox>
					</view>
				</view>
				<view class="bom-t" style="margin: 15px 0 5px 0; align-items: flex-start">
					<view style="width: 95px">检验所见</view>
					<view class="bom-mup">
						<uni-easyinput
							style="width: 400px"
							type="textarea"
							v-model="formData.remark"
							placeholder="请输入检查描述说明"
							:disabled="stateFlag == '详情'"
						></uni-easyinput>
					</view>
				</view>
			</view>
			<view class="bom-right">
				<view style="padding: 10px; font-size: 18px">采集图片</view>
				<view style="display: flex; align-items: center; justify-content: center">
					<image style="width: 150px;height: 150px;"  :src="selectedImage" mode="aspectFit"></image>
					<up-button
						class="custom-style"
						@click="takePhoto"
						v-if="stateFlag != '详情'"
						icon="plus"
						iconColor="rgba(36, 93, 209, 1)"
					>
						<span v-if="!this.formData.chestRadiograph">采集CT照片</span>
						<span v-if="this.formData.chestRadiograph">修改CT照片</span>
					</up-button>
				</view>
			</view>
		</view>

		<view class="sg-bom">
			<view class="bom-m">
				<view>医生签名</view>
				<view class="sign-bg" v-if="!signPath" @click="onSign">
					<view class="sign-text">
						<image class="sign-img" src="../../../static/images/tb/sign.png" mode=""></image>
						<span>点击签名</span>
					</view>
				</view>
				<view
					class="sign-imgbg"
					v-if="signPath"
					:style="{
						background: stateFlag == '详情' ? 'rgba(245, 245, 245, 1)' : 'rgba(255, 246, 235, 1)',
						border: stateFlag == '详情' ? 'none' : '1px solid rgba(255, 185, 20, 1)'
					}"
				>
					<view style="margin-left: -50px">
						<image class="sign-image" :src="signPath" mode="widthFix"></image>
					</view>
					<up-button
						@click="onSign"
						v-if="stateFlag != '详情'"
						class="custom-reset"
						:plain="true"
						text="重写"
					></up-button>
				</view>
			</view>
		</view>

		<view>
			<u-popup :show="show" @close="close" mode="center" :closeOnClickOverlay="false">
				<view class="sign">
					<sp-sign-board
						v-if="show"
						ref="signBoardRef"
						:mark-text="markText"
						horizontal
						@reset="reset"
						@event="event"
						@firstTouchStart="firstTouchStart"
					></sp-sign-board>
				</view>
			</u-popup>
		</view>
		<view style="display: flex; justify-content: center">
			<up-button class="cur-add" text="保存" @click="submitForm" v-if="stateFlag !== '详情'"></up-button>
		</view>
	</view>
</template>

<script>
import UniForms from '../../../uni_modules/uni-forms/components/uni-forms/uni-forms.vue';
import UniEasyinput from '../../../uni_modules/uni-easyinput/components/uni-easyinput/uni-easyinput.vue';
import UniFormsItem from '../../../uni_modules/uni-forms/components/uni-forms-item/uni-forms-item.vue';
import { dbName, tbScreenChestRadiograph, tbScreenSum, user } from '../../../utils/sqlite';
import { getCTById, updateDr, insertToSum, updateSum } from '../../../api/screen/dr';
import { nationMap, sex, getValueByKey, getLabelByValue } from '../../../utils/dict';
import { insertImg, updateImg } from '../../../api/screen/img';
import * as ctApi from '../../../api/screen/ct';
import * as sumApi from '../../../api/screen/sum';
import { getOneCt } from '../../../api/screen/ct';
import upload from '../../../utils/upload';
export default {
	components: { UniFormsItem, UniEasyinput, UniForms },
	data() {
		return {
			nav: [],
			disabledBtu: false,
			selectedImage: null, // 保存选择的图片的临时路径
			//页面状态
			stateFlag: '新增',
			markText: '',
			signPath: null,
			// 签名图片base64
			signBase64: '',
			// 签名
			signTempimg: '',
			show: false,
			// 照片列表
			imageFile: [],
			// 结果单选
			outcome: [
				{
					text: '无结核相关异常',
					value: 0
				},
				{
					text: '疑似结核',
					value: 1
				},
				{
					text: '其他异常',
					value: 2
				}
			],
			// 结果值
			radio: null,
			// 患者信息
			patient: {},
			// 表单
			formData: {
				year: 2024,
				screenType: uni.$screenType,
				creator: null,
				updater: null,
				updateTime: null,
				createTime: null,
				photoTime: null,
				personId: '',
				screenId: '',
				outcome: '',
				computedTomographyCode: '',
				screenTime: '',
				screenOrder: '',
				computedTomography: null,
				doctorSignature: null
			},
			screenTime:''
		};
	},
	onLoad(e) {
		let title = 'CT结果';
		if (e.label === '详情') {
			title = title + e.label;
		} else {
			title = e.label + title;
		}
		uni.setNavigationBarTitle({
			title: title
		});
		this.getNavItems(uni.$screenType, e.isNew);
		this.refreshMark();
		this.patient = JSON.parse(e.val);
		this.formData.screenId = this.patient.screenId;
		this.formData.idNum = this.patient.idNum;
		this.formData.year = this.patient.year;
		if (!this.patient.age) {
			this.patient.age = '';
		}
		this.stateFlag = e.label;
		// console.log(e);
		// console.log(this.patient);
		if (e.label == '修改' || e.label == '详情') {
			ctApi.getOneCt({
					idNum: this.patient.idNum,
					screenOrder: this.patient.screenOrder,
					screenType: uni.$screenType,
					year: this.patient.year
				})
				.then((res) => {
					this.formData = res.data;
					console.log("data=",res)
					this.selectedImage = this.formData.computedTomography;
					// console.log(this.selectedImage)
					this.formData.computedTomography = null;
					this.signPath = this.formData.doctorSignature;
					// console.log(this.signPath)
					this.formData.doctorSignature = null;
					// console.log("this.formData=",this.formData);
				});
		}
	},
	methods: {
		// 时间戳转换
		formatDate(value) {
			if (value === 'null' || value === null || value === '' || value === 0) {
				return '';
			}
			const date = new Date(value);
			let y = date.getFullYear();
			let MM = date.getMonth() + 1;
			MM = MM < 10 ? '0' + MM : MM; //月补0
			let d = date.getDate();
			d = d < 10 ? '0' + d : d; //天补0
			return y + '-' + MM + '-' + d; //年月日
		},
		getNavItems(screenType, label) {
			let drResult = '';
			switch (label) {
				case '修改':
					drResult = '新增患者';
					break;
				case '详情':
					drResult = '修改患者';
					break;
				default:
					drResult = '新增患者';
			}
			const screenNames = {
				1: '常规筛查',
				2: '新生入学筛查',
				3: '应急筛查'
			};
			this.nav = [{ value: screenNames[screenType] }, { value: 'CT管理' }, { value: drResult, isActive: true }];
			return this.nav;
		},
		getValueByKey,
		getLabelByValue,
		insertImg,
		close() {},
		sex() {
			return sex;
		},
		nationMap() {
			return nationMap;
		},
		imgTest() {},
		// 签名
		onSign() {
			this.refreshMark();
			this.show = true;
			uni.$on('getSignImg', async ({ base64, path }) => {
				this.signBase64 = base64;
				this.signTempimg = path;
				// console.log('==== base64 path :', base64, path);
				let url = 'ccc';
				this.signPath = path;
				this.formData.doctorSignature = path;
				// console.log('==== path :',  path);
				// 之后取消监听，防止重复监听
				uni.$off('getSignImg');
			});
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
		},
		firstTouchStart() {
			// this.refreshMark()
			// 手动调用组件内绘制水印方法重新绘制
			this.$refs.signBoardRef.drawMark(this.refreshMark());
		},
		reset() {
			this.refreshMark();
		},
		event() {
			this.show = false;
		},
		takePhoto() {
			uni.chooseImage({
				count: 1, // 最多可以选择的图片张数
				sourceType: ['camera', 'album'], // 指定拍照模式
				success: (res) => {
					const tempFilePaths = res.tempFilePaths;
					this.selectedImage = res.tempFilePaths[0]; // 保存选择的图片的临时路径
					this.formData.computedTomography = res.tempFilePaths[0];
				}
			});
		},
		async savePhoto(tempFilePath, type) {
			uni.saveFile({
				tempFilePath: tempFilePath, // 需要保存的文件的临时路径
				success: async (res) => {
					console.log('res=', res);
					const savedFilePath = res.savedFilePath;
					if (type == 1) {
						this.formData.computedTomography = savedFilePath;
						let time = this.refreshMark();
						this.formData.photoTime = '' + time[0] + ' ' + time[1];
					} else {
						this.formData.doctorSignature = savedFilePath;
					}
					// 将保存后的文件路径赋值给photoUrl以显示在页面上

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
		uploadFilePromise(data, localPath) {
			console.log('localPath=', localPath);
			if (localPath) {
				return upload({
					url: '/admin-api/tb/screen-person/update-image2',
					filePath: localPath,
					name: 'file',
					formData: data
				});
			}
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
			if (!(this.formData.computedTomographyCode + '').trim()) {
				this.$modal.msgError('请输入胸片编号');
				return false;
			} else {
				this.formData.computedTomographyCode = (this.formData.computedTomographyCode + '').trim();
				let regex = /^[^\u4e00-\u9fa5\s]+$/;
				if (!regex.test(this.formData.computedTomographyCode)) {
					this.$modal.msgError('胸片编号错误');
					return false;
				}
			}
			if (!(this.formData.outcome + '').trim()) {
				this.$modal.msgError('请选择结果');
				return false;
			}
			if (this.formData.remark == null || this.formData.remark.trim() == '') {
				this.$modal.msgError('请输入检查所见');
				return false;
			}
			if (this.stateFlag == '新增') {
				if (!this.formData.computedTomography) {
					this.$modal.msgError('请拍ct照片');
					return false;
				}
				if (!this.formData.doctorSignature) {
					this.$modal.msgError('请签名');
					return false;
				}
			} else {
				if (!this.selectedImage) {
					this.$modal.msgError('请拍ct照片');
					return false;
				}
				if (!this.signPath) {
					this.$modal.msgError('请签名');
					return false;
				}
			}

			this.formData.remark = (this.formData.remark + '').trim();
			this.formData.computedTomographyCode = (this.formData.computedTomographyCode + '').trim();
			return true;
		},
		// 提交
		async submitForm() {
			uni.showLoading({
				title: '保存中',
				mask: true
			});
			this.disabledBtu = true;
			this.markText = this.refreshMark();
			this.formData.personId = this.patient.id;
			this.formData.idNum = this.patient.idNum;
			this.formData.screenTime = this.markText[0];
			this.formData.screenTime = new Date(this.markText[0]).getTime();
			this.formData.updateTime = this.markText[0] + ' ' + this.markText[1];
			// this.formData.creator=user.id
			this.formData.updater = user.id;

			if (this.stateFlag == '新增') {
				this.formData.creator = user.id;
				this.formData.createTime = this.markText[0] + ' ' + this.markText[1];
			}
			if (this.check()) {
			// 校验通过,上传图片
			let imgForm = {
			  screenId: this.patient.screenId,
			  screenTime: this.formData.screenTime,
			  screenOrder: 0,
			  idNum: this.patient.idNum,
			  screenPoint: this.patient.screenPoint,
			  personId: this.formData.personId,
			  imageType: 2,
			  year: this.patient.year,
			  screenType: uni.$screenType
			};
			ctApi.getCreateOrder({
			  idNum: this.patient.idNum,
			  screenType: uni.$screenType,
			  year: this.patient.year
			})
            .then(async (res) => {
				console.log(res);
              imgForm.screenOrder = res.data
              // 判断是否以http开头，以http开头时不需要上传，否则需要上传
			  console.log(this.formData);
              if (this.formData.computedTomography) {
				  console.log("图片上传");
                let data = await this.uploadFilePromise(imgForm, this.formData.computedTomography);
                if (data) {
                  this.formData.computedTomography = data.data;
                  let time = this.refreshMark();
                  this.formData.photoTime = new Date(time[0] + ' ' + time[1]).getTime();
                }
              }
              if (this.formData.doctorSignature) {
				  console.log("签名上传");
                let signForm = Object.assign({}, imgForm);
                signForm.imageType = 11;
                this.uploadFilePromise(signForm, this.formData.doctorSignature).then((res) => {
                  this.formData.doctorSignature = res.data;
                })
              }
			  console.log(123);
			  setTimeout(()=>{
				  if (this.stateFlag == '新增') {
                  ctApi.createTransaction(this.formData)
				  setTimeout(()=>{
					  uni.hideLoading();
					  this.back()
				  },1500)
                  
                } else {
					console.log(222);
                  // console.log("updateData=",data)
                  ctApi.update(this.formData);
                  // await updateImg(imgForm)
                  this.disabledBtu = false;
                  this.back();
                }
			  },500)
              
            })
      }
		},
		back() {
			uni.navigateBack({
				delta: 1
			});
		},
	}
};
</script>

<style lang="scss" scoped>
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
		width: 720px;
		background-color: #fff;
		padding: 10px 20px 20px 20px;
		.bom-t {
			display: flex;
			align-items: center;
			.bom-ipt {
				display: flex;
				align-items: center;
				justify-content: center;
				input {
					height: 30px;
					margin-left: 30px;
					padding: 2px;
					border: 1px solid rgba(204, 204, 204, 1);
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
		padding: 10px 20px 20px 20px;
		width: 390px;
		background-color: #fff;
		margin-left: 10px;
		.custom-style {
			display: flex;
			flex-direction: column;
			align-items: center;
			justify-content: center;
			width: 110px;
			height: 110px;
			border: 1px dashed rgba(204, 204, 204, 1);
			color: rgba(36, 93, 209, 1);
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
	margin: 40px 0;
	background: rgba(36, 93, 209, 1);
	color: #fff;
}
.sign {
	width: 80vw;
	height: 90vh;
}
</style>
