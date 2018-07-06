SET FOREIGN_KEY_CHECKS=0;

delete from financial_contract;
delete from app;
delete from company;
delete from contract;
delete from asset_package;
delete from customer;
delete from contract_account;
delete from asset_set;
delete from house;
delete from bank;
delete from province;
delete from city;
delete from ledger_book;
INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`, `day_of_month`, `pay_for_go`, `repurchase_principal_algorithm`, `repurchase_interest_algorithm`, `repurchase_penalty_algorithm`, `repurchase_other_charges_algorithm`, `temporary_repurchases`, `repurchase_cycle`, `allow_freewheeling_repayment`, `days_of_cycle`, `repayment_check_days`)
VALUES ('1', '0', '3', '2016-05-17 00:00:00', 'G32000', '用钱宝测试', '1', '1', '30', '2099-12-01 00:00:00', '1', '0', '0', '0', '0', 'e17d8068-46a0-48bb-b158-8e80168bbfc3', 'b674a323-0c30-4e4b-9eba-b14e05a9d80a', '0', '0', '0', '0', '0', '0', NULL, NULL, NULL, NULL, '0', '0', '0', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, '-1');

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`)
VALUES
	(1, 'nongfenqi', '11111db75ebb24fa0993f4fa25775023', 1, 'http://e.zufangbao.cn', '农分期', 5, NULL);

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`)
VALUES
	(1, '上海', '云南国际信托有限公司', '云南信托'),
	(5, '杭州', '农分期', '农分期');

INSERT INTO `bank` (`id`, `bank_code`, `bank_name`)
VALUES
	(10, 'C10102', '中国工商银行 ');
INSERT INTO `province` (`id`, `code`, `is_deleted`, `name`)
VALUES
	(1, '330000', 00000000, '浙江省');
INSERT INTO `city` (`id`, `code`, `is_deleted`, `name`, `province_code`)
VALUES
	(89, '330300', 00000000, '温州市', '330000');

INSERT INTO `ledger_book` (`id`, `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`) VALUES ('37', 'e17d8068-46a0-48bb-b158-8e80168bbfc3', '1', NULL, NULL);

INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`,`financial_contract_uuid`,`contract_uuid`, `repay_schedule_no`, `outer_repayment_plan_no`)
VALUES
	(148660, 0, 0, 0, 80000.00, 10000.00, 90000.00, '2018-11-10', '2018-10-30', 0.00, 0, 1, 0, NULL, '890d41d9-2484-46bb-a856-e987ef1da40e', '2016-10-25 11:06:48', '2016-10-25 11:14:01', NULL, 'ZC275016985BF712EB', 54340, '2018-10-25 11:14:01', 1, 0, NULL, 1, 0, 0,'b674a323-0c30-4e4b-9eba-b14e05a9d80a','3e8711d4-9573-4965-a878-480ee4c1f5fc','56248960ff4820604ac87a246bd5a35d','outer1');

INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`)
VALUES
	(54340, '3e8711d4-9573-4965-a878-480ee4c1f5fc', 'e568793f-a44c-4362-9e78-0ce433131f3e', '2016-09-10', '云信信2016-241-DK(428522112675736881)', '2018-12-09', NULL, 0.00, 2, 54349, 54505, NULL, '2016-10-25 11:06:48', 0.8923000000, 0, 0, 2, 2, 4500.00, 0.0050000000, 1, NULL, 2, 'b674a323-0c30-4e4b-9eba-b14e05a9d80a');

SET FOREIGN_KEY_CHECKS=1;