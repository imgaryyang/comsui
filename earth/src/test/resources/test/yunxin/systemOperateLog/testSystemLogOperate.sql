SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM `system_operate_log`;
DELETE FROM `principal`;
DELETE FROM `company`;
DELETE FROM `user_group`;
DELETE FROM `t_user`;

INSERT INTO system_operate_log
(id, user_id, ip, object_uuid, log_function_type, log_operate_type, key_content, update_content_detail, record_content_detail, record_content, occur_time)
VALUES
(4937, 39, '192.168.0.200', 'a41f96c0-fea7-45fe-be52-ace95ee1a253', 0, 0, NULL, NULL, NULL, '用户fanteng登陆成功', '2017-11-15 23:58:59'),
(4938, 3, '192.168.0.200', '84922dc6-ff12-432c-b090-005099769f70', 0, 0, NULL, NULL, NULL, '用户zhushiyun登陆成功', '2017-11-16 00:12:24'),
(4939, 22, '192.168.0.200', '5be15718-2386-43ba-9652-ad0d00a564ad', 0, 0, NULL, NULL, NULL, '用户lijie登陆成功', '2017-11-16 00:26:49'),
(4940, 3, '192.168.0.200', 'a93b598f-bc82-44a2-89bf-7a877f0e53e6', 0, 0, NULL, NULL, NULL, '用户zhushiyun登陆成功', '2017-11-16 00:26:54'),
(4941, 39, '192.168.0.200', '417fbcaf-608c-4386-88db-557903863d50', 0, 0, NULL, NULL, NULL, '用户fanteng登陆成功', '2017-11-16 00:35:13'),
(4942, 39, '192.168.0.200', '6c23b4be-417a-4dec-ba6b-bcabb5420cbe', 0, 0, NULL, NULL, NULL, '用户fanteng登陆成功', '2017-11-16 00:40:27'),
(4943, 39, '192.168.0.200', '73e9a399-0144-43d5-8477-fb819ea1853b', 0, 0, NULL, NULL, NULL, '用户fanteng登陆成功', '2017-11-16 00:42:57'),
(4944, 3, '192.168.0.200', 'cab96fa8-76be-403f-ba5c-ee3902b12776', 0, 0, NULL, NULL, NULL, '用户zhushiyun登陆成功', '2017-11-16 00:47:37'),
(4945, 39, '192.168.0.200', '34b016a8-db1d-42de-9d06-16b2083eb41a', 0, 0, NULL, NULL, NULL, '用户fanteng登陆成功', '2017-11-16 00:51:55'),
(4946, 22, '192.168.0.200', 'cdee2e02-654f-4b1e-81e7-993e0558a2d0', 0, 0, NULL, NULL, NULL, '用户lijie登陆成功', '2017-11-16 00:52:03'),
(5000, 4, '192.168.0.200', '035ec7df-f500-4bf0-990d-0235fba6e702', 0, 0, NULL, NULL, NULL, '用户zhenghangbo登陆成功', '2017-11-17 12:31:27');

INSERT INTO principal (id, authority, name, password, start_date, thru_date, t_user_id, created_time, creator_id, modify_password_time)
VALUES
(1, 'ROLE_AUTHORITY_DISTRIBUTOR,ROLE_SUPER_USER', 'zhanghongbing', 'd539aade5213cede6bf0aa878446fe19', NULL, '2017-09-18 14:53:04', 2, NULL, NULL, 2),
(2, 'ROLE_SUPER_USER,ROLE_AUTHORITY_DISTRIBUTOR', 'louguanyang', 'd3786ec2413a8cd9413bfcb24be95a73', NULL, NULL, 27, NULL, NULL, 1),
(3, 'ROLE_AUTHORITY_DISTRIBUTOR,ROLE_SUPER_USER', 'zhushiyun', 'c33367701511b4f6020ec61ded352059', NULL, NULL, 67, NULL, NULL, 1),
(4, 'ROLE_AUTHORITY_DISTRIBUTOR,ROLE_SUPER_USER', 'zhenghangbo', 'e10adc3949ba59abbe56e057f20f883e', '2017-07-31 13:45:04', NULL, 68, NULL, NULL, 1),
(5, 'ROLE_SUPER_USER', 'guanzhishi', 'befd2450f81f88ecc5fbcc4c1f97f0b4', NULL, '2017-02-22 12:48:01', NULL, NULL, NULL, 1),
(6, 'ROLE_AUTHORITY_DISTRIBUTOR,ROLE_SUPER_USER', 'lixu', '1fb2cf7d2f99b13ad8d850cd32544c4c', NULL, NULL, 8, NULL, NULL, 1),
(7, 'ROLE_AUTHORITY_DISTRIBUTOR,ROLE_SUPER_USER', 'chenhaonan', 'a8af2590df208890fef7da70f309f2ff', NULL, NULL, 16, NULL, NULL, 1),
(8, 'ROLE_AUTHORITY_DISTRIBUTOR,ROLE_SUPER_USER', 'ouyangdi', '47dd8a9ec829495a27b01ad7f3e5b000', NULL, NULL, NULL, NULL, NULL, 1),
(9, 'ROLE_AUTHORITY_DISTRIBUTOR,ROLE_SUPER_USER', 'libo', '376c43878878ac04e05946ec1dd7a55f', NULL, NULL, NULL, NULL, NULL, 1),
(11, 'ROLE_SUPER_USER', 'yunxintest001', 'c33367701511b4f6020ec61ded352059', '2016-05-30 16:21:32', NULL, NULL, NULL, NULL, 2),
(12, 'ROLE_SUPER_USER', 'yunxintest002', '46f94c8de14fb36680850768ff1b7f2a', '2016-05-30 16:21:40', NULL, NULL, NULL, NULL, 2);

