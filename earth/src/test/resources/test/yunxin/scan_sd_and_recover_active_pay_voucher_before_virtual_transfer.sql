SET FOREIGN_KEY_CHECKS=0;

delete from `asset_set`;
delete from `financial_contract`;
delete from `contract`;
delete from `asset_valuation_detail`;
delete from `ledger_book_shelf`;
delete from `payment_channel`;
delete from `customer`;
delete from `app`;
delete from `company`;
delete from `ledger_book`;
delete from `asset_package`;
delete from `loan_batch`;
delete from `virtual_account`;
delete from `source_document_detail`;
delete from `source_document`;
delete from `journal_voucher`;
delete from `business_voucher`;
delete from `rent_order`;
delete from `virtual_account_payment_black_list`;
delete from `contract_account`;

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee` )
VALUES 
('1', 'anmeitu', '11111db75ebb24fa0993f4fa25775023', '\0', 'http://e.zufangbao.cn', '安美途', '1', NULL);

INSERT INTO `ledger_book` (`id`, `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`) VALUES 
('1', 'yunxin_ledger_book', '1', '1', '');

INSERT INTO `asset_package` (`id`, `contract_id`, `asset_package_no`, `financial_contract_id`, `loan_batch_id`) VALUES 
('1', '1', NULL, '1', '3'),
('2', '7', NULL, '1', '4');
INSERT INTO `loan_batch` (`id`, `is_available`, `code`, `create_time`, `financial_contract_id`, `last_modified_time`, `loan_date`) VALUES 
('3', 1, 'DCF-NFQ-LR903A 20160516 16:52:905', '2016-05-16 16:52:57', '1', NULL, NULL),
('4', 1, 'DCF-NFQ-LR903A 20160516 16:52:905', '2016-05-16 16:52:57', '1', NULL, NULL);


INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`) VALUES 
(1, '上海', '云南国际信托有限公司', '云南信托');

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`,`customer_uuid`,`customer_type`) VALUES 
(1, NULL, NULL, NULL, NULL, 1,'customerUuid1',0),
(2, NULL, NULL, NULL, NULL, 1,'company_customer_uuid_1',1);

INSERT INTO `financial_contract` (`adva_repayment_term`,`id`,`financial_contract_uuid`, `adva_matuterm`, `contract_no`, `contract_name`, `app_id`, `company_id`,   `adva_repo_term`,`loan_overdue_end_day`,`loan_overdue_start_day`,`payment_channel_id`,`ledger_book_no`,`asset_package_format`,`financial_contract_type`) VALUES 
('0',1, 'financial_contract_uuid_1', 3, 'DCF-NFQ-LR903A', '云南信托', 1, 1, 30,90,1,1,'yunxin_ledger_book',1,0);

INSERT INTO `payment_channel` (`id`, `channel_name`, `user_name`, `user_password`, `merchant_id`, `cer_file_path`, `pfx_file_path`, `pfx_file_key`, `payment_channel_type`, `api_url`, `from_date`, `thru_date`) 
 VALUES 
 ('1', '广东银联', 'user1', '123456', '123', NULL, NULL, NULL, '0', NULL, NULL, NULL);

 -- contract1不引用
INSERT INTO `contract` (`id`, `uuid`,`unique_id`,`begin_date`, `contract_no`,`end_date`,`app_id`, `customer_id`, `house_id`,`actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`,`active_version_no`,`financial_contract_uuid`) VALUES 
(1, 'contract_uuid_1','contract_unique_id_1','2015-10-19', 'DKHD-000', '2016-10-19', 1, 1, 1, NULL, '2015-10-19 13:34:35', 0.001, 1, 1, 12, 0, 1000, 0.0001,1,'financial_contract_uuid_1'),
(7, 'contract_uuid_2','contract_unique_id_2','2015-10-19', 'DKHD-001', '2016-10-19', 1, 1, 1, NULL, '2015-10-19 13:34:35', 0.001, 1, 1, 12, 0, 1000, 0.0001,1,'financial_contract_uuid_1');


INSERT INTO `asset_valuation_detail` (`id`, `amount`, `asset_value_date`, `created_date`, `subject`, `asset_set_id`) VALUES 
('1', '1000.00', '2016-03-14 19:46:38', '2016-03-14', '0', '1'),
('2', '5.00', '2016-03-15 19:47:16', '2016-03-15', '1', '1'),
('3', '15.00', '2016-03-16 19:47:16', '2016-03-16', '1', '1');


INSERT INTO `virtual_account` (`id`, `total_balance`, `virtual_account_uuid`, `version`, `owner_uuid`, `owner_name`, `create_time`, `last_update_time`) VALUES 
('1', '58035.20', '9126313e-f89d-4222-847c-4e36331cb787', '7d3aad51-05f1-4896-abff-caee93afca79','company_customer_uuid_1', 'aa', '2016-08-24 21:27:10', '2016-08-27 16:57:09'),
('2', '1050', 'virtual_account_uuid_2', '7d3aad51-05f1-4896-abff-caeesdsfwef','customerUuid1', 'aa', '2016-08-24 21:27:10', '2016-08-27 16:57:09');

INSERT INTO `ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`) VALUES 
('1', 'ledger_uuid_1', '0.00', '900.00', 'FST_LONGTERM_LIABILITY', '40000', '0', 'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL, '1', NULL, NULL, NULL, NULL, '871562dd-5a0f-4a8d-990f-07d75bfc0304', '2016-05-01', '2016-05-16 14:26:50', '', NULL, '7', NULL, '2016-05-27 00:00:00', '', 'yunxin_ledger_book', '1', '1', 'repayment_plan_no_1', 'd2c21631-f2fb-4e8f-beef-0a2e72447434', NULL, NULL, NULL, NULL, ''),
('2', 'ledger_uuid_2', '0.00', '10005.00', 'FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS', '50000', '0', 'SND_LIABILITIES_INDEPENDENT_CUSTOMER_DEPOSIT', '50000.01', NULL, NULL, 'customerUuid1', '1', NULL, NULL, NULL, '51b062d9-beff-4688-a284-1b6c34ef3d06', NULL, '2016-08-27 16:57:09', '90f15a7d-78f9-4b42-985d-ea9f47ece17d', NULL, NULL, NULL, NULL, 'c2d731d8-fdfc-4ba1-8e70-4706eaa75d96', 'yunxin_ledger_book', '1', '1', NULL, NULL, NULL, NULL, NULL, NULL, '2bc312f7-c0a9-460c-80aa-052fa4ee7e99'),
('3', 'ledger_uuid_3', '1000.00', '0.00', 'FST_RECIEVABLE_ASSET', '20000', '1', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', NULL, NULL, '1', 'customerUuid1', NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '616', NULL, '2016-08-28 00:00:00', NULL, 'yunxin_ledger_book', '1', '1', 'test_single_no', 'asset_uuid_1', NULL, NULL, NULL, NULL, NULL),
('4', 'ledger_uuid_4', '0.00', '10005.00', 'FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS', '50000', '0', 'SND_LIABILITIES_INDEPENDENT_CUSTOMER_DEPOSIT', '50000.01', NULL, NULL, 'customerUuid1', '1', NULL, NULL, NULL, '51b062d9-beff-4688-a284-1b6c34ef3d06', NULL, '2016-08-27 16:57:09', '90f15a7d-78f9-4b42-985d-ea9f47ece17d', NULL, NULL, NULL, NULL, 'c2d731d8-fdfc-4ba1-8e70-4706eaa75d96', 'yunxin_ledger_book', '1', '1', NULL, NULL, NULL, NULL, NULL, NULL, '2bc312f7-c0a9-460c-80aa-052fa4ee7e99'),
('5', 'ledger_uuid_5', '500.00', '0.00', 'FST_RECIEVABLE_ASSET', '20000', '1', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', NULL, NULL, '1', 'customerUuid1', NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '616', NULL, '2016-08-28 00:00:00', NULL, 'yunxin_ledger_book', '1', '1', 'test_single_no2', 'asset_uuid_2', NULL, NULL, NULL, NULL, NULL),
('6', 'ledger_uuid_6', '0.00', '1000', 'FST_RECIEVABLE_ASSET', '20000', '1', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', NULL, NULL, '1', 'customerUuid1', NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '616', NULL, '2016-08-28 00:00:00', NULL, 'yunxin_ledger_book', '1', '1', 'test_single_no', 'asset_uuid_1', NULL, NULL, NULL, NULL, NULL),
('7', 'ledger_uuid_7', '0.00', '500.00', 'FST_RECIEVABLE_ASSET', '20000', '1', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', NULL, NULL, '1', 'customerUuid1', NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '616', NULL, '2016-08-28 00:00:00', NULL, 'yunxin_ledger_book', '1', '1', 'test_single_no2', 'asset_uuid_2', NULL, NULL, NULL, NULL, NULL);


-- asset1 还1000, asset2还500
INSERT INTO `asset_set` (`id`, `actual_recycle_date`, `asset_fair_value`, `asset_initial_value`, `asset_interest_value`, `asset_principal_value`, `asset_recycle_date`, `asset_status`, `asset_uuid`, `create_time`, `current_period`, `guarantee_status`, `last_modified_time`, `last_valuation_time`, `on_account_status`, `settlement_status`, `single_loan_contract_no`, `contract_id`,`version_no`,`active_status`,`plan_type`) VALUES 
('1', NULL, '1000.00', '1000.00', '100.00', '900.00', '2016-05-01', '1', 'asset_uuid_1', '2016-05-16 14:26:50', '1', '0', NULL, NULL, '1', '0', 'repayment_plan_no_1', '7',1,0,'0'),
('2', NULL, '500.00', '500.00', '200.00', '800.00', '2016-05-05', '1', 'asset_uuid_2', '2016-05-16 14:26:50', '2', '0', NULL, NULL, '1', '0', 'repayment_plan_no_2', '7',1,0,'0');




INSERT INTO `source_document` (`id`,`company_id`, `source_document_uuid`, `source_document_type`, `create_time`, `source_document_status`, `source_account_side`, `booking_amount`, `outlier_document_uuid`, `outlier_trade_time`, `outlier_counter_party_account`, `outlier_counter_party_name`, `outlier_account`, `outlier_company_id`, `outlier_serial_global_identity`, `outlier_memo`, `outlier_amount`, `outlier_settlement_modes`, `outlier_breif`, `outlier_account_side`, `appendix`, `first_outlier_doc_type`, `second_outlier_doc_type`, `third_outlier_doc_type`, `currency_type`, `audit_status`, `first_party_id`, `second_party_id`, `virtual_account_uuid`, `first_account_id`, `second_account_id`,`excute_status`,`excute_result`,`financial_contract_uuid`,`source_document_no`) VALUES 
('1','1', 'source_document_uuid_1', '1', '2016-08-24 18:58:23', '0', '1', '1500.00', 'cash_flow_uuid_1', '2016-08-24 16:56:18', 'account_account_no', 'account_account_name', 'yunxin_account_no', '1', 'cash_flow_no_1', NULL, '2000', '3', '测试', '1', NULL, 'FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS', 'SND_LIABILITIES_INDEPENDENT_CUSTOMER_DEPOSIT', '', NULL, '0', 'compay_customerUuid2', '1', 'cfa28906-adb0-4b81-a6c2-d84fe4033947', '50000', '50000.01',0 , 0, 'financial_contract_uuid_1','source_document_no');

INSERT INTO `source_document_detail` (`id`, `uuid`, `source_document_uuid`, `contract_unique_id`, `repayment_plan_no`, `amount`, `status`, `first_type`, `first_no`, `second_type`, `second_no`, `payer`,`check_state`) VALUES 
('1', 'source_document_detail_uuid_1', 'source_document_uuid_1', 'contract_unique_id_2','repayment_plan_no_1', '1000.00', '0', 'enum.voucher-source.active_payment_voucher', 'bank_transaction_no_1', 'enum.voucher-type.active_pay', 'cash_flow_no_1', '0',2),
('2', 'source_document_detail_uuid_2', 'source_document_uuid_1', 'contract_unique_id_2','repayment_plan_no_2', '500.00', '0', 'enum.voucher-source.active_payment_voucher', 'bank_transaction_no_1', 'enum.voucher-type.active_pay', 'cash_flow_no_1', '0',2);




INSERT INTO `journal_voucher` (`id`, `account_side`, `billing_plan_uuid`, `booking_amount`, `business_voucher_type_uuid`, `business_voucher_uuid`, `checking_level`, `company_id`, `completeness`, `counter_party_account`, `counter_party_name`, `journal_voucher_uuid`, `source_document_amount`, `source_document_identity`, `source_document_uuid`, `status`, `trade_time`, `batch_uuid`, `created_date`, `source_document_counter_party_account`, `source_document_counter_party_name`, `issued_time`, `journal_voucher_type`, `counter_account_type`, `related_bill_contract_info_lv_1`, `related_bill_contract_info_lv_2`, `related_bill_contract_info_lv_3`, `cash_flow_account_info`, `journal_voucher_no`, `related_bill_contract_no_lv_1`, `related_bill_contract_no_lv_2`, `related_bill_contract_no_lv_3`, `related_bill_contract_no_lv_4`, `source_document_no`, `appendix`, `last_modified_time`) VALUES 
('1', '1', 'asset_uuid_1', '1000.00', '', '', '0', '1', NULL, NULL, NULL, '69682a72-2842-4d2e-b3dc-aad548c35f98', '1000.00','source_document_uuid_1', 'source_document_detail_uuid_1', '1', NULL, NULL, '2016-10-20 16:57:12', NULL, NULL, NULL, '3', '1', 'financial_contract_uuid_1', 'contract_uuid_2', 'asset_uuid_1', '', 'ZF274FC9E08F5A715B', '云南信托', 'DKHD-001', 'repayment_plan_no_1', 'order_no_1', 'source_document_no', NULL, '2016-10-20 16:57:12'),
('2', '1', 'asset_uuid_2', '500.00', '', '', '0', '1', NULL, NULL, NULL, '2e2a0219-a945-45e4-946a-5c7572d9f3cb','500.00', 'source_document_uuid_1', 'source_document_detail_uuid_2', '1', NULL, NULL, '2016-10-20 16:57:12', NULL, NULL, NULL, '3', '1', 'financial_contract_uuid_1', 'contract_uuid_2', 'asset_uuid_2', '', 'ZF274FC9E08FCF7A84', '云南信托', 'DKHD-001', 'repayment_plan_no_2', 'order_no_2', 'source_document_no', NULL, '2016-10-20 16:57:12');

INSERT INTO `rent_order` (`id`, `order_type`, `due_date`, `order_no`, `payout_time`, `total_rent`, `customer_id`, `user_upload_param`, `modify_time`, `financial_contract_id`, `repayment_bill_id`, `asset_set_id`, `create_time`, `clearing_status`, `executing_settling_status`, `comment`) VALUES 
('1', '0', '2016-10-20', 'order_no_1', '2016-10-20 18:39:44', '1000.00', '1', '', '2016-10-20 18:39:44', '1', 'e4d2c8ffca59408fab9bc7d99faab61d', '1', '2016-10-20 18:39:44', '1', '2', NULL),
('2', '0', '2016-10-20', 'order_no_2', '2016-10-20 18:39:44', '500.00', '1', '', '2016-10-20 18:39:44', '1', '453bbaafaf4646d99c6635218c1ebf07', '2', '2016-10-20 18:39:44', '1', '2', NULL);

INSERT INTO `contract_account` (`id`, `pay_ac_no`, `bankcard_type`, `contract_id`, `payer_name`, `bank`, `bind_id`, `id_card_num`, `bank_code`, `province`, `province_code`, `city`, `city_code`, `standard_bank_code`, `from_date`, `thru_date`) VALUES 
('1', '10001', NULL, '1', '测试7', '中国建设银行 ', NULL, '410402198801115658', NULL, '上海市', '310000', '上海市', '310100', 'C10105', '2016-08-27 16:06:53', '2900-01-01 00:00:00'),
('2', '10001', NULL, '7', '测试7', '中国建设银行 ', NULL, '410402198801115658', NULL, '上海市', '310000', '上海市', '310100', 'C10105', '2016-08-27 16:06:53', '2900-01-01 00:00:00');


SET FOREIGN_KEY_CHECKS=1;