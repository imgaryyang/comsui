SET FOREIGN_KEY_CHECKS=0;

delete from `asset_set`;
delete from `journal_voucher`;
delete from `source_document`;
delete from `t_voucher`;
DELETE FROM `financial_contract`;

DELETE FROM `app`;
DELETE FROM `company`;
DELETE FROM `customer`;
DELETE FROM `account`;
DELETE FROM `repurchase_doc`;
DELETE FROM `contract`;
DELETE FROM `ledger_book`;
DELETE FROM `ledger_book_shelf`;
DELETE FROM `t_deduct_plan`;


INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`) 
VALUES 
('1', '上海', '云南国际信托有限公司', '云南信托');

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`,`customer_uuid`,`customer_type`) VALUES 
(1, NULL, NULL, NULL, NULL, 1,'customerUuid1',0),
(2, NULL, NULL, NULL, NULL, 1,'company_customer_uuid_1',1);

INSERT INTO `financial_contract` (`adva_repayment_term`,`id`,`financial_contract_uuid`,`capital_account_id`, `adva_matuterm`, `contract_no`, `contract_name`, `app_id`, `company_id`,   `adva_repo_term`,`loan_overdue_end_day`,`loan_overdue_start_day`,`payment_channel_id`,`ledger_book_no`,`asset_package_format`,`financial_contract_type`) VALUES 
('0','1', 'financial_contract_uuid_1','1', 3, 'DCF-NFQ-LR903A', '云南信托', 1, 1, 30,90,1,1,'yunxin_ledger_book',1,0);

INSERT INTO `account` (`id`, `account_name`, `account_no`, `bank_name`, `alias`, `attr`, `scan_cash_flow_switch`, `usb_key_configured`, `uuid`, `bank_code`) VALUES 
('1', '1212', '1212', '12', NULL, NULL, '\0', '\0', '8b6b6ea0-36b0-11e6-9a40-b083fe8d3abe', NULL);


INSERT INTO `contract` (`id`, `uuid`,`unique_id`,`financial_contract_uuid`,`begin_date`, `contract_no`,`end_date`,`app_id`, `customer_id`, `house_id`,`actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`,`active_version_no`) VALUES 
(1, 'contract_uuid_2','contract_unique_id_2','financial_contract_uuid_1','2015-10-19', 'contract_no', '2016-10-19', 1, 1, 1, NULL, '2015-10-19 13:34:35', 0.001, 1, 1, 12, 0, 1000, 0.0001,1);


