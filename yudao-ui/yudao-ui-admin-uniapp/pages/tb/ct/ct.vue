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
						@click="toAddPatient"
						:plain="true"
						class="custom-add"
						icon="plus"
						iconColor="#fff"
						color="#fff"
						text="新增患者"
					></up-button>
					<up-button
						@click="scanner"
						style="margin-left: 10px"
						class="custom-sm"
						icon="scan"
						iconColor="#fff"
						color="#fff"
						:plain="true"
						text="扫描搜索"
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
							今日筛查数
							<span class="sp-text">
								{{ timeCount.dayNum }}<span style="font-size: 18px;">人</span>
							</span>
						</view>
					</view>
					<view class="line"></view>
					<view class="card-sp">
						<view class="card-text">
							本月筛查数
							<span class="sp-text">
								{{ timeCount.monthNum }}<span style="font-size: 18px;">人</span>
							</span>
						</view>
					</view>
					<view class="line"></view>
					<view class="card-sp">
						<view class="card-text">
							本年筛查数
							<span class="sp-text">
								{{ timeCount.yearNum }}<span style="font-size: 18px;">人</span>
							</span>
						</view>
					</view>
				</view>
			</view>
			<view class="card-main">
				<view class="card-top various2">类型统计</view>
				<view class="card-tle">
					<view class="card-sp">
						<view class="card-text">
							疑似结核人数
							<span class="sp-text">
								{{ count.suspected }}<span style="font-size: 18px;">人</span>
							</span>
						</view>
					</view>
					<view class="line"></view>
					<view class="card-sp">
						<view class="card-text">
							无异常人数
							<span class="sp-text">
								{{ count.normal }}<span style="font-size: 18px;">人</span>
							</span>
						</view>
					</view>
					<view class="line"></view>
					<view class="card-sp">
						<view class="card-text">
							总人数
							<span class="sp-text">
								{{ count.suspected + count.normal }}<span style="font-size: 18px;">人</span>
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
						v-model="queryParam.screenId"
						clearButton="always"
						placeholder="筛查编号"
						style="width: 245px"
						cancelButton="none"
						@clear="queryParam.screenId = ''"
					></uni-search-bar>
					<span style="font-size: 18px; display: inline-block; margin-left: 55px">身份证号</span>
					<uni-search-bar
						:focus="false"
						class="top-search"
						v-model="queryParam.idNum"
						clearButton="always"
						placeholder="身份证号"
						style="width: 230px"
						cancelButton="none"
						@clear="queryParam.idNum = ''"
					></uni-search-bar>
					<span style="font-size: 18px; display: inline-block; margin-left: 55px">姓名</span>
					<uni-search-bar
						:focus="false"
						class="top-search"
						v-model="queryParam.name"
						clearButton="always"
						placeholder="患者姓名"
						style="width: 165px"
						cancelButton="none"
						@clear="queryParam.name = ''"
					></uni-search-bar>
				</view>
			</view>
			<view class="statistics" style="justify-content: flex-start">
				<span style="font-size: 18px; margin-left: 10px">筛查日期</span>
				<select-date style="margin-left: 10px" ref="componentDate" @selectDate="handleSelectDate" />
				<span style="font-size: 18px; display: inline-block; margin-left: 60px">人群分类</span>
				<select-crowd
					style="margin-left: 15px"
					@updateValues="handleUpdateValues"
				/>
				<view class="search-btn">
					<up-button @click="handleSearch" :plain="true" class="custom-search" text="搜索"></up-button>
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
			<up-line style="margin-top: 5px;"></up-line>
		</view>

		<!-- 患者列表 -->
		<view class="uni-container">
			<uni-table :style="tableStyle" ref="table" :loading="loading"  stripe emptyText="暂无更多数据">
				<uni-tr>
					<uni-th width="60" align="center">序号</uni-th>
					<uni-th width="110" align="center">筛查编号</uni-th>
					<uni-th width="100" align="center">姓名</uni-th>
					<uni-th width="180" align="center">筛查次序/时间</uni-th>
					<uni-th width="150" align="center">检测结果</uni-th>
					<uni-th width="180" align="center">下一步检查</uni-th>
					<uni-th width="350" align="center">操作</uni-th>
					<uni-th width="180" align="center">身份证号</uni-th>
					<uni-th width="80" align="center">年龄</uni-th>
					<uni-th width="100" align="center">民族</uni-th>
					<uni-th width="180" align="center">第一人群分类</uni-th>
					<uni-th width="160" align="center">重点人群分类</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in list" :key="index">
					<uni-td align="center">{{ index + 1 }}</uni-td>
					<uni-td align="center">{{ item.screenId }}</uni-td>
					<uni-td align="center">{{ item.name }}</uni-td>
					<uni-td align="center">
						<view>
							<uni-data-select
								v-model="item.screenOrder"
								:localdata="item.localData"
								placeholder="选择筛查次序"
								:clear="false"
								placement="top"
								@change="selectHandle(index)"
								@click="toggleOverflow"
							></uni-data-select>
						</view>
					</uni-td>
					<uni-td align="center">{{ getLabelByValue(ctOutcome(), item.outcome) }}</uni-td>
					<uni-td align="left">
						<view v-if="checkLength(item.next)">{{ item.next }}</view>
						<view v-else>
							<up-text text="点击查看" type="success" @click="clickTextHandler(item.next)"></up-text>
						</view>
					</uni-td>
					<uni-td align="center">
						<view style="display: flex; justify-content: space-around; align-items: center">
							<span class="btn-span" style="color: rgba(223, 65, 65, 1);border: 1px solid rgba(223, 65, 65, 1);" @click="navTo(item, getItemByLabel(state(), '修改'))">修改</span>
							<span
								class="btn-span"
								style="color: rgba(21, 99, 232, 1);border: 1px solid rgba(21, 99, 232, 1);margin: 0 10px"
								@click="navTo(item, getItemByLabel(state(), '新增'))"
							>
								采集
							</span>
							<span class="btn-span" style="color:  rgba(51, 176, 19, 1);border: 1px solid  rgba(51, 176, 19, 1);" @click="navTo(item, getItemByLabel(state(), '详情'))">详情</span>
							<span class="btn-span" style="color: rgba(102, 68, 216, 1.0);border: 1px solid rgba(102, 68, 216, 1.0);margin: 0 5px;" @click="revise(item)">
								修改患者信息
							</span>
						</view>
					</uni-td>
					<uni-td align="center">{{ item.idNum }}</uni-td>
					<uni-td align="center">{{ item.age }}</uni-td>
					<uni-td align="center">{{ getValueByKey(nationMap(), item.nation) }}</uni-td>
					<uni-td align="center">
						{{ getLabelByValue(firstType(), item.firstType) }}
					</uni-td>
					<uni-td align="center">
						{{ showMoreTypeLabel(item.moreType) }}
					</uni-td>
				</uni-tr>
			</uni-table>
			<view class="uni-pagination-box">
				<uni-pagination
					show-icon
					:page-size="pageParams.pageSize"
					:current="pageParams.pageNo"
					:total="total"
					@change="change"
				/>
			</view>
		</view>
		<!--    扫描弹框-->
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
					<up-radio-group v-model="studentType" placement="row" :size="30">
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
		<!--    下一步检查弹框-->
		<u-popup :show="showNextText" mode="center" @close="showNextText = false" :round="10" style="height: 100rpx">
			<view style="height: 280rpx; padding: 30rpx">
				<b style="display: flex; justify-content: center; margin-top: 10rpx">
					查看该待筛查人员不同身份的下一步检查
				</b>
				<view v-for="text in nextText">
					{{ text }}
				</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
