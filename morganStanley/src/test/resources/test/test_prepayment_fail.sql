DELETE FROM `financial_contract`;
DELETE FROM	`contract`;
DELETE FROM `asset_set`;
DELETE FROM `app`;
DELETE FROM `house`;
DELETE FROM `customer`;
DELETE FROM `company`;
DELETE FROM `app`;

INSERT INTO `house` (`id`, `address`, `app_id`)
VALUES
('1', 'newyork', '1');

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`)
VALUES
('1', NULL, NULL, 'customer', NULL, '1', 'test-customer-uuid-1', '0');

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`)
VALUES
('1', '上海', '测试金融公司', '测试金融', 'test-conpany-uuid-1');

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`)
VALUES
('1', 'zfb', NULL, b'1', NULL, 'zufangbao', '1', NULL);

INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`)
VALUES
('1', '0', '0', '2016-11-24 15:07:46', 'test-contract-no-2', 'test-contract', '1', '1', '0', '2018-06-24 15:08:32', '1', '0', '1', '2', '1', NULL, 'test_financial-contract-uuid-1', '0', '0', '0', '0', '1', '0', NULL, NULL, NULL, NULL, '0', '0', '0', '1', NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, `interest_rate_cycle`)
VALUES
('1', 'test-uuid-1', 'test-unique-id-1', '2016-10-25', 'test-contract-no-1', '2018-06-24', NULL, NULL, '1', '1', '1', NULL, '2016-11-24 15:05:25', NULL, '1', '1', '1', NULL, '10000.00', NULL, '10002', NULL, '2', 'test-financial-contract-uuid-1', '0');

INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`)
VALUES
('1', '0', '0', '10000.00', '9000.00', '1000.00', '0.00', '2017-12-24', NULL, '0.00', '0', '1', '0', NULL, 'test-asset-uuid-1', '2016-11-24 15:01:12', '2016-11-25 00:01:18', NULL, 'test-asset-no-1', '1', NULL, '1', '0', NULL, NULL, '0', '0', 'empty_deduct_uuid', NULL, 'test-financial-contract-uuid');
