import { userSotre } from "@/store/user";
import { Directive } from "vue";

//自定义按钮权限指令
export const permission: Directive = {
    mounted(el, binding) {
        const store = userSotre();
        //按钮上的权限字段
        const { value } = binding;
        //当前用户的所有权限字段
        const permission = store.getCodeList;
        if (value && value instanceof Array && value.length > 0) {
            const permissionRoles = value;
            //判断按钮权限是否存在用户的权限里面
            const hasPermission = permission.some((role) => {
                return permissionRoles.includes(role)
            })
            //如果没有按钮权限，隐藏按钮
            if (!hasPermission) {
                el.style.display = 'none';
            }
            if (!hasPermission) {
                el.style.display = 'none';
            }
        } else {
            throw new Error("按钮权限的传递方式v-permission:['sys:role:add']")
        }
    }
}