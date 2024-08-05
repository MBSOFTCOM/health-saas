<template>
	<view class="yile-breadcrumb-con" :style="[mainStyle,ownStyle]">
		<view v-for="(item,index) in nav" :key="index">
			<text @click="onClick(item.url,item.type)" :style="[item.isActive?bcActColor:'']">{{item.value}}</text>
			<text v-if="index<nav.length-1">{{separator}}</text>
		</view>
	</view>
</template>
<script>
	/**
	 * Yile-Breadcrumb 文字
	 * @property {Array} nav 内容数组
	 * @property {String} separator 分隔符（默认 /）
	 * @property {String, Number} size 字体大小（默认14px，支持rpx等格式，值为数字类型时格式默认px）
	 * @property {String} color 字体颜色（默认#6a6a6a）
	 * @property {String} actColor 选中字体颜色（默认#2979ff）
	 * @property {String, Number} marginTop 距离上一元素距离（支持rpx等格式，值为数字类型时格式默认px）
	 * @property {Object} ownStyle 自定义样式（格式{'font-weight':'bold'}）
	 * @example <yile-breadcrumb></yile-breadcrumb>
	 */
	export default {
		props: {
			nav: {
				type: Array,
				default: function() {
					return []
				}
			},
			separator: {
				type: String,
				default: '/'
			},
			size: {
				type: [String, Number],
				default: 14
			},
			color: {
				type: [String],
				default: '#6a6a6a'
			},
			actColor: {
				type: [String],
				default: '#2979ff'
			},
			marginTop: {
				type: [String, Number],
				default: ''
			},
			ownStyle: {
				type: Object,
				default: ()=>{ return {} }
			},
		},
		data() {
			return {}
		},
		computed: {
			//主样式
			mainStyle() {
				var style = {};

				//字体大小
				style.fontSize = this.getValue(this.size);
				//颜色
				style.color = this.color;
				//距离上一元素距离
				if (this.marginTop != '') style.marginTop = this.getValue(this.marginTop);

				return style;
			},
			//选中字体颜色
			bcActColor() {
				return {
					color: this.actColor,
					fontWeight: 'bolder'
				}
			},
		},
		methods: {
			//获取大小（兼容任意格式，数字默认px）
			getValue(val) {
				const reg = /^[0-9]*$/g
				return (typeof val === 'number' || reg.test(val)) ? (val + 'px') : val;
			},
			//点击事件
			onClick(url, type) {
				if (url == '') return;
				switch (type) {
					case "navigateTo":
						uni.navigateTo({
							url: url
						});
						break;
					case "redirectTo":
						uni.redirectTo({
							url: url
						});
						break;
					case "reLaunch":
						uni.reLaunch({
							url: url
						});
						break;
					case "switchTab":
						uni.switchTab({
							url: url
						});
						break;
				}
			},
		}
	}
</script>
<style lang="scss">
	.yile-breadcrumb-con {
		display: flex;
		flex-direction: row;
		align-items: center;

		view text {
			margin-right: 15rpx;
		}

		view text.active {
			font-weight: bolder;
		}
	}
</style>
