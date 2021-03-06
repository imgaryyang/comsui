USE galaxy_autotest_yunxin;

SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM t_api_config;
DELETE FROM dictionary;
DELETE FROM customer;
DELETE FROM financial_contract;
DELETE FROM company;
DELETE FROM ledger_book;
DELETE FROM contract;
DELETE FROM asset_package;
DELETE FROM account;
DELETE FROM asset_set;
DELETE FROM principal;
DELETE FROM rent_order;
DELETE FROM asset_valuation_detail;
DELETE FROM contract_account;
DELETE FROM transfer_application;
DELETE FROM batch_pay_record;
DELETE FROM payment_channel;
DELETE FROM cash_flow;
DELETE FROM source_document;
DELETE FROM source_document_detail;
DELETE FROM app;
DELETE FROM third_party_pay_voucher_batch;
DELETE FROM third_party_voucher_command_log;


INSERT INTO t_api_config (api_url, fn_code, description, api_status)
VALUES
  ('/api/v3/third-part-vouchers/submit', NULL, '第三方付款凭证指令', 1);

INSERT INTO dictionary (code, content)
VALUES
  ('PLATFORM_PRI_KEY',
   'MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=');
INSERT INTO customer (id, app_id)
VALUES
  (54349, 2);
INSERT INTO company (id, address, full_name, short_name, uuid) VALUES
  ('1', '上海', '云南国际信托有限公司', '云南信托', '15615156');

INSERT INTO financial_contract (adva_repayment_term, id, ledger_book_no, adva_matuterm, contract_no, app_id, company_id, adva_repo_term, capital_account_id, payment_channel_id, financial_contract_uuid)
VALUES
  ('0', '1', 'yunxin_ledger_book', 3, 'G31700', 1, 1, 30, 1, 1, 'x12323ww');

INSERT INTO asset_package (id, is_available, create_time, contract_id, asset_package_no, financial_contract_id) VALUES
  (1, 1, now(), 1, 'no', 1);

INSERT INTO account (id, account_name, account_no, alias, attr) VALUES
  (1, 'account_name_1', '955103657777777', NULL, NULL);

INSERT INTO payment_channel (id, channel_name, user_name, user_password, merchant_id, cer_file_path, pfx_file_path, pfx_file_key, payment_channel_type, api_url, from_date, thru_date)
VALUES
  (1, '广东银联', 'user1', '123456', '000191400205800', NULL, NULL, NULL, 0, NULL, NULL, NULL);


INSERT INTO asset_set (id, asset_fair_value, asset_initial_value, asset_recycle_date, asset_status, asset_uuid, create_time, last_modified_time, single_loan_contract_no, contract_id, actual_recycle_date)
VALUES
  ('1', 1010, 1000, '2015-10-19 13:34:35', 0, 'asset_uuid_1', '2015-10-19 13:34:35', '2015-10-19 13:34:35',
        'DKHD-001-01', 1, '2015-10-19 13:34:35');

INSERT INTO contract (id, begin_date, contract_no, end_date, app_id, customer_id, house_id, actual_end_date, create_time, interest_rate, payment_day_in_month, payment_frequency, periods, repayment_way, total_amount, penalty_interest, financial_contract_uuid)
VALUES
  (1, '2015-10-19', 'DKHD-001', '2016-10-19', 1, 1, 1, NULL, '2015-10-19 13:34:35', 0.001, 1, 1, 12, 0, 1000, 0.0001,
   'x12323ww');

INSERT INTO ledger_book (id, ledger_book_no, ledger_book_orgnization_id, book_master_id, party_concerned_ids) VALUES
  ('1', 'yunxin_ledger_book', '1', '1', '');

INSERT INTO rent_order (id, due_date, order_no, payout_time, total_rent, customer_id, user_upload_param, modify_time, asset_set_id, create_time, clearing_status, executing_settling_status, financial_contract_id, repayment_bill_id)
VALUES
  ('1', '2015-03-18', 'DKHD-001-01-20160308', '2016-10-19 13:34:35', 10, '1', '', '2016-10-19', 1, '2015-10-19', 0, 0,
   1, 'repayment_bill_id_1'),
  ('2', '2015-03-18', 'DKHD-001-02-20160308', '2016-10-19 13:34:35', 10, '1', '', '2016-10-19', 1, '2015-10-19', 0, 0,
   1, 'repayment_bill_id_2');

INSERT INTO asset_valuation_detail (id, amount, asset_value_date, created_date, subject, asset_set_id) VALUES
  ('1', '1000.00', '2016-03-14 19:46:38', '2016-03-14', '0', '1'),
  ('2', '10.00', '2016-03-15 19:47:16', '2016-03-15', '1', '1'),
  ('3', '1000.00', '2016-03-14 19:46:38', '2016-03-14', '0', '2');

