import http from "@/http";
import { MenuType } from "./MenuModel";
//获取上级菜单
export const getParentApi = () =>{
  return http.get("/api/sysMenu/getParent");
};
//新增
export const addApi = (parm:MenuType) =>{
  return http.post("/api/sysMenu",parm);
};
//列表
export const getListApi = () =>{
  return http.get("/api/sysMenu/list");
};