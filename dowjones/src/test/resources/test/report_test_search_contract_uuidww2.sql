DELETE FROM galaxy_autotest_yunxin.asset_set;
DELETE FROM galaxy_autotest_yunxin.contract;
DELETE FROM galaxy_autotest_yunxin.financial_contract;
DELETE FROM galaxy_autotest_yunxin.company;
DELETE FROM galaxy_autotest_yunxin.customer;
DELETE FROM galaxy_autotest_yunxin.ledger_book_shelf;
DELETE FROM galaxy_autotest_yunxin.t_remittance_application;
DELETE FROM galaxy_autotest_yunxin.t_remittance_plan;


INSERT INTO `galaxy_autotest_yunxin`.`t_remittance_plan` (`id`, `remittance_plan_uuid`, `remittance_application_uuid`, `remittance_application_detail_uuid`, `business_record_no`, `financial_contract_uuid`, `financial_contract_id`, `contract_unique_id`, `contract_no`, `payment_gateway`, `payment_channel_uuid`, `payment_channel_name`, `pg_account_name`, `pg_account_no`, `pg_clearing_account`, `transaction_type`, `transaction_remark`, `priority_level`, `cp_bank_code`, `cp_bank_card_no`, `cp_bank_account_holder`, `cp_id_type`, `cp_id_number`, `cp_bank_province`, `cp_bank_city`, `cp_bank_name`, `planned_payment_date`, `complete_payment_date`, `planned_total_amount`, `actual_total_amount`, `execution_precond`, `execution_status`, `execution_remark`, `transaction_serial_no`, `create_time`, `creator_name`, `last_modified_time`, `int_field_1`, `int_field_2`, `int_field_3`, `string_field_1`, `string_field_2`, `string_field_3`, `decimal_field_1`, `decimal_field_2`, `decimal_field_3`)
VALUES (251304, '20a13290-8596-44f1-9f74-161950416d56', '55fa211c-6525-454b-bfc6-2f58c90caaac',
                '5478d4cf-5fba-4c20-8784-3e5fa8902a1e', '1', 'bf7267d0-d6f0-4905-95cf-4cb82fba4dc9', 100078,
                'b1af7496-b0d3-404f-ae8e-b1868b07c753', 'b1af7496-b0d3-404f-ae8e-b1868b07c753', 2,
                'f8bb9956-1952-4893-98c8-66683d25d7ce', 'HF4200平安银企直联', '测试专户号', '600000000001', NULL, 0, '', 1,
                                                        'C10105', '1853871252755555556', '郅志伟', 0, NULL, '410000',
                                                                                                '410100', '建设银行',
                                                                                                '2017-11-27 00:00:00',
                                                                                                '2017-11-27 16:38:33',
                                                                                                3000.00, 3000.00, NULL,
  2, '测试置成功', '52413199052599', '2017-11-27 16:38:14', 't_merchant', '2017-11-27 16:38:41', NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`t_remittance_application` (`id`, `remittance_application_uuid`, `request_no`, `financial_contract_uuid`, `financial_contract_id`, `financial_product_code`, `contract_unique_id`, `contract_no`, `planned_total_amount`, `actual_total_amount`, `auditor_name`, `audit_time`, `notify_url`, `plan_notify_number`, `actual_notify_number`, `remittance_strategy`, `remark`, `transaction_recipient`, `execution_status`, `execution_remark`, `create_time`, `creator_name`, `ip`, `last_modified_time`, `opposite_receive_date`, `remittance_id`, `total_count`, `actual_count`, `version_lock`, `check_request_no`, `check_status`, `check_retry_number`, `check_send_time`, `notify_status`, `int_field_1`, `int_field_2`, `int_field_3`, `string_field_1`, `string_field_2`, `string_field_3`, `decimal_field_1`, `decimal_field_2`, `decimal_field_3`)
VALUES (204053, '55fa211c-6525-454b-bfc6-2f58c90caaac', '8e9d175cd7084398be3120dc92109fc7',
                'bf7267d0-d6f0-4905-95cf-4cb82fba4dc9', 100078, 'WUBO123', 'b1af7496-b0d3-404f-ae8e-b1868b07c753',
                'b1af7496-b0d3-404f-ae8e-b1868b07c753', 3000.00, 3000.00, '', NULL,
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
INSERT INTO `galaxy_autotest_yunxin`.`contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, `interest_rate_cycle`, `customer_uuid`, `repaymented_periods`, `completion_status`, `date_field_one`, `long_field_one`, `long_field_two`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`)
VALUES (12397698, '463cb83c-be47-4062-8c72-ab78a4fdec19', 'b1af7496-b0d3-404f-ae8e-b1868b07c753', '2017-11-16',
                  'b1af7496-b0d3-404f-ae8e-b1868b07c753', '2099-01-01', NULL, 0.00, 2, 180447, 180581, NULL,
                                                                                                       '2017-11-16 21:50:42',
                                                                                                       0.0000000000, 0,
                                                                                                       0, 3, 2, 3000.00,
                                                                                                       0.0005000000, 1,
  NULL, 2, 'bf7267d0-d6f0-4905-95cf-4cb82fba4dc9', 1, 'f5d820b5-c280-494e-9080-8ce6fe08a705', 0, NULL, NULL, NULL, NULL,
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
VALUES (12440400, 0, 0, 1000.00, 1000.00, 0.00, 1000.00, '2017-11-16', NULL, 0.00, 0, 1, 0, '2017-11-27 00:00:57',
                                                                                      '31e384c1-16e5-49f2-9a24-84c93773ef35',
                                                                                      '2017-11-16 21:50:42',
                                                                                      '2017-11-27 00:00:57', NULL,
                                                                                      'ZC128655143134740480', 12397698,
                                                                                      NULL, 1, 2, '2017-11-23', 1, 0, 0,
                                                                                            'empty_deduct_uuid', NULL,
                                                                                            'bf7267d0-d6f0-4905-95cf-4cb82fba4dc9',
                                                                                            'a3f6f6ec71ed966fc0692d2c0e4c61d4',
  '00bfd64b58361d989ac8bf13dccc3c9b', '2017-11-16 21:50:42', '2017-11-16 21:50:42', 0, 0, 0, 2, 0, 1, 0,
        'f5d820b5-c280-494e-9080-8ce6fe08a705', '463cb83c-be47-4062-8c72-ab78a4fdec19', NULL,
        '6717faf7-49e5-403c-aad4-e5a847fbc870', 0, NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`, `version_lock`, `order_payment_status`, `repay_schedule_no`, `outer_repayment_plan_no`)
VALUES (12440401, 0, 0, 1000.00, 1000.00, 0.00, 1000.00, '2018-01-16', NULL, 0.00, 0, 1, 0, NULL,
                                                                                      '31ef98e9-4db9-44e6-93e7-ea6b60473470',
                                                                                      '2017-11-16 21:50:42',
                                                                                      '2017-11-16 21:50:42', NULL,
                                                                                      'ZC128655143168294912', 12397698,
                                                                                      NULL, 2, 0, NULL, 1, 0, 0,
                                                                                            'empty_deduct_uuid', NULL,
                                                                                            'bf7267d0-d6f0-4905-95cf-4cb82fba4dc9',
                                                                                            '04066f3fd8b2f73c50efa02582227027',
  '00bfd64b58361d989ac8bf13dccc3c9b', '2017-11-16 21:50:42', '2017-11-16 21:50:42', 0, 0, 0, 0, 0, 0, 0,
        'f5d820b5-c280-494e-9080-8ce6fe08a705', '463cb83c-be47-4062-8c72-ab78a4fdec19', NULL,
        'da6e9ea0-10d0-4673-a508-f900c69c14a0', 0, NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`, `version_lock`, `order_payment_status`, `repay_schedule_no`, `outer_repayment_plan_no`)
VALUES (12440402, 0, 0, 1000.00, 1000.00, 0.00, 1000.00, '2018-02-16', NULL, 0.00, 0, 1, 0, NULL,
                                                                                      'a99d00b0-6e3f-4fec-9170-d3e9e4642ad3',
                                                                                      '2017-11-16 21:50:42',
                                                                                      '2017-11-16 21:50:42', NULL,
                                                                                      'ZC128655143176683520', 12397698,
                                                                                      NULL, 3, 0, NULL, 1, 0, 0,
                                                                                            'empty_deduct_uuid', NULL,
                                                                                            'bf7267d0-d6f0-4905-95cf-4cb82fba4dc9',
                                                                                            'dd88cae0b477d41aecab17a2d03569d7',
  '00bfd64b58361d989ac8bf13dccc3c9b', '2017-11-16 21:50:42', '2017-11-16 21:50:42', 0, 0, 0, 0, 0, 0, 0,
        'f5d820b5-c280-494e-9080-8ce6fe08a705', '463cb83c-be47-4062-8c72-ab78a4fdec19', NULL,
        'a1a01834-eee8-433f-ae2a-81f7fac33d9f', 0, NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`company` (`id`, `address`, `full_name`, `short_name`, `uuid`, `legal_person`, `business_licence`)
VALUES (5, '杭州', 'ppd', 'ppd', '1f871f9b-7404-11e6-bf08-00163e002839', NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`, `customer_style`, `status`, `id_type`)
VALUES (180447, '320301198502169142', NULL, 'WUBO', '1268fc84-4259-4bd6-8c55-05d20547caa5', 2,
                'f5d820b5-c280-494e-9080-8ce6fe08a705', 0, 0, 0, 0);
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17943117, '3bcb9fab-47de-46a3-8462-4e151553fed2', 0.00, 1000.00, 'FST_LONGTERM_LIABILITY', '40000', 0,
                  'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL,
  '1f871f9b-7404-11e6-bf08-00163e002839', 'f5d820b5-c280-494e-9080-8ce6fe08a705', NULL, NULL, NULL,
  'a557fc65-ad6d-4eec-92e5-3fdced41408d', '2017-11-16', '2017-11-16 21:50:42', '', NULL, 12397698,
                                                                                         '463cb83c-be47-4062-8c72-ab78a4fdec19',
                                                                                         '2017-11-16 00:00:00', '',
                                                                                         '0858c2b7-85ae-4308-a61a-36661ba93710',
                                                                                         '5', 1, 'ZC128655143134740480',
                                                                                         '31e384c1-16e5-49f2-9a24-84c93773ef35',
                                                                                         NULL, NULL, NULL, NULL, '');
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17943118, '13351bf4-8b62-4fa0-9c4e-33a72fb50058', 1000.00, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, '1f871f9b-7404-11e6-bf08-00163e002839',
                                                                               'f5d820b5-c280-494e-9080-8ce6fe08a705',
                                                                               NULL, NULL, NULL,
                                                                               'a557fc65-ad6d-4eec-92e5-3fdced41408d',
                                                                               '2017-11-16', '2017-11-16 21:50:42', '',
                                                                               NULL, 12397698,
                                                                                     '463cb83c-be47-4062-8c72-ab78a4fdec19',
                                                                                     '2017-11-16 00:00:00', '',
                                                                                     '0858c2b7-85ae-4308-a61a-36661ba93710',
                                                                                     '5', 1, 'ZC128655143134740480',
                                                                                     '31e384c1-16e5-49f2-9a24-84c93773ef35',
                                                                                     NULL, NULL, NULL, NULL, '');
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17943119, '41a369c6-ab23-4d45-baa8-715d34fe034f', 0.00, 1000.00, 'FST_LONGTERM_LIABILITY', '40000', 0,
                  'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL,
  '1f871f9b-7404-11e6-bf08-00163e002839', 'f5d820b5-c280-494e-9080-8ce6fe08a705', NULL, NULL, NULL,
  'b4adf144-8dab-435a-bd5e-b9fe126115a0', '2018-01-16', '2017-11-16 21:50:42', '', NULL, 12397698,
                                                                                         '463cb83c-be47-4062-8c72-ab78a4fdec19',
                                                                                         '2018-01-16 00:00:00', '',
                                                                                         '0858c2b7-85ae-4308-a61a-36661ba93710',
                                                                                         '5', 1, 'ZC128655143168294912',
                                                                                         '31ef98e9-4db9-44e6-93e7-ea6b60473470',
                                                                                         NULL, NULL, NULL, NULL, '');
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17943120, '24458db5-34b3-44ad-a044-58bb77c2785d', 1000.00, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, '1f871f9b-7404-11e6-bf08-00163e002839',
                                                                               'f5d820b5-c280-494e-9080-8ce6fe08a705',
                                                                               NULL, NULL, NULL,
                                                                               'b4adf144-8dab-435a-bd5e-b9fe126115a0',
                                                                               '2018-01-16', '2017-11-16 21:50:42', '',
                                                                               NULL, 12397698,
                                                                                     '463cb83c-be47-4062-8c72-ab78a4fdec19',
                                                                                     '2018-01-16 00:00:00', '',
                                                                                     '0858c2b7-85ae-4308-a61a-36661ba93710',
                                                                                     '5', 1, 'ZC128655143168294912',
                                                                                     '31ef98e9-4db9-44e6-93e7-ea6b60473470',
                                                                                     NULL, NULL, NULL, NULL, '');
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17943121, 'ad0d49e6-20a4-454d-bb11-12d3235a61c3', 0.00, 1000.00, 'FST_LONGTERM_LIABILITY', '40000', 0,
                  'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL,
  '1f871f9b-7404-11e6-bf08-00163e002839', 'f5d820b5-c280-494e-9080-8ce6fe08a705', NULL, NULL, NULL,
  '938179ca-921d-410a-91e6-931450c04b3d', '2018-02-16', '2017-11-16 21:50:42', '', NULL, 12397698,
                                                                                         '463cb83c-be47-4062-8c72-ab78a4fdec19',
                                                                                         '2018-02-16 00:00:00', '',
                                                                                         '0858c2b7-85ae-4308-a61a-36661ba93710',
                                                                                         '5', 1, 'ZC128655143176683520',
                                                                                         'a99d00b0-6e3f-4fec-9170-d3e9e4642ad3',
                                                                                         NULL, NULL, NULL, NULL, '');
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17943122, '7c0118e1-2bb4-4bb9-b72c-5b8c28e18a66', 1000.00, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, '1f871f9b-7404-11e6-bf08-00163e002839',
                                                                               'f5d820b5-c280-494e-9080-8ce6fe08a705',
                                                                               NULL, NULL, NULL,
                                                                               '938179ca-921d-410a-91e6-931450c04b3d',
                                                                               '2018-02-16', '2017-11-16 21:50:42', '',
                                                                               NULL, 12397698,
                                                                                     '463cb83c-be47-4062-8c72-ab78a4fdec19',
                                                                                     '2018-02-16 00:00:00', '',
                                                                                     '0858c2b7-85ae-4308-a61a-36661ba93710',
                                                                                     '5', 1, 'ZC128655143176683520',
                                                                                     'a99d00b0-6e3f-4fec-9170-d3e9e4642ad3',
                                                                                     NULL, NULL, NULL, NULL, '');
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17943129, '1a4cc283-02af-41c4-a8e7-7c3823c66a5e', 0.00, 1000.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, '1f871f9b-7404-11e6-bf08-00163e002839',
                                                                               'f5d820b5-c280-494e-9080-8ce6fe08a705',
                                                                               NULL,
                                                                               '2d9cc8af-8735-4dc8-8024-f2fb79ecae1b',
                                                                               NULL,
                                                                               '8e6235f5-0558-42f3-9f3b-deff815b5347',
                                                                               '2017-11-16', '2017-11-16 21:50:42',
                                                                               NULL, NULL, 12397698,
                                                                                           '463cb83c-be47-4062-8c72-ab78a4fdec19',
                                                                                           '2017-11-16 00:00:00', NULL,
                                                                                           '0858c2b7-85ae-4308-a61a-36661ba93710',
                                                                                           '5', 1,
                                                                                           'ZC128655143134740480',
                                                                                           '31e384c1-16e5-49f2-9a24-84c93773ef35',
                                                                                           NULL, NULL, NULL, NULL,
        NULL);
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17943130, '2d9cc8af-8735-4dc8-8024-f2fb79ecae1b', 1000.00, 0.00, 'FST_RECIEVABLE_ASSET', '20000', 1,
                  'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_PRINCIPLE', '20000.01.01',
  '1f871f9b-7404-11e6-bf08-00163e002839', 'f5d820b5-c280-494e-9080-8ce6fe08a705', NULL, NULL,
  '1a4cc283-02af-41c4-a8e7-7c3823c66a5e', '8e6235f5-0558-42f3-9f3b-deff815b5347', '2017-11-16', '2017-11-16 21:50:42',
  NULL, NULL, 12397698, '463cb83c-be47-4062-8c72-ab78a4fdec19', '2017-11-16 00:00:00', NULL,
              '0858c2b7-85ae-4308-a61a-36661ba93710', '5', 1, 'ZC128655143134740480',
              '31e384c1-16e5-49f2-9a24-84c93773ef35', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17949208, 'e4f4a0eb-968d-459a-bbe9-330cf984b40c', 0.00, 1000.00, 'FST_RECIEVABLE_ASSET', '20000', 1,
                  'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_PRINCIPLE', '20000.01.01',
  '1f871f9b-7404-11e6-bf08-00163e002839', 'f5d820b5-c280-494e-9080-8ce6fe08a705', NULL,
  '08437bbe-5727-4219-a8d7-695a6fb1daaa', NULL, 'eeabf705-239a-4ded-a5db-b8771981f8af', '2017-11-16',
  '2017-11-20 00:00:57', NULL, NULL, 12397698, '463cb83c-be47-4062-8c72-ab78a4fdec19', '2017-11-16 00:00:00', NULL,
                                     '0858c2b7-85ae-4308-a61a-36661ba93710', '5', 1, 'ZC128655143134740480',
                                     '31e384c1-16e5-49f2-9a24-84c93773ef35', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `galaxy_autotest_yunxin`.`ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES (17949209, '08437bbe-5727-4219-a8d7-695a6fb1daaa', 1000.00, 0.00, 'FST_RECIEVABLE_ASSET', '20000', 1,
                  'SND_RECIEVABLE_OVERDUE_LOAN_ASSET', '20000.05', 'TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_PRINCIPLE',
                  '20000.05.01', '1f871f9b-7404-11e6-bf08-00163e002839', 'f5d820b5-c280-494e-9080-8ce6fe08a705', NULL,
                                 NULL, 'e4f4a0eb-968d-459a-bbe9-330cf984b40c', 'eeabf705-239a-4ded-a5db-b8771981f8af',
                                 '2017-11-16', '2017-11-20 00:00:57', NULL, NULL, 12397698,
                                                                                  '463cb83c-be47-4062-8c72-ab78a4fdec19',
                                                                                  '2017-11-16 00:00:00', NULL,
                                                                                  '0858c2b7-85ae-4308-a61a-36661ba93710',
                                                                                  '5', 1, 'ZC128655143134740480',
                                                                                  '31e384c1-16e5-49f2-9a24-84c93773ef35',
                                                                                  NULL, NULL, NULL, NULL, NULL);
