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
   menu_id              int not null auto_increment comment '�˵�id',
   parent_id            int comment '�ϼ�id',
   title                varchar(36) comment '�˵�����',
   code                 varchar(36) comment 'Ȩ���ֶ�',
   name                 varchar(36) comment '·������',
   path                 varchar(36) comment '·�ɵ�ַ',
   url                  varchar(128) comment '���·��',
   type                 varchar(2) comment '�˵�����',
   icon                 varchar(36) comment '�˵�ͼ��',
   parent_name          varchar(36) comment '�ϼ��˵�����',
   order_num            int comment '���',
   create_time          datetime comment '����ʱ��',
   update_time          datetime comment '����ʱ��',
   primary key (menu_id)
);

/*==============================================================*/
/* Table: sys_role                                              */
/*==============================================================*/
create table sys_role
(
   role_id              int not null auto_increment comment '��ɫid',
   role_name            varchar(36) comment '��ɫ����',
   type                 varchar(4) comment '����',
   remark               varchar(64) comment '����',
   create_time          datetime comment '����ʱ��',
   update_time          datetime comment '����ʱ��',
   primary key (role_id)
);

/*==============================================================*/
/* Table: sys_role_menu                                         */
/*==============================================================*/
create table sys_role_menu
(
   role_menu_id         int not null auto_increment comment '����',
   role_id              int comment '��ɫid',
   menu_id              int comment '�˵�id',
   primary key (role_menu_id)
);

/*==============================================================*/
/* Table: sys_user                                              */
/*==============================================================*/
create table sys_user
(
   user_id              int not null auto_increment comment '�û�',
   username             varchar(36) comment '��¼�˺�',
   password             varchar(128) comment '��¼����',
   phone                varchar(18) comment '�绰',
   email                varchar(36) comment '����',
   sex                  varchar(4) comment '�Ա�',
   is_admin             varchar(2) comment '�Ƿ��ǳ�������Ա 1����  0 ����',
   is_accout_non_expirtd tinyint comment '�˺��Ƿ����   1�� δ���� 0���ѹ���',
   is_accout_non_looked tinyint comment '�˺��Ƿ�����   1��δ���� 0��������',
   is_credentials_non_expirtd tinyint comment '�����Ƿ����   1��δ���� 0���ѹ���',
   is_enabled           tinyint comment '�˺��Ƿ����   1������   0 ��ɾ���û�',
   nick_name            varchar(36) comment '����',
   create_time          datetime comment '����ʱ��',
   update_time          datetime comment '����ʱ��',
   primary key (user_id)
);

/*==============================================================*/
/* Table: sys_user_role                                         */
/*==============================================================*/
create table sys_user_role
(
   user_role_id         int not null auto_increment comment '����',
   user_id              int comment '�û�',
   role_id              int comment '��ɫid',
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

