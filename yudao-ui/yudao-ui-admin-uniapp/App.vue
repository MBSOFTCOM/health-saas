<script>
import config from './config';
import { districtInitSql } from '@/utils/districtInitSql.js';
import store from '@/store';
import {tableSqls} from "@/utils/sqlite.js"
import {dbName} from "@/utils/sqlite.js"
import { getAccessToken } from '@/utils/auth';
import { onShow } from '@dcloudio/uni-app'
export default {
	onLaunch: function () {
		this.initApp();
    districtInitSql(this.$dbUtils)
    // this.initDistrict()
  },
	onShow:function() {
		uni.onNetworkStatusChange(function (res) {
			console.log(res);
			if (!res.isConnected) {
				uni.showToast({
					title: '当前无网络连接',
					icon: 'none'
				});
			throw new Error("无网络连接")
			}
		});
		this.initDateBase()
    // this.initDistrict()
	},
  data(){
    return {
    }
  },
	methods: {
    initDistrict(){
      districtInitSql(this.$dbUtils)
    },
		// 初始化数据库
		initDateBase(){
			this.$dbUtils.openDb("tb_screen")
			this.$dbUtils.init("tb_screen",tableSqls)
			this.$dbUtils.closeSQL("th_screen")
		},
		// 初始化应用
		initApp() {
			// 初始化应用配置
			this.initConfig();
			// 检查用户登录状态
			//#ifdef H5
			this.checkLogin();
			//#endif
		},
		initConfig() {
			this.globalData.config = config;
		},
		checkLogin() {
			if (!getAccessToken()) {
				this.$tab.reLaunch('/pages/index');
			}
		}
	}
};
</script>

<style lang="scss">
/* 注意要写在第一行，同时给style标签加入lang="scss"属性 */
@import "@/uni_modules/uview-plus/index.scss";
@import '@/static/scss/index.scss';
</style>
