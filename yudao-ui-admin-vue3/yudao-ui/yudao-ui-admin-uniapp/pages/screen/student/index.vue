<template>
	<view class="student-page">
		<!-- 筛选区 -->
		<view class="filter-bar">
			<view class="filter-row">
				<view class="filter-item">
					<text class="filter-label">学校</text>
					<uni-easyinput v-model="query.schoolName" placeholder="学校名称" clearable style="width: 180px" />
				</view>
				<view class="filter-item">
					<text class="filter-label">年级</text>
					<uni-easyinput v-model="query.grade" placeholder="年级" clearable style="width: 120px" />
				</view>
				<view class="filter-item">
					<text class="filter-label">班级</text>
					<uni-easyinput v-model="query.className" placeholder="班级" clearable style="width: 120px" />
				</view>
				<view class="filter-item">
					<text class="filter-label">姓名</text>
					<uni-easyinput v-model="query.name" placeholder="学生姓名" clearable style="width: 140px" />
				</view>
				<view class="filter-actions">
					<u-button type="primary" text="查询" @click="handleSearch" :loading="loading" />
					<u-button text="重置" @click="handleReset" style="margin-left: 10px" />
					<u-button type="success" icon="plus" text="新增" @click="toAdd" style="margin-left: 10px" />
				</view>
			</view>
		</view>

		<!-- 学生列表 -->
		<view class="student-list">
			<uni-table :loading="loading" stripe emptyText="暂无学生数据">
				<uni-tr>
					<uni-th width="60" align="center">序号</uni-th>
					<uni-th width="100" align="center">姓名</uni-th>
					<uni-th width="60" align="center">性别</uni-th>
					<uni-th width="60" align="center">年龄</uni-th>
					<uni-th width="150" align="center">学校</uni-th>
					<uni-th width="80" align="center">年级</uni-th>
					<uni-th width="80" align="center">班级</uni-th>
					<uni-th width="120" align="center">二维码</uni-th>
					<uni-th width="180" align="center">操作</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in studentList" :key="item.id">
					<uni-td align="center">{{ index + 1 + (pageCurrent - 1) * pageSize }}</uni-td>
					<uni-td align="center">{{ item.name }}</uni-td>
					<uni-td align="center">{{ formatSex(item.sex) }}</uni-td>
					<uni-td align="center">{{ item.age }}</uni-td>
					<uni-td align="center">{{ item.schoolName }}</uni-td>
					<uni-td align="center">{{ item.grade }}</uni-td>
					<uni-td align="center">{{ item.className }}</uni-td>
					<uni-td align="center">
						<text class="qrcode-text" @click="viewQrcode(item)">{{ item.qrcode || '查看' }}</text>
					</uni-td>
					<uni-td align="center">
						<view class="op-btns">
							<text class="op-btn op-edit" @click="toEdit(item)">编辑</text>
							<text class="op-btn op-qr" @click="viewQrcode(item)">二维码</text>
							<text class="op-btn op-print" @click="printQrcode(item)">打印</text>
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

		<!-- 二维码弹窗 -->
		<u-popup :show="showQrcode" mode="center" @close="closeQrcode" round="12">
			<view class="qrcode-popup" v-if="currentStudent">
				<view class="popup-title">受检者二维码</view>
				<view class="student-info">
					<text class="info-text">姓名：{{ currentStudent.name }}</text>
					<text class="info-text">学校：{{ currentStudent.schoolName }}</text>
					<text class="info-text">班级：{{ currentStudent.grade }} {{ currentStudent.className }}</text>
				</view>
				<view class="qrcode-image">
					<canvas canvas-id="qrcodeCanvas" id="qrcodeCanvas" style="width: 200px; height: 200px;"></canvas>
				</view>
				<view class="qrcode-value">编号：{{ currentStudent.qrcode }}</view>
				<view class="popup-footer">
					<u-button text="关闭" @click="closeQrcode" />
					<u-button type="primary" text="蓝牙打印" @click="printQrcode(currentStudent)" style="margin-left: 10px" />
				</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
import * as StudentApi from '@/api/screen/student';
import uQRCode from '@/uni_modules/Sansnn-uQRCode';

