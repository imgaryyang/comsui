package com.suidifu.datasync.canal.rowprocesser.redis.tableprocessor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.suidifu.datasync.canal.rowprocesser.redis.domain.CashFlowResult;
import com.suidifu.datasync.canal.rowprocesser.redis.domain.ExecLog;
import com.suidifu.datasync.canal.rowprocesser.redis.domain.ExecLogResult;
import com.suidifu.datasync.canal.rowprocesser.redis.domain.Result;

/**
 * 系统流水表-代付单表监控
 * 
 * @author lisf
 *
 */
@Component("t_remittance_plan_exec_log")
public class RemittanceExecLog extends AbstractTableProcessor {
	private static final Logger LOG = LoggerFactory.getLogger(RemittanceExecLog.class);

	@Override
	public void onInsert(List<Column> afterColumnsList) {
		onChange(afterColumnsList, Boolean.TRUE, Boolean.FALSE);
	}

	@Override
	public void onUpdate(List<Column> beforeColumnsList, List<Column> afterColumnsList) {
		onChange(afterColumnsList, Boolean.FALSE, Boolean.TRUE);
	}

	@Override
	public void onDelete(List<Column> beforeColumnsList) {

	}

	private void onChange(List<Column> afterColumnsList, boolean insert, boolean update) {
		ExecLog execlog = new ExecLog();
		ExecLogResult exec_result = new ExecLogResult();
		boolean flush_execlog = Boolean.FALSE;
		boolean flush_tradeid_execid = Boolean.FALSE;
		boolean flush_execid_result = Boolean.FALSE;
		String columnName = null;
		String columnValue = null;
		for (Column column : afterColumnsList) {
			columnName = column.getName();
			columnValue = column.getValue();
			if ("exec_req_no".equalsIgnoreCase(columnName)) {
				execlog.setExecid(columnValue);
				exec_result.setSystemBillIdentity(columnValue);
				if (column.getUpdated()) {
					flush_execlog = Boolean.TRUE;
					flush_tradeid_execid = Boolean.TRUE;
					flush_execid_result = Boolean.TRUE;
				}
			} else if ("exec_rsp_no".equalsIgnoreCase(columnName)) {
				execlog.setTradeid(columnValue);
				exec_result.setTradeId(columnValue);
				if (column.getUpdated()) {
					flush_execlog = Boolean.TRUE;
					flush_tradeid_execid = Boolean.TRUE;
					flush_execid_result = Boolean.TRUE;
				}
			} else if ("execution_status".equalsIgnoreCase(columnName)) {
				execlog.setExecutionStatus(getInteger(columnValue));
				if (column.getUpdated()) {
					flush_execlog = Boolean.TRUE;
					flush_execid_result = Boolean.TRUE;
				}
			} else if ("reverse_status".equalsIgnoreCase(columnName)) {
				execlog.setReverseStatus(getInteger(columnValue));
				if (column.getUpdated()) {
					flush_execlog = Boolean.TRUE;
					flush_execid_result = Boolean.TRUE;
				}
			} else if ("financial_contract_uuid".equalsIgnoreCase(columnName)) {
				exec_result.setFinancialContractUuid(columnValue);
				if (column.getUpdated())
					flush_execid_result = Boolean.TRUE;
			} else if ("payment_channel_uuid".equalsIgnoreCase(columnName)) {
				exec_result.setPaymentChannelUuid(columnValue);
				if (column.getUpdated())
					flush_execid_result = Boolean.TRUE;
			} else if ("last_modified_time".equalsIgnoreCase(columnName)) {
				exec_result.setSystemBillOccurDate(columnValue);
				if (column.getUpdated())
					flush_execid_result = Boolean.TRUE;
			} else if ("planned_amount".equalsIgnoreCase(columnName)) {
				exec_result.setSystemBillPlanAmount(getBigDecimal(columnValue));
				if (column.getUpdated())
					flush_execid_result = Boolean.TRUE;
			}
		}
		if (!flush_execlog && !flush_tradeid_execid && !flush_execid_result)
			return;
		if (flush_execlog)
			flush_execlog(execlog);
		if (flush_tradeid_execid)
			flush_tradeid_execid(execlog.getTradeid(), execlog.getExecid());
		if (flush_execid_result)
			flush_execid_result(exec_result, execlog, insert, update);
	}

	/**
	 * 刷新execlog
	 * 
	 * @param execlog
	 */
	private void flush_execlog(ExecLog execlog) {
		if (StringUtils.isEmpty(execlog.getExecid()))
			return;
		execlog_put(execlog);
		LOG.info("flush_execlog：" + execlog.getExecid());
	}

	/**
	 * 刷新tradeid_execid
	 * 
	 * @param tradeid
	 * @param execid
	 */
	private void flush_tradeid_execid(String tradeid, String execid) {
		if (StringUtils.isEmpty(tradeid) || StringUtils.isEmpty(execid))
			return;
		tradeid_execid_put(tradeid, execid);
		LOG.info("flush_tradeid_execid：" + tradeid + "，" + execid);
	}

	/**
	 * 刷新execid_result
	 * 
	 * @param exec_result
	 * @param insert
	 * @param update
	 */
	private void flush_execid_result(ExecLogResult exec_result, ExecLog execlog, boolean insert, boolean update) {
		if (StringUtils.isEmpty(exec_result.getSystemBillIdentity()))
			return;
		Object cashflowid = exec_tradeid_cashflow_get(execlog.getTradeid());
		boolean cashflow_exist_tradeid = !StringUtils.isEmpty(cashflowid);
		exec_result.setResultType(execlog.createResultType(insert ? Boolean.FALSE : cashflow_exist_tradeid));
		if (insert) {
			execid_result_put(exec_result.getSystemBillIdentity(), exec_result);
		} else if (update) {
			Result result = execid_result_get(exec_result.getSystemBillIdentity());
			if (result == null)
				result = new ExecLogResult();
			if (!cashflow_exist_tradeid) {
				ExecLogResult._clone(exec_result, result);
				execid_result_put(result.getSystemBillIdentity(), result);
				return;
			}
			Result cashflow_result = execid_result_get(cashflowid.toString());
			if (cashflow_result != null) {
				CashFlowResult._clone(cashflow_result, result);
				execid_result_delete(cashflowid.toString());
			}
			ExecLogResult._clone(exec_result, result);
			execid_result_put(result.getSystemBillIdentity(), result);
		}
		LOG.info("flush_execid_result1：" + exec_result.getSystemBillIdentity() + "，" + exec_result.getResultCode());
	}
}
