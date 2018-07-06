SET FOREIGN_KEY_CHECKS=0;

DELETE FROM t_merchant_config;
DELETE FROM dictionary;
DELETE FROM t_product_category;
INSERT INTO `t_merchant_config` (`id`, `mer_id`, `secret`, `company_id`, `pub_key_path`, `pub_key`)
VALUES
  (2, 't_test_zfb', '123456', 3, NULL, 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDl1BjinRJLLDiNc6jcOKW+nph9aNSWMaKMk0OxTdSATakyS7rwNxrLMFyJLkI9IpnHussBv1zgsHPUdZeRcHDkbcMdhYoRgpe3gZIVMJ09BMBjhAET4fensvk377L0Whzp+u9r9UxIWowH7YJuJe3yQ7R3RgYxzrPuTJYq/WeuUQIDAQAB');

INSERT INTO `dictionary` (`id`, `code`, `content`)
VALUES
  (11, 'PLATFORM_PRI_KEY', 'MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=');


INSERT INTO t_product_category (uuid, product_lv_1_code, product_lv_1_name, product_lv_2_code, product_lv_2_name, product_lv_3_code, product_lv_3_name, pre_process_interface_url, post_process_interface_url, pre_process_script, status, script_md_5_version, date_field_one, date_field_two, date_field_three, long_field_one, long_field_two, long_field_three, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two, decimal_field_three, delay_task_config_uuid) VALUES ('735546d7-816a-11e7-8014-525400dbb013', 'spdbank', '上海浦东发展银行', 'upload', '文件上传', '10003', '逾期费用变更文件', null, null, null, 1, '735546f9-816a-11e7-8014-525400dbb013', null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO t_product_category (uuid, product_lv_1_code, product_lv_1_name, product_lv_2_code, product_lv_2_name, product_lv_3_code, product_lv_3_name, pre_process_interface_url, post_process_interface_url, pre_process_script, status, script_md_5_version, date_field_one, date_field_two, date_field_three, long_field_one, long_field_two, long_field_three, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two, decimal_field_three, delay_task_config_uuid) VALUES ('735546d7-816a-11e7-8014-525400dbb013', 'spdbank', '上海浦东发展银行', 'upload', '文件上传', '10002', '浮点费用变更文件', null, null, null, 1, '70bd6d37-8c7d-11e7-b26b-525400dbb013', null, null, null, null, null, null, null, null, null, null, null, null, null);

SET FOREIGN_KEY_CHECKS=1;