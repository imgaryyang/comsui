package com.zufangbao.earth.yunxin.handler.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.zufangbao.sun.ledgerbookv2.handler.LedgerBookV2Handler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo2do.core.entity.Result;
import com.demo2do.core.web.resolver.Page;
import com.suidifu.matryoshka.customize.DelayTaskServices;
import com.suidifu.matryoshka.delayPosition.handler.DelayProcessingTaskCacheHandler;
import com.suidifu.matryoshka.delayPosition.handler.DelayTaskConfigCacheHandler;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.zufangbao.earth.yunxin.handler.RepurchaseHandler;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationCode;
import com.zufangbao.sun.entity.repurchase.FileRepurchaseDetail;
import com.zufangbao.sun.entity.repurchase.RepurchaseAmountDetail;
import com.zufangbao.sun.entity.repurchase.RepurchaseAmountEnvVar;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.entity.repurchase.RepurchaseRule;
import com.zufangbao.sun.entity.repurchase.RepurchaseStatus;
import com.zufangbao.sun.ledgerbook.LedgerBookHandler;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.PrepaymentApplicationService;
import com.zufangbao.sun.service.RepurchaseService;
import com.zufangbao.sun.utils.AmountUtils;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.GeneratorUtils;
import com.zufangbao.sun.yunxin.ContractState;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.AuditOverdueStatus;
import com.zufangbao.sun.yunxin.entity.OnAccountStatus;
import com.zufangbao.sun.yunxin.entity.api.PrepaymentApplication;
import com.zufangbao.sun.yunxin.entity.excel.RepurchaseDetailExcelVO;
import com.zufangbao.sun.yunxin.entity.model.repurchase.RepurchaseQueryModel;
import com.zufangbao.sun.yunxin.entity.model.repurchase.RepurchaseShowModel;
import com.zufangbao.sun.yunxin.handler.ContractHandler;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.RecordLogCore;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;
import com.zufangbao.wellsfargo.yunxin.handler.PrepaymentHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Component("repurchaseHandler")
public class RepurchaseHandlerImpl implements RepurchaseHandler {

    @Autowired
    private FinancialContractService financialContractService;

    @Autowired
    private RepurchaseService repurchaseService;

    @Autowired
    private RepaymentPlanService repaymentPlanService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private ContractHandler contractHandler;

    @Autowired
    private RecordLogCore recordLogCore;

    @Autowired
    private SystemOperateLogService systemOperateLogService;

    @Autowired
    private LedgerBookHandler ledgerBookHandler;

    @Autowired
    private PrepaymentApplicationService prepaymentApplicationService;

    @Autowired
    private PrepaymentHandler prepaymentHandler;

    @Autowired
    private DelayProcessingTaskCacheHandler delayProcessingTaskCacheHandler;

    @Autowired
    private SandboxDataSetHandler sandboxDataSetHandler;

    @Autowired
    private DelayTaskConfigCacheHandler delayTaskConfigCacheHandler;

    @Autowired
    private FinancialContractConfigurationService financialContractConfigurationService;

    @Autowired
    private LedgerBookStatHandler ledgerBookStatHandler;

    @Autowired
    private RepaymentPlanHandler repaymentPlanHandler;
    @Autowired
    private LedgerBookV2Handler ledgerBookV2Handler;

    private static Log log = LogFactory.getLog(RepurchaseHandlerImpl.class);
    // 系统自动生成回购单
    @Override
    public RepurchaseDoc conductRepurchaseViaAutomatic(Long contractId) {
        Contract contract = contractService.getContract(contractId);
        if (contract == null) {
            return null;
        }
        FinancialContract fc = financialContractService.getFinancialContractBy(contract.getFinancialContractUuid());
        if (fc == null) {
            return null;
        }
        RepurchaseDoc repurchaseDoc = conductRepurchase(RepurchaseDoc.REPURCHASE_BY_SYSTEM, contract, fc, new RepurchaseAmountDetail(), GeneratorUtils
                .generateAutoBatchNo());

        genDelayTask(fc, contract);

        return repurchaseDoc;
    }

