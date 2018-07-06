DELETE FROM financial_contract;
DELETE FROM contract;
DELETE FROM t_remittance_application;
DELETE FROM customer;
DELETE FROM contract_account;
DELETE FROM asset_set;

INSERT INTO financial_contract (asset_package_format, adva_matuterm, adva_start_date, contract_no, contract_name,
                                app_id, company_id, adva_repo_term, thru_date, capital_account_id, financial_contract_type, loan_overdue_start_day,
                                loan_overdue_end_day, payment_channel_id, ledger_book_no, financial_contract_uuid, sys_normal_deduct_flag,
                                sys_overdue_deduct_flag, sys_create_penalty_flag, sys_create_guarantee_flag, unusual_modify_flag, sys_create_statement_flag,
                                transaction_limit_per_transcation, transaction_limit_per_day, remittance_strategy_mode, app_account_uuids,
                                allow_online_repayment, allow_offline_repayment, allow_advance_deduct_flag, adva_repayment_term, penalty,
                                overdue_default_fee, overdue_service_fee, overdue_other_fee, create_time, last_modified_time, repurchase_approach,
                                repurchase_rule, repurchase_algorithm, day_of_month, pay_for_go, repurchase_principal_algorithm,
                                repurchase_interest_algorithm, repurchase_penalty_algorithm, repurchase_other_charges_algorithm,
                                repayment_check_days, repurchase_cycle, days_of_cycle, allow_freewheeling_repayment, temporary_repurchases,
                                capital_party, other_party, contract_short_name, financial_type, remittance_object)
VALUES (3, 0, '2015-01-01 00:00:00', 'WB123', 'WUBO的测试信托', 39, 41, 7, '2020-11-16 00:00:00', 103, 0, 6, 6, NULL,
                                                                                                     '445d31f8-0275-4f7e-9dec-acb582108c95',
                                                                                                     '2495a5ce-094e-4eb6-9fb9-95454b138427',
                                                                                                     0, 0, 1, 0, 0, 0,
                                                                                                                    0.50,
                                                                                                                    NULL,
                                                                                                                    0,
                                                                                                                    'null',
                                                                                                                    1,
                                                                                                                    1,
                                                                                                                    1,
                                                                                                                    5,
                                                                                                                    '',
  NULL, NULL, NULL, '2017-11-27 15:11:44', '2018-01-16 20:07:18', 2, NULL, '', NULL, 1,
  'outstandingPrincipal', '', '', '', -1, NULL, NULL, 0, NULL, '[39]', '[39]', 'WUBO', 0, 0);

INSERT INTO contract (id, uuid, unique_id, begin_date, contract_no, end_date, asset_type, month_fee, app_id, customer_id, house_id, actual_end_date, create_time, interest_rate, payment_day_in_month, payment_frequency, periods, repayment_way, total_amount, penalty_interest, active_version_no, repayment_plan_operate_logs, state, financial_contract_uuid, interest_rate_cycle, customer_uuid, repaymented_periods, completion_status, date_field_one, long_field_one, long_field_two, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two)
VALUES ('412214', '7ce70c8f-c67a-48eb-a176-44c4e9f59097', 'f1684dbe-b70e-4f40-be9c-e31a920fe422', '2018-01-19',
                  'f1684dbe-b70e-4f40-be9c-e31a920fe422', '2099-01-01', NULL, 0.00, 39, 1521323, 719173, NULL,
                                                                                                         '2018-01-19 12:16:49',
                                                                                                         0.0000000000,
                                                                                                         0, 0, 3, 2,
                                                                                                         3000.00,
                                                                                                         0.0005000000,
                                                                                                         1, NULL, 2,
                                                                                                            '2495a5ce-094e-4eb6-9fb9-95454b138427',
                                                                                                            1,
                                                                                                            'a240d0d3-3d18-4126-a104-34d7e90f10e4',
                                                                                                            0, NULL,
                                                                                                            NULL, NULL,
                                                                                                            NULL, NULL,
        NULL, NULL, NULL, NULL);

INSERT INTO t_remittance_application (remittance_application_uuid, request_no, financial_contract_uuid, financial_contract_id, financial_product_code, contract_unique_id, contract_no, planned_total_amount, actual_total_amount, auditor_name, audit_time, notify_url, plan_notify_number, actual_notify_number, remittance_strategy, remark, transaction_recipient, execution_status, execution_remark, create_time, creator_name, ip, last_modified_time, opposite_receive_date, remittance_id, total_count, actual_count, version_lock, check_request_no, check_status, check_retry_number, check_send_time, notify_status, int_field_1, int_field_2, int_field_3, string_field_1, string_field_2, string_field_3, decimal_field_1, decimal_field_2, decimal_field_3)
VALUES ('df6e33e0-cd82-4ca5-a529-199ae71b2ef5', '764a120f-6bd2-4c7a-bf12-ff7dce87107c',
                                                '2495a5ce-094e-4eb6-9fb9-95454b138427', 90, 'WB123',
                                                'f1684dbe-b70e-4f40-be9c-e31a920fe422',
                                                'f1684dbe-b70e-4f40-be9c-e31a920fe422', 3000.00, 3000.00,
                                                'auditorName1', '2016-08-20 00:00:00',
  'http://101.52.128.166/Loan/PaidNotic', 3, 0, 0, '交易备注', 1, 2, NULL, '2018-01-19 12:05:08', 't_test_zfb',
  '192.168.0.200', '2018-01-19 12:05:30', '2018-01-19 12:05:10', NULL, 1, 1, 'e06e5a0a-3167-4482-823b-ccb1112d2096',
  'aa844519-0d23-4790-9ee8-cf1aeff841b2', 4, 3, '2018-01-19 12:05:08', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
                                                NULL, NULL);
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
VALUES ('320301198502169142', NULL, 'WUBO', 'aa964743-291b-4a46-bb76-3f4f962afc94', 39,
        'a240d0d3-3d18-4126-a104-34d7e90f10e4', 0, 0, 0, 0);

