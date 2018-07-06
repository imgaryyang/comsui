package com.suidifu.morganstanley.controller.api.repayment;

import com.suidifu.hathaway.job.Priority;
import com.suidifu.matryoshka.handler.ProductCategoryCacheHandler;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.morganstanley.configuration.bean.weifang.WeiFangProperties;
import com.suidifu.morganstanley.controller.base.BaseApiController;
import com.suidifu.morganstanley.handler.repayment.PrepaymentApiHandler;
import com.suidifu.morganstanley.model.request.repayment.CheckPrepayment;
import com.suidifu.morganstanley.model.request.repayment.Prepayment;
import com.suidifu.morganstanley.model.response.BaseResponse;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.wellsfargo.yunxin.handler.ContractApiHandler;
import com.zufangbao.wellsfargo.yunxin.handler.PrepaymentHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_API_V3;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_PREPAYMENT;

/**
 * 提前全额还款接口
 *
 * @author louguanyang at 2017/9/27 15:24
 */
@RestController
@RequestMapping(URL_API_V3)
@Api(tags = {"五维金融贷后接口3.0"}, description = " ")
@Log4j2
public class PrepaymentController extends BaseApiController {
    @Resource
    private ContractApiHandler contractApiHandler;
    @Resource
    private PrepaymentHandler prepaymentHandler;
    @Resource
    private PrepaymentApiHandler prepaymentApiHandler;
    @Autowired
    private SandboxDataSetHandler sandboxDataSetHandler;
    @Autowired
    private WeiFangProperties weiFangProperties;
    @Autowired
    private ProductCategoryCacheHandler productCategoryCacheHandler;

    /**
     * 提前全额还款接口
     *
     * @param request       Http请求
     * @param response      Http响应
     * @param model         请求Model
     * @param bindingResult 校验结果
     * @return 响应结果
     */
    @ApiOperation(value = "提前全额还款接口", notes = "提前全额还款接口")
    @ApiResponses(value = {@ApiResponse(code = 0, message = "成功", response = BaseResponse.class)})
    @PostMapping(value = URL_PREPAYMENT)
    @ResponseBody
    public BaseResponse prepayment(HttpServletRequest request, HttpServletResponse response,
                                   @Validated @ModelAttribute Prepayment model, BindingResult bindingResult) {
        String startTime = DateUtils.getCurrentTimeMillis();
        String ip = IpUtil.getIpAddress(request);
        log.info("PrepaymentController#prepayment,  请求时间:{}, 请求ip:{}, 请求参数:{}", startTime, ip, model.toString());
        BaseResponse baseResponse = getValidatedResult(bindingResult);
        if (baseResponse != null) {
            log.info("PrepaymentController#prepayment, validate failed, error message:{}, 请求参数:{}", baseResponse.getMessage(), model.toString());
            return baseResponse;
        }
        CheckPrepayment.check(model);
        Contract contract = contractApiHandler.getContractBy(model.getUniqueId(), model.getContractNo());
        contractApiHandler.checkAndReturnFinancialContract(model.getFinancialProductCode(), contract);
        CheckPrepayment.checkInterestAndPrincipal(model, weiFangProperties.isEnable(), sandboxDataSetHandler, productCategoryCacheHandler);
        prepaymentApiHandler.checkRequestNo(model.getRequestNo(), ip);

        String contractUuid = contract.getUuid();
        int oldVersionNo = contract.getActiveVersionNo();
        Integer hasDeducted = model.getHasDeducted();
        String repaymentPlanNo = prepaymentHandler.apiPrepayment(contractUuid, model, oldVersionNo, ip, Priority.High.getPriority());
        String repayScheduleNo = model.getRepayScheduleNo();
        Map<String, Object> resultMap = buildResultMap(repaymentPlanNo, repayScheduleNo);
        return wrapHttpServletResponse(response, ApiMessage.SUCCESS, resultMap);
    }

    /**
     * 创建提前还款接口返回数据Map
     *
     * @param repaymentPlanNo 五维还款计划编号
     * @param repayScheduleNo 商户还款计划编号
     * @return 提前还款接口返回数据Map
     */
    private Map<String, Object> buildResultMap(String repaymentPlanNo, String repayScheduleNo) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("repaymentPlanNo", repaymentPlanNo);
        if (StringUtils.isNotEmpty(repayScheduleNo)) {
            resultMap.put("repayScheduleNo", repayScheduleNo);
        }
        return resultMap;
    }

}
