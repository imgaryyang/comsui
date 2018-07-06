DELETE FROM contract;
DELETE FROM financial_contract;
DELETE FROM asset_set;
DELETE FROM company;
DELETE FROM ledger_book_shelf;
DELETE FROM t_remittance_plan;
DELETE FROM repurchase_doc;
DELETE FROM journal_voucher;
DELETE FROM app;
DELETE FROM account;
DELETE FROM payment_channel;
DELETE FROM contract;

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


INSERT INTO journal_voucher (account_side, bank_identity, billing_plan_uuid, booking_amount, business_voucher_type_uuid, business_voucher_uuid, cash_flow_amount, cash_flow_breif, cash_flow_channel_type, cash_flow_serial_no, cash_flow_uuid, checking_level, company_id, completeness, counter_party_account, counter_party_name, journal_voucher_uuid, notification_identity, notification_memo, notification_record_uuid, notified_date, settlement_modes, source_document_amount, source_document_breif, source_document_cash_flow_serial_no, source_document_counter_party_uuid, source_document_identity, source_document_uuid, status, trade_time, batch_uuid, created_date, source_document_counter_party_account, source_document_counter_party_name, issued_time, journal_voucher_type, counter_account_type, related_bill_contract_info_lv_1, related_bill_contract_info_lv_2, related_bill_contract_info_lv_3, cash_flow_account_info, journal_voucher_no, related_bill_contract_no_lv_1, related_bill_contract_no_lv_2, related_bill_contract_no_lv_3, related_bill_contract_no_lv_4, source_document_no, appendix, last_modified_time, local_party_account, local_party_name, source_document_local_party_account, source_document_local_party_name, second_journal_voucher_type, third_journal_voucher_type, is_has_data_sync_log)
VALUES (1, '中国建设银行 ', 'c7f0e04c-9e34-402c-b236-fb9d699b5bd2', 1050.00, '2b784ef2-a1ab-41fa-b315-8e1bf8cfa2ba',
           'f6c6e961-0c8e-43c3-b387-5310b2bb0f80', 1050.00, NULL, 2, NULL, NULL, 0, 1, 3, '6217001210075327590', '韩方园',
                                                                                 'c70a6e34-b263-4325-9637-26419bb5fedb',
                                                                                 NULL, NULL, NULL, NULL, NULL, 1050.00,
                                                                                                         NULL, NULL,
                                                                                                         NULL,
                                                                                                         'bf06d745-897f-4a0e-8ea8-93743b18aa7b',
                                                                                                         '0a83bf96-6557-4e25-ba15-465799d6b95f',
                                                                                                         1,
                                                                                                         '2017-02-17 12:59:53',
                                                                                                         NULL,
  '2017-02-17 12:59:52', '6217001210075327590', '韩方园', '2017-02-17 12:59:55', 7, 0,
  'd2812bc5-5057-4a91-b3fd-9019506f0499', 'b12608b1-726b-4517-b3c0-789e4daeab8e',
  'c7f0e04c-9e34-402c-b236-fb9d699b5bd2', '', '32c4b9a6-5788-4050-995e-db0707b3ee23', '拍拍贷测试',
                                              '2016-236-DK(hk23168343874178145)号', 'ZC275CC8173B35E7FE',
                                              'JS958466203836882944', 'KK958465816752955392', NULL,
                                              '2017-02-17 12:59:55', '600000000001', '云南信托国际有限公司', '600000000001',
        '云南信托国际有限公司', 1, 2, 1);
INSERT INTO journal_voucher (account_side, bank_identity, billing_plan_uuid, booking_amount, business_voucher_type_uuid, business_voucher_uuid, cash_flow_amount, cash_flow_breif, cash_flow_channel_type, cash_flow_serial_no, cash_flow_uuid, checking_level, company_id, completeness, counter_party_account, counter_party_name, journal_voucher_uuid, notification_identity, notification_memo, notification_record_uuid, notified_date, settlement_modes, source_document_amount, source_document_breif, source_document_cash_flow_serial_no, source_document_counter_party_uuid, source_document_identity, source_document_uuid, status, trade_time, batch_uuid, created_date, source_document_counter_party_account, source_document_counter_party_name, issued_time, journal_voucher_type, counter_account_type, related_bill_contract_info_lv_1, related_bill_contract_info_lv_2, related_bill_contract_info_lv_3, cash_flow_account_info, journal_voucher_no, related_bill_contract_no_lv_1, related_bill_contract_no_lv_2, related_bill_contract_no_lv_3, related_bill_contract_no_lv_4, source_document_no, appendix, last_modified_time, local_party_account, local_party_name, source_document_local_party_account, source_document_local_party_name, second_journal_voucher_type, third_journal_voucher_type, is_has_data_sync_log)
VALUES (1, '中国工商银行', 'e0983d23-eaba-438f-8746-ee5c297ff607', 965.38, '2b784ef2-a1ab-41fa-b315-8e1bf8cfa2ba',
           '1dc71566-6267-4f03-b148-94da9f6fe02a', 965.38, NULL, 2, NULL, NULL, 0, 1, 3, '6222020111122220000', '徐奇辉',
                                                                                'e70eb2cc-8567-4819-9f18-73680294c32d',
                                                                                NULL, NULL, NULL, NULL, NULL, 965.38,
                                                                                                        NULL, NULL,
                                                                                                        NULL,
                                                                                                        '65bbd6ba-c855-408e-8571-aa6b4223eae3',
                                                                                                        '24e4468c-5763-4200-a8e0-ad375f04e310',
                                                                                                        1,
                                                                                                        '2017-02-17 13:43:53',
                                                                                                        NULL,
  '2017-02-17 13:43:52', '6222020111122220000', '徐奇辉', '2017-02-17 13:43:57', 7, 0,
  'd2812bc5-5057-4a91-b3fd-9019506f0499', 'dbbb7166-d277-403b-bc3c-ef845193468f',
  'e0983d23-eaba-438f-8746-ee5c297ff607', '', '8c8c2b55-9f1a-4607-b29a-842a6cd5007a', '拍拍贷测试', '2016-236-DK(12828655)号',
                                              'ZC958632357062692864', 'JS958820792041865216', 'KK958820210342232064',
                                              NULL, '2017-02-17 13:43:57', '600000000001', '云南信托国际有限公司', '600000000001',
        '云南信托国际有限公司', 1, 1, 1);
INSERT INTO journal_voucher (account_side, bank_identity, billing_plan_uuid, booking_amount, business_voucher_type_uuid, business_voucher_uuid, cash_flow_amount, cash_flow_breif, cash_flow_channel_type, cash_flow_serial_no, cash_flow_uuid, checking_level, company_id, completeness, counter_party_account, counter_party_name, journal_voucher_uuid, notification_identity, notification_memo, notification_record_uuid, notified_date, settlement_modes, source_document_amount, source_document_breif, source_document_cash_flow_serial_no, source_document_counter_party_uuid, source_document_identity, source_document_uuid, status, trade_time, batch_uuid, created_date, source_document_counter_party_account, source_document_counter_party_name, issued_time, journal_voucher_type, counter_account_type, related_bill_contract_info_lv_1, related_bill_contract_info_lv_2, related_bill_contract_info_lv_3, cash_flow_account_info, journal_voucher_no, related_bill_contract_no_lv_1, related_bill_contract_no_lv_2, related_bill_contract_no_lv_3, related_bill_contract_no_lv_4, source_document_no, appendix, last_modified_time, local_party_account, local_party_name, source_document_local_party_account, source_document_local_party_name, second_journal_voucher_type, third_journal_voucher_type, is_has_data_sync_log)
VALUES (1, '中国工商银行', '02ded3b5-4441-407f-9478-e05a5ab63f9d', 965.38, '2b784ef2-a1ab-41fa-b315-8e1bf8cfa2ba',
           '679d0567-c1d5-44d0-a74d-7dc4f3f1f847', 965.38, NULL, 2, NULL, NULL, 0, 1, 3, '6222020111122220000', '徐奇辉',
                                                                                '5a215040-4c8a-4d02-a41d-57a27dccf097',
                                                                                NULL, NULL, NULL, NULL, NULL, 965.38,
                                                                                                        NULL, NULL,
                                                                                                        NULL,
                                                                                                        '5f1ddb60-ea0f-480c-97d6-99f6bda92d94',
                                                                                                        '186799d5-e9b8-4db8-bcb6-c4b263b7a2e3',
                                                                                                        1,
                                                                                                        '2017-02-17 14:49:54',
                                                                                                        NULL,
  '2017-02-17 14:49:53', '6222020111122220000', '徐奇辉', '2017-02-17 14:49:58', 7, 0,
  'd2812bc5-5057-4a91-b3fd-9019506f0499', 'dbbb7166-d277-403b-bc3c-ef845193468f',
  '02ded3b5-4441-407f-9478-e05a5ab63f9d', '', 'cb9cf77d-39af-49c1-93ee-9cf172b8def2', '拍拍贷测试', '2016-236-DK(12828655)号',
                                              'ZC958632357465346048', 'JS959352424435941376', 'KK959351770258735104',
                                              NULL, '2017-02-17 14:49:58', '600000000001', '云南信托国际有限公司', '600000000001',
        '云南信托国际有限公司', 1, 0, 1);
INSERT INTO journal_voucher (account_side, bank_identity, billing_plan_uuid, booking_amount, business_voucher_type_uuid, business_voucher_uuid, cash_flow_amount, cash_flow_breif, cash_flow_channel_type, cash_flow_serial_no, cash_flow_uuid, checking_level, company_id, completeness, counter_party_account, counter_party_name, journal_voucher_uuid, notification_identity, notification_memo, notification_record_uuid, notified_date, settlement_modes, source_document_amount, source_document_breif, source_document_cash_flow_serial_no, source_document_counter_party_uuid, source_document_identity, source_document_uuid, status, trade_time, batch_uuid, created_date, source_document_counter_party_account, source_document_counter_party_name, issued_time, journal_voucher_type, counter_account_type, related_bill_contract_info_lv_1, related_bill_contract_info_lv_2, related_bill_contract_info_lv_3, cash_flow_account_info, journal_voucher_no, related_bill_contract_no_lv_1, related_bill_contract_no_lv_2, related_bill_contract_no_lv_3, related_bill_contract_no_lv_4, source_document_no, appendix, last_modified_time, local_party_account, local_party_name, source_document_local_party_account, source_document_local_party_name, second_journal_voucher_type, third_journal_voucher_type, is_has_data_sync_log)
VALUES (1, '中国建设银行', '2f12ea99-786c-44ad-befc-0fec8706d3c0', 500.00, '2b784ef2-a1ab-41fa-b315-8e1bf8cfa2ba',
           'e14ed960-13d1-49cf-a240-13b9bd0c4478', 500.00, NULL, 2, NULL, NULL, 0, 1, 3, '6217002430031060800', '王铁成',
                                                                                '1ac4495e-e0b9-49f4-836b-a6eb134be202',
                                                                                NULL, NULL, NULL, NULL, NULL, 500.00,
                                                                                                        NULL, NULL,
                                                                                                        NULL,
                                                                                                        'ffd229da-b4cd-4b1a-ac39-75d4865cc31f',
                                                                                                        '90516e4c-936a-48f5-ada0-09044e5d7196',
                                                                                                        1,
                                                                                                        '2017-02-17 15:35:33',
                                                                                                        NULL,
  '2017-02-17 15:35:33', '6217002430031060800', '王铁成', '2017-02-17 15:35:35', 7, 0,
  'd2812bc5-5057-4a91-b3fd-9019506f0499', 'cfc7170e-52e6-497f-85dd-862cfcb22387',
  '2f12ea99-786c-44ad-befc-0fec8706d3c0', '', '7c768bc8-6dcc-4fc8-b3f1-32e9b33edb74', '拍拍贷测试', '2016-236-DK(12432222)号',
                                              'ZC17359336539766784', 'JS959719836876414976', 'KK959719494352773120',
                                              NULL, '2017-02-17 15:35:35', '600000000001', '云南信托国际有限公司', '600000000001',
        '云南信托国际有限公司', 1, 2, 1);
