package com.suidifu.datasync.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suidifu.datasync.canal.StaticsConfig;
import com.suidifu.datasync.canal.StaticsConfig.EXEC_CASHFLOW_TBS;
import com.suidifu.datasync.canal.StaticsConfig.REFUND_CASHFLOW_TBS;
import com.suidifu.datasync.canal.StaticsConfig.RemittanceType;
import com.suidifu.datasync.repository.RemittanceAuditResultRepository;

@Controller
@RequestMapping("/redis")
public class UserController {
	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private StringRedisTemplate redistemp;

	@Resource(name = "remittanceAuditResultRepositoryImpl")
	private RemittanceAuditResultRepository mysqlRepository;

	@Value("${spring.profiles.active}")
	private String springProfilesActive;

	@RequestMapping("/init")
	@ResponseBody
	public String init() {
		if (!"dev".equals(springProfilesActive))
			return "不要在非测试环境中调用此方法！";
		LOG.info("初始化...");
		//mysqlRepository.clear();

		// LAST_CLEAR_STATUS
		redistemp.delete(StaticsConfig.LAST_STATUS);
		for (RemittanceType remittanceType : RemittanceType.values()) {
			redistemp.delete(remittanceType.errorResultRedisKey());

			for (int i = 0; i < 100; i++)
				redistemp.delete(remittanceType.syncResultRedisKey(i));

			// cashflow account_side 0;
			for (EXEC_CASHFLOW_TBS exec_cashflow_tb : EXEC_CASHFLOW_TBS.values()) {
				redistemp.delete(remittanceType.redisKey(exec_cashflow_tb));
				redistemp.delete(remittanceType.recordRedisKey(exec_cashflow_tb));
			}
			// cashflow account_side 1;
			for (REFUND_CASHFLOW_TBS refund_cashflow_tb : REFUND_CASHFLOW_TBS.values()) {
				redistemp.delete(remittanceType.redisKey(refund_cashflow_tb));
				redistemp.delete(remittanceType.recordRedisKey(refund_cashflow_tb));
			}
		}
		return "初始化结束...";
	}

	@RequestMapping("/result")
	@ResponseBody
	public String result() {
		LOG.info("查询result结果数目");
		long size1 = redistemp.opsForHash().size(RemittanceType.贷.resultRedisKey());
		long size2 = redistemp.opsForHash().size(RemittanceType.借.resultRedisKey());
		long size3 = mysqlRepository.countResult();
		return String.format("贷结果：%s,借结果：%s,共：%s，数据库结果共：%s，同步结果是否一致？：%s", size1, size2, (size1 + size2), size3, (size3 == (size1 + size2)));
	}
}
