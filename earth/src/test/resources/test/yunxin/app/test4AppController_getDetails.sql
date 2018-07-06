SET FOREIGN_KEY_CHECKS=0;


delete from `app`;
delete from `company`;
delete from `customer`;
delete from `app_account`;
delete from `financial_contract`;


INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`,`create_time`,`last_modify_time`)
VALUES
	('1','xiaoyu',NULL,1,'http://beta.demo2do.com/jupiter/','寓见',1,null,null),
    ('2', 'suidifu', NULL, 1, NULL, '随地付', '2', '2017-09-20 15:47:10', '2017-09-20 15:47:10');

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`, `legal_person`, `business_licence`) 
VALUES 
('1', '上海', 'fullName1', 'shortName1', '11', NULL, NULL),
('2', '杭州', '随地付网络技术公司', '随地付', '00674dbc-251d-4f7f-a823-ad570544b160', '', '');

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`, `customer_style`) 
VALUES 
('1', NULL, NULL, '测试用户1', 'C74211', '1', '81cbfcab-aac4-4c92-9580-0b1d3d5b768f', '1',null);

INSERT INTO `virtual_account` (`id`, `total_balance`, `virtual_account_uuid`, `parent_account_uuid`, `virtual_account_alias`, `virtual_account_no`, `version`, `owner_uuid`, `owner_name`, `fst_level_contract_uuid`, `snd_level_contract_uuid`, `trd_level_contract_uuid`, `create_time`, `last_update_time`, `last_modified_time`, `customer_type`, `virtual_account_status`)
VALUES ('1', '11000000.00', '9126313e-f89d-4222-847c-4e36331cb787', NULL, '', 'VACC27438CADB442A6A0', '45b52af0-7a1a-4c39-beac-4f32a2365986', '81cbfcab-aac4-4c92-9580-0b1d3d5b768f', 'yqb', 'db36ecc9-d80c-4350-bd0d-59b1139d550d', NULL, '', '2016-08-24 21:27:10', '2017-09-19 10:19:53', '2016-10-03 15:12:45', NULL, '0');


INSERT INTO `app_account` (`id`, `uuid`, `bank_name`, `account_name`, `account_no`, `app_account_active_status`, `app_id`, `virtual_account_uuid`, `bank_card_status`, `id_card_num`, `bank_code`, `province`, `province_code`, `city`, `city_code`) 
VALUES 
('1', 'uuid_1', '银行', '农分期', '10002', '0', '1','016cce27-f37f-4d68-a0a1-23beb6966141', '1', NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO`financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`, `day_of_month`, `pay_for_go`, `repurchase_principal_algorithm`, `repurchase_interest_algorithm`, `repurchase_penalty_algorithm`, `repurchase_other_charges_algorithm`, `repayment_check_days`, `repurchase_cycle`, `days_of_cycle`, `temporary_repurchases`, `allow_freewheeling_repayment`, `capital_party`, `other_party`, `contract_short_name`, `financial_type`, `remittance_object`) 
VALUES 
('1', '1', '0', '2016-06-14 00:00:00', 'ceshi003', 'test006', '1', '1', '2', '2016-06-25 00:00:00', '16', '0', '1', '1', '1', 'c374dc6b-132d-4260-86be-534b153a7464', '92846f20-87e3-49f4-8f90-fe04a72c0484', '1', '1', '1', '0', '0', '0', NULL, NULL, '2', '[\"dd621204-656d-417a-8e2d-aa3d6d68f74d\",\"36ed38cc-16e9-4d42-960b-20a985e9950f\"]', '1', '0', '0', '0', '(principal+interest)*0.05/100*overdueDay', '123.00', '23.00', NULL, NULL, '2017-06-12 10:00:53', '1', '1', 'outstandingPrincipal+outstandingOverdueCharges', '2', '0', 'outstandingPrincipal', 'outstandingPenaltyInterest+6', 'outstandingPenaltyInterest+6', '', '-1', '1', '[13]', '[{\"effectEndDate\":\"2017-06-30\",\"effectStartDate\":\"2017-06-01\",\"repurchaseDate\":\"2017-06-14\",\"repurchaseUuid\":\"34ab059d-8b49-4bde-808b-625fab801063\"}]', '0', NULL, NULL, NULL, NULL, NULL),
('2', '0', '0', '2017-07-04 00:00:00', 'HA9111', 'YIDJNDO', '1', '1', '5', '2019-07-01 00:00:00', '239', '0', '4', '4', NULL, 'c2e39237-a147-4dd8-94eb-76d65ff718ad', '888194c0-cdc3-4180-bdc4-50a29b2528b3', '0', '0', '0', '0', '0', '0', '1.00', '100.00', '0', 'null', '1', '0', '1', '3', '', NULL, NULL, NULL, '2017-07-04 16:32:22', '2017-09-20 15:52:20', '2', NULL, '', NULL, '0', '', '', '', '', '-1', NULL, NULL, NULL, '0', '[2]', '[2]', 'll', NULL, '0');



	
SET FOREIGN_KEY_CHECKS=1;