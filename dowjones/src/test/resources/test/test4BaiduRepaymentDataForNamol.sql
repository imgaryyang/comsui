DELETE FROM contract;
DELETE FROM financial_contract;
DELETE FROM asset_set;
DELETE FROM journal_voucher;
DELETE FROM t_remittance_application;
DELETE FROM customer;
DELETE FROM contract_account;
DELETE FROM customer_person;
TRUNCATE TABLE ledger_book_shelf;
DELETE FROM asset_set_extra_charge;
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004348, '0605b91d-8c9a-4805-a8a6-f4a6c925d8cc', 0.00, 1000.00, 'FST_LONGTERM_LIABILITY', '40000', 0,
                  'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL,
  '9714eb42-6c52-467b-8ade-7e71d8f58d0b', 'a4783304-0c39-49e2-9f92-0a301047c4c7', NULL, NULL, NULL,
  'cbd057c2-add8-4a9a-bea2-f70925d9d036', '2018-02-22', '2018-01-22 14:31:06', '', NULL, 12483590,
                                                                                         '07ee9b8e-e90c-4bc0-b75a-5dde8baa8016',
                                                                                         '2018-02-22 00:00:00', '',
                                                                                         'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                         '11', 1,
                                                                                         'ZC152824499795042304',
                                                                                         'aeffa864-302d-47c0-b0a5-444ff5979e90',
                                                                                         NULL, NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004349, '5ef9cb85-5c08-4c17-b8f1-03d886366cf5', 1000.00, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, '9714eb42-6c52-467b-8ade-7e71d8f58d0b',
                                                                               'a4783304-0c39-49e2-9f92-0a301047c4c7',
                                                                               NULL, NULL, NULL,
                                                                               'cbd057c2-add8-4a9a-bea2-f70925d9d036',
                                                                               '2018-02-22', '2018-01-22 14:31:06', '',
                                                                               NULL, 12483590,
                                                                                     '07ee9b8e-e90c-4bc0-b75a-5dde8baa8016',
                                                                                     '2018-02-22 00:00:00', '',
                                                                                     'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                     '11', 1, 'ZC152824499795042304',
                                                                                     'aeffa864-302d-47c0-b0a5-444ff5979e90',
                                                                                     NULL, NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004350, '5349c49b-49c7-44a0-8da7-e6805036aad1', 0.00, 1000.00, 'FST_LONGTERM_LIABILITY', '40000', 0,
                  'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL,
  '9714eb42-6c52-467b-8ade-7e71d8f58d0b', 'a4783304-0c39-49e2-9f92-0a301047c4c7', NULL, NULL, NULL,
  'f1c042e6-54ac-4196-9701-8cf08461da51', '2018-03-22', '2018-01-22 14:31:06', '', NULL, 12483590,
                                                                                         '07ee9b8e-e90c-4bc0-b75a-5dde8baa8016',
                                                                                         '2018-03-22 00:00:00', '',
                                                                                         'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                         '11', 1,
                                                                                         'ZC152824499816013824',
                                                                                         '1067bd0d-8155-4423-b98f-fd55c4bc55b4',
                                                                                         NULL, NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004351, '1510b1a6-0007-46df-95b5-091059634fd8', 1000.00, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, '9714eb42-6c52-467b-8ade-7e71d8f58d0b',
                                                                               'a4783304-0c39-49e2-9f92-0a301047c4c7',
                                                                               NULL, NULL, NULL,
                                                                               'f1c042e6-54ac-4196-9701-8cf08461da51',
                                                                               '2018-03-22', '2018-01-22 14:31:06', '',
                                                                               NULL, 12483590,
                                                                                     '07ee9b8e-e90c-4bc0-b75a-5dde8baa8016',
                                                                                     '2018-03-22 00:00:00', '',
                                                                                     'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                     '11', 1, 'ZC152824499816013824',
                                                                                     '1067bd0d-8155-4423-b98f-fd55c4bc55b4',
                                                                                     NULL, NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004352, '142339af-4d5a-4874-bbf4-74288cf5fc00', 0.00, 1000.00, 'FST_LONGTERM_LIABILITY', '40000', 0,
                  'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL,
  '9714eb42-6c52-467b-8ade-7e71d8f58d0b', 'a4783304-0c39-49e2-9f92-0a301047c4c7', NULL, NULL, NULL,
  'faf901a6-15d6-4280-9723-d74fb266d893', '2018-04-22', '2018-01-22 14:31:06', '', NULL, 12483590,
                                                                                         '07ee9b8e-e90c-4bc0-b75a-5dde8baa8016',
                                                                                         '2018-04-22 00:00:00', '',
                                                                                         'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                         '11', 1,
                                                                                         'ZC152824499832791040',
                                                                                         '0218ec45-e7fd-4072-b980-a95f802e7f89',
                                                                                         NULL, NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004353, '335a7a6d-6f8f-40ec-a5cf-01e5084a7daf', 1000.00, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, '9714eb42-6c52-467b-8ade-7e71d8f58d0b',
                                                                               'a4783304-0c39-49e2-9f92-0a301047c4c7',
                                                                               NULL, NULL, NULL,
                                                                               'faf901a6-15d6-4280-9723-d74fb266d893',
                                                                               '2018-04-22', '2018-01-22 14:31:06', '',
                                                                               NULL, 12483590,
                                                                                     '07ee9b8e-e90c-4bc0-b75a-5dde8baa8016',
                                                                                     '2018-04-22 00:00:00', '',
                                                                                     'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                     '11', 1, 'ZC152824499832791040',
                                                                                     '0218ec45-e7fd-4072-b980-a95f802e7f89',
                                                                                     NULL, NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004354, 'b8260bf1-4124-454a-8299-f2a32a033cb9', 0.00, 1000.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, '9714eb42-6c52-467b-8ade-7e71d8f58d0b',
                                                                               'a4783304-0c39-49e2-9f92-0a301047c4c7',
                                                                               NULL,
                                                                               '8fac3005-05f2-4349-86cb-46fec6950f5d',
                                                                               NULL,
                                                                               '18aee4d6-8dff-44f0-b487-b8326b7ed0ee',
                                                                               '2018-02-22', '2018-01-22 14:33:14',
                                                                               'b51a919c-52df-4115-bd5c-ae5e271bbb03',
                                                                               '2018-01-22 14:33:01', 12483590,
                                                                                                      '07ee9b8e-e90c-4bc0-b75a-5dde8baa8016',
                                                                                                      '2018-02-22 00:00:00',
                                                                                                      'f119a29d-d875-4fb0-aa39-a7a8a961ce88',
                                                                                                      'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                                      '11', 1,
                                                                                                      'ZC152824499795042304',
                                                                                                      'aeffa864-302d-47c0-b0a5-444ff5979e90',
                                                                                                      NULL, NULL, NULL,
        '184a89fbfe1a1c526e5c5888e16fbbf7', '7be7d3b9-b1b3-44d1-b5be-ec371b6c9202');
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004355, '8fac3005-05f2-4349-86cb-46fec6950f5d', 1000.00, 0.00, 'FST_BANK_SAVING', '60000', 1, '002762_001',
                  '60000.002762_001', 'TRD_BANK_SAVING_GENERAL_PRINCIPAL', '60000.1000.01',
  '9714eb42-6c52-467b-8ade-7e71d8f58d0b', '', NULL, NULL, 'b8260bf1-4124-454a-8299-f2a32a033cb9',
  '18aee4d6-8dff-44f0-b487-b8326b7ed0ee', '2018-02-22', '2018-01-22 14:33:14', 'b51a919c-52df-4115-bd5c-ae5e271bbb03',
  '2018-01-22 14:33:01', 12483590, '07ee9b8e-e90c-4bc0-b75a-5dde8baa8016', '2018-02-22 00:00:00',
                         'f119a29d-d875-4fb0-aa39-a7a8a961ce88', 'a0ea9406-eee9-478d-9a99-445058e7ecc9', '11', 1,
                         'ZC152824499795042304', 'aeffa864-302d-47c0-b0a5-444ff5979e90', NULL, NULL, NULL,
        '184a89fbfe1a1c526e5c5888e16fbbf7', '7be7d3b9-b1b3-44d1-b5be-ec371b6c9202');
