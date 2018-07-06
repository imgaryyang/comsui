package com.suidifu.microservice.handler.impl;

import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.web.resolver.Page;
import com.suidifu.hathaway.job.Priority;
import com.suidifu.hathaway.mq.annotations.MqAsyncRpcMethod;
import com.suidifu.hathaway.mq.annotations.MqRpcBusinessUuid;
import com.suidifu.hathaway.mq.annotations.MqRpcPriority;
import com.suidifu.microservice.handler.CashFlowHandler;
import com.suidifu.microservice.handler.ThirdPartyVoucherRepaymentOrderWithReconciliationNoSession;
import com.suidifu.microservice.handler.TransactionApiHandler;
import com.suidifu.microservice.service.ThirdPartyPaymentVoucherDetailService;
import com.suidifu.microservice.service.ThirdPartyPaymentVoucherService;
import com.suidifu.owlman.microservice.exception.PaymentOrderException;
import com.suidifu.owlman.microservice.exception.PaymentSystemException;
import com.suidifu.owlman.microservice.handler.PaymentOrderHandler;
import com.suidifu.owlman.microservice.model.DeductReturnModel;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.exception.repayment.RepaymentOrderDeductCallBackException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.api.model.deduct.DeductApplicationExecutionStatus;
import com.zufangbao.sun.api.model.deduct.IsTotal;
import com.zufangbao.sun.api.model.deduct.RepaymentType;
import com.zufangbao.sun.api.model.repayment.PaymentOrderRequestModel;
import com.zufangbao.sun.entity.account.Account;
import com.zufangbao.sun.entity.bank.Bank;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.entity.financial.BusinessType;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.PaymentChannelSummaryInfo;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.entity.payment.order.PaymentOrderCashFlowShowModel;
import com.zufangbao.sun.entity.payment.order.PaymentOrderRecordModel;
import com.zufangbao.sun.entity.repayment.order.AliveStatus;
import com.zufangbao.sun.entity.repayment.order.OrderPayStatus;
import com.zufangbao.sun.entity.repayment.order.OrderRecoverStatus;
import com.zufangbao.sun.entity.repayment.order.PayStatus;
import com.zufangbao.sun.entity.repayment.order.PayWay;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderItem;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderPayResult;
import com.zufangbao.sun.entity.repayment.order.RepaymentStatus;
import com.zufangbao.sun.entity.repayment.order.RepaymentWayGroupType;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.ContractAccountService;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.PaymentOrderService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationDetailService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplicationDetail;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import com.zufangbao.sun.yunxin.entity.api.deduct.SourceType;
import com.zufangbao.sun.yunxin.entity.barclays.ThirdPartyTransactionRecord;
import com.zufangbao.sun.yunxin.entity.model.api.ExtraChargeSpec;
import com.zufangbao.sun.yunxin.entity.model.repaymentOrder.PaymentContext;
import com.zufangbao.sun.yunxin.entity.thirdPartVoucher.ThirdPartyPaymentVoucher;
import com.zufangbao.sun.yunxin.entity.thirdPartVoucher.ThirdPartyPaymentVoucherDetail;
import com.zufangbao.sun.yunxin.handler.PaymentChannelInformationHandler;
import com.zufangbao.sun.yunxin.service.BankService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.barclays.ThirdPartyTransactionRecordService;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

@Log4j2
@Component("paymentOrderHandler")
public class PaymentHandlerImpl implements PaymentOrderHandler {
    @Resource
    private PaymentOrderService paymentOrderService;
    @Resource
    private RepaymentOrderService repaymentOrderService;
    @Resource
    private ContractService contractService;
    @Resource
    private ContractAccountService contractAccountService;
    @Resource
    private FinancialContractService financialContractService;
    @Resource
    private CashFlowService cashFlowService;
    @Resource
    private BankService bankService;
    @Resource
    private DeductApplicationService deductApplicationService;
    @Resource
    private DeductPlanService deductPlanService;
    @Resource
    private CashFlowHandler cashFlowHandler;
    @Resource
    private RepaymentOrderItemService repaymentOrderItemService;
    @Resource
    private ThirdPartyTransactionRecordService thirdPartyTransactionRecordService;
    @Resource
    private RepaymentPlanService repaymentPlanService;
    @Resource
    private DeductApplicationDetailService deductApplicationDetailService;
    @Resource
    private ThirdPartyPaymentVoucherService thirdPartyPaymentVoucherService;
    @Resource
    private ThirdPartyPaymentVoucherDetailService thirdPartyPaymentVoucherDetailService;
    @Resource
    private PaymentChannelInformationHandler paymentChannelInformationHandler;
    @Resource
    private TransactionApiHandler transactionApiHandler;
    @Resource
    private ThirdPartyVoucherRepaymentOrderWithReconciliationNoSession thirdPartyVoucherRepaymentOrderWithReconciliationNoSession;

    @Override
    public String savePaymentOrderAndUpdateOrder(PayWay payWay, RepaymentOrder order, CashFlow cashFlow, String remark) {
        PaymentOrder paymentOrder = new PaymentOrder(payWay, order, cashFlow, remark);
        paymentOrderService.save(paymentOrder);

        cashFlowService.connectVoucher(cashFlow.getCashFlowUuid(), paymentOrder.getUuid());

        log.info("生成支付订单,paymentOrderUuid:[" + paymentOrder.getUuid() + "].orderUuid:[" + order.getOrderUuid() + "].");

        BigDecimal paidAmount = paymentOrderService.getPaySuccessPaymentOrdersAmount(paymentOrder.getOrderUuid());
        order.refresh_paid_amount_and_update_pay_status(paidAmount);
        //支付已完成，设置资金入账时间
        if (order.getOrderPayStatus() == OrderPayStatus.PAY_END) {
            order.setCashFlowTime(paymentOrderService.getEarliestCashFlowTime(order.getOrderUuid()));
        }
        repaymentOrderService.saveOrUpdate(order);
        log.info(
                "更新还款订单状态,paymentOrderUuid:[" + paymentOrder.getUuid() + "].orderUuid:[" + order.getOrderUuid() + "].");
        return paymentOrder.getUuid();
    }

    @Override
    public PaymentOrder cancelPaymentOrder(String paymentOrderUuid) throws PaymentOrderException {
        PaymentOrder paymentOrder = checkPaymentOrder(paymentOrderUuid);
        RepaymentOrder order = getAndCheckRepaymentOrder(paymentOrder.getOrderUuid(), paymentOrderUuid);
        checkRepaymentOrderPaidAmountStatus(paymentOrder, order);

        paymentOrder.setAliveStatus(AliveStatus.CANCEL);
        paymentOrderService.update(paymentOrder);

        BigDecimal paidAmount = paymentOrderService.getPaySuccessPaymentOrdersAmount(paymentOrder.getOrderUuid());
        order.refresh_paid_amount_and_update_pay_status(paidAmount);
        repaymentOrderService.update(order);

        cashFlowService.cancelVoucherRelation(paymentOrder.getOutlierDocumentUuid());
        return paymentOrder;
    }

