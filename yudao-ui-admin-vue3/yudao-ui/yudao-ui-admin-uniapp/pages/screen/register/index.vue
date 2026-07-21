<template>
	<view class="register-page">
		<!-- 顶部：批次信息 + 扫码 -->
		<view class="top-bar">
			<view class="batch-info">
				<text class="info-label">当前批次：</text>
				<text class="info-value">{{ batchName || '未选择' }}</text>
				<text class="change-btn" @click="showBatchPicker = true">切换</text>
			</view>
			<u-button type="primary" icon="scan" text="扫码登记" @click="scanQrcode" :loading="scanning" />
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
					<text class="info-item">班级：{{ studentInfo.className }}</text>
				</view>
				<view class="info-row">
					<text class="info-item">二维码：{{ studentInfo.qrcode }}</text>
				</view>
			</view>
		</view>

		<view v-else class="empty-tip">
			<text class="empty-text">请扫描受检者二维码开始登记</text>
		</view>

		<!-- 五健筛查项目录入 -->
		<view class="screening-tabs" v-if="studentInfo">
			<view
				v-for="(tab, idx) in categoryTabs"
				:key="idx"
				class="tab-item"
				:class="{ active: currentTab === tab.key, done: tab.filled }"
				@click="currentTab = tab.key"
			>
				<text class="tab-icon">{{ tab.icon }}</text>
				<text class="tab-label">{{ tab.label }}</text>
				<text v-if="tab.filled" class="tab-check">✓</text>
			</view>
		</view>

		<view class="form-container" v-if="studentInfo && currentTab">
			<!-- 体形 -->
			<view v-if="currentTab === 'body-shape'">
				<uni-forms ref="bodyForm" :model="formData.bodyShape" label-width="120" label-position="left">
					<uni-forms-item label="身高(cm)" name="height" required>
						<uni-easyinput v-model="formData.bodyShape.height" type="number" placeholder="请输入身高" />
					</uni-forms-item>
					<uni-forms-item label="体重(kg)" name="weight" required>
						<uni-easyinput v-model="formData.bodyShape.weight" type="number" placeholder="请输入体重" />
					</uni-forms-item>
					<uni-forms-item label="BMI" name="bmi">
						<text class="calc-value">{{ calcBmi }}</text>
					</uni-forms-item>
					<uni-forms-item label="体型评估" name="bodyType">
						<uni-data-select
							v-model="formData.bodyShape.bodyType"
							:localdata="bodyTypeOptions"
							placeholder="请选择"
						/>
					</uni-forms-item>
					<uni-forms-item label="设备采集" name="deviceData">
						<u-button text="连接身高体重秤采集" @click="collectFromDevice('stadiometer')" :loading="deviceLoading.stadiometer" />
					</uni-forms-item>
				</uni-forms>
			</view>

			<!-- 视力 -->
			<view v-if="currentTab === 'vision'">
				<uni-forms ref="visionForm" :model="formData.vision" label-width="120" label-position="left">
					<uni-forms-item label="左眼裸眼视力" name="leftNaked" required>
						<uni-easyinput v-model="formData.vision.leftNaked" type="digit" placeholder="如 5.0" />
					</uni-forms-item>
					<uni-forms-item label="右眼裸眼视力" name="rightNaked" required>
						<uni-easyinput v-model="formData.vision.rightNaked" type="digit" placeholder="如 5.0" />
					</uni-forms-item>
					<uni-forms-item label="左眼矫正视力" name="leftCorrected">
						<uni-easyinput v-model="formData.vision.leftCorrected" type="digit" placeholder="无矫正可留空" />
					</uni-forms-item>
					<uni-forms-item label="右眼矫正视力" name="rightCorrected">
						<uni-easyinput v-model="formData.vision.rightCorrected" type="digit" placeholder="无矫正可留空" />
					</uni-forms-item>
					<uni-forms-item label="色觉" name="colorVision">
						<uni-data-select v-model="formData.vision.colorVision" :localdata="colorVisionOptions" placeholder="请选择" />
					</uni-forms-item>
					<uni-forms-item label="设备采集" name="deviceData">
						<u-button text="连接视力筛查仪采集" @click="collectFromDevice('visionScreener')" :loading="deviceLoading.visionScreener" />
					</uni-forms-item>
				</uni-forms>
			</view>

			<!-- 骨骼 -->
			<view v-if="currentTab === 'bone'">
				<uni-forms ref="boneForm" :model="formData.bone" label-width="120" label-position="left">
					<uni-forms-item label="脊柱侧弯" name="scoliosis" required>
						<uni-data-select v-model="formData.bone.scoliosis" :localdata="abnormalOptions" placeholder="请选择" />
					</uni-forms-item>
					<uni-forms-item label="脊柱前倾角" name="forwardAngle">
						<uni-easyinput v-model="formData.bone.forwardAngle" type="number" placeholder="度数" />
					</uni-forms-item>
					<uni-forms-item label="高低肩" name="shoulderImbalance">
						<uni-data-select v-model="formData.bone.shoulderImbalance" :localdata="abnormalOptions" placeholder="请选择" />
					</uni-forms-item>
					<uni-forms-item label="骨密度T值" name="bmdTScore">
						<uni-easyinput v-model="formData.bone.bmdTScore" type="digit" placeholder="请输入" />
					</uni-forms-item>
					<uni-forms-item label="足底压力" name="footPressure">
						<uni-easyinput v-model="formData.bone.footPressure" placeholder="如 左/右 mmHg" />
					</uni-forms-item>
					<uni-forms-item label="设备采集" name="deviceData">
						<view style="display:flex;gap:10px;flex-wrap:wrap">
							<u-button text="脊柱侧弯仪" @click="collectFromDevice('scoliosometer')" :loading="deviceLoading.scoliosometer" />
							<u-button text="足底压力仪" @click="collectFromDevice('footPressure')" :loading="deviceLoading.footPressure" />
						</view>
					</uni-forms-item>
				</uni-forms>
			</view>

			<!-- 口腔 -->
			<view v-if="currentTab === 'oral'">
				<uni-forms ref="oralForm" :model="formData.oral" label-width="120" label-position="left">
					<uni-forms-item label="龋齿" name="caries" required>
						<uni-data-select v-model="formData.oral.caries" :localdata="abnormalOptions" placeholder="请选择" />
					</uni-forms-item>
					<uni-forms-item label="龋齿数量" name="cariesCount">
						<uni-easyinput v-model="formData.oral.cariesCount" type="number" placeholder="颗" />
					</uni-forms-item>
					<uni-forms-item label="牙龈炎" name="gingivitis">
						<uni-data-select v-model="formData.oral.gingivitis" :localdata="abnormalOptions" placeholder="请选择" />
					</uni-forms-item>
					<uni-forms-item label="牙菌斑" name="plaque">
						<uni-data-select v-model="formData.oral.plaque" :localdata="abnormalOptions" placeholder="请选择" />
					</uni-forms-item>
					<uni-forms-item label="牙齿缺失" name="toothLoss">
						<uni-data-select v-model="formData.oral.toothLoss" :localdata="abnormalOptions" placeholder="请选择" />
					</uni-forms-item>
					<uni-forms-item label="备注" name="remark">
						<uni-easyinput type="textarea" v-model="formData.oral.remark" placeholder="口腔其他情况说明" />
					</uni-forms-item>
				</uni-forms>
			</view>

			<!-- 心理 -->
			<view v-if="currentTab === 'mental'">
				<uni-forms ref="mentalForm" :model="formData.mental" label-width="140" label-position="left">
					<uni-forms-item label="心理健康评分" name="score" required>
						<uni-easyinput v-model="formData.mental.score" type="number" placeholder="0-100" />
					</uni-forms-item>
					<uni-forms-item label="情绪状态" name="emotion">
						<uni-data-select v-model="formData.mental.emotion" :localdata="emotionOptions" placeholder="请选择" />
					</uni-forms-item>
					<uni-forms-item label="睡眠质量" name="sleep">
						<uni-data-select v-model="formData.mental.sleep" :localdata="sleepOptions" placeholder="请选择" />
					</uni-forms-item>
					<uni-forms-item label="社交能力" name="social">
						<uni-data-select v-model="formData.mental.social" :localdata="socialOptions" placeholder="请选择" />
					</uni-forms-item>
					<uni-forms-item label="异常表现" name="abnormal">
						<uni-easyinput type="textarea" v-model="formData.mental.abnormal" placeholder="如有异常表现请描述" />
					</uni-forms-item>
				</uni-forms>
			</view>

			<!-- 操作按钮 -->
			<view class="form-actions">
				<u-button text="校验数据" @click="validateData" />
				<u-button type="warning" text="异常识别" @click="checkAbnormal" style="margin-left: 10px" />
				<u-button type="primary" text="保存当前项目" @click="saveCurrent" :loading="saving" style="margin-left: 10px" />
				<u-button type="success" text="提交全部" @click="submitAll" :loading="submitting" style="margin-left: 10px" />
			</view>
		</view>

		<!-- 异常结果提示 -->
		<u-popup :show="showAbnormalPopup" mode="center" @close="showAbnormalPopup = false" round="12">
			<view class="abnormal-popup">
				<view class="popup-title">异常结果识别</view>
				<view class="popup-content">
					<view v-if="abnormalList.length === 0" class="empty-tip">未识别到异常项</view>
					<view v-else>
						<view v-for="(item, idx) in abnormalList" :key="idx" class="abnormal-item">
							<text class="abnormal-category">{{ item.category }}</text>
							<text class="abnormal-desc">{{ item.desc }}</text>
						</view>
					</view>
				</view>
				<view class="popup-footer">
					<u-button text="知道了" @click="showAbnormalPopup = false" />
				</view>
			</view>
		</u-popup>

		<!-- 批次选择 -->
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
import * as RegisterApi from '@/api/screen/register';
import * as BatchApi from '@/api/screen/batch';
import * as DeviceApi from '@/api/screen/device';

