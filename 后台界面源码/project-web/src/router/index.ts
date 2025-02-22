import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import Layout from '@/layout/index.vue';
//动态生成
export const constantRoutes: Array<RouteRecordRaw> = [
  {
    path: '/login',
    component: () => import('@/views/login/login.vue'),
    name: 'login',
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: '/dashboard',
        component: () => import('@/views/dashboard/Index.vue'),
        name: 'dashboard',
        meta: {
          title: '首页',
          icon: 'House',
        },
      },
    ],
  },
];
//静态生成
// const routes: Array<RouteRecordRaw> = [
//   {
//     path: '/login',
//     name: 'login',
//     component: () => import('@/views/login/login.vue'),
//   },
//   {
//     path: '/',
//     component: Layout,
//     redirect: '/dashboard',
//     children: [
//       {
//         path: '/dashboard',
//         component: () => import('@/views/dashboard/Index.vue'),
//         name: 'dashboard',
//         meta: {
//           title: '首页',
//           icon: '#icondashboard',
//         },
//       },
//     ],
//   },
//   {
//     path: '/system',
//     component: Layout,
//     name: 'system',
//     meta: {
//       title: '系统管理',
//       icon: 'Setting',
//       roles: ['sys:manager'],
//     },
//     children: [
//       {
//         path: '/userList',
//         component: () => import('@/views/system/User/UserList.vue'),
//         name: 'userList',
//         meta: {
//           title: '用户管理',
//           icon: 'UserFilled',
//           roles: ['sys:user'],
//         },
//       },
//       {
//         path: '/roleList',
//         component: () => import('@/views/system/Role/RoleList.vue'),
//         name: 'roleList',
//         meta: {
//           title: '角色管理',
//           icon: 'wallet',
//           roles: ['sys:role'],
//         },
//       },
//       {
//         path: '/menuList',
//         component: () => import('@/views/system/Menu/MenuList.vue'),
//         name: 'menuList',
//         meta: {
//           title: '菜单管理',
//           icon: 'Menu',
//           roles: ['sys:menu'],
//         },
//       },
//     ],
//   },
//   {
//     path: '/goodsRoot',
//     component: Layout,
//     name: 'goodsRoot',
//     meta: {
//       title: '商品管理',
//       icon: 'Setting',
//       roles: ['sys:goodsRoot'],
//     },
//     children: [
//       {
//         path: '/category',
//         component: () => import('@/views/category/CategoryList.vue'),
//         name: 'category',
//         meta: {
//           title: '商品类型',
//           icon: 'UserFilled',
//           roles: ['sys:category'],
//         },
//       },
//       {
//         path: '/goodsList',
//         component: () => import('@/views/goods/GoodsList.vue'),
//         name: 'goodsList',
//         meta: {
//           title: '商品信息',
//           icon: 'wallet',
//           roles: ['sys:goodsList'],
//         },
//       },
//     ],
//   },
// ];

const router = createRouter({
  history: createWebHistory(),
  routes:constantRoutes,
});

export default router;
console.log("这个是router"+router)
