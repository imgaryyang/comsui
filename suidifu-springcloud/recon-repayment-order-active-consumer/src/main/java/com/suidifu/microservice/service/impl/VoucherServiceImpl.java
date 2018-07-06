package com.suidifu.microservice.service.impl;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.microservice.service.VoucherService;
import com.suidifu.microservice.model.SourceDocument;
import com.suidifu.microservice.model.Voucher;
import com.suidifu.owlman.microservice.enumation.SourceDocumentDetailStatus;
import com.zufangbao.sun.utils.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

@Service("voucherService")
public class VoucherServiceImpl extends GenericServiceImpl<Voucher>  implements VoucherService {

    @Override
    public Voucher get_voucher_by_sourceDocument(SourceDocument sourceDocument) {
        if(sourceDocument == null) {
            return null;
        }
        String voucherUuid = sourceDocument.getVoucherUuid();
        String globalIdentity = sourceDocument.getOutlierSerialGlobalIdentity();
        if(StringUtils.isEmpty(voucherUuid) || StringUtils.isEmpty(globalIdentity)) {
            return null;
        }
        StringBuffer buffer = new StringBuffer("FROM Voucher WHERE uuid = :voucherUuid AND secondNo = :secondNo AND status <> :status");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.putIfAbsent("voucherUuid", voucherUuid);
        parameters.putIfAbsent("secondNo", globalIdentity);
        parameters.putIfAbsent("status", SourceDocumentDetailStatus.INVALID);
        List<Voucher> result = this.genericDaoSupport.searchForList(buffer.toString(), parameters);
        if(CollectionUtils.isEmpty(result)) {
            return null;
        }else {
            return result.get(0);
        }
    }

}
