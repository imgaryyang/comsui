package com.suidifu.bridgewater.task.v2;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.suidifu.bridgewater.handler.DeductApplicationBusinessHandler;
import com.suidifu.bridgewater.model.v2.BasicTask;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;

@Component("zhongHangDeductFillInfoTask")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ZhongHangDeductFillInfoTask extends BasicTask {
	
	 private static final Log Logger = LogFactory.getLog(ZhongHangDeductFillInfoTask.class);
	 
	 @Autowired
	 private DeductApplicationService deductApplicationService;
	 
	 @Autowired
	 private DeductApplicationBusinessHandler deductApplicationBusinessHandler;

	@Deprecated
	public void run(){
		
		try {
			
			List<String> deductApplicationUuids = deductApplicationService.getDeductApplicationUuidsNeedFillInfo();
			
			Logger.info(".#ZhongHangDeductFillInfoTask doFillInfoByDeductApplicationUuids start totalCount["+deductApplicationUuids.size()+"]");
			
			doFillInfoByDeductApplicationUuids(deductApplicationUuids);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			Logger.error(".#ZhongHangDeductFillInfoTask occours error,  error message["+e.getMessage()+"]");
			
		}
	}

	public void doFillInfoByDeductApplicationUuids(List<String> deductApplicationUuids){
		
		long startTime = System.currentTimeMillis();
		
		for (String deductApplicationUuid : deductApplicationUuids) {
			
			try {
				
				deductApplicationBusinessHandler.doFillInfoByDeductApplicationUuid(deductApplicationUuid);
				
			} catch (Exception e) {
				
				Logger.error(".#doFillInfoByDeductApplicationUuids occours error,  error message["+e.getMessage()+"]  deductApplicationUuid ["+deductApplicationUuids+"]");
			}
			
		}
		
		Logger.info(".#doFillInfoByDeductApplicationUuids success, used time ["+(System.currentTimeMillis()-startTime)+"]");
		
	}
	
	
}
