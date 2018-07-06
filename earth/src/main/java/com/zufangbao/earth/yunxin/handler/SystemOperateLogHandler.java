package com.zufangbao.earth.yunxin.handler;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.sun.entity.directbank.business.AppArriveRecord;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLogRequestParam;
import com.zufangbao.sun.yunxin.log.SystemOperateLogVO;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.VoucherOperationInfo;

import java.util.List;
import java.util.Map;

public interface SystemOperateLogHandler {

	void generateSystemOperateLog(SystemOperateLogRequestParam param)
			throws Exception;

	void generateAssociateSystemLog(Principal principal, String ip,
                                    String offlineBillUuid, Map<String, Object> map) throws Exception;

	void generateCashFLowAuditSystemLog(VoucherOperationInfo voucherOperationInfo, AppArriveRecord appArriveRecord)throws Exception;
	
	List<SystemOperateLogVO> getSystemOperateLogVOListBy(
            String objectUuid, Page page);

	List<SystemOperateLogVO> getLogVOListByUuid(String uuid);

	void operateLog(Principal principal, String ip, LogFunctionType functionType,
                    LogOperateType logOperateType, Object object, String keyContent, String recordContent) throws Exception;
	
	
}

