<template>
	<view class="rescreen-register-page">
		<!-- 顶部 -->
		<view class="top-bar">
			<view class="batch-info">
				<text class="info-label">当前批次：</text>
				<text class="info-value">{{ batchName || '未选择' }}</text>
				<text class="change-btn" @click="showBatchPicker = true">切换</text>
			</view>
			<u-button type="primary" icon="scan" text="扫码复筛登记" @click="scanQrcode" :loading="scanning" />
		</view>

		<!-- 受检者信息 -->
		<view class="student-card" v-if="studentInfo">
			<view class="card-header">
				<text class="header-title">受检者信息</text>
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
			<text class="empty-text">请扫描受检者二维码进入复筛登记</text>
		</view>

		<!-- 关联的初筛阳性项目 -->
		<view class="related-card" v-if="studentInfo">
			<view class="card-title">关联初筛阳性项目</view>
			<view v-if="positiveItems.length === 0" class="empty-tip">无关联的阳性项目</view>
			<view v-else class="related-list">
				<view
					v-for="(item, idx) in positiveItems"
					:key="idx"
					class="related-item"
					:class="{ active: currentItem && currentItem.itemId === item.itemId }"
					@click="selectItem(item)"
				>
					<view class="related-header">
						<text class="related-category">{{ item.category }}</text>
						<text class="related-name">{{ item.itemName }}</text>
					</view>
					<view class="related-body">
						<text class="related-initial">初筛：{{ item.initialResult }}</text>
						<text class="related-status" :class="getRescreenStatusClass(item.rescreenStatus)">{{ getRescreenStatusText(item.rescreenStatus) }}</text>
					</view>
				</view>
			</view>
		</view>

		<!-- 复筛结果录入 -->
		<view class="form-card" v-if="currentItem">
			<view class="card-title">复筛结果登记 - {{ currentItem.itemName }}</view>
			<uni-forms ref="rescreenForm" :model="formData" label-width="120" label-position="left">
				<uni-forms-item label="复筛结果" name="result" required>
					<uni-easyinput v-model="formData.result" placeholder="请输入复筛结果" />
				</uni-forms-item>
				<uni-forms-item label="结果数值" name="value">
					<uni-easyinput v-model="formData.value" type="digit" placeholder="如需要请填写数值" />
				</uni-forms-item>
				<uni-forms-item label="结果单位" name="unit">
					<uni-easyinput v-model="formData.unit" placeholder="如 mm、度、kg" />
				</uni-forms-item>
				<uni-forms-item label="复筛结论" name="conclusion" required>
					<uni-data-select v-model="formData.conclusion" :localdata="conclusionOptions" placeholder="请选择复筛结论" />
				</uni-forms-item>
				<uni-forms-item label="是否转诊" name="needReferral">
					<uni-data-select v-model="formData.needReferral" :localdata="referralOptions" placeholder="请选择" />
				</uni-forms-item>
				<uni-forms-item label="转诊医院" name="referralHospital" v-if="formData.needReferral === true || formData.needReferral === 'true'">
					<uni-easyinput v-model="formData.referralHospital" placeholder="请输入转诊医院" />
				</uni-forms-item>
				<uni-forms-item label="复筛医生" name="doctor" required>
					<uni-easyinput v-model="formData.doctor" placeholder="请输入复筛医生姓名" />
				</uni-forms-item>
				<uni-forms-item label="复筛时间" name="screenTime" required>
					<uni-datetime-picker type="datetime" v-model="formData.screenTime" placeholder="请选择复筛时间" />
				</uni-forms-item>
				<uni-forms-item label="备注" name="remark">
					<uni-easyinput type="textarea" v-model="formData.remark" placeholder="其他说明" />
				</uni-forms-item>
			</uni-forms>

			<view class="form-actions">
				<u-button text="重置" @click="resetForm" />
				<u-button type="warning" text="同步上传" @click="syncUpload" :loading="syncing" style="margin-left: 10px" />
				<u-button type="primary" text="保存结果" @click="saveResult" :loading="saving" style="margin-left: 10px" />
				<u-button type="success" text="保存并归档" @click="saveAndArchive" :loading="archiving" style="margin-left: 10px" />
			</view>
		</view>

		<!-- 已录入的复筛结果列表 -->
		<view class="saved-list-card" v-if="studentInfo && savedResults.length">
			<view class="card-title">已录入的复筛结果</view>
			<uni-table stripe emptyText="暂无已录入结果">
				<uni-tr>
					<uni-th width="60" align="center">序号</uni-th>
					<uni-th width="150" align="center">项目</uni-th>
					<uni-th width="120" align="center">复筛结果</uni-th>
					<uni-th width="120" align="center">结论</uni-th>
					<uni-th width="120" align="center">复筛医生</uni-th>
					<uni-th width="150" align="center">复筛时间</uni-th>
					<uni-th width="100" align="center">归档状态</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in savedResults" :key="index">
					<uni-td align="center">{{ index + 1 }}</uni-td>
					<uni-td align="center">{{ item.itemName }}</uni-td>
					<uni-td align="center">{{ item.result }}</uni-td>
					<uni-td align="center">{{ getConclusionText(item.conclusion) }}</uni-td>
					<uni-td align="center">{{ item.doctor }}</uni-td>
					<uni-td align="center">{{ item.screenTime }}</uni-td>
					<uni-td align="center">
						<text class="status-tag" :class="item.archived ? 'tag-done' : 'tag-pending'">{{ item.archived ? '已归档' : '待归档' }}</text>
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
	</view>
