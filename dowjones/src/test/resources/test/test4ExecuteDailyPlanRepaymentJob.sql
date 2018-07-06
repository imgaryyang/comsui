DELETE FROM `financial_contract`;
DELETE FROM `app`;
DELETE FROM `company`;
DELETE FROM `account`;
DELETE FROM `payment_channel`;

DELETE FROM `daily_plan_repayment`;

DELETE FROM `asset_set`;
DELETE FROM `asset_set_extra_charge`;

DELETE FROM `quartz_job`;

INSERT INTO `financial_contract` (`asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`, `day_of_month`, `pay_for_go`, `repurchase_principal_algorithm`, `repurchase_interest_algorithm`, `repurchase_penalty_algorithm`, `repurchase_other_charges_algorithm`, `repayment_check_days`, `repurchase_cycle`, `days_of_cycle`, `temporary_repurchases`, `allow_freewheeling_repayment`, `capital_party`, `other_party`, `contract_short_name`, `remittance_object`, `financial_type`)
VALUES
  ('1', '0', '2016-09-01 00:00:00', 'G31700', '拍拍贷测试', '3', '5', '12', '2017-08-31 00:00:00', '5', '0', '1', '11', '1',
                                                                                                        '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58',
                                                                                                        'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                                        '0', '0', '1',
                                                                                                        '1', '1', '0',
                                                                                                                  NULL,
                                                                                                                  NULL,
                                                                                                                  '3',
                                                                                                                  'null',
                                                                                                                  '1',
                                                                                                                  '1',
                                                                                                                  '1',
                                                                                                                  '1',
                                                                                                                  '',
    NULL, NULL, NULL, NULL, '2017-10-11 15:21:13', '1', '1',
    'outstandingPrincipal+outstandingInterest+outstandingPenaltyInterest', '6', '0', 'outstandingPrincipal',
                                                                                     'outstandingInterest',
                                                                                     'outstandingPenaltyInterest', '',
                                                                                     '7', NULL, '[]', '[]', '1', NULL,
   NULL, NULL, NULL, '0');

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
VALUES
  ('1', '0', '636.56', '477.57', '54.99', '532.56', '2016-10-06', NULL, '0.00', '0', '1', '0', '2017-08-04 00:54:00',
                                                                                          'e0fdcb1e-68cb-45c8-a1c7-82d6dca09879',
                                                                                          '2016-09-06 18:12:27',
                                                                                          '2017-08-04 00:54:00', NULL,
                                                                                          'ZC2748AACD4F3F059B', '3276',
                                                                                          NULL, '1', '1', NULL, '1',
                                                                                                     '0', '0',
                                                                                                     'empty_deduct_uuid',
                                                                                                     NULL,
                                                                                                     'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                                     NULL, NULL, NULL,
                                                                                                                 NULL,
                                                                                                                 '0',
                                                                                                                 '0',
                                                                                                                 '0',
                                                                                                                 '1',
                                                                                                                 '4',
                                                                                                                 '1',
                                                                                                                 NULL,
                                                                                                                 'eb517aa5-7e2f-459e-a387-cbe8feab48e7',
   '5679cc5b-d6e6-4c40-82fc-e98a0f71e1a2', '0', '1c3a17d0-6650-11e7-bff1-00163e002839', '0', NULL, NULL);
INSERT INTO `asset_set` (`guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`, `version_lock`, `order_payment_status`, `repay_schedule_no`, `outer_repayment_plan_no`)
VALUES ('1', '0', '46.79', '37.63', '9.16', '46.79', '2016-10-06', NULL, '0.00', '0', '1', '0', '2017-08-04 00:36:18',
                                                                                           '12607ef0-f097-4d37-9be0-63baf1717815',
                                                                                           '2016-09-07 20:14:28',
                                                                                           '2017-08-04 00:36:18', NULL,
                                                                                           'ZC2748BC018C35E9AD', '5542',
                                                                                           NULL, '1', '2', NULL, '1',
                                                                                                      '0', '0',
                                                                                                      'empty_deduct_uuid',
                                                                                                      NULL,
                                                                                                      'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                                      NULL, NULL, NULL,
                                                                                                                  NULL,
                                                                                                                  '0',
                                                                                                                  '0',
                                                                                                                  '0',
                                                                                                                  '1',
                                                                                                                  '0',
                                                                                                                  '0',
                                                                                                                  NULL,
                                                                                                                  '5b6b0e5d-90ff-429c-aaa2-90c5ed8661ed',
        'a8222089-9d22-482f-9f6d-5cdcb427ead4', '0', '1ce3c177-6650-11e7-bff1-00163e002839', '0', NULL, NULL);

INSERT INTO `asset_set_extra_charge` (`asset_set_extra_charge_uuid`, `asset_set_uuid`, `create_time`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`)
VALUES ('3af07871-c7a6-4da7-9a87-d85850b3e536', '12607ef0-f097-4d37-9be0-63baf1717815', '2016-09-12 17:10:53',
                                                '2016-09-12 17:10:53', 'FST_RECIEVABLE_ASSET', '20000', NULL,
                                                '20000.06', 'TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE', '20000.06.03',
                                                '111.00');
INSERT INTO `asset_set_extra_charge` (`asset_set_extra_charge_uuid`, `asset_set_uuid`, `create_time`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`)
VALUES ('aabcc85f-2011-43d3-93a8-6cded2c35ab5', 'e0fdcb1e-68cb-45c8-a1c7-82d6dca09879', '2016-09-12 17:10:53',
                                                '2017-08-04 00:54:00', 'FST_RECIEVABLE_ASSET', '20000',
                                                'SND_RECIEVABLE_LOAN_PENALTY', '20000.03', NULL, NULL, '1.00');
INSERT INTO `asset_set_extra_charge` (`asset_set_extra_charge_uuid`, `asset_set_uuid`, `create_time`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`)
VALUES ('ec0f6223-55a7-4eea-bbc2-3e40bf081ab0', 'e0fdcb1e-68cb-45c8-a1c7-82d6dca09879', '2016-09-12 17:10:53',
                                                '2016-09-12 17:10:53', 'FST_RECIEVABLE_ASSET', '20000', NULL,
                                                '20000.06', 'TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION', '20000.06.01',
                                                '2.00');
INSERT INTO `asset_set_extra_charge` (`asset_set_extra_charge_uuid`, `asset_set_uuid`, `create_time`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`)
VALUES ('ba5a8b9d-96bc-4c5f-bc99-c23508babe82', 'e0fdcb1e-68cb-45c8-a1c7-82d6dca09879', '2016-09-12 17:10:53',
                                                '2016-09-12 17:10:53', 'FST_RECIEVABLE_ASSET', '20000',
                                                'SND_UNEARNED_LOAN_ASSET_OTHER_FEE', '20000.06', NULL, '20000.06.02',
                                                '100.00');
