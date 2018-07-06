SET FOREIGN_KEY_CHECKS=0;

delete from `loan_batch`;
delete from `financial_contract`;

INSERT INTO `loan_batch` (`id`, `is_available`, `code`, `create_time`, `financial_contract_id`, `last_modified_time`,
						  `loan_date`, `loan_batch_uuid`) 
				  VALUES ('1',false, 'nfqtest001 20160527 18:27:477', '2016-05-27 18:27:16', '5','2016-06-16 15:45:54',
				          '2016-06-16 15:45:54', '0fdd0eac-16e7-4045-8d32-e4d1fe06772e');
SET FOREIGN_KEY_CHECKS=1;