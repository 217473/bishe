<template>
  <el-main>
    <!-- 搜索栏 -->
    <el-form :model="reactive" :inline="true" size="default">
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
        <el-button icon="Search" @click="searchBtn">搜索</el-button>
        <el-button icon="Close" @click="resetBtn" type="danger" plain
          >重置</el-button
        >
        <el-button icon="Plus" type="primary" @click="addBtn">新增</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格数据 -->
    <el-table :height="tableHeight" :data="tableList" border stripe>
      <el-table-column prop="nickName" label="姓名"></el-table-column>
      <el-table-column prop="sex" label="性别">
        <template #default="scope">
          <el-tag
            v-if="scope.row.sex == '0'"
            type="primary"
            size="default"
            effect="dark"
            >男</el-tag
          >
          <el-tag
            v-if="scope.row.sex == '1'"
            type="danger"
            size="default"
            effect="dark"
            >女</el-tag
          >
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="电话"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column align="center" width="320" label="操作">
        <template #default="scope">
          <el-button
            type="primary"
            icon="Edit"
            size="default"
            @click="editBtn(scope.row)"
            >编辑</el-button
          >
          <el-button
            type="warning"
            icon="Setting"
            size="default"
            @click="resetPasswordBtn(scope.row.userId)"
            >重置密码</el-button
          >
          <el-button
            type="danger"
            icon="Delete"
            size="default"
            @click="deleteBtn(scope.row.userId)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <el-pagination
      @size-change="sizeChange"
      @current-change="currentChange"
      :current-page.sync="searchParm.currentPage"
      :page-sizes="[10, 20, 40, 80, 100]"
      :page-size="searchParm.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="searchParm.total"
      background
    >
    </el-pagination>
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
            <el-form-item prop="nickName" label="姓名">
              <el-input v-model="addModel.nickName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" :offset="0">
            <el-form-item prop="sex" label="性别:">
              <el-radio-group v-model="addModel.sex">
                <el-radio label="0">男</el-radio>
                <el-radio label="1">女</el-radio>
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
                :bindValue="bindValue"
                @selected="selected"
                ref="selectRef"
              ></SelectChecked>
            </el-form-item>
          </el-col>
          <el-col :span="12" :offset="0">
            <el-form-item prop="username" label="账号:">
              <el-input v-model="addModel.username"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="tags == '0'">
          <el-col :span="12" :offset="0">
            <el-form-item prop="password" label="密码:">
              <el-input type="password" v-model="addModel.password"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </template>
  </SysDialog>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, nextTick } from 'vue';
import SysDialog from '@/components/SysDialog.vue';
import useDialog from '@/hooks/useDialog';
import { ElMessage, FormInstance } from 'element-plus';
import SelectChecked from '@/components/SelectChecked.vue';
import { getSelectApi } from '@/api/role/index';
import {
  addApi,
  getListApi,
  getRoleListApi,
  editApi,
  deleteApi,
  resetPasswordApi,
} from '@/api/user/index';
import { User } from '@/api/user/UserModel';
import useInstance from '@/hooks/useInstance';
const { global } = useInstance();
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
  username: '',
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
  username: [
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
    addModel.roleId = res.data.join(',');
  }
};
//用户拥有的角色Id
const bindValue = ref([]);
const roleIds = ref('');
//根据用户ID查询角色
const getRoleList = async (userId: string) => {
  let res = await getRoleListApi(userId);
  if (res && res.code == 200) {
    bindValue.value = res.data;
    roleIds.value = res.data.join(',');
  }
};
const tags = ref('');
//新增按钮
const addBtn = () => {
  tags.value = '0';
  dialog.title = '新增';
  dialog.height = 230;
  //显示弹框
  onShow();
  console.log('新增按钮');
  //清空下拉数据
  options.value = [];
  bindValue.value = [];
  //获取下拉数据
  getSelect();
  nextTick(() => {
    //清空下拉数据
    selectRef.value.clear();
  });
  //清空表单
  addForm.value?.resetFields();
};

//编辑
const editBtn = async (row: User) => {
  tags.value = '1';
  dialog.title = '编辑';
  dialog.height = 230;
  //清空下拉数据
  options.value = [];
  bindValue.value = [];
  //获取下拉数据
  await getSelect();
  //查询角色id
  await getRoleList(row.userId);
  //显示弹框
  onShow();
  nextTick(() => {
    //数据回显
    Object.assign(addModel, row);
    //设置角色的id
    addModel.roleId = roleIds.value;
    addModel.password = '';
  });
  //清空表单
  addForm.value?.resetFields();
};
//删除
const deleteBtn = async (userId: string) => {
  console.log(userId);
  const confirm = await global.$myconfirm('确定删除该数据吗？');
  if (confirm) {
    let res = await deleteApi(userId);
    if (res && res.code == 200) {
      ElMessage.success(res.msg);
      //刷新列表
      getList();
    }
  }
};
//重置密码
const resetPasswordBtn = async (userId: string) => {
  const confirm = await global.$myconfirm(
    '确定重置密码吗?重置之后密码是【666666】'
  );
  if (confirm) {
    let res = await resetPasswordApi({ userId: userId });
    if (res && res.code == 200) {
      ElMessage.success(res.msg);
      //刷新列表
      getList();
    }
  }
};
//勾选的值
const selected = (value: Array<string | number>) => {
  // console.log(value);
  addModel.roleId = value.join(','); //将选中的值绑定到表单数据   自己加的
  console.log(addModel);
};

//提交表单
const commit = () => {
  //验证表单
  addForm.value?.validate(async (valid) => {
    if (valid) {
      console.log('表单验证通过');
      let res = null;
      if (tags.value == '0') {
        res = await addApi(addModel);
      } else {
        res = await editApi(addModel);
      }
      if (res && res.code === 200) {
        ElMessage.success(res.msg);
        getList();
        onClose();
      }
    }
  });
};
//表格数据
const tableList = ref([]);
//查询表格数据
const getList = async () => {
  console.log('请求参数:', searchParm); // 打印请求参数
  let res = await getListApi(searchParm);
  if (res && res.code == 200) {
    console.log('API 返回结果:', res);
    tableList.value = res.data.records;
    searchParm.total = res.data.total;
  }
};
//页容量改变时触发
const sizeChange = (size: number) => {
  searchParm.pageSize = size;
  getList();
};
//页码改变时触发
const currentChange = (page: number) => {
  searchParm.currentPage = page;
  getList();
};
//表格高度
const tableHeight = ref(0);
//搜索按钮点击事件
const searchBtn = () => {
  console.log('搜索姓名:', searchParm.nickName);
  console.log('搜索电话:', searchParm.phone);
  getList();
  console.log('搜索');
};
//重置按钮点击事件
const resetBtn = () => {
  searchParm.nickName = '';
  searchParm.phone = '';
  searchParm.currentPage = 1;
  getList();
};
onMounted(() => {
  nextTick(() => {
    tableHeight.value = window.innerHeight - 200;
  });
  getList();
});
</script>

<style scoped></style>
