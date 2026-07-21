<template>
	<view class="scale-page">
		<view class="patient-bar">
			<text class="patient-label">就诊人：</text>
			<view class="patient-picker" @click="showPatientPicker = true">
				<text :class="currentPatient ? 'picker-value' : 'picker-placeholder'">
					{{ currentPatient ? currentPatient.name : '请选择就诊人' }}
				</text>
				<text class="iconfont icon-right"></text>
			</view>
		</view>

		<view class="scale-list" v-if="scaleList.length">
			<view class="scale-card" v-for="s in scaleList" :key="s.id" @click="toFill(s)">
				<view class="card-top">
					<view class="card-title">{{ s.title }}</view>
					<view class="card-status" :class="getStatusClass(s.status)">{{ formatStatus(s.status) }}</view>
				</view>
				<view class="card-desc" v-if="s.description">{{ s.description }}</view>
				<view class="card-meta">
					<text class="meta-item" v-if="s.category">类型：{{ s.category }}</text>
					<text class="meta-item" v-if="s.questionCount">题量：{{ s.questionCount }}</text>
				</view>
			</view>
		</view>
		<view v-else class="empty-state">
			<text class="empty-text">暂无可填写的量表</text>
		</view>

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
			scaleList: []
		};
	},
	onLoad() {
		const cache = uni.getStorageSync('parent_current_patient');
		if (cache) this.currentPatient = cache;
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
				const res = await ParentApi.getScaleList(this.currentPatient.id);
				this.scaleList = res.list || res.data || res || [];
			} catch (e) {
				this.scaleList = [];
			}
		},
		formatStatus(s) {
			const map = { 0: '未填写', 1: '填写中', 2: '已提交', 3: '已过期' };
			return map[s] || s || '-';
		},
		getStatusClass(s) {
			const map = { 0: 'st-todo', 1: 'st-progress', 2: 'st-done', 3: 'st-expired' };
			return map[s] || 'st-todo';
		},
		toFill(s) {
			uni.navigateTo({
				url: `/pages/parent/scale/fill?id=${s.id}&patientId=${this.currentPatient.id}`
			});
		}
	}
};
</script>

<style scoped lang="scss">
.scale-page {
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
	.patient-label { font-size: 14px; color: rgba(102, 102, 102, 1); }
	.patient-picker {
		flex: 1;
		.picker-value { font-size: 16px; font-weight: 600; color: rgba(36, 93, 209, 1); }
		.picker-placeholder { font-size: 14px; color: rgba(153, 153, 153, 1); }
	}
}
.scale-list {
	.scale-card {
		background: #fff;
		border-radius: 10px;
		padding: 14px 16px;
		margin-bottom: 12px;
		box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
		.card-top {
			display: flex;
			justify-content: space-between;
			align-items: center;
			margin-bottom: 6px;
			.card-title { font-size: 16px; font-weight: 600; color: rgba(51, 51, 51, 1); }
			.card-status { font-size: 12px; padding: 2px 8px; border-radius: 10px; }
			.st-todo { background: rgba(240, 163, 41, 0.1); color: rgba(240, 163, 41, 1); }
			.st-progress { background: rgba(36, 93, 209, 0.1); color: rgba(36, 93, 209, 1); }
			.st-done { background: rgba(51, 176, 19, 0.1); color: rgba(51, 176, 19, 1); }
			.st-expired { background: rgba(153, 153, 153, 0.1); color: rgba(153, 153, 153, 1); }
		}
		.card-desc { font-size: 13px; color: rgba(102, 102, 102, 1); margin-bottom: 6px; }
		.card-meta {
			margin-top: 4px;
			.meta-item { font-size: 12px; color: rgba(153, 153, 153, 1); margin-right: 12px; }
		}
	}
}
.empty-state {
	background: #fff;
	border-radius: 10px;
	padding: 50px 20px;
	text-align: center;
	.empty-text { font-size: 14px; color: rgba(153, 153, 153, 1); }
}
.popup-wrap {
	background: #fff;
	border-radius: 12px;
	padding: 16px;
	max-height: 70vh;
	.popup-title { font-size: 16px; font-weight: 600; text-align: center; margin-bottom: 12px; color: rgba(36, 93, 209, 1); }
	.popup-list { max-height: 50vh; overflow-y: auto; }
	.popup-item {
		display: flex; align-items: center; justify-content: space-between;
		padding: 12px; border-radius: 8px; margin-bottom: 8px; background: rgba(244, 247, 252, 1);
		&.active { background: rgba(36, 93, 209, 0.08); }
		.popup-item-l { display: flex; flex-direction: column; }
		.p-name { font-size: 15px; font-weight: 600; color: rgba(51, 51, 51, 1); }
		.p-meta { font-size: 12px; color: rgba(153, 153, 153, 1); margin-top: 2px; }
		.check { color: rgba(51, 176, 19, 1); font-size: 18px; }
	}
	.empty-text { text-align: center; font-size: 14px; color: rgba(153, 153, 153, 1); padding: 30px 0; }
}
</style>
