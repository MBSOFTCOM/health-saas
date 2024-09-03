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
				<view class="bom-mup" style="width: 70%">
					<uni-data-checkbox multiple v-model="checkbox" :localdata="items" disabled></uni-data-checkbox>
				</view>
			</view>
			<view style="display: flex;">
				<view style="width: 590rpx;margin-top: 10rpx;">2年内是否有与结核病患者的接触史:</view>
				<span style="display: flex;">
					<up-radio-group
						v-model="contacted"
						placement="row"
						>
						<up-radio
							:customStyle="{marginRight: '12px'}"
							v-for="(item, index) in radiolist1"
							labelSize="21px"
							size="29px"
							:key="index"
							:label="item.name"
							:name="item.value"
							@change="radioChange"
						/>
					</up-radio-group>
				</span>
			</view>
			<view class="bom-m">
				<view>医生签名</view>
				<view class="sign-imgbg">
					<view style="margin-left: -50px">
						<image class="sign-image" :src="signBase64" mode="widthFix"></image>
					</view>
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
						@firstTouchStart="firstTouchStart"/>
				</view>
			</u-popup>
		</view>
	</view>
</template>

<script>
import { getGather,dbName,tbScreenCollect,getCollect, tbScreenSum,getCollectToId,getCollectOen,updateCollect } from '@/utils/sqlite';
import dbUtils from '../../../uni_modules/zjy-sqlite-manage/components/zjy-sqlite-manage/dbUtils';
export default {
	data() {
		return {
			nav:[],
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
			//存储次序和采集时间
			orderAndTime: {
				order: null,
				time: null
			},
			//采集症状
			checkbox:[],
			items: [
				{
					text: '咳嗽、咳痰(超过两周)',
					value: 1
				},
				{
					text: '血痰或咯血',
					value: 2
				},
				{
					text: '乏力、盗汗',
					value: 3
				},
				{
					text: '体重减轻（超过6斤）',
					value: 4
				},
				{
					text: '发热',
					value: 5
				},
				{
					text: '食欲不振',
					value: 6
				},
				{
					text: '胸痛',
					value: 7
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
			isNewStudent:null
		};
	},
	onLoad(e) {
		this.isNewStudent = e.isNewStudent
		this.patient = e
		this.getNavItems(uni.$screenType);
		this.refreshMark()
		//生成次序和时间
		this.newOrderAndTime()
		//获取症状数据
		getCollectOen(e.id,e.order,parseInt(e.year),uni.$screenType).then(res=>{
			// console.log(res);
			const str =res[0].outcome.toString()
			this.contacted=res[0].contacted
			if (str){
				this.checkbox=str.split("").map(Number)
			}
			this.signBase64 = res[0].doctorSignature
		})
		
	},

	methods: {
		getNavItems(screenType) {
			const screenNames = {
				1: '常规筛查',
				2: '新生入学筛查',
				3: '应急筛查'
			};
			this.nav = [
				{ value: screenNames[screenType] },
				{ value: '采集组' },
				{ value: '采集详情', isActive: true }
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
			uni.$on('getSignImg', ({ base64, path }) => {
				this.signBase64 = base64;
				this.signTempimg = path;
				// console.log('==== base64 path :', base64, path);
				// 之后取消监听，防止重复监听
				uni.$off('getSignImg');
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
		//获取本次采集的时间和次序
		 newOrderAndTime(){
			this.orderAndTime.order = this.patient.order
			this.orderAndTime.time = this.patient.screenTime
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
		.sign-imgbg {
			position: relative;
			display: flex;
			align-items: center;
			justify-content: center;
			background: rgba(245, 245, 245, 1);
			border: 1px solid rgba(204, 204, 204, 1);
			width: 90%;
			padding: 10px;
			border-radius: 5px;
			margin-left: 30px;
			.sign-image {
				width: 60px;
				transform: scaleX(3) scaleY(2.1) rotate(90deg);
			}
		}
	}
}
</style>
