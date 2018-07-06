delete from `system_operate_log`;
delete from `offline_bill`;

INSERT INTO `offline_bill` (`id`, `amount`, `bank_show_name`, `comment`, `create_time`, `trade_time`, 
							`is_delete`, `status_modified_time`, `last_modified_time`, `offline_bill_no`, `offline_bill_status`, `offline_bill_uuid`, 
							`serial_no`, `payer_account_name`, `payer_account_no`, `financial_contract_uuid`) 
					VALUES ('14', '100.00', '农业银行', 'test', '2016-09-30 11:46:44', '2016-10-27 11:27:28',
							 false, '2016-09-30 11:46:44', '2016-09-30 11:46:44', 'XX274A26D55505CA58', '1', '0da4d513e11b43bd866b1bb031939ff4', 
							'16666666', '张三单 还款方名称	', '6228450012353365123', '0da4d513e11b43bd866b1bb03193dde2');

						  		 
