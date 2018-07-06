package com.suidifu.datasync.canal.rowprocesser.redis.queue.hash;

public interface HashFunction {
	public int hash(String key);
}
