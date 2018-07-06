package com.zufangbao.earth.handler;

import com.zufangbao.earth.model.report.UserInfoModel;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.yunxin.entity.Message;

public interface MessageHandler {
	void generateMessage(UserInfoModel userInfoModel,String publicKey,String sourceUuid);

	Long updateMessageBackPrincipalId(Message message, Integer result, String feedback);

    Principal createPrincipal(Message message, Integer result);

}
