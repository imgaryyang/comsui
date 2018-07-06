	SET FOREIGN_KEY_CHECKS=0;
	
	delete from `repayment_order`;
  	delete from `repayment_order_item`;
  	delete from `repayment_order_item_check_fail_log`;
  	delete from `financial_contract`;
  	
  	INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`, `day_of_month`, `pay_for_go`, `repurchase_principal_algorithm`, `repurchase_interest_algorithm`, `repurchase_penalty_algorithm`, `repurchase_other_charges_algorithm`, `repayment_check_days`, `allow_freewheeling_repayment`, `repurchase_cycle`, `days_of_cycle`, `temporary_repurchases`) VALUES 
  	('1', '0', '1', '2016-09-01 00:00:00', 'G31700', '拍拍贷测试', '3', '1', '60', '2017-08-31 00:00:00', '102', '0', '1', '2', '1', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', 'financial_contract_uuid_1', '0', '0', '0', '1', '1', '1', NULL, NULL, NULL, NULL, '0', '0', '0', '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'outstandingPrincipal', 'outstandingInterest', 'outstandingPenaltyInterest', NULL, '-1', '0', NULL, NULL, NULL);

  	
	/*'first_repayment_way_group还款类型分组：0：线上代扣,1:商户代扣,2:(委托转付,商户代偿，差额划拨，回购)，3:（主动还款，他人贷款）4：表外支付'5 线上代扣—快捷支付 */
  	INSERT INTO `repayment_order` (`id`, `order_uuid`, `order_unique_id`, `mer_id`, `order_request_no`,`first_repayment_way_group`, `order_alive_status`, `order_check_status`, `order_db_status`, `order_pay_status`, `order_recover_result`, `order_recover_status`, `paid_amount`, `order_amount`, `order_create_time`, `order_last_modified_time`, `order_notify_url`, `ip`, `financial_contract_uuid`, `financial_contract_no`, `financial_contract_name`, `order_detail_file_path`, `active_order_uuid`, `remark`)
  	VALUES
	(1, 'order_uuid_1', 'order_unique_id_1', 'mer_id_1', 'e573be60-f16c-4918-9e8c-b220561b9282',1, 0, 1, 0, 0, 0, 0, 0.00, 500.00, '2017-06-12 17:40:39', '2017-06-12 17:40:39', '', '127.0.0.1', 'financial_contract_uuid_1', 'G31700', '拍拍贷测试', '/home/dell/Desktop/repaymentOrderDetails/repayment_order_1497260439015062c99f4-ee0c-46ee-af5c-019ed8635eea.csv', 'empty_order_uuid', 'aaa'),
	(2, 'order_uuid_2', 'order_unique_id_2', 'mer_id_1', 'e573be60-f16c-4918-9e8c-b220561b9282',1, 0, 1, 0, 0, 0, 0, 0.00, 500.00, '2017-06-12 17:40:39', '2017-06-12 17:40:39', '', '127.0.0.1', 'financial_contract_uuid_1', 'G31700', '拍拍贷测试', '/home/dell/Desktop/repaymentOrderDetails/repayment_order_1497260439015062c99f4-ee0c-46ee-af5c-019ed8635eea.csv', 'empty_order_uuid', 'bbb'),
	(3, 'order_uuid_3', 'order_unique_id_3', 'mer_id_1', 'e573be60-f16c-4918-9e8c-b220561b9282',1, 0, 1, 0, 2, 0, 0, 0.00, 500.00, '2017-06-12 17:40:39', '2017-06-12 17:40:39', '', '127.0.0.1', 'financial_contract_uuid_1', 'G31700', '拍拍贷测试', '/home/dell/Desktop/repaymentOrderDetails/repayment_order_1497260439015062c99f4-ee0c-46ee-af5c-019ed8635eea.csv', 'empty_order_uuid', ''),
	(4, 'order_uuid_4', 'order_unique_id_4', 'mer_id_1', 'e573be60-f16c-4918-9e8c-b220561b9282',0, 0, 1, 0, 2, 0, 0, 0.00, 500.00, '2017-06-12 17:40:39', '2017-06-12 17:40:39', '', '127.0.0.1', 'financial_contract_uuid_1', 'G31700', '拍拍贷测试', '/home/dell/Desktop/repaymentOrderDetails/repayment_order_1497260439015062c99f4-ee0c-46ee-af5c-019ed8635eea.csv', 'empty_order_uuid', ''),
	(5, 'order_uuid_5', 'order_unique_id_5', 'mer_id_1', 'e573be60-f16c-4918-9e8c-b220561b9282',5, 0, 1, 0, 2, 0, 0, 0.00, 500.00, '2017-06-12 17:40:39', '2017-06-12 17:40:39', '', '127.0.0.1', 'financial_contract_uuid_1', 'G31700', '拍拍贷测试', '/home/dell/Desktop/repaymentOrderDetails/repayment_order_1497260439015062c99f4-ee0c-46ee-af5c-019ed8635eea.csv', 'empty_order_uuid', ''),
	(6, 'order_uuid_6', 'order_unique_id_6', 'mer_id_1', 'e573be60-f16c-4918-9e8c-b220561b9282',5, 0, 1, 0, 2, 0, 0, 0.00, 500.00, '2017-06-12 17:40:39', '2017-06-12 17:40:39', '', '127.0.0.1', 'financial_contract_uuid_1', 'G31700', '拍拍贷测试', '/home/dell/Desktop/repaymentOrderDetails/repayment_order_1497260439015062c99f4-ee0c-46ee-af5c-019ed8635eea.csv', 'empty_order_uuid', ''),
	(7, 'order_uuid_7', 'order_unique_id_7', 'mer_id_1', 'e573be60-f16c-4918-9e8c-b220561b9282',0, 0, 1, 0, 2, 0, 0, 0.00, 500.00, '2017-06-12 17:40:39', '2017-06-12 17:40:39', '', '127.0.0.1', 'financial_contract_uuid_1', 'G31700', '拍拍贷测试', '/home/dell/Desktop/repaymentOrderDetails/repayment_order_1497260439015062c99f4-ee0c-46ee-af5c-019ed8635eea.csv', 'empty_order_uuid', ''),
	(8, 'order_uuid_8', 'order_unique_id_8', 'mer_id_1', 'e573be60-f16c-4918-9e8c-b220561b9282',5, 0, 1, 0, 2, 0, 0, 0.00, 500.00, '2017-06-12 17:40:39', '2017-06-12 17:40:39', '', '127.0.0.1', 'financial_contract_uuid_1', 'G31700', '拍拍贷测试', '/home/dell/Desktop/repaymentOrderDetails/repayment_order_1497260439015062c99f4-ee0c-46ee-af5c-019ed8635eea.csv', 'empty_order_uuid', ''),
	(9, 'order_uuid_9', 'order_unique_id_9', 'mer_id_1', 'e573be60-f16c-4918-9e8c-b220561b9282',0, 0, 1, 0, 2, 0, 0, 0.00, 500.00, '2017-06-12 17:40:39', '2017-06-12 17:40:39', '', '127.0.0.1', 'financial_contract_uuid_1', 'G31700', '拍拍贷测试', '/home/dell/Desktop/repaymentOrderDetails/repayment_order_1497260439015062c99f4-ee0c-46ee-af5c-019ed8635eea.csv', 'empty_order_uuid', ''),
	(10, 'order_uuid_10', 'order_unique_id_10', 'mer_id_1', 'e573be60-f16c-4918-9e8c-b220561b9282',0, 0, 1, 0, 2, 0, 0, 0.00, 500.00, '2017-06-12 17:40:39', '2017-06-12 17:40:39', '', '127.0.0.1', 'financial_contract_uuid_1', 'G31700', '拍拍贷测试', '/home/dell/Desktop/repaymentOrderDetails/repayment_order_1497260439015062c99f4-ee0c-46ee-af5c-019ed8635eea.csv', 'empty_order_uuid', ''),
	(11, 'order_uuid_11', 'order_unique_id_11', 'mer_id_1', 'e573be60-f16c-4918-9e8c-b220561b9282',5, 0, 1, 0, 2, 0, 0, 0.00, 500.00, '2017-06-12 17:40:39', '2017-06-12 17:40:39', '', '127.0.0.1', 'financial_contract_uuid_1', 'G31700', '拍拍贷测试', '/home/dell/Desktop/repaymentOrderDetails/repayment_order_1497260439015062c99f4-ee0c-46ee-af5c-019ed8635eea.csv', 'empty_order_uuid', '');
	
	
	
	/*repayment_way: 还款方式：0:线上代扣，1:委托转付，2:商户担保、3：回购，4:差额划拨、5：商户代偿，6:主动付款、7:他人代偿、8:商户代扣*/
	INSERT INTO `repayment_order_item` (`id`, `order_detail_uuid`, `contract_unique_id`, `contract_no`, `contract_uuid`, `amount`, `detail_alive_status`, `detail_pay_status`, `mer_id`, `financial_contract_uuid`, `repayment_way`, `repayment_business_uuid`, `repayment_business_no`, `repayment_business_type`, `repayment_plan_time`, `order_uuid`, `order_unique_id`, `remark`)
	VALUES
	(1, 'order_detail_uuid_1', 'contract_unique_id_1', 'contract_no_1', 'contract_uuid_1', 400.00, 0, 0, 'mer_id_1', 'financial_contract_uuid_1', 8, 'asset_uuid_1', 'repayment_plan_no_1', 0, '2017-06-15 10:10:10', 'order_uuid_1', 'oder_unique_id_1', 'remark_1'),
	(2, 'order_detail_uuid_2', 'contract_unique_id_1', 'contract_no_1', 'contract_uuid_1', 100.00, 0, 0, 'mer_id_1', 'financial_contract_uuid_1', 8, 'asset_uuid_1', 'repayment_plan_no_1', 0, '2017-06-15 10:10:10', 'order_uuid_2', 'oder_unique_id_2', 'remark_1'),
	
	(3, 'order_detail_uuid_3', 'contract_unique_id_1', 'contract_no_1', 'contract_uuid_1', 400.00, 0, 0, 'mer_id_1', 'financial_contract_uuid_1', 8, 'asset_uuid_1', 'repayment_plan_no_1', 0, '2017-06-15 10:10:10', 'order_uuid_3', 'oder_unique_id_3', 'remark_1'),
	(4, 'order_detail_uuid_4', 'contract_unique_id_1', 'contract_no_1', 'contract_uuid_1', 100.00, 0, 0, 'mer_id_1', 'financial_contract_uuid_1', 8, 'asset_uuid_1', 'repayment_plan_no_1', 0, '2017-06-15 10:10:10', 'order_uuid_3', 'oder_unique_id_3', 'remark_1'),
	
	(5, 'order_detail_uuid_5', 'contract_unique_id_1', 'contract_no_1', 'contract_uuid_1', 400.00, 0, 0, 'mer_id_1', 'financial_contract_uuid_1', 8, 'asset_uuid_1', 'repayment_plan_no_1', 0, '2017-06-15 10:10:10', 'order_uuid_3', 'oder_unique_id_3', 'remark_1'),
	(6, 'order_detail_uuid_6', 'contract_unique_id_1', 'contract_no_1', 'contract_uuid_1', 100.00, 0, 0, 'mer_id_1', 'financial_contract_uuid_1', 8, 'asset_uuid_1', 'repayment_plan_no_1', 0, '2017-06-15 10:10:10', 'order_uuid_3', 'oder_unique_id_3', 'remark_1'),
	
	(7, 'order_detail_uuid_7', 'contract_unique_id_1', 'contract_no_1', 'contract_uuid_1', 500.00, 0, 0, 'mer_id_1', 'financial_contract_uuid_1', 8, 'asset_uuid_1', 'repayment_plan_no_1', 0, '2017-06-15 10:10:10', 'order_uuid_4', 'oder_unique_id_4', 'remark_1'),

	(8, 'order_detail_uuid_8', 'contract_unique_id_1', 'contract_no_1', 'contract_uuid_1', 500.00, 0, 0, 'mer_id_1', 'financial_contract_uuid_1', 8, 'asset_uuid_1', 'repayment_plan_no_1', 0, '2017-06-15 10:10:10', 'order_uuid_5', 'oder_unique_id_5', 'remark_1'),

	(9, 'order_detail_uuid_9', 'contract_unique_id_1', 'contract_no_1', 'contract_uuid_1', 400.00, 0, 0, 'mer_id_1', 'financial_contract_uuid_1', 8, 'asset_uuid_1', 'repayment_plan_no_1', 0, '2017-06-15 10:10:10', 'order_uuid_6', 'oder_unique_id_6', 'remark_1'),
	(10, 'order_detail_uuid_10', 'contract_unique_id_1', 'contract_no_1', 'contract_uuid_1', 100.00, 0, 0, 'mer_id_1', 'financial_contract_uuid_1', 8, 'asset_uuid_1', 'repayment_plan_no_1', 0, '2017-06-15 10:10:10', 'order_uuid_6', 'oder_unique_id_6', 'remark_1'),
	
	(11, 'order_detail_uuid_11', 'contract_unique_id_1', 'contract_no_1', 'contract_uuid_1', 500.00, 0, 0, 'mer_id_1', 'financial_contract_uuid_1', 8, 'asset_uuid_1', 'repayment_plan_no_1', 0, '2017-06-15 10:10:10', 'order_uuid_7', 'oder_unique_id_7', 'remark_1'),

	(12, 'order_detail_uuid_12', 'contract_unique_id_1', 'contract_no_1', 'contract_uuid_1', 400.00, 0, 0, 'mer_id_1', 'financial_contract_uuid_1', 8, 'asset_uuid_1', 'repayment_plan_no_1', 0, '2017-06-15 10:10:10', 'order_uuid_8', 'oder_unique_id_8', 'remark_1'),
	(13, 'order_detail_uuid_13', 'contract_unique_id_1', 'contract_no_1', 'contract_uuid_1', 100.00, 0, 0, 'mer_id_1', 'financial_contract_uuid_1', 8, 'asset_uuid_1', 'repayment_plan_no_1', 0, '2017-06-15 10:10:10', 'order_uuid_8', 'oder_unique_id_8', 'remark_1'),
	
	(14, 'order_detail_uuid_14', 'contract_unique_id_1', 'contract_no_1', 'contract_uuid_1', 400.00, 0, 0, 'mer_id_1', 'financial_contract_uuid_1', 8, 'asset_uuid_1', 'repayment_plan_no_1', 0, '2017-06-15 10:10:10', 'order_uuid_9', 'oder_unique_id_9', 'remark_1'),
	(15, 'order_detail_uuid_15', 'contract_unique_id_1', 'contract_no_1', 'contract_uuid_1', 100.00, 0, 0, 'mer_id_1', 'financial_contract_uuid_1', 8, 'asset_uuid_1', 'repayment_plan_no_1', 0, '2017-06-15 10:10:10', 'order_uuid_9', 'oder_unique_id_9', 'remark_1'),
	
	(16, 'order_detail_uuid_16', 'contract_unique_id_1', 'contract_no_1', 'contract_uuid_1', 500.00, 0, 0, 'mer_id_1', 'financial_contract_uuid_1', 8, 'asset_uuid_1', 'repayment_plan_no_1', 0, '2017-06-15 10:10:10', 'order_uuid_10', 'oder_unique_id_10', 'remark_1'),
	
	(17, 'order_detail_uuid_17', 'contract_unique_id_1', 'contract_no_1', 'contract_uuid_1', 400.00, 0, 0, 'mer_id_1', 'financial_contract_uuid_1', 8, 'asset_uuid_1', 'repayment_plan_no_1', 0, '2017-06-15 10:10:10', 'order_uuid_11', 'oder_unique_id_11', 'remark_11'),
	(18, 'order_detail_uuid_18', 'contract_unique_id_1', 'contract_no_1', 'contract_uuid_1', 100.00, 0, 0, 'mer_id_1', 'financial_contract_uuid_1', 8, 'asset_uuid_1', 'repayment_plan_no_1', 0, '2017-06-15 10:10:10', 'order_uuid_11', 'oder_unique_id_11', 'remark_11');
	
	
	
	INSERT INTO `repayment_order_item_check_fail_log` (`id`, `order_detail_uuid`, `contract_unique_id`, `contract_no`, `amount`,`repayment_business_no` , `mer_id`, `financial_contract_uuid`,`repayment_business_type`, `order_uuid`, `order_unique_id`, `remark`)
	VALUES
	(1, 'order_detail_uuid_1', 'contract_unique_id_1', 'contract_no_1', 500.00, 'repayment_plan_no_2', 'mer_id_1', 'financial_contract_uuid_1', 0,'order_uuid_1', 'oder_unique_id_1', 'remark_2'),
	(2, 'order_detail_uuid_2', 'contract_unique_id_1', 'contract_no_1', 600.00, 'repayment_plan_no_2', 'mer_id_1', 'financial_contract_uuid_1', 0, 'order_uuid_2', 'oder_unique_id_2', 'remark_2');
	

SET FOREIGN_KEY_CHECKS=1;