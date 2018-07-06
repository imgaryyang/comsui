package com.suidifu.morganstanley.controller.api.voucher;

import com.suidifu.hathaway.job.Priority;
import com.suidifu.morganstanley.controller.base.BaseApiController;
import com.suidifu.morganstanley.handler.voucher.ActivePaymentVoucherHandler;
import com.suidifu.morganstanley.model.request.voucher.ActivePaymentVoucher;
import com.suidifu.morganstanley.model.response.BaseResponse;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.exception.voucher.CashFlowNotExistException;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.contract.ContractActiveSourceDocumentMapper;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.CashFlowChargeProxy;
import com.zufangbao.wellsfargo.yunxin.handler.ActivePaymentVoucherProxy;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.zufangbao.gluon.spec.morganstanley.GlobalSpec4MorganStanley.ErrorMsg4Voucher.CASH_FLOW_NOT_EXIST_EXCEPTION_MSG;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/10/9 <br>
 * Time:上午10:49 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@RestController
@RequestMapping("/api/v3")
@Api(tags = {"五维金融贷后接口3.0"}, description = " ")
@Log4j2
public class ActivePaymentVoucherController extends BaseApiController {
    @Resource
    private ActivePaymentVoucherProxy activePaymentVoucherProxy;
    @Resource
    private CashFlowChargeProxy cashFlowChargeProxy;
    @Resource
    private FinancialContractService financialContractService;
    /**
     * 主动付款凭证指令 - 提交
     */
    @Resource
    private ActivePaymentVoucherHandler activePaymentVoucherHandler;

