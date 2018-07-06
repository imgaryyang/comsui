delete from `extra_charge_snap_shot`;
delete from `asset_set`;

INSERT INTO  `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`,
						  `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, 
						  `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, 
						  `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`,
						  `active_status`, `repayment_plan_type`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, 
						  `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, 
						  `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, 
						  `contract_funding_status`) 
				  VALUES ('6221', '0', '0', '3999.00', '3999.00', '0.00', 
				  		  '3999.00', '2016-11-27', '2016-11-30', '0.00', '1', '2',
				  		  '2016-11-30 18:01:44', '7b834649-22e6-4815-b4ec-f3fbe5fc61c7', '2016-11-30 18:01:44', '2016-11-30 18:05:42', NULL, 'ZC2756AABCC69DCDCF', 
				  		  '3181', '2016-11-30 18:04:36', '1', '1', NULL, '1', 
				  		  '0', '0', '0', '31d83464-b5b7-4b87-9447-60545bbab140', NULL, 'beb90aa6-5cba-4535-b783-57f0801ed7c0', 
				  		  '8913875a21996d42aaaf5714f5e4b863', '00bfd64b58361d989ac8bf13dccc3c9b', '2016-11-30 18:01:44', '2016-11-30 18:01:44', '0', '0', 
				  		  '0', '1', '0', '2', NULL, '01836679-1ade-4551-b43b-d7ad8e3683fc', 
				  		  '4e69ef71-276a-43a1-8424-6628723e133b', '0');

INSERT INTO `extra_charge_snap_shot` (`id`, `asset_set_uuid`, `create_time`, `overdue_fee_penalty`, `overdue_fee_obligation`, `overdue_fee_service`, `overdue_fee_other`, `data`, `repayment_plan_no`) 
				              VALUES ('651', '7b834649-22e6-4815-b4ec-f3fbe5fc61c7', '2017-01-17 16:22:15', '1.00', '1.00', '1.00', '1.00', NULL, 'ZC2756AABCC69DCDCF'),
		   				 			 ('652', '7b834649-22e6-4815-b4ec-f3fbe5fc61c7', '2017-01-17 16:22:15', '2.00', '2.00', '2.00', '2.00', NULL, 'ZC2756AABCC69DCDCF'),
          				             ('653', '7b834649-22e6-4815-b4ec-f3fbe5fc61c7', '2017-01-17 16:22:16', '3.00', '3.00', '3.00', '3.00', NULL, 'ZC2756AABCC69DCDCF'),
           				             ('654', '7b834649-22e6-4815-b4ec-f3fbe5fc61c7', '2017-01-17 16:22:16', '4.00', '4.00', '4.00', '4.00', NULL, 'ZC2756AABCC69DCDCF'),
           				             ('655', '7b834649-22e6-4815-b4ec-f3fbe5fc61c7', '2017-01-17 16:22:17', '5.00', '5.00', '5.00', '5.00', NULL, 'ZC2756AABCC69DCDCF');



