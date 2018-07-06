CREATE TABLE if not EXISTS t_ledger_book_batch (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `ledger_book_batch_uuid` VARCHAR(255) DEFAULT NULL,
  `booking_account` INT(1) NOT NULL DEFAULT 0,
  `add_time` TIMESTAMP NULL DEFAULT NULL,
  `booking_account_time` TIMESTAMP NULL DEFAULT NULL,
  `ledger_book_no` VARCHAR(255) DEFAULT NULL,
  `ledger_book_d_b_location` VARCHAR(255) DEFAULT NULL,
  `contract_uuid` VARCHAR(255) DEFAULT NULL,
  `financial_contract_uuid` VARCHAR(255) DEFAULT NULL,
  `batch_size` int(11) NOT NULL,
  PRIMARY KEY (`id`)
)  ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

TRUNCATE t_ledger_book_batch;