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
						@click="PcToPad"
						style="margin-left: 10px"
						type="success"
						icon="download"
						iconColor='#fff'
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
				</view>
				<view class="top-right" style="margin-right: 10px">
						<up-button
							@click="handleQuery"
							:plain="true"
							class="custom-search"
							text="搜索"
						></up-button>
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
					<uni-th width="5%" align="center">筛查结果</uni-th>
					<uni-th width="5%" align="center">医生签名</uni-th>
					<uni-th width="10%" align="center">筛查时间</uni-th>
					<uni-th width="10%" align="center">筛查次序</uni-th>
					<uni-th width="10%" align="center">筛查单位</uni-th>
					<uni-th width="10%" align="center">筛查点</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in pageData" :key="index">
					<uni-td align="center">{{ index + 1 }}</uni-td>
					<uni-td align="center">{{ item.screenId }}</uni-td>
					<uni-td align="center">{{ item.year }}</uni-td>
					<uni-td align="center">{{ screenType(item.screenType) }}</uni-td>
					<uni-td align="center">{{ item.name }}</uni-td>
					<uni-td align="center">{{ outcome(item.outcome) }}</uni-td>
					<uni-td align="center">{{ item.doctorSignature }}</uni-td>
					<uni-td align="center">{{ formatDate1(item.screenTime) }}</uni-td>
					<uni-td align="center">{{ item.screenOrder }}</uni-td>
					<uni-td align="center">{{ item.screenAgency }}</uni-td>
					<uni-td align="center">{{ item.screenPoint }}</uni-td>
				</uni-tr>
			</uni-table>
			<view class="uni-pagination-box">
				<uni-pagination
					show-icon
					:page-size="queryParams.pageSize"
					:current="queryParams.pageNo"
					:total="total"
					@change="change"
				/>
			</view>
		</view>
	</view>
</template>

<script>
import * as SynchronizeApi from '@/api/synchronize/synchronize';
import dbUtils from '../../../uni_modules/zjy-sqlite-manage/components/zjy-sqlite-manage/dbUtils';
import { dbName, getById, tbScreenCollect, tbScreenSum } from '@/utils/sqlite';
import { collectSymptoms, screenType, getLabelByValue ,collectSymptoms_new} from '@/utils/dict.js';
import {removeNullProperties} from "../../../api/synchronize/synchronize";

