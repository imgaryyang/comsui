SET FOREIGN_KEY_CHECKS=0;

DELETE FROM dictionary;
DELETE FROM t_merchant_config;
DELETE FROM t_api_config;
DELETE FROM account;
DELETE FROM financial_contract;
DELETE FROM contract;
DELETE FROM customer;
DELETE FROM asset_set;
DELETE from ledger_book_shelf;
DELETE from t_voucher;
DELETE FROM cash_flow;
DELETE FROM source_document;
DELETE FROM t_interface_active_voucher_log;

INSERT INTO `dictionary` (`id`, `code`, `content`)
VALUES
	(11, 'PLATFORM_PRI_KEY', 'MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=');


INSERT INTO `t_merchant_config` (`id`, `mer_id`, `secret`, `company_id`, `pub_key_path`, `pub_key`, `project_code`)
VALUES
  (2, 't_test_zfb', '123456', 3, NULL, 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDl1BjinRJLLDiNc6jcOKW+nph9aNSWMaKMk0OxTdSATakyS7rwNxrLMFyJLkI9IpnHussBv1zgsHPUdZeRcHDkbcMdhYoRgpe3gZIVMJ09BMBjhAET4fensvk377L0Whzp+u9r9UxIWowH7YJuJe3yQ7R3RgYxzrPuTJYq/WeuUQIDAQAB', 'spdbank');


INSERT INTO `t_api_config` (`api_url`, `fn_code`, `description`, `api_status`)
VALUES
	('/api/v3/active-payment-vouchers/submit', NULL, '主动付款凭证指令-提交', 1);

INSERT INTO account (id, account_name, account_no, bank_name, alias, attr, scan_cash_flow_switch, usb_key_configured, uuid, bank_code, cash_flow_config) VALUES
(102, '云南信托国际有限公司', '600000000001', '平安银行深圳分行', null, '{"usbUuid":"55b9d615-62cc-4998-88a4-2bf6b4dc70af"}', false, false, 'bd7dd5b1-aa53-4faf-9647-00ddb8ab4b42', 'PAB', null);

INSERT INTO financial_contract (asset_package_format, adva_matuterm, adva_start_date, contract_no, contract_name, app_id, company_id, adva_repo_term, thru_date, capital_account_id, financial_contract_type, loan_overdue_start_day, loan_overdue_end_day, payment_channel_id, ledger_book_no, financial_contract_uuid, sys_normal_deduct_flag, sys_overdue_deduct_flag, sys_create_penalty_flag, sys_create_guarantee_flag, unusual_modify_flag, sys_create_statement_flag, transaction_limit_per_transcation, transaction_limit_per_day, remittance_strategy_mode, app_account_uuids, allow_online_repayment, allow_offline_repayment, allow_advance_deduct_flag, adva_repayment_term, penalty, overdue_default_fee, overdue_service_fee, overdue_other_fee, create_time, last_modified_time, repurchase_approach, repurchase_rule, repurchase_algorithm, day_of_month, pay_for_go, repurchase_principal_algorithm, repurchase_interest_algorithm, repurchase_penalty_algorithm, repurchase_other_charges_algorithm, temporary_repurchases, repurchase_cycle, allow_freewheeling_repayment, days_of_cycle, repayment_check_days, capital_party, other_party, contract_short_name, financial_type, remittance_object, asset_party, channel_party, supplier) VALUES
(0, 0, '2016-09-01 00:00:00', 'G31700', '拍拍贷测试', 3, 1, 4, '2017-08-31 00:00:00', 102, 0, 3, 3, 1, '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', 'd2812bc5-5057-4a91-b3fd-9019506f0499', 1, 1, 1, 1, 1, 1, null, null, 3, 'null', 1, 0, 1, 2, '', null, null, null, null, '2017-10-11 17:13:51', 1, 1, 'outstandingPrincipal+outstandingInterest+outstandingPenaltyInterest', 6, 1, 'outstandingPrincipal', 'outstandingInterest', 'outstandingInterest', 'outstandingInterest*2', '[]', null, 1, '[]', 7, null, null, null, 0, 2, null, null, null);

INSERT INTO contract (uuid, unique_id, begin_date, contract_no, end_date, asset_type, month_fee, app_id, customer_id, house_id, actual_end_date, create_time, interest_rate, payment_day_in_month, payment_frequency, periods, repayment_way, total_amount, penalty_interest, active_version_no, repayment_plan_operate_logs, state, financial_contract_uuid, interest_rate_cycle, customer_uuid) VALUES
('a46ca20c-5a2a-4e2d-89b2-39f46261df3d', '063dc97f-2c44-4fe5-ab09-2cb8020dc05a', '2016-08-01', '2016-236-DK(hk771837350822830067)号', '2019-01-01', null, 0.00, 3, 5540, 5699, null, '2016-09-07 19:22:27', 0.1100000000, 0, 0, 1, 2, 0.01, 0.2240000000, 1, null, 2, 'd2812bc5-5057-4a91-b3fd-9019506f0499', 0, '1ca62ca3-6638-4497-8d73-07342c8278e3');

INSERT INTO customer (id, account, mobile, name, source, app_id, customer_uuid, customer_type, customer_style, status, id_type) VALUES
(5540, '410402198801115658', null, '韩方园', '063dc97f-2c44-4fe5-ab09-2cb8020dc05a', 3, '1ca62ca3-6638-4497-8d73-07342c8278e3', 0, 0, 1, 0);

INSERT INTO asset_set (guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no) VALUES
(0, 0, 1040.00, 1000.00, 10.00, 1010.00, '2017-10-11', null, 0.00, 0, 1, 0, '2017-10-11 17:14:19', '032c6a1f-2c3d-4fdc-81a6-517d5cb127b4', '2017-10-11 17:14:19', '2017-10-11 17:14:19', null, 'ZC115539625962819584', 197832, null, 1, 0, null, 1, 0, 0, 'empty_deduct_uuid', null, 'd2812bc5-5057-4a91-b3fd-9019506f0499', '6fc6507746570ea3381a64b35721896e', 'eb9d6a6f00df7b43f1561d9b57037981', '2017-10-11 17:14:19', '2017-10-11 17:14:19', 0, 0, 0, 1, 0, 1, 0, '3fedee36-b5d9-4fef-8642-a660a28623d0', 'a46ca20c-5a2a-4e2d-89b2-39f46261df3d', null, '1d94b178-dc6f-4951-bcbb-17e04e250f6c', 0, '1ab8a44a445a292678b920390758c39e', 'TESTDZZ0822_270DZZ0');

INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid) VALUES
('6acc1239-2727-4224-9094-baab0b4bcc50', 490.00, 0.00, 'FST_RECIEVABLE_ASSET', '20000', 1, 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_INTEREST', '20000.01.02', 'a02c02b9-6f98-11e6-bf08-00163e002839', '1ca62ca3-6638-4497-8d73-07342c8278e3', null, '57b5faca-d6a8-4524-ae2f-997f5c131500', null, 'bb51e244-e139-4a1a-8736-f46fe660818b', '2017-09-15', '2017-09-18 11:42:04', null, null, 197592, '383101ae-812f-4135-b6a4-32d8baaf361b', '2017-09-15 00:00:00', null, '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1, 'ZC107121092128985088', '032c6a1f-2c3d-4fdc-81a6-517d5cb127b4', 'e6bcfc3a-bef7-42d7-8400-2be6de77bb6d', null, null, null, null);