    // 手工触发生成回购单
    @Override
    public RepurchaseDoc conductRepurchaseViaManual(Long contractId, RepurchaseAmountDetail repurchaseAmountDetail, String batchNo) {
        Contract contract = contractService.getContract(contractId);
        if (contract == null) {
            return null;
        }
        FinancialContract fc = financialContractService.getFinancialContractBy(contract.getFinancialContractUuid());
        if (fc == null) {
            return null;
        }
        RepurchaseDoc repurchaseDoc = conductRepurchase(RepurchaseDoc.REPURCHASE_BY_MANUAL, contract, fc, repurchaseAmountDetail,batchNo);

        genDelayTask(fc, contract);
        return repurchaseDoc;
    }

    private void genDelayTask(FinancialContract fc, Contract contract){
        String taskConfigUuid = financialContractConfigurationService.getFinancialContractConfigContentContent(fc.getFinancialContractUuid(),
            FinancialContractConfigurationCode.REPURCHASE_DELAY_TASK_UUID.toString());
        if (StringUtils.isBlank(taskConfigUuid)){
            return;
        }
        Map<String, Object> inputMap = new HashMap<String, Object>();
        inputMap.put("uniqueId", contract.getUniqueId());
        inputMap.put("taskConfigUuid", taskConfigUuid);
        DelayTaskServices delayTaskServices = (DelayTaskServices)delayTaskConfigCacheHandler.getCompiledObjectDelayTaskConfigUuid(taskConfigUuid);
        if(delayTaskServices == null ) return;
        delayTaskServices.evaluate(new Result(),delayProcessingTaskCacheHandler,sandboxDataSetHandler,inputMap,new HashMap<>(), log);
    }

    // 接口生成回购单
    @Override
    public RepurchaseDoc conductRepurchaseViaInterface(Contract contract, FinancialContract fc, RepurchaseAmountDetail repurchaseAmountDetail, String batchNo) {
        if (contract == null || fc == null) {
            return null;
        }
        return conductRepurchase(RepurchaseDoc.REPURCHASE_BY_MANUAL, contract, fc, repurchaseAmountDetail,batchNo);
    }

    // 执行回购
    private RepurchaseDoc conductRepurchase(int activateType, Contract contract, FinancialContract fc, RepurchaseAmountDetail amountDetail,String batchNo) {

        // 设置贷款合同和还款计划的状态
        updateAssetsetAndContractWithRepurchasingStatus(contract);
        RepurchaseDoc repurchaseDoc = generateRepurchaseDoc(activateType, amountDetail, contract, fc, batchNo);
        //作废资产  生成回购单
        ledgerBookV2Handler.create_repurchase_order(contract.getId(), repurchaseDoc.getRepurchaseDocUuid());
        ledgerBookHandler.create_repurchase_order(contract.getId(), repurchaseDoc.getRepurchaseDocUuid());
        return repurchaseDoc;
    }

    // 生成回购单
    private RepurchaseDoc generateRepurchaseDoc(int activateType, RepurchaseAmountDetail amountDetail, Contract contract, FinancialContract fc, String batchNo) {

        Date repoStartDate = new Date();
        List<AssetSet> assetSets = repaymentPlanService.getAllRepursingAssetSetsUnderContract(contract.getId());
        if (activateType == RepurchaseDoc.REPURCHASE_BY_SYSTEM) {
            repoStartDate = calculate_repo_start_date(fc, assetSets);
            if (repoStartDate == null) {
                throw new RuntimeException("系统错误：不存在可触发回购的还款计划");
            }
            contractHandler.cal_repurchase_detail_algorithm(amountDetail, fc);
        }
        contractHandler.calculateRepurchaseAmountDetail(assetSets, fc, contract, amountDetail);
        List<String> assetSetUuids = CollectionUtils.isEmpty(assetSets) ? Collections.emptyList() :
                assetSets.stream().map(AssetSet::getAssetUuid).collect(Collectors.toList());
        int merchant_payment_grace_period = fc.getAdvaMatuterm();// 宽限日
        RepurchaseDoc repurchaseDoc = new RepurchaseDoc(contract, fc, amountDetail, repoStartDate, merchant_payment_grace_period, assetSetUuids, activateType,batchNo);
        repurchaseService.save(repurchaseDoc);// 存表
        return repurchaseDoc;
    }

