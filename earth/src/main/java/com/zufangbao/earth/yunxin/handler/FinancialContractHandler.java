package com.zufangbao.earth.yunxin.handler;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.TemporaryRepurchaseJson;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.yunxin.entity.model.CreateFinancialContractModel;
import com.zufangbao.sun.yunxin.entity.model.ModifyFinancialContractBasicInfoModel;
import com.zufangbao.sun.yunxin.entity.model.ModifyFinancialContractRemittanceInfoModel;
import com.zufangbao.sun.yunxin.entity.model.ModifyFinancialContractRepaymentInfoModel;
import com.zufangbao.sun.yunxin.entity.model.financialcontract.FinancialContractQueryModel;

public interface FinancialContractHandler {
	public FinancialContract modifyFinancialContractBasicInfo(ModifyFinancialContractBasicInfoModel modifyFinancialContractBasicInfoModel,FinancialContract financialContract) throws ModifyException;
	
	public FinancialContract modifyFinancialContractRemittanceInfo(ModifyFinancialContractRemittanceInfoModel modifyFinancialContractRemittanceInfoModel,FinancialContract financialContract) throws ModifyException;
	
	public FinancialContract modifyFinancialContractRepaymentInfo(ModifyFinancialContractRepaymentInfoModel modifyFinancialContractRepaymentInfoModel,FinancialContract financialContract) throws ModifyException;
	
	public Map<String, Object> query(FinancialContractQueryModel financialContractQueryModel, Page page);

	public List<FinancialContract> getFinancialContractList(Long financialContractId);

	public FinancialContract createNewFinancialContract(CreateFinancialContractModel createFinancialContractModel, Long principalId) throws CreateException;

	public String createTemporaryRepurchases(Principal principal, HttpServletRequest request, TemporaryRepurchaseJson tr, FinancialContract fc) throws Exception;

	public String deleteTemporaryRepurchases(Principal principal, HttpServletRequest request, TemporaryRepurchaseJson deleteTr, FinancialContract fc) throws Exception;

	public String editTemporaryRepurchases(Principal principal, HttpServletRequest request, TemporaryRepurchaseJson editTr, FinancialContract fc) throws Exception;

	public Integer getModifyFlag(FinancialContract financialContract);

	void modifyFinancialContractConfiguration(String financialContractUuid, String code, Integer content)
			throws ModifyException;
	
	public FinancialContract configFinancialContract(CreateFinancialContractModel createFinancialContractModel, Long principalId);

	void sendRequestToCitiGroupForModifyBankSavingLoan(BigDecimal amount,FinancialContract financialContract);
}
