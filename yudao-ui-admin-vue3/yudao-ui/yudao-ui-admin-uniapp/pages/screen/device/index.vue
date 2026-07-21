<template>
	<view class="device-page">
		<!-- 顶部操作 -->
		<view class="top-bar">
			<view class="filter-row">
				<view class="filter-item">
					<text class="filter-label">品牌</text>
					<uni-easyinput v-model="query.brand" placeholder="品牌" clearable style="width: 140px" />
				</view>
				<view class="filter-item">
					<text class="filter-label">型号</text>
					<uni-easyinput v-model="query.model" placeholder="型号" clearable style="width: 140px" />
				</view>
				<view class="filter-item">
					<text class="filter-label">状态</text>
					<uni-data-select v-model="query.status" :localdata="statusOptions" placeholder="全部" style="width: 140px" />
				</view>
				<view class="filter-actions">
					<u-button type="primary" text="查询" @click="handleSearch" :loading="loading" />
					<u-button text="重置" @click="handleReset" style="margin-left: 10px" />
					<u-button type="success" icon="plus" text="配对新设备" @click="showPairPopup = true" style="margin-left: 10px" />
				</view>
			</view>
		</view>

		<!-- 设备列表 -->
		<view class="device-list">
			<uni-table :loading="loading" stripe emptyText="暂无设备">
				<uni-tr>
					<uni-th width="60" align="center">序号</uni-th>
					<uni-th width="120" align="center">设备名称</uni-th>
					<uni-th width="100" align="center">品牌</uni-th>
					<uni-th width="120" align="center">型号</uni-th>
					<uni-th width="120" align="center">设备类型</uni-th>
					<uni-th width="100" align="center">连接状态</uni-th>
					<uni-th width="100" align="center">同步状态</uni-th>
					<uni-th width="140" align="center">最后同步</uni-th>
					<uni-th width="220" align="center">操作</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in deviceList" :key="item.id">
					<uni-td align="center">{{ index + 1 + (pageCurrent - 1) * pageSize }}</uni-td>
					<uni-td align="center">{{ item.deviceName }}</uni-td>
					<uni-td align="center">{{ item.brand }}</uni-td>
					<uni-td align="center">{{ item.model }}</uni-td>
					<uni-td align="center">{{ formatDeviceType(item.deviceType) }}</uni-td>
					<uni-td align="center">
						<text class="status-tag" :class="getConnStatusClass(item.connStatus)">{{ getConnStatusText(item.connStatus) }}</text>
					</uni-td>
					<uni-td align="center">
						<text class="status-tag" :class="getSyncStatusClass(item.syncStatus)">{{ getSyncStatusText(item.syncStatus) }}</text>
					</uni-td>
					<uni-td align="center">{{ item.lastSyncTime || '-' }}</uni-td>
					<uni-td align="center">
						<view class="op-btns">
							<text class="op-btn op-check" @click="checkStatus(item)">检测</text>
							<text class="op-btn op-sync" @click="syncData(item)">同步</text>
							<text class="op-btn op-verify" @click="verifySync(item)">校验</text>
							<text class="op-btn op-alert" @click="viewAlerts(item)">提醒</text>
						</view>
					</uni-td>
				</uni-tr>
			</uni-table>
			<view class="pagination-box">
				<uni-pagination
					show-icon
					:page-size="pageSize"
					:current="pageCurrent"
					:total="total"
					@change="changePage"
				/>
			</view>
		</view>

		<!-- 配对弹窗 -->
		<u-popup :show="showPairPopup" mode="center" @close="showPairPopup = false" round="12">
			<view class="pair-popup">
				<view class="popup-title">配对新设备</view>
				<uni-forms :model="pairForm" label-width="100" label-position="left">
					<uni-forms-item label="品牌" required>
						<uni-easyinput v-model="pairForm.brand" placeholder="如：海泰" />
					</uni-forms-item>
					<uni-forms-item label="型号" required>
						<uni-easyinput v-model="pairForm.model" placeholder="如：HT-100" />
					</uni-forms-item>
					<uni-forms-item label="设备类型" required>
						<uni-data-select v-model="pairForm.deviceType" :localdata="deviceTypeOptions" placeholder="请选择" />
					</uni-forms-item>
					<uni-forms-item label="设备名称">
						<uni-easyinput v-model="pairForm.deviceName" placeholder="自定义名称" />
					</uni-forms-item>
					<uni-forms-item label="蓝牙MAC">
						<uni-easyinput v-model="pairForm.mac" placeholder="如：00:11:22:33:44:55" />
					</uni-forms-item>
				</uni-forms>
				<view class="pair-actions">
					<u-button text="自动识别" @click="autoDetect" :loading="detecting" />
					<u-button type="primary" text="开始配对" @click="startPair" :loading="pairing" style="margin-left: 10px" />
					<u-button text="取消" @click="showPairPopup = false" style="margin-left: 10px" />
				</view>
			</view>
		</u-popup>

		<!-- 异常提醒弹窗 -->
		<u-popup :show="showAlertPopup" mode="center" @close="showAlertPopup = false" round="12">
			<view class="alert-popup">
				<view class="popup-title">设备异常提醒</view>
				<view class="alert-content">
					<view v-if="alertList.length === 0" class="empty-tip">暂无异常提醒</view>
					<view v-else>
						<view v-for="(alert, idx) in alertList" :key="idx" class="alert-item">
							<view class="alert-header">
								<text class="alert-level" :class="getAlertLevelClass(alert.level)">{{ getAlertLevelText(alert.level) }}</text>
								<text class="alert-time">{{ alert.time }}</text>
							</view>
							<text class="alert-msg">{{ alert.message }}</text>
						</view>
					</view>
				</view>
				<view class="popup-footer">
					<u-button text="关闭" @click="showAlertPopup = false" />
				</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
