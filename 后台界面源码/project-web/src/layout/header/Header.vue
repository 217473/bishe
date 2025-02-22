<template>
    <div class="header-container">
       <div class="left">
        <Collapse></Collapse>
       <BreadCrumb></BreadCrumb>
       </div>
       <div class="right">
        <span style="padding-right: 100px; color: aliceblue;" >欢迎您，{{ name }}</span>
        <LoginOut></LoginOut>
       </div>
    </div>
</template>

<script setup lang="ts">
import Collapse from './Collapse.vue';
import BreadCrumb from './BreadCrumb.vue';
import LoginOut from './LoginOut.vue';
import { userSotre } from '@/store/user';
import { computed } from 'vue';
const store = userSotre();
const name = computed(() => {
    return store.getNickName
})
</script>

<style scoped lang="scss">
.header-container{
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    .left{
        display: flex;
        justify-content: center;
        align-items: center;
    }
    .right{
        display: flex;
        align-items: center;
    }
}
// :deep(.el-dropdown-link:focus){
//     outline: none ;
// }
</style>