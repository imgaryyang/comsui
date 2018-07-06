delete from `rent_order`;
delete from `customer`;

INSERT INTO `rent_order` (`id`, `order_type`, `due_date`, `order_no`, `payout_time`, `total_rent`, `customer_id`, `user_upload_param`, `modify_time`, `financial_contract_id`, `repayment_bill_id`, `asset_set_id`, `create_time`, `clearing_status`, `executing_settling_status`, `comment`) 
				VALUES   ('1', '0', '2015-10-19', 'DKHD-001-01-20160307', '2016-10-19 13:34:35', '1000.00', '1', '[3]', '2016-10-19 00:00:00', '1', NULL, '1', '2015-10-19 00:00:00', '1', '2', NULL);
INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`)
				VALUES ('1', '330683199403062411', NULL, '测试员', '123456', '3', 'd3c8162e-4e8d-4b59-a581-b34e9603ef6d', '0');
