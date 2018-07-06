package com.suidifu.morganstanley.controller.api.voucher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.suidifu.morganstanley.controller.base.BaseApiController;
import com.suidifu.morganstanley.handler.voucher.BusinessPaymentVoucherHandler;
import com.suidifu.morganstanley.model.request.voucher.BusinessPaymentVoucher;
import com.suidifu.morganstanley.model.response.BaseResponse;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.exception.voucher.BankTransactionNoExistException;
import com.zufangbao.gluon.exception.voucher.CashFlowNotExistException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.wellsfargo.api.util.IpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

import static com.zufangbao.gluon.spec.morganstanley.GlobalSpec4MorganStanley.ErrorMsg4Voucher.CASH_FLOW_NOT_EXIST_EXCEPTION_MSG;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/10/9 <br>
 * Time:上午10:33 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@RestController
@RequestMapping("/api/v3")
@Api(tags = {"五维金融贷后接口3.0"}, description = " ")
@Log4j2
public class BusinessPaymentVoucherController extends BaseApiController {
    @Resource
    private BusinessPaymentVoucherHandler businessPaymentVoucherHandler;

    private final String BankTransactionNoExistExceptionMsg = "打款流水号已关联凭证！";

    private final String cashFlowNotExistExceptionMsg = "凭证对应流水不存在或已提交！";

    /**
     * 商户付款凭证指令-提交
     */
    @PostMapping(value = "/business-payment-vouchers/submit")
    @ApiOperation(value = "商户付款凭证", notes = "商户付款凭证提交", httpMethod = "POST", response = BaseResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 0, message = "成功", response = BaseResponse.class)})
    @ResponseBody
    public BaseResponse submit(HttpServletRequest request,
                               HttpServletResponse response,
                               @ApiParam(value = "商户付款凭证", required = true)
                               @Valid @ModelAttribute BusinessPaymentVoucher model,
                               BindingResult bindingResult) throws JsonProcessingException {
        String ip = IpUtils.getIpAddress(request);
        log.info("\n开始调用商户付款凭证指令提交接口, 请求参数：\n[financialContractNo：{} \nrequestNo：{}\nvoucherType：{}\nsubmitBusinessPaymentVoucher json data：{}\nip：{}]\n",
                model.getFinancialContractNo(), model.getRequestNo(),
                model.getVoucherType(), model.getDetail(), ip);

        // 判断要变更的数据中是否存在异常数据
        BaseResponse baseResponse = getValidatedResult(bindingResult);
        if (baseResponse != null) {
            return baseResponse;
        }

        businessPaymentVoucherHandler.checkByRequestNo(model.getRequestNo());

        businessPaymentVoucherHandler.saveLog(model, ip);

        try {
            List<CashFlow> cashFlowList = businessPaymentVoucherHandler.submitBusinessPaymentVoucher(model);
            String resultMessage = ApiMessage.SUCCESS.getMessage();
            if (cashFlowList.size() > 1) {
                resultMessage = "处理中！";
            }
            return wrapHttpServletResponse(response, ApiMessage.SUCCESS.getCode(), resultMessage);
        } catch (CashFlowNotExistException e) {
            log.error("#commandBusinessPaymentVoucher occur error {}: ", cashFlowNotExistExceptionMsg);
            throw new ApiException(ApiMessage.INVALID_PARAMS.getCode(), CASH_FLOW_NOT_EXIST_EXCEPTION_MSG);
        }catch (BankTransactionNoExistException e) {
            log.error("#commandBusinessPaymentVoucher occur error {}: ", BankTransactionNoExistExceptionMsg);
            throw new ApiException(ApiMessage.INVALID_PARAMS.getCode(), BankTransactionNoExistExceptionMsg);
        }
    }

    /**
     * 商户付款凭证指令-撤销
     */
    @PostMapping(value = "/business-payment-vouchers/undo")
    @ApiOperation(value = "商户付款凭证", notes = "商户付款凭证撤销", httpMethod = "POST", response = BaseResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 0, message = "成功", response = BaseResponse.class)})
    @ResponseBody
    public BaseResponse undo(HttpServletRequest request,
                             HttpServletResponse response,
                             @ApiParam(value = "商户付款凭证", required = true)
                             @Validated @ModelAttribute BusinessPaymentVoucher model,
                             BindingResult bindingResult) throws JsonProcessingException {
        String ip = IpUtils.getIpAddress(request);
        log.info("\n开始调用商户付款凭证指令撤销接口, 请求参数：\n[financialContractNo：{} \nrequestNo：{}\nvoucherType：{}\nundoBusinessPaymentVoucher json data：{}\nip：{}]\n",
                model.getFinancialContractNo(), model.getRequestNo(),
                model.getVoucherType(), model.getDetail(), ip);

        // 判断要变更的数据中是否存在异常数据
        BaseResponse baseResponse = getValidatedResult(bindingResult);
        if (baseResponse != null) {
            return baseResponse;
        }

        businessPaymentVoucherHandler.checkByRequestNo(model.getRequestNo());

        businessPaymentVoucherHandler.saveLog(model, ip);

        businessPaymentVoucherHandler.undoBusinessPaymentVoucher(model);

        return wrapHttpServletResponse(response, ApiMessage.SUCCESS);
    }
}