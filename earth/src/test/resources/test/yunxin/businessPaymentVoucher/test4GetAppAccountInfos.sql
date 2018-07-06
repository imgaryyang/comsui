SET FOREIGN_KEY_CHECKS=0;

DELETE FROM `financial_contract`;
DELETE FROM `account`;
DELETE FROM `company`;
DELETE FROM `app_account`;
DELETE FROM `app`;
DELETE FROM `cash_flow`;

INSERT INTO financial_contract (asset_package_format, adva_matuterm, adva_start_date, contract_no, contract_name, app_id, company_id, adva_repo_term, thru_date, capital_account_id, financial_contract_type, loan_overdue_start_day, loan_overdue_end_day, payment_channel_id, ledger_book_no, financial_contract_uuid, sys_normal_deduct_flag, sys_overdue_deduct_flag, sys_create_penalty_flag, sys_create_guarantee_flag, unusual_modify_flag, sys_create_statement_flag, transaction_limit_per_transcation, transaction_limit_per_day, remittance_strategy_mode, app_account_uuids, allow_online_repayment, allow_offline_repayment, allow_advance_deduct_flag, adva_repayment_term, penalty, overdue_default_fee, overdue_service_fee, overdue_other_fee, create_time, last_modified_time, repurchase_approach, repurchase_rule, repurchase_algorithm, day_of_month, repurchase_principal_algorithm, repurchase_interest_algorithm, repurchase_penalty_algorithm, repurchase_other_charges_algorithm, pay_for_go) VALUES
(1, 3, '2016-04-01 00:00:00', 'nfqtest001', '测试合同', 1, 1, 91, '2016-07-01 00:00:00', 1, 0, 5, 90, 2, '74a9ce4d-cafc-407d-b013-987077541bdc', '2d380fe1-7157-490d-9474-12c5a9901e29', 0, 0, 0, 0, 0, 0, null, null, null, null, 0, 0, 0, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0);

INSERT INTO account (id, account_name, account_no, bank_name, alias, attr, scan_cash_flow_switch, usb_key_configured, uuid, bank_code, cash_flow_config) VALUES
(1,'银企直连专用账户9', '591902896710201', '', null, '{"bankBranchNo":"59","usbUuid":"b173f24a-3c27-4243-85b7-e93ef6a128ac"}', true, true, 'uuid_1', null, null);

INSERT INTO company (id, address, full_name, short_name, uuid) VALUES
(1,'上海', '测试金融公司', '测试金融', 'company_uuid_1');

INSERT INTO app_account (uuid, bank_name, account_name, account_no, app_account_active_status, app_id, virtual_account_uuid, bank_card_status, id_card_num, bank_code, province, province_code, city, city_code) VALUES
('uuid_1', '工商银行', '农分期', '10001', 0, 1, null, 1, null, null, null, null, null, null),
('uuid_2', '工商银行', '农分期', '10002', 0, 1, null, 1, null, null, null, null, null, null);

INSERT INTO app (id, app_id, app_secret, is_disabled, host, name, company_id, addressee) VALUES
(1,'nongfenqi', '', false, '', '测试分期', 1, null);

