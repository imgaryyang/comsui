package com.suidifu.matryoshka.customize;

import com.demo2do.core.entity.Result;
import com.suidifu.matryoshka.delayPosition.handler.DelayProcessingTaskCacheHandler;
import com.suidifu.matryoshka.delayPosition.handler.DelayTaskConfigCacheHandler;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.repayment.order.RepaymentBusinessType;
import com.zufangbao.sun.service.CustomerService;
import com.zufangbao.sun.yunxin.entity.AssetRecoverType;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.RepaymentPlanExtraData;
import com.zufangbao.sun.yunxin.entity.model.RepaymentChargesDetail;
import com.zufangbao.sun.yunxin.service.RepaymentPlanExtraDataService;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//import com.suidifu.matryoshaka.test.scripts.ReconciliationDelayTaskServices;

@Component("reconciliationDelayTaskProcess")
public class ReconciliationDelayTaskProcessImpl implements ReconciliationDelayTaskProcess {
	@Autowired
	private RepaymentPlanExtraDataService repaymentPlanExtraDataService;
	@Autowired
	private DelayProcessingTaskCacheHandler delayProcessingTaskCacheHandler;

	@Autowired
	private CustomerService customerService;
	@Autowired
	private DelayTaskConfigCacheHandler delayTaskConfigCacheHandler;

	@Autowired
    private SandboxDataSetHandler sandboxDataSetHandler;
	private static final Log logger = LogFactory.getLog(ReconciliationDelayTaskProcessImpl.class);
//	@Override
//	public void processingDelayTask(Date tradeTime, Contract contract, RepaymentChargesDetail detailAmount, AssetSet assetSet, AssetRecoverType assetRecoverType){
//		if(assetRecoverType==null || !assetRecoverType.isLoanAssetOrRepurchaseAsset()){
//			return;
//		}
//
//		SandboxDataSet dataSet = new SandboxDataSet();
//
//		ContractSnapshot contractSnapshot = new ContractSnapshot(contract);
//		dataSet.setContractSnapshot(contractSnapshot);
//
//		HashMap<String,String> extraData = new HashMap<String,String>();
//
//		String identityCardNo = customerService.getCustomerAccount(contract.getCustomerUuid());
//		//TODO 组装
//		int repayType = getRepayType(assetSet.getAssetUuid(), assetRecoverType);
//		int currentPeriod = getCurrentPeriod(repayType, assetSet.getCurrentPeriod());
//		extraData.put("tradeNo", UUID.randomUUID().toString());
//		extraData.put("loanNo", contractSnapshot.getUniqueId());
//		extraData.put("thirdUserNo", identityCardNo);
//
//		dataSet.setExtraData(extraData);
//
//		Map<String, Object> inputMap = new HashMap<String, Object>();
//        inputMap.put("uniqueId", contract.getUniqueId());
//        inputMap.put("contractNo", contract.getContractNo());
//
//        extraData.put("installmentNo", currentPeriod+"");
//		extraData.put("paidPrincipal", detailAmount.getLoanAssetPrincipal()+"");
//		extraData.put("paidInterest", detailAmount.getLoanAssetInterest()+"");
//		extraData.put("paidRepayCharge", detailAmount.get_total_fee_except_principal_interest()+"");
//		extraData.put("repayType", repayType+"");
//		extraData.put("tradeTime", DateUtils.format(new Date(),com.zufangbao.sun.utils.DateUtils.LONG_DATE_FORMAT));
//
//		inputMap.put("workParams", JsonUtils.toJsonString(extraData));
//		DelayTaskServices reconciliationDelayTaskServices = (DelayTaskServices)delayTaskConfigCacheHandler.getCompiledObjectDelayTaskConfigUuid("");//new ReconciliationDelayTaskServices();
//		reconciliationDelayTaskServices.evaluate(new Result(), delayProcessingTaskCacheHandler, sandboxDataSetHandler,inputMap,new HashMap<String, Object>());
//	}

//	private int getRepayType(String assetSetUuid, AssetRecoverType assetRecoverType){
//		//1. 提前结清还款
//		//2. 退货还款
//		//3. 回购还款
//		//4. 正常还款
//		if(assetRecoverType.isRepurchaseAsset()){
//			return 3;
//		}
//		RepaymentPlanExtraData repaymentPlanextraData = repaymentPlanExtraDataService.get(assetSetUuid);
//		if(repaymentPlanextraData==null){
//
//		}
//		RepaymentPlanModifyReason modifyReason = repaymentPlanextraData.getReasonCode();
//		if(modifyReason==RepaymentPlanModifyReason.REASON_6
//				||modifyReason==RepaymentPlanModifyReason.REASON_8){
//			return 1;
//		}
//		if(modifyReason==RepaymentPlanModifyReason.REASON_9){
//			return 2;
//		}
//
//		return 4;
//	}
//
//	private int getCurrentPeriod(int repayType,int assetCurrentPeriod){
//		if(repayType==4){
//			return assetCurrentPeriod;
//		}
//		return 0;
//	}

