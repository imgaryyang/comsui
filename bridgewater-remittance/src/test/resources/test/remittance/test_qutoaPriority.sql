delete from `financial_contract_config`;
INSERT INTO `financial_contract_config` (`financial_contract_uuid`, `business_type`, `payment_channel_uuids_for_credit`, `payment_channel_uuids_for_debit`, `credit_payment_channel_mode`, `debit_payment_channel_mode`, `payment_channel_router_for_credit`, `payment_channel_router_for_debit`, `payment_channel_config_for_credit`, `payment_channel_config_for_debit`)
VALUES
	('ace7522b-0b94-4f4f-8d6f-2e4b6744ef6b', 0, '[\"8ce3469e-9059-11e6-9aaf-00163e002839\",\"8ce3469e-9059-11e6-9aaf-00163e002840\"]', '[\"8ce34734-9059-11e6-9aaf-00163e002839\"]', 2, 0, '8ce3469e-9059-11e6-9aaf-00163e002839', '8ce34734-9059-11e6-9aaf-00163e002839', NULL, NULL);

delete from `payment_channel_information`;
INSERT INTO `payment_channel_information` (`related_financial_contract_uuid`, `related_financial_contract_name`, `payment_channel_uuid`, `payment_channel_name`, `payment_institution_name`, `outlier_channel_name`, `create_time`, `last_modify_time`, `credit_channel_working_status`, `debit_channel_working_status`, `credit_payment_channel_service_uuid`, `debit_payment_channel_service_uuid`, `configure_progress`, `payment_configure_data`, `clearing_no`, `business_type`)
VALUES
	('ace7522b-0b94-4f4f-8d6f-2e4b6744ef6b', 'G40900测试信托', '8ce3469e-9059-11e6-9aaf-00163e002839', 'G40900招行银企直连', 2, '201600000040900', '2017-04-12 10:41:29', '2016-10-19 11:04:53', 1, 0, 'df49620c-5b21-4c07-82af-4b8079655a32', NULL, 0, '{\"complete\":true,\"creditChannelConfigure\":{\"channelStatus\":\"ON\",\"chargeExcutionMode\":\"FORWARD\",\"chargeRateMode\":\"SINGLEFIXED\",\"clearingInterval\":0,\"valid\":true,\"trasncationLimitPerTransaction\":0.5},\"debitChannelConfigure\":{\"channelStatus\":\"NOTLINK\",\"clearingInterval\":0,\"valid\":true}}', NULL, 0),
  ('ace7522b-0b94-4f4f-8d6f-2e4b6744ef6b', 'G40900测试信托', '8ce3469e-9059-11e6-9aaf-00163e002840', 'G40900招行银企直连', 2, '201600000040900', '2016-04-12 10:41:29',
                                           '2016-10-19 11:04:53', 1, 0, 'df49620c-5b21-4c07-82af-4b8079655a33', NULL, 0,
   '{\"complete\":true,\"creditChannelConfigure\":{\"channelStatus\":\"ON\",\"chargeExcutionMode\":\"FORWARD\",\"chargeRateMode\":\"SINGLEFIXED\",\"clearingInterval\":0,\"valid\":true,\"trasncationLimitPerTransaction\":5},\"debitChannelConfigure\":{\"channelStatus\":\"NOTLINK\",\"clearingInterval\":0,\"valid\":true}}',
   NULL, 0);

