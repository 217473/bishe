<template>
  <el-main>
    <!-- 搜索栏 -->
    <el-form :model="searchParm" :inline="true" size="default">
      <el-form-item>
        <el-input
          placeholder="请输入姓名"
          v-model="searchParm.nickName"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-input
          placeholder="请输入电话"
          v-model="searchParm.phone"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button icon="Search">搜索</el-button>
        <el-button icon="Close" type="danger" plain>重置</el-button>
        <el-button icon="Plus" type="primary" @click="addBtn">新增</el-button>
      </el-form-item>
    </el-form>
  </el-main>
  <!-- 新增编辑 -->
  <SysDialog
    :title="dialog.title"
    :width="dialog.width"
    :height="dialog.height"
    :visible="dialog.visible"
    @on-close="onClose"
    @on-confirm="commit"
  >
    <template v-slot:content>
      <el-form
        :model="addModel"
        ref="addForm"
        :rules="rules"
        label-width="80px"
        :inline="false"
        size="default"
      >
        <el-row :gutter="20">
          <el-col :span="12" :offset="0">
            <el-form-item prop="nickName" label="姓名:">
              <el-input v-model="addModel.nickName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" :offset="0">
            <el-form-item prop="sex" label="性别:">
              <el-radio-group v-model="addModel.sex">
                <el-radio label="'0'">男</el-radio>
                <el-radio label="'1'">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" :offset="0">
            <el-form-item prop="phone" label="电话:">
              <el-input v-model="addModel.phone"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" :offset="0">
            <el-form-item prop="email" label="邮箱:">
              <el-input v-model="addModel.email"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" :offset="0">
            <el-form-item prop="roleId" label="角色:">
              <SelectChecked
                :options="options"
                @selected="selected"
                ref="selectRef"
              ></SelectChecked>
            </el-form-item>
          </el-col>
          <el-col :span="12" :offset="0">
            <el-form-item prop="userName" label="账号:">
              <el-input v-model="addModel.userName"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" :offset="0">
            <el-form-item prop="password" label="密码:">
              <el-input v-model="addModel.password"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </template>
  </SysDialog>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import SysDialog from '@/components/SysDialog.vue';
import useDialog from '@/hooks/useDialog';
import { ElMessage, FormInstance } from 'element-plus';
import SelectChecked from '@/components/SelectChecked.vue';
import { getSelectApi } from '@/api/role/index';
import { addApi, getListApi } from '@/api/user/index';
import { nextTick } from 'process';

const selectRef = ref();
//下拉数据
let options = ref([]);
//表单ref属性
const addForm = ref<FormInstance>();
//弹框属性
const { dialog, onClose, onShow } = useDialog();

//搜索栏绑定对象
const searchParm = reactive({
  phone: '',
  nickName: '',
  currentPage: 1,
  pageSize: 10,
  total: 0,
});
//新增绑定对象
const addModel = reactive({
  userId: '',
  userName: '',
  password: '',
  phone: '',
  email: '',
  sex: '',
  nickName: '',
  roleId: '',
});
//表单验证规则
const rules = reactive({
  //姓名验证
  nickName: [
    {
      required: true,
      message: '请输入姓名',
      trigger: ['blur', 'change'],
    },
  ],
  //性别验证
  sex: [
    {
      required: true,
      message: '请选择性别',
      trigger: ['blur', 'change'],
    },
  ],
  //电话验证
  phone: [
    {
      required: true,
      message: '请输入电话',
      trigger: ['blur', 'change'],
    },
  ],
  //账号验证
  userName: [
    {
      required: true,
      message: '请输入账号',
      trigger: ['blur', 'change'],
    },
  ],
  //密码验证
  password: [
    {
      required: true,
      message: '请输入密码',
      trigger: ['blur', 'change'],
    },
  ],
  //角色验证。选择
  roleId: [
    {
      required: true,
      message: '请选择角色',
      trigger: ['blur', 'change'],
    },
  ],
});
//查询角色下拉数据
const getSelect = async () => {
  let res = await getSelectApi();
  if (res && res.code == 200) {
    options.value = [];
    options.value = res.data;
  }
};

//新增按钮
const addBtn = () => {
  console.log('新增按钮');
  //清空下拉数据
  options.value = [];
  //获取下拉数据
  getSelect();
  dialog.title = '新增';
  dialog.height = 230;
  //显示弹框
  onShow();
  nextTick(() => {
    //清空下拉数据
    selectRef.value.clear();
  });
  //清空表单
  addForm.value?.resetFields();
};


//勾选的值
const selected = (value: Array<string | number>) => {
  console.log(value);
  addModel.roleId = value.join(','); //将选中的值绑定到表单数据   自己加的
  console.log(addModel);
};

//提交表单
const commit = () => {
  //验证表单
  addForm.value?.validate(async (valid) => {
    if (valid) {
      console.log('表单验证通过', addModel);
      let res = await addApi(addModel);
      if (res && res.code === 200) {
        ElMessage.success(res.msg);
        onClose();
      }
    }
  });
};
//表格数据
const tableList = ref([]);
//查询表格数据
const getList = async () => {
  let res = await getListApi(searchParm);
  if (res && res.code === 200) {
    tableList.value = res.data.records;
    searchParm.total = res.data.total;
  }
};
onMounted(() => {
  getSelect();
});
</script>

<style scoped></style>
