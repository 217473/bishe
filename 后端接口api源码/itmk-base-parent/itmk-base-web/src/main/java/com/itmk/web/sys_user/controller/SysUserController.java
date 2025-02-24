package com.itmk.web.sys_user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.itmk.jwt.JwtUtils;
import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import com.itmk.web.sys_menu.entity.AssignTreeParm;
import com.itmk.web.sys_menu.entity.AssignTreeVo;
import com.itmk.web.sys_menu.entity.SysMenu;
import com.itmk.web.sys_menu.service.SysMenuService;
import com.itmk.web.sys_user.entity.*;
import com.itmk.web.sys_user.service.SysUserService;
import com.itmk.web.sys_user_role.entity.SysUserRole;
import com.itmk.web.sys_user_role.service.SysUserRoleService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/api/sysUser")
@RestController
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private DefaultKaptcha defaultKaptcha;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //新增
    @PreAuthorize("hasAnyAuthority('sys:user:add')")
    @PostMapping
    public ResultVo add(@RequestBody SysUser sysUser){
        sysUser.setCreateTime(new Date());
        //密码加密
        sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        sysUserService.saveUser(sysUser);
        return ResultUtils.success("新增成功！");
    }

    //编辑
    @PreAuthorize("hasAnyAuthority('sys:user:edit')")
    @PutMapping
    public ResultVo edit(@RequestBody SysUser sysUser){
        sysUser.setUpdateTime(new Date());
        sysUserService.editUser(sysUser);
        return ResultUtils.success("编辑成功！");
    }


    //删除
    @PreAuthorize("hasAnyAuthority('sys:user:delete')")
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
    @PreAuthorize("hasAnyAuthority('sys:user:reset')")
    @PostMapping("/resetPassword")
    public ResultVo resetPassword(@RequestBody SysUser sysUser){
        UpdateWrapper<SysUser> query = new UpdateWrapper<>();
        query.lambda().eq(SysUser::getUserId,sysUser.getUserId())
                .set(SysUser::getPassword,passwordEncoder.encode("666666"));
        if (sysUserService.update(query)){
            return ResultUtils.success("密码重置成功！");
        }
        return ResultUtils.error("密码重置失败！");
    }
    //图片验证码
    @PostMapping("/getImage")
    public ResultVo imageCode(jakarta.servlet.http.HttpServletRequest request){
        //获取session
        jakarta.servlet.http.HttpSession session = request.getSession();
        //生成验证码
        String test = defaultKaptcha.createText();
        //存放到session
        session.setAttribute("code",test);
        //生成图片，转换为base64
        BufferedImage bufferedImage = defaultKaptcha.createImage(test);
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);
//            BASE64Encoder encoder = new BASE64Encoder();
//            String base64 = encoder.encode(outputStream.toByteArray());
            String base64 = Base64.encodeBase64String(outputStream.toByteArray());
            String captchaBase64 = "data:image/jpeg;base64," + base64.replaceAll("\r\n","");
            ResultVo result = new ResultVo("生成成功",200,captchaBase64);
            return result;
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if (outputStream != null){
                    outputStream.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }


    //登录
    @PostMapping("/login")
    public ResultVo login(HttpServletRequest request,@RequestBody LoginParm parm){
        //获取前端传递过来的code
        String code = parm.getCode();
        //获取session
        HttpSession session = request.getSession();
        //获取session里面的code
        String code1 = (String) session.getAttribute("code");
        if (StringUtils.isEmpty(code1)){
            return ResultUtils.error("验证码已过期，请重新获取！");
        }
        //判断前端传递过来的code和session里面的code是否一致
        if (!code1.equals(code)){
            return ResultUtils.error("验证码错误，请重新输入！");
        }
        //springSecurity加密之后的密码进行验证
        String password = passwordEncoder.encode(parm.getPassword());
        //查询用户信息,交给springSecurity进行验证查询
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(parm.getUsername(),parm.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //交给springSecurity进行验证
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        //获取用户信息
        SysUser user = (SysUser) authenticate.getPrincipal();

//        QueryWrapper<SysUser> query = new QueryWrapper<>();
//        query.lambda().eq(SysUser::getUsername,parm.getUsername())
//                .eq(SysUser::getPassword,parm.getPassword());
//        SysUser one = sysUserService.getOne(query);
//        if (one == null){
//            return ResultUtils.error("用户名或密码错误！");
//        }
        //返回用户信息和token
        LoginVo vo = new LoginVo();
        vo.setUserId(user.getUserId());
        vo.setNickName(user.getNickName());
        //生成token
        Map<String, String> map = new HashMap<>();
        map.put("userId",Long.toString(user.getUserId()));
        map.put("username",user.getUsername());
        String token = jwtUtils.generateToken(map);
        vo.setToken(token);
        return ResultUtils.success("登录成功！",vo);

    }
    //查询菜单树
    @GetMapping("/getAssignTree")
    public ResultVo getAssignTree(AssignTreeParm parm){
        AssignTreeVo assignTree = sysUserService.getAssignTree(parm);
        return ResultUtils.success("查询成功！",assignTree);
    }

    //修改登录密码
    @PostMapping("/updatePassword")
    public ResultVo updatePassword(@RequestBody UpdatePasswordParm parm){
        SysUser user = sysUserService.getById(parm.getUserId());
//        if(!parm.getOldPassword().equals(user.getPassword())){
//            return ResultUtils.error("原密码不正确！");
//        }
        if(!passwordEncoder.matches(parm.getOldPassword(),user.getPassword())){
            return ResultUtils.error("原密码不正确！");
        }
        //更新条件
        UpdateWrapper<SysUser> query = new UpdateWrapper<>();
        query.lambda().set(SysUser::getPassword,passwordEncoder.encode(parm.getPassword()))
                .eq(SysUser::getUserId,parm.getUserId());
        if (sysUserService.update(query)){
            return ResultUtils.success("密码修改成功！");
        }
        return ResultUtils.error("密码修改失败！");
    }

    //获取用户信息
    @GetMapping("/getInfo")
    public ResultVo getInfo(Long userId){
        //根据用户id查询用户信息
        SysUser user = sysUserService.getById(userId);
        List<SysMenu> menuList = null;
        //判断是否是超级管理员
        if(StringUtils.isNotEmpty(user.getIsAdmin()) && "1".equals(user.getIsAdmin())){
            //是超级管理员,查询所有菜单
            menuList = sysMenuService.list();
        }else{
            menuList = sysMenuService.getMenuByUserId(user.getUserId());
        }
        //获取菜单表的code字段
        List<String> collect = Optional.ofNullable(menuList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> item != null && StringUtils.isNotEmpty(item.getCode()))
                .map(item -> item.getCode())
                .collect(Collectors.toList());
        //设置返回值
        UserInfo userInfo = new UserInfo();
        userInfo.setName(user.getNickName());
        userInfo.setUserId(user.getUserId());
        userInfo.setPermissons(collect.toArray());
        return ResultUtils.success("查询成功！",userInfo);
    }

    //退出登录
    @PostMapping("loginOut")
    public ResultVo loginOut(HttpServletRequest request, HttpServletResponse response){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null ){
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }
        return ResultUtils.success("退出成功！");
    }
}

