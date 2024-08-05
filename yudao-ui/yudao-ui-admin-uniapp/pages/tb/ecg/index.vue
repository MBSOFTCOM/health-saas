<template>
	<view>
		<view class="top1">
			<view class="top1-left">
				<yile-breadcrumb
					:nav="nav"
					color="rgba(153, 153, 153, 1)"
					actColor="rgba(36, 93, 209, 1)"
				></yile-breadcrumb>
			</view>
			<!-- 右侧功能按钮 -->
			<view class="top1-right">
				<view class="search-btn">
					<up-button
						@click="scanner"
						style="margin-left: 10px"
						class="custom-sm"
						icon="scan"
						iconColor="#fff"
						color="#fff"
						:plain="true"
						text="扫描"
					></up-button>
				</view>
			</view>
		</view>
		
		<view class="card">
			<view class="card-main">
				<view class="card-top various1">采集人数</view>
				<view class="card-tle">
					<view class="card-sp">
						<view class="card-text">
							今日
							<span class="sp-text">
								{{ this.dataView.todayCount }}<span style="font-size: 18px;">人</span>
							</span>
						</view>
					</view>
					<view class="line"></view>
					<view class="card-sp">
						<view class="card-text">
							本月
							<span class="sp-text">
								{{ this.dataView.currentMonthCount }}<span style="font-size: 18px;">人</span>
							</span>
						</view>
					</view>
					<view class="line"></view>
					<view class="card-sp">
						<view class="card-text">
							本年
							<span class="sp-text">
								{{ this.dataView.currentYearCount }}<span style="font-size: 18px;">人</span>
							</span>
						</view>
					</view>
				</view>
			</view>
			<view class="card-main">
				<view class="card-top various2">监测结果</view>
				<view class="card-tle">
					<view class="card-sp">
						<view class="card-text">
							窦性心率
							<span class="sp-text">
								{{
									this.dataView.TestResultData.sinus_rhythm_count
										? this.dataView.TestResultData.sinus_rhythm_count
										: 0
								}}<span style="font-size: 18px;">人</span>
							</span>
						</view>
						<view class="card-text1" style="margin-left: 70px">
							其他
							<span class="sp-text" style="color: #000">
								{{
									this.dataView.TestResultData.other_ecg_count
										? this.dataView.TestResultData.other_ecg_count
										: 0
								}}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
					<view class="line"></view>
					<view class="card-sp">
						<view class="card-text">
							正常心电图
							<span class="sp-text">
								{{
									this.dataView.TestResultData.normal_ecg_count
										? this.dataView.TestResultData.normal_ecg_count
										: 0
								}}<span style="font-size: 18px;">人</span>
							</span>
						</view>
					</view>
					<view class="line"></view>
					<view class="card-sp">
						<view class="card-text">
							总人数
							<span class="sp-text">
								{{ this.dataView.TestResultData.total }}<span style="font-size: 18px;">人</span>
							</span>
						</view>
					</view>
				</view>
			</view>
		</view>
		<view class="content">
			<!-- 顶部搜索栏 -->
			<view class="top">
				<view class="top-left">
					<span style="font-size: 18px">筛查编号</span>
					<uni-search-bar
						:focus="false"
						class="top-search"
						v-model="screenNum"
						clearButton="always"
						placeholder="筛查编号"
						style="width: 245px"
						cancelButton="none"
						@clear="clearScreenNum"
					></uni-search-bar>
					<span style="font-size: 18px; display: inline-block; margin-left: 55px">身份证号</span>
					<uni-search-bar
						:focus="false"
						class="top-search"
						v-model="searchIdCard"
						clearButton="always"
						placeholder="身份证号"
						style="width: 230px"
						cancelButton="none"
						@clear="clearIdCard"
					></uni-search-bar>
					<span style="font-size: 18px; display: inline-block; margin-left: 55px">姓名</span>
					<uni-search-bar
						:focus="false"
						class="top-search"
						v-model="searchName"
						clearButton="always"
						placeholder="患者姓名"
						style="width: 165px"
						cancelButton="none"
						@clear="clearName"
					></uni-search-bar>
				</view>
			</view>
			<view class="statistics" style="justify-content: flex-start">
				<span style="font-size: 18px; margin-left: 10px">筛查日期</span>
				<select-date style="margin-left: 10px" ref="componentDate" @selectDate="handleSelectDate" />
				<view class="search-btn">
					<up-button @click="search" :plain="true" class="custom-search" text="搜索"></up-button>
					<up-button
						@click="reset"
						style="margin: 0 10px"
						class="custom-reset"
						:plain="true"
						text="重置"
					></up-button>
					<up-button
						@click="reviewProcess"
						style="width: 115px;"
						type="primary"
						:plain="true"
						text="查看筛查流程"
					></up-button>
				</view>
			</view>
		</view>
		<!-- 患者列表 -->
		<view class="uni-container">
			<uni-table :style="tableStyle" ref="table" :loading="loading"  stripe emptyText="暂无更多数据">
				<uni-tr>
					<uni-th width="60" align="center">序号</uni-th>
					<uni-th width="110" align="center">筛查编号</uni-th>
					<uni-th width="100" align="center">姓名</uni-th>
					<uni-th width="180" align="center">采集次序/时间</uni-th>
					<uni-th width="230" align="center">操作</uni-th>
					<uni-th width="180" align="center">身份证号</uni-th>
					<uni-th width="60" align="center">性别</uni-th>
					<uni-th width="60" align="center">年龄</uni-th>
					<uni-th width="5%" align="center">民族</uni-th>
					<uni-th width="150" align="center">筛查点</uni-th>
					<uni-th width="120" align="center">第一人群分类</uni-th>
					<uni-th width="5%" align="center">重点人群分类</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in pageData" :key="index + 'a'">
					<uni-td align="center">{{ index + 1 }}</uni-td>
					<uni-td align="center">{{ item.screenId }}</uni-td>
					<uni-td align="center">{{ item.name }}</uni-td>
					<uni-td align="center">
						<view>
							<uni-data-select
								v-model="item.orderVal"
								:localdata="item.orderTime"
								@change="changeOrder"
								placeholder="选择采集次序"
								:clear="false"
								placement="top"
								@click="toggleOverflow"
							></uni-data-select>
						</view>
					</uni-td>
					<uni-td align="center">
						<view style="display: flex; justify-content: space-around; align-items: center">
							<span class="btn-span" style="color: rgba(21, 99, 232, 1);border: 1px solid rgba(21, 99, 232, 1);margin: 0 10px" @click="gather(item)">
								采集心电图
							</span>
							<span
								v-if="item.orderTime && item.orderTime.length > 0"
								class="btn-span"
								style="color: rgba(102, 68, 216, 1.0);border: 1px solid rgba(102, 68, 216, 1.0);"
								@click="revise(item)"
							>
								修改心电图
							</span>
						</view>
					</uni-td>
					<uni-td align="center">{{ item.idNum }}</uni-td>
					<uni-td align="center">{{ getSex(item.sex) }}</uni-td>
					<uni-td align="center">{{ item.age }}</uni-td>
					<uni-td align="center">{{ getNation(item.nation) }}</uni-td>
					<uni-td align="center">{{ item.screenPoint }}</uni-td>
					<uni-td align="center">{{ getFirstType(item.firstType) }}</uni-td>
					<uni-td align="center">{{ getMoreType(item) }}</uni-td>
				</uni-tr>
			</uni-table>
			<view class="uni-pagination-box">
				<uni-pagination
					show-icon
					:page-size="pageSize"
					:current="pageCurrent"
					:total="total"
					@change="change"
				/>
			</view>
		</view>

		<!-- 扫描弹框 -->
		<u-popup :show="show" mode="center" @close="close">
			<view class="injection">
				<view class="injection-tle">扫描选项</view>
				<u-radio-group style="margin-left: 10vw" v-model="scanVal" placement="column">
					<u-radio
						v-for="(item, index) in scanRadio"
						:key="index + 'a'"
						:label="item.label"
						:name="item.val"
						labelSize="20px"
						iconSize="23px"
						size="25px"
					></u-radio>
				</u-radio-group>
			</view>
			<view class="injection-btn">
				<up-button class="btn-1" text="取消" @click="close">取消</up-button>
				<up-button class="btn-2" text="确认" @click="okClick">确认</up-button>
			</view>
		</u-popup>
		<!--    流程弹框-->
		<u-popup :show="showProcess" mode="center" @close="closeProcess" :round="10">
			<h1 style="display: flex; justify-content: center; margin-top: 10rpx">查看不同人群的筛查流程</h1>
			<up-tabs
				:list="personTypeForProcess"
				@click="clickFlow"
				:activeStyle="{ fontSize: '20px' }"
				:inactiveStyle="{ fontSize: '20px' }"
				:lineWidth="lineWith"
			></up-tabs>
			<view style="padding: 30rpx">
				<view class="pro-text" v-if="tabIndex === 3">
					<span class="pro-title">密切接触者：</span>
					<span class="pro-main">症状筛查+PPD+胸片检查，异常或强阳性进行实验室检查;</span>
				</view>
				<view v-if="tabIndex === 0 || tabIndex === 5" style="display: flex; align-items: center">
					<span style="font-size: 22px">选择年龄段：</span>
					<up-radio-group v-model="studentType" placement="row" @change="studentTypeChange" :size="30">
						<up-radio
							style="margin-left: 70px"
							:labelSize="25"
							:iconSize="20"
							:customStyle="{ marginBottom: '8px' }"
							v-for="(item, index) in ageTypeList"
							:key="index"
							:label="item.name"
							:name="index"
						></up-radio>
					</up-radio-group>
				</view>
				<view v-if="tabIndex === 0">
					<view class="pro-text" v-if="tabIndex === 0 && studentType === 0">
						<span class="pro-title">0-5岁学生：</span>
						<span class="pro-main">症状筛查+查验卡痕，有症状做PPD，强阳性进一步检查;</span>
					</view>
					<view class="pro-text" v-if="tabIndex === 0 && studentType === 1">
						<span class="pro-title">6-14岁学生：</span>
						<span class="pro-main">症状筛查+PPD+查验卡痕，有症状或强阳性进一步检查;</span>
					</view>
					<view class="pro-text" v-if="tabIndex === 0 && studentType === 2">
						<span class="pro-title">≥15岁学生：</span>
						<span class="pro-main">症状筛查+PPD+查验卡痕+胸片检查，有症状或强阳性或异常进一步检查;</span>
					</view>
				</view>

				<view class="pro-text" v-if="tabIndex === 1">
					<span class="pro-title">教职工：</span>
					<span class="pro-main">症状筛查+胸片检查，有症状或异常进一步检查;</span>
				</view>
				<view class="pro-text" v-if="tabIndex === 2">
					<span class="pro-title">僧尼：</span>
					<span class="pro-main">
						0-5岁、6-14岁同学生；≥15岁症状筛查+查验卡痕+胸片检查，有症状或强阳性或异常进一步检查;
					</span>
				</view>
				<view class="pro-text" v-if="tabIndex === 4">
					<span class="pro-title">老年人、糖尿病患者、HIV/AIDS 和既往结核病患者：</span>
					<span class="pro-main">症状筛查+胸片检查，有症状或异常进一步检查;</span>
				</view>
				<view v-if="tabIndex === 5">
					<view class="pro-text" v-if="studentType === 0">
						<span class="pro-title">0-5岁非重点人群：</span>
						<span class="pro-main">症状筛查+查验卡痕，有症状做PPD，强阳性进一步检查;</span>
					</view>
					<view class="pro-text" v-if="studentType === 1">
						<span class="pro-title">6-14岁非重点人群：</span>
						<span class="pro-main">症状筛查+PPD+查验卡痕，有症状或强阳性进一步检查;</span>
					</view>
					<view class="pro-text" v-if="studentType === 2">
						<span class="pro-title">≥15岁非重点人群：</span>
						<span class="pro-main">症状筛查+胸片检查，有症状或异常进一步检查;</span>
					</view>
				</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
