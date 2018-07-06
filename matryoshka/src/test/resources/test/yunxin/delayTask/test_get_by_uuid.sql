SET @@FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE t_delay_processing_task_config;

INSERT INTO `t_delay_processing_task_config` (`id`, `uuid`, `product_lv_1_code`, `product_lv_1_name`, `product_lv_2_code`
  , `product_lv_2_name`, `product_lv_3_code`, `product_lv_3_name`, `type_code`, `trigger_type_code`
  , `execute_code_version`, `status`)
VALUES (1, 'uuid', 'zhongan', '众安保险', '10000'
  , '变更后置任务', '10000', '变更统计', 1, 0
  , 'md5', 1);

INSERT INTO `t_delay_processing_task_config` (`id`, `uuid`, `product_lv_1_code`, `product_lv_1_name`, `product_lv_2_code`
  , `product_lv_2_name`, `product_lv_3_code`, `product_lv_3_name`, `type_code`, `trigger_type_code`
  , `execute_code_version`, `status`)
VALUES (2, 'invalid_uuid', 'zhongan', '众安保险', '10000'
  , '变更后置任务', '10000', '变更统计', 1, 0
  , 'invalid_md5', 0);
SET @@FOREIGN_KEY_CHECKS = 1;