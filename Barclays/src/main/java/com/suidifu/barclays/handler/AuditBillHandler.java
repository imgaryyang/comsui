package com.suidifu.barclays.handler;

import java.util.List;
import java.util.Map;

import com.suidifu.barclays.exception.PullAuditBillException;
import com.zufangbao.sun.yunxin.entity.barclays.ThirdPartyAuditBill;

public interface AuditBillHandler {

	List<ThirdPartyAuditBill> execPullThirdPartyAuditBill(Map<String, String> workingParms) throws PullAuditBillException;
}
