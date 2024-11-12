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
						text="同步"/>
				</view>
			</view>
      <up-row>
        上次传输失败数据：<b style="color: #b31d28" v-if="errorStr !='无'">{{errorStr}}</b>
        <b v-else>{{errorStr}}</b>
      </up-row>
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
					<uni-th width="10%" align="center">单位(学校)</uni-th>
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
					<uni-td align="center">{{ checkNull(item.screenPoint) }}</uni-td>
					<uni-td align="center">{{ screenType(item.screenType) }}</uni-td>
					<uni-td align="center">{{ transform(item.isNew) }}</uni-td>
					<uni-td align="center">{{ transform(item.isNewStudent) }}</uni-td>
					<uni-td align="center">{{ screenStatus(item.isScreened) }}</uni-td>
					<uni-td align="center">{{ firstType(item.firstType) }}</uni-td>
					<uni-td align="center">{{ moreType(item) }}</uni-td>
					<uni-td align="center">{{ dayjs(item.screenStartTime).format('YYYY/MM/DD') }}-{{dayjs(item.screenEndTime).format('YYYY/MM/DD')}}</uni-td>
					<uni-td align="center">{{ gender(item.sex) }}</uni-td>
					<uni-td align="center">{{ item.age }}</uni-td>
					<uni-td align="center">{{ item.tel }}</uni-td>
					<uni-td align="center">{{ nation(item.nation) }}</uni-td>
					<uni-td align="center">{{ item.schoolOrTemple }}</uni-td>
					<uni-td align="center">{{ checkNull(item.classroom) }}</uni-td>
					<uni-td align="center">{{ item.height }}</uni-td>
					<uni-td align="center">{{ item.weight }}</uni-td>
					<uni-td align="center">{{ item.permanentAddress }}</uni-td>
					<uni-td align="center">{{ item.address }}</uni-td>
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
import dayjs from 'dayjs';
import { dbName, tbScreenChestRadiograph, tbScreenCollect, tbScreenPerson, tbScreenPpd, tbScreenSum, updatePerson ,errorKey} from '@/utils/sqlite';
import {
  errorUpload,
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
import * as SynchronizeApi from '@/api/synchronize/synchronize';
import { tbScreenImages } from '../../../utils/screenImages';
import {updateErrorFlag} from "../../../api/synchronize/synchronize";

export default {
	data() {
		return {
      errorUpload,
      errorStr:'无',
			nav: [
				{
					value: '数据同步'
				},
				{
					value: '待筛查人员',
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
			SyncData: [],
			synchronizeTime:null //上次同步时间
		};
	},
	//页面加载时自动调用
	async onLoad() {
    try{
      let v = await uni.getStorage({key:errorKey})
      let show=[]
      if (v.data.length>0){
        v.data.forEach(i=>{
          show.push(getLabelByValue(errorUpload,i))
        })
        this.errorStr=show
      }
    }catch(e){
      // this.errorStr
    }
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
    dayjs,
    getLabelByValue,
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
			    key: 'personPad'
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
			SynchronizeApi.getPersonCount(this.queryParams.screenId, this.queryParams.screenPoint,'not null').then((res) => {
				// console.log(res);
				if (res[0].num > 0) {
					this.total = res[0].num;
					SynchronizeApi.getPersonData(
						this.queryParams.screenId,
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
			this.pageNo = 1;
			this.search();
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
		checkNull(value) {
			if (value == null || value == 'null') {
				return '';
			}
			return value;
		},
		//性别
		gender(value) {
			return genderMap[value];
		},
		//是否新增、新生、已筛查 转换
		transform(value) {
			return commonMap[value];
		},
		//筛查类型
		screenType(value) {
			return getLabelByValue(screenType, value);
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
    //返回当前网络状态，有网true ，断网 false
    getNetworkStatus() {
      return new Promise((resolve, reject) => {
        uni.getNetworkType({
          success: (res) => {
            resolve(res.networkType !== 'none');
          },
          fail: (err) => {
            reject(err);
          }
        });
      });
    },
		// 平板到pc
		async PadToPc() {
      let netType=await this.getNetworkStatus()
      if (!netType){
        uni.showToast({
          title: '当前无网络连接',
          mask: true,
          icon: 'none',
          duration: 3000
        });
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
                let pageSize=100
                let personCount=await SynchronizeApi.getPersonCount(this.queryParams.screenId, this.queryParams.screenPoint, 'not null')
                let pageCount=Math.ceil(personCount[0].num / pageSize)
              if (pageCount == 0) {
                uni.showToast({
                  title: '暂无需上传的数据',
                  mask: true,
                  icon: 'none',
                  duration:3000
                });
                return
              }
              // 获取本地数据 上传到pc端
                let local=await SynchronizeApi.getPersonData(this.queryParams.screenId, this.queryParams.screenPoint, 'not null',-1, this.pageSize)
								// 筛查时间转换成时间戳
								local.forEach((item) => {
								  let startDate = new Date(item.screenStartTime);
								  item.screenStartTime = startDate.getTime();
								  let endDate = new Date(item.screenEndTime);
								  item.screenEndTime = endDate.getTime();
								});
              let personStart=0
              let personEnd=0
								// console.log(self.SyncData);
                uni.showLoading({
                  title: '正在上传...',
                  mask: true
                });
                await this.updateErrorFlag(1, '0')
								// 上传
                try {
                  for (let j = 0; j < local.length; j++) {
                    personEnd = personStart + pageSize
                    let updateReq = localCollect.slice(personStart, personEnd)
                    personStart += pageSize
                    let res = SynchronizeApi.updateTableData1(updateReq)
                    if(!updateDataRes || updateDataRes.data ==null){
                      throw new Error("未收到响应")
                    }
                    for (var i = 0; i < res.data.length; i++) {
                      await SynchronizeApi.updatePersonId(tbScreenPerson, res.data[i].id, res.data[i].newId, res.data[i].idNum, res.data[i].screenId)
                      await SynchronizeApi.updatePersonIdOnly(tbScreenSum, res.data[i].id, res.data[i].newId, res.data[i].idNum, res.data[i].screenId)
                      await SynchronizeApi.updatePersonIdOnly(tbScreenCollect, res.data[i].id, res.data[i].newId, res.data[i].idNum, res.data[i].screenId)
                      await SynchronizeApi.updatePersonIdOnly(tbScreenPpd, res.data[i].id, res.data[i].newId, res.data[i].idNum, res.data[i].screenId)
                      await SynchronizeApi.updatePersonIdOnly(tbScreenChestRadiograph, res.data[i].id, res.data[i].newId, res.data[i].idNum, res.data[i].screenId)
                      await SynchronizeApi.updatePersonIdOnly(tbScreenImages, res.data[i].id, res.data[i].newId, res.data[i].idNum, res.data[i].screenId)
                    }
                  }
                }catch (e) {
                  console.error(e)
                  uni.hideLoading();
                  uni.showToast({
                    title:'上传失败',
                    icon: 'error',
                    duration:3000
                  })
                  return
                }
              await SynchronizeApi.updateErrorFlag(0, '0')
              uni.hideLoading();
                uni.showToast({
                  title: '上传成功',
                  mask: true,
                  icon: 'success',
                  duration:3000
                });
                // 记录本次同步时间(存缓存)
                let time = self.getCurrentTime()
                uni.setStorage({
                  key:'personPad',
                  data:time
                })
                self.synchronizeTime=time
                uni.setStorage({
                  key:'person',
                  data:time
                })

						} else if (res.cancel) {
							uni.showToast({
								title: '取消上传',
								mask: true,
								icon: 'error',
                duration:3000
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
              await SynchronizeApi.updateErrorFlag(1, '0')
              self.selectedIndexs.map((i) => {
                self.SyncData.push(self.pageData[i]);
              });

              // 筛查时间转换成时间戳
              self.SyncData.forEach((item) => {
                let date = new Date(item.screenTime);
                item.screenTime = date.getTime();
              });
              // console.log(self.SyncData);
              uni.showLoading({
                title: '正在上传...',
                mask: true
              });
              // 上传
              try {
                let res =await SynchronizeApi.updateTableData1(self.SyncData)
                console.log(res)
                for (let i = 0; i < res.data.length; i++) {
                  // console.log(tableData);
                  await SynchronizeApi.updatePersonId(tbScreenPerson, res.data[i].id, res.data[i].newId, res.data[i].idNum, res.data[i].screenId)
                  await SynchronizeApi.updatePersonIdOnly(tbScreenSum, res.data[i].id, res.data[i].newId, res.data[i].idNum, res.data[i].screenId)
                  await SynchronizeApi.updatePersonIdOnly(tbScreenCollect, res.data[i].id, res.data[i].newId, res.data[i].idNum, res.data[i].screenId)
                  await SynchronizeApi.updatePersonIdOnly(tbScreenPpd, res.data[i].id, res.data[i].newId, res.data[i].idNum, res.data[i].screenId)
                  await SynchronizeApi.updatePersonIdOnly(tbScreenChestRadiograph, res.data[i].id, res.data[i].newId, res.data[i].idNum, res.data[i].screenId)
                  await SynchronizeApi.updatePersonIdOnly(tbScreenImages, res.data[i].id, res.data[i].newId, res.data[i].idNum, res.data[i].screenId)
                }
              }catch (e) {
                console.error(e)
                uni.hideLoading();
                uni.showToast({
                  title:'上传失败',
                  icon: 'error',
                  duration:3000
                })
                return
              }
              await SynchronizeApi.updateErrorFlag(0, '0')
              uni.hideLoading();
              uni.showToast({
                title: '上传成功',
                mask: true,
                icon: 'success',
                duration:3000
              });
              // 记录本次同步时间(存缓存)
              let time = self.getCurrentTime()
              uni.setStorage({
                key:'personPad',
                data:time
              })
              self.synchronizeTime=time
              uni.setStorage({
                key:'person',
                data:time
              })
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
</style>
