/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2025/1/15 15:30:02                           */
/*==============================================================*/


drop table if exists sys_menu;

drop table if exists sys_role;

drop table if exists sys_role_menu;

drop table if exists sys_user;

drop table if exists sys_user_role;

/*==============================================================*/
/* Table: sys_menu                                              */
/*==============================================================*/
create table sys_menu
(
   menu_id              int not null auto_increment comment '菜单id',
   parent_id            int comment '上级id',
   title                varchar(36) comment '菜单名称',
   code                 varchar(36) comment '权限字段',
   name                 varchar(36) comment '路由名称',
   path                 varchar(36) comment '路由地址',
   url                  varchar(128) comment '组件路径',
   type                 varchar(2) comment '菜单类型',
   icon                 varchar(36) comment '菜单图标',
   parent_name          varchar(36) comment '上级菜单名称',
   order_num            int comment '序号',
   create_time          datetime comment '创建时间',
   update_time          datetime comment '更新时间',
   primary key (menu_id)
);

/*==============================================================*/
/* Table: sys_role                                              */
/*==============================================================*/
create table sys_role
(
   role_id              int not null auto_increment comment '角色id',
   role_name            varchar(36) comment '角色名称',
   type                 varchar(4) comment '类型',
   remark               varchar(64) comment '描述',
   create_time          datetime comment '创建时间',
   update_time          datetime comment '更新时间',
   primary key (role_id)
);

/*==============================================================*/
/* Table: sys_role_menu                                         */
/*==============================================================*/
create table sys_role_menu
(
   role_menu_id         int not null auto_increment comment '主键',
   role_id              int comment '角色id',
   menu_id              int comment '菜单id',
   primary key (role_menu_id)
);

/*==============================================================*/
/* Table: sys_user                                              */
/*==============================================================*/
create table sys_user
(
   user_id              int not null auto_increment comment '用户',
   username             varchar(36) comment '登录账号',
   password             varchar(128) comment '登录密码',
   phone                varchar(18) comment '电话',
   email                varchar(36) comment '邮箱',
   sex                  varchar(4) comment '性别',
   is_admin             varchar(2) comment '是否是超级管理员 1：是  0 ：否',
   is_accout_non_expirtd tinyint comment '账号是否过期   1： 未过期 0：已过期',
   is_accout_non_looked tinyint comment '账号是否被锁定   1：未锁定 0：已锁定',
   is_credentials_non_expirtd tinyint comment '密码是否过期   1：未过期 0：已过期',
   is_enabled           tinyint comment '账号是否可用   1：可用   0 ：删除用户',
   nick_name            varchar(36) comment '姓名',
   create_time          datetime comment '创建时间',
   update_time          datetime comment '更新时间',
   primary key (user_id)
);

/*==============================================================*/
/* Table: sys_user_role                                         */
/*==============================================================*/
create table sys_user_role
(
   user_role_id         int not null auto_increment comment '主键',
   user_id              int comment '用户',
   role_id              int comment '角色id',
   primary key (user_role_id)
);

alter table sys_role_menu add constraint FK_Reference_3 foreign key (role_id)
      references sys_role (role_id) on delete restrict on update restrict;

alter table sys_role_menu add constraint FK_Reference_4 foreign key (menu_id)
      references sys_menu (menu_id) on delete restrict on update restrict;

alter table sys_user_role add constraint FK_Reference_1 foreign key (user_id)
      references sys_user (user_id) on delete restrict on update restrict;

alter table sys_user_role add constraint FK_Reference_2 foreign key (role_id)
      references sys_role (role_id) on delete restrict on update restrict;