</template>

<script>
import * as RescreenApi from '@/api/screen/rescreen';
import * as BatchApi from '@/api/screen/batch';

export default {
	data() {
		return {
			batchId: '',
			batchName: '',
			batchOptions: [],
			showBatchPicker: false,
			studentInfo: null,
			qrcode: '',
			scanning: false,
			positiveItems: [],
			currentItem: null,
			saving: false,
			syncing: false,
			archiving: false,
			savedResults: [],
			formData: {
				result: '',
				value: '',
				unit: '',
				conclusion: '',
				needReferral: '',
				referralHospital: '',
				doctor: '',
				screenTime: '',
				remark: ''
			},
			conclusionOptions: [
				{ value: 'normal', text: '正常' },
				{ value: 'mild', text: '轻度异常' },
				{ value: 'moderate', text: '中度异常' },
				{ value: 'severe', text: '重度异常' },
				{ value: 'confirmed', text: '确诊' },
				{ value: 'excluded', text: '排除' }
			],
			referralOptions: [
				{ value: true, text: '需要转诊' },
				{ value: false, text: '无需转诊' }
			]
		};
	},
	onLoad(e) {
		this.batchId = e.batchId || '';
		this.batchName = e.batchName ? decodeURIComponent(e.batchName) : '';
		this.qrcode = e.qrcode || '';
		this.loadBatchOptions().then(() => {
			if (this.qrcode) {
				this.handleScanResult(this.qrcode);
			}
		});
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
			this.qrcode = qrcode;
			try {
				const res = await RescreenApi.getRescreenRegisterByQrcode(qrcode, this.batchId);
				this.studentInfo = res.student || res;
				this.studentInfo.qrcode = qrcode;
				this.positiveItems = res.positiveItems || res.items || [];
				this.savedResults = res.savedResults || [];
				if (this.positiveItems.length) {
					this.selectItem(this.positiveItems[0]);
				}
				uni.showToast({ title: '登记就绪', icon: 'success' });
			} catch (e) {
				uni.showToast({ title: '二维码无效或未关联复筛项目', icon: 'none' });
			}
		},
		selectItem(item) {
			this.currentItem = item;
			this.resetForm();
		},
		resetForm() {
			this.formData = {
				result: '',
				value: '',
				unit: '',
				conclusion: '',
				needReferral: '',
				referralHospital: '',
				doctor: '',
				screenTime: '',
				remark: ''
			};
		},
		formatSex(sex) {
			if (sex === 1 || sex === '1' || sex === '男') return '男';
			if (sex === 2 || sex === '2' || sex === '女') return '女';
			return sex || '-';
		},
		getRescreenStatusText(s) {
			const map = { pending: '待复筛', checkedIn: '已到检', completed: '已完成', cancelled: '已取消' };
			return map[s] || s;
		},
		getRescreenStatusClass(s) {
			if (s === 'completed') return 'tag-done';
			if (s === 'checkedIn') return 'tag-doing';
			if (s === 'cancelled') return 'tag-abnormal';
			return 'tag-pending';
		},
		getConclusionText(c) {
			const opt = this.conclusionOptions.find(o => o.value === c);
			return opt ? opt.text : c;
		},
		buildPayload() {
			return {
				studentId: this.studentInfo ? this.studentInfo.id : null,
				batchId: this.batchId,
				itemId: this.currentItem ? this.currentItem.itemId : null,
				itemName: this.currentItem ? this.currentItem.itemName : '',
				category: this.currentItem ? this.currentItem.category : '',
				initialResult: this.currentItem ? this.currentItem.initialResult : '',
				...this.formData
			};
		},
		async saveResult() {
			if (!this.currentItem) {
				uni.showToast({ title: '请先选择复筛项目', icon: 'none' });
				return;
			}
			if (!this.formData.result || !this.formData.conclusion || !this.formData.doctor || !this.formData.screenTime) {
				uni.showToast({ title: '请填写完整必填项', icon: 'none' });
				return;
			}
			this.saving = true;
			try {
				await RescreenApi.saveRescreenResult(this.buildPayload());
				uni.showToast({ title: '保存成功', icon: 'success' });
				this.refreshItems();
			} catch (e) {
				uni.showToast({ title: '保存失败', icon: 'none' });
			} finally {
				this.saving = false;
			}
		},
		async syncUpload() {
			if (!this.currentItem) {
				uni.showToast({ title: '请先选择复筛项目', icon: 'none' });
				return;
			}
			this.syncing = true;
			try {
				await RescreenApi.syncRescreenData(this.buildPayload());
				uni.showToast({ title: '同步上传成功', icon: 'success' });
			} catch (e) {
				uni.showToast({ title: '同步失败', icon: 'none' });
			} finally {
				this.syncing = false;
			}
		},
		async saveAndArchive() {
			if (!this.currentItem) {
				uni.showToast({ title: '请先选择复筛项目', icon: 'none' });
				return;
			}
			if (!this.formData.result || !this.formData.conclusion || !this.formData.doctor || !this.formData.screenTime) {
				uni.showToast({ title: '请填写完整必填项', icon: 'none' });
				return;
			}
			this.archiving = true;
			try {
				// 1. 保存复筛结果
				await RescreenApi.saveRescreenResult(this.buildPayload());
				// 2. 同步上传
				await RescreenApi.syncRescreenData(this.buildPayload());
				// 3. 自动归档至健康档案
				await RescreenApi.archiveRescreenToHealth(this.studentInfo.id, this.batchId);
				uni.showToast({ title: '保存并归档成功', icon: 'success' });
				this.refreshItems();
			} catch (e) {
				uni.showToast({ title: '归档失败', icon: 'none' });
			} finally {
				this.archiving = false;
			}
		},
		async refreshItems() {
			if (!this.qrcode) return;
			try {
				const res = await RescreenApi.getRescreenRegisterByQrcode(this.qrcode, this.batchId);
				this.positiveItems = res.positiveItems || res.items || [];
				this.savedResults = res.savedResults || [];
			} catch (e) {}
		}
	}
};
</script>

