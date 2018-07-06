INSERT INTO `asset_set` (`guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`, `reason_code`)
VALUES
	(0,0,1010.00,900.00,100.00,1000.00,'2015-10-19',NULL,NULL,0,NULL,0,NULL,'asset_set_uuid_1','2015-10-19 13:34:35','2015-10-19 13:34:35',NULL,'DK_00101111',1,'2015-10-19 13:34:35',0,0,NULL,1,0,0,'empty_deduct_uuid',NULL,NULL,NULL,NULL,NULL,NULL,0,0,0,0,0,0,NULL,'11111db75ebb24fa0993f4fa25775023',NULL,NULL,NULL);


INSERT INTO `contract` (`uuid`, `unique_id`, `begin_date`, `contract_no`, `end_date`, `asset_type`, `month_fee`, `app_id`, `customer_id`, `house_id`, `actual_end_date`, `create_time`, `interest_rate`, `payment_day_in_month`, `payment_frequency`, `periods`, `repayment_way`, `total_amount`, `penalty_interest`, `active_version_no`, `repayment_plan_operate_logs`, `state`, `financial_contract_uuid`, `interest_rate_cycle`, `customer_uuid`)
VALUES
	("test_contract_uuid_1","test_contract_unique_1",'2015-10-19','TEST_DKHD-001','2016-10-19',NULL,NULL,1,1,1,NULL,'2015-10-19 13:34:35',0.0010000000,1,1,12,0,1000.00,0.0001000000,1,NULL,2,NULL,0,'11111db75ebb24fa0993f4fa25775023');

INSERT INTO `customer` (`account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`)
VALUES
	("1234567899",NULL,NULL,NULL,1,'1111db75ebb24fa0993f4fa25775023',1);


insert into repayment_order_item(order_detail_uuid, contract_unique_id, contract_no, contract_uuid, detail_alive_status, detail_pay_status, detail_pay_result_status, repayment_way, repayment_business_no,repayment_business_uuid,mer_id,repayment_business_type, repayment_plan_time, order_uuid,order_unique_id,remark)
values (
	"order_detail_uuid", "contract_unique_id", "contract_no", "contract_uuid", 0, 0, 0, 0, "repayment_business_no","repayment_business_uuid","mer_id",0, null, "order_uuid","order_unique_id", "remark"
);
