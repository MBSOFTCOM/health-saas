import tab from './tab';
import auth from './auth';
import modal from './modal';

const MyPlugin = {
  install: (app) => {
    // 页签操作
    app.config.globalProperties.$tab = tab;
    // 认证对象
    app.config.globalProperties.$auth = auth;
    // 模态框对象
    app.config.globalProperties.$modal = modal;
  },
};

export default MyPlugin;