    public PaymentOrder checkIfPaymentOrderCanBeCancelled(String paymentOrderUuid) throws PaymentOrderException {
        PaymentOrder paymentOrder = checkPaymentOrder(paymentOrderUuid);
        RepaymentOrder order = getAndCheckRepaymentOrder(paymentOrder.getOrderUuid(), paymentOrderUuid);
        checkRepaymentOrderPaidAmountStatus(paymentOrder, order);
        return paymentOrder;
    }

    private void checkRepaymentOrderPaidAmountStatus(PaymentOrder paymentOrder, RepaymentOrder order)
            throws PaymentOrderException {
        BigDecimal paymentOrderAmount = paymentOrder.getAmount();
        if (paymentOrderAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new PaymentOrderException("该支付单金额异常(小于零),支付单金额为[" + paymentOrderAmount + "].");
        }

        if (paymentOrder.getPayWay() != PayWay.BUSINESS_DEDUCT) {
            if (order.getPaidAmount().compareTo(paymentOrder.getAmount()) < 0) {
                throw new PaymentOrderException(
                        "作废支付单后,还款订单金额异常(小于零),还款订单金额为[" + order.getPaidAmount() + "],支付订单金额为[" + paymentOrderAmount + "].");
            }
        }
    }

    private RepaymentOrder getAndCheckRepaymentOrder(String orderUuid, String paymentOrderUuid)
            throws PaymentOrderException {
        RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid(orderUuid);
        if (null == order) {
            throw new PaymentOrderException("未找到还款订单,还款订单号为:[" + orderUuid + "],支付订单号为:[" + paymentOrderUuid + "].");
        }
        RepaymentStatus repaymentStatus = order.transferToRepaymentStatus();
        if (order.getFirstRepaymentWayGroup() == RepaymentWayGroupType.BUSINESS_DEDUCT_REPAYMENT_ORDER_TYPE) {

            if (RepaymentStatus.PAYMENT_IN_PROCESS != repaymentStatus) {
                throw new PaymentOrderException(
                        "还款订单状态应为支付中,还款订单号为:[" + orderUuid + "],支付订单号为:[" + paymentOrderUuid + "].");
            }
        } else {

            if (RepaymentStatus.PAYMENT_IN_PROCESS != repaymentStatus && RepaymentStatus.PAYMENT_END != (repaymentStatus)) {
                throw new PaymentOrderException(
                        "还款订单应为支付中或支付成功状态,还款订单号为:[" + orderUuid + "],支付订单号为:[" + paymentOrderUuid + "].");
            }
        }

        if (OrderRecoverStatus.TO_PAY != order.getOrderRecoverStatus()) {
            throw new PaymentOrderException(
                    "还款订单应为未核销状态,还款订单号为:[" + orderUuid + "],支付订单号为:[" + paymentOrderUuid + "].");
        }
        return order;
    }

    private PaymentOrder checkPaymentOrder(String paymentOrderUuid) throws PaymentOrderException {
        PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid(paymentOrderUuid);
        if (null == paymentOrder) {
            throw new PaymentOrderException("未找到支付订单,支付订单号为:[" + paymentOrderUuid + "].");
        }
        if (!paymentOrder.isCanBeLapsed()) {
            throw new PaymentOrderException("作废的支付订单状态不对,支付订单号为:[" + paymentOrderUuid + "].");
        }
        return paymentOrder;
    }

    @Override
    public PaymentOrder generatePaymentOrderAndUpdayeRepaymentOrder(String financialContractUuid,
                                                                    String repaymentOrderUuid) {
        if (financialContractUuid == null || StringUtils.isEmpty(repaymentOrderUuid)) return null;

        FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);

        RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUuid(repaymentOrderUuid);

        PaymentOrder paymentorderForDb = paymentOrderService.getPaymentOrderByOrderUuidAndType(repaymentOrder.getOrderUuid(), PayWay.MERCHANT_DEDUCT_EASY_PAY);

        if (paymentorderForDb != null) {
            throw new ApiException(ApiResponseCode.REPAYMENTORDER_FOR_EXIST_PAYMENT);
        }

        Contract contract = contractService.getContract(repaymentOrder.getFirstContractUuid());
        ContractAccount contractAccount = contractAccountService.getTheEfficientContractAccountBy(contract);
        Account account = financialContract.getCapitalAccount();

        PaymentOrder paymentOrder = new PaymentOrder(PayWay.MERCHANT_DEDUCT_EASY_PAY,
                repaymentOrder, contractAccount, account, "15317312520");
        paymentOrderService.save(paymentOrder);

        //更改还款订单状态  支付中
        repaymentOrder.update_order_pay_status(OrderPayStatus.PAYING);
        repaymentOrderService.saveOrUpdate(repaymentOrder);

