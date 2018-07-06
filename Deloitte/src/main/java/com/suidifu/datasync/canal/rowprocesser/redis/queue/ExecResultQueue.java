package com.suidifu.datasync.canal.rowprocesser.redis.queue;

import org.springframework.data.redis.core.StringRedisTemplate;

import com.suidifu.datasync.canal.StaticsConfig.RemittanceType;
import com.suidifu.datasync.repository.RemittanceAuditResultRepository;

public class ExecResultQueue extends ResultQueue {

	public ExecResultQueue(StringRedisTemplate redistemp, RemittanceAuditResultRepository mysqlRepository, int queueNum, int batchSize) {
		super(redistemp, mysqlRepository, queueNum, batchSize);
	}

	@Override
	public void run() {
		process(RemittanceType.贷);
	}

}
