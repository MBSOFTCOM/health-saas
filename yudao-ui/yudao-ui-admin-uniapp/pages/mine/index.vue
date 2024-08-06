<template>
	<view class="mine-container" :style="{ height: `${windowHeight}px` }">
		<view class="top">
			<image class="logo-image" src="/static/images/tb/logo.png"></image>
			<view class="main-title">校园筛查管理系统</view>
		</view>
		<!--顶部个人信息栏-->
		<view class="header-section">
			<view class="flex padding justify-between">
				<view class="flex align-center">
					<image
						src="../../static/images/tb/man.png"
						class="cu-avatar xl round"
						mode="widthFix"
						style="width: 80px;margin-right: 10px;"
					></image>
					<view v-if="!name" @click="handleToLogin" class="login-tip">点击登录</view>
					<view v-if="name" @click="handleToInfo" class="user-info">
						<view class="u_title">用户名 {{ name }}</view>
					</view>
				</view>
				<view @click="handleToInfo" class="flex align-center">
					<text>个人信息</text>
					<view class="iconfont icon-right"></view>
				</view>
			</view>
		</view>
		
		<view class="list-main">
			<view class="main-1" v-for="(item,index) in listMenu" :key="index + 'a'" @click="btnTo(item)">
				<view class="main-1-l">
					<image class="main-img" :src="item.img"></image>
					<view>{{item.text}}</view>
				</view>
				<view class="iconfont icon-right"></view>
			</view>
		</view>
			
			<view style="display: flex;">
				<up-button class="custom-style" size="large" text="退出登录" @click="handleLogout">
				</up-button>
			</view>
			
		</view>
</template>

<script>
import storage from '@/utils/storage';
import * as JPU from '../../static/images/tb/avatar.png'
export default {
	data() {
		return {
			name: this.$store.state.user.name,
			version: this.$config.appInfo.version,
			windowHeight: uni.getSystemInfoSync().windowHeight,
			listMenu:[
				{img:'../../static/images/tb/mine-1.png',text:'编辑资料',url:'/pages/mine/info/edit'},
				{img:'../../static/images/tb/mine-2.png',text:'关于我们',url:'/pages/mine/about/index'},
				{img:'../../static/images/tb/mine-3.png',text:'修改密码',url:'/pages/mine/pwd/index'}
			]
		};
	},
	computed: {
		avatar() {
			return '../../static/images/tb/avatar.png';
		},
		windowHeight1() {
			return uni.getSystemInfoSync().windowHeight - 50;
		}
	},

	onLoad() {
	
	},
	methods: {
		btnTo(val){
        if (val.text==='关于我们'){
          this.$tab.navigateTo(val.url);
          return
        }
        uni.showToast({
        title: '暂未提供该功能',
        icon: 'none',
        duration: 2000
      })
		},
		handleToInfo() {
			this.$tab.navigateTo('/pages/mine/info/index');
		},
		handleToLogin() {
			this.$tab.reLaunch('/pages/login');
		},
		handleLogout() {
			if(uni.$loginStatus){
				this.$modal.confirm('确定注销并退出系统吗？').then(() => {
					uni.$person = {}
					this.$store.dispatch('LogOut').then((res) => {
						this.$tab.reLaunch('/pages/login');
					});
				})
				return
			}
			this.$modal.confirm('确定注销并退出离线系统吗？').then(() => {
				uni.$person = {}
				this.$tab.reLaunch('/pages/login');
			})
			
		}
	}
};
</script>

<style lang="scss" scoped>
page {
	background: rgba(244, 247, 252, 1);
}

.mine-container {
	width: 100%;
	height: 100%;
	.top{
		height: 110px;
		width: 100vw;
		background-color: #fff;
		
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
	.list-main{
		font-size: 20px;
		.main-1{
			border-radius: 5px;
			margin: 10px 20px;
			background-color: #fff;
			padding:15px 20px;
			display: flex;
			align-items: center;
			justify-content: space-between;
			.main-1-l{
				display: flex;
				align-items: center;
				.main-img{
					margin-top: 3px;
					margin-right: 10px;
					width: 25px;
					height: 25px;
				}
			}
		}

	}

	.header-section {
		border-radius: 5px;
		margin: 10px 20px 0 20px;
		font-size: 20px;
		padding: 15px;
		background-color: #3c96f3;
		color: white;

		.login-tip {
			margin-left: 10px;
		}

		.cu-avatar {
			border: 2px solid #eaeaea;

			.icon {
				font-size: 40px;
			}
		}

		.user-info {
			margin-left: 15px;

			.u_title {
				line-height: 30px;
			}
		}
	}
	.custom-style{
		margin: 20px;
		width: 100%;
		background: rgba(36, 93, 209, 1);
		color: #fff;
	}
}
</style>
