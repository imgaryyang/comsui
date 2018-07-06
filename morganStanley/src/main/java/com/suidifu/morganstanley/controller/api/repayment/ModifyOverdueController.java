package com.suidifu.morganstanley.controller.api.repayment;

import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_API_V3;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_MODIFY_OVER_DUE_FEE;

import com.suidifu.morganstanley.controller.base.BaseApiController;
import com.suidifu.morganstanley.handler.repayment.OverdueFeeApiHandler;
import com.suidifu.morganstanley.model.request.repayment.CheckModifyOverdueFee;
import com.suidifu.morganstanley.model.request.repayment.ModifyOverdueFee;
import com.suidifu.morganstanley.model.request.repayment.OverdueFeeDetail;
import com.suidifu.morganstanley.model.response.BaseResponse;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.api.model.modify.ModifyOverdueParams;
import com.zufangbao.sun.utils.DateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 更新逾期费用明细接口
 * @author louguanyang at 2017/9/25 17:47
 */
@RestController
@RequestMapping(URL_API_V3)
@Api(tags = {"五维金融贷后接口3.0"}, description = " ")
@Log4j2
public class ModifyOverdueController extends BaseApiController {

    @Resource
    private OverdueFeeApiHandler overdueFeeApiHandler;

    /**
     * 更新逾期费用明细接口
     *
     * @param request       Http请求
     * @param response      Http响应
     * @param model         请求参数Model
     * @param bindingResult Validate结果
     * @return 响应结果
     */
    @ApiOperation(value = "更新逾期费用明细接口", notes = "更新逾期费用明细接口", httpMethod = "POST", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 0, message = "成功", response = BaseResponse.class)})
    @PostMapping(value = URL_MODIFY_OVER_DUE_FEE)
    @ResponseBody
    public BaseResponse modifyOverdueFee(HttpServletRequest request, HttpServletResponse response,
                                         @Validated @ModelAttribute ModifyOverdueFee model, BindingResult bindingResult) {
        long start = 0, end;

        if (log.isDebugEnabled()) {
            start = System.currentTimeMillis();
        }

        String startTime = DateUtils.getCurrentTimeMillis();
        String ip = IpUtil.getIpAddress(request);
        String requestNo = model.getRequestNo();

        log.info("更新逾期费用明细接口, 请求时间:{}, 请求ip:{}, 请求参数:{}", startTime, ip, model.toString());
        BaseResponse baseResponse = getValidatedResult(bindingResult);
        if (baseResponse != null) {
            return baseResponse;
        }

        if (log.isDebugEnabled()) {
            end = System.currentTimeMillis();
            log.debug("modifyOverdueFee#getValidatedResult, usage:{}, currentTimeMillis:{}, requestNo:{}", (end - start), end, requestNo);
            start = end;
        }

        CheckModifyOverdueFee.check(model);

        if (log.isDebugEnabled()) {
            end = System.currentTimeMillis();
            log.debug("modifyOverdueFee#CheckModifyOverdueFee, usage:{}, currentTimeMillis:{}, requestNo:{}", (end - start), end, requestNo);
            start = end;
        }

        overdueFeeApiHandler.checkRequestNo(requestNo, ip);

        if (log.isDebugEnabled()) {
            end = System.currentTimeMillis();
            log.debug("modifyOverdueFee#checkRequestNo, usage:{}, currentTimeMillis:{}, requestNo:{}", (end - start), end, requestNo);
            start = end;
        }

        String requestData = model.getModifyOverDueFeeDetails();
        overdueFeeApiHandler.saveLog(requestNo, requestData, null);

        if (log.isDebugEnabled()) {
            end = System.currentTimeMillis();
            log.debug("modifyOverdueFee#saveLog, usage:{}, currentTimeMillis:{}, requestNo:{}", (end - start), end, requestNo);
            start = end;
        }

        List<OverdueFeeDetail> overdueFeeDetails = model.getDetailList();
        List<ModifyOverdueParams> paramsList = overdueFeeApiHandler.verifyAndReturnParamsList(overdueFeeDetails);

        if (log.isDebugEnabled()) {
            end = System.currentTimeMillis();
            log.debug("modifyOverdueFee#verifyAndReturnParamsList, usage:{}, currentTimeMillis:{}, requestNo:{}", (end - start), end, requestNo);
            start = end;
        }

        List<Map<String, String>> failList = overdueFeeApiHandler.modifyOverdueFee(paramsList);

        if (log.isDebugEnabled()) {
            end = System.currentTimeMillis();
            log.debug("modifyOverdueFee#modifyOverdueFee, usage:{}, currentTimeMillis:{}, requestNo:{}", (end - start), end, requestNo);
            start = end;
        }

        if (CollectionUtils.isEmpty(failList)) {

            if (log.isDebugEnabled()) {
                end = System.currentTimeMillis();
                log.debug("modifyOverdueFee#wrapHttpServletResponse#emptyFailList, usage:{}, currentTimeMillis:{}, requestNo:{}", (end - start), end, requestNo);
            }

            return wrapHttpServletResponse(response, ApiMessage.SUCCESS);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("repaymentPlanNoList", failList);
        BaseResponse wrapHttpServletResponse = wrapHttpServletResponse(response, ApiMessage.SUCCESS, resultMap);

        if (log.isDebugEnabled()) {
            end = System.currentTimeMillis();
            log.debug("modifyOverdueFee#wrapHttpServletResponse, usage:{}, currentTimeMillis:{}, requestNo:{}", (end - start), end, requestNo);
        }

        return wrapHttpServletResponse;
    }

}
