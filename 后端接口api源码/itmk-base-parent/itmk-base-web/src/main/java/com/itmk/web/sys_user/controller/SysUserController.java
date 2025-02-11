package com.itmk.web.sys_user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import com.itmk.web.sys_user.entity.SysUser;
import com.itmk.web.sys_user.entity.SysUserPage;
import com.itmk.web.sys_user.service.SysUserService;
import com.itmk.web.sys_user_role.entity.SysUserRole;
import com.itmk.web.sys_user_role.service.SysUserRoleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequestMapping("/api/sysUser")
@RestController
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    //新增
    @PostMapping
    public ResultVo add(@RequestBody SysUser sysUser){
        sysUser.setCreateTime(new Date());
        sysUserService.saveUser(sysUser);
        return ResultUtils.success("新增成功！");
    }

    //编辑
    @PutMapping
    public ResultVo edit(@RequestBody SysUser sysUser){
        sysUser.setUpdateTime(new Date());
        sysUserService.editUser(sysUser);
        return ResultUtils.success("编辑成功！");
    }


    //删除
    @DeleteMapping("/{userId}")
    public ResultVo delete(@PathVariable("userId") Long userId){
        sysUserService.deleteUser(userId);
        return ResultUtils.success("删除成功！");
    }

    //列表
    @GetMapping("/List")
    public ResultVo list(SysUserPage parm){
        //构造分页对象
        IPage<SysUser> page = new Page<>(parm.getCurrentPage(),parm.getPageSize());
        //构造查询条件
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(parm.getPhone())){
            query.lambda().like(SysUser::getPhone,parm.getPhone());
        }
        // 新增对姓名的查询条件
        if (StringUtils.isNotEmpty(parm.getNickName())){
            query.lambda().like(SysUser::getNickName,parm.getNickName());
        }
        query.lambda().orderByAsc(SysUser::getCreateTime);
        //查询列表
        IPage<SysUser> list = sysUserService.page(page, query);
        return ResultUtils.success("查询成功",list);
    }
    //根据用户id查询用户的角色
    @GetMapping("/getRoleList")
    public ResultVo getRoleList(Long userId){
        QueryWrapper<SysUserRole> query = new QueryWrapper<>();
        query.lambda().eq(SysUserRole::getUserId,userId);
        List<SysUserRole> list = sysUserRoleService.list(query);
        //角色id
        List<Long> roleList = new ArrayList<>();
        Optional.ofNullable(list).orElse(new ArrayList<>())
                .forEach(item ->{
                    roleList.add(item.getRoleId());
                });
        return ResultUtils.success("查询成功",roleList);
    }
    //重置密码
    @PostMapping("/resetPassword")
    public ResultVo resetPassword(@RequestBody SysUser sysUser){
        UpdateWrapper<SysUser> query = new UpdateWrapper<>();
        query.lambda().eq(SysUser::getUserId,sysUser.getUserId())
                .set(SysUser::getPassword,"666666");
        if (sysUserService.update(query)){
            return ResultUtils.success("密码重置成功！");
        }
        return ResultUtils.error("密码重置失败！");
    }
}

