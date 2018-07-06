
DELETE FROM  `system_menu`;
DELETE FROM  `system_button`;
DELETE FROM  `link_privilege_button`;
DELETE FROM   `system_privilege`;

-- 插入菜单
INSERT INTO `system_menu` (`id`, `mkey`, `name`, `url`, `description`, `seq_no`, `parent_id`, `system_menu_level`) VALUES (1, 'menu-data', '资产管理', 'v#/data/contracts', '资产管理', 1, NULL, 0);
INSERT INTO `system_menu` (`id`, `mkey`, `name`, `url`, `description`, `seq_no`, `parent_id`, `system_menu_level`) VALUES (8, 'submenu-assets-contract', '贷款合同', 'v#/data/contracts', NULL, 1, 1, 1);
INSERT INTO `system_menu` (`id`, `mkey`, `name`, `url`, `description`, `seq_no`, `parent_id`, `system_menu_level`) VALUES (9, 'submenu-assets-package-loan_batch', '资产包导入', 'v#/data/loan-batch', NULL, 2, 1, 1);
-- 插入按钮
INSERT INTO `system_button` (`id`, `bkey`, `name`, `url`, `description`, `parent_mkey`) VALUES (1, 'asserts_contract_query', '贷款合同查询', 'v#/data/contract_query', '', 'submenu-assets-contract');
INSERT INTO `system_button` (`id`, `bkey`, `name`, `url`, `description`, `parent_mkey`) VALUES (2, 'asserts_package_loan', '资产包导入', 'v#/data/asserts_package_loan', '', 'submenu-assets-package-loan_batch');-- 权限配置
INSERT INTO `system_privilege` (`id`, `privilege_strategy`) VALUES (1, 0);
-- 关联权限和按钮
INSERT INTO `link_privilege_button` (`id`, `button_id`, `privilege_id`, `create_time`) VALUES (1, 1, 1, '2017-4-11 17:26:45');
INSERT INTO `link_privilege_button` (`id`, `button_id`, `privilege_id`, `create_time`) VALUES (2, 2, 1, '2017-4-11 17:27:10');



