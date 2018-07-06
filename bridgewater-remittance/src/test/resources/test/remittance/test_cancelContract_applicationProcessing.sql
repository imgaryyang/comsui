delete from `t_remittance_application`;
delete from `t_remittance_black_list`;
delete from `financial_contract`;

INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`) 
VALUES 
	('1', NULL, '0', '2016-10-01 18:58:13', 'test_product_code', 'test_contract', '1', '1', '0', '2017-06-01 18:58:56', NULL, '0', '0', '0', '0', NULL, 'test_financial_contract_uuid', '0', '0', '0', '0', '0', '0', NULL, NULL, NULL, NULL, '0', '0', '0', '0', NULL, '0.00', '0.00', '0.00', '2016-12-14 19:00:17', '2016-12-14 19:00:19');

INSERT INTO `t_remittance_application` (`id`, `remittance_application_uuid`, `request_no`, `financial_contract_uuid`, `financial_contract_id`, `financial_product_code`, `contract_unique_id`, `contract_no`, `planned_total_amount`, `actual_total_amount`, `auditor_name`, `audit_time`, `notify_url`, `plan_notify_number`, `actual_notify_number`, `remittance_strategy`, `remark`, `transaction_recipient`, `execution_status`, `execution_remark`, `create_time`, `creator_name`, `ip`, `last_modified_time`, `opposite_receive_date`) 
VALUES 
	('1', 'test_uuid_1', 'test_request_no_1', 'test_financial_contract_uuid', '1', 'test_product_code', 'test_contract_unique_id', 'test_contract_no', '10000.00', NULL, 'test_auditor', '2016-12-01 12:29:32', NULL, '10', '10', '0','test_remark', '0', '3', NULL, '2016-12-01 12:31:20', 'test_creator', '127.0.0.1', NULL, NULL),
	('2', 'test_uuid_2', 'test_request_no_2', 'test_financial_contract_uuid', '1', 'test_product_code', 'test_contract_unique_id', 'test_contract_no', '10000.00', NULL, 'test_auditor', '2016-12-01 12:29:32', NULL, '10', '10', '0','test_remark', '0', '3', NULL, '2016-12-01 12:31:20', 'test_creator', '127.0.0.1', NULL, NULL),
	('3', 'test_uuid_3', 'test_request_no_3', 'test_financial_contract_uuid', '1', 'test_product_code', 'test_contract_unique_id', 'test_contract_no', '10000.00', NULL, 'test_auditor', '2016-12-01 12:29:32', NULL, '10', '10', '0','test_remark', '0', '0', NULL, '2016-12-01 12:31:20', 'test_creator', '127.0.0.1', NULL, NULL);

INSERT INTO `t_remittance_black_list` (`id`, `uuid`, `request_no`, `contract_unique_id`, `contract_no`, `financial_contract_uuid`, `financial_contract_id`, `financial_product_code`, `ip`, `creator_name`, `create_time`, `last_modified_time`) 
VALUES 
	('1', 'test_uuid', 'test_request_no', 'test_unique_id', '', 'test_financial_contract_uuid', '1', 'test_product_code', '127.0.0.1', NULL, '2016-12-08 14:51:20', '2016-12-14 14:51:23');
