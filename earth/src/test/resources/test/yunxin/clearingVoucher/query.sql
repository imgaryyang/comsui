SET FOREIGN_KEY_CHECKS=0;
	delete from `audit_job`;

INSERT INTO `audit_job` (`id`, `uuid`, `audit_job_no`, `financial_contract_uuid`, `capital_account_no`, `payment_channel_uuid`, `payment_channel_service_uuid`, `pg_clearing_account`, `payment_institution`, `audit_job_source`, `account_side`, `start_time`, `end_time`, `audit_result`, `create_time`, `last_modified_time`, `clearing_status`, `merchant_no`, `audit_result_Code`) 
VALUES ('1', 'b2e136c7-f595-4861-86af-f95fcf888d70', 'DZ131269695966961664', 'b6b407fb-de54-4d84-9cba-84ccb2c17fcf', '19014526016005', 'b0f571da-72fa-4c0e-bf34-2b4285134c13', 'f8bb9956-1952-4893-98c8-66683d25d7ce', '', '3', '0', '0', '2017-11-23 00:00:00', '2017-11-23 23:59:59', '0', '2017-11-24 03:00:00', '2017-11-24 03:00:00', '0', '001053110000001', 0),
('2', 'b8c4033f-28f5-47eb-b1ab-d5315e2ec0cf', 'DZ131269696000516096', 'b6b407fb-de54-4d84-9cba-84ccb2c17fcf', '19014526016005', 'e0b75e20-e350-4daf-ada4-474016a25c73', 'ae9e5cdd-4566-4c85-8d97-f34756f8ee88', '001', '1', '0', '0', '2017-11-23 00:00:00', '2017-11-23 23:59:59', '1', '2017-11-24 03:00:00', '2017-11-24 03:00:00', '0', '002762' , 1),
('3', '2d056efa-1359-4dd0-a5fb-006f9c0802cc', 'DZ131269696008904704', 'b6b407fb-de54-4d84-9cba-84ccb2c17fcf', '19014526016005', 'ff6c7dbf-bdc6-420d-a30e-2a556f1d4e10', 'f1ccca57-7c80-4429-b226-8ad31a729609', '', '1', '0', '1', '2017-11-23 00:00:00', '2017-11-23 23:59:59', '2', '2017-11-24 03:00:00', '2017-11-24 03:00:00', '2', '001053110000001',2),
('4', 'efa11c46-caa0-4391-b751-e47d96bf8d1a', 'DZ131269696034070528', '4eac7451-8808-4e3f-8101-2abaddcfd14b', '19014526954002', 'feb7b169-f47d-4344-9511-4efee080ae13', 'f8bb9956-1952-4893-98c8-66683d25d7ce', '', '2', '0', '0', '2017-11-23 00:00:00', '2017-11-23 23:59:59', '2', '2017-11-24 03:00:00', '2017-11-24 03:00:00', '0', '11002923034501',3);


SET FOREIGN_KEY_CHECKS=1;