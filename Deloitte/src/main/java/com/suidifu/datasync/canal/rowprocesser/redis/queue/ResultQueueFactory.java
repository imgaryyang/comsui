package com.suidifu.datasync.canal.rowprocesser.redis.queue;

import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

import com.suidifu.datasync.canal.StaticsConfig.RemittanceType;
import com.suidifu.datasync.canal.rowprocesser.redis.queue.hash.ConsistentHash;
import com.suidifu.datasync.repository.RemittanceAuditResultRepository;

@Component("resultQueueFactory")
public class ResultQueueFactory {
	@Autowired
	private StringRedisTemplate redistemp;

	@Resource(name = "remittanceAuditResultRepositoryImpl")
	private RemittanceAuditResultRepository mysqlRepository;

	@Value("${redis.result.queue.exec-size}")
	private int execQueueSize;
	@Value("${redis.result.queue.refund-size}")
	private int refundQueueSize;
	@Value("${redis.result.queue.batch-size}")
	private int batchSize;
	@Value("${redis.result.queue.fixed-delay}")
	private long delay;

	// 对账结果队列
	public static ConsistentHash CONSISTENT_HASH_EXEC = null;
	public static ConsistentHash CONSISTENT_HASH_REFUND = null;

	public void init() {
		CONSISTENT_HASH_EXEC = new ConsistentHash(RemittanceType.贷, execQueueSize);
		CONSISTENT_HASH_REFUND = new ConsistentHash(RemittanceType.借, refundQueueSize);
		for (int i = 1; i <= execQueueSize; i++)
			new ConcurrentTaskScheduler(Executors.newSingleThreadScheduledExecutor(new CustomizableThreadFactory("<Q_exec_" + i + ">")))
					.scheduleWithFixedDelay(new ExecResultQueue(redistemp, mysqlRepository, i, batchSize), delay);
		for (int i = 1; i <= refundQueueSize; i++)
			new ConcurrentTaskScheduler(Executors.newSingleThreadScheduledExecutor(new CustomizableThreadFactory("<Q_refund_" + i + ">")))
					.scheduleWithFixedDelay(new RefundResultQueue(redistemp, mysqlRepository, i, batchSize), delay);
	}

	public boolean hashNotNull() {
		return CONSISTENT_HASH_EXEC != null && CONSISTENT_HASH_REFUND != null;
	}
}
