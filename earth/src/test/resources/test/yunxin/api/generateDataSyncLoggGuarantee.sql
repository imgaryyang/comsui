SET FOREIGN_KEY_CHECKS=0;


delete from journal_voucher;
delete from financial_contract;
delete from  asset_set;
delete from  source_document;
delete from  `contract`;
delete from  asset_set_extra_charge;
delete from  ledger_book_shelf;
delete from `rent_order`;
delete from `t_interface_data_sync_log`;

INSERT INTO `rent_order` (`id`, `order_type`, `due_date`, `order_no`, `payout_time`, `total_rent`, `customer_id`, `user_upload_param`, `modify_time`, `financial_contract_id`, `repayment_bill_id`, `asset_set_id`, `create_time`, `clearing_status`, `executing_settling_status`, `comment`, `charges_detail`, `order_source`)
VALUES
	(561787, 1, '2016-12-29', 'DB275CCA477AB3E843', '2016-12-28 21:07:35', 250.00, 310045, '', '2016-12-28 21:07:35', 38, 'bcd564948bf846bb91e1f811bf2d1864', 310244, '2016-12-28 17:24:56', 1, 2, NULL, NULL, 0);


INSERT INTO `journal_voucher` (`id`, `account_side`, `bank_identity`, `billing_plan_uuid`, `booking_amount`, `business_voucher_type_uuid`, `business_voucher_uuid`, `cash_flow_amount`, `cash_flow_breif`, `cash_flow_channel_type`, `cash_flow_serial_no`, `cash_flow_uuid`, `checking_level`, `company_id`, `completeness`, `counter_party_account`, `counter_party_name`, `journal_voucher_uuid`, `notification_identity`, `notification_memo`, `notification_record_uuid`, `notified_date`, `settlement_modes`, `source_document_amount`, `source_document_breif`, `source_document_cash_flow_serial_no`, `source_document_counter_party_uuid`, `source_document_identity`, `source_document_uuid`, `status`, `trade_time`, `batch_uuid`, `created_date`, `source_document_counter_party_account`, `source_document_counter_party_name`, `issued_time`, `journal_voucher_type`, `counter_account_type`, `related_bill_contract_info_lv_1`, `related_bill_contract_info_lv_2`, `related_bill_contract_info_lv_3`, `cash_flow_account_info`, `journal_voucher_no`, `related_bill_contract_no_lv_1`, `related_bill_contract_no_lv_2`, `related_bill_contract_no_lv_3`, `related_bill_contract_no_lv_4`, `source_document_no`, `appendix`, `last_modified_time`, `source_document_local_party_account`, `source_document_local_party_name`, `local_party_account`, `local_party_name`, `second_journal_voucher_type`, `third_journal_voucher_type`)
VALUES
	(251891, 1, NULL, 'bcd564948bf846bb91e1f811bf2d1864', 200.00, 'ef8d1fbd-3bcc-42a1-b021-912133d76744', '58497226-e2c5-414c-8129-1c31f0f369db', NULL, NULL, NULL, NULL, NULL, 1, 1, 0, NULL, NULL, 'd8d618ac-b9c3-4226-8268-11a460e329e9', NULL, NULL, NULL, '2016-12-21 21:01:44', 3, 250.00, '', 'sd', NULL, 'eb159bf6ad7446af9236da8d2e5049ae', 'ded1cc284feb4fd88cedb2d4205e7828', 1, '2016-12-21 21:01:44', NULL, '2016-12-28 21:07:02', 'sdf', 'dfsa', NULL, 1, NULL, 'd2812bc5-5057-4a91-b3fd-9019506f0499', '140b6662-cd73-405d-8718-96e5aae9ebe5', '792b77cd-4a56-4f71-a0d2-41aa6a6842e1', NULL, 'e5954746-cacc-448b-a920-46e7bf602448', NULL, NULL, NULL, NULL, NULL, NULL, '2016-12-28 21:07:02', NULL, NULL, NULL, NULL, NULL, NULL),
	(251895, 1, NULL, 'bcd564948bf846bb91e1f811bf2d1864', 50.00, 'ef8d1fbd-3bcc-42a1-b021-912133d76744', '58497226-e2c5-414c-8129-1c31f0f369db', NULL, NULL, NULL, NULL, NULL, 1, 1, 0, NULL, NULL, '6432ac8c-ca2f-4d4a-bc07-bd52f60a1bc6', NULL, NULL, NULL, '2016-12-21 21:01:44', 3, 250.00, '', 'sd', NULL, 'eb159bf6ad7446af9236da8d2e5049ae', 'ded1cc284feb4fd88cedb2d4205e7828', 1, '2016-12-21 21:01:44', NULL, '2016-12-28 21:07:35', 'sdf', 'dfsa', NULL, 1, NULL, 'd2812bc5-5057-4a91-b3fd-9019506f0499', '140b6662-cd73-405d-8718-96e5aae9ebe5', '792b77cd-4a56-4f71-a0d2-41aa6a6842e1', NULL, '3fa04037-18a6-4dc7-9bdc-c57cf1fbe279', NULL, NULL, NULL, NULL, NULL, NULL, '2016-12-28 21:07:35', NULL, NULL, NULL, NULL, NULL, NULL);

	INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`)
VALUES
	(310244, 2, 0, 253.38, 250.00, 0.00, 250.00, '2016-12-01', NULL, 0.00, 0, 1, 0, '2016-12-29 03:07:22', '792b77cd-4a56-4f71-a0d2-41aa6a6842e1', '2016-12-28 17:13:48', '2016-12-29 03:07:22', NULL, 'ZC275CCA3691F92AC8', 310044, NULL, 1, 1, NULL, 1, 0, 0, 'empty_deduct_uuid', NULL, 'd2812bc5-5057-4a91-b3fd-9019506f0499', 'ad4383fb86c45dadde9ae02c8bb56932', '00bfd64b58361d989ac8bf13dccc3c9b', '2016-12-28 17:13:48', '2016-12-28 17:13:48', 0, 0, 0, 1, 0, 1, 0, 'b9dfad43-eaaf-4796-a708-59ab6265937f', '140b6662-cd73-405d-8718-96e5aae9ebe5', NULL);


	INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, `interest_rate_cycle`, `customer_uuid`)
VALUES
	(310044, '140b6662-cd73-405d-8718-96e5aae9ebe5', 'a964da76-a333-4635-b94c-661d0f7d9340', '2016-11-28', 'wwtest1--fa10f36a-d442-44cc-b914-79224ce1b1970', '2099-01-01', NULL, 0.00, 3, 310045, 310044, NULL, '2016-12-28 17:13:48', 0.1560000000, 0, 0, 1, 2, 250.00, 0.0005000000, 1, NULL, 2, 'd2812bc5-5057-4a91-b3fd-9019506f0499', 1, 'b9dfad43-eaaf-4796-a708-59ab6265937f');

	INSERT INTO `asset_set_extra_charge` (`id`, `asset_set_extra_charge_uuid`, `asset_set_uuid`, `create_time`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`)
