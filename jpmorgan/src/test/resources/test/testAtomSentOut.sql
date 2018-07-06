SET FOREIGN_KEY_CHECKS=0;

delete from `payment_order`;

INSERT INTO `payment_order` (`id`, `access_version`, `account_side`, `business_status`, `business_status_last_update_time`, `communication_end_time`, `communication_last_sent_time`, `communication_start_time`, `communication_status`, `currency_code`, `date_field_one`, `date_field_three`, `date_field_two`, `decimal_field_one`, `decimal_field_three`, `decimal_field_two`, `destination_account_name`, `destination_account_no`, `destination_bank_info`, `fst_communication_status`, `fst_occupied_feed_back_time`, `fst_occupied_force_stop_time`, `fst_occupied_message_record_uuid`, `fst_occupied_result_record_uuid`, `fst_occupied_sent_time`, `fst_occupied_time`, `fst_occupied_worker_ip`, `fst_occuppied_worker_uuid`, `fst_occupy_status`, `gateway_type`, `long_field_one`, `long_field_three`, `long_field_two`, `postscript`, `snd_communication_status`, `snd_occupied_feed_back_time`, `snd_occupied_force_stop_time`, `snd_occupied_message_record_uuid`, `snd_occupied_result_record_uuid`, `snd_occupied_sent_time`, `snd_occupied_time`, `snd_occupied_worker_ip`, `snd_occuppied_worker_uuid`, `snd_occupy_status`, `source_account_name`, `source_account_no`, `source_bank_info`, `source_message_ip`, `source_message_time`, `source_message_uuid`, `string_field_one`, `string_field_three`, `string_field_two`, `transaction_amount`, `transaction_uuid`, `trd_communication_status`, `trd_occupied_feed_back_time`, `trd_occupied_force_stop_time`, `trd_occupied_message_record_uuid`, `trd_occupied_result_record_uuid`, `trd_occupied_sent_time`, `trd_occupied_time`, `trd_occupied_worker_ip`, `trd_occuppied_worker_uuid`, `trd_occupy_status`, `uuid`)
VALUES
	(1, '4be9587b-cd39-432e-bd3a-65a249e1dcf8', 0, 0, NULL, NULL, NULL, '2016-06-20 12:17:12', 1, '10', NULL, NULL, NULL, NULL, NULL, NULL, '张建明', '6226227705568339', '305100000013', 1, NULL, NULL, NULL, NULL, NULL, '2016-06-20 12:17:12', NULL, '030f6ca5-558a-47d6-a259-6cf9d6842f6c', 1, 1, 0, 0, 0, '超级网银测试', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, '571907757810703', 'CB', NULL, '2016-06-07 16:06:17', '25b90874-3dc7-4439-910f-6525c9a71bc9', NULL, NULL, NULL, 0.01, '997914de-a946-49b4-9b80-0246e5192de7', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'ee85778a-d0a4-4e1d-af20-985c112eb064');


SET FOREIGN_KEY_CHECKS=1;
