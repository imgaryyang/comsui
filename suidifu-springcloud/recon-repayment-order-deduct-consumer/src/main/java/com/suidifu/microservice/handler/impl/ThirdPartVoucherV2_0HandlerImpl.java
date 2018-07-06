package com.suidifu.microservice.handler.impl;


import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.giotto.handler.FastHandler;
import com.suidifu.giotto.keyenum.FastContractKeyEnum;
import com.suidifu.giotto.model.FastContract;
import com.suidifu.microservice.entity.JournalVoucher;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.entity.SourceDocumentDetail;
import com.suidifu.microservice.entity.SourceDocumentDetailImporter;
import com.suidifu.microservice.handler.SourceDocumentHandler;
import com.suidifu.microservice.handler.ThirdPartVoucherV2_0Handler;
import com.suidifu.microservice.model.ThirdPartVoucherSourceMapSpec;
import com.suidifu.microservice.service.JournalVoucherService;
import com.suidifu.microservice.service.SourceDocumentDetailService;
import com.suidifu.microservice.service.SourceDocumentService;
import com.suidifu.owlman.microservice.enumation.JournalVoucherCheckingLevel;
import com.suidifu.owlman.microservice.enumation.JournalVoucherStatus;
import com.suidifu.owlman.microservice.spec.GeneralLeasingDocumentTypeDictionary.BusinessDocumentTypeUuid;
import com.zufangbao.sun.api.model.deduct.RepaymentType;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderItem;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.service.ContractAccountService;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.PaymentOrderService;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationDetailService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.enums.voucher.JournalVoucherType;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemChargeService;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("thirdPartVoucherV2_0Handler")
public class ThirdPartVoucherV2_0HandlerImpl implements ThirdPartVoucherV2_0Handler {

	@Autowired
	private DeductApplicationService deductApplicationService;

	@Autowired
	private DeductPlanService deductPlanService;

	@Autowired
	private DeductApplicationDetailService deductApplicationDetailService;

	@Autowired
	private FinancialContractService financialContractService;

	@Autowired
	private RepaymentPlanService repaymentPlanService;

	@Autowired
	private ContractAccountService contractAccountService;

	@Autowired
	private JournalVoucherService journalVoucherService;

	@Autowired
	private ContractService contractService;

	@Autowired
	private SourceDocumentDetailService sourceDocumentDetailService;

	@Autowired
	private SourceDocumentService sourceDocumentService;
	@Autowired
	private SourceDocumentHandler sourceDocumentHandler;

	@Autowired
	private RepaymentOrderItemService repaymentOrderItemService;
	@Autowired
	private PaymentOrderService paymentOrderService;
	@Autowired
	private RepaymentOrderItemChargeService repaymentOrderItemChargeService;
	@Autowired
	private FinancialContractConfigurationService financialContractConfigurationService;
	@Autowired
	private LedgerBookStatHandler ledgerBookStatHandler;

	@Autowired
	private FastHandler fastHandler;

	private static final Log logger = LogFactory.getLog(ThirdPartVoucherV2_0HandlerImpl.class);

	@Override
	public String createDeductRepaymentOrderSourceDocumentUuid(RepaymentOrder repaymentOrder,PaymentOrder paymentOrder) throws GiottoException {
		if(paymentOrder==null || paymentOrder.isPaySuc()==false){//支付状态
			return null;
		}
		String sourceDocumentUuid = sourceDocumentService.getUnWriteOffSourceDocumentUuidByDeductInformation(paymentOrder.getOutlierDocumentUuid());
		if(StringUtils.isNotEmpty(sourceDocumentUuid)){
			return sourceDocumentUuid;
		}
		String financialContractUuid = repaymentOrder.getFinancialContractUuid();
		FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
		String contractUuid=repaymentOrder.getFirstContractUuid();
		FastContract fastContract = fastHandler.getByKey(FastContractKeyEnum.UUID, contractUuid, FastContract.class, true);

		SourceDocument sourceDocument = sourceDocumentHandler.createUnSignedDeductRepaymentOrderSourceDocument(fastContract.getCustomerUuid(), financialContract, repaymentOrder, paymentOrder);
		List<RepaymentOrderItem> repaymentOrderItems = repaymentOrderItemService.getRepaymentOrderItems(repaymentOrder.getOrderUuid());
		for (RepaymentOrderItem repaymentOrderItem :repaymentOrderItems){
			Map<String,BigDecimal> detailAmount = repaymentOrderItemChargeService.getRepaymentOrderChargeMapByItemUuid(repaymentOrderItem.getOrderDetailUuid());
			SourceDocumentDetail sourceDocumentDetail = SourceDocumentDetailImporter.createSourceDocumentDetail(
					sourceDocument.getSourceDocumentUuid(), repaymentOrderItem,paymentOrder, detailAmount, repaymentOrderItem.getRepaymentBusinessNo());

			sourceDocumentDetailService.save(sourceDocumentDetail);
			JournalVoucher journalVoucher = new JournalVoucher();

			journalVoucher.createFromThirdPartyDeductionVoucher(sourceDocumentDetail, AccountSide.DEBIT,
					financialContract.getCompany(), financialContract.getCapitalAccount(),
					sourceDocument);

			// 制证金额(凭证关联金额)
			BigDecimal bookingAmount = sourceDocumentDetail.getAmount();
			journalVoucher.fill_voucher_and_booking_amount(repaymentOrderItem.getRepaymentBusinessUuid(),
					BusinessDocumentTypeUuid.REPAYMENT_ORDER_BV_TYPE, repaymentOrder.getOrderUuid(), bookingAmount,
					JournalVoucherStatus.VOUCHER_CREATED, JournalVoucherCheckingLevel.AUTO_BOOKING,
					JournalVoucherType.THIRD_PARTY_DEDUCT_VOUCHER);
			journalVoucher.fillBillContractInfo(financialContract.getFinancialContractUuid(), repaymentOrderItem.getContractUuid(),
					repaymentOrderItem.getRepaymentBusinessUuid(), financialContract.getContractName(), repaymentOrderItem.getContractNo(),
					repaymentOrderItem.getRepaymentBusinessNo(), null);
			// 凭证来源
			journalVoucher.fillVoucherSource(sourceDocument, RepaymentType.NORMAL);
			// 流水信息
			journalVoucher.fillFlowInformation(paymentOrder.getAmount(),paymentOrder.getOutlierDocumentIdentity(), ThirdPartVoucherSourceMapSpec.cashFlowChannelTypeMap.get(paymentOrder.getPaymentGateWay()),"",paymentOrder.getCounterAccountBankName() );
			journalVoucherService.save(journalVoucher);
		}


		return sourceDocument.getSourceDocumentUuid();
	}

}
