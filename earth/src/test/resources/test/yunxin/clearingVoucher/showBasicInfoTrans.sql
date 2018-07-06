SET FOREIGN_KEY_CHECKS=0;

delete from `audit_job`;
delete from `deduct_plan_stat_cache`;
delete from `beneficiary_audit_result`;
delete from `t_deduct_plan`;
delete from `cash_flow`;
delete from `payment_channel`;
delete from `contract_account`;
delete from `financial_contract`;
delete from `ledger_book`;
delete from `account`;
delete from `app`;
delete from `company`;
delete from `payment_channel_information`;
delete from `total_receivable_bills`;
delete from `third_party_audit_bill_stat`;
delete from `clearing_voucher`;
delete from `journal_voucher`;

INSERT INTO `audit_job` (`id`, `uuid`, `audit_job_no`, `merchant_no`, `pg_clearing_account`, `payment_institution`, `audit_job_source`, `account_side`, `start_time`, `end_time`, `audit_result`, `create_time`, `last_modified_time`,`payment_channel_service_uuid`)
VALUES
	(1, 'audit_job_uuid_1', 'audit_job_no_1', '001053110000001', '', '0', 1, 1, '2017-03-23 00:00:00', '2017-03-23 23:59:59', 0, '2017-03-23 10:57:16', '2016-12-29 14:26:52','payment_channel_uuid_1');

INSERT INTO `total_receivable_bills` (`id`, `uuid`, `payment_channel_uuid`, `clearing_receivable_identity`, `fst_merchant_no`, `fst_merchant_name`, `total_num`, `total_amount`, `total_charge`, `total_receivable_amount`, `clearing_cash_flow_identity`, `clearing_cash_flow_identity_list`, `clearing_time`, `create_time`, `last_modified_time`, `cash_flow_clearing_time`, `audit_status`,`snd_merchant_no`,`payment_gateway`)
VALUES
('579512', '1eebd205-025a-4de7-91c0-bf9edc119b87', 'ae9e5cdd-4566-4c85-8d97-f34756f8ee88', 'YS128370597446180864', '001053110000001', '用钱宝', '3', '30.00', '0.00', '10.00', '', '', '2017-03-23 00:00:00', '2017-11-16 03:00:01', '2017-11-16 03:00:01', NULL, NULL,'','0');
INSERT INTO `total_receivable_bills` (`id`, `uuid`, `financial_contract_uuid`, `payment_channel_uuid`, `clearing_receivable_identity`, `fst_merchant_no`, `fst_merchant_name`, `total_num`, `total_amount`, `total_charge`, `total_receivable_amount`, `clearing_cash_flow_identity`, `clearing_cash_flow_identity_list`, `clearing_time`, `create_time`, `last_modified_time`, `cash_flow_clearing_time`, `audit_status`, `payment_gateway`, `snd_merchant_no`) VALUES 
('5797', 'ed055704-25c1-46fe-ab56-d733d9af0bb0', 'b6b407fb-de54-4d84-9cba-84ccb2c17fcf', 'f1ccca57-7c80-4429-b226-8ad31a729609', 'YS128370599623024640', '001053110000001', '拍拍贷', '0', '100000.00', '0.00', '100000.00', '', '', '2017-11-23 00:00:00', '2017-11-16 03:00:01', '2017-11-23 03:00:01', NULL, NULL,'3',''),
('5796', '1eebd205-025a-4de7-91c0-bf9edc119b87', 'b6b407fb-de54-4d84-9cba-84ccb2c17fcf', 'ae9e5cdd-4566-4c85-8d97-f34756f8ee88', 'YS128370597446180864', '001053110000001', '用钱宝', '0', '100000.00', '0.00', '90000.00', '', '', '2017-11-23 00:00:00', '2017-11-16 03:00:01', '2017-11-23 03:00:01', NULL, NULL,'3',''),
('5798', 'c54e63c9-cff2-4cd2-b394-f05c916e88e9', '4eac7451-8808-4e3f-8101-2abaddcfd14b', 'ae9e5cdd-4566-4c85-8d97-f34756f8ee88', 'YS128370600323473408', '001053110000001', '用钱宝', '0', '100000.00', '0.00', '100000.00', '', '', '2017-11-23 00:00:00', '2017-11-16 03:00:01', '2017-11-23 03:00:01', NULL, NULL,'3','');
	
