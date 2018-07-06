SET FOREIGN_KEY_CHECKS=0;

delete from contract;
delete from asset_package;
delete from loan_batch;
delete from asset_set;
delete from principal;
delete from financial_contract; 

INSERT INTO `loan_batch` (`id`, `is_available`, `code`, `create_time`, `financial_contract_id`, `last_modified_time`, `loan_date`) 
VALUES 
('5', '', 'DCF-NFQ-LR903A 20160511 18:09:322', '2016-05-11 18:09:39', '2', NULL, NULL);

INSERT INTO `asset_package` (`id`, `is_available`, `create_time`, `contract_id`, `asset_package_no`, `financial_contract_id`, `loan_batch_id`)
VALUES 
('1285', 0, '2016-05-11 18:09:40','1750', 'asset_package_no', '2', '5');


INSERT INTO `contract` (`id`, `asset_type`, `begin_date`, `contract_no`, `month_fee`, `app_id`, `customer_id`, `house_id`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`) 
VALUES 
('1750', '1', '2016-04-27', 'ZQ2016042422406', '15976.15', '1', '1514', '1440', '2016-03-28 11:20:28', '0.1500000000', '0', '1', '20', '0', '100000.00', '0.0005000000');

INSERT INTO `principal` (`id`, `authority`, `name`, `password`, `start_date`, `thru_date`) VALUES 
('1', 'ROLE_SUPER_USER', 'zhushiyun', 'e10adc3949ba59abbe56e057f20f883e', NULL, NULL);

INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`)
VALUES
	(38, 0, 1, '2016-09-01 00:00:00', 'G31700', '拍拍贷测试', 3, 1, 60, '2017-08-31 00:00:00', 102, 0, 1, 2, 1, '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', 'd2812bc5-5057-4a91-b3fd-9019506f0499', 0, 0, 0, 0, 1, 0, NULL, NULL, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL);


SET FOREIGN_KEY_CHECKS=1;