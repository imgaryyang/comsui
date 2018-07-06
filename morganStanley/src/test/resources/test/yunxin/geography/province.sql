SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for province
-- ----------------------------
DROP TABLE IF EXISTS `province`;
CREATE TABLE `province` (
  `id`         BIGINT(20) NOT NULL AUTO_INCREMENT,
  `code`       VARCHAR(255)        DEFAULT NULL,
  `is_deleted` BIT(1)     NOT NULL,
  `name`       VARCHAR(255)        DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 32
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of province
-- ----------------------------
BEGIN;
INSERT INTO `province` VALUES (1, '330000', b'0', '浙江省');
INSERT INTO `province` VALUES (2, '120000', b'0', '天津市');
INSERT INTO `province` VALUES (3, '130000', b'0', '河北省');
INSERT INTO `province` VALUES (4, '140000', b'0', '山西省');
INSERT INTO `province` VALUES (5, '150000', b'0', '内蒙古');
INSERT INTO `province` VALUES (6, '210000', b'0', '辽宁省');
INSERT INTO `province` VALUES (7, '220000', b'0', '吉林省');
INSERT INTO `province` VALUES (8, '230000', b'0', '黑龙江');
INSERT INTO `province` VALUES (9, '310000', b'0', '上海市');
INSERT INTO `province` VALUES (10, '320000', b'0', '江苏省');
INSERT INTO `province` VALUES (11, '110000', b'0', '北京市');
INSERT INTO `province` VALUES (12, '340000', b'0', '安徽省');
INSERT INTO `province` VALUES (13, '350000', b'0', '福建省');
INSERT INTO `province` VALUES (14, '360000', b'0', '江西省');
INSERT INTO `province` VALUES (15, '370000', b'0', '山东省');
INSERT INTO `province` VALUES (16, '410000', b'0', '河南省');
INSERT INTO `province` VALUES (17, '420000', b'0', '湖北省');
INSERT INTO `province` VALUES (18, '430000', b'0', '湖南省');
INSERT INTO `province` VALUES (19, '440000', b'0', '广东省');
INSERT INTO `province` VALUES (20, '450000', b'0', '广西');
INSERT INTO `province` VALUES (21, '460000', b'0', '海南省');
INSERT INTO `province` VALUES (22, '500000', b'0', '重庆市');
INSERT INTO `province` VALUES (23, '510000', b'0', '四川省');
INSERT INTO `province` VALUES (24, '520000', b'0', '贵州省');
INSERT INTO `province` VALUES (25, '530000', b'0', '云南省');
INSERT INTO `province` VALUES (26, '540000', b'0', '西藏');
INSERT INTO `province` VALUES (27, '610000', b'0', '陕西省');
INSERT INTO `province` VALUES (28, '620000', b'0', '甘肃省');
INSERT INTO `province` VALUES (29, '630000', b'0', '青海省');
INSERT INTO `province` VALUES (30, '640000', b'0', '宁夏');
INSERT INTO `province` VALUES (31, '650000', b'0', '新疆');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;