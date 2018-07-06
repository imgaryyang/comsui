# ************************************************************
# Sequel Pro SQL dump
# Version 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.6.24)
# Database: jpmorgan_dev
# Generation Time: 2016-07-27 09:38:00 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table payment_gateway_config
# ------------------------------------------------------------

DROP TABLE IF EXISTS `payment_gateway_config`;

CREATE TABLE `payment_gateway_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `config_key` varchar(255) DEFAULT NULL,
  `config_value` varchar(255) DEFAULT NULL,
  `decimal_field_one` decimal(19,2) DEFAULT NULL,
  `decimal_field_three` decimal(19,2) DEFAULT NULL,
  `decimal_field_two` decimal(19,2) DEFAULT NULL,
  `effective_date` datetime DEFAULT NULL,
  `expiration_date` datetime DEFAULT NULL,
  `gateway_online_status` int(11) DEFAULT NULL,
  `gateway_type` int(11) DEFAULT NULL,
  `gateway_working_status` int(11) DEFAULT NULL,
  `long_field_one` bigint(20) NOT NULL DEFAULT '0',
  `long_field_three` bigint(20) NOT NULL DEFAULT '0',
  `long_field_two` bigint(20) NOT NULL DEFAULT '0',
  `payment_channel_uuid` varchar(255) DEFAULT NULL,
  `payment_gateway_uuid` varchar(255) DEFAULT NULL,
  `string_field_one` varchar(255) DEFAULT NULL,
  `string_field_three` varchar(255) DEFAULT NULL,
  `string_field_two` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `payment_gateway_config` WRITE;
/*!40000 ALTER TABLE `payment_gateway_config` DISABLE KEYS */;

INSERT INTO `payment_gateway_config` (`id`, `config_key`, `config_value`, `decimal_field_one`, `decimal_field_three`, `decimal_field_two`, `effective_date`, `expiration_date`, `gateway_online_status`, `gateway_type`, `gateway_working_status`, `long_field_one`, `long_field_three`, `long_field_two`, `payment_channel_uuid`, `payment_gateway_uuid`, `string_field_one`, `string_field_three`, `string_field_two`)
VALUES
	(1,'businessStatusUpdateURL','http://127.0.0.1:9090/jpmorgan/paymentOrder/queryStatus',NULL,NULL,NULL,NULL,NULL,0,1,0,0,0,0,'f8bb9956-1952-4893-98c8-66683d25d7ce','b9d5d4be-0644-4f04-90b2-421bf0a2e45b',NULL,NULL,NULL),
	(2,'distributeURLKey','http://127.0.0.1:9090/jpmorgan/paymentOrder/distribute',NULL,NULL,NULL,NULL,NULL,0,1,0,0,0,0,'f8bb9956-1952-4893-98c8-66683d25d7ce','b9d5d4be-0644-4f04-90b2-421bf0a2e45b',NULL,NULL,NULL),
	(3,'gatewayName','GATEWAY_TYPE_SUPER_BANK_TEST',NULL,NULL,NULL,NULL,NULL,0,1,0,0,0,0,'f8bb9956-1952-4893-98c8-66683d25d7ce','b9d5d4be-0644-4f04-90b2-421bf0a2e45b',NULL,NULL,NULL),
	(4,'paymentOrderQueueTableName','payment_order_superbank_001',NULL,NULL,NULL,NULL,NULL,0,1,0,0,0,0,'f8bb9956-1952-4893-98c8-66683d25d7ce','b9d5d4be-0644-4f04-90b2-421bf0a2e45b',NULL,NULL,NULL),
	(5,'businessStatusUpdateURL','http://127.0.0.1:9090/jpmorgan/paymentOrder/queryStatus',NULL,NULL,NULL,NULL,NULL,0,2,0,0,0,0,'25821cae-2537-48fa-9722-5fa9d48ba02d','fe8e6d92-0e57-4368-92f5-f73e72c820d3',NULL,NULL,NULL),
	(6,'distributeURLKey','http://127.0.0.1:9090/jpmorgan/paymentOrder/distribute',NULL,NULL,NULL,NULL,NULL,0,2,0,0,0,0,'25821cae-2537-48fa-9722-5fa9d48ba02d','fe8e6d92-0e57-4368-92f5-f73e72c820d3',NULL,NULL,NULL),
	(7,'gatewayName','GATEWAY_TYPE_UNIONPAY_TEST',NULL,NULL,NULL,NULL,NULL,0,2,0,0,0,0,'25821cae-2537-48fa-9722-5fa9d48ba02d','fe8e6d92-0e57-4368-92f5-f73e72c820d3',NULL,NULL,NULL),
	(8,'paymentOrderQueueTableName','payment_order_gzunionpay_001',NULL,NULL,NULL,NULL,NULL,0,2,0,0,0,0,'25821cae-2537-48fa-9722-5fa9d48ba02d','fe8e6d92-0e57-4368-92f5-f73e72c820d3',NULL,NULL,NULL);

/*!40000 ALTER TABLE `payment_gateway_config` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table payment_order_gzunionpay_001
# ------------------------------------------------------------

DROP TABLE IF EXISTS `payment_order_gzunionpay_001`;

CREATE TABLE `payment_order_gzunionpay_001` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `access_version` varchar(255) DEFAULT NULL,
  `account_side` int(11) DEFAULT NULL,
  `business_status` int(11) DEFAULT NULL,
  `business_status_last_update_time` datetime DEFAULT NULL,
  `communication_end_time` datetime DEFAULT NULL,
  `communication_last_sent_time` datetime DEFAULT NULL,
  `communication_start_time` datetime DEFAULT NULL,
  `communication_status` int(11) DEFAULT NULL,
  `currency_code` varchar(255) DEFAULT NULL,
  `date_field_one` datetime DEFAULT NULL,
  `date_field_three` datetime DEFAULT NULL,
  `date_field_two` datetime DEFAULT NULL,
  `decimal_field_one` decimal(19,2) DEFAULT NULL,
  `decimal_field_three` decimal(19,2) DEFAULT NULL,
  `decimal_field_two` decimal(19,2) DEFAULT NULL,
  `destination_account_name` varchar(255) DEFAULT NULL,
  `destination_account_no` varchar(255) DEFAULT NULL,
  `destination_bank_info` varchar(255) NOT NULL,
  `fst_communication_status` int(11) DEFAULT NULL,
  `fst_occupied_feed_back_time` datetime DEFAULT NULL,
  `fst_occupied_force_stop_time` datetime DEFAULT NULL,
  `fst_occupied_message_record_uuid` varchar(255) DEFAULT NULL,
  `fst_occupied_result_record_uuid` varchar(255) DEFAULT NULL,
  `fst_occupied_sent_time` datetime DEFAULT NULL,
  `fst_occupied_time` datetime DEFAULT NULL,
  `fst_occupied_worker_ip` varchar(255) DEFAULT NULL,
  `fst_occuppied_worker_uuid` varchar(255) DEFAULT NULL,
  `fst_occupy_status` int(11) DEFAULT NULL,
  `gateway_type` int(11) DEFAULT NULL,
  `long_field_one` bigint(20) NOT NULL DEFAULT '0',
  `long_field_three` bigint(20) NOT NULL DEFAULT '0',
  `long_field_two` bigint(20) NOT NULL DEFAULT '0',
  `postscript` varchar(255) DEFAULT NULL,
  `snd_communication_status` int(11) DEFAULT NULL,
  `snd_occupied_feed_back_time` datetime DEFAULT NULL,
  `snd_occupied_force_stop_time` datetime DEFAULT NULL,
  `snd_occupied_message_record_uuid` varchar(255) DEFAULT NULL,
  `snd_occupied_result_record_uuid` varchar(255) DEFAULT NULL,
  `snd_occupied_sent_time` datetime DEFAULT NULL,
  `snd_occupied_time` datetime DEFAULT NULL,
  `snd_occupied_worker_ip` varchar(255) DEFAULT NULL,
  `snd_occuppied_worker_uuid` varchar(255) DEFAULT NULL,
  `snd_occupy_status` int(11) DEFAULT NULL,
  `source_account_name` varchar(255) DEFAULT NULL,
  `source_account_no` varchar(255) DEFAULT NULL,
  `source_bank_info` varchar(255) NOT NULL,
  `source_message_ip` varchar(255) DEFAULT NULL,
  `source_message_time` datetime DEFAULT NULL,
  `source_message_uuid` varchar(255) DEFAULT NULL,
  `string_field_one` varchar(255) DEFAULT NULL,
  `string_field_three` varchar(255) DEFAULT NULL,
  `string_field_two` varchar(255) DEFAULT NULL,
  `transaction_amount` decimal(19,2) DEFAULT NULL,
  `transaction_uuid` varchar(255) DEFAULT NULL,
  `trd_communication_status` int(11) DEFAULT NULL,
  `trd_occupied_feed_back_time` datetime DEFAULT NULL,
  `trd_occupied_force_stop_time` datetime DEFAULT NULL,
  `trd_occupied_message_record_uuid` varchar(255) DEFAULT NULL,
  `trd_occupied_result_record_uuid` varchar(255) DEFAULT NULL,
  `trd_occupied_sent_time` datetime DEFAULT NULL,
  `trd_occupied_time` datetime DEFAULT NULL,
  `trd_occupied_worker_ip` varchar(255) DEFAULT NULL,
  `trd_occuppied_worker_uuid` varchar(255) DEFAULT NULL,
  `trd_occupy_status` int(11) DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `gateway_router_info` varchar(255) DEFAULT NULL,
  `destination_account_appendix` varchar(255) DEFAULT NULL,
  `source_account_appendix` varchar(255) DEFAULT NULL,
  `outlier_transaction_uuid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table payment_order_superbank_001
