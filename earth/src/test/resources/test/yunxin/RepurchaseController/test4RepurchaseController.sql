
SET FOREIGN_KEY_CHECKS=0;

delete from `repurchase_doc`;
delete from `contract`;
delete from `asset_set`;
delete from `principal`;
delete from `customer`;
delete from `company`;
delete from `app`;
delete from `house`;

INSERT INTO `principal` (`id`, `authority`, `name`, `password`, `start_date`, `thru_date`, `t_user_id`, `created_time`, `creator_id`) VALUES 
('1', 'ROLE_SUPER_USER', 'test', 'e10adc3949ba59abbe56e057f20f883e', NULL, NULL, NULL, NULL, NULL);

INSERT INTO `repurchase_doc` (`id`, `repurchase_doc_uuid`, `financial_contract_uuid`, `amount`, `repo_start_date`, `repo_end_date`, `repo_days`, `creat_time`, `verification_time`, `last_modifed_time`, `contract_id`, `contract_no`, `app_id`, `app_name`, `customer_uuid`, `customer_name`, `executing_asset_set_uuids`, `asset_set_uuids`, `repurchase_status`) 
VALUES (1, 'repurchase_doc_uuid1', 'financial_contract_uuid', '11.11', '2016-10-31', '2016-11-5', '5', '2016-10-31 17:11:20', '2016-10-31 17:12:20',   '2016-10-31 17:13:20', '1', 'contract_no', '1', 'app_name', 'customer_uuid', 'customer_name', 'executing_asset_set_uuids', '[\"asset_set_uuids\"]', '0'),
       (8, 'repurchase_doc_uuid8', 'financial_contract_uuid', '11.11', '2016-10-31', '2016-11-5', '5', '2016-10-31 17:11:20', '2016-10-31 17:12:20',   '2016-10-31 17:13:20', '2', 'contract_no', '1', 'app_name', 'customer_uuid', 'customer_name', 'executing_asset_set_uuids', '[\"asset_set_uuids\"]', '3'),
       (10, 'repurchase_doc_uuid10', 'financial_contract_uuid', '11.11', '2016-10-31', '2016-11-5', '5', '2016-10-31 17:11:20', '2016-10-31 17:12:20',   '2016-10-31 17:13:20', '2', 'contract_no', '1', 'app_name', 'customer_uuid', 'customer_name', 'executing_asset_set_uuids', '[\"asset_set_uuids\"]', '2'),
       (9, 'repurchase_doc_uuid9', 'financial_contract_uuid', '11.11', '2016-10-31', '2016-11-5', '5', '2016-10-31 17:11:20', '2016-10-31 17:12:20',   '2016-10-31 17:13:20', '3', 'contract_no', '1', 'app_name', 'customer_uuid', 'customer_name', 'executing_asset_set_uuids', '[\"asset_set_uuids\"]', '2'),
       (2, 'repurchase_doc_uuid', 'financial_contract_uuid', '11.11', '2016-10-31', '2016-11-5', '5', '2016-10-31 17:11:20', '2016-10-31 17:12:20',   '2016-10-31 17:13:20', '1', 'contract_no', '1', 'app_name', 'customer_uuid', 'customer_name', 'executing_asset_set_uuids', '[\"asset_set_uuids\"]', '1'),
       (3, 'repurchase_doc_uuid', 'financial_contract_uuid', '11.11', '2016-10-31', '2016-11-6', '5', '2016-10-31 17:11:20', '2016-10-31 17:12:20',   '2016-10-31 17:13:20', '1', 'contract_no', '1', 'app_name', 'customer_uuid', 'customer_name', 'executing_asset_set_uuids','[\"asset_set_uuids\"]', '1'),
       (4, 'repurchase_doc_uuid', 'financial_contract_uuid', '11.11', '2016-10-30', '2016-11-6', '5', '2016-10-31 17:11:20', '2016-10-31 17:12:20',   '2016-10-31 17:13:20', '1', 'contract_no', '1', 'app_name', 'customer_uuid', 'customer_name', 'executing_asset_set_uuids', '[\"asset_set_uuids\"]', '1'),
       (5, 'repurchase_doc_uuid', 'financial_contract_uuid', '11.11', '2016-10-30', '2016-11-6', '5', '2016-10-31 17:11:20', '2016-10-31 17:12:20',   '2016-10-31 17:13:20', '1', 'contract_no', '1', 'app_name', 'customer_uuid', 'customer_name1', 'executing_asset_set_uuids', '[\"asset_set_uuids\"]', '1'),
       (6, 'repurchase_doc_uuid6', 'financial_contract_uuid', '11.11', '2016-10-30', '2016-11-6', '5', '2016-10-31 17:11:20', '2016-10-31 17:12:20',   '2016-10-31 17:13:20', '6', 'contract_no1', '1', 'app_name', 'customer_uuid', 'customer_name1', 'executing_asset_set_uuids', '[\"asset_set_uuids\"]', '3'),
       (7, 'repurchase_doc_uuid7', 'financial_contract_uuid', '11.11', '2016-10-30', '2016-11-6', '5', '2016-10-31 17:11:20', '2016-10-31 17:12:20',   '2016-10-31 17:13:20', '7', 'contract_no1', '2', 'app_name', 'customer_uuid', 'customer_name1', 'executing_asset_set_uuids', '[\"asset_set_uuids\"]', '0');

INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`) 
VALUES ('1', 'uuid1', 'unique_id1', '2015-10-19', 'DKHD-001', '2016-10-19', NULL, NULL, '1', '1', '1', NULL, '2015-10-19 13:34:35', '0.0010000000', '1', '1', '12', '0', '1000.00', '0.0001000000', '1', NULL, '2', NULL),
       ('2', 'uuid2', 'unique_id2', '2015-10-19', 'DKHD-002', '2016-10-19', NULL, NULL, '1', '1', '1', NULL, '2015-10-19 13:34:35', '0.0010000000', '1', '1', '12', '0', '1000.00', '0.0001000000', '1', NULL, '2', NULL),
       ('3', 'uuid3', 'unique_id3', '2015-10-19', 'DKHD-003', '2016-10-19', NULL, NULL, '1', '1', '1', NULL, '2015-10-19 13:34:35', '0.0010000000', '1', '1', '12', '0', '1000.00', '0.0001000000', '1', NULL, '2', NULL),
       ('7', 'uuid7', 'unique_id7', '2015-10-19', 'DKHD-004', '2016-10-19', NULL, NULL, '1', '1', '1', NULL, '2015-10-19 13:34:35', '0.0010000000', '1', '1', '12', '0', '1000.00', '0.0001000000', '1', NULL, '2', NULL),
       ('6', 'uuid6', 'unique_id6', '2015-10-19', 'DKHD-005', '2016-10-19', NULL, NULL, '1', '1', '1', NULL, '2015-10-19 13:34:35', '0.0010000000', '1', '1', '12', '0', '1000.00', '0.0001000000', '1', NULL, '2', NULL);

INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `repurchase_status`, `active_deduct_application_uuid`, `sync_status`) 
VALUES ('1', '0', '0', '1010.00', NULL, NULL, NULL, '2016-09-27', NULL, NULL, '0', NULL, '0', NULL, 'asset_uuid_1', '2015-10-19 13:34:35', '2015-10-19 13:34:35', NULL, 'DKHD-001-01', '1', '2015-10-19 13:34:35', '0', '0', NULL, '1', '0',  '0', 'repurchasing', '0'),
       ('2', '0', '0', '1000.00', NULL, NULL, NULL, '2016-09-27', NULL, NULL, '0', NULL, '0', NULL, 'asset_uuid_2', '2015-10-19 13:34:35', '2015-10-19 13:34:35', NULL, 'DKHD-001-02', '2', '2015-10-19 13:34:35', '0', '0', NULL, '1', '0', NULL, 'empty_deduct_uuid', '0'),
       ('3', '0', '0', '1000.00', NULL, NULL, NULL, '2016-09-27', NULL, NULL, '0', NULL, '0', NULL, 'asset_uuid_3', '2015-10-19 13:34:35', '2015-10-19 13:34:35', NULL, 'DKHD-001-03', '3', '2015-10-19 13:34:35', '0', '0', NULL, '1', '0', NULL, 'repurchasing', '0');

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`) VALUES 
('1', NULL, NULL, '测试用户1', 'C74211', '1', 'customer_uuid', '0');

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`) VALUES 
('2', '南京', '测试商务公司', '测试分期', 'a02c078d-6f98-11e6-bf08-00163e002839');

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`) VALUES 
('1', 'nongfenqi', '', '', '', '测试分期', '2', NULL);

INSERT INTO `house` (`id`, `address`, `app_id`) VALUES 
('1', 'cesd', '1');

SET FOREIGN_KEY_CHECKS=1;