SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM `t_external_trade_batch_cash`;
DELETE FROM `t_external_trade_batch_detail`;
DELETE FROM `t_external_trade_batch`;
DELETE FROM `company`;
DELETE FROM `app`;
DELETE FROM `financial_contract`;


INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch` (`id`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `financial_product_code`, `external_batch_no`, `total_amount`, `deduct_amount`, `online_transfer_amount`, `offline_cash_amount`, `total_detail_number`, `positive_audit_number`, `negative_audit_number`, `active_status`, `detail_verify_result_summary`, `positive_verify_number`, `negative_verify_number`, `trade_audit_progress`, `cash_flow_audit_result`, `trade_date`, `create_time`, `creator_name`, `ip_address`, `last_modified_time`, `remark`, `fst_party_company_uuid`, `snd_party_company_uuid`, `pulled_audit_bill`)
VALUES ('45', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99', 'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '900000004', '10001',
              '64491.00', '39274.00', '10106.00', '15111.00', '100', '0', '0', '0', '1', '100', '0', '0', '0',
                                                                     '2016-12-30', '2017-01-17 12:20:58', NULL, NULL,
        '2017-01-17 12:20:58', NULL, 'c3e6b592-6f9c-11e6-b776-58d41e171d4a', 'c3e6bd08-6f9c-11e6-b776-58d41e171d4b',
        '0');


INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_cash` (`id`, `batch_cash_related_uuid`, `external_trade_batch_uuid`, `cash_flow_uuid`, `related_trade_type`, `account_side`, `bank_sequence_no`, `trade_amount`, `trade_time`, `trade_remark`, `counter_account_no`, `counter_account_name`, `counter_bank_name`, `create_time`, `last_modified_time`)
VALUES
  ('5', '24d5b4b2-ad23-4199-b476-201d89e55fd3', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99', '2323232', '0', NULL, '1212',
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_cash` (`id`, `batch_cash_related_uuid`, `external_trade_batch_uuid`, `cash_flow_uuid`, `related_trade_type`, `account_side`, `bank_sequence_no`, `trade_amount`, `trade_time`, `trade_remark`, `counter_account_no`, `counter_account_name`, `counter_bank_name`, `create_time`, `last_modified_time`)
VALUES
  ('6', '25d5b4b2-ad23-4199-b476-201d89e55fd3', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99', '2323232', '2', NULL, '1213',
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_cash` (`id`, `batch_cash_related_uuid`, `external_trade_batch_uuid`, `cash_flow_uuid`, `related_trade_type`, `account_side`, `bank_sequence_no`, `trade_amount`, `trade_time`, `trade_remark`, `counter_account_no`, `counter_account_name`, `counter_bank_name`, `create_time`, `last_modified_time`)
VALUES
  ('7', '26d5b4b2-ad23-4199-b476-201d89e55fd3', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99', '2323232', '2', NULL, '1214',
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_cash` (`id`, `batch_cash_related_uuid`, `external_trade_batch_uuid`, `cash_flow_uuid`, `related_trade_type`, `account_side`, `bank_sequence_no`, `trade_amount`, `trade_time`, `trade_remark`, `counter_account_no`, `counter_account_name`, `counter_bank_name`, `create_time`, `last_modified_time`)
VALUES
  ('8', '27d5b4b2-ad23-4199-b476-201d89e55fd3', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99', '2323232', '0', NULL, '1215',
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);


INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('734988', '0ada3a5c-b5e1-4de6-961b-a78394d1d71d', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '1', 'f36695f1-cd80-11e6-ac5c-0242ac200000',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('734989', '5f461eb4-cdec-4e1e-a09e-3954ad84686e', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '2', 'f36695f1-cd80-11e6-ac5c-0242ac200001',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('734990', 'ad8d234c-c397-4c0e-928d-b6e96f7f67f7', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '3', 'f36695f1-cd80-11e6-ac5c-0242ac200002',
                  '367a206e-cd81-11e6-ac5c-0242ac110008', '8.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1',
                                                                        '2535052', '18D045C0C58969CD70B92CEC9973E98',
                                                                        '18D045C0C58969CD70B92CEC9973E98', NULL,
                                                                        '2016-12-28 18:00:10', '0', '成功', '4', NULL,
                                                                                                    NULL, '0', '1', '',
                                                                                                    '0', '0', NULL, '0',
        NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('734991', '6af14833-2e9e-4d1f-9011-b10a5d606ad8', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '4', 'f36695f1-cd80-11e6-ac5c-0242ac200003',
                  '367a206e-cd81-11e6-ac5c-0242ac110008', '8.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1',
                                                                        '2535052', '18D045C0C58969CD70B92CEC9973E98',
                                                                        '18D045C0C58969CD70B92CEC9973E98', NULL,
                                                                        '2016-12-28 18:00:10', '0', '成功', '4', NULL,
                                                                                                    NULL, '0', '1', '',
                                                                                                    '0', '0', NULL, '0',
        NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('734992', 'e3cd5496-6562-46c9-9e69-41238e4ba355', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '5', 'f36695f1-cd80-11e6-ac5c-0242ac200004',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('734993', '451ed065-0733-4e57-8123-0f4df5c86067', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '6', 'f36695f1-cd80-11e6-ac5c-0242ac200005',
                  '367a206e-cd81-11e6-ac5c-0242ac110008', '8.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1',
                                                                        '2535052', '18D045C0C58969CD70B92CEC9973E98',
                                                                        '18D045C0C58969CD70B92CEC9973E98', NULL,
                                                                        '2016-12-28 18:00:10', '0', '成功', '4', NULL,
                                                                                                    NULL, '0', '1', '',
                                                                                                    '0', '0', NULL, '0',
        NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('734994', '8ed526cc-8fd5-4e00-84c2-dc7b3a4892b6', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '7', 'f36695f1-cd80-11e6-ac5c-0242ac200006',
                  '367a206e-cd81-11e6-ac5c-0242ac110008', '8.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1',
                                                                        '2535052', '18D045C0C58969CD70B92CEC9973E98',
                                                                        '18D045C0C58969CD70B92CEC9973E98', NULL,
                                                                        '2016-12-28 18:00:10', '0', '成功', '4', NULL,
                                                                                                    NULL, '0', '1', '',
                                                                                                    '0', '0', NULL, '0',
        NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('734995', '99e3b2ba-f6aa-4dd7-a2b2-f3372ecc4845', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '8', 'f36695f1-cd80-11e6-ac5c-0242ac200007',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('734996', 'dd18531e-cca7-43b4-a70d-a4dc06b8357f', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '9', 'f36695f1-cd80-11e6-ac5c-0242ac200008',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('734997', 'd679c5d7-0ca0-4219-8215-8ed5d70a700c', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '10', 'f36695f1-cd80-11e6-ac5c-0242ac200009',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('734998', 'fca13886-91ce-493b-98ab-d972ca3d9eb2', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '11', 'f36695f1-cd80-11e6-ac5c-0242ac200010',
                  '367a206e-cd81-11e6-ac5c-0242ac110008', '8.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1',
                                                                        '2535052', '18D045C0C58969CD70B92CEC9973E98',
                                                                        '18D045C0C58969CD70B92CEC9973E98', NULL,
                                                                        '2016-12-28 18:00:10', '0', '成功', '4', NULL,
                                                                                                    NULL, '0', '1', '',
                                                                                                    '0', '0', NULL, '0',
        NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('734999', 'a151bd23-f353-4b68-9593-b9816a759d78', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '12', 'f36695f1-cd80-11e6-ac5c-0242ac200011',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735000', 'b74f86ad-9e18-44d3-bef3-07ac71e654b5', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '13', 'f36695f1-cd80-11e6-ac5c-0242ac200012',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735001', 'e59facf0-8bed-4a5f-ad46-56b7f446fdb5', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '14', 'f36695f1-cd80-11e6-ac5c-0242ac200013',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735002', 'b232d7e8-fe64-4adc-8299-260977ea9389', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '15', 'f36695f1-cd80-11e6-ac5c-0242ac200014',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735003', '4464ed95-4914-41e1-b2be-e219210de49a', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '16', 'f36695f1-cd80-11e6-ac5c-0242ac200015',
                  '367a206e-cd81-11e6-ac5c-0242ac110008', '8.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1',
                                                                        '2535052', '18D045C0C58969CD70B92CEC9973E98',
                                                                        '18D045C0C58969CD70B92CEC9973E98', NULL,
                                                                        '2016-12-28 18:00:10', '0', '成功', '4', NULL,
                                                                                                    NULL, '0', '1', '',
                                                                                                    '0', '0', NULL, '0',
        NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735004', '9c132a12-7571-4133-b357-b0acec027eae', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '17', 'f36695f1-cd80-11e6-ac5c-0242ac200016',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735005', '7e7d018c-ef46-43db-b444-c8c2ffd64814', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '18', 'f36695f1-cd80-11e6-ac5c-0242ac200017',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735006', '1f8fa169-42da-4d9e-81f1-d7382d249aa8', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '19', 'f36695f1-cd80-11e6-ac5c-0242ac200018',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735007', '8edebdcf-95f3-48dd-bd77-09c744d78452', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '20', 'f36695f1-cd80-11e6-ac5c-0242ac200019',
                  '367a206e-cd81-11e6-ac5c-0242ac110008', '8.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1',
                                                                        '2535052', '18D045C0C58969CD70B92CEC9973E98',
                                                                        '18D045C0C58969CD70B92CEC9973E98', NULL,
                                                                        '2016-12-28 18:00:10', '0', '成功', '4', NULL,
                                                                                                    NULL, '0', '1', '',
                                                                                                    '0', '0', NULL, '0',
        NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735008', 'e4c0085c-9f89-47df-9fc6-02680d4c4f36', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '21', 'f36695f1-cd80-11e6-ac5c-0242ac200020',
                  '367a206e-cd81-11e6-ac5c-0242ac110008', '8.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1',
                                                                        '2535052', '18D045C0C58969CD70B92CEC9973E98',
                                                                        '18D045C0C58969CD70B92CEC9973E98', NULL,
                                                                        '2016-12-28 18:00:10', '0', '成功', '4', NULL,
                                                                                                    NULL, '0', '1', '',
                                                                                                    '0', '0', NULL, '0',
        NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735009', 'edf6e963-df01-46c5-a1ba-5fa4f2d4031c', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '22', 'f36695f1-cd80-11e6-ac5c-0242ac200021',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735010', 'b36bfaf0-b75b-4df5-89e0-4a317815a641', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '23', 'f36695f1-cd80-11e6-ac5c-0242ac200022',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735011', '602255fc-1390-4c66-86a8-3bf29af1d17f', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '24', 'f36695f1-cd80-11e6-ac5c-0242ac200023',
                  '367a206e-cd81-11e6-ac5c-0242ac110008', '8.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1',
                                                                        '2535052', '18D045C0C58969CD70B92CEC9973E98',
                                                                        '18D045C0C58969CD70B92CEC9973E98', NULL,
                                                                        '2016-12-28 18:00:10', '0', '成功', '4', NULL,
                                                                                                    NULL, '0', '1', '',
                                                                                                    '0', '0', NULL, '0',
        NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735012', 'af65038a-52e0-454c-a951-d32f93cc6092', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '25', 'f36695f1-cd80-11e6-ac5c-0242ac200024',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735013', '8e6ce4da-d928-4825-8c9d-8c077eec5c0b', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '26', 'f36695f1-cd80-11e6-ac5c-0242ac200025',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735014', '865dca19-3463-43f9-b9b5-9f8ee4dbef80', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '27', 'f36695f1-cd80-11e6-ac5c-0242ac200026',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735015', '40bc5290-fd45-43b6-b31b-8e95a620e7d8', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '28', 'f36695f1-cd80-11e6-ac5c-0242ac200027',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735016', 'b7c91f1a-21ad-4053-80bf-a0d091ad6d5d', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '29', 'f36695f1-cd80-11e6-ac5c-0242ac200028',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735017', 'bbae8bfe-c41b-413a-acc0-a5a554c6164d', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '30', 'f36695f1-cd80-11e6-ac5c-0242ac200029',
                  '367a206e-cd81-11e6-ac5c-0242ac110008', '8.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1',
                                                                        '2535052', '18D045C0C58969CD70B92CEC9973E98',
                                                                        '18D045C0C58969CD70B92CEC9973E98', NULL,
                                                                        '2016-12-28 18:00:10', '0', '成功', '4', NULL,
                                                                                                    NULL, '0', '1', '',
                                                                                                    '0', '0', NULL, '0',
        NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735018', '63e77fdf-5408-4894-bd5b-2817231b3e63', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '31', 'f36695f1-cd80-11e6-ac5c-0242ac200030',
                  '367a206e-cd81-11e6-ac5c-0242ac110008', '8.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1',
                                                                        '2535052', '18D045C0C58969CD70B92CEC9973E98',
                                                                        '18D045C0C58969CD70B92CEC9973E98', NULL,
                                                                        '2016-12-28 18:00:10', '0', '成功', '4', NULL,
                                                                                                    NULL, '0', '1', '',
                                                                                                    '0', '0', NULL, '0',
        NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735019', '597ba41e-59c4-4574-83c8-8531052af1f3', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '32', 'f36695f1-cd80-11e6-ac5c-0242ac200031',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735020', '0b94ffa0-49e1-404d-a55c-6f86a9f38ae5', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '33', 'f36695f1-cd80-11e6-ac5c-0242ac200032',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735021', 'ef500b5d-3d94-41b8-aadc-1f0e899bdf1c', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '34', 'f36695f1-cd80-11e6-ac5c-0242ac200033',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735022', '08aa5fcf-a87d-488f-8e7a-f0c0a9e256fe', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '35', 'f36695f1-cd80-11e6-ac5c-0242ac200034',
                  '367a206e-cd81-11e6-ac5c-0242ac110008', '8.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1',
                                                                        '2535052', '18D045C0C58969CD70B92CEC9973E98',
                                                                        '18D045C0C58969CD70B92CEC9973E98', NULL,
                                                                        '2016-12-28 18:00:10', '0', '成功', '4', NULL,
                                                                                                    NULL, '0', '1', '',
                                                                                                    '0', '0', NULL, '0',
        NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735023', 'cd0d1a09-e173-4ce6-8700-3555c6034fda', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '36', 'f36695f1-cd80-11e6-ac5c-0242ac200035',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735024', '90497ab5-9de6-49fa-879b-c52a622ec3f7', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '37', 'f36695f1-cd80-11e6-ac5c-0242ac200036',
                  '367a206e-cd81-11e6-ac5c-0242ac110008', '8.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1',
                                                                        '2535052', '18D045C0C58969CD70B92CEC9973E98',
                                                                        '18D045C0C58969CD70B92CEC9973E98', NULL,
                                                                        '2016-12-28 18:00:10', '0', '成功', '4', NULL,
                                                                                                    NULL, '0', '1', '',
                                                                                                    '0', '0', NULL, '0',
        NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735025', '1522e21a-21a6-4aaa-b6df-14ab645884f7', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '38', 'f36695f1-cd80-11e6-ac5c-0242ac200037',
                  '367a206e-cd81-11e6-ac5c-0242ac110008', '8.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1',
                                                                        '2535052', '18D045C0C58969CD70B92CEC9973E98',
                                                                        '18D045C0C58969CD70B92CEC9973E98', NULL,
                                                                        '2016-12-28 18:00:10', '0', '成功', '4', NULL,
                                                                                                    NULL, '0', '1', '',
                                                                                                    '0', '0', NULL, '0',
        NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735026', '602ce3d3-dd17-4a92-a642-df30358bc559', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '39', 'f36695f1-cd80-11e6-ac5c-0242ac200038',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735027', 'cc85b52a-64ac-4105-b5c6-cd67cbed75c9', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '40', 'f36695f1-cd80-11e6-ac5c-0242ac200039',
                  '367a206e-cd81-11e6-ac5c-0242ac110008', '8.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1',
                                                                        '2535052', '18D045C0C58969CD70B92CEC9973E98',
                                                                        '18D045C0C58969CD70B92CEC9973E98', NULL,
                                                                        '2016-12-28 18:00:10', '0', '成功', '4', NULL,
                                                                                                    NULL, '0', '1', '',
                                                                                                    '0', '0', NULL, '0',
        NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735028', 'a9e35139-de2d-4974-80db-686b9e44494f', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '41', 'f36695f1-cd80-11e6-ac5c-0242ac200040',
                  '367a206e-cd81-11e6-ac5c-0242ac110008', '8.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1',
                                                                        '2535052', '18D045C0C58969CD70B92CEC9973E98',
                                                                        '18D045C0C58969CD70B92CEC9973E98', NULL,
                                                                        '2016-12-28 18:00:10', '0', '成功', '4', NULL,
                                                                                                    NULL, '0', '1', '',
                                                                                                    '0', '0', NULL, '0',
        NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735029', 'cbba1d48-41e0-4bef-a876-2d3a426e8d39', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '42', 'f36695f1-cd80-11e6-ac5c-0242ac200041',
                  '367a206e-cd81-11e6-ac5c-0242ac110008', '8.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1',
                                                                        '2535052', '18D045C0C58969CD70B92CEC9973E98',
                                                                        '18D045C0C58969CD70B92CEC9973E98', NULL,
                                                                        '2016-12-28 18:00:10', '0', '成功', '4', NULL,
                                                                                                    NULL, '0', '1', '',
                                                                                                    '0', '0', NULL, '0',
        NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735030', 'ed0adc84-9cae-486b-b0b2-d703ef4db495', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '43', 'f36695f1-cd80-11e6-ac5c-0242ac200042',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735031', '88909219-33b5-4c30-810a-c81081cf9cf0', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '44', 'f36695f1-cd80-11e6-ac5c-0242ac200043',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735032', '943a8466-209c-4445-ac4e-3ae11b1b79e4', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '45', 'f36695f1-cd80-11e6-ac5c-0242ac200044',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735033', '65069b4c-e5af-4cea-9911-663c02d5232b', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '46', 'f36695f1-cd80-11e6-ac5c-0242ac200045',
                  '367a206e-cd81-11e6-ac5c-0242ac110008', '8.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1',
                                                                        '2535052', '18D045C0C58969CD70B92CEC9973E98',
                                                                        '18D045C0C58969CD70B92CEC9973E98', NULL,
                                                                        '2016-12-28 18:00:10', '0', '成功', '4', NULL,
                                                                                                    NULL, '0', '1', '',
                                                                                                    '0', '0', NULL, '0',
        NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735034', 'ba24524d-4d9a-4327-9e82-e886cea208b3', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '47', 'f36695f1-cd80-11e6-ac5c-0242ac200046',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735035', '3cedce53-43fa-42ca-bd8d-5e9ac8735d39', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '48', 'f36695f1-cd80-11e6-ac5c-0242ac200047',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735036', 'b0cd9f63-7c70-48fc-805e-72bd1cff60b2', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '49', 'f36695f1-cd80-11e6-ac5c-0242ac200048',
                  '367a206e-cd81-11e6-ac5c-0242ac110008', '8.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1',
                                                                        '2535052', '18D045C0C58969CD70B92CEC9973E98',
                                                                        '18D045C0C58969CD70B92CEC9973E98', NULL,
                                                                        '2016-12-28 18:00:10', '0', '成功', '4', NULL,
                                                                                                    NULL, '0', '1', '',
                                                                                                    '0', '0', NULL, '0',
        NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735037', 'b5958937-1918-4e5b-8062-dd3c5e934a8f', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '50', 'f36695f1-cd80-11e6-ac5c-0242ac200049',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735038', 'a1af56c0-3cf8-4aed-8a9b-1f120dca8f25', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '51', 'f36695f1-cd80-11e6-ac5c-0242ac200050',
                  '367a206e-cd81-11e6-ac5c-0242ac110008', '8.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1',
                                                                        '2535052', '18D045C0C58969CD70B92CEC9973E98',
                                                                        '18D045C0C58969CD70B92CEC9973E98', NULL,
                                                                        '2016-12-28 18:00:10', '0', '成功', '4', NULL,
                                                                                                    NULL, '0', '1', '',
                                                                                                    '0', '0', NULL, '0',
        NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735039', 'cfbad454-fe54-47a7-9f35-01deecd54e30', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '52', 'f36695f1-cd80-11e6-ac5c-0242ac200051',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735040', '317a956a-3a35-423d-8a18-a70bc305729d', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '53', 'f36695f1-cd80-11e6-ac5c-0242ac200052',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735041', 'b3a0ca83-340a-4c36-ae26-434da98ed1f9', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '54', 'f36695f1-cd80-11e6-ac5c-0242ac200053',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735042', '10933fcf-d8d5-467a-8b76-9fb9a51b5706', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '55', 'f36695f1-cd80-11e6-ac5c-0242ac200054',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735043', 'c5edb58c-5943-41b7-b0db-d78ca4de1dbd', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '56', 'f36695f1-cd80-11e6-ac5c-0242ac200055',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735044', 'b3a1c3e1-5963-4cf4-8520-db75c6f5d38e', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '57', 'f36695f1-cd80-11e6-ac5c-0242ac200056',
                  '367a206e-cd81-11e6-ac5c-0242ac110008', '8.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1',
                                                                        '2535052', '18D045C0C58969CD70B92CEC9973E98',
                                                                        '18D045C0C58969CD70B92CEC9973E98', NULL,
                                                                        '2016-12-28 18:00:10', '0', '成功', '4', NULL,
                                                                                                    NULL, '0', '1', '',
                                                                                                    '0', '0', NULL, '0',
        NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735045', 'be91cb8f-23fa-4721-b4a8-24cc5776efcd', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '58', 'f36695f1-cd80-11e6-ac5c-0242ac200057',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735046', 'a7d8cb06-0763-4315-a40f-1b935a5b39d1', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '59', 'f36695f1-cd80-11e6-ac5c-0242ac200058',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735047', '99b130c3-4479-4436-ae40-7817d9b5b4b8', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '60', 'f36695f1-cd80-11e6-ac5c-0242ac200059',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735048', '5027dfe8-8f3d-49db-a56b-2ec26ea6979a', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '61', 'f36695f1-cd80-11e6-ac5c-0242ac200060',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735049', 'c4af2743-0f1b-413a-94a1-3d13b718dde3', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '62', 'f36695f1-cd80-11e6-ac5c-0242ac200061',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735050', '6a68b4e6-9653-4d23-b6ae-1eb582c4e772', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '63', 'f36695f1-cd80-11e6-ac5c-0242ac200062',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735051', 'd8e73932-cecc-4064-9140-a08393885f69', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '64', 'f36695f1-cd80-11e6-ac5c-0242ac200063',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735052', '2dc49987-00ac-4529-9b5a-f99d3d44e347', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '65', 'f36695f1-cd80-11e6-ac5c-0242ac200064',
                  '367a206e-cd81-11e6-ac5c-0242ac110008', '8.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1',
                                                                        '2535052', '18D045C0C58969CD70B92CEC9973E98',
                                                                        '18D045C0C58969CD70B92CEC9973E98', NULL,
                                                                        '2016-12-28 18:00:10', '0', '成功', '4', NULL,
                                                                                                    NULL, '0', '1', '',
                                                                                                    '0', '0', NULL, '0',
        NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735053', '7c43a3b7-aa84-4601-b35a-cea9c8870559', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '66', 'f36695f1-cd80-11e6-ac5c-0242ac200065',
                  '367a206e-cd81-11e6-ac5c-0242ac110008', '8.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1',
                                                                        '2535052', '18D045C0C58969CD70B92CEC9973E98',
                                                                        '18D045C0C58969CD70B92CEC9973E98', NULL,
                                                                        '2016-12-28 18:00:10', '0', '成功', '4', NULL,
                                                                                                    NULL, '0', '1', '',
                                                                                                    '0', '0', NULL, '0',
        NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735054', '6c0eb358-a916-4ff5-bd6e-47fe525e8d62', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '67', 'f36695f1-cd80-11e6-ac5c-0242ac200066',
                  '367a206e-cd81-11e6-ac5c-0242ac110008', '8.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1',
                                                                        '2535052', '18D045C0C58969CD70B92CEC9973E98',
                                                                        '18D045C0C58969CD70B92CEC9973E98', NULL,
                                                                        '2016-12-28 18:00:10', '0', '成功', '4', NULL,
                                                                                                    NULL, '0', '1', '',
                                                                                                    '0', '0', NULL, '0',
        NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735055', '7c2ee6e2-4dbc-485a-aa58-7fb416e1e9a4', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '68', 'f36695f1-cd80-11e6-ac5c-0242ac200067',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735056', 'd27016be-0c25-42cf-9996-b712eb344d3a', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '69', 'f36695f1-cd80-11e6-ac5c-0242ac200068',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735057', '0d203097-ad42-4d16-a592-5d8c16dc4420', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '70', 'f36695f1-cd80-11e6-ac5c-0242ac200069',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735058', 'ed95fd1a-ad97-4a03-9586-c52a71bb55a5', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '71', 'f36695f1-cd80-11e6-ac5c-0242ac200070',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735059', '25d71247-12ca-4bad-af5a-95caf616c1b4', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '72', 'f36695f1-cd80-11e6-ac5c-0242ac200071',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735060', 'effab856-5d62-4b40-92c6-ff931b0c1c74', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '73', 'f36695f1-cd80-11e6-ac5c-0242ac200072',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735061', '5c65dc4d-7b0f-4180-8d36-d5bfbfc8d7a5', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '74', 'f36695f1-cd80-11e6-ac5c-0242ac200073',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735062', '97ba9df7-fec1-4076-9146-3978a26671d6', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '75', 'f36695f1-cd80-11e6-ac5c-0242ac200074',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735063', '2e8fc3ad-320c-4cc7-8c2d-0b002921c1c3', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '76', 'f36695f1-cd80-11e6-ac5c-0242ac200075',
                  '367a206e-cd81-11e6-ac5c-0242ac110008', '8.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1',
                                                                        '2535052', '18D045C0C58969CD70B92CEC9973E98',
                                                                        '18D045C0C58969CD70B92CEC9973E98', NULL,
                                                                        '2016-12-28 18:00:10', '0', '成功', '4', NULL,
                                                                                                    NULL, '0', '1', '',
                                                                                                    '0', '0', NULL, '0',
        NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735064', '85b0c6fe-2591-47a6-a8fe-95a0b733719e', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '77', 'f36695f1-cd80-11e6-ac5c-0242ac200076',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735065', '8bf30e07-6233-4af3-9aa4-9c2c7545a9da', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '78', 'f36695f1-cd80-11e6-ac5c-0242ac200077',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735066', '97f54096-0e91-4e24-8701-be534b06fb8c', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '79', 'f36695f1-cd80-11e6-ac5c-0242ac200078',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735067', '0d5a9138-86ec-4476-9708-67667bb2f971', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '80', 'f36695f1-cd80-11e6-ac5c-0242ac200079',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735068', '12a3794c-7f64-4ef3-a788-e8a5bb71ea33', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '81', 'f36695f1-cd80-11e6-ac5c-0242ac200080',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735069', 'e4eafee1-8f3e-4a15-8e28-974357fd23e0', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '82', 'f36695f1-cd80-11e6-ac5c-0242ac200081',
                  '367a206e-cd81-11e6-ac5c-0242ac110008', '8.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1',
                                                                        '2535052', '18D045C0C58969CD70B92CEC9973E98',
                                                                        '18D045C0C58969CD70B92CEC9973E98', NULL,
                                                                        '2016-12-28 18:00:10', '0', '成功', '4', NULL,
                                                                                                    NULL, '0', '1', '',
                                                                                                    '0', '0', NULL, '0',
        NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735070', 'bfffa533-d4c6-418b-a347-55e37f354428', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '83', 'f36695f1-cd80-11e6-ac5c-0242ac200082',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735071', 'b29f3106-d6d1-4283-b31c-3fc502a445a3', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '84', 'f36695f1-cd80-11e6-ac5c-0242ac200083',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735072', '91b781f0-2137-4e5b-807f-5e01d621abeb', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '85', 'f36695f1-cd80-11e6-ac5c-0242ac200084',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735073', '42929bcb-2570-4277-adca-7d22b6415380', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '86', 'f36695f1-cd80-11e6-ac5c-0242ac200085',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735074', 'a1c538d7-e6f5-4b1d-9fe0-7e5ede8f89f7', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '87', 'f36695f1-cd80-11e6-ac5c-0242ac200086',
                  '367a206e-cd81-11e6-ac5c-0242ac110008', '8.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1',
                                                                        '2535052', '18D045C0C58969CD70B92CEC9973E98',
                                                                        '18D045C0C58969CD70B92CEC9973E98', NULL,
                                                                        '2016-12-28 18:00:10', '0', '成功', '4', NULL,
                                                                                                    NULL, '0', '1', '',
                                                                                                    '0', '0', NULL, '0',
        NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735075', '1f304c7b-696a-4c04-ad86-57acb312017e', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '88', 'f36695f1-cd80-11e6-ac5c-0242ac200087',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735076', '28ba089b-7840-4aee-bba9-d961fab7432f', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '89', 'f36695f1-cd80-11e6-ac5c-0242ac200088',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735077', '5f3967c7-6074-4660-875c-536594eb19fd', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '90', 'f36695f1-cd80-11e6-ac5c-0242ac200089',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735078', 'aecfa3c4-5f47-41db-84e1-5d862d08123f', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '91', 'f36695f1-cd80-11e6-ac5c-0242ac200090',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735079', '6ca24257-efe5-4d21-8ad7-c9f5903a8645', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '92', 'f36695f1-cd80-11e6-ac5c-0242ac200091',
                  '367a206e-cd81-11e6-ac5c-0242ac110008', '8.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1',
                                                                        '2535052', '18D045C0C58969CD70B92CEC9973E98',
                                                                        '18D045C0C58969CD70B92CEC9973E98', NULL,
                                                                        '2016-12-28 18:00:10', '0', '成功', '4', NULL,
                                                                                                    NULL, '0', '1', '',
                                                                                                    '0', '0', NULL, '0',
        NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735080', '109b1a72-8898-48a7-b67c-cb9a7bb17505', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '93', 'f36695f1-cd80-11e6-ac5c-0242ac200092',
                  '367a206e-cd81-11e6-ac5c-0242ac110008', '8.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1',
                                                                        '2535052', '18D045C0C58969CD70B92CEC9973E98',
                                                                        '18D045C0C58969CD70B92CEC9973E98', NULL,
                                                                        '2016-12-28 18:00:10', '0', '成功', '4', NULL,
                                                                                                    NULL, '0', '1', '',
                                                                                                    '0', '0', NULL, '0',
        NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735081', '35676bed-f73e-45c8-9136-044220bbd614', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '94', 'f36695f1-cd80-11e6-ac5c-0242ac200093',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735082', '72b6cefa-4092-4397-84dc-3fe44a9f1f4c', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '95', 'f36695f1-cd80-11e6-ac5c-0242ac200094',
                  NULL, '3.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'C16045C0C58969CD70B92CEC9973E98', 'C16045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 20:00:10', '2', '现金支付', '4', NULL, NULL, '0', '1', '', '0', '0', NULL,
        '0', NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735083', '81bcebaf-e842-4363-9a08-3eeeb8eedf3f', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '96', 'f36695f1-cd80-11e6-ac5c-0242ac200095',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735084', '7e67f4d7-bc0e-4482-abb8-6f2152baf566', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '97', 'f36695f1-cd80-11e6-ac5c-0242ac200096',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735085', 'e858ab29-2390-473b-ab30-77338b25be44', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '98', 'f36695f1-cd80-11e6-ac5c-0242ac200097',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735086', 'ff323a97-dbbc-4cc0-b98b-c67f961543fd', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '99', 'f36695f1-cd80-11e6-ac5c-0242ac200098',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735087', '8b298193-940d-4d92-af68-8625bc7c9fb7', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '100', 'f36695f1-cd80-11e6-ac5c-0242ac200099',
                  '367a206e-cd81-11e6-ac5c-0242ac110008', '8.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1',
                                                                        '2535052', '18D045C0C58969CD70B92CEC9973E98',
                                                                        '18D045C0C58969CD70B92CEC9973E98', NULL,
                                                                        '2016-12-28 18:00:10', '0', '成功', '4', NULL,
                                                                                                    NULL, '0', '1', '',
                                                                                                    '0', '0', NULL, '0',
        NULL, '2017-01-17 15:21:28', '2017-01-17 15:21:28');
INSERT INTO `galaxy_autotest_yunxin`.`t_external_trade_batch_detail` (`id`, `external_trade_batch_detail_uuid`, `external_trade_batch_uuid`, `financial_contract_uuid`, `financial_contract_id`, `external_batch_no`, `external_record_sn`, `external_record_unique_id`, `external_business_order_no`, `amount`, `charges_detail`, `currency`, `cp_name`, `cp_bank_card_no`, `cp_bank_code_type`, `cp_bank_code`, `contract_unique_id`, `contract_no`, `repayment_plan_no`, `trade_time`, `trade_type`, `trade_remark`, `payment_gateway`, `external_trade_serial_no`, `cash_flow_serial_no`, `active_status`, `detail_verify_result`, `detail_verify_result_remark`, `trade_audit_progress`, `trade_audit_result`, `trade_audit_result_remark`, `cash_flow_audit_result`, `cash_flow_audit_result_remark`, `create_time`, `last_modified_time`)
VALUES ('735088', 'e46c898a-26db-46bc-8229-481849734936', '4ad53e7b-ccd7-4d68-acee-50ae49c18a99',
                  'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5', '10001', '101', 'f36695f1-cd80-11e6-ac5c-0242ac200100',
                  NULL, '2.00', NULL, 'CNY', '卢俊义', '635347896002549488', '1', '2535052',
                                      'K1D045C0C58969CD70B92CEC9973E98', 'K1D045C0C58969CD70B92CEC9973E98', NULL,
                                      '2016-12-28 19:00:10', '1', '转账成功', '4', '6342346543690834234634', NULL, '0', '1',
                                                                  '', '0', '0', NULL, '0', NULL, '2017-01-17 15:21:28',
        '2017-01-17 15:21:28');


INSERT INTO `galaxy_autotest_yunxin`.`company` (`id`, `address`, `full_name`, `short_name`, `uuid`)
VALUES ('1', '上海', '测试金融公司', '测试金融', 'c3e6b592-6f9c-11e6-b776-58d41e171d4a');
INSERT INTO `galaxy_autotest_yunxin`.`company` (`id`, `address`, `full_name`, `short_name`, `uuid`)
VALUES ('2', '南京', '测试商务公司', '测试商户', 'c3e6bd08-6f9c-11e6-b776-58d41e171d4b');
INSERT INTO `galaxy_autotest_yunxin`.`company` (`id`, `address`, `full_name`, `short_name`, `uuid`)
VALUES ('5', '杭州', 'ppd', 'ppd', '1f871f9b-7404-11e6-bf08-00163e002839');
INSERT INTO `galaxy_autotest_yunxin`.`company` (`id`, `address`, `full_name`, `short_name`, `uuid`)
VALUES ('6', '杭州', '杭州随地付网络技术有限公司', '随地付', '9921922c-9b5d-11e6-9926-f8b6a5188a6b');

INSERT INTO `galaxy_autotest_yunxin`.`financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`)
VALUES ('5', '1', '3', '2016-04-01 00:00:00', '900000004', '测试合同', '1', '1', '91', '2016-07-01 00:00:00', '5', '0', '2',
                                                                                                               '90',
                                                                                                               NULL,
                                                                                                               '74a9ce4d-cafc-407d-b013-987077541bdc',
                                                                                                               'beb90aa6-5cba-4535-b783-57f0801ed7c0',
                                                                                                               '1', '1',
                                                                                                               '1', '1',
  '0', '1', NULL, NULL, NULL, NULL, '1', '0', '0', '1', '', NULL, NULL, NULL, NULL, '2016-12-05 16:36:49');

INSERT INTO `galaxy_autotest_yunxin`.`app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`)
VALUES ('1', 'test_app_id', '', b'0', '', '测试商户', '2', NULL);
INSERT INTO `galaxy_autotest_yunxin`.`app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`)
VALUES ('2', 'yongqianbao', '', b'1', '', '用钱宝', '3', NULL);
INSERT INTO `galaxy_autotest_yunxin`.`app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`)
VALUES ('3', 'ppd', NULL, b'0', NULL, '测试商户ppd', '5', NULL);


SET FOREIGN_KEY_CHECKS = 1;