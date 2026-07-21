<template>
	<view class="patient-page">
		<view class="page-header">
			<view class="header-title">就诊人管理</view>
			<view class="header-desc">支持绑定多个受检者，家庭维度统一健康管理</view>
		</view>

		<view class="patient-list" v-if="patientList.length">
			<view class="patient-card" v-for="p in patientList" :key="p.id">
				<view class="card-top">
					<view class="card-avatar">{{ p.name ? p.name.charAt(0) : '?' }}</view>
					<view class="card-info">
						<view class="info-name">
							<text class="name-text">{{ p.name }}</text>
							<text class="sex-tag" :class="p.sex === 1 || p.sex === '男' ? 'tag-male' : 'tag-female'">
								{{ formatSex(p.sex) }}
							</text>
						</view>
						<view class="info-meta">
							<text class="meta-item">{{ p.age }}岁</text>
							<text class="meta-item" v-if="p.schoolName">{{ p.schoolName }}</text>
							<text class="meta-item" v-if="p.grade">{{ p.grade }}{{ p.className }}</text>
						</view>
						<view class="info-meta" v-if="p.idCard">
							<text class="meta-item">身份证：{{ maskIdCard(p.idCard) }}</text>
						</view>
					</view>
				</view>
				<view class="card-actions">
					<text class="op-btn op-report" @click="toReport(p)">体检报告</text>
					<text class="op-btn op-rescreen" @click="toRescreen(p)">复筛通知</text>
					<text class="op-btn op-unbind" @click="handleUnbind(p)">解绑</text>
				</view>
			</view>
		</view>

		<view v-else class="empty-state">
			<text class="empty-text">暂未绑定任何就诊人</text>
			<text class="empty-tip">点击下方按钮绑定受检者</text>
		</view>

		<view class="footer-btn">
			<u-button type="primary" icon="plus" text="绑定就诊人" @click="toAdd" />
		</view>
	</view>
</template>

<script>
import * as ParentApi from '@/api/parent/index';

export default {
	data() {
		return {
			patientList: []
		};
	},
	onShow() {
		if (uni.$patientListRefresh) {
			uni.$patientListRefresh = false;
		}
		this.loadList();
	},
	methods: {
		async loadList() {
			try {
				const res = await ParentApi.getBoundPatientList();
				this.patientList = res.list || res.data || res || [];
			} catch (e) {
				this.patientList = [];
			}
		},
		formatSex(sex) {
			if (sex === 1 || sex === '1' || sex === '男') return '男';
			if (sex === 2 || sex === '2' || sex === '女') return '女';
			return sex || '-';
		},
		maskIdCard(idCard) {
			if (!idCard || idCard.length < 8) return idCard;
			return idCard.substring(0, 4) + '********' + idCard.substring(idCard.length - 4);
		},
		toAdd() {
			uni.navigateTo({ url: '/pages/parent/patient/add' });
		},
		toReport(p) {
			uni.setStorageSync('parent_current_patient', p);
			uni.navigateTo({ url: '/pages/parent/report/index?patientId=' + p.id });
		},
		toRescreen(p) {
			uni.setStorageSync('parent_current_patient', p);
			uni.navigateTo({ url: '/pages/parent/rescreen-notice/index?patientId=' + p.id });
		},
		handleUnbind(p) {
			uni.showModal({
				title: '确认解绑',
				content: `确定解绑就诊人「${p.name}」吗？解绑后该就诊人相关业务将不再展示。`,
				success: async (r) => {
					if (!r.confirm) return;
					try {
						await ParentApi.unbindPatient(p.id);
						uni.showToast({ title: '已解绑', icon: 'success' });
						uni.$patientListRefresh = true;
						this.loadList();
					} catch (e) {
						uni.showToast({ title: '解绑失败', icon: 'none' });
					}
				}
			});
		}
	}
};
</script>

<style scoped lang="scss">
.patient-page {
	min-height: 100vh;
	background: rgba(244, 247, 252, 1);
	padding: 16px;
}
.page-header {
	background: #fff;
	border-radius: 10px;
	padding: 18px 20px;
	margin-bottom: 16px;
	box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
	.header-title {
		font-size: 20px;
		font-weight: 600;
		color: rgba(36, 93, 209, 1);
	}
	.header-desc {
		font-size: 13px;
		color: rgba(153, 153, 153, 1);
		margin-top: 4px;
	}
}
.patient-list {
	.patient-card {
		background: #fff;
		border-radius: 10px;
		padding: 16px;
		margin-bottom: 12px;
		box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
		.card-top {
			display: flex;
			align-items: center;
		}
		.card-avatar {
			width: 50px;
			height: 50px;
			border-radius: 50%;
			background: linear-gradient(135deg, rgba(36, 93, 209, 1), rgba(80, 104, 242, 1));
			color: #fff;
			font-size: 22px;
			font-weight: 600;
			display: flex;
			align-items: center;
			justify-content: center;
			margin-right: 14px;
		}
		.card-info {
			flex: 1;
			.info-name {
				display: flex;
				align-items: center;
				.name-text {
					font-size: 17px;
					font-weight: 600;
					color: rgba(51, 51, 51, 1);
					margin-right: 8px;
				}
				.sex-tag {
					font-size: 11px;
					padding: 1px 6px;
					border-radius: 8px;
				}
				.tag-male {
					background: rgba(36, 93, 209, 0.1);
					color: rgba(36, 93, 209, 1);
				}
				.tag-female {
					background: rgba(223, 65, 65, 0.1);
					color: rgba(223, 65, 65, 1);
				}
			}
			.info-meta {
				margin-top: 4px;
				.meta-item {
					font-size: 13px;
					color: rgba(102, 102, 102, 1);
					margin-right: 12px;
				}
			}
		}
		.card-actions {
			display: flex;
			justify-content: flex-end;
			margin-top: 12px;
			padding-top: 10px;
			border-top: 1px solid rgba(238, 238, 238, 1);
			gap: 8px;
			.op-btn {
				padding: 5px 12px;
				font-size: 13px;
				border-radius: 14px;
				cursor: pointer;
			}
			.op-report {
				color: rgba(36, 93, 209, 1);
				border: 1px solid rgba(36, 93, 209, 1);
			}
			.op-rescreen {
				color: rgba(240, 163, 41, 1);
				border: 1px solid rgba(240, 163, 41, 1);
			}
			.op-unbind {
				color: rgba(223, 65, 65, 1);
				border: 1px solid rgba(223, 65, 65, 1);
			}
		}
	}
}
.empty-state {
	background: #fff;
	border-radius: 10px;
	padding: 60px 20px;
	text-align: center;
	.empty-text {
		display: block;
		font-size: 16px;
		color: rgba(102, 102, 102, 1);
	}
	.empty-tip {
		display: block;
		font-size: 13px;
		color: rgba(153, 153, 153, 1);
		margin-top: 8px;
	}
}
.footer-btn {
	margin-top: 20px;
}
</style>
