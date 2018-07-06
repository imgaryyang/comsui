package com.suidifu.owlman.microservice.spec;

/**
 * GlobalRuntimeException Error Message
 *
 * @author louguanyang
 */
public class GlobalErrorMsgSpec {

    public class VoucherErrorMsgSpec {
        public static final String VOUCHER_ERROR_OF_CONTRACT_MSG = "contractUniqueId错误";

        public static final String VOUCHER_ERROR_OF_FINANCIAL_CONTRACT_MSG = "贷款合同不在信托计划内";

        public static final String VOUCHER_ERROR_OF_REPAYMENT_PLAN_NO_MSG = "repaymentPlanNo错误";

        public static final String VOUCHER_ERROR_OF_REPURCHASE_DOC_NOT_EXIST_MSG = "回购单不存在";

        public static final String VOUCHER_ERROR_OF_VOUCHER_TYPE_MSG = "不支持的凭证类型";

        public static final String VOUCHER_ERROR_OF_RECEIVABLE_AMOUNT_MSG = "金额大于未还金额";

        public static final String VOUCHER_ERROR_OF_NO_DETAIL_AMOUNT_MSG = "明细未传";

        public static final String VOUCHER_ERROR_OF_REPURCHASE_AMOUNT_MSG = "金额大于回购金额";

        public static final String VOUCHER_ERROR_OF_GUARANTEE_AMOUNT_MSG = "金额大于担保金额";

        public static final String VOUCHER_ERROR_OF_PAYER_MSG = "付款人类型错误，0:贷款人,1:商户垫付";

        public static final String VOUCHER_ERROR_OF_DETAIL_FORMAT = "凭证明细金额格式错误";

        public static final String VOUCHER_DETAIL_AND_AMOUNT_NOT_MATCH = "凭证明细金额与凭证金额不匹配";

        public static final String VOUCHER_DETAIL_TRANSACTION_TIME_CHECK = "凭证明细设定还款时间区间校验错误，应大于合同生效日小于流水发生时间，与流水发生时间间隔天数不大于";

        public static final String VOUCHER_EXIST_PAYMENTING_ASSETSET_MSG = "明细中包含支付中的还款计划";

        public static final String VOUCHER_ERROR_OF_REPAY_SCHEDULE_NO_MSG = "repayScheduleNo错误";
    }

    public class PrepaymentErrorMsgSpec {
        public static final String CONTRACT_NOT_EXIST = "贷款合同不存在";

        public static final String NO_AVAILABLE_ASSET_SET = "不存在可提前还款的还款计划";

        public static final String PREPAYMENT_ASSETSET_EXSITED = "存在未执行的提前还款";

        public static final String EXPIRE_UNCLEAR_ASSETSET_EXISTED = "存在到期未偿还款计划";

        public static final String INVALID_PARAMS = "无效参数";

        public static final String WRONG_PREPAYMENT_DATE = "提前还款日期错误";

        public static final String PREPAYMENT_AMOUNT_INVALID = "提前还款金额错误";

        public static final String ACTIVE_DEDUCT_APPLICATION_EXISTED = "存在正在处理中的还款计划";

        public static final String ADVANCE_TRANSFER_EXISTED = "存在提前划拨的还款计划";

        public static final String WRONG_DEDUCTION_STATUS = "处理中的还款计划扣款状态与请求的状态不匹配";

        public static final String EXIST_PAYING_ASSETSET = "申请中包含支付中的还款计划";

    }

    public class ModifyAssetErrorMsgSpec {
        public static final String FINANCIAL_CONTRACT_NOT_EXIST = "信托合同不存在";

        public static final String FAIL_TO_MODIFY = "当前贷款合同无法变更";

        public static final String EXSIT_PROCESSING_OR_SUCCESS_REPAYMENT_PLAN = "存在当日扣款成功或处理中的还款计划";

        public static final String EXSIT_PAYING_REPAYMENT_PLAN = "处于 支付中 的还款计划不允许变更";

        public static final String WRONG_ASSET_RECYCLE_DATE = "计划还款日期排序错误，需按计划还款日期递增";

        public static final String INVALID_PRINCIPAL_AMOUNT = "无效的计划本金总额";

        public static final String ASSET_RECYCLE_DATE_TOO_EARLY = "计划还款日期不能早于贷款合同开始日期";

        public static final String REPEATED_SUBMIT = "与原计划一致，不予变更";

        public static final String REQUEST_FREQUENT = "单个贷款合同变更还款计划请求过于频繁，请降低频率后重试";

        public static final String ONLY_ONE_ASSETSET_WHEN_PRE_CLEAR = "提前结清时未偿还款计划只能有一条";

