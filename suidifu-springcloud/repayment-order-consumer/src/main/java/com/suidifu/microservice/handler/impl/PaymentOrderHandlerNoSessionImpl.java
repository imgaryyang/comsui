package com.suidifu.microservice.handler.impl;

import com.demo2do.core.entity.Result;
import com.suidifu.microservice.handler.CashFlowAutoIssueHandler;
import com.suidifu.microservice.handler.PaymentOrderHandlerNoSession;
import com.suidifu.owlman.microservice.enumation.PaymentCondition;
import com.suidifu.owlman.microservice.exception.PaymentOrderException;
import com.suidifu.owlman.microservice.handler.PaymentOrderHandler;
import com.suidifu.owlman.microservice.model.PaymentOrderSubmitModel;
import com.zufangbao.sun.entity.account.AuditStatus;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.repayment.order.PayWay;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.PaymentOrderService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("paymentOrderHandlerNoSession")
public class PaymentOrderHandlerNoSessionImpl implements PaymentOrderHandlerNoSession {
	private static Log logger = LogFactory.getLog(PaymentOrderHandlerNoSessionImpl.class);

	@Autowired
    CashFlowService cashFlowService;

	@Autowired
    FinancialContractService financialContractService;

	@Autowired
    PaymentOrderService paymentOrderService;

	@Autowired
	private RepaymentOrderService repaymentOrderService;

	@Autowired
	private RepaymentOrderItemService repaymentOrderItemService;

	@Autowired
	private CashFlowAutoIssueHandler cashFlowAutoIssueHandler;

	@Autowired
	private PaymentOrderHandler paymentOrderHandler;


	/**
	 * 07/06/15目前关联流水,自动充值都是取流水的全部金额
	 */
	@Override
	public Result submit(PaymentOrderSubmitModel model) throws PaymentOrderException {
		Result result = new Result().success();
		if (null == model)
			throw new PaymentOrderException("系统错误");
		if (org.apache.commons.lang.StringUtils.isBlank(model.getRemark())){
			throw new PaymentOrderException("备注不能为空");
		}
		List<CashFlow> cashFlowList = checkAndGetCashFlowList(model.getCashFlowList());
		RepaymentOrder order = checkAndGetRepaymentOrder(model, cashFlowList);
		PayWay payWay = checkAndGetPayWay(model);
		for(CashFlow cashFlow : cashFlowList){
			String paymentOrderUuid=paymentOrderHandler.savePaymentOrderAndUpdateOrder(payWay, order, cashFlow, model.getRemark());
			result.data(paymentOrderUuid,getMessage(order.getOrderUuid(), cashFlowAutoIssue(cashFlow, order)));
		}
		result.setMessage("还款订单支付成功");
		return result;
	}

	private PayWay checkAndGetPayWay(PaymentOrderSubmitModel model) throws PaymentOrderException {
		PayWay payWay = EnumUtil.fromOrdinal(PayWay.class, model.getPayWay());
		if (null == payWay)
			throw new PaymentOrderException("支付方式错误!");
		return payWay;
	}

	private PaymentCondition cashFlowAutoIssue(CashFlow cashFlow, RepaymentOrder order) {
		String orderUuid = order.getOrderUuid();
		if (AuditStatus.ISSUED.equals(cashFlow.getAuditStatus())) {
			return PaymentCondition.还款订单支付成功;
		}
		if (AuditStatus.CREATE.equals(cashFlow.getAuditStatus())
				|| AuditStatus.CLOSE.equals(cashFlow.getAuditStatus())) {
			List<String> contractUuids = repaymentOrderItemService.get_item_contract_uuid_list(orderUuid);
			if (CollectionUtils.isEmpty(contractUuids)
					|| (contractUuids.stream().distinct().collect(Collectors.toList()).size() != 1)) {
				return PaymentCondition.还款订单支付成功但是流水无法自动充值到虚户;
			}
			return cashFlowAutoIssueHandler.paymentOrderRecharge(cashFlow, order, contractUuids.get(0),
					financialContractService.getFinancialContractBy(order.getFinancialContractUuid()));
		}
		return PaymentCondition.还款订单支付成功但是流水无法自动充值到虚户;
	}

	private String getMessage(String orderUuid, PaymentCondition condition) {
		StringBuilder builder = new StringBuilder("");
		switch (condition) {
		case 还款订单支付成功:
			return builder.append("还款订单:[").append(orderUuid).append("]支付成功!").toString();
		case 还款订单支付成功但是流水无法自动充值到虚户:
			return builder.append("还款订单:[").append(orderUuid).append("]支付成功,流水无法自动充值到虚户,请手动完成充值!").toString();
		case 还款订单支付成功流水自动充值到虚户中:
			return builder.append("还款订单:[").append(orderUuid).append("]支付成功,流水自动充值到对应虚户中...").toString();
		}
		return "";
	}

	private List<CashFlow> checkAndGetCashFlowList(List<String> cashFlowUuidList) throws PaymentOrderException{
		if(CollectionUtils.isEmpty(cashFlowUuidList)){
			throw new PaymentOrderException("提交的流水信息不能为空");
		}
		Set<String> cashFlowUuidSet = cashFlowUuidList.stream().collect(Collectors.toSet());
		if(cashFlowUuidList.size() != cashFlowUuidSet.size()){
			throw new PaymentOrderException("提交的流水信息有重复");
		}
		List<CashFlow> cashFlows = new ArrayList<CashFlow>();
		for(String cashFlowUuid : cashFlowUuidList){
			CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
			if (null == cashFlow)
				throw new PaymentOrderException("找不到"+cashFlowUuid+"流水");
			cashFlows.add(cashFlow);
		}
		return cashFlows;
	}

	private RepaymentOrder checkAndGetRepaymentOrder(PaymentOrderSubmitModel model, List<CashFlow> cashFlowList)
			throws PaymentOrderException {
		RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid(model.getOrderUuid());
		if (null == order)
			throw new PaymentOrderException("找不到还款订单");
		BigDecimal totalCashFlow = new BigDecimal(0);
		for(CashFlow cashFlow : cashFlowList){
			if(StringUtils.isNotEmpty(model.getCounterAccountName()) && StringUtils.isNotEmpty(cashFlow.getCounterAccountName())){
				if(!model.getCounterAccountName().equals(cashFlow.getCounterAccountName())){
					throw new PaymentOrderException("付款方户名与选中流水账户名不一致");
				}
			}
			if(StringUtils.isNotEmpty(model.getCounterAccountName()) && StringUtils.isEmpty(cashFlow.getCounterAccountName())){
				throw new PaymentOrderException("付款方户名与选中流水账户名不一致");
			}

			if(StringUtils.isNotEmpty(cashFlow.getStringFieldOne()))
				throw new PaymentOrderException("流水已支付了其他还款订单");
			totalCashFlow = totalCashFlow.add(cashFlow.getTransactionAmount());
		}
		BigDecimal successPayingAmount = paymentOrderService.getPaySuccessAndPayingPaymentOrdersAmount(order.getOrderUuid());
		BigDecimal unPaidAmount = order.getOrderAmount().subtract(successPayingAmount);
		if(totalCashFlow.compareTo(unPaidAmount) > 0)
			throw new PaymentOrderException("需支付金额(订单总金额-已支付金额)应大于流水金额");
		if (order.isCanNotBePaid())
			throw new PaymentOrderException("该还款订单不能被支付");
		return order;
	}
}
