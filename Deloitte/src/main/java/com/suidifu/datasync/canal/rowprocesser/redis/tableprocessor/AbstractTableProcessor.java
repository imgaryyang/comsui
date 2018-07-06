package com.suidifu.datasync.canal.rowprocesser.redis.tableprocessor;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.suidifu.datasync.canal.StaticsConfig.EXEC_CASHFLOW_TBS;
import com.suidifu.datasync.canal.StaticsConfig.Event;
import com.suidifu.datasync.canal.StaticsConfig.REFUND_CASHFLOW_TBS;
import com.suidifu.datasync.canal.StaticsConfig.RemittanceType;
import com.suidifu.datasync.canal.rowprocesser.TableProcessor;
import com.suidifu.datasync.canal.rowprocesser.redis.RollbackData;
import com.suidifu.datasync.canal.rowprocesser.redis.domain.ExecLog;
import com.suidifu.datasync.canal.rowprocesser.redis.domain.Result;
import com.suidifu.datasync.canal.rowprocesser.redis.domain.ResultEvent;

/**
 * AbstractTableProcessor
 * 
 * @author lisf
 *
 */
public abstract class AbstractTableProcessor implements TableProcessor {
	@Autowired
	private StringRedisTemplate redistemp;

	@Resource(name = "RollbackData")
	private RollbackData rollbackData;

	@Override
	public String bloomfilterElement(List<Column> columns, EventType eventType) {
		return null;
	}

	// -----------------------result to queue
	protected void result_to_queue(RemittanceType remittanceType, String resultkey, Result result, Event event) {
		redistemp.opsForList().rightPush(remittanceType.getNode(resultkey),
				JSON.toJSONString(new ResultEvent(remittanceType, resultkey, event.code(), result)));
	}

	// -----------------------t_remittance_plan_exec_log-------------
	protected void execlog_put(ExecLog execlog) {
		execlog_put(execlog.getExecid(), JSON.toJSONString(execlog));
	}

	protected void execlog_put(String execid, String execlog_jsonstr) {
		exec_cashflow_put(EXEC_CASHFLOW_TBS.execid_execlog, execid, execlog_jsonstr);
	}

	protected ExecLog execlog_get(String execid) {
		Object jsonstr = exec_cashflow_get(EXEC_CASHFLOW_TBS.execid_execlog, execid);
		if (jsonstr == null)
			return null;
		return JSON.parseObject(jsonstr.toString(), ExecLog.class);
	}

	protected void execid_result_put(String resultkey, Result result) {
		exec_cashflow_put(EXEC_CASHFLOW_TBS.execid_result, resultkey, JSON.toJSONString(result));
		result_to_queue(RemittanceType.贷, resultkey, result, Event.添加或更新);
	}

	protected void execid_result_delete(String resultkey) {
		exec_cashflow_delete(EXEC_CASHFLOW_TBS.execid_result, resultkey);
		result_to_queue(RemittanceType.贷, resultkey, null, Event.删除);
	}

	protected Result execid_result_get(String execid) {
		Object jsonstr = exec_cashflow_get(EXEC_CASHFLOW_TBS.execid_result, execid);
		if (jsonstr == null)
			return null;
		return JSON.parseObject(jsonstr.toString(), Result.class);
	}

	protected void tradeid_execid_put(String tradeid, String execid) {
		exec_cashflow_put(EXEC_CASHFLOW_TBS.tradeid_execid, tradeid, execid);
	}

	protected Object exec_tradeid_cashflow_get(String tradeid) {
		return exec_cashflow_get(EXEC_CASHFLOW_TBS.tradeid_cashflowid, tradeid);
	}

	private void exec_cashflow_put(EXEC_CASHFLOW_TBS exec_cashflow_tb, String hashKey, String hashValue) {
		rollbackData.put(RemittanceType.贷.redisKey(exec_cashflow_tb), hashKey, hashValue);
	}

	private Object exec_cashflow_get(EXEC_CASHFLOW_TBS exec_cashflow_tb, String hashKey) {
		return redistemp.opsForHash().get(RemittanceType.贷.redisKey(exec_cashflow_tb), hashKey);
	}

	private void exec_cashflow_delete(EXEC_CASHFLOW_TBS exec_cashflow_tb, String hashKey) {
		redistemp.opsForHash().delete(RemittanceType.贷.redisKey(exec_cashflow_tb), hashKey);
	}

	// -----------------------cash_flow-------------
	protected void tradeid_cashflowid_put(int account_side, String tradeid, String cashflowid) {
		rollbackData.put(RemittanceType.getCashflowKey(account_side, EXEC_CASHFLOW_TBS.tradeid_cashflowid.name()), tradeid, cashflowid);
	}

	protected Object tradeid_cashflowid_get(int account_side, String tradeid) {
		return redistemp.opsForHash().get(RemittanceType.getCashflowKey(account_side, EXEC_CASHFLOW_TBS.tradeid_cashflowid.name()), tradeid);
	}

