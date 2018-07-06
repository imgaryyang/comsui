DELETE FROM `principal`;
DELETE FROM `t_user`;
DELETE FROM `company`;
DELETE FROM `financial_contract`;
DELETE FROM `finance_company`;
DELETE FROM `app`;
DELETE FROM `account`;

INSERT INTO `principal` (`id`, `authority`, `name`, `password`, `t_user_id`)
VALUES ('1',
        'ROLE_TRUST_OBSERVER',
        'zhanghongbing',
        '376c43878878ac04e05946ec1dd7a55f',
        '2');

INSERT INTO `t_user` (`id`, `uuid`, `name`, `email`, `phone`, `company_id`, `dept_name`, `position_name`, `remark`, `financial_contract_ids`)
VALUES ('2',
        '787c8a18-2d4e-4a49-ba38-54736c328244',
        '张红兵',
        'zhanghongbing@hzsuidifu.com',
        '',
        '3',
        '研发部',
        '',
        '',
        '[5,15,21]');

INSERT INTO `principal` (`id`, `authority`, `name`, `password`, `start_date`, `thru_date`, `t_user_id`, `created_time`, `creator_id`)
VALUES
	(2, 'ROLE_SUPER_USER', 'louguanyang', 'd3786ec2413a8cd9413bfcb24be95a73', NULL, NULL, NULL, NULL, NULL);


INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`)
VALUES
	(1, '上海', '测试金融公司', '测试金融', 'a02c02b9-6f98-11e6-bf08-00163e002839');

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`)
VALUES
	(2, '南京', '测试商务公司', '测试分期', 'a02c078d-6f98-11e6-bf08-00163e002839');
	
INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`)
VALUES ('3',
        '杭州',
        '杭州随地付有限公司',
        '随地付',
        'a02c0830-6f98-11e6-bf08-00163e002839');

INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`
	, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`
	, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`
	, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`
	, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`
	, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`
	, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`
	, `create_time`, `last_modified_time`)
VALUES (5, 1, 3, '2016-04-01 00:00:00', 'G08200'
	, '测试合同', 1, 1, 91, '2016-07-01 00:00:00'
	, 5, 0, 5, 90, 2
	, '74a9ce4d-cafc-407d-b013-987077541bdc', '2d380fe1-7157-490d-9474-12c5a9901e29', 1, 1, 1
	, 1, 0, 0, NULL, NULL
	, NULL, NULL, 0, 0, 0
	, 0, NULL, NULL, NULL, NULL
	, NULL, NULL), (15, 1, 3, '2016-06-14 00:00:00', 'ceshi003'
	, 'test006', 1, 1, 91, '2016-06-25 00:00:00'
	, 16, 0, 1, 90, 1
	, 'c374dc6b-132d-4260-86be-534b153a7464', '92846f20-87e3-49f4-8f90-fe04a72c0484', 0, 0, 0
	, 0, 0, 0, NULL, NULL
	, NULL, NULL, 0, 0, 0
	, 0, NULL, NULL, NULL, NULL
	, NULL, NULL), (21, 1, 3, '1900-01-01 00:00:00', 'G00000'
	, '测试信托合同名称0724', 1, 1, 91, '2900-01-01 00:00:00'
	, 22, 0, 1, 90, 2
	, '95efeb8f-9e6f-413c-812c-765e96266851', 'd84e2927-839e-4162-9af1-e648e15bbf59', 0, 0, 0
	, 0, 0, 0, NULL, NULL
	, NULL, NULL, 0, 0, 0
	, 0, NULL, NULL, NULL, NULL
	, NULL, NULL), (22, 1, 5, '2016-08-01 00:00:00', 'zabx20160801'
	, '众安保险', 1, 1, 91, '2019-08-01 00:00:00'
	, 23, 0, 4, 90, 1
	, '293094ed-9644-4312-ac97-00b9504435c0', '930f1d3d-8158-420e-89bd-6f3922395eae', 0, 0, 0
	, 0, 0, 0, NULL, NULL
	, NULL, NULL, 0, 0, 0
	, 0, NULL, NULL, NULL, NULL
	, NULL, NULL), (23, 1, 3, '2016-08-01 00:00:00', 'xthtcs'
	, '20160810信托合同测试', 1, 1, 91, '2019-05-18 00:00:00'
	, 24, 0, 1, 90, 1
	, '951f5273-5934-4853-aab9-a4ec3902f9b9', '8e517faa-172b-4745-a2f8-e7ab646f4ae2', 0, 0, 0
	, 0, 0, 0, NULL, NULL
	, NULL, NULL, 0, 0, 0
	, 0, NULL, NULL, NULL, NULL
	, NULL, NULL);

INSERT INTO `finance_company` (`id`, `company_id`)
VALUES
	(1, 1);
	
INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`)
VALUES
	(1, 'nongfenqi', '', 1, '', '测试分期', 2, NULL);
	
INSERT INTO `account` (`id`, `account_name`, `account_no`, `bank_name`, `alias`, `attr`, `scan_cash_flow_switch`, `usb_key_configured`, `uuid`, `bank_code`, `cash_flow_config`)
VALUES
	(5, '银企直连专用账户9', '591902896710201', '', NULL, '{\"bankBranchNo\":\"59\",\"usbUuid\":\"b173f24a-3c27-4243-85b7-e93ef6a128ac\"}', 1, 1, 'uuid_5', 'PAB', '{\"nextPageNo\":\"1\"}'),
	(16, 'ceshi911', 'ceshi0045', '测试', NULL, NULL, 1, 0, '121e8222-cca9-4b82-af9e-04b57f4516b5', 'PAB', NULL),
	(22, '测试信托专户户名0724', '测试信托专户账号0724', '测试信托专户开户行0724', NULL, NULL, 0, 0, '737b1e64-8140-4f21-b2ff-b4f49ea65805', 'PAB', NULL),
	(23, '众安保险001', '4357622210008946', '中国工商银行', NULL, NULL, 0, 0, 'ce960bff-d29d-4a56-97eb-bb0905f65095', 'PAB', NULL),
	(24, '20160810信托合同测试的专户', '2016081020160810', '20160810信托合同测试的开户行', NULL, NULL, 0, 0, '14b1d2f8-ae41-466a-b2d4-0975282c0a16', 'PAB', NULL);
	
