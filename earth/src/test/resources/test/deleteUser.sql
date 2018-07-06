SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM principal;
DELETE FROM company;

INSERT INTO `principal` (`id`, `authority`, `name`, `password`, `start_date`, `thru_date`) VALUES
  ('1', 'ROLE_SUPER_USER', 'root', 'a82a92061f9ad7a549a843658107141b', NULL, NULL),
  ('2', 'ROLE_INSTITUTION', 'dingcheng', 'df5b59f050d316b72b17f73714473f8b', NULL, NULL),
  ('3', 'ROLE_APP', 'xiaoyu', 'e10adc3949ba59abbe56e057f20f883e', NULL, NULL),
  ('4', 'ROLE_BANK_APP', 'yopark', '086246bffb2de7288946151fc900db59', NULL, NULL),
  ('5', 'ROLE_INSTITUTION', 'DCF001', '3cf5eaa7d33e0d023e811c90cd6f2f73', NULL, NULL),
  ('6', 'ROLE_INSTITUTION', 'DCF002', 'adb29c63523254a343864e97c84e6360', NULL, NULL),
  ('7', 'ROLE_APP', 'laoA', 'e10adc3949ba59abbe56e057f20f883e', NULL, NULL),
  ('8', 'ROLE_SUPER_USER', 'guanzhishi', 'befd2450f81f88ecc5fbcc4c1f97f0b4', NULL, NULL),
  ('9', 'ROLE_SUPER_USER', 'zhangjianming', '14ebdd77cd348da2ee411e118d125b53', NULL, NULL),
  ('10', 'ROLE_SUPER_USER', 'dongjigong', 'cb8d07590edc38e54bb40e3719acc189', NULL, NULL),
  ('11', 'ROLE_SUPER_USER', 'lishuzhen', 'a82a92061f9ad7a549a843658107141b', NULL, NULL),
  ('12', 'ROLE_SUPER_USER', 'chenjie', '64b2f4c902086b2220afd5b05ad25fb9', NULL, NULL),
  ('13', 'ROLE_APP', 'test4Zufangbao', 'e10adc3949ba59abbe56e057f20f883e', NULL, NULL);


INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`) VALUES
  (2, '上海陆家嘴', '鼎程（上海）商业保理有限公司', '鼎程', uuid()),
  (3, '杭州万塘路8号', '杭州随地付网络技术有限公司', '租房宝', uuid()),
  (4, '上海', '上海优帕克投资管理有限公司', '优帕克', uuid()),
  (5, '上海', '小寓科技', '小寓', uuid()),
  (6, '上海', '杭州驻客网络技术有限公司', '驻客', uuid()),
  (7, '上海', '上海元轼信息咨询有限公司', '老A', uuid()),
  (8, '上海', '柯罗芭', '柯罗芭', uuid()),
  (9, '杭州', '租房宝测试', '租房宝测试', uuid()),
  (10, NULL, '小寓测试帐号', '小寓测试帐号', uuid()),
  (11, NULL, '杭州蜗居', '杭州蜗居', uuid()),
  (12, NULL, '美家公寓', '美家公寓', uuid()),
  (13, NULL, '安心公寓', '安心公寓', uuid()),
  (14, NULL, '源涞国际', '源涞国际', uuid()),
  (15, NULL, '汉维仓储', '汉维仓储', uuid());


SET FOREIGN_KEY_CHECKS = 1;