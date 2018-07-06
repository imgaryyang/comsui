SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM `financial_contract`;

DELETE FROM `offline_bill`;

INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`, `day_of_month`, `pay_for_go`, `repurchase_principal_algorithm`, `repurchase_interest_algorithm`, `repurchase_penalty_algorithm`, `repurchase_other_charges_algorithm`, `repayment_check_days`, `repurchase_cycle`, `days_of_cycle`, `temporary_repurchases`)
VALUES
  ('31', '1', '3', '1900-01-01 00:00:00', 'G00000', '测试信托合同名称0724', '1', '1', '91', '2900-01-01 00:00:00', '22', '0',
                                                                                                                 '1',
                                                                                                                 '90',
                                                                                                                 '2',
                                                                                                                 '95efeb8f-9e6f-413c-812c-765e96266851',
                                                                                                                 'd84e2927-839e-4162-9af1-e648e15bbf59',
                                                                                                                 '0',
                                                                                                                 '0',
                                                                                                                 '0',
                                                                                                                 '0',
    '0', '0', NULL, NULL, NULL, NULL, '0', '0', '0', '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
   '0', NULL, NULL, NULL, NULL, '-1', NULL, NULL, '0');

INSERT INTO `offline_bill` (`id`, `amount`, `bank_show_name`, `comment`, `create_time`, `trade_time`, `is_delete`, `status_modified_time`, `last_modified_time`, `offline_bill_no`, `offline_bill_status`, `offline_bill_uuid`, `serial_no`, `payer_account_name`, `payer_account_no`, `financial_contract_uuid`)
VALUES ('2', '10.12', '款方开户行', 'comment', '2016-05-31 15:05:23', '2016-04-25 04:20:00', b'0', '2016-05-31 15:05:23',
             '2016-05-31 15:05:23', 'XX273138F8CE27B3A7', '1', '6b8e03e450ae4ab3b9e10edd3df48f67', '支付机构流水号', '款方名称',
        '120003', '2d380fe1-7157-490d-9474-12c5a9901e29');

SET FOREIGN_KEY_CHECKS = 1;