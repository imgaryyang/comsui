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
  delete from `financial_contract_config`;
  delete from `payment_channel_information`;
  delete from `payment_channel`;
  delete from `t_deduct_application`;
  delete from `principal`;

  INSERT INTO `principal` (`id`, `authority`, `name`, `password`, `start_date`, `thru_date`, `t_user_id`, `created_time`, `creator_id`, `modify_password_time`)
 VALUES (1, 'ROLE_SUPER_USER', 'zhanghongbing', '376c43878878ac04e05946ec1dd7a55f', NULL, NULL, 2, NULL, NULL, 1);

  
INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`, `day_of_month`, `pay_for_go`, `repurchase_principal_algorithm`, `repurchase_interest_algorithm`, `repurchase_penalty_algorithm`, `repurchase_other_charges_algorithm`, `repayment_check_days`, `allow_freewheeling_repayment`, `repurchase_cycle`, `days_of_cycle`, `temporary_repurchases`)
VALUES
	(1, 1, 3, '2016-04-01 00:00:00', 'G08200', '测试合同', 1, 1, 91, '2016-07-01 00:00:00', 1, 0, 1, 90, 2, '74a9ce4d-cafc-407d-b013-987077541bdc', 'financial_contract_uuid_1', 1, 1, 1, 1, 0, 1, NULL, NULL, NULL, NULL, 1, 0, 0, 0, '(principal+interest)*0.05/100*overdueDay', 123.00, 23.00, NULL, NULL, '2017-03-16 22:56:48', 1, 0, 'outstandingPrincipal+outstandingInterest+outstandingPenaltyInterest', 30, 0, 'outstandingPrincipal', 'outstandingInterest', 'outstandingPenaltyInterest', NULL, -1, 0, NULL, NULL, NULL);

INSERT INTO `financial_contract_config` (`id`, `financial_contract_uuid`, `business_type`, `payment_channel_uuids_for_credit`, `payment_channel_uuids_for_debit`, `credit_payment_channel_mode`, `debit_payment_channel_mode`, `payment_channel_router_for_credit`, `payment_channel_router_for_debit`, `payment_channel_config_for_credit`, `payment_channel_config_for_debit`) VALUES 
('4', 'financial_contract_uuid_1', '0', 'f2591e61-735e-11e6-bf08-00163e002839,16ea0d08-735f-11e6-bf08-00163e002839', 'f2591e61-735e-11e6-bf08-00163e002839,16ea0d08-735f-11e6-bf08-00163e002839', '0', '0', 'f2591e61-735e-11e6-bf08-00163e002839', '16ea0d08-735f-11e6-bf08-00163e002839', NULL, NULL);


INSERT INTO `payment_channel_information` (`id`, `related_financial_contract_uuid`, `related_financial_contract_name`, `payment_channel_uuid`, `payment_channel_name`, `payment_institution_name`, `outlier_channel_name`, `create_time`, `last_modify_time`, `credit_channel_working_status`, `debit_channel_working_status`, `credit_payment_channel_service_uuid`, `debit_payment_channel_service_uuid`, `configure_progress`, `payment_configure_data`, `clearing_no`, `business_type`) VALUES 
('6', 'financial_contract_uuid_1', '测试合同', '16ea0d08-735f-11e6-bf08-00163e002839', 'G31700平安银企直联', '2', '600000000001', '2016-09-02 00:00:00', '2016-09-05 19:53:39', '1', '1', 'f8bb9956-1952-4893-98c8-66683d25d7ce', 'f8bb9956-1952-4893-98c8-66683d25d7ce', '1', '{\"complete\":false,\"creditChannelConfigure\":{\"channelStatus\":\"ON\",\"chargeExcutionMode\":\"BACKWARD\",\"chargePerTranscation\":0,\"chargeRateMode\":\"SINGLEFIXED\",\"clearingInterval\":0,\"trasncationLimitPerTransaction\":100,\"valid\":true},\"debitChannelConfigure\":{\"channelStatus\":\"NOTLINK\",\"clearingInterval\":0,\"valid\":false}}', NULL, '0');

INSERT INTO `payment_channel` (`id`, `channel_name`, `user_name`, `user_password`, `merchant_id`, `cer_file_path`, `pfx_file_path`, `pfx_file_key`, `payment_channel_type`, `api_url`, `from_date`, `thru_date`) VALUES 
('1', '测试通道1', 'operator', 'operator', '001053110000001', '/data/webapps/tomcat-earth/webapps/earth/WEB-INF/classes/certificate/gzdsf.cer', '/data/webapps/tomcat-earth/webapps/earth/WEB-INF/classes/certificate/ORA@TEST1.pfx', '123456', '0', 'http://59.41.103.98:333/gzdsf/ProcessServlet', NULL, NULL);

	
	
INSERT INTO `account` (`id`, `account_name`, `account_no`, `bank_name`, `alias`, `attr`, `scan_cash_flow_switch`, `usb_key_configured`, `uuid`, `bank_code`, `cash_flow_config`) VALUES 
('1', '云南信托国际有限公司', '600000000001', '云南国际信托有限公司', NULL, NULL, '\0', '\0', '9d2c6b35-9e0a-447f-bd76-42a8634f8c3e', NULL, NULL);

	
INSERT INTO `contract` (`id`, `uuid`,`unique_id`,`begin_date`, `contract_no`,`end_date`,`app_id`, `customer_id`, `house_id`,`actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`,`active_version_no`,`financial_contract_uuid`)
VALUES (1, 'contract_uuid_1','contract_unique_id_1','2015-10-19', 'contract_no', '2016-10-19', 1, 1, 1, NULL, '2015-10-19 13:34:35', 0.001, 1, 1, 12, 0, 1000, 0.0001,1,'financial_contract_uuid_1');

INSERT INTO `contract_account` (`id`, `pay_ac_no`, `bankcard_type`, `contract_id`, `payer_name`, `bank`, `bind_id`, `id_card_num`, `bank_code`, `province`, `city`, `from_date`, `thru_date`,`contract_account_type`) VALUES 
('1', 'pay_ac_no_1', NULL, '1', '测试用户1', '中国邮政储蓄银行', NULL, 'ID_card1', '403', '安徽省', '亳州', '2016-04-17 00:00:00', '2900-01-01 00:00:00','0');


INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`,`customer_uuid`,`customer_type`) VALUES
	(1, NULL, NULL, NULL, NULL, 1,'customerUuid1',0),
	(2, NULL, NULL, NULL, NULL, 1,'company_customer_uuid_1',1);
	
	
