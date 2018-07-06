SET FOREIGN_KEY_CHECKS = 0;
delete from `asset_set_extra_charge`;
INSERT INTO `asset_set_extra_charge` (`id`, `asset_set_extra_charge_uuid`, `asset_set_uuid`, `create_time`, `last_modify_time`, `first_account_name`, 
									  `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`) 
	VALUES ('309', 'aabcc85f-2011-43d3-93a8-6cded2c35ab5', 'e0fdcb1e-68cb-45c8-a1c7-82d6dca09879', '2016-09-12 17:10:53', '2016-09-12 17:10:53', 'FST_RECIEVABLE_ASSET', 
	       '20000', 'SND_RECIEVABLE_LOAN_PENALTY', '20000.03', NULL, NULL, '2.00'),
           ('310', 'ec0f6223-55a7-4eea-bbc2-3e40bf081ab0', 'e0fdcb1e-68cb-45c8-a1c7-82d6dca09879', '2016-09-12 17:10:53', '2016-09-12 17:10:53', 'FST_RECIEVABLE_ASSET', 
           '20000', 'SND_RECIEVABLE_OVERDUE_FEE', '20000.06', 'TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION', '20000.06.01', '2.00'),
		   ('311', 'ba5a8b9d-96bc-4c5f-bc99-c23508babe82', 'e0fdcb1e-68cb-45c8-a1c7-82d6dca09879', '2016-09-12 17:10:53', '2016-09-12 17:10:53', 'FST_RECIEVABLE_ASSET', 
		   '20000', 'SND_RECIEVABLE_OVERDUE_FEE', '20000.06', 'TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE', '20000.06.02', '100.00');
SET FOREIGN_KEY_CHECKS = 1;