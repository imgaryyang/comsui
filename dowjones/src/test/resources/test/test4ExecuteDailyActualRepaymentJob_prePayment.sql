DELETE FROM `asset_set`;
DELETE FROM `ledger_book_shelf`;
DELETE FROM `financial_contract`;
DELETE FROM `app`;
DELETE FROM `company`;
DELETE FROM `account`;
DELETE FROM `payment_channel`;
DELETE FROM `quartz_job`;
DELETE FROM `daily_pre_repayment`;
DELETE FROM `daily_part_repayment`;

INSERT INTO `financial_contract` (`asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`,
                                  `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`,
                                  `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`,
                                  `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`,
                                  `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`,
                                  `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`,
                                  `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`,
                                  `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`,
                                  `repurchase_algorithm`, `day_of_month`, `pay_for_go`, `repurchase_principal_algorithm`, `repurchase_interest_algorithm`,
                                  `repurchase_penalty_algorithm`, `repurchase_other_charges_algorithm`, `repayment_check_days`, `repurchase_cycle`,
                                  `days_of_cycle`, `temporary_repurchases`, `allow_freewheeling_repayment`, `capital_party`, `other_party`,
                                  `contract_short_name`, `remittance_object`, `financial_type`)
VALUES ('1', '0', '2016-09-01 00:00:00', 'G31700', '拍拍贷测试', '3', '5', '12', '2017-08-31 00:00:00',
             '5', '0', '1', '11', '1', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', 'd2812bc5-5057-4a91-b3fd-9019506f0499',
                       '0', '0',
                       '1', '1', '1', '0', NULL, NULL, '3', 'null', '1', '1', '1', '0', '', NULL, NULL, NULL, NULL,
                                                                                            '2017-10-11 15:21:13',
                                                                                            '1', '1',
                                                                                            'outstandingPrincipal+outstandingInterest+outstandingPenaltyInterest',
                                                                                            '6', '0',
  'outstandingPrincipal',
  'outstandingInterest', 'outstandingPenaltyInterest', '', '7', NULL, '[]', '[]', '1', NULL, NULL, NULL, NULL, '0');

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`, `create_time`, `last_modify_time`)
VALUES ('3', 'ppd', NULL, b'1', NULL, '测试商户ppd', '5', NULL, NULL, NULL);

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`, `legal_person`, `business_licence`)
VALUES ('5', '杭州', 'ppd', 'ppd', '1f871f9b-7404-11e6-bf08-00163e002839', NULL, NULL);

INSERT INTO `account` (`id`, `account_name`, `account_no`, `bank_name`, `alias`, `attr`, `scan_cash_flow_switch`, `usb_key_configured`, `uuid`, `bank_code`, `cash_flow_config`)
VALUES ('5', '银企直连专用账户9', '591902896710201', '工商银行', NULL,
             '{\"bankBranchNo\":\"59\",\"usbUuid\":\"b173f24a-3c27-4243-85b7-e93ef6a128ac\"}', '', '', 'uuid_5',
             'PAB', '{\"nextPageNo\":\"1\"}');

INSERT INTO `payment_channel` (`id`, `channel_name`, `user_name`, `user_password`, `merchant_id`, `cer_file_path`, `pfx_file_path`, `pfx_file_key`, `payment_channel_type`, `api_url`, `from_date`, `thru_date`)
VALUES ('1', '测试通道1', 'operator', 'operator', '001053110000001',
             '/data/webapps/tomcat-earth/webapps/earth/WEB-INF/classes/certificate/gzdsf.cer',
             '/data/webapps/tomcat-earth/webapps/earth/WEB-INF/classes/certificate/ORA@TEST1.pfx', '123456', '0',
             'http://59.41.103.98:333/gzdsf/ProcessServlet', NULL, NULL);