export default {
	data() {
		return {
			loading: false,
			query: {
				schoolName: '',
				grade: '',
				className: '',
				name: ''
			},
			studentList: [],
			pageCurrent: 1,
			pageSize: 10,
			total: 0,
			showQrcode: false,
			currentStudent: null
		};
	},
	onLoad() {
		this.loadList();
	},
	onShow() {
		// 新增/编辑后刷新
		if (uni.$studentListRefresh) {
			uni.$studentListRefresh = false;
			this.loadList();
		}
	},
	methods: {
		async loadList() {
			this.loading = true;
			try {
				const res = await StudentApi.getStudentPage({
					pageNo: this.pageCurrent,
					pageSize: this.pageSize,
					...this.query
				});
				this.studentList = res.list || res.data || [];
				this.total = res.total || 0;
			} catch (e) {
				this.studentList = [];
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
			this.query = { schoolName: '', grade: '', className: '', name: '' };
			this.pageCurrent = 1;
			this.loadList();
		},
		changePage(e) {
			this.pageCurrent = e.current;
			this.loadList();
		},
		formatSex(sex) {
			if (sex === 1 || sex === '1' || sex === '男') return '男';
			if (sex === 2 || sex === '2' || sex === '女') return '女';
			return sex || '-';
		},
		toAdd() {
			uni.navigateTo({ url: '/pages/screen/student/add' });
		},
		toEdit(item) {
			uni.navigateTo({ url: '/pages/screen/student/add?id=' + item.id });
		},
		viewQrcode(item) {
			this.currentStudent = item;
			this.showQrcode = true;
			this.$nextTick(() => {
				this.drawQrcode(item.qrcode || String(item.id));
			});
		},
		drawQrcode(text) {
			// #ifdef APP-PLUS || H5
			const ctx = uni.createCanvasContext('qrcodeCanvas');
			uQRCode.make({
				canvasContext: ctx,
				componentInstance: this,
				text: text,
				size: 200,
				margin: 10,
				backgroundColor: '#ffffff',
				foregroundColor: '#333333',
				errorCorrectLevel: uQRCode.errorCorrectLevel.H,
				success: () => {
					ctx.draw();
				}
			});
			// #endif
		},
		closeQrcode() {
			this.showQrcode = false;
			this.currentStudent = null;
		},
		async printQrcode(item) {
			// #ifdef APP-PLUS
			uni.showLoading({ title: '连接蓝牙打印...' });
			try {
				// 调用蓝牙打印插件
				const printPlugin = uni.requireNativePlugin('BluetoothPrinter');
				if (printPlugin) {
					printPlugin.printQrcode({
						content: item.qrcode || String(item.id),
						name: item.name,
						school: item.schoolName,
						className: item.grade + item.className
					}, (res) => {
						uni.hideLoading();
						if (res.code === 0) {
							uni.showToast({ title: '打印成功', icon: 'success' });
						} else {
							uni.showToast({ title: '打印失败：' + res.msg, icon: 'none' });
						}
					});
				} else {
					uni.hideLoading();
					uni.showToast({ title: '未检测到蓝牙打印插件', icon: 'none' });
				}
			} catch (e) {
				uni.hideLoading();
				uni.showToast({ title: '蓝牙打印异常', icon: 'none' });
			}
			// #endif
			// #ifndef APP-PLUS
			uni.showToast({ title: '请在App环境使用蓝牙打印', icon: 'none' });
			// #endif
		}
	}
};
</script>

<style scoped lang="scss">
.student-page {
	padding: 16px;
	background: rgba(244, 247, 252, 1);
	min-height: 100vh;
}
.filter-bar {
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
		.filter-label {
			font-size: 14px;
			color: rgba(102, 102, 102, 1);
			margin-right: 8px;
			white-space: nowrap;
		}
	}
	.filter-actions {
		margin-left: auto;
		display: flex;
	}
}
.student-list {
	background: #fff;
	border-radius: 8px;
	padding: 16px;
	box-shadow: 0 1px 4px rgba(36, 93, 209, 0.06);
}
.qrcode-text {
	color: rgba(36, 93, 209, 1);
	text-decoration: underline;
	cursor: pointer;
}
.op-btns {
	display: flex;
	justify-content: center;
	gap: 6px;
	.op-btn {
		padding: 4px 8px;
		font-size: 12px;
		border-radius: 4px;
		cursor: pointer;
	}
	.op-edit {
		color: rgba(36, 93, 209, 1);
		border: 1px solid rgba(36, 93, 209, 1);
	}
	.op-qr {
		color: rgba(51, 176, 19, 1);
		border: 1px solid rgba(51, 176, 19, 1);
	}
	.op-print {
		color: rgba(240, 163, 41, 1);
		border: 1px solid rgba(240, 163, 41, 1);
	}
}
.pagination-box {
	margin-top: 16px;
	display: flex;
	justify-content: flex-end;
}
.qrcode-popup {
	width: 60vw;
	max-width: 500px;
	background: #fff;
	border-radius: 12px;
	padding: 20px;
	text-align: center;
	.popup-title {
		font-size: 18px;
		font-weight: 600;
		margin-bottom: 12px;
		color: rgba(36, 93, 209, 1);
	}
	.student-info {
		background: rgba(244, 247, 252, 1);
		padding: 10px;
		border-radius: 6px;
		margin-bottom: 16px;
		.info-text {
			display: block;
			font-size: 14px;
			color: rgba(51, 51, 51, 1);
			text-align: left;
			margin: 2px 0;
		}
	}
	.qrcode-image {
		display: flex;
		justify-content: center;
		margin: 16px 0;
	}
	.qrcode-value {
		font-size: 13px;
		color: rgba(102, 102, 102, 1);
		margin-bottom: 16px;
	}
	.popup-footer {
		display: flex;
		justify-content: center;
	}
}
</style>
