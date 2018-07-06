DELETE FROM galaxy_autotest_yunxin.asset_set;
DELETE FROM galaxy_autotest_yunxin.contract;
DELETE FROM galaxy_autotest_yunxin.financial_contract;
DELETE FROM galaxy_autotest_yunxin.company;
DELETE FROM galaxy_autotest_yunxin.customer;
DELETE FROM galaxy_autotest_yunxin.ledger_book_shelf;

INSERT INTO `galaxy_autotest_yunxin`.`contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, `interest_rate_cycle`, `customer_uuid`, `repaymented_periods`, `completion_status`, `date_field_one`, `long_field_one`, `long_field_two`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`)
VALUES
  (12398880, '042d5e20-6c6b-43b0-ad02-253fd7c86e89', 'TESTDZZ0824_075', '2017-12-02', 'TESTDZZ0824_075', '2099-01-01',
             NULL, 0.00, 1, 181628, 181764, NULL, '2017-11-22 19:57:37', 0.1560000000, 0, 0, 1, 2, 1000.00,
                                            0.0005000000, 1, NULL, 2, '3ce00618-99e5-42ec-8c07-d60e39decde2', 1,
                                                             'df125d0b-012f-4815-80ff-427bd601ed44', 0, NULL, NULL,
                                                             NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`, `day_of_month`, `pay_for_go`, `repurchase_principal_algorithm`, `repurchase_interest_algorithm`, `repurchase_penalty_algorithm`, `repurchase_other_charges_algorithm`, `repayment_check_days`, `repurchase_cycle`, `days_of_cycle`, `temporary_repurchases`, `allow_freewheeling_repayment`, `capital_party`, `other_party`, `contract_short_name`, `financial_type`, `remittance_object`, `asset_party`, `channel_party`, `supplier`)
VALUES (100035, 3, 0, '2017-07-30 00:00:00', 'HB5000', 'HB5000测试', 1, 1, 91, '2024-07-30 00:00:00', 244, 0, 4, 90, NULL,
                                                                                                         '0d97fea9-7bc9-4257-a026-f245cc6731a2',
                                                                                                         '3ce00618-99e5-42ec-8c07-d60e39decde2',
                                                                                                         0, 0, 0, 0, 0,
                                                                                                                     0,
                                                                                                                     NULL,
                                                                                                                     NULL,
                                                                                                                     3,
                                                                                                                     'null',
                                                                                                                     1,
                                                                                                                     0,
                                                                                                                     1,
                                                                                                                     3,
  '', NULL, NULL, NULL, '2017-08-04 13:37:25', '2017-11-22 19:57:14', 2, NULL, '', NULL, 1, '', '', '', '', -1, NULL,
                                                                                         NULL, NULL, 0, NULL, NULL,
        NULL, 0, 2, NULL, NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`, `version_lock`, `order_payment_status`, `repay_schedule_no`, `outer_repayment_plan_no`)
VALUES (12443563, 0, 0, 1000.00, 1000.00, 0.00, 1000.00, '2017-11-22', NULL, 0.00, 0, 1, 0, '2017-11-27 00:03:14',
                                                                                      '4de2e8b4-9674-4a1a-b85d-677ff70e6f4a',
                                                                                      '2017-11-22 19:57:37',
                                                                                      '2017-11-27 00:03:14', NULL,
                                                                                      'ZC130801012334706688', 12398880,
                                                                                      NULL, 1, 1, NULL, 1, 0, 0,
                                                                                            'empty_deduct_uuid', NULL,
                                                                                            '3ce00618-99e5-42ec-8c07-d60e39decde2',
                                                                                            '659c63c11c959b54d8ea439629d0f154',
  'eebf2f4c926f3c347b3dd373aab7959f', '2017-11-22 19:57:37', '2017-11-22 19:57:37', 0, 0, 0, 1, 0, 1, 0,
        'df125d0b-012f-4815-80ff-427bd601ed44', '042d5e20-6c6b-43b0-ad02-253fd7c86e89', NULL,
        'f0956367-e3f8-4a09-80e1-19bd4ec49f98', 1, 'efda8d398d58454c8b7c790e1a2a241b', 'TESTDZZ0824_075DZZ0');
