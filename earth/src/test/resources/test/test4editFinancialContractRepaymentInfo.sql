SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM financial_contract;
DELETE FROM company;
DELETE FROM app;
DELETE FROM account;


INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`, `day_of_month`, `pay_for_go`, `repurchase_principal_algorithm`, `repurchase_interest_algorithm`, `repurchase_penalty_algorithm`, `repurchase_other_charges_algorithm`, `repayment_check_days`, `repurchase_cycle`, `days_of_cycle`, `allow_freewheeling_repayment`, `temporary_repurchases`)
VALUES
  ('108', '0', '0', '2016-03-28 00:00:00', 'xiewenqian', 'test_repurchase', '1', '1', '3', '2019-03-30 00:00:00', '5',
    '0', '2', '2', NULL, '053aa76e-3248-432f-be00-2db73d4329de', 'e2c178ac-be53-4574-b936-c5d40ba00691', '1', '1', '1',
    '0', '0', '0', '1.00', '6.00', '0', 'null', '1', '1', '1', '1', '', NULL, NULL, NULL, '2017-05-02 14:54:19',
                                                                    '2017-05-02 15:36:24', '1', '1', '', NULL, '0', '',
                                                                                                               '', '',
                                                                                                               '', '-1',
                                                                                                               '1',
                                                                                                               '[4,5]',
                                                                                                               '0',
                                                                                                               '[{\"effectEndDate\":1495036800000,\"effectStartDate\":1493568000000,\"repurchaseDate\":1493740800000,\"repurchaseUuid\":\"eec5b655-a2ef-4dd6-8dbb-b1dc3e371053\"},{\"effectEndDate\":1496160000000,\"effectStartDate\":1493654400000,\"repurchaseDate\":1495036800000,\"repurchaseUuid\":\"bda64296-e19d-49ed-81e4-c99628b3943d\"}]');

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`)
VALUES
  (1, '上海', '测试金融公司', '测试金融', 'a02c02b9-6f98-11e6-bf08-00163e002839'),
  (2, '上海', '测试金融公司', '测试金融', 'a02c02b9-6f98-11e6-bf08-00163e002838');

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`)
VALUES
  (1, 'nongfenqi', '', 1, '', '测试分期', 2, NULL),
  (2, 'paipaidai', '', 1, '', '测试分期', 2, NULL);

INSERT INTO `account` (`id`, `account_name`, `account_no`, `bank_name`, `alias`, `attr`, `scan_cash_flow_switch`, `usb_key_configured`, `uuid`, `bank_code`, `cash_flow_config`)
VALUES
  (5, '银企直连专用账户9', '591902896710201', '', '中国银行',
      '{\"bankBranchNo\":\"59\",\"usbUuid\":\"b173f24a-3c27-4243-85b7-e93ef6a128ac\"}', 1, 1, 'uuid_5', NULL, NULL);


SET FOREIGN_KEY_CHECKS = 1;