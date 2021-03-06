SET FOREIGN_KEY_CHECKS=0;

delete from `app`;
delete from `customer`;
delete from `company`;
delete from `rent_order`;
delete from `asset_set`;
delete from `account`;
delete from `contract`;
delete from `asset_valuation_detail`;
delete from `contract_account`;
delete from `asset_package`;
delete from `transfer_application`;
delete from `batch_pay_record`;
delete from `source_document`;
delete from `journal_voucher`;
delete from `business_voucher`;
delete from `ledger_book`;
delete from `asset_package`;
delete from `loan_batch`;
delete from `ledger_book_shelf`;
delete from `financial_contract`;
delete from `payment_channel`;
delete from `rent_order`;
delete from `offline_bill`;

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

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`) 
VALUES 
('1', '上海', '云南国际信托有限公司', '云南信托');

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`,`customer_uuid`) VALUES 
(1, NULL, NULL, NULL, NULL, 1,'customerUuid1');

INSERT INTO `financial_contract` (`id`, `adva_matuterm`, `contract_no`, `contract_name`, `app_id`, `company_id`,   `adva_repo_term`,`loan_overdue_end_day`,`loan_overdue_start_day`,`payment_channel_id`,`ledger_book_no`,`asset_package_format`,`financial_contract_type`) VALUES 
('1', 3, 'DCF-NFQ-LR903A', '云南信托', 1, 1, 30,90,1,1,'yunxin_ledger_book',1,0);

INSERT INTO `payment_channel` (`id`, `channel_name`, `user_name`, `user_password`, `merchant_id`, `cer_file_path`, `pfx_file_path`, `pfx_file_key`, `payment_channel_type`, `api_url`, `from_date`, `thru_date`) 
 VALUES 
 ('1', '广东银联', 'user1', '123456', '123', NULL, NULL, NULL, '0', NULL, NULL, NULL);

INSERT INTO `contract` (`id`, `begin_date`, `contract_no`,`end_date`,`app_id`, `customer_id`, `house_id`,`actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`) VALUES 
(1, '2015-10-19', 'DKHD-001', '2016-10-19', 1, 1, 1, NULL, '2015-10-19 13:34:35', 0.001, 1, 1, 12, 0, 1000, 0.0001),
(7, '2015-10-19', 'DKHD-001', '2016-10-19', 1, 1, 1, NULL, '2015-10-19 13:34:35', 0.001, 1, 1, 12, 0, 1000, 0.0001);


INSERT INTO `ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`) VALUES 
('1', 'b3667a54-0f09-47b1-a777-73910f7ae8d0', '0.00', '1000.00', 'FST_FINANCIAL_ASSETS_SOLD_FOR_REPURCHASE', '80000', '0', NULL, NULL, NULL, NULL, '1', '1', NULL, NULL, NULL, '6fb7edd4-af52-43e8-8239-a70c480274ae', '2016-05-01', '2016-05-17 19:22:17', '', NULL, '7', NULL, '2016-05-01 00:00:00', '', 'yunxin_ledger_book', '1', '1', 'ZC27304880AB312E3A', 'asset_uuid_2', NULL, NULL, NULL, NULL, ''),
('2', '3010f376-f89d-4684-99e7-35d7411fa602', '1000.00', '0.00', 'FST_RECIEVABLE_ASSET', '20000', '1', 'SND_RECIEVABLE_LOAN_GUARANTEE', '20000.02', NULL, NULL, '1', '1', NULL, NULL, NULL, '6fb7edd4-af52-43e8-8239-a70c480274ae', '2016-05-01', '2016-05-17 19:22:17', '', NULL, '7', NULL, '2016-05-01 00:00:00', '', 'yunxin_ledger_book', '1', '1', 'ZC27304880AB312E3A', 'asset_uuid_2', NULL, NULL, NULL, NULL, ''),
('3', 'fc83fe1f-2797-42bb-ad4d-e7dd4c69701f', '0.00', '0.10', 'FST_REVENUE', '70000', '0', 'SND_REVENUE_INVESTMENT_ESTIMATE', '70000.04', NULL, NULL, '1', NULL, NULL, NULL, NULL, 'db1925e5-b5de-4d29-8cb5-6fae410d816a', '2016-05-02', '2016-05-17 19:22:17', '', NULL, '7', NULL, '2016-05-02 00:00:00', '', 'yunxin_ledger_book', '1', '1', 'ZC27304880AB312E3A', 'asset_uuid_2', NULL, NULL, NULL, NULL, ''),
('4', '4a802d33-8005-41cd-ad8c-fde52d188e16', '0.10', '0.00', 'FST_RECIEVABLE_ASSET', '20000', '1', 'SND_RECIEVABLE_LOAN_PENALTY', '20000.03', NULL, NULL, '1', NULL, NULL, NULL, NULL, 'db1925e5-b5de-4d29-8cb5-6fae410d816a', '2016-05-02', '2016-05-17 19:22:17', '', NULL, '7', NULL, '2016-05-02 00:00:00', '', 'yunxin_ledger_book', '1', '1', 'ZC27304880AB312E3A', 'asset_uuid_2', NULL, NULL, NULL, NULL, ''),
('5', 'b1bc325b-22d4-417d-ad4c-f71e2befb792', '1000.00', '0.00', 'FST_RECIEVABLE_ASSET', '20000', '1', 'SND_RECIEVABLE_OVERDUE_LOAN_ASSET', '20000.05', NULL, NULL, '1', NULL, NULL, NULL, 'ledger_uuid_7', NULL, '2016-05-01', '2016-05-17 22:26:48', '', NULL, '7', NULL, '2016-05-01 00:00:00', '', 'yunxin_ledger_book', '1', '1', 'ZC27304880AB312E3A', 'asset_uuid_2', NULL, NULL, NULL, NULL, ''),
('6', 'ledger_uuid_2', '0.00', '100.00', 'FST_DEFERRED_INCOME', '100000', '0', 'SND_DEFERRED_INCOME_INTEREST_ESTIMATE', '100000.01', NULL, NULL, '1', NULL, NULL, NULL, NULL, '871562dd-5a0f-4a8d-990f-07d75bfc0304', '2016-05-01', '2016-05-16 14:26:50', '', NULL, '7', NULL, '2016-05-27 00:00:00', '', 'yunxin_ledger_book', '1', '1', 'ZC27304880AB312E3A', 'asset_uuid_2', NULL, NULL, NULL, NULL, '');