INSERT INTO company (id, address, full_name, short_name, uuid, legal_person, business_licence) VALUES
(1, '上海', '云南国际信托有限公司', '云南信托', '1f304176-7e9c-414b-b2b8-46f15481e994', NULL, NULL),
(2, '南京', '南京农纷期电子商务有限公司', '农分期', '3a93dbfd-7063-11e6-8a0b-0050568ad186', NULL, NULL),
(3, '北京', '北京智融时代信息技术有限公司 ', '用钱宝', '1fdd8d77-7065-11e6-8a0b-0050568ad186', NULL, NULL),
(4, '上海', '上海拍拍贷金融信息服务有限公司', '拍拍贷', '1d3cb97f-e100-41d4-9878-8a4ab7f59cad', NULL, NULL),
(5, '广东', '佛山市碧桂园投资服务咨询有限公司', '碧桂园', '99b9b5a3-9355-11e6-9aaf-00163e002839', NULL, NULL),
(6, '北京', '百融（北京）金融信息服务股份有限公司', '百融', 'a47c5016-9b5e-11e6-aaf6-005056960acf', NULL, NULL),
(7, '杭州', '杭州随地付网络技术有限公司', '随地付', 'e4912ab7-b76f-11e6-83ae-005056960acf', NULL, NULL),
(8, '杭州', '杭州康付信息科技有限公司', '钱大夫', '5340b9bf-fbd8-11e6-8d2c-00505696dbf2', NULL, NULL),
(9, '黑龙江', '黑龙江农之家科技有限公司', '农之家', '55fcb30a-ff0f-11e6-8d2c-00505696dbf2', NULL, NULL);

INSERT INTO user_group (id, group_name, create_time) VALUES
(1, '测试', '2017-02-22 10:48:49'),
(2, '运营', '2017-02-28 15:37:02'),
(3, '产品', '2017-03-07 11:17:36'),
(4, '研发', '2017-04-12 16:33:43');

INSERT INTO t_user (id, uuid, name, email, phone, company_id, dept_name, position_name, remark, financial_contract_ids, user_group_id, bind_all) VALUES
(1, '3ee32fd7-c47a-4a61-9b4e-4b9bf4e6c348', '测试', '', '', NULL, '', '', '', NULL, NULL, 0),
(2, '787c8a18-2d4e-4a49-ba38-54736c328244', '张红兵', 'zhanghongbing@hzsuidifu.com', '', 3, '研发部', '', '', '[101,100,95,94,92,91,90,89,88,87,86,85,84,83,82,81,80,79,78,77,76,75,74,73,72,71,70,69,68,67,66,65,64,63,62,61,60,59,58,57,56,53,48,47,46,45,44,42,39,38,37,36,33,31,23,22,21,15,5]', NULL, 0),
(3, '35e61df1-6dff-4262-970a-8861b2d8c4d7', '测试', '', '', NULL, '', '', '', NULL, NULL, 0),
(4, '9da0beab-f24c-4502-b0eb-84ba829eec62', '杜丽', '1092394854@qq.com', '', 1, '云南国际信托有限公司网络金融信息部', '', '', NULL, NULL, 0),
(5, 'a0c1ef62-0641-4b2f-8fe8-26b3de05fd63', '李杰', '', '', 3, '', '', '', '[95,94,92,91,90,89,88,87,86,85,84,83,82,81,80,79,78,77,76,75,74,73,72,71,70,69,68,67,66,65,64,63,62,61,60,59,58,57,56,53,48,47,46,45,44,42,39,38,37,36,33,31,23,22,21,15,5,93]', NULL, 0),
(6, '7fd089c4-7cf3-439f-83ab-2c13c5d5f853', '江旭', '', '', 3, '', '', '', '[89,88,90]', NULL, 0),
(7, '2fb64de2-3ebe-44d4-b027-7b39c1e5a454', '李旭', '', '', 3, '', '', '', '[95,94,92,91,90,89,88,87,86,85,84,83,82,81,80,79,78,77,76,75,74,73,72,71,70,69,68,67,66,65,64,63,62,61,60,59,58,57,56,53,48,47,46,45,44,42,39,38,37,36,33,31,23,22,21,15,5]', NULL, 0),
(8, '4caae44c-fd20-45af-ab81-8d42a37461d9', '李旭', '', '', 3, '', '', '', '[95,94,92,91,90,89,88,87,86,85,84,83,82,81,80,79,78,77,76,75,74,73,72,71,70,69,68,67,66,65,64,63,62,61,60,59,58,57,56,53,48,47,46,45,44,42,39,38,37,36,33,31,23,22,21,15,5]', NULL, 0),
(9, 'a280e46b-2554-43f1-af3f-f582930ca4df', '朱凯', '', '', 3, '', '', '', NULL, NULL, 0),
(67, 'deee597e-ae12-4e48-9702-be76658a458b', '朱师云', '', '', 3, NULL, NULL, '', '[97,96,95,94,92,91,90,89,88,87,86,85,84,83,82,81,80,79,78,77,76,75,74,73,72,71,70,69,68,67,66,65,64,63,62,61,60,59,58,57,56,53,48,47,46,45,44,42,39,38,37,36,33,31,23,22,21,15,5]', 1, 0);


SET FOREIGN_KEY_CHECKS = 1;