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
						{{ patientInfo.screenId }}
					</view>
				</view>
				<view class="main-text" style="margin: 0 150px">
					<view class="text-top">姓名</view>
					<view class="text-bom">
						{{ patientInfo.name }}
					</view>
				</view>
				<view class="main-text" style="margin-right: 150px">
					<view class="text-top">年龄</view>
					<view class="text-bom">
						{{ patientInfo.age }}
					</view>
				</view>
				<view class="main-text">
					<view class="text-top">身份证</view>
					<view class="text-bom">
						{{ patientInfo.idNum }}
					</view>
				</view>
			</view>
			<view class="main-top-1" v-if="this.optionType == 2">
				<view class="main-text">
					<view class="text-top">采集次序</view>
					<view class="text-bom" style="color: rgba(212, 48, 48, 1)">
						<span>第{{ patientInfo.orderVal }}次</span>
					</view>
				</view>
				<view class="main-text" style="margin-left: 315px; margin-top: -5px">
					<view class="text-top">采集时间</view>
					<view class="text-bom">
						{{ patientInfo.orderTime.filter(item => item.value === patientInfo.orderVal)[0].text.replace(`第${patientInfo.orderVal}次/`, '') }}
					</view>
				</view>
			</view>
		</view>
		<view class="main-bottom">
			<view class="bom-left">
				<view class="bom-t">
					<view class="bom-ipt">
						<view>检测结果</view>
						<view class="bom-mup">
							<u-radio-group v-model="formData.test_result" placement="row">
								<u-radio
									style="margin-right: 20px;"
									v-for="(item, index) in ecg_test_result"
									:key="index + 'a'"
									:label="item.name"
									:name="item.name"
									labelSize="20px"
									iconSize="23px"
									size="25px"
								></u-radio>
							</u-radio-group>
						</view>
					</view>
				</view>
				<view class="bom-t" style="margin: 15px 0 5px 0; align-items: flex-start">
					<view style="margin-right: 35px;">备注</view>
					<view class="bom-mup">
						<uni-easyinput
							style="width: 400px"
							type="textarea"
							v-model="remark"
							placeholder="请输入备注"
						></uni-easyinput>
					</view>
				</view>
			</view>
			<view class="bom-right">
				<view style="padding-bottom: 20px; font-size: 18px">心电图照片</view>
				<view>
					<u-upload
						:fileList="fileList1"
						@afterRead="afterRead"
						@delete="deletePic"
						width='120'
						height='120'
						name="1"
						multiple
						:maxCount="1"
						:previewFullImage="true"
					></u-upload>
				</view>
			</view>
		</view>
		<view class="sg-bom">
			<view class="bom-m">
				<view>医生签名</view>
				<view class="sign-bg" v-if="!formData.doctorSignature" @click="onSign">
					<view class="sign-text">
						<image class="sign-img" src="../../../static/images/tb/sign.png" mode=""></image>
						<span>点击签名</span>
					</view>
				</view>
				<view class="sign-imgbg" v-if="formData.doctorSignature">
					<view style="margin-left: -50px">
						<image class="sign-image" :src="formData.doctorSignature" mode="widthFix"></image>
					</view>
					<up-button @click="onSign" class="custom-reset" :plain="true" text="重写"></up-button>
				</view>
			</view>
		</view>
		<view style="display: flex; justify-content: center">
			<up-button class="cur-add" text="保存" @click="submitForm"></up-button>
		</view>
		<!-- 签字弹出层 -->
		<view>
			<u-popup :show="show" @close="close" mode="center" @open="open" :closeOnClickOverlay="false">
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
	</view>
</template>

<script>
// 复用采集组
import { ethnic } from '@/utils/dict.js';
import * as ecgApi from '@/api/screen/ecg.js'

// sqlite-manage 插件
import dbUtils from '../../../uni_modules/zjy-sqlite-manage/components/zjy-sqlite-manage/dbUtils';
import { dbName, tbScreenImages, tbScreenElectrocardiogram,tbScreenSum } from '@/utils/sqlite';
import {selectOne} from "../../../utils/screenSum";

