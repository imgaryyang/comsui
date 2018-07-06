SET FOREIGN_KEY_CHECKS=0;

DELETE FROM `financial_contract`;
DELETE FROM `account`;
DELETE FROM `contract`;
DELETE FROM `asset_set`;
DELETE FROM `asset_set_extra_charge`;
DELETE FROM `journal_voucher`;
DELETE FROM `source_document`;
DELETE FROM `t_deduct_application`;
DELETE FROM `cash_flow`;
DELETE FROM `source_document_detail`;
DELETE FROM `repurchase_doc`;
DELETE FROM `rent_order`;
DELETE FROM `settlement_order`;


INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`, `day_of_month`)
  VALUES ('38', '0', '0', '2016-09-01 00:00:00', 'G31700', '拍拍贷测试', '3', '1', '10', '2017-08-31 00:00:00', '102', '0', '2', '9', '1', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '0', '0', '1', '1', '1', '0', NULL, NULL, NULL, NULL, '0', '1', '1', '1', '', NULL, NULL, NULL, NULL, '2017-03-16 21:41:08', '1', '1', 'outstandingPrincipal+outstandingInterest+outstandingPenaltyInterest', '6'),
         ('5', '1', '3', '2016-04-01 00:00:00', 'G08200', '测试合同', '1', '1', '91', '2016-07-01 00:00:00', '5', '0', '1', '90', '2', '74a9ce4d-cafc-407d-b013-987077541bdc', '2d380fe1-7157-490d-9474-12c5a9901e29', '1', '1', '1', '1', '0', '1', NULL, NULL, NULL, NULL, '1', '0', '0', '0', '(principal+interest)*0.05/100*overdueDay', '123.00', '23.00', NULL, NULL, '2017-03-16 22:56:48', '1', '1', 'outstandingPrincipal+outstandingOverdueCharges', '30');

INSERT INTO `account` (`id`, `account_name`, `account_no`, `bank_name`, `alias`, `attr`, `scan_cash_flow_switch`, `usb_key_configured`, `uuid`, `bank_code`, `cash_flow_config`) VALUES ('102', '云南信托国际有限公司', '600000000001', '平安银行深圳分行', NULL, NULL, '\0', '\0', 'bd7dd5b1-aa53-4faf-9647-00ddb8ab4b42', 'PAB', NULL);

INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, `interest_rate_cycle`, `customer_uuid`)
  VALUES ('103237', 'e148be81-c448-4da0-ad53-f9f1cb96e4a3', 'f15a55f7-3ca1-4c67-bebf-1da964b44940', '2017-03-21', 'f15a55f7-3ca1-4c67-bebf-1da964b44940', '2017-07-21', NULL, '0.00', '3', '103571', '103726', NULL, '2017-03-21 19:42:34', '0.1560000000', '0', '0', '3', '2', '1500.00', '0.0005000000', '1462614751', '[{\"content\":{0:\"ZC1332794190847463424,ZC1332794191384334336\",1:\"ZC1332794019191209984,ZC1332794019459645440\",2:\"\"},\"ipAddress\":\"115.197.181.189\",\"occurTime\":\"2017-03-21 19:42:36\",\"triggerEvent\":1},{\"content\":{0:\"ZC1332811197709996032,ZC1332811198381084672\",1:\"ZC1332794190847463424,ZC1332794191384334336\",2:\"\"},\"ipAddress\":\"115.197.181.189\",\"occurTime\":\"2017-03-21 19:44:43\",\"triggerEvent\":1}]', '2', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '1', '2bfefd48-1989-45ab-af04-8cac99c0fe5c');

INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`)
  VALUES ('349604', '0', '0', '500.00', '500.00', '0.00', '500.00', '2017-03-31', NULL, '0.00', '0', '1', '0', NULL, '7d47e577-bb1b-4a94-81fa-2d34c9d03e47', '2017-03-21 19:44:42', '2017-03-21 19:44:42', NULL, 'ZC1332811198381084672', '103237', NULL, '3', '0', NULL, '1462614751', '0', '0', 'empty_deduct_uuid', NULL, 'd2812bc5-5057-4a91-b3fd-9019506f0499', '8cec13b4a16c7e21cee24ececfe6cdb7', 'eebf2f4c926f3c347b3dd373aab7959f', '2017-03-21 19:44:42', '2017-03-21 19:44:42', '1', '0', '0', '0', '0', '0', '0', '2bfefd48-1989-45ab-af04-8cac99c0fe5c', 'e148be81-c448-4da0-ad53-f9f1cb96e4a3', NULL);

SET FOREIGN_KEY_CHECKS=1;