# ------------------------------------------------------------

DROP TABLE IF EXISTS `payment_order_superbank_001`;

CREATE TABLE `payment_order_superbank_001` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `access_version` varchar(255) DEFAULT NULL,
  `account_side` int(11) DEFAULT NULL,
  `business_status` int(11) DEFAULT NULL,
  `business_status_last_update_time` datetime DEFAULT NULL,
  `communication_end_time` datetime DEFAULT NULL,
  `communication_last_sent_time` datetime DEFAULT NULL,
  `communication_start_time` datetime DEFAULT NULL,
  `communication_status` int(11) DEFAULT NULL,
  `currency_code` varchar(255) DEFAULT NULL,
  `date_field_one` datetime DEFAULT NULL,
  `date_field_three` datetime DEFAULT NULL,
  `date_field_two` datetime DEFAULT NULL,
  `decimal_field_one` decimal(19,2) DEFAULT NULL,
  `decimal_field_three` decimal(19,2) DEFAULT NULL,
  `decimal_field_two` decimal(19,2) DEFAULT NULL,
  `destination_account_name` varchar(255) DEFAULT NULL,
  `destination_account_no` varchar(255) DEFAULT NULL,
  `destination_bank_info` varchar(255) NOT NULL,
  `fst_communication_status` int(11) DEFAULT NULL,
  `fst_occupied_feed_back_time` datetime DEFAULT NULL,
  `fst_occupied_force_stop_time` datetime DEFAULT NULL,
  `fst_occupied_message_record_uuid` varchar(255) DEFAULT NULL,
  `fst_occupied_result_record_uuid` varchar(255) DEFAULT NULL,
  `fst_occupied_sent_time` datetime DEFAULT NULL,
  `fst_occupied_time` datetime DEFAULT NULL,
  `fst_occupied_worker_ip` varchar(255) DEFAULT NULL,
  `fst_occuppied_worker_uuid` varchar(255) DEFAULT NULL,
  `fst_occupy_status` int(11) DEFAULT NULL,
  `gateway_type` int(11) DEFAULT NULL,
  `long_field_one` bigint(20) NOT NULL DEFAULT '0',
  `long_field_three` bigint(20) NOT NULL DEFAULT '0',
  `long_field_two` bigint(20) NOT NULL DEFAULT '0',
  `postscript` varchar(255) DEFAULT NULL,
  `snd_communication_status` int(11) DEFAULT NULL,
  `snd_occupied_feed_back_time` datetime DEFAULT NULL,
  `snd_occupied_force_stop_time` datetime DEFAULT NULL,
  `snd_occupied_message_record_uuid` varchar(255) DEFAULT NULL,
  `snd_occupied_result_record_uuid` varchar(255) DEFAULT NULL,
  `snd_occupied_sent_time` datetime DEFAULT NULL,
  `snd_occupied_time` datetime DEFAULT NULL,
  `snd_occupied_worker_ip` varchar(255) DEFAULT NULL,
  `snd_occuppied_worker_uuid` varchar(255) DEFAULT NULL,
  `snd_occupy_status` int(11) DEFAULT NULL,
  `source_account_name` varchar(255) DEFAULT NULL,
  `source_account_no` varchar(255) DEFAULT NULL,
  `source_bank_info` varchar(255) NOT NULL,
  `source_message_ip` varchar(255) DEFAULT NULL,
  `source_message_time` datetime DEFAULT NULL,
  `source_message_uuid` varchar(255) DEFAULT NULL,
  `string_field_one` varchar(255) DEFAULT NULL,
  `string_field_three` varchar(255) DEFAULT NULL,
  `string_field_two` varchar(255) DEFAULT NULL,
  `transaction_amount` decimal(19,2) DEFAULT NULL,
  `transaction_uuid` varchar(255) DEFAULT NULL,
  `trd_communication_status` int(11) DEFAULT NULL,
  `trd_occupied_feed_back_time` datetime DEFAULT NULL,
  `trd_occupied_force_stop_time` datetime DEFAULT NULL,
  `trd_occupied_message_record_uuid` varchar(255) DEFAULT NULL,
  `trd_occupied_result_record_uuid` varchar(255) DEFAULT NULL,
  `trd_occupied_sent_time` datetime DEFAULT NULL,
  `trd_occupied_time` datetime DEFAULT NULL,
  `trd_occupied_worker_ip` varchar(255) DEFAULT NULL,
  `trd_occuppied_worker_uuid` varchar(255) DEFAULT NULL,
  `trd_occupy_status` int(11) DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `gateway_router_info` varchar(255) DEFAULT NULL,
  `destination_account_appendix` varchar(255) DEFAULT NULL,
  `source_account_appendix` varchar(255) DEFAULT NULL,
  `outlier_transaction_uuid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table payment_order_test
# ------------------------------------------------------------

DROP TABLE IF EXISTS `payment_order_test`;

CREATE TABLE `payment_order_test` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `worker_uuid` varchar(255) DEFAULT NULL,
  `order_uuid` varchar(255) DEFAULT NULL,
  `slot` int(10) DEFAULT NULL,
  `occupy_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table payment_order_worker_config
