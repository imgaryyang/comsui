SET FOREIGN_KEY_CHECKS=0;

delete from `financial_contract`;
delete from `contract`;
delete from `company`;
delete from `account`;
delete from `customer`;
delete from `house`;
delete from `app`;
DELETE FROM `t_product_category`;
DELETE FROM `repurchase_doc`;
TRUNCATE TABLE `t_delay_processing_task`;
TRUNCATE TABLE `t_delay_processing_task_config`;
TRUNCATE TABLE `t_source_repository`;

INSERT INTO `t_product_category` (`uuid`, `pre_process_interface_url`, `status`, `delay_task_config_uuid`)
VALUES
  ( 'faabb9a2-1334-4af5-833f-cd7c4d966bd8', 'repurchase/zhongan/repurchase', 1, 'delay_task_config_uuid_01');

INSERT INTO `t_delay_processing_task_config` (`uuid`,`execute_code_version`, `status`)
VALUES ('delay_task_config_uuid_01','execute_code_version_01', 1);

INSERT INTO t_source_repository (business_type, source_code, signature, source_file_path, add_time, last_modify_time, author, compile_import) VALUES ('delay_task_config_uuid_01',
'public boolean evaluate(Result processedResult, DelayProcessingTaskCacheHandler delayProcessingTaskCacheHandler, SandboxDataSetHandler sandboxDataSetHandler, Map<String, Object> inputMap, Map<String, Object> resultMap,Log log) {
        try{
        log.info("start delay task");
            Date now = new Date();
            String contractUniqueId = (String)inputMap.get("uniqueId");
            String contractNo = (String) inputMap.get("contractNo");
            String taskConfigUuid =(String) inputMap.get("taskConfigUuid");

            SandboxDataSet sandboxDataSet = sandboxDataSetHandler.get_sandbox_for_apply_repurchase_by_contract_uniqueId_contractNo(contractUniqueId, contractNo);
            log.info("sandboxDataSet:" + com.zufangbao.sun.utils.JsonUtils.toJSONString(sandboxDataSet));
            ContractSnapshot contractSnapshot = sandboxDataSet.getContractSnapshot();
            RepurchaseDocSnapshot repurchaseDocSnapshot = sandboxDataSet.getRepurchaseDocSnapshot();

            DelayProcessingTask task = new DelayProcessingTask();
            task.setCreateTime(now);
            task.setConfigUuid(taskConfigUuid);
            task.setRepurchaseDocUuid(repurchaseDocSnapshot.getRepurchaseDocUuid());
            task.setTaskExecuteDate(DateUtils.addDays(now, 1));

            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("tradeNo", UUID.randomUUID().toString().replaceAll("-", ""));
            map.put("loanNo", contractSnapshot.getUniqueId());
byte[] encode_byte = Base64.getEncoder().encode(sandboxDataSet.getIdCardNo().getBytes());
String idCardNo = new String(encode_byte);
            map.put("thirdUserNo",idCardNo);
            map.put("installmentNo", 0);
            map.put("principal",repurchaseDocSnapshot.getRepurchasePrincipal());
            map.put("interest",repurchaseDocSnapshot.getRepurchaseInterest());
            map.put("repayCharge",repurchaseDocSnapshot.getRepurchaseOtherCharges().add(repurchaseDocSnapshot.getRepurchasePenalty()));
            map.put("changeType",8);
            map.put("tradeTime",repurchaseDocSnapshot.getCreateTime());
            String workParams = com.zufangbao.sun.utils.JsonUtils.toJSONString(map);
            task.setWorkParams(workParams);

            task.setFinancialContractUuid(contractSnapshot.getFinancialContractUuid());
            task.setContractUuid(contractSnapshot.getUuid());
            task.setCustomerUuid(contractSnapshot.getCustomerUuid());
            task.setCreateTime(now);
            task.setLastModifyTime(now);

            delayProcessingTaskCacheHandler.save_to_db_cache(task);

            return true;
        }catch (Exception e){
            return false;
        }
    }', 'execute_code_version_01', null, '2017-05-11 19:05:20', '2017-05-11 19:05:25', 'sys','java.util.*,com.suidifu.matryoshka.delayTask.*,com.demo2do.core.entity.*,com.zufangbao.sun.yunxin.entity.model.api.modify.*,com.suidifu.matryoshka.snapshot.*,com.zufangbao.sun.utils.*,com.alibaba.fastjson.JSONObject,com.zufangbao.sun.yunxin.entity.api.mutableFee.MutableFeeReasonCode,org.apache.commons.logging.Log');

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

INSERT INTO repurchase_doc (repurchase_doc_uuid, financial_contract_uuid, amount, repo_start_date, repo_end_date, repo_days, creat_time, verification_time, last_modifed_time, contract_id, contract_no, app_id, app_name, customer_uuid, customer_name, executing_asset_set_uuids, asset_set_uuids, repurchase_status, repurchase_algorithm, repurchase_approach, repurchase_rule, day_of_month, adva_repo_term, repurchase_periods, amount_detail, repurchase_principal, repurchase_principal_algorithm, repurchase_interest, repurchase_interest_algorithm, repurchase_penalty, repurchase_penalty_algorithm, repurchase_other_charges, repurchase_other_charges_algorithm, repurchase_rule_detail) VALUES
('e2dc60ef-768a-4834-9b95-83734937598c', 'b3ec04b4-ec49-416c-8c1e-bb9358516559', 0.00, '2017-05-09', '2017-05-09', 1, '2017-05-09 22:24:10', null, '2017-05-09 22:24:10', 1, '云信信2016-78-DK（378）号', 1, '测试分期', '57d2f333-de15-4ded-8700-f3fcae0e946c', '王林广', null, '["58deae0f-9960-4567-ba40-40d854896d5c"]', 0, null, 2, null, null, 60, 1, '{"amount":0,"repurchaseInterest":0,"repurchaseInterestAlgorithm":"","repurchaseOtherCharges":0,"repurchaseOtherChargesAlgorithm":"","repurchasePenalty":0,"repurchasePenaltyAlgorithm":"","repurchasePrincipal":0,"repurchasePrincipalAlgorithm":""}', 0.00, '', 0.00, '', 0.00, '', 0.00, '', null);

SET FOREIGN_KEY_CHECKS=1;

