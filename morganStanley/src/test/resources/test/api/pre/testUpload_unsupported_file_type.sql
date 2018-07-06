USE galaxy_autotest_yunxin;
SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM t_product_category;
DELETE FROM financial_contract;
DELETE FROM asset_set;

INSERT INTO t_product_category (uuid, product_lv_1_code, product_lv_1_name, product_lv_2_code, product_lv_2_name, product_lv_3_code, product_lv_3_name, pre_process_interface_url, post_process_interface_url, pre_process_script, status, script_md_5_version, date_field_one, date_field_two, date_field_three, long_field_one, long_field_two, long_field_three, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two, decimal_field_three, delay_task_config_uuid)
VALUES ('834a968c-3e29-11e7-952e-ba77244e1da4', 'HA0100', '众安保险', 'upload', '文件上传', '10003', '变更还款计划文件上传',
                                                'upload/HA0100/10001', NULL, NULL, 1,
  'e5b761d8-2af6-11e7-952e-ba77244e1da5', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;

