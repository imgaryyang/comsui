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
  delete from `t_deduct_application_detail`;
  delete from `t_deduct_plan`;
  delete from `bank`;
  delete from `third_party_transaction_record`;
  delete from `third_party_payment_voucher`;
  delete from `third_party_payment_voucher_detail`;

  INSERT INTO `bank` (`id`, `bank_code`, `bank_name`) VALUES 
  ('10', 'C10102', '中国工商银行'),
  ('13', 'C10105', '中国建设银行');

  
INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`, `day_of_month`, `pay_for_go`, `repurchase_principal_algorithm`, `repurchase_interest_algorithm`, `repurchase_penalty_algorithm`, `repurchase_other_charges_algorithm`, `repayment_check_days`, `allow_freewheeling_repayment`, `repurchase_cycle`, `days_of_cycle`, `temporary_repurchases`)
VALUES
	(1, 1, 3, '2016-04-01 00:00:00', 'G08200', '测试合同', 1, 1, 91, '2016-07-01 00:00:00', 1, 0, 1, 90, 2, '74a9ce4d-cafc-407d-b013-987077541bdc', 'financial_contract_uuid_1', 1, 1, 1, 1, 0, 1, NULL, NULL, NULL, NULL, 1, 0, 0, 0, '(principal+interest)*0.05/100*overdueDay', 123.00, 23.00, NULL, NULL, '2017-03-16 22:56:48', 1, 0, 'outstandingPrincipal+outstandingInterest+outstandingPenaltyInterest', 30, 0, 'outstandingPrincipal', 'outstandingInterest', 'outstandingPenaltyInterest', NULL, -1, 0, NULL, NULL, NULL);

INSERT INTO `financial_contract_config` (`id`, `financial_contract_uuid`, `business_type`, `payment_channel_uuids_for_credit`, `payment_channel_uuids_for_debit`, `credit_payment_channel_mode`, `debit_payment_channel_mode`, `payment_channel_router_for_credit`, `payment_channel_router_for_debit`, `payment_channel_config_for_credit`, `payment_channel_config_for_debit`) VALUES 
('4', 'financial_contract_uuid_1', '0', 'f2591e61-735e-11e6-bf08-00163e002839,16ea0d08-735f-11e6-bf08-00163e002839', 'f2591e61-735e-11e6-bf08-00163e002839,16ea0d08-735f-11e6-bf08-00163e002839', '0', '0', 'f2591e61-735e-11e6-bf08-00163e002839', '16ea0d08-735f-11e6-bf08-00163e002839', NULL, NULL);


INSERT INTO `payment_channel_information` (`id`, `related_financial_contract_uuid`, `related_financial_contract_name`, `payment_channel_uuid`, `payment_channel_name`, `payment_institution_name`, `outlier_channel_name`, `create_time`, `last_modify_time`, `credit_channel_working_status`, `debit_channel_working_status`, `credit_payment_channel_service_uuid`, `debit_payment_channel_service_uuid`, `configure_progress`, `payment_configure_data`, `clearing_no`, `business_type`) VALUES 
('6', 'financial_contract_uuid_1', '测试合同', '16ea0d08-735f-11e6-bf08-00163e002839', 'G31700平安银企直联', '2', '600000000001', '2016-09-02 00:00:00', '2016-09-05 19:53:39', '1', '1', 'f8bb9956-1952-4893-98c8-66683d25d7ce', 'f8bb9956-1952-4893-98c8-66683d25d7ce', '1', '{\"complete\":false,\"creditChannelConfigure\":{\"channelStatus\":\"ON\",\"chargeExcutionMode\":\"BACKWARD\",\"chargePerTranscation\":0,\"chargeRateMode\":\"SINGLEFIXED\",\"clearingInterval\":0,\"trasncationLimitPerTransaction\":100,\"valid\":true},\"debitChannelConfigure\":{\"channelStatus\":\"NOTLINK\",\"clearingInterval\":0,\"trasncationLimitPerTransaction\":100,\"valid\":true}}', 'clearing_no', '0');

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
	
	
	INSERT INTO `repayment_order` (`id`, `order_uuid`, `order_unique_id`, `mer_id`, `order_request_no`,`first_repayment_way_group`, `order_alive_status`, `order_check_status`, `order_db_status`, `order_pay_status`, `order_recover_result`, `order_recover_status`, `paid_amount`, `order_amount`, `order_create_time`, `order_last_modified_time`, `order_notify_url`, `ip`, `financial_contract_uuid`, `financial_contract_no`, `financial_contract_name`, `order_detail_file_path`, `active_order_uuid`, `remark`,`first_contract_uuid`)
VALUES
	(10, 'order_uuid_1', 'orderUniqueId', 'merId', 'e573be60-f16c-4918-9e8c-b220561b9282',2, 0, 1, 0, 0, 0, 0, 0.00, 2000.00, '2017-06-12 17:40:39', '2017-06-12 17:40:39', '', '127.0.0.1', 'financial_contract_uuid_1', 'G08200', '测试合同', '/home/dell/Desktop/repaymentOrderDetails/repayment_order_1497260439015062c99f4-ee0c-46ee-af5c-019ed8635eea.csv', 'empty', '','contract_uuid_1');

	
INSERT INTO `payment_order` (`id`, `uuid`, `request_no`, `order_uuid`, `order_unique_id`, `outlier_document_uuid`, `outlier_document_identity`, `account_side`, `host_account_no`, `host_account_name`, `host_account_bank_name`, `counter_account_no`, `counter_account_name`, `counter_account_bank_name`, `pay_status`, `recover_status`, `pay_way`, `alive_status`, `mer_id`, `financial_contract_uuid`, `financial_contract_no`, `order_pay_result_status`, `amount`, `transaction_time`, `issue_time`, `create_time`, `last_modified_time`, `remark`, `string_field_1`, `string_field_2`, `string_field_3`, `date_field_1`, `date_field_2`, `date_field_3`, `decimal_field_1`, `decimal_field_2`, `decimal_field_3`, `deduct_request_no`,`trade_uuid`,`retried_transaction_record_nums`,`batch_no`,`payment_gate_way`) VALUES 
('1', 'paymentOrderUuid_1', '56313cec-97f3-46ab-b5c2-ffbcb5e5061d', 'order_uuid_1', 'orderUniqueId', 'cash_flow_uuid_2', '84046e27-7ce2-11e7-8014-525400dbb013', '1', '600000000001', '云南国际信托有限公司', '', '6214855712107780', '范腾', NULL, '0', '0', '3', '0', 'systemdeduct', 'financial_contract_uuid_1', 'G08200', '2', '2000.00', '2017-08-09 17:09:57', NULL, '2017-08-09 17:10:14', '2017-08-09 17:10:14', 'bbb', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,'JIAOYIQINGQIU100_200',6,'batch_no',2);


	
INSERT INTO `cash_flow` (`id`, `cash_flow_uuid`, `cash_flow_channel_type`, `company_uuid`, `host_account_uuid`, `host_account_no`, `host_account_name`, `counter_account_no`, `counter_account_name`, `counter_account_appendix`, `counter_bank_info`, `account_side`, `transaction_time`, `transaction_amount`, `balance`, `transaction_voucher_no`, `bank_sequence_no`, `remark`, `other_remark`, `strike_balance_status`, `cash_flow_type`, `trade_uuid`, `issued_amount`, `audit_status`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`) VALUES 
('1', 'cash_flow_uuid_1', '0', 'company_uuid_1', 'd0503298-e890-425a-3333333', '600000000001', '测试专户开户行', '6214855712107780', '范腾', '', '', '1', '2016-09-05 10:51:02', '1960.00', '2000.00', '', 'cash_flow_no_1', 'remark_1', '', NULL, '0', '', '1000.00', '2', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
('2', 'cash_flow_uuid_2', '0', 'company_uuid_1', 'd0503298-e890-425a-3333333', '600000000001', '测试专户开户行', '6214855712107780', '范腾', '', '', '1', '2016-09-06 10:51:02', '1960.00', '2000.00', '', 'cash_flow_no_2', 'remark_2', '', NULL, '0', '', '1000.00', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);


INSERT INTO `third_party_transaction_record` (`id`, `transaction_record_uuid`, `business_process_status`, `financial_contract_uuid`, `payment_gateway`, `merchant_no`, `snd_lvl_merchant_no`, `account_side`, `channel_sequence_no`, `merchant_order_no`, `settle_date`, `transaction_amount`, `service_fee`, `transaction_time`, `counter_account_no`, `counter_account_name`, `counter_account_appendix`, `remark`, `create_time`, `issued_amount`, `audit_status`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`, `reckon_account`, `transaction_create_time`, `pay_time`, `status_modify_time`, `md_5_version`, `batch_no`) VALUES 
('1', 'c74259ea-d355-11e7-a3df-525400dbb013', '1', 'G31700', '0', '001053110000001', NULL, '2', NULL, 'JIAOYIQINGQIU100_200', '2017-05-25 00:00:00', '2000.00', NULL, '2017-11-27 17:31:42', '6214855712107780', '范腾', NULL, '', NULL, '0.00', '2', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, '2017-11-27 17:31:42', '2017-11-27 23:29:18', NULL, NULL);


INSERT INTO `repayment_order_item` (`id`, `order_detail_uuid`, `contract_unique_id`, `contract_no`, `contract_uuid`, `current_period`, `amount`, `detail_alive_status`, `detail_pay_status`, `mer_id`, `financial_contract_uuid`, `repayment_way`, `repayment_business_uuid`, `repayment_business_no`, `repay_schedule_no`, `identification_mode`, `repayment_business_type`, `repayment_plan_time`, `order_uuid`, `order_unique_id`, `create_time`, `last_modified_time`, `string_field_1`, `string_field_2`, `string_field_3`, `date_field_1`, `date_field_2`, `date_field_3`, `decimal_field_1`, `decimal_field_2`, `decimal_field_3`, `remark`, `receivable_in_advance_status`, `charge_detail`) VALUES 
('33', 'd99bd059-51b6-4891-a40e-df174c9d2be9', 'contract_unique_id_1', 'contract_no', 'contract_uuid_1', '2', '1000.00', '0', '1', 't_test_zfb', '9495f5f2-d306-461a-8b03-5896923dc1b3', '0', 'c9c50683-30c8-4bb3-ace9-49ce9b8cdd01', 'ZC133104659475144704', NULL, '1', '0', NULL, 'order_uuid_1', 'orderUniqueId', '2017-11-30 01:33:29', '2017-11-30 01:34:58', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '0', '{\"SND_UNEARNED_LOAN_ASSET_INTEREST\":200,\"SND_UNEARNED_LOAN_ASSET_PRINCIPLE\":800}'),
('34', '0b2a46df-5f7c-4096-8795-b95767f716ee', 'contract_unique_id_1', 'contract_no', 'contract_uuid_1', '1', '1000.00', '0', '1', 't_test_zfb', '9495f5f2-d306-461a-8b03-5896923dc1b3', '0', '76e328c5-3ca4-45e6-b503-0a9cfa194dff', 'ZC133104659449978880', NULL, '1', '0', '2017-09-13 00:00:00', 'order_uuid_1', 'orderUniqueId', '2017-11-30 01:33:29', '2017-11-30 01:34:58', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '0', '{\"SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE\":300,\"SND_UNEARNED_LOAN_ASSET_PRINCIPLE\":700}');


INSERT INTO `repayment_order_item_charge` (`id`, `uuid`, `repayment_order_item_uuid`, `contract_uuid`, `repayment_business_no`, `repayment_business_uuid`, `order_uuid`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`, `create_time`, `last_modified_time`, `string_field_1`, `string_field_2`, `string_field_3`, `date_field_1`, `date_field_2`, `date_field_3`, `decimal_field_1`, `decimal_field_2`, `decimal_field_3`) VALUES 
('75129', '8120e891-a85f-4359-888c-0bce48a6e690', 'd99bd059-51b6-4891-a40e-df174c9d2be9', 'contract_uuid_1', 'ZC133104659475144704', 'c9c50683-30c8-4bb3-ace9-49ce9b8cdd01', '39e74cb8-5d61-434f-b165-1c319e1875c6', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, '800.00', '2017-11-30 01:33:29', '2017-11-30 01:33:29', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
('75130', 'c0fb8662-9799-4de4-a8aa-c7cf635ef4a9', 'd99bd059-51b6-4891-a40e-df174c9d2be9', 'contract_uuid_1', 'ZC133104659475144704', 'c9c50683-30c8-4bb3-ace9-49ce9b8cdd01', '39e74cb8-5d61-434f-b165-1c319e1875c6', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_INTEREST', '10000.01', NULL, NULL, '200.00', '2017-11-30 01:33:29', '2017-11-30 01:33:29', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);


INSERT INTO `repayment_order_item_charge` (`id`, `uuid`, `repayment_order_item_uuid`, `contract_uuid`, `repayment_business_no`, `repayment_business_uuid`, `order_uuid`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`, `create_time`, `last_modified_time`, `string_field_1`, `string_field_2`, `string_field_3`, `date_field_1`, `date_field_2`, `date_field_3`, `decimal_field_1`, `decimal_field_2`, `decimal_field_3`) VALUES 
('75134', 'f3b7ee3d-e2f6-4f78-9783-dd35747fecb7', '0b2a46df-5f7c-4096-8795-b95767f716ee', 'contract_uuid_1', 'ZC133104659449978880', '76e328c5-3ca4-45e6-b503-0a9cfa194dff', '39e74cb8-5d61-434f-b165-1c319e1875c6', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, '700.00', '2017-11-30 01:33:29', '2017-11-30 01:33:29', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
('75136', '29fe0cc4-29af-4a1f-8022-54d2486a95fc', '0b2a46df-5f7c-4096-8795-b95767f716ee', 'contract_uuid_1', 'ZC133104659449978880', '76e328c5-3ca4-45e6-b503-0a9cfa194dff', '39e74cb8-5d61-434f-b165-1c319e1875c6', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE', '10000.03', NULL, NULL, '300.00', '2017-11-30 01:33:29', '2017-11-30 01:33:29', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);


INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`, `version_lock`, `order_payment_status`, `repay_schedule_no`, `outer_repayment_plan_no`) VALUES 
('1', '0', '0', '1000.00', '1000.00', '0.00', '1000.00', '2017-12-31', '2017-11-30', '0.00', '1', '2', '0', NULL, 'c9c50683-30c8-4bb3-ace9-49ce9b8cdd01', '2017-11-29 04:31:29', '2017-11-30 01:34:58', NULL, 'ZC133104659475144704', '1', '2017-11-30 01:35:00', '2', '0', NULL, '1', '0', '0', 'empty_deduct_uuid', NULL, '9495f5f2-d306-461a-8b03-5896923dc1b3', 'cc4ccbf6948abab4dcca100768c4f6a6', 'd4de8e2b6fc4af949a22e10a3efd3a65', '2017-11-29 04:31:29', '2017-11-29 04:31:29', '0', '0', '0', '0', '0', '2', '0', 'bc48f666-e981-4ba0-9f07-609272752bf8', '83e459f7-a182-43a3-9c11-a7c846b17d3c', '0', '92df1ed0-96de-46ae-aba7-841160f0aadc', '0', 'repay_schedule_no_1', 'outer_repayment_plan_no_1'),
('2', '0', '0', '1000.00', '1000.00', '0.00', '1000.00', '2017-11-30', '2017-11-30', '0.00', '1', '2', '0', '2017-11-30 00:02:08', '76e328c5-3ca4-45e6-b503-0a9cfa194dff', '2017-11-29 04:31:29', '2017-11-30 01:34:58', NULL, 'ZC133104659449978880', '1', '2017-11-30 01:35:00', '1', '0', NULL, '1', '0', '0', 'empty_deduct_uuid', NULL, '9495f5f2-d306-461a-8b03-5896923dc1b3', '32ab72d7cbc906787e964576ee1c68a0', 'd4de8e2b6fc4af949a22e10a3efd3a65', '2017-11-29 04:31:29', '2017-11-29 04:31:29', '0', '0', '0', '1', '0', '2', '0', 'bc48f666-e981-4ba0-9f07-609272752bf8', '83e459f7-a182-43a3-9c11-a7c846b17d3c', '0', 'eb3cc17a-044a-45f4-a908-aa254ca74f2b', '0', 'repay_schedule_no_2', 'outer_repayment_plan_no_2');

	
INSERT INTO `third_party_payment_voucher` (`id`, `voucher_uuid`, `voucher_no`, `trade_uuid`, `financial_contract_uuid`, `financial_contract_no`, `transcation_amount`, `transaction_time`, `transcation_complete_time`, `transcation_gateway`, `create_time`, `last_modified_time`, `execution_status`, `process_status`, `voucher_log_status`, `voucher_log_issue_status`, `receivable_account_no`, `payment_account_no`, `payment_name`, `payment_bank_name`, `voucher_source`, `remark`, `contract_uuid`, `contract_no`, `batch_no`, `version_no`, `batch_uuid`, `bank_transaction_no`, `contract_unique_id`) VALUES 
('2', 'b4fc7a3c-67e9-498a-ad57-3c4c1056787f', '07e8c0e6-e93b-4ef9-878d-a09bf2010ed7', 'JIAOYIQINGQIU100_200', 'financial_contract_uuid_1', 'G08200', '2000.00', '2017-08-09 17:09:57', NULL, '2', '2017-12-11 15:54:21', '2017-12-11 15:54:21', '2', '1', '7', '0', '600000000001', '6214855712107780', '范腾', NULL, '1', '', 'contract_uuid_1', 'contract_no', 'batch_no', '9b790f72-66a5-467d-bc1d-30673c9184d3', '018a47b6-5a66-4de6-8f9a-167d17fd57dd', '', 'contract_unique_id_1');

INSERT INTO `third_party_payment_voucher_detail` (`id`, `detail_uuid`, `voucher_uuid`, `voucher_no`, `amount`, `repayment_business_uuid`, `repayment_business_no`, `financial_contract_uuid`, `financial_contract_no`, `contract_uuid`, `contract_no`, `create_time`, `last_modified_time`, `repay_schedule_no`, `outer_repayment_plan_no`, `detail_amount_json`, `current_period`) VALUES 
('3', '9bfee4cf-f443-4654-ab94-79a9089965ee', 'b4fc7a3c-67e9-498a-ad57-3c4c1056787f', '07e8c0e6-e93b-4ef9-878d-a09bf2010ed7', '1000.00', 'c9c50683-30c8-4bb3-ace9-49ce9b8cdd01', 'ZC133104659475144704', 'financial_contract_uuid_1', 'G08200', 'contract_uuid_1', 'contract_no', '2017-12-11 15:54:21', '2017-12-11 15:54:21', 'repay_schedule_no_1', 'outer_repayment_plan_no_1', '{\"SND_UNEARNED_LOAN_ASSET_INTEREST\":200,\"SND_UNEARNED_LOAN_ASSET_PRINCIPLE\":800}', '2');

INSERT INTO `third_party_payment_voucher_detail` (`id`, `detail_uuid`, `voucher_uuid`, `voucher_no`, `amount`, `repayment_business_uuid`, `repayment_business_no`, `financial_contract_uuid`, `financial_contract_no`, `contract_uuid`, `contract_no`, `create_time`, `last_modified_time`, `repay_schedule_no`, `outer_repayment_plan_no`, `detail_amount_json`, `current_period`) VALUES 
('4', '31bf7dd9-9067-45c5-8791-1ffb07ddf888', 'b4fc7a3c-67e9-498a-ad57-3c4c1056787f', '07e8c0e6-e93b-4ef9-878d-a09bf2010ed7', '1000.00', '76e328c5-3ca4-45e6-b503-0a9cfa194dff', 'ZC133104659449978880', 'financial_contract_uuid_1', 'G08200', 'contract_uuid_1', 'contract_no', '2017-12-11 15:54:21', '2017-12-11 15:54:21', 'repay_schedule_no_2', 'outer_repayment_plan_no_2', '{\"SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE\":300,\"SND_UNEARNED_LOAN_ASSET_PRINCIPLE\":700}', '1');

INSERT INTO `t_deduct_application` (`id`, `deduct_application_uuid`, `deduct_id`, `request_no`, `financial_contract_uuid`, `financial_product_code`, `contract_unique_id`, `repayment_plan_code_list`, `contract_no`, `planned_deduct_total_amount`, `actual_deduct_total_amount`, `notify_url`, `transcation_type`, `repayment_type`, `execution_status`, `execution_remark`, `create_time`, `creator_name`, `ip`, `last_modified_time`, `record_status`, `is_available`, `api_called_time`, `transaction_recipient`, `customer_name`, `mobile`, `gateway`, `source_type`, `third_part_voucher_status`, `complete_time`, `transaction_time`, `actual_notify_number`, `plan_notify_number`, `batch_deduct_id`, `none_business_check_status`, `batch_deduct_application_uuid`, `retry_times`, `payment_order_uuid`, `retriable`, `notify_status`, `business_check_status`, `version`, `total_count`, `executed_count`, `receive_status`, `request_params`, `check_response_no`, `plan_notify_citigroup_number`, `actual_notify_citigroup_number`, `date_field_one`, `date_field_two`, `date_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_One`, `decimal_field_two`, `decimal_field_three`, `int_field_one`, `int_field_two`, `int_field_three`) VALUES 
('2', 'e506798c-6f6c-4404-9d70-a36a84d8d9a7', '07e8c0e6-e93b-4ef9-878d-a09bf2010ed7', NULL, 'financial_contract_uuid_1', 'G08200', 'contract_unique_id_1', '[\"ZC133104659475144704\",\"ZC133104659449978880\"]', 'contract_no', '2000.00', '2000.00', '', '1', '0', '2', '', '2017-12-11 15:54:21', '', NULL, '2017-12-11 15:54:21', '0', '0', '2017-12-11 15:54:21', '1', '范腾', NULL, '', '2', '0', '2017-11-27 17:31:42', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL, '2c604f06-042c-4cbd-90a5-83957125ba7b', '0', '0', '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0');

INSERT INTO `t_deduct_plan` (`id`, `deduct_plan_uuid`, `deduct_application_uuid`, `deduct_application_detail_uuid`, `financial_contract_uuid`, `contract_unique_id`, `contract_no`, `payment_gateway`, `payment_channel_uuid`, `pg_account`, `pg_clearing_account`, `transaction_type`, `transaction_remark`, `cp_bank_code`, `cp_bank_card_no`, `cp_bank_account_holder`, `cp_id_type`, `cp_id_number`, `cp_bank_province`, `cp_bank_city`, `cp_bank_name`, `planned_payment_date`, `complete_payment_date`, `planned_total_amount`, `actual_total_amount`, `execution_precond`, `execution_status`, `execution_remark`, `create_time`, `creator_name`, `last_modified_time`, `transaction_serial_no`, `mobile`, `transaction_recipient`, `trade_uuid`, `repayment_type`, `source_type`, `complete_time`, `transaction_time`, `batch_deduct_application_uuid`, `batch_deduct_id`, `none_business_check_status`, `business_check_status`, `notify_status`, `retriable`, `retry_times`, `payment_order_uuid`, `version`, `clearing_status`, `clearing_cash_flow_uuid`, `clearing_time`, `trade_schedule_slot_uuid`, `transaction_uuid`, `deduct_cash_identity`, `deduct_mode`, `date_field_one`, `date_field_two`, `date_field_three`, `int_field_one`, `int_field_two`, `int_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_One`, `decimal_field_two`, `decimal_field_three`) VALUES 
('2', '199b8c74-c905-46c8-a79e-c02409374def', 'e506798c-6f6c-4404-9d70-a36a84d8d9a7', NULL, 'financial_contract_uuid_1', 'contract_unique_id_1', 'contract_no', '2', 'f8bb9956-1952-4893-98c8-66683d25d7ce', '600000000001', 'clearing_no', '1', '', '', '6214855712107780', '范腾', '0', '', '', '', NULL, NULL, '2017-11-27 17:31:42', '2000.00', '2000.00', '', '2', NULL, '2017-12-11 15:54:21', '', '2017-12-11 15:54:21', NULL, '', '1', 'JIAOYIQINGQIU100_200', '0', '2', '2017-11-27 17:31:42', NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL, '0', NULL, NULL, NULL, NULL, '65c75e86f8e5abf15541b51db990f371', NULL, NULL, NULL, NULL, '0', '0', '0', NULL, NULL, NULL, NULL, NULL, NULL);

