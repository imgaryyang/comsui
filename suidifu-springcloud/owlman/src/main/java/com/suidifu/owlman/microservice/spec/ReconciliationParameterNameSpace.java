package com.suidifu.owlman.microservice.spec;

public class ReconciliationParameterNameSpace {
    public final static String PARAMS_TMP_DEPSOIT_RECOVER_PARAMS = "tmpDepositDocRecoverParams";

    public static class ReconciliationForSubrogation {
        public static String PARAMS_SOURCE_DOCUMENT_DETAIL_UUID = "sourceDocumentDetailUuid";
        public static String PARAMS_SOURCE_DOCDUMENT_UUID = "sourceDocdumentUuid";

    }

    public static class ReconciliationForRepurchase {
        public static String PARAMS_SOURCE_DOCUMENT_DETAIL_UUID = "sourceDocumentDetailUuid";
        public static String PARAMS_SOURCE_DOCDUMENT_UUID = "sourceDocdumentUuid";
//		public static String PARAMS_SOURCE_FINANCIAL_CONTRACT_UUID="financialContractUuid";

    }

    public static class ReconciliationForSettlementSheet {
        public static String PARAMS_FINANCIAL_CONTRACT_UUID = "financialContractUuid";
        public static String PARAMS_CONTRACT_UUID = "contractUuid";

        public static String PARAMS_ASSET_SET = "params_asset_set";
    }

    public static class ReconciliationForDeductApi {
        public static String PARAMS_SOURCE_DOCUMENT_DETAIL_UUID = "sourceDocumentDetailUuid";
        public static String PARAMS_SOURCE_DOCDUMENT = "sourceDocdument";
        public static String PARAMS_DEDUCT_APPLICATION = "deductApplication";
    }

    public static class ReconciliationForChargeCashIntoVirtualAccountApi {
        public static String PARAMS_CASH_FLOW_UUID = "cashFlowUuid";
        public static String PARAMS_FINANCIAL_CONTRACT = "financialContract";
        public static String PARAMS_APP_ACCOUNT = "AppAccount";
        public static String PARAMS_SOURCE_DOCDUMENT = "sourceDocdument";
        public static String PARAMS_REMARK = "remark";
        public static String PARAMS_JOURNAL_VOUCHER_UUID = "journalVoucherUuid";
    }

    public static class ReconciliationForRefundApi {
        public static String PARAMS_REMARK = "remark";
        public static String PARAMS_OLD_JOURNAL_VOUCHER_UUID = "journalVoucherUuid";
    }

    public static class ReconciliationForDeductApiRefundApi {
        public static String PARAMS_SOURCE_DOCUMENT_DETAIL_UUID = "souceDocumentDetailUuid";
    }

    public static class ReconciliationForClearingVoucher {
        public static String PARAMS_DEDUCT_PLAN = "deductPlan";
        public static String PARAMS_AUDIT_JOB = "auditJob";
        public static String PARAMS_TOTAL_RECEIVABLE_BILLS = "totalReceivableBills";
        public static String PARAMS_CASH_FLOW = "cashFlow";
        public static String PARAMS_SOURCE_DOCUMENT = "sourceDocument";
        public static String PARAMS_DEDUCT_DETTAILS = "deductDetails";

    }
}
