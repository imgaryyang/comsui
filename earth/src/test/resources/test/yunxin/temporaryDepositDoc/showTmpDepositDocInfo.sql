SET FOREIGN_KEY_CHECKS=0;

	DELETE FROM `temporary_deposit_doc`;
	DELETE FROM `financial_contract`;
	DELETE FROM `contract`;
	DELETE FROM `virtual_account`;
	DELETE FROM `t_voucher`;
	DELETE FROM `cash_flow`;
	
INSERT INTO `temporary_deposit_doc`(`id`,`uuid`,`doc_no`,`total_amount`,`balance`,`alive_status`,`status`,`owner_name`,`owner_type`,`financial_contract_uuid`,`created_time`,`voucher_uuid`,`related_contract_uuid`,`source_document_uuid`,`virtual_account_uuid`,`cash_flow_uuid`,`last_modified_time`)
VALUES
	('1','TemDptDoc_uuid_1','doc_co_1',1000.00,1000.00,0,0,'owner_name_1',0,'financial_contract_uuid_1','2017-10-30 00:00:00','voucher_uuid_1','relatedContractUuid','source_document_uuid_1','virtual_account_uuid_1','cash_flow_uuid_1','2017-10-30 00:00:00');
	
INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`,`adva_repayment_term`,`repayment_check_days`)
VALUES 
	('1', '1', '7', '2016-06-08 00:00:00', 'financial_contract_no', 'sasad', 2, '1', '3', '2016-09-05 00:00:00', 2, '1', '0', '3', NULL, 'ledger_book_no', 'financial_contract_uuid_1', 1, 7);

INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, `interest_rate_cycle`, `customer_uuid`)
VALUES 
	('1', 'relatedContractUuid', 'contract_unique_id_1', '2017-05-02', 'contract_no_1', '2099-01-01', NULL, '0.00', '3', 1, '199316', NULL, '2017-05-02 16:37:26', '0.1560000000', '0', '0', '3', '2', '1500.00', '0.0005000000', '1', NULL, '2', 'financial_contract_uuid_1', '1', 'company_customer_uuid_1');

INSERT INTO `virtual_account` (`id`, `total_balance`, `virtual_account_uuid`, `parent_account_uuid`, `virtual_account_alias`, `virtual_account_no`, `version`, `owner_uuid`, `owner_name`, `fst_level_contract_uuid`, `snd_level_contract_uuid`, `trd_level_contract_uuid`, `create_time`, `last_update_time`, `customer_type`, `virtual_account_status`)
VALUES 
	('40', '1460.00', 'virtual_account_uuid_1', NULL, '测试员1', 'VACC275B96CD27E64F02', '5ce01306-45c2-421b-8d0e-531055075275', 'customer_uuid_1', '测试员1', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '491df853-846a-4958-9131-c8459a7dcb6f', '', '2016-12-09 11:41:08', '2016-12-22 14:52:56', '0', '0'),
	('41', '1460.00', '4b6e315a-7f95-4203-b081-efc0d3b28f99', NULL, '测试员2', 'VACC275B96CD27E64F03', '5ce01306-45', 'company_customer_uuid_1', '测试员2', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '491df853-846a-4958-9131-c8459a7dcb6f', '', '2016-12-09 11:41:08', '2016-12-22 14:52:56', '1', '0');

INSERT INTO `t_voucher` (`id`, `uuid`, `voucher_no`, `source_document_uuid`, `financial_contract_uuid`, `amount`, `status`, `first_type`, `first_no`, `second_type`, `second_no`, `receivable_account_no`, `payment_account_no`, `payment_name`, `payment_bank`, `check_state`, `comment`, `create_time`, `contract_no`, `last_modified_time`, `cash_flow_uuid`, `transaction_time`, `detail_amount`)
VALUES
	('1', 'voucher_uuid_1', 'voucher_no_1', 'source_document_uuid_1', 'financial_contract_uuid_1', '570.00', '0', 'enum.voucher-source.business-payment-voucher', 'f8dbc062-d00e-4dd6-af03-b1800ed195a2', 'enum.voucher-type.pay', '00144407-0b86-4604-b4c3-084047aecd14', NULL, '6214855712106520', '夏侯你惇哥', '中国建设银行', '2', NULL, '2017-05-09 22:40:02', NULL, '2017-05-09 22:40:02', '1b0b4455-34c5-11e7-bf99-00163e002839', '2017-05-09 22:39:23', '0');

INSERT INTO `cash_flow` (`id`, `cash_flow_uuid`, `cash_flow_channel_type`, `company_uuid`, `host_account_uuid`, `host_account_no`, `host_account_name`, `counter_account_no`, `counter_account_name`, `counter_account_appendix`, `counter_bank_info`, `account_side`, `transaction_time`, `transaction_amount`, `balance`, `transaction_voucher_no`, `bank_sequence_no`, `remark`, `other_remark`, `strike_balance_status`, `cash_flow_type`, `trade_uuid`, `issued_amount`, `audit_status`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`) VALUES 
('1', 'cash_flow_uuid_1', '0', 'company_uuid_1', 'd0503298-e890-425a-3333333', '600000000001', '测试专户开户行', '6214855712107780', '范腾', '', '', '1', '2016-09-05 10:51:02', '1000.00', '2000.00', '', 'cash_flow_no_1', 'remark_1', '', NULL, '0', '', '0.00', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
('2', 'cash_flow_uuid_2', '0', 'company_uuid_1', 'd0503298-e890-425a-3333333', '6000000000012', '测试专户开户行', '6214855712107780', '范腾', '', '', '1', '2016-09-06 10:51:02', '1960.00', '2000.00', '', 'cash_flow_no_2', 'remark_2', '', NULL, '0', '', '1000.00', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS=1;
