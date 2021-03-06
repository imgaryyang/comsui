SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM `company`;
DELETE FROM `rent_order`;
DELETE FROM `asset_set`;
DELETE FROM `financial_contract`;
DELETE FROM `account`;
DELETE FROM `customer`;
DELETE FROM `contract`;
DELETE FROM `asset_valuation_detail`;
DELETE FROM `contract_account`;
DELETE FROM `asset_package`;
DELETE FROM `transfer_application`;
DELETE FROM `batch_pay_record`;
DELETE FROM `source_document`;
DELETE FROM `journal_voucher`;
DELETE FROM `business_voucher`;
DELETE FROM `payment_channel`;
DELETE FROM `ledger_book`;
DELETE FROM `ledger_book_shelf`;


INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`) VALUES
  ('1', '上海', '云南国际信托有限公司', '云南信托');

INSERT INTO `financial_contract` (`adva_repayment_term`, `id`, `ledger_book_no`, `adva_matuterm`, `contract_no`, `app_id`, `company_id`, `adva_repo_term`, `capital_account_id`, `payment_channel_id`, `financial_contract_uuid`)
VALUES
  ('0', '1', 'yunxin_ledger_book', 3, 'YX_AMT_001', 1, 1, 30, 1, 1, 'x12323ww');

INSERT INTO `asset_package` (`id`, `is_available`, `create_time`, `contract_id`, `asset_package_no`, `financial_contract_id`)
VALUES
  (1, 1, now(), 1, 'no', 1);

INSERT INTO `account` (`id`, `account_name`, `account_no`, `alias`, `attr`) VALUES
  (1, 'account_name_1', '955103657777777', NULL, NULL);

INSERT INTO `payment_channel` (`id`, `channel_name`, `user_name`, `user_password`, `merchant_id`, `cer_file_path`, `pfx_file_path`, `pfx_file_key`, `payment_channel_type`, `api_url`, `from_date`, `thru_date`)
VALUES
  (1, '广东银联', 'user1', '123456', '000191400205800', NULL, NULL, NULL, 0, NULL, NULL, NULL);

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`) VALUES
  ('1', '', NULL, NULL, NULL, '10', NULL, '0');

INSERT INTO `asset_set` (`id`, `asset_fair_value`, `asset_initial_value`, `asset_recycle_date`, `asset_status`, `asset_uuid`, `create_time`, `last_modified_time`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `financial_contract_uuid`)
VALUES
  ('1', 1010, 1000, '2015-10-19 13:34:35', 0, 'asset_uuid_1', '2015-10-19 13:34:35', '2015-10-19 13:34:35',
        'DKHD-001-01', 1, '2015-10-19 13:34:35', 'x12323ww');

INSERT INTO `contract` (`id`, `begin_date`, `contract_no`, `end_date`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `financial_contract_uuid`)
VALUES
  (1, '2015-10-19', 'DKHD-001', '2016-10-19', 1, 1, 1, NULL, '2015-10-19 13:34:35', 0.001, 1, 1, 12, 0, 1000, 0.0001,
   'x12323ww');

INSERT INTO `ledger_book` (`id`, `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`)
VALUES
  ('1', 'yunxin_ledger_book', '1', '1', '');


INSERT INTO `rent_order` (`id`, `due_date`, `order_no`, `payout_time`, `total_rent`, `customer_id`, `user_upload_param`, `modify_time`, `asset_set_id`, `create_time`, `clearing_status`, `executing_settling_status`, `financial_contract_id`, `repayment_bill_id`)
VALUES
  ('1', '2015-03-18', 'DKHD-001-01-20160308', '2016-10-19 13:34:35', 10, '1', '', '2016-10-19', 1, '2015-10-19', 0, 0,
   1, 'repayment_bill_id_1'),
  ('2', '2015-03-18', 'DKHD-001-02-20160308', '2016-10-19 13:34:35', 10, '1', '', '2016-10-19', 1, '2015-10-19', 0, 0,
   1, 'repayment_bill_id_2');

