SET FOREIGN_KEY_CHECKS=0;

delete from t_product_category;
DELETE from t_file_repository;
INSERT INTO  `t_product_category` (`id`, `uuid`, `product_lv_1_code`, `product_lv_1_name`, `product_lv_2_code`, `product_lv_2_name`, `product_lv_3_code`, `product_lv_3_name`, `pre_process_interface_url`, `post_process_interface_url`, `pre_process_script`, `status`, `script_md_5_version`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`, `delay_task_config_uuid`) VALUES
('11', 'e8266b7a-7d7d-11e7-9d74-502b73c136df', 'spdbank', '上海浦东发展银行', 'testUpload', '文件上传', '10003', '逾期费用文件上传', 'testUpload/spdbank/10003', NULL, NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL)
, ('13', 'f343a994-7d7d-11e7-9d74-502b73c136df', 'spdbank', '上海浦东发展银行', 'overdueFee', '逾期费用变更', 'standard', '标准接口', 'overdueFee/spdbank/standard', '/api/modify', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO `t_file_repository` (`id`, `uuid`, `product_code`, `file_type_code`, `status`, `path`, `upload_time`, `add_time`, `last_modify_time`, `csv_size`, `trade_size`) VALUES ('57', '136432f3-a72b-43s0-95g7-66680259f537', 'spdbank', '10001', '1', '/home/hwr/down/spdbank/2017-08-10.txt', '2017-08-10 10:05:52', '2017-08-10 10:05:32', '2017-08-10 10:05:36', '1', '1')
, ('58', 'f8dd5c0e-7d96-11e7-9d74-502b73c136df', 'spdbank', '10003', '1', '/home/hwr/down/spdbank/2017-08-10_overfee.txt', '2017-08-10 14:42:20', '2017-08-10 14:42:22', '2017-08-10 14:42:24', '1', '1');


SET FOREIGN_KEY_CHECKS=1;
