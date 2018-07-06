# 第三方凭证表增加batch_no字段
ALTER TABLE third_party_voucher_command_log
  ADD COLUMN batch_no VARCHAR(255) DEFAULT NULL
COMMENT '批次号';
ALTER TABLE third_party_transaction_record
  ADD COLUMN batch_no VARCHAR(255) DEFAULT NULL
COMMENT '批次号';

# 第三方凭证表增加version_no字段
ALTER TABLE third_party_voucher_command_log
  ADD COLUMN version_no VARCHAR(255) DEFAULT NULL
COMMENT '版本号';

# 第三方凭证表增加process_status字段
ALTER TABLE third_party_voucher_command_log
  ADD COLUMN process_status TINYINT(4) DEFAULT 0;

# 第三方凭证表增加retied_times字段
ALTER TABLE third_party_voucher_command_log
  ADD COLUMN retied_times INT(11) DEFAULT 0
COMMENT '重试次数';

# 第三方凭证表增加retied_time字段
ALTER TABLE third_party_voucher_command_log
  ADD COLUMN retied_time DATETIME DEFAULT NULL
COMMENT '重试时间';

# channel_worker_config表增加merchant_no字段
ALTER TABLE channel_worker_config
  ADD COLUMN merchant_no VARCHAR(50) DEFAULT NULL
COMMENT '商户号';

# 新增第三方凭证历史表
CREATE TABLE `third_party_voucher_command_log_in_history` (
  `id`                         BIGINT(20) NOT NULL     AUTO_INCREMENT
  COMMENT 'id',
  `voucher_uuid`               VARCHAR(255)
                               COLLATE utf8_unicode_ci DEFAULT NULL
  COMMENT '凭证编号',
  `voucher_no`                 VARCHAR(255)
                               COLLATE utf8_unicode_ci DEFAULT NULL,
  `request_no`                 VARCHAR(255)
                               COLLATE utf8_unicode_ci DEFAULT NULL,
  `batch_uuid`                 VARCHAR(255)
                               COLLATE utf8_unicode_ci DEFAULT NULL,
  `trade_uuid`                 VARCHAR(255)
                               COLLATE utf8_unicode_ci DEFAULT NULL,
  `bank_transaction_no`        VARCHAR(255)
                               COLLATE utf8_unicode_ci DEFAULT NULL,
  `financial_contract_uuid`    VARCHAR(255)
                               COLLATE utf8_unicode_ci DEFAULT NULL,
  `financial_contract_no`      VARCHAR(255)
                               COLLATE utf8_unicode_ci DEFAULT NULL,
  `contract_unique_id`         VARCHAR(255)
                               COLLATE utf8_unicode_ci DEFAULT NULL,
  `transcation_amount`         DECIMAL(19, 2)          DEFAULT NULL,
  `transaction_time`           DATETIME                DEFAULT NULL,
  `transcation_complete_time`  DATETIME                DEFAULT NULL,
  `transcation_gateway`        TINYINT(4)              DEFAULT NULL,
  `repayment_no_json_list`     VARCHAR(255)
                               COLLATE utf8_unicode_ci DEFAULT NULL,
  `create_time`                DATETIME                DEFAULT NULL,
  `execution_status`           TINYINT(4)              DEFAULT NULL,
  `voucher_log_status`         TINYINT(4)              DEFAULT NULL,
  `voucher_log_issue_status`   TINYINT(4)              DEFAULT NULL,
  `receivable_account_no`      VARCHAR(255)
                               COLLATE utf8_unicode_ci DEFAULT NULL,
  `payment_account_no`         VARCHAR(255)
                               COLLATE utf8_unicode_ci DEFAULT NULL,
  `payment_name`               VARCHAR(255)
                               COLLATE utf8_unicode_ci DEFAULT NULL,
  `third_part_voucher_content` TEXT COLLATE utf8_unicode_ci COMMENT '第三方凭证内容详情',
  `voucher_source`             TINYINT(4)              DEFAULT NULL,
  `ip`                         VARCHAR(255)
                               COLLATE utf8_unicode_ci DEFAULT NULL,
  `error_message`              TEXT COLLATE utf8_unicode_ci,
  `comment`                    TEXT COLLATE utf8_unicode_ci,
  `status_modify_time`         DATETIME                DEFAULT NULL,
  `error_code_list`            VARCHAR(255)
                               COLLATE utf8_unicode_ci DEFAULT NULL
  COMMENT '错误编码集合',
  `version_no`                 VARCHAR(255)            DEFAULT NULL
  COMMENT '版本号',
  `batch_no`                   VARCHAR(255)
                               COLLATE utf8_unicode_ci DEFAULT NULL
  COMMENT '批次号',
  `process_status`             TINYINT(4)              DEFAULT '0',
  `retied_times`               INT(11)                 DEFAULT 0
  COMMENT '重试次数',
  `retied_time`                DATETIME                DEFAULT NULL
  COMMENT '重试时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `voucher_no` (`voucher_no`),
  KEY `batch_uuid` (`batch_uuid`) USING HASH,
  KEY `trade_uuid` (`trade_uuid`) USING HASH
)

  # 插入channel_worker_config表
  INSERT INTO channel_worker_config (id, worker_uuid, channel_identity, local_working_config, merchant_no
) VALUES (NULL, NULL, 'GATEWAY_TRANSACTION_RECORDS_GZ', '{"userName":"operator","userPwd":"operator","merchantId":"001053110000001","url":"http://59.41.103.98:333/gzdsf/ProcessServlet","cerFilePath":"/data/test_cert/gzdsf.cer","pfxFilePath":"/data/test_cert/ORA@TEST1.pfx","pfxFileKey":"123456"}', '001053110000001'
);
INSERT INTO channel_worker_config (id, worker_uuid, channel_identity, local_working_config, merchant_no) VALUES
  (NULL, NULL, 'GATEWAY_TRANSACTION_RECORDS_BAOFU',
   '{"pfxpath":"/data/test_cert/bfkey_100000276@@100000991.pfx","cerpath":"/data/test_cert/bfkey_100000276@@100000991.cer","url":"https://tgw.baofoo.com/cutpayment/api/backTransRequest","memberId":"100000276","terminalId":"100000991","merchantId":"000191400206128","channelAccountNo":"100000276","pfxpwd":"123456"}',
   '100000276');
