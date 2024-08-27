<template>
	<view style="padding: 15px 0 0 10px">
		<view>
			<view class="top1">
				<view class="top-left1">
					<yile-breadcrumb
						:nav="nav"
						color="rgba(153, 153, 153, 1)"
						actColor="rgba(36, 93, 209, 1)"
					></yile-breadcrumb>
				</view>
				<view style="margin-right: 18%;" v-if="synchronizeTime">
					上次同步时间:{{synchronizeTime}}
				</view>
				<view class="top-right1" style="margin-right: 10px">
					<up-button
						@click="PadToPc"
						style="margin-left: 10px"
						type="success"
						icon="download"
						iconColor="#fff"
						:plain="true"
						class="custom-sync"
						text="同步"
					></up-button>
				</view>
			</view>
			<view class="top">
				<view class="top-left">
					<span style="font-size: 16px">筛查编号 </span>
					<uni-search-bar
						@confirm="handleQuery"
						:focus="false"
						class="top-search"
						v-model="queryParams.screenId"
						placeholder="请输入筛查编号"
						style="width: 230px"
						cancelButton="none"
						@clear="clearScreenId"
					></uni-search-bar>
					<span style="font-size: 16px">胸片结果 </span>
					<uni-data-select
						style="width: 120px;margin-left: 10px;background-color: #f9f9f9;"
						v-model="queryParams.outcome"
						:localdata="dataRadio"
					></uni-data-select>
				</view>
				<view class="top-right" style="margin-right: 10px">
					<up-button @click="handleQuery" :plain="true" class="custom-search" text="搜索"></up-button>
					<up-button
						@click="reset"
						style="margin-left: 10px"
						class="custom-reset"
						:plain="true"
						text="重置"
					></up-button>
				</view>
			</view>
		</view>

		<view class="uni-container">
			<uni-table
				ref="table"
				:loading="loading"
				stripe
				emptyText="暂无更多数据"
				type="selection"
				@selection-change="selectionChange"
			>
				<uni-tr>
					<uni-th width="5%" align="center">序号</uni-th>
					<uni-th width="5%" align="center">筛查编号</uni-th>
					<uni-th width="5%" align="center">工作年度</uni-th>
					<uni-th width="5%" align="center">筛查类型</uni-th>
					<uni-th width="8%" align="center">患者姓名</uni-th>
					<uni-th width="5%" align="center">胸片编号</uni-th>
					<uni-th width="5%" align="center">胸片</uni-th>
					<uni-th width="5%" align="center">采集时间</uni-th>
					<uni-th width="8%" align="center">医生签名</uni-th>
					<uni-th width="8%" align="center">胸片结果</uni-th>
					<uni-th width="8%" align="center">筛查次序</uni-th>
					<uni-th width="8%" align="center">筛查时间</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in pageData" :key="index">
					<uni-td align="center">{{ index + 1 }}</uni-td>
					<uni-td align="center">{{ item.screenId }}</uni-td>
					<uni-td align="center">{{ item.year }}</uni-td>
					<uni-td align="center">{{ screenType(item.screenType) }}</uni-td>
					<uni-td align="center">{{ item.name }}</uni-td>
					<uni-td align="center">{{ item.chestRadiographCode }}</uni-td>
					<uni-td align="center">
						<image :src="item.chestRadiograph" style="width: 4rem;height: 2rem;"></image>
					</uni-td>
					<uni-td align="center">{{ formatDate(item.photoTime) }}</uni-td>
					<uni-td align="center">
						<image :src="item.doctorSignature" class="image-transform"></image>
					</uni-td>
					<uni-td align="center">{{ chestRadiographOutcome(item.outcome) }}</uni-td>
					<uni-td align="center">{{ item.screenOrder }}</uni-td>
					<uni-td align="center">{{ formatDate(item.screenTime) }}</uni-td>
				</uni-tr>
			</uni-table>
			<view class="uni-pagination-box">
				<uni-pagination show-icon :page-size="pageSize" :current="pageNo" :total="total" @change="change" />
			</view>
		</view>
	</view>
</template>

<script>
import dbUtils from '../../../uni_modules/zjy-sqlite-manage/components/zjy-sqlite-manage/dbUtils';
import { dbName, tbScreenChestRadiograph, tbScreenSum } from '@/utils/sqlite';
import { screenType, ctOutcome, getLabelByValue } from '@/utils/dict.js';
import * as SynchronizeApi from '@/api/synchronize/synchronize';

