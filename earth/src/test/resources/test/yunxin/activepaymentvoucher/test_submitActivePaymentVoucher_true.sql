DELETE FROM `dictionary`;
DELETE FROM `house`;
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
DELETE FROM `contract_account`;
DELETE FROM `t_remittance_plan_exec_log`;
DELETE FROM `cash_flow`;
DELETE FROM `source_document_resource`;
DELETE FROM `t_voucher`;
DELETE FROM `source_document`;
DELETE FROM `source_document_detail`;
DELETE FROM `contract_account`;

INSERT INTO `cash_flow` (`id`, `cash_flow_uuid`, `cash_flow_channel_type`, `company_uuid`, `host_account_uuid`, `host_account_no`, `host_account_name`, `counter_account_no`, `counter_account_name`, `counter_account_appendix`, `counter_bank_info`, `account_side`, `transaction_time`, `transaction_amount`, `balance`, `transaction_voucher_no`, `bank_sequence_no`, `remark`, `other_remark`, `strike_balance_status`, `trade_uuid`, `issued_amount`, `audit_status`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`) 
VALUES 
('1', 'cashflowuuid1', '0', 'companyuuid1', 'hostaccountuuid1', 'hostaccountno1', 'hostaccount', 'paymentno1', 'payment', '', NULL, NULL, NULL, '5000.00', NULL, 'transactionno1', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);


INSERT INTO `contract_account` (`id`, `pay_ac_no`, `bankcard_type`, `contract_id`, `payer_name`, `bank`, `bind_id`, `id_card_num`, `bank_code`, `province`, `province_code`, `city`, `city_code`, `standard_bank_code`, `from_date`, `thru_date`, `virtual_account_uuid`, `bank_card_status`, `contract_account_uuid`, `contract_account_type`)
VALUES
('1', '6217000000000003006', NULL, '54341', '测试用户1', '中国邮政储蓄银行', NULL, '6217000000000003006', '403', '安徽省', NULL, '亳州', NULL, NULL, '2016-04-17 00:00:00', '2900-01-01', NULL, 0, '999e54ce-c2db-11e6-908d-745c6f182c95', 0);

INSERT INTO `house` (`id`, `address`, `app_id`) 
VALUES 
	('54505', 'newyork', '2');
INSERT INTO `dictionary` ( `code`, `content`)
VALUES
	( 'PLATFORM_PRI_KEY', 'MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=');
INSERT INTO `customer` (`id`, `app_id`)
VALUES 
	(54349, 2);
INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `adva_repayment_term`)
VALUES
	(37, 0, 3, '2016-09-01 00:00:00', 'G32000', '用钱宝测试', 2, 1, 60, '2017-12-01 00:00:00', 101, 0, 1, 2, 1, 'e17d8068-46a0-48bb-b158-8e80168bbfc3', 'b674a323-0c30-4e4b-9eba-b14e05a9d80a', 0, 0, 0, 0, 1, 0, 5),
	(38, 0, 3, '2016-09-01 00:00:00', 'G32001', '用钱宝测试', 3, 1, 60, '2017-12-01 00:00:00', 101, 0, 1, 2, 1, 'e17d8068-46a0-48bb-b158-8e80168bbfc3', 'b674a323-0c30-4e4b-9eba-b14e05a9d80b', 0, 0, 0, 0, 1, 0, 5);

INSERT INTO `ledger_book` (`id`, `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`)
VALUES
	(36, 'e17d8068-46a0-48bb-b158-8e80168bbfc3', '1', NULL, NULL);
INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`)
VALUES
	(54340, '3e8711d4-9573-4965-a878-480ee4c1f5fc', 'e568793f-a44c-4362-9e78-0ce433131f3e', '2017-09-10', '云信信2016-241-DK(428522112675736881)', '2018-12-09', NULL, 0.00, 2, 54349, 54505, NULL, '2016-10-25 11:06:48', 0.8923000000, 0, 0, 2, 2, 4500.00, 0.0050000000, 1, NULL, 2, 'b674a323-0c30-4e4b-9eba-b14e05a9d80a'),
	(54341, '3e8711d4-9573-4965-a878-480ee4c1f5fd', 'e568793f-a44c-4362-9e78-0ce433131f3f', '2017-09-10', '云信信2016-241-DK(428522112675736882)', '2018-12-20', NULL, 0.00, 3, 54349, 54505, NULL, '2016-10-25 11:06:48', 0.8923000000, 0, 0, 2, 2, 4500.00, 0.0050000000, 1, NULL, 2, 'b674a323-0c30-4e4b-9eba-b14e05a9d80b');

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
	(2, 'qyb', NULL, 1, NULL, '测试商户yqb', 1, NULL),
  	(3, 'qyd', NULL, 1, NULL, '测试商户yqd', 1, NULL);

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`)
VALUES
	(1, '上海', '测试金融公司', '测试金融', 'a02c02b9-6f98-11e6-bf08-00163e002839'),
	(4, '杭州', 'yqb', 'yqb', 'a02c08b5-6f98-11e6-bf08-00163e002839');


INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `contract_uuid`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`)
VALUES
	(148660, 0, 0, 100000.00, 100000.00, 0,0, '2016-11-10', NULL, 0.00, 0, 1, 1, NULL, '890d41d9-2484-46bb-a856-e987ef1da40a', '2016-10-25 11:06:48', '2016-10-25 11:14:01', NULL, 'ZC275016985BF712EB', 54340, '3e8711d4-9573-4965-a878-480ee4c1f5fc', '2016-10-25 11:14:01', 1, 0, NULL, 1, 2, 0),
  (148661, 0, 0, 50000.00, 40000.00, 10000.00,0, '2016-11-15', NULL, 0.00, 0, 1, 0, NULL, '890d41d9-2484-46bb-a856-e987ef1da40b', '2016-10-25 11:06:48', '2016-10-25 11:14:01', NULL, 'ZC275016985BF712EC', 54340, '3e8711d4-9573-4965-a878-480ee4c1f5fc', '2016-10-25 11:14:01', 1, 0, NULL, 1, 0, 0),
  (148662, 0, 0, 50000.00, 40000.00, 10000.00,0, '2016-11-20', NULL, 0.00, 0, 1, 0, NULL, '890d41d9-2484-46bb-a856-e987ef1da40c', '2016-10-25 11:06:48', '2016-10-25 11:14:01', NULL, 'ZC275016985BF712ED', 54340, '3e8711d4-9573-4965-a878-480ee4c1f5fc', '2016-10-25 11:14:01', 2, 0, NULL, 1, 0, 0),
	(148664, 0, 0, 15000.00, 10000.00, 5000.00,0, '2017-11-15', NULL, 0.00, 0, 1, 0, NULL, '890d41d9-2484-46bb-a856-e987ef1da40d', '2016-10-25 11:06:48', '2016-10-25 11:14:01', NULL, 'ZC275016985BF712EF', 54341, '3e8711d4-9573-4965-a878-480ee4c1f5fd', '2016-10-25 11:14:01', 1, 0, NULL, 1, 0, 0),
	(148665, 0, 0, 20000.00, 15000.00, 5000.00,0, '2017-11-25', NULL, 0.00, 0, 1, 0, NULL, '890d41d9-2484-46bb-a856-e987ef1da40e', '2016-10-25 11:06:48', '2016-10-25 11:14:01', NULL, 'ZC275016985BF712EG', 54341, '3e8711d4-9573-4965-a878-480ee4c1f5fd', '2016-10-25 11:14:01', 2, 0, NULL, 1, 0, 0),
	(148666, 0, 0, 25000.00, 20000.00, 5000.00,0, '2017-12-05', NULL, 0.00, 0, 1, 0, NULL, '890d41d9-2484-46bb-a856-e987ef1da40f', '2016-10-25 11:06:48', '2016-10-25 11:14:01', NULL, 'ZC275016985BF712EH', 54341, '3e8711d4-9573-4965-a878-480ee4c1f5fd' ,'2016-10-25 11:14:01', 3, 0, NULL, 1, 0, 0);

