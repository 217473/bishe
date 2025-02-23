import { createApp } from 'vue';
//引入element plus
import ElementPlus from 'element-plus';
//国际化
import zhCn from 'element-plus/es/locale/lang/zh-cn';
import 'element-plus/dist/index.css';
import * as ElementPlusIconsVue from '@element-plus/icons-vue';
import piniaPersist from 'pinia-plugin-persist';
import myconfirm from './utils/myconfirm';
import hasPerm from './directive/hasPerm';
//echarts
import * as echarts from 'echarts';
//按钮权限指令
// import { permission } from './directive/permission';
//权限验证
import './permisson';
// import './style.css'
import App from './App.vue';
// 引入Pinia构造函数
import { createPinia } from 'pinia';
import router from './router/index';
// 实例化 Pinia
const pinia = createPinia();
//使用持久化插件
pinia.use(piniaPersist);
// createApp(App).mount('#app')
const app = createApp(App);
// app.directive('permission', permission);
app.use(router).use(ElementPlus, { locale: zhCn }).use(pinia).mount('#app');
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}
//全局挂载
app.config.globalProperties.$myconfirm = myconfirm;
app.config.globalProperties.$hasPerm = hasPerm;
app.config.globalProperties.$echarts = echarts;
