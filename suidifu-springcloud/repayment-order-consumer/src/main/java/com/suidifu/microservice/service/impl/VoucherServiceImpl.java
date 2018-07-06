package com.suidifu.microservice.service.impl;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.entity.Voucher;
import com.suidifu.microservice.service.VoucherService;
import com.suidifu.owlman.microservice.enumation.SourceDocumentDetailStatus;
import com.zufangbao.sun.utils.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

@Service("voucherService")
public class VoucherServiceImpl extends GenericServiceImpl<Voucher> implements VoucherService {
    @SuppressWarnings("unchecked")
    @Override
    public Voucher getVoucherBy(SourceDocument sourceDocument) {
        if (sourceDocument == null) {
            return null;
        }
        String voucherUuid = sourceDocument.getVoucherUuid();
        String globalIdentity = sourceDocument.getOutlierSerialGlobalIdentity();
        if (StringUtils.isEmpty(voucherUuid) || StringUtils.isEmpty(globalIdentity)) {
            return null;
        }
        Map<String, Object> parameters = new HashMap<>();
        parameters.putIfAbsent("voucherUuid", voucherUuid);
        parameters.putIfAbsent("secondNo", globalIdentity);
        parameters.putIfAbsent("status", SourceDocumentDetailStatus.INVALID);
        List<Voucher> result = this.genericDaoSupport.searchForList("FROM Voucher WHERE " +
                        "uuid = :voucherUuid AND secondNo = :secondNo AND status <> :status",
                parameters);
        if (CollectionUtils.isEmpty(result)) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public String get_unique_valid_voucher_uuid(String cashFlowUuid) {
        if (StringUtils.isEmpty(cashFlowUuid)) {
            return "";
        }
        String sql = "SELECT uuid FROM t_voucher WHERE cash_flow_uuid = :cashFlowUuid AND status != :invalid";
        Map<String, Object> params = new HashMap<>();
        params.put("cashFlowUuid", cashFlowUuid);
        params.put("invalid", SourceDocumentDetailStatus.INVALID.ordinal());
        List<String> voucherUuidList = genericDaoSupport.queryForSingleColumnList(sql, params, String.class);
        if (CollectionUtils.isEmpty(voucherUuidList) || voucherUuidList.size() > 1) {
            return "";
        }
        return voucherUuidList.get(0);
    }
}