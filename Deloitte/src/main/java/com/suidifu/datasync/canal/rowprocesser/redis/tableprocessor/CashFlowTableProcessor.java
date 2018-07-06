package com.suidifu.datasync.canal.rowprocesser.redis.tableprocessor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.suidifu.datasync.canal.StaticsConfig.RemittanceType;
import com.suidifu.datasync.canal.StaticsConfig.ResultType;
import com.suidifu.datasync.canal.rowprocesser.redis.domain.CashFlowResult;
import com.suidifu.datasync.canal.rowprocesser.redis.domain.Result;

/**
 * 通道流水表-银行流水表监控
 * 
 * @author lisf
 *
 */
@Component("cash_flow")
public class CashFlowTableProcessor extends AbstractTableProcessor {
	private static final Logger LOG = LoggerFactory.getLogger(CashFlowTableProcessor.class);

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
		CashFlowResult cashflow_result = new CashFlowResult();
		boolean flush_tradeid_cashflowid = Boolean.FALSE;
		boolean flush_cashflow_result = Boolean.FALSE;
		String columnName = null;
		String columnValue = null;
		for (Column column : afterColumnsList) {
			columnName = column.getName();
			columnValue = column.getValue();
			if ("trade_uuid".equalsIgnoreCase(columnName)) {
				cashflow_result.setTradeId(columnValue);
				if (column.getUpdated()) {
					flush_tradeid_cashflowid = Boolean.TRUE;
					flush_cashflow_result = Boolean.TRUE;
				}
			}
			if ("cash_flow_uuid".equalsIgnoreCase(columnName)) {
				cashflow_result.setCashFlowIdentity(columnValue);
				if (column.getUpdated()) {
					flush_tradeid_cashflowid = Boolean.TRUE;
					flush_cashflow_result = Boolean.TRUE;
				}
			}
			if ("account_side".equalsIgnoreCase(columnName)) {
				cashflow_result.setCashFlowAccountSide(getInteger(columnValue));
				if (column.getUpdated()) {
					flush_cashflow_result = Boolean.TRUE;
				}
			}
			if ("host_account_no".equalsIgnoreCase(columnName)) {
				cashflow_result.setHostAccountNo(columnValue);
				if (column.getUpdated()) {
					flush_cashflow_result = Boolean.TRUE;
				}
			}
			if ("transaction_time".equalsIgnoreCase(columnName)) {
				cashflow_result.setCashFlowTransactionTime(columnValue);
				if (column.getUpdated()) {
					flush_cashflow_result = Boolean.TRUE;
				}
			}
			if ("transaction_amount".equalsIgnoreCase(columnName)) {
				cashflow_result.setCashFlowTransactionAmount(getBigDecimal(columnValue));
				if (column.getUpdated()) {
					flush_cashflow_result = Boolean.TRUE;
				}
			}
		}
		LOG.info("cash_flow_uuid :" + cashflow_result.getCashFlowIdentity()+"flush_tradeid_cashflowid :"+flush_tradeid_cashflowid + "flush_cashflow_result :"+flush_cashflow_result);
		if (!flush_tradeid_cashflowid && !flush_cashflow_result)
			return;
		if (RemittanceType.未知.eq(cashflow_result.getCashFlowAccountSide()))
			return;
		// 相同tradeId情况
		if (tradeid_cashflowid_get(cashflow_result.getCashFlowAccountSide(), cashflow_result.getTradeId()) != null) {
			cashflow_result.setResultType(ResultType.对端多账);
			cashflow_result_put(cashflow_result.getCashFlowAccountSide(), cashflow_result.getCashFlowIdentity(), cashflow_result);
			return;
		}
		if (flush_tradeid_cashflowid)
			flush_tradeid_cashflowid(cashflow_result);
		if (flush_cashflow_result)
			flush_cashflow_result(cashflow_result);
	}

	/**
	 * 刷新tradeid_cashflowid
	 * 
	 * @param cashflow_result
	 */
	private void flush_tradeid_cashflowid(CashFlowResult cashflow_result) {
		if (StringUtils.isEmpty(cashflow_result.getTradeId()) || StringUtils.isEmpty(cashflow_result.getCashFlowIdentity()))
			return;
		tradeid_cashflowid_put(cashflow_result.getCashFlowAccountSide(), cashflow_result.getTradeId(), cashflow_result.getCashFlowIdentity());
		LOG.info("flush_tradeid_cashflowid：" + cashflow_result.getTradeId() + "，" + cashflow_result.getCashFlowIdentity() + "，"
				+ cashflow_result.getCashFlowAccountSide());
	}

	/**
	 * 刷新execid_result或refundid_result
	 * 
	 * @param cashflow_result
	 */
	private void flush_cashflow_result(CashFlowResult cashflow_result) {
		Object execid_or_refundid = exec_or_refund_exist_tradeid(cashflow_result.getCashFlowAccountSide(), cashflow_result.getTradeId());
		if (execid_or_refundid == null) {
			cashflow_result.setResultType(ResultType.对端多账);
			cashflow_result_put(cashflow_result.getCashFlowAccountSide(), cashflow_result.getCashFlowIdentity(), cashflow_result);
			return;
		}
		if (RemittanceType.贷.eq(cashflow_result.getCashFlowAccountSide()))
			flush_execid_result(execid_or_refundid.toString(), cashflow_result);
		else if (RemittanceType.借.eq(cashflow_result.getCashFlowAccountSide()))
			flush_refundid_result(execid_or_refundid.toString(), cashflow_result);
	}

	/**
	 * 刷新execid_result
	 * 
	 * @param execid
	 * @param cashflow_result
	 */
	private void flush_execid_result(String execid, CashFlowResult cashflow_result) {
		if (StringUtils.isEmpty(execid))
			return;
		cashflow_result.setResultType(execlog_get(execid).createResultType(Boolean.TRUE));
		Result result = execid_result_get(execid);
		if (result == null) {
			execid_result_put(execid, cashflow_result);
		} else {
			CashFlowResult._clone(cashflow_result, result);
			execid_result_put(execid, result);
		}
		LOG.info("flush_execid_result2：" + execid + "，" + cashflow_result.getResultCode());
	}

	/**
	 * 刷新refundid_result
	 * 
	 * @param refundid
	 * @param cashflow_result
	 */
	private void flush_refundid_result(String refundid, CashFlowResult cashflow_result) {
		if (StringUtils.isEmpty(refundid))
			return;
		if (refundbill_get(refundid) == null)
			cashflow_result.setResultType(ResultType.对端多账);
		else
			cashflow_result.setResultType(ResultType.平账);
		Result result = refundid_result_get(refundid);
		if (result == null) {
			refundid_result_put(refundid, cashflow_result);
		} else {
			CashFlowResult._clone(cashflow_result, result);
			refundid_result_put(refundid, result);
		}
		LOG.info("flush_refundid_result2：" + refundid + "，" + cashflow_result.getResultCode());
	}

}
