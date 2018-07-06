delete from `t_interface_voucher_log`;
delete from `t_interface_active_voucher_log`;


INSERT INTO `t_interface_voucher_log` (`id`, `request_no`, `transaction_type`, `business_voucher_no`, `voucher_type`, `voucher_amount`, `financial_contract_no`, `receivable_account_no`, `payment_account_no`, `payment_name`, `payment_bank`, `bank_transaction_no`, `create_time`, `ip`)
VALUES
(37, '9b2ae74d-290f-4d30-b45c-96c2d342477b', 0, NULL, 0, 3000.00, 'G00003', '66000000000000000011', '10001', '10001', 'account_account_name', 'bank_transaction_no_12', '2016-08-26 01:15:59', '101.231.215.146');



INSERT INTO `t_interface_active_voucher_log` (`id`, `request_no`, `transaction_type`, `voucher_type`, `unique_id`, `contract_no`, `repayment_plan_no`, `voucher_amount`, `receivable_account_no`, `payment_account_no`, `payment_name`, `payment_bank`, `bank_transaction_no`, `create_time`, `ip`)
VALUES
(86, '8b444fe7-9ecb-4750-bf46-bce8f1f34a9a', 0, 6, '9a6d98f5-dc84-47e9-bd69-4dd6ed7dc148', NULL, '[\"ZC274A16A41A0E4A20\"]', 1100.00, 'd0503298-e890-425a-4444444', '622222123456', '测试主动付款', '平安银行杭州分行', '201609291130', '2016-09-29 13:23:33', '127.0.0.1');
