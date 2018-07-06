SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM financial_contract;
DELETE FROM company;
DELETE FROM app;
DELETE FROM account;


INSERT INTO `financial_contract` (`id`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`, `sys_create_statement_flag`, `asset_package_format`)
VALUES
  ('5', '3', '2016-04-01 00:00:00', 'nfqtest001', '测试合同', '1', '1', '91', '2016-07-01 00:00:00', '5', '0', '5', '90',
                                                                                                           '2',
                                                                                                           '74a9ce4d-cafc-407d-b013-987077541bdc',
                                                                                                           '2d380fe1-7157-490d-9474-12c5a9901e29',
                                                                                                           '1', '1',
                                                                                                           '1', '0',
                                                                                                           '0', 10, 50,
                                                                                                                0,
                                                                                                                'uuid_1',
                                                                                                                '1',
                                                                                                                '0',
                                                                                                                '0',
                                                                                                                '5',
                                                                                                                'h*h',
                                                                                                                '50',
   NULL, NULL, NULL, NULL, '0', '1'),
  ('6', '3', '2016-04-01 00:00:00', 'nfqtest001', '测试合同', '2', '2', '91', '2016-07-01 00:00:00', NULL, '0', '5', '90',
                                                                                                            '2',
                                                                                                            '74a9ce4d-cafc-407d-b013-987077541bdc',
                                                                                                            '2d380fe1-7157-490d-9474-12c5a9901e28',
                                                                                                            '1', '1',
                                                                                                            '1', '0',
                                                                                                            '0', 10, 50,
                                                                                                                 0,
                                                                                                                 'uuid_1',
                                                                                                                 '1',
                                                                                                                 '0',
                                                                                                                 '0',
                                                                                                                 '5',
                                                                                                                 'h*h',
                                                                                                                 '50',
   NULL, NULL, NULL, NULL, '0', '1');

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