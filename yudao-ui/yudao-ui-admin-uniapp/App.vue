<script>
import config from './config';
import { districtInitSql } from '@/utils/districtInitSql.js';
import store from '@/store';
import {tableSqls} from "@/utils/sqlite.js"
import {dbName} from "@/utils/sqlite.js"
import { getAccessToken } from '@/utils/auth';
import { onShow } from '@dcloudio/uni-app'
import dbUtils from '@/uni_modules/zjy-sqlite-manage/components/zjy-sqlite-manage/dbUtils'
export default {
	onLaunch: function () {
		this.initApp();
		this.initDateBase()
		districtInitSql(this.$dbUtils)
    // this.initDistrict()
  },
	async onShow() {
		try{
			let res=await this.listenToNetworkStatus()
			console.log(res);
		}catch(e){
			console.error(e);
			// 不需要图标，icon用none，可选图标（'success' 、'error' 、'worining'）
			uni.showToast({
				title: e,
				icon: 'error',
				duration: 2000
			})  
			//TODO handle the exception
		}
		this.initDateBase()
    // this.initDistrict()
	},
  data(){
    return {
    }
  },
	methods: {
		async listenToNetworkStatus() {
		  return new Promise((resolve, reject) => {
		    uni.onNetworkStatusChange((res) => {
		      if (!res.isConnected) {
		        reject(new Error('当前无网络连接'));
		      } else {
		        resolve(res);
		      }
		    });
		  });
		},
		initDistrict(){
		  districtInitSql(this.$dbUtils)
		},
		// 初始化数据库
		initDateBase(){
			dbUtils.openDb("tb_screen")
			dbUtils.init("tb_screen",tableSqls)
			// this.$dbUtils.closeSQL("th_screen")
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
