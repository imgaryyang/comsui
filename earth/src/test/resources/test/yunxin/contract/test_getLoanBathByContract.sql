SET FOREIGN_KEY_CHECKS=0;

delete from asset_package;
delete from loan_batch;


INSERT INTO `asset_package` (`id`, `is_available`, `create_time`, `contract_id`, `asset_package_no`, `financial_contract_id`, `loan_batch_id`, `customer_uuid`)
VALUES 
('1', b'1', '2016-05-27 18:27:16', '1', NULL, '5', '1', '81cbfcab-aac4-4c92-9580-0b1d3d5b768f');
 
INSERT INTO  `loan_batch` (`id`, `is_available`, `code`, `create_time`, `financial_contract_id`, `last_modified_time`, `loan_date`, `loan_batch_uuid`, `request_batch_no`)
VALUES 
('1', b'1', 'nfqtest001 20160527 18:27:477', '2016-05-27 18:27:16', '5', '2016-06-16 15:45:54', '2016-06-16 15:45:54', '0fdd0eac-16e7-4045-8d32-e4d1fe06772e', NULL);