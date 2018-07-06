SET FOREIGN_KEY_CHECKS=0;

delete from `app`;
delete from `customer`;
delete from `customer_person`;
delete from `customer_enterprise`;
delete from `bank_card`;
delete from `virtual_account`;
delete from `contract`;
delete from `financial_contract`;

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`, `create_time`, `last_modify_time`)
VALUES ('1', 'nongfenqi', '', '', '', '测试分期', '2', NULL, NULL, NULL);

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`, `customer_style`, `status`, `id_type`)
VALUES
('1', '3412221997', NULL, 'CustomerName11', 'C30433', '1', 'CustomerUuid11', '0', '0', '1', '0'),
('2', '3412221998', NULL, 'CustomerName12', 'C30433', '1', 'CustomerUuid12', '0', '1', '1', '0'),
('3', '3412221999', NULL, 'CustomerName13', 'C30433', '1', 'CustomerUuid13', '0', '0', '0', '0'),
('4', '3412221990', NULL, 'CustomerName14', 'C30433', '1', 'CustomerUuid14', '0', '0', '1', '1'),
('5', '3412221991', NULL, 'CustomerName15', 'C30433', '1', 'CustomerUuid21', '0', '0', '1', '0'),
('6', '3412221991', NULL, 'CustomerName21', 'C30433', '1', 'CustomerUuid15', '0', '1', '1', '0'),
('7', '3412221991', NULL, 'CustomerName16', 'C30433', '1', 'CustomerUuid16', '1', '1', '1', '0');

