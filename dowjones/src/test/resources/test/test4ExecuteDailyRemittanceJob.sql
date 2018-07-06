DELETE FROM `journal_voucher`;
DELETE FROM `financial_contract`;
DELETE FROM `app`;
DELETE FROM `company`;
DELETE FROM `account`;
DELETE FROM `payment_channel`;
DELETE FROM `t_remittance_application`;
DELETE FROM `t_remittance_plan`;
DELETE FROM `quartz_job`;
DELETE FROM `t_remittance_plan_exec_log`;
DELETE FROM `contract`;
DELETE FROM `asset_set`;
DELETE FROM `asset_set_extra_charge`;
DELETE FROM `daily_remittance`;
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


INSERT INTO t_remittance_application (remittance_application_uuid, request_no, financial_contract_uuid, financial_contract_id, financial_product_code, contract_unique_id, contract_no, planned_total_amount, actual_total_amount, auditor_name, audit_time, notify_url, plan_notify_number, actual_notify_number, remittance_strategy, remark, transaction_recipient, execution_status, execution_remark, create_time, creator_name, ip, last_modified_time, opposite_receive_date, remittance_id, total_count, actual_count, version_lock, check_request_no, check_status, check_retry_number, check_send_time, notify_status, int_field_1, int_field_2, int_field_3, string_field_1, string_field_2, string_field_3, decimal_field_1, decimal_field_2, decimal_field_3)
VALUES ('84ff315b-8bb0-4d36-8a3b-34f69fb38f0a', '1239e0f6-3cfd-4065-84b6-46471991ad55',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 'G31700', 'overFT501', NULL,
                                                300000.00, 300000.00, NULL, NULL,
  'http://101.52.128.166/Loan/BatchPaidNotic', 3, 3, 0, '交易备注', 1, 2, NULL, '2017-12-25 10:43:05', 't_test_zfb',
  '115.197.185.153, 120.26.102.180', '2017-12-25 10:43:42', '2017-12-25 10:43:32', NULL, 1, 1,
  'ca652d96-d804-45b3-9d91-c641224c5c3b', '9998162a-3f74-40fe-9ab6-798204ba1a56', 1, 3, '2017-12-25 10:43:05', 1, NULL,
                                                                                        NULL, NULL, NULL, NULL, NULL,
                                                                                        NULL, NULL, NULL);
INSERT INTO t_remittance_application (remittance_application_uuid, request_no, financial_contract_uuid, financial_contract_id, financial_product_code, contract_unique_id, contract_no, planned_total_amount, actual_total_amount, auditor_name, audit_time, notify_url, plan_notify_number, actual_notify_number, remittance_strategy, remark, transaction_recipient, execution_status, execution_remark, create_time, creator_name, ip, last_modified_time, opposite_receive_date, remittance_id, total_count, actual_count, version_lock, check_request_no, check_status, check_retry_number, check_send_time, notify_status, int_field_1, int_field_2, int_field_3, string_field_1, string_field_2, string_field_3, decimal_field_1, decimal_field_2, decimal_field_3)
VALUES ('7ae7c538-c49e-4bab-bc73-61a2364dea17', '4fc14c41-6b2e-4431-9366-9849882313b7',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 'G31700', 'overFT502', NULL,
                                                300000.00, 300000.00, NULL, NULL,
  'http://101.52.128.166/Loan/BatchPaidNotic', 3, 3, 0, '交易备注', 1, 2, NULL, '2017-12-25 10:58:12', 't_test_zfb',
  '115.197.185.153, 120.26.102.180', '2017-12-25 10:58:42', '2017-12-25 10:58:32', NULL, 1, 1,
  '7c621753-041f-4cda-86a6-5f0f18a78ab7', 'dbdeec61-a8f8-4688-950c-ac5d0b6efa04', 1, 3, '2017-12-25 10:58:12', 1, NULL,
                                                                                        NULL, NULL, NULL, NULL, NULL,
                                                                                        NULL, NULL, NULL);
INSERT INTO t_remittance_application (remittance_application_uuid, request_no, financial_contract_uuid, financial_contract_id, financial_product_code, contract_unique_id, contract_no, planned_total_amount, actual_total_amount, auditor_name, audit_time, notify_url, plan_notify_number, actual_notify_number, remittance_strategy, remark, transaction_recipient, execution_status, execution_remark, create_time, creator_name, ip, last_modified_time, opposite_receive_date, remittance_id, total_count, actual_count, version_lock, check_request_no, check_status, check_retry_number, check_send_time, notify_status, int_field_1, int_field_2, int_field_3, string_field_1, string_field_2, string_field_3, decimal_field_1, decimal_field_2, decimal_field_3)
VALUES ('a9fd9572-4cd8-4184-a7be-c86c6a05391d', '6da07f7c-206c-4ecc-9976-bc43d94deca3',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 'G31700', 'overFT503', NULL,
                                                300000.00, 300000.00, NULL, NULL,
  'http://101.52.128.166/Loan/BatchPaidNotic', 3, 3, 0, '交易备注', 1, 2, NULL, '2017-12-25 11:00:12', 't_test_zfb',
  '115.197.185.153, 120.26.102.180', '2017-12-25 11:00:43', '2017-12-25 11:00:32', NULL, 1, 1,
  '1c28fa96-ee5c-4756-96fb-fea61e84c426', '6d6f7bae-691e-4f9c-a975-fe5a7549a7ce', 1, 3, '2017-12-25 11:00:12', 1, NULL,
                                                                                        NULL, NULL, NULL, NULL, NULL,
                                                                                        NULL, NULL, NULL);
INSERT INTO t_remittance_application (remittance_application_uuid, request_no, financial_contract_uuid, financial_contract_id, financial_product_code, contract_unique_id, contract_no, planned_total_amount, actual_total_amount, auditor_name, audit_time, notify_url, plan_notify_number, actual_notify_number, remittance_strategy, remark, transaction_recipient, execution_status, execution_remark, create_time, creator_name, ip, last_modified_time, opposite_receive_date, remittance_id, total_count, actual_count, version_lock, check_request_no, check_status, check_retry_number, check_send_time, notify_status, int_field_1, int_field_2, int_field_3, string_field_1, string_field_2, string_field_3, decimal_field_1, decimal_field_2, decimal_field_3)
VALUES
  ('d593188f-d40e-46d7-b43d-58435dc72efe', '105f4aa07fac4e7f9d795ee286d1b41d', 'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                           38, 'G31700', 'ab025451e63042a48fa834e9102e689e',
                                           '合同模板(hk15141834394613323)号', 10000.00, 10000.00, '', NULL,
    'http://101.52.128.166/Loan/BatchPaidNotic', 3, 3, 0, 'mudi', 1, 2, NULL, '2017-12-25 15:19:52', 't_merchant',
    '101.52.128.162, 120.26.102.180', '2017-12-25 15:20:11', '2017-12-25 15:20:02', NULL, 1, 1,
    'd4a0c89b-905b-4ac6-813c-a32ecb76f606', '5df9b33d-8e48-4798-be75-26ae4a26ca16', 1, 3, '2017-12-25 15:19:52', 1,
                                                                                          NULL, NULL, NULL, NULL, NULL,
                                                                                          NULL, NULL, NULL, NULL);
INSERT INTO t_remittance_application (remittance_application_uuid, request_no, financial_contract_uuid, financial_contract_id, financial_product_code, contract_unique_id, contract_no, planned_total_amount, actual_total_amount, auditor_name, audit_time, notify_url, plan_notify_number, actual_notify_number, remittance_strategy, remark, transaction_recipient, execution_status, execution_remark, create_time, creator_name, ip, last_modified_time, opposite_receive_date, remittance_id, total_count, actual_count, version_lock, check_request_no, check_status, check_retry_number, check_send_time, notify_status, int_field_1, int_field_2, int_field_3, string_field_1, string_field_2, string_field_3, decimal_field_1, decimal_field_2, decimal_field_3)
VALUES ('dc5bd9dc-9ba6-447c-892c-9088ace34e00', 'Remittance179', 'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 'G31700',
                                                'Remittance179', 'Remittance179', 1500.00, 1500.00, 'auditorName1',
                                                '2017-12-25 00:00:00', 'www.bai.com', 3, 0, 0, '交易备注', 1, 2, NULL,
                                                                       '2017-12-25 17:47:19', 't_test_zfb',
  '115.197.185.153, 120.26.102.180', '2017-12-25 17:47:42', '2017-12-25 17:47:32',
  '5f8b168f-18be-4f56-81a6-867514adb31f', 1, 1, '53b1986b-5e48-4bc9-9278-4e898af221ba',
  '5b67fa17-3e3b-43ba-b60b-f8be9fd04180', 1, 3, '2017-12-25 17:47:19', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
                                                NULL, NULL);
INSERT INTO t_remittance_application (remittance_application_uuid, request_no, financial_contract_uuid, financial_contract_id, financial_product_code, contract_unique_id, contract_no, planned_total_amount, actual_total_amount, auditor_name, audit_time, notify_url, plan_notify_number, actual_notify_number, remittance_strategy, remark, transaction_recipient, execution_status, execution_remark, create_time, creator_name, ip, last_modified_time, opposite_receive_date, remittance_id, total_count, actual_count, version_lock, check_request_no, check_status, check_retry_number, check_send_time, notify_status, int_field_1, int_field_2, int_field_3, string_field_1, string_field_2, string_field_3, decimal_field_1, decimal_field_2, decimal_field_3)
VALUES ('e41de349-b719-44e1-8b88-c97258246b15', '795bab70-7303-45b4-8825-c678c62bcf31DZZ',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 'G31700', '1514196210772',
                                                '1514196210772', 1000.00, 1000.00, 'auditorName1',
                                                '2016-08-20 00:00:00', 'www.baidu.com', 3, 0, 0, 'recordSn', 1, 2, NULL,
                                                                       '2017-12-25 18:03:29', 't_test_zfb',
  '115.197.185.153, 120.26.102.180', '2017-12-25 18:04:14', '2017-12-25 18:04:02',
  'a783f746-f3dd-4901-b0cb-78f251dbffcf', 1, 1, '5c5a3568-231a-449d-9f75-72712eb2e69b',
  '511532a3-3459-4f0b-b48f-b4152f2e84b0', 1, 3, '2017-12-25 18:03:29', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
                                                NULL, NULL);
INSERT INTO t_remittance_application (remittance_application_uuid, request_no, financial_contract_uuid, financial_contract_id, financial_product_code, contract_unique_id, contract_no, planned_total_amount, actual_total_amount, auditor_name, audit_time, notify_url, plan_notify_number, actual_notify_number, remittance_strategy, remark, transaction_recipient, execution_status, execution_remark, create_time, creator_name, ip, last_modified_time, opposite_receive_date, remittance_id, total_count, actual_count, version_lock, check_request_no, check_status, check_retry_number, check_send_time, notify_status, int_field_1, int_field_2, int_field_3, string_field_1, string_field_2, string_field_3, decimal_field_1, decimal_field_2, decimal_field_3)
VALUES ('50c0f08a-5f32-46e7-b7ac-720a7820ae99', 'd23c0667-e6dc-41f4-8a0a-cf34100fbd7eDZZ',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 'G31700', '1514196778105',
                                                '1514196778105', 1000.00, 1000.00, 'auditorName1',
                                                '2016-08-20 00:00:00', 'www.baidu.com', 3, 0, 0, 'recordSn', 1, 2, NULL,
                                                                       '2017-12-25 18:12:57', 't_test_zfb',
  '115.197.185.153, 120.26.102.180', '2017-12-25 18:13:11', '2017-12-25 18:13:03',
  '2e3305ee-d7a1-493a-94d0-4c0fd18a0451', 1, 1, 'd0ac2fa4-a68f-4f51-9c19-2f1c9ef19bd9',
  'e9e7d662-d563-4ae2-bb0d-b548ce1a17a9', 1, 3, '2017-12-25 18:12:57', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
                                                NULL, NULL);
