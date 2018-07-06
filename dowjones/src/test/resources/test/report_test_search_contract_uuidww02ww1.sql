DELETE FROM galaxy_autotest_yunxin.t_remittance_plan;
DELETE FROM galaxy_autotest_yunxin.asset_set;
DELETE FROM galaxy_autotest_yunxin.contract;
DELETE FROM galaxy_autotest_yunxin.financial_contract;
DELETE FROM galaxy_autotest_yunxin.company;
DELETE FROM galaxy_autotest_yunxin.customer;
DELETE FROM galaxy_autotest_yunxin.ledger_book_shelf;
DELETE FROM galaxy_autotest_yunxin.t_remittance_application;


INSERT INTO `galaxy_autotest_yunxin`.`t_remittance_application` (`id`, `remittance_application_uuid`, `request_no`, `financial_contract_uuid`, `financial_contract_id`, `financial_product_code`, `contract_unique_id`, `contract_no`, `planned_total_amount`, `actual_total_amount`, `auditor_name`, `audit_time`, `notify_url`, `plan_notify_number`, `actual_notify_number`, `remittance_strategy`, `remark`, `transaction_recipient`, `execution_status`, `execution_remark`, `create_time`, `creator_name`, `ip`, `last_modified_time`, `opposite_receive_date`, `remittance_id`, `total_count`, `actual_count`, `version_lock`, `check_request_no`, `check_status`, `check_retry_number`, `check_send_time`, `notify_status`, `int_field_1`, `int_field_2`, `int_field_3`, `string_field_1`, `string_field_2`, `string_field_3`, `decimal_field_1`, `decimal_field_2`, `decimal_field_3`)
VALUES (204053, '55fa211c-6525-454b-bfc6-2f58c90caaac', '8e9d175cd7084398be3120dc92109fc7',
                'df4e674e-3630-4bff-a1b0-0b42ecb65f10', 100084, 'HF4200', '230efd9cd643426484c9bd1169234eaa',
                '云信信2017-1801-DK(1870130364488352295)号', 1200.00, 1200.00, '', NULL,
                                                                               'http://101.52.128.166/Loan/BatchPaidNotic',
                                                                               1, 1, 0, '', 1, 2, NULL,
                                                                               '2017-11-27 16:38:11', 't_merchant',
                                                                                                      '101.52.128.162, 120.26.102.180',
                                                                                                      '2017-11-27 16:38:41',
                                                                                                      '2017-11-27 16:38:33',
                                                                                                      NULL, 1, 1,
                                                                                                      '0b2673c8-22f7-4ba9-a5ee-1645e3787edd',
                                                                                                      'b16f90cb-f7e2-4182-9d8b-d449b3ba0553',
                                                                                                      1, 3,
                                                                                                         '2017-11-27 16:38:11',
                                                                                                         0, NULL, NULL,
                                                                                                         NULL, NULL,
                                                                                                         NULL, NULL,
                                                                                                         NULL, NULL,
        NULL);