INSERT INTO contract_account (id, pay_ac_no, bankcard_type, contract_id, payer_name) VALUES
  (1, 'pay_ac_no_1', 0, 1, 'payer_name_1'),
  (2, 'pay_ac_no_2', 0, 1, 'payer_name_1');

INSERT INTO transfer_application (id, amount, creator_id, batch_pay_record_id, comment, create_time, deduct_status, executing_deduct_status, deduct_time, last_modified_time, contract_account_id, order_id)
VALUES
  (1, '10', NULL, 1, NULL, '2015-10-10', 0, 1, NULL, NULL, 1, 1),
  (2, '10', NULL, 2, NULL, '2015-10-13', 1, 2, NULL, NULL, 1, 2);

INSERT INTO batch_pay_record (id, batch_pay_record_uuid, amount, modify_time, request_no, serial_no, request_data, response_data, query_response_data, trans_date_time)
VALUES
  ('1', 'batch_pay_record_uuid_1', '10.00', NULL, 'test', 'serial_no_1', NULL, '', '', '20160323111100'),
  ('2', 'batch_pay_record_uuid_2', '10.00', NULL, 'test', 'serial_no_2', NULL, '', '', '20160111111100');

-- test_businessPaymentVoucherHaveDocumentNoDetail
INSERT INTO cash_flow (id, cash_flow_uuid, cash_flow_channel_type, company_uuid, host_account_uuid, host_account_no, host_account_name, counter_account_no, counter_account_name, counter_account_appendix, counter_bank_info, account_side, transaction_time, transaction_amount, balance, transaction_voucher_no, bank_sequence_no, remark, other_remark, strike_balance_status, trade_uuid, issued_amount, audit_status, date_field_one, date_field_two, date_field_three, long_field_one, long_field_two, long_field_three, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two, decimal_field_three)
VALUES
  ('1', 'batch_pay_record_uuid_1', '0', NULL, NULL, '11014807398006', '杭州随地付网络技术有限公司', 'serial_no', '张建明', NULL,
        '{\"bankCode\":\"\",\"bankName\":\"中国民生银行\"}', '0', '2016-08-16 15:21:26', '0.10', '22.81', NULL, '1122049',
                                                       '0816 平安银企直连测试', '实时转账', NULL, NULL, NULL, '0', NULL, NULL, NULL,
                                                                                            NULL, NULL, NULL, NULL,
                                                                                            NULL, NULL, NULL, NULL,
   NULL);

INSERT INTO source_document (id, company_id, source_document_uuid, source_document_type, create_time, issued_time, source_document_status, source_account_side, booking_amount, outlier_document_uuid, outlier_trade_time, outlier_counter_party_account, outlier_counter_party_name, outlier_account, outlie_account_name, outlier_account_id, outlier_company_id, outlier_serial_global_identity, outlier_memo, outlier_amount, outlier_settlement_modes, outlier_breif, outlier_account_side, related_contract_uuid)
VALUES
  ('1', '1', 'source_document_uuid_1', '1', '2016-03-29 17:21:09', '2015-04-03 12:00:00', '0', '1', '0.00',
        'batch_pay_record_uuid_1', '2015-03-30 16:11:50', 'pay_ac_no_1', 'payer_name_1', '1001300219000027827', NULL,
                                                          NULL, '1', 'serial_no', '汇川路88号1号楼1803?', '10.00', '3', '',
   '0', 'xxx');

-- test_businessPaymentVoucherHave2CashFlow
INSERT INTO cash_flow (id, cash_flow_uuid, cash_flow_channel_type, company_uuid, host_account_uuid, host_account_no, host_account_name, counter_account_no, counter_account_name, counter_account_appendix, counter_bank_info, account_side, transaction_time, transaction_amount, balance, transaction_voucher_no, bank_sequence_no, remark, other_remark, strike_balance_status, trade_uuid, issued_amount, audit_status, date_field_one, date_field_two, date_field_three, long_field_one, long_field_two, long_field_three, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two, decimal_field_three)
VALUES
  ('3', 'batch_pay_record_uuid_3', '0', NULL, NULL, '11014807398006', '杭州随地付网络技术有限公司', 'serial_no_3', '张建明', NULL,
        '{\"bankCode\":\"\",\"bankName\":\"中国民生银行\"}', '0', '2016-08-16 15:21:26', '0.10', '22.81', NULL, '1122049',
                                                       '0816 平安银企直连测试', '实时转账', NULL, NULL, NULL, '0', NULL, NULL, NULL,
                                                                                            NULL, NULL, NULL, NULL,
                                                                                            NULL, NULL, NULL, NULL,
   NULL),
  ('4', 'batch_pay_record_uuid_4', '0', NULL, NULL, '11014807398006', '杭州随地付网络技术有限公司', 'serial_no_3', '张建明', NULL,
        '{\"bankCode\":\"\",\"bankName\":\"中国民生银行\"}', '0', '2016-08-16 15:21:26', '0.10', '22.81', NULL, '1122049',
                                                       '0816 平安银企直连测试', '实时转账', NULL, NULL, NULL, '0', NULL, NULL, NULL,
                                                                                            NULL, NULL, NULL, NULL,
                                                                                            NULL, NULL, NULL, NULL,
   NULL);
