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
						placeholder="筛查编号"
						style="width: 230px"
						cancelButton="none"
						@clear="clearScreenId"
					></uni-search-bar>
					<span style="font-size: 16px">是否雾化 </span>
					<uni-data-select
						style="width: 100px;margin-left: 10px;background-color: #f9f9f9;"
						v-model="queryParams.atomization"
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
					<uni-th width="3%" align="center">序号</uni-th>
					<uni-th width="5%" align="center">筛查编号</uni-th>
					<uni-th width="5%" align="center">工作年度</uni-th>
					<uni-th width="5%" align="center">筛查类型</uni-th>
					<uni-th width="10%" align="center">患者姓名</uni-th>
					<uni-th width="5%" align="center">医生签名</uni-th>
					<uni-th width="5%" align="center">是否雾化</uni-th>
					<uni-th width="10%" align="center">痰标本类型</uni-th>
					<uni-th width="8%" align="center">即时痰照片</uni-th>
					<uni-th width="8%" align="center">即时痰标本号</uni-th>
					<uni-th width="8%" align="center">即时痰采集时间</uni-th>
					<uni-th width="8%" align="center">夜痰照片</uni-th>
					<uni-th width="8%" align="center">夜痰标本号</uni-th>
					<uni-th width="8%" align="center">夜痰采集时间</uni-th>
					<uni-th width="8%" align="center">晨痰照片</uni-th>
					<uni-th width="8%" align="center">晨痰标本号</uni-th>
					<uni-th width="8%" align="center">晨痰采集时间</uni-th>
					<uni-th width="10%" align="center">结果</uni-th>
					<uni-th width="10%" align="center">筛查次序</uni-th>
					<uni-th width="10%" align="center">筛查时间</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in pageData" :key="index">
					<uni-td align="center">{{ index + 1 }}</uni-td>
					<uni-td align="center">{{ item.screenId }}</uni-td>
					<uni-td align="center">{{ item.year }}</uni-td>
					<uni-td align="center">{{ screenType(item.screenType) }}</uni-td>
					<uni-td align="center">{{ item.name }}</uni-td>
					<uni-td align="center">
						<image :src="item.doctorSignature" class="image-transform"></image>
					</uni-td>
					<uni-td align="center">{{ transform(item.atomization) }}</uni-td>
					<uni-td align="center">{{ sputumType(item.type) }}</uni-td>
					<uni-td align="center">
						<image :src="item.forthwithSputum" style="width: 4rem;height: 2rem;" ></image>
					</uni-td>
					<uni-td class="uni-td" align="center">{{ item.forthwithSputumCode }}</uni-td>
					<uni-td class="uni-td" align="center">{{ formatDate(item.forthwithSputumTime) }}</uni-td>
					<uni-td align="center">
						<image :src="item.eveningSputum" style="width: 4rem;height: 2rem;"></image>
					</uni-td>
					<uni-td class="uni-td" align="center">{{ item.eveningSputumCode }}</uni-td>
					<uni-td class="uni-td" align="center">{{ formatDate(item.eveningSputumTime) }}</uni-td>
					<uni-td align="center">
						<image :src="item.morningSputum" style="width: 4rem;height: 2rem;"></image>
					</uni-td>
					<uni-td class="uni-td" align="center">{{ item.morningSputumCode }}</uni-td>
					<uni-td class="uni-td" align="center">{{ formatDate(item.morningSputumTime) }}</uni-td>
					<uni-td align="center">{{ sputumCheck(item.outcome) }}</uni-td>
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
import { dbName, tbScreenSputumExamination, tbScreenSum } from '@/utils/sqlite';
import {
	screenType,
	getLabelByValue,
	sputumType,
	sputumCheck,
	genderMap,
	commonMap,
	firstTypeMap,
	moreTypeMap,
	nationMap
} from '@/utils/dict.js';
import * as SynchronizeApi from '@/api/synchronize/synchronize';

