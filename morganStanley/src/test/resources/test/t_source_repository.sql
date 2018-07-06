
CREATE TABLE if not EXISTS `t_source_repository` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `business_type` varchar(255) NOT NULL unique,
  `source_code` longtext not null,
  `signature` varchar(255) not null,
  `source_file_path` varchar(255) null DEFAULT NULL,
  `add_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `last_modify_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `author`  varchar(255) null DEFAULT NULL,
  PRIMARY KEY (`id`)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

truncate `t_source_repository`;