    // 系统触发回购时，需要设置的回购起始日
    private Date calculate_repo_start_date(FinancialContract fc, List<AssetSet> assetSets) {
        if (CollectionUtils.isEmpty(assetSets)) {
            return null;
        }
        Date curTime = new Date();
        if (fc.getRepurchaseRule() == RepurchaseRule.IRREGULARLY) {
            return curTime;
        }
        if (fc.getRepurchaseRule() == RepurchaseRule.BAD_DEBTS) {
            return assetSets.stream()
                    .filter(a -> a.getOverdueStatus() == AuditOverdueStatus.OVERDUE)
                    .map(b -> DateUtils.addDays(b.getAssetRecycleDate(), fc.getAdvaRepoTerm()))
                    .sorted()
                    .filter(c -> c.before(curTime))
                    .findFirst().orElse(null);
        }
        return curTime;
    }

    @Override
    public void updateAssetsetAndContractWithRepurchasingStatus(Contract contract) {
        if (contract == null) return;

        // 作废未还款提前还款
        AssetSet prepaymentPlan = repaymentPlanService.getPrepaymentPlan(contract.getId());
        if (prepaymentPlan != null && prepaymentPlan.getOnAccountStatus() != OnAccountStatus.PART_WRITE_OFF
                && prepaymentPlan.getOnAccountStatus() != OnAccountStatus.WRITE_OFF) {// 提前还款未扣款
            PrepaymentApplication application = prepaymentApplicationService.getUniquePrepaymentApplicationByRepaymentPlanUuid(prepaymentPlan.getAssetUuid());
            String errMsg = prepaymentHandler.invalid_prepayment_and_undo_frozen_be_pred_repayment_plan(application, prepaymentPlan);
            if (StringUtils.isNotBlank(errMsg)) {
                throw new RuntimeException("作废提前还款单失败[id=" + prepaymentPlan.getId() + "]");
            }
        }

        Integer current_version_no = contract.getActiveVersionNo();
        // 设置还款计划为回购中
        List<String> assetSetUuids = repaymentPlanService.getCanBeRepurchasedAssetSetUuids(contract.getId());
        repaymentPlanService.lockAssetSetWithRepurchasingStatus(contract);
        List<String> assetSetUuidsInRepursing = repaymentPlanService.getAllRepursingAssetSetUuidsUnderContract(contract);
        if (!validate_repursing_all_assetset(assetSetUuids, assetSetUuidsInRepursing)) {
            throw new RuntimeException("设置还款计划为回购中的操作失败");
        }

        // 设置贷款合同为回购中
        contractService.updateContractWithRepurchasingStatus(contract.getUuid());
        Integer latest_version_no = contractService.getActiveVersionNo(contract.getUuid());
        if (occur_concurrent_modification(current_version_no, latest_version_no)) {
            throw new RuntimeException("设置贷款合同为回购中的操作失败"); // TODO 确认回滚？？？？？
        }

    }

    private boolean occur_concurrent_modification(Integer current_version_no, Integer latest_version_no) {
        return !current_version_no.equals(latest_version_no);
    }

    private boolean validate_repursing_all_assetset(List<String> assetSetUuids,
                                                    List<String> assetSetUuidsInRepursing) {
        return !CollectionUtils.isEmpty(assetSetUuids)
                && !CollectionUtils.isEmpty(assetSetUuidsInRepursing)
                && (assetSetUuids.size() == assetSetUuidsInRepursing.size())
                && assetSetUuids.containsAll(assetSetUuidsInRepursing);
    }

