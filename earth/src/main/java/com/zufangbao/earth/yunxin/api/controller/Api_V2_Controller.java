package com.zufangbao.earth.yunxin.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.suidifu.hathaway.job.Priority;
import com.zufangbao.earth.yunxin.api.BaseApiController;
import com.zufangbao.earth.yunxin.api.exception.ApiResponseCode;
import com.zufangbao.earth.yunxin.api.handler.RepurchaseCommandHandler;
import com.zufangbao.earth.yunxin.api.model.command.RepurchaseCommandModel;
import com.zufangbao.gluon.exception.*;
import com.zufangbao.gluon.exception.mutableFee.FinancialContractNotPAYGException;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.api.model.repayment.QueryRepaymentOrderModel;
import com.zufangbao.sun.entity.payment.order.QueryRepaymentOrderRequest;
import com.zufangbao.sun.entity.repurchase.RepurchaseApiResponse;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.MutableFeeLogService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.yunxin.entity.api.mutableFee.MutableFeeModel;
import com.zufangbao.sun.yunxin.exception.*;
import com.zufangbao.wellsfargo.yunxin.handler.RepaymentOrderHandler;
import com.zufangbao.wellsfargo.yunxin.handler.RepaymentPlanHandlerNoSession;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/api/v2")
@Api(value = "五维金融贷后接口V2.0", description = "五维金融贷后接口V2.0")
public class Api_V2_Controller extends BaseApiController {

	@Autowired
	private MutableFeeLogService mutableFeeLogService;
	
	@Autowired
	private RepurchaseCommandHandler repurchaseCommandHandler;

	@Autowired
	private RepaymentPlanHandlerNoSession repaymentPlanHandlerNoSession;
	
	@Autowired
	private FinancialContractService financialContractService;
	
	@Autowired
	private RepaymentOrderService repaymentOrderService;
	
	@Autowired
	private RepaymentOrderHandler repaymentOrderHandler;
	
	private static final Log logger = LogFactory.getLog(Api_V2_Controller.class);

