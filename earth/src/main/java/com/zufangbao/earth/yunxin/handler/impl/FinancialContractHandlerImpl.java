package com.zufangbao.earth.yunxin.handler.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.demo2do.core.persistence.support.Filter;
import com.demo2do.core.persistence.support.Order;
import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.utils.StringUtils;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.yunxin.handler.CreateException;
import com.zufangbao.earth.yunxin.handler.FinancialContractHandler;
import com.zufangbao.earth.yunxin.handler.ModifyException;
import com.zufangbao.earth.yunxin.handler.SystemOperateLogHandler;
import com.zufangbao.gluon.opensdk.HttpClientUtils;
import com.zufangbao.gluon.spec.citigroup.BalanceRequestModel;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.account.Account;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.FinancialContractConfiguration;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationCode;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationContentValue;
import com.zufangbao.sun.entity.financial.FinancialContractType;
import com.zufangbao.sun.entity.financial.FinancialType;
import com.zufangbao.sun.entity.financial.PlanRepaymentTimeConfiguration;
import com.zufangbao.sun.entity.financial.RemittanceObject;
import com.zufangbao.sun.entity.financial.TemporaryRepurchaseJson;
import com.zufangbao.sun.entity.repurchase.RepurchaseApproach;
import com.zufangbao.sun.entity.repurchase.RepurchaseCycle;
import com.zufangbao.sun.entity.repurchase.RepurchaseRule;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.service.AccountService;
import com.zufangbao.sun.service.AppService;
import com.zufangbao.sun.service.CompanyService;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.AssetPackageFormat;
import com.zufangbao.sun.yunxin.entity.CnEnTranslation;
import com.zufangbao.sun.yunxin.entity.model.CreateFinancialContractModel;
import com.zufangbao.sun.yunxin.entity.model.ModifyFinancialContractBasicInfoModel;
import com.zufangbao.sun.yunxin.entity.model.ModifyFinancialContractRemittanceInfoModel;
import com.zufangbao.sun.yunxin.entity.model.ModifyFinancialContractRepaymentInfoModel;
import com.zufangbao.sun.yunxin.entity.model.SpecialAccountInitializationModel;
import com.zufangbao.sun.yunxin.entity.model.financialcontract.FinancialContractQueryModel;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceStrategyMode;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLogRequestParam;
import com.zufangbao.sun.yunxin.log.TemporaryRepurchaseLog;
import com.zufangbao.sun.yunxin.service.account.SpecialAccountService;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec;
import com.zufangbao.wellsfargo.yunxin.handler.specialaccount.SpecialAccountHandler;

@Component("FinancialContractHandler")
public class FinancialContractHandlerImpl implements FinancialContractHandler {

	@Autowired
	private AccountService accountService;
	@Autowired
	private FinancialContractService financialContractService;
	@Autowired
	private AppService appService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private LedgerBookService ledgerBookService;
	@Autowired
	private SystemOperateLogHandler systemOperateLogHandler;
	@Autowired
	private FinancialContractConfigurationService financialContractConfigurationService;
	@Autowired
    private PrincipalService principalService;
	@Autowired
	private SpecialAccountHandler specialAccountHandler;

	@Autowired
	private SpecialAccountService specialAccountService;
	
    @Value("#{config['urlToCitigroupForUpdateBankSavingLoan']}")
    private String urlToCitigroupForUpdateBankSavingLoan;
	
	@Override
	public FinancialContract configFinancialContract(CreateFinancialContractModel createFinancialContractModel,
			Long principalId) {
		FinancialContract financialContract =createNewFinancialContract(createFinancialContractModel, principalId);
		if(financialContract == null){
			throw new CreateException();
		}
		SpecialAccountInitializationModel specialAccountInitializationModel = JsonUtils.parse(createFinancialContractModel.getSpecialAccountInitializationModelJson(),SpecialAccountInitializationModel.class);
		if(specialAccountInitializationModel != null && createFinancialContractModel.isSpecialAccountSwitchOn() ){
			specialAccountHandler.initializeSpecialAccount(financialContract.getUuid(), specialAccountInitializationModel, "");
		}
		return financialContract;
	}
	