INSERT INTO `asset_set` (`id`, `actual_recycle_date`, `asset_fair_value`, `asset_initial_value`, `asset_interest_value`, `asset_principal_value`, `asset_recycle_date`, `asset_status`, `asset_uuid`, `repayment_plan_type`, `create_time`, `current_period`, `guarantee_status`, `last_modified_time`, `last_valuation_time`, `on_account_status`, `settlement_status`, `single_loan_contract_no`, `contract_id`, `version_no`) VALUES 
('2', NULL, '1000.10', '1000.00', '100.00', '900.00', '2016-05-01', '0', 'asset_uuid_2', 0, '2016-05-16 14:26:50', '1', '0', NULL, NULL, '1', '0', 'ZC27304880AB312E3A', '7',1);

INSERT INTO `rent_order` (`id`, `due_date`, `order_no`, `payout_time`, `total_rent`, `customer_id`, `user_upload_param`, `modify_time`, `asset_set_id`, `create_time`,`clearing_status`,`executing_settling_status`,`financial_contract_id`,`repayment_bill_id`,`order_type`) VALUES
('2', '2015-03-18', 'DKHD-001-02-20160308', '2016-10-19 13:34:35',1000,  '1','', '2016-10-19',  2, '2015-10-19',0,0,1,'repayment_bill_id_2',1);

INSERT INTO `transfer_application` (`id`, `amount`,`creator_id`, `batch_pay_record_id`, `comment`, `create_time` , `deduct_status`, `executing_deduct_status`, `deduct_time`, `last_modified_time`,  `contract_account_id`,`order_id`) VALUES 
(2, '10', NULL, 2, NULL, '2015-10-13', 1, 2, NULL, NULL, 1,2);
-- batch_pay_record_1为到期资产, batch_pay_record_2为逾期资产
INSERT INTO `batch_pay_record` (`id`,`batch_pay_record_uuid`, `amount`,  `modify_time`,  `request_no`, `serial_no`, `request_data`, `response_data`, `query_response_data`, `trans_date_time`) VALUES 
('2','batch_pay_record_uuid_2', '1000.00',  NULL, 'test', 'serial_no_2', NULL, '', '', '20160111111100');

INSERT INTO `source_document` (`id`, `company_id`, `source_document_uuid`, `source_document_type`, `create_time`, `issued_time`, `source_document_status`, `source_account_side`, `booking_amount`, `outlier_document_uuid`, `outlier_trade_time`, `outlier_counter_party_account`, `outlier_counter_party_name`, `outlier_account`, `outlie_account_name`, `outlier_account_id`, `outlier_company_id`, `outlier_serial_global_identity`, `outlier_memo`, `outlier_amount`, `outlier_settlement_modes`, `outlier_breif`, `outlier_account_side`) VALUES 
('2', '1', 'source_document_uuid_2', '1', '2016-03-29 17:21:09', '2015-04-03 12:00:00', '0', '1', '0.00', 'offline_bill_uuid_1', '2015-03-30 16:11:50', 'pay_ac_no_1', 'payer_name_1', '1001300219000027827', NULL, NULL, '4', 'serial_no_1', '汇川路88号1号楼1803?', '2000.00', '3', '', '0');

INSERT INTO `offline_bill` (`id`, `amount`, `bank_show_name`, `comment`, `create_time`, `trade_time`, `is_delete`, `status_modified_time`, `last_modified_time`, `offline_bill_no`, `offline_bill_status`, `offline_bill_uuid`, `serial_no`, `payer_account_name`, `payer_account_no`)
VALUES
	(1, 2000, '中国银行滨江支行', '王二线下打款:29831.56 元', '2016-05-04 15:25:08', '2016-05-04 15:24:00', 0, '2016-05-04 15:25:08', '2016-05-04 15:25:08', 'XX272F8917169A7B1B', 1, 'offline_bill_uuid_1', '2016050415243801', '王二', '0109108221231233');


SET FOREIGN_KEY_CHECKS=1;
