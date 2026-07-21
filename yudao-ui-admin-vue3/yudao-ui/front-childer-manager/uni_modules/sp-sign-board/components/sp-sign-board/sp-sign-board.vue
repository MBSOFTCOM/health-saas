<template>
	<view class="sign-page" v-cloak>
		<view class="sign-body">
			<canvas
				canvas-id="signCanvas"
				class="sign-canvas"
				disable-scroll
				@touchstart="signCanvasStart"
				@touchmove="signCanvasMove"
				@touchend="signCanvasEnd"
			></canvas>
			<!-- #ifdef H5 -->
			<!--用于临时储存横屏图片的canvas容器，仅H5需要-->
			<canvas
				v-if="horizontal"
				style="position: absolute; top: -1000px; z-index: -1"
				:style="{ width: canvasHeight + 'px', height: canvasWidth + 'px' }"
				canvas-id="hsignCanvas"
			></canvas>
			<!-- #endif -->
		</view>
		<view class="sign-footer" :class="[horizontal ? 'horizontal-btns' : 'vertical-btns']">
			<view class="btn" @click="cancle">取消</view>
			<view class="btn" @click="reset">重写</view>
			<view class="btn" @click="confirm">确认</view>
		</view>
	</view>
</template>

<script>
import { pathToBase64, base64ToPath } from './index.js'
export default {
	name: 'sign',
	props: {
		// 背景水印图，优先级大于 bgColor
		bgImg: {
			type: String,
			default: ''
		},
		// 背景纯色底色，为空则透明
		bgColor: {
			type: String,
			default: ''
		},
		// 是否显示水印
		showMark: {
			type: Boolean,
			default: false
		},
		// 水印内容，可多行
		markText: {
			type: Array,
			default: () => {
				return [] // ['水印1', '水印2']
			}
		},
		// 水印样式
		markStyle: {
			type: Object,
			default: () => {
				return {
					fontSize: 12, // 水印字体大小
					fontFamily: 'microsoft yahei', // 水印字体
					color: '#cccccc', // 水印字体颜色
					rotate: 60, // 水印旋转角度
					step: 2.2 // 步长，部分场景下可通过调节该参数来调整水印间距，建议为1.4-2.6左右
				}
			}
		},
		// 是否横屏
		horizontal: {
			type: Boolean,
			default: true
		},
		// 画笔样式
		penStyle: {
			type: Object,
			default: () => {
				return {
					lineWidth: 5, // 画笔线宽 建议1~5
					color: '#000000' // 画笔颜色
				}
			}
		},
		// 导出图片配置
		expFile: {
			type: Object,
			default: () => {
				return {
					fileType: 'png', // png/jpg (png不可压缩质量，支持透明；jpg可压缩质量，不支持透明)
					quality: 1 // 范围 0 - 1 (仅jpg支持)
				}
			}
		}
	},
	data() {
		return {
			canvasCtx: null, // canvascanvasWidth: 0, // canvas宽度
			canvasWidth: 0, // canvas宽度
			canvasHeight: 0, // canvas高度
			x0: 0, // 初始横坐标或上一段touchmove事件中触摸点的横坐标
			y0: 0, // 初始纵坐标或上一段touchmove事件中触摸点的纵坐标
			signFlag: false // 签名旗帜
		}
	},
	computed: {},
	created() {},
	mounted() {
		this.$nextTick(() => {
			this.createCanvas()
		})
	},
	methods: {
		// 创建canvas实例
		createCanvas() {
			this.canvasCtx = uni.createCanvasContext('signCanvas', this)
			this.canvasCtx.setLineCap('round') // 向线条的每个末端添加圆形线帽

			// 获取canvas宽高
			const query = uni.createSelectorQuery().in(this)
			query
				.select('.sign-body')
				.boundingClientRect((data) => {
					this.canvasWidth = data.width
					this.canvasHeight = data.height
				})
				.exec(async () => {
					await this.drawBg()
					this.drawMark(this.markText)
				})
		},
		async drawBg() {
			if (this.bgImg) {
				const img = await uni.getImageInfo({ src: this.bgImg })
				this.canvasCtx.drawImage(img.path, 0, 0, this.canvasWidth, this.canvasHeight)
			} else if (this.bgColor) {
				// 绘制底色填充，否则为透明
				this.canvasCtx.setFillStyle(this.bgColor)
				this.canvasCtx.rect(0, 0, this.canvasWidth, this.canvasHeight)
				this.canvasCtx.fill() // 设置填充
			}
		},
		// 绘制动态水印
		drawMark(textArray) {
			if (!this.showMark) {
				this.canvasCtx.draw()
				return
			}
      // 绘制背景
      this.drawBg()
      
      // 水印参数
			const markStyle = Object.assign(
				{
					fontSize: 12, // 水印字体大小
					fontFamily: 'microsoft yahei', // 水印字体
					color: '#cccccc', // 水印字体颜色
					rotate: 60, // 水印旋转角度
					step: 2 // 步长，部分场景下可通过调节该参数来调整水印间距，建议为1.4-2.6左右
				},
				this.markStyle
			)
			this.canvasCtx.font = `${markStyle.fontSize}px ${markStyle.fontFamily}`
			this.canvasCtx.fillStyle = markStyle.color
			// 文字坐标
			const maxPx = Math.max(this.canvasWidth / 2, this.canvasHeight / 2)
			const stepPx = Math.floor(maxPx / markStyle.step)
			let arrayX = [0] // 初始水印位置 canvas坐标 0 0 点
			while (arrayX[arrayX.length - 1] < maxPx / 2) {
				arrayX.push(arrayX[arrayX.length - 1] + stepPx)
			}
			arrayX.push(
				...arrayX.slice(1, arrayX.length).map((item) => {
					return -item
				})
			)

			for (let i = 0; i < arrayX.length; i++) {
				for (let j = 0; j < arrayX.length; j++) {
					this.canvasCtx.save()
					this.canvasCtx.translate(this.canvasWidth / 2, this.canvasHeight / 2) // 画布旋转原点 移到 图片中心
					this.canvasCtx.rotate(Math.PI * (markStyle.rotate / 180))
					textArray.forEach((item, index) => {
						let offsetY = markStyle.fontSize * index
						this.canvasCtx.fillText(item, arrayX[i], arrayX[j] + offsetY)
					})
					this.canvasCtx.restore()
				}
			}
     
			this.canvasCtx.draw()
		},
		cancle() {
			//取消按钮事件
			this.$emit('cancle')
			this.$emit('event');
			this.reset()
			// uni.navigateBack()
		},
		async reset() {
			this.$emit('reset')
			this.signFlag = false
			this.canvasCtx.clearRect(0, 0, this.canvasWidth, this.canvasHeight)
			await this.drawBg()
			this.drawMark(this.markText)
		},
		async confirm() {
			this.$emit('confirm')
			// 确认按钮事件
			if (!this.signFlag) {
				uni.showToast({
					title: '请签名后再点击确定',
					icon: 'none',
					duration: 2000
				})
				return
			}

			uni.showModal({
				title: '确认',
				content: '确认签名无误吗',
				showCancel: true,
				success: async ({ confirm, cancel }) => {
					if (confirm) {
						let tempFile
						if (this.horizontal) {
							tempFile = await this.saveHorizontalCanvas()
						} else {
							tempFile = await this.saveCanvas()
						}
						const base64 = await pathToBase64(tempFile)
						const path = await base64ToPath(base64)
						uni.$emit('getSignImg', { base64, path })
						// uni.navigateBack()
						this.$emit('event');
					}
				}
			})
		},
		signCanvasEnd(e) {
			// 签名抬起事件
			// console.log(e, 'signCanvasEnd')
			this.x0 = 0
			this.y0 = 0
		},
		signCanvasMove(e) {
			// 签名滑动事件
			// console.log(e, 'signCanvasMove')
			// #ifdef MP-WEIXIN
			let dx = e.touches[0].clientX - this.x0
			let dy = e.touches[0].clientY - this.y0
			// #endif
			// #ifdef APP-PLUS || H5
			let dx = e.touches[0].x - this.x0
			let dy = e.touches[0].y - this.y0
			// #endif
			this.canvasCtx.moveTo(this.x0, this.y0)
			this.canvasCtx.lineTo(this.x0 + dx, this.y0 + dy)
			this.canvasCtx.setLineWidth(this.penStyle?.lineWidth || 4)
			this.canvasCtx.strokeStyle = this.penStyle?.color || '#000000'
			this.canvasCtx.stroke()
			this.canvasCtx.draw(true)
			// #ifdef MP-WEIXIN
			this.x0 = e.touches[0].clientX
			this.y0 = e.touches[0].clientY
			// #endif
			// #ifdef APP-PLUS || H5
			this.x0 = e.touches[0].x
			this.y0 = e.touches[0].y
			// #endif
		},
		signCanvasStart(e) {
			// 签名按下事件 app获取的e不一样区分小程序app
			// console.log('signCanvasStart', e)
			if (!this.signFlag) {
				// 导出第一次开始触碰事件
				this.$emit('firstTouchStart')
			}
			this.signFlag = true
			// #ifdef MP-WEIXIN
			this.x0 = e.touches[0].clientX
			this.y0 = e.touches[0].clientY
			// #endif
			// #ifdef APP-PLUS || H5
			this.x0 = e.touches[0].x
			this.y0 = e.touches[0].y
			// #endif
		},
		// 保存竖屏图片
		async saveCanvas() {
			return await new Promise((resolve, reject) => {
				uni.canvasToTempFilePath(
					{
						canvasId: 'signCanvas',
						fileType: this.expFile.fileType, // 只支持png和jpg
						quality: this.expFile.quality, // 范围 0 - 1
						success: (res) => {
							if (!res.tempFilePath) {
								uni.showModal({
									title: '提示',
									content: '保存签名失败',
									showCancel: false
								})
								return
							}
							resolve(res.tempFilePath)
						},
						fail: (r) => {
							console.log('图片生成失败：' + r)
							resolve(false)
						}
					},
					this
				)
			})
		},
		// 保存横屏图片
		async saveHorizontalCanvas() {
			return await new Promise((resolve, reject) => {
				uni.canvasToTempFilePath(
					{
						canvasId: 'signCanvas',
						fileType: this.expFile.fileType, // 只支持png和jpg
						success: (res) => {
							if (!res.tempFilePath) {
								uni.showModal({
									title: '提示',
									content: '保存签名失败',
									showCancel: false
								})
								return
							}

							// #ifndef H5
							uni.compressImage({
								src: res.tempFilePath,
								quality: this.expFile.quality * 100, // 范围 0 - 100
								rotate: 270,
								success: (r) => {
									// console.log('==== compressImage :', r)
									resolve(r.tempFilePath)
								}
							})
							// #endif

							// #ifdef H5
							uni.getImageInfo({
								src: res.tempFilePath,
								success: (r) => {
									// console.log('==== getImageInfo :', r)
									// 将signCanvas的内容复制到hsignCanvas中
									const hcanvasCtx = uni.createCanvasContext('hsignCanvas', this)
									// 横屏宽高互换
									hcanvasCtx.translate(this.canvasHeight / 2, this.canvasWidth / 2)
									hcanvasCtx.rotate(Math.PI * (-90 / 180))
									hcanvasCtx.drawImage(
										r.path,
										-this.canvasWidth / 2,
										-this.canvasHeight / 2,
										this.canvasWidth,
										this.canvasHeight
									)
									hcanvasCtx.draw(false, async () => {
										const hpathRes = await uni.canvasToTempFilePath(
											{
												canvasId: 'hsignCanvas',
												fileType: this.expFile.fileType, // 只支持png和jpg
												quality: this.expFile.quality // 范围 0 - 1
											},
											this
										)
										let tempFile = ''
										if (Array.isArray(hpathRes)) {
											hpathRes.some((item) => {
												if (item) {
													tempFile = item.tempFilePath
													return
												}
											})
										} else {
											tempFile = hpathRes.tempFilePath
										}
										resolve(tempFile)
									})
								}
							})
							// #endif
						},
						fail: (err) => {
							console.log('图片生成失败：' + err)
							resolve(false)
						}
					},
					this
				)
			})
		}
	}
}
</script>