    // 去掉贷款合同和还款计划的回购中状态
    @Override
    public void updateAssetsetAndContractRemoveRepurchasingStatus(Contract contract) {
        Integer preVersionNo = contract.getActiveVersionNo();

        List<String> assetSetUuids = repaymentPlanService.getAllRepursingAssetSetUuidsUnderContract(contract);
        repaymentPlanService.unlockAssetSetRemoveRepurchasingStatus(contract);
        List<String> assetSetUuidsNotInRepursing = repaymentPlanService.getCanBeRepurchasedAssetSetUuids(contract.getId());
        if (!validate_repursing_all_assetset(assetSetUuids, assetSetUuidsNotInRepursing)) {
            throw new RuntimeException("取消还款计划为回购中的操作失败");
        }

        contractService.updateContractRemoveRepurchasingStatus(contract.getUuid());
        Integer nowVersionNo = contractService.getActiveVersionNo(contract.getUuid());
        if (occur_concurrent_modification(preVersionNo, nowVersionNo)) {
            throw new RuntimeException("取消贷款合同为回购中的操作失败");
        }

    }

    // 设置贷款合同和还款计划的状态为违约
    @Override
    public void updateAssetsetAndContractWithDefaultStatus(Contract contract) {
        Integer preVersionNo = contract.getActiveVersionNo();

        List<String> assetSetUuids = repaymentPlanService.getAllRepursingAssetSetUuidsUnderContract(contract);
        repaymentPlanService.lockAssetSetWithDefaultStatus(contract);
        List<String> assetSetUuidsInDefault = repaymentPlanService.getAllDefaultAssetSetUuidsUnderContract(contract);
        if (!validate_repursing_all_assetset(assetSetUuids, assetSetUuidsInDefault)) {
            throw new RuntimeException("设置还款计划状态为违约的操作失败");
        }

        contractService.updateContractWithDefaultStatus(contract.getUuid());
        Integer nowVersionNo = contractService.getActiveVersionNo(contract.getUuid());
        if (occur_concurrent_modification(preVersionNo, nowVersionNo)) {
            throw new RuntimeException("设置贷款合同为违约的操作失败");
        }
    }

    @Override
    public List<RepurchaseShowModel> query(RepurchaseQueryModel queryModel, Page page) {
        List<RepurchaseDoc> repurchaseDoc = repurchaseService.query(queryModel, page);
        if (CollectionUtils.isEmpty(repurchaseDoc)) {
            return Collections.emptyList();
        }
        return repurchaseDoc.stream().map(a -> {
            RepurchaseShowModel x = new RepurchaseShowModel(a);
            Contract contract = contractService.getContract(a.getContractId());
            if (contract != null) {
                x.setUniqueId(contract.getUniqueId());
            }
            return x;
        }).collect(Collectors.toList());
    }

    @Override
    public List<RepurchaseDetailExcelVO> previewRepurchaseDetail(RepurchaseQueryModel queryModel, Page page) {
    	List<RepurchaseDoc> repurchaseDoc = repurchaseService.query(queryModel, page);
    	if (CollectionUtils.isEmpty(repurchaseDoc)) {
    		return Collections.emptyList();
    	}
    	return repurchaseDoc.stream().map(a -> {
    		Contract contract = contractService.getContract(a.getContractId());
    		return new RepurchaseDetailExcelVO(a, contract);
    	}).collect(Collectors.toList());
    }

    @Override
    public String repurchaseDefault(String repurchaseDocUuid, Long principalId, String ipAddr) throws Exception {
        RepurchaseDoc repurchaseDoc = repurchaseService.getRepurchaseDocByUuid(repurchaseDocUuid);
        if (repurchaseDoc == null) {
            return "参数错误";
        }
        if (repurchaseDoc.getRepurchaseStatus() == RepurchaseStatus.INVALID) {
            return "该回购单已作废，不能进行违约操作";
        }
        Contract contract = contractService.getContract(repurchaseDoc.getContractId());
        if (contract == null) {
            return "该回购单对应的贷款合同不存在";
        }
        updateAssetsetAndContractWithDefaultStatus(contract);
        repurchaseDoc.setRepurchaseStatus(RepurchaseStatus.DEFAULT);
        repurchaseService.update(repurchaseDoc);
        SystemOperateLog log = recordLogCore.insertNormalRecordLog(principalId, ipAddr, LogFunctionType.REPURCHASEDOCNULLIFY,
                LogOperateType.INVALIDATE, repurchaseDoc);
        log.setKeyContent(repurchaseDoc.getUuid());
        log.setRecordContent("回购单[" + repurchaseDoc.getUuid() + "]违约");
        systemOperateLogService.saveOrUpdate(log);
        return "";
    }