	/**
	 * 费用浮动接口
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ApiOperation(value = "费用浮动", notes = "更新相关费用，目前仅支持应还利息")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"),
			@ApiResponse(code = ApiResponseCode.REPEAT_REQUEST_NO, message = "请求编号重复"),
			@ApiResponse(code = 404, message = "No matching user found"),
			@ApiResponse(code = ApiResponseCode.APPROVED_DATE_ERROR, message = "审核日期格式错误"),
			@ApiResponse(code = ApiResponseCode.FINANCIAL_CONTRACT_NOT_EXIST, message = "信托合同不存在"),
			@ApiResponse(code = ApiResponseCode.FINANCIAL_CONTRACT_NOT_PAY_FOR_GO, message = "信托合同不支持随借随还"),
			@ApiResponse(code = ApiResponseCode.CONTRACT_NOT_EXIST, message = "贷款合同不存在"),
			@ApiResponse(code = ApiResponseCode.CONTRACT_NOT_EQUAL_FINANCIAL_CONTRACT, message = "信托计划与贷款合同不匹配"),
			@ApiResponse(code = ApiResponseCode.REPAYMENT_CODE_NOT_IN_CONTRACT, message = "不存在该有效还款计划或者还款计划不在贷款合同内"),
			@ApiResponse(code = ApiResponseCode.REPAYMENT_PLAN_CONTRACT_NOT_EQUAL, message = "还款计划与贷款合同不匹配"),
			@ApiResponse(code = ApiResponseCode.REPAYMENTPLAN_LOCKED, message = "还款计划被锁定"),
			@ApiResponse(code = ApiResponseCode.REPAYMENTPLAN_SUCCESS, message = "还款计划已还款成功"),
			@ApiResponse(code = ApiResponseCode.REPAYMENT_PLAN_CAN_BE_ROLLBACK, message = "还款计划已申请提前还款"),
			@ApiResponse(code = ApiResponseCode.MUTABLE_FEE_AMOUNT_INVAILD, message = "非法还息金额") })
	@RequestMapping(value = "/mutableFee", method = RequestMethod.POST)
	@Deprecated
	public @ResponseBody String mutableFee(@ModelAttribute MutableFeeModel mutableFeeModel, HttpServletRequest request,
			HttpServletResponse response) {
		String resultMsg = "";
		try {
			if (!mutableFeeModel.isValid()) {
				resultMsg = mutableFeeModel.getCheckFailedMsg();
				return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, mutableFeeModel.getCheckFailedMsg());
			}
	
			mutableFeeLogService.checkByRequestNo(mutableFeeModel.getRequestNo());
			
			repaymentPlanHandlerNoSession.mutableFeeV2(mutableFeeModel, IpUtil.getIpAddress(request), -1L, Priority.High.getPriority());
			
			return signSucResult(response);
		} catch (ApiException e) {
			e.printStackTrace();
			resultMsg = "请求编号重复 [requestNo : " + mutableFeeModel.getRequestNo() + "]";
			logger.error("#request occur error [requestNo : " + mutableFeeModel.getRequestNo() + " ]!");
			return signErrorResult(response, e);
		} catch (DateFormatException e) {
			resultMsg = "审核日期格式错误 [approvedTime : " + mutableFeeModel.getApprovedTime() + "] not [yyyy-MM-dd]";
			logger.error("#approvedTime error [approvedTime : " + mutableFeeModel.getApprovedTime()
					+ " ] not like [yyyy-MM-dd]!");
			return signErrorResult(response, new ApiException(ApiResponseCode.APPROVED_DATE_ERROR));
		} catch (FinancialContractNotExistException e) {
			e.printStackTrace();
			resultMsg = "信托合同不存在 [financialProductCode : " + mutableFeeModel.getFinancialProductCode() + "]";
			logger.error("#financial contract occur error [financialProductCode : "
					+ mutableFeeModel.getFinancialProductCode() + " ]!");
			return signErrorResult(response, new ApiException(ApiResponseCode.FINANCIAL_CONTRACT_NOT_EXIST));
		} catch (FinancialContractNotPAYGException e) {
			e.printStackTrace();
			resultMsg = "信托合同不支持随借随还 [financialProductCode : " + mutableFeeModel.getFinancialProductCode() + "]";
			logger.error("#financial contract (pay_for_go) occur error [financialProductCode : "
					+ mutableFeeModel.getFinancialProductCode() + " ]!");
			return signErrorResult(response, new ApiException(ApiResponseCode.FINANCIAL_CONTRACT_NOT_PAY_FOR_GO));
		} catch (ContractNotExistException e) {
			e.printStackTrace();
			resultMsg = "贷款合同不存在 [contractNo : " + mutableFeeModel.getContractNo() + "]";
			logger.error("#contract occur error [contractNo : " + mutableFeeModel.getContractNo() + " ]!");
			return signErrorResult(response, new ApiException(ApiResponseCode.CONTRACT_NOT_EXIST));
		} catch (FinancialContractNotIncludedContractException e) {
			e.printStackTrace();
			resultMsg = "信托合同不包含贷款合同 [contractNo : " + mutableFeeModel.getContractNo() + " , "
					+ "financialProductCode : " + mutableFeeModel.getFinancialProductCode() + "]";
			logger.error("#financial contract (contract) occur error [contractNo : " + mutableFeeModel.getContractNo()
					+ "," + "financialProductCode : " + mutableFeeModel.getFinancialProductCode() + " ]!");
			return signErrorResult(response, new ApiException(ApiResponseCode.CONTRACT_NOT_EQUAL_FINANCIAL_CONTRACT));
		} catch (RepaymentPlanNotExistException e) {
			e.printStackTrace();
			resultMsg = "还款计划不存在 [repaymentPlanNo : " + mutableFeeModel.getRepaymentPlanNo() + "]";
			logger.error("#prepayment occur error [repaymentPlanNo : " + mutableFeeModel.getRepaymentPlanNo() + " ]!");
			return signErrorResult(response, new ApiException(ApiResponseCode.REPAYMENT_CODE_NOT_IN_CONTRACT));
		} catch (ContractNotIncludedRepaymentPlanException e) {
			resultMsg = "贷款合同不包含还款计划 [repaymentPlanNo : " + mutableFeeModel.getRepaymentPlanNo() + " , "
					+ "financialProductCode : " + mutableFeeModel.getFinancialProductCode() + "]";
			logger.error("#financial contract (prepayment) occur error [repaymentPlanNo : "
					+ mutableFeeModel.getRepaymentPlanNo() + " , " + "financialProductCode : "
					+ mutableFeeModel.getFinancialProductCode() + " ]!");
			return signErrorResult(response, new ApiException(ApiResponseCode.REPAYMENT_PLAN_CONTRACT_NOT_EQUAL));
		} catch (RepaymentPlanSuccessException e) {
			e.printStackTrace();
			resultMsg = "还款计划已还款成功 [repaymentPlanNo : " + mutableFeeModel.getRepaymentPlanNo() + "]";
			logger.error("#assetset is already successed occur error [repaymentPlanNo : "
					+ mutableFeeModel.getRepaymentPlanNo() + " ]!");
			return signErrorResult(response, new ApiException(ApiResponseCode.REPAYMENTPLAN_SUCCESS));
		} catch (RepaymentPlanDeductLockedException e) {
			e.printStackTrace();
			resultMsg = "还款计划扣款锁定 [repaymentPlanNo : " + mutableFeeModel.getRepaymentPlanNo() + "]";
			logger.error("#prepayment (!empty_deduct_uuid) occur error [repaymentPlanNo : "
					+ mutableFeeModel.getRepaymentPlanNo() + " ]!");
			return signErrorResult(response, new ApiException(ApiResponseCode.REPAYMENTPLAN_LOCKED));
		} catch (RepaymentPlanCanBeRollbackException e) {
			e.printStackTrace();
			resultMsg = "还款计划已申请提前还款 [repaymentPlanNo : " + mutableFeeModel.getRepaymentPlanNo() + "]";
			logger.error("#prepayment (canBeRollback) occur error [repaymentPlanNo : "
					+ mutableFeeModel.getRepaymentPlanNo() + " ]!");
			return signErrorResult(response, new ApiException(ApiResponseCode.REPAYMENT_PLAN_CAN_BE_ROLLBACK));
		} catch (MutableFeeAmountInvaildException e) {
			e.printStackTrace();
			resultMsg = "非法还息金额 [不得小于当前实收金额]";
			logger.error("#feeAmount occur error [too little]!");
			return signErrorResult(response, new ApiException(ApiResponseCode.MUTABLE_FEE_AMOUNT_INVAILD));
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = e.getMessage();
			logger.error("#mutableFee occur error !", e);
			// 返回结果信息为失败，出现异常
			return signErrorResult(response, e);
		} finally {
			String ip = IpUtil.getIpAddress(request);
			mutableFeeLogService.saveMutableFeeLog(mutableFeeModel, ip, resultMsg);
		}
	}

	/**
	 * 回购申请接口（提交及撤销）
	 *
	 * @param request
	 * @param response
	 * @return
	 */

