package com.zufangbao.earth.yunxin.handler;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.sun.yunxin.entity.model.OfflineBillQueryModel;

import java.util.Map;

public interface YunxinOfflinePaymentHandler {

	
	Map<String, Object> queryAllOfflineBillCorrespondingSourceDocumentByAuditStatus(OfflineBillQueryModel offlineBillQueryModel, Page page);

	Map<String, Object> amountStatistics(OfflineBillQueryModel offlineBillQueryModel);
}
