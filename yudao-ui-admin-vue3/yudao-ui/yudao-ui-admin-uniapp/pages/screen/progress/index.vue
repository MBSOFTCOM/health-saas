<template>
	<view class="progress-page">
		<!-- 筛选区 -->
		<view class="filter-bar">
			<view class="filter-row">
				<view class="filter-item">
					<text class="filter-label">批次</text>
					<view class="select-box" @click="showBatchPicker = true">
						<text>{{ batchName || '请选择批次' }}</text>
					</view>
				</view>
				<view class="filter-item">
					<text class="filter-label">统计维度</text>
					<uni-data-select
						v-model="query.dimension"
						:localdata="dimensionOptions"
						placeholder="选择维度"
						style="width: 180px"
					/>
				</view>
				<view class="filter-item">
					<text class="filter-label">学校</text>
					<uni-easyinput v-model="query.schoolName" placeholder="学校名称" clearable style="width: 160px" />
				</view>
				<view class="filter-item">
					<text class="filter-label">年级</text>
					<uni-easyinput v-model="query.grade" placeholder="年级" clearable style="width: 100px" />
				</view>
				<view class="filter-item">
					<text class="filter-label">班级</text>
					<uni-easyinput v-model="query.className" placeholder="班级" clearable style="width: 100px" />
				</view>
				<view class="filter-actions">
					<u-button type="primary" text="查询" @click="handleSearch" :loading="loading" />
					<u-button text="重置" @click="handleReset" style="margin-left: 10px" />
				</view>
			</view>
		</view>

		<!-- 总览统计卡片 -->
		<view class="summary-cards">
			<view class="summary-card card-blue">
				<view class="card-icon">人</view>
				<view class="card-info">
					<text class="card-num">{{ overview.totalCount || 0 }}</text>
					<text class="card-label">受检总人数</text>
				</view>
			</view>
			<view class="summary-card card-green">
				<view class="card-icon">筛</view>
				<view class="card-info">
					<text class="card-num">{{ overview.screenedCount || 0 }}</text>
					<text class="card-label">已筛查人数</text>
				</view>
			</view>
			<view class="summary-card card-orange">
				<view class="card-icon">未</view>
				<view class="card-info">
					<text class="card-num">{{ overview.unscreenedCount || 0 }}</text>
					<text class="card-label">未筛查人数</text>
				</view>
			</view>
			<view class="summary-card card-purple">
				<view class="card-icon">率</view>
				<view class="card-info">
					<text class="card-num">{{ overview.completionRate || 0 }}%</text>
					<text class="card-label">筛查完成率</text>
				</view>
			</view>
		</view>

		<!-- 现场实时进度 -->
		<view class="live-card">
			<view class="card-title">
				<text>现场实时进度</text>
				<text class="refresh-btn" @click="refreshLive">刷新</text>
			</view>
			<view class="live-content" v-if="liveProgress.tasks && liveProgress.tasks.length">
				<view v-for="(task, idx) in liveProgress.tasks" :key="idx" class="live-item">
					<view class="live-item-header">
						<text class="live-name">{{ task.taskName }}</text>
						<text class="live-status" :class="getLiveStatusClass(task.status)">{{ getLiveStatusText(task.status) }}</text>
					</view>
					<view class="live-progress">
						<u-line-progress :percentage="task.completionRate || 0" :height="10" activeColor="#245dd1" />
					</view>
					<view class="live-stats">
						<text class="live-stat">已检：{{ task.screenedCount || 0 }}</text>
						<text class="live-stat">总数：{{ task.totalCount || 0 }}</text>
						<text class="live-stat">负责人：{{ task.leader || '-' }}</text>
					</view>
				</view>
			</view>
			<view v-else class="empty-tip">暂无实时进度数据</view>
		</view>

		<!-- 多维度统计列表 -->
		<view class="dimension-card">
			<view class="card-title">
				<text>按{{ currentDimensionLabel }}统计</text>
			</view>
			<uni-table :loading="loading" stripe emptyText="暂无数据">
				<uni-tr>
					<uni-th width="60" align="center">序号</uni-th>
					<uni-th width="180" align="center">{{ currentDimensionLabel }}</uni-th>
					<uni-th width="100" align="center">受检人数</uni-th>
					<uni-th width="100" align="center">已筛查</uni-th>
					<uni-th width="100" align="center">未筛查</uni-th>
					<uni-th width="120" align="center">完成率</uni-th>
					<uni-th width="100" align="center">异常人数</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in dimensionList" :key="index">
					<uni-td align="center">{{ index + 1 }}</uni-td>
					<uni-td align="center">{{ item.name }}</uni-td>
					<uni-td align="center">{{ item.totalCount || 0 }}</uni-td>
					<uni-td align="center">{{ item.screenedCount || 0 }}</uni-td>
					<uni-td align="center">{{ item.unscreenedCount || 0 }}</uni-td>
					<uni-td align="center">
						<view class="progress-cell">
							<u-line-progress :percentage="item.completionRate || 0" :height="8" activeColor="#245dd1" />
							<text class="progress-text">{{ item.completionRate || 0 }}%</text>
						</view>
					</uni-td>
					<uni-td align="center">
						<text :class="{ 'abnormal-text': item.abnormalCount > 0 }">{{ item.abnormalCount || 0 }}</text>
					</uni-td>
				</uni-tr>
			</uni-table>
		</view>

		<!-- 任务完成情况 -->
		<view class="task-card">
			<view class="card-title">任务完成情况</view>
			<uni-table :loading="taskLoading" stripe emptyText="暂无任务数据">
				<uni-tr>
					<uni-th width="60" align="center">序号</uni-th>
					<uni-th width="180" align="center">任务名称</uni-th>
					<uni-th width="120" align="center">任务类型</uni-th>
					<uni-th width="100" align="center">负责人</uni-th>
					<uni-th width="100" align="center">完成状态</uni-th>
					<uni-th width="120" align="center">完成时间</uni-th>
				</uni-tr>
				<uni-tr v-for="(task, index) in taskList" :key="index">
					<uni-td align="center">{{ index + 1 }}</uni-td>
					<uni-td align="center">{{ task.taskName }}</uni-td>
					<uni-td align="center">{{ task.taskType }}</uni-td>
					<uni-td align="center">{{ task.leader || '-' }}</uni-td>
					<uni-td align="center">
						<text class="status-tag" :class="getTaskStatusClass(task.status)">{{ getTaskStatusText(task.status) }}</text>
					</uni-td>
					<uni-td align="center">{{ task.completeTime || '-' }}</uni-td>
				</uni-tr>
			</uni-table>
		</view>

		<!-- 批次选择弹窗 -->
		<u-popup :show="showBatchPicker" mode="center" @close="showBatchPicker = false" round="12">
			<view class="batch-picker">
				<view class="popup-title">选择体检批次</view>
				<scroll-view scroll-y class="batch-list-scroll">
					<view
						v-for="b in batchOptions"
						:key="b.id"
						class="batch-option"
						:class="{ active: b.id === batchId }"
						@click="selectBatch(b)"
					>
						<text class="batch-name">{{ b.batchName }}</text>
						<text class="batch-date">{{ b.screeningDate }}</text>
					</view>
					<view v-if="!batchOptions.length" class="empty-tip">暂无批次</view>
				</scroll-view>
			</view>
		</u-popup>
	</view>
