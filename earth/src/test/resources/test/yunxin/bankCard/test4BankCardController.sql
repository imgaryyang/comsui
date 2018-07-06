SET FOREIGN_KEY_CHECKS=0;

delete from `bank_card`;

INSERT INTO `bank_card` (`id`, `uuid`, `outer_identifier`, `identity`, `account_name`, `account_no`, `bank_code`, `bank_name`, `province`, `province_code`, `city`, `city_code`, `create_time`, `last_modify_time`)
VALUES
('1', '02dc514a-3260-417e-a3e8-8ceecf31b310', 'b074faf2-a3e8-41c8-adc4-17f66b4bea36', '1', '陈彩非', '6666667777', 'C10102', '中国工商银行', '山东省', '370000', '青岛市', '370200', '2017-09-20 14:27:36', '2017-09-20 14:28:38'),
('2', '02dc514a-3260-417e-a3e8-8ceecf31b311', 'b074faf2-a3e8-41c8-adc4-17f66b4bea36', '1', '张三', '123456789', 'C10102', '中国工商银行', '山东省', '370000', '青岛市', '370200', '2017-09-20 14:27:36', '2017-09-20 14:28:38');

SET FOREIGN_KEY_CHECKS=1;