export default {
	data() {
		return {
			nav: [],
			remark: '',
			formData: {
				test_result: null,
				doctorSignature: null
			},
			ecg_test_result: [
				{
					name: '正常心电图',
					disabled: false
				},
				{
					name: '窦性心率',
					disabled: false
				},
				{
					name: '其他',
					disabled: false
				}
			],
			fileList1: [],
			patientInfo: {},

			// 民族列表
			ethnic: [{ value: 61, text: '汉族' }],
			optionType: 0,
			// 心电图表数据对象
			electrocardiogra: undefined,
			// 签字
			show: false,
			signBase64: '',
			//存储生成的时间
			markText: ''
		};
	},
	onLoad(option) {
		let title = '心电图';
		const item = JSON.parse(decodeURIComponent(option.item));
		console.log(item);
		this.ethnic = ethnic;
		this.patientInfo = item;
		this.optionType = parseInt(option.type);
		if (this.optionType == '1') {
			title = '采集' + title;
		} else {
			title = '修改' + title;
		}
		uni.setNavigationBarTitle({
			title: title
		});
		this.getNavItems(uni.$screenType, this.optionType);

		// 修改心电图信息 数据回显
		if (this.optionType == 2) {
			dbUtils
				.selectDataList(
					dbName,
					tbScreenElectrocardiogram,
					{
						personId: item.id,
						screenOrder: this.patientInfo.orderVal
					},
					'screenOrder',
					'desc'
				)
				.then((res) => {
					if (res && res.length > 0) {
						this.fileList1 = [{ url: res[0].electrocardiogram }];
						this.formData.test_result = res[0].testResult;
						this.formData.doctorSignature = res[0].doctorSignature;
						this.remark = res[0].remark;
						this.electrocardiogra = res[0];
					}
				});
		}
	},
	methods: {
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
				case '1':
					drResult = '采集心电图';
					break;
				case '2':
					drResult = '修改心电图';
					break;
				default:
					drResult = '采集心电图';
			}
			const screenNames = {
				1: '常规筛查',
				2: '新生入学筛查',
				3: '应急筛查'
			};
			this.nav = [{ value: screenNames[screenType] }, { value: '心电图组' }, { value: drResult, isActive: true }];
			return this.nav;
		},
		// 关闭弹窗
		close() {
			this.show = false;
			this.$refs.signBoardRef.$destroy();
		},
		reset() {
			this.refreshMark();
		},
		event() {
			this.show = false;
		},
		// 生成时间
		refreshMark() {
			const currentDate = new Date();
			const year = currentDate.getFullYear();
			const month = String(currentDate.getMonth() + 1).padStart(2, '0');
			const day = String(currentDate.getDate()).padStart(2, '0');
			const hours = String(currentDate.getHours()).padStart(2, '0');
			const minutes = String(currentDate.getMinutes()).padStart(2, '0');
			const seconds = String(currentDate.getSeconds()).padStart(2, '0');
			this.markText = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
		},
		onSign() {
			this.refreshMark();
			this.show = true;

			uni.$on('getSignImg', async ({ base64, path }) => {
				this.signBase64 = base64;
				this.signTempimg = path;

				// 保存图片
				await this.savePhoto(path, 2);

				// 之后取消监听，防止重复监听
				uni.$off('getSignImg');
			});
		},
		async savePhoto(tempFilePath, type) {
			uni.saveFile({
				tempFilePath: tempFilePath, // 需要保存的文件的临时路径
				success: async (res) => {
					console.log('res=', res);
					const savedFilePath = res.savedFilePath;
					this.formData.doctorSignature = savedFilePath;
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
		// 新增图片
		async afterRead(event) {
			// 当设置 mutiple 为 true 时, file 为数组格式，否则为对象格式
			let lists = [].concat(event.file);
			let fileListLen = this[`fileList${event.name}`].length;
			lists.map((item) => {
				this[`fileList${event.name}`].push({
					...item,
					status: 'uploading',
					message: '上传中'
				});
			});
			for (let i = 0; i < lists.length; i++) {
				const result = lists[i].url;
				let item = this[`fileList${event.name}`][fileListLen];
				this[`fileList${event.name}`].splice(
					fileListLen,
					1,
					Object.assign(item, {
						status: 'success',
						message: '',
						url: result
					})
				);
				fileListLen++;
			}
		},
		// 删除图片
		deletePic(event) {
			this[`fileList${event.name}`].splice(event.index, 1);
		},
		submitForm() {
			// 检验表单不为空
			if (!this.fileList1[0] || !this.formData.doctorSignature || !this.formData.test_result) {
				uni.$u.toast('表单数据存在空,请检查表单,并填写完整!');
				return;
			}

			// 保存逻辑
			const now = new Date();
			const currentYear = now.getFullYear();
			let that = this;
			let item = this.patientInfo;

			// 循环选中的图片列表
			for (var i = 0; i < this.fileList1.length; i++) {
				// 保存图片到本地
				uni.saveFile({
					tempFilePath: this.fileList1[i].url,
					success: (saveFileRes) => {
						// 获取到本地文件的路径
						this.localPath = saveFileRes.savedFilePath;

						let date = new Date();
						const Y = date.getFullYear() + '-';
						const M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
						const D = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate()) + ' ';
						const h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
						const m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
						const s = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds();
						let cureentDateFormat = Y + M + D + h + m + s;

						// 获取该筛查人员最新一次的筛查次序
						dbUtils
							.selectDataList(
								dbName,
								tbScreenElectrocardiogram,
								{
									personId: item.id,
                  screenType: uni.$screenType
								},
								'screenOrder',
								'desc'
							)
							.then(async(res) => {
								let screenOrder = 0;
								if (res && res.length > 0) {
									// 最近一次筛查次序
									screenOrder = res[0].screenOrder;
								}

								if (this.optionType == 2 && this.electrocardiogra) {
									// 修改心电图表
									dbUtils.updateSQL(
										dbName,
										tbScreenElectrocardiogram,
										{
											testResult: this.formData.test_result,
											remark: this.remark,
											electrocardiogram: this.localPath,
											doctorSignature: this.formData.doctorSignature
										},
										'id',
										this.electrocardiogra.id
									);
								} else {
									// 此处多次插入操作无法保证事务一致性

									// 给心电图表中插入数据
									dbUtils.addTabItem(dbName, tbScreenElectrocardiogram, {
										screenId: item.screenId,
										personId: item.id,
										doctorSignature: this.formData.doctorSignature,
										electrocardiogram: this.localPath,
										screenOrder: screenOrder + 1,
										testResult: this.formData.test_result,
										remark: this.remark,
										screenTime: cureentDateFormat,
										screenPoint: item.screenPoint,
										createTime: cureentDateFormat,
										year: currentYear,
										screenType: uni.$screenType
									});

									// 新增一条心电图图片数据到离线图片表
									dbUtils.addTabItem(dbName, tbScreenImages, {
										personId: item.id,
										type: 4,
										path: this.localPath,
										screenId: item.screenId,
										screenOrder: screenOrder + 1,
										screenTime: cureentDateFormat,
										screenPoint: item.screenPoint,
										createTime: cureentDateFormat,
										year: currentYear,
										screenType: uni.$screenType
									});

									// 插入一条心电图医生签名数据到离线图片表
									dbUtils.addTabItem(dbName, tbScreenImages, {
										personId: item.id,
										type: 15,
										path: this.formData.doctorSignature,
										screenId: item.screenId,
										screenOrder: screenOrder + 1,
										screenTime: cureentDateFormat,
										screenPoint: item.screenPoint,
										createTime: cureentDateFormat
									});
								}
                let resp=await ecgApi.getLastOne(this.patientInfo.id, uni.$screenType)
                let sumData={
                  screenId: this.patientInfo.screenId,
                  year: this.patientInfo.year,
                  screenType: uni.$screenType,
                  personId: this.patientInfo.id,
                  electrocardiogramNum: resp[0].screenOrder,
                  lastElectrocardiogramTime: resp[0].screenTime,
                  electrocardiogramId: resp[0].id
                }
                // console.log(sumData)
                selectOne({personId: this.patientInfo.id,year:this.patientInfo.year,screenType:uni.$screenType}).then(res=>{
                  // console.log(res)
                  if (res && res.length>=1){
                    dbUtils.updateSQL(dbName,tbScreenSum,sumData,'id',res[0].id)
					            // console.log('up');
                  }else {
                    dbUtils.addTabItem(dbName,tbScreenSum,sumData)
					            // console.log('ins');
                  }
                })
							});
					},
					fail: (err) => {
						uni.showToast({
							title: '上传失败',
							icon: 'none',
							duration: 2000
						});
					}
				});
			}

			uni.showToast({
				title: '保存成功',
				icon: 'none',
				duration: 2000
			});
      setTimeout(()=>{
        uni.navigateBack({
          delta: 1
        })
      },1000)
		},

		// 处理性别
		getSex(sex) {
			if (sex != null) {
				if (sex === 1) {
					return '女';
				} else {
					return '男';
				}
			}
			return '';
		},
		// 处理民族
		getNation(e) {
			const foundItem = this.ethnic.find((i) => i.value === e);
			if (foundItem) {
				return foundItem.text;
			}
			return '';
		},
		isTablet() {
			// 简单的判断逻辑，实际使用时可能需要更精确的判断
			return window.innerWidth >= 768;
		}
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
		width: 760px;
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
		width: 350px;
		background-color: #fff;
		margin-left: 10px;
		.custom-style {
			width: 130px;
			height: 130px;
			border: 1px solid rgba(204, 204, 204, 1);
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
