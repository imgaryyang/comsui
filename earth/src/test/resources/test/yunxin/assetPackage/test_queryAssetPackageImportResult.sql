SET FOREIGN_KEY_CHECKS=0;

delete from `contract`;

delete from `t_remittance_application`;

delete from `customer`;

INSERT INTO `t_remittance_application` (`id`, `remittance_application_uuid`, `request_no`, `financial_contract_uuid`, `financial_contract_id`, `financial_product_code`, `contract_unique_id`, `contract_no`, `planned_total_amount`, `actual_total_amount`, `auditor_name`, `audit_time`, `notify_url`, `plan_notify_number`, `actual_notify_number`, `remittance_strategy`, `remark`, `transaction_recipient`, `execution_status`, `execution_remark`, `create_time`, `creator_name`, `ip`, `last_modified_time`, `opposite_receive_date`) 
	VALUES 
		  ('49', 'd0f124bd-a10c-4778-9888-aa3ffcd802as', 'bbab8fbc-5cda-4609-8563-59c2838c3afd', 'db36ecc9-d80c-4350-bd0d-59b1139d550d', '33', 'G08200', 'CONTRACT_UNIQUEID_ID001', 'CONTRACT_NO001', '0.02', '0.00', '', NULL, 'http://101.52.128.166/Loan/PaidNotic', '3', '3', '0', '测试放款', '1', '2', NULL, '2016-09-01 11:29:28', 't_test_zfb', '115.197.179.183', '2016-09-01 11:43:27', '2017-02-17 00:00:00'),
          ('50', 'd0f124bd-a10c-4778-9888-aa3ffcd802gf', 'bbab8fbc-5cda-4609-8563-59c2838c3232a', 'db36ecc9-d80c-4350-bd0d-59b1139d5fds', '33', 'G08200', 'CONTRACT_UNIQUEID_ID002', 'CONTRACT_NO002', '0.02', '0.00', '', NULL, 'http://101.52.128.166/Loan/PaidNotic', '3', '3', '0', '测试放款', '1', '2', NULL, '2016-09-01 11:29:28', 't_test_zfb', '115.197.179.183', '2016-09-01 11:43:27', '2016-11-01 00:00:00'),
          ('51', 'd0f124bd-a10c-4778-9888-aa3ffcd802ec', 'bbab8fbc-5cda-4609-8563-59c2838c3aer', 'db36ecc9-d80c-4350-bd0d-59b1139d55rd', '33', 'G08200', 'CONTRACT_UNIQUEID_ID003', 'CONTRACT_NO003', '0.02', '0.00', '', NULL, 'http://101.52.128.166/Loan/PaidNotic', '3', '3', '0', '测试放款', '1', '2', NULL, '2016-09-01 11:29:28', 't_test_zfb', '115.197.179.183', '2016-09-01 11:43:27', '2016-11-01 00:00:00'),
          ('52', 'd0f124bd-a10c-4778-9888-aa3ffcd802ed', 'bbab8fbc-5cda-4609-8563-59c2838c3af2', 'db36ecc9-d80c-4350-bd0d-59b1139d550d', '33', 'G08200', 'CONTRACT_UNIQUEID_ID004', 'CONTRACT_NO004', '0.02', '0.00', '', NULL, 'http://101.52.128.166/Loan/PaidNotic', '3', '3', '0', '测试放款', '1', '2', NULL, '2016-09-01 11:29:28', 't_test_zfb', '115.197.179.183', '2016-09-01 11:43:27', '2016-11-01 00:00:00'),
          ('53', 'd0f124bd-a10c-4778-9888-aa3ffcd802ef', 'bbab8fbc-5cda-4609-8563-59c2838c3afw', 'db36ecc9-d80c-4350-bd0d-59b1139d550d', '33', 'G08200', 'CONTRACT_UNIQUEID_ID004', 'CONTRACT_NO004', '0.02', '0.00', '', NULL, 'http://101.52.128.166/Loan/PaidNotic', '3', '3', '0', '测试放款', '1', '2', NULL, '2016-09-01 11:29:28', 't_test_zfb', '115.197.179.183', '2016-09-01 11:43:27', '2016-11-02 00:00:00');

INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, `interest_rate_cycle`, `customer_uuid`) 
	VALUES 
		  ('6612', 'aedad0cf-5563-4112-973c-67591e585f76', 'CONTRACT_UNIQUEID_ID001', '2017-02-17', 'CONTRACT_NO001', '2099-01-01', NULL, '0.00', '1', '6612', '7423', NULL, '2017-02-15 17:12:52', '0.1560000000', '0', '0', '2', '2', '444.00', '0.0005000000', '1', NULL, '2', 'beb90aa6-5cba-4535-b783-57f0801ed7c0', '1', '766b0bab-e606-4763-9697-96ef2d5865a9');

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`)
    VALUES 
    	  ('6612', '330683199403062411', NULL, '测试员1', 'fa7bf110-0145-4ea8-b972-34097f696afe', '1', '766b0bab-e606-4763-9697-96ef2d5865a9', '0');

SET FOREIGN_KEY_CHECKS=1;