package com.suidifu.jpmorgan.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.suidifu.jpmorgan.entity.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suidifu.jpmorgan.handler.JobHandler;
import com.suidifu.jpmorgan.handler.TradeScheduleHandler;
import com.suidifu.jpmorgan.service.TradeScheduleService;
import com.suidifu.jpmorgan.spec.DistributeTaskSpec;

@Component("distributeJob")
public class DistributeJob implements JobHandler {
	
	@Autowired
	private TradeScheduleService tradeScheduleService;
	@Autowired
	TradeScheduleHandler tradeScheduleHandler;
	
	private static Log logger = LogFactory.getLog(DistributeJob.class);
		
	@Override
	public void run(Map<String, Object> workingParms) {
			
		try {
			int workerNo = Integer.parseInt(workingParms.getOrDefault("workerNo", 1).toString());
			List<Integer> modPriority = (List<Integer>) workingParms.getOrDefault("modPriority", new ArrayList<Integer>() {{}});
			
			List<TradeSchedule> idleScheduleList = new ArrayList<TradeSchedule>();	
			
			int modIndex = 0;
			while(CollectionUtils.isEmpty(idleScheduleList) && modIndex <= modPriority.size()) {
				if(modIndex == modPriority.size()) {
					modIndex = -1;
				}

				idleScheduleList = tradeScheduleService.peekIdleSchedules(DistributeTaskSpec.PEEK_IDLE_LIMIT, modPriority, modIndex, workerNo);

				modIndex ++;
				if(0 == modIndex) {
					break;
				}
			}
			
			if(CollectionUtils.isEmpty(idleScheduleList)) {
				return;
			}
				
		
			for(TradeSchedule idleSchedule : idleScheduleList) {
				try {

					int nthSlot = idleSchedule.nextReadySlot();
					if(0 == nthSlot) {
						continue;
					}
					
					GatewaySlot gatewaySlot = idleSchedule.extractSlotInfo(nthSlot);
					
					if(!tradeScheduleHandler.executionNextSlot(idleSchedule)) {
						continue;
					}
					
					if(! tradeScheduleHandler.meetExecutionPrecond(idleSchedule)) {
						continue;
					}
					
					boolean occupySuccess = tradeScheduleService.ProcessScheduleInSlot(idleSchedule.getId(), nthSlot);
					
					if(!occupySuccess) {
						continue;
					}
					
					gatewaySlot.setTransactionBeginTime(new Date());
					
					tradeScheduleHandler.distributeToWorker(idleSchedule, gatewaySlot, nthSlot);
										
					tradeScheduleService.updateTransactionTime(idleSchedule.getId(), nthSlot, gatewaySlot.getTransactionBeginTime(), gatewaySlot.getCommunicationEndTime(), DistributeTaskSpec.UPDATE_TRY_TIMES);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
