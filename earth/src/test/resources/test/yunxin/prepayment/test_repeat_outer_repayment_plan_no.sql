set foreign_key_checks=0;

delete from `house`;
delete from `company`;
delete from `app`;
delete from `customer`;
delete from `financial_contract`;
delete from `contract`;
delete from `asset_set`;
delete from `asset_set_extra_charge`;
delete from `t_prepayment_application`;
delete from `ledger_book`;

INSERT INTO `house` (`id`, `address`, `app_id`)
VALUES
	('1', 'cesd', '1');

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`)
VALUES
	('1', 'newyork', 'test_company', 'tc', 'test_company_uuid');

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`)
VALUES
	('1', 'zfb', NULL, b'1', NULL, 'zufangbao', '1', NULL);

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`)
VALUES
	('1', 'test_account', '12345678900', 'test_customer', 'test', '1', 'test_customer_uuid', '0');

INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`)
VALUES
	('1', NULL, '0', '2016-09-01 15:31:58', 'G32000', 'test_financial_contract', '1', '1', '0', '2017-11-01 15:32:56', '1', '0', '0', '0', '0', 'test_ledger_book_no', 'test_financial_contract_uuid', '0', '0', '0', '0', '0', '0', NULL, NULL, NULL, NULL, '0', '0', '0', '0', NULL, NULL, NULL, NULL, '2016-12-20 15:35:21', '2016-12-20 15:35:27');

INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, `interest_rate_cycle`, `customer_uuid`)
VALUES
	('1', 'test_contract_uuid', 'test_unique_id', '2016-11-01', 'test_contract_no', '2017-08-01', '0', NULL, '1', '1', '1', NULL, '2016-12-20 15:37:58', '0.0000000000', '3', '3', '3', NULL, '33000.00', NULL, '1', NULL, '2', 'test_financial_contract_uuid', '0', 'test_customer_uuid');

INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_Interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`,`order_payment_status`, `repay_schedule_no`, `outer_repayment_plan_no`)
VALUES
	('1', '0', '0', '11000.00', '10000.00', '1000.00', '10000.00', '2017-10-01', NULL, NULL, '0', '0', '0', NULL, 'test_asset_uuid_1', '2016-12-20 15:49:24', '2016-12-20 15:49:27', NULL, 'test_asset_no_1', '1', NULL, '1', '0', NULL, NULL, '0', '0', 'empty_deduct_uuid', NULL, 'test_financial_contract_uuid', NULL, NULL, NULL, NULL, '0', NULL, '0', '0', '0', '0', NULL,  'test_customer_uuid', 'test_contract_uuid', NULL,'0','56248960ff4820604ac87a246bd5a35d', 'outer1'),
	('2', '0', '0', '11000.00', '10000.00', '1000.00', '10000.00', '2017-11-01', NULL, NULL, '0', '0', '0', NULL, 'test_asset_uuid_2', '2016-12-20 15:49:24', '2016-12-20 15:49:27', NULL, 'test_asset_no_2', '1', NULL, '2', '0', NULL, NULL, '0', '0', 'empty_deduct_uuid', NULL, 'test_financial_contract_uuid', NULL, NULL, NULL, NULL, '0', NULL, '0', '0', '0', '0', NULL,  'test_customer_uuid', 'test_contract_uuid', NULL,'0','27b7b990b9a1a1e86ecc3541a124dddc', 'outer2'),
	('3', '0', '0', '11000.00', '10000.00', '1000.00', '10000.00', '2017-12-01', NULL, NULL, '0', '0', '0', NULL, 'test_asset_uuid_3', '2016-12-20 15:49:24', '2016-12-20 15:49:27', NULL, 'test_asset_no_3', '1', NULL, '3', '0', NULL, NULL, '0', '0', 'empty_deduct_uuid', NULL, 'test_financial_contract_uuid', NULL, NULL, NULL, NULL, '0', NULL, '0', '0', '0', '0', NULL,  'test_customer_uuid', 'test_contract_uuid', NULL,'0','7d07b1aab9a77a492257521154710f4d', 'outer3');

INSERT INTO `ledger_book` (`id`, `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`)
VALUES
	('1', 'test_ledger_book_no', '1', '1', NULL);

set foreign_key_checks=1;