	//新增信托合同
	@Override
	public FinancialContract createNewFinancialContract(CreateFinancialContractModel model, Long principalId) throws CreateException{
		if(model == null || !model.validate_params()){
			throw new CreateException("请求参数错误！");
		}
		FinancialContract financialContract = createFinancialContract(model);
		if(financialContract.getCompany() != null){
			LedgerBook LedgerBook = new LedgerBook(financialContract.getLedgerBookNo(), financialContract.getCompany().getId()+"");
			if(model.getIsOpenBookBackups() == 1){
				LedgerBook.setLedgerBookVersion("openScript");
			}else if (model.getIsOpenBookBackups() == 2){
				LedgerBook.setLedgerBookVersion("closeProgram");
			}
			ledgerBookService.save(LedgerBook);
		}
		financialContractService.save(financialContract);
		if (model.getAllowModifyRemittanceApplication() == 1) {
			FinancialContractConfiguration configuration1 = new FinancialContractConfiguration(financialContract.getFinancialContractUuid(),FinancialContractConfigurationCode.ALLOW_MODIFY_REMITTANCE_APPLICATION.getCode(),model.getAllowModifyRemittanceApplication()+"");
			financialContractConfigurationService.save(configuration1);
		}
		if (model.getAllowNotOverdueAutoConfirm() == 1) {
			FinancialContractConfiguration configuration2 = new FinancialContractConfiguration(financialContract.getFinancialContractUuid(),FinancialContractConfigurationCode.ALLOW_NOT_OVERDUE_AUTO_CONFIRM.getCode(),model.getAllowNotOverdueAutoConfirm()+"");
			financialContractConfigurationService.save(configuration2);
		}
		if (model.getAllowOverdueAutoConfirm() != null) {
			FinancialContractConfiguration configuration3 = new FinancialContractConfiguration(financialContract.getFinancialContractUuid(),FinancialContractConfigurationCode.ALLOW_OVERDUE_AUTO_CONFIRM.getCode(),model.getAllowOverdueAutoConfirm().toString());
			financialContractConfigurationService.save(configuration3);
		}
		if(model.isSpecialAccountSwitchOn()){
			FinancialContractConfiguration specialAccountSwitch = new FinancialContractConfiguration(financialContract.getFinancialContractUuid(),FinancialContractConfigurationCode.SPECIAL_ACCOUNT_SWITCH.getCode(),
					FinancialContractConfigurationContentValue.SPECIAL_ACCOUNT_SWITCH_ON);
			financialContractConfigurationService.save(specialAccountSwitch);
		}
		FinancialContractConfiguration configuration4 = new FinancialContractConfiguration(financialContract.getFinancialContractUuid(),FinancialContractConfigurationCode.PLAN_REPAYMENT_TIME_CONFIGURATION.getCode(),
					model.onlineOrOfflineChooseType().getName());
			financialContractConfigurationService.save(configuration4);
		principalService.bindFinancialContract(financialContract.getId(),principalId);
		return financialContract;
	}
	
	
	private FinancialContract createFinancialContract(CreateFinancialContractModel model) {
		FinancialContract financialContract = create_financial_contract_with_basic_info(model);
		create_financial_contract_with_remittance_info(model, financialContract);
		create_financial_contract_with_repayment_info(model, financialContract);
		return financialContract;
	}

	private FinancialContract create_financial_contract_with_basic_info(CreateFinancialContractModel model) {
		FinancialContract financialContract = create_financial_contract_basic_info(model);
		create_trust_account(model, financialContract);
		create_sub_accounts(model, financialContract);
		return financialContract;
	}
	
	private FinancialContract create_financial_contract_basic_info(CreateFinancialContractModel model) {
		FinancialContract financialContract = new FinancialContract();
		financialContract.setContractNo(model.getFinancialContractNo());
		financialContract.setContractName(model.getFinancialContractName());
		financialContract.setContractShortName(model.getFinancialContractShortName());
		if (model.validateApp()) {
			financialContract.setApp(appService.getAppById(model.getAppId()));
		}
		financialContract.setCapitalParty(model.getCapitalParty());
		financialContract.setOtherParty(model.getOtherParty());
		financialContract.setFinancialContractType(EnumUtil.fromOrdinal(FinancialContractType.class, model.getFinancialContractType()));
		financialContract.setFinancialType(EnumUtil.fromOrdinal(FinancialType.class, model.getFinancialType()));
		financialContract.setCompany(companyService.getCompanyByUuid(model.getCompanyUuid()));
		financialContract.setAdvaStartDate(model.getAdvaStartDate());
		financialContract.setThruDate(model.getThruDate());
		
		financialContract.setLedgerBookNo(UUID.randomUUID().toString());
		financialContract.setCreateTime(new Date());
		return financialContract;
	}
	
	private void create_trust_account(CreateFinancialContractModel model,FinancialContract financialContract) {
		Account capitalAccount = new Account(model.getBankName(),model.getAccountName(),model.getAccountNo());
		Serializable accountId = accountService.save(capitalAccount);
		capitalAccount = accountService.load(Account.class, accountId);
		financialContract.setCapitalAccount(capitalAccount);
	}
	
