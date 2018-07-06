package com.suidifu.bridgewater.handler.single.v2;

import com.suidifu.bridgewater.deduct.notify.handler.batch.v2.DeductNotifyJobServer;

public interface ZhongHangDeductApplicationBusinessHandler {
	public void execSingleNotifyFordeductApplication(String deductApplicationUuid, DeductNotifyJobServer deductSingleCallBackNotifyJobServer,String groupName, boolean isYunXin) throws Exception;
	
	public void updateDeductExecuteIfFailed(int actualNotifyNumber,int planNotifyNumber,String oppositeKeyWord, String deductApplicationUuid);
}
