package com.suidifu.datasync.canal.rowprocesser.redis.tableprocessor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.suidifu.datasync.canal.StaticsConfig.ResultType;
import com.suidifu.datasync.canal.StaticsConfig.ReverseType;
import com.suidifu.datasync.canal.rowprocesser.redis.domain.RefundResult;
import com.suidifu.datasync.canal.rowprocesser.redis.domain.Result;

/**
 * 系统流水表-代付款撤销单表监控
 * 
 * @author lisf
 *
 */
@Component("t_remittance_refund_bill")
public class RemittanceRefundBill extends AbstractTableProcessor {
	private static final Logger LOG = LoggerFactory.getLogger(RemittanceRefundBill.class);

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
		RefundResult refund_result = new RefundResult();
		boolean flush_refundbill = Boolean.FALSE;
		boolean flush_tradeid_refundid = Boolean.FALSE;
		boolean flush_refundid_result = Boolean.FALSE;
		for (Column column : afterColumnsList) {
			String columnName = column.getName();
			String columnValue = column.getValue();
			if ("remittance_refund_bill_uuid".equalsIgnoreCase(columnName)) {
				refund_result.setSystemBillIdentity(columnValue);
				if (column.getUpdated()) {
					flush_refundbill = Boolean.TRUE;
					flush_tradeid_refundid = Boolean.TRUE;
					flush_refundid_result = Boolean.TRUE;
				}
			}
			if ("related_exec_rsp_no".equalsIgnoreCase(columnName)) {
				refund_result.setTradeId(columnValue);
				if (column.getUpdated()) {
					flush_tradeid_refundid = Boolean.TRUE;
					flush_refundid_result = Boolean.TRUE;
				}
			}
			if ("financial_contract_uuid".equalsIgnoreCase(columnName)) {
				refund_result.setFinancialContractUuid(columnValue);
				if (column.getUpdated()) {
					flush_refundid_result = Boolean.TRUE;
				}
			}
			if ("payment_channel_uuid".equalsIgnoreCase(columnName)) {
				refund_result.setPaymentChannelUuid(columnValue);
				if (column.getUpdated()) {
					flush_refundid_result = Boolean.TRUE;
				}
			}
			if ("last_modified_time".equalsIgnoreCase(columnName)) {
				refund_result.setSystemBillOccurDate(columnValue);
				if (column.getUpdated()) {
					flush_refundid_result = Boolean.TRUE;
				}
			}
			if ("related_cash_flow_uuid".equalsIgnoreCase(columnName)) {
				refund_result.setCashFlowIdentity(columnValue);
				if (column.getUpdated()) {
					flush_refundid_result = Boolean.TRUE;
				}
			}
			if ("transaction_time".equalsIgnoreCase(columnName)) {
				refund_result.setCashFlowTransactionTime(columnValue);
				if (column.getUpdated()) {
					flush_refundid_result = Boolean.TRUE;
				}
			}
			if ("host_account_no".equalsIgnoreCase(columnName)) {
				refund_result.setHostAccountNo(columnValue);
				if (column.getUpdated()) {
					flush_refundid_result = Boolean.TRUE;
				}
			}
			if ("amount".equalsIgnoreCase(columnName)) {
				refund_result.setSystemBillPlanAmount(getBigDecimal(columnValue));
				if (column.getUpdated()) {
					flush_refundid_result = Boolean.TRUE;
				}
			}
			if ("reverse_type".equalsIgnoreCase(columnName)) {
				LOG.info("reverse_type"+getInteger(columnValue));
				if(ReverseType.REFUND.getCode()==getInteger(columnValue)){
					flush_refundbill = Boolean.FALSE;
					flush_tradeid_refundid = Boolean.FALSE;
					flush_refundid_result = Boolean.FALSE;
					break;
				}
			}
		}
		LOG.info("remittance_refund_bill_uuid"+refund_result.getSystemBillIdentity()+"flush_refundbill"+flush_refundbill+"；flush_tradeid_refundid:"+flush_tradeid_refundid+";flush_refundid_result:"+flush_refundid_result);
		if (!flush_refundbill && !flush_tradeid_refundid && !flush_refundid_result)
			return;
		if (flush_refundbill)
			flush_refundbill(refund_result.getSystemBillIdentity(), refund_result.getTradeId());
		if (flush_tradeid_refundid)
			flush_tradeid_refundid(refund_result.getTradeId(), refund_result.getSystemBillIdentity());
		if (flush_refundid_result)
			flush_refundid_result(refund_result);
	}

	/**
	 * 刷新refundbill
	 * 
	 * @param refundid
	 * @param tradeid
	 */
	private void flush_refundbill(String refundid, String tradeid) {
		if (StringUtils.isEmpty(refundid) || StringUtils.isEmpty(tradeid))
			return;
		refundbill_put(refundid, tradeid);
		LOG.info("flush_refundbill：" + refundid);
	}

	/**
	 * 刷新tradeid_refundid
	 * 
	 * @param tradeid
	 * @param refundid
	 */
	private void flush_tradeid_refundid(String tradeid, String refundid) {
		if (StringUtils.isEmpty(tradeid) || StringUtils.isEmpty(refundid))
			return;
		tradeid_refundid_put(tradeid, refundid);
		LOG.info("flush_tradeid_refundid：" + tradeid + "，" + refundid);
	}

	/**
	 * 刷新refundid_result
	 * 
	 * @param refund_result
	 */
	private void flush_refundid_result(RefundResult refund_result) {
		if (refund_cashflow_exist_tradeid(refund_result.getTradeId())) {
			refund_result.setResultType(ResultType.平账);
			Result result = refundid_result_get(refund_result.getCashFlowIdentity());
			if (result == null) {
				refundid_result_put(refund_result.getSystemBillIdentity(), refund_result);
			} else {
				RefundResult._clone(refund_result, result);
				refundid_result_put(result.getSystemBillIdentity(), result);
				refundid_result_delete(result.getCashFlowIdentity());
			}
		} else {
			refund_result.setResultType(ResultType.本端多账);
			refundid_result_put(refund_result.getSystemBillIdentity(), refund_result);
		}
		LOG.info("flush_refundid_result1：" + refund_result.getSystemBillIdentity() + "，" + refund_result.getResultCode());
	}

}
