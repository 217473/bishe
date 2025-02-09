<template>
    <div>
        <el-button type="primary" size="default" @click="addBtn">新增</el-button>
        <!-- 使用弹窗 -->
         <SysDialog
         :title="dialog.title"
         :visible="dialog.visible"
         :width="dialog.width"
         :height="dialog.height"
         @on-close="onClose"
         @on-confirm="onConfirm"
         >
        <!-- 弹框内容 -->
         <template v-slot:content>
            <div>张三</div>
         </template>
        </SysDialog>
    </div>
</template>

<script setup lang="ts">
import SysDialog from '@/components/SysDialog.vue';
import useDialog from '@/hooks/useDialog';
//弹框属性
const {dialog,onClose,onConfirm,onShow} = useDialog();

//新增按钮
const addBtn = () => {
    dialog.title = '新增'
    onShow()
};

</script>

<style scoped>

</style>