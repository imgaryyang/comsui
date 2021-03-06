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
delete from ledger_book;

INSERT INTO `ledger_book` (`id`, `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`) VALUES 
(1, 'ledger_book_no', 1, 1, NULL);

INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `financial_contract_uuid`,`ledger_book_no`,sys_normal_deduct_flag,sys_overdue_deduct_flag,sys_create_penalty_flag,sys_create_guarantee_flag,unusual_modify_flag,sys_create_statement_flag,adva_repayment_term)
VALUES
	(2, 1, 7, '2015-04-02 00:00:00', '', '农分期', 2, 1, 0, '9999-01-01 00:00:00', 2, 1, 0, 0, 2,'uuid11','ledger_book_no',1,1,1,1,1,1,1);

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`)
VALUES
	(2, 'nongfenqi', '11111db75ebb24fa0993f4fa25775023', 1, 'http://e.zufangbao.cn', '农分期', 5, NULL);

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`)
VALUES
	(1, '上海', '云南国际信托有限公司', '云南信托'),
	(5, '杭州', '农分期', '农分期');

SET FOREIGN_KEY_CHECKS=1;