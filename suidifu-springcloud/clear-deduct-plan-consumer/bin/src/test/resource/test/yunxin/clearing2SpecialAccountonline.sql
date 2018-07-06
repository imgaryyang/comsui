SET FOREIGN_KEY_CHECKS=0;

TRUNCATE TABLE `clearing_voucher`;
TRUNCATE TABLE `t_deduct_plan`;
TRUNCATE TABLE `clearing_exec_log`;
TRUNCATE TABLE `t_deduct_application`;
TRUNCATE TABLE `financial_contract`;
TRUNCATE TABLE `ledger_book`;
TRUNCATE TABLE `ledger_book_shelf`;
TRUNCATE TABLE `special_account`;
TRUNCATE TABLE `company`;
TRUNCATE TABLE `asset_set`;
TRUNCATE TABLE `journal_voucher`;


INSERT INTO `clearing_voucher` (`id`, `uuid`, `voucher_no`, `batch_uuid`, `source_account_side`, `payment_institution`, `merchant_no`, `pg_clearing_account`, `host_account_no`, `host_account_name`, `counter_account_no`, `counter_account_name`, `bank_info`, `clearing_voucher_status`, `create_time`, `last_modified_time`, `complete_time`, `cash_flow_time`, `cash_flow_uuid`, `audit_job_uuid`, `total_receivable_bills_uuid`, `voucher_amount`, `single_signs`, `appendix`, `remark`) 
VALUES 
('29', 'b59a141a-3b52-4fd9-a588-f23773c7446a', 'V138072515572424704', '2ce6ebdb-4255-45c8-b713-ca3b2a15bb61', '0', '3', '001053110000001', '', '95200155300001595', '浦发2000040752', '95130154900000571', '浦发2000040752', '浦发杭州高新支行', '0', '2017-12-12 21:31:59', '2017-12-12 21:31:59', NULL, '2017-09-07 10:31:19', '635e5285-e3c8-4df2-826b-b58dd2f201b5', 'b2e136c7-f595-4861-86af-f95fcf888d70', 'ed055704-25c1-46fe-ab56-d733d9af0bb0', '40000.00', '1', '{\"audit_job\":\"[\\\"b2e136c7-f595-4861-86af-f95fcf888d70\\\",\\\"b8c4033f-28f5-47eb-b1ab-d5315e2ec0cf\\\",\\\"2d056efa-1359-4dd0-a5fb-006f9c0802cc\\\"]\",\"cash_flow\":\"[\\\"635e5285-e3c8-4df2-826b-b58dd2f201b5\\\"]\",\"total_receivable_bills\":\"[\\\"ed055704-25c1-46fe-ab56-d733d9af0bb0\\\",\\\"1eebd205-025a-4de7-91c0-bf9edc119b87\\\",\\\"c54e63c9-cff2-4cd2-b394-f05c916e88e9\\\"]\"}', '备注1'),
('39', 'b59a141a-3b52-4fd9-a588-f23773c7446b', 'V138072515572424705', '2ce6ebdb-4255-45c8-b713-ca3b2a15bb62', '1', '3', '001053110000001', '001', '95200155300001595', '浦发2000040752', '95130154900000571', '浦发2000040752', '浦发杭州高新支行', '0', '2017-12-12 21:31:59', '2017-12-12 21:31:59', NULL, '2017-09-07 11:31:19', '635e5285-e3c8-4df2-826b-b58dd2f201b5', 'b2e136c7-f595-4861-86af-f95fcf888d70', 'ed055704-25c1-46fe-ab56-d733d9af0bb0', '40000.00', '1', '{\"audit_job\":\"[\\\"b2e136c7-f595-4861-86af-f95fcf888d70\\\",\\\"b8c4033f-28f5-47eb-b1ab-d5315e2ec0cf\\\",\\\"2d056efa-1359-4dd0-a5fb-006f9c0802cc\\\"]\",\"cash_flow\":\"[\\\"635e5285-e3c8-4df2-826b-b58dd2f201b5\\\"]\",\"total_receivable_bills\":\"[\\\"ed055704-25c1-46fe-ab56-d733d9af0bb0\\\",\\\"1eebd205-025a-4de7-91c0-bf9edc119b87\\\",\\\"c54e63c9-cff2-4cd2-b394-f05c916e88e9\\\"]\"}', '备注2'),
('49', 'b59a141a-3b52-4fd9-a588-f23773c7446c', 'V138072515572424706', '2ce6ebdb-4255-45c8-b713-ca3b2a15bb63', '0', '3', '001053110000001', '002', '95200155300001595', '浦发2000040752', '95130154900000571', '浦发2000040752', '浦发杭州高新支行', '0', '2017-12-12 21:31:59', '2017-12-12 21:31:59', NULL, '2017-09-07 09:31:19', '635e5285-e3c8-4df2-826b-b58dd2f201b5', 'b2e136c7-f595-4861-86af-f95fcf888d70', 'ed055704-25c1-46fe-ab56-d733d9af0bb0', '40000.00', '1', '{\"audit_job\":\"[\\\"b2e136c7-f595-4861-86af-f95fcf888d70\\\",\\\"b8c4033f-28f5-47eb-b1ab-d5315e2ec0cf\\\",\\\"2d056efa-1359-4dd0-a5fb-006f9c0802cc\\\"]\",\"cash_flow\":\"[\\\"635e5285-e3c8-4df2-826b-b58dd2f201b5\\\"]\",\"total_receivable_bills\":\"[\\\"ed055704-25c1-46fe-ab56-d733d9af0bb0\\\",\\\"1eebd205-025a-4de7-91c0-bf9edc119b87\\\",\\\"c54e63c9-cff2-4cd2-b394-f05c916e88e9\\\"]\"}', '备注3'),
('59', 'b59a141a-3b52-4fd9-a588-f23773c7446d', 'V138072515572424707', '2ce6ebdb-4255-45c8-b713-ca3b2a15bb64', '0', '1', '001053110000001', '', '95200155300001595', '浦发2000040752', '95130154900000571', '浦发2000040752', '浦发杭州高新支行', '0', '2017-12-12 21:31:59', '2017-12-12 21:31:59', NULL, '2017-09-07 08:31:19', '635e5285-e3c8-4df2-826b-b58dd2f201b5', 'b2e136c7-f595-4861-86af-f95fcf888d70', 'ed055704-25c1-46fe-ab56-d733d9af0bb0', '40000.00', '1', '{\"audit_job\":\"[\\\"b2e136c7-f595-4861-86af-f95fcf888d70\\\",\\\"b8c4033f-28f5-47eb-b1ab-d5315e2ec0cf\\\",\\\"2d056efa-1359-4dd0-a5fb-006f9c0802cc\\\"]\",\"cash_flow\":\"[\\\"635e5285-e3c8-4df2-826b-b58dd2f201b5\\\"]\",\"total_receivable_bills\":\"[\\\"ed055704-25c1-46fe-ab56-d733d9af0bb0\\\",\\\"1eebd205-025a-4de7-91c0-bf9edc119b87\\\",\\\"c54e63c9-cff2-4cd2-b394-f05c916e88e9\\\"]\"}', '备注4');


