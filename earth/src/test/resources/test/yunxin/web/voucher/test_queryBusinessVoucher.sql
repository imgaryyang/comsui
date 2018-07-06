delete from `t_voucher`;

INSERT INTO `t_voucher` (`id`, `uuid`, `voucher_no`, `source_document_uuid`, `financial_contract_uuid`, `amount`, `status`, `first_type`, `first_no`, `second_type`, `second_no`, `receivable_account_no`, `payment_account_no`, `payment_name`, `payment_bank`, `check_state`, `comment`, `create_time`, `contract_no`, `last_modified_time`) 
VALUES 
	('1', 'f6129d01-b063-11e6-bedc-00163e002839', 'CZ2743ED6CF5506163', '5dfe45e6-4ec8-4728-9e56-0c1a2123ac68', 'db36ecc9-d80c-4350-bd0d-59b1139d550d', '11000.00', '1', 'enum.voucher-source.business-payment-voucher', '4b1ac11e-9bfe-49ac-a736-29ed7db09036', 'enum.voucher-type.pay', 'bank_transaction_no_10000', '6600000000000000001', '10001', 'counter_name', 'account_account_name', '0', '测试', '2016-08-31 20:07:23', NULL, NULL);
