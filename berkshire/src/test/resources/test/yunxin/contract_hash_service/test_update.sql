SET FOREIGN_KEY_CHECKS=0;

DELETE from `contract_hash`;


INSERT INTO `contract_hash` (`id`, `hash_algorithm`, `hash_count`, `hash_value`, `last_update_time`, `first_level_contract`, `second_level_contract`, `third_level_contract`)
VALUES
	(1, 1, 200, 'yyyy-cccc-hhhh', '2016-09-18 00:00:00', "1st_0001", "2nd_0001", NULL);

	
SET FOREIGN_KEY_CHECKS=1;