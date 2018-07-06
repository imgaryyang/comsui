SET FOREIGN_KEY_CHECKS=0;
delete from `contract`;
delete from `app`;
delete from `customer`;
delete from `house`;
delete from `asset_set`;
delete from `company`;
delete from `asset_set_extra_charge`;
delete from `financial_contract`;
delete from `transfer_application`;
delete from `rent_order`;
delete from `batch_pay_record`;
delete from `contract_account`;
delete from `repayment_plan_extra_data`;

INSERT INTO `contract` (`id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`,`active_version_no`)
VALUES 
('1', '2016-04-17', '云信信2016-78-DK(ZQ2016042522478)', NULL, '1', '0.00', '1', '337', '337', NULL, '2016-06-15 22:04:59', '0.1560000000', '0', '0', '1', '2', '1200.00', '0.0005000000',1),
('323', '2016-04-17', '云信信2016-78-DK(ZQ2016042522479)', NULL, '1', '0.00', '1', '337', '337', NULL, '2016-06-15 22:04:59', '0.1560000000', '0', '0', '1', '2', '1200.00', '0.0005000000',1);

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`) 
VALUES
('1', 'nongfenqi', '', '', '', '农分期', '2', NULL);

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`) 
VALUES 
('337', NULL, NULL, '测试用户1', 'C74211', '1', '6120dce9-6214-433f-a7bd-acf9f89e2b7c');

INSERT INTO `house` (`id`, `address`, `app_id`)
VALUES 
('337', NULL, '1');

-- asset2为已作废的asset
INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`,`contract_uuid`,`outer_repayment_plan_no`,`repay_schedule_no`) 
VALUES 
	('1', '1', '0', '1217.40', '0.00', '1200.00', '1200.00', '2016-06-02', NULL, '0.00', '0', '1', '0', '2016-06-15 22:13:45', 'e86cba4c-b447-4e05-89c2-4fb339b4f888', '2016-06-02 22:04:59', '2016-06-02 23:24:31', NULL, 'ZC27367D23EF4A36F3', '1', NULL, '1', '0', NULL, '1', '0', '0', 'empty_deduct_uuid', NULL, 'b674a323-0c30-4e4b-9eba-b14e05a9d80a','111','repayScheduleNo1','repayScheduleNo1MD5');





INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, `interest_rate_cycle`) 
VALUES 
	('7', '111', '123', '2016-06-02', '12321', '2016-06-02', '1', '1', '1', '1', '1', '2016-06-02 12:31:24', '2016-06-02 12:31:11', '1', '1', '1', '1', '1', '1', '1', '1', '1', '2', 'b674a323-0c30-4e4b-9eba-b14e05a9d80a', '0');

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`) 
VALUES 
('2', '南京', '南京农纷期电子商务有限公司', '农分期');

INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`,adva_repayment_term)
VALUES
	(37, 0, 3, '2016-05-17 00:00:00', '云信信2016-78-DK(ZQ2016042522479)', '用钱宝测试', 2, 1, 60, '2017-12-01 00:00:00', 101, 0, 1, 2, 1, 'e17d8068-46a0-48bb-b158-8e80168bbfc3', 'b674a323-0c30-4e4b-9eba-b14e05a9d80a', 0, 0, 0, 0, 1, 0,0),
	(38, 0, 3, '2016-09-01 00:00:00', 'G32001', '用钱宝测试', 3, 1, 60, '2017-12-01 00:00:00', 101, 0, 1, 2, 1, 'e17d8068-46a0-48bb-b158-8e80168bbfc3', 'b674a323-0c30-4e4b-9eba-b14e05a9d80b', 0, 0, 0, 0, 1, 0,0);

INSERT INTO `transfer_application` (`id`, `transfer_application_uuid`, `amount`, `batch_pay_record_id`, `comment`, `create_time`, `creator_id`, `deduct_status`, `payment_way`, `executing_deduct_status`, `transfer_application_no`, `order_id`, `deduct_time`, `last_modified_time`, `contract_account_id`, `union_pay_order_no`, `payment_channel_uuid`, `deduct_send_time`, `financial_contract_id`)
VALUES
	(1, '09056ae58eb04046a8aaa176662c152f', 1200.00, 7, '账号错误', '2016-06-02 17:18:52', 1, 0, 0, 3, '27486A3E3F07344F', 4, '2016-06-02 17:19:14', '2016-06-02 17:19:14', 7, '27486A3E3F07344F', 1, '2016-06-02 17:18:52', '37');

INSERT INTO `rent_order` (`id`, `order_type`, `due_date`, `order_no`, `payout_time`, `total_rent`, `customer_id`, `user_upload_param`, `modify_time`, `financial_contract_id`, `repayment_bill_id`, `asset_set_id`, `create_time`, `clearing_status`, `executing_settling_status`, `comment`) 
VALUES 
	('4', '1', '2016-05-17 00:00:00', '2016-05-17 00:00:00', '2016-05-17 00:00:00', '20.00', '1', '1', '2016-05-17 00:00:00', '37', '1', '1', '2016-05-17 00:00:00', '1', '1', 'sds');

INSERT INTO `batch_pay_record` (`id`, `batch_pay_record_uuid`, `amount`, `modify_time`, `create_time`, `pay_time`, `request_no`, `serial_no`, `request_data`, `response_data`, `query_response_data`, `trans_date_time`) 
VALUES
	('7', '23232', '20.00', '2016-05-17 00:00:00', '2016-05-17 00:00:00', '2016-05-17 00:00:00', '21', '212', '2321', '232', '231', '2016-05-17 00:00:00');

INSERT INTO `contract_account` (`id`, `pay_ac_no`, `bankcard_type`, `contract_id`, `payer_name`, `bank`, `bind_id`, `id_card_num`, `bank_code`, `province`, `province_code`, `city`, `city_code`, `standard_bank_code`, `from_date`, `thru_date`) 
VALUES 
	('7', '6217000000000003006', NULL, '54340', '测试用户1', '中国邮政储蓄银行', NULL, '6217000000000003006', '403', '安徽省', NULL, '亳州', NULL, NULL, '2016-04-17 00:00:00', '2900-01-01 00:00:00');
	
SET FOREIGN_KEY_CHECKS=1;