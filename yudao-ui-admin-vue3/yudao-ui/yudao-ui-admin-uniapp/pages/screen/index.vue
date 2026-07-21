<template>
	<view class="page-container">
		<view class="top">
			<image class="logo-image" src="/static/images/tb/logo.png"></image>
			<view class="main-title">五健筛查管理</view>
		</view>
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
	</view>
</template>

<script>
export default {
	data() {
		return {
			menuItems: [
				{
					label: '体检批次查询',
					desc: '查询已创建体检批次',
					icon: '批',
					bgColor: 'rgba(36, 93, 209, 0.1)',
					url: '/pages/screen/batch/index',
					reqNo: '127'
				},
				{
					label: '筛查数据登记',
					desc: '扫码登记五健筛查结果',
					icon: '登',
					bgColor: 'rgba(80, 104, 242, 0.1)',
					url: '/pages/screen/register/index',
					reqNo: '128'
				},
				{
					label: '学生信息管理',
					desc: '受检者信息与二维码',
					icon: '生',
					bgColor: 'rgba(51, 176, 19, 0.1)',
					url: '/pages/screen/student/index',
					reqNo: '129-130'
				},
				{
					label: '体检总检管理',
					desc: '查看筛查进度与补检',
					icon: '检',
					bgColor: 'rgba(240, 163, 41, 0.1)',
					url: '/pages/screen/examination/index',
					reqNo: '131-132'
				},
				{
					label: '筛查进度查询',
					desc: '多维度进度统计',
					icon: '度',
					bgColor: 'rgba(223, 65, 65, 0.1)',
					url: '/pages/screen/progress/index',
					reqNo: '133-134'
				},
				{
					label: '设备管理与数据同步',
					desc: '设备配对与数据同步',
					icon: '设',
					bgColor: 'rgba(36, 93, 209, 0.1)',
					url: '/pages/screen/device/index',
					reqNo: '135'
				},
				{
					label: '复筛报到',
					desc: '扫码复筛报到登记',
					icon: '到',
					bgColor: 'rgba(80, 104, 242, 0.1)',
					url: '/pages/screen/rescreen/checkin',
					reqNo: '136-137'
				},
				{
					label: '复筛信息登记',
					desc: '扫码录入复筛结果',
					icon: '复',
					bgColor: 'rgba(51, 176, 19, 0.1)',
					url: '/pages/screen/rescreen/register',
					reqNo: '138'
				}
			]
		};
	},
	methods: {
		goPage(item) {
			uni.navigateTo({
				url: item.url,
				fail: () => {
					uni.showToast({
						title: '页面未注册：' + item.url,
						icon: 'none',
						duration: 2000
					});
				}
			});
		}
	}
};
</script>

<style scoped lang="scss">
.page-container {
	position: relative;
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
	.menu-grid {
		display: flex;
		flex-wrap: wrap;
		padding: 30px;
		justify-content: flex-start;
	}
	.menu-card {
		width: 22%;
		margin: 1.5%;
		box-sizing: border-box;
		.menu-card-inner {
			background-color: #fff;
			border-radius: 12px;
			padding: 24px 16px;
			display: flex;
			flex-direction: column;
			align-items: center;
			box-shadow: 0 2px 8px rgba(36, 93, 209, 0.08);
			cursor: pointer;
			transition: all 0.2s;
			&:active {
				transform: translateY(2px);
				box-shadow: 0 1px 4px rgba(36, 93, 209, 0.12);
			}
			.menu-icon {
				width: 80px;
				height: 80px;
				border-radius: 50%;
				display: flex;
				align-items: center;
				justify-content: center;
				margin-bottom: 12px;
				.icon-text {
					font-size: 32px;
					font-weight: bold;
					color: rgba(36, 93, 209, 1);
				}
			}
			.menu-label {
				font-size: 18px;
				font-weight: 600;
				color: rgba(51, 51, 51, 1);
				margin-bottom: 6px;
				text-align: center;
			}
			.menu-desc {
				font-size: 13px;
				color: rgba(153, 153, 153, 1);
				text-align: center;
			}
		}
	}
}
</style>