INSERT INTO contract_account (pay_ac_no, bankcard_type, contract_id, payer_name, bank, bind_id, id_card_num, bank_code, province, province_code, city, city_code, standard_bank_code, from_date, thru_date, virtual_account_uuid, bank_card_status, contract_account_uuid, contract_account_type)
VALUES ('6214855712106520', NULL, 412214, 'WUBO', '邮政储蓄 ', NULL, '320301198502169142', NULL, '北京市', '110000', '北京市',
        '110100', 'C10403', '2018-01-19 12:16:49', '2900-01-01 00:00:00', NULL, 1, '45660bb455bc46bfb47e3036b9fd0411',
        0);

INSERT INTO asset_set (guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES (0, 0, 1000.00, 1000.00, 0.00, 1000.00, '2018-02-19', NULL, 0.00, 0, 1, 0, NULL,
                                                                               'ffc1822a-62e1-4219-84ea-4dbfbefa038e',
                                                                               '2018-01-19 12:16:49',
                                                                               '2018-01-19 12:16:49', NULL,
                                                                               'ZC151703544645038080', 412214, NULL, 1,
  0, NULL, 1, 0, 0, 'empty_deduct_uuid', NULL, '2495a5ce-094e-4eb6-9fb9-95454b138427',
  '157cd77ed45bb1b37dae723fa9adcb03', '00bfd64b58361d989ac8bf13dccc3c9b', '2018-01-19 12:16:49', '2018-01-19 12:16:49',
                                                                          0, 0, 0, 0, 0, 0, 0,
                                                                          'a240d0d3-3d18-4126-a104-34d7e90f10e4',
        '7ce70c8f-c67a-48eb-a176-44c4e9f59097', NULL, 'be0ef715-46c9-4935-b51e-ef976177b2cc', 0, NULL, NULL);
INSERT INTO asset_set (guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES (0, 0, 1000.00, 1000.00, 0.00, 1000.00, '2018-03-19', NULL, 0.00, 0, 1, 0, NULL,
                                                                               '4494b5d8-3c47-4d0e-a6e5-ee3fbc14dada',
                                                                               '2018-01-19 12:16:49',
                                                                               '2018-01-19 12:16:49', NULL,
                                                                               'ZC151703544817004544', 412214, NULL, 2,
  0, NULL, 1, 0, 0, 'empty_deduct_uuid', NULL, '2495a5ce-094e-4eb6-9fb9-95454b138427',
  'aabb591862279e640d957ed900f87dd7', '00bfd64b58361d989ac8bf13dccc3c9b', '2018-01-19 12:16:49', '2018-01-19 12:16:49',
                                                                          0, 0, 0, 0, 0, 0, 0,
                                                                          'a240d0d3-3d18-4126-a104-34d7e90f10e4',
        '7ce70c8f-c67a-48eb-a176-44c4e9f59097', NULL, 'ded12436-3d21-4cd8-a249-bebe5b91c787', 0, NULL, NULL);
INSERT INTO asset_set (guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES (0, 0, 1000.00, 1000.00, 0.00, 1000.00, '2018-04-19', NULL, 0.00, 0, 1, 0, NULL,
                                                                               '8f41dfe2-d395-4003-9faf-e5e2c98f3dde',
                                                                               '2018-01-19 12:16:49',
                                                                               '2018-01-19 12:16:49', NULL,
                                                                               'ZC151703544821198848', 412214, NULL, 3,
  0, NULL, 1, 0, 0, 'empty_deduct_uuid', NULL, '2495a5ce-094e-4eb6-9fb9-95454b138427',
  '34c54754db1ae330e71ac4c06fc2f945', '00bfd64b58361d989ac8bf13dccc3c9b', '2018-01-19 12:16:49', '2018-01-19 12:16:49',
                                                                          0, 0, 0, 0, 0, 0, 0,
                                                                          'a240d0d3-3d18-4126-a104-34d7e90f10e4',
        '7ce70c8f-c67a-48eb-a176-44c4e9f59097', NULL, '699e94d7-4e0d-4ab0-baba-857c44dd3c8c', 0, NULL, NULL);

