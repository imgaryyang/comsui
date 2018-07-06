SET FOREIGN_KEY_CHECKS=0;

DELETE FROM `asset_set`;

INSERT INTO `asset_set` (`id`, `asset_uuid`, `contract_id`, `asset_status`, `repayment_plan_type`, `current_period`, `overdue_status`, `overdue_date`, `active_status`, `active_deduct_application_uuid`, `executing_status`)
VALUES 
('1', '1', '39679', '0', '0', '1', '2', '2016-10-13', '0', 'empty_deduct_uuid', '3');

INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`)
VALUES
('39679', 'a0afc961-5fa8-11e6-b2c2-00163e002839', NULL, '2016-04-17', '2016-78-DK(ZQ2016042522479)', NULL, '1', '0.00', '1', '39686', '1', NULL, '2016-05-27 18:27:16', '0.1560000000', '0', '0', '1', '2', '1200.00', '0.0005000000', '1', NULL, '2', '2d380fe1-7157-490d-9474-12c5a9901e29');
