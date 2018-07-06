SET FOREIGN_KEY_CHECKS=0;

delete from settlement_order;
delete from asset_set;
delete from contract;
delete from app;
delete from financial_contract;
delete from asset_package;

INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repayment_term`,`adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`) 
VALUES ('1', NULL, '3', NULL, 'YX_AMT_001', NULL, '1', '1','5', '30', NULL, NULL, '0', '0', '0', '0', NULL, NULL);

INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`)
VALUES ('7101', '2', '0', NULL, '0.00', '1200.00', '1200.00', '2016-05-17', NULL, '0.00', '0', '1', '0', '2016-11-04 03:00:10', '18b302ac-281d-4b39-b906-cded34b42b58', '2016-05-27 18:27:15', '2016-11-04 03:00:10', '265', 'DKHD-001-01', '1687', NULL, '1', '2', '2016-07-28', '1', '0', '0', 'repurchasing', '0', '2d380fe1-7157-490d-9474-12c5a9901e29', NULL, NULL, NULL, NULL);

INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, `interest_rate_cycle`)
VALUES ('1687', NULL, NULL, '2016-03-08', 'DKHD-001', '2016-10-19', '0', '1000.00', '1', '1', '1', NULL, '2015-10-19 13:34:35', '0.0010000000', '1', '1', '12', '0', '10000.00', '0.0001000000', '1', NULL, '2', NULL, '0');

INSERT INTO `settlement_order` (`id`, `asset_set_id`, `settle_order_no`, `guarantee_order_no`, `due_date`, `overdue_days`, `overdue_penalty`, `create_time`, `last_modify_time`, `settlement_amount`,`comment`)
VALUES ('1', '7101', 'qs123456', 'bc123456', '2016-04-18', '0', '0.00', NULL, '2016-04-13 16:45:00', '0.00',  NULL);

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`)
VALUES 
('1', 'anmeitu', '11111db75ebb24fa0993f4fa25775023', '\0', 'http://e.zufangbao.cn', '安美途', '1', NULL);

INSERT INTO `asset_package` (`id`, `is_available`, `create_time`, `contract_id`, `asset_package_no`, `financial_contract_id`, `loan_batch_id`) 
VALUES ('1', NULL, NULL, '1687', NULL, '1', '1');

SET FOREIGN_KEY_CHECKS=1;
