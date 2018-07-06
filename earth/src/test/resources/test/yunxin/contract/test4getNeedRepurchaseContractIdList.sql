SET FOREIGN_KEY_CHECKS=0;

DELETE FROM `asset_set`;
DELETE FROM `contract`;
DELETE FROM `customer`;
DELETE FROM `company`;
DELETE FROM `app`;
DELETE FROM `house`;
DELETE FROM `financial_contract`;

INSERT INTO `asset_set` (`id`, `asset_uuid`, `contract_id`,`asset_status`, `active_status`, `repayment_plan_type`, `asset_recycle_date`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_deduct_application_uuid`)VALUES 
('1', '1', '39679', '0', '0', '0', '3016-10-20','2016-10-20 14:47:43', '2', '2', '3016-10-13', '2', 'empty_deduct_uuid'),
('2', '123456', '2222', '0','0', '0','2016-10-20', '2016-10-20 14:47:43', '2', '2', '1916-10-13', '1', 'empty_deduct_uuid');


INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `financial_contract_uuid`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`) VALUES
('39679', 'a0afc961-5fa8-11e6-b2c2-00163e002839', NULL,'financialContractUuid', '2016-04-17', '2016-78-DK(ZQ2016042522479)', NULL, '1', '0.00', '1', '39686', '1', NULL, '2016-05-27 18:27:16', '0.1560000000', '0', '0', '1', '2', '1200.00', '0.0005000000', '1', NULL, '2'),
('2222', 'uuid2222', 'uniqueId2222', 'financialContractUuid','2016-04-17', '2016-78-DK(ZQ2016042522478)', NULL, '1', '0.00', '1', '39686', '1', NULL, '2016-05-27 18:27:16', '0.1560000000', '0', '0', '1', '2', '1200.00', '0.0005000000', '1', NULL, '2');


INSERT INTO `financial_contract` (`id`, `financial_contract_uuid`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`,`sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`, `day_of_month`)VALUES
('1','financialContractUuid', '1', '3', '1900-01-01 00:00:00', 'G00003', '平安放款测试用信托合同', '1', '1', '91', '2900-01-01 00:00:00', '1', '0', '1', '90', '1', 'b08637f8-0691-407d-a1a0-f7f5ba380436', '1', '1', '1', '1', '0', '0', NULL, NULL, NULL, NULL, '0', '0', '0', '0', NULL, NULL, NULL, NULL, NULL, NULL,1, 0, 'outstandingPrincipal+outstandingInterest+outstandingPenaltyInterest', NULL);

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`) VALUES
('39686', NULL, NULL, '测试用户1', 'C74211', '1', '81cbfcab-aac4-4c92-9580-0b1d3d5b768f', '0');

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`) VALUES 
('2', '南京', '测试商务公司', '测试分期', 'a02c078d-6f98-11e6-bf08-00163e002839');

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`) VALUES 
('1', 'nongfenqi', '', '', '', '测试分期', '2', NULL);


INSERT INTO `house` (`id`, `address`, `app_id`) VALUES 
('1', 'cesd', '1');

SET FOREIGN_KEY_CHECKS=1;
