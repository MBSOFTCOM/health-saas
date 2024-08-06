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
					<view class="text-top">采集编号</view>
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
					<view class="text-top">采集次序</view>
					<view class="text-bom" style="color: rgba(212, 48, 48, 1)">
						<span v-if="orderAndTime.order">第 {{ orderAndTime.order }} 次</span>
						<span v-if="!orderAndTime.order"></span>
					</view>
				</view>
				<view class="main-text" style="margin-left: 315px; margin-top: -5px">
					<view class="text-top">采集时间</view>
					<view class="text-bom" v-if="orderAndTime.time">
						{{ orderAndTime.time.split(' ')[0] }}
					</view>
				</view>
			</view>
		</view>

		<view class="main-bottom">
			<view class="bom-t">
				<view style="width: 95px;margin-top: 5px;">选择症状</view>
				<view class="bom-mup" v-if="this.isNewStudent == 0" style="width: 70%">
					<uni-data-checkbox multiple v-model="checkbox" :localdata="items"></uni-data-checkbox>
				</view>
				<view class="bom-stu" v-if="this.isNewStudent == 1">
					<uni-data-checkbox multiple v-model="checkbox" :localdata="itemStudent"></uni-data-checkbox>
				</view>
			</view>
      <view style="width: 95px;margin-top: 5px;">2年内是否有与结核病患者的接触史:</view>
      <up-radio-group
          v-model="contacted"
          placement="column">
        <up-radio
            :customStyle="{marginBottom: '8px'}"
            v-for="(item, index) in radiolist1"
            :key="index"
            :label="item.name"
            :name="item.value"
            @change="radioChange"
        />
      </up-radio-group>
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
		<view>
			<u-popup :show="show" @close="close" mode="center" @open="open" :closeOnClickOverlay="false">
				<view class="sign">
					<sp-sign-board
						v-if="show"
						ref="signBoardRef"
						bgColor="#ffffff"
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
			<up-button
				class="cur-add"
				style="width: 25%; margin-top: 80rpx"
				type="primary"
				text="保存"
				@click="newAdd"
			></up-button>
		</view>
	</view>
</template>