INSERT INTO `asset_set` (`id`, `actual_recycle_date`, `asset_fair_value`, `asset_initial_value`, `asset_interest_value`, `asset_principal_value`, `asset_recycle_date`, `asset_status`, `asset_uuid`, `create_time`, `current_period`, `guarantee_status`, `last_modified_time`, `last_valuation_time`, `on_account_status`, `settlement_status`, `single_loan_contract_no`, `contract_id`,`version_no`,`active_status`,`financial_contract_uuid`,`plan_type`) VALUES
('1', NOW(), '1000.00', '1000.00', '900.00', '100.00', '2016-05-01', '0', 'asset_uuid_1', '2016-05-16 14:26:50', '1', '0', NULL, NULL, '1', '0', 'repayment_plan_no_1', '1',1,0,'financial_contract_uuid_1','0');

	
	
	
INSERT INTO `repayment_order` (`id`, `order_uuid`, `order_unique_id`, `mer_id`, `order_request_no`,`first_repayment_way_group`, `order_alive_status`, `order_check_status`, `order_db_status`, `order_pay_status`, `order_recover_result`, `order_recover_status`, `paid_amount`, `order_amount`, `order_create_time`, `order_last_modified_time`, `order_notify_url`, `ip`, `financial_contract_uuid`, `financial_contract_no`, `financial_contract_name`, `order_detail_file_path`, `active_order_uuid`, `remark`)
VALUES
	(10, 'order_uuid_1', 'orderUniqueId', 'merId', 'e573be60-f16c-4918-9e8c-b220561b9282',5, 0, 1, 0, 0, 0, 0, 0.00, 2000.00, '2017-06-12 17:40:39', '2017-06-12 17:40:39', '', '127.0.0.1', 'financial_contract_uuid_1', 'G31700', '拍拍贷测试', '/home/dell/Desktop/repaymentOrderDetails/repayment_order_1497260439015062c99f4-ee0c-46ee-af5c-019ed8635eea.csv', 'empty', '');

	
