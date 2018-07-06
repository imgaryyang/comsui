SET FOREIGN_KEY_CHECKS=0;

delete from `financial_contract`;
delete from `ledger_book_shelf`;
delete from `special_account`;
delete from `special_account_flow`;

INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`, `day_of_month`, `pay_for_go`, `repurchase_principal_algorithm`, `repurchase_interest_algorithm`, `repurchase_penalty_algorithm`, `repurchase_other_charges_algorithm`, `temporary_repurchases`, `repurchase_cycle`, `allow_freewheeling_repayment`, `days_of_cycle`, `repayment_check_days`, `capital_party`, `other_party`, `contract_short_name`, `financial_type`, `remittance_object`, `asset_party`, `channel_party`, `supplier`) VALUES
('1', '0', '3', '2016-09-01 00:00:00', 'G32000', '用钱宝测试', '2', '1', '60', '2017-12-01 00:00:00', '101', '0', '1', '2', '1', 'ledger_book_no_1', 'financial_contract_uuid_1', '0', '0', '0', '0', '1', '0', NULL, NULL, NULL, NULL, '0', '0', '0', '7', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, '-1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
	
INSERT INTO `special_account` (`id`, `uuid`, `balance`, `basic_account_type`, `account_type_code`, `account_name`, `level`, `parent_account_uuid`, `fst_level_contract_uuid`, `snd_level_contract_uuid`, `trd_level_contract_uuid`, `create_time`, `last_update_time`, `version`) VALUES
('254', '971b02e6-79fe-4449-aefa-1845f53672d7', '1000.00', '0', '60000', '专户账户', '0', '', 'financial_contract_uuid_1', '', '', '2017-12-27 11:30:20', '2017-12-27 11:30:20', '0741aa6a-5903-425c-a395-829881770f9f'),
('174', '9e2b5c30-bc10-4d49-a285-370cc8fdedfc', '1000.00', '4', '70000', '还款户', '1', '971b02e6-79fe-4449-aefa-1845f53672d7', 'financial_contract_uuid_1', '', '', '2017-12-25 19:23:38', '2017-12-25 19:23:38', '2640c7b1-5e99-4515-a24d-aef23347899b'),
('175', '088aa3f9-d89c-4103-90ae-b78734c0030b', '1000.00', '4', '40000.04', '还款本金', '2', '9e2b5c30-bc10-4d49-a285-370cc8fdedfc', 'financial_contract_uuid_1', '', '', '2017-12-25 19:23:38', '2017-12-25 19:23:38', 'dd0c8f1f-4df0-42f7-871c-e26b5abf39fa'), 
('272', 'ec4a9e56-be35-4e72-a9ef-84b3c8a96d9d', '0.00', '5', '30000', '计提户', '1', '971b02e6-79fe-4449-aefa-1845f53672d7', 'financial_contract_uuid_1', '', '', '2017-12-27 11:30:20', '2017-12-27 11:30:20', 'c5f701b6-82f3-440d-a4f9-57b504299431'),
('273', 'df73f660-21f9-4fc6-b653-cdc484f3068f', '0.00', '5', '30000.1000', '111', '2', 'ec4a9e56-be35-4e72-a9ef-84b3c8a96d9d', 'financial_contract_uuid_1', '', '', '2017-12-27 11:30:20', '2017-12-27 11:30:20', '88413546-6d8a-416d-96d3-5da3030483a0');


SET FOREIGN_KEY_CHECKS=1;