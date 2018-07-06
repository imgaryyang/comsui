delete from `app`;
delete from `company`;
delete from `customer`;
delete from `asset_package`;
delete from `financial_contract`;
delete from `contract`;
delete from `asset_set`;
delete from `ledger_book`;


INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`) 
VALUES 
	('1', NULL, '12345678900', 'test_customer', 'test_customer_no', '1', 'test_customer_uuid', '0');

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`) 
VALUES 
	('1', '1', 'test_app_secret', b'0', NULL, 'test_app', '1', 'newyork');

INSERT INTO `asset_package` (`id`, `is_available`, `create_time`, `contract_id`, `asset_package_no`, `financial_contract_id`, `loan_batch_id`) 
VALUES 
	('1', b'0', '2016-12-01 19:22:04', '1', 'test_asset_package_no', '1', NULL);

INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`) 
VALUES 
	('1', '0', '0', '2016-12-01 14:54:24', 'test_contract_no', 'test_contract', '1', '1', '30', '2016-12-31 15:11:34', '1', '0', '1', '29', '0', 'test_ledger_book_no', 'test_financial_contract_uuid', '1', '1', '1', '1', '1', '1', '10000.00', '100000.00', '0', 'test_app_account_uuids', '0', '0', '0', '0', NULL, '100.00', '10.00', '1.00', '2016-11-30 15:17:27', '2016-12-01 15:17:35');

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`) 
VALUES 
	('1', 'newyork', 'test_company', 'tc', 'test_company_uuid');

INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, `interest_rate_cycle`) 
VALUES 
	('1', 'test_contact_uuid', 'test_unique_id', '2016-12-01', 'test_contract_no', '2016-12-31', '0', NULL, '1', '1', '1', NULL, '2016-12-01 15:35:06', NULL, '10', '5', '2', '0', '10000.00', '0.0100000000', NULL, NULL, '2', 'test_financial_contract_uuid', '0');

INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`) 
VALUES 
	('1', '0', '0', '5000.00', '4500.00', '500.00', '4500.00', '2016-12-03', NULL, NULL, '0', '0', '0', NULL, 'test_asset_uuid_1', '2016-12-02 15:45:28', '2016-12-02 15:45:33', NULL, 'test_asset_no_1', '1', NULL, '1', '0', NULL, NULL, '0', '0', 'empty_deduct_uuid', NULL, 'test_financial_contract_uuid', NULL, NULL, NULL, NULL);
INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`) 
VALUES 
	('2', '0', '0', '5000.00', '4500.00', '500.00', '4500.00', '2016-12-28', NULL, NULL, '0', '0', '0', NULL, 'test_asset_uuid_2', '2016-12-02 15:47:49', '2016-12-02 15:47:55', NULL, 'test_asset_no_2', '1', NULL, '2', '0', NULL, NULL, '0', '0', 'empty_deduct_uuid', NULL, 'test_financial_contract_uuid', NULL, NULL, NULL, NULL);

INSERT INTO `ledger_book` (`id`, `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`) 
VALUES 
	('1', 'test_ledger_book_no', '1', '1', NULL);