INSERT INTO t_voucher (uuid, voucher_no, source_document_uuid, financial_contract_uuid, amount, status, first_type, first_no, second_type, second_no, receivable_account_no, payment_account_no, payment_name, payment_bank, check_state, comment, create_time, contract_no, last_modified_time, cash_flow_uuid, transaction_time) VALUES
('cc3c6973-bb62-458f-9351-b9a733c0c492', 'V64044517222567936', null, 'd2812bc5-5057-4a91-b3fd-9019506f0499', 3240.00, 2, 'enum.voucher-source.active_payment_voucher', 'deb43fc6-b032-4bc0-a873-4e0b87c260c2', 'enum.voucher-type.active_pay', '722455d9-30e8-49a1-9976-f981958bf72f', null, '6214855712106521', '范腾', '中国建设银行', 2, null, '2017-05-22 14:51:08', null, '2017-05-22 14:51:08', 'b8798a29-3eba-11e7-ab82-525400dbb013', '2017-05-22 14:48:53');

INSERT INTO cash_flow (cash_flow_uuid, cash_flow_channel_type, company_uuid, host_account_uuid, host_account_no, host_account_name, counter_account_no, counter_account_name, counter_account_appendix, counter_bank_info, account_side, transaction_time, transaction_amount, balance, transaction_voucher_no, bank_sequence_no, remark, other_remark, strike_balance_status, cash_flow_type, trade_uuid, issued_amount, audit_status, date_field_one, date_field_two, date_field_three, long_field_one, long_field_two, long_field_three, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two, decimal_field_three) VALUES
('930dd431-943f-11e7-b26b-525400dbb013', 0, null, 'd0503298-e890-425a-4444444', '600000000001', '云南国际信托有限公司', '6214855712117980', '高渐离', null, null, 1, '2017-09-08 10:44:02', 490.00, null, null, '930dd4a2-943f-11e7-b26b-525400dbb013', '凭证重构', null, null, 0, null, 980.00, 2, null, null, null, null, null, null, '', null, null, null, null, null);