import * as DeviceApi from '@/api/screen/device';

export default {
	data() {
		return {
			loading: false,
			query: { brand: '', model: '', status: '' },
			deviceList: [],
			pageCurrent: 1,
			pageSize: 10,
			total: 0,
			showPairPopup: false,
			pairing: false,
			detecting: false,
			pairForm: { brand: '', model: '', deviceType: '', deviceName: '', mac: '' },
			showAlertPopup: false,
			alertList: [],
			statusOptions: [
				{ value: '', text: '全部' },
				{ value: 'online', text: '在线' },
				{ value: 'offline', text: '离线' },
				{ value: 'error', text: '异常' }
			],
			deviceTypeOptions: [
				{ value: 'stadiometer', text: '身高体重秤' },
				{ value: 'visionScreener', text: '视力筛查仪' },
				{ value: 'scoliosometer', text: '脊柱侧弯检测仪' },
				{ value: 'optometer', text: '验光仪' },
				{ value: 'sphygmomanometer', text: '血压计' },
				{ value: 'spirometer', text: '肺活量仪' },
				{ value: 'footPressure', text: '足底压力检测仪' }
			]
		};
	},
	onLoad() {
		this.loadList();
	},
	methods: {
		async loadList() {
			this.loading = true;
			try {
				const res = await DeviceApi.getDevicePage({
					pageNo: this.pageCurrent,
					pageSize: this.pageSize,
					...this.query
				});
				this.deviceList = res.list || res.data || [];
				this.total = res.total || 0;
			} catch (e) {
				this.deviceList = [];
				this.total = 0;
			} finally {
				this.loading = false;
			}
		},
		handleSearch() {
			this.pageCurrent = 1;
			this.loadList();
		},
		handleReset() {
			this.query = { brand: '', model: '', status: '' };
			this.pageCurrent = 1;
			this.loadList();
		},
		changePage(e) {
			this.pageCurrent = e.current;
			this.loadList();
		},
		formatDeviceType(type) {
			const opt = this.deviceTypeOptions.find(o => o.value === type);
			return opt ? opt.text : type || '-';
		},
		getConnStatusText(s) {
			const map = { online: '在线', offline: '离线', error: '异常' };
			return map[s] || s || '-';
		},
		getConnStatusClass(s) {
			if (s === 'online') return 'tag-done';
			if (s === 'error') return 'tag-abnormal';
			return 'tag-pending';
		},
		getSyncStatusText(s) {
			const map = { synced: '已同步', pending: '待同步', failed: '同步失败', syncing: '同步中' };
			return map[s] || s || '-';
		},
		getSyncStatusClass(s) {
			if (s === 'synced') return 'tag-done';
			if (s === 'failed') return 'tag-abnormal';
			if (s === 'syncing') return 'tag-doing';
			return 'tag-pending';
		},
		getAlertLevelText(l) {
			const map = { info: '提示', warning: '警告', error: '错误' };
			return map[l] || l;
		},
		getAlertLevelClass(l) {
			if (l === 'error') return 'level-error';
			if (l === 'warning') return 'level-warning';
			return 'level-info';
		},
		async checkStatus(item) {
			uni.showLoading({ title: '检测中...' });
			try {
				const res = await DeviceApi.checkDeviceStatus(item.id);
				uni.hideLoading();
				uni.showModal({
					title: '设备状态',
					content: '连接状态：' + this.getConnStatusText(res.connStatus) + '\n电池：' + (res.battery || '-') + '%\n信号：' + (res.signal || '-'),
					showCancel: false
				});
				this.loadList();
			} catch (e) {
				uni.hideLoading();
				uni.showToast({ title: '检测失败', icon: 'none' });
			}
		},
		async syncData(item) {
			uni.showLoading({ title: '同步中...' });
			try {
				await DeviceApi.syncDeviceData({ deviceId: item.id });
				uni.hideLoading();
				uni.showToast({ title: '同步成功', icon: 'success' });
				this.loadList();
			} catch (e) {
				uni.hideLoading();
				uni.showToast({ title: '同步失败', icon: 'none' });
			}
		},
		async verifySync(item) {
			uni.showLoading({ title: '校验中...' });
			try {
				const res = await DeviceApi.verifySyncData({ deviceId: item.id });
				uni.hideLoading();
				if (res.passed) {
					uni.showToast({ title: '校验通过', icon: 'success' });
				} else {
					uni.showModal({
						title: '校验未通过',
						content: '差异：' + (res.diff || '数据不一致'),
						showCancel: false
					});
				}
			} catch (e) {
				uni.hideLoading();
				uni.showToast({ title: '校验失败', icon: 'none' });
			}
		},
		async viewAlerts(item) {
			try {
				const res = await DeviceApi.getDeviceAlerts(item.id);
				this.alertList = res || [];
				this.showAlertPopup = true;
			} catch (e) {
				uni.showToast({ title: '加载异常提醒失败', icon: 'none' });
			}
		},
		async autoDetect() {
			this.detecting = true;
			try {
				// #ifdef APP-PLUS
				uni.showToast({ title: '请将设备靠近手机', icon: 'none' });
				// 模拟蓝牙搜索
				setTimeout(() => {
					this.pairForm.brand = this.pairForm.brand || '海泰';
					this.pairForm.model = this.pairForm.model || 'HT-100';
					this.pairForm.mac = this.pairForm.mac || '00:11:22:33:44:55';
					uni.showToast({ title: '识别成功', icon: 'success' });
					this.detecting = false;
				}, 1500);
				// #endif
				// #ifndef APP-PLUS
				setTimeout(() => {
					this.pairForm.brand = this.pairForm.brand || '海泰';
					this.pairForm.model = this.pairForm.model || 'HT-100';
					this.pairForm.mac = this.pairForm.mac || '00:11:22:33:44:55';
					uni.showToast({ title: '识别成功（模拟）', icon: 'success' });
					this.detecting = false;
				}, 1000);
				// #endif
			} catch (e) {
				this.detecting = false;
				uni.showToast({ title: '识别失败', icon: 'none' });
			}
		},
		async startPair() {
			if (!this.pairForm.brand || !this.pairForm.model || !this.pairForm.deviceType) {
				uni.showToast({ title: '请填写完整信息', icon: 'none' });
				return;
			}
			this.pairing = true;
			try {
				await DeviceApi.pairDevice(this.pairForm.brand, this.pairForm.model, this.pairForm);
				uni.showToast({ title: '配对成功', icon: 'success' });
				this.showPairPopup = false;
				this.pairForm = { brand: '', model: '', deviceType: '', deviceName: '', mac: '' };
				this.loadList();
			} catch (e) {
				uni.showToast({ title: '配对失败', icon: 'none' });
			} finally {
				this.pairing = false;
			}
		}
	}
};
</script>

