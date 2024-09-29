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
					<span style="font-size: 16px">身份证号 </span>
					<uni-search-bar
						@confirm="handleQuery"
						:focus="false"
						class="top-search"
						v-model="queryParams.idNum"
						placeholder="请输入身份证号后四位"
						style="width: 230px"
						cancelButton="none"
						@clear="clearIdNum"
					></uni-search-bar>
					<span style="font-size: 16px; margin: 0 10px">筛查类型 </span>
					<uni-data-select
						style="width: 180px; background-color: #f8f8f8"
						v-model="queryParams.screenType"
						:localdata="screenTypeList"
						@change="changeScreenType"
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
					<uni-th width="5%" align="center">姓名</uni-th>
					<uni-th width="10%" align="center">身份证号</uni-th>
					<uni-th width="10%" align="center">工作年度</uni-th>
					<uni-th width="10%" align="center">筛查编号</uni-th>
					<uni-th width="10%" align="center">筛查点</uni-th>
					<uni-th width="10%" align="center">筛查类型</uni-th>
					<uni-th width="5%" align="center">是否新增</uni-th>
					<uni-th width="5%" align="center">是否为新生</uni-th>
					<uni-th width="10%" align="center">筛查状态</uni-th>
					<uni-th width="10%" align="center">第一人群分类</uni-th>
					<uni-th width="10%" align="center">多人群分类</uni-th>
					<uni-th width="10%" align="center">计划筛查时间</uni-th>
					<uni-th width="10%" align="center">性别</uni-th>
					<uni-th width="10%" align="center">年龄</uni-th>
					<uni-th width="10%" align="center">联系电话</uni-th>
					<uni-th width="10%" align="center">民族</uni-th>
					<uni-th width="10%" align="center">学校或寺庙</uni-th>
					<uni-th width="10%" align="center">班级</uni-th>
					<uni-th width="10%" align="center">身高</uni-th>
					<uni-th width="10%" align="center">体重</uni-th>
					<uni-th width="10%" align="center">户籍地址</uni-th>
					<uni-th width="10%" align="center">现住址</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in pageData" :key="index">
					<uni-td align="center">{{ index + 1 }}</uni-td>
					<uni-td align="center">{{ item.name }}</uni-td>
					<uni-td align="center">{{ item.idNum }}</uni-td>
					<uni-td align="center">{{ item.year }}</uni-td>
					<uni-td align="center">{{ item.screenId }}</uni-td>
					<uni-td align="center">{{ screenPoint(item.screenPoint) }}</uni-td>
					<uni-td align="center">{{ screenType(item.screenType) }}</uni-td>
					<uni-td align="center">{{ transform(item.isNew) }}</uni-td>
					<uni-td align="center">{{ transform(item.isNewStudent) }}</uni-td>
					<uni-td align="center">{{ screenStatus(item.isScreened) }}</uni-td>
					<uni-td align="center">{{ firstType(item.firstType) }}</uni-td>
					<uni-td align="center">{{ moreType(item) }}</uni-td>
					<uni-td align="center">{{ formatDate(item.screenTime) }}</uni-td>
					<uni-td align="center">{{ gender(item.sex) }}</uni-td>
					<uni-td align="center">{{ item.age }}</uni-td>
					<uni-td align="center">{{ item.tel }}</uni-td>
					<uni-td align="center">{{ nation(item.nation) }}</uni-td>
					<uni-td align="center">{{ item.schoolOrTemple }}</uni-td>
					<uni-td align="center">{{ item.classroom }}</uni-td>
					<uni-td align="center">{{ item.height }}</uni-td>
					<uni-td align="center">{{ item.weight }}</uni-td>
					<uni-td align="center">{{ item.permanentAddress }}</uni-td>
					<uni-td align="center">{{ item.address }}</uni-td>
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
import { dbName, tbScreenPerson, getById, updatePerson, getMaxScreenOrder } from '@/utils/sqlite';
import {
	screenType,
	getLabelByValue,
	genderMap,
	commonMap,
	firstTypeMap,
	moreTypeMap,
	nationMap,
	screenStatusMap,
	items2,
	items3
} from '@/utils/dict.js';

