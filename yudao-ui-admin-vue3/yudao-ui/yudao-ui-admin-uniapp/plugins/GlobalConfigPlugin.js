export default {
	install: (app) => {
		const config = {
			// baseUrl: 'http://api-dashboard.yudao.iocoder.cn',
			// baseUrl: 'http://localhost:48080',
			baseUrl: 'http://192.168.220.232:48080',
			baseApi: '/admin-api',
			// 应用信息
			appInfo: {
				// 应用名称
				name: "yudao-app",
				// 应用版本
				version: "1.0.0",
				// 应用logo
				logo: "/static/logo.png",
				// 官方网站
				site_url: "https://iocoder.cn",
				// 政策协议
				agreements: [{
						title: "隐私政策",
						url: "https://iocoder.cn"
					},
					{
						title: "用户服务协议",
						url: "https://iocoder.cn"
					}
				]
			}
		}

		app.config.globalProperties.$config = config
	}
}