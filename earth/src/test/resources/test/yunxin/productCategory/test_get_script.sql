SET FOREIGN_KEY_CHECKS=0;

DELETE FROM `t_product_category`;

INSERT INTO `t_product_category` 
(`id`, `uuid`, `product_lv_1_code`, `product_lv_1_name`, `product_lv_2_code`, `product_lv_2_name`, `product_lv_3_code`, `product_lv_3_name`, `pre_process_interface_url`, `pre_process_script`, `status`, `script_md_5_version`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`)
 VALUES
(311420, '36b7d036-4ec4-48c5-9c03-0cbfe35f99b5', 'zhongan', 'asside', 'modify-repaymentPlan', 'operation',
         'tiqianjieqing', 'function', 'modify-repaymentPlan/zhongan/tiqianjieqing', 'boolean evaluate
         (FinancialContractSnapshot financialContractSnapshot, ContractSnapshot contractSnapshot,
         List<PaymentPlanSnapshot> assetSetSnapshotList, Map<String, Object> parameters, Map<String, Object> postParams)
         {return false;}', 1,'0b00f3d395dc80f6382dd021b9ae5f22', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS=1;