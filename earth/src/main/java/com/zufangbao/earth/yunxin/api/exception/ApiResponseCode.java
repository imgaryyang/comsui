package com.zufangbao.earth.yunxin.api.exception;

/**
 * 接口响应代码
 * @author zhanghongbing
 *
 */
public class ApiResponseCode {
	/** 成功 **/
	public static final int SUCCESS = 0; 

	/**** 系统级错误代码 1xxxx ****/
	
	/** 系统错误 **/
	public static final int SYSTEM_ERROR = 10001;
	/** 接口不存在 **/
	public static final int API_NOT_FOUND = 10002;
	/** 接口已关闭 **/
	public static final int API_UNAVAILABLE = 10003;
	/** 功能代码未指定或该请求接口下不存在此功能代码！**/
	public static final int INVALID_FN_CODE = 10004;
	/** 验签失败 **/
	public static final int SIGN_VERIFY_FAIL = 10005;
	/** 缺少商户号merId或商户密钥secret **/
	public static final int SIGN_MER_CONFIG_ERROR = 10006;
	/** 系统繁忙，请稍后再试 **/
	public static final int SYSTEM_BUSY = 10007;
	/** 未找到merId所关联商户 **/
	public static final int NOT_EXIST_MER_ERROR = 10011;
	/** 解密失败 **/
	public static final int DECRYPTION_VERIFY_ERROR = 10009;
	/** 解密内容格式错误 **/
	public static final int DECRYPTION_CONTENT_FORMAT_ERROR = 10010;
	
	/**** 业务级错误代码 2xxxx ****/

	/** 无效参数 **/
	public static final int INVALID_PARAMS = 20001;
	
	/** 第三方凭证状态为success，无法插入**/
	public static final int STATUS_CANNOT_INSERT = 20000;
	
	/** 请求参数解析错误 **/
	public static final int WRONG_FORMAT = 20002;
	
	/** 合同，还款计划相关，错误代码 21xxx **/
	/** 贷款合同不存在 **/
	public static final int CONTRACT_NOT_EXIST = 21001;
	/** 请求编号重复 **/
	public static final int REPEAT_REQUEST_NO = 21002;
	/** 当前贷款合同无法变更 **/
	public static final int FAIL_TO_MODIFY = 21003;
	/** 无效的计划本金总额! **/
	public static final int INVALID_PRINCIPAL_AMOUNT = 21004;
	/** 无效的计划利息总额 **/
	public static final int INVALID_INTEREST_AMOUNT = 21005;
	/** 没有逾期还款计划 **/
	public static final int NO_OVERDUE_REPAYMENT_PLAN = 21006;
	/** 扣款金额超过剩余可扣金额 **/
	public static final int OVERFLOW_DEDUCT_AMOUNT = 21007;
	/** 逾期扣款请求不存在 **/
	public static final int OVERDUE_DEDUCT_REQUEST_NOT_FOUND = 21008;
	/** 计划还款日期排序错误，需按计划还款日期递增 **/
	public static final int WRONG_ASSET_RECYCLE_DATE = 21009;
	/** 不存在可进行提前还款的还款计划 **/
	public static final int NO_AVAILABLE_ASSET_SET = 21010;
	/** 存在未执行的提前还款 **/
	public static final int PREPAYMENT_ASSETSET_EXSITED = 21011;
	/** 提前还款总金额或本金错误 **/
	public static final int PREPAYMENT_AMOUNT_INVALID = 21012;
	/** 存在未偿还款计划 **/
	public static final int EXPIRE_UNCLEAR_ASSETSET_EXISTED = 21013;
	/** 提前还款日期错误 **/
	public static final int WRONG_PREPAYMENT_DATE = 21014;
	/** 扣款唯一编号重复 **/
	public static final int REPEAT_DEDUCT_ID = 21015;
	/** 计划还款日期不能晚于贷款合同终止日期后108天！ **/
	public static final int ASSET_RECYCLE_DATE_TOO_LATE = 21016;
	/** 权限不足，当前信托计划无法变更当日还款计划，请联系运营人员开通相关权限！ **/
	public static final int PERMISSION_DENIED = 21017;
	/** 存在当日扣款成功或处理中的还款计划！ **/
	public static final int EXSIT_PROCESSING_OR_SUCCESS_REPAYMENT_PLAN = 21018;
	/** 计划还款日期不能早于贷款合同开始日期! **/
	public static final int ASSET_RECYCLE_DATE_TOO_EARLY = 21019;
	/** 单个贷款合同变更还款计划请求过于频繁，请降低频率后重试! **/
	public static final int REQUEST_FREQUENT = 21020;
	/** 与原计划一致，不予变更! **/
	public static final int REPEATED_SUBMIT = 21021;
	/** 处理中的还款计划扣款状态与请求的状态不匹配 **/
	public static final int WRONG_DEDUCTION_STATUS = 21022;
	/**还款计划编号（或商户还款计划编号）错误**/
	public static final int  SINGLE_LOAN_CONTRACT_NO_ERROR = 21023;
	/**提前结清时未偿还款计划只能有一条**/
	public static final int  ONLY_ONE_ASSETSET_WHEN_PRE_CLEAR = 21024;
	/**变更原因不属于非随心还项目**/
	public static final int  MODIFY_REASON_NOT_BELONG_TO_COMMON = 21025;

