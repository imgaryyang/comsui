delete from `t_voucher`;
delete from `cash_flow`;
delete from `company`;
delete from `source_document`;



INSERT INTO `t_voucher` (`id`, `uuid`, `voucher_no`, `source_document_uuid`, `financial_contract_uuid`, `amount`, `status`, `first_type`, `first_no`, `second_type`, `second_no`, `receivable_account_no`, `payment_account_no`, `payment_name`, `payment_bank`, `check_state`, `comment`, `create_time`)
VALUES
(1, 'f6129d01-b063-11e6-bedc-00163e002839', 'CZ2743ED6CF5506163', '5dfe45e6-4ec8-4728-9e56-0c1a2123ac68', 'db36ecc9-d80c-4350-bd0d-59b1139d550d', 11000.00, 1, 'enum.voucher-source.business-payment-voucher', '4b1ac11e-9bfe-49ac-a736-29ed7db09036', 'enum.voucher-type.pay', 'bank_transaction_no_10000', '6600000000000000001', '10001', 'counter_name', 'account_account_name', 0, '测试', '2016-08-31 20:07:23'),
(2, 'f612a5ea-b063-11e6-bedc-00163e002839', 'CZ274878A1D1B20E8F', '7cf60311-c5cd-47f7-8825-c89e0df262af', '9b5da2a4-a2ac-40d3-9fe5-0f0e92045e80', 11000.00, 2, 'enum.voucher-source.business-payment-voucher', '67413fc7-c96a-41bd-950f-1f6259882b48', 'enum.voucher-type.pay', 'bank_transaction_no_10001', '600000000112', '10001', 'counter_name', 'account_account_name', 2, '', '2016-09-03 15:33:00');


INSERT INTO `cash_flow` (`id`, `cash_flow_uuid`, `cash_flow_channel_type`, `company_uuid`, `host_account_uuid`, `host_account_no`, `host_account_name`, `counter_account_no`, `counter_account_name`, `counter_account_appendix`, `counter_bank_info`, `account_side`, `transaction_time`, `transaction_amount`, `balance`, `transaction_voucher_no`, `bank_sequence_no`, `remark`, `other_remark`, `strike_balance_status`, `trade_uuid`, `issued_amount`, `audit_status`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`)
VALUES
(1, 'cash_flow_uuid_12', 0, 'company_uuid_1', 'd0503298-e890-425a-b5b4-12', '6600000000000000001', '测试专户开户行', '10001', 'counter_name', NULL, '{\"bankCode\":\"xx\",\"bankName\":\"招商银行\"}', 1, '2016-08-31 19:51:02', 11000.00, 11000.00, NULL, 'cash_flow_no_12', NULL, NULL, NULL, NULL, 11000.00, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(2, 'cash_flow_uuid_13', 0, 'a02c078d-6f98-11e6-bf08-00163e002839', 'd0503298-e890-425a-3333333', '600000000112', '测试专户开户行', '10001', 'counter_name', '', '', 1, '2016-09-03 10:51:02', 11000.00, 11000.00, '', 'cash_flow_no_13', '', '', NULL, '', 11000.00, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);


INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`)
VALUES
(1, '上海', '测试金融公司', '测试金融', 'company_uuid_1'),
(2, '南京', '测试商务公司', '测试分期', 'a02c078d-6f98-11e6-bf08-00163e002839');


INSERT INTO `source_document` (`id`, `company_id`, `source_document_uuid`, `source_document_type`, `create_time`, `issued_time`, `source_document_status`, `source_account_side`, `booking_amount`, `outlier_document_uuid`, `outlier_trade_time`, `outlier_counter_party_account`, `outlier_counter_party_name`, `outlier_account`, `outlie_account_name`, `outlier_account_id`, `outlier_company_id`, `outlier_serial_global_identity`, `outlier_memo`, `outlier_amount`, `outlier_settlement_modes`, `outlier_breif`, `outlier_account_side`, `appendix`, `first_outlier_doc_type`, `second_outlier_doc_type`, `third_outlier_doc_type`, `currency_type`, `audit_status`, `first_party_id`, `second_party_id`, `virtual_account_uuid`, `first_account_id`, `second_account_id`, `third_account_id`, `excute_status`, `excute_result`, `related_contract_uuid`, `financial_contract_uuid`, `source_document_no`, `first_party_type`, `first_party_name`, `virtual_account_no`, `last_modified_time`)
VALUES
(74, 1, '5dfe45e6-4ec8-4728-9e56-0c1a2123ac68', 1, '2016-08-30 22:52:44', NULL, 0, 1, 0.00, 'cash_flow_uuid_12', '2016-08-30 16:56:18', 'account_account_no', 'account_account_name', '6600000000000000001', NULL, NULL, 1, '352624543526', NULL, 11000.00, 3, '测试', 1, NULL, 'FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS', 'SND_LIABILITIES_INDEPENDENT_CUSTOMER_DEPOSIT', '', NULL, 0, '7d3aad51-05f1-4896-abff-caee93afca79', '1', '9126313e-f89d-4222-847c-4e36331cb787', '50000', '50000.01', '', 2, NULL, NULL, 'db36ecc9-d80c-4350-bd0d-59b1139d550d', 'CZ2743ED6CF5506163', NULL, NULL, NULL, '2016-08-30 22:52:44'),
(79, 1, '7cf60311-c5cd-47f7-8825-c89e0df262af', 1, '2016-09-03 14:48:22', NULL, 1, 1, 1001.00, 'cash_flow_uuid_13', '2016-09-03 10:51:02', '10001', 'counter_name', '600000000112', NULL, NULL, 1, 'bank_transaction_no_10001', '', 11000.00, 3, '', 1, NULL, 'FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS', 'SND_LIABILITIES_INDEPENDENT_CUSTOMER_DEPOSIT', '', NULL, 1, '7d3aad51-05f1-4896-abff-caee93afca79', '1', '9126313e-f89d-4222-847c-4e36331cb787', '50000', '50000.01', '', 2, 1, NULL, '9b5da2a4-a2ac-40d3-9fe5-0f0e92045e80', 'CZ274878A1D1B20E8F', '1', 'yqb', 'VACC27438CADB442A6A0', '2016-09-03 14:48:22');

