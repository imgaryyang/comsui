SET FOREIGN_KEY_CHECKS = 0;
delete from `asset_set_extra_charge`;

INSERT INTO `asset_set_extra_charge` (`id`, `asset_set_extra_charge_uuid`, `asset_set_uuid`, `create_time`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`) 
VALUES 
('1', '90d22404-99ea-4b5a-872c-5ffc6c7909fd', '1', '2016-10-26 11:09:03', '2016-10-27 11:09:09', 'FIRST_NAME', '1', 'SECOND_NAME', '1', 'THIRD_NAME', '1', '100.00'),
('2', '90d22404-99ea-4b5a-872c-5ffc6c7909fe', '2', '2016-10-26 11:09:03', '2016-10-27 11:09:09', 'FIRST_NAME', '1', 'SECOND_NAME', '1', NULL, NULL, '100.00'),
('3', '90d22404-99ea-4b5a-872c-5ffc6c7905fe', '3', '2016-10-26 11:09:03', '2016-10-27 11:09:09', 'FIRST_NAME', '1', NULL, NULL, NULL, NULL, '100.00');

SET FOREIGN_KEY_CHECKS = 1;