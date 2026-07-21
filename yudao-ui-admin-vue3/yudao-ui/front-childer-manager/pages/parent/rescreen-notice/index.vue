<template>
	<view class="notice-page">
		<view class="patient-bar">
			<text class="patient-label">就诊人：</text>
			<view class="patient-picker" @click="showPatientPicker = true">
				<text :class="currentPatient ? 'picker-value' : 'picker-placeholder'">
					{{ currentPatient ? currentPatient.name : '请选择就诊人' }}
				</text>
				<text class="iconfont icon-right"></text>
			</view>
		</view>

		<view class="notice-list" v-if="noticeList.length">
			<view class="notice-card" v-for="n in noticeList" :key="n.id" @click="toDetail(n)">
				<view class="card-top">
					<view class="card-title">{{ n.title || '复筛通知单' }}</view>
					<view class="card-status" :class="getStatusClass(n.status)">{{ formatStatus(n.status) }}</view>
				</view>
				<view class="card-section">
					<view class="section-label">阳性结果</view>
					<view class="section-value">{{ n.positiveResult || '-' }}</view>
				</view>
				<view class="card-section">
					<view class="section-label">复筛项目</view>
					<view class="section-value">{{ n.rescreenItems || '-' }}</view>
				</view>
				<view class="card-meta">
					<text class="meta-item">复筛时间：{{ n.rescreenTime || '-' }}</text>
					<text class="meta-item">复筛地点：{{ n.rescreenLocation || '-' }}</text>
				</view>
			</view>
		</view>
		<view v-else class="empty-state">
			<text class="empty-text">暂无复筛通知单</text>
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
	</view>
</template>

<script>
import * as ParentApi from '@/api/parent/index';

export default {
	data() {
		return {
			patientList: [],
			currentPatient: null,
			showPatientPicker: false,
			noticeList: []
		};
	},
	onLoad(e) {
		const cache = uni.getStorageSync('parent_current_patient');
		if (cache) this.currentPatient = cache;
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
		},
		async loadList() {
			if (!this.currentPatient) return;
			try {
				const res = await ParentApi.getRescreenNoticeList(this.currentPatient.id);
				this.noticeList = res.list || res.data || res || [];
			} catch (e) {
				this.noticeList = [];
			}
		},
		formatStatus(s) {
			const map = { 0: '待复筛', 1: '已预约', 2: '已到检', 3: '已完成', 4: '已过期' };
			return map[s] || s || '-';
		},
		getStatusClass(s) {
			const map = { 0: 'st-pending', 1: 'st-booked', 2: 'st-arrived', 3: 'st-done', 4: 'st-expired' };
			return map[s] || 'st-pending';
		},
		toDetail(n) {
			uni.navigateTo({ url: '/pages/parent/rescreen-notice/detail?id=' + n.id });
		}
	}
};
</script>

<style scoped lang="scss">
.notice-page {
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
.notice-list {
	.notice-card {
		background: #fff;
		border-radius: 10px;
		padding: 14px 16px;
		margin-bottom: 12px;
		box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
		.card-top {
			display: flex;
			justify-content: space-between;
			align-items: center;
			margin-bottom: 10px;
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
			.st-pending { background: rgba(240, 163, 41, 0.1); color: rgba(240, 163, 41, 1); }
			.st-booked { background: rgba(36, 93, 209, 0.1); color: rgba(36, 93, 209, 1); }
			.st-arrived { background: rgba(80, 104, 242, 0.1); color: rgba(80, 104, 242, 1); }
			.st-done { background: rgba(51, 176, 19, 0.1); color: rgba(51, 176, 19, 1); }
			.st-expired { background: rgba(153, 153, 153, 0.1); color: rgba(153, 153, 153, 1); }
		}
		.card-section {
			margin-bottom: 8px;
			.section-label {
				font-size: 12px;
				color: rgba(153, 153, 153, 1);
			}
			.section-value {
				font-size: 14px;
				color: rgba(51, 51, 51, 1);
				margin-top: 2px;
			}
		}
		.card-meta {
			margin-top: 8px;
			padding-top: 8px;
			border-top: 1px dashed rgba(238, 238, 238, 1);
			.meta-item {
				font-size: 13px;
				color: rgba(102, 102, 102, 1);
				margin-right: 16px;
			}
		}
	}
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