<style scoped lang="scss">
.device-page {
	padding: 16px;
	background: rgba(244, 247, 252, 1);
	min-height: 100vh;
}
.top-bar {
	background: #fff;
	border-radius: 8px;
	padding: 16px;
	margin-bottom: 16px;
	box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
	.filter-row {
		display: flex;
		flex-wrap: wrap;
		align-items: center;
		gap: 16px;
	}
	.filter-item {
		display: flex;
		align-items: center;
		.filter-label { font-size: 14px; color: rgba(102, 102, 102, 1); margin-right: 8px; white-space: nowrap; }
	}
	.filter-actions { margin-left: auto; display: flex; }
}
.device-list {
	background: #fff;
	border-radius: 8px;
	padding: 16px;
	box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
}
.op-btns {
	display: flex;
	justify-content: center;
	gap: 4px;
	flex-wrap: wrap;
	.op-btn {
		padding: 3px 6px;
		font-size: 11px;
		border-radius: 4px;
		cursor: pointer;
	}
	.op-check { color: rgba(36, 93, 209, 1); border: 1px solid rgba(36, 93, 209, 1); }
	.op-sync { color: rgba(51, 176, 19, 1); border: 1px solid rgba(51, 176, 19, 1); }
	.op-verify { color: rgba(240, 163, 41, 1); border: 1px solid rgba(240, 163, 41, 1); }
	.op-alert { color: rgba(223, 65, 65, 1); border: 1px solid rgba(223, 65, 65, 1); }
}
.status-tag {
	padding: 2px 8px;
	border-radius: 4px;
	font-size: 12px;
	&.tag-done { background: rgba(51, 176, 19, 0.1); color: rgba(51, 176, 19, 1); }
	&.tag-pending { background: rgba(240, 163, 41, 0.1); color: rgba(240, 163, 41, 1); }
	&.tag-doing { background: rgba(36, 93, 209, 0.1); color: rgba(36, 93, 209, 1); }
	&.tag-abnormal { background: rgba(223, 65, 65, 0.1); color: rgba(223, 65, 65, 1); }
}
.pagination-box { margin-top: 16px; display: flex; justify-content: flex-end; }
.pair-popup {
	width: 70vw;
	max-width: 600px;
	background: #fff;
	border-radius: 12px;
	padding: 20px;
	.popup-title { font-size: 18px; font-weight: 600; text-align: center; margin-bottom: 16px; color: rgba(36, 93, 209, 1); }
	.pair-actions { display: flex; justify-content: center; margin-top: 16px; padding-top: 16px; border-top: 1px solid rgba(238, 238, 238, 1); }
}
.alert-popup {
	width: 70vw;
	max-width: 600px;
	background: #fff;
	border-radius: 12px;
	padding: 20px;
	.popup-title { font-size: 18px; font-weight: 600; text-align: center; margin-bottom: 16px; color: rgba(223, 65, 65, 1); }
	.alert-content { max-height: 50vh; overflow-y: auto;
		.alert-item { padding: 10px 0; border-bottom: 1px dashed rgba(238, 238, 238, 1);
			.alert-header { display: flex; justify-content: space-between; margin-bottom: 6px;
				.alert-level { padding: 2px 8px; border-radius: 4px; font-size: 12px;
					&.level-info { background: rgba(36, 93, 209, 0.1); color: rgba(36, 93, 209, 1); }
					&.level-warning { background: rgba(240, 163, 41, 0.1); color: rgba(240, 163, 41, 1); }
					&.level-error { background: rgba(223, 65, 65, 0.1); color: rgba(223, 65, 65, 1); }
				}
				.alert-time { font-size: 12px; color: rgba(153, 153, 153, 1); }
			}
			.alert-msg { font-size: 14px; color: rgba(51, 51, 51, 1); }
		}
		.empty-tip { text-align: center; color: rgba(51, 176, 19, 1); padding: 20px 0; }
	}
	.popup-footer { margin-top: 16px; display: flex; justify-content: center; }
}
</style>