INSERT INTO `galaxy_autotest_yunxin`.`t_remittance_plan` (`id`, `remittance_plan_uuid`, `remittance_application_uuid`, `remittance_application_detail_uuid`, `business_record_no`, `financial_contract_uuid`, `financial_contract_id`, `contract_unique_id`, `contract_no`, `payment_gateway`, `payment_channel_uuid`, `payment_channel_name`, `pg_account_name`, `pg_account_no`, `pg_clearing_account`, `transaction_type`, `transaction_remark`, `priority_level`, `cp_bank_code`, `cp_bank_card_no`, `cp_bank_account_holder`, `cp_id_type`, `cp_id_number`, `cp_bank_province`, `cp_bank_city`, `cp_bank_name`, `planned_payment_date`, `complete_payment_date`, `planned_total_amount`, `actual_total_amount`, `execution_precond`, `execution_status`, `execution_remark`, `transaction_serial_no`, `create_time`, `creator_name`, `last_modified_time`, `int_field_1`, `int_field_2`, `int_field_3`, `string_field_1`, `string_field_2`, `string_field_3`, `decimal_field_1`, `decimal_field_2`, `decimal_field_3`)
VALUES (251304, '20a13290-8596-44f1-9f74-161950416d56', '55fa211c-6525-454b-bfc6-2f58c90caaac',
                '5478d4cf-5fba-4c20-8784-3e5fa8902a1e', '1', 'df4e674e-3630-4bff-a1b0-0b42ecb65f10', 100084,
                '230efd9cd643426484c9bd1169234eaa', '云信信2017-1801-DK(1870130364488352295)号', 2,
                'f8bb9956-1952-4893-98c8-66683d25d7ce', 'HF4200平安银企直联', '测试专户号', '600000000001', NULL, 0, '', 1,
                                                        'C10105', '1853871252755555556', '郅志伟', 0, NULL, '410000',
                                                                                                '410100', '建设银行',
                                                                                                '2017-11-27 00:00:00',
                                                                                                '2017-11-27 16:38:33',
                                                                                                1200.00, 1200.00, NULL,
  2, '测试置成功', '52413199052599', '2017-11-27 16:38:14', 't_merchant', '2017-11-27 16:38:41', NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, `interest_rate_cycle`, `customer_uuid`, `repaymented_periods`, `completion_status`, `date_field_one`, `long_field_one`, `long_field_two`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`)
VALUES (12399054, 'ad884571-1ba9-403a-8cd6-82238ca212c2', '230efd9cd643426484c9bd1169234eaa', '2017-11-27',
                  '云信信2017-1801-DK(1870130364488352295)号', '2017-12-10', NULL, 0.00, 1, 181802, 181938, NULL,
                                                                                                        '2017-11-27 16:39:11',
                                                                                                        0.0200000000, 0,
                                                                                                        0, 1, 2,
                                                                                                        1200.00,
                                                                                                        0.0000000000, 1,
  NULL, 2, 'df4e674e-3630-4bff-a1b0-0b42ecb65f10', 0, '06cfbf2b-752f-451b-8360-b5c0ee1069ab', 0, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`, `day_of_month`, `pay_for_go`, `repurchase_principal_algorithm`, `repurchase_interest_algorithm`, `repurchase_penalty_algorithm`, `repurchase_other_charges_algorithm`, `repayment_check_days`, `repurchase_cycle`, `days_of_cycle`, `temporary_repurchases`, `allow_freewheeling_repayment`, `capital_party`, `other_party`, `contract_short_name`, `financial_type`, `remittance_object`, `asset_party`, `channel_party`, `supplier`)
