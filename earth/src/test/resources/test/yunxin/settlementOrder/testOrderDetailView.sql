SET FOREIGN_KEY_CHECKS=0;

delete from `asset_package`;
delete from `rent_order`;
delete from `asset_set`;
delete from `financial_contract`;
delete from `contract`;
delete from `app`;
delete from `customer`;
delete from `contract_account`;
delete from `asset_valuation_detail`;
delete from `transfer_application`;
delete from `batch_pay_record`;
delete from `company`;
delete from `t_deduct_application_detail`;
delete from `t_deduct_application`;
delete from `t_deduct_plan`;
delete from `journal_voucher`;

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`)
VALUES
	(1, '上海', '测试金融公司', '测试金融', 'a02c02b9-6f98-11e6-bf08-00163e002839'),
	(2, '杭州', 'yqb', 'yqb', 'a02c08b5-6f98-11e6-bf08-00163e002839');

INSERT INTO `asset_package` (`id`, `create_time`, `contract_id`, `asset_package_no`, `financial_contract_id`) VALUES 
('1', '2015-10-19 13:34:35', '1','no1','1'),
('2', '2015-10-19 13:34:35', '2','no2','2');

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`) VALUES 
('1', 'anmeitu', '11111db75ebb24fa0993f4fa25775023', '\0', 'http://e.zufangbao.cn', '安美途', '1', NULL),
('2', 'test4Zufangbao', '2e85ae999845f25faf8ea7b514ee0aca', '\0', 'http://e.zufangbao.cn', '租房宝测试账号', '2', NULL);

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`) VALUES 
('1', '01091081XXXXX', '13777846666', '王二', 'D001', '1'),
('2', '01091082XXXXX', '13777846666', '王二', 'D002', '1');



INSERT INTO `financial_contract` (`adva_repayment_term`,`id`, `adva_matuterm`, `contract_no`, `app_id`, `company_id`,   `adva_repo_term`) VALUES 
('0','1', 3, 'YX_AMT_001', 1, 1, 30),
('0','2', 3, 'YX_AMT_002', 2, 1, 30);


INSERT INTO `asset_set` (`id`, `asset_fair_value`,`asset_initial_value`, `asset_recycle_date`, `asset_status`, `asset_uuid`, `create_time`, `last_modified_time`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `version_no`) VALUES 
('1', 1010,1000, '2016-10-10', 0, 'asset_uuid_1', '2015-10-19 13:34:35', '2015-10-19 13:34:35', 'DKHD-001-01', 1, '2015-10-19 13:34:35',1),
('2', 1000,1000, '2016-10-10', 0, 'asset_uuid_2', '2015-10-20 13:34:35', '2015-10-20 13:34:35', 'DKHD-001-02', 1, '2015-10-19 13:34:35',1),
('3', 1000,1000, '2016-11-11', 0, 'asset_uuid_3', '2015-10-20 13:34:35', '2015-10-20 13:34:35', 'DKHD-002-01', 2, '2015-10-19 13:34:35',1),
('4', 1000,1000, '2016-11-11', 0, 'asset_uuid_4', '2015-10-20 13:34:35', '2015-10-20 13:34:35', 'DKHD-002-02', 2, '2015-10-19 13:34:35',1);

INSERT INTO `contract` (`id`, `begin_date`, `contract_no`,`end_date`,`app_id`, `customer_id`, `house_id`,`actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`,`month_fee`,`active_version_no`) VALUES 
(1, '2015-10-19', 'DKHD-001', '2016-10-19', 1, 1, 1, NULL, '2015-10-19 13:34:35', 0.001, 1, 1, 12, 0, 10000, 0.0001,1000,1),
(2, '2015-10-20', 'DKHD-002', '2016-10-19', 2, 1, 1, NULL, '2015-10-19 13:34:35', 0.001, 1, 1, 12, 0, 10000, 0.0001,1000,1);

INSERT INTO `contract_account` (`id`, `pay_ac_no`, `bankcard_type`, `contract_id`, `payer_name`, `bank`, `bind_id`, `id_card_num`, `bank_code`, `province`, `city`, `from_date`, `thru_date`)
VALUES 
(1, 'pay_ac_no_1', 0, 1, 'payer_name_1',NUll, 'bind_id',NULL,NULL,NULL,NULL,'2016-04-17 00:00:00', '2900-01-01 00:00:00');


INSERT INTO `rent_order` (`id`, `due_date`, `order_no`, `payout_time`, `total_rent`, `customer_id`, `user_upload_param`, `modify_time`, `asset_set_id`, `create_time`,`clearing_status`,`executing_settling_status`,`financial_contract_id`) VALUES
(1, '2016-10-09', 'DKHD-001-01-20160307', '2016-10-19 13:34:35',1000, '1','', '2016-01-19', 1, '2015-10-19', 0,0,1);

INSERT INTO `transfer_application` (`id`,`transfer_application_no`, `amount`,`creator_id`, `batch_pay_record_id`, `comment`, `create_time` , `deduct_status`, `executing_deduct_status`, `deduct_time`, `last_modified_time`,  `contract_account_id`,`order_id` ) VALUES 
(1,'transfer_application_no_1', '1000', NULL, 1, NULL, '2015-10-10 13:34:35', 0, 1, NULL, NULL, 1,1),
(2,'transfer_application_no_2', '1000', NULL, 1, NULL, '2015-10-10 15:00:00', 0, 1, NULL, NULL, 1,1);

INSERT INTO `batch_pay_record` (`id`, `amount`, `modify_time`, `request_no`, `serial_no`, `request_data`, `response_data`, `query_response_data`, `trans_date_time`) VALUES 
('1', '10.00', NULL, 'test', 'serial_no_1', NULL, '', '', '20160323111100');

INSERT INTO `asset_valuation_detail` (`id`, `amount`, `asset_value_date`, `created_date`, `subject`, `asset_set_id`) VALUES 
('1', '1000.00', '2016-03-14 19:46:38', '2016-03-14', '0', '1'),
('2', '10.00', '2016-03-15 19:47:16', '2016-03-15', '1', '1'),
('3', '1000.00', '2016-03-14 19:46:38', '2016-03-14', '0', '2');

INSERT INTO `t_deduct_application_detail` (`id`, `deduct_application_detail_uuid`, `deduct_application_uuid`, `financial_contract_uuid`, `contract_unique_id`, `repayment_plan_code`, `asset_set_uuid`, `request_no`, `repayment_type`, `transaction_type`, `create_time`, `execution_remark`, `creator_name`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`, `is_total`) 
VALUES 
('1', '122881e1-1df1-4622-bc1d-da9f9587d667', '9d13d98f-6b5a-47f0-ae0c-537bd54afcc1', '63b7e281-d5f7-4f6e-9369-2a1d5ff8a148', NULL, 'DKHD-001-01', NULL, '2445ef09-f220-4e52-abe2-ec69aa6fbcaf', '0', '1', '2016-08-24 17:21:00', '', 't_test_zfb', '2016-08-24 17:21:00', 'FST_RECIEVABLE_ASSET', '20000', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_PRINCIPLE', '20000.01.01', '100.00', '1'),
('2', '122881e1-1df1-4622-bc1d-da9f9587d667', '9d13d98f-6b5a-47f0-ae0c-537bd54afcc2', '63b7e281-d5f7-4f6e-9369-2a1d5ff8a148', NULL, 'DKHD-001-01', NULL, '2445ef09-f220-4e52-abe2-ec69aa6fbcaf', '0', '1', '2016-08-24 17:21:00', '', 't_test_zfb', '2016-08-24 17:21:00', 'FST_RECIEVABLE_ASSET', '20000', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_INTEREST', '20000.01.02', '0.00', '1');

INSERT INTO `t_deduct_application` (`id`, `deduct_application_uuid`, `deduct_id`, `request_no`, `financial_contract_uuid`, `financial_product_code`, `contract_unique_id`, `repayment_plan_code_list`, `contract_no`, `planned_deduct_total_amount`, `actual_deduct_total_amount`, `notify_url`, `transcation_type`, `repayment_type`, `execution_status`, `execution_remark`, `create_time`, `creator_name`, `ip`, `last_modified_time`, `record_status`, `is_available`, `api_called_time`, `transaction_recipient`, `customer_name`, `mobile`, `gateway`, `source_type`, `third_part_voucher_status`, `complete_time`)
VALUES 
('1', '9d13d98f-6b5a-47f0-ae0c-537bd54afcc1', '94d1f220-21e6-4642-a681-d0f1816a0d20', '2445ef09-f220-4e52-abe2-ec69aa6fbcaf', '63b7e281-d5f7-4f6e-9369-2a1d5ff8a148', 'G00001', NULL, NULL, 'contractNo1', '100.00', '0.00', '', '1', '0', '3', '放款请求受理失败!', '2016-08-24 17:21:00', 't_test_zfb', '115.197.180.125', '2016-08-24 17:21:00', '2', '0', '2016-08-04 00:00:00', NULL, NULL, NULL, NULL, '1', '1', '2016-08-24 17:21:00'),
('2', '9d13d98f-6b5a-47f0-ae0c-537bd54afcc2', 'c906d969-4654-4972-b284-40cb93331ade', 'c906d969-4654-4972-b284-40cb93331ade', 'db36ecc9-d80c-4350-bd0d-59b1139d550d', 'G00003', '4c05b1ea-fc25-47eb-9c76-dcabd0271e1e', NULL, 'G00003(zht934582950106399250)', '0.02', '0.00', '', '1', '0', '5', '', '2016-08-26 00:03:58', 't_merchant', '101.231.215.146', '2016-08-26 22:20:27', '2', '0', '2016-08-26 00:00:00', NULL, NULL, NULL, NULL, '1', '1', NULL),
('3', '9d13d98f-6b5a-47f0-ae0c-537bd54afcc3', 'fe98397b-fd90-438e-9d73-5c8faf43834e', 'fe98397b-fd90-438e-9d73-5c8faf43834e', 'db36ecc9-d80c-4350-bd0d-59b1139d550d', 'G00003', '4c05b1ea-fc25-47eb-9c76-dcabd0271e1e', NULL, 'G00003(zht934582950106399250)', '0.02', '0.00', '', '1', '0', '5', '', '2016-08-26 00:13:37', 't_merchant', '101.231.215.146', '2016-08-26 22:20:27', '2', '0', '2016-08-26 00:00:00', NULL, NULL, NULL, NULL, '1', '1', NULL);

INSERT INTO `t_deduct_plan` (`id`, `deduct_plan_uuid`, `deduct_application_uuid`, `deduct_application_detail_uuid`, `financial_contract_uuid`, `contract_unique_id`, `contract_no`, `payment_gateway`, `payment_channel_uuid`, `pg_account`, `pg_clearing_account`, `transaction_type`, `transaction_remark`, `cp_bank_code`, `cp_bank_card_no`, `cp_bank_account_holder`, `cp_id_type`, `cp_id_number`, `cp_bank_province`, `cp_bank_city`, `cp_bank_name`, `planned_payment_date`, `complete_payment_date`, `planned_total_amount`, `actual_total_amount`, `execution_precond`, `execution_status`, `execution_remark`, `create_time`, `creator_name`, `last_modified_time`, `transaction_serial_no`, `mobile`, `transaction_recipient`, `trade_uuid`, `repayment_type`, `source_type`, `complete_time`)
VALUES 
('1', '304b26c6-de3e-4f65-bd21-57dc3bd87dc3', '9d13d98f-6b5a-47f0-ae0c-537bd54afcc1', NULL, '63b7e281-d5f7-4f6e-9369-2a1d5ff8a148', NULL, 'contractNo1', NULL, 'debit_channel_test_uuid', NULL, NULL, '1', NULL, NULL, '23456787654323456', '郑航波', '0', '330683199403062411', '330000', '110100', '中国工商银行 ', NULL, NULL, '100.00', '0.00', NULL, '3', '放款请求受理失败!', '2016-08-24 17:21:00', 't_test_zfb', '2016-08-24 17:21:00', NULL, NULL, '0', NULL, NULL, '1', '2016-08-24 17:21:00'),
('2', 'ffbe8900-ed02-446f-b2f8-7a7491cee144', '4d5f042e-7030-4911-92b5-02a2e9a76b0f', NULL, 'db36ecc9-d80c-4350-bd0d-59b1139d550d', '4c05b1ea-fc25-47eb-9c76-dcabd0271e1e', '', NULL, 'f8bb9956-1952-4893-98c8-66683d25d7ce', NULL, NULL, '1', NULL, NULL, '6217001210075327591', '韩方园', '0', '410402198801115658', '310000', '310100', '中国建设银行 ', NULL, NULL, '0.02', '0.00', NULL, '3', '无法查询到该交易！', '2016-08-26 00:03:58', 't_merchant', '2016-08-26 22:20:27', NULL, NULL, '0', NULL, NULL, '1', '2016-08-26 22:20:27'),
('3', 'f95f685d-93ea-4f60-a1d3-b782f56a8925', '12eeb545-c96f-416d-b904-ed70ece1ea0c', NULL, 'db36ecc9-d80c-4350-bd0d-59b1139d550d', '4c05b1ea-fc25-47eb-9c76-dcabd0271e1e', '', NULL, 'f8bb9956-1952-4893-98c8-66683d25d7ce', NULL, NULL, '1', NULL, NULL, '6217001210075327591', '韩方园', '0', '410402198801115658', '310000', '310100', '中国建设银行 ', NULL, NULL, '0.02', '0.00', NULL, '3', '无法查询到该交易！', '2016-08-26 00:13:37', 't_merchant', '2016-08-26 22:20:27', NULL, NULL, '0', NULL, NULL, '1', '2016-08-26 22:20:27');

INSERT INTO `journal_voucher` (`id`, `account_side`, `bank_identity`, `billing_plan_uuid`, `booking_amount`, `business_voucher_type_uuid`, `business_voucher_uuid`, `cash_flow_amount`, `cash_flow_breif`, `cash_flow_channel_type`, `cash_flow_serial_no`, `cash_flow_uuid`, `checking_level`, `company_id`, `completeness`, `counter_party_account`, `counter_party_name`, `journal_voucher_uuid`, `notification_identity`, `notification_memo`, `notification_record_uuid`, `notified_date`, `settlement_modes`, `source_document_amount`, `source_document_breif`, `source_document_cash_flow_serial_no`, `source_document_counter_party_uuid`, `source_document_identity`, `source_document_uuid`, `status`, `trade_time`, `batch_uuid`, `created_date`, `source_document_counter_party_account`, `source_document_counter_party_name`, `issued_time`, `journal_voucher_type`, `counter_account_type`, `related_bill_contract_info_lv_1`, `related_bill_contract_info_lv_2`, `related_bill_contract_info_lv_3`, `cash_flow_account_info`, `journal_voucher_no`, `related_bill_contract_no_lv_1`, `related_bill_contract_no_lv_2`, `related_bill_contract_no_lv_3`, `related_bill_contract_no_lv_4`, `source_document_no`, `appendix`, `last_modified_time`, `source_document_local_party_account`, `source_document_local_party_name`, `local_party_account`, `local_party_name`, `second_journal_voucher_type`, `third_journal_voucher_type`)
VALUES 
('1', '1', NULL, 'fa80d206251042bf856d9d426e197068', '2406.00', 'ef8d1fbd-3bcc-42a1-b021-912133d76744', '42a4324b-cea6-41c0-b8eb-fadab79b7963', NULL, NULL, NULL, NULL, NULL, '1', '1', '0', NULL, NULL, '60569f74-f874-47b1-819c-7d7316c8c837', NULL, NULL, NULL, '2016-05-27 18:29:50', '3', '2406.00', '', '2730FAE730E7B3C6', NULL, 'ed0cd216d03b4a889d023ac20a46880e', 'df937f9856b9436baf915b1f873129a6', '1', '2016-05-27 18:29:50', NULL, '2016-05-27 18:32:36', '6217000000000000000', '测试用户18', NULL, '3', NULL, NULL, NULL, NULL, NULL, '8d1fce0d-80c7-11e6-b7d3-00163e002839', NULL, NULL, NULL, 'DKHD-001-01-20160307', NULL, NULL, '2016-05-27 18:32:36', NULL, NULL, NULL, NULL, NULL, NULL),
('2', '1', NULL, '8af40bb20dc949369c055bdbe86c0352', '1804.50', 'ef8d1fbd-3bcc-42a1-b021-912133d76744', '77a7edc7-9d17-4cc7-abe2-dd06a70df5b1', NULL, NULL, NULL, NULL, NULL, '1', '1', '0', NULL, NULL, '8250f480-e255-4f9d-8933-e3680b9956f4', NULL, NULL, NULL, '2016-05-27 18:29:53', '3', '1804.50', '', '2730FAE730EC7B52', NULL, '883992639dc04cb8b596f26a187d52a3', '554eb836c238421aade748501184b724', '1', '2016-05-27 18:29:53', NULL, '2016-05-27 18:32:36', '6217000000000000000', '测试用户19', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '8d1fd1b3-80c7-11e6-b7d3-00163e002835', NULL, NULL, NULL, 'DKHD-001-01-20160307', NULL, NULL, '2016-05-27 18:32:36', NULL, NULL, NULL, NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS=1;
