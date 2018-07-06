USE galaxy_autotest_yunxin;
DELETE FROM dictionary;
DELETE FROM customer;
DELETE FROM financial_contract;
DELETE FROM ledger_book;
DELETE FROM contract;
DELETE FROM asset_package;
DELETE FROM company;
DELETE FROM app;
DELETE FROM asset_set;
DELETE FROM asset_set_extra_charge;
DELETE FROM ledger_book_shelf;
DELETE FROM finance_company;
DELETE FROM account;
DELETE FROM principal;
DELETE FROM t_api_config;

DELETE from t_product_category;
DELETE from t_source_repository;

INSERT INTO `t_product_category` (`id`, `uuid`, `product_lv_1_code`, `product_lv_1_name`, `product_lv_2_code`, `product_lv_2_name`, `product_lv_3_code`, `product_lv_3_name`, `pre_process_interface_url`, `post_process_interface_url`, `pre_process_script`, `status`, `script_md_5_version`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`, `delay_task_config_uuid`) VALUES ('118', '2d376396-c38f-11e7-a1b1-502b73c136df', 'weifang', '潍坊', 'importAssetPackage', '导入资产包', '10003', '提前还款', 'importAssetPackage/weifang/10003', NULL, NULL, '1', '21f9ee54-c390-11e7-a1b1-502b73c136df', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_product_category` (`id`, `uuid`, `product_lv_1_code`, `product_lv_1_name`, `product_lv_2_code`, `product_lv_2_name`, `product_lv_3_code`, `product_lv_3_name`, `pre_process_interface_url`, `post_process_interface_url`, `pre_process_script`, `status`, `script_md_5_version`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`, `delay_task_config_uuid`) VALUES ('117', '066aca36-c2bb-11e7-a1b1-502b73c136df', 'weifang', '潍坊', 'importAssetPackage', '导入资产包', '10001', '111', 'importAssetPackage/weifang/10001', NULL, NULL, '1', '857986d8-c2bd-11e7-a1b1-502b73c136df', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_source_repository` (`id`, `business_type`, `source_code`, `signature`, `source_file_path`, `add_time`, `last_modify_time`, `author`, `compile_import`) VALUES ('14', 'importAssetPackage/weifang/10003', 'public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest, Map<String, String> postRequest, Log logger) {\r\n        logger.info(\"start to get param\");\r\n        logger.info(\"param : \" + preRequest);\r\n        String uniqueId = (String) preRequest.getOrDefault(\"uniqueId\", \"\");\r\n        String contractNo = (String) preRequest.getOrDefault(\"contractNo\", \"\");\r\n        logger.info(\"uniqueId,contractNo:\" + uniqueId + \",\" + contractNo);\r\n        if(StringUtils.isEmpty(uniqueId)&&StringUtils.isEmpty(contractNo)){\r\n            logger.error(\"uniqueId,contractNo 至少一个不能为空\");\r\n            return false;\r\n        }\r\n        SandboxDataSet sandboxDataSet = sandboxDataSetHandler.get_sandbox_by_contract_uniqueId_contractNo(uniqueId, contractNo);\r\n        if(sandboxDataSet == null){\r\n            logger.error(\"sandboxDataSet is null\");\r\n            return false;\r\n        }\r\n\r\n        ContractSnapshot contractSnapshot = sandboxDataSet.getContractSnapshot();\r\n        String jsonAssetRecycleDate = (String) preRequest.getOrDefault(\"assetRecycleDate\", \"\");\r\n        if(StringUtils.isEmpty(jsonAssetRecycleDate)){\r\n            logger.error(\"jsonAssetRecycleDate is empty\");\r\n            return false;\r\n        }\r\n        logger.info(\"jsonAssetRecycleDate : \" + jsonAssetRecycleDate);\r\n\r\n        List<String> assetRecycleDates = JsonUtils.parseArray(jsonAssetRecycleDate, String.class);\r\n        logger.info(\"BeginDate : \"+ contractSnapshot.getBeginDate());\r\n        if(contractSnapshot.getBeginDate() == null){\r\n            logger.error(\"BeginDate is empty\");\r\n            return false;\r\n        }\r\n\r\n        String jsonInterest = (String) preRequest.getOrDefault(\"interest\", \"\");\r\n        String jsonPrincipal = (String) preRequest.getOrDefault(\"principal\", \"\");\r\n\r\n        logger.info(\"jsonInterest : \" + jsonInterest);\r\n        logger.info(\"jsonPrincipal : \" + jsonPrincipal);\r\n\r\n        List<BigDecimal> interests = JsonUtils.parseArray(jsonInterest, BigDecimal.class);\r\n        List<BigDecimal> principals = JsonUtils.parseArray(jsonPrincipal, BigDecimal.class);\r\n        if(interests == null){\r\n            logger.info(\"interests is null\");\r\n            return false;\r\n        }\r\n        if(principals == null){\r\n            logger.info(\"principals is null\");\r\n            return false;\r\n        }\r\n\r\n        for(int i = 0;i < interests.size();i ++){\r\n            int days = DateUtils.compareTwoDatesOnDay(DateUtils.asDay((String)assetRecycleDates.get(i)), contractSnapshot.getBeginDate());\r\n            logger.info(\"应计利息天数 days[{\" + i + \"}] : \" + days);\r\n\r\n            logger.info(\"old interest[{\" + i + \"}] : \"+ ((BigDecimal) interests.get(i)));\r\n            logger.info(\"old principal[{\" + i + \"}] : \"+ ((BigDecimal) principals.get(i)));\r\n\r\n            BigDecimal interest = ((BigDecimal) interests.get(i)).setScale(2, BigDecimal.ROUND_HALF_UP);\r\n            BigDecimal principal = ((BigDecimal) principals.get(i)).multiply(new BigDecimal(0.24*days/360)).setScale(2, BigDecimal.ROUND_HALF_UP);\r\n\r\n            logger.info(\"after calculating, the interest[{\" + i + \"}] : \"+ interest);\r\n            logger.info(\"after calculating, the principal[{\" + i + \"}] : \"+ principal);\r\n            if(interest.compareTo(principal) == 0 ? false :true){\r\n                return false;\r\n            }\r\n        }\r\n        return true;\r\n    }', '3ebe33f813f7570191eea79fcc89e078', NULL, '2017-11-07 11:00:38', '2017-11-07 11:02:36', 'zhushiyun', 'com.suidifu.matryoshka.customize.CustomizeServices,java.math.*,java.util.*,com.suidifu.matryoshka.snapshot.ContractSnapshot,com.suidifu.matryoshka.snapshot.SandboxDataSet,com.zufangbao.sun.utils.*');
INSERT INTO `t_source_repository` (`id`, `business_type`, `source_code`, `signature`, `source_file_path`, `add_time`, `last_modify_time`, `author`, `compile_import`) VALUES ('11', 'importAssetPackage/weifang/10001', 'public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest, Map<String, String> postRequest, Log logger) {\r\n        logger.info(\"start to get param\");\r\n        logger.info(\"param : \" + preRequest);\r\n        String jsonInterest = (String) preRequest.getOrDefault(\"interest\", \"\");\r\n        String jsonPrincipal = (String) preRequest.getOrDefault(\"principal\", \"\");\r\n\r\n        logger.info(\"jsonInterest : \" + jsonInterest);\r\n        logger.info(\"jsonPrincipal : \" + jsonPrincipal);\r\n\r\n        List<BigDecimal> interests = JsonUtils.parseArray(jsonInterest, BigDecimal.class);\r\n        List<BigDecimal> principals = JsonUtils.parseArray(jsonPrincipal, BigDecimal.class);\r\n\r\n        logger.info(\"interests : \" + interests);\r\n        logger.info(\"principals : \" + principals);\r\n        if(interests == null){\r\n            logger.info(\"interests is null\");\r\n            return false;\r\n        }\r\n        if(principals == null){\r\n            logger.info(\"principals is null\");\r\n            return false;\r\n        }\r\n\r\n        for(int i = 0;i < interests.size();i ++){\r\n\r\n            logger.info(\"old interest[{\" + i + \"}] : \"+ ((BigDecimal)interests.get(i)));\r\n            logger.info(\"old principal[{\" + i + \"}] : \"+ ((BigDecimal)interests.get(i)));\r\n\r\n            BigDecimal interest = ((BigDecimal)interests.get(i)).setScale(2, BigDecimal.ROUND_HALF_UP);\r\n            BigDecimal principal = ((BigDecimal)principals.get(i)).multiply(new BigDecimal(0.24)).setScale(2, BigDecimal.ROUND_HALF_UP);\r\n\r\n            logger.info(\"after calculating, the interest[{\" + i + \"}] : \"+ interest);\r\n            logger.info(\"after calculating, the principal[{\" + i + \"}] : \"+ principal);\r\n\r\n            if(interest.compareTo(principal) <= 0 ? false :true){\r\n                logger.info(\"计划还款利息 > 计划还款本金 * 24%\");\r\n                return false;\r\n            }\r\n        }\r\n        return true;\r\n    }', 'f2172aed-c2c7-11e7-a1b1-502b73c136df', NULL, '2017-11-06 14:10:20', NULL, 'huangwr', 'com.suidifu.matryoshka.customize.CustomizeServices,com.suidifu.matryoshka.handler.SandboxDataSetHandler,com.zufangbao.sun.utils.JsonUtils,java.math.BigDecimal,java.util.List,java.util.Map');


INSERT INTO t_api_config (api_url, fn_code, description, api_status)
VALUES
  ('/api/v3/modifyRepaymentPlan', NULL, '变更还款计划', 1);

INSERT INTO principal (id, authority, name, password, start_date, thru_date, t_user_id, created_time, creator_id, modify_password_time)
VALUES (1, 'ROLE_SUPER_USER', 'zhanghongbing', '376c43878878ac04e05946ec1dd7a55f', NULL, NULL, 2, NULL, NULL, 1);

INSERT INTO dictionary (code, content)
VALUES
  ('PLATFORM_PRI_KEY',
   'MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=');
INSERT INTO customer (id, app_id)
VALUES
  (54349, 2);
INSERT INTO financial_contract (id, asset_package_format, adva_matuterm, adva_start_date, contract_no, contract_name, app_id, company_id, adva_repo_term, thru_date, capital_account_id, financial_contract_type, loan_overdue_start_day, loan_overdue_end_day, payment_channel_id, ledger_book_no, financial_contract_uuid, sys_normal_deduct_flag, sys_overdue_deduct_flag, sys_create_penalty_flag, sys_create_guarantee_flag, unusual_modify_flag, sys_create_statement_flag, adva_repayment_term)
VALUES
  (37, 0, 3, '2018-09-01 00:00:00', 'G32000', '用钱宝测试', 2, 1, 60, '2018-12-01 00:00:00', 101, 0, 1, 2, 1,
                                                                                             'e17d8068-46a0-48bb-b158-8e80168bbfc3',
                                                                                             'b674a323-0c30-4e4b-9eba-b14e05a9d80a',
                                                                                             0, 0, 0, 0, 1, 0, 7);

INSERT INTO ledger_book (id, ledger_book_no, ledger_book_orgnization_id, book_master_id, party_concerned_ids)
VALUES
  (36, 'e17d8068-46a0-48bb-b158-8e80168bbfc3', '1', NULL, NULL);
INSERT INTO contract (id, uuid, unique_id, begin_date, contract_no, end_date, asset_type, month_fee, app_id, customer_id, house_id, actual_end_date, create_time, interest_rate, payment_day_in_month, payment_frequency, periods, repayment_way, total_amount, penalty_interest, active_version_no, repayment_plan_operate_logs, state, financial_contract_uuid)
VALUES
  (54340, '3e8711d4-9573-4965-a878-480ee4c1f5fc', 'e568793f-a44c-4362-9e78-0ce433131f3e', '2016-09-10',
          '云信信2016-241-DK(428522112675736881)', '2018-12-09', NULL, 0.00, 2, 54349, 54505, NULL, '2016-10-25 11:06:48',
                                                                                           0.8923000000, 0, 0, 2, 2,
                                                                                           4500.00, 0.0050000000, 1,
   NULL, 2, 'b674a323-0c30-4e4b-9eba-b14e05a9d80a'),
  (54341, '4e8711d4-9573-4965-a878-480ee4c1f5fc', 'e668793f-a44c-4362-9e78-0ce433131f3e', '2016-09-10',
          '云信信2016-241-DK(428522112675736882)', '2018-12-09', NULL, 0.00, 2, 54349, 54505, NULL, '2016-10-25 11:06:48',
                                                                                           0.8923000000, 0, 0, 2, 2,
                                                                                           4500.00, 0.0050000000, 1,
   NULL, 2, 'b674a323-0c30-4e4b-9eba-b14e05a9d80a');

INSERT INTO asset_package (id, is_available, create_time, contract_id, asset_package_no, financial_contract_id, loan_batch_id)
VALUES
  (54326, NULL, NULL, 54340, NULL, 37, 50131);

INSERT INTO account (id, account_name, account_no, bank_name, alias, attr, scan_cash_flow_switch, usb_key_configured, uuid, bank_code)
VALUES
  (101, '云南信托', '20001', '平安银行深圳分行', NULL, NULL, 0, 0, '8e3cd5c5-8fb6-4cd6-b6c7-660a9f35f47c', NULL);

INSERT INTO finance_company (id, company_id)
VALUES
  (1, 1);

INSERT INTO app (id, app_id, app_secret, is_disabled, host, name, company_id, addressee)
VALUES
  (2, 'qyb', NULL, 1, NULL, '测试商户yqb', 4, NULL);

INSERT INTO company (id, address, full_name, short_name, uuid)
VALUES
  (1, '上海', '测试金融公司', '测试金融', 'a02c02b9-6f98-11e6-bf08-00163e002839'),
  (4, '杭州', 'yqb', 'yqb', 'a02c08b5-6f98-11e6-bf08-00163e002839');


INSERT INTO asset_set (id, guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, financial_contract_uuid, contract_uuid,asset_finger_print,asset_extra_fee_finger_print)
VALUES
  (148660, 0, 0, 0, 160000.00, 10000.00, 90000.00, '2018-11-10', '2018-10-30', 0.00, 0, 1, 0, NULL,
                                                                                        '890d41d9-2484-46bb-a856-e987ef1da40e',
                                                                                        '2016-10-25 11:06:48',
                                                                                        '2016-10-25 11:14:01', NULL,
                                                                                        'ZC275016985BF712EB', 54340,
                                                                                        '2016-10-25 11:14:01', 1, 0,
   NULL, 1, 0, 0, 'b674a323-0c30-4e4b-9eba-b14e05a9d80a', '3e8711d4-9573-4965-a878-480ee4c1f5fc','9dd2c07aa77474c8cf09efc0407d4d6c','1a3e1fdae2512718d0cadfbe288b30dd'),
  (148662, 0, 0, 0, 80000.00, 10000.00, 90000.00, '2018-12-10', '2018-12-30', 0.00, 0, 1, 0, NULL,
                                                                                       '190d41d9-2484-46bb-a856-e987ef1da40e',
                                                                                       '2016-10-25 11:06:48',
                                                                                       '2016-10-25 11:14:01', NULL,
                                                                                       'ZC275016985BF712ED', 54341,
                                                                                       '2016-10-25 11:14:01', 1, 0,
   NULL, 1, 0, 0, 'b674a323-0c30-4e4b-9eba-b14e05a9d80a', '4e8711d4-9573-4965-a878-480ee4c1f5fc','','');

INSERT INTO asset_set_extra_charge (id, asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES
  (381437, '2afe06f0-5499-477a-b81a-4dd300416a86', '890d41d9-2484-46bb-a856-e987ef1da40e', '2016-10-25 11:06:48',
           '2016-10-25 11:06:48', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_OTHER_FEE', '10000.05',
           NULL, NULL, 3.60),
  (381438, 'b6eea1ba-b327-4970-ad81-973eb84042e2', '890d41d9-2484-46bb-a856-e987ef1da40e', '2016-10-25 11:06:48',
           '2016-10-25 11:06:48', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_TECH_FEE', '10000.04',
           NULL, NULL, 2.50);


INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  (2055424, '4c2cb54a-0c67-4118-bb95-72f2616bd55f', 0.00, 1900.00, 'FST_LONGTERM_LIABILITY', '40000', 0,
            'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL, 'a02c02b9-6f98-11e6-bf08-00163e002839',
                                                                            'cc384fb8-0fcc-4632-b723-bafde8dca88e',
                                                                            NULL, NULL, NULL,
                                                                            'd792767d-beb4-4862-89a9-6a15ed739456',
                                                                            '2016-11-14', '2016-10-25 11:06:48', '',
                                                                            NULL, 54340,
                                                                                  '3e8711d4-9573-4965-a878-480ee4c1f5fc',
                                                                                  '2016-11-14 00:00:00', '',
                                                                                  'e17d8068-46a0-48bb-b158-8e80168bbfc3',
                                                                                  '1', 1, 'ZC275016985BF712EB',
                                                                                  '890d41d9-2484-46bb-a856-e987ef1da40e',
                                                                                  NULL, NULL, NULL, NULL, ''),
  (2055425, '5040cef6-02a0-4e56-aa79-a7ba78f8c02e', 0.00, 1.00, 'FST_DEFERRED_INCOME', '100000', 0,
            'SND_DEFERRED_INCOME_INTEREST_ESTIMATE', '100000.01', NULL, NULL, 'a02c02b9-6f98-11e6-bf08-00163e002839',
                                                                              'cc384fb8-0fcc-4632-b723-bafde8dca88e',
                                                                              NULL, NULL, NULL,
                                                                              'd792767d-beb4-4862-89a9-6a15ed739456',
                                                                              '2016-11-14', '2016-10-25 11:06:48', '',
                                                                              NULL, 54340,
                                                                                    '3e8711d4-9573-4965-a878-480ee4c1f5fc',
                                                                                    '2016-11-14 00:00:00', '',
                                                                                    'e17d8068-46a0-48bb-b158-8e80168bbfc3',
                                                                                    '1', 1, 'ZC275016985BF712EB',
                                                                                    '890d41d9-2484-46bb-a856-e987ef1da40e',
                                                                                    NULL, NULL, NULL, NULL, ''),
  (2055426, 'e8919a9d-26f2-420e-9611-3d41745c217f', 2.50, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
            'SND_UNEARNED_LOAN_ASSET_TECH_FEE', '10000.04', NULL, NULL, 'a02c02b9-6f98-11e6-bf08-00163e002839',
                                                                        'cc384fb8-0fcc-4632-b723-bafde8dca88e', NULL,
                                                                        NULL, NULL,
                                                                        'd792767d-beb4-4862-89a9-6a15ed739456',
                                                                        '2016-11-14', '2016-10-25 11:06:48', '', NULL,
    54340, '3e8711d4-9573-4965-a878-480ee4c1f5fc', '2016-11-14 00:00:00', '', 'e17d8068-46a0-48bb-b158-8e80168bbfc3',
    '1', 1, 'ZC275016985BF712EB', '890d41d9-2484-46bb-a856-e987ef1da40e', NULL, NULL, NULL, NULL, ''),
  (2055427, '2e63f0bb-737a-4bef-beb4-c2885b5f1ddf', 1900.00, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
            'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, 'a02c02b9-6f98-11e6-bf08-00163e002839',
                                                                         'cc384fb8-0fcc-4632-b723-bafde8dca88e', NULL,
                                                                         NULL, NULL,
                                                                         'd792767d-beb4-4862-89a9-6a15ed739456',
                                                                         '2016-11-14', '2016-10-25 11:06:48', '', NULL,
    54340, '3e8711d4-9573-4965-a878-480ee4c1f5fc', '2016-11-14 00:00:00', '', 'e17d8068-46a0-48bb-b158-8e80168bbfc3',
    '1', 1, 'ZC275016985BF712EB', '890d41d9-2484-46bb-a856-e987ef1da40e', NULL, NULL, NULL, NULL, ''),
  (2055428, 'b669c37c-2b55-49cd-86bf-5a39768c0c3d', 0.00, 3.60, 'FST_DEFERRED_INCOME', '100000', 0,
            'SND_DEFERRED_INCOME_FEE', '100000.02', 'TRD_DEFERRED_INCOME_LOAN_OTHER_FEE', '100000.02.03',
    'a02c02b9-6f98-11e6-bf08-00163e002839', 'cc384fb8-0fcc-4632-b723-bafde8dca88e', NULL, NULL, NULL,
    'd792767d-beb4-4862-89a9-6a15ed739456', '2016-11-14', '2016-10-25 11:06:48', '', NULL, 54340,
                                                                                           '3e8711d4-9573-4965-a878-480ee4c1f5fc',
                                                                                           '2016-11-14 00:00:00', '',
                                                                                           'e17d8068-46a0-48bb-b158-8e80168bbfc3',
                                                                                           '1', 1, 'ZC275016985BF712EB',
                                                                                           '890d41d9-2484-46bb-a856-e987ef1da40e',
                                                                                           NULL, NULL, NULL, NULL, ''),
  (2055429, '05ce8a7a-e45e-48e1-9f40-117790dfdcac', 0.00, 2.50, 'FST_DEFERRED_INCOME', '100000', 0,
            'SND_DEFERRED_INCOME_FEE', '100000.02', 'TRD_DEFERRED_INCOME_LOAN_TECH_FEE', '100000.02.02',
    'a02c02b9-6f98-11e6-bf08-00163e002839', 'cc384fb8-0fcc-4632-b723-bafde8dca88e', NULL, NULL, NULL,
    'd792767d-beb4-4862-89a9-6a15ed739456', '2016-11-14', '2016-10-25 11:06:48', '', NULL, 54340,
                                                                                           '3e8711d4-9573-4965-a878-480ee4c1f5fc',
                                                                                           '2016-11-14 00:00:00', '',
                                                                                           'e17d8068-46a0-48bb-b158-8e80168bbfc3',
                                                                                           '1', 1, 'ZC275016985BF712EB',
                                                                                           '890d41d9-2484-46bb-a856-e987ef1da40e',
                                                                                           NULL, NULL, NULL, NULL, ''),
  (2055430, '439f543a-7f42-4ca2-bcd5-f065e8f3d04f', 3.60, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
            'SND_UNEARNED_LOAN_ASSET_OTHER_FEE', '10000.05', NULL, NULL, 'a02c02b9-6f98-11e6-bf08-00163e002839',
                                                                         'cc384fb8-0fcc-4632-b723-bafde8dca88e', NULL,
                                                                         NULL, NULL,
                                                                         'd792767d-beb4-4862-89a9-6a15ed739456',
                                                                         '2016-11-14', '2016-10-25 11:06:48', '', NULL,
    54340, '3e8711d4-9573-4965-a878-480ee4c1f5fc', '2016-11-14 00:00:00', '', 'e17d8068-46a0-48bb-b158-8e80168bbfc3',
    '1', 1, 'ZC275016985BF712EB', '890d41d9-2484-46bb-a856-e987ef1da40e', NULL, NULL, NULL, NULL, ''),
  (2055431, 'eff2a9f1-1c80-456c-8939-b6425f6d79f1', 1.00, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
            'SND_UNEARNED_LOAN_ASSET_INTEREST', '10000.01', NULL, NULL, 'a02c02b9-6f98-11e6-bf08-00163e002839',
                                                                        'cc384fb8-0fcc-4632-b723-bafde8dca88e', NULL,
                                                                        NULL, NULL,
                                                                        'd792767d-beb4-4862-89a9-6a15ed739456',
                                                                        '2016-11-14', '2016-10-25 11:06:48', '', NULL,
    54340, '3e8711d4-9573-4965-a878-480ee4c1f5fc', '2016-11-14 00:00:00', '', 'e17d8068-46a0-48bb-b158-8e80168bbfc3',
    '1', 1, 'ZC275016985BF712EB', '890d41d9-2484-46bb-a856-e987ef1da40e', NULL, NULL, NULL, NULL, '');