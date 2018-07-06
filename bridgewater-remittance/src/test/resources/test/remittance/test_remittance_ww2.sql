SET foreign_key_checks = 0;

DELETE FROM t_remittance_application;
DELETE FROM t_remittance_application_detail;
DELETE FROM t_remittance_plan;
DELETE FROM t_remittance_plan_exec_log;

INSERT INTO `galaxy_autotest_yunxin`.`t_remittance_application` (`id`, `remittance_application_uuid`, `request_no`, `financial_contract_uuid`, `financial_contract_id`, `financial_product_code`, `contract_unique_id`, `contract_no`, `planned_total_amount`, `actual_total_amount`, `auditor_name`, `audit_time`, `notify_url`, `plan_notify_number`, `actual_notify_number`, `remittance_strategy`, `remark`, `transaction_recipient`, `execution_status`, `execution_remark`, `create_time`, `creator_name`, `ip`, `last_modified_time`, `opposite_receive_date`, `remittance_id`, `total_count`, `actual_count`, `version_lock`)
VALUES (159044, '400efe7d-6603-421b-a4e5-f68e1f5e9589', '4326a757-7d98-41ab-abcd-efgg', 'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 'G31700',
                '30719377-b427-4915-ba11-55a37398a4d1', '5cc4d1f5-f657-431d-b889-e0f505ed92fe', 3.33, 3.33, 'auditorName1', '2016-08-20 00:00:00',
                                                                                                                            'http://www.google.com/ncr', 6, 6, 0,
                                                                                                                            '交易备注', 1, 2, NULL, '2017-07-11 20:57:01',
        't_test_zfb', '192.168.0.200', '2017-07-11 21:49:50', '2017-07-11 20:57:20', NULL, 2, 1, 'twetwetweetwq');

INSERT INTO `galaxy_autotest_yunxin`.`t_remittance_application_detail` (`id`, `remittance_application_detail_uuid`, `remittance_application_uuid`, `financial_contract_uuid`, `financial_contract_id`, `business_record_no`, `priority_level`, `cp_bank_code`, `cp_bank_card_no`, `cp_bank_account_holder`, `cp_id_type`, `cp_id_number`, `cp_bank_province`, `cp_bank_city`, `cp_bank_name`, `planned_payment_date`, `complete_payment_date`, `planned_total_amount`, `actual_total_amount`, `execution_status`, `execution_remark`, `create_time`, `creator_name`, `last_modified_time`, `total_count`, `actual_count`, `version_lock`)
VALUES (202464, '54f6e54c-d0e9-4056-89c8-d7b194c773d3', '400efe7d-6603-421b-a4e5-f68e1f5e9589', 'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 'detailNo1', 1, 'C10102',
                '5685968545868856', '汪水', 0, 'idNumber1', 'bankProvince1', 'bankCity1', 'bankName1', '2017-07-11 00:00:00', NULL, 1.11, 1.11, 2, '测试置成功',
        '2017-07-11 20:57:01', 't_test_zfb', '2017-07-11 21:49:50', 1, 1, '33twetwetweetwq534w6dssd');
INSERT INTO `galaxy_autotest_yunxin`.`t_remittance_application_detail` (`id`, `remittance_application_detail_uuid`, `remittance_application_uuid`, `financial_contract_uuid`, `financial_contract_id`, `business_record_no`, `priority_level`, `cp_bank_code`, `cp_bank_card_no`, `cp_bank_account_holder`, `cp_id_type`, `cp_id_number`, `cp_bank_province`, `cp_bank_city`, `cp_bank_name`, `planned_payment_date`, `complete_payment_date`, `planned_total_amount`, `actual_total_amount`, `execution_status`, `execution_remark`, `create_time`, `creator_name`, `last_modified_time`, `total_count`, `actual_count`, `version_lock`)
VALUES (202466, 'c1bda00d-c3bb-4e40-9f99-88c04d145b3b', '400efe7d-6603-421b-a4e5-f68e1f5e9589', 'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 'detailNo1', 1, 'C10102',
                '5685968545868858', '汪水', 0, 'idNumber1', 'bankProvince1', 'bankCity1', 'bankName1', '2017-07-11 00:00:00', NULL, 2.22, 2.22, 2, '',
        '2017-07-11 21:44:14', 't_test_zfb', '2017-07-11 21:49:50', 1, 0, '525twetwetweetwqerqw');

