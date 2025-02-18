//定义用户数据类型
export type User = {
  userId: string;
  username: string;
  password: string;
  phone: string;
  email: string;
  sex: string;
  nickName: string;
  roleId: string;
};
//列表参数类型
export type UserListParm = {
  phone: string;
  nickName: string;
  currentPage: number;
  pageSize: number;
  total: number;
};
//登录
export type Login = {
  username: string;
  password: string;
  code: string;
};
