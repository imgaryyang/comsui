SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM t_account_template;

DELETE FROM t_business_scenario_definition;


INSERT INTO `t_account_template` (`id`, `default_date`, `event_type`, `ledger_book_no`, `uuid`, `scenario_id`)
VALUES
	(8, '2017-05-09 10:52:14', 1, 'yunxin_ledger_book', 'd1f4f09a-9476-467b-888c-222ce72478d7', 11);


INSERT INTO `t_business_scenario_definition` (`id`, `uuid`, `event_type`, `template`, `add_time`, `last_modify_time`)
VALUES
	(11, 'f9c6266e-d776-460a-869c-f5468171d875', 1, '[{\"DynamicLedgerDefinition\":\"[{\\\"beanName\\\":\\\"ledgerItemV2Service\\\",\\\"methodName\\\":\\\"carry_over_by_account_balance_table\\\",\\\"parameters\\\":[\\\"LedgerBookCarryOverContext\\\",\\\"amountOfTable\\\"]}]\",\"contextDefinition\":\"[{\\\"name\\\":\\\"ledgerBookNo\\\",\\\"valueReference\\\":\\\"@ledgerBookNo\\\"},{\\\"name\\\":\\\"account_to_account_mapping_table\\\",\\\"valueReference\\\":\\\"@account_to_account_mapping_table\\\"},{\\\"name\\\":\\\"amountOfTable\\\",\\\"valueReference\\\":\\\"@amountOfTable\\\"},{\\\"name\\\":\\\"batchSerialUuid\\\",\\\"valueReference\\\":\\\"@batchSerialUuid\\\"},{\\\"name\\\":\\\"LedgerBookCarryOverContext\\\",\\\"valueReference\\\":\\\"@LedgerBookCarryOverContext\\\"}]\"}]', '2017-05-09 10:52:14', '2017-05-09 10:52:14');

	
SET FOREIGN_KEY_CHECKS = 1;