DELETE FROM `t_interface_active_voucher_log`;


INSERT INTO `t_interface_active_voucher_log` (`id`, `request_no`, `transaction_type`, `voucher_type`, `unique_id`, `contract_no`, `repayment_plan_no`, `voucher_amount`, `receivable_account_no`, `payment_account_no`, `payment_name`, `payment_bank`, `bank_transaction_no`, `create_time`, `ip`) 
VALUES 
('1', 'requestNoTest', '0', '0', 'uniqueId1', 'contractNo1', 'repaymentPlanNo1', '10000.00', 'receivableAccountNo1', 'paymentAccountNo1', 'payment', '中国建设银行', 'transacctionNo1', NULL, '127.0.0.1');
