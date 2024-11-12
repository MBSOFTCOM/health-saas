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
				<view style="margin-left: 15px;" v-if="synchronizeTime">
					上次同步时间:{{synchronizeTime}}
				</view>
				<view class="top-right1" style="margin-right: 10px">
					<up-button
						@click="PadToPc"
						style="margin-left: 10px;"
						type="success"
						icon="download"
						iconColor="#fff"
						:plain="true"
						class="custom-sync"
						text="同步"/>
          <up-button
              style="margin-left: 10px"
              type="success"
              text="上传汇总表"
              icon="download"
              iconColor="#fff"
              :plain="true"
              class="custom-sync"
              @click="uploadSum()"/>
          <up-button
              style="margin-left: 10px"
              type="success"
              icon="download"
              iconColor="#fff"
              :plain="true"
              class="custom-sync"
              text="上传图片"
              @click="uploadImage()"/>
          <up-button
              style="margin-left: 10px"
              type="success"
              icon="download"
              iconColor="#fff"
              :plain="true"
              class="custom-sync"
              text="上传试剂消耗"
              @click="uploadConsume()"/>
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
					<span style="font-size: 16px">是否注射 </span>
					<uni-data-select
						style="width: 100px;margin-left: 10px;background-color: #f9f9f9;"
						v-model="queryParams.injection"
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
					<uni-th width="60" align="center">序号</uni-th>
					<uni-th width="5%" align="center">筛查编号</uni-th>
					<uni-th width="5%" align="center">筛查类型</uni-th>
					<uni-th width="10%" align="center">工作年度</uni-th>
					<uni-th width="8%" align="center">患者姓名</uni-th>
					<uni-th width="5%" align="center">硬结横径(mm)</uni-th>
					<uni-th width="5%" align="center">硬结纵径(mm)</uni-th>
					<uni-th width="5%" align="center">红晕横径(mm)</uni-th>
					<uni-th width="5%" align="center">红晕纵径(mm)</uni-th>
					<uni-th width="8%" align="center">是否含有水泡/双圈/坏死/淋巴管炎</uni-th>
					<uni-th width="8%" align="center">是否提交注射结果</uni-th>
					<uni-th width="8%" align="center">注射方式</uni-th>
					<uni-th width="8%" align="center">是否感染</uni-th>
					<uni-th width="8%" align="center">医生签名</uni-th>
					<uni-th width="8%" align="center">注射单位</uni-th>
					<uni-th width="8%" align="center">筛查次序</uni-th>
					<uni-th width="8%" align="center">筛查时间</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in pageData" :key="index">
					<uni-td align="center">{{ index + 1 }}</uni-td>
					<uni-td align="center">{{ item.screenId }}</uni-td>
					<uni-td align="center">{{ screenType(item.screenType) }}</uni-td>
					<uni-td align="center">{{ item.year }}</uni-td>
					<uni-td align="center">{{ item.name }}</uni-td>
					<uni-td align="center">{{ item.transverseDiameter }}</uni-td>
					<uni-td align="center">{{ item.longitudinalDiameter }}</uni-td>
					<uni-td align="center">{{ item.blushTransverseDiameter }}</uni-td>
					<uni-td align="center">{{ item.blushLongitudinalDiameter }}</uni-td>
					<uni-td align="center">{{ ppdOutcome(item.bleb) }}</uni-td>
					<uni-td align="center">{{ transform(item.injection) }}</uni-td>
					<uni-td align="center">{{ injection(item.injectionWay) }}</uni-td>
					<uni-td align="center">{{ transform(item.outcome) }}</uni-td>
					<uni-td align="center">
						<image :src="item.doctorSignature" class="image-transform"></image>
					</uni-td>
					<uni-td align="center">{{ item.injectionAgency }}</uni-td>
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
import { dbName, tbScreenPpd, tbScreenSum ,errorKey} from '@/utils/sqlite';
import { screenType, getLabelByValue, commonMap, injectionWayMap, ppdOutcome ,errorUpload} from '@/utils/dict.js';
import * as SynchronizeApi from '@/api/synchronize/synchronize';
import * as ConsumeRecordApi from '@/api/screen/consumeRecord';
import * as regentApi from '@/api/screen/regent'

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
					value: 'PDD组',
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
				injection: undefined,
				screenPoint: undefined
			},
			agency: undefined, //筛查单位
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
			    key: 'ppdPad'
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
			// console.log(this.queryParams);
			SynchronizeApi.getPpdCount(
				this.queryParams.screenId,
				this.queryParams.injection,
				this.queryParams.screenPoint,
				'not null'
			).then((res) => {
				if (res[0].num > 0) {
					this.total = res[0].num;
					SynchronizeApi.getPpdData(
						this.queryParams.screenId,
						this.queryParams.injection,
						this.queryParams.screenPoint,
						'not null',
						this.pageNo,
						this.pageSize
					).then((resp) => {
						// console.log(resp);
						this.pageData = resp.map((item) => ({
							...item,
							injectionAgency: this.agency
						}));
						// console.log(this.pageData);
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
			this.queryParams.injection = undefined;
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
		//筛查类型
		screenType(value) {
			return getLabelByValue(screenType, value);
		},
		// 局部症状
		ppdOutcome(value) {
			if (value == null || value == 'null') {
				return '';
			}
			let data = value.toString().split('');
			let str = '';

			for (let i = 0; i < data.length; i++) {
				str += getLabelByValue(ppdOutcome, parseInt(data[i]));
				if (i < data.length - 1) {
					str += ',';
				}
			}
			return str;
		},
		//是否注射
		transform(value) {
			return commonMap[value];
		},
		// 注射方式
		injection(value) {
			return injectionWayMap[value];
		},
		async uploadPPD(){
      await SynchronizeApi.updateErrorFlag(1,'4')
      let uploadPageSize=100
      let ppdCount=await SynchronizeApi.getPpdCount(this.queryParams.screenId,this.queryParams.injection,this.queryParams.screenPoint,'not null')
      let pageCount=Math.ceil(ppdCount[0].num / uploadPageSize)
// 处理ppd数据
      let ppdData=await SynchronizeApi.getPpdData(this.queryParams.screenId,this.queryParams.injection,this.queryParams.screenPoint,'not null',-1,this.pageSize)
      let ppdUploadData = ppdData.map((item) => ({
        ...item,
        padId:item.padId??""+item.id+item.idNum,
        injectionAgency: self.agency
      }));
      // 筛查时间转换成时间戳
      ppdUploadData.forEach((item) => {
        let date = new Date(item.screenTime);
        item.screenTime = date.getTime();
      });
      let collectStart=0
      let collectEnd=0
// 上传ppd数据
      uni.showLoading({
        title: '正在上传PPD组数据...',
        mask: true
      });
      try{
        for (let j = 1; j <= pageCount; j++) {
          collectEnd=collectStart+uploadPageSize
          let updateReq=ppdUploadData.slice(collectStart,collectEnd)
          collectStart+=uploadPageSize
          let updatePpd=await SynchronizeApi.updateTableData3(updateReq)
          if(!updatePpd || updatePpd.data ==null){
            throw new Error("未收到响应")
          }
          for (let i = 0; i < updatePpd.data.length; i++) {
            if(updatePpd.data[i].id == updatePpd.data[i].newId) {
              await SynchronizeApi.updateStatusFlagOnly(tbScreenPpd, updatePpd.data[i].id, updatePpd.data[i].idNum)
            }else {
              await SynchronizeApi.updateIdAndStatusFlag(tbScreenPpd, updatePpd.data[i].id, updatePpd.data[i].newId, updatePpd.data[i].idNum)
              await SynchronizeApi.updateSumFieldId(updatePpd.data[i].id, updatePpd.data[i].newId, updatePpd.data[i].idNum, 'ppdId')
            }
          }
        }
      }catch(e){
        console.error(e)
        uni.hideLoading();
        uni.showToast({
          title:'上传失败',
          icon: 'error',
          duration: 2000
        })
        return
      }
      await SynchronizeApi.updateErrorFlag(0,'4')
		},
    // 上传汇总表
    async uploadSum(){
      let localSum=await SynchronizeApi.getLocalSumDataTypeAndYear(2,uni.$user.year)
      if(!localSum || localSum.length==0 || localSum == []){
        uni.showToast({
          title: '汇总表没有需要上传的数据',
          icon: 'none',
          duration: 3000
        })
        return
      }
      uni.showLoading({
        title: '上传中...',
        mask: true
      });
      await SynchronizeApi.updateErrorFlag(1,'5')
      await localSum.forEach(item=>{
        item.padId=item.padId??(""+item.id+item.idNum)
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
        let onceLength=50 // 步长
        let start =0  // 开始索引
        let end =0  // 结束索引
        let num=Math.ceil(localSum.length / onceLength)
        console.log(num);
        try{
          for (var k = 0; k < num; k++) {
            end =start+onceLength
            console.log(`${start}---${end}`);
            let updateReq=localSum.slice(start,end)
            start=start+onceLength
            let sumResp=await SynchronizeApi.uploadSumData(updateReq);
            console.log(updateReq);
            for (var i = 0; i < sumResp.length; i++) {
              await SynchronizeApi.updateStatusFlagOnly(tbScreenSum, sumResp[i].id, sumResp[i].idNum)
            }
          }
        }catch(e){
          uni.hideLoading();
          uni.showToast({
            title: `上传失败,请重新上传`,
            mask: true,
            icon: 'none',
            duration: 2000
          });
          return
        }
      }else{
        try{
          let sumResp=await SynchronizeApi.uploadSumData(localSum);
          for (var i = 0; i < sumResp.length; i++) {
            await SynchronizeApi.updateStatusFlagOnly(tbScreenSum, sumResp[i].id, sumResp[i].idNum)
          }
        }catch(e){
          uni.hideLoading();
          uni.showToast({
            title: `上传失败,请重新上传`,
            mask: true,
            icon: 'none',
            duration: 3000
          });
          return
        }
      }
      await SynchronizeApi.updateErrorFlag(0,'5')
      uni.hideLoading()
      uni.showToast({
        title: '上传成功',
        icon: 'success',
        duration: 2000
      })
    },
    async uploadImage(){
      try{
        uni.showLoading({
          title: '上传中...',
          mask: true
        });
        await SynchronizeApi.updateErrorFlag(1,'6')
        await SynchronizeApi.uploadOfflineImage(2);
      }catch(e){
        uni.hideLoading();
        uni.showToast({
          title: `请重新上传图片`,
          mask: true,
          icon: 'none',
          duration: 3000
        });
        return
      }
      await SynchronizeApi.updateErrorFlag(0,'6')
    },
    async uploadConsume(){
      let consume=await ConsumeRecordApi.selectLocalData()
      console.log(consume)
      if (!consume || consume.length==0){
        uni.showToast({
          title:'暂无需要上传的数据',
          icon: 'none',
          duration: 3000
        })
        return
      }
      await SynchronizeApi.updateErrorFlag(1,'10')
      uni.showLoading({
        title: '上传中...',
        mask: true
      });
      let str=this.getCurrentDateString()
      // 处理数据
      for (let i = 0; i < consume.length; i++) {
        consume[i].padId=consume[i].padId??(''+consume[i].id+uni.$person.id+str)
        if(consume[i].createTime){
          consume[i].createTime = new Date(consume[i].createTime).getTime();
        }
        if(consume[i].updateTime){
          consume[i].updateTime = new Date(consume[i].updateTime).getTime();
        }
      }
      // 上传试剂消耗
      try{
        let consumeResult=await ConsumeRecordApi.upload(consume)
        console.log(consumeResult)
        console.log(consume.length)
        if(!consumeResult || consumeResult.data==null){
          throw new Error('上传试剂消耗失败')
        }
        console.log(consumeResult.data !=consume.length)
        if (consumeResult.data !=consume.length){
          uni.showToast({
            title:'部分数据无法上传，请联系相关人员查看使用试剂的现有库存数量',
            icon: 'none',
            duration: 3000
          })
          return
        }
      }catch(e){
        console.error(e)
        uni.hideLoading()
        uni.showToast({
          title:'上传失败',
          icon: 'error',
          duration: 2000
        })
        return
      }
      uni.hideLoading()
      uni.showToast({
        title:'上传成功',
        icon: 'success',
        duration: 2000
      })
      await SynchronizeApi.updateErrorFlag(0,'10')
    },
		listenNet(){
      // 添加实时监听网络状态，当无网络连接时抛出错误
			uni.onNetworkStatusChange(function (res) {
				console.log(res);
				if (!res.isConnected) {
					uni.showToast({
						title: '当前无网络连接',
            duration:2000,
						icon: 'none'
					});
				throw new Error("无网络连接")
				}
			});
		},
		/**
		 * 获取当前时间的日期字符串
		 */
		getCurrentDateString() {
		  const date = new Date();
		  const year = date.getFullYear();
		  const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份从0开始，需要加1
		  const day = String(date.getDate()).padStart(2, '0');
		  return `${year}${month}${day}`;
		},
		// 平板到pc
		async PadToPc() {
			try{
				uni.getNetworkType({
					success: async (res)=> {
						if( !res || res.networkType == "null" || res.networkType=='none' || !res.networkType){
              uni.showToast({
                title: '当前无网络连接',
                duration:2000,
                icon: 'none'
              });
            }else {
              let person=await SynchronizeApi.getPersonCount(this.queryParams.screenId, this.queryParams.screenPoint,'not null', -1, this.pageSize)
              // console.log(person);
              if(person[0].num>=1){
                uni.showToast({
                  title: '待筛查人员信息还有未上传的改动，请先同步待筛查人员信息',
                  icon: 'none',
                  duration: 3000
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
                    // try{
                    if (res.confirm) {
                      await SynchronizeApi.updateErrorFlag(1, ['4', '5', '6', '10'])
                      let uploadPageSize=100
                      let ppdCount=await SynchronizeApi.getPpdCount(this.queryParams.screenId,this.queryParams.injection,this.queryParams.screenPoint,'not null')
                      let pageCount=Math.ceil(ppdCount[0].num / uploadPageSize)
                      if (pageCount == 0) {
                        uni.showToast({
                          title: '暂无需上传的数据',
                          mask: true,
                          icon: 'none',
                          duration: 2000
                        });
                        return
                      }
// 处理ppd数据
                        let ppdData=await SynchronizeApi.getPpdData(self.queryParams.screenId,self.queryParams.injection,self.queryParams.screenPoint,'not null',-1,self.pageSize)
                        let ppdUploadData = ppdData.map((item) => ({
                          ...item,
                          padId:item.padId??(""+item.id+item.idNum),
                          injectionAgency: self.agency
                        }));
                        // 筛查时间转换成时间戳
                        ppdUploadData.forEach((item) => {
                          let date = new Date(item.screenTime);
                          item.screenTime = date.getTime();
                        });
                        let collectStart=0
                        let collectEnd=0
// 上传ppd数据
                        uni.showLoading({
                          title: '正在上传PPD组数据...',
                          mask: true
                        });
                        try{
                          for (let j = 1; j <= pageCount; j++) {
                            collectEnd=collectStart+uploadPageSize
                            let updateReq=ppdUploadData.slice(collectStart,collectEnd)
                            collectStart+=uploadPageSize
                            let updatePpd=await SynchronizeApi.updateTableData3(updateReq)
                            if(!updatePpd || updatePpd.data ==null){
                              throw new Error("未收到响应")
                            }
                            for (let i = 0; i < updatePpd.data.length; i++) {
                              if(updatePpd.data[i].id == updatePpd.data[i].newId) {
                                await SynchronizeApi.updateStatusFlagOnly(tbScreenPpd, updatePpd.data[i].id, updatePpd.data[i].idNum)
                              }else {
                                await SynchronizeApi.updateIdAndStatusFlag(tbScreenPpd, updatePpd.data[i].id, updatePpd.data[i].newId, updatePpd.data[i].idNum)
                                await SynchronizeApi.updateSumFieldId(updatePpd.data[i].id, updatePpd.data[i].newId, updatePpd.data[i].idNum, 'ppdId')
                              }
                            }
                          }
                        }catch(e){
                        console.error(e)
                        uni.hideLoading();
                        uni.showToast({
                          title:'上传失败',
                          icon: 'error',
                          duration: 2000
                        })
                        return
                      }
                        await SynchronizeApi.updateErrorFlag(0,'4')
//上传汇总表
                        uni.hideLoading();
                        uni.showLoading({
                          title: '正在上传汇总表数据...',
                          mask: true
                        });
                        let localSum=await SynchronizeApi.getLocalSumDataTypeAndYear(2,uni.$user.year)
                        await localSum.forEach(item=>{
                          item.padId=item.padId??(""+item.id+item.idNum)
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
                          try {
                            for (var k = 0; k < num; k++) {
                              end = start + onceLength
                              console.log(`${start}---${end}`);
                              let updateReq = localSum.slice(start, end)
                              start = start + onceLength
                              let sumResp = await SynchronizeApi.uploadSumData(updateReq);
                              for (var i = 0; i < sumResp.length; i++) {
                                await SynchronizeApi.updateStatusFlagOnly(tbScreenSum, sumResp[i].id, sumResp[i].idNum)
                              }
                              console.log(updateReq);
                            }
                          } catch (e) {
                            console.error(e)
                            uni.hideLoading();
                            uni.showToast({
                              title:'上传失败',
                              icon: 'error',
                              duration: 2000
                            })
                            return
                          }
                        }else{
// 汇总表总数据量小于200
                          try{
                            let sumResp=await SynchronizeApi.uploadSumData(localSum);
                            for (var i = 0; i < sumResp.length; i++) {
                              await SynchronizeApi.updateStatusFlagOnly(tbScreenSum, sumResp[i].id, sumResp[i].idNum)
                            }
                          }catch(e){
                            uni.hideLoading();
                            uni.showToast({
                              title: '上传失败',
                              icon: 'error',
                              duration: 2000
                            })
                            return
                          }
                        }
                        uni.hideLoading();
                        await SynchronizeApi.updateErrorFlag(0, '5')
// 上传ppd组图片
                        try {
                          await SynchronizeApi.uploadOfflineImage(2);
                        }catch (e) {
                          console.error(e)
                          uni.hideLoading();
                          uni.showToast({
                            title:'上传失败',
                            icon: 'error',
                            duration: 2000
                          })
                          return
                        }
                        await SynchronizeApi.updateErrorFlag(0, '6')
                        // 记录本次同步时间(存缓存)
                        let time = self.getCurrentTime()
                        uni.setStorage({
                        key:'ppdPad',
                        data:time
                        })
                        self.synchronizeTime=time
                        uni.setStorage({
                          key:'ppd',
                          data:time
                        })

                    } else if (res.cancel) {
                      uni.showToast({
                        title: '取消上传',
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
                  content: '是否上传当前所选数据？',
                  cancelText: '取消',
                  confirmText: '确认',
                  success: async (res) =>{
                    if (res.confirm) {
                      await SynchronizeApi.updateErrorFlag(1, ['4', '5', '6', '10'])
                      self.selectedIndexs.map((i) => {
                        self.SyncData.push(self.pageData[i]);
                      });
                      // console.log(self.SyncData);
                      // return
                      // self.SyncData=self.SyncData.map(item=>({
                      // 	...item,
                      // 	transverseDiameter:item.xLength,
                      // 	longitudinalDiameter:item.yLength
                      // }))
                      // 筛查时间转换成时间戳
                      self.SyncData.forEach((item) => {
                        let date = new Date(item.screenTime);
                        item.screenTime = date.getTime();
                        item.padId=item.padId??(""+item.id+item.idNum)
                      });
                      // console.log(self.SyncData);
// 上传ppd组数据
                      uni.hideLoading();
                      uni.showLoading({
                        title: '上传ppd组数据中...',
                        mask: true
                      });
                      try{
                        let updatePpd = await SynchronizeApi.updateTableData3(self.SyncData)
                        for (let i = 0; i < updatePpd.data.length; i++) {
                          console.log(i)
                          if (updatePpd.data[i].id == updatePpd.data[i].newId) {
                            console.log(560)
                            await SynchronizeApi.updateStatusFlagOnly(tbScreenPpd, updatePpd.data[i].id, updatePpd.data[i].idNum)
                          } else {
                            console.log(789)
                            await SynchronizeApi.updateIdAndStatusFlag(tbScreenPpd, updatePpd.data[i].id, updatePpd.data[i].newId, updatePpd.data[i].idNum)
                            console.log(89)
                            await SynchronizeApi.updateSumFieldId(updatePpd.data[i].id, updatePpd.data[i].newId, updatePpd.data[i].idNum, 'ppdId')
                            console.log(10)
                          }
                        }
                        console.log(3344)
                      }catch (e) {
                        console.error(e)
                        uni.hideLoading();
                        uni.showToast({
                          title:'上传失败',
                          icon: 'error',
                          duration: 2000
                        })
                        return
                      }
                      console.log(556)
                      await SynchronizeApi.updateErrorFlag(0, '4')
// 上传汇总表
                      uni.hideLoading();
                      uni.showLoading({
                        title: '上传汇总数据中...',
                        mask: true
                      });
                      try{
                        for (let i = 0; i < self.SyncData.length; i++) {
                          let sumData = await SynchronizeApi.getLocalSumData(self.SyncData[i].screenId, self.SyncData[i].screenType, self.SyncData[i].year, self.SyncData[i].personId,self.SyncData[i].idNum)
                          sumData.forEach(item => {
                            item.padId=item.padId??(""+item.id+item.idNum)
                            if (item.lastCollectTime) {
                              item.lastCollectTime = new Date(item.lastCollectTime).getTime();
                            }
                            if (item.lastPpdTime) {
                              item.lastPpdTime = new Date(item.lastPpdTime).getTime();
                            }
                            if (item.lastChestRadiographTime) {
                              item.lastChestRadiographTime = new Date(item.lastChestRadiographTime).getTime();
                            }
                            if (item.lastSputumExaminationTime) {
                              item.lastSputumExaminationTime = new Date(item.lastSputumExaminationTime).getTime();
                            }
                            if (item.lastElectrocardiogramTime) {
                              item.lastElectrocardiogramTime = new Date(item.lastElectrocardiogramTime).getTime();
                            }
                          })
                          let sumResp=await SynchronizeApi.uploadSumData(sumData);
                          if (sumResp.data) {
                            await SynchronizeApi.updateStatusFlagOnly(tbScreenSum, sumData[0].id, sumData[0].idNum)
                          }
                        }
                      }catch (e) {
                        console.error(e)
                        uni.hideLoading();
                        uni.showToast({
                          title:'上传失败',
                          icon: 'error',
                          duration: 2000
                        })
                        return
                      }
                      await SynchronizeApi.updateErrorFlag(0, '5')
// 上传ppd组图片
                      try{
                        await self.SyncData.forEach((item) => {
                          SynchronizeApi.uploadOfflineImageOne(
                              2,
                              item.screenId,
                              item.personId,
                              item.screenOrder,
                              item.year,
                              item.screenType
                          );
                        });
                      }catch (e) {
                        console.error(e)
                        uni.hideLoading();
                        uni.showToast({
                          title:'上传失败',
                          icon: 'error',
                          duration: 2000
                        })
                        return
                      }
                      await SynchronizeApi.updateErrorFlag(0, '6')
                      // 记录本次同步时间(存缓存)
                      let time = self.getCurrentTime()
                      uni.setStorage({
                        key:'ppdPad',
                        data:time
                      })
                      self.synchronizeTime=time
                      uni.setStorage({
                        key:'ppd',
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
		      	}
				},
          fail(e) {
            uni.showToast({
              title: '网络状态异常',
              icon: 'error',
              duration: 2000
            })
          }
			  });
			}catch(e){
				uni.showToast({
					title: e.message,
					icon: 'none',
					duration:2000
				})
				return
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
