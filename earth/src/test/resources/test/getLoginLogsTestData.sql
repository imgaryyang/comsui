SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM system_log;

INSERT INTO `system_log` (`id`, `operate_time`, `user_id`, `ip`, `event`, `content`)
VALUES ('2', '2015-07-02 13:28:28', '5', '127.0.0.1', '登陆', '登陆');
INSERT INTO `system_log` (`id`, `operate_time`, `user_id`, `ip`, `event`, `content`)
VALUES ('3', '2015-07-02 13:28:59', '5', '127.0.0.1', '退出', '退出登陆');

SET FOREIGN_KEY_CHECKS = 1;