// 离线ocr插件
const ocrModule = uni.requireNativePlugin('YY-TomatoOCR');
const mpaasScanModule = uni.requireNativePlugin('Mpaas-Scan-Module');

// sqlite-manage 插件
import dbUtils from '../../../uni_modules/zjy-sqlite-manage/components/zjy-sqlite-manage/dbUtils';
import {
	dbName,
	getPatientPage,
	tbScreenElectrocardiogram,
	getElectrocardiogram,
	getElectrocardiogramDataView
} from '@/utils/sqlite';

// 复用采集组
import { ethnic } from '@/utils/dict.js';
import { items2, items3 } from '@/utils/dict.js';
import { personTypeForProcess } from '../../../utils/dict';

export default {
	data() {
		return {
			nav: [
				{
					value: '常规筛查'
				},
				{
					value: '心电图组',
					isActive: true
				}
			],
			personTypeForProcess,
			scanRadio: [
				{
					label: '待筛查人员二维码',
					val: 1
				},
				{
					label: '身份证',
					val: 2
				}
			],
			overflowVisible: false, // 初始时设置为 false
			scanVal: 1,
			title: '采集心电图',
			fileList1: [],
			show: false,
			gatherShow: false,
			// 查看筛查流程中的索引
			tabIndex: 0,
			// 学生类型单选值
			studentType: 0,
			ageTypeList: [{ name: '0-5岁' }, { name: '6-14岁' }, { name: '≥15岁' }],
			lineWith: 30,
			showProcess: false,
			// 每页数据量
			pageSize: 5,
			// 当前页
			pageCurrent: 1,
			loading: false,
			// 数据总量
			total: 0,
			dateRange: [],
			// 姓名
			searchName: '',
			pageData: [],
			localPath: '',

			// 民族列表
			ethnic: [{ value: 61, text: '汉族' }],
			// 筛查编号
			screenNum: '',
			secondType: [],
			firstType: [],
			// 身份证
			searchIdCard: null,
			// dataView
			dataView: {
				todayCount: 0,
				currentMonthCount: 0,
				currentYearCount: 0,
				TestResultData: {}
			}
		};
	},
	onLoad() {
  //   console.log('load')
	},
  onShow(){
    // console.log('show')
    this.ethnic = ethnic;
    this.search();
    this.dateScreening();
    this.getElectrocardiogramDataView();
    this.getNavItems(uni.$screenType)
  },
	mounted() {
    // console.log('mounted')
	},
	computed: {
		tableStyle() {
			return {
				overflow: this.overflowVisible ? 'visible' : 'auto'
			};
		}
	},
	methods: {
		getNavItems(screenType) {
		    switch (screenType) {
		        case 1:
		            this.nav = [
		                { value: '常规筛查' },
		                { value: '心电图组', isActive: true }
		            ];
		            break;
		        case 2:
		            this.nav = [
		                { value: '新生入学筛查' },
		                { value: '心电图组', isActive: true }
		            ];
		            break;
		        case 3:
		            this.nav = [
		                { value: '应急筛查' },
		                { value: '心电图组', isActive: true }
		            ];
		            break;
		        default:
		            this.nav
		            break;
		    }
		
		    return this.nav;
		},
		reviewProcess() {
			this.showProcess = true;
		},
		clickFlow(item) {
			this.tabIndex = item.index;
			switch (this.tabIndex) {
				case 0:
					this.lineWith = 30;
					break;
				case 1:
					this.lineWith = 40;
					break;
				case 2:
					this.lineWith = 30;
					break;
				case 3:
					this.lineWith = 70;
					break;
				case 4:
					this.lineWith = 380;
					break;
				case 5:
					this.lineWith = 85;
					break;
				default:
					this.lineWith = 30;
			}
		},
		// 获取统计数据
		getElectrocardiogramDataView() {
			const data = getElectrocardiogramDataView();
			data.then((res) => {
				if (res) {
					this.dataView.todayCount = res.todayCount[0].today_count;
					this.dataView.currentMonthCount = res.currentMonthCount[0].current_month_count;
					this.dataView.currentYearCount = res.currentYearCount[0].current_year_count;
					this.dataView.TestResultData = res.TestResultData[0];
				}
			});
		},
		toggleOverflow() {
			this.overflowVisible = !this.overflowVisible; // 切换 overflowVisible 的值
		},
		// 扫码模态框选中后确认
		okClick() {
			if (this.scanVal === 1) {
				mpaasScanModule.mpaasScan(
					{
						// 扫码识别类型，参数可多选，qrCode、barCode，不设置，默认识别所有
						scanType: ['qrCode', 'barCode'],
						// 是否隐藏相册，默认false不隐藏
						hideAlbum: false
					},
					(ret) => {
						if (ret.resp_message == 'success') {
							let data = ret.resp_result.split(';');
							let name = data[0].trim();
							this.searchName = name;
							this.show = false;
							this.search();
						} else {
							uni.showToast({
								icon: 'error',
								title: '扫码失败请重试'
							});
						}
					}
				);
			}
			if (this.scanVal === 2) {
				ocrModule.ocrAsyncFunc(
					{
						type: 'idcard',
						sourceType: ['camera', 'album'],
						showCorp: true
					},
					(ret) => {
						if (ret) {
							// 识别出身份证信息
							this.searchName = ret.result.name;
							this.searchIdCard = ret.result.number;
							// 关闭弹出层
							this.show = false;
							this.search();
						}
					}
				);
			}
		},
		// 时间
		handleSelectDate(value) {
			this.dateRange = value;
		},
		// 点击扫描
		scanner() {
			this.show = true;
		},
		open() {},
		close() {
			this.show = false;
		},
		closeProcess() {
			this.showProcess = false;
		},
		gather(item) {
			// 采集
			uni.navigateTo({
				url: '/pages/tb/ecgDetail/ecgDetail?item=' + encodeURIComponent(JSON.stringify(item)) + '&type=1'
			});
		},
		revise(item) {
			uni.navigateTo({
				url: '/pages/tb/ecgDetail/ecgDetail?item=' + encodeURIComponent(JSON.stringify(item)) + '&type=2'
			});
		},
		// 处理性别
		getSex(sex) {
			if (sex != null) {
				if (sex === 1) {
					return '女';
				} else {
					return '男';
				}
			}
			return '';
		},
		// 民族回显
		getNation(e) {
			const foundItem = this.ethnic.find((i) => i.value === e);
			if (foundItem) {
				return foundItem.text;
			}
			return '';
		},
		// 第一人群回显
		getFirstType(type) {
			switch (type) {
				case 1:
					return '重点人群';
				case 2:
					return '非重点人群';
				case 4:
					return '教职工';
				default:
					return '无';
			}
		},
		// 重点人群分类回显
		getMoreType(val) {
			let selectedOptions = [];
			if (val.moreType == 0 || val.moreType == 'NaN') {
				return '无';
			}

			if (val.firstType == 1) {
				items2.forEach((item) => {
					if (val.moreType & parseInt(item.value, 10)) {
						selectedOptions.push(item.text);
					}
				});
			}
			if (val.firstType == 4) {
				items3.forEach((item) => {
					if (val.moreType & parseInt(item.value, 10)) {
						selectedOptions.push(item.text);
					}
				});
			}

			let text = '';
			if (selectedOptions.length == 0) {
				text = '无';
			}
			selectedOptions.forEach((i) => {
				text = text + (text == '' ? '' : '/') + i;
			});

			return text;
		},
		// 搜索
		search() {
			this.getEcgDataPage();

			// 强制搜索后显示第一页
			let e = { type: 'current', current: 1 };
			this.change(e);
		},
		// 重置
		reset() {
			this.dateRange = [];
			this.$refs.componentDate.resetSelectDate();
			this.clearScreenNum();
			this.clearIdCard();
			this.clearName();
			this.secondType = [];
			this.firstType = [];
			this.pageCurrent = 1;
			this.search();
		},
		clearName() {
			this.searchName = '';
		},
		clearScreenNum() {
			this.screenNum = '';
		},
		clearIdCard() {
			this.searchIdCard = '';
		},
		// 获取心电图分页数据
		getEcgDataPage() {
			let promises = [
				getPatientPage(
					this.pageCurrent,
					this.pageSize,
					this.searchName,
					this.searchIdCard,
					this.firstType,
					this.secondType,
					this.dateRange[0],
					this.dateRange[1],
					this.screenNum,
					'lastCollectTime'
				),
				getElectrocardiogram()
			];

			Promise.all(promises)
				.then((results) => {
					// results 是一个包含两个 Promise 结果的数组
					let patientPageRes = results[0];
					this.total = patientPageRes.total;
					let collectRes = results[1];
					let result = patientPageRes.data.map((patient) => {
						let orderTime = collectRes
							.filter((screen) => screen.personId == patient.id)
							.map((screen) => {
								return {
									value: screen.screenOrder,
									text: `第${screen.screenOrder}次/${screen.screenTime.split(' ')[0]}`
								};
							});
						// 对orderTime进行排序
						orderTime.sort((a, b) => b.value - a.value);
						// 检查orderTime是否有元素
						let orderVal = orderTime.length > 0 ? orderTime[0].value : null;
						return {
							...patient,
							orderTime: orderTime,
							orderVal: orderVal // 获取排序后的第一个value
						};
					});
					this.pageData = result;
				})
				.catch((error) => {
					// 捕获可能的错误
					console.error('Error occurred:', error);
				});
		},
		// 分页触发
		change(e) {
			this.pageCurrent = e.current;

			this.getEcgDataPage();
		},
		// 处理数据
		dateScreening() {
			this.pageData.forEach((item) => {
				item.csrq = item.cs + '/' + item.rq;
			});
		}
	}
};
</script>

