<template>
    <div class="logo">
        <img :src="MenuLogo"/>
        <span v-if="show" class="logo-title">{{ title }}</span>
    </div>
</template>

<script setup lang="ts">
import {ref,watch } from 'vue'
import MenuLogo from '@/assets/MenuLogo.png'
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
    height: 60px;
    background-color: #2b2f3a;
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
    color: #fff;
    font-weight: 800;
    line-height: 60px;
    font-size: 22px;
    font-family: FangSong;
}

}

</style>