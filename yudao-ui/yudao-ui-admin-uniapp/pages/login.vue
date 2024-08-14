<template>
	<view class="main-bg">
		<image class="logo" src="../static/images/tb/logo.png" mode=""></image>
		<image class="bg-1" src="../static/images/tb/bg-1.png" mode=""></image>
		<image class="bg-2" src="../static/images/tb/bg-2.png" mode=""></image>
		<image class="bg-3" src="../static/images/tb/bg-3.png" mode=""></image>
		<view class="log-main">
			<view class="title">校园筛查管理系统</view>
			<view class="log-ipt">
				<view class="text">账号</view>
				<input v-model="loginForm.username" class="input" type="text" placeholder="请输入账号" maxlength="30" />
			</view>
			<view class="log-ipt">
				<view class="text">密码</view>
				<input
					v-model="loginForm.password"
					type="password"
					class="input"
					placeholder="请输入密码"
					maxlength="20"
				/>
			</view>
			<view class="log-btn">
				<up-button @click="localLogin" class="local">离线登录</up-button>
				<up-button @click="handleLogin" class="handle">在线登录</up-button>
			</view>
		</view>
	</view>
</template>

<script>
import Verify from '@/components/verifition/Verify';
import { getUser, getUserList } from '@/utils/user.js';
import * as SynchronizeApi from '@/api/synchronize/synchronize';
import CryptoJS from 'crypto-js';

export default {
	name: 'Login',
	components: {
		Verify
	},
	data() {
		return {
			captchaEnabled: false, // 验证码开关 TODO 芋艿：需要抽到配置里
			globalConfig: this.$config,
			loginForm: {
				username: 'wanzhouqu0001',
				password: '123456',
				captchaVerification: ''
			}
		};
	},
	onLoad: function () {},
	methods: {
		//本地登录
		async localLogin() {
			if (this.loginForm.username === '') {
				this.$modal.msgError('请输入您的账号');
			}
			const password = CryptoJS.MD5(this.loginForm.password).toString() + 'pingban';
			const user = await getUser(this.loginForm.username);
			// console.log(user);
			if (user.length == 0) {
				uni.$u.toast('登录用户不存在！');
				return;
			}

			if (user[0].pwd != password) {
				uni.$u.toast('密码错误！');
				return;
			}
			const year = await SynchronizeApi.getYear(this.loginForm.username);
			// console.log(year);
			const groupName = await SynchronizeApi.getGroups(this.loginForm.username);

			const agency = await SynchronizeApi.getScreenPoint(this.loginForm.username);
			// console.log(groupName);
			uni.$person = user[0];
			// console.log(uni.$person);
			if (year) {
				uni.$person.year = year[0].year;
			}
			if (groupName) {
				uni.$person.groupName = groupName[0].groupName;
			}
			if (agency) {
				uni.$person.agency = agency[0].agency;
				uni.$person.screenPoint = agency[0].screenPoint;
			}
			// console.log(uni.$person);
			//置为离线
			uni.$loginStatus = false;
			this.$tab.reLaunch('/pages/index');
		},
		// 隐私协议
		handlePrivacy() {
			let site = this.globalConfig.appInfo.agreements[0];
			this.$tab.navigateTo(`/pages/common/webview/index?title=${site.title}&url=${site.url}`);
		},
		// 用户协议
		handleUserAgrement() {
			let site = this.globalConfig.appInfo.agreements[1];
			this.$tab.navigateTo(`/pages/common/webview/index?title=${site.title}&url=${site.url}`);
		},
		// 登录方法
		async handleLogin(params) {
			if (this.loginForm.username === '') {
				this.$modal.msgError('请输入您的账号');
			} else if (this.loginForm.password === '') {
				this.$modal.msgError('请输入您的密码');
			} else {
				// 显示验证码
				if (this.captchaEnabled) {
					this.$refs.verify.show();
				} else {
					// 直接登录
					await this.pwdLogin({});
				}
			}
		},
		// 密码登录
		async pwdLogin(captchaParams) {
			this.$modal.loading('登录中，请耐心等待...');
			//置为在线
			uni.$loginStatus = true;
			// 执行登录
			// console.log("服务器登录");
			this.loginForm.captchaVerification = captchaParams.captchaVerification;
			this.$store.dispatch('Login', this.loginForm).then(() => {
				// console.log("登录");
				this.$modal.closeLoading();
				this.loginSuccess();
			});
		},
		// 登录成功后，处理函数
		loginSuccess(result) {
			// 设置用户信息
			this.$store.dispatch('GetInfo').then((res) => {
				SynchronizeApi.getUserInfo(res.user.id).then((resp) => {
					let temp = resp.data;
					// console.log(temp);
					uni.$person = temp;
					uni.$user = temp;
					SynchronizeApi.getYear(temp.name).then((response) => {
						// console.log(response);
						if (response[0].year) {
							uni.$person.year = response[0].year;
							uni.$user.year = response[0].year;
						}
					});
					SynchronizeApi.getGroups(temp.name).then((res) => {
						// console.log(res);
						if (res[0].groupName) {
							uni.$person.groupName = res[0].groupName;
							uni.$user.groupName = res[0].groupName;
						}
					});
					SynchronizeApi.getScreenPoint(temp.name).then((res) => {
						// console.log(res);
						if (res.length > 0) {
							uni.$person.agency = res[0].agency;
							uni.$user.agency = res[0].agency;
							uni.$person.screenPoint = res[0].screenPoint;
							uni.$user.screenPoint = res[0].screenPoint;
						}
						// console.log(uni.$user);
						// console.log(uni.$person);
					});
					// console.log(uni.$user);
					// console.log(uni.$person);
				});
				this.$tab.reLaunch('/pages/index');
			});
		}
	}
};
</script>

<style lang="scss" scoped>
.main-bg {
	width: 100%;
	height: 100%;
	position: relative;
	background: linear-gradient(90deg, rgba(70, 176, 242, 1) 0%, rgba(45, 112, 237, 1) 100%);
	.logo {
		width: 200px;
		height: 50px;
		position: absolute;
		top: 40px;
		left: 20px;
		z-index: 11;
	}
	.bg-1 {
		position: absolute;
		top: -32px;
		left: -10px;
		width: 500px;
		transform: rotate(8deg);
	}
	.bg-2 {
		position: absolute;
		width: 250px;
		height: 120px;
		bottom: 60px;
		left: 100px;
	}
	.bg-3 {
		position: absolute;
		right: 40px;
		height: 500px;
		top: 50px;
	}
	.log-main {
		border-radius: 15px;
		gap: 20px;
		width: 500px;
		height: 350px;
		position: absolute;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
		display: flex;
		flex-direction: column;
		align-items:center;
		justify-content: center;
		background-color: #fff;
		padding: 10px;
		.title {
			font-size: 22px;
			font-weight: 800;
			color: rgba(42, 130, 228, 1);
		}
		.log-ipt {
			font-size: 18px;
			.text {
				color: rgba(59, 91, 130, 1);
			}
			.input {
				width: 400px;
				height: 35px;
				border: 1px solid rgba(206, 212, 219, 1);
			}
		}
		.log-btn {
			display: flex;
			align-items: center;
			justify-content: space-evenly;
			.local {
				font-size: 20px;
				width: 170px;
				padding: 10px 0;
				border: 1px solid rgba(1, 111, 241, 1);
				color: rgba(1, 111, 241, 1);
				background-color: #fff;
			}
			.handle {
				margin-left: 60px;
				font-size: 22px;
				padding: 10px 0;
				width: 170px;
				background: rgba(1, 111, 241, 1);
				color: #fff;
			}
		}
	}
}
</style>
