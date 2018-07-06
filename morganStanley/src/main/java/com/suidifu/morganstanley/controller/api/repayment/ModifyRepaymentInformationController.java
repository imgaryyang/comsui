package com.suidifu.morganstanley.controller.api.repayment;

import com.suidifu.morganstanley.controller.base.BaseApiController;
import com.suidifu.morganstanley.handler.repayment.RepaymentInformationApiHandler;
import com.suidifu.morganstanley.model.request.repayment.ModifyRepaymentInformation;
import com.suidifu.morganstanley.model.response.BaseResponse;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.wellsfargo.yunxin.handler.ContractApiHandler;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_API_V3;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_MODIFY_REPAYMENT_INFORMATION;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/9/25 <br>
 * Time:下午6:20 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@RestController
@RequestMapping(URL_API_V3)
@Api(tags = {"五维金融贷后接口3.0"}, description = " ")
@Log4j2
public class ModifyRepaymentInformationController extends BaseApiController {
    @Resource
    private RepaymentInformationApiHandler repaymentInformationApiHandler;

    @Resource
    private ContractApiHandler contractApiHandler;

    /**
     * 变更还款信息
     *
     * @param information 还款信息变更模型
     * @param request     http请求
     * @param response    http响应
     * @return 返回变更的结果信息，包括成功和其他各种失败情况
     */
    @ApiOperation(value = "变更还款信息", notes = "变更还款信息", response = BaseResponse.class, httpMethod = "POST")
    @ApiResponses(value = {@ApiResponse(code = 0, message = "成功", response = BaseResponse.class)})
    @PostMapping(value = URL_MODIFY_REPAYMENT_INFORMATION)
    @ResponseBody
    public BaseResponse modifyRepaymentInformation(HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   @ApiParam(value = "变更还款信息", required = true)
                                                   @Validated
                                                   @ModelAttribute ModifyRepaymentInformation information, BindingResult bindingResult) {
        String ip = IpUtil.getIpAddress(request);
        log.info("\n开始调用变更还款信息接口，请求参数：\n[uniqueId: {}, \ncontractNo: {}, " +
                        "\nrequestNo: {}, \nipAddress:{}]\n",
                information.getUniqueId(), information.getContractNo(),
                information.getRequestNo(), ip);

        BaseResponse baseResponse = getValidatedResult(bindingResult);
        if (baseResponse != null) {
            return baseResponse;
        }

        repaymentInformationApiHandler.checkRequestNo(information.getRequestNo());

        Contract contract = contractApiHandler.getContractBy(information.getUniqueId(), information.getContractNo());

        if (null == contract) {
            log.error("#modifyRepaymentInformation occur error [requestNo : {}]!", information.getRequestNo());
            return wrapHttpServletResponse(response, ApiMessage.CONTRACT_NOT_EXIST);
        }
        repaymentInformationApiHandler.saveLog(information, contract, ip);

        repaymentInformationApiHandler.modifyRepaymentInformation(information, contract);

        return wrapHttpServletResponse(response, ApiMessage.SUCCESS);
    }
}