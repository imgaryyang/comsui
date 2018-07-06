package com.suidifu.morganstanley.controller.api.repayment;

import com.suidifu.hathaway.job.Priority;
import com.suidifu.matryoshka.handler.ProductCategoryCacheHandler;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.morganstanley.configuration.bean.weifang.WeiFangProperties;
import com.suidifu.morganstanley.controller.base.BaseApiController;
import com.suidifu.morganstanley.handler.RepaymentPlanHandlerSession;
import com.suidifu.morganstanley.model.request.CheckMutableFee;
import com.suidifu.morganstanley.model.request.MutableFee;
import com.suidifu.morganstanley.model.response.BaseResponse;
import com.suidifu.morganstanley.service.MutableFeeLogService;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.gluon.exception.*;
import com.zufangbao.gluon.exception.mutableFee.FinancialContractNotPAYGException;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.yunxin.exception.*;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.*;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_API_V3;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_MUTABLE_FEE;

/**
 * Created by hwr on 17-9-25.
 */

@RestController
@RequestMapping(URL_API_V3)
@Api(tags = {"五维金融贷后接口3.0"}, description = " ")
@Log4j2
public class MutableFeeController extends BaseApiController {
    @Qualifier("mutableFeeLogV2Service")
    @Autowired
    private MutableFeeLogService mutableFeeLogService;

    @Autowired
    private RepaymentPlanHandlerSession repaymentPlanHandlerSession;

    @Autowired
    private ProductCategoryCacheHandler productCategoryCacheHandler;

    @Autowired
    private WeiFangProperties weiFangProperties;

    @Autowired
    private SandboxDataSetHandler sandboxDataSetHandler;