export default {
	data() {
		return {
			batchId: '',
			batchName: '',
			batchOptions: [],
			showBatchPicker: false,
			studentInfo: null,
			scanning: false,
			saving: false,
			submitting: false,
			currentTab: 'body-shape',
			showAbnormalPopup: false,
			abnormalList: [],
			deviceLoading: {
				stadiometer: false,
				visionScreener: false,
				scoliosometer: false,
				footPressure: false
			},
			formData: {
				bodyShape: { height: '', weight: '', bmi: '', bodyType: '' },
				vision: { leftNaked: '', rightNaked: '', leftCorrected: '', rightCorrected: '', colorVision: '' },
				bone: { scoliosis: '', forwardAngle: '', shoulderImbalance: '', bmdTScore: '', footPressure: '' },
				oral: { caries: '', cariesCount: '', gingivitis: '', plaque: '', toothLoss: '', remark: '' },
				mental: { score: '', emotion: '', sleep: '', social: '', abnormal: '' }
			},
			bodyTypeOptions: [
				{ value: 'thin', text: '偏瘦' },
				{ value: 'normal', text: '正常' },
				{ value: 'overweight', text: '超重' },
				{ value: 'obese', text: '肥胖' }
			],
			colorVisionOptions: [
				{ value: 'normal', text: '正常' },
				{ value: 'abnormal', text: '异常' }
			],
			abnormalOptions: [
				{ value: 'normal', text: '正常' },
				{ value: 'mild', text: '轻度异常' },
				{ value: 'moderate', text: '中度异常' },
				{ value: 'severe', text: '重度异常' }
			],
			emotionOptions: [
				{ value: 'good', text: '良好' },
				{ value: 'stable', text: '稳定' },
				{ value: 'anxious', text: '焦虑' },
				{ value: 'depressed', text: '抑郁' }
			],
			sleepOptions: [
				{ value: 'good', text: '良好' },
				{ value: 'fair', text: '一般' },
				{ value: 'poor', text: '较差' },
				{ value: 'insomnia', text: '失眠' }
			],
			socialOptions: [
				{ value: 'good', text: '良好' },
				{ value: 'fair', text: '一般' },
				{ value: 'poor', text: '较差' },
				{ value: 'isolated', text: '孤僻' }
			]
		};
	},
	computed: {
		calcBmi() {
			const h = parseFloat(this.formData.bodyShape.height);
			const w = parseFloat(this.formData.bodyShape.weight);
			if (h && w) {
				const hM = h / 100;
				return (w / (hM * hM)).toFixed(1);
			}
			return '';
		},
		statusText() {
			if (!this.studentInfo) return '';
			return this.studentInfo.status === 'completed' ? '已完成' : '登记中';
		},
		statusClass() {
			return this.studentInfo && this.studentInfo.status === 'completed' ? 'status-done' : 'status-doing';
		},
		categoryTabs() {
			return [
				{ key: 'body-shape', label: '体形', icon: '身', filled: this.isCategoryFilled('body-shape') },
				{ key: 'vision', label: '视力', icon: '眼', filled: this.isCategoryFilled('vision') },
				{ key: 'bone', label: '骨骼', icon: '骨', filled: this.isCategoryFilled('bone') },
				{ key: 'oral', label: '口腔', icon: '口', filled: this.isCategoryFilled('oral') },
				{ key: 'mental', label: '心理', icon: '心', filled: this.isCategoryFilled('mental') }
			];
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
				const res = await RegisterApi.getScreeningTaskByQrcode(qrcode, this.batchId);
				this.studentInfo = res.student || res;
				this.studentInfo.qrcode = qrcode;
				// 加载已登记的结果
				if (this.studentInfo.id) {
					this.loadExistingResults(this.studentInfo.id);
				}
				uni.showToast({ title: '登记就绪', icon: 'success' });
			} catch (e) {
				uni.showToast({ title: '二维码无效或未找到受检者', icon: 'none' });
			}
		},
		async loadExistingResults(studentId) {
			try {
				const res = await RegisterApi.getScreeningResult(studentId, this.batchId);
				if (res) {
					Object.keys(this.formData).forEach(key => {
						if (res[key]) {
							this.formData[key] = { ...this.formData[key], ...res[key] };
						}
					});
				}
			} catch (e) {}
		},
		isCategoryFilled(category) {
			const data = this.formData[category];
			return data && Object.values(data).some(v => v !== '' && v !== null && v !== undefined);
		},
		formatSex(sex) {
			if (sex === 1 || sex === '1' || sex === '男') return '男';
			if (sex === 2 || sex === '2' || sex === '女') return '女';
			return sex || '-';
		},
		async collectFromDevice(deviceType) {
			this.deviceLoading[deviceType] = true;
			try {
				const res = await DeviceApi.syncDeviceData({
					deviceType,
					studentId: this.studentInfo ? this.studentInfo.id : null,
					batchId: this.batchId
				});
				if (res && res.data) {
					this.applyDeviceData(deviceType, res.data);
					uni.showToast({ title: '采集成功', icon: 'success' });
				}
			} catch (e) {
				uni.showToast({ title: '设备采集失败，请手动录入', icon: 'none' });
			} finally {
				this.deviceLoading[deviceType] = false;
			}
		},
		applyDeviceData(deviceType, data) {
			if (deviceType === 'stadiometer') {
				this.formData.bodyShape.height = data.height || '';
				this.formData.bodyShape.weight = data.weight || '';
			} else if (deviceType === 'visionScreener') {
				this.formData.vision.leftNaked = data.leftNaked || '';
				this.formData.vision.rightNaked = data.rightNaked || '';
				this.formData.vision.leftCorrected = data.leftCorrected || '';
				this.formData.vision.rightCorrected = data.rightCorrected || '';
			} else if (deviceType === 'scoliosometer') {
				this.formData.bone.scoliosis = data.scoliosis || '';
				this.formData.bone.forwardAngle = data.forwardAngle || '';
				this.formData.bone.shoulderImbalance = data.shoulderImbalance || '';
			} else if (deviceType === 'footPressure') {
				this.formData.bone.footPressure = data.footPressure || '';
			}
		},
		async validateData() {
			try {
				await RegisterApi.validateScreeningData({
					category: this.currentTab,
					data: this.formData[this.currentTab],
					studentId: this.studentInfo ? this.studentInfo.id : null,
					batchId: this.batchId
				});
				uni.showToast({ title: '校验通过', icon: 'success' });
			} catch (e) {
				uni.showToast({ title: '校验失败：' + (e.msg || '数据不完整'), icon: 'none' });
			}
		},
		async checkAbnormal() {
			try {
				const res = await RegisterApi.checkAbnormal({
					studentId: this.studentInfo ? this.studentInfo.id : null,
					batchId: this.batchId,
					formData: this.formData
				});
				this.abnormalList = res || [];
				this.showAbnormalPopup = true;
			} catch (e) {
				uni.showToast({ title: '异常识别失败', icon: 'none' });
			}
		},
		async saveCurrent() {
			if (!this.studentInfo) {
				uni.showToast({ title: '请先扫码选择受检者', icon: 'none' });
				return;
			}
			this.saving = true;
			try {
				await RegisterApi.saveScreeningResult({
					studentId: this.studentInfo.id,
					batchId: this.batchId,
					category: this.currentTab,
					data: this.formData[this.currentTab]
				});
				uni.showToast({ title: '保存成功', icon: 'success' });
			} catch (e) {
				uni.showToast({ title: '保存失败', icon: 'none' });
			} finally {
				this.saving = false;
			}
		},
		async submitAll() {
			if (!this.studentInfo) {
				uni.showToast({ title: '请先扫码选择受检者', icon: 'none' });
				return;
			}
			this.submitting = true;
			try {
				await RegisterApi.batchSaveScreeningResult({
					studentId: this.studentInfo.id,
					batchId: this.batchId,
					formData: this.formData
				});
				uni.showToast({ title: '全部提交成功', icon: 'success' });
				this.studentInfo.status = 'completed';
			} catch (e) {
				uni.showToast({ title: '提交失败', icon: 'none' });
			} finally {
				this.submitting = false;
			}
		}
	}
};
</script>

