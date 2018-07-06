SET FOREIGN_KEY_CHECKS=0;

truncate `t_notify_job_fileNotifyServer`;
CREATE TABLE `t_notify_job_fileNotifyServer` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `notify_job_uuid` varchar(255) DEFAULT NULL,
  `request_url` text,
  `request_method` varchar(255) DEFAULT NULL,
  `request_params` text,
  `response` text,
  `last_http_response_code` varchar(255) DEFAULT NULL,
  `last_request_time` timestamp NULL DEFAULT NULL,
  `job_status` int(11) DEFAULT NULL,
  `retry_times` int(11) DEFAULT NULL,
  `retry_intervals` text,
  `retried_times` int(11) DEFAULT NULL,
  `absolute_start_time` timestamp NULL DEFAULT NULL,
  `last_modified_time` timestamp NULL DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `server_identity`  varchar(255) DEFAULT NULL,
  `business_id` varchar(255) NULL DEFAULT NULL,
  `business_type`  varchar(255) DEFAULT NULL,
  `priority` int(11),
  `job_dir` varchar(255) default null,
  `head_params` text,
  PRIMARY KEY (`id`)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

SET FOREIGN_KEY_CHECKS=1;