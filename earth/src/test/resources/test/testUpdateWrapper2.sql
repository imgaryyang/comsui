SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM `asset_set`;
DELETE FROM `customer`;
DELETE FROM `financial_contract`;
DELETE FROM `ledger_book_shelf`;
DELETE FROM `contract`;
DELETE FROM `repurchase_doc`;
DELETE FROM `account`;
DELETE FROM `company`;
DELETE FROM `payment_channel`;
DELETE FROM `app`;


INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`)
VALUES ('360633', '0', '0', '1503.03', '1500.00', '0.00', '1500.00', '2017-04-30', NULL, '0.00', '0', '1', '0', NULL,
                                                                                                      '0c180e76-49a4-438f-b10a-f57685a3ebf2',
                                                                                                      '2017-04-18 13:59:45',
                                                                                                      '2017-04-18 13:59:45',
                                                                                                      NULL,
                                                                                                      'ZC1654732812221087744',
                                                                                                      '113921', NULL,
  '1', '0', NULL, '1', '2', '0', 'repurchasing', NULL, 'd2812bc5-5057-4a91-b3fd-9019506f0499',
  'c45f7d8b7e0edfac96109339747ff4ed', '45e85cf0b20803f04dfbf44fc66243a5', '2017-04-18 13:59:45', '2017-04-18 13:59:45',
                                      '0', '0', '0', '0', '0', '3', '0', '933f3f17-2287-4407-aed0-9dbf9778ab33',
        '2173fa90-7ecc-4231-9a19-f93fafc903fc', NULL);

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`) VALUES
  ('114255', '320301198502169142', NULL, '车贷中', 'a745bee9-77bb-4031-b0af-4debb624c753', '3',
   '933f3f17-2287-4407-aed0-9dbf9778ab33', '0');

INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`, `day_of_month`, `pay_for_go`, `repurchase_principal_algorithm`, `repurchase_interest_algorithm`, `repurchase_penalty_algorithm`, `repurchase_other_charges_algorithm`)
VALUES
  ('38', '0', '0', '2016-09-01 00:00:00', 'G31700', '拍拍贷测试', '3', '1', '10', '2017-08-31 00:00:00', '102', '0', '2',
                                                                                                           '9', '1',
                                                                                                           '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58',
                                                                                                           'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                                           '0', '0',
                                                                                                           '1', '1',
    '1', '0', NULL, NULL, NULL, NULL, '0', '1', '1', '1', '', NULL, NULL, NULL, NULL, '2017-04-17 19:10:19', '1', '1',
                                                          'outstandingPrincipal+outstandingInterest+outstandingPenaltyInterest',
                                                          '6', '1', 'outstandingPrincipal', 'outstandingInterest',
   'outstandingInterest', 'outstandingInterest*2');

INSERT INTO `ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES ('15071592', '51c3eb82-2869-40a1-a56d-802bc7f0d96f', '1.01', '0.00', 'FST_UNEARNED_LOAN_ASSET', '10000', '1',
                    'SND_UNEARNED_LOAN_ASSET_TECH_FEE', '10000.04', NULL, NULL, 'a02c02b9-6f98-11e6-bf08-00163e002839',
                                                                                '933f3f17-2287-4407-aed0-9dbf9778ab33',
                                                                                NULL, NULL, NULL,
                                                                                '97e24090-b35f-4010-849d-445c47ab80ab',
                                                                                '2017-04-30', '2017-04-18 13:59:45', '',
                                                                                NULL, '113921',
                                                                                      '2173fa90-7ecc-4231-9a19-f93fafc903fc',
                                                                                      '2017-04-30 00:00:00', '',
                                                                                      '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58',
                                                                                      '1', '1', 'ZC1654732812221087744',
                                                                                      '0c180e76-49a4-438f-b10a-f57685a3ebf2',
                                                                                      NULL, NULL, NULL, NULL, '');


INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`)
VALUES ('8043', '', '', 'ppd', '', '3', 'uuid_5d-4166-44cb-b406-9b41eaaaaaaa', '1');

INSERT INTO `repurchase_doc` (`id`, `repurchase_doc_uuid`, `financial_contract_uuid`, `amount`, `repo_start_date`, `repo_end_date`, `repo_days`, `creat_time`, `verification_time`, `last_modifed_time`, `contract_id`, `contract_no`, `app_id`, `app_name`, `customer_uuid`, `customer_name`, `executing_asset_set_uuids`, `asset_set_uuids`, `repurchase_status`, `repurchase_algorithm`, `repurchase_approach`, `repurchase_rule`, `day_of_month`, `adva_repo_term`, `repurchase_periods`, `amount_detail`, `repurchase_principal`, `repurchase_principal_algorithm`, `repurchase_interest`, `repurchase_interest_algorithm`, `repurchase_penalty`, `repurchase_penalty_algorithm`, `repurchase_other_charges`, `repurchase_other_charges_algorithm`)
VALUES ('119', '9560762b-c9e1-4aca-8ada-171f9692c618', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '2000.00', '2017-04-18',
               '2017-04-18', '0', '2017-04-18 17:02:54', NULL, '2017-04-18 17:02:54', '113921',
  '984c6227-995e-48b8-a5d4-1234e5bf45c4', '3', '测试商户ppd', '933f3f17-2287-4407-aed0-9dbf9778ab33', '车贷中', NULL,
  '[\"0c180e76-49a4-438f-b10a-f57685a3ebf2\"]', '0', NULL, '2', NULL, NULL, '10', '1',
                                                                '{\"amount\":2000,\"repurchaseInterest\":500,\"repurchaseOtherCharges\":0,\"repurchasePenalty\":0,\"repurchasePrincipal\":1500}',
                                                                '1500.00', NULL, '500.00', NULL, '0.00', NULL, '0.00',
        NULL);

INSERT INTO `account` (`id`, `account_name`, `account_no`, `bank_name`, `alias`, `attr`, `scan_cash_flow_switch`, `usb_key_configured`, `uuid`, `bank_code`, `cash_flow_config`)
VALUES ('102', '云南信托国际有限公司', '600000000001', '平安银行深圳分行', NULL, NULL, b'0', b'0', 'bd7dd5b1-aa53-4faf-9647-00ddb8ab4b42',
               'PAB', NULL);
INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`)
VALUES ('1', '上海', '测试金融公司', '测试金融', 'a02c02b9-6f98-11e6-bf08-00163e002839');
INSERT INTO `payment_channel` (`id`, `channel_name`, `user_name`, `user_password`, `merchant_id`, `cer_file_path`, `pfx_file_path`, `pfx_file_key`, `payment_channel_type`, `api_url`, `from_date`, `thru_date`)
VALUES ('1', '测试通道1', 'operator', 'operator', '001053110000001',
             '/data/webapps/tomcat-earth/webapps/earth/WEB-INF/classes/certificate/gzdsf.cer',
             '/data/webapps/tomcat-earth/webapps/earth/WEB-INF/classes/certificate/ORA@TEST1.pfx', '123456', '0',
             'http://59.41.103.98:333/gzdsf/ProcessServlet', NULL, NULL);
INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`)
VALUES ('3', 'ppd', NULL, b'1', NULL, '测试商户ppd', '5', NULL);


SET FOREIGN_KEY_CHECKS = 1;