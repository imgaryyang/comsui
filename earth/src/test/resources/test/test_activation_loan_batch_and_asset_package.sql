SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM `loan_batch`;
DELETE FROM `asset_package`;

INSERT INTO `loan_batch` (`id`, `factoring_contract_id`, `code`, `is_available`, `loan_date`, `create_time`, `last_modified_time`)
VALUES ('1', '58', 'DCF-YPK-LR903A-2016-01-21', '\0', NULL, '2016-02-01 15:14:21', '2016-02-01 15:14:21');
INSERT INTO `loan_batch` (`id`, `factoring_contract_id`, `code`, `is_available`, `loan_date`, `create_time`, `last_modified_time`)
VALUES ('2', '58', 'DCF-YPK-LR903A-2016-01-28', '\0', NULL, '2016-02-01 15:15:18', '2016-02-01 15:15:18');

INSERT INTO `asset_package` (`id`, `adva_start_date`, `is_available`, `bank_account`, `bank_name`, `create_time`, `latest_settle_date`, `otsd_amt`, `otsd_amt_aval`, `rec_transfee_total_amt`, `remove_time`, `contract_id`, `factoring_contract_id`, `thru_date`, `effective_asset_start_date`, `receivables_transferee_date`, `effective_asset_deadline_date`, `loan_batch_id`)
VALUES
  ('961', NULL, '\0', NULL, NULL, '2016-02-01 15:14:21', NULL, '92400.00', '92400.00', '132000.00', NULL, '1410', '58',
   NULL, '2015-12-25 00:00:00', '2016-01-21 00:00:00', '2016-12-24 00:00:00', '1');
INSERT INTO `asset_package` (`id`, `adva_start_date`, `is_available`, `bank_account`, `bank_name`, `create_time`, `latest_settle_date`, `otsd_amt`, `otsd_amt_aval`, `rec_transfee_total_amt`, `remove_time`, `contract_id`, `factoring_contract_id`, `thru_date`, `effective_asset_start_date`, `receivables_transferee_date`, `effective_asset_deadline_date`, `loan_batch_id`)
VALUES
  ('962', NULL, '\0', NULL, NULL, '2016-02-01 15:14:22', NULL, '61792.50', '61792.50', '88275.00', NULL, '1411', '58',
   NULL, '2015-12-25 00:00:00', '2016-01-21 00:00:00', '2016-12-24 00:00:00', '1');
INSERT INTO `asset_package` (`id`, `adva_start_date`, `is_available`, `bank_account`, `bank_name`, `create_time`, `latest_settle_date`, `otsd_amt`, `otsd_amt_aval`, `rec_transfee_total_amt`, `remove_time`, `contract_id`, `factoring_contract_id`, `thru_date`, `effective_asset_start_date`, `receivables_transferee_date`, `effective_asset_deadline_date`, `loan_batch_id`)
VALUES ('963', NULL, '\0', NULL, NULL, '2016-02-01 15:14:22', NULL, '151200.00', '151200.00', '216000.00', NULL, '1412',
        '58', NULL, '2015-12-25 00:00:00', '2016-01-21 00:00:00', '2016-12-24 00:00:00', '1');
INSERT INTO `asset_package` (`id`, `adva_start_date`, `is_available`, `bank_account`, `bank_name`, `create_time`, `latest_settle_date`, `otsd_amt`, `otsd_amt_aval`, `rec_transfee_total_amt`, `remove_time`, `contract_id`, `factoring_contract_id`, `thru_date`, `effective_asset_start_date`, `receivables_transferee_date`, `effective_asset_deadline_date`, `loan_batch_id`)
VALUES ('964', NULL, '\0', NULL, NULL, '2016-02-01 15:14:22', NULL, '154000.00', '154000.00', '220000.00', NULL, '1413',
        '58', NULL, '2015-12-25 00:00:00', '2016-01-21 00:00:00', '2016-12-24 00:00:00', '1');
INSERT INTO `asset_package` (`id`, `adva_start_date`, `is_available`, `bank_account`, `bank_name`, `create_time`, `latest_settle_date`, `otsd_amt`, `otsd_amt_aval`, `rec_transfee_total_amt`, `remove_time`, `contract_id`, `factoring_contract_id`, `thru_date`, `effective_asset_start_date`, `receivables_transferee_date`, `effective_asset_deadline_date`, `loan_batch_id`)
VALUES
  ('965', NULL, '\0', NULL, NULL, '2016-02-01 15:14:22', NULL, '94500.00', '94500.00', '135000.00', NULL, '1414', '58',
   NULL, '2015-12-25 00:00:00', '2016-01-21 00:00:00', '2016-12-24 00:00:00', '1');
