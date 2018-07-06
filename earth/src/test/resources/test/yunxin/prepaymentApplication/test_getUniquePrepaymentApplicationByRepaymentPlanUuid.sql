
delete from `t_prepayment_application`;



INSERT INTO `t_prepayment_application` (`id`, `contract_id`, `asset_set_id`, `unique_id`, `contract_no`, `request_no`, `asset_recycle_date`, `asset_initial_value`, `type`, `asset_set_uuid`, `create_time`, `ip`, `prepayment_status`, `completed_time`, `bepred_repayment_plan_uuid_list`, `pay_way`)
VALUES
(1, 8050, 18183, 'f522e269-8281-4bf2-b922-7c691881ef6a', NULL, 'b6fd1e37-3a8b-459a-8e94-e5397db47ba2', '2016-09-30', '50001.0', 0, 'ce51a10c-67b1-43cd-ac7d-983071531e5c', '2016-09-20 15:21:48', '101.52.128.162', 1, '2016-09-30 13:00:15', NULL, NULL),
(2, 10281, 25001, '282686d5-7d9f-4105-8c2b-8e26d1fc4c3a', NULL, 'ed5a89d7-58ea-4d11-af51-3f1c4fdc66f6', '2016-09-23', '5001.0', 0, '0b15d285-7136-48c4-9bc6-a3a7a7530936', '2016-09-22 17:44:35', '101.52.128.162', 1, '2016-09-23 13:00:29', NULL, NULL);
