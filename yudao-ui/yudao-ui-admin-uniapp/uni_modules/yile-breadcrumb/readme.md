# yile-breadcrumb 面包屑

## 说明

面包屑导航，目的是为了帮助你追溯来路，让用户了解当前所处位置，以及当前页面在整个网站中的位置。

## 属性

| 参数       | 作用   |类型    |  默认值 |
| --------   | -----:  |-----:  | :----:  |
| nav    | 内容数组 |Array  |   ''    |
| separator     | 分隔符 | String |   / |
| size    | 字体大小 | String, Number  |  默认14px，支持rpx等格式，值为数字类型时格式默认px     |
| color    | 字体颜色 | String  |  #6a6a6a     |
| actColor    | 选中字体颜色 | String  |  #2979ff     |
| marginTop    | 距离上一元素距离 | String, Number  |  无默认值，支持rpx等格式，值为数字类型时格式默认px     |
| ownStyle    | 自定义样式 | Object  |  无默认值，格式{'font-weight':'bold'}     |

### nav格式如下
```js
nav: [{
		value: '首页',       //名称
		url: '',             //链接
		type: 'navigateTo',  //跳转方式
		isActive: true       //选中状态
	}, {
		value: '在线客服',
		url: '',
		type: 'redirectTo'
	}, {
		value: '反馈中心',
		url: '',
		type: 'reLaunch'
	}, {
		value: '个人中心',
		url: '',
		type: 'switchTab'
	}]
```


## 调用

1. 基础
```html
<template>
	<yile-breadcrumb :nav="nav"></yile-breadcrumb>
</template>
```

2. 颜色
```html
<template>
	<yile-breadcrumb :nav="nav" color="#999999"></yile-breadcrumb>
</template>
```

3. 自定义分隔符
```html
<template>
	<yile-breadcrumb :nav="nav" separator=">"></yile-breadcrumb>
</template>
```

4. 选中字体颜色
```html
<template>
	<yile-breadcrumb :nav="nav" actColor="#ff0000"></yile-breadcrumb>
</template>
```

5. 字体大小
```html
<template>
	<yile-breadcrumb :nav="nav" size="16"></yile-breadcrumb>
</template>
```

6. 距离上一元素距离
```html
<template>
	<yile-breadcrumb :nav="nav" marginTop="20"></yile-breadcrumb>
</template>
```