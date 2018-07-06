SET FOREIGN_KEY_CHECKS=0;

delete from galaxy_autotest_yunxin.financial_contract;
DELETE FROM galaxy_autotest_yunxin.ledger_book;
DELETE FROM galaxy_autotest_yunxin.company;
DELETE FROM galaxy_autotest_yunxin.app;
DELETE FROM galaxy_autotest_yunxin.finance_company;

DELETE FROM galaxy_autotest_yunxin.contract;
DELETE FROM galaxy_autotest_yunxin.house;
DELETE FROM galaxy_autotest_yunxin.customer;
DELETE FROM galaxy_autotest_yunxin.asset_set;

INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`, `day_of_month`, `pay_for_go`, `repurchase_principal_algorithm`, `repurchase_interest_algorithm`, `repurchase_penalty_algorithm`, `repurchase_other_charges_algorithm`, `repayment_check_days`, `repurchase_cycle`, `days_of_cycle`, `temporary_repurchases`, `allow_freewheeling_repayment`)
VALUES
  (38, 3, 0, '2016-09-01 00:00:00', 'G31700', '拍拍贷测试', 3, 1, 12, '2017-08-31 00:00:00', 102, 0, 1, 11, 1, '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', 'd2812bc5-5057-4a91-b3fd-9019506f0499', 0, 0, 1, 1, 1, 0, NULL, NULL, 3, 'null', 1, 1, 1, 0, '', NULL, NULL, NULL, NULL, '2017-07-05 10:55:51', 1, 1, 'outstandingPrincipal+outstandingInterest+outstandingPenaltyInterest', 6, 1, 'outstandingPrincipal', 'outstandingInterest', 'outstandingPenaltyInterest', '', 7, NULL, '[]', '[]', 1);

INSERT INTO `ledger_book` (`id`, `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`)
VALUES
  (37, '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', NULL, NULL);

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`)
VALUES
  (1, '上海', '测试金融公司', '测试金融', 'a02c02b9-6f98-11e6-bf08-00163e002839');

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`)
VALUES
  (5, '杭州', 'ppd', 'ppd', '1f871f9b-7404-11e6-bf08-00163e002839');

INSERT INTO `finance_company` (`id`, `company_id`)
VALUES
  (1, 1);

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`)
VALUES
  (3, 'ppd', NULL, 1, NULL, '测试商户ppd', 5, NULL);


INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, `interest_rate_cycle`, `customer_uuid`)
VALUES
  (12368464, '17c900cb-c2c6-42a3-ae80-818192b43696', 'wb111452341271', NULL, 'wb111452341271', '2018-01-01', NULL, 0.00, 3, 151195, 151348, NULL, '2017-07-06 15:01:26', 0.1560000000, 0, 0, 3, 2, 1500.00, 0.0005000000, 1, NULL, 2, 'd2812bc5-5057-4a91-b3fd-9019506f0499', 1, 'c1ad22b2-6de5-4f98-ab95-94f381454174');

INSERT INTO `house` (`id`, `address`, `app_id`)
VALUES
  (151348, 'f3ab19f0-8223-4386-8a9a-a860f956d61c', 3);

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`)
VALUES
  (151195, '320301198502169142', NULL, '王宝', '82241011-13f4-4082-8694-f9e8d561b3c6', 3, 'c1ad22b2-6de5-4f98-ab95-94f381454174', 0);

INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`)
VALUES
  (12383433, 0, 0, 500.00, 500.00, 0.00, 500.00, '2017-07-06', NULL, 0.00, 0, 1, 0, '2017-07-06 15:01:26', 'a6c1c280-4240-4553-a660-bc76f041553c', '2017-07-06 15:01:26', '2017-07-06 15:01:26', NULL, 'ZC80354562385854464', 12368464, NULL, 1, 0, NULL, 1, 0, 0, 'empty_deduct_uuid', NULL, 'd2812bc5-5057-4a91-b3fd-9019506f0499', 'e8d7d949fffbffea0672200001dcab69', '00bfd64b58361d989ac8bf13dccc3c9b', '2017-07-06 15:01:26', '2017-07-06 15:01:26', 0, 0, 0, 1, 0, 1, 0, 'c1ad22b2-6de5-4f98-ab95-94f381454174', '17c900cb-c2c6-42a3-ae80-818192b43696', NULL),
  (12383434, 0, 0, 500.00, 500.00, 0.00, 500.00, '2017-09-06', NULL, 0.00, 0, 1, 0, NULL, 'b3d0a0bf-8219-46b4-ba1a-309ff5e7423c', '2017-07-06 15:01:26', '2017-07-06 15:01:26', NULL, 'ZC80354562402631680', 12368464, NULL, 2, 0, NULL, 1, 0, 0, 'empty_deduct_uuid', NULL, 'd2812bc5-5057-4a91-b3fd-9019506f0499', '67068ce8592a4f69ebeead181859d510', '00bfd64b58361d989ac8bf13dccc3c9b', '2017-07-06 15:01:26', '2017-07-06 15:01:26', 0, 0, 0, 0, 0, 0, 0, 'c1ad22b2-6de5-4f98-ab95-94f381454174', '17c900cb-c2c6-42a3-ae80-818192b43696', NULL),
  (12383435, 0, 0, 500.00, 500.00, 0.00, 500.00, '2017-10-06', NULL, 0.00, 0, 1, 0, NULL, 'f4dfe22a-161d-4d4f-a6a3-cdee97354f28', '2017-07-06 15:01:26', '2017-07-06 15:01:26', NULL, 'ZC80354562411020288', 12368464, NULL, 3, 0, NULL, 1, 0, 0, 'empty_deduct_uuid', NULL, 'd2812bc5-5057-4a91-b3fd-9019506f0499', '7fc9fe1390c09c2ac44de00861f20793', '00bfd64b58361d989ac8bf13dccc3c9b', '2017-07-06 15:01:26', '2017-07-06 15:01:26', 0, 0, 0, 0, 0, 0, 0, 'c1ad22b2-6de5-4f98-ab95-94f381454174', '17c900cb-c2c6-42a3-ae80-818192b43696', NULL);

SET FOREIGN_KEY_CHECKS=1;