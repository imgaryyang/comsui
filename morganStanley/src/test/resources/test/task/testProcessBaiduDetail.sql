DELETE FROM t_file_repository;
DELETE FROM t_product_category;
DELETE FROM customer_person;
DELETE FROM customer;
DELETE FROM contract;

INSERT INTO t_file_repository (id, uuid, product_code, file_type_code, status, path, upload_time, add_time, last_modify_time, csv_size, trade_size, process_status, send_status, execute_status, process_size, mer_id_batch_no, batch_index, batch_count)
VALUES
(1, '3aa166df-df33-4b98-acea-36054cb00e0d', 'I02900', 10006, 0, '/home/zsh2014/programming/test/BAIDU_SB.xlsx', '2017-05-25 03:05:52', '2017-05-25 03:05:52', '2017-05-25 03:06:20', 1, 1, 1, 1, 1, 0, NULL, 1, 1);
INSERT INTO `t_product_category` (`id`, `uuid`, `product_lv_1_code`, `product_lv_1_name`, `product_lv_2_code`, `product_lv_2_name`, `product_lv_3_code`, `product_lv_3_name`, `pre_process_interface_url`, `post_process_interface_url`, `pre_process_script`, `status`, `script_md_5_version`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`, `delay_task_config_uuid`)
VALUES
('117', '066aca36-c2bb-11e7-a1b1-502b73c136df', 'I02900', '前隆金融', 'upload', '文件上传', '10006', '百度资产包附加信息文件', 'importAssetPackage/weifang/10001', NULL, NULL, '1', '857986d8-c2bd-11e7-a1b1-502b73c136df', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `customer_person` (`id`, `uuid`, `customer_uuid`, `name`, `sex`, `birthday`, `id_type`, `id_number`, `mobile`, `marital_status`, `highest_education`, `highest_degree`, `residential_status`, `residential_address`, `postal_address`, `residential_code`, `postal_code`, `occupation`, `duty`, `title`, `industry`, `company_name`, `create_time`, `last_modify_time`)
VALUES
('3', 'e5e0a090-dcba-4f2f-a0ae-7853312fddb4', 'e67ba4bc-48ee-4f23-b8cd-b4fea2448a1c', '程浩', NULL, NULL, '0', '420114199402125432', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2017-10-16 14:22:34', '2017-10-16 14:22:34');
INSERT INTO `customer`
(`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`, `customer_style`, `status`, `id_type`)
VALUES
('169340', '420114199402125432', NULL, '程浩', 'ac2fd9e7660247dca4dfdf00e3494e9a', '1', 'e67ba4bc-48ee-4f23-b8cd-b4fea2448a1c', '0', '0', '1', '0');

INSERT INTO `contract`
(`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, `interest_rate_cycle`, `customer_uuid`, `repaymented_periods`, `completion_status`, `date_field_one`, `long_field_one`, `long_field_two`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`)
VALUES
  ('12386593', '28a8da6a-9f66-4e03-bd4e-4e13e2d9a3f8', '51a2af04b08649b9a99a6fb851046ba0', '2017-10-16', '合同模板(yntrust17101316535358652382331)号', '2019-04-16', NULL, '0.00', '1', '169340', '169477', NULL, '2017-10-16 11:23:01', '0.2200000000', '0', '0', '18', '2', '100000.00', '0.0008000000', '1', NULL, '2', '21863662-0627-4e8e-963e-438391acfc44', '0', 'e67ba4bc-48ee-4f23-b8cd-b4fea2448a1c', '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);