package com.suidifu.morganstanley.controller.api.repayment;

import com.suidifu.matryoshka.handler.ProductCategoryCacheHandler;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.morganstanley.configuration.bean.weifang.WeiFangProperties;
import com.suidifu.morganstanley.controller.base.BaseApiController;
import com.suidifu.morganstanley.handler.repayment.AssetSetHandler;
import com.suidifu.morganstanley.model.request.repayment.CheckModifyRepaymentPlan;
import com.suidifu.morganstanley.model.request.repayment.ModifyRepaymentPlan;
import com.suidifu.morganstanley.model.response.BaseResponse;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.sun.api.model.repayment.RepaymentPlanModifyDetail;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.wellsfargo.api.util.IpUtils;
import com.zufangbao.wellsfargo.yunxin.handler.ContractApiHandler;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_API_V3;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_MODIFY_REPAYMENT_PLAN;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/9/25 <br>
 * Time:下午6:01 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@RestController
@RequestMapping(URL_API_V3)
@Api(tags = {"五维金融贷后接口3.0"}, description = " ")
@Log4j2
public class ModifyRepaymentPlanController extends BaseApiController {
    @Resource
    private AssetSetHandler assetSetHandler;

    @Resource
    private ContractApiHandler contractApiHandler;

    @Autowired
    private SandboxDataSetHandler sandboxDataSetHandler;

    @Autowired
    private WeiFangProperties weiFangProperties;

    @Autowired
    private ProductCategoryCacheHandler productCategoryCacheHandler;

    /**
     * 变更还款计划
     *
     * @param model    还款计划变更模型
     * @param request  http请求
     * @param response http响应
     * @return 返回变更的结果信息，包括成功和其他各种失败情况
     */
    @ApiOperation(value = "变更还款计划", notes = "变更还款计划", httpMethod = "POST", response = BaseResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 0, message = "成功", response = BaseResponse.class)})
    @PostMapping(value = URL_MODIFY_REPAYMENT_PLAN)
    @ResponseBody
    public BaseResponse modifyRepaymentPlan(HttpServletRequest request,
                                            HttpServletResponse response,
                                            @ApiParam(value = "变更还款计划", required = true)
                                            @Validated @ModelAttribute ModifyRepaymentPlan model,
                                            BindingResult bindingResult) {
        log.info("\n开始调用变更还款计划接口, 请求参数：\n[uniqueId: {}, \ncontractNo：{}\nrequestNo：{}\nrequestReason：{} \nrepayment plan json data：{}\nip：{}]\n",
                model.getUniqueId(),
                model.getContractNo(),
                model.getRequestNo(),
                model.getRequestReason(),
                model.getRequestData(),
                IpUtils.getIpAddress(request));

        // 判断要变更的数据中是否存在异常数据
        BaseResponse baseResponse = getValidatedResult(bindingResult);
        if (baseResponse != null) {
            return baseResponse;
        }

        CheckModifyRepaymentPlan check = new CheckModifyRepaymentPlan();
        check.hasErrors(model);
        check.checkInterestAndPrincipal(model, weiFangProperties.isEnable(), sandboxDataSetHandler, productCategoryCacheHandler);
        //check.checkInterestAndPrincipalForPayment(model, weiFangProperties.isEnable(), sandboxDataSetHandler, productCategoryCacheHandler);

        // 获得变更的还款计划所属的贷款合同
        Contract contract = contractApiHandler.getContractBy(model.getUniqueId(), model.getContractNo());
        if (null == contract) {
            return wrapHttpServletResponse(response, ApiMessage.CONTRACT_NOT_EXIST);
        }

        int oldActiveVersionNo = contractApiHandler.getOldActiveVersionNo(contract);
        // 修改还款计划，返回修改后的还款计划集合
        List<RepaymentPlanModifyDetail> details = assetSetHandler.modifyRepaymentPlan(contract, oldActiveVersionNo, model, IpUtils.getIpAddress(request));

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("repaymentPlanList", details);

        return wrapHttpServletResponse(response, ApiMessage.SUCCESS, resultMap);
    }
}