
SET FOREIGN_KEY_CHECKS=0;

delete from t_remittance_application;



INSERT INTO `t_remittance_application` (`id`, `remittance_application_uuid`, `request_no`, `financial_contract_uuid`, `financial_contract_id`, `financial_product_code`, `contract_unique_id`, `contract_no`, `planned_total_amount`, `actual_total_amount`, `auditor_name`, `audit_time`, `notify_url`, `plan_notify_number`, `actual_notify_number`, `remittance_strategy`, `remark`, `transaction_recipient`, `execution_status`, `execution_remark`, `create_time`, `creator_name`, `ip`, `last_modified_time`, `opposite_receive_date`)
VALUES
	(4, '895efa32-66e3-4fad-99fc-4c5af166afa6', '5b6f59b9-67d3-48f6-81d9-588dfb99898e', 'db36ecc9-d80c-4350-bd0d-59b1139d550d', 33, 'G00003', '547083b0-0c32-42b4-862e-653e85d944fa', 'G00003(zht205999057302258273)', 0.02, 0.00, '', NULL, 'http://101.52.128.166/Loan/PaidNotic', 3, 3, 0, '', 1, 2, NULL, '2016-09-02 11:38:02', 't_merchant', '101.52.128.162', '2016-09-01 11:43:27', '2016-09-02 11:43:27'),
	(2, '183fdea2-9773-4686-ba87-9caf83865609', '1384071f-a7cd-4235-80a7-89a667138f5d', 'db36ecc9-d80c-4350-bd0d-59b1139d550d', 33, 'G00003', '547083b0-0c32-42b4-862e-653e85d944fa', 'G00003(zht205999057302258273)', 0.02, 0.00, '', NULL, 'http://101.52.128.166/Loan/PaidNotic', 3, 3, 0, '', 1, 2, NULL, '2016-09-01 11:37:43', 't_merchant', '101.52.128.162', '2016-09-01 11:43:27', '2016-09-01 11:43:27'),
	(3, '15d52e4d-c5a6-428f-bda2-c31147eb79af', 'e93bbf5f-39c4-49ea-a3a2-ff72d14a1c47', 'db36ecc9-d80c-4350-bd0d-59b1139d550d', 33, 'G00003', '547083b0-0c32-42b4-862e-653e85d944fa', 'G00003(zht205999057302258273)', 0.02, 0.00, '', NULL, 'http://101.52.128.166/Loan/PaidNotic', 3, 3, 0, '', 1, 2, NULL, '2016-09-03 11:37:52', 't_merchant', '101.52.128.162', '2016-09-01 11:43:27', '2016-09-03 11:43:27');

	
	
	
	
SET FOREIGN_KEY_CHECKS=1;