INSERT INTO `asset_set` (`guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`, `version_lock`, `order_payment_status`, `repay_schedule_no`, `outer_repayment_plan_no`)
VALUES ('0', '0', '561.56', '459.84', '101.72', '561.56', '2017-05-27', '2016-09-14', '0.00', '1', '2', '1', NULL,
                                                                                                        '6c531afa-d773-44bc-9a7a-81fcfcb9bf20',
                                                                                                        '2016-09-12 11:55:01',
                                                                                                        '2016-09-14 16:40:52',
                                                                                                        NULL,
                                                                                                        'ZC274906E26AB28374',
                                                                                                        '7938',
                                                                                                        '2017-05-16 16:40:52',
                                                                                                        '2', '0', NULL,
                                                                                                             '2', '0',
                                                                                                             '0',
                                                                                                             'empty_deduct_uuid',
                                                                                                             NULL,
                                                                                                             'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                                             NULL, NULL,
  NULL, NULL, '1', '0', '0', '1', '0', '2', NULL, 'bf453048-d24c-4188-8fa8-55fd5c0d5ac6',
        'de0f3e5a-ed6c-4e89-a98b-1df4a151133d', '0', '1d57fd11-6650-11e7-bff1-00163e002839', '0', NULL, NULL);
INSERT INTO `asset_set` (`guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`, `version_lock`, `order_payment_status`, `repay_schedule_no`, `outer_repayment_plan_no`)
VALUES ('0', '0', '1123.08', '1000.00', '123.08', '1123.08', '2017-07-08', '2016-09-14', '0.00', '1', '2', '1', NULL,
                                                                                                           '200cd1cb-1023-439b-937d-39f20028dcc4',
                                                                                                           '2016-09-14 13:54:06',
                                                                                                           '2016-09-14 16:40:52',
                                                                                                           NULL,
                                                                                                           'ZC2749281223E72910',
                                                                                                           '7924',
                                                                                                           '2017-05-16 16:40:52',
                                                                                                           '1', '0',
                                                                                                                NULL,
                                                                                                                '4',
                                                                                                                '0',
                                                                                                                '0',
                                                                                                                'empty_deduct_uuid',
                                                                                                                NULL,
                                                                                                                'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                                                NULL,
                                                                                                                NULL,
  NULL, NULL, '1', '0', '0', '1', '0', '2', NULL, 'd81f0a64-2b00-4721-939b-616e25bd1bcf',
        '45b31a64-ad3c-434d-8a8a-6a43f9c2c87d', '1', '1d598176-6650-11e7-bff1-00163e002839', '0', NULL, NULL);
INSERT INTO `asset_set` (`guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`, `version_lock`, `order_payment_status`, `repay_schedule_no`, `outer_repayment_plan_no`)
VALUES ('0', '0', '526.57', '481.57', '45.00', '526.57', '2017-01-08', '2016-09-18', '0.00', '0', '3', '1', NULL,
                                                                                                       '6472c4ba-8f4e-49c8-94aa-089cdb487097',
                                                                                                       '2016-09-18 11:30:01',
                                                                                                       '2016-09-18 11:40:29',
                                                                                                       NULL,
                                                                                                       'ZC274966BC441E07F6',
                                                                                                       '7993',
                                                                                                       '2017-05-16 11:40:29',
                                                                                                       '1', '0', NULL,
                                                                                                            '2', '0',
                                                                                                            '0',
                                                                                                            'empty_deduct_uuid',
                                                                                                            NULL,
                                                                                                            'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                                            NULL, NULL,
  NULL, NULL, '1', '0', '0', '1', '0', '2', NULL, '88c0b32f-c0b9-47af-b5e9-7ad6bcea0ae8',
        '0c2fafb2-371b-448c-8160-89b00932170c', '0', '1d5ab4dc-6650-11e7-bff1-00163e002839', '0', NULL, NULL);


