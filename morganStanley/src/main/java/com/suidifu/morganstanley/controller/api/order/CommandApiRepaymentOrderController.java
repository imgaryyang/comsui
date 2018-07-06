package com.suidifu.morganstanley.controller.api.order;

import com.suidifu.hathaway.job.Priority;
import com.suidifu.morganstanley.controller.base.BaseApiController;
import com.suidifu.morganstanley.handler.RepaymentOrderApplicationHandler;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.api.model.repayment.OrderRequestModel;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationCode;
import com.zufangbao.sun.entity.repayment.order.OrderAliveStatus;
import com.zufangbao.sun.entity.repayment.order.OrderModifyType;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderSourceStatus;
import com.zufangbao.sun.entity.repayment.order.TransType;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.wellsfargo.yunxin.handler.RepaymentOrderForSingleContractHandlerProxy;
import com.zufangbao.wellsfargo.yunxin.handler.RepaymentOrderHandler;
import com.zufangbao.wellsfargo.yunxin.model.RepaymentOrderQueryResult;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v2")
@Api(tags = {"五维金融贷后接口2.0"}, description = " ")
@Log4j2
public class CommandApiRepaymentOrderController extends BaseApiController {
    @Value("${orderNotifyUrl}")
    private String orderNotifyUrl;

    @Autowired
    private RepaymentOrderApplicationHandler repaymentOrderApplicationHandler;

    @Autowired
    private RepaymentOrderForSingleContractHandlerProxy repaymentOrderForSingleContractHandlerProxy;

    @Autowired
    private FinancialContractConfigurationService financialContractConfigurationService;

    @Autowired
    private RepaymentOrderHandler repaymentOrderHandler;
    
    @Autowired
    private RepaymentOrderService repaymentOrderService;


    @Value("${yx.api.order_detail_path}")
    private String YX_API_ORDER_DETAIL_PATH = "";

    private boolean isReceivableInAdvance(String financialContractNo) {
        return financialContractConfigurationService.isConfigedByFinancialContractNo(financialContractNo, FinancialContractConfigurationCode.IS_REPAYMENT_ORDER_RECEIVED_IN_ADVANCE.getCode());
    }
    //还款订单变更  业务金额明细是否可为空
    private boolean isAllowDetailsAmountEmptyForModify(String financialContractNo) {
        return financialContractConfigurationService.isConfigedByFinancialContractNo(financialContractNo, FinancialContractConfigurationCode.ALLOW_REPAYMENT_ORDER_MODIFY_DETAIL_CONFIRM.getCode());
    }
    
    /**
     * 执行还款单下单
     * @return
     */
    @RequestMapping(value = "/repaymentOrder", method = RequestMethod.POST, params = "transType=0")
    public @ResponseBody
    String addRepaymentOrder(
            HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute OrderRequestModel commandModel) {

        return handlerRepaymentOrder(request, response, commandModel, TransType.ORDER_TRANS_TYPE_ADD);
    }

    @RequestMapping(value = "/repaymentOrder", method = RequestMethod.POST, params = "transType=1")
    public @ResponseBody
    String cancelRepaymentOrder(
            HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute OrderRequestModel commandModel) {

        return handlerRepaymentOrder(request, response, commandModel, TransType.ORDER_TRANS_TYPE_CANCEL);
    }
    
    @RequestMapping(value = "/repaymentOrder", method = RequestMethod.POST, params = "transType=3")
    public @ResponseBody
    String modifyRepaymentOrder(
            HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute OrderRequestModel commandModel) {

        return handlerRepaymentOrder(request, response, commandModel, TransType.ORDER_TRANS_TYPE_MODIFY);
    }

    @RequestMapping(value = "/repaymentOrder", method = RequestMethod.POST)
    public @ResponseBody
    String repaymentOrderForNoTransType(
            HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute OrderRequestModel commandModel) {

        return signErrorResult(response, ApiResponseCode.ORDER_TRANS_TYPE_PARSE_ERROR);
    }