import { getOutcomeByOrder } from '../../../api/screen/sputumExamination';

const ocrModule = uni.requireNativePlugin('YY-TomatoOCR');
const mpaasScanModule = uni.requireNativePlugin('Mpaas-Scan-Module');
import { count, dbName, tbScreenChestRadiograph } from '../../../utils/sqlite';
import {
	state,
	firstType,
	ctOutcome,
	nationMap,
	getItemByLabel,
	sex,
	getLabelByValue,
	getValueByKey,
	moreType,
	personTypeForProcess
} from '../../../utils/dict';
import { chronic, studentFifteen, studentFive, personType } from '../../../utils/dictData';
import UniPopupDialog from '../../../uni_modules/uni-popup/components/uni-popup-dialog/uni-popup-dialog.vue';
import UniPopup from '../../../uni_modules/uni-popup/components/uni-popup/uni-popup.vue';
import UPopup from '../../../uni_modules/uview-plus/components/u-popup/u-popup.vue';
import UniTable from '../../../uni_modules/uni-table/components/uni-table/uni-table.vue';
import UniForms from '../../../uni_modules/uni-forms/components/uni-forms/uni-forms.vue';
import UniFormsItem from '../../../uni_modules/uni-forms/components/uni-forms-item/uni-forms-item.vue';
import { splitDecimalIntoList, getPatientPage } from '../../../utils/sqlite';
import { currentDayMonthYearStatistics, getOrderAndTime, getTypeStatistics } from '../../../api/screen/dr';
import { getAll } from '../../../api/screen/img';
import { parsePatientType, parseNext } from '../../../utils/common';

