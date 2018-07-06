 package com.zufangbao.earth.yunxin.task;

 import com.zufangbao.earth.yunxin.handler.RepurchaseHandler;
 import com.zufangbao.sun.service.ContractService;
 import com.zufangbao.sun.utils.DateUtils;
 import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
 import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
 import org.apache.commons.collections.CollectionUtils;
 import org.apache.commons.logging.Log;
 import org.apache.commons.logging.LogFactory;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Component;

 import java.util.Date;
 import java.util.List;

/**
 * 回购Task
 * @author jx
 */
@Component("RepurchaseTask")
public class RepurchaseTask {
	
	@Autowired
	private RepaymentPlanHandler repaymentPlanHandler;
	
	@Autowired
	private RepaymentPlanService repaymentPlanService;

	@Autowired
	private ContractService contractService;
	
	@Autowired
	private RepurchaseHandler repurchaseHandler;
	
	private static Log logger = LogFactory.getLog(RepurchaseTask.class);

	/**
	 * 查询逾期、未还 的还款计划，再判断其贷款合同下，是否存在执行中的还款计划，不存在则触发贷款合同回购，生成回购单
	 */
	public void genRepurchaseDoc(){
		logger.info("##genRepurchaseDocTask## start!");
		long start = System.currentTimeMillis();
		String currentTime = DateUtils.format(new Date(), DateUtils.LONG_DATE_FORMAT);
		try{
			List<Long> contractIds = repaymentPlanHandler.getNeedRepurchaseContractIdList();// 需要进行回购的贷款合同
			logger.info(currentTime+"#可能需要回购的贷款合同总计（"+contractIds.size()+"）条,ids--" + contractIds);
			generateRepurchaseDoc(contractIds);
		}catch (RuntimeException re){
			logger.error("##genRepurchaseDocTask## occur error[RuntimeException].");
			re.printStackTrace();
		}catch (Exception e){
			logger.error("##genRepurchaseDocTask## occur error[Exception].");
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		logger.info("##genRepurchaseDocTask## end! Used ["+(end-start)+"]ms");
	}
	public void generateRepurchaseDoc(List<Long> contractIds){
		if(CollectionUtils.isEmpty(contractIds)){
			return ;
		} 
		for (Long contractId : contractIds) {
			List<String> executing_assetset_uuid_list = repaymentPlanService.getExecutingAssetSetUuids(contractId);
			if(can_be_generated_repurchase_doc(executing_assetset_uuid_list)){ // 没有正在执行或提前还款的还款计划
				logger.info("==repurchase the contract, id:"+contractId+"==");
				repurchaseHandler.conductRepurchaseViaAutomatic(contractId);
			}
		}
	}

	private boolean can_be_generated_repurchase_doc(List<String> executing_assetset_uuid_list) {
		return CollectionUtils.isEmpty(executing_assetset_uuid_list);
	}
	
}
