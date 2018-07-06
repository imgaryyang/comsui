package com.suidifu.bridgewater.handler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zufangbao.gluon.util.SpringContextUtil;

public interface IDeductAsyncNotifyHandler {

	
	public static final Map<DeductNotifyType, String> handlerMap = new HashMap<DeductNotifyType,String>(){
		{
			put(DeductNotifyType.COMMON,"deductAsyncNotifyHandler");
			
		}
	};
	
	public static IDeductAsyncNotifyHandler getNotifyHandler(String notifyType){
		
		DeductNotifyType notifyTypeEnum = DeductNotifyType.fromKey(notifyType);
		return getHandler(notifyTypeEnum);
		
	}
	
	
	public static IDeductAsyncNotifyHandler getHandler(DeductNotifyType notifyTypeEnum) {
		String BeanName = handlerMap.get(notifyTypeEnum);
		IDeductAsyncNotifyHandler deductAsyncNotifyHandler = SpringContextUtil.getBean(BeanName);
		return deductAsyncNotifyHandler;
	}
	
	
	public void processingdeductCallback(String deductApplicationUuid);
	
	public  List<String> getWaitingNoticedeductApplicationBy(int pageSize, Date queryStartDate);
	
}