export default {
	components: { UniFormsItem, UniForms, UniTable, UPopup, UniPopup, UniPopupDialog },
	data() {
		return {
			nav: [
				{
					value: '常规筛查'
				},
				{
					value: 'DR管理',
					isActive: true
				}
			],
			studentFifteen,
			personType,
			personTypeForProcess,
			timeCount: { yearNum: 0, monthNum: 0, dayNum: 0 },
			count: { normal: 0, suspected: 0 },
			dateRange: [],
			overflowVisible: false, // 初始时设置为 false
			addUrl: '/pages/tb/addCt/addCt',
			dialogShow: false,
			// 弹框信息
			dialogInfo: {},
			// 患者列表
			list: [],
			// 列表加载
			loading: false,
			// 分页总数量
			total: 0,
			// 分页查询参数
			pageParams: {
				pageSize: 5,
				pageNo: 1
			},
			queryParam: {
				name: null,
				idNum: null,
				firstType: [],
				moreType: [],
				screenId: null,
				startTime: null,
				endTime: null
			},
			// 扫描弹框的页面数据
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
			scanVal: 1,
			// 扫描弹框的展示
			show: false,
			// 查看筛查流程中的索引
			tabIndex: 0,
			// 学生类型单选值
			studentType: 0,
			ageTypeList: [{ name: '0-5岁' }, { name: '6-14岁' }, { name: '≥15岁' }],
			lineWith: 30,
			showProcess: false,
			showNextText: false,
			nextText: []
		};
	},
	onLoad() {
		this.getNavItems(uni.$screenType)
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
		                { value: 'DR管理', isActive: true }
		            ];
		            break;
		        case 2:
		            this.nav = [
		                { value: '新生入学筛查' },
		                { value: 'DR管理', isActive: true }
		            ];
		            break;
		        case 3:
		            this.nav = [
		                { value: '应急筛查' },
		                { value: 'DR管理', isActive: true }
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
							this.queryParam.name = name;
							this.show = false;
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
							this.queryParam.name = ret.result.name;
							this.queryParam.idNum = ret.result.number;
							// 关闭弹出层
							this.show = false;
						}
					}
				);
			}
		},
		parsePatientType,
		/**
		 * 展示
		 * @param map
		 * @param key
		 * @returns {string}
		 */
		showNext(value, item) {
			if (item.outcome == 0) {
				return '';
			}
			let str = '';
			if (value && Array.isArray(value)) {
				str = value.join(' | ');
			}
			// console.log(`${key}-${str}`)
			return str;
		},
		moreType() {
			return moreType;
		},
		// 时间
		handleSelectDate(value) {
			this.dateRange = [];
			for (let i = 0; i < 2; i++) {
				let split = value[i].split(' ');
				this.dateRange.push(split[0]);
			}
			this.queryParam.startTime = this.dateRange[0];
			this.queryParam.endTime = this.dateRange[1];
			// console.log('query=',this.queryParam)
		},
		selectHandle(index) {
			getOutcomeByOrder(
				this.list[index].screenOrder,
				this.list[index].id,
				this.list[index].year,
				tbScreenChestRadiograph
			).then((res) => {
				this.list[index].outcome = res;
			});
		},
		toggleOverflow() {
			this.overflowVisible = !this.overflowVisible; // 切换 overflowVisible 的值
		},
		firstType() {
			return firstType;
		},
		state() {
			return state;
		},
		sex() {
			return sex;
		},
		ctOutcome() {
			return ctOutcome;
		},
		nationMap() {
			return nationMap;
		},
		getAll,
		getItemByLabel,
		getLabelByValue,
		getOrderAndTime,
		getTypeStatistics,
		currentDayMonthYearStatistics,
		getValueByKey,
		splitDecimalIntoList,
		navTo(param, dictItem) {
			if (dictItem.label != '新增') {
				if (!param.screenOrder) {
					uni.$u.toast('该患者还未拍过胸片，请先拍胸片！');
					return;
				}
			}
			// 需要传递患者信息
			uni.navigateTo({
				url: '/pages/tb/addCt/addCt?val=' + JSON.stringify(param) + '&label=' + dictItem.label
			});
		},
		// 修改患者信息
		revise(val) {
			uni.navigateTo({
				url: '/pages/tb/addPatient/index?val=' + JSON.stringify(val) + '&isNew=' + false + '&flag=4'
			});
		},
		clickTextHandler(text) {
			this.showNextText = true;
			// 将text字符串对;做分割形成数组
			this.nextText = text.split(';');
		},
		checkLength(text) {
			return (text + '').length < 35;
		},
		showMoreTypeLabel(type) {
			if (!type || type.length == 0) {
				return '';
			}
			let valueList = splitDecimalIntoList(type);
			// console.log('valueList=',valueList)
			// console.log(`第一个${type}分解后为`+valueList)
			let labels = '';
			if (valueList && valueList.length > 0)
				valueList.forEach((e) => {
					let label = getLabelByValue(moreType, e);
					labels += label + '/';
				});
			if (labels.endsWith('/')) {
				labels = labels.slice(0, -1);
			}
			// console.log("label=",labels)
			return labels;
		},
		// 搜索重置
		reset() {
			this.dateRange = [];
			this.$refs.componentDate.resetSelectDate();
			this.queryParam.name = null;
			this.queryParam.idNum = null;
			this.queryParam.type = null;
			this.queryParam.screenId = null;
			this.queryParam.startTime = null;
			this.queryParam.endTim = null;
			this.queryParam.firstType = [];
			this.queryParam.moreType = [];
			this.handleSearch();
		},
		handleSearch() {
			let e = { type: 'current', current: 1 };
			this.change(e);
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
		// 查询患者分页
		async getPatientList() {
			// 群众分页
			let data = await getPatientPage(
				this.pageParams.pageNo,
				this.pageParams.pageSize,
				this.queryParam.name,
				this.queryParam.idNum,
				this.queryParam.firstType,
				this.queryParam.moreType,
				this.queryParam.startTime,
				this.queryParam.endTime,
				this.queryParam.screenId,
				'lastChestRadiographTime'
			);
			this.list = data.data;
			this.total = data.total;

			// console.log("list=",this.list)
			// 下拉框
			for (const item of this.list) {
				let data = [];
				let orderAndTime = await getOrderAndTime(item.id);
				for (const e of orderAndTime) {
					e.value = e.screenOrder;
					// 如果e.screenTime 字符串包含时分秒 如 第一次/2024-07-14 10:00:00，则通过正则表达式去掉时分秒,剩下第一次/2024-07-14
					if (e.screenTime && e.screenTime.includes(':')) {
						let reg = /(\d{4}-\d{2}-\d{2})/g;
						let match = e.screenTime.match(reg);
						if (match && match.length > 0) {
							e.screenTime = match[0];
						}
					}

					e.text = '第' + e.screenOrder + '次/' + e.screenTime;
					data.push(e);
				}
				if (orderAndTime && orderAndTime.length > 0) {
					item.outcome = orderAndTime[orderAndTime.length - 1].outcome;
				} else {
					item.outcome = '';
				}
				item.localData = data;
				item.screenOrder = '';
				if (data.length > 0) {
					item.screenOrder = data.length;
				}
				let map = parsePatientType(
					item.age,
					splitDecimalIntoList(item.moreType),
					item.firstType,
					item.name,
					item.curFinish,
					false
				);
				if (map && map[0]) {
					item.next = '';
					for (let i = 0; i < map.length; i++) {
						item.next += parseNext(item.curFinish, personType.get(map[i]), item.outcome !== 0) + ';';
					}
				}
			}
			// console.log('list=', this.list);
		},
		handleUpdateValues(val) {
			this.queryParam.firstType = val.firstVal ?? [];
			this.queryParam.moreType = val.secondVal ?? [];
			// console.log("query=",this.queryParam)
		},
		// 跳转到新增患者
		toAddPatient() {
			uni.navigateTo({
				url: '/pages/tb/addPatient/index?flag=4'
			});
		},
		// 分页触发
		async change(e) {
			this.pageParams.pageNo = e.current;
			await this.getPatientList();
		},
		// 扫描弹框
		close() {
			this.show = false;
		},
		closeProcess() {
			this.showProcess = false;
		},
		// 扫描
		scanner() {
			this.show = true;
		},
		async sta() {
			let data = await getTypeStatistics();
			this.count.normal = data.normal;
			this.count.suspected = data.suspected;
		},
		async getTimeSta() {
			let data = await currentDayMonthYearStatistics();
			this.timeCount = data;
		}
	},
	async onShow() {
		await this.getPatientList();
		await this.sta();
		await this.getTimeSta();
		let imgs = await getAll();
		// console.log("imgs=",imgs)
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
	    left: 58px;
	    top: 0;
	    background-color: #fff;
	    z-index: 100;
	}
	::v-deep .uni-table-td:nth-child(3),.uni-table-th:nth-child(3) {
	    position: sticky;
	    left: 225px;
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
