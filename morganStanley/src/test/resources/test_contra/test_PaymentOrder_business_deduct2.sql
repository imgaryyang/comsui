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
	(1, NULL, NULL, 'customer_name', NULL, 1,'customerUuid1',0),
	(2, NULL, NULL, NULL, NULL, 1,'company_customer_uuid_1',1);
	
	
	INSERT INTO `repayment_order` (`id`, `order_uuid`, `order_unique_id`, `mer_id`, `order_request_no`,`first_repayment_way_group`, `order_alive_status`, `order_check_status`, `order_db_status`, `order_pay_status`, `order_recover_result`, `order_recover_status`, `paid_amount`, `order_amount`, `order_create_time`, `order_last_modified_time`, `order_notify_url`, `ip`, `financial_contract_uuid`, `financial_contract_no`, `financial_contract_name`, `order_detail_file_path`, `active_order_uuid`, `remark`)
VALUES
	(10, 'order_uuid_1', 'orderUniqueId', 'merId', 'e573be60-f16c-4918-9e8c-b220561b9282',1, 0, 1, 0, 1, 0, 0, 0.00, 1000.00, '2017-06-12 17:40:39', '2017-06-12 17:40:39', '', '127.0.0.1', 'financial_contract_uuid_1', 'G08200', '测试合同', '/home/dell/Desktop/repaymentOrderDetails/repayment_order_1497260439015062c99f4-ee0c-46ee-af5c-019ed8635eea.csv', 'empty', '');
