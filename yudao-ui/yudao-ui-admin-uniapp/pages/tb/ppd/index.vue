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
			<view class="card-main" style="width: 420px">
				<view class="card-top various1">采集人数</view>
				<view class="card-tle">
					<view class="card-sp">
						<view class="card-text">
							今日
							<span class="sp-text">
								{{ statisticsData.dayNum }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
						<view class="card-text" style="margin-left: 40px">
							阴性
							<span class="sp-text">
								{{ statisticsData.dayYinNum }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
						<view class="card-text1" style="margin-left: 40px">
							阳性
							<span class="sp-text">
								{{ statisticsData.dayYangNum }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
					<view class="line" style="width: 340px"></view>
					<view class="card-sp">
						<view class="card-text">
							本月
							<span class="sp-text">
								{{ statisticsData.monthlyNum }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
						<view class="card-text" style="margin-left: 40px">
							阴性
							<span class="sp-text">
								{{ statisticsData.monthlyYinNum }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
						<view class="card-text1" style="margin-left: 40px">
							阳性
							<span class="sp-text">
								{{ statisticsData.monthlyYangNum }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
					<view class="line" style="width: 340px"></view>
					<view class="card-sp">
						<view class="card-text">
							本年
							<span class="sp-text">
								{{ statisticsData.yearNum }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
						<view class="card-text" style="margin-left: 40px">
							阴性
							<span class="sp-text">
								{{ statisticsData.yearYinNum }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
						<view class="card-text1" style="margin-left: 40px">
							阳性
							<span class="sp-text">
								{{ statisticsData.yearYangNum }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
				</view>
			</view>
			<view class="card-main" style="width: 320px;">
				<view class="card-top various2">试剂类型统计</view>
				<view class="card-tle">
					<view class="card-sp">
						<view class="card-text">
							PDD
							<span class="sp-text">
								{{ statisticsData.ppdNum }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
					<view class="line"></view>
					<view class="card-sp">
						<view class="card-text">
							EC
							<span class="sp-text">
								{{ statisticsData.ecNum }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
					<view class="line"></view>
					<view class="card-sp">
						<view class="card-text">
							IGRA
							<span class="sp-text">
								{{ statisticsData.igraNum }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
				</view>
			</view>
			<view class="card-main" v-if="showDetail">
				<view class="card-top various3">症状人数统计</view>
				<view class="card-tle">
					<view class="card-sp">
						<view class="card-text">
							硬结横经&ge;10mm
							<span class="sp-text">
								{{ statisticsData.xLengthGtTenNum }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
						<view class="card-text1" style="margin-left: 70px">
							双圈
							<span class="sp-text" style="color: #000">
								{{ statisticsData.doubleCircleNum }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
					<view class="line"></view>
					<view class="card-sp">
						<view class="card-text">
							硬结纵经&le;10mm
							<span class="sp-text">
								{{ statisticsData.yLengthLtTenNum }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
						<view class="card-text1" style="margin-left: 70px">
							水泡
							<span class="sp-text" style="color: #000">
								{{ statisticsData.blisterNum }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
					</view>
					<view class="line"></view>
					<view class="card-sp">
						<view class="card-text">
							淋巴管炎
							<span class="sp-text">
								{{ statisticsData.lymphangitisNum }}
								<span style="font-size: 18px">人</span>
							</span>
						</view>
						<view class="card-text1" style="margin-left: 97px">
							坏死
							<span class="sp-text" style="color: #000">
								{{ statisticsData.necrosisNum }}
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
						style="width: 230px"
						cancelButton="none"
						@clear="clearName"
					></uni-search-bar>
				</view>
			</view>
			<!-- 输入框和搜索按钮 -->
			<view class="statistics" style="justify-content: flex-start">
				<span style="font-size: 18px; margin-left: 10px;width:150rpx">筛查日期</span>
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
						text="重置"/>
					<up-button
						v-if="showFlow"
						@click="reviewProcess"
						style="width: 115px"
						type="primary"
						:plain="true"
						text="查看筛查流程"/>
			</view>
		</view>
    </view>

      <view class="uni-container">
        <uni-table :style="tableStyle" ref="table" :loading="loading" stripe emptyText="暂无更多数据">
          <uni-tr>
            <uni-th width="60" align="center">序号</uni-th>
            <uni-th width="110" align="center">筛查编号</uni-th>
            <uni-th width="100" align="center">姓名</uni-th>
            <uni-th width="180" align="center">筛查次序/时间</uni-th>
            <uni-th width="180" align="center" v-if="showFlow">下一步检查</uni-th>
            <uni-th width="170" align="center">操作</uni-th>
            <uni-th width="180" align="center">身份证号</uni-th>
            <uni-th width="60" align="center">性别</uni-th>
            <uni-th width="60" align="center">年龄</uni-th>
            <uni-th width="60" align="center">民族</uni-th>
            <uni-th width="150" align="center">筛查点</uni-th>
            <uni-th width="120" align="center">第一人群分类</uni-th>
            <uni-th width="120" align="center">重点人群分类</uni-th>
            <uni-th width="150" align="center">患者信息操作</uni-th>
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
                  placeholder="选择筛查次序"
                  :clear="false"
                  placement="top"
                  @click="toggleOverflow(item)"
                  @change="changeOrder(item)"
                ></uni-data-select>
              </view>
            </uni-td>
            <uni-td align="left" v-if="showFlow">
              <view v-if="checkLength(item.next)">{{ item.next }}</view>
              <view v-else>
                <up-text text="点击查看" type="success" @click="clickTextHandler(item.next)"></up-text>
              </view>
            </uni-td>
            <uni-td align="center">
              <view style="display: flex; justify-content: space-around; align-items: center">
                <u-button
                  @click="gather(item)"
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
                  新增注射
                </u-button>
                <u-button
                  class="btn-span"
                  style="
                    margin: 0 10px;
                    background-color: #fff;
                    border: 1px solid #fa9f37;
                    color: #fa9f37;
                    display: flex;
                    flex-direction: column;
                    justify-content: center;
                    height: 6vh;
                    width: 15vh;
                    font-size: 16px;
                  "
                  @click="modify(item)"
                >
                  修改结果
                </u-button>
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
              <view style="display: flex; justify-content: space-around; align-items: center">
                <span
                  class="btn-span"
                  style="
                    background-color: #fff;
                    border: 1px solid rgba(223, 65, 65, 1);
                    color: rgba(223, 65, 65, 1);
                    padding: 5px 10px;
                  "
                  @click="revise(item)"
                >
                  修改
                </span>
                <span
                  class="btn-span"
                  style="
                    background-color: #fff;
                    border: 1px solid rgba(80, 104, 242, 1);
                    color: rgba(80, 104, 242, 1);
                    padding: 5px 10px;
                  "
                  @click="print(item)"
                >
                  打印
                </span>
              </view>
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
        <!-- 注射弹窗 -->
        <u-popup :show="show" mode="center">
          <view class="injection">
            <view class="injection-tle">注射试剂</view>
            <up-form-item label="选择试剂" labelWidth="200rpx" style="padding-left: 45rpx;">
            <uni-data-select
              style="margin-top: 15rpx;margin-right: 25rpx;"
                v-model="regentId"
                :localdata="regentList"
              :clear="false"
                @change="selectRegentList"/>
            </up-form-item>
              <up-row>
                <up-col span="7">
                  <u-radio-group style="margin-left: 20rpx;" v-model="injectionReagent.reagentType" placement="column" disabled>
                    <u-radio
                      v-for="(item, index) in radiolist1"
                      :key="index"
                      :label="item.name"
                      :name="item.value"
                      labelSize="20px"
                      iconSize="23px"
                      size="25px"
                    ></u-radio>
                  </u-radio-group>
                </up-col>
                <up-col span="5" >
                  <view v-if="injectionReagent.reagentSpecsNum">
                    <up-row>转换系数 ：{{this.injectionReagent.reagentSpecsNum}} (人次)</up-row>
                    <up-row>现有库存 ：{{this.injectionReagent.currentNumber}}</up-row>
                    <up-row>已消耗数量 ：{{this.injectionReagent.changeNumber}}</up-row>
                  </view>
                </up-col>
              </up-row>

            </view>
          <view class="injection-btn">
            <up-button class="btn-1" @click="show = false">取消</up-button>
            <up-button class="btn-2" @click="injectionUpload()">保存</up-button>
          </view>
        </u-popup>

        <!-- 打印弹窗 -->
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

        <!-- 扫描弹框 -->
        <u-popup :show="printShow" mode="center" @close="printClose" @open="open">
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
            <up-button class="btn-1" text="取消" @click="printClose">取消</up-button>
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
        <!--    下一步检查弹框-->
        <u-popup
          :show="showNextText"
          mode="center"
          @close="showNextText = false"
          :round="10"
          style="height: 100rpx"
        >
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
import { commitTransaction, openTransaction, rollbackTransaction, tbScreenConsume } from '@/utils/sqlite';

const ocrModule = uni.requireNativePlugin('YY-TomatoOCR');
const mpaasScanModule = uni.requireNativePlugin('Mpaas-Scan-Module');
import dbUtils from '@/uni_modules/zjy-sqlite-manage/components/zjy-sqlite-manage/dbUtils';
import { updateOne } from '@/utils/screenSum.js';
import {
	dbName,
  tbScreenConsumeRecord,
	tbScreenPerson,
	tbScreenCollect,
	tbScreenPpd,
	getPatientPage,
	getGather,
	tbScreenSum
} from '@/utils/sqlite';
import {
	getPpdList,
	getPpdBypersonIdToOrder,
	getPddToId,
	getBypersonIdAndScreenOrder,
	statisticsNum
} from '@/utils/ppd.js';
import { items1, items2, items3, personTypeForProcess, ethnic } from '@/utils/dict.js';
import UQRCode from '../../../uni_modules/Sansnn-uQRCode/js_sdk/uqrcode/uqrcode.js';
import { tbScreenImages, splitDecimalIntoList, getCollectOen } from '@/utils/sqlite.js';
import { parsePatientType } from '@/utils/common.js';
import { parseNext } from '../../../utils/common';
import { personType } from '../../../utils/dictData';
import * as regentApi from '@/api/screen/regent'
import * as consumeApi from '@/api/screen/consume'
import * as consumeRecordApi from '@/api/screen/consumeRecord'
import {staticsConsumeById} from "../../../api/screen/consume";
export default {
	data() {
		return {
			showFlow:uni.$showFlow,
			showDetail:false,
			nav: [
				{
					value: '常规筛查'
				},
				{
					value: 'PPD组',
					isActive: true
				}
			],
			personTypeForProcess,
			screenNum: '',
			showSelectCrowd: false, // 控制子组件显示的布尔值
			overflowVisible: false, // 初始时设置为 false
			// 每页数据量
			pageSize: 5,
			// 当前页
			pageCurrent: 1,
			loading: false,
			// 数据总量
			total: 0,
			// 姓名
			searchName: '',
			// 身份证
			searchIdCard: null,
			//民族列表
			ethnic: [{ value: 61, text: '汉族' }],
			// 归属地区
			searchRegion: '',
			// 人群分类
			searchClassify: '',
			//列表数据
			pageData: [],
			//注射弹窗
			show: false,
			//患者打印弹窗
			showGather: false,
			showProcess: false,
			// 查看筛查流程中的索引
			tabIndex: 0,
			// 学生类型单选值
			studentType: 0,
			ageTypeList: [{ name: '0-5岁' }, { name: '6-14岁' }, { name: '≥15岁' }],
			lineWith: 30,
			//扫描弹窗
			printShow: false,
			injectionReagent:{},
			//注射方式
			radiovalue1: null,
			radiolist1: [
				{
					name: 'PPD注射',
					value: 1
				},
				{
					name: 'EC注射',
					value: 2
				},
				{
					name: 'IGRA注射',
					value: 3
				}
			],
			//扫描
			scanVal: 1,
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
			// 筛查条件下面的统计值
			statisticsData: {},
			//操作对象信息
			person: {},
			//新增注射时间
			screenTime: '',
			//第一人群分类
			firstType: [],
			//第二人群分类
			secondType: [],
			//筛查时间
			dateRange: [],
			showNextText: false,
			nextText: [],
			regentId: null,  // 选择的试剂id
			regentList:[]  // 试剂下拉数据
		};
	},
	//页面加载时自动调用
	async onLoad() {
		// console.log(uni.$person);
		this.ethnic = ethnic;
		this.dateScreening();
		this.getRegentList()
		this.getNavItems(uni.$screenType);
		
	},
	onShow() {
		this.search();
		// console.log(111);
		this.statisticalMethod();
	},
	computed: {
		tableStyle() {
			return {
				overflow: this.overflowVisible ? 'visible' : 'auto'
			};
		}
	},
	methods: {
		async getRegentList(){
			this.regentList=[]
			this.injectionReagent={}
			let data=await regentApi.getDataFromLocal()
			for (var i = 0; i < data.length; i++) {
				let item={}
				item.text=data[i].name
				item.value=data[i].id
				this.regentList.push(item)
			}
		},
		getNavItems(screenType) {
			switch (screenType) {
				case 1:
					this.nav = [{ value: '常规筛查' }, { value: 'PPD组', isActive: true }];
					break;
				case 2:
					this.nav = [{ value: '新生入学筛查' }, { value: 'PPD组', isActive: true }];
					break;
				case 3:
					this.nav = [{ value: '应急筛查' }, { value: 'PPD组', isActive: true }];
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
							this.printShow = false;
						} else {
							uni.showToast({
								icon: 'error',
                duration:3000,
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
		async toggleOverflow(val) {
			const data = await getBypersonIdAndScreenOrder(val.orderVal, val.id);
			// console.log(data);
			if (data.length != 0) {
				val.injection = data[0].injection;
			}
			this.overflowVisible = !this.overflowVisible; // 切换 overflowVisible 的值
		},
		async changeOrder(val) {
			// 先假设isBad为true，等待getCollectOen确定最终值
			let isBad = true;
			try {
				// 等待getCollectOen Promise解决
				const res = await getBypersonIdAndScreenOrder(val.orderVal, val.id);
				console.log(res);
				// console.log(res);
				if (res.length > 0) {
					isBad = res[0].outcome == 0 || res[0].outcome == null ? true : false;
				}
			} catch (error) {
				console.error('Error occurred in getCollectOen:', error);
				// 根据错误处理逻辑，可能需要设置isBad为true或其他值
			}
			console.log(isBad);
			val.next = parsePatientType(
				val.age,
				splitDecimalIntoList(val.moreType),
				val.firstType,
				val.name,
				val.curFinish,
				isBad
			);
			if (val.next && Array.isArray(val.next)) {
				val.next = '请医生确认是否做了这些检查：【' + val.next.join(' | ') + '】';
			}
		},
		close() {
			this.show = false;
			// console.log('close');
		},
		printClose() {
			this.printShow = false;
			// console.log('close');
		},
		closeGather() {
			this.showGather = false;
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
		closeProcess() {
			this.showProcess = false;
		},
		open() {},
		// 跳转到新增患者
		toAddPatient() {
			uni.navigateTo({
				url: '/pages/tb/addPatient/index?flag=2'
			});
		},
		// 修改患者信息
		revise(val) {
			uni.navigateTo({
				url: '/pages/tb/addPatient/index?val=' + JSON.stringify(val) + '&isNew=' + false + '&flag=2'
			});
		},
		// 扫描
		scanner() {
			this.printShow = true;
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
		//注射
		gather(val) {
			//打开弹窗
			this.show = true;
			//保存本次注射的人员信息
			this.person = val;
      this.regentId=null
			this.getRegentList()
		},
		selectRegentList(e){
			// console.log(e);
			consumeApi.getFirstConsume(e).then(res=>{
				// console.log(res);
				this.injectionReagent=res[0]
				if(this.injectionReagent.currentNumber - this.injectionReagent.changeNumber<=0){
					uni.showToast({
						title: '注意：当前试剂的现有库存已为0',
						icon: 'none',
            duration:3000
					})
				// this.injectionReagent={}
				}
				this.radiovalue1=this.injectionReagent.reagentType
			})
		},
		//已注射点击弹窗确认事件
		async injectionUpload() {
			// console.log("信息",this.person);
			if (Object.entries(uni.$person).length === 0) {
				uni.showModal({
					title: '提示',
					content: '登录信息已过期，是否重新登陆？',
					success: (res) => {
						if (res.confirm) {
							uni.reLaunch({
								url: '/pages/login'
							});
						} else if (res.cancel) {
							uni.showToast({
								title: '已取消',
                duration:2000,
								icon: 'none'
							});
						}
					}
				});
				return;
			}
			if(!this.regentId){
				uni.showToast({
					title: '请先选择试剂',
					icon: 'none',
					duration: 3000
				})  
				return
			}
			uni.showLoading({
				title: '保存中',
				mask: true
			});
			openTransaction()
				.then(async (r) => {
					//生成注射时间
					this.refreshMark();
					// console.log(this.injectionReagent);
					let record=await consumeRecordApi.selectLocalRecordByRegentId(this.injectionReagent.id)
					// console.log(record);
					let recordData={
						consumeId:this.injectionReagent.id,
						changeNumber:1,
						type:1,
						createTime: this.screenTime,
						updateTime: this.screenTime,
						creator: uni.$person.id,
						updater: uni.$person.id
					}

					//整理插入数据类
					const inData = {
						year: uni.$person.year,
						screenType: uni.$screenType,
						screenId: this.person.screenId,
						personId: this.person.id,
						injection: 1,
						idNum:this.person.idNum,
						injectionWay: this.radiovalue1,
						screenOrder: '',
						screenTime: this.screenTime,
						statusFlag:1,
						creator: uni.$person.id
					};
					inData.reagentId= this.injectionReagent.id
					inData.reagentSpecsNum= this.injectionReagent.reagentSpecsNum
					//获取本次注射次序
					let order = await getPpdBypersonIdToOrder(this.person.id);
					inData.screenOrder = order[0].screenOrder == null ? 1 : order[0].screenOrder + 1;

					let staticsConsume = await consumeApi.staticsConsumeById(this.injectionReagent.id)  // 实际统计出已消耗的人次
					let consumeNum = 0  // 库存中应消耗的试剂人次
					if (!record || record.length==0){  // 第一次消耗一支试剂
						// console.log(recordData);
						//插入数据请求
						await dbUtils.addTabItem(dbName, tbScreenPpd, inData);
						await dbUtils.addTabItem(dbName,tbScreenConsumeRecord,recordData)
					}else{
						consumeNum=record[0].num * this.injectionReagent.reagentSpecsNum
						console.log(consumeNum);
						console.log(staticsConsume[0].num);
						if (consumeNum>staticsConsume[0].num){
						//插入数据请求
							await dbUtils.addTabItem(dbName, tbScreenPpd, inData);
						}else if(consumeNum==staticsConsume[0].num){
							if(this.injectionReagent.currentNumber - this.injectionReagent.changeNumber==0){
								uni.showToast({
									title: '请重新选择试剂',
									icon: 'none',
									duration: 3000
								}) 
								return
							}
							//插入数据请求
							await dbUtils.addTabItem(dbName, tbScreenPpd, inData);
							await consumeRecordApi.updateRecordNum(Math.ceil((staticsConsume[0].num+1)/this.injectionReagent.reagentSpecsNum),this.injectionReagent.id, uni.$person.id,this.screenTime)
						}else if (consumeNum<staticsConsume[0].num){  //
							await dbUtils.addTabItem(dbName, tbScreenPpd, inData);
							await consumeRecordApi.updateRecordNum(Math.ceil((staticsConsume[0].num+1)/this.injectionReagent.reagentSpecsNum),this.injectionReagent.id, uni.$person.id,this.screenTime)
						}
				
					}
					
					//插入数据进入汇总表
					const gatherData = await getGather(this.person.id, uni.$person.year, uni.$screenType);
					const pddId = await getPddToId(this.person.id);
				
					if (gatherData == null || gatherData.length == 0) {
						//获取初始汇总表插入信息
						const gather = {
							year: uni.$person.year,
							idNum:this.person.idNum,
							screenType: uni.$screenType,
							screenId: this.person.screenId,
							personId: this.person.id,
							ppdId: pddId[0].id,
							ppdNum: inData.screenOrder,
							lastPpdTime: this.screenTime,
							statusFlag:1,
							curFinish: 'ppd组'
						};
						console.log(gather);
						await dbUtils.addTabItem(dbName, tbScreenSum, gather);
					} else {
						//获取初始汇总表插入信息
						const gather = {
							ppdId: pddId[0].id,
							ppdNum: inData.screenOrder,
							lastPpdTime: this.screenTime,
							statusFlag:2,
							curFinish: 'ppd组'
						};
						// console.log(gather);
						//存在的话修改
						await updateOne(gather, this.person.id, uni.$person.year, uni.$screenType);
					}

					//关闭出口
					this.show = false;
					this.search();
					this.statisticalMethod();

					//流程无错误，提交事务
					commitTransaction().then((r) => {
						if (r != 200) {
							throw new Error('提交失败');
						}
						this.$modal.msgSuccess('保存成功');
						uni.hideLoading();
					});
				})
				.catch((e) => {
					// 存在问题，回滚
					rollbackTransaction().then((res) => {
						if (res != 200) {
							uni.hideLoading();
							this.$modal.msgError('更新失败,请重试');
						}
					});
				});
		},
		//提交结果
		async submitOutcome(val) {
			// console.log('val', val);

			if (val.orderTime.length == 0) {
				uni.$u.toast('该人员还未进行过注射，请先注射！',3000);
				return;
			}

			const data = await getBypersonIdAndScreenOrder(val.orderVal, val.id);
			if (data[0].injection == 1) {
				uni.$u.toast('本次筛查已经提交。如需修改,请点击修改!',3000);
				return;
			}

			// 首先找到 orderVal 对应的索引
			const index = val.orderVal - 1; // 因为索引是从 0 开始的，而 orderVal 是从 1 开始的，所以要减 1
			// 然后根据索引获取对应的日期值
      let dateValue=''
      if (val.orderTime && val.orderTime.length>0){
        let obj = val.orderTime.find(obj => obj.value == val.orderVal)
        dateValue = obj.text.split('/')[1]; // 日期值是在文本中的第二部分
      }

			uni.navigateTo({
				url:
					'/pages/tb/ppd/ppdOutcome?id=' +
					val.id +
					'&idNum=' +
					val.idNum +
					'&name=' +
					val.name +
					'&age=' +
					val.age +
					'&isNew=' +
					1 +
					'&screenTime=' +
					dateValue +
					'&order=' +
					val.orderVal +
					'&screenId=' +
					val.screenId
			});
		},
		// 修改结果
		modify(val) {
			if (val.orderTime.length == 0) {
				uni.$u.toast('该人员还未进行过注射，请先注射！',3000);
				return;
			}

			// 首先找到 orderVal 对应的索引
			const index = val.orderVal - 1; // 因为索引是从 0 开始的，而 orderVal 是从 1 开始的，所以要减 1
			// 然后根据索引获取对应的日期值
      let dateValue=''
      if (val.orderTime && val.orderTime.length>0){
        let obj = val.orderTime.find(obj => obj.value == val.orderVal)
        dateValue = obj.text.split('/')[1]; // 日期值是在文本中的第二部分
      }
			uni.navigateTo({
				url:
					'/pages/tb/ppd/ppdOutcome?id=' +
					val.id +
					'&idNum=' +
					val.idNum +
					'&name=' +
					val.name +
					'&age=' +
					val.age +
					'&isNew=' +
					0 +
					'&screenTime=' +
					dateValue +
					'&order=' +
					val.orderVal +
					'&screenId=' +
					val.screenId
			});
		},
		clearName() {
			this.searchName = '';
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
		clearScreenNum() {
			this.screenNum = '';
		},
		clickTextHandler(text) {
			this.showNextText = true;
			// 将text字符串对;做分割形成数组
			this.nextText = text.split(';');
		},
		checkLength(text) {
			return (text + '').length < 35;
		},
		// 分页触发
		change(e) {
			this.$refs.table.clearSelection();
			this.pageCurrent = e.current;
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
					'lastPpdTime'
				),
				getPpdList()
			];

			// console.log('promises', promises);

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

						// console.log(patient);
						//获取是否为已提交状态
						let collect = collectRes.filter(
							(screen) => screen.personId == patient.id && screen.screenOrder == orderVal
						);
						// console.log("collect",collect);
						let injection = 0;
						// console.log();
						if (collect.length != 0) {
							injection = collect[0].injection;
						}

						// 先假设isBad为true，等待getCollectOen确定最终值
						let isBad = true;
						try {
							// 等待getCollectOen Promise解决
							const res = await getBypersonIdAndScreenOrder(orderVal, patient.id);
							// console.log(res);
							if (res.length > 0) {
								isBad = res[0].outcome == 0 || res[0].outcome == null ? true : false;
							}
						} catch (error) {
							console.error('Error occurred in getCollectOen:', error);
							// 根据错误处理逻辑，可能需要设置isBad为true或其他值
						}
						// console.log(isBad);
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
								patient.next += parseNext(patient.curFinish, personType.get(map[i]), isBad) + ';';
							}
						}
						return {
							...patient,
							injection: injection,
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
				item.csrq = item.classroom + '/' + item.createTime;
			});
		},
		// 时间
		handleSelectDate(value) {
			this.dateRange = value;
		},
		// 人群分类
		handleUpdateValues(val) {
			this.firstType = val.firstVal;
			this.secondType = val.secondVal;
		},
		// 搜索
		handleSearch() {
			this.pageCurrent = 1;
			this.search();
		},
		async search() {
			// getPpdList().then((res) => {
			// 	console.log(res);
			// });

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
					'lastPpdTime'
				),
				getPpdList()
			];

			Promise.all(promises)
				.then(async (results) => {
					// results 是一个包含两个 Promise 结果的数组
					let patientPageRes = results[0];
					console.log(patientPageRes);
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
						//获取是否为已提交状态
						let collect = collectRes.filter(
							(screen) => screen.personId == patient.id && screen.screenOrder == orderVal
						);
						// console.log("collect",collect);
						let injection = 0;
						// console.log();
						if (collect.length != 0) {
							injection = collect[0].injection;
						}

						// 先假设isBad为true，等待getCollectOen确定最终值
						let isBad = true;
						try {
							// 等待getCollectOen Promise解决
							const res = await getBypersonIdAndScreenOrder(orderVal, patient.id);

							// console.log(res);
							if (res.length > 0) {
								isBad = res[0].outcome == 0 || res[0].outcome == null ? true : false;
							}
						} catch (error) {
							console.error('Error occurred in getCollectOen:', error);
							// 根据错误处理逻辑，可能需要设置isBad为true或其他值
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
                let str = parseNext(patient.curFinish, personType.get(map[i]), isBad) + ';';
                // console.log(`${patient.name}-${map}-${patient.curFinish}-${isBad}`)
								patient.next += str;
							}
						}
						return {
							...patient,
							injection: injection,
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
			statisticsNum(time).then((res) => {
				// console.log(res);
				if (res.length != 0) {
					this.statisticsData = res[0];
				} else {
					this.statisticsData = {
						yearNum: 0,
						yearYangNum: 0,
						yearYinNum: 0,
						monthlyNum: 0,
						monthlyYangNum: 0,
						monthlyYinNum: 0,
						dayNum: 0,
						dayYangNum: 0,
						dayYinNum: 0,
						ppdNum: 0,
						ecNum: 0,
						igraNum: 0,
						xLengthGtTenNum: 0,
						yLengthLtTenNum: 0,
						lymphangitisNum: 0,
						doubleCircleNum: 0,
						blisterNum: 0,
						necrosisNum: 0
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
			this.showSelectCrowd = !this.showSelectCrowd;
			this.pageCurrent = 1;
			this.search();
		},
		//获取当前日期
		refreshMark() {
			const currentDate = new Date();
			const year = currentDate.getFullYear();
			const month = String(currentDate.getMonth() + 1).padStart(2, '0');
			const day = String(currentDate.getDate()).padStart(2, '0');
			const hours = String(currentDate.getHours()).padStart(2, '0');
			const minutes = String(currentDate.getMinutes()).padStart(2, '0');
			const seconds = String(currentDate.getSeconds()).padStart(2, '0');
			this.screenTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
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
