package com.suidifu.morganstanley.controller.api.order;

import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.gluon.resolver.JsonViewResolver;
import com.zufangbao.wellsfargo.yunxin.handler.PlanOrderAndSinglePaymentModelHandler;
import com.zufangbao.wellsfargo.yunxin.model.paymentOrder.PlanOrderAndSinglePaymentModelResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by zj on 17-11-30.
 */
@RestController
@RequestMapping("/ledgerbook")
public class LedgerBookPlanOrderAndSinglePaymentController {

    private static Log log = LogFactory.getLog
            (LedgerBookPlanOrderAndSinglePaymentController.class);
    @Autowired
    private PlanOrderAndSinglePaymentModelHandler planOrderAndSinglePaymentModelHandler;

    @Autowired
    private JsonViewResolver jsonViewResolver;

    @RequestMapping(value = "/queryOrderPlan", method = RequestMethod.POST)
    public @ResponseBody
    String queryPlanOrderAndSinglePaymentStatus
            (HttpServletRequest request, HttpServletResponse response,
             String queryType,String queryUuidList) {
        log.info("queryType:["+queryType+"]");
        try {
            if (StringUtils.isBlank(queryType) || StringUtils.isBlank(queryUuidList)) {
                return jsonViewResolver.errorJsonResult(4001,"请求数据为空!!!");
            }
            List<String> uuidLIst= JsonUtils.parseArray(queryUuidList,String.class);
            if (CollectionUtils.isEmpty(uuidLIst)) {
                return jsonViewResolver.errorJsonResult(4002,
                        "查询类型错误Or查询条件为空!!!");
            }
            log.info("请求参数数量:["+uuidLIst.size()+"]");
            List<PlanOrderAndSinglePaymentModelResponse> result =
                    planOrderAndSinglePaymentModelHandler.queryRemittanceOrderPlanORSinglePayment(queryType,uuidLIst);
            if (null == result){
                return jsonViewResolver.errorJsonResult(4002,"查询条件错误!!!");
            }
            log.info("SUC return number:["+result.size()+"]");
            return jsonViewResolver.sucJsonResult("rspPlanOrder",result);
        } catch (Exception exception) {
            log.info(ExceptionUtils.getFullStackTrace(exception));
            return  jsonViewResolver.errorJsonResult(4003,"系统错误!!!");
        }
    }
}

