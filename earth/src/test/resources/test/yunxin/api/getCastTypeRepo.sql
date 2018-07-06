

SET FOREIGN_KEY_CHECKS=0;

DELETE FROM `asset_set`;
DELETE FROM `journal_voucher`;
delete from `financial_contract`;
delete from `contract`;
delete from `t_deduct_application`;
delete from `t_deduct_application_detail`;


INSERT INTO `journal_voucher` (`id`, `account_side`, `bank_identity`, `billing_plan_uuid`, `booking_amount`, `business_voucher_type_uuid`, `business_voucher_uuid`, `cash_flow_amount`, `cash_flow_breif`, `cash_flow_channel_type`, `cash_flow_serial_no`, `cash_flow_uuid`, `checking_level`, `company_id`, `completeness`, `counter_party_account`, `counter_party_name`, `journal_voucher_uuid`, `notification_identity`, `notification_memo`, `notification_record_uuid`, `notified_date`, `settlement_modes`, `source_document_amount`, `source_document_breif`, `source_document_cash_flow_serial_no`, `source_document_counter_party_uuid`, `source_document_identity`, `source_document_uuid`, `status`, `trade_time`, `batch_uuid`, `created_date`, `source_document_counter_party_account`, `source_document_counter_party_name`, `issued_time`, `journal_voucher_type`, `counter_account_type`, `related_bill_contract_info_lv_1`, `related_bill_contract_info_lv_2`, `related_bill_contract_info_lv_3`, `cash_flow_account_info`, `journal_voucher_no`, `related_bill_contract_no_lv_1`, `related_bill_contract_no_lv_2`, `related_bill_contract_no_lv_3`, `related_bill_contract_no_lv_4`, `source_document_no`, `appendix`, `last_modified_time`, `local_party_account`, `local_party_name`, `source_document_local_party_account`, `source_document_local_party_name`, `second_journal_voucher_type`, `third_journal_voucher_type`)
VALUES
	(65289, 1, NULL, '3d88fd8f-30e1-451b-9991-ce201774cfa6', 6001.00, '', '', NULL, NULL, NULL, NULL, NULL, 0, 1, NULL, 'VACC27564C436FC93CE8', '测试员', '85196442-79e3-45e7-9460-d02345719fc7', NULL, NULL, NULL, NULL, NULL, 6001.00, NULL, NULL, NULL, '4a23041c-6fc5-4076-bf88-12ade5bb7bff', 'b7e1237f-35ac-4725-97cd-2d5066f670c7', 2, NULL, NULL, '2016-11-24 21:00:12', '1001133419006708197xxx', '上海拍拍贷金融信息服务有限公司xxx', NULL, 10, 1, 'd2812bc5-5057-4a91-b3fd-9019506f0499', 'ee112ac3-cbaf-4efd-ae82-820a248f9249', '3d88fd8f-30e1-451b-9991-ce201774cfa6', 'a02c02b9-6f98-11e6-bf08-00163e002839', 'd2b1e0bc-04f6-4153-b2e5-e36d956af1a2', '拍拍贷测试', 'wwtest--6001bcd81a54-2762-44ee-92ba-97e8a7c90f3f0', 'ZC27564C4202E090A6', 'JS27564C8489112ABE', 'V27564C436FD4E144', NULL, '2016-12-15 20:53:03', '0000008840', '测试员', '600000000001', '云南国际信托有限公司', NULL, NULL);

SET FOREIGN_KEY_CHECKS=1;