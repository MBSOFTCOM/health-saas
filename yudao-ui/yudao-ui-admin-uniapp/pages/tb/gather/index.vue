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
						@click="scannerPop"
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
								{{ statisticsData.dayNum }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
						<view class="card-text1">
							异常
							<span class="sp-text">
								{{ statisticsData.dayAbnormalNum }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
					<view class="line"></view>
					<view class="card-sp">
						<view class="card-text">
							本月筛查数
							<span class="sp-text">
								{{ statisticsData.monthlyNum }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
						<view class="card-text1">
							异常
							<span class="sp-text">
								{{ statisticsData.monthlyAbnormalNum }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
					<view class="line"></view>
					<view class="card-sp">
						<view class="card-text">
							本年筛查数
							<span class="sp-text">
								{{ statisticsData.yearNum }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
						<view class="card-text1">
							异常
							<span class="sp-text">
								{{ statisticsData.yearAbnormalNum }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
				</view>
			</view>
			<view class="card-main">
				<view class="card-top various2">卡痕人数统计</view>
				<view class="card-tle">
					<view class="card-sp">
						<view class="card-text">
							有卡痕人数
							<span class="sp-text">
								{{ statisticsData.CardMark }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
					<view class="line"></view>
					<view class="card-sp">
						<view class="card-text">
							无卡痕人数
							<span class="sp-text">
								{{ statisticsData.noCardMark }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
					<view class="line"></view>
					<view class="card-sp">
						<view class="card-text">
							总人数
							<span class="sp-text">
								{{ statisticsData.noCardMark + statisticsData.CardMark }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
				</view>
			</view>
			<view class="card-main">
				<view class="card-top various3">症状人数统计</view>
				<view class="card-tle">
					<view class="card-sp">
						<view class="card-text">
							咳血、血痰
							<span class="sp-text">
								{{ statisticsData.hemoptysisNum }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
						<view class="card-text1">
							夜间盗汗
							<span class="sp-text" style="color: #000">
								{{ statisticsData.nightSweatNum }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
					<view class="line"></view>
					<view class="card-sp">
						<view class="card-text">
							发热
							<span class="sp-text">
								{{ statisticsData.feverNum }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
						<view class="card-text1" style="margin-left: 110px">
							食欲不振
							<span class="sp-text" style="color: #000">
								{{ statisticsData.anorexiaNum }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
					<view class="line"></view>
					<view class="card-sp">
						<view class="card-text">
							乏力
							<span class="sp-text">
								{{ statisticsData.weakNum }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
						<view class="card-text1" style="margin-left: 110px">
							胸痛
							<span class="sp-text" style="color: #000">
								{{ statisticsData.chestPainNum }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
				</view>
			</view>
		</view>
		<view class="content" >
			<!-- 顶部搜索栏 -->
			<view class="top">
				<view class="top-left">
					<span style="font-size: 18px">筛查编号</span>
					<uni-search-bar
						@confirm="search"
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
						@confirm="search"
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
						@confirm="search"
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
			<!-- 输入框和搜索按钮 -->
			<view class="statistics" style="justify-content: flex-start;margin-bottom: 5px;">
				<span style="font-size: 18px; margin-left: 10px">筛查日期</span>
				<select-date style="margin-left: 10px" ref="componentDate" @selectDate="handleSelectDate" />
				<span style="font-size: 18px; display: inline-block; margin-left: 60px">人群分类</span>
				<select-crowd
					style="margin-left: 15px"
					@updateValues="handleUpdateValues"
					:is-visible="showSelectCrowd"
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
						style="width: 115px"
						type="primary"
						:plain="true"
						text="查看筛查流程"
					></up-button>
				</view>
			</view>
			<up-line></up-line>
		</view>
		<!-- 患者列表 -->
		<view class="uni-container">
			<uni-table :style="tableStyle" ref="table" :loading="loading" stripe emptyText="暂无更多数据">
				<uni-tr>
					<uni-th width="60" align="center">序号</uni-th>
					<uni-th width="110" align="center">筛查编号</uni-th>
					<uni-th width="100" align="center">姓名</uni-th>
					<uni-th width="180" align="center">筛查次序/时间</uni-th>
					<uni-th width="180" align="center">下一步检查</uni-th>
					<uni-th width="220" align="center">采集操作</uni-th>
					<uni-th width="180" align="center">身份证号</uni-th>
					<uni-th width="60" align="center">性别</uni-th>
					<uni-th width="60" align="center">年龄</uni-th>
					<uni-th width="60" align="center">民族</uni-th>
					<uni-th width="150" align="center">筛查点</uni-th>
					<uni-th width="120" align="center">第一人群分类</uni-th>
					<uni-th width="120" align="center">重点人群分类</uni-th>
					<uni-th width="250" align="center">患者信息操作</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in pageData" :key="index">
					<uni-td align="center">{{ index + 1 }}</uni-td>
					<uni-td align="center">{{ item.screenId }}</uni-td>
					<uni-td align="center">{{ item.name }}</uni-td>
					<uni-td align="center">
						<view>
							<uni-data-select
								v-model="item.orderVal"
								:localdata="item.orderTime"
								@change="changeOrder(item)"
								placeholder="选择筛查次序"
								:clear="false"
								placement="top"
								@click="toggleOverflow"
							></uni-data-select>
						</view>
					</uni-td>
					<uni-td align="left">
						<view v-if="checkLength(item.next)">{{ item.next }}</view>
						<view v-else>
							<up-text text="点击查看" type="success" @click="clickTextHandler(item.next)"></up-text>
						</view>
					</uni-td>
					<uni-td align="center">
						<view style="display: flex; justify-content: space-around; align-items: center">
							<span
								class="btn-span"
								style="color: rgba(223, 65, 65, 1); border: 1px solid rgba(223, 65, 65, 1)"
								@click="uGather(item)"
							>
								修改
							</span>
							<span
								class="btn-span"
								style="
									color: rgba(21, 99, 232, 1);
									border: 1px solid rgba(21, 99, 232, 1);
									margin: 0 10px;
								"
								@click="gather(item)"
							>
								新增
							</span>
							<span
								class="btn-span"
								style="color: rgba(51, 176, 19, 1); border: 1px solid rgba(51, 176, 19, 1)"
								@click="detail(item)"
							>
								详情
							</span>
						</view>
					</uni-td>
					<uni-td align="center">{{ item.idNum }}</uni-td>
					<uni-td align="center">{{ getSex(item.sex) }}</uni-td>
					<uni-td align="center">{{ item.age }}</uni-td>
					<uni-td align="center">{{ get(item.nation) }}</uni-td>
					<uni-td align="center">{{ item.screenPoint }}</uni-td>
					<uni-td align="center">{{ getFirstType(item.firstType) }}</uni-td>
					<uni-td align="center">{{ getMoreType(item) }}</uni-td>
					<uni-td align="center">
						<div style="display: flex; justify-content: space-around; align-items: center">
							<span
								class="btn-span"
								style="color: rgba(223, 65, 65, 1); border: 1px solid rgba(223, 65, 65, 1)"
								@click="revise(item)"
							>
								修改
							</span>
							<span
								class="btn-span"
								style="
									color: rgba(240, 163, 41, 1);
									border: 1px solid rgba(240, 163, 41, 1);
									margin: 0 10px;
								"
								@click="inspections(item)"
							>
								查看检验单
							</span>
							<span
								class="btn-span"
								style="color: rgba(80, 104, 242, 1); border: 1px solid rgba(80, 104, 242, 1)"
								@click="print(item)"
							>
								打印
							</span>
						</div>
					</uni-td>
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
		<u-popup :show="show" mode="center" @close="close" @open="open">
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
		<u-popup :show="showGather" :closeOnClickOverlay="false" mode="center" @close="closeGather">
			<view class="gather-open">
				<view style="display: flex; flex-direction: column; align-items: center; justify-content: center">
					<view v-for="(row, rowI) in modules" :key="rowI" style="display: flex; flex-direction: row">
						<view v-for="(col, colI) in row" :key="colI">
							<view v-if="col.isBlack" style="width: 5px; height: 5px; background-color: black">
								<!-- 黑色码点 -->
							</view>
							<view v-else style="width: 5px; height: 5px; background-color: white">
								<!-- 白色码点 -->
							</view>
						</view>
					</view>
					<view style="font-size: 18px; font-weight: 600; margin-top: 10px">{{ this.prtScreenId }}</view>
				</view>
				<view class="scan-btn">
					<up-button style="width: 60px" text="取消" @click="closeGather"></up-button>
					<up-button type="primary" style="width: 60px" text="打印"></up-button>
				</view>
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
		<!--    下一步检查弹框-->
		<u-popup :show="showNextText" mode="center" @close="showNextText = false" :round="10" style="height: 100rpx">
			<view style="height: 280rpx; padding: 30rpx">
				<b style="display: flex; justify-content: center; margin-top: 10rpx">
					查看该待筛查人员不同身份的下一步检查
				</b>
				<view v-for="(text, index) in nextText" :key="index + 'a'">
					{{ text }}
				</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
const ocrModule = uni.requireNativePlugin('YY-TomatoOCR');
const mpaasScanModule = uni.requireNativePlugin('Mpaas-Scan-Module');
import UQRCode from '../../../uni_modules/Sansnn-uQRCode/js_sdk/uqrcode/uqrcode.js';
import dbUtils from '../../../uni_modules/zjy-sqlite-manage/components/zjy-sqlite-manage/dbUtils';
import { parsePatientType, parseNext } from '../../../utils/common';
import { splitDecimalIntoList, getCollectOen } from '@/utils/sqlite.js';
import { personType } from '../../../utils/dictData';
import {
	dbName,
	tbScreenPerson,
	getPatientPage,
	getCollectByPatientId,
	getCollect,
	statisticsNum,
	inList
} from '@/utils/sqlite';
import { items1, items2, items3, personTypeForProcess } from '@/utils/dict.js';
import { districtInitSql } from '@/utils/districtInitSql.js';
import { ethnic } from '@/utils/dict.js';
export default {
	data() {
		return {
			nav: [
				{
					value: '常规筛查'
				},
				{
					value: '采集组',
					isActive: true
				}
			],
			personTypeForProcess,
			personType,
			showSelectCrowd: false, // 控制子组件显示的布尔值
			overflowVisible: false, // 初始时设置为 false
			modules: [],
			// 查看筛查流程中的索引
			tabIndex: 0,
			// 学生类型单选值
			studentType: 0,
			ageTypeList: [{ name: '0-5岁' }, { name: '6-14岁' }, { name: '≥15岁' }],
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
			//民族列表
			ethnic: [{ value: 61, text: '汉族' }],
			scanVal: 1,
			show: false,
			showGather: false,
			showProcess: false,
			// 每页数据量
			pageSize: 5,
			// 当前页
			pageCurrent: 1,
			loading: false,
			// 数据总量
			total: 0,
			// 筛查编号
			screenNum: '',
			dateRange: [],
			// 姓名
			searchName: '',
			// 身份证
			searchIdCard: null,
			// 人群分类
			searchClassify: '',
			pageData: [],
			// 筛查条件下面的统计值
			statisticsData: {},
			// 打印筛查编号
			prtScreenId: null,
			firstType: [],
			secondType: [],
			showNextText: false,
			nextText: []
		};
	},
	computed: {
		tableStyle() {
			return {
				overflow: this.overflowVisible ? 'visible' : 'auto'
			};
		}
	},
	onLoad() {
		this.ethnic = ethnic;
		this.dateScreening();
		this.search();
		this.statisticalMethod();
		this.getNavItems(uni.$screenType);
	},
	onShow() {
		this.search();
		this.statisticalMethod();
	},
	methods: {
		getNavItems(screenType) {
			switch (screenType) {
				case 1:
					this.nav = [{ value: '常规筛查' }, { value: '采集组', isActive: true }];
					break;
				case 2:
					this.nav = [{ value: '新生入学筛查' }, { value: '采集组', isActive: true }];
					break;
				case 3:
					this.nav = [{ value: '应急筛查' }, { value: '采集组', isActive: true }];
					break;
				default:
					this.nav;
					break;
			}

			return this.nav;
		},
		reviewProcess() {
			this.showProcess = true;
		},
		toggleOverflow() {
			this.overflowVisible = !this.overflowVisible; // 切换 overflowVisible 的值
		},
		async changeOrder(e) {
			const res = await getCollectOen(e.id, e.orderVal, uni.$person.year, uni.$screenType);
			let isBad = true;
			if (res.length > 0) {
				const outcomeStr = res[0].outcome.toString();
				const contains1to8 = /[1-8]/.test(outcomeStr);
				const contains9 = /[9]/.test(outcomeStr);
				// 如果包含1-8中的数字，但不包含9，则返回false，表示正常
				isBad = !(contains1to8 || !contains9);
			}
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
							this.searchName = name;
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
							this.searchName = ret.result.name;
							this.searchIdCard = ret.result.number;
							// 关闭弹出层
							this.show = false;
						}
					}
				);
			}
		},

		open() {},
		close() {
			this.show = false;
		},
		closeGather() {
			this.showGather = false;
		},
		studentTypeChange(e) {
			console.log(e);
			console.log(this.studentType);
		},
		closeProcess() {
			this.showProcess = false;
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
		// 新增采集
		gather(val) {
			// console.log(val);
			// 需要传递患者信息
			uni.navigateTo({
				url:
					'/pages/tb/collectSymptoms/index?age=' +
					val.age +
					'&name=' +
					val.name +
					'&idNum=' +
					val.idNum +
					'&screenId=' +
					val.screenId +
					'&id=' +
					val.id +
					'&isNew=' +
					1 +
					'&isNewStudent=' +
					val.isNewStudent +
					'&year=' +
					val.year
			});
		},
		uGather(val) {
			// console.log("val",val);
			if (val.orderTime.length == 0) {
				uni.$u.toast('该人员还未进行过采集，请先采集！');
				return;
			}
			// 首先找到 orderVal 对应的索引
			const index = val.orderVal - 1; // 因为索引是从 0 开始的，而 orderVal 是从 1 开始的，所以要减 1
			// 然后根据索引获取对应的日期值
			const dateValue = val.orderTime[index].text.split('/')[1]; // 日期值是在文本中的第二部分
			// 需要传递患者信息
			uni.navigateTo({
				url:
					'/pages/tb/collectSymptoms/index?age=' +
					val.age +
					'&name=' +
					val.name +
					'&idNum=' +
					val.idNum +
					'&screenId=' +
					val.screenId +
					'&id=' +
					val.id +
					'&order=' +
					val.orderVal +
					'&screenTime=' +
					dateValue +
					'&isNewStudent=' +
					val.isNewStudent +
					'&isNew=' +
					0 +
					'&year=' +
					val.year
			});
		},
		detail(val) {
			if (val.orderTime.length == 0) {
				uni.$u.toast('该人员还未进行过采集，请先采集！');
				return;
			}
			// 首先找到 orderVal 对应的索引
			const index = val.orderVal - 1; // 因为索引是从 0 开始的，而 orderVal 是从 1 开始的，所以要减 1
			// 然后根据索引获取对应的日期值
			const dateValue = val.orderTime[index].text.split('/')[1]; // 日期值是在文本中的第二部分
			//跳转至详情页面,并传输部分数据
			uni.navigateTo({
				url:
					'/pages/tb/collectSymptoms/details?age=' +
					val.age +
					'&name=' +
					val.name +
					'&idNum=' +
					val.idNum +
					'&screenId=' +
					val.screenId +
					'&id=' +
					val.id +
					'&order=' +
					val.orderVal +
					'&screenTime=' +
					dateValue +
					'&isNewStudent=' +
					val.isNewStudent +
					'&doctorSignature=' +
					val.doctorSignature +
					'&year=' +
					val.year
			});
		},
		// 查看检验单
		inspections(val) {
			uni.navigateTo({
				url: '/pages/tb/MedicalExaminationForm/MedicalExaminationForm?val=' + JSON.stringify(val)
			});
		},
		// 修改患者信息
		revise(val) {
			// console.log('修改患者', val);
			uni.navigateTo({
				url: '/pages/tb/addPatient/index?val=' + JSON.stringify(val) + '&isNew=' + false + '&flag=1'
			});
		},
		// 打印
		print(val) {
			this.prtScreenId = val.screenId;
			const qr = new UQRCode();
			qr.data = val.name + ';' + val.idNum + ';' + val.screenId;
			qr.make();
			this.modules = qr.modules;
			this.showGather = true;
		},
		// 扫描
		scannerPop() {
			this.show = true;
		},

		// 时间
		handleSelectDate(value) {
			this.dateRange = value;
		},
		handleUpdateValues(val) {
			this.firstType = val.firstVal;
			this.secondType = val.secondVal;
		},
		// 搜索
		handleSearch() {
			this.pageCurrent = 1;
			this.search();
		},
		clickTextHandler(text) {
			this.showNextText = true;
			// 将text字符串对;做分割形成数组
			this.nextText = text.split(';');
		},
		checkLength(text) {
			return (text + '').length < 35;
		},
		search() {
			// 创建一个 Promise 对象数组
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
				getCollect()
			];

			Promise.all(promises)
				.then(async (results) => {
					// results 是一个包含两个 Promise 结果的数组
					let patientPageRes = results[0];
					this.total = patientPageRes.total;
					let collectRes = results[1];

					// 使用 Promise.all 来处理数组中的每个元素
					let resultPromises = patientPageRes.data.map(async (patient) => {
						let orderTime = collectRes
							.filter((screen) => screen.personId == patient.id)
							.map((screen) => {
								return {
									value: screen.screenOrder,
									text: `第${screen.screenOrder}次/${screen.screenTime.split(' ')[0]}`
								};
							})
							.sort((a, b) => b.value - a.value); // 对orderTime进行排序

						let orderVal = orderTime.length > 0 ? orderTime[0].value : null;

						// 先假设isBad为true，等待getCollectOen确定最终值
						let isBad = true;
						try {
							// 等待getCollectOen Promise解决
							const res = await getCollectOen(patient.id, orderVal, uni.$person.year, uni.$screenType);
							// console.log(res);
							if (res.length > 0) {
								const outcomeStr = res[0].outcome.toString();
								const contains1to8 = /[1-8]/.test(outcomeStr);
								const contains9 = /[9]/.test(outcomeStr);
								// 如果包含1-8中的数字，但不包含9，则返回false，表示正常
								isBad = !(contains1to8 || !contains9);
							}
						} catch (error) {
							console.error('Error occurred in getCollectOen:', error);
							// 根据错误处理逻辑，可能需要设置isBad为true或其他值
							throw new Error(error);
						}
						let map = parsePatientType(
							patient.age,
							splitDecimalIntoList(patient.moreType),
							patient.firstType,
							patient.name,
							patient.curFinish,
							false
						);
						if (map && map[0]) {
							patient.next = '';
							for (let i = 0; i < map.length; i++) {
								patient.next += parseNext(patient.curFinish, personType.get(map[i]), true) + ';';
							}
						}
						return {
							...patient,
							orderTime: orderTime,
							orderVal: orderVal // 获取排序后的第一个value
						};
					});

					// 等待所有resultPromises解决
					return Promise.all(resultPromises);
				})
				.then((result) => {
					// 所有患者数据处理完毕
					this.pageData = result;
				})
				.catch((error) => {
					// 捕获可能的错误
					console.error('Error occurred:', error);
				});
		},
		//统计数据
		statisticalMethod() {
			const currentDate = new Date();
			const year = currentDate.getFullYear();
			const month = String(currentDate.getMonth() + 1).padStart(2, '0');
			const day = String(currentDate.getDate()).padStart(2, '0');
			let time = `${year}-${month}-${day}`;
			const data = {};

			statisticsNum(time, data).then((res) => {
				if (res.length != 0) {
					this.statisticsData = res[0];
				} else {
					this.statisticsData = {
						yearNum: 0,
						yearAbnormalNum: 0,
						monthlyNum: 0,
						monthlyAbnormalNum: 0,
						dayNum: 0,
						dayAbnormalNum: 0,
						CardMark: 0,
						noCardMark: 0,
						hemoptysisNum: 0,
						feverNum: 0,
						chestPainNum: 0,
						nightSweatNum: 0,
						anorexiaNum: 0,
						weakNum: 0
					};
				}
			});
		},
		// 重置
		reset() {
			this.dateRange = [];
			this.$refs.componentDate.resetSelectDate();
			this.clearScreenNum();
			this.clearIdCard();
			this.clearName();
			this.clearScreenNum();
			this.clearRegion();
			this.clearClassify();
			this.secondType = [];
			this.firstType = [];
			this.pageCurrent = 1;
			this.showSelectCrowd = !this.showSelectCrowd;
			this.search();
		},
		// 跳转到新增患者
		toAddPatient() {
			uni.navigateTo({
				url: '/pages/tb/addPatient/index?flag=1'
			});
		},
		// 分页触发
		change(e) {
			this.$refs.table.clearSelection();
			this.pageCurrent = e.current;
			// 创建一个 Promise 对象数组
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
				getCollect()
			];

			Promise.all(promises)
				.then(async (results) => {
					// results 是一个包含两个 Promise 结果的数组
					let patientPageRes = results[0];
					this.total = patientPageRes.total;
					let collectRes = results[1];

					// 使用 Promise.all 来处理数组中的每个元素
					let resultPromises = patientPageRes.data.map(async (patient) => {
						let orderTime = collectRes
							.filter((screen) => screen.personId == patient.id)
							.map((screen) => {
								return {
									value: screen.screenOrder,
									text: `第${screen.screenOrder}次/${screen.screenTime.split(' ')[0]}`
								};
							})
							.sort((a, b) => b.value - a.value); // 对orderTime进行排序

						let orderVal = orderTime.length > 0 ? orderTime[0].value : null;

						// 先假设isBad为true，等待getCollectOen确定最终值
						let isBad = true;
						try {
							// 等待getCollectOen Promise解决
							const res = await getCollectOen(patient.id, orderVal, uni.$person.year, uni.$screenType);
							if (res.length > 0) {
								const outcomeStr = res[0].outcome.toString();
								const contains1to8 = /[1-8]/.test(outcomeStr);
								const contains9 = /[9]/.test(outcomeStr);
								// 如果包含1-8中的数字，但不包含9，则返回false，表示正常
								isBad = !(contains1to8 || !contains9);
							}
						} catch (error) {
							console.error('Error occurred in getCollectOen:', error);
							// 根据错误处理逻辑，可能需要设置isBad为true或其他值
						}

						patient.next = parsePatientType(
							patient.age,
							splitDecimalIntoList(patient.moreType),
							patient.firstType,
							patient.name,
							patient.curFinish,
							isBad
						);
						if (patient.next && Array.isArray(patient.next)) {
							patient.next = '请医生确认是否做了这些检查：【' + patient.next.join(' | ') + '】';
						}

						return {
							...patient,
							orderTime: orderTime,
							orderVal: orderVal // 获取排序后的第一个value
						};
					});

					// 等待所有resultPromises解决
					return Promise.all(resultPromises);
				})
				.then((result) => {
					// 所有患者数据处理完毕
					this.pageData = result;
				})
				.catch((error) => {
					// 捕获可能的错误
					console.error('Error occurred:', error);
				});
		},
		// 处理数据
		dateScreening() {
			this.pageData.forEach((item) => {
				item.csrq = item.schoolOrTemple + '/' + item.classroom;
			});
		},
		clearName() {
			this.searchName = '';
		},
		clearScreenNum() {
			this.screenNum = '';
		},
		clearIdCard() {
			this.searchIdCard = null;
		},
		clearRegion() {
			this.searchRegion = '';
		},
		clearClassify() {
			this.searchClassify = '';
		},
		//民族回显
		get(e) {
			const foundItem = this.ethnic.find((i) => i.value === e);
			if (foundItem) {
				return foundItem.text;
			}
			return '';
		},
		//处理性别
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
		//第一人群回显
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
		//重点人群分类回显
		getMoreType(val) {
			let selectedOptions = [];
			// console.log("val:"+val.id+"=",val);
			if (val.moreType == 0 || val.moreType == 'NaN') {
				return '无';
			}

			if (val.firstType == 1) {
				// console.log(1);
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

			// console.log("selectedOptions",selectedOptions);
			let text = '';
			if (selectedOptions.length == 0) {
				text = '无';
			}
			selectedOptions.forEach((i) => {
				text = text + (text == '' ? '' : '/') + i;
			});

			return text;
		},
		//生成采集数据====后续删除
		insert() {
			inList().then((res) => {});
		}
	}
};
</script>

<style lang="scss" scoped>
.uni-table-tr {
	overflow: visible;
	background-color: #fff;

	::v-deep .uni-table-td:first-child,
	.uni-table-th:first-child {
		position: sticky;
		left: 0;
		top: 0;
		background-color: #fff;
		z-index: 100;
	}
	::v-deep .uni-table-td:nth-child(2),
	.uni-table-th:nth-child(2) {
		position: sticky;
		left: 55px;
		top: 0;
		background-color: #fff;
		z-index: 100;
	}
	::v-deep .uni-table-td:nth-child(3),
	.uni-table-th:nth-child(3) {
		position: sticky;
		left: 224px;
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
	justify-content: space-evenly;
	.card-main {
		font-size: 18px;
		width: 370px;
		border-radius: 5px;
		margin: 0 10px;
		.card-top {
			width: 100%;
			padding: 5px 20px;
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
					.sp-text {
						display: inline-block;
						color: #000;
						margin-left: 10px;
						font-size: 24px;
					}
				}
				.card-text1 {
					margin-left: 60px;
					color: rgba(115, 115, 115, 1);
					.sp-text {
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
				background-color: rgba(239, 239, 239, 1);
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
.btn-span {
	padding: 5px 10px;
	background-color: #fff;
	border-radius: 5px;
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
		padding-top: 10px;
		width: 200px;
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
