package com.suidifu.barclays.handler.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.demo2do.core.utils.DateUtils;
import com.suidifu.barclays.exception.PullTransactionRecordException;
import com.suidifu.barclays.handler.TransactionRecordHandler;
import com.suidifu.coffer.entity.PullThirdPartyTransactionRecordModel;
import com.suidifu.coffer.entity.ThirdPartyTransactionRecordModel;
import com.suidifu.coffer.exception.UnSupportedException;
import com.suidifu.coffer.handler.ThirdPartyPayHandler;
import com.zufangbao.sun.entity.account.InstitutionReconciliationAuditStatus;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.ledgerbook.AccountSide;
import com.zufangbao.sun.yunxin.entity.barclays.BusinessProcessStatus;
import com.zufangbao.sun.yunxin.entity.barclays.ChannelWorkerConfig;
import com.zufangbao.sun.yunxin.entity.barclays.ThirdPartyTransactionRecord;

/**
 * @author qinweichao Created on 2017-05-10
 */
@Component("sinapayTransactionRecordHandler")
public class SinapayTransactionRecordHandler implements TransactionRecordHandler {

	@Autowired
	@Qualifier("sinapayHandler")
	ThirdPartyPayHandler sinapayHandler;

	@Override
	public List<ThirdPartyTransactionRecord> execPullThirdPartyTransactionRecord(ChannelWorkerConfig channelWorkerConfig)
			throws PullTransactionRecordException {
		Map<String, String> workingParms = channelWorkerConfig.getLocalWorkingConfig();
		if (null == workingParms) {
			throw new PullTransactionRecordException();
		}

		String merchantNo = workingParms.getOrDefault("memberId", StringUtils.EMPTY);
		if (null == merchantNo) {
			throw new PullTransactionRecordException();
		}
		String beginTime = workingParms.getOrDefault("beginTime", StringUtils.EMPTY);//TODO
		String endTime = DateUtils.format(new Date(), "yyyyMMddHHmmss");
		String pageNo = workingParms.getOrDefault("pageNo", StringUtils.EMPTY);
		String pageSize = workingParms.getOrDefault("pageSize", StringUtils.EMPTY);
		Date requestTime = new Date();
		PullThirdPartyTransactionRecordModel pullThirdPartyTransactionRecordModel = new PullThirdPartyTransactionRecordModel(
				merchantNo, beginTime, endTime, pageNo, pageSize, requestTime);
		List<ThirdPartyTransactionRecordModel> transactionRecordModelList = new ArrayList<>();
		try {
			transactionRecordModelList = sinapayHandler
					.pullTransactionRecord(pullThirdPartyTransactionRecordModel, workingParms);
		} catch (UnSupportedException e) {
			e.printStackTrace();
		}
		return transferToThirdPartyTransactionRecord(transactionRecordModelList);
	}

	private List<ThirdPartyTransactionRecord> transferToThirdPartyTransactionRecord(
			List<ThirdPartyTransactionRecordModel> transactionRecordModelList) {
		List<ThirdPartyTransactionRecord> transactionRecordList = new ArrayList<>();
		for (ThirdPartyTransactionRecordModel thirdPartyTransactionRecordModel : transactionRecordModelList) {
			ThirdPartyTransactionRecord transactionRecord = new ThirdPartyTransactionRecord();
			transactionRecord.setTransactionRecordUuid(UUID.randomUUID().toString());
			transactionRecord.setAccountSide(AccountSide.DEBIT);
			transactionRecord.setAuditStatus(InstitutionReconciliationAuditStatus.CREATE);
			transactionRecord.setChannelSequenceNo(thirdPartyTransactionRecordModel.getChannelSequenceNo());
			transactionRecord.setCreateTime(new Date());
			transactionRecord.setIssuedAmount(BigDecimal.ZERO);
			transactionRecord.setMerchantNo(thirdPartyTransactionRecordModel.getMerchantNo());
			transactionRecord.setMerchantOrderNo(thirdPartyTransactionRecordModel.getMerchantOrderNo());
			transactionRecord.setPaymentGateway(PaymentInstitutionName.SINA_PAY);
			transactionRecord.setServiceFee(thirdPartyTransactionRecordModel.getServiceFee());
			transactionRecord.setTransactionAmount(thirdPartyTransactionRecordModel.getTransactionAmount());
			transactionRecord.setTransactionCreateTime(thirdPartyTransactionRecordModel.getCreateTime());
			transactionRecord.setPayTime(thirdPartyTransactionRecordModel.getPayTime());
			transactionRecord.setTransactionTime(thirdPartyTransactionRecordModel.getTransactionTime());
			BusinessProcessStatus businessProcessStatus = thirdPartyTransactionRecordModel.getBusinessProcessStatus()
					.equals(com.suidifu.coffer.entity.BusinessProcessStatus.SUCCESS) ? BusinessProcessStatus.SUCCESS
							: BusinessProcessStatus.FAIL;
			transactionRecord.setBusinessProcessStatus(businessProcessStatus);
			transactionRecordList.add(transactionRecord);
		}
		return transactionRecordList;
	}

}
