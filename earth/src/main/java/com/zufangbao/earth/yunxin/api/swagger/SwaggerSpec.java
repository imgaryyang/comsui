package com.zufangbao.earth.yunxin.api.swagger;

/**
 * Swagger 配置类
 * @author louguanyang on 2017/5/19.
 */
public class SwaggerSpec {
    public static final String PARAM_TYPE_PATH = "path";
    public static final String PARAM_TYPE_QUERY = "query";

    public static final String DATA_TYPE_STRING = "String";
    public static final String DATA_TYPE_INTEGER = "Integer";
    public static final String DATA_TYPE_Date = "Date";

    public static final String CHANNEL_CODE = "channelCode";
    public static final String CHANNEL_CODE_VALUE = "商户代码";
    public static final String DEFAULT_CHANNEL_CODE = "zhongan";

    public static final String PRODUCT_CODE = "productCode";
    public static final String PRODUCT_CODE_VALUE = "信托产品代码";
    public static final String HA0100 = "HA0100";

    public static final String SERVICE_CODE = "serviceCode";
    public static final String SERVICE_CODE_VALUE = "服务代码";

    public static final String SXH_DEFERRED_PAYMENT = "SXH-DeferredPayment";
    private static final String SXH_CHANGE_REPAYMENT_DATE = "SXH-ChangeRepaymentDate";
    private static final String PREPAYMENT = "Prepayment";
    private static final String SXH_PREPAYMENT = "SXH-" + PREPAYMENT;
    private static final String SXH_CANCEL = "SXH-Cancel";
    private static final String REFUNDS = "Refunds";
    public static final String SXH_ALLOWABLE_VALUES = SXH_DEFERRED_PAYMENT + "," + SXH_CHANGE_REPAYMENT_DATE + "," + SXH_PREPAYMENT + "," + SXH_CANCEL + "," +
            PREPAYMENT + "," + REFUNDS;

    public static final String REQUEST_NO = "requestNo";
    public static final String REQUEST_NO_VALUE = "请求编号";
    public static final String UNIQUE_ID = "uniqueId";
    public static final String UNIQUE_ID_VALUE = "贷款合同唯一编号";
    public static final String CONTRACT_NO = "contractNo";
    public static final String CONTRACT_NO_VALUE = "贷款合同编号";
    public static final String REQUEST_DATA = "requestData";
    public static final String REQUEST_DATA_VALUE = "具体变更内容";
    public static final String MODIFY_REPAYMENT_PLAN_TYPE = "type";
    public static final String MODIFY_REPAYMENT_PLAN_TYPE_VALUE = "还款计划变更类型";
    public static final String FINANCIAL_CONTRACT_NO = "financialContractNo";
    public static final String FINANCIAL_CONTRACT_NO_VALUE = "信托产品代码";

    public static final String TRADE_TIME = "tradeTime";
    public static final String TRADE_TIME_VALUE = "交易时间";
}
