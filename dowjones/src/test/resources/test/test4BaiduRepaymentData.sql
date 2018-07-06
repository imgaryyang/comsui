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

INSERT INTO financial_contract (asset_package_format, adva_matuterm, adva_start_date, contract_no, contract_name, app_id,
                                company_id, adva_repo_term, thru_date, capital_account_id, financial_contract_type, loan_overdue_start_day,
                                loan_overdue_end_day, payment_channel_id, ledger_book_no, financial_contract_uuid, sys_normal_deduct_flag,
                                sys_overdue_deduct_flag, sys_create_penalty_flag, sys_create_guarantee_flag, unusual_modify_flag, sys_create_statement_flag,
                                transaction_limit_per_transcation, transaction_limit_per_day, remittance_strategy_mode, app_account_uuids,
                                allow_online_repayment, allow_offline_repayment, allow_advance_deduct_flag, adva_repayment_term, penalty,
                                overdue_default_fee, overdue_service_fee, overdue_other_fee, create_time, last_modified_time,
                                repurchase_approach, repurchase_rule, repurchase_algorithm, day_of_month, pay_for_go,
                                repurchase_principal_algorithm, repurchase_interest_algorithm, repurchase_penalty_algorithm,
                                repurchase_other_charges_algorithm, repayment_check_days, repurchase_cycle, days_of_cycle,
                                allow_freewheeling_repayment, temporary_repurchases, capital_party
  , other_party, contract_short_name,
                                financial_type, remittance_object) VALUES (
  3, 0, '2015-01-01 00:00:00', 'WB123', 'WUBO的测试信托', 39, 41, 7,
     '2020-11-16 00:00:00', 103, 0, 6, 6, NULL, '445d31f8-0275-4f7e-9dec-acb582108c95',
                                    '2495a5ce-094e-4eb6-9fb9-95454b138427', 0, 0, 1, 0, 0, 0, 0.50, NULL, 0, 'null', 1,
                                                                                           1, 1, 5, '',
  NULL, NULL, NULL, '2017-11-27 15:11:44', '2018-01-16 20:07:18', 2, NULL, '', NULL, 1,
  'outstandingPrincipal', '', '', '', -1, NULL, NULL, 0, NULL, '[39]', '[39]', 'WUBO', 0, 0);

INSERT INTO journal_voucher (account_side, bank_identity, billing_plan_uuid, booking_amount, business_voucher_type_uuid,
                             business_voucher_uuid, cash_flow_amount, cash_flow_breif, cash_flow_channel_type, cash_flow_serial_no, cash_flow_uuid,
                             checking_level, company_id, completeness, counter_party_account, counter_party_name, journal_voucher_uuid,
                             notification_identity, notification_memo, notification_record_uuid, notified_date, settlement_modes,
                             source_document_amount, source_document_breif, source_document_cash_flow_serial_no, source_document_counter_party_uuid,
                             source_document_identity, source_document_uuid, status, trade_time, batch_uuid, created_date,
                             source_document_counter_party_account, source_document_counter_party_name, issued_time, journal_voucher_type,
                             counter_account_type, related_bill_contract_info_lv_1, related_bill_contract_info_lv_2, related_bill_contract_info_lv_3,
                             cash_flow_account_info, journal_voucher_no, related_bill_contract_no_lv_1, related_bill_contract_no_lv_2,
                             related_bill_contract_no_lv_3, related_bill_contract_no_lv_4, source_document_no, appendix,
                             last_modified_time, local_party_account, local_party_name, source_document_local_party_account,
                             source_document_local_party_name, second_journal_voucher_type, third_journal_voucher_type,
                             is_has_data_sync_log) VALUES (1, '中国银行 ', 'cb4bc4fe-31b9-4d3f-a90f-90961fea954e', 1000.00,
                                                              '2b784ef2-a1ab-41fa-b315-8e1bf8cfa2ba',
                                                              '293f6042-2508-42cf-ad37-9da24c2b3f80', 1000.00, NULL, 8,
                                                              NULL, NULL,
  0, 41, 3, '66660123456789990', '吴波', 'dabb4f0c-67d6-4e56-b563-21c445e83025', NULL, 'be4e4124428859f16e8c16549f94a7c2',
  '3c96bdf5-ae33-450c-b06d-c77c4350123d', NULL, NULL, 1000.00, NULL, NULL, NULL, '62e7f330-8933-4253-bedc-41f78296973a',
                                                'bd7e44dd-5f33-4809-9050-d661290b182a', 1, '2018-01-19 12:23:00', NULL,
  '2018-01-19 12:21:58', '66660123456789990',
  '吴波', '2018-01-19 12:21:59', 7, 0, '2495a5ce-094e-4eb6-9fb9-95454b138427', '989928cc-7ec2-41b9-9aa5-5b6a76e569db',
  'cb4bc4fe-31b9-4d3f-a90f-90961fea954e', '', 'a688d614-192c-44c5-9da8-d3538468a484', 'WUBO的测试信托',
                                              'acc38ad1-e20b-4c6a-873f-57d71ba1de04', 'ZC151703893640491008',
                                              'JS151704844126703616', 'KK151704843765993472',
                                              NULL, '2018-01-19 12:21:59', '831952389159132', '测试组最菜的那个',
                                                           '831952389159132', '测试组最菜的那个', 1, 0, 1);

