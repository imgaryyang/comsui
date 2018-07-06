package com.suidifu.datasync.canal;

import com.suidifu.datasync.canal.rowprocesser.redis.queue.ResultQueueFactory;

/**
 * 
 * @author lisf
 *
 */
public class StaticsConfig {

	public static enum RemittanceType {
		未知(-1, null), 贷(0, "exec_cashflow"), 借(1, "refund_cashflow");
		private int type = -1;
		private String redisKey;

		RemittanceType(int type, String redisKey) {
			this.type = type;
			this.redisKey = redisKey;
		}

		public String redisKey() {
			return this.redisKey;
		}

		public String redisKey(EXEC_CASHFLOW_TBS exec_cashflow_tb) {
			return this.redisKey + ":" + exec_cashflow_tb.name();
		}

		public String redisKey(REFUND_CASHFLOW_TBS refund_cashflow_tb) {
			return this.redisKey + ":" + refund_cashflow_tb.name();
		}

		public String recordRedisKey(EXEC_CASHFLOW_TBS exec_cashflow_tb) {
			return ROLLBACK_UNACK + ":" + this.redisKey(exec_cashflow_tb);
		}

		public String recordRedisKey(REFUND_CASHFLOW_TBS refund_cashflow_tb) {
			return ROLLBACK_UNACK + ":" + this.redisKey(refund_cashflow_tb);
		}

		public String resultRedisKey() {
			switch (this.type) {
			case 0:
				return 贷.redisKey(EXEC_CASHFLOW_TBS.execid_result);
			case 1:
				return 借.redisKey(REFUND_CASHFLOW_TBS.refundid_result);
			}
			return null;
		}

		public String errorResultRedisKey() {
			return SYNC_ERROR + ":" + this.redisKey();
		}

		public String syncResultRedisKey(int queueNum) {
			return SYNC_RESULT + ":" + this.redisKey() + queueNum;
		}

		// cashflow tradeid key
		public static String getCashflowKey(int account_side, String hkey) {
			String key = null;
			switch (account_side) {
			case 0:
				key = 贷.redisKey();
				if (hkey == null)
					hkey = EXEC_CASHFLOW_TBS.tradeid_execid.name();
				break;
			case 1:
				key = 借.redisKey();
				if (hkey == null)
					hkey = REFUND_CASHFLOW_TBS.tradeid_refundid.name();
				break;
			}
			return key + ":" + hkey;
		}

		public int type() {
			return this.type;
		}

		public boolean eq(int type) {
			return this.type == type;
		}

		public String getNode(String resultkey) {
			switch (this.type) {
			case 0:
				return ResultQueueFactory.CONSISTENT_HASH_EXEC.getNode(resultkey);
			case 1:
				return ResultQueueFactory.CONSISTENT_HASH_REFUND.getNode(resultkey);
			}
			return null;
		}
	}

	// 贷REDIS - EXEC_CASHFLOW
	public static enum EXEC_CASHFLOW_TBS {
		execid_execlog, execid_result, tradeid_execid, tradeid_cashflowid, cashflowid_cashflow
	}

	// 借REDIS - EXEC_CASHFLOW
	public static enum REFUND_CASHFLOW_TBS {
		refundid_refundbill, refundid_result, tradeid_refundid, tradeid_cashflowid, cashflowid_cashflow
	}

	// 监控哪几张表
	public static enum TABLE {
		cash_flow, t_remittance_plan_exec_log, t_remittance_refund_bill
	}

	// 定时同步结果到mysql
	public static final String SYNC_RESULT = "sync_result";
	public static final String SYNC_ERROR = "sync_error";

	// 当前批次unack回滚记录表
	public static final String ROLLBACK_UNACK = "rollback_data";

	// 对账结果
	public enum ResultType {
		未知(-1), 本端多账(1), 平账(2), 对端多账(3);
		private int resultCode;

		ResultType(int resultCode) {
			this.resultCode = resultCode;
		}

		public int code() {
			return this.resultCode;
		}

		public boolean eq(int result_code) {
			return this.resultCode == result_code;
		}

		public String msg() {
			return this.name();
		}
	}

	// execution_status
	public enum ExecutionStatus {
		未知(-1), 处理中(1), 成功(2), 失败(3);
		private int statusCode;

		ExecutionStatus(int statusCode) {
			this.statusCode = statusCode;
		}

		public int code() {
			return this.statusCode;
		}

		public boolean eq(int execution_status) {
			return this.statusCode == execution_status;
		}

		public String msg() {
			return this.name();
		}
	}

	// reverse_status
	public enum ReverseStatus {
		未知(-1), 未发生(0), 未冲账(1), 已冲账(2);
		private int statusCode;

		ReverseStatus(int statusCode) {
			this.statusCode = statusCode;
		}

		public int code() {
			return this.statusCode;
		}

		public boolean eq(int reverse_status) {
			return this.statusCode == reverse_status;
		}

		public String msg() {
			return this.name();
		}
	}

	public enum Event {
		未知(-1), 删除(0), 添加或更新(1);
		private int code;

		Event(int code) {
			this.code = code;
		}

		public int code() {
			return this.code;
		}

		public boolean eq(int code) {
			return this.code == code;
		}

		public String msg() {
			return this.name();
		}
	}
	
	public enum ReverseType{
		/**
		 * 冲账,退票 
		 */
		REVERSE(0),REFUND(1);
		
		private int code;
		
		ReverseType(int code) {
			this.code = code;
		}

		public int getCode() {
			return this.code;
		}

		public boolean eq(int code) {
			return this.code == code;
		}

		public String msg() {
			return this.name();
		}
	}

	public static final String EMPTY = "";
	public static String LAST_STATUS = "last_status";
}
