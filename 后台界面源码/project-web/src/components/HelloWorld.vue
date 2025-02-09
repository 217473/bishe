<template>
  <el-button type="primary" size="default" @click="addBtn">新增</el-button>
  
  <div>
    {{ count }}
  </div>
</template>

<script setup lang="ts">
import { testSotre } from '@/store/test/index';
import { computed } from 'vue';
//获取store
const store = testSotre();
//cont展示到页面
const count = computed(() => {
  return store.getCount;
});

//点击按钮改变count值
const addBtn = () => {
  store.setCount(++store.count)
}
</script>

<style scoped></style>