	protected Object exec_or_refund_exist_tradeid(int account_side, String tradeid) {
		return redistemp.opsForHash().get(RemittanceType.getCashflowKey(account_side, null), tradeid);
	}

	protected void cashflow_result_put(int account_side, String resultkey, Result result) {
		if (RemittanceType.贷.eq(account_side))
			execid_result_put(resultkey, result);
		else if (RemittanceType.借.eq(account_side))
			refundid_result_put(resultkey, result);
	}

	// protected void cashflowid_cashflow_put(int account_side, String
	// cashflowid, String tradeid) {
	// redistemp.opsForHash().put(StaticsConfig.CANAL_BATCH + ":" +
	// RemittanceType.getCashflowKey(account_side,
	// EXEC_CASHFLOW_TBS.cashflowid_cashflow.name()),
	// cashflowid, tradeid);
	// redistemp.opsForHash().put(RemittanceType.getCashflowKey(account_side,
	// EXEC_CASHFLOW_TBS.cashflowid_cashflow.name()), cashflowid, tradeid);
	// }

	protected Object cashflowid_cashflow_get(int account_side, String cashflowid) {
		return redistemp.opsForHash().get(RemittanceType.getCashflowKey(account_side, EXEC_CASHFLOW_TBS.cashflowid_cashflow.name()), cashflowid);
	}

	// -----------------------t_remittance_refund_bill-------------
	protected boolean refund_cashflow_exist_tradeid(String tradeid) {
		return tradeid != null && refund_tradeid_cashflowid_get(tradeid) != null;
	}

	protected Object refund_tradeid_cashflowid_get(String tradeid) {
		return refund_cashflow_get(REFUND_CASHFLOW_TBS.tradeid_cashflowid, tradeid);
	}

	protected Object refundbill_get(String refundid) {
		return refund_cashflow_get(REFUND_CASHFLOW_TBS.refundid_refundbill, refundid);
	}

	protected void refundbill_put(String refundid, String refundbill_jsonstr) {
		refund_cashflow_put(REFUND_CASHFLOW_TBS.refundid_refundbill, refundid, refundbill_jsonstr);
	}

	protected Result refundid_result_get(String resultkey) {
		Object jsonstr = refund_cashflow_get(REFUND_CASHFLOW_TBS.refundid_result, resultkey);
		if (jsonstr == null)
			return null;
		return JSON.parseObject(jsonstr.toString(), Result.class);
	}

	protected void refundid_result_put(String resultkey, Result result) {
		refund_cashflow_put(REFUND_CASHFLOW_TBS.refundid_result, resultkey, JSON.toJSONString(result));
		result_to_queue(RemittanceType.借, resultkey, result, Event.添加或更新);
	}

	protected void refundid_result_delete(String resultkey) {
		refund_cashflow_delete(REFUND_CASHFLOW_TBS.refundid_result, resultkey);
		result_to_queue(RemittanceType.借, resultkey, null, Event.删除);
	}

	protected void tradeid_refundid_put(String tradeid, String refundid) {
		refund_cashflow_put(REFUND_CASHFLOW_TBS.tradeid_refundid, tradeid, refundid);
	}

	private void refund_cashflow_put(REFUND_CASHFLOW_TBS refund_cashflow_tb, String hashKey, String hashValue) {
		// redistemp.opsForHash().put(StaticsConfig.CANAL_BATCH + ":" +
		// RemittanceType.借.redisKey(refund_cashflow_tb), hashKey,
		// StaticsConfig.EMPTY);
		// redistemp.opsForHash().put(RemittanceType.借.redisKey(refund_cashflow_tb),
		// hashKey, hashValue);
		rollbackData.put(RemittanceType.借.redisKey(refund_cashflow_tb), hashKey, hashValue);
	}

	private Object refund_cashflow_get(REFUND_CASHFLOW_TBS refund_cashflow_tb, String hashKey) {
		return redistemp.opsForHash().get(RemittanceType.借.redisKey(refund_cashflow_tb), hashKey);
	}

	private void refund_cashflow_delete(REFUND_CASHFLOW_TBS refund_cashflow_tb, String hashKey) {
		redistemp.opsForHash().delete(RemittanceType.借.redisKey(refund_cashflow_tb), hashKey);
	}

	// -----------------------json------------------
	protected StringBuilder jsonInit() {
		return new StringBuilder("{");
	}

	protected void jsonAppend(StringBuilder json_builder, String columnName, String columnValue, boolean end) {
		json_builder.append("\"").append(columnName).append("\"").append(":").append("\"").append(columnValue).append("\"");
		if (!end)
			json_builder.append(",");
		else
			json_builder.append("}");
	}

	protected int getInteger(String s) {
		return (s == null || s.trim().length() == 0) ? -1 : Integer.parseInt(s);
	}

	protected BigDecimal getBigDecimal(String s) {
		return (s == null || s.trim().length() == 0) ? null : new BigDecimal(s);
	}
}
