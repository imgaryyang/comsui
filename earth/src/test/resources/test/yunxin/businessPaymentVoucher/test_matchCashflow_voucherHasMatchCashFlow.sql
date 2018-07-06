DELETE FROM `financial_contract`;
DELETE FROM `t_interface_voucher_log`;
DELETE FROM `app`;
DELETE FROM `company`;
DELETE FROM `source_document`;
DELETE FROM `source_document_detail`;
DELETE FROM `cash_flow`;
DELETE FROM `t_voucher`;

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`) 
VALUES 
	('1', 'pairs', 'test_company_1', 'tc1', 'test_company_uuid_1');
INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`) 
VALUES 
	('2', '`newyork', 'test_company_2', 'tc2', 'test_company_uuid_2');

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`) 
VALUES 
	('1', '1', 'test_app_sercet_1', false, 'test', 'test_app_name_1', '2', 'newyork');
	
INSERT INTO `t_interface_voucher_log` (`id`, `request_no`, `transaction_type`, `business_voucher_no`, `voucher_type`, `voucher_amount`, `financial_contract_no`, `receivable_account_no`, `payment_account_no`, `payment_name`, `payment_bank`, `bank_transaction_no`, `create_time`, `ip`) 
VALUES 
	('1', 'test_request_no_1', '0', 'test_business_voucher_no_1', '0', '10000.00', 'test_contract_no_1', 'test_receivable_account_no_1', 'test_payment_account_no_1', 'test_payment_name_1', NULL, 'test_transaction_no_1', '2016-11-01 11:00:55', '127.0.0.1');

INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`) 
VALUES 
	('1', '0', '3', '2016-11-01 10:51:44', 'test_contract_no_1', 'test_contract_name_1', '1', '1', '60', '2017-04-30 10:52:25', '1', '0', '1', '2', '1', 'test_ledger_book_no_1', 'test_financial_contract_uuid_1', '0', '0', '0', '0', '1', '0', NULL, NULL, NULL, NULL, '0', '0', '0', '7', NULL, NULL, NULL, NULL, NULL, NULL);
	
INSERT INTO `cash_flow` (`id`, `cash_flow_uuid`, `cash_flow_channel_type`, `company_uuid`, `host_account_uuid`, `host_account_no`, `host_account_name`, `counter_account_no`, `counter_account_name`, `counter_account_appendix`, `counter_bank_info`, `account_side`, `transaction_time`, `transaction_amount`, `balance`, `transaction_voucher_no`, `bank_sequence_no`, `remark`, `other_remark`, `strike_balance_status`, `trade_uuid`, `issued_amount`, `audit_status`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`) 
VALUES 
	('1', 'test_cash_flow_uuid_1', '0', 'test_company_uuid_1', 'test_host_account_uuid_1', 'test_host_account_no_1', 'test_host_account_name_1', 'payment_account_no', 'payment_name', NULL, NULL, NULL, '2016-11-22 14:49:53', '10000.00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'bankTransactionNo', NULL, NULL, NULL, NULL, NULL);

INSERT INTO `t_voucher` (`id`, `uuid`, `voucher_no`, `source_document_uuid`, `financial_contract_uuid`, `amount`, `status`, `first_type`, `first_no`, `second_type`, `second_no`, `receivable_account_no`, `payment_account_no`, `payment_name`, `payment_bank`, `check_state`, `comment`, `create_time`, `cash_flow_uuid`) 
VALUES 
	('1', 'test_voucher_uuid_1', 'test_voucher_no_1', 'test_source_document_uuid_1', 'test_financial_contract_uuid_1', '10000.00', '0', 'requestNo', 'test_request_no_1', 'bankTrasactionNo', 'test_transaction_no_1', 'receivable_account_no', 'payment_account_no', 'payment_name', 'payment_bank', '1', NULL, '2016-09-01 14:11:27', 'test_cash_flow_uuid_1');