<style scoped lang="scss">
.register-page {
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
		.info-label {
			font-size: 14px;
			color: rgba(102, 102, 102, 1);
		}
		.info-value {
			font-size: 15px;
			font-weight: 600;
			color: rgba(36, 93, 209, 1);
			margin: 0 8px;
		}
		.change-btn {
			font-size: 13px;
			color: rgba(36, 93, 209, 1);
			text-decoration: underline;
			cursor: pointer;
		}
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
		.header-title {
			font-size: 16px;
			font-weight: 600;
			color: rgba(51, 51, 51, 1);
		}
		.header-status {
			padding: 2px 10px;
			border-radius: 12px;
			font-size: 12px;
		}
		.status-doing {
			background: rgba(240, 163, 41, 0.1);
			color: rgba(240, 163, 41, 1);
		}
		.status-done {
			background: rgba(51, 176, 19, 0.1);
			color: rgba(51, 176, 19, 1);
		}
	}
	.card-body {
		padding: 12px 16px;
		.info-row {
			display: flex;
			gap: 24px;
			margin-bottom: 6px;
			font-size: 14px;
			.info-item {
				color: rgba(51, 51, 51, 1);
			}
		}
	}
}
.empty-tip {
	background: #fff;
	border-radius: 8px;
	padding: 60px 0;
	text-align: center;
	.empty-text {
		color: rgba(153, 153, 153, 1);
		font-size: 15px;
	}
}
.screening-tabs {
	display: flex;
	background: #fff;
	border-radius: 8px;
	padding: 8px;
	margin-bottom: 12px;
	box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
	.tab-item {
		flex: 1;
		display: flex;
		flex-direction: column;
		align-items: center;
		padding: 12px 4px;
		border-radius: 6px;
		cursor: pointer;
		position: relative;
		&.active {
			background: rgba(36, 93, 209, 0.1);
			.tab-label {
				color: rgba(36, 93, 209, 1);
				font-weight: 600;
			}
			.tab-icon {
				background: rgba(36, 93, 209, 1);
				color: #fff;
			}
		}
		&.done {
			.tab-check {
				position: absolute;
				top: 4px;
				right: 8px;
				color: rgba(51, 176, 19, 1);
				font-size: 14px;
			}
		}
		.tab-icon {
			width: 36px;
			height: 36px;
			border-radius: 50%;
			background: rgba(244, 247, 252, 1);
			color: rgba(102, 102, 102, 1);
			display: flex;
			align-items: center;
			justify-content: center;
			font-size: 16px;
			font-weight: 600;
			margin-bottom: 4px;
		}
		.tab-label {
			font-size: 13px;
			color: rgba(102, 102, 102, 1);
		}
	}
}
.form-container {
	background: #fff;
	border-radius: 8px;
	padding: 16px;
	box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
	.calc-value {
		font-size: 16px;
		font-weight: 600;
		color: rgba(36, 93, 209, 1);
		line-height: 36px;
	}
	.form-actions {
		display: flex;
		justify-content: center;
		margin-top: 16px;
		padding-top: 16px;
		border-top: 1px solid rgba(238, 238, 238, 1);
	}
}
.abnormal-popup {
	width: 70vw;
	max-width: 600px;
	background: #fff;
	border-radius: 12px;
	padding: 20px;
	.popup-title {
		font-size: 18px;
		font-weight: 600;
		text-align: center;
		margin-bottom: 16px;
		color: rgba(223, 65, 65, 1);
	}
	.popup-content {
		max-height: 50vh;
		.abnormal-item {
			display: flex;
			padding: 8px 0;
			border-bottom: 1px dashed rgba(238, 238, 238, 1);
			.abnormal-category {
				width: 100px;
				font-weight: 600;
				color: rgba(223, 65, 65, 1);
			}
			.abnormal-desc {
				flex: 1;
				color: rgba(51, 51, 51, 1);
			}
		}
		.empty-tip {
			text-align: center;
			color: rgba(51, 176, 19, 1);
			padding: 20px 0;
		}
	}
	.popup-footer {
		margin-top: 16px;
		display: flex;
		justify-content: center;
	}
}
.batch-picker {
	width: 70vw;
	max-width: 600px;
	background: #fff;
	border-radius: 12px;
	padding: 20px;
	.popup-title {
		font-size: 18px;
		font-weight: 600;
		text-align: center;
		margin-bottom: 16px;
		color: rgba(36, 93, 209, 1);
	}
	.batch-list-scroll {
		max-height: 50vh;
		.batch-option {
			padding: 12px;
			border: 1px solid rgba(238, 238, 238, 1);
			border-radius: 6px;
			margin-bottom: 8px;
			cursor: pointer;
			display: flex;
			justify-content: space-between;
			&.active {
				border-color: rgba(36, 93, 209, 1);
				background: rgba(36, 93, 209, 0.04);
			}
			.batch-name {
				font-size: 15px;
				color: rgba(51, 51, 51, 1);
			}
			.batch-date {
				font-size: 13px;
				color: rgba(102, 102, 102, 1);
			}
		}
		.empty-tip {
			text-align: center;
			color: rgba(153, 153, 153, 1);
			padding: 20px 0;
		}
	}
}
</style>