	private void create_sub_accounts(CreateFinancialContractModel model,FinancialContract financialContract) {
		if (StringUtils.isEmpty(model.getSubAccounts())){
			financialContract.setSubAccountUuids(null);
		    return;
		}
		
		List<Account> subAccountList = model.getSubAccountList();
		List<String> uuids = new ArrayList<String>();
		for (Account subAccount : subAccountList) {
			subAccount.setUuid(UUID.randomUUID().toString());
			accountService.save(subAccount);
			uuids.add(subAccount.getUuid());
		}
		financialContract.setSubAccountUuidList(uuids);
	}
	
	private void create_financial_contract_with_remittance_info(CreateFinancialContractModel model,FinancialContract financialContract) {
		financialContract.setTransactionLimitPerTranscation(model.getTransactionLimitPerTranscation());
		financialContract.setTransactionLimitPerDay(model.getTransactionLimitPerDay());
		financialContract.setRemittanceStrategyMode(EnumUtil.fromOrdinal(RemittanceStrategyMode.class, model.getRemittanceStrategyMode()));
		financialContract.setRemittanceObject(EnumUtil.fromOrdinal(RemittanceObject.class, model.getRemittanceObject()));

		create_remittance_related_accounts(model, financialContract);
	}

	private void create_remittance_related_accounts(CreateFinancialContractModel model,FinancialContract financialContract) {
		if (StringUtils.isEmpty(model.getAppAccounts())){
			financialContract.setAppAccountUuidList(null);
		    return;
		}
		
		List<Account> appCapitalAccountList = model.getAppAccountList();
		List<String> uuids = new ArrayList<String>();
		for (Account appCapitalAccount : appCapitalAccountList) {
				appCapitalAccount.setUuid(UUID.randomUUID().toString());
				accountService.save(appCapitalAccount);
				uuids.add(appCapitalAccount.getUuid());
		}
		financialContract.setAppAccountUuidList(uuids);
	}
	
	private void create_financial_contract_with_repayment_info(CreateFinancialContractModel model,FinancialContract financialContract) {
		financialContract.setAssetPackageFormat(EnumUtil.fromOrdinal(AssetPackageFormat.class,model.getAssetPackageFormat()));
		financialContract.setPayForGo(model.getAssetPackageFormat() == AssetPackageFormat.WITH_BORROW_ALSO.getOrdinal() ? 1 : 0);
		financialContract.setAllowOnlineRepayment(model.getAllowOnlineRepayment());
		financialContract.setAllowOfflineRepayment(model.getAllowOfflineRepayment());
		if(model.getAllowOnlineRepayment()) {
			financialContract.setSysNormalDeductFlag(model.getSysNormalDeductFlag());
			financialContract.setSysOverdueDeductFlag(model.getSysOverdueDeductFlag());
			financialContract.setAllowAdvanceDeductFlag(model.getAllowAdvanceDeductFlag());
			financialContract.setSysCreateStatementFlag(model.getSysNormalDeductFlag() || model.getSysOverdueDeductFlag());
		}
		financialContract.setAdvaRepaymentTerm(model.getAdvaRepaymentTerm());
		financialContract.setLoanOverdueStartDay(model.getAdvaRepaymentTerm()+1);
		financialContract.setAdvaMatuterm(model.getAdvaMatuterm() == null ? 0 : model.getAdvaMatuterm());
		financialContract.setAdvaRepoTerm(model.getAdvaRepoTerm());
		financialContract.setLoanOverdueEndDay(model.getAdvaRepoTerm()-1);
		financialContract.setSysCreatePenaltyFlag(model.getSysCreatePenaltyFlag());
		financialContract.setPenalty(CnEnTranslation.convertPenaltyCnToEn(model.getPenalty()));
		financialContract.setOverdueDefaultFee(model.getOverdueDefaultFee());
		financialContract.setOverdueServiceFee(model.getOverdueServiceFee());
		financialContract.setOverdueOtherFee(model.getOverdueOtherFee());
		financialContract.setRepurchaseApproach(EnumUtil.fromOrdinal(RepurchaseApproach.class,model.getRepurchaseApproach()));
		if(model.onlineOrOfflineIsChoosed()){
			financialContract.setRepaymentCheckDays(model.getRepaymentCheckDays());
		}
		if(model.getRepurchaseApproach()!=null && model.getRepurchaseApproach() == RepurchaseApproach.SYSTEM_GENERATE.ordinal()){
			financialContract.setRepurchaseRule(EnumUtil.fromOrdinal(RepurchaseRule.class,model.getRepurchaseRule()));
			if(model.getRepurchaseRule() != null && model.getRepurchaseRule() == RepurchaseRule.IRREGULARLY.ordinal()){
				financialContract.setRepurchaseCycle(EnumUtil.fromOrdinal(RepurchaseCycle.class,model.getRepurchaseCycle()));
				financialContract.setDaysOfCycle(model.getDaysOfCycle());
				financialContract.setTemporaryRepurchaseWithList(model.getTemporaryRepurchaseList());
			}
		}
		financialContract.setRepurchaseAlgorithm(CnEnTranslation.convertRepurchaseCnToEn(model.getRepurchaseAlgorithm()));
		financialContract.setRepurchasePrincipalAlgorithm(CnEnTranslation.convertRepurchaseCnToEn(model.getRepurchasePenaltyAlgorithm()));
		financialContract.setRepurchaseInterestAlgorithm(CnEnTranslation.convertRepurchaseCnToEn(model.getRepurchaseInterestAlgorithm()));
		financialContract.setRepurchasePenaltyAlgorithm(CnEnTranslation.convertRepurchaseCnToEn(model.getRepurchasePenaltyAlgorithm()));
		financialContract.setRepurchaseOtherChargesAlgorithm(CnEnTranslation.convertRepurchaseCnToEn(model.getRepurchaseOtherChargesAlgorithm()));
	}




