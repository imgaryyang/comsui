SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `payment_order_test`;

CREATE TABLE `payment_order_test` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `worker_uuid` varchar(255) DEFAULT NULL,
  `order_uuid`  varchar(255) DEFAULT NULL,
  `slot`  int(10) DEFAULT NULL,
  `occupy_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


SET FOREIGN_KEY_CHECKS=1;
