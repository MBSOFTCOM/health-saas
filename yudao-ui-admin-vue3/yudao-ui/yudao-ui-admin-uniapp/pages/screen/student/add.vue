<template>
	<view class="add-page">
		<view class="form-card">
			<view class="card-title">{{ isEdit ? '编辑受检者' : '新增受检者' }}</view>
			<uni-forms ref="form" :model="form" :rules="rules" label-width="100" label-position="left">
				<uni-forms-item label="姓名" name="name" required>
					<uni-easyinput v-model="form.name" placeholder="请输入姓名" />
				</uni-forms-item>
				<uni-forms-item label="性别" name="sex" required>
					<uni-data-select v-model="form.sex" :localdata="sexOptions" placeholder="请选择性别" />
				</uni-forms-item>
				<uni-forms-item label="出生日期" name="birthDate" required>
					<uni-datetime-picker type="date" v-model="form.birthDate" placeholder="请选择出生日期" />
				</uni-forms-item>
				<uni-forms-item label="年龄" name="age">
					<uni-easyinput v-model="form.age" type="number" placeholder="自动计算" disabled />
				</uni-forms-item>
				<uni-forms-item label="学校" name="schoolName" required>
					<uni-easyinput v-model="form.schoolName" placeholder="请输入学校名称" />
				</uni-forms-item>
				<uni-forms-item label="年级" name="grade" required>
					<uni-easyinput v-model="form.grade" placeholder="如 一年级" />
				</uni-forms-item>
				<uni-forms-item label="班级" name="className" required>
					<uni-easyinput v-model="form.className" placeholder="如 1班" />
				</uni-forms-item>
				<uni-forms-item label="学号" name="studentNo">
					<uni-easyinput v-model="form.studentNo" placeholder="请输入学号" />
				</uni-forms-item>
				<uni-forms-item label="身份证号" name="idCard">
					<uni-easyinput v-model="form.idCard" placeholder="请输入身份证号" />
				</uni-forms-item>
				<uni-forms-item label="联系电话" name="phone">
					<uni-easyinput v-model="form.phone" placeholder="家长联系电话" />
				</uni-forms-item>
				<uni-forms-item label="家长姓名" name="parentName">
					<uni-easyinput v-model="form.parentName" placeholder="家长姓名" />
				</uni-forms-item>
				<uni-forms-item label="区域" name="district">
					<uni-easyinput v-model="form.district" placeholder="所在区域" />
				</uni-forms-item>
				<uni-forms-item label="备注" name="remark">
					<uni-easyinput type="textarea" v-model="form.remark" placeholder="备注信息" />
				</uni-forms-item>
			</uni-forms>

			<view class="form-actions">
				<u-button text="返回" @click="goBack" />
				<u-button type="primary" text="保存" @click="submit" :loading="submitting" style="margin-left: 12px" />
				<u-button v-if="isEdit" type="success" text="生成二维码" @click="generateQrcode" style="margin-left: 12px" />
			</view>
		</view>
	</view>
</template>

<script>
import * as StudentApi from '@/api/screen/student';

export default {
	data() {
		return {
			isEdit: false,
			submitting: false,
			form: {
				id: '',
				name: '',
				sex: '',
				birthDate: '',
				age: '',
				schoolName: '',
				grade: '',
				className: '',
				studentNo: '',
				idCard: '',
				phone: '',
				parentName: '',
				district: '',
				remark: ''
			},
			rules: {
				name: { rules: [{ required: true, errorMessage: '请输入姓名' }] },
				sex: { rules: [{ required: true, errorMessage: '请选择性别' }] },
				birthDate: { rules: [{ required: true, errorMessage: '请选择出生日期' }] },
				schoolName: { rules: [{ required: true, errorMessage: '请输入学校名称' }] },
				grade: { rules: [{ required: true, errorMessage: '请输入年级' }] },
				className: { rules: [{ required: true, errorMessage: '请输入班级' }] }
			},
			sexOptions: [
				{ value: 1, text: '男' },
				{ value: 2, text: '女' }
			]
		};
	},
	watch: {
		'form.birthDate'(val) {
			if (val) {
				this.form.age = this.calcAge(val);
			}
		}
	},
	onLoad(e) {
		if (e.id) {
			this.isEdit = true;
			this.loadDetail(e.id);
		}
	},
	methods: {
		calcAge(birth) {
			const now = new Date();
			const b = new Date(birth);
			let age = now.getFullYear() - b.getFullYear();
			const m = now.getMonth() - b.getMonth();
			if (m < 0 || (m === 0 && now.getDate() < b.getDate())) age--;
			return age >= 0 ? age : '';
		},
		async loadDetail(id) {
			try {
				const res = await StudentApi.getStudentDetail(id);
				this.form = { ...this.form, ...res };
			} catch (e) {
				uni.showToast({ title: '加载失败', icon: 'none' });
			}
		},
		submit() {
			this.$refs.form.validate().then(async () => {
				this.submitting = true;
				try {
					if (this.isEdit) {
						await StudentApi.updateStudent(this.form);
					} else {
						await StudentApi.createStudent(this.form);
					}
					uni.showToast({ title: '保存成功', icon: 'success' });
					uni.$studentListRefresh = true;
					setTimeout(() => uni.navigateBack(), 800);
				} catch (e) {
					uni.showToast({ title: '保存失败', icon: 'none' });
				} finally {
					this.submitting = false;
				}
			}).catch(() => {});
		},
		async generateQrcode() {
			if (!this.form.id) {
				uni.showToast({ title: '请先保存学生信息', icon: 'none' });
				return;
			}
			try {
				await StudentApi.getStudentQrcode(this.form.id);
				uni.showToast({ title: '二维码已生成', icon: 'success' });
			} catch (e) {
				uni.showToast({ title: '生成失败', icon: 'none' });
			}
		},
		goBack() {
			uni.navigateBack();
		}
	}
};
</script>

<style scoped lang="scss">
.add-page {
	padding: 16px;
	background: rgba(244, 247, 252, 1);
	min-height: 100vh;
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
		margin-bottom: 20px;
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
</style>
