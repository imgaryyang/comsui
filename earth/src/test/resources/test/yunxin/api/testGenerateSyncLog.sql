SET FOREIGN_KEY_CHECKS=0;
delete from `asset_set`;
delete from `financial_contract`;
delete from `rent_order`;

INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`) 
VALUES 
('18', '0', '0', '2406.00', '0.00', '2400.00', '2400.00', '2016-05-18', '2016-05-27', '0.00', '1', '2', '0', '2016-05-27 18:28:01', '90d22404-99ea-4b5a-872c-5ffc6c7909fd', '2016-05-27 18:27:17', '2016-05-27 18:30:23', NULL, 'ZC2730FAE40B0F2478', '24', '2016-05-27 18:21:04', '1', '0', NULL, '1', '0', '0', 'empty_deduct_uuid');


INSERT INTO  `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`) 
VALUES 
('5', '1', '3', '2016-04-01 00:00:00', '2016-78-DK(ZQ2016042522537)', '测试合同', '1', '1', '91', '2016-07-01 00:00:00', '5', '0', '5', '90', '2', '74a9ce4d-cafc-407d-b013-987077541bdc', '2d380fe1-7157-490d-9474-12c5a9901e29', '0', '0', '0', '0', '0', '0');


INSERT INTO `rent_order` (`id`, `order_type`, `due_date`, `order_no`, `payout_time`, `total_rent`, `customer_id`, `user_upload_param`, `modify_time`, `financial_contract_id`, `repayment_bill_id`, `asset_set_id`, `create_time`, `clearing_status`, `executing_settling_status`, `comment`) 
VALUES ('1', '0', '2016-05-27', 'JS2730FAE57E8F1E67', NULL, '1203.60', '1', '[1,2,3,4,5,6,7,8,9,10,11]', '2016-05-27 18:29:50', '5', '4dee1d4bce2742d19480e236585f2b46', '1', '2016-05-27 18:28:12', '0', '1', NULL);

SET FOREIGN_KEY_CHECKS=1;