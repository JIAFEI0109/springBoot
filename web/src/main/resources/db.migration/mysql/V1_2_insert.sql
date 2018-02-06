-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES ('1', 'admin', '超级管理员', '5d69188f019d0f0a9f83fb4b98411739', '1', '1', '0', '2018-02-06 14:52:37', null);

DELETE FROM `access_key_info`;
INSERT INTO `access_key_info` VALUES (1,'123456', '654321', null, '2017-11-14 10:43:32','2018-11-14 10:43:32');
COMMIT;