package com.suidifu.jpmorgan.entity.unionpay;

/**
 * 银联常量表
 */
public class UnionPayConstants {
	
	public class GZUnionPayBankCode {
		/**
		 * 中国邮政储蓄银行
		 */
		public static final String BANK_CODE_PSBC = "403";
	}

	/**
	 * 业务代码常量
	 */
	public class GZUnionPayBusinessCode {
		/**
		 * 其他费用
		 */
		public static final String OTHER = "14900";
	}
	
	/**
	 * 响应码常量
	 *
	 */
	public class GZUnionPayResponseCode {
		/**
		 * 处理完成
		 */
		public static final String PROCESSED = "0000";
		
		/**
		 * 无法查询到该交易
		 */
		public static final String QUERY_REQ_NO_NOT_EXIST = "1002";
		
		/**
		 * 处理中
		 */
		public static final String PROCESSING = "2000";
		
		/**
		 * 跨行处理中
		 */
		public static final String DIFFERENT_BANK_PROCESSING = "2008";
		
		/**
		 * 系统繁忙
		 */
		public static final String SYSTEM_BUSY = "3028";
		
		/**
		 * 余额不足
		 */
		public static final String BALANCE_IS_NOT_ENOUGH = "3008";
	}
	
}