</template>

<script>
import * as ProgressApi from '@/api/screen/progress';
import * as BatchApi from '@/api/screen/batch';

export default {
	data() {
		return {
			loading: false,
			taskLoading: false,
			batchId: '',
			batchName: '',
			batchOptions: [],
			showBatchPicker: false,
			query: {
				dimension: 'school',
				schoolName: '',
				grade: '',
				className: ''
			},
			dimensionOptions: [
				{ value: 'batch', text: '按批次' },
				{ value: 'school', text: '按学校' },
				{ value: 'grade', text: '按年级' },
				{ value: 'class', text: '按班级' }
			],
			overview: {},
			dimensionList: [],
			liveProgress: {},
			taskList: []
		};
	},
	computed: {
		currentDimensionLabel() {
			const opt = this.dimensionOptions.find(o => o.value === this.query.dimension);
			return opt ? opt.text.replace('按', '') : '学校';
		}
	},
	onLoad(e) {
		this.batchId = e.batchId || '';
		this.batchName = e.batchName ? decodeURIComponent(e.batchName) : '';
		this.loadBatchOptions();
	},
	methods: {
		async loadBatchOptions() {
			try {
				const res = await BatchApi.getBatchList({});
				this.batchOptions = res || [];
				if (!this.batchId && this.batchOptions.length) {
					this.selectBatch(this.batchOptions[0]);
				} else if (this.batchId) {
					this.loadAll();
				}
			} catch (e) {}
		},
		selectBatch(b) {
			this.batchId = b.id;
			this.batchName = b.batchName;
			this.showBatchPicker = false;
			this.loadAll();
		},
		async loadAll() {
			this.loadOverview();
			this.loadDimension();
			this.loadLive();
			this.loadTasks();
		},
		async loadOverview() {
			try {
				const res = await ProgressApi.getProgressSummary({
					batchId: this.batchId,
					...this.query
				});
				this.overview = res || {};
			} catch (e) {
				this.overview = {};
			}
		},
		async loadDimension() {
			this.loading = true;
			try {
				const res = await ProgressApi.getProgressByDimension(this.query.dimension, {
					batchId: this.batchId,
					...this.query
				});
				this.dimensionList = res || [];
			} catch (e) {
				this.dimensionList = [];
			} finally {
				this.loading = false;
			}
		},
		async loadLive() {
			try {
				const res = await ProgressApi.getLiveProgress(this.batchId);
				this.liveProgress = res || {};
			} catch (e) {
				this.liveProgress = {};
			}
		},
		async loadTasks() {
			this.taskLoading = true;
			try {
				const res = await ProgressApi.getTaskCompletion(this.batchId, {
					...this.query
				});
				this.taskList = res || [];
			} catch (e) {
				this.taskList = [];
			} finally {
				this.taskLoading = false;
			}
		},
		handleSearch() {
			this.loadAll();
		},
		handleReset() {
			this.query = { dimension: 'school', schoolName: '', grade: '', className: '' };
			this.loadAll();
		},
		refreshLive() {
			this.loadLive();
			uni.showToast({ title: '已刷新', icon: 'success' });
		},
		getLiveStatusText(status) {
			const map = { running: '进行中', done: '已完成', waiting: '待开始' };
			return map[status] || status;
		},
		getLiveStatusClass(status) {
			if (status === 'done') return 'tag-done';
			if (status === 'waiting') return 'tag-pending';
			return 'tag-doing';
		},
		getTaskStatusText(status) {
			const map = { completed: '已完成', doing: '进行中', pending: '未开始' };
			return map[status] || status;
		},
		getTaskStatusClass(status) {
			if (status === 'completed') return 'tag-done';
			if (status === 'pending') return 'tag-pending';
			return 'tag-doing';
		}
	}
};
</script>

