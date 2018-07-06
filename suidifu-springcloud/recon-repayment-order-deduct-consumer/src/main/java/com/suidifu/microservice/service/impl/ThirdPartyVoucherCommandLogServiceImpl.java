package com.suidifu.microservice.service.impl;

import com.demo2do.core.persistence.support.Filter;
import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.microservice.service.ThirdPartyVoucherCommandLogService;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.VoucherLogIssueStatus;
import com.zufangbao.sun.yunxin.entity.thirdPartVoucher.ThirdPartyVoucherCommandLog;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

@Component("thirdPartyVoucherCommandLogService")
public class ThirdPartyVoucherCommandLogServiceImpl extends GenericServiceImpl<ThirdPartyVoucherCommandLog>
		implements ThirdPartyVoucherCommandLogService {

	@Override
	public void updateTransactionCommandVoucherLogIssueStatus(VoucherLogIssueStatus voucherLogIssueStatus,String voucherNo){
		String hql = "update  ThirdPartyVoucherCommandLog set voucherLogIssueStatus=:voucherLogIssueStatus where voucherNo =:voucherNo";

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("voucherLogIssueStatus", voucherLogIssueStatus);
		parameters.put("voucherNo", voucherNo);

		this.genericDaoSupport.executeHQL(hql, parameters);
	}

	@Override
	public ThirdPartyVoucherCommandLog getThirdPartyVoucherCommandLogByVoucherUuid(String voucherUuid) {
		if (StringUtils.isEmpty(voucherUuid)) {
			return null;
		}
		Filter filter = new Filter();
		filter.addEquals("voucherUuid", voucherUuid);

		List<ThirdPartyVoucherCommandLog> results = this.list(ThirdPartyVoucherCommandLog.class, filter);
		if (CollectionUtils.isEmpty(results)) {
			return null;
		}
		return results.get(0);
	}
}
