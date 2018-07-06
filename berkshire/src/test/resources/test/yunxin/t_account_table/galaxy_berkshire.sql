-- MySQL dump 10.13  Distrib 5.7.15, for Linux (i686)
--
-- Host: localhost    Database: galaxy_develop
-- ------------------------------------------------------
-- Server version	5.7.15-0ubuntu0.16.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_name` varchar(255) DEFAULT NULL,
  `account_no` varchar(255) DEFAULT NULL,
  `alias` varchar(255) DEFAULT NULL,
  `attr` varchar(255) DEFAULT NULL,
  `bank_code` varchar(255) DEFAULT NULL,
  `bank_name` varchar(255) DEFAULT NULL,
  `scan_cash_flow_switch` bit(1) NOT NULL,
  `usb_key_configured` bit(1) NOT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `app`
--

DROP TABLE IF EXISTS `app`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `addressee` varchar(255) DEFAULT NULL,
  `app_id` varchar(255) DEFAULT NULL,
  `app_secret` varchar(255) DEFAULT NULL,
  `is_disabled` bit(1) DEFAULT NULL,
  `host` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `company_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_sw03avhjpih3a9mre2dqtmljs` (`company_id`),
  CONSTRAINT `FK_sw03avhjpih3a9mre2dqtmljs` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app`
--