    @Override
    public String activateRepurchaseDoc(String repurchaseDocUuid, Long principalId, String ipAddr) throws Exception {
        RepurchaseDoc repurchaseDoc = repurchaseService.getRepurchaseDocByUuid(repurchaseDocUuid);
        if (repurchaseDoc == null) {
            return "请求参数错误";
        }
        if (repurchaseDoc.getRepurchaseStatus() != RepurchaseStatus.INVALID) {
            return "该回购单不在作废状态，不能激活";
        }
        Contract contract = contractService.getContract(repurchaseDoc.getContractId());
        if (contract == null) {
            return "该回购单对应的贷款合同不存在";
        }
        updateAssetsetAndContractWithRepurchasingStatus(contract);
        repurchaseDoc.setRepurchaseStatus(RepurchaseStatus.REPURCHASING);
        repurchaseService.update(repurchaseDoc);
        //ledger_book
        ledgerBookV2Handler.create_repurchase_order(contract.getId(), repurchaseDocUuid);
        ledgerBookHandler.create_repurchase_order(contract.getId(), repurchaseDocUuid);
        SystemOperateLog log = recordLogCore.insertNormalRecordLog(principalId, ipAddr, LogFunctionType.REPURCHASEDOCACTIVE,
                LogOperateType.ACTIVE, repurchaseDoc);
        log.setKeyContent(repurchaseDoc.getUuid());
        log.setRecordContent("激活回购单【" + repurchaseDoc.getUuid() + "】");
        systemOperateLogService.saveOrUpdate(log);
        return null;
    }

    @Override
    public void nullifyRepurchaseDoc(RepurchaseDoc repurchaseDoc, Contract contract, FinancialContract financialContract, Long principalId, String ipAddr) throws Exception {
        if(repurchaseDoc == null || contract == null || financialContract == null ){
        	throw new RuntimeException("参数错误");
        }
    	updateAssetsetAndContractRemoveRepurchasingStatus(contract);
        if (ledgerBookV2Handler.checkLedgerbookVersionByledgerBookNo
                (financialContract.getLedgerBookNo())){
            log.info("begin to #RepurchaseHandlerImpl#nullifyRepurchaseDoc " +
                    "[AccountTemplate] RepurchaseDocUuid:["+repurchaseDoc
                    .getUuid()+"]");
            ledgerBookV2Handler.roll_back_repurchase_and_related_assets(financialContract.getLedgerBookNo(), contract, repurchaseDoc.getRepurchaseDocUuid());
            log.info("end !!!");
        }
        //ledgerBook
        if (ledgerBookV2Handler.checkLedgerbookVersionByledgerBookNoV1(financialContract.getLedgerBookNo())){
            ledgerBookHandler.roll_back_repurchase_and_related_assets(financialContract.getLedgerBookNo(), contract, repurchaseDoc.getRepurchaseDocUuid());
        }
        repurchaseDoc.nullify();
        repurchaseService.update(repurchaseDoc);
        SystemOperateLog log = recordLogCore.insertNormalRecordLog(principalId, ipAddr, LogFunctionType.REPURCHASEDOCNULLIFY,
                LogOperateType.INVALIDATE, repurchaseDoc);
        log.setKeyContent(repurchaseDoc.getUuid());
        log.setRecordContent("作废回购单【" + repurchaseDoc.getUuid() + "】");
        systemOperateLogService.saveOrUpdate(log);
    }

