package com.zufangbao.earth.yunxin.api.controller;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zufangbao.earth.yunxin.api.BaseApiController;
import com.zufangbao.earth.yunxin.api.constant.QueryOpsFunctionCodes;
import com.zufangbao.earth.yunxin.api.handler.*;
import com.zufangbao.earth.yunxin.api.model.PagedTradeList;
import com.zufangbao.earth.yunxin.api.model.RepaymentListDetail;
import com.zufangbao.earth.yunxin.api.model.RepurchaseDetail;
import com.zufangbao.earth.yunxin.api.model.query.*;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.exception.GlobalRuntimeException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.api.model.repayment.AssetPackageBatchQueryModel;
import com.zufangbao.sun.api.model.repayment.AssetPackageImportResultModel;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.model.api.query.QueryVoucherApiLog;
import com.zufangbao.sun.yunxin.entity.model.api.query.VoucherQueryApiModel;
import com.zufangbao.sun.yunxin.entity.model.api.query.VoucherQueryApiResponse;
import com.zufangbao.sun.yunxin.service.QueryVoucherApiLogService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.VoucherHandler;
import com.zufangbao.wellsfargo.yunxin.handler.ImportAssetPackageApiHandler;
import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zufangbao.earth.yunxin.api.constant.ApiConstant.PARAMS_FN_KEY_WITH_COMBINATORS;
import static com.zufangbao.earth.yunxin.api.constant.QueryOpsFunctionCodes.*;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.GeneralErrorCode.REPEAT_REQUEST_NO;

@Controller
@RequestMapping("/api/query")
//@Api(value="查询相关Api",description="查询接口")
public class QueryApiController extends BaseApiController{

	private static final Logger logger = LoggerFactory.getLogger(QueryApiController.class);

	@Autowired
	private RepaymentPlanApiHandler repaymentPlanAPIHandler;

	@Autowired
	private RepaymentPlanDetailsApiHandler repaymentPlanDetailsApiHandler;

	@Autowired
	private RepaymentListApitHandler repaymentListApitHandler;

	@Autowired
	private ImportAssetPackageApiHandler importAssetPackageApiHandler;

	@Autowired
	private VoucherHandler voucherHandler;

	@Autowired
	private QueryVoucherApiLogService queryVoucherApiLogService;
	
	@Autowired
	private FinancialContractService financialContractService;

	@Autowired
	AccountBalanceApiHandler accountBalanceApiHandler;

	@Autowired
	private TradeListApiHandler thirdPartyTradeListApiHandler;

	@Autowired
	private TradeListApiHandler directBankTradeListApiHandler;

	/**
	 * 查询还款计划 fn:100001
	 * uniqueId与contractNo选填，requestNo必填。否则返回code：20001
	 * queryModel 为空时，返回code：20002
	 * @param queryModel 还款计划查询模型
	 * @return
	 *
	 */
	@RequestMapping(value = "", params = {PARAMS_FN_KEY_WITH_COMBINATORS + QUERY_REPAYMENT_PLAN}, method = RequestMethod.POST)
	public @ResponseBody String queryRepaymentPlan(@ModelAttribute RepaymentPlanQueryModel queryModel, HttpServletResponse response) {
		try {
			if (queryModel == null) {
				return signErrorResult(response, ApiResponseCode.WRONG_FORMAT);
			}

			if(!queryModel.isValid()) {
				return signErrorResult(response, ApiResponseCode.INVALID_PARAMS);
			}
			List<RepaymentPlanDetail> repaymentPlanDetails = repaymentPlanAPIHandler.queryRepaymentPlanDetail(queryModel);
			RepurchaseDoc repurchaseDoc = repaymentPlanAPIHandler.queryRepurchaseDoc(queryModel);
			if(repurchaseDoc!=null){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("repaymentPlanDetails", repaymentPlanDetails);
				map.put("repurchaseDoc",new RepurchaseDetail(repurchaseDoc));
				return signSucResult(response,map);
			}
			return signSucResult(response, "repaymentPlanDetails", repaymentPlanDetails);
		} catch (Exception e) {
			e.printStackTrace();
			return signErrorResult(response, e);
		}
	}

	/**
	 * 查询还款清单接口 fn:100003
	 */

	@RequestMapping(value= "" ,params ={PARAMS_FN_KEY_WITH_COMBINATORS + QueryOpsFunctionCodes.QUERY_REPAYMENT_LIST}, method = RequestMethod.POST)
	public @ResponseBody String queryRepaymentList(@ModelAttribute RepaymentListQueryModel queryModel, HttpServletResponse response){
		try {
			if (queryModel == null) {
				return signErrorResult(response, ApiResponseCode.WRONG_FORMAT);
			}
			if(!queryModel.isVaild() ){
				return signErrorResult(response, ApiResponseCode.DATE_RANGE_ERROR);
			}
			if(!queryModel.isQueryDateBetweenThreeMonths()){
				return signErrorResult(response, ApiResponseCode.DATE_RANGE_NOT_ALLOWED_THAN_THREE_MONTHS);
			}

			List<RepaymentListDetail> repaymentListDetails= repaymentListApitHandler.queryRepaymentList(queryModel);
			return signSucResult(response, "repaymentListDetail", repaymentListDetails);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#queryOverdueDeductResult occur error [requestNo : "+ queryModel.getRequestNo() +" ]!");
			return signErrorResult(response, e);
		}

	}

