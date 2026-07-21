<template>
	<view class="report-page">
		<view class="patient-bar">
			<text class="patient-label">就诊人：</text>
			<view class="patient-picker" @click="showPatientPicker = true">
				<text :class="currentPatient ? 'picker-value' : 'picker-placeholder'">
					{{ currentPatient ? currentPatient.name : '请选择就诊人' }}
				</text>
				<text class="iconfont icon-right"></text>
			</view>
		</view>

		<view class="tabs">
			<view class="tab-item" :class="{ active: tab === 'list' }" @click="tab = 'list'">体检报告</view>
			<view class="tab-item" :class="{ active: tab === 'trend' }" @click="switchTrend">趋势变化</view>
		</view>

		<!-- 报告列表 -->
		<view v-if="tab === 'list'">
			<view class="report-list" v-if="reportList.length">
				<view class="report-card" v-for="r in reportList" :key="r.id" @click="toDetail(r)">
					<view class="card-top">
						<view class="card-title">{{ r.batchName || r.reportName || '体检报告' }}</view>
						<view class="card-status" :class="r.hasAbnormal ? 'st-abnormal' : 'st-normal'">
							{{ r.hasAbnormal ? '存在异常' : '正常' }}
						</view>
					</view>
					<view class="card-meta">
						<text class="meta-item">体检时间：{{ r.examTime || '-' }}</text>
					</view>
					<view class="card-meta" v-if="r.schoolName">
						<text class="meta-item">学校：{{ r.schoolName }}</text>
					</view>
					<view class="card-arrow">
						<text class="iconfont icon-right"></text>
					</view>
				</view>
			</view>
			<view v-else class="empty-state">
				<text class="empty-text">暂无体检报告</text>
			</view>
		</view>

		<!-- 趋势 -->
		<view v-else class="trend-section">
			<view class="trend-filter">
				<text class="filter-label">项目：</text>
				<view class="picker-row" style="flex: 1" @click="showItemPicker = true">
					<text :class="currentItem ? 'picker-value' : 'picker-placeholder'">
						{{ currentItem ? currentItem.name : '请选择项目' }}
					</text>
					<text class="iconfont icon-right"></text>
				</view>
			</view>
			<view class="trend-list" v-if="trendList.length">
				<view class="trend-row" v-for="(t, idx) in trendList" :key="idx">
					<text class="trend-time">{{ t.time }}</text>
					<text class="trend-value">{{ t.value }}{{ t.unit || '' }}</text>
					<text class="trend-flag" :class="t.abnormal ? 'st-abnormal' : 'st-normal'">
						{{ t.abnormal ? '异常' : '正常' }}
					</text>
				</view>
			</view>
			<view v-else class="empty-state">
				<text class="empty-text">暂无趋势数据</text>
			</view>
		</view>

		<!-- 就诊人选择 -->
		<u-popup :show="showPatientPicker" mode="bottom" @close="showPatientPicker = false" round="12">
			<view class="popup-wrap">
				<view class="popup-title">选择就诊人</view>
				<view class="popup-list">
					<view
						v-for="p in patientList"
						:key="p.id"
						class="popup-item"
						:class="{ active: currentPatient && currentPatient.id === p.id }"
						@click="selectPatient(p)"
					>
						<view class="popup-item-l">
							<text class="p-name">{{ p.name }}</text>
							<text class="p-meta">{{ formatSex(p.sex) }} · {{ p.age }}岁</text>
						</view>
						<text v-if="currentPatient && currentPatient.id === p.id" class="check">✓</text>
					</view>
					<view v-if="!patientList.length" class="empty-text">请先绑定就诊人</view>
				</view>
			</view>
		</u-popup>

		<!-- 项目选择 -->
		<u-popup :show="showItemPicker" mode="bottom" @close="showItemPicker = false" round="12">
			<view class="popup-wrap">
				<view class="popup-title">选择项目</view>
				<view class="popup-list">
					<view
						v-for="i in itemList"
						:key="i.code"
						class="popup-item"
						@click="selectItem(i)"
					>
						<text class="p-name">{{ i.name }}</text>
					</view>
					<view v-if="!itemList.length" class="empty-text">暂无可选项目</view>
				</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
