SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM journal_voucher;
DELETE FROM financial_contract;
DELETE from contract;
DELETE FROM asset_set;
DELETE FROM source_document;
DELETE FROM source_document_detail;
delete from t_tag;
DELETE from t_tag_identity_map;

INSERT INTO journal_voucher (account_side, bank_identity, billing_plan_uuid, booking_amount, business_voucher_type_uuid, business_voucher_uuid, cash_flow_amount, cash_flow_breif, cash_flow_channel_type, cash_flow_serial_no, cash_flow_uuid, checking_level, company_id, completeness, counter_party_account, counter_party_name, journal_voucher_uuid, notification_identity, notification_memo, notification_record_uuid, notified_date, settlement_modes, source_document_amount, source_document_breif, source_document_cash_flow_serial_no, source_document_counter_party_uuid, source_document_identity, source_document_uuid, status, trade_time, batch_uuid, created_date, source_document_counter_party_account, source_document_counter_party_name, issued_time, journal_voucher_type, counter_account_type, related_bill_contract_info_lv_1, related_bill_contract_info_lv_2, related_bill_contract_info_lv_3, cash_flow_account_info, journal_voucher_no, related_bill_contract_no_lv_1, related_bill_contract_no_lv_2, related_bill_contract_no_lv_3, related_bill_contract_no_lv_4, source_document_no, appendix, last_modified_time, local_party_account, local_party_name, source_document_local_party_account, source_document_local_party_name, second_journal_voucher_type, third_journal_voucher_type, is_has_data_sync_log)
VALUES
  (1, '中国建设银行 ', 'd4ba338c-cd4a-43c0-9eb6-867e0f31b7ea', 3010.00, 'repayment_order', NULL, NULL, NULL, 2, NULL, NULL, 0,
                                                                                                                      1,
                                                                                                                      NULL,
                                                                                                                      '6217001210075327590',
                                                                                                                      '韩方园',
                                                                                                                      '87398c82-2986-4ddf-ae57-a5ad957d15b2',
                                                                                                                      NULL,
                                                                                                                      NULL,
                                                                                                                      '',
                                                                                                                      NULL,
    NULL, 3010.00, NULL, NULL, NULL, 'e668556b-f978-4895-af74-8c7fe563ea19', '00002272-c9c5-487d-b3de-17382f3db44f', 1,
    '2017-11-10 07:41:17', NULL, '2016-11-26 07:43:31', '6217001210075327590', '韩方园', '2016-11-26 07:43:31', 7, 0,
                                 'd2812bc5-5057-4a91-b3fd-9019506f0499', '86b33f91-6641-490b-8af2-ccc4734d015f',
                                 'd4ba338c-cd4a-43c0-9eb6-867e0f31b7ea', 'a02c02b9-6f98-11e6-bf08-00163e002839',
    'bfaa501e-721b-4430-a73c-85d08e485749', '拍拍贷测试', '2016-236-DK(hk962322417393308756)号', 'ZC274FD6122A206BBB',
    'JS275651E6F639250E', 'KK2756646E3431A56A', NULL, '2017-11-15 07:43:31', '6217001210075327590', '韩方园',
   '6217001210075327590', '韩方园', 0, 2, 0);

INSERT INTO financial_contract (asset_package_format, adva_matuterm, adva_start_date, contract_no, contract_name, app_id, company_id, adva_repo_term, thru_date, capital_account_id, financial_contract_type, loan_overdue_start_day, loan_overdue_end_day, payment_channel_id, ledger_book_no, financial_contract_uuid, sys_normal_deduct_flag, sys_overdue_deduct_flag, sys_create_penalty_flag, sys_create_guarantee_flag, unusual_modify_flag, sys_create_statement_flag, transaction_limit_per_transcation, transaction_limit_per_day, remittance_strategy_mode, app_account_uuids, allow_online_repayment, allow_offline_repayment, allow_advance_deduct_flag, adva_repayment_term, penalty, overdue_default_fee, overdue_service_fee, overdue_other_fee, create_time, last_modified_time, repurchase_approach, repurchase_rule, repurchase_algorithm, day_of_month, pay_for_go, repurchase_principal_algorithm, repurchase_interest_algorithm, repurchase_penalty_algorithm, repurchase_other_charges_algorithm, repayment_check_days, repurchase_cycle, days_of_cycle, temporary_repurchases, allow_freewheeling_repayment, capital_party, other_party, contract_short_name, financial_type, remittance_object, asset_party, channel_party, supplier)
VALUES (3, 0, '2016-09-01 00:00:00', 'G31700', '拍拍贷测试', 3, 3, 33, '2019-12-31 00:00:00', 102, 0, 11, 32, 1,
                                                                                                 '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58',
                                                                                                 'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                                 0, 0, 1, 1, 1, 0, NULL,
                                                                                                                NULL, 0,
                                                                                                                'null',
                                                                                                                1, 1, 1,
                                                                                                                10, '',
  NULL, NULL, NULL, NULL, '2017-11-15 15:00:39', 1, 1,
  'outstandingPrincipal+outstandingInterest+outstandingPenaltyInterest', 6, 1, 'outstandingPrincipal',
                                                                               'outstandingInterest',
                                                                               'outstandingPenaltyInterest', '', 3,
                                                                               NULL, '[]', '[]', 1, '[1]', '[]',
        'G31700', 0, 0, NULL, NULL, NULL);

