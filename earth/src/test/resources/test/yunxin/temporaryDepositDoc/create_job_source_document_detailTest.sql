SET FOREIGN_KEY_CHECKS=0;

DELETE from `job`;
DELETE from `source_document_detail`;
DELETE from `source_document`;
DELETE from `financial_contract`;
DELETE from `t_voucher`;
DELETE from `contract`;
DELETE from `asset_set`;
DELETE from `ledger_book_shelf`;
DELETE from `ledger_book`;
DELETE from `customer`;
DELETE from `temporary_deposit_doc`;
DELETE from `virtual_account`;

DELETE from `account`;
DELETE from `contract_account`;


--DELETE from `company`;
--DELETE from `virtual_account_flow`;

INSERT INTO `temporary_deposit_doc`(`id`,`uuid`,`doc_no`,`total_amount`,`balance`,`alive_status`,`status`,`owner_name`,`owner_type`,`financial_contract_uuid`,`created_time`,`voucher_uuid`,`related_contract_uuid`,`source_document_uuid`)
VALUES
	('1','TemDptDoc_uuid_1','doc_co_1',1000.00,1000.00,0,0,'owner_name_1',0,'financial_contract_uuid_1','2017-10-30 00:00:00','voucher_uuid_1','relatedContractUuid','source_document_uuid_1');

INSERT INTO `source_document` (`id`, `company_id`, `source_document_uuid`, `source_document_type`, `create_time`, `issued_time`, `source_document_status`, `source_account_side`, `booking_amount`, `outlier_document_uuid`, `outlier_trade_time`, `outlier_counter_party_account`, `outlier_counter_party_name`, `outlier_account`, `outlie_account_name`, `outlier_account_id`, `outlier_company_id`, `outlier_serial_global_identity`, `outlier_memo`, `outlier_amount`, `outlier_settlement_modes`, `outlier_breif`, `outlier_account_side`, `appendix`, `first_outlier_doc_type`, `second_outlier_doc_type`, `third_outlier_doc_type`, `currency_type`, `audit_status`, `voucher_uuid`,`financial_contract_uuid`)
VALUES 
	('1', '1', 'source_document_uuid_1', '1', '2016-05-27 18:32:35', '2016-05-27 18:32:36', '1', '1', '450.00', 'ed0cd216d03b4a889d023ac20a46880e', '2016-05-27 18:29:50', '6217000000000000000', '测试用户18', 'account_no', 'account_name', '5', '1', '2730FAE730E7B3C6', '', '450.00', '3', '', '1', '', 'batch_pay_record', '', '', NULL, '2', 'voucher_uuid_1','financial_contract_uuid_1');

INSERT INTO `t_voucher` (`id`, `uuid`, `voucher_no`, `source_document_uuid`, `financial_contract_uuid`, `amount`, `status`, `first_type`, `first_no`, `second_type`, `second_no`, `receivable_account_no`, `payment_account_no`, `payment_name`, `payment_bank`, `check_state`, `comment`, `create_time`, `contract_no`, `last_modified_time`, `cash_flow_uuid`, `transaction_time`, `detail_amount`)
VALUES
	('1', 'voucher_uuid_1', 'voucher_no_1', 'source_document_uuid_1', 'financial_contract_uuid_1', '570.00', '1', 'enum.voucher-source.business-payment-voucher', 'f8dbc062-d00e-4dd6-af03-b1800ed195a2', 'enum.voucher-type.pay', '00144407-0b86-4604-b4c3-084047aecd14', NULL, '6214855712106520', '夏侯你惇哥', '中国建设银行', '2', NULL, '2017-05-09 22:40:02', NULL, '2017-05-09 22:40:02', '1b0b4455-34c5-11e7-bf99-00163e002839', '2017-05-09 22:39:23', '0');

INSERT INTO `source_document_detail` (`id`, `uuid`, `source_document_uuid`, `contract_unique_id`, `repayment_plan_no`, `amount`, `status`, `first_type`, `first_no`, `second_type`, `second_no`, `payer`, `receivable_account_no`, `payment_account_no`, `payment_name`, `payment_bank`, `check_state`, `comment`, `financial_contract_uuid`, `principal`, `interest`, `service_charge`, `maintenance_charge`, `voucher_uuid`)
VALUES
	(1, 'detail_uuid_1', 'source_document_uuid_1', 'contract_unique_id_1', 'repayment_plan_no_1', 450.00, 0, 'enum.voucher-source.business-payment-voucher', 'TemDptDoc_uuid_1', 'enum.voucher-type.pay', 'random_uuid', 0, '600000000001', '1001133419006708197', '上海拍拍贷金融信息服务有限公司', 'bank', 0, NULL, 'financial_contract_uuid_1','400','50.00','0.00','0.00','voucher_uuid_1');
	
INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`,`adva_repayment_term`,`repayment_check_days`)
VALUES 
	('1', '1', '7', '2016-06-08 00:00:00', 'financial_contract_no', 'sasad', 2, '1', '3', '2016-09-05 00:00:00', 2, '1', '0', '3', NULL, 'ledger_book_no', 'financial_contract_uuid_1', 1, 7);
		
INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, `interest_rate_cycle`, `customer_uuid`) VALUES 
('1', 'relatedContractUuid', 'contract_unique_id_1', '2017-05-02', '0609666a-a295-4935-94c4-5c27be174036', '2099-01-01', NULL, '0.00', '3', 1, '199316', NULL, '2017-05-02 16:37:26', '0.1560000000', '0', '0', '3', '2', '1500.00', '0.0005000000', '1', NULL, '2', 'financial_contract_uuid_1', '1', 'company_customer_uuid_1');

INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`) 
VALUES
	(1, '1', '0', '1050.00', '1000.00', '50.00', '1050.00', '2017-01-20', NULL, '0.00', '0', '0', '0', '2017-03-22 06:17:07', 'asset_uuid', '2017-01-20 21:45:34', '2017-03-22 06:17:07', NULL, 'repayment_plan_no_1', 1, NULL, '1', '1', NULL, '1', '0', '0', 'empty_deduct_uuid', NULL, 'financial_contract_uuid_1', NULL, NULL, '2017-01-20 21:45:34', '2017-01-20 21:45:34', '1', '0', '1', '1', '3', '1', '0', 'company_customer_uuid_1', 'relatedContractUuid', NULL);

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`)
VALUES
	(1, NULL, NULL, '测试用户1', 'C74211', 1, 'customer_uuid_1', 0),
	(2, NULL, NULL, '测试用户2', 'C74212', 2, 'company_customer_uuid_1', 1);
	
INSERT INTO `virtual_account` (`id`, `total_balance`, `virtual_account_uuid`, `parent_account_uuid`, `virtual_account_alias`, `virtual_account_no`, `version`, `owner_uuid`, `owner_name`, `fst_level_contract_uuid`, `snd_level_contract_uuid`, `trd_level_contract_uuid`, `create_time`, `last_update_time`, `customer_type`, `virtual_account_status`) VALUES 
('40', '1460.00', '4b6e315a-7f95-4203-b081-efc0d3b28f9e', NULL, '测试员1', 'VACC275B96CD27E64F02', '5ce01306-45c2-421b-8d0e-531055075275', 'customer_uuid_1', '测试员1', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '491df853-846a-4958-9131-c8459a7dcb6f', '', '2016-12-09 11:41:08', '2016-12-22 14:52:56', '0', '0'),
('41', '1460.00', '4b6e315a-7f95-4203-b081-efc0d3b28f99', NULL, '测试员2', 'VACC275B96CD27E64F03', '5ce01306-45', 'company_customer_uuid_1', '测试员2', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '491df853-846a-4958-9131-c8459a7dcb6f', '', '2016-12-09 11:41:08', '2016-12-22 14:52:56', '1', '0');

INSERT INTO `ledger_book` (`id`, `ledger_book_no`,
`ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`,
`ledger_book_version`)
VALUES ('1', 'ledger_book_no', '1', '1', '','1111');
	
INSERT INTO `ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`) VALUES 
('1', 'ledger_uuid_1', '0.00', '1000.00', 'FST_LONGTERM_LIABILITY', '40000', '0', 'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL, '1', NULL, NULL, NULL, NULL, '871562dd-5a0f-4a8d-990f-07d75bfc0304', '2016-05-01', '2016-05-16 14:26:50', '', NULL, '7', NULL, '2016-05-27 00:00:00', '', 'ledger_book_no', '1', '1', 'repayment_plan_no_1', 'd2c21631-f2fb-4e8f-beef-0a2e72447434', NULL, NULL, NULL, NULL, ''),
('2', 'ledger_uuid_2', '1000', '0.00', 'FST_RECIEVABLE_ASSET', '20000', '1', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_PRINCIPLE', '20000.01.01', '1', 'company_customer_uuid_1', NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '616', NULL, '2016-08-28 00:00:00', NULL, 'ledger_book_no', '1', '1', 'test_single_no', 'asset_uuid', NULL, NULL, NULL, NULL, NULL),
('3', 'ledger_uuid_3', '50.00', '0.00', 'FST_RECIEVABLE_ASSET', '20000', '1', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_INTEREST', '20000.01.02', '1', 'company_customer_uuid_1', NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '616', NULL, '2016-08-28 00:00:00', NULL, 'ledger_book_no', '1', '1', 'test_single_no2', 'asset_uuid', NULL, NULL, NULL, NULL, NULL),
('4', 'ledger_uuid_4', '0.00', '460.00', 'FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS', '50000', '1', NULL, NULL, NULL, NULL , 'company_customer_uuid_1', NULL, NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '616', NULL, '2016-08-28 00:00:00', NULL, 'ledger_book_no', '1', '1', 'test_single_no2', 'asset_uuid', NULL, NULL, NULL, NULL, NULL),
('5', 'ledger_uuid_5', '0.00', '1000.00', 'FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS', '50000', '1', 'SND_LIABILITIES_OF_INDEPENDENT_ACCOUNTS_PENDING', '50000.03', NULL, NULL , 'company_customer_uuid_1', NULL, NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '616', NULL, '2016-08-28 00:00:00', NULL, 'ledger_book_no', '1', '1', 'test_single_no2', 'asset_uuid', NULL, 'TemDptDoc_uuid_1', NULL, NULL, NULL);

INSERT INTO `account` (`id`, `account_name`, `account_no`, `bank_name`, `alias`, `attr`, `scan_cash_flow_switch`, `usb_key_configured`, `uuid`, `bank_code`, `cash_flow_config`) VALUES 
(2, '云南信托国际有限公司', '600000000001', '平安银行深圳分行', NULL, NULL, '\0', '\0', 'bd7dd5b1-aa53-4faf-9647-00ddb8ab4b42', 'PAB', NULL);

INSERT INTO `contract_account` (`id`, `pay_ac_no`, `bankcard_type`, `contract_id`, `payer_name`, `bank`, `bind_id`, `id_card_num`, `bank_code`, `province`, `province_code`, `city`, `city_code`, `standard_bank_code`, `from_date`, `thru_date`)
VALUES
(1, '6217000000000003006', null, '1', '测试用户1', '中国邮政储蓄银行', null, '6217000000000003006', '403', '安徽省', null, '亳州', null, null, '2016-04-17 00:00:00', '2900-01-01 00:00:00');

SET FOREIGN_KEY_CHECKS=1;
