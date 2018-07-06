SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM `source_document_detail`;
delete from `asset_set`;
delete from `financial_contract`;
delete from `contract`;
delete from `asset_valuation_detail`;
delete from `ledger_book_shelf`;
delete from `payment_channel`;
delete from `customer`;
delete from `app`;
delete from `company`;
delete from `house`;
delete from `ledger_book`;
delete from `asset_package`;
delete from `loan_batch`;
delete from `virtual_account`;
delete from `virtual_account_flow`;
delete from `source_document_detail`;
delete from `source_document`;
delete from `journal_voucher`;
delete from `business_voucher`;
delete from `rent_order`;
delete from `cash_flow`;
delete from `account`;
delete from `virtual_account_payment_black_list`;
delete from `app_account`;
delete from `repurchase_doc`;
delete from `company`;

INSERT INTO `account` (`id`, `account_name`, `account_no`, `bank_name`, `alias`, `attr`, `scan_cash_flow_switch`, `usb_key_configured`, `uuid`, `bank_code`) VALUES 
('1', '云南信托国际有限公司', '600000000001', '云南国际信托有限公司', NULL, NULL, '\0', '\0', '9d2c6b35-9e0a-447f-bd76-42a8634f8c3e', NULL);




INSERT INTO `app_account` (`id`, `uuid`, `bank_name`, `account_name`, `account_no`, `app_account_active_status`, `app_id`) VALUES 
('1', 'uuid_3', '银行', 'ppd_user', '10001', '0', '1');

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee` )
VALUES 
('1', 'anmeitu', '11111db75ebb24fa0993f4fa25775023', '\0', 'http://e.zufangbao.cn', '安美途', '1', NULL);

INSERT INTO `payment_channel` (`id`, `channel_name`, `user_name`, `user_password`, `merchant_id`, `cer_file_path`, `pfx_file_path`, `pfx_file_key`, `payment_channel_type`, `api_url`, `from_date`, `thru_date`) 
 VALUES 
 ('1', '广东银联', 'user1', '123456', '123', NULL, NULL, NULL, '0', NULL, NULL, NULL);


INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`,`uuid`) 
VALUES 
('1', '上海', '云南国际信托有限公司', '云南信托','companyuuid');


INSERT INTO `journal_voucher` (`id`, `account_side`, `bank_identity`, `billing_plan_uuid`, `booking_amount`, `business_voucher_type_uuid`, `business_voucher_uuid`, `cash_flow_amount`, `cash_flow_breif`, `cash_flow_channel_type`, `cash_flow_serial_no`, `cash_flow_uuid`, `checking_level`, `company_id`, `completeness`, `counter_party_account`, `counter_party_name`, `journal_voucher_uuid`, `notification_identity`, `notification_memo`, `notification_record_uuid`, `notified_date`, `settlement_modes`, `source_document_amount`, `source_document_breif`, `source_document_cash_flow_serial_no`, `source_document_counter_party_uuid`, `source_document_identity`, `source_document_uuid`, `status`, `trade_time`, `batch_uuid`, `created_date`, `source_document_counter_party_account`, `source_document_counter_party_name`, `issued_time`, `journal_voucher_type`, `counter_account_type`, `related_bill_contract_info_lv_1`, `related_bill_contract_info_lv_2`, `related_bill_contract_info_lv_3`, `cash_flow_account_info`, `journal_voucher_no`, `related_bill_contract_no_lv_1`, `related_bill_contract_no_lv_2`, `related_bill_contract_no_lv_3`, `related_bill_contract_no_lv_4`, `source_document_no`, `appendix`, `last_modified_time`) VALUE
 ('1', '1', '', '113e1f29-856f-4916-82d1-424e32612be2', '1.01', '2b784ef2-a1ab-41fa-b315-8e1bf8cfa2ba', '', '0.00', '', NULL, '', '', '0', '1', '1', '6217001210031616480', '韩方园', 'cd0f2929-b4a9-49b5-9712-652465346e74', '', '', '', '2016-08-27 15:57:47', NULL, '1.01', '', '', '', '6fbe92ed-674b-436f-96ae-cd71e5cd6ecc', 'e73e28ee-1dd2-4009-8973-576c038042e4', '0', '2016-08-27 15:57:47', '', '2016-09-21 09:33:46', '20001', '云南信托', '2016-08-27 15:57:47', '7', '0', 'b674a323-0c30-4e4b-9eba-b14e05a9d80a', '287c16f2-77cb-4d3a-8f9a-05904b370546', 'ef0ae4ed-7a75-4895-a739-a82e2e8930f5', '', '8d297098-80c7-11e6-b7d3-00163e002839', '', '', '', 'JS2736C1C9D8B66E34', '', '', '2016-08-27 15:57:47');

 
