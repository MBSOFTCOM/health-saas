<template>
	<view style="margin: 10px; font-size: 18px">
		<navigator url="/pages/tb/SQLiteManager/index" hover-class="navigator-hover">
			<button type="default" v-if="false">SQLiteManager</button>
		</navigator>
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
						@click="scranOcr"
						style="margin-left: 10px"
						class="custom-sm"
						icon="scan"
						iconColor="#fff"
						color="#fff"
						:plain="true"
						text="扫描患者身份证"
					></up-button>
				</view>
			</view>
		</view>
		<view class="container">
			<view class="main">
				<view class="main-1" style="padding-top: 20px">
					<view class="top-1">
						<span style="margin-left: 20px; display: inline-block">筛查编号</span>
						<input
							class="uni-input"
							confirm-type="search"
							placeholder="请填写筛查编号"
							v-model="FormData.screenId"
							@blur="screenIdCheck()"
							disabled
						/>
					</view>
					<view class="top-1">
						<span style="margin-left: 110px; display: inline-block">姓名</span>
            <span class="red-badge">*</span>
						<input
							class="uni-input"
							confirm-type="search"
							placeholder="请填写姓名"
							v-model="FormData.name"
							@blur="nameCheck()"
						/>
					</view>
					<view class="top-1">
						<span style="margin-left: 93px; display: inline-block">身高(cm)</span>
						<input
							class="uni-input"
							confirm-type="search"
							placeholder="请填写身高"
							v-model="FormData.height"
							maxlength="6"
							@blur="heightCheck()"
						/>
					</view>
				</view>
				<view class="main-1">
					<view class="top-1">
						<span style="margin-left: 20px; display: inline-block">体重(kg)</span>
						<input
							style="margin-left: 19px"
							class="uni-input"
							confirm-type="search"
							placeholder="请填写体重"
							v-model="FormData.weight"
							type="number"
							maxlength="6"
							@blur="weightCheck()"
						/>
					</view>
					<view class="top-1">
						<span style="margin-left: 110px; display: inline-block">电话</span>
            <span class="red-badge">*</span>
						<input
							style="margin-left: 15px"
							class="uni-input"
							confirm-type="search"
							placeholder="请填写电话"
							v-model="FormData.tel"
							maxlength="11"
							@blur="telCheck('tel')"
						/>
					</view>
					<view class="top-1">
						<span style="margin-left: 65px; display: inline-block">身份证号码</span>
            <span class="red-badge">*</span>
						<input
							type="idNum"
							class="uni-input"
							confirm-type="search"
							placeholder="请填写身份证号码"
							v-model="FormData.idNum"
							@blur="idNumCheck"
							maxlength="18"
						/>
					</view>
				</view>
				<view class="main-1">
					<view class="top-1">
						<span style="margin-left: 55px; display: inline-block">民族</span>
						<w-select
							style="margin-left: 15px"
							v-model="FormData.nation"
							:list="ethnic"
							valueName="text"
							keyName="value"
							height="35px"
							filterable
							showClose
							width="210px"
						></w-select>
					</view>
					<view class="top-1">
						<span style="margin-left: 37px; display: inline-block">计划筛查时间</span>
            <span class="red-badge">*</span>
						<view style="width: 213px; margin-left: 15px">
							<uni-datetime-picker type="daterange" :clear-icon="false" v-model="FormData.screenTime" @change="maskClick" />
						</view>
					</view>
					<!-- <view class="top-1">
						<span style="margin-left: 80px; display: inline-block">是否新增</span>
						<uni-data-select
							style="width: 210px; margin-left: 15px"
							v-model="FormData.isNew"
							:localdata="dataRadio"
						></uni-data-select>
					</view> -->
				</view>
			</view>
			<view class="bom-1">
				<view class="bom-1-1">
					<view class="bom-1-text">户籍地址</view>
					<view class="bom-add">
						<view class="select">
							<span class="se-sp" style="margin-left: 50px">省</span>
						<uni-data-select
							v-model="FormData.permanentAddressProvince"
							:localdata="permanentAddressProvince"
							class="districtSelect"
							placeholder="请选择户籍地址"
							@change="selectpermanentAddressProvince"/>
						</view>
						<view class="select">
							<span class="se-sp">市</span>
						<uni-data-select
							v-model="FormData.permanentAddressCity"
							:localdata="permanentAddressCity"
							class="districtSelect"
							placeholder="请选择户籍地址"
							@change="selectpermanentAddressCity"/>
						</view>
						<view class="select">
							<span class="se-sp">县</span>
						<uni-data-select
							v-model="FormData.permanentAddressCounty"
							:localdata="permanentAddressCounty"
							class="districtSelect"
							placeholder="请选择户籍地址"
							@change="selectpermanentAddressCounty"/>
						</view>
						<view class="select">
							<span class="se-sp">乡镇</span>
						<uni-data-select
							v-model="FormData.permanentAddressTown"
							class="districtSelect"
							placeholder="请选择户籍地址"
							:localdata="permanentAddressTown"/>
						</view>
					</view>
				</view>
				<view class="bom-1-2" style="margin-left: 4px;">
					<span>详细地址</span>
					<input
						class="address-input"
						confirm-type="search"
						placeholder="请填写户籍地址"
						v-model="FormData.permanentAddress"
					/>
				</view>
				<view class="bom-1-1">
					<view class="bom-1-text">现住地址</view>
					<view class="bom-add">
						<view class="select">
							<span class="se-sp" style="margin-left: 50px">省</span>
              <span class="red-badge">*</span>
						<uni-data-select
							v-model="FormData.province"
							class="districtSelect"
							placeholder="请选择现住地址"
							:localdata="province"
							@change="selectProvince"/>
						</view>
						<view class="select">
							<span class="se-sp">市</span>
              <span class="red-badge">*</span>
						<uni-data-select
							v-model="FormData.city"
							class="districtSelect"
							placeholder="请选择现住地址"
							:localdata="city"
							@change="selectCity"/>
						</view>
						<view class="select">
							<span class="se-sp">县</span>
              <span class="red-badge">*</span>
						<uni-data-select
							v-model="FormData.county"
              class="districtSelect"
              placeholder="请选择现住地址"
							:localdata="county"
							@change="selectCounty"
						></uni-data-select>
						</view>
						<view class="select">
							<span class="se-sp">乡镇</span>
              <span class="red-badge">*</span>
						<uni-data-select
							v-model="FormData.town"
              class="districtSelect"
              placeholder="请选择现住地址"
							:localdata="town"
						></uni-data-select>
						</view>
					</view>
				</view>
				<view class="bom-1-2" style="margin-left: 4px;">
					<span>详细地址</span>
          <span class="red-badge">*</span>
					<input
						class="address-input"
						confirm-type="search"
						placeholder="请填写现住地址"
						v-model="FormData.address"
					/>
				</view>
				<view class="bom-1-3">
					<span>备注</span>
					<view class="u-text">
						<u--textarea height="50" placeholder="请输入备注" v-model="FormData.remark"></u--textarea>
					</view>
				</view>
				<view class="bom-1-4">
					<view class="bom-p1">
						<span style="display: inline-block; margin-left: 10px">人群分类</span>
            <span class="red-badge">*</span>
						<uni-data-checkbox
							@change="changeCrowdVal"
							v-model="crowdVal"
							:localdata="items1"
							style="margin-left: 15px"
						></uni-data-checkbox>
					</view>
					<view class="bom-p1" v-if="this.crowdVal == 1">
						<span style="display: inline-block; margin-left: -5px">多人群分类</span>
						<uni-data-checkbox
							style="margin-left: 13px"
							multiple
							v-model="crowdArr"
							:localdata="items2"
							@change="moreTypeChange"
						></uni-data-checkbox>
					</view>
					<view class="bom-p1" v-if="this.crowdVal == 4" style="margin-left: 4px;">
						<span style="display: inline-block; margin-left: -5px">多人群分类</span>
						<uni-data-checkbox
							style="margin-left: 13px"
							multiple
							v-model="crowdArr"
							:localdata="items3"
						></uni-data-checkbox>
					</view>
					<view class="bom-bm">
						<view class="bom-se">
							<span style="width: 180rpx;">单位(学校)</span>
							<input
								class="address-input"
								confirm-type="search"
								placeholder="请填写单位"
								v-model="FormData.schoolOrTemple"
							/>
						</view>
						<view class="bom-se" v-if="showStudent == true">
							<span>班级</span>
							<input
								confirm-type="search"
								placeholder="请填写班级"
								class="address-input"
								v-model="FormData.classroom"
							/>
						</view>
						<view class="bom-se" v-if="showStudent == true">
							<span style="font-size: 18px">学生类型</span>
							<uni-data-select
								style="width: 250rpx; margin-left: 15px"
								v-model="FormData.studentType"
								placement="top"
								:localdata="studentType"/>
						</view>
					</view>
					<view class="bom-bm" v-if="showStudent">
						<view class="bom-se">
							<span style="display: inline-block;font-size: 18px">监护人电话</span>
							<input
								style="margin-left: 15px;border: 1px solid rgba(204, 204, 204, 1);height: 35px;"
								class="uni-input"
								confirm-type="search"
								placeholder="请填写电话"
								v-model="FormData.guardianTel"
								maxlength="11"
								@blur="telCheck('guardianTel')"
							/>
						</view>
						<view class="bom-se">
							<span style="font-size: 18px">是否新生</span>
							<uni-data-select
								style="width: 250rpx; margin-left: 15px"
								v-model="FormData.isNewStudent"
								:localdata="dataRadio"
							></uni-data-select>
						</view>
					</view>
				</view>
			</view>
		</view>

		<view style="display: flex; justify-content: center">
			<up-button class="cur-add" text="保存" v-on:click="newAdd"></up-button>
		</view>
		<u-toast ref="uToastRef"></u-toast>
	</view>
