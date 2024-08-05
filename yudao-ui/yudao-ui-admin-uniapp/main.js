import { createSSRApp  } from 'vue'
import App from './App.vue';
import store from './store'; // store
import plugins from './plugins'; // plugins
import './permission'; // permission
import GlobalConfigPlugin from './plugins/GlobalConfigPlugin'
import uviewPlus from '@/uni_modules/uview-plus'
import dbUtils from "@/uni_modules/zjy-sqlite-manage/components/zjy-sqlite-manage/dbUtils.js" //sqlite-manage插件



export function createApp(){
  const app = createSSRApp(App);
  app.config.globalProperties.$dbUtils = dbUtils;
  app.use(GlobalConfigPlugin)
  app.use(store);
  app.use(plugins);
  app.use(uviewPlus)
  return{
    app
  }
}


