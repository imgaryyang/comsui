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
  	delete from `asset_set_extra_charge`;
  	delete from `dictionary`;
  	delete from `t_deduct_plan`;

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
	VALUES ('1', 'cash_flow_uuid_1', '0', '', 'd0503298-e890-425a-44sdf', '600000000001', '云南国际信托有限公司', 'account_account_no', 'account_account_name', '', '','1', '2016-10-13 10:07:13', '1000.00', '6000.00', '', '207e3cd6-7ff0-11e6-a06e-12313', '1万条测试', '', NULL, '', '0.0', '0');

	INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`,`customer_uuid`,`customer_type`) VALUES
	(1, NULL, NULL, NULL, NULL, 1,'customerUuid1',0),
	(2, NULL, NULL, NULL, NULL, 1,'company_customer_uuid_1',1);
	/*
	 * outlierDocumentUuid:外部凭据号
	 * outlier_counter_party_account:外部凭据发生账号
	 */
	INSERT INTO `source_document` (`id`,`company_id`, `source_document_uuid`, `source_document_type`, `create_time`, `source_document_status`, `source_account_side`, `booking_amount`, `outlier_document_uuid`, `outlier_trade_time`, `outlier_counter_party_account`, `outlier_counter_party_name`, `outlier_account`, `outlier_company_id`, `outlier_serial_global_identity`, `outlier_memo`, `outlier_amount`, `outlier_settlement_modes`, `outlier_breif`, `outlier_account_side`, `appendix`, `first_outlier_doc_type`, `second_outlier_doc_type`, `third_outlier_doc_type`, `currency_type`, `audit_status`, `first_party_id`, `second_party_id`, `virtual_account_uuid`, `first_account_id`, `second_account_id`,`excute_status`,`excute_result`,`financial_contract_uuid`) VALUES 
	('1','1', 'source_document_uuid_1', '1', '2016-08-24 18:58:23', '0', '1', '500.00', 'deduct_application_uuid_1', '2016-08-24 16:56:18', 'account_account_no', 'account_account_name', 'yunxin_account_no', '1', 'cash_flow_no_1', NULL, '500', '3', '测试', '1', NULL, 'deduct_application', 'SND_LIABILITIES_INDEPENDENT_CUSTOMER_DEPOSIT', '', NULL, '0', 'company_customer_uuid_1', '1', '7d3aad51-05f1-4896-abff-caee93afca79', '10000', 'order_uuid_1',0 , 0, 'financial_contract_uuid_1');

	INSERT INTO `source_document_detail` (`id`, `uuid`, `source_document_uuid`, `contract_unique_id`, `repayment_plan_no`, `amount`, `status`, `first_type`, `first_no`, `second_type`, `second_no`, `payer`) VALUES 
	('1', 'source_document_detail_uuid_1', 'source_document_uuid_1', 'contract_unique_id_1','repayment_plan_no_1', '400.00', '0', 'BUSINESS_PAYMENT_VOUCHER', 'order_uuid_1', 'enum.voucher-type.pay', 'order_detail_uuid_1', '0'),
	('2', 'source_document_detail_uuid_2', 'source_document_uuid_1', 'contract_unique_id_1','repayment_plan_no_1', '100.00', '0', 'BUSINESS_PAYMENT_VOUCHER', 'order_uuid_1', 'enum.voucher-type.pay', 'order_detail_uuid_2', '0');

	INSERT INTO `t_deduct_application` (`id`, `deduct_application_uuid`, `deduct_id`, `request_no`, `financial_contract_uuid`, `financial_product_code`, `contract_unique_id`, `repayment_plan_code_list`, `contract_no`, `planned_deduct_total_amount`, `actual_deduct_total_amount`, `notify_url`, `transcation_type`, `repayment_type`, `execution_status`, `execution_remark`, `create_time`, `creator_name`, `ip`, `last_modified_time`, `record_status`, `is_available`, `api_called_time`, `transaction_recipient`,`complete_time`) VALUES 
	('1', 'deduct_application_uuid_1', 'deduct_id_1', 'request_no_1', 'financial_contract_uuid_1', 'G00003', 'contract_unique_id_1', '[\"ZC27438B14F806E86C\"]', 'contract_no_1', '500.00', '500.00', '', '1', '1', '2', '', '2016-05-24 17:30:42', 't_test_zfb', '115.197.179.183', '2016-05-03 17:30:42', '0', '0', '2016-05-04 00:00:00', '0','2017-06-15 00:00:00');

	INSERT INTO `t_deduct_application_detail` (`id`, `deduct_application_detail_uuid`, `deduct_application_uuid`, `financial_contract_uuid`, `contract_unique_id`, `repayment_plan_code`, `request_no`, `repayment_type`, `transaction_type`, `create_time`, `execution_remark`, `creator_name`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`, `is_total`,`asset_set_uuid`) VALUES 
	('1', 'deduct_application_detail_uuid_1', 'deduct_application_uuid_1', 'financial_contract_uuid_1', 'contract_unique_id_1', 'repayment_plan_no_1', '2445ef09-f220-4e52-abe2-ec69aa6fbcaf', '1', '1', '2016-05-24 17:21:00', '', 't_test_zfb', '2016-05-24 17:21:00', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, '400.00', '0','asset_uuid_1'),
	('2', 'deduct_application_detail_uuid_1', 'deduct_application_uuid_1', 'financial_contract_uuid_1', 'contract_unique_id_1', 'repayment_plan_no_1', '2445ef09-f220-4e52-abe2-ec69aa6fbcaf', '1', '1', '2016-05-24 17:21:00', '', 't_test_zfb', '2016-05-24 17:21:00', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_INTEREST', '10000.01', NULL, NULL, '100.00', '0','asset_uuid_1');

 	
	/*adva_repayment_term: 还款宽限期（日） 注：set此字段时，请同时 set贷款逾期开始日（loanOverdueStartDay）为 此字段值+1*/
	/*adva_repo_term: 逾期转坏账日 注：set此字段时，请同时 set贷款逾期结束日（loanOverdueEndDay）为 此字段值-1*/
	/*loan_overdue_start_day: 贷款逾期开始日 注：set此字段时，请同时 set还款宽限期（日）（advaRepaymentTerm）为 此字段值-1 */
	/*还款类型--资产包格式 0:等额本息，1:锯齿型，2:等额本金*/
	/*信托合约类型 0:消费贷,1:小微企业贷款*/
	INSERT INTO `financial_contract` (`adva_repayment_term`,`id`,`financial_contract_uuid`,`capital_account_id`, `adva_matuterm`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`,`loan_overdue_end_day`,`loan_overdue_start_day`,`payment_channel_id`,`ledger_book_no`,`asset_package_format`,`financial_contract_type`)
	VALUES  ('0','1', 'financial_contract_uuid_1',1, 3, 'financial_contract_code_1', '云南信托', 1, 1, 30,90,1,1,'yunxin_ledger_book',1,0);
	  INSERT INTO `contract` (`id`, `uuid`,`unique_id`,`begin_date`, `contract_no`,`end_date`,`app_id`, `customer_id`, `house_id`,`actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`,`active_version_no`,`financial_contract_uuid`)
	VALUES (1, 'contract_uuid_1','contract_unique_id_1','2015-10-19', 'contract_no_1', '2016-10-19', 1, 1, 1, NULL, '2015-10-19 13:34:35', 0.20, 1, 1, 12, 0, 3000, 0.284,1,'financial_contract_uuid_1');
	 

	
	INSERT INTO `virtual_account` (`id`, `total_balance`, `virtual_account_uuid`, `version`, `owner_uuid`, `owner_name`, `create_time`, `last_update_time`,`virtual_account_no`) VALUES 
('1', '10005', 'virtual_account_uuid', '7d3aad51-05f1-4896-abff-caee93afca79','customerUuid1', '', '2016-08-24 21:27:10', '2016-08-27 16:57:09','testaccount');

	INSERT INTO `contract_account` (`id`, `pay_ac_no`, `bankcard_type`, `contract_id`, `payer_name`, `bank`, `bind_id`, `id_card_num`, `bank_code`, `province`, `city`, `from_date`, `thru_date`,`contract_account_type`) VALUES 
('1', 'pay_ac_no_1', NULL, '1', '测试用户1', '中国邮政储蓄银行', NULL, 'ID_card1', '403', '安徽省', '亳州', '2016-04-17 00:00:00', '2900-01-01 00:00:00','0');

/*asset_fair_value:资产公允值*/
/*asset_initial_value:资产初始价值*/
/*asset_interest_value:本期资产利息*/
/*asset_principal_value:本期资产本金*/
/*single_loan_contract_no:单笔还款编号*/
INSERT INTO `asset_set` (`id`, `actual_recycle_date`, `asset_fair_value`, `asset_initial_value`, `asset_interest_value`, `asset_principal_value`, `asset_recycle_date`, `asset_status`, `asset_uuid`, `create_time`, `current_period`, `guarantee_status`, `last_modified_time`, `last_valuation_time`, `on_account_status`, `settlement_status`, `single_loan_contract_no`, `contract_id`,`version_no`,`active_status`,`financial_contract_uuid`,`plan_type`) VALUES
('1', NOW(), '500', '500', '100.00', '400.00', '2016-05-01', '0', 'asset_uuid_1', '2016-05-16 14:26:50', '1', '0', '2016-05-17 14:26:50', NULL, '1', '0', 'repayment_plan_no_1', '1',1,0,'financial_contract_uuid_1','0');


INSERT INTO `asset_set_extra_charge` (`id`, `asset_set_extra_charge_uuid`, `asset_set_uuid`, `create_time`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`) VALUES
(1, 'asset_set_extra_charge_uuid_1', 'asset_uuid_1', '2016-05-01 17:10:53', '2016-05-02 00:25:21', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, 400),
(2, 'asset_set_extra_charge_uuid_2', 'asset_uuid_1', '2016-05-01 17:10:53', '2016-05-02 00:25:21', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_INTEREST', '10000.01', NULL, NULL, 100);

/*first_repayment_way_group： 还款类型分组：0：线上代扣,1:商户代扣,2:(委托转付,商户代偿，差额划拨，回购)，3:（主动还款，他人贷款）4：表外支付'*//*5:线上代扣—快捷支付*/
/*orderAliveStatus: 订单生存周期:0,创建，1,作废, 2,合并作废*/
/*order_check_status: 校验结果，0,未校验，1,校验成功，2,校验失败，3, 校验中*/
/*order_db_status: 订单落单状态 0：未落单，1：已落单*/
/*order_pay_status: 支付状态(关联金额状态)0:未支付1,支付中2.支付完成3.支付异常**/
/*order_recover_status: 核销结果:未核销 部分核销 全部核销*/
INSERT INTO `repayment_order` (`id`, `order_uuid`, `order_unique_id`, `mer_id`, `order_request_no`,`first_repayment_way_group`, `order_alive_status`, `order_check_status`, `order_db_status`, `order_pay_status`, `order_recover_result`, `order_recover_status`, `paid_amount`, `order_amount`, `order_create_time`, `order_last_modified_time`, `order_notify_url`, `ip`, `financial_contract_uuid`, `financial_contract_no`, `financial_contract_name`, `order_detail_file_path`, `active_order_uuid`, `remark`)
VALUES
	(10, 'order_uuid_1', 'order_unique_id_1', 'mer_id_1', 'e573be60-f16c-4918-9e8c-b220561b9282',5, 0, 1, 0, 2, 0, 0, 0.00, 500.00, '2017-06-12 17:40:39', '2017-01-12 17:40:39', '', '127.0.0.1', 'financial_contract_uuid_1', 'G31700', '拍拍贷测试', '/home/dell/Desktop/repaymentOrderDetails/repayment_order_1497260439015062c99f4-ee0c-46ee-af5c-019ed8635eea.csv', 'empty_order_uuid', '');
/*detail_alive_status: 订单详情生存周期:0:创建,1:作废*/
/*detail_pay_status: 核销状态:0: 未核销，1:已核销*/
/*repayment_way: 还款方式：0:线上代扣，1:委托转付，2:商户担保、3：回购，4:差额划拨、5：商户代偿，6:主动付款、7:他人代偿、8:商户代扣, 9 线上代扣—快捷支付*/
/*repayment_business_type: 还款类型分组：0：还款计划 1：回购单*/
INSERT INTO `repayment_order_item` (`id`, `order_detail_uuid`, `contract_unique_id`, `contract_no`, `contract_uuid`, `amount`, `detail_alive_status`, `detail_pay_status`, `mer_id`, `financial_contract_uuid`, `repayment_way`, `repayment_business_uuid`, `repayment_business_no`, `repayment_business_type`, `repayment_plan_time`, `order_uuid`, `order_unique_id`, `remark`)
VALUES
	(1, 'order_detail_uuid_1', 'contract_unique_id_1', 'contract_no_1', 'contract_uuid_1', 400.00, 0, 0, 'mer_id_1', 'financial_contract_uuid_1', 9, 'asset_uuid_1', 'repayment_plan_no_1', 0, '2017-06-15 10:10:10', 'order_uuid_1', 'oder_unique_id_1', 'remark_1'),
	(2, 'order_detail_uuid_2', 'contract_unique_id_1', 'contract_no_1', 'contract_uuid_1', 100.00, 0, 0, 'mer_id_1', 'financial_contract_uuid_1', 9, 'asset_uuid_1', 'repayment_plan_no_1', 0, '2017-06-15 10:10:10', 'order_uuid_1', 'oder_unique_id_1', 'remark_1');
INSERT INTO `repayment_order_item_charge` (`id`, `uuid`, `repayment_order_item_uuid`, `contract_uuid`, `repayment_business_no`, `repayment_business_uuid`, `order_uuid`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`, `create_time`, `last_modified_time`, `string_field_1`, `string_field_2`, `string_field_3`, `date_field_1`, `date_field_2`, `date_field_3`, `decimal_field_1`, `decimal_field_2`, `decimal_field_3`) VALUES
	(2, 'order_item_charge_uuid_2', 'order_detail_uuid_1', 'contract_uuid_1' , 'repayment_plan_no_1', 'asset_uuid_1', 'order_uuid_1', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, 400.00, '2017-06-18 10:10:10', '2017-06-18 10:10:10',NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
 	(3, 'order_item_charge_uuid_3', 'order_detail_uuid_2', 'contract_uuid_1' , 'repayment_plan_no_1', 'asset_uuid_1', 'order_uuid_1', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_INTEREST', '10000.01', NULL, NULL, 100.00, '2017-06-18 10:10:10', '2017-06-18 10:10:10',NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
 
INSERT INTO `repayment_plan_extra_data` (`id`, `uuid`, `asset_set_uuid`, `financial_contract_uuid`, `customer_uuid`, `contract_uuid`, `reason_code`, `comment`, `create_time`, `last_modify_time`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`, `order_item_count`, `order_total_amount`, `order_paying_amount`) VALUES 
('2', 'e7932bd1-413e-46fd-87c9-0c706c8a3fd3', 'asset_uuid_1', 'financial_contract_uuid_1', '4f5ae6a5-9456-428d-a500-f7d1b539adb3', 'contract_uuid_1', '3', NULL, '2017-05-15 21:18:39', '2017-05-15 21:18:39', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '500.00', '0.00');

INSERT INTO `ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`) VALUES
('2', 'ledger_uuid_1', '0.00', '10005.00', 'FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS', '50000', '0', 'SND_LIABILITIES_INDEPENDENT_CUSTOMER_DEPOSIT', '50000.01', NULL, NULL, 'company_customer_uuid_1', '', NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '1', 'contract_uuid_1', '2016-08-28 00:00:00', NULL, 'yunxin_ledger_book', '1', '1', 'test_single_no', NULL, NULL, NULL, NULL, NULL, ''),

('8', 'ledger_uuid_2', '400.00', '0.00', 'FST_RECIEVABLE_ASSET', '20000', '1', 'SND_RECIEVABLE_OVERDUE_LOAN_ASSET', '20000.05', 'TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_PRINCIPLE', '20000.05.01', '1', 'customerUuid1', NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '1', 'contract_uuid_1', '2016-08-28 00:00:00', NULL, 'yunxin_ledger_book', '1', '1', 'test_single_no', 'asset_uuid_1', NULL, NULL, NULL, NULL, NULL),
('9', 'ledger_uuid_3', '100.00', '0.00', 'FST_RECIEVABLE_ASSET', '20000', '1', 'SND_RECIEVABLE_OVERDUE_LOAN_ASSET', '20000.05', 'TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_INTEREST', '20000.05.02', '1', 'customerUuid1', NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '1', 'contract_uuid_1', '2016-08-28 00:00:00', NULL, 'yunxin_ledger_book', '1', '1', 'test_single_no', 'asset_uuid_1', NULL, NULL, NULL, NULL, NULL);


--('2', 'ledger_uuid_2', '400.00', '0.00', 'FST_UNEARNED_LOAN_ASSET', '10000', '1', 'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL , '1', 'customerUuid1', NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '1', 'contract_uuid_1', '2016-08-28 00:00:00', NULL, 'yunxin_ledger_book', '1', '1', 'test_single_no', 'asset_uuid_1', NULL, NULL, NULL, NULL, NULL),
--('3', 'ledger_uuid_3', '100.00', '0.00', 'FST_UNEARNED_LOAN_ASSET', '10000', '1', 'SND_UNEARNED_LOAN_ASSET_INTEREST', '10000.01', NULL , NULL , '1', 'customerUuid1', NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd1', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f0', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '1', 'contract_uuid_1', '2016-08-28 00:00:00', NULL, 'yunxin_ledger_book', '1', '1', 'test_single_no', 'asset_uuid_1', NULL, NULL, NULL, NULL, NULL);

INSERT INTO `journal_voucher` (`id`, `account_side`, `billing_plan_uuid`,`trade_time`, `booking_amount`, `checking_level`, `company_id`,  `journal_voucher_uuid`, `source_document_amount`, `source_document_identity`, `source_document_uuid`, `status`, `source_document_counter_party_account`, `source_document_counter_party_name`, `issued_time`, `journal_voucher_type`, `counter_account_type`, `related_bill_contract_info_lv_1`, `related_bill_contract_info_lv_2`, `related_bill_contract_info_lv_3`, `cash_flow_account_info`, `journal_voucher_no`, `related_bill_contract_no_lv_1`, `related_bill_contract_no_lv_2`, `related_bill_contract_no_lv_3`, `related_bill_contract_no_lv_4`, `source_document_no`, `appendix`, `last_modified_time`) VALUES 
('1', '1', 'asset_uuid_1','2016-08-04 00:00:00', '400.00', '0', '1', 'journal_voucher_uuid_1', '400.00','source_document_uuid_1', 'source_document_detail_uuid_1', '0','','', '2016-10-20 16:57:12', '7', '1', 'financial_contract_uuid_1', 'contract_uuid_', 'asset_uuid_1', '', 'ZF274FC9E08F5A715B', '云南信托', 'contract_no_1', 'repayment_plan_no_1', 'order_no_1', 'source_document_no', NULL, '2016-10-20 16:57:12');
INSERT INTO `journal_voucher` (`id`, `account_side`, `billing_plan_uuid`,`trade_time`, `booking_amount`, `checking_level`, `company_id`,  `journal_voucher_uuid`, `source_document_amount`, `source_document_identity`, `source_document_uuid`, `status`, `source_document_counter_party_account`, `source_document_counter_party_name`, `issued_time`, `journal_voucher_type`, `counter_account_type`, `related_bill_contract_info_lv_1`, `related_bill_contract_info_lv_2`, `related_bill_contract_info_lv_3`, `cash_flow_account_info`, `journal_voucher_no`, `related_bill_contract_no_lv_1`, `related_bill_contract_no_lv_2`, `related_bill_contract_no_lv_3`, `related_bill_contract_no_lv_4`, `source_document_no`, `appendix`, `last_modified_time`) VALUES 
('2', '1', 'asset_uuid_1','2016-08-04 00:00:00', '100.00', '0', '1', 'journal_voucher_uuid_2', '100.00','source_document_uuid_1', 'source_document_detail_uuid_2', '0','','', '2016-10-20 16:57:12', '7', '1', 'financial_contract_uuid_1', 'contract_uuid_', 'asset_uuid_1', '', 'ZF274FC9E08F5A715B', '云南信托', 'contract_no_1', 'repayment_plan_no_1', 'order_no_1', 'source_document_no', NULL, '2016-10-20 16:57:12');
INSERT INTO `t_deduct_plan` (`id`,`deduct_plan_uuid`, `deduct_application_uuid`, `deduct_application_detail_uuid`, `financial_contract_uuid`, `contract_unique_id`, `contract_no`, `payment_gateway`, `payment_channel_uuid`, `pg_account`, `pg_clearing_account`, `transaction_type`, `transaction_remark`, `cp_bank_code`, `cp_bank_card_no`, `cp_bank_account_holder`, `cp_id_type`, `cp_id_number`, `cp_bank_province`, `cp_bank_city`, `cp_bank_name`, `planned_payment_date`, `complete_payment_date`, `planned_total_amount`, `actual_total_amount`, `execution_precond`, `execution_status`, `execution_remark`, `create_time`, `creator_name`, `last_modified_time`, `transaction_serial_no`, `mobile`, `transaction_recipient`, `trade_uuid`, `repayment_type`, `source_type`, `complete_time`, `transaction_time`, `batch_deduct_application_uuid`, `batch_deduct_id`, `none_business_check_status`, `business_check_status`, `notify_status`, `retriable`, `retry_times`, `payment_order_uuid`, `version`, `clearing_status`, `clearing_cash_flow_uuid`, `clearing_time`, `trade_schedule_slot_uuid`, `transaction_uuid`, `deduct_cash_identity`)
VALUES
	('1','deduct_plan_uuid_1', 'deduct_application_uuid_1', NULL, 'financial_contract_uuid_1', NULL, NULL, 7, 'ae9e5cdd-4566-4c85-8d97-f34756f8ee88', '002762', '001', 1, NULL, 'C10105', '6222020200002432', 'WUBO', 0, '320301198502169142', '110000', '110100', '中国建设银行 ', NULL, '2017-09-20 10:10:10', 1000.00, 1000.00, NULL, 2, '成功', '2017-09-20 18:14:04', 't_test_zfb', '2017-09-20 18:16:00', NULL, '', 0, '107944268275044353', 0, 1, '2017-09-20 18:16:00', NULL, '', '', 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, '107944268275044353', '309207914fedc462a048a6ec183d88aa', 'deduct_cash_identity_1');

	SET FOREIGN_KEY_CHECKS=1;