import * as ParentApi from '@/api/parent/index';

export default {
	data() {
		return {
			tab: 'list',
			patientList: [],
			currentPatient: null,
			showPatientPicker: false,
			reportList: [],
			trendList: [],
			itemList: [],
			currentItem: null,
			showItemPicker: false
		};
	},
	onLoad(e) {
		const cache = uni.getStorageSync('parent_current_patient');
		if (cache) {
			this.currentPatient = cache;
		}
		if (e.patientId) {
			// 通过参数指定就诊人
		}
		this.loadPatients();
	},
	onShow() {
		if (uni.$patientListRefresh) {
			uni.$patientListRefresh = false;
			this.loadPatients();
		} else if (this.currentPatient) {
			this.loadList();
		}
	},
	methods: {
		formatSex(sex) {
			if (sex === 1 || sex === '1' || sex === '男') return '男';
			if (sex === 2 || sex === '2' || sex === '女') return '女';
			return sex || '-';
		},
		async loadPatients() {
			try {
				const res = await ParentApi.getBoundPatientList();
				this.patientList = res.list || res.data || res || [];
				if (!this.currentPatient && this.patientList.length) {
					this.currentPatient = this.patientList[0];
				}
				if (this.currentPatient) this.loadList();
			} catch (e) {
				this.patientList = [];
			}
		},
		selectPatient(p) {
			this.currentPatient = p;
			uni.setStorageSync('parent_current_patient', p);
			this.showPatientPicker = false;
			this.loadList();
			if (this.tab === 'trend') this.loadTrend();
		},
		async loadList() {
			if (!this.currentPatient) return;
			try {
				const res = await ParentApi.getReportList(this.currentPatient.id);
				this.reportList = res.list || res.data || res || [];
			} catch (e) {
				this.reportList = [];
			}
		},
		switchTrend() {
			this.tab = 'trend';
			if (!this.itemList.length) this.loadItemList();
			if (this.currentItem) this.loadTrend();
		},
		async loadItemList() {
			if (!this.currentPatient) return;
			try {
				const res = await ParentApi.getReportTrend(this.currentPatient.id, { type: 'items' });
				this.itemList = res.items || res.list || res.data || res || [];
			} catch (e) {
				this.itemList = [];
			}
		},
		selectItem(i) {
			this.currentItem = i;
			this.showItemPicker = false;
			this.loadTrend();
		},
		async loadTrend() {
			if (!this.currentPatient || !this.currentItem) return;
			try {
				const res = await ParentApi.getReportTrend(this.currentPatient.id, {
					itemCode: this.currentItem.code
				});
				this.trendList = res.list || res.data || res.trend || [];
			} catch (e) {
				this.trendList = [];
			}
		},
		toDetail(r) {
			uni.navigateTo({ url: '/pages/parent/report/detail?id=' + r.id });
		}
	}
};
</script>