INSERT INTO journal_voucher (account_side, bank_identity, billing_plan_uuid, booking_amount, business_voucher_type_uuid, business_voucher_uuid, cash_flow_amount, cash_flow_breif, cash_flow_channel_type, cash_flow_serial_no, cash_flow_uuid, checking_level, company_id, completeness, counter_party_account, counter_party_name, journal_voucher_uuid, notification_identity, notification_memo, notification_record_uuid, notified_date, settlement_modes, source_document_amount, source_document_breif, source_document_cash_flow_serial_no, source_document_counter_party_uuid, source_document_identity, source_document_uuid, status, trade_time, batch_uuid, created_date, source_document_counter_party_account, source_document_counter_party_name, issued_time, journal_voucher_type, counter_account_type, related_bill_contract_info_lv_1, related_bill_contract_info_lv_2, related_bill_contract_info_lv_3, cash_flow_account_info, journal_voucher_no, related_bill_contract_no_lv_1, related_bill_contract_no_lv_2, related_bill_contract_no_lv_3, related_bill_contract_no_lv_4, source_document_no, appendix, last_modified_time, local_party_account, local_party_name, source_document_local_party_account, source_document_local_party_name, second_journal_voucher_type, third_journal_voucher_type, is_has_data_sync_log)
VALUES (1, '中国建设银行', '2f12ea99-786c-44ad-befc-0fec8706d3c0', 100.00, '2b784ef2-a1ab-41fa-b315-8e1bf8cfa2ba',
           'bb8f9255-0719-4bab-8ba0-8a836acf7cd4', 100.00, NULL, 2, NULL, NULL, 0, 1, 3, '6217002430031060800', '王铁成',
                                                                                '672fb655-5348-4cab-b0c7-2d0c763fa8ba',
                                                                                NULL, NULL, NULL, NULL, NULL, 100.00,
                                                                                                        NULL, NULL,
                                                                                                        NULL,
                                                                                                        'e4a0e28e-0bee-4cd9-b527-8d4d4a5d7686',
                                                                                                        '8e82fe10-d60b-43fe-bd6b-91e185a11036',
                                                                                                        1,
                                                                                                        '2017-02-17 15:47:33',
                                                                                                        NULL,
  '2017-02-17 15:47:32', '6217002430031060800', '王铁成', '2017-02-17 15:47:36', 7, 0,
  'd2812bc5-5057-4a91-b3fd-9019506f0499', 'cfc7170e-52e6-497f-85dd-862cfcb22387',
  '2f12ea99-786c-44ad-befc-0fec8706d3c0', '', '048a3c90-4636-4430-b971-5fefbb05289d', '拍拍贷测试', '2016-236-DK(12432222)号',
                                              'ZC17359336539766784', 'JS959816532562157568', 'KK959816080785285120',
                                              NULL, '2017-02-17 15:47:36', '600000000001', '云南信托国际有限公司', '600000000001',
        '云南信托国际有限公司', 1, 2, 1);
INSERT INTO journal_voucher (account_side, bank_identity, billing_plan_uuid, booking_amount, business_voucher_type_uuid, business_voucher_uuid, cash_flow_amount, cash_flow_breif, cash_flow_channel_type, cash_flow_serial_no, cash_flow_uuid, checking_level, company_id, completeness, counter_party_account, counter_party_name, journal_voucher_uuid, notification_identity, notification_memo, notification_record_uuid, notified_date, settlement_modes, source_document_amount, source_document_breif, source_document_cash_flow_serial_no, source_document_counter_party_uuid, source_document_identity, source_document_uuid, status, trade_time, batch_uuid, created_date, source_document_counter_party_account, source_document_counter_party_name, issued_time, journal_voucher_type, counter_account_type, related_bill_contract_info_lv_1, related_bill_contract_info_lv_2, related_bill_contract_info_lv_3, cash_flow_account_info, journal_voucher_no, related_bill_contract_no_lv_1, related_bill_contract_no_lv_2, related_bill_contract_no_lv_3, related_bill_contract_no_lv_4, source_document_no, appendix, last_modified_time, local_party_account, local_party_name, source_document_local_party_account, source_document_local_party_name, second_journal_voucher_type, third_journal_voucher_type, is_has_data_sync_log)
VALUES (1, '中国工商银行', 'd58e06cc-19a8-40b0-9b90-2ff2a20c39ab', 3774.94, '2b784ef2-a1ab-41fa-b315-8e1bf8cfa2ba',
           '0f7b6cfe-7c88-4d3e-bd5b-e49cb0f27186', 3774.94, NULL, 2, NULL, NULL, 0, 1, 3, '6222020111122220000', '徐奇辉',
                                                                                 '6d779b7f-ecc0-4217-86be-ff3807a41a8f',
                                                                                 NULL, NULL, NULL, NULL, NULL, 3774.94,
                                                                                                         NULL, NULL,
                                                                                                         NULL,
                                                                                                         '53353fa9-f91b-4d5e-8cb3-fb80e32887a6',
                                                                                                         '3f6fc0ec-29d6-4eb3-947f-0b5ae9094e23',
                                                                                                         1,
                                                                                                         '2017-02-17 15:47:34',
                                                                                                         NULL,
  '2017-02-17 15:47:33', '6222020111122220000', '徐奇辉', '2017-02-17 15:47:36', 7, 0,
  'd2812bc5-5057-4a91-b3fd-9019506f0499', 'dbbb7166-d277-403b-bc3c-ef845193468f',
  'd58e06cc-19a8-40b0-9b90-2ff2a20c39ab', '', '585ee21e-bde8-41eb-a26e-6002dfe19200', '拍拍贷测试', '2016-236-DK(12828655)号',
                                              'ZC959809880865820672', 'JS959816562358484992', 'KK959816216613617664',
                                              NULL, '2017-02-17 15:47:36', '600000000001', '云南信托国际有限公司', '600000000001',
        '云南信托国际有限公司', 1, 0, 1);
INSERT INTO journal_voucher (account_side, bank_identity, billing_plan_uuid, booking_amount, business_voucher_type_uuid, business_voucher_uuid, cash_flow_amount, cash_flow_breif, cash_flow_channel_type, cash_flow_serial_no, cash_flow_uuid, checking_level, company_id, completeness, counter_party_account, counter_party_name, journal_voucher_uuid, notification_identity, notification_memo, notification_record_uuid, notified_date, settlement_modes, source_document_amount, source_document_breif, source_document_cash_flow_serial_no, source_document_counter_party_uuid, source_document_identity, source_document_uuid, status, trade_time, batch_uuid, created_date, source_document_counter_party_account, source_document_counter_party_name, issued_time, journal_voucher_type, counter_account_type, related_bill_contract_info_lv_1, related_bill_contract_info_lv_2, related_bill_contract_info_lv_3, cash_flow_account_info, journal_voucher_no, related_bill_contract_no_lv_1, related_bill_contract_no_lv_2, related_bill_contract_no_lv_3, related_bill_contract_no_lv_4, source_document_no, appendix, last_modified_time, local_party_account, local_party_name, source_document_local_party_account, source_document_local_party_name, second_journal_voucher_type, third_journal_voucher_type, is_has_data_sync_log)
VALUES (1, '中国建设银行', '2f12ea99-786c-44ad-befc-0fec8706d3c0', 20.00, '2b784ef2-a1ab-41fa-b315-8e1bf8cfa2ba',
           '8f788523-8c36-482c-9e19-6dd4e1dc3042', 20.00, NULL, 2, NULL, NULL, 0, 1, 3, '6217002430031060800', '王铁成',
                                                                               'fe6aa6d2-9af0-4463-a0f3-189320ebe7d3',
                                                                               NULL, NULL, NULL, NULL, NULL, 20.00,
                                                                                                       NULL, NULL, NULL,
                                                                                                       '362f1ccf-a4cb-47b6-b9e3-9c5ef4f28418',
                                                                                                       '861a42ca-b360-413c-96ba-2daedd626287',
                                                                                                       1,
                                                                                                       '2017-02-17 15:55:53',
                                                                                                       NULL,
  '2017-02-17 15:55:53', '6217002430031060800', '王铁成', '2017-02-17 15:55:56', 7, 0,
  'd2812bc5-5057-4a91-b3fd-9019506f0499', 'cfc7170e-52e6-497f-85dd-862cfcb22387',
  '2f12ea99-786c-44ad-befc-0fec8706d3c0', '', 'f0010f68-dd6a-4c56-8070-8df1f03929cd', '拍拍贷测试', '2016-236-DK(12432222)号',
                                              'ZC17359336539766784', 'JS959883679409774592', 'KK959883212063645696',
                                              NULL, '2017-02-17 15:55:56', '600000000001', '云南信托国际有限公司', '600000000001',
        '云南信托国际有限公司', 1, 2, 1);
INSERT INTO journal_voucher (account_side, bank_identity, billing_plan_uuid, booking_amount, business_voucher_type_uuid, business_voucher_uuid, cash_flow_amount, cash_flow_breif, cash_flow_channel_type, cash_flow_serial_no, cash_flow_uuid, checking_level, company_id, completeness, counter_party_account, counter_party_name, journal_voucher_uuid, notification_identity, notification_memo, notification_record_uuid, notified_date, settlement_modes, source_document_amount, source_document_breif, source_document_cash_flow_serial_no, source_document_counter_party_uuid, source_document_identity, source_document_uuid, status, trade_time, batch_uuid, created_date, source_document_counter_party_account, source_document_counter_party_name, issued_time, journal_voucher_type, counter_account_type, related_bill_contract_info_lv_1, related_bill_contract_info_lv_2, related_bill_contract_info_lv_3, cash_flow_account_info, journal_voucher_no, related_bill_contract_no_lv_1, related_bill_contract_no_lv_2, related_bill_contract_no_lv_3, related_bill_contract_no_lv_4, source_document_no, appendix, last_modified_time, local_party_account, local_party_name, source_document_local_party_account, source_document_local_party_name, second_journal_voucher_type, third_journal_voucher_type, is_has_data_sync_log)
VALUES (1, '中国建设银行', '2f12ea99-786c-44ad-befc-0fec8706d3c0', 15.00, '2b784ef2-a1ab-41fa-b315-8e1bf8cfa2ba',
           '8fd9b498-5a50-4935-bb78-17735bc23ea8', 15.00, NULL, 2, NULL, NULL, 0, 1, 3, '6217002430031060800', '王铁成',
                                                                               'b34cd179-4fa9-4af8-ae7b-a63265f5435e',
                                                                               NULL, NULL, NULL, NULL, NULL, 15.00,
                                                                                                       NULL, NULL, NULL,
                                                                                                       'afa84e50-1c42-45f9-9f03-a2f23998f8f6',
                                                                                                       '78b4d5cf-f812-4d7f-8ebd-c3d8afa54582',
                                                                                                       1,
                                                                                                       '2017-02-17 16:13:33',
                                                                                                       NULL,
  '2017-02-17 16:13:32', '6217002430031060800', '王铁成', '2017-02-17 16:13:34', 7, 0,
  'd2812bc5-5057-4a91-b3fd-9019506f0499', 'cfc7170e-52e6-497f-85dd-862cfcb22387',
  '2f12ea99-786c-44ad-befc-0fec8706d3c0', '', 'b2e985eb-7a41-4700-ae9e-fa2422268d9b', '拍拍贷测试', '2016-236-DK(12432222)号',
                                              'ZC17359336539766784', 'JS960025754109353984', 'KK960025426349662208',
                                              NULL, '2017-02-17 16:13:34', '600000000001', '云南信托国际有限公司', '600000000001',
        '云南信托国际有限公司', 1, 2, 1);
INSERT INTO journal_voucher (account_side, bank_identity, billing_plan_uuid, booking_amount, business_voucher_type_uuid, business_voucher_uuid, cash_flow_amount, cash_flow_breif, cash_flow_channel_type, cash_flow_serial_no, cash_flow_uuid, checking_level, company_id, completeness, counter_party_account, counter_party_name, journal_voucher_uuid, notification_identity, notification_memo, notification_record_uuid, notified_date, settlement_modes, source_document_amount, source_document_breif, source_document_cash_flow_serial_no, source_document_counter_party_uuid, source_document_identity, source_document_uuid, status, trade_time, batch_uuid, created_date, source_document_counter_party_account, source_document_counter_party_name, issued_time, journal_voucher_type, counter_account_type, related_bill_contract_info_lv_1, related_bill_contract_info_lv_2, related_bill_contract_info_lv_3, cash_flow_account_info, journal_voucher_no, related_bill_contract_no_lv_1, related_bill_contract_no_lv_2, related_bill_contract_no_lv_3, related_bill_contract_no_lv_4, source_document_no, appendix, last_modified_time, local_party_account, local_party_name, source_document_local_party_account, source_document_local_party_name, second_journal_voucher_type, third_journal_voucher_type, is_has_data_sync_log)
VALUES (1, '中国建设银行', '2f12ea99-786c-44ad-befc-0fec8706d3c0', 13.72, '2b784ef2-a1ab-41fa-b315-8e1bf8cfa2ba',
           'd74add08-9d00-4a18-afd1-51af15a3cbed', 13.72, NULL, 2, NULL, NULL, 0, 1, 3, '6217002430031060800', '王铁成',
                                                                               'a765b873-3153-4561-8ea1-3189dbc3e3e3',
                                                                               NULL, NULL, NULL, NULL, NULL, 13.72,
                                                                                                       NULL, NULL, NULL,
                                                                                                       'e2f63883-a8d1-45f7-882b-b0098ee88370',
                                                                                                       '25d8db00-452e-4d86-87c3-a585ae1811a0',
                                                                                                       1,
                                                                                                       '2017-02-17 16:43:53',
                                                                                                       NULL,
  '2017-02-17 16:43:52', '6217002430031060800', '王铁成', '2017-02-17 16:43:54', 7, 0,
  'd2812bc5-5057-4a91-b3fd-9019506f0499', 'cfc7170e-52e6-497f-85dd-862cfcb22387',
  '2f12ea99-786c-44ad-befc-0fec8706d3c0', '', '64ef81a6-6869-4b4a-b424-fde1404a365a', '拍拍贷测试', '2016-236-DK(12432222)号',
                                              'ZC17359336539766784', 'JS960270017355194368', 'KK960269697514348544',
                                              NULL, '2017-02-17 16:43:54', '600000000001', '云南信托国际有限公司', '600000000001',
        '云南信托国际有限公司', 1, 2, 1);
