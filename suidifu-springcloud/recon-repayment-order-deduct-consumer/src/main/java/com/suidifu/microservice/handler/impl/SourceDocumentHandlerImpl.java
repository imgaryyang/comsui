package com.suidifu.microservice.handler.impl;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.giotto.handler.FastHandler;
import com.suidifu.giotto.keyenum.FastCustomerKeyEnum;
import com.suidifu.giotto.model.FastCustomer;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.entity.SourceDocumentImporter;
import com.suidifu.microservice.handler.SourceDocumentHandler;
import com.suidifu.microservice.service.SourceDocumentService;
import com.zufangbao.sun.api.model.deduct.DeductApplicationExecutionStatus;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("sourceDocumentHandler")
public class SourceDocumentHandlerImpl implements SourceDocumentHandler {

	@Autowired
	private SourceDocumentService sourceDocumentService;

	@Autowired
	private DeductPlanService deductPlanService;
	@Autowired
	private FastHandler fastHandler;
	private static Log logger = LogFactory.getLog(SourceDocumentHandlerImpl.class);

	private static final String EMPTY = "";

	private static final String pattern = "yyyy-MM-dd HH:mm:ss";

	@Override
	public SourceDocument createUnSignedDeductRepaymentOrderSourceDocument(String customerUuid, FinancialContract financialContract, RepaymentOrder repaymentOrder, PaymentOrder paymentOrder) throws GiottoException {
		List<DeductPlan> deductPlanList = deductPlanService.getDedcutPlanListByDeductApplicationUuid(paymentOrder.getOutlierDocumentUuid(), DeductApplicationExecutionStatus.SUCCESS);
		String deductCashIdentity = deductPlanList.size()>1?"":deductPlanList.get(0).getDeductCashIdentity();
		FastCustomer fastCustomer = fastHandler.getByKey(FastCustomerKeyEnum.CUSTOMER_UUID,customerUuid,FastCustomer.class,true);
		String contractUuid=repaymentOrder.getFirstContractUuid();
		Date paymentSucTime = deductPlanService.getMaxPaymentSucTime(paymentOrder.getOutlierDocumentUuid());
		SourceDocument sourceDocument = SourceDocumentImporter.createRepaymentOrderDeductionVoucher(financialContract.getCompany(), paymentOrder, fastCustomer, contractUuid, financialContract, deductCashIdentity, paymentSucTime);
		sourceDocumentService.save(sourceDocument);
		return sourceDocument;
	}

}
