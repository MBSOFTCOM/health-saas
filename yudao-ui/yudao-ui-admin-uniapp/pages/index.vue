<template>
	<view class="page-container">
		<view class="top">
			<image class="logo-image" src="/static/images/tb/logo.png"></image>
			<view class="main-title">校园筛查管理系统</view>
		</view>
		<view class="circle-container">
			<image class="bgimg" @click="btn1" src="../static/images/tb/mian-1.png" mode="" disabled></image>
			<image class="bgimg" @click="btn2" style="margin: 0 20px;" src="../static/images/tb/mian-3.png" mode=""></image>
			<image class="bgimg" @click="btn3" src="../static/images/tb/mian-2.png" mode="" disabled></image>
		</view>
		<image class="bom-img" @click="toSynchronize" src="../static/images/tb/mian-bom.png" mode=""></image>
	</view>
</template>

<script>
import * as SynchronizeApi from '@/api/synchronize/synchronize';
	
export default {
	data() {
		return {
			name:null,
			screenPoint:null,
		};
	},
	methods:{
		btn1(){
			let item = {
				label:'常规筛查',
				val:1
			}
			this.btnClick(item)
		},
		btn2(){
			let item = {
				label:'新生入学筛查',
				val:2
			}
			this.btnClick(item)
		},
		btn3(){
			let item = {
				label:'应急筛查',
				val:3
			}
			this.btnClick(item)
		},
		btnClick(item){
			SynchronizeApi.getWorkTeamCount(null).then(res=>{
				// console.log(res);
				if(res[0].num<=0){
					uni.showToast({
						title: '请先同步工作队伍' ,
						mask: true,
						icon: 'none',
						duration: 1500
					})
					return
				}else{
					SynchronizeApi.getYear(uni.$person.name).then(res=>{
						// console.log(res);
						if(!res[0].year){
							uni.showToast({
								title: '没有工作年度' ,
								mask: true,
								icon: 'none',
								duration: 1500
							})
						}else{
							let label = item.label
							let val = item.val
							uni.$screenType = item.val
							uni.navigateTo({
								url:'/pages/index2?label=' + label
							})
						}
					})
				}
			})
		},
		toSynchronize(){
			if(uni.$loginStatus){
				uni.navigateTo({
					url:'/pages/tb/synchronize/index'
				})
			} else{
				uni.showToast({
					title: '离线状态无此功能' ,
					mask: true,
					icon: 'none',
					duration: 1500
				})
			}
		}
	}
};
</script>

<style scoped lang="scss">
.page-container {
	position: relative;
	height: 100vh;
	background: rgba(244, 247, 252, 1);
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
	.circle-container {
		width: 75vw;
		position: absolute;
		top: 25%;
		left: 21%;
		.bgimg{
			width:210px;
			height:220px;
		}
	}
}
.bom-img{
	position: absolute;
	top: 65%;
	left: 21%;
	width:670px;
	height:180px;
}
</style>
