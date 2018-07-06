SET FOREIGN_KEY_CHECKS=0;
delete from t_deduct_application;
delete from financial_contract;
delete from t_deduct_plan;



INSERT INTO `t_deduct_application` (`id`, `deduct_application_uuid`, `deduct_id`, `request_no`, `financial_contract_uuid`, `financial_product_code`, `contract_unique_id`, `repayment_plan_code_list`, `contract_no`, `planned_deduct_total_amount`, `actual_deduct_total_amount`, `notify_url`, `transcation_type`, `repayment_type`, `execution_status`, `execution_remark`, `create_time`, `creator_name`, `ip`, `last_modified_time`, `record_status`, `is_available`, `api_called_time`, `transaction_recipient`, `customer_name`, `mobile`, `gateway`, `third_part_voucher_status`)
VALUES
	(350, 'bb5e3010-0649-4234-8cfc-de7f913d0bbd', 'b16afa35-b8e2-4de0-ac55-55bdab9bc5d5', 'e50a1d0f-0f5c-4ddb-87f9-d6a7519e0ec1', 'd2812bc5-5057-4a91-b3fd-9019506f0499', 'G31700', '1c7f58c3-d724-43f3-aba6-ae18d243e443', '[\"ZC27561A5DCB1B70BF\",\"ZC27561A5DCB207334\"]', 'yq-17-0b91787f-d258-451f-8eba-dc4b973e8b370', 60.00, 60.00, '', 1, 2, 2, '交易成功', '2016-11-21 17:40:43', 't_test_zfb', '127.0.0.1', '2016-11-21 17:41:38', 0, 0, '2016-11-19 00:00:00', 1, '测试员', '13777847783', '', NULL);
	

	INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`,`adva_repayment_term`)
VALUES
	(38, 0, 1, '2016-09-01 00:00:00', 'G31700', '拍拍贷测试', 3, 1, 60, '2017-08-31 00:00:00', 102, 0, 1, 2, 1, '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', 'd2812bc5-5057-4a91-b3fd-9019506f0499', 0, 0, 0, 0, 1, 0,1);

	
	
	INSERT INTO `t_deduct_plan` (`id`, `deduct_plan_uuid`, `deduct_application_uuid`, `deduct_application_detail_uuid`, `financial_contract_uuid`, `contract_unique_id`, `contract_no`, `payment_gateway`, `payment_channel_uuid`, `pg_account`, `pg_clearing_account`, `transaction_type`, `transaction_remark`, `cp_bank_code`, `cp_bank_card_no`, `cp_bank_account_holder`, `cp_id_type`, `cp_id_number`, `cp_bank_province`, `cp_bank_city`, `cp_bank_name`, `planned_payment_date`, `complete_payment_date`, `planned_total_amount`, `actual_total_amount`, `execution_precond`, `execution_status`, `execution_remark`, `create_time`, `creator_name`, `last_modified_time`, `transaction_serial_no`, `mobile`, `transaction_recipient`, `trade_uuid`, `repayment_type`)
VALUES
	(420, '25989d18-d7dd-44d6-a526-aebd1009400e', 'bb5e3010-0649-4234-8cfc-de7f913d0bbd', NULL, 'd2812bc5-5057-4a91-b3fd-9019506f0499', '1c7f58c3-d724-43f3-aba6-ae18d243e443', 'yq-17-0b91787f-d258-451f-8eba-dc4b973e8b370', 0, 'f1ccca57-7c80-4429-b226-8ad31a729609', NULL, NULL, 1, '', 'C10102', '00000016090', '测试员', 0, '330683199403062411', '330000', '110100', '中国工商银行 ', NULL, '2016-11-21 17:39:24', 60.00, 60.00, NULL, 2, '交易?成功', '2016-11-21 17:40:58', 't_test_zfb', '2016-11-21 17:41:38', NULL, '13777847783', 1, 'lvl9Qyu13NrQVvUJ1p4', 2),
	(419, 'dae796d0-fa17-4584-ad43-7c1cbb39c136', 'bb5e3010-0649-4234-8cfc-de7f913d0bbd', NULL, 'd2812bc5-5057-4a91-b3fd-9019506f0499', '1c7f58c3-d724-43f3-aba6-ae18d243e443', 'yq-17-0b91787f-d258-451f-8eba-dc4b973e8b370', 3, '342c1ff6-0684-4422-a58e-e135aefe468e', NULL, NULL, 1, NULL, 'C10102', '00000016090', '测试员', 0, '330683199403062411', '330000', '110100', '中国工商银行 ', NULL, NULL, 60.00, 0.00, NULL, 3, '报文交易要素格式错误', '2016-11-21 17:40:43', 't_test_zfb', '2016-11-21 17:40:58', NULL, '13777847783', 1, '8TN3TEeruEZMWl5MXmg', 2),
	(404, 'e3829417-1e48-492a-a75f-c68b598fd1fd', 'db22bd49-f882-4be4-8b93-15a0c649cf8b', NULL, 'asdasdas', '1b29cade-96d9-47a9-a0ee-01f2c865c7f8', 'test-23456890234567890sds-bd5c70cf-c732-4b36-921a-671643dc10da0', 0, 'f1ccca57-7c80-4429-b226-8ad31a729609', NULL, NULL, 1, '', 'C10102', '6228480444455553300', '5ae8501f-50a9-4df3-bfd9-e6a460084d52', 0, '330683199403062411', '330000', '110100', '中国工商银行 ', NULL, '2016-11-01 23:29:51', 1.00, 1.00, NULL, 2, '交易?成功', '2016-11-01 23:30:39', 't_test_zfb', '2016-11-01 23:30:59', NULL, '13777847783', 1, 'YwzwFq6s5gBpVVEAtUZ', 0);

	


SET FOREIGN_KEY_CHECKS=1;