INSERT INTO `ledger_book_shelf` (`ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES ('4adabb19-dc82-4b1d-a447-d0659049d612', '1000.00', '1.00', 'FST_LONGTERM_LIABILITY', '40000', '0',
                                                'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01',
                                                'TRD_BANK_SAVING_GENERAL_PRINCIPAL', NULL,
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  'c262636a-8905-4115-a3d5-06f042a0da3e', NULL, NULL, NULL, 'e99c3dbc-8efb-4af8-9c2b-3e956483a57b', '2017-09-20',
  '2017-09-20 14:55:23', '', NULL, '12384259', '124b2149-d164-4dfd-bda8-1b46c4e89e35', '2017-09-20 00:00:00',
                                               '1f3d4429-0a4a-4e0b-bad1-bcd76d5769d3',
                                               '74a9ce4d-cafc-407d-b013-987077541bdc', '1', '1', 'ZC107894517445812224',
                                               '6472c4ba-8f4e-49c8-94aa-089cdb487097', 'fgz123wqeqw', NULL, NULL, NULL,
        '');
INSERT INTO `ledger_book_shelf` (`ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES ('464d4066-068c-4ce9-b558-f639ea78cd19', '1000.00', '0.00', 'FST_UNEARNED_LOAN_ASSET', '10000', '1',
                                                'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02',
                                                'TRD_BANK_SAVING_GENERAL_INTEREST', NULL,
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  'c262636a-8905-4115-a3d5-06f042a0da3e', NULL, NULL, NULL, 'e99c3dbc-8efb-4af8-9c2b-3e956483a57b', '2017-09-20',
  '2017-09-20 14:55:23', '', NULL, '12384259', '124b2149-d164-4dfd-bda8-1b46c4e89e35', '2017-09-20 00:00:00',
                                               '1f3d4429-0a4a-4e0b-bad1-bcd76d5769d3',
                                               '74a9ce4d-cafc-407d-b013-987077541bdc', '1', '1', 'ZC107894517445812224',
                                               '6472c4ba-8f4e-49c8-94aa-089cdb487097', 'fgz123wqeqw', NULL, NULL, NULL,
        '');
INSERT INTO `ledger_book_shelf` (`ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES ('1671836a-e7d7-493f-80f3-041c0248dfc9', '200.00', '10.00', 'FST_LONGTERM_LIABILITY', '40000', '0',
                                                'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01',
                                                'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_LOAN_SERVICE_FEE',
                                                NULL, 'a02c02b9-6f98-11e6-bf08-00163e002839',
  'f8ac2c8d-a688-44e9-97b8-da5d3e44d129', NULL, NULL, NULL, 'd1fbc225-df3c-40bc-af7d-3ee0eb0929ac', '2017-06-09',
  '2017-10-11 15:21:40', '', NULL, '12384260', 'b31c1fd6-3078-4dc0-841a-307b3412915d', '2017-06-09 00:00:00',
                                               '1f3d4429-0a4a-4e0b-bad1-bcd76d5769d3',
                                               '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', '1', 'ZC115511275567513600',
                                               '6472c4ba-8f4e-49c8-94aa-089cdb487097', 'xwq122', NULL, NULL, NULL, '');
INSERT INTO `ledger_book_shelf` (`ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES ('d3c7c0d1-c451-40b1-9953-3af86cd9fc16', '994.00', '0.00', 'FST_UNEARNED_LOAN_ASSET', '10000', '1',
                                                'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02',
                                                'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_TECH_FEE', NULL,
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  '477f23f3-4c2a-4022-8f09-4121517a2504', NULL, NULL, NULL, '4bb4d96a-2959-42a2-a18c-ad5bfbd60481', '2017-05-20',
  '2017-08-11 14:25:02', '', NULL, '12384257', '2fcd2ec2-a58e-43e9-83c1-6562c3f9569d', '2017-05-20 00:00:00',
                                               '6892dce4-be0f-4453-81ce-1206715cb2b6',
                                               '74a9ce4d-cafc-407d-b013-987077541bdc', '1', '1', 'ZC93391366567026688',
                                               '200cd1cb-1023-439b-937d-39f20028dcc4', NULL, NULL, NULL, NULL, '');
INSERT INTO `ledger_book_shelf` (`ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES ('1249836d-53ea-46ea-851f-f368c6726708', '112.00', '100.00', 'FST_LONGTERM_LIABILITY', '40000', '0',
                                                'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01',
                                                'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OTHER_FEE', NULL,
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  '2dfdb833-d1a8-4ff1-ac51-a081e0e9ffc4', NULL, NULL, NULL, 'fdd98e61-957f-4304-a42f-5dec3f87b693', '2017-09-20',
  '2017-09-20 14:52:57', '', NULL, '12384258', 'b07b0a38-74d6-4871-acf2-5a7f1ae89ab8', '2017-09-20 00:00:00',
                                               '6892dce4-be0f-4453-81ce-1206715cb2b6',
                                               '74a9ce4d-cafc-407d-b013-987077541bdc', '1', '1', 'ZC107893904188235776',
                                               '200cd1cb-1023-439b-937d-39f20028dcc4', 'fgz123wqeqw', NULL, NULL, NULL,
        '');
INSERT INTO `ledger_book_shelf` (`ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES ('bfa2efa1-c605-4e37-9157-ee47c3991034', '1000.00', '0.00', 'FST_UNEARNED_LOAN_ASSET', '10000', '1',
                                                'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02',
                                                'TRD_BANK_SAVING_GENERAL_PRINCIPAL', NULL,
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  '2dfdb833-d1a8-4ff1-ac51-a081e0e9ffc4', NULL, NULL, NULL, 'fdd98e61-957f-4304-a42f-5dec3f87b693', '2017-09-20',
  '2017-09-20 14:52:57', '', NULL, '12384258', 'b07b0a38-74d6-4871-acf2-5a7f1ae89ab8', '2017-09-20 00:00:00',
                                               '6892dce4-be0f-4453-81ce-1206715cb2b6',
                                               '74a9ce4d-cafc-407d-b013-987077541bdc', '1', '1', 'ZC107893904188235776',
                                               '200cd1cb-1023-439b-937d-39f20028dcc4', 'fgz123wqeqw', NULL, NULL, NULL,
        '');
INSERT INTO `ledger_book_shelf` (`ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES ('22d7cd20-5047-47dd-a5c6-1bd80be1499d', '110.00', '54.00', 'FST_LONGTERM_LIABILITY', '40000', '0',
                                                'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01',
                                                'TRD_BANK_SAVING_GENERAL_PRINCIPAL', NULL,
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  'b074faf2-a3e8-41c8-adc4-17f66b4bea36', NULL, NULL, NULL, '4beee4eb-f93a-422c-b57a-1f6bd6b504b4', '2017-05-20',
  '2017-08-11 14:17:38', '', NULL, '12384256', 'a340ca30-af49-42aa-86a7-c207527d9542', '2017-05-20 00:00:00',
                                               '7851ef08-3f2a-4d97-9ecc-f41da6922c82',
                                               '74a9ce4d-cafc-407d-b013-987077541bdc', '1', '1', 'ZC93389501829799936',
                                               '6c531afa-d773-44bc-9a7a-81fcfcb9bf20', NULL, NULL, NULL, NULL, '');
INSERT INTO `ledger_book_shelf` (`ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES ('65386f6c-039b-4ce5-a9c4-26a3f36ab27a', '464.00', '0.00', 'FST_UNEARNED_LOAN_ASSET', '10000', '1',
                                                'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02',
                                                'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY', NULL,
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  'b074faf2-a3e8-41c8-adc4-17f66b4bea36', NULL, NULL, NULL, '4beee4eb-f93a-422c-b57a-1f6bd6b504b4', '2017-05-20',
  '2017-08-11 14:17:38', '', NULL, '12384256', 'a340ca30-af49-42aa-86a7-c207527d9542', '2017-05-20 00:00:00',
                                               '7851ef08-3f2a-4d97-9ecc-f41da6922c82',
                                               '74a9ce4d-cafc-407d-b013-987077541bdc', '1', '1', 'ZC93389501829799936',
                                               '6c531afa-d773-44bc-9a7a-81fcfcb9bf20', NULL, NULL, NULL, NULL, '');
INSERT INTO `ledger_book_shelf` (`ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES ('19b63c16-ed9c-4c98-a5ad-f7a40000574f', '855.00', '234.00', 'FST_LONGTERM_LIABILITY', '40000', '0',
                                                'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01',
                                                'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION', NULL,
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  '477f23f3-4c2a-4022-8f09-4121517a2504', NULL, NULL, NULL, '4bb4d96a-2959-42a2-a18c-ad5bfbd60481', '2017-05-20',
  '2017-08-11 14:25:02', '', NULL, '12384257', '2fcd2ec2-a58e-43e9-83c1-6562c3f9569d', '2017-05-20 00:00:00',
                                               '7851ef08-3f2a-4d97-9ecc-f41da6922c82',
                                               '74a9ce4d-cafc-407d-b013-987077541bdc', '1', '1', 'ZC93391366567026688',
                                               '6c531afa-d773-44bc-9a7a-81fcfcb9bf20', NULL, NULL, NULL, NULL, '');