export default {
	data() {
		return {
			nav: [
				{
					value: '数据同步'
				},
				{
					value: '待筛查人员',
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
				idNum: undefined,
				screenPoint: undefined,
				year: uni.$user.year,
				// 当前所选筛查类型
				screenType: undefined
				// screenType:uni.$screenType
			},
			isSelectYear: false,
			// 封装待同步的数据
			SyncData: [],
			range: [],
			screenTypeList: [
				{ value: 1, text: '常规筛查' },
				{ value: 2, text: '新生入学筛查' },
				{ value: 3, text: '应急筛查' }
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
		// console.log(this.queryParams.screenPoint);
		// this.generateYear()
	},
	methods: {
		back() {
			uni.navigateBack({
				delta: 1
			});
		},
		close() {
			this.show = false;
			// console.log('close');
		},
		// 分页触发
		change(e) {
			// console.log(e);
			this.$refs.table.clearSelection(); //清除勾选内容
			this.selectedIndexs.length = 0; // 清空索引数组
			this.queryParams.pageNo = e.current;
			this.search();
		},
		//获取的上次同步时间
		getLastSynchronizeTime(){
			uni.getStorage({
			    key: 'personPc'
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
		// 生成年份
		generateYear() {
			let nowYear = new Date().getFullYear();
			let lastYear = nowYear - 1;
			let nextYear = nowYear + 1;
			// 添加新的年份到range数组中
			this.range.push({ value: lastYear, text: String(lastYear) });
			this.range.push({ value: nowYear, text: String(nowYear) });
			this.range.push({ value: nextYear, text: String(nextYear) });
		},
		// 选择年份
		changeYear(e) {
			// console.log("e:", e);
		},
		// 选择筛查类型
		changeScreenType(e) {
			// console.log("e:", e);
		},
		handleQuery() {
			let event = { type: 'current', current: 1 };
			this.change(event);
		},
		// 搜索
		search() {
			// this.loading = true;
			//摸底表 待筛查人员数据
			if (this.queryParams.year) {
				this.isSelectYear = true;
			}
			SynchronizeApi.getTableData1(this.queryParams).then((resp) => {
				this.pageData = resp.data.list;
				this.total = resp.data.total;
				// 	this.loading = false;
			});
		},
		// 重置
		reset() {
			this.$refs.table.clearSelection(); //清除勾选内容
			this.selectedIndexs.length = 0; // 清空索引数组
			this.queryParams.idNum = undefined;
			this.queryParams.screenType = undefined;
			this.queryParams.year = undefined;
			this.queryParams.pageNo = 1;
			this.search();
			this.isSelectYear = false;
		},
		// 筛查状态
		screenStatus(value) {
			if (value == null || value == 'null') {
				return '';
			}
			return screenStatusMap[value];
		},
		// 时间戳转换
		formatDate(value) {
			if (value === 'null' || value === null || value === 0) {
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
		// 身份证
		idCard(value) {
			if (value == null || value == 'null') {
				return '';
			}
			return value;
		},
		screenPoint(value) {
			if (value == null || value == 'null') {
				return '';
			}
			return value;
		},
		//筛查类型
		screenType(value) {
			return getLabelByValue(screenType, value);
		},
		//性别
		gender(value) {
			return genderMap[value];
		},
		//是否新增、新生、已筛查 转换
		transform(value) {
			return commonMap[value];
		},
		// 第一人群分类
		firstType(value) {
			return firstTypeMap[value];
		},
		// 多人群分类
		moreType(value) {
			if (value.moreType == 0 || value.moreType == 'NaN') {
				return '无';
			}
			
			let selectedOptions = [];
			if(value.firstType==1){
				items2.forEach(item=>{
					if(value.moreType & parseInt(item.value,10)){
						selectedOptions.push(item.text)
					}
				})
			}
			if(value.firstType==4){
				items3.forEach(item=>{
					if(value.moreType & parseInt(item.value,10)){
						selectedOptions.push(item.text)
					}
				})
			}
			
			if (selectedOptions.length == 0) {
				return '无';
			}
			let text = '';
			selectedOptions.forEach((i) => {
				text = text + (text == '' ? '' : ',') + i;
			});
			
			return text;
		},
		// 民族
		nation(value) {
			return nationMap[value];
		},
		// pc到平板
		PcToPad() {
			// 查询用户表中是否有数据
			// SynchronizeApi.getWorkTeamCount(null).then(res=>{
			// 	// console.log(res);
			// 	if(res[0].num<=0){
			// 		uni.showToast({
			// 			title: '请先同步工作队伍' ,
			// 			mask: true,
			// 			icon: 'none',
			// 			duration: 1500
			// 		})
			// 		return
			// 	}
			// })

			// if(!this.queryParams.year){
			// 	uni.showToast({
			// 		title: '请先选择工作年度并点击搜索再同步' ,
			// 		mask: true,
			// 		icon: 'none',
			// 		duration: 2000
			// 	})
			// 	return
			// }
			// if(!this.isSelectYear){
			// 	uni.showToast({
			// 		title: '请先选择工作年度并点击搜索再同步' ,
			// 		mask: true,
			// 		icon: 'none',
			// 		duration: 2000
			// 	})
			// 	return
			// }

			// 插入year到用户表
			// SynchronizeApi.insertYear(this.queryParams.year,this.queryParams.screenPoint)
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

								SynchronizeApi.getTableData1(self.queryParams).then((resp) => {
									// console.log(uni.$user.year);
									// console.log(resp);
									if (uni.$user.year) {
										// 过滤出与当前工作年度不同的数据
										let differentYears = resp.data.list.filter(
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

									self.SyncData = resp.data.list;
									let newData = self.SyncData.map((item) => {
										// 创建一个新对象，包含除了 moreTypeStr 属性之外的其他属性
										const { moreTypeStr, ...newItem } = item;
										return newItem;
									});
									//处理数据
									let newList = newData.map((item) => ({
										...item,
                    screenStartTime: item.screenStartTime == 'null' || item.screenStartTime == null ? '' : item.screenStartTime,
										screenEndTime: item.screenEndTime == 'null' || item.screenEndTime == null ? '' : item.screenEndTime,
										remark: item.remark == 'null' || item.remark == null ? '' : item.remark
									}));

									// console.log(newList);

									let i = 0;
									newList.forEach((item) => {
                    // 遍历对象的属性，判断如果值为null，就删除这个属性
                    Object.keys(item).forEach(key => {
                      // console.log(`${key}--${item[key]}`)
                      if (item[key] == null || item[key] == 'null') {
                        delete item[key];
                      }
                    });
                    console.log(item)
                    // 根据 筛查类型screenType, year(筛查年份),idNum查询 是否有对应记录的id
										// 有记录 -- 更新
										// 无记录 -- 根据id查记录
										//           无记录 -- 插入
										//           有记录 -- 查询表中id的最大值 ， 更新待插入数据的id值 再插入
										SynchronizeApi.selectPersonIdByIndex(
											item.screenType,
											item.idNum,
											item.year
										).then((res) => {
											if (res.length > 0) {
												let personId = res[0].id;
												let updateData = { ...item };
												delete updateData.id; // 删除 id 属性

												SynchronizeApi.updateTable(tbScreenPerson, updateData, personId);
											} else {
												getById(dbName, tbScreenPerson, item.id).then((res) => {
													if (res.length > 0) {
																let newUpdateData = {
																	...item
																};
                                Object.keys(newUpdateData).forEach(key => {
                                  if (newUpdateData[key] == null) {
                                    delete newUpdateData[key];
                                  }
                                });
                                delete newUpdateData['id']
																// 执行插入操作
																dbUtils.addTabItem(
																	dbName,
																	tbScreenPerson,
																	newUpdateData
																);
													} else {
                            let newUpdateData = {
                              ...item
                            };
                            Object.keys(newUpdateData).forEach(key => {
                              if (newUpdateData[key] == null) {
                                delete newUpdateData[key];
                              }
                            });
														dbUtils.addTabItem(dbName, tbScreenPerson, item);
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
											duration: 2000
										});
										// 记录本次同步时间(存缓存)
										let time = self.getCurrentTime()
										uni.setStorage({
											key:'personPc',
											data:time
										})
										self.synchronizeTime=time
										uni.setStorage({
											key:'person',
											data:time
										})
									}
								});
							}
						} else if (res.cancel) {
							uni.showToast({
								title: '取消同步',
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
								let differentYears = self.SyncData.filter((item) => item.year != uni.$user.year);
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

							let newData = self.SyncData.map((item) => {
								// 创建一个新对象，包含除了 moreTypeStr 属性之外的其他属性
								const { moreTypeStr, ...newItem } = item;
								return newItem;
							});
							//处理数据
							let newList = newData.map((item) => ({
								...item,
								screenTime: item.screenTime == 'null' || item.screenTime == null ? '' : item.screenTime,
								remark: item.remark == 'null' || item.remark == null ? '' : item.remark
							}));

							// console.log(newList);

							let i = 0;
							newList.forEach((item) => {
								// 根据year(筛查年份),idNum查询 是否有对应记录的id
								// 有记录 -- 更新
								// 无记录 -- 根据id查记录
								//           无记录 -- 插入
								//           有记录 -- 查询表中id的最大值 ， 更新待插入数据的id值 再插入
								SynchronizeApi.selectPersonIdByIndex(item.screenType, item.idNum, item.year).then(
									(res) => {
										if (res.length > 0) {
											let personId = res[0].id;
											let updateData = { ...item };
											delete updateData.id; // 删除 id 属性
											SynchronizeApi.updateTable(tbScreenPerson, updateData, personId);
										} else {
											getById(dbName, tbScreenPerson, item.id).then((res) => {
												if (res.length > 0) {
													SynchronizeApi.selectMaxId(tbScreenPerson).then((resp) => {
														if (resp.length > 0) {
															let maxId = resp[0].maxId;
															let newUpdateData = {
																...item,
																id: maxId + 1
															};
															// 执行插入操作
															dbUtils.addTabItem(dbName, tbScreenPerson, newUpdateData);
														}
													});
												} else {
													dbUtils.addTabItem(dbName, tbScreenPerson, item);
												}
											});
										}
									}
								);
								i++;
							});
							if (i > 0) {
								uni.showToast({
									title: '同步了' + i + '条数据',
									mask: true,
									icon: 'success',
									duration: 2000
								});
								// 记录本次同步时间(存缓存)
								let time = self.getCurrentTime()
								uni.setStorage({
									key:'personPc',
									data:time
								})
								self.synchronizeTime=time
								uni.setStorage({
									key:'person',
									data:time
								})
							}
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
		.custom-sync{
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
