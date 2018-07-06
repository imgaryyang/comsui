package com.suidifu.microservice.service.impl;


import com.demo2do.core.persistence.support.Filter;
import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.microservice.service.ThirdPartyPaymentVoucherDetailService;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.entity.thirdPartVoucher.ThirdPartyPaymentVoucherDetail;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

@Component("thirdPartyPaymentVoucherDetailService")
public class ThirdPartyPaymentVoucherDetailServiceImpl extends GenericServiceImpl<ThirdPartyPaymentVoucherDetail>
        implements ThirdPartyPaymentVoucherDetailService {
    @Override
    public ThirdPartyPaymentVoucherDetail getThirdPartyPaymentVoucherDetailByUuid(String detailUuid) {
        if (StringUtils.isEmpty(detailUuid)) {
            return null;
        }
        Filter filter = new Filter();
        filter.addEquals("detailUuid", detailUuid);

        List<ThirdPartyPaymentVoucherDetail> results = this.list(ThirdPartyPaymentVoucherDetail.class, filter);
        if (CollectionUtils.isEmpty(results)) {
            return null;
        }
        return results.get(0);
    }

    @Override
    public List<ThirdPartyPaymentVoucherDetail> getThirdPartyPaymentVoucherDetailByVoucherUuid(String voucherUuid) {
        if (StringUtils.isEmpty(voucherUuid)) {
            return Collections.emptyList();
        }

        Map<String, Object> params = new HashMap<>();
        String hql = " FROM ThirdPartyPaymentVoucherDetail WHERE voucherUuid = :voucherUuid ";
        params.put("voucherUuid", voucherUuid);

        return genericDaoSupport.searchForList(hql, params);
    }
}