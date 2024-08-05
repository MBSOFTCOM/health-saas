<script>
  import config from './config'
  import store from '@/store'
  import { getAccessToken } from '@/utils/auth'
  import dbUtils from "./uni_modules/zjy-sqlite-manage/components/zjy-sqlite-manage/dbUtils";
  import {tableSqls} from "@/utils/sqlite";

  export default {
    onLaunch: function() {
      this.initApp()
      this.initDateBase()
	  plus.screen.lockOrientation('landscape-primary'); //锁定横屏
    },
    methods: {
      // 初始化应用
      initApp() {
        // 初始化应用配置
        this.initConfig()
        // 检查用户登录状态
        //#ifdef H5
        this.checkLogin()
        //#endif
      },
      // 初始化数据库
      initDateBase(){
        dbUtils.openDb("tb_screen")
        dbUtils.init("tb_screen",tableSqls)
        dbUtils.closeSQL("th_screen")
      },
      initConfig() {
        this.globalData.config = config
      },
      checkLogin() {
        if (!getAccessToken()) {
          this.$tab.reLaunch('/pages/login')
        }
      }
    }
  }
</script>

<style lang="scss">
  @import '@/static/scss/index.scss'
</style>
