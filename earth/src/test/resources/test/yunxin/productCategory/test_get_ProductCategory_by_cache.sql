SET FOREIGN_KEY_CHECKS=0;

DELETE FROM `t_product_category`;

INSERT INTO `t_product_category` 
(`id`, `uuid`, `product_lv_1_code`, `product_lv_1_name`, `product_lv_2_code`, `product_lv_2_name`, `product_lv_3_code`, `product_lv_3_name`, `pre_process_interface_url`, `pre_process_script`, `status`, `script_md_5_version`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`)
 VALUES
(311420, '36b7d036-4ec4-48c5-9c03-0cbfe35f99b5', 'zhongan', 'asside', 'modify-repaymentPlan', 'operation', 'yanqihuankuan', 'function', '', 'public static void main(String[] args) {\n\tSystem.out.println(0);\n}', 1,'bda500743d6a6be9fec1969984ed8d12', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(311421, 'fe410a76-b8cd-4858-b019-3d9b1c1f3238', 'zhongan', 'asside', 'modify-repaymentPlan', 'operation', null, 'function', '', 'public static void main(String[] args) {\n\tSystem.out.println(1);\n}', 1,'17892d90955c133e9e232989da09d57f', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(311422, '3f056394-b7bc-41ea-bc20-f16e99ebdc7d', null, 'asside', 'modify-repaymentPlan', 'operation', null, 'function', '', 'public static void main(String[] args) {\n\tSystem.out.println(2);\n}', 1,'5366659504a0cda33605276670fc3e241', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS=1;