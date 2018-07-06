package com.suidifu.microservice.service.impl;

import static com.zufangbao.sun.ledgerbook.ChartOfAccount.SND_LIABILITIES_INDEPENDENT_CUSTOMER_DEPOSIT;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.service.SourceDocumentService;
import com.suidifu.owlman.microservice.enumation.SourceDocumentExcuteResult;
import com.suidifu.owlman.microservice.enumation.SourceDocumentExcuteStatus;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.TAccountInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Log4j2
@Service("sourceDocumentService")
public class SourceDocumentServiceImpl extends GenericServiceImpl<SourceDocument> implements SourceDocumentService {
    @Override
    public SourceDocument getSourceDocumentBy(String sourceDocumentUuid) {
        if (StringUtils.isEmpty(sourceDocumentUuid)) {
            return null;
        }

        List<SourceDocument> sourceDocumentList = genericDaoSupport.searchForList("FROM SourceDocument WHERE sourceDocumentUuid =:sourceDocumentUuid", "sourceDocumentUuid", sourceDocumentUuid);
        if (CollectionUtils.isEmpty(sourceDocumentList)) {
            return null;
        }
        return sourceDocumentList.get(0);
    }

    @Override
    public SourceDocument getSourceDocumentByOutlierDocumentUuid(
            String outlierDocumentUuid, String firstOutlierDocType) {

        if (StringUtils.isEmpty(outlierDocumentUuid)) {
            return null;
        }

        String queryString = "from SourceDocument where outlierDocumentUuid =:outlierDocumentUuid AND firstOutlierDocType =:firstOutlierDocType";

        Map<String, Object> params = new HashMap<>();
        params.put("outlierDocumentUuid", outlierDocumentUuid);
        params.put("firstOutlierDocType", firstOutlierDocType);

        List<SourceDocument> sourceDocuments = genericDaoSupport.
                searchForList(queryString, params);
        if (CollectionUtils.isEmpty(sourceDocuments)) {
            return null;
        }
        return sourceDocuments.get(0);
    }

    @Override
    public List<SourceDocument> getDepositSourceDocumentListConnectedBy(SourceDocumentExcuteResult executeResult,
                                                                        SourceDocumentExcuteStatus executeStatus) {
        StringBuilder queryString = new StringBuilder("from SourceDocument where excuteStatus=:excuteStatus "
                + " AND excuteResult=:excuteResult ");
        Map<String, Object> params = new HashMap<>();
        TAccountInfo tAccountInfo = ChartOfAccount.EntryBook().get(SND_LIABILITIES_INDEPENDENT_CUSTOMER_DEPOSIT);
        String queryDepositType = build3LvlFilter(tAccountInfo);
        queryString.append(queryDepositType);
        params.put("excuteResult", executeResult);
        params.put("excuteStatus", executeStatus);
        return genericDaoSupport.searchForList(queryString.toString(), params);
    }

    private String build3LvlFilter(TAccountInfo account) {
        String accountFilter = "";
        if (account.getFirstLevelAccount() != null &&
                !StringUtils.isEmpty(account.getFirstLevelAccount().getAccountCode())) {
            accountFilter += " AND firstAccountId='" + account.getFirstLevelAccount().getAccountCode() + "'";
            if (account.getSecondLevelAccount() != null &&
                    !StringUtils.isEmpty(account.getSecondLevelAccount().getAccountCode())) {
                accountFilter += " AND secondAccountId='" + account.getSecondLevelAccount().getAccountCode() + "'";
                if (account.getThirdLevelAccount() != null &&
                        !StringUtils.isEmpty(account.getThirdLevelAccount().getAccountCode())) {
                    accountFilter += " AND thirdAccountId='" + account.getThirdLevelAccount().getAccountCode() + "'";
                }
            }
        }
        return accountFilter;
    }
}