    private String handlerRepaymentOrder(HttpServletRequest request, HttpServletResponse response,
                                         OrderRequestModel commandModel, TransType transType) {
        String oppositeKeyWord = "[requestNo=" + commandModel.getOrderRequestNo() + ",orderUniqueId=" + commandModel.getOrderUniqueId() + ",transType=" + commandModel.getTransType() + "]";
        try {
            log.info(GloableLogSpec.AuditLogHeaderSpec() + GloableLogSpec.REPAYMENT_ORDER_FUNCTION_POINT.RECV_TRADE_REQUEST_FROM_OUTLIER_SYSTEM + oppositeKeyWord);
            String ip = IpUtil.getIpAddress(request);
            String merchantId = getMerchantId(request);
            Map<String, String> result = new HashMap<>();
            commandModel.configDefaultNotifyUrl(orderNotifyUrl);
            if (transType == TransType.ORDER_TRANS_TYPE_ADD) {
                result = addRepaymentOrder(commandModel, ip, merchantId, oppositeKeyWord);
            } else if (transType == TransType.ORDER_TRANS_TYPE_CANCEL) {
                result = cancelRepaymentOrder(commandModel, ip, merchantId);
            }else if (transType == TransType.ORDER_TRANS_TYPE_MODIFY) {
                result = repaymentOrderHandler.modifyRepaymentOrder(commandModel, ip, merchantId,oppositeKeyWord,YX_API_ORDER_DETAIL_PATH);
            }
            log.info(GloableLogSpec.AuditLogHeaderSpec() + GloableLogSpec.REPAYMENT_ORDER_FUNCTION_POINT.RECV_TRADE_REQUEST_FROM_OUTLIER_SYSTEM + oppositeKeyWord + "[SUC]");
            return signSucResult(response, "data", result);
        } catch (Exception e) {
            log.error(GloableLogSpec.AuditLogHeaderSpec() + GloableLogSpec.REPAYMENT_ORDER_FUNCTION_POINT.RECV_TRADE_REQUEST_FROM_OUTLIER_SYSTEM + oppositeKeyWord
                    + "[ERR:" + e.getMessage() + "],"+ExceptionUtils.getFullStackTrace(e));
            return signErrorResult(response, e);
        }
    }
    
    private Map<String, String> addRepaymentOrder(OrderRequestModel commandModel, String ip, String merchantId, String oppositeKeyWord) {
        //判断为空校验
        if (!commandModel.isValid(isReceivableInAdvance(commandModel.getFinancialContractNo()), true,isAllowDetailsAmountEmptyForModify(commandModel.getFinancialContractNo()))) {
            log.info(GloableLogSpec.AuditLogHeaderSpec() + GloableLogSpec.REPAYMENT_ORDER_FUNCTION_POINT.RECV_TRADE_REQUEST_FROM_OUTLIER_SYSTEM + oppositeKeyWord +
                    ",check fail, msg[" + commandModel.getCheckFailedMsg() + "]");
            throw new ApiException(ApiResponseCode.INVALID_PARAMS, commandModel.getCheckFailedMsg());
        }
        //数据校验
        repaymentOrderHandler.addRepaymentOrderInfoCheck(commandModel, merchantId);
        RepaymentOrderQueryResult handlerResult = repaymentOrderHandler.saveRepaymentOrderTotalBeforeFilterDetail(commandModel, ip, merchantId, YX_API_ORDER_DETAIL_PATH, RepaymentOrderSourceStatus.ORDINARY,OrderModifyType.ORDINARY_ORDER_TYPE);
        if (handlerResult == null) {
            throw new ApiException(ApiResponseCode.REPAYMENT_ORDER_OPT_ERROR);
        }
        RepaymentOrder repaymentOrder = handlerResult.getRepaymentOrder();

        if (repaymentOrder == null) {
            throw new ApiException(ApiResponseCode.REPAYMENT_ORDER_OPT_ERROR);
        }

        if (handlerResult.getGroupType() != null && handlerResult.getGroupType().singleContractForRepaymentWayGroupType()) {
            //线上代扣-快捷支付
            log.info("repaymentOrder of singleContract send msg[EasyPay, check and deduct]  start ,orderUuid[" + repaymentOrder.getOrderUuid() + "]");
            repaymentOrderForSingleContractHandlerProxy.repaymentOrderSingleForEasyPay(handlerResult.getContractUuid(), repaymentOrder.getOrderUuid(), Priority.High.getPriority());
            log.info("repaymentOrder of singleContract send msg[EasyPay, check and deduct] end ,orderUuid[" + repaymentOrder.getOrderUuid() + "]");
        }

        Map<String, String> result = new HashMap<>();
        addResult(result, repaymentOrder, "订单已受理");
        return result;
    }

    private Map<String, String> cancelRepaymentOrder(OrderRequestModel commandModel, String ip, String merchantId) {
        repaymentOrderApplicationHandler.cancelRepaymentOrderInfoCheck(commandModel, merchantId);
        RepaymentOrder repaymentOrder = repaymentOrderApplicationHandler.cancelRepaymentOrder(commandModel, ip, merchantId);
        if (repaymentOrder == null) {
            throw new ApiException(ApiResponseCode.REPAYMENT_ORDER_OPT_ERROR);
        }

        //单合同类型
        if (repaymentOrder.getFirstRepaymentWayGroup().singleContractForRepaymentWayGroupType()) {

            repaymentOrderForSingleContractHandlerProxy.cancelRepaymentOrderForSingleContract(repaymentOrder.getFirstContractUuid(), repaymentOrder.getOrderUuid(),
                    Priority.Medium.getPriority());
        }

        Map<String, String> result = new HashMap<>(2);
        addResult(result, repaymentOrder, "订单撤销已受理");
        return result;
    }

    private void addResult(Map<String, String> result, RepaymentOrder repaymentOrder, String resultMsg) {
        if (repaymentOrder != null) {
            result.put("orderUniqueId", repaymentOrder.getOrderUniqueId());
            result.put("orderUuid", repaymentOrder.getOrderUuid());
        }
        result.put("result", resultMsg);
    }
}
