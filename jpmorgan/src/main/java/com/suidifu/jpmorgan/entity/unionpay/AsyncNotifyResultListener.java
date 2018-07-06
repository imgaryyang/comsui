package com.suidifu.jpmorgan.entity.unionpay;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.swift.notifyserver.notifyserver.NotifyJob;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultDelegateHandler;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 回调结果监听
 * @author wsh
 *
 */
@Component("asyncNotifyResultListener")
public class AsyncNotifyResultListener implements NotifyResultListener {

	private static Log logger = LogFactory.getLog(AsyncNotifyResultListener.class);

	@Override
	public void onResult(NotifyJob result) {

	}

}









