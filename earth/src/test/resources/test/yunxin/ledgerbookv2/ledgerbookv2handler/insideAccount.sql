CREATE TABLE if not EXISTS t_inside_account (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `account_name` VARCHAR(255) NOT NULL,
  `account_code` VARCHAR(255) NOT NULL,
  account_alias VARCHAR(255) NOT NULL,
  parent_account_id int(11) null DEFAULT NULL,
  level int(3) NOT NULL,
  account_side int(3) NOT NULL,
  PRIMARY KEY (`id`)
)  ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

TRUNCATE t_inside_account;

SET FOREIGN_KEY_CHECKS=0;
INSERT INTO t_inside_account (id, account_alias, account_name, account_code, parent_account_id, LEVEL, account_side)
VALUES (1, "", "FST_UNEARNED_LOAN_ASSET", "10000", NULL, 0, 1),
  (2, "", "FST_DEFERRED_INCOME", "100000", NULL, 0, 0),
  (NULL, "", "SND_UNEARNED_LOAN_ASSET_INTEREST", "10000.01", 1, 1, 1),
  (NULL, "", "SND_UNEARNED_LOAN_ASSET_PRINCIPLE", "10000.02", 1, 1, 1),
  (NULL, "", "SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE", "10000.03", 1, 1, 1),
  (NULL, "", "SND_UNEARNED_LOAN_ASSET_TECH_FEE", "10000.04", 1, 1, 1),
  (NULL, "", "SND_UNEARNED_LOAN_ASSET_OTHER_FEE", "10000.05", 1, 1, 1),
  (NULL, "", "SND_DEFERRED_INCOME_INTEREST_ESTIMATE", "100000.01", 2, 1, 0),
  (NULL, "", "SND_DEFERRED_INCOME_FEE", "100000.05", 2, 1, 0);
SET FOREIGN_KEY_CHECKS=1;