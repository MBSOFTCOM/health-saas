<template>
	<view class="register-page">
		<view class="tabs">
			<view class="tab-item" :class="{ active: tab === 'apply' }" @click="tab = 'apply'">提交报名</view>
			<view class="tab-item" :class="{ active: tab === 'list' }" @click="switchTab('list')">报名状态</view>
		</view>

		<!-- 提交报名 -->
		<view v-if="tab === 'apply'">
			<view class="form-card">
				<view class="card-title">受检者在线报名</view>
				<view class="card-tip">填写信息提交后将自动同步至筛查系统，并与学校及筛查批次自动关联</view>
				<uni-forms ref="form" :model="form" :rules="rules" label-width="110" label-position="left">
					<uni-forms-item label="就诊人" name="patientId" required>
						<view class="patient-picker" @click="showPatientPicker = true">
							<text :class="currentPatient ? 'picker-value' : 'picker-placeholder'">
								{{ currentPatient ? `${currentPatient.name}（${currentPatient.idCard || ''}）` : '请选择就诊人' }}
							</text>
							<text class="iconfont icon-right"></text>
						</view>
					</uni-forms-item>
					<uni-forms-item label="姓名" name="name" required>
						<uni-easyinput v-model="form.name" placeholder="自动带入，可修改" />
					</uni-forms-item>
					<uni-forms-item label="身份证号" name="idCard" required>
						<uni-easyinput v-model="form.idCard" placeholder="自动带入" @blur="onIdCardBlur" />
					</uni-forms-item>
					<uni-forms-item label="性别" name="sex" required>
						<uni-data-select v-model="form.sex" :localdata="sexOptions" placeholder="请选择性别" />
					</uni-forms-item>
					<uni-forms-item label="年龄" name="age">
						<uni-easyinput v-model="form.age" type="number" placeholder="自动计算" disabled />
					</uni-forms-item>
					<uni-forms-item label="学校" name="schoolId" required>
						<view class="picker-row" @click="openSchoolPicker">
							<text :class="form.schoolName ? 'picker-value' : 'picker-placeholder'">
								{{ form.schoolName || '请选择学校' }}
							</text>
							<text class="iconfont icon-right"></text>
						</view>
					</uni-forms-item>
					<uni-forms-item label="年级" name="grade" required>
						<view class="picker-row" @click="openGradePicker">
							<text :class="form.grade ? 'picker-value' : 'picker-placeholder'">
								{{ form.grade || '请选择年级' }}
							</text>
							<text class="iconfont icon-right"></text>
						</view>
					</uni-forms-item>
					<uni-forms-item label="班级" name="className" required>
						<view class="picker-row" @click="openClassPicker">
							<text :class="form.className ? 'picker-value' : 'picker-placeholder'">
								{{ form.className || '请选择班级' }}
							</text>
							<text class="iconfont icon-right"></text>
						</view>
					</uni-forms-item>
					<uni-forms-item label="筛查批次" name="batchId" required>
						<view class="picker-row" @click="openBatchPicker">
							<text :class="form.batchName ? 'picker-value' : 'picker-placeholder'">
								{{ form.batchName || '请选择筛查批次' }}
							</text>
							<text class="iconfont icon-right"></text>
						</view>
					</uni-forms-item>
					<uni-forms-item label="家长姓名" name="parentName" required>
						<uni-easyinput v-model="form.parentName" placeholder="请输入家长姓名" />
					</uni-forms-item>
					<uni-forms-item label="联系方式" name="phone" required>
						<uni-easyinput v-model="form.phone" type="number" placeholder="请输入家长手机号" />
					</uni-forms-item>
					<uni-forms-item label="备注" name="remark">
						<uni-easyinput type="textarea" v-model="form.remark" placeholder="备注信息（选填）" />
					</uni-forms-item>
				</uni-forms>

				<view class="form-actions">
					<u-button text="重置" @click="resetForm" />
					<u-button type="primary" text="提交报名" @click="submit" :loading="submitting" style="margin-left: 12px" />
				</view>
			</view>
		</view>

		<!-- 报名状态列表 -->
		<view v-else class="list-section">
			<view class="filter-bar">
				<view class="filter-item">
					<text class="filter-label">就诊人</text>
					<view class="picker-row" style="width: 200px" @click="showPatientPicker = true">
						<text :class="currentPatient ? 'picker-value' : 'picker-placeholder'">
							{{ currentPatient ? currentPatient.name : '全部就诊人' }}
						</text>
						<text class="iconfont icon-right"></text>
					</view>
				</view>
				<u-button type="primary" text="查询" @click="loadList" :loading="loading" />
			</view>
			<view class="reg-list" v-if="regList.length">
				<view class="reg-card" v-for="r in regList" :key="r.id" @click="toDetail(r)">
					<view class="reg-card-top">
						<view class="reg-name">{{ r.name }}</view>
						<view class="reg-status" :class="getStatusClass(r.status)">{{ formatStatus(r.status) }}</view>
					</view>
					<view class="reg-meta">
						<text class="meta-item">学校：{{ r.schoolName || '-' }}</text>
						<text class="meta-item">批次：{{ r.batchName || '-' }}</text>
					</view>
					<view class="reg-meta">
						<text class="meta-item">提交时间：{{ r.submitTime || '-' }}</text>
					</view>
				</view>
			</view>
			<view v-else class="empty-state">
				<text class="empty-text">暂无报名记录</text>
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

		<!-- 学校选择 -->
		<u-popup :show="showSchoolPicker" mode="bottom" @close="showSchoolPicker = false" round="12">
			<view class="popup-wrap">
				<view class="popup-title">选择学校</view>
				<view class="popup-list">
					<view
						v-for="s in schoolList"
						:key="s.id"
						class="popup-item"
						@click="selectSchool(s)"
					>
						<text class="p-name">{{ s.name }}</text>
					</view>
					<view v-if="!schoolList.length" class="empty-text">暂无可选学校</view>
				</view>
			</view>
		</u-popup>

		<!-- 年级选择 -->
		<u-popup :show="showGradePicker" mode="bottom" @close="showGradePicker = false" round="12">
			<view class="popup-wrap">
				<view class="popup-title">选择年级</view>
				<view class="popup-list">
					<view v-for="g in gradeList" :key="g" class="popup-item" @click="selectGrade(g)">
						<text class="p-name">{{ g }}</text>
					</view>
					<view v-if="!gradeList.length" class="empty-text">请先选择学校</view>
				</view>
			</view>
		</u-popup>

		<!-- 班级选择 -->
		<u-popup :show="showClassPicker" mode="bottom" @close="showClassPicker = false" round="12">
			<view class="popup-wrap">
				<view class="popup-title">选择班级</view>
				<view class="popup-list">
					<view v-for="c in classList" :key="c" class="popup-item" @click="selectClass(c)">
						<text class="p-name">{{ c }}</text>
					</view>
					<view v-if="!classList.length" class="empty-text">请先选择年级</view>
				</view>
			</view>
		</u-popup>

		<!-- 批次选择 -->
		<u-popup :show="showBatchPicker" mode="bottom" @close="showBatchPicker = false" round="12">
			<view class="popup-wrap">
				<view class="popup-title">选择筛查批次</view>
				<view class="popup-list">
					<view
						v-for="b in batchList"
						:key="b.id"
						class="popup-item"
						@click="selectBatch(b)"
					>
						<view class="popup-item-l">
							<text class="p-name">{{ b.name }}</text>
							<text class="p-meta" v-if="b.screenTime">{{ b.screenTime }}</text>
						</view>
					</view>
					<view v-if="!batchList.length" class="empty-text">暂无可选批次</view>
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
			tab: 'apply',
			submitting: false,
			loading: false,
			patientList: [],
			currentPatient: null,
			showPatientPicker: false,
			schoolList: [],
			showSchoolPicker: false,
			gradeList: [],
			showGradePicker: false,
			classList: [],
			showClassPicker: false,
			batchList: [],
			showBatchPicker: false,
			form: {
				patientId: '',
				name: '',
				idCard: '',
				sex: '',
				birthDate: '',
				age: '',
				schoolId: '',
				schoolName: '',
				grade: '',
				className: '',
				batchId: '',
				batchName: '',
				parentName: '',
				phone: '',
				remark: ''
			},
			rules: {
				name: { rules: [{ required: true, errorMessage: '请输入姓名' }] },
				idCard: {
					rules: [
						{ required: true, errorMessage: '请输入身份证号' },
						{ pattern: /^\d{17}[\dXx]$/, errorMessage: '身份证号格式不正确' }
					]
				},
				sex: { rules: [{ required: true, errorMessage: '请选择性别' }] },
				schoolId: { rules: [{ required: true, errorMessage: '请选择学校' }] },
				grade: { rules: [{ required: true, errorMessage: '请选择年级' }] },
				className: { rules: [{ required: true, errorMessage: '请选择班级' }] },
				batchId: { rules: [{ required: true, errorMessage: '请选择筛查批次' }] },
				parentName: { rules: [{ required: true, errorMessage: '请输入家长姓名' }] },
				phone: {
					rules: [
						{ required: true, errorMessage: '请输入家长手机号' },
						{ pattern: /^1\d{10}$/, errorMessage: '手机号格式不正确' }
					]
				}
			},
			sexOptions: [
				{ value: 1, text: '男' },
				{ value: 2, text: '女' }
			],
			regList: []
		};
	},
	onLoad(e) {
		// 恢复当前就诊人
		const cache = uni.getStorageSync('parent_current_patient');
		if (cache) {
			this.currentPatient = cache;
			this.applyPatient(cache);
		}
		this.loadPatients();
		this.loadOptions();
		if (e.tab === 'list') this.tab = 'list';
	},
	onShow() {
		if (uni.$patientListRefresh) {
			uni.$patientListRefresh = false;
			this.loadPatients();
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
			} catch (e) {
				this.patientList = [];
			}
		},
		async loadOptions() {
			try {
				const [s, b] = await Promise.all([
					ParentApi.getSchoolOptionList(),
					ParentApi.getBatchOptionList()
				]);
				this.schoolList = s.list || s.data || s || [];
				this.batchList = b.list || b.data || b || [];
			} catch (e) {
				this.schoolList = [];
				this.batchList = [];
			}
		},
		async loadGrades() {
			if (!this.form.schoolId) return;
			try {
				const res = await ParentApi.getGradeOptionList(this.form.schoolId);
				this.gradeList = res.list || res.data || res || [];
			} catch (e) {
				this.gradeList = [];
			}
		},
		async loadClasses() {
			if (!this.form.schoolId || !this.form.grade) return;
			try {
				const res = await ParentApi.getClassOptionList(this.form.schoolId, this.form.grade);
				this.classList = res.list || res.data || res || [];
			} catch (e) {
				this.classList = [];
			}
		},
		applyPatient(p) {
			this.currentPatient = p;
			this.form.patientId = p.id;
			this.form.name = p.name || '';
			this.form.idCard = p.idCard || '';
			this.form.sex = p.sex || '';
			this.form.birthDate = p.birthDate || '';
			this.form.age = p.age || '';
			this.form.schoolName = p.schoolName || '';
			this.form.grade = p.grade || '';
			this.form.className = p.className || '';
			this.form.parentName = p.parentName || '';
			this.form.phone = p.phone || '';
		},
		selectPatient(p) {
			this.applyPatient(p);
			uni.setStorageSync('parent_current_patient', p);
			this.showPatientPicker = false;
		},
		openSchoolPicker() {
			this.showSchoolPicker = true;
		},
		selectSchool(s) {
			this.form.schoolId = s.id;
			this.form.schoolName = s.name;
			this.form.grade = '';
			this.form.className = '';
			this.gradeList = [];
			this.classList = [];
			this.showSchoolPicker = false;
			this.loadGrades();
		},
		openGradePicker() {
			if (!this.form.schoolId) {
				uni.showToast({ title: '请先选择学校', icon: 'none' });
				return;
			}
			this.showGradePicker = true;
		},
		selectGrade(g) {
			this.form.grade = typeof g === 'object' ? g.name : g;
			this.form.className = '';
			this.classList = [];
			this.showGradePicker = false;
			this.loadClasses();
		},
		openClassPicker() {
			if (!this.form.grade) {
				uni.showToast({ title: '请先选择年级', icon: 'none' });
				return;
			}
			this.showClassPicker = true;
		},
		selectClass(c) {
			this.form.className = typeof c === 'object' ? c.name : c;
			this.showClassPicker = false;
		},
		openBatchPicker() {
			this.showBatchPicker = true;
		},
		selectBatch(b) {
			this.form.batchId = b.id;
			this.form.batchName = b.name;
			this.showBatchPicker = false;
		},
		onIdCardBlur() {
			const id = this.form.idCard;
			if (!id || !/^\d{17}[\dXx]$/.test(id)) return;
			const sexCode = parseInt(id.charAt(16), 10);
			this.form.sex = sexCode % 2 === 1 ? 1 : 2;
			const birth = id.substring(6, 14);
			this.form.birthDate = `${birth.substring(0, 4)}-${birth.substring(4, 6)}-${birth.substring(6, 8)}`;
			if (this.form.birthDate) {
				const now = new Date();
				const b = new Date(this.form.birthDate);
				let age = now.getFullYear() - b.getFullYear();
				const m = now.getMonth() - b.getMonth();
				if (m < 0 || (m === 0 && now.getDate() < b.getDate())) age--;
				this.form.age = age >= 0 ? age : '';
			}
		},
		switchTab(t) {
			this.tab = t;
			if (t === 'list') this.loadList();
		},
		async loadList() {
			this.loading = true;
			try {
				const params = this.currentPatient ? { patientId: this.currentPatient.id } : {};
				const res = await ParentApi.getRegistrationList(params);
				this.regList = res.list || res.data || res || [];
			} catch (e) {
				this.regList = [];
			} finally {
				this.loading = false;
			}
		},
		formatStatus(s) {
			const map = { 0: '待审核', 1: '已通过', 2: '已驳回', 3: '已取消' };
			return map[s] || s || '-';
		},
		getStatusClass(s) {
			const map = { 0: 'st-pending', 1: 'st-pass', 2: 'st-reject', 3: 'st-cancel' };
			return map[s] || 'st-pending';
		},
		toDetail(r) {
			uni.navigateTo({ url: '/pages/parent/register/detail?id=' + r.id });
		},
		resetForm() {
			this.form = {
				patientId: this.currentPatient ? this.currentPatient.id : '',
				name: this.currentPatient ? this.currentPatient.name : '',
				idCard: this.currentPatient ? this.currentPatient.idCard : '',
				sex: this.currentPatient ? this.currentPatient.sex : '',
				birthDate: this.currentPatient ? this.currentPatient.birthDate : '',
				age: this.currentPatient ? this.currentPatient.age : '',
				schoolId: '',
				schoolName: '',
				grade: '',
				className: '',
				batchId: '',
				batchName: '',
				parentName: this.currentPatient ? this.currentPatient.parentName : '',
				phone: this.currentPatient ? this.currentPatient.phone : '',
				remark: ''
			};
		},
		submit() {
			if (!this.currentPatient) {
				uni.showToast({ title: '请先选择就诊人', icon: 'none' });
				return;
			}
			this.$refs.form.validate().then(async () => {
				this.submitting = true;
				try {
					await ParentApi.submitRegistration(this.form);
					uni.showToast({ title: '报名已提交', icon: 'success' });
					setTimeout(() => {
						this.switchTab('list');
					}, 800);
				} catch (e) {
					uni.showToast({ title: '提交失败', icon: 'none' });
				} finally {
					this.submitting = false;
				}
			}).catch(() => {});
		}
	}
};
</script>

