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
  delete from `job`;
  delete from `asset_set`;
  delete from `ledger_book_shelf`;

INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`, `day_of_month`, `pay_for_go`, `repurchase_principal_algorithm`, `repurchase_interest_algorithm`, `repurchase_penalty_algorithm`, `repurchase_other_charges_algorithm`, `repayment_check_days`, `allow_freewheeling_repayment`, `repurchase_cycle`, `days_of_cycle`, `temporary_repurchases`)
VALUES
	(1, 1, 3, '2016-04-01 00:00:00', 'G08200', '测试合同', 1, 1, 91, '2016-07-01 00:00:00', 1, 0, 1, 90, 2, 'ledger_book_no', 'financial_contract_uuid_1', 1, 1, 1, 1, 0, 1, NULL, NULL, NULL, NULL, 1, 0, 0, 0, '(principal+interest)*0.05/100*overdueDay', 123.00, 23.00, NULL, NULL, '2017-03-16 22:56:48', 1, 0, 'outstandingPrincipal+outstandingInterest+outstandingPenaltyInterest', 30, 0, 'outstandingPrincipal', 'outstandingInterest', 'outstandingPenaltyInterest', NULL, -1, 0, NULL, NULL, NULL);

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
	
	
INSERT INTO `asset_set` (`id`, `actual_recycle_date`, `asset_fair_value`, `asset_initial_value`, `asset_interest_value`, `asset_principal_value`, `asset_recycle_date`, `asset_status`, `asset_uuid`, `create_time`, `current_period`, `guarantee_status`, `last_modified_time`, `last_valuation_time`, `on_account_status`, `settlement_status`, `single_loan_contract_no`, `contract_id`,`version_no`,`active_status`,`financial_contract_uuid`,`plan_type`,`version_lock`,`contract_uuid`) VALUES
('1', NOW(), '600.00', '600.00', '300.00', '200.00', '2016-05-01', '0', 'asset_uuid_1', '2016-05-16 14:26:50', '1', '0', NULL, NULL, '1', '0', '123213', 1,1,0,'financial_contract_uuid_1','0','11111','contract_uuid_1');

	
INSERT INTO `repayment_order` (`id`, `order_uuid`, `order_unique_id`, `mer_id`, `order_request_no`,`first_repayment_way_group`, `order_alive_status`, `order_check_status`, `order_db_status`, `order_pay_status`, `order_recover_result`, `order_recover_status`, `paid_amount`, `order_amount`, `order_create_time`, `order_last_modified_time`, `order_notify_url`, `ip`, `financial_contract_uuid`, `financial_contract_no`, `financial_contract_name`, `order_detail_file_path`, `active_order_uuid`, `remark`,`order_modify_type`)
VALUES
	(10, 'order_uuid_1', 'orderUniqueId', 'merId', 'e573be60-f16c-4918-9e8c-b220561b9282',5, 3, 1, 0, 1, 0, 0, 0.00, 1000.00, '2017-06-12 17:40:39', '2017-06-12 17:40:39', '', '127.0.0.1', 'financial_contract_uuid_1', 'G08200', '测试合同', '/home/dell/Desktop/repaymentOrderDetails/repayment_order_1497260439015062c99f4-ee0c-46ee-af5c-019ed8635eea.csv', 'check', '','0');
	
	
INSERT INTO `repayment_order` (`id`, `order_uuid`, `order_unique_id`, `mer_id`, `order_request_no`, `order_alive_status`, `order_check_status`, `order_db_status`, `order_pay_status`, `order_recover_result`, `order_recover_status`, `paid_amount`, `order_amount`, `first_customer_source`, `first_customer_name`, `first_repayment_way_group`, `order_create_time`, `order_last_modified_time`, `order_notify_url`, `ip`, `financial_contract_uuid`, `financial_contract_no`, `financial_contract_name`, `order_detail_file_path`, `active_order_uuid`, `string_field_1`, `string_field_2`, `string_field_3`, `date_field_1`, `date_field_2`, `date_field_3`, `decimal_field_1`, `decimal_field_2`, `decimal_field_3`, `remark`, `reserved_field3`, `reserved_field9`, `reserved_field6`, `reserved_field2`, `reserved_field1`, `reserved_field7`, `reserved_field8`, `reserved_field4`, `order_pay_result_status`, `reserved_field5`, `repayment_orders_list`, `source_status`, `placing_job_uuid`, `cancel_job_uuid`, `write_off_job_uuid`, `repayment_order_detail_json`, `first_contract_uuid`, `actual_notify_check_nums`, `actual_notify_cancel_nums`, `actual_notify_pay_nums`, `receivable_in_advance_status`, `cash_flow_time`, `details_number`, `order_modify_type`,`is_detail_amount_empty`) VALUES 
(21, 'order_uuid_2', 'orderUniqueId', 'merId', 'a0304140-0245-41af-8a6e-2ad867e04f84', '0', '3', '0', '0', '0', '0', '0.00', '600.00', NULL, 'customer_name', '5', '2018-03-28 16:40:50', '2018-03-28 16:40:51', '', 'ip', 'financial_contract_uuid_1', 'G08200', '测试合同', '', 'check', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '0', '6e8d5b01-56de-4460-acd7-02f964778ec4', NULL, NULL, '[{\"contractNo\":\"contract_no\",\"contractUniqueId\":\"\",\"currentPeriod\":0,\"detailsAmountJsonList\":\"[{\\\"actualAmount\\\":300,\\\"feeType\\\":\\\"1000\\\",\\\"feeTypeEnum\\\":\\\"PRINCIPAL\\\"},{\\\"actualAmount\\\":200,\\\"feeType\\\":\\\"1001\\\",\\\"feeTypeEnum\\\":\\\"INTEREST\\\"},{\\\"actualAmount\\\":100,\\\"feeType\\\":\\\"1002\\\",\\\"feeTypeEnum\\\":\\\"LOAN_SERVICE_FEE\\\"}]\",\"detailsTotalAmount\":\"600.00\",\"plannedDate\":\"2017-06-06 01:02:03\",\"repayScheduleNo\":\"\",\"repaymentBusinessNo\":\"1232131111\",\"repaymentOrderDetailUuid\":\"\",\"repaymentWay\":\"5001\"}]', 'contract_uuid_1', '0', '0', '0', '0', NULL, '0', '1','1');


INSERT INTO `ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`) VALUES 

--('6', 'a73569e3-6271-4028-baa3-e9def7c843d1', '0.00', '900.00', 'FST_LONGTERM_LIABILITY', '40000', '0', 'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL, '14', 'customerUuid1', NULL, NULL, NULL, 'b18831e1-3ab7-46a4-8005-7698814440e5', '2015-10-19', '2016-09-13 13:24:23', '', NULL, '1', NULL, '2015-10-19 00:00:00', '', 'yunxin_ledger_book', '14', '1', 'DKHD-001-01', 'asset_uuid_1', NULL, NULL, NULL, NULL, ''),
--('7', '0e49b608-55b7-4375-957d-4a9f3172ac7a', '0.00', '100.00', 'FST_DEFERRED_INCOME', '100000', '0', 'SND_DEFERRED_INCOME_INTEREST_ESTIMATE', '100000.01', NULL, NULL, '14', 'customerUuid1', NULL, NULL, NULL, 'b18831e1-3ab7-46a4-8005-7698814440e5', '2015-10-19', '2016-09-13 13:24:23', '', NULL, '1', NULL, '2015-10-19 00:00:00', '', 'yunxin_ledger_book', '14', '1', 'DKHD-001-01', 'asset_uuid_1', NULL, NULL, NULL, NULL, ''),

('8', '9bb36c8e-ca27-4a9d-a376-23eaf503b389', '300.00', '0.00', 'FST_RECIEVABLE_ASSET', '20000', '1', 'SND_RECIEVABLE_OVERDUE_LOAN_ASSET', '20000.05', 'TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_PRINCIPLE', '20000.05.01', '14', 'customerUuid1', NULL, NULL, NULL, 'b18831e1-3ab7-46a4-8005-7698814440e5', '2015-10-19', '2016-09-13 13:24:23', '', NULL, '1', NULL, '2015-10-19 00:00:00', '', 'ledger_book_no', '14', '1', 'DKHD-001-01', 'asset_uuid_1', NULL, NULL, NULL, NULL, ''),
('9', '62050685-3d58-430c-b979-b38b6fc970bb', '200.00', '0.00', 'FST_RECIEVABLE_ASSET', '20000', '1', 'SND_RECIEVABLE_OVERDUE_LOAN_ASSET', '20000.05', 'TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_INTEREST', '20000.05.02', '14', 'customerUuid1', NULL, NULL, NULL, 'b18831e1-3ab7-46a4-8005-7698814440e5', '2015-10-19', '2016-09-13 13:24:23', '', NULL, '1', NULL, '2015-10-19 00:00:00', '', 'ledger_book_no', '14', '1', 'DKHD-001-01', 'asset_uuid_1', NULL, NULL, NULL, NULL, ''),
('10', 'c5d380f0-83ab-420b-9c59-1d5206d32764', '100.00', '0.00', 'FST_RECIEVABLE_ASSET', '20000', '1', 'SND_RECIEVABLE_OVERDUE_LOAN_ASSET', '20000.05', 'TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_LOAN_SERVICE_FEE', '20000.05.03', '14', 'customerUuid1', NULL, NULL, NULL, 'b18831e1-3ab7-46a4-8005-7698814440e5', '2015-10-19', '2016-09-13 13:24:23', '', NULL, '1', NULL, '2015-10-19 00:00:00', '', 'ledger_book_no', '14', '1', 'DKHD-001-01', 'asset_uuid_1', NULL, NULL, NULL, NULL, '');
--('11', '380999a9-43fb-4cf4-b669-808a9832c262', '30.00', '0.00', 'FST_RECIEVABLE_ASSET', '20000', '1', 'SND_RECIEVABLE_OVERDUE_LOAN_ASSET', '20000.05', 'TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_TECH_FEE', '20000.05.04', '14', 'customerUuid1', NULL, NULL, NULL, 'b18831e1-3ab7-46a4-8005-7698814440e5', '2015-10-19', '2016-09-13 13:24:23', '', NULL, '1', NULL, '2015-10-19 00:00:00', '', 'yunxin_ledger_book', '14', '1', 'DKHD-001-01', 'asset_uuid_1', NULL, NULL, NULL, NULL, '');


INSERT INTO `ledger_book` (`id`, `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`, `ledger_book_version`) VALUES 
('1', 'ledger_book_no', '1', '', NULL, NULL);


INSERT INTO `job` (`id`, `create_time`, `end_time`, `excuting_result`, `excuting_status`, `fifth_stage_current_task_expired_time`, `fifth_stage_excuting_result`, `fifth_stage_excuting_status`, `fifth_stage_retry_times`, `fifth_stage_uuid`, `fifth_stagecreate_time`, `fifth_stagelast_modified_time`, `fourth_stage_current_task_expired_time`, `fourth_stage_excuting_result`, `fourth_stage_excuting_status`, `fourth_stage_retry_times`, `fourth_stage_uuid`, `fourth_stagecreate_time`, `fourth_stagelast_modified_time`, `fst_business_code`, `fst_stage_current_task_expired_time`, `fst_stage_excuting_result`, `fst_stage_excuting_status`, `fst_stage_retry_times`, `fst_stage_uuid`, `fst_stagecreate_time`, `fst_stagelast_modified_time`, `issuer_ip`, `issuer_identity`, `job_name`, `job_type`, `last_modified_time`, `snd_business_code`, `snd_stage_current_task_expired_time`, `snd_stage_excuting_result`, `snd_stage_excuting_status`, `snd_stage_retry_times`, `snd_stage_uuid`, `snd_stagecreate_time`, `snd_stagelast_modified_time`, `start_time`, `trd_business_code`, `trd_stage_current_task_expired_time`, `trd_stage_excuting_result`, `trd_stage_excuting_status`, `trd_stage_retry_times`, `trd_stage_uuid`, `trd_stagecreate_time`, `trd_stagelast_modified_time`, `uuid`, `fifth_stage_timeout`, `fourth_stage_timeout`, `fst_stage_timeout`, `snd_stage_timeout`, `trd_stage_timeout`, `fifth_bean_name`, `fifth_method_name`, `fifth_stage_chunk_size`, `fourth_bean_name`, `fourth_method_name`, `fourth_stage_chunk_size`, `fst_bean_name`, `fst_method_name`, `fst_stage_chunk_size`, `snd_bean_name`, `snd_method_name`, `snd_stage_chunk_size`, `trd_bean_name`, `trd_method_name`, `trd_stage_chunk_size`, `fifth_stage_priority`, `fourth_stage_priority`, `fst_stage_priority`, `snd_stage_priority`, `trd_stage_priority`) VALUES 
('11', '2018-03-28 16:40:50', NULL, NULL, '0', '2018-03-30 16:40:51', NULL, '0', '3', '58e07580-2584-4b0f-adea-1faa87e5e02d', NULL, '2018-03-28 16:40:51', '2018-03-30 16:40:51', NULL, '0', '3', '6fa12d0c-4cb6-464d-aa21-11fa18219233', NULL, '2018-03-28 16:40:51', 'order_uuid_2', '2018-03-30 16:40:50', NULL, '0', '3', 'b37324b3-6a05-430e-8a90-d2234eea5f02', NULL, '2018-03-28 16:40:50', '192.168.1.101', 'wukai-mac', 'jobType_REPAYMENT_ORDER_MODIFY_fstBusinessCode_ed42b94a-2a2c-4251-ab33-67c88a851cbb', '12', '2018-03-28 16:40:50', 'orderUniqueId', '2018-03-30 16:40:51', NULL, '0', '3', 'fee75ff3-0b3e-4c57-a5cf-e7de09a3ca9a', NULL, '2018-03-28 16:40:51', '2018-03-28 16:40:50', 'a0304140-0245-41af-8a6e-2ad867e04f84', '2018-03-30 16:40:51', NULL, '0', '3', '2b6cc57b-d9ac-47a9-8212-7855711ca4d6', NULL, '2018-03-28 16:40:51', '6e8d5b01-56de-4460-acd7-02f964778ec4', '172800000', '172800000', '172800000', '172800000', '172800000', 'dstJobRepaymentOrderModifyPlacing', 'roll_back', '20', 'dstJobRepaymentOrderModifyPlacing', 'cancel_original_order', '20', 'dstJobRepaymentOrderModifyPlacing', 'criticalMarker', '20', 'dstJobRepaymentOrderModifyPlacing', 'check_and_save', '20', 'dstJobRepaymentOrderModifyPlacing', 'relate_payment_order_to_modify_order', '20', '0', '2', '2', '2', '2');
