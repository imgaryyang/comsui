SET FOREIGN_KEY_CHECKS = 0;


DELETE FROM `contract`;
DELETE FROM `asset_set`;
DELETE FROM `asset_valuation_detail`;
DELETE FROM `rent_order`;
DELETE FROM `financial_contract`;
DELETE FROM `payment_channel`;
DELETE FROM `asset_package`;
delete from `customer`;
DELETE FROM `principal`;
delete from special_account;
delete from virtual_account;
delete from special_account_flow;

INSERT INTO `principal` (`id`, `authority`, `name`, `password`, `start_date`, `thru_date`) VALUES 
('1', 'ROLE_SUPER_USER', 'zhushiyun', 'e10adc3949ba59abbe56e057f20f883e', NULL, NULL);


INSERT INTO `contract` (`id`, `begin_date`, `contract_no`, `end_date`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`,`active_version_no`)
VALUES
	(1, '2016-03-01', 'DKHD-001', '2017-03-15', 10000.00, 1, 1, 1, NULL, '2016-03-15 13:34:35', 0.0010000000, 1, 1, 10, 0, 100000, 0.0005000000,1);

INSERT INTO `asset_package` (`id`, `is_available`,  `contract_id`, `asset_package_no`, `financial_contract_id`) VALUES 
('1', 1, 1, 'no1', '1');

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`) VALUES 
('1', NULL, NULL, '测试用户1', 'C74211', '2', 'customer_uuid_1');


INSERT INTO `financial_contract` (`id`, `adva_matuterm`, `contract_no`, `app_id`, `company_id`,   `adva_repo_term`, `payment_channel_id`,`adva_repayment_term`,`financial_contract_uuid`) VALUES 
('1', 3, 'YX_AMT_001', 1, 1, 30,1,1,'financial_contract_uuid_1'),
('2', 3, 'YX_AMT_002', 2, 1, 30,1,1,'financial_contract_uuid_2');

INSERT INTO `payment_channel` (`id`, `channel_name`, `user_name`, `user_password`, `merchant_id`, `cer_file_path`, `pfx_file_path`, `pfx_file_key`, `payment_channel_type`, `api_url`, `from_date`, `thru_date`) VALUES 
(1, 'channel_name', 'user_name', 'user_password', 'merchant_id', 'cer_file_path', 'pfx_file_path', 'pfx_file_key', 0, 'api_url', NULL, NULL);


INSERT INTO `special_account` (`id`, `uuid`, `balance`, `basic_account_type`, `account_type_code`, `account_name`, `level`, `parent_account_uuid`, `fst_level_contract_uuid`, `snd_level_contract_uuid`, `trd_level_contract_uuid`, `create_time`, `last_update_time`, `version`) VALUES 
('1', 'uuid_1', '100.00', '3', NULL, '客户账户', '1', NULL, 'financial_contract_uuid_1', NULL, NULL, '2017-12-22 17:37:38', '2017-12-22 17:37:42', 'ewrfegtrg'),
('2', 'uuid_2', '200.00', '3', NULL, '客户账户', '1', NULL, 'financial_contract_uuid_2', NULL, NULL, '2017-12-22 17:37:38', '2017-12-22 17:37:42', 'ewrfegtrgdsgdrg');


INSERT INTO `special_account_flow` (`id`, `uuid`, `account_flow_no`, `basic_account_type`, `account_side`, `create_time`, `financial_contract_uuid`, `host_account_uuid`, `host_account_type_code`, `counter_account_uuid`, `counter_account_type_code`, `account_trans_type`, `transaction_amount`, `balance`, `batch_uuid`, `cash_flow_uuid`, `cash_flow_identity`, `remark`) VALUES 
('1', 'flow_uuid_1', 'account_flow_no_1', '1', '1', '2017-12-25 15:33:13', 'financial_contract_uuid_1', 'uuid_1', NULL, 'uuid_1', NULL, '1', '200.00', '50.00', NULL, NULL, 'bankSequenceNo1', 'remark_1'),
('2', 'flow_uuid_2', 'account_flow_no_2', '1', '1', '2017-12-25 15:34:13', 'financial_contract_uuid_1', 'uuid_1', NULL, 'uuid_1', NULL, '2', '500.00', '70.00', NULL, NULL, 'bankSequenceNo2', 'remark_2');






INSERT INTO `virtual_account` (`id`, `total_balance`, `virtual_account_uuid`, `parent_account_uuid`, `virtual_account_alias`, `virtual_account_no`, `version`, `owner_uuid`, `owner_name`, `fst_level_contract_uuid`, `snd_level_contract_uuid`, `trd_level_contract_uuid`, `create_time`, `last_update_time`, `last_modified_time`, `customer_type`, `virtual_account_status`) VALUES 
('1', '300.00', '58886ef3-3e4e-4ea7-b786-cb35e952ac7f', NULL, '高渐离啊', 'VACC128941594058629120', '35094a39-8665-4424-8bce-f51a0039033f', 'f7a246e0-a610-42e2-9112-0c3471f95a91', '高渐离啊', 'financial_contract_uuid_1', '5db9b0c3-118c-4247-a01c-08b599e1fdf1', '', '2017-11-17 16:48:57', '2017-11-17 17:36:08', NULL, '0', '0'),
('2', '200.00', '58886ef3-3e4e-4ea7-b786-cb35e952ac7f33', NULL, '高渐离啊', 'VACC128941594058629120', '35094a39-8665-4424-8bce-f51a0039033f', 'f7a246e0-a610-42e2-9112-0c3471f95a91', '高渐离啊', 'financial_contract_uuid_1', '5db9b0c3-118c-4247-a01c-08b599e1fdf1', '', '2017-11-17 16:48:57', '2017-11-17 17:36:08', NULL, '0', '0'),
('3', '700.00', '9435c651-b20b-49bb-a465-99938dd35539', NULL, '高渐离啊', 'VACC130014714827579392', 'ea86cc5e-1b65-4989-84bb-874a5671beec', 'f36af1c6-18a0-442b-bc09-b338d1cf7872', '高渐离啊', 'financial_contract_uuid_2', 'bd0cc9d1-1ff6-4ca5-90d8-b35fca6f8d8f', '', '2017-11-20 15:53:09', '2017-11-20 15:53:09', NULL, '0', '0');











-- assetSet1未结转，2已结转
INSERT INTO `asset_set` (`id`, `asset_fair_value`,`asset_initial_value`, `asset_recycle_date`, `asset_status`, `asset_uuid`, `create_time`, `last_modified_time`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`,`version_no`)
VALUES
	(1, 10010.00,10000.00,'2016-05-16', 0, 'asset_uuid_1', '2016-05-16 13:34:35', '2016-05-16 13:34:35', 'DKHD-001-01', 1, NULL,1),
	(2, 10000.00,10000.00,'2016-05-17', 1, 'asset_uuid_2', '2016-05-17 13:34:35', '2016-05-17 13:34:35', 'DKHD-002-01', 2, NULL,1);







SET FOREIGN_KEY_CHECKS = 1;