	/**存在跨期还款**/
	public static final int EXIST_INTERTEMPORAL = 21028;

	/**变更还款信息**/
	/**银行代码错误**/
	public static final int  NO_BANK_CODE = 22010;
	/**账号错误**/
	public static final int  NO_BANK_ACCOUNT = 22011;

	/**** 通道相关错误代码 22100 - 22150 ****/
	/** 支付通道不存在  **/
	public static final int CHANNEL_NOT_FOUND = 22100;
	
	/**** 放款相关错误代码 22151 - 22200 ****/
	/** 该贷款合同下存在处理中或已成功的放款请求  **/
	public static final int HAS_EXIST_PROCESSING_OR_SUCCESSED_REMITTANCE = 22200;
	
	/**扣款接口相关 22201 - 22300**/
	/**扣款明细金额累加与扣款总金额不一致**/
	public static final int  REPAYMENT_DETAILS_AMOUNT_ERROR = 22201;
	/**还款计划总额与扣款总额不等**/
	public static final int  REPAYMENT_TOTAL_AMOUNT_NOT_EQUALS_DEDUCT_AMOUNT = 22202;
	/**不存在该有效还款计划或者还款计划不在贷款合同内**/
	public static final int  REPAYMENT_CODE_NOT_IN_CONTRACT = 22203;
	/**还款计划条数有误**/
	public static final int  REPAYMENT_PLAN_NUMBER_ERROR =  22204;
	/**扣款金额错误**/
	public static final int  DEDUCT_AMOUNT_ERROR =  22205;
	
	/**不存在相应的扣款请求**/
	public static final int  NOT_DEDUCT_ID =  22206;
	/**扣款类型不符合当前还款计划还款状态**/
	public static final int  API_CALLED_TIME_NOT_MEET_PLAN_RECYCLE_TIME = 22207;
	/**系统查询错误**/
	public static final int  QUERY_SYSTEM_ERROR = 22208;
	/**还款计划已经存在处理中和成功中的扣款申请**/
	public static final int  HAS_EXIST_DEDUCT_APPLICATION = 22209;
	/**还款计划已经成功**/
	public static final int  REPAYMENT_PLAN_HAS_SUCCESS = 22210;
	/**扣款并发错误**/
	public static final int  DEDUCT_CONCURRENT_ERROR = 22211;
	/**逾期费用明细金额累加与逾期总金额不相等**/
	public static final int  OVERDUE_FEE_ERROR = 22212;
	/**扣款金额应小于或者等于应收金额**/
	public static final int  DEDUCT_AMOUNT_LESS_THAN_RECEIVE_AMOUNT = 22213;
	/**提前划扣上一期必须已完成*/
	public static final int  PRE_DEDUCT_BEFORE_PREIODS_MUST_OVER = 22214;
	