INSERT INTO journal_voucher (account_side, bank_identity, billing_plan_uuid, booking_amount, business_voucher_type_uuid, business_voucher_uuid, cash_flow_amount, cash_flow_breif, cash_flow_channel_type, cash_flow_serial_no, cash_flow_uuid, checking_level, company_id, completeness, counter_party_account, counter_party_name, journal_voucher_uuid, notification_identity, notification_memo, notification_record_uuid, notified_date, settlement_modes, source_document_amount, source_document_breif, source_document_cash_flow_serial_no, source_document_counter_party_uuid, source_document_identity, source_document_uuid, status, trade_time, batch_uuid, created_date, source_document_counter_party_account, source_document_counter_party_name, issued_time, journal_voucher_type, counter_account_type, related_bill_contract_info_lv_1, related_bill_contract_info_lv_2, related_bill_contract_info_lv_3, cash_flow_account_info, journal_voucher_no, related_bill_contract_no_lv_1, related_bill_contract_no_lv_2, related_bill_contract_no_lv_3, related_bill_contract_no_lv_4, source_document_no, appendix, last_modified_time, local_party_account, local_party_name, source_document_local_party_account, source_document_local_party_name, second_journal_voucher_type, third_journal_voucher_type, is_has_data_sync_log)
VALUES (1, '中国建设银行', '05219404-8890-49da-8b5a-0477bb4e40c1', 646.72, '2b784ef2-a1ab-41fa-b315-8e1bf8cfa2ba',
           '761ff320-418b-4f67-abc4-edc3474a7b0a', 646.72, NULL, 2, NULL, NULL, 0, 1, 3, '6217002430031060800', '王铁成',
                                                                                'c1da3fab-2b4d-4ed4-895f-5ce914b1110f',
                                                                                NULL, NULL, NULL, NULL, NULL, 646.72,
                                                                                                        NULL, NULL,
                                                                                                        NULL,
                                                                                                        '2ee5989d-fa41-4ff4-bd94-c940a1679e65',
                                                                                                        '6af97075-caf6-47da-a4db-3b30f2939798',
                                                                                                        1,
                                                                                                        '2017-02-17 17:05:53',
                                                                                                        NULL,
  '2017-02-17 17:05:52', '6217002430031060800', '王铁成', '2017-02-17 17:05:55', 7, 0,
  'd2812bc5-5057-4a91-b3fd-9019506f0499', 'cfc7170e-52e6-497f-85dd-862cfcb22387',
  '05219404-8890-49da-8b5a-0477bb4e40c1', '', 'e41324bd-e469-4ba4-a963-c472fa5d1d01', '拍拍贷测试', '2016-236-DK(12432222)号',
                                              'ZC17359336548155392', 'JS960447277232168960', 'KK960446933366349824',
                                              NULL, '2017-02-17 17:05:55', '600000000001', '云南信托国际有限公司', '600000000001',
        '云南信托国际有限公司', 1, 2, 1);