	//修改信托合同 --基础信息
	@Override
	public FinancialContract modifyFinancialContractBasicInfo(ModifyFinancialContractBasicInfoModel model,FinancialContract financialContract) throws ModifyException{
		if(model== null || !model.checkParams() || financialContract == null){
			throw new ModifyException("请求参数错误！");
		}
		financialContract.setContractNo(model.getFinancialContractNo());
		financialContract.setContractName(model.getFinancialContractName());
		financialContract.setContractShortName(model.getFinancialContractShortName());
		if (model.validateApp()) {
			financialContract.setApp(appService.getAppById(model.getAppId()));
		}
		financialContract.setCapitalParty(model.getCapitalParty());
		financialContract.setOtherParty(model.getOtherParty());
		financialContract.setFinancialType(EnumUtil.fromOrdinal(FinancialType.class, model.getFinancialType()));
		financialContract.setFinancialContractType(EnumUtil.fromOrdinal(FinancialContractType.class, model.getFinancialContractType()));
		financialContract.setCompany(companyService.getCompanyByUuid(model.getCompanyUuid()));
		financialContract.setAdvaStartDate(model.getAdvaStartDate());
		financialContract.setThruDate(model.getThruDate());
		financialContract.setContractShortName(model.getFinancialContractShortName());
		save_or_update_trust_account(model, financialContract);
		modify_sub_accounts(model, financialContract);
		financialContract.setLastModifiedTime(new Date());
		financialContractService.update(financialContract);
		return financialContract;
	}

	private void save_or_update_trust_account(ModifyFinancialContractBasicInfoModel model,FinancialContract financialContract) {
		Account capitalAccount = financialContract.getCapitalAccount();
		if (capitalAccount == null) {
			Account newcapitalAccount = new Account(model.getBankName(),model.getAccountName(),model.getAccountNo());
			Serializable accountId = accountService.save(newcapitalAccount);
			newcapitalAccount = accountService.load(Account.class, accountId);
			financialContract.setCapitalAccount(newcapitalAccount);
		} else {
			capitalAccount.setBankName(model.getBankName());
			capitalAccount.setAccountName(model.getAccountName());
			capitalAccount.setAccountNo(model.getAccountNo());
			accountService.update(capitalAccount);
			financialContract.setCapitalAccount(capitalAccount);
		}
    }
	
	private void modify_sub_accounts(ModifyFinancialContractBasicInfoModel model,FinancialContract financialContract) {
		if (StringUtils.isEmpty(model.getSubAccounts())){
			financialContract.setSubAccountUuidList(null);
		}else{
			List<Account> subAccountList = model.getSubAccountList();
			List<String> uuids = new ArrayList<String>();
			for (Account subAccount : subAccountList) {
				accountService.saveOrUpdateWith(subAccount);
				uuids.add(subAccount.getUuid());
			}
			financialContract.setSubAccountUuidList(uuids);
		}
	}
	
