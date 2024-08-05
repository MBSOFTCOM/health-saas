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
				<view class="top-right1" style="margin-right: 10px">
					<up-button
						@click="PcToPad"
						style="margin-left: 10px"
						type="success"
						icon="download"
						iconColor="#fff"
						:plain="true"
						class="custom-sync"
						text="同步"
					></up-button>
				</view>
			</view>
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
						@clear="clearIdNum"
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
					<uni-th width="60" align="center">序号</uni-th>
					<uni-th width="5%" align="center">筛查编号</uni-th>
					<uni-th width="8%" align="center">摸底表id</uni-th>
					<uni-th width="5%" align="center">心电图照片</uni-th>
					<uni-th width="5%" align="center">医生签名</uni-th>
					<uni-th width="8%" align="center">筛查次序</uni-th>
					<uni-th width="8%" align="center">筛查时间</uni-th>
					<uni-th width="8%" align="center">筛查点</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in pageData" :key="index">
					<uni-td align="center">{{ index + 1 }}</uni-td>
					<uni-td align="center">{{ item.screenId }}</uni-td>
					<uni-td align="center">{{ item.personId }}</uni-td>
					<uni-td align="center">{{ item.electrocardiogram }}</uni-td>
					<uni-td align="center">{{ item.doctorSignature }}</uni-td>
					<uni-td align="center">{{ item.screenOrder }}</uni-td>
					<uni-td align="center">{{ formatDate(item.screenTime) }}</uni-td>
					<uni-td align="center">{{ item.screenPoint }}</uni-td>
				</uni-tr>
			</uni-table>
			<view class="uni-pagination-box">
				<uni-pagination
					show-icon
					:page-size="queryParams.pageSize"
					:current="queryParams.pageNo"
					:total="total"
					@change="change"
				/>
			</view>
		</view>
	</view>
</template>

<script>
import * as SynchronizeApi from '@/api/synchronize/synchronize';
import dbUtils from '../../../uni_modules/zjy-sqlite-manage/components/zjy-sqlite-manage/dbUtils';
import { dbName, tbScreenPerson } from '@/utils/sqlite';
import { genderMap, commonMap, firstTypeMap, moreTypeMap, nationMap } from '@/utils/dict.js';

export default {
	data() {
		return {
			nav: [
				{
					value: '数据同步'
				},
				{
					value: '心电图组',
				},
				{
					value: 'PC端到平板端',
					isActive: true
				}
			],
			loading: false,
			// 数据总量
			total: 0,
			//列表数据
			pageData: [],
			queryParams: {
				// 每页数据量
				pageSize: 10,
				// 当前页
				pageNo: 1,
				screenId: undefined,
				screenPoint: uni.$user.screenPoint
			},
			// 封装待同步的数据
			SyncData: []
		};
	},
	//页面加载时自动调用
	onLoad() {
		this.selectedIndexs = [];
		this.search();
	},
	methods: {
		back() {
			uni.navigateBack({
				delta: 1
			});
		},
		close() {
			this.show = false;
			// console.log('close');
		},
		// 分页触发
		change(e) {
			this.$refs.table.clearSelection(); //清除勾选内容
			this.selectedIndexs.length = 0; // 清空索引数组
			this.queryParams.pageNo = e.current;
			this.search();
		},
		handleQuery() {
			let event = { type: 'current', current: 1 };
			this.change(event);
		},
		// 搜索
		search() {
			// this.loading = true;
			// 心电表
			SynchronizeApi.getTableData6(this.queryParams).then((resp) => {
				this.pageData = resp.data.list;
				this.total = resp.data.total;
				// 	this.loading = false;
			});
		},
		// 重置
		reset() {
			this.$refs.table.clearSelection(); //清除勾选内容
			this.selectedIndexs.length = 0; // 清空索引数组
			this.queryParams.screenId = undefined;
			this.queryParams.pageNo = 1;
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

		// pc到平板
		PcToPad() {
			if (this.SyncData.length == 0) {
				let self = this;
				uni.showModal({
					title: '提示信息',
					content: '是否同步全部数据？',
					cancelText: '取消',
					confirmText: '确认',
					success: function (res) {
						if (res.confirm) {
							// 获取全部数据，不分页
							self.queryParams.pageSize = -1;

							SynchronizeApi.getTableData2(self.queryParams).then((resp) => {
								self.SyncData = resp.data.list;
								let newData = self.SyncData.map((item) => {
									// 创建一个新对象，包含除了 moreTypeStr 属性之外的其他属性
									const { moreTypeStr, ...newItem } = item;
									return newItem;
								});

								let i = 0;
								newData.forEach((item) => {
									dbUtils.addTabItem(dbName, tbScreenPerson, item);
									i++;
								});
								if (i > 0) {
									uni.showToast({
										title: '同步了' + i + '条数据',
										mask: true,
										icon: 'success',
										duration: 3000
									});
								}
							});
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
			} else {
				let self = this;
				uni.showModal({
					title: '提示信息',
					content: '是否同步当前所选数据？',
					cancelText: '取消',
					confirmText: '确认',
					success: function (res) {
						if (res.confirm) {
							// todo 同步勾选数据

							let i = 0;
							newData.forEach((item) => {
								dbUtils.addTabItem(dbName, tbScreenPerson, item);
								i++;
							});
							console.log('同步了' + i + '条数据');
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
		// 多选处理
		selectedItems() {
			return this.selectedIndexs.map((i) => this.pageData[i]);
		},
		// 多选
		selectionChange(e) {
			// 存 筛查编号??
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
</style>
