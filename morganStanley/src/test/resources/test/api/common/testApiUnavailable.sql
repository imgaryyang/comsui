SET FOREIGN_KEY_CHECKS = 0;

USE galaxy_autotest_yunxin;
DROP TABLE IF EXISTS t_api_config;

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

DELETE FROM t_api_config;

INSERT INTO t_api_config (api_url, fn_code, description, api_status)
VALUES
  ('/api/v3/modifyRepaymentPlan', NULL, '变更还款计划', 0);
