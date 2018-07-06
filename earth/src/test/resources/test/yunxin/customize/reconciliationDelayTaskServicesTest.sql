set foreign_key_checks=0;

delete from `financial_contract`;
delete from `contract`;
delete from `company`;
delete from `account`;
delete from `customer`;
delete from `house`;
delete from `app`;
delete from `asset_set`;
delete from `asset_set_extra_charge`;
delete from `t_delay_processing_task`;
delete from `repayment_plan_extra_data`;
delete from `financial_contract_configuration`;
delete from `t_delay_processing_task_config`;

INSERT INTO financial_contract (asset_package_format, adva_matuterm, adva_start_date, contract_no, contract_name, app_id, company_id, adva_repo_term, thru_date, capital_account_id, financial_contract_type, loan_overdue_start_day, loan_overdue_end_day, payment_channel_id, ledger_book_no, financial_contract_uuid, sys_normal_deduct_flag, sys_overdue_deduct_flag, sys_create_penalty_flag, sys_create_guarantee_flag, unusual_modify_flag, sys_create_statement_flag, transaction_limit_per_transcation, transaction_limit_per_day, remittance_strategy_mode, app_account_uuids, allow_online_repayment, allow_offline_repayment, allow_advance_deduct_flag, adva_repayment_term, penalty, overdue_default_fee, overdue_service_fee, overdue_other_fee, create_time, last_modified_time, repurchase_approach, repurchase_rule, repurchase_algorithm, day_of_month, pay_for_go, repurchase_principal_algorithm, repurchase_interest_algorithm, repurchase_penalty_algorithm, repurchase_other_charges_algorithm, repayment_check_days) VALUES
 (1, 3, '2016-04-01 00:00:00', 'G08200', '测试合同', 1, 1, 91, '2016-07-01 00:00:00', 5, 0, 1, 90, 2, '74a9ce4d-cafc-407d-b013-987077541bdc', 'financial_contract_uuid_1', 1, 1, 1, 1, 0, 1, null, null, null, null, 1, 0, 0, 0, '(principal+interest)*0.05/100*overdueDay', 123, 23, null, null, '2017-04-06 17:55:17', 1, 1, 'outstandingPrincipal+outstandingOverdueCharges', 30, 0, 'outstandingPrincipal', 'outstandingInterest', 'outstandingPenaltyInterest', null, -1);

 INSERT INTO contract (id, uuid, unique_id, begin_date, contract_no, end_date, asset_type, month_fee, app_id, customer_id, house_id, actual_end_date, create_time, interest_rate, payment_day_in_month, payment_frequency, periods, repayment_way, total_amount, penalty_interest, active_version_no, repayment_plan_operate_logs, state, financial_contract_uuid, interest_rate_cycle, customer_uuid) VALUES
 (1, 'contract_uuid_1', '51fd9fbc-1e13-473b-92c5-4adfade246b7', '2017-02-23', '云信信2016-78-DK（378）号', '2019-01-01', null, 0.00, 1, 102464, 102619, null, '2017-02-23 17:24:34', 1.0000000000, 0, 0, 3, 2, 3500.00, 0.0050000000, 1, null, 2, 'financial_contract_uuid_1', 2, 'customer_uuid_1');

INSERT INTO company (id, address, full_name, short_name, uuid) VALUES
(1, '上海', '测试金融公司', '测试金融', 'a02c02b9-6f98-11e6-bf08-00163e002839');

INSERT INTO account (id, account_name, account_no, bank_name, alias, attr, scan_cash_flow_switch, usb_key_configured, uuid, bank_code, cash_flow_config) VALUES
(5, '银企直连专用账户9', '591902896710201', '工商银行', null, '{"bankBranchNo":"59","usbUuid":"b173f24a-3c27-4243-85b7-e93ef6a128ac"}', true, true, 'uuid_5', 'PAB', '{"nextPageNo":"1"}');

INSERT INTO customer (id, account, mobile, name, source, app_id, customer_uuid, customer_type) VALUES
(102464, '340826199407034015', null, '王林广', '51fd9fbc-1e13-473b-92c5-4adfade246b7', 1, 'customer_uuid_1', 0);

INSERT INTO house (id, address, app_id) VALUES (102619, null, 1);

INSERT INTO app (id, app_id, app_secret, is_disabled, host, name, company_id, addressee) VALUES
(1, 'nongfenqi', '', true, '', '测试分期', 2, null);


INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`)
VALUES
	(1, 0, 0, 980.00, 900.00, 20.00, 920.00, '2017-05-09', '2017-05-09', 0.00, 1, 2, 0, '2017-05-09 17:31:59', '868b9b0b-af17-470f-89a8-22c7576df8e3', '2017-05-09 17:31:59', '2017-05-09 18:02:17', NULL, 'ZC1899966570036105216', 1, '2017-05-09 17:57:52', 1, 0, NULL, 1, 0, 0, 'empty_deduct_uuid', NULL, 'd2812bc5-5057-4a91-b3fd-9019506f0499', '02db7cb99efd405bbf03f65a9b8f96ca', 'd4de8e2b6fc4af949a22e10a3efd3a65', '2017-05-09 17:31:59', '2017-05-09 17:31:59', 0, 0, 0, 1, 0, 2, 0, 'customer_uuid_1', 'contract_uuid_1', 0);

INSERT INTO `asset_set_extra_charge` (`id`, `asset_set_extra_charge_uuid`, `asset_set_uuid`, `create_time`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`)
VALUES
	(1010714, 'a6ebf80e-815f-42e2-851d-ef6dce94a0df', '868b9b0b-af17-470f-89a8-22c7576df8e3', '2017-05-09 17:31:59', '2017-05-09 17:31:59', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_OTHER_FEE', '10000.05', NULL, NULL, 20.00),
	(1010715, 'e0c63f23-8cbd-433c-9dab-32ece27e5f67', '868b9b0b-af17-470f-89a8-22c7576df8e3', '2017-05-09 17:31:59', '2017-05-09 17:31:59', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE', '10000.03', NULL, NULL, 20.00),
	(1010716, '19271988-ba24-4c23-8223-8a91c951f6b8', '868b9b0b-af17-470f-89a8-22c7576df8e3', '2017-05-09 17:31:59', '2017-05-09 17:31:59', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_TECH_FEE', '10000.04', NULL, NULL, 20.00);


INSERT INTO `repayment_plan_extra_data` (`id`, `uuid`, `asset_set_uuid`, `financial_contract_uuid`, `customer_uuid`, `contract_uuid`, `reason_code`, `comment`, `create_time`, `last_modify_time`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`)
VALUES
(1, 'extra_data_uuid_1', '868b9b0b-af17-470f-89a8-22c7576df8e3', 'financial_contract_uuid_1', 'customer_uuid_1', 'contract_uuid_1', 5, NULL, '2017-05-11 19:19:06', '2017-05-11 19:19:06', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);



INSERT INTO `financial_contract_configuration`(`uuid`, `financial_contract_uuid`, `code`,`content`) VALUE
	('financial_contract_configuration_1','financial_contract_uuid_1','1', 'delay_task_config_uuid_01');

INSERT INTO `t_delay_processing_task_config` (`uuid`,`execute_code_version`, `status`)
VALUES ('delay_task_config_uuid_01','execute_code_version_01', 1);


set foreign_key_checks=1;


