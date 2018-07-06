SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM `asset_set`;
DELETE FROM `financial_contract`;
DELETE FROM `contract`;
DELETE FROM `asset_valuation_detail`;
DELETE FROM `ledger_book_shelf`;
DELETE FROM `payment_channel`;
DELETE FROM `customer`;
DELETE FROM `app`;
DELETE FROM `company`;
DELETE FROM `house`;
DELETE FROM `ledger_book`;
DELETE FROM `asset_package`;
DELETE FROM `loan_batch`;
DELETE FROM `virtual_account`;
DELETE FROM `virtual_account_flow`;
DELETE FROM `source_document_detail`;
DELETE FROM `source_document`;
DELETE FROM `journal_voucher`;
DELETE FROM `business_voucher`;
DELETE FROM `rent_order`;
DELETE FROM `cash_flow`;
DELETE FROM `account`;
DELETE FROM `virtual_account_payment_black_list`;
DELETE FROM `app_account`;
DELETE FROM `repurchase_doc`;
DELETE FROM `company`;
DELETE FROM `t_deduct_application`;
DELETE FROM `t_deduct_application_detail`;

INSERT INTO `account` (`id`, `account_name`, `account_no`, `bank_name`, `alias`, `attr`, `scan_cash_flow_switch`, `usb_key_configured`, `uuid`, `bank_code`)
VALUES
  ('1', '云南信托国际有限公司', '600000000001', '云南国际信托有限公司', NULL, NULL, '\0', '\0', '9d2c6b35-9e0a-447f-bd76-42a8634f8c3e',
   NULL);


INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`)
VALUES
  ('1', 'anmeitu', '11111db75ebb24fa0993f4fa25775023', '\0', 'http://e.zufangbao.cn', '安美途', '1', NULL);

INSERT INTO `payment_channel` (`id`, `channel_name`, `user_name`, `user_password`, `merchant_id`, `cer_file_path`, `pfx_file_path`, `pfx_file_key`, `payment_channel_type`, `api_url`, `from_date`, `thru_date`)
VALUES
  ('1', '广东银联', 'user1', '123456', '123', NULL, NULL, NULL, '0', NULL, NULL, NULL);


INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`)
VALUES
  ('1', '上海', '云南国际信托有限公司', '云南信托', 'companyuuid');


INSERT INTO `journal_voucher` (`id`, `account_side`, `billing_plan_uuid`, `booking_amount`, `checking_level`, `company_id`, `journal_voucher_uuid`, `source_document_amount`, `source_document_identity`, `source_document_uuid`, `status`, `source_document_counter_party_account`, `source_document_counter_party_name`, `issued_time`, `journal_voucher_type`, `counter_account_type`, `related_bill_contract_info_lv_1`, `related_bill_contract_info_lv_2`, `related_bill_contract_info_lv_3`, `cash_flow_account_info`, `journal_voucher_no`, `related_bill_contract_no_lv_1`, `related_bill_contract_no_lv_2`, `related_bill_contract_no_lv_3`, `related_bill_contract_no_lv_4`, `source_document_no`, `appendix`, `last_modified_time`)
VALUES
  ('1', '1', 'asset_uuid_1', '1090.00', '0', '1', 'journal_voucher_uuid_1', '1090.00', 'source_document_uuid_1',
        'source_document_detail_uuid_1', '1', '', '', '2016-10-20 16:57:12', '7', '1', 'financial_contract_uuid_1',
                                              'contract_uuid_2', 'asset_uuid_1', '', 'ZF274FC9E08F5A715B', '云南信托',
   'DKHD-001', 'repayment_plan_no_1', 'order_no_1', 'source_document_no', NULL, '2016-10-20 16:57:12');


INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`)
VALUES
  ('1', 'contract_uuid', 'unique_id', '2016-01-02', 'G00003(zht36853461685090410)', '2018-01-01', NULL, '0.00', '1',
        '1', '1', NULL, '2016-08-27 16:06:53', '0.1200000000', '0', '0', '2', '2', '0.02', '11.0000000000', '1', NULL,
   '2', 'financial_contract_uuid');


INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`, `day_of_month`, `pay_for_go`, `repurchase_principal_algorithm`, `repurchase_interest_algorithm`, `repurchase_penalty_algorithm`, `repurchase_other_charges_algorithm`, `repayment_check_days`, `allow_freewheeling_repayment`, `repurchase_cycle`, `days_of_cycle`, `temporary_repurchases`)
VALUES
  ('1', '0', '1', '2016-09-01 00:00:00', 'G31700', '拍拍贷测试', '3', '1', '60', '2017-08-31 00:00:00', '1', '0', '1', '2',
                                                                                                        '1',
                                                                                                        'ledger_book_no',
                                                                                                        'financial_contract_uuid',
                                                                                                        '0', '0', '0',
                                                                                                        '1', '1', '1',
                                                                                                             NULL, NULL,
                                                                                                             NULL, NULL,
                                                                                                             '0', '0',
                                                                                                             '0', '0',
    NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'outstandingPrincipal', 'outstandingInterest',
                                                                'outstandingPenaltyInterest', NULL, '-1', '0', NULL,
                                                                NULL, NULL);


INSERT INTO `ledger_book` (`id`, `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`)
VALUES
  ('1', 'ledger_book_no', '1', NULL, NULL);

INSERT INTO `house` (`id`, `address`, `app_id`) VALUES ('1', NULL, '1');


