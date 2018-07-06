package com.suidifu.datasync.repository;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import com.suidifu.datasync.canal.StaticsConfig.TABLE;
import com.suidifu.datasync.entity.RemittanceAuditResult;

@Component("remittanceAuditResultRepositoryImpl")
public class RemittanceAuditResultRepositoryImpl extends JdbcDaoSupport implements RemittanceAuditResultRepository {

	private static final String SQL_EXIST = "SELECT count(id) FROM remittance_audit_result WHERE redis_key=? ";
	private static final String SQL_INSERT = "INSERT INTO remittance_audit_result (`last_modified_time`,`redis_key`, `cash_flow_identity`, `cash_flow_transaction_time`, `financial_contract_uuid`, `host_account_no`,  `payment_channel_uuid`, `result_code`, `result_time`, `system_bill_identity`, `system_bill_occur_date`, `system_bill_type`,`trade_uuid`,`system_bill_plan_amount`,`cash_flow_transaction_amount`,`cash_flow_account_side`) VALUES (SYSDATE(),?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?)";
	private static final String SQL_UPDATE = "UPDATE remittance_audit_result SET `last_modified_time`=SYSDATE(),`cash_flow_identity`=?, `cash_flow_transaction_time`=?, `financial_contract_uuid`=?, `host_account_no`=?,`payment_channel_uuid`=?, `result_code`=?,`result_time`=?, `system_bill_identity`=?, `system_bill_occur_date`=?,`system_bill_type`=?,`trade_uuid`=?,`system_bill_plan_amount`=?,`cash_flow_transaction_amount`=?,`cash_flow_account_side`=? WHERE `redis_key`=?";
	private static final String SQL_DELETE = "DELETE FROM remittance_audit_result WHERE redis_key=? ";
	private static final String SQL_COUNT = "SELECT COUNT(id) FROM remittance_audit_result";

	@Autowired
	public RemittanceAuditResultRepositoryImpl(DataSource dataSource) {
		setDataSource(dataSource);
	}

	@Override
	public void saveOrUpdate(RemittanceAuditResult result) {
		// if(result==null)return;
		if (getJdbcTemplate().queryForObject(SQL_EXIST, Integer.class, result.getRedisKey()) <= 0)
			getJdbcTemplate().update(SQL_INSERT, result.getRedisKey(), result.getCashFlowIdentity(), result.getCashFlowTransactionTime(),
					result.getFinancialContractUuid(), result.getHostAccountNo(), result.getPaymentChannelUuid(), result.getResultCode(),
					result.getResultTime(), result.getSystemBillIdentity(), result.getSystemBillOccurDate(), result.getSystemBillType(), result.getTradeId(),
					result.getSystemBillPlanAmount(), result.getCashFlowTransactionAmount(), result.getCashFlowAccountSide());
		else
			getJdbcTemplate().update(SQL_UPDATE, result.getCashFlowIdentity(), result.getCashFlowTransactionTime(), result.getFinancialContractUuid(),
					result.getHostAccountNo(), result.getPaymentChannelUuid(), result.getResultCode(), result.getResultTime(), result.getSystemBillIdentity(),
					result.getSystemBillOccurDate(), result.getSystemBillType(), result.getTradeId(), result.getSystemBillPlanAmount(),
					result.getCashFlowTransactionAmount(), result.getCashFlowAccountSide(), result.getRedisKey());
	}

	@Override
	public void delete(String rediskey) {
		getJdbcTemplate().update(SQL_DELETE, rediskey);
	}

	@Override
	public long countResult() {
		return getJdbcTemplate().queryForObject(SQL_COUNT, Long.class);
	}

	@Override
	public void clear() {
		getJdbcTemplate().execute("TRUNCATE remittance_audit_result");
		for (TABLE table : TABLE.values())
			getJdbcTemplate().execute("TRUNCATE " + table.name());
	}

	@Override
	public void test(String sql) {
		getJdbcTemplate().execute(sql);
	}

}
