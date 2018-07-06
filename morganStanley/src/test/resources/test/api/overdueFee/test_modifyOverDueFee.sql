delete from asset_set;
delete from contract;
delete from app;
delete from customer;
delete from house;
delete from asset_package;
delete from financial_contract;
delete from ledger_book;
delete from `company`;
DELETE FROM t_interface_modfify_overdue_fee_log;
DELETE from t_product_category;
DELETE from t_source_repository;
DELETE FROM financial_contract_configuration;

INSERT INTO `t_source_repository` (`id`, `business_type`, `source_code`, `signature`, `source_file_path`, `add_time`, `last_modify_time`, `author`, `compile_import`) VALUES ('15', 'importAssetPackage/weifang/10002', 'public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest, Map<String, String> postRequest, Log logger) {\r\n        logger.info(\"start to get param\");\r\n        logger.info(\"param : \" + preRequest);\r\n        String jsonR = (String) preRequest.getOrDefault(\"r\", \"\");\r\n        if(StringUtils.isEmpty(jsonR)){\r\n            logger.error(\"r 利息 is empty\");\r\n            return false;\r\n        }\r\n        BigDecimal r =  new BigDecimal(jsonR);\r\n        logger.info(\"r : \"+ r);\r\n\r\n        String jsonPenalty = (String) preRequest.getOrDefault(\"penalty\", \"\");\r\n        String jsonPrincipal = (String) preRequest.getOrDefault(\"principal\", \"\");\r\n\r\n        logger.info(\"jsonInterest : \" + jsonPenalty);\r\n        logger.info(\"jsonPrincipal : \" + jsonPrincipal);\r\n\r\n        List<BigDecimal> penaltys = JsonUtils.parseArray(jsonPenalty, BigDecimal.class);\r\n        List<BigDecimal> principals = JsonUtils.parseArray(jsonPrincipal, BigDecimal.class);\r\n        if(penaltys == null){\r\n            logger.info(\"penaltys is null\");\r\n            return false;\r\n        }\r\n        if(principals == null){\r\n            logger.info(\"principals is null\");\r\n            return false;\r\n        }\r\n\r\n        String jsonOverdueDay = (String) preRequest.getOrDefault(\"overdueDay\", \"\");\r\n        if(StringUtils.isEmpty(jsonOverdueDay)){\r\n            logger.error(\"overdueDay is empty\");\r\n            return false;\r\n        }\r\n        int overdueDay = Integer.parseInt(jsonOverdueDay);\r\n        logger.info(\"overdueDay : \" + overdueDay);\r\n\r\n        for(int i = 0;i < penaltys.size();i ++){\r\n            logger.info(\"old penalty[{\" + i + \"}] : \"+ ((BigDecimal) penaltys.get(i)));\r\n            logger.info(\"old principal[{\" + i + \"}] : \"+ ((BigDecimal) principals.get(i)));\r\n\r\n            BigDecimal penalty = ((BigDecimal) penaltys.get(i)).setScale(2, BigDecimal.ROUND_HALF_UP);\r\n            BigDecimal principal = ((BigDecimal) principals.get(i)).multiply(new BigDecimal(0.24*overdueDay/360).multiply(r.add(new BigDecimal(1)))).setScale(2, BigDecimal.ROUND_HALF_UP);\r\n\r\n            logger.info(\"after calculating, the penalty[{\" + i + \"}] : \"+ penaltys);\r\n            logger.info(\"after calculating, the principal[{\" + i + \"}] : \"+ principal);\r\n            if(penalty.compareTo(principal) <= 0 ? false :true){\r\n                return false;\r\n            }\r\n        }\r\n        return true;\r\n    }', '86f961fc554b3ce4f7c520c4d5fa6fcf', NULL, '2017-11-07 10:16:48', '2017-11-07 10:16:48', 'zhushiyun', 'com.suidifu.matryoshka.customize.CustomizeServices,com.suidifu.matryoshka.handler.SandboxDataSetHandler,com.zufangbao.sun.utils.*,java.math.BigDecimal,java.util.*');
INSERT INTO `t_product_category` (`id`, `uuid`, `product_lv_1_code`, `product_lv_1_name`, `product_lv_2_code`, `product_lv_2_name`, `product_lv_3_code`, `product_lv_3_name`, `pre_process_interface_url`, `post_process_interface_url`, `pre_process_script`, `status`, `script_md_5_version`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`, `delay_task_config_uuid`) VALUES ('119', '77ebae2a-c42d-11e7-a1b1-502b73c136df', 'weifang', '潍坊', 'importAssetPackage', '导入资产包', '10002', '罚息', 'importAssetPackage/weifang/10002', NULL, NULL, '1', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `financial_contract_configuration` (`id`, `uuid`, `financial_contract_uuid`, `code`, `content`) VALUES ('1', '5fb1cc84-c42c-11e7-a1b1-502b73c136df', '984149f1-cb43-410c-a789-d8f4bba123b6', 'weifang_interest_rate', '0.1');

delete from dictionary;
INSERT INTO `dictionary` (`id`, `code`, `content`)
VALUES
	(11, 'PLATFORM_PRI_KEY', 'MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=');

delete from t_merchant_config;
INSERT INTO `t_merchant_config` (`id`, `mer_id`, `secret`, `company_id`, `pub_key_path`, `pub_key`, `project_code`)
VALUES
	(2, 't_test_zfb', '123456', 3, NULL, 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDl1BjinRJLLDiNc6jcOKW+nph9aNSWMaKMk0OxTdSATakyS7rwNxrLMFyJLkI9IpnHussBv1zgsHPUdZeRcHDkbcMdhYoRgpe3gZIVMJ09BMBjhAET4fensvk377L0Whzp+u9r9UxIWowH7YJuJe3yQ7R3RgYxzrPuTJYq/WeuUQIDAQAB', 'spdbank');

delete from t_api_config;
INSERT INTO `t_api_config` (`id`, `api_url`, `fn_code`, `description`, `api_status`)
VALUES
	(35, '/api/v3/modifyOverDueFee', NULL, '变更逾期费用', 1);

INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`,
		`app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`,
		`loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`,`adva_repayment_term`) 
