DELETE FROM `asset_set`;

INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`)
VALUES
	(1, 2, 0, 1236.00, 400.00, 800.00, 1200.00, '2016-05-17', NULL, 0.00, 1, 1, 0, '2016-07-20 03:00:08', '18b302ac-281d-4b39-b906-cded34b42b58', '2016-05-27 18:27:16', '2016-07-20 03:00:08', NULL, 'ZC2730FAE4092E0A6E', 1, '2016-05-17 00:00:00', 1, 1, NULL),
	(2, 2, 0, 1236.00, 600.00, 600.00, 1200.00, '2016-05-17', NULL, 0.00, 1, 1, 0, '2016-07-20 03:00:08', 'b2453ef0-853a-47a7-a41e-de6fe2bad389', '2016-05-27 18:27:16', '2016-07-20 03:00:08', NULL, 'ZC2730FAE4095260A1', 2, '2016-05-17 00:00:00', 1, 1, NULL),
	(3, 2, 0, 2472.00, 1200.00, 1200.00, 2400.00, '2016-06-17', NULL, 0.00, 0, 1, 0, '2016-07-20 03:00:09', '57d007bc-d590-492c-9622-3689cc552539', '2016-05-27 18:27:16', '2016-07-20 03:00:09', NULL, 'ZC2730FAE409733A89', 3, NULL, 1, 1, NULL),
	(4, 2, 0, 2163.00, 1500.00, 600.00, 2100.00, '2016-07-17', NULL, 0.00, 0, 1, 0, '2016-07-20 03:00:09', '61f5a455-93ab-4c14-9cf6-45ab32da456a', '2016-05-27 18:27:16', '2016-07-20 03:00:09', NULL, 'ZC2730FAE409912CBB', 4, NULL, 1, 1, NULL);