INSERT INTO contract (uuid, unique_id, begin_date, contract_no, end_date, asset_type, month_fee, app_id, customer_id, house_id, actual_end_date, create_time, interest_rate, payment_day_in_month, payment_frequency, periods, repayment_way, total_amount, penalty_interest, active_version_no, repayment_plan_operate_logs, state, financial_contract_uuid, interest_rate_cycle, customer_uuid, repaymented_periods, completion_status, date_field_one, long_field_one, long_field_two, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two)
VALUES ('86b33f91-6641-490b-8af2-ccc4734d015f', '882067cd-7d85-46f7-b7f6-37cdfe2ad679', '2016-10-11',
                                                '2016-236-DK(hk962322417393308756)号', '2019-01-01', NULL, 0.00, 3,
                                                53234, 53390, NULL, '2016-10-21 10:18:53', 0.1100000000, 0, 0, 3, 2,
                                                                    10000.00, 0.2240000000, 1, NULL, 2,
                                                                                                     'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                                     0,
                                                                                                     '4bca56c1-7596-41bf-b867-6da3b3da5119',
                                                                                                     0, NULL, NULL,
                                                                                                     NULL, NULL, NULL,
        NULL, NULL, NULL, NULL);
INSERT INTO asset_set (guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES (0, 0, 3010.00, 3000.00, 1.00, 3001.00, '2016-10-30', '2016-11-26', 0.00, 1, 2, 0, '2016-11-26 03:11:05',
                                                                                       'd4ba338c-cd4a-43c0-9eb6-867e0f31b7ea',
                                                                                       '2016-10-21 10:18:53',
                                                                                       '2016-11-26 07:43:31', NULL,
                                                                                       'ZC274FD6122A206BBB', 53225,
                                                                                       '2016-11-26 07:41:17', 1, 1,
                                                                                                                 NULL,
                                                                                                                 1, 0,
                                                                                                                 0,
                                                                                                                 '0fe032d5-e559-4dc9-8e24-c94bbc6a63fe',
                                                                                                                 NULL,
                                                                                                                 'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                                                 NULL,
                                                                                                                 NULL,
  NULL, NULL, 0, 0, 0, 1, 0, 2, NULL, '4bca56c1-7596-41bf-b867-6da3b3da5119', '86b33f91-6641-490b-8af2-ccc4734d015f', 0,
        '29f29890-6650-11e7-bff1-00163e002839', 0, NULL, NULL);
INSERT INTO source_document_detail (uuid, source_document_uuid, contract_unique_id, repayment_plan_no, amount, status, first_type, first_no, second_type, second_no, payer, receivable_account_no, payment_account_no, payment_name, payment_bank, check_state, comment, financial_contract_uuid, principal, interest, service_charge, maintenance_charge, other_charge, penalty_fee, late_penalty, late_fee, late_other_cost, voucher_uuid, actual_payment_time, repay_schedule_no, current_period, outer_repayment_plan_no) VALUES ('00002272-c9c5-487d-b3de-17382f3db44f', 'e668556b-f978-4895-af74-8c7fe563ea19', '882067cd-7d85-46f7-b7f6-37cdfe2ad679', 'ZC274FD6122A206BBB', 3010.00, 1, 'enum.voucher-source.third-party-deduction-voucher', '0fe032d5-e559-4dc9-8e24-c94bbc6a63fe', 'enum.voucher-type.third_party_deduction_voucher', 'e4a28f74-b8ae-448a-b65a-8a0ae46f3d2b', 0, '600000000001', '6217001210075327590', '韩方园', '中国建设银行 ', 0, null, 'd2812bc5-5057-4a91-b3fd-9019506f0499', 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, null, null, null, null, null);
INSERT INTO source_document (company_id, source_document_uuid, source_document_type, create_time, issued_time, source_document_status, source_account_side, booking_amount, outlier_document_uuid, outlier_trade_time, outlier_counter_party_account, outlier_counter_party_name, outlier_account, outlie_account_name, outlier_account_id, outlier_company_id, outlier_serial_global_identity, outlier_memo, outlier_amount, outlier_settlement_modes, outlier_breif, outlier_account_side, appendix, first_outlier_doc_type, second_outlier_doc_type, third_outlier_doc_type, currency_type, audit_status, first_party_id, second_party_id, virtual_account_uuid, first_account_id, second_account_id, third_account_id, excute_status, excute_result, related_contract_uuid, financial_contract_uuid, source_document_no, first_party_type, first_party_name, virtual_account_no, last_modified_time, voucher_uuid, plan_booking_amount) VALUES (1, 'e668556b-f978-4895-af74-8c7fe563ea19', 1, '2016-11-26 07:43:31', null, 1, 1, 3010.00, '0fe032d5-e559-4dc9-8e24-c94bbc6a63fe', '2016-11-26 07:41:17', '6217001210075327590', '韩方园', '600000000001', '云南信托国际有限公司', 102, 1, 'e4a28f74-b8ae-448a-b65a-8a0ae46f3d2b', '交易成功', 3010.00, 3, '["ZC274FD6122A206BBB"]', 1, null, 'deduct_application', 'system_on_line_payment', null, null, 0, 'a02c02b9-6f98-11e6-bf08-00163e002839', '4bca56c1-7596-41bf-b867-6da3b3da5119', null, '0fe032d5-e559-4dc9-8e24-c94bbc6a63fe', '0f7f38f3-db44-42be-88b4-7166d16f1391', null, null, null, '86b33f91-6641-490b-8af2-ccc4734d015f', 'd2812bc5-5057-4a91-b3fd-9019506f0499', 'KK2756646E3431A56A', null, '测试金融', null, '2016-11-26 07:43:31', null, 0.00);
INSERT INTO t_tag_identity_map (uuid, tag_uuid, outer_identifier, financial_contract_uuid, contract_uuid, status, type, create_time, last_modified_time, date_field_one, date_field_two, date_field_three, long_field_one, long_field_two, long_field_three, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two, decimal_field_three)
VALUES ('6098b6f0-88fa-4185-beef-a9e687f83aac', '1346b7cb-0823-4b88-9132-7307d989d793', 'ZC274FD6122A206BBB',
                                                '5bfede8e-d08f-4544-94c6-11e33bf7b3c2',
                                                '15bc3a44-eebc-4664-aad0-d7575d4b1f0a', 0, 0, '2017-09-11 14:20:44',
                                                '2017-09-11 14:20:44', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
                                                                                   NULL, NULL, NULL, NULL);
INSERT INTO t_tag_identity_map (uuid, tag_uuid, outer_identifier, financial_contract_uuid, contract_uuid, status, type, create_time, last_modified_time, date_field_one, date_field_two, date_field_three, long_field_one, long_field_two, long_field_three, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two, decimal_field_three)
VALUES ('9fb4f1fb-687a-4418-ad05-55334cdb549b', 'f94a6d6a-0ea8-4417-b00c-b56a189683f5', 'ZC274FD6122A206BBB',
                                                '5bfede8e-d08f-4544-94c6-11e33bf7b3c2',
                                                '62746f3d-8b41-4fa7-a1b9-a9b392883c72', 0, 0, '2017-09-11 15:41:18',
                                                '2017-09-11 15:41:18', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
                                                                                   NULL, NULL, NULL, NULL);
INSERT INTO t_tag (uuid, name, description, create_time, last_modified_time, status, date_field_one, date_field_two, date_field_three, long_field_one, long_field_two, long_field_three, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two, decimal_field_three)
VALUES
  ('1346b7cb-0823-4b88-9132-7307d989d793', '123123213123', '', '2017-09-11 14:20:44', '2017-09-11 14:20:44', 0, NULL,
                                           NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO t_tag (uuid, name, description, create_time, last_modified_time, status, date_field_one, date_field_two, date_field_three, long_field_one, long_field_two, long_field_three, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two, decimal_field_three)
VALUES
  ('f94a6d6a-0ea8-4417-b00c-b56a189683f5', '8888888888888', '234234234', '2017-09-11 15:41:18', '2017-09-11 15:41:18',
                                           0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;