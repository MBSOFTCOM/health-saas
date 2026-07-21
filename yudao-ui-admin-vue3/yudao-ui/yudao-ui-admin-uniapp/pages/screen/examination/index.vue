<template>
	<view class="examination-page">
		<!-- 顶部操作 -->
		<view class="top-bar">
			<view class="batch-info">
				<text class="info-label">当前批次：</text>
				<text class="info-value">{{ batchName || '未选择' }}</text>
				<text class="change-btn" @click="showBatchPicker = true">切换</text>
			</view>
			<u-button type="primary" icon="scan" text="扫码查看总检" @click="scanQrcode" :loading="scanning" />
		</view>

		<!-- 受检者信息 -->
		<view class="student-card" v-if="studentInfo">
			<view class="card-header">
				<text class="header-title">受检者信息</text>
				<text class="header-status" :class="statusClass">{{ statusText }}</text>
			</view>
			<view class="card-body">
				<view class="info-row">
					<text class="info-item">姓名：{{ studentInfo.name }}</text>
					<text class="info-item">性别：{{ formatSex(studentInfo.sex) }}</text>
					<text class="info-item">年龄：{{ studentInfo.age }}</text>
				</view>
				<view class="info-row">
					<text class="info-item">学校：{{ studentInfo.schoolName }}</text>
					<text class="info-item">班级：{{ studentInfo.grade }}{{ studentInfo.className }}</text>
				</view>
			</view>
		</view>

		<view v-else class="empty-tip">
			<text class="empty-text">请扫描受检者二维码查看筛查完成情况</text>
		</view>

		<!-- 筛查进度概览 -->
		<view class="overview-card" v-if="studentInfo">
			<view class="card-title">筛查进度概览</view>
			<view class="overview-stats">
				<view class="stat-block stat-total">
					<text class="stat-num">{{ summary.totalItems || 0 }}</text>
					<text class="stat-label">总项目</text>
				</view>
				<view class="stat-block stat-done">
					<text class="stat-num">{{ summary.completedItems || 0 }}</text>
					<text class="stat-label">已完成</text>
				</view>
				<view class="stat-block stat-pending">
					<text class="stat-num">{{ summary.pendingItems || 0 }}</text>
					<text class="stat-label">未完成</text>
				</view>
				<view class="stat-block stat-abnormal">
					<text class="stat-num">{{ summary.abnormalItems || 0 }}</text>
					<text class="stat-label">异常项</text>
				</view>
				<view class="stat-block stat-rate">
					<text class="stat-num">{{ summary.completionRate || 0 }}%</text>
					<text class="stat-label">完成率</text>
				</view>
			</view>
		</view>

		<!-- 项目状态列表 -->
		<view class="items-card" v-if="studentInfo">
			<view class="card-title">项目状态明细</view>
			<uni-table stripe emptyText="暂无项目数据">
				<uni-tr>
					<uni-th width="60" align="center">序号</uni-th>
					<uni-th width="150" align="center">项目类别</uni-th>
					<uni-th width="180" align="center">项目名称</uni-th>
					<uni-th width="100" align="center">状态</uni-th>
					<uni-th width="100" align="center">结果</uni-th>
					<uni-th width="120" align="center">登记时间</uni-th>
					<uni-th width="120" align="center">操作</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in itemList" :key="index">
					<uni-td align="center">{{ index + 1 }}</uni-td>
					<uni-td align="center">{{ item.category }}</uni-td>
					<uni-td align="center">{{ item.itemName }}</uni-td>
					<uni-td align="center">
						<text class="status-tag" :class="getStatusClass(item.status)">{{ getStatusText(item.status) }}</text>
					</uni-td>
					<uni-td align="center">
						<text :class="{ 'abnormal-text': item.abnormal }">{{ item.result || '-' }}</text>
					</uni-td>
					<uni-td align="center">{{ item.registerTime || '-' }}</uni-td>
					<uni-td align="center">
						<text v-if="item.status === 'pending'" class="op-btn op-recheck" @click="createRecheck(item)">补检</text>
						<text v-else class="op-btn op-detail" @click="viewItemDetail(item)">详情</text>
					</uni-td>
				</uni-tr>
			</uni-table>
		</view>

		<!-- 异常结果列表 -->
		<view class="abnormal-card" v-if="studentInfo && abnormalList.length">
			<view class="card-title">异常结果</view>
			<view class="abnormal-list">
				<view v-for="(item, index) in abnormalList" :key="index" class="abnormal-item">
					<view class="abnormal-left">
						<text class="abnormal-category">{{ item.category }}</text>
						<text class="abnormal-name">{{ item.itemName }}</text>
					</view>
					<view class="abnormal-right">
						<text class="abnormal-result">{{ item.result }}</text>
						<text class="abnormal-flag">异常</text>
					</view>
				</view>
			</view>
		</view>

		<!-- 补检任务列表 -->
		<view class="recheck-card" v-if="studentInfo">
			<view class="card-title">补检任务</view>
			<uni-table stripe emptyText="暂无补检任务">
				<uni-tr>
					<uni-th width="60" align="center">序号</uni-th>
					<uni-th width="150" align="center">项目</uni-th>
					<uni-th width="120" align="center">状态</uni-th>
					<uni-th width="150" align="center">创建时间</uni-th>
					<uni-th width="120" align="center">操作</uni-th>
				</uni-tr>
				<uni-tr v-for="(task, index) in recheckList" :key="task.id">
					<uni-td align="center">{{ index + 1 }}</uni-td>
					<uni-td align="center">{{ task.itemName }}</uni-td>
					<uni-td align="center">
						<text class="status-tag" :class="getRecheckStatusClass(task.status)">{{ getRecheckStatusText(task.status) }}</text>
					</uni-td>
					<uni-td align="center">{{ task.createTime }}</uni-td>
					<uni-td align="center">
						<text v-if="task.status === 'pending'" class="op-btn op-complete" @click="completeRecheck(task)">完成</text>
						<text v-else class="op-btn op-detail">已完成</text>
					</uni-td>
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

		<!-- 项目详情弹窗 -->
		<u-popup :show="showItemDetail" mode="center" @close="showItemDetail = false" round="12">
			<view class="detail-popup" v-if="currentItem">
				<view class="popup-title">项目详情</view>
				<view class="detail-row">
					<text class="detail-label">项目类别：</text>
					<text class="detail-value">{{ currentItem.category }}</text>
				</view>
				<view class="detail-row">
					<text class="detail-label">项目名称：</text>
					<text class="detail-value">{{ currentItem.itemName }}</text>
				</view>
				<view class="detail-row">
					<text class="detail-label">状态：</text>
					<text class="detail-value">{{ getStatusText(currentItem.status) }}</text>
				</view>
				<view class="detail-row">
					<text class="detail-label">结果：</text>
					<text class="detail-value" :class="{ 'abnormal-text': currentItem.abnormal }">{{ currentItem.result || '-' }}</text>
				</view>
				<view class="detail-row">
					<text class="detail-label">登记时间：</text>
					<text class="detail-value">{{ currentItem.registerTime || '-' }}</text>
				</view>
				<view class="detail-row">
					<text class="detail-label">登记人：</text>
					<text class="detail-value">{{ currentItem.operator || '-' }}</text>
				</view>
				<view v-if="currentItem.remark" class="detail-row">
					<text class="detail-label">备注：</text>
					<text class="detail-value">{{ currentItem.remark }}</text>
				</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