	/**资产包导入**/
	//格式校验
	/**合同总条数格式错误**/
	public static final int  TOTAL_NUMBER_ERROR  = 23010;
	/**合同还款本金总额格式错误**/
	public static final int  TOTAL_AMOUNT_ERROR  = 23011;
	/**贷款客户姓名格式错误！**/
	public static final int  LOAN_CUSTOMER_NAME_ERROR  = 23012;
	/**身份证号格式错误**/
	public static final int  ID_CARD_ERROR  = 23013;
	/**贷款本金总额格式错误**/
	public static final int  LOAN_TOTAL_AMOUNT_ERROR  = 23014;
	/**贷款期数格式错误**/
	public static final int  LOAN_PERIODS_ERROR  = 23015;
	/**设定生效日期格式错误**/
	public static final int  EFFECT_DATE_ERROR  = 23016;
	/**设定到期日期格式错误**/
	public static final int  EXPIRE_DATE_ERROR  = 23017;
	/**贷款利率格式错误**/
	public static final int  LOAN_RATES_ERROR  = 23018;
	/**罚息利率格式错误**/
	public static final int  PENALTY_ERROR  = 23019;
	/**未知回款方式**/
	public static final int  NO_MATCH_REPAYMENT_WAY  = 23020;
	/**还款日期格式错误**/
	public static final int  REPAYMENT_PLAN_REPAYMENT_DATE_ERROR  = 23021;
	/**还款本金格式错误**/
	public static final int  REPAYMENT_PRINCIPAL_ERROR  = 23022;
	/**还款利息格式错误**/
	public static final int  REPAYMENT_INTEREST_ERROR  = 23023;
	/**其他金额格式错误**/
	public static final int  OTHER_FEE_ERROR  = 23024;
	/**批次合同总条数错误**/
	public static final int  TOTAL_CONTRACTS_NUMBER_NOT_MATCH = 23025;
	/**批次合同本金总额错误**/
	public static final int  TOTAL_CONTRACTS_AMOUNT_NOT_MATCH = 23026;
	/**贷款合同编号重复**/
	public static final int  EXIST_LOAN_CONTRACT_NO = 23027;
	/**贷款合同唯一编号重复**/
	public static final int  EXIST_LOAN_CONTRACT_UNIQUE_ID = 23028;
	/**标的物已存在**/
	public static final int  EXIST_THE_SUBJECT_MATTER= 23029;
	/**信托合同商户未设置**/
	public static final int  NOT_SET_APP_IN_FINANCIAL_CONTRACT = 23030;
	/**信托产品代码错误**/
	public static final int  FINANCIAL_PRODUCT_CODE_ERROR = 23031;
	/**还款计划总金额错误**/
	public static final int  REPAYMENT_PLAN_TOTAL_AMOUNT_ERROR = 23032;
	/**还款计划总条数错误**/
	public static final int  REPAYMENT_PLAN_TOTAL_PERIODS_ERROR = 23033;
	/**未知银行代码**/
	public static final int  NO_MATCH_BANK = 23034;
	/**未知省份代码**/
	public static final int  NO_MATCH_PROVINCE = 23035;
	/**未知城市代码**/
	public static final int  NO_MATCH_CITY = 23036;
	/**首期还款还款日期错误**/
	public static final int  FIRST_REPAYMENT_DATE_NOT_CORRECT = 23037;
	/**贷款合同编号或者uniqId为空**/
	public static final int  LOAN_CONTRACT_NO_OR_UNIQUEID_IS_EMPTY = 23038;
	/**贷款客户编号错误**/
	public static final int  LOAN_CUSTOMER_NO_ERROR = 23039;
	/**技术服务费格式错误**/
	public static final int   TECH_MAINTENANCE_FEE_ERROR = 23040;
	/**贷款服务费格式错误**/
	public static final int  LOAN_SERVICE_FEE_ERROR = 23041;
	/**还款账户号错误**/
	public static final int  REPAYMENT_ACCOUNT_ERROR = 23042;
	/**城市与省份代码不匹配**/
	public static final int  CITY_AND_PROVINCE_CODE_DOES_NOT_MATCH = 23043;
	/**信托代码与对应放款中信托代码不匹配**/
	public static final int  TRUST_CODE_NOT_AGREE_WITH_TRUST_CODE_OF_REMITTANCE= 23044;
	/**信托代码与还款计划中信托代码不匹配**/
	public static final int  TRUST_CODE_NOT_AGREE_WITH_TRUST_CODE_OF_ASSET= 23045;
	/**信托代码与专户账户不匹配**/
	public static final int  TRUST_CODE_NOT_AGREE_WITH_ACCOUNT_NO = 23046;
	/**未查询到合同对应放款**/
	public static final int  NO_MATCH_REMITTANCE = 23047;
	/**贷款合同本金和放款成功金额不等**/
	public static final int  LOAN_TOTAL_AMOUNT_NOT_MATCH_REMITTANCE_AMOUNT = 23048;
	/**商户订单号列表不满足条件**/
	public static final int ORDERUNIQUEIDS_NOT_CONTENTMENT =23049;
	/**没有与商户订单号列表对应的还款订单**/
	public static final int REPAYMENTORDERUNIQUEIDS_ISNULL =23050;
	
