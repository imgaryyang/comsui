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


INSERT INTO `repurchase_doc` (`id`, `repurchase_doc_uuid`, `financial_contract_uuid`, `amount`, `repo_start_date`, `repo_end_date`, `repo_days`, `creat_time`, `verification_time`, `last_modifed_time`, `contract_id`, `contract_no`, `app_id`, `app_name`, `customer_uuid`, `customer_name`, `executing_asset_set_uuids`, `asset_set_uuids`, `repurchase_status`, `repurchase_algorithm`, `repurchase_approach`, `repurchase_rule`, `day_of_month`, `adva_repo_term`, `repurchase_periods`, `amount_detail`, `repurchase_principal`, `repurchase_principal_algorithm`, `repurchase_interest`, `repurchase_interest_algorithm`, `repurchase_penalty`, `repurchase_penalty_algorithm`, `repurchase_other_charges`, `repurchase_other_charges_algorithm`, `repurchase_rule_detail`)
VALUES
	(259, 'f25233bb-bf72-47b3-9ea2-92841025d98b', 'd2812bc5-5057-4a91-b3fd-9019506f0499', 2002.00, '2017-07-10', '2017-07-10', 0, '2017-07-10 15:27:23', NULL, '2017-07-10 15:27:23', 12368600, 'wb9988760016', 3, '测试商户ppd', '0894f9af-9e33-47d4-801e-ff6e8aa64744', 'WUBO', NULL, '[\"0f282b52-8dd5-4f38-be5f-5040e8116122\",\"63f7e20c-a2f4-4a2c-be8c-94c915f5c7f7\"]', 0, NULL, 2, NULL, NULL, 12, 2, '{\"amount\":2002.00,\"repurchaseInterest\":2.00,\"repurchaseInterestAlgorithm\":\"outstandingInterest\",\"repurchaseInterestExpression\":\"2.00\",\"repurchaseOtherCharges\":0,\"repurchaseOtherChargesAlgorithm\":\"\",\"repurchaseOtherChargesExpression\":\"\",\"repurchasePenalty\":0.00,\"repurchasePenaltyAlgorithm\":\"outstandingPenaltyInterest\",\"repurchasePenaltyExpression\":\"0\",\"repurchasePrincipal\":2000.00,\"repurchasePrincipalAlgorithm\":\"outstandingPrincipal\",\"repurchasePrincipalExpression\":\"2000.00\"}', 2000.00, 'outstandingPrincipal', 2.00, 'outstandingInterest', 0.00, 'outstandingPenaltyInterest', 0.00, '', NULL);

INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, `interest_rate_cycle`, `customer_uuid`)
VALUES
	(12368600, '95647947-c085-4131-b907-0cbf63983282', 'wb9988760016', '2017-07-08', 'wb9988760016', '2099-01-01', NULL, 0.00, 3, 151331, 151484, NULL, '2017-07-08 17:54:56', 0.1560000000, 0, 0, 3, 2, 3000.00, 0.0005000000, 1, NULL, 4, 'd2812bc5-5057-4a91-b3fd-9019506f0499', 1, '0894f9af-9e33-47d4-801e-ff6e8aa64744');

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`)
VALUES
	(151331, '320301198502169142', NULL, 'WUBO', '27930a84-6673-4760-a489-21ca9e1629fc', 3, '0894f9af-9e33-47d4-801e-ff6e8aa64744', 0);

INSERT INTO `house` (`id`, `address`, `app_id`)
VALUES
	(151484, '6337ed7a-7514-4d3d-96c2-d1f0a9eb4fa1', 3);

INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`)
VALUES
	(12383897, 0, 0, 1004.00, 1000.00, 1.00, 1001.00, '2017-07-08', '2017-07-08', 0.00, 1, 2, 0, '2017-07-08 17:54:58', '15d9d8a2-9fb6-4515-b25b-dfaa4a6d4a0a', '2017-07-08 17:54:56', '2017-07-08 17:55:33', NULL, 'ZC81123003026857984', 12368600, '2017-07-08 17:55:35', 1, 0, NULL, 1, 0, 0, '0c80da90-ca86-4ac0-9741-ff18041be829', NULL, 'd2812bc5-5057-4a91-b3fd-9019506f0499', '8e000f78559db5bd4092e9a99350a605', '009e9d3b93ddd63ba8e89a9efade9591', '2017-07-08 17:54:56', '2017-07-08 17:54:56', 0, 0, 0, 1, 3, 2, 0, '0894f9af-9e33-47d4-801e-ff6e8aa64744', '95647947-c085-4131-b907-0cbf63983282', 0),
	(12383898, 0, 0, 1004.00, 1000.00, 1.00, 1001.00, '2017-09-08', NULL, 0.00, 0, 1, 0, NULL, '0f282b52-8dd5-4f38-be5f-5040e8116122', '2017-07-08 17:54:56', '2017-07-08 17:54:56', NULL, 'ZC81123003320459264', 12368600, NULL, 2, 0, NULL, 1, 2, 0, 'repurchasing', NULL, 'd2812bc5-5057-4a91-b3fd-9019506f0499', '138b977889e35a1ead3beae02c31e05d', '009e9d3b93ddd63ba8e89a9efade9591', '2017-07-08 17:54:56', '2017-07-08 17:54:56', 0, 0, 0, 0, 0, 3, 0, '0894f9af-9e33-47d4-801e-ff6e8aa64744', '95647947-c085-4131-b907-0cbf63983282', NULL),
	(12383899, 0, 0, 1004.00, 1000.00, 1.00, 1001.00, '2017-10-08', NULL, 0.00, 0, 1, 0, NULL, '63f7e20c-a2f4-4a2c-be8c-94c915f5c7f7', '2017-07-08 17:54:56', '2017-07-08 17:54:56', NULL, 'ZC81123003567923200', 12368600, NULL, 3, 0, NULL, 1, 2, 0, 'repurchasing', NULL, 'd2812bc5-5057-4a91-b3fd-9019506f0499', 'df9769db55d9e5abb018d8516053a195', '009e9d3b93ddd63ba8e89a9efade9591', '2017-07-08 17:54:56', '2017-07-08 17:54:56', 0, 0, 0, 0, 0, 3, 0, '0894f9af-9e33-47d4-801e-ff6e8aa64744', '95647947-c085-4131-b907-0cbf63983282', NULL);
SET FOREIGN_KEY_CHECKS=1;