INSERT INTO `asset_valuation_detail` (`id`, `amount`, `asset_value_date`, `created_date`, `subject`, `asset_set_id`)
VALUES
  ('1', '1000.00', '2016-03-14 19:46:38', '2016-03-14', '0', '1'),
  ('2', '10.00', '2016-03-15 19:47:16', '2016-03-15', '1', '1'),
  ('3', '1000.00', '2016-03-14 19:46:38', '2016-03-14', '0', '2');

INSERT INTO `contract_account` (`id`, `pay_ac_no`, `bankcard_type`, `contract_id`, `payer_name`) VALUES
  (1, 'pay_ac_no_1', 0, 1, 'payer_name_1');

INSERT INTO `transfer_application` (`id`, `amount`, `creator_id`, `batch_pay_record_id`, `comment`, `create_time`, `deduct_status`, `executing_deduct_status`, `deduct_time`, `last_modified_time`, `contract_account_id`, `order_id`)
VALUES
  (1, '10', NULL, 1, NULL, '2015-10-10', 0, 1, NULL, NULL, 1, 1),
  (2, '10', NULL, 2, NULL, '2015-10-13', 1, 2, NULL, NULL, 1, 2);

INSERT INTO `batch_pay_record` (`id`, `batch_pay_record_uuid`, `amount`, `modify_time`, `request_no`, `serial_no`, `request_data`, `response_data`, `query_response_data`, `trans_date_time`)
VALUES
  ('1', 'batch_pay_record_uuid_1', '10.00', NULL, 'test', 'serial_no_1', NULL, '', '', '20160323111100'),
  ('2', 'batch_pay_record_uuid_2', '10.00', NULL, 'test', 'serial_no_2', NULL, '', '', '20160111111100');

INSERT INTO `source_document` (`id`, `company_id`, `source_document_uuid`, `source_document_type`, `create_time`, `issued_time`, `source_document_status`, `source_account_side`, `booking_amount`, `outlier_document_uuid`, `outlier_trade_time`, `outlier_counter_party_account`, `outlier_counter_party_name`, `outlier_account`, `outlie_account_name`, `outlier_account_id`, `outlier_company_id`, `outlier_serial_global_identity`, `outlier_memo`, `outlier_amount`, `outlier_settlement_modes`, `outlier_breif`, `outlier_account_side`)
VALUES
  ('1', '1', 'source_document_uuid_1', '1', '2016-03-29 17:21:09', '2015-04-03 12:00:00', '0', '1', '0.00',
        'batch_pay_record_uuid_1', '2015-03-30 16:11:50', 'pay_ac_no_1', 'payer_name_1', '1001300219000027827', NULL,
                                                          NULL, '4', 'serial_no_1', '汇川路88号1号楼1803?', '10.00', '3', '',
   '0'),
  ('2', '1', 'source_document_uuid_2', '1', '2016-03-29 17:21:09', '2015-04-03 12:00:00', '1', '1', '0.00',
        'batch_pay_record_uuid_2', '2015-04-02 09:40:07', 'pay_ac_no_1', 'payer_name_1', '1001300219000027827', NULL,
                                                          NULL, '4', 'serial_no_2', 'MONTHLY RENTAL FOR 8', '10.00',
                                                          '3', '', '0'),
  ('3', '1', 'source_document_uuid_3', '0', '2016-03-29 17:21:09', '2015-04-03 12:00:00', '1', '1', '10.00',
        'batch_pay_record_uuid_3', '2015-04-01 14:48:52', 'pay_ac_no_1', 'payer_name_1', '1001300219000027827', NULL,
                                                          NULL, '4', 'serial_no_3', '1804 静安国际广场3月', '10.00', '3', '',
   '0');


SET FOREIGN_KEY_CHECKS = 1;