        return paymentOrder;
    }

    @Override
    public PaymentOrder create_payment_order(
            PaymentOrderRequestModel paymentOrderRequestModel,
            RepaymentOrder repaymentOrder) {
        String financialContractUuid = repaymentOrder.getFinancialContractUuid();

        FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
        Account account = financialContract.getCapitalAccount();
        Contract contract = contractService.getContract(repaymentOrder.getFirstContractUuid());
        ContractAccount contractAccount = contractAccountService.getTheEfficientContractAccountBy(contract);

        String receivableAccountNo = paymentOrderRequestModel.getReceivableAccountNo();
        String receivableAccountName = paymentOrderRequestModel.getReceivableAccountName();
        String receivableBankName = "";

        if (com.zufangbao.sun.utils.StringUtils.isNotEmpty(paymentOrderRequestModel.getReceivableBankCode())) {
            Bank bank = bankService.getCachedBanks().get(paymentOrderRequestModel.getReceivableBankCode());
            receivableBankName = bank.getBankName();
        }

        if (StringUtils.isEmpty(paymentOrderRequestModel.getReceivableAccountNo()) && StringUtils.isEmpty(paymentOrderRequestModel.getReceivableAccountName())
                && StringUtils.isEmpty(paymentOrderRequestModel.getReceivableBankCode())) {

            receivableAccountNo = account.getAccountNo();
            receivableAccountName = account.getAccountName();
            receivableBankName = account.getBankName();
        }

        //生成支付单
        PaymentOrder paymentOrder;
        //如果身份证号为空，则传一个默认合同账户的身份证
        if (StringUtils.isEmpty(paymentOrderRequestModel.getIdCardNum())) {
            paymentOrderRequestModel.setIdCardNum(contractAccount.getIdCardNum());
        }
        //手机没传，则传一个手机号
        if (StringUtils.isEmpty(paymentOrderRequestModel.getMobile())) {
            paymentOrderRequestModel.setMobile("15317312520");
        }
        if (StringUtils.isEmpty(paymentOrderRequestModel.getPaymentAccountNo()) && StringUtils.isEmpty(paymentOrderRequestModel.getPaymentAccountName())
                && StringUtils.isEmpty(paymentOrderRequestModel.getPaymentBankCode()) && StringUtils.isEmpty(paymentOrderRequestModel.getPaymentProvinceCode())
                && StringUtils.isEmpty(paymentOrderRequestModel.getPaymentCityCode()) && StringUtils.isEmpty(paymentOrderRequestModel.getIdCardNum())
                && StringUtils.isEmpty(paymentOrderRequestModel.getMobile())) {

            //默认
            paymentOrder = new PaymentOrder(PayWay.MERCHANT_DEDUCT, paymentOrderRequestModel, repaymentOrder.getMerId(), financialContractUuid, contractAccount,
                    receivableAccountNo, receivableAccountName, receivableBankName, repaymentOrder, PayStatus.PAY_FAIL);
        } else {
            String paymentBankName = "";
            if (StringUtils.isNotEmpty(paymentOrderRequestModel.getPaymentBankCode())) {
                Bank bank = bankService.getCachedBanks().get(paymentOrderRequestModel.getPaymentBankCode());
                paymentBankName = bank.getBankName();
            }

            paymentOrder = new PaymentOrder(PayWay.MERCHANT_DEDUCT, paymentOrderRequestModel, repaymentOrder.getMerId(), financialContractUuid,
                    receivableAccountNo, receivableAccountName, receivableBankName, repaymentOrder, paymentBankName, PayStatus.IN_PAY);
        }

        paymentOrderService.save(paymentOrder);

        repaymentOrder.update_order_pay_status(OrderPayStatus.PAYING);
        repaymentOrderService.saveOrUpdate(repaymentOrder);

        return paymentOrder;
    }

    @Override
    public List<PaymentOrderRecordModel> getPaymentOrderRecordModelsByPaymentOrder(String paymentOrderUuid, Page page) {
        if (StringUtils.isEmpty(paymentOrderUuid)) {
            return Collections.emptyList();
        }

        List<PaymentOrderRecordModel> recordModes = new ArrayList<>();

        PaymentOrder paymentOrder = paymentOrderService.getPaymenrOrderByUuid(paymentOrderUuid);

        DeductApplication deductApplication = deductApplicationService.getDeductApplicationByDeductApplicationUuid(paymentOrder.getOutlierDocumentUuid());

        List<DeductPlan> deductPlanList = new ArrayList<>();

        if (deductApplication != null) {
            deductPlanList = deductPlanService.getDeductPlansDeductApplicationUuidNum(deductApplication.getDeductApplicationUuid(), page);
        }

        for (DeductPlan deductPlan : deductPlanList) {
            PaymentOrderRecordModel model = new PaymentOrderRecordModel(deductPlan);
            recordModes.add(model);
        }

        return recordModes;
    }

    @Override
    public int getCountPaymentOrderRecordModel(String paymentOrderUuid) {
        if (StringUtils.isEmpty(paymentOrderUuid)) {
            return 0;
        }

        PaymentOrder paymentOrder = paymentOrderService.getPaymenrOrderByUuid(paymentOrderUuid);
        DeductApplication deductApplication = deductApplicationService.getDeductApplicationByDeductApplicationUuid(paymentOrder.getOutlierDocumentUuid());

        int count = 0;

        if (deductApplication != null) {
            count = deductPlanService.getDeductPlanCountDeductApplicationUuid(deductApplication.getDeductApplicationUuid());
        }

        return count;
    }

    @Override
    public List<PaymentOrderCashFlowShowModel> getCashFlowShowModels(String paymentOrderUuid) {
        if (StringUtils.isEmpty(paymentOrderUuid)) {
            return Collections.emptyList();
        }

        List<PaymentOrderCashFlowShowModel> showModelList = new ArrayList<>();

        PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid(paymentOrderUuid);

        FinancialContract financialContract = financialContractService.getFinancialContractBy(paymentOrder.getFinancialContractUuid());

        Account account = financialContract.getCapitalAccount();
        if (account == null) {
            throw new ApiException(ApiResponseCode.FINANCIAL_CONTRACT_FOR_ACCOUNT_NOT_EXIST);
        }

        //匹配出来的流水
        List<CashFlow> cashFlowList = cashFlowHandler.getCashFlowBy(account.getAccountNo(), paymentOrder, Boolean.FALSE, Boolean.FALSE);

        for (CashFlow cashFlow : cashFlowList) {

            PaymentOrderCashFlowShowModel showModel = new PaymentOrderCashFlowShowModel(cashFlow);
            showModelList.add(showModel);
        }

        return showModelList;
    }


    @Override
    public void paymentOrderMatchCashFlow(RepaymentOrder order, PaymentOrder paymentOrder, String accountNo) {

        //匹配出来的流水
        List<CashFlow> cashFlowList = cashFlowHandler.getCashFlowBy(accountNo, paymentOrder, Boolean.TRUE, Boolean.TRUE);

        //未匹配到流水 或 匹配到多条流水
        if (CollectionUtils.isEmpty(cashFlowList) || cashFlowList.size() > 1) {
            log.info("match more or empty cashFlow , orderUuid[" + order.getOrderUuid() + "]." + "paymentOrderUuid :" + paymentOrder.getUuid());
            return;
        }
        long start = System.currentTimeMillis();

        //匹配唯一条流水
        log.info("match only one cashFlow begin  , orderUuid[" + order.getOrderUuid() + "]." + "paymentOrderUuid :" + paymentOrder.getUuid() + "cashFlowUuid :" + cashFlowList.get(0).getCashFlowUuid());
        updatePaymentOrderAndOrderStatus(cashFlowList.get(0), paymentOrder, order);
        log.info("match only one cashFlow end , orderUuid[" + order.getOrderUuid() + "]." + "paymentOrderUuid :" + paymentOrder.getUuid() + "cashFlowUuid :" + cashFlowList.get(0).getCashFlowUuid());

        long endTime2 = System.currentTimeMillis();
        log.info("updatePaymentOrderAndOrderStatus total use times[" + (endTime2 - start) + "]ms");
    }

    @Override
    public void updatePaymentOrderAndRpaymentOrderFail(PaymentOrder paymentOrder, RepaymentOrder order, int paymentOrderStatus, String remark) {

        PayStatus payStatus = EnumUtil.fromOrdinal(PayStatus.class, paymentOrderStatus);

        paymentOrder.updateOrderPayResultStatus(RepaymentOrderPayResult.PAY_FAIL, payStatus, remark);
        paymentOrderService.update(paymentOrder);

        //是否足额支付
        BigDecimal paidAmount = paymentOrderService.getPaySuccessPaymentOrdersAmount(paymentOrder.getOrderUuid());
        order.refresh_paid_amount_and_update_pay_status(paidAmount);
        //支付我完成，设置资金入账时间
        if (order.getOrderPayStatus() == OrderPayStatus.PAY_END) {
            order.setCashFlowTime(paymentOrderService.getEarliestCashFlowTime(order.getOrderUuid()));
        }


        repaymentOrderService.update(order);

    }

    @Override
    public void updatePaymentOrderAndOrderStatus(CashFlow cashFlow,
                                                 PaymentOrder paymentOrder, RepaymentOrder repaymentOrder) {

        //关联支付单与流水 ,支付单支付成功
        paymentOrder.relatedCashFlowAndUpdateStatus(cashFlow, RepaymentOrderPayResult.PAY_SUCCESS, PayStatus.PAY_SUCCESS);
        paymentOrderService.update(paymentOrder);

        //stringFildOne
        cashFlowService.connectVoucher(cashFlow.getCashFlowUuid(), paymentOrder.getTradeUuid());

        //是否足额支付
        BigDecimal paidAmount = paymentOrderService.getPaySuccessPaymentOrdersAmount(paymentOrder.getOrderUuid());
        repaymentOrder.refresh_paid_amount_and_update_pay_status(paidAmount);
        //支付我完成，设置资金入账时间
        if (repaymentOrder.getOrderPayStatus() == OrderPayStatus.PAY_END) {
            repaymentOrder.setCashFlowTime(paymentOrderService.getEarliestCashFlowTime(repaymentOrder.getOrderUuid()));
        }
        repaymentOrderService.update(repaymentOrder);

    }

    @Override
    public boolean isMoreOrEmpty(String financialContractNo, String counterAccountNo,
                                 String counterAccountName, String orderUuid, BigDecimal amount) {

        FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(financialContractNo);
        Account account = financialContract.getCapitalAccount();
        if (account == null) {
            throw new ApiException(ApiResponseCode.FINANCIAL_CONTRACT_FOR_ACCOUNT_NOT_EXIST);
        }

        boolean isMoreEmpty = false;

        List<CashFlow> filteredCashFlows = cashFlowService.getFilteredCashFlowsBy(account.getAccountNo(), counterAccountNo, amount, orderUuid);
        List<CashFlow> cashFlowList = new ArrayList<CashFlow>();
        for (CashFlow cashFlow : filteredCashFlows) {
            if (org.apache.commons.lang.StringUtils.equals(cashFlow.getCounterAccountName(), counterAccountName)) {
                cashFlowList.add(cashFlow);
            }
        }
        if (CollectionUtils.isEmpty(filteredCashFlows) || cashFlowList.size() > 1 || (CollectionUtils.isEmpty(cashFlowList) && !CollectionUtils.isEmpty(filteredCashFlows))) {
            isMoreEmpty = true;
        }


        return isMoreEmpty;
    }

    @Override
    @MqAsyncRpcMethod
    public void offlineTransferPaymentOrderPay(@MqRpcBusinessUuid String repaymentOrderUuid,
                                               PaymentOrderRequestModel commandModel, String paymentOrderUuid, @MqRpcPriority int priority) throws PaymentSystemException {


        long start = System.currentTimeMillis();
        log.info("offlineTransferPaymentOrderPay berkshire  start 1 -----------------------------------------------------use times[" + (start) + "]ms" + ", 当前时间  " + System.currentTimeMillis());


        PaymentOrder paymentOrder = paymentOrderService.getPaymenrOrderByUuid(paymentOrderUuid);
        if (paymentOrder == null) {
            throw new ApiException(ApiResponseCode.PAYMENT_ORDER_IS_NOT_EXIST);
        }
        RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUuid(repaymentOrderUuid);

        if (repaymentOrder == null) {
            throw new ApiException(ApiResponseCode.REPAYMENTORDER_IS_NOT_EXISTED);
        }

        BigDecimal successPayingAmount = paymentOrderService.getPaySuccessAndPayingPaymentOrdersAmount(repaymentOrder.getOrderUuid());
        log.info("offlineTransferPaymentOrderPay, orderUuid[" + repaymentOrderUuid + "]." + "paymentOrderUuid :" + paymentOrder.getUuid() + ",successPayingAmount :" + successPayingAmount);
        if (successPayingAmount.compareTo(repaymentOrder.getOrderAmount()) > 0) {
            paymentOrder.updateOrderPayResultStatus(RepaymentOrderPayResult.PAY_FAIL, PayStatus.PAY_FAIL);
            paymentOrderService.update(paymentOrder);

            throw new PaymentSystemException("该还款订单支付有误！");
        }

        long endTime = System.currentTimeMillis();
        log.info("check successPayingAmount 2 -----------------------------------------------------use times[" + (endTime - start) + "]ms" + ", 当前时间  " + System.currentTimeMillis());


        try {

            FinancialContract financialContract = financialContractService.getFinancialContractBy(repaymentOrder.getFinancialContractUuid());

            Account account = financialContract.getCapitalAccount();

            //匹配流水
            log.info("match cashFlow begin, orderUuid[" + repaymentOrderUuid + "]." + "paymentOrderUuid :" + paymentOrder.getUuid());
            this.paymentOrderMatchCashFlow(repaymentOrder, paymentOrder, account.getAccountNo());

            log.info("businessDeductPaymentOrderPay berkshire  end 2 --------------------------22222222-2222222222-------------------------------use times[" + (System.currentTimeMillis() - start) + "]ms" + ", 当前时间  " + System.currentTimeMillis());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private PaymentOrder createPaymentOrderAndUpdateRepaymentOrder(PaymentOrderRequestModel commandModel, RepaymentOrder repaymentOrder, PayWay payWay) {

        PaymentOrder paymentOrder = null;

        String receivableBankName = "";
        String paymentBankName = "";

        if (StringUtils.isNotEmpty(commandModel.getReceivableBankCode())) {
            Bank bank = bankService.getCachedBanks().get(commandModel.getReceivableBankCode());
            receivableBankName = bank.getBankName();
        }

        if (StringUtils.isNotEmpty(commandModel.getPaymentBankCode())) {
            Bank bank = bankService.getCachedBanks().get(commandModel.getPaymentBankCode());
            paymentBankName = bank.getBankName();
        }

        //创建支付订单
        log.info("create paymentOrder, orderUuid[" + repaymentOrder.getOrderUuid() + "].");
        paymentOrder = new PaymentOrder(payWay, commandModel, receivableBankName, repaymentOrder, paymentBankName);
        paymentOrderService.save(paymentOrder);

        //还款订单 更改支付中
        log.info("update repaymentOrder paying, orderUuid[" + repaymentOrder.getOrderUuid() + "]." + "paymentOrderUuid :" + paymentOrder.getUuid());
        repaymentOrder.update_order_pay_status(OrderPayStatus.PAYING);
        repaymentOrderService.saveOrUpdate(repaymentOrder);

        return paymentOrder;

    }

    @Override
    @MqAsyncRpcMethod
    public void updatePaymentOrderAndOrder(@MqRpcBusinessUuid String repaymentOrderUuid,
                                           String paymentOrderUuid, String cashFlowUuid, @MqRpcPriority int priority) {

        try {

            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUuid(repaymentOrderUuid);

            PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid(paymentOrderUuid);

            CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);

            this.updatePaymentOrderAndOrderStatus(cashFlow, paymentOrder, repaymentOrder);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @MqAsyncRpcMethod
    public void matchCashFlowByPaymentOrder(@MqRpcBusinessUuid String repaymentOrderUuid,
                                            String paymentOrderUuid, String accountNo, @MqRpcPriority int priority) {
        try {

            RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUuid(repaymentOrderUuid);

            PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid(paymentOrderUuid);

            log.info("match cashFlow begin, orderUuid[" + repaymentOrderUuid + "]." + "paymentOrderUuid :" + paymentOrder.getUuid());
            this.paymentOrderMatchCashFlow(repaymentOrder, paymentOrder, accountNo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public PaymentContext collectPaymentOrderContextBy(String paymentOrderUuid,
                                                       String orderUuid, DeductReturnModel deductReturnModel) throws RepaymentOrderDeductCallBackException {

        PaymentContext paymentContext = null;

        PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid(paymentOrderUuid);
        RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUuid(orderUuid);

        // 1. 更新支付单状态
        PayStatus expectedPayStatus = PayStatus.getExpectedPayStatusFrom(deductReturnModel.getDeductStatusEnum());
        RepaymentOrderPayResult expectedRepaymentOrderPayResult = RepaymentOrderPayResult.getExpectedStatus(deductReturnModel.getDeductStatusEnum());
        DeductPlan sucDeductPlan = null;
        PaymentInstitutionName paymentInstitutionNameOfSucDeductPlan = null;
        if (expectedPayStatus == PayStatus.PAY_SUCCESS) {
            sucDeductPlan = deductPlanService.getDeductPlanByDeductApplicationUuid(paymentOrder.getOutlierDocumentUuid(), DeductApplicationExecutionStatus.SUCCESS);
            paymentInstitutionNameOfSucDeductPlan = sucDeductPlan == null ? null : sucDeductPlan.getPaymentGateway();
            if (sucDeductPlan == null || paymentInstitutionNameOfSucDeductPlan == null) {
                expectedPayStatus = PayStatus.IN_PAY;
                expectedRepaymentOrderPayResult = RepaymentOrderPayResult.PAY_ABNORMAL;
                log.error("internal deduct notify update paymentOrder error:sucDeductPlan[" + sucDeductPlan + "] is null or paymentInstitutionName[" + paymentInstitutionNameOfSucDeductPlan + "] is null. : expectedPayStatus[" + expectedPayStatus + "], expectedRepaymentOrderPayResult[" + expectedRepaymentOrderPayResult + "], paymentOrderUuid[" + paymentOrder.getUuid() + "], orderUuid[" + paymentOrder.getOrderUuid() + "], deductReturnModel[" + deductReturnModel + "]");
                throw new RepaymentOrderDeductCallBackException();
            }
        }

        paymentContext = new PaymentContext(paymentOrder, repaymentOrder, expectedPayStatus, expectedRepaymentOrderPayResult,
                sucDeductPlan, paymentInstitutionNameOfSucDeductPlan, deductReturnModel.getLastModifiedTimeDate(), deductReturnModel.getComment());
        return paymentContext;
    }


    @Override
    @MqAsyncRpcMethod
    public void businessDeductPaymentOrderPay(@MqRpcBusinessUuid String repaymentOrderUuid, PaymentOrderRequestModel commandModel,
                                              String paymentOrderUuid, @MqRpcPriority int priority) throws PaymentSystemException {

        long start = System.currentTimeMillis();
        log.info("businessDeductPaymentOrderPay berkshire  start 1 -----------------businessDeductPaymentOrderPay------------------------------------use times[" + (start) + "]ms" + ", 当前时间  " + System.currentTimeMillis());

        PaymentOrder paymentOrder = paymentOrderService.getPaymenrOrderByUuid(paymentOrderUuid);
        if (paymentOrder == null) {
            throw new ApiException(ApiResponseCode.PAYMENT_ORDER_IS_NOT_EXIST);
        }
        RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUuid(repaymentOrderUuid);

        if (repaymentOrder == null) {
            throw new ApiException(ApiResponseCode.REPAYMENTORDER_IS_NOT_EXISTED);
        }

        BigDecimal successPayingAmount = paymentOrderService.getPaySuccessAndPayingPaymentOrdersAmount(repaymentOrder.getOrderUuid());
        log.info("businessDeductPaymentOrderPay, orderUuid[" + repaymentOrderUuid + "]." + "paymentOrderUuid :" + paymentOrder.getUuid() + ",successPayingAmount :" + successPayingAmount);
        if (successPayingAmount.compareTo(repaymentOrder.getOrderAmount()) > 0) {
            paymentOrder.updateOrderPayResultStatus(RepaymentOrderPayResult.PAY_FAIL, PayStatus.PAY_FAIL);
            paymentOrderService.update(paymentOrder);

            throw new PaymentSystemException("该还款订单支付有误！");
        }

        long endTime = System.currentTimeMillis();
        log.info("check successPayingAmount 1 -----------------------------------------------------use times[" + (endTime - start) + "]ms" + ", 当前时间  " + System.currentTimeMillis());

        try {
            ThirdPartyTransactionRecord transactionRecord = thirdPartyTransactionRecordService.getThirdPartyTransactionRecordBytradeUuid(commandModel.getTradeUuid());

            if (transactionRecord != null) {

                //验真，生成凭证 和 deductInfo
                data_vertify_create_payment_voucher_deductInfo(paymentOrder, repaymentOrder, transactionRecord);
            } else {

                //拉交易记录
                rtFlowQueryByNotifyServer(paymentOrder);
            }

            log.info("businessDeductPaymentOrderPay berkshire  end 2 --------------------businessDeductPaymentOrderPay22222222222222222222222222222---------------------------------use times[" + (System.currentTimeMillis() - start) + "]ms" + ", 当前时间  " + System.currentTimeMillis());

        } catch (Exception e) {
            e.printStackTrace();
            log.error("occur error businessDeductPaymentOrderPay . FullStackTrace:" + ExceptionUtils.getFullStackTrace(e));
        }

    }


    @Override
    @MqAsyncRpcMethod
    public void handlerPaymentOrderForBusinessDeduct(@MqRpcBusinessUuid String repaymentOrderUuid, String tradeUuid, Integer retryTimes) {

        log.info("receive  paymentOrder businessDeduct rpc : " + "tradeUuid:" + tradeUuid);
        if (StringUtils.isEmpty(tradeUuid)) return;
        try {

            PaymentOrder paymentOrder = paymentOrderService.getPaymentOrderByTradeUuid(tradeUuid);
            ;
            ThirdPartyTransactionRecord transactionRecord = thirdPartyTransactionRecordService.getThirdPartyTransactionRecordBytradeUuid(tradeUuid);

            if (paymentOrder == null) {
                log.info("paymentOrder businessDeduct rpc paymentOrder is null 。 " + "tradeUuid:" + tradeUuid + ", repaymentOrderUuid:" + repaymentOrderUuid);
                return;
            }
            RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid(repaymentOrderUuid);

            if (transactionRecord == null) {
                //达到重试次数
                if (paymentOrder.getRetriedTransactionRecordNums() >= retryTimes) {
                    log.info("paymentOrder businessDeduct retry transactionRecord Nums is 6,update paymentOrder status fail " + "tradeUuid:" + tradeUuid + ", paymentOrderUuid:" + paymentOrder.getUuid());
                    //支付单支付失败 ,订单支付异常
                    paymentOrderService.updatePaymentOrderAndRepaymentStatus(paymentOrder, order, RepaymentOrderPayResult.PAY_FAIL, PayStatus.PAY_FAIL, OrderPayStatus.PAY_ABNORMAL);
                }
                log.info("paymentOrder businessDeduct transactionRecord is null 。 " + "tradeUuid:" + tradeUuid + ", repaymentOrderUuid:" + repaymentOrderUuid);
                return;
            }

            log.info("paymentOrder businessDeduct data_vertify_create_payment_voucher_deductInfo begin: " + "paymentOrderUuid:" + paymentOrder.getUuid() + ",tradeUuid:" + tradeUuid + ",orderUuid:" + repaymentOrderUuid);

            data_vertify_create_payment_voucher_deductInfo(paymentOrder, order, transactionRecord);

            log.info("paymentOrder businessDeduct data_vertify_create_payment_voucher_deductInfo end: " + "paymentOrderUuid:" + paymentOrder.getUuid() + ",tradeUuid:" + tradeUuid + ",orderUuid:" + repaymentOrderUuid);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("occur error handlerPaymentOrderForBusinessDeduct . FullStackTrace:" + ExceptionUtils.getFullStackTrace(e));
        }
    }

    private void data_vertify_create_payment_voucher_deductInfo(PaymentOrder paymentOrder, RepaymentOrder order, ThirdPartyTransactionRecord transactionRecord) {

        long start = System.currentTimeMillis();

        //验真
        boolean isValidateSuc = paymentOrderService.checkPaymentOrderAndTransactionRecord(paymentOrder, transactionRecord);
        if (!isValidateSuc) {
            //验真失败  支付单支付失败   订单支付异常
            log.info(" paymentOrder and transactionRecord  checkPaymentOrderAndTransactionRecord fail " + "paymentOrderUuid:" + paymentOrder.getUuid() + ",tradeUuid:" + paymentOrder.getTradeUuid());
            paymentOrderService.updatePaymentOrderAndRepaymentStatus(paymentOrder, order, RepaymentOrderPayResult.PAY_FAIL, PayStatus.PAY_FAIL, OrderPayStatus.PAY_ABNORMAL);
            return;
        }

        long endTime = System.currentTimeMillis();
        log.info("验真 checkPaymentOrderAndTransactionRecord -------------------------------------------------use times[" + (endTime - start) + "]ms" + ", 当前时间  " + System.currentTimeMillis());


        //验真成功  支付单支付成功   订单支付成功
        log.info(" paymentOrder and transactionRecord  checkPaymentOrderAndTransactionRecord success " + "paymentOrderUuid:" + paymentOrder.getUuid() + ",tradeUuid:" + paymentOrder.getTradeUuid());
        paymentOrderService.updatePaymentOrderAndRepaymentStatus(paymentOrder, order, RepaymentOrderPayResult.PAY_SUCCESS, PayStatus.PAY_SUCCESS, OrderPayStatus.PAY_END);
        log.info("tradeUuid：[" + paymentOrder.getTradeUuid() + "] paymentOrder and transactionRecord  check end  ");

        List<RepaymentOrderItem> itemList = repaymentOrderItemService.getRepaymentOrderItems(order.getOrderUuid());
        if (CollectionUtils.isEmpty(itemList)) return;

        //生成第三方支付凭证
        ThirdPartyPaymentVoucher paymentVoucher = createThirdPartyVoucherInfo(paymentOrder, order.getFirstContractUuid(), itemList);

        //生成deductPlan deductApplication deductApplicationDetail
        createDeductInfo(paymentOrder, transactionRecord, order, itemList, paymentVoucher.getVoucherNo());

        long endTime2 = System.currentTimeMillis();
        log.info("create  paymentVoucher and deductInfo ---------------- ----------------------------------use times[" + (endTime2 - endTime) + "]ms");
        log.info("create  paymentVoucher and deductInfo ----------------------------------------------total use times[" + (endTime2 - start) + "]ms" + ", 当前时间  " + System.currentTimeMillis());

        //核销
        log.info("begin recover orderUuid：[" + paymentOrder.getOrderUuid() + "]" + "paymentOrderUuid:" + paymentOrder.getUuid());
        thirdPartyVoucherRepaymentOrderWithReconciliationNoSession.generateThirdPartVoucherWithReconciliation(order.getFirstContractUuid(), order.getOrderUuid(), paymentOrder.getUuid(), Priority.High.getPriority());
    }

    private ThirdPartyPaymentVoucher createThirdPartyVoucherInfo(PaymentOrder paymentOrder, String contractUuid, List<RepaymentOrderItem> itemList) {

        Contract contract = contractService.getContract(contractUuid);
        String contractNo = contract == null ? "" : contract.getContractNo();
        String contractUniqueId = contract == null ? "" : contract.getUniqueId();

        ThirdPartyPaymentVoucher paymentVoucher = new ThirdPartyPaymentVoucher(paymentOrder, contractNo, contractUuid, contractUniqueId);
        thirdPartyPaymentVoucherService.save(paymentVoucher);

        createThirdPartyVoucherDetail(itemList, paymentVoucher);

        return paymentVoucher;
    }

    private void createThirdPartyVoucherDetail(List<RepaymentOrderItem> itemList, ThirdPartyPaymentVoucher paymentVoucher) {

        for (RepaymentOrderItem repaymentOrderItem : itemList) {

            AssetSet repaymentPlan = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentOrderItem.getRepaymentBusinessNo());
            String outerRepaymentPlanNo = repaymentPlan == null ? "" : repaymentPlan.getOuterRepaymentPlanNo();
            String repayScheduleNo = repaymentPlan == null ? "" : repaymentPlan.getRepayScheduleNo();
            int currentPeriod = repaymentPlan == null ? null : repaymentPlan.getCurrentPeriod();

            ThirdPartyPaymentVoucherDetail paymentOrderVoucherDetail = new ThirdPartyPaymentVoucherDetail(repaymentOrderItem, paymentVoucher, repaymentOrderItem.getChargeDetail(), repayScheduleNo, outerRepaymentPlanNo, currentPeriod);
            thirdPartyPaymentVoucherDetailService.save(paymentOrderVoucherDetail);
        }
    }

    private void createDeductInfo(PaymentOrder paymentOrder, ThirdPartyTransactionRecord transactionRecord, RepaymentOrder order,
                                  List<RepaymentOrderItem> itemList, String deductId) {

        log.info("create deductInfo begin paymentOrderUuid: " + paymentOrder.getUuid());
        RepaymentOrderItem firstItem = itemList.get(0);
        String contractUniqueId = firstItem == null ? "" : firstItem.getContractUniqueId();
        String contractNo = firstItem == null ? "" : firstItem.getContractNo();
        //根据网关和信托合同判断通道
        PaymentChannelSummaryInfo usedPaymentChannelInfo = getUsedPaymentChannelInfo(paymentOrder);

        DeductApplication dedcutApplication = createAndSaveDeductApplication(paymentOrder, transactionRecord, order, contractUniqueId, contractNo, deductId);
        log.info("create deductInfo paymentOrderUuid: " + paymentOrder.getUuid() + ",dedcutApplicationUuid:" + dedcutApplication.getDeductApplicationUuid());
        createDeductApplicationDetailInfo(order, dedcutApplication, itemList);
        createAndSaveDeductPlan(paymentOrder, transactionRecord, dedcutApplication, usedPaymentChannelInfo, contractUniqueId, contractNo);

        log.info("create deductInfo end paymentOrderUuid: " + paymentOrder.getUuid() + ",dedcutApplicationUuid:" + dedcutApplication.getDeductApplicationUuid());
    }

    private DeductPlan createAndSaveDeductPlan(PaymentOrder paymentOrder, ThirdPartyTransactionRecord transactionRecord, DeductApplication dedcutApplication, PaymentChannelSummaryInfo usedPaymentChannelInfo, String contractUniqueId, String contractNo) {

        String channelServiceUuid = usedPaymentChannelInfo == null ? "" : usedPaymentChannelInfo.getChannelServiceUuid();
        String merchantId = usedPaymentChannelInfo == null ? "" : usedPaymentChannelInfo.getMerchantId();
        String clearingNo = usedPaymentChannelInfo == null ? "" : usedPaymentChannelInfo.getClearingNo();
        DeductPlan deductPlan = new DeductPlan(dedcutApplication.getDeductApplicationUuid(), paymentOrder.getFinancialContractUuid(), contractUniqueId, contractNo, paymentOrder.getPaymentGateWay().ordinal(), channelServiceUuid
                , merchantId, clearingNo, paymentOrder.getCounterAccountNo(), paymentOrder.getCounterAccountName(), paymentOrder.getIdCardNumInAppendix(), paymentOrder.getCounterAccountBankName(), paymentOrder.getTransactionTime(), paymentOrder.getTransactionTime()
                , paymentOrder.getAmount(), transactionRecord.getTransactionAmount(), dedcutApplication.getRepaymentType(), paymentOrder.getTradeUuid(), StringUtils.EMPTY);
        deductPlanService.save(deductPlan);
        log.info("create deductInfo paymentOrderUuid: " + paymentOrder.getUuid() + ",deductPlanUuid:" + deductPlan.getDeductPlanUuid());
        return deductPlan;

    }

    private DeductApplication createAndSaveDeductApplication(PaymentOrder paymentOrder, ThirdPartyTransactionRecord transactionRecord, RepaymentOrder order, String contractUniqueId, String contractNo, String deductId) {

        List<String> repaymentPlanNoList = repaymentOrderItemService.get_repayment_plan_no_list_By_order_uuid(order.getOrderUuid());
        RepaymentType repaymentType = getRepaymentType(paymentOrder.getTransactionTime(), repaymentPlanNoList);

        DeductApplication deductApplication = new DeductApplication(paymentOrder.getDeductRequestNo(), paymentOrder.getFinancialContractUuid(), paymentOrder.getFinancialContractNo(), contractUniqueId,
                contractNo, paymentOrder.getCounterAccountName(), JsonUtils.toJsonString(repaymentPlanNoList), paymentOrder.getAmount(), repaymentType.ordinal(), paymentOrder.getTransactionTime(), paymentOrder.getTransactionTime());
        deductApplication.setDeductId(deductId);
        deductApplication.setSourceType(SourceType.REPAYMENTORDER);
        deductApplicationService.save(deductApplication);

        paymentOrder.setOutlierDocumentUuid(deductApplication.getDeductApplicationUuid());
        paymentOrder.setOutlierDocumentIdentity(transactionRecord.getMerchantOrderNo());
        paymentOrderService.saveOrUpdate(paymentOrder);
        return deductApplication;
    }

    private void createDeductApplicationDetailInfo(RepaymentOrder order, DeductApplication deductApplication, List<RepaymentOrderItem> itemList) {

        if (CollectionUtils.isEmpty(itemList)) {
            return;
        }

        for (RepaymentOrderItem repaymentOrderItem : itemList) {

            Map<String, BigDecimal> detailMap = repaymentOrderItem.parseDetailAmount();

            createSingleDeductApplicationDetail(deductApplication, repaymentOrderItem, ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_PRINCIPLE, IsTotal.DETAIL, detailMap.getOrDefault(ExtraChargeSpec.LOAN_ASSET_PRINCIPAL_KEY, BigDecimal.ZERO));
            createSingleDeductApplicationDetail(deductApplication, repaymentOrderItem, ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_INTEREST, IsTotal.DETAIL, detailMap.getOrDefault(ExtraChargeSpec.LOAN_ASSET_INTEREST_KEY, BigDecimal.ZERO));
            createSingleDeductApplicationDetail(deductApplication, repaymentOrderItem, ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_TECH_FEE, IsTotal.DETAIL, detailMap.getOrDefault(ExtraChargeSpec.LOAN_TECH_FEE_KEY, BigDecimal.ZERO));
            createSingleDeductApplicationDetail(deductApplication, repaymentOrderItem, ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_LOAN_SERVICE_FEE, IsTotal.DETAIL, detailMap.getOrDefault(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY, BigDecimal.ZERO));
            createSingleDeductApplicationDetail(deductApplication, repaymentOrderItem, ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_OTHER_FEE, IsTotal.DETAIL, detailMap.getOrDefault(ExtraChargeSpec.LOAN_OTHER_FEE_KEY, BigDecimal.ZERO));

            createSingleDeductApplicationDetail(deductApplication, repaymentOrderItem, ChartOfAccount.SND_RECIEVABLE_LOAN_PENALTY, IsTotal.DETAIL, detailMap.getOrDefault(ExtraChargeSpec.PENALTY_KEY, BigDecimal.ZERO));
            createSingleDeductApplicationDetail(deductApplication, repaymentOrderItem, ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION, IsTotal.DETAIL, detailMap.getOrDefault(ExtraChargeSpec.OVERDUE_FEE_OBLIGATION_KEY, BigDecimal.ZERO));
            createSingleDeductApplicationDetail(deductApplication, repaymentOrderItem, ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE, IsTotal.DETAIL, detailMap.getOrDefault(ExtraChargeSpec.OVERDUE_FEE_SERVICE_FEE_KEY, BigDecimal.ZERO));
            createSingleDeductApplicationDetail(deductApplication, repaymentOrderItem, ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE, IsTotal.DETAIL, detailMap.getOrDefault(ExtraChargeSpec.OVERDUE_FEE_OTHER_FEE_KEY, BigDecimal.ZERO));

            BigDecimal totalOverDueFee = detailMap.getOrDefault(ExtraChargeSpec.PENALTY_KEY, BigDecimal.ZERO).add(detailMap.getOrDefault(ExtraChargeSpec.OVERDUE_FEE_OBLIGATION_KEY, BigDecimal.ZERO)).
                    add(detailMap.getOrDefault(ExtraChargeSpec.OVERDUE_FEE_SERVICE_FEE_KEY, BigDecimal.ZERO)).add(detailMap.getOrDefault(ExtraChargeSpec.OVERDUE_FEE_OTHER_FEE_KEY, BigDecimal.ZERO));

            createSingleDeductApplicationDetailTotalOverDueFee(deductApplication, repaymentOrderItem, IsTotal.DETAIL, totalOverDueFee);
            createSingleDeductApplicationDetailTotalReceivableAmount(deductApplication, repaymentOrderItem, IsTotal.TOTAL, repaymentOrderItem.getAmount());
        }
    }

    private void createSingleDeductApplicationDetail(DeductApplication deductApplication,
                                                     RepaymentOrderItem repaymentOrderItem, String chartString, IsTotal isTotal, BigDecimal amount) {

        //金额小于等于零不生成明细记录
        if (amount.compareTo(BigDecimal.ZERO) < 1) {
            return;
        }
        AssetSet repaymentPlan = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentOrderItem.getRepaymentBusinessNo());
        if (null == repaymentPlan) {
            return;
        }
        DeductApplicationDetail deductApplicationDetail = new DeductApplicationDetail(deductApplication, repaymentOrderItem, isTotal, amount, repaymentPlan.getAssetUuid());
        deductApplicationDetail.copyTAccount(ChartOfAccount.EntryBook().get(chartString));
        deductApplicationDetailService.save(deductApplicationDetail);
    }

    private void createSingleDeductApplicationDetailTotalOverDueFee(DeductApplication deductApplication, RepaymentOrderItem repaymentOrderItem, IsTotal isTotal, BigDecimal totalOverdueFee) {
        //金额小于等于零不生成明细记录
        if (totalOverdueFee.compareTo(BigDecimal.ZERO) < 1) {
            return;
        }
        AssetSet repaymentPlan = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentOrderItem.getRepaymentBusinessNo());
        if (repaymentPlan == null) {
            return;
        }
        DeductApplicationDetail deductApplicationDetail = new DeductApplicationDetail(deductApplication, repaymentOrderItem, isTotal, totalOverdueFee, repaymentPlan.getAssetUuid());
        deductApplicationDetail.setFirstAccountName(ExtraChargeSpec.TOTAL_OVERDUE_FEE);
        deductApplicationDetailService.save(deductApplicationDetail);

    }


    private void createSingleDeductApplicationDetailTotalReceivableAmount(DeductApplication deductApplication,
                                                                          RepaymentOrderItem repaymentOrderItem, IsTotal total, BigDecimal caclAccountReceivableAmount) {

        //金额小于等于零不生成明细记录
        if (caclAccountReceivableAmount.compareTo(BigDecimal.ZERO) < 1) {
            return;
        }
        AssetSet repaymentPlan = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentOrderItem.getRepaymentBusinessNo());

        if (null == repaymentPlan) {
            return;
        }

        DeductApplicationDetail deductApplicationDetail = new DeductApplicationDetail(deductApplication, repaymentOrderItem, total, caclAccountReceivableAmount, repaymentPlan.getAssetUuid());
        deductApplicationDetail.setFirstAccountName(ExtraChargeSpec.TOTAL_RECEIVABEL_AMOUNT);
        deductApplicationDetailService.save(deductApplicationDetail);
    }


    private PaymentChannelSummaryInfo getUsedPaymentChannelInfo(PaymentOrder paymentOrder) {
        List<PaymentChannelSummaryInfo> deductChannelInfoList = paymentChannelInformationHandler.getPaymentChannelServiceUuidsBy(paymentOrder.getFinancialContractUuid(), BusinessType.SELF, com.zufangbao.sun.yunxin.entity.remittance.AccountSide.DEBIT, null, null);
        PaymentChannelSummaryInfo usedPaymentChannelInfo = null;
        for (PaymentChannelSummaryInfo paymentChannelInfo : deductChannelInfoList) {
            if (paymentChannelInfo.getPaymentGateway() == paymentOrder.getPaymentGateWay().ordinal()) {
                usedPaymentChannelInfo = paymentChannelInfo;
                return usedPaymentChannelInfo;
            }
        }
        return usedPaymentChannelInfo;
    }

    private RepaymentType getRepaymentType(Date transactionTime, List<String> repaymentPlanNoList) {

        List<AssetSet> repaymentPlans = repaymentPlanService.getByRepaymentPlanNoList(repaymentPlanNoList);

        Collections.sort(repaymentPlans, new Comparator<AssetSet>() {
            public int compare(AssetSet a, AssetSet b) {
                return com.demo2do.core.utils.DateUtils.asDay(a.getAssetRecycleDate()).compareTo(com.demo2do.core.utils.DateUtils.asDay(b.getAssetRecycleDate()));
            }
        });

        AssetSet repaymentPlan = repaymentPlans.get(0);
        Date earliestRepayDate = repaymentPlan.getAssetRecycleDate();
        int compare = earliestRepayDate.compareTo(com.demo2do.core.utils.DateUtils.asDay(transactionTime));

        if (compare > 0) {
            return RepaymentType.ADVANCE;
        } else if (compare == 0) {
            return RepaymentType.NORMAL;
        } else {
            return RepaymentType.OVERDUE;
        }
    }

    @Override
    public void rtFlowQueryByNotifyServer(PaymentOrder paymentOrder) {

        ThirdPartyTransactionRecord transactionRecord = thirdPartyTransactionRecordService.getThirdPartyTransactionRecordBytradeUuid(paymentOrder.getTradeUuid());
        if (transactionRecord != null) {
            log.info("transactionRecord exist ---[" + paymentOrder.getTradeUuid() + "] ---");
            return;
        }

        log.info("---[" + paymentOrder.getTradeUuid() + "] start async call barclays---");
        transactionApiHandler.queryCreditModelByNotifyServer(paymentOrder.getTradeUuid(), paymentOrder.getPaymentGateWay().ordinal(), paymentOrder.getFinancialContractUuid(), paymentOrder.getBatchNo());
        return;
    }


    @Override
    public PaymentOrder savePaymentOrderByCommandModel(
            PaymentOrderRequestModel commandModel, RepaymentOrder order) {

        if (commandModel == null || order == null) return null;

        PaymentOrder paymentOrder = null;

        if (commandModel.getFormatPayWay() == PayWay.MERCHANT_DEDUCT) {

            paymentOrder = this.create_payment_order(commandModel, order);

        } else if (commandModel.getFormatPayWay() == PayWay.OFFLINE_TRANSFER) {
            //生成支付单，更改订单状态支付中
            paymentOrder = createPaymentOrderAndUpdateRepaymentOrder(commandModel, order, PayWay.OFFLINE_TRANSFER);

        } else if (commandModel.getFormatPayWay() == PayWay.BUSINESS_DEDUCT) {

            paymentOrder = createPaymentOrderAndUpdateRepaymentOrder(commandModel, order, PayWay.BUSINESS_DEDUCT);
        }

        return paymentOrder;
    }
}