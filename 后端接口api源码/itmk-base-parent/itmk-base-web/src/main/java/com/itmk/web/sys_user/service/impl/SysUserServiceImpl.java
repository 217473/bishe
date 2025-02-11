package com.itmk.web.sys_user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itmk.web.sys_user.entity.SysUser;
import com.itmk.web.sys_user.mapper.SysUserMapper;
import com.itmk.web.sys_user.service.SysUserService;
import com.itmk.web.sys_user_role.entity.SysUserRole;
import com.itmk.web.sys_user_role.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Transactional
    @Override
    public void saveUser(SysUser sysUser) {
    //插入用户信息
        int i = this.baseMapper.insert(sysUser);
        //设置用户的角色
        if (i>0){
            //把前端逗号分隔的字符串转为数组
            String[] split = sysUser.getRoleId().split(",");
            if (split.length>0){
                List<SysUserRole> roles = new ArrayList<>();
                for (int j = 0; j < split.length; j++) {
                    SysUserRole userRole = new SysUserRole();
                    userRole.setUserId(sysUser.getUserId());
                    userRole.setRoleId(Long.parseLong(split[j]));
                    roles.add(userRole);
                }
                //保存到用户角色表
                sysUserRoleService.saveBatch(roles);
            }
        }
    }

    @Override
    @Transactional
    public void editUser(SysUser sysUser) {
        //编辑用户信息
        int i = this.baseMapper.updateById(sysUser);
        //设置用户的角色
        if (i>0){
            //把前端逗号分隔的字符串转为数组
            String[] split = sysUser.getRoleId().split(",");
            //删除用户原来的角色
            QueryWrapper<SysUserRole> query = new QueryWrapper<>();
            query.lambda().eq(SysUserRole::getUserId,sysUser.getUserId());
            sysUserRoleService.remove(query);
            //重新插入
            if (split.length>0){
                List<SysUserRole> roles = new ArrayList<>();
                for (int j = 0; j < split.length; j++) {
                    SysUserRole userRole = new SysUserRole();
                    userRole.setUserId(sysUser.getUserId());
                    userRole.setRoleId(Long.parseLong(split[j]));
                    roles.add(userRole);
                }
                //保存到用户角色表
                sysUserRoleService.saveBatch(roles);
            }
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        //删除用户
        int i = this.baseMapper.deleteById(userId);
        if(i>0){
            //删除角色
            //删除用户原来的角色
            QueryWrapper<SysUserRole> query = new QueryWrapper<>();
            query.lambda().eq(SysUserRole::getUserId,userId);
            sysUserRoleService.remove(query);
        }

    }
}
