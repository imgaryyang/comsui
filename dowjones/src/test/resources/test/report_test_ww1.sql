DELETE FROM galaxy_autotest_yunxin.contract;
DELETE FROM galaxy_autotest_yunxin.financial_contract;
DELETE FROM galaxy_autotest_yunxin.asset_set;
DELETE FROM galaxy_autotest_yunxin.company;
DELETE FROM galaxy_autotest_yunxin.customer;
DELETE FROM galaxy_autotest_yunxin.ledger_book_shelf;

INSERT INTO `galaxy_autotest_yunxin`.`contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, `interest_rate_cycle`, `customer_uuid`, `repaymented_periods`, `completion_status`, `date_field_one`, `long_field_one`, `long_field_two`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`)
VALUES (12398918, '6274ec56-e15d-4395-a172-6d3a33c0cd51', 'd113f15a-c4f7-4087-9549-df317683e67a', '2017-11-23',
                  'd113f15a-c4f7-4087-9549-df317683e67a', '2099-01-01', NULL, 0.00, 2, 181666, 181802, NULL,
                                                                                                       '2017-11-23 02:47:06',
                                                                                                       0.1560000000, 0,
                                                                                                       0, 3, 2, 3000.00,
                                                                                                       0.0005000000, 1,
  NULL, 2, 'bf7267d0-d6f0-4905-95cf-4cb82fba4dc9', 1, '2d2513c4-9341-4786-b272-2e6d5cf071c4', 0, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`, `day_of_month`, `pay_for_go`, `repurchase_principal_algorithm`, `repurchase_interest_algorithm`, `repurchase_penalty_algorithm`, `repurchase_other_charges_algorithm`, `repayment_check_days`, `repurchase_cycle`, `days_of_cycle`, `temporary_repurchases`, `allow_freewheeling_repayment`, `capital_party`, `other_party`, `contract_short_name`, `financial_type`, `remittance_object`, `asset_party`, `channel_party`, `supplier`)
VALUES (100078, 1, 5, '2015-02-01 00:00:00', 'WUBO123', 'WUBO', 2, 5, 41, '2019-12-01 00:00:00', 290, 0, 4, 40, NULL,
                                                                                                      '0858c2b7-85ae-4308-a61a-36661ba93710',
                                                                                                      'bf7267d0-d6f0-4905-95cf-4cb82fba4dc9',
                                                                                                      0, 0, 1, 0, 0, 0,
                                                                                                                  5.00,
                                                                                                                  9.00,
                                                                                                                  3,
                                                                                                                  'null',
                                                                                                                  1, 1,
                                                                                                                  1, 3,
  '', NULL, NULL, NULL, '2017-09-22 19:17:49', '2017-11-23 02:46:54', 2, NULL, '', NULL, 0,
                                                                                         'outstandingPenaltyInterest+9',
                                                                                         'outstandingInterest+6',
                                                                                         'outstandingInterest+6',
                                                                                         '8+outstandingOverdueCharges',
                                                                                         -1, NULL, NULL, NULL, 0, '[1]',
        '[1]', 'wb123', 0, 1, NULL, NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`, `version_lock`, `order_payment_status`, `repay_schedule_no`, `outer_repayment_plan_no`)
VALUES (12443671, 0, 0, 1000.00, 1000.00, 0.00, 1000.00, '2017-12-23', NULL, 0.00, 0, 1, 0, NULL,
                                                                                      '3b575324-1a67-41ba-b71c-9f5e761e31a9',
                                                                                      '2017-11-23 02:47:07',
                                                                                      '2017-11-23 02:47:07', NULL,
                                                                                      'ZC130904063966756864', 12398918,
                                                                                      NULL, 1, 0, NULL, 1, 0, 0,
                                                                                            'empty_deduct_uuid', NULL,
                                                                                            'bf7267d0-d6f0-4905-95cf-4cb82fba4dc9',
                                                                                            '2770bf6fc9275e8e9d287ce570cbf0eb',
  '00bfd64b58361d989ac8bf13dccc3c9b', '2017-11-23 02:47:07', '2017-11-23 02:47:07', 0, 0, 0, 0, 4, 0, 0,
        '2d2513c4-9341-4786-b272-2e6d5cf071c4', '6274ec56-e15d-4395-a172-6d3a33c0cd51', NULL,
        '9b3b96ce-98eb-4f6a-88c2-855629a582f8', 0, NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`, `version_lock`, `order_payment_status`, `repay_schedule_no`, `outer_repayment_plan_no`)
VALUES (12443672, 0, 0, 1000.00, 1000.00, 0.00, 1000.00, '2018-01-23', NULL, 0.00, 0, 1, 0, NULL,
                                                                                      '0f0b207d-d876-4730-ac62-91662fbfa6cd',
                                                                                      '2017-11-23 02:47:07',
                                                                                      '2017-11-23 02:47:07', NULL,
                                                                                      'ZC130904064784646144', 12398918,
                                                                                      NULL, 2, 0, NULL, 1, 0, 0,
                                                                                            'empty_deduct_uuid', NULL,
                                                                                            'bf7267d0-d6f0-4905-95cf-4cb82fba4dc9',
                                                                                            '23cfe8728fae00ed5d51cc846857226a',
  '00bfd64b58361d989ac8bf13dccc3c9b', '2017-11-23 02:47:07', '2017-11-23 02:47:07', 0, 0, 0, 0, 0, 0, 0,
        '2d2513c4-9341-4786-b272-2e6d5cf071c4', '6274ec56-e15d-4395-a172-6d3a33c0cd51', NULL,
        '624c4ad4-a392-49bd-9a1f-d34ee2d6eee9', 0, NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`, `version_lock`, `order_payment_status`, `repay_schedule_no`, `outer_repayment_plan_no`)