	//佰仟一期
	@Override
	public void processingDelayTask(Date tradeTime, Contract contract, RepaymentChargesDetail detailAmount, AssetSet assetSet, String repurchaseUuid, AssetRecoverType assetRecoverType, String recover_delay_task_uuid){
		if(assetRecoverType==null || !assetRecoverType.isLoanAssetOrRepurchaseAsset()){
			logger.info("assetRecoverType is not loanRepuachase");
			return;
		}
		//TODO asset 可以为null
		if (StringUtils.isBlank(recover_delay_task_uuid)){
			logger.info("recover_delay_task_uuid is empty");
			return;
		}

		DelayTaskServices reconciliationDelayTaskServices = (DelayTaskServices)delayTaskConfigCacheHandler.getCompiledObjectDelayTaskConfigUuid(recover_delay_task_uuid);

		if(reconciliationDelayTaskServices == null ) {
			logger.info("reconciliationDelayTaskServices is null");
			return;
		}

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("configUuid",recover_delay_task_uuid);
        inputMap.put("uniqueId", contract.getUniqueId());
        inputMap.put("contractNo", contract.getContractNo());
        inputMap.put("repaymentPlanUuid", assetSet==null?null:assetSet.getAssetUuid());
        inputMap.put("repurchaseDocUuid", repurchaseUuid);
		inputMap.put("assetRecoverType",assetRecoverType);
		inputMap.put("tradeTime",tradeTime);
		inputMap.put("detailAmount",detailAmount);
		Integer currentPeriod = null;
		RepaymentPlanExtraData repaymentPlanExtraData = null;
		if(assetSet!=null){
			repaymentPlanExtraData= repaymentPlanExtraDataService.get(assetSet.getAssetUuid());
			currentPeriod = assetSet.getCurrentPeriod();
			inputMap.put("repaymentPlanModifyReason",repaymentPlanExtraData==null?null:repaymentPlanExtraData.getReasonCode());
			inputMap.put("assetSetCurrentPeriod",currentPeriod);
		}
		reconciliationDelayTaskServices.evaluate(new Result(), delayProcessingTaskCacheHandler, sandboxDataSetHandler,inputMap,new HashMap<String, Object>(), logger);
	}

	//佰仟二三期
	@Override
	public void processingRepaymentOrderCheckDelayTask(Date tradeTime, Contract contract,
		RepaymentChargesDetail detailAmount,
		AssetSet assetSet, int period, String repurchaseUuid, RepaymentBusinessType repaymentBusinessType,
		String configUuid) {
		if(repaymentBusinessType==null || !repaymentBusinessType.isLoanAssetOrRepurchaseAsset()){
			logger.info("repaymentBusinessType is not loanRepuachase");
			return;
		}
		//TODO asset 可以为null
		if (StringUtils.isBlank(configUuid) ){
			logger.info("delay_task_uuid is empty");
			return;
		}

		DelayTaskServices repaymentOrderDelayTaskService = (DelayTaskServices)delayTaskConfigCacheHandler.getCompiledObjectDelayTaskConfigUuid(
			configUuid);

		if(repaymentOrderDelayTaskService == null ) {
			logger.info("reconciliationDelayTaskServices is null");
			return;
		}

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("configUuid", configUuid);
		inputMap.put("uniqueId", contract.getUniqueId());
		inputMap.put("contractNo", contract.getContractNo());
		inputMap.put("repaymentPlanUuid", assetSet==null?null:assetSet.getAssetUuid());
		inputMap.put("repurchaseDocUuid", repurchaseUuid);
		inputMap.put("repaymentBusinessType", repaymentBusinessType);
		inputMap.put("tradeTime",tradeTime);
		inputMap.put("detailAmount",detailAmount);
		inputMap.put("assetSetCurrentPeriod",period);
		inputMap.put("assetRecycleDate",assetSet==null?null:assetSet.getAssetRecycleDate());

		repaymentOrderDelayTaskService.evaluate(new Result(), delayProcessingTaskCacheHandler, sandboxDataSetHandler,inputMap,new HashMap<String, Object>(), logger);

	}
}
