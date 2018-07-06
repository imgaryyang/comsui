DELETE FROM `journal_voucher`;
DELETE FROM `ledger_book_shelf`;
DELETE FROM `financial_contract`;
DELETE FROM `app`;
DELETE FROM `company`;
DELETE FROM `account`;
DELETE FROM `payment_channel`;
DELETE FROM `quartz_job`;
DELETE FROM `daily_actual_repayment`;

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

INSERT INTO `journal_voucher` (`account_side`, `bank_identity`, `billing_plan_uuid`, `booking_amount`, `business_voucher_type_uuid`, `business_voucher_uuid`, `cash_flow_amount`, `cash_flow_breif`, `cash_flow_channel_type`, `cash_flow_serial_no`, `cash_flow_uuid`, `checking_level`, `company_id`, `completeness`, `counter_party_account`, `counter_party_name`, `journal_voucher_uuid`, `notification_identity`, `notification_memo`, `notification_record_uuid`, `notified_date`, `settlement_modes`, `source_document_amount`, `source_document_breif`, `source_document_cash_flow_serial_no`, `source_document_counter_party_uuid`, `source_document_identity`, `source_document_uuid`, `status`, `trade_time`, `batch_uuid`, `created_date`, `source_document_counter_party_account`, `source_document_counter_party_name`, `issued_time`, `journal_voucher_type`, `counter_account_type`, `related_bill_contract_info_lv_1`, `related_bill_contract_info_lv_2`, `related_bill_contract_info_lv_3`, `cash_flow_account_info`, `journal_voucher_no`, `related_bill_contract_no_lv_1`, `related_bill_contract_no_lv_2`, `related_bill_contract_no_lv_3`, `related_bill_contract_no_lv_4`, `source_document_no`, `appendix`, `last_modified_time`, `local_party_account`, `local_party_name`, `source_document_local_party_account`, `source_document_local_party_name`, `second_journal_voucher_type`, `third_journal_voucher_type`, `is_has_data_sync_log`)
VALUES ('1', '中国建设银行 ', '17ca5c3a-6732-4d76-8107-0742613bd333', '2010.00', '2b784ef2-a1ab-41fa-b315-8e1bf8cfa2ba', '',
             '2010.00', NULL, '2', NULL, NULL, '0', '1', '3', '6217001210075327590', '韩方园',
                                               '7851ef08-3f2a-4d97-9ecc-f41da6922c82', NULL, NULL, NULL, NULL, NULL,
                                                                                                               '2010.00',
                                                                                                               NULL,
                                                                                                               NULL,
                                                                                                               NULL,
                                                                                                               '13136af6-46e0-44bb-a9e7-e41e30cd7ccf',
                                                                                                               'f8f98c5d-cc1c-4687-b77c-c8352adce0e4',
                                                                                                               '1',
                                                                                                               '2017-05-16 15:22:50',
                                                                                                               NULL,
  '2017-05-16 15:22:54', '6217001210075327590', '韩方园', '2017-05-16 15:22:56', '5', '0',
  'd2812bc5-5057-4a91-b3fd-9019506f0499', 'dd51e53d-8236-4bb9-9c8a-09278c7d0f0c',
  '17ca5c3a-6732-4d76-8107-0742613bd333', '', '0751cdeb-4fb0-4480-9180-ccf90ae2df4b', '拍拍贷测试',
                                              '2016-236-DK(HT1494919061078)号', 'ZC1980086123512913920',
                                              'JS1980102146257518592', 'KK1980101926677315584', NULL,
                                              '2017-05-16 15:22:56', '600000000001', '云南信托国际有限公司', '600000000001',
        '云南信托国际有限公司', '1', '0', '1');
