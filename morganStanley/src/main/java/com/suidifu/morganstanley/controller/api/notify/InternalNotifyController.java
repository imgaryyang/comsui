package com.suidifu.morganstanley.controller.api.notify;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.hathaway.job.Priority;
import com.suidifu.morganstanley.controller.base.BaseApiController;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.service.PaymentOrderService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.wellsfargo.yunxin.handler.PaymentOrderHandlerProxy;
import com.zufangbao.wellsfargo.yunxin.model.order.DeductReturnModel;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by zhusy on 2017/7/26.
 */
@RestController
@RequestMapping("/api/notify/internal")
@Log4j2
public class InternalNotifyController extends BaseApiController {
    @Autowired
    private PaymentOrderService paymentOrderService;
    @Autowired
    private RepaymentOrderService repaymentOrderService;

    @Autowired
    private PaymentOrderHandlerProxy paymentOrderHandlerProxy;

    /**
     * 还款订单扣款回调
     *
     * @param deductReturnModel
     * @return
     */
    @PostMapping(value = "/deduct")
    @ResponseBody
    public String notifyDeduct(@RequestBody String deductReturnModelString) {
        try {
            log.info("internal deduct notify start: deductReturnModel[{}]", deductReturnModelString);
            DeductReturnModel deductReturnModel = JsonUtils.parse(deductReturnModelString,DeductReturnModel.class);
            if (!ObjectUtils.notEqual(null, deductReturnModel)) {
                log.error("internal deduct notify parse deductReturnModel error,deductReturnModel is [{}]", deductReturnModel);
                return buildResults(Boolean.FALSE);
            }

            PaymentOrder paymentOrder = paymentOrderService.getPaymentOrderByDeductId(deductReturnModel.getOrderNo());
            if(paymentOrder.hasPayResult()){
            	log.info("internal deduct notify, paymentOrder.hasPayResult,paymentOrderUuid[{}],deductReturnModel is [{}]",paymentOrder.getUuid(),deductReturnModel);
            	return buildResults(Boolean.TRUE);
            }
            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUuid(paymentOrder.getOrderUuid());

            paymentOrderHandlerProxy.deductNotifyHandle(repaymentOrder.getFirstContractUuid(), paymentOrder.getUuid(), repaymentOrder.getOrderUuid(), deductReturnModel, Priority.High.getPriority());

            return buildResults(Boolean.TRUE);
        } catch (Exception e) {
            log.error("internal deduct notify,deductReturnModel [{}], occur error:msg[{}],stack[{}]", deductReturnModelString, ExceptionUtils.getMessage(e),ExceptionUtils.getFullStackTrace(e));
            return buildResults(Boolean.FALSE);
        }
    }

    private String buildResults(Boolean isSuc) {
    	 Map<String,Object> results = new HashMap<String,Object>();
         Map<String,Object> statusMap = new HashMap<String,Object>();
         statusMap.put("isSuccess", isSuc);
         statusMap.put("responseCode", isSuc?"0000":"1111");
         results.put("status", statusMap);
         return JsonUtils.toJsonString(results);
    }
}