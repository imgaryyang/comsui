SET FOREIGN_KEY_CHECKS=0;

TRUNCATE TABLE `t_delay_processing_task_config`;
TRUNCATE TABLE `t_delay_processing_task`;
TRUNCATE TABLE `t_source_repository`;
TRUNCATE TABLE `repayment_plan_extra_data`;
DELETE FROM galaxy_autotest_yunxin.contract;
DELETE FROM galaxy_autotest_yunxin.financial_contract;
DELETE FROM galaxy_autotest_yunxin.asset_set;
DELETE FROM galaxy_autotest_yunxin.customer;
DELETE FROM galaxy_autotest_yunxin.contract_account;

INSERT INTO `t_delay_processing_task_config` (`id`, `uuid`, `product_lv_1_code`, `product_lv_1_name`, `product_lv_2_code`, `product_lv_2_name`, `product_lv_3_code`, `product_lv_3_name`, `type_code`, `trigger_type_code`, `execute_code_version`, `status`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`)
VALUES
	(1, 'delayTaskConfigUuid', 'zhongan', '众安保险', '10000', '变更后置任务', '10000', '变更统计', 1, 0, 'md5', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO `t_source_repository` (`id`, `business_type`, `source_code`, `signature`, `compile_import`, `add_time`, `last_modify_time`, `author`)
VALUES
	(1, 'delayTaskConfigUuid', 'public boolean evaluate(Result processedResult, DelayProcessingTaskCacheHandler delayProcessingTaskHandler,
                             SandboxDataSetHandler sandboxDataSetHandler, Map<String, Object>
                                     inputMap, Map<String, Object> resultMap,Log log) {
        try {
        log.info("start delay task");
            if (processedResult != null && !StringUtils.equalsIngoreNull("0", processedResult.getCode())) {
                return false;
            }
            List<String> repaymentPlanNoList = (List<String>)inputMap.get(SandboxDataSetSpec.REPAYMENT_PLAN_NO_LIST);
            String financialContractUuid = (String) inputMap.getOrDefault(SandboxDataSetSpec.FINANCIAL_CONTRACT_UUID, "");
            String contractUuid = (String) inputMap.getOrDefault(SandboxDataSetSpec.CONTRACT_UUID, "");
            String configUuid = (String) inputMap.getOrDefault(SandboxDataSetSpec.CONFING_UUID, "");

            SandboxDataSet sandboxDataSet = sandboxDataSetHandler.get_sandbox_by_repaymentPlanNoList(financialContractUuid, contractUuid,repaymentPlanNoList);
            log.info("sandboxDataSet:" + com.zufangbao.sun.utils.JsonUtils.toJSONString(sandboxDataSet));
            String changeType = (String) sandboxDataSet.getExtraData().getOrDefault("reasonCode", null);
            if (changeType == null || configUuid == null) {
                return false;
            }
            if (StringUtils.equalsIngoreNull(changeType, RepaymentPlanModifyReason.REASON_4.getOrdinal() + "")) {
                changeType = "1";
            } else if (StringUtils.equalsIngoreNull(changeType, RepaymentPlanModifyReason.REASON_5.getOrdinal() + "")) {
                changeType = "2";
            } else {
                return false;
            }
byte[] encode_byte = Base64.getEncoder().encode(sandboxDataSet.getIdCardNo().getBytes());
String idCardNo = new String(encode_byte);
            String uniqueId = sandboxDataSet.getContractUniqueId();
            Date taskExecuteDate = DateUtils.addDays(DateUtils.getToday(), 1);

            List<PaymentPlanSnapshot> paymentPlanSnapshotList = sandboxDataSet.getPaymentPlanSnapshotList();
            for (PaymentPlanSnapshot paymentPlanSnapshot: paymentPlanSnapshotList) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("tradeNo", UUID.randomUUID().toString().replaceAll("-", ""));
                map.put("loanNo", uniqueId);
                map.put("thirdUserNo", idCardNo);
                map.put("installmentNo", paymentPlanSnapshot.getCurrentPeriod());
                map.put("agreedRepayDate", paymentPlanSnapshot.getAssetRecycleDate());
                map.put("changeType", changeType);
                map.put("tradeTime", paymentPlanSnapshot.getCreateTime());

                JSONObject jsonObject = new JSONObject(map);
                String workParam = com.zufangbao.sun.utils.JsonUtils.toJSONString(jsonObject);

                DelayProcessingTask task = new DelayProcessingTask(paymentPlanSnapshot, taskExecuteDate, workParam,
                        configUuid);
                delayProcessingTaskHandler.save_to_db_cache(task);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }', 'c9699350-3935-11e7-952e-ba77244e1da4', 'java.util.*,com.suidifu.matryoshka.delayTask.*,com.demo2do.core.entity.*,com.zufangbao.sun.yunxin.entity.model.api.modify.*,com.suidifu.matryoshka.snapshot.*,com.zufangbao.sun.utils.*,com.alibaba.fastjson.JSONObject,org.apache.commons.logging.Log', '2017-05-15 18:20:38', '2017-05-15 18:20:38', 'sys');

INSERT INTO `repayment_plan_extra_data` (`id`, `uuid`, `asset_set_uuid`, `financial_contract_uuid`, `customer_uuid`, `contract_uuid`, `reason_code`, `comment`, `create_time`, `last_modify_time`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`)
VALUES
	(1, uuid(), '922b2d45-da2c-4b6b-87a2-30a5303a5da0', 'b674a323-0c30-4e4b-9eba-b14e05a9d80a', 'c714fe88-8ed3-45e9-9807-59ca5bd37ae3', 'be834b15-56a7-4175-b926-64c90869a2f0', 3, NULL, '2017-05-11 23:31:25', '2017-05-11 23:31:25', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO `repayment_plan_extra_data` (`id`, `uuid`, `asset_set_uuid`, `financial_contract_uuid`, `customer_uuid`, `contract_uuid`, `reason_code`, `comment`, `create_time`, `last_modify_time`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`)
VALUES
	(2, uuid(), '1dcb05fd-8602-4030-b45b-180b4d0b8281', 'b674a323-0c30-4e4b-9eba-b14e05a9d80a', 'c714fe88-8ed3-45e9-9807-59ca5bd37ae3', 'be834b15-56a7-4175-b926-64c90869a2f0', 3, NULL, '2017-05-11 23:31:25', '2017-05-11 23:31:25', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO `contract` (`id`, `uuid`, `unique_id`, `begin_date`, `contract_no`
	, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`
	, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`
	, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`
	, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, `interest_rate_cycle`
	, `customer_uuid`)
VALUES (124365, 'be834b15-56a7-4175-b926-64c90869a2f0', 'ad6b3053-8625-4eb6-a78a-dcabc6132b5d', '2017-04-21', '云信信2016-241-DK(321515600990709045)'
	, '2019-02-02', NULL, 0.00, 2, 124701
	, 124854, NULL, '2017-04-21 17:50:40', 0.8923000000, 0
	, 0, 3, 2, 20000.00, 0.0050000000
	, 1, NULL, 2, 'b674a323-0c30-4e4b-9eba-b14e05a9d80a', 2
	, 'c714fe88-8ed3-45e9-9807-59ca5bd37ae3');

INSERT INTO `financial_contract` (`id`, `asset_package_format`, `adva_matuterm`, `adva_start_date`, `contract_no`
	, `contract_name`, `app_id`, `company_id`, `adva_repo_term`, `thru_date`
	, `capital_account_id`, `financial_contract_type`, `loan_overdue_start_day`, `loan_overdue_end_day`, `payment_channel_id`
	, `ledger_book_no`, `financial_contract_uuid`, `sys_normal_deduct_flag`, `sys_overdue_deduct_flag`, `sys_create_penalty_flag`
	, `sys_create_guarantee_flag`, `unusual_modify_flag`, `sys_create_statement_flag`, `transaction_limit_per_transcation`, `transaction_limit_per_day`
	, `remittance_strategy_mode`, `app_account_uuids`, `allow_online_repayment`, `allow_offline_repayment`, `allow_advance_deduct_flag`
	, `adva_repayment_term`, `penalty`, `overdue_default_fee`, `overdue_service_fee`, `overdue_other_fee`
	, `create_time`, `last_modified_time`, `repurchase_approach`, `repurchase_rule`, `repurchase_algorithm`
	, `day_of_month`, `pay_for_go`, `repurchase_principal_algorithm`, `repurchase_interest_algorithm`, `repurchase_penalty_algorithm`
	, `repurchase_other_charges_algorithm`, `repayment_check_days`, `repurchase_cycle`, `days_of_cycle`, `temporary_repurchases`
	, `allow_freewheeling_repayment`)
VALUES (37, 0, 3, '2016-09-01 00:00:00', 'G32000'
	, '用钱宝测试', 2, 1, 60, '2017-12-01 00:00:00'
	, 101, 0, 1, 2, 1
	, 'e17d8068-46a0-48bb-b158-8e80168bbfc3', 'b674a323-0c30-4e4b-9eba-b14e05a9d80a', 0, 0, 0
	, 0, 1, 0, NULL, NULL
	, NULL, NULL, 0, 0, 0
	, 0, NULL, NULL, NULL, NULL
	, NULL, NULL, NULL, NULL, NULL
	, NULL, 0, NULL, NULL, NULL
	, NULL, -1, NULL, NULL, NULL
	, 0);

INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`
	, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`
	, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`
	, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`
	, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`
	, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`
	, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`
	, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`
	, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`)
VALUES (373500, 0, 0, 100.00, 90.00
	, 1.00, 91.00, '2017-04-22', '2017-04-09', 0.00
	, 1, 2, 0, NULL, '259ef4d2-95d0-478a-b2e3-e9fe3ec1dc0d'
	, '2017-04-21 17:50:40', '2017-04-21 17:53:02', NULL, 'ZC1691381619185422336', 124365
	, '2017-04-09 10:00:00', 1, 0, NULL, 1
	, 0, 0, 'empty_deduct_uuid', NULL, 'b674a323-0c30-4e4b-9eba-b14e05a9d80a'
	, '12f2d1ab43432e896004fab5eeac44b2', '513521f46449e01fe6b0e9dc3ee9035b', '2017-04-21 17:50:40', '2017-04-21 17:50:40', 0
	, 0, 0, 0, 0, 2
	, 0, 'c714fe88-8ed3-45e9-9807-59ca5bd37ae3', 'be834b15-56a7-4175-b926-64c90869a2f0', 0),
	(373501, 0, 0, 10010.00, 10000.00
		, 1.00, 10001.00, '2027-05-16', NULL, 0.00
		, 0, 1, 0, NULL, '922b2d45-da2c-4b6b-87a2-30a5303a5da0'
		, '2017-04-21 17:50:40', '2017-04-21 17:50:40', NULL, 'ZC1691381619856510976', 124365
		, NULL, 2, 0, NULL, 1
		, 0, 0, 'empty_deduct_uuid', NULL, 'b674a323-0c30-4e4b-9eba-b14e05a9d80a'
		, 'baed3060329832c3b3c8df6337e1bf35', '513521f46449e01fe6b0e9dc3ee9035b', '2017-04-21 17:50:40', '2017-04-21 17:50:40', 0
		, 0, 0, 0, 0, 0
		, 0, 'c714fe88-8ed3-45e9-9807-59ca5bd37ae3', 'be834b15-56a7-4175-b926-64c90869a2f0', NULL),
	(373502, 0, 0, 9920.00, 9910.00
		, 1.00, 9911.00, '2027-12-18', NULL, 0.00
		, 0, 1, 0, NULL, '1dcb05fd-8602-4030-b45b-180b4d0b8281'
		, '2017-04-21 17:50:40', '2017-04-21 17:50:40', NULL, 'ZC1691381620527599616', 124365
		, NULL, 3, 0, NULL, 1
		, 0, 0, 'empty_deduct_uuid', NULL, 'b674a323-0c30-4e4b-9eba-b14e05a9d80a'
		, 'eefa7bbe6cca45afba129716475512ec', '513521f46449e01fe6b0e9dc3ee9035b', '2017-04-21 17:50:40', '2017-04-21 17:50:40', 0
		, 0, 0, 0, 0, 0
		, 0, 'c714fe88-8ed3-45e9-9807-59ca5bd37ae3', 'be834b15-56a7-4175-b926-64c90869a2f0', NULL);

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`
	, `app_id`, `customer_uuid`, `customer_type`)
VALUES (124701, '410402198801111211', NULL, '测试', 'ad6b3053-8625-4eb6-a78a-dcabc6132b5d'
	, 2, 'c714fe88-8ed3-45e9-9807-59ca5bd37ae3', 0);

INSERT INTO `contract_account` (`id`, `pay_ac_no`, `bankcard_type`, `contract_id`, `payer_name`, `bank`, `bind_id`, `id_card_num`, `bank_code`, `province`, `province_code`, `city`, `city_code`, `standard_bank_code`, `from_date`, `thru_date`, `virtual_account_uuid`, `bank_card_status`, `contract_account_uuid`, `contract_account_type`)
VALUES
	(132478, '621700121007322332325327590', NULL, 124365, '测试', '中国建设银行 ', NULL, '410402198801111211', NULL, '上海市',
					 '310000', '上海市', '310100', 'C10105', '2017-04-21 17:50:40', '2900-01-01 00:00:00', NULL, 1, 'ee49f71e318b4976876a9fe367f464d0', 0);

SET FOREIGN_KEY_CHECKS=1;