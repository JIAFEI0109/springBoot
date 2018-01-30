SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `user_info`
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `u_id` bigint(10) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `login_name` varchar(255) NOT NULL,
  `pwd` varchar(255) NOT NULL,
  PRIMARY KEY (`u_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `sys_resources`
-- ----------------------------
DROP TABLE IF EXISTS `sys_resources`;
CREATE TABLE `sys_resources` (
  `resource_id` bigint(10) NOT NULL AUTO_INCREMENT  COMMENT '资源ID',
  `resource_type` bigint(10)  NOT NULL COMMENT '资源类型1、web端资源2、客户端资源',
  `resource_name` varchar(255) NOT NULL COMMENT '资源名称',
  `resource_desc` varchar(2000) DEFAULT NULL COMMENT '资源描述',
  `client_type` bigint(10) DEFAULT NULL COMMENT '客户端资源分类',
  `resource_path` varchar(255) NOT NULL  COMMENT '资源路径',
  `interface_path` varchar(255) NOT NULL COMMENT '系统接口路径',
  `disable_flag` bigint(10) DEFAULT 1 COMMENT '0禁用、1.使用',
  `sys_flag` bigint(10) DEFAULT 0 COMMENT '是否系统默认资源0不是1是',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_mod` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`resource_id`),
  UNIQUE KEY `resource_id` (`resource_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统资源表';

-- ----------------------------
-- Table structure for `sys_roles_resources`
-- ----------------------------
DROP TABLE IF EXISTS `sys_roles_resources`;
CREATE TABLE `sys_roles_resources` (
  `rec_id` bigint(10) NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
  `resource_id` bigint(10)  NOT NULL COMMENT '资源ID',
  `role_id` bigint(10) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`rec_id`),
  UNIQUE KEY `resource_id_role_id` (`resource_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色资源关系表';

-- ----------------------------
-- Table structure for `sys_roles`
-- ----------------------------
DROP TABLE IF EXISTS `sys_roles`;
CREATE TABLE `sys_roles` (
  `role_id` bigint(10) NOT NULL AUTO_INCREMENT  COMMENT '角色ID',
  `role_name` varchar(255) NOT NULL COMMENT '角色名称',
  `role_desc` varchar(2000) DEFAULT NULL COMMENT '角色描述',
  `disable_flag` bigint(10) DEFAULT 1 COMMENT '0禁用、1.使用',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_mod` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';

-- ----------------------------
-- Table structure for `sys_user_roles`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_roles`;
CREATE TABLE `sys_user_roles` (
  `rec_id` bigint(10) NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
  `user_id` bigint(10)  NOT NULL COMMENT '用户ID',
  `role_id` bigint(10) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`rec_id`),
  UNIQUE KEY `user_id_role_id` (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关系表';