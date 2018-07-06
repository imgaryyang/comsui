SET FOREIGN_KEY_CHECKS=0;

DELETE FROM galaxy_autotest_yunxin.cash_flow;
DELETE FROM galaxy_autotest_yunxin.financial_contract;
DELETE FROM galaxy_autotest_yunxin.app;
DELETE FROM galaxy_autotest_yunxin.company;
DELETE FROM `account`;

INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`, `day_of_month`, `pay_for_go`, `repurchase_principal_algorithm`, `repurchase_interest_algorithm`, `repurchase_penalty_algorithm`, `repurchase_other_charges_algorithm`)
VALUES
  (38, 0, 0, '2016-09-01 00:00:00', 'G31700', '拍拍贷测试', 3, 1, 10, '2097-08-31 00:00:00', 102, 0, 2, 9, 1,
                                                                                             '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', 'd2812bc5-5057-4a91-b3fd-9019506f0499', 0, 0, 1, 1, 1, 0, NULL, NULL, NULL, NULL, 0, 1, 1, 1, '', NULL, NULL, NULL, NULL, '2017-03-16 21:41:08', 1, 1, 'outstandingPrincipal+outstandingInterest+outstandingPenaltyInterest', 6, 1, 'outstandingPrincipal', 'outstandingInterest', 'outstandingPenaltyInterest', NULL);
INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`)
VALUES
  (3, 'ppd', NULL, 1, NULL, '测试商户ppd', 5, NULL);

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`)
VALUES
  (1, '上海', '测试金融公司', '测试金融', 'a02c02b9-6f98-11e6-bf08-00163e002839'),
  (5, '杭州', 'ppd', 'ppd', '1f871f9b-7404-11e6-bf08-00163e002839');

 INSERT INTO `account` (`id`, `account_name`, `account_no`, `bank_name`, `alias`, `attr`, `scan_cash_flow_switch`, `usb_key_configured`, `uuid`, `bank_code`, `cash_flow_config`) 
 VALUES 
 	('102', 'test_account', 'test_receivable_account_no_1', '中国建设银行', NULL, NULL, b'0', b'0', 'test_account_uuid', NULL, NULL);



SET FOREIGN_KEY_CHECKS=1;