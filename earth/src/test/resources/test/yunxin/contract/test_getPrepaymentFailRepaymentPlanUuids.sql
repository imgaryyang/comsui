DELETE FROM `contract`;
DELETE FROM `asset_set`;
DELETE FROM `app`;
DELETE FROM `customer`;
DELETE FROM `house`;
DELETE FROM `financial_contract`;

INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, `interest_rate_cycle`)
VALUES
	(159, 'a0b006e7-5fa8-11e6-b2c2-00163e002839', NULL, '2016-04-18', '2016-78-DK(ZQ2016001000015)', NULL, 1, 0.00, 1, 163, 324, NULL, '2016-06-03 19:28:03', 0.1560000000, 0, 0, 1, 2, 1800.00, 0.0005000000, 1, NULL, 2, '2d380fe1-7157-490d-9474-12c5a9901e29', 0);

	
	
INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`)
VALUES
	(438, 1, 0, 1927.80, 0.00, 1800.00, 1800.00, '2016-05-18', NULL, 0.00, 0, 0, 0, '2016-10-11 03:00:37', '2573d456-b326-43c0-abdf-fc98c2cb3582', '2016-06-03 19:28:03', '2016-10-11 03:00:37', NULL, 'ZC2735BB7DF49DF851', 159, NULL, 1, 1, NULL, 1, 2, 0, 'empty_deduct_uuid', NULL, '2d380fe1-7157-490d-9474-12c5a9901e29');

	
INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`)
VALUES
	(1, 'nongfenqi', '', 1, '', '测试分期', 2, NULL);
	

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`)
VALUES
	(163, NULL, NULL, '测试用户00115', 'C75652', 1, '8b72688f-4906-42d0-9418-f98d09d99e22', 0);

	
	
INSERT INTO `house` (`id`, `address`, `app_id`)
VALUES
	(139, NULL, 1);
	
	
INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`)
VALUES
	(5, 1, 3, '2016-04-01 00:00:00', 'nfqtest001', '测试合同', 1, 1, 91, '2016-07-01 00:00:00', 5, 0, 5, 90, 2, '74a9ce4d-cafc-407d-b013-987077541bdc', '2d380fe1-7157-490d-9474-12c5a9901e29', 1, 1, 1, 1, 0, 0, NULL, NULL, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL);
	
	
	
	
	
	
	
	
	
	
	
	
	

	