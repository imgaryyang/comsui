delete from `notice`;
INSERT INTO `notice` (`id`, `notice_uuid`, `title`, `content`, `release_time`, `status_change_time`, `notice_status`) 
	VALUES ('1', 'dafd068f-7975-4320-b960-e3049d3ca846', '标题1', '内容1', '2016-11-22 16:35:27', '2016-11-22 16:35:27', '1');
INSERT INTO `notice` (`id`, `notice_uuid`, `title`, `content`, `release_time`, `status_change_time`, `notice_status`) 
	VALUES ('2', 'cc422abc-df26-431e-a0dd-caaf56d0bfac', '标题2', '内容2', NULL, '2016-11-22 16:50:30', '2');
INSERT INTO `notice` (`id`, `notice_uuid`, `title`, `content`, `release_time`, `status_change_time`, `notice_status`) 
	VALUES ('3', '9de257f7-b6cb-405d-8b9e-2328f4ea52ef', '标题3', '内容3', '2016-11-16 13:44:45', '2016-11-22 16:50:37', '1');
