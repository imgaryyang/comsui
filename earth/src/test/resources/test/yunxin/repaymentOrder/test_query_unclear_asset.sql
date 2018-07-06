	SET FOREIGN_KEY_CHECKS=0;

	delete from `repayment_order_item`;
	delete from `asset_set`;
	delete from `contract`;
	  
	INSERT INTO `repayment_order_item` (`id`, `order_detail_uuid`, `contract_unique_id`, `contract_no`, `contract_uuid`,`current_period`, `amount`, `detail_alive_status`, `detail_pay_status`,`receivable_in_advance_status`, `mer_id`, `financial_contract_uuid`, `repayment_way`, `repayment_business_uuid`, `repayment_business_no`, `repayment_business_type`, `repayment_plan_time`, `order_uuid`, `order_unique_id`, `remark`)
	VALUES
		(1, 'order_detail_uuid_1', 'contract_unique_id_1', 'contract_no', 'contract_uuid_1', 1, 400.00, 0, 0,1, 'mer_id_1', 'financial_contract_uuid_1', 8, 'asset_uuid_1', 'repayment_plan_no_1', 0, '2017-06-15 10:10:10', 'order_uuid_1', 'oder_unique_id_1', 'remark_1');
	
	INSERT INTO `contract` (`id`, `uuid`,`unique_id`,`begin_date`, `contract_no`,`end_date`,`app_id`, `customer_id`, `house_id`,`actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`,`active_version_no`,`financial_contract_uuid`)
	VALUES 
		(1, 'contract_uuid_1','contract_unique_id_1','2015-10-19', 'contract_no', '2016-10-19', 1, 1, 1, NULL, '2015-10-19 13:34:35', 0.001, 1, 1, 12, 0, 1000, 0.0001,1,'financial_contract_uuid_1');

	INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`, `day_of_month`, `pay_for_go`, `repurchase_principal_algorithm`, `repurchase_interest_algorithm`, `repurchase_penalty_algorithm`, `repurchase_other_charges_algorithm`, `repayment_check_days`, `allow_freewheeling_repayment`, `repurchase_cycle`, `days_of_cycle`, `temporary_repurchases`)
	VALUES
		(1, 1, 3, '2016-04-01 00:00:00', 'G08200', '测试合同', 1, 1, 91, '2016-07-01 00:00:00', 1, 0, 1, 90, 2, '74a9ce4d-cafc-407d-b013-987077541bdc', 'financial_contract_uuid_1', 1, 1, 1, 1, 0, 1, NULL, NULL, NULL, NULL, 1, 0, 0, 0, '(principal+interest)*0.05/100*overdueDay', 123.00, 23.00, NULL, NULL, '2017-03-16 22:56:48', 1, 0, 'outstandingPrincipal+outstandingInterest+outstandingPenaltyInterest', 30, 0, 'outstandingPrincipal', 'outstandingInterest', 'outstandingPenaltyInterest', NULL, -1, 0, NULL, NULL, NULL);
	
	INSERT INTO `asset_set` (`id`, `actual_recycle_date`, `asset_fair_value`, `asset_initial_value`, `asset_interest_value`, `asset_principal_value`, `asset_recycle_date`, `asset_status`, `asset_uuid`, `create_time`, `current_period`, `guarantee_status`, `last_modified_time`, `last_valuation_time`, `on_account_status`, `settlement_status`, `single_loan_contract_no`, `contract_id`,`contract_uuid`,`version_no`,`active_status`,`financial_contract_uuid`,`plan_type`)
	VALUES
		('1', NOW(), '200', '200', '100.00', '100.00', '2016-05-01', '0', 'asset_uuid_1', '2016-05-16 14:26:50', '1', '0', '2016-05-17 14:26:50', NULL, '1', '0', 'repayment_plan_no_1', '1','contract_uuid_1',1,0,'financial_contract_uuid_1','0'),
		('2', NOW(), '300', '300', '100.00', '200.00', '2016-05-01', '0', 'asset_uuid_2', '2016-05-16 14:26:50', '1', '0', '2016-05-17 14:26:50', NULL, '1', '0', 'repayment_plan_no_2', '1','contract_uuid_1',1,0,'financial_contract_uuid_1','0'),
		('3', NOW(), '400', '400', '100.00', '300.00', '2016-05-01', '0', 'asset_uuid_3', '2016-05-16 14:26:50', '1', '0', '2016-05-17 14:26:50', NULL, '1', '0', 'repayment_plan_no_3', '1','contract_uuid_1',1,1,'financial_contract_uuid_1','0'),
		('4', NOW(), '500', '500', '100.00', '400.00', '2016-05-01', '1', 'asset_uuid_4', '2016-05-16 14:26:50', '1', '0', '2016-05-17 14:26:50', NULL, '1', '0', 'repayment_plan_no_4', '1','contract_uuid_1',1,0,'financial_contract_uuid_1','0'),
		('5', NOW(), '600', '600', '100.00', '500.00', '2016-05-01', '0', 'asset_uuid_5', '2016-05-16 14:26:50', '1', '0', '2016-05-17 14:26:50', NULL, '1', '0', 'repayment_plan_no_5', '1','contract_uuid_1',1,0,'financial_contract_uuid_1','0'),
		('6', NOW(), '700', '700', '100.00', '600.00', '2016-05-01', '0', 'asset_uuid_6', '2016-05-16 14:26:50', '1', '0', '2016-05-17 14:26:50', NULL, '1', '0', 'repayment_plan_no_6', '1','contract_uuid_1',1,0,'financial_contract_uuid_1','0');

	SET FOREIGN_KEY_CHECKS=1;