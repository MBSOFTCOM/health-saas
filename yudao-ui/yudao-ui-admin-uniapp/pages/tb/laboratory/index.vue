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
								{{ dataView.dayPerson }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
					<view class="line"></view>
					<view class="card-sp">
						<view class="card-text">
							本月
							<span class="sp-text">
								{{ dataView.monthPerson }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
					<view class="line"></view>
					<view class="card-sp">
						<view class="card-text">
							本年
							<span class="sp-text">
								{{ dataView.yearPerson }}
								<span style="font-size: 18px">人</span>
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
							痰涂片
							<span class="sp-text">
								{{ dataView.smearResultPerson }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
						<view class="card-text1" style="margin-left: 40px">
							痰培养
							<span class="sp-text">
								{{ dataView.cultureResultPerson }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
					<view class="line"></view>
					<view class="card-sp">
						<view class="card-text">
							药敏耐药
							<span class="sp-text">
								{{ dataView.drugResistanceResultPerson }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
					<view class="line"></view>
					<view class="card-sp">
						<view class="card-text">
							分子生物学
							<span class="sp-text">
								{{ dataView.molecularBiologyPerson }}
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
					<span style="font-size: 18px">即时痰标本号</span>
					<uni-search-bar
						@confirm="search"
						:focus="false"
						class="top-search"
						v-model="forthwithSputumCode"
						clearButton="always"
						placeholder="即时痰标本号"
						style="width: 245px"
						cancelButton="none"
						@clear="clearClassify"
					></uni-search-bar>
					<span style="font-size: 18px; display: inline-block; margin-left: 55px">晨痰标本号</span>
					<uni-search-bar
						@confirm="search"
						:focus="false"
						class="top-search"
						v-model="morningSputumCode"
						clearButton="always"
						placeholder="晨痰标本号"
						style="width: 245px"
						cancelButton="none"
						@clear="clearRegion"
					></uni-search-bar>
				</view>
			</view>
			<view class="statistics" style="justify-content: flex-start">
				<span style="font-size: 18px; display: inline-block;">夜痰标本号</span>
				<uni-search-bar
					@confirm="search"
					:focus="false"
					class="top-search"
					v-model="eveningSputumCode"
					clearButton="always"
					placeholder="夜痰标本号"
					style="width: 245px"
					cancelButton="none"
					@clear="clearRegion"
				></uni-search-bar>
				<span style="font-size: 18px; margin-left: 10px">筛查日期</span>
				<select-date style="margin-left: 10px" ref="componentDate" @selectDate="handleSelectDate" />
			</view>
			<view class="statistics" style="justify-content: flex-start">
				<span style="font-size: 18px; display: inline-block;">身份证号</span>
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

			<view class="uni-container">
				<uni-table ref="table" :loading="loading" stripe emptyText="暂无更多数据">
					<uni-tr>
						<uni-th width="60" align="center">序号</uni-th>
						<uni-th width="110" align="center">筛查编号</uni-th>
						<uni-th width="100" align="center">姓名</uni-th>
						<uni-th width="170" align="center">筛查次序/时间</uni-th>
						<uni-th width="25%" align="center">下一步检查</uni-th>
						<uni-th width="130" align="center">操作</uni-th>
						<uni-th width="5%" align="center">第一人群分类</uni-th>
						<uni-th width="60" align="center">性别</uni-th>
						<uni-th width="60" align="center">年龄</uni-th>
						<uni-th width="5%" align="center">民族</uni-th>
						<uni-th width="170" align="center">身份证</uni-th>
					</uni-tr>
					<uni-tr v-for="(item, index) in pageData" :key="index">
						<uni-td align="center">{{ index + 1 }}</uni-td>
						<uni-td align="center">{{ item.screenId }}</uni-td>
						<uni-td align="center">{{ item.name }}</uni-td>
						<uni-td align="center">
							<view>
								<uni-data-select
									v-model="item.orderVal"
									:localdata="item.screenOrderValues"
									@change="changeOrder(item)"
									placeholder="选择筛查次序"
									:clear="false"
									placement="top"
									@click="toggleOverflow"
								></uni-data-select>
							</view>
						</uni-td>
						<uni-td align="center">{{ item.showNextStr }}</uni-td>
						<uni-td align="center">
							<view style="display: flex; justify-content: space-around; align-items: center">
								<span
									class="btn-span"
									style="color: rgba(21, 99, 232, 1); border: 1px solid rgba(21, 99, 232, 1)"
									@click="submitResult(item)"
								>
									实验结果
								</span>
							</view>
						</uni-td>
						<uni-td align="center">{{ item.firstTypeStr }}</uni-td>
						<uni-td align="center">{{ item.sexStr }}</uni-td>
						<uni-td align="center">{{ item.age }}</uni-td>
						<uni-td align="center">{{ item.nationStr }}</uni-td>
						<uni-td align="center">{{ item.idNum }}</uni-td>
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
							<span class="pro-main">
								症状筛查+PPD+查验卡痕+胸片检查，有症状或强阳性或异常进一步检查;
							</span>
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
	</view>
</template>

<script>
import * as laboratosyApi from '@/api/screen/laboratory/index';
import {personTypeForProcess, screenTypeMap} from '../../../utils/dict';

// 离线ocr插件
const ocrModule = uni.requireNativePlugin('YY-TomatoOCR');
const mpaasScanModule = uni.requireNativePlugin('Mpaas-Scan-Module');

export default {
	data() {
		return {
			nav: [
				{
					value: '常规筛查'
				},
				{
					value: '实验室组',
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
          label: '痰检二维码',
          val: 3
        },{
          label: '身份证',
          val: 2
        }
			],
			scanVal: 1,
			// 扫描弹出层显示
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

			// 筛查时间范围
			dateRange: [],
			// 姓名
			searchName: '',
			// 身份证
			searchIdCard: null,
			// 晨痰标本号
			morningSputumCode: '',
			// 即时痰标本号
			forthwithSputumCode: '',
			// 夜痰标本号
			eveningSputumCode: '',
			// 分页数据
			pageData: [],
			dataView: {}
		};
	},
	onLoad() {
		this.getNavItems(uni.$screenType);
		this.getDataView();
		this.search();
	},
	onShow() {
		this.getDataView();
		this.search();
	},
	methods: {
		getNavItems(screenType) {
			switch (screenType) {
				case 1:
					this.nav = [{ value: '常规筛查' }, { value: 'DR管理', isActive: true }];
					break;
				case 2:
					this.nav = [{ value: '新生入学筛查' }, { value: 'DR管理', isActive: true }];
					break;
				case 3:
					this.nav = [{ value: '应急筛查' }, { value: 'DR管理', isActive: true }];
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
		getDataView() {
			const now = new Date();
			const currentYear = now.getFullYear();

			// 获取显示数据
			laboratosyApi
				.getExperimentData({
					// 筛查年份、类型
					year: currentYear,
					screenType: uni.$screenType
				})
				.then((res) => {
					if (res.data) {
						this.dataView = res.data;
					}
				});
		},
		toggleOverflow() {
			this.overflowVisible = !this.overflowVisible; // 切换 overflowVisible 的值
		},
		changeOrder(item) {
			const orderV = item.orderVal;
			const screenOrderData = item.screenOrderValues.filter((item) => item.value === orderV);
			if (screenOrderData[0]) {
				item.sputumExaminationId = screenOrderData[0].id;
			}
		},
		submitResult(item) {
			uni.navigateTo({
				url: '/pages/tb/laboratoryDetail/laboratoryDetail?item=' + encodeURIComponent(JSON.stringify(item))
			});
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
      if (this.scanVal === 3) {
        mpaasScanModule.mpaasScan(
            {
              // 扫码识别类型，参数可多选，qrCode、barCode，不设置，默认识别所有
              scanType: ['qrCode', 'barCode'],
              // 是否隐藏相册，默认false不隐藏
              hideAlbum: false
            },
            (ret) => {
              if (ret.resp_message == 'success') {
                // 生成信息的规则：患者id、筛查编号、姓名、年度、筛查类型(1-常规,2-新生,3-应急)、痰类型(1-即时痰，2-晨痰，3-夜痰)、痰记录id，通过 ‘;’分割
                let data = ret.resp_result.split(';');
                let patientParam={personId:data[0],screenId:data[1],id:data[6],screenType:data[4]}
                if (uni.$screenType !== Number.parseInt(data[4])) {
                  let that = this;
                  uni.showModal({
                    title: '扫描的患者筛查类型不匹配',
                    content: `该患者是${screenTypeMap[data[4]]},而您所进入的是${
                        screenTypeMap[uni.$screenType]
                    },请确认是否继续？审核结果以患者类型为准，审核后请到${
                        screenTypeMap[data[4]]
                    }中查看审核详情`,
                    cancelText: '取消',
                    confirmText: '确认',
                    success: function (res) {
                      // console.log(res)
                      if (res.confirm) {
                        that.submitResult(patientParam);
                      }
                      if (res.cancel) {
                        uni.showToast({
                          title: '取消',
                          mask: true,
                          icon: 'error',
                          duration: 1500
                        });
                      }
                    }
                  });
                } else {
                  this.submitResult(patientParam);
                }

                // console.log(data);
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
		open() {},
		close() {
			this.show = false;
		},
		closeGather() {
			this.showGather = false;
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
			// 获取实验组分页数据
			this.getScreenExperimentPage();

			// 强制搜索后显示第一页
			let e = { type: 'current', current: 1 };
			this.change(e);
		},
		// 重置
		reset() {
			this.dateRange = [];
			// 姓名
			this.searchName = '';
			// 身份证
			this.searchIdCard = '';
			// 晨痰标本号
			this.morningSputumCode = '';
			// 即时痰标本号
			this.forthwithSputumCode = '';
			// 夜痰标本号
			this.eveningSputumCode = '';
			// 重置日期组件
			this.$refs.componentDate.resetSelectDate();
			// 统计数据
			this.getDataView();
			// 搜索
			this.search();
		},
		// 分页触发
		change(e) {
			this.$refs.table.clearSelection();
			this.pageCurrent = e.current;

			// 获取实验组分页数据
			this.getScreenExperimentPage();
		},
		getScreenExperimentPage() {
			const now = new Date();
			const currentYear = now.getFullYear();

			laboratosyApi
				.getScreenExperimentPage({
					pageNo: this.pageCurrent,
					pageSize: this.pageSize,
					screenTime: this.dateRange,
					forthwithSputumCode: this.forthwithSputumCode,
					eveningSputumCode: this.eveningSputumCode,
					morningSputumCode: this.morningSputumCode,
					searchName: this.searchName,
					searchIdCard: this.searchIdCard,

					// 筛查年份、类型
					year: currentYear,
					screenType: uni.$screenType
				})
				.then((res) => {
					this.pageData = res.data.list === null ? [] : res.data.list;
					console.log(this.pageData);
					this.total = res.data.total === null ? 0 : res.data.total;
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
		left: 59px;
		top: 0;
		background-color: #fff;
		z-index: 100;
	}
	::v-deep .uni-table-td:nth-child(3),
	.uni-table-th:nth-child(3) {
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
		margin-left: 10px;
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
