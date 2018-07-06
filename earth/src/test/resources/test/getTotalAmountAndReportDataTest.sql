SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM app;
DELETE FROM transaction_record;
DELETE FROM rent_order;
DELETE FROM transaction_record_log;
DELETE FROM contract;
DELETE FROM customer;
DELETE FROM payment_institution;

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`) VALUES
  ('1', 'xiaoyu', '70991db75ebb24fa0993f4fa25775022', b'0', 'http://beta.demo2do.com/jupiter/', '寓见', '5'),
  ('2', 'youpark', '123456', b'0', '', '优帕克', '4');

INSERT INTO `payment_institution` (`id`, `alias`, `code`, `day_upper_limit`, `name`, `once_upper_limit`) VALUES
  ('1', 'umpay', 'umpay', NULL, '联动优势', NULL),
  ('2', 'alipay', 'alipay', NULL, '支付宝', NULL),
  ('3', 'directbank.icbc', 'directbank.icbc', NULL, '工行银企互联', NULL);

INSERT INTO `transaction_record` (`id`, `card_no`, `channel`, `last_modified_time`, `merchant_payment_no`, `order_no`, `pay_money`, `request_no`, `trade_no`, `transaction_record_status`, `app_id`, `payment_institution_id`)
VALUES
  ('7', NULL, '2', '2015-04-03 12:00:00', NULL, 'KXHY1#1803-20150402-1', 16500.00, NULL, '0', '2', '1', '3'),
  ('8', NULL, '2', '2015-04-03 12:00:00', NULL, 'JAGJ1804-20150402-12', 13500.00, NULL, '0', '2', '1', '3'),
  ('9', NULL, '2', '2015-04-03 12:00:00', NULL, 'HNHY3#602-20150402-6', 12600.00, NULL, '0', '2', '1', '3'),
  ('10', NULL, '2', '2015-04-03 12:00:00', NULL, 'HQHY1#33B-20150402-11', 17000.00, NULL, '0', '2', '1', '3'),
  ('11', NULL, '2', '2015-04-03 12:00:00', NULL, 'JAHJ3#2203-20150402-10', 20800.00, NULL, '0', '2', '1', '3');

INSERT INTO `rent_order` (`id`, `due_date`, `end_date`, `late_fee`, `order_no`, `order_status`, `paid_rent`, `payout_time`, `is_settled`, `start_date`, `total_rent`, `contract_id`, `customer_id`, `repayment_type`, `user_upload_param`, `unique_particle_id`, `transfer_status`, `modify_time`, `unique_bill_id`)
VALUES
  ('107', '2015-04-01', NULL, NULL, 'KXHY1#1803-20150402-1', '3', 16500.00, '2015-04-03 16:45:24', b'0', NULL, 16500.00,
   '13', '106', '1', NULL, NULL, '0', NULL, NULL),
  ('108', '2015-05-01', NULL, NULL, 'JAGJ1804-20150402-12', '3', 16500.00, '2015-05-13 15:17:34', b'0', NULL, 16500.00, '13', '106', '0', NULL, NULL, '0', NULL, NULL),
  ('109', '2015-06-01', NULL, NULL, 'HNHY3#602-20150402-6', '3', 16500.00, '2015-05-27 16:45:49', b'0', NULL, 16500.00, '13', '106', '0', NULL, NULL, '0', NULL, NULL),
  ('110', '2015-07-01', NULL, NULL, 'HQHY1#33B-20150402-11', '3', 16500.00, '2015-06-23 17:04:30', b'0', NULL, 16500.00, '13', '106', '0', NULL, NULL, '0', NULL, NULL),
  ('111', '2015-08-01', NULL, NULL, 'JAHJ3#2203-20150402-10', '0', 0.00, NULL, b'0', NULL, 16500.00, '13', '106', '0', NULL, NULL, '0', NULL, NULL),
  ('112', '2015-09-01', NULL, NULL, 'KXHY1#1803-20150402-6', '0', 0.00, NULL, b'0', NULL, 16500.00, '13', '106', '0', NULL, NULL, '0', NULL, NULL),
  ('113', '2015-10-01', NULL, NULL, 'KXHY1#1803-20150402-7', '0', 0.00, NULL, b'0', NULL, 16500.00, '13', '106', '0', NULL, NULL, '0', NULL, NULL),
  ('114', '2015-11-01', NULL, NULL, 'KXHY1#1803-20150402-8', '0', 0.00, NULL, b'0', NULL, 16500.00, '13', '106', '0', NULL, NULL, '0', NULL, NULL),
  ('115', '2015-12-01', NULL, NULL, 'KXHY1#1803-20150402-9', '0', 0.00, NULL, b'0', NULL, 16500.00, '13', '106', '0', NULL, NULL, '0', NULL, NULL),
  ('116', '2016-01-01', NULL, NULL, 'KXHY1#1803-20150402-10', '0', 0.00, NULL, b'0', NULL, 16500.00, '13', '106', '0', NULL, NULL, '0', NULL, NULL),
  ('117', '2016-02-01', NULL, NULL, 'KXHY1#1803-20150402-11', '0', 0.00, NULL, b'0', NULL, 16500.00, '13', '106', '0', NULL, NULL, '0', NULL, NULL);

INSERT INTO `transaction_record_log` (`id`, `modified_time`, `operator_id`, `transaction_record_operate_status`, `transaction_record_id`)
VALUES
  ('5', '2015-04-13 19:27:02', '130', '0', '22'),
  ('6', '2015-04-13 20:16:04', '126', '0', '23'),
  ('7', '2015-04-13 20:17:11', '6', '1', '23'),
  ('8', '2015-04-13 20:19:06', '126', '0', '24'),
  ('9', '2015-04-13 20:19:45', '8', '1', '24'),
  ('10', '2015-04-13 20:24:07', '126', '0', '25'),
  ('11', '2015-04-13 20:24:44', '10', '1', '25'),
  ('12', '2015-04-13 20:29:12', '126', '0', '26'),
  ('13', '2015-04-13 20:29:31', '12', '1', '26'),
  ('14', '2015-04-13 20:32:29', '126', '0', '27');

INSERT INTO `contract` (`id`, `begin_date`, `contract_no`, `contract_status`, `deposit`, `end_date`, `interrupt_date`, `interrupt_reason`, `month_fee`, `non_deposit_guarantee_way`, `payment_instrument`, `renewal`, `url`, `app_id`, `customer_id`, `house_id`, `transfer_status`)
VALUES
  ('13', '2015-04-01', 'KXHY1#1803', '0', 33000.00, '2016-03-31', NULL, '', 16500.00, b'0', '0', NULL, NULL, '2', '106',
   '13', '0'),
  ('14', '2014-11-01', 'HNHY3#602', '0', 25200.00, '2015-10-31', NULL, '', 12600.00, b'0', '0', NULL, NULL, '2', '107',
   '14', '0'),
  ('15', '2014-06-01', 'HQHY1#33B', '0', 34000.00, '2015-11-30', NULL, '', 17000.00, b'0', '0', NULL, NULL, '2', '108',
   '15', '0'),
  ('16', '2014-05-01', 'JAGJ1804', '0', 27000.00, '2016-04-30', NULL, '', 13500.00, b'0', '0', NULL, NULL, '2', '109',
   '16', '0'),
  ('17', '2014-05-14', 'JAFJ4#26A', '0', 35600.00, '2015-11-13', NULL, '', 17800.00, b'0', '0', NULL, NULL, '2', '110',
   '17', '0'),
  ('18', '2014-05-08', 'JAHJ4#1203', '0', 33000.00, '2015-11-07', NULL, '', 16500.00, b'0', '0', NULL, NULL, '2', '111',
   '18', '0'),
  ('57', '2014-04-13', 'KXHY9#2601', '0', 38000.00, '2015-10-12', NULL, '', 19500.00, b'0', '0', NULL, NULL, '2', '118',
   '60', '0'),
  ('58', '2014-07-01', 'JAHJ3#2203', '0', 41600.00, '2015-12-30', NULL, '', 20800.00, b'0', '0', NULL, NULL, '2', '119',
   '61', '0');

INSERT INTO `customer` (`id`, `account`, `city`, `mobile`, `name`, `province`, `source`, `app_id`) VALUES
  ('-1', NULL, NULL, NULL, '小寓预付款承租方', NULL, '寓见', '1'),
  ('106', NULL, NULL, NULL, '上海化耀国际贸易有限公司', NULL, '优帕克', '2'),
  ('107', NULL, NULL, NULL, '吴林飞', NULL, '优帕克', '2'),
  ('108', NULL, NULL, NULL, 'R.B', NULL, '优帕克', '2'),
  ('109', NULL, NULL, NULL, 'OWENBOYDALEXANDER', NULL, '优帕克', '2'),
  ('110', NULL, NULL, NULL, 'QM', NULL, '优帕克', '2'),
  ('111', NULL, NULL, NULL, 'eileen', NULL, '优帕克', '2'),
  ('118', NULL, NULL, NULL, '目黑克彦', NULL, '优帕克', '2'),
  ('119', NULL, NULL, NULL, '冼的坤', NULL, '优帕克', '2'),
  ('120', NULL, NULL, NULL, 'Javier Diaz', NULL, '优帕克', '2'),
  ('121', NULL, NULL, NULL, 'Michael Charles Madely', NULL, '优帕克', '2'),
  ('122', NULL, NULL, NULL, 'Missart Reynoso Agustina', NULL, '优帕克', '2'),
  ('124', NULL, NULL, NULL, '参环国际货运代理（上海）有限公司', NULL, '优帕克', '2');

SET FOREIGN_KEY_CHECKS = 1;