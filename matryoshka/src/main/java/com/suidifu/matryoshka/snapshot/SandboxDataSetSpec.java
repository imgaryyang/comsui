package com.suidifu.matryoshka.snapshot;

/**
 * 沙盒数据Key
 * @author louguanyang on 2017/5/11.
 */
public class SandboxDataSetSpec {
    public final static String FINANCIAL_CONTRACT_UUID = "financialContractUuid";
    public final static String CONTRACT_UUID = "contractUuid";
    public final static String REPAYMENT_PLAN_NO_LIST = "repaymentPlanNoList";
    public final static String ORIGINAL_REPAYMENT_PLAN_NO_LIST = "originalRepaymentPlanNoList";
    public final static String CONFING_UUID = "configUuid";
    public final static String EXTRA_ITEM = "extraItem";

    public class ExtraItem{
        public final static String REASON_CODE = "reasonCode";
        public final static String PRINCIPAL = "principal";
        public final static String INTEREST = "interest";
        public final static String REPAY_CHARGE = "repayCharge";
    }
}
