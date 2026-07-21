<template>
	<view class="parent-page">
		<view class="top">
			<image class="logo-image" src="/static/images/tb/logo.png"></image>
			<view class="main-title">家长健康服务</view>
		</view>

		<!-- 当前就诊人切换 -->
		<view class="patient-bar">
			<view class="patient-info" @click="showPatientPicker = true">
				<text class="patient-label">当前就诊人：</text>
				<text class="patient-name">{{ currentPatient ? currentPatient.name : '未选择' }}</text>
				<text v-if="currentPatient" class="patient-meta">
					{{ formatSex(currentPatient.sex) }} · {{ currentPatient.age }}岁
				</text>
				<text class="iconfont icon-right"></text>
			</view>
			<text class="manage-btn" @click="toPatientManage">管理</text>
		</view>

		<!-- 功能菜单 -->
		<view class="menu-grid">
			<view
				v-for="(item, index) in menuItems"
				:key="index"
				class="menu-card"
				@click="goPage(item)"
			>
				<view class="menu-card-inner">
					<view class="menu-icon" :style="{ backgroundColor: item.bgColor }">
						<text class="icon-text">{{ item.icon }}</text>
					</view>
					<view class="menu-label">{{ item.label }}</view>
					<view class="menu-desc">{{ item.desc }}</view>
				</view>
			</view>
		</view>

		<!-- 就诊人选择弹窗 -->
		<u-popup :show="showPatientPicker" mode="bottom" @close="showPatientPicker = false" round="12">
			<view class="patient-popup">
				<view class="popup-title">选择就诊人</view>
				<view class="patient-list">
					<view
						v-for="p in patientList"
						:key="p.id"
						class="patient-item"
						:class="{ active: currentPatient && currentPatient.id === p.id }"
						@click="selectPatient(p)"
					>
						<view class="patient-item-l">
							<text class="p-name">{{ p.name }}</text>
							<text class="p-meta">{{ formatSex(p.sex) }} · {{ p.age }}岁 · {{ p.schoolName || '' }}</text>
						</view>
						<text v-if="currentPatient && currentPatient.id === p.id" class="iconfont icon-right active-check">✓</text>
					</view>
					<view v-if="!patientList.length" class="empty-text">暂无绑定的就诊人，请先添加</view>
				</view>
				<view class="popup-footer">
					<u-button type="primary" text="添加就诊人" @click="toPatientAdd" />
				</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
import * as ParentApi from '@/api/parent/index';

const PATIENT_CACHE_KEY = 'parent_current_patient';

export default {
	data() {
		return {
			patientList: [],
			currentPatient: null,
			showPatientPicker: false,
			menuItems: [
				{
					label: '自主报名',
					desc: '在线填写信息提交报名',
					icon: '报',
					bgColor: 'rgba(36, 93, 209, 0.1)',
					url: '/pages/parent/register/index',
					reqNo: '139-140',
					needPatient: true
				},
				{
					label: '体检报告查询',
					desc: '查看筛查结果与阳性解读',
					icon: '告',
					bgColor: 'rgba(80, 104, 242, 0.1)',
					url: '/pages/parent/report/index',
					reqNo: '141',
					needPatient: true
				},
				{
					label: '复筛通知单',
					desc: '查看复筛项目与注意事项',
					icon: '复',
					bgColor: 'rgba(240, 163, 41, 0.1)',
					url: '/pages/parent/rescreen-notice/index',
					reqNo: '142-143',
					needPatient: true
				},
				{
					label: '问卷填写',
					desc: '健康问卷与随访问卷',
					icon: '问',
					bgColor: 'rgba(51, 176, 19, 0.1)',
					url: '/pages/parent/questionnaire/index',
					reqNo: '144-145',
					needPatient: true
				},
				{
					label: '量表填写',
					desc: '心理筛查与健康评估',
					icon: '量',
					bgColor: 'rgba(223, 65, 65, 0.1)',
					url: '/pages/parent/scale/index',
					reqNo: '146-147',
					needPatient: true
				},
				{
					label: '健康宣教',
					desc: '科普知识与健康指导',
					icon: '健',
					bgColor: 'rgba(36, 93, 209, 0.1)',
					url: '/pages/parent/education/index',
					reqNo: '148-149',
					needPatient: false
				},
				{
					label: '就诊人管理',
					desc: '绑定多个受检者',
					icon: '诊',
					bgColor: 'rgba(80, 104, 242, 0.1)',
					url: '/pages/parent/patient/index',
					reqNo: '150-151',
					needPatient: false
				}
			]
		};
	},
	onShow() {
		this.loadPatients();
	},
	methods: {
		async loadPatients() {
			try {
				const res = await ParentApi.getBoundPatientList();
				this.patientList = res.list || res.data || res || [];
				// 恢复上次选中的就诊人
				const cache = uni.getStorageSync(PATIENT_CACHE_KEY);
				if (cache && this.patientList.find(p => p.id === cache.id)) {
					this.currentPatient = cache;
				} else if (this.patientList.length) {
					this.currentPatient = this.patientList[0];
				} else {
					this.currentPatient = null;
				}
			} catch (e) {
				this.patientList = [];
				this.currentPatient = null;
			}
		},
		selectPatient(p) {
			this.currentPatient = p;
			uni.setStorageSync(PATIENT_CACHE_KEY, p);
			this.showPatientPicker = false;
		},
		formatSex(sex) {
			if (sex === 1 || sex === '1' || sex === '男') return '男';
			if (sex === 2 || sex === '2' || sex === '女') return '女';
			return sex || '-';
		},
		goPage(item) {
			if (item.needPatient && !this.currentPatient) {
				uni.showToast({ title: '请先选择或绑定就诊人', icon: 'none' });
				setTimeout(() => {
					uni.navigateTo({ url: '/pages/parent/patient/add' });
				}, 1000);
				return;
			}
			uni.navigateTo({ url: item.url });
		},
		toPatientManage() {
			uni.navigateTo({ url: '/pages/parent/patient/index' });
		},
		toPatientAdd() {
			this.showPatientPicker = false;
			uni.navigateTo({ url: '/pages/parent/patient/add' });
		}
	}
};
</script>

