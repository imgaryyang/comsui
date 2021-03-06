SET FOREIGN_KEY_CHECKS=0;

delete from `rent_order`;
delete from `asset_set`;
delete from `financial_contract`;
delete from `account`;
delete from `contract`;
delete from `asset_valuation_detail`;
delete from `contract_account`;
delete from `asset_package`;
delete from `transfer_application`;
delete from `batch_pay_record`;

INSERT INTO `financial_contract` (`adva_repayment_term`,`id`, `adva_matuterm`, `contract_no`, `app_id`, `company_id`,`adva_repo_term`) VALUES 
('0','1', 3, 'YX_AMT_001', 1, 1, 30);

INSERT INTO `asset_package` (`id`, `is_available`, `create_time`, `contract_id`, `asset_package_no`, `financial_contract_id`) VALUES 
(1, 1, now(), 1, 'no', 1);

INSERT INTO `account` (`id`, `account_name`, `account_no`, `alias`, `attr`) VALUES 
(1, 'account_name_1', 'account_no_1', NULL, NULL);

INSERT INTO `asset_set` (`id`, `asset_fair_value`,`asset_initial_value`, `asset_recycle_date`, `asset_status`, `asset_uuid`, `create_time`, `last_modified_time`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`) VALUES 
('1', 1010,1000, '2015-10-19 13:34:35', 0, 'asset_uuid_1', '2015-10-19 13:34:35', '2015-10-19 13:34:35', 'DKHD-001-01', 1, '2015-10-19 13:34:35');

INSERT INTO `contract` (`id`, `begin_date`, `contract_no`,`end_date`,`app_id`, `customer_id`, `house_id`,`actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`) VALUES 
(1, '2015-10-19', 'DKHD-001', '2016-10-19', 1, 1, 1, NULL, '2015-10-19 13:34:35', 0.001, 1, 1, 12, 0, 1000, 0.0001);


INSERT INTO `rent_order` (`id`, `due_date`, `order_no`, `payout_time`, `total_rent`, `customer_id`, `user_upload_param`, `modify_time`, `asset_set_id`, `create_time`,`clearing_status`,`executing_settling_status`,`financial_contract_id`) VALUES
('2', Date(NOW()), 'DKHD-001-01-20160308', '2016-10-19 13:34:35',1000, '1','', '2016-10-19', 1, '2015-10-19',0,0,1),
('3', '2015-03-18', 'DKHD-001-02-20160308', '2016-10-19 13:34:35',1000, '1','', '2016-10-19', 1, '2015-10-19',0,0,1);

INSERT INTO `asset_valuation_detail` (`id`, `amount`, `asset_value_date`, `created_date`, `subject`, `asset_set_id`) VALUES 
('1', '1000.00', '2016-03-14 19:46:38', '2016-03-14', '0', '1'),
('2', '10.00', '2016-03-15 19:47:16', '2016-03-15', '1', '1'),
('3', '1000.00', '2016-03-14 19:46:38', '2016-03-14', '0', '2');

INSERT INTO `contract_account` (`id`, `pay_ac_no`, `bankcard_type`, `contract_id`, `payer_name`) VALUES 
(1, 'pay_ac_no_1', 0, 1, 'payer_name_1');

INSERT INTO `transfer_application` (`id`, `amount`,`creator_id`, `batch_pay_record_id`, `comment`, `create_time` , `deduct_status`, `executing_deduct_status`, `deduct_time`, `last_modified_time`,  `contract_account_id`,`order_id` ) VALUES 
(1, '1000', 1, 5, NULL, '2015-10-10', 0, 3, '2015-10-10', NULL, 1,2),
(3, '1000', 1, 3, NULL, '2015-10-11', 0, 0, '2015-10-11', NULL, 1,2),
(4, '2000', 1, 2, NULL, '2015-10-12', 0, 0, '2015-10-12', NULL, 1,2),
(5, '1000', 1, 1, NULL, '2015-10-13', 0, 0, '2015-10-13', NULL, 1,3);


SET FOREIGN_KEY_CHECKS=1;
