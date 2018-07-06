package com.suidifu.microservice.handler.impl;

import com.suidifu.swift.notifyserver.notifyserver.NotifyJob;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultDelegateHandler;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultListener;
import com.zufangbao.sun.utils.SpringContextUtil;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;

//@Component("repaymentOrderForEasyPayNotifyResultListener")
public class RepaymentOrderForEasyPayNotifyResultListener implements NotifyResultListener {

    private static final Log logger = LogFactory.getLog(RepaymentOrderForEasyPayNotifyResultListener.class);

    private NotifyResultDelegateHandler notifyResultDelegateHandler;

    @Value("#{config['notifyserver.groupCacheJobQueueMap_group3']}")
    private String groupNameForDeductBusinessAcceptance;
    ;

    private Map<String, String> groupNameAndResultHandlerMapping = null;

    @PostConstruct
    public void init() {
        groupNameAndResultHandlerMapping = new HashMap<String, String>() {
            {
                put(groupNameForDeductBusinessAcceptance, "repaymentOrderBusinessAcceptanceDelegateHandler");
            }
        };

    }

    @Override
    public void onResult(NotifyJob result) {
        String groupName = result.getGroupName();

        String resultDelegateHandlerBeanName = groupNameAndResultHandlerMapping.get(groupName);

        notifyResultDelegateHandler = SpringContextUtil.getBean(resultDelegateHandlerBeanName);

        notifyResultDelegateHandler.onResult(result);

    }
}