package com.suidifu.bridgewater.deduct.notify.handler.batch.v2;

import com.alibaba.fastjson.JSON;
import com.demo2do.core.utils.StringUtils;
import com.suidifu.bridgewater.handler.DeductApplicationBusinessHandler;
import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductRequestModel;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.swift.notifyserver.notifyserver.NotifyJob;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultDelegateHandler;
import com.zufangbao.sun.api.model.deduct.DeductApplicationReceiveStatus;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;

import java.util.Map;


/**
 * 
 * @author zhanglonfei
 *
 */
@Component("sendCitigroupNotifyResultDelegateHandler")
public class SendCitigroupNotifyResultDelegateHandler implements NotifyResultDelegateHandler {

	private static final Log logger = LogFactory.getLog(SendCitigroupNotifyResultDelegateHandler.class);

	@Autowired
	private DeductApplicationService deductApplicationService;

	@Autowired
	private DeductApplicationBusinessHandler deductApplicationBusinessHandler;

	@Override
	public void onResult(NotifyJob result) {
		
		Map<String,String> requestParameters = JSON.parseObject(result.getRequestParamJson(),Map.class);
		if (MapUtils.isEmpty(requestParameters)){
			logger.error("sendCitigroupNotifyResultDelegateHandler error#result["+JsonUtils.toJsonString(result)+"]");
			return;
		}
		String deductRequestModelString = requestParameters.getOrDefault(ZhonghangResponseMapSpec.DEDUCTREQUESTMODEL, StringUtils.EMPTY);
		DeductRequestModel deductRequestModel = JsonUtils.parse(deductRequestModelString, DeductRequestModel.class);
		if (deductRequestModel == null){
			logger.error("sendCitigroupNotifyResultDelegateHandler error deductRequestModel is null#result["+JsonUtils.toJsonString(result)+"]");
			return;
		}
		String deductApplicationUuid = deductRequestModel.getDeductApplicationUuid();
		String checkResponseNo = deductRequestModel.getCheckResponseNo();
		
		if(!NotifyJob.RESPONSE_CODE_200_OK.equals(result.getLastTimeHttpResponseCode())) {

			logger.info("sendCitigroupNotifyResultDelegateHandler TIMEOUT lastTimeHttpResponseCode:"+result.getLastTimeHttpResponseCode()+"#result["+JsonUtils.toJsonString(result)+"]");

			DeductApplication deductApplication = deductApplicationService.getDeductApplicationByDeductApplicationUuid(deductApplicationUuid);

			if (!deductApplication.getCheckResponseNo().equals(checkResponseNo)){
				return;
			}
			deductApplicationBusinessHandler.updatDeductApplicationToCitigroup(deductApplication, DeductApplicationReceiveStatus.TIMEOUT.ordinal(), checkResponseNo);

		}
	}

}
