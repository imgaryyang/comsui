delete from `t_voucher`;
delete from `source_document_resource`;
delete from `source_document`;

INSERT INTO `t_voucher` (`id`, `uuid`, `voucher_no`, `source_document_uuid`, `financial_contract_uuid`, `amount`, `status`, `first_type`, `first_no`, `second_type`, `second_no`, `receivable_account_no`, `payment_account_no`, `payment_name`, `payment_bank`, `check_state`, `comment`, `create_time`)
VALUES
(1, 'f6129d01-b063-11e6-bedc-00163e002839', 'CZ2743ED6CF5506163', '5dfe45e6-4ec8-4728-9e56-0c1a2123ac68', 'db36ecc9-d80c-4350-bd0d-59b1139d550d', 11000.00, 2, 'enum.voucher-source.business-payment-voucher', '4b1ac11e-9bfe-49ac-a736-29ed7db09036', 'enum.voucher-type.pay', 'bank_transaction_no_10000', '6600000000000000001', '10001', 'counter_name', 'account_account_name', 0, '测试', '2016-08-31 20:07:23');


INSERT INTO `source_document_resource` (`id`, `uuid`, `source_document_uuid`, `batch_no`, `path`, `status`, `voucher_no`)
VALUES
(1, 'b28056a5-24af-4c1a-9e17-01199bfcc45d', '5dfe45e6-4ec8-4728-9e56-0c1a2123ac68', '4b1ac11e-9bfe-49ac-a736-29ed7db09036', '/data/webapps/tomcat-earth/file/2016-10-15/3c81610f-2c5d-47cf-bbb4-b337b8336f93.pdf', true, 'CZ2743ED6CF5506163');


INSERT INTO `source_document` (`id`, `company_id`, `source_document_uuid`, `source_document_type`, `create_time`, `issued_time`, `source_document_status`, `source_account_side`, `booking_amount`, `outlier_document_uuid`, `outlier_trade_time`, `outlier_counter_party_account`, `outlier_counter_party_name`, `outlier_account`, `outlie_account_name`, `outlier_account_id`, `outlier_company_id`, `outlier_serial_global_identity`, `outlier_memo`, `outlier_amount`, `outlier_settlement_modes`, `outlier_breif`, `outlier_account_side`, `appendix`, `first_outlier_doc_type`, `second_outlier_doc_type`, `third_outlier_doc_type`, `currency_type`, `audit_status`, `first_party_id`, `second_party_id`, `virtual_account_uuid`, `first_account_id`, `second_account_id`, `third_account_id`, `excute_status`, `excute_result`, `related_contract_uuid`, `financial_contract_uuid`, `source_document_no`, `first_party_type`, `first_party_name`, `virtual_account_no`, `last_modified_time`)
VALUES
(3766, 1, '5dfe45e6-4ec8-4728-9e56-0c1a2123ac68', 1, '2016-10-14 23:44:12', NULL, 0, 1, 0.00, 'a38ef7cf-9222-11e6-9aaf-00163e002839', '2016-10-12 11:07:17', '6227007200341122450', '韩梅梅', '600000000001', NULL, NULL, 1, '20161015123456', '韩梅梅主动付款501.00', 501.00, 3, '韩梅梅主动付款501.00', 1, NULL, 'FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS', 'SND_LIABILITIES_INDEPENDENT_CUSTOMER_DEPOSIT', '', NULL, 2, 'd0bb49d4-ee20-480e-ae0c-4f7696689750', '1', 'f1323b8c-a2e3-4fd5-9506-7aa1ef356a47', '50000', '50000.01', '', 2, 1, '0fe2e467-23ed-4e2b-87f2-70c687ab87ee', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '', '0', '王林广', 'VACC274F6DF8DA53A5D1', '2016-10-27 21:29:17');
