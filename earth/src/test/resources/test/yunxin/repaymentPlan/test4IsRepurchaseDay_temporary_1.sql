SET FOREIGN_KEY_CHECKS=0;

DELETE FROM `asset_set`;
DELETE FROM `contract`;
DELETE FROM `customer`;
DELETE FROM `company`;
DELETE FROM `app`;
DELETE FROM `house`;
DELETE FROM `financial_contract`;
DELETE FROM `ledger_book`;
DELETE FROM `repurchase_doc`;


INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`, `day_of_month`, `pay_for_go`, `repurchase_principal_algorithm`, `repurchase_interest_algorithm`, `repurchase_penalty_algorithm`, `repurchase_other_charges_algorithm`, `repayment_check_days`, `repurchase_cycle`, `days_of_cycle`, `allow_freewheeling_repayment`, `temporary_repurchases`)
VALUES ('108', '0', '0', '2016-03-28 00:00:00', 'xiewenqian', 'test_repurchase', '1', '1', '3', '2019-03-30 00:00:00', '198', '0', '2', '2', NULL, '053aa76e-3248-432f-be00-2db73d4329de', 'e2c178ac-be53-4574-b936-c5d40ba00691', '1', '1', '1', '0', '0', '0', '1.00', '6.00', '0', 'null', '1', '1', '1', '1', '', NULL, NULL, NULL, '2017-05-02 14:54:19', '2017-05-02 17:30:53', '1', '1', '', NULL, '0', '', '', '', '', '-1', '0', '', '0', '[{\"effectEndDate\":\"2017-05-27\",\"effectStartDate\":\"2017-05-09\",\"repurchaseDate\":\"2017-05-08\",\"repurchaseUuid\":\"d7a24137-3d82-4121-ba73-1fc40b90b4ff\"}]');

INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`) VALUES
 ('123456', 'cuuid111', 'cuid111', '2016-08-01', 'G00003(zht765714537113774060)', '2018-01-01', NULL, '0.00', '1', '23', '1', NULL, '2016-08-27 16:06:51', '0.1200000000', '0', '0', '2', '2', '0.02', '11.0000000000', '1', NULL, '2', 'e2c178ac-be53-4574-b936-c5d40ba00691');

INSERT INTO `asset_set` (`id`, `contract_id`, `financial_contract_uuid`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`) VALUES 
('123456','123456','e2c178ac-be53-4574-b936-c5d40ba00691', '0', '1647.20', '800.00', '800.00', '1600.00', '2016-06-01', NULL, '0.00', '0', '1', '0', '2016-10-11 03:01:09', '168c2fbe-a95f-4cae-8514-eabd7fb73a2a', '2016-06-01 14:54:13', '2016-10-11 03:01:09', NULL, 'ZC293D48AAD6E61015',  NULL, '1', '2', '2016-08-11', '1', '0', '0', 'empty_deduct_uuid',NULL,NULL,NULL,NULL);

INSERT INTO `account` (`id`, `account_name`, `account_no`, `bank_name`, `alias`, `attr`, `scan_cash_flow_switch`, `usb_key_configured`, `uuid`, `bank_code`, `cash_flow_config`)
VALUES (198, '银企直连专用账户9', '591902896710201', '', '中国银行', '{\"bankBranchNo\":\"59\",\"usbUuid\":\"b173f24a-3c27-4243-85b7-e93ef6a128ac\"}', 1, 1, 'uuid_5', NULL, NULL);

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`) VALUES 
('23', NULL, NULL, 'ceshi24', 'C75688', '1', '08b5ae5f-7b16-48a1-a858-b8fd7bf6d53f', '0');

INSERT INTO `house` (`id`, `address`, `app_id`) VALUES 
('1', 'cesd', '1');

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`)
VALUES (1, '上海', '测试金融公司', '测试金融', 'a02c02b9-6f98-11e6-bf08-00163e002839'),
	   (2, '上海', '测试金融公司', '测试金融', 'a02c02b9-6f98-11e6-bf08-00163e002838');
	
INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`)
VALUES (1, 'nongfenqi', '', 1, '', '测试分期', 2, NULL),
	   (2, 'paipaidai', '', 1, '', '测试分期', 2, NULL);

INSERT INTO `ledger_book` (`id`, `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`) VALUES 
(1, '053aa76e-3248-432f-be00-2db73d4329de', '1', '1', '1');

SET FOREIGN_KEY_CHECKS=1;

