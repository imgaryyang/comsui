SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM system_log;
DELETE FROM principal;
INSERT INTO `principal` (`id`, `authority`, `name`, `password`) VALUES
  ('1', 'ROLE_SUPER_USER', 'root', 'befd2450f81f88ecc5fbcc4c1f97f0b4'),
  ('2', 'ROLE_INSTITUTION', 'dingcheng', 'df5b59f050d316b72b17f73714473f8b'),
  ('3', 'ROLE_APP', 'xiaoyu', 'e10adc3949ba59abbe56e057f20f883e'),
  ('4', 'ROLE_BANK_APP', 'yopark', '086246bffb2de7288946151fc900db59'),
  ('5', 'ROLE_INSTITUTION', 'DCF001', '3cf5eaa7d33e0d023e811c90cd6f2f73'),
  ('6', 'ROLE_INSTITUTION', 'DCF002', 'adb29c63523254a343864e97c84e6360'),
  ('7', 'ROLE_APP', 'laoA', 'e10adc3949ba59abbe56e057f20f883e'),
  ('8', 'ROLE_SUPER_USER', 'guanzhishi', 'befd2450f81f88ecc5fbcc4c1f97f0b4'),
  ('9', 'ROLE_SUPER_USER', 'zhangjianming', '14ebdd77cd348da2ee411e118d125b53'),
  ('10', 'ROLE_SUPER_USER', 'dongjigong', 'cb8d07590edc38e54bb40e3719acc189'),
  ('11', 'ROLE_SUPER_USER', 'lishuzhen', 'a82a92061f9ad7a549a843658107141b'),
  ('12', 'ROLE_SUPER_USER', 'chenjie', '64b2f4c902086b2220afd5b05ad25fb9'),
  ('13', 'ROLE_APP', 'test4Zufangbao', 'e10adc3949ba59abbe56e057f20f883e'),
  ('14', 'ROLE_SUPER_USER', 'zhushiyun', '2ba9a0c7b7bf6b07846c7468c32552d1'),
  ('15', 'ROLE_SUPER_USER', 'lixu', 'a82a92061f9ad7a549a843658107141b'),
  ('16', 'ROLE_SUPER_USER', 'jinyin', '9c74066927e18620a8bc89580f23e8ed'),
  ('17', 'ROLE_APP', 'woju', 'e10adc3949ba59abbe56e057f20f883e'),
  ('18', 'ROLE_SUPER_USER', 'wukai', 'c4de644efae81ff323fa45b50c31296b'),
  ('19', 'ROLE_APP', 'meijia', 'e10adc3949ba59abbe56e057f20f883e'),
  ('20', 'ROLE_BANK_APP', 'YoparkTest', 'befd2450f81f88ecc5fbcc4c1f97f0b4'),
  ('21', 'ROLE_MERCURY_APP', 'XY001', 'e10adc3949ba59abbe56e057f20f883e');

SET FOREIGN_KEY_CHECKS = 1;