<style lang="scss" scoped>
.uni-table-tr {
    overflow: visible;
    background-color: #fff;

    ::v-deep .uni-table-td:first-child,.uni-table-th:first-child {
        position: sticky;
        left: 0;
        top: 0;
        background-color: #fff;
        z-index: 100;
    }
	::v-deep .uni-table-td:nth-child(2),.uni-table-th:nth-child(2) {
	    position: sticky;
	    left: 59px;
	    top: 0;
	    background-color: #fff;
	    z-index: 100;
	}
	::v-deep .uni-table-td:nth-child(3),.uni-table-th:nth-child(3) {
	    position: sticky;
	    left: 228px;
	    top: 0;
	    background-color: #fff;
	    z-index: 100;
	}
}

.top1 {
	display: flex;
	align-items: center;
	justify-content: space-between;
	.top1-left {
		margin: 10px;
	}
	.top1-right {
		display: flex;
		align-items: center;
		justify-content: flex-end;
		margin: 10px;
		.search-btn {
			display: flex;
			align-items: center;
			justify-content: center;
			.custom-add {
				background: rgba(28, 176, 117, 1);
			}
			.custom-sm {
				background: rgba(84, 141, 255, 1);
			}
		}
	}
}
.card {
	display: flex;
	align-items: center;
	.card-main {
		font-size: 18px;
		width: 370px;
		border-radius: 5px;
		margin: 0 10px;
		.card-top {
			width: 100%;
			padding:5px 20px;
		}
		.card-tle {
			display: flex;
			flex-direction: column;
			background-color: #fff;
			.card-sp {
				display: flex;
				align-items: center;
				padding: 5px 0;
				.card-text {
					margin-left: 20px;
					color: rgba(115, 115, 115, 1);
					.sp-text{
						display: inline-block;
						color: #000;
						margin-left: 10px;
						font-size: 24px;
					}
				}
				.card-text1 {
					margin-left: 60px;
					color: rgba(115, 115, 115, 1);
					.sp-text{
						display: inline-block;
						color: rgba(242, 58, 58, 1);
						margin-left: 10px;
						font-size: 24px;
					}
				}
			}
			.line {
				width: 320px;
				height: 1px;
				background-color: rgba(239, 239, 239, 1.0);
				margin-left: 15px;
			}
		}
		.various1 {
			background-color: #d2f0ff;
			color: rgba(0, 134, 207, 1);
		}
		.various2 {
			background-color: #e6e0ff;
			color: rgba(108, 86, 197, 1);
		}
		.various3 {
			background-color: #ffece3;
			color: rgba(240, 87, 53, 1);
		}
	}
}
.content {
	margin: 10px 10px 0 10px;
	background-color: #fff;
	.top {
		display: flex;
		align-items: center;
		justify-content: space-between;
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
		}
	}
	.statistics {
		display: flex;
		align-items: center;
		justify-content: space-around;
		.search-btn {
			display: flex;
			align-items: center;
			justify-content: center;
			margin: 0 20px;
			border-radius: 5px;
			.custom-reset {
				padding: 0 5px;
				color: #000;
				border: 1px solid rgba(207, 207, 207, 1);
				width: 90px;
			}
			.custom-search {
				margin-left: 45px;
				color: #fff;
				background: rgba(36, 93, 209, 1);
				border: 1px solid rgba(44, 98, 208, 1);
				padding: 0 5px;
				width: 90px;
			}
		}
	}
}
.uni-container {
	padding: 0 10px;
}
.btn-span {
	padding: 5px 10px;
	background-color: #fff;
	border-radius: 5px;
}
.injection {
	width: 35vw;
	.injection-tle {
		font-size: 18px;
		background: rgba(244, 247, 252, 1);
		padding: 10px;
	}
}
.injection-btn {
	display: flex;
	align-items: center;
	justify-content: space-evenly;
	padding: 10px 0;
	.btn-1 {
		border: 1px solid rgba(36, 93, 209, 1);
		color: rgba(36, 93, 209, 1);
		width: 14vw;
	}
	.btn-2 {
		background-color: rgba(36, 93, 209, 1);
		color: #fff;
		width: 14vw;
	}
}
.gather-open {
	padding: 5px 15px;
	display: flex;
	flex-direction: column;
	justify-content: space-around;
	align-items: center;
	.name-top {
		font-weight: 600;
		color: #66c7e8;
		display: flex;
		align-items: center;
		justify-content: space-around;
	}
	.scan-btn {
		border-top: 1px solid #000;
		padding-top: 10px;
		width: 300px;
		display: flex;
		align-items: center;
		justify-content: space-around;
		span {
			font-size: 16px;
		}
	}
}
.pro-text {
	font-size: 19px;
	.pro-title {
		font-weight: 800;
	}
}
</style>