INSERT INTO `asset_set` (`contract_uuid`,`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`,`executing_status`) VALUES 
('contract_uuid','1', '0', '0', '0.01', '0.01', '0.00', '0.01', '2016-01-01', NULL, '0.00', '0', '1', '0', NULL, 'asset_uuid', '2016-08-27 15:57:47', '2016-08-27 15:57:47', NULL, 'ZC2743B948833DA777', '1', NULL, '2', '0', NULL, '1', '0', '0', 'empty_deduct_uuid','3'),
('contract_uuid','2', '0', '0', '0.01', '0.01', '0.00', '0.01', '2016-01-01', NULL, '0.00', '0', '1', '0', NULL, 'asset_uuid2', '2016-06-27 15:57:47', '2016-08-27 15:57:47', NULL, 'ZC2743B948833DA776', '1', NULL, '2', '0', NULL, '1', '0', '0', 'empty_deduct_uuid','3');



INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`) VALUES 
('1', 'contract_uuid', 'unique_id', '2016-01-02', 'G00003(zht36853461685090410)', '2018-01-01', NULL, '0.00', '1', '1', '1', NULL, '2016-08-27 16:06:53', '0.1200000000', '0', '0', '2', '2', '0.02', '11.0000000000', '1', NULL, '2', 'financial_contract_uuid');



INSERT INTO `financial_contract` (`adva_repayment_term`,`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`) VALUES 
('0','1', '0', '1', '2016-09-01 00:00:00', 'G31700', '拍拍贷测试', '1', '1', '60', '2017-08-31 00:00:00', '1', '0', '1', '2', '1', 'ledger_book_no', 'financial_contract_uuid', '0', '0', '0', '0', '1', '0');
 

INSERT INTO `ledger_book` (`id`, `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`) VALUES 
('1', 'ledger_book_no', '1', NULL, NULL);

INSERT INTO `house` (`id`, `address`, `app_id`) VALUES ('1', NULL, '1');


INSERT INTO `source_document_detail` (`id`, `uuid`, `source_document_uuid`, `contract_unique_id`, `repayment_plan_no`, `amount`, `status`, `first_type`, `first_no`, `second_type`, `second_no`, `payer`, `receivable_account_no`, `payment_account_no`, `payment_name`, `payment_bank`, `check_state`, `comment`, `financial_contract_uuid`, `principal`, `interest`, `service_charge`, `maintenance_charge`, `other_charge`, `penalty_fee`, `late_penalty`, `late_fee`, `late_other_cost`, `voucher_uuid`, `actual_payment_time`) VALUES ('1', 'source_document_detail_uuid', 'source_document_uuid', 'unique_id', 'repurchase_doc_uuid', '2700.00', '1', 'enum.voucher-source.business-payment-voucher', 'ebfdd395-d5aa-4612-b728-0858479317d3', 'enum.voucher-type.repurchase', '1dfab272-8b75-4439-91e9-af6eabb15670', '0', '6600000000000000001', '10001', 'counter_name', 'account_account_name', '0', NULL, NULL, '2100.00', '100.00', '0.00', '0.00', '300.00', '200.00', '0.00', '0.00', '0.00', NULL, NULL);

INSERT INTO `repurchase_doc` (`id`, `repurchase_doc_uuid`, `financial_contract_uuid`, `amount`, `repo_start_date`, `repo_end_date`, `repo_days`, `creat_time`, `verification_time`, `last_modifed_time`, `contract_id`, `contract_no`, `app_id`, `app_name`, `customer_uuid`, `customer_name`, `executing_asset_set_uuids`, `asset_set_uuids`, `repurchase_status`, `repurchase_algorithm`, `repurchase_approach`, `repurchase_rule`, `day_of_month`, `adva_repo_term`, `repurchase_periods`, `amount_detail`) VALUES ('1', 'repurchase_doc_uuid', 'financial_contract_uuid', '2200.00', '2016-10-20', '2016-11-03', '3', '2016-10-31 20:14:59', '2016-11-01 15:17:55', '2016-10-31 20:14:59', '1', 'G00003(zht36853461685090410)', '1', '安美途', 'customerUuid1', 'aa', NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO `ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`) VALUES 
('1', 'ledger_uuid_1', '0.00', '900.00', 'FST_LONGTERM_LIABILITY', '40000', '0', 'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL, '1', NULL, NULL, NULL, NULL, '871562dd-5a0f-4a8d-990f-07d75bfc0304', '2016-05-01', '2016-05-16 14:26:50', '', NULL, '7', NULL, '2016-05-27 00:00:00', '', 'ledger_book_no', '1', '1', 'repayment_plan_no_1', 'd2c21631-f2fb-4e8f-beef-0a2e72447434', NULL, NULL, NULL, NULL, ''),
('2', 'ledger_uuid_2', '1000.00', '0.00', 'FST_RECIEVABLE_ASSET', '20000', '1', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', NULL, NULL, '1', 'customerUuid1', NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '616', NULL, '2016-08-28 00:00:00', NULL, 'ledger_book_no', '1', '1', 'test_single_no', 'asset_uuid', NULL, NULL, NULL, NULL, NULL),
('3', 'ledger_uuid_3', '500.00', '0.00', 'FST_RECIEVABLE_ASSET', '20000', '1', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', NULL, NULL, '1', 'customerUuid1', NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '616', NULL, '2016-08-28 00:00:00', NULL, 'ledger_book_no', '1', '1', 'test_single_no2', 'asset_uuid', NULL, NULL, NULL, NULL, NULL),
('4', 'ledger_uuid_4', '1600.00', '0.00', 'FST_RECIEVABLE_ASSET', '20000', '1', 'SND_RECIEVABLE_LOAN_GUARANTEE', '20000.02', NULL, NULL, '1', 'customerUuid1', NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '616', NULL, '2016-08-28 00:00:00', NULL, 'ledger_book_no', '1', '1', 'test_single_no2', 'asset_uuid', NULL, NULL, NULL, NULL, NULL),
('5', 'ledger_uuid_5', '200.00', '0.00', 'FST_UNEARNED_LOAN_ASSET', '10000', '1', 'SND_UNEARNED_LOAN_ASSET_TECH_FEE', '10000.04', NULL, NULL, '1', 'customerUuid1', NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '616', NULL, '2016-08-28 00:00:00', NULL, 'ledger_book_no', '1', '1', 'test_single_no', 'asset_uuid', NULL, NULL, NULL, NULL, NULL),


('6', 'ledger_uuid_6', '500.00', '0.00', 'FST_UNEARNED_LOAN_ASSET', '10000', '1', 'SND_UNEARNED_LOAN_ASSET_TECH_FEE', '10000.04', NULL, NULL, '1', 'customerUuid1', NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '616', NULL, '2016-08-28 00:00:00', NULL, 'ledger_book_no', '1', '1', 'test_single_no', 'asset_uuid2', NULL, NULL, NULL, NULL, NULL),
('7', 'ledger_uuid_7', '470.00', '0.00', 'FST_RECIEVABLE_ASSET', '20000', '1', 'SND_RECIEVABLE_LOAN_GUARANTEE', '20000.02', NULL, NULL, '1', 'customerUuid1', NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '616', NULL, '2016-08-28 00:00:00', NULL, 'ledger_book_no', '1', '1', 'test_single_no2', 'asset_uuid2', NULL, NULL, NULL, NULL, NULL),
('8', '965a7760-3207-4681-b0e9-f5da496ce2cb', '300.00', '0.00', 'FST_REPURCHASE_ASSET', '120000', '1', 'SND_RECIEVABLE_REPURCHASE_ASSET_OTHER_FEE', '120000.04', NULL, NULL, 'companyuuid', 'customerUuid1', NULL, NULL, NULL, '1237de63-8f78-408e-89af-8c1d37b8c8b9', NULL, '2017-05-02 16:40:36', '', NULL, '1', 'contract_uuid', NULL, '', 'ledger_book_no', '1', '1', NULL, NULL, NULL, 'repurchase_doc_uuid', NULL, NULL, ''),
('9', '57367fbc-97d1-4666-927b-fcbaecfbdad4', '200.00', '0.00', 'FST_REPURCHASE_ASSET', '120000', '1', 'SND_RECIEVABLE_REPURCHASE_ASSET_PENALTY', '120000.03', NULL, NULL, 'companyuuid', 'customerUuid1', NULL, NULL, NULL, '1237de63-8f78-408e-89af-8c1d37b8c8b9', NULL, '2017-05-02 16:40:36', '', NULL, '1', 'contract_uuid', NULL, '', 'ledger_book_no', '1', '1', NULL, NULL, NULL, 'repurchase_doc_uuid', NULL, NULL, ''),
('10', '12ecbdfd-92fd-4535-927a-3a9f30e7f104', '100.00', '0.00', 'FST_REPURCHASE_ASSET', '120000', '1', 'SND_RECIEVABLE_REPURCHASE_ASSET_INTEREST', '120000.02', NULL, NULL, 'companyuuid', 'customerUuid1', NULL, NULL, NULL, '1237de63-8f78-408e-89af-8c1d37b8c8b9', NULL, '2017-05-02 16:40:36', '', NULL, '1', 'contract_uuid', NULL, '', 'ledger_book_no', '1', '1', NULL, NULL, NULL, 'repurchase_doc_uuid', NULL, NULL, ''),
('11', 'ada1986f-2034-4bd8-8196-a163589aa7dc', '2100.00', '0.00', 'FST_REPURCHASE_ASSET', '120000', '1', 'SND_RECIEVABLE_REPURCHASE_ASSET_PRINCIPLE', '120000.01', NULL, NULL, 'companyuuid', 'customerUuid1', NULL, NULL, NULL, '1237de63-8f78-408e-89af-8c1d37b8c8b9', NULL, '2017-05-02 16:40:36', '', NULL, '1', 'contract_uuid', NULL, '', 'ledger_book_no', '1', '1', NULL, NULL, NULL, 'repurchase_doc_uuid', NULL, NULL, '');


SET FOREIGN_KEY_CHECKS = 1;
