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
	delete from `contract_account`;
	delete from `business_task_message`;

	delete from `repayment_order`;
	delete from `payment_order`;
  delete from `repayment_order_item`;
  delete from `repayment_order_item_charge`;
  delete from `repayment_order_item_check_fail_log`;
  delete from `repayment_plan_extra_data`;
  delete from `dictionary`;

  		INSERT INTO `dictionary` VALUES ('16', 'recievable_loan_carry_over_piority', 'PIORITY_FILLER');
		INSERT INTO `account` (`id`, `account_name`, `account_no`, `bank_name`, `alias`, `attr`, `scan_cash_flow_switch`, `usb_key_configured`, `uuid`, `bank_code`) 
	VALUES ('1', '云南信托国际有限公司', '600000000001', '云南国际信托有限公司', NULL, NULL, '\0', '\0', '9d2c6b35-9e0a-447f-bd76-42a8634f8c3e', NULL);
		INSERT INTO `app_account` (`id`, `uuid`, `bank_name`, `account_name`, `account_no`, `app_account_active_status`, `app_id`) 
	VALUES ('1', 'uuid_3', '银行', 'ppd_user', '10003', '0', '1');
		INSERT INTO `payment_channel` (`id`, `channel_name`, `user_name`, `user_password`, `merchant_id`, `cer_file_path`, `pfx_file_path`, `pfx_file_key`, `payment_channel_type`, `api_url`, `from_date`, `thru_date`) 
	VALUES ('1', '广东银联', 'user1', '123456', '123', NULL, NULL, NULL, '0', NULL, NULL, NULL);
		INSERT INTO `ledger_book` (`id`, `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`)
	VALUES ('1', 'yunxin_ledger_book', '1', '1', '');
		INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee` )
	VALUES ('1', 'anmeitu', '11111db75ebb24fa0993f4fa25775023', '\0', 'http://e.zufangbao.cn', '安美途', '1', NULL);
		INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`,`uuid`) 
	VALUES ('1', '上海', '云南国际信托有限公司', '云南信托','companyuuid');
		INSERT INTO `cash_flow` (`id`, `cash_flow_uuid`, `cash_flow_channel_type`, `company_uuid`, `host_account_uuid`, `host_account_no`, `host_account_name`, `counter_account_no`, `counter_account_name`, `counter_account_appendix`, `counter_bank_info`, `account_side`, `transaction_time`, `transaction_amount`, `balance`, `transaction_voucher_no`, `bank_sequence_no`, `remark`, `other_remark`, `strike_balance_status`, `trade_uuid`, `issued_amount`, `audit_status`) 
	VALUES ('1', 'cash_flow_uuid_1', '0', 'companyuuid', 'd0503298-e890-425a-44sdf', '600000000001', '云南国际信托有限公司', '10003', 'account_account_no', 'account_account_name', '', '1', '2016-10-13 10:07:13', '1000.00', '6000.00', '', '207e3cd6-7ff0-11e6-a06e-12313', '1万条测试', '', NULL, '', '0.0', '0');

	INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`,`customer_uuid`,`customer_type`) VALUES 
	(1, NULL, NULL, NULL, NULL, 1,'customerUuid1',0),
	(2, NULL, NULL, NULL, NULL, 1,'company_customer_uuid_1',1);

	INSERT INTO `financial_contract` (`adva_repayment_term`,`id`,`financial_contract_uuid`,`capital_account_id`, `adva_matuterm`, `contract_no`, `contract_name`, `app_id`, `company_id`,   `adva_repo_term`,`loan_overdue_end_day`,`loan_overdue_start_day`,`payment_channel_id`,`ledger_book_no`,`asset_package_format`,`financial_contract_type`)
	VALUES ('0','1', 'financial_contract_uuid_1',1, 3, 'financial_contract_code_1', '云南信托', 1, 1, 30,90,1,1,'yunxin_ledger_book',1,0);
		INSERT INTO `contract` (`id`, `uuid`,`unique_id`,`begin_date`, `contract_no`,`end_date`,`app_id`, `customer_id`, `house_id`,`actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`,`active_version_no`,`financial_contract_uuid`)
	VALUES (1, 'contract_uuid_1','contract_unique_id_1','2015-10-19', 'DKHD-001', '2016-10-19', 1, 1, 1, NULL, '2015-10-19 13:34:35', 0.001, 1, 1, 12, 0, 1000, 0.0001,1,'financial_contract_uuid_1');


	
INSERT INTO `virtual_account` (`id`, `total_balance`, `virtual_account_uuid`, `version`, `owner_uuid`, `owner_name`, `create_time`, `last_update_time`,`virtual_account_no`) VALUES 
('1', '10005', 'virtual_account_uuid', '7d3aad51-05f1-4896-abff-caee93afca79','company_customer_uuid_1', '', '2016-08-24 21:27:10', '2016-08-27 16:57:09','testaccount');

	
	
	
INSERT INTO `asset_set` (`id`, `actual_recycle_date`, `asset_fair_value`, `asset_initial_value`, `asset_interest_value`, `asset_principal_value`, `asset_recycle_date`, `asset_status`, `asset_uuid`, `create_time`, `current_period`, `guarantee_status`, `last_modified_time`, `last_valuation_time`, `on_account_status`, `settlement_status`, `single_loan_contract_no`, `contract_id`,`version_no`,`active_status`,`financial_contract_uuid`,`plan_type`,`version_lock`) VALUES
('1', NOW(), '1500.00', '1500.00', '500.00', '1000.00', '2016-05-01', '0', 'asset_uuid_1', '2016-05-16 14:26:50', '1', '0', NULL, NULL, '1', '0', 'repayment_plan_no_1', '1',1,0,'financial_contract_uuid_1','0','11111');

	
	INSERT INTO `contract_account` (`id`, `pay_ac_no`, `bankcard_type`, `contract_id`, `payer_name`, `bank`, `bind_id`, `id_card_num`, `bank_code`, `province`, `city`, `from_date`, `thru_date`,`contract_account_type`) VALUES 
('1', 'pay_ac_no_1', NULL, '1', '测试用户1', '中国邮政储蓄银行', NULL, 'ID_card1', '403', '安徽省', '亳州', '2016-04-17 00:00:00', '2900-01-01 00:00:00','0');



INSERT INTO `repayment_order` (`id`, `order_uuid`, `order_unique_id`, `mer_id`, `order_request_no`,`first_repayment_way_group`, `order_alive_status`, `order_check_status`, `order_db_status`, `order_pay_status`, `order_recover_result`, `order_recover_status`, `paid_amount`, `order_amount`, `order_create_time`, `order_last_modified_time`, `order_notify_url`, `ip`, `financial_contract_uuid`, `financial_contract_no`, `financial_contract_name`, `order_detail_file_path`, `active_order_uuid`, `remark`, `cash_flow_time`)
VALUES
	(10, 'order_uuid_1', 'order_unique_id_1', 'mer_id_1', 'e573be60-f16c-4918-9e8c-b220561b9282',2, 0, 0, 0, 0, 0, 0, 0.00, 6000.00, '2017-06-12 17:40:39', '2017-06-12 17:40:39', '', '127.0.0.1', 'financial_contract_uuid_1', 'G31700', '拍拍贷测试', '/home/dell/Desktop/repaymentOrderDetails/repayment_order_1497260439015062c99f4-ee0c-46ee-af5c-019ed8635eea.csv', 'empty_order_uuid', '', '2016-10-13 10:07:13');

-- repayment way 6为主动付款
INSERT INTO `repayment_order_item` (`id`, `order_detail_uuid`, `contract_unique_id`, `contract_no`, `contract_uuid`, `amount`, `detail_alive_status`, `detail_pay_status`, `mer_id`, `financial_contract_uuid`,  `repayment_way`, `repayment_business_uuid`, `repayment_business_no`, `repayment_business_type`, `repayment_plan_time`, `order_uuid`, `order_unique_id`, `remark`)
VALUES
	(1, 'order_detail_uuid_1', 'contract_unique_id_1', 'contract_no_1', 'contract_uuid_1', 1000.00, 0, 0, 'mer_id_1', 'financial_contract_uuid_1', 1, 'asset_uuid_1', 'repayment_plan_no_1', 0, '2017-06-15 10:10:10', 'order_uuid_1', 'oder_unique_id_1', 'remark_1'),
	(2, 'order_detail_uuid_2', 'contract_unique_id_1', 'contract_no_1', 'contract_uuid_1', 500.00, 0, 0, 'mer_id_1', 'financial_contract_uuid_1', 1, 'asset_uuid_1', 'repayment_plan_no_1', 0, '2017-06-15 10:10:10', 'order_uuid_1', 'oder_unique_id_1', 'remark_1');


INSERT INTO `repayment_plan_extra_data` (`id`, `uuid`, `asset_set_uuid`, `financial_contract_uuid`, `customer_uuid`, `contract_uuid`, `reason_code`, `comment`, `create_time`, `last_modify_time`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`, `order_item_count`, `order_total_amount`, `order_paying_amount`) VALUES 
('2', 'e7932bd1-413e-46fd-87c9-0c706c8a3fd3', 'asset_uuid_1', 'financial_contract_uuid_1', '4f5ae6a5-9456-428d-a500-f7d1b539adb3', 'contract_uuid_1', '3', NULL, '2017-05-15 21:18:39', '2017-05-15 21:18:39', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '1500.00', '0.00');




