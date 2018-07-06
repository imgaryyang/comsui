package com.suidifu.microservice.service;


import com.demo2do.core.service.GenericService;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.VoucherLogIssueStatus;
import com.zufangbao.sun.yunxin.entity.thirdPartVoucher.ThirdPartyPaymentVoucher;

public interface ThirdPartyPaymentVoucherService extends GenericService<ThirdPartyPaymentVoucher> {
	void updateVoucherLogIssueStatus(VoucherLogIssueStatus status, String voucherNo);
}
