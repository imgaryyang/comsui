SET FOREIGN_KEY_CHECKS=0;

delete from  journal_voucher;

delete from source_document_detail;

INSERT INTO `journal_voucher` (`id`, `account_side`, `bank_identity`, `billing_plan_uuid`, `booking_amount`, `business_voucher_type_uuid`, `business_voucher_uuid`, `cash_flow_amount`, `cash_flow_breif`, `cash_flow_channel_type`, `cash_flow_serial_no`, `cash_flow_uuid`, `checking_level`, `company_id`, `completeness`, `counter_party_account`, `counter_party_name`, `journal_voucher_uuid`, `notification_identity`, `notification_memo`, `notification_record_uuid`, `notified_date`, `settlement_modes`, `source_document_amount`, `source_document_breif`, `source_document_cash_flow_serial_no`, `source_document_counter_party_uuid`, `source_document_identity`, `source_document_uuid`, `status`, `trade_time`, `batch_uuid`, `created_date`, `source_document_counter_party_account`, `source_document_counter_party_name`, `issued_time`, `journal_voucher_type`, `counter_account_type`, `related_bill_contract_info_lv_1`, `related_bill_contract_info_lv_2`, `related_bill_contract_info_lv_3`, `cash_flow_account_info`, `journal_voucher_no`, `related_bill_contract_no_lv_1`, `related_bill_contract_no_lv_2`, `related_bill_contract_no_lv_3`, `related_bill_contract_no_lv_4`, `source_document_no`, `appendix`, `last_modified_time`, `second_journal_voucher_type`, `third_journal_voucher_type`, `local_party_account`, `local_party_name`, `source_document_local_party_account`, `source_document_local_party_name`)
VALUES
	(101638, 1, NULL, '9f6f47f0-345b-4b1d-9586-3f045eb7ec23', 704.00, '', '', NULL, NULL, 4, NULL, NULL, 0, 1, NULL, '6217000000000000000', '测试员1', '18e3edd2-f6ed-4d47-9121-72b7c0c4aa7c', NULL, NULL, NULL, NULL, NULL, 704.00, NULL, NULL, NULL, 'c76ca8e5-0d03-4aba-8093-7eed1df4a2c8', 'ae54c710-87ef-48ab-bb96-07310cd74a5d', 1, NULL, NULL, '2016-11-26 13:37:28', '6217000000000000000', '测试员1', NULL, 7, 0, 'd2812bc5-5057-4a91-b3fd-9019506f0499', 'aeb48507-1d44-4890-8b4d-b80371cbb742', '9f6f47f0-345b-4b1d-9586-3f045eb7ec23', 'a02c02b9-6f98-11e6-bf08-00163e002839', 'a8112079-5dd2-47e0-ad1f-c1351bc94166', '拍拍贷测试', 'yq-11-23-011-21fa04a4-0398-4a00-b238-d9f2e3fc22e20', 'ZC27563C3FF4BD47E1', 'JS275651F60EB1F0FD', 'KK275667F8837CA9A0', NULL, '2016-11-26 13:37:28', 0, 0, '600000000001', '云南信托国际有限公司', '600000000001', '云南信托国际有限公司');


	
	INSERT INTO `source_document_detail` (`id`, `uuid`, `source_document_uuid`, `contract_unique_id`, `repayment_plan_no`, `amount`, `status`, `first_type`, `first_no`, `second_type`, `second_no`, `payer`, `receivable_account_no`, `payment_account_no`, `payment_name`, `payment_bank`, `check_state`, `comment`, `financial_contract_uuid`)
VALUES
	(10007, 'ae54c710-87ef-48ab-bb96-07310cd74a5d', 'c76ca8e5-0d03-4aba-8093-7eed1df4a2c8', '9db8ce1f-34d2-4601-b246-8d015d92308c', 'ZC2748790EAB0CED9A', 1100.00, 1, 'enum.voucher-source.business-payment-voucher', '67413fc7-c96a-41bd-950f-1f6259882b48', 'enum.voucher-type.pay', 'bank_transaction_no_10001', 0, '600000000112', '10001', 'counter_name', 'account_account_name', 2, NULL, '9b5da2a4-a2ac-40d3-9fe5-0f0e92045e80');





SET FOREIGN_KEY_CHECKS=1;
