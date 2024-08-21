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
			<view class="main-top-1">
				<view class="main-text">
					<view class="text-top">注射次序</view>
					<view class="text-bom" style="color: rgba(212, 48, 48, 1)">
						<span v-if="patient.order">第 {{ patient.order }} 次</span>
						<span v-if="!patient.order"></span>
					</view>
				</view>
				<view class="main-text" style="margin-left: 315px; margin-top: -5px">
					<view class="text-top">注射时间</view>
					<view class="text-bom">
						{{ patient.screenTime }}
					</view>
				</view>
			</view>
		</view>
		<view class="main-bottom">
			<up-row>
				<up-col span="7">
					<view class="bom-t">
						<view style="width: 250rpx">皮肤反应类型</view>
						<view class="bom-mup">
							<u-radio-group v-model="FormData.outcome" placement="row" @change="changeOutcome">
								<u-radio
									:customStyle="{ marginRight: '5vw' }"
									v-for="(item, index) in outcomes"
									:key="index"
									:label="item.name"
									:name="item.value"
									labelSize="21px"
									iconSize="27px"
									size="29px"
								></u-radio>
							</u-radio-group>
						</view>
					</view>
					<view class="bom-t" style="margin: 15px 0" v-if="FormData.outcome">
						<view style="width: 250rpx">局部症状</view>
						<view class="bom-mup">
							<uni-data-checkbox multiple v-model="crowdArr" :localdata="items"></uni-data-checkbox>
						</view>
					</view>
				</up-col>
			</up-row>

			<up-row gutter="10">
				<view>
					<view class="img-left">
						<view style="margin: 0 13rpx">图像采集</view>
						<view style="margin-left: 50rpx;">
							<image
								style="width: 280rpx; height: 280rpx"
								v-if="FormData.actualPhoto"
								:src="FormData.actualPhoto"
								mode="aspectFit"
							/>
							<view
								:class="{
									'custom-style': !this.FormData.actualPhoto,
									'custom-style2': this.FormData.actualPhoto
								}"
								@click="chooseImg()"
							>
								<up-icon
									name="plus"
									color="rgba(36, 93, 209, 1)"
									:size="this.FormData.actualPhoto ? 20 : 30"
								></up-icon>
								<span v-if="!FormData.actualPhoto">采集照片</span>
								<span v-else>修改照片</span>
							</view>
						</view>
					</view>
				</view>
				<!-- 硬结 -->
				<view class="img-content">
					<view class="induration">
						<view>硬结</view>
						<view class="bom-t" style="margin: 20rpx 0">
							<view>横经(mm)</view>
							<input
								class="inpt"
								v-model="FormData.transverseDiameter"
								confirm-type="search"
								placeholder="请填写横经"
								@blur="numberCheck(FormData.transverseDiameter, 1)"
							/>
						</view>
						<view class="bom-t">
							<view>纵经(mm)</view>
							<input
								class="inpt"
								v-model="FormData.longitudinalDiameter"
								confirm-type="search"
								placeholder="请填写纵经"
								@blur="numberCheck(FormData.longitudinalDiameter, 2)"
							/>
						</view>
					</view>
					<view>
						<image
							style="width: 280rpx; height: 280rpx"
							v-if="FormData.scleromaPhoto"
							:src="FormData.scleromaPhoto"
							mode="aspectFit"
						/>
						<view
							:class="{
								'custom-style': !this.FormData.scleromaPhoto,
								'custom-style2': this.FormData.scleromaPhoto
							}"
							@click="editImage(0, null)"
						>
							<up-icon
								name="plus"
								color="rgba(36, 93, 209, 1)"
								:size="this.FormData.scleromaPhoto ? 20 : 30"
							></up-icon>
							<span v-if="!FormData.scleromaPhoto">绘制范围</span>
							<span v-else>修改绘制范围</span>
						</view>
					</view>
				</view>
				<!-- 红晕 -->
				<view class="img-content">
					<view class="induration">
						<view>红晕</view>
						<view class="bom-t" style="margin: 20rpx 0">
							<view>横经(mm)</view>
							<input
								class="inpt"
								v-model="FormData.blushTransverseDiameter"
								confirm-type="search"
								placeholder="请填写横经"
								@blur="numberCheck(FormData.blushTransverseDiameter, 1)"
							/>
						</view>
						<view class="bom-t">
							<view>纵经(mm)</view>
							<input
								class="inpt"
								v-model="FormData.blushLongitudinalDiameter"
								confirm-type="search"
								placeholder="请填写纵经"
								@blur="numberCheck(FormData.blushLongitudinalDiameter, 2)"
							/>
						</view>
					</view>
					<view>
						<image
							style="width: 280rpx; height: 280rpx"
							v-if="FormData.blushPhoto"
							:src="FormData.blushPhoto"
							mode="aspectFit"
						/>
						<view
							:class="{
								'custom-style': !this.FormData.blushPhoto,
								'custom-style2': this.FormData.blushPhoto
							}"
							@click="editImage(1,null)"
						>
							<up-icon
								name="plus"
								color="rgba(36, 93, 209, 1)"
								:size="this.FormData.blushPhoto ? 20 : 30"
							></up-icon>
							<span v-if="!FormData.blushPhoto">绘制范围</span>
							<span v-else>修改绘制范围</span>
						</view>
					</view>
				</view>
			</up-row>

			<view class="bom-m">
				<view style="width: 250rpx">医生签名</view>
				<view class="sign-bg" v-if="!FormData.doctorSignature" @click="onSign">
					<view class="sign-text">
						<image class="sign-img" src="../../../static/images/tb/sign.png" mode=""></image>
						<span>点击签名</span>
					</view>
				</view>
				<view class="sign-imgbg" v-if="FormData.doctorSignature">
					<view style="margin-left: -50px">
						<image class="sign-image" :src="FormData.doctorSignature" mode="widthFix"></image>
					</view>
					<up-button @click="onSign" class="custom-reset" :plain="true" text="重写"></up-button>
				</view>
			</view>
		</view>
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
		<view style="display: flex; justify-content: center">
			<up-button class="cur-add" text="保存" @click="newAdd"></up-button>
		</view>
	</view>