VALUES
	(60180, 'c4d606fc-b732-4eee-94b7-9e2644552371', '792b77cd-4a56-4f71-a0d2-41aa6a6842e1', '2016-12-28 17:13:48', '2016-12-28 21:35:50', 'FST_RECIEVABLE_ASSET', '20000', 'SND_RECIEVABLE_LOAN_PENALTY', '20000.03', NULL, NULL, 3.38);

	
	
	INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`)
VALUES
	(38, 0, 1, '2016-09-01 00:00:00', 'G00003', '拍拍贷测试', 3, 1, 60, '2017-08-31 00:00:00', 102, 0, 1, 59, 1, '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', 'd2812bc5-5057-4a91-b3fd-9019506f0499', 0, 0, 0, 0, 1, 0, NULL, NULL, 1, '[\"c68558b6-21ce-445a-824f-96a8fb124db6\"]', 0, 1, 0, 0, NULL, 1212.00, NULL, NULL, NULL, '2016-12-01 16:55:49');

	
	INSERT INTO `source_document` (`id`, `company_id`, `source_document_uuid`, `source_document_type`, `create_time`, `issued_time`, `source_document_status`, `source_account_side`, `booking_amount`, `outlier_document_uuid`, `outlier_trade_time`, `outlier_counter_party_account`, `outlier_counter_party_name`, `outlier_account`, `outlie_account_name`, `outlier_account_id`, `outlier_company_id`, `outlier_serial_global_identity`, `outlier_memo`, `outlier_amount`, `outlier_settlement_modes`, `outlier_breif`, `outlier_account_side`, `appendix`, `first_outlier_doc_type`, `second_outlier_doc_type`, `third_outlier_doc_type`, `currency_type`, `audit_status`, `first_party_id`, `second_party_id`, `virtual_account_uuid`, `first_account_id`, `second_account_id`, `third_account_id`, `excute_status`, `excute_result`, `related_contract_uuid`, `financial_contract_uuid`, `source_document_no`, `first_party_type`, `first_party_name`, `virtual_account_no`, `last_modified_time`)
VALUES
	(93, 1, 'ded1cc284feb4fd88cedb2d4205e7828', 1, '2016-12-28 21:04:03', '2016-12-28 21:07:35', 1, 1, 250.00, 'eb159bf6ad7446af9236da8d2e5049ae', '2016-12-21 21:01:44', 'sdf', 'dfsa', '', '', NULL, 1, 'sd', '', 250.00, 3, '', 1, '', 'offline_bill', '', '', NULL, 2, '', '', '', '', '', '', NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, '2016-12-28 21:04:03');

	
SET FOREIGN_KEY_CHECKS=1;