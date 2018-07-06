SET FOREIGN_KEY_CHECKS=0;

DELETE FROM customer;
DELETE FROM bank;
DELETE FROM province;
DELETE FROM city;
DELETE FROM contract;
DELETE FROM contract_account;
DELETE FROM t_interface_repayment_information_log;
DELETE FROM company;
DELETE FROM app;

INSERT INTO customer (id, app_id)
VALUES 
	(337, 2);
	
INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`)
VALUES
	(1, '上海', '测试金融公司', '测试金融', 'a02c02b9-6f98-11e6-bf08-00163e002839'),
	(4, '杭州', 'yqb', 'yqb', 'a02c08b5-6f98-11e6-bf08-00163e002839');
	
INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`)
VALUES
	(337, 'qyb', NULL, 1, NULL, '测试商户yqb', 4, NULL);

INSERT INTO `contract` (`id`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`) 
VALUES 
('323', '12345678', '2016-04-17', '云信信2016-78-DK(ZQ2016042522479)', NULL, '1', '0.00', '1', '337', '337', NULL, '2016-06-15 22:04:59', '0.1560000000', '0', '0', '3', '2', '1200.00', '0.0005000000');

INSERT INTO `contract_account` (`id`, `pay_ac_no`, `bankcard_type`, `contract_id`, `payer_name`, `bank`, `bind_id`, `id_card_num`, `bank_code`, `province`, `city`, `from_date`, `thru_date`) 
VALUES 
('323', '6217000000000003006', NULL, '323', '测试用户1', '中国工商银行', NULL, NULL, '102', '安徽省', '亳州', '2016-07-13 10:03:25', '2016-07-13 10:21:26'),
('348', 'bankAccount', NULL, '323', '测试用户1', '中国工商银行', NULL, NULL, '102', '安徽省', '亳州', '2016-07-13 10:21:26', '2900-01-01 00:00:00');

INSERT INTO city(id, code, is_deleted, name, province_code)
VALUES 
		(1, '00001', 0, '北京', '001'),
		(2, '00002', 0, '上海', '002');

INSERT INTO province (id, code, is_deleted, name)
VALUES 
		(1, '001', 0, '北京市'),
		(2, '002', 0, '上海市');
		
INSERT INTO bank ( id, bank_code, bank_name)
VALUES 
		(1,'C10102', '测试银行1'),
	   	(2,'C10103', '测试银行2');
	   	
SET FOREIGN_KEY_CHECKS=1;