	//修改信托合同 --放款信息
	@Override
	public FinancialContract modifyFinancialContractRemittanceInfo(ModifyFinancialContractRemittanceInfoModel model,FinancialContract financialContract) throws ModifyException {
		if(model== null || !model.checkParams() || financialContract == null){
			throw new ModifyException("请求参数错误！");
		}
		financialContract.setTransactionLimitPerTranscation(model.getTransactionLimitPerTranscation());
		financialContract.setTransactionLimitPerDay(model.getTransactionLimitPerDay());
		financialContract.setRemittanceStrategyMode(EnumUtil.fromOrdinal(RemittanceStrategyMode.class, model.getRemittanceStrategyMode()));
		financialContract.setRemittanceObject(EnumUtil.fromOrdinal(RemittanceObject.class, model.getRemittanceObject()));
		modify_remittance_related_accounts(model, financialContract);
		financialContract.setLastModifiedTime(new Date());
		financialContractService.update(financialContract);
		modifyFinancialContractConfiguration(financialContract.getFinancialContractUuid(),FinancialContractConfigurationCode.ALLOW_MODIFY_REMITTANCE_APPLICATION.getCode(),model.getAllowModifyRemittanceApplication());
		return financialContract;
	}

	private void modify_remittance_related_accounts(ModifyFinancialContractRemittanceInfoModel model,FinancialContract financialContract) {
		if (StringUtils.isEmpty(model.getAppAccounts())){
			financialContract.setAppAccountUuidList(null);
		}else{
			List<Account> appCapitalAccountList = JsonUtils.parseArray(model.getAppAccounts(), Account.class);
			List<String> uuids = new ArrayList<String>();
			for (Account appCapitalAccount : appCapitalAccountList) {
				accountService.saveOrUpdateWith(appCapitalAccount);
				uuids.add(appCapitalAccount.getUuid());
			}
			financialContract.setAppAccountUuidList(uuids);
		}
	}


	//修改信托合同 --还款信息
	@Override
	public FinancialContract modifyFinancialContractRepaymentInfo(ModifyFinancialContractRepaymentInfoModel model,FinancialContract financialContract) throws ModifyException {
		if(model== null || !model.checkParams() || financialContract == null){
			throw new ModifyException("请求参数错误！");
		}
        
		financialContract.setAssetPackageFormat(EnumUtil.fromOrdinal(AssetPackageFormat.class, model.getAssetPackageFormat()));
		financialContract.setPayForGo(model.getAssetPackageFormat() == AssetPackageFormat.WITH_BORROW_ALSO.getOrdinal() ? 1 : 0);
		financialContract.setAllowOnlineRepayment(model.getAllowOnlineRepayment());
		financialContract.setAllowOfflineRepayment(model.getAllowOfflineRepayment());
		if(model.getAllowOnlineRepayment()) {
			financialContract.setSysNormalDeductFlag(model.getSysNormalDeductFlag());
			financialContract.setSysOverdueDeductFlag(model.getSysOverdueDeductFlag());
			financialContract.setAllowAdvanceDeductFlag(model.getAllowAdvanceDeductFlag());
			financialContract.setSysCreateStatementFlag(model.getSysNormalDeductFlag() || model.getSysOverdueDeductFlag());
		}
		financialContract.setAdvaRepaymentTerm(model.getAdvaRepaymentTerm());
		financialContract.setLoanOverdueStartDay(model.getAdvaRepaymentTerm()+1);
		financialContract.setAdvaMatuterm(model.getAdvaMatuterm() == null ? 0 : model.getAdvaMatuterm());
		financialContract.setAdvaRepoTerm(model.getAdvaRepoTerm());
		financialContract.setLoanOverdueEndDay(model.getAdvaRepoTerm()-1);
		financialContract.setSysCreatePenaltyFlag(model.getSysCreatePenaltyFlag());
		financialContract.setPenalty(CnEnTranslation.convertPenaltyCnToEn(model.getPenalty()));


		financialContract.setOverdueDefaultFee(decimalFormat(model.getOverdueDefaultFee()));
		financialContract.setOverdueServiceFee(decimalFormat(model.getOverdueServiceFee()));
		financialContract.setOverdueOtherFee(decimalFormat(model.getOverdueOtherFee()));

		financialContract.setRepurchaseApproach(EnumUtil.fromOrdinal(RepurchaseApproach.class,model.getRepurchaseApproach()));
		if(model.getRepurchaseApproach() != null  && model.getRepurchaseApproach() == RepurchaseApproach.SYSTEM_GENERATE.ordinal()){
			financialContract.setRepurchaseRule(EnumUtil.fromOrdinal(RepurchaseRule.class,model.getRepurchaseRule()));
			if(model.getRepurchaseRule() != null && model.getRepurchaseRule() == RepurchaseRule.IRREGULARLY.ordinal()){
				financialContract.setRepurchaseCycle(EnumUtil.fromOrdinal(RepurchaseCycle.class,model.getRepurchaseCycle()));
				financialContract.setDaysOfCycle(model.getDaysOfCycle());
				financialContract.setTemporaryRepurchaseWithList(model.getTemporaryRepurchaseList());
			}
		}
		financialContract.setRepurchaseAlgorithm(CnEnTranslation.convertRepurchaseCnToEn(model.getRepurchaseAlgorithm()));
		financialContract.setRepurchasePrincipalAlgorithm(CnEnTranslation.convertRepurchaseCnToEn(model.getRepurchasePrincipalAlgorithm()));
		financialContract.setRepurchaseInterestAlgorithm(CnEnTranslation.convertRepurchaseCnToEn(model.getRepurchaseInterestAlgorithm()));
		financialContract.setRepurchasePenaltyAlgorithm(CnEnTranslation.convertRepurchaseCnToEn(model.getRepurchasePenaltyAlgorithm()));
		financialContract.setRepurchaseOtherChargesAlgorithm(CnEnTranslation.convertRepurchaseCnToEn(model.getRepurchaseOtherChargesAlgorithm()));
		financialContract.setLastModifiedTime(new Date());
		modifyPlanRepaymentTimeLock(financialContract, model);
		financialContractService.update(financialContract);
		
		FinancialContractConfiguration configuration = financialContractConfigurationService.getFinancialContractConfig(financialContract.getFinancialContractUuid(), FinancialContractConfigurationCode.ALLOW_MODIFY_REPAYMENT_PLAN.getCode());
		String modifyFlagContent = model.getModifyFlag()+"";
		if(configuration != null) {
			configuration.setContent(modifyFlagContent);
			financialContractConfigurationService.update(configuration);
		} else {
			configuration = new FinancialContractConfiguration(financialContract.getUuid(), "modifyFlag", modifyFlagContent);
			financialContractConfigurationService.save(configuration);
		}

		modifyFinancialContractConfiguration(financialContract.getFinancialContractUuid(),FinancialContractConfigurationCode.ALLOW_NOT_OVERDUE_AUTO_CONFIRM.getCode(),model.getAllowNotOverdueAutoConfirm());
		modifyAllowOverdueAutoConfirm(financialContract, model.getAllowOverdueAutoConfirm());
		return financialContract;
	}