    @PostMapping(value = "/active-payment-vouchers/submit")
    @ApiOperation(value = "主动付款凭证指令", notes = "主动付款凭证指令提交", httpMethod = "POST", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 0, message = "成功", response = String.class)})
    @ResponseBody
    public BaseResponse submit(HttpServletRequest request,
                               HttpServletResponse response,
                               @ApiParam(value = "主动付款凭证指令", required = true)
                               @Validated @ModelAttribute ActivePaymentVoucher model,
                               BindingResult bindingResult) {
        log.info("开始调用主动付款凭证指令 - 提交, 请求参数：[ requestNo: {}, ", model.getRequestNo());
        log.info("json data：{} ", model.getDetail());
        log.info("bankTransactionNo：{} ", model.getBankTransactionNo());
        log.info("financialContractNo：{} ", model.getFinancialContractNo());
        log.info("paymentAccountNo：{} ", model.getPaymentAccountNo());
        log.info("paymentBank：{} ", model.getPaymentBank());
        log.info("receivableAccountNo：{} ", model.getReceivableAccountNo());
        log.info("paymentName：{} ", model.getPaymentName());
        log.info("ip：{} ]", IpUtil.getIpAddress(request));

        // 判断要变更的数据中是否存在异常数据
        BaseResponse baseResponse = getValidatedResult(bindingResult);
        if (baseResponse != null) {
            return baseResponse;
        }

        try {
            MultipartHttpServletRequest fileRequest = (MultipartHttpServletRequest) request;
            activePaymentVoucherHandler.checkRequestNoAndSaveLog(model, IpUtil.getIpAddress(fileRequest));
            List<ContractActiveSourceDocumentMapper> mappers = activePaymentVoucherHandler.submitActivePaymentVoucher(model, fileRequest);
            if (CollectionUtils.isNotEmpty(mappers)) {
                for (ContractActiveSourceDocumentMapper mapper : mappers) {
                    sendChargeMsg2Mq(mapper);
                    sendRecoverMsg2Mq(mapper);
                }
            }
            return wrapHttpServletResponse(response, ApiMessage.SUCCESS);
        } catch (CashFlowNotExistException e) {
            log.error("#submitActivePaymentVoucher occur error ]: {}", CASH_FLOW_NOT_EXIST_EXCEPTION_MSG);
            throw new ApiException(ApiMessage.INVALID_PARAMS.getCode(), CASH_FLOW_NOT_EXIST_EXCEPTION_MSG);
        } catch (IOException e) {
            log.error("#submitActivePaymentVoucher occur error ]: {}", e.getMessage());
            throw new ApiException(ApiMessage.SYSTEM_ERROR);
        }
    }

    private void sendChargeMsg2Mq(ContractActiveSourceDocumentMapper mapper) {
        try {
            log.info("#api提交主动还款凭证后，发送充值消息，contractUuid[" + mapper.getContractUuid() + "],sourceDocumentUuid[" + mapper.getSourceDocumentUuid() + "]," +
                    "cashFlowUuid[" + mapper.getCashFlowUuid() + "]");
            cashFlowChargeProxy.charge_cash_into_virtual_account_for_rpc(mapper.getCashFlowUuid(), mapper.getContractUuid(), Priority.High.getPriority());
        } catch (Exception e) {
            log.error("#api提交主动还款凭证后，发送充值消息，contractUuid[" + mapper.getContractUuid() + "],sourceDocumentUuid[" + mapper.getSourceDocumentUuid() + "]," +
                    "cashFlowUuid[" + mapper.getCashFlowUuid() + "]，with exception stack trace[" + ExceptionUtils.getFullStackTrace(e) + "]");
        }
    }

    private void sendRecoverMsg2Mq(ContractActiveSourceDocumentMapper mapper) {
        try {
            log.info("#api提交主动还款凭证后，发送待核销主动还款凭证消息，contractUuid[" + mapper.getContractUuid() + "],sourceDocumentUuid[" + mapper.getSourceDocumentUuid() + "]");
            activePaymentVoucherProxy.recover_active_payment_voucher(mapper.getContractUuid(), mapper.getSourceDocumentUuid(), Priority.Medium.getPriority());
        } catch (Exception e) {
            log.error("#api提交主动还款凭证后，发送待核销主动还款凭证消息，contractUuid[" + mapper.getContractUuid() + "],sourceDocumentUuid[" + mapper.getSourceDocumentUuid() + "]," +
                    "with exception stack trace[" + ExceptionUtils.getFullStackTrace(e) + "]");
        }
    }

    /**
     * 主动付款凭证指令-撤销
     */
    @PostMapping(value = "/active-payment-vouchers/undo")
    @ApiOperation(value = "主动付款凭证指令", notes = "主动付款凭证指令撤销", httpMethod = "POST", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 0, message = "成功", response = String.class)})
    @ResponseBody
    public BaseResponse undo(HttpServletRequest request,
                             HttpServletResponse response,
                             @ApiParam(value = "主动付款凭证指令", required = true)
                             @Validated @ModelAttribute ActivePaymentVoucher model,
                             BindingResult bindingResult) {
        log.info("开始调用主动付款凭证指令 - 撤销, 请求参数：[ requestNo: {}, ", model.getRequestNo());
        log.info("json data：{} ", model.getDetail());
        log.info("bankTransactionNo：{} ", model.getBankTransactionNo());
        log.info("financialContractNo：{} ", model.getFinancialContractNo());
        log.info("paymentAccountNo：{} ", model.getPaymentAccountNo());
        log.info("paymentBank：{} ", model.getPaymentBank());
        log.info("receivableAccountNo：{} ", model.getReceivableAccountNo());
        log.info("paymentName：{} ", model.getPaymentName());
        log.info("ip：{} ]", IpUtil.getIpAddress(request));

        // 判断要变更的数据中是否存在异常数据
        BaseResponse baseResponse = getValidatedResult(bindingResult);
        if (baseResponse != null) {
            return baseResponse;
        }

        activePaymentVoucherHandler.checkRequestNoAndSaveLog(model, IpUtil.getIpAddress(request));
        FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(model.getFinancialContractNo());
        String bankTransactionNo = model.getBankTransactionNo();
        activePaymentVoucherHandler.undoActivePaymentVoucher(financialContract, bankTransactionNo);
        return wrapHttpServletResponse(response, ApiMessage.SUCCESS);
    }
}