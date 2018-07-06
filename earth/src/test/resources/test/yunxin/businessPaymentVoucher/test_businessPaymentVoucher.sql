DELETE FROM `financial_contract`;
DELETE FROM `t_interface_voucher_log`;
DELETE FROM `app`;
DELETE FROM `company`;
DELETE FROM `source_document`;
DELETE FROM `cash_flow`;
DELETE FROM `source_document_detail`;
DELETE FROM `dictionary`;
DELETE FROM `account`;
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

INSERT INTO `source_document` (`id`, `company_id`, `source_document_uuid`, `source_document_type`, `create_time`, `issued_time`, `source_document_status`, `source_account_side`, `booking_amount`, `outlier_document_uuid`, `outlier_trade_time`, `outlier_counter_party_account`, `outlier_counter_party_name`, `outlier_account`, `outlie_account_name`, `outlier_account_id`, `outlier_company_id`, `outlier_serial_global_identity`, `outlier_memo`, `outlier_amount`, `outlier_settlement_modes`, `outlier_breif`, `outlier_account_side`, `appendix`, `first_outlier_doc_type`, `second_outlier_doc_type`, `third_outlier_doc_type`, `currency_type`, `audit_status`, `first_party_id`, `second_party_id`, `virtual_account_uuid`, `first_account_id`, `second_account_id`, `third_account_id`, `excute_status`, `excute_result`, `related_contract_uuid`, `financial_contract_uuid`, `source_document_no`, `first_party_type`, `first_party_name`, `virtual_account_no`, `last_modified_time`) 
VALUES
	('1', '1', 'test_source_document_uuid_1', '0', '2016-11-01 11:59:56', '2016-12-02 12:00:01', '1', NULL, '0.00', 'test_cash_flow_uuid_1', '2016-11-30 12:00:59', 'test_party_account_1', 'test_party_name_1', 'test_outlier_account_1', 'test_outlier_account_name_1', '1', '2', 'test_transaction_no_1', NULL, '10000.00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL, '50000', '50000.01', NULL, NULL, NULL, NULL, 'test_financial_contract_uuid_1', 'test_source_document_no_1', NULL, NULL, NULL, NULL);

INSERT INTO `cash_flow` (`id`, `cash_flow_uuid`, `cash_flow_channel_type`, `company_uuid`, `host_account_uuid`, `host_account_no`, `host_account_name`, `counter_account_no`, `counter_account_name`, `counter_account_appendix`, `counter_bank_info`, `account_side`, `transaction_time`, `transaction_amount`, `balance`, `transaction_voucher_no`, `bank_sequence_no`, `remark`, `other_remark`, `strike_balance_status`, `trade_uuid`, `issued_amount`, `audit_status`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`) 
VALUES 
	('1', 'test_cash_flow_uuid_1', '0', 'test_company_uuid_1', 'test_host_account_uuid_1', 'test_host_account_no_1', 'test_host_account_name_1', 'test_payment_account_no_1', 'test_payment_name_1', NULL, NULL, NULL, '2016-11-22 14:49:53', '10000.00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO `account` (`id`, `account_name`, `account_no`, `bank_name`, `alias`, `attr`, `scan_cash_flow_switch`, `usb_key_configured`, `uuid`, `bank_code`, `cash_flow_config`) 
VALUES 
	('1', 'test_account_name', 'test_receivable_account_no_1', '', NULL, NULL, b'0', b'0', 'test_account_uuid', NULL, NULL);

	
INSERT INTO `dictionary` (`code`, `content`)
VALUES
	('PLATFORM_PRI_KEY', 'MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=');