<style scoped lang="scss">
.register-page {
	min-height: 100vh;
	background: rgba(244, 247, 252, 1);
	padding: 16px;
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
.form-card {
	background: #fff;
	border-radius: 8px;
	padding: 20px;
	box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
	max-width: 800px;
	margin: 0 auto;
	.card-title {
		font-size: 18px;
		font-weight: 600;
		color: rgba(36, 93, 209, 1);
		text-align: center;
		margin-bottom: 6px;
	}
	.card-tip {
		font-size: 12px;
		color: rgba(153, 153, 153, 1);
		text-align: center;
		margin-bottom: 16px;
		padding-bottom: 12px;
		border-bottom: 1px solid rgba(238, 238, 238, 1);
	}
	.form-actions {
		display: flex;
		justify-content: center;
		margin-top: 20px;
		padding-top: 16px;
		border-top: 1px solid rgba(238, 238, 238, 1);
	}
}
.patient-picker, .picker-row {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 0 10px;
	height: 36px;
	background: rgba(244, 247, 252, 1);
	border-radius: 4px;
	.picker-value {
		font-size: 14px;
		color: rgba(51, 51, 51, 1);
	}
	.picker-placeholder {
		font-size: 14px;
		color: rgba(153, 153, 153, 1);
	}
}
.list-section {
	.filter-bar {
		display: flex;
		align-items: center;
		gap: 12px;
		background: #fff;
		padding: 12px 16px;
		border-radius: 10px;
		margin-bottom: 16px;
		box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
		.filter-item {
			display: flex;
			align-items: center;
			flex: 1;
			.filter-label {
				font-size: 14px;
				color: rgba(102, 102, 102, 1);
				margin-right: 8px;
			}
		}
	}
	.reg-list {
		.reg-card {
			background: #fff;
			border-radius: 10px;
			padding: 14px 16px;
			margin-bottom: 10px;
			box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
			.reg-card-top {
				display: flex;
				justify-content: space-between;
				align-items: center;
				margin-bottom: 8px;
				.reg-name {
					font-size: 16px;
					font-weight: 600;
					color: rgba(51, 51, 51, 1);
				}
				.reg-status {
					font-size: 12px;
					padding: 2px 8px;
					border-radius: 10px;
				}
				.st-pending { background: rgba(240, 163, 41, 0.1); color: rgba(240, 163, 41, 1); }
				.st-pass { background: rgba(51, 176, 19, 0.1); color: rgba(51, 176, 19, 1); }
				.st-reject { background: rgba(223, 65, 65, 0.1); color: rgba(223, 65, 65, 1); }
				.st-cancel { background: rgba(153, 153, 153, 0.1); color: rgba(153, 153, 153, 1); }
			}
			.reg-meta {
				margin-top: 4px;
				.meta-item {
					font-size: 13px;
					color: rgba(102, 102, 102, 1);
					margin-right: 12px;
				}
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
		&.active {
			background: rgba(36, 93, 209, 0.08);
		}
		.popup-item-l {
			display: flex;
			flex-direction: column;
			.p-name {
				font-size: 15px;
				font-weight: 600;
				color: rgba(51, 51, 51, 1);
			}
			.p-meta {
				font-size: 12px;
				color: rgba(153, 153, 153, 1);
				margin-top: 2px;
			}
		}
		.check {
			color: rgba(51, 176, 19, 1);
			font-size: 18px;
		}
	}
	.empty-text {
		text-align: center;
		font-size: 14px;
		color: rgba(153, 153, 153, 1);
		padding: 30px 0;
	}
}
</style>
