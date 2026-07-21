<template>
	<view class="batch-page">
		<!-- 筛选区 -->
		<view class="filter-bar">
			<view class="filter-row">
				<view class="filter-item">
					<text class="filter-label">体检时间</text>
					<uni-datetime-picker
						type="date"
						v-model="query.screeningDate"
						:clear-icon="true"
						placeholder="选择日期"
						@change="onDateChange"
					/>
				</view>
				<view class="filter-item">
					<text class="filter-label">学校</text>
					<uni-easyinput
						v-model="query.schoolName"
						placeholder="请输入学校名称"
						clearable
						style="width: 220px"
					/>
				</view>
				<view class="filter-item">
					<text class="filter-label">区域</text>
					<uni-easyinput
						v-model="query.district"
						placeholder="请输入区域"
						clearable
						style="width: 220px"
					/>
				</view>
				<view class="filter-actions">
					<u-button type="primary" text="查询" @click="handleSearch" :loading="loading" />
					<u-button text="重置" @click="handleReset" style="margin-left: 10px" />
				</view>
			</view>
		</view>

		<!-- 批次列表 -->
		<view class="batch-list">
			<uni-table :loading="loading" stripe emptyText="暂无批次数据">
				<uni-tr>
					<uni-th width="60" align="center">序号</uni-th>
					<uni-th width="180" align="center">批次名称</uni-th>
					<uni-th width="120" align="center">批次编号</uni-th>
					<uni-th width="120" align="center">体检时间</uni-th>
					<uni-th width="120" align="center">区域</uni-th>
					<uni-th width="150" align="center">学校数</uni-th>
					<uni-th width="120" align="center">受检人数</uni-th>
					<uni-th width="120" align="center">筛查项目</uni-th>
					<uni-th width="120" align="center">筛查进度</uni-th>
					<uni-th width="180" align="center">操作</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in batchList" :key="item.id">
					<uni-td align="center">{{ index + 1 + (pageCurrent - 1) * pageSize }}</uni-td>
					<uni-td align="center">{{ item.batchName }}</uni-td>
					<uni-td align="center">{{ item.batchNo }}</uni-td>
					<uni-td align="center">{{ item.screeningDate }}</uni-td>
					<uni-td align="center">{{ item.district }}</uni-td>
					<uni-td align="center">{{ item.schoolCount || 0 }}</uni-td>
					<uni-td align="center">{{ item.personCount || 0 }}</uni-td>
					<uni-td align="center">{{ item.screenItemCount || 0 }}</uni-td>
					<uni-td align="center">
						<view class="progress-cell">
							<u-line-progress
								:percentage="item.completionRate || 0"
								:height="8"
								activeColor="#245dd1"
							/>
							<text class="progress-text">{{ item.completionRate || 0 }}%</text>
						</view>
					</uni-td>
					<uni-td align="center">
						<view class="op-btns">
							<text class="op-btn op-detail" @click="viewDetail(item)">详情</text>
							<text class="op-btn op-task" @click="enterTask(item)">进入任务</text>
						</view>
					</uni-td>
				</uni-tr>
			</uni-table>
			<view class="pagination-box">
				<uni-pagination
					show-icon
					:page-size="pageSize"
					:current="pageCurrent"
					:total="total"
					@change="changePage"
				/>
			</view>
		</view>

		<!-- 批次详情弹窗 -->
		<u-popup :show="showDetail" mode="center" :closeOnClickOverlay="true" @close="closeDetail" round="12">
			<view class="detail-popup" v-if="currentBatch">
				<view class="detail-title">批次详情</view>
				<scroll-view scroll-y class="detail-content">
					<view class="detail-row">
						<text class="detail-label">批次名称：</text>
						<text class="detail-value">{{ currentBatch.batchName }}</text>
					</view>
					<view class="detail-row">
						<text class="detail-label">批次编号：</text>
						<text class="detail-value">{{ currentBatch.batchNo }}</text>
					</view>
					<view class="detail-row">
						<text class="detail-label">体检时间：</text>
						<text class="detail-value">{{ currentBatch.screeningDate }}</text>
					</view>
					<view class="detail-row">
						<text class="detail-label">区域：</text>
						<text class="detail-value">{{ currentBatch.district }}</text>
					</view>

					<view class="section-title">学校列表</view>
					<view class="tag-list">
						<view
							v-for="(school, idx) in detailSchools"
							:key="idx"
							class="tag-item"
						>{{ school.schoolName }}（{{ school.classCount || 0 }}班 / {{ school.personCount || 0 }}人）</view>
						<view v-if="!detailSchools.length" class="empty-tip">暂无学校数据</view>
					</view>

					<view class="section-title">筛查项目</view>
					<view class="tag-list">
						<view
							v-for="(item, idx) in detailScreenItems"
							:key="idx"
							class="tag-item tag-item-blue"
						>{{ item.itemName }}</view>
						<view v-if="!detailScreenItems.length" class="empty-tip">暂无项目数据</view>
					</view>

					<view class="section-title">筛查进度</view>
					<view class="progress-block">
						<view class="progress-stat">
							<text class="stat-label">受检人数：</text>
							<text class="stat-value">{{ detailProgress.personCount || 0 }} 人</text>
						</view>
						<view class="progress-stat">
							<text class="stat-label">已筛查：</text>
							<text class="stat-value">{{ detailProgress.screenedCount || 0 }} 人</text>
						</view>
						<view class="progress-stat">
							<text class="stat-label">未筛查：</text>
							<text class="stat-value">{{ detailProgress.unscreenedCount || 0 }} 人</text>
						</view>
						<view class="progress-stat">
							<text class="stat-label">完成率：</text>
							<text class="stat-value">{{ detailProgress.completionRate || 0 }}%</text>
						</view>
					</view>
				</scroll-view>
				<view class="detail-footer">
					<u-button text="关闭" @click="closeDetail" />
					<u-button type="primary" text="进入筛查任务" @click="enterTask(currentBatch)" style="margin-left: 10px" />
				</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
