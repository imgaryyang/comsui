SET FOREIGN_KEY_CHECKS=0;

DELETE FROM `repurchase_doc`;
DELETE FROM `contract`;
DELETE FROM `financial_contract`;
DELETE FROM `customer`;
DELETE FROM `ledger_book`;
DELETE FROM `ledger_book_shelf`;

INSERT INTO `repurchase_doc` (`id`, `repurchase_doc_uuid`, `financial_contract_uuid`, `amount`, `repo_start_date`, `repo_end_date`, `repo_days`, `creat_time`, `verification_time`, `last_modifed_time`, `contract_id`, `contract_no`, `app_id`, `app_name`, `customer_uuid`, `customer_name`, `executing_asset_set_uuids`, `asset_set_uuids`, `repurchase_status`, `repurchase_principal` ,`repurchase_interest`, `repurchase_penalty`, `repurchase_other_charges`) VALUES 
('1', 'repurchase_doc_uuid_1', 'financialContractUuid', '2502.00', '2016-10-31', '2016-11-01', '1', '2016-10-31 20:20:41', NULL, '2016-10-31 20:20:41', '1', 'contract_no_1', '3', 'ppd', 'customer_uuid_1', '测试用户1', NULL, NULL, '0', 2000.00,200.00,100.00,50.00);

INSERT INTO `contract`(`id`, `uuid`, `unique_id`, `financial_contract_uuid`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`)
	VALUES ('1', 'uuid_1','unique_id_1','financialContractUuid_1', '2016-04-17', 'contract_no_1', NULL, '1', '0.00', '1', '1', '1', NULL, '2016-05-27 18:27:16', '0.1560000000', '0', '0', '1', '2', '1200.00', '0.0005000000', '1', NULL, '2');

INSERT INTO `financial_contract` (`id`, `financial_contract_uuid`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`,`sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`, `day_of_month`)
	VALUES ('1','financialContractUuid', '1', '3', '1900-01-01 00:00:00', 'G00003', '平安放款测试用信托合同', '1', '1', '91', '2900-01-01 00:00:00', '1', '0', '1', '90', '1', 'yunxin_ledger_book', '1', '1', '1', '1', '0', '0', NULL, NULL, NULL, NULL, '0', '0', '0', '0', NULL, NULL, NULL, NULL, NULL, NULL,1, 0, 'outstandingPrincipal+outstandingInterest+outstandingPenaltyInterest', NULL);

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`)
	VALUES ('1', NULL, NULL, '测试用户1', 'C74211', '1', 'customer_uuid_1', '0');

INSERT INTO `ledger_book` (`id`, `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`, `ledger_book_version`)
	VALUES (1, 'yunxin_ledger_book', '1', NULL, NULL,'1');

INSERT INTO `ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`) VALUES 
('8', 'ledger_uuid_8', '500.00', '0.00', 'FST_BANK_SAVING', '60000', '1', 'SND_BANK_SAVING_GENERAL', '60000.1000', 'TRD_BANK_SAVING_GENERAL_PRINCIPAL', '60000.1000.01', '1', NULL, NULL, NULL, 'ledger_uuid_7', NULL, '2016-05-01', '2016-05-17 22:26:48', '', NULL, '7', NULL, '2016-05-01 00:00:00', '', 'yunxin_ledger_book', '1', '1', 'ZC27304880AB312E3A', 'asset_uuid_1', NULL, NULL, NULL, NULL, ''),
('4', 'ledger_uuid_4', '450.00', '0.00', 'FST_REPURCHASE_ASSET_CODE', '120000', '1', 'SND_RECIEVABLE_REPURCHASE_ASSET_PRINCIPLE', '120000.01', NULL, NULL , 'company_customer_uuid_1', NULL, NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '616', 'uuid_1', '2016-08-28 00:00:00', NULL, 'yunxin_ledger_book', '1', '1', 'test_single_no2', 'asset_uuid_1', NULL, NULL, NULL, NULL, NULL),
('5', 'ledger_uuid_5', '50.00', '0.00', 'FST_REPURCHASE_ASSET_CODE', '120000', '1', 'SND_RECIEVABLE_REPURCHASE_ASSET_INTEREST', '120000.02', NULL, NULL , 'company_customer_uuid_1', NULL, NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '616', 'uuid_1', '2016-08-28 00:00:00', NULL, 'yunxin_ledger_book', '1', '1', 'test_single_no2', 'asset_uuid_1', NULL, NULL, NULL, NULL, NULL);
	

SET FOREIGN_KEY_CHECKS=1;