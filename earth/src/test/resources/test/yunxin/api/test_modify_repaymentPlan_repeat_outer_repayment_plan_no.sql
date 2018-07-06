DELETE FROM `customer`;
DELETE FROM `financial_contract`;
DELETE FROM `ledger_book`;
DELETE FROM `contract`;
DELETE FROM `asset_package`;
DELETE FROM `company`;
DELETE FROM `app`;
DELETE FROM `asset_set`;
DELETE FROM `asset_set_extra_charge`;
DELETE FROM `ledger_book_shelf`;
DELETE FROM `finance_company`;
DELETE FROM `account`;
DELETE FROM `ledger_book`;

INSERT INTO customer (id, app_id)
VALUES
	(54349,2);
INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `adva_repayment_term`)
VALUES
	(37, 0, 3, '2018-09-01 00:00:00', 'G32000', '用钱宝测试', 2, 1, 60, '2018-12-01 00:00:00', 101, 0, 1, 2, 1, 'e17d8068-46a0-48bb-b158-8e80168bbfc3', 'b674a323-0c30-4e4b-9eba-b14e05a9d80a', 0, 0, 0, 0, 1, 0,7);

INSERT INTO `ledger_book` (`id`, `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`)
VALUES
	(36, 'e17d8068-46a0-48bb-b158-8e80168bbfc3', '1', NULL, NULL);
INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`)
VALUES
	(54340, '3e8711d4-9573-4965-a878-480ee4c1f5fc', 'e568793f-a44c-4362-9e78-0ce433131f3e', '2016-09-10', '云信信2016-241-DK(428522112675736881)', '2018-12-09', NULL, 0.00, 2, 54349, 54505, NULL, '2016-10-25 11:06:48', 0.8923000000, 0, 0, 2, 2, 4500.00, 0.0050000000, 1, NULL, 2, 'b674a323-0c30-4e4b-9eba-b14e05a9d80a');

INSERT INTO `asset_package` (`id`, `is_available`, `create_time`, `contract_id`, `asset_package_no`, `financial_contract_id`, `loan_batch_id`)
VALUES
	(54326, NULL, NULL, 54340, NULL, 37, 50131);

INSERT INTO `account` (`id`, `account_name`, `account_no`, `bank_name`, `alias`, `attr`, `scan_cash_flow_switch`, `usb_key_configured`, `uuid`, `bank_code`)
VALUES
	(101, '云南信托', '20001', '平安银行深圳分行', NULL, NULL, 0, 0, '8e3cd5c5-8fb6-4cd6-b6c7-660a9f35f47c', NULL);

INSERT INTO `finance_company` (`id`, `company_id`)
VALUES
	(1, 1);

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`)
VALUES
	(2, 'qyb', NULL, 1, NULL, '测试商户yqb', 4, NULL);

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`)
VALUES
	(1, '上海', '测试金融公司', '测试金融', 'a02c02b9-6f98-11e6-bf08-00163e002839'),
	(4, '杭州', 'yqb', 'yqb', 'a02c08b5-6f98-11e6-bf08-00163e002839');


INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`,`financial_contract_uuid`,`contract_uuid`, `repay_schedule_no`, `outer_repayment_plan_no`)
VALUES
	(148660, 0, 0, 0, 80000.00, 10000.00, 90000.00, '2018-11-10', '2018-10-30', 0.00, 0, 1, 0, NULL, '890d41d9-2484-46bb-a856-e987ef1da40e', '2016-10-25 11:06:48', '2016-10-25 11:14:01', NULL, 'ZC275016985BF712EB', 54340, '2018-10-25 11:14:01', 1, 0, NULL, 1, 0, 0,'b674a323-0c30-4e4b-9eba-b14e05a9d80a','3e8711d4-9573-4965-a878-480ee4c1f5fc','56248960ff4820604ac87a246bd5a35d','outer1');

