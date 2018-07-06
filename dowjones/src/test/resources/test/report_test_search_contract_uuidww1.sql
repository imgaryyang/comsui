DELETE FROM galaxy_autotest_yunxin.asset_set;
DELETE FROM galaxy_autotest_yunxin.contract;
DELETE FROM galaxy_autotest_yunxin.financial_contract;
DELETE FROM galaxy_autotest_yunxin.company;
DELETE FROM galaxy_autotest_yunxin.customer;
DELETE FROM galaxy_autotest_yunxin.ledger_book_shelf;


INSERT INTO `galaxy_autotest_yunxin`.`asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`, `version_lock`, `order_payment_status`, `repay_schedule_no`, `outer_repayment_plan_no`)
VALUES
  (12439526, 0, 0, 1500.00, 1500.00, 0.00, 1500.00, '2017-11-01', '2017-11-15', 0.00, 1, 2, 0, '2017-11-15 14:40:24',
                                                                                         '1f3da385-1db2-4f16-bfc0-3f834134d01d',
                                                                                         '2017-11-15 14:40:24',
                                                                                         '2017-11-15 14:45:33', NULL,
                                                                                         'ZC128184466302222336',
                                                                                         12397493,
                                                                                         '2017-11-15 14:42:11', 1, 2,
                                                                                                                '2017-11-06',
                                                                                                                1, 0, 0,
                                                                                                                'd65d26ae-5515-478a-b704-fe20537c8648',
                                                                                                                NULL,
                                                                                                                '28d4c296-c63d-404c-98a4-54bea5c0fa78',
                                                                                                                '563acd1b3ee07b35417a42de21568071',
    '00bfd64b58361d989ac8bf13dccc3c9b', '2017-11-15 14:40:24', '2017-11-15 14:40:24', 0, 0, 0, 2, 3, 2, 0,
   'd50103c3-4bb9-4471-97e7-3d8b8562eda1', '0c9a2325-f5cc-4f92-aec0-4a0741552051', 1,
   '238a0ca2-a7a1-413c-88fb-410b633e95af', 0, 'e432d35dc549b5f03e696934b6d74d3f', 'pufa304513');