INSERT INTO contract (id, uuid, unique_id, begin_date, contract_no, end_date, asset_type, month_fee, app_id, customer_id, house_id, actual_end_date, create_time, interest_rate, payment_day_in_month, payment_frequency, periods, repayment_way, total_amount, penalty_interest, active_version_no, repayment_plan_operate_logs, state, financial_contract_uuid, interest_rate_cycle, customer_uuid, repaymented_periods, completion_status, date_field_one, long_field_one, long_field_two, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two)
VALUES (12483590, '07ee9b8e-e90c-4bc0-b75a-5dde8baa8016', 'acc38ad1-e20b-4c6a-873f-57d71ba1de04', '2018-01-22',
                  'acc38ad1-e20b-4c6a-873f-57d71ba1de04', '2099-01-01', NULL, 0.00, 9, 271665, 271800, NULL,
                                                                                                       '2018-01-22 14:31:05',
                                                                                                       0.0000000000, 0,
                                                                                                       0, 3, 2, 3000.00,
                                                                                                       0.0005000000, 1,
  NULL, 2, 'c420576a-4d5b-4d45-b880-2fd5508cc2db', 1, 'a4783304-0c39-49e2-9f92-0a301047c4c7', 1, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL);

INSERT INTO financial_contract (id, asset_package_format, adva_matuterm, adva_start_date, contract_no, contract_name, app_id, company_id, adva_repo_term, thru_date, capital_account_id, financial_contract_type, loan_overdue_start_day,
                                loan_overdue_end_day, payment_channel_id, ledger_book_no, financial_contract_uuid, sys_normal_deduct_flag,
                                sys_overdue_deduct_flag, sys_create_penalty_flag, sys_create_guarantee_flag, unusual_modify_flag,
                                sys_create_statement_flag, transaction_limit_per_transcation, transaction_limit_per_day,
                                remittance_strategy_mode, app_account_uuids, allow_online_repayment, allow_offline_repayment,
                                allow_advance_deduct_flag, adva_repayment_term, penalty, overdue_default_fee, overdue_service_fee,
                                overdue_other_fee, create_time, last_modified_time, repurchase_approach, repurchase_rule,
                                repurchase_algorithm, day_of_month, pay_for_go, repurchase_principal_algorithm,
                                repurchase_interest_algorithm, repurchase_penalty_algorithm, repurchase_other_charges_algorithm,
                                repayment_check_days, repurchase_cycle, days_of_cycle, temporary_repurchases,
                                allow_freewheeling_repayment, capital_party, other_party, contract_short_name, financial_type,
                                remittance_object) VALUES
  (100125, 0, 0, '2015-01-01 00:00:00', 'WB123', 'WB123', 9, 11, 6, '2019-06-01 00:00:00', 340, 0, 5, 5, NULL,
                                                                                                'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                                'c420576a-4d5b-4d45-b880-2fd5508cc2db',
                                                                                                1, 1, 1, 0, 0, 1, NULL,
                                                                                                            NULL, 0,
                                                                                                            'null', 1,
                                                                                                            1, 1, 4, '',
                                                                                                                     NULL,
                                                                                                                     NULL,
                                                                                                                     NULL,
                                                                                                                     '2017-12-15 16:03:35',
                                                                                                                     '2018-01-22 14:24:04',
                                                                                                                     2,
                                                                                                                     NULL,
                                                                                                                     '',
                                                                                                                     NULL,
    0, '', '', '', '', -1, NULL, NULL, NULL, 0, '[9]', '[9]', 'WB', 0, 1);