INSERT INTO `asset_package` (`id`, `adva_start_date`, `is_available`, `bank_account`, `bank_name`, `create_time`, `latest_settle_date`, `otsd_amt`, `otsd_amt_aval`, `rec_transfee_total_amt`, `remove_time`, `contract_id`, `factoring_contract_id`, `thru_date`, `effective_asset_start_date`, `receivables_transferee_date`, `effective_asset_deadline_date`, `loan_batch_id`)
VALUES
  ('966', NULL, '\0', NULL, NULL, '2016-02-01 15:14:22', NULL, '77000.00', '77000.00', '110000.00', NULL, '1415', '58',
   NULL, '2015-12-25 00:00:00', '2016-01-21 00:00:00', '2016-12-24 00:00:00', '1');
INSERT INTO `asset_package` (`id`, `adva_start_date`, `is_available`, `bank_account`, `bank_name`, `create_time`, `latest_settle_date`, `otsd_amt`, `otsd_amt_aval`, `rec_transfee_total_amt`, `remove_time`, `contract_id`, `factoring_contract_id`, `thru_date`, `effective_asset_start_date`, `receivables_transferee_date`, `effective_asset_deadline_date`, `loan_batch_id`)
VALUES ('967', NULL, '\0', NULL, NULL, '2016-02-01 15:14:22', NULL, '106260.00', '106260.00', '151800.00', NULL, '1416',
        '58', NULL, '2015-12-25 00:00:00', '2016-01-21 00:00:00', '2016-12-24 00:00:00', '1');
INSERT INTO `asset_package` (`id`, `adva_start_date`, `is_available`, `bank_account`, `bank_name`, `create_time`, `latest_settle_date`, `otsd_amt`, `otsd_amt_aval`, `rec_transfee_total_amt`, `remove_time`, `contract_id`, `factoring_contract_id`, `thru_date`, `effective_asset_start_date`, `receivables_transferee_date`, `effective_asset_deadline_date`, `loan_batch_id`)
VALUES ('968', NULL, '\0', NULL, NULL, '2016-02-01 15:14:22', NULL, '100100.00', '100100.00', '143000.00', NULL, '1417',
        '58', NULL, '2015-12-25 00:00:00', '2016-01-21 00:00:00', '2016-12-24 00:00:00', '1');
INSERT INTO `asset_package` (`id`, `adva_start_date`, `is_available`, `bank_account`, `bank_name`, `create_time`, `latest_settle_date`, `otsd_amt`, `otsd_amt_aval`, `rec_transfee_total_amt`, `remove_time`, `contract_id`, `factoring_contract_id`, `thru_date`, `effective_asset_start_date`, `receivables_transferee_date`, `effective_asset_deadline_date`, `loan_batch_id`)
VALUES
  ('969', NULL, '\0', NULL, NULL, '2016-02-01 15:14:22', NULL, '87500.00', '87500.00', '125000.00', NULL, '1418', '58',
   NULL, '2015-12-25 00:00:00', '2016-01-21 00:00:00', '2016-12-24 00:00:00', '1');
INSERT INTO `asset_package` (`id`, `adva_start_date`, `is_available`, `bank_account`, `bank_name`, `create_time`, `latest_settle_date`, `otsd_amt`, `otsd_amt_aval`, `rec_transfee_total_amt`, `remove_time`, `contract_id`, `factoring_contract_id`, `thru_date`, `effective_asset_start_date`, `receivables_transferee_date`, `effective_asset_deadline_date`, `loan_batch_id`)
VALUES ('970', NULL, '\0', NULL, NULL, '2016-02-01 15:14:23', NULL, '169400.00', '169400.00', '242000.00', NULL, '1419',
        '58', NULL, '2015-12-25 00:00:00', '2016-01-21 00:00:00', '2016-12-24 00:00:00', '1');
INSERT INTO `asset_package` (`id`, `adva_start_date`, `is_available`, `bank_account`, `bank_name`, `create_time`, `latest_settle_date`, `otsd_amt`, `otsd_amt_aval`, `rec_transfee_total_amt`, `remove_time`, `contract_id`, `factoring_contract_id`, `thru_date`, `effective_asset_start_date`, `receivables_transferee_date`, `effective_asset_deadline_date`, `loan_batch_id`)
VALUES
  ('971', NULL, '\0', NULL, NULL, '2016-02-01 15:14:23', NULL, '66500.00', '66500.00', '95000.00', NULL, '1420', '58',
   NULL, '2015-12-25 00:00:00', '2016-01-21 00:00:00', '2016-12-24 00:00:00', '1');
INSERT INTO `asset_package` (`id`, `adva_start_date`, `is_available`, `bank_account`, `bank_name`, `create_time`, `latest_settle_date`, `otsd_amt`, `otsd_amt_aval`, `rec_transfee_total_amt`, `remove_time`, `contract_id`, `factoring_contract_id`, `thru_date`, `effective_asset_start_date`, `receivables_transferee_date`, `effective_asset_deadline_date`, `loan_batch_id`)
VALUES ('972', NULL, '\0', NULL, NULL, '2016-02-01 15:14:23', NULL, '210000.00', '210000.00', '300000.00', NULL, '1421',
        '58', NULL, '2015-12-25 00:00:00', '2016-01-21 00:00:00', '2016-12-24 00:00:00', '1');