INSERT INTO `galaxy_autotest_yunxin`.`contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, `interest_rate_cycle`, `customer_uuid`, `repaymented_periods`, `completion_status`, `date_field_one`, `long_field_one`, `long_field_two`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`)
VALUES
  (12397493, '0c9a2325-f5cc-4f92-aec0-4a0741552051', 'wenjian318', '2017-01-07', 'wenjian318', '2018-12-20', NULL, 0.00,
             3, 180241, 180377, '2017-11-15 00:00:00', '2017-11-15 14:40:24', 0.1560000000, 0, 0, 1, 2, 1500.00,
                                0.0005000000, 1, NULL, 2, '28d4c296-c63d-404c-98a4-54bea5c0fa78', 1,
                                                 'd50103c3-4bb9-4471-97e7-3d8b8562eda1', 1, 0, NULL, NULL, NULL, NULL,
   NULL, NULL, NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`, `day_of_month`, `pay_for_go`, `repurchase_principal_algorithm`, `repurchase_interest_algorithm`, `repurchase_penalty_algorithm`, `repurchase_other_charges_algorithm`, `repayment_check_days`, `repurchase_cycle`, `days_of_cycle`, `temporary_repurchases`, `allow_freewheeling_repayment`, `capital_party`, `other_party`, `contract_short_name`, `financial_type`, `remittance_object`, `asset_party`, `channel_party`, `supplier`)
VALUES (100100, 1, 0, '2017-11-07 00:00:00', 'TEST100', 'TEST100', 3, 5, 4, '2021-12-31 00:00:00', 314, 0, 3, 3, NULL,
                                                                                                        '7ccf9227-2515-43f2-a4bd-ae3e5f0d5173',
                                                                                                        '28d4c296-c63d-404c-98a4-54bea5c0fa78',
                                                                                                        1, 1, 1, 0, 0,
                                                                                                                    1,
                                                                                                                    3.00,
                                                                                                                    3.00,
                                                                                                                    3,
                                                                                                                    'null',
                                                                                                                    1,
                                                                                                                    1,
                                                                                                                    1,
                                                                                                                    2,
  '', NULL, NULL, NULL, '2017-11-07 18:39:08', '2017-11-07 21:10:59', 1, 1, '', NULL, 0, 'outstandingPrincipal', '', '',
                                                                                      '', -1, NULL, '[]', '[]', 0,
        '[3]', '[3]', '', 0, 0, NULL, NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`company` (`id`, `address`, `full_name`, `short_name`, `uuid`, `legal_person`, `business_licence`)
VALUES (5, '杭州', 'ppd', 'ppd', '1f871f9b-7404-11e6-bf08-00163e002839', NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`, `customer_style`, `status`, `id_type`)
VALUES (180241, '320301198502169142', NULL, '张文三', 'abcfd123-e651-4526-ad23-21dcafbb1d1d', 3,
                'd50103c3-4bb9-4471-97e7-3d8b8562eda1', 0, 0, 0, 0);
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17937865, '28b5ef96-66dc-435b-86bb-edb8e8aaf293', 0.00, 1500.00, 'FST_LONGTERM_LIABILITY', '40000', 0,
                  'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL,
  '1f871f9b-7404-11e6-bf08-00163e002839', 'd50103c3-4bb9-4471-97e7-3d8b8562eda1', NULL, NULL, NULL,
  '6db0a8e1-aac5-4a8f-92db-d5195931af9e', '2017-11-01', '2017-11-15 14:40:24', '', NULL, 12397493,
                                                                                         '0c9a2325-f5cc-4f92-aec0-4a0741552051',
                                                                                         '2017-11-01 00:00:00', '',
                                                                                         '7ccf9227-2515-43f2-a4bd-ae3e5f0d5173',
                                                                                         '5', 1, 'ZC128184466302222336',
                                                                                         '1f3da385-1db2-4f16-bfc0-3f834134d01d',
                                                                                         'pufa304513', NULL, NULL, NULL,
        '');
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17937866, 'd633bafe-938c-427a-9446-420036e509c1', 1500.00, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, '1f871f9b-7404-11e6-bf08-00163e002839',
                                                                               'd50103c3-4bb9-4471-97e7-3d8b8562eda1',
                                                                               NULL, NULL, NULL,
                                                                               '6db0a8e1-aac5-4a8f-92db-d5195931af9e',
                                                                               '2017-11-01', '2017-11-15 14:40:24', '',
                                                                               NULL, 12397493,
                                                                                     '0c9a2325-f5cc-4f92-aec0-4a0741552051',
                                                                                     '2017-11-01 00:00:00', '',
                                                                                     '7ccf9227-2515-43f2-a4bd-ae3e5f0d5173',
                                                                                     '5', 1, 'ZC128184466302222336',
                                                                                     '1f3da385-1db2-4f16-bfc0-3f834134d01d',
                                                                                     'pufa304513', NULL, NULL, NULL,
        '');
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17937867, '66e63624-c42c-4d24-bf00-69333b526797', 0.00, 1500.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, '1f871f9b-7404-11e6-bf08-00163e002839',
                                                                               'd50103c3-4bb9-4471-97e7-3d8b8562eda1',
                                                                               NULL,
                                                                               '5e6ae98c-b148-48d0-a615-3c4a44f4af45',
                                                                               NULL,
                                                                               '5024c606-421a-4725-abab-083caa99a884',
                                                                               '2017-11-01', '2017-11-15 14:40:24',
                                                                               NULL, NULL, 12397493,
                                                                                           '0c9a2325-f5cc-4f92-aec0-4a0741552051',
                                                                                           '2017-11-01 00:00:00', NULL,
                                                                                           '7ccf9227-2515-43f2-a4bd-ae3e5f0d5173',
                                                                                           '5', 1,
                                                                                           'ZC128184466302222336',
                                                                                           '1f3da385-1db2-4f16-bfc0-3f834134d01d',
                                                                                           'pufa304513', NULL, NULL,
        NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17937868, '5e6ae98c-b148-48d0-a615-3c4a44f4af45', 1500.00, 0.00, 'FST_RECIEVABLE_ASSET', '20000', 1,
                  'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_PRINCIPLE', '20000.01.01',
  '1f871f9b-7404-11e6-bf08-00163e002839', 'd50103c3-4bb9-4471-97e7-3d8b8562eda1', NULL, NULL,
  '66e63624-c42c-4d24-bf00-69333b526797', '5024c606-421a-4725-abab-083caa99a884', '2017-11-01', '2017-11-15 14:40:24',
  NULL, NULL, 12397493, '0c9a2325-f5cc-4f92-aec0-4a0741552051', '2017-11-01 00:00:00', NULL,
              '7ccf9227-2515-43f2-a4bd-ae3e5f0d5173', '5', 1, 'ZC128184466302222336',
              '1f3da385-1db2-4f16-bfc0-3f834134d01d', 'pufa304513', NULL, NULL, NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17937869, 'b45ffe13-98e8-4fa9-9834-01afb8d74165', 0.00, 1500.00, 'FST_RECIEVABLE_ASSET', '20000', 1,
                  'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_PRINCIPLE', '20000.01.01',
  '1f871f9b-7404-11e6-bf08-00163e002839', 'd50103c3-4bb9-4471-97e7-3d8b8562eda1', NULL,
  '832a48eb-6022-4eca-a9ef-a89871372820', NULL, '23b7c9da-dbf7-40b6-9d65-d9d3dddd88b9', '2017-11-01',
  '2017-11-15 14:40:24', NULL, NULL, 12397493, '0c9a2325-f5cc-4f92-aec0-4a0741552051', '2017-11-01 00:00:00', NULL,
                                     '7ccf9227-2515-43f2-a4bd-ae3e5f0d5173', '5', 1, 'ZC128184466302222336',
                                     '1f3da385-1db2-4f16-bfc0-3f834134d01d', 'pufa304513', NULL, NULL, NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17937870, '832a48eb-6022-4eca-a9ef-a89871372820', 1500.00, 0.00, 'FST_RECIEVABLE_ASSET', '20000', 1,
                  'SND_RECIEVABLE_OVERDUE_LOAN_ASSET', '20000.05', 'TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_PRINCIPLE',
                  '20000.05.01', '1f871f9b-7404-11e6-bf08-00163e002839', 'd50103c3-4bb9-4471-97e7-3d8b8562eda1', NULL,
                                 NULL, 'b45ffe13-98e8-4fa9-9834-01afb8d74165', '23b7c9da-dbf7-40b6-9d65-d9d3dddd88b9',
                                 '2017-11-01', '2017-11-15 14:40:24', NULL, NULL, 12397493,
                                                                                  '0c9a2325-f5cc-4f92-aec0-4a0741552051',
                                                                                  '2017-11-01 00:00:00', NULL,
                                                                                  '7ccf9227-2515-43f2-a4bd-ae3e5f0d5173',
                                                                                  '5', 1, 'ZC128184466302222336',
                                                                                  '1f3da385-1db2-4f16-bfc0-3f834134d01d',
                                                                                  'pufa304513', NULL, NULL, NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17937873, 'a811c231-f24a-47c0-a68a-30e694c95a7f', 0.00, 1500.00, 'FST_RECIEVABLE_ASSET', '20000', 1,
                  'SND_RECIEVABLE_OVERDUE_LOAN_ASSET', '20000.05', 'TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_PRINCIPLE',
                  '20000.05.01', '1f871f9b-7404-11e6-bf08-00163e002839', 'd50103c3-4bb9-4471-97e7-3d8b8562eda1', NULL,
                                 '94b14c9f-7902-41d3-ba5e-562473173c5f', NULL, '32e6fd9e-9318-4f28-8997-573e8fef52fd',
                                 '2017-11-01', '2017-11-15 14:45:33', 'd65d26ae-5515-478a-b704-fe20537c8648',
                                 '2017-11-15 14:42:11', 12397493, '0c9a2325-f5cc-4f92-aec0-4a0741552051',
                                                        '2017-11-01 00:00:00', '3bf1b0a6-41ce-41b2-b026-c90605f56b59',
                                                        '7ccf9227-2515-43f2-a4bd-ae3e5f0d5173', '5', 1,
                                                        'ZC128184466302222336', '1f3da385-1db2-4f16-bfc0-3f834134d01d',
                                                        'pufa304513', NULL, NULL, '9fd19cfe700fe5d236e93d9fd2d82f38',
        '608b774e-dd07-466a-9a48-4e452565a4fb');
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17937874, '94b14c9f-7902-41d3-ba5e-562473173c5f', 1500.00, 0.00, 'FST_BANK_SAVING', '60000', 1,
                  '001053110000001_001', '60000.001053110000001_001', 'TRD_BANK_SAVING_GENERAL_PRINCIPAL',
                  '60000.1000.01', '1f871f9b-7404-11e6-bf08-00163e002839', '', NULL, NULL,
                                   'a811c231-f24a-47c0-a68a-30e694c95a7f', '32e6fd9e-9318-4f28-8997-573e8fef52fd',
                                   '2017-11-01', '2017-11-15 14:45:33', 'd65d26ae-5515-478a-b704-fe20537c8648',
                                   '2017-11-15 14:42:11', 12397493, '0c9a2325-f5cc-4f92-aec0-4a0741552051',
                                                          '2017-11-01 00:00:00', '3bf1b0a6-41ce-41b2-b026-c90605f56b59',
                                                          '7ccf9227-2515-43f2-a4bd-ae3e5f0d5173', '5', 1,
                                                          'ZC128184466302222336',
                                                          '1f3da385-1db2-4f16-bfc0-3f834134d01d', 'pufa304513', NULL,
        NULL, '9fd19cfe700fe5d236e93d9fd2d82f38', '608b774e-dd07-466a-9a48-4e452565a4fb');

