package com.zufangbao.earth.yunxin.handler.impl;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.web.controller.reapymentInfo.BankCoreCodeMapSpec;
import com.zufangbao.earth.yunxin.handler.BankTransactionLimitSheetHandler;
import com.zufangbao.gluon.util.BeanWrapperUtil;
import com.zufangbao.sun.entity.financial.*;
import com.zufangbao.sun.service.BankTransactionLimitSheetService;
import com.zufangbao.sun.service.FinancialContractConfigService;
import com.zufangbao.sun.service.PaymentChannelInformationService;
import com.zufangbao.sun.yunxin.entity.model.TransactionLimitQueryModel;
import com.zufangbao.sun.yunxin.entity.remittance.AccountSide;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component("bankTransactionLimitSheet")
public class BankTransactionLimitSheetHandlerImpl implements BankTransactionLimitSheetHandler{

	@Autowired
	private BankTransactionLimitSheetService bankTransactionLimitSheetService;
	
	@Autowired
	private FinancialContractConfigService financialContractConfigService;
	
	@Autowired
	private PaymentChannelInformationService paymentChannelInformationService;
	
	@Override
	public List<Map<String, Object>> getAllBanks(String financialContractUuid, BusinessType businessType, AccountSide accountSide) {
		FinancialContractConfig financialContractConfig = financialContractConfigService.getFinancialContractConfigBy(financialContractUuid, businessType);
		if(financialContractConfig == null){
			return Collections.emptyList();
		}
		List<String> paymentChannelUuids = null;
		if(accountSide == AccountSide.CREDIT){
			paymentChannelUuids = financialContractConfig.getPaymentChannelUuidListForCredit();
		}else if(accountSide == AccountSide.DEBIT){
			paymentChannelUuids = financialContractConfig.getPaymentChannelUuidListForDebit();
		}
		if(StringUtils.isEmpty(financialContractUuid)){
			return Collections.emptyList();
		}
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		List<String> allBanks = new ArrayList<String>();
		for (String paymentChannelUuid : paymentChannelUuids) {
			PaymentChannelInformation pci = paymentChannelInformationService.getPaymentChannelInformationBy(paymentChannelUuid);
			if(pci == null){
				continue;
			}
			List<String> bankCodes = bankTransactionLimitSheetService.getAllBanks(pci.getPaymentInstitutionName(), pci.getOutlierChannelName(), accountSide);
			for (String bankCode : bankCodes) {
				if(allBanks.contains(bankCode)){
					continue;
				}
				allBanks.add(bankCode);
				Map<String, Object> dataPart = new HashMap<String, Object>();
				dataPart.put("bankCode", bankCode);
				dataPart.put("bankName", BankCoreCodeMapSpec.coreBankMap.get(bankCode));
				data.add(dataPart);
			}
		}
		return data;
	}

	@Override
	public Map<String, Object> searchBankTransactionLimitBy(TransactionLimitQueryModel queryModel, Page page) {
		List<BankTransactionLimitSheet> bankTransactionLimitSheetList = bankTransactionLimitSheetService.query(queryModel, page);
		int allSize = bankTransactionLimitSheetService.queryCount(queryModel);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("size", allSize);
		List<BankTransactionLimitSheetListModel> listModelList = new ArrayList<BankTransactionLimitSheetListModel>();
		if(CollectionUtils.isNotEmpty(bankTransactionLimitSheetList)){
			listModelList = bankTransactionLimitSheetList.stream().map(a->new BankTransactionLimitSheetListModel(a)).collect(Collectors.toList());
		}
		resultMap.put("list", listModelList);
		return resultMap;
	}

	@Override
	public List<Map<String, Object>> getBankLimitPreview(PaymentInstitutionName paymentInstitutionName, AccountSide accountSide, String outlierChannelName, Page page) {
		List<BankTransactionLimitSheet> sheets = bankTransactionLimitSheetService.getBankTransactionLimitSheetsBy(paymentInstitutionName, accountSide, outlierChannelName, null);
		if(CollectionUtils.isEmpty(sheets)){
			return Collections.emptyList();
		}
		String[] propertyNames = { "bankName", "transactionLimitPerTranscation","transcationLimitPerDay", "transactionLimitPerMonth"};
		List<Map<String, Object>> rtnList = BeanWrapperUtil.wrapperList(sheets, propertyNames);
		return rtnList;
	}

	@Override
	public Map<String, Object> getBankChannelLimitPreview(PaymentInstitutionName paymentInstitutionName, AccountSide accountSide, String outlierChannelName, String bankCode) {
		BankTransactionLimitSheet sheet = bankTransactionLimitSheetService.getBankTransactionLimitSheetBy(paymentInstitutionName, accountSide, outlierChannelName, bankCode);
		Map<String, Object> resultMap = new HashMap<>();
		if(sheet == null){
			return resultMap;
		}
		resultMap.put("bankName", sheet.getBankName());
		resultMap.put("transactionLimitPerTranscation", sheet.getTransactionLimitPerTranscation());
		resultMap.put("transcationLimitPerDay", sheet.getTranscationLimitPerDay());
		resultMap.put("transactionLimitPerMonth", sheet.getTransactionLimitPerMonth());
		return resultMap;
	}
	
	@Override
	public Map<String, Object> searchTransactionLimitBy(
			TransactionLimitQueryModel queryModel, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

}