</template>

<script>
import {
	dbName,
	tbScreenCollect,
	getCollect,
	tbScreenSum,
	getCollectToId,
	getCollectOen,
	updateCollect
} from '@/utils/sqlite';
const imageEditor = uni.requireNativePlugin('Ba-ImageEditor');
import dbUtils from '../../../uni_modules/zjy-sqlite-manage/components/zjy-sqlite-manage/dbUtils';
import {
	getPpdList,
	getPpdBypersonIdToOrder,
	updateBypersonIdAndScreenOrder,
	getBypersonIdAndScreenOrder,
	getBypersonIdAndScreenOrderOne
} from '@/utils/ppd.js';
import { getTimeStamp } from '@/utils/common';
import { updateOne } from '/utils/screenSum.js';
import ScreenImages, { tbScreenImages } from '../../../utils/screenImages.js';
import { openTransaction } from '../../../utils/sqlite';
export default {
	data() {
		return {
			nav: [],
			//签名
			images: [],
			crowdArr: [],
			//传过来的患者信息
			patient: {
				id: '',
				name: '',
				age: '',
				idNum: '',
				screenId: '',
				order: '',
				screenTime: '',
				isNew: true
			},
			//采集症状
			checkbox: [],
			items: [
				{
					text: '双圈',
					value: 1
				},
				{
					text: '水泡',
					value: 2
				},
				{
					text: '坏死',
					value: 3
				},
				{
					text: '淋巴管炎',
					value: 4
				}
			],
			// 数据
			symptoms: [
				{
					name: '是',
					value: 1
				},
				{
					name: '否',
					value: 0
				}
			],
			// 结果
			outcomes: [
				{
					name: '阳性',
					value: 1
				},
				{
					name: '阴性',
					value: 0
				}
			],
			//提交数据
			FormData: {},
			//签名弹窗
			show: false,
			signBase64: '',
			//存储生成的时间
			markText: '',
			scleromaPhoto: null, // 硬结编辑图
			blushPhoto: null, // 红晕编辑图
			actualPhoto: null, // 实拍图
			doctorSignature: null // 签名
		};
	},
	onLoad(e) {
		this.patient = e;
		// console.log(this.patient);
		//isNew 为0表示修改
		if (e.isNew == 0) {
			getBypersonIdAndScreenOrderOne(e.order, e.id).then((res) => {
				// console.log(res);
				this.FormData = res[0];
				this.FormData.bleb = res[0].bleb.toString();
				this.crowdArr = this.FormData.bleb.split('').map(Number);
			});
		}
		this.getNavItems(uni.$screenType);
	},

	methods: {
		getNavItems(screenType) {
			const screenNames = {
				1: '常规筛查',
				2: '新生入学筛查',
				3: '应急筛查'
			};
			this.nav = [{ value: screenNames[screenType] }, { value: 'PPD组' }, { value: '提交结果', isActive: true }];

			return this.nav;
		},
		//关闭弹窗
		close() {
			this.show = false;
			this.$refs.signBoardRef.$destroy();
		},
		onSign() {
			this.refreshMark();
			this.show = true;
			uni.$on('getSignImg', async ({ base64, path }) => {
				this.signBase64 = base64;
				this.signTempimg = path;
				// console.log('==== base64 path :', base64, path);
				let url = 'ccc';
				await this.savePhoto(path, 1);
				// console.log('==== path :',  path);
				// 之后取消监听，防止重复监听
				uni.$off('getSignImg');
			});
		},
		/**
		 * @param {Object} tempFilePath
		 * @param {Object} type 0-实拍 1-签名
		 */
		async savePhoto(tempFilePath, type) {
			uni.saveFile({
				tempFilePath: tempFilePath, // 需要保存的文件的临时路径
				success: async (res) => {
					const savedFilePath = res.savedFilePath;
					if (type) {
						this.FormData.doctorSignature = savedFilePath;
						this.doctorSignature = savedFilePath;
					} else {
						this.FormData.actualPhoto = savedFilePath;
						this.actualPhoto = savedFilePath;
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
		reset() {
			this.refreshMark();
		},
		event() {
			this.show = false;
		},
		//数字校验
		numberCheck(i, count) {
			const regex = /^[1-9][0-9]{0,2}$/;
			if (!regex.test(i)) {
				if (count == 1) {
					this.FormData.transverseDiameter = '';
				} else {
					this.FormData.longitudinalDiameter = '';
				}
				uni.$u.toast('请输入不超过3位的整数且第一位不能位0');
				return;
			}
		},
		scanId() {},
		/**
		 * @param {Object} type 编辑图片的类型 0-硬结 1-红晕
		 * @param {Object} saveName 保存的文件名
		 */
		editImage(type, saveName) {
			saveName = getTimeStamp(null);

			imageEditor.selectImage((ret) => {
				if (ret.outputPath) {
					imageEditor.imageEdit(
						{
							isShowSticker: false, //是否展示贴图功能，默认为true
							path: ret.outputPath, //原始图片路径
							outputPath: `/storage/emulated/0/Pictures/${saveName}.jpg` //保存图片路径
						},
						(res) => {
							if (res.outputPath && res.isImageEdit) {
								if (type) {
									// 红晕
									this.blushPhoto = res.outputPath;
									this.FormData.blushPhoto = res.outputPath;
								} else {
									this.scleromaPhoto = res.outputPath;
									this.FormData.scleromaPhoto = res.outputPath;
								}
							}
						}
					);
				}
			});
		},
		changeOutcome() {
			if (this.FormData.outcome == 0) {
				this.FormData.bleb = null;
				this.crowdArr = [];
			}
		},
		chooseImg() {
			uni.chooseImage({
				count: 1, // 最多可以选择的图片张数
				sourceType: ['camera', 'album'], // 指定拍照模式
				success: (res) => {
					const tempFilePaths = res.tempFilePaths;
					this.savePhoto(tempFilePaths[0], 0); // 保存拍摄的照片
				}
			});
		},
		//提交保存
		async newAdd() {
			// console.log(this.FormData);
			// return
			if (Object.entries(uni.$person).length === 0) {
				uni.showModal({
					title: '提示',
					content: '登录信息已过期，是否重新登陆？',
					success: (res) => {
						if (res.confirm) {
							uni.reLaunch({
								url: '/pages/login'
							});
						} else if (res.cancel) {
							uni.showToast({
								title: '已取消',
								icon: 'none'
							});
						}
					}
				});
				return;
			}


			//症状
			this.FormData.bleb = '';
			this.crowdArr.forEach((i) => {
				// console.log(i);
				this.FormData.bleb = this.FormData.bleb + i.toString();
			});

			this.FormData.updater = uni.$person.id;
			this.refreshMark();
			this.FormData.updateTime = this.markText;

			//非空校验
			// console.log(this.FormData);
			const data = {
				transverseDiameter: this.FormData.transverseDiameter, //横泾
				longitudinalDiameter: this.FormData.longitudinalDiameter, //纵径
				blushTransverseDiameter: this.FormData.blushTransverseDiameter,
				blushLongitudinalDiameter: this.FormData.blushLongitudinalDiameter,
				actualPhoto: this.FormData.actualPhoto,
				blushPhoto: this.FormData.blushPhoto,
				scleromaPhoto: this.FormData.scleromaPhoto,
				outcome: this.FormData.outcome, //结果
				bleb: this.FormData.bleb, //症状
				doctorSignature: this.FormData.doctorSignature, //签名
				statusFlag:this.FormData.statusFlag??2,
				updater: this.FormData.updater, //修改者
				updateTime: this.FormData.updateTime //修改时间
			};
			// console.log(data);
			const isNull = this.ifNull(data);
			if (!isNull) {
				uni.$u.toast('表单数据存在空,请检查表单,并填写完整!');
				return;
			}

			openTransaction()
				.then(async (r) => {
					//提交信息
					await updateBypersonIdAndScreenOrder(data, this.patient.order, this.patient.id);

					//补充汇总表数据
					const gather = {
						curFinish: 'ppd组'
					};

					await updateOne(gather, this.patient.id, uni.$person.year, uni.$screenType);

					//补充离线图片表图片信息
					const setScreenImagesData = {
						path: this.FormData.doctorSignature
					};
					// console.log(this.patient);
					// 签名
					await ScreenImages.deleteOne(
						this.patient.idNum,
						uni.$person.year,
						uni.$screenType,
						this.patient.order,
						9
					);
					let imageData = {
						idNum: this.patient.idNum,
						screenType: uni.$screenType,
						year: uni.$person.year,
						screenOrder: this.patient.order,
						path: this.FormData.doctorSignature,
						personId: this.patient.id,
						screenId: this.patient.screenId,
						type: 9
					};
					// console.log(imageData);
					await dbUtils.addTabItem(dbName, tbScreenImages, imageData);
					// 实拍
					await ScreenImages.deleteOne(
						this.patient.idNum,
						uni.$person.year,
						uni.$screenType,
						this.patient.order,
						16
					);
					imageData.type = 16;
					imageData.path = this.FormData.actualPhoto;
					await dbUtils.addTabItem(dbName, tbScreenImages, imageData);
					// 红晕
					await ScreenImages.deleteOne(
						this.patient.idNum,
						uni.$person.year,
						uni.$screenType,
						this.patient.order,
						18
					);
					imageData.type = 18;
					imageData.path = this.FormData.blushPhoto;
					await dbUtils.addTabItem(dbName, tbScreenImages, imageData);
					// 硬结
					await ScreenImages.deleteOne(
						this.patient.idNum,
						uni.$person.year,
						uni.$screenType,
						this.patient.order,
						17
					);
					imageData.type = 17;
					imageData.path = this.FormData.scleromaPhoto;
					await dbUtils.addTabItem(dbName, tbScreenImages, imageData);

					//返回上一页
					uni.navigateBack({
						delta: 1 // 返回的页面数，如果delta是1，表示返回上一个页面
					});

					//流程无错误，提交事务
					commitTransaction().then((r) => {
						if (r != 200) {
							throw new Error('提交失败');
						}
						this.$modal.msgSuccess('保存成功');
						uni.hideLoading();
					});
				})
				.catch((e) => {
					// 存在问题，回滚
					rollbackTransaction().then((res) => {
						if (res != 200) {
							uni.hideLoading();
							this.$modal.msgError('更新失败,请重试');
						}
					});
				});
		},
		//对提交数据非空校验进行
		ifNull(obj) {
			for (let key in obj) {
				// console.log(key);
				if (obj.hasOwnProperty(key)) {
					if (obj[key] === null || obj[key] === undefined) {
						return false;
					}
				}
			}
			return true;
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
		open() {}
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
.custom-style {
	padding: 5px;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	width: 280rpx;
	height: 280rpx;
	border: 1px dashed rgba(204, 204, 204, 1);
	color: rgba(36, 93, 209, 1);
	background-color: #fff;
}
.custom-style2 {
	padding: 5px;
	display: flex;
	align-items: center;
	justify-content: center;
	width: 280rpx;
	height: 80rpx;
	border: 1px dashed rgba(204, 204, 204, 1);
	color: rgba(36, 93, 209, 1);
	background-color: #fff;
}
.main-bottom {
	background-color: #fff;
	font-size: 18px;
	padding: 20px;
	.bom-t {
		display: flex;
		align-items: center;
		.bom-ipt {
			display: flex;
			align-items: center;
			justify-content: center;
			input {
				height: 30px;
				margin-left: 23px;
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
	.bom-m {
		margin-top: 20px;
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
.img-left {
	display: flex;
}
.img-content {
	margin-left: 20rpx;
	display: flex;
	.induration {
		display: flex;
		flex-direction: column;
		margin: 0 30rpx 0 100rpx;

		.inpt {
			border: 1rpx silver solid;
			height: 60rpx;
			border-radius: 5rpx;
			width: 200rpx;
			margin-left: 20rpx;
			margin-top: 10rpx;
		}
	}
}
</style>