INSERT INTO `t_remittance_plan` (`id`, `remittance_plan_uuid`, `remittance_application_uuid`, `remittance_application_detail_uuid`, `business_record_no`, `financial_contract_uuid`, `financial_contract_id`, `contract_unique_id`, `contract_no`, `payment_gateway`, `payment_channel_uuid`, `payment_channel_name`, `pg_account_name`, `pg_account_no`, `pg_clearing_account`, `transaction_type`, `transaction_remark`, `priority_level`, `cp_bank_code`, `cp_bank_card_no`, `cp_bank_account_holder`, `cp_id_type`, `cp_id_number`, `cp_bank_province`, `cp_bank_city`, `cp_bank_name`, `planned_payment_date`, `complete_payment_date`, `planned_total_amount`, `actual_total_amount`, `execution_precond`, `execution_status`, `execution_remark`, `transaction_serial_no`, `create_time`, `creator_name`, `last_modified_time`)
VALUES (202605, '7ccef8bb-7589-4bb1-9179-84834ba668d8', '400efe7d-6603-421b-a4e5-f68e1f5e9589', '54f6e54c-d0e9-4056-89c8-d7b194c773d3', 'detailNo1',
                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, '30719377-b427-4915-ba11-55a37398a4d1', '5cc4d1f5-f657-431d-b889-e0f505ed92fe', 2,
                'f8bb9956-1952-4893-98c8-66683d25d7ce', 'G31700平安银企直联', '', '', NULL, 0, '交易备注', 1, 'C10102', '5685968545868856', '汪水', 0, 'idNumber1',
                                                                                                                                        'bankProvince1', 'bankCity1',
                                                                                                                                        'bankName1',
                                                                                                                                        '2017-07-11 00:00:00', NULL,
                                                                                                                                        1.11, 1.11, NULL, 2, '测试置成功',
        NULL, '2017-07-11 20:57:01', 't_test_zfb', '2017-07-11 20:58:50');
INSERT INTO `t_remittance_plan` (`id`, `remittance_plan_uuid`, `remittance_application_uuid`, `remittance_application_detail_uuid`, `business_record_no`, `financial_contract_uuid`, `financial_contract_id`, `contract_unique_id`, `contract_no`, `payment_gateway`, `payment_channel_uuid`, `payment_channel_name`, `pg_account_name`, `pg_account_no`, `pg_clearing_account`, `transaction_type`, `transaction_remark`, `priority_level`, `cp_bank_code`, `cp_bank_card_no`, `cp_bank_account_holder`, `cp_id_type`, `cp_id_number`, `cp_bank_province`, `cp_bank_city`, `cp_bank_name`, `planned_payment_date`, `complete_payment_date`, `planned_total_amount`, `actual_total_amount`, `execution_precond`, `execution_status`, `execution_remark`, `transaction_serial_no`, `create_time`, `creator_name`, `last_modified_time`)
VALUES (202607, '269548eb-9963-4eae-9c4e-daaf36e8e4f1', '400efe7d-6603-421b-a4e5-f68e1f5e9589', 'c1bda00d-c3bb-4e40-9f99-88c04d145b3b', 'detailNo1',
                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, '30719377-b427-4915-ba11-55a37398a4d1', '5cc4d1f5-f657-431d-b889-e0f505ed92fe', 2,
                'f8bb9956-1952-4893-98c8-66683d25d7ce', 'G31700平安银企直联', '', '', NULL, 0, 'ercifk', 1, 'C10102', '5685968545868858', '汪水', 0, 'idNumber1',
                                                                                                                                          'bankProvince1',
                                                                                                                                          'bankCity1', 'bankName1',
                                                                                                                                          '2017-07-11 00:00:00', NULL,
                                                                                                                                          2.22, 2.22, NULL, 2, NULL,
        NULL, '2017-07-11 21:44:14', 't_test_zfb', '2017-07-11 21:49:50');