</template>

<script>

import {getNextId} from "../../../utils/common";

const ocrModule = uni.requireNativePlugin('YY-TomatoOCR');
const modal = uni.requireNativePlugin('modal');
import dbUtils from '@/uni_modules/zjy-sqlite-manage/components/zjy-sqlite-manage/dbUtils';
import { count, dbName, tbScreenPerson, updatePerson, repeatCheck,openTransaction } from '@/utils/sqlite';
import { selectDistrict, selectDistrictName,selectDistrictForSelect } from '@/utils/districtInitSql.js';
import { ethnic } from '@/utils/dict.js';
import { studentType } from '@/utils/dictData.js';
import * as CommonApi from '@/utils/common.js'

export default {
	data() {
		return {
			studentType,
			nav: [],
			// 点击学生
			showStudent: false,
			// 点击僧尼
			showMonks: false,
			// 是否新增绑定
			newlyAdded: 1,
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
			//存储当前操作时间
			single: '',
			//人群类型
			crowdVal: '1',
			crowdArr: ['1'],
			//第一人群分类
			items1: [
				{
					text: '重点人群',
					value: '1'
				},
				{
					text: '非重点人群',
					value: '2'
				},
				{
					text: '教职工',
					value: '4'
				}
			],
			//重点人群分类
			items2: [
				{
					text: '学生',
					value: '1'
				},
				{
					text: '老年人',
					value: '2'
				},
				{
					text: '教职工',
					value: '4'
				},
				{
					text: '密接者',
					value: '8'
				},
				{
					text: '糖尿病',
					value: '16'
				},
				{
					text: '僧尼',
					value: '32'
				},
				{
					text: '既往患者',
					value: '64'
				},
				{
					text: 'HIV/AIDS',
					value: '128'
				}
			],
			//教职工
			items3: [
				{
					text: '密接者',
					value: '8'
				},
				{
					text: '糖尿病',
					value: '16'
				},
				{
					text: '既往患者',
					value: '64'
				},
				{
					text: 'HIV/AIDS',
					value: '128'
				}
			],
			//民族
			ethnic: [],
			fristVal: null,
			moreVal: null,
			//户籍地址
			permanentAddressProvince: [],
			permanentAddressCity: [],
			permanentAddressCounty: [],
			permanentAddressTown: [],
			//现住址
			province: [],
			city: [],
			county: [],
			town: [],
			// 绑定数据
			FormData: {
				id: undefined,
				screenId: '0',
				name: undefined,
				height: undefined,
				weight: undefined,
				idNum: undefined,
				tel: undefined,
				nation: 1,
				permanentAddress: undefined,
				permanentAddressProvince: '',
				permanentAddressCity: undefined,
				permanentAddressCounty: undefined,
				permanentAddressTown: undefined,
				address: undefined,
				province: undefined,
				city: undefined,
				county: undefined,
				town: undefined,
				screenPoint: undefined,
				isNew: 1,
				isNewStudent: 1,
				screenTime: undefined,
				createTime: null,
				remark: undefined
			},
			//提示
			show: false,
			//是否是新增患者
			isNew: true,
			year: null
		};
	},
	onLoad(e) {
		this.getNavItems(uni.$screenType, e.flag, e.isNew);
		this.ethnic = ethnic;
		//修改传输进来的数据
		this.uData(e);
		//初始化省级列表
		this.provinceList();
	},
	watch: {
		// 监听 crowdArr 的变化
		crowdArr: {
			handler(newVal) {
				// 判断 crowdArr 中是否包含 1
				if (newVal.includes('1')) {
					this.showStudent = true;
				} else {
					this.FormData.isNewStudent = 0;
					this.showStudent = false;
					this.FormData.classroom = '';
				}
				// 判断 crowdArr 中是否包含 32
				this.showMonks = newVal.includes('32');

				/* if (!(newVal.includes('1') && newVal.includes('32'))) {
					this.FormData.schoolOrTemple = '';
				} */
			},
			immediate: true // 立即执行一次
		}
	},
	methods: {
		getNavItems(screenType, flag, label) {
			let drResult = '';
			let categories = '';
			switch (flag) {
				case '1':
					categories = '采集组';
					break;
				case '2':
					categories = 'PPD组';
					break;
				case '3':
					categories = '痰检组';
					break;
				case '4':
					categories = 'DR组';
					break;
				case '5':
					categories = 'CT组';
					break;
				default:
					drResult = '采集组';
			}

			if (typeof label === 'undefined') {
				drResult = '新增患者';
			} else {
				drResult = '修改患者信息';
			}

			const screenNames = {
				1: '常规筛查',
				2: '新生入学筛查',
				3: '应急筛查'
			};
			this.nav = [{ value: screenNames[screenType] }, { value: categories }, { value: drResult, isActive: true }];
			return this.nav;
		},
		changeCrowdVal() {
			this.crowdArr = [];
			if (this.crowdVal == 2) {
				this.showStudent = false;
				this.showMonks = false;
			}
		},
		moreTypeChange(){
			console.log(this.crowdArr);
			if(!this.crowdArr.includes("1")){
				this.FormData.isNewStudent=null
				this.FormData.studentType=0
				this.FormData.guardianTel=null
			}
		},
		conversionDate() {
			const currentDate = new Date();
			const year = currentDate.getFullYear();
			const month = String(currentDate.getMonth() + 1).padStart(2, '0');
			const day = String(currentDate.getDate()).padStart(2, '0');
			const hours = String(currentDate.getHours()).padStart(2, '0');
			const minutes = String(currentDate.getMinutes()).padStart(2, '0');
			const seconds = String(currentDate.getSeconds()).padStart(2, '0');
			this.single = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
		},
		scranOcr() {
			// 识别患者身份证件
			ocrModule.ocrAsyncFunc(
				{
					type: 'idcard',
					sourceType: ['camera', 'album'],
					showCorp: true
				},
				(ret) => {
					if (ret) {
						this.FormData.name = ret.result.name;
						this.FormData.idNum = ret.result.number;
						this.FormData.permanentAddress = ret.result.address;
						// this.FormData.nation = ret.result.nation;
					}
				}
			);
		},
		back() {
			uni.navigateBack({
				delta: 1
			});
		},
		//新增患者提交
		async newAdd() {
      // console.log(this.FormData)
			//数据校验
			// this.newCheck()

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
								icon: 'none'
							});
						}
					}
				});
				return;
			}
			if(!this.FormData.idNum){
				uni.$u.toast('请填入正确的身份证号',3000);
			}
			//通过身份证获取性别年龄
			const genderAndAge = this.getGenderAndAge(this.FormData.idNum);

			// console.log(this.FormData);
			openTransaction()
				.then(async (r) => {
					if (this.isNew == true) {
						console.log(this.FormData);
						//新增患者数据
						const FormData1 = {
							screenType: uni.$screenType,
							name: this.FormData.name,
							idNum: this.FormData.idNum,
							screenId: this.FormData.screenId,
							tel: this.FormData.tel,
							age: genderAndAge.age,
							sex: genderAndAge.sex,
							height: this.FormData.height??'',
							weight: this.FormData.weight??'',
							permanentAddress: this.FormData.permanentAddress??'',
							permanentAddressProvince: this.FormData.permanentAddressProvince??'',
							permanentAddressCity: this.FormData.permanentAddressCity??'',
							permanentAddressCounty: this.FormData.permanentAddressCounty??'',
							permanentAddressTown: this.FormData.permanentAddressTown??'',
							address: this.FormData.address,
							nation: this.FormData.nation,
							province: this.FormData.province,
							city: this.FormData.city,
							county: this.FormData.county,
							town: this.FormData.town,
							firstType: this.crowdVal,
							studentType:this.FormData.studentType??0,
							moreType: 0,
							schoolOrTemple: this.FormData.schoolOrTemple,
							isNew: this.FormData.isNew,
							isScreened: 0,
							isNewStudent: this.FormData.isNewStudent,
							screenPoint: uni.$person.screenPoint,
							screenTime: this.FormData.screenTime,
							screenStartTime: this.FormData.screenStartTime,
							screenEndTime: this.FormData.screenEndTime,
							guardianTel: this.FormData.guardianTel??'',
							creator: uni.$person.id,
							remark: this.FormData.remark??'',
							statusFlag: 1
						};
						// console.log(FormData1);
						//生成时间
						this.conversionDate();
						FormData1.year = uni.$person.year;
						FormData1.createTime = this.single;

						// FormData1.firstType = this.crowdVal;

						// console.log('FormData1', FormData1);
						//如果是教职工
						if (FormData1.firstType == 4) {
							this.crowdArr.push('4');
							// console.log(FormData1.moreType);
						}

						//转换多人群
						this.crowdArr.forEach((i) => {
							FormData1.moreType = parseInt(FormData1.moreType) + parseInt(i);
						});

						// console.log('FormData1——1', FormData1);
						//对数据校验
						const isNull = this.dataCheck(FormData1, 'dataCheck');
						if (!isNull) {
							uni.$u.toast('请检查表单，存在数据为空',3000);
							return;
						}

						//补充班级
						FormData1.classroom = this.FormData.classroom;

						//单独对多人群分类校验
						if (FormData1.firstType == 1 && FormData1.moreType == 0) {
							uni.$u.toast('选择了重点人群，多人群分类必须选择',3000);
							return;
						}

						//学生多一个班级校验
						if (this.crowdArr.includes('1')) {
							if (
								FormData1.classroom === null ||
								FormData1.classroom === undefined ||
								FormData1.classroom === ''
							) {
								uni.$u.toast('该人员为学生，班级不能为空',3000);
								return;
							}
						}

						//数据重复校验
						let Num = await repeatCheck(FormData1.idNum, FormData1.screenId, null);

						if (Num[0].count > 0) {
							uni.$u.toast('该人员信息已经存在',3000);
							return;
						}

            let nextId =await CommonApi.getNextId(tbScreenPerson)
            FormData1.id=nextId
						//插入数据
						dbUtils.addTabItem(dbName, tbScreenPerson, FormData1);
						//返回上一页
						uni.navigateBack({
							delta: 1 // 返回的页面数，如果delta是1，表示返回上一个页面
						});
					} else {
						//定义修改后的数据
						const FormData2 = {
							name: this.FormData.name,
							idNum: this.FormData.idNum,
							screenId: this.FormData.screenId,
							tel: this.FormData.tel,
							age: genderAndAge.age,
							sex: genderAndAge.sex,
							height: this.FormData.height??'',
							weight: this.FormData.weight??'',
							permanentAddress: this.FormData.permanentAddress??'',
							permanentAddressProvince: this.FormData.permanentAddressProvince??'',
							permanentAddressCity: this.FormData.permanentAddressCity??'',
							permanentAddressCounty: this.FormData.permanentAddressCounty??'',
							permanentAddressTown: this.FormData.permanentAddressTown??'',
							address: this.FormData.address,
							nation: this.FormData.nation,
							province: this.FormData.province,
							city: this.FormData.city,
							county: this.FormData.county,
							town: this.FormData.town,
							firstType: this.crowdVal,
							moreType: 0,
							studentType:this.FormData.studentType??0,
							schoolOrTemple: this.FormData.schoolOrTemple,
							isNew: this.FormData.isNew,
							isNewStudent: this.FormData.isNewStudent,
							// screenPoint: this.FormData.screenPoint,
							guardianTel: this.FormData.guardianTel??'',
							screenStartTime: this.FormData.screenStartTime,
							screenEndTime: this.FormData.screenEndTime,
							updater: uni.$person.id,
							remark: this.FormData.remark??'',
							statusFlag:this.FormData.statusFlag??2
						};

						this.conversionDate();
						FormData2.updateTime = this.single;
						// console.log('2', FormData2);

						//如果是教职工
						if (FormData2.firstType == 4) {
							this.crowdArr.push('4');
							// console.log(FormData1.moreType);
						}
						console.log(this.crowdArr);
						//转换多人群
						this.crowdArr.forEach((i) => {
							FormData2.moreType = parseInt(FormData2.moreType) + parseInt(i);
						});
						// console.log('2', FormData2);
						//对数据校验
						const isNull = this.dataCheck(FormData2, 'dataCheck');
						if (!isNull) {
							uni.$u.toast('请检查表单，存在数据为空',3000);
							return;
						}

						//补充班级
						FormData2.classroom = this.FormData.classroom;

						//单独对多人群分类校验
						if (FormData2.firstType == 1 && FormData2.moreType == 0) {
							uni.$u.toast('选择了重点人群，多人群分类必须选择',3000);
							return;
						}

						//学生多一个班级校验
						if (this.crowdArr.includes('1')) {
							if (
								FormData2.classroom === null ||
								FormData2.classroom === undefined ||
								FormData2.classroom === ''
							) {
								uni.$u.toast('该人员为学生，班级不能为空',3000);
								return;
							}
						}

						//数据重复校验
						let Num = await repeatCheck(FormData2.idNum, FormData2.screenId, this.FormData.id);

						// console.log(Num);
						if (Num[0].count > 0) {
							uni.$u.toast('该人员信息已经存在',3000);
							return;
						}
						console.log(FormData2);
						updatePerson(FormData2, this.FormData.id);
						//返回上一页
						uni.navigateBack({
							delta: 1 // 返回的页面数，如果delta是1，表示返回上一个页面
						});
					}

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
		//新增列表校验
		newCheck() {
			if (this.FormData.name == null) {
			}
		},
		//筛查编号校验
		screenIdCheck() {
			const regex = /^[0-9]+$/;
			if (!regex.test(this.FormData.screenId)) {
				this.FormData.screenId = '';
				uni.$u.toast('请输入正整数类型数据',3000);
				return;
			}
		},
		//名字校验
		nameCheck() {
			// 定义一个正则表达式，匹配只包含中文字符
			const regex = /^[\u4e00-\u9fa5]+$/;
			if (!regex.test(this.FormData.name)) {
				this.FormData.name = '';
				uni.$u.toast('请输入中文',3000);
				return;
			}
		},
		//身高校验
		heightCheck() {
			const regex = /^(?:\d{1,3}(?:\.\d{1,2})?)$/;
			if (!regex.test(this.FormData.height)) {
				this.FormData.height = '';
				uni.$u.toast('请输入有效合理数值！',3000);
				return;
			}
		},
		//体重校验
		weightCheck() {
			const regex = /^(?:\d{1,3}(?:\.\d{1,2})?)$/;
			if (!regex.test(this.FormData.weight)) {
				this.FormData.weight = '';
				uni.$u.toast('请输入有效合理数值！',3000);
				return;
			}
		},
		//手机号验证
		telCheck(typeFlag) {
			const regex = /^1[3456789]\d{9}$/;
			if (!regex.test(this.FormData[typeFlag])) {
        this.FormData[typeFlag] = '';
				uni.$u.toast('请输入有效的手机号',3000);
				return;
			}
		},
		//身份证验证
		idNumCheck() {
			const regex = /^([1-9]\d{5})(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}(\d|X|x)$/;
			if (!regex.test(this.FormData.idNum)) {
				this.FormData.idNum = '';
				uni.$u.toast('请输入有效的身份证号',3000);
				return;
			}
		},
		//提交时数据非空校验
		dataCheck(obj, classroom) {
			let checkData={
				name:obj.name??null,
				age:obj.age??null,
				tel:obj.tel??null,
				province:obj.province??null,
				city:obj.city??null,
				county:obj.county??null,
				town:obj.town??null,
				address:obj.address??null,
				firstType:obj.firstType??null,
				moreType:obj.moreType??null,
				screenStartTime: obj.screenStartTime,
				screenEndTime: obj.screenEndTime
			}
			if(this.showStudent){
				checkData.schoolOrTemple=obj.schoolOrTemple??null
				checkData.classroom=obj.classroom??null
				checkData.guardianTel=obj.guardianTel??null
				checkData.studentType=obj.studentType??0
			}
			// console.log(checkData);
			for (let key in checkData) {
				// console.log(key);
				if (obj.hasOwnProperty(key)) {
					if (obj[key] === null || obj[key] === undefined || obj[key] === '') {
						console.log(key, '有问题');
						return false;
					}
				}
			}
			return true;
		},
		//通过身份证获取性别年龄
		getGenderAndAge(idNum) {
			//判断是否为整型，是整型就把其转换成字符串
			if (Number.isInteger(idNum)) {
				idNum = idNum.toString();
			}
			// 获取出生日期和性别信息
			var birthYear = idNum.substring(6, 10);
			var birthMonth = idNum.substring(10, 12);
			var birthDay = idNum.substring(12, 14);
			var genderCode = idNum.substring(16, 17);

			// 计算性别
			var gender = genderCode % 2 === 0 ? '1' : '0';

			// 计算年龄
			var today = new Date();
			var age = today.getFullYear() - birthYear - 1;
			if (
				today.getMonth() + 1 > birthMonth ||
				(today.getMonth() + 1 === birthMonth && today.getDate() >= birthDay)
			) {
				age++;
			}

			return {
				sex: gender,
				age: age
			};
		},
		//人群选择回显方法
		displaySelected(savedValue) {
			let selectedOptions = [];
			// console.log(savedValue);
			if (this.crowdVal == '1') {
				this.items2.forEach((item) => {
					if (savedValue & parseInt(item.value, 10)) {
						selectedOptions.push(item.value);
					}
				});
			}
			if (this.crowdVal == '4') {
				// console.log(4);
				// savedValue=savedValue.split('4').join('')
				this.items3.forEach((item) => {
					if (savedValue & parseInt(item.value, 10)) {
						selectedOptions.push(item.value);
					}
				});
			}
			this.crowdArr=[]
			selectedOptions.forEach((i) => {
				this.crowdArr.push(i.toString());
			});
		},
		//修改传输进来的数据
		async uData(e) {
			this.FormData = JSON.parse(e.val);
			if(!this.FormData.nation || !this.FormData.hasOwnProperty('nation')){
				this.FormData.nation=''
			}
			// console.log('p', this.FormData);
			selectDistrictForSelect(this.FormData.province).then((res) => {
				this.city = res;
			});
			selectDistrictForSelect(this.FormData.city).then((res) => {
				this.county = res;
			});
			selectDistrictForSelect(this.FormData.county).then((res) => {
				this.town = res;
			});
			selectDistrictForSelect(this.FormData.permanentAddressProvince).then((res) => {
				this.permanentAddressCity = res;
			});
			selectDistrictForSelect(this.FormData.permanentAddressCity).then((res) => {
				this.permanentAddressCounty = res;
			});
			selectDistrictForSelect(this.FormData.permanentAddressCounty).then((res) => {
				this.permanentAddressTown = res;
			});
			this.FormData.screenTime=[]
			this.FormData.screenTime.push(this.FormData.screenStartTime)
			this.FormData.screenTime.push(this.FormData.screenEndTime)
			this.isNew = e.isNew;
			this.crowdVal = this.FormData.firstType.toString();
			if (e.isNew == true) {
				uni.setNavigationBarTitle({
					title: '新增患者'
				});
			} else {
				uni.setNavigationBarTitle({
					title: '修改患者信息'
				});
			}

			// console.log(this.crowdVal);
			//转换多人群
			this.displaySelected(this.FormData.moreType);
			// console.log('crowdArr', this.crowdArr);

			// 判断 crowdArr 中是否包含 1
			if (this.crowdArr.includes('1')) {
				this.showStudent = true;
			} else {
				this.FormData.isNewStudent = 0;
				this.showStudent = false;
				this.FormData.classroom = '';
			}
			// 判断 crowdArr 中是否包含 32
			this.showMonks = this.crowdArr.includes('32');
			selectProvince({code:this.FormData.permanentAddressProvince})
			/* if (!(this.crowdArr.includes('1') && this.crowdArr.includes('32'))) {
				this.FormData.schoolOrTemple = '';
			} */

		},

		//现住址
		//选择省级区划
		selectProvince(val) {
			// console.log(val);
			this.FormData.city = '';
			this.FormData.county = '';
			this.FormData.town = '';
			this.city = [];
			this.county = [];
			this.town = [];
			selectDistrictForSelect(val).then((res) => {
				console.log(this.city);
				this.city = res;
			});
		},
		maskClick(){
			this.FormData.screenStartTime=this.FormData.screenTime[0]
			this.FormData.screenEndTime=this.FormData.screenTime[1]
			this.FormData.screenTime=null
			console.log(this.FormData);
		},
		//选择市级区划
		selectCity(val) {
			this.FormData.county = '';
			this.FormData.town = '';
			this.county = [];
			this.town = [];
			selectDistrictForSelect(val).then((res) => {
				this.county = res;
			});
		},
		//选择县级区划
		selectCounty(val) {
			this.FormData.town = '';
			this.town = [];
			selectDistrictForSelect(val).then((res) => {
				this.town = res;
			});
		},
		//户籍
		//选择省级区划
		selectpermanentAddressProvince(val) {
			this.FormData.permanentAddressCity = '';
			this.FormData.permanentAddressCounty = '';
			this.FormData.permanentAddressTown = '';
			this.permanentAddressCity = [];
			this.permanentAddressCounty = [];
			this.permanentAddressTown = [];
			selectDistrictForSelect(val).then((res) => {
				this.permanentAddressCity = res;
			});
		},
		//选择市级区划
		selectpermanentAddressCity(val) {
			this.FormData.permanentAddressCounty = '';
			this.FormData.permanentAddressTown = '';
			this.permanentAddressCounty = [];
			this.permanentAddressTown = [];
			selectDistrictForSelect(val).then((res) => {
				this.permanentAddressCounty = res;
			});
		},
		//选择县级区划
		selectpermanentAddressCounty(val) {
			this.FormData.permanentAddressTown = '';
			this.permanentAddressTown = [];
			selectDistrictForSelect(val).then((res) => {
				this.permanentAddressTown = res;
			});
		},
		//初始化省级区划列表
		provinceList() {
			selectDistrictForSelect(0).then((res) => {
				// console.log(res);
				this.province = res;
				this.permanentAddressProvince = res;
			});
		}
	}
};
</script>

