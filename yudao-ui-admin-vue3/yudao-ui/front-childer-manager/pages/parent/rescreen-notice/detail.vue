<template>
	<view class="detail-page" v-if="detail">
		<view class="detail-card">
			<view class="card-title">{{ detail.title || '复筛通知单' }}</view>
			<view class="status-bar">
				<text class="status-label">复筛状态：</text>
				<text class="status-value" :class="getStatusClass(detail.status)">{{ formatStatus(detail.status) }}</text>
			</view>

			<view class="section-block">
				<view class="block-title">受检者信息</view>
				<view class="info-row">
					<text class="info-label">姓名</text>
					<text class="info-value">{{ detail.name || '-' }}</text>
				</view>
				<view class="info-row">
					<text class="info-label">性别</text>
					<text class="info-value">{{ formatSex(detail.sex) }}</text>
				</view>
				<view class="info-row">
					<text class="info-label">年龄</text>
					<text class="info-value">{{ detail.age || '-' }}</text>
				</view>
				<view class="info-row" v-if="detail.schoolName">
					<text class="info-label">学校</text>
					<text class="info-value">{{ detail.schoolName }}</text>
				</view>
				<view class="info-row" v-if="detail.batchName">
					<text class="info-label">批次</text>
					<text class="info-value">{{ detail.batchName }}</text>
				</view>
			</view>

			<view class="section-block">
				<view class="block-title">阳性结果</view>
				<view class="block-content">{{ detail.positiveResult || '无' }}</view>
			</view>

			<view class="section-block">
				<view class="block-title">复筛项目</view>
				<view class="block-content">{{ detail.rescreenItems || '无' }}</view>
			</view>

			<view class="section-block">
				<view class="block-title">复筛安排</view>
				<view class="info-row">
					<text class="info-label">复筛时间</text>
					<text class="info-value">{{ detail.rescreenTime || '-' }}</text>
				</view>
				<view class="info-row">
					<text class="info-label">复筛地点</text>
					<text class="info-value">{{ detail.rescreenLocation || '-' }}</text>
				</view>
				<view class="info-row" v-if="detail.contactPhone">
					<text class="info-label">联系电话</text>
					<text class="info-value">{{ detail.contactPhone }}</text>
				</view>
			</view>

			<view class="section-block" v-if="detail.attention">
				<view class="block-title">注意事项</view>
				<view class="block-content">{{ detail.attention }}</view>
			</view>

			<view class="section-block" v-if="detail.remark">
				<view class="block-title">备注</view>
				<view class="block-content">{{ detail.remark }}</view>
			</view>
		</view>
	</view>
	<view v-else class="loading-tip">加载中...</view>
</template>

<script>
import * as ParentApi from '@/api/parent/index';

export default {
	data() {
		return {
			detail: null
		};
	},
	onLoad(e) {
		if (e.id) this.loadDetail(e.id);
	},
	methods: {
		async loadDetail(id) {
			try {
				const res = await ParentApi.getRescreenNoticeDetail(id);
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
			const map = { 0: '待复筛', 1: '已预约', 2: '已到检', 3: '已完成', 4: '已过期' };
			return map[s] || s || '-';
		},
		getStatusClass(s) {
			const map = { 0: 'st-pending', 1: 'st-booked', 2: 'st-arrived', 3: 'st-done', 4: 'st-expired' };
			return map[s] || 'st-pending';
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
		margin-bottom: 12px;
	}
	.status-bar {
		display: flex;
		align-items: center;
		justify-content: center;
		margin-bottom: 16px;
		padding-bottom: 12px;
		border-bottom: 1px solid rgba(238, 238, 238, 1);
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
		.st-booked { background: rgba(36, 93, 209, 0.1); color: rgba(36, 93, 209, 1); }
		.st-arrived { background: rgba(80, 104, 242, 0.1); color: rgba(80, 104, 242, 1); }
		.st-done { background: rgba(51, 176, 19, 0.1); color: rgba(51, 176, 19, 1); }
		.st-expired { background: rgba(153, 153, 153, 0.1); color: rgba(153, 153, 153, 1); }
	}
	.section-block {
		margin-bottom: 16px;
		.block-title {
			font-size: 15px;
			font-weight: 600;
			color: rgba(36, 93, 209, 1);
			margin-bottom: 8px;
			padding-left: 8px;
			border-left: 3px solid rgba(36, 93, 209, 1);
		}
		.block-content {
			font-size: 14px;
			color: rgba(51, 51, 51, 1);
			line-height: 1.7;
			white-space: pre-wrap;
			padding: 8px 10px;
			background: rgba(244, 247, 252, 1);
			border-radius: 6px;
		}
		.info-row {
			display: flex;
			padding: 8px 0;
			.info-label {
				width: 90px;
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
}
.loading-tip {
	text-align: center;
	font-size: 14px;
	color: rgba(153, 153, 153, 1);
	padding: 50px 0;
}
</style>
