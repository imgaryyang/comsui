delete from `finance_company`;
delete from `company`;
delete from `transfer_application`;
delete from `contract`;
delete from `rent_order`;
delete from `contract_account`;
delete from `app`;
delete from `asset_set`;
delete from `account`;
delete from `financial_contract`;
INSERT INTO `finance_company` (`id`, `company_id`) VALUES 
('1', '1');

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`) VALUES 
('1', '上海', '云南国际信托有限公司', '云南信托'),
('2', '安美途', '安美途', '安美途'),
('3', '杭州', '杭州随地付有限公司', '租房宝');

INSERT INTO `transfer_application` (`id`, `transfer_application_uuid`, `amount`, `batch_pay_record_id`, `comment`, `create_time`, `creator_id`, `deduct_status`, `payment_way`, `executing_deduct_status`, `transfer_application_no`, `order_id`, `deduct_time`, `last_modified_time`, `contract_account_id`, `union_pay_order_no`)
VALUES 
('1', 'd046021fa9604c3a90908a11bf234362', '1210.20', '1', '签名工具类异常！', '2016-06-03 11:39:30', NULL, '0', '0', '0', '2735B6CA6FCADF73', '2', NULL, '2016-06-03 11:39:30', '1', '2735B6CA6FCADF73');

INSERT INTO `rent_order` (`id`, `order_type`, `due_date`, `order_no`, `payout_time`, `total_rent`, `customer_id`, `user_upload_param`, `modify_time`, `financial_contract_id`, `repayment_bill_id`, `asset_set_id`, `create_time`, `clearing_status`, `executing_settling_status`, `comment`) 
VALUES 
('2', '0', '2016-06-03', 'JS2735B6BE27E1EB0D', NULL, '1210.20', '1', '[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18]', '2016-06-03 11:39:30', '1', '02350064170a45fca3e382f477ecf507', '1', '2016-06-03 11:31:25', '0', '1', NULL);

INSERT INTO `contract_account` (`id`, `pay_ac_no`, `bankcard_type`, `contract_id`, `payer_name`, `bank`, `bind_id`, `id_card_num`, `bank_code`, `province`, `city`) 
 VALUES 
 ('1', '6217000000000003006', NULL, '1', '测试用户1', '中国邮政储蓄银行', NULL, NULL, '403', '安徽省', '亳州');