    @Override
    public boolean canNullifyRepurchaseDoc(String repurchaseUuid){
		RepurchaseDoc repurchaseDoc = repurchaseService.getRepurchaseDocByUuid(repurchaseUuid);
		if(null==repurchaseDoc)
        	throw new RuntimeException("参数错误");
		if(!RepurchaseStatus.REPURCHASING.equals(repurchaseDoc.getRepurchaseStatus()))
			return false;
		BigDecimal repurchaseDetailTotal= AmountUtils.addAll(repurchaseDoc.getRepurchasePrincipal(),repurchaseDoc.getRepurchaseInterest()
				,repurchaseDoc.getRepurchasePenalty(),repurchaseDoc.getRepurchaseOtherCharges());
		FinancialContract financialContract=financialContractService.getFinancialContractBy(repurchaseDoc.getFinancialContractUuid());
		Contract contract=contractService.getContract(repurchaseDoc.getContractId());
		if(null==financialContract||null==contract)
			throw new RuntimeException("参数错误");
		BigDecimal totalInDb=ledgerBookStatHandler.get_repurchase_amount(financialContract.getLedgerBookNo(), contract.getUuid());
        return AmountUtils.equals(repurchaseDetailTotal, totalInDb);

    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<FileRepurchaseDetail> batchRepurchase(FinancialContract fc, List<FileRepurchaseDetail> details, String batchNo) {
		if (fc == null || CollectionUtils.isEmpty(details)) {
			return new ArrayList<>();
		}
		List<FileRepurchaseDetail> result = new ArrayList<>();
		for (FileRepurchaseDetail detail : details) {
			String checkMsg = detail.getSubmitCheckFailedMsgDetail();
			if (StringUtils.isNotEmpty(checkMsg)) {
				detail.setCheckFailedMsg(checkMsg);
				result.add(detail);
				continue;
			}
			Contract contract = contractService.getContractBy(detail.getUniqueId(), detail.getContractNo());
			if (contract == null) {
				detail.setCheckFailedMsg("贷款合同不存在");
				result.add(detail);
				continue;
			}
			if(!fc.getFinancialContractUuid().equals(contract.getFinancialContractUuid())){
				detail.setCheckFailedMsg("信托计划与贷款合同不匹配");
				result.add(detail);
				continue;
			}
			if(contract.getState() != ContractState.EFFECTIVE){
				if (contract.getState() == null || Arrays.asList(ContractState.PRE_PROCESS, ContractState.INEFFECTIVE, ContractState.INVALIDATE).contains(contract.getState())) {
					detail.setCheckFailedMsg("该贷款合同不允许回购申请");
				}else {
					detail.setCheckFailedMsg("该贷款合同已回购，请勿重复操作");
				}
				result.add(detail);
				continue;
			}
			List<String> assetSetUuids = repaymentPlanService.getExecutingAssetSetUuids(contract.getId());
			if (CollectionUtils.isNotEmpty(assetSetUuids)) {
				detail.setCheckFailedMsg("该贷款合同下有正在执行的还款计划");
				result.add(detail);
				continue;
			}
			List<AssetSet> assetSets = contractHandler.getNeedRepurchasedAssetSetList(contract.getId());
			if(CollectionUtils.isEmpty(assetSets)){
				detail.setCheckFailedMsg("该贷款合同下没有可回购的还款计划");
				result.add(detail);
				continue;
			}
			boolean existRepaymentPlanInPayment = repaymentPlanHandler.existRepaymentPlanInPayment(assetSets);
			if(existRepaymentPlanInPayment){
				detail.setCheckFailedMsg("存在支付中的还款计划");
				result.add(detail);
				continue;
			}
			RepurchaseAmountEnvVar envVar = contractHandler.parseRepurchaseEnvironmentVariables(assetSets);
			if(!AmountUtils.equals(envVar.getOutstandingPrincipal(), detail.getPrincipal())){
				detail.setCheckFailedMsg("回购本金错误");
				result.add(detail);
				continue;
			}
			RepurchaseAmountDetail repurchaseAmountDetail = new RepurchaseAmountDetail(detail);
			RepurchaseDoc repurchaseDoc = conductRepurchaseViaManual(contract.getId(),repurchaseAmountDetail,batchNo);
			if(repurchaseDoc == null){
				detail.setCheckFailedMsg("回购操作失败");
				result.add(detail);
				continue;
			}
		}
		return result;
	}
}



