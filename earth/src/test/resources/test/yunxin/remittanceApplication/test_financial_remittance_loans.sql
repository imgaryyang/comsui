SET FOREIGN_KEY_CHECKS=0;


delete from `journal_voucher`;
delete from `source_document`;
delete from `financial_contract`;
delete from `cash_flow`;
--delete from `ledger_book_shelf`;
--delete from `t_account_template`;
--delete from `t_business_scenario_definition`;
--delete from `t_ledger_book_batch`;


INSERT INTO `ledger_book` (`id`, `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`, `ledger_book_version`) VALUES ('1', 'yunxin_ledger_book', '1', NULL, NULL, '');


INSERT INTO `journal_voucher` (`id`, `account_side`, `bank_identity`, `billing_plan_uuid`, `booking_amount`, `business_voucher_type_uuid`, `business_voucher_uuid`, `cash_flow_amount`, `cash_flow_breif`, `cash_flow_channel_type`, `cash_flow_serial_no`, `cash_flow_uuid`, `checking_level`, `company_id`, `completeness`, `counter_party_account`, `counter_party_name`, `journal_voucher_uuid`, `notification_identity`, `notification_memo`, `notification_record_uuid`, `notified_date`, `settlement_modes`, `source_document_amount`, `source_document_breif`, `source_document_cash_flow_serial_no`, `source_document_counter_party_uuid`, `source_document_identity`, `source_document_uuid`, `status`, `trade_time`, `batch_uuid`, `created_date`, `source_document_counter_party_account`, `source_document_counter_party_name`, `issued_time`, `journal_voucher_type`, `counter_account_type`, `related_bill_contract_info_lv_1`, `related_bill_contract_info_lv_2`, `related_bill_contract_info_lv_3`, `cash_flow_account_info`, `journal_voucher_no`, `related_bill_contract_no_lv_1`, `related_bill_contract_no_lv_2`, `related_bill_contract_no_lv_3`, `related_bill_contract_no_lv_4`, `source_document_no`, `appendix`, `last_modified_time`, `local_party_account`, `local_party_name`, `source_document_local_party_account`, `source_document_local_party_name`, `second_journal_voucher_type`, `third_journal_voucher_type`, `is_has_data_sync_log`) 
VALUES (1, 1, '2213', 'billing_plan_uuid_1', 1000.00, 'business_voucher_type_uuid_1','business_voucher_uuid_1', 10000, '', 1, 'cash_flow_serial_no_1', 'cash_flow_uuid_1', 1, 2233, 1,'counter_party_account', 'counter_party_name', 'journal_voucher_uuid_1', 'notification_identity_1', 'notification_memo_1', 'notification_record_uuid_1', '2015-10-19 13:34:35', 1, 1000.00, 'source_document_breif_1', 'source_document_cash_flow_serial_no_1', 'source_document_counter_party_uuid_1', 'source_document_identity_1', 'source_document_uuid_1', 1, '2015-10-20 13:34:35', 'batch_uuid_1', '2017-08-07 12:12:23', 'source_document_counter_party_account_1', 'source_document_counter_party_name_1', '2017-08-07 12:12:23', 1, 1, 'related_bill_contract_info_lv_1_1', 'related_bill_contract_info_lv_2_1', 'related_bill_contract_info_lv_3_1', 'cash_flow_account_info_1', 'journal_voucher_no_1', 'related_bill_contract_no_lv_1_1', 'related_bill_contract_no_lv_2_1', 'related_bill_contract_no_lv_3_1', 'related_bill_contract_no_lv_4_1', 'source_document_no_1', '', '2017-08-22 12:00:00', 'local_party_account_1', 'local_party_name_1', 'source_document_local_party_account_1', 'source_document_local_party_name_1', 1, 1, 1);


INSERT INTO `source_document` (`id`, `company_id`, `source_document_uuid`, `source_document_type`, `create_time`, `issued_time`, `source_document_status`, `source_account_side`, `booking_amount`, `outlier_document_uuid`, `outlier_trade_time`, `outlier_counter_party_account`, `outlier_counter_party_name`, `outlier_account`, `outlie_account_name`, `outlier_account_id`, `outlier_company_id`, `outlier_serial_global_identity`, `outlier_memo`, `outlier_amount`, `outlier_settlement_modes`, `outlier_breif`, `outlier_account_side`, `appendix`, `first_outlier_doc_type`, `second_outlier_doc_type`, `third_outlier_doc_type`, `currency_type`, `audit_status`, `first_party_id`, `second_party_id`, `virtual_account_uuid`, `first_account_id`, `second_account_id`, `third_account_id`, `excute_status`, `excute_result`, `related_contract_uuid`, `financial_contract_uuid`, `source_document_no`, `first_party_type`, `first_party_name`, `virtual_account_no`, `last_modified_time`, `voucher_uuid`, `plan_booking_amount`) 
VALUES (1, 2233, 'source_document_uuid_1', 1, '2017-08-07 12:00:00', '2017-08-07 12:12:23', 1, 1, 1000.00, 'outlier_document_uuid_1', '2017-08-17 12:00:00', 'outlier_counter_party_account_1', 'outlier_counter_party_name_1', 'outlier_account_1', 'outlie_account_name_1', 12345, 12345, 'outlier_serial_global_identity_1', 'outlier_memo_1', 123.00, 1, '', 1, '', 'first_outlier_doc_type_1', 'second_outlier_doc_type_1', 'third_outlier_doc_type_1', 1, 1, 'first_party_id_1', 'second_party_id_2', 'virtual_account_uuid_1', 'first_account_id_1', 'second_account_id_1', 'third_account_id_1', 1, 1, 'related_contract_uuid_1', 'financial_contract_uuid_1', 'source_document_no_1', '1', 'first_party_name_1', 'virtual_account_no_1', '2017-08-22 12:00:00', 'voucher_uuid_1',1000.00);

INSERT INTO `financial_contract` (`financial_contract_uuid`,`adva_repayment_term`,`id`, `adva_matuterm`, `contract_no`, `contract_name`, `app_id`, `company_id`,   `adva_repo_term`,`loan_overdue_end_day`,`loan_overdue_start_day`,`payment_channel_id`,`ledger_book_no`,`asset_package_format`,`financial_contract_type`,`unusual_modify_flag`) VALUES 
('financial_contract_uuid_1','0','1', 3, 'DCF-NFQ-LR903A', '云南信托', 1, 14, 30,90,1,1,'yunxin_ledger_book',1,0,0);

INSERT INTO `cash_flow` (`id`, `cash_flow_uuid`, `cash_flow_channel_type`, `company_uuid`, `host_account_uuid`, `host_account_no`, `host_account_name`, `counter_account_no`, `counter_account_name`, `counter_account_appendix`, `counter_bank_info`, `account_side`, `transaction_time`, `transaction_amount`, `balance`, `transaction_voucher_no`, `bank_sequence_no`, `remark`, `other_remark`, `strike_balance_status`, `cash_flow_type`, `trade_uuid`, `issued_amount`, `audit_status`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`) 
VALUES ('1', 'cash_flow_uuid_1', NULL, '33333333tttttter33333', '4444fdjskfndjks4232ifdskf', '3274832786', 'zhongtaiyinhang', '2131241233213', 'suidifugongsi', NULL, NULL, NULL, NULL, '100000.00', NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, '9999.00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);



SET FOREIGN_KEY_CHECKS=1;


