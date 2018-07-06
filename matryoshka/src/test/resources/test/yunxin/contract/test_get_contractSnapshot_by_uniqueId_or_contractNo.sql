SET FOREIGN_KEY_CHECKS=0;

DELETE FROM galaxy_autotest_yunxin.contract;

INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, `interest_rate_cycle`, `customer_uuid`)
VALUES
	(1, 'be834b15-56a7-4175-b926-64c90869a2f0', 'ad6b3053-8625-4eb6-a78a-dcabc6132b5d', '2017-04-21', '云信信2016-241-DK(321515600990709045)', '2019-02-02', NULL, 0.00, 2, 124701, 124854, NULL, '2017-04-21 17:50:40', 0.8923000000, 0, 0, 3, 2, 20000.00, 0.0050000000, 1, NULL, 2, 'b674a323-0c30-4e4b-9eba-b14e05a9d80a', 2, 'c714fe88-8ed3-45e9-9807-59ca5bd37ae3');


SET FOREIGN_KEY_CHECKS=1;