	/** 修改逾期费用相关，错误代码231xx**/
	
	/** 逾期罚息计算日不能早于罚息计算日**/
	public static final int   OVER_DUE_FEE_CALC_DATE_AFTER_OVER_DUE_DATE = 23100;
	/** 还款计划未激活**/
	public static final int   REPAYMENT_PLAN_NOT_OPEN = 23101;
	/**还款计划已还清**/
	public static final int   REPAYMENT_PLAN_IS_PAID_OFF= 23102;
	/**还款计划未到期**/
	public static final int   REPAYMENT_PLAN_IS_NOT_RECEIVABLE= 23103;
	/**逾期修改金额小于相对应的逾期实收金额和支付中逾期金额之和**/
	public static final int   OVERDUE_MODIFY_AMOUNT_LESS_THAN_PAIN_IN_AMOUNT_GREATER= 23104;
	/**还款计划被锁定**/
	public static final int   REPAYMENTPLAN_LOCKED= 23105;
	/**还款计划已还款成功**/
	public static final int   REPAYMENTPLAN_SUCCESS= 23106;
	
	/** 逾期罚息计算日不能早于计划还款日**/
	public static final int   OVER_DUE_FEE_CALC_DATE_AFTER_ASSET_RECYCLE_DATE = 23107;
	
	/** 浮动费用相关。错误代码24xxx**/
	
	/** 信托合同不支持随借随还**/
	public static final int   FINANCIAL_CONTRACT_NOT_PAY_FOR_GO = 24001;
	/** 还款计划到期/逾期**/
	public static final int   REPAYMENT_PLAN_IS_RECEIVABLE = 24002;
	/** 非法还息金额**/
	public static final int   MUTABLE_FEE_AMOUNT_INVAILD = 24003;
	/** 还款计划申请提前**/
	public static final int   REPAYMENT_PLAN_CAN_BE_ROLLBACK = 24004;
	/** 审核日期格式错误**/
	public static final int   APPROVED_DATE_ERROR = 24005;

	/** 缺少文件**/
	public static final int	  FILE_NOT_FOUND = 25001;
	
	/** 查询还款清单相关。错误代码3xxx**/
	
	/** 日期范围不准确**/
	public static final int DATE_RANGE_ERROR = 30001;
	/** 查询的日期范围不能超过3个月！**/
	public static final int DATE_RANGE_NOT_ALLOWED_THAN_THREE_MONTHS = 30002;
	/** 信托合同不存在**/
	public static final int FINANCIAL_CONTRACT_NOT_EXIST = 30003;
	
	/** 商户付款凭证相关。错误代码33xxx**/
	
	/** 不支持的交易类型**/
	public static final int NO_SUCH_TRANSACTION_TYPE = 33001;
	/** 不支持的凭证类型**/
	public static final int NO_SUCH_VOUCHER_TYPE = 33002;
	/** 凭证对应流水不存在或已提交!**/
	public static final int NO_SUCH_CASH_FLOW = 33003;
	/** 凭证金额与流水金额不匹配**/
	public static final int VOUCHER_CASH_FLOW_NOT_EQUAL = 33004;
	/** 还款计划与贷款合同不匹配**/
	public static final int REPAYMENT_PLAN_CONTRACT_NOT_EQUAL = 33005;
	/** 信托计划与贷款合同不匹配**/
	public static final int CONTRACT_NOT_EQUAL_FINANCIAL_CONTRACT = 33006;
	/** 凭证对应流水不存在**/
	public static final int NO_SUCH_VOUCHER = 33007;
	/** 当前凭证无法撤销**/
	public static final int VOUCHER_CAN_NOT_CANCEL = 33008;
	/** 明细金额大于还款计划担保金额**/
	public static final int RECEIVABLE_AMOUNT_NOT_EQUAL = 33009;
	/** 明细金额大于还款计划应还金额**/
	public static final int GURANTEE_AMOUNT_NOT_EQUAL = 33010;
	/** 付款人类型错误，0:贷款人,1:商户垫付**/
	public static final int NO_SUCH_VOUCHER_PAYER = 33011;

