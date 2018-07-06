package com.suidifu.microservice.service.impl;


import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.microservice.service.ThirdPartyPaymentVoucherService;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.VoucherLogIssueStatus;
import com.zufangbao.sun.yunxin.entity.thirdPartVoucher.ThirdPartyPaymentVoucher;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component("thirdPartyPaymentVoucherService")
public class ThirdPartyPaymentVoucherServiceImpl extends GenericServiceImpl<ThirdPartyPaymentVoucher>
        implements ThirdPartyPaymentVoucherService {
    @Override
    public void updateVoucherLogIssueStatus(VoucherLogIssueStatus voucherLogIssueStatus, String voucherNo) {
        String hql = "update ThirdPartyPaymentVoucher set voucherLogIssueStatus=:voucherLogIssueStatus where voucherNo =:voucherNo";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("voucherLogIssueStatus", voucherLogIssueStatus);
        parameters.put("voucherNo", voucherNo);

        this.genericDaoSupport.executeHQL(hql, parameters);
    }
}