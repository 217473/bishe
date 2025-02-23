<template>
  <MenuLogo></MenuLogo>
  <el-menu
    :default-active="defauLtActive"
    class="el-menu-vertical-demo"
    :collapse="isCollapse"
    @open="handleOpen"
    @close="handleClose"
    background-color="#304156"
    unique-opened
    router
  >
    <MenuItem :menuList="menuList"></MenuItem>
  </el-menu>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { menuStore } from '@/store/menu/index';
import { useRoute } from 'vue-router';
import MenuItem from './MenuItem.vue';
import MenuLogo from './MenuLogo.vue';

//获取store
const store = menuStore();
//获取状态
const isCollapse = computed(() => {
  return store.getCollapse;
});
const route = useRoute();
//定义响应式数据
// const isCollapse = ref(false);
//当前激活的菜单：当前激活的菜单
const defauLtActive = computed(() => {
  const { path } = route
  return path
})
//菜单数据
const menuList = computed(() => {
  return store.getMenu
})
// let menuList = reactive([
//   {
//     path: '/dashboard',
//     component: "/dashboard/Index",
//     name: "dashboard",
//     meta: {
//       title: "首页",
//       icon: "House",
//       roles:["sys:dashboard"],
//     }
//     },
//   {
//     path: '/system',
//     component: 'Layout',
//     name: 'system',
//     meta: {
//       title: '系统管理',
//       icon: 'Setting',
//       roles: ['sys:manage'],
//     },
//     children: [
//       {
//         path: 'userList',
//         component: '/system/User/UserList',
//         name: 'userList',
//         meta: {
//           title: '用户管理',
//           icon: 'UserFilled',
//           roles: ['sys:user'],
//         },
//       },
//       {
//         path: '/roleList',
//         component: '/system/Role/RoleList',
//         name: 'roleList',
//         meta: {
//           title: '角色管理',
//           icon: 'wallet',
//           roles: ['sys:role'],
//         },
//       },
//       {
//         path: '/menuList',
//         component: '/system/Menu/MenuList',
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
//     component: 'Layout',
//     name: 'goodsRoot',
//     meta: {
//       title: '商品管理',
//       icon: 'Setting',
//       roles: ['sys:goodsRoot'],
//     },
//     children: [
//       {
//         path: '/category',
//         component: '/goods/Category',
//         name: 'category',
//         meta: {
//           title: '物资类型',
//           icon: 'UserFilled',
//           roles: ['sys:category'],
//         },
//       },
//       {
//         path: '/goodsList',
//         component: '/goods/GoodsList',
//         name: 'goodsList',
//         meta: {
//           title: '商品信息',
//           icon: 'wallet',
//           roles: ['sys:goodsList'],
//         },
//       },
//     ],
//   },
// ]);
const handleOpen = (key: string, keyPath: string[]) => {
  console.log(key, keyPath);
};
const handleClose = (key: string, keyPath: string[]) => {
  console.log(key, keyPath);
};
</script>

<style scoped lang="scss">
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 230px;
  min-height: 400px;
}
.el-menu {
  border-right: none;
}
:deep(.el-sub-menu .el-sub-menu__title) {
  color: #f4f4f5 !important;
}
:deep(.el-menu-item) {
  color: #bfcbd9;
}

// 菜单点中文字的颜色
:deep(.el-menu-item.is-active) {
  color: #409eff !important;
}

// 当前打开菜单的所有子菜单颜色
:deep(.is-opened .el-menu-item) {
  background-color: #1f2d3d !important;
}

// 移动鼠标菜单的颜色
:deep(.el-menu-item:hover) {
  background-color: #001528 !important;
}
</style>
