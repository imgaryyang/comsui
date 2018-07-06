delete from `app`;
delete from `contract`;
delete from `customer`;
delete from `house`;
delete from `company`;
delete from `asset_package`;
delete from `asset_set`;
delete from `contract_account`;
delete from  `t_deduct_application`;
delete from  `t_deduct_plan`;
delete from  `t_deduct_application_detail`;

INSERT INTO `contract` (`id`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`,`active_version_no`)
VALUES
	(24, NULL, '2016-04-17', '629测试(ZQ2016002000001)', NULL, 1, 0.00, 2, 116, 116, NULL, '2016-06-29 18:14:01', 0.1560000000, 0, 0, 1, 2, 100.00, 0.0005000000,1),
	(25, NULL, '2016-04-17', '629测试(ZQ2016002000002)', NULL, 1, 0.00, 2, 116, 116, NULL, '2016-06-29 18:14:01', 0.1560000000, 0, 0, 1, 2, 100.00, 0.0005000000,1);

INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`,`version_no`, `active_status`)
VALUES
	(25, 1, 0, 200.20, 0.00, 200.00, 200.00, '2016-06-01', NULL, 0.00, 0, 1, 0, '2016-06-30 14:03:03', '88450378-e6fd-4857-9562-22971b05b932', '2016-06-29 18:14:01', '2016-06-30 14:03:03', NULL, 'ZC27375ACFF4234804', 25, NULL, 1, 1, NULL,1, 0),
	(24, 1, 0, 200.20, 0.00, 200.00, 200.00, '2016-06-01', NULL, 0.00, 0, 1, 0, '2016-06-30 14:03:03', '88450378-e6fd-4857-9562-22971b05b932', '2016-06-29 18:14:01', '2016-06-30 14:03:03', NULL, 'ZC27375ACFF4234805', 24, NULL, 1, 1, NULL,1, 1);

	
INSERT INTO `asset_package` (`id`, `is_available`, `create_time`, `contract_id`, `asset_package_no`, `financial_contract_id`, `loan_batch_id`)
VALUES
	(47, NULL, NULL, 24, NULL, 14, 14),
	(48, NULL, NULL, 25, NULL, 14, 14);
	
INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`)
VALUES
	(2, 'nongfenqi', '11111db75ebb24fa0993f4fa25775022', 00000000, 'http://e.zufangbao.cn', '农分期', 4, NULL);

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`)
VALUES
	(116, NULL, NULL, '测试用户00101', 'C74211', 2, '4faef2f9-1155-4ca8-bd22-bf6230bbc72c');

INSERT INTO `house` (`id`, `address`, `app_id`)
VALUES
	(116, NULL, 2);
	
INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`)
VALUES
	(4, '杭州', '南京农纷期电子商务有限公司', '农分期');
	
	INSERT INTO `contract_account` (`id`, `pay_ac_no`, `bankcard_type`, `contract_id`, `payer_name`, `bank`, `bind_id`, `id_card_num`, `bank_code`, `province`, `city`, `from_date`, `thru_date`)
VALUES 
	(24, '6217000000000003006', NULL, 24, '测试用户00101', '中国邮政储蓄银行', NULL, NULL, '403', '安徽省', '亳州市', '2016-04-17 00:00:00', '2900-01-01 00:00:00'),
	(25, '6217000000000003007', NULL, 25, '测试用户00102', '中国邮政储蓄银行', NULL, NULL, '403', '江苏省', '淮安市', '2016-04-17 00:00:00', '2900-01-01 00:00:00');

INSERT INTO `t_deduct_application_detail` (`id`, `deduct_application_detail_uuid`, `deduct_application_uuid`, `financial_contract_uuid`, `contract_unique_id`, `repayment_plan_code`, `asset_set_uuid`, `request_no`, `repayment_type`, `transaction_type`, `create_time`, `execution_remark`, `creator_name`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`, `is_total`)
VALUES 
('176', '5b6b91e3-4f5f-4d08-aafe-ab268b7499bd', '00bcd5a0-30a5-43d3-9d15-a8886222f49e', '984149f1-cb43-410c-a789-d8f4bba123b6', NULL, 'ZC27375ACFF4234805', '1234567', '2a95fe6e-dd47-4f68-8b6d-357a114927fb', '0', '1', '2016-08-24 14:33:13', '', 't_test_zfb', '2016-08-24 14:33:13', 'FST_RECIEVABLE_ASSET', '20000', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_PRINCIPLE', '20000.01.01', '0.00', '1'),
 ('177', '5b6b91e3-4f5f-4d08-aafe-ab268b7499bd', '00bcd5a0-30a5-43d3-9d15-a8886222f49e', '984149f1-cb43-410c-a789-d8f4bba123b6', NULL, 'ZC27375ACFF4234805', '1234567', '2a95fe6e-dd47-4f68-8b6d-357a114927fb', '0', '1', '2016-08-24 14:33:13', '', 't_test_zfb', '2016-08-24 14:33:13', 'FST_RECIEVABLE_ASSET', '20000', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_INTEREST', '20000.01.02', '100.00', '1'),
 ('178', '5b6b91e3-4f5f-4d08-aafe-ab268b7499bd', '00bcd5a0-30a5-43d3-9d15-a8886222f49e', '984149f1-cb43-410c-a789-d8f4bba123b6', NULL, 'ZC27375ACFF4234805', '1234567', '2a95fe6e-dd47-4f68-8b6d-357a114927fb', '0', '1', '2016-08-24 14:33:13', '', 't_test_zfb', '2016-08-24 14:33:13', 'FST_RECIEVABLE_ASSET', '20000', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_TECH_FEE', '20000.01.04', '0.00', '1'),
 ('179', '5b6b91e3-4f5f-4d08-aafe-ab268b7499bd', '00bcd5a0-30a5-43d3-9d15-a8886222f49e', '984149f1-cb43-410c-a789-d8f4bba123b6', NULL, 'ZC27375ACFF4234805', '1234567', '2a95fe6e-dd47-4f68-8b6d-357a114927fb', '0', '1', '2016-08-24 14:33:13', '', 't_test_zfb', '2016-08-24 14:33:13', 'FST_RECIEVABLE_ASSET', '20000', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_LOAN_SERVICE_FEE', '20000.01.03', '0.00', '1'),
('180', '5b6b91e3-4f5f-4d08-aafe-ab268b7499bd', '00bcd5a0-30a5-43d3-9d15-a8886222f49e', '984149f1-cb43-410c-a789-d8f4bba123b6', NULL, 'ZC27375ACFF4234805', '1234567', '2a95fe6e-dd47-4f68-8b6d-357a114927fb', '0', '1', '2016-08-24 14:33:13', '', 't_test_zfb', '2016-08-24 14:33:13', 'FST_RECIEVABLE_ASSET', '20000', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_OTHER_FEE', '20000.01.05', '0.00', '1'),
('181', '5b6b91e3-4f5f-4d08-aafe-ab268b7499bd', '00bcd5a0-30a5-43d3-9d15-a8886222f49e', '984149f1-cb43-410c-a789-d8f4bba123b6', NULL, 'ZC27375ACFF4234805', '1234567', '2a95fe6e-dd47-4f68-8b6d-357a114927fb', '0', '1', '2016-08-24 14:33:13', '', 't_test_zfb', '2016-08-24 14:33:13', 'FST_RECIEVABLE_ASSET', '20000', 'SND_RECIEVABLE_LOAN_PENALTY', '20000.03', NULL, NULL, '0.00', '1'),
('182', '5b6b91e3-4f5f-4d08-aafe-ab268b7499bd', '00bcd5a0-30a5-43d3-9d15-a8886222f49e', '984149f1-cb43-410c-a789-d8f4bba123b6', NULL, 'ZC27375ACFF4234805', '1234567', '2a95fe6e-dd47-4f68-8b6d-357a114927fb', '0', NULL, '2016-08-24 14:33:13', '', 't_test_zfb', '2016-08-24 14:33:13', NULL, NULL, NULL, NULL, NULL, NULL, '100.00', '0'),
('183', '90bf5fa7-7564-4045-9459-04bd0905463d', '7df23435-2624-432e-a6bc-ec797280bdb5', '984149f1-cb43-410c-a789-d8f4bba123b6', NULL, 'ZC27375ACFF4234805', '1234567', '3e29f1c1-91a7-441d-aea4-2ee3c34c05d1', '0', '1', '2016-08-24 14:33:51', '', 't_test_zfb', '2016-08-24 14:33:51', 'FST_RECIEVABLE_ASSET', '20000', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_PRINCIPLE', '20000.01.01', '0.00', '1'),
 ('184', '90bf5fa7-7564-4045-9459-04bd0905463d', '7df23435-2624-432e-a6bc-ec797280bdb5', '984149f1-cb43-410c-a789-d8f4bba123b6', NULL, 'ZC27375ACFF4234805', '1234567', '3e29f1c1-91a7-441d-aea4-2ee3c34c05d1', '0', '1', '2016-08-24 14:33:51', '', 't_test_zfb', '2016-08-24 14:33:51', 'FST_RECIEVABLE_ASSET', '20000', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_INTEREST', '20000.01.02', '100.00', '1'),
('185', '90bf5fa7-7564-4045-9459-04bd0905463d', '7df23435-2624-432e-a6bc-ec797280bdb5', '984149f1-cb43-410c-a789-d8f4bba123b6', NULL, 'ZC27375ACFF4234805', '1234567', '3e29f1c1-91a7-441d-aea4-2ee3c34c05d1', '0', '1', '2016-08-24 14:33:51', '', 't_test_zfb', '2016-08-24 14:33:51', 'FST_RECIEVABLE_ASSET', '20000', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_TECH_FEE', '20000.01.04', '0.00', '1'),
('186', '90bf5fa7-7564-4045-9459-04bd0905463d', '7df23435-2624-432e-a6bc-ec797280bdb5', '984149f1-cb43-410c-a789-d8f4bba123b6', NULL, 'ZC27375ACFF4234805', '1234567', '3e29f1c1-91a7-441d-aea4-2ee3c34c05d1', '0', '1', '2016-08-24 14:33:51', '', 't_test_zfb', '2016-08-24 14:33:51', 'FST_RECIEVABLE_ASSET', '20000', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_LOAN_SERVICE_FEE', '20000.01.03', '0.00', '1'),
('187', '90bf5fa7-7564-4045-9459-04bd0905463d', '7df23435-2624-432e-a6bc-ec797280bdb5', '984149f1-cb43-410c-a789-d8f4bba123b6', NULL, 'ZC27375ACFF4234805', '1234567', '3e29f1c1-91a7-441d-aea4-2ee3c34c05d1', '0', '1', '2016-08-24 14:33:51', '', 't_test_zfb', '2016-08-24 14:33:51', 'FST_RECIEVABLE_ASSET', '20000', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_OTHER_FEE', '20000.01.05', '0.00', '1'),
('188', '90bf5fa7-7564-4045-9459-04bd0905463d', '7df23435-2624-432e-a6bc-ec797280bdb5', '984149f1-cb43-410c-a789-d8f4bba123b6', NULL, 'ZC27375ACFF4234805', '1234567', '3e29f1c1-91a7-441d-aea4-2ee3c34c05d1', '0', '1', '2016-08-24 14:33:51', '', 't_test_zfb', '2016-08-24 14:33:51', 'FST_RECIEVABLE_ASSET', '20000', 'SND_RECIEVABLE_LOAN_PENALTY', '20000.03', NULL, NULL, '0.00', '1'),
('189', '90bf5fa7-7564-4045-9459-04bd0905463d', '7df23435-2624-432e-a6bc-ec797280bdb5', '984149f1-cb43-410c-a789-d8f4bba123b6', NULL, 'ZC27375ACFF4234805', '1234567', '3e29f1c1-91a7-441d-aea4-2ee3c34c05d1', '0', NULL, '2016-08-24 14:33:51', '', 't_test_zfb', '2016-08-24 14:33:51', NULL, NULL, NULL, NULL, NULL, NULL, '100.00', '0');


INSERT INTO `t_deduct_application` (`id`, `deduct_application_uuid`, `deduct_id`, `request_no`, `financial_contract_uuid`, `financial_product_code`, `contract_unique_id`, `repayment_plan_code_list`, `contract_no`, `planned_deduct_total_amount`, `actual_deduct_total_amount`, `notify_url`, `transcation_type`, `repayment_type`, `execution_status`, `execution_remark`, `create_time`, `creator_name`, `ip`, `last_modified_time`, `record_status`, `is_available`, `api_called_time`, `transaction_recipient`) 
VALUES 
('31', '00bcd5a0-30a5-43d3-9d15-a8886222f49e', '1087e301-bd27-45b4-8368-3137b9e6e12c', '4eecd64a-6f81-44b7-94dd-77f1fb189ed5', '984149f1-cb43-410c-a789-d8f4bba123b6', 'G00001', NULL, '[\"ZC27375ACFF4234805\"]', '629测试(ZQ2016002000001)', '100.00', '0.00', '', '1', '0', '1', '', '2016-08-25 17:14:31', 't_test_zfb', '127.0.0.1', '2016-08-26 21:23:54', '1', '0', '2016-05-04 00:00:00', '1'),
('38', '7df23435-2624-432e-a6bc-ec797280bdb5', '3', '2', '984149f1-cb43-410c-a789-d8f4bba123b6', 'G00001', NULL, '[\"ZC27375ACFF4234805\"]', '629测试(ZQ2016002000001)', '100.00', '0.00', '', '1', '0', '1', '', '2016-08-29 16:30:58', NULL, '127.0.0.1', '2016-08-29 16:30:58', '1', '0', '2016-03-05 00:00:00', '0');


INSERT INTO `t_deduct_plan` (`id`, `deduct_plan_uuid`, `deduct_application_uuid`, `deduct_application_detail_uuid`, `financial_contract_uuid`, `contract_unique_id`, `contract_no`, `payment_gateway`, `payment_channel_uuid`, `pg_account`, `pg_clearing_account`, `transaction_type`, `transaction_remark`, `cp_bank_code`, `cp_bank_card_no`, `cp_bank_account_holder`, `cp_id_type`, `cp_id_number`, `cp_bank_province`, `cp_bank_city`, `cp_bank_name`, `planned_payment_date`, `complete_payment_date`, `planned_total_amount`, `actual_total_amount`, `execution_precond`, `execution_status`, `execution_remark`, `create_time`, `creator_name`, `last_modified_time`, `transaction_serial_no`)
VALUES 
     ('26', '78a70c4f-6e95-419e-b3ed-cee5b50a8c91', '00bcd5a0-30a5-43d3-9d15-a8886222f49e', NULL, '984149f1-cb43-410c-a789-d8f4bba123b6', NULL, '629测试(ZQ2016002000001)', NULL, 'debit_test_uuid', NULL, NULL, '1', NULL, '403', '6217000000000003006', '测试用户00101', '0', NULL, '130000', '130001', '中国邮政储蓄银行', NULL, NULL, '100.00', '0.00', NULL, '1', '系统繁忙，请稍后再试', '2016-08-24 14:33:13', 't_test_zfb', '2016-08-24 14:33:50', NULL),
     ('27', '78a70c4f-6e95-419e-b3ed-cee5b50a8c88', '00bcd5a0-30a5-43d3-9d15-a8886222f49e', NULL, '984149f1-cb43-410c-a789-d8f4bba123b6', NULL, '629测试(ZQ2016002000001)', NULL, 'debit_test_uuid', NULL, NULL, '1', NULL, '403', '6217000000000003006', '测试用户00101', '0', NULL, '130000', '130001', '中国邮政储蓄银行', NULL, NULL, '100.00', '0.00', NULL, '1', '系统繁忙，请稍后再试', '2016-08-24 14:33:13', 't_test_zfb', '2016-08-24 14:33:50', NULL);