<style scoped lang="scss">
.rescreen-register-page {
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
	.batch-info { display: flex; align-items: center;
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
	.card-header { padding: 12px 16px; background: rgba(36, 93, 209, 0.04); border-bottom: 1px solid rgba(238, 238, 238, 1);
		.header-title { font-size: 16px; font-weight: 600; color: rgba(51, 51, 51, 1); }
	}
	.card-body { padding: 12px 16px;
		.info-row { display: flex; gap: 24px; margin-bottom: 6px; font-size: 14px;
			.info-item { color: rgba(51, 51, 51, 1); }
		}
	}
}
.empty-tip { background: #fff; border-radius: 8px; padding: 60px 0; text-align: center;
	.empty-text { color: rgba(153, 153, 153, 1); font-size: 15px; } }
.related-card, .form-card, .saved-list-card {
	background: #fff;
	border-radius: 8px;
	padding: 16px;
	margin-bottom: 12px;
	box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
	.card-title { font-size: 16px; font-weight: 600; color: rgba(51, 51, 51, 1); margin-bottom: 12px; border-left: 3px solid rgba(36, 93, 209, 1); padding-left: 8px; }
}
.related-list {
	display: flex;
	flex-wrap: wrap;
	gap: 10px;
	.related-item {
		flex: 1;
		min-width: 220px;
		padding: 12px;
		background: rgba(244, 247, 252, 1);
		border-radius: 6px;
		border: 2px solid transparent;
		cursor: pointer;
		&.active {
			border-color: rgba(36, 93, 209, 1);
			background: rgba(36, 93, 209, 0.04);
		}
		.related-header { display: flex; gap: 8px; align-items: center; margin-bottom: 6px;
			.related-category { color: rgba(102, 102, 102, 1); font-size: 12px; }
			.related-name { color: rgba(51, 51, 51, 1); font-size: 14px; font-weight: 600; }
		}
		.related-body { display: flex; justify-content: space-between; font-size: 12px;
			.related-initial { color: rgba(223, 65, 65, 1); }
		}
	}
}
.status-tag { padding: 2px 8px; border-radius: 4px; font-size: 12px;
	&.tag-done { background: rgba(51, 176, 19, 0.1); color: rgba(51, 176, 19, 1); }
	&.tag-pending { background: rgba(240, 163, 41, 0.1); color: rgba(240, 163, 41, 1); }
	&.tag-doing { background: rgba(36, 93, 209, 0.1); color: rgba(36, 93, 209, 1); }
	&.tag-abnormal { background: rgba(223, 65, 65, 0.1); color: rgba(223, 65, 65, 1); }
}
.form-actions {
	display: flex;
	justify-content: center;
	margin-top: 16px;
	padding-top: 16px;
	border-top: 1px solid rgba(238, 238, 238, 1);
	flex-wrap: wrap;
	gap: 8px;
}
.batch-picker { width: 70vw; max-width: 600px; background: #fff; border-radius: 12px; padding: 20px;
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
