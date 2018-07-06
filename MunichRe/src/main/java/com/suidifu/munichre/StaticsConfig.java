package com.suidifu.munichre;

public class StaticsConfig {

	public enum TransactionCommandExecutionStatus {
		
		/** 已创建 */
		CREATE(0,"enum.deduct-execution-status.create"),
		
		/** 处理中 */
		PROCESSING(1,"enum.deduct-execution-status.processing"),

		/** 成功 */
		SUCCESS(2,"enum.deduct-execution-status.success"),

		/** 失败 */
		FAIL(3,"enum.deduct-execution-status.fail"),
		
		/** 异常 */
		ABNORMAL(4,"enum.deduct-execution-status.abnormal"),
		
		/** 撤销 */
		ABANDON(5,"enum.deduct-execution-status.abandon");
		
		private int code = -1;
		private String key;

		TransactionCommandExecutionStatus(int code, String key) {
			this.code = code;
			this.key = key;
		}

		public int getCode() {
			return code;
		}

		public String getKey() {
			return key;
		}
	}
	
//	ContractState
	
	public enum ContractState {
		
		/** 0:放款中 **/
		PRE_PROCESS(0,"enum.contract-enum.pre-process"),
		
		/** 1:未生效 **/
		INEFFECTIVE(1,"enum.contract-enum.ineffective"),
		
		/** 2:已生效 **/
		EFFECTIVE(2,"enum.contract-enum.effective"),
		
		/** 3:异常中止 **/
		INVALIDATE(3,"enum.contract-enum.invalidate"),
		
		/** 4:回购中 */
		REPURCHASING(4,"enum.repurchase-status.repurchasing"),
		
		/** 5:已回购 */
		REPURCHASED(5,"enum.repurchase-status.repurchased"),
		
		/** 6:违约 */
		DEFAULT(6,"enum.repurchase-status.default");
		
		private int code = -1;
		private String key;
		
		ContractState(int code, String key) {
			this.code = code;
			this.key = key;
		}
		
		public int getCode() {
			return code;
		}

		public String getKey() {
			return key;
		}
	}

}
