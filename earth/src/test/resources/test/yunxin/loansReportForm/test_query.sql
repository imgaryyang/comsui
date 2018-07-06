
delete from `financial_contract`;



INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`)
VALUES
(5, 1, 3, '2016-04-01 00:00:00', 'G08200', '测试合同', 1, 1, 91, '2016-07-01 00:00:00', 5, 0, 1, 90, 2, '74a9ce4d-cafc-407d-b013-987077541bdc', '2d380fe1-7157-490d-9474-12c5a9901e29', 1, 1, 1, 1, 0, 1, NULL, NULL, NULL, NULL, 1, 0, 0, 0, 'N*N', 123.00, 23.00, NULL, NULL, '2016-12-08 22:46:27'),
(15, 1, 3, '2016-04-14 00:00:00', 'ceshi003', 'test006', 1, 1, 91, '2016-06-25 00:00:00', 16, 0, 1, 90, 1, 'c374dc6b-132d-4260-86be-534b153a7464', '92846f20-87e3-49f4-8f90-fe04a72c0484', 0, 0, 0, 0, 0, 0, NULL, NULL, 2, '[\"dd621204-656d-417a-8e2d-aa3d6d68f74d\",\"36ed38cc-16e9-4d42-960b-20a985e9950f\"]', 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, '2016-11-22 22:34:41'),
(21, 1, 3, '2016-04-01 00:00:00', 'G00000', '测试信托合同名称0724', 1, 1, 91, '2900-01-01 00:00:00', 22, 0, 1, 90, 2, '95efeb8f-9e6f-413c-812c-765e96266851', 'd84e2927-839e-4162-9af1-e648e15bbf59', 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL),
(22, 1, 5, '2016-04-01 00:00:00', 'zabx20160801', '众安保险', 1, 1, 91, '2019-08-01 00:00:00', 23, 0, 4, 90, 1, '293094ed-9644-4312-ac97-00b9504435c0', '930f1d3d-8158-420e-89bd-6f3922395eae', 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, 0, 0, 0, 3, NULL, NULL, NULL, NULL, NULL, NULL);
