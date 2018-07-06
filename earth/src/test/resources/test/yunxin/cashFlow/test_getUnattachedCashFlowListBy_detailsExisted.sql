DELETE FROM `cash_flow`;
DELETE FROM `source_document`;
DELETE FROM `source_document_detail`;


INSERT INTO `cash_flow` (`id`, `cash_flow_uuid`, `cash_flow_channel_type`, `company_uuid`, `host_account_uuid`, `host_account_no`, `host_account_name`, `counter_account_no`, `counter_account_name`, `counter_account_appendix`, `counter_bank_info`, `account_side`, `transaction_time`, `transaction_amount`, `balance`, `transaction_voucher_no`, `bank_sequence_no`, `remark`, `other_remark`, `strike_balance_status`, `trade_uuid`, `issued_amount`, `audit_status`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`) 
VALUES 
	('1', 'test_cash_flow_uuid_1', '0', 'test_company_uuid_1', 'test_host_account_uuid_1', 'test_host_account_no_1', 'test_host_account_name_1', 'test_counter_account_no_1', 'test_counter_account_name_1', NULL, NULL, '0', '2016-11-29 17:54:02', '10000.00', '0.00', 'test_voucher_no_1', 'test_sequence_no_1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'test_transaction_no1', NULL, NULL, NULL, NULL, NULL);

INSERT INTO `source_document` (`id`, `company_id`, `source_document_uuid`, `source_document_type`, `create_time`, `issued_time`, `source_document_status`, `source_account_side`, `booking_amount`, `outlier_document_uuid`, `outlier_trade_time`, `outlier_counter_party_account`, `outlier_counter_party_name`, `outlier_account`, `outlie_account_name`, `outlier_account_id`, `outlier_company_id`, `outlier_serial_global_identity`, `outlier_memo`, `outlier_amount`, `outlier_settlement_modes`, `outlier_breif`, `outlier_account_side`, `appendix`, `first_outlier_doc_type`, `second_outlier_doc_type`, `third_outlier_doc_type`, `currency_type`, `audit_status`, `first_party_id`, `second_party_id`, `virtual_account_uuid`, `first_account_id`, `second_account_id`, `third_account_id`, `excute_status`, `excute_result`, `related_contract_uuid`, `financial_contract_uuid`, `source_document_no`, `first_party_type`, `first_party_name`, `virtual_account_no`, `last_modified_time`) 
VALUES 
	('1', '1', 'test_source_document_uuid_1', '0', '2016-11-29 18:10:09', '2016-11-29 18:10:12', '0', '0', '0.00', 'test_cash_flow_uuid_1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'test_identity_1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO `source_document_detail` (`id`, `uuid`, `source_document_uuid`, `contract_unique_id`, `repayment_plan_no`, `amount`, `status`, `first_type`, `first_no`, `second_type`, `second_no`, `payer`, `receivable_account_no`, `payment_account_no`, `payment_name`, `payment_bank`, `check_state`, `comment`, `financial_contract_uuid`) 
VALUES 
	('1', 'test_detail_uuid_1', 'test_source_document_uuid_1', 'test_contract_unique_id_1', 'test_repayment_plan_no_1', '10000.00', '0', NULL, NULL, NULL, 'test_identity_1', NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL);
