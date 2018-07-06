package com.suidifu.bridgewater.handler;

import com.zufangbao.sun.api.model.remittance.RemittanceSqlModel;
import com.zufangbao.sun.utils.SpringContextUtil;

import java.util.Date;
import java.util.EnumMap;
import java.util.List;

import com.zufangbao.sun.api.model.remittance.RemittanceProjectName;
import com.zufangbao.sun.api.model.remittance.RemittanceSqlModel;
import com.zufangbao.sun.utils.SpringContextUtil;


/**
 * 将放款结果回调给外部client
 *
 * @author wsh
 */
public interface IRemittanceAsyncNotifyHandler {

	List<String> getWaitingNoticeRemittanceApplicationBy(int pageSize, Date queryStartDate, Date notifyOutlierDelay);

	void pushApplicationsToOutlier(RemittanceSqlModel remittanceSqlMode, String financialContractUuid);
	
	public static EnumMap<RemittanceProjectName, String> notifyHandlerTable=new EnumMap<RemittanceProjectName, String>(RemittanceProjectName.class){

	private static final long serialVersionUID = -6260630874138240959L;

		{
			put(RemittanceProjectName.YUN_XIN, "yXRemittanceAsyncNotifyHandler");
			put(RemittanceProjectName.ZHONG_HANG, "zHRemittanceAsyncNotifyHandler");
			put(RemittanceProjectName.HUA_RUI, "hRRemittanceAsyncNotifyHandler");
		}
	};

	public static IRemittanceAsyncNotifyHandler getNotifyHandler(String notifyType) {
		RemittanceProjectName notifyTypeEnum = RemittanceProjectName.fromKey(notifyType);
		return getNotifyHandler(notifyTypeEnum);
	}
	
	public static IRemittanceAsyncNotifyHandler getNotifyHandler(RemittanceProjectName notifyTypeEnum) {

		String BeanName = notifyHandlerTable.get(notifyTypeEnum);
		IRemittanceAsyncNotifyHandler remittanceAsyncNotifyHandler = SpringContextUtil.getBean(BeanName);
		return remittanceAsyncNotifyHandler;
	}


}