import * as ExaminationApi from '@/api/screen/examination';
import * as BatchApi from '@/api/screen/batch';

export default {
	data() {
		return {
			batchId: '',
			batchName: '',
			batchOptions: [],
			showBatchPicker: false,
			studentInfo: null,
			scanning: false,
			summary: {},
			itemList: [],
			abnormalList: [],
			recheckList: [],
			showItemDetail: false,
			currentItem: null
		};
	},
	computed: {
		statusText() {
			if (!this.studentInfo) return '';
			if (this.summary.completionRate >= 100) return '已完成';
			return '筛查中';
		},
		statusClass() {
			return this.summary.completionRate >= 100 ? 'status-done' : 'status-doing';
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
				}
			} catch (e) {}
		},
		selectBatch(b) {
			this.batchId = b.id;
			this.batchName = b.batchName;
			this.showBatchPicker = false;
		},
		scanQrcode() {
			if (!this.batchId) {
				uni.showToast({ title: '请先选择批次', icon: 'none' });
				return;
			}
			this.scanning = true;
			// #ifdef APP-PLUS
			uni.scanCode({
				scanType: ['qrCode'],
				success: (res) => this.handleScanResult(res.result),
				fail: () => uni.showToast({ title: '扫码取消', icon: 'none' }),
				complete: () => { this.scanning = false; }
			});
			// #endif
			// #ifndef APP-PLUS
			uni.showModal({
				title: '模拟扫码',
				content: '请输入二维码内容',
				editable: true,
				placeholderText: '受检者二维码',
				success: (res) => {
					if (res.confirm && res.content) {
						this.handleScanResult(res.content);
					}
					this.scanning = false;
				},
				fail: () => { this.scanning = false; }
			});
			// #endif
		},
		async handleScanResult(qrcode) {
			try {
				const res = await ExaminationApi.getExaminationSummary(qrcode, this.batchId);
				this.studentInfo = res.student || res;
				this.studentInfo.qrcode = qrcode;
				this.summary = res.summary || {};
				this.itemList = res.items || [];
				this.abnormalList = res.abnormalItems || [];
				if (this.studentInfo.id) {
					this.loadRecheckList(this.studentInfo.id);
				}
				uni.showToast({ title: '查询成功', icon: 'success' });
			} catch (e) {
				uni.showToast({ title: '二维码无效', icon: 'none' });
			}
		},
		async loadRecheckList(studentId) {
			try {
				const res = await ExaminationApi.getRecheckTaskList({
					studentId,
					batchId: this.batchId
				});
				this.recheckList = res || [];
			} catch (e) {
				this.recheckList = [];
			}
		},
		formatSex(sex) {
			if (sex === 1 || sex === '1' || sex === '男') return '男';
			if (sex === 2 || sex === '2' || sex === '女') return '女';
			return sex || '-';
		},
		getStatusText(status) {
			const map = { completed: '已完成', pending: '未完成', doing: '进行中' };
			return map[status] || status;
		},
		getStatusClass(status) {
			if (status === 'completed') return 'tag-done';
			if (status === 'pending') return 'tag-pending';
			return 'tag-doing';
		},
		getRecheckStatusText(status) {
			const map = { pending: '待补检', completed: '已完成', cancelled: '已取消' };
			return map[status] || status;
		},
		getRecheckStatusClass(status) {
			if (status === 'completed') return 'tag-done';
			if (status === 'pending') return 'tag-pending';
			return 'tag-doing';
		},
		viewItemDetail(item) {
			this.currentItem = item;
			this.showItemDetail = true;
		},
		async createRecheck(item) {
			uni.showModal({
				title: '创建补检任务',
				content: '确认为该项目创建补检任务？',
				success: async (res) => {
					if (res.confirm) {
						try {
							await ExaminationApi.createRecheckTask({
								studentId: this.studentInfo.id,
								batchId: this.batchId,
								itemId: item.itemId,
								itemName: item.itemName,
								category: item.category
							});
							uni.showToast({ title: '补检任务已创建', icon: 'success' });
							this.loadRecheckList(this.studentInfo.id);
						} catch (e) {
							uni.showToast({ title: '创建失败', icon: 'none' });
						}
					}
				}
			});
		},
		async completeRecheck(task) {
			uni.showModal({
				title: '完成补检',
				content: '确认该补检任务已完成？',
				success: async (res) => {
					if (res.confirm) {
						try {
							await ExaminationApi.completeRecheckTask(task.id, {
								studentId: this.studentInfo.id,
								batchId: this.batchId
							});
							uni.showToast({ title: '已完成补检', icon: 'success' });
							this.loadRecheckList(this.studentInfo.id);
						} catch (e) {
							uni.showToast({ title: '操作失败', icon: 'none' });
						}
					}
				}
			});
		}
	}
};
</script>