INSERT INTO `repurchase_doc` (`id`, `repurchase_doc_uuid`, `financial_contract_uuid`, `amount`, `repo_start_date`, `repo_end_date`, `repo_days`, `creat_time`, `verification_time`, `last_modifed_time`, `contract_id`, `contract_no`, `app_id`, `app_name`, `customer_uuid`, `customer_name`, `executing_asset_set_uuids`, `asset_set_uuids`, `repurchase_status`, `repurchase_algorithm`, `repurchase_approach`, `repurchase_rule`, `day_of_month`, `adva_repo_term`, `repurchase_periods`, `amount_detail`, `repurchase_principal`, `repurchase_principal_algorithm`, `repurchase_interest`, `repurchase_interest_algorithm`, `repurchase_penalty`, `repurchase_penalty_algorithm`, `repurchase_other_charges`, `repurchase_other_charges_algorithm`, `repurchase_rule_detail`) VALUES 
('1', 'repurchase_doc_uuid', 'financial_contract_uuid_1', '500.00', '2016-11-24', '2016-11-25', '1', '2016-11-24 15:54:33', '2016-11-24 16:11:38', '2016-11-24 16:11:38', '1', 'contract_no', '3', '测试商户ppd', 'ed204958-6a7f-47b8-bb24-b750bae67a6d', '测试员1', NULL, '[\"fe030786-e187-4973-836e-c5a23e81a02c\"]', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '500.00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee` )
VALUES 
('1', 'anmeitu', '11111db75ebb24fa0993f4fa25775023', '\0', 'http://e.zufangbao.cn', '安美途', '1', NULL);

INSERT INTO `ledger_book` (`id`, `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`) VALUES 
('1', 'yunxin_ledger_book', '1', '1', '');


INSERT INTO `ledger_book_shelf` (`id`, `ledger_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `account_side`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `forward_ledger_uuid`, `backward_ledger_uuid`, `batch_serial_uuid`, `amortized_date`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `default_date`, `journal_voucher_uuid`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `source_document_uuid`) VALUES 

('6', 'a73569e3-6271-4028-baa3-e9def7c843d1', '900.00', '0.00', 'FST_BANK_SAVING', '60000', '1', '600000000001', '60000.600000000001', 'TRD_BANK_SAVING_GENERAL_PRINCIPAL', '60000.1000.01', '14', 'customerUuid1', NULL, NULL, NULL, 'b18831e1-3ab7-46a4-8005-7698814440e5', '2015-10-19', '2016-09-13 13:24:23', '', NULL, '1', NULL, '2015-10-19 00:00:00', 'journal_voucher_uuid_1', 'yunxin_ledger_book', '14', '1', 'DKHD-001-01', 'asset_uuid_1', NULL, NULL, NULL, NULL, ''),
('7', '0e49b608-55b7-4375-957d-4a9f3172ac7a', '100.00', '0.00', 'FST_BANK_SAVING', '60000', '1', '600000000001', '60000.600000000001', 'TRD_BANK_SAVING_GENERAL_INTEREST', '60000.1000.02', '14', 'customerUuid1', NULL, NULL, NULL, 'b18831e1-3ab7-46a4-8005-7698814440e5', '2015-10-19', '2016-09-13 13:24:23', '', NULL, '1', NULL, '2015-10-19 00:00:00', 'journal_voucher_uuid_1', 'yunxin_ledger_book', '14', '1', 'DKHD-001-01', 'asset_uuid_1', NULL, NULL, NULL, NULL, ''),
('5', '0e49b608-55b7-4375-957d-4a9f3172ac74', '200.00', '0.00', 'FST_BANK_SAVING', '60000', '1', '600000000001', '60000.600000000001', 'TRD_BANK_SAVING_GENERAL_PRINCIPAL', '60000.1000.01', '14', 'customerUuid1', NULL, NULL, NULL, 'b18831e1-3ab7-46a4-8005-7698814440e5', '2015-10-19', '2016-09-13 13:24:23', '', NULL, '1', NULL, '2015-10-19 00:00:00', 'journal_voucher_uuid_3', 'yunxin_ledger_book', '14', '1', 'DKHD-001-01', 'asset_uuid_2', NULL, NULL, NULL, NULL, ''),
('8', '9bb36c8e-ca27-4a9d-a376-23eaf503b389', '500.00', '0.00', 'FST_BANK_SAVING', '60000', '1', 'SND_BANK_SAVING_GENERAL', '60000.1000', 'TRD_BANK_SAVING_GENERAL_REPURCHASE_ASSET_PRINCIPLE', '60000.1000.14', '14', 'customerUuid1', NULL, NULL, NULL, 'b18831e1-3ab7-46a4-8005-7698814440e5', '2015-10-19', '2016-09-13 13:24:23', '', NULL, '1', 'contract_uuid_2', '2015-10-19 00:00:00', 'journal_voucher_uuid_4', 'yunxin_ledger_book', '14', '1', 'DKHD-001-01', '', NULL, NULL, NULL, NULL, '');




INSERT INTO `asset_set` (`id`, `actual_recycle_date`, `asset_fair_value`, `asset_initial_value`, `asset_interest_value`, `asset_principal_value`, `asset_recycle_date`, `asset_status`, `asset_uuid`, `create_time`, `current_period`, `guarantee_status`, `last_modified_time`, `last_valuation_time`, `on_account_status`, `settlement_status`, `single_loan_contract_no`, `contract_id`,`version_no`,`active_status`,`financial_contract_uuid`,`plan_type`,`can_be_rollbacked`,`version_lock`,`order_payment_status`) VALUES 
('1', NULL, '1000.00', '1000.00', '100.00', '900.00', '2016-05-01', '1', 'asset_uuid_1', '2016-05-16 14:26:50', '1', '0', NULL, NULL, '1', '0', 'repayment_plan_no_1', '1',1,0,'financial_contract_uuid_1','0','1','111','1'),
('2', NULL, '200.00', '200.00', '200.00', '200.00', '2016-05-05', '1', 'asset_uuid_2', '2016-05-16 14:26:50', '1', '0', NULL, NULL, '1', '0', 'repayment_plan_no_2', '1',1,0,'financial_contract_uuid_1','0','1','222','0');



INSERT INTO `journal_voucher` (`id`, `account_side`, `billing_plan_uuid`, `booking_amount`, `checking_level`, `company_id`,  `journal_voucher_uuid`, `source_document_amount`, `source_document_identity`, `source_document_uuid`, `status`, `source_document_counter_party_account`, `source_document_counter_party_name`, `issued_time`, `journal_voucher_type`, `counter_account_type`, `related_bill_contract_info_lv_1`, `related_bill_contract_info_lv_2`, `related_bill_contract_info_lv_3`, `cash_flow_account_info`, `journal_voucher_no`, `related_bill_contract_no_lv_1`, `related_bill_contract_no_lv_2`, `related_bill_contract_no_lv_3`, `related_bill_contract_no_lv_4`, `source_document_no`, `appendix`, `last_modified_time`,`trade_time`,`cash_flow_time`,`second_journal_voucher_type`) VALUES
('1', '1', 'asset_uuid_1', '1000.00', '0', '1', 'journal_voucher_uuid_1', '1000.00','source_document_uuid_1', 'source_document_detail_uuid_1', '1','','', '2016-10-20 16:57:12', '7', '1', 'financial_contract_uuid_1', 'contract_uuid_2', 'asset_uuid_1', '', 'ZF274FC9E08F5A715B', '云南信托', 'DKHD-001', 'repayment_plan_no_1', 'order_no_1', 'source_document_no', '{"clearing_time":"2017-06-13 16:57:12"}', '2016-10-20 16:57:12','2017-06-08 18:13:42','2017-06-13 16:57:12',0),
--('2', '1', 'asset_uuid_1', '100.00', '0', '1', 'journal_voucher_uuid_2', '100.00','source_document_uuid_1', 'source_document_detail_uuid_1', '1','','', '2016-10-20 16:57:12', '7', '1', 'financial_contract_uuid_1', 'contract_uuid_2', 'asset_uuid_1', '', 'ZF274FC9E08F5A715B', '云南信托', 'DKHD-001', 'repayment_plan_no_1', 'order_no_1', 'source_document_no', NULL, '2016-10-20 16:57:12'),
('3', '1', 'asset_uuid_2', '200.00', '0', '1', 'journal_voucher_uuid_3', '200.00','source_document_uuid_1', 'source_document_detail_uuid_1', '1','','', '2016-10-20 16:57:12', '7', '1', 'financial_contract_uuid_1', 'contract_uuid_2', 'asset_uuid_2', '', 'ZF274FC9E08F5A715B', '云南信托', 'DKHD-001', 'repayment_plan_no_1', 'order_no_1', 'source_document_no', '{"clearing_time":"2017-06-12 16:57:12"}', '2016-10-20 16:57:12','2017-06-09 18:13:42','2017-06-12 16:57:12',2),
('4', '1', 'repurchase_doc_uuid', '500.00', '0', '1', 'journal_voucher_uuid_4', '500.00','source_document_uuid_1', 'source_document_detail_uuid_1', '1','','', '2016-10-20 16:57:12', '10', '1', 'financial_contract_uuid_1', 'contract_uuid_2', 'repurchase_doc_uuid', '', 'ZF274FC9E08F5A715B', '云南信托', 'DKHD-001', 'repayment_plan_no_1', 'order_no_1', 'source_document_no', '', '2016-10-20 16:57:12','2017-06-10 18:13:42','2017-06-10 18:13:42',NULL);


INSERT INTO `source_document` (`id`, `company_id`, `source_document_uuid`,`voucher_uuid`, `source_document_type`, `create_time`, `issued_time`, `source_document_status`, `source_account_side`, `booking_amount`, `outlier_document_uuid`, `outlier_trade_time`, `outlier_counter_party_account`, `outlier_counter_party_name`, `outlier_account`, `outlie_account_name`, `outlier_account_id`, `outlier_company_id`, `outlier_serial_global_identity`, `outlier_memo`, `outlier_amount`, `outlier_settlement_modes`, `outlier_breif`, `outlier_account_side`, `appendix`, `first_outlier_doc_type`, `second_outlier_doc_type`, `third_outlier_doc_type`, `currency_type`, `audit_status`, `first_party_id`, `second_party_id`, `virtual_account_uuid`, `first_account_id`, `second_account_id`, `third_account_id`, `excute_status`, `excute_result`, `related_contract_uuid`, `financial_contract_uuid`, `source_document_no`, `first_party_type`, `first_party_name`, `virtual_account_no`, `last_modified_time`)
VALUES ('1', '1', 'source_document_uuid_1', 'voucher_uuid', '1', '2016-11-30 17:59:39', NULL, '1', '1', '1.00', '5d16391a-9b6b-49e2-8b98-612191afc5c5', '2016-11-30 17:57:43', '12312893521892130', '测试员1', '3', '1', '5', '1', '0d345da6-c171-4c6e-9665-c5e7fbc4e3f0', '交易成功', '1.00', '3', '[\"ZC2756A9C9B814DF18\"]', '1', NULL, 'deduct_application', 'interface_on_line_payment', NULL, NULL, '0', 'c3e6b592-6f9c-11e6-b776-58d41e171d4a', '8ac336c4-ddd3-4768-b8b5-04587ffa8aca', NULL, '5d16391a-9b6b-49e2-8b98-612191afc5c5', 'deduct_plan_uuid', NULL, NULL, NULL, '04c3b2e5-8d52-4b4e-92a1-5c5282ee4a36', 'beb90aa6-5cba-4535-b783-57f0801ed7c0', 'KK2756AA7C9CD65737', NULL, '测试金融', NULL, '2016-11-30 17:59:41'),
       ('2', '1', '04c0add8-94cb-49a9-8d61-25fc1eb11cfe', '7f1342f4-1d1b-4dc0-8e1e-d17b2a64aacc', '1', '2016-11-30 17:59:39', NULL, '1', '1', '1.00', 'ba81d90c-8d78-4161-83c2-32a4dc943515', '2016-11-30 17:57:44', '12312893521892130', '测试员1', '3', '1', '5', '1', '99da9019-ef5c-426a-8a60-89b5c4bf65a7', '交易成功', '1.00', '3', '[\"ZC2756A9C9BE801C5C\"]', '1', NULL, 'deduct_application', 'interface_on_line_payment', NULL, NULL, '0', 'c3e6b592-6f9c-11e6-b776-58d41e171d4a', 'd255de0e-f8b9-4623-98ed-3a8712e7fc60', NULL, 'ba81d90c-8d78-4161-83c2-32a4dc943515', 'c9c07522-3608-4d6d-b0d2-a3b18eb56ab8', NULL, NULL, NULL, '06e71ad7-985b-454e-bfb4-0bcda42d50ed', 'beb90aa6-5cba-4535-b783-57f0801ed7c0', 'KK2756AA7C9DC15C1A', NULL, '测试金融', NULL, '2016-11-30 17:59:41'),
       ('3', '1', '02706184-ebe4-4954-b1bf-7bb31dcc260f', '4a34d8f4-a8df-4b5a-90f1-ad11bbae8f7d', '1', '2016-11-30 17:59:39', NULL, '1', '1', '1.00', 'ed984771-1926-4403-b360-675b037bc7e9', '2016-11-30 17:57:44', '12312893521892130', '测试员1', '3', '1', '5', '1', 'b9ded4b2-4e57-4baa-bf41-825455466a3f', '交易成功', '1.00', '3', '[\"ZC2756A9C9BFAE0C2E\"]', '1', NULL, 'deduct_application', 'interface_on_line_payment', NULL, NULL, '0', 'c3e6b592-6f9c-11e6-b776-58d41e171d4a', '5607e0d3-7268-48b9-aa2c-82d342e40e90', NULL, 'ed984771-1926-4403-b360-675b037bc7e9', '0001211a-f114-4135-a8a4-8dd5669831d6', NULL, NULL, NULL, '33164b16-b811-49ce-82ac-a838d68d3555', 'beb90aa6-5cba-4535-b783-57f0801ed7c0', 'KK2756AA7C9E2A94D3', NULL, '测试金融', NULL, '2016-11-30 17:59:42');

INSERT INTO `t_voucher` (`id`, `uuid`, `voucher_no`, `source_document_uuid`, `financial_contract_uuid`, `amount`, `status`, `first_type`, `first_no`, `second_type`, `second_no`, `receivable_account_no`, `payment_account_no`, `payment_name`, `payment_bank`, `check_state`, `comment`, `create_time`, `contract_no`, `last_modified_time`)
VALUES ('1', 'voucher_uuid', 'V14804964998590464', 'aa38d31a-53e9-4496-8784-08fb0e24fd0d', 'beb90aa6-5cba-4535-b783-57f0801ed7c0', '4001.00', '1', 'enum.voucher-source.active_payment_voucher', 'b114e6e1-68df-4c24-92e5-d2e445feca71', 'enum.voucher-type.active_pay', '0d345da6-c171-4c6e-9665-c5e7fbc4e3f0', NULL, '2d86a2cb-95a3-4ca9-9961-76c94fb76fbc', '测试员3001', '中国工商银行 ', '2', NULL, '2017-01-06 17:50:44', NULL, NULL),
       ('2', '7f1342f4-1d1b-4dc0-8e1e-d17b2a64aacc', 'V14813637425836032', '04c0add8-94cb-49a9-8d61-25fc1eb11cfe', 'beb90aa6-5cba-4535-b783-57f0801ed7c0', '4601.00', '1', 'enum.voucher-source.active_payment_voucher', '5d7280d7-74e2-469a-a0d8-eac48d62db01', 'enum.voucher-type.active_pay', 'ksjdalksjdl222ksajdl3kasjdlk', NULL, '04f9c951-675e-46b2-b2b3-4dbbdabd6de6', '测试员3601', '中国工商银行 ', '2', NULL, '2017-01-06 18:25:12', NULL, NULL),
       ('3', '4a34d8f4-a8df-4b5a-90f1-ad11bbae8f7d', 'V14820477835620352', '02706184-ebe4-4954-b1bf-7bb31dcc260f', 'beb90aa6-5cba-4535-b783-57f0801ed7c0', '5001.00', '1', 'enum.voucher-source.active_payment_voucher', 'f0d286da-dd7b-49fa-89e1-b89e669bb994', 'enum.voucher-type.active_pay', 'ksjdalksjdl222ksajdl3kasjdlk', NULL, '7e942827-57f6-4b70-8b62-aa748af39b00', '测试员4001', '中国工商银行 ', '2', NULL, '2017-01-06 18:52:23', NULL, NULL);

       
INSERT INTO `t_deduct_plan` (`id`, `deduct_plan_uuid`, `deduct_application_uuid`, `deduct_application_detail_uuid`, `financial_contract_uuid`, `contract_unique_id`, `contract_no`, `payment_gateway`, `payment_channel_uuid`, `pg_account`, `pg_clearing_account`, `transaction_type`, `transaction_remark`, `cp_bank_code`, `cp_bank_card_no`, `cp_bank_account_holder`, `cp_id_type`, `cp_id_number`, `cp_bank_province`, `cp_bank_city`, `cp_bank_name`, `planned_payment_date`, `complete_payment_date`, `planned_total_amount`, `actual_total_amount`, `execution_precond`, `execution_status`, `execution_remark`, `create_time`, `creator_name`, `last_modified_time`, `transaction_serial_no`, `mobile`, `transaction_recipient`, `trade_uuid`, `repayment_type`, `source_type`, `complete_time`, `transaction_time`, `batch_deduct_application_uuid`, `batch_deduct_id`, `none_business_check_status`, `business_check_status`, `notify_status`, `retriable`, `retry_times`, `payment_order_uuid`, `version`, `clearing_status`, `clearing_cash_flow_uuid`, `clearing_time`) VALUES 
('12', 'deduct_plan_uuid', '0209b346-d781-4c4d-8fb2-024b185dbe05', NULL, 'db36ecc9-d80c-4350-bd0d-59b1139d550d', NULL, '2c1ebc2a-a6ce-4709-a20f-b4d6b56460a2', '0', 'f8bb9956-1952-4893-98c8-66683d25d7ce', 't_test_zfb', NULL, '1', NULL, NULL, '23456787654323456', '郑航波', '0', '330683199403062411', '330000', '110100', '中国工商银行 ', NULL, NULL, '20.00', '0.00', NULL, '3', '系统繁忙，请稍后再试', '2016-08-30 17:30:42', 't_test_zfb', '2016-08-30 17:30:42', NULL, NULL, '0', NULL, '0', '1', '2016-08-30 17:30:42', '2016-08-30 17:30:42', NULL, NULL, '0', '0', '0', '0', '0', NULL, NULL, '0', NULL, NULL);
       
       
SET FOREIGN_KEY_CHECKS=1;