INSERT INTO contract (id, uuid, unique_id, begin_date, contract_no, end_date, asset_type, month_fee, app_id, customer_id, house_id, actual_end_date, create_time, interest_rate, payment_day_in_month, payment_frequency, periods, repayment_way, total_amount, penalty_interest, active_version_no, repayment_plan_operate_logs, state, financial_contract_uuid, interest_rate_cycle, customer_uuid, repaymented_periods, completion_status, date_field_one, long_field_one, long_field_two, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two)
VALUES ('412215', '989928cc-7ec2-41b9-9aa5-5b6a76e569db', 'acc38ad1-e20b-4c6a-873f-57d71ba1de04', '2018-01-19',
                  'acc38ad1-e20b-4c6a-873f-57d71ba1de04', '2099-01-01', NULL, 0.00, 39, 1521324, 719174, NULL,
                                                                                                         '2018-01-19 12:18:12',
                                                                                                         0.02, 0, 0, 3,
                                                                                                         2, 3000.00,
                                                                                                         0.0005000000,
                                                                                                         1, NULL, 2,
                                                                                                            '2495a5ce-094e-4eb6-9fb9-95454b138427',
                                                                                                            1,
                                                                                                            'fa8fbafb-8dcc-4859-befe-fb26373bb48a',
                                                                                                            1, NULL,
                                                                                                            NULL, NULL,
                                                                                                            NULL, NULL,
        NULL, NULL, NULL, NULL);

INSERT INTO t_remittance_application (remittance_application_uuid, request_no, financial_contract_uuid, financial_contract_id, financial_product_code, contract_unique_id, contract_no, planned_total_amount, actual_total_amount, auditor_name, audit_time, notify_url, plan_notify_number, actual_notify_number, remittance_strategy, remark, transaction_recipient, execution_status, execution_remark, create_time, creator_name, ip, last_modified_time, opposite_receive_date, remittance_id, total_count, actual_count, version_lock, check_request_no, check_status, check_retry_number, check_send_time, notify_status, int_field_1, int_field_2, int_field_3, string_field_1, string_field_2, string_field_3, decimal_field_1, decimal_field_2, decimal_field_3)
VALUES ('41d91b80-12a4-4dab-abc6-92e678c5b1c6', '794b61a4-8f80-49ec-9d34-fbf9b6653291',
                                                '2495a5ce-094e-4eb6-9fb9-95454b138427', 90, 'WB123',
                                                'acc38ad1-e20b-4c6a-873f-57d71ba1de04',
                                                'acc38ad1-e20b-4c6a-873f-57d71ba1de04', 3000.00, 3000.00,
                                                'auditorName1', '2016-08-20 00:00:00',
  'http://101.52.128.166/Loan/PaidNotic', 3, 0, 0, '交易备注', 1, 2, NULL, '2018-01-19 12:05:24', 't_test_zfb',
  '192.168.0.200', '2018-01-19 12:06:30', '2018-01-19 12:05:30', NULL, 1, 1, '39612874-9a14-4f09-b09e-907af8d8f3cd',
  '09f48103-dc7b-45ae-b98b-d42a1bee202c', 4, 3, '2018-01-19 12:05:24', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
                                                NULL, NULL);