import * as BatchApi from '@/api/screen/batch';

export default {
	data() {
		return {
			loading: false,
			query: {
				screeningDate: '',
				schoolName: '',
				district: ''
			},
			batchList: [],
			pageCurrent: 1,
			pageSize: 10,
			total: 0,
			showDetail: false,
			currentBatch: null,
			detailSchools: [],
			detailScreenItems: [],
			detailProgress: {}
		};
	},
	onLoad() {
		this.loadList();
	},
	onPullDownRefresh() {
		this.pageCurrent = 1;
		this.loadList().finally(() => uni.stopPullDownRefresh());
	},
	methods: {
		async loadList() {
			this.loading = true;
			try {
				const res = await BatchApi.getBatchPage({
					pageNo: this.pageCurrent,
					pageSize: this.pageSize,
					...this.query
				});
				this.batchList = res.list || res.data || [];
				this.total = res.total || 0;
			} catch (e) {
				this.batchList = [];
				this.total = 0;
			} finally {
				this.loading = false;
			}
		},
		onDateChange(e) {
			this.query.screeningDate = e;
		},
		handleSearch() {
			this.pageCurrent = 1;
			this.loadList();
		},
		handleReset() {
			this.query = { screeningDate: '', schoolName: '', district: '' };
			this.pageCurrent = 1;
			this.loadList();
		},
		changePage(e) {
			this.pageCurrent = e.current;
			this.loadList();
		},
		async viewDetail(item) {
			this.currentBatch = item;
			this.showDetail = true;
			this.detailSchools = [];
			this.detailScreenItems = [];
			this.detailProgress = {};
			try {
				const [schools, items, progress] = await Promise.all([
					BatchApi.getBatchSchools(item.id),
					BatchApi.getBatchScreenItems(item.id),
					BatchApi.getBatchProgress(item.id)
				]);
				this.detailSchools = schools || [];
				this.detailScreenItems = items || [];
				this.detailProgress = progress || {};
			} catch (e) {
				uni.showToast({ title: '详情加载失败', icon: 'none' });
			}
		},
		closeDetail() {
			this.showDetail = false;
			this.currentBatch = null;
		},
		enterTask(item) {
			uni.navigateTo({
				url: '/pages/screen/register/index?batchId=' + item.id + '&batchName=' + encodeURIComponent(item.batchName || '')
			});
		}
	}
};
</script>

