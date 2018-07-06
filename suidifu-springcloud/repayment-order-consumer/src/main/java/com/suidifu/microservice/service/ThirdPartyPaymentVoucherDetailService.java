package com.suidifu.microservice.service;


import com.demo2do.core.service.GenericService;
import com.zufangbao.sun.yunxin.entity.thirdPartVoucher.ThirdPartyPaymentVoucherDetail;
import java.util.List;

public interface ThirdPartyPaymentVoucherDetailService extends GenericService<ThirdPartyPaymentVoucherDetail> {
    ThirdPartyPaymentVoucherDetail getThirdPartyPaymentVoucherDetailByUuid(String detailUuid);

    List<ThirdPartyPaymentVoucherDetail> getThirdPartyPaymentVoucherDetailByVoucherUuid(String voucherUuid);
}