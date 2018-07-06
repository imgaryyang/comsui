package com.suidifu.datasync.repository;

import com.suidifu.datasync.entity.RemittanceAuditResult;

/**
 * 同步mysql数据库操作
 * 
 * @author lisf
 *
 */
public interface RemittanceAuditResultRepository {

	public void delete(String rediskey);

	public void saveOrUpdate(RemittanceAuditResult result);

	public long countResult();

	// for test
	public void clear();

	// for test
	public void test(String sql);
}