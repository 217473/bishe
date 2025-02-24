<template>
  <template v-for="menu in menuList" :key="menu.path">
    <!-- 有下级菜单 -->
    <el-sub-menu
      v-if="menu.children && menu.children.length > 0 && !menu.redirect"
      :index="menu.path"
    >
      <template #title>
        <el-icon>
          <!-- 动态组件的方式生成图标 -->
          <component :is="menu.meta.icon"></component>
        </el-icon>
        <span style="font-size: var(--el-mymenu-font); font-weight: bold;">{{ menu.meta.title }}</span>
      </template>
      <!-- 生成下级:递归调用：自己调用自己 -->
      <menu-item :menuList="menu.children"></menu-item>
    </el-sub-menu>
    <!-- 没有下级菜单 -->
    <el-menu-item class="first" v-else :index="menu.path">
      <el-icon>
        <!-- 动态组件的方式生成图标 -->
        <component :is="menu.meta.icon"></component>
      </el-icon>
      <template #title><span style="font-size: var(--el-mymenu-font);font-weight: bold;">{{ menu.meta.title }}</span></template>
    </el-menu-item>
  </template>
</template>

<script setup lang="ts">
//通过defineProps接收父组件传递的数据
defineProps(['menuList']);
</script>

<style scoped lang="scss">
.first{
  color: var(--el-color-mymenu-first) !important;
}
</style>