INSERT INTO `t_remittance_plan_exec_log` (`id`, `remittance_application_uuid`, `remittance_plan_uuid`, `financial_contract_uuid`, `financial_contract_id`, `payment_gateway`, `payment_channel_uuid`, `payment_channel_name`, `pg_account_name`, `pg_account_no`, `pg_clearing_account`, `planned_amount`, `actual_total_amount`, `cp_bank_code`, `cp_bank_card_no`, `cp_bank_account_holder`, `cp_id_type`, `cp_id_number`, `cp_bank_province`, `cp_bank_city`, `cp_bank_name`, `transaction_type`, `transaction_remark`, `exec_req_no`, `exec_rsp_no`, `execution_status`, `execution_remark`, `transaction_serial_no`, `complete_payment_date`, `create_time`, `last_modified_time`, `plan_credit_cash_flow_check_number`, `actual_credit_cash_flow_check_number`, `reverse_status`, `credit_cash_flow_uuid`, `debit_cash_flow_uuid`, `transaction_recipient`, `opposite_receive_date`)
VALUES (202662, '400efe7d-6603-421b-a4e5-f68e1f5e9589', '7ccef8bb-7589-4bb1-9179-84834ba668d8', 'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 2,
                'f8bb9956-1952-4893-98c8-66683d25d7ce', 'G31700平安银企直联', '', '', NULL, 1.11, 1.11, 'C10102', '5685968545868856', '汪水', 0, 'idNumber1', 'bankProvince1',
                                                                                      'bankCity1', 'bankName1', 0, '交易备注', 'e0a87cf9-ad78-42a3-a835-ea8b35be29a0',
                                                                                                                '82255987136016384', 2, '测试置成功', NULL, NULL,
                                                                                                                '2017-07-11 20:57:01', '2017-07-11 20:58:50', 3, 0, 0,
        NULL, NULL, 1, '2017-07-11 20:57:20');
INSERT INTO `t_remittance_plan_exec_log` (`id`, `remittance_application_uuid`, `remittance_plan_uuid`, `financial_contract_uuid`, `financial_contract_id`, `payment_gateway`, `payment_channel_uuid`, `payment_channel_name`, `pg_account_name`, `pg_account_no`, `pg_clearing_account`, `planned_amount`, `actual_total_amount`, `cp_bank_code`, `cp_bank_card_no`, `cp_bank_account_holder`, `cp_id_type`, `cp_id_number`, `cp_bank_province`, `cp_bank_city`, `cp_bank_name`, `transaction_type`, `transaction_remark`, `exec_req_no`, `exec_rsp_no`, `execution_status`, `execution_remark`, `transaction_serial_no`, `complete_payment_date`, `create_time`, `last_modified_time`, `plan_credit_cash_flow_check_number`, `actual_credit_cash_flow_check_number`, `reverse_status`, `credit_cash_flow_uuid`, `debit_cash_flow_uuid`, `transaction_recipient`, `opposite_receive_date`)
VALUES (202666, '400efe7d-6603-421b-a4e5-f68e1f5e9589', '269548eb-9963-4eae-9c4e-daaf36e8e4f1', 'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 2,
                'f8bb9956-1952-4893-98c8-66683d25d7ce', 'G31700平安银企直联', '', '', NULL, 2.22, 2.22, 'C10102', '5685968545868858', '汪水', 0, 'idNumber1', 'bankProvince1',
                                                                                      'bankCity1', 'bankName1', 0, 'ercifk', '9730f107-cd8b-4538-bff2-d561ff2f20f2',
                                                                                                                '82267870777847808', 2, NULL, NULL, NULL,
                                                                                                                '2017-07-11 21:44:14', '2017-07-11 21:49:50', 3, 0, 0,
        NULL, NULL, 1, '2017-07-11 21:44:20');

SET foreign_key_checks = 1;
