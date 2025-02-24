<template>
  <el-container class="mycontainer">
    <el-aside 
    width="auto" 
    :class="[menubg == 'white'?'leftmenu':'leftmenu-default' ]"
    >
    <MenuBar></MenuBar>
    </el-aside>
    <el-container>
      <el-header id="myheader" class="header">
        <Header></Header>
      </el-header>
      <el-main class="main">
        <div class="tabs">
          <Tabs class="tabs-content"></Tabs>
          <ColseTabs></ColseTabs>
        </div>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import Header from './header/Header.vue';
import MenuBar from './menu/MenuBar.vue';
import Tabs from './tabs/Tabs.vue';
import ColseTabs from './tabs/ColseTabs.vue';
import { ref, nextTick } from 'vue';
//菜单背景色
let menubg = ref('');
//头部背景色
let headerbg = ref('');
nextTick(() => {
  let menubox = document.getElementById("mymenu") as HTMLElement;
  menubg.value = getComputedStyle(menubox).getPropertyValue("--el-color-mymenu");
  //如果头骨背景色是白色
  let headerbox = document.getElementById("myheader") as HTMLElement;
  headerbg.value = getComputedStyle(headerbox).getPropertyValue("--el-color-myheader");
  if (headerbg.value == 'white') {
    headerbox.style.borderBottom = '1px solid #e5e5e5';
  }
});
</script>

<style scoped>
.mycontainer {
  height: 100%;
  .leftmenu-default {
    background-color: var(--el-color-mymenubg);
  }
  .leftmenu{
    background-color: var(--el-color-mymenubg);
    border-right: 1px solid rgb(230, 230, 230);
  }
  .header {
    height: var(--el-header-height);
    background-color: var(--el-color-myheader);
    display: flex;
    align-items: center;
    border-left: 1px solid var(--el-color-myheaderleft);
  }
  .main {
    padding: 0px;
    /* background-color: #301616;  */
    .tabs {
      display: flex;
      justify-content: space-between;
      flex-grow: 1;
      border-bottom: 1px solid #e4e7ed;
      padding-right: 85px;
      .el-tabs {
        width: 100%;
        height: 33px;
      }
      .tabs-content {
        padding-top: 8px;
        padding-left: 10px;
        padding-right: 10px;
      }
    }
  }
}
</style>