INSERT INTO `asset_set_extra_charge` (`id`, `asset_set_extra_charge_uuid`, `asset_set_uuid`, `create_time`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`)
VALUES
	(381437, '2afe06f0-5499-477a-b81a-4dd300416a86', '890d41d9-2484-46bb-a856-e987ef1da40e', '2016-10-25 11:06:48', '2016-10-25 11:06:48', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_OTHER_FEE', '10000.05', NULL, NULL, 3.60),
	(381438, 'b6eea1ba-b327-4970-ad81-973eb84042e2', '890d41d9-2484-46bb-a856-e987ef1da40e', '2016-10-25 11:06:48', '2016-10-25 11:06:48', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_TECH_FEE', '10000.04', NULL, NULL, 2.50);

INSERT INTO `ledger_book` (`id`, `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`)
VALUES
	(1, 'e17d8068-46a0-48bb-b158-8e80168bbfc3', '1', '', NULL);

INSERT INTO `ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES
	(2055424, '4c2cb54a-0c67-4118-bb95-72f2616bd55f', 0.00, 1900.00, 'FST_LONGTERM_LIABILITY', '40000', 0, 'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL, 'a02c02b9-6f98-11e6-bf08-00163e002839', 'cc384fb8-0fcc-4632-b723-bafde8dca88e', NULL, NULL, NULL, 'd792767d-beb4-4862-89a9-6a15ed739456', '2016-11-14', '2016-10-25 11:06:48', '', NULL, 54340, '3e8711d4-9573-4965-a878-480ee4c1f5fc', '2016-11-14 00:00:00', '', 'e17d8068-46a0-48bb-b158-8e80168bbfc3', '1', 1, 'ZC275016985BF712EB', '890d41d9-2484-46bb-a856-e987ef1da40e', NULL, NULL, NULL, NULL, ''),
	(2055425, '5040cef6-02a0-4e56-aa79-a7ba78f8c02e', 0.00, 1.00, 'FST_DEFERRED_INCOME', '100000', 0, 'SND_DEFERRED_INCOME_INTEREST_ESTIMATE', '100000.01', NULL, NULL, 'a02c02b9-6f98-11e6-bf08-00163e002839', 'cc384fb8-0fcc-4632-b723-bafde8dca88e', NULL, NULL, NULL, 'd792767d-beb4-4862-89a9-6a15ed739456', '2016-11-14', '2016-10-25 11:06:48', '', NULL, 54340, '3e8711d4-9573-4965-a878-480ee4c1f5fc', '2016-11-14 00:00:00', '', 'e17d8068-46a0-48bb-b158-8e80168bbfc3', '1', 1, 'ZC275016985BF712EB', '890d41d9-2484-46bb-a856-e987ef1da40e', NULL, NULL, NULL, NULL, ''),
	(2055426, 'e8919a9d-26f2-420e-9611-3d41745c217f', 2.50, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1, 'SND_UNEARNED_LOAN_ASSET_TECH_FEE', '10000.04', NULL, NULL, 'a02c02b9-6f98-11e6-bf08-00163e002839', 'cc384fb8-0fcc-4632-b723-bafde8dca88e', NULL, NULL, NULL, 'd792767d-beb4-4862-89a9-6a15ed739456', '2016-11-14', '2016-10-25 11:06:48', '', NULL, 54340, '3e8711d4-9573-4965-a878-480ee4c1f5fc', '2016-11-14 00:00:00', '', 'e17d8068-46a0-48bb-b158-8e80168bbfc3', '1', 1, 'ZC275016985BF712EB', '890d41d9-2484-46bb-a856-e987ef1da40e', NULL, NULL, NULL, NULL, ''),
	(2055427, '2e63f0bb-737a-4bef-beb4-c2885b5f1ddf', 1900.00, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1, 'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, 'a02c02b9-6f98-11e6-bf08-00163e002839', 'cc384fb8-0fcc-4632-b723-bafde8dca88e', NULL, NULL, NULL, 'd792767d-beb4-4862-89a9-6a15ed739456', '2016-11-14', '2016-10-25 11:06:48', '', NULL, 54340, '3e8711d4-9573-4965-a878-480ee4c1f5fc', '2016-11-14 00:00:00', '', 'e17d8068-46a0-48bb-b158-8e80168bbfc3', '1', 1, 'ZC275016985BF712EB', '890d41d9-2484-46bb-a856-e987ef1da40e', NULL, NULL, NULL, NULL, ''),
	(2055428, 'b669c37c-2b55-49cd-86bf-5a39768c0c3d', 0.00, 3.60, 'FST_DEFERRED_INCOME', '100000', 0, 'SND_DEFERRED_INCOME_FEE', '100000.02', 'TRD_DEFERRED_INCOME_LOAN_OTHER_FEE', '100000.02.03', 'a02c02b9-6f98-11e6-bf08-00163e002839', 'cc384fb8-0fcc-4632-b723-bafde8dca88e', NULL, NULL, NULL, 'd792767d-beb4-4862-89a9-6a15ed739456', '2016-11-14', '2016-10-25 11:06:48', '', NULL, 54340, '3e8711d4-9573-4965-a878-480ee4c1f5fc', '2016-11-14 00:00:00', '', 'e17d8068-46a0-48bb-b158-8e80168bbfc3', '1', 1, 'ZC275016985BF712EB', '890d41d9-2484-46bb-a856-e987ef1da40e', NULL, NULL, NULL, NULL, ''),
	(2055429, '05ce8a7a-e45e-48e1-9f40-117790dfdcac', 0.00, 2.50, 'FST_DEFERRED_INCOME', '100000', 0, 'SND_DEFERRED_INCOME_FEE', '100000.02', 'TRD_DEFERRED_INCOME_LOAN_TECH_FEE', '100000.02.02', 'a02c02b9-6f98-11e6-bf08-00163e002839', 'cc384fb8-0fcc-4632-b723-bafde8dca88e', NULL, NULL, NULL, 'd792767d-beb4-4862-89a9-6a15ed739456', '2016-11-14', '2016-10-25 11:06:48', '', NULL, 54340, '3e8711d4-9573-4965-a878-480ee4c1f5fc', '2016-11-14 00:00:00', '', 'e17d8068-46a0-48bb-b158-8e80168bbfc3', '1', 1, 'ZC275016985BF712EB', '890d41d9-2484-46bb-a856-e987ef1da40e', NULL, NULL, NULL, NULL, ''),
	(2055430, '439f543a-7f42-4ca2-bcd5-f065e8f3d04f', 3.60, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1, 'SND_UNEARNED_LOAN_ASSET_OTHER_FEE', '10000.05', NULL, NULL, 'a02c02b9-6f98-11e6-bf08-00163e002839', 'cc384fb8-0fcc-4632-b723-bafde8dca88e', NULL, NULL, NULL, 'd792767d-beb4-4862-89a9-6a15ed739456', '2016-11-14', '2016-10-25 11:06:48', '', NULL, 54340, '3e8711d4-9573-4965-a878-480ee4c1f5fc', '2016-11-14 00:00:00', '', 'e17d8068-46a0-48bb-b158-8e80168bbfc3', '1', 1, 'ZC275016985BF712EB', '890d41d9-2484-46bb-a856-e987ef1da40e', NULL, NULL, NULL, NULL, ''),
	(2055431, 'eff2a9f1-1c80-456c-8939-b6425f6d79f1', 1.00, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1, 'SND_UNEARNED_LOAN_ASSET_INTEREST', '10000.01', NULL, NULL, 'a02c02b9-6f98-11e6-bf08-00163e002839', 'cc384fb8-0fcc-4632-b723-bafde8dca88e', NULL, NULL, NULL, 'd792767d-beb4-4862-89a9-6a15ed739456', '2016-11-14', '2016-10-25 11:06:48', '', NULL, 54340, '3e8711d4-9573-4965-a878-480ee4c1f5fc', '2016-11-14 00:00:00', '', 'e17d8068-46a0-48bb-b158-8e80168bbfc3', '1', 1, 'ZC275016985BF712EB', '890d41d9-2484-46bb-a856-e987ef1da40e', NULL, NULL, NULL, NULL, '');