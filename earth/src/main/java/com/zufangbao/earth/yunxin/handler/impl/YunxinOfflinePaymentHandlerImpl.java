package com.zufangbao.earth.yunxin.handler.impl;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.yunxin.handler.YunxinOfflinePaymentHandler;
import com.zufangbao.sun.yunxin.entity.model.OfflineBillQueryModel;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.OfflineBillModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("yunxinOfflinePaymentHandler")
public class YunxinOfflinePaymentHandlerImpl implements
		YunxinOfflinePaymentHandler {

	@Autowired
	private GenericDaoSupport genericDaoSupport;

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryAllOfflineBillCorrespondingSourceDocumentByAuditStatus(
			OfflineBillQueryModel offlineBillQueryModel, Page page) {
		if(CollectionUtils.isEmpty(offlineBillQueryModel.getFinancialContractUuidList())) {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("size", 0);
			resultMap.put("list", Collections.emptyList());
			return resultMap;
		}
		
		StringBuffer querySentence = new StringBuffer("SELECT new com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.OfflineBillModel(a,b) FROM OfflineBill as a, SourceDocument as b WHERE a.offlineBillUuid = b.outlierDocumentUuid");
		querySentence.append(" AND a.financialContractUuid IN (:financialContractUuidList)");
		if (!StringUtils.isEmpty(offlineBillQueryModel.getPayAcNo())) {
			querySentence.append(" AND a.payerAccountNo LIKE :payerAccountNo");
		}
		if (!StringUtils.isEmpty(offlineBillQueryModel.getAccountName())) {
			querySentence.append(" AND a.payerAccountName LIKE :payerAccountName");
		}
		querySentence.append(offlineBillQueryModel.getOrderBySentence());
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("financialContractUuidList", offlineBillQueryModel.getFinancialContractUuidList());
		parameters.put("payerAccountName", "%"+offlineBillQueryModel.getAccountName()+"%");
		parameters.put("payerAccountNo", "%"+offlineBillQueryModel.getPayAcNo()+"%");
		
		List<OfflineBillModel> result = genericDaoSupport.searchForList(querySentence.toString(), parameters, page.getBeginIndex(), page.getEveryPage());
		
		List<OfflineBillModel> all = genericDaoSupport.searchForList(querySentence.toString(), parameters);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("size", all.size());
		resultMap.put("list", result);
		return resultMap;
	}
	
	@Override
	public Map<String, Object> amountStatistics(OfflineBillQueryModel offlineBillQueryModel){
		if(CollectionUtils.isEmpty(offlineBillQueryModel.getFinancialContractUuidList()) || offlineBillQueryModel.getFinancialContractUuidList().size() > 1) {
			return Collections.EMPTY_MAP;
		}
		
		StringBuffer querySentence = new StringBuffer("SELECT SUM(a.amount) AS paymentAmount FROM OfflineBill as a, SourceDocument as b WHERE a.offlineBillUuid = b.outlierDocumentUuid");
		querySentence.append(" AND a.financialContractUuid IN (:financialContractUuidList)");
		if (!StringUtils.isEmpty(offlineBillQueryModel.getPayAcNo())) {
			querySentence.append(" AND a.payerAccountNo LIKE :payerAccountNo");
		}
		if (!StringUtils.isEmpty(offlineBillQueryModel.getAccountName())) {
			querySentence.append(" AND a.payerAccountName LIKE :payerAccountName");
		}
		querySentence.append(offlineBillQueryModel.getOrderBySentence());
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("financialContractUuidList", offlineBillQueryModel.getFinancialContractUuidList());
		parameters.put("payerAccountName", "%"+offlineBillQueryModel.getAccountName()+"%");
		parameters.put("payerAccountNo", "%"+offlineBillQueryModel.getPayAcNo()+"%");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<BigDecimal> obList = genericDaoSupport.searchForList(querySentence.toString(), parameters);
		BigDecimal paymentAmount = obList.get(0) == null ? BigDecimal.ZERO : obList.get(0);
		resultMap.put("paymentAmount", paymentAmount);
		return resultMap;
	}
}
