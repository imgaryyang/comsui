--drop table IF exists `balance_entry`
--
--CREATE TABLE `balance_entry` (
--  `id` bigint(20) NOT NULL AUTO_INCREMENT,
--  `uuid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
--  `general_balance_uuid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
--  `debit_balance` decimal(19,2) DEFAULT NULL,
--  `credit_balance` decimal(19,2) DEFAULT NULL,
--  `first_account_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
--  `first_account_uuid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
--  `second_account_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
--  `second_account_uuid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
--  `third_account_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
--  `third_account_uuid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
--  `first_party_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
--  `second_party_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
--  `third_party_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
--   `account_side` int(11) NOT NULL,
--   `book_in_date` datetime DEFAULT NULL,
--  `related_lv_1_asset_outer_idenity` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
--  `related_lv_1_asset_uuid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
--  `related_lv_2_asset_outer_idenity` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
--  `related_lv_2_asset_uuid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
--  `related_lv_3_asset_outer_idenity` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
--  `related_lv_3_asset_uuid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
--  PRIMARY KEY (`id`),
--  KEY `uuid` (`uuid`) USING BTREE
--) ENGINE=InnoDB AUTO_INCREMENT=34579 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='单条放款信息表';

delete from `balance_entry`;

INSERT INTO `balance_entry` (`uuid`, `general_balance_uuid`, `financial_contract_uuid`, `remittance_application_uuid`, `remittance_plan_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_side`, `book_in_date`, `combine_prevent_repetition`) VALUES ('sdfgsdfgdfg', 'sfdgsfgfg', 'sfgsfgsdfgfgsgsfdgsdfg', 'sfgsfgsfgsgfg', '266c5769-c503-423c-a8ff-fac2a6e4c9a5', '12.00', '45.00', 'ertett', 'ertert', 'ertert', 'fgdgfg', 'tytyy', 'tyuty', '1', '2017-12-08 11:19:51', 'd32a316e-6202-4fca-ba91-a353b03c4a2c_60000.01.02_1',null,null,null,null,null,null,null,null,null);



