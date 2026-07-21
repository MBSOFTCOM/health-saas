<template>
	<view class="fill-page">
		<view class="scale-header" v-if="detail">
			<view class="scale-title">{{ detail.title }}</view>
			<view class="scale-desc" v-if="detail.description">{{ detail.description }}</view>
			<view class="scale-progress">
				<text class="progress-text">已填 {{ answeredCount }} / {{ detail.questions.length }}</text>
				<view class="progress-bar">
					<view class="progress-inner" :style="{ width: progressPercent + '%' }"></view>
				</view>
			</view>
			<view class="scale-instruction" v-if="detail.instruction">
				<text class="instruction-label">填写说明：</text>
				<text class="instruction-text">{{ detail.instruction }}</text>
			</view>
		</view>

		<view class="question-list" v-if="detail && detail.questions && detail.questions.length">
			<view class="question-card" v-for="(q, idx) in detail.questions" :key="idx">
				<view class="q-title">
					<text class="q-index">{{ idx + 1 }}.</text>
					<text class="q-text">{{ q.title }}</text>
					<text class="q-required" v-if="q.required">*</text>
				</view>

				<!-- 量表选项（带分值） -->
				<view class="q-options" v-if="q.options && q.options.length">
					<view
						v-for="opt in q.options"
						:key="opt.value"
						class="q-option"
						:class="{ active: answers[q.id] === opt.value }"
						@click="setAnswer(q.id, opt.value, opt.score)"
					>
						<text class="opt-radio">{{ answers[q.id] === opt.value ? '●' : '○' }}</text>
						<text class="opt-text">{{ opt.label }}</text>
						<text class="opt-score" v-if="opt.score !== undefined">{{ opt.score }}分</text>
					</view>
				</view>

				<!-- 文本类题目 -->
				<view v-else-if="q.type === 'text'" class="q-options">
					<uni-easyinput v-model="answers[q.id]" placeholder="请输入" />
				</view>
				<view v-else-if="q.type === 'textarea'" class="q-options">
					<uni-easyinput type="textarea" v-model="answers[q.id]" placeholder="请输入" />
				</view>
			</view>
		</view>

		<view class="footer-actions" v-if="detail">
			<u-button text="暂存" @click="saveProgress" :loading="saving" />
			<u-button type="primary" text="提交并查看结果" @click="submit" :loading="submitting" style="margin-left: 12px" />
		</view>

		<view v-if="!detail" class="loading-tip">加载中...</view>

		<!-- 量表结果（自动计算结果与风险等级） -->
		<u-popup :show="showResult" mode="center" @close="showResult = false" round="12">
			<view class="result-popup" v-if="resultData">
				<view class="result-title">量表评估结果</view>
				<view class="result-row">
					<text class="result-label">总得分</text>
					<text class="result-value score">{{ resultData.totalScore }} 分</text>
				</view>
				<view class="result-row">
					<text class="result-label">风险等级</text>
					<text class="result-value" :class="getRiskClass(resultData.riskLevel)">{{ resultData.riskLevelText || resultData.riskLevel }}</text>
				</view>
				<view class="result-row" v-if="resultData.interpretation">
					<text class="result-label">结果解读</text>
					<text class="result-content">{{ resultData.interpretation }}</text>
				</view>
				<view class="result-row" v-if="resultData.suggestion">
					<text class="result-label">建议</text>
					<text class="result-content">{{ resultData.suggestion }}</text>
				</view>
				<view class="result-footer">
					<u-button type="primary" text="确定" @click="closeResult" />
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
			id: '',
			patientId: '',
			detail: null,
			answers: {},
			scores: {},
			saving: false,
			submitting: false,
			showResult: false,
			resultData: null
		};
	},
	computed: {
		answeredCount() {
			if (!this.detail || !this.detail.questions) return 0;
			return this.detail.questions.filter(q => {
				const a = this.answers[q.id];
				return a !== undefined && a !== '' && a !== null;
			}).length;
		},
		progressPercent() {
			if (!this.detail || !this.detail.questions || !this.detail.questions.length) return 0;
			return Math.round((this.answeredCount / this.detail.questions.length) * 100);
		}
	},
	onLoad(e) {
		this.id = e.id;
		this.patientId = e.patientId;
		if (this.id && this.patientId) this.loadDetail();
	},
	methods: {
		async loadDetail() {
			try {
				const res = await ParentApi.getScaleDetail(this.id, this.patientId);
				this.detail = res.data || res;
				if (this.detail.progress) {
					this.answers = { ...(this.detail.progress.answers || {}) };
					this.scores = { ...(this.detail.progress.scores || {}) };
				}
			} catch (e) {
				uni.showToast({ title: '加载失败', icon: 'none' });
			}
		},
		setAnswer(qid, val, score) {
			this.$set(this.answers, qid, val);
			if (score !== undefined) {
				this.$set(this.scores, qid, score);
			}
		},
		async saveProgress() {
			this.saving = true;
			try {
				await ParentApi.saveScaleProgress({
					scaleId: this.id,
					patientId: this.patientId,
					answers: this.answers,
					scores: this.scores
				});
				uni.showToast({ title: '已暂存', icon: 'success' });
			} catch (e) {
				uni.showToast({ title: '暂存失败', icon: 'none' });
			} finally {
				this.saving = false;
			}
		},
		submit() {
			if (!this.detail) return;
			for (const q of this.detail.questions) {
				if (!q.required) continue;
				const a = this.answers[q.id];
				if (a === undefined || a === '' || a === null) {
					uni.showToast({ title: `请完成第${this.detail.questions.indexOf(q) + 1}题`, icon: 'none' });
					return;
				}
			}
			uni.showModal({
				title: '确认提交',
				content: '提交后量表结果将自动计算，并关联心理筛查与阳性结果分析，确定提交吗？',
				success: async (r) => {
					if (!r.confirm) return;
					this.submitting = true;
					try {
						const res = await ParentApi.submitScale({
							scaleId: this.id,
							patientId: this.patientId,
							answers: this.answers,
							scores: this.scores
						});
						this.resultData = res.data || res;
						this.showResult = true;
					} catch (e) {
						uni.showToast({ title: '提交失败', icon: 'none' });
					} finally {
						this.submitting = false;
					}
				}
			});
		},
		getRiskClass(level) {
			if (!level) return '';
			const lv = String(level).toLowerCase();
			if (lv.includes('高') || lv.includes('high')) return 'risk-high';
			if (lv.includes('中') || lv.includes('mid')) return 'risk-mid';
			if (lv.includes('低') || lv.includes('low')) return 'risk-low';
			return '';
		},
		closeResult() {
			this.showResult = false;
			uni.navigateBack();
		}
	}
};
</script>