<style scoped lang="scss">
.report-page {
	min-height: 100vh;
	background: rgba(244, 247, 252, 1);
	padding: 16px;
}
.patient-bar {
	display: flex;
	align-items: center;
	background: #fff;
	padding: 14px 16px;
	border-radius: 10px;
	margin-bottom: 16px;
	box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
	.patient-label {
		font-size: 14px;
		color: rgba(102, 102, 102, 1);
	}
	.patient-picker {
		flex: 1;
		.picker-value {
			font-size: 16px;
			font-weight: 600;
			color: rgba(36, 93, 209, 1);
		}
		.picker-placeholder {
			font-size: 14px;
			color: rgba(153, 153, 153, 1);
		}
	}
}
.tabs {
	display: flex;
	background: #fff;
	border-radius: 10px;
	margin-bottom: 16px;
	overflow: hidden;
	box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
	.tab-item {
		flex: 1;
		text-align: center;
		padding: 14px 0;
		font-size: 15px;
		color: rgba(102, 102, 102, 1);
		&.active {
			color: rgba(36, 93, 209, 1);
			font-weight: 600;
			border-bottom: 2px solid rgba(36, 93, 209, 1);
		}
	}
}
.report-list {
	.report-card {
		background: #fff;
		border-radius: 10px;
		padding: 14px 16px;
		margin-bottom: 12px;
		box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
		position: relative;
		.card-top {
			display: flex;
			justify-content: space-between;
			align-items: center;
			margin-bottom: 8px;
			.card-title {
				font-size: 16px;
				font-weight: 600;
				color: rgba(51, 51, 51, 1);
			}
			.card-status {
				font-size: 12px;
				padding: 2px 8px;
				border-radius: 10px;
			}
			.st-normal { background: rgba(51, 176, 19, 0.1); color: rgba(51, 176, 19, 1); }
			.st-abnormal { background: rgba(223, 65, 65, 0.1); color: rgba(223, 65, 65, 1); }
		}
		.card-meta {
			margin-top: 4px;
			.meta-item {
				font-size: 13px;
				color: rgba(102, 102, 102, 1);
				margin-right: 12px;
			}
		}
		.card-arrow {
			position: absolute;
			right: 12px;
			top: 50%;
			transform: translateY(-50%);
			color: rgba(153, 153, 153, 1);
		}
	}
}
.trend-section {
	.trend-filter {
		display: flex;
		align-items: center;
		background: #fff;
		padding: 12px 16px;
		border-radius: 10px;
		margin-bottom: 16px;
		box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
		.filter-label {
			font-size: 14px;
			color: rgba(102, 102, 102, 1);
			margin-right: 8px;
		}
	}
	.trend-list {
		background: #fff;
		border-radius: 10px;
		padding: 12px 16px;
		box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
		.trend-row {
			display: flex;
			align-items: center;
			padding: 12px 0;
			border-bottom: 1px dashed rgba(238, 238, 238, 1);
			&:last-child { border-bottom: none; }
			.trend-time {
				flex: 1;
				font-size: 14px;
				color: rgba(102, 102, 102, 1);
			}
			.trend-value {
				width: 120px;
				text-align: center;
				font-size: 15px;
				font-weight: 600;
				color: rgba(51, 51, 51, 1);
			}
			.trend-flag {
				width: 60px;
				text-align: right;
				font-size: 12px;
				padding: 2px 8px;
				border-radius: 10px;
			}
			.st-normal { color: rgba(51, 176, 19, 1); }
			.st-abnormal { color: rgba(223, 65, 65, 1); }
		}
	}
}
.picker-row {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 0 10px;
	height: 36px;
	background: rgba(244, 247, 252, 1);
	border-radius: 4px;
	.picker-value { font-size: 14px; color: rgba(51, 51, 51, 1); }
	.picker-placeholder { font-size: 14px; color: rgba(153, 153, 153, 1); }
}
.empty-state {
	background: #fff;
	border-radius: 10px;
	padding: 50px 20px;
	text-align: center;
	.empty-text {
		font-size: 14px;
		color: rgba(153, 153, 153, 1);
	}
}
.popup-wrap {
	background: #fff;
	border-radius: 12px;
	padding: 16px;
	max-height: 70vh;
	.popup-title {
		font-size: 16px;
		font-weight: 600;
		text-align: center;
		margin-bottom: 12px;
		color: rgba(36, 93, 209, 1);
	}
	.popup-list {
		max-height: 50vh;
		overflow-y: auto;
	}
	.popup-item {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 12px;
		border-radius: 8px;
		margin-bottom: 8px;
		background: rgba(244, 247, 252, 1);
		&.active { background: rgba(36, 93, 209, 0.08); }
		.popup-item-l {
			display: flex;
			flex-direction: column;
			.p-name { font-size: 15px; font-weight: 600; color: rgba(51, 51, 51, 1); }
			.p-meta { font-size: 12px; color: rgba(153, 153, 153, 1); margin-top: 2px; }
		}
		.check { color: rgba(51, 176, 19, 1); font-size: 18px; }
	}
	.empty-text {
		text-align: center;
		font-size: 14px;
		color: rgba(153, 153, 153, 1);
		padding: 30px 0;
	}
}
</style>