	/** 主动付款凭证相关。错误代码34xxx**/
	/** 不支持的文件类型**/
	public static final int UNSUPPORTED_FILE_TYPE = 34001;
	/** 收款账户错误，收款账户不是贷款合同的回款账户**/
	public static final int NO_SUCH_RECEIVABLE_ACCOUNT_NO = 34002;  
	/** 主动付款凭证金额错误，明细总额与凭证金额不一致**/
	public static final int VOUCHER_AMOUNT_ERROR = 34003;  
	/** 主动付款凭证金额错误，明细金额应不大于还款计划应还未还金额**/
	public static final int VOUCHER_DETAIL_AMOUNT_TOO_LARGE = 34004;
	/** 主动付款凭证金额错误，明细未传**/
	public static final int VOUCHER_DETAIL_AMOUNT_IS_NULL = 34005;
	/** 主动付款凭证金额错误，凭证明细字段金额有误**/
	public static final int VOUCHER_DETAIL_AMOUNT_HAS_ERROR = 34006;
	/**贷款合同不在同一贷款人名下**/
	public static final int CUSTOMER_NOT_UNIQUE = 34007;

	
	/** 第三方支付凭证，错误代码36xxx**/
	public static final int REPEATE_VOUCHER_NO =36000;
	
	public static final int TRANSCATION_REQUEST_NO_IN_PROCESSING_OR_SUCCESS =36001;
	
	public static final int ONE_BATCH_REPEATE_VOUCHER_NO_OR_TRANSCATION_REQUEST_NO =36002;
	
	/** 回购，错误代码37xxx**/
	/** 批次号重复 **/
	public static final int REPEAT_BATCH_NO = 37000;
	/**该贷款合同不允许进行回购操作**/
	public static final int NOT_ALLOW_REPURCHASE = 37001;
	/**该贷款合同下有正在执行的还款计划**/
	public static final int EXIST_EXECUTING_ASSET = 37002;
	/**回购本金错误**/
	public static final int ERROR_REPURCHASE_PRINCIPAL = 37003;
	/**该回购单不存在**/
	public static final int NOT_EXIST_REPURCHASE_DOC = 37004;
	/**该回购单处于不可作废状态**/
	public static final int NOT_ALLOW_INVALID = 37005;
	/**存在支付中的还款计划**/
	public static final int REPAYMENT_PLAN_IN_PAYING = 37006;


	/** 查询,错误代码38XXX**/
	/**根据给定的合约号找不到相关的账户信息**/
	public static final int ACCOUNT_INFO_NOT_EXIST_WITH_THIS_FINANCIAL_NO = 38000;
	/**账户信息和产品代码对应的账户信息不匹配**/
	public static final int FINANCIAL_NO_CANNOT_MATCH_WITH_ACCOUNT_NO = 38001;
	/** 开始时间小于结束时间**/
	public static final int START_TIME_MUST_LESS_THAN_END_TIME = 38002;
	/**合约产品代码为空**/
	public static final int CONTRACT_INFO_BLANK = 38003;
	/**请求编号信息为空**/
	public static final int REQUEST_INFO_BLANK = 38004;
	/**帐号信息为空**/
	public static final int CAPITAL_ACCOUNT_NO_BLANK = 38005;
	/**支付网管信息为空或填写不正确**/
	public static final int PAYMENT_INSTITUTION_NAME_BLANK = 38006;
	/**开始时间格式不正确或者填写内容为空**/
	public static final int START_TIME_BLANK = 38007;
	/**结束时间格式不正确或者填写内容为空**/
	public static final int END_TIME_BLANK = 38008;
	/**请求页数填写不正确或者内容为空**/
	public static final int PAGE_INFO_BLANK = 38009;
	/**请求唯一标识不能为空**/
	public static final int REQUESTNO_ISNULL= 38012;
	/**信托产品代码不能为空**/
	public static final int FINANCIALPRODUCTCODE_ISNULL= 38013;
	/**商户订单号列表和五维订单号列表不能都为空**/
	public static final int ORDERUNIQUEIDS_ISNULL= 38014;


	/** 文件上传接口 **/

	/** 文件类型错误 **/
	public static final int FILE_UNSUPPORTED = 40001;
	/** 文件内容格式错误 **/
	public static final int FILE_FORMAT_ERROR = 40002;
	
	/** 拆单配置 **/
	/** 卡签约最大限额金额为空**/
	public static final int SIGN_UP_MAX_AMOUNT_LIMIT_IS_EMPTY = 50001;
	
	public static final int PAYMENT_CHANNEL_INFO_IS_EMPTY = 50002;
	


}

