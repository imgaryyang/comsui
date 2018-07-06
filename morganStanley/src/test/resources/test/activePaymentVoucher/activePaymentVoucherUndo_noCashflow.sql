SET FOREIGN_KEY_CHECKS=0;

DELETE FROM dictionary;
DELETE FROM t_merchant_config;
DELETE FROM t_api_config;
DELETE FROM t_voucher;
DELETE FROM source_document_detail;
DELETE FROM financial_contract;
DELETE FROM t_interface_active_voucher_log;

INSERT INTO `dictionary` (`id`, `code`, `content`)
VALUES
	(11, 'PLATFORM_PRI_KEY', 'MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=');


INSERT INTO `t_merchant_config` (`id`, `mer_id`, `secret`, `company_id`, `pub_key_path`, `pub_key`, `project_code`)
VALUES
  (2, 't_test_zfb', '123456', 3, NULL, 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDl1BjinRJLLDiNc6jcOKW+nph9aNSWMaKMk0OxTdSATakyS7rwNxrLMFyJLkI9IpnHussBv1zgsHPUdZeRcHDkbcMdhYoRgpe3gZIVMJ09BMBjhAET4fensvk377L0Whzp+u9r9UxIWowH7YJuJe3yQ7R3RgYxzrPuTJYq/WeuUQIDAQAB', 'spdbank');


INSERT INTO `t_api_config` (`api_url`, `fn_code`, `description`, `api_status`)
VALUES
	('/api/v3/active-payment-vouchers/undo', NULL, '主动付款凭证指令-撤销', 1);

INSERT INTO financial_contract (asset_package_format, adva_matuterm, adva_start_date, contract_no, contract_name, app_id, company_id, adva_repo_term, thru_date, capital_account_id, financial_contract_type, loan_overdue_start_day, loan_overdue_end_day, payment_channel_id, ledger_book_no, financial_contract_uuid, sys_normal_deduct_flag, sys_overdue_deduct_flag, sys_create_penalty_flag, sys_create_guarantee_flag, unusual_modify_flag, sys_create_statement_flag, transaction_limit_per_transcation, transaction_limit_per_day, remittance_strategy_mode, app_account_uuids, allow_online_repayment, allow_offline_repayment, allow_advance_deduct_flag, adva_repayment_term, penalty, overdue_default_fee, overdue_service_fee, overdue_other_fee, create_time, last_modified_time, repurchase_approach, repurchase_rule, repurchase_algorithm, day_of_month, pay_for_go, repurchase_principal_algorithm, repurchase_interest_algorithm, repurchase_penalty_algorithm, repurchase_other_charges_algorithm, temporary_repurchases, repurchase_cycle, allow_freewheeling_repayment, days_of_cycle, repayment_check_days, capital_party, other_party, contract_short_name, financial_type, remittance_object, asset_party, channel_party, supplier) VALUES
(0, 0, '2016-09-01 00:00:00', 'G31700', '拍拍贷测试', 3, 1, 4, '2017-08-31 00:00:00', 102, 0, 3, 3, 1, '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', 'd2812bc5-5057-4a91-b3fd-9019506f0499', 1, 1, 1, 1, 1, 1, null, null, 3, 'null', 1, 0, 1, 2, '', null, null, null, null, '2017-10-11 17:13:51', 1, 1, 'outstandingPrincipal+outstandingInterest+outstandingPenaltyInterest', 6, 1, 'outstandingPrincipal', 'outstandingInterest', 'outstandingInterest', 'outstandingInterest*2', '[]', null, 1, '[]', 7, null, null, null, 0, 2, null, null, null);

INSERT INTO t_voucher (uuid, voucher_no, source_document_uuid, financial_contract_uuid, amount, status, first_type, first_no, second_type, second_no, receivable_account_no, payment_account_no, payment_name, payment_bank, check_state, comment, create_time, contract_no, last_modified_time, cash_flow_uuid, transaction_time) VALUES
('cc3c6973-bb62-458f-9351-b9a733c0c492', 'V64044517222567936', null, 'd2812bc5-5057-4a91-b3fd-9019506f0499', 3240.00, 1, 'enum.voucher-source.active_payment_voucher', 'deb43fc6-b032-4bc0-a873-4e0b87c260c2', 'enum.voucher-type.active_pay', '722455d9-30e8-49a1-9976-f981958bf72f', null, '6214855712106521', '范腾', '中国建设银行', 2, null, '2017-05-22 14:51:08', null, '2017-05-22 14:51:08', 'b8798a29-3eba-11e7-ab82-525400dbb013', '2017-05-22 14:48:53');

INSERT INTO t_interface_active_voucher_log (request_no, transaction_type, voucher_type, unique_id, contract_no, repayment_plan_no, voucher_amount, receivable_account_no, payment_account_no, payment_name, payment_bank, bank_transaction_no, create_time, ip) VALUES
('e821c6b0-02ae-4f60-8873-ee2d6c47fabf', 1, 1, '1', '', 'null', 100.00, '1', '1', '1', '', '1', '2016-09-26 18:10:16', '101.231.215.146');
SET FOREIGN_KEY_CHECKS=1;