INSERT INTO asset_set (guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES (0, 0, 965.38, 896.13, 69.25, 965.38, '2017-02-18', '2017-02-17', 0.00, 1, 2, 0, NULL,
                                                                                     '02ded3b5-4441-407f-9478-e05a5ab63f9d',
                                                                                     '2017-02-17 13:20:33',
                                                                                     '2017-02-17 14:49:58', NULL,
                                                                                     'ZC958632357465346048', 101918,
                                                                                     '2017-02-17 14:49:54', 2, 0, NULL,
                                                                                                               489337117,
                                                                                                               0, 0,
                                                                                                               '679d0567-c1d5-44d0-a74d-7dc4f3f1f847',
                                                                                                               NULL,
                                                                                                               'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                                               'e5a70b461d5de385d2fc7fc4273a3f44',
                                                                                                               '00bfd64b58361d989ac8bf13dccc3c9b',
  '2017-02-17 13:20:33', '2017-02-17 13:20:33', 0, 0, 0, 0, 3, 2, 0, 'dc377713-82c3-4fbd-afc1-62e4a3fd7477',
        'dbbb7166-d277-403b-bc3c-ef845193468f', 0, '3d229e06-6650-11e7-bff1-00163e002839', 0, NULL, NULL);
INSERT INTO asset_set (guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES (1, 0, 646.72, 515.11, 102.61, 617.72, '2017-01-15', '2017-02-17', 0.00, 1, 2, 2, '2017-02-17 17:05:08',
                                                                                      '05219404-8890-49da-8b5a-0477bb4e40c1',
                                                                                      '2017-01-13 19:00:53',
                                                                                      '2017-02-17 17:05:55', NULL,
                                                                                      'ZC17359336548155392', 101431,
                                                                                      '2017-02-17 17:05:53', 3, 1, NULL,
                                                                                                                -248733414,
                                                                                                                0, 0,
                                                                                                                '761ff320-418b-4f67-abc4-edc3474a7b0a',
                                                                                                                NULL,
                                                                                                                'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                                                '4ed0cafdc2e9eea0491433745ec1e4ff',
                                                                                                                '00bfd64b58361d989ac8bf13dccc3c9b',
  '2017-01-13 19:00:53', '2017-01-13 19:00:53', 0, 0, 0, 1, 3, 2, 0, 'f13879a4-112c-4c4b-8898-d1dc8f404e83',
        'cfc7170e-52e6-497f-85dd-862cfcb22387', 0, '3ce92bd1-6650-11e7-bff1-00163e002839', 0, NULL, NULL);
INSERT INTO asset_set (guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES (1, 0, 648.72, 505.83, 111.89, 617.72, '2017-01-14', '2017-02-17', 0.00, 1, 2, 2, '2017-02-17 15:55:00',
                                                                                      '2f12ea99-786c-44ad-befc-0fec8706d3c0',
                                                                                      '2017-01-13 19:00:53',
                                                                                      '2017-02-17 16:43:55', NULL,
                                                                                      'ZC17359336539766784', 101431,
                                                                                      '2017-02-17 16:43:53', 2, 1, NULL,
                                                                                                                -248733414,
                                                                                                                0, 0,
                                                                                                                'd74add08-9d00-4a18-afd1-51af15a3cbed',
                                                                                                                NULL,
                                                                                                                'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                                                '82e288413b0ad0c62fa14afceeee5c03',
                                                                                                                '00bfd64b58361d989ac8bf13dccc3c9b',
  '2017-01-13 19:00:53', '2017-01-13 19:00:53', 0, 0, 0, 1, 3, 2, 0, 'f13879a4-112c-4c4b-8898-d1dc8f404e83',
        'cfc7170e-52e6-497f-85dd-862cfcb22387', 0, '3ce92916-6650-11e7-bff1-00163e002839', 0, NULL, NULL);
INSERT INTO asset_set (guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES (1, 0, 1050.00, 1000.00, 1.00, 1001.00, '2016-12-28', '2017-02-17', 0.00, 1, 2, 2, '2017-02-17 11:38:16',
                                                                                       'c7f0e04c-9e34-402c-b236-fb9d699b5bd2',
                                                                                       '2016-12-28 13:57:40',
                                                                                       '2017-02-17 12:59:56', NULL,
                                                                                       'ZC275CC8173B35E7FE', 101168,
                                                                                       '2017-02-17 12:59:53', 1, 2,
                                                                                                                 '2017-02-17',
                                                                                                                 1, 0,
                                                                                                                 0,
                                                                                                                 'f6c6e961-0c8e-43c3-b387-5310b2bb0f80',
                                                                                                                 NULL,
                                                                                                                 'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                                                 '02144ee2b7b2fa168dfbaae735dc510f',
                                                                                                                 'cbbaaad2f883a6da3e6d6f77ec41c211',
  '2016-12-28 13:57:40', '2016-12-28 13:57:40', 0, 0, 0, 2, 3, 2, 0, '54b691d6-7355-4a3d-8562-7484d54f5035',
        'b12608b1-726b-4517-b3c0-789e4daeab8e', 0, '3cbcba9c-6650-11e7-bff1-00163e002839', 0, NULL, NULL);
INSERT INTO asset_set (guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES (0, 0, 3774.94, 3720.99, 53.95, 3774.94, '2017-02-22', '2017-02-17', 0.00, 1, 2, 0, NULL,
                                                                                        'd58e06cc-19a8-40b0-9b90-2ff2a20c39ab',
                                                                                        '2017-02-17 15:46:46',
                                                                                        '2017-02-17 15:47:36', NULL,
                                                                                        'ZC959809880865820672', 101918,
                                                                                        '2017-02-17 15:47:34', 3, 0,
                                                                                                                  NULL,
                                                                                                                  -993564434,
                                                                                                                  0, 0,
                                                                                                                  '0f7b6cfe-7c88-4d3e-bd5b-e49cb0f27186',
                                                                                                                  NULL,
                                                                                                                  'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                                                  'e5f9fbc82d10cfbe77adb5a0a3d35aba',
                                                                                                                  '00bfd64b58361d989ac8bf13dccc3c9b',
  '2017-02-17 15:46:46', '2017-02-17 15:46:46', 0, 0, 0, 0, 3, 2, 0, 'dc377713-82c3-4fbd-afc1-62e4a3fd7477',
        'dbbb7166-d277-403b-bc3c-ef845193468f', 1, '3d232ef6-6650-11e7-bff1-00163e002839', 0, NULL, NULL);
INSERT INTO asset_set (guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES (0, 0, 965.38, 882.88, 82.50, 965.38, '2017-02-17', '2017-02-17', 0.00, 1, 2, 0, '2017-02-17 13:20:34',
                                                                                     'e0983d23-eaba-438f-8746-ee5c297ff607',
                                                                                     '2017-02-17 13:20:33',
                                                                                     '2017-02-17 13:43:58', NULL,
                                                                                     'ZC958632357062692864', 101918,
                                                                                     '2017-02-17 13:43:53', 1, 0, NULL,
                                                                                                               489337117,
                                                                                                               0, 0,
                                                                                                               '1dc71566-6267-4f03-b148-94da9f6fe02a',
                                                                                                               NULL,
                                                                                                               'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                                               'b54e56504920ddf226b66f4e9dbdc4b1',
                                                                                                               '00bfd64b58361d989ac8bf13dccc3c9b',
  '2017-02-17 13:20:33', '2017-02-17 13:20:33', 0, 0, 0, 1, 3, 2, 0, 'dc377713-82c3-4fbd-afc1-62e4a3fd7477',
        'dbbb7166-d277-403b-bc3c-ef845193468f', 0, '3d229b55-6650-11e7-bff1-00163e002839', 0, NULL, NULL);

INSERT INTO contract (uuid, unique_id, begin_date, contract_no, end_date, asset_type, month_fee, app_id, customer_id, house_id, actual_end_date, create_time, interest_rate, payment_day_in_month, payment_frequency, periods, repayment_way, total_amount, penalty_interest, active_version_no, repayment_plan_operate_logs, state, financial_contract_uuid, interest_rate_cycle, customer_uuid, repaymented_periods, completion_status, date_field_one, long_field_one, long_field_two, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two)
VALUES ('b12608b1-726b-4517-b3c0-789e4daeab8e', 'fb9e27ed-a3a1-48ec-be14-f3014d1ce955', '2016-12-28',
                                                '2016-236-DK(hk23168343874178145)号', '2019-01-01', NULL, 0.00, 3,
                                                101502, 101657, NULL, '2016-12-28 13:57:40', 0.1100000000, 0, 0, 3, 2,
                                                                      6000.00, 0.2240000000, 1, NULL, 2,
                                                                                                      'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                                      2,
                                                                                                      '54b691d6-7355-4a3d-8562-7484d54f5035',
                                                                                                      0, NULL, NULL,
                                                                                                      NULL, NULL, NULL,
        NULL, NULL, NULL, NULL);
INSERT INTO contract (uuid, unique_id, begin_date, contract_no, end_date, asset_type, month_fee, app_id, customer_id, house_id, actual_end_date, create_time, interest_rate, payment_day_in_month, payment_frequency, periods, repayment_way, total_amount, penalty_interest, active_version_no, repayment_plan_operate_logs, state, financial_contract_uuid, interest_rate_cycle, customer_uuid, repaymented_periods, completion_status, date_field_one, long_field_one, long_field_two, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two)
VALUES ('cfc7170e-52e6-497f-85dd-862cfcb22387', '7597de16-c6bf-4268-a98d-d4fd11fb06bc', '2017-01-13',
                                                '2016-236-DK(12432222)号', '2018-01-14', NULL, 0.00, 3, 101765, 101920,
                                                NULL, '2017-01-13 15:03:44', 0.2200000000, 0, 0, 12, 2, 6600.00,
                                                      0.2240000000, -248733414,
                                                      '[{"content":{0:"ZC17359336527183872,ZC17359336539766784,ZC17359336548155392,ZC17359336569126912,ZC17359336577515520,ZC17359336585904128,ZC17359336598487040,ZC17359336611069952,ZC17359336632041472,ZC17359336787230720,ZC17359336795619328,ZC17359336804007936",1:"ZC17299652113940480,ZC17299652160077824,ZC17299652181049344,ZC17299652202020864,ZC17299652222992384,ZC17299652239769600,ZC17299652256546816,ZC17299652269129728,ZC17299652281712640,ZC17299652306878464,ZC17299652319461376,ZC17299652344627200",2:""},"ipAddress":"117.122.201.170","occurTime":"2017-01-13 19:00:54","triggerEvent":1}]',
  2, 'd2812bc5-5057-4a91-b3fd-9019506f0499', 2, 'f13879a4-112c-4c4b-8898-d1dc8f404e83', 0, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL);
INSERT INTO contract (uuid, unique_id, begin_date, contract_no, end_date, asset_type, month_fee, app_id, customer_id, house_id, actual_end_date, create_time, interest_rate, payment_day_in_month, payment_frequency, periods, repayment_way, total_amount, penalty_interest, active_version_no, repayment_plan_operate_logs, state, financial_contract_uuid, interest_rate_cycle, customer_uuid, repaymented_periods, completion_status, date_field_one, long_field_one, long_field_two, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two)
VALUES ('dbbb7166-d277-403b-bc3c-ef845193468f', '4fbec45a-658b-4814-9c67-534b1ef38cee', '2017-02-16',
                                                '2016-236-DK(12828655)号', '2017-08-17', NULL, 0.00, 3, 102252, 102407,
                                                NULL, '2017-02-16 17:01:40', 0.1800000000, 0, 0, 3, 2, 5500.00,
                                                      0.2240000000, -993564434,
                                                      '[{"content":{0:"ZC958572315164725248,ZC958572316104249344,ZC958572316372684800,ZC958572316641120256,ZC958572317446426624,ZC958572317849079808",1:"ZC948816682941071360,ZC948816683343724544,ZC948816683612160000,ZC948816683880595456,ZC948816684283248640,ZC948816684551684096",2:""},"ipAddress":"101.52.128.162","occurTime":"2017-02-17 13:13:07","triggerEvent":1},{"content":{0:"ZC958632357062692864,ZC958632357465346048,ZC958632357867999232,ZC958632358270652416,ZC958632358404870144,ZC958632358673305600",1:"ZC958572315164725248,ZC958572316104249344,ZC958572316372684800,ZC958572316641120256,ZC958572317446426624,ZC958572317849079808",2:""},"ipAddress":"101.52.128.162","occurTime":"2017-02-17 13:20:34","triggerEvent":1},{"content":{0:"ZC959809880865820672",1:"ZC958632357867999232,ZC958632358270652416,ZC958632358404870144,ZC958632358673305600",2:""},"ipAddress":"117.122.201.170","occurTime":"2017-02-17 15:46:46","triggerEvent":1}]',
  2, 'd2812bc5-5057-4a91-b3fd-9019506f0499', 2, 'dc377713-82c3-4fbd-afc1-62e4a3fd7477', 0, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL);


INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('e6983703-add3-40b2-a1db-7592e574ce3d', '05219404-8890-49da-8b5a-0477bb4e40c1', '2017-02-17 17:05:08',
                                                '2017-02-17 17:05:08', 'FST_RECIEVABLE_ASSET', '20000',
                                                'SND_RECIEVABLE_LOAN_PENALTY', '20000.03', NULL, NULL, 10.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('a804a174-62ee-428c-8d1b-5d4be13b443c', '05219404-8890-49da-8b5a-0477bb4e40c1', '2017-02-17 17:05:08',
                                                '2017-02-17 17:05:08', 'FST_RECIEVABLE_ASSET', '20000',
                                                'SND_RECIEVABLE_OVERDUE_FEE', '20000.06',
                                                'TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE', '20000.06.02', 19.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('2a71eaa4-e2c3-463d-ad36-6ca9b7c527e9', '2f12ea99-786c-44ad-befc-0fec8706d3c0', '2017-02-17 15:34:23',
                                                '2017-02-17 15:55:00', 'FST_RECIEVABLE_ASSET', '20000',
                                                'SND_RECIEVABLE_LOAN_PENALTY', '20000.03', NULL, NULL, 11.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('6aa1ccaf-60c1-41da-9f58-9ddf234dea98', '2f12ea99-786c-44ad-befc-0fec8706d3c0', '2017-02-17 15:34:23',
                                                '2017-02-17 15:55:00', 'FST_RECIEVABLE_ASSET', '20000',
                                                'SND_RECIEVABLE_OVERDUE_FEE', '20000.06',
                                                'TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE', '20000.06.02', 20.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('4cec7590-c3dd-4368-9f92-e4d2108193c8', 'c7f0e04c-9e34-402c-b236-fb9d699b5bd2', '2016-12-28 13:57:40',
                                                '2016-12-28 13:57:40', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_OTHER_FEE', '10000.05', NULL, NULL, 3.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('02d0b2cc-884e-4ae8-8539-66956426ff8c', 'c7f0e04c-9e34-402c-b236-fb9d699b5bd2', '2016-12-28 13:57:40',
                                                '2016-12-28 13:57:40', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE', '10000.03', NULL, NULL,
                                                4.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('ac8d0f49-3e2f-4084-97c9-66af853447a6', 'c7f0e04c-9e34-402c-b236-fb9d699b5bd2', '2016-12-28 13:57:40',
                                                '2016-12-28 13:57:40', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_TECH_FEE', '10000.04', NULL, NULL, 2.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('6a19a7f3-ec30-46f3-a573-b6ba88980a42', 'c7f0e04c-9e34-402c-b236-fb9d699b5bd2', '2017-02-14 02:53:45',
                                                '2017-02-17 11:38:14', 'FST_RECIEVABLE_ASSET', '20000',
                                                'SND_RECIEVABLE_LOAN_PENALTY', '20000.03', NULL, NULL, 10.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('65b73424-0bed-4a06-be2a-581de0f9b19f', 'c7f0e04c-9e34-402c-b236-fb9d699b5bd2', '2017-02-17 11:38:14',
                                                '2017-02-17 11:38:14', 'FST_RECIEVABLE_ASSET', '20000',
                                                'SND_RECIEVABLE_OVERDUE_FEE', '20000.06',
                                                'TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION', '20000.06.01', 10.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('bc167a67-4c3e-41c8-86e8-b826b2b74b56', 'c7f0e04c-9e34-402c-b236-fb9d699b5bd2', '2017-02-17 11:38:15',
                                                '2017-02-17 11:38:15', 'FST_RECIEVABLE_ASSET', '20000',
                                                'SND_RECIEVABLE_OVERDUE_FEE', '20000.06',
                                                'TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE', '20000.06.02', 10.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('a3fbe0be-fc5c-433e-b39d-1696184556c3', 'c7f0e04c-9e34-402c-b236-fb9d699b5bd2', '2017-02-17 11:38:15',
                                                '2017-02-17 11:38:15', 'FST_RECIEVABLE_ASSET', '20000',
                                                'SND_RECIEVABLE_OVERDUE_FEE', '20000.06',
                                                'TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE', '20000.06.03', 10.00);

INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('50918bbd-3eb3-45d0-aae1-b5c01c5a21ca', 0.00, 1000.00, 'FST_RECIEVABLE_ASSET', '20000', 1,
                                                'SND_RECIEVABLE_OVERDUE_LOAN_ASSET', '20000.05',
                                                'TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_PRINCIPLE', '20000.05.01',
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  '54b691d6-7355-4a3d-8562-7484d54f5035', NULL, 'badcfe82-b36e-4fdb-98c0-b2e8b38b8fd0',
  'badcfe82-b36e-4fdb-98c0-b2e8b38b8fd0', '2b03cdfc-50c2-49a3-b62a-92009f987600', '2016-12-28', '2017-02-17 12:59:56',
  '', NULL, 101168, 'b12608b1-726b-4517-b3c0-789e4daeab8e', '2016-12-28 00:00:00',
                    'c70a6e34-b263-4325-9637-26419bb5fedb', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1,
                    'ZC275CC8173B35E7FE', 'c7f0e04c-9e34-402c-b236-fb9d699b5bd2', NULL, NULL, NULL, NULL,
        '0a83bf96-6557-4e25-ba15-465799d6b95f');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('39cdaf0b-5807-4f71-8bfe-d344b496da26', 0.00, 1.00, 'FST_RECIEVABLE_ASSET', '20000', 1,
                                                'SND_RECIEVABLE_OVERDUE_LOAN_ASSET', '20000.05',
                                                'TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_INTEREST', '20000.05.02',
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  '54b691d6-7355-4a3d-8562-7484d54f5035', NULL, 'a96c95df-cb04-4806-8aa9-81618e4d0377',
  'a96c95df-cb04-4806-8aa9-81618e4d0377', '16b8d006-f5d3-4c20-941f-6a8c1f479969', '2016-12-28', '2017-02-17 12:59:56',
  '', NULL, 101168, 'b12608b1-726b-4517-b3c0-789e4daeab8e', '2016-12-28 00:00:00',
                    'c70a6e34-b263-4325-9637-26419bb5fedb', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1,
                    'ZC275CC8173B35E7FE', 'c7f0e04c-9e34-402c-b236-fb9d699b5bd2', NULL, NULL, NULL, NULL,
        '0a83bf96-6557-4e25-ba15-465799d6b95f');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('b15118be-c798-4a82-aec2-f887623f8ebf', 0.00, 2.00, 'FST_RECIEVABLE_ASSET', '20000', 1,
                                                'SND_RECIEVABLE_OVERDUE_LOAN_ASSET', '20000.05',
                                                'TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_TECH_FEE', '20000.05.04',
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  '54b691d6-7355-4a3d-8562-7484d54f5035', NULL, '00971cd2-100c-4ea0-9994-6e2acdca1ebb',
  '00971cd2-100c-4ea0-9994-6e2acdca1ebb', 'f597d6be-2c40-4f04-b2d6-39020a40de2e', '2016-12-28', '2017-02-17 12:59:56',
  '', NULL, 101168, 'b12608b1-726b-4517-b3c0-789e4daeab8e', '2016-12-28 00:00:00',
                    'c70a6e34-b263-4325-9637-26419bb5fedb', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1,
                    'ZC275CC8173B35E7FE', 'c7f0e04c-9e34-402c-b236-fb9d699b5bd2', NULL, NULL, NULL, NULL,
        '0a83bf96-6557-4e25-ba15-465799d6b95f');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('d7ba99bb-00fd-4e94-b51c-29cce230d75d', 0.00, 3.00, 'FST_RECIEVABLE_ASSET', '20000', 1,
                                                'SND_RECIEVABLE_OVERDUE_LOAN_ASSET', '20000.05',
                                                'TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_OTHER_FEE', '20000.05.05',
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  '54b691d6-7355-4a3d-8562-7484d54f5035', NULL, '8b60d326-e9e5-4515-ab60-f02b31c715ff',
  '8b60d326-e9e5-4515-ab60-f02b31c715ff', '3f250064-deae-4f5f-a435-69a42da2c4ef', '2016-12-28', '2017-02-17 12:59:56',
  '', NULL, 101168, 'b12608b1-726b-4517-b3c0-789e4daeab8e', '2016-12-28 00:00:00',
                    'c70a6e34-b263-4325-9637-26419bb5fedb', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1,
                    'ZC275CC8173B35E7FE', 'c7f0e04c-9e34-402c-b236-fb9d699b5bd2', NULL, NULL, NULL, NULL,
        '0a83bf96-6557-4e25-ba15-465799d6b95f');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('eb7e18a8-0a1a-49ad-b615-b55e9e242fe8', 0.00, 10.00, 'FST_RECIEVABLE_ASSET', '20000', 1,
                                                'SND_RECIEVABLE_OVERDUE_FEE', '20000.06',
                                                'TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION', '20000.06.01',
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  '54b691d6-7355-4a3d-8562-7484d54f5035', NULL, '84ad9e2b-0f6d-4193-b175-faa70f038c0e',
  '84ad9e2b-0f6d-4193-b175-faa70f038c0e', '310096e8-91f6-41bf-88b4-533e1fb85bb2', '2016-12-28', '2017-02-17 12:59:56',
  '', NULL, 101168, 'b12608b1-726b-4517-b3c0-789e4daeab8e', '2016-12-28 00:00:00',
                    'c70a6e34-b263-4325-9637-26419bb5fedb', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1,
                    'ZC275CC8173B35E7FE', 'c7f0e04c-9e34-402c-b236-fb9d699b5bd2', NULL, NULL, NULL, NULL,
        '0a83bf96-6557-4e25-ba15-465799d6b95f');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('25384c0e-d68c-458b-afa3-64ce10e59712', 0.00, 10.00, 'FST_RECIEVABLE_ASSET', '20000', 1,
                                                'SND_RECIEVABLE_OVERDUE_FEE', '20000.06',
                                                'TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE', '20000.06.02',
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  '54b691d6-7355-4a3d-8562-7484d54f5035', NULL, '69193715-94d9-4aeb-9136-338f7296aac0',
  '69193715-94d9-4aeb-9136-338f7296aac0', 'a42cb2ec-cabf-4200-b215-d146d4acf0e8', '2016-12-28', '2017-02-17 12:59:56',
  '', NULL, 101168, 'b12608b1-726b-4517-b3c0-789e4daeab8e', '2016-12-28 00:00:00',
                    'c70a6e34-b263-4325-9637-26419bb5fedb', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1,
                    'ZC275CC8173B35E7FE', 'c7f0e04c-9e34-402c-b236-fb9d699b5bd2', NULL, NULL, NULL, NULL,
        '0a83bf96-6557-4e25-ba15-465799d6b95f');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('1d2927d4-9b18-440c-bea6-35c635bc94e0', 0.00, 10.00, 'FST_RECIEVABLE_ASSET', '20000', 1,
                                                'SND_RECIEVABLE_LOAN_PENALTY', '20000.03', NULL, NULL,
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  '54b691d6-7355-4a3d-8562-7484d54f5035', NULL, '4c11c780-dab4-4c63-b2e6-a4328d143e5c',
  '4c11c780-dab4-4c63-b2e6-a4328d143e5c', '4507d791-98ef-4c14-a948-f46397bae41b', '2016-12-28', '2017-02-17 12:59:56',
  '', NULL, 101168, 'b12608b1-726b-4517-b3c0-789e4daeab8e', '2016-12-28 00:00:00',
                    'c70a6e34-b263-4325-9637-26419bb5fedb', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1,
                    'ZC275CC8173B35E7FE', 'c7f0e04c-9e34-402c-b236-fb9d699b5bd2', NULL, NULL, NULL, NULL,
        '0a83bf96-6557-4e25-ba15-465799d6b95f');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('cbfa4b3c-a142-4cba-baa5-29059366e31f', 0.00, 10.00, 'FST_RECIEVABLE_ASSET', '20000', 1,
                                                'SND_RECIEVABLE_OVERDUE_FEE', '20000.06',
                                                'TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE', '20000.06.03',
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  '54b691d6-7355-4a3d-8562-7484d54f5035', NULL, 'e69a952d-1e5f-4a6e-8648-6eb7f66118d5',
  'e69a952d-1e5f-4a6e-8648-6eb7f66118d5', 'aa7c6da2-ba7b-4aac-8499-20d03091df2d', '2016-12-28', '2017-02-17 12:59:56',
  '', NULL, 101168, 'b12608b1-726b-4517-b3c0-789e4daeab8e', '2016-12-28 00:00:00',
                    'c70a6e34-b263-4325-9637-26419bb5fedb', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1,
                    'ZC275CC8173B35E7FE', 'c7f0e04c-9e34-402c-b236-fb9d699b5bd2', NULL, NULL, NULL, NULL,
        '0a83bf96-6557-4e25-ba15-465799d6b95f');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('de96f582-914c-486a-8e6f-601c7f470130', 0.00, 4.00, 'FST_RECIEVABLE_ASSET', '20000', 1,
                                                'SND_RECIEVABLE_OVERDUE_LOAN_ASSET', '20000.05',
                                                'TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_LOAN_SERVICE_FEE', '20000.05.03',
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  '54b691d6-7355-4a3d-8562-7484d54f5035', NULL, '2ea1d0a8-5dcc-4e5d-8f96-b130546731e4',
  '2ea1d0a8-5dcc-4e5d-8f96-b130546731e4', 'f3cb394a-5eca-493f-8a5b-d1aa259ee92c', '2016-12-28', '2017-02-17 12:59:56',
  '', NULL, 101168, 'b12608b1-726b-4517-b3c0-789e4daeab8e', '2016-12-28 00:00:00',
                    'c70a6e34-b263-4325-9637-26419bb5fedb', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1,
                    'ZC275CC8173B35E7FE', 'c7f0e04c-9e34-402c-b236-fb9d699b5bd2', NULL, NULL, NULL, NULL,
        '0a83bf96-6557-4e25-ba15-465799d6b95f');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('badcfe82-b36e-4fdb-98c0-b2e8b38b8fd0', 1000.00, 0.00, 'FST_BANK_SAVING', '60000', 1, '001053110000001', '60000.99',
                                           'TRD_BANK_SAVING_GENERAL_PRINCIPAL', '60000.1000.01',
                                           'a02c02b9-6f98-11e6-bf08-00163e002839', '', NULL, NULL, NULL,
                                                                                   '2b03cdfc-50c2-49a3-b62a-92009f987600',
                                                                                   '2016-12-28', '2017-02-17 12:59:56',
                                                                                   '', NULL, 101168,
    'b12608b1-726b-4517-b3c0-789e4daeab8e', '2016-12-28 00:00:00', 'c70a6e34-b263-4325-9637-26419bb5fedb',
    '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1, 'ZC275CC8173B35E7FE', 'c7f0e04c-9e34-402c-b236-fb9d699b5bd2', NULL,
    NULL, NULL, NULL, '0a83bf96-6557-4e25-ba15-465799d6b95f');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('a96c95df-cb04-4806-8aa9-81618e4d0377', 1.00, 0.00, 'FST_BANK_SAVING', '60000', 1, '001053110000001', '60000.99',
                                           'TRD_BANK_SAVING_GENERAL_INTEREST', '60000.1000.02',
                                           'a02c02b9-6f98-11e6-bf08-00163e002839', '', NULL, NULL, NULL,
                                                                                   '16b8d006-f5d3-4c20-941f-6a8c1f479969',
                                                                                   '2016-12-28', '2017-02-17 12:59:56',
                                                                                   '', NULL, 101168,
    'b12608b1-726b-4517-b3c0-789e4daeab8e', '2016-12-28 00:00:00', 'c70a6e34-b263-4325-9637-26419bb5fedb',
    '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1, 'ZC275CC8173B35E7FE', 'c7f0e04c-9e34-402c-b236-fb9d699b5bd2', NULL,
    NULL, NULL, NULL, '0a83bf96-6557-4e25-ba15-465799d6b95f');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('00971cd2-100c-4ea0-9994-6e2acdca1ebb', 2.00, 0.00, 'FST_BANK_SAVING', '60000', 1, '001053110000001', '60000.99',
                                           'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_TECH_FEE', '60000.1000.04',
                                           'a02c02b9-6f98-11e6-bf08-00163e002839', '', NULL, NULL, NULL,
                                                                                   'f597d6be-2c40-4f04-b2d6-39020a40de2e',
                                                                                   '2016-12-28', '2017-02-17 12:59:56',
                                                                                   '', NULL, 101168,
    'b12608b1-726b-4517-b3c0-789e4daeab8e', '2016-12-28 00:00:00', 'c70a6e34-b263-4325-9637-26419bb5fedb',
    '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1, 'ZC275CC8173B35E7FE', 'c7f0e04c-9e34-402c-b236-fb9d699b5bd2', NULL,
    NULL, NULL, NULL, '0a83bf96-6557-4e25-ba15-465799d6b95f');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('8b60d326-e9e5-4515-ab60-f02b31c715ff', 3.00, 0.00, 'FST_BANK_SAVING', '60000', 1, '001053110000001', '60000.99',
                                           'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OTHER_FEE', '60000.1000.05',
                                           'a02c02b9-6f98-11e6-bf08-00163e002839', '', NULL, NULL, NULL,
                                                                                   '3f250064-deae-4f5f-a435-69a42da2c4ef',
                                                                                   '2016-12-28', '2017-02-17 12:59:56',
                                                                                   '', NULL, 101168,
    'b12608b1-726b-4517-b3c0-789e4daeab8e', '2016-12-28 00:00:00', 'c70a6e34-b263-4325-9637-26419bb5fedb',
    '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1, 'ZC275CC8173B35E7FE', 'c7f0e04c-9e34-402c-b236-fb9d699b5bd2', NULL,
    NULL, NULL, NULL, '0a83bf96-6557-4e25-ba15-465799d6b95f');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('84ad9e2b-0f6d-4193-b175-faa70f038c0e', 10.00, 0.00, 'FST_BANK_SAVING', '60000', 1, '001053110000001', '60000.99',
                                           'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION', '60000.1000.07',
                                           'a02c02b9-6f98-11e6-bf08-00163e002839', '', NULL, NULL, NULL,
                                                                                   '310096e8-91f6-41bf-88b4-533e1fb85bb2',
                                                                                   '2016-12-28', '2017-02-17 12:59:56',
                                                                                   '', NULL, 101168,
    'b12608b1-726b-4517-b3c0-789e4daeab8e', '2016-12-28 00:00:00', 'c70a6e34-b263-4325-9637-26419bb5fedb',
    '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1, 'ZC275CC8173B35E7FE', 'c7f0e04c-9e34-402c-b236-fb9d699b5bd2', NULL,
    NULL, NULL, NULL, '0a83bf96-6557-4e25-ba15-465799d6b95f');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('69193715-94d9-4aeb-9136-338f7296aac0', 10.00, 0.00, 'FST_BANK_SAVING', '60000', 1, '001053110000001', '60000.99',
                                           'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE',
                                           '60000.1000.08', 'a02c02b9-6f98-11e6-bf08-00163e002839', '', NULL, NULL,
                                                                                                    NULL,
                                                                                                    'a42cb2ec-cabf-4200-b215-d146d4acf0e8',
                                                                                                    '2016-12-28',
                                                                                                    '2017-02-17 12:59:56',
                                                                                                    '', NULL, 101168,
    'b12608b1-726b-4517-b3c0-789e4daeab8e', '2016-12-28 00:00:00', 'c70a6e34-b263-4325-9637-26419bb5fedb',
    '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1, 'ZC275CC8173B35E7FE', 'c7f0e04c-9e34-402c-b236-fb9d699b5bd2', NULL,
    NULL, NULL, NULL, '0a83bf96-6557-4e25-ba15-465799d6b95f');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('4c11c780-dab4-4c63-b2e6-a4328d143e5c', 10.00, 0.00, 'FST_BANK_SAVING', '60000', 1, '001053110000001', '60000.99',
                                           'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY', '60000.1000.06',
                                           'a02c02b9-6f98-11e6-bf08-00163e002839', '', NULL, NULL, NULL,
                                                                                   '4507d791-98ef-4c14-a948-f46397bae41b',
                                                                                   '2016-12-28', '2017-02-17 12:59:56',
                                                                                   '', NULL, 101168,
    'b12608b1-726b-4517-b3c0-789e4daeab8e', '2016-12-28 00:00:00', 'c70a6e34-b263-4325-9637-26419bb5fedb',
    '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1, 'ZC275CC8173B35E7FE', 'c7f0e04c-9e34-402c-b236-fb9d699b5bd2', NULL,
    NULL, NULL, NULL, '0a83bf96-6557-4e25-ba15-465799d6b95f');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('e69a952d-1e5f-4a6e-8648-6eb7f66118d5', 10.00, 0.00, 'FST_BANK_SAVING', '60000', 1, '001053110000001', '60000.99',
                                           'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE', '60000.1000.09',
                                           'a02c02b9-6f98-11e6-bf08-00163e002839', '', NULL, NULL, NULL,
                                                                                   'aa7c6da2-ba7b-4aac-8499-20d03091df2d',
                                                                                   '2016-12-28', '2017-02-17 12:59:56',
                                                                                   '', NULL, 101168,
    'b12608b1-726b-4517-b3c0-789e4daeab8e', '2016-12-28 00:00:00', 'c70a6e34-b263-4325-9637-26419bb5fedb',
    '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1, 'ZC275CC8173B35E7FE', 'c7f0e04c-9e34-402c-b236-fb9d699b5bd2', NULL,
    NULL, NULL, NULL, '0a83bf96-6557-4e25-ba15-465799d6b95f');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('2ea1d0a8-5dcc-4e5d-8f96-b130546731e4', 4.00, 0.00, 'FST_BANK_SAVING', '60000', 1, '001053110000001', '60000.99',
                                           'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_LOAN_SERVICE_FEE', '60000.1000.03',
                                           'a02c02b9-6f98-11e6-bf08-00163e002839', '', NULL, NULL, NULL,
                                                                                   'f3cb394a-5eca-493f-8a5b-d1aa259ee92c',
                                                                                   '2016-12-28', '2017-02-17 12:59:56',
                                                                                   '', NULL, 101168,
    'b12608b1-726b-4517-b3c0-789e4daeab8e', '2016-12-28 00:00:00', 'c70a6e34-b263-4325-9637-26419bb5fedb',
    '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1, 'ZC275CC8173B35E7FE', 'c7f0e04c-9e34-402c-b236-fb9d699b5bd2', NULL,
    NULL, NULL, NULL, '0a83bf96-6557-4e25-ba15-465799d6b95f');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('ee6baa7b-3bd2-4608-a780-54ace4d0a43c', 0.00, 882.88, 'FST_RECIEVABLE_ASSET', '20000', 1,
                                                'SND_RECIEVABLE_LOAN_ASSET', '20000.01',
                                                'TRD_RECIEVABLE_LOAN_ASSET_PRINCIPLE', '20000.01.01',
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  'dc377713-82c3-4fbd-afc1-62e4a3fd7477', NULL, 'f83b249c-b73f-41ec-bcd2-87a913880a74',
  'f83b249c-b73f-41ec-bcd2-87a913880a74', '2a8453f6-3b14-4b4f-bf2b-e2bab2ebaee5', '2017-02-17', '2017-02-17 13:43:58',
  '', NULL, 101918, 'dbbb7166-d277-403b-bc3c-ef845193468f', '2017-02-17 00:00:00',
                    'e70eb2cc-8567-4819-9f18-73680294c32d', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1,
                    'ZC958632357062692864', 'e0983d23-eaba-438f-8746-ee5c297ff607', NULL, NULL, NULL, NULL,
        '24e4468c-5763-4200-a8e0-ad375f04e310');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('17f98c4f-ac90-4f0e-bdc1-f1fd36635ef0', 0.00, 82.50, 'FST_RECIEVABLE_ASSET', '20000', 1, 'SND_RECIEVABLE_LOAN_ASSET',
                                           '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_INTEREST', '20000.01.02',
                                           'a02c02b9-6f98-11e6-bf08-00163e002839',
    'dc377713-82c3-4fbd-afc1-62e4a3fd7477', NULL, 'a6c7cbe8-37c4-4648-8bff-df92c3663d5e',
    'a6c7cbe8-37c4-4648-8bff-df92c3663d5e', 'a1a7419c-3538-451c-9bb8-e70154bf0046', '2017-02-17', '2017-02-17 13:43:58',
    '', NULL, 101918, 'dbbb7166-d277-403b-bc3c-ef845193468f', '2017-02-17 00:00:00',
                      'e70eb2cc-8567-4819-9f18-73680294c32d', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1,
                      'ZC958632357062692864', 'e0983d23-eaba-438f-8746-ee5c297ff607', NULL, NULL, NULL, NULL,
   '24e4468c-5763-4200-a8e0-ad375f04e310');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('f83b249c-b73f-41ec-bcd2-87a913880a74', 882.88, 0.00, 'FST_BANK_SAVING', '60000', 1, '001053110000001', '60000.99',
                                           'TRD_BANK_SAVING_GENERAL_PRINCIPAL', '60000.1000.01',
                                           'a02c02b9-6f98-11e6-bf08-00163e002839', '', NULL, NULL, NULL,
                                                                                   '2a8453f6-3b14-4b4f-bf2b-e2bab2ebaee5',
                                                                                   '2017-02-17', '2017-02-17 13:43:58',
                                                                                   '', NULL, 101918,
    'dbbb7166-d277-403b-bc3c-ef845193468f', '2017-02-17 00:00:00', 'e70eb2cc-8567-4819-9f18-73680294c32d',
    '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1, 'ZC958632357062692864', 'e0983d23-eaba-438f-8746-ee5c297ff607',
    NULL, NULL, NULL, NULL, '24e4468c-5763-4200-a8e0-ad375f04e310');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('a6c7cbe8-37c4-4648-8bff-df92c3663d5e', 82.50, 0.00, 'FST_BANK_SAVING', '60000', 1, '001053110000001', '60000.99',
                                           'TRD_BANK_SAVING_GENERAL_INTEREST', '60000.1000.02',
                                           'a02c02b9-6f98-11e6-bf08-00163e002839', '', NULL, NULL, NULL,
                                                                                   'a1a7419c-3538-451c-9bb8-e70154bf0046',
                                                                                   '2017-02-17', '2017-02-17 13:43:58',
                                                                                   '', NULL, 101918,
    'dbbb7166-d277-403b-bc3c-ef845193468f', '2017-02-17 00:00:00', 'e70eb2cc-8567-4819-9f18-73680294c32d',
    '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1, 'ZC958632357062692864', 'e0983d23-eaba-438f-8746-ee5c297ff607',
    NULL, NULL, NULL, NULL, '24e4468c-5763-4200-a8e0-ad375f04e310');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('400712e1-763e-49ea-ad15-87ddb32249ae', 69.25, 0.00, 'FST_DEFERRED_INCOME', '100000', 0,
                                                'SND_DEFERRED_INCOME_INTEREST_ESTIMATE', '100000.01', NULL, NULL,
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  'dc377713-82c3-4fbd-afc1-62e4a3fd7477', NULL, '4c599c74-03cd-4e55-8269-cca784f11721',
  '4c599c74-03cd-4e55-8269-cca784f11721', 'b72c3c20-1e50-47cc-baf6-6347747c0944', '2017-02-18', '2017-02-17 14:49:58',
  '', NULL, 101918, 'dbbb7166-d277-403b-bc3c-ef845193468f', '2017-02-18 00:00:00',
                    '5a215040-4c8a-4d02-a41d-57a27dccf097', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1,
                    'ZC958632357465346048', '02ded3b5-4441-407f-9478-e05a5ab63f9d', NULL, NULL, NULL, NULL,
        '186799d5-e9b8-4db8-bcb6-c4b263b7a2e3');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('4c599c74-03cd-4e55-8269-cca784f11721', 0.00, 69.25, 'FST_REVENUE', '70000', 0, 'SND_REVENUE_INTEREST', '70000.03',
                                           NULL, NULL, 'a02c02b9-6f98-11e6-bf08-00163e002839', '', NULL, NULL, NULL,
                                                                                               'b72c3c20-1e50-47cc-baf6-6347747c0944',
                                                                                               '2017-02-18',
                                                                                               '2017-02-17 14:49:58',
                                                                                               '', NULL, 101918,
    'dbbb7166-d277-403b-bc3c-ef845193468f', '2017-02-18 00:00:00', '5a215040-4c8a-4d02-a41d-57a27dccf097',
    '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1, 'ZC958632357465346048', '02ded3b5-4441-407f-9478-e05a5ab63f9d',
    NULL, NULL, NULL, NULL, '186799d5-e9b8-4db8-bcb6-c4b263b7a2e3');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('f8e311be-645f-4f90-bc32-dff57bda613f', 0.00, 896.13, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                                                'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL,
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  'dc377713-82c3-4fbd-afc1-62e4a3fd7477', NULL, 'a3a75a55-6bfa-43fe-b27b-27b249bf0eae',
  'a3a75a55-6bfa-43fe-b27b-27b249bf0eae', '0fe42dfd-c68f-40f1-9ef6-85b8248570ef', '2017-02-18', '2017-02-17 14:49:58',
  '', NULL, 101918, 'dbbb7166-d277-403b-bc3c-ef845193468f', '2017-02-18 00:00:00',
                    '5a215040-4c8a-4d02-a41d-57a27dccf097', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1,
                    'ZC958632357465346048', '02ded3b5-4441-407f-9478-e05a5ab63f9d', NULL, NULL, NULL, NULL,
        '186799d5-e9b8-4db8-bcb6-c4b263b7a2e3');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('410088a1-5833-4fb2-a309-8c1f5f84c797', 0.00, 69.25, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                                                'SND_UNEARNED_LOAN_ASSET_INTEREST', '10000.01', NULL, NULL,
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  'dc377713-82c3-4fbd-afc1-62e4a3fd7477', NULL, 'f000741b-185d-4b46-a2b3-f8b6313e87e6',
  'f000741b-185d-4b46-a2b3-f8b6313e87e6', 'c15f7cbc-5857-4739-88d9-2bfdcbfb4e3a', '2017-02-18', '2017-02-17 14:49:58',
  '', NULL, 101918, 'dbbb7166-d277-403b-bc3c-ef845193468f', '2017-02-18 00:00:00',
                    '5a215040-4c8a-4d02-a41d-57a27dccf097', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1,
                    'ZC958632357465346048', '02ded3b5-4441-407f-9478-e05a5ab63f9d', NULL, NULL, NULL, NULL,
        '186799d5-e9b8-4db8-bcb6-c4b263b7a2e3');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('a3a75a55-6bfa-43fe-b27b-27b249bf0eae', 896.13, 0.00, 'FST_BANK_SAVING', '60000', 1, '001053110000001', '60000.99',
                                           'TRD_BANK_SAVING_GENERAL_PRINCIPAL', '60000.1000.01',
                                           'a02c02b9-6f98-11e6-bf08-00163e002839', '', NULL, NULL, NULL,
                                                                                   '0fe42dfd-c68f-40f1-9ef6-85b8248570ef',
                                                                                   '2017-02-18', '2017-02-17 14:49:58',
                                                                                   '', NULL, 101918,
    'dbbb7166-d277-403b-bc3c-ef845193468f', '2017-02-18 00:00:00', '5a215040-4c8a-4d02-a41d-57a27dccf097',
    '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1, 'ZC958632357465346048', '02ded3b5-4441-407f-9478-e05a5ab63f9d',
    NULL, NULL, NULL, NULL, '186799d5-e9b8-4db8-bcb6-c4b263b7a2e3');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('f000741b-185d-4b46-a2b3-f8b6313e87e6', 69.25, 0.00, 'FST_BANK_SAVING', '60000', 1, '001053110000001', '60000.99',
                                           'TRD_BANK_SAVING_GENERAL_INTEREST', '60000.1000.02',
                                           'a02c02b9-6f98-11e6-bf08-00163e002839', '', NULL, NULL, NULL,
                                                                                   'c15f7cbc-5857-4739-88d9-2bfdcbfb4e3a',
                                                                                   '2017-02-18', '2017-02-17 14:49:58',
                                                                                   '', NULL, 101918,
    'dbbb7166-d277-403b-bc3c-ef845193468f', '2017-02-18 00:00:00', '5a215040-4c8a-4d02-a41d-57a27dccf097',
    '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1, 'ZC958632357465346048', '02ded3b5-4441-407f-9478-e05a5ab63f9d',
    NULL, NULL, NULL, NULL, '186799d5-e9b8-4db8-bcb6-c4b263b7a2e3');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('71eccf94-e875-4b24-8d5c-38384f93e118', 0.00, 500.00, 'FST_RECIEVABLE_ASSET', '20000', 1,
                                                'SND_RECIEVABLE_OVERDUE_LOAN_ASSET', '20000.05',
                                                'TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_PRINCIPLE', '20000.05.01',
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  'f13879a4-112c-4c4b-8898-d1dc8f404e83', NULL, '06408f35-3bd7-4349-90ae-c63813081c6f',
  '06408f35-3bd7-4349-90ae-c63813081c6f', '5e36e2fb-c9fd-4b25-bfbe-a64fd0d7dc37', '2017-01-14', '2017-02-17 15:35:35',
  '', NULL, 101431, 'cfc7170e-52e6-497f-85dd-862cfcb22387', '2017-01-14 00:00:00',
                    '1ac4495e-e0b9-49f4-836b-a6eb134be202', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1,
                    'ZC17359336539766784', '2f12ea99-786c-44ad-befc-0fec8706d3c0', NULL, NULL, NULL, NULL,
        '90516e4c-936a-48f5-ada0-09044e5d7196');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('06408f35-3bd7-4349-90ae-c63813081c6f', 500.00, 0.00, 'FST_BANK_SAVING', '60000', 1, '001053110000001', '60000.99',
                                           'TRD_BANK_SAVING_GENERAL_PRINCIPAL', '60000.1000.01',
                                           'a02c02b9-6f98-11e6-bf08-00163e002839', '', NULL, NULL, NULL,
                                                                                   '5e36e2fb-c9fd-4b25-bfbe-a64fd0d7dc37',
                                                                                   '2017-01-14', '2017-02-17 15:35:35',
                                                                                   '', NULL, 101431,
    'cfc7170e-52e6-497f-85dd-862cfcb22387', '2017-01-14 00:00:00', '1ac4495e-e0b9-49f4-836b-a6eb134be202',
    '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1, 'ZC17359336539766784', '2f12ea99-786c-44ad-befc-0fec8706d3c0', NULL,
    NULL, NULL, NULL, '90516e4c-936a-48f5-ada0-09044e5d7196');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('4618e2b7-98ba-4ad4-9548-977e9c8b7f32', 0.00, 5.83, 'FST_RECIEVABLE_ASSET', '20000', 1,
                                                'SND_RECIEVABLE_OVERDUE_LOAN_ASSET', '20000.05',
                                                'TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_PRINCIPLE', '20000.05.01',
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  'f13879a4-112c-4c4b-8898-d1dc8f404e83', NULL, '7c8a90e4-e649-4dd9-9ead-ea929203762f',
  '7c8a90e4-e649-4dd9-9ead-ea929203762f', '06772de2-d44f-4440-9020-3344b43527a4', '2017-01-14', '2017-02-17 15:47:36',
  '', NULL, 101431, 'cfc7170e-52e6-497f-85dd-862cfcb22387', '2017-01-14 00:00:00',
                    '672fb655-5348-4cab-b0c7-2d0c763fa8ba', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1,
                    'ZC17359336539766784', '2f12ea99-786c-44ad-befc-0fec8706d3c0', NULL, NULL, NULL, NULL,
        '8e82fe10-d60b-43fe-bd6b-91e185a11036');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('20a8ee39-f05a-40a3-b51d-e99c26eb3ef7', 0.00, 94.17, 'FST_RECIEVABLE_ASSET', '20000', 1,
                                                'SND_RECIEVABLE_OVERDUE_LOAN_ASSET', '20000.05',
                                                'TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_INTEREST', '20000.05.02',
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  'f13879a4-112c-4c4b-8898-d1dc8f404e83', NULL, '124b27d2-2fb1-47a5-bef6-7f66ec9811f8',
  '124b27d2-2fb1-47a5-bef6-7f66ec9811f8', '0874f260-36cd-4113-b6a0-86165275ac62', '2017-01-14', '2017-02-17 15:47:36',
  '', NULL, 101431, 'cfc7170e-52e6-497f-85dd-862cfcb22387', '2017-01-14 00:00:00',
                    '672fb655-5348-4cab-b0c7-2d0c763fa8ba', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1,
                    'ZC17359336539766784', '2f12ea99-786c-44ad-befc-0fec8706d3c0', NULL, NULL, NULL, NULL,
        '8e82fe10-d60b-43fe-bd6b-91e185a11036');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('7c8a90e4-e649-4dd9-9ead-ea929203762f', 5.83, 0.00, 'FST_BANK_SAVING', '60000', 1, '001053110000001', '60000.99',
                                           'TRD_BANK_SAVING_GENERAL_PRINCIPAL', '60000.1000.01',
                                           'a02c02b9-6f98-11e6-bf08-00163e002839', '', NULL, NULL, NULL,
                                                                                   '06772de2-d44f-4440-9020-3344b43527a4',
                                                                                   '2017-01-14', '2017-02-17 15:47:36',
                                                                                   '', NULL, 101431,
    'cfc7170e-52e6-497f-85dd-862cfcb22387', '2017-01-14 00:00:00', '672fb655-5348-4cab-b0c7-2d0c763fa8ba',
    '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1, 'ZC17359336539766784', '2f12ea99-786c-44ad-befc-0fec8706d3c0', NULL,
    NULL, NULL, NULL, '8e82fe10-d60b-43fe-bd6b-91e185a11036');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('124b27d2-2fb1-47a5-bef6-7f66ec9811f8', 94.17, 0.00, 'FST_BANK_SAVING', '60000', 1, '001053110000001', '60000.99',
                                           'TRD_BANK_SAVING_GENERAL_INTEREST', '60000.1000.02',
                                           'a02c02b9-6f98-11e6-bf08-00163e002839', '', NULL, NULL, NULL,
                                                                                   '0874f260-36cd-4113-b6a0-86165275ac62',
                                                                                   '2017-01-14', '2017-02-17 15:47:36',
                                                                                   '', NULL, 101431,
    'cfc7170e-52e6-497f-85dd-862cfcb22387', '2017-01-14 00:00:00', '672fb655-5348-4cab-b0c7-2d0c763fa8ba',
    '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1, 'ZC17359336539766784', '2f12ea99-786c-44ad-befc-0fec8706d3c0', NULL,
    NULL, NULL, NULL, '8e82fe10-d60b-43fe-bd6b-91e185a11036');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('815da445-d363-4c95-a221-b4c7882c6f1b', 53.95, 0.00, 'FST_DEFERRED_INCOME', '100000', 0,
                                                'SND_DEFERRED_INCOME_INTEREST_ESTIMATE', '100000.01', NULL, NULL,
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  'dc377713-82c3-4fbd-afc1-62e4a3fd7477', NULL, '8c92d6b8-e4c6-415f-b4d7-27eaab9ecdc9',
  '8c92d6b8-e4c6-415f-b4d7-27eaab9ecdc9', '85e6f55e-4ae7-4cb7-a844-b55d89b41d06', '2017-02-22', '2017-02-17 15:47:36',
  '', NULL, 101918, 'dbbb7166-d277-403b-bc3c-ef845193468f', '2017-02-22 00:00:00',
                    '6d779b7f-ecc0-4217-86be-ff3807a41a8f', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1,
                    'ZC959809880865820672', 'd58e06cc-19a8-40b0-9b90-2ff2a20c39ab', NULL, NULL, NULL, NULL,
        '3f6fc0ec-29d6-4eb3-947f-0b5ae9094e23');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('8c92d6b8-e4c6-415f-b4d7-27eaab9ecdc9', 0.00, 53.95, 'FST_REVENUE', '70000', 0, 'SND_REVENUE_INTEREST', '70000.03',
                                           NULL, NULL, 'a02c02b9-6f98-11e6-bf08-00163e002839', '', NULL, NULL, NULL,
                                                                                               '85e6f55e-4ae7-4cb7-a844-b55d89b41d06',
                                                                                               '2017-02-22',
                                                                                               '2017-02-17 15:47:36',
                                                                                               '', NULL, 101918,
    'dbbb7166-d277-403b-bc3c-ef845193468f', '2017-02-22 00:00:00', '6d779b7f-ecc0-4217-86be-ff3807a41a8f',
    '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1, 'ZC959809880865820672', 'd58e06cc-19a8-40b0-9b90-2ff2a20c39ab',
    NULL, NULL, NULL, NULL, '3f6fc0ec-29d6-4eb3-947f-0b5ae9094e23');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('8fff66a4-200a-4238-8cfd-a592dca7963d', 0.00, 3720.99, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                                                'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL,
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  'dc377713-82c3-4fbd-afc1-62e4a3fd7477', NULL, '206328e3-b8ea-4b36-a741-faa2b8b22d8e',
  '206328e3-b8ea-4b36-a741-faa2b8b22d8e', '7441db43-6eef-4b42-81fa-20dfead23df0', '2017-02-22', '2017-02-17 15:47:36',
  '', NULL, 101918, 'dbbb7166-d277-403b-bc3c-ef845193468f', '2017-02-22 00:00:00',
                    '6d779b7f-ecc0-4217-86be-ff3807a41a8f', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1,
                    'ZC959809880865820672', 'd58e06cc-19a8-40b0-9b90-2ff2a20c39ab', NULL, NULL, NULL, NULL,
        '3f6fc0ec-29d6-4eb3-947f-0b5ae9094e23');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('b87466ed-e397-445e-9073-46977d2ac91d', 0.00, 53.95, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                                                'SND_UNEARNED_LOAN_ASSET_INTEREST', '10000.01', NULL, NULL,
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  'dc377713-82c3-4fbd-afc1-62e4a3fd7477', NULL, '247b7a23-e12e-4008-aa46-d9fb4a86d818',
  '247b7a23-e12e-4008-aa46-d9fb4a86d818', 'd75b64c2-d9d7-49cf-b58a-1a71e040fcff', '2017-02-22', '2017-02-17 15:47:36',
  '', NULL, 101918, 'dbbb7166-d277-403b-bc3c-ef845193468f', '2017-02-22 00:00:00',
                    '6d779b7f-ecc0-4217-86be-ff3807a41a8f', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1,
                    'ZC959809880865820672', 'd58e06cc-19a8-40b0-9b90-2ff2a20c39ab', NULL, NULL, NULL, NULL,
        '3f6fc0ec-29d6-4eb3-947f-0b5ae9094e23');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('206328e3-b8ea-4b36-a741-faa2b8b22d8e', 3720.99, 0.00, 'FST_BANK_SAVING', '60000', 1, '001053110000001', '60000.99',
                                           'TRD_BANK_SAVING_GENERAL_PRINCIPAL', '60000.1000.01',
                                           'a02c02b9-6f98-11e6-bf08-00163e002839', '', NULL, NULL, NULL,
                                                                                   '7441db43-6eef-4b42-81fa-20dfead23df0',
                                                                                   '2017-02-22', '2017-02-17 15:47:36',
                                                                                   '', NULL, 101918,
    'dbbb7166-d277-403b-bc3c-ef845193468f', '2017-02-22 00:00:00', '6d779b7f-ecc0-4217-86be-ff3807a41a8f',
    '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1, 'ZC959809880865820672', 'd58e06cc-19a8-40b0-9b90-2ff2a20c39ab',
    NULL, NULL, NULL, NULL, '3f6fc0ec-29d6-4eb3-947f-0b5ae9094e23');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('247b7a23-e12e-4008-aa46-d9fb4a86d818', 53.95, 0.00, 'FST_BANK_SAVING', '60000', 1, '001053110000001', '60000.99',
                                           'TRD_BANK_SAVING_GENERAL_INTEREST', '60000.1000.02',
                                           'a02c02b9-6f98-11e6-bf08-00163e002839', '', NULL, NULL, NULL,
                                                                                   'd75b64c2-d9d7-49cf-b58a-1a71e040fcff',
                                                                                   '2017-02-22', '2017-02-17 15:47:36',
                                                                                   '', NULL, 101918,
    'dbbb7166-d277-403b-bc3c-ef845193468f', '2017-02-22 00:00:00', '6d779b7f-ecc0-4217-86be-ff3807a41a8f',
    '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1, 'ZC959809880865820672', 'd58e06cc-19a8-40b0-9b90-2ff2a20c39ab',
    NULL, NULL, NULL, NULL, '3f6fc0ec-29d6-4eb3-947f-0b5ae9094e23');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('2f3e2686-3eb4-49d6-bd16-c8c50a974e43', 0.00, 17.72, 'FST_RECIEVABLE_ASSET', '20000', 1,
                                                'SND_RECIEVABLE_OVERDUE_LOAN_ASSET', '20000.05',
                                                'TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_INTEREST', '20000.05.02',
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  'f13879a4-112c-4c4b-8898-d1dc8f404e83', NULL, 'be455498-f7ea-4b07-b930-2208442a5cb6',
  'be455498-f7ea-4b07-b930-2208442a5cb6', '86ce3758-7d97-4c16-9462-f7ae1756a732', '2017-01-14', '2017-02-17 15:55:56',
  '', NULL, 101431, 'cfc7170e-52e6-497f-85dd-862cfcb22387', '2017-01-14 00:00:00',
                    'fe6aa6d2-9af0-4463-a0f3-189320ebe7d3', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1,
                    'ZC17359336539766784', '2f12ea99-786c-44ad-befc-0fec8706d3c0', NULL, NULL, NULL, NULL,
        '861a42ca-b360-413c-96ba-2daedd626287');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('7da46b54-6e1d-4577-be06-f655507b4f5c', 0.00, 2.28, 'FST_RECIEVABLE_ASSET', '20000', 1,
                                                'SND_RECIEVABLE_LOAN_PENALTY', '20000.03', NULL, NULL,
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  'f13879a4-112c-4c4b-8898-d1dc8f404e83', NULL, '437a7360-cb81-4fb4-883e-d7d9a7f1f91a',
  '437a7360-cb81-4fb4-883e-d7d9a7f1f91a', '3aa0480a-9f12-47c9-9d2c-e86005fdd5d5', '2017-01-14', '2017-02-17 15:55:56',
  '', NULL, 101431, 'cfc7170e-52e6-497f-85dd-862cfcb22387', '2017-01-14 00:00:00',
                    'fe6aa6d2-9af0-4463-a0f3-189320ebe7d3', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1,
                    'ZC17359336539766784', '2f12ea99-786c-44ad-befc-0fec8706d3c0', NULL, NULL, NULL, NULL,
        '861a42ca-b360-413c-96ba-2daedd626287');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('be455498-f7ea-4b07-b930-2208442a5cb6', 17.72, 0.00, 'FST_BANK_SAVING', '60000', 1, '001053110000001', '60000.99',
                                           'TRD_BANK_SAVING_GENERAL_INTEREST', '60000.1000.02',
                                           'a02c02b9-6f98-11e6-bf08-00163e002839', '', NULL, NULL, NULL,
                                                                                   '86ce3758-7d97-4c16-9462-f7ae1756a732',
                                                                                   '2017-01-14', '2017-02-17 15:55:56',
                                                                                   '', NULL, 101431,
    'cfc7170e-52e6-497f-85dd-862cfcb22387', '2017-01-14 00:00:00', 'fe6aa6d2-9af0-4463-a0f3-189320ebe7d3',
    '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1, 'ZC17359336539766784', '2f12ea99-786c-44ad-befc-0fec8706d3c0', NULL,
    NULL, NULL, NULL, '861a42ca-b360-413c-96ba-2daedd626287');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('437a7360-cb81-4fb4-883e-d7d9a7f1f91a', 2.28, 0.00, 'FST_BANK_SAVING', '60000', 1, '001053110000001', '60000.99',
                                           'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY', '60000.1000.06',
                                           'a02c02b9-6f98-11e6-bf08-00163e002839', '', NULL, NULL, NULL,
                                                                                   '3aa0480a-9f12-47c9-9d2c-e86005fdd5d5',
                                                                                   '2017-01-14', '2017-02-17 15:55:56',
                                                                                   '', NULL, 101431,
    'cfc7170e-52e6-497f-85dd-862cfcb22387', '2017-01-14 00:00:00', 'fe6aa6d2-9af0-4463-a0f3-189320ebe7d3',
    '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1, 'ZC17359336539766784', '2f12ea99-786c-44ad-befc-0fec8706d3c0', NULL,
    NULL, NULL, NULL, '861a42ca-b360-413c-96ba-2daedd626287');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('fc5778c7-d2c6-4815-8d49-6f34ddc23122', 0.00, 6.28, 'FST_RECIEVABLE_ASSET', '20000', 1, 'SND_RECIEVABLE_OVERDUE_FEE',
                                           '20000.06', 'TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE', '20000.06.02',
                                           'a02c02b9-6f98-11e6-bf08-00163e002839',
    'f13879a4-112c-4c4b-8898-d1dc8f404e83', NULL, 'eafe2ac5-1e39-4ee5-ac52-28c7a4b8f6bc',
    'eafe2ac5-1e39-4ee5-ac52-28c7a4b8f6bc', 'c2acab64-421a-42c9-8188-22680d5ae9cc', '2017-01-14', '2017-02-17 16:13:35',
    '', NULL, 101431, 'cfc7170e-52e6-497f-85dd-862cfcb22387', '2017-01-14 00:00:00',
                      'b34cd179-4fa9-4af8-ae7b-a63265f5435e', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1,
                      'ZC17359336539766784', '2f12ea99-786c-44ad-befc-0fec8706d3c0', NULL, NULL, NULL, NULL,
   '78b4d5cf-f812-4d7f-8ebd-c3d8afa54582');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('c5155a4e-2fd6-4ac8-9b73-4bd35e105047', 0.00, 8.72, 'FST_RECIEVABLE_ASSET', '20000', 1,
                                                'SND_RECIEVABLE_LOAN_PENALTY', '20000.03', NULL, NULL,
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  'f13879a4-112c-4c4b-8898-d1dc8f404e83', NULL, '57b80f43-b3ff-4e8c-94f6-66eecc8afdc0',
  '57b80f43-b3ff-4e8c-94f6-66eecc8afdc0', 'fafe6852-35b5-498f-97dc-d2e3c826209f', '2017-01-14', '2017-02-17 16:13:35',
  '', NULL, 101431, 'cfc7170e-52e6-497f-85dd-862cfcb22387', '2017-01-14 00:00:00',
                    'b34cd179-4fa9-4af8-ae7b-a63265f5435e', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1,
                    'ZC17359336539766784', '2f12ea99-786c-44ad-befc-0fec8706d3c0', NULL, NULL, NULL, NULL,
        '78b4d5cf-f812-4d7f-8ebd-c3d8afa54582');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('eafe2ac5-1e39-4ee5-ac52-28c7a4b8f6bc', 6.28, 0.00, 'FST_BANK_SAVING', '60000', 1, '001053110000001', '60000.99',
                                           'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE',
                                           '60000.1000.08', 'a02c02b9-6f98-11e6-bf08-00163e002839', '', NULL, NULL,
                                                                                                    NULL,
                                                                                                    'c2acab64-421a-42c9-8188-22680d5ae9cc',
                                                                                                    '2017-01-14',
                                                                                                    '2017-02-17 16:13:35',
                                                                                                    '', NULL, 101431,
    'cfc7170e-52e6-497f-85dd-862cfcb22387', '2017-01-14 00:00:00', 'b34cd179-4fa9-4af8-ae7b-a63265f5435e',
    '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1, 'ZC17359336539766784', '2f12ea99-786c-44ad-befc-0fec8706d3c0', NULL,
    NULL, NULL, NULL, '78b4d5cf-f812-4d7f-8ebd-c3d8afa54582');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('57b80f43-b3ff-4e8c-94f6-66eecc8afdc0', 8.72, 0.00, 'FST_BANK_SAVING', '60000', 1, '001053110000001', '60000.99',
                                           'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY', '60000.1000.06',
                                           'a02c02b9-6f98-11e6-bf08-00163e002839', '', NULL, NULL, NULL,
                                                                                   'fafe6852-35b5-498f-97dc-d2e3c826209f',
                                                                                   '2017-01-14', '2017-02-17 16:13:35',
                                                                                   '', NULL, 101431,
    'cfc7170e-52e6-497f-85dd-862cfcb22387', '2017-01-14 00:00:00', 'b34cd179-4fa9-4af8-ae7b-a63265f5435e',
    '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1, 'ZC17359336539766784', '2f12ea99-786c-44ad-befc-0fec8706d3c0', NULL,
    NULL, NULL, NULL, '78b4d5cf-f812-4d7f-8ebd-c3d8afa54582');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('1ec9326e-7d9b-4c7a-80e0-a80eb037f4dd', 0.00, 13.72, 'FST_RECIEVABLE_ASSET', '20000', 1,
                                                'SND_RECIEVABLE_OVERDUE_FEE', '20000.06',
                                                'TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE', '20000.06.02',
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  'f13879a4-112c-4c4b-8898-d1dc8f404e83', NULL, 'f8e565ef-ae3d-41eb-88a4-19b9e7e7a981',
  'f8e565ef-ae3d-41eb-88a4-19b9e7e7a981', '7122e446-20de-409e-a9b6-254cf1aff83b', '2017-01-14', '2017-02-17 16:43:55',
  '', NULL, 101431, 'cfc7170e-52e6-497f-85dd-862cfcb22387', '2017-01-14 00:00:00',
                    'a765b873-3153-4561-8ea1-3189dbc3e3e3', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1,
                    'ZC17359336539766784', '2f12ea99-786c-44ad-befc-0fec8706d3c0', NULL, NULL, NULL, NULL,
        '25d8db00-452e-4d86-87c3-a585ae1811a0');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('f8e565ef-ae3d-41eb-88a4-19b9e7e7a981', 13.72, 0.00, 'FST_BANK_SAVING', '60000', 1, '001053110000001', '60000.99',
                                           'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE',
                                           '60000.1000.08', 'a02c02b9-6f98-11e6-bf08-00163e002839', '', NULL, NULL,
                                                                                                    NULL,
                                                                                                    '7122e446-20de-409e-a9b6-254cf1aff83b',
                                                                                                    '2017-01-14',
                                                                                                    '2017-02-17 16:43:55',
                                                                                                    '', NULL, 101431,
    'cfc7170e-52e6-497f-85dd-862cfcb22387', '2017-01-14 00:00:00', 'a765b873-3153-4561-8ea1-3189dbc3e3e3',
    '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1, 'ZC17359336539766784', '2f12ea99-786c-44ad-befc-0fec8706d3c0', NULL,
    NULL, NULL, NULL, '25d8db00-452e-4d86-87c3-a585ae1811a0');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('155ed5ac-6ff1-4df9-9c9a-2ff4d3c672f4', 0.00, 515.11, 'FST_RECIEVABLE_ASSET', '20000', 1,
                                                'SND_RECIEVABLE_OVERDUE_LOAN_ASSET', '20000.05',
                                                'TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_PRINCIPLE', '20000.05.01',
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  'f13879a4-112c-4c4b-8898-d1dc8f404e83', NULL, '2cb2d1b6-ca70-4e79-9592-27af11eb6430',
  '2cb2d1b6-ca70-4e79-9592-27af11eb6430', '563a5ed0-71eb-4545-aa21-ca34cf15217d', '2017-01-15', '2017-02-17 17:05:55',
  '', NULL, 101431, 'cfc7170e-52e6-497f-85dd-862cfcb22387', '2017-01-15 00:00:00',
                    'c1da3fab-2b4d-4ed4-895f-5ce914b1110f', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1,
                    'ZC17359336548155392', '05219404-8890-49da-8b5a-0477bb4e40c1', NULL, NULL, NULL, NULL,
        '6af97075-caf6-47da-a4db-3b30f2939798');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('1398b26b-8eb9-4886-9a4d-708ba42655a3', 0.00, 102.61, 'FST_RECIEVABLE_ASSET', '20000', 1,
                                                'SND_RECIEVABLE_OVERDUE_LOAN_ASSET', '20000.05',
                                                'TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_INTEREST', '20000.05.02',
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  'f13879a4-112c-4c4b-8898-d1dc8f404e83', NULL, '67680acf-70aa-4fb1-8b29-89957562e6b3',
  '67680acf-70aa-4fb1-8b29-89957562e6b3', '5ec79c08-0c01-4d3b-ac3d-712b79bcf280', '2017-01-15', '2017-02-17 17:05:55',
  '', NULL, 101431, 'cfc7170e-52e6-497f-85dd-862cfcb22387', '2017-01-15 00:00:00',
                    'c1da3fab-2b4d-4ed4-895f-5ce914b1110f', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1,
                    'ZC17359336548155392', '05219404-8890-49da-8b5a-0477bb4e40c1', NULL, NULL, NULL, NULL,
        '6af97075-caf6-47da-a4db-3b30f2939798');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('40979452-1208-4ae8-a272-d16dd4f20f8c', 0.00, 19.00, 'FST_RECIEVABLE_ASSET', '20000', 1,
                                                'SND_RECIEVABLE_OVERDUE_FEE', '20000.06',
                                                'TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE', '20000.06.02',
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  'f13879a4-112c-4c4b-8898-d1dc8f404e83', NULL, 'f8401972-c671-4391-9cf3-bad6ecce8027',
  'f8401972-c671-4391-9cf3-bad6ecce8027', '01cf36ae-c515-4886-8d28-b593e52ebf77', '2017-01-15', '2017-02-17 17:05:55',
  '', NULL, 101431, 'cfc7170e-52e6-497f-85dd-862cfcb22387', '2017-01-15 00:00:00',
                    'c1da3fab-2b4d-4ed4-895f-5ce914b1110f', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1,
                    'ZC17359336548155392', '05219404-8890-49da-8b5a-0477bb4e40c1', NULL, NULL, NULL, NULL,
        '6af97075-caf6-47da-a4db-3b30f2939798');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES ('5817156b-d0fe-4964-90b7-901ae699f555', 0.00, 10.00, 'FST_RECIEVABLE_ASSET', '20000', 1,
                                                'SND_RECIEVABLE_LOAN_PENALTY', '20000.03', NULL, NULL,
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  'f13879a4-112c-4c4b-8898-d1dc8f404e83', NULL, '73b93b5a-50c9-467e-b089-507a8d35ad74',
  '73b93b5a-50c9-467e-b089-507a8d35ad74', '0390e816-8924-42cf-bd29-c62df68f6349', '2017-01-15', '2017-02-17 17:05:55',
  '', NULL, 101431, 'cfc7170e-52e6-497f-85dd-862cfcb22387', '2017-01-15 00:00:00',
                    'c1da3fab-2b4d-4ed4-895f-5ce914b1110f', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1,
                    'ZC17359336548155392', '05219404-8890-49da-8b5a-0477bb4e40c1', NULL, NULL, NULL, NULL,
        '6af97075-caf6-47da-a4db-3b30f2939798');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('2cb2d1b6-ca70-4e79-9592-27af11eb6430', 515.11, 0.00, 'FST_BANK_SAVING', '60000', 1, '001053110000001', '60000.99',
                                           'TRD_BANK_SAVING_GENERAL_PRINCIPAL', '60000.1000.01',
                                           'a02c02b9-6f98-11e6-bf08-00163e002839', '', NULL, NULL, NULL,
                                                                                   '563a5ed0-71eb-4545-aa21-ca34cf15217d',
                                                                                   '2017-01-15', '2017-02-17 17:05:55',
                                                                                   '', NULL, 101431,
    'cfc7170e-52e6-497f-85dd-862cfcb22387', '2017-01-15 00:00:00', 'c1da3fab-2b4d-4ed4-895f-5ce914b1110f',
    '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1, 'ZC17359336548155392', '05219404-8890-49da-8b5a-0477bb4e40c1', NULL,
    NULL, NULL, NULL, '6af97075-caf6-47da-a4db-3b30f2939798');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('67680acf-70aa-4fb1-8b29-89957562e6b3', 102.61, 0.00, 'FST_BANK_SAVING', '60000', 1, '001053110000001', '60000.99',
                                           'TRD_BANK_SAVING_GENERAL_INTEREST', '60000.1000.02',
                                           'a02c02b9-6f98-11e6-bf08-00163e002839', '', NULL, NULL, NULL,
                                                                                   '5ec79c08-0c01-4d3b-ac3d-712b79bcf280',
                                                                                   '2017-01-15', '2017-02-17 17:05:55',
                                                                                   '', NULL, 101431,
    'cfc7170e-52e6-497f-85dd-862cfcb22387', '2017-01-15 00:00:00', 'c1da3fab-2b4d-4ed4-895f-5ce914b1110f',
    '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1, 'ZC17359336548155392', '05219404-8890-49da-8b5a-0477bb4e40c1', NULL,
    NULL, NULL, NULL, '6af97075-caf6-47da-a4db-3b30f2939798');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('f8401972-c671-4391-9cf3-bad6ecce8027', 19.00, 0.00, 'FST_BANK_SAVING', '60000', 1, '001053110000001', '60000.99',
                                           'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE',
                                           '60000.1000.08', 'a02c02b9-6f98-11e6-bf08-00163e002839', '', NULL, NULL,
                                                                                                    NULL,
                                                                                                    '01cf36ae-c515-4886-8d28-b593e52ebf77',
                                                                                                    '2017-01-15',
                                                                                                    '2017-02-17 17:05:55',
                                                                                                    '', NULL, 101431,
    'cfc7170e-52e6-497f-85dd-862cfcb22387', '2017-01-15 00:00:00', 'c1da3fab-2b4d-4ed4-895f-5ce914b1110f',
    '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1, 'ZC17359336548155392', '05219404-8890-49da-8b5a-0477bb4e40c1', NULL,
    NULL, NULL, NULL, '6af97075-caf6-47da-a4db-3b30f2939798');
INSERT INTO ledger_book_shelf (ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  ('73b93b5a-50c9-467e-b089-507a8d35ad74', 10.00, 0.00, 'FST_BANK_SAVING', '60000', 1, '001053110000001', '60000.99',
                                           'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY', '60000.1000.06',
                                           'a02c02b9-6f98-11e6-bf08-00163e002839', '', NULL, NULL, NULL,
                                                                                   '0390e816-8924-42cf-bd29-c62df68f6349',
                                                                                   '2017-01-15', '2017-02-17 17:05:55',
                                                                                   '', NULL, 101431,
    'cfc7170e-52e6-497f-85dd-862cfcb22387', '2017-01-15 00:00:00', 'c1da3fab-2b4d-4ed4-895f-5ce914b1110f',
    '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', '1', 1, 'ZC17359336548155392', '05219404-8890-49da-8b5a-0477bb4e40c1', NULL,
    NULL, NULL, NULL, '6af97075-caf6-47da-a4db-3b30f2939798');