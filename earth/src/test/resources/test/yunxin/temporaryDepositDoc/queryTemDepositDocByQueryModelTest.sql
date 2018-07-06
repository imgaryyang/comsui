SET FOREIGN_KEY_CHECKS=0;
	
DELETE FROM `temporary_deposit_doc`;

INSERT INTO `temporary_deposit_doc`(`id`,`uuid`,`doc_no`,`total_amount`,`balance`,`alive_status`,`status`,`owner_name`,`owner_type`,`financial_contract_uuid`,`created_time`)
VALUES
	('1','uuid_1','doc_co_1',1000.00,200.00,0,1,'owner_name_1',0,'financial_contract_uuid_1','2017-10-30 00:00:00'),
	('2','uuid_2','doc_co_2',2000.00,2000.00,0,0,'owner_name_2',0,'financial_contract_uuid_1','2017-10-31 00:00:00'),
	('3','uuid_3','doc_co_3',3000.00,1200.00,0,1,'owner_name_3',0,'financial_contract_uuid_1','2017-09-30 00:00:00'),
	('4','uuid_4','doc_co_4',4000.00,4000.00,0,0,'owner_name_4',0,'financial_contract_uuid_1','2017-10-03 00:00:00'),
	('5','uuid_5','doc_co_5',5000.00,5000.00,0,0,'owner_name_5',0,'financial_contract_uuid_1','2017-10-30 01:00:00'),
	('6','uuid_6','doc_co_6',6000.00,1200.00,0,1,'owner_name_6',0,'financial_contract_uuid_2','2017-10-30 00:00:00'),
	('7','uuid_7','doc_co_7',70000.00,70000.00,0,0,'company_name_1',1,'financial_contract_uuid_1','2017-10-30 00:00:00'),
	('8','uuid_8','doc_co_8',90000.00,0.00,0,2,'company_name_2',1,'financial_contract_uuid_1','2017-10-11 00:00:00'),
	('9','uuid_9','doc_co_9',80000.00,62000.00,0,0,'company_name_3',1,'financial_contract_uuid_1','2017-10-30 00:00:00'),
	('10','uuid_10','doc_co_10',100000.00,100000.00,0,0,'company_name_4',1,'financial_contract_uuid_2','2017-10-17 00:00:00');
										
SET FOREIGN_KEY_CHECKS=1;