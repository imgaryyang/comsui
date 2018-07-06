SET FOREIGN_KEY_CHECKS=0;

delete from `financial_contract`;
delete from `loan_batch`;

INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`,
								  `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, 
								  `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`,
								  `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, 
								  `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, 
								  `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, 
								  `last_modified_time`) 
						  VALUES ('5', '1', '3', '2016-04-01 00:00:00', 'G08200', '测试合同', 
						  		  '1', '1', '91', '2016-07-01 00:00:00', '5', '0', 
						  		  '5', '90', '2', '74a9ce4d-cafc-407d-b013-987077541bdc', '2d380fe1-7157-490d-9474-12c5a9901e29', '1', 
						  		  '1', '1', '1', '0', '0', NULL, 
						  		  NULL, NULL, NULL, '0', '0', '0', 
						  		  '0', NULL, NULL, NULL, NULL, NULL, 
						  		  NULL);
						  		  
SET FOREIGN_KEY_CHECKS=1;