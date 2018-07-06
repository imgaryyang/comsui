SET FOREIGN_KEY_CHECKS=0;

DELETE FROM `asset_set`;
DELETE FROM `contract`;
DELETE FROM `customer`;
DELETE FROM `company`;
DELETE FROM `app`;
DELETE FROM `house`;
DELETE FROM `financial_contract`;
DELETE FROM `ledger_book`;
DELETE FROM `repurchase_doc`;
DELETE FROM `dictionary`;
DELETE FROM `t_interface_repurchase_command`;

INSERT INTO `asset_set` (`id`, `contract_id`, `financial_contract_uuid`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`,`executing_status`,`executing_status_bak`,`contract_uuid`) VALUES
('323456','234567','92846f20-87e3-49f4-8f90-fe04a72c0484', '0', '1647.20', '800.00', '800.00', '1600.00', '2016-06-01', NULL, '0.00', '0', '0', '0', '2016-10-11 03:01:09', '168c2fbe-a95f-4cae-8514-eabd7fb73a2a', '2016-06-01 14:54:13', '2016-10-11 03:01:09', NULL, 'ZC293D48AAD6E61017',  NULL, '1', '2', '2016-08-10', '1', '0', '0', 'empty_deduct_uuid',NULL,NULL,NULL,NULL,3,NULL,'cuuid222'),

('223456','123456','92846f20-87e3-49f4-8f90-fe04a72c0484', '0', '1647.20', '800.00', '800.00', '1600.00', '2016-11-01', NULL, '0.00', '0', '1', '0', '2016-10-11 03:01:09', '168c2fbe-a95f-4cae-8514-eabd7fb73a2b', '2016-06-01 14:54:13', '2016-10-11 03:01:09', NULL, 'ZC293D48AAD6E61016',  NULL, '1', '2', '2016-08-09', '1', '0', '0', 'empty_deduct_uuid',NULL,NULL,NULL,NULL,1,NULL,'cuid222' ),
('123456','123456','92846f20-87e3-49f4-8f90-fe04a72c0484', '0', '1647.20', '800.00', '800.00', '1600.00', '2016-06-01', NULL, '0.00', '0', '1', '0', '2016-10-11 03:01:09', '168c2fbe-a95f-4cae-8514-eabd7fb73a2c', '2016-06-01 14:54:13', '2016-10-11 03:01:09', NULL, 'ZC293D48AAD6E61015',  NULL, '1', '2', '2016-08-11', '1', '0', '0', 'empty_deduct_uuid',NULL,NULL,NULL,NULL,1, NULL,'cuid222'),

('423456','345678','92846f20-87e3-49f4-8f90-fe04a72c0484', '0', '1647.20', '800.00', '800.00', '1600.00', '2016-06-01', NULL, '0.00', '0', '1', '0', '2016-10-11 03:01:09', '168c2fbe-a95f-4cae-8514-eabd7fb73a2d', '2016-06-01 14:54:13', '2016-10-11 03:01:09', NULL, 'ZC293D48AAD6E61018',  NULL, '1', '2', '2016-08-11', '1', '2', '0', 'repurchasing',NULL,NULL,NULL,NULL,1,1,'cuid222'),
('523456','345678','92846f20-87e3-49f4-8f90-fe04a72c0484', '0', '1647.20', '800.00', '800.00', '1600.00', '2016-06-01', NULL, '0.00', '0', '1', '0', '2016-10-11 03:01:09', '168c2fbe-a95f-4cae-8514-eabd7fb73a2e', '2016-06-01 14:54:13', '2016-10-11 03:01:09', NULL, 'ZC293D48AAD6E61019',  NULL, '1', '2', '2016-08-11', '1', '2', '0', 'repurchasing',NULL,NULL,NULL,NULL,1,1,'cuid221');

INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`) VALUES
 ('123456', 'cuuid111', 'cuid111', '2016-08-01', 'G00003(zht765714537113774060)', '2018-01-01', NULL, '0.00', '1', '23', '1', NULL, '2016-08-27 16:06:51', '0.1200000000', '0', '0', '2', '2', '0.02', '11.0000000000', '1', NULL, '2', '92846f20-87e3-49f4-8f90-fe04a72c0484'),
 ('234567', 'cuuid222', 'cuid222', '2016-08-01', 'G00003(zht765714537113774061)', '2018-01-01', NULL, '0.00', '1', '23', '1', NULL, '2016-08-27 16:06:51', '0.1200000000', '0', '0', '2', '2', '0.02', '11.0000000000', '1', NULL, '2', '92846f20-87e3-49f4-8f90-fe04a72c0484'),
 ('345678', 'cuuid333', 'cuid333', '2016-08-01', 'G00003(zht765714537113774062)', '2018-01-01', NULL, '0.00', '1', '23', '1', NULL, '2016-08-27 16:06:51', '0.1200000000', '0', '0', '2', '2', '0.02', '11.0000000000', '1', NULL, '4', '92846f20-87e3-49f4-8f90-fe04a72c0484');

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`) VALUES
('23', NULL, NULL, 'ceshi24', 'C75688', '1', '08b5ae5f-7b16-48a1-a858-b8fd7bf6d53f', '0');

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`) VALUES
('2', 'NanJin', 'ceshishanwugongsi', 'ceshifenqi', 'a02c078d-6f98-11e6-bf08-00163e002839');

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`) VALUES
('1', 'nongfenqi', '', '', '', 'ceshifenqi', '2', NULL),
('2', 'qyb', '', '', '', 'ceshifenqi', '2', NULL);

INSERT INTO `house` (`id`, `address`, `app_id`) VALUES
('1', 'cesd', '1');

INSERT INTO `financial_contract` (`id`, `financial_contract_uuid`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`, `ledger_book_no`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`, `create_time`, `last_modified_time`,`repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`, `day_of_month`, `repurchase_principal_algorithm`,`repurchase_interest_algorithm`,`repurchase_penalty_algorithm`,`repurchase_other_charges_algorithm`,`allow_freewheeling_repayment`,`days_of_cycle`)VALUES
	(15,'92846f20-87e3-49f4-8f90-fe04a72c0484', 0, 3, '2016-06-14 00:00:00', 'ceshi003', 'test006', 1, 2, 10, '2016-06-25 00:00:00', 16, 0, 1, 90, 1, 'yunxin_ledger_book', 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL,1, 0, 'outstandingPrincipal+outstandingInterest', NULL, 'outstandingPrincipal', 'outstandingInterest','outstandingPenaltyInterest',NULL ,1,NULL );

INSERT INTO `ledger_book` (`id`, `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`,`ledger_book_version`) VALUES
(1, 'yunxin_ledger_book', '1', '1', '1','kjljkljk');


INSERT INTO `repurchase_doc` (repurchase_doc_uuid, financial_contract_uuid, amount, repo_start_date, repo_end_date, repo_days, creat_time, verification_time, last_modifed_time, contract_id, contract_no, app_id, app_name, customer_uuid, customer_name, executing_asset_set_uuids, asset_set_uuids, repurchase_status, repurchase_algorithm, repurchase_approach, repurchase_rule, day_of_month, adva_repo_term, repurchase_periods, amount_detail, repurchase_principal, repurchase_principal_algorithm, repurchase_interest, repurchase_interest_algorithm, repurchase_penalty, repurchase_penalty_algorithm, repurchase_other_charges, repurchase_other_charges_algorithm) VALUES
('766d20fb-c8e1-4eab-be02-0a6f2485cb73', '92846f20-87e3-49f4-8f90-fe04a72c0484', 3200, '2015-04-13', '2015-04-13', 0, '2017-04-13 17:43:06', null, '2017-04-13 17:43:06', 345678, 'G00003(zht765714537113774062', 1, 'ceshifenqi', '08b5ae5f-7b16-48a1-a858-b8fd7bf6d53f', '李杰', null, '["168c2fbe-a95f-4cae-8514-eabd7fb73a2d","168c2fbe-a95f-4cae-8514-eabd7fb73a2e"]', 0, null, 1, 1, 6, 10, 3, '{"amount":3200.00,"repurchaseInterest":1600,"repurchaseInterestExpression":"","repurchaseOtherCharges":0,"repurchaseOtherChargesExpression":"","repurchasePenalty":0,"repurchasePenaltyExpression":"","repurchasePrincipal":1600.00,"repurchasePrincipalAlgorithm":"outstandingPrincipal","repurchasePrincipalExpression":"1600.00"}', 1600, 'outstandingPrincipal', 1600, null, 0, null, 0, null);


INSERT INTO `dictionary` ( `code`, `content`)
VALUES
	( 'PLATFORM_PRI_KEY', 'MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=');

INSERT INTO `t_interface_repurchase_command` (request_no, batch_no, transaction_type, financial_contract_no, repurchase_detail, reviewer, review_time, create_time, ip) VALUES
('327cf084-232f-11e7-a992-dfe1c742f3fb', '60aa5f6e-232f-11e7-a992-dfe1c742f3fb', 0, 'ceshi003', '[{\"amount\":1600.00,\"interest\":800.00,\"penaltyInterest\":0,\"principal\":800.00,\"repurchaseOtherFee\":0,\"uniqueId\":\"cuid333\"}]', NULL, NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS=1;
