package com.suidifu.bridgewater.task;

import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suidifu.bridgewater.api.model.BasicTask;
import com.suidifu.bridgewater.handler.IRemittanceApplicationHandler;
import com.suidifu.bridgewater.handler.RemittanceTaskNoSession;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplication;

/**
 * 主动将校验超时且状态处于校验中的ra发送给MorganStanly进行业务校验
 *
 * @author wsh
 */
@Component("bussinessVerifyTask")
public class BussinessVerifyTask extends BasicTask {

	private static Log logger = LogFactory.getLog(BussinessVerifyTask.class);

	@Autowired
	private IRemittanceApplicationHandler iRemittanceApplicationHandler;

	@Autowired
	private RemittanceTaskNoSession remittanceTaskNoSession;

	public void run() {
		long start = System.currentTimeMillis();
		String noticeBatchNo = "noticeBatchNo:[" + UUID.randomUUID().toString() + "]";

		logger.info("#execSendMorganstanleyForBussinessVerifyFromDB begin." + noticeBatchNo);

		int limit = getIntegetFromWorkParamOrDefault(RemittanceTaskSpec.PARAM_LIMIT_SIZE,
				RemittanceTaskSpec.DEFAULT_LIMIT_SIZE);
		long sleepMillis = getLongFromWorkParamOrDefault(RemittanceTaskSpec.PARAM_SLEEP_MILLIS,
				RemittanceTaskSpec.DEFAULT_SLEEP_MILLIS);

		List<RemittanceApplication> remittanceApplicationList = iRemittanceApplicationHandler
				.getRemittanceApplicationInNotVerify(limit);

		if (CollectionUtils.isEmpty(remittanceApplicationList)) {
			logger.info("#待补漏给Morganstanley总计（0）条 !" + noticeBatchNo);
			return;
		}

		logger.info("#待补漏给Morganstanley总计（" + remittanceApplicationList.size() + "）条 !" + noticeBatchNo);

		for (RemittanceApplication remittanceApplication : remittanceApplicationList) {

			try {
				remittanceTaskNoSession.sendToCitigroupForBussinessValidation(remittanceApplication);
				Thread.sleep(sleepMillis);
			} catch (Exception e) {
				logger.error("#execSendMorganstanleyForBussinessVerifyFromDB occur error.noticeBatchNo:["
						+ noticeBatchNo + "],remittanceApplicationUuid:["
						+ remittanceApplication.getRemittanceApplicationUuid() + "]");
				e.printStackTrace();

			}
		}
		long end = System.currentTimeMillis();
		logger.info(
				"#execSendMorganstanleyForBussinessVerifyFromDB end. used [" + (end - start) + "]ms" + noticeBatchNo);
	}
}
