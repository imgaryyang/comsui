SET FOREIGN_KEY_CHECKS=0;

TRUNCATE TABLE `t_delay_processing_task_config`;
TRUNCATE TABLE `t_source_repository`;

INSERT INTO `t_delay_processing_task_config` (`id`, `uuid`, `product_lv_1_code`, `product_lv_1_name`, `product_lv_2_code`, `product_lv_2_name`, `product_lv_3_code`, `product_lv_3_name`, `type_code`, `trigger_type_code`, `execute_code_version`, `status`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`)
VALUES
	(1, 'delayTaskConfigUuid3', 'zhongan', '众安保险', '10000', '变更后置任务', '10000', '变更统计', 1, 0, 'md5', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);


INSERT INTO `t_source_repository` (`id`, `business_type`, `source_code`, `signature`, `source_file_path`, `add_time`, `last_modify_time`, `author`)
VALUES
  (1, 'delayTaskConfigUuid3', 'public boolean evaluate(Result processedResult, DelayProcessingTaskCacheHandler delayProcessingTaskHandler, SandboxDataSetHandler sandboxDataSetHandler, Map<String, Object> inputMap, Map<String, Object> resultMap, Log log) {\n        return true;\n}', 'md5', NULL, now(), now(), 'sys');

SET FOREIGN_KEY_CHECKS=1;