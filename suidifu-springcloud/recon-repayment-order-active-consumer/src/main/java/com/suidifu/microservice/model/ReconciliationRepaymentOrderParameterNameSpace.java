package com.suidifu.microservice.model;

/**
 * @link com.zufangbao.wellsfargo.silverpool.cashauditing.repayment.handler.ReconciliationRepaymentOrderParameterNameSpace.ReconciliationRepaymentParams
 */
public class ReconciliationRepaymentOrderParameterNameSpace {

	public static class ReconciliationRepaymentParams
	{
		public static String PARAMS_REPAYMENT_ORDER_UUID="repaymentOrderUuid";
		public static String PARAMS_REPAYMENT_ORDER_ITEM_UUID="repaymentOrderItemUuid";
		public static String PARAMS_REPAYMENT_SOURCE_DOCUMENT_UUID = "sourceDocumentUuid";
		public static String PARAMS_REPAYMENT_SOURCE_DOCUMENT_DETAIL_UUID = "sourceDocumentDetailUuid";
		public static String PARAMS_REPAYMENT_SOURCE_DOCUMENT = "sourceDocument";
		public static String PARAMS_REPAYMENT_ORDER = "paymentOrder";
		public static String PARAMS_REPAYMENT_ORDER_TMPDEPOSIT_RECOVER_PARAM = "tmpDepositRecoverParam";
		public static String PARAMS_UNIQUE_CASH_IDENTITY = "unique_cash_identity";
	}

	public static class ReconciliationRepaymentForSubrogation {

	}

	public static class ReconciliationRepaymentForDeductApi{
		public static String PARAMS_DEDUCT_APPLICATION = "deductApplication";
	}

	public static class ReconciliationRepaymentForRecover{
		public static String PARAMS_DEDUCT_APPLICATION = "deductApplication";
		public static String PARAMS_ASSET_SET_UUID = "assetSetUuid";
	}

}