INSERT INTO `galaxy_autotest_yunxin`.`company` (`id`, `address`, `full_name`, `short_name`, `uuid`, `legal_person`, `business_licence`)
VALUES (1, '上海', '测试金融公司', '测试金融', 'a02c02b9-6f98-11e6-bf08-00163e002839', NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`, `customer_style`, `status`, `id_type`)
VALUES (181628, '320301198502169141', NULL, '王宝', '6cd0ffdb-1818-45c7-bf92-5c4958d33ebe', 1,
                'df125d0b-012f-4815-80ff-427bd601ed44', 0, 0, 0, 0);
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17974402, '7ea1ae23-3e9e-4813-bf0f-8f85289b6962', 0.00, 1000.00, 'FST_LONGTERM_LIABILITY', '40000', 0,
                  'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL,
  'a02c02b9-6f98-11e6-bf08-00163e002839', 'df125d0b-012f-4815-80ff-427bd601ed44', NULL, NULL, NULL,
  'ce074a3a-c5fc-41d7-b18f-924642ceedc7', '2017-11-22', '2017-11-22 19:57:37', '', NULL, 12398880,
                                                                                         '042d5e20-6c6b-43b0-ad02-253fd7c86e89',
                                                                                         '2017-11-22 00:00:00', '',
                                                                                         '0d97fea9-7bc9-4257-a026-f245cc6731a2',
                                                                                         '1', 1, 'ZC130801012334706688',
                                                                                         '4de2e8b4-9674-4a1a-b85d-677ff70e6f4a',
                                                                                         'TESTDZZ0824_075DZZ0', NULL,
        NULL, NULL, '');
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17974403, '46b4b670-4538-4b3d-864f-34f3c694c2d1', 1000.00, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, 'a02c02b9-6f98-11e6-bf08-00163e002839',
                                                                               'df125d0b-012f-4815-80ff-427bd601ed44',
                                                                               NULL, NULL, NULL,
                                                                               'ce074a3a-c5fc-41d7-b18f-924642ceedc7',
                                                                               '2017-11-22', '2017-11-22 19:57:37', '',
                                                                               NULL, 12398880,
                                                                                     '042d5e20-6c6b-43b0-ad02-253fd7c86e89',
                                                                                     '2017-11-22 00:00:00', '',
                                                                                     '0d97fea9-7bc9-4257-a026-f245cc6731a2',
                                                                                     '1', 1, 'ZC130801012334706688',
                                                                                     '4de2e8b4-9674-4a1a-b85d-677ff70e6f4a',
                                                                                     'TESTDZZ0824_075DZZ0', NULL, NULL,
        NULL, '');
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17974404, '9a3126db-643f-43c4-a062-60df470502f8', 0.00, 1000.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, 'a02c02b9-6f98-11e6-bf08-00163e002839',
                                                                               'df125d0b-012f-4815-80ff-427bd601ed44',
                                                                               NULL,
                                                                               '714ddaa6-458d-45d9-985c-60352ed89bb6',
                                                                               NULL,
                                                                               '745d9be6-6e48-4305-85dd-577312d1bd33',
                                                                               '2017-11-22', '2017-11-22 19:57:40',
                                                                               NULL, NULL, 12398880,
                                                                                           '042d5e20-6c6b-43b0-ad02-253fd7c86e89',
                                                                                           '2017-11-22 00:00:00', NULL,
                                                                                           '0d97fea9-7bc9-4257-a026-f245cc6731a2',
                                                                                           '1', 1,
                                                                                           'ZC130801012334706688',
                                                                                           '4de2e8b4-9674-4a1a-b85d-677ff70e6f4a',
                                                                                           'TESTDZZ0824_075DZZ0', NULL,
        NULL, NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17974405, '714ddaa6-458d-45d9-985c-60352ed89bb6', 1000.00, 0.00, 'FST_RECIEVABLE_ASSET', '20000', 1,
                  'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_PRINCIPLE', '20000.01.01',
  'a02c02b9-6f98-11e6-bf08-00163e002839', 'df125d0b-012f-4815-80ff-427bd601ed44', NULL, NULL,
  '9a3126db-643f-43c4-a062-60df470502f8', '745d9be6-6e48-4305-85dd-577312d1bd33', '2017-11-22', '2017-11-22 19:57:40',
  NULL, NULL, 12398880, '042d5e20-6c6b-43b0-ad02-253fd7c86e89', '2017-11-22 00:00:00', NULL,
              '0d97fea9-7bc9-4257-a026-f245cc6731a2', '1', 1, 'ZC130801012334706688',
              '4de2e8b4-9674-4a1a-b85d-677ff70e6f4a', 'TESTDZZ0824_075DZZ0', NULL, NULL, NULL, NULL);
