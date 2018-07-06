

SET FOREIGN_KEY_CHECKS=0;

delete from `t_deduct_application`;
delete from `t_deduct_application_detail`;
DELETE FROM contract;
DELETE FROM `asset_set`;
DELETE FROM `journal_voucher`;
delete from `financial_contract`;
delete from `contract`;


INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`, `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`, `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`, `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`, `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`, `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`, `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`, `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`, `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`)
VALUES
	(310220, 0, 0, 444.00, 444.00, 0.00, 444.00, '2016-12-26', '2016-12-26', 0.00, 0, 2, 1, '2016-12-26 18:34:13', 'e0793743-f967-4e62-9ae4-2977ba720144', '2016-12-26 18:34:13', '2016-12-26 19:12:02', NULL, 'ZC275CAAEEACACC480', 310021, '2016-12-26 19:11:59', 1, 2, NULL, 2062951494, 0, 0, '31627dc0-dcf3-49dc-adda-8caf52228398', NULL, 'd2812bc5-5057-4a91-b3fd-9019506f0499', NULL, NULL, '2016-12-26 18:34:13', '2016-12-26 18:34:13', 1, 0, 1, 1, 3, 2, 0, '02cbb1fd-0b29-4884-b347-471f5970d4f6', 'e8e5b8e3-2f50-47f2-b7ed-316e21ac3cbb', 0);

	INSERT INTO `journal_voucher` (`id`, `account_side`, `bank_identity`, `billing_plan_uuid`, `booking_amount`, `business_voucher_type_uuid`, `business_voucher_uuid`, `cash_flow_amount`, `cash_flow_breif`, `cash_flow_channel_type`, `cash_flow_serial_no`, `cash_flow_uuid`, `checking_level`, `company_id`, `completeness`, `counter_party_account`, `counter_party_name`, `journal_voucher_uuid`, `notification_identity`, `notification_memo`, `notification_record_uuid`, `notified_date`, `settlement_modes`, `source_document_amount`, `source_document_breif`, `source_document_cash_flow_serial_no`, `source_document_counter_party_uuid`, `source_document_identity`, `source_document_uuid`, `status`, `trade_time`, `batch_uuid`, `created_date`, `source_document_counter_party_account`, `source_document_counter_party_name`, `issued_time`, `journal_voucher_type`, `counter_account_type`, `related_bill_contract_info_lv_1`, `related_bill_contract_info_lv_2`, `related_bill_contract_info_lv_3`, `cash_flow_account_info`, `journal_voucher_no`, `related_bill_contract_no_lv_1`, `related_bill_contract_no_lv_2`, `related_bill_contract_no_lv_3`, `related_bill_contract_no_lv_4`, `source_document_no`, `appendix`, `last_modified_time`, `source_document_local_party_account`, `source_document_local_party_name`, `local_party_account`, `local_party_name`, `second_journal_voucher_type`, `third_journal_voucher_type`)
VALUES
	(250022, 1, '中国建设银行 ', 'e0793743-f967-4e62-9ae4-2977ba720144', 434.00, '2b784ef2-a1ab-41fa-b315-8e1bf8cfa2ba', '', 434.00, NULL, 2, NULL, NULL, 0, 1, 3, '12312893521892130', '测试员1', '48d58b2a-f2ac-4797-b08a-5704280dd423', NULL, NULL, NULL, NULL, NULL, 434.00, NULL, NULL, NULL, '4f7aa4a3-5623-45f8-a4a9-d1cd2ecdc263', '944a0ba8-dcaa-4f90-b699-06d993e697ba', 1, NULL, NULL, '2016-12-26 19:12:02', '12312893521892130', '测试员1', NULL, 7, 0, 'd2812bc5-5057-4a91-b3fd-9019506f0499', 'e8e5b8e3-2f50-47f2-b7ed-316e21ac3cbb', 'e0793743-f967-4e62-9ae4-2977ba720144', '', '794a6ef1-e702-46c8-8ea0-61ffe5b62d3c', '拍拍贷测试', 'bmw云信信016-103-DK(20161226)', 'ZC275CAAEEACACC480', 'JS275CAB6585B6AB82', 'KK275CAB658515BFC1', NULL, '2016-12-26 19:12:02', '600000000001', '云南信托国际有限公司', '600000000001', '云南信托国际有限公司', 1, 1);

	
SET FOREIGN_KEY_CHECKS=1;