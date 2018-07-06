package com.suidifu.microservice.service.impl;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.entity.Voucher;
import com.suidifu.microservice.service.VoucherService;
import com.suidifu.owlman.microservice.enumation.SourceDocumentDetailCheckState;
import com.suidifu.owlman.microservice.enumation.SourceDocumentDetailStatus;
import com.zufangbao.sun.utils.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

@Service("voucherService")
public class VoucherServiceImpl extends GenericServiceImpl<Voucher> implements VoucherService {
    @Override
    public Voucher getVoucherByUuid(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            return null;
        }
        Map<String, Object> parameters = new HashMap<>();
        String hql = "FROM Voucher WHERE uuid =:uuid Order By id DESC";
        parameters.put("uuid", uuid);
        List<Voucher> result = genericDaoSupport.searchForList(hql, parameters);
        if (CollectionUtils.isEmpty(result)) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public void updateCheckStateVoucherState(String voucherUuid,
                                             SourceDocumentDetailCheckState checkState,
                                             SourceDocumentDetailStatus voucherState) {
        if (checkState == null && voucherState == null) {
            return;
        }

        Voucher voucher = getVoucherByUuid(voucherUuid);
        if (voucher != null && voucher.getStatus() != SourceDocumentDetailStatus.INVALID) {
            if (checkState != null) {
                voucher.setCheckState(checkState);
            }
            if (voucherState != null) {
                voucher.setStatus(voucherState);
            }
            update(voucher);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Voucher getVoucherBySourceDocument(SourceDocument sourceDocument) {
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
        List<Voucher> result = genericDaoSupport.searchForList("FROM Voucher WHERE uuid = :voucherUuid AND secondNo = :secondNo AND status <> :status", parameters);
        if (CollectionUtils.isEmpty(result)) {
            return null;
        } else {
            return result.get(0);
        }
    }
}