<template>
	<view class="add-page">
		<view class="form-card">
			<view class="card-title">绑定就诊人</view>
			<view class="card-tip">通过身份证号校验，确保受检者账号唯一绑定</view>
			<uni-forms ref="form" :model="form" :rules="rules" label-width="100" label-position="left">
				<uni-forms-item label="姓名" name="name" required>
					<uni-easyinput v-model="form.name" placeholder="请输入受检者姓名" />
				</uni-forms-item>
				<uni-forms-item label="身份证号" name="idCard" required>
					<uni-easyinput v-model="form.idCard" placeholder="请输入身份证号" @blur="onIdCardBlur" />
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
				<uni-forms-item label="学校" name="schoolName">
					<uni-easyinput v-model="form.schoolName" placeholder="选填，如已可下拉选择学校" />
				</uni-forms-item>
				<uni-forms-item label="年级" name="grade">
					<uni-easyinput v-model="form.grade" placeholder="如 一年级" />
				</uni-forms-item>
				<uni-forms-item label="班级" name="className">
					<uni-easyinput v-model="form.className" placeholder="如 1班" />
				</uni-forms-item>
				<uni-forms-item label="家长姓名" name="parentName" required>
					<uni-easyinput v-model="form.parentName" placeholder="请输入家长姓名" />
				</uni-forms-item>
				<uni-forms-item label="联系方式" name="phone" required>
					<uni-easyinput v-model="form.phone" type="number" placeholder="请输入家长手机号" />
				</uni-forms-item>
				<uni-forms-item label="与受检者关系" name="relation">
					<uni-data-select v-model="form.relation" :localdata="relationOptions" placeholder="请选择关系" />
				</uni-forms-item>
			</uni-forms>

			<view class="form-actions">
				<u-button text="返回" @click="goBack" />
				<u-button type="primary" text="校验并绑定" @click="submit" :loading="submitting" style="margin-left: 12px" />
			</view>
		</view>
	</view>
</template>

<script>
import * as ParentApi from '@/api/parent/index';

export default {
	data() {
		return {
			submitting: false,
			form: {
				name: '',
				idCard: '',
				sex: '',
				birthDate: '',
				age: '',
				schoolName: '',
				grade: '',
				className: '',
				parentName: '',
				phone: '',
				relation: ''
			},
			rules: {
				name: { rules: [{ required: true, errorMessage: '请输入姓名' }] },
				idCard: {
					rules: [
						{ required: true, errorMessage: '请输入身份证号' },
						{
							pattern: /^\d{17}[\dXx]$/,
							errorMessage: '身份证号格式不正确'
						}
					]
				},
				sex: { rules: [{ required: true, errorMessage: '请选择性别' }] },
				birthDate: { rules: [{ required: true, errorMessage: '请选择出生日期' }] },
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
			relationOptions: [
				{ value: '父亲', text: '父亲' },
				{ value: '母亲', text: '母亲' },
				{ value: '爷爷', text: '爷爷' },
				{ value: '奶奶', text: '奶奶' },
				{ value: '外公', text: '外公' },
				{ value: '外婆', text: '外婆' },
				{ value: '其他', text: '其他' }
			]
		};
	},
	watch: {
		'form.birthDate'(val) {
			if (val) this.form.age = this.calcAge(val);
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
		// 通过身份证号自动解析性别与出生日期
		onIdCardBlur() {
			const id = this.form.idCard;
			if (!id || !/^\d{17}[\dXx]$/.test(id)) return;
			// 性别：倒数第二位奇数为男，偶数为女
			const sexCode = parseInt(id.charAt(16), 10);
			this.form.sex = sexCode % 2 === 1 ? 1 : 2;
			// 出生日期：第7-14位
			const birth = id.substring(6, 14);
			this.form.birthDate = `${birth.substring(0, 4)}-${birth.substring(4, 6)}-${birth.substring(6, 8)}`;
		},
		async checkBindable() {
			try {
				const res = await ParentApi.checkPatientBindable(this.form.idCard);
				// 后端返回 bindable: true/false，message: ...
				if (res && res.bindable === false) {
					uni.showToast({ title: res.message || '该受检者已被绑定', icon: 'none' });
					return false;
				}
				return true;
			} catch (e) {
				// 校验接口异常时不阻塞提交，由后端在绑定接口再次校验
				return true;
			}
		},
		submit() {
			this.$refs.form.validate().then(async () => {
				const ok = await this.checkBindable();
				if (!ok) return;
				this.submitting = true;
				try {
					await ParentApi.bindPatient(this.form);
					uni.showToast({ title: '绑定成功', icon: 'success' });
					uni.$patientListRefresh = true;
					setTimeout(() => uni.navigateBack(), 800);
				} catch (e) {
					uni.showToast({ title: '绑定失败', icon: 'none' });
				} finally {
					this.submitting = false;
				}
			}).catch(() => {});
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
</style>