INSERT INTO journal_voucher (id, account_side, bank_identity, billing_plan_uuid, booking_amount, business_voucher_type_uuid, business_voucher_uuid, cash_flow_amount, cash_flow_breif, cash_flow_channel_type, cash_flow_serial_no, cash_flow_uuid, checking_level, company_id, completeness, counter_party_account, counter_party_name, journal_voucher_uuid, notification_identity, notification_memo, notification_record_uuid, notified_date, settlement_modes, source_document_amount, source_document_breif, source_document_cash_flow_serial_no, source_document_counter_party_uuid, source_document_identity, source_document_uuid, status, trade_time, batch_uuid, created_date, source_document_counter_party_account, source_document_counter_party_name, issued_time, journal_voucher_type, counter_account_type, related_bill_contract_info_lv_1, related_bill_contract_info_lv_2, related_bill_contract_info_lv_3, cash_flow_account_info, journal_voucher_no, related_bill_contract_no_lv_1, related_bill_contract_no_lv_2, related_bill_contract_no_lv_3, related_bill_contract_no_lv_4, source_document_no, appendix, last_modified_time, local_party_account, local_party_name, source_document_local_party_account, source_document_local_party_name, second_journal_voucher_type, third_journal_voucher_type, is_has_data_sync_log)
VALUES (229194, 1, '中国银行 ', 'aeffa864-302d-47c0-b0a5-444ff5979e90', 1000.00, '2b784ef2-a1ab-41fa-b315-8e1bf8cfa2ba',
                'b51a919c-52df-4115-bd5c-ae5e271bbb03', 1000.00, NULL, 8, NULL, NULL, 0, 11, 3, '66660123456789990',
                                                                                '吴波',
                                                                                'f119a29d-d875-4fb0-aa39-a7a8a961ce88',
                                                                                NULL,
                                                                                '184a89fbfe1a1c526e5c5888e16fbbf7',
                                                                                '140b1401-ae67-4103-ac7a-d5291dceb607',
  NULL, NULL, 1000.00, NULL, NULL, NULL, '125f2a74-7f01-404c-b8e2-7074c4292468', '7be7d3b9-b1b3-44d1-b5be-ec371b6c9202',
  1, '2018-01-22 14:33:01', NULL, '2018-01-22 14:33:14', '66660123456789990', '吴波', '2018-01-22 14:33:14', 7, 0,
                            'c420576a-4d5b-4d45-b880-2fd5508cc2db', '07ee9b8e-e90c-4bc0-b75a-5dde8baa8016',
                            'aeffa864-302d-47c0-b0a5-444ff5979e90', '', '4aa779e3-b929-4d6f-89f7-4f1ff11ace46', 'WB123',
                                                                    'acc38ad1-e20b-4c6a-873f-57d71ba1de04',
                                                                    'ZC152824499795042304', 'JS152825038452203520',
                                                                    'KK152825035025457152', NULL, '2018-01-22 14:33:17',
                                                                    '831952389159132', '测试组最菜的那个', '831952389159132',
        '测试组最菜的那个', 1, 0, 1);

