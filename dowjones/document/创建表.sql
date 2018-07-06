-- 还款汇总表
CREATE TABLE IF NOT EXISTS `t_return_summary` (
  `create_date`                                  DATE                                 DEFAULT NULL COMMENT '日期',
  `capital_addition`                             DECIMAL(19, 4)                       DEFAULT NULL COMMENT '资金追加',
  `remittance_count`                             BIGINT(20)                           DEFAULT NULL COMMENT '当日放款笔数',
  `remittance_amount`                            DECIMAL(19, 4)                       DEFAULT NULL COMMENT '当日放款金额',
  `deduct_count`                                 BIGINT(20)                           DEFAULT NULL COMMENT '当日扣款笔数',
  `deduct_amount`                                DECIMAL(19, 4)                       DEFAULT NULL COMMENT '扣款金额',
  `deduct_principal`                             DECIMAL(19, 4)                       DEFAULT NULL COMMENT '扣款本金',
  `deduct_interest`                              DECIMAL(19, 4)                       DEFAULT NULL COMMENT '扣款利息',
  `deduct_service_fee`                           DECIMAL(19, 4)                       DEFAULT NULL COMMENT '扣款服务费',
  `deduct_tech_fee`                              DECIMAL(19, 4)                       DEFAULT NULL COMMENT '扣款技术维护费',
  `deduct_other_fee`                             DECIMAL(19, 4)                       DEFAULT NULL COMMENT '扣款其他费用',
  `deduct_overdue_fee_penalty`                   DECIMAL(19, 4)                       DEFAULT NULL COMMENT '扣款罚息',
  `deduct_overdue_fee_obligation`                DECIMAL(19, 4)                       DEFAULT NULL COMMENT '扣款逾期违约金',
  `deduct_overdue_fee_service`                   DECIMAL(19, 4)                       DEFAULT NULL COMMENT '扣款逾期服务费',
  `deduct_overdue_fee_other`                     DECIMAL(19, 4)                       DEFAULT NULL COMMENT '扣款逾期其他费用',
  `offline_repayment_count`                      BIGINT(20)                           DEFAULT NULL COMMENT '线下还款笔数',
  `offline_repayment_amount`                     DECIMAL(19, 4)                       DEFAULT NULL COMMENT '线下还款金额',
  `offline_repayment_principal`                  DECIMAL(19, 4)                       DEFAULT NULL COMMENT '线下还款本金',
  `offline_repayment_interest`                   DECIMAL(19, 4)                       DEFAULT NULL COMMENT '线下还款利息',
  `offline_repayment_service_fee`                DECIMAL(19, 4)                       DEFAULT NULL COMMENT '线下还款服务费',
  `offline_repayment_tech_fee`                   DECIMAL(19, 4)                       DEFAULT NULL COMMENT '线下还款技术维护费',
  `offline_repayment_other_fee`                  DECIMAL(19, 4)                       DEFAULT NULL COMMENT '线下还款其他费用',
  `offline_repayment_overdue_fee_penalty`        DECIMAL(19, 4)                       DEFAULT NULL COMMENT '线下还款罚息',
  `offline_repayment_overdue_fee_obligation`     DECIMAL(19, 4)                       DEFAULT NULL COMMENT '线下还款逾期违约金',
  `offline_repayment_overdue_fee_service`        DECIMAL(19, 4)                       DEFAULT NULL COMMENT '线下还款逾期服务费',
  `offline_repayment_overdue_fee_other`          DECIMAL(19, 4)                       DEFAULT NULL COMMENT '线下还款逾期其他费用',
  `fst_payment_channel_name`                     VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '第一通道名称',
  `fst_payment_channel_count`                    BIGINT(20)                           DEFAULT NULL COMMENT '第一通道笔数',
  `fst_payment_channel_amount`                   DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第一通道金额',
  `fst_payment_channel_principal`                DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第一通道本金',
  `fst_payment_channel_interest`                 DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第一通道利息',
  `fst_payment_channel_service_fee`              DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第一通道服务费',
  `fst_payment_channel_tech_fee`                 DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第一通道技术维护费',
  `fst_payment_channel_other_fee`                DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第一通道其他费用',
  `fst_payment_channel_overdue_fee_penalty`      DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第一通道罚息',
  `fst_payment_channel_overdue_fee_obligation`   DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第一通道逾期违约金',
  `fst_payment_channel_overdue_fee_service`      DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第一通道逾期服务费',
  `fst_payment_channel_overdue_fee_other`        DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第一通道逾期其他费用',
  `snd_payment_channel_name`                     VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '第二通道名称',
  `snd_payment_channel_count`                    BIGINT(20)                           DEFAULT NULL COMMENT '第二通道笔数',
  `snd_payment_channel_amount`                   DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第二通道金额',
  `snd_payment_channel_principal`                DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第二通道本金',
  `snd_payment_channel_interest`                 DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第二通道利息',
  `snd_payment_channel_service_fee`              DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第二通道服务费',
  `snd_payment_channel_tech_fee`                 DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第二通道技术维护费',
  `snd_payment_channel_other_fee`                DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第二通道其他费用',
  `snd_payment_channel_overdue_fee_penalty`      DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第二通道罚息',
  `snd_payment_channel_overdue_fee_obligation`   DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第二通道逾期违约金',
  `snd_payment_channel_overdue_fee_service`      DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第二通道逾期服务费',
  `snd_payment_channel_overdue_fee_other`        DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第二通道逾期其他费用',
  `trd_payment_channel_name`                     VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '第三通道名称',
  `trd_payment_channel_count`                    BIGINT(20)                           DEFAULT NULL COMMENT '第三通道笔数',
  `trd_payment_channel_amount`                   DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第三通道金额',
  `trd_payment_channel_principal`                DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第三通道本金',
  `trd_payment_channel_interest`                 DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第三通道利息',
  `trd_payment_channel_service_fee`              DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第三通道服务费',
  `trd_payment_channel_tech_fee`                 DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第三通道技术维护费',
  `trd_payment_channel_other_fee`                DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第三通道其他费用',
  `trd_payment_channel_overdue_fee_penalty`      DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第三通道罚息',
  `trd_payment_channel_overdue_fee_obligation`   DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第三通道逾期违约金',
  `trd_payment_channel_overdue_fee_service`      DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第三通道逾期服务费',
  `trd_payment_channel_overdue_fee_other`        DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第三通道逾期其他费用',
  `fth_payment_channel_name`                     VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '第四通道名称',
  `fth_payment_channel_count`                    BIGINT(20)                           DEFAULT NULL COMMENT '第四通道笔数',
  `fth_payment_channel_amount`                   DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第四通道金额',
  `fth_payment_channel_principal`                DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第四通道本金',
  `fth_payment_channel_interest`                 DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第四通道利息',
  `fth_payment_channel_service_fee`              DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第四通道服务费',
  `fth_payment_channel_tech_fee`                 DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第四通道技术维护费',
  `fth_payment_channel_other_fee`                DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第四通道其他费用',
  `fth_payment_channel_overdue_fee_penalty`      DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第四通道罚息',
  `fth_payment_channel_overdue_fee_obligation`   DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第四通道逾期违约金',
  `fth_payment_channel_overdue_fee_service`      DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第四通道逾期服务费',
  `fth_payment_channel_overdue_fee_other`        DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第四通道逾期其他费用',
  `fifth_payment_channel_name`                   VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '第五通道名称',
  `fifth_payment_channel_count`                  BIGINT(20)                           DEFAULT NULL COMMENT '第五通道笔数',
  `fifth_payment_channel_amount`                 DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第五通道金额',
  `fifth_payment_channel_principal`              DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第五通道本金',
  `fifth_payment_channel_interest`               DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第五通道利息',
  `fifth_payment_channel_service_fee`            DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第五通道服务费',
  `fifth_payment_channel_tech_fee`               DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第五通道技术维护费',
  `fifth_payment_channel_other_fee`              DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第五通道其他费用',
  `fifth_payment_channel_overdue_fee_penalty`    DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第五通道罚息',
  `fifth_payment_channel_overdue_fee_obligation` DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第五通道逾期违约金',
  `fifth_payment_channel_overdue_fee_service`    DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第五通道逾期服务费',
  `fifth_payment_channel_overdue_fee_other`      DECIMAL(19, 4)                       DEFAULT NULL COMMENT '第五通道逾期其他费用',
  `id`                                           INT(11) UNSIGNED NOT NULL            AUTO_INCREMENT,
  `financial_contract_uuid`                      VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_financial_contract_uuid` (`financial_contract_uuid`),
  KEY `index_create_date` (`create_date`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- 借方流水
CREATE TABLE IF NOT EXISTS `debit_cash_flow` (
  `fingerPrinter`           VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '指纹',
  `cashFlowNo`              VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '流水号',
  `accountSide`             VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '借贷标记',
  `amount`                  DECIMAL(19, 4)                       DEFAULT NULL COMMENT '借方发生金额',
  `principal`               DECIMAL(19, 4)                       DEFAULT NULL COMMENT '实收本金',
  `interest`                DECIMAL(19, 4)                       DEFAULT NULL COMMENT '实收利息',
  `loanServiceFee`          DECIMAL(19, 4)                       DEFAULT NULL COMMENT '实收贷款服务费',
  `loanTechFee`             DECIMAL(19, 4)                       DEFAULT NULL COMMENT '实收技术维护费',
  `loanOtherFee`            DECIMAL(19, 4)                       DEFAULT NULL COMMENT '实收其他费用',
  `punishment`              DECIMAL(19, 4)                       DEFAULT NULL COMMENT '实收逾期罚息',
  `overdueFee`              DECIMAL(19, 4)                       DEFAULT NULL COMMENT '实收逾期违约金',
  `overdueServiceFee`       DECIMAL(19, 4)                       DEFAULT NULL COMMENT '实收逾期服务费',
  `overdueOtherFee`         DECIMAL(19, 4)                       DEFAULT NULL COMMENT '实收逾期其他费用',
  `counterAccountNo`        VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '对方账号',
  `counterAccountName`      VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '对方户名',
  `counterBankInfo`         VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '对方开户行',
  `transactionTime`         DATETIME                             DEFAULT NULL COMMENT '入账时间',
  `remark`                  VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '摘要',
  `otherRemark`             VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '附言',
  `id`                      INT(11) UNSIGNED NOT NULL            AUTO_INCREMENT,
  `create_date`             DATE                                 DEFAULT NULL,
  `financial_contract_uuid` VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_financial_contract_uuid` (`financial_contract_uuid`),
  KEY `index_create_date` (`create_date`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- 还款订单汇总表
CREATE TABLE IF NOT EXISTS `repayment_order_summary` (
  `orderNo`                 VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '订单号',
  `totalAmount`             DECIMAL(19, 4)                       DEFAULT NULL COMMENT '总额',
  `principal`               DECIMAL(19, 4)                       DEFAULT NULL COMMENT '本金',
  `interest`                DECIMAL(19, 4)                       DEFAULT NULL COMMENT '利息',
  `loanServiceFee`          DECIMAL(19, 4)                       DEFAULT NULL COMMENT '贷款服务费',
  `loanTechFee`             DECIMAL(19, 4)                       DEFAULT NULL COMMENT '贷款技术费',
  `loanOtherFee`            DECIMAL(19, 4)                       DEFAULT NULL COMMENT '贷款其他费用',
  `punishment`              DECIMAL(19, 4)                       DEFAULT NULL COMMENT '罚息',
  `overdueFee`              DECIMAL(19, 4)                       DEFAULT NULL COMMENT '逾期违约金',
  `overdueServiceFee`       DECIMAL(19, 4)                       DEFAULT NULL COMMENT '逾期服务费',
  `overdueOtherFee`         DECIMAL(19, 4)                       DEFAULT NULL COMMENT '逾期其他费用',
  `id`                      INT(11) UNSIGNED NOT NULL            AUTO_INCREMENT,
  `create_date`             DATE                                 DEFAULT NULL,
  `financial_contract_uuid` VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_financial_contract_uuid` (`financial_contract_uuid`),
  KEY `index_create_date` (`create_date`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;


-- 在途线上支付单明细
CREATE TABLE IF NOT EXISTS `online_payment_details_in_transit` (
  `fingerPrinter`           VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '指纹',
  `bankCard`                VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '银行卡',
  `orderNo`                 VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '订单号',
  `totalAmount`             DECIMAL(19, 4)                       DEFAULT NULL COMMENT '总额',
  `principal`               DECIMAL(19, 4)                       DEFAULT NULL COMMENT '本金',
  `interest`                DECIMAL(19, 4)                       DEFAULT NULL COMMENT '利息',
  `loanServiceFee`          DECIMAL(19, 4)                       DEFAULT NULL COMMENT '贷款服务费',
  `loanTechFee`             DECIMAL(19, 4)                       DEFAULT NULL COMMENT '贷款技术费',
  `loanOtherFee`            DECIMAL(19, 4)                       DEFAULT NULL COMMENT '贷款其他费用',
  `punishment`              DECIMAL(19, 4)                       DEFAULT NULL COMMENT '罚息',
  `overdueFee`              DECIMAL(19, 4)                       DEFAULT NULL COMMENT '逾期违约金',
  `overdueServiceFee`       DECIMAL(19, 4)                       DEFAULT NULL COMMENT '逾期服务费',
  `overdueOtherFee`         DECIMAL(19, 4)                       DEFAULT NULL COMMENT '逾期其他费用',
  `id`                      INT(11) UNSIGNED NOT NULL            AUTO_INCREMENT,
  `create_date`             DATE                                 DEFAULT NULL,
  `financial_contract_uuid` VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_financial_contract_uuid` (`financial_contract_uuid`),
  KEY `index_create_date` (`create_date`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- 银企直联借记流水
CREATE TABLE IF NOT EXISTS `bank_corporate_cash_flow` (
  `bank_sequence_no`        VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '流水号',
  `account_side`            VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '借贷标记',
  `transaction_amount`      DECIMAL(19, 4)                       DEFAULT NULL COMMENT '总额',
  `id`                      INT(11) UNSIGNED NOT NULL            AUTO_INCREMENT,
  `create_date`             DATE                                 DEFAULT NULL,
  `financial_contract_uuid` VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_financial_contract_uuid` (`financial_contract_uuid`),
  KEY `index_create_date` (`create_date`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;


