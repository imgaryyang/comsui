delete from `asset_set`;
delete from `rent_order`;

INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_Interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`) 
VALUES 
('1', '0', '0', '10000.00', '9000.00', '1000.00', '10000.00', '2017-02-14', NULL, NULL, NULL, NULL, '0', NULL, NULL, '2017-02-04 11:24:42', NULL, NULL, 'test_prepayment_1', '1', NULL, '1', '0', NULL, NULL, '0', '0', 'empty_deduct_uuid', NULL, NULL, NULL, NULL, NULL, NULL, '1', '0', '1', '0', '0', '0', NULL, NULL, NULL, NULL);

INSERT INTO `rent_order` (`id`, `order_type`, `due_date`, `order_no`, `payout_time`, `total_rent`, `customer_id`, `user_upload_param`, `modify_time`, `financial_contract_id`, `repayment_bill_id`, `asset_set_id`, `create_time`, `clearing_status`, `executing_settling_status`, `comment`, `charges_detail`, `order_source`) 
VALUES 
('1', '0', NOW(), 'test_order_1', NULL, NULL, '1', NULL, NULL, '1', NULL, '1', '2017-02-04 12:00:09', '0', '1', NULL, NULL, '0');
