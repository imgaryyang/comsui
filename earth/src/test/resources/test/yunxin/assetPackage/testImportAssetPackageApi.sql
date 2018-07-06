SET FOREIGN_KEY_CHECKS=0;

DELETE from t_interface_import_asset_package;
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
INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`, `day_of_month`, `pay_for_go`, `repurchase_principal_algorithm`, `repurchase_interest_algorithm`, `repurchase_penalty_algorithm`, `repurchase_other_charges_algorithm`, `temporary_repurchases`, `repurchase_cycle`, `allow_freewheeling_repayment`, `days_of_cycle`, `repayment_check_days`) VALUES ('1', '0', '3', '2016-05-17 00:00:00', 'G0000000', '用钱宝测试', '1', '1', '30', '2099-12-01 00:00:00', '1', '0', '0', '0', '0', 'e17d8068-46a0-48bb-b158-8e80168bbfc3', 'uuid', '0', '0', '0', '0', '0', '0', NULL, NULL, NULL, NULL, '0', '0', '0', '1', "", NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, '-1');

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

INSERT INTO `ledger_book` (`id`, `ledger_book_no`,
`ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`,
`ledger_book_version`) VALUES ('37', 'e17d8068-46a0-48bb-b158-8e80168bbfc3',
'1', NULL, NULL,'1213');

SET FOREIGN_KEY_CHECKS=1;