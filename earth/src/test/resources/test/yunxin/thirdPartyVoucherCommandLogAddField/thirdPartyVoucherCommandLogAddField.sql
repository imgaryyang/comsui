alter  table `third_party_voucher_command_log` add `outer_repayment_plan_no_list` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '商户计划还款还款编号(MD5加密)',
 alter table `third_party_voucher_command_log` add `current_period_list` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '还款期数',