INSERT INTO source_document (company_id, source_document_uuid, source_document_type, create_time, issued_time, source_document_status, source_account_side, booking_amount, outlier_document_uuid, outlier_trade_time, outlier_counter_party_account, outlier_counter_party_name, outlier_account, outlie_account_name, outlier_account_id, outlier_company_id, outlier_serial_global_identity, outlier_memo, outlier_amount, outlier_settlement_modes, outlier_breif, outlier_account_side, appendix, first_outlier_doc_type, second_outlier_doc_type, third_outlier_doc_type, currency_type, audit_status, first_party_id, second_party_id, virtual_account_uuid, first_account_id, second_account_id, third_account_id, excute_status, excute_result, related_contract_uuid, financial_contract_uuid, source_document_no, first_party_type, first_party_name, virtual_account_no, last_modified_time, voucher_uuid, plan_booking_amount) VALUES
(1, 'df937f9856b9436baf915b1f873129a6', 1, '2016-05-27 18:32:35', '2016-05-27 18:32:36', 1, 1, 2406.00, '930dd431-943f-11e7-b26b-525400dbb013', '2016-05-27 18:29:50', '6217000000000000000', '测试用户18', 'account_no', 'account_name', 5, 1, '2730FAE730E7B3C6', '', 2406.00, 3, '', 1, '', 'batch_pay_record', '', '', null, 2, null, null, null, 50000, 50000.01, null, 2, null, 'a46ca20c-5a2a-4e2d-89b2-39f46261df3d', 'd2812bc5-5057-4a91-b3fd-9019506f0499', null, null, null, null, '2016-05-27 18:32:36', null, 0.00);

INSERT INTO t_interface_active_voucher_log (request_no, transaction_type, voucher_type, unique_id, contract_no, repayment_plan_no, voucher_amount, receivable_account_no, payment_account_no, payment_name, payment_bank, bank_transaction_no, create_time, ip) VALUES
('e821c6b0-02ae-4f60-8873-ee2d6c47fabf', 1, 1, '1', '', 'null', 100.00, '1', '1', '1', '', '1', '2016-09-26 18:10:16', '101.231.215.146');
SET FOREIGN_KEY_CHECKS=1;