

create table if not EXISTS t_business_scenario_definition (
  id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  uuid  VARCHAR(255) DEFAULT NULL,
  event_type int(3) not null,
  template longtext not null,
  add_time TIMESTAMP NULL DEFAULT NULL,
  last_modify_time TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

TRUNCATE `t_business_scenario_definition`;

INSERT INTO `t_business_scenario_definition` (id, uuid, event_type,
template, add_time, last_modify_time)
VALUES
	(1,"74a9ce4d-cafc-407d-b013-987077541bde", 0,
   "", now(), now());


create table if not exists t_account_template (
  id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  uuid  VARCHAR(255) DEFAULT NULL,
  scenario_id  int(11) not null,
  event_type int(3) not null,
  `ledger_book_no` VARCHAR(255) not NULL,
  default_date TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
)
ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

TRUNCATE `t_account_template`;
INSERT INTO `t_account_template` (id, uuid, scenario_id, event_type,
ledger_book_no, default_date)
VALUES
	(1,"74a9ce4d-cafc-407d-b013-987077541bdb", 1,
	0, "74a9ce4d-cafc-407d-b013-987077541bdc", now());
