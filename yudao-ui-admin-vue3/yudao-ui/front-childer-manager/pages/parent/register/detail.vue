<template>
	<view class="detail-page">
		<view class="detail-card" v-if="detail">
			<view class="card-title">报名详情</view>
			<view class="status-bar">
				<text class="status-label">报名状态：</text>
				<text class="status-value" :class="getStatusClass(detail.status)">{{ formatStatus(detail.status) }}</text>
			</view>
			<view class="info-block">
				<view class="info-row">
					<text class="info-label">姓名</text>
					<text class="info-value">{{ detail.name || '-' }}</text>
				</view>
				<view class="info-row">
					<text class="info-label">身份证号</text>
					<text class="info-value">{{ detail.idCard || '-' }}</text>
				</view>
				<view class="info-row">
					<text class="info-label">性别</text>
					<text class="info-value">{{ formatSex(detail.sex) }}</text>
				</view>
				<view class="info-row">
					<text class="info-label">年龄</text>
					<text class="info-value">{{ detail.age || '-' }}</text>
				</view>
				<view class="info-row">
					<text class="info-label">学校</text>
					<text class="info-value">{{ detail.schoolName || '-' }}</text>
				</view>
				<view class="info-row">
					<text class="info-label">年级</text>
					<text class="info-value">{{ detail.grade || '-' }}</text>
				</view>
				<view class="info-row">
					<text class="info-label">班级</text>
					<text class="info-value">{{ detail.className || '-' }}</text>
				</view>
				<view class="info-row">
					<text class="info-label">筛查批次</text>
					<text class="info-value">{{ detail.batchName || '-' }}</text>
				</view>
				<view class="info-row">
					<text class="info-label">家长姓名</text>
					<text class="info-value">{{ detail.parentName || '-' }}</text>
				</view>
				<view class="info-row">
					<text class="info-label">联系方式</text>
					<text class="info-value">{{ detail.phone || '-' }}</text>
				</view>
				<view class="info-row">
					<text class="info-label">提交时间</text>
					<text class="info-value">{{ detail.submitTime || '-' }}</text>
				</view>
				<view class="info-row">
					<text class="info-label">审核时间</text>
					<text class="info-value">{{ detail.auditTime || '-' }}</text>
				</view>
				<view class="info-row" v-if="detail.auditRemark">
					<text class="info-label">审核说明</text>
					<text class="info-value">{{ detail.auditRemark }}</text>
				</view>
				<view class="info-row" v-if="detail.remark">
					<text class="info-label">备注</text>
					<text class="info-value">{{ detail.remark }}</text>
				</view>
			</view>
			<view class="form-actions" v-if="detail.status === 0">
				<u-button type="error" text="取消报名" @click="handleCancel" :loading="cancelling" />
			</view>
		</view>
	</view>
</template>

<script>
import * as ParentApi from '@/api/parent/index';

export default {
	data() {
		return {
			detail: null,
			cancelling: false
		};
	},
	onLoad(e) {
		if (e.id) this.loadDetail(e.id);
	},
	methods: {
		async loadDetail(id) {
			try {
				const res = await ParentApi.getRegistrationDetail(id);
				this.detail = res.data || res;
			} catch (e) {
				uni.showToast({ title: '加载失败', icon: 'none' });
			}
		},
		formatSex(sex) {
			if (sex === 1 || sex === '1' || sex === '男') return '男';
			if (sex === 2 || sex === '2' || sex === '女') return '女';
			return sex || '-';
		},
		formatStatus(s) {
			const map = { 0: '待审核', 1: '已通过', 2: '已驳回', 3: '已取消' };
			return map[s] || s || '-';
		},
		getStatusClass(s) {
			const map = { 0: 'st-pending', 1: 'st-pass', 2: 'st-reject', 3: 'st-cancel' };
			return map[s] || 'st-pending';
		},
		handleCancel() {
			uni.showModal({
				title: '确认取消',
				content: '确定取消该报名申请吗？',
				success: async (r) => {
					if (!r.confirm) return;
					this.cancelling = true;
					try {
						await ParentApi.cancelRegistration(this.detail.id);
						uni.showToast({ title: '已取消', icon: 'success' });
						this.loadDetail(this.detail.id);
					} catch (e) {
						uni.showToast({ title: '取消失败', icon: 'none' });
					} finally {
						this.cancelling = false;
					}
				}
			});
		}
	}
};
</script>

<style scoped lang="scss">
.detail-page {
	min-height: 100vh;
	background: rgba(244, 247, 252, 1);
	padding: 16px;
}
.detail-card {
	background: #fff;
	border-radius: 10px;
	padding: 20px;
	box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
	max-width: 800px;
	margin: 0 auto;
	.card-title {
		font-size: 18px;
		font-weight: 600;
		color: rgba(36, 93, 209, 1);
		text-align: center;
		margin-bottom: 16px;
		padding-bottom: 12px;
		border-bottom: 1px solid rgba(238, 238, 238, 1);
	}
	.status-bar {
		display: flex;
		align-items: center;
		justify-content: center;
		margin-bottom: 16px;
		.status-label {
			font-size: 14px;
			color: rgba(102, 102, 102, 1);
		}
		.status-value {
			font-size: 14px;
			font-weight: 600;
			padding: 3px 12px;
			border-radius: 12px;
			margin-left: 6px;
		}
		.st-pending { background: rgba(240, 163, 41, 0.1); color: rgba(240, 163, 41, 1); }
		.st-pass { background: rgba(51, 176, 19, 0.1); color: rgba(51, 176, 19, 1); }
		.st-reject { background: rgba(223, 65, 65, 0.1); color: rgba(223, 65, 65, 1); }
		.st-cancel { background: rgba(153, 153, 153, 0.1); color: rgba(153, 153, 153, 1); }
	}
	.info-block {
		.info-row {
			display: flex;
			padding: 10px 0;
			border-bottom: 1px dashed rgba(238, 238, 238, 1);
			.info-label {
				width: 100px;
				font-size: 14px;
				color: rgba(102, 102, 102, 1);
			}
			.info-value {
				flex: 1;
				font-size: 14px;
				color: rgba(51, 51, 51, 1);
			}
		}
	}
	.form-actions {
		display: flex;
		justify-content: center;
		margin-top: 20px;
	}
}
</style>
