DELETE FROM `financial_contract`;
DELETE FROM `t_interface_voucher_log`;
DELETE FROM `app`;
DELETE FROM `company`;
DELETE FROM `source_document`;
DELETE FROM `source_document_detail`;
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
	
INSERT INTO `source_document` (`id`, `company_id`, `source_document_uuid`, `source_document_type`, `create_time`, `issued_time`, `source_document_status`, `source_account_side`, `booking_amount`, `outlier_document_uuid`, `outlier_trade_time`, `outlier_counter_party_account`, `outlier_counter_party_name`, `outlier_account`, `outlie_account_name`, `outlier_account_id`, `outlier_company_id`, `outlier_serial_global_identity`, `outlier_memo`, `outlier_amount`, `outlier_settlement_modes`, `outlier_breif`, `outlier_account_side`, `appendix`, `first_outlier_doc_type`, `second_outlier_doc_type`, `third_outlier_doc_type`, `currency_type`, `audit_status`, `first_party_id`, `second_party_id`, `virtual_account_uuid`, `first_account_id`, `second_account_id`, `third_account_id`, `excute_status`, `excute_result`, `related_contract_uuid`, `financial_contract_uuid`, `source_document_no`, `first_party_type`, `first_party_name`, `virtual_account_no`, `last_modified_time`) 
VALUES
	('1', '1', 'test_source_document_uuid_1', '0', '2016-11-01 11:59:56', '2016-12-02 12:00:01', '0', NULL, '0.00', 'test_cash_flow_uuid_1', '2016-11-30 12:00:59', 'test_party_account_1', 'test_party_name_1', 'test_outlier_account_1', 'test_outlier_account_name_1', '1', '2', 'test_transaction_no_1', NULL, '10000.00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'test_financial_contract_uuid_1', 'test_source_document_no_1', NULL, NULL, NULL, NULL);
	
INSERT INTO `t_interface_voucher_log` (`id`, `request_no`, `transaction_type`, `business_voucher_no`, `voucher_type`, `voucher_amount`, `financial_contract_no`, `receivable_account_no`, `payment_account_no`, `payment_name`, `payment_bank`, `bank_transaction_no`, `create_time`, `ip`) 
VALUES 
	('1', 'test_request_no_1', '0', 'test_business_voucher_no_1', '0', '10000.00', 'test_contract_no_1', 'test_receivable_account_no_1', 'test_payment_account_no_1', 'test_payment_name_1', NULL, 'test_transaction_no_1', '2016-11-01 11:00:55', '127.0.0.1');
	
	INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`) 
VALUES 
	('1', '0', '3', '2016-11-01 10:51:44', 'test_contract_no_1', 'test_contract_name_1', '1', '1', '60', '2017-04-30 10:52:25', '1', '0', '1', '2', '1', 'test_ledger_book_no_1', 'test_financial_contract_uuid_1', '0', '0', '0', '0', '1', '0', NULL, NULL, NULL, NULL, '0', '0', '0', '7', NULL, NULL, NULL, NULL, NULL, NULL);
	
INSERT INTO `source_document_detail` (`id`, `uuid`, `source_document_uuid`, `contract_unique_id`, `repayment_plan_no`, `amount`, `status`, `first_type`, `first_no`, `second_type`, `second_no`, `payer`, `receivable_account_no`, `payment_account_no`, `payment_name`, `payment_bank`, `check_state`, `comment`, `financial_contract_uuid`, `voucher_uuid`) 
VALUES 
	('1', 'test_detail_uuid_1', 'test_source_document_uuid_1', 'test_contract_unique_id_1', 'test_repayment_plan_no_1', '10000.00', '1', 'requestNo', 'test_request_no_1', 'bankTransactionNo', 'test_transaction_no_1', '0', 'test_receivable_account_no_1', 'test_payment_account_no_1', 'test_payment_name_1', 'test_payment_bank_1', '0', NULL, 'test_financial_contract_uuid_1', 'test_voucher_uuid');
	
INSERT INTO `t_voucher` (`id`, `uuid`, `voucher_no`, `source_document_uuid`, `financial_contract_uuid`, `amount`, `status`, `first_type`, `first_no`, `second_type`, `second_no`, `receivable_account_no`, `payment_account_no`, `payment_name`, `payment_bank`, `check_state`, `comment`, `create_time`, `contract_no`, `last_modified_time`, `cash_flow_uuid`, `transaction_time`) 
VALUES 
	('1', 'test_voucher_uuid', 'test_voucher_no', NULL, 'test_financial_contract_uuid_1', '1000.00', '0', 'test_first_type', 'test_first_no', 'test_second_type', 'test_transaction_no_1', NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL);