<style scoped lang="scss">
.examination-page {
	padding: 16px;
	background: rgba(244, 247, 252, 1);
	min-height: 100vh;
}
.top-bar {
	display: flex;
	align-items: center;
	justify-content: space-between;
	background: #fff;
	padding: 12px 16px;
	border-radius: 8px;
	margin-bottom: 12px;
	box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
	.batch-info {
		display: flex;
		align-items: center;
		.info-label { font-size: 14px; color: rgba(102, 102, 102, 1); }
		.info-value { font-size: 15px; font-weight: 600; color: rgba(36, 93, 209, 1); margin: 0 8px; }
		.change-btn { font-size: 13px; color: rgba(36, 93, 209, 1); text-decoration: underline; cursor: pointer; }
	}
}
.student-card {
	background: #fff;
	border-radius: 8px;
	margin-bottom: 12px;
	box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
	overflow: hidden;
	.card-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 12px 16px;
		background: rgba(36, 93, 209, 0.04);
		border-bottom: 1px solid rgba(238, 238, 238, 1);
		.header-title { font-size: 16px; font-weight: 600; color: rgba(51, 51, 51, 1); }
		.header-status { padding: 2px 10px; border-radius: 12px; font-size: 12px; }
		.status-doing { background: rgba(240, 163, 41, 0.1); color: rgba(240, 163, 41, 1); }
		.status-done { background: rgba(51, 176, 19, 0.1); color: rgba(51, 176, 19, 1); }
	}
	.card-body {
		padding: 12px 16px;
		.info-row { display: flex; gap: 24px; margin-bottom: 6px; font-size: 14px;
			.info-item { color: rgba(51, 51, 51, 1); }
		}
	}
}
.empty-tip {
	background: #fff;
	border-radius: 8px;
	padding: 60px 0;
	text-align: center;
	.empty-text { color: rgba(153, 153, 153, 1); font-size: 15px; }
}
.overview-card, .items-card, .abnormal-card, .recheck-card {
	background: #fff;
	border-radius: 8px;
	padding: 16px;
	margin-bottom: 12px;
	box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
	.card-title {
		font-size: 16px;
		font-weight: 600;
		color: rgba(51, 51, 51, 1);
		margin-bottom: 12px;
		border-left: 3px solid rgba(36, 93, 209, 1);
		padding-left: 8px;
	}
}
.overview-stats {
	display: flex;
	gap: 12px;
	.stat-block {
		flex: 1;
		text-align: center;
		padding: 16px 8px;
		border-radius: 6px;
		.stat-num { display: block; font-size: 24px; font-weight: 700; margin-bottom: 4px; }
		.stat-label { font-size: 13px; color: rgba(102, 102, 102, 1); }
	}
	.stat-total { background: rgba(36, 93, 209, 0.08); .stat-num { color: rgba(36, 93, 209, 1); } }
	.stat-done { background: rgba(51, 176, 19, 0.08); .stat-num { color: rgba(51, 176, 19, 1); } }
	.stat-pending { background: rgba(240, 163, 41, 0.08); .stat-num { color: rgba(240, 163, 41, 1); } }
	.stat-abnormal { background: rgba(223, 65, 65, 0.08); .stat-num { color: rgba(223, 65, 65, 1); } }
	.stat-rate { background: rgba(80, 104, 242, 0.08); .stat-num { color: rgba(80, 104, 242, 1); } }
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
.op-btns {
	display: flex;
	justify-content: center;
	gap: 6px;
	.op-btn { padding: 4px 10px; font-size: 12px; border-radius: 4px; cursor: pointer; }
	.op-detail { color: rgba(36, 93, 209, 1); border: 1px solid rgba(36, 93, 209, 1); }
	.op-recheck { color: rgba(240, 163, 41, 1); border: 1px solid rgba(240, 163, 41, 1); }
	.op-complete { color: rgba(51, 176, 19, 1); border: 1px solid rgba(51, 176, 19, 1); }
}
.abnormal-list {
	.abnormal-item {
		display: flex;
		justify-content: space-between;
		padding: 10px 0;
		border-bottom: 1px dashed rgba(238, 238, 238, 1);
		.abnormal-left { display: flex; gap: 12px;
			.abnormal-category { color: rgba(102, 102, 102, 1); font-size: 13px; }
			.abnormal-name { color: rgba(51, 51, 51, 1); font-size: 14px; }
		}
		.abnormal-right { display: flex; gap: 8px; align-items: center;
			.abnormal-result { color: rgba(223, 65, 65, 1); font-size: 14px; }
			.abnormal-flag { background: rgba(223, 65, 65, 1); color: #fff; padding: 1px 6px; border-radius: 3px; font-size: 11px; }
		}
	}
}
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
.detail-popup {
	width: 70vw;
	max-width: 600px;
	background: #fff;
	border-radius: 12px;
	padding: 20px;
	.popup-title { font-size: 18px; font-weight: 600; text-align: center; margin-bottom: 16px; color: rgba(36, 93, 209, 1); }
	.detail-row { display: flex; margin-bottom: 10px; font-size: 14px;
		.detail-label { width: 100px; color: rgba(102, 102, 102, 1); }
		.detail-value { flex: 1; color: rgba(51, 51, 51, 1); }
	}
}
</style>