INSERT INTO source_document (id, company_id, source_document_uuid, source_document_type, create_time, issued_time, source_document_status, source_account_side, booking_amount, outlier_document_uuid, outlier_trade_time, outlier_counter_party_account, outlier_counter_party_name, outlier_account, outlie_account_name, outlier_account_id, outlier_company_id, outlier_serial_global_identity, outlier_memo, outlier_amount, outlier_settlement_modes, outlier_breif, outlier_account_side)
VALUES
  ('3', '1', 'source_document_uuid_3', '0', '2016-03-29 17:21:09', '2015-04-03 12:00:00', '1', '1', '10.00',
        'batch_pay_record_uuid_3', '2015-04-01 14:48:52', 'pay_ac_no_1', 'payer_name_1', '1001300219000027827', NULL,
                                                          NULL, '4', 'serial_no_3', '1804 静安国际广场3月', '10.00', '3', '',
   '0'),
  ('4', '2', 'source_document_uuid_3', '0', '2016-03-29 17:21:09', '2015-04-03 12:00:00', '1', '1', '10.00',
        'batch_pay_record_uuid_4', '2015-04-01 14:48:52', 'pay_ac_no_1', 'payer_name_1', '1001300219000027827', NULL,
                                                          NULL, '4', 'serial_no_4', '1804 静安国际广场3月', '10.00', '3', '',
   '0');

-- test_businessPaymentVoucherNoDoc
INSERT INTO cash_flow (id, cash_flow_uuid, cash_flow_channel_type, company_uuid, host_account_uuid, host_account_no, host_account_name, counter_account_no, counter_account_name, counter_account_appendix, counter_bank_info, account_side, transaction_time, transaction_amount, balance, transaction_voucher_no, bank_sequence_no, remark, other_remark, strike_balance_status, trade_uuid, issued_amount, audit_status, date_field_one, date_field_two, date_field_three, long_field_one, long_field_two, long_field_three, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two, decimal_field_three)
VALUES
  ('2', 'batch_pay_record_uuid_2', '0', NULL, NULL, '11014807398006', '杭州随地付网络技术有限公司', 'serial_no_2', '张建明', NULL,
        '{\"bankCode\":\"\",\"bankName\":\"中国民生银行\"}', '0', '2016-08-16 15:21:26', '0.10', '22.81', NULL, '1122049',
                                                       '0816 平安银企直连测试', '实时转账', NULL, NULL, NULL, '0', NULL, NULL, NULL,
                                                                                            NULL, NULL, NULL, NULL,
                                                                                            NULL, NULL, NULL, NULL,
   NULL),
  ('5', 'batch_pay_record_uuid_4', '0', NULL, NULL, '11014807398006', '杭州随地付网络技术有限公司', 'serial_no_5', '张建明', NULL,
        '{\"bankCode\":\"\",\"bankName\":\"中国民生银行\"}', '0', '2016-08-16 15:21:26', '0.10', '22.81', NULL, '1122049',
                                                       '0816 平安银企直连测试', '实时转账', NULL, NULL, NULL, '0', NULL, NULL, NULL,
                                                                                            NULL, NULL, NULL, NULL,
                                                                                            NULL, NULL, NULL, NULL,
   NULL);


INSERT INTO source_document (id, company_id, source_document_uuid, source_document_type, create_time, issued_time, source_document_status, source_account_side, booking_amount, outlier_document_uuid, outlier_trade_time, outlier_counter_party_account, outlier_counter_party_name, outlier_account, outlie_account_name, outlier_account_id, outlier_company_id, outlier_serial_global_identity, outlier_memo, outlier_amount, outlier_settlement_modes, outlier_breif, outlier_account_side)
VALUES
  ('2', '1', 'source_document_uuid_2', '1', '2016-03-29 17:21:09', '2015-04-03 12:00:00', '1', '1', '10.00',
        'batch_pay_record_uuid_2', '2015-04-02 09:40:07', 'pay_ac_no_1', 'payer_name_1', '1001300219000027827', NULL,
                                                          NULL, '1', 'serial_no_2', 'MONTHLY RENTAL FOR 8', '10.00',
                                                          '3', '', '0'),
  ('5', '2', 'source_document_uuid_4', '0', '2016-03-29 17:21:09', '2015-04-03 12:00:00', '1', '1', '10.00',
        'batch_pay_record_uuid_4', '2015-04-01 14:48:52', 'pay_ac_no_1', 'payer_name_1', '1001300219000027827', NULL,
                                                          NULL, '4', 'serial_no_5', '1804 静安国际广场3月', '10.00', '3', '',
   '0');