    /**
     * 费用浮动接口
     *
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "费用浮动", notes = "更新相关费用，目前仅支持应还利息", httpMethod = "POST", produces = "application/json", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 0, message = "成功", response = String.class)
//			,@ApiResponse(code = ApiResponseCode.REPEAT_REQUEST_NO, message = "请求编号重复"),
//			@ApiResponse(code = ApiResponseCode.APPROVED_DATE_ERROR, message = "审核日期格式错误"),
//			@ApiResponse(code = ApiResponseCode.FINANCIAL_CONTRACT_NOT_EXIST, message = "信托合同不存在"),
//			@ApiResponse(code = ApiResponseCode.FINANCIAL_CONTRACT_NOT_PAY_FOR_GO, message = "信托合同不支持随借随还"),
//			@ApiResponse(code = ApiResponseCode.CONTRACT_NOT_EXIST, message = "贷款合同不存在"),
//			@ApiResponse(code = ApiResponseCode.CONTRACT_NOT_EQUAL_FINANCIAL_CONTRACT, message = "信托计划与贷款合同不匹配"),
//			@ApiResponse(code = ApiResponseCode.REPAYMENT_CODE_NOT_IN_CONTRACT, message = "不存在该有效还款计划或者还款计划不在贷款合同内"),
//			@ApiResponse(code = ApiResponseCode.REPAYMENT_PLAN_CONTRACT_NOT_EQUAL, message = "还款计划与贷款合同不匹配"),
//			@ApiResponse(code = ApiResponseCode.REPAYMENT_PLAN_LOCKED, message = "还款计划被锁定"),
//			@ApiResponse(code = ApiResponseCode.REPAYMENTPLAN_SUCCESS, message = "还款计划已还款成功"),
//			@ApiResponse(code = ApiResponseCode.REPAYMENT_PLAN_CAN_BE_ROLLBACK, message = "还款计划已申请提前还款"),
//			@ApiResponse(code = ApiResponseCode.MUTABLE_FEE_AMOUNT_INVAILD, message = "非法还息金额")
    })
    @PostMapping(value = URL_MUTABLE_FEE)
    @ResponseBody
    public BaseResponse mutableFee(HttpServletRequest request,
                                   HttpServletResponse response,
                                   @ApiParam(value = "浮动费用", required = true)
                                   @Validated @ModelAttribute MutableFee mutableFee,
                                   BindingResult bindingResult) {
        log.info("开始调用费用浮动接口, 请求参数：[ uniqueId: {}, ", mutableFee.getUniqueId());
        log.info("requestNo：{} ", mutableFee.getRequestNo());
        log.info("contractNo：{} ", mutableFee.getContractNo());
        log.info("requestNo：{} ", mutableFee.getRequestNo());
        log.info("reasonCode：{} ", mutableFee.getReasonCode());
        log.info("json data：{} ", mutableFee.getDetails());
        log.info("ip：{} ]", IpUtil.getIpAddress(request));

        // 判断要变更的数据中是否存在异常数据
        BaseResponse baseResponse = getValidatedResult(bindingResult);
        if (baseResponse != null) {
            return baseResponse;
        }

        String resultMsg = "";
        try {
            CheckMutableFee.isValid(mutableFee);
            mutableFeeLogService.checkByRequestNo(mutableFee.getRequestNo());

            repaymentPlanHandlerSession.mutableFeeV2(mutableFee, com.zufangbao.gluon.util.IpUtil.getIpAddress(request), -1L, Priority.High.getPriority());

            return wrapHttpServletResponse(response, ApiMessage.SUCCESS);
        } catch (ApiException e) {
            resultMsg = "Msg:[" + e.getMessage() + "] code:[" + e.getCode() + "] 请求编号:[requestNo : " + mutableFee.getRequestNo() + "]";
//            log.error("#request occur error [requestNo : " + mutableFee.getRequestNo() + " ]! " + e.getCode() + ":" + e.getMessage());
            throw e;
        } catch (DateFormatException e) {

            resultMsg = "审核日期格式错误 [approvedTime : " + mutableFee.getApprovedTime() + "] not [yyyy-MM-dd]";
            log.error("#approvedTime error [approvedTime : " + mutableFee.getApprovedTime()
                    + " ] not like [yyyy-MM-dd]!");
            throw new ApiException(APPROVED_DATE_ERROR);
        } catch (FinancialContractNotExistException e) {
            resultMsg = "信托合同不存在 [financialProductCode : " + mutableFee.getFinancialProductCode() + "]";
            log.error("#financial contract occur error [financialProductCode : "
                    + mutableFee.getFinancialProductCode() + " ]!");
            throw new ApiException(FINANCIAL_CONTRACT_NOT_EXIST);
        } catch (FinancialContractNotPAYGException e) {
            resultMsg = "信托合同不支持随借随还 [financialProductCode : " + mutableFee.getFinancialProductCode() + "]";
            log.error("#financial contract (pay_for_go) occur error [financialProductCode : "
                    + mutableFee.getFinancialProductCode() + " ]!");
            throw new ApiException(FINANCIAL_CONTRACT_NOT_PAY_FOR_GO);
        } catch (ContractNotExistException e) {
            resultMsg = "贷款合同不存在 [contractNo : " + mutableFee.getContractNo() + "]";
            log.error("#contract occur error [contractNo : " + mutableFee.getContractNo() + " ]!");
            throw new ApiException(CONTRACT_NOT_EXIST);
        } catch (FinancialContractNotIncludedContractException e) {
            resultMsg = "信托合同不包含贷款合同 [contractNo : " + mutableFee.getContractNo() + " , "
                    + "financialProductCode : " + mutableFee.getFinancialProductCode() + "]";
            log.error("#financial contract (contract) occur error [contractNo : " + mutableFee.getContractNo()
                    + "," + "financialProductCode : " + mutableFee.getFinancialProductCode() + " ]!");
            throw new ApiException(CONTRACT_NOT_EQUAL_FINANCIAL_CONTRACT);
        } catch (RepaymentPlanNotExistException e) {
            resultMsg = "还款计划不存在 [repaymentPlanNo : " + mutableFee.getRepaymentPlanNo() + "]";
            log.error("#prepayment occur error [repaymentPlanNo : " + mutableFee.getRepaymentPlanNo() + " ]!");
            throw new ApiException(REPAYMENT_CODE_NOT_IN_CONTRACT);
        } catch (ContractNotIncludedRepaymentPlanException e) {

            resultMsg = "贷款合同不包含还款计划 [repaymentPlanNo : " + mutableFee.getRepaymentPlanNo() + " , "
                    + "financialProductCode : " + mutableFee.getFinancialProductCode() + "]";
            log.error("#financial contract (prepayment) occur error [repaymentPlanNo : "
                    + mutableFee.getRepaymentPlanNo() + " , " + "financialProductCode : "
                    + mutableFee.getFinancialProductCode() + " ]!");
            throw new ApiException(REPAYMENT_PLAN_CONTRACT_NOT_EQUAL);
        } catch (RepaymentPlanSuccessException e) {
            resultMsg = "还款计划已还款成功 [repaymentPlanNo : " + mutableFee.getRepaymentPlanNo() + "]";
            log.error("#assetset is already successed occur error [repaymentPlanNo : "
                    + mutableFee.getRepaymentPlanNo() + " ]!");
            throw new ApiException(REPAYMENTPLAN_SUCCESS);
        } catch (RepaymentPlanDeductLockedException e) {
            resultMsg = "还款计划扣款锁定 [repaymentPlanNo : " + mutableFee.getRepaymentPlanNo() + "]";
            log.error("#prepayment (!empty_deduct_uuid) occur error [repaymentPlanNo : "
                    + mutableFee.getRepaymentPlanNo() + " ]!");
            throw new ApiException(REPAYMENT_PLAN_LOCKED);
        } catch (RepaymentPlanCanBeRollbackException e) {
            resultMsg = "还款计划已申请提前还款 [repaymentPlanNo : " + mutableFee.getRepaymentPlanNo() + "]";
            log.error("#prepayment (canBeRollback) occur error [repaymentPlanNo : "
                    + mutableFee.getRepaymentPlanNo() + " ]!");
            throw new ApiException(REPAYMENT_PLAN_CAN_BE_ROLLBACK);
        } catch (MutableFeeAmountInvaildException e) {
            resultMsg = "非法还息金额 [不得小于当前实收金额]";
            log.error("#feeAmount occur error [too little]!");
            throw new ApiException(MUTABLE_FEE_AMOUNT_INVAILD);
//            return wrapHttpServletResponse(response, MUTABLE_FEE_AMOUNT_INVAILD);
        } catch (Exception e) {
            resultMsg = e.getMessage();
            log.error("#mutableFee occur error !", e);
            // 返回结果信息为失败，出现异常
            throw e;
        } finally {
            String ip = com.zufangbao.gluon.util.IpUtil.getIpAddress(request);
            mutableFeeLogService.saveMutableFeeLog(mutableFee, ip, resultMsg);
        }
    }
}
