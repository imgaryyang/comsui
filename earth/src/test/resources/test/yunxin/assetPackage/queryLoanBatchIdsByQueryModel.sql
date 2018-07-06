
SET FOREIGN_KEY_CHECKS=0;

delete from `loan_batch`;
delete from `financial_contract`;

INSERT INTO `loan_batch` (`id`, `is_available`, `code`, `create_time`, `financial_contract_id`, `last_modified_time`,
						  `loan_date`, `loan_batch_uuid`) 
				  VALUES ('1',false, 'nfqtest001 20160527 18:27:477', '2016-05-27 18:27:16', '5','2016-06-16 15:45:54',
			              '2016-06-16 15:45:54', '0fdd0eac-16e7-4045-8d32-e4d1fe06772e');

INSERT INTO `loan_batch` (`id`, `is_available`, `code`, `create_time`, `financial_contract_id`, `last_modified_time`, 
						  `loan_date`, `loan_batch_uuid`) 
				  VALUES ('64', false, 'nfq003 20160718 22:57:156', '2016-07-18 22:57:48', '15', '2016-07-18 23:27:57',
				          '2016-07-18 23:27:57', 'ab4f1bc9-9eb4-4c45-94e0-eb3af3b92d42');
				          
INSERT INTO `loan_batch` (`id`, `is_available`, `code`, `create_time`, `financial_contract_id`, `last_modified_time`,
						  `loan_date`, `loan_batch_uuid`) 
				  VALUES ('2',false, 'nfqtest001 20160527 18:27:477', '2016-05-27 18:27:16', '10','2016-06-16 15:45:54',
			              '2016-06-16 15:45:54', '0fdd0eac-16e7-4045-8d32-e4d1fe06772e');

INSERT INTO `loan_batch` (`id`, `is_available`, `code`, `create_time`, `financial_contract_id`, `last_modified_time`, 
						  `loan_date`, `loan_batch_uuid`) 
				  VALUES ('3', false, 'nfq003 20160718 22:57:156', '2016-07-18 22:57:48', '11', '2016-07-18 23:27:57',
				          '2016-07-18 23:27:57', 'ab4f1bc9-9eb4-4c45-94e0-eb3af3b92d42');

				          
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

INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`,
								  `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, 
								  `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, 
								  `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, 
								  `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, 
								  `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`) 
						  VALUES ('15', '1', '3', '2016-06-14 00:00:00', 'ceshi003', 'test006', 
						          '1', '1', '91', '2016-06-25 00:00:00', '16', '0',
						          '1', '90', '1', 'c374dc6b-132d-4260-86be-534b153a7464', '92846f20-87e3-49f4-8f90-fe04a72c0484', '0', 
						          '0', '0', '0', '0', '0', NULL, 
						          NULL, NULL, NULL, '0', '0', '0', 
						          '0', NULL, NULL, NULL, NULL, NULL, 
						          NULL);
						        
INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`,
								  `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, 
								  `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`,
								  `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, 
								  `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, 
								  `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, 
								  `last_modified_time`) 
						  VALUES ('10', '1', '3', '2016-04-01 00:00:00', 'G08200', '测试合同', 
						  		  '1', '1', '91', '2016-07-01 00:00:00', '5', '0', 
						  		  '5', '90', '2', '74a9ce4d-cafc-407d-b013-987077541bdc', '2d380fe1-7157-490d-9474-12c5a9901e30', '1', 
						  		  '1', '1', '1', '0', '0', NULL, 
						  		  NULL, NULL, NULL, '0', '0', '0', 
						  		  '0', NULL, NULL, NULL, NULL, NULL, 
						  		  NULL);

INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`,
								  `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, 
								  `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, 
								  `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, 
								  `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, 
								  `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`) 
						  VALUES ('11', '1', '3', '2016-06-14 00:00:00', 'ceshi003', 'test006', 
						          '1', '1', '91', '2016-06-25 00:00:00', '16', '0',
						          '1', '90', '1', 'c374dc6b-132d-4260-86be-534b153a7464', '92846f20-87e3-49f4-8f90-fe04a72c0431', '0', 
						          '0', '0', '0', '0', '0', NULL, 
						          NULL, NULL, NULL, '0', '0', '0', 
						          '0', NULL, NULL, NULL, NULL, NULL, 
						          NULL);
						  	
						  		  
SET FOREIGN_KEY_CHECKS=1;