<style scoped lang="scss">
.fill-page {
	min-height: 100vh;
	background: rgba(244, 247, 252, 1);
	padding: 16px;
	padding-bottom: 80px;
}
.scale-header {
	background: #fff;
	border-radius: 10px;
	padding: 16px 18px;
	margin-bottom: 16px;
	box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
	.scale-title { font-size: 18px; font-weight: 600; color: rgba(36, 93, 209, 1); }
	.scale-desc { font-size: 13px; color: rgba(102, 102, 102, 1); margin-top: 6px; }
	.scale-progress {
		margin-top: 12px;
		.progress-text { font-size: 12px; color: rgba(153, 153, 153, 1); }
		.progress-bar {
			margin-top: 4px;
			height: 6px;
			background: rgba(238, 238, 238, 1);
			border-radius: 3px;
			overflow: hidden;
			.progress-inner {
				height: 100%;
				background: linear-gradient(90deg, rgba(36, 93, 209, 1), rgba(80, 104, 242, 1));
				transition: width 0.3s;
			}
		}
	}
	.scale-instruction {
		margin-top: 10px;
		padding: 10px;
		background: rgba(240, 163, 41, 0.06);
		border-radius: 6px;
		font-size: 12px;
		.instruction-label { color: rgba(240, 163, 41, 1); font-weight: 600; }
		.instruction-text { color: rgba(102, 102, 102, 1); }
	}
}
.question-list {
	.question-card {
		background: #fff;
		border-radius: 10px;
		padding: 14px 16px;
		margin-bottom: 12px;
		box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
		.q-title {
			display: flex;
			align-items: flex-start;
			.q-index { font-size: 14px; color: rgba(36, 93, 209, 1); margin-right: 4px; font-weight: 600; }
			.q-text { flex: 1; font-size: 15px; font-weight: 600; color: rgba(51, 51, 51, 1); }
			.q-required { color: rgba(223, 65, 65, 1); margin-left: 4px; }
		}
		.q-options { margin-top: 10px; }
		.q-option {
			display: flex;
			align-items: center;
			padding: 8px 10px;
			margin-bottom: 6px;
			background: rgba(244, 247, 252, 1);
			border-radius: 6px;
			&.active { background: rgba(36, 93, 209, 0.08); }
			.opt-radio {
				font-size: 16px;
				color: rgba(36, 93, 209, 1);
				margin-right: 8px;
			}
			.opt-text {
				flex: 1;
				font-size: 14px;
				color: rgba(51, 51, 51, 1);
			}
			.opt-score {
				font-size: 12px;
				color: rgba(153, 153, 153, 1);
			}
		}
	}
}
.footer-actions {
	position: fixed;
	bottom: 0;
	left: 0;
	right: 0;
	display: flex;
	justify-content: center;
	padding: 12px 16px;
	background: #fff;
	box-shadow: 0 -2px 8px rgba(36, 93, 209, 0.06);
	z-index: 10;
}
.loading-tip { text-align: center; font-size: 14px; color: rgba(153, 153, 153, 1); padding: 50px 0; }
.result-popup {
	background: #fff;
	border-radius: 12px;
	padding: 20px;
	width: 80vw;
	max-width: 500px;
	.result-title { font-size: 18px; font-weight: 600; color: rgba(36, 93, 209, 1); text-align: center; margin-bottom: 16px; }
	.result-row {
		display: flex;
		align-items: flex-start;
		padding: 8px 0;
		border-bottom: 1px dashed rgba(238, 238, 238, 1);
		.result-label { width: 80px; font-size: 14px; color: rgba(102, 102, 102, 1); }
		.result-value { flex: 1; font-size: 14px; color: rgba(51, 51, 51, 1); }
		.result-value.score { font-size: 18px; font-weight: 600; color: rgba(36, 93, 209, 1); }
		.risk-high { color: rgba(223, 65, 65, 1); font-weight: 600; }
		.risk-mid { color: rgba(240, 163, 41, 1); font-weight: 600; }
		.risk-low { color: rgba(51, 176, 19, 1); font-weight: 600; }
		.result-content { flex: 1; font-size: 14px; color: rgba(51, 51, 51, 1); line-height: 1.7; white-space: pre-wrap; }
	}
	.result-footer { display: flex; justify-content: center; margin-top: 16px; }
}
</style>