	private void modifyAllowOverdueAutoConfirm(FinancialContract financialContract, Integer allowOverdueAutoConfirm) {
		FinancialContractConfiguration configuration = financialContractConfigurationService.getFinancialContractConfig(financialContract.getFinancialContractUuid(), FinancialContractConfigurationCode.ALLOW_OVERDUE_AUTO_CONFIRM.getCode());
		String content = allowOverdueAutoConfirm == null ? "" : allowOverdueAutoConfirm.toString();
		if(configuration != null) {
			configuration.setContent(content);
			financialContractConfigurationService.update(configuration);
		} else if (allowOverdueAutoConfirm != null) {
			configuration = new FinancialContractConfiguration(financialContract.getUuid(), FinancialContractConfigurationCode.ALLOW_OVERDUE_AUTO_CONFIRM.getCode(), content);
			financialContractConfigurationService.save(configuration);
		}
	}
	
	private void modifyPlanRepaymentTimeLock(FinancialContract financialContract,ModifyFinancialContractRepaymentInfoModel model){
		PlanRepaymentTimeConfiguration oldPlanRepaymentTimeLock = financialContractConfigurationService.getPlanRepaymentTimeConfiguration(financialContract.getUuid());
		if(model.onlineOrOfflineChooseType() != oldPlanRepaymentTimeLock){
			financialContractConfigurationService.updateOrSaveFinancialContractConfiguration(financialContract.getUuid(), FinancialContractConfigurationCode.PLAN_REPAYMENT_TIME_CONFIGURATION.getCode(), model.onlineOrOfflineChooseType().getName());
		}
		if(model.isRepaymentDayCheck() && financialContract.getRepaymentCheckDays() != model.getRepaymentCheckDays()){
			financialContract.setRepaymentCheckDays(model.getRepaymentCheckDays());
		}
		if(model.onlineOrOfflineChooseType()== PlanRepaymentTimeConfiguration.NEITHER){
			financialContract.setRepaymentCheckDays(-1);
		}
	}

