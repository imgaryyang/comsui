package com.suidifu.morganstanley.controller.api.voucher;

import com.suidifu.morganstanley.controller.base.BaseApiController;
import com.suidifu.morganstanley.handler.voucher.ThirdPartVoucherCommandHandler;
import com.suidifu.morganstanley.model.request.voucher.ThirdPartVoucher;
import com.suidifu.morganstanley.model.response.BaseResponse;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.gluon.util.IpUtil;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@RestController
@RequestMapping("/api/v3")
@Api(tags = {"五维金融贷后接口3.0"}, description = " ")
@Log4j2
public class ThirdPartyVoucherController extends BaseApiController {
    @Resource
    private ThirdPartVoucherCommandHandler thirdPartVoucherCommandHandler;

    /**
     * 第三方付款凭证指令
     */
    @PostMapping(value = "/third-part-vouchers/submit")
    @ApiOperation(value = "第三方付款凭证指令", notes = "第三方付款凭证指令", httpMethod = "POST", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 0, message = "成功", response = String.class)})
    @ResponseBody
    public BaseResponse thirdPartVoucherCommandModel(HttpServletRequest request,
                                                     HttpServletResponse response,
                                                     @ApiParam(value = "第三方付款凭证指令", required = true)
                                                     @Validated @ModelAttribute ThirdPartVoucher model,
                                                     BindingResult bindingResult) {
        log.info("开始调用商户付款凭证指令提交接口, 请求参数：[");
        log.info("financialContractNo：{} ", model.getFinancialContractNo());
        log.info("requestNo：{} ", model.getRequestNo());
        log.info("commandThirdPartyVoucher json data：{} ", model.getDetailListParseJson());
        log.info("ip：{} ]", IpUtil.getIpAddress(request));

        //校验是否存在异常数据
        BaseResponse baseResponse = getValidatedResult(bindingResult);
        if (baseResponse != null){
            return baseResponse;
        }
        //逻辑校验
        thirdPartVoucherCommandHandler.commandRequestLogicValidate(model);
        //生成对应的请求log
        Map<String, Object> errMap = thirdPartVoucherCommandHandler.generateThirdPartVoucherCommandLog(model, IpUtil.getIpAddress(request));
        if (errMap.size() > 0) {
            return wrapHttpServletResponse(response, ApiMessage.STATUS_CANNOT_INSERT, errMap);
        }
        return wrapHttpServletResponse(response,ApiMessage.SUCCESS);


    }
}