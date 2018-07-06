DELETE FROM financial_contract;
DELETE FROM third_party_audit_bill;

INSERT INTO financial_contract (asset_package_format, adva_matuterm, adva_start_date, contract_no, contract_name, app_id, company_id, adva_repo_term, thru_date, capital_account_id, financial_contract_type, loan_overdue_start_day, loan_overdue_end_day, payment_channel_id, ledger_book_no, financial_contract_uuid, sys_normal_deduct_flag, sys_overdue_deduct_flag, sys_create_penalty_flag, sys_create_guarantee_flag, unusual_modify_flag, sys_create_statement_flag, transaction_limit_per_transcation, transaction_limit_per_day, remittance_strategy_mode, app_account_uuids, allow_online_repayment, allow_offline_repayment, allow_advance_deduct_flag, adva_repayment_term, penalty, overdue_default_fee, overdue_service_fee, overdue_other_fee, create_time, last_modified_time, repurchase_approach, repurchase_rule, repurchase_algorithm, day_of_month, pay_for_go, repurchase_principal_algorithm, repurchase_interest_algorithm, repurchase_penalty_algorithm, repurchase_other_charges_algorithm, repayment_check_days, repurchase_cycle, days_of_cycle, allow_freewheeling_repayment, temporary_repurchases, capital_party, asset_party, channel_party, supplier, other_party, contract_short_name, financial_type, remittance_object)
VALUES (1, 0, '2016-09-02 00:00:00', 'G32000', '云南信托普惠7号用钱宝单一资金信托', 2, 1, 42, '2018-10-26 00:00:00', 8, 0, 41, 41, NULL,
                                                                                                           '0cda835a-c41d-4f44-b192-88de57fdb8be',
                                                                                                           'b6b407fb-de54-4d84-9cba-84ccb2c17fcf',
                                                                                                           0, 0, 0, 0,
                                                                                                           1, 0, NULL,
                                                                                                              NULL,
                                                                                                              NULL,
                                                                                                              NULL, 1,
                                                                                                              1, 0, 40,
                                                                                                              '', NULL,
                                                                                                                  NULL,
                                                                                                                  NULL,
                                                                                                                  NULL,
                                                                                                                  '2017-10-27 17:01:22',
                                                                                                                  2,
                                                                                                                  NULL,
                                                                                                                  '',
                                                                                                                  NULL,
                                                                                                                  0, '',
                                                                                                                     '',
                                                                                                                     '',
                                                                                                                     '',
                                                                                                                     -1,
                                                                                                                     NULL,
                                                                                                                     NULL,
                                                                                                                     0,
                                                                                                                     NULL,
                                                                                                                     '[]',
        NULL, NULL, NULL, '[]', '用钱宝', 0, NULL);

INSERT INTO third_party_audit_bill (financial_contract_uuid, audit_bill_uuid, payment_gateway, merchant_no, snd_lvl_merchant_no, account_side, channel_sequence_no, merchant_order_no, settle_date, reckon_account, transaction_amount, service_fee, order_create_time, transaction_time, counter_account_no, counter_account_name, counter_account_appendix, remark, create_time, issued_amount, audit_status, date_field_one, date_field_two, date_field_three, long_field_one, long_field_two, long_field_three, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two, decimal_field_three, batchNo, batch_no)
VALUES ('b6b407fb-de54-4d84-9cba-84ccb2c17fcf', '5fa69846-41d8-42c0-a284-f1df43607bdf', 7, '002762', NULL, 1, NULL,
                                                '128113924349726720', '2017-11-15 00:00:00', '001', 1000.00, 0.00, NULL,
                                                                                                             '2017-12-11 16:49:27',
                                                                                                             NULL, NULL,
                                                                                                             NULL, '',
                                                                                                             '2017-11-16 01:03:00',
                                                                                                             0.00, 0,
  NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '11', '12');

