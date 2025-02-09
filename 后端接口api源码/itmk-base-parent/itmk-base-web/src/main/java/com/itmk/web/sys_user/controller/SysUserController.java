package com.itmk.web.sys_user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import com.itmk.web.sys_user.entity.SysUser;
import com.itmk.web.sys_user.entity.SysUserPage;
import com.itmk.web.sys_user.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RequestMapping("/api/sysUser")
@RestController
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

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
        query.lambda().orderByAsc(SysUser::getCreateTime);
        //查询列表
        IPage<SysUser> list = sysUserService.page(page, query);
        return ResultUtils.success("查询成功",list);
    }
}

