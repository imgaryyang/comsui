SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM `financial_contract`;
DELETE FROM `app`;
DELETE FROM `account`;
DELETE FROM `app_account`;
DELETE FROM `ledger_book`;
DELETE FROM `company`;
DELETE FROM `payment_channel`;
DELETE FROM `customer`;

INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`, `day_of_month`, `pay_for_go`, `repurchase_principal_algorithm`, `repurchase_interest_algorithm`, `repurchase_penalty_algorithm`, `repurchase_other_charges_algorithm`, `repayment_check_days`, `repurchase_cycle`, `days_of_cycle`, `temporary_repurchases`, `allow_freewheeling_repayment`) VALUES ('38', '0', '0', '2016-09-01 00:00:00', 'G31700', '拍拍贷测试', '3', '1', '10', '2017-08-31 00:00:00', '102', '0', '2', '9', '1', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '0', '0', '1', '1', '1', '0', NULL, NULL, NULL, NULL, '0', '1', '1', '1', '', NULL, NULL, NULL, NULL, '2017-03-16 21:41:08', '1', '1', 'outstandingPrincipal+outstandingInterest+outstandingPenaltyInterest', '6', '1', NULL, NULL, NULL, NULL, '-1', NULL, NULL, NULL, '0');

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`) VALUES ('3', 'ppd', NULL, b'1', NULL, '测试商户ppd', '5', NULL);

INSERT INTO `account` (`id`, `account_name`, `account_no`, `bank_name`, `alias`, `attr`, `scan_cash_flow_switch`, `usb_key_configured`, `uuid`, `bank_code`, `cash_flow_config`) VALUES ('102', '云南信托国际有限公司', '600000000001', '平安银行深圳分行', NULL, NULL, b'0', b'0', 'bd7dd5b1-aa53-4faf-9647-00ddb8ab4b42', 'PAB', NULL);

INSERT INTO `app_account` (`id`, `uuid`, `bank_name`, `account_name`, `account_no`, `app_account_active_status`, `app_id`, `virtual_account_uuid`, `bank_card_status`, `id_card_num`, `bank_code`, `province`, `province_code`, `city`, `city_code`) VALUES ('3', 'uuid_3', NULL, '上海拍拍贷金融信息服务有限公司', '1001133419006708197', '0', '3', '1fb5cefb-5af3-40b6-994d-f3deec044870', '1', NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO `ledger_book` (`id`, `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`) VALUES ('37', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', NULL, NULL);

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`) VALUES ('1', '上海', '测试金融公司', '测试金融', 'a02c02b9-6f98-11e6-bf08-00163e002839');
INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`) VALUES ('5', '杭州', 'ppd', 'ppd', '1f871f9b-7404-11e6-bf08-00163e002839');

INSERT INTO `payment_channel` (`id`, `channel_name`, `user_name`, `user_password`, `merchant_id`, `cer_file_path`, `pfx_file_path`, `pfx_file_key`, `payment_channel_type`, `api_url`, `from_date`, `thru_date`) VALUES ('1', '测试通道1', 'operator', 'operator', '001053110000001', '/data/webapps/tomcat-earth/webapps/earth/WEB-INF/classes/certificate/gzdsf.cer', '/data/webapps/tomcat-earth/webapps/earth/WEB-INF/classes/certificate/ORA@TEST1.pfx', '123456', '0', 'http://59.41.103.98:333/gzdsf/ProcessServlet', NULL, NULL);
INSERT INTO `payment_channel` (`id`, `channel_name`, `user_name`, `user_password`, `merchant_id`, `cer_file_path`, `pfx_file_path`, `pfx_file_key`, `payment_channel_type`, `api_url`, `from_date`, `thru_date`) VALUES ('2', '测试分期通道2', 'operator', 'operator', '001053110000001', '/data/webapps/tomcat-earth/webapps/earth/WEB-INF/classes/certificate/gzdsf.cer', '/data/webapps/tomcat-earth/webapps/earth/WEB-INF/classes/certificate/ORA@TEST1.pfx', '123456', '0', 'http://59.41.103.98:333/gzdsf/ProcessServlet', NULL, NULL);
INSERT INTO `payment_channel` (`id`, `channel_name`, `user_name`, `user_password`, `merchant_id`, `cer_file_path`, `pfx_file_path`, `pfx_file_key`, `payment_channel_type`, `api_url`, `from_date`, `thru_date`) VALUES ('3', 'G31700广银联代收付', NULL, NULL, '000191400206128', NULL, NULL, NULL, '0', NULL, NULL, NULL);

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`) VALUES ('1', '330683199403062411', NULL, '测试员1001', '123456', '3', '011d17a7-ac4e-40c0-8f43-76092aa47115', '0');
INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`) VALUES ('2', '330683199403062411', NULL, '测试员1002', '123456', '3', 'fdd0d6d4-e10e-4b4d-a5c5-f591b2fc7d3c', '0');
INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`) VALUES ('3', '330683199403062411', NULL, '测试员1003', '123456', '3', '15449ef7-1c9f-4ed4-b567-8510f5d9d6c1', '0');
INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`) VALUES ('4', '330683199403062222', NULL, '测试员1004', '123456', '3', '4d09ab0d-774e-4b1b-8177-b5dd91e607e9', '1');



SET FOREIGN_KEY_CHECKS = 1;
