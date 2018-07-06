SET FOREIGN_KEY_CHECKS=0;

delete from `repurchase_doc`;
delete from `financial_contract`;

INSERT INTO `repurchase_doc` (`id`, `repurchase_doc_uuid`, `financial_contract_uuid`, `amount`, `repo_start_date`, `repo_end_date`, `repo_days`, `creat_time`, `verification_time`, `last_modifed_time`, `contract_id`, `contract_no`, `app_id`, `app_name`, `customer_uuid`, `customer_name`, `executing_asset_set_uuids`, `asset_set_uuids`, `repurchase_status`)
VALUES 
	('1', 'f17dcb19-7163-4ba0-b348-6f8821268ba6', 'uuid1', '2502.00', '2016-11-01', '2016-11-02', '1', '2016-11-01 11:23:27', NULL, '2016-11-01 11:23:27', '39678', '2016-236-DK(745949740360731625ht)号', '3', 'ppd', '645b5b65-44e4-467c-aabb-ed8dbb2b6a82', '王林广', NULL, NULL, NULL),
	('2', '57f13cc3-d251-4513-80e9-f960fb27a7a5', 'uuid1', '2502.00', '2016-11-01', '2016-11-02', '1', '2016-11-01 11:23:40', NULL, '2016-11-01 11:23:40', '39677', '2016-236-DK(27046725700646471ht)号', '3', 'ppd', 'a0ab84f6-a0e9-4dd8-8f04-e6db1971fbbb', '王林广', '2', NULL, '2'),
	('3', '0bdf90f0-bb21-4fc9-974e-31126f4c8a3c', 'uuid1', '516.12', '2016-11-01', '2016-11-02', '1', '2016-11-01 11:23:49', NULL, '2016-11-01 11:23:49', '39676', '2016-236-DK(12838990)号', '3', 'ppd', '95cbde46-f613-457e-9d9a-885c540d3480', '深圳一号', NULL, NULL, '1'),
	('4', 'b8a83954-11e8-42ec-b3ec-3014e5007fd1', 'uuid1', '5147.67', '2016-11-01', '2016-11-02', '1', '2016-11-01 11:24:26', NULL, '2016-11-01 11:24:26', '39674', '2016-236-DK(12771089)号', '3', 'ppd', '03812739-8945-483c-9166-719cc9a4fc6c', '深圳一号', NULL, NULL, '0'),
	('5', '63fa34ea-628c-47e1-8ca1-9d2c39b55972', 'uuid1', '85.80', '2016-10-27', '2016-11-01', '3', '2016-11-04 00:05:00', NULL, '2016-11-04 00:05:00', '1', '2016-78-DK(ZQ2016042522479)', '1', '测试分期', '81cbfcab-aac4-4c92-9580-0b1d3d5b768f', '测试用户1', NULL, NULL, '0'),
	('6', '0478fa56-8c14-4f08-9eba-3c6809d03255', 'uuid2', '85.80', '2016-09-30', '2016-10-05', '3', '2016-11-04 00:05:00', NULL, '2016-11-04 00:05:00', '2', '2016-78-DK(ZQ2016042422395)', '1', '测试分期', '3da51915-6207-43c9-a444-e521faded5b0', '测试用户2', NULL, NULL, '2'),
	('7', 'e767bcc1-21c6-47b4-a729-f0e9a8e5e19c', 'uuid2', '171.60', '2016-10-28', '2016-11-02', '3', '2016-11-04 00:05:00', NULL, '2016-11-04 00:05:00', '3', '2016-78-DK(ZQ2016042522502)', '1', '测试分期', '2a810605-46c2-4dc5-87c1-7198d735d76a', '测试用户3', NULL, NULL, '1'),
	('8', '01dec8e4-19a9-4e4d-8761-816663fb70d4', 'uuid2', '3645.75', '2016-08-02', '2016-08-05', '3', '2016-11-04 00:05:00', NULL, '2016-11-04 00:05:00', '265', '2016-13-T(test-contract2016062401)', '1', '测试分期', '89dd2bc7-9cd3-4de5-80d3-3b275be21d9c', '测试用户103', NULL, NULL, '0');

INSERT INTO `financial_contract` (`adva_repayment_term`,`id`, `adva_matuterm`, `contract_no`, `app_id`, `company_id`,   `adva_repo_term`, `payment_channel_id`,`financial_contract_uuid`) VALUES 
('0','1', 3, 'YX_AMT_001', 1, 1, 30,1,'uuid1'),
('0','2', 3, 'YX_AMT_002', 2, 1, 30,1,'uuid2');

SET FOREIGN_KEY_CHECKS=1;