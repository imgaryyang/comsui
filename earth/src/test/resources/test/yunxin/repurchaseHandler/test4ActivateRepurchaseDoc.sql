SET FOREIGN_KEY_CHECKS=0;

DELETE FROM `asset_set`;
DELETE FROM `contract`;
DELETE FROM `customer`;
DELETE FROM `company`;
DELETE FROM `app`;
DELETE FROM `house`;
DELETE FROM `repurchase_doc`;

INSERT INTO `asset_set` (`id`, `asset_uuid`, `contract_id`, `asset_status`, `repayment_plan_type`, `current_period`, `overdue_status`, `overdue_date`, `active_status`, `active_deduct_application_uuid`, `executing_status`) VALUES 
('1', '1', '39679', '0', '0', '1', '2', '2016-10-13', '0', 'empty_deduct_uuid', '1'),
('2', '123456','39679', '0', '0', '1', '2', '2016-10-13', '0', 'empty_deduct_uuid', '1');


INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`) VALUES
('39679', 'a0afc961-5fa8-11e6-b2c2-00163e002839', NULL, '2016-04-17', '2016-78-DK(ZQ2016042522479)', NULL, '1', '0.00', '1', '39686', '1', NULL, '2016-05-27 18:27:16', '0.1560000000', '0', '0', '1', '2', '1200.00', '0.0005000000', '1', NULL, '2', '2d380fe1-7157-490d-9474-12c5a9901e29');

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`) VALUES 
('39686', NULL, NULL, '测试用户1', 'C74211', '1', '81cbfcab-aac4-4c92-9580-0b1d3d5b768f', '0');

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`) VALUES 
('2', '南京', '测试商务公司', '测试分期', 'a02c078d-6f98-11e6-bf08-00163e002839');

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`) VALUES 
('1', 'nongfenqi', '', '', '', '测试分期', '2', NULL);


INSERT INTO `house` (`id`, `address`, `app_id`) VALUES 
('1', 'cesd', '1');

INSERT INTO `repurchase_doc` (`id`, `repurchase_doc_uuid`,`contract_id`, `contract_no`, `financial_contract_uuid`, `amount`, `repo_start_date`, `repo_end_date`, `repo_days`, `app_id`, `app_name`, `customer_uuid`, `customer_name`, `executing_asset_set_uuids`, `asset_set_uuids`, `repurchase_status`) VALUES
('1', 'repurchase_doc_uuid_1', '39679', 'G00003(zht765714537113774061)', '92846f20-87e3-49f4-8f90-fe04a72c0484', '3200.00', '2016-08-18', '2016-08-22', '3', '1', '测试分期', '81cbfcab-aac4-4c92-9580-0b1d3d5b768f', '测试用户1', NULL, '[\"168c2fbe-a95f-4cae-8514-eabd7fb73a2a\",\"168c2fbe-a95f-4cae-8514-eabd7fb73a2a\"]', '3');

SET FOREIGN_KEY_CHECKS=1;
