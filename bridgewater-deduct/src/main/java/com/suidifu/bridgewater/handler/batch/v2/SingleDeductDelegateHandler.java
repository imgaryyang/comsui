package com.suidifu.bridgewater.handler.batch.v2;

import com.suidifu.bridgewater.api.entity.deduct.batch.v2.BatchDeductApplication;
import com.suidifu.bridgewater.deduct.notify.handler.batch.v2.DeductNotifyJobServer;
import com.suidifu.bridgewater.model.v2.BatchDeductItem;

/**
 * @author wukai
 *  
 *  单扣委托处理器
 * 
 */
public interface SingleDeductDelegateHandler {

	public void pushDeductJob(DeductNotifyJobServer deductNotifyJobServer,BatchDeductApplication batchDeductApplication,BatchDeductItem batchDeductItem, String merId, String secret,String groupName);
}
