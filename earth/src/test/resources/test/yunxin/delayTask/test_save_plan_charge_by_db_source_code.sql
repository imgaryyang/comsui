SET FOREIGN_KEY_CHECKS=0;

DELETE FROM `financial_contract`;
DELETE FROM `contract`;
DELETE FROM `company`;
DELETE FROM `account`;
DELETE FROM `customer`;
DELETE FROM `house`;
DELETE FROM `app`;
DELETE FROM `t_delay_processing_task`;
DELETE FROM `asset_set`;
DELETE FROM `asset_set_extra_charge`;
DELETE FROM `repayment_plan_extra_data`;
DELETE FROM `t_delay_processing_task`;
DELETE FROM `t_delay_processing_task_config`;
DELETE FROM `t_source_repository`;

INSERT INTO `t_delay_processing_task_config` (`id`, `uuid`, `product_lv_1_code`, `product_lv_1_name`, `product_lv_2_code`, `product_lv_2_name`, `product_lv_3_code`, `product_lv_3_name`, `type_code`, `trigger_type_code`, `execute_code_version`, `status`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`) VALUES ('2', 'delayTaskConfigUuid_1', 'zhongan', '众安保险', '10000', '变更后置任务', '10000', '变更统计', '1', '0', 'md5_1', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO `t_source_repository` (`id`, `business_type`, `source_code`, `signature`, `compile_import`, `add_time`, `last_modify_time`, `author`) VALUES ('2', 'delayTaskConfigUuid_1', 'public boolean evaluate(Result processedResult, DelayProcessingTaskCacheHandler delayProcessingTaskHandler,
SandboxDataSetHandler sandboxDataSetHandler, Map<String, Object> inputMap, Map<String, Object> resultMap,Log log) {
try {
log.info("start delay task");
if (processedResult == null || !StringUtils.equalsIngoreNull("0", processedResult.getCode())) {
return false;
}
String financialContractUuid = (String) inputMap.get(SandboxDataSetSpec.FINANCIAL_CONTRACT_UUID);
String contractUuid = (String) inputMap.get(SandboxDataSetSpec.CONTRACT_UUID);
List<String> repaymentPlanNoSrcList = (List<String>)inputMap.get(SandboxDataSetSpec.ORIGINAL_REPAYMENT_PLAN_NO_LIST);
List<String> repaymentPlanNoTarList = (List<String>)inputMap.get(SandboxDataSetSpec.REPAYMENT_PLAN_NO_LIST);
        String configUuid = (String) inputMap.getOrDefault(SandboxDataSetSpec.CONFING_UUID, "");

SandboxDataSet sandboxDataSetSrc = sandboxDataSetHandler
.get_sandbox_by_repaymentPlanNoList(financialContractUuid, contractUuid, repaymentPlanNoSrcList);
SandboxDataSet sandboxDataSetTar = sandboxDataSetHandler
.get_sandbox_by_repaymentPlanNoList(financialContractUuid, contractUuid, repaymentPlanNoTarList);
log.info("sandboxDataSetSrc:" + com.zufangbao.sun.utils.JsonUtils.toJSONString(sandboxDataSetSrc));
log.info("sandboxDataSetTar:" + com.zufangbao.sun.utils.JsonUtils.toJSONString(sandboxDataSetTar));
String reason = (String) sandboxDataSetTar.getExtraData().getOrDefault("reasonCode", null);


int reasonCode = Integer.valueOf(reason);

String changeType = null;
if (RepaymentPlanModifyReason.REASON_7.getOrdinal() == reasonCode) {
changeType = "3";
} else if (RepaymentPlanModifyReason.REASON_8.getOrdinal() == reasonCode) {
changeType = "4";
} else if (RepaymentPlanModifyReason.REASON_6.getOrdinal() == reasonCode) {
changeType = "5";
} else if (RepaymentPlanModifyReason.REASON_9.getOrdinal() == reasonCode) {
changeType = "6";
}
if (changeType == null) {
return false;
}
byte[] encode_byte = Base64.getEncoder().encode(sandboxDataSetTar.getIdCardNo().getBytes());
String idCardNo = new String(encode_byte);
String uniqueId = sandboxDataSetTar.getContractUniqueId();
Date taskExecuteDate = DateUtils.addDays(DateUtils.getToday(), 1);

List<PaymentPlanSnapshot> paymentPlanSnapshotSrcList = sandboxDataSetSrc.getPaymentPlanSnapshotList();
Map<Integer,PaymentPlanSnapshot> srcMap=new HashMap<Integer,PaymentPlanSnapshot>();
for (PaymentPlanSnapshot paymentPlanSnapshot : paymentPlanSnapshotSrcList) {
srcMap.put(paymentPlanSnapshot.getCurrentPeriod(), paymentPlanSnapshot);
}
List<PaymentPlanSnapshot> paymentPlanSnapshotList = sandboxDataSetTar.getPaymentPlanSnapshotList();
for (PaymentPlanSnapshot paymentPlanSnapshot : paymentPlanSnapshotList) {
HashMap<String, Object> map = new HashMap<String, Object>();
map.put("tradeNo", UUID.randomUUID().toString().replaceAll("-", ""));
map.put("loanNo", uniqueId);
map.put("thirdUserNo", idCardNo);

PaymentPlanSnapshot srcSnapshot = (PaymentPlanSnapshot)srcMap.get(paymentPlanSnapshot.getCurrentPeriod());
map.put("installmentNo", paymentPlanSnapshot.getCurrentPeriod());

//所有第0期，应收表中的应收金额不用做减法
int installmentNo = Integer.valueOf(map.get("installmentNo").toString());
if (0 == installmentNo) {
map.put("principal", paymentPlanSnapshot.getAssetPrincipalValue());
map.put("interest", paymentPlanSnapshot.getAssetInterestValue());
map.put("repayCharge", paymentPlanSnapshot.getRepayCharge());
}else{
map.put("principal", paymentPlanSnapshot.getAssetPrincipalValue().subtract(srcSnapshot.getAssetPrincipalValue()));
map.put("interest", paymentPlanSnapshot.getAssetInterestValue().subtract(srcSnapshot.getAssetInterestValue()));
map.put("repayCharge",paymentPlanSnapshot.getRepayCharge().subtract(srcSnapshot.getRepayCharge()));

}

map.put("changeType", changeType);
map.put("tradeTime", paymentPlanSnapshot.getCreateTime());

JSONObject jsonObject = new JSONObject(map);
String workParam = com.zufangbao.sun.utils.JsonUtils.toJSONString(jsonObject);
DelayProcessingTask task = new DelayProcessingTask(paymentPlanSnapshot, taskExecuteDate, workParam,
                        configUuid);
String taskJSONString = JsonUtils.toJSONString(task);

delayProcessingTaskHandler.save_to_db_cache(task);
}
return true;
} catch (Exception e) {
e.printStackTrace();
return false;
}
}', 'md5_1', 'java.util.*,com.suidifu.matryoshka.delayTask.*,com.demo2do.core.entity.*,com.zufangbao.sun.yunxin.entity.model.api.modify.*,com.suidifu.matryoshka.snapshot.*,com.zufangbao.sun.utils.*,com.alibaba.fastjson.JSONObject,com.zufangbao.sun.yunxin.entity.api.mutableFee.MutableFeeReasonCode,org.apache.commons.logging.Log', '2017-05-11 23:57:53', '2017-05-11 23:57:53', 'sys');

INSERT INTO financial_contract (asset_package_format, adva_matuterm, adva_start_date, contract_no, contract_name, app_id, company_id, adva_repo_term, thru_date, capital_account_id, financial_contract_type, loan_overdue_start_day, loan_overdue_end_day, payment_channel_id, ledger_book_no, financial_contract_uuid, sys_normal_deduct_flag, sys_overdue_deduct_flag, sys_create_penalty_flag, sys_create_guarantee_flag, unusual_modify_flag, sys_create_statement_flag, transaction_limit_per_transcation, transaction_limit_per_day, remittance_strategy_mode, app_account_uuids, allow_online_repayment, allow_offline_repayment, allow_advance_deduct_flag, adva_repayment_term, penalty, overdue_default_fee, overdue_service_fee, overdue_other_fee, create_time, last_modified_time, repurchase_approach, repurchase_rule, repurchase_algorithm, day_of_month, pay_for_go, repurchase_principal_algorithm, repurchase_interest_algorithm, repurchase_penalty_algorithm, repurchase_other_charges_algorithm, repayment_check_days) VALUES
 (1, 3, '2016-04-01 00:00:00', 'G08200', '测试合同', 1, 1, 91, '2016-07-01 00:00:00', 5, 0, 1, 90, 2, '74a9ce4d-cafc-407d-b013-987077541bdc', '2d380fe1-7157-490d-9474-12c5a9901e29', 1, 1, 1, 1, 0, 1, null, null, null, null, 1, 0, 0, 0, '(principal+interest)*0.05/100*overdueDay', 123, 23, null, null, '2017-04-06 17:55:17', 1, 1, 'outstandingPrincipal+outstandingOverdueCharges', 30, 0, 'outstandingPrincipal', 'outstandingInterest', 'outstandingPenaltyInterest', null, -1);

 INSERT INTO contract (id, uuid, unique_id, begin_date, contract_no, end_date, asset_type, month_fee, app_id, customer_id, house_id, actual_end_date, create_time, interest_rate, payment_day_in_month, payment_frequency, periods, repayment_way, total_amount, penalty_interest, active_version_no, repayment_plan_operate_logs, state, financial_contract_uuid, interest_rate_cycle, customer_uuid) VALUES
 (1,'315317d0-438a-4e6d-a4b8-20a260624b6e', '51fd9fbc-1e13-473b-92c5-4adfade246b7', '2017-02-23', '云信信2016-78-DK（378）号', '2019-01-01', null, 0.00, 1, 102464, 102619, null, '2017-02-23 17:24:34', 1.0000000000, 0, 0, 3, 2, 3500.00, 0.0050000000, 1, null, 2, '2d380fe1-7157-490d-9474-12c5a9901e29', 2, '57d2f333-de15-4ded-8700-f3fcae0e946c');

INSERT INTO company (id, address, full_name, short_name, uuid) VALUES
(1, '上海', '测试金融公司', '测试金融', 'a02c02b9-6f98-11e6-bf08-00163e002839');

INSERT INTO account (id, account_name, account_no, bank_name, alias, attr, scan_cash_flow_switch, usb_key_configured, uuid, bank_code, cash_flow_config) VALUES
(5, '银企直连专用账户9', '591902896710201', '工商银行', null, '{"bankBranchNo":"59","usbUuid":"b173f24a-3c27-4243-85b7-e93ef6a128ac"}', true, true, 'uuid_5', 'PAB', '{"nextPageNo":"1"}');

INSERT INTO customer (id, account, mobile, name, source, app_id, customer_uuid, customer_type) VALUES
(102464, '340826199407034015', null, '王林广', '51fd9fbc-1e13-473b-92c5-4adfade246b7', 1, '57d2f333-de15-4ded-8700-f3fcae0e946c', 0);

INSERT INTO house (id, address, app_id) VALUES (102619, null, 1);

INSERT INTO app (id, app_id, app_secret, is_disabled, host, name, company_id, addressee) VALUES
(1, 'nongfenqi', '', true, '', '测试分期', 2, null);

INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`) VALUES ('373500', '0', '0', '100.00', '90.00', '1.00', '91.00', '2017-04-22', '2017-04-09', '0.00', '1', '2', '0', NULL, '259ef4d2-95d0-478a-b2e3-e9fe3ec1dc0d', '2017-04-21 17:50:40', '2017-04-21 17:53:02', NULL, 'ZC1691381619185422336', '1', '2017-04-09 10:00:00', '1', '0', NULL, '1', '0', '0', 'empty_deduct_uuid', NULL, '2d380fe1-7157-490d-9474-12c5a9901e29', '12f2d1ab43432e896004fab5eeac44b2', '513521f46449e01fe6b0e9dc3ee9035b', '2017-04-21 17:50:40', '2017-04-21 17:50:40', '0', '0', '0', '0', '0', '2', '0', '57d2f333-de15-4ded-8700-f3fcae0e946c', '315317d0-438a-4e6d-a4b8-20a260624b6e', '0');
INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`) VALUES ('373501', '0', '0', '10010.00', '10000.00', '1.00', '10001.00', '2027-05-16', NULL, '0.00', '0', '1', '0', NULL, '922b2d45-da2c-4b6b-87a2-30a5303a5da0', '2027-04-21 17:50:40', '2017-04-21 17:50:40', NULL, 'ZC1691381619856510976', '1', NULL, '2', '0', NULL, '1', '0', '0', 'empty_deduct_uuid', NULL, '2d380fe1-7157-490d-9474-12c5a9901e29', 'baed3060329832c3b3c8df6337e1bf35', '513521f46449e01fe6b0e9dc3ee9035b', '2017-04-21 17:50:40', '2017-04-21 17:50:40', '0', '0', '0', '0', '0', '0', '0', '57d2f333-de15-4ded-8700-f3fcae0e946c', '315317d0-438a-4e6d-a4b8-20a260624b6e', NULL);
INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`) VALUES ('373502', '0', '0', '9920.00', '9910.00', '1.00', '9911.00', '2027-12-18', NULL, '0.00', '0', '1', '0', NULL, '1dcb05fd-8602-4030-b45b-180b4d0b8281', '2027-04-21 17:50:40', '2017-04-21 17:50:40', NULL, 'ZC1691381620527599616', '1', NULL, '3', '0', NULL, '1', '0', '0', 'empty_deduct_uuid', NULL, '2d380fe1-7157-490d-9474-12c5a9901e29', 'eefa7bbe6cca45afba129716475512ec', '513521f46449e01fe6b0e9dc3ee9035b', '2017-04-21 17:50:40', '2017-04-21 17:50:40', '0', '0', '0', '0', '0', '0', '0', '57d2f333-de15-4ded-8700-f3fcae0e946c', '315317d0-438a-4e6d-a4b8-20a260624b6e', NULL);

INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`) VALUES ('373503', '0', '0', '100.00', '90.00', '1.00', '91.00', '2017-04-22', '2017-04-09', '0.00', '1', '2', '0', NULL, 'b91e77b6-2d3c-4691-86b9-40fa0dc1e611', '2017-04-21 17:50:40', '2017-04-21 17:53:02', NULL, 'ZC1750723569322520543', '1', '2017-04-09 10:00:00', '1', '0', NULL, '1', '1', '0', 'empty_deduct_uuid', NULL, '2d380fe1-7157-490d-9474-12c5a9901e29', '12f2d1ab43432e896004fab5eeac44b2', '513521f46449e01fe6b0e9dc3ee9035b', '2017-04-21 17:50:40', '2017-04-21 17:50:40', '0', '0', '0', '0', '0', '2', '0', '57d2f333-de15-4ded-8700-f3fcae0e946c', '315317d0-438a-4e6d-a4b8-20a260624b6e', '0');
INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`) VALUES ('373504', '0', '0', '10010.00', '10000.00', '1.00', '10001.00', '2027-05-16', NULL, '0.00', '0', '1', '0', NULL, '46e5b86d-9d47-4f84-8ad7-4dbec504d396', '2027-04-21 17:50:40', '2017-04-21 17:50:40', NULL, 'ZC1750723569590956032', '1', NULL, '2', '0', NULL, '1', '1', '0', 'empty_deduct_uuid', NULL, '2d380fe1-7157-490d-9474-12c5a9901e29', 'baed3060329832c3b3c8df6337e1bf35', '513521f46449e01fe6b0e9dc3ee9035b', '2017-04-21 17:50:40', '2017-04-21 17:50:40', '0', '0', '0', '0', '0', '0', '0', '57d2f333-de15-4ded-8700-f3fcae0e946c', '315317d0-438a-4e6d-a4b8-20a260624b6e', NULL);
INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`) VALUES ('373505', '0', '0', '9920.00', '9910.00', '1.00', '9911.00', '2027-12-18', NULL, '0.00', '0', '1', '0', NULL, '4b351be7-a152-42dc-9f91-9fad6713da34', '2027-04-21 17:50:40', '2017-04-21 17:50:40', NULL, 'ZC1750723569859391488', '1', NULL, '3', '0', NULL, '1', '1', '0', 'empty_deduct_uuid', NULL, '2d380fe1-7157-490d-9474-12c5a9901e29', 'eefa7bbe6cca45afba129716475512ec', '513521f46449e01fe6b0e9dc3ee9035b', '2017-04-21 17:50:40', '2017-04-21 17:50:40', '0', '0', '0', '0', '0', '0', '0', '57d2f333-de15-4ded-8700-f3fcae0e946c', '315317d0-438a-4e6d-a4b8-20a260624b6e', NULL);

INSERT INTO `asset_set_extra_charge` (`id`, `asset_set_extra_charge_uuid`, `asset_set_uuid`, `create_time`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`) VALUES ('314', 'bf927ba4-eafa-4720-994c-0d0413bc9459', '259ef4d2-95d0-478a-b2e3-e9fe3ec1dc0d', '2016-09-12 19:39:42', '2016-09-12 19:39:42', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE', '10000.03', NULL, NULL, '0.00');
INSERT INTO `asset_set_extra_charge` (`id`, `asset_set_extra_charge_uuid`, `asset_set_uuid`, `create_time`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`) VALUES ('315', 'b6b445b2-1fbe-4d97-94bc-24590f9db307', '259ef4d2-95d0-478a-b2e3-e9fe3ec1dc0d', '2016-09-12 19:39:42', '2016-09-12 19:39:42', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_TECH_FEE', '10000.04', NULL, NULL, '0.00');
INSERT INTO `asset_set_extra_charge` (`id`, `asset_set_extra_charge_uuid`, `asset_set_uuid`, `create_time`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`) VALUES ('316', '229500a4-58ac-4d8b-b725-fbd21ac46256', '259ef4d2-95d0-478a-b2e3-e9fe3ec1dc0d', '2016-09-12 19:39:42', '2016-09-12 19:39:42', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_OTHER_FEE', '10000.05', NULL, NULL, '0.00');
INSERT INTO `asset_set_extra_charge` (`id`, `asset_set_extra_charge_uuid`, `asset_set_uuid`, `create_time`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`) VALUES ('317', '5502486a-dd61-4eb3-b58f-179738a6cd94', '922b2d45-da2c-4b6b-87a2-30a5303a5da0', '2016-09-12 19:39:42', '2016-09-12 19:39:42', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE', '10000.03', NULL, NULL, '0.00');
INSERT INTO `asset_set_extra_charge` (`id`, `asset_set_extra_charge_uuid`, `asset_set_uuid`, `create_time`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`) VALUES ('318', 'eda30b30-bdf8-4751-ba88-5a8fa2f0c346', '922b2d45-da2c-4b6b-87a2-30a5303a5da0', '2016-09-12 19:39:42', '2016-09-12 19:39:42', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_TECH_FEE', '10000.04', NULL, NULL, '0.00');
INSERT INTO `asset_set_extra_charge` (`id`, `asset_set_extra_charge_uuid`, `asset_set_uuid`, `create_time`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`) VALUES ('319', '0dc5e6b3-149a-4eef-b89a-3bec61577fda', '922b2d45-da2c-4b6b-87a2-30a5303a5da0', '2016-09-13 10:07:52', '2016-09-13 10:07:52', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_OTHER_FEE', '10000.05', NULL, NULL, '0.00');
INSERT INTO `asset_set_extra_charge` (`id`, `asset_set_extra_charge_uuid`, `asset_set_uuid`, `create_time`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`) VALUES ('320', '99d53933-b272-4406-a330-537570d50a55', '1dcb05fd-8602-4030-b45b-180b4d0b8281', '2016-09-13 10:07:52', '2016-09-13 10:07:52', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE', '10000.03', NULL, NULL, '0.00');
INSERT INTO `asset_set_extra_charge` (`id`, `asset_set_extra_charge_uuid`, `asset_set_uuid`, `create_time`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`) VALUES ('321', 'ca7bace7-a17c-4545-9532-4bfeaf36895c', '1dcb05fd-8602-4030-b45b-180b4d0b8281', '2016-09-13 10:07:52', '2016-09-13 10:07:52', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_TECH_FEE', '10000.04', NULL, NULL, '0.00');
INSERT INTO `asset_set_extra_charge` (`id`, `asset_set_extra_charge_uuid`, `asset_set_uuid`, `create_time`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`) VALUES ('322', '1724e70a-4898-417e-bf82-caf941478f8e', '1dcb05fd-8602-4030-b45b-180b4d0b8281', '2016-09-13 10:07:52', '2016-09-13 10:07:52', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_OTHER_FEE', '10000.05', NULL, NULL, '0.00');

INSERT INTO `asset_set_extra_charge` (`id`, `asset_set_extra_charge_uuid`, `asset_set_uuid`, `create_time`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`) VALUES ('323', 'f8df053b-b9f1-406e-b6df-541398574743', 'b91e77b6-2d3c-4691-86b9-40fa0dc1e611', '2016-09-13 10:07:52', '2016-09-13 10:07:52', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE', '10000.03', NULL, NULL, '0.00');
INSERT INTO `asset_set_extra_charge` (`id`, `asset_set_extra_charge_uuid`, `asset_set_uuid`, `create_time`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`) VALUES ('324', '2e71f147-26b6-4449-8c85-e8245dc85e02', 'b91e77b6-2d3c-4691-86b9-40fa0dc1e611', '2016-09-13 10:07:52', '2016-09-13 10:07:52', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_TECH_FEE', '10000.04', NULL, NULL, '0.00');
INSERT INTO `asset_set_extra_charge` (`id`, `asset_set_extra_charge_uuid`, `asset_set_uuid`, `create_time`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`) VALUES ('325', 'f737d201-1719-4b3b-814f-877364409d78', 'b91e77b6-2d3c-4691-86b9-40fa0dc1e611', '2016-09-13 10:07:52', '2016-09-13 10:07:52', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_OTHER_FEE', '10000.05', NULL, NULL, '0.00');
INSERT INTO `asset_set_extra_charge` (`id`, `asset_set_extra_charge_uuid`, `asset_set_uuid`, `create_time`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`) VALUES ('326', 'd3902f7b-a4f1-44eb-9e47-9c7532ab6118', '46e5b86d-9d47-4f84-8ad7-4dbec504d396', '2016-09-13 10:07:52', '2016-09-13 10:07:52', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE', '10000.03', NULL, NULL, '0.00');
INSERT INTO `asset_set_extra_charge` (`id`, `asset_set_extra_charge_uuid`, `asset_set_uuid`, `create_time`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`) VALUES ('327', '297c57fc-36c0-4610-add3-4a2affc42205', '46e5b86d-9d47-4f84-8ad7-4dbec504d396', '2016-09-13 10:07:52', '2016-09-13 10:07:52', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_TECH_FEE', '10000.04', NULL, NULL, '0.00');
INSERT INTO `asset_set_extra_charge` (`id`, `asset_set_extra_charge_uuid`, `asset_set_uuid`, `create_time`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`) VALUES ('328', 'fa0c4bd2-8914-4d7a-adb1-89ccb2962a9c', '46e5b86d-9d47-4f84-8ad7-4dbec504d396', '2016-09-13 10:07:52', '2016-09-13 10:07:52', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_OTHER_FEE', '10000.05', NULL, NULL, '0.00');
INSERT INTO `asset_set_extra_charge` (`id`, `asset_set_extra_charge_uuid`, `asset_set_uuid`, `create_time`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`) VALUES ('329', '05284b30-ecc9-4352-b9ce-5c1b38b3f860', 'e0f7053b-383a-4ded-a881-69bfa9c1fe50', '2016-09-13 10:07:52', '2016-09-13 10:07:52', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE', '10000.03', NULL, NULL, '0.00');
INSERT INTO `asset_set_extra_charge` (`id`, `asset_set_extra_charge_uuid`, `asset_set_uuid`, `create_time`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`) VALUES ('330', 'ccf06646-e5a2-4a49-92a7-bd21d135259f', 'e0f7053b-383a-4ded-a881-69bfa9c1fe50', '2016-09-13 10:07:52', '2016-09-13 10:07:52', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_TECH_FEE', '10000.04', NULL, NULL, '0.00');
INSERT INTO `asset_set_extra_charge` (`id`, `asset_set_extra_charge_uuid`, `asset_set_uuid`, `create_time`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`) VALUES ('331', '2bd42ba9-654b-4432-b6c2-125f39dce77c', '4b351be7-a152-42dc-9f91-9fad6713da34', '2016-09-13 10:07:52', '2016-09-13 10:07:52', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_OTHER_FEE', '10000.05', NULL, NULL, '0.00');

INSERT INTO `repayment_plan_extra_data` (`id`, `uuid`, `asset_set_uuid`, `financial_contract_uuid`, `customer_uuid`, `contract_uuid`, `reason_code`, `comment`, `create_time`, `last_modify_time`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`) VALUES ('1', '4d61fdb5-0709-4cc0-ba97-5f8b704a8eaf', '259ef4d2-95d0-478a-b2e3-e9fe3ec1dc0d', '2d380fe1-7157-490d-9474-12c5a9901e29', '57d2f333-de15-4ded-8700-f3fcae0e946c', '315317d0-438a-4e6d-a4b8-20a260624b6e', '6', NULL, '2017-05-09 17:07:12', '2017-05-09 17:07:12', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `repayment_plan_extra_data` (`id`, `uuid`, `asset_set_uuid`, `financial_contract_uuid`, `customer_uuid`, `contract_uuid`, `reason_code`, `comment`, `create_time`, `last_modify_time`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`) VALUES ('2', 'a0683639-db61-426a-81b1-af7bfa80962b', '922b2d45-da2c-4b6b-87a2-30a5303a5da0', '2d380fe1-7157-490d-9474-12c5a9901e29', '57d2f333-de15-4ded-8700-f3fcae0e946c', '315317d0-438a-4e6d-a4b8-20a260624b6e', '6', NULL, '2017-05-09 17:08:54', '2017-05-09 17:08:54', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `repayment_plan_extra_data` (`id`, `uuid`, `asset_set_uuid`, `financial_contract_uuid`, `customer_uuid`, `contract_uuid`, `reason_code`, `comment`, `create_time`, `last_modify_time`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`) VALUES ('3', 'd75e96be-cf19-40c4-9b76-e1f01bcfd9da', '1dcb05fd-8602-4030-b45b-180b4d0b8281', '2d380fe1-7157-490d-9474-12c5a9901e29', '57d2f333-de15-4ded-8700-f3fcae0e946c', '315317d0-438a-4e6d-a4b8-20a260624b6e', '6', NULL, '2017-05-09 17:08:54', '2017-05-09 17:08:54', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO `repayment_plan_extra_data` (`id`, `uuid`, `asset_set_uuid`, `financial_contract_uuid`, `customer_uuid`, `contract_uuid`, `reason_code`, `comment`, `create_time`, `last_modify_time`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`) VALUES ('4', 'cac4036f-ff30-4b90-aeb8-b773d07ae798', 'b91e77b6-2d3c-4691-86b9-40fa0dc1e611', '2d380fe1-7157-490d-9474-12c5a9901e29', '57d2f333-de15-4ded-8700-f3fcae0e946c', '315317d0-438a-4e6d-a4b8-20a260624b6e', '5', NULL, '2017-05-09 17:08:54', '2017-05-09 17:08:54', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `repayment_plan_extra_data` (`id`, `uuid`, `asset_set_uuid`, `financial_contract_uuid`, `customer_uuid`, `contract_uuid`, `reason_code`, `comment`, `create_time`, `last_modify_time`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`) VALUES ('5', 'e0a64fb9-aae8-4e74-b454-ad48a2d8afe1', '46e5b86d-9d47-4f84-8ad7-4dbec504d396', '2d380fe1-7157-490d-9474-12c5a9901e29', '57d2f333-de15-4ded-8700-f3fcae0e946c', '315317d0-438a-4e6d-a4b8-20a260624b6e', '7', NULL, '2017-05-09 17:30:38', '2017-05-09 17:30:38', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `repayment_plan_extra_data` (`id`, `uuid`, `asset_set_uuid`, `financial_contract_uuid`, `customer_uuid`, `contract_uuid`, `reason_code`, `comment`, `create_time`, `last_modify_time`, `date_field_one`, `date_field_two`, `date_field_three`, `long_field_one`, `long_field_two`, `long_field_three`, `string_field_one`, `string_field_two`, `string_field_three`, `decimal_field_one`, `decimal_field_two`, `decimal_field_three`) VALUES ('6', 'd2baa933-e7a3-485d-9cbc-a77cba953364', '4b351be7-a152-42dc-9f91-9fad6713da34', '2d380fe1-7157-490d-9474-12c5a9901e29', '57d2f333-de15-4ded-8700-f3fcae0e946c', '315317d0-438a-4e6d-a4b8-20a260624b6e', '6', NULL, '2017-05-09 17:31:02', '2017-05-09 17:31:02', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS=1;