INSERT INTO asset_set (id, guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES (12563808, 0, 0, 1000.00, 1000.00, 0.00, 1000.00, '2018-02-22', '2018-01-22', 0.00, 1, 2, 0, NULL,
                                                                                              'aeffa864-302d-47c0-b0a5-444ff5979e90',
                                                                                              '2018-01-22 14:31:06',
                                                                                              '2018-01-22 14:33:15',
                                                                                              NULL,
                                                                                              'ZC152824499795042304',
                                                                                              12483590,
                                                                                              '2018-01-22 14:33:01', 1,
                                                                                                                     0,
                                                                                                                     NULL,
                                                                                                                     1,
                                                                                                                     0,
                                                                                                                     0,
                                                                                                                     'b51a919c-52df-4115-bd5c-ae5e271bbb03',
                                                                                                                     NULL,
                                                                                                                     'c420576a-4d5b-4d45-b880-2fd5508cc2db',
                                                                                                                     '435376352014b9784a616433cc1fb3d4',
  '00bfd64b58361d989ac8bf13dccc3c9b', '2018-01-22 14:31:06', '2018-01-22 14:31:06', 0, 0, 0, 0, 3, 2, 0,
        'a4783304-0c39-49e2-9f92-0a301047c4c7', '07ee9b8e-e90c-4bc0-b75a-5dde8baa8016', 0,
        'f62e4b28-3655-4f4f-807e-e9d5c4ec8811', 0, NULL, NULL);
INSERT INTO asset_set (id, guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES (12563809, 0, 0, 1000.00, 1000.00, 0.00, 1000.00, '2018-03-22', NULL, 0.00, 0, 1, 0, NULL,
                                                                                      '1067bd0d-8155-4423-b98f-fd55c4bc55b4',
                                                                                      '2018-01-22 14:31:06',
                                                                                      '2018-01-22 14:31:06', NULL,
                                                                                      'ZC152824499816013824', 12483590,
                                                                                      NULL, 2, 0, NULL, 1, 0, 0,
                                                                                            'empty_deduct_uuid', NULL,
                                                                                            'c420576a-4d5b-4d45-b880-2fd5508cc2db',
                                                                                            '8e0b40e3fa8e85ed8b4343caedf77752',
  '00bfd64b58361d989ac8bf13dccc3c9b', '2018-01-22 14:31:06', '2018-01-22 14:31:06', 0, 0, 0, 0, 0, 0, 0,
        'a4783304-0c39-49e2-9f92-0a301047c4c7', '07ee9b8e-e90c-4bc0-b75a-5dde8baa8016', NULL,
        'a958e81b-80a1-4aec-b184-287dffd6d945', 0, NULL, NULL);
INSERT INTO asset_set (id, guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES (12563810, 0, 0, 1000.00, 1000.00, 0.00, 1000.00, '2018-04-22', NULL, 0.00, 0, 1, 0, NULL,
                                                                                      '0218ec45-e7fd-4072-b980-a95f802e7f89',
                                                                                      '2018-01-22 14:31:06',
                                                                                      '2018-01-22 14:31:06', NULL,
                                                                                      'ZC152824499832791040', 12483590,
                                                                                      NULL, 3, 0, NULL, 1, 0, 0,
                                                                                            'empty_deduct_uuid', NULL,
                                                                                            'c420576a-4d5b-4d45-b880-2fd5508cc2db',
                                                                                            '8d4c84c2e330ee3ccf7148dc159872c3',
  '00bfd64b58361d989ac8bf13dccc3c9b', '2018-01-22 14:31:06', '2018-01-22 14:31:06', 0, 0, 0, 0, 0, 0, 0,
        'a4783304-0c39-49e2-9f92-0a301047c4c7', '07ee9b8e-e90c-4bc0-b75a-5dde8baa8016', NULL,
        'cd688e4b-b673-434c-9c8a-9c00ea819054', 0, NULL, NULL);

INSERT INTO contract_account (id, pay_ac_no, bankcard_type, contract_id, payer_name, bank, bind_id, id_card_num, bank_code, province, province_code, city, city_code, standard_bank_code, from_date, thru_date, virtual_account_uuid, bank_card_status, contract_account_uuid, contract_account_type)
VALUES (279839, '6214855712106520', NULL, 12483590, 'WUBO', '邮政储蓄 ', NULL, '320301198502169142', NULL, '北京市', '110000',
        '北京市', '110100', 'C10403', '2018-01-22 14:31:05', '2900-01-01 00:00:00', NULL, 1,
        '2731df7b38934d1aa385aab6a0ff4939', 0);
INSERT INTO t_remittance_application (id, remittance_application_uuid, request_no, financial_contract_uuid, financial_contract_id, financial_product_code, contract_unique_id, contract_no, planned_total_amount, actual_total_amount, auditor_name, audit_time, notify_url, plan_notify_number, actual_notify_number, remittance_strategy, remark, transaction_recipient, execution_status, execution_remark, create_time, creator_name, ip, last_modified_time, opposite_receive_date, remittance_id, total_count, actual_count, version_lock, check_request_no, check_status, check_retry_number, check_send_time, notify_status, int_field_1, int_field_2, int_field_3, string_field_1, string_field_2, string_field_3, decimal_field_1, decimal_field_2, decimal_field_3)
VALUES (238685, '0fad449a-e29e-4665-9f7b-a007d5808e6f', '52f7028d-cfc7-4972-8f3b-1fe2fc7cf7e9',
                'c420576a-4d5b-4d45-b880-2fd5508cc2db', 100125, 'WB123', 'acc38ad1-e20b-4c6a-873f-57d71ba1de04',
                'acc38ad1-e20b-4c6a-873f-57d71ba1de04', 3000.00, 3000.00, 'auditorName1', '2016-08-20 00:00:00',
                                                                                          'http://101.52.128.166/Loan/PaidNotic',
                                                                                          3, 2, 0, '交易备注', 1, 2, NULL,
                                                                                          '2018-01-22 14:24:09',
  't_test_zfb', '115.197.186.160, 120.26.102.180', '2018-01-22 14:28:11', '2018-01-22 14:24:15', NULL, 1, 1,
  'b7d99d3f-6b9b-48a5-8570-be5119d6d8d5', 'aa7c4d24-5429-4b24-a1e1-0af9640c95b4', 4, 3, '2018-01-22 14:24:09', 1, NULL,
                                                                                     NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL);

INSERT INTO customer (id, account, mobile, name, source, app_id, customer_uuid, customer_type, customer_style, status, id_type)
VALUES (271665, '320301198502169142', NULL, 'WUBO', '58cce7c7-9036-4f0d-aac3-db73a42ab903', 9,
                'a4783304-0c39-49e2-9f92-0a301047c4c7', 0, 0, 0, 0);

