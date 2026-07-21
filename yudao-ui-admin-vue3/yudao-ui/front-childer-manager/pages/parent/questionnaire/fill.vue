<template>
	<view class="fill-page">
		<view class="qn-header" v-if="detail">
			<view class="qn-title">{{ detail.title }}</view>
			<view class="qn-desc" v-if="detail.description">{{ detail.description }}</view>
			<view class="qn-progress">
				<text class="progress-text">已填 {{ answeredCount }} / {{ detail.questions.length }}</text>
				<view class="progress-bar">
					<view class="progress-inner" :style="{ width: progressPercent + '%' }"></view>
				</view>
			</view>
		</view>

		<view class="question-list" v-if="detail && detail.questions && detail.questions.length">
			<view class="question-card" v-for="(q, idx) in detail.questions" :key="idx">
				<view class="q-title">
					<text class="q-index">{{ idx + 1 }}.</text>
					<text class="q-text">{{ q.title }}</text>
					<text class="q-required" v-if="q.required">*</text>
				</view>
				<view class="q-desc" v-if="q.description">{{ q.description }}</view>

				<!-- 单选 -->
				<view v-if="q.type === 'radio'" class="q-options">
					<view
						v-for="opt in q.options"
						:key="opt.value"
						class="q-option"
						:class="{ active: answers[q.id] === opt.value }"
						@click="setAnswer(q.id, opt.value)"
					>
						<text class="opt-radio">{{ answers[q.id] === opt.value ? '●' : '○' }}</text>
						<text class="opt-text">{{ opt.label }}</text>
					</view>
				</view>

				<!-- 多选 -->
				<view v-else-if="q.type === 'checkbox'" class="q-options">
					<view
						v-for="opt in q.options"
						:key="opt.value"
						class="q-option"
						:class="{ active: (answers[q.id] || []).includes(opt.value) }"
						@click="toggleCheckbox(q.id, opt.value)"
					>
						<text class="opt-radio">{{ (answers[q.id] || []).includes(opt.value) ? '■' : '□' }}</text>
						<text class="opt-text">{{ opt.label }}</text>
					</view>
				</view>

				<!-- 文本 -->
				<view v-else-if="q.type === 'text'" class="q-options">
					<uni-easyinput v-model="answers[q.id]" placeholder="请输入" />
				</view>

				<!-- 文本域 -->
				<view v-else-if="q.type === 'textarea'" class="q-options">
					<uni-easyinput type="textarea" v-model="answers[q.id]" placeholder="请输入" />
				</view>

				<!-- 数字 -->
				<view v-else-if="q.type === 'number'" class="q-options">
					<uni-easyinput v-model="answers[q.id]" type="number" placeholder="请输入数字" />
				</view>
			</view>
		</view>

		<view class="footer-actions" v-if="detail">
			<u-button text="暂存" @click="saveProgress" :loading="saving" />
			<u-button type="primary" text="提交" @click="submit" :loading="submitting" style="margin-left: 12px" />
		</view>

		<view v-if="!detail" class="loading-tip">加载中...</view>

		<!-- 提交结果展示 -->
		<u-popup :show="showResult" mode="center" @close="showResult = false" round="12">
			<view class="result-popup">
				<view class="result-title">问卷结果</view>
				<view class="result-content" v-if="resultData">{{ resultData.analysis || '已完成' }}</view>
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
				return a !== undefined && a !== '' && a !== null && (!Array.isArray(a) || a.length > 0);
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
				const res = await ParentApi.getQuestionnaireDetail(this.id, this.patientId);
				this.detail = res.data || res;
				// 恢复已填进度
				if (this.detail.progress) {
					this.answers = { ...(this.detail.progress.answers || {}) };
				} else {
					this.detail.questions.forEach(q => {
						if (q.type === 'checkbox') this.$set(this.answers, q.id, []);
					});
				}
			} catch (e) {
				uni.showToast({ title: '加载失败', icon: 'none' });
			}
		},
		setAnswer(qid, val) {
			this.$set(this.answers, qid, val);
		},
		toggleCheckbox(qid, val) {
			const arr = this.answers[qid] || [];
			const idx = arr.indexOf(val);
			if (idx > -1) {
				arr.splice(idx, 1);
			} else {
				arr.push(val);
			}
			this.$set(this.answers, qid, [...arr]);
		},
		async saveProgress() {
			this.saving = true;
			try {
				await ParentApi.saveQuestionnaireProgress({
					questionnaireId: this.id,
					patientId: this.patientId,
					answers: this.answers
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
			// 必填校验
			for (const q of this.detail.questions) {
				if (!q.required) continue;
				const a = this.answers[q.id];
				if (a === undefined || a === '' || a === null || (Array.isArray(a) && a.length === 0)) {
					uni.showToast({ title: `请完成第${this.detail.questions.indexOf(q) + 1}题`, icon: 'none' });
					return;
				}
			}
			uni.showModal({
				title: '确认提交',
				content: '提交后将无法修改，确定要提交吗？',
				success: async (r) => {
					if (!r.confirm) return;
					this.submitting = true;
					try {
						const res = await ParentApi.submitQuestionnaire({
							questionnaireId: this.id,
							patientId: this.patientId,
							answers: this.answers
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
.qn-header {
	background: #fff;
	border-radius: 10px;
	padding: 16px 18px;
	margin-bottom: 16px;
	box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
	.qn-title { font-size: 18px; font-weight: 600; color: rgba(36, 93, 209, 1); }
	.qn-desc { font-size: 13px; color: rgba(102, 102, 102, 1); margin-top: 6px; }
	.qn-progress {
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
		.q-desc { font-size: 12px; color: rgba(153, 153, 153, 1); margin: 4px 0 8px 18px; }
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
				font-size: 14px;
				color: rgba(51, 51, 51, 1);
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
	.result-title { font-size: 18px; font-weight: 600; color: rgba(36, 93, 209, 1); text-align: center; margin-bottom: 12px; }
	.result-content { font-size: 14px; color: rgba(51, 51, 51, 1); line-height: 1.7; margin-bottom: 16px; }
	.result-footer { display: flex; justify-content: center; }
}
</style>
