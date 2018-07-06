package com.suidifu.microservice.service.impl;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.service.SourceDocumentDetailService;
import com.suidifu.microservice.service.SourceDocumentService;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.TAccountInfo;
import com.zufangbao.sun.yunxin.entity.SourceDocumentStatus;
import com.zufangbao.sun.yunxin.entity.api.VoucherSource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service("sourceDocumentService")
public class SourceDocumentServiceImpl extends GenericServiceImpl<SourceDocument> implements SourceDocumentService {
    @Resource
    private SourceDocumentDetailService sourceDocumentDetailService;

    @Override
    public SourceDocument getSourceDocumentBy(String sourceDocumentUuid) {
        if (StringUtils.isEmpty(sourceDocumentUuid)) {
            return null;
        }

        List<SourceDocument> sourceDocumentList = genericDaoSupport.searchForList(
                "FROM SourceDocument WHERE sourceDocumentUuid =:sourceDocumentUuid",
                "sourceDocumentUuid", sourceDocumentUuid);
        if (CollectionUtils.isEmpty(sourceDocumentList)) {
            return null;
        }
        return sourceDocumentList.get(0);
    }

    @Override
    public String getUnWriteOffSourceDocumentUuidBy(String deductApplicationUuid) {
        String queryString = "select sourceDocumentUuid from  SourceDocument where outlierDocumentUuid =:deductApplicationUuid and  sourceDocumentStatus =:sourceDocumentStatus ";

        Map<String, Object> params = new HashMap<>();
        params.put("deductApplicationUuid", deductApplicationUuid);
        params.put("sourceDocumentStatus", SourceDocumentStatus.CREATE);
        List<String> sourceDocumentUuidList = genericDaoSupport.searchForList(queryString, params);
        if (CollectionUtils.isEmpty(sourceDocumentUuidList)) {
            return null;
        }
        return sourceDocumentUuidList.get(0);
    }

    @Override
    public void signSourceDocument(SourceDocument sourceDocument, BigDecimal issuedAmount) {
        sourceDocument.setSourceDocumentStatus(SourceDocumentStatus.SIGNED);
        sourceDocument.setIssuedTime(new Date());
        sourceDocument.changeBookingAmountAndAuditStatus(issuedAmount);
        update(sourceDocument);
    }

    @Override
    public SourceDocument getActiveVoucher(String cashFlowUuid, SourceDocumentStatus sourceDocumentStatus, String contractUuid, String financialContractUuid) {
        List<SourceDocument> sourceDocuments = getDepositReceipt(cashFlowUuid, sourceDocumentStatus, contractUuid, financialContractUuid);
        if (CollectionUtils.isEmpty(sourceDocuments)) {
            return null;
        }
        SourceDocument sourceDocument = sourceDocuments.get(0);
        boolean existsActivePaymentVoucher = sourceDocumentDetailService.exist(sourceDocument.getSourceDocumentUuid(), VoucherSource.ACTIVE_PAYMENT_VOUCHER.getKey(), sourceDocument.getOutlierSerialGlobalIdentity());
        if (!existsActivePaymentVoucher) {
            return null;
        }
        return sourceDocument;
    }

    @Override
    public List<SourceDocument> getDepositReceipt(String cashFlowUuid, SourceDocumentStatus sourceDocumentStatus, String contractUuid, String financialContractUuid) {
        StringBuffer querySentence = new StringBuffer("From SourceDocument where outlierDocumentUuid=:cashFlowUuid And sourceDocumentStatus=:sourceDocumentStatus");
        HashMap<String, Object> parameter = new HashMap<>();
        if (!StringUtils.isEmpty(contractUuid)) {
            querySentence.append(" AND relatedContractUuid=:contractUuid");
            parameter.put("contractUuid", contractUuid);
        }
        if (!StringUtils.isEmpty(financialContractUuid)) {
            querySentence.append(" AND financialContractUuid=:financialContractUuid");
            parameter.put("financialContractUuid", financialContractUuid);
        }
        TAccountInfo account = ChartOfAccount.EntryBook().get(ChartOfAccount.SND_LIABILITIES_INDEPENDENT_CUSTOMER_DEPOSIT);
        querySentence.append(build_3lvl_filter(account));
        querySentence.append(" order by id");
        parameter.put("sourceDocumentStatus", sourceDocumentStatus);
        parameter.put("cashFlowUuid", cashFlowUuid);

        return genericDaoSupport.searchForList(querySentence.toString(), parameter);
    }

    private String build_3lvl_filter(TAccountInfo account) {
        String accountFilter = "";
        if (account.getFirstLevelAccount() != null && !StringUtils.isEmpty(account.getFirstLevelAccount().getAccountCode())) {
            accountFilter += " AND firstAccountId='" + account.getFirstLevelAccount().getAccountCode() + "'";
            if (account.getSecondLevelAccount() != null && !StringUtils.isEmpty(account.getSecondLevelAccount().getAccountCode())) {
                accountFilter += " AND secondAccountId='" + account.getSecondLevelAccount().getAccountCode() + "'";
                if (account.getThirdLevelAccount() != null && !StringUtils.isEmpty(account.getThirdLevelAccount().getAccountCode())) {
                    accountFilter += " AND thirdAccountId='" + account.getThirdLevelAccount().getAccountCode() + "'";
                }
            }

        }
        return accountFilter;
    }
}