delete from `t_remittance_application`;
delete from `t_remittance_plan`;


INSERT INTO `t_remittance_application` (`id`, `remittance_application_uuid`, `request_no`, `financial_contract_uuid`, `financial_contract_id`, `financial_product_code`, `contract_unique_id`, `contract_no`, `planned_total_amount`, `actual_total_amount`, `auditor_name`, `audit_time`, `notify_url`, `plan_notify_number`, `actual_notify_number`, `remittance_strategy`, `remark`, `transaction_recipient`, `execution_status`, `execution_remark`, `create_time`, `creator_name`, `ip`, `last_modified_time`, `opposite_receive_date`)
  VALUES (255, 'f9da5a7f-05d0-415a-8b94-8d1d9651a3bc', '9cde18e1-b926-4816-81bc-62b3cc0458fb', 'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 'G31700', 'FANT078', 'FANT078', 1500.00, 1500.00, 'auditorName1', '2016-8-20 00:00:00', 'http://hello369.tunnel.qydev.com/loan/paidnotic', 1, 1, 0, '交易备注', 1, 2, NULL, '2017-3-23 15:18:58', 't_test_zfb', '192.168.0.33', '2017-3-23 15:19:46', '2017-1-1 00:00:00');
INSERT INTO `t_remittance_application` (`id`, `remittance_application_uuid`, `request_no`, `financial_contract_uuid`, `financial_contract_id`, `financial_product_code`, `contract_unique_id`, `contract_no`, `planned_total_amount`, `actual_total_amount`, `auditor_name`, `audit_time`, `notify_url`, `plan_notify_number`, `actual_notify_number`, `remittance_strategy`, `remark`, `transaction_recipient`, `execution_status`, `execution_remark`, `create_time`, `creator_name`, `ip`, `last_modified_time`, `opposite_receive_date`)
  VALUES (256, '0cbb2545-d2a1-47f0-b7d9-8cf127fe0f4b', 'f9b542db-2c34-4cd2-848e-3021b2683b6d', 'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 'G31700', 'FANT200', 'FANT200', 1500.00, 1500.00, 'auditorName1', '2016-8-20 00:00:00', 'http://hello369.tunnel.qydev.com/loan/paidnotic', 1, 0, 0, '交易备注', 1, 2, NULL, '2017-3-23 15:42:22', 't_test_zfb', '192.168.0.33', '2017-3-23 15:42:56', '2017-1-1 00:00:00');

INSERT INTO `t_remittance_plan` (`id`, `remittance_plan_uuid`, `remittance_application_uuid`, `remittance_application_detail_uuid`, `business_record_no`, `financial_contract_uuid`, `financial_contract_id`, `contract_unique_id`, `contract_no`, `payment_gateway`, `payment_channel_uuid`, `payment_channel_name`, `pg_account_name`, `pg_account_no`, `pg_clearing_account`, `transaction_type`, `transaction_remark`, `priority_level`, `cp_bank_code`, `cp_bank_card_no`, `cp_bank_account_holder`, `cp_id_type`, `cp_id_number`, `cp_bank_province`, `cp_bank_city`, `cp_bank_name`, `planned_payment_date`, `complete_payment_date`, `planned_total_amount`, `actual_total_amount`, `execution_precond`, `execution_status`, `execution_remark`, `transaction_serial_no`, `create_time`, `creator_name`, `last_modified_time`)
  VALUES (98, '37c2c1dc-aaa9-41db-9295-342fe5767e1d', 'f9da5a7f-05d0-415a-8b94-8d1d9651a3bc', '66967f5c-eac8-4f42-bade-2426da45afd5', 'detailNo1', 'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 'FANT076', 'FANT076', 2, 'f8bb9956-1952-4893-98c8-66683d25d7ce', 'G31700平安银企直联', '', '', NULL, 0, NULL, 1, 'C10102', '6214855712106520', '测试用户1', 0, 'idNumber1', 'bankProvince1', 'bankCity1', 'bankName1', '2016-8-20 00:00:00', NULL, 1500.00, 1500.00, NULL, 2, '测试置成功', NULL, '2017-3-23 15:18:58', 't_test_zfb', '2017-3-23 15:19:46');

INSERT INTO `t_remittance_plan` (`id`, `remittance_plan_uuid`, `remittance_application_uuid`, `remittance_application_detail_uuid`, `business_record_no`, `financial_contract_uuid`, `financial_contract_id`, `contract_unique_id`, `contract_no`, `payment_gateway`, `payment_channel_uuid`, `payment_channel_name`, `pg_account_name`, `pg_account_no`, `pg_clearing_account`, `transaction_type`, `transaction_remark`, `priority_level`, `cp_bank_code`, `cp_bank_card_no`, `cp_bank_account_holder`, `cp_id_type`, `cp_id_number`, `cp_bank_province`, `cp_bank_city`, `cp_bank_name`, `planned_payment_date`, `complete_payment_date`, `planned_total_amount`, `actual_total_amount`, `execution_precond`, `execution_status`, `execution_remark`, `transaction_serial_no`, `create_time`, `creator_name`, `last_modified_time`)
  VALUES (99, '37c2c1dc-aaa9-41db-9295-342fe5767e2d', 'f9da5a7f-05d0-415a-8b94-8d1d9651a3bc', '66967f5c-eac8-4f42-bade-2426da45afd5', 'detailNo1', 'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 'FANT076', 'FANT076', 2, 'f8bb9956-1952-4893-98c8-66683d25d7ce', 'G31700平安银企直联', '', '', NULL, 0, NULL, 1, 'C10102', '6214855712106520', '测试用户1', 0, 'idNumber1', 'bankProvince1', 'bankCity1', 'bankName1', '2016-8-20 00:00:00', NULL, 1500.00, 1500.00, NULL, 2, '测试置成功', NULL, '2017-3-23 15:18:58', 't_test_zfb', '2017-3-23 15:19:46');
