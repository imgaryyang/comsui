delete from asset_set;
delete from contract;
delete from app;
delete from customer;
delete from house;
delete from asset_package;
delete from financial_contract;
delete from ledger_book;
delete from `company`;
delete from `repayment_order_item_charge`;
delete from `repayment_order_item`;

INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`,
		`app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`,
		`loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`,`adva_repayment_term`) 
VALUES 
		('14', '0', '3', '1900-01-01 00:00:00', 'G00001', '放款测试用信托合同', 
		'1', '1', '1', '2900-01-01 00:00:00', '1', '0', '1', 
		'90', '1', 'a5e84b90-e55c-4bba-9b9e-3463d36a01e0', '984149f1-cb43-410c-a789-d8f4bba123b6','0');

INSERT INTO `asset_package` (`id`, `is_available`, `create_time`, `contract_id`, `asset_package_no`, `financial_contract_id`, `loan_batch_id`) 
VALUES ('47', NULL, NULL, '24', NULL, '14', '14');


INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, 
		`asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`,
		`create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, 
		`total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`,`financial_contract_uuid`)
VALUES 
		('24', NULL, '123456', '2016-04-17', '629测试(ZQ2016002000001)', NULL, 
		'1', '0.00', '2', '116', '116', NULL, 
		'2016-06-29 18:14:01', '0.1560000000', '0', '0', '1', '2', 
		'100.00', '0.0005000000', '1', NULL, '2','984149f1-cb43-410c-a789-d8f4bba123b6');


INSERT INTO  `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, 
		`asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, 
		`repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, 
		`single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, 
		`version_no`, `active_status`,`financial_contract_uuid`,`order_payment_status`) 
VALUES 
		('24', '1', '0', '200.20', '0.00', '200.00', 
		'200.00', NOW(), NULL, '0.00', '0', '1', 
		'0', '2016-06-30 14:03:03', '88450378-e6fd-4857-9562-22971b05b932', '2016-06-29 18:14:01', '2016-06-30 14:03:03', NULL,
		'ZC2730FAE4092E0A6E', '24', NULL, '1', '1', NULL, 
		'1', '0','984149f1-cb43-410c-a789-d8f4bba123b6','1');

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`,
		`company_id`, `addressee`) 
VALUES
		('2', 'nongfenqi', '11111db75ebb24fa0993f4fa25775022', '\0', 'http://e.zufangbao.cn', '农分期', 
		'4', NULL);

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`)
VALUES 
		('116', NULL, NULL, '测试用户00101', 'C74211', '2', '4faef2f9-1155-4ca8-bd22-bf6230bbc72c');


INSERT INTO  `house` (`id`, `address`, `app_id`) VALUES ('116', NULL, '2');

insert into `ledger_book` ( `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`) values('a5e84b90-e55c-4bba-9b9e-3463d36a01e0','1',NULL,NULL);

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`)
VALUES 
	('1', '南京', '测试商务公司', '测试分期', '984149f1-cb43-410c-a789-d8f4bba123b6');

	
	
INSERT INTO `repayment_order_item` (`id`, `order_detail_uuid`, `contract_unique_id`, `contract_no`, `contract_uuid`, `amount`, `detail_alive_status`, `detail_pay_status`, `mer_id`, `financial_contract_uuid`, `repayment_way`, `repayment_business_uuid`, `repayment_business_no`, `repayment_business_type`, `repayment_plan_time`, `order_uuid`, `order_unique_id`, `remark`)
VALUES
	(1, 'order_detail_uuid_1', 'contract_unique_id_1', 'contract_no_1', 'contract_uuid_1', 1500.00, 0, 0, 'mer_id_1', '984149f1-cb43-410c-a789-d8f4bba123b6', 0, '88450378-e6fd-4857-9562-22971b05b932', 'ZC2730FAE4092E0A6E', 0, '2017-06-15 10:10:10', 'order_uuid_1', 'oder_unique_id_1', 'remark_1');	

INSERT INTO `repayment_order_item_charge` (`id`, `uuid`, `repayment_order_item_uuid`, `contract_uuid`, `repayment_business_no`, `repayment_business_uuid`, `order_uuid`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`, `create_time`, `last_modified_time`, `string_field_1`, `string_field_2`, `string_field_3`, `date_field_1`, `date_field_2`, `date_field_3`, `decimal_field_1`, `decimal_field_2`, `decimal_field_3`) VALUES 
('1', 'repayment_order_item_charge_uuid_1', 'order_detail_uuid_1', 'contract_uuid_1', 'ZC2730FAE4092E0A6E', '88450378-e6fd-4857-9562-22971b05b932', 'order_uuid_1', 'FST_RECIEVABLE_ASSET', '20000', 'SND_RECIEVABLE_LOAN_PENALTY', '20000.03', NULL, NULL, '900.00', '2017-06-28 16:38:17', '2017-06-28 16:38:17', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
	
set FOREIGN_KEY_CHECKS = 1;