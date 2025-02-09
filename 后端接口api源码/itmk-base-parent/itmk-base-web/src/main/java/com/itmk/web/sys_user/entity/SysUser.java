package com.itmk.web.sys_user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


import lombok.Data;
import java.util.Date;

@Data
@TableName("sys_user")
public class SysUser {
    @TableId(type = IdType.AUTO)
    private Long userId;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String sex;
    private String isAdmin;
//    不属于用户表,需要排除
    @TableField(exist = false)
    private String roleId;
    //账号是否过期 1:未过期 0:已过期
    private boolean isAccountNonExpired= true;
    //账号是否被锁定 1:未锁定 0:已锁定
    private boolean isAccountNonLocked= true;
    //密码是否过期 1:未过期 0:已过期
    private boolean isCredentialsNonExpired= true;
    //账号是否可用 1:可用 0:不可用
    private boolean isEnabled= true;
    private String nickName;

    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;

}
