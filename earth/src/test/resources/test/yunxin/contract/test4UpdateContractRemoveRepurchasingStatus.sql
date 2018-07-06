SET FOREIGN_KEY_CHECKS=0;

DELETE FROM `asset_set`;
DELETE FROM `contract`;
DELETE FROM `customer`;
DELETE FROM `company`;
DELETE FROM `app`;
DELETE FROM `house`;

INSERT INTO `asset_set` (`id`, `asset_uuid`, `contract_id`,  `repayment_plan_type`, `current_period`, `overdue_status`, `overdue_date`,`executing_status`, `active_status`,`asset_status`, `active_deduct_application_uuid`) VALUES 
('1', '1', '39679','0', '1', '2', '2016-10-13','1', '0', '0', 'empty_deduct_uuid'),
('2', '123456', '39679', '0', '1', '2', '2016-10-13','1', '0','0', 'repurchasing');

INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`) VALUES
('39679', 'a0afc961-5fa8-11e6-b2c2-00163e002839', NULL, '2016-04-17', '2016-78-DK(ZQ2016042522479)', NULL, '1', '0.00', '1', '39686', '1', NULL, '2016-05-27 18:27:16', '0.1560000000', '0', '0', '1', '2', '1200.00', '0.0005000000', '1', NULL, '4', '2d380fe1-7157-490d-9474-12c5a9901e29');

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`) VALUES 
('39686', NULL, NULL, '测试用户1', 'C74211', '1', '81cbfcab-aac4-4c92-9580-0b1d3d5b768f', '0');

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`) VALUES 
('2', '南京', '测试商务公司', '测试分期', 'a02c078d-6f98-11e6-bf08-00163e002839');

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`) VALUES 
('1', 'nongfenqi', '', '', '', '测试分期', '2', NULL);

INSERT INTO `house` (`id`, `address`, `app_id`) VALUES 
('1', 'cesd', '1');

SET FOREIGN_KEY_CHECKS=1;