	@ApiOperation(value = "回购申请（提交及撤销）", notes = "调用该接口，向贷后系统发送回购申请请求，支持批量处理。")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"),
			@ApiResponse(code = ApiResponseCode.REPEAT_REQUEST_NO, message = "请求编号重复"),
			@ApiResponse(code = ApiResponseCode.REPEAT_BATCH_NO, message = "批次号重复"),
			@ApiResponse(code = 404, message = "No matching user found"),
			@ApiResponse(code = ApiResponseCode.INVALID_PARAMS, message = "无效参数"),
			@ApiResponse(code = ApiResponseCode.FINANCIAL_CONTRACT_NOT_EXIST, message = "信托合同不存在"),
			@ApiResponse(code = ApiResponseCode.CONTRACT_NOT_EXIST, message = "贷款合同不存在"),
			@ApiResponse(code = ApiResponseCode.CONTRACT_NOT_EQUAL_FINANCIAL_CONTRACT, message = "信托计划与贷款合同不匹配"),
			@ApiResponse(code = ApiResponseCode.NOT_ALLOW_REPURCHASE, message = "该贷款合同不允许进行回购操作"),
			@ApiResponse(code = ApiResponseCode.EXIST_EXECUTING_ASSET, message = "该贷款合同下有正在执行的还款计划"),
			@ApiResponse(code = ApiResponseCode.ERROR_REPURCHASE_PRINCIPAL, message = "回购本金错误"),
			@ApiResponse(code = ApiResponseCode.NOT_EXIST_REPURCHASE_DOC, message = "该回购单不存在"),
			@ApiResponse(code = ApiResponseCode.NOT_ALLOW_INVALID, message = "该回购单处于不可作废状态"),
			@ApiResponse(code = ApiResponseCode.SYSTEM_ERROR, message = "系统错误") })
	@RequestMapping(value = "/repurchase", method = RequestMethod.POST)
	public @ResponseBody String submitRepurchase(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute RepurchaseCommandModel model) {
		try {
			logger.info("回购申请："+ JSON.toJSONString(model));
			if (model == null) {
				RepurchaseApiResponse apiResponse = new RepurchaseApiResponse().fail(ApiResponseCode.INVALID_PARAMS);
				return signResult(response, JSON.toJSONString(apiResponse, SerializerFeature.DisableCircularReferenceDetect));
			}
			if (model.getTransactionType() == RepurchaseCommandModel.SUBMIT) {
				if (model.submitParamsError()) {
					RepurchaseApiResponse apiResponse = new RepurchaseApiResponse().fail(ApiResponseCode.INVALID_PARAMS + "", model.getCheckFailedMsg());
					return signResult(response, JSON.toJSONString(apiResponse, SerializerFeature.DisableCircularReferenceDetect));
				}
				RepurchaseApiResponse apiResponse = repurchaseCommandHandler.batchRepurchaseCommand(model, IpUtil.getIpAddress(request));
				logger.info("回购申请执行成功，返回结果"+ JSON.toJSONString(apiResponse));
				return signResult(response, JSON.toJSONString(apiResponse, SerializerFeature.DisableCircularReferenceDetect));
			}
			if (model.getTransactionType() == RepurchaseCommandModel.UNDO) {
				if (model.undoParamsError()) {
					RepurchaseApiResponse apiResponse = new RepurchaseApiResponse().fail(ApiResponseCode.INVALID_PARAMS + "", model.getCheckFailedMsg());
					return signResult(response, JSON.toJSONString(apiResponse, SerializerFeature.DisableCircularReferenceDetect));
				}
				RepurchaseApiResponse apiResponse = repurchaseCommandHandler.undoRepurchaseCommand(model, IpUtil.getIpAddress(request));
				logger.info("回购撤销操作成功");
				return signResult(response, JSON.toJSONString(apiResponse, SerializerFeature.DisableCircularReferenceDetect));
			}
			RepurchaseApiResponse apiResponse = new RepurchaseApiResponse().fail(ApiResponseCode.INVALID_PARAMS+"", "交易类型［transactionType］错误！");
			return signResult(response, JSON.toJSONString(apiResponse, SerializerFeature.DisableCircularReferenceDetect));
		} catch (ApiException e) {
			e.printStackTrace();
			logger.error("#submitRepurchase occur error ! code:" + e.getCode() +", message "+ e.getMessage());
			RepurchaseApiResponse apiResponse = new RepurchaseApiResponse().fail(e.getCode());
			return signResult(response, JSON.toJSONString(apiResponse, SerializerFeature.DisableCircularReferenceDetect));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#submitRepurchase occur error ! code:" + e.getMessage());
			RepurchaseApiResponse apiResponse = new RepurchaseApiResponse().fail(ApiResponseCode.SYSTEM_ERROR);
			return signResult(response, JSON.toJSONString(apiResponse, SerializerFeature.DisableCircularReferenceDetect));
		}
	}

	
	/**
	 * 
	 * hjl
	 * 2017年8月4日
	 * 接口描述:供还款订单申请方调用，查询订单状态和明细，支持实时查询。
	 * 请求参数:Model(requestNo,financialProductCode,orderUniqueIds)
	 * 响应参数:code(返回码：０表示成功),message(请求返回信息),data(json)
	 */
	@ApiOperation(value = "还款订单查询", notes = "供还款订单申请方调用，查询订单状态和明细，支持实时查询")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"),
								 @ApiResponse(code = com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.SIGN_MER_CONFIG_ERROR, message = "缺少商户号merId或商户密钥secret"),
			 					 @ApiResponse(code = com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.REQUEST_NO_IS_EMPTY, message = "请求编号为空"),
			 					 @ApiResponse(code = com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.FINANCIAL_PRODUCT_CODE_IS_EMPTY, message = "信托产品代码不能为空"),
			 					 @ApiResponse(code = com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.ORDERUNIQUEIDS_OR_ORDERUUIDS_ISNULL, message = "商户订单号列表和五维订单号列表不能都为空"),
			 					 @ApiResponse(code = com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.FINANCIAL_PRODUCT_CODE_ERROR, message = "信托产品代码错误"),
			 					 @ApiResponse(code = com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.REPAYMENT_ORDER_UNIQUEIDSOR_ORDERUUIDS_ISNULL, message = "没有与商户订单号列表或五维订单号列表对应的还款订单"),
			 					 @ApiResponse(code = com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.ORDERUNIQUEIDS_NOT_CONTENTMENT, message = "商户订单号列表不满足条件"),
			 					 @ApiResponse(code = com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.ORDERUUIDS_NOT_CONTENTMENT, message = "五维订单号列表不满足条件") })
	@RequestMapping(value= "/query-repaymentOrder" , method=RequestMethod.POST)
	public @ResponseBody String CommandqueryRepaymentOrder(HttpServletRequest request,HttpServletResponse response,
			@ModelAttribute QueryRepaymentOrderRequest queryOrderModel){
		
		try{
			long startTime= System.currentTimeMillis();
	
			String merId=getMerchantId(request);
			if(merId==null || merId.isEmpty()){
				return signErrorResult(response, com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.SIGN_MER_CONFIG_ERROR);
			}
			//非空效和数据校验
			repaymentOrderHandler.checkQueryRepaymentOrderModelData(queryOrderModel);
			
			long end_query_count= System.currentTimeMillis();
			logger.info("query repaymentOrder count(orderUuid),use["+(end_query_count-startTime)+"]ms");
			
			List<QueryRepaymentOrderModel> queryRepaymentOrderModelList = repaymentOrderHandler.queryRepaymentOrderModelList(queryOrderModel);
			
			if(queryRepaymentOrderModelList.size()==0){
				return signErrorResult(response, com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.REPAYMENT_ORDER_UNIQUEIDSOR_ORDERUUIDS_ISNULL);
			}
			long queryRepaymentOrderModelsEnd = System.currentTimeMillis();
			logger.info("requestNo:"+queryOrderModel.getRequestNo()+"==query repaymentorderinterface total Time,use["+(queryRepaymentOrderModelsEnd-startTime)+"]ms");
			return signSucResult(response,"repaymentOrderList",queryRepaymentOrderModelList);
		} catch (Exception e) {
			logger.error("#CommandqueryRepaymentOrder occur error !: " + e.getMessage());
			e.printStackTrace();
			return signErrorResult(response, e);
		}
	}
	
}