LOCK TABLES `app` WRITE;
/*!40000 ALTER TABLE `app` DISABLE KEYS */;
/*!40000 ALTER TABLE `app` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company`
--

DROP TABLE IF EXISTS `company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `company` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `short_name` varchar(255) DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company`
--

LOCK TABLES `company` WRITE;
/*!40000 ALTER TABLE `company` DISABLE KEYS */;
/*!40000 ALTER TABLE `company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contract_hash`
--

DROP TABLE IF EXISTS `contract_hash`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contract_hash` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `first_level_contract` varchar(255) DEFAULT NULL,
  `hash_algorithm` int(11) DEFAULT NULL,
  `hash_count` bigint(20) DEFAULT NULL,
  `hash_value` varchar(255) DEFAULT NULL,
  `last_update_time` datetime DEFAULT NULL,
  `second_level_contract` varchar(255) DEFAULT NULL,
  `third_level_contract` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contract_hash`
--

LOCK TABLES `contract_hash` WRITE;
/*!40000 ALTER TABLE `contract_hash` DISABLE KEYS */;
/*!40000 ALTER TABLE `contract_hash` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `financial_contract`
--

DROP TABLE IF EXISTS `financial_contract`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `financial_contract` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `adva_matuterm` int(11) NOT NULL,
  `adva_repo_term` int(11) NOT NULL,
  `adva_start_date` datetime DEFAULT NULL,
  `asset_package_format` int(11) DEFAULT NULL,
  `contract_name` varchar(255) DEFAULT NULL,
  `contract_no` varchar(255) DEFAULT NULL,
  `financial_contract_type` int(11) DEFAULT NULL,
  `financial_contract_uuid` varchar(255) DEFAULT NULL,
  `ledger_book_no` varchar(255) DEFAULT NULL,
  `loan_overdue_end_day` int(11) NOT NULL,
  `loan_overdue_start_day` int(11) NOT NULL,
  `sys_create_guarantee_flag` bit(1) NOT NULL,
  `sys_create_penalty_flag` bit(1) NOT NULL,
  `sys_normal_deduct_flag` bit(1) NOT NULL,
  `sys_overdue_deduct_flag` bit(1) NOT NULL,
  `thru_date` datetime DEFAULT NULL,
  `unusual_modify_flag` bit(1) NOT NULL,
  `app_id` bigint(20) NOT NULL,
  `capital_account_id` bigint(20) DEFAULT NULL,
  `company_id` bigint(20) NOT NULL,
  `payment_channel_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_9ohjmybwivwxo1asg7q2lp2kk` (`app_id`),
  KEY `FK_gcg7s7lvq8s7a4u7c2h0f5g3d` (`capital_account_id`),
  KEY `FK_8cwqovcsuvhc9dfimgs0b8qa6` (`company_id`),
  KEY `FK_7vx5tg18co351kujxhx3oeggm` (`payment_channel_id`),
  CONSTRAINT `FK_7vx5tg18co351kujxhx3oeggm` FOREIGN KEY (`payment_channel_id`) REFERENCES `payment_channel` (`id`),
  CONSTRAINT `FK_8cwqovcsuvhc9dfimgs0b8qa6` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`),
  CONSTRAINT `FK_9ohjmybwivwxo1asg7q2lp2kk` FOREIGN KEY (`app_id`) REFERENCES `app` (`id`),
  CONSTRAINT `FK_gcg7s7lvq8s7a4u7c2h0f5g3d` FOREIGN KEY (`capital_account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `financial_contract`
--

LOCK TABLES `financial_contract` WRITE;
/*!40000 ALTER TABLE `financial_contract` DISABLE KEYS */;
/*!40000 ALTER TABLE `financial_contract` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `house`
--

DROP TABLE IF EXISTS `house`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `house` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `app_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_c77xof7x7l7q26wxuw8y62o31` (`app_id`),
  CONSTRAINT `FK_c77xof7x7l7q26wxuw8y62o31` FOREIGN KEY (`app_id`) REFERENCES `app` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `house`
--

LOCK TABLES `house` WRITE;
/*!40000 ALTER TABLE `house` DISABLE KEYS */;
/*!40000 ALTER TABLE `house` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ledger_book`
--

DROP TABLE IF EXISTS `ledger_book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ledger_book` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `book_master_id` varchar(255) DEFAULT NULL,
  `ledger_book_no` varchar(255) DEFAULT NULL,
  `ledger_book_orgnization_id` varchar(255) DEFAULT NULL,
  `party_concerned_ids` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ledger_book`
--

LOCK TABLES `ledger_book` WRITE;
/*!40000 ALTER TABLE `ledger_book` DISABLE KEYS */;
INSERT INTO `ledger_book` VALUES (1,NULL,'YUNXIN_AMEI_ledger_book','14',NULL);
/*!40000 ALTER TABLE `ledger_book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ledger_book_shelf`
--

DROP TABLE IF EXISTS `ledger_book_shelf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ledger_book_shelf` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_side` int(11) NOT NULL,
  `amortized_date` date DEFAULT NULL,
  `backward_ledger_uuid` varchar(255) DEFAULT NULL,
  `batch_serial_uuid` varchar(255) DEFAULT NULL,
  `book_in_date` datetime DEFAULT NULL,
  `business_voucher_uuid` varchar(255) DEFAULT NULL,
  `carried_over_date` datetime DEFAULT NULL,
  `contract_id` bigint(20) DEFAULT NULL,
  `contract_uuid` varchar(255) DEFAULT NULL,
  `credit_balance` decimal(19,2) DEFAULT NULL,
  `debit_balance` decimal(19,2) DEFAULT NULL,
  `default_date` datetime DEFAULT NULL,
  `first_account_name` varchar(255) DEFAULT NULL,
  `first_account_uuid` varchar(255) DEFAULT NULL,
  `first_party_id` varchar(255) DEFAULT NULL,
  `forward_ledger_uuid` varchar(255) DEFAULT NULL,
  `journal_voucher_uuid` varchar(255) DEFAULT NULL,
  `ledger_book_no` varchar(255) DEFAULT NULL,
  `ledger_book_owner_id` varchar(255) DEFAULT NULL,
  `ledger_uuid` varchar(255) DEFAULT NULL,
  `life_cycle` int(11) DEFAULT NULL,
  `related_lv_1_asset_outer_idenity` varchar(255) DEFAULT NULL,
  `related_lv_1_asset_uuid` varchar(255) DEFAULT NULL,
  `related_lv_2_asset_outer_idenity` varchar(255) DEFAULT NULL,
  `related_lv_2_asset_uuid` varchar(255) DEFAULT NULL,
  `related_lv_3_asset_outer_idenity` varchar(255) DEFAULT NULL,
  `related_lv_3_asset_uuid` varchar(255) DEFAULT NULL,
  `second_account_name` varchar(255) DEFAULT NULL,
  `second_account_uuid` varchar(255) DEFAULT NULL,
  `second_party_id` varchar(255) DEFAULT NULL,
  `source_document_uuid` varchar(255) DEFAULT NULL,
  `third_account_name` varchar(255) DEFAULT NULL,
  `third_account_uuid` varchar(255) DEFAULT NULL,
  `third_party_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=426 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ledger_book_shelf`
--

LOCK TABLES `ledger_book_shelf` WRITE;
/*!40000 ALTER TABLE `ledger_book_shelf` DISABLE KEYS */;
INSERT INTO `ledger_book_shelf` VALUES (2,0,NULL,'','',NULL,'',NULL,NULL,NULL,1.00,0.00,NULL,'FST_RECIEVABLE_ASSET','20000','','','','YUNXIN_AMEI_ledger_book','14','test_ledger_1',NULL,NULL,'sss',NULL,NULL,NULL,NULL,'','','',NULL,'','',''),(4,0,NULL,'','',NULL,'',NULL,NULL,NULL,1.00,0.00,NULL,'FST_RECIEVABLE_ASSET','20000','','','','YUNXIN_AMEI_ledger_book','14','test_ledger_2',NULL,NULL,'sss',NULL,NULL,NULL,NULL,'SND_RECIEVABLE_COLLECTION','20000.05','',NULL,'','',''),(5,0,NULL,'','',NULL,'',NULL,NULL,NULL,1.00,0.00,NULL,'FST_RECIEVABLE_ASSET','20000','','','','YUNXIN_AMEI_ledger_book','14','test_ledger_3',NULL,NULL,'sss',NULL,NULL,NULL,NULL,'SND_RECIEVABLE_COLLECTION','20000.05','',NULL,'TRD_RECIEVABLE_COLLECTION_OVERDUE_LOAN','20000.05.01',''),(418,0,'2015-10-19',NULL,'35b113d5-0784-4a36-8fd9-9468435c02b4','2016-09-28 20:21:48','',NULL,1,'uuidxxxxx',120.00,0.00,'2015-10-19 00:00:00','FST_REVENUE','70000','1',NULL,'','YUNXIN_AMEI_ledger_book','14','d4fd3d58-235b-4871-bd05-ea9fa1be6eb4',1,'DKHD-001-01','asset_uuid_1',NULL,NULL,NULL,NULL,'SND_REVENUE_OVERDUE_FEE','70000.06','2','','TRD_REVENUE_OVERDUE_FEE_OTHER_FEE','SND_REVENUE_OVERDUE_FEE.03',NULL),(419,1,'2015-10-19',NULL,'35b113d5-0784-4a36-8fd9-9468435c02b4','2016-09-28 20:21:48','',NULL,1,'uuidxxxxx',0.00,100.00,'2015-10-19 00:00:00','FST_RECIEVABLE_ASSET','20000','1',NULL,'','YUNXIN_AMEI_ledger_book','14','1d0fa49d-be21-49fa-85ba-18d34d6dbf12',1,'DKHD-001-01','asset_uuid_1',NULL,NULL,NULL,NULL,'SND_RECIEVABLE_OVERDUE_FEE','20000.06','2','','TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION','20000.06.01',NULL),(420,1,'2015-10-19',NULL,'35b113d5-0784-4a36-8fd9-9468435c02b4','2016-09-28 20:21:48','',NULL,1,'uuidxxxxx',0.00,110.00,'2015-10-19 00:00:00','FST_RECIEVABLE_ASSET','20000','1',NULL,'','YUNXIN_AMEI_ledger_book','14','9cbf0c84-ddcf-4743-9463-34c1f9e91a10',1,'DKHD-001-01','asset_uuid_1',NULL,NULL,NULL,NULL,'SND_RECIEVABLE_OVERDUE_FEE','20000.06','2','','TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE','20000.06.02',NULL),(421,0,'2015-10-19',NULL,'35b113d5-0784-4a36-8fd9-9468435c02b4','2016-09-28 20:21:48','',NULL,1,'uuidxxxxx',110.00,0.00,'2015-10-19 00:00:00','FST_REVENUE','70000','1',NULL,'','YUNXIN_AMEI_ledger_book','14','bb28c325-3aa5-48d7-9a45-d5baccadab21',1,'DKHD-001-01','asset_uuid_1',NULL,NULL,NULL,NULL,'SND_REVENUE_OVERDUE_FEE','70000.06','2','','TRD_REVENUE_OVERDUE_FEE_SERVICE_FEE','SND_REVENUE_OVERDUE_FEE.02',NULL),(422,1,'2015-10-19',NULL,'35b113d5-0784-4a36-8fd9-9468435c02b4','2016-09-28 20:21:48','',NULL,1,'uuidxxxxx',0.00,120.00,'2015-10-19 00:00:00','FST_RECIEVABLE_ASSET','20000','1',NULL,'','YUNXIN_AMEI_ledger_book','14','feffbbca-e6f2-43af-a6d6-041672b457b6',1,'DKHD-001-01','asset_uuid_1',NULL,NULL,NULL,NULL,'SND_RECIEVABLE_OVERDUE_FEE','20000.06','2','','TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE','20000.06.03',NULL),(423,0,'2015-10-19',NULL,'35b113d5-0784-4a36-8fd9-9468435c02b4','2016-09-28 20:21:48','',NULL,1,'uuidxxxxx',100.00,0.00,'2015-10-19 00:00:00','FST_REVENUE','70000','1',NULL,'','YUNXIN_AMEI_ledger_book','14','d6b39e7d-2158-4c27-8b96-b0f9e91277a8',1,'DKHD-001-01','asset_uuid_1',NULL,NULL,NULL,NULL,'SND_REVENUE_OVERDUE_FEE','70000.06','2','','TRD_REVENUE_OVERDUE_FEE_OBLIGATION','SND_REVENUE_OVERDUE_FEE.01',NULL),(424,1,'2015-10-19',NULL,'b27246c1-9640-471a-80ba-958da33661c3','2016-09-28 20:21:48','',NULL,1,'uuidxxxxx',100.00,0.00,'2015-10-19 00:00:00','FST_RECIEVABLE_ASSET','20000','1',NULL,'','YUNXIN_AMEI_ledger_book','14','39732c79-b9c8-4680-b5bb-64d276fae437',1,'DKHD-001-01','asset_uuid_1',NULL,NULL,NULL,NULL,'SND_RECIEVABLE_OVERDUE_FEE','20000.06','2','','TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION','20000.06.01',NULL),(425,0,'2015-10-19',NULL,'b27246c1-9640-471a-80ba-958da33661c3','2016-09-28 20:21:48','',NULL,1,'uuidxxxxx',0.00,100.00,'2015-10-19 00:00:00','FST_REVENUE','70000','1',NULL,'','YUNXIN_AMEI_ledger_book','14','fdda1684-0ab6-4696-81e7-51416cbc56a7',1,'DKHD-001-01','asset_uuid_1',NULL,NULL,NULL,NULL,'SND_REVENUE_OVERDUE_FEE','70000.06','2','','TRD_REVENUE_OVERDUE_FEE_OBLIGATION','SND_REVENUE_OVERDUE_FEE.01',NULL);
/*!40000 ALTER TABLE `ledger_book_shelf` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `loan_batch`
--

DROP TABLE IF EXISTS `loan_batch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `loan_batch` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `is_available` bit(1) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `last_modified_time` datetime DEFAULT NULL,
  `loan_batch_uuid` varchar(255) DEFAULT NULL,
  `loan_date` datetime DEFAULT NULL,
  `financial_contract_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_s0kjo6tnych7ybxare3de7r59` (`financial_contract_id`),
  CONSTRAINT `FK_s0kjo6tnych7ybxare3de7r59` FOREIGN KEY (`financial_contract_id`) REFERENCES `financial_contract` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `loan_batch`
--

LOCK TABLES `loan_batch` WRITE;
/*!40000 ALTER TABLE `loan_batch` DISABLE KEYS */;
/*!40000 ALTER TABLE `loan_batch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_channel`
--

DROP TABLE IF EXISTS `payment_channel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment_channel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `api_url` varchar(255) DEFAULT NULL,
  `cer_file_path` varchar(255) DEFAULT NULL,
  `channel_name` varchar(255) DEFAULT NULL,
  `from_date` date DEFAULT NULL,
  `merchant_id` varchar(255) DEFAULT NULL,
  `payment_channel_type` int(11) DEFAULT NULL,
  `pfx_file_key` varchar(255) DEFAULT NULL,
  `pfx_file_path` varchar(255) DEFAULT NULL,
  `thru_date` date DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `user_password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_channel`
--

LOCK TABLES `payment_channel` WRITE;
/*!40000 ALTER TABLE `payment_channel` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment_channel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_account_table`
--

DROP TABLE IF EXISTS `t_account_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_account_table`
--

LOCK TABLES `t_account_table` WRITE;
/*!40000 ALTER TABLE `t_account_table` DISABLE KEYS */;
INSERT INTO `t_account_table` VALUES (1,'2016-09-13 22:47:53','20000','FST_RECIEVABLE_ASSET',1,'2016-09-13 22:47:53',2,'20000.05','SND_RECIEVABLE_OVERDUE_LOAN_ASSET',1,'20000.05.04','TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_TECH_FEE',1,'0e1e34db-59a3-4cc6-bbfe-e800b3a86b57'),(2,'2016-09-13 22:47:53','70000','FST_REVENUE',0,'2016-09-13 22:47:53',1,'70000.03','SND_REVENUE_INTEREST',0,'','',NULL,'4fe298a7-eeeb-4bf8-ae3c-5df15f38152a'),(3,'2016-09-13 22:47:53','60000','FST_BANK_SAVING',1,'2016-09-13 22:47:53',1,'60000.01','000191400205800',1,'','',NULL,'5197b3fc-13f1-4f97-8d38-f5f6420a48b9'),(4,'2016-09-13 22:47:53','100000','FST_DEFERRED_INCOME',0,'2016-09-13 22:47:53',2,'100000.02','SND_DEFERRED_INCOME_FEE',0,'100000.02.01','TRD_DEFERRED_INCOME_LOAN_SERVICE_FEE',0,'e4788d72-f6ae-4b21-b428-d8991219c2dd'),(5,'2016-09-13 22:47:53','20000','FST_RECIEVABLE_ASSET',1,'2016-09-13 22:47:53',2,'20000.01','SND_RECIEVABLE_LOAN_ASSET',1,'20000.01.02','TRD_RECIEVABLE_LOAN_ASSET_INTEREST',1,'34baa033-2109-4c8f-8008-78b1c4768348'),(6,'2016-09-13 22:47:53','20000','FST_RECIEVABLE_ASSET',1,'2016-09-13 22:47:53',2,'20000.06','SND_RECIEVABLE_OVERDUE_FEE',1,'20000.06.01','TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION',1,'4008e85b-7c72-411a-a0fd-d6da31ffea1d'),(7,'2016-09-13 22:47:53','100000','FST_DEFERRED_INCOME',0,'2016-09-13 22:47:53',1,'100000.02','SND_DEFERRED_INCOME_FEE',0,'','',NULL,'9b56c90f-775a-446c-a36b-4f7991ca6b1b'),(8,'2016-09-13 22:47:53','10000','FST_UNEARNED_LOAN_ASSET',1,'2016-09-13 22:47:53',1,'10000.01','SND_UNEARNED_LOAN_ASSET_INTEREST',1,'','',NULL,'f274e5b0-1414-4dbf-8a8b-1d76541fa572'),(9,'2016-09-13 22:47:53','70000','FST_REVENUE',0,'2016-09-13 22:47:53',1,'70000.01','SND_REVENUE_INVESTMENT_INCOMING',0,'','',NULL,'a41566a8-7955-4cb2-b026-d927e4c41f35'),(10,'2016-09-13 22:47:53','90000','FST_COST',1,'2016-09-13 22:47:53',0,'','',NULL,'','',NULL,'020cabab-6f0c-45fe-a93c-0dfa3857267b'),(11,'2016-09-13 22:47:53','100000','FST_DEFERRED_INCOME',0,'2016-09-13 22:47:53',0,'','',NULL,'','',NULL,'31e8a5f8-26a7-44a7-8833-015b6041519e'),(12,'2016-09-13 22:47:53','100000','FST_DEFERRED_INCOME',0,'2016-09-13 22:47:53',2,'100000.02','SND_DEFERRED_INCOME_FEE',0,'100000.02.03','TRD_DEFERRED_INCOME_LOAN_OTHER_FEE',0,'e109a37e-ca9a-431d-b44d-bb47a31bf0eb'),(13,'2016-09-13 22:47:53','60000','FST_BANK_SAVING',1,'2016-09-13 22:47:53',2,'60000.02','955103657777777',1,'60000.02.01','955103657777777_INDEPENDENT_ASSETS',1,'8b1bb91f-d40d-4941-87d4-90aa352577eb'),(14,'2016-09-13 22:47:53','20000','FST_RECIEVABLE_ASSET',1,'2016-09-13 22:47:53',1,'20000.05','SND_RECIEVABLE_OVERDUE_LOAN_ASSET',1,'','',NULL,'e46de2ac-07ab-4398-af14-dcecb78a09e8'),(15,'2016-09-13 22:47:53','50000','FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS',0,'2016-09-13 22:47:53',1,'50000.02','SND_LIABILITIES_INDEPENDENT_INTER_ACCOUNT_REMITTANCE',0,'','',NULL,'706829d1-d2c4-4ff1-b335-c29e74c7c5cb'),(16,'2016-09-13 22:47:53','70000','FST_REVENUE',0,'2016-09-13 22:47:53',2,'70000.05','SND_REVENUE_INCOME_FEE',0,'70000.05.03','TRD_REVENUE_INCOME_LOAN_OTHER_FEE',0,'912d9451-fe05-49af-8253-a8ee13f3a6f5'),(17,'2016-09-13 22:47:53','70000','FST_REVENUE',0,'2016-09-13 22:47:53',2,'70000.05','SND_REVENUE_INCOME_FEE',0,'70000.05.01','TRD_REVENUE_INCOME_LOAN_SERVICE_FEE',0,'c8fde5ea-3ee2-456a-91e6-ac2b61e8aa71'),(18,'2016-09-13 22:47:53','40000','FST_LONGTERM_LIABILITY',0,'2016-09-13 22:47:53',1,'40000.01','SND_LONGTERM_LIABILITY_ABSORB_SAVING',0,'','',NULL,'074eab5c-d96f-4c07-a9d6-81fa1fdb179c'),(19,'2016-09-13 22:47:53','20000','FST_RECIEVABLE_ASSET',1,'2016-09-13 22:47:53',2,'20000.01','SND_RECIEVABLE_LOAN_ASSET',1,'20000.01.03','TRD_RECIEVABLE_LOAN_ASSET_LOAN_SERVICE_FEE',1,'16c8924c-f0c2-4125-83ff-cb336069769c'),(20,'2016-09-13 22:47:53','30000','FST_PAYABLE_ASSET',0,'2016-09-13 22:47:53',1,'30000.03','SND_OTHER_PAYABLE_CUSTODY_SAVING',0,'','',NULL,'b15ef309-6272-4bf7-93a3-690b4d879ef2'),(21,'2016-09-13 22:47:53','70000','FST_REVENUE',0,'2016-09-13 22:47:53',2,'70000.06','SND_REVENUE_OVERDUE_FEE',0,'SND_REVENUE_OVERDUE_FEE.03','TRD_REVENUE_OVERDUE_FEE_OTHER_FEE',0,'2564ae40-e901-4418-958b-4f9c6ffe5168'),(22,'2016-09-13 22:47:53','70000','FST_REVENUE',0,'2016-09-13 22:47:53',1,'70000.04','SND_REVENUE_INVESTMENT_ESTIMATE',0,'','',NULL,'0a03d8a1-bb78-4729-a840-8a885eadb1f1'),(23,'2016-09-13 22:47:53','10000','FST_UNEARNED_LOAN_ASSET',1,'2016-09-13 22:47:53',1,'10000.02','SND_UNEARNED_LOAN_ASSET_PRINCIPLE',1,'','',NULL,'62d802f2-132a-41fb-b239-dd4c033d975f'),(24,'2016-09-13 22:47:53','10000','FST_UNEARNED_LOAN_ASSET',1,'2016-09-13 22:47:53',1,'10000.04','SND_UNEARNED_LOAN_ASSET_TECH_FEE',1,'','',NULL,'0097aef8-52b1-4cf8-aa13-979bdd41fac8'),(25,'2016-09-13 22:47:53','20000','FST_RECIEVABLE_ASSET',1,'2016-09-13 22:47:53',2,'20000.06','SND_RECIEVABLE_OVERDUE_FEE',1,'20000.06.02','TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE',1,'7efe0b26-47b6-4755-872c-171f936d12b3'),(26,'2016-09-13 22:47:53','30000','FST_PAYABLE_ASSET',0,'2016-09-13 22:47:53',1,'30000.02','SND_PAYABLE_CUSTODY_LOAN_PENALTY',0,'','',NULL,'15766325-3a8c-43ce-a044-ea678f598a36'),(27,'2016-09-13 22:47:53','50000','FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS',0,'2016-09-13 22:47:53',1,'50000.01','SND_LIABILITIES_INDEPENDENT_CUSTOMER_DEPOSIT',0,'','',NULL,'3ef11fa2-73b1-4143-a0e6-2e2c3b48d4fe'),(28,'2016-09-13 22:47:53','90000','FST_COST',1,'2016-09-13 22:47:53',1,'90000.03','SND_COST_COMMISSION',1,'','',NULL,'709f760a-a402-491d-8f50-727a1e5ce156'),(29,'2016-09-13 22:47:53','10000','FST_UNEARNED_LOAN_ASSET',1,'2016-09-13 22:47:53',0,'','',NULL,'','',NULL,'c64c0790-bd98-4b2a-9e82-43981d53748e'),(30,'2016-09-13 22:47:53','20000','FST_RECIEVABLE_ASSET',1,'2016-09-13 22:47:53',2,'20000.01','SND_RECIEVABLE_LOAN_ASSET',1,'20000.01.05','TRD_RECIEVABLE_LOAN_ASSET_OTHER_FEE',1,'7d967895-c85e-4a56-a3a1-036718f37e95'),(31,'2016-09-13 22:47:53','90000','FST_COST',1,'2016-09-13 22:47:53',1,'90000.01','SND_COST_REMITTANCE_FEE',1,'','',NULL,'c0da8dea-0d75-44ca-88fc-9a53bcbb78b8'),(32,'2016-09-13 22:47:53','20000','FST_RECIEVABLE_ASSET',1,'2016-09-13 22:47:53',2,'20000.05','SND_RECIEVABLE_OVERDUE_LOAN_ASSET',1,'20000.05.01','TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_PRINCIPLE',1,'9af33200-3533-4824-97af-9c82f4dee312'),(33,'2016-09-13 22:47:53','70000','FST_REVENUE',0,'2016-09-13 22:47:53',2,'70000.06','SND_REVENUE_OVERDUE_FEE',0,'SND_REVENUE_OVERDUE_FEE.02','TRD_REVENUE_OVERDUE_FEE_SERVICE_FEE',0,'3dd739ff-df48-4e6a-bf99-5d894288ff95'),(34,'2016-09-13 22:47:53','30000','FST_PAYABLE_ASSET',0,'2016-09-13 22:47:53',0,'','',NULL,'','',NULL,'3c615543-e44b-4c5f-868b-0345dff75957'),(35,'2016-09-13 22:47:53','50000','FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS',0,'2016-09-13 22:47:53',1,'50000.04','SND_LIABILITIES_INDEPENDENT_WITHDRAW_DEPOSIT',0,'','',NULL,'aa1abcd4-b231-480c-a14a-9eed1289bd35'),(36,'2016-09-13 22:47:53','20000','FST_RECIEVABLE_ASSET',1,'2016-09-13 22:47:53',2,'20000.06','SND_RECIEVABLE_OVERDUE_FEE',1,'20000.06.03','TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE',1,'e2269d43-70e2-4d82-982e-6a56e6c4bf22'),(37,'2016-09-13 22:47:53','40000','FST_LONGTERM_LIABILITY',0,'2016-09-13 22:47:53',0,'','',NULL,'','',NULL,'0d84006d-2ab4-47a8-808f-e82207e9fff9'),(38,'2016-09-13 22:47:53','20000','FST_RECIEVABLE_ASSET',1,'2016-09-13 22:47:53',1,'20000.02','SND_RECIEVABLE_LOAN_GUARANTEE',1,'','',NULL,'1278f88c-1880-4f40-9c8c-a23c02dfed12'),(39,'2016-09-13 22:47:53','60000','FST_BANK_SAVING',1,'2016-09-13 22:47:53',0,'','',NULL,'','',NULL,'fe6c71b4-ec2d-4742-ac3e-bdbae839c9af'),(40,'2016-09-13 22:47:53','30000','FST_PAYABLE_ASSET',0,'2016-09-13 22:47:53',1,'30000.05','SND_PAYABLE_REPURCHASE',0,'','',NULL,'69b15b28-e2eb-4e71-ab99-24a64ed284da'),(41,'2016-09-13 22:47:53','50000','FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS',0,'2016-09-13 22:47:53',0,'','',NULL,'','',NULL,'f5dd0971-04ce-43e0-bd82-ac98f89bb3e0'),(42,'2016-09-13 22:47:53','20000','FST_RECIEVABLE_ASSET',1,'2016-09-13 22:47:53',2,'20000.05','SND_RECIEVABLE_OVERDUE_LOAN_ASSET',1,'20000.05.03','TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_LOAN_SERVICE_FEE',1,'886fd793-30be-4f73-9b6a-0451adc57fd5'),(43,'2016-09-13 22:47:53','70000','FST_REVENUE',0,'2016-09-13 22:47:53',2,'70000.06','SND_REVENUE_OVERDUE_FEE',0,'SND_REVENUE_OVERDUE_FEE.01','TRD_REVENUE_OVERDUE_FEE_OBLIGATION',0,'05bb0f14-31a5-4a58-972b-4c27a0677564'),(44,'2016-09-13 22:47:53','70000','FST_REVENUE',0,'2016-09-13 22:47:53',0,'','',NULL,'','',NULL,'aec30a2c-c5a8-40b1-b44d-7a984d1f49cb'),(45,'2016-09-13 22:47:53','20000','FST_RECIEVABLE_ASSET',1,'2016-09-13 22:47:53',2,'20000.05','SND_RECIEVABLE_OVERDUE_LOAN_ASSET',1,'20000.05.02','TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_INTEREST',1,'6ab50e4d-d2bc-4ed2-a7db-191a06282c0c'),(46,'2016-09-13 22:47:53','20000','FST_RECIEVABLE_ASSET',1,'2016-09-13 22:47:53',1,'20000.06','SND_RECIEVABLE_OVERDUE_FEE',1,'','',NULL,'d40fc9cb-14c7-419d-8964-8d7d6bb4188a'),(47,'2016-09-13 22:47:53','20000','FST_RECIEVABLE_ASSET',1,'2016-09-13 22:47:53',2,'20000.05','SND_RECIEVABLE_OVERDUE_LOAN_ASSET',1,'20000.05.05','TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_OTHER_FEE',1,'853b1d26-63a3-4e4c-89a6-dceaf241a838'),(48,'2016-09-13 22:47:53','10000','FST_UNEARNED_LOAN_ASSET',1,'2016-09-13 22:47:53',1,'10000.05','SND_UNEARNED_LOAN_ASSET_OTHER_FEE',1,'','',NULL,'6424a2e9-fdf6-4de2-b5f8-8c80f9953bec'),(49,'2016-09-13 22:47:53','20000','FST_RECIEVABLE_ASSET',1,'2016-09-13 22:47:53',2,'20000.01','SND_RECIEVABLE_LOAN_ASSET',1,'20000.01.04','TRD_RECIEVABLE_LOAN_ASSET_TECH_FEE',1,'73526815-6ee5-4031-953f-aa0b287dd060'),(50,'2016-09-13 22:47:53','30000','FST_PAYABLE_ASSET',0,'2016-09-13 22:47:53',1,'30000.06','SND_PAYABLE_COMMISSION',0,'','',NULL,'79b6ca33-204e-49d9-b8a7-4a691d6b5eac'),(51,'2016-09-13 22:47:53','20000','FST_RECIEVABLE_ASSET',1,'2016-09-13 22:47:53',1,'20000.01','SND_RECIEVABLE_LOAN_ASSET',1,'','',NULL,'ef584b4d-8df5-4b92-89e1-b643f83e3340'),(52,'2016-09-13 22:47:53','100000','FST_DEFERRED_INCOME',0,'2016-09-13 22:47:53',1,'100000.01','SND_DEFERRED_INCOME_INTEREST_ESTIMATE',0,'','',NULL,'bc4a51c8-cc29-4f51-983e-4d0ae6a04084'),(53,'2016-09-13 22:47:53','30000','FST_PAYABLE_ASSET',0,'2016-09-13 22:47:53',1,'30000.08','SND_PAYABLE_REMITTANCE_FEE',0,'','',NULL,'e76a82a2-f04e-4660-96c2-41d2e78b3a6e'),(54,'2016-09-13 22:47:53','20000','FST_RECIEVABLE_ASSET',1,'2016-09-13 22:47:53',0,'','',NULL,'','',NULL,'7fc705b5-50b4-4d8f-b79f-ba78105eaadc'),(55,'2016-09-13 22:47:53','70000','FST_REVENUE',0,'2016-09-13 22:47:53',1,'70000.05','SND_REVENUE_INCOME_FEE',0,'','',NULL,'ec40883c-30a5-4a26-85ea-b5dc584deb3b'),(56,'2016-09-13 22:47:53','60000','FST_BANK_SAVING',1,'2016-09-13 22:47:53',1,'60000.03','19014526016005',1,'','',NULL,'90c653a3-4488-40be-a4fa-3d5bed7c9b56'),(57,'2016-09-13 22:47:53','90000','FST_COST',1,'2016-09-13 22:47:53',1,'90000.02','SND_COST_REFUND',1,'','',NULL,'b74a720c-94c2-49ad-a068-6a85986767d1'),(58,'2016-09-13 22:47:53','20000','FST_RECIEVABLE_ASSET',1,'2016-09-13 22:47:53',1,'20000.03','SND_RECIEVABLE_LOAN_PENALTY',1,'','',NULL,'7e4248dc-1b03-45c2-be90-e3a23e36867a'),(59,'2016-09-13 22:47:53','70000','FST_REVENUE',0,'2016-09-13 22:47:53',1,'70000.06','SND_REVENUE_OVERDUE_FEE',0,'','',NULL,'182edb9b-c46b-443d-a4e5-624bea2ee50d'),(60,'2016-09-13 22:47:53','30000','FST_PAYABLE_ASSET',0,'2016-09-13 22:47:53',1,'30000.04','SND_PAYABLE_CUSTODY_INTEREST',0,'','',NULL,'b8b1305f-e79a-403c-bc0b-593fdfea8508'),(61,'2016-09-13 22:47:53','80000','FST_FINANCIAL_ASSETS_SOLD_FOR_REPURCHASE',0,'2016-09-13 22:47:53',0,'','',NULL,'','',NULL,'f9239d4a-16be-4904-ab09-49dcc65528c2'),(62,'2016-09-13 22:47:53','60000','FST_BANK_SAVING',1,'2016-09-13 22:47:53',1,'60000.02','955103657777777',1,'','',NULL,'4ca5b040-310d-4e89-b76e-1bbbbe8f292c'),(63,'2016-09-13 22:47:53','50000','FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS',0,'2016-09-13 22:47:53',1,'50000.03','SND_LIABILITIES_INDEPENDENT_INTER_ACCOUNT_BENEFICIARY',0,'','',NULL,'3b59b18f-f9b6-43c2-8a92-93d99cc114e0'),(64,'2016-09-13 22:47:53','60000','FST_BANK_SAVING',1,'2016-09-13 22:47:53',2,'60000.03','19014526016005',1,'60000.03.01','19014526016005_INDEPENDENT_ASSETS',1,'e4a8f5b0-fad8-4dd4-89c2-a467f4de8b88'),(65,'2016-09-13 22:47:53','20000','FST_RECIEVABLE_ASSET',1,'2016-09-13 22:47:53',2,'20000.01','SND_RECIEVABLE_LOAN_ASSET',1,'20000.01.01','TRD_RECIEVABLE_LOAN_ASSET_PRINCIPLE',1,'cdbf5e1a-c18f-48f3-a03c-e0f295a5067a'),(66,'2016-09-13 22:47:53','30000','FST_PAYABLE_ASSET',0,'2016-09-13 22:47:53',1,'30000.07','SND_PAYABLE_REFUND',0,'','',NULL,'55997079-c8d9-456e-bc4c-c487fef222c7'),(67,'2016-09-13 22:47:53','100000','FST_DEFERRED_INCOME',0,'2016-09-13 22:47:53',2,'100000.02','SND_DEFERRED_INCOME_FEE',0,'100000.02.02','TRD_DEFERRED_INCOME_LOAN_TECH_FEE',0,'b8215ba4-103a-41d1-b798-82e30fb638f6'),(68,'2016-09-13 22:47:53','70000','FST_REVENUE',0,'2016-09-13 22:47:53',2,'70000.05','SND_REVENUE_INCOME_FEE',0,'70000.05.02','TRD_REVENUE_INCOME_LOAN_TECH_FEE',0,'33d6cf85-9d6b-40fe-9ea3-e6623a2267ed'),(69,'2016-09-13 22:47:53','10000','FST_UNEARNED_LOAN_ASSET',1,'2016-09-13 22:47:53',1,'10000.03','SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE',1,'','',NULL,'4fa17ad0-12ca-4d52-9278-87c4c8012ccc');
/*!40000 ALTER TABLE `t_account_table` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-09-28 20:24:33
