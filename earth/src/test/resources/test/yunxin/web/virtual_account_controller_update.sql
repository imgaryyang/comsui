DELETE FROM `dictionary`;
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
DELETE FROM `principal`;
DELETE FROM `contract_account`;
DELETE FROM `virtual_account`;

INSERT INTO `principal` (`id`, `authority`, `name`, `password`, `start_date`, `thru_date`, `t_user_id`, `created_time`, `creator_id`, `modify_password_time`)
 VALUES (1, 'ROLE_SUPER_USER', 'zhanghongbing', '376c43878878ac04e05946ec1dd7a55f', NULL, NULL, 2, NULL, NULL, 1);

INSERT INTO `dictionary` ( `code`, `content`)
VALUES
	( 'PLATFORM_PRI_KEY', 'MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=');
INSERT INTO customer (id, app_id)
VALUES 
	(54349,2);
INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `adva_repayment_term`)
VALUES
	(37, 0, 3, '2018-09-01 00:00:00', 'G32000', '用钱宝测试', 2, 1, 60, '2018-12-01 00:00:00', 101, 0, 1, 2, 1, 'e17d8068-46a0-48bb-b158-8e80168bbfc3', 'b674a323-0c30-4e4b-9eba-b14e05a9d80a', 0, 0, 0, 0, 1, 0, 7);

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

INSERT INTO `contract_account` (`id`, `pay_ac_no`, `bankcard_type`, `contract_id`, `payer_name`, `bank`, `bind_id`, `id_card_num`, `bank_code`, `province`, `province_code`, `city`, `city_code`, `standard_bank_code`, `from_date`, `thru_date`, `virtual_account_uuid`, `bank_card_status`, `contract_account_uuid`, `contract_account_type`) VALUES 
('47222', 'aa123', '0', '39662', 'ceshi2', '中信银行', NULL, '341225199207070128', 'C10302', '云南省', '530000', '昆明市', '530100', NULL, '2016-12-14 11:09:49', '2900-01-01 00:00:00', '4b6e315a-7f95-4203-b081-efc0d3b28f9e', '1', 'd45f1f4260954e13aa4e6197a7c9a88a', '1');

	
	
INSERT INTO `virtual_account` (`id`, `total_balance`, `virtual_account_uuid`, `parent_account_uuid`, `virtual_account_alias`, `virtual_account_no`, `version`, `owner_uuid`, `owner_name`, `fst_level_contract_uuid`, `snd_level_contract_uuid`, `trd_level_contract_uuid`, `create_time`, `last_update_time`, `customer_type`, `virtual_account_status`) VALUES 
('40', '1460.00', '4b6e315a-7f95-4203-b081-efc0d3b28f9e', NULL, '测试员1', 'VACC275B96CD27E64F02', '5ce01306-45c2-421b-8d0e-531055075275', 'bf5dd304-87ac-4f7f-a7b9-8c172635e053', '测试员1', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '491df853-846a-4958-9131-c8459a7dcb6f', '', '2016-12-09 11:41:08', '2016-12-22 14:52:56', '0', '0');
