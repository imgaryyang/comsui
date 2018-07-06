SET FOREIGN_KEY_CHECKS=0;
	
DELETE FROM `temporary_deposit_doc`;
DELETE FROM `t_voucher`;
DELETE FROM `source_document_detail`;

INSERT INTO `temporary_deposit_doc`(`id`,`uuid`,`doc_no`,`total_amount`,`balance`,`alive_status`,`status`,`owner_name`,`owner_type`,`financial_contract_uuid`,`created_time`,`voucher_uuid`,`related_contract_uuid`,`source_document_uuid`)
VALUES
	('1','TemDptDoc_uuid_1','doc_co_1',1000.00,900.00,0,1,'owner_name_1',0,'financial_contract_uuid_1','2017-10-30 00:00:00','voucher_uuid_1','relatedContractUuid','source_document_uuid_1');
	
INSERT INTO `t_voucher` (`id`, `uuid`, `voucher_no`, `source_document_uuid`, `financial_contract_uuid`, `amount`, `status`, `first_type`, `first_no`, `second_type`, `second_no`, `receivable_account_no`, `payment_account_no`, `payment_name`, `payment_bank`, `check_state`, `comment`, `create_time`, `contract_no`, `last_modified_time`, `cash_flow_uuid`, `transaction_time`)
VALUES 
	('1', 'voucher_uuid_1', 'voucher_no_1', 'source_document_uuid_1', 'financial_contract_uuid_1', '570.00', '1', 'enum.voucher-source.active_payment_voucher', 'f8dbc062-d00e-4dd6-af03-b1800ed195a2', 'enum.voucher-type.active_pay', '00144407-0b86-4604-b4c3-084047aecd14', NULL, '6214855712106520', '夏侯你惇哥', '中国建设银行', '2', NULL, '2017-05-09 22:40:02', NULL, '2017-05-09 22:40:02', '1b0b4455-34c5-11e7-bf99-00163e002839', '2017-05-09 22:39:23');

SET FOREIGN_KEY_CHECKS=1;