export default {
	data() {
		return {
			nav: [
				{
					value: '数据同步'
				},
				{
					value: '痰检组',
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
				atomization: undefined,
				screenPoint: undefined
			},
			SyncData: [],
			dataRadio: [
				{
					text: '是',
					value: 1
				},
				{
					text: '否',
					value: 0
				}
			],
			synchronizeTime:null //上次同步时间
		};
	},
	//页面加载时自动调用
	onLoad() {
		this.getLastSynchronizeTime()
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
			    key: 'sputumPad'
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
			SynchronizeApi.getSputumExaminationCount(
				this.queryParams.screenId,
				this.queryParams.atomization,
				this.queryParams.screenPoint
			).then((res) => {
				// console.log(res);
				if (res[0].num > 0) {
					this.total = res[0].num;
					SynchronizeApi.getSputumExaminationData(
						this.queryParams.screenId,
						this.queryParams.atomization,
						this.queryParams.screenPoint,
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
			this.queryParams.atomization = undefined;
			this.pageNo = 1;
			this.search();
		},
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
		//筛查类型
		screenType(value) {
			return getLabelByValue(screenType, value);
		},
		//是否雾化、痰检 转换
		transform(value) {
			return commonMap[value];
		},
		// 痰标本类型
		sputumType(value) {
			let data = value.toString().split('');
			let str = '';

			for (let i = 0; i < data.length; i++) {
				str += getLabelByValue(sputumType, parseInt(data[i]));
				if (i < data.length - 1) {
					str += ',';
				}
			}
			return str;
		},
		// 痰检结果
		sputumCheck(value) {
			if (value === null) {
				return '未审核';
			}
			if (value === '' || value === 'null') {
				return '全部合格';
			}
			if (value === null) {
				return '';
			}
			let data = value.toString().split('');
			let str = '';

			for (let i = 0; i < data.length; i++) {
				str += getLabelByValue(sputumCheck, parseInt(data[i]));
				if (i < data.length - 1) {
					str += '、';
				}
			}
			return str + '不合格';
		},
		// 平板到pc
		PadToPc() {
			if (this.selectedIndexs.length == 0) {
				let self = this;
				uni.showModal({
					title: '提示信息',
					content: '当前未勾选数据，是否上传全部数据？',
					cancelText: '取消',
					confirmText: '确认',
					success: function (res) {
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
								SynchronizeApi.getSputumExaminationData(
									self.queryParams.screenId,
									self.queryParams.atomization,
									self.queryParams.screenPoint,
									-1,
									self.pageSize
								).then((res) => {
									self.SyncData = res;
									// 筛查时间转换成时间戳
									self.SyncData.forEach((item) => {
										item.screenTime = new Date(item.screenTime).getTime();
										if (item.forthwithSputumTime) {
											item.forthwithSputumTime = new Date(item.forthwithSputumTime).getTime();
										}
										if (item.eveningSputumTime) {
											item.eveningSputumTime = new Date(item.eveningSputumTime).getTime();
										}
										if (item.morningSputumTime) {
											item.morningSputumTime = new Date(item.morningSputumTime).getTime();
										}

									});
									// console.log(self.SyncData);
									
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

									// 上传
									SynchronizeApi.updateTableData5(self.SyncData).then((res) => {
										// 上传痰检组图片
										SynchronizeApi.uploadOfflineImage(4);

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
												key:'sputumPad',
												data:time
											})
											self.synchronizeTime=time
											uni.setStorage({
												key:'sputum',
												data:time
											})
										}
									});
									
								});
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
					success: function (res) {
						if (res.confirm) {
							self.selectedIndexs.map((i) => {
								self.SyncData.push(self.pageData[i]);
							});

							// 筛查时间转换成时间戳
							self.SyncData.forEach((item) => {
								item.screenTime = new Date(item.screenTime).getTime();
								if (item.forthwithSputumTime) {
									item.forthwithSputumTime = new Date(item.forthwithSputumTime).getTime();
								}
								if (item.eveningSputumTime) {
									item.eveningSputumTime = new Date(item.eveningSputumTime).getTime();
								}
								if (item.morningSputumTime) {
									item.morningSputumTime = new Date(item.morningSputumTime).getTime();
								}
							});
							// console.log(self.SyncData);
							self.SyncData.forEach((item) => {
								// 上传痰检组图片
								SynchronizeApi.uploadOfflineImageOne(
									4,
									item.screenId,
									item.personId,
									item.screenOrder,
									item.year,
									item.screenType
								);
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

							// 上传
							SynchronizeApi.updateTableData5(self.SyncData).then((res) => {
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
										key:'sputumPad',
										data:time
									})
									self.synchronizeTime=time
									uni.setStorage({
										key:'sputum',
										data:time
									})
								}
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

		// 多选
		selectionChange(e) {
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