# ------------------------------------------------------------

DROP TABLE IF EXISTS `payment_order_worker_config`;

CREATE TABLE `payment_order_worker_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ip` varchar(255) DEFAULT NULL,
  `local_working_config` text,
  `payment_gateway_uuid` varchar(255) DEFAULT NULL,
  `payment_order_worker_uuid` varchar(255) DEFAULT NULL,
  `port` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `payment_order_worker_config` WRITE;
/*!40000 ALTER TABLE `payment_order_worker_config` DISABLE KEYS */;

INSERT INTO `payment_order_worker_config` (`id`, `ip`, `local_working_config`, `payment_gateway_uuid`, `payment_order_worker_uuid`, `port`, `url`)
VALUES
	(1,NULL,NULL,'b9d5d4be-0644-4f04-90b2-421bf0a2e45b','030f6ca5-558a-47d6-a259-6cf9d6842000','',''),
	(2,NULL,NULL,'fe8e6d92-0e57-4368-92f5-f73e72c820d3','030f6ca5-558a-47d6-a259-6cf9d6842001','','');

/*!40000 ALTER TABLE `payment_order_worker_config` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table schedule_job
# ------------------------------------------------------------

DROP TABLE IF EXISTS `schedule_job`;

CREATE TABLE `schedule_job` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_uuid` varchar(255) DEFAULT NULL,
  `job_name` varchar(255) DEFAULT NULL,
  `job_group` varchar(255) DEFAULT NULL,
  `job_status` int(11) DEFAULT NULL,
  `cron_expression` varchar(255) DEFAULT NULL,
  `desc` varchar(255) DEFAULT NULL,
  `worker_uuid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `schedule_job` WRITE;
/*!40000 ALTER TABLE `schedule_job` DISABLE KEYS */;