	private BigDecimal decimalFormat(BigDecimal decimal) {
		if(decimal == null){
			return null;
		}
		DecimalFormat df = new DecimalFormat("#.00");
		String fee = df.format(decimal);
		BigDecimal feeDecimal = new BigDecimal(fee);
		return feeDecimal;
	}

	@Override
	public Map<String, Object> query(FinancialContractQueryModel financialContractQueryModel, Page page) {
		Filter filter = getContractFilter(financialContractQueryModel);
		Order order = new Order(financialContractQueryModel.getOrderDBField(), financialContractQueryModel.getOrderSortType());
		List<FinancialContract> totalFinancialContracts = financialContractService.list(FinancialContract.class, filter, order);
		List<FinancialContract> pagedFinancialContracts = financialContractService.list(FinancialContract.class, filter, order, page);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("size", totalFinancialContracts.size());
		result.put("list", pagedFinancialContracts);
		return result;
	}
	
	private Filter getContractFilter(FinancialContractQueryModel financialContractQueryModel) {
		financialContractQueryModel = financialContractQueryModel == null? new FinancialContractQueryModel() : financialContractQueryModel;
		Filter filter = new Filter();
		if (is_where_condition(financialContractQueryModel.getFinancialContractNo())) {
			filter.addLike("contractNo", financialContractQueryModel.getFinancialContractNo());
		}
		if (is_where_condition(financialContractQueryModel.getFinancialContractName())) {
			filter.addLike("contractName", financialContractQueryModel.getFinancialContractName());
		}
		if ((financialContractQueryModel.getAppId() == null) || (financialContractQueryModel.getAppId() != -1) ) {
			filter.addEquals("app.id", financialContractQueryModel.getAppId());
		}
		if (FinancialContractType.formValue(financialContractQueryModel.getFinancialContractType()) != null) {
			filter.addEquals("financialContractType", FinancialContractType.formValue(financialContractQueryModel.getFinancialContractType()));
		}
		if (is_where_condition(financialContractQueryModel.getFinancialAccountNo())) {
			filter.addLike("capitalAccount.accountNo", financialContractQueryModel.getFinancialAccountNo());
		}
		return filter;
	}

	private boolean is_where_condition(String address) {
		return !StringUtils.isEmpty(address);
	}

	@Override
	public List<FinancialContract> getFinancialContractList(Long financialContractId) {
		if(financialContractId == -1) {
			return financialContractService.loadAll(FinancialContract.class);
		}
		FinancialContract financialContract = financialContractService.load(FinancialContract.class, financialContractId);
		if(financialContract != null) {
			List<FinancialContract> financialContractList = new ArrayList<>();
			financialContractList.add(financialContract);
			return financialContractList;
		}
		return Collections.emptyList();
	}
	
	//新增临时回购任务
	@Override
	public String createTemporaryRepurchases(Principal principal, HttpServletRequest request, TemporaryRepurchaseJson tr, FinancialContract fc) throws Exception{
		if(tr == null || fc == null){
			return "参数错误！";
		}
		List<TemporaryRepurchaseJson> trList = fc.getTemporaryRepurchaseList();
		trList.add(tr);
		fc.setTemporaryRepurchaseWithList(trList);
		financialContractService.saveOrUpdate(fc);
		
		TemporaryRepurchaseLog log = new TemporaryRepurchaseLog(tr, fc.getFinancialContractUuid());
		SystemOperateLogRequestParam param = getSystemOperateLogrequestParam(principal, request, log, LogFunctionType.ADD_TEMPORARY_REPURCHASE, LogOperateType.ADD);
		systemOperateLogHandler.generateSystemOperateLog(param);
		
		return null;
	}
	
	//删除临时回购任务
	@Override
	public String deleteTemporaryRepurchases(Principal principal, HttpServletRequest request, TemporaryRepurchaseJson deleteTr, FinancialContract fc) throws Exception{
		if(deleteTr == null || fc == null){
			return "参数错误！";
		}
		List<TemporaryRepurchaseJson> trList = fc.getTemporaryRepurchaseList();
		for (TemporaryRepurchaseJson tr : trList) {
			if(deleteTr.getRepurchaseUuid().equals(tr.getRepurchaseUuid())){
				trList.remove(tr);
				break;
			}
		}
		fc.setTemporaryRepurchaseWithList(trList);
		financialContractService.saveOrUpdate(fc);
		
		TemporaryRepurchaseLog log = new TemporaryRepurchaseLog(deleteTr, fc.getFinancialContractUuid());
		SystemOperateLogRequestParam param = getSystemOperateLogrequestParam(principal, request, log, LogFunctionType.DELETE_TEMPORARY_REPURCHASE, LogOperateType.DELETE);
		systemOperateLogHandler.generateSystemOperateLog(param);
		
		return null;
	}
	
