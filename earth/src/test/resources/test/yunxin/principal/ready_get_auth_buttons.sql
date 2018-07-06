
DELETE FROM  `system_menu`;
DELETE FROM  `system_button`;
DELETE FROM  `link_privilege_button`;
DELETE FROM   `system_privilege`;
DELETE FROM  `link_role_privilege`;
DELETE FROM  `system_role`;
DELETE FROM  `principal`;
DELETE FROM   `t_user`;



-- 插入角色
INSERT INTO `system_role` (`id`, `role_name`, `role_remark`, `create_time`, `role_state`) VALUES (1, 'ROLE_SUPER_USER', '管理员', '2016-12-30 02:15:47', 0);
INSERT INTO `system_role` (`id`, `role_name`, `role_remark`, `create_time`, `role_state`) VALUES (2, 'ROLE_TRUST_OBSERVER', '用户', '2017-1-6 04:04:12', 0);
-- 插入账户
INSERT INTO `principal` (`id`, `authority`, `name`, `password`, `start_date`, `thru_date`, `t_user_id`, `created_time`, `creator_id`, `modify_password_time`) VALUES ('1', 'ROLE_SUPER_USER', 'zhanghongbing', '376c43878878ac04e05946ec1dd7a55f', NULL, NULL, '2', NULL, NULL, '1');
-- 插入用户
INSERT INTO `t_user` (`id`, `uuid`, `name`, `email`, `phone`, `company_id`, `dept_name`, `position_name`, `remark`, `financial_contract_ids`, `user_group_id`) VALUES ('2', '787c8a18-2d4e-4a49-ba38-54736c328244', '张红兵', 'zhanghongbing@hzsuidifu.com', '', '3', '研发部', '', '', NULL, NULL);

-- 插入菜单
INSERT INTO `system_menu` (`id`, `mkey`, `name`, `url`, `description`, `seq_no`, `parent_id`, `system_menu_level`) VALUES (1, 'menu-data', '资产管理', 'v#/data/contracts', '资产管理', 1, NULL, 0);
INSERT INTO `system_menu` (`id`, `mkey`, `name`, `url`, `description`, `seq_no`, `parent_id`, `system_menu_level`) VALUES (8, 'submenu-assets-contract', '贷款合同', 'v#/data/contracts', NULL, 1, 1, 1);
INSERT INTO `system_menu` (`id`, `mkey`, `name`, `url`, `description`, `seq_no`, `parent_id`, `system_menu_level`) VALUES (9, 'submenu-assets-package-loan_batch', '资产包导入', 'v#/data/loan-batch', NULL, 2, 1, 1);
-- 插入按钮
-- 插入按钮
INSERT INTO `system_button` (`id`, `bkey`, `name`, `url`, `description`, `parent_mkey`) VALUES (1, 'asserts_contract_query', '贷款合同查询', 'v#/data/contract_query', '', 'submenu-assets-contract');
INSERT INTO `system_button` (`id`, `bkey`, `name`, `url`, `description`, `parent_mkey`) VALUES (2, 'asserts_package_loan', '资产包导入', 'v#/data/asserts_package_loan', '', 'submenu-assets-package-loan_batch');-- 权限配置
INSERT INTO `system_privilege` (`id`, `privilege_strategy`) VALUES (1, 0);
-- 关联权限和按钮
INSERT INTO `link_privilege_button` (`id`, `button_id`, `privilege_id`, `create_time`) VALUES (1, 1, 1, '2017-4-11 17:26:45');
INSERT INTO `link_privilege_button` (`id`, `button_id`, `privilege_id`, `create_time`) VALUES (2, 2, 1, '2017-4-11 17:27:10');

-- 关联角色和权限
INSERT INTO `link_role_privilege` (`id`, `role_id`, `link_privilege_button_id`, `create_time`) VALUES (1, 2, 1, '2017-4-11 17:30:11');
INSERT INTO `link_role_privilege` (`id`, `role_id`, `link_privilege_button_id`, `create_time`) VALUES (2, 1, 1, '2017-4-11 17:30:23');
INSERT INTO `link_role_privilege` (`id`, `role_id`, `link_privilege_button_id`, `create_time`) VALUES (3, 1, 2, '2017-4-11 17:30:40');