INSERT INTO `third_party_audit_bill_stat` (`id`, `audit_bill_stat_uuid`, `financial_contract_uuid`, `payment_gateway`, `merchant_no`, `snd_lvl_merchant_no`, `account_side`, `settle_date`, `reckon_account`, `sum_amount`, `sum_count`, `sum_service_fee`)
VALUES
('42', 'e96911b4-2007-4b1c-90fb-c54708d83c66', 'b6b407fb-de54-4d84-9cba-84ccb2c17fcf', '0', '001053110000001', '', '1', '2017-03-23 00:00:00', '', '20.00', '2', '0.00');

INSERT INTO `deduct_plan_stat_cache` (`id`, `uuid`, `financial_contract_uuid`, `payment_channel_uuid`, `payment_gateway`, `pg_account`, `pg_clearing_account`, `start_time`, `end_time`, `suc_amount`, `suc_num`, `fail_num`, `last_modified_time`, `actual_repayment_amount`, `actual_total_fee`, `actual_principal`, `actual_interest`, `actual_loan_service_fee`, `actual_tech_fee`, `actual_other_fee`, `actual_overdue_penalty`, `actual_overdue_default_fee`, `actual_overdue_service_fee`, `actual_overdue_other_fee`)
VALUES 
('141', 'e7dd207a-fea3-4bd1-bd21-f1392f39d193', '7eb9c015-732e-4b0e-a9c0-76774ad24e38', 'f1ccca57-7c80-4429-b226-8ad31a729609', '0', '001053110000001', '', '2017-03-23 16:00:00', '2017-03-23 17:00:00', '0.00', '0', '5', '2017-03-30 16:58:36', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00');

--id=2平账,id=3本端多帐,id=4对端多帐
INSERT INTO `beneficiary_audit_result` (`id`, `uuid`, `financial_contract_uuid`, `payment_channel_uuid`, `payment_gateway`, `fst_merchant_no`, `snd_merchant_no`, `system_bill_identity`, `system_bill_plan_amount`, `system_bill_occur_date`, `system_bill_transaction_time`, `system_bill_type`, `trans_excution_status`, `local_party_account_no`, `counter_party_payment_bank`, `counter_party_account_name`, `counter_party_account_no`, `counter_party_id_card_no`, `cash_flow_financial_contract_identity`, `cash_flow_payment_channel_identity`, `cash_flow_payment_gateway`, `cash_flow_fst_merchant_no`, `cash_flow_snd_merchant_no`, `cash_flow_identity`, `cash_flow_type`, `cash_flow_sequence_no`, `cash_flow_transaction_time`, `cash_flow_transaction_amount`, `cash_flow_settle_date`, `cash_flow_account_side`, `cash_flow_remark`, `cash_flow_appendix`, `last_modified_time`, `result_code`, `result_time`, `trade_uuid`)
VALUES 
('2', '183e4d80-dba6-4e14-9bda-a48cf0677102', 'd2812bc5-5057-4a91-b3fd-9019506f0499', 'f1ccca57-7c80-4429-b226-8ad31a729609', '0', '001053110000001', '', '00453b95-bfb7-4e63-bc71-8fc933589d0b', '10.00', '2017-03-23 16:21:40', NULL, '2', '3', '', '', '深圳一号', '11021315964009', '410402198801115650', NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2017-03-23 16:21:41', '2', '2017-03-23 16:21:40', '8G9r6h7KwTx0Cm0CkFt');

INSERT INTO `beneficiary_audit_result` (`id`, `uuid`, `financial_contract_uuid`, `payment_channel_uuid`, `payment_gateway`, `fst_merchant_no`, `snd_merchant_no`, `system_bill_identity`, `system_bill_plan_amount`, `system_bill_occur_date`, `system_bill_transaction_time`, `system_bill_type`, `trans_excution_status`, `local_party_account_no`, `counter_party_payment_bank`, `counter_party_account_name`, `counter_party_account_no`, `counter_party_id_card_no`, `cash_flow_financial_contract_identity`, `cash_flow_payment_channel_identity`, `cash_flow_payment_gateway`, `cash_flow_fst_merchant_no`, `cash_flow_snd_merchant_no`, `cash_flow_identity`, `cash_flow_type`, `cash_flow_sequence_no`, `cash_flow_transaction_time`, `cash_flow_transaction_amount`, `cash_flow_settle_date`, `cash_flow_account_side`, `cash_flow_remark`, `cash_flow_appendix`, `last_modified_time`, `result_code`, `result_time`, `trade_uuid`) 
VALUES 
('3', 'ebad9e80-ea18-4b97-b030-2d70d7449234', '2d380fe1-7157-490d-9474-12c5a9901e29', 'f1ccca57-7c80-4429-b226-8ad31a729609', '0', '001053110000001', '', '0b08ea18-835b-4ba2-a219-3d21f48428c6', '10.00', '2017-03-23 16:40:50', NULL, '2', '3', '', '', '李杰', '6214855712106527', '320301198502169142', NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2017-03-23 16:40:56', '1', '2017-03-23 16:40:50', 'lXdPJVxVH2oteEXbOoM');

INSERT INTO `beneficiary_audit_result` (`id`, `uuid`, `financial_contract_uuid`, `payment_channel_uuid`, `payment_gateway`, `fst_merchant_no`, `snd_merchant_no`, `system_bill_identity`, `system_bill_plan_amount`, `system_bill_occur_date`, `system_bill_transaction_time`, `system_bill_type`, `trans_excution_status`, `local_party_account_no`, `counter_party_payment_bank`, `counter_party_account_name`, `counter_party_account_no`, `counter_party_id_card_no`, `cash_flow_financial_contract_identity`, `cash_flow_payment_channel_identity`, `cash_flow_payment_gateway`, `cash_flow_fst_merchant_no`, `cash_flow_snd_merchant_no`, `cash_flow_identity`, `cash_flow_type`, `cash_flow_sequence_no`, `cash_flow_transaction_time`, `cash_flow_transaction_amount`, `cash_flow_settle_date`, `cash_flow_account_side`, `cash_flow_remark`, `cash_flow_appendix`, `last_modified_time`, `result_code`, `result_time`, `trade_uuid`) 
VALUES 
('4', '4a3170af-c844-4164-8ca6-210bea6e3159', '2d380fe1-7157-490d-9474-12c5a9901e29', 'f1ccca57-7c80-4429-b226-8ad31a729609', '0', '001053110000001', '', '43ffe428-869f-45e5-b525-45cceebf9a8e', '10.00', '2017-03-23 16:47:01', NULL, '2', '3', '', '', '李杰', '6214855712106520', '320301198502169142', NULL, '', '0', '001053110000001', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2017-03-23 16:56:41', '3', '2017-03-23 16:47:01', 'aKL1LSN8CGcMkJhO1e4');


INSERT INTO `t_deduct_plan` (`id`, `deduct_plan_uuid`, `deduct_application_uuid`, `deduct_application_detail_uuid`, `financial_contract_uuid`, `contract_unique_id`, `contract_no`, `payment_gateway`, `payment_channel_uuid`, `pg_account`, `pg_clearing_account`, `transaction_type`, `transaction_remark`, `cp_bank_code`, `cp_bank_card_no`, `cp_bank_account_holder`, `cp_id_type`, `cp_id_number`, `cp_bank_province`, `cp_bank_city`, `cp_bank_name`, `planned_payment_date`, `complete_payment_date`, `planned_total_amount`, `actual_total_amount`, `execution_precond`, `execution_status`, `execution_remark`, `create_time`, `creator_name`, `last_modified_time`, `transaction_serial_no`, `mobile`, `transaction_recipient`, `trade_uuid`, `repayment_type`, `source_type`, `complete_time`, `transaction_time`, `clearing_cash_flow_uuid`, `clearing_status`, `clearing_time`, `retriable`, `payment_order_uuid`, `batch_deduct_application_uuid`, `batch_deduct_id`, `retry_times`, `notify_status`, `none_business_check_status`, `business_check_status`)
VALUES 
('112659', '00453b95-bfb7-4e63-bc71-8fc933589d0b', '812dfa99-98ba-42f2-a094-d594c46b2bd1', NULL, 'd2812bc5-5057-4a91-b3fd-9019506f0499', 'b728321d-0bcd-41aa-a456-c57adad7b013', '云信信2016-236号(3629472)号', '0', 'f1ccca57-7c80-4429-b226-8ad31a729609', '001053110000001', NULL, '1', '', 'C10104', '11021315964009', '深圳一号', '0', '410402198801115650', '410000', '410400', '中国银行 ', NULL, NULL, '10.00', '0.00', NULL, '3', '其它错误', '2017-03-23 16:21:01', 't_test_zfb', '2017-03-23 16:21:40', NULL, '13777847783', '1', '8G9r6h7KwTx0Cm0CkFt', '1', NULL, '2017-03-23 16:21:40', NULL, NULL, '0', NULL, '0', NULL, NULL, NULL, '0', '0', '0', '0');

INSERT INTO `t_deduct_plan` (`id`, `deduct_plan_uuid`, `deduct_application_uuid`, `deduct_application_detail_uuid`, `financial_contract_uuid`, `contract_unique_id`, `contract_no`, `payment_gateway`, `payment_channel_uuid`, `pg_account`, `pg_clearing_account`, `transaction_type`, `transaction_remark`, `cp_bank_code`, `cp_bank_card_no`, `cp_bank_account_holder`, `cp_id_type`, `cp_id_number`, `cp_bank_province`, `cp_bank_city`, `cp_bank_name`, `planned_payment_date`, `complete_payment_date`, `planned_total_amount`, `actual_total_amount`, `execution_precond`, `execution_status`, `execution_remark`, `create_time`, `creator_name`, `last_modified_time`, `transaction_serial_no`, `mobile`, `transaction_recipient`, `trade_uuid`, `repayment_type`, `source_type`, `complete_time`, `transaction_time`, `clearing_cash_flow_uuid`, `clearing_status`, `clearing_time`, `retriable`, `payment_order_uuid`, `batch_deduct_application_uuid`, `batch_deduct_id`, `retry_times`, `notify_status`, `none_business_check_status`, `business_check_status`)
VALUES 
('112661', '0b08ea18-835b-4ba2-a219-3d21f48428c6', 'efbe2456-8c63-4e86-9704-4e72c610e222', NULL, '2d380fe1-7157-490d-9474-12c5a9901e29', '5d299a68-81dc-4031-a925-67313bbdff95', '5d299a68-81dc-4031-a925-67313bbdff95', '0', 'f1ccca57-7c80-4429-b226-8ad31a729609', '001053110000001', NULL, '1', NULL, 'C10105', '6214855712106527', '李杰', '0', '320301198502169142', '110000', '110100', '中国建设银行 ', NULL, NULL, '10.00', '0.00', NULL, '3', '余额不足', '2017-03-23 16:40:26', 't_test_zfb', '2017-03-23 16:40:50', NULL, '13777847783', '1', 'lXdPJVxVH2oteEXbOoM', '1', '1', '2017-03-23 16:40:50', NULL, NULL, '0', NULL, '0', NULL, NULL, NULL, '0', '0', '0', '0');

INSERT INTO `t_deduct_plan` (`id`, `deduct_plan_uuid`, `deduct_application_uuid`, `deduct_application_detail_uuid`, `financial_contract_uuid`, `contract_unique_id`, `contract_no`, `payment_gateway`, `payment_channel_uuid`, `pg_account`, `pg_clearing_account`, `transaction_type`, `transaction_remark`, `cp_bank_code`, `cp_bank_card_no`, `cp_bank_account_holder`, `cp_id_type`, `cp_id_number`, `cp_bank_province`, `cp_bank_city`, `cp_bank_name`, `planned_payment_date`, `complete_payment_date`, `planned_total_amount`, `actual_total_amount`, `execution_precond`, `execution_status`, `execution_remark`, `create_time`, `creator_name`, `last_modified_time`, `transaction_serial_no`, `mobile`, `transaction_recipient`, `trade_uuid`, `repayment_type`, `source_type`, `complete_time`, `transaction_time`, `clearing_cash_flow_uuid`, `clearing_status`, `clearing_time`, `retriable`, `payment_order_uuid`, `batch_deduct_application_uuid`, `batch_deduct_id`, `retry_times`, `notify_status`, `none_business_check_status`, `business_check_status`)
VALUES 
('112662', '43ffe428-869f-45e5-b525-45cceebf9a8e', 'c32e0097-df00-4eeb-b33e-088bfde9238b', NULL, '2d380fe1-7157-490d-9474-12c5a9901e29', '5d299a68-81dc-4031-a925-67313bbdff95', '5d299a68-81dc-4031-a925-67313bbdff95', '0', 'f1ccca57-7c80-4429-b226-8ad31a729609', '001053110000001', NULL, '1', NULL, 'C10105', '6214855712106520', '李杰', '0', '320301198502169142', '110000', '110100', '中国建设银行 ', NULL, '2017-03-23 16:43:08', '10.00', '10.00', NULL, '3', '交易?成功', '2017-03-23 16:46:30', 't_test_zfb', '2017-03-23 16:47:01', NULL, '13777847783', '1', 'aKL1LSN8CGcMkJhO1e4', '1', '1', '2017-03-23 16:47:01', NULL, NULL, '0', NULL, '0', NULL, NULL, NULL, '0', '0', '0', '0');


INSERT INTO `cash_flow` (`id`, `cash_flow_uuid`, `cash_flow_channel_type`, `company_uuid`, `host_account_uuid`, `host_account_no`, `host_account_name`, `counter_account_no`, `counter_account_name`, `counter_account_appendix`, `counter_bank_info`, `account_side`, `transaction_time`, `transaction_amount`, `balance`, `transaction_voucher_no`, `bank_sequence_no`, `remark`, `other_remark`, `strike_balance_status`, `trade_uuid`, `issued_amount`, `audit_status`)
VALUES
	(1, '39e1f7a8-aac3-45e8-a356-427603b7fdf811', 0, 'companyUuid', 'hostAccountUuid', '600000000002', 'hostAccountName', 'e897feb2-5444-4533-8136-d9e5df294c1c0', 'ounterAccountName', '', '中国银行', 1, '2016-12-23 19:24:02', 1.00, 0.00, 'transactionVoucherNo', 'dcbb0c37-eb03-42d5-83d5-3d1e7329d149', '测试(8d88111d-1748-49a3-be5d-1fee151516bd-1.5)', 'otherRemark', 1, '8d88111d-1748-49a3-be5d-1fee151516bd-1.5', 0.00, 0),
	(2, 'e59ff688-0b26-40ad-ad9f-d894ec7e2ea3', 0, 'companyUuid', 'hostAccountUuid', '600000000002', 'hostAccountName', 'cd67ceb2-8001-438f-874b-ea7d260307060', 'ounterAccountName', '', '中国银行', 0, '2016-12-23 19:24:02', 1.00, 0.00, 'transactionVoucherNo', 'a23f5e72-04ef-4410-b596-9e1646050659', '测试(2bd2c733-9204-4b53-b4f9-b3d69b42c525-3.1)', 'otherRemark', 0, '2bd2c733-9204-4b53-b4f9-b3d69b42c525-3.1', 0.00, 0);

	
INSERT INTO `payment_channel` (`id`, `channel_name`, `user_name`, `user_password`, `merchant_id`, `cer_file_path`, `pfx_file_path`, `pfx_file_key`, `payment_channel_type`, `api_url`, `from_date`, `thru_date`)
	VALUES ('1', 'channel_name', 'user_name', 'user_password', '001053110000001', 'cer_file_path', 'pfx_file_path', 'pfx_file_key', '0', 'api_url', NULL, NULL);

INSERT INTO `contract_account` (`id`, `pay_ac_no`, `bankcard_type`, `contract_id`, `payer_name`, `bank`, `bind_id`, `id_card_num`, `bank_code`, `province`, `province_code`, `city`, `city_code`, `standard_bank_code`, `from_date`, `thru_date`, `virtual_account_uuid`, `bank_card_status`, `contract_account_uuid`, `contract_account_type`)
VALUES
('1', '6217000000000003006', NULL, '54340', '测试用户1', '中国邮政储蓄银行', NULL, '6217000000000003006', '403', '安徽省', NULL, '亳州', NULL, NULL, '2016-04-17 00:00:00', '2900-01-01 00:00:00', NULL, 0, '999e54ce-c2db-11e6-908d-745c6f182c95', 0);


INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`,`adva_repayment_term`)
VALUES
	(1, 0, 3, '2016-09-01 00:00:00', 'G32000', '用钱宝测试', 2, 1, 60, '2017-12-01 00:00:00', 101, 0, 1, 2, 1, 'e17d8068-46a0-48bb-b158-8e80168bbfc3', 'financial_contract_uuid_1', 0, 0, 0, 0, 1, 0,7);
INSERT INTO `ledger_book` (`id`, `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`)
VALUES
	(1, 'ledger_book_no_1', '1', NULL, NULL);

INSERT INTO `account` (`id`, `account_name`, `account_no`, `bank_name`, `alias`, `attr`, `scan_cash_flow_switch`, `usb_key_configured`, `uuid`, `bank_code`)
VALUES
	(101, '云南信托', '600000000002', '平安银行深圳分行', NULL, NULL, 0, 0, '8e3cd5c5-8fb6-4cd6-b6c7-660a9f35f47c', NULL);

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`)
VALUES
	(2, 'qyb', NULL, 1, NULL, '测试商户yqb', 4, NULL),
  (3, 'qyd', NULL, 1, NULL, '测试商户yqd', 4, NULL);

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`)
VALUES
	(1, '上海', '测试金融公司', '测试金融', 'a02c02b9-6f98-11e6-bf08-00163e002839'),
	(4, '杭州', 'yqb', 'yqb', 'a02c08b5-6f98-11e6-bf08-00163e002839');

INSERT INTO `payment_channel_information` (`id`, `related_financial_contract_uuid`, `related_financial_contract_name`, `payment_channel_uuid`, `payment_channel_name`, `payment_institution_name`, `outlier_channel_name`, `create_time`, `last_modify_time`, `credit_channel_working_status`, `debit_channel_working_status`, `credit_payment_channel_service_uuid`, `debit_payment_channel_service_uuid`, `configure_progress`, `payment_configure_data`, `clearing_no`, `business_type`)
VALUES
	(1, 'financial_contract_uuid_1', '测试农分期', 'payment_channel_uuid_1', 'G08200银联代收付', 0, '001053110000001', '2016-09-02 00:00:00', '2016-11-09 23:59:46', 0, 1, NULL, 'f1ccca57-7c80-4429-b226-8ad31a729609', 0, '{\"complete\":true,\"creditChannelConfigure\":{\"channelStatus\":\"NOTLINK\",\"clearingInterval\":0,\"valid\":true},\"debitChannelConfigure\":{\"channelStatus\":\"ON\",\"chargeExcutionMode\":\"BACKWARD\",\"chargePerTranscation\":0,\"chargeRateMode\":\"SINGLEFIXED\",\"clearingInterval\":1,\"valid\":true}}', NULL, 0);


INSERT INTO `clearing_voucher` (`id`, `uuid`, `voucher_no`, `batch_uuid`, `source_account_side`, `payment_institution`, `merchant_no`, `pg_clearing_account`, `host_account_no`, `host_account_name`, `counter_account_no`, `counter_account_name`, `bank_info`, `clearing_voucher_status`, `create_time`, `last_modified_time`, `clearing_time`, `cash_flow_time`, `cash_flow_uuid`, `audit_job_uuid`, `total_receivable_bills_uuid`, `voucher_amount`, `single_signs`, `appendix`, `remark`)
VALUES
('11', 'f6623e33-02a8-497a-b576-f9641376b6bd', 'V138056156130729984', '52bd00cd-6633-4e35-8a06-7c7eabfe7522', '0', '3', '001053110000001', '', '95200155300001595', '浦发2000040752', '95130154900000571', '浦发2000040752', '浦发杭州高新支行', '1', '2017-12-12 20:26:58', '2017-12-12 20:26:58', NULL, '2017-09-07 10:31:19', '635e5285-e3c8-4df2-826b-b58dd2f201b5', 'b2e136c7-f595-4861-86af-f95fcf888d70', 'ed055704-25c1-46fe-ab56-d733d9af0bb0', '40000.00', '1', '{\"audit_job\":\"[\\\"b2e136c7-f595-4861-86af-f95fcf888d70\\\",\\\"b8c4033f-28f5-47eb-b1ab-d5315e2ec0cf\\\",\\\"2d056efa-1359-4dd0-a5fb-006f9c0802cc\\\"]\",\"cash_flow\":\"[\\\"635e5285-e3c8-4df2-826b-b58dd2f201b5\\\"]\",\"total_receivable_bills\":\"[\\\"ed055704-25c1-46fe-ab56-d733d9af0bb0\\\",\\\"1eebd205-025a-4de7-91c0-bf9edc119b87\\\",\\\"c54e63c9-cff2-4cd2-b394-f05c916e88e9\\\"]\"}', '备注');

INSERT INTO `journal_voucher` (`id`, `account_side`, `bank_identity`, `billing_plan_uuid`, `booking_amount`, `business_voucher_type_uuid`, `business_voucher_uuid`, `cash_flow_amount`, `cash_flow_breif`, `cash_flow_channel_type`, `cash_flow_serial_no`, `cash_flow_uuid`, `checking_level`, `company_id`, `completeness`, `counter_party_account`, `counter_party_name`, `journal_voucher_uuid`, `notification_identity`, `notification_memo`, `notification_record_uuid`, `notified_date`, `settlement_modes`, `source_document_amount`, `source_document_breif`, `source_document_cash_flow_serial_no`, `source_document_counter_party_uuid`, `source_document_identity`, `source_document_uuid`, `status`, `trade_time`, `batch_uuid`, `created_date`, `source_document_counter_party_account`, `source_document_counter_party_name`, `issued_time`, `journal_voucher_type`, `counter_account_type`, `related_bill_contract_info_lv_1`, `related_bill_contract_info_lv_2`, `related_bill_contract_info_lv_3`, `cash_flow_account_info`, `journal_voucher_no`, `related_bill_contract_no_lv_1`, `related_bill_contract_no_lv_2`, `related_bill_contract_no_lv_3`, `related_bill_contract_no_lv_4`, `source_document_no`, `appendix`, `last_modified_time`, `local_party_account`, `local_party_name`, `source_document_local_party_account`, `source_document_local_party_name`, `second_journal_voucher_type`, `third_journal_voucher_type`, `is_has_data_sync_log`, `cash_flow_time`)
VALUES
('189', '1', '10320102939202010', 'ed055704-25c1-46fe-ab56-d733d9af0bb0', '100080.00', '2fbc0014-32ae-4054-9707-450280a63f69', '', '100080.00', '凭证重构', '0', '5e172404-d4ed-11e7-a3df-525400dbb013', '5e172381-d4ed-11e7-a3df-525400dbb013', '0', '41', NULL, '10320102939202010', '杭州随地付', 'c875a5c0-5ddb-4856-bdc6-c1709063b704', '5e172404-d4ed-11e7-a3df-525400dbb013', NULL, NULL, '2017-11-29 18:09:20', '3', '100080.00', '凭证重构', '5e172404-d4ed-11e7-a3df-525400dbb013', NULL, '5e172381-d4ed-11e7-a3df-525400dbb013', '372e33b4-19fe-4b5a-a812-d9dda68a7f20', '1', '2017-11-29 18:09:20', '52bd00cd-6633-4e35-8a06-7c7eabfe7522', '2017-11-29 18:12:20', '10320102939202010', '杭州随地付', '2017-11-29 18:12:20', '13', '0', '9495f5f2-d306-461a-8b03-5896923dc1b3', NULL, '', 'cbf70764-ca28-11e7-b26b-525400dbb013', '8d0a7b38-c5e8-466c-a96a-5bd84841bdb9', 'VAN测试', '', NULL, NULL, 'CZ133311233296486400', NULL, '2017-11-29 18:12:20', NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL);


SET FOREIGN_KEY_CHECKS=1;