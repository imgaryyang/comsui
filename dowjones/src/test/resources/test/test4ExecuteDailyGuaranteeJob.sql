DELETE FROM `journal_voucher`;
DELETE FROM `financial_contract`;
DELETE FROM `app`;
DELETE FROM `company`;
DELETE FROM `account`;
DELETE FROM `payment_channel`;
DELETE FROM `quartz_job`;
DELETE FROM `daily_guarantee`;

INSERT INTO `financial_contract` (`asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`,
                                  `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`,
                                  `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`,
                                  `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`,
                                  `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`,
                                  `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`,
                                  `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`,
                                  `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`,
                                  `repurchase_algorithm`, `day_of_month`, `pay_for_go`, `repurchase_principal_algorithm`,
                                  `repurchase_interest_algorithm`, `repurchase_penalty_algorithm`, `repurchase_other_charges_algorithm`,
                                  `repayment_check_days`, `repurchase_cycle`, `days_of_cycle`, `temporary_repurchases`,
                                  `allow_freewheeling_repayment`, `capital_party`, `other_party`, `contract_short_name`, `remittance_object`,
                                  `financial_type`)
VALUES ('1', '0', '2016-09-01 00:00:00', 'G31700', '拍拍贷测试', '3', '5', '12', '2017-08-31 00:00:00', '5', '0',
  '1', '11', '1', '7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58', 'd2812bc5-5057-4a91-b3fd-9019506f0499',
  '0', '0', '1', '1', '1', '0', NULL, NULL, '3', 'null', '1', '1', '1', '0', '', NULL, NULL, NULL, NULL,
                                                                                 '2017-10-11 15:21:13', '1', '1',
                                                                                 'outstandingPrincipal+outstandingInterest+outstandingPenaltyInterest',
                                                                                 '6', '0', 'outstandingPrincipal',
                                                                                           'outstandingInterest',
                                                                                           'outstandingPenaltyInterest',
                                                                                           '', '7', NULL, '[]', '[]',
                                                                                           '1', NULL, NULL, NULL, NULL,
        '0');

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
VALUES
  ('1', NULL, '6b1ff8e2-ff1e-4906-87c3-ba3c9d280de8', '1000.00', '', '', NULL, NULL, NULL, NULL, NULL, '0', '3', NULL,
                                                                                                       'VACC274A0A4546979A41',
                                                                                                       'ppd',
                                                                                                       'f36dda02-fa5a-4c65-b128-42565199517c',
                                                                                                       NULL, NULL, NULL,
                                                                                                       NULL, NULL,
                                                                                                             '1000.00',
                                                                                                             NULL, NULL,
                                                                                                             NULL,
                                                                                                             'fe9688cc-1e89-4573-9b80-34d07feefa29',
                                                                                                             'a8a182dc-958a-47a2-95db-c8d6260e3f11',
                                                                                                             '1',
                                                                                                             '2017-12-25 19:35:48',
                                                                                                             NULL,
    '2017-12-25 19:40:39', '1001133419006708190', '上海拍拍贷金融信息服务有限公司', '2017-12-25 19:40:39', '12', '1',
    'd2812bc5-5057-4a91-b3fd-9019506f0499', '0ff77f51-e227-433f-bb90-e9f9d0651d61',
    '6b1ff8e2-ff1e-4906-87c3-ba3c9d280de8', 'a02c0830-6f98-11e6-bf08-00163e002839', 'ZF142755543113318400', '拍拍贷测试',
                                                                                    '1514201704435',
                                                                                    '6b1ff8e2-ff1e-4906-87c3-ba3c9d280de8',
                                                                                    '6b1ff8e2-ff1e-4906-87c3-ba3c9d280de8',
                                                                                    'V142754341584551936', NULL,
                                                                                    '2017-12-25 19:40:42',
                                                                                    '6214855712106520', '王宝',
   '600000000001', '云南信托国际有限公司', NULL, NULL, '1');
INSERT INTO `journal_voucher` (`account_side`, `bank_identity`, `billing_plan_uuid`, `booking_amount`, `business_voucher_type_uuid`, `business_voucher_uuid`, `cash_flow_amount`, `cash_flow_breif`, `cash_flow_channel_type`, `cash_flow_serial_no`, `cash_flow_uuid`, `checking_level`, `company_id`, `completeness`, `counter_party_account`, `counter_party_name`, `journal_voucher_uuid`, `notification_identity`, `notification_memo`, `notification_record_uuid`, `notified_date`, `settlement_modes`, `source_document_amount`, `source_document_breif`, `source_document_cash_flow_serial_no`, `source_document_counter_party_uuid`, `source_document_identity`, `source_document_uuid`, `status`, `trade_time`, `batch_uuid`, `created_date`, `source_document_counter_party_account`, `source_document_counter_party_name`, `issued_time`, `journal_voucher_type`, `counter_account_type`, `related_bill_contract_info_lv_1`, `related_bill_contract_info_lv_2`, `related_bill_contract_info_lv_3`, `cash_flow_account_info`, `journal_voucher_no`, `related_bill_contract_no_lv_1`, `related_bill_contract_no_lv_2`, `related_bill_contract_no_lv_3`, `related_bill_contract_no_lv_4`, `source_document_no`, `appendix`, `last_modified_time`, `local_party_account`, `local_party_name`, `source_document_local_party_account`, `source_document_local_party_name`, `second_journal_voucher_type`, `third_journal_voucher_type`, `is_has_data_sync_log`)
VALUES
  ('1', NULL, 'ddb110b8-cb70-4a78-a776-cab3d114f70d', '1000.00', '', '', NULL, NULL, NULL, NULL, NULL, '0', '3', NULL,
                                                                                                       'VACC274A0A4546979A41',
                                                                                                       'ppd',
                                                                                                       '09f2a744-0a9e-4137-b871-574a518c7b2a',
                                                                                                       NULL, NULL, NULL,
                                                                                                       NULL, NULL,
                                                                                                             '1000.00',
                                                                                                             NULL, NULL,
                                                                                                             NULL,
                                                                                                             '21623481-9bcc-49a1-a06f-99380a93ac11',
                                                                                                             '730c3867-edc1-4313-8186-de817abf60a1',
                                                                                                             '1',
                                                                                                             '2017-12-25 18:13:16',
                                                                                                             NULL,
    '2017-12-25 19:52:58', '1001133419006708190', '上海拍拍贷金融信息服务有限公司', '2017-12-25 19:52:58', '12', '1',
    'd2812bc5-5057-4a91-b3fd-9019506f0499', '26029dec-2412-4fb9-84e5-51d0d4d69db2',
    'ddb110b8-cb70-4a78-a776-cab3d114f70d', 'a02c0830-6f98-11e6-bf08-00163e002839', 'ZF142758639344336896', '拍拍贷测试',
                                                                                    '1514196778105',
                                                                                    'ddb110b8-cb70-4a78-a776-cab3d114f70d',
                                                                                    'ddb110b8-cb70-4a78-a776-cab3d114f70d',
                                                                                    'V142733575493173248', NULL,
                                                                                    '2017-12-25 19:53:02',
                                                                                    '6214855712106520', '王宝',
   '600000000001', '云南信托国际有限公司', NULL, NULL, '1');


