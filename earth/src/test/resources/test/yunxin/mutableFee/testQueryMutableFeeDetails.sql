delete from `t_mutable_fee_detail_log`;

INSERT INTO `t_mutable_fee_detail_log` (`id`, `effective_time`, `mutable_fee_no`, `reason_code`, `original_asset_interest_value`, `asset_interest_value`, `approver`, `approved_time`, `comment`, `create_time`, `repayment_plan_no`) 
VALUES
 ('68', '2017-04-10 12:12:40', 'MF1491797560886', '0', '20.00', '60.00', 'FXF', '2017-04-08 00:00:00', 'TestInterface', '2017-04-10 12:12:40', 'ZC1561074340376481792'),
 ('69', '2017-04-10 12:12:40', 'MF1491897560854', '0', '20.00', '60.00', 'FXF', '2017-04-08 00:00:00', 'TestInterface', '2017-04-10 14:12:40', 'ZC1561074340376481792'),
 ('70', '2017-04-10 14:29:52', 'MF1491905792280', '0', '80.00', '60.00', 'FXF', '2017-04-10 00:00:00', 'TestInterface', '2017-04-10 15:29:52', 'ZC1561074340376481792'),
 ('71', '2017-04-10 14:29:52', 'MF1492105792654', '0', '80.00', '60.00', 'FXF', '2017-04-10 00:00:00', 'TestInterface', '2017-04-10 16:29:52', 'ZC1561074340376481792');
