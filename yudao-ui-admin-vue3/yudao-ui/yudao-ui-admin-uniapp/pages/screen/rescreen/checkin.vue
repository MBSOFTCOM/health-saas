<template>
	<view class="checkin-page">
		<!-- 顶部 -->
		<view class="top-bar">
			<view class="batch-info">
				<text class="info-label">当前批次：</text>
				<text class="info-value">{{ batchName || '未选择' }}</text>
				<text class="change-btn" @click="showBatchPicker = true">切换</text>
			</view>
			<u-button type="primary" icon="scan" text="扫码复筛报到" @click="scanQrcode" :loading="scanning" />
		</view>

		<!-- 受检者信息 -->
		<view class="student-card" v-if="studentInfo">
			<view class="card-header">
				<text class="header-title">受检者信息</text>
				<text class="header-status" :class="checkinStatusClass">{{ checkinStatusText }}</text>
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
				<view class="info-row">
					<text class="info-item">身份核验：</text>
					<text class="verify-result" :class="verifyClass">{{ verifyText }}</text>
				</view>
			</view>
		</view>

		<view v-else class="empty-tip">
			<text class="empty-text">请扫描受检者二维码进行复筛报到</text>
		</view>

		<!-- 阳性结果与复筛项目 -->
		<view class="positive-card" v-if="studentInfo">
			<view class="card-title">初筛阳性结果与复筛项目</view>
			<view v-if="positiveItems.length === 0" class="empty-tip">暂无阳性结果</view>
			<view v-else class="positive-list">
				<view v-for="(item, idx) in positiveItems" :key="idx" class="positive-item">
					<view class="positive-header">
						<text class="positive-category">{{ item.category }}</text>
						<text class="positive-name">{{ item.itemName }}</text>
						<text class="positive-flag">阳性</text>
					</view>
					<view class="positive-body">
						<text class="positive-result">初筛结果：{{ item.initialResult }}</text>
						<text class="positive-rescreen" :class="getRescreenStatusClass(item.rescreenStatus)">
							复筛状态：{{ getRescreenStatusText(item.rescreenStatus) }}
						</text>
					</view>
					<view class="positive-actions" v-if="item.rescreenStatus === 'pending'">
						<u-button type="primary" size="mini" text="去复筛登记" @click="goRegister(item)" />
						<u-button type="success" size="mini" text="记录到检" @click="recordArrival(item)" style="margin-left: 8px" />
					</view>
				</view>
			</view>
		</view>

		<!-- 复筛状态汇总 -->
		<view class="status-summary-card" v-if="studentInfo">
			<view class="card-title">复筛状态汇总</view>
			<view class="summary-stats">
				<view class="stat-block">
					<text class="stat-num">{{ rescreenStatus.totalItems || 0 }}</text>
					<text class="stat-label">复筛项目</text>
				</view>
				<view class="stat-block">
					<text class="stat-num">{{ rescreenStatus.checkedIn || 0 }}</text>
					<text class="stat-label">已到检</text>
				</view>
				<view class="stat-block">
					<text class="stat-num">{{ rescreenStatus.completed || 0 }}</text>
					<text class="stat-label">已完成</text>
				</view>
				<view class="stat-block">
					<text class="stat-num">{{ rescreenStatus.pending || 0 }}</text>
					<text class="stat-label">待复筛</text>
				</view>
			</view>
		</view>

		<!-- 复筛报到记录列表 -->
		<view class="checkin-list-card">
			<view class="card-title">本批次复筛报到记录</view>
			<uni-table :loading="listLoading" stripe emptyText="暂无报到记录">
				<uni-tr>
					<uni-th width="60" align="center">序号</uni-th>
					<uni-th width="100" align="center">姓名</uni-th>
					<uni-th width="120" align="center">学校</uni-th>
					<uni-th width="100" align="center">复筛项目数</uni-th>
					<uni-th width="120" align="center">到检时间</uni-th>
					<uni-th width="100" align="center">报到状态</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in checkinList" :key="index">
					<uni-td align="center">{{ index + 1 + (pageCurrent - 1) * pageSize }}</uni-td>
					<uni-td align="center">{{ item.name }}</uni-td>
					<uni-td align="center">{{ item.schoolName }}</uni-td>
					<uni-td align="center">{{ item.rescreenItemCount || 0 }}</uni-td>
					<uni-td align="center">{{ item.checkinTime || '-' }}</uni-td>
					<uni-td align="center">
						<text class="status-tag" :class="getCheckinStatusClass(item.checkinStatus)">{{ getCheckinStatusText(item.checkinStatus) }}</text>
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
			scanning: false,
			positiveItems: [],
			rescreenStatus: {},
			listLoading: false,
			checkinList: [],
			pageCurrent: 1,
			pageSize: 10,
			total: 0
		};
	},
	computed: {
		checkinStatusText() {
			if (!this.studentInfo) return '';
			return this.studentInfo.checkedIn ? '已报到' : '未报到';
		},
		checkinStatusClass() {
			return this.studentInfo && this.studentInfo.checkedIn ? 'status-done' : 'status-pending';
		},
		verifyText() {
			if (!this.studentInfo) return '';
			return this.studentInfo.verified ? '通过' : '未通过';
		},
		verifyClass() {
			return this.studentInfo && this.studentInfo.verified ? 'verify-pass' : 'verify-fail';
		}
	},
	onLoad(e) {
		this.batchId = e.batchId || '';
		this.batchName = e.batchName ? decodeURIComponent(e.batchName) : '';
		this.loadBatchOptions();
		this.loadCheckinList();
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
			this.loadCheckinList();
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
				// 报到登记 + 身份核验
				const checkinRes = await RescreenApi.rescreenCheckin(qrcode, this.batchId);
				this.studentInfo = checkinRes.student || checkinRes;
				this.studentInfo.qrcode = qrcode;
				this.studentInfo.checkedIn = checkinRes.checkedIn;
				this.studentInfo.verified = checkinRes.verified;
				// 加载阳性结果与复筛项目
				await this.loadRescreenItems(qrcode);
				if (this.studentInfo.id) {
					await this.loadRescreenStatus(this.studentInfo.id);
				}
				this.loadCheckinList();
				uni.showToast({ title: this.studentInfo.checkedIn ? '报到成功' : '报到失败', icon: this.studentInfo.checkedIn ? 'success' : 'none' });
			} catch (e) {
				uni.showToast({ title: '二维码无效', icon: 'none' });
			}
		},
		async loadRescreenItems(qrcode) {
			try {
				const res = await RescreenApi.getRescreenItems(qrcode, this.batchId);
				this.positiveItems = res || [];
			} catch (e) {
				this.positiveItems = [];
			}
		},
		async loadRescreenStatus(studentId) {
			try {
				const res = await RescreenApi.getRescreenStatus(studentId, this.batchId);
				this.rescreenStatus = res || {};
			} catch (e) {
				this.rescreenStatus = {};
			}
		},
		async loadCheckinList() {
			if (!this.batchId) return;
			this.listLoading = true;
			try {
				const res = await RescreenApi.getRescreenCheckinList({
					batchId: this.batchId,
					pageNo: this.pageCurrent,
					pageSize: this.pageSize
				});
				this.checkinList = res.list || res.data || [];
				this.total = res.total || 0;
			} catch (e) {
				this.checkinList = [];
				this.total = 0;
			} finally {
				this.listLoading = false;
			}
		},
		changePage(e) {
			this.pageCurrent = e.current;
			this.loadCheckinList();
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
		getCheckinStatusText(s) {
			const map = { checkedIn: '已报到', completed: '已完成', pending: '待到检' };
			return map[s] || s;
		},
		getCheckinStatusClass(s) {
			if (s === 'completed') return 'tag-done';
			if (s === 'checkedIn') return 'tag-doing';
			return 'tag-pending';
		},
		goRegister(item) {
			uni.navigateTo({
				url: '/pages/screen/rescreen/register?qrcode=' + this.studentInfo.qrcode + '&batchId=' + this.batchId
			});
		},
		async recordArrival(item) {
			try {
				await RescreenApi.recordRescreenArrival({
					studentId: this.studentInfo.id,
					batchId: this.batchId,
					itemId: item.itemId,
					itemName: item.itemName,
					arrivalTime: this.formatNow()
				});
				uni.showToast({ title: '到检记录已保存', icon: 'success' });
				this.loadRescreenItems(this.studentInfo.qrcode);
				this.loadRescreenStatus(this.studentInfo.id);
			} catch (e) {
				uni.showToast({ title: '记录失败', icon: 'none' });
			}
		},
		formatNow() {
			const d = new Date();
			const pad = (n) => String(n).padStart(2, '0');
			return d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate()) + ' ' + pad(d.getHours()) + ':' + pad(d.getMinutes()) + ':' + pad(d.getSeconds());
		}
	}
};
</script>

