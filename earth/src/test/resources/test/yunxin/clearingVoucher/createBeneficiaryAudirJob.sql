SET FOREIGN_KEY_CHECKS=0;
	delete from `payment_channel_information`;

INSERT INTO `payment_channel_information` (`id`, `related_financial_contract_uuid`, `related_financial_contract_name`, `payment_channel_uuid`, `payment_channel_name`, `payment_institution_name`, `outlier_channel_name`, `create_time`, `last_modify_time`, `credit_channel_working_status`, `debit_channel_working_status`, `credit_payment_channel_service_uuid`, `debit_payment_channel_service_uuid`, `configure_progress`, `payment_configure_data`, `clearing_no`, `business_type`)
VALUES
	(1, 'financial_contract_uuid_1', '测试农分期', 'payment_channel_uuid_1', 'G08200银联代收付', 0, '001053110000001', '2016-09-02 00:00:00', '2016-11-09 23:59:46', 0, 1, NULL, 'f1ccca57-7c80-4429-b226-8ad31a729609', 0, '{\"complete\":true,\"creditChannelConfigure\":{\"channelStatus\":\"NOTLINK\",\"clearingInterval\":0,\"valid\":true},\"debitChannelConfigure\":{\"channelStatus\":\"ON\",\"chargeExcutionMode\":\"BACKWARD\",\"chargePerTranscation\":0,\"chargeRateMode\":\"SINGLEFIXED\",\"clearingInterval\":1,\"valid\":true}}', '', 0);

SET FOREIGN_KEY_CHECKS=1;