<style scoped lang="scss">
.parent-page {
	min-height: 100vh;
	background: rgba(244, 247, 252, 1);
	.top {
		height: 110px;
		width: 100vw;
		background-color: #fff;
		position: relative;
		.logo-image {
			position: absolute;
			top: 6%;
			left: 3%;
			width: 18%;
			height: 8%;
		}
		.main-title {
			position: absolute;
			text-align: center;
			font-size: 35px;
			font-weight: 800;
			top: 5%;
			left: 24%;
			color: rgba(36, 93, 209, 1);
		}
	}
	.patient-bar {
		display: flex;
		align-items: center;
		justify-content: space-between;
		background: #fff;
		margin: 12px 16px;
		padding: 14px 18px;
		border-radius: 10px;
		box-shadow: 0 1px 4px rgba(36, 93, 209, 0.08);
		.patient-info {
			display: flex;
			align-items: center;
			flex: 1;
			.patient-label {
				font-size: 14px;
				color: rgba(102, 102, 102, 1);
			}
			.patient-name {
				font-size: 18px;
				font-weight: 600;
				color: rgba(36, 93, 209, 1);
				margin: 0 8px;
			}
			.patient-meta {
				font-size: 13px;
				color: rgba(153, 153, 153, 1);
			}
		}
		.manage-btn {
			font-size: 14px;
			color: rgba(36, 93, 209, 1);
			padding: 4px 10px;
			border: 1px solid rgba(36, 93, 209, 1);
			border-radius: 14px;
		}
	}
	.menu-grid {
		display: flex;
		flex-wrap: wrap;
		padding: 16px 16px 30px;
		justify-content: flex-start;
	}
	.menu-card {
		width: 31%;
		margin: 1.16%;
		box-sizing: border-box;
		.menu-card-inner {
			background-color: #fff;
			border-radius: 12px;
			padding: 20px 12px;
			display: flex;
			flex-direction: column;
			align-items: center;
			box-shadow: 0 2px 8px rgba(36, 93, 209, 0.08);
			transition: all 0.2s;
			&:active {
				transform: translateY(2px);
				box-shadow: 0 1px 4px rgba(36, 93, 209, 0.12);
			}
			.menu-icon {
				width: 60px;
				height: 60px;
				border-radius: 50%;
				display: flex;
				align-items: center;
				justify-content: center;
				margin-bottom: 10px;
				.icon-text {
					font-size: 26px;
					font-weight: bold;
					color: rgba(36, 93, 209, 1);
				}
			}
			.menu-label {
				font-size: 15px;
				font-weight: 600;
				color: rgba(51, 51, 51, 1);
				margin-bottom: 4px;
				text-align: center;
			}
			.menu-desc {
				font-size: 12px;
				color: rgba(153, 153, 153, 1);
				text-align: center;
			}
		}
	}
}
.patient-popup {
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
	.patient-list {
		max-height: 50vh;
		overflow-y: auto;
	}
	.patient-item {
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
		.patient-item-l {
			display: flex;
			flex-direction: column;
			.p-name {
				font-size: 16px;
				font-weight: 600;
				color: rgba(51, 51, 51, 1);
			}
			.p-meta {
				font-size: 12px;
				color: rgba(153, 153, 153, 1);
				margin-top: 2px;
			}
		}
		.active-check {
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
	.popup-footer {
		margin-top: 12px;
	}
}
</style>