<style scoped lang="scss">
.checkin-page {
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
	.card-header { display: flex; justify-content: space-between; align-items: center; padding: 12px 16px; background: rgba(36, 93, 209, 0.04); border-bottom: 1px solid rgba(238, 238, 238, 1);
		.header-title { font-size: 16px; font-weight: 600; color: rgba(51, 51, 51, 1); }
		.header-status { padding: 2px 10px; border-radius: 12px; font-size: 12px; }
		.status-doing { background: rgba(240, 163, 41, 0.1); color: rgba(240, 163, 41, 1); }
		.status-done { background: rgba(51, 176, 19, 0.1); color: rgba(51, 176, 19, 1); }
		.status-pending { background: rgba(153, 153, 153, 0.1); color: rgba(153, 153, 153, 1); }
	}
	.card-body { padding: 12px 16px;
		.info-row { display: flex; gap: 24px; margin-bottom: 6px; font-size: 14px;
			.info-item { color: rgba(51, 51, 51, 1); }
			.verify-result { font-weight: 600; &.verify-pass { color: rgba(51, 176, 19, 1); } &.verify-fail { color: rgba(223, 65, 65, 1); } }
		}
	}
}
.empty-tip { background: #fff; border-radius: 8px; padding: 60px 0; text-align: center;
	.empty-text { color: rgba(153, 153, 153, 1); font-size: 15px; } }
