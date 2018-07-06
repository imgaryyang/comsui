DROP TABLE IF EXISTS t_api_config;
USE galaxy_autotest_yunxin;
CREATE TABLE t_api_config (
  id          BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  api_url     VARCHAR(255)
              COLLATE utf8_unicode_ci      DEFAULT NULL
  COMMENT '接口地址',
  fn_code     VARCHAR(255)
              COLLATE utf8_unicode_ci      DEFAULT NULL
  COMMENT '功能代码',
  description VARCHAR(255)
              COLLATE utf8_unicode_ci      DEFAULT NULL
  COMMENT '接口说明',
  api_status  INT(11)                      DEFAULT '0'
  COMMENT '接口状态0 : 关闭，1 : 启用',
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci
  COMMENT ='接口配置表';

DELETE FROM dictionary;
DELETE FROM t_api_config;

INSERT INTO t_api_config (api_url, fn_code, description, api_status)
VALUES
  ('/api/v3/modifyRepaymentPlan', NULL, '变更还款计划', 1);

INSERT INTO dictionary (code, content)
VALUES
  ('PLATFORM_PRI_KEY',
   'MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=');