<script>
import {
	getGather,
	dbName,
	tbScreenCollect,
	getCollect,
	tbScreenSum,
	getCollectToId,
	getCollectOen,
	updateCollect,
	tbScreenPerson,
	tbScreenImages
} from '@/utils/sqlite';
import * as PersonApi from '@/api/screen/person'
import dbUtils from '@/uni_modules/zjy-sqlite-manage/components/zjy-sqlite-manage/dbUtils';
import { updateOne } from '@/utils/screenSum.js';
import ScreenImages from '@/utils/screenImages.js';
import { commitTransaction, openTransaction, rollbackTransaction } from '@/utils/sqlite';
import { login } from '@/api/login';
export default {
	data() {
		return {
			nav: [],
			images: [], // 存储所有图片路径
			signBase64: '',
			signTempimg: '',
			show: false,
			markText: '',
			fileList1: [],
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
			formData: {
				doctorSignature: null
			},
			//存储次序和采集时间
			orderAndTime: {
				order: null,
				time: null
			},
			//采集症状
			checkbox: [],
			items: [
				{
					text: '咳嗽、咳痰(超过一周)',
					value: 1
				},
				{
					text: '血痰或咯血',
					value: 2
				},
				{
					text: '发热',
					value: 3
				},
				{
					text: '胸痛',
					value: 4
				},
				{
					text: '夜间盗汗',
					value: 5
				},
				{
					text: '食欲不振',
					value: 6
				},
				{
					text: '乏力',
					value: 7
				},
				{
					text: '体重减轻(超过6斤)',
					value: 8
				},
				{
					text: '有无卡痕',
					value: 9
				}
			],
			itemStudent: [
				{
					text: '咳嗽、咳痰(不小于2周)',
					value: 1
				},
				{
					text: '痰中带血或咯血',
					value: 2
				},
				{
					text: '反复发热2周以上',
					value: 3
				},
				{
					text: '淋巴结肿大',
					value: 4
				}
			],
			contacted:null,
			radiolist1:[{name:'是',value: 1},{name:'否',value:0}],
			isNewStudent: null
		};
	},
	onLoad(e) {
		this.isNewStudent = e.isNewStudent;
		this.patient = e;
		// console.log("e",e);
		this.refreshMark();
		//生成次序和时间
		this.newOrderAndTime();
		PersonApi.getPersonByIdNum(this.patient.idNum).then((res)=>{
			let data=res[0]
			this.patient.tel=data.tel
			this.patient.schoolName=data.schoolOrTemple
			this.patient.classroom=data.classroom
		})
		//修改初始数据
		if (e.isNew == '0') {
			uni.setNavigationBarTitle({
				title: '修改采集症状'
			});
			getCollectOen(e.id, e.order, parseInt(e.year), uni.$screenType).then((res) => {
				const str = res[0].outcome.toString();
				this.contacted=res[0].contacted
				this.checkbox = str.split('').map(Number);
				this.formData.doctorSignature = res[0].doctorSignature;
			});
		}
		this.getNavItems(uni.$screenType, e.isNew);
	},

	methods: {
		radioChange(e){
		  // console.log(e)
		  // console.log(this.contacted)
		  this.contacted=e
		},
		getNavItems(screenType, isNew) {
			const screenNames = {
				1: '常规筛查',
				2: '新生入学筛查',
				3: '应急筛查'
			};
			let collectSymptomText = '修改采集症状';
			if (isNew == 1) {
				collectSymptomText = '采集症状';
			}
			this.nav = [
				{ value: screenNames[screenType] },
				{ value: '采集组' },
				{ value: collectSymptomText, isActive: true }
			];

			return this.nav;
		},

		// 删除图片
		deletePic(event) {
			this[`fileList${event.name}`].splice(event.index, 1);
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
				const result = await this.uploadFilePromise(lists[i].url);
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
		uploadFilePromise(url) {
			return new Promise((resolve, reject) => {
				let a = uni.uploadFile({
					url: 'http://192.168.2.21:7001/upload', // 仅为示例，非真实的接口地址
					filePath: url,
					name: 'file',
					formData: {
						user: 'test'
					},
					success: (res) => {
						setTimeout(() => {
							resolve(res.data.data);
						}, 1000);
					}
				});
			});
		},
		open() {},
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
				await this.savePhoto(path, 2);
				// console.log("url=", url)
				// console.log('==== path :',  path);
				// 之后取消监听，防止重复监听
				uni.$off('getSignImg');
			});
		},
		async savePhoto(tempFilePath, type) {
			uni.saveFile({
				tempFilePath: tempFilePath, // 需要保存的文件的临时路径
				success: async (res) => {
					// console.log("res=",res)
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
		refreshMark() {
			const currentDate = new Date();
			const year = currentDate.getFullYear();
			const month = String(currentDate.getMonth() + 1).padStart(2, '0');
			const day = String(currentDate.getDate()).padStart(2, '0');
			const hours = String(currentDate.getHours()).padStart(2, '0');
			const minutes = String(currentDate.getMinutes()).padStart(2, '0');
			const seconds = String(currentDate.getSeconds()).padStart(2, '0');
			this.markText = [`${year}-${month}-${day}`, `${hours}:${minutes}:${seconds}`];
		},
		firstTouchStart() {
			this.refreshMark();
			// 手动调用组件内绘制水印方法重新绘制
			this.$refs.signBoardRef.drawMark(this.markText);
		},
		reset() {
			this.refreshMark();
		},
		event() {
			this.show = false;
		},
		back() {
			uni.navigateBack({
				delta: 1
			});
		},
		scanId() {},
		//提交保存
		async newAdd() {
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

			uni.showLoading({
				title: '保存中',
				mask: true
			});

			let outcome = '';
			this.checkbox.forEach((i) => {
				outcome = outcome + i;
			});
			//启动事务
			openTransaction()
				.then(async (r) => {
					// 1 表示新增
					if (this.patient.isNew == '1') {
						//修改摸底人员的筛查状态
						const personData = {
							isScreened: 2
						};
						await dbUtils.updateSQL(dbName, tbScreenPerson, personData, 'id', parseInt(this.patient.id));

						//整理采集组的新增数据
						const collect = {
							idNum:this.patient.idNum,
							screenType: uni.$screenType,
							year: parseInt(this.patient.year),
							screenId: this.patient.screenId,
							personId: this.patient.id,
							outcome: outcome,
							contacted:this.contacted,
							tel:this.patient.tel,
							classroom:this.patient.classroom,
							schoolName:this.patient.schoolName,
							age:this.patient.age,
							doctorSignature: this.formData.doctorSignature,
							screenOrder: this.orderAndTime.order,
							screenTime: this.orderAndTime.time,
							creator: uni.$person.id,
							createTime: this.orderAndTime.time
						};
						
						// console.log(collect);
						//结果·校验
						if (collect.outcome == '' || collect.outcome == null || collect.outcome == undefined) {
							collect.outcome = 0;
							/* this.$modal.msgError('症状不能为空');
            return */
						}
						if(!collect.contacted){
							uni.showToast({
							title: '请选择有无接触史',
							icon: 'none',
							duration: 1000
							})
							return;
						}
						if (
							collect.doctorSignature == '' ||
							collect.doctorSignature == null ||
							collect.doctorSignature == undefined
						) {
							this.$modal.msgError('签名不能为空');
							return;
						}
						//插入采集数据
						dbUtils.addTabItem(dbName, tbScreenCollect, collect);

						//查询是否已经存在该患者的汇总表，如果不存在则插入，如果存在则修改
						let length = await getGather(this.patient.id, this.patient.year, uni.$screenType);

						const collectId = await getCollectToId(this.patient.id);

						//插入离线图片表
						const screenImagesData = {
							personId: this.patient.id,
							screenId: this.patient.screenId,
							type: 8, //八代表采集组签名
							path: this.formData.doctorSignature,
							screenOrder: this.orderAndTime.order,
							screenTime: this.orderAndTime.time,
							screenPoint: uni.$person.screenPoint,
							year: this.patient.year,
							screenType: uni.$screenType
						};

						await dbUtils.addTabItem(dbName, tbScreenImages, screenImagesData);

						//获取汇总表初始插入信息
						const gather = {
							year: parseInt(this.patient.year),
							screenType: uni.$screenType,
							screenId: this.patient.screenId,
							personId: parseInt(this.patient.id),
							collectId: collectId[0].id,
							collectNum: this.orderAndTime.order,
							lastCollectTime: collect.screenTime,
							curFinish: '采集组'
						};

						// console.log("gather",gather);

						//为null不存在
						if (length == null || length.length == 0) {
							// console.log("aaa");
							//插入
							await dbUtils.addTabItem(dbName, tbScreenSum, gather);
						} else {
							await updateOne(gather, this.patient.id, this.patient.year, uni.$screenType);
						}
					} else {
						//修改采集数据
						// console.log(this.patient);
						this.refreshMark();
						const collect = {
							outcome: outcome,
							doctorSignature: this.formData.doctorSignature,
							contacted:this.contacted,
							updateTime: this.markText[0] + ' ' + this.markText[1],
							updater: uni.$person.id
						};

						if (collect.outcome == '' || collect.outcome == null || collect.outcome == undefined) {
							collect.outcome = 0;
							/* this.$modal.msgError('症状不能为空');
            return */
						}
						if(!collect.contacted){
							uni.showToast({
							title: '请选择有无接触史',
							icon: 'none',
							duration: 1000
							})
							return;
						}
						if (
							collect.doctorSignature == '' ||
							collect.doctorSignature == null ||
							collect.doctorSignature == undefined
						) {
							this.$modal.msgError('签名不能为空');
							return;
						}

						await updateCollect(
							collect,
							this.patient.id,
							this.patient.order,
							parseInt(this.patient.year),
							uni.$screenType
						);

						//修改汇总表的
						const gather = {
							curFinish: '采集组'
						};

						await updateOne(gather, this.patient.id, this.patient.year, uni.$screenType);

						//修改离线照片存储信息
						//插入离线图片表
						const setScreenImagesData = {
							path: this.formData.doctorSignature
						};

						await ScreenImages.updateOne(
							setScreenImagesData,
							this.patient.id,
							this.patient.year,
							uni.$screenType,
							this.orderAndTime.order,
							8
						);
					}
					//流程无错误，提交事务
					commitTransaction().then((r) => {
						if (r != 200) {
							throw new Error('提交失败');
						}
						this.$modal.msgSuccess('保存成功');
						uni.hideLoading();
						this.back();
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
		//生成本次新增次序和时间
		async newOrderAndTime() {
			if (this.patient.isNew == '1') {
				const res = await getGather(this.patient.id, this.patient.year, uni.$screenType);
				// console.log(res);
				if (res.length == 0) {
					this.orderAndTime.order = 1;
					this.orderAndTime.time = this.markText[0] + ' ' + this.markText[1];
				} else {
					this.orderAndTime.order = res[0].collectNum + 1;
					this.orderAndTime.time = this.markText[0] + ' ' + this.markText[1];
				}
			} else {
				this.orderAndTime.order = this.patient.order;
				this.orderAndTime.time = this.patient.screenTime;
			}

			// console.log(this.orderAndTime);
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
	background-color: #fff;
	font-size: 18px;
	padding: 20px;
	.bom-t {
		display: flex;
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
	margin-top: 45px;
	background: rgba(36, 93, 209, 1);
}
.sign {
	width: 80vw;
	height: 90vh;
}
</style>
