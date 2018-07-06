package com.zufangbao.earth.report.constant;


/**
 * 导出操作－报表编号
 * @author zhanghongbing
 *
 */
public class ExportOpsReportCodes {
	
	/** ################################ 资产管理 ################################ **/
	/**
	 * 1:资产管理－贷款合同表导出
	 */	
	public static final String REPORT_ASSET_CONTRACT_LIST = "1";
	
	/**
	 * 2:资产管理－批次管理－还款计划表导出
	 */
	public static final String REPORT_ASSET_BATCH_LIST_REPAYMENT_PLAN = "2";
	
	/**
	 * 3:资产管理－还款计划表导出
	 */
	public static final String REPORT_ASSET_REPAYMENT_PLAN_LIST = "3";
	
	/** ################################ 还款管理 ################################ **/
	/**
	 * 4:还款管理－还款－逾期还款明细表导出
	 */
	public static final String REPORT_REPAYMENT_LIST_OVERDUE = "4";
	
	/**
	 * 5:还款管理－还款－还款管理表导出
	 */
	public static final String REPORT_REPAYMENT_LIST_MANAGEMENT = "5";

	/**
	 * 6:还款管理－担保－担保汇总表导出
	 */
	public static final String REPORT_REPAYMENT_GUARANTEE_LIST = "6";
	
	/**
	 * 7:还款管理－担保－清算汇总表导出
	 */
	public static final String REPORT_REPAYMENT_GUARANTEE_CLEAR = "7";
	
	/**
	 * 8:还款管理－回购－回购汇总表导出
	 */
	public static final String REPORT_REPAYMENT_REPURCHASE_LIST = "8";
	
	/** ################################ 资金管理 ################################ **/
	/**
	 * 9:资金管理－放款－线上代付单导出
	 */
	public static final String REPORT_CAPITAL_REMITTANCE_ONLINE_PAY_LIST = "9";
	
	/**
	 * 10:资金管理－扣款－对账单导出
	 */
	public static final String REPORT_CAPITAL_DEDUCT_AUDIT_BILL = "10";
	
	/**
	 * 11:资金管理－扣款－当日还款清单导出
	 */
	public static final String REPORT_CAPITAL_DEDUCT_TODAY_REPAYMENT_LIST = "11";
	
	/**
	 * 12:资金管理－专户账户－银行流水导出
	 */
	public static final String REPORT_CAPITAL_PROJECT_ACCOUNT_CASH_FLOW_LIST = "12";
	
	/** ################################ 报表管理 ################################ **/
	/**
	 * 13:报表管理－项目信息导出
	 */
	public static final String REPORT_PROJECT_INFO_LIST = "13";
	
	/**
	 * 14:报表管理－贷款规模导出
	 */
	public static final String REPORT_LOAN_SCALE_LIST = "14";
	
	/**
	 * 15:报表管理－应收利息导出
	 */
	public static final String REPORT_RECEIVABLE_INTEREST_LIST = "15";

	/**
	 * 16:报表管理－部分还款表导出
	 */
	public static final String REPORT_PART_REPAYMENT_LIST = "16";
	
	/**
	 * 17:报表管理－第三方支付凭证表导出
	 */
	public static final String REPORT_THIRD_PARTY_PAY_VOUCHER = "17";
	
	/**
	 * 18:报表管理－扣款订单表导出
	 */
	public static final String REPORT_DEDUCT_APPLICATION = "18";
	
	/**
	 * 19:报表管理－代收－对账结果
	 */
	public static final String REPORT_BENEFICIARY_AUDIT_RESULT = "19";
	/**
	 * 20.报表管理－还款订单导出
	 */
	public static final String REPORT_REPAYMENT_ORDER_LIST="20";
	/**
	 * 21.报表管理－还款记录导出
	 */
	public static final String REPORT_REPAYMENT_RECORD_LIST="21";

    /**
     * 22.报表管理-首逾率导出
     */
	public static final String REPORT_FIRST_OVERDUE_RATE="22";
}