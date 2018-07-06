DELETE FROM `financial_contract`;
DELETE FROM `asset_set`;
DELETE FROM `asset_set_extra_charge`;
DELETE FROM `t_interface_data_sync_log`;

INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`) 
VALUES ('5', '1', '3', '2016-04-01 00:00:00', '2016-78-DK(ZQ2016042522537)', '测试合同', '1', '1', '91', '2016-07-01 00:00:00', '5', '0', '5', '90', '2', '74a9ce4d-cafc-407d-b013-987077541bdc', '2d380fe1-7157-490d-9474-12c5a9901e29', '0', '0', '0', '0', '0', '0');

INSERT INTO  `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`) 
VALUES 
('18', '0', '0', '2406.00', '0.00', '2400.00', '2400.00', '2016-05-18', '2016-05-27', '0.00', '1', '2', '0', '2016-05-27 18:28:01', '90d22404-99ea-4b5a-872c-5ffc6c7909fd', '2016-05-27 18:27:17', '2016-05-27 18:30:23', '1', 'ZC2730FAE40B0F2478', '24', '2016-05-27 18:21:04', '1', '0', NULL, '1', '0', '0', 'empty_deduct_uuid');

INSERT INTO `asset_set_extra_charge` (`id`, `asset_set_extra_charge_uuid`, `asset_set_uuid`, `create_time`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`) 
VALUES 
('310', 'ec0f6223-55a7-4eea-bbc2-3e40bf081ab0', '90d22404-99ea-4b5a-872c-5ffc6c7909fd', '2016-09-12 17:10:53', '2016-09-12 17:10:53', 'FST_RECIEVABLE_ASSET', '20000', 'SND_RECIEVABLE_OVERDUE_FEE', '20000.06', 'TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION', '20000.06.01', '100.00');
