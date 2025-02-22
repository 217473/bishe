package com.itmk.web.sys_role.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import com.itmk.web.sys_role.entity.RoleParm;
import com.itmk.web.sys_role.entity.SelectItme;
import com.itmk.web.sys_role.entity.SysRole;
import com.itmk.web.sys_role.service.SysRoleService;
import com.itmk.web.sys_role_menu.RoleMenu.RoleMenuService;
import com.itmk.web.sys_role_menu.entity.SaveMenuParm;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@RequestMapping("/api/role")
@RestController
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private RoleMenuService roleMenuService;

    // 新增
    @PostMapping
    public ResultVo add(@RequestBody SysRole sysRole){
        // 使用 java.util.Date 设置创建时间
        sysRole.setCreateTime(new Date());  // 或者使用 LocalDateTime.now() 如果字段类型是 LocalDateTime
        if (sysRoleService.save(sysRole)){
            return ResultUtils.success("新增成功！");
        }
        return ResultUtils.error("新增失败！");
    }

    // 编辑
    @PutMapping
    public ResultVo edit(@RequestBody SysRole sysRole){
        // 使用 java.util.Date 设置更新时间
        sysRole.setUpdateTime(new Date()); // 或者使用 LocalDateTime.now() 如果字段类型是 LocalDateTime
        if (sysRoleService.updateById(sysRole)){
            return ResultUtils.success("编辑成功！");
        }
        return ResultUtils.error("编辑失败！");
    }
    //删除
    @DeleteMapping("/{roleId}")
    public ResultVo delete(@PathVariable("roleId") Long roleId){
        if (sysRoleService.removeById(roleId)){
            return ResultUtils.success("删除成功！");
        }
        return ResultUtils.error("删除失败！");
    }

    //列表
    @GetMapping("/getList")
    public ResultVo getList(RoleParm parm){
        //构造分页对象
        IPage<SysRole> page = new Page<>(parm.getCurrentPage(),parm.getPageSize());
        //构造查询条件
        QueryWrapper<SysRole> query = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(parm.getRoleName())){
            query.lambda().like(SysRole::getRoleName,parm.getRoleName());
        }
        query.lambda().orderByDesc(SysRole::getCreateTime);
        IPage<SysRole> list = sysRoleService.page(page, query);
        return ResultUtils.success("查询成功",list);
    }
    //角色下拉数据
    @GetMapping("/selectList")
    public ResultVo selectList(){
        List<SysRole> list = sysRoleService.list();
        //返回的值
        List<SelectItme> selectItmes = new ArrayList<>();
        Optional.ofNullable(list).orElse(new ArrayList<>())
                .forEach(item ->{
                    SelectItme vo = new SelectItme();
                    vo.setCheck(false);
                    vo.setLabel(item.getRoleName());
                    vo.setValue(item.getRoleId());
                    selectItmes.add(vo);
                });
        return ResultUtils.success("查询成功",selectItmes);
    }
    //保存角色菜单
    @PostMapping("/saveRoleMenu")
    public ResultVo saveRoleMenu(@RequestBody SaveMenuParm parm) {
        roleMenuService.saveRoleMenu(parm);
        return ResultUtils.success("分配成功！");
    }
}

