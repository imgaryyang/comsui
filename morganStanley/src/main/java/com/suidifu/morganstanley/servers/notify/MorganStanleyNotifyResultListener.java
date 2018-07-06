package com.suidifu.morganstanley.servers.notify;

import com.suidifu.morganstanley.configuration.MorganStanleyNotifyConfig;
import com.suidifu.swift.notifyserver.notifyserver.NotifyJob;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultDelegateHandler;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultListener;
import com.zufangbao.sun.utils.SpringContextUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 扣款和签约的监听器
 *
 * @author wukai
 */
@Component("morganStanleyNotifyResultListener")
@Log4j2
public class MorganStanleyNotifyResultListener implements NotifyResultListener {
    private NotifyResultDelegateHandler notifyResultDelegateHandler;

    private Map<String, String> groupNameAndResultHandlerMapping = null;

    @Resource
    private MorganStanleyNotifyConfig morganStanleyNotifyConfig;

    @PostConstruct
    public void init() {
        groupNameAndResultHandlerMapping = new HashMap<String, String>(4) {
            private static final long serialVersionUID = -4323389508154647259L;

            {
                String groupNameForModifyPlan = morganStanleyNotifyConfig.getFileNotifyGroupName();
                // TODO modifyPlanNotifyResultDelegateHandler 移到配置中
                put(groupNameForModifyPlan, "modifyPlanNotifyResultDelegateHandler");

                String groupNameForSignup = morganStanleyNotifyConfig.getSignUpGroupName();
                put(groupNameForSignup, "channelsSignUpDelegateHandler");

                String groupName = morganStanleyNotifyConfig.getAsyncGroupName();
                put(groupName, "asyncImportAssetPackageNotifyResultDelegateHandler");

                String callbackGroupName = morganStanleyNotifyConfig.getCallbackGroupName();
                put(callbackGroupName, "asyncImportCallbackNotifyResultDelegateHandler");
                
                String groupNameForTransfer = morganStanleyNotifyConfig.getTransferNotifyGroupName();
                put(groupNameForTransfer, "asyncTransferBillNotifyResultDelegateHandler");

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