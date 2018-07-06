SET FOREIGN_KEY_CHECKS = 0;

delete from special_account;

INSERT INTO `special_account` (`id`, `uuid`, `balance`, `basic_account_type`, `account_type_code`, `account_name`, `level`, `parent_account_uuid`, `fst_level_contract_uuid`, `snd_level_contract_uuid`, `trd_level_contract_uuid`, `create_time`, `last_update_time`, `version`) VALUES 
('251', '26f5b7c2-4aa9-43f4-8d71-f8e52df3a9a2', '0.00', '5', '30000', '计提户', '1', '95913ab8-5f24-4ed2-85d8-e899e612d397', 'financialContractUuid', '', '', '2017-12-26 20:25:28', '2017-12-26 20:25:28', '365ac631-040f-4bb8-a42c-0101d24da143');
INSERT INTO `special_account` (`id`, `uuid`, `balance`, `basic_account_type`, `account_type_code`, `account_name`, `level`, `parent_account_uuid`, `fst_level_contract_uuid`, `snd_level_contract_uuid`, `trd_level_contract_uuid`, `create_time`, `last_update_time`, `version`) VALUES 
('252', 'ff9d5745-9bf3-4e61-814b-763b95d1ae43', '0.00', '5', '30000.1000', '计提户-01', '2', '26f5b7c2-4aa9-43f4-8d71-f8e52df3a9a2', 'financialContractUuid', '', '', '2017-12-26 20:25:28', '2017-12-26 20:25:28', 'd154b85d-2924-42cc-adad-1be64cf22c86');
INSERT INTO `special_account` (`id`, `uuid`, `balance`, `basic_account_type`, `account_type_code`, `account_name`, `level`, `parent_account_uuid`, `fst_level_contract_uuid`, `snd_level_contract_uuid`, `trd_level_contract_uuid`, `create_time`, `last_update_time`, `version`) VALUES 
('253', 'ff9d5745-9bf3-4e61-814b-763b95d1ae44', '0.00', '5', '30000.1000', '计提户-02', '2', '26f5b7c2-4aa9-43f4-8d71-f8e52df3a9a2', 'financialContractUuid', '', '', '2017-12-26 20:25:28', '2017-12-26 20:25:28', 'd154b85d-2924-42cc-adad-1be64cf22c86');


SET FOREIGN_KEY_CHECKS = 1;