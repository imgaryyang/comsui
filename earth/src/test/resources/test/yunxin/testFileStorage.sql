SET FOREIGN_KEY_CHECKS = 0;

delete from `file_storage`;

INSERT INTO `file_storage` (`id`, `uuid`, `related_uuid`, `origin_file_name`, `save_file_name`, `extension`, `path`, `application_type`, `create_time`, `uploader`, `version_no`, `expiry_time`, `can_be_downloaded`)
VALUES 
	('1', '6eb9511f-30e6-40dc-b17a-9613857f36ae', 'e83aa72d-da72-4e0f-be80-322dcac6ba62', 'asset_set.sql', '6eb9511f30e640dcb17a9613857f36ae_asset_set.sql', 'sql', 'C:\\upload\\contracts\\', '1', '2016-12-06 17:33:54', 'xiewenqian', '-253556996', NULL, '1');


SET FOREIGN_KEY_CHECKS = 1;