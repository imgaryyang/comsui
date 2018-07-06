SET FOREIGN_KEY_CHECKS=0;

TRUNCATE TABLE t_delay_processing_task;
INSERT INTO `t_delay_processing_task` (`id`, `uuid`,`config_uuid`, `repayment_plan_uuid`, `repurchase_doc_uuid`, `task_execute_date`, `work_params`, `execute_status`, `create_time`, `last_modify_time`, `financial_contract_uuid`, `customer_uuid`, `contract_uuid`)
VALUES
	(1, 'uuid','config_uuid', NULL, 'repurchaseDocUuid', '2017-05-09', 'workParams', 0, '2017-05-09 19:08:02', '2017-05-09 19:08:02',
	'financialContractUuid', 'customerUuid', 'contractUuid');


SET FOREIGN_KEY_CHECKS=1;