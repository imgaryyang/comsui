package com.suidifu.morganstanley.controller.api.order;

import com.suidifu.hathaway.job.Priority;
import com.suidifu.morganstanley.controller.base.BaseApiController;
import com.suidifu.morganstanley.handler.RepaymentOrderApplicationHandler;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.util.CardBinUtil;
import com.zufangbao.sun.api.model.repayment.PaymentOrderRequestModel;
import com.zufangbao.sun.entity.repayment.order.PayWay;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.yunxin.entity.barclays.ThirdPartyTransactionRecord;
import com.zufangbao.sun.yunxin.service.CardBinService;
import com.zufangbao.sun.yunxin.service.barclays.ThirdPartyTransactionRecordService;
import com.zufangbao.wellsfargo.yunxin.handler.PaymentOrderHandler;
import com.zufangbao.wellsfargo.yunxin.handler.RepaymentOrderForSingleContractHandlerProxy;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/api/v2")
public class CommandApiPaymentOrderController extends BaseApiController {

    private static final Log logger = LogFactory.getLog(CommandApiPaymentOrderController.class);

    @Autowired
    private RepaymentOrderApplicationHandler repaymentOrderApplicationHandler;

    @Autowired
    private RepaymentOrderForSingleContractHandlerProxy repaymentOrderForSingleContractHandlerProxy;

    @Autowired
    private PaymentOrderHandler paymentOrderHandler;

    @Autowired
    private CardBinService cardBinService;
    
    @Autowired
	private ThirdPartyTransactionRecordService thirdPartyTransactionRecordService;

    /**
     * 订单支付接口
     *
     * @return
     */
    @RequestMapping(value = "/paymentOrder", method = RequestMethod.POST)
    public @ResponseBody
    String commandRepaymentOrder(
            HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute PaymentOrderRequestModel commandModel) {

    	long start = System.currentTimeMillis();
    	logger.info("async loca start---------------- ---------11111111111111111111------------------------------use times["+start + "]ms");
        String oppositeKeyWord = "[requestNo=" + commandModel.getRequestNo() + ",orderUniqueId=" + commandModel.getOrderUniqueId() + "]";
        try {
            logger.info(GloableLogSpec.AuditLogHeaderSpec() + GloableLogSpec.PAYMENT_ORDER_FUNCTION_POINT.RECV_TRADE_REQUEST_FROM_OUTLIER_SYSTEM + oppositeKeyWord);
            String merchantId = getMerchantId(request);

            RepaymentOrder repaymentOrder = null;
            PaymentOrder paymentOrder = null;

            String paymentBankCode = commandModel.getPaymentBankCode();
            if (StringUtils.isEmpty(paymentBankCode)) {
                String time = Long.toString(System.currentTimeMillis());
                logger.info("CommandApiPaymentOrderController#commandRepaymentOrder the bankCode is empty --------------------------times-------["+time+"]" );
                String paymentAccountNo = commandModel.getPaymentAccountNo();
                Map<String, String> cardBinBankCodeMap = cardBinService.getCachedCardBins();
                paymentBankCode = CardBinUtil.getBankCodeViaCardNo(cardBinBankCodeMap, paymentAccountNo);
                logger.info("CommandApiPaymentOrderController#commandRepaymentOrde the paymentBankCode is " +
                        paymentBankCode + " the account is " + paymentAccountNo + " " + time);
                commandModel.setPaymentBankCode(paymentBankCode);
            }
            //判断为空校验
            if (!commandModel.isValid()) {
                logger.info(GloableLogSpec.AuditLogHeaderSpec() + GloableLogSpec.PAYMENT_ORDER_FUNCTION_POINT.RECV_TRADE_REQUEST_FROM_OUTLIER_SYSTEM +
                        oppositeKeyWord + ",check fail, msg[" + commandModel.getCheckFailedMsg() + "]");
                return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, commandModel.getCheckFailedMsg());
            }
            //数据校验
            repaymentOrder = repaymentOrderApplicationHandler.paymentOrderInfoCheck(commandModel, merchantId);
            
            long endTime = System.currentTimeMillis();
            logger.info("checkInfo data -----------------------------------------------------use times["+(endTime - start) + "]ms"+", 当前时间  "+System.currentTimeMillis());
            
            //保存paymentOrder
            paymentOrder = paymentOrderHandler.savePaymentOrderByCommandModel(commandModel, repaymentOrder);
            
            long endTime2 = System.currentTimeMillis();
            logger.info("create paymentOrder use times["+(endTime2 - endTime) + "]ms");
            logger.info("create paymentOrder total use times["+(endTime2 - start) + "]ms"+", 当前时间  "+System.currentTimeMillis());
            
            
            if (paymentOrder == null) {
                throw new ApiException(ApiResponseCode.PAYMENT_ORDER_IS_NOT_EXIST);
            }
            
            if (commandModel.getFormatPayWay() == PayWay.MERCHANT_DEDUCT) {
                //线上代扣
            	 repaymentOrderForSingleContractHandlerProxy.onlineDeductPaymentOrderPay(repaymentOrder.getFirstContractUuid(), repaymentOrder
                        .getOrderUuid(), commandModel,paymentOrder.getUuid(), Priority.High.getPriority());
            } else if (commandModel.getFormatPayWay() == PayWay.OFFLINE_TRANSFER) {
                //线下转账
                 paymentOrderHandler.offlineTransferPaymentOrderPay(repaymentOrder.getOrderUuid(), commandModel,paymentOrder.getUuid(), Priority.High.getPriority());
            }else if(commandModel.getFormatPayWay() == PayWay.BUSINESS_DEDUCT){
				//商户代扣
				paymentOrderHandler.businessDeductPaymentOrderPay(repaymentOrder.getOrderUuid(), commandModel,paymentOrder.getUuid(), Priority.High.getPriority());
			}
            
            Map<String, String> result = new HashMap<>();
            result.put("paymentNo", commandModel.getPaymentNo());
            result.put("paymentUuid", paymentOrder.getUuid());
            result.put("resultMsg", "订单支付已受理！");
            
            long endTime3 = System.currentTimeMillis();
            logger.info("async local---------------- ---------33333333333333333------------------------------use times["+(endTime3 - endTime2) + "]ms");
            logger.info("async local-----------------------------3333333333333333-------------------------- total use times["+(endTime3 - start) + "]ms"+", 当前时间  "+System.currentTimeMillis());


            logger.info(GloableLogSpec.AuditLogHeaderSpec() + GloableLogSpec.PAYMENT_ORDER_FUNCTION_POINT.RECV_TRADE_REQUEST_FROM_OUTLIER_SYSTEM + oppositeKeyWord
                    + "[SUC]");
            return signSucResult(response, "paymentResult", result);
        } catch (UndeclaredThrowableException ute) {
            ute.printStackTrace();
            logger.error(GloableLogSpec.AuditLogHeaderSpec() + GloableLogSpec.PAYMENT_ORDER_FUNCTION_POINT.RECV_TRADE_REQUEST_FROM_OUTLIER_SYSTEM + oppositeKeyWord
                    + "[ERR:" + ute.getMessage() + "]");
            throw new ApiException(ApiResponseCode.SYSTEM_TIME_OUT);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(GloableLogSpec.AuditLogHeaderSpec() + GloableLogSpec.PAYMENT_ORDER_FUNCTION_POINT.RECV_TRADE_REQUEST_FROM_OUTLIER_SYSTEM + oppositeKeyWord
                    + "[ERR:" + e.getMessage() + "],"+ExceptionUtils.getFullStackTrace(e));
            return signErrorResult(response, e);
        }
    }


}
