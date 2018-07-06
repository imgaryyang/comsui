package com.suidifu.barclays.handler.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.demo2do.core.utils.SignatureUtils;
import com.zufangbao.sun.yunxin.entity.barclays.ChannelWorkerConfig;
import com.zufangbao.sun.yunxin.service.barclays.ChannelWorkerConfigService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo2do.core.utils.DateUtils;
import com.suidifu.barclays.exception.PullTransactionRecordException;
import com.suidifu.barclays.handler.TransactionRecordHandler;
import com.suidifu.barclays.unionpay.model.TransactionDetailNode;
import com.suidifu.barclays.unionpay.model.TransactionDetailQueryResult;
import com.suidifu.coffer.entity.DebitResult;
import com.suidifu.coffer.entity.unionpay.gz.GZUnionPayTransactionDetailQueryModel;
import com.suidifu.coffer.handler.unionpay.GZUnionpayHandler;
import com.zufangbao.sun.entity.account.InstitutionReconciliationAuditStatus;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.ledgerbook.AccountSide;
import com.zufangbao.sun.yunxin.entity.barclays.BusinessProcessStatus;
import com.zufangbao.sun.yunxin.entity.barclays.ThirdPartyTransactionRecord;
import com.zufangbao.sun.yunxin.handler.PaymentChannelInformationHandler;

@Component("gzUionTransactionRecordHandler")
public class GzUionTransactionRecordHandler implements TransactionRecordHandler {
	
	@Autowired
	GZUnionpayHandler gzUnionpayHandler;
	@Autowired
	PaymentChannelInformationHandler paymentChannelInformationHandler;
	@Autowired
	ChannelWorkerConfigService channelWorkerConfigService;

	@Override
	public List<ThirdPartyTransactionRecord> execPullThirdPartyTransactionRecord(ChannelWorkerConfig channelWorkerConfig)
			throws PullTransactionRecordException {
		Map<String, String> workingParms = channelWorkerConfig.getLocalWorkingConfig();
		if (null == workingParms) {
			throw new PullTransactionRecordException();
		}

		String merchantNo = workingParms.getOrDefault("merchantId", StringUtils.EMPTY);
		if (null == merchantNo) {
			throw new PullTransactionRecordException();
		}
		
		GZUnionPayTransactionDetailQueryModel transactionDetailQueryModel = new GZUnionPayTransactionDetailQueryModel();
		String daysBefore = workingParms.getOrDefault("daysBefore", StringUtils.EMPTY);
		if (StringUtils.isNotEmpty(daysBefore)) {
			int dayBefore = Integer.parseInt(daysBefore);
			String settleDate = DateUtils.format(DateUtils.addDays(new Date(), -dayBefore), "yyyyMMdd");
			transactionDetailQueryModel.setBeginDate(settleDate);
			transactionDetailQueryModel.setEndDate(settleDate);
		}
		String pageNum = workingParms.getOrDefault("pageNum","1");

		DebitResult debitResult = gzUnionpayHandler.transactionDetailQuery(transactionDetailQueryModel, workingParms);
		TransactionDetailQueryResult transactionDetailQueryResult = TransactionDetailQueryResult.initialization(debitResult.getResponsePacket());
		Map<String, String> map = paymentChannelInformationHandler.getFinancialContractUuid(merchantNo);

		Integer pageNumInteger = Integer.valueOf(pageNum);
		Integer pageSum = 1;
		if (StringUtils.isNotEmpty(transactionDetailQueryResult.getPageSum())){
			pageSum = Integer.valueOf(transactionDetailQueryResult.getPageSum());
		}
		if (pageSum > pageNumInteger){
			Integer nextPageNo = pageNumInteger + 1;
			workingParms.put("pageNum", nextPageNo + "");
		}
		channelWorkerConfigService.updateLocalWorkingConfig(channelWorkerConfig.getWorkerUuid(), workingParms);

		return transferToThirdPartyTransactionRecord(transactionDetailQueryResult, map, pageNumInteger);
	}
	
	private List<ThirdPartyTransactionRecord> transferToThirdPartyTransactionRecord(TransactionDetailQueryResult transactionDetailQueryResult, Map<String, String> map, Integer pageNum){
		List<ThirdPartyTransactionRecord> transactionRecordList = new ArrayList<>();
		List<TransactionDetailNode> detailNodes = transactionDetailQueryResult.getDetailNodes();
		for (TransactionDetailNode transactionDetail : detailNodes) {
			ThirdPartyTransactionRecord transactionRecord = new ThirdPartyTransactionRecord();
			if (StringUtils.isNotEmpty(transactionDetail.getReckonAccount())) {
				transactionRecord.setFinancialContractUuid(map.get(transactionDetail.getReckonAccount()));
			}else {
				transactionRecord.setFinancialContractUuid(map.get("noClearingNo"));
			}
			transactionRecord.setTransactionRecordUuid(UUID.randomUUID().toString());
			transactionRecord.setAccountSide(AccountSide.DEBIT);
			transactionRecord.setAuditStatus(InstitutionReconciliationAuditStatus.CREATE);
			transactionRecord.setSettleDate(DateUtils.parseDate(transactionDetail.getSettDate(), "yyyyMMdd"));
			transactionRecord.setRemark(transactionDetail.getRemark());
			transactionRecord.setCreateTime(new Date());
			transactionRecord.setIssuedAmount(BigDecimal.ZERO);
			transactionRecord.setMerchantNo(transactionDetailQueryResult.getMerchantId());
			transactionRecord.setMerchantOrderNo(transactionDetail.getSn());
			transactionRecord.setBatchNo(transactionDetail.getReqNo());
			transactionRecord.setPaymentGateway(PaymentInstitutionName.UNIONPAYGZ);
			transactionRecord.setTransactionAmount(transactionDetail.getAmount());
			transactionRecord.setTransactionTime(DateUtils.parseDate(transactionDetail.getCompleteTime(), "yyyyMMddHHmmss"));
			transactionRecord.setReckonAccount(transactionDetail.getReckonAccount());
			transactionRecord.setBusinessProcessStatus(BusinessProcessStatus.SUCCESS);

			transactionRecordList.add(transactionRecord);
		}

		return transactionRecordList;
	}
	
}