<style scoped lang="scss">
.batch-page {
	padding: 16px;
	background: rgba(244, 247, 252, 1);
	min-height: 100vh;
}
.filter-bar {
	background: #fff;
	border-radius: 8px;
	padding: 16px;
	margin-bottom: 16px;
	box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
	.filter-row {
		display: flex;
		flex-wrap: wrap;
		align-items: center;
		gap: 16px;
	}
	.filter-item {
		display: flex;
		align-items: center;
		.filter-label {
			font-size: 14px;
			color: rgba(102, 102, 102, 1);
			margin-right: 8px;
			white-space: nowrap;
		}
	}
	.filter-actions {
		margin-left: auto;
		display: flex;
	}
}
.batch-list {
	background: #fff;
	border-radius: 8px;
	padding: 16px;
	box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
}
.progress-cell {
	display: flex;
	flex-direction: column;
	align-items: center;
	width: 100px;
	.progress-text {
		font-size: 12px;
		color: rgba(36, 93, 209, 1);
		margin-top: 2px;
	}
}
.op-btns {
	display: flex;
	justify-content: center;
	gap: 8px;
	.op-btn {
		padding: 4px 10px;
		font-size: 13px;
		border-radius: 4px;
		cursor: pointer;
	}
	.op-detail {
		color: rgba(36, 93, 209, 1);
		border: 1px solid rgba(36, 93, 209, 1);
	}
	.op-task {
		color: rgba(51, 176, 19, 1);
		border: 1px solid rgba(51, 176, 19, 1);
	}
}
.pagination-box {
	margin-top: 16px;
	display: flex;
	justify-content: flex-end;
}
.detail-popup {
	width: 80vw;
	max-width: 800px;
	background: #fff;
	border-radius: 12px;
	padding: 20px;
	.detail-title {
		font-size: 20px;
		font-weight: 600;
		text-align: center;
		margin-bottom: 16px;
		color: rgba(36, 93, 209, 1);
	}
	.detail-content {
		max-height: 60vh;
		padding: 0 8px;
	}
	.detail-row {
		display: flex;
		margin-bottom: 10px;
		font-size: 14px;
		.detail-label {
			width: 100px;
			color: rgba(102, 102, 102, 1);
		}
		.detail-value {
			flex: 1;
			color: rgba(51, 51, 51, 1);
		}
	}
	.section-title {
		font-size: 15px;
		font-weight: 600;
		margin: 16px 0 10px;
		color: rgba(51, 51, 51, 1);
		border-left: 3px solid rgba(36, 93, 209, 1);
		padding-left: 8px;
	}
	.tag-list {
		display: flex;
		flex-wrap: wrap;
		gap: 8px;
		.tag-item {
			padding: 4px 10px;
			background: rgba(51, 176, 19, 0.1);
			color: rgba(51, 176, 19, 1);
			border-radius: 4px;
			font-size: 13px;
		}
		.tag-item-blue {
			background: rgba(36, 93, 209, 0.1);
			color: rgba(36, 93, 209, 1);
		}
		.empty-tip {
			color: rgba(153, 153, 153, 1);
			font-size: 13px;
		}
	}
	.progress-block {
		background: rgba(244, 247, 252, 1);
		padding: 12px;
		border-radius: 6px;
		.progress-stat {
			display: flex;
			justify-content: space-between;
			margin-bottom: 6px;
			font-size: 14px;
			.stat-label {
				color: rgba(102, 102, 102, 1);
			}
			.stat-value {
				color: rgba(36, 93, 209, 1);
				font-weight: 600;
			}
		}
	}
	.detail-footer {
		display: flex;
		justify-content: center;
		margin-top: 16px;
		padding-top: 16px;
		border-top: 1px solid rgba(238, 238, 238, 1);
	}
}
</style>
