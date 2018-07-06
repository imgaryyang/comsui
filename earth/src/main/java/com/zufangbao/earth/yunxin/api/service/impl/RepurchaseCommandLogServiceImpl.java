package com.zufangbao.earth.yunxin.api.service.impl;

import com.demo2do.core.persistence.support.Filter;
import com.demo2do.core.service.impl.GenericServiceImpl;
import com.zufangbao.earth.yunxin.api.model.command.RepurchaseCommandModel;
import com.zufangbao.earth.yunxin.api.model.logs.RepurchaseCommandLog;
import com.zufangbao.earth.yunxin.api.service.RepurchaseCommandLogService;
import com.zufangbao.gluon.exception.ApiException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.REPEAT_BATCH_NO;
import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.REPEAT_REQUEST_NO;

@Service("repurchaseCommandLogService")
public class RepurchaseCommandLogServiceImpl extends GenericServiceImpl<RepurchaseCommandLog> implements RepurchaseCommandLogService  {
	private static final int submit = 0;
	private static final int undo = 1;
	
	@Override
	public void checkByRequestNo(String requestNo) throws ApiException {
		Filter filter = new Filter();
		filter.addEquals("requestNo", requestNo);
		List<RepurchaseCommandLog> result = this.list(RepurchaseCommandLog.class, filter);
		if(CollectionUtils.isNotEmpty(result)) {
			throw new ApiException(REPEAT_REQUEST_NO);
		}
	}
	
	@Override
	public void checkByBatchNo(String batchNo, int transactionType) throws ApiException {
		if(StringUtils.isEmpty(batchNo)){
			return;
		}
		Filter filter = new Filter();
		filter.addEquals("batchNo", batchNo);
		filter.addEquals("transactionType", transactionType);
		List<RepurchaseCommandLog> result = this.list(RepurchaseCommandLog.class, filter);
		if(CollectionUtils.isNotEmpty(result)) {
			throw new ApiException(REPEAT_BATCH_NO);
		}
	}

	@Override
	public void saveRepurchaseCommandLog(RepurchaseCommandModel model, String ip) {
		RepurchaseCommandLog log = new RepurchaseCommandLog(model, ip);
		this.save(log);
	}
	
	@Override
	public RepurchaseCommandLog getRepurchaseCommandLogByBatchNo(String batchNo){
		if(StringUtils.isEmpty(batchNo)){
			return null;
		}
		Filter filter = new Filter();
		filter.addEquals("batchNo", batchNo);
		filter.addEquals("transactionType", submit);
		List<RepurchaseCommandLog> repurchaseCommandLogs = list(RepurchaseCommandLog.class, filter);
		if(CollectionUtils.isEmpty(repurchaseCommandLogs)){
			return null;
		}
		return repurchaseCommandLogs.get(0);
	}
}