INSERT INTO `customer_person` (`id`, `uuid`, `customer_uuid`, `name`, `sex`, `birthday`, `id_type`, `id_number`, `mobile`, `marital_status`, `highest_education`, `highest_degree`, `residential_status`, `residential_address`, `postal_address`, `residential_code`, `postal_code`, `occupation`, `duty`, `title`, `industry`, `company_name`, `create_time`, `last_modify_time`)
VALUES ('1', '8c8e0197-3d7c-45ff-a136-f50afd7e6e63', 'CustomerUuid11', '陈彩非', NULL, NULL, '0', '1234567', '1352246', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2017-09-19 16:45:12', '2017-09-20 14:24:33');

INSERT INTO `customer_enterprise` (`id`, `uuid`, `customer_uuid`, `name`, `birthday`, `id_type`, `id_number`, `legal_person`, `id_card_num`, `industry`, `address`, `registered_capital`, `company_type`, `create_time`, `last_modify_time`)
VALUES ('1', 'b37f3972-8f54-4e5c-b21c-9a97f630cdfe', 'CustomerUuid12', '测试用户321', NULL, '6', '111111111111111111111111111111111111111', NULL, '23@#￥@#￥#@￥@#4', NULL, NULL, NULL, NULL, '2017-09-19 16:52:46', '2017-09-19 16:52:46');

INSERT INTO `bank_card` (`id`, `uuid`, `outer_identifier`, `identity`, `account_name`, `account_no`, `bank_code`, `bank_name`, `province`, `province_code`, `city`, `city_code`, `create_time`, `last_modify_time`)
VALUES
('1', '02dc514a-3260-417e-a3e8-8ceecf31b310', 'CustomerUuid11', '1', '陈彩非', '6666667777', 'C10102', '中国工商银行', '山东省', '370000', '青岛市', '370200', '2017-09-20 14:27:36', '2017-09-20 14:28:38');

INSERT INTO `virtual_account` (`id`, `total_balance`, `virtual_account_uuid`, `parent_account_uuid`, `virtual_account_alias`, `virtual_account_no`, `version`, `owner_uuid`, `owner_name`, `fst_level_contract_uuid`, `snd_level_contract_uuid`, `trd_level_contract_uuid`, `create_time`, `last_update_time`, `last_modified_time`, `customer_type`, `virtual_account_status`)
VALUES
('1', '11000000.00', '847c-4e36331cb781', NULL, '', 'VACC27438CADB442A6A1', '45b52af0-7a1a-4c39-beac-4f32a2365986', 'CustomerUuid21', 'yqb', 'db36ecc9-d80c-4350-bd0d-59b1139d550d', NULL, '', '2016-08-24 21:27:10', '2017-09-19 10:19:53', '2016-10-03 15:12:45', NULL, '0'),
('2', '11000000.00', '847c-4e36331cb782', NULL, '', 'VACC27438CADB442A6A2', '45b52af0-7a1a-4c39-beac-4f32a2365986', 'CustomerUuid15', 'yqb', 'db36ecc9-d80c-4350-bd0d-59b1139d550d', NULL, '', '2016-08-24 21:27:10', '2017-09-19 10:19:53', '2016-10-03 15:12:45', NULL, '0'),
('3', '11000000.00', '847c-4e36331cb783', NULL, '', 'VACC27438CADB442A6A3', '45b52af0-7a1a-4c39-beac-4f32a2365986', 'CustomerUuid16', 'yqb', 'db36ecc9-d80c-4350-bd0d-59b1139d550d', NULL, '', '2016-08-24 21:27:10', '2017-09-19 10:19:53', '2016-10-03 15:12:45', NULL, '0');

INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, `interest_rate_cycle`, `customer_uuid`)
VALUES
('1', '00163e002831', '0914ac9bc1', '2016-04-17', '2016-78-DK(ZQ2016042522471)', NULL, '1', '0.00', '1', '5', '162', NULL, '2016-05-27 18:27:16', '0.1560000000', '0', '0', '1', '2', '1200.00', '0.0005000000', '1', NULL, '6', '2d380fe1-7157-490d-9474-12c5a9901e29', '0', 'CustomerUuid21'),
('2', '00163e002832', '0914ac9bc2', '2016-04-17', '2016-78-DK(ZQ2016042522472)', NULL, '1', '0.00', '1', '6', '162', NULL, '2016-05-27 18:27:16', '0.1560000000', '0', '0', '1', '2', '1200.00', '0.0005000000', '1', NULL, '6', '2d380fe1-7157-490d-9474-12c5a9901e29', '0', 'CustomerUuid15'),
('3', '00163e002833', '0914ac9bc3', '2016-04-17', '2016-78-DK(ZQ2016042522473)', NULL, '1', '0.00', '1', '7', '162', NULL, '2016-05-27 18:27:16', '0.1560000000', '0', '0', '1', '2', '1200.00', '0.0005000000', '1', NULL, '6', '2d380fe1-7157-490d-9474-12c5a9901e29', '0', 'CustomerUuid16');

INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`, `day_of_month`, `pay_for_go`, `repurchase_principal_algorithm`, `repurchase_interest_algorithm`, `repurchase_penalty_algorithm`, `repurchase_other_charges_algorithm`, `repayment_check_days`, `repurchase_cycle`, `days_of_cycle`, `temporary_repurchases`, `allow_freewheeling_repayment`, `capital_party`, `other_party`, `contract_short_name`, `remittance_object`, `financial_type`)
VALUES ('1', '1', '3', '2016-04-01 00:00:00', 'H73500', '测试合同', '1', '1', '91', '2016-07-01 00:00:00', '5', '0', '1', '90', '2', '74a9ce4d-cafc-407d-b013-987077541bdc', '2d380fe1-7157-490d-9474-12c5a9901e29', '1', '1', '1', '1', '0', '1', NULL, NULL, NULL, NULL, '1', '0', '0', '5', '(principal+interest)*0.05/100*overdueDay', '123.00', '23.00', NULL, NULL, '2017-05-11 14:39:40', '1', '1', 'outstandingPrincipal+outstandingOverdueCharges', '30', '0', 'outstandingPrincipal', 'outstandingInterest', 'outstandingPenaltyInterest', '', '-1', '0', '[]', '[]', '1', NULL, NULL, NULL, NULL, '0');

SET FOREIGN_KEY_CHECKS=1;
