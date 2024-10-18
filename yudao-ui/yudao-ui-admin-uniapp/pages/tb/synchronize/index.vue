<template>
	<view class="sidebar-main">
		<view class="sidebar-container">
			<!-- 侧边栏 -->
			<view class="sidebar" style="background-color: #fff; color: #000; height: 115vh; width: 13vw">
				<image class="sidebar-header" src="../../../static/images/tb/logo.png" mode=""></image>
				<view class="sidebar-menu">
					<view
						v-for="(item, index) in sidebarItems"
						:key="index"
						@click="handleSidebarItemClick(index)"
						:class="['sidebar-item', item.className, { active: activeItem == index }]"
					>
						<view style="display: flex; align-items: center">
							<image style="width: 18px; height: 18px; margin-right: 8px" :src="item.url" mode=""></image>
							{{ item.title }}
						</view>
					</view>
				</view>
			</view>
		</view>
		<view class="sidebar-right">
			<view class="sidebar-right-top1">
				<view class="top-left">
					<yile-breadcrumb
						:nav="nav"
						color="rgba(153, 153, 153, 1)"
						actColor="rgba(36, 93, 209, 1)"
					></yile-breadcrumb>
					
				</view>
				<view style="margin-right: 47%;" v-if="synchronizeTime">
					上次同步时间:{{synchronizeTime}}
				</view>
				<view class="top-right-2">
					<up-button
						@click="districtCover"
						:plain="true"
						style="margin-right: 10px"
						text="同步区划数据"
						v-if="activeItem === 0"
						class="custom-style"
					></up-button>
					<up-button
						@click="workTeam"
						:plain="true"
						text="同步工作队伍"
						v-if="activeItem === 0"
						class="custom-style"
					></up-button>
					<up-button
						@click="showTableData(activeItem)"
						:plain="true"
						class="custom-style"
						text="PC到平板"
						v-if="activeItem !== 6 && activeItem !== 0"
					></up-button>
					<up-button
						@click="showLocalTableData(activeItem)"
						style="margin-left: 10px"
						class="custom-style"
						:plain="true"
						text="平板到PC"
						v-if="activeItem !== 0"
					></up-button>
					<up-button
						@click="deleteData"
						style="margin-left: 10px"
						:plain="true"
						text="清除所有数据"
						class="custom-style1"
					></up-button>
				</view>
			</view>
			<view class="sidebar-right-top">
				<view class="top-left">
					<span style="font-size: 16px" v-if="activeItem !== 0">筛查编号</span>
					<uni-search-bar
						@confirm="handleQuery"
						:focus="false"
						class="top-search"
						v-model="queryParams.screenId"
						placeholder="请输入筛查编号"
						style="width: 210px"
						cancelButton="none"
						@clear="clearScreenId"
						v-if="activeItem !== 0"
					></uni-search-bar>
					<span style="font-size: 16px" v-if="activeItem === 0">分组</span>
					<uni-search-bar
						@confirm="handleQuery"
						:focus="false"
						class="top-search"
						v-model="queryParams.groupName"
						placeholder="请输入分组"
						style="width: 210px"
						cancelButton="none"
						@clear="clearGroupName"
						v-if="activeItem === 0"
					></uni-search-bar>
				</view>
				<view class="top-right-1">
					<up-button @click="handleQuery" :plain="true" class="custom-search" text="搜索"></up-button>
					<up-button
						@click="reset"
						style="margin: 0 10px"
						class="custom-reset"
						:plain="true"
						text="重置"
					></up-button>
				</view>
			</view>
			<view class="sidebar-right-bom">
				<!-- 用户表 -->
				<uni-table ref="table" :loading="loading" stripe emptyText="暂无更多数据" v-if="activeItem === 0">
					<uni-tr>
						<uni-th width="3%" align="center">序号</uni-th>
						<uni-th width="5%" align="center">工作人员</uni-th>
						<uni-th width="5%" align="center">账号</uni-th>
						<uni-th width="10%" align="center">分组</uni-th>
						<uni-th width="10%" align="center">筛查点</uni-th>
						<uni-th width="10%" align="center">筛查单位</uni-th>
						<uni-th width="10%" align="center">工作年度</uni-th>
					</uni-tr>
					<uni-tr v-for="(item, index) in pageData" :key="index">
						<uni-td align="center">{{ index + 1 }}</uni-td>
						<uni-td align="center">{{ item.nickName }}</uni-td>
						<uni-td align="center">{{ item.name }}</uni-td>
						<uni-td align="center">{{ item.groupName }}</uni-td>
						<uni-td align="center">{{ item.screenPoint }}</uni-td>
						<uni-td align="center">{{ item.agency }}</uni-td>
						<uni-td align="center">{{ item.year }}</uni-td>
					</uni-tr>
				</uni-table>
				<!-- 摸底表数据 -->
				<uni-table
					class="table-bom"
					ref="table"
					:loading="loading"
					stripe
					emptyText="暂无更多数据"
					v-if="activeItem === 1"
				>
					<uni-tr>
						<uni-th width="60" align="center">序号</uni-th>
						<uni-th width="5%" align="center">姓名</uni-th>
						<uni-th width="180" align="center">身份证号</uni-th>
						<uni-th width="10%" align="center">工作年度</uni-th>
						<uni-th width="10%" align="center">筛查编号</uni-th>
						<uni-th width="10%" align="center">筛查点</uni-th>
						<uni-th width="10%" align="center">筛查类型</uni-th>
						<uni-th width="95" align="center">是否新增</uni-th>
						<uni-th width="95" align="center">是否为新生</uni-th>
						<uni-th width="100" align="center">筛查状态</uni-th>
						<uni-th width="8%" align="center">第一人群分类</uni-th>
						<uni-th width="8%" align="center">多人群分类</uni-th>
						<uni-th width="8%" align="center">计划筛查时间</uni-th>
						<uni-th width="8%" align="center">性别</uni-th>
						<uni-th width="8%" align="center">年龄</uni-th>
						<uni-th width="8%" align="center">联系电话</uni-th>
						<uni-th width="8%" align="center">民族</uni-th>
						<uni-th width="8%" align="center">单位(学校)</uni-th>
						<uni-th width="8%" align="center">班级</uni-th>
						<uni-th width="8%" align="center">身高</uni-th>
						<uni-th width="8%" align="center">体重</uni-th>
						<uni-th width="8%" align="center">户籍地址</uni-th>
						<uni-th width="8%" align="center">现住址</uni-th>
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
						<uni-td align="center">{{ item.screenStartTime }}-{{item.screenEndTime}}</uni-td>
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
				<!-- 采集组数据 -->
				<uni-table ref="table" :loading="loading" stripe emptyText="暂无更多数据" v-if="activeItem === 2">
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
				<!-- ppd组数据 -->
				<uni-table ref="table" :loading="loading" stripe emptyText="暂无更多数据" v-if="activeItem === 3">
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
				<!-- dr/ct组数据 -->
				<uni-table ref="table" :loading="loading" stripe emptyText="暂无更多数据" v-if="activeItem === 4">
					<uni-tr>
						<uni-th width="5%" align="center">序号</uni-th>
						<uni-th width="5%" align="center">筛查编号</uni-th>
						<uni-th width="5%" align="center">工作年度</uni-th>
						<uni-th width="5%" align="center">筛查类型</uni-th>
						<uni-th width="8%" align="center">患者姓名</uni-th>
						<uni-th width="5%" align="center">胸片编号</uni-th>
						<uni-th width="5%" align="center">胸片</uni-th>
						<uni-th width="5%" align="center">采集时间</uni-th>
						<uni-th width="8%" align="center">医生签名</uni-th>
						<uni-th width="8%" align="center">胸片结果</uni-th>
						<uni-th width="8%" align="center">筛查次序</uni-th>
						<uni-th width="8%" align="center">筛查时间</uni-th>
					</uni-tr>
					<uni-tr v-for="(item, index) in pageData" :key="index">
						<uni-td align="center">{{ index + 1 }}</uni-td>
						<uni-td align="center">{{ item.screenId }}</uni-td>
						<uni-td align="center">{{ item.year }}</uni-td>
						<uni-td align="center">{{ screenType(item.screenType) }}</uni-td>
						<uni-td align="center">{{ item.name }}</uni-td>
						<uni-td align="center">{{ item.chestRadiographCode }}</uni-td>
						<uni-td align="center">
							<image :src="item.chestRadiograph" style="width: 4rem;height: 2rem;"></image>
						</uni-td>
						<uni-td align="center">{{ item.photoTime }}</uni-td>
						<uni-td align="center">
							<image :src="item.doctorSignature" class="image-transform"></image>
						</uni-td>
						<uni-td align="center">{{ chestRadiographOutcome(item.outcome) }}</uni-td>
						<uni-td align="center">{{ item.screenOrder }}</uni-td>
						<uni-td align="center">{{ formatDate(item.screenTime) }}</uni-td>
					</uni-tr>
				</uni-table>
				<!-- 痰检组数据 -->
				<uni-table ref="table" :loading="loading" stripe emptyText="暂无更多数据" v-if="activeItem === 5">
					<uni-tr>
						<uni-th width="3%" align="center">序号</uni-th>
						<uni-th width="5%" align="center">筛查编号</uni-th>
						<uni-th width="5%" align="center">工作年度</uni-th>
						<uni-th width="5%" align="center">筛查类型</uni-th>
						<uni-th width="10%" align="center">患者姓名</uni-th>
						<uni-th width="5%" align="center">医生签名</uni-th>
						<uni-th width="5%" align="center">是否雾化</uni-th>
						<uni-th width="10%" align="center">痰标本类型</uni-th>
						<uni-th width="10%" align="center">即时痰照片</uni-th>
						<uni-th width="10%" align="center">即时痰标本号</uni-th>
						<uni-th width="10%" align="center">即时痰采集时间</uni-th>
						<uni-th width="10%" align="center">夜痰照片</uni-th>
						<uni-th width="10%" align="center">夜痰标本号</uni-th>
						<uni-th width="10%" align="center">夜痰采集时间</uni-th>
						<uni-th width="10%" align="center">晨痰照片</uni-th>
						<uni-th width="10%" align="center">晨痰标本号</uni-th>
						<uni-th width="10%" align="center">晨痰采集时间</uni-th>
						<uni-th width="10%" align="center">结果</uni-th>
						<uni-th width="10%" align="center">筛查次序</uni-th>
						<uni-th width="10%" align="center">筛查时间</uni-th>
					</uni-tr>
					<uni-tr v-for="(item, index) in pageData" :key="index">
						<uni-td align="center">{{ index + 1 }}</uni-td>
						<uni-td align="center">{{ item.screenId }}</uni-td>
						<uni-td align="center">{{ item.year }}</uni-td>
						<uni-td align="center">{{ screenType(item.screenType) }}</uni-td>
						<uni-td align="center">{{ item.name }}</uni-td>
						<uni-td align="center">
							<image :src="item.doctorSignature" class="image-transform"></image>
						</uni-td>
						<uni-td align="center">{{ transform(item.atomization) }}</uni-td>
						<uni-td align="center">{{ sputumType(item.type) }}</uni-td>
						<uni-td align="center">
							<image :src="item.forthwithSputum" style="width: 4rem;height: 2rem;"></image>
						</uni-td>
						<uni-td class="uni-td" align="center">{{ item.forthwithSputumCode }}</uni-td>
						<uni-td class="uni-td" align="center">{{ formatDate(item.forthwithSputumTime) }}</uni-td>
						<uni-td align="center">
							<image :src="item.eveningSputum" style="width: 4rem;height: 2rem;"></image>
						</uni-td>
						<uni-td class="uni-td" align="center">{{ item.eveningSputumCode }}</uni-td>
						<uni-td class="uni-td" align="center">{{ formatDate(item.eveningSputumTime) }}</uni-td>
						<uni-td align="center">
							<image :src="item.morningSputum" style="width: 4rem;height: 2rem;"></image>
						</uni-td>
						<uni-td class="uni-td" align="center">{{ item.morningSputumCode }}</uni-td>
						<uni-td class="uni-td" align="center">{{ formatDate(item.morningSputumTime) }}</uni-td>
						<uni-td align="center">{{ sputumCheck(item.outcome) }}</uni-td>
						<uni-td align="center">{{ item.screenOrder }}</uni-td>
						<uni-td align="center">{{ formatDate(item.screenTime) }}</uni-td>
					</uni-tr>
				</uni-table> 
				<!-- 心电图组数据 -->
				<uni-table ref="table" :loading="loading" stripe emptyText="暂无更多数据" v-if="activeItem === 6">
					<uni-tr>
						<uni-th width="60" align="center">序号</uni-th>
						<uni-th width="5%" align="center">筛查编号</uni-th>
						<uni-th width="8%" align="center">工作年度</uni-th>
						<uni-th width="8%" align="center">筛查类型</uni-th>
						<uni-th width="8%" align="center">患者姓名</uni-th>
						<uni-th width="5%" align="center">心电图照片</uni-th>
						<uni-th width="5%" align="center">检测结果</uni-th>
						<uni-th width="5%" align="center">医生签名</uni-th>
						<uni-th width="8%" align="center">筛查次序</uni-th>
						<uni-th width="8%" align="center">筛查时间</uni-th>
						<uni-th width="8%" align="center">筛查点</uni-th>
					</uni-tr>
					<uni-tr v-for="(item, index) in pageData" :key="index">
						<uni-td align="center">{{ index + 1 }}</uni-td>
						<uni-td align="center">{{ item.screenId }}</uni-td>
						<uni-td align="center">{{ item.year }}</uni-td>
						<uni-td align="center">{{ screenType(item.screenType) }}</uni-td>
						<uni-td align="center">{{ item.name }}</uni-td>
						<uni-td align="center">
							<image :src="item.electrocardiogram" style="width: 4rem;height: 2rem;"></image>
						</uni-td>
						<uni-td align="center">{{ item.testResult }}</uni-td>
						<uni-td align="center">
							<image :src="item.doctorSignature" class="image-transform"></image>
						</uni-td>
						<uni-td align="center">{{ item.screenOrder }}</uni-td>
						<uni-td align="center">{{ formatDate(item.screenTime) }}</uni-td>
						<uni-td align="center">{{ checkNull(item.screenPoint) }}</uni-td>
					</uni-tr>
				</uni-table>
				<view class="uni-pagination-box">
					<uni-pagination show-icon :page-size="pageSize" :current="pageNo" :total="total" @change="change" />
				</view>
			</view>
		</view>
	</view>
