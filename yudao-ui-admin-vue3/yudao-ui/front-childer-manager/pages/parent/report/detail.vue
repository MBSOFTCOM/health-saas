<template>
	<view class="detail-page">
		<view class="report-summary" v-if="detail">
			<view class="summary-title">{{ detail.batchName || '体检报告' }}</view>
			<view class="summary-meta">
				<text class="meta-item">受检者：{{ detail.name || '-' }}</text>
				<text class="meta-item">性别：{{ formatSex(detail.sex) }}</text>
				<text class="meta-item">年龄：{{ detail.age || '-' }}</text>
			</view>
			<view class="summary-meta">
				<text class="meta-item">体检时间：{{ detail.examTime || '-' }}</text>
				<text class="meta-item">学校：{{ detail.schoolName || '-' }}</text>
			</view>
			<view class="summary-status" :class="detail.hasAbnormal ? 'st-abnormal' : 'st-normal'">
				{{ detail.hasAbnormal ? '存在异常项目，请关注阳性解读与护理建议' : '本次筛查结果正常' }}
			</view>
		</view>

		<!-- 筛查结果 -->
		<view class="section-card" v-if="detail && detail.results && detail.results.length">
			<view class="section-title">筛查结果</view>
			<view class="result-row" v-for="(r, idx) in detail.results" :key="idx">
				<text class="result-name">{{ r.name }}</text>
				<text class="result-value">{{ r.value }}{{ r.unit || '' }}</text>
				<text class="result-flag" :class="r.abnormal ? 'st-abnormal' : 'st-normal'">
					{{ r.abnormal ? '异常' : '正常' }}
				</text>
			</view>
		</view>

		<!-- 异常项目 -->
		<view class="section-card" v-if="detail && detail.abnormals && detail.abnormals.length">
			<view class="section-title">异常项目</view>
			<view class="abnormal-row" v-for="(a, idx) in detail.abnormals" :key="idx">
				<view class="abnormal-name">{{ a.name }}</view>
				<view class="abnormal-value">{{ a.value }}{{ a.unit || '' }}</view>
				<view class="abnormal-ref" v-if="a.reference">参考范围：{{ a.reference }}</view>
			</view>
		</view>

		<!-- 阳性解读 -->
		<view class="section-card" v-if="detail && detail.positiveExplain">
			<view class="section-title">阳性解读</view>
			<view class="section-content">{{ detail.positiveExplain }}</view>
		</view>

		<!-- 护理建议 -->
		<view class="section-card" v-if="detail && detail.nursingAdvice">
			<view class="section-title">护理建议</view>
			<view class="section-content">{{ detail.nursingAdvice }}</view>
		</view>

		<!-- 复筛建议 -->
		<view class="section-card" v-if="detail && detail.recheckAdvice">
			<view class="section-title">复筛建议</view>
			<view class="section-content">{{ detail.recheckAdvice }}</view>
		</view>

		<view v-if="!detail" class="loading-tip">加载中...</view>
	</view>
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
				const res = await ParentApi.getReportDetail(id);
				this.detail = res.data || res;
			} catch (e) {
				uni.showToast({ title: '加载失败', icon: 'none' });
			}
		},
		formatSex(sex) {
			if (sex === 1 || sex === '1' || sex === '男') return '男';
			if (sex === 2 || sex === '2' || sex === '女') return '女';
			return sex || '-';
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
.report-summary {
	background: #fff;
	border-radius: 10px;
	padding: 16px 18px;
	margin-bottom: 16px;
	box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
	.summary-title {
		font-size: 18px;
		font-weight: 600;
		color: rgba(36, 93, 209, 1);
		margin-bottom: 10px;
	}
	.summary-meta {
		margin-top: 4px;
		.meta-item {
			font-size: 13px;
			color: rgba(102, 102, 102, 1);
			margin-right: 16px;
		}
	}
	.summary-status {
		margin-top: 12px;
		padding: 10px 12px;
		border-radius: 6px;
		font-size: 14px;
	}
	.st-normal { background: rgba(51, 176, 19, 0.08); color: rgba(51, 176, 19, 1); }
	.st-abnormal { background: rgba(223, 65, 65, 0.08); color: rgba(223, 65, 65, 1); }
}
.section-card {
	background: #fff;
	border-radius: 10px;
	padding: 16px 18px;
	margin-bottom: 16px;
	box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
	.section-title {
		font-size: 16px;
		font-weight: 600;
		color: rgba(36, 93, 209, 1);
		margin-bottom: 12px;
		padding-bottom: 8px;
		border-bottom: 1px solid rgba(238, 238, 238, 1);
	}
	.section-content {
		font-size: 14px;
		color: rgba(51, 51, 51, 1);
		line-height: 1.7;
		white-space: pre-wrap;
	}
	.result-row {
		display: flex;
		align-items: center;
		padding: 10px 0;
		border-bottom: 1px dashed rgba(238, 238, 238, 1);
		&:last-child { border-bottom: none; }
		.result-name {
			flex: 1;
			font-size: 14px;
			color: rgba(102, 102, 102, 1);
		}
		.result-value {
			width: 120px;
			text-align: center;
			font-size: 14px;
			font-weight: 600;
			color: rgba(51, 51, 51, 1);
		}
		.result-flag {
			width: 60px;
			text-align: right;
			font-size: 12px;
		}
		.st-normal { color: rgba(51, 176, 19, 1); }
		.st-abnormal { color: rgba(223, 65, 65, 1); }
	}
	.abnormal-row {
		padding: 10px 0;
		border-bottom: 1px dashed rgba(238, 238, 238, 1);
		&:last-child { border-bottom: none; }
		.abnormal-name {
			font-size: 15px;
			font-weight: 600;
			color: rgba(223, 65, 65, 1);
		}
		.abnormal-value {
			font-size: 14px;
			color: rgba(51, 51, 51, 1);
			margin-top: 2px;
		}
		.abnormal-ref {
			font-size: 12px;
			color: rgba(153, 153, 153, 1);
			margin-top: 2px;
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