VALUES (12443673, 0, 0, 1000.00, 1000.00, 0.00, 1000.00, '2018-02-23', NULL, 0.00, 0, 1, 0, NULL,
                                                                                      '4eb6bf3b-e38e-44e5-ab88-3b690f025a3a',
                                                                                      '2017-11-23 02:47:07',
                                                                                      '2017-11-23 02:47:07', NULL,
                                                                                      'ZC130904064843366400', 12398918,
                                                                                      NULL, 3, 0, NULL, 1, 0, 0,
                                                                                            'empty_deduct_uuid', NULL,
                                                                                            'bf7267d0-d6f0-4905-95cf-4cb82fba4dc9',
                                                                                            '6ee835d0892d3e0e00e08d39632ba87f',
  '00bfd64b58361d989ac8bf13dccc3c9b', '2017-11-23 02:47:07', '2017-11-23 02:47:07', 0, 0, 0, 0, 0, 0, 0,
        '2d2513c4-9341-4786-b272-2e6d5cf071c4', '6274ec56-e15d-4395-a172-6d3a33c0cd51', NULL,
        'a47e9a38-e7e8-4f68-adf1-c09d0a4a472f', 0, NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`company` (`id`, `address`, `full_name`, `short_name`, `uuid`, `legal_person`, `business_licence`)
VALUES (5, '杭州', 'ppd', 'ppd', '1f871f9b-7404-11e6-bf08-00163e002839', NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`, `customer_style`, `status`, `id_type`)
VALUES (181666, '320301198502169142', NULL, 'WUBO', '8c2970cd-aa96-4d2a-a245-3fee600da5da', 2,
                '2d2513c4-9341-4786-b272-2e6d5cf071c4', 0, 0, 0, 0);
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17975830, '067f8d62-e421-45b6-a304-c472bee636ca', 0.00, 1000.00, 'FST_LONGTERM_LIABILITY', '40000', 0,
                  'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL,
  '1f871f9b-7404-11e6-bf08-00163e002839', '2d2513c4-9341-4786-b272-2e6d5cf071c4', NULL, NULL, NULL,
  'c48e78e2-5a3d-4881-992c-c04ea3c6f28c', '2017-12-23', '2017-11-23 02:47:07', '', NULL, 12398918,
                                                                                         '6274ec56-e15d-4395-a172-6d3a33c0cd51',
                                                                                         '2017-12-23 00:00:00', '',
                                                                                         '0858c2b7-85ae-4308-a61a-36661ba93710',
                                                                                         '5', 1, 'ZC130904063966756864',
                                                                                         '3b575324-1a67-41ba-b71c-9f5e761e31a9',
                                                                                         NULL, NULL, NULL, NULL, '');
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17975831, '9073ab4a-0dd6-446e-9dcc-a20897098721', 1000.00, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, '1f871f9b-7404-11e6-bf08-00163e002839',
                                                                               '2d2513c4-9341-4786-b272-2e6d5cf071c4',
                                                                               NULL, NULL, NULL,
                                                                               'c48e78e2-5a3d-4881-992c-c04ea3c6f28c',
                                                                               '2017-12-23', '2017-11-23 02:47:07', '',
                                                                               NULL, 12398918,
                                                                                     '6274ec56-e15d-4395-a172-6d3a33c0cd51',
                                                                                     '2017-12-23 00:00:00', '',
                                                                                     '0858c2b7-85ae-4308-a61a-36661ba93710',
                                                                                     '5', 1, 'ZC130904063966756864',
                                                                                     '3b575324-1a67-41ba-b71c-9f5e761e31a9',
                                                                                     NULL, NULL, NULL, NULL, '');
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17975832, 'f6110b1f-edf5-4967-a91a-acb3be52ab62', 0.00, 1000.00, 'FST_LONGTERM_LIABILITY', '40000', 0,
                  'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL,
  '1f871f9b-7404-11e6-bf08-00163e002839', '2d2513c4-9341-4786-b272-2e6d5cf071c4', NULL, NULL, NULL,
  'f96d129a-8dfd-4a6d-a1b2-1343b8de159b', '2018-01-23', '2017-11-23 02:47:07', '', NULL, 12398918,
                                                                                         '6274ec56-e15d-4395-a172-6d3a33c0cd51',
                                                                                         '2018-01-23 00:00:00', '',
                                                                                         '0858c2b7-85ae-4308-a61a-36661ba93710',
                                                                                         '5', 1, 'ZC130904064784646144',
                                                                                         '0f0b207d-d876-4730-ac62-91662fbfa6cd',
                                                                                         NULL, NULL, NULL, NULL, '');
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17975833, '4c357955-5f3c-4932-8081-9f06c2607ded', 1000.00, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, '1f871f9b-7404-11e6-bf08-00163e002839',
                                                                               '2d2513c4-9341-4786-b272-2e6d5cf071c4',
                                                                               NULL, NULL, NULL,
                                                                               'f96d129a-8dfd-4a6d-a1b2-1343b8de159b',
                                                                               '2018-01-23', '2017-11-23 02:47:07', '',
                                                                               NULL, 12398918,
                                                                                     '6274ec56-e15d-4395-a172-6d3a33c0cd51',
                                                                                     '2018-01-23 00:00:00', '',
                                                                                     '0858c2b7-85ae-4308-a61a-36661ba93710',
                                                                                     '5', 1, 'ZC130904064784646144',
                                                                                     '0f0b207d-d876-4730-ac62-91662fbfa6cd',
                                                                                     NULL, NULL, NULL, NULL, '');
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17975834, '53bee7bd-be52-48d1-bd32-b18577dec7ad', 0.00, 1000.00, 'FST_LONGTERM_LIABILITY', '40000', 0,
                  'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL,
  '1f871f9b-7404-11e6-bf08-00163e002839', '2d2513c4-9341-4786-b272-2e6d5cf071c4', NULL, NULL, NULL,
  'e8bbed32-f8e5-4025-9a81-cd777a9ca019', '2018-02-23', '2017-11-23 02:47:07', '', NULL, 12398918,
                                                                                         '6274ec56-e15d-4395-a172-6d3a33c0cd51',
                                                                                         '2018-02-23 00:00:00', '',
                                                                                         '0858c2b7-85ae-4308-a61a-36661ba93710',
                                                                                         '5', 1, 'ZC130904064843366400',
                                                                                         '4eb6bf3b-e38e-44e5-ab88-3b690f025a3a',
                                                                                         NULL, NULL, NULL, NULL, '');
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17975835, '68cc4a3d-b5db-4af6-9529-2f0e7eb3f097', 1000.00, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, '1f871f9b-7404-11e6-bf08-00163e002839',
                                                                               '2d2513c4-9341-4786-b272-2e6d5cf071c4',
                                                                               NULL, NULL, NULL,
                                                                               'e8bbed32-f8e5-4025-9a81-cd777a9ca019',
                                                                               '2018-02-23', '2017-11-23 02:47:07', '',
                                                                               NULL, 12398918,
                                                                                     '6274ec56-e15d-4395-a172-6d3a33c0cd51',
                                                                                     '2018-02-23 00:00:00', '',
                                                                                     '0858c2b7-85ae-4308-a61a-36661ba93710',
                                                                                     '5', 1, 'ZC130904064843366400',
                                                                                     '4eb6bf3b-e38e-44e5-ab88-3b690f025a3a',
                                                                                     NULL, NULL, NULL, NULL, '');
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17976473, 'c7f3ce95-d017-11e7-9e29-502b73e44997', 0.00, 30.00, 'FST_LONGTERM_LIABILITY', '40000', 0,
                  'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL,
  '1f871f9b-7404-11e6-bf08-00163e002839', '2d2513c4-9341-4786-b272-2e6d5cf071c4', NULL, NULL, NULL,
  'c48e78e2-5a3d-4881-992c-c04ea3c6f28c', '2017-12-23', '2017-11-23 02:47:07', '', NULL, 12398918,
                                                                                         '6274ec56-e15d-4395-a172-6d3a33c0cd51',
                                                                                         '2017-12-23 00:00:00', '',
                                                                                         '0858c2b7-85ae-4308-a61a-36661ba93710',
                                                                                         '5', 1, 'ZC130904063966756864',
                                                                                         '3b575324-1a67-41ba-b71c-9f5e761e31a9',
                                                                                         NULL, NULL, NULL, NULL, '');
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17976474, 'c7fcc8bc-d017-11e7-9e29-502b73e44997', 30.00, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_INTEREST', '10000.01', NULL, NULL, '1f871f9b-7404-11e6-bf08-00163e002839',
                                                                              '2d2513c4-9341-4786-b272-2e6d5cf071c4',
                                                                              NULL, NULL, NULL,
                                                                              'c48e78e2-5a3d-4881-992c-c04ea3c6f28c',
                                                                              '2017-12-23', '2017-11-23 02:47:07', '',
                                                                              NULL, 12398918,
                                                                                    '6274ec56-e15d-4395-a172-6d3a33c0cd51',
                                                                                    '2017-12-23 00:00:00', '',
                                                                                    '0858c2b7-85ae-4308-a61a-36661ba93710',
                                                                                    '5', 1, 'ZC130904063966756864',
                                                                                    '3b575324-1a67-41ba-b71c-9f5e761e31a9',
                                                                                    NULL, NULL, NULL, NULL, '');
