
delete from `asset_set`;
delete from `t_deduct_application_detail`;




INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`)
VALUES
(20, 0, 0, 3007.50, 0.00, 3000.00, 3000.00, '2016-05-18', '2016-05-27', 0.00, 1, 2, 0, '2016-05-27 18:28:05', '6ad8f444-76e8-4b66-b3b7-cb19599603d5', '2016-05-27 18:27:17', '2016-05-27 18:37:37', NULL, 'ZC2730FAE40B3FD041', 20, '2016-05-17', 1, 0, NULL, 1, 0, 0, 'empty_deduct_uuid', NULL, '2d380fe1-7157-490d-9474-12c5a9901e29', NULL, NULL, NULL, NULL),
(401, 1, 0, 1328.40, 0.00, 1200.00, 1200.00, '2016-05-17', NULL, 0.00, 0, 1, 0, '2016-12-17 03:01:03', '106732f7-2235-44de-9da7-5520638748e1', '2016-06-02 20:12:45', '2016-12-17 03:01:03', NULL, 'ZC2735ABFEC2B8373B', 122, NULL, 1, 1, NULL, 1, 0, 0, '4c79be82-61f2-4af0-bf51-af5be590cbfc', NULL, '2d380fe1-7157-490d-9474-12c5a9901e29', NULL, NULL, NULL, NULL);


INSERT INTO `t_deduct_application_detail` (`id`, `deduct_application_detail_uuid`, `deduct_application_uuid`, `financial_contract_uuid`, `contract_unique_id`, `repayment_plan_code`, `asset_set_uuid`, `request_no`, `repayment_type`, `transaction_type`, `create_time`, `execution_remark`, `creator_name`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`, `is_total`)
VALUES
(1, '122881e1-1df1-4622-bc1d-da9f9587d667', '9d13d98f-6b5a-47f0-ae0c-537bd54afcc2', '63b7e281-d5f7-4f6e-9369-2a1d5ff8a148', NULL, 'ZC27438A3C7D00C129', '6ad8f444-76e8-4b66-b3b7-cb19599603d5', '2445ef09-f220-4e52-abe2-ec69aa6fbcaf', 0, 1, '2016-08-24 17:21:00', '', 't_test_zfb', '2016-08-24 17:21:00', 'FST_RECIEVABLE_ASSET', '20000', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_PRINCIPLE', '20000.01.01', 100.00, 1),
(10, '28dbaa73-35a7-4107-a354-b3c35f79446d', '4d5f042e-7030-4911-92b5-02a2e9a76b0f', 'db36ecc9-d80c-4350-bd0d-59b1139d550d', '4c05b1ea-fc25-47eb-9c76-dcabd0271e1e', 'ZC27439DCCCA4A391A','106732f7-2235-44de-9da7-5520638748e1', 'c906d969-4654-4972-b284-40cb93331ade', 0, 1, '2016-08-26 00:03:58', '', 't_merchant', '2016-08-26 00:03:58', 'FST_RECIEVABLE_ASSET', '20000', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_TECH_FEE', '20000.01.04', 0.00, 1);
