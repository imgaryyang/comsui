SET FOREIGN_KEY_CHECKS=0;

delete from contract;
delete from app;
delete from customer;
delete from house;
delete from financial_contract;
delete from asset_package;
delete from contract_account;
delete from account;
delete from asset_set;
delete from company;
delete from `source_document_detail`;


INSERT INTO  `contract` (`id`,`uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`,`active_version_no`) 
VALUES 
('325', '1212','ca08a2ff-6585-49d5-9902-d4d775be7d0e','2016-11-12', '云信信2016-76-DK(ZQ2016042522502)', NULL, '1', '0.00', '1', '339', '339', NULL, '2016-06-15 22:04:59', '0.1560000000', '0', '0', '2', '2', '4500.00', '0.0005000000',1),
('327', '1216','xx', '2016-11-12', '云信信2016-78-DK(ZQ2016042522502)', NULL, '1', '0.00', '1', '339', '339', NULL, '2016-06-15 22:04:59', '0.1560000000', '0', '0', '2', '2', '4500.00', '0.0005000000',1),
('328', '1217','17a72e2f-dc29-4e0d-b4c3-0b694ec51f6b', '2016-11-12', '云信信2016-79-DK(ZQ2016042522502)', NULL, '1', '0.00', '1', '339', '339', NULL, '2016-06-15 22:04:59', '0.1560000000', '0', '0', '2', '2', '4500.00', '0.0005000000',1)

;

INSERT INTO `source_document_detail` (`id`, `uuid`, `source_document_uuid`, `contract_unique_id`, `repayment_plan_no`, `amount`, `status`, `first_type`, `first_no`, `second_type`, `second_no`, `payer`, `receivable_account_no`, `payment_account_no`, `payment_name`, `payment_bank`, `check_state`, `comment`, `financial_contract_uuid`)
VALUES
	(1, '06016b5f-0cb6-40b0-9242-0ccec7441a62', 'caa3f455-f0fe-4871-9edd-551263f96510', 'ca08a2ff-6585-49d5-9902-d4d775be7d0e', 'ZC27561A3CA1768095', 8996.00, 0, 'enum.voucher-source.business-payment-voucher', '4316441c-fdcd-4c47-a676-12df988e0391', 'enum.voucher-type.pay', '3379e0ce-5b34-4090-a42a-ddf541fa3c09', 0, '600000000001', '1001133419006708197', '上海拍拍贷金融信息服务有限公司', 'bank', 0, NULL, 'd2812bc5-5057-4a91-b3fd-9019506f0499');
INSERT INTO `source_document_detail` (`id`, `uuid`, `source_document_uuid`, `contract_unique_id`, `repayment_plan_no`, `amount`, `status`, `first_type`, `first_no`, `second_type`, `second_no`, `payer`, `receivable_account_no`, `payment_account_no`, `payment_name`, `payment_bank`, `check_state`, `comment`, `financial_contract_uuid`)
VALUES
	(2, 'eff22cef-2b28-4548-93b9-9e7ecd5b097f', 'caa3f455-f0fe-4871-9edd-551263f96510', '8b99fc34-b23c-4428-bde7-c20501a9233b', 'ZC27561A34EF993089', 10087.00, 0, 'enum.voucher-source.business-payment-voucher', '4316441c-fdcd-4c47-a676-12df988e0391', 'enum.voucher-type.pay', '3379e0ce-5b34-4090-a42a-ddf541fa3c09', 0, '600000000001', '1001133419006708197', '上海拍拍贷金融信息服务有限公司', 'bank', 0, NULL, 'd2812bc5-5057-4a91-b3fd-9019506f0499'),
	(3, '225d4e01-bf37-4d0a-8c33-ddaa5c28f062', 'caa3f455-f0fe-4871-9edd-551263f96510', '17a72e2f-dc29-4e0d-b4c3-0b694ec51f6b', 'ZC27561A3027DA58F7', 7118.00, 0, 'enum.voucher-source.business-payment-voucher', '4316441c-fdcd-4c47-a676-12df988e0391', 'enum.voucher-type.pay', '3379e0ce-5b34-4090-a42a-ddf541fa3c09', 0, '600000000001', '1001133419006708197', '上海拍拍贷金融信息服务有限公司', 'bank', 0, NULL, 'd2812bc5-5057-4a91-b3fd-9019506f0499'),
	(4, 'source_document_detail_for_no_unique_id', 'caa3f455-f0fe-4871-9edd-551263f96510', 'no_unique_id', 'ZC27561AAAAAA', 7118.00, 0, 'enum.voucher-source.business-payment-voucher', '4316441c-fdcd-4c47-a676-12df988e0391', 'enum.voucher-type.pay', '3379e0ce-5b34-4090-a42a-ddf541fa3c09', 0, '600000000001', '1001133419006708197', '上海拍拍贷金融信息服务有限公司', 'bank', 0, NULL, 'd2812bc5-5057-4a91-b3fd-9019506f0499');



INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`)
VALUES 
('1', 'nongfenqi', '', '', '', '农分期', '2', NULL);


INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`) 
VALUES 
('339', NULL, NULL, '测试用户3', 'C30433', '1', '76000e99-c92f-4a45-a58a-39857326cd42');


INSERT INTO `house` (`id`, `address`, `app_id`) VALUES ('339', NULL, '1');

INSERT INTO `financial_contract` (`adva_repayment_term`,`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`) 
VALUES 
('0','1', '1', '7', '2016-06-08 00:00:00', 'DCF-NFQ-LR903', 'sasad', '1', '1', '3', '2016-09-05 00:00:00', '2', '1', '0', '3', '0', 'yunxin_nfq_ledger_book', 'd7b3b325-719a-42af-a129-0ac861f18ebe');


INSERT INTO `asset_package` (`id`, `is_available`, `create_time`, `contract_id`, `asset_package_no`, `financial_contract_id`, `loan_batch_id`) 
VALUES 
('325', NULL, NULL, '325', NULL, '1', '32');


INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`) 
VALUES 
('1', '上海', '云南国际信托有限公司', '云南信托');

INSERT INTO `contract_account` (`id`, `pay_ac_no`, `bankcard_type`, `contract_id`, `payer_name`, `bank`, `bind_id`, `id_card_num`, `bank_code`, `province`, `city`,`thru_date`) 
VALUES 
('325', '6217000000000003015', NULL, '325', '测试用户3', '中国建设银行', NULL, NULL, '105', '安徽省', '亳州','2900-01-01 00:00:00');

INSERT INTO `account` (`id`, `account_name`, `account_no`, `bank_name`, `alias`, `attr`, `scan_cash_flow_switch`, `usb_key_configured`) VALUES ('2', 'name', 'sadas', 'name', NULL, NULL, '\0', '\0');


INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`,`version_no`,`active_status`) 
VALUES 
('5', '1', '0', '2434.80', '0.00', '2400.00', '2400.00', '2016-05-17', NULL, '0.00', '0', '1', 0, '2016-06-15 22:13:47', '5df794c5-5362-497f-adcb-65b03f74e425', '2016-06-15 22:04:59', '2016-06-15 23:24:31', NULL, 'ZC27367D23EFEE063B', '325', NULL, '1',1,0),
('6', '1', '0', '2130.45', '0.00', '2100.00', '2100.00', '2016-06-18 00:00:00', NULL, '0.00', '0', '1', 0, '2016-06-15 22:13:48', 'efd85258-ce01-4adb-ab4d-168e42d1e51b', '2016-06-15 22:04:59', '2016-06-15 23:24:31', NULL, 'ZC27367D23F0180329', '325', NULL, '2',1,0),
('7', '1', '0', '2000.45', '0.00', '2100.00', '2100.00', '2016-05-17', NULL, '0.00', '0', '1', 0, '2016-06-15 22:13:48', 'efd85258-ce01-4adb-ab4d-168e42d1e51b', '2016-06-15 22:04:59', '2016-06-15 23:24:31', NULL, 'ZC27367D23F0180329x', '325', NULL, '3',1,1);

SET FOREIGN_KEY_CHECKS=1;






