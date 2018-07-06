USE galaxy_autotest_yunxin;
SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM t_product_category;
DELETE FROM financial_contract;
DELETE FROM asset_set;

INSERT INTO t_product_category (uuid, product_lv_1_code, product_lv_1_name, product_lv_2_code, product_lv_2_name, product_lv_3_code, product_lv_3_name, pre_process_interface_url, post_process_interface_url, pre_process_script, status, script_md_5_version, date_field_one, date_field_two, date_field_three, long_field_one, long_field_two, long_field_three, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two, decimal_field_three, delay_task_config_uuid)
VALUES ('834a968c-3e29-11e7-952e-ba77244e1da4', 'HA0100', '众安保险', 'upload', '文件上传', '10001', '变更还款计划文件上传',
                                                'upload/HA0100/10001', NULL, NULL, 1,
  'e5b761d8-2af6-11e7-952e-ba77244e1da5', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO t_product_category (uuid, product_lv_1_code, product_lv_1_name, product_lv_2_code, product_lv_2_name, product_lv_3_code, product_lv_3_name, pre_process_interface_url, post_process_interface_url, pre_process_script, status, script_md_5_version, date_field_one, date_field_two, date_field_three, long_field_one, long_field_two, long_field_three, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two, decimal_field_three, delay_task_config_uuid)
VALUES ('c67283d6-4066-11e7-b482-a6072dcb76eb', 'HA0100', '众安保险', 'upload', '文件上传', '10002', '浮动费用文件上传',
                                                'upload/HA0100/10002', NULL, NULL, 1,
  'e5b761d8-2af6-11e7-952e-ba77244e1da4', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO t_product_category (uuid, product_lv_1_code, product_lv_1_name, product_lv_2_code, product_lv_2_name, product_lv_3_code, product_lv_3_name, pre_process_interface_url, post_process_interface_url, pre_process_script, status, script_md_5_version, date_field_one, date_field_two, date_field_three, long_field_one, long_field_two, long_field_three, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two, decimal_field_three, delay_task_config_uuid)
VALUES ('475bd4d8-72ae-11e7-96e7-dec9554e87bc', 'zhonghang', '中航信托', 'upload', '文件上传', '10001', '变更还款计划文件上传',
                                                'upload/zhonghang/10001', NULL, NULL, 1,
  '475bd550-72ae-11e7-96e7-dec9554e87bc', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO t_product_category (uuid, product_lv_1_code, product_lv_1_name, product_lv_2_code, product_lv_2_name, product_lv_3_code, product_lv_3_name, pre_process_interface_url, post_process_interface_url, pre_process_script, status, script_md_5_version, date_field_one, date_field_two, date_field_three, long_field_one, long_field_two, long_field_three, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two, decimal_field_three, delay_task_config_uuid)
VALUES
  ('7353999f-816a-11e7-8014-525400dbb013', 'spdbank', '上海浦东发展银行', 'upload', '文件上传', '10001', '变更还款计划文件', NULL, NULL,
                                           NULL, 1, '735399c8-816a-11e7-8014-525400dbb013', NULL, NULL, NULL, NULL,
                                                    NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO t_product_category (uuid, product_lv_1_code, product_lv_1_name, product_lv_2_code, product_lv_2_name, product_lv_3_code, product_lv_3_name, pre_process_interface_url, post_process_interface_url, pre_process_script, status, script_md_5_version, date_field_one, date_field_two, date_field_three, long_field_one, long_field_two, long_field_three, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two, decimal_field_three, delay_task_config_uuid)
VALUES
  ('735546d7-816a-11e7-8014-525400dbb013', 'spdbank', '上海浦东发展银行', 'upload', '文件上传', '10003', '逾期费用变更文件', NULL, NULL,
                                           NULL, 1, '735546f9-816a-11e7-8014-525400dbb013', NULL, NULL, NULL, NULL,
                                                    NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO t_product_category (uuid, product_lv_1_code, product_lv_1_name, product_lv_2_code, product_lv_2_name, product_lv_3_code, product_lv_3_name, pre_process_interface_url, post_process_interface_url, pre_process_script, status, script_md_5_version, date_field_one, date_field_two, date_field_three, long_field_one, long_field_two, long_field_three, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two, decimal_field_three, delay_task_config_uuid)
VALUES ('e5b4c784-2af6-11e7-952e-ba77244e1da5', 'spdbank', '上海浦东发展银行', 'upload', '文件上传', '10004', '还款订单文件',
                                                '/api/v2/repaymentOrder', '', NULL, 1,
  '735546f9-816a-11e7-8014-525400dbb099', NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, '');

SET FOREIGN_KEY_CHECKS = 1;

