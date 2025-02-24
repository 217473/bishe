<template>
    <div class="logo">
        <img :src="MenuLogo"/>
        <!-- 文字显示方式 -->
        <span v-if="show" class="logo-title">{{ title }}</span>
        <!-- 图片显示方式 -->
         <!-- <img v-if="show" class="title" :src="title"/> -->
    </div>
</template>

<script setup lang="ts">
import {ref,watch } from 'vue'
import MenuLogo from '@/assets/MenuLogo.png'
// import title from '@/assets/title.png'
import { menuStore } from '@/store/menu/index';
//获取store
const store = menuStore();
const title = ref('智慧医疗管理系统')
// const show = computed(() => {
//     return store.getCollapse
// })
//解决伸缩僵硬问题：通过延时
const show = ref(true)
watch(
    () => store.getCollapse,
    (collapsed: boolean) => {
        if (!collapsed) {
            setTimeout(() => {
                show.value = !collapsed;
            }, 300);
        } else {
            show.value = !collapsed;
        }
    }
)
</script>

<style scoped lang="scss"">
.logo{
    display: flex;
    width: 100%;
    height: var(--el-header-height);
    background-color: var(--el-color-mylogo);
    text-align: center;
    cursor: pointer;
    align-items: center;
    img{
        width: 50px;
        height: 45px;
        margin-left: 10px;
        margin-right: 2px;
    }
    .logo-title{
    color: var(--el-color-logo-color);
    font-weight: bold;
    line-height: var(--el-header-height);
    font-size: 21px;
    font-family: Avenir, Helvetica, Neue,Helvetica,sans-serif;//FangSong
}
.title{
    width: 160px;
    height: 55px;
}

}

</style>