INSERT INTO `journal_voucher` (`account_side`, `bank_identity`, `billing_plan_uuid`, `booking_amount`, `business_voucher_type_uuid`, `business_voucher_uuid`, `cash_flow_amount`, `cash_flow_breif`, `cash_flow_channel_type`, `cash_flow_serial_no`, `cash_flow_uuid`, `checking_level`, `company_id`, `completeness`, `counter_party_account`, `counter_party_name`, `journal_voucher_uuid`, `notification_identity`, `notification_memo`, `notification_record_uuid`, `notified_date`, `settlement_modes`, `source_document_amount`, `source_document_breif`, `source_document_cash_flow_serial_no`, `source_document_counter_party_uuid`, `source_document_identity`, `source_document_uuid`, `status`, `trade_time`, `batch_uuid`, `created_date`, `source_document_counter_party_account`, `source_document_counter_party_name`, `issued_time`, `journal_voucher_type`, `counter_account_type`, `related_bill_contract_info_lv_1`, `related_bill_contract_info_lv_2`, `related_bill_contract_info_lv_3`, `cash_flow_account_info`, `journal_voucher_no`, `related_bill_contract_no_lv_1`, `related_bill_contract_no_lv_2`, `related_bill_contract_no_lv_3`, `related_bill_contract_no_lv_4`, `source_document_no`, `appendix`, `last_modified_time`, `local_party_account`, `local_party_name`, `source_document_local_party_account`, `source_document_local_party_name`, `second_journal_voucher_type`, `third_journal_voucher_type`, `is_has_data_sync_log`)
VALUES
  ('1', '中国建设银行 ', 'ad9ef660-d79b-4956-9612-93025eeb2703', '60.00', '2b784ef2-a1ab-41fa-b315-8e1bf8cfa2ba', '', '60.00',
        NULL, '2', NULL, NULL, '0', '1', '3', '6214855712106520', '悟空', '6892dce4-be0f-4453-81ce-1206715cb2b6', NULL,
                               NULL, NULL, NULL, NULL, '60.00', NULL, NULL, NULL,
                                                 '2cced052-a6d5-4e1b-87ef-7db1a31b1dc6',
                                                 '24083fae-4524-45c3-942c-36dfdadc2e31', '1', '2017-05-16 22:23:30',
                                                 NULL, '2017-05-16 22:23:32', '6214855712106520', '悟空',
                                                       '2017-05-16 22:23:35', '5', '0',
                                                       'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                       'c52186ac-c40b-4134-9c38-1a0ccda06393',
                                                       'ad9ef660-d79b-4956-9612-93025eeb2703', '',
    '4ad310f1-b092-4efe-9d3d-1a9fdf8c349e', '拍拍贷测试', 'CHENHAONAN3999', 'ZC1970714415349809152', 'JS1983489742396358656',
    'KK1983489233308516352', NULL, '2017-05-16 22:23:35', '600000000001', '云南信托国际有限公司', '600000000001', '云南信托国际有限公司',
   '1', '1', '1');