INSERT INTO t_remittance_application (remittance_application_uuid, request_no, financial_contract_uuid, financial_contract_id, financial_product_code, contract_unique_id, contract_no, planned_total_amount, actual_total_amount, auditor_name, audit_time, notify_url, plan_notify_number, actual_notify_number, remittance_strategy, remark, transaction_recipient, execution_status, execution_remark, create_time, creator_name, ip, last_modified_time, opposite_receive_date, remittance_id, total_count, actual_count, version_lock, check_request_no, check_status, check_retry_number, check_send_time, notify_status, int_field_1, int_field_2, int_field_3, string_field_1, string_field_2, string_field_3, decimal_field_1, decimal_field_2, decimal_field_3)
VALUES ('bf90e83b-d903-463e-830e-5717fab2adaf', '4034a648-fd84-4b4c-9cb9-948deaa8a702DZZ',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 'G31700', '1514201704435',
                                                '1514201704435', 1000.00, 1000.00, 'auditorName1',
                                                '2016-08-20 00:00:00', 'www.baidu.com', 3, 0, 0, 'recordSn', 1, 2, NULL,
                                                                       '2017-12-25 19:35:03', 't_test_zfb',
  '115.197.185.153, 120.26.102.180', '2017-12-25 19:35:42', '2017-12-25 19:35:32',
  '54342c00-a9b4-47b4-94df-c8d90a619c50', 1, 1, 'abe33387-d718-4d14-a16f-1f8836ed85d4',
  '0f49eef2-4c45-4320-9130-f02e981e8978', 1, 3, '2017-12-25 19:35:03', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
                                                NULL, NULL);
INSERT INTO t_remittance_application (remittance_application_uuid, request_no, financial_contract_uuid, financial_contract_id, financial_product_code, contract_unique_id, contract_no, planned_total_amount, actual_total_amount, auditor_name, audit_time, notify_url, plan_notify_number, actual_notify_number, remittance_strategy, remark, transaction_recipient, execution_status, execution_remark, create_time, creator_name, ip, last_modified_time, opposite_receive_date, remittance_id, total_count, actual_count, version_lock, check_request_no, check_status, check_retry_number, check_send_time, notify_status, int_field_1, int_field_2, int_field_3, string_field_1, string_field_2, string_field_3, decimal_field_1, decimal_field_2, decimal_field_3)
VALUES ('77aa983e-96e8-4aea-957a-d81c1aed4695', '3288001d-6e1a-46ef-b123-a655e65cee72DZZ',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 'G31700', '1514202051446',
                                                '1514202051446', 1000.00, 1000.00, 'auditorName1',
                                                '2016-08-20 00:00:00', 'www.baidu.com', 3, 0, 0, 'recordSn', 1, 2, NULL,
                                                                       '2017-12-25 19:40:49', 't_test_zfb',
  '115.197.185.153, 120.26.102.180', '2017-12-25 19:41:12', '2017-12-25 19:41:02',
  '383d8262-c906-44ea-96fe-e05faba68a53', 1, 1, 'c5cd4662-a0e9-425c-906d-72bd08255f97',
  'ddc76dd1-4de0-4c4c-9e48-4c16b6700e54', 1, 3, '2017-12-25 19:40:49', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
                                                NULL, NULL);
INSERT INTO t_remittance_application (remittance_application_uuid, request_no, financial_contract_uuid, financial_contract_id, financial_product_code, contract_unique_id, contract_no, planned_total_amount, actual_total_amount, auditor_name, audit_time, notify_url, plan_notify_number, actual_notify_number, remittance_strategy, remark, transaction_recipient, execution_status, execution_remark, create_time, creator_name, ip, last_modified_time, opposite_receive_date, remittance_id, total_count, actual_count, version_lock, check_request_no, check_status, check_retry_number, check_send_time, notify_status, int_field_1, int_field_2, int_field_3, string_field_1, string_field_2, string_field_3, decimal_field_1, decimal_field_2, decimal_field_3)
VALUES ('b0f106be-14d6-4147-9587-9e49bf20f76d', 'e7d4a5ac-4022-4aae-bccc-55cfbb4eafd1DZZ',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 'G31700', '1514202504451',
                                                '1514202504451', 1000.00, 1000.00, 'auditorName1',
                                                '2016-08-20 00:00:00', 'www.baidu.com', 3, 0, 0, 'recordSn', 1, 2, NULL,
                                                                       '2017-12-25 19:48:22', 't_test_zfb',
  '115.197.185.153, 120.26.102.180', '2017-12-25 19:48:42', '2017-12-25 19:48:32',
  '03ab1895-089d-42f9-9895-8754fdfcaf81', 1, 1, '8855f992-bdb1-4ae7-992b-3ce8ba6239bc',
  '4d7d3457-a0cf-484e-93aa-db23918b3f16', 1, 3, '2017-12-25 19:48:22', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
                                                NULL, NULL);
INSERT INTO t_remittance_application (remittance_application_uuid, request_no, financial_contract_uuid, financial_contract_id, financial_product_code, contract_unique_id, contract_no, planned_total_amount, actual_total_amount, auditor_name, audit_time, notify_url, plan_notify_number, actual_notify_number, remittance_strategy, remark, transaction_recipient, execution_status, execution_remark, create_time, creator_name, ip, last_modified_time, opposite_receive_date, remittance_id, total_count, actual_count, version_lock, check_request_no, check_status, check_retry_number, check_send_time, notify_status, int_field_1, int_field_2, int_field_3, string_field_1, string_field_2, string_field_3, decimal_field_1, decimal_field_2, decimal_field_3)
VALUES ('defcd2bf-0603-4360-8c4c-714a58ed7d1a', '7c18b081-14c5-49f2-bb67-88f257e33228DZZ',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 'G31700', '1514204062496',
                                                '1514204062496', 1000.00, 1000.00, 'auditorName1',
                                                '2016-08-20 00:00:00', 'www.baidu.com', 3, 0, 0, 'recordSn', 1, 2, NULL,
                                                                       '2017-12-25 20:14:20', 't_test_zfb',
  '115.197.185.153, 120.26.102.180', '2017-12-25 20:14:41', '2017-12-25 20:14:33',
  'd88144e3-ec84-41f7-a0c1-66668b4e9c63', 1, 1, 'fc41d7a8-c3a4-47d4-ac87-b29d3e5c9e86',
  'b9d718fd-35ae-4b14-9ce1-f11ae894a7c6', 1, 3, '2017-12-25 20:14:20', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
                                                NULL, NULL);
INSERT INTO t_remittance_application (remittance_application_uuid, request_no, financial_contract_uuid, financial_contract_id, financial_product_code, contract_unique_id, contract_no, planned_total_amount, actual_total_amount, auditor_name, audit_time, notify_url, plan_notify_number, actual_notify_number, remittance_strategy, remark, transaction_recipient, execution_status, execution_remark, create_time, creator_name, ip, last_modified_time, opposite_receive_date, remittance_id, total_count, actual_count, version_lock, check_request_no, check_status, check_retry_number, check_send_time, notify_status, int_field_1, int_field_2, int_field_3, string_field_1, string_field_2, string_field_3, decimal_field_1, decimal_field_2, decimal_field_3)
VALUES ('84ee7c10-b3bc-4487-ba66-7587ba614cc7', 'c3654dcb-143c-4dce-b37e-6191ea6aafd8DZZ',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 'G31700', '1514204300818',
                                                '1514204300818', 1000.00, 1000.00, 'auditorName1',
                                                '2016-08-20 00:00:00', 'www.baidu.com', 3, 0, 0, 'recordSn', 1, 2, NULL,
                                                                       '2017-12-25 20:18:19', 't_test_zfb',
  '115.197.185.153, 120.26.102.180', '2017-12-25 20:18:41', '2017-12-25 20:18:33',
  '4f961ff3-0c81-4170-82c7-c59eb3321d4d', 1, 1, '7cbc672c-b322-4e10-82e0-0530ea51cde3',
  '4e463ff5-76e2-41b7-8ab2-268497dd677b', 1, 3, '2017-12-25 20:18:19', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
                                                NULL, NULL);