VALUES (100084, 1, 0, '2017-10-19 00:00:00', 'HF4200', 'HF4200测试', 1, 1, 90, '2018-10-18 00:00:00', 296, 0, 1, 89, NULL,
                                                                                                         '09e184d2-177b-476e-9d5a-612b8dccac04',
                                                                                                         'df4e674e-3630-4bff-a1b0-0b42ecb65f10',
                                                                                                         1, 1, 0, 0, 0,
                                                                                                                     1,
                                                                                                                     NULL,
                                                                                                                     NULL,
                                                                                                                     0,
                                                                                                                     'null',
                                                                                                                     1,
                                                                                                                     0,
                                                                                                                     1,
                                                                                                                     0,
  '', NULL, NULL, NULL, '2017-10-19 14:35:39', NULL, 2, NULL, '', NULL, 0, '', '', '', '', -1, NULL, NULL, NULL, 0,
        '[]', '[]', '', 0, 0, NULL, NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`, `version_lock`, `order_payment_status`, `repay_schedule_no`, `outer_repayment_plan_no`)
VALUES (12444298, 0, 0, 1205.04, 1200.00, 5.04, 1205.04, '2017-12-10', NULL, 0.00, 0, 1, 0, NULL,
                                                                                      '86556b02-4358-43e3-8a81-3bddf9596105',
                                                                                      '2017-11-27 16:39:11',
                                                                                      '2017-11-27 16:39:11', NULL,
                                                                                      'ZC132563015559987200', 12399054,
                                                                                      NULL, 1, 0, NULL, 1, 0, 0,
                                                                                            'empty_deduct_uuid', NULL,
                                                                                            'df4e674e-3630-4bff-a1b0-0b42ecb65f10',
                                                                                            '9b3ac42b0413c21f4119ab682e6975be',
  'e7b6398d328aaa5a0828014d0a940c0c', '2017-11-27 16:39:11', '2017-11-27 16:39:11', 0, 0, 0, 0, 0, 0, 0,
        '06cfbf2b-752f-451b-8360-b5c0ee1069ab', 'ad884571-1ba9-403a-8cd6-82238ca212c2', NULL,
        '3e17fc3b-1185-4e98-9e52-a127394cfbbc', 0, '09bcecd0cef4e6503e7fe014efe19f7e',
        '230efd9cd643426484c9bd1169234eaa0');
INSERT INTO `galaxy_autotest_yunxin`.`company` (`id`, `address`, `full_name`, `short_name`, `uuid`, `legal_person`, `business_licence`)
VALUES (1, '上海', '测试金融公司', '测试金融', 'a02c02b9-6f98-11e6-bf08-00163e002839', NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`, `customer_style`, `status`, `id_type`)
VALUES (181802, '410181199102288537', NULL, '郅志伟', '230efd9cd643426484c9bd1169234eaa', 1,
                '06cfbf2b-752f-451b-8360-b5c0ee1069ab', 0, 0, 0, 0);
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17984427, '05974c61-969f-47c1-9832-b54c1572505f', 0.00, 1200.00, 'FST_LONGTERM_LIABILITY', '40000', 0,
                  'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL,
  'a02c02b9-6f98-11e6-bf08-00163e002839', '06cfbf2b-752f-451b-8360-b5c0ee1069ab', NULL, NULL, NULL,
  '7b5e98f6-df69-47b9-95b4-49a06ccc0a0d', '2017-12-10', '2017-11-27 16:39:12', '', NULL, 12399054,
                                                                                         'ad884571-1ba9-403a-8cd6-82238ca212c2',
                                                                                         '2017-12-10 00:00:00', '',
                                                                                         '09e184d2-177b-476e-9d5a-612b8dccac04',
                                                                                         '1', 1, 'ZC132563015559987200',
                                                                                         '86556b02-4358-43e3-8a81-3bddf9596105',
                                                                                         '230efd9cd643426484c9bd1169234eaa0',
        NULL, NULL, NULL, '');
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17984428, 'bcf7d773-de34-4446-bea8-e0f5e3188877', 0.00, 5.04, 'FST_DEFERRED_INCOME', '100000', 0,
                  'SND_DEFERRED_INCOME_INTEREST_ESTIMATE', '100000.01', NULL, NULL,
  'a02c02b9-6f98-11e6-bf08-00163e002839', '06cfbf2b-752f-451b-8360-b5c0ee1069ab', NULL, NULL, NULL,
  '7b5e98f6-df69-47b9-95b4-49a06ccc0a0d', '2017-12-10', '2017-11-27 16:39:12', '', NULL, 12399054,
                                                                                         'ad884571-1ba9-403a-8cd6-82238ca212c2',
                                                                                         '2017-12-10 00:00:00', '',
                                                                                         '09e184d2-177b-476e-9d5a-612b8dccac04',
                                                                                         '1', 1, 'ZC132563015559987200',
                                                                                         '86556b02-4358-43e3-8a81-3bddf9596105',
                                                                                         '230efd9cd643426484c9bd1169234eaa0',
        NULL, NULL, NULL, '');
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17984429, 'e8f74bd0-a4ed-4938-b2c4-aa5b8ef74f73', 1200.00, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, 'a02c02b9-6f98-11e6-bf08-00163e002839',
                                                                               '06cfbf2b-752f-451b-8360-b5c0ee1069ab',
                                                                               NULL, NULL, NULL,
                                                                               '7b5e98f6-df69-47b9-95b4-49a06ccc0a0d',
                                                                               '2017-12-10', '2017-11-27 16:39:12', '',
                                                                               NULL, 12399054,
                                                                                     'ad884571-1ba9-403a-8cd6-82238ca212c2',
                                                                                     '2017-12-10 00:00:00', '',
                                                                                     '09e184d2-177b-476e-9d5a-612b8dccac04',
                                                                                     '1', 1, 'ZC132563015559987200',
                                                                                     '86556b02-4358-43e3-8a81-3bddf9596105',
                                                                                     '230efd9cd643426484c9bd1169234eaa0',
        NULL, NULL, NULL, '');
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17984430, '79f67de7-5124-4ebc-a5b1-155f17276f3b', 5.04, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_INTEREST', '10000.01', NULL, NULL, 'a02c02b9-6f98-11e6-bf08-00163e002839',
                                                                              '06cfbf2b-752f-451b-8360-b5c0ee1069ab',
                                                                              NULL, NULL, NULL,
                                                                              '7b5e98f6-df69-47b9-95b4-49a06ccc0a0d',
                                                                              '2017-12-10', '2017-11-27 16:39:12', '',
                                                                              NULL, 12399054,
                                                                                    'ad884571-1ba9-403a-8cd6-82238ca212c2',
                                                                                    '2017-12-10 00:00:00', '',
                                                                                    '09e184d2-177b-476e-9d5a-612b8dccac04',
                                                                                    '1', 1, 'ZC132563015559987200',
                                                                                    '86556b02-4358-43e3-8a81-3bddf9596105',
                                                                                    '230efd9cd643426484c9bd1169234eaa0',
        NULL, NULL, NULL, '');
