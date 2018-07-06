delete from `offline_bill`;
delete from `financial_contract`;


INSERT INTO `offline_bill` (`id`, `amount`, `bank_show_name`, `comment`, `create_time`, `trade_time`, `is_delete`, `status_modified_time`, `last_modified_time`, `offline_bill_no`, `offline_bill_status`, `offline_bill_uuid`, `serial_no`, `payer_account_name`, `payer_account_no`, `financial_contract_uuid`)
VALUES
(1, 8900.00, 'nibaa', 'fghjkl', '2016-05-28', '2016-05-04', 0, '2016-05-28 21:13:08', '2016-05-28 21:13:08', 'XX27310C98501C416F', 1, '97fc01a6117640f38730033265f60766', '234567890', 'nimaa', '34567890-', '2d380fe1-7157-490d-9474-12c5a9901e29'),
(2, 10.12, '款方开户行', 'comment', '2016-05-28', '2016-05-04', 0, '2016-05-31 15:05:23', '2016-05-31 15:05:23', 'XX273138F8CE27B3A7', 1, '6b8e03e450ae4ab3b9e10edd3df48f67', '支付机构流水号', '款方名称', '120003', '2d380fe1-7157-490d-9474-12c5a9901e29');


INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`)
VALUES
(5, 1, 3, '2016-04-01 00:00:00', 'G08200', '测试合同', 1, 1, 91, '2016-07-01 00:00:00', 5, 0, 1, 90, 2, '74a9ce4d-cafc-407d-b013-987077541bdc', '2d380fe1-7157-490d-9474-12c5a9901e29', 1, 1, 1, 1, 0, 1, NULL, NULL, NULL, NULL, 1, 0, 0, 0, 'N*N', 123.00, 23.00, NULL, NULL, '2016-12-08 22:46:27');
