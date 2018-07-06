SET FOREIGN_KEY_CHECKS = 0;

delete from `contract`;

INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, `interest_rate_cycle`, `customer_uuid`, `repaymented_periods`,`completion_status`)
	VALUES 
('39759', '0004ff89-de9e-4568-b28d-d3955f8fb23e', 'e3876f95-a930-4e96-9614-ea33ce3422d5', '2016-09-20', '云信信2016-152-DK(ww222222)号', '2016-11-13', NULL, '0.00', '5', '39766', '39924', NULL, '2016-10-14 19:35:14', '0.0347000000', '0', '0', '3', '2', '300000.00', '0.0003000000', '1', NULL, '7', 'e0f67c64-38ad-48aa-a310-ca610b7f74b0', '0', '6c04f7e6-feb9-4f68-af21-f5acb95a3b70','3','0');
INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, `interest_rate_cycle`, `customer_uuid`, `repaymented_periods`,`completion_status`) 
	VALUES 
('1', 'a0b0b011-5fa8-11e6-b2c2-00163e002839', NULL, '2016-06-28', '2016-16-T(test-contract2016062402)', 2016-09-28, '1', '0.00', '1', '270', '431', NULL, '2016-07-01 14:52:46', '15.6000000000', '0', '0', '3', '2', '1200.00', '0.0005000000', '1', NULL, '2', '2d380fe1-7157-490d-9474-12c5a9901e29', '0', 'e72e42ce-a2e2-4d82-8396-dadf54e1d662', '3','0');

SET FOREIGN_KEY_CHECKS = 1;