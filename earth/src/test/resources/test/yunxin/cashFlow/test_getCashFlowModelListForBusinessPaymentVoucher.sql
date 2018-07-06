SET FOREIGN_KEY_CHECKS=0;

DELETE FROM `cash_flow`;

INSERT INTO cash_flow (cash_flow_uuid, cash_flow_channel_type, company_uuid, host_account_uuid, host_account_no, host_account_name, counter_account_no, counter_account_name, counter_account_appendix, counter_bank_info, account_side, transaction_time, transaction_amount, balance, transaction_voucher_no, bank_sequence_no, remark, other_remark, strike_balance_status, trade_uuid, issued_amount, audit_status, date_field_one, date_field_two, date_field_three, long_field_one, long_field_two, long_field_three, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two, decimal_field_three) VALUES
('cash_flow_uuid_14', 0, 'company_uuid_1', 'd0503298-e890-425a-3333333', '600000000112', '测试专户开户行', '10001', 'counter_name', '', '', 0, '2016-09-06 10:51:02', 1000, 2000, '', 'cash_flow_no_14', '', '', null, '', 1000, 2, null, null, null, null, null, null, null, null, null, null, null, null),
('cash_flow_uuid_15', 0, 'company_uuid_1', 'd0503298-e890-425a-3333333', '600000000112', '测试专户开户行', '11112', 'counter_name', '', '', 1, '2016-09-03 10:51:02', 1100, 3100, '', 'cash_flow_no_15', '', '', null, '', 0, 0, null, null, null, null, null, null, null, null, null, null, null, null),
('cash_flow_uuid_16', 0, 'company_uuid_1', 'd0503298-e890-425a-3333333', '600000000112', '测试专户开户行', '10003', 'counter_name', '', '', 1, '2016-09-03 10:51:02', 1200, 4300, '', 'cash_flow_no_16', '', '', null, '', 0, 0, null, null, null, null, null, null, '1234', null, null, null, null, null),
('cash_flow_uuid_17', 0, 'company_uuid_1', 'd0503298-e890-425a-3333333', '600000000112', '测试专户开户行', '10004', 'counter_name', '', '', 1, '2016-09-06 10:51:02', 2300, 4500, '', 'cash_flow_no_17', '', '', null, '', 2300, 2, null, null, null, null, null, null, null, null, null, null, null, null);

SET FOREIGN_KEY_CHECKS=1;