VALUES 
		('14', '0', '3', '1900-01-01 00:00:00', 'G00001', '放款测试用信托合同', 
		'1', '1', '1', '2900-01-01 00:00:00', '1', '0', '1', 
		'90', '1', 'a5e84b90-e55c-4bba-9b9e-3463d36a01e0', '984149f1-cb43-410c-a789-d8f4bba123b6','0');

INSERT INTO `asset_package` (`id`, `is_available`, `create_time`, `contract_id`, `asset_package_no`, `financial_contract_id`, `loan_batch_id`) 
VALUES ('47', NULL, NULL, '24', NULL, '14', '14');


INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, 
		`asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`,
		`create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, 
		`total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`,`financial_contract_uuid`)
VALUES 
		('24', '111', '1234567890', '2016-04-17', '629测试(ZQ2016002000001)', NULL, 
		'1', '0.00', '2', '116', '116', NULL, 
		'2016-06-29 18:14:01', '0.1560000000', '0', '0', '1', '2', 
		'100.00', '0.0005000000', '1', NULL, '2','984149f1-cb43-410c-a789-d8f4bba123b6');


INSERT INTO  `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, 
		`asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, 
		`repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, 
		`single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, 
		`version_no`, `active_status`,`financial_contract_uuid`,`repay_schedule_no`,`contract_uuid`,`outer_repayment_plan_no`) 
VALUES 
		('24', '1', '0', '200.20', '10000.00', '200.00',
		'200.00', '2016-06-01', NULL, '0.00', '0', '1', 
		'0', '2016-06-30 14:03:03', '88450378-e6fd-4857-9562-22971b05b932', '2016-06-29 18:14:01', '2016-06-30 14:03:03', NULL,
		'ZC27375ACFF4234805', '24', NULL, '1', '1', NULL, 
		'1', '0','984149f1-cb43-410c-a789-d8f4bba123b6','76e9c579a83d6c11dd73afe7e5b2b908','111','outer1');

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`,
		`company_id`, `addressee`) 
VALUES
		('2', 'nongfenqi', '11111db75ebb24fa0993f4fa25775022', '\0', 'http://e.zufangbao.cn', '农分期', 
		'4', NULL);

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`)
VALUES 
		('116', NULL, NULL, '测试用户00101', 'C74211', '2', '4faef2f9-1155-4ca8-bd22-bf6230bbc72c');


INSERT INTO  `house` (`id`, `address`, `app_id`) VALUES ('116', NULL, '2');

insert into `ledger_book` ( `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`) values('a5e84b90-e55c-4bba-9b9e-3463d36a01e0','1',NULL,NULL);

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`)
VALUES 
	('1', '南京', '测试商务公司', '测试分期', '984149f1-cb43-410c-a789-d8f4bba123b6');

INSERT INTO `t_interface_modfify_overdue_fee_log` (`id`, `request_no`, `over_due_fee_data`, `create_time`, `ip_address`) VALUES (63524, '1',
																																																																 '[{\"contractNo\":\"\",\"contractUniqueId\":\"1234567890\",\"currentPeriod\":0,\"financialProductCode\":\"G00001\",\"lateFee\":\"300.00\",\"lateOtherCost\":\"200.00\",\"latePenalty\":\"100.00\",\"overDueFeeCalcDate\":\"2017-11-01\",\"penaltyFee\":\"1090.00\",\"repayScheduleNo\":\"\",\"repaymentPlanNo\":\"ZC27375ACFF4234805\",\"totalOverdueFee\":\"1690.00\"}]',
																																																																 '2017-11-01 16:36:03', NULL);

set FOREIGN_KEY_CHECKS = 1;