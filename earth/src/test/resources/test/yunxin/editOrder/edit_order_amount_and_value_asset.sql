DELETE FROM `contract`;
DELETE FROM `asset_set`;
DELETE FROM `asset_valuation_detail`;
DELETE FROM `rent_order`;
DELETE FROM `financial_contract`;
DELETE FROM `payment_channel`;
DELETE FROM `asset_package`;
delete from `customer`;
delete from `company`;
delete from `ledger_book`;
delete from `ledger_book_shelf`;
delete from `asset_set_extra_charge`;


INSERT INTO `contract` (`id`, `begin_date`, `contract_no`, `end_date`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`,asset_type)
VALUES
	(1, '2016-03-01', 'DKHD-001', '2017-03-15', 10000.00, 1, 1, 1, NULL, '2016-03-15 13:34:35', 0.0010000000, 1, 1, 10, 0, 100000, 0.0005000000,1);

INSERT INTO `asset_package` (`id`, `is_available`,  `contract_id`, `asset_package_no`, `financial_contract_id`) VALUES 
('1', 1, 1, 'no1', '1');

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`) VALUES 
('1', NULL, NULL, '测试用户1', 'C74211', '2', 'customer_uuid_1');

INSERT INTO `company` (`id`) VALUES
('1');

INSERT INTO `ledger_book` (`id`, `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`) VALUES 
('1', 'ledger_book_no_1', '1', NULL, NULL),
('2', 'ledger_book_no_2', '2', NULL, NULL);


INSERT INTO `financial_contract` (`adva_repayment_term`,`id`, `adva_matuterm`, `contract_no`, `app_id`, `company_id`,   `adva_repo_term`, `payment_channel_id`,`sys_create_penalty_flag`,`ledger_book_no`) VALUES 
('0','1', 3, 'YX_AMT_001', 1, 1, 30,1,1,'ledger_book_no_1'),
('0','2', 3, 'YX_AMT_002', 2, 1, 30,1,1,'ledger_book_no_2');

INSERT INTO `payment_channel` (`id`, `channel_name`, `user_name`, `user_password`, `merchant_id`, `cer_file_path`, `pfx_file_path`, `pfx_file_key`, `payment_channel_type`, `api_url`, `from_date`, `thru_date`) VALUES 
(1, 'channel_name', 'user_name', 'user_password', 'merchant_id', 'cer_file_path', 'pfx_file_path', 'pfx_file_key', 0, 'api_url', NULL, NULL);


INSERT INTO `asset_set` (`id`, `asset_fair_value`,`asset_initial_value`,`asset_principal_value`,`asset_interest_value`, `asset_recycle_date`, `asset_status`, `asset_uuid`, `create_time`, `last_modified_time`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`,`version_no`)
VALUES
	(1, 10010.00,10000.00,10000.00,0,'2016-05-16', 0, 'asset_uuid_1', '2016-05-16 13:34:35', '2016-05-16 13:34:35', 'DKHD-001-01', 1, NULL,1),
	(2, 10000.00,10000.00,10000.00,0,'2016-05-17', 0, 'asset_uuid_2', '2016-05-17 13:34:35', '2016-05-17 13:34:35', 'DKHD-002-01', 2, NULL,1);

INSERT INTO `asset_valuation_detail` (`id`, `amount`, `asset_value_date`, `created_date`, `subject`, `asset_set_id`)
VALUES
	(1, 10000.00, '2016-05-16', '2016-05-16', 0, 1),
	(2, 10000.00, '2016-05-17', '2016-05-17', 0, 2),
	(3, 5.00, '2016-05-17', '2016-05-17', 1, 1),
	(4, 5.00, '2016-05-18', '2016-05-18', 1, 1),
	(5, 5.00, '2016-05-18', '2016-05-18', 1, 2);

INSERT INTO `rent_order` (`id`, `order_type`, `due_date`, `order_no`, `payout_time`, `total_rent`, `customer_id`, `user_upload_param`, `modify_time`, `financial_contract_id`, `repayment_bill_id`, `asset_set_id`, `create_time`, `clearing_status`, `executing_settling_status`) VALUES 
('1', '0', Date_add(now(),INTERVAL -2 DAY), 'order_no_1', NULL, '10000.00', '1', '[1]', Date_add(now(),INTERVAL -2 DAY), '2', 'repayment_bill_id_1', '1', Date_add(now(),INTERVAL -2 DAY), '1', '2'),
('2', '0', Date_add(now(),INTERVAL -1 DAY), 'order_no_2', NULL, '10000.00', '1', '[1,3]', Date_add(now(),INTERVAL -1 DAY), '2', 'repayment_bill_id_2', '1', Date_add(now(),INTERVAL -1 DAY), '0', '3'),
('3', '0', now(), 'order_no_3', NULL, '10010.00', '1', '[1,3,4]',now(), '2', 'repayment_bill_id_3', '1', now(), '0', '0');

INSERT INTO `ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`) VALUES 
('1', 'ledger_uuid_1', '0.00', '900.00', 'FST_LONGTERM_LIABILITY', '40000', '0', 'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL, '1', NULL, NULL, NULL, NULL, '871562dd-5a0f-4a8d-990f-07d75bfc0304', '2016-05-01', '2016-05-16 14:26:50', '', NULL, '7', NULL, '2016-05-27 00:00:00', '', 'yunxin_ledger_book', '1', '1', 'repayment_plan_no_1', 'd2c21631-f2fb-4e8f-beef-0a2e72447434', NULL, NULL, NULL, NULL, ''),
('2', 'ledger_uuid_2', '0.00', '10005.00', 'FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS', '50000', '0', 'SND_LIABILITIES_INDEPENDENT_CUSTOMER_DEPOSIT', '50000.01', NULL, NULL, 'company_customer_uuid_1', '1', NULL, NULL, NULL, '51b062d9-beff-4688-a284-1b6c34ef3d06', NULL, '2016-08-27 16:57:09', '90f15a7d-78f9-4b42-985d-ea9f47ece17d', NULL, NULL, NULL, NULL, 'c2d731d8-fdfc-4ba1-8e70-4706eaa75d96', 'yunxin_ledger_book', '1', '1', NULL, NULL, NULL, NULL, NULL, NULL, '2bc312f7-c0a9-460c-80aa-052fa4ee7e99'),
('3', 'ledger_uuid_3', '10000.00', '0.00', 'FST_RECIEVABLE_ASSET', '20000', '1', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', NULL, NULL, '1', 'customerUuid1', NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '616', NULL, '2016-08-28 00:00:00', NULL, 'yunxin_ledger_book', '1', '1', 'test_single_no', 'asset_uuid_1', NULL, NULL, NULL, NULL, NULL);