INSERT INTO `ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES
  ('1', '18e374c1-ba80-47ae-ab41-b9ac85c90d2e', '900.00', '0.00', 'FST_BANK_SAVING', '60000', '1', '000191400205800',
        '60000.01', 'TRD_BANK_SAVING_GENERAL_PRINCIPAL', '60000.1000.01', 'q', '', NULL, NULL,
                                                                          'f8dcf931-5105-4e74-9b85-d63a90993f54',
                                                                          '5ead8899-f25b-4b6e-8273-c8b84c53add5',
                                                                          '2016-05-01', '2017-06-05 18:13:21', '', NULL,
    '7', NULL, '2016-05-01 00:00:00', 'journal_voucher_uuid_1', 'ledger_book_no', '1', '1', 'repayment_plan_no_1',
    'asset_uuid_1', NULL, NULL, NULL, NULL, 'source_document_detail_uuid_1'),
  ('2', 'e644bfd5-aba0-4e40-966d-24e4451f7e9f', '100.00', '0.00', 'FST_BANK_SAVING', '60000', '1', '000191400205800',
        '60000.01', 'TRD_BANK_SAVING_GENERAL_INTEREST', '60000.1000.02', 'q', '', NULL, NULL,
                                                                         '50422297-281c-4af4-b4b4-ca24f18719b0',
                                                                         '33f9a263-3b26-4c83-a60a-22cfb9d118cb',
                                                                         '2016-05-01', '2017-06-05 18:13:21', '', NULL,
    '7', NULL, '2016-05-01 00:00:00', 'journal_voucher_uuid_1', 'ledger_book_no', '1', '1', 'repayment_plan_no_1',
    'asset_uuid_1', NULL, NULL, NULL, NULL, 'source_document_detail_uuid_1'),
  ('3', '8ea4ea32-db93-4aa4-be86-0b7737c62168', '30.00', '0.00', 'FST_BANK_SAVING', '60000', '1', '000191400205800',
        '60000.01', 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_TECH_FEE', '60000.1000.04', 'q', '', NULL, NULL,
                                                                                    '5253446b-e8bc-440d-9a61-3248ab6a3205',
                                                                                    '6733c199-c392-4652-be70-9aba8815bf2a',
                                                                                    '2016-05-01', '2017-06-05 18:13:21',
                                                                                    '', NULL, '7', NULL,
                                                                                              '2016-05-01 00:00:00',
                                                                                              'journal_voucher_uuid_1',
                                                                                              'ledger_book_no', '1',
                                                                                              '1',
                                                                                              'repayment_plan_no_1',
                                                                                              'asset_uuid_1', NULL,
   NULL, NULL, NULL, 'source_document_detail_uuid_1'),
  ('4', 'bb66ab01-d36e-4ba8-97ea-4e5405736b17', '10.00', '0.00', 'FST_BANK_SAVING', '60000', '1', '000191400205800',
        '60000.01', 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION', '60000.1000.07', 'q', '', NULL, NULL,
                                                                                                  'edfb991f-5e4e-4efd-bcd9-9bbee2690397',
                                                                                                  'fb1aa675-5691-4a98-97d7-80c6377a5f4f',
                                                                                                  '2016-05-01',
                                                                                                  '2017-06-05 18:13:21',
                                                                                                  '', NULL, '7', NULL,
                                                                                                            '2016-05-01 00:00:00',
                                                                                                            'journal_voucher_uuid_1',
                                                                                                            'ledger_book_no',
                                                                                                            '1', '1',
                                                                                                            'repayment_plan_no_1',
                                                                                                            'asset_uuid_1',
                                                                                                            NULL, NULL,
   NULL, NULL, 'source_document_detail_uuid_1'),
  ('5', '2393c18c-d1ea-483c-9c03-9311ed4a95ec', '10.00', '0.00', 'FST_BANK_SAVING', '60000', '1', '000191400205800',
        '60000.01', 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE', '60000.1000.08', 'q', '', NULL, NULL,
                                                                                                   'b588b383-ffa7-43b8-bee2-5f2dbb0ac2f9',
                                                                                                   '511d7c95-4f18-464d-9235-5c810aa3f669',
                                                                                                   '2016-05-01',
                                                                                                   '2017-06-05 18:13:21',
                                                                                                   '', NULL, '7', NULL,
                                                                                                             '2016-05-01 00:00:00',
                                                                                                             'journal_voucher_uuid_1',
                                                                                                             'ledger_book_no',
                                                                                                             '1', '1',
                                                                                                             'repayment_plan_no_1',
                                                                                                             'asset_uuid_1',
                                                                                                             NULL, NULL,
   NULL, NULL, 'source_document_detail_uuid_1'),
  ('6', '70ea0fec-35e2-4bc8-adc9-88cd37118c20', '10.00', '0.00', 'FST_BANK_SAVING', '60000', '1', '000191400205800',
        '60000.01', 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY', '60000.1000.06', 'q', '', NULL, NULL,
                                                                                   'fee7f81e-c196-4d1e-b5e5-444bce087d9e',
                                                                                   '7671e343-9d59-4b87-baee-34cd0b7dbfbe',
                                                                                   '2016-05-01', '2017-06-05 18:13:21',
                                                                                   '', NULL, '7', NULL,
                                                                                             '2016-05-01 00:00:00',
                                                                                             'journal_voucher_uuid_1',
                                                                                             'ledger_book_no', '1', '1',
                                                                                             'repayment_plan_no_1',
                                                                                             'asset_uuid_1', NULL, NULL,
   NULL, NULL, 'source_document_detail_uuid_1'),
  ('7', '67894864-785b-493e-bf70-0003a242ffc6', '10.00', '0.00', 'FST_BANK_SAVING', '60000', '1', '000191400205800',
        '60000.01', 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE', '60000.1000.09', 'q', '', NULL, NULL,
                                                                                                 '9edbcf0e-a8f9-403a-9688-523922b69b17',
                                                                                                 'ffa0a516-2d3a-4800-ba71-5c7e9cc4e20f',
                                                                                                 '2016-05-01',
                                                                                                 '2017-06-05 18:13:21',
                                                                                                 '', NULL, '7', NULL,
                                                                                                           '2016-05-01 00:00:00',
                                                                                                           'journal_voucher_uuid_1',
                                                                                                           'ledger_book_no',
                                                                                                           '1', '1',
                                                                                                           'repayment_plan_no_1',
                                                                                                           'asset_uuid_1',
                                                                                                           NULL, NULL,
   NULL, NULL, 'source_document_detail_uuid_1'),
  ('8', '695dd41c-2878-4765-9faa-25f3e82eea2b', '20.00', '0.00', 'FST_BANK_SAVING', '60000', '1', '000191400205800',
        '60000.01', 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_LOAN_SERVICE_FEE', '60000.1000.03', 'q', '', NULL, NULL,
                                                                                            '0992b8dc-74e5-45b2-83e7-fe3ef84071ef',
                                                                                            'a7c21c59-096c-4425-b712-3ad61fc09190',
                                                                                            '2016-05-01',
                                                                                            '2017-06-05 18:13:21', '',
                                                                                            NULL, '7', NULL,
                                                                                                  '2016-05-01 00:00:00',
                                                                                                  'journal_voucher_uuid_1',
                                                                                                  'ledger_book_no', '1',
                                                                                                  '1',
                                                                                                  'repayment_plan_no_1',
                                                                                                  'asset_uuid_1', NULL,
   NULL, NULL, NULL, 'source_document_detail_uuid_1');


INSERT INTO `asset_set` (`id`, `actual_recycle_date`, `asset_fair_value`, `asset_initial_value`, `asset_interest_value`, `asset_principal_value`, `asset_recycle_date`, `asset_status`, `asset_uuid`, `create_time`, `current_period`, `guarantee_status`, `last_modified_time`, `last_valuation_time`, `on_account_status`, `settlement_status`, `single_loan_contract_no`, `contract_id`, `version_no`, `active_status`, `financial_contract_uuid`, `plan_type`)
VALUES
  ('1', NULL, '1000.00', '1000.00', '100.00', '900.00', '2016-05-01', '0', 'asset_uuid_1', '2016-05-16 14:26:50', '1',
    '0', NULL, NULL, '1', '0', 'repayment_plan_no_1', '1', 1, 0, 'financial_contract_uuid_1', '0'),
  ('2', NULL, '500.00', '500.00', '200.00', '800.00', '2016-05-05', '0', 'asset_uuid_2', '2016-05-16 14:26:50', '2',
    '0', NULL, NULL, '1', '0', 'repayment_plan_no_2', '1', 1, 0, 'financial_contract_uuid_1', '0');


INSERT INTO `source_document` (`id`, `company_id`, `source_document_uuid`, `source_document_type`, `create_time`, `source_document_status`, `source_account_side`, `booking_amount`, `outlier_document_uuid`, `outlier_trade_time`, `outlier_counter_party_account`, `outlier_counter_party_name`, `outlier_account`, `outlier_company_id`, `outlier_serial_global_identity`, `outlier_memo`, `outlier_amount`, `outlier_settlement_modes`, `outlier_breif`, `outlier_account_side`, `appendix`, `first_outlier_doc_type`, `second_outlier_doc_type`, `third_outlier_doc_type`, `currency_type`, `audit_status`, `first_party_id`, `second_party_id`, `virtual_account_uuid`, `first_account_id`, `second_account_id`, `excute_status`, `excute_result`, `financial_contract_uuid`, `source_document_no`)
VALUES
  ('1', '1', 'source_document_uuid_1', '1', '2016-08-24 18:58:23', '1', '1', '1090.00', 'deduct_application_uuid_1',
        '2016-08-24 16:56:18', 'account_account_no', 'account_account_name', 'yunxin_account_no', '1', 'cash_flow_no_1',
                                                     NULL, '2000', '3', '测试', '1', NULL, 'deduct_application',
                                                                                         'SND_LIABILITIES_INDEPENDENT_CUSTOMER_DEPOSIT',
                                                                                         '', NULL, '0',
                                                                                         'compay_customerUuid2', '1',
                                                                                         'cfa28906-adb0-4b81-a6c2-d84fe4033947',
                                                                                         '50000', '50000.01', 0, 0,
   'financial_contract_uuid_1', 'source_document_no');


INSERT INTO `source_document_detail` (`id`, `uuid`, `source_document_uuid`, `contract_unique_id`, `repayment_plan_no`, `amount`, `status`, `first_type`, `first_no`, `second_type`, `second_no`, `payer`)
VALUES
  ('1', 'source_document_detail_uuid_1', 'source_document_uuid_1', 'contract_unique_id_2', 'repayment_plan_no_1',
        '1090.00', '0', 'BUSINESS_PAYMENT_VOUCHER', 'bank_transaction_no_1', 'enum.voucher-type.pay',
        'deduct_application_detail_uuid_1', '0');
--('2', 'source_document_detail_uuid_2', 'source_document_uuid_1', 'contract_unique_id_2', 'repayment_plan_no_2', '500.00', '0', 'BUSINESS_PAYMENT_VOUCHER', 'bank_transaction_no_1', 'enum.voucher-type.pay', 'deduct_application_detail_uuid_1', '0');


INSERT INTO `t_deduct_application` (`id`, `deduct_application_uuid`, `deduct_id`, `request_no`, `financial_contract_uuid`, `financial_product_code`, `contract_unique_id`, `repayment_plan_code_list`, `contract_no`, `planned_deduct_total_amount`, `actual_deduct_total_amount`, `notify_url`, `transcation_type`, `repayment_type`, `execution_status`, `execution_remark`, `create_time`, `creator_name`, `ip`, `last_modified_time`, `record_status`, `is_available`, `api_called_time`, `transaction_recipient`, `complete_time`)
VALUES
  ('1', 'deduct_application_uuid_1', 'deduct_id_1', 'request_no_1', 'financial_contract_uuid_1', 'G00003',
        'contract_unique_id_2', '[\"ZC27438B14F806E86C\"]', 'contract_no_1', '1090.00', '100.00', '', '1', '1', '2', '',
                                                                                                  '2016-08-30 17:30:42',
                                                                                                  't_test_zfb',
                                                                                                  '115.197.179.183',
                                                                                                  '2016-08-30 17:30:42',
                                                                                                  '2', '0',
   '2016-08-04 00:00:00', '0', '2016-08-04 00:00:00');

INSERT INTO `t_deduct_application_detail` (`id`, `deduct_application_detail_uuid`, `deduct_application_uuid`, `financial_contract_uuid`, `contract_unique_id`, `repayment_plan_code`, `request_no`, `repayment_type`, `transaction_type`, `create_time`, `execution_remark`, `creator_name`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`, `is_total`, `asset_set_uuid`)
VALUES
  ('1', 'deduct_application_detail_uuid_1', 'deduct_application_uuid_1', 'financial_contract_uuid_1',
        'contract_unique_id_2', 'repayment_plan_no_1', '2445ef09-f220-4e52-abe2-ec69aa6fbcaf', '0', '1',
        '2016-08-24 17:21:00', '', 't_test_zfb', '2016-08-24 17:21:00', 'FST_RECIEVABLE_ASSET', '20000',
                                   'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_PRINCIPLE',
                                   '20000.01.01', '900.00', '0', 'asset_uuid_1');


INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`) VALUES
  (1, NULL, NULL, 'aa', NULL, 1, 'customerUuid1', 1);


INSERT INTO `cash_flow` (`id`, `cash_flow_uuid`, `cash_flow_channel_type`, `company_uuid`, `host_account_uuid`, `host_account_no`, `host_account_name`, `counter_account_no`, `counter_account_name`, `counter_account_appendix`, `counter_bank_info`, `account_side`, `transaction_time`, `transaction_amount`, `balance`, `transaction_voucher_no`, `bank_sequence_no`, `remark`, `other_remark`, `strike_balance_status`, `trade_uuid`, `issued_amount`, `audit_status`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`)
VALUES
  ('1', 'cash_flow_uuid_1', '0', 'company_uuid_1', 'd0503298-e890-425a-b5b4-12', '6600000000000000001', '测试专户开户行',
        '10001', 'counter_name', NULL, NULL, '1', '2016-08-31 19:51:02', '11000.00', '11000.00', NULL,
                                             'cash_flow_no_12', NULL, NULL, NULL, NULL, '11000.00', '2', NULL, NULL,
                                                                                        NULL, NULL, NULL, NULL, NULL,
                                                                                        NULL, NULL, NULL, NULL, NULL);


SET FOREIGN_KEY_CHECKS = 1;