INSERT INTO `schedule_job` (`id`, `job_uuid`, `job_name`, `job_group`, `job_status`, `cron_expression`, `desc`, `worker_uuid`)
VALUES
	(1,'d1760acd-804f-4cfe-aa82-f69bc3e7004d','distributeJob01','distributeJob',1,'0/1 * * * * ?',NULL,'d1760acd-804f-4cfe-aa82-f69bc3e7004d'),
	(2,'030f6ca5-558a-47d6-a259-6cf9d6842000','paymentJob_superbank','paymentJob',1,'0/1 * * * * ?',NULL,'030f6ca5-558a-47d6-a259-6cf9d6842000'),
	(3,'030f6ca5-558a-47d6-a259-6cf9d6842001','paymentJob_gzunion','paymentJob',1,'0/1 * * * * ?',NULL,'030f6ca5-558a-47d6-a259-6cf9d6842001'),
	(4,'3b4e8205-e6b2-4a80-9abe-7904f88aae74','businessJob_superbank','businessStatusJob',1,'0/1 * * * * ?',NULL,'030f6ca5-558a-47d6-a259-6cf9d6842000'),
	(5,'faec158d-d594-4eb6-9260-34978132d5c7','businessJob_gzunion','businessStatusJob',1,'0/1 * * * * ?',NULL,'030f6ca5-558a-47d6-a259-6cf9d6842001'),
	(6,'faddf8cf-0843-4b5a-8ca2-aa535522649b','statusWritebackJob01','statusWritebackJob',1,'20/1 * * * * ? ',NULL,'faddf8cf-0843-4b5a-8ca2-aa535522649b');

