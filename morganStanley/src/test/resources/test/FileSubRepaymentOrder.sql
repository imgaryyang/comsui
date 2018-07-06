SET FOREIGN_KEY_CHECKS=0;
	delete from `t_notify_job_fileNotifyServer`;
	delete from `t_file_repository`;
INSERT INTO `t_file_repository` (`id`, `uuid`, `product_code`, `file_type_code`, `status`, `path`, `upload_time`, `add_time`, `last_modify_time`, `csv_size`, `trade_size`, `execute_status`, `send_status`, `process_status`, `process_size`) VALUES ('24', '7ac9bf17-b4d5-4aba-8ebf-f3ff3e380899', 'spdbank', '10004', '0', '/home/hjl/桌面/测试用文件夹/spdbank/2017-08-24/repaymentOrder_t_test_zfb_20170816_sdfasdfasdfasfsdasfasdfasfasd.txt', '2017-08-24 15:57:46', '2017-08-24 15:57:46', '2017-08-24 19:34:13', '0', '0', '0', '0', '0', '0');

SET FOREIGN_KEY_CHECKS=1;