</template>

<script>
import dbUtils from '../../../uni_modules/zjy-sqlite-manage/components/zjy-sqlite-manage/dbUtils';
import {
	dbName,
	tbScreenPerson,
	tbScreenCollect,
	tbScreenPpd,
	tbScreenChestRadiograph,
	tbScreenSputumExamination,
	tbScreenElectrocardiogram,
	tbScreenUser,
	tbScreenSum,
	tbScreenImages,
	tbScreenReagent,
	tbScreenConsume,
	tbScreenConsumeRecord
} from '@/utils/sqlite';
import {
	collectSymptoms,
	collectSymptoms_new,
	screenType,
	getLabelByValue,
	genderMap,
	injectionWayMap,
	commonMap,
	firstTypeMap,
	moreTypeMap,
	nationMap,
	sputumCheck,
	sputumType,
	ctOutcome,
	ppdOutcome,
	screenStatusMap,
	items2,
	items3
} from '@/utils/dict.js';
import * as SynchronizeApi from '@/api/synchronize/synchronize';
import * as DistrictApi from '@/api/screen/district';
import CryptoJS from 'crypto-js';
import {delImgFromDir} from "../../../api/synchronize/synchronize";

export default {
	data() {
		return {
			nav: [
				{
					value: '数据同步'
				},
				{
					value: '工作队',
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
				screenPoint: undefined,
				groupName: undefined
			},
			agency: undefined, //筛查单位
			// 侧边栏 菜单项
			sidebarItems: [
				{ title: '工作队', url: '../../../static/images/tb/ac-1.png' },
				{ title: '待筛查人员', url: '../../../static/images/tb/sy-2.png' },
				{ title: '采集组', url: '../../../static/images/tb/sy-3.png' },
				{ title: 'PPD组', url: '../../../static/images/tb/sy-4.png' },
				{ title: 'DR/CT组', url: '../../../static/images/tb/sy-5.png' },
				/*
				{ title: '痰检组', url: '../../../static/images/tb/sy-6.png' },
				{ title: '心电图组', url: '../../../static/images/tb/sy-7.png' }
				*/
			],
			// 初始激活 第一个菜单项
			activeItem: 0,
			// 封装同步的工作队伍数据
			workTeamData: [],
			// 当前登录用户信息
			user: {
				id: null,
				pwd: uni.$user.pwd,
				groupName: '工作队,待筛查人员,'
			},
			year: undefined,
			range: [],
			yearList: [],
			synchronizeTime:null //上次同步时间
		};
	},
	//页面加载时自动调用
	onLoad() {
		// console.log(uni.$user.screenPoint);
		let screenPoint = uni.$user.screenPoint.toString().split(',');
		if (screenPoint.length > 1) {
			SynchronizeApi.getScreenPoint(uni.$user.name).then((res) => {
				// console.log(res);
				this.queryParams.screenPoint = res[0].screenPoint;
				uni.$user.screenPoint = res[0].screenPoint;
				this.agency = res[0].agency;
				uni.$user.agency = res[0].agency;
			});
		} else {
			this.queryParams.screenPoint = uni.$user.screenPoint;
			this.agency = uni.$user.agency;
		}
		this.getGroups();
		
		// console.log(this.agency);
		// this.queryParams.screenPoint=uni.$user.screenPoint
		// this.user.id=uni.$user.id
		// this.user.groupName='工作队,待筛查人员,'
		// console.log(this.user.groupName);
		// this.search(0);
		// console.log(this.user.pwd);
		// console.log(uni.$user);
		// this.getPwd();
		// this.generateYear()
	},
	onShow() {
		this.search(this.activeItem);
		// 获取对应分组的上次同步时间
		this.getLastSynchronizeTime(this.activeItem)
	},
	methods: {
updateArray(number) {
    let groupName = '';
    let sidebarImages = [
        '../../../static/images/tb/sy-1.png',
        '../../../static/images/tb/sy-2.png',
        '../../../static/images/tb/sy-3.png',
        '../../../static/images/tb/sy-4.png',
        '../../../static/images/tb/sy-5.png',
        '../../../static/images/tb/sy-6.png',
        '../../../static/images/tb/sy-7.png'
    ];

    switch (number + 1) {
        case 1:
            groupName = '工作队';
            this.sidebarItems.forEach((item, index) => {
                item.url = index === 0 ? `../../../static/images/tb/ac-1.png` : sidebarImages[index];
            });
            break;
        case 2:
            groupName = '待筛查人员';
            this.sidebarItems.forEach((item, index) => {
                item.url = index === 1 ? `../../../static/images/tb/ac-2.png` : sidebarImages[index];
            });
            break;
        case 3:
            groupName = '采集组';
            this.sidebarItems.forEach((item, index) => {
                item.url = index === 2 ? `../../../static/images/tb/ac-3.png` : sidebarImages[index];
            });
            break;
        case 4:
            groupName = 'PDD组';
            this.sidebarItems.forEach((item, index) => {
                item.url = index === 3 ? `../../../static/images/tb/ac-4.png` : sidebarImages[index];
            });
            break;
        case 5:
            groupName = 'DR/CT组';
            this.sidebarItems.forEach((item, index) => {
                item.url = index === 4 ? `../../../static/images/tb/ac-5.png` : sidebarImages[index];
            });
            break;
        case 6:
            groupName = '痰检组';
            this.sidebarItems.forEach((item, index) => {
                item.url = index === 5 ? `../../../static/images/tb/ac-6.png` : sidebarImages[index];
            });
            break;
        case 7:
            groupName = '心电图组';
            this.sidebarItems.forEach((item, index) => {
                item.url = index === 6 ? `../../../static/images/tb/ac-7.png` : sidebarImages[index];
            });
            break;
        default:
            groupName = ''; // handle default case if needed
            break;
    }

    this.nav = [{ value: '数据同步' }, { value: groupName, isActive: true }];
},
		clearScreenId() {
			this.queryParams.screenId = undefined;
		},
		clearGroupName() {
			this.queryParams.groupName = undefined;
		},
		close() {
			this.show = false;
			// console.log('close');
		},
		//获取对应分组的上次同步时间
		getLastSynchronizeTime(value){
			let keyName=''
			switch(value){
				case 0: keyName='workTeam'; break;
				case 1: keyName='person'; break;
				case 2: keyName='collect'; break;
				case 3: keyName='ppd'; break;
				case 4: keyName='drct'; break;
				case 5: keyName='sputum'; break;
				case 6: keyName='electrocardiogram'; break;
			}
			uni.getStorage({
			    key: keyName
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
			this.pageNo = e.current; // 更新当前页码
			this.search(this.activeItem);
		},
		// 生成年份
		generateYear() {
			let nowYear = new Date().getFullYear();
			let lastYear = nowYear - 1;
			let nextYear = nowYear + 1;
			// 添加新的年份到range数组中
			this.yearList.push(lastYear);
			this.yearList.push(nowYear);
			this.yearList.push(nextYear);
			// 添加新的年份到range数组中
			this.range.push({ value: lastYear, text: String(lastYear) });
			this.range.push({ value: nowYear, text: String(nowYear) });
			this.range.push({ value: nextYear, text: String(nextYear) });
		},
		// 选择年份
		changeYear(e) {
			// 查询用户表中是否有数据
			SynchronizeApi.getWorkTeamCount(null).then((res) => {
				// console.log(res);
				if (res[0].num <= 0) {
					uni.showToast({
						title: '请先同步工作队伍',
						mask: true,
						icon: 'none',
						duration: 1500
					});
					return;
				} else {
					SynchronizeApi.getYear(uni.$user.name).then((res) => {
						// console.log(res);
						if (!res[0].year) {
							uni.$user.year = e;
							// uni.$person.year=e
							// console.log(uni.$user.year);
							SynchronizeApi.insertYear(e);
						} else {
							// console.log("e:", e);
							let self = this;

							uni.showModal({
								title: '提示信息',
								content: '确定要切换为当前所选工作年度吗',
								showCancel: true,
								confirmText: '确认',
								success: function (res) {
									if (res.confirm) {
										uni.$user.year = e;
										// uni.$person.year=e
										// console.log(uni.$year);
										SynchronizeApi.insertYear(e);
										uni.showToast({
											title: '成功',
											mask: true,
											icon: 'none',
											duration: 1500
										});
									} else if (res.cancel) {
										uni.showToast({
											title: '取消',
											mask: true,
											icon: 'none',
											duration: 1500
										});
									}
								}
							});
						}
					});
				}
			});
		},
		getPwd() {
			SynchronizeApi.getUserInfo(uni.$user.id).then((resp) => {
				let temp = resp;

				uni.$person = temp.data;

				this.user.pwd = temp.data.pwd;
			});
		},
		checkNull(value) {
			if (value == null || value == 'null') {
				return '';
			}
			return value;
		},
		// 筛查状态
		screenStatus(value) {
			if (value == null || value == 'null') {
				return '';
			}
			return screenStatusMap[value];
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
		/**
		 * 覆盖区划数据
		 */
		districtCover(){
			uni.showLoading({
				title: '加载中...',
				mask: true
			});
			DistrictApi.coverData()
			uni.showToast({
				title: '载入成功',
				icon: 'success',
				duration: 1000
			}) 
			setTimeout(()=>{
				uni.hideLoading();
			},1000)
		},
		//工作队伍 同步
		workTeam() {
			let self = this;
			let screenPointList =[]
			
			// console.log(uni.$user);
			SynchronizeApi.getUserInfo(uni.$user.id).then((res) => {
				if (res.data.screenPoint === '' || res.data.screenPoint === null) {
					uni.showToast({
						title: '当前账号暂未分配工作队',
						mask: true,
						icon: 'none',
						duration: 2000
					});
					return;
				}
				// screenPointList = res.data.screenPoint.split(',');
				
				// 获取用户所在筛查点信息
				screenPointList = uni.$user.screenPointList;
				// console.log(screenPointList);
				// 筛查点名称-单位-年份 数组
				let screenPoints = screenPointList.map(item => item.name);
				
				
				console.log(screenPointList);
				if (screenPointList.length > 1) {
					uni.showActionSheet({
						itemList: screenPoints,
						success: function (resp) {
							// console.log('选择了' + res.tapIndex);
							// 根据选择的索引进行相应的处理
							uni.showModal({
								title: '提示信息',
								content: '是否同步' + screenPointList[resp.tapIndex].name + '的工作队伍信息?',
								cancelText: '取消',
								confirmText: '确认',
								success: function (res) {
									if (res.confirm) {
										// 记录本次同步时间(存缓存)
										let time = self.getCurrentTime()
										uni.setStorage({
											key:'workTeam',
											data:time
										})
										self.synchronizeTime=time
										
										// 同步之前 清除全部工作队伍信息
										SynchronizeApi.truncateTable(tbScreenUser);
										// 清除分组
										self.user.groupName = '工作队,待筛查人员,';
										
										// 获取当前所选筛查点信息
										let screenPointInfo = screenPointList[resp.tapIndex].name.split('-')
										
										// 存储全局变量(筛查点)
										uni.$user.screenPoint = screenPointInfo[0];
										self.queryParams.screenPoint = screenPointInfo[0];
										// 存储全局变量（筛查单位）
										uni.$user.agency = screenPointInfo[1];
										uni.$person.agency = screenPointInfo[1];
										// 存储全局变量（工作年度）
										uni.$user.year = screenPointInfo[2];
										uni.$person.year = screenPointInfo[2];
										// console.log(screenPointList[resp.tapIndex]);
										//获取pc端 工作队伍信息
										SynchronizeApi.getWorkTeamInfo(screenPointList[resp.tapIndex].screenPointId).then((res) => {
											// console.log(res.data);
											// console.log(uni.$user);
											// console.log(res);
											if (uni.$user.year) {
												// 过滤出与当前工作年度不同的数据
												let differentYears = res.data.filter(
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

											// console.log(res);
											// 分组权限
											let groups = res.data.filter((item) => item.id === uni.$user.id);
											// console.log(groups);
											let groupList = groups[0].groupName.toString().split(',');
											if (groupList.length > 1) {
												// console.log(self.user.groupName);
												groupList.forEach((item) => {
													if (!String(self.user.groupName).includes(item)) {
														self.user.groupName += item + ',';
													}
												});
											} else {
												// console.log(self.user.groupName);
												if (!String(self.user.groupName).includes(groupList[0])) {
													self.user.groupName += groupList[0] + ',';
												}
											}
											// console.log(self.user.groupName);
											uni.$user.groupName = groups[0].groupName;
											uni.$person.groupName = groups[0].groupName;

											self.workTeamData = res.data;
											self.workTeamData.forEach((item) => {
												dbUtils.addTabItem(dbName, tbScreenUser, item);
											});
											
											// SynchronizeApi.insertYear(self.yearList[res.tapIndex])
											// 同步完成 再查询一遍
											self.search(0);
										});

										uni.showToast({
											title: '同步成功',
											mask: true,
											icon: 'success',
											duration: 2000
										});
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
						}
					});
				} else {
					uni.showModal({
						title: '提示信息',
						content: '是否同步' + screenPointList[0].name + '的工作队伍信息?',
						cancelText: '取消',
						confirmText: '确认',
						success: function (res) {
							if (res.confirm) {
								// 记录本次同步时间(存缓存)
								let time = self.getCurrentTime()
								uni.setStorage({
									key:'workTeam',
									data:time
								})
								self.synchronizeTime=time
								
								// 同步之前 清除全部工作队伍信息
								SynchronizeApi.truncateTable(tbScreenUser);
								// 清除分组
								self.user.groupName = '工作队,待筛查人员,';

								// 获取当前所选筛查点信息
								let screenPointInfo = screenPointList[0].name.split('-')
								let pointId=uni.$user.screenPointList[0].screenPointId
								// 存储全局变量(筛查点)
								uni.$user.screenPoint = screenPointInfo[0];
								self.queryParams.screenPoint = screenPointInfo[0];
								// 存储全局变量（筛查单位）
								uni.$user.agency = screenPointInfo[1];
								uni.$person.agency = screenPointInfo[1];
								// 存储全局变量（工作年度）
								uni.$user.year = screenPointInfo[2];
								uni.$person.year = screenPointInfo[2];

								// console.log(screenPointList[resp.tapIndex]);
								console.log(screenPointInfo);
								//获取pc端 工作队伍信息
								SynchronizeApi.getWorkTeamInfo(screenPointList[0].screenPointId).then((res) => {
									if (uni.$user.year) {
										// 过滤出与当前工作年度不同的数据
										let differentYears = res.data.filter((item) => item.year != uni.$user.year);
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

									// 分组权限
									let groups = res.data.filter((item) => item.id === uni.$user.id);
									let groupList = groups[0].groupName.toString().split(',');
									if (groupList.length > 1) {
										// console.log(self.user.groupName);
										groupList.forEach((item) => {
											if (!String(self.user.groupName).includes(item)) {
												self.user.groupName += item + ',';
											}
										});
									} else {
										// console.log(self.user.groupName);
										if (!String(self.user.groupName).includes(groupList[0])) {
											self.user.groupName += groupList[0] + ',';
										}
									}
									// console.log(self.user.groupName);
									uni.$user.groupName = groups[0].groupName;
									uni.$person.groupName = groups[0].groupName;

									// console.log(res);
									self.workTeamData = res.data;
									self.workTeamData.forEach((item) => {
										dbUtils.addTabItem(dbName, tbScreenUser, item);
									});
									// SynchronizeApi.insertYear(self.yearList[res.tapIndex])

									// 同步完成 再查询一遍
									self.search(0);
								});

								uni.showToast({
									title: '同步成功',
									mask: true,
									icon: 'success',
									duration: 2000
								});
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
				}
			});
		},
		// 查看表数据
		showTableData(value) {
			const urlMap = {
				1: '/pages/tb/synchronize/table1',
				2: '/pages/tb/synchronize/table2',
				3: '/pages/tb/synchronize/table3',
				4: '/pages/tb/synchronize/table4',
				5: '/pages/tb/synchronize/table5',
				7: '/pages/tb/synchronize/test'
			};
			if (urlMap[value]) {
				uni.navigateTo({
					url: urlMap[value]
				});
			}
		},
		// 查看本地库表数据
		showLocalTableData(value) {
			const urlMap = {
				1: '/pages/tb/synchronize/localTable1',
				2: '/pages/tb/synchronize/localTable2',
				3: '/pages/tb/synchronize/localTable3',
				4: '/pages/tb/synchronize/localTable4',
				5: '/pages/tb/synchronize/localTable5',
				6: '/pages/tb/synchronize/localTable6',
				7: '/pages/tb/synchronize/localTest'
			};
			if (urlMap[value]) {
				uni.navigateTo({
					url: urlMap[value]
				});
			}
		},
		getGroups() {
			// 从本地数据库获取分组
			// 无数据-提示先同步工作队
			// 有数据-判断是否重复
			SynchronizeApi.getGroups(uni.$user.name).then((res) => {
				// console.log(res);
				if (res.length <= 0) {
					uni.showToast({
						title: '请先同步工作队伍',
						mask: true,
						icon: 'none',
						duration: 2000
					});
				} else {
					let groupList = res[0].groupName.toString().split(',');
					// console.log(groupList);
					if (groupList.length > 1) {
						groupList.forEach((item) => {
							if (!String(this.user.groupName).includes(item)) {
								this.user.groupName += item + ',';
							}
						});
					} else {
						if (!String(this.user.groupName).includes(groupList[0])) {
							this.user.groupName += groupList[0];
						}
					}
					uni.$user.groupName = res[0].groupName;
					uni.$person.groupName = res[0].groupName;
				}
			});
		},
		// 处理侧边栏菜单项点击事件
		handleSidebarItemClick(item) {
			if (String(this.user.groupName).includes('队长')) {
				if (item != 0) {
					// 查询用户表中是否有数据
					SynchronizeApi.getWorkTeamCount(null).then((res) => {
						// console.log(res);
						if (res[0].num <= 0) {
							uni.showModal({
								title: '提示信息',
								content: '请先同步工作队伍',
								showCancel: false,
								confirmText: '确认',
								success: function (res) {
									if (res.confirm) {
									} else if (res.cancel) {
									}
								}
							});
						} else {
							// 当前激活的菜单项
							this.activeItem = item;
							this.updateArray(item);
							// 获取对应分组的上次同步时间
							this.getLastSynchronizeTime(item)
							this.pageNo = 1;

							this.reset();
						}
					});
				} else {
					// 当前激活的菜单项
					this.activeItem = item;
					this.updateArray(item);
					// 获取对应分组的上次同步时间
					this.getLastSynchronizeTime(item)
					this.pageNo = 1;

					this.reset();
				}
			} else {
				// 其他队员
				if (String(this.user.groupName).includes(this.sidebarItems[item].title)) {
					if (item != 0) {
						// 查询用户表中是否有数据
						SynchronizeApi.getWorkTeamCount(null).then((res) => {
							// console.log(res);
							if (res[0].num <= 0) {
								uni.showModal({
									title: '提示信息',
									content: '请先同步工作队伍',
									showCancel: false,
									confirmText: '确认',
									success: function (res) {
										if (res.confirm) {
										} else if (res.cancel) {
										}
									}
								});
							} else {
								// 当前激活的菜单项
								this.activeItem = item;
								this.updateArray(item);
								// 获取对应分组的上次同步时间
								this.getLastSynchronizeTime(item)
								this.pageNo = 1;

								this.reset();
							}
						});
					} else {
						// 当前激活的菜单项
						this.activeItem = item;
						this.updateArray(item);
						// 获取对应分组的上次同步时间
						this.getLastSynchronizeTime(item)
						this.pageNo = 1;

						this.reset();
					}
				} else {
					uni.showModal({
						title: '提示信息',
						content: '无权限操作此分组的数据',
						showCancel: false,
						confirmText: '确认',
						success: function (res) {
							if (res.confirm) {
							} else if (res.cancel) {
							}
						}
					});
				}
			}
		},
		handleQuery() {
			let event = { type: 'current', current: 1 };
			this.change(event);
			// this.pageNo = 1;
			// this.search(item)
		},
		// 访问本地数据库
		search(item) {
			switch (item) {
				case 0:
					SynchronizeApi.getWorkTeamCount(this.queryParams.groupName).then((res) => {
						// console.log(res);
						if (res[0].num > 0) {
							this.total = res[0].num;
							SynchronizeApi.getWorkTeamData(this.queryParams.groupName, this.pageNo, this.pageSize).then(
								(resp) => {
									this.pageData = resp;
								}
							);
						} else {
							this.total = 0;
							this.pageData = [];
						}
					});
					break;
				case 1:
					SynchronizeApi.getPersonCount(this.queryParams.screenId, this.queryParams.screenPoint,null).then(
						(res) => {
							// console.log(this.queryParams);
							// console.log(res);
							if (res[0].num > 0) {
								this.total = res[0].num;
								SynchronizeApi.getPersonData(
									this.queryParams.screenId,
									this.queryParams.screenPoint,
									null,
									this.pageNo,
									this.pageSize
								).then((resp) => {
									// console.log(resp);
									this.pageData = resp;
								});
							} else {
								this.total = 0;
								this.pageData = [];
							}
						}
					);
					break;
				case 2:
					SynchronizeApi.getCollectCount(this.queryParams.screenId, this.queryParams.screenPoint,null).then(
						(res) => {
							if (res[0].num > 0) {
								this.total = res[0].num;
								SynchronizeApi.getCollectData(
									this.queryParams.screenId,
									this.queryParams.screenPoint,
									null,
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
						}
					);
					break;
				case 3:
					SynchronizeApi.getPpdCount(this.queryParams.screenId, null, this.queryParams.screenPoint,null).then(
						(res) => {
							// console.log(res);
							if (res[0].num > 0) {
								this.total = res[0].num;
								SynchronizeApi.getPpdData(
									this.queryParams.screenId,
									null,
									this.queryParams.screenPoint,
									null,
									this.pageNo,
									this.pageSize
								).then((resp) => {
									this.pageData = resp.map((item) => ({
										...item,
										injectionAgency: this.agency
									}));
									// console.log(this.pageData);
									// this.pageData=resp
								});
							} else {
								this.total = 0;
								this.pageData = [];
							}
						}
					);
					break;
				case 4:
					SynchronizeApi.getDrCtCount(this.queryParams.screenId, null, this.queryParams.screenPoint,null).then(
						(res) => {
							// console.log(res);
							if (res[0].num > 0) {
								this.total = res[0].num;
								SynchronizeApi.getDrCtData(
									this.queryParams.screenId,
									null,
									this.queryParams.screenPoint,
									null,
									this.pageNo,
									this.pageSize
								).then((resp) => {
									this.pageData = resp;
								});
							} else {
								this.total = 0;
								this.pageData = [];
							}
						}
					);
					break;
				case 5:
					SynchronizeApi.getSputumExaminationCount(
						this.queryParams.screenId,
						null,
						this.queryParams.screenPoint
					).then((res) => {
						// console.log(res);
						if (res[0].num > 0) {
							this.total = res[0].num;
							SynchronizeApi.getSputumExaminationData(
								this.queryParams.screenId,
								null,
								this.queryParams.screenPoint,
								this.pageNo,
								this.pageSize
							).then((resp) => {
								// console.log(resp);
								this.pageData = resp;
							});
						} else {
							this.total = 0;
							this.pageData = [];
						}
					});
					break;
				case 6:
					SynchronizeApi.getElectrocardiogramCount(
						this.queryParams.screenId,
						this.queryParams.screenPoint
					).then((res) => {
						// console.log(res);
						if (res[0].num > 0) {
							this.total = res[0].num;
							SynchronizeApi.getElectrocardiogramData(
								this.queryParams.screenId,
								this.queryParams.screenPoint,
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
					break;
			}
		},
		reset() {
			this.pageNo = 1;
			this.queryParams.screenId = undefined;
			this.queryParams.groupName = undefined;
			// todo 待补充

			this.search(this.activeItem);
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
			// h = h < 10 ? '0' + h : h; //小时补0
			// let m = date.getMinutes();
			// m = m < 10 ? '0' + m : m; //分钟补0
			// let s = date.getSeconds();
			// s = s < 10 ? '0' + s : s; //秒补0
			return y + '-' + MM + '-' + d; //年月日
			// return y + '-' + MM + '-' + d + ' ' + h + ':' + m + ':' + s; //年月日时分秒
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
			let label=''
			for (let item of screenType) {
				   if (item.value==value){
					   // console.log(item.label)
					   label= item.label
			   }
			}
			return label
		},
		//胸片结果转换
		chestRadiographOutcome(value) {
			return getLabelByValue(ctOutcome, value);
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
		// 痰标本类型
		sputumType(value) {
			if (value == null || value == 'null') {
				return '';
			}
			let data = value.toString().split('');
			let str = '';

			for (let i = 0; i < data.length; i++) {
				str += getLabelByValue(sputumType, parseInt(data[i]));
				if (i < data.length - 1) {
					str += ',';
				}
			}
			return str;
		},
		// 痰检结果
		sputumCheck(value) {
			if (value === null) {
				return '未审核';
			}
			if (value === 'null' || value === '') {
				return '全部合格';
			}

			let data = value.toString().split('');
			// console.log(data);

			let str = '';

			for (let i = 0; i < data.length; i++) {
				str += getLabelByValue(sputumCheck, parseInt(data[i]));
				if (i < data.length - 1) {
					str += '、';
				}
			}
			return str + '不合格';
		},
		//注射方式
		injection(value) {
			return injectionWayMap[value];
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
		// 清除所有表数据
		truncateAllTable() {
			SynchronizeApi.truncateTable(tbScreenUser);
			SynchronizeApi.truncateTable(tbScreenPerson);
			SynchronizeApi.truncateTable(tbScreenCollect);
			SynchronizeApi.truncateTable(tbScreenPpd);
			SynchronizeApi.truncateTable(tbScreenChestRadiograph);
			SynchronizeApi.truncateTable(tbScreenSputumExamination);
			SynchronizeApi.truncateTable(tbScreenElectrocardiogram);
			SynchronizeApi.truncateTable(tbScreenSum);
			SynchronizeApi.truncateTable(tbScreenImages);
			SynchronizeApi.truncateTable(tbScreenReagent);
			SynchronizeApi.truncateTable(tbScreenConsume);
			SynchronizeApi.truncateTable(tbScreenConsumeRecord);
			this.search(this.activeItem);
		},
		isPasswordMatch(value) {
			let pwd = CryptoJS.MD5(value).toString();
			let newPwd = pwd + 'pingban';

			return newPwd === this.user.pwd ? true : false;
		},
		// 清除所有数据
		deleteData() {
			let self = this;
			uni.showModal({
				title: '提示信息',
				content: '所有分组的数据都将被清除，请输入密码确认',
				cancelText: '取消',
				confirmText: '确认',
				editable: true,
				placeholderText: '请输入密码',
				success: function (res) {
					if (res.confirm) {
						// console.log(res.content);
						if (res.content != '') {
							let match = self.isPasswordMatch(res.content);
							// console.log(self.user.pwd);
							if (match) {
								self.truncateAllTable();
                SynchronizeApi.delImgFromDir('_doc/uniapp_save/')
								uni.showToast({
									title: '清除成功',
									mask: true,
									icon: 'success',
									duration: 2000
								});
								self.activeItem = 0;
							} else {
								uni.showToast({
									title: '密码错误',
									icon: 'none',
									mask: true,
									duration: 1500
								});
							}
						} else {
							uni.showToast({
								title: '密码不能为空',
								mask: true,
								icon: 'none',
								duration: 1500
							});
						}
					} else if (res.cancel) {
						uni.showToast({
							title: '取消',
							mask: true,
							icon: 'error',
							duration: 1500
						});
					}
				}
			});
		}
	}
};
</script>

<style lang="scss" scoped>
.sidebar-main {
	background: rgba(244, 247, 252, 1);
	display: flex;
	.sidebar-container {
		width: 10vw;
	}
	.sidebar-right {
		margin-left: 50px;
		.sidebar-right-top1 {
			height: 70px;
			display: flex;
			align-items: center;
			justify-content: space-between;
			.top-left {
				display: flex;
				align-items: center;
				justify-content: flex-start;
				margin-left: 5px;
			}
			.top-right-2 {
				display: flex;
				align-items: center;
				justify-content: flex-end;
				position: absolute;
				top: 10px;
				right: 10rpx;
				.custom-style {
					color: rgba(0, 148, 89, 1);
					border: 1px solid rgba(0, 148, 89, 1);
				}
				.custom-style1 {
					color: rgba(220, 37, 29, 1);
					border: 1px solid rgba(220, 37, 29, 1);
				}
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
		.sidebar-right-top {
			background-color: #fff;
			display: flex;
		}
		.sidebar-right-bom {
			width: 85vw;
			overflow-x: auto;
		}
	}
}
.top-left {
	display: flex;
	align-items: center;
	justify-content: flex-start;
	margin-left: 5px;
}
.top-right {
	display: flex;
	align-items: center;
	justify-content: flex-end;
}
.top-right-1 {
	display: flex;
	align-items: center;
	justify-content: flex-end;
	position: absolute;
	top: 80px;
	right: 10rpx;
	.custom-style {
		color: rgba(0, 148, 89, 1);
		border: 1px solid rgba(0, 148, 89, 1);
	}
	.custom-style1 {
		color: rgba(220, 37, 29, 1);
		border: 1px solid rgba(220, 37, 29, 1);
	}
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
.sidebar {
	background-color: #f0f0f0;
	width: 200px;
}

.sidebar-header {
	width: 120px;
	height: 30px;
	margin: 10px 0 20px 10px;
}

.sidebar-menu {
	margin-top: 10px;
	text-align: left;
}

.sidebar-item {
	padding: 10px;
	margin-top: 5px;
	cursor: pointer;
	font-size: 16px;
	text-align: center;
	transition: background-color 0.3s;
	text-align: left;
}

.active {
	color: #fff;
	background: rgba(36, 93, 209, 1);
	text-align: left;
}

.uni-td {
	word-break: break-all; /* 在任意字符间断开 */
	word-wrap: break-word; /* 同上，更通用的写法 */
	hyphens: auto; /* 自动连字符 */
}
.image-transform{
	transform: scaleX(3.5) scaleY(1.25) rotate(90deg);
	width: 4rem;
	height: 2rem;
}
</style>
