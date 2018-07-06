delete from `contract`;
delete from `t_voucher`;
delete from `financial_contract`;
delete from `customer`;
delete from `asset_set`;
delete from `account`;
delete from `company`;
delete from `cash_flow`;
delete from `contract_account`;


INSERT INTO `contract_account` (`id`, `pay_ac_no`, `bankcard_type`, `contract_id`, `payer_name`, `bank`, `bind_id`, `id_card_num`, `bank_code`, `province`, `province_code`, `city`, `city_code`, `standard_bank_code`, `from_date`, `thru_date`, `virtual_account_uuid`, `bank_card_status`, `contract_account_uuid`, `contract_account_type`)
VALUES
(10, '6217000000000000003', NULL, 10, '测试用户10', '中国邮政储蓄银行', NULL, NULL, '403', '江苏省', NULL, '泰州', NULL, NULL, '2016-04-19 00:00:00', '2900-01-01 00:00:00', NULL, 0, 'c3add148-c2c1-11e6-abc5-00163e002839', 0);


INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, `interest_rate_cycle`)
VALUES
(10, 'a0afdbba-5fa8-11e6-b2c2-00163e002839', NULL, '2016-04-19', '2016-78-DK(ZQ2016042522477)', NULL, 1, 0.00, 1, 10, 171, NULL, '2016-05-27 18:27:16', 0.1560000000, 0, 0, 1, 2, 2000.00, 0.0005000000, 1, NULL, 2, '2d380fe1-7157-490d-9474-12c5a9901e29', 0);

INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`)
VALUES
(5, 1, 3, '2016-04-01 00:00:00', '2016-78-DK(ZQ2016042522477)', '测试合同', 1, 1, 91, '2016-07-01 00:00:00', 5, 0, 1, 90, 2, '74a9ce4d-cafc-407d-b013-987077541bdc', '2d380fe1-7157-490d-9474-12c5a9901e29', 1, 1, 1, 1, 0, 1, NULL, NULL, NULL, NULL, 1, 0, 0, 0, 'N*N', 123.00, 23.00, NULL, NULL, '2016-12-08 22:46:27');

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`)
VALUES
(10, NULL, NULL, '测试用户10', 'C19463', 1, '28fff02e-6a67-4eef-bb6a-a799c1102a11', 0);

INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`)
VALUES
(10, 1, 0, 2207.00, 0.00, 2000.00, 2000.00, '2016-05-19', NULL, 0.00, 0, 1, 0, '2016-12-12 03:00:54', '8fb926e0-097c-4f0f-8c8b-93b937096735', '2016-05-27 18:27:16', '2016-12-12 03:00:54', NULL, 'ZC2730FAE40A2D3188', 10, NULL, 1, 1, NULL, 1, 0, 0, '684fddbe-a1f6-43cc-b3f3-9c9346a035d5', NULL, '2d380fe1-7157-490d-9474-12c5a9901e29', NULL, NULL, NULL, NULL);

INSERT INTO `account` (`id`, `account_name`, `account_no`, `bank_name`, `alias`, `attr`, `scan_cash_flow_switch`, `usb_key_configured`, `uuid`, `bank_code`, `cash_flow_config`)
VALUES
(5, '银企直连专用账户9', '591902896710201', '工商银行', NULL, '{\"bankBranchNo\":\"59\",\"usbUuid\":\"b173f24a-3c27-4243-85b7-e93ef6a128ac\"}', 1, 1, 'uuid_5', 'PAB', '{\"nextPageNo\":\"1\"}');

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`)
VALUES
(1, '上海', '测试金融公司', '测试金融', 'company_uuid_1');

INSERT INTO `cash_flow` (`id`, `cash_flow_uuid`, `cash_flow_channel_type`, `company_uuid`, `host_account_uuid`, `host_account_no`, `host_account_name`, `counter_account_no`, `counter_account_name`, `counter_account_appendix`, `counter_bank_info`, `account_side`, `transaction_time`, `transaction_amount`, `balance`, `transaction_voucher_no`, `bank_sequence_no`, `remark`, `other_remark`, `strike_balance_status`, `trade_uuid`, `issued_amount`, `audit_status`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`)
VALUES
(1, 'cash_flow_uuid_12', 0, 'company_uuid_1', 'd0503298-e890-425a-b5b4-12', '6600000000000000001', '测试专户开户行', '10001', 'counter_name', NULL, '{\"bankCode\":\"xx\",\"bankName\":\"招商银行\"}', 1, '2016-08-31 19:51:02', 2207.00, 11000.00, NULL, 'cash_flow_no_12', NULL, NULL, NULL, NULL, 11000.00, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);