INSERT INTO cash_flow (cash_flow_uuid, cash_flow_channel_type, company_uuid, host_account_uuid, host_account_no, host_account_name, counter_account_no, counter_account_name, counter_account_appendix, counter_bank_info, account_side, transaction_time, transaction_amount, balance, transaction_voucher_no, bank_sequence_no, remark, other_remark, strike_balance_status, trade_uuid, issued_amount, audit_status, date_field_one, date_field_two, date_field_three, long_field_one, long_field_two, long_field_three, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two, decimal_field_three) VALUES
('cash_flow_uuid_1', 0, 'company_uuid_1', 'd0503298-e890-425a-3333333', '591902896710201', '银企直连专用账户9', '10002', '农分期', '', '', 1, '2016-09-03 10:51:02', 1001.00, 1000.00, '', 'cash_flow_no_14', '', '', null, '', 1000.00, 2, null, null, null, null, null, null, null, null, null, null, null, null),
('cash_flow_uuid_2', 0, 'company_uuid_1', 'd0503298-e890-425a-3333333', '591902896710201', '银企直连专用账户9', '10002', '农分期', '', '', 1, '2016-09-03 10:51:02', 1001.00, 1000.00, '', 'cash_flow_no_14', '', '', null, '', 1000.00, 2, null, null, null, null, null, null, null, null, null, null, null, null),
('cash_flow_uuid_3', 0, 'company_uuid_1', 'd0503298-e890-425a-3333333', '591902896710201', '银企直连专用账户9', '10002', '农分期', '', '', 1, '2016-09-03 10:51:02', 1001.00, 1000.00, '', 'cash_flow_no_14', '', '', null, '', 1000.00, 2, null, null, null, null, null, null, null, null, null, null, null, null),
('cash_flow_uuid_4', 0, 'company_uuid_1', 'd0503298-e890-425a-3333333', '591902896710201', '银企直连专用账户9', '10002', '农分期', '', '', 1, '2016-09-03 10:51:02', 1001.00, 1000.00, '', 'cash_flow_no_14', '', '', null, '', 1000.00, 2, null, null, null, null, null, null, null, null, null, null, null, null),
('cash_flow_uuid_5', 0, 'company_uuid_1', 'd0503298-e890-425a-3333333', '591902896710201', '银企直连专用账户9', '10002', '农分期', '', '', 1, '2016-09-03 10:51:02', 1001.00, 1000.00, '', 'cash_flow_no_14', '', '', null, '', 1000.00, 2, null, null, null, null, null, null, null, null, null, null, null, null),
('cash_flow_uuid_6', 0, 'company_uuid_1', 'd0503298-e890-425a-3333333', '591902896710201', '银企直连专用账户9', '10002', '农分期', '', '', 1, '2016-09-03 10:51:02', 1001.00, 1000.00, '', 'cash_flow_no_14', '', '', null, '', 1000.00, 2, null, null, null, null, null, null, null, null, null, null, null, null),
('cash_flow_uuid_7', 0, 'company_uuid_1', 'd0503298-e890-425a-3333333', '591902896710201', '银企直连专用账户9', '10002', '农分期', '', '', 1, '2016-09-03 10:51:02', 1001.00, 1000.00, '', 'cash_flow_no_14', '', '', null, '', 1000.00, 2, null, null, null, null, null, null, null, null, null, null, null, null),
('cash_flow_uuid_8', 0, 'company_uuid_1', 'd0503298-e890-425a-3333333', '591902896710201', '银企直连专用账户9', '10002', '农分期', '', '', 1, '2016-09-03 10:51:02', 1001.00, 1000.00, '', 'cash_flow_no_14', '', '', null, '', 1000.00, 2, null, null, null, null, null, null, null, null, null, null, null, null),
('cash_flow_uuid_9', 0, 'company_uuid_1', 'd0503298-e890-425a-3333333', '591902896710201', '银企直连专用账户9', '10002', '农分期', '', '', 1, '2016-09-03 10:51:02', 1001.00, 1000.00, '', 'cash_flow_no_14', '', '', null, '', 1000.00, 2, null, null, null, null, null, null, null, null, null, null, null, null),
('cash_flow_uuid_10', 0, 'company_uuid_1', 'd0503298-e890-425a-3333333', '591902896710201', '银企直连专用账户9', '10003', '农分期2', '', '', 1, '2016-09-03 10:51:02', 1001.00, 1000.00, '', 'cash_flow_no_14', '', '', null, '', 1000.00, 2, null, null, null, null, null, null, null, null, null, null, null, null),
('cash_flow_uuid_11', 0, 'company_uuid_1', 'd0503298-e890-425a-3333333', '591902896710201', '银企直连专用账户9', '10004', '农分期3', '', '', 1, '2016-09-03 10:51:02', 1001.00, 1000.00, '', 'cash_flow_no_14', '', '', null, '', 1000.00, 2, null, null, null, null, null, null, null, null, null, null, null, null);

SET FOREIGN_KEY_CHECKS=1;