INSERT INTO `source_document_resource` (`id`, `uuid`, `source_document_uuid`, `batch_no`, `path`, `status`, `voucher_no`) 
VALUES 
	('1', 'test_resource_uuid_1', NULL, NULL, NULL, b'0', NULL),
	('2', 'test_resource_uuid_2', NULL, NULL, NULL, b'0', NULL),
	('3', 'test_resource_uuid_3', NULL, NULL, NULL, b'0', NULL),
	('4', 'test_resource_uuid_4', NULL, NULL, NULL, b'0', NULL);
	
INSERT INTO `asset_set_extra_charge` (`id`, `asset_set_extra_charge_uuid`, `asset_set_uuid`, `create_time`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`)
VALUES
	(381437, '2afe06f0-5499-477a-b81a-4dd300416a86', '890d41d9-2484-46bb-a856-e987ef1da40e', '2016-10-25 11:06:48', '2016-10-25 11:06:48', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_OTHER_FEE', '10000.05', NULL, NULL, 3.60),
	(381438, 'b6eea1ba-b327-4970-ad81-973eb84042e2', '890d41d9-2484-46bb-a856-e987ef1da40e', '2016-10-25 11:06:48', '2016-10-25 11:06:48', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_TECH_FEE', '10000.04', NULL, NULL, 2.50);

INSERT INTO `t_remittance_plan_exec_log` (`id`, `remittance_application_uuid`, `remittance_plan_uuid`, `financial_contract_uuid`, `financial_contract_id`, `payment_gateway`, `payment_channel_uuid`, `payment_channel_name`, `pg_account_name`, `pg_account_no`, `pg_clearing_account`, `planned_amount`, `actual_total_amount`, `cp_bank_code`, `cp_bank_card_no`, `cp_bank_account_holder`, `cp_id_type`, `cp_id_number`, `cp_bank_province`, `cp_bank_city`, `cp_bank_name`, `transaction_type`, `transaction_remark`, `exec_req_no`, `exec_rsp_no`, `execution_status`, `execution_remark`, `transaction_serial_no`, `complete_payment_date`, `create_time`, `last_modified_time`, `plan_credit_cash_flow_check_number`, `actual_credit_cash_flow_check_number`, `reverse_status`, `credit_cash_flow_uuid`, `debit_cash_flow_uuid`, `transaction_recipient`)
VALUES 
 	('1', 'remittance_application_uuid1', 'remittance_plan_uuid1', 'b674a323-0c30-4e4b-9eba-b14e05a9d80a', '37', '1', 'payment_channel_uuid1', 'payment_channel_name1', 'pg_account_name1', 'pg_account_no1', 'pg_clearing_account1', '0.03', '0.00', 'cp_bank_code1', 'cp_bank_card_no1', 'cp_bank_account_holder1', '0', 'cp_id_number1', 'cp_bank_province1', 'cp_bank_city1', 'cp_bank_name1', '0', NULL, 'exec_req_no1', 'exec_rsp_no1', '3', NULL, 'transaction_serial_no1', '2016-09-22 15:32:23', '2016-09-22 15:32:23', '2016-09-22 15:32:58', '55', '0', '0', 'credit_cash_flow_uuid1', 'debit_cash_flow_uuid1','1'),
	('2', 'remittance_application_uuid2', 'remittance_plan_uuid2', 'b674a323-0c30-4e4b-9eba-b14e05a9d80a', '37', '1', 'payment_channel_uuid2', 'payment_channel_name2', 'pg_account_name2', 'pg_account_no2', 'pg_clearing_account2', '0.04', '0.00', 'cp_bank_code2', 'cp_bank_card_no2', 'cp_bank_account_holder2', '0', 'cp_id_number2', 'cp_bank_province2', 'cp_bank_city2', 'cp_bank_name2', '0', NULL, 'exec_req_no2', 'exec_rsp_no2', '2', NULL, 'transaction_serial_no2', '2016-09-22 15:32:23', '2016-09-22 15:32:23', '2016-09-22 15:32:58', '0', '0', '0', 'credit_cash_flow_uuid2', 'debit_cash_flow_uuid2','1'),
	('3', 'remittance_application_uuid3', 'remittance_plan_uuid3', 'b674a323-0c30-4e4b-9eba-b14e05a9d80a', '37', '1', 'payment_channel_uuid3', 'payment_channel_name3', 'pg_account_name3', 'pg_account_no3', 'pg_clearing_account3', '0.05', '0.00', 'cp_bank_code3', 'cp_bank_card_no3', 'cp_bank_account_holder3', '0', 'cp_id_number3', 'cp_bank_province3', 'cp_bank_city3', 'cp_bank_name3', '0', NULL, 'exec_req_no3', 'exec_rsp_no3', '1', NULL, 'transaction_serial_no3', '2016-09-22 15:32:23', '2016-09-22 15:32:23', '2016-09-22 15:32:58', '55', '0', '0', 'credit_cash_flow_uuid3', 'debit_cash_flow_uuid3','1'),
	('4', 'remittance_application_uuid4', 'remittance_plan_uuid4', 'b674a323-0c30-4e4b-9eba-b14e05a9d80a', '37', '1', 'payment_channel_uuid3', 'payment_channel_name3', 'pg_account_name3', 'pg_account_no3', 'pg_clearing_account3', '0.05', '0.00', 'cp_bank_code3', 'cp_bank_card_no3', 'cp_bank_account_holder3', '0', 'cp_id_number3', 'cp_bank_province3', 'cp_bank_city3', 'cp_bank_name3', '0', NULL, 'exec_req_no3', 'exec_rsp_no3', '1', NULL, 'transaction_serial_no3', '2016-09-22 15:32:23', '2016-09-22 15:32:23', '2016-09-22 15:32:58', '55', '0', '0', 'credit_cash_flow_uuid3', 'debit_cash_flow_uuid3','0');
	
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
 	
 INSERT INTO `ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`) 
 VALUES 
 	('2055432', NULL, '10000.00', '5000.00', NULL, '20000', '111', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'e17d8068-46a0-48bb-b158-8e80168bbfc3', NULL, '0', NULL, '890d41d9-2484-46bb-a856-e987ef1da40d', NULL, NULL, NULL, NULL, NULL);
 	
 INSERT INTO `contract_account` (`id`, `pay_ac_no`, `bankcard_type`, `contract_id`, `payer_name`, `bank`, `bind_id`, `id_card_num`, `bank_code`, `province`, `province_code`, `city`, `city_code`, `standard_bank_code`, `from_date`, `thru_date`, `virtual_account_uuid`, `bank_card_status`, `contract_account_uuid`, `contract_account_type`) 
 VALUES 
 	('1559', 'paymentno1', NULL, '54341', 'payment', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2900-01-01 00:00:00', NULL, '0', NULL, '0');

