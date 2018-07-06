package com.suidifu.microservice.handler.impl;

import com.suidifu.microservice.handler.CashFlowAutoIssueV2_0Handler;
import com.suidifu.microservice.handler.ThirdPartVoucherV2_0Handler;
import com.suidifu.owlman.microservice.handler.ThirdPartyVoucherRepaymentOrderWithReconciliation;
import com.zufangbao.sun.entity.repayment.order.OrderRecoverResult;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderItem;
import com.zufangbao.sun.service.PaymentOrderService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

/**
 * Created by zhusy on 2018/2/27.
 */
@Component("thirdPartyVoucherRepaymentOrderWithReconciliation")
@Log4j2
public class ThirdPartyVoucherRepaymentOrderWithReconciliationImpl implements
    ThirdPartyVoucherRepaymentOrderWithReconciliation {

  @Resource
  private CashFlowAutoIssueV2_0Handler cashFlowAutoIssueV2_0Handler;
  @Resource
  private DeductApplicationService deductApplicationService;
  @Resource
  private ThirdPartVoucherV2_0Handler thirdPartVoucherV2_0Handler;
  @Resource
  private RepaymentOrderService repaymentOrderService;
  @Resource
  private PaymentOrderService paymentOrderService;
  @Resource
  private RepaymentOrderItemService repaymentOrderItemService;


  @Override
  public void generateThirdPartVoucherWithReconciliation(String contractUuid, String repaymentOrderUuid, String paymentOrderUuid, int priority) {
    thirdPartVoucherWithReconciliation(repaymentOrderUuid, paymentOrderUuid);
  }

  public void thirdPartVoucherWithReconciliation(String repaymentOrderUuid,String paymentOrderUuid){
    log.info("begin to  generate journal_voucher and audit repaymentOrderUuid[" + repaymentOrderUuid	+ "],paymentOrderUuid["+paymentOrderUuid+"]");
    RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUuid(repaymentOrderUuid);
    if (null == repaymentOrder || repaymentOrder.getOrderRecoverResult()== OrderRecoverResult.ALL ){ //核销结果：NONE:未核销,PART:部分核销,ALL:全部核销.
      return;
    }
    PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid(paymentOrderUuid);

    if (null == paymentOrder || paymentOrder.isPaySuc()==false ){ //核销结果：NONE:未核销,PART:部分核销,ALL:全部核销.
      return;
    }
    if(isReceivableInAdvance(repaymentOrderUuid, repaymentOrder, paymentOrder)){
      try{
        cashFlowAutoIssueV2_0Handler.recover_receivable_in_advance(repaymentOrderUuid, paymentOrder.getUuid());
      } catch(Exception e){
        log.error("generate journal_voucher and audit(isReceivableInAdvance)occur error ,repaymentOrderUuid[" + repaymentOrderUuid	+ "],paymentOrderUuid["+paymentOrderUuid+"],msg:"+ExceptionUtils.getFullStackTrace(e));
      }
    }else {
      nonReceivableInAdvance(repaymentOrderUuid, repaymentOrder, paymentOrder);
    }
    log.info("end to  generate journal_voucher and audit repaymentOrderUuid[" + repaymentOrderUuid	+ "],paymentOrderUuid["+paymentOrderUuid+"]");
  }

  private void nonReceivableInAdvance(String repaymentOrderUuid, RepaymentOrder repaymentOrder, PaymentOrder paymentOrder) {
    String sourceDocumentUuid = null;
    try {
      log.info("begin to generate  third part journal voucher repaymentOrderUuid [" + repaymentOrderUuid
          + "]");
      sourceDocumentUuid = thirdPartVoucherV2_0Handler
          .createDeductRepaymentOrderSourceDocumentUuid(repaymentOrder,paymentOrder);
      log.info("end to generate  third part journal voucher repaymentOrderUuid [" + repaymentOrderUuid
          + "]");
    } catch (Exception e) {
      log.error("occur error when loop gengerate third part voucher repaymentOrderUuid ["
          + repaymentOrderUuid + "],with the full error stack:["+ExceptionUtils.getFullStackTrace(e)+"].");
      e.printStackTrace();
    }
    if (StringUtils.isEmpty(sourceDocumentUuid)){
      log.error("recover_assets_online_deduct_by_interface_each_deduct_application,error sourceDocumentUuid is empty");
      return;
    }
    try {
      log.info("begin to recover_assets_online_deduct_by_interface_each_deduct_application,sourceDocumentUuid["
          + sourceDocumentUuid + "],repaymentOrderUuid["+repaymentOrderUuid+"].");

      cashFlowAutoIssueV2_0Handler
          .recover_assets_repayment_order_deduct(sourceDocumentUuid);
      log.info("end to recover_assets_online_deduct_by_interface_each_deduct_application,sourceDocumentUuid["
          + sourceDocumentUuid + "],repaymentOrderUuid["+repaymentOrderUuid+"].");
    } catch (Exception e) {
      log.error(
          "occur error when recover_assets_online_deduct_by_interface_each_deduct_application,sourceDocumentUuid["
              + sourceDocumentUuid + "],repaymentOrderUuid["+repaymentOrderUuid+"],with the full error stack:["+ ExceptionUtils
              .getFullStackTrace(e)+"].");
      e.printStackTrace();
    }
  }

  private boolean isReceivableInAdvance(String repaymentOrderUuid, RepaymentOrder repaymentOrder, PaymentOrder paymentOrder) {

    List<RepaymentOrderItem> items=repaymentOrderItemService.getRepaymentOrderItems(repaymentOrder.getOrderUuid());
    RepaymentOrderItem item=items.get(0);
    if(item.isReceivableInAdvance()) {
      return true;
    }else {
      return false;
    }
  }
}