	/**
	 * 批量查询还款计划接口 fn:100005
	 */
	@RequestMapping(value = "", params = {PARAMS_FN_KEY_WITH_COMBINATORS + QueryOpsFunctionCodes.QUERY_BATCH_REPAYMENT_PLAN}, method = RequestMethod.POST)
	public @ResponseBody String queryBatchRepaymentPlans(@ModelAttribute RepaymentPlanBatchQueryModel queryModel, HttpServletResponse response) {
		try {
			long start = System.currentTimeMillis();
			if(!queryModel.isValid()){
				return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, queryModel.getCheckFailedMsg());
			}
			List<Map<String, Object>> repaymentPlanDatas = repaymentPlanAPIHandler.queryRepaymentPlanDetail(queryModel);
			long end = System.currentTimeMillis();
			logger.info("#queryBatchRepaymentPlans success, contracts size : " + repaymentPlanDatas.size() + ". used ["+(end-start)+"]ms");
			return signSucResult(response, "repaymentPlanDatas", repaymentPlanDatas);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#queryBatchRepaymentPlans occur error [requestNo : "+ queryModel.getRequestNo() +" ]!");
			return signErrorResult(response, e);
		}
	}

	/**
	 * 资产包批量查询接口 fn:100007
	 */
	@RequestMapping(value = "", params = {PARAMS_FN_KEY_WITH_COMBINATORS + QueryOpsFunctionCodes.QUERY_BATCH_ASSET_PACKAGE}, method = RequestMethod.POST)
	public @ResponseBody String queryBatchAssetPackages(@ModelAttribute AssetPackageBatchQueryModel queryModel, HttpServletResponse response) {
		try {
			long start = System.currentTimeMillis();

			if(!queryModel.isValid()){
				return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, queryModel.getCheckFailedMsg());
			}

			List<AssetPackageImportResultModel> assetPackageList = importAssetPackageApiHandler.queryAssetPackageImportResult(queryModel);

			long end = System.currentTimeMillis();

			logger.info("#queryBatchAssetPackages success,execute "+(end-start)+"[ms]");

			Map<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("assetPackageList",assetPackageList);
			resultMap.put("productCode",queryModel.getProductCode());
			return signSucResult(response,resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#queryBatchAssetPackages occur error [requestNo : "+ queryModel.getRequestNo() +"]!");
			return signErrorResult(response, e);
		}
	}

//    @ApiOperation(value = "查询凭证", notes = "查询凭证核销状态")
//    @ApiResponse(code = 200, message = "success", response = String.class)
    @RequestMapping(value = "", params = {PARAMS_FN_KEY_WITH_COMBINATORS + QUERY_VOUCHER}, method = RequestMethod.POST)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "fn", value = "请求编号", required = true, paramType = "pa", dataType = "Long", defaultValue = QUERY_VOUCHER)