INSERT INTO `asset_package` (`id`, `adva_start_date`, `is_available`, `bank_account`, `bank_name`, `create_time`, `latest_settle_date`, `otsd_amt`, `otsd_amt_aval`, `rec_transfee_total_amt`, `remove_time`, `contract_id`, `factoring_contract_id`, `thru_date`, `effective_asset_start_date`, `receivables_transferee_date`, `effective_asset_deadline_date`, `loan_batch_id`)
VALUES
  ('973', NULL, '\0', NULL, NULL, '2016-02-01 15:15:18', NULL, '92400.00', '92400.00', '132000.00', NULL, '1422', '58',
   NULL, '2016-01-03 00:00:00', '2016-01-28 00:00:00', '2017-01-02 00:00:00', '2');
INSERT INTO `asset_package` (`id`, `adva_start_date`, `is_available`, `bank_account`, `bank_name`, `create_time`, `latest_settle_date`, `otsd_amt`, `otsd_amt_aval`, `rec_transfee_total_amt`, `remove_time`, `contract_id`, `factoring_contract_id`, `thru_date`, `effective_asset_start_date`, `receivables_transferee_date`, `effective_asset_deadline_date`, `loan_batch_id`)
VALUES
  ('974', NULL, '\0', NULL, NULL, '2016-02-01 15:15:18', NULL, '72100.00', '72100.00', '103000.00', NULL, '1423', '58',
   NULL, '2016-01-03 00:00:00', '2016-01-28 00:00:00', '2017-01-02 00:00:00', '2');
INSERT INTO `asset_package` (`id`, `adva_start_date`, `is_available`, `bank_account`, `bank_name`, `create_time`, `latest_settle_date`, `otsd_amt`, `otsd_amt_aval`, `rec_transfee_total_amt`, `remove_time`, `contract_id`, `factoring_contract_id`, `thru_date`, `effective_asset_start_date`, `receivables_transferee_date`, `effective_asset_deadline_date`, `loan_batch_id`)
VALUES
  ('975', NULL, '\0', NULL, NULL, '2016-02-01 15:15:18', NULL, '84000.00', '84000.00', '120000.00', NULL, '1424', '58',
   NULL, '2016-01-03 00:00:00', '2016-01-28 00:00:00', '2017-01-02 00:00:00', '2');
INSERT INTO `asset_package` (`id`, `adva_start_date`, `is_available`, `bank_account`, `bank_name`, `create_time`, `latest_settle_date`, `otsd_amt`, `otsd_amt_aval`, `rec_transfee_total_amt`, `remove_time`, `contract_id`, `factoring_contract_id`, `thru_date`, `effective_asset_start_date`, `receivables_transferee_date`, `effective_asset_deadline_date`, `loan_batch_id`)
VALUES
  ('976', NULL, '\0', NULL, NULL, '2016-02-01 15:15:18', NULL, '77000.00', '77000.00', '110000.00', NULL, '1425', '58',
   NULL, '2016-01-03 00:00:00', '2016-01-28 00:00:00', '2017-01-02 00:00:00', '2');
INSERT INTO `asset_package` (`id`, `adva_start_date`, `is_available`, `bank_account`, `bank_name`, `create_time`, `latest_settle_date`, `otsd_amt`, `otsd_amt_aval`, `rec_transfee_total_amt`, `remove_time`, `contract_id`, `factoring_contract_id`, `thru_date`, `effective_asset_start_date`, `receivables_transferee_date`, `effective_asset_deadline_date`, `loan_batch_id`)
VALUES
  ('977', NULL, '\0', NULL, NULL, '2016-02-01 15:15:19', NULL, '57750.00', '57750.00', '82500.00', NULL, '1426', '58',
   NULL, '2016-01-03 00:00:00', '2016-01-28 00:00:00', '2017-01-02 00:00:00', '2');
INSERT INTO `asset_package` (`id`, `adva_start_date`, `is_available`, `bank_account`, `bank_name`, `create_time`, `latest_settle_date`, `otsd_amt`, `otsd_amt_aval`, `rec_transfee_total_amt`, `remove_time`, `contract_id`, `factoring_contract_id`, `thru_date`, `effective_asset_start_date`, `receivables_transferee_date`, `effective_asset_deadline_date`, `loan_batch_id`)
VALUES
  ('978', NULL, '\0', NULL, NULL, '2016-02-01 15:15:19', NULL, '75460.00', '75460.00', '107800.00', NULL, '1427', '58',
   NULL, '2016-01-03 00:00:00', '2016-01-28 00:00:00', '2017-01-02 00:00:00', '2');

SET FOREIGN_KEY_CHECKS = 1;