INSERT INTO `payment_order` (`id`, `uuid`, `request_no`, `order_uuid`, `order_unique_id`, `outlier_document_uuid`, `outlier_document_identity`, `account_side`, `host_account_no`, `host_account_name`, `host_account_bank_name`, `counter_account_no`, `counter_account_name`, `counter_account_bank_name`, `pay_status`, `recover_status`, `pay_way`, `alive_status`, `mer_id`, `financial_contract_uuid`, `financial_contract_no`, `order_pay_result_status`, `amount`, `transaction_time`, `issue_time`, `create_time`, `last_modified_time`, `remark`, `string_field_1`, `string_field_2`, `string_field_3`, `date_field_1`, `date_field_2`, `date_field_3`, `decimal_field_1`, `decimal_field_2`, `decimal_field_3`, `deduct_request_no`) VALUES 
('1', 'paymentOrderUuid_1', '56313cec-97f3-46ab-b5c2-ffbcb5e5061d', 'order_uuid_1', 'orderUniqueId', 'cash_flow_uuid_3', '84046e27-7ce2-11e7-8014-525400dbb013', '1', '600000000001', '云南国际信托有限公司', '', '6214855712107780', '范腾', NULL, '1', '0', '0', '0', 'systemdeduct', 'financial_contract_uuid_1', 'G08200', '2', '1960.00', '2017-08-09 17:09:57', NULL, '2017-08-09 17:10:14', '2017-08-09 17:10:14', 'bbb', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
('2', 'paymentOrderUuid_2', '36313cec-97f3-46ab-b5c2-ffbcb5e5061d', 'order_uuid_1', 'orderUniqueId', 'cash_flow_uuid_3', '84046e27-7ce2-11e7-8014-525400dbb013', '1', '600000000001', '云南国际信托有限公司', '', '6214855712107780', '范腾', NULL, '1', '0', '0', '0', 'systemdeduct', 'financial_contract_uuid_1', 'G08200', '2', '1960.00', '2017-08-09 17:09:57', NULL, '2017-08-09 17:10:14', '2017-08-09 17:10:14', 'aaa', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);


	
INSERT INTO `cash_flow` (`id`, `cash_flow_uuid`, `cash_flow_channel_type`, `company_uuid`, `host_account_uuid`, `host_account_no`, `host_account_name`, `counter_account_no`, `counter_account_name`, `counter_account_appendix`, `counter_bank_info`, `account_side`, `transaction_time`, `transaction_amount`, `balance`, `transaction_voucher_no`, `bank_sequence_no`, `remark`, `other_remark`, `strike_balance_status`, `cash_flow_type`, `trade_uuid`, `issued_amount`, `audit_status`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`) VALUES 
('1', 'cash_flow_uuid_1', '0', 'company_uuid_1', 'd0503298-e890-425a-3333333', '600000000001', '测试专户开户行', '6214855712107780', '范腾', '', '', '1', '2016-09-05 10:51:02', '1960.00', '2000.00', '', 'cash_flow_no_1', 'remark_1', '', NULL, '0', '', '1000.00', '2', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
('2', 'cash_flow_uuid_2', '0', 'company_uuid_1', 'd0503298-e890-425a-3333333', '600000000001', '测试专户开户行', '6214855712107780', '范腾', '', '', '1', '2016-09-06 10:51:02', '1960.00', '2000.00', '', 'cash_flow_no_2', 'remark_2', '', NULL, '0', '', '1000.00', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

	
	
	
-- repayment way 6为主动付款
INSERT INTO `repayment_order_item` (`id`, `order_detail_uuid`, `contract_unique_id`, `contract_no`, `contract_uuid`, `amount`, `detail_alive_status`, `detail_pay_status`, `mer_id`, `financial_contract_uuid`, `repayment_way`, `repayment_business_uuid`, `repayment_business_no`, `repayment_business_type`, `repayment_plan_time`, `order_uuid`, `order_unique_id`, `remark`)
VALUES
	(1, 'order_detail_uuid_1', 'contract_unique_id_1', 'contract_no_1', 'contract_uuid_1', 1000.00, 0, 0, 'merId', 'financial_contract_uuid_1', 6, 'asset_uuid_1', 'repayment_plan_no_1', 0, '2017-06-15 10:10:10', 'order_uuid_1', 'oder_unique_id_1', 'remark_1');
	
	
INSERT INTO `repayment_order_item_charge` (`id`, `uuid`, `repayment_order_item_uuid`, `contract_uuid`, `repayment_business_no`, `repayment_business_uuid`, `order_uuid`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`, `create_time`, `last_modified_time`, `string_field_1`, `string_field_2`, `string_field_3`, `date_field_1`, `date_field_2`, `date_field_3`, `decimal_field_1`, `decimal_field_2`, `decimal_field_3`) VALUES 
('1', 'repayment_order_item_charge_uuid_1', 'order_detail_uuid_1', 'contract_uuid_1', 'ZC77112003734024192', 'cce4204c-dedd-440b-a342-e9d18803992b', 'order_uuid_1', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, '900.00', '2017-06-28 16:38:17', '2017-06-28 16:38:17', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
	
	
INSERT INTO `repayment_plan_extra_data` (`id`, `uuid`, `asset_set_uuid`, `financial_contract_uuid`, `customer_uuid`, `contract_uuid`, `reason_code`, `comment`, `create_time`, `last_modify_time`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`, `order_item_count`, `order_total_amount`, `order_paying_amount`) VALUES 
('2', 'e7932bd1-413e-46fd-87c9-0c706c8a3fd3', 'asset_uuid_1', 'financial_contract_uuid_1', '4f5ae6a5-9456-428d-a500-f7d1b539adb3', 'contract_uuid_1', '3', NULL, '2017-05-15 21:18:39', '2017-05-15 21:18:39', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '1000.00', '0.00');

	
	
	