//    })
    public @ResponseBody String queryVoucher(@ModelAttribute VoucherQueryApiModel queryModel, HttpServletRequest request, HttpServletResponse response) {
        try {
            logger.info("开始凭证查询接口，时间:" + DateUtils.getCurrentTimeMillis() + ", IP:" + IpUtil.getIpAddress(request) + "请求参数:" + queryModel.toString());
            if (queryModel.paramHasError()) {
                return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, queryModel.getErrorMsg());
            }
            //新增加产品代码和凭证来源编号的关联关系校验
          String financialContractUuid  =   checkRequestNoAndSaveLog(queryModel.getRequestNo(), queryModel.getBatchRequestNo(), IpUtil.getIpAddress(request),queryModel.getFinancialProductCode());
            VoucherQueryApiResponse result = voucherHandler.queryVoucher(queryModel, financialContractUuid);
            return signSucResult(response, "voucher", result);
        } catch (GlobalRuntimeException e) {
            ApiException exception = new ApiException(e.getCode());
            logger.error("#queryVoucher occur error [requestNo : " + queryModel.getRequestNo() + "]!");
            return signErrorResult(response, exception);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("#queryVoucher occur error [requestNo : " + queryModel.getRequestNo() + "]!");
            return signErrorResult(response, e);
        }
    }

    private String checkRequestNoAndSaveLog(String requestNo,String requestBody,String ip,String financialProductCode) {
		boolean exist_query = queryVoucherApiLogService.is_exist_query(requestNo);
		FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(financialProductCode);
		
		if (exist_query) {
		    throw new GlobalRuntimeException(REPEAT_REQUEST_NO);
		}
		QueryVoucherApiLog log = new QueryVoucherApiLog(requestNo, requestBody, ip);
	    queryVoucherApiLogService.save(log);
	    
	    return null==financialContract?StringUtils.EMPTY:financialContract.getFinancialContractUuid();

	}

    /**
	 * 还款详情查询接口 fn:100009
	 * uniqueId与contractNo选填，requestNo必填。否则返回code：20001
	 * queryModel 为空时，返回code：20002
	 * @param queryModel 还款详情查询模型
	 * @return
	 *
	 */
	@RequestMapping(value = "", params = {PARAMS_FN_KEY_WITH_COMBINATORS + QUERY_REPAYMENT_PLAN_DETAILS}, method = RequestMethod.POST)
	public @ResponseBody String queryRepaymentPlanDetails(@ModelAttribute RepaymentPlanDetailsQueryModel queryModel, HttpServletRequest request, HttpServletResponse response) {
		try {
			Date queryTime = new Date();
			if (queryModel == null) {
				return signErrorResult(response, ApiResponseCode.WRONG_FORMAT);
			}
			if(queryModel.hasError()) {
				return signErrorResult(response, ApiResponseCode.INVALID_PARAMS);
			}
			RepaymentPlanDetailsResultModel repaymentPlanDetails = repaymentPlanDetailsApiHandler.queryRepaymentPlanDetails(queryModel, queryTime);
			return signSucResult(response, "repaymentPlanDetails", repaymentPlanDetails, SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty);
		} catch (Exception e) {
			e.printStackTrace();
			return signErrorResult(response, e);
		}
	}

	/**
	 * 专户实时余额查询接口 fn:100010
	 *
	 */
	@RequestMapping(value = "", params = {PARAMS_FN_KEY_WITH_COMBINATORS + QueryOpsFunctionCodes.QUERY_ACCOUNT_BALANCE}, method = RequestMethod.POST)
	public @ResponseBody String queryAccountBalance(@ModelAttribute AccontBalanceQueryModel queryModel, HttpServletRequest request, HttpServletResponse response) {
		try {
			if (queryModel == null) {
				return signErrorResult(response, ApiResponseCode.WRONG_FORMAT);
			}
			if (queryModel.hasError()) {
				return signErrorResult(response, ApiResponseCode.INVALID_PARAMS);
			}

			AccontBalanceResultModel accontBalanceResult = accountBalanceApiHandler.queryAccountBalance(queryModel);
			return signSucResult(response, "accontBalance", accontBalanceResult, SerializerFeature.WriteNullStringAsEmpty);
		} catch (Exception e) {
			logger.warn("occur error when query account balance..accountNo:" + queryModel.getAccountNo() + "productCode:" + queryModel.getProductCode());
			return signErrorResult(response, e);
		}
	}

	/**
	 * 专户流水查询接口 fn100012
	 * 银企直连
	 */
	@RequestMapping(value = "", params = {PARAMS_FN_KEY_WITH_COMBINATORS + QueryOpsFunctionCodes.QUERY_ACCOUNT_TRADE_DETAIL}, method = RequestMethod.POST)
	public @ResponseBody String queryAccountTradeDetailDirectBank(
			@ModelAttribute AccountTradeDetailModel accountTradeDetailModel, HttpServletResponse response){
		try{
			TradeListApiHandler tradeListApiHandler;
			if (PaymentInstitutionName.DIRECTBANK.equals(accountTradeDetailModel.getPaymentInstitutionName())) {
				tradeListApiHandler = directBankTradeListApiHandler;
			} else {
				tradeListApiHandler = thirdPartyTradeListApiHandler;
			}
			Integer verifyCode = tradeListApiHandler.verifyAccountTradeDetailModel(accountTradeDetailModel);
			if(0 != verifyCode){
				return signErrorResult(response, verifyCode);
			}

			PagedTradeList pagedList = tradeListApiHandler.findAccountTradeListBy(
					accountTradeDetailModel.getProductCode(),
					accountTradeDetailModel.getCapitalAccountNo(),
					accountTradeDetailModel.getPaymentInstitutionName(),
					accountTradeDetailModel.getStartTime(),
					accountTradeDetailModel.getEndTime(),
					accountTradeDetailModel.getPage(),
					accountTradeDetailModel.getAccountSide());

			AccountTradeListResultModel accountTradeListResultModel =
					new AccountTradeListResultModel(
							accountTradeDetailModel.getProductCode(),
							accountTradeDetailModel.getCapitalAccountNo(),
							accountTradeDetailModel.getPaymentInstitutionName().getOrdinal(),
							pagedList.isHasNextPage(),
							pagedList.getTradeList()
					);
			String responseString = signSucResult(response, "accountTradeDetail", accountTradeListResultModel,
					SerializerFeature.WriteNullListAsEmpty);
			logger.info("QueryApiController#queryAccountTradeDetailDirectBank fn 100012 " +
							"the request parameter is {} the response is {}", accountTradeDetailModel.toString(),
					responseString);
			return responseString;
		}catch (Exception e){
			logger.error("QueryApiController#queryAccountTradeDetailDirectBank fn 100012 error occurred", e);
			return signErrorResult(response, e);
		}
	}
}