export default {
	data() {
		return {
			nav: [
				{
					value: '数据同步'
				},
				{
					value: 'DR/CT组',
				},
				{
					value: '平板端到PC端',
					isActive: true
				}
			],
			loading: false,
			// 数据总量
			total: 0,
			//列表数据
			pageData: [],
			// 每页数据量
			pageSize: 10,
			// 当前页
			pageNo: 1,
			queryParams: {
				screenId: undefined,
				outcome: undefined,
				screenPoint: undefined
			},
			SyncData: [],
			dataRadio: [
				{
					text: '正常',
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
			synchronizeTime:null //上次同步时间
		};
	},
	//页面加载时自动调用
	onLoad() {
		this.getLastSynchronizeTime()
		console.log(uni.$user);
		let screenPoint = uni.$user.screenPoint.toString().split(',');
		if (screenPoint.length > 1) {
			// console.log(111);
			SynchronizeApi.getScreenPoint(uni.$user.name).then((res) => {
				this.queryParams.screenPoint = res[0].screenPoint;
				uni.$user.screenPoint = res[0].screenPoint;
			});
		} else {
			// console.log(222);
			this.queryParams.screenPoint = uni.$user.screenPoint;
		}
		this.selectedIndexs = [];
		this.search();
	},
	methods: {
		clearScreenId() {
			this.queryParams.screenId = undefined;
		},
		back() {
			uni.navigateBack({
				delta: 1
			});
		},
		close() {
			this.show = false;
			// console.log('close');
		},
		//获取的上次同步时间
		getLastSynchronizeTime(){
			uni.getStorage({
			    key: 'drctPad'
			}).then(res => {
			    if (res.data) {
			        this.synchronizeTime = res.data;
			    } else {
			        this.synchronizeTime = null;
			    }
			}).catch(err => {
			    // console.error('没找到相应的缓存:', err);
			    this.synchronizeTime = null; 
			});
		},
		// 分页触发
		change(e) {
			this.$refs.table.clearSelection(); //清除勾选内容
			this.selectedIndexs.length = 0; // 清空索引数组
			this.pageNo = e.current;
			this.search();
		},
		handleQuery() {
			let event = { type: 'current', current: 1 };
			this.change(event);
		},
		// 搜索
		search() {
			SynchronizeApi.getDrCtCount(
				this.queryParams.screenId,
				this.queryParams.outcome,
				this.queryParams.screenPoint,
				'not null'
			).then((res) => {
				if (res[0].num > 0) {
					this.total = res[0].num;
					SynchronizeApi.getDrCtData(
						this.queryParams.screenId,
						this.queryParams.outcome,
						this.queryParams.screenPoint,
						'not null',
						this.pageNo,
						this.pageSize
					).then((resp) => {
						this.pageData = resp;
					});
				} else {
					this.total = 0;
					this.pageData = [];
				}
			});
			
		},
		// 重置
		reset() {
			this.$refs.table.clearSelection(); //清除勾选内容
			this.selectedIndexs.length = 0; // 清空索引数组
			this.queryParams.screenId = undefined;
			this.queryParams.outcome = undefined;
			this.pageNo = 1;
			this.search();
		},
		// 时间戳转换
		formatDate(value) {
			const date = new Date(value);

			let y = date.getFullYear();
			let MM = date.getMonth() + 1;
			MM = MM < 10 ? '0' + MM : MM; //月补0
			let d = date.getDate();
			d = d < 10 ? '0' + d : d; //天补0
			// let h = date.getHours();
			// h = h < 10 ? ('0' + h) : h; //小时补0
			// let m = date.getMinutes();
			// m = m < 10 ? ('0' + m) : m; //分钟补0
			// let s = date.getSeconds();
			// s = s < 10 ? ('0' + s) : s; //秒补0
			return y + '-' + MM + '-' + d; //年月日
			// return y + '-' + MM + '-' + d + ' ' + h + ':' + m+ ':' + s; //年月日时分秒
		},
		formatDate1(value) {
			if (value === 'null' || value === null || value === '' || value === 0) {
				return '';
			}
			const date = new Date(value);
		
			let y = date.getFullYear();
			let MM = date.getMonth() + 1;
			MM = MM < 10 ? '0' + MM : MM; //月补0
			let d = date.getDate();
			d = d < 10 ? '0' + d : d; //天补0
			let h = date.getHours();
			h = h < 10 ? '0' + h : h; //小时补0
			let m = date.getMinutes();
			m = m < 10 ? '0' + m : m; //分钟补0
			let s = date.getSeconds();
			s = s < 10 ? '0' + s : s; //秒补0
			// return y + '-' + MM + '-' + d; //年月日
			return y + '-' + MM + '-' + d + ' ' + h + ':' + m + ':' + s; //年月日时分秒
		},
		getCurrentTime() {
			const date = new Date();
			let y = date.getFullYear();
			let MM = date.getMonth() + 1;
			MM = MM < 10 ? '0' + MM : MM; //月补0
			let d = date.getDate();
			d = d < 10 ? '0' + d : d; //天补0
			let h = date.getHours();
			h = h < 10 ? '0' + h : h; //小时补0
			let m = date.getMinutes();
			m = m < 10 ? '0' + m : m; //分钟补0
			let s = date.getSeconds();
			s = s < 10 ? '0' + s : s; //秒补0
			// return y + '-' + MM + '-' + d; //年月日
			return y + '-' + MM + '-' + d + ' ' + h + ':' + m + ':' + s; //年月日时分秒
		},
		doctorSignature(value) {
			if (value == null || value == 'null') {
				return '';
			}
			return value;
		},
		//筛查类型
		screenType(value) {
			return getLabelByValue(screenType, value);
		},
		//胸片结果转换
		chestRadiographOutcome(value) {
			return getLabelByValue(ctOutcome, value);
		},
		// 平板到pc
		async PadToPc() {
			let person=await SynchronizeApi.getPersonCount(self.queryParams.screenId, self.queryParams.screenPoint,'not null', -1, self.pageSize)
			console.log(person);
			if(person[0].num>=1){
				uni.showToast({
					title: '待筛查人员信息还有未上传的改动，请先同步待筛查人员信息',
					icon: 'none',
					duration: 2000
				})  
				return
			}
			if (this.selectedIndexs.length == 0) {
				let self = this;
				uni.showModal({
					title: '提示信息',
					content: '当前未勾选数据，是否上传全部数据？',
					cancelText: '取消',
					confirmText: '确认',
					success: async (res) =>{
						if (res.confirm) {
							if (self.pageData.length == 0) {
								uni.showToast({
									title: '暂无数据，上传失败',
									mask: true,
									icon: 'none',
									duration: 1500
								});
							} else {
								// 获取本地数据 上传到pc端
								let local=await SynchronizeApi.getDrCtData(self.queryParams.screenId,self.queryParams.outcome,self.queryParams.screenPoint,'not null',-1,self.pageSize)
									self.SyncData = local;

									self.SyncData.forEach((item) => {
										// 筛查时间转换成时间戳
										item.screenTime = new Date(item.screenTime).getTime();
										// 采集时间转换成时间戳
										item.photoTime = new Date(item.photoTime).getTime();
										if (item.doctorSignature == null || item.doctorSignature == 'null') {
											item.doctorSignature = '';
										}
									});
									// console.log(self.SyncData);
									// 上传
									let updateDr=await SynchronizeApi.updateTableData4(self.SyncData)
									console.log(updateDr);
									for (var i = 0; i < updateDr.data.length; i++) {
										await SynchronizeApi.updateIdAndStatusFlag(tbScreenChestRadiograph,updateDr.data[i].id,updateDr.data[i].newId,updateDr.data[i].idNum)
										await SynchronizeApi.updateSumFieldId(updateDr.data[i].id,updateDr.data[i].newId,updateDr.data[i].idNum,'chestRadiographId')
									}
									//上传汇总表
									for (let i = 0; i < self.SyncData.length; i++) {
										await SynchronizeApi.getLocalSumData(self.SyncData[i].screenId,self.SyncData[i].screenType,self.SyncData[i].year,self.SyncData[i].personId,self.SyncData[i].idNum).then(res=>{
											res.forEach(item=>{
												if(item.lastCollectTime){
													item.lastCollectTime = new Date(item.lastCollectTime).getTime();
												}
												if(item.lastPpdTime){
													item.lastPpdTime = new Date(item.lastPpdTime).getTime();
												}
												if(item.lastChestRadiographTime){
													item.lastChestRadiographTime = new Date(item.lastChestRadiographTime).getTime();
												}
												if(item.lastSputumExaminationTime){
													item.lastSputumExaminationTime = new Date(item.lastSputumExaminationTime).getTime();
												}
												if(item.lastElectrocardiogramTime){
													item.lastElectrocardiogramTime = new Date(item.lastElectrocardiogramTime).getTime();
												}
												
											})
										SynchronizeApi.uploadSumData(res);
									})
								}
									// 上传dr/ct组图片
									SynchronizeApi.uploadOfflineImage(3).then(res=>{
										if (res.data) {
											uni.showToast({
												title: '上传成功',
												mask: true,
												icon: 'success',
												duration: 1500
											});
											// 记录本次同步时间(存缓存)
											let time = self.getCurrentTime()
											uni.setStorage({
												key:'drctPad',
												data:time
											})
											self.synchronizeTime=time
											uni.setStorage({
												key:'drct',
												data:time
											})
										}
									})
							}
						} else if (res.cancel) {
							uni.showToast({
								title: '取消上传',
								mask: true,
								icon: 'error',
								duration: 1500
							});
						}
					}
				});
			} else {
				let self = this;
				uni.showModal({
					title: '提示信息',
					content: '是否上传当前所选数据？',
					cancelText: '取消',
					confirmText: '确认',
					success: async (res)=> {
						if (res.confirm) {
							self.selectedIndexs.map((i) => {
								self.SyncData.push(self.pageData[i]);
							});

							self.SyncData.forEach((item) => {
								// 筛查时间转换成时间戳
								item.screenTime = new Date(item.screenTime).getTime();
								// 采集时间转换成时间戳
								item.photoTime = new Date(item.photoTime).getTime();

								if (item.doctorSignature == null || item.doctorSignature == 'null') {
									item.doctorSignature = '';
								}
							});
							// console.log(self.SyncData);
							
							// 上传
							SynchronizeApi.updateTableData4(self.SyncData).then(async(res) => {
								for (var i = 0; i < res.data.length; i++) {
									await SynchronizeApi.updateIdAndStatusFlag(tbScreenChestRadiograph,res.data[i].id,res.data[i].newId,res.data[i].idNum)
									await SynchronizeApi.updateSumFieldId(res.data[i].id,res.data[i].newId,res.data[i].idNum,'chestRadiographId')
								}
								if (res.data) {
									uni.showToast({
										title: '上传成功',
										mask: true,
										icon: 'success',
										duration: 1500
									});
									// 记录本次同步时间(存缓存)
									let time = self.getCurrentTime()
									uni.setStorage({
										key:'drctPad',
										data:time
									})
									self.synchronizeTime=time
									uni.setStorage({
										key:'drct',
										data:time
									})
								}
							});
							//上传汇总表
							for (let i = 0; i < self.SyncData.length; i++) {
								SynchronizeApi.getLocalSumData(self.SyncData[i].screenId,self.SyncData[i].screenType,self.SyncData[i].year,self.SyncData[i].personId).then(res=>{
									res.forEach(item=>{
										if(item.lastCollectTime){
											item.lastCollectTime = new Date(item.lastCollectTime).getTime();
										}
										if(item.lastPpdTime){
											item.lastPpdTime = new Date(item.lastPpdTime).getTime();
										}
										if(item.lastChestRadiographTime){
											item.lastChestRadiographTime = new Date(item.lastChestRadiographTime).getTime();
										}
										if(item.lastSputumExaminationTime){
											item.lastSputumExaminationTime = new Date(item.lastSputumExaminationTime).getTime();
										}
										if(item.lastElectrocardiogramTime){
											item.lastElectrocardiogramTime = new Date(item.lastElectrocardiogramTime).getTime();
										}
										
									})
									SynchronizeApi.uploadSumData(res);
								})
							}

							self.SyncData.forEach((item) => {
								// 上传dr/ct组图片
								SynchronizeApi.uploadOfflineImageOne(
									3,
									item.screenId,
									item.personId,
									item.screenOrder,
									item.year,
									item.screenType
								);
							});
							self.SyncData = []; //清空同步数组
							self.$refs.table.clearSelection(); //清除勾选内容
							self.selectedIndexs.length = 0; // 清空索引数组
						} else if (res.cancel) {
							uni.showToast({
								title: '取消同步',
								mask: true,
								icon: 'error',
								duration: 2000
							});
						}
					}
				});
			}
		},
		// 多选处理
		selectedItems() {
			return this.selectedIndexs.map((i) => this.pageData[i]);
		},
		// 多选
		selectionChange(e) {
			// console.log(e.detail.index)
			this.selectedIndexs = e.detail.index;
		}
	}
};
</script>

<style lang="scss" scoped>
.top1 {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-bottom: 15px;
	.top-left1 {
		display: flex;
		align-items: center;
		justify-content: flex-start;
		margin-left: 10px;
	}
	.top-right1 {
		display: flex;
		align-items: center;
		justify-content: flex-end;
		.custom-sync {
			background: rgba(28, 176, 117, 1);
			color: #fff;
		}
	}
}
.top {
	display: flex;
	align-items: center;
	justify-content: space-between;
	background-color: #fff;
	.top-left {
		display: flex;
		align-items: center;
		justify-content: flex-start;
		margin-left: 10px;
	}
	.top-right {
		display: flex;
		align-items: center;
		justify-content: flex-end;
		.custom-reset {
			padding: 0 5px;
			color: #000;
			border: 1px solid rgba(207, 207, 207, 1);
			width: 80px;
		}
		.custom-search {
			color: #fff;
			background: rgba(36, 93, 209, 1);
			border: 1px solid rgba(44, 98, 208, 1);
			padding: 0 5px;
			width: 80px;
		}
	}
}
.image-transform{
	transform: scaleX(3.5) scaleY(1.25) rotate(90deg);
	width: 4rem;
	height: 2rem;
}
</style>
