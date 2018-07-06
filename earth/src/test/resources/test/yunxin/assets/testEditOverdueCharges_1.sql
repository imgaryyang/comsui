delete from `t_interface_modfify_overdue_fee_log`;
delete from `asset_set`;
delete from `financial_contract`;
delete from `contract`;
delete from `ledger_book`;
delete from `company`;


INSERT INTO  `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`,
						  `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, 
						  `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, 
						  `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`,
						  `active_status`, `repayment_plan_type`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, 
						  `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, 
						  `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, 
						  `contract_funding_status`) 
				  VALUES ('6223', '0', '0', '3999.00', '3999.00', '0.00', 
				  		  '3999.00', '2016-11-27', '2016-11-30', '0.00', '0', '2',
				  		  '2016-11-30 18:01:44', '5a0017c2-7521-4640-aebe-5bfdc1708d8c', '2016-11-30 18:01:44', '2016-11-30 18:05:42', NULL, 'ZC2756AABCC69DCDCG', 
				  		  '3181',now(), '1', '1', NULL, '1', 
				  		  '0', '0', '0', 'empty_deduct_uuid', NULL, 'beb90aa6-5cba-4535-b783-57f0801ed7c0', 
				  		  '8913875a21996d42aaaf5714f5e4b863', '00bfd64b58361d989ac8bf13dccc3c9b', '2016-11-30 18:01:44', '2016-11-30 18:01:44', '0', '0', 
				  		  '0', '1', '0', '2', NULL, '01836679-1ade-4551-b43b-d7ad8e3683fc', 
				  		  '4e69ef71-276a-43a1-8424-6628723e133b', '0');

				  		  
INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, 
								  `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, 
								  `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, 
								  `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, 
								  `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, 
								  `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, 
								  `last_modified_time`)
						  VALUES ('1', '1', '3', '2016-04-01 00:00:00', 'G08200', '测试合同',
						  		  '1', '1', '91', '2016-07-01 00:00:00', '5', '0', 
						  		  '2', '90', NULL, '74a9ce4d-cafc-407d-b013-987077541bdc', 'beb90aa6-5cba-4535-b783-57f0801ed7c0', '1', 
						  		  '1', '1', '1', '0', '1', NULL, 
						  		  NULL, NULL, NULL, '0', '0', '0', 
						  		  '0', NULL, NULL, NULL, NULL, NULL, 
						  		  NULL);

INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, 
						`asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, 
						`create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, 
						`total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, 
						`interest_rate_cycle`, `customer_uuid`) 
				VALUES ('3181', '4e69ef71-276a-43a1-8424-6628723e133b', '9b6f4593-6424-4f84-9521-255c25424504', '2016-10-01', 'beb90aa6-5cba-4535-b783-57f0801ed7c0', '2099-01-01', 
						NULL, '0.00', '1', '3181', '3992', NULL,
						'2016-11-30 18:01:44', '0.1560000000', '0', '0', '2', '1', 
						'4000.00', '0.0005000000', '1', NULL, '2', 'beb90aa6-5cba-4535-b783-57f0801ed7c0',
						'1', '01836679-1ade-4551-b43b-d7ad8e3683fc');

INSERT INTO  `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`) 
				 VALUES ('3181', '330683199403062411', NULL, '测试员1', '123456', '1', 'beb90aa6-5cba-4535-b783-57f0801ed7c0', '0');

INSERT INTO  `company` (`id`, `address`, `full_name`, `short_name`, `uuid`)
                VALUES ('1', '上海', '测试金融公司', '测试金融', 'c3e6b592-6f9c-11e6-b776-58d41e171d4a');
						  		  
INSERT INTO `ledger_book` (`id`, `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`) 
                   VALUES ('3181', '74a9ce4d-cafc-407d-b013-987077541bdc', '1', NULL, NULL);



