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
			<view class="card-main" style="width: 340px;">
				<view class="card-top various1">诊断人数</view>
				<view class="card-tle">
					<view class="card-sp">
						<view class="card-text">
							今日
							<span class="sp-text">
								{{ viewData.todayCount }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
						<view class="card-text1">
							异常
							<span class="sp-text">
								{{ viewData.todayAbnormalCount }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
					<view class="line" style="width: 290px;"></view>
					<view class="card-sp">
						<view class="card-text">
							本月
							<span class="sp-text">
								{{ viewData.monthCount }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
						<view class="card-text1">
							异常
							<span class="sp-text">
								{{ viewData.monthAbnormalCount }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
					<view class="line" style="width: 290px;"></view>
					<view class="card-sp">
						<view class="card-text">
							本年
							<span class="sp-text">
								{{ viewData.yearCount }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
						<view class="card-text1">
							异常
							<span class="sp-text">
								{{ viewData.yearAbnormalCount }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
					<view class="line" style="width: 290px;"></view>
					<view class="card-sp">
						<view class="card-text">
							
							<span class="sp-text">
								
								<span style="font-size: 18px"></span>
							</span>
						</view>
						<view class="card-text1">
							
							<span class="sp-text">
								
								<span style="font-size: 18px"></span>
							</span>
						</view>
					</view>
				</view>
			</view>
			<view class="card-main" style="width: 400px;">
				<view class="card-top various2">诊断结果</view>
				<view class="card-tle">
					<view class="card-sp">
						<view class="card-text">
							病原学阳性
							<span class="sp-text">
								{{ viewData.outcome1Count }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
						<view class="card-text1">
							陈旧性肺结核
							<span class="sp-text" style="color: #000">
								{{ viewData.outcome5Count }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
					<view class="line" style="width: 350px;"></view>
					<view class="card-sp">
						<view class="card-text">
							病原学阴性
							<span class="sp-text">
								{{ viewData.outcome2Count }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
						<view class="card-text1">
							无病原学结果
							<span class="sp-text" style="color: #000">
								{{ viewData.outcome3Count }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
					<view class="line" style="width: 350px;"></view>
					<view class="card-sp">
						<view class="card-text">
							潜伏感染者
							<span class="sp-text">
								{{ viewData.outcome7Count }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
						<view class="card-text1">
							暂时排除结核病
							<span class="sp-text" style="color: #000">
								{{ viewData.outcome6Count }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
					<view class="line" style="width: 350px;"></view>
					<view class="card-sp">
						<view class="card-text">
							耐药
							<span class="sp-text">
								{{ viewData.outcome4Count }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
				</view>
			</view>
			<view class="card-main">
				<view class="card-top various3">治疗方案</view>
				<view class="card-tle">
					<view class="card-sp">
						<view class="card-text">
							住院治疗
							<span class="sp-text">
								{{ viewData.treatmentProgram2Count }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
						<view class="card-text1">
							服药管理(服药)
							<span class="sp-text" style="color: #000">
								{{ viewData.treatmentProgram5Count }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
					<view class="line"></view>
					<view class="card-sp">
						<view class="card-text">
							隔离治疗
							<span class="sp-text">
								{{ viewData.treatmentProgram3Count }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
						<view class="card-text1">
							服药管理(复诊)
							<span class="sp-text" style="color: #000">
								{{ viewData.treatmentProgram4Count }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
					<view class="line"></view>
					<view class="card-sp">
						<view class="card-text">
							停止治疗
							<span class="sp-text">
								{{ viewData.treatmentProgram7Count }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
						<view class="card-text1">
							服药管理(随访)
							<span class="sp-text" style="color: #000">
								{{ viewData.treatmentProgram6Count }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
					<view class="line"></view>
					<view class="card-sp">
						<view class="card-text">
							14天治疗管理
							<span class="sp-text">
								{{ viewData.treatmentProgram1Count }}
								<span style="font-size: 18px">人</span>
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
						@confirm="search"
						:focus="false"
						class="top-search"
						v-model="screenId"
						clearButton="always"
						placeholder="筛查编号"
						style="width: 245px"
						cancelButton="none"
						@clear="clearRegion"
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
						style="width: 115px"
						type="primary"
						:plain="true"
						text="查看筛查流程"
					></up-button>
				</view>
			</view>
		</view>

		<view class="uni-container">
			<uni-table ref="table" :loading="loading"  stripe emptyText="暂无更多数据">
				<uni-tr>
					<uni-th width="60" align="center">序号</uni-th>
					<uni-th width="110" align="center">筛查编号</uni-th>
					<uni-th width="60" align="center">姓名</uni-th>
					<uni-th width="110" align="center">筛查时间</uni-th>
					<uni-th width="150" align="center">操作</uni-th>
					<uni-th width="5%" align="center">性别</uni-th>
					<uni-th width="5%" align="center">年龄</uni-th>
					<uni-th width="5%" align="center">第一人群分类</uni-th>
					<uni-th width="5%" align="center">民族</uni-th>
					<uni-th width="10%" align="center">身份证</uni-th>
					<uni-th width="10%" align="center">结果</uni-th>
					<uni-th width="10%" align="center">治疗方案</uni-th>
					<uni-th width="10%" align="center">是否网报</uni-th>
					<uni-th width="8%" align="center">是否进行预防性治疗</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in pageData" :key="index">
					<uni-td align="center">{{ index + 1 }}</uni-td>
					<uni-td align="center">{{ item.screenId }}</uni-td>
					<uni-td align="center">{{ item.name }}</uni-td>
					<uni-td align="center">{{ this.parseTime(item.screenTime) }}</uni-td>
					<uni-td align="center">
						<view style="display: flex; justify-content: space-around; align-items: center">
							<u-button
								@click="submit(item)"
								style="
									background-color: #fff;
									border: 1px solid rgba(21, 99, 232, 1);
									color: rgba(21, 99, 232, 1);
									display: flex;
									flex-direction: column;
									justify-content: center;
									height: 6vh;
									width: 15vh;
									font-size: 16px;
								"
								class="btn-span"
							>
								诊断结果
							</u-button>
						</view>
					</uni-td>
					<uni-td align="center">{{ item.sexStr }}</uni-td>
					<uni-td align="center">{{ item.age }}</uni-td>
					<uni-td align="center">{{ item.firstTypeStr }}</uni-td>
					<uni-td align="center">{{ item.nationStr }}</uni-td>
					<uni-td align="center">{{ item.idNum }}</uni-td>
					<uni-td align="center">{{ item.outcomeStr }}</uni-td>
					<uni-td align="center">{{ item.treatmentProgramStr }}</uni-td>
					<uni-td align="center">{{ item.reportStr }}</uni-td>
					<uni-td align="center">{{ item.preventiveTreatmentStr }}</uni-td>
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

import * as diagnosisApi from '@/api/screen/diagnosis/index';
import { parseTime } from '@/utils/ruoyi';
import { personTypeForProcess } from '../../../utils/dict';
export default {
	data() {
		return {
			nav: [
				{
					value: '常规筛查'
				},
				{
					value: '诊断组',
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
			scanVal: 1,
			show: false,
			showGather: false,
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
			// 身份证
			searchIdCard: null,
			// 筛查编号
			screenId: '',
			pageData: [],
			viewData: {}
		};
	},
	onLoad() {
		this.getNavItems(uni.$screenType);
		this.search();
		this.getScreenDiagnosisData();
	},
	methods: {
		getNavItems(screenType) {
			switch (screenType) {
				case 1:
					this.nav = [{ value: '常规筛查' }, { value: '诊断组', isActive: true }];
					break;
				case 2:
					this.nav = [{ value: '新生入学筛查' }, { value: '诊断组', isActive: true }];
					break;
				case 3:
					this.nav = [{ value: '应急筛查' }, { value: '诊断组', isActive: true }];
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
		getScreenDiagnosisData() {
			const now = new Date();
			const currentYear = now.getFullYear();

			// 查询统计数据
			diagnosisApi
				.getScreenDiagnosisData({
					// 筛查年份、类型
					year: currentYear,
					screenType: uni.$screenType
				})
				.then((res) => {
					if (res.data) {
						this.viewData = res.data;
					}
				});
		},
		parseTime(time) {
			return parseTime(time, '{y}-{m}-{d}');
		},
		groupChange(n) {
			console.log('groupChange', n);
		},
		submit(item) {
			uni.navigateTo({
				url: '/pages/tb/diagnosisDetail/diagnosisDetail?item=' + encodeURIComponent(JSON.stringify(item))
			});
		},
		open() {},
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
		// 时间
		handleSelectDate(value) {
			this.dateRange = value;
		},
		// 搜索
		search() {
			// 获取分页数据
			this.getScreenDiagnosisPage();

			// 查询统计数据
			this.getScreenDiagnosisData();

			// 强制搜索后显示第一页
			let e = { type: 'current', current: 1 };
			this.change(e);
		},
		// 重置
		reset() {
			// 清空样式
			this.dateRange = [];
			// 姓名
			this.searchName = '';
			// 身份证
			this.searchIdCard = null;
			// 筛查编号
			this.screenId = '';
			this.$refs.componentDate.resetSelectDate();
			// 搜索
			this.search();
		},
		// 分页触发
		change(e) {
			this.$refs.table.clearSelection();
			this.pageCurrent = e.current;
			// 获取分页数据
			this.getScreenDiagnosisPage();
		},
		getScreenDiagnosisPage() {
			const now = new Date();
			const currentYear = now.getFullYear();

			diagnosisApi
				.getScreenDiagnosisPage({
					pageNo: this.pageCurrent,
					pageSize: this.pageSize,
					screenTime: this.dateRange,
					name: this.searchName,
					idNum: this.searchIdCard,
					screenId: this.screenId,

					// 筛查年份、类型
					year: currentYear,
					screenType: uni.$screenType
				})
				.then((res) => {
					this.pageData = res.data.list;
					this.total = res.data.total;
				});
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
		left: 60px;
		top: 0;
		background-color: #fff;
		z-index: 100;
	}
	::v-deep .uni-table-td:nth-child(3),
	.uni-table-th:nth-child(3) {
		position: sticky;
		left: 229px;
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
					margin-left: 30px;
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
	padding: 5px;
	color: #fff;
	border-radius: 5px;
}
.uni-container {
	padding: 0 10px;
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

.scan-open {
	padding: 5px 15px;
	display: flex;
	flex-direction: column;
	justify-content: space-around;
	align-items: center;
	.scan-btn {
		border-top: 1px solid #000;
		padding-top: 10px;
		width: 110%;
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