INSERT INTO source_document_detail (id, uuid, source_document_uuid, contract_unique_id, repayment_plan_no, amount, status, first_type, first_no, second_type, second_no, payer, receivable_account_no, payment_account_no, payment_name, payment_bank, check_state, comment)
VALUES
  ('1', 'uuid_1111', 'source_document_uuid_0', 'contract_unique_id', 'plan_no1', '500.00', NULL, 'fist_type',
        'first_no', '11', 'serial_no_1', NULL, '5615156s', '16151551', '王二', '工商银行', '0', 'comment'),
  ('2', 'uuid_1112', 'source_document_uuid_2', 'contract_unique_id', 'plan_no2', '500.00', '2', 'fist_type', 'first_no',
        '11', 'serial_no_2', NULL, '5615156s', '16151551', '李四', '农业银行', '0', 'comment'),
  ('3', 'uuid_1113', 'source_document_uuid_2', 'contract_unique_id', 'plan_no3', '500.00', '1', 'fist_type', 'first_no',
        '11', 'serial_no_2', NULL, '5615156s', '16151551', '王二', '招商银行', '0', 'comment'),
  ('4', 'uuid_1114', 'source_document_uuid_3', 'contract_unique_id', 'plan_no4', '500.00', NULL, 'fist_type',
        'first_no', '11', 'serial_no_4', NULL, '5615156s', '16151551', '周五', '招商银行', '0', 'comment'),
  ('5', 'uuid_1115', 'source_document_uuid_3', 'contract_unique_id', 'plan_no5', '500.00', NULL, 'fist_type',
        'first_no', '11', 'serial_no_5', NULL, '5615156s', '16151551', '周五', '招商银行', '0', 'comment');

INSERT INTO third_party_voucher_command_log (id, voucher_uuid, voucher_no, request_no, batch_uuid, trade_uuid, bank_transaction_no, financial_contract_uuid, financial_contract_no, contract_unique_id, transcation_amount, transaction_time, transcation_complete_time, transcation_gateway, repayment_no_json_list, create_time, execution_status, voucher_log_status, voucher_log_issue_status, receivable_account_no, payment_account_no, payment_name, third_part_voucher_content, voucher_source, ip, error_message, comment, status_modify_time, error_code_list, batch_no, version_no, process_status, retried_times, retried_time, outer_repayment_plan_no_list, current_period_list, repay_schedule_no_list)
VALUES
  (2, '739831ab-dfb8-43c1-a777-813098280446', '827aee18-049e-4a60-adc2-843bb41f7f7f', '835e978d-7b18-4196-9941-9e18c7e85abb', '434f0d2a-c283-45bc-9a2e-619d71b990cf', 'dzzz', '4145a701-0fdd-4290-a27b-58f0aa15e1fe', 'd2812bc5-5057-4a91-b3fd-9019506f0499', 'G31700', '妹妹你大胆的往前走45', 980.00, '2017-07-12 17:23:00', '2017-07-12 17:23:00', 7, '["ZC82550869837361152"]', '2017-07-12 17:22:10', 2, 6, 0, '12345tyui', '23r4t5y6i8o9', 'qwertyu', '{"amount":980,"bankTransactionNo":"4145a701-0fdd-4290-a27b-58f0aa15e1fe","comment":"易极付测试","completeTime":"2017-07-12 17:23:00","contractUniqueId":"妹妹你大胆的往前走45","createTime":"2017-07-12 17:23:00","currency":0,"customerIdNo":"1234567890-","customerName":"dzz","paymentAccountNo":"23r4t5y6i8o9","paymentBank":"23erdzz10","paymentName":"qwertyu","receivableAccountNo":"12345tyui","repayDetailList":[{"amount":900,"interest":20,"lateFee":0,"lateOtherCost":0,"latePenalty":0,"maintenanceCharge":20,"otherCharge":20,"penaltyFee":0,"principal":900,"repaymentPlanNo":"ZC82550869837361152","serviceCharge":20}],"transactionGateway":7,"transactionRequestNo":"dzzz","transactionTime":"2017-07-12 17:23:00","voucherNo":"827aee18-049e-4a60-adc2-843bb41f7f7f","versionNo":"3809b9e0-c3b2-447b-943c-659c75ff0e75"}', 0, '192.168.0.200', '该还款计划状态不允许(核销锁定中或已结清)', '易极付测试', '2017-07-17 15:00:31', 'null', NULL, '3809b9e0-c3b2-447b-943c-659c75ff0e75', 1, 6, '2017-07-17 15:00:20', NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;