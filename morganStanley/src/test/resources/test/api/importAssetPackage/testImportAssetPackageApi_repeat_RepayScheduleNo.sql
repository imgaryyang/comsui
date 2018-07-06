SET FOREIGN_KEY_CHECKS = 0;

USE galaxy_autotest_yunxin;
DELETE FROM financial_contract;
DELETE FROM app;
DELETE FROM company;
DELETE FROM contract;
DELETE FROM asset_package;
DELETE FROM customer;
DELETE FROM contract_account;
DELETE FROM asset_set;
DELETE FROM house;
DELETE FROM bank;
DELETE FROM province;
DELETE FROM city;
DELETE FROM ledger_book;
DELETE FROM t_merchant_config;
DELETE FROM t_api_config;
DELETE FROM dictionary;
DELETE FROM t_remittance_application;
DELETE from t_product_category;
DELETE from t_source_repository;

INSERT INTO `t_product_category` (`id`, `uuid`, `product_lv_1_code`, `product_lv_1_name`, `product_lv_2_code`, `product_lv_2_name`, `product_lv_3_code`, `product_lv_3_name`, `pre_process_interface_url`, `post_process_interface_url`, `pre_process_script`, `status`, `script_md_5_version`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`, `delay_task_config_uuid`) VALUES ('117', '066aca36-c2bb-11e7-a1b1-502b73c136df', 'weifang', '潍坊', 'importAssetPackage', '导入资产包', '10001', '111', 'importAssetPackage/weifang/10001', NULL, NULL, '1', '857986d8-c2bd-11e7-a1b1-502b73c136df', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_source_repository` (`id`, `business_type`, `source_code`, `signature`, `source_file_path`, `add_time`, `last_modify_time`, `author`, `compile_import`) VALUES ('11', 'importAssetPackage/weifang/10001', 'public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest, Map<String, String> postRequest, Log logger) {\r\n        logger.info(\"start to get param\");\r\n        logger.info(\"param : \" + preRequest);\r\n        String jsonInterest = (String) preRequest.getOrDefault(\"interest\", \"\");\r\n        String jsonPrincipal = (String) preRequest.getOrDefault(\"principal\", \"\");\r\n\r\n        logger.info(\"jsonInterest : \" + jsonInterest);\r\n        logger.info(\"jsonPrincipal : \" + jsonPrincipal);\r\n\r\n        List<BigDecimal> interests = JsonUtils.parseArray(jsonInterest, BigDecimal.class);\r\n        List<BigDecimal> principals = JsonUtils.parseArray(jsonPrincipal, BigDecimal.class);\r\n\r\n        logger.info(\"interests : \" + interests);\r\n        logger.info(\"principals : \" + principals);\r\n        if(interests == null){\r\n            logger.info(\"interests is null\");\r\n            return false;\r\n        }\r\n        if(principals == null){\r\n            logger.info(\"principals is null\");\r\n            return false;\r\n        }\r\n\r\n        for(int i = 0;i < interests.size();i ++){\r\n\r\n            logger.info(\"old interest[{\" + i + \"}] : \"+ ((BigDecimal)interests.get(i)));\r\n            logger.info(\"old principal[{\" + i + \"}] : \"+ ((BigDecimal)interests.get(i)));\r\n\r\n            BigDecimal interest = ((BigDecimal)interests.get(i)).setScale(2, BigDecimal.ROUND_HALF_UP);\r\n            BigDecimal principal = ((BigDecimal)principals.get(i)).multiply(new BigDecimal(0.24)).setScale(2, BigDecimal.ROUND_HALF_UP);\r\n\r\n            logger.info(\"after calculating, the interest[{\" + i + \"}] : \"+ interest);\r\n            logger.info(\"after calculating, the principal[{\" + i + \"}] : \"+ principal);\r\n\r\n            if(interest.compareTo(principal) <= 0 ? false :true){\r\n                logger.info(\"计划还款利息 > 计划还款本金 * 24%\");\r\n                return false;\r\n            }\r\n        }\r\n        return true;\r\n    }', 'f2172aed-c2c7-11e7-a1b1-502b73c136df', NULL, '2017-11-06 14:10:20', NULL, 'huangwr', 'com.suidifu.matryoshka.customize.CustomizeServices,com.suidifu.matryoshka.handler.SandboxDataSetHandler,com.zufangbao.sun.utils.JsonUtils,java.math.BigDecimal,java.util.List,java.util.Map');


INSERT INTO t_api_config (api_url, fn_code, description, api_status)
VALUES
  ('/api/v3/modifyRepaymentInformation', NULL, '变更还款信息', 1);

INSERT INTO dictionary (id, code, content)
VALUES
  (11, 'PLATFORM_PRI_KEY',
   'MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=');


INSERT INTO t_merchant_config (id, mer_id, secret, company_id, pub_key_path, pub_key, project_code)
VALUES
  (2, 't_test_zfb', '123456', 3, NULL,
   'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDl1BjinRJLLDiNc6jcOKW+nph9aNSWMaKMk0OxTdSATakyS7rwNxrLMFyJLkI9IpnHussBv1zgsHPUdZeRcHDkbcMdhYoRgpe3gZIVMJ09BMBjhAET4fensvk377L0Whzp+u9r9UxIWowH7YJuJe3yQ7R3RgYxzrPuTJYq/WeuUQIDAQAB',
   'spdbank');


INSERT INTO t_api_config (api_url, fn_code, description, api_status)
VALUES
  ('/api/v3/importAssetPackage', NULL, '导入资产包', 1);

INSERT INTO financial_contract (id, asset_package_format, adva_matuterm, adva_start_date, contract_no, contract_name, app_id, company_id, adva_repo_term, thru_date, capital_account_id, financial_contract_type, loan_overdue_start_day, loan_overdue_end_day, payment_channel_id, ledger_book_no, financial_contract_uuid, sys_normal_deduct_flag, sys_overdue_deduct_flag, sys_create_penalty_flag, sys_create_guarantee_flag, unusual_modify_flag, sys_create_statement_flag, transaction_limit_per_transcation, transaction_limit_per_day, remittance_strategy_mode, app_account_uuids, allow_online_repayment, allow_offline_repayment, allow_advance_deduct_flag, adva_repayment_term, penalty, overdue_default_fee, overdue_service_fee, overdue_other_fee, create_time, last_modified_time, repurchase_approach, repurchase_rule, repurchase_algorithm, day_of_month, pay_for_go, repurchase_principal_algorithm, repurchase_interest_algorithm, repurchase_penalty_algorithm, repurchase_other_charges_algorithm, temporary_repurchases, repurchase_cycle, allow_freewheeling_repayment, days_of_cycle, repayment_check_days)
VALUES
  ('1', '0', '3', '2016-05-17 00:00:00', 'G0000000', '用钱宝测试', '1', '1', '30', '2099-12-01 00:00:00', '1', '0', '0', '0',
                                                                                                          '0',
                                                                                                          'e17d8068-46a0-48bb-b158-8e80168bbfc3',
                                                                                                          'uuid', '0',
                                                                                                          '0', '0', '0',
    '0', '0', NULL, NULL, NULL, NULL, '0', '0', '0', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
    '0', NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, '-1');

INSERT INTO app (id, app_id, app_secret, is_disabled, host, name, company_id, addressee)
VALUES
  (1, 'nongfenqi', '11111db75ebb24fa0993f4fa25775023', 1, 'http://e.zufangbao.cn', '农分期', 5, NULL);

INSERT INTO company (id, address, full_name, short_name)
VALUES
  (1, '上海', '云南国际信托有限公司', '云南信托'),
  (5, '杭州', '农分期', '农分期');

INSERT INTO bank (id, bank_code, bank_name)
VALUES
  (10, 'C10102', '中国工商银行 ');
INSERT INTO province (id, code, is_deleted, name)
VALUES
  (1, '330000', 00000000, '浙江省');
INSERT INTO city (id, code, is_deleted, name, province_code)
VALUES
  (89, '330300', 00000000, '温州市', '330000');

INSERT INTO ledger_book (id, ledger_book_no, ledger_book_orgnization_id, book_master_id, party_concerned_ids)
VALUES ('37', 'e17d8068-46a0-48bb-b158-8e80168bbfc3', '1', NULL, NULL);

INSERT INTO asset_set (id, guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, financial_contract_uuid, contract_uuid, repay_schedule_no)
VALUES
  (148660, 0, 0, 0, 80000.00, 10000.00, 90000.00, '2018-11-10', '2018-10-30', 0.00, 0, 1, 0, NULL,
                                                                                       '890d41d9-2484-46bb-a856-e987ef1da40e',
                                                                                       '2016-10-25 11:06:48',
                                                                                       '2016-10-25 11:14:01', NULL,
                                                                                       'ZC275016985BF712EB', 54340,
                                                                                       '2018-10-25 11:14:01', 1, 0,
   NULL, 1, 0, 0, 'b674a323-0c30-4e4b-9eba-b14e05a9d80a', '3e8711d4-9573-4965-a878-480ee4c1f5fc',
   '435ef1457d6fc7a2bdba46e5a3dd0ed1');

SET FOREIGN_KEY_CHECKS = 1;