INSERT INTO `ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`) VALUES
('2', 'ledger_uuid_2', '0.00', '10005.00', 'FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS', '50000', '0', 'SND_LIABILITIES_INDEPENDENT_CUSTOMER_DEPOSIT', '50000.01', NULL, NULL, 'company_customer_uuid_1', '', NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '1', 'contract_uuid_1', '2016-08-28 00:00:00', NULL, 'yunxin_ledger_book', '1', '1', 'test_single_no', NULL, NULL, NULL, NULL, NULL, ''),

('8', '9bb36c8e-ca27-4a9d-a376-23eaf503b389', '1000.00', '0.00', 'FST_RECIEVABLE_ASSET', '20000', '1', 'SND_RECIEVABLE_OVERDUE_LOAN_ASSET', '20000.05', 'TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_PRINCIPLE', '20000.05.01', '1', 'customerUuid1', NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '1', 'contract_uuid_1', '2016-08-28 00:00:00', NULL, 'yunxin_ledger_book', '1', '1', 'test_single_no', 'asset_uuid_1', NULL, NULL, NULL, NULL, NULL),
('9', '62050685-3d58-430c-b979-b38b6fc970bb', '500.00', '0.00', 'FST_RECIEVABLE_ASSET', '20000', '1', 'SND_RECIEVABLE_OVERDUE_LOAN_ASSET', '20000.05', 'TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_INTEREST', '20000.05.02', '1', 'customerUuid1', NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '1', 'contract_uuid_1', '2016-08-28 00:00:00', NULL, 'yunxin_ledger_book', '1', '1', 'test_single_no', 'asset_uuid_1', NULL, NULL, NULL, NULL, NULL);



	SET FOREIGN_KEY_CHECKS=1;