<style scoped lang="scss">
[v-cloak] {
	display: none !important;
}
.sign-page {
	height: 100%;
	width: 100%;
	display: flex;
	flex-direction: column;

	.sign-body {
		width: 100%;
		flex-grow: 1;

		.sign-canvas {
			width: 100%;
			height: 100%;
		}
	}

	.sign-footer {
		width: 100%;
		height: 120rpx;
		display: flex;
		justify-content: space-evenly;
		align-items: center;
		border-top: 1px solid #cccccc;
		box-sizing: border-box;

		.btn {
			line-height: 90rpx;
			text-align: center;
			border-radius: 12rpx;
			background-color: #fff;

			&:nth-child(1) {
				color: #000;
				border: 1px solid rgba(207, 207, 207, 1);

			}

			&:nth-child(2) {
				border: 1px solid rgba(51, 176, 19, 1);
				color: rgba(51, 176, 19, 1);
			} 

			&:nth-child(3) {
				color: #fff;
				background-color: rgba(21, 99, 232, 1);
			}
		}
	}

	.vertical-btns {
		.btn {
			width: 200rpx;
			height: 70rpx;
		}
	}

	.horizontal-btns {
		.btn {
			height: 90rpx;
			width: 160rpx;
			// writing-mode: vertical-lr;
			writing-mode: horizontal-tb;
			// transform: rotate(90deg);
		}
	}
}
</style>
