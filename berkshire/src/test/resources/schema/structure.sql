DROP TABLE IF EXISTS  `ledger_book`;

CREATE TABLE `ledger_book` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `ledger_book_no` varchar(255) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '账本编号',
  `ledger_book_orgnization_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '账本所属公司id',
  `book_master_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT '',
  `party_concerned_ids` text COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='分类账本表';


DROP TABLE  IF EXISTS `ledger_book_shelf`;

CREATE TABLE `ledger_book_shelf` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ledger_uuid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `debit_balance` decimal(19,2) DEFAULT NULL,
  `credit_balance` decimal(19,2) DEFAULT NULL,
  `first_account_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `first_account_uuid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `account_side` int(11) NOT NULL,
  `second_account_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `second_account_uuid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `third_account_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `third_account_uuid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `first_party_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `second_party_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `third_party_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `forward_ledger_uuid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `backward_ledger_uuid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `batch_serial_uuid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `amortized_date` datetime DEFAULT NULL,
  `book_in_date` datetime DEFAULT NULL,
  `business_voucher_uuid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `carried_over_date` datetime DEFAULT NULL,
  `contract_id` bigint(20) DEFAULT NULL,
  `contract_uuid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `default_date` datetime DEFAULT NULL,
  `journal_voucher_uuid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ledger_book_no` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ledger_book_owner_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `life_cycle` int(11) DEFAULT NULL,
  `related_lv_1_asset_outer_idenity` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `related_lv_1_asset_uuid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `related_lv_2_asset_outer_idenity` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `related_lv_2_asset_uuid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `related_lv_3_asset_outer_idenity` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `related_lv_3_asset_uuid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `source_document_uuid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ledger_book_no` (`ledger_book_no`) USING BTREE,
  KEY `first_account_uuid` (`first_account_uuid`) USING BTREE,
  KEY `second_account_uuid` (`second_account_uuid`) USING BTREE,
  KEY `third_account_uuid` (`third_account_uuid`) USING BTREE,
  KEY `contract_uuid` (`contract_uuid`) USING BTREE,
  KEY `related_lv_1_asset_uuid` (`related_lv_1_asset_uuid`) USING BTREE,
  KEY `related_lv_2_asset_uuid` (`related_lv_2_asset_uuid`) USING BTREE,
  KEY `related_lv_3_asset_uuid` (`related_lv_3_asset_uuid`) USING BTREE,
  KEY `first_party_id` (`first_party_id`) USING BTREE,
  KEY `second_party_id` (`second_party_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=213315 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='分类账本架表';

DROP TABLE IF EXISTS `t_account_table` ;

CREATE TABLE `t_account_table` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `first_account_code` varchar(255) DEFAULT NULL,
  `first_account_name` varchar(255) DEFAULT NULL,
  `first_account_side` int(11) DEFAULT NULL,
  `last_modified_time` datetime DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `second_account_code` varchar(255) DEFAULT NULL,
  `second_account_name` varchar(255) DEFAULT NULL,
  `second_account_side` int(11) DEFAULT NULL,
  `third_account_code` varchar(255) DEFAULT NULL,
  `third_account_name` varchar(255) DEFAULT NULL,
  `third_account_side` int(11) DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8;