<style scoped lang="scss">
.progress-page {
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
		.filter-label { font-size: 14px; color: rgba(102, 102, 102, 1); margin-right: 8px; white-space: nowrap; }
		.select-box {
			padding: 6px 12px;
			border: 1px solid rgba(220, 220, 220, 1);
			border-radius: 4px;
			min-width: 160px;
			font-size: 14px;
			cursor: pointer;
		}
	}
	.filter-actions { margin-left: auto; display: flex; }
}
.summary-cards {
	display: flex;
	gap: 12px;
	margin-bottom: 16px;
	flex-wrap: wrap;
	.summary-card {
		flex: 1;
		min-width: 200px;
		background: #fff;
		border-radius: 8px;
		padding: 16px;
		display: flex;
		align-items: center;
		gap: 12px;
		box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
		.card-icon {
			width: 50px;
			height: 50px;
			border-radius: 50%;
			display: flex;
			align-items: center;
			justify-content: center;
			color: #fff;
			font-size: 20px;
			font-weight: 600;
		}
		.card-info {
			display: flex;
			flex-direction: column;
			.card-num { font-size: 24px; font-weight: 700; color: rgba(51, 51, 51, 1); }
			.card-label { font-size: 13px; color: rgba(102, 102, 102, 1); margin-top: 2px; }
		}
		&.card-blue .card-icon { background: rgba(36, 93, 209, 1); }
		&.card-green .card-icon { background: rgba(51, 176, 19, 1); }
		&.card-orange .card-icon { background: rgba(240, 163, 41, 1); }
		&.card-purple .card-icon { background: rgba(80, 104, 242, 1); }
	}
}
.live-card, .dimension-card, .task-card {
	background: #fff;
	border-radius: 8px;
	padding: 16px;
	margin-bottom: 16px;
	box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
	.card-title {
		font-size: 16px;
		font-weight: 600;
		color: rgba(51, 51, 51, 1);
		margin-bottom: 12px;
		border-left: 3px solid rgba(36, 93, 209, 1);
		padding-left: 8px;
		display: flex;
		justify-content: space-between;
		align-items: center;
		.refresh-btn {
			font-size: 13px;
			color: rgba(36, 93, 209, 1);
			cursor: pointer;
			font-weight: 400;
		}
	}
}
.live-content {
	.live-item {
		padding: 12px;
		background: rgba(244, 247, 252, 1);
		border-radius: 6px;
		margin-bottom: 10px;
		.live-item-header {
			display: flex;
			justify-content: space-between;
			margin-bottom: 8px;
			.live-name { font-size: 15px; font-weight: 600; color: rgba(51, 51, 51, 1); }
			.live-status { padding: 2px 8px; border-radius: 4px; font-size: 12px; }
		}
		.live-progress { margin-bottom: 8px; }
		.live-stats {
			display: flex;
			gap: 16px;
			font-size: 13px;
			color: rgba(102, 102, 102, 1);
		}
	}
}
.progress-cell {
	display: flex;
	flex-direction: column;
	align-items: center;
	width: 100px;
	.progress-text { font-size: 12px; color: rgba(36, 93, 209, 1); margin-top: 2px; }
}
.status-tag {
	padding: 2px 8px;
	border-radius: 4px;
	font-size: 12px;
	&.tag-done { background: rgba(51, 176, 19, 0.1); color: rgba(51, 176, 19, 1); }
	&.tag-pending { background: rgba(240, 163, 41, 0.1); color: rgba(240, 163, 41, 1); }
	&.tag-doing { background: rgba(36, 93, 209, 0.1); color: rgba(36, 93, 209, 1); }
}
.abnormal-text { color: rgba(223, 65, 65, 1); font-weight: 600; }
.empty-tip { text-align: center; color: rgba(153, 153, 153, 1); padding: 20px 0; font-size: 14px; }
.batch-picker {
	width: 70vw;
	max-width: 600px;
	background: #fff;
	border-radius: 12px;
	padding: 20px;
	.popup-title { font-size: 18px; font-weight: 600; text-align: center; margin-bottom: 16px; color: rgba(36, 93, 209, 1); }
	.batch-list-scroll { max-height: 50vh;
		.batch-option { padding: 12px; border: 1px solid rgba(238, 238, 238, 1); border-radius: 6px; margin-bottom: 8px; cursor: pointer; display: flex; justify-content: space-between;
			&.active { border-color: rgba(36, 93, 209, 1); background: rgba(36, 93, 209, 0.04); }
			.batch-name { font-size: 15px; color: rgba(51, 51, 51, 1); }
			.batch-date { font-size: 13px; color: rgba(102, 102, 102, 1); }
		}
		.empty-tip { text-align: center; color: rgba(153, 153, 153, 1); padding: 20px 0; }
	}
}
</style>
