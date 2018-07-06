SET FOREIGN_KEY_CHECKS=0;
	
DELETE FROM `source_document_detail`;
DELETE FROM `asset_set`;

INSERT INTO `source_document_detail` (`id`, `uuid`, `source_document_uuid`, `contract_unique_id`, `repayment_plan_no`, `amount`, `status`, `first_type`, `first_no`, `second_type`, `second_no`, `payer`, `receivable_account_no`, `payment_account_no`, `payment_name`, `payment_bank`, `check_state`, `comment`, `financial_contract_uuid`, `principal`, `interest`, `service_charge`, `maintenance_charge`)
VALUES
	(1, 'detail_uuid_1', 'caa3f455-f0fe-4871-9edd-551263f96510', 'ca08a2ff-6585-49d5-9902-d4d775be7d0e', 'repayment_plan_no_1', 899.00, 0, 'enum.voucher-source.business-payment-voucher', 'temporary_deposit_doc_1', 'enum.voucher-type.pay', '3379e0ce-5b34-4090-a42a-ddf541fa3c09', 0, '600000000001', '1001133419006708197', '上海拍拍贷金融信息服务有限公司', 'bank', 2, NULL, 'd2812bc5-5057-4a91-b3fd-9019506f0499','800.00','60.00','20.00','10.00'),
	(2, 'detail_uuid_2', 'caa3f455-f0fe-4871-9edd-551263f96510', '8b99fc34-b23c-4428-bde7-c20501a9233b', 'repayment_plan_no_1', 1008.00, 0, 'enum.voucher-source.business-payment-voucher', 'temporary_deposit_doc_1', 'enum.voucher-type.pay', '3379e0ce-5b34-4090-a42a-ddf541fa3c09', 0, '600000000001', '1001133419006708197', '上海拍拍贷金融信息服务有限公司', 'bank', 2, NULL, 'd2812bc5-5057-4a91-b3fd-9019506f0499','900.00','100.00','0.00','0.00'),
	(3, 'detail_uuid_3', 'caa3f455-f0fe-4871-9edd-551263f96510', '17a72e2f-dc29-4e0d-b4c3-0b694ec51f6b', 'repayment_plan_no_2', 711.00, 0, 'enum.voucher-source.business-payment-voucher', 'temporary_deposit_doc_1', 'enum.voucher-type.pay', '3379e0ce-5b34-4090-a42a-ddf541fa3c09', 0, '600000000001', '1001133419006708197', '上海拍拍贷金融信息服务有限公司', 'bank', 2, NULL, 'd2812bc5-5057-4a91-b3fd-9019506f0499','600.00','90.00','0.00','10.00'),
	(4, 'detail_uuid_4', 'caa3f455-f0fe-4871-9edd-551263f96510', 'no_unique_id', 'repayment_plan_no_3', 711.00, 0, 'enum.voucher-source.business-payment-voucher', 'temporary_deposit_doc_1', 'enum.voucher-type.pay', '3379e0ce-5b34-4090-a42a-ddf541fa3c09', 0, '600000000001', '1001133419006708197', '上海拍拍贷金融信息服务有限公司', 'bank', 2, NULL, 'd2812bc5-5057-4a91-b3fd-9019506f0499','600.00','90.00','0.00','10.00');

INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`,`version_no`,`active_status`) 
VALUES 
('5', '1', '0', '2434.80', '0.00', '2400.00', '2400.00', '2016-05-17 00:00:00', NULL, '0.00', '0', '1', 0, '2016-06-15 22:13:47', '5df794c5-5362-497f-adcb-65b03f74e425', '2016-06-15 22:04:59', '2016-06-15 23:24:31', NULL, 'repayment_plan_no_1', '325', NULL, '1',1,0),
('6', '1', '0', '2130.45', '0.00', '2100.00', '2100.00', '2016-06-18 00:00:00', NULL, '0.00', '0', '1', 0, '2016-06-15 22:13:48', 'efd85258-ce01-4adb-ab4d-168e42d1e51b', '2016-06-15 22:04:59', '2016-06-15 23:24:31', NULL, 'repayment_plan_no_2', '325', NULL, '2',1,0),
('7', '1', '0', '2000.45', '0.00', '2100.00', '2100.00', '2016-05-17', NULL, '0.00', '0', '1', 0, '2016-06-15 22:13:48', 'efd85258-ce01-4adb-ab4d-168e42d1e51b', '2016-06-15 22:04:59', '2016-06-15 23:24:31', NULL, 'repayment_plan_no_3', '325', NULL, '3',1,1);

SET FOREIGN_KEY_CHECKS=1;