INSERT INTO t_remittance_plan (remittance_plan_uuid, remittance_application_uuid, remittance_application_detail_uuid, business_record_no, financial_contract_uuid, financial_contract_id, contract_unique_id, contract_no, payment_gateway, payment_channel_uuid, payment_channel_name, pg_account_name, pg_account_no, pg_clearing_account, transaction_type, transaction_remark, priority_level, cp_bank_code, cp_bank_card_no, cp_bank_account_holder, cp_id_type, cp_id_number, cp_bank_province, cp_bank_city, cp_bank_name, planned_payment_date, complete_payment_date, planned_total_amount, actual_total_amount, execution_precond, execution_status, execution_remark, transaction_serial_no, create_time, creator_name, last_modified_time, int_field_1, int_field_2, int_field_3, string_field_1, string_field_2, string_field_3, decimal_field_1, decimal_field_2, decimal_field_3)
VALUES ('e5a55c27-e41b-4985-bcd0-081f97e55fc7', '84ff315b-8bb0-4d36-8a3b-34f69fb38f0a',
                                                '9095839c-97ff-4b8a-a85b-94efef701ee6', 'detailNo1',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 'overFT501', NULL, 2,
                                                'd9f4dc55-cecf-4721-a23c-724d53460b30', 'G31700浦发银企直联', '云南国际信托有限公司',
                                                                                                        '6224080002395',
                                                                                                        NULL, 0, '交易备注',
                                                                                                        1, 'C10102',
                                                                                                        '6214855712106520',
                                                                                                        '测试用户1', 0,
  'idNumber1', 'bankProvince1', 'bankCity1', 'bankName1', '2017-12-25 00:00:00', '2017-12-25 10:43:32', 300000.00,
  300000.00, NULL, 2, '测试置成功', '91986361790435', '2017-12-25 10:43:09', 't_test_zfb', '2017-12-25 10:43:42', NULL, NULL,
                      NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO t_remittance_plan (remittance_plan_uuid, remittance_application_uuid, remittance_application_detail_uuid, business_record_no, financial_contract_uuid, financial_contract_id, contract_unique_id, contract_no, payment_gateway, payment_channel_uuid, payment_channel_name, pg_account_name, pg_account_no, pg_clearing_account, transaction_type, transaction_remark, priority_level, cp_bank_code, cp_bank_card_no, cp_bank_account_holder, cp_id_type, cp_id_number, cp_bank_province, cp_bank_city, cp_bank_name, planned_payment_date, complete_payment_date, planned_total_amount, actual_total_amount, execution_precond, execution_status, execution_remark, transaction_serial_no, create_time, creator_name, last_modified_time, int_field_1, int_field_2, int_field_3, string_field_1, string_field_2, string_field_3, decimal_field_1, decimal_field_2, decimal_field_3)
VALUES ('5b7ea2d1-8d19-4268-a3c9-ba3714a7efee', '7ae7c538-c49e-4bab-bc73-61a2364dea17',
                                                'e7454b4a-befd-4400-a78c-0926e33737b2', 'detailNo1',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 'overFT502', NULL, 2,
                                                'd9f4dc55-cecf-4721-a23c-724d53460b30', 'G31700浦发银企直联', '云南国际信托有限公司',
                                                                                                        '6224080002395',
                                                                                                        NULL, 0, '交易备注',
                                                                                                        1, 'C10102',
                                                                                                        '6214855712106520',
                                                                                                        '测试用户1', 0,
  'idNumber1', 'bankProvince1', 'bankCity1', 'bankName1', '2017-12-25 00:00:00', '2017-12-25 10:58:32', 300000.00,
  300000.00, NULL, 2, '测试置成功', '70392449451975', '2017-12-25 10:58:15', 't_test_zfb', '2017-12-25 10:58:42', NULL, NULL,
                      NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO t_remittance_plan (remittance_plan_uuid, remittance_application_uuid, remittance_application_detail_uuid, business_record_no, financial_contract_uuid, financial_contract_id, contract_unique_id, contract_no, payment_gateway, payment_channel_uuid, payment_channel_name, pg_account_name, pg_account_no, pg_clearing_account, transaction_type, transaction_remark, priority_level, cp_bank_code, cp_bank_card_no, cp_bank_account_holder, cp_id_type, cp_id_number, cp_bank_province, cp_bank_city, cp_bank_name, planned_payment_date, complete_payment_date, planned_total_amount, actual_total_amount, execution_precond, execution_status, execution_remark, transaction_serial_no, create_time, creator_name, last_modified_time, int_field_1, int_field_2, int_field_3, string_field_1, string_field_2, string_field_3, decimal_field_1, decimal_field_2, decimal_field_3)
VALUES ('fe2de331-0c0e-4fbe-a597-59a034b0052a', 'a9fd9572-4cd8-4184-a7be-c86c6a05391d',
                                                '19c92e5c-fa60-426a-b10d-621c95dafaa5', 'detailNo1',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 'overFT503', NULL, 2,
                                                'd9f4dc55-cecf-4721-a23c-724d53460b30', 'G31700浦发银企直联', '云南国际信托有限公司',
                                                                                                        '6224080002395',
                                                                                                        NULL, 0, '交易备注',
                                                                                                        1, 'C10102',
                                                                                                        '6214855712106520',
                                                                                                        '测试用户1', 0,
  'idNumber1', 'bankProvince1', 'bankCity1', 'bankName1', '2017-12-25 00:00:00', '2017-12-25 11:00:32', 300000.00,
  300000.00, NULL, 2, '测试置成功', '37679903430566', '2017-12-25 11:00:17', 't_test_zfb', '2017-12-25 11:00:42', NULL, NULL,
                      NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO t_remittance_plan (remittance_plan_uuid, remittance_application_uuid, remittance_application_detail_uuid, business_record_no, financial_contract_uuid, financial_contract_id, contract_unique_id, contract_no, payment_gateway, payment_channel_uuid, payment_channel_name, pg_account_name, pg_account_no, pg_clearing_account, transaction_type, transaction_remark, priority_level, cp_bank_code, cp_bank_card_no, cp_bank_account_holder, cp_id_type, cp_id_number, cp_bank_province, cp_bank_city, cp_bank_name, planned_payment_date, complete_payment_date, planned_total_amount, actual_total_amount, execution_precond, execution_status, execution_remark, transaction_serial_no, create_time, creator_name, last_modified_time, int_field_1, int_field_2, int_field_3, string_field_1, string_field_2, string_field_3, decimal_field_1, decimal_field_2, decimal_field_3)
VALUES ('d01c0cc9-b337-4d04-8f42-66fa341af700', 'd593188f-d40e-46d7-b43d-58435dc72efe',
                                                '4ecc0cfd-0f88-483d-9586-b562eccf1018', '1',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38,
                                                'ab025451e63042a48fa834e9102e689e', '合同模板(hk15141834394613323)号', 2,
                                                'd9f4dc55-cecf-4721-a23c-724d53460b30', 'G31700浦发银企直联', '云南国际信托有限公司',
                                                                                                        '6224080002395',
                                                                                                        NULL, 0, 'mudi',
                                                                                                        1, 'C10305',
                                                                                                        '6217001210075323010',
                                                                                                        'wyejob03', 0,
  NULL, '310000', '310100', 'tianlin', '2017-12-25 00:00:00', '2017-12-25 15:20:02', 10000.00, 10000.00, NULL, 2,
  '测试置成功', '47958958752415', '2017-12-25 15:19:54', 't_merchant', '2017-12-25 15:20:11', NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL);
INSERT INTO t_remittance_plan (remittance_plan_uuid, remittance_application_uuid, remittance_application_detail_uuid, business_record_no, financial_contract_uuid, financial_contract_id, contract_unique_id, contract_no, payment_gateway, payment_channel_uuid, payment_channel_name, pg_account_name, pg_account_no, pg_clearing_account, transaction_type, transaction_remark, priority_level, cp_bank_code, cp_bank_card_no, cp_bank_account_holder, cp_id_type, cp_id_number, cp_bank_province, cp_bank_city, cp_bank_name, planned_payment_date, complete_payment_date, planned_total_amount, actual_total_amount, execution_precond, execution_status, execution_remark, transaction_serial_no, create_time, creator_name, last_modified_time, int_field_1, int_field_2, int_field_3, string_field_1, string_field_2, string_field_3, decimal_field_1, decimal_field_2, decimal_field_3)
VALUES ('8d6273ff-b763-498d-bb92-114862182362', 'dc5bd9dc-9ba6-447c-892c-9088ace34e00',
                                                '92f31e2e-211d-4e13-b213-ec8f9ee5b02a', 'detailNo1',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 'Remittance179',
                                                'Remittance179', 2, 'd9f4dc55-cecf-4721-a23c-724d53460b30',
                                                'G31700浦发银企直联', '云南国际信托有限公司', '6224080002395', NULL, 0, '交易备注', 1,
                                                                'C10102', '6214855712106520', '测试用户1', 0, 'idNumber1',
                                                                                                          'bankProvince1',
                                                                                                          'bankCity1',
                                                                                                          '',
                                                                                                          '2017-12-25 00:00:00',
                                                                                                          '2017-12-25 17:47:32',
                                                                                                          1500.00,
                                                                                                          1500.00, NULL,
                                                                                                          2, '测试置成功',
                                                                                                             '69836884056997',
                                                                                                             '2017-12-25 17:47:20',
                                                                                                             't_test_zfb',
                                                                                                             '2017-12-25 17:47:42',
                                                                                                             NULL, NULL,
                                                                                                             NULL, NULL,
                                                                                                             NULL, NULL,
        NULL, NULL, NULL);
INSERT INTO t_remittance_plan (remittance_plan_uuid, remittance_application_uuid, remittance_application_detail_uuid, business_record_no, financial_contract_uuid, financial_contract_id, contract_unique_id, contract_no, payment_gateway, payment_channel_uuid, payment_channel_name, pg_account_name, pg_account_no, pg_clearing_account, transaction_type, transaction_remark, priority_level, cp_bank_code, cp_bank_card_no, cp_bank_account_holder, cp_id_type, cp_id_number, cp_bank_province, cp_bank_city, cp_bank_name, planned_payment_date, complete_payment_date, planned_total_amount, actual_total_amount, execution_precond, execution_status, execution_remark, transaction_serial_no, create_time, creator_name, last_modified_time, int_field_1, int_field_2, int_field_3, string_field_1, string_field_2, string_field_3, decimal_field_1, decimal_field_2, decimal_field_3)
VALUES ('3d13ffab-a474-4a90-ba05-6379a3e82ca3', 'e41de349-b719-44e1-8b88-c97258246b15',
                                                'fc6e9ee7-63e7-40bd-ba62-f64c5e691b38', 'detailNo1',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, '1514196210772',
                                                '1514196210772', 2, 'd9f4dc55-cecf-4721-a23c-724d53460b30',
                                                'G31700浦发银企直联', '云南国际信托有限公司', '6224080002395', NULL, 0, 'recordSn', 1,
                                                                'C10102', '6214885712106520', '尹邦凤', 0, 'idNumber1',
                                                                                                        '310000',
                                                                                                        '310100',
                                                                                                        '中国农业银行',
                                                                                                        '2017-12-25 00:00:00',
                                                                                                        '2017-12-25 18:04:02',
                                                                                                        1000.00,
                                                                                                        1000.00, NULL,
                                                                                                        2, '测试置成功',
                                                                                                           '19976690430172',
                                                                                                           '2017-12-25 18:03:33',
                                                                                                           't_test_zfb',
                                                                                                           '2017-12-25 18:04:14',
                                                                                                           NULL, NULL,
                                                                                                           NULL, NULL,
                                                                                                           NULL, NULL,
        NULL, NULL, NULL);
INSERT INTO t_remittance_plan (remittance_plan_uuid, remittance_application_uuid, remittance_application_detail_uuid, business_record_no, financial_contract_uuid, financial_contract_id, contract_unique_id, contract_no, payment_gateway, payment_channel_uuid, payment_channel_name, pg_account_name, pg_account_no, pg_clearing_account, transaction_type, transaction_remark, priority_level, cp_bank_code, cp_bank_card_no, cp_bank_account_holder, cp_id_type, cp_id_number, cp_bank_province, cp_bank_city, cp_bank_name, planned_payment_date, complete_payment_date, planned_total_amount, actual_total_amount, execution_precond, execution_status, execution_remark, transaction_serial_no, create_time, creator_name, last_modified_time, int_field_1, int_field_2, int_field_3, string_field_1, string_field_2, string_field_3, decimal_field_1, decimal_field_2, decimal_field_3)
VALUES ('d249fde2-56b5-4a40-af3e-2b78762619ba', '50c0f08a-5f32-46e7-b7ac-720a7820ae99',
                                                'a962b20a-5adf-4e94-a778-499230ec16af', 'detailNo1',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, '1514196778105',
                                                '1514196778105', 2, 'd9f4dc55-cecf-4721-a23c-724d53460b30',
                                                'G31700浦发银企直联', '云南国际信托有限公司', '6224080002395', NULL, 0, 'recordSn', 1,
                                                                'C10102', '6214885712106520', '尹邦凤', 0, 'idNumber1',
                                                                                                        '310000',
                                                                                                        '310100',
                                                                                                        '中国农业银行',
                                                                                                        '2017-12-25 00:00:00',
                                                                                                        '2017-12-25 18:13:03',
                                                                                                        1000.00,
                                                                                                        1000.00, NULL,
                                                                                                        2, '测试置成功',
                                                                                                           '42207569295733',
                                                                                                           '2017-12-25 18:13:01',
                                                                                                           't_test_zfb',
                                                                                                           '2017-12-25 18:13:11',
                                                                                                           NULL, NULL,
                                                                                                           NULL, NULL,
                                                                                                           NULL, NULL,
        NULL, NULL, NULL);
INSERT INTO t_remittance_plan (remittance_plan_uuid, remittance_application_uuid, remittance_application_detail_uuid, business_record_no, financial_contract_uuid, financial_contract_id, contract_unique_id, contract_no, payment_gateway, payment_channel_uuid, payment_channel_name, pg_account_name, pg_account_no, pg_clearing_account, transaction_type, transaction_remark, priority_level, cp_bank_code, cp_bank_card_no, cp_bank_account_holder, cp_id_type, cp_id_number, cp_bank_province, cp_bank_city, cp_bank_name, planned_payment_date, complete_payment_date, planned_total_amount, actual_total_amount, execution_precond, execution_status, execution_remark, transaction_serial_no, create_time, creator_name, last_modified_time, int_field_1, int_field_2, int_field_3, string_field_1, string_field_2, string_field_3, decimal_field_1, decimal_field_2, decimal_field_3)
VALUES ('a4168d2a-70e2-4e02-8ada-028a619b67c8', 'bf90e83b-d903-463e-830e-5717fab2adaf',
                                                'd429da74-df90-498a-a0c9-5f07349cd66c', 'detailNo1',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, '1514201704435',
                                                '1514201704435', 2, 'd9f4dc55-cecf-4721-a23c-724d53460b30',
                                                'G31700浦发银企直联', '云南国际信托有限公司', '6224080002395', NULL, 0, 'recordSn', 1,
                                                                'C10102', '6214885712106520', '尹邦凤', 0, 'idNumber1',
                                                                                                        '310000',
                                                                                                        '310100',
                                                                                                        '中国农业银行',
                                                                                                        '2017-12-25 00:00:00',
                                                                                                        '2017-12-25 19:35:32',
                                                                                                        1000.00,
                                                                                                        1000.00, NULL,
                                                                                                        2, '测试置成功',
                                                                                                           '19991217665366',
                                                                                                           '2017-12-25 19:35:07',
                                                                                                           't_test_zfb',
                                                                                                           '2017-12-25 19:35:42',
                                                                                                           NULL, NULL,
                                                                                                           NULL, NULL,
                                                                                                           NULL, NULL,
        NULL, NULL, NULL);
INSERT INTO t_remittance_plan (remittance_plan_uuid, remittance_application_uuid, remittance_application_detail_uuid, business_record_no, financial_contract_uuid, financial_contract_id, contract_unique_id, contract_no, payment_gateway, payment_channel_uuid, payment_channel_name, pg_account_name, pg_account_no, pg_clearing_account, transaction_type, transaction_remark, priority_level, cp_bank_code, cp_bank_card_no, cp_bank_account_holder, cp_id_type, cp_id_number, cp_bank_province, cp_bank_city, cp_bank_name, planned_payment_date, complete_payment_date, planned_total_amount, actual_total_amount, execution_precond, execution_status, execution_remark, transaction_serial_no, create_time, creator_name, last_modified_time, int_field_1, int_field_2, int_field_3, string_field_1, string_field_2, string_field_3, decimal_field_1, decimal_field_2, decimal_field_3)
VALUES ('4276d0a5-1fa3-44d2-a6f2-30272c1aa32b', '77aa983e-96e8-4aea-957a-d81c1aed4695',
                                                '93f9b566-5ed5-45f8-9304-9d2a10126636', 'detailNo1',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, '1514202051446',
                                                '1514202051446', 2, 'd9f4dc55-cecf-4721-a23c-724d53460b30',
                                                'G31700浦发银企直联', '云南国际信托有限公司', '6224080002395', NULL, 0, 'recordSn', 1,
                                                                'C10102', '6214885712106520', '尹邦凤', 0, 'idNumber1',
                                                                                                        '310000',
                                                                                                        '310100',
                                                                                                        '中国农业银行',
                                                                                                        '2017-12-25 00:00:00',
                                                                                                        '2017-12-25 19:41:02',
                                                                                                        1000.00,
                                                                                                        1000.00, NULL,
                                                                                                        2, '测试置成功',
                                                                                                           '45918354155419',
                                                                                                           '2017-12-25 19:40:54',
                                                                                                           't_test_zfb',
                                                                                                           '2017-12-25 19:41:12',
                                                                                                           NULL, NULL,
                                                                                                           NULL, NULL,
                                                                                                           NULL, NULL,
        NULL, NULL, NULL);
INSERT INTO t_remittance_plan (remittance_plan_uuid, remittance_application_uuid, remittance_application_detail_uuid, business_record_no, financial_contract_uuid, financial_contract_id, contract_unique_id, contract_no, payment_gateway, payment_channel_uuid, payment_channel_name, pg_account_name, pg_account_no, pg_clearing_account, transaction_type, transaction_remark, priority_level, cp_bank_code, cp_bank_card_no, cp_bank_account_holder, cp_id_type, cp_id_number, cp_bank_province, cp_bank_city, cp_bank_name, planned_payment_date, complete_payment_date, planned_total_amount, actual_total_amount, execution_precond, execution_status, execution_remark, transaction_serial_no, create_time, creator_name, last_modified_time, int_field_1, int_field_2, int_field_3, string_field_1, string_field_2, string_field_3, decimal_field_1, decimal_field_2, decimal_field_3)
VALUES ('6f5e2ce8-b3c1-4160-895e-636fc20c9681', 'b0f106be-14d6-4147-9587-9e49bf20f76d',
                                                'bd463a57-c348-4396-94bd-f321ead4b812', 'detailNo1',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, '1514202504451',
                                                '1514202504451', 2, 'd9f4dc55-cecf-4721-a23c-724d53460b30',
                                                'G31700浦发银企直联', '云南国际信托有限公司', '6224080002395', NULL, 0, 'recordSn', 1,
                                                                'C10102', '6214885712106520', '尹邦凤', 0, 'idNumber1',
                                                                                                        '310000',
                                                                                                        '310100',
                                                                                                        '中国农业银行',
                                                                                                        '2017-12-25 00:00:00',
                                                                                                        '2017-12-25 19:48:32',
                                                                                                        1000.00,
                                                                                                        1000.00, NULL,
                                                                                                        2, '测试置成功',
                                                                                                           '32013544935745',
                                                                                                           '2017-12-25 19:48:25',
                                                                                                           't_test_zfb',
                                                                                                           '2017-12-25 19:48:42',
                                                                                                           NULL, NULL,
                                                                                                           NULL, NULL,
                                                                                                           NULL, NULL,
        NULL, NULL, NULL);
INSERT INTO t_remittance_plan (remittance_plan_uuid, remittance_application_uuid, remittance_application_detail_uuid, business_record_no, financial_contract_uuid, financial_contract_id, contract_unique_id, contract_no, payment_gateway, payment_channel_uuid, payment_channel_name, pg_account_name, pg_account_no, pg_clearing_account, transaction_type, transaction_remark, priority_level, cp_bank_code, cp_bank_card_no, cp_bank_account_holder, cp_id_type, cp_id_number, cp_bank_province, cp_bank_city, cp_bank_name, planned_payment_date, complete_payment_date, planned_total_amount, actual_total_amount, execution_precond, execution_status, execution_remark, transaction_serial_no, create_time, creator_name, last_modified_time, int_field_1, int_field_2, int_field_3, string_field_1, string_field_2, string_field_3, decimal_field_1, decimal_field_2, decimal_field_3)
VALUES ('99341cbb-801e-439d-9330-beb99cdfb5fc', 'defcd2bf-0603-4360-8c4c-714a58ed7d1a',
                                                '5bc09fca-6e3d-4a27-9e19-23289b0ab0c5', 'detailNo1',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, '1514204062496',
                                                '1514204062496', 2, 'd9f4dc55-cecf-4721-a23c-724d53460b30',
                                                'G31700浦发银企直联', '云南国际信托有限公司', '6224080002395', NULL, 0, 'recordSn', 1,
                                                                'C10102', '6214885712106520', '尹邦凤', 0, 'idNumber1',
                                                                                                        '310000',
                                                                                                        '310100',
                                                                                                        '中国农业银行',
                                                                                                        '2017-12-25 00:00:00',
                                                                                                        '2017-12-25 20:14:33',
                                                                                                        1000.00,
                                                                                                        1000.00, NULL,
                                                                                                        2, '测试置成功',
                                                                                                           '86570786579112',
                                                                                                           '2017-12-25 20:14:24',
                                                                                                           't_test_zfb',
                                                                                                           '2017-12-25 20:14:41',
                                                                                                           NULL, NULL,
                                                                                                           NULL, NULL,
                                                                                                           NULL, NULL,
        NULL, NULL, NULL);
INSERT INTO t_remittance_plan (remittance_plan_uuid, remittance_application_uuid, remittance_application_detail_uuid, business_record_no, financial_contract_uuid, financial_contract_id, contract_unique_id, contract_no, payment_gateway, payment_channel_uuid, payment_channel_name, pg_account_name, pg_account_no, pg_clearing_account, transaction_type, transaction_remark, priority_level, cp_bank_code, cp_bank_card_no, cp_bank_account_holder, cp_id_type, cp_id_number, cp_bank_province, cp_bank_city, cp_bank_name, planned_payment_date, complete_payment_date, planned_total_amount, actual_total_amount, execution_precond, execution_status, execution_remark, transaction_serial_no, create_time, creator_name, last_modified_time, int_field_1, int_field_2, int_field_3, string_field_1, string_field_2, string_field_3, decimal_field_1, decimal_field_2, decimal_field_3)
VALUES ('3d3495da-4203-4587-aef0-4283d44a7ebb', '84ee7c10-b3bc-4487-ba66-7587ba614cc7',
                                                '3ce43b1c-af09-4ea1-914d-c77be11ae195', 'detailNo1',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, '1514204300818',
                                                '1514204300818', 2, 'd9f4dc55-cecf-4721-a23c-724d53460b30',
                                                'G31700浦发银企直联', '云南国际信托有限公司', '6224080002395', NULL, 0, 'recordSn', 1,
                                                                'C10102', '6214885712106520', '尹邦凤', 0, 'idNumber1',
                                                                                                        '310000',
                                                                                                        '310100',
                                                                                                        '中国农业银行',
                                                                                                        '2017-12-25 00:00:00',
                                                                                                        '2017-12-25 20:18:33',
                                                                                                        1000.00,
                                                                                                        1000.00, NULL,
                                                                                                        2, '测试置成功',
                                                                                                           '4526623901470',
                                                                                                           '2017-12-25 20:18:23',
                                                                                                           't_test_zfb',
                                                                                                           '2017-12-25 20:18:41',
                                                                                                           NULL, NULL,
                                                                                                           NULL, NULL,
                                                                                                           NULL, NULL,
        NULL, NULL, NULL);


INSERT INTO t_remittance_plan_exec_log (remittance_application_uuid, remittance_plan_uuid, financial_contract_uuid, financial_contract_id, payment_gateway, payment_channel_uuid, payment_channel_name, pg_account_name, pg_account_no, pg_clearing_account, planned_amount, actual_total_amount, cp_bank_code, cp_bank_card_no, cp_bank_account_holder, cp_id_type, cp_id_number, cp_bank_province, cp_bank_city, cp_bank_name, transaction_type, transaction_remark, exec_req_no, exec_rsp_no, execution_status, execution_remark, transaction_serial_no, complete_payment_date, create_time, last_modified_time, plan_credit_cash_flow_check_number, actual_credit_cash_flow_check_number, reverse_status, credit_cash_flow_uuid, debit_cash_flow_uuid, transaction_recipient, opposite_receive_date, remittance_refund_bill_uuid)
VALUES ('84ff315b-8bb0-4d36-8a3b-34f69fb38f0a', 'e5a55c27-e41b-4985-bcd0-081f97e55fc7',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 2,
                                                'd9f4dc55-cecf-4721-a23c-724d53460b30', 'G31700浦发银企直联', '云南国际信托有限公司',
                                                '6224080002395', NULL, 300000.00, 300000.00, 'C10102',
                                                                                  '6214855712106520', '测试用户1', 0,
                                                                                  'idNumber1', 'bankProvince1',
                                                                                  'bankCity1', 'bankName1', 0, '交易备注',
                                                                                                               '80787b6d-dcec-45f8-8daf-03971c9d1401',
                                                                                                               '6e5baa31000085c0',
                                                                                                               2,
                                                                                                               '测试置成功',
                                                                                                               '91986361790435',
                                                                                                               '2017-12-25 10:43:32',
                                                                                                               '2017-12-25 10:43:09',
                                                                                                               '2017-12-25 10:43:42',
                                                                                                               3, 0, 0,
        NULL, NULL, 1, '2017-12-25 10:43:32', NULL);
INSERT INTO t_remittance_plan_exec_log (remittance_application_uuid, remittance_plan_uuid, financial_contract_uuid, financial_contract_id, payment_gateway, payment_channel_uuid, payment_channel_name, pg_account_name, pg_account_no, pg_clearing_account, planned_amount, actual_total_amount, cp_bank_code, cp_bank_card_no, cp_bank_account_holder, cp_id_type, cp_id_number, cp_bank_province, cp_bank_city, cp_bank_name, transaction_type, transaction_remark, exec_req_no, exec_rsp_no, execution_status, execution_remark, transaction_serial_no, complete_payment_date, create_time, last_modified_time, plan_credit_cash_flow_check_number, actual_credit_cash_flow_check_number, reverse_status, credit_cash_flow_uuid, debit_cash_flow_uuid, transaction_recipient, opposite_receive_date, remittance_refund_bill_uuid)
VALUES ('7ae7c538-c49e-4bab-bc73-61a2364dea17', '5b7ea2d1-8d19-4268-a3c9-ba3714a7efee',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 2,
                                                'd9f4dc55-cecf-4721-a23c-724d53460b30', 'G31700浦发银企直联', '云南国际信托有限公司',
                                                '6224080002395', NULL, 300000.00, 300000.00, 'C10102',
                                                                                  '6214855712106520', '测试用户1', 0,
                                                                                  'idNumber1', 'bankProvince1',
                                                                                  'bankCity1', 'bankName1', 0, '交易备注',
                                                                                                               'fdfc6df5-b2be-41ff-b29e-a750bb949a5e',
                                                                                                               '6e5baa31000086c0',
                                                                                                               2,
                                                                                                               '测试置成功',
                                                                                                               '70392449451975',
                                                                                                               '2017-12-25 10:58:32',
                                                                                                               '2017-12-25 10:58:15',
                                                                                                               '2017-12-25 10:58:42',
                                                                                                               3, 0, 0,
        NULL, NULL, 1, '2017-12-25 10:58:32', NULL);
INSERT INTO t_remittance_plan_exec_log (remittance_application_uuid, remittance_plan_uuid, financial_contract_uuid, financial_contract_id, payment_gateway, payment_channel_uuid, payment_channel_name, pg_account_name, pg_account_no, pg_clearing_account, planned_amount, actual_total_amount, cp_bank_code, cp_bank_card_no, cp_bank_account_holder, cp_id_type, cp_id_number, cp_bank_province, cp_bank_city, cp_bank_name, transaction_type, transaction_remark, exec_req_no, exec_rsp_no, execution_status, execution_remark, transaction_serial_no, complete_payment_date, create_time, last_modified_time, plan_credit_cash_flow_check_number, actual_credit_cash_flow_check_number, reverse_status, credit_cash_flow_uuid, debit_cash_flow_uuid, transaction_recipient, opposite_receive_date, remittance_refund_bill_uuid)
VALUES ('a9fd9572-4cd8-4184-a7be-c86c6a05391d', 'fe2de331-0c0e-4fbe-a597-59a034b0052a',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 2,
                                                'd9f4dc55-cecf-4721-a23c-724d53460b30', 'G31700浦发银企直联', '云南国际信托有限公司',
                                                '6224080002395', NULL, 300000.00, 300000.00, 'C10102',
                                                                                  '6214855712106520', '测试用户1', 0,
                                                                                  'idNumber1', 'bankProvince1',
                                                                                  'bankCity1', 'bankName1', 0, '交易备注',
                                                                                                               'dc475fa6-def8-44fd-87d9-bbfa148a24b2',
                                                                                                               '6e5baa31000087c0',
                                                                                                               2,
                                                                                                               '测试置成功',
                                                                                                               '37679903430566',
                                                                                                               '2017-12-25 11:00:32',
                                                                                                               '2017-12-25 11:00:17',
                                                                                                               '2017-12-25 11:00:42',
                                                                                                               3, 0, 0,
        NULL, NULL, 1, '2017-12-25 11:00:32', NULL);
INSERT INTO t_remittance_plan_exec_log (remittance_application_uuid, remittance_plan_uuid, financial_contract_uuid, financial_contract_id, payment_gateway, payment_channel_uuid, payment_channel_name, pg_account_name, pg_account_no, pg_clearing_account, planned_amount, actual_total_amount, cp_bank_code, cp_bank_card_no, cp_bank_account_holder, cp_id_type, cp_id_number, cp_bank_province, cp_bank_city, cp_bank_name, transaction_type, transaction_remark, exec_req_no, exec_rsp_no, execution_status, execution_remark, transaction_serial_no, complete_payment_date, create_time, last_modified_time, plan_credit_cash_flow_check_number, actual_credit_cash_flow_check_number, reverse_status, credit_cash_flow_uuid, debit_cash_flow_uuid, transaction_recipient, opposite_receive_date, remittance_refund_bill_uuid)
VALUES ('d593188f-d40e-46d7-b43d-58435dc72efe', 'd01c0cc9-b337-4d04-8f42-66fa341af700',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 2,
                                                'd9f4dc55-cecf-4721-a23c-724d53460b30', 'G31700浦发银企直联', '云南国际信托有限公司',
                                                '6224080002395', NULL, 10000.00, 10000.00, 'C10305',
                                                                                 '6217001210075323010', 'wyejob03', 0,
                                                                                 NULL, '310000', '310100', 'tianlin', 0,
  'mudi', 'b97c74d2-413a-49cc-b6c7-0234eed7757e', '6e5baa3100008ac0', 2, '测试置成功', '47958958752415',
  '2017-12-25 15:20:02', '2017-12-25 15:19:54', '2017-12-25 15:20:11', 3, 0, 0, NULL, NULL, 1, '2017-12-25 15:20:02',
        NULL);
INSERT INTO t_remittance_plan_exec_log (remittance_application_uuid, remittance_plan_uuid, financial_contract_uuid, financial_contract_id, payment_gateway, payment_channel_uuid, payment_channel_name, pg_account_name, pg_account_no, pg_clearing_account, planned_amount, actual_total_amount, cp_bank_code, cp_bank_card_no, cp_bank_account_holder, cp_id_type, cp_id_number, cp_bank_province, cp_bank_city, cp_bank_name, transaction_type, transaction_remark, exec_req_no, exec_rsp_no, execution_status, execution_remark, transaction_serial_no, complete_payment_date, create_time, last_modified_time, plan_credit_cash_flow_check_number, actual_credit_cash_flow_check_number, reverse_status, credit_cash_flow_uuid, debit_cash_flow_uuid, transaction_recipient, opposite_receive_date, remittance_refund_bill_uuid)
VALUES ('dc5bd9dc-9ba6-447c-892c-9088ace34e00', '8d6273ff-b763-498d-bb92-114862182362',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 2,
                                                'd9f4dc55-cecf-4721-a23c-724d53460b30', 'G31700浦发银企直联', '云南国际信托有限公司',
                                                '6224080002395', NULL, 1500.00, 1500.00, 'C10102', '6214855712106520',
                                                                                '测试用户1', 0, 'idNumber1',
                                                                                'bankProvince1', 'bankCity1', '', 0,
  '交易备注', '5b750bad-abb5-4080-a2aa-418756cdd04a', '6e5baa3100008dc0', 2, '测试置成功', '69836884056997',
  '2017-12-25 17:47:32', '2017-12-25 17:47:21', '2017-12-25 17:47:42', 3, 0, 0, NULL, NULL, 1, '2017-12-25 17:47:32',
        NULL);
INSERT INTO t_remittance_plan_exec_log (remittance_application_uuid, remittance_plan_uuid, financial_contract_uuid, financial_contract_id, payment_gateway, payment_channel_uuid, payment_channel_name, pg_account_name, pg_account_no, pg_clearing_account, planned_amount, actual_total_amount, cp_bank_code, cp_bank_card_no, cp_bank_account_holder, cp_id_type, cp_id_number, cp_bank_province, cp_bank_city, cp_bank_name, transaction_type, transaction_remark, exec_req_no, exec_rsp_no, execution_status, execution_remark, transaction_serial_no, complete_payment_date, create_time, last_modified_time, plan_credit_cash_flow_check_number, actual_credit_cash_flow_check_number, reverse_status, credit_cash_flow_uuid, debit_cash_flow_uuid, transaction_recipient, opposite_receive_date, remittance_refund_bill_uuid)
VALUES ('e41de349-b719-44e1-8b88-c97258246b15', '3d13ffab-a474-4a90-ba05-6379a3e82ca3',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 2,
                                                'd9f4dc55-cecf-4721-a23c-724d53460b30', 'G31700浦发银企直联', '云南国际信托有限公司',
                                                '6224080002395', NULL, 1000.00, 1000.00, 'C10102', '6214885712106520',
                                                                                '尹邦凤', 0, 'idNumber1', '310000',
                                                                                '310100', '中国农业银行', 0, 'recordSn',
                                                                                                       '929d75b6-84a2-4e3e-aca1-6cf3a20a80a6',
                                                                                                       '6e5baa3100008ec0',
                                                                                                       2, '测试置成功',
                                                                                                       '19976690430172',
                                                                                                       '2017-12-25 18:04:02',
                                                                                                       '2017-12-25 18:03:33',
                                                                                                       '2017-12-25 18:04:14',
                                                                                                       3, 0, 0, NULL,
        NULL, 1, '2017-12-25 18:04:02', NULL);
INSERT INTO t_remittance_plan_exec_log (remittance_application_uuid, remittance_plan_uuid, financial_contract_uuid, financial_contract_id, payment_gateway, payment_channel_uuid, payment_channel_name, pg_account_name, pg_account_no, pg_clearing_account, planned_amount, actual_total_amount, cp_bank_code, cp_bank_card_no, cp_bank_account_holder, cp_id_type, cp_id_number, cp_bank_province, cp_bank_city, cp_bank_name, transaction_type, transaction_remark, exec_req_no, exec_rsp_no, execution_status, execution_remark, transaction_serial_no, complete_payment_date, create_time, last_modified_time, plan_credit_cash_flow_check_number, actual_credit_cash_flow_check_number, reverse_status, credit_cash_flow_uuid, debit_cash_flow_uuid, transaction_recipient, opposite_receive_date, remittance_refund_bill_uuid)
VALUES ('50c0f08a-5f32-46e7-b7ac-720a7820ae99', 'd249fde2-56b5-4a40-af3e-2b78762619ba',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 2,
                                                'd9f4dc55-cecf-4721-a23c-724d53460b30', 'G31700浦发银企直联', '云南国际信托有限公司',
                                                '6224080002395', NULL, 1000.00, 1000.00, 'C10102', '6214885712106520',
                                                                                '尹邦凤', 0, 'idNumber1', '310000',
                                                                                '310100', '中国农业银行', 0, 'recordSn',
                                                                                                       'df3b3ba2-b50d-473a-84a4-232821cb0e03',
                                                                                                       '6e5baa3100008fc0',
                                                                                                       2, '测试置成功',
                                                                                                       '42207569295733',
                                                                                                       '2017-12-25 18:13:03',
                                                                                                       '2017-12-25 18:13:01',
                                                                                                       '2017-12-25 18:13:11',
                                                                                                       3, 0, 0, NULL,
        NULL, 1, '2017-12-25 18:13:03', NULL);
INSERT INTO t_remittance_plan_exec_log (remittance_application_uuid, remittance_plan_uuid, financial_contract_uuid, financial_contract_id, payment_gateway, payment_channel_uuid, payment_channel_name, pg_account_name, pg_account_no, pg_clearing_account, planned_amount, actual_total_amount, cp_bank_code, cp_bank_card_no, cp_bank_account_holder, cp_id_type, cp_id_number, cp_bank_province, cp_bank_city, cp_bank_name, transaction_type, transaction_remark, exec_req_no, exec_rsp_no, execution_status, execution_remark, transaction_serial_no, complete_payment_date, create_time, last_modified_time, plan_credit_cash_flow_check_number, actual_credit_cash_flow_check_number, reverse_status, credit_cash_flow_uuid, debit_cash_flow_uuid, transaction_recipient, opposite_receive_date, remittance_refund_bill_uuid)
VALUES ('bf90e83b-d903-463e-830e-5717fab2adaf', 'a4168d2a-70e2-4e02-8ada-028a619b67c8',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 2,
                                                'd9f4dc55-cecf-4721-a23c-724d53460b30', 'G31700浦发银企直联', '云南国际信托有限公司',
                                                '6224080002395', NULL, 1000.00, 1000.00, 'C10102', '6214885712106520',
                                                                                '尹邦凤', 0, 'idNumber1', '310000',
                                                                                '310100', '中国农业银行', 0, 'recordSn',
                                                                                                       '58d265fb-e93f-4209-92d1-0bb90ee56fb6',
                                                                                                       '6e5baa31000090c0',
                                                                                                       2, '测试置成功',
                                                                                                       '19991217665366',
                                                                                                       '2017-12-25 19:35:32',
                                                                                                       '2017-12-25 19:35:07',
                                                                                                       '2017-12-25 19:35:42',
                                                                                                       3, 0, 0, NULL,
        NULL, 1, '2017-12-25 19:35:32', NULL);
INSERT INTO t_remittance_plan_exec_log (remittance_application_uuid, remittance_plan_uuid, financial_contract_uuid, financial_contract_id, payment_gateway, payment_channel_uuid, payment_channel_name, pg_account_name, pg_account_no, pg_clearing_account, planned_amount, actual_total_amount, cp_bank_code, cp_bank_card_no, cp_bank_account_holder, cp_id_type, cp_id_number, cp_bank_province, cp_bank_city, cp_bank_name, transaction_type, transaction_remark, exec_req_no, exec_rsp_no, execution_status, execution_remark, transaction_serial_no, complete_payment_date, create_time, last_modified_time, plan_credit_cash_flow_check_number, actual_credit_cash_flow_check_number, reverse_status, credit_cash_flow_uuid, debit_cash_flow_uuid, transaction_recipient, opposite_receive_date, remittance_refund_bill_uuid)
VALUES ('77aa983e-96e8-4aea-957a-d81c1aed4695', '4276d0a5-1fa3-44d2-a6f2-30272c1aa32b',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 2,
                                                'd9f4dc55-cecf-4721-a23c-724d53460b30', 'G31700浦发银企直联', '云南国际信托有限公司',
                                                '6224080002395', NULL, 1000.00, 1000.00, 'C10102', '6214885712106520',
                                                                                '尹邦凤', 0, 'idNumber1', '310000',
                                                                                '310100', '中国农业银行', 0, 'recordSn',
                                                                                                       '66841082-8515-4522-ad2b-4131d9770742',
                                                                                                       '6e5baa31000091c0',
                                                                                                       2, '测试置成功',
                                                                                                       '45918354155419',
                                                                                                       '2017-12-25 19:41:02',
                                                                                                       '2017-12-25 19:40:54',
                                                                                                       '2017-12-25 19:41:12',
                                                                                                       3, 0, 0, NULL,
        NULL, 1, '2017-12-25 19:41:02', NULL);
INSERT INTO t_remittance_plan_exec_log (remittance_application_uuid, remittance_plan_uuid, financial_contract_uuid, financial_contract_id, payment_gateway, payment_channel_uuid, payment_channel_name, pg_account_name, pg_account_no, pg_clearing_account, planned_amount, actual_total_amount, cp_bank_code, cp_bank_card_no, cp_bank_account_holder, cp_id_type, cp_id_number, cp_bank_province, cp_bank_city, cp_bank_name, transaction_type, transaction_remark, exec_req_no, exec_rsp_no, execution_status, execution_remark, transaction_serial_no, complete_payment_date, create_time, last_modified_time, plan_credit_cash_flow_check_number, actual_credit_cash_flow_check_number, reverse_status, credit_cash_flow_uuid, debit_cash_flow_uuid, transaction_recipient, opposite_receive_date, remittance_refund_bill_uuid)
VALUES ('b0f106be-14d6-4147-9587-9e49bf20f76d', '6f5e2ce8-b3c1-4160-895e-636fc20c9681',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 2,
                                                'd9f4dc55-cecf-4721-a23c-724d53460b30', 'G31700浦发银企直联', '云南国际信托有限公司',
                                                '6224080002395', NULL, 1000.00, 1000.00, 'C10102', '6214885712106520',
                                                                                '尹邦凤', 0, 'idNumber1', '310000',
                                                                                '310100', '中国农业银行', 0, 'recordSn',
                                                                                                       '12e5a3b7-49eb-4d7f-9671-fdc74258a884',
                                                                                                       '6e5baa31000092c0',
                                                                                                       2, '测试置成功',
                                                                                                       '32013544935745',
                                                                                                       '2017-12-25 19:48:32',
                                                                                                       '2017-12-25 19:48:26',
                                                                                                       '2017-12-25 19:48:42',
                                                                                                       3, 0, 0, NULL,
        NULL, 1, '2017-12-25 19:48:32', NULL);
INSERT INTO t_remittance_plan_exec_log (remittance_application_uuid, remittance_plan_uuid, financial_contract_uuid, financial_contract_id, payment_gateway, payment_channel_uuid, payment_channel_name, pg_account_name, pg_account_no, pg_clearing_account, planned_amount, actual_total_amount, cp_bank_code, cp_bank_card_no, cp_bank_account_holder, cp_id_type, cp_id_number, cp_bank_province, cp_bank_city, cp_bank_name, transaction_type, transaction_remark, exec_req_no, exec_rsp_no, execution_status, execution_remark, transaction_serial_no, complete_payment_date, create_time, last_modified_time, plan_credit_cash_flow_check_number, actual_credit_cash_flow_check_number, reverse_status, credit_cash_flow_uuid, debit_cash_flow_uuid, transaction_recipient, opposite_receive_date, remittance_refund_bill_uuid)
VALUES ('defcd2bf-0603-4360-8c4c-714a58ed7d1a', '99341cbb-801e-439d-9330-beb99cdfb5fc',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 2,
                                                'd9f4dc55-cecf-4721-a23c-724d53460b30', 'G31700浦发银企直联', '云南国际信托有限公司',
                                                '6224080002395', NULL, 1000.00, 1000.00, 'C10102', '6214885712106520',
                                                                                '尹邦凤', 0, 'idNumber1', '310000',
                                                                                '310100', '中国农业银行', 0, 'recordSn',
                                                                                                       '5cf1bab0-69da-4847-9ccb-8c98320c3082',
                                                                                                       '6e5baa31000093c0',
                                                                                                       2, '测试置成功',
                                                                                                       '86570786579112',
                                                                                                       '2017-12-25 20:14:33',
                                                                                                       '2017-12-25 20:14:24',
                                                                                                       '2017-12-25 20:14:41',
                                                                                                       3, 0, 0, NULL,
        NULL, 1, '2017-12-25 20:14:33', NULL);
INSERT INTO t_remittance_plan_exec_log (remittance_application_uuid, remittance_plan_uuid, financial_contract_uuid, financial_contract_id, payment_gateway, payment_channel_uuid, payment_channel_name, pg_account_name, pg_account_no, pg_clearing_account, planned_amount, actual_total_amount, cp_bank_code, cp_bank_card_no, cp_bank_account_holder, cp_id_type, cp_id_number, cp_bank_province, cp_bank_city, cp_bank_name, transaction_type, transaction_remark, exec_req_no, exec_rsp_no, execution_status, execution_remark, transaction_serial_no, complete_payment_date, create_time, last_modified_time, plan_credit_cash_flow_check_number, actual_credit_cash_flow_check_number, reverse_status, credit_cash_flow_uuid, debit_cash_flow_uuid, transaction_recipient, opposite_receive_date, remittance_refund_bill_uuid)
VALUES ('84ee7c10-b3bc-4487-ba66-7587ba614cc7', '3d3495da-4203-4587-aef0-4283d44a7ebb',
                                                'd2812bc5-5057-4a91-b3fd-9019506f0499', 38, 2,
                                                'd9f4dc55-cecf-4721-a23c-724d53460b30', 'G31700浦发银企直联', '云南国际信托有限公司',
                                                '6224080002395', NULL, 1000.00, 1000.00, 'C10102', '6214885712106520',
                                                                                '尹邦凤', 0, 'idNumber1', '310000',
                                                                                '310100', '中国农业银行', 0, 'recordSn',
                                                                                                       'd456b18e-17a2-4ea7-87ed-e1b081d8029c',
                                                                                                       '6e5baa31000094c0',
                                                                                                       2, '测试置成功',
                                                                                                       '4526623901470',
                                                                                                       '2017-12-25 20:18:33',
                                                                                                       '2017-12-25 20:18:23',
                                                                                                       '2017-12-25 20:18:41',
                                                                                                       3, 0, 0, NULL,
        NULL, 1, '2017-12-25 20:18:33', NULL);


INSERT INTO contract (uuid, unique_id, begin_date, contract_no, end_date, asset_type, month_fee, app_id, customer_id, house_id, actual_end_date, create_time, interest_rate, payment_day_in_month, payment_frequency, periods, repayment_way, total_amount, penalty_interest, active_version_no, repayment_plan_operate_logs, state, financial_contract_uuid, interest_rate_cycle, customer_uuid, repaymented_periods, completion_status, date_field_one, long_field_one, long_field_two, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two)
VALUES
  ('03914980-1122-4b72-a3bf-6be51776d72c', 'overFT501', '2017-12-25', 'overFT501', '2018-08-20', NULL, 0.00, 3, 270536,
                                           270671, NULL, '2017-12-25 10:45:30', 0.0003330000, 0, 0, 3, 2, 300000.00,
                                                         0.0005000000, 1, NULL, 2,
                                                                                'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                1,
                                                                                '2a9ac733-157a-4796-bad1-31c904a1796e',
                                                                                0, NULL, NULL, NULL, NULL, NULL, NULL,
   NULL, NULL, NULL);
INSERT INTO contract (uuid, unique_id, begin_date, contract_no, end_date, asset_type, month_fee, app_id, customer_id, house_id, actual_end_date, create_time, interest_rate, payment_day_in_month, payment_frequency, periods, repayment_way, total_amount, penalty_interest, active_version_no, repayment_plan_operate_logs, state, financial_contract_uuid, interest_rate_cycle, customer_uuid, repaymented_periods, completion_status, date_field_one, long_field_one, long_field_two, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two)
VALUES
  ('c3d1229c-13dc-4092-9c24-899768bb049d', 'overFT502', '2017-12-25', 'overFT502', '2018-08-20', NULL, 0.00, 3, 270537,
                                           270672, NULL, '2017-12-25 10:59:43', 0.0003330000, 0, 0, 3, 2, 300000.00,
                                                         0.0005000000, 1, NULL, 2,
                                                                                'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                1,
                                                                                '71bbcf28-d69a-4f8b-9f77-77a8b023420b',
                                                                                0, NULL, NULL, NULL, NULL, NULL, NULL,
   NULL, NULL, NULL);
INSERT INTO contract (uuid, unique_id, begin_date, contract_no, end_date, asset_type, month_fee, app_id, customer_id, house_id, actual_end_date, create_time, interest_rate, payment_day_in_month, payment_frequency, periods, repayment_way, total_amount, penalty_interest, active_version_no, repayment_plan_operate_logs, state, financial_contract_uuid, interest_rate_cycle, customer_uuid, repaymented_periods, completion_status, date_field_one, long_field_one, long_field_two, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two)
VALUES
  ('1f6b9b46-1ca3-44f5-b00f-ceec36bdf3a8', 'overFT503', '2017-12-25', 'overFT503', '2018-08-20', NULL, 0.00, 3, 270539,
                                           270674, NULL, '2017-12-25 11:01:19', 0.0003330000, 0, 0, 3, 2, 300000.00,
                                                         0.0005000000, 1, NULL, 2,
                                                                                'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                1,
                                                                                '2a6d9072-7695-4e4d-afa6-0fb97b2935e7',
                                                                                0, NULL, NULL, NULL, NULL, NULL, NULL,
   NULL, NULL, NULL);
INSERT INTO contract (uuid, unique_id, begin_date, contract_no, end_date, asset_type, month_fee, app_id, customer_id, house_id, actual_end_date, create_time, interest_rate, payment_day_in_month, payment_frequency, periods, repayment_way, total_amount, penalty_interest, active_version_no, repayment_plan_operate_logs, state, financial_contract_uuid, interest_rate_cycle, customer_uuid, repaymented_periods, completion_status, date_field_one, long_field_one, long_field_two, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two)
VALUES
  ('d77a59d4-e44d-499a-99be-a6fa1b1f0c6b', '1514196210772', '2017-12-25', '1514196210772', '2099-01-01', NULL, 0.00, 3,
                                           270583, 270718, NULL, '2017-12-25 18:04:17', 0.1560000000, 0, 0, 1, 2,
                                                                 1000.00, 0.0005000000, 1, NULL, 2,
                                                                                                 'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                                 1,
                                                                                                 '4ec38eda-61a8-47f9-8bab-500dc0d6b6e5',
                                                                                                 0, NULL, NULL, NULL,
                                                                                                 NULL, NULL, NULL, NULL,
   NULL, NULL);


INSERT INTO asset_set (guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES (1, 0, 100080.00, 100000.00, 20.00, 100020.00, '2017-12-26', NULL, 0.00, 0, 1, 0, '2018-01-09 06:32:42',
                                                                                      'adae7eb0-326a-49c2-abc1-6f5d28825df6',
                                                                                      '2017-12-25 10:45:30',
                                                                                      '2018-01-09 06:32:42', NULL,
                                                                                      'ZC142620868161093632', 12482461,
                                                                                      NULL, 1, 1, NULL, 1, 0, 0,
                                                                                               'empty_deduct_uuid',
                                                                                               NULL,
                                                                                               'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                               '027016d29fab670d726c03968ed7d2e2',
                                                                                               'd4de8e2b6fc4af949a22e10a3efd3a65',
  '2017-12-25 10:45:30', '2017-12-25 10:45:30', 0, 0, 0, 1, 0, 1, 0, '2a9ac733-157a-4796-bad1-31c904a1796e',
        '03914980-1122-4b72-a3bf-6be51776d72c', NULL, '954d3bf6-3a32-43ed-a047-254107963e0f', 0,
        'f309501cd361c51be5a201c6cabf5a98', 'c3c46bc4-c1d9-4537-87b7-08058aa38ae9');
INSERT INTO asset_set (guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES (0, 0, 100080.00, 100000.00, 20.00, 100020.00, '2018-01-15', NULL, 0.00, 0, 1, 0, NULL,
                                                                                      '2c3e6cae-1466-4aeb-a804-0a65a65b5d59',
                                                                                      '2017-12-25 10:45:31',
                                                                                      '2017-12-25 10:45:31', NULL,
                                                                                      'ZC142620869205475328', 12482461,
                                                                                      NULL, 2, 0, NULL, 1, 0, 0,
                                                                                               'empty_deduct_uuid',
                                                                                               NULL,
                                                                                               'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                               'f7ddff60fb29959537c48452d15949a2',
                                                                                               'd4de8e2b6fc4af949a22e10a3efd3a65',
  '2017-12-25 10:45:31', '2017-12-25 10:45:31', 0, 0, 0, 0, 0, 0, 0, '2a9ac733-157a-4796-bad1-31c904a1796e',
        '03914980-1122-4b72-a3bf-6be51776d72c', NULL, '8a629f35-2dfb-4874-ad20-e960c0a17499', 0,
        '139e4c5fd0f71b23b7d2df02affd43b1', '4d0a9e11-70ad-4ddf-a620-6c01daa46bfc');
INSERT INTO asset_set (guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES (0, 0, 100080.00, 100000.00, 20.00, 100020.00, '2018-02-20', NULL, 0.00, 0, 1, 0, NULL,
                                                                                      '592547a3-639d-4536-9f04-1d3dacf718d2',
                                                                                      '2017-12-25 10:45:31',
                                                                                      '2017-12-25 10:45:31', NULL,
                                                                                      'ZC142620869335498752', 12482461,
                                                                                      NULL, 3, 0, NULL, 1, 0, 0,
                                                                                               'empty_deduct_uuid',
                                                                                               NULL,
                                                                                               'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                               '7d2eec5d4688ba20074a2f52872fb0ff',
                                                                                               'd4de8e2b6fc4af949a22e10a3efd3a65',
  '2017-12-25 10:45:31', '2017-12-25 10:45:31', 0, 0, 0, 0, 0, 0, 0, '2a9ac733-157a-4796-bad1-31c904a1796e',
        '03914980-1122-4b72-a3bf-6be51776d72c', NULL, '413ef55a-cbda-4cb2-832d-437075a34459', 0,
        '538442a3e61834c59c07eeddfbcbe915', 'fdf99af3-190c-4c42-949b-990c31d64368');
INSERT INTO asset_set (guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES (1, 0, 100080.00, 100000.00, 20.00, 100020.00, '2017-12-26', NULL, 0.00, 0, 1, 0, '2018-01-09 04:37:41',
                                                                                      'ca024d00-06a2-439c-b334-ea065d41b49f',
                                                                                      '2017-12-25 10:59:43',
                                                                                      '2018-01-09 04:37:41', NULL,
                                                                                      'ZC142624444790784000', 12482462,
                                                                                      NULL, 1, 1, NULL, 1, 0, 0,
                                                                                               'empty_deduct_uuid',
                                                                                               NULL,
                                                                                               'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                               '027016d29fab670d726c03968ed7d2e2',
                                                                                               'd4de8e2b6fc4af949a22e10a3efd3a65',
  '2017-12-25 10:59:43', '2017-12-25 10:59:43', 0, 0, 0, 1, 0, 1, 0, '71bbcf28-d69a-4f8b-9f77-77a8b023420b',
        'c3d1229c-13dc-4092-9c24-899768bb049d', NULL, 'cc4d0ec2-a8e2-4b25-84c9-fc33e2b7c237', 0,
        '4e5c0ae6d6d23b203cf09ff90640333e', '9c2b758a-9ea9-43af-9dce-43ec125f90cd');
INSERT INTO asset_set (guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES (0, 0, 100080.00, 100000.00, 20.00, 100020.00, '2018-01-15', NULL, 0.00, 0, 1, 0, NULL,
                                                                                      'a81205bd-3b59-4160-a62b-18c37544751f',
                                                                                      '2017-12-25 10:59:43',
                                                                                      '2017-12-25 10:59:43', NULL,
                                                                                      'ZC142624445075996672', 12482462,
                                                                                      NULL, 2, 0, NULL, 1, 0, 0,
                                                                                               'empty_deduct_uuid',
                                                                                               NULL,
                                                                                               'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                               'f7ddff60fb29959537c48452d15949a2',
                                                                                               'd4de8e2b6fc4af949a22e10a3efd3a65',
  '2017-12-25 10:59:43', '2017-12-25 10:59:43', 0, 0, 0, 0, 0, 0, 0, '71bbcf28-d69a-4f8b-9f77-77a8b023420b',
        'c3d1229c-13dc-4092-9c24-899768bb049d', NULL, '35d99726-c6bf-4742-81dc-1e0bacc0fff7', 0,
        '2766b58ec1584273eff1d3112d275052', 'fe67e7ce-2d64-4e98-aff9-dc17570c56e1');
INSERT INTO asset_set (guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES (0, 0, 100080.00, 100000.00, 20.00, 100020.00, '2018-02-20', NULL, 0.00, 0, 1, 0, NULL,
                                                                                      'c81d0e55-6030-40d8-8552-dbf1fe770aa8',
                                                                                      '2017-12-25 10:59:43',
                                                                                      '2017-12-25 10:59:43', NULL,
                                                                                      'ZC142624445201825792', 12482462,
                                                                                      NULL, 3, 0, NULL, 1, 0, 0,
                                                                                               'empty_deduct_uuid',
                                                                                               NULL,
                                                                                               'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                               '7d2eec5d4688ba20074a2f52872fb0ff',
                                                                                               'd4de8e2b6fc4af949a22e10a3efd3a65',
  '2017-12-25 10:59:43', '2017-12-25 10:59:43', 0, 0, 0, 0, 0, 0, 0, '71bbcf28-d69a-4f8b-9f77-77a8b023420b',
        'c3d1229c-13dc-4092-9c24-899768bb049d', NULL, 'd37b7a31-dfc6-4cae-a604-6fab1a4e240d', 0,
        '55f0528595aac5c2d64e0e6bedf40d5e', 'ea1d465c-0ec7-454e-be84-88ff97ece563');
INSERT INTO asset_set (guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES (1, 0, 100080.00, 100000.00, 20.00, 100020.00, '2017-12-26', NULL, 0.00, 0, 1, 0, '2018-01-09 06:38:24',
                                                                                      '029a7e9a-c894-450b-bdf6-dc72b1191e95',
                                                                                      '2017-12-25 11:01:19',
                                                                                      '2018-01-09 06:38:24', NULL,
                                                                                      'ZC142624846382809088', 12482464,
                                                                                      NULL, 1, 1, NULL, 1, 0, 0,
                                                                                               'empty_deduct_uuid',
                                                                                               NULL,
                                                                                               'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                               '027016d29fab670d726c03968ed7d2e2',
                                                                                               'd4de8e2b6fc4af949a22e10a3efd3a65',
  '2017-12-25 11:01:19', '2017-12-25 11:01:19', 0, 0, 0, 1, 0, 1, 0, '2a6d9072-7695-4e4d-afa6-0fb97b2935e7',
        '1f6b9b46-1ca3-44f5-b00f-ceec36bdf3a8', NULL, 'bac94a33-26e3-40a0-9d5f-e3db0e6534c0', 0,
        '3f6083dfa8402ad94f4076ddf23f1260', 'b5caf2d6-1400-475b-9d6e-aebc031ca458');
INSERT INTO asset_set (guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES (0, 0, 100080.00, 100000.00, 20.00, 100020.00, '2018-01-15', NULL, 0.00, 0, 1, 0, NULL,
                                                                                      'b36dab0e-4a39-4d27-9e97-a6a687d6ec6e',
                                                                                      '2017-12-25 11:01:19',
                                                                                      '2017-12-25 11:01:19', NULL,
                                                                                      'ZC142624846537998336', 12482464,
                                                                                      NULL, 2, 0, NULL, 1, 0, 0,
                                                                                               'empty_deduct_uuid',
                                                                                               NULL,
                                                                                               'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                               'f7ddff60fb29959537c48452d15949a2',
                                                                                               'd4de8e2b6fc4af949a22e10a3efd3a65',
  '2017-12-25 11:01:19', '2017-12-25 11:01:19', 0, 0, 0, 0, 0, 0, 0, '2a6d9072-7695-4e4d-afa6-0fb97b2935e7',
        '1f6b9b46-1ca3-44f5-b00f-ceec36bdf3a8', NULL, 'bde0bb02-1e72-4e18-af1d-52ceaea97771', 0,
        'ced863f6d98de8f297390ebf5de865c9', '197c2b87-06aa-4fb6-a2a6-97dd27062bf4');
INSERT INTO asset_set (guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES (0, 0, 100080.00, 100000.00, 20.00, 100020.00, '2018-02-20', NULL, 0.00, 0, 1, 0, NULL,
                                                                                      '40247e60-af7c-4139-923d-1c0e1bf540a1',
                                                                                      '2017-12-25 11:01:19',
                                                                                      '2017-12-25 11:01:19', NULL,
                                                                                      'ZC142624846642855936', 12482464,
                                                                                      NULL, 3, 0, NULL, 1, 0, 0,
                                                                                               'empty_deduct_uuid',
                                                                                               NULL,
                                                                                               'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                               '7d2eec5d4688ba20074a2f52872fb0ff',
                                                                                               'd4de8e2b6fc4af949a22e10a3efd3a65',
  '2017-12-25 11:01:19', '2017-12-25 11:01:19', 0, 0, 0, 0, 0, 0, 0, '2a6d9072-7695-4e4d-afa6-0fb97b2935e7',
        '1f6b9b46-1ca3-44f5-b00f-ceec36bdf3a8', NULL, '31dafa55-3316-4da8-80ef-f51f64ed35b5', 0,
        '39ff1c46f6c59a4f3ada6b0c332174cb', '3829e24b-dabb-4707-8cf0-1ead10552737');
INSERT INTO asset_set (guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES (1, 0, 1000.00, 1000.00, 0.00, 1000.00, '2017-12-26', NULL, 0.00, 0, 1, 0, '2018-01-09 06:26:19',
                                                                               '5f7bf1ff-c594-42ef-8d95-63cb7893ee95',
                                                                               '2017-12-25 18:04:18',
                                                                               '2018-01-09 06:26:19', NULL,
                                                                               'ZC142731292474916864', 12482508, NULL,
                                                                               1, 1, NULL, 1, 0, 0, 'empty_deduct_uuid',
                                                                                  NULL,
                                                                                  'd2812bc5-5057-4a91-b3fd-9019506f0499',
                                                                                  'd0477d2d6c8c3d2f8403d60074990b42',
                                                                                  'eebf2f4c926f3c347b3dd373aab7959f',
  '2017-12-25 18:04:18', '2017-12-25 18:04:18', 0, 0, 0, 1, 0, 1, 0, '4ec38eda-61a8-47f9-8bab-500dc0d6b6e5',
        'd77a59d4-e44d-499a-99be-a6fa1b1f0c6b', NULL, 'f32fd505-02d8-46a3-84a6-9d330948c5a5', 0,
        'c6cbd1f0ebe0581fb0f3752cd7d070ec', '1514196210772DZZ0');


INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('3171689d-7d82-4198-94d8-63352002158a', '029a7e9a-c894-450b-bdf6-dc72b1191e95', '2017-12-25 11:01:19',
                                                '2017-12-25 11:01:19', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_OTHER_FEE', '10000.05', NULL, NULL, 20.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('61576018-9d75-4ed8-861b-8052e2f4fc8d', '029a7e9a-c894-450b-bdf6-dc72b1191e95', '2017-12-25 11:01:19',
                                                '2017-12-25 11:01:19', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE', '10000.03', NULL, NULL,
                                                20.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('7127b017-176f-4501-bd6c-c2fcfac8e466', '029a7e9a-c894-450b-bdf6-dc72b1191e95', '2017-12-25 11:01:19',
                                                '2017-12-25 11:01:19', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_TECH_FEE', '10000.04', NULL, NULL, 20.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('23e0d3fc-339e-4aed-b000-83b0a3c7a408', '2c3e6cae-1466-4aeb-a804-0a65a65b5d59', '2017-12-25 10:45:31',
                                                '2017-12-25 10:45:31', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_OTHER_FEE', '10000.05', NULL, NULL, 20.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('3790ffbf-43f4-47aa-be4b-4ed3091eb2ee', '2c3e6cae-1466-4aeb-a804-0a65a65b5d59', '2017-12-25 10:45:31',
                                                '2017-12-25 10:45:31', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE', '10000.03', NULL, NULL,
                                                20.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('fa6f6714-eb72-4d87-8cb2-282db4697153', '2c3e6cae-1466-4aeb-a804-0a65a65b5d59', '2017-12-25 10:45:31',
                                                '2017-12-25 10:45:31', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_TECH_FEE', '10000.04', NULL, NULL, 20.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('028769e9-91a3-43cf-8e49-9b454a169a57', '40247e60-af7c-4139-923d-1c0e1bf540a1', '2017-12-25 11:01:19',
                                                '2017-12-25 11:01:19', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_OTHER_FEE', '10000.05', NULL, NULL, 20.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('ae76a8ad-d2af-4175-a24d-0b3344036577', '40247e60-af7c-4139-923d-1c0e1bf540a1', '2017-12-25 11:01:19',
                                                '2017-12-25 11:01:19', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE', '10000.03', NULL, NULL,
                                                20.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('c9854ce7-dddd-46cc-896e-6c4f070a157c', '40247e60-af7c-4139-923d-1c0e1bf540a1', '2017-12-25 11:01:19',
                                                '2017-12-25 11:01:19', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_TECH_FEE', '10000.04', NULL, NULL, 20.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('78f6432a-167c-471c-b30d-6ddbe423e5dd', '592547a3-639d-4536-9f04-1d3dacf718d2', '2017-12-25 10:45:31',
                                                '2017-12-25 10:45:31', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_OTHER_FEE', '10000.05', NULL, NULL, 20.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('4fb8d568-e63a-4937-b21a-17db1a4c8432', '592547a3-639d-4536-9f04-1d3dacf718d2', '2017-12-25 10:45:31',
                                                '2017-12-25 10:45:31', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE', '10000.03', NULL, NULL,
                                                20.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('27fbd5ca-843b-4c1d-9085-8db9dc9e21aa', '592547a3-639d-4536-9f04-1d3dacf718d2', '2017-12-25 10:45:31',
                                                '2017-12-25 10:45:31', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_TECH_FEE', '10000.04', NULL, NULL, 20.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('89dde725-aeab-438d-b946-e7ffbcea1bb3', 'a81205bd-3b59-4160-a62b-18c37544751f', '2017-12-25 10:59:43',
                                                '2017-12-25 10:59:43', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_OTHER_FEE', '10000.05', NULL, NULL, 20.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('f0d63085-19a7-492b-8ea8-be76e0857af5', 'a81205bd-3b59-4160-a62b-18c37544751f', '2017-12-25 10:59:43',
                                                '2017-12-25 10:59:43', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE', '10000.03', NULL, NULL,
                                                20.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('19385f6d-3e74-4050-bd87-9a7be60d7df7', 'a81205bd-3b59-4160-a62b-18c37544751f', '2017-12-25 10:59:43',
                                                '2017-12-25 10:59:43', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_TECH_FEE', '10000.04', NULL, NULL, 20.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('2cdebada-4a85-4538-bfa8-502f7d9babcb', 'adae7eb0-326a-49c2-abc1-6f5d28825df6', '2017-12-25 10:45:30',
                                                '2017-12-25 10:45:30', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_OTHER_FEE', '10000.05', NULL, NULL, 20.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('9ebbc719-abb0-4876-a0e2-c4b30471629d', 'adae7eb0-326a-49c2-abc1-6f5d28825df6', '2017-12-25 10:45:30',
                                                '2017-12-25 10:45:30', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE', '10000.03', NULL, NULL,
                                                20.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('322a0087-7da1-463f-b027-b89aa1519981', 'adae7eb0-326a-49c2-abc1-6f5d28825df6', '2017-12-25 10:45:30',
                                                '2017-12-25 10:45:30', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_TECH_FEE', '10000.04', NULL, NULL, 20.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('8571e5d9-eb25-48d4-a74f-88ea1b708a78', 'b36dab0e-4a39-4d27-9e97-a6a687d6ec6e', '2017-12-25 11:01:19',
                                                '2017-12-25 11:01:19', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_OTHER_FEE', '10000.05', NULL, NULL, 20.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('1de9249e-a998-467f-bf5b-86142dfba589', 'b36dab0e-4a39-4d27-9e97-a6a687d6ec6e', '2017-12-25 11:01:19',
                                                '2017-12-25 11:01:19', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE', '10000.03', NULL, NULL,
                                                20.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('42ae653f-641c-4ec2-8542-207d5bad53dc', 'b36dab0e-4a39-4d27-9e97-a6a687d6ec6e', '2017-12-25 11:01:19',
                                                '2017-12-25 11:01:19', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_TECH_FEE', '10000.04', NULL, NULL, 20.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('71d1ba2e-df7b-4bb9-9991-45f6e1a32be5', 'c81d0e55-6030-40d8-8552-dbf1fe770aa8', '2017-12-25 10:59:43',
                                                '2017-12-25 10:59:43', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_OTHER_FEE', '10000.05', NULL, NULL, 20.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('2738cd92-d1b9-4409-a6b4-ac36dda89e05', 'c81d0e55-6030-40d8-8552-dbf1fe770aa8', '2017-12-25 10:59:43',
                                                '2017-12-25 10:59:43', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE', '10000.03', NULL, NULL,
                                                20.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('4cf2da2e-1af9-464a-9c30-8420161abfc8', 'c81d0e55-6030-40d8-8552-dbf1fe770aa8', '2017-12-25 10:59:43',
                                                '2017-12-25 10:59:43', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_TECH_FEE', '10000.04', NULL, NULL, 20.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('242204a9-0c20-466b-aec9-0d741a10a55a', 'ca024d00-06a2-439c-b334-ea065d41b49f', '2017-12-25 10:59:43',
                                                '2017-12-25 10:59:43', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_OTHER_FEE', '10000.05', NULL, NULL, 20.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('3779b61c-2c13-4b54-bbcf-be0b1e15f1f7', 'ca024d00-06a2-439c-b334-ea065d41b49f', '2017-12-25 10:59:43',
                                                '2017-12-25 10:59:43', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE', '10000.03', NULL, NULL,
                                                20.00);
INSERT INTO asset_set_extra_charge (asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES ('63a25e28-0b27-4fc6-8e7f-577557c0c200', 'ca024d00-06a2-439c-b334-ea065d41b49f', '2017-12-25 10:59:43',
                                                '2017-12-25 10:59:43', 'FST_UNEARNED_LOAN_ASSET', '10000',
                                                'SND_UNEARNED_LOAN_ASSET_TECH_FEE', '10000.04', NULL, NULL, 20.00);