INSERT INTO `t_deduct_application` (`id`, `deduct_application_uuid`, `deduct_id`, `request_no`, `financial_contract_uuid`, `financial_product_code`, `contract_unique_id`, `repayment_plan_code_list`, `contract_no`, `planned_deduct_total_amount`, `actual_deduct_total_amount`, `notify_url`, `transcation_type`, `repayment_type`, `execution_status`, `execution_remark`, `create_time`, `creator_name`, `ip`, `last_modified_time`, `record_status`, `is_available`, `api_called_time`, `transaction_recipient`, `customer_name`, `mobile`, `gateway`, `source_type`, `third_part_voucher_status`, `complete_time`, `transaction_time`, `actual_notify_number`, `plan_notify_number`, `batch_deduct_id`, `none_business_check_status`, `batch_deduct_application_uuid`, `retry_times`, `payment_order_uuid`, `retriable`, `notify_status`, `business_check_status`, `version`, `total_count`, `executed_count`, `receive_status`, `request_params`, `check_response_no`, `plan_notify_citigroup_number`, `actual_notify_citigroup_number`, `date_field_one`, `date_field_two`, `date_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_One`, `decimal_field_two`, `decimal_field_three`, `int_field_one`, `int_field_two`, `int_field_three`) 
VALUES 
('54', '5f47ec47-8475-4015-8df1-1aec1da0d4bc', '966aa703-d68d-4e1a-b0fe-6533c04f6034', '9d10fa42-8db8-4227-8b92-4623610f47ba', '2495a5ce-094e-4eb6-9fb9-95454b138427', 'WB123', '8dcb5a4b-df1c-4b43-b50a-57b730b8ac68', '[\"ZC132635543776206848\"]', '8dcb5a4b-df1c-4b43-b50a-57b730b8ac68', '100.00', '0.00', 'http://192.168.0.212:7778/api/notify/internal/deduct', '1', '0', '3', 'null:［TZ1001044］［绑卡受限］ ', '2017-11-27 21:27:24', 't_test_zfb', '192.168.0.200', '2017-11-27 22:49:03', '0', '0', '2017-11-27 00:00:00', '1', NULL, '17682481094', NULL, '1', '0', '2017-11-27 22:49:02', '2017-11-27 21:27:24', '1', '1', NULL, '0', NULL, NULL, NULL, '0', '0', '0', '6f444882-87a4-4691-af99-9884a4a4e3f7', '1', '1', '2', NULL, '4bde23a4-01d0-4bdd-9ffd-43ed9fdb8361', '3', '3', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', '0');

INSERT INTO `clearing_exec_log` (`id`, `uuid`, `repayment_business_uuid`, `contract_uuid`, `financial_contract_uuid`, `clearing_status`, `create_time`, `clearing_complete_time`, `last_modified_time`, `amount`, `detail_amount`, `batch_uuid`, `journal_voucher_type`, `journal_voucher_uuid`, `principal`, `interest`, `service_charge`, `maintenance_charge`, `other_charge`, `penalty_fee`, `late_penalty`, `late_fee`, `late_other_cost`, `clearing_voucher_uuid`) 
VALUES 
('1', 'b59a141a-3b52-4fd9-a588-f23773c74001', '111111', '1111111111', 'financial_contract_uuid_1', '0', '2017-12-22 15:18:49', NULL, '2017-12-22 15:18:55', '10.00',  '{"principal":"10.00"}', NULL, '7', '111111111', '10.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '1111');


INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`,`adva_repayment_term`)
VALUES
	(1, 0, 3, '2016-09-01 00:00:00', 'G32000', '用钱宝测试', 2, 1, 60, '2017-12-01 00:00:00', 101, 0, 1, 2, 1, 'e17d8068-46a0-48bb-b158-8e80168bbfc3', 'financial_contract_uuid_1', 0, 0, 0, 0, 1, 0,7);
INSERT INTO `ledger_book` (`id`, `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`)
VALUES
	(1, 'e17d8068-46a0-48bb-b158-8e80168bbfc3', '1', NULL, NULL);
	
INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`)
VALUES
	(1, '上海', '测试金融公司', '测试金融', 'a02c02b9-6f98-11e6-bf08-00163e002839'),
	(4, '杭州', 'yqb', 'yqb', 'a02c08b5-6f98-11e6-bf08-00163e002839');
	
INSERT INTO `special_account` (`id`, `uuid`, `balance`, `basic_account_type`, `account_type_code`, `account_name`, `level`, `parent_account_uuid`, `fst_level_contract_uuid`, `snd_level_contract_uuid`, `trd_level_contract_uuid`, `create_time`, `last_update_time`, `version`) 
VALUES 
('1', '142544', '0.00', '1', '60000.1000.30', '暂存金额', '2', '1957455', 'financial_contract_uuid_1', NULL, NULL, '2017-12-25 11:13:36', NULL, 'verion-8988'),
('2', '154521', '0.00', '4', '40000.04', '还款本金', '2', '121545154', 'financial_contract_uuid_1', NULL, NULL, '2017-12-25 11:13:39', NULL, 'verion-8987'),
('3', '1957455', '0.00', '1', '60000.1000', '暂存户', '1', NULL, 'financial_contract_uuid_1', NULL, NULL, '2017-12-25 11:13:42', NULL, 'verion-8981'),
('4', '121545154', '0.00', '4', '70000', '还款户', '1', NULL, 'financial_contract_uuid_1', NULL, NULL, '2017-12-25 11:13:45', NULL, 'verion-8982');

INSERT INTO `source_document` (`id`, `company_id`, `source_document_uuid`, `source_document_type`, `create_time`, `issued_time`, `source_document_status`, `source_account_side`, `booking_amount`, `outlier_document_uuid`, `outlier_trade_time`, `outlier_counter_party_account`, `outlier_counter_party_name`, `outlier_account`, `outlie_account_name`, `outlier_account_id`, `outlier_company_id`, `outlier_serial_global_identity`, `outlier_memo`, `outlier_amount`, `outlier_settlement_modes`, `outlier_breif`, `outlier_account_side`, `appendix`, `first_outlier_doc_type`, `second_outlier_doc_type`, `third_outlier_doc_type`, `currency_type`, `audit_status`, `first_party_id`, `second_party_id`, `virtual_account_uuid`, `first_account_id`, `second_account_id`, `third_account_id`, `excute_status`, `excute_result`, `related_contract_uuid`, `financial_contract_uuid`, `source_document_no`, `first_party_type`, `first_party_name`, `virtual_account_no`, `last_modified_time`, `voucher_uuid`, `plan_booking_amount`) 
VALUES 
('79', '1', '7cf60311-c5cd-47f7-8825-c89e0df262af', '1', '2016-09-03 14:48:22', NULL, '2', '1', '1001.00', 'cash_flow_uuid_13', '2016-09-03 10:51:02', '10001', 'counter_name', '600000000112', NULL, NULL, '1', 'bank_transaction_no_10001', '', '11000.00', '3', '', '1', NULL, 'FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS', 'SND_LIABILITIES_INDEPENDENT_CUSTOMER_DEPOSIT', '', NULL, '1', '7d3aad51-05f1-4896-abff-caee93afca79', '1', '9126313e-f89d-4222-847c-4e36331cb787', '50000', '50000.01', '', '2', '1', NULL, '9b5da2a4-a2ac-40d3-9fe5-0f0e92045e80', 'CZ274878A1D1B20E8F', '1', 'yqb', 'VACC27438CADB442A6A0', '2017-04-23 17:59:02', 'f612a5ea-b063-11e6-bedc-00163e002839', '11000.00');

INSERT INTO `source_document_detail` (`id`, `uuid`, `source_document_uuid`, `contract_unique_id`, `repayment_plan_no`, `amount`, `status`, `first_type`, `first_no`, `second_type`, `second_no`, `payer`, `receivable_account_no`, `payment_account_no`, `payment_name`, `payment_bank`, `check_state`, `comment`, `financial_contract_uuid`, `principal`, `interest`, `service_charge`, `maintenance_charge`, `other_charge`, `penalty_fee`, `late_penalty`, `late_fee`, `late_other_cost`, `voucher_uuid`, `actual_payment_time`, `repay_schedule_no`, `current_period`, `outer_repayment_plan_no`) 
VALUES 
('10004', 'f5abc9fc-4192-4cfa-a3f2-99509ef50b71', '7cf60311-c5cd-47f7-8825-c89e0df262af', '9db8ce1f-34d2-4601-b246-8d015d92308c', 'ZC2748790EAB0CED9A', '1100.00', '1', 'enum.voucher-source.business-payment-voucher', '67413fc7-c96a-41bd-950f-1f6259882b48', 'enum.voucher-type.pay', 'bank_transaction_no_10001', '0', '600000000112', '10001', 'counter_name', 'account_account_name', '2', NULL, '9b5da2a4-a2ac-40d3-9fe5-0f0e92045e80', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', 'f612a5ea-b063-11e6-bedc-00163e002839', NULL, NULL, NULL, NULL),
('10005', 'ab9ddcbf-12a3-4819-bc92-0d926b2e5b67', '7cf60311-c5cd-47f7-8825-c89e0df262af', '9db8ce1f-34d2-4601-b246-8d015d92308c', 'ZC2748790EAB0CED9A', '1100.00', '1', 'enum.voucher-source.business-payment-voucher', '67413fc7-c96a-41bd-950f-1f6259882b48', 'enum.voucher-type.pay', 'bank_transaction_no_10001', '0', '600000000112', '10001', 'counter_name', 'account_account_name', '2', NULL, '9b5da2a4-a2ac-40d3-9fe5-0f0e92045e80', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', 'f612a5ea-b063-11e6-bedc-00163e002839', NULL, NULL, NULL, NULL),
('10006', '6f49395c-1a61-41d1-860e-c8280edb2924', '7cf60311-c5cd-47f7-8825-c89e0df262af', '9db8ce1f-34d2-4601-b246-8d015d92308c', 'ZC2748790EAB0CED9A', '1100.00', '1', 'enum.voucher-source.business-payment-voucher', '67413fc7-c96a-41bd-950f-1f6259882b48', 'enum.voucher-type.pay', 'bank_transaction_no_10001', '0', '600000000112', '10001', 'counter_name', 'account_account_name', '2', NULL, '9b5da2a4-a2ac-40d3-9fe5-0f0e92045e80', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', 'f612a5ea-b063-11e6-bedc-00163e002839', NULL, NULL, NULL, NULL),
('10007', '387f54c2-e332-4cdb-b247-a3a622e878be', '7cf60311-c5cd-47f7-8825-c89e0df262af', '9db8ce1f-34d2-4601-b246-8d015d92308c', 'ZC2748790EAB0CED9A', '1100.00', '1', 'enum.voucher-source.business-payment-voucher', '67413fc7-c96a-41bd-950f-1f6259882b48', 'enum.voucher-type.pay', 'bank_transaction_no_10001', '0', '600000000112', '10001', 'counter_name', 'account_account_name', '2', NULL, '9b5da2a4-a2ac-40d3-9fe5-0f0e92045e80', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', 'f612a5ea-b063-11e6-bedc-00163e002839', NULL, NULL, NULL, NULL),
('10008', '7f3c11ce-5e4c-4f05-a14b-92ac9148ebb0', '7cf60311-c5cd-47f7-8825-c89e0df262af', '9db8ce1f-34d2-4601-b246-8d015d92308c', 'ZC2748790EAB0CED9A', '1100.00', '1', 'enum.voucher-source.business-payment-voucher', '67413fc7-c96a-41bd-950f-1f6259882b48', 'enum.voucher-type.pay', 'bank_transaction_no_10001', '0', '600000000112', '10001', 'counter_name', 'account_account_name', '2', NULL, '9b5da2a4-a2ac-40d3-9fe5-0f0e92045e80', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', 'f612a5ea-b063-11e6-bedc-00163e002839', NULL, NULL, NULL, NULL),
('10009', '0bc5e394-8c1a-4367-8c7b-d35568d89079', '7cf60311-c5cd-47f7-8825-c89e0df262af', '9db8ce1f-34d2-4601-b246-8d015d92308c', 'ZC2748790EAB0CED9A', '1100.00', '1', 'enum.voucher-source.business-payment-voucher', '67413fc7-c96a-41bd-950f-1f6259882b48', 'enum.voucher-type.pay', 'bank_transaction_no_10001', '0', '600000000112', '10001', 'counter_name', 'account_account_name', '2', NULL, '9b5da2a4-a2ac-40d3-9fe5-0f0e92045e80', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', 'f612a5ea-b063-11e6-bedc-00163e002839', NULL, NULL, NULL, NULL),
('10010', '56a21b20-9056-4e13-a1a1-d086f0256e8f', '7cf60311-c5cd-47f7-8825-c89e0df262af', '9db8ce1f-34d2-4601-b246-8d015d92308c', 'ZC2748790EAB0CED9A', '1100.00', '1', 'enum.voucher-source.business-payment-voucher', '67413fc7-c96a-41bd-950f-1f6259882b48', 'enum.voucher-type.pay', 'bank_transaction_no_10001', '0', '600000000112', '10001', 'counter_name', 'account_account_name', '2', NULL, '9b5da2a4-a2ac-40d3-9fe5-0f0e92045e80', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', 'f612a5ea-b063-11e6-bedc-00163e002839', NULL, NULL, NULL, NULL),
('10011', 'f39eaea3-3ad6-4cbb-85ef-89c6131fe41a', '7cf60311-c5cd-47f7-8825-c89e0df262af', '9db8ce1f-34d2-4601-b246-8d015d92308c', 'ZC2748790EAB0CED9A', '1100.00', '1', 'enum.voucher-source.business-payment-voucher', '67413fc7-c96a-41bd-950f-1f6259882b48', 'enum.voucher-type.pay', 'bank_transaction_no_10001', '0', '600000000112', '10001', 'counter_name', 'account_account_name', '2', NULL, '9b5da2a4-a2ac-40d3-9fe5-0f0e92045e80', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', 'f612a5ea-b063-11e6-bedc-00163e002839', NULL, NULL, NULL, NULL),
('10012', 'f3796158-a22f-4c87-b03c-a318b4c76a16', '7cf60311-c5cd-47f7-8825-c89e0df262af', '9db8ce1f-34d2-4601-b246-8d015d92308c', 'ZC2748790EAB0CED9A', '1100.00', '1', 'enum.voucher-source.business-payment-voucher', '67413fc7-c96a-41bd-950f-1f6259882b48', 'enum.voucher-type.pay', 'bank_transaction_no_10001', '0', '600000000112', '10001', 'counter_name', 'account_account_name', '2', NULL, '9b5da2a4-a2ac-40d3-9fe5-0f0e92045e80', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', 'f612a5ea-b063-11e6-bedc-00163e002839', NULL, NULL, NULL, NULL),
('10013', 'e17bc158-7f5b-4444-ac81-94cbeedff4d0', '7cf60311-c5cd-47f7-8825-c89e0df262af', '9db8ce1f-34d2-4601-b246-8d015d92308c', 'ZC2748790EAB0CED9A', '1100.00', '1', 'enum.voucher-source.business-payment-voucher', '67413fc7-c96a-41bd-950f-1f6259882b48', 'enum.voucher-type.pay', 'bank_transaction_no_10001', '0', '600000000112', '10001', 'counter_name', 'account_account_name', '2', NULL, '9b5da2a4-a2ac-40d3-9fe5-0f0e92045e80', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', 'f612a5ea-b063-11e6-bedc-00163e002839', NULL, NULL, NULL, NULL);

INSERT INTO `journal_voucher` (`id`, `account_side`, `bank_identity`, `billing_plan_uuid`, `booking_amount`, `business_voucher_type_uuid`, `business_voucher_uuid`, `cash_flow_amount`, `cash_flow_breif`, `cash_flow_channel_type`, `cash_flow_serial_no`, `cash_flow_uuid`, `checking_level`, `company_id`, `completeness`, `counter_party_account`, `counter_party_name`, `journal_voucher_uuid`, `notification_identity`, `notification_memo`, `notification_record_uuid`, `notified_date`, `settlement_modes`, `source_document_amount`, `source_document_breif`, `source_document_cash_flow_serial_no`, `source_document_counter_party_uuid`, `source_document_identity`, `source_document_uuid`, `status`, `trade_time`, `batch_uuid`, `created_date`, `source_document_counter_party_account`, `source_document_counter_party_name`, `issued_time`, `journal_voucher_type`, `counter_account_type`, `related_bill_contract_info_lv_1`, `related_bill_contract_info_lv_2`, `related_bill_contract_info_lv_3`, `cash_flow_account_info`, `journal_voucher_no`, `related_bill_contract_no_lv_1`, `related_bill_contract_no_lv_2`, `related_bill_contract_no_lv_3`, `related_bill_contract_no_lv_4`, `source_document_no`, `appendix`, `last_modified_time`, `local_party_account`, `local_party_name`, `source_document_local_party_account`, `source_document_local_party_name`, `second_journal_voucher_type`, `third_journal_voucher_type`, `is_has_data_sync_log`, `cash_flow_time`) 
VALUES 
('23', '1', '中国建设银行 ', '6cfd2caf-801d-4339-97b2-4f2748226542', '1000.00', '2b784ef2-a1ab-41fa-b315-8e1bf8cfa2ba', '431fd9e0-827c-4d5e-8e6c-27bf7600df19', '1000.00', NULL, '2', NULL, NULL, '0', '41', '3', '62220202000024320', '李景华', '565828f6-6525-44ab-affd-33a0fe050610', NULL, 'e44609687e0a5d2a9ba28a949964f6dc', 'd7a29a13-a63e-4bc3-aca8-8f7cbb1d706b', NULL, NULL, '1000.00', NULL, NULL, NULL, '24884c68-4b57-4688-a4f3-c72c1d75c70d', '19478f46-27d5-4719-9505-2952d8341c6d', '1', '2017-11-27 23:33:00', NULL, '2017-11-28 10:48:10', '62220202000024320', '李景华', '2017-11-28 10:48:11', '7', '0', '15263f22-cb9d-41cf-94a5-dd57f347c28d', 'bafb466c-bb39-42cf-a08b-a4a1d5ce2d9a', '6cfd2caf-801d-4339-97b2-4f2748226542', '', 'c9213fdc-be4e-45a1-9f35-1bab1ee0ecae', 'FGZ测试信托', 'ce5098f0-bf92-433f-b42e-390c75838b25', 'ZC132541077733187584', 'JS132837069575081984', 'KK132837069440864256', NULL, '2017-12-04 16:27:52', '31600700009000356', '宁波惠康国际工业有限公司', '31600700009000356', '宁波惠康国际工业有限公司', '1', '0', '1', NULL);

INSERT INTO `ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES
('15491565', 'd51db760-ed19-4ee0-9d4d-533579aebf75', '1100.00', '0.00', 'FST_RECIEVABLE_ASSET', '20000', '1', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_BANK_SAVING_GENERAL_PRINCIPAL', '20000.01.01', 'a02c02b9-6f98-11e6-bf08-00163e002839', 'bc830e41-c6df-41da-9874-b8853e06ad82', NULL, NULL, NULL, '10a114f0-ab92-4180-946c-b3472f503beb', NULL, '2017-04-23 17:59:02', '', NULL, NULL, NULL, NULL, '565828f6-6525-44ab-affd-33a0fe050610', '192bd38f-b088-479b-8b89-b5bc5e53ad52', '1', '1', NULL, '6cfd2caf-801d-4339-97b2-4f2748226542', NULL, NULL, NULL, NULL, '7cf60311-c5cd-47f7-8825-c89e0df262af');

INSERT INTO `asset_set` ( `id`,`guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`, `version_lock`, `order_payment_status`, `repay_schedule_no`, `outer_repayment_plan_no`)
VALUES
('45', '2', '0', '1324.80', '0.00', '1200.00', '1200.00', '2016-05-17', NULL, '0.00', '0', '1', '0', '2016-12-11 03:00:38', '6cfd2caf-801d-4339-97b2-4f2748226542', '2016-05-27 18:27:16', '2016-12-11 03:00:38', '2656', 'ZC2730FAE4092E0A6E', '1', NULL, '1', '2', '2016-07-29', '1', '0', '0', 'repurchasing', '2', '2d380fe1-7157-490d-9474-12c5a9901e29', NULL, NULL, NULL, NULL, '0', '0', '0', '2', '0', '5', NULL, '81cbfcab-aac4-4c92-9580-0b1d3d5b768f', 'a0afc961-5fa8-11e6-b2c2-00163e002839', '1', '1adfe8bc-6650-11e7-bff1-00163e002839', '0', NULL, NULL);





SET FOREIGN_KEY_CHECKS=1;