        public static final String MODIFY_REASON_NOT_BELONG_TO_COMMON = "变更原因不属于非随心还项目";

        public static final String ASSET_RECYCLE_DATE_TOO_LATE = "计划还款日期不能晚于贷款合同结束日108天";

        public static final String EXIST_INTERTEMPORAL = "存在跨期还款";

        public static final String REPEAT_OUTER_REPAYMENT_PLAN_NO_MSG = "商户还款计划编号重复";

    }

    public class RepaymentOrderErrorMsgSpec {
        public static final String ERROR_OF_CONTRACT_MSG = "合同contractUniqueId或contractNo错误";

        public static final String ERROR_OF_CREATE_TIME_MSG = "订单创建时间不能为空";

        public static final String ERROR_OF_FINANCIAL_CONTRACT_MSG = "贷款合同不在信托计划内";

        public static final String ERROR_OF_PLAN_DATE_MSG = "设定还款时间不能为空";

        public static final String ERROR_OF_PLAN_DATE_CHECK_MSG = "设定还款时间范围错误";

        public static final String ERROR_OF_REPAYMENT_WAY_CODE_MSG = "还款方式错误";

        public static final String ERROR_OF_REPAYMENT_WAY_GROUP_MSG = "还款方式冲突";

        public static final String ERROR_OF_CUSTOMER_SOURCE_IN_MULTI_CONTRACT = "多个合同的贷款人不一致";

        public static final String ERROR_OF_NO_REPURCHASE_DOC = "回购单不存在";

        public static final String ERROR_OF_WRONG_REPURCHASE_DOC = "回购单号与系统单号不一致";

        public static final String ERROR_OF_ERROR_REPURCHASE_DOC = "回购单号不处于可提交状态";

        public static final String ERROR_OF_REPAYMENT_PLAN_NO_MSG = "还款计划编号错误";

        public static final String ERROR_OF_REPAY_SCHEDULE_NO_MSG = "商户还款编号错误";

        public static final String ERROR_OF_MER_ID_MSG = "商户号为空";


        public static final String ERROR_OF_REPAYMENT_PLAN_CONTRACT_MSG = "还款计划与合同不一致";

        public static final String ERROR_OF_REPAYMENT_PLAN_STATE_MSG = "还款计划不处于可提交状态";

        public static final String ERROR_OF_REPAYMENT_PLAN_IN_DEDUCT_MSG = "还款计划处于扣款中";

        public static final String ERROR_OF_REPAYMENT_ORDER_DETAIL_AMOUNT_MSG = "明细总金额格式有误";
        public static final String ERROR_OF_FEE_AMOUNT_FORMAT_MSG = "费用金额格式有误";
        public static final String ERROR_OF_REPAYMENT_ORDER_DETAIL_AMOUNT_AND_FEE_AMOUNT_MSG = "明细总金额与明细金额累加不一致";
        public static final String ERROR_OF_REPAYMENT_ORDER_TOTAL_AMOUNT_CHECK_MSG = "总金额校验错误，还款金额应小于等于剩余应还金额";
        public static final String ERROR_OF_REPAYMENT_ORDER_DETAIL_AMOUNT_CHECK_MSG = "明细金额校验错误,还款金额应小于等于剩余应还金额";

        public static final String ERROR_OF_FEE_AMOUNT_MSG = "费用明细有误";

        public static final String ERROR_OF_FEE_TYPE_AMOUNT_MSG = "费用金额类型有误";

        public static final String ERROR_OF_REPAYMENT_WAY_MSG = "该还款方式暂未开放";

        public static final String ERROR_OF_VOUCHER_TYPE_MSG = "不支持的凭证类型";

        public static final String ERROR_OF_RECEIVABLE_AMOUNT_MSG = "金额大于未还金额";

        public static final String ERROR_OF_NO_DETAIL_AMOUNT_MSG = "明细未传";

        public static final String ERROR_OF_REPURCHASE_AMOUNT_MSG = "金额大于回购金额";

        public static final String ERROR_OF_GUARANTEE_AMOUNT_MSG = "金额大于担保金额";

        public static final String ERROR_OF_PAYER_MSG = "付款人类型错误，0:贷款人,1:商户垫付";

        public static final String ERROR_OF_DETAIL_FORMAT = "凭证明细金额格式错误";

        public static final String DETAIL_AND_AMOUNT_NOT_MATCH = "凭证明细金额与凭证金额不匹配";

        public static final String DETAIL_TRANSACTION_TIME_CHECK = "凭证明细设定还款时间校验错误";

        public static final String ERROR_OF_NOT_SAME_CONTRACT_MSG = "还款订单包含多个贷款合同";
    }
}
