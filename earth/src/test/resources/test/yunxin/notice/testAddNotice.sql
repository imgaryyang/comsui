delete from `notice`;
delete from `system_operate_log`;
INSERT INTO `notice` (`id`, `notice_uuid`, `title`, `content`, `release_time`,`status_change_time`, `notice_status`) 
	VALUES ('1', 'dafd068f-7975-4320-b960-e3049d3ca846', '标题1', '内容1', '2016-11-16 13:11:39','2016-11-16 13:11:39', '1');
INSERT INTO `notice` (`id`, `notice_uuid`, `title`, `content`, `release_time`, `status_change_time`,`notice_status`) 
	VALUES ('2', 'cc422abc-df26-431e-a0dd-caaf56d0bfac', '标题2', '内容2','2016-11-16 13:42:42','2016-11-16 13:42:42','2');
INSERT INTO `notice` (`id`, `notice_uuid`, `title`, `content`, `release_time`,`status_change_time`, `notice_status`) 
	VALUES ('3', '9de257f7-b6cb-405d-8b9e-2328f4ea52ef', '标题3', '内容3', '2016-11-16 13:44:45','2016-11-16 13:44:45','1');
INSERT INTO `notice` (`id`, `notice_uuid`, `title`, `content`,`notice_status`) 
	VALUES ('4', '9de257f7-b6cb-405d-8b9e-2328f4ea53fc', '标题4', '内容4','0');
