SET FOREIGN_KEY_CHECKS=0;
delete from `app`;
delete from `contract`;
delete from `customer`;
delete from `house`;
delete from `company`;
delete from `asset_package`;
delete from `asset_set`;
delete from `contract_account`;
delete from `t_deduct_application`;
delete from `financial_contract`;
delete from `t_deduct_application_detail`;
delete from `ledger_book_shelf`;
delete from `ledger_book`;

INSERT INTO `contract` (`id`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`,`active_version_no`,`financial_contract_uuid`)
VALUES
	(24, NULL, '2016-04-17', '629测试(ZQ2016002000001)', NULL, 1, 0.00, 2, 116, 116, NULL, '2016-06-29 18:14:01', 0.1560000000, 0, 0, 1, 2, 100.00, 0.0005000000,1,'d2812bc5-5057-4a91-b3fd-9019506f0499'),
	(25, NULL, '2016-04-17', '629测试(ZQ2016002000002)', NULL, 1, 0.00, 2, 116, 116, NULL, '2016-06-29 18:14:01', 0.1560000000, 0, 0, 1, 2, 100.00, 0.0005000000,1,'d2812bc5-5057-4a91-b3fd-9019506f0499');

INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`,`version_no`, `active_status`,`sync_status`,`active_deduct_application_uuid`)
VALUES
	(25, 1, 0, 200.20, 0.00, 200.00, 200.00, '2016-06-01', NULL, 0.00, 0, 1, 0, '2016-06-30 14:03:03', 'asset_uuid_1', '2016-06-29 18:14:01', '2016-06-30 14:03:03', NULL, 'ZC2754DB71DB38D77F', 25, NULL, 1, 1, NULL,1, 0,0,'51610829-01f9-400b-adaa-5f7a9545441a');

	
INSERT INTO `asset_package` (`id`, `is_available`, `create_time`, `contract_id`, `asset_package_no`, `financial_contract_id`, `loan_batch_id`)
VALUES
	(47, NULL, NULL, 24, NULL, 14, 14),
	(48, NULL, NULL, 25, NULL, 14, 14);
	
INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`)
VALUES
	(2, 'nongfenqi', '11111db75ebb24fa0993f4fa25775022', 00000000, 'http://e.zufangbao.cn', '农分期', 4, NULL);

	INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`)
VALUES
	(3, 'ppd', NULL, 00000000, NULL, '测试商户ppd', 5, NULL);

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`)
VALUES
	(116, NULL, NULL, '测试用户00101', 'C74211', 2, 'customer_uuid_1');

INSERT INTO `house` (`id`, `address`, `app_id`)
VALUES
	(116, NULL, 2);
	
INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`)
VALUES
(1, '杭州', '南京农纷期电子商务有限公司', '农分期');

	
	INSERT INTO `contract_account` (`id`, `pay_ac_no`, `bankcard_type`, `contract_id`, `payer_name`, `bank`, `bind_id`, `id_card_num`, `bank_code`, `province`, `city`, `from_date`, `thru_date`)
VALUES 
	(24, '6217000000000003006', NULL, 24, '测试用户00101', '中国邮政储蓄银行', NULL, NULL, '403', '安徽省', '亳州市', '2016-04-17 00:00:00', '2900-01-01 00:00:00'),
	(25, '6217000000000003007', NULL, 25, '测试用户00102', '中国邮政储蓄银行', NULL, NULL, '403', '江苏省', '淮安市', '2016-04-17 00:00:00', '2900-01-01 00:00:00');

INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`,`adva_repayment_term`)
VALUES
	(14, 0, 1, '2016-09-01 00:00:00', 'G31700', '拍拍贷测试', 3, 1, 60, '2017-08-31 00:00:00', 102, 0, 1, 2, 1, 'yunxin_ledger_book', 'd2812bc5-5057-4a91-b3fd-9019506f0499', 0, 0, 0, 0, 1, 0,0);


INSERT INTO `ledger_book` (`id`, `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`) VALUES 
('1', 'yunxin_ledger_book', '1', '1', '');

INSERT INTO `ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`) VALUES 
('3', 'ledger_uuid_3', '0.00', '200.00', 'FST_RECIEVABLE_ASSET', '20000', '1', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', NULL, NULL, '1', 'customer_uuid_1', 'TRD_RECIEVABLE_LOAN_ASSET_PRINCIPLE', '20000.01.01', 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '616', NULL, '2016-08-28 00:00:00', NULL, 'yunxin_ledger_book', '1', '1', 'test_single_no', 'asset_uuid_1', NULL, NULL, NULL, NULL, NULL),
('6', 'ledger_uuid_6', '0.00', '1000', 'FST_RECIEVABLE_ASSET', '20000', '1', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', NULL, NULL, '1', 'customer_uuid_1', NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '616', NULL, '2016-08-28 00:00:00', NULL, 'yunxin_ledger_book', '1', '1', 'test_single_no', 'asset_uuid_1', NULL, NULL, NULL, NULL, NULL);
	

INSERT INTO `t_deduct_application_detail` (`id`, `deduct_application_detail_uuid`, `deduct_application_uuid`, `financial_contract_uuid`, `contract_unique_id`, `repayment_plan_code`, `asset_set_uuid`, `request_no`, `repayment_type`, `transaction_type`, `create_time`, `execution_remark`, `creator_name`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`, `is_total`)
	VALUES ('2331', 'b394c99b-cae1-456e-8f0b-fde182b522af', '51610829-01f9-400b-adaa-5f7a9545441a', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '8f544362-bac1-4a00-a470-63b4fd2bdba5', 'ZC2754DB71DB38D77F', 'asset_uuid_1', '07ab9d8a-e1c1-40c9-a10e-968f6a8912a4', '1', '1', '2016-11-01 19:20:25', '', 't_merchant', '2016-11-01 19:20:25', 'TOTAL_RECEIVABEL_AMOUNT', NULL, NULL, NULL, NULL, NULL, '1510.50', '0'),
		   ('2330', 'b394c99b-cae1-456e-8f0b-fde182b522af', '51610829-01f9-400b-adaa-5f7a9545441a', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '8f544362-bac1-4a00-a470-63b4fd2bdba5', 'ZC2754DB71DB38D77F', 'asset_uuid_1', '07ab9d8a-e1c1-40c9-a10e-968f6a8912a4', '1', '1', '2016-11-01 19:20:25', '', 't_merchant', '2016-11-01 19:20:25', 'TOTAL_OVERDUE_FEE', NULL, NULL, NULL, NULL, NULL, '0.00', '1');



SET FOREIGN_KEY_CHECKS=1;