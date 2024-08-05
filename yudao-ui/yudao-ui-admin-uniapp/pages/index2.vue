<template>
	<view class="page-container">
		<view class="circle-container">
			<view class="top-circles">
				<view hover-class="none" v-for="(item, index) in topMenuItems" :key="index + 'a'" @click="btnTo(item)">
					<view class="circle">
						<image class="circle-img" :src="item.img" mode=""></image>
						<view class="circle-label">{{ item.label }}</view>
					</view>
				</view>
			</view>
			<view class="bottom-circles">
				<view v-for="(item, index) in bottomMenuItems" :key="index" @click="btnTo(item)">
					<view class="circle">
						<image class="circle-img" :src="item.img" mode=""></image>
						<view class="circle-label">{{ item.label }}</view>
					</view>
				</view>
				<view class="dr-ct">
					<view class="bg" @click="btnClick(ct)">
						<image class="img" src="../static/images/tb/index-7.png" mode=""></image>
						<view class="label">DR</view>
					</view>
					<view class="bg" @click="btnClick(dr)">
						<image class="img" src="../static/images/tb/index-8.png" mode=""></image>
						<view class="label">CT</view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
import { mapState } from 'vuex';
export default {
	data() {
		return {
			title: '',
			groupName: '',
			dr:{
				label: 'DR/CT组',
				url: '/pages/tb/dr/dr',
			},
			ct:{
				label: 'DR/CT组',
				url: '/pages/tb/ct/ct',
			},
			topMenuItems: [
				{
					label: '采集组',
					url: '/pages/tb/gather/index',
					img: '../static/images/tb/index-1.png'
				},
				{
					label: 'PPD组',
					url: '/pages/tb/ppd/index',
					img: '../static/images/tb/index-2.png'
				},
				{
					label: '痰检组',
					url: '/pages/tb/sputumExamination/index',
					img: '../static/images/tb/index-3.png'
				},
				{
					label: '实验室组',
					url: '/pages/tb/laboratory/index',
					img: '../static/images/tb/index-4.png'
				}
			],
			bottomMenuItems: [
				{
					label: '诊断组',
					url: '/pages/tb/diagnosis/index',
					img: '../static/images/tb/index-5.png'
				},
				{
					label: '心电图组',
					url: '/pages/tb/ecg/index',
					img: '../static/images/tb/index-6.png'
				}
			]
		};
	},
	methods: {
		btnClick(item){
			const groups = this.groupName.split(',');
			if (groups.includes('队长')) {
				uni.navigateTo({
					url: item.url
				});
				return true;
			}
			let found = groups.includes(item.label);
			if (found) {
				uni.navigateTo({
					url: item.url
				});
				return true;
			} else {
				uni.showToast({
					title: '您没有操作权限',
					icon: 'error',
					duration: 2000
				});
				return false;
			}
		},
		btnTo(item) {
			const groups = this.groupName.split(',');
			if (groups.includes('队长')) {
				uni.navigateTo({
					url: item.url
				});
				return true;
			}
			let found = groups.includes(item.label);
			if (found) {
				uni.navigateTo({
					url: item.url
				});
				return true;
			} else {
				uni.showToast({
					title: '您没有操作权限',
					icon: 'error',
					duration: 2000
				});
				return false;
			}
		}
	},
	onLoad(e) {
		this.groupName = uni.$person.groupName;
		this.title = e.label;
		uni.setNavigationBarTitle({
			title: e.label
		});
		uni.setNavigationBarColor({
		    frontColor: '#000',
		    backgroundColor: '#fff',
		})
	}
};
</script>

<style scoped lang="scss">
.page-container {
	position: relative;
	height: 100vh;
	background: rgba(244, 247, 252, 1);
	.circle-container {
		position: absolute;
		display: flex;
		justify-content: center;
		align-items: center;
		padding: 0 20px;
		left: 10%;
		top: 11%;

		.top-circles {
			display: flex;
			justify-content: space-between;
			margin-bottom: 10vh;
		}

		.bottom-circles {
			display: flex;
			justify-content: space-between;
			position: absolute;
			top: 100%;
			left: 2%;
		}
		.dr-ct {
			margin-left: 20px;
			display: flex;
			background-color: #fff;
			justify-content: space-between;
			height: 180px;
			width: 400px;
			border-radius: 10px;
			font-size: 16px;
			color: rgba(74, 74, 74, 1);
			box-shadow: 1px 1px 2px rgba(36, 93, 209, 0.1);
			background-image: linear-gradient(
				to right,
				transparent calc(50% - 0.5px),
				rgba(224, 224, 224, 1) 0,
				rgba(224, 224, 224, 1) calc(50% + 0.5px),
				transparent 0
			);
			background-size: 1px 70%;
			background-position: center;
			background-repeat: no-repeat;
			.label {
				text-align: center;
				margin-left: -20px;
			}
			.img {
				width: 100px;
				height: 100px;
			}
			.bg {
				flex: 1;
				display: flex;
				flex-direction: column;
				align-items: center;
				justify-content: center;
			}
		}

		.circle {
			background-color: #fff;
			width: 180px;
			height: 180px;
			border-radius: 10px;
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;
			font-size: 16px;
			color: rgba(74, 74, 74, 1);
			cursor: pointer;
			box-shadow: 1px 1px 2px rgba(36, 93, 209, 0.1);
			margin: 0 20px;
			.circle-img {
				width: 100px;
				height: 100px;
				margin-left: 15px;
			}
			.circle-label {
				text-align: center;
			}
		}
	}
}
</style>
