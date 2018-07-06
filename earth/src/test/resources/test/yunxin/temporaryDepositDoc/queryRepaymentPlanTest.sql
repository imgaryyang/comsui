SET FOREIGN_KEY_CHECKS=0;

DELETE FROM `asset_set`;
DELETE FROM `contract`;
DELETE FROM `financial_contract`;
DELETE FROM `customer`;
DELETE FROM `ledger_book`;
DELETE FROM `ledger_book_shelf`;

INSERT INTO `asset_set`(`id`, `asset_uuid`, `contract_id`,`asset_status`, `active_status`, `repayment_plan_type`, `asset_recycle_date`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_deduct_application_uuid`,`financial_contract_uuid`,`executing_status`,`time_interval`,`contract_uuid`, `single_loan_contract_no`, `asset_fair_value`, `customer_uuid`) 
	VALUES (1,'asset_uuid_1','1', '0', '0', '0', '3016-10-20','2016-10-20 14:47:43', '2', '0', '3016-10-13', '2', 'empty_deduct_uuid', 'financialContractUuid','1','0','uuid_1', 'repayment_plan_no', '1000', 'customer_uuid_1');

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
('4', 'ledger_uuid_4', '450.00', '0.00', 'FST_RECIEVABLE_ASSET', '20000', '1', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_PRINCIPLE', '20000.01.01' , 'company_customer_uuid_1', NULL, NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '616', NULL, '2016-08-28 00:00:00', NULL, 'yunxin_ledger_book', '1', '1', 'test_single_no2', 'asset_uuid_1', NULL, NULL, NULL, NULL, NULL),
('5', 'ledger_uuid_5', '50.00', '0.00', 'FST_RECIEVABLE_ASSET', '20000', '1', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_INTEREST', '20000.01.02' , 'company_customer_uuid_1', NULL, NULL, NULL, 'aa48dd5b-4772-4f2b-9964-cbd37c5fcfd0', 'daa2e9e7-98c7-4c6d-afe4-339a116a05f9', '2016-08-28', '2016-08-28 03:02:34', NULL, NULL, '616', NULL, '2016-08-28 00:00:00', NULL, 'yunxin_ledger_book', '1', '1', 'test_single_no2', 'asset_uuid_1', NULL, NULL, NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS=1;