INSERT INTO channel_worker_config (id, worker_uuid, channel_identity, local_working_config, merchant_no) VALUES
  (NULL, NULL, 'GATEWAY_TRANSACTION_RECORDS_CPCN',
   '{"InstitutionID":"002762","pfxKeyPath":"/data/test_cert/test.pfx","pfxKeyPassword":"cfca1234","cerKeyPath":"/data/test_cert/.paytest.cer","url":"https://test.cpcn.com.cn/Gateway/InterfaceII"}',
   '002762');
INSERT INTO channel_worker_config (id, worker_uuid, channel_identity, local_working_config, merchant_no) VALUES
  (NULL, NULL, 'GATEWAY_TRANSACTION_RECORDS_UFCPAY',
   '{"merchantId":"M200000550","pfxKey":"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQChFetx5+VKDoEXzZ+5Wozt3MfWMM/TiKMlWmAKXBViv8/e6j6SU/lSlWkMajd59aiWczs+qf9dMuRpe/l9Qke9DnVMn24JNLXjWD+y+w3yKRwd3CTtF7gx8/ToZl5XqFIT5YB1QfQCdAf8Z18IdQrJIijs8ssczY/RfqKZLo+KLQIDAQAB","url":"http://sandbox.firstpay.com/security/gateway.do"}',
   'M200000550');
INSERT INTO channel_worker_config (id, worker_uuid, channel_identity, local_working_config, merchant_no) VALUES
  (NULL, NULL, 'GATEWAY_TRANSACTION_RECORDS_YIJIFU',
   '{"partnerId":"20160831020000752643","secretyKey":"b04fbc6afc77b131c355dd1788215dbb","filePath":"/data/webapp/","url":"http://merchantapi.yijifu.net/gateway.html"}',
   '20160831020000752643');