	//修改临时回购任务
	@Override
	public String editTemporaryRepurchases(Principal principal, HttpServletRequest request, TemporaryRepurchaseJson editTr, FinancialContract fc) throws Exception{
		if(editTr == null || fc == null){
			return "参数错误！";
		}
		List<TemporaryRepurchaseJson> trList = fc.getTemporaryRepurchaseList();
		TemporaryRepurchaseLog oldLog = null;
		for(TemporaryRepurchaseJson trJson : trList){
			if(trJson.getRepurchaseUuid().equals(editTr.getRepurchaseUuid())){
				oldLog = new TemporaryRepurchaseLog(trJson, fc.getFinancialContractUuid());
				trList.set(trList.indexOf(trJson), editTr);
				break;
			}
		}
		fc.setTemporaryRepurchaseWithList(trList);
		financialContractService.saveOrUpdate(fc);
		
		TemporaryRepurchaseLog newLog = new TemporaryRepurchaseLog(editTr, fc.getFinancialContractUuid());
		SystemOperateLogRequestParam param = getSystemOperateLogRequestParam(principal, request, oldLog, newLog);
		systemOperateLogHandler.generateSystemOperateLog(param);
		
		return null;
	}
	
	
	private SystemOperateLogRequestParam getSystemOperateLogrequestParam(Principal principal,HttpServletRequest request, TemporaryRepurchaseLog log, LogFunctionType functionType, LogOperateType operateType) {
		SystemOperateLogRequestParam param = new SystemOperateLogRequestParam(
				principal.getId(), IpUtil.getIpAddress(request), log.getRepurchaseDate(),
				functionType, operateType,TemporaryRepurchaseLog.class, log, null, null);
		return param;
	}

	
	private SystemOperateLogRequestParam getSystemOperateLogRequestParam(Principal principal, HttpServletRequest request, TemporaryRepurchaseLog oldLog, TemporaryRepurchaseLog newLog) {
		SystemOperateLogRequestParam param = new SystemOperateLogRequestParam(
				principal.getId(), IpUtil.getIpAddress(request),
				oldLog.getRepurchaseDate(),
				LogFunctionType.MODIFY_TEMPORARY_REPURCHASE, LogOperateType.UPDATE,
				TemporaryRepurchaseJson.class, oldLog,
				newLog, null);
		return param;
	}
	
	@Override
	public Integer getModifyFlag(FinancialContract financialContract) {
		List<FinancialContractConfiguration> configurations = financialContractConfigurationService.get_financialContractConfiguration_list(financialContract.getUuid());
		String a = "1";
		String modifyFlagContent = null;
		for(FinancialContractConfiguration configuration : configurations) {
			if(configuration.getCode().equals("modifyFlag")) {
				modifyFlagContent = configuration.getContent();
			}
		}
		if(StringUtils.isEmpty(modifyFlagContent)) {
			if(financialContract.isUnusualModifyFlag()) {
				return 1;
			} else {
				return 0;
			}
		} else {
			return Integer.parseInt(modifyFlagContent);
		}
	}

	@Override
	public void modifyFinancialContractConfiguration(String financialContractUuid, String code, Integer content) throws ModifyException {
		if(content== null || StringUtils.isEmpty(financialContractUuid) || StringUtils.isEmpty(code)){
			throw new ModifyException("请求参数错误！");
		}
		FinancialContractConfiguration configuration = financialContractConfigurationService.getFinancialContractConfig(financialContractUuid, code);
		if (configuration == null) {
			if (content != 1) {
				return;
			}
			configuration = new FinancialContractConfiguration(financialContractUuid,code,content+"");
			financialContractConfigurationService.save(configuration);
		} else {
			configuration.setContent(content + "");
			financialContractConfigurationService.update(configuration);
		}
	}

	@Override
	public void sendRequestToCitiGroupForModifyBankSavingLoan(BigDecimal amount,FinancialContract financialContract) {
		
		  Map<String, String> sendToCitiGroup = new HashMap<>();
		  
         BalanceRequestModel balanceRequestModel = new BalanceRequestModel(financialContract.getFinancialContractUuid(),financialContract.getContractName(),amount);
          
         sendToCitiGroup.put(ZhonghangResponseMapSpec.BALANCEREQUESTMODEL, JsonUtils.toJsonString(balanceRequestModel));
          
         HttpClientUtils.executePostRequest(urlToCitigroupForUpdateBankSavingLoan, sendToCitiGroup, null);
		
	}
}