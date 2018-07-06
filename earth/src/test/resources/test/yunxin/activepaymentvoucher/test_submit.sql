delete from `contract`;
delete from `financial_contract`;
delete from `asset_set`;
delete from `company`;
delete from `cash_flow`;
delete from `customer`;
delete from `contract_account`;
delete from `account`;
delete from `t_voucher`;
delete from `source_document_detail`;
delete from `virtual_account`;

INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, `interest_rate_cycle`, `customer_uuid`) 
VALUES 
('1', 'test_contract_uuid', 'test_unique_id', '2016-11-01', 'test_contract_no', '2017-08-01', '0', NULL, '1', '1', '1', NULL, '2016-12-20 15:37:58', '0.0000000000', '3', '3', '2', NULL, '33000.00', NULL, '1020470591', NULL, '2', 'test_financial_contract_uuid', '0', 'test_customer_uuid');

INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`, `day_of_month`) 
VALUES 
('1', NULL, '0', '2016-09-01 15:31:58', 'test_financial_contract_no', 'test_financial_contract', '1', '1', '0', '2017-11-01 15:32:56', '1', '0', '0', '0', '0', 'test_ledger_book_no', 'test_financial_contract_uuid', '0', '0', '0', '0', '0', '0', NULL, NULL, NULL, NULL, '0', '0', '0', '0', NULL, NULL, NULL, NULL, '2016-12-20 15:35:21', '2016-12-20 15:35:27', NULL, NULL, NULL, NULL);

INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`) 
VALUES 
('1', '0', '0', '11000.00', '10000.00', '1000.00', '10000.00', '2016-12-10', NULL, NULL, '0', '0', '0', NULL, 'test_asset_uuid_1', '2016-12-20 15:49:24', '2016-12-20 15:49:27', NULL, 'test_asset_no_1', '1', NULL, '1', '0', NULL, NULL, '0', '0', 'empty_deduct_uuid', NULL, 'test_financial_contract_uuid', NULL, NULL, NULL, NULL, '0', NULL, '0', '0', '0', '0', NULL, 'test_customer_uuid', 'test_contract_uuid', NULL),
('2', '0', '0', '11000.00', '10000.00', '1000.00', '10000.00', '2017-05-01', NULL, NULL, '0', '0', '0', NULL, 'test_asset_uuid_2', '2016-12-20 15:49:24', '2016-12-20 15:49:27', NULL, 'test_asset_no_2', '1', NULL, '2', '0', NULL, NULL, '0', '0', 'empty_deduct_uuid', NULL, 'test_financial_contract_uuid', NULL, NULL, NULL, NULL, '0', NULL, '0', '0', '0', '0', NULL, 'test_customer_uuid', 'test_contract_uuid', NULL),
('3', '0', '0', '11000.00', '10000.00', '1000.00', '10000.00', '2017-06-01', NULL, NULL, '0', '0', '0', NULL, 'test_asset_uuid_3', '2016-12-20 15:49:24', '2016-12-20 15:49:27', NULL, 'test_asset_no_3', '1', NULL, '3', '0', NULL, NULL, '0', '0', 'empty_deduct_uuid', NULL, 'test_financial_contract_uuid', NULL, NULL, NULL, NULL, '0', NULL, '0', '0', '0', '0', NULL, 'test_customer_uuid', 'test_contract_uuid', NULL);

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`) 
VALUES 
('1', 'shanghai', 'test_company', 't_c', 'test_uuid');

INSERT INTO `cash_flow` (`id`, `cash_flow_uuid`, `cash_flow_channel_type`, `company_uuid`, `host_account_uuid`, `host_account_no`, `host_account_name`, `counter_account_no`, `counter_account_name`, `counter_account_appendix`, `counter_bank_info`, `account_side`, `transaction_time`, `transaction_amount`, `balance`, `transaction_voucher_no`, `bank_sequence_no`, `remark`, `other_remark`, `strike_balance_status`, `trade_uuid`, `issued_amount`, `audit_status`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`) 
VALUES 
('1', 'test_cash_flow_uuid', NULL, 'test_uuid', 'test_h_a_uuid', 'test_h_a_n', 'test_h_a_n', 'test_c_a_n', 'test_c_a_n', '', NULL, NULL, NULL, '10000.00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`) 
VALUES 
('1', 'test_account', '12345678910', 'test_name', NULL, '1', 'test_customer_uuid', '0');

INSERT INTO `contract_account` (`id`, `pay_ac_no`, `bankcard_type`, `contract_id`, `payer_name`, `bank`, `bind_id`, `id_card_num`, `bank_code`, `province`, `province_code`, `city`, `city_code`, `standard_bank_code`, `from_date`, `thru_date`, `virtual_account_uuid`, `bank_card_status`, `contract_account_uuid`, `contract_account_type`) 
VALUES 
('1', 'test_pay_ac_no', NULL, '1', 'test_payer_name', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2900-01-01 00:00:00', NULL, '0', 'test_contract_account_uuid', '0');

INSERT INTO `account` (`id`, `account_name`, `account_no`, `bank_name`, `alias`, `attr`, `scan_cash_flow_switch`, `usb_key_configured`, `uuid`, `bank_code`, `cash_flow_config`) 
VALUES 
('1', 'tset_account_name', 'test_account_no', '', NULL, NULL, b'0', b'0', 'test_account_uuid', NULL, NULL);