.positive-card, .status-summary-card, .checkin-list-card {
	background: #fff;
	border-radius: 8px;
	padding: 16px;
	margin-bottom: 12px;
	box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
	.card-title { font-size: 16px; font-weight: 600; color: rgba(51, 51, 51, 1); margin-bottom: 12px; border-left: 3px solid rgba(36, 93, 209, 1); padding-left: 8px; }
}
.positive-list {
	.positive-item { padding: 12px; background: rgba(244, 247, 252, 1); border-radius: 6px; margin-bottom: 10px;
		.positive-header { display: flex; gap: 12px; align-items: center; margin-bottom: 8px;
			.positive-category { color: rgba(102, 102, 102, 1); font-size: 13px; }
			.positive-name { color: rgba(51, 51, 51, 1); font-size: 15px; font-weight: 600; }
			.positive-flag { background: rgba(223, 65, 65, 1); color: #fff; padding: 1px 6px; border-radius: 3px; font-size: 11px; }
		}
		.positive-body { display: flex; gap: 24px; margin-bottom: 8px; font-size: 13px;
			.positive-result { color: rgba(223, 65, 65, 1); }
		}
		.positive-actions { display: flex; }
	}
}
.summary-stats { display: flex; gap: 12px;
	.stat-block { flex: 1; text-align: center; padding: 16px 8px; border-radius: 6px; background: rgba(244, 247, 252, 1);
		.stat-num { display: block; font-size: 24px; font-weight: 700; color: rgba(36, 93, 209, 1); margin-bottom: 4px; }
		.stat-label { font-size: 13px; color: rgba(102, 102, 102, 1); }
	}
}
.status-tag { padding: 2px 8px; border-radius: 4px; font-size: 12px;
	&.tag-done { background: rgba(51, 176, 19, 0.1); color: rgba(51, 176, 19, 1); }
	&.tag-pending { background: rgba(240, 163, 41, 0.1); color: rgba(240, 163, 41, 1); }
	&.tag-doing { background: rgba(36, 93, 209, 0.1); color: rgba(36, 93, 209, 1); }
	&.tag-abnormal { background: rgba(223, 65, 65, 0.1); color: rgba(223, 65, 65, 1); }
}
.pagination-box { margin-top: 16px; display: flex; justify-content: flex-end; }
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