export default {
	data() {
		return {
			nav: [
				{
					value: '数据同步'
				},
				{
					value: '采集组',
				},
				{
					value: 'PC端到平板端',
					isActive: true
				}
			],
			loading: false,
			// 数据总量
			total: 0,
			//列表数据
			pageData: [],
			queryParams: {
				// 每页数据量
				pageSize: 10,
				// 当前页
				pageNo: 1,
				screenId: undefined,
				screenPoint: undefined,
				year: uni.$user.year
				// 当前所选筛查类型
				// screenType:uni.$screenType
			},
			// 封装待同步的数据
			SyncData: [],
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
			    key: 'collectPc'
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
			this.queryParams.pageNo = e.current;
			this.search();
		},
		handleQuery() {
			let event = { type: 'current', current: 1 };
			this.change(event);
		},
		// 搜索
		search() {
			// this.loading = true;
			//采集组 数据
			// console.log(this.queryParams);
			SynchronizeApi.getTableData2(this.queryParams).then((resp) => {
				this.pageData = resp.data.list;
				// console.log(this.pageData);
				this.total = resp.data.total;
				// 	this.loading = false;
			});
		},
		// 重置
		reset() {
			this.$refs.table.clearSelection(); //清除勾选内容
			this.selectedIndexs.length = 0; // 清空索引数组
			this.queryParams.screenId = undefined;
			this.queryParams.pageNo = 1;
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
			let h = date.getHours();
			h = h < 10 ? '0' + h : h; //小时补0
			let m = date.getMinutes();
			m = m < 10 ? '0' + m : m; //分钟补0
			let s = date.getSeconds();
			s = s < 10 ? '0' + s : s; //秒补0
			// return y + '-' + MM + '-' + d; //年月日
			return y + '-' + MM + '-' + d + ' ' + h + ':' + m + ':' + s; //年月日时分秒
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

			return y + '-' + MM + '-' + d; //年月日
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
		//筛查结果
		outcome(value) {
			if (value == null || value == 'null') {
				return '';
			}
			
			let data = value.toString().split('');
			// console.log(data);
			let str = '';
			// console.log(collectSymptoms[0].text);
		
			for (let i = 0; i < data.length; i++) {
				if (data[i] === '9') {
					continue;
				} else {
					for (let j = 0; j < collectSymptoms_new.length; j++) {
						if (parseInt(data[i]) == collectSymptoms_new[j].value) {
							str += collectSymptoms_new[j].text;
						}
					}
					if (i < data.length - 1) {
						str += ',';
					}
				}
			}
			
			return str;
		},
		// pc到平板
		PcToPad() {
			// console.log(111);
			// 查询用户表中是否有数据
			SynchronizeApi.getPersonCount(null, this.queryParams.screenPoint).then((res) => {
				// console.log(res);
				if (res[0].num <= 0) {
					uni.showToast({
						title: '请先同步待筛查人员',
						mask: true,
						icon: 'none',
						duration: 2000
					});
					return;
				} else {
					// console.log(333);
					if (this.selectedIndexs.length == 0) {
						let self = this;
						uni.showModal({
							title: '提示信息',
							content: '当前没有勾选数据，是否同步全部数据？',
							cancelText: '取消',
							confirmText: '确认',
							success: function (res) {
								if (res.confirm) {
									if (self.pageData.length == 0) {
										uni.showToast({
											title: '暂无数据，同步失败',
											mask: true,
											icon: 'none',
											duration: 2000
										});
									} else {
										// 获取全部数据，不分页
										self.queryParams.pageSize = -1;
										SynchronizeApi.getTableData2(self.queryParams).then((resp) => {
											console.log(resp);
											self.SyncData = resp.data.list;
											if (uni.$user.year) {
												// 过滤出与当前工作年度不同的数据
												let differentYears = self.SyncData.filter(
													(item) => item.year != uni.$user.year
												);
												if (differentYears.length > 0) {
													uni.showToast({
														title: '工作年度冲突，请先清除数据',
														mask: true,
														icon: 'none',
														duration: 2000
													});
													return;
												}
											}

											// console.log(self.SyncData);

											let i = 0;
											self.SyncData.forEach((item) => {
                        SynchronizeApi.removeNullProperties(item)
												// 根据筛查编号，筛查次序查询 是否有对应记录的id
												// 有记录 -- 更新
												// 无记录 -- 根据id查记录
												//           无记录 -- 插入
												//           有记录 -- 查询表中id的最大值 ， 更新待插入数据的id值 再插入
												SynchronizeApi.selectTableIdByIndex(
													tbScreenCollect,
													item.screenId,
													item.screenOrder,
													item.personId
												).then((res) => {
													// 插入到本地数据库前 时间戳转换
													item.screenTime = self.formatDate(item.screenTime);

													if (res.length > 0) {
														let id = res[0].id;
														let updateData = { ...item };
														delete updateData.id;
														delete updateData.name;
														SynchronizeApi.updateTable(tbScreenCollect, updateData, id);
													} else {
														getById(dbName, tbScreenCollect, item.id).then((res) => {
															if (res.length > 0) {
																SynchronizeApi.selectMaxId(tbScreenCollect).then(
																	(resp) => {
																		if (resp.length > 0) {
																			let maxId = resp[0].maxId;
																			let newUpdateData = {
																				...item,
																				id: maxId + 1
																			};
																			delete newUpdateData.name;
																			// 执行插入操作
																			dbUtils.addTabItem(
																				dbName,
																				tbScreenCollect,
																				newUpdateData
																			);
																		}
																	}
																);
															} else {
																let addData = {
																	...item
																};
																delete addData.name;
                                console.log(addData)
																dbUtils.addTabItem(dbName, tbScreenCollect, addData);
															}
														});
													}
												});
												i++;
											});

											if (i > 0) {
												self.queryParams.pageSize = 10;
												uni.showToast({
													title: '同步了' + i + '条数据',
													mask: true,
													icon: 'success',
													duration: 3000
												});
												// 记录本次同步时间(存缓存)
												let time = self.getCurrentTime()
												uni.setStorage({
													key:'collectPc',
													data:time
												})
												self.synchronizeTime=time
												uni.setStorage({
													key:'collect',
													data:time
												})
											}
										});
										// 下拉汇总表数据
										SynchronizeApi.getSumData(self.queryParams).then((res) => {
											let sumData = res.data;
											// 先清除再插入（覆盖）
											if (sumData.length > 0) {
												SynchronizeApi.truncateTable(tbScreenSum);
												sumData.forEach((item) => {
                          delete item['padId']
													// 时间戳转换
													if(item.lastCollectTime){
														item.lastCollectTime = self.formatDate1(
															item.lastCollectTime
														);
													}
													if(item.lastPpdTime){
														item.lastPpdTime = self.formatDate1(item.lastPpdTime);
													}
													if(item.lastChestRadiographTime){
														item.lastChestRadiographTime = self.formatDate1(
															item.lastChestRadiographTime
														);
													}
													if(item.lastComputedTomographyTime){
														item.lastComputedTomographyTime = self.formatDate1(
															item.lastComputedTomographyTime
														);
													}
													if(item.lastSputumExaminationTime){
														item.lastSputumExaminationTime = self.formatDate1(
															item.lastSputumExaminationTime
														);
													}
													if(item.lastExperimentTime){
														item.lastExperimentTime = self.formatDate1(
															item.lastExperimentTime
														);
													}
													if(item.lastElectrocardiogramTime){
														item.lastElectrocardiogramTime = self.formatDate1(
															item.lastElectrocardiogramTime
														);
													}
													if(item.lastDiagnosisTime){
														item.lastDiagnosisTime = self.formatDate1(
															item.lastDiagnosisTime
														);
													}
													SynchronizeApi.removeNullProperties(item)
													// 插入数据
													dbUtils.addTabItem(dbName, tbScreenSum, item);
												});
											}
										});
									}
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
					} else {
						let self = this;
						uni.showModal({
							title: '提示信息',
							content: '是否同步当前所选数据？',
							cancelText: '取消',
							confirmText: '确认',
							success: function (res) {
								if (res.confirm) {
									self.selectedIndexs.map((i) => {
										self.SyncData.push(self.pageData[i]);
									});
									if (uni.$user.year) {
										// 过滤出与当前工作年度不同的数据
										let differentYears = self.SyncData.filter(
											(item) => item.year != uni.$user.year
										);
										if (differentYears.length > 0) {
											uni.showToast({
												title: '工作年度冲突，请先清除数据',
												mask: true,
												icon: 'none',
												duration: 2000
											});
											return;
										}
									}
									// console.log(self.SyncData);

									let i = 0;
									self.SyncData.forEach((item) => {
                    SynchronizeApi.removeNullProperties(item)
										// 根据筛查编号，筛查次序查询 是否有对应记录的id
										// 有记录 -- 更新
										// 无记录 -- 根据id查记录
										//           无记录 -- 插入
										//           有记录 -- 查询表中id的最大值 ， 更新待插入数据的id值 再插入
										SynchronizeApi.selectTableIdByIndex(
											tbScreenCollect,
											item.screenId,
											item.screenOrder,
											item.personId
										).then((res) => {
											// 插入到本地数据库前 时间戳转换
											item.screenTime = self.formatDate(item.screenTime);

											if (res.length > 0) {
												let id = res[0].id;
												let updateData = { ...item };
												delete updateData.id;
												delete updateData.name;
												SynchronizeApi.updateTable(tbScreenCollect, updateData, id);
											} else {
												getById(dbName, tbScreenCollect, item.id).then((res) => {
													if (res.length > 0) {
														SynchronizeApi.selectMaxId(tbScreenCollect).then((resp) => {
															if (resp.length > 0) {
																let maxId = resp[0].maxId;
																let newUpdateData = {
																	...item,
																	id: maxId + 1
																};
																delete newUpdateData.name;
																// 执行插入操作
																dbUtils.addTabItem(
																	dbName,
																	tbScreenCollect,
																	newUpdateData
																);
															}
														});
													} else {
														let addData = {
															...item
														};
														delete addData.name;
														dbUtils.addTabItem(dbName, tbScreenCollect, addData);
													}
												});
											}
										});
										i++;
									});
									if (i > 0) {
										uni.showToast({
											title: '同步了' + i + '条数据',
											mask: true,
											icon: 'success',
											duration: 3000
										});
										// 记录本次同步时间(存缓存)
										let time = self.getCurrentTime()
										uni.setStorage({
											key:'collectPc',
											data:time
										})
										self.synchronizeTime=time
										uni.setStorage({
											key:'collect',
											data:time
										})
									}

									// 下拉汇总表数据
									SynchronizeApi.getSumData(self.queryParams).then((res) => {
										let sumData = res.data;
										// 先清除再插入（覆盖）
										if (sumData.length > 0) {
											SynchronizeApi.truncateTable(tbScreenSum);
											sumData.forEach((item) => {
                        delete item['padId']
                        // 时间戳转换
												if(item.lastCollectTime){
													item.lastCollectTime = self.formatDate1(
														item.lastCollectTime
													);
												}
												if(item.lastPpdTime){
													item.lastPpdTime = self.formatDate1(item.lastPpdTime);
												}
												if(item.lastChestRadiographTime){
													item.lastChestRadiographTime = self.formatDate1(
														item.lastChestRadiographTime
													);
												}
												if(item.lastComputedTomographyTime){
													item.lastComputedTomographyTime = self.formatDate1(
														item.lastComputedTomographyTime
													);
												}
												if(item.lastSputumExaminationTime){
													item.lastSputumExaminationTime = self.formatDate1(
														item.lastSputumExaminationTime
													);
												}
												if(item.lastExperimentTime){
													item.lastExperimentTime = self.formatDate1(
														item.lastExperimentTime
													);
												}
												if(item.lastElectrocardiogramTime){
													item.lastElectrocardiogramTime = self.formatDate1(
														item.lastElectrocardiogramTime
													);
												}
												if(item.lastDiagnosisTime){
													item.lastDiagnosisTime = self.formatDate1(
														item.lastDiagnosisTime
													);
												}
                        SynchronizeApi.removeNullProperties(item)
												// 插入数据
												dbUtils.addTabItem(dbName, tbScreenSum, item);
											});
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
				}
			});
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
</style>