<style lang="scss" scoped>
.red-badge {
  color: red;          /* 白色字 */
  border-radius: 50%;    /* 圆形 */
  margin-left: 5px;      /* 左边距 */
  font-weight: bold;      /* 加粗 */
  font-size: 14px;        /* 字体大小 */
  display: inline-block;   /* 行内块元素 */
  vertical-align: middle;  /* 垂直对齐 */
}

.top1 {
	display: flex;
	align-items: center;
	justify-content: space-between;
	.top1-left {
		margin: 0 10px;
	}
	.top1-right {
		display: flex;
		align-items: center;
		justify-content: flex-end;
		margin: 0 10px;
		.search-btn {
			display: flex;
			align-items: center;
			justify-content: center;
			.custom-sm {
				background: rgba(84, 141, 255, 1);
			}
		}
	}
}
.container {
	background-color: #fff;
	.main {
		.main-1 {
			display: flex;
			align-items: center;
			margin-top: 10px;
			.top-1 {
				display: flex;
				align-items: center;
				.uni-input {
					width: 210px;
					height: 35px;
					border: 1px solid rgba(204, 204, 204, 1);
					margin-left: 15px;
				}
			}
		}
	}
	.bom-1 {
		margin: 10px 20px;
		.bom-1-1 {
			.bom-1-text{
				font-weight: 800;
			}
			.bom-add {
				margin-bottom: 10px;
				display: flex;
				align-items: center;
				.select {
					display: flex;
					align-items: center;
					.seach {
						margin-left: 15px;
					}
					.se-sp {
						margin-left: 30px;
					}
				}
			}
		}
		.bom-1-2 {
			margin-bottom: 10px;
			display: flex;
			align-items: center;
			.address-input {
				border: 1px solid rgba(204, 204, 204, 1);
				height: 35px;
				width: 950px;
				margin-left: 13px;
			}
		}
		.bom-1-3 {
			display: flex;
			align-items: center;
			margin-left: 40px;
			margin-bottom: 5px;
			.u-text {
				margin-left: 15px;
				width: 955px;
			}
		}
		.bom-1-4 {
			.bom-p1 {
				display: flex;
				align-items: center;
			}
			.bom-bm {
				margin-top: 10px;
				padding-bottom: 15px;
				display: flex;
				align-items: center;
				.bom-se {
					margin-left: 48px;
					display: flex;
					align-items: center;
					.address-input {
						border: 1px solid rgba(204, 204, 204, 1);
						height: 35px;
						margin-left: 15px;
					}
				}
			}
		}
	}
}
.cur-add {
	width: 28%;
	margin: 25px 0;
	background: rgba(36, 93, 209, 1);
	color: #fff;
}
.districtSelect{
  width: 300rpx;
  margin-left: 40rpx;
}
</style>