INSERT INTO `journal_voucher` (`account_side`, `bank_identity`, `billing_plan_uuid`, `booking_amount`, `business_voucher_type_uuid`, `business_voucher_uuid`, `cash_flow_amount`, `cash_flow_breif`, `cash_flow_channel_type`, `cash_flow_serial_no`, `cash_flow_uuid`, `checking_level`, `company_id`, `completeness`, `counter_party_account`, `counter_party_name`, `journal_voucher_uuid`, `notification_identity`, `notification_memo`, `notification_record_uuid`, `notified_date`, `settlement_modes`, `source_document_amount`, `source_document_breif`, `source_document_cash_flow_serial_no`, `source_document_counter_party_uuid`, `source_document_identity`, `source_document_uuid`, `status`, `trade_time`, `batch_uuid`, `created_date`, `source_document_counter_party_account`, `source_document_counter_party_name`, `issued_time`, `journal_voucher_type`, `counter_account_type`, `related_bill_contract_info_lv_1`, `related_bill_contract_info_lv_2`, `related_bill_contract_info_lv_3`, `cash_flow_account_info`, `journal_voucher_no`, `related_bill_contract_no_lv_1`, `related_bill_contract_no_lv_2`, `related_bill_contract_no_lv_3`, `related_bill_contract_no_lv_4`, `source_document_no`, `appendix`, `last_modified_time`, `local_party_account`, `local_party_name`, `source_document_local_party_account`, `source_document_local_party_name`, `second_journal_voucher_type`, `third_journal_voucher_type`, `is_has_data_sync_log`)
VALUES ('1', '中国农业银行 ', 'f0cf71fe-9203-41f0-b4ac-df8f9a6efe95', '2820.00', '2b784ef2-a1ab-41fa-b315-8e1bf8cfa2ba', '',
             '2820.00', NULL, '4', NULL, NULL, '0', '1', '3', '6228480444455553333', '王宝',
                                               '1f3d4429-0a4a-4e0b-bad1-bcd76d5769d3', NULL, NULL, NULL, NULL, NULL,
                                                                                                               '2820.00',
                                                                                                               NULL,
                                                                                                               '201705160110000402576026',
                                                                                                               NULL,
                                                                                                               '98393ebc-11e2-48a1-b82e-e23aa900801d',
                                                                                                               '2d89d8f1-1dac-4a61-80e1-50067b01b942',
                                                                                                               '1',
                                                                                                               '2017-05-16 22:32:30',
                                                                                                               NULL,
  '2017-05-16 22:32:33', '6228480444455553333', '王宝', '2017-05-16 22:32:42', '9', '0',
  'd2812bc5-5057-4a91-b3fd-9019506f0499', '4cdfdae6-3031-4f46-9cf9-10a3627fb642',
  'f0cf71fe-9203-41f0-b4ac-df8f9a6efe95', '', '45a25685-7a09-4036-b003-83bdcc7b630c', '拍拍贷测试', 'WANGHAIBO4',
                                              'ZC1983551608850157568', 'JS1983563154259107840', 'KK1983561956902756352',
                                              NULL, '2017-05-16 22:32:42', '600000000001', '云南信托国际有限公司', '600000000001',
        '云南信托国际有限公司', '1', '1', '1');


