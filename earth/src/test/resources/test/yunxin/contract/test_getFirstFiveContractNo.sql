delete from `contract`;
delete from `customer`;

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`) 
VALUES 
	('1', NULL, NULL, NULL, NULL, '1', 'test_customer_uuid', '0');

INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, `interest_rate_cycle`, `customer_uuid`) 
VALUES 
	('1', 'test_contract_uuid_1', 'test_unique_id_1', '2017-3-01', 'test_contract_no_1', '2017-08-01', '0', NULL, '1', '1', '1', NULL, '2016-12-20 15:37:58', '0.0000000000', '3', '3', '3', NULL, '33000.00', NULL, '1', NULL, '2', 'test_financial_contract_uuid', '0', 'test_customer_uuid'),
	('2', 'test_contract_uuid_2', 'test_unique_id_2', '2017-3-01', 'test_contract_no_2', '2017-08-01', '0', NULL, '1', '1', '1', NULL, '2016-12-20 15:37:58', '0.0000000000', '3', '3', '3', NULL, '33000.00', NULL, '1', NULL, '2', 'test_financial_contract_uuid', '0', 'test_customer_uuid'),
	('3', 'test_contract_uuid_3', 'test_unique_id_3', '2017-3-01', 'test_contract_no_3', '2017-08-01', '0', NULL, '1', '1', '1', NULL, '2016-12-20 15:37:58', '0.0000000000', '3', '3', '3', NULL, '33000.00', NULL, '1', NULL, '2', 'test_financial_contract_uuid', '0', 'test_customer_uuid'),
	('4', 'test_contract_uuid_4', 'test_unique_id_4', '2017-3-01', 'test_contract_no_4', '2017-08-01', '0', NULL, '1', '1', '1', NULL, '2016-12-20 15:37:58', '0.0000000000', '3', '3', '3', NULL, '33000.00', NULL, '1', NULL, '2', 'test_financial_contract_uuid', '0', 'test_customer_uuid'),
	('5', 'test_contract_uuid_5', 'test_unique_id_5', '2017-3-01', 'test_contract_no_5', '2017-08-01', '0', NULL, '1', '1', '1', NULL, '2016-12-20 15:37:58', '0.0000000000', '3', '3', '3', NULL, '33000.00', NULL, '1', NULL, '2', 'test_financial_contract_uuid', '0', 'test_customer_uuid'),
	('6', 'test_contract_uuid_6', 'test_unique_id_6', '2017-3-01', 'test_contract_no_6', '2017-08-01', '0', NULL, '1', '1', '1', NULL, '2016-12-20 15:37:58', '0.0000000000', '3', '3', '3', NULL, '33000.00', NULL, '1', NULL, '2', 'test_financial_contract_uuid', '0', 'test_customer_uuid');