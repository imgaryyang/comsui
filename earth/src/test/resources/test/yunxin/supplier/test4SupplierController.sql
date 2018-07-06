SET FOREIGN_KEY_CHECKS=0;

delete from `supplier`;
delete from `bank_card`;
delete from `supplier_identity_map`;
delete from `contract`;
delete from `financial_contract`;

INSERT INTO `supplier` (`id`, `uuid`, `name`, `legal_person`, `business_licence`, `create_time`, `last_modify_time`)
VALUES
('1', 'c496a45d-3d9f-417e-905c-dfd2871d8271', 'supplier1', 'zhangsan', '', '2017-09-20 14:36:16', '2017-09-20 14:37:13'),
('2', 'c496a45d-3d9f-417e-905c-dfd2871d8272', 'supplier2', 'zhangsan', '', '2017-09-20 14:36:16', '2017-09-20 14:37:13'),
('3', 'c496a45d-3d9f-417e-905c-dfd2871d8273', 'supplier3', 'zhangsan', '', '2017-09-20 14:36:16', '2017-09-20 14:37:13'),
('4', 'c496a45d-3d9f-417e-905c-dfd2871d8274', 'supplier4', 'zhangsan', '', '2017-09-20 14:36:16', '2017-09-20 14:37:13'),
('5', 'c496a45d-3d9f-417e-905c-dfd2871d8275', 'supplier5', 'zhangsan', '', '2017-09-20 14:36:16', '2017-09-20 14:37:13'),
('6', 'c496a45d-3d9f-417e-905c-dfd2871d8276', 'supplier6', 'zhangsan', '', '2017-09-20 14:36:16', '2017-09-20 14:37:13'),
('7', 'c496a45d-3d9f-417e-905c-dfd2871d8277', 'supplie7', 'zhangsan', '', '2017-09-20 14:36:16', '2017-09-20 14:37:13'),
('8', 'c496a45d-3d9f-417e-905c-dfd2871d8288', 'supplier8', 'zhangsan', '', '2017-09-20 14:36:16', '2017-09-20 14:37:13');


INSERT INTO `bank_card` (`id`, `uuid`, `outer_identifier`, `identity`, `account_name`, `account_no`, `bank_code`, `bank_name`, `province`, `province_code`, `city`, `city_code`, `create_time`, `last_modify_time`)
VALUES
('1', '02dc514a-3260-417e-a3e8-8ceecf31b311', 'c496a45d-3d9f-417e-905c-dfd2871d8271', '0', '陈彩非', '6666667777', 'C10102', '中国工商银行', '山东省', '370000', '青岛市', '370200', '2017-09-20 14:27:36', '2017-09-20 14:28:38'),
('2', '02dc514a-3260-417e-a3e8-8ceecf31b312', 'c496a45d-3d9f-417e-905c-dfd2871d8271', '0', '陈彩飞', '6666667778', 'C10102', '中国工商银行', '山东省', '370000', '青岛市', '370200', '2017-09-20 14:27:36', '2017-09-20 14:28:38');

INSERT INTO `supplier_identity_map` (`id`, `uuid`, `supplier_uuid`, `financial_contract_uuid`, `contract_unique_id`, `create_time`, `last_modify_time`)
VALUES ('1', '1111111111111', 'c496a45d-3d9f-417e-905c-dfd2871d8271', '2d380fe1-7157-490d-9474-12c5a9901e29', '594167137676416fb1ea1b0914ac9bc6', NULL, NULL);

INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, `interest_rate_cycle`, `customer_uuid`)
VALUES ('1', 'a0afc961-5fa8-11e6-b2c2-00163e002839', '594167137676416fb1ea1b0914ac9bc6', '2016-04-17', '2016-78-DK(ZQ2016042522479)', NULL, '1', '0.00', '1', '1', '162', NULL, '2016-05-27 18:27:16', '0.1560000000', '0', '0', '1', '2', '1200.00', '0.0005000000', '1', NULL, '6', '2d380fe1-7157-490d-9474-12c5a9901e29', '0', '81cbfcab-aac4-4c92-9580-0b1d3d5b768f');

INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`, `day_of_month`, `pay_for_go`, `repurchase_principal_algorithm`, `repurchase_interest_algorithm`, `repurchase_penalty_algorithm`, `repurchase_other_charges_algorithm`, `repayment_check_days`, `repurchase_cycle`, `days_of_cycle`, `temporary_repurchases`, `allow_freewheeling_repayment`, `capital_party`, `other_party`, `contract_short_name`, `remittance_object`, `financial_type`)
VALUES ('5', '1', '3', '2016-04-01 00:00:00', 'H73500', '测试合同', '1', '1', '91', '2016-07-01 00:00:00', '5', '0', '1', '90', '2', '74a9ce4d-cafc-407d-b013-987077541bdc', '2d380fe1-7157-490d-9474-12c5a9901e29', '1', '1', '1', '1', '0', '1', NULL, NULL, NULL, NULL, '1', '0', '0', '5', '(principal+interest)*0.05/100*overdueDay', '123.00', '23.00', NULL, NULL, '2017-05-11 14:39:40', '1', '1', 'outstandingPrincipal+outstandingOverdueCharges', '30', '0', 'outstandingPrincipal', 'outstandingInterest', 'outstandingPenaltyInterest', '', '-1', '0', '[]', '[]', '1', NULL, NULL, NULL, NULL, '0');

SET FOREIGN_KEY_CHECKS=0;