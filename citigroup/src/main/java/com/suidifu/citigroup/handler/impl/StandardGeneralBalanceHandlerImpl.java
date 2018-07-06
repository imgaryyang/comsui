package com.suidifu.citigroup.handler.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.citigroup.entity.BalanceEntrySqlMode;
import com.suidifu.citigroup.entity.GeneralBalanceSqlMode;
import com.suidifu.citigroup.exception.CitiGroupRuntimeException;
import com.suidifu.citigroup.handler.StandardGeneralBalanceHandler;
import com.suidifu.citigroup.service.BalanceEntryService;
import com.suidifu.citigroup.service.GeneralBalanceService;
import com.suidifu.citigroup.service.TableCacheService;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.suidifu.swift.notifyserver.notifyserver.NotifyJobServer;
import com.suidifu.swift.notifyserver.notifyserver.configurations.NotifyServerConfig;
import com.zufangbao.gluon.spec.citigroup.BalanceRequestModel;
import com.zufangbao.sun.api.model.remittance.RemittanceResponsePacket;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationCode;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec;

@Component("standardGeneralBalanceHandler")
public class StandardGeneralBalanceHandlerImpl implements StandardGeneralBalanceHandler {

	private static Log logger = LogFactory.getLog(GeneralBalanceHandlerImpl.class);
	
	@Autowired
	private GeneralBalanceService generalBalanceService;
	
	@Autowired
	private BalanceEntryService balanceEntryService;
	
	@Autowired
	private FinancialContractConfigurationService financialContractConfigurationService;
	
	@Autowired
	private NotifyJobServer fileJobServer;
	
	@Autowired
	private NotifyServerConfig notifyServerConfig;
	
	@Autowired
	private TableCacheService tableCacheService;
	
	
	@Override
	public void insertGeneralBalance(BalanceRequestModel balanceRequestModel) {

		BalanceEntrySqlMode.validateBalanceRequestModelForInsert(balanceRequestModel);

		List<BalanceEntrySqlMode> balanceEntrySqlModes = BalanceEntrySqlMode
				.combineBalanceEntrySqlModeForIncreaseOrInsertPayAble(balanceRequestModel);

		GeneralBalanceSqlMode generalBalanceSqlMode = new GeneralBalanceSqlMode(balanceRequestModel);

		generalBalanceService.saveGeneralBalance(generalBalanceSqlMode);

		balanceEntrySqlModes.forEach(
				balanceEntrySqlMode -> balanceEntrySqlMode.setGeneralBalanceUuid(generalBalanceSqlMode.getUuid()));

		balanceEntryService.saveBalanceEntry(balanceEntrySqlModes);
	}

	
	@Override
	public void updatBankSavingLoan(BalanceRequestModel balanceRequestModel) {
		
		BalanceEntrySqlMode.validateBalanceRequestModelForUpdate(balanceRequestModel);

		String generalBalanceUuid = generalBalanceService.getGeneralBanlanceuUuidByFinancialContractUuid(balanceRequestModel.getFinancialContractUuid());

		if (StringUtils.isEmpty(generalBalanceUuid)) {

			throw new CitiGroupRuntimeException("没有信托[" + balanceRequestModel.getFinancialContractUuid() + "]对应的额度信息！");
		}

		List<BalanceEntrySqlMode> balanceEntrySqlModes = BalanceEntrySqlMode
				.combineBalanceEntrySqlModeForIncreaseOrInsertPayAble(balanceRequestModel);

		generalBalanceService.updatePayAbleAndBankSavingLoan(balanceRequestModel.getFinancialContractUuid(),
				balanceRequestModel.getAmount());

		balanceEntrySqlModes
				.forEach(balanceEntrySqlMode -> balanceEntrySqlMode.setGeneralBalanceUuid(generalBalanceUuid));

		balanceEntryService.saveBalanceEntry(balanceEntrySqlModes);
	}


	@Override
	public void doUpdatOrInsertBankSavingLoan(BalanceRequestModel balanceRequestModel) {
		
		if (!tableCacheService.validFinancialContractIsInBalance(balanceRequestModel.getFinancialContractUuid())) {
			
			doInsertGeneralBalance(balanceRequestModel);
		}else{
			
			doUpdateGeneralBalance(balanceRequestModel);
		}
		
	}
	
	@Override
	public void pushJobToRemittanceForBusinessValidation(RemittanceResponsePacket remittanceResponsePacket,
			boolean isSuccess, String failMessage) {
		remittanceResponsePacket.setIsSuccess(isSuccess);
		remittanceResponsePacket.setFailMessage(failMessage);
		@SuppressWarnings("serial")
		HashMap<String, String> responsePacket = new HashMap<String, String>() {
			{
				put(ZhonghangResponseMapSpec.REMITTANCEREQUESTMODEL, JsonUtils.toJsonString(remittanceResponsePacket));

			}
		};
		NotifyApplication notifyApplication = new NotifyApplication();
		notifyApplication.setRequestUrl(remittanceResponsePacket.getCallBackUrl());
		notifyApplication.setRequestParameters(responsePacket);

		pushJob(notifyApplication,remittanceResponsePacket.getFinancialContractUuid());
	}
	
	
	private void doUpdateGeneralBalance(BalanceRequestModel balanceRequestModel){
		
		String content = financialContractConfigurationService.getFinancialContractConfigContentContent(balanceRequestModel.getFinancialContractUuid(), FinancialContractConfigurationCode.ALLOW_FINANCIAL_CONTRACT_DO_BALANCE.getCode());
		
		BigDecimal totalAmount = balanceRequestModel.getAmount().add(new BigDecimal(content));
		
		financialContractConfigurationService.updateFinancialContractConfiguration(balanceRequestModel.getFinancialContractUuid(), FinancialContractConfigurationCode.ALLOW_FINANCIAL_CONTRACT_DO_BALANCE.getCode(), totalAmount.toString());
		
		this.updatBankSavingLoan(balanceRequestModel);
	}
	
	private void doInsertGeneralBalance(BalanceRequestModel balanceRequestModel){
		
		financialContractConfigurationService.insertFinancialContractConfiguration(balanceRequestModel.getFinancialContractUuid(), FinancialContractConfigurationCode.ALLOW_FINANCIAL_CONTRACT_DO_BALANCE.getCode(), balanceRequestModel.getAmount().toString());
		
		this.insertGeneralBalance(balanceRequestModel);
		
	}
	
	private boolean isExclusive(String financialContractUuid) {
		
		Map<String, Integer> groupCacheJobQueueMap = notifyServerConfig.getGroupCacheJobQueueMapForMq();
		if(StringUtils.isEmpty(financialContractUuid) || MapUtils.isEmpty(groupCacheJobQueueMap) || !groupCacheJobQueueMap.containsKey(financialContractUuid)) {
			return false;
		}
		return true;
	}

	private void pushJob(NotifyApplication notifyApplication, String financialContractUuid) {
		if (isExclusive(financialContractUuid)) {
			notifyApplication.setGroupName(financialContractUuid);
		}
		fileJobServer.pushJob(notifyApplication);
	}
}
