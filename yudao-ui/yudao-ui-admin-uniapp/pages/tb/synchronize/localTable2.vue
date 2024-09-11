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
					<uni-td align="center">
						<image :src="item.doctorSignature" class="image-transform"></image>
					</uni-td>
					<uni-td align="center">{{ formatDate(item.screenTime) }}</uni-td>
					<uni-td align="center">{{ item.screenOrder }}</uni-td>
					<uni-td align="center">{{ item.screenAgency }}</uni-td>
					<uni-td align="center">{{ item.screenPoint }}</uni-td>
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
import { dbName, getById, tbScreenCollect, tbScreenSum } from '@/utils/sqlite';
import { collectSymptoms, screenType, getLabelByValue ,collectSymptoms_new} from '@/utils/dict.js';
import * as SynchronizeApi from '@/api/synchronize/synchronize';
import { getCollectData } from '../../../api/synchronize/synchronize';

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
				screenPoint: undefined
			},
			agency: undefined, //筛查单位
			SyncData: [],
			synchronizeTime:null //上次同步时间
		};
	},
	//页面加载时自动调用
	onLoad() {
		this.getLastSynchronizeTime()
		let screenPoint = uni.$user.screenPoint.toString().split(',');
		if (screenPoint.length > 1) {
			SynchronizeApi.getScreenPoint(uni.$user.name).then((res) => {
				this.queryParams.screenPoint = res[0].screenPoint;
				uni.$user.screenPoint = res[0].screenPoint;
				this.agency = res[0].agency;
				uni.$user.agency = res[0].agency;
			});
		} else {
			this.queryParams.screenPoint = uni.$user.screenPoint;
			this.agency = uni.$user.agency;
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
			    key: 'collectPad'
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
			SynchronizeApi.getCollectCount(this.queryParams.screenId, this.queryParams.screenPoint,'not null').then((res) => {
				if (res[0].num > 0) {
					this.total = res[0].num;
					SynchronizeApi.getCollectData(
						this.queryParams.screenId,
						this.queryParams.screenPoint,
						'not null',
						this.pageNo,
						this.pageSize
					).then((resp) => {
						this.pageData = resp.map((item) => ({
							...item,
							screenAgency: this.agency
						}));
						// console.log(this.pageData);
						// this.pageData=resp
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
			let label=''
			for (let item of screenType) {
				   if (item.value==value){
					   // console.log(item.label)
					   label= item.label
			   }
			}
			return label
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
		async uploadCollect(screenId,injection,screenPoint,statusFlag,pageNo,pageSize){
			let SyncData=[]
			let local=await SynchronizeApi.getCollectData(screenId, screenPoint,statusFlag, pageNo, pageSize)
				// console.log(local);
				SyncData = local.map((item) => ({
					...item,
					screenAgency: this.agency,
					padId:""+item.id+item.idNum
				}));
				// 筛查时间转换成时间戳
				SyncData.forEach((item) => {
				  item.screenTime = new Date(item.screenTime).getTime();
				});
			// console.log(self.SyncData);
			// 上传
				try{
					let updateDataRes=await SynchronizeApi.updateTableData2(SyncData)
					console.log(updateDataRes);
					if(!updateDataRes || updateDataRes.data ==null){
						throw Error("未收到响应")
					}
					  // console.log(res);
					for (let i = 0; i < updateDataRes.data.length; i++) {
						if(updateDataRes.data[i].id == updateDataRes.data[i].newId) {
							await SynchronizeApi.updateStatusFlagOnly(tbScreenCollect, updateDataRes.data[i].id, updateDataRes.data[i].idNum)
						}else{
							await SynchronizeApi.updateIdAndStatusFlag(tbScreenCollect,updateDataRes.data[i].id,updateDataRes.data[i].newId,updateDataRes.data[i].idNum)
							await SynchronizeApi.updateSumFieldId(updateDataRes.data[i].id,updateDataRes.data[i].newId,updateDataRes.data[i].idNum,'collectId')
						}
					}
				}catch(e){
					console.log(e);
					return -1
				}
				return 1
		},
		// 平板到pc
		async PadToPc() {
			uni.getNetworkType({
				success: async (res)=> {
					if( !res || res.networkType == "null" || res.networkType=='none' || !res.networkType){
						uni.showToast({
							title: '当前无网络连接',
							icon: 'none'
						});
						throw new Error("无网络连接")
					}else{
						let person=await SynchronizeApi.getPersonCount(this.queryParams.screenId, this.queryParams.screenPoint,'not null', -1, this.pageSize)
						// console.log(person);
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
								success: async (res)=> {
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
											let collectCount=await SynchronizeApi.getCollectCount(this.queryParams.screenId, this.queryParams.screenPoint,'not null')
											let pageCount=Math.ceil(collectCount[0].num / self.pageSize)
											console.log(pageCount);
											let errorPageNo=[]
											let errorSumData=[]
											uni.showLoading({
												title: '正在上传采集组数据...',
												mask: true
											});
											for (let j = 1; j <= pageCount; j++) {
												let local=await SynchronizeApi.getCollectData(self.queryParams.screenId, self.queryParams.screenPoint,'not null', j, self.pageSize)
												// console.log(local);
												self.SyncData = local.map((item) => ({
													...item,
													screenAgency: self.agency,
													padId:""+item.id+item.idNum
												}));
												// 筛查时间转换成时间戳
												self.SyncData.forEach((item) => {
												  item.screenTime = new Date(item.screenTime).getTime();
												});
											// console.log(self.SyncData);
											// 上传
												try{
													let updateDataRes=await SynchronizeApi.updateTableData2(self.SyncData)
													console.log(updateDataRes);
													if(!updateDataRes || updateDataRes.data ==null){
														throw Error("未收到响应")
													}
													  // console.log(res);
													for (let i = 0; i < updateDataRes.data.length; i++) {
														if(updateDataRes.data[i].id == updateDataRes.data[i].newId) {
															await SynchronizeApi.updateStatusFlagOnly(tbScreenCollect, updateDataRes.data[i].id, updateDataRes.data[i].idNum)
														}else{
															await SynchronizeApi.updateIdAndStatusFlag(tbScreenCollect,updateDataRes.data[i].id,updateDataRes.data[i].newId,updateDataRes.data[i].idNum)
															await SynchronizeApi.updateSumFieldId(updateDataRes.data[i].id,updateDataRes.data[i].newId,updateDataRes.data[i].idNum,'collectId')
														}
													}
												}catch(e){
													errorPageNo.push(j)
												}
											}
											uni.hideLoading();
											uni.showToast({
												title: '采集组数据上传成功',
												icon: 'success',
												duration: 2000
											})
											uni.showLoading({
												title: '正在上传汇总数据...',
												mask: true
											});
											//上传汇总表
											for (let i = 0; i < self.SyncData.length; i++) {
												let localSum=await SynchronizeApi.getLocalSumData(self.SyncData[i].screenId,self.SyncData[i].screenType,self.SyncData[i].year,self.SyncData[i].personId,self.SyncData[i].idNum)
												await localSum.forEach(item=>{
													item.padId=""+item.id+item.idNum
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
												if(localSum.length>200){
													let onceLength=100 // 步长
													let start =0  // 开始索引
													let end =0  // 结束索引
													let num=Math.ceil(localSum.length / onceLength)
													for (var k = 0; k < num; k++) {
														end =start+onceLength
														let updateReq=localSum.slice(start,end+1)
														let sumResp=await SynchronizeApi.uploadSumData(updateReq);
														if(!sumResp || !sumResp.data ){
															errorSumData.push(...updateReq)
														}
													}
												}else{
													let sumResp=await SynchronizeApi.uploadSumData(localSum);
													if(!sumResp || !sumResp.data ){
														errorSumData.push(...localSum)
													}
												}
											}
											if(errorPageNo.length>0 || errorSumData.length>0){
												uni.showModal({
													title: '提示',
													content: '部分数据上传失败，是否重新上传？',
													success:async (res)=> {
														if (res.confirm) {
															uni.showLoading({
																title: '重试中...',
																mask: true
															});
															if(errorSumData.length>0){
																await errorSumData.forEach(item=>{
																		item.padId=""+item.id+item.idNum
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
																	
																	if(errorSumData.length>200){
																		let onceLength=100 // 步长
																		let start =0  // 开始索引
																		let end =0  // 结束索引
																		let num=Math.ceil(errorSumData.length / onceLength)
																		for (var k = 0; k < num; k++) {
																			try{
																				end =start+onceLength
																				let updateReq=errorSumData.slice(start,end+1)
																				console.log(updateReq);
																				let sumResp=await SynchronizeApi.uploadSumData(updateReq);
																				if(sumResp && sumResp.data ){
																					errorSumData.pop(updateReq)
																				}
																			}catch(e){
																				console.error(e);
																				break
																				
																			}
																		}
																	}else{
																		try{
																			// console.log(errorSumData);
																			let sumResp=await SynchronizeApi.uploadSumData(errorSumData);
																			if(sumResp && sumResp.data ){
																				errorSumData=[]
																			}
																		}catch(e){
																			console.error(e);
																		}
																	}
											
															}
															if(errorPageNo.length>0){
																for (let i = 0; i < errorPageNo.length; i++) {
																	let result=await this.uploadCollect(self.queryParams.screenId,self.queryParams.injection,self.queryParams.screenPoint,'not null',errorPageNo[i],self.pageSize)
																	if(result==1){
																		errorPageNo.pop(errorPageNo[i])
																	}
																}
																
															}
															uni.hideLoading();
															if(errorPageNo.length>0 || errorSumData.length>0){
																uni.showModal({
																	title: '失败',
																	content: '重试失败，请在网络稳定时重试',
																	success: (res)=> {
																		if (res.confirm) {
																			// 执行确认后的操作
																		} 
																		else {
																			// 执行取消后的操作
																		}
																	}
																})
															}else{
																uni.hideLoading();
																uni.showToast({
																	title: '重试成功',
																	icon: 'success',
																	duration: 2000
																})  
															}
														} 
														else {
															// 执行取消后的操作
														}
													}
												})
											}
											uni.hideLoading();
											uni.showToast({
												title: '汇总数据上传成功',
												icon: 'success',
												duration: 2000
											})
											// 上传采集组图片
											await SynchronizeApi.uploadOfflineImage(1);
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
												  key:'collectPad',
												  data:time
												})
												self.synchronizeTime=time
												uni.setStorage({
												  key:'collect',
												  data:time
												})
											  }
											
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
								success: async (res) =>{
									if (res.confirm) {
										self.selectedIndexs.map((i) => {
											self.SyncData.push(self.pageData[i]);
										});
										// 筛查时间转换成时间戳
										self.SyncData.forEach((item) => {
											let date = new Date(item.screenTime);
											item.screenTime = date.getTime();
										});
										// console.log(self.SyncData);
										// 上传
										SynchronizeApi.updateTableData2(self.SyncData).then(async(res) => {
											// console.log(res);
											for (var i = 0; i < res.data.length; i++) {
												if(res.data[i].id == res.data[i].newId) {
													SynchronizeApi.updateStatusFlagOnly(tbScreenCollect, res.data[i].id, res.data[i].idNum)
												}else{
													await SynchronizeApi.updateIdAndStatusFlag(tbScreenCollect,res.data[i].id,res.data[i].newId,res.data[i].idNum)
													await SynchronizeApi.updateSumFieldId(res.data[i].id,res.data[i].newId,res.data[i].idNum,'collectId')
												}
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
													key:'collectPad',
													data:time
												})
												self.synchronizeTime=time
												uni.setStorage({
													key:'collect',
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
											// 上传采集组图片
											SynchronizeApi.uploadOfflineImageOne(
												1,
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
.image-transform{
	transform: scaleX(3.5) scaleY(1.25) rotate(90deg);
	width: 4rem;
	height: 2rem;
}
</style>
