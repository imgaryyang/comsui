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

INSERT INTO `app_account` (`id`, `uuid`, `bank_name`, `account_name`, `account_no`, `app_account_active_status`, `app_id`) VALUES 
('1', 'uuid_3', '银行', 'ppd_user', '10003', '0', '1');


INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`,`uuid`) 
VALUES 
('1', '上海', '云南国际信托有限公司', '云南信托','companyuuid');

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`,`customer_uuid`,`customer_type`) VALUES 
(1, NULL, NULL, NULL, NULL, 1,'customerUuid1',0);


INSERT INTO `financial_contract` (`adva_repayment_term`,`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`) VALUES 
('0','1', '1', '10', NULL, 'test20160706', 'Test', '1', '1', '10', NULL, '104', '1', '10', '5', NULL, '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', 'bbe8ed7a-a52e-4a82-bdf9-54581170d044', '0', '0', '0', '0', '0', '0');



INSERT INTO `payment_channel` (`id`, `channel_name`, `user_name`, `user_password`, `merchant_id`, `cer_file_path`, `pfx_file_path`, `pfx_file_key`, `payment_channel_type`, `api_url`, `from_date`, `thru_date`) 
 VALUES 
 ('1', '广东银联', 'user1', '123456', '123', NULL, NULL, NULL, '0', NULL, NULL, NULL);

INSERT INTO `ledger_book` (`id`, `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`) VALUES 
('39', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', NULL, NULL);

INSERT INTO `virtual_account` (`id`, `total_balance`, `virtual_account_uuid`, `parent_account_uuid`, `virtual_account_alias`, `virtual_account_no`, `version`, `owner_uuid`, `owner_name`, `fst_level_contract_uuid`, `snd_level_contract_uuid`, `trd_level_contract_uuid`, `create_time`, `last_update_time`, `customer_type`) VALUES 
('1', '10.00', '546d9b49-cfd3-4ba0-9867-1878bd367187', NULL, NULL, '6217001210031616480', '48ea0da5-0c4f-4e2e-aad0-35b1e2efabfa', 'customerUuid1', '测试用户24', '930f1d3d-8158-420e-89bd-6f3922395eae', 'a0b1ab91-5fa8-11e6-b2c2-00163e002839', '', '2016-10-18 14:17:32', '2016-10-18 14:17:32', '0');


INSERT INTO `ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`) VALUES 
('1', 'ledger_uuid_1', '0.00', '900.00', 'FST_LONGTERM_LIABILITY', '40000', '0', 'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL, '1', NULL, NULL, NULL, NULL, '871562dd-5a0f-4a8d-990f-07d75bfc0304', '2016-05-01', '2016-05-16 14:26:50', '', NULL, '7', NULL, '2016-05-27 00:00:00', '', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', '1', 'repayment_plan_no_1', NULL, NULL, NULL, NULL, NULL, ''),
('2', 'ledger_uuid_2', '0.00', '10.00', 'FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS', '50000', '0', 'SND_LIABILITIES_INDEPENDENT_CUSTOMER_DEPOSIT', '50000.01', NULL, NULL, 'customerUuid1', '1', NULL, NULL, NULL, '51b062d9-beff-4688-a284-1b6c34ef3d06', NULL, '2016-08-27 16:57:09', '90f15a7d-78f9-4b42-985d-ea9f47ece17d', NULL, NULL, NULL, NULL, 'c2d731d8-fdfc-4ba1-8e70-4706eaa75d96', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', '1', NULL, NULL, NULL, NULL, NULL, NULL, '2bc312f7-c0a9-460c-80aa-052fa4ee7e99'),
('3', 'ledger_uuid_3', '1000.00', '0.00', 'FST_RECIEVABLE_ASSET', '20000', '1', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', NULL, NULL, '1', 'customerUuid1', NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '616', NULL, '2016-08-28 00:00:00', NULL, '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', '1', 'test_single_no', 'asset_uuid_1', NULL, NULL, NULL, NULL, NULL),
('4', 'ledger_uuid_4', '0.00', '0.00', 'FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS', '50000', '0', 'SND_LIABILITIES_INDEPENDENT_CUSTOMER_DEPOSIT', '50000.01', NULL, NULL, 'customerUuid1', '1', NULL, NULL, NULL, '51b062d9-beff-4688-a284-1b6c34ef3d06', NULL, '2016-08-27 16:57:09', '90f15a7d-78f9-4b42-985d-ea9f47ece17d', NULL, NULL, NULL, NULL, 'c2d731d8-fdfc-4ba1-8e70-4706eaa75d96', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', '1', NULL, NULL, NULL, NULL, NULL, NULL, '2bc312f7-c0a9-460c-80aa-052fa4ee7e99'),
('5', 'ledger_uuid_5', '500.00', '0.00', 'FST_RECIEVABLE_ASSET', '20000', '1', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', NULL, NULL, '1', 'customerUuid1', NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '616', NULL, '2016-08-28 00:00:00', NULL, '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', '1', 'test_single_no2', NULL, NULL, NULL, NULL, NULL, NULL),
('6', 'ledger_uuid_6', '1000.00', '0.00', 'FST_RECIEVABLE_ASSET', '20000', '1', 'SND_RECIEVABLE_LOAN_GUARANTEE', '20000.02', NULL, NULL, '1', 'customerUuid1', NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '616', NULL, '2016-08-28 00:00:00', NULL, '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', '1', 'test_single_no2', NULL, NULL, NULL, NULL, NULL, NULL);


INSERT INTO `account` (`id`, `account_name`, `account_no`, `bank_name`, `alias`, `attr`, `scan_cash_flow_switch`, `usb_key_configured`, `uuid`, `bank_code`) VALUES 
('1', '云南信托国际有限公司', '600000000001', '云南国际信托有限公司', NULL, NULL, '\0', '\0', '9d2c6b35-9e0a-447f-bd76-42a8634f8c3e', NULL);


SET FOREIGN_KEY_CHECKS=1;