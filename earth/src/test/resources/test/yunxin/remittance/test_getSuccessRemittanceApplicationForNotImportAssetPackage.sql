SET FOREIGN_KEY_CHECKS=0;

delete from `t_remittance_application`;


INSERT INTO `t_remittance_application` (`id`, `remittance_application_uuid`, `request_no`, `financial_contract_uuid`, `financial_contract_id`, `financial_product_code`, `contract_unique_id`, `contract_no`, `planned_total_amount`, `actual_total_amount`, `auditor_name`, `audit_time`, `notify_url`, `plan_notify_number`, `actual_notify_number`, `remittance_strategy`, `remark`, `transaction_recipient`, `execution_status`, `execution_remark`, `create_time`, `creator_name`, `ip`, `last_modified_time`, `opposite_receive_date`) 
VALUES 
	('1', 'test_uuid_1', 'test_request_no_1', 'test_financial_contract_uuid', '1', 'test_product_code', 'test_contract_unique_id_1', 'test_contract_no_1', '10000.00', NULL, 'test_auditor', '2016-12-01 12:29:32', NULL, '10', '10', '0','test_remark', '0', '3', NULL, '2016-12-01 12:31:20', 'test_creator', '127.0.0.1', NULL, NULL),
	('2', 'test_uuid_2', 'test_request_no_2', 'test_financial_contract_uuid', '1', 'test_product_code', 'test_contract_unique_id_2', 'test_contract_no_2', '10000.00', NULL, 'test_auditor', '2016-12-01 12:29:32', NULL, '10', '10', '0','test_remark', '0', '2', NULL, '2016-12-01 12:31:20', 'test_creator', '127.0.0.1', NULL, NULL),
	('3', 'test_uuid_3', 'test_request_no_3', 'test_financial_contract_uuid', '1', 'test_product_code', 'test_contract_unique_id_3', 'test_contract_no_3', '10000.00', NULL, 'test_auditor', '2016-12-01 12:29:32', NULL, '10', '10', '0','test_remark', '0', '2', NULL, '2016-12-01 12:31:20', 'test_creator', '127.0.0.1', NULL, NULL);

SET FOREIGN_KEY_CHECKS=1;