INSERT INTO `ledger_book_shelf` (`ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES ('4adabb19-dc82-4b1d-a447-d0659049d612', '1000.00', '1.00', 'FST_LONGTERM_LIABILITY', '40000', '0',
                                                'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01',
                                                'TRD_BANK_SAVING_GENERAL_PRINCIPAL', NULL,
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  'c262636a-8905-4115-a3d5-06f042a0da3e', NULL, NULL, NULL, 'e99c3dbc-8efb-4af8-9c2b-3e956483a57b', '2017-09-20',
  '2017-09-20 14:55:23', '', NULL, '12384259', '124b2149-d164-4dfd-bda8-1b46c4e89e35', '2017-09-20 00:00:00',
                                               '1f3d4429-0a4a-4e0b-bad1-bcd76d5769d3',
                                               '74a9ce4d-cafc-407d-b013-987077541bdc', '1', '1', 'ZC107894517445812224',
                                               'da240ed2-1692-47d8-a843-0a0c540b4313', 'fgz123wqeqw', NULL, NULL, NULL,
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
                                               'da240ed2-1692-47d8-a843-0a0c540b4313', 'fgz123wqeqw', NULL, NULL, NULL,
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
                                               '4a980298-11e8-42cd-b0ea-ab603d25d8c8', 'xwq122', NULL, NULL, NULL, '');
INSERT INTO `ledger_book_shelf` (`ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES ('d3c7c0d1-c451-40b1-9953-3af86cd9fc16', '994.00', '0.00', 'FST_UNEARNED_LOAN_ASSET', '10000', '1',
                                                'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02',
                                                'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_TECH_FEE', NULL,
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  '477f23f3-4c2a-4022-8f09-4121517a2504', NULL, NULL, NULL, '4bb4d96a-2959-42a2-a18c-ad5bfbd60481', '2017-05-20',
  '2017-08-11 14:25:02', '', NULL, '12384257', '2fcd2ec2-a58e-43e9-83c1-6562c3f9569d', '2017-05-20 00:00:00',
                                               '6892dce4-be0f-4453-81ce-1206715cb2b6',
                                               '74a9ce4d-cafc-407d-b013-987077541bdc', '1', '1', 'ZC93391366567026688',
                                               'c51526d0-e7a1-4505-bc90-b5ca15a5471a', NULL, NULL, NULL, NULL, '');
INSERT INTO `ledger_book_shelf` (`ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES ('1249836d-53ea-46ea-851f-f368c6726708', '112.00', '100.00', 'FST_LONGTERM_LIABILITY', '40000', '0',
                                                'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01',
                                                'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OTHER_FEE', NULL,
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  '2dfdb833-d1a8-4ff1-ac51-a081e0e9ffc4', NULL, NULL, NULL, 'fdd98e61-957f-4304-a42f-5dec3f87b693', '2017-09-20',
  '2017-09-20 14:52:57', '', NULL, '12384258', 'b07b0a38-74d6-4871-acf2-5a7f1ae89ab8', '2017-09-20 00:00:00',
                                               '6892dce4-be0f-4453-81ce-1206715cb2b6',
                                               '74a9ce4d-cafc-407d-b013-987077541bdc', '1', '1', 'ZC107893904188235776',
                                               '94aab249-6243-4299-bf49-645ef6148275', 'fgz123wqeqw', NULL, NULL, NULL,
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
                                               '94aab249-6243-4299-bf49-645ef6148275', 'fgz123wqeqw', NULL, NULL, NULL,
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
                                               '7e01114f-dacf-4a1c-8beb-761ab850a5d2', NULL, NULL, NULL, NULL, '');
INSERT INTO `ledger_book_shelf` (`ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES ('65386f6c-039b-4ce5-a9c4-26a3f36ab27a', '464.00', '0.00', 'FST_UNEARNED_LOAN_ASSET', '10000', '1',
                                                'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02',
                                                'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY', NULL,
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  'b074faf2-a3e8-41c8-adc4-17f66b4bea36', NULL, NULL, NULL, '4beee4eb-f93a-422c-b57a-1f6bd6b504b4', '2017-05-20',
  '2017-08-11 14:17:38', '', NULL, '12384256', 'a340ca30-af49-42aa-86a7-c207527d9542', '2017-05-20 00:00:00',
                                               '7851ef08-3f2a-4d97-9ecc-f41da6922c82',
                                               '74a9ce4d-cafc-407d-b013-987077541bdc', '1', '1', 'ZC93389501829799936',
                                               '7e01114f-dacf-4a1c-8beb-761ab850a5d2', NULL, NULL, NULL, NULL, '');
INSERT INTO `ledger_book_shelf` (`ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`)
VALUES ('19b63c16-ed9c-4c98-a5ad-f7a40000574f', '855.00', '234.00', 'FST_LONGTERM_LIABILITY', '40000', '0',
                                                'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01',
                                                'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION', NULL,
                                                'a02c02b9-6f98-11e6-bf08-00163e002839',
  '477f23f3-4c2a-4022-8f09-4121517a2504', NULL, NULL, NULL, '4bb4d96a-2959-42a2-a18c-ad5bfbd60481', '2017-05-20',
  '2017-08-11 14:25:02', '', NULL, '12384257', '2fcd2ec2-a58e-43e9-83c1-6562c3f9569d', '2017-05-20 00:00:00',
                                               '7851ef08-3f2a-4d97-9ecc-f41da6922c82',
                                               '74a9ce4d-cafc-407d-b013-987077541bdc', '1', '1', 'ZC93391366567026688',
                                               'c51526d0-e7a1-4505-bc90-b5ca15a5471a', NULL, NULL, NULL, NULL, '');
