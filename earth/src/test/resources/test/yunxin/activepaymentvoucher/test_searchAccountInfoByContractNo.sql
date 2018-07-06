delete from `contract`; 
delete from `contract_account`; 
delete from `asset_set`; 
delete from `customer`;


INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, `interest_rate_cycle`)
VALUES
(1, 'a0afc961-5fa8-11e6-b2c2-00163e002839', NULL, '2016-04-17', '2016-78-DK(ZQ2016042522479)', NULL, 1, 0.00, 1, 1, 162, NULL, '2016-05-27 18:27:16', 0.1560000000, 0, 0, 1, 2, 1200.00, 0.0005000000, 1, NULL, 6, '2d380fe1-7157-490d-9474-12c5a9901e29', 0),
(2, 'a0afcd9a-5fa8-11e6-b2c2-00163e002839', NULL, '2016-04-17', '2016-78-DK(ZQ2016042422395)', NULL, 1, 0.00, 1, 2, 163, NULL, '2016-05-27 18:27:16', 0.1560000000, 0, 0, 1, 2, 1200.00, 0.0005000000, 1, NULL, 4, '2d380fe1-7157-490d-9474-12c5a9901e29', 0),
(3, 'a0afcf5b-5fa8-11e6-b2c2-00163e002839', NULL, '2016-04-17', '2016-78-DK(ZQ2016042522502)', NULL, 1, 0.00, 1, 3, 164, NULL, '2016-05-27 18:27:16', 0.1560000000, 0, 0, 1, 2, 2400.00, 0.0005000000, 1, NULL, 6, '2d380fe1-7157-490d-9474-12c5a9901e29', 0);

	
INSERT INTO `contract_account` (`id`, `pay_ac_no`, `bankcard_type`, `contract_id`, `payer_name`, `bank`, `bind_id`, `id_card_num`, `bank_code`, `province`, `province_code`, `city`, `city_code`, `standard_bank_code`, `from_date`, `thru_date`)
VALUES
(1, '6217000000000003006', NULL, 1, '测试用户1', '中国邮政储蓄银行', NULL, '6217000000000000000', '403', '安徽省', NULL, '亳州', NULL, NULL, '2016-04-17 00:00:00', '2900-01-01 00:00:00'),
(3, '6217000000000003015', NULL, 3, '测试用户1', '中国建设银行', NULL, NULL, '105', '安徽省', NULL, '亳州', NULL, NULL, '2016-04-17 00:00:00', '2900-01-01 00:00:00');


INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`)
VALUES
(1, 2, 0, 1324.80, 0.00, 1200.00, 1200.00, '2016-05-17', NULL, 0.00, 0, 1, 0, '2016-12-11 03:00:38', '18b302ac-281d-4b39-b906-cded34b42b58', '2016-05-27 18:27:16', '2016-12-11 03:00:38', '2656', 'ZC2730FAE4092E0A6E', 1, NULL, 1, 2, '2016-07-29', 1, 0, 0, 'repurchasing', 2, '2d380fe1-7157-490d-9474-12c5a9901e29', NULL, NULL, NULL, NULL),
(2, 2, 0, 1324.80, 0.00, 1200.00, 1200.00, '2016-05-17', NULL, 0.00, 0, 1, 0, '2016-12-11 03:00:38', 'b2453ef0-853a-47a7-a41e-de6fe2bad389', '2016-05-27 18:27:16', '2016-12-11 03:00:38', NULL, 'ZC2730FAE4095260A1', 2, NULL, 1, 2, '2016-07-01', 1, 0, 0, 'repurchasing', 0, '2d380fe1-7157-490d-9474-12c5a9901e29', NULL, NULL, NULL, NULL);

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`)
VALUES
(2, NULL, NULL, '测试用户2', 'C74931', 1, '3da51915-6207-43c9-a444-e521faded5b0', 0),
(3, NULL, NULL, '测试用户3', 'C30433', 1, '2a810605-46c2-4dc5-87c1-7198d735d76a', 0),
(1, NULL, NULL, '测试用户1', 'C74211', 1, '81cbfcab-aac4-4c92-9580-0b1d3d5b768f', 0);




