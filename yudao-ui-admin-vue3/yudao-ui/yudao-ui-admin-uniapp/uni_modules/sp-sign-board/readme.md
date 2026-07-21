# sp-sign-board

## prop参数

```
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
		default: true
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
		default: false
	},
	// 画笔样式
	penStyle: {
		type: Object,
		default: () => {
			return {
				lineWidth: 3, // 画笔线宽 建议1~5
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
}
```

## emit事件

```
@confirm 					点击确认回调
@reset 						点击重置回调
@cancle 					点击取消回调
@firstTouchStart 	首次触碰签字板回调
```

## uni.$emit事件

```
uni.$emit('getSignImg', { base64, path })

uni.$on('getSignImg', ({ base64, path }) => {
	console.log('签名base64, path ====>', base64, path) //拿到的图片数据
	// 之后取消监听，防止重复监听
	uni.$off('getSignImg')
})
```

## 写在最后
若对插件有任何疑问或者优化建议，欢迎在评论区留言，在插件市场中的私信消息本人可能不经常留意，导致没能及时回复，
可以加入本人的插件问答QQ交流群: 852637893，欢迎进群交流。

<img width="200" src="https://mp-0ecede5c-a993-48bf-ba4b-45d9a8c7e79b.cdn.bspapp.com/resource/qqqun.jpg" alt="交流群:852637893"/>
