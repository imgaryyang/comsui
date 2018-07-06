package com.suidifu.microservice.service;

import com.demo2do.core.service.GenericService;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.VoucherLogIssueStatus;
import com.zufangbao.sun.yunxin.entity.thirdPartVoucher.ThirdPartyVoucherCommandLog;

public interface ThirdPartyVoucherCommandLogService extends GenericService<ThirdPartyVoucherCommandLog> {
	void updateTransactionCommandVoucherLogIssueStatus(VoucherLogIssueStatus voucherLogIssueStatus, String voucherNo);
}