INSERT INTO customer (account, mobile, name, source, app_id, customer_uuid, customer_type, customer_style, status, id_type)
VALUES ('320301198502169142', NULL, 'WUBO', '78078935-59b7-4dc0-94cb-e19c5f6ba7f5', 39,
        'fa8fbafb-8dcc-4859-befe-fb26373bb48a', 0, 0, 0, 0);

INSERT INTO contract_account (pay_ac_no, bankcard_type, contract_id, payer_name, bank, bind_id, id_card_num, bank_code, province, province_code, city, city_code, standard_bank_code, from_date, thru_date, virtual_account_uuid, bank_card_status, contract_account_uuid, contract_account_type)
VALUES ('6214855712106520', NULL, 412215, 'WUBO', '邮政储蓄 ', NULL, '320301198502169142', NULL, '北京市', '110000', '北京市',
        '110100', 'C10403', '2018-01-19 12:18:12', '2900-01-01 00:00:00', NULL, 1, '51d20b89e19c46beb9a64490e506becd',
        0);

INSERT INTO asset_set (guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES (0, 0, 1000.00, 1000.00, 0.00, 1000.00, '2018-02-19', '2018-01-19', 0.00, 1, 2, 0, NULL,
                                                                                       'cb4bc4fe-31b9-4d3f-a90f-90961fea954e',
                                                                                       '2018-01-19 12:18:12',
                                                                                       '2018-01-19 12:21:59', NULL,
                                                                                       'ZC151703893640491008', 412215,
                                                                                       '2018-01-19 12:23:00', 1, 0,
                                                                                                                 NULL,
                                                                                                                 1, 0,
                                                                                                                 0,
                                                                                                                 '293f6042-2508-42cf-ad37-9da24c2b3f80',
                                                                                                                 NULL,
                                                                                                                 '2495a5ce-094e-4eb6-9fb9-95454b138427',
                                                                                                                 '157cd77ed45bb1b37dae723fa9adcb03',
                                                                                                                 '00bfd64b58361d989ac8bf13dccc3c9b',
  '2018-01-19 12:18:12', '2018-01-19 12:18:12', 0, 0, 0, 0, 3, 2, 0, 'fa8fbafb-8dcc-4859-befe-fb26373bb48a',
        '989928cc-7ec2-41b9-9aa5-5b6a76e569db', 0, 'e47e7fdf-0d7a-4a6b-9f51-aaf616e505c1', 0, NULL, NULL);
INSERT INTO asset_set (guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES (0, 0, 1000.00, 1000.00, 0.00, 1000.00, '2018-03-19', NULL, 0.00, 0, 1, 0, NULL,
                                                                               '4e1f8177-12dc-4069-bed4-d5f374257b69',
                                                                               '2018-01-19 14:58:15',
                                                                               '2018-01-19 16:23:07', NULL,
                                                                               'ZC151744170088779776', 412215, NULL, 2,
  0, NULL, 1, 2, 0, 'empty_deduct_uuid', NULL, '2495a5ce-094e-4eb6-9fb9-95454b138427',
  '8e20c81738ea6858053da2860255a24d', '00bfd64b58361d989ac8bf13dccc3c9b', '2018-01-19 14:58:15', '2018-01-19 14:58:15',
                                                                          0, 0, 0, 0, 0, 0, 0,
                                                                          'fa8fbafb-8dcc-4859-befe-fb26373bb48a',
        '989928cc-7ec2-41b9-9aa5-5b6a76e569db', NULL, '749934d8-b15a-4ee6-8d61-4b8f0fb83c3b', 0, NULL, NULL);
INSERT INTO asset_set (guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES
  (0, 0, 500.00, 500.00, 0.00, 500.00, '2018-04-19', NULL, 0.00, 0, 1, 0, NULL, 'ff4b3a0d-bcf1-4ba3-8577-bbb21d936ad6',
                                                                       '2018-01-19 14:58:15', '2018-01-19 16:23:07',
                                                                       NULL, 'ZC151744170151694336', 412215, NULL, 3, 0,
                                                                                                                      NULL,
                                                                                                                      1,
                                                                                                                      2,
                                                                                                                      0,
                                                                                                                      'empty_deduct_uuid',
                                                                                                                      NULL,
                                                                                                                      '2495a5ce-094e-4eb6-9fb9-95454b138427',
                                                                                                                      'c1f9eba38ec67f9c8a71265604ed05ca',
                                                                                                                      '00bfd64b58361d989ac8bf13dccc3c9b',
    '2018-01-19 14:58:15', '2018-01-19 14:58:15', 0, 0, 0, 0, 0, 0, 0, 'fa8fbafb-8dcc-4859-befe-fb26373bb48a',
   '989928cc-7ec2-41b9-9aa5-5b6a76e569db', NULL, '6438a42c-f4bf-49b5-beaa-69e5cfb6eb1e', 0, NULL, NULL);
INSERT INTO asset_set (guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES
  (0, 0, 500.00, 500.00, 0.00, 500.00, '2018-05-19', NULL, 0.00, 0, 1, 0, NULL, '9c128d0d-2626-4d6a-9033-5806e95ef17a',
                                                                       '2018-01-19 14:58:15', '2018-01-19 16:23:07',
                                                                       NULL, 'ZC151744170168471552', 412215, NULL, 4, 0,
                                                                                                                      NULL,
                                                                                                                      1,
                                                                                                                      2,
                                                                                                                      0,
                                                                                                                      'empty_deduct_uuid',
                                                                                                                      NULL,
                                                                                                                      '2495a5ce-094e-4eb6-9fb9-95454b138427',
                                                                                                                      '1a07c7c28f96cc27c04e9f6d4a3e00ea',
                                                                                                                      '00bfd64b58361d989ac8bf13dccc3c9b',
    '2018-01-19 14:58:15', '2018-01-19 14:58:15', 0, 0, 0, 0, 0, 0, 0, 'fa8fbafb-8dcc-4859-befe-fb26373bb48a',
   '989928cc-7ec2-41b9-9aa5-5b6a76e569db', NULL, '7478b569-294a-47b2-ad1f-73218750fdc2', 0, NULL, NULL);
INSERT INTO asset_set (guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES (0, 0, 2031.00, 2000.00, 22.00, 2022.00, '2018-01-19', NULL, 0.00, 0, 1, 1, '2018-01-19 16:23:07',
                                                                                'f181b67d-55f5-48b1-889f-33e22f5770e8',
                                                                                '2018-01-19 16:23:07',
                                                                                '2018-01-19 16:23:07', '3',
                                                                                'ZC151765528021618688', 412215, NULL, 2,
  0, NULL, 834874532, 0, 0, 'empty_deduct_uuid', NULL, '2495a5ce-094e-4eb6-9fb9-95454b138427', NULL, NULL,
  '2018-01-19 16:23:07', '2018-01-19 16:23:07', 1, 0, 1, 1, 0, 1, 0, 'fa8fbafb-8dcc-4859-befe-fb26373bb48a',
        '989928cc-7ec2-41b9-9aa5-5b6a76e569db', NULL, 'ecdd9ce5-4a3b-4536-ac0a-28e6b02ecdbd', 0, NULL, '');

INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('2a5cd385-a3f7-4a52-92ea-acd8b89a6e53', 'f181b67d-55f5-48b1-889f-33e22f5770e8', '2018-01-19 16:23:07',
                                                '2018-01-19 16:23:07', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_OTHER_FEE', '10000.05', NULL, NULL, 4.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('3969d954-0567-4032-8e91-4a2be6859093', 'f181b67d-55f5-48b1-889f-33e22f5770e8', '2018-01-19 16:23:07',
                                                '2018-01-19 16:23:07', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE', '10000.03', NULL, NULL,
                                                2.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('b67a7537-5f94-454e-bbb3-adabb17e89fd', 'f181b67d-55f5-48b1-889f-33e22f5770e8', '2018-01-19 16:23:07',
                                                '2018-01-19 16:23:07', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_TECH_FEE', '10000.04', NULL, NULL, 3.00);

INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('3711c5d1-b386-4a69-9488-91400617be2f', 0.00, 1000.00, 'FST_LONGTERM_LIABILITY', '40000', 0,
                                                'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL,
                                                'cbf70764-ca28-11e7-b26b-525400dbb013',
  'fa8fbafb-8dcc-4859-befe-fb26373bb48a', NULL, NULL, NULL, 'd33b0cd8-a5e3-4af3-b232-fbbeef219298', '2018-02-19',
  '2018-01-19 12:18:12', '', NULL, 412215, '989928cc-7ec2-41b9-9aa5-5b6a76e569db', '2018-02-19 00:00:00', '',
                                           '445d31f8-0275-4f7e-9dec-acb582108c95', '41', 1, 'ZC151703893640491008',
                                           'cb4bc4fe-31b9-4d3f-a90f-90961fea954e', NULL, NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('bd6021e8-c0a6-4e4a-9498-a65b7c012191', 1000.00, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                                                'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL,
                                                'cbf70764-ca28-11e7-b26b-525400dbb013',
  'fa8fbafb-8dcc-4859-befe-fb26373bb48a', NULL, NULL, NULL, 'd33b0cd8-a5e3-4af3-b232-fbbeef219298', '2018-02-19',
  '2018-01-19 12:18:12', '', NULL, 412215, '989928cc-7ec2-41b9-9aa5-5b6a76e569db', '2018-02-19 00:00:00', '',
                                           '445d31f8-0275-4f7e-9dec-acb582108c95', '41', 1, 'ZC151703893640491008',
                                           'cb4bc4fe-31b9-4d3f-a90f-90961fea954e', NULL, NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('9b491400-8695-4938-b387-8175dd25a318', 0.00, 1000.00, 'FST_LONGTERM_LIABILITY', '40000', 0,
                                                'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL,
                                                'cbf70764-ca28-11e7-b26b-525400dbb013',
  'fa8fbafb-8dcc-4859-befe-fb26373bb48a', NULL, NULL, NULL, '5914867f-8bbe-4f87-90a7-e6a32215776e', '2018-03-19',
  '2018-01-19 12:18:12', '', NULL, 412215, '989928cc-7ec2-41b9-9aa5-5b6a76e569db', '2018-03-19 00:00:00', '',
                                           '445d31f8-0275-4f7e-9dec-acb582108c95', '41', 1, 'ZC151703893644685312',
                                           'e728de3f-9a00-46f1-885d-efd0acec3fe1', NULL, NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('83f6dd6a-5004-4a82-a409-751eb81be1a8', 1000.00, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                                                'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL,
                                                'cbf70764-ca28-11e7-b26b-525400dbb013',
  'fa8fbafb-8dcc-4859-befe-fb26373bb48a', NULL, NULL, NULL, '5914867f-8bbe-4f87-90a7-e6a32215776e', '2018-03-19',
  '2018-01-19 12:18:12', '', NULL, 412215, '989928cc-7ec2-41b9-9aa5-5b6a76e569db', '2018-03-19 00:00:00', '',
                                           '445d31f8-0275-4f7e-9dec-acb582108c95', '41', 1, 'ZC151703893644685312',
                                           'e728de3f-9a00-46f1-885d-efd0acec3fe1', NULL, NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('2c3f3845-0edc-4c6e-9bce-6b73fbeef887', 0.00, 1000.00, 'FST_LONGTERM_LIABILITY', '40000', 0,
                                                'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL,
                                                'cbf70764-ca28-11e7-b26b-525400dbb013',
  'fa8fbafb-8dcc-4859-befe-fb26373bb48a', NULL, NULL, NULL, '62cd981a-f15b-4f7a-807e-4712b44f53e3', '2018-04-19',
  '2018-01-19 12:18:12', '', NULL, 412215, '989928cc-7ec2-41b9-9aa5-5b6a76e569db', '2018-04-19 00:00:00', '',
                                           '445d31f8-0275-4f7e-9dec-acb582108c95', '41', 1, 'ZC151703893653073920',
                                           'b053336e-0bad-4712-9bbb-94ed20b7d49b', NULL, NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('2f87c15b-b1f8-4dd0-8e94-a7bcd213d369', 1000.00, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                                                'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL,
                                                'cbf70764-ca28-11e7-b26b-525400dbb013',
  'fa8fbafb-8dcc-4859-befe-fb26373bb48a', NULL, NULL, NULL, '62cd981a-f15b-4f7a-807e-4712b44f53e3', '2018-04-19',
  '2018-01-19 12:18:12', '', NULL, 412215, '989928cc-7ec2-41b9-9aa5-5b6a76e569db', '2018-04-19 00:00:00', '',
                                           '445d31f8-0275-4f7e-9dec-acb582108c95', '41', 1, 'ZC151703893653073920',
                                           'b053336e-0bad-4712-9bbb-94ed20b7d49b', NULL, NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('1cf22429-5e12-4438-9ff9-238db95cb51a', 0.00, 1000.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                                                'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL,
                                                'cbf70764-ca28-11e7-b26b-525400dbb013',
  'fa8fbafb-8dcc-4859-befe-fb26373bb48a', NULL, '14636982-ef89-4fa3-8f42-fab570b56d10', NULL,
  'cc34c0b7-960a-42e0-baf8-4e4034f764d8', '2018-02-19', '2018-01-19 12:21:59', '293f6042-2508-42cf-ad37-9da24c2b3f80',
  '2018-01-19 12:23:00', 412215, '989928cc-7ec2-41b9-9aa5-5b6a76e569db', '2018-02-19 00:00:00',
                                 'dabb4f0c-67d6-4e56-b563-21c445e83025', '445d31f8-0275-4f7e-9dec-acb582108c95', '41',
                                 1, 'ZC151703893640491008', 'cb4bc4fe-31b9-4d3f-a90f-90961fea954e', NULL, NULL, NULL,
        'be4e4124428859f16e8c16549f94a7c2', 'bd7e44dd-5f33-4809-9050-d661290b182a');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('14636982-ef89-4fa3-8f42-fab570b56d10', 1000.00, 0.00, 'FST_BANK_SAVING', '60000', 1, '002762_001',
                                                '60000.002762_001', 'TRD_BANK_SAVING_GENERAL_PRINCIPAL',
                                                '60000.1000.01', 'cbf70764-ca28-11e7-b26b-525400dbb013', '', NULL, NULL,
                                                                                                         '1cf22429-5e12-4438-9ff9-238db95cb51a',
                                                                                                         'cc34c0b7-960a-42e0-baf8-4e4034f764d8',
                                                                                                         '2018-02-19',
                                                                                                         '2018-01-19 12:21:59',
                                                                                                         '293f6042-2508-42cf-ad37-9da24c2b3f80',
                                                                                                         '2018-01-19 12:23:00',
                                                                                                         412215,
  '989928cc-7ec2-41b9-9aa5-5b6a76e569db', '2018-02-19 00:00:00', 'dabb4f0c-67d6-4e56-b563-21c445e83025',
  '445d31f8-0275-4f7e-9dec-acb582108c95', '41', 1, 'ZC151703893640491008', 'cb4bc4fe-31b9-4d3f-a90f-90961fea954e', NULL,
  NULL, NULL, 'be4e4124428859f16e8c16549f94a7c2', 'bd7e44dd-5f33-4809-9050-d661290b182a');











