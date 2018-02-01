-- ----------------------------
-- Records of login
-- ----------------------------
DELETE FROM `login`;
INSERT INTO `login` VALUES (NULL, 'admin', 'admin', '5d69188f019d0f0a9f83fb4b98411739')

DELETE FROM `access_key_info`;
INSERT INTO `access_key_info` VALUES (1,'123456', '654321', null, '2017-11-14 10:43:32','2018-11-14 10:43:32');
COMMIT;