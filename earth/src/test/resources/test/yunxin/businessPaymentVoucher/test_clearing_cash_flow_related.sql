SET FOREIGN_KEY_CHECKS=0;

DELETE FROM `cash_flow`;
DELETE FROM `source_document`;
DELETE FROM `financial_contract`;
DELETE FROM `audit_job`;
DELETE FROM `deduct_plan_stat_cache`;
DELETE FROM `total_receivable_bills`;
DELETE FROM `company`;
DELETE FROM `app`;
DELETE FROM `source_document`;
DELETE FROM `journal_voucher`;


INSERT INTO `audit_job` (`id`, `uuid`, `audit_job_no`, `financial_contract_uuid`, `capital_account_no`, `payment_channel_uuid`, `payment_channel_service_uuid`, `pg_clearing_account`, `payment_institution`, `audit_job_source`, `account_side`, `start_time`, `end_time`, `audit_result`, `create_time`, `last_modified_time`, `clearing_status`, `merchant_no`) 
VALUES ('7', 'f0106ff3-92f6-4609-9415-9f88cfc43c62', 'DZ1334073007007002624', 'b674a323-0c30-4e4b-9eba-b14e05a9d80a', '20001', 'e9c77d22-73da-11e6-bf08-00163e002839', 'f1ccca57-7c80-4429-b226-8ad31a729609', NULL, '0', '1', '1', '2017-03-01 00:00:00', '2017-03-08 23:59:59', '2', '2017-03-21 22:21:24', '2017-03-22 00:23:26', '1', '001053110000001');

INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`, `day_of_month`) 
VALUES ('37', '0', '3', '2016-09-01 00:00:00', 'G32000', '用钱宝测试', '2', '1', '60', '2017-12-01 00:00:00', '101', '0', '1', '2', '1', 'e17d8068-46a0-48bb-b158-8e80168bbfc3', 'b674a323-0c30-4e4b-9eba-b14e05a9d80a', '0', '0', '0', '0', '1', '0', NULL, NULL, NULL, NULL, '0', '0', '0', '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO `deduct_plan_stat_cache` (`id`, `uuid`, `financial_contract_uuid`, `payment_channel_uuid`, `payment_gateway`, `pg_account`, `pg_clearing_account`, `start_time`, `end_time`, `suc_amount`, `suc_num`, `fail_num`, `last_modified_time`) 
VALUES ('1', 'dbfbbd', 'b674a323-0c30-4e4b-9eba-b14e05a9d80a', 'f1ccca57-7c80-4429-b226-8ad31a729609', NULL, NULL, NULL, '2017-03-07 00:05:38', '2017-03-07 00:05:44', '400.00', '11', '22', '2017-03-10 00:06:22');

INSERT INTO `total_receivable_bills` (`id`, `uuid`, `financial_contract_uuid`, `payment_channel_uuid`, `clearing_receivable_identity`, `fst_merchant_no`, `fst_merchant_name`, `total_num`, `total_amount`, `total_charge`, `total_receivable_amount`, `last_clearing_cash_flow_identity`, `clearing_cash_flow_identity_list`, `clearing_time`, `create_time`, `last_modified_time`) 
VALUES ('1', 'dfvbfb', 'b674a323-0c30-4e4b-9eba-b14e05a9d80a', 'f1ccca57-7c80-4429-b226-8ad31a729609', '1111', 'a', 'a', '11', '400.00', '15.00', '500.00', '123', NULL, '2017-03-03 22:54:14', '2017-03-01 22:54:21', '2017-03-22 17:59:53');

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`) 
VALUES ('1', '上海', '测试金融公司', '测试金融', 'a02c02b9-6f98-11e6-bf08-00163e002839'),
('2', '上海', '测试金融公司', '测试金融', 'a02c02b9-6f98-11e6-bf08-00163e002839');																			

INSERT INTO `cash_flow` (`id`, `cash_flow_uuid`, `cash_flow_channel_type`, `company_uuid`, `host_account_uuid`, `host_account_no`, `host_account_name`, `counter_account_no`, `counter_account_name`, `counter_account_appendix`, `counter_bank_info`, `account_side`, `transaction_time`, `transaction_amount`, `balance`, `transaction_voucher_no`, `bank_sequence_no`, `remark`, `other_remark`, `strike_balance_status`, `trade_uuid`, `issued_amount`, `audit_status`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`) VALUES 
('111', '5e1a7e88-0a07-11e7-bf99-00163e00283912erf', '0', '', 'd0503298-e890-425a-4444444', '20001', '云南国际信托有限公司', '6217001210077600480', 'Wangtao', '', '', '1', '2017-03-22 16:14:01', '300.00', '0.00', '', '5e1a7ea1-0a07-11e7-bf99-00163e002839', '测试', '', NULL, '', '100.00', '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'cashFlowIdentity', NULL, NULL, NULL, NULL),
('112', '5e1a7e88-0a07-11e7-bf99-00163e00283912erfsdd', '0', '', 'd0503298-e890-425a-4444444', '20001', '云南国际信托有限公司', '6217001210077600480', 'Wangtao', '', '', '1', '2017-03-22 19:14:01', '400.00', '0.00', '', '5e1a7ea1-0a07-11e7-bf99-00163e002839', '测试', '', NULL, '', '0.00', '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'cashFlowIdentity', NULL, NULL, NULL, NULL);

INSERT INTO `source_document` (`id`, `company_id`, `source_document_uuid`, `source_document_type`, `create_time`, `issued_time`, `source_document_status`, `source_account_side`, `booking_amount`, `outlier_document_uuid`, `outlier_trade_time`, `outlier_counter_party_account`, `outlier_counter_party_name`, `outlier_account`, `outlie_account_name`, `outlier_account_id`, `outlier_company_id`, `outlier_serial_global_identity`, `outlier_memo`, `outlier_amount`, `outlier_settlement_modes`, `outlier_breif`, `outlier_account_side`, `appendix`, `first_outlier_doc_type`, `second_outlier_doc_type`, `third_outlier_doc_type`, `currency_type`, `audit_status`, `first_party_id`, `second_party_id`, `virtual_account_uuid`, `first_account_id`, `second_account_id`, `third_account_id`, `excute_status`, `excute_result`, `related_contract_uuid`, `financial_contract_uuid`, `source_document_no`, `first_party_type`, `first_party_name`, `virtual_account_no`, `last_modified_time`) 
VALUES
	('1', '1', 'test_source_document_uuid_1', '0', '2016-11-01 11:59:56', '2016-12-02 12:00:01', '0', NULL, '0.00', 'test_cash_flow_uuid_1', '2016-11-30 12:00:59', 'test_party_account_1', 'test_party_name_1', 'test_outlier_account_1', 'test_outlier_account_name_1', '1', '2', 'test_transaction_no_1', NULL, '10000.00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'test_financial_contract_uuid_1', 'test_source_document_no_1', NULL, NULL, NULL, NULL);

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`) 
VALUES ('2', 'qyb', NULL, '\0', NULL, '测试商户yqb', '1', NULL);

SET FOREIGN_KEY_CHECKS=1;