/*!40000 ALTER TABLE `schedule_job` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table trade_schedule
# ------------------------------------------------------------

DROP TABLE IF EXISTS `trade_schedule`;

CREATE TABLE `trade_schedule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `access_version` varchar(255) DEFAULT NULL,
  `account_side` int(11) DEFAULT NULL,
  `business_status` int(11) DEFAULT NULL,
  `business_status_last_update_time` datetime DEFAULT NULL,
  `currency_code` varchar(255) DEFAULT NULL,
  `date_field_one` datetime DEFAULT NULL,
  `date_field_three` datetime DEFAULT NULL,
  `date_field_two` datetime DEFAULT NULL,
  `decimal_field_one` decimal(19,2) DEFAULT NULL,
  `decimal_field_three` decimal(19,2) DEFAULT NULL,
  `decimal_field_two` decimal(19,2) DEFAULT NULL,
  `destination_account_appendix` text,
  `destination_account_name` varchar(255) DEFAULT NULL,
  `destination_account_no` varchar(255) DEFAULT NULL,
  `destination_bank_info` text,
  `fst_business_failed_reason` varchar(255) DEFAULT NULL,
  `fst_business_status` int(11) DEFAULT NULL,
  `fst_communication_end_time` datetime DEFAULT NULL,
  `fst_communication_last_sent_time` datetime DEFAULT NULL,
  `fst_communication_start_time` datetime DEFAULT NULL,
  `fst_communication_status` int(11) DEFAULT NULL,
  `fst_effective_absolute_time` datetime DEFAULT NULL,
  `fst_gateway_router_info` text,
  `fst_gateway_router_result` varchar(255) DEFAULT NULL,
  `fst_gateway_type` int(11) DEFAULT NULL,
  `fst_payment_channel_uuid` varchar(255) DEFAULT NULL,
  `fst_slot_uuid` varchar(255) DEFAULT NULL,
  `fst_transaction_amount` decimal(19,2) DEFAULT NULL,
  `fst_transaction_begin_time` datetime DEFAULT NULL,
  `fst_transaction_end_time` datetime DEFAULT NULL,
  `fst_worker_uuid` varchar(255) DEFAULT NULL,
  `fth_business_failed_reason` varchar(255) DEFAULT NULL,
  `fth_business_status` int(11) DEFAULT NULL,
  `fth_communication_end_time` datetime DEFAULT NULL,
  `fth_communication_last_sent_time` datetime DEFAULT NULL,
  `fth_communication_start_time` datetime DEFAULT NULL,
  `fth_communication_status` int(11) DEFAULT NULL,
  `fth_effective_absolute_time` datetime DEFAULT NULL,
  `fth_gateway_router_info` text,
  `fth_gateway_router_result` varchar(255) DEFAULT NULL,
  `fth_gateway_type` int(11) DEFAULT NULL,
  `fth_payment_channel_uuid` varchar(255) DEFAULT NULL,
  `fth_slot_uuid` varchar(255) DEFAULT NULL,
  `fth_transaction_amount` decimal(19,2) DEFAULT NULL,
  `fth_transaction_begin_time` datetime DEFAULT NULL,
  `fth_transaction_end_time` datetime DEFAULT NULL,
  `fth_worker_uuid` varchar(255) DEFAULT NULL,
  `fvth_business_failed_reason` varchar(255) DEFAULT NULL,
  `fvth_business_status` int(11) DEFAULT NULL,
  `fvth_communication_end_time` datetime DEFAULT NULL,
  `fvth_communication_last_sent_time` datetime DEFAULT NULL,
  `fvth_communication_start_time` datetime DEFAULT NULL,
  `fvth_communication_status` int(11) DEFAULT NULL,
  `fvth_effective_absolute_time` datetime DEFAULT NULL,
  `fvth_gateway_router_info` text,
  `fvth_gateway_router_result` varchar(255) DEFAULT NULL,
  `fvth_gateway_type` int(11) DEFAULT NULL,
  `fvth_payment_channel_uuid` varchar(255) DEFAULT NULL,
  `fvth_slot_uuid` varchar(255) DEFAULT NULL,
  `fvth_transaction_amount` decimal(19,2) DEFAULT NULL,
  `fvth_transaction_begin_time` datetime DEFAULT NULL,
  `fvth_transaction_end_time` datetime DEFAULT NULL,
  `fvth_worker_uuid` varchar(255) DEFAULT NULL,
  `long_field_one` bigint(20) NOT NULL,
  `long_field_three` bigint(20) NOT NULL,
  `long_field_two` bigint(20) NOT NULL,
  `out_lier_transaction_uuid` varchar(255) DEFAULT NULL,
  `postscript` varchar(255) DEFAULT NULL,
  `snd_business_failed_reason` varchar(255) DEFAULT NULL,
  `snd_business_status` int(11) DEFAULT NULL,
  `snd_communication_end_time` datetime DEFAULT NULL,
  `snd_communication_last_sent_time` datetime DEFAULT NULL,
  `snd_communication_start_time` datetime DEFAULT NULL,
  `snd_communication_status` int(11) DEFAULT NULL,
  `snd_effective_absolute_time` datetime DEFAULT NULL,
  `snd_gateway_router_info` text,
  `snd_gateway_router_result` varchar(255) DEFAULT NULL,
  `snd_gateway_type` int(11) DEFAULT NULL,
  `snd_payment_channel_uuid` varchar(255) DEFAULT NULL,
  `snd_slot_uuid` varchar(255) DEFAULT NULL,
  `snd_transaction_amount` decimal(19,2) DEFAULT NULL,
  `snd_transaction_begin_time` datetime DEFAULT NULL,
  `snd_transaction_end_time` datetime DEFAULT NULL,
  `snd_worker_uuid` varchar(255) DEFAULT NULL,
  `source_account_appendix` text,
  `source_account_name` varchar(255) DEFAULT NULL,
  `source_account_no` varchar(255) DEFAULT NULL,
  `source_bank_info` text,
  `source_message_ip` varchar(255) DEFAULT NULL,
  `source_message_time` datetime DEFAULT NULL,
  `source_message_uuid` varchar(255) DEFAULT NULL,
  `string_field_one` varchar(255) DEFAULT NULL,
  `string_field_three` varchar(255) DEFAULT NULL,
  `string_field_two` varchar(255) DEFAULT NULL,
  `trade_uuid` varchar(255) DEFAULT NULL,
  `trd_business_failed_reason` varchar(255) DEFAULT NULL,
  `trd_business_status` int(11) DEFAULT NULL,
  `trd_communication_end_time` datetime DEFAULT NULL,
  `trd_communication_last_sent_time` datetime DEFAULT NULL,
  `trd_communication_start_time` datetime DEFAULT NULL,
  `trd_communication_status` int(11) DEFAULT NULL,
  `trd_effective_absolute_time` datetime DEFAULT NULL,
  `trd_gateway_router_info` text,
  `trd_gateway_router_result` varchar(255) DEFAULT NULL,
  `trd_gateway_type` int(11) DEFAULT NULL,
  `trd_payment_channel_uuid` varchar(255) DEFAULT NULL,
  `trd_slot_uuid` varchar(255) DEFAULT NULL,
  `trd_transaction_amount` decimal(19,2) DEFAULT NULL,
  `trd_transaction_begin_time` datetime DEFAULT NULL,
  `trd_transaction_end_time` datetime DEFAULT NULL,
  `trd_worker_uuid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table trade_schedule_log
# ------------------------------------------------------------

DROP TABLE IF EXISTS `trade_schedule_log`;

CREATE TABLE `trade_schedule_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `access_version` varchar(255) DEFAULT NULL,
  `account_side` int(11) DEFAULT NULL,
  `business_status` int(11) DEFAULT NULL,
  `business_status_last_update_time` datetime DEFAULT NULL,
  `currency_code` varchar(255) DEFAULT NULL,
  `date_field_one` datetime DEFAULT NULL,
  `date_field_three` datetime DEFAULT NULL,
  `date_field_two` datetime DEFAULT NULL,
  `decimal_field_one` decimal(19,2) DEFAULT NULL,
  `decimal_field_three` decimal(19,2) DEFAULT NULL,
  `decimal_field_two` decimal(19,2) DEFAULT NULL,
  `destination_account_appendix` text,
  `destination_account_name` varchar(255) DEFAULT NULL,
  `destination_account_no` varchar(255) DEFAULT NULL,
  `destination_bank_info` text,
  `fst_business_failed_reason` varchar(255) DEFAULT NULL,
  `fst_business_status` int(11) DEFAULT NULL,
  `fst_communication_end_time` datetime DEFAULT NULL,
  `fst_communication_last_sent_time` datetime DEFAULT NULL,
  `fst_communication_start_time` datetime DEFAULT NULL,
  `fst_communication_status` int(11) DEFAULT NULL,
  `fst_effective_absolute_time` datetime DEFAULT NULL,
  `fst_gateway_router_info` text,
  `fst_gateway_router_result` varchar(255) DEFAULT NULL,
  `fst_gateway_type` int(11) DEFAULT NULL,
  `fst_payment_channel_uuid` varchar(255) DEFAULT NULL,
  `fst_slot_uuid` varchar(255) DEFAULT NULL,
  `fst_transaction_amount` decimal(19,2) DEFAULT NULL,
  `fst_transaction_begin_time` datetime DEFAULT NULL,
  `fst_transaction_end_time` datetime DEFAULT NULL,
  `fst_worker_uuid` varchar(255) DEFAULT NULL,
  `fth_business_failed_reason` varchar(255) DEFAULT NULL,
  `fth_business_status` int(11) DEFAULT NULL,
  `fth_communication_end_time` datetime DEFAULT NULL,
  `fth_communication_last_sent_time` datetime DEFAULT NULL,
  `fth_communication_start_time` datetime DEFAULT NULL,
  `fth_communication_status` int(11) DEFAULT NULL,
  `fth_effective_absolute_time` datetime DEFAULT NULL,
  `fth_gateway_router_info` text,
  `fth_gateway_router_result` varchar(255) DEFAULT NULL,
  `fth_gateway_type` int(11) DEFAULT NULL,
  `fth_payment_channel_uuid` varchar(255) DEFAULT NULL,
  `fth_slot_uuid` varchar(255) DEFAULT NULL,
  `fth_transaction_amount` decimal(19,2) DEFAULT NULL,
  `fth_transaction_begin_time` datetime DEFAULT NULL,
  `fth_transaction_end_time` datetime DEFAULT NULL,
  `fth_worker_uuid` varchar(255) DEFAULT NULL,
  `fvth_business_failed_reason` varchar(255) DEFAULT NULL,
  `fvth_business_status` int(11) DEFAULT NULL,
  `fvth_communication_end_time` datetime DEFAULT NULL,
  `fvth_communication_last_sent_time` datetime DEFAULT NULL,
  `fvth_communication_start_time` datetime DEFAULT NULL,
  `fvth_communication_status` int(11) DEFAULT NULL,
  `fvth_effective_absolute_time` datetime DEFAULT NULL,
  `fvth_gateway_router_info` text,
  `fvth_gateway_router_result` varchar(255) DEFAULT NULL,
  `fvth_gateway_type` int(11) DEFAULT NULL,
  `fvth_payment_channel_uuid` varchar(255) DEFAULT NULL,
  `fvth_slot_uuid` varchar(255) DEFAULT NULL,
  `fvth_transaction_amount` decimal(19,2) DEFAULT NULL,
  `fvth_transaction_begin_time` datetime DEFAULT NULL,
  `fvth_transaction_end_time` datetime DEFAULT NULL,
  `fvth_worker_uuid` varchar(255) DEFAULT NULL,
  `long_field_one` bigint(20) NOT NULL,
  `long_field_three` bigint(20) NOT NULL,
  `long_field_two` bigint(20) NOT NULL,
  `out_lier_transaction_uuid` varchar(255) DEFAULT NULL,
  `postscript` varchar(255) DEFAULT NULL,
  `snd_business_failed_reason` varchar(255) DEFAULT NULL,
  `snd_business_status` int(11) DEFAULT NULL,
  `snd_communication_end_time` datetime DEFAULT NULL,
  `snd_communication_last_sent_time` datetime DEFAULT NULL,
  `snd_communication_start_time` datetime DEFAULT NULL,
  `snd_communication_status` int(11) DEFAULT NULL,
  `snd_effective_absolute_time` datetime DEFAULT NULL,
  `snd_gateway_router_info` text,
  `snd_gateway_router_result` varchar(255) DEFAULT NULL,
  `snd_gateway_type` int(11) DEFAULT NULL,
  `snd_payment_channel_uuid` varchar(255) DEFAULT NULL,
  `snd_slot_uuid` varchar(255) DEFAULT NULL,
  `snd_transaction_amount` decimal(19,2) DEFAULT NULL,
  `snd_transaction_begin_time` datetime DEFAULT NULL,
  `snd_transaction_end_time` datetime DEFAULT NULL,
  `snd_worker_uuid` varchar(255) DEFAULT NULL,
  `source_account_appendix` text,
  `source_account_name` varchar(255) DEFAULT NULL,
  `source_account_no` varchar(255) DEFAULT NULL,
  `source_bank_info` text,
  `source_message_ip` varchar(255) DEFAULT NULL,
  `source_message_time` datetime DEFAULT NULL,
  `source_message_uuid` varchar(255) DEFAULT NULL,
  `string_field_one` varchar(255) DEFAULT NULL,
  `string_field_three` varchar(255) DEFAULT NULL,
  `string_field_two` varchar(255) DEFAULT NULL,
  `trade_uuid` varchar(255) DEFAULT NULL,
  `trd_business_failed_reason` varchar(255) DEFAULT NULL,
  `trd_business_status` int(11) DEFAULT NULL,
  `trd_communication_end_time` datetime DEFAULT NULL,
  `trd_communication_last_sent_time` datetime DEFAULT NULL,
  `trd_communication_start_time` datetime DEFAULT NULL,
  `trd_communication_status` int(11) DEFAULT NULL,
  `trd_effective_absolute_time` datetime DEFAULT NULL,
  `trd_gateway_router_info` text,
  `trd_gateway_router_result` varchar(255) DEFAULT NULL,
  `trd_gateway_type` int(11) DEFAULT NULL,
  `trd_payment_channel_uuid` varchar(255) DEFAULT NULL,
  `trd_slot_uuid` varchar(255) DEFAULT NULL,
  `trd_transaction_amount` decimal(19,2) DEFAULT NULL,
  `trd_transaction_begin_time` datetime DEFAULT NULL,
  `trd_transaction_end_time` datetime DEFAULT NULL,
  `trd_worker_uuid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
