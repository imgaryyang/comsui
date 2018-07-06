package com.suidifu.bridgewater.handler;

import com.suidifu.bridgewater.api.model.RemittanceBlackListCommandModel;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceBlackList;

public interface RemittanceBlackListHandler {

	/**
	 * 取消贷款合同（放款黑名单）
	 * @return 
	 */
	public void recordRemittanceBlackList(RemittanceBlackListCommandModel model, String ip, String creatorName);


}
