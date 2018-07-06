--CREATE TABLE `general_balance` (
--  `id` bigint(20) NOT NULL AUTO_INCREMENT,
--  `uuid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
--  `related_financial_contract_uuid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
--  `related_contract_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
--  `bank_saving_loan` decimal(19,2) DEFAULT NULL,
--  `bank_saving_freeze` decimal(19,2) DEFAULT NULL,
--  `pay_able` decimal(19,2) DEFAULT NULL,
--  `create_time` datetime DEFAULT NULL,
--  `lastest_modified_time` datetime DEFAULT NULL,
--  `version_lock` varchar(255) COLLATE utf8_unicode_ci default NULL,
--  PRIMARY KEY (`id`),
--  KEY `uuid` (`uuid`) USING BTREE,
--  KEY `related_financial_contract_uuid` (`related_financial_contract_uuid`) USING BTREE
--) ENGINE=InnoDB AUTO_INCREMENT=34579 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='放款限额管理表';

delete from `general_balance`;

INSERT INTO `general_balance` (`uuid`, `related_financial_contract_uuid`, `related_contract_name`, `bank_saving_loan`, `bank_saving_freeze`, `pay_able`, `create_time`, `lastest_modified_time`, `version_lock`) VALUES ( '11', '1111111111', '11', 60, 60, -120, '2017-12-01 00:00:00', '2017-12-01 00:00:00', '123');
