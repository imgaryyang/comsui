package com.zufangbao.earth.yunxin.api.handler.impl;


import com.demo2do.core.BusinessException;
import com.zufangbao.earth.exception.BusinessSystemException;
import com.zufangbao.earth.util.FileUtil;
import com.zufangbao.earth.yunxin.api.handler.ThirdPartVoucherCommandHandler;
import com.zufangbao.gluon.api.swissre.institutionrecon.request.model.CommonCounterCashFlow;
import com.zufangbao.gluon.api.swissre.institutionrecon.request.model.CommonLocalTransactionCommand;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.sun.api.model.deduct.IsTotal;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.ThirdPartVoucherCommandLogService;
import com.zufangbao.sun.service.ThirdPartyVoucherBatchService;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.uuid.UUIDUtil;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationDetailService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplicationDetail;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import com.zufangbao.sun.yunxin.entity.model.api.ExtraChargeSpec;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.*;
import com.zufangbao.sun.yunxin.entity.thirdPartVoucher.ThirdPartyPayExecStatus;
import com.zufangbao.sun.yunxin.entity.thirdPartVoucher.ThirdPartyPayVoucherBatch;
import com.zufangbao.sun.yunxin.entity.thirdPartVoucher.ThirdPartyVoucherCommandLog;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.FINANCIAL_CONTRACT_NOT_EXIST;
import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.ONE_BATCH_REPEATE_VOUCHER_NO_OR_TRANSCATION_REQUEST_NO;

@Component(value="ThirdPartVoucherCommandHandler")
public class ThirdPartVoucherCommandHandlerImpl implements ThirdPartVoucherCommandHandler {
	
	private static final Log logger = LogFactory.getLog(ThirdPartVoucherCommandHandlerImpl.class);
	
	public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private FinancialContractService financialContractService;
	
	@Autowired
	private ThirdPartVoucherCommandLogService thirdPartVoucherCommandLogService;
	
	@Autowired
	private DeductApplicationService deductApplicationService;
	
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	
	@Autowired
	private DeductApplicationDetailService deductApplicationDetailService;
	
	@Autowired
	private DeductPlanService deductPlanService;
	
	@Autowired
	private ThirdPartyVoucherBatchService thirdPartVoucherBatchService;

	@Override
	public void commandRequestLogicValidate(ThirdPartVoucherModel model) {
		
		//financial_contract_no需要有效
		FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(model.getFinancialContractNo());
		if(financialContract == null){
			throw new ApiException(FINANCIAL_CONTRACT_NOT_EXIST);
		}
		List<ThirdPartVoucherDetailModel> detailModelList = model.getDetailListParseJson();
		
		checkRepeateOneBatchTranscationRequestNoAndVoucherNo(detailModelList);
		
		
	}


	private void checkRepeateOneBatchTranscationRequestNoAndVoucherNo(
			List<ThirdPartVoucherDetailModel> detailModelList) {
		int distinctTranscationRequestNoSize = detailModelList.stream().map(detail -> detail.getTransactionRequestNo()).distinct().collect(Collectors.toList()).size();
		int distinctVoucherNoSize = detailModelList.stream().map(detail -> detail.getVoucherNo()).distinct().collect(Collectors.toList()).size();
		if(detailModelList.size() !=distinctTranscationRequestNoSize || detailModelList.size() !=distinctVoucherNoSize )
		{
			throw new ApiException(ONE_BATCH_REPEATE_VOUCHER_NO_OR_TRANSCATION_REQUEST_NO);
		}
	}
	
	@Override
	public Map<String, Object> generateThirdPartVoucherCommandLog(ThirdPartVoucherModel model, String ipAddress) {
		
		FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(
				model.getFinancialContractNo());
		List<ThirdPartVoucherDetailModel> detailModelList = model.getDetailListParseJson();
		String batchUuid = model.getBatchUuid();
		createThirdPartVoucherPayBatch(model, financialContract);
		
		Map<String, Object> errmap = new HashMap<String, Object>();
		for(ThirdPartVoucherDetailModel detailModel:detailModelList ){
			ThirdPartyVoucherCommandLog log =
					thirdPartVoucherCommandLogService.getThirdPartyVoucherLogByTradeuuid(
							detailModel.getTransactionRequestNo());
			//查不到则为第一次插入
			if (null == log) {
				logger.info("插入ThirdPartyvoucherDetai"+log);
				log = new ThirdPartyVoucherCommandLog(detailModel,model,ipAddress,financialContract.getFinancialContractUuid(), financialContract.getContractNo(), batchUuid, UUIDUtil.randomUUID());
				try {
					thirdPartVoucherCommandLogService.save(log);
				}catch (Exception e){
					logger.error("保存commandlog错误");
					throw new BusinessSystemException("保存commandlog错误"+e.getMessage());
				}

			}else {
				//如果没有验证成功
				if (!VoucherLogStatus.SUCCESS.equals(log.getVoucherLogStatus())) {
					saveHistoryLogAndUpdateLog(log,detailModel.getTransactionRequestNo());
					//更新第三方凭证中的明细及版本号
					log.updateRepay(detailModel, model,ipAddress,financialContract,batchUuid,UUIDUtil.randomUUID());
					try {
						thirdPartVoucherCommandLogService.update(log);
					}catch (Exception e){
						logger.error("更新log错误");
						throw new BusinessSystemException("更新log错误" + e.getMessage());
					}

				}
				else {
					//验证成功
					errmap.put(detailModel.getTransactionRequestNo(), "该凭证已经校验成功");
				}
			}
		}
		return errmap;
	}
	
	
	


	


	private void createThirdPartVoucherPayBatch(ThirdPartVoucherModel model, FinancialContract financialContract) {
		ThirdPartyPayVoucherBatch thirdPartVoucherBatch = new ThirdPartyPayVoucherBatch(model,financialContract);
		thirdPartVoucherBatchService.save(thirdPartVoucherBatch);
	}
	
	
	public void reverseCastDeductInfoAndWriteOff(String logJsonContent,String cashFlowJsonContent){
		
		
		CommonCounterCashFlow  cashFlowInfo= JsonUtils.parse(cashFlowJsonContent, CommonCounterCashFlow.class);
		CommonLocalTransactionCommand transcationVoucherInfo = JsonUtils.parse(cashFlowJsonContent, CommonLocalTransactionCommand.class);
		
		createDeductInfo(cashFlowInfo,transcationVoucherInfo);
	}
	
	
	
	private void createDeductInfo(CommonCounterCashFlow cashFlowInfo,CommonLocalTransactionCommand transcationVoucherInfo) {
		
		DeductApplication dedcutApplication  = createAndSaveDeductApplication(transcationVoucherInfo,cashFlowInfo);
		createDeductApplicationDetailInfo(transcationVoucherInfo,cashFlowInfo,dedcutApplication);
		
		createAndSaveDeductPlan(transcationVoucherInfo,cashFlowInfo,dedcutApplication);
		
	}
	private DeductPlan createAndSaveDeductPlan(CommonLocalTransactionCommand transcationVoucherInfo,CommonCounterCashFlow cashFlowInfo, DeductApplication dedcutApplication) {
		
		DeductPlan deductPlan = new DeductPlan();
		deductPlanService.save(deductPlan);
		return deductPlan;
		
	}
	private void createDeductApplicationDetailInfo(CommonLocalTransactionCommand transcationVoucherInfo,
			CommonCounterCashFlow cashFlowInfo, DeductApplication deductApplication) {
		List<CommonAuditResultRepaymentDetail> repaymentDetailInfos =  (List<CommonAuditResultRepaymentDetail>) JsonUtils.parse(transcationVoucherInfo.getRepayDetailList(), CommonAuditResultRepaymentDetail.class);
		for(CommonAuditResultRepaymentDetail repaymentDetailInfo:repaymentDetailInfos){
			createSingleDeductApplicationDetail(deductApplication, repaymentDetailInfo,ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_PRINCIPLE, IsTotal.DETAIL,repaymentDetailInfo.getPrincipal());
			createSingleDeductApplicationDetail(deductApplication, repaymentDetailInfo,ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_INTEREST, IsTotal.DETAIL,repaymentDetailInfo.getInterest());
			createSingleDeductApplicationDetail(deductApplication, repaymentDetailInfo,ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_TECH_FEE, IsTotal.DETAIL, repaymentDetailInfo.getMaintenanceCharge());
			createSingleDeductApplicationDetail(deductApplication, repaymentDetailInfo,ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_LOAN_SERVICE_FEE, IsTotal.DETAIL,repaymentDetailInfo.getServiceCharge());
			createSingleDeductApplicationDetail(deductApplication, repaymentDetailInfo,ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_OTHER_FEE, IsTotal.DETAIL, repaymentDetailInfo.getOtherCharge());
			
			createSingleDeductApplicationDetail(deductApplication, repaymentDetailInfo,ChartOfAccount.SND_RECIEVABLE_LOAN_PENALTY, IsTotal.DETAIL,repaymentDetailInfo.getPenaltyFee());
			createSingleDeductApplicationDetail(deductApplication, repaymentDetailInfo,ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION, IsTotal.DETAIL,repaymentDetailInfo.getLatePenalty());
			createSingleDeductApplicationDetail(deductApplication, repaymentDetailInfo,ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE, IsTotal.DETAIL,repaymentDetailInfo.getLateFee());
			createSingleDeductApplicationDetail(deductApplication, repaymentDetailInfo,ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE, IsTotal.DETAIL,repaymentDetailInfo.getLateOtherCost());
			createSingleDeductApplicationDetailTotalOverDueFee(deductApplication, repaymentDetailInfo, IsTotal.DETAIL,repaymentDetailInfo.calcTotalOverDueFee());
			createSingleDeductApplicationDetailTotalReceivableAmount(deductApplication, repaymentDetailInfo,  IsTotal.TOTAL, repaymentDetailInfo.getAmount());
		}
		
	}
	private void createSingleDeductApplicationDetail(DeductApplication deductApplication,
			CommonAuditResultRepaymentDetail repaymentDetailInfo, String chartString, IsTotal isTotal, BigDecimal amount) {
		
		//金额小于等于零不生成明细记录
		if(amount.compareTo(BigDecimal.ZERO) < 1){
			return ;
		}
		AssetSet repaymentPlan = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentDetailInfo.getRepaymentPlanNo());
		DeductApplicationDetail deductApplicationDetail = new DeductApplicationDetail(deductApplication,repaymentDetailInfo, isTotal, amount, repaymentPlan.getAssetUuid());
		deductApplicationDetail.copyTAccount(ChartOfAccount.EntryBook().get(chartString));
		deductApplicationDetailService.save(deductApplicationDetail);
	}
	private void createSingleDeductApplicationDetailTotalReceivableAmount(DeductApplication deductApplication,
			CommonAuditResultRepaymentDetail repaymentDetail, IsTotal total, BigDecimal caclAccountReceivableAmount) {

		//金额小于等于零不生成明细记录
		if(caclAccountReceivableAmount.compareTo(BigDecimal.ZERO) < 1){
			return ;
		}
		AssetSet repaymentPlan = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentDetail.getRepaymentPlanNo());
		
		DeductApplicationDetail deductApplicationDetail = new DeductApplicationDetail(deductApplication,repaymentDetail, total, caclAccountReceivableAmount, repaymentPlan.getAssetUuid());
		deductApplicationDetail.setFirstAccountName(ExtraChargeSpec.TOTAL_RECEIVABEL_AMOUNT);
		deductApplicationDetailService.save(deductApplicationDetail);
	}

	private void createSingleDeductApplicationDetailTotalOverDueFee(DeductApplication deductApplication,CommonAuditResultRepaymentDetail repaymentDetail, IsTotal isTotal,BigDecimal totalOverdueFee) {
		//金额小于等于零不生成明细记录
		if(totalOverdueFee.compareTo(BigDecimal.ZERO) < 1){
			return ;
		}
		AssetSet repaymentPlan = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentDetail.getRepaymentPlanNo());
		DeductApplicationDetail deductApplicationDetail = new DeductApplicationDetail(deductApplication,repaymentDetail, isTotal, totalOverdueFee, repaymentPlan.getAssetUuid());
		deductApplicationDetail.setFirstAccountName(ExtraChargeSpec.TOTAL_OVERDUE_FEE);
		deductApplicationDetailService.save(deductApplicationDetail);
		
	}
	private DeductApplication createAndSaveDeductApplication(CommonLocalTransactionCommand transcationVoucherInfo,
			CommonCounterCashFlow cashFlowInfo) {
		
		DeductApplication deductApplication = new DeductApplication();
		deductApplicationService.save(deductApplication);
		return deductApplication;
	}

	@Value("#{config['thirdPartyCommandLogHistorySavePath']}")
	private String savePath = "";
	@Override
	public Map<String, Object> getThirdPartVoucherRepayDetailList(String tradeUuid) {
		//logger.info("---save path : " + savePath);
		File file = new File(savePath + File.separator+tradeUuid);
		File[] detail = file.listFiles();
		List<String> detailList = new ArrayList<String>();
		Map<String, Object> resMap = new HashMap<String, Object>(3);
		if (null != detail && detail.length > 0){
			for (int i = 0; i < detail.length; i++) {
				String fileName = detail[i].getName();
				detailList.add(fileName.substring(0, fileName.indexOf(".")));
			}

			resMap.put("versionNameList", detailList);
			String detailFirstName = detailList.get(0);
			try{
//				ThirdPartVoucherRepayDetailModel model = redThirdPartVoucherRepayDetail(tradeUuid,detailFirstName);
				ThirdPartyVoucherCommandLog log = redThirdPartVoucherRepayDetail(tradeUuid, detailFirstName);
				ThirdPartVoucherDetailModel repayModel =
						JsonUtils.parse(log.getThirdPartVoucherContent(), ThirdPartVoucherDetailModel.class);


				List<ThirdPartVoucherRepayDetailModel> repayDetailModelList = repayModel.getRepayDetailList();
				if (!CollectionUtils.isEmpty(repayDetailModelList)) {
					resMap.put("historyDetail", repayDetailModelList.get(0));
				} else {
					resMap.put("historyDetail", null);
				}

//			}catch (Exception e){
//				logger.error("读取还款明细错误");
//				throw new BusinessSystemException("读取还款明细错误"+e.getMessage());
//			}
//			try {
//				ThirdPartyVoucherCommandLog log = thirdPartVoucherCommandLogService.getThirdPartyVoucherLogByTradeuuid(tradeUuid);
				resMap.put("voucher", log);
			}catch (Exception e){
				logger.error("读取第三凭证错误");
				throw new BusinessSystemException("读取第三方凭证错误"+e.getMessage());
			}
			return resMap;
		}else {
			resMap.put("versionNameList", null);
			resMap.put("historyDetail", null);
			resMap.put("voucher", null);
			return resMap;
		}


	}


	@Override
	public ThirdPartyVoucherCommandLog redThirdPartVoucherRepayDetail(String tradeUuid , String versionName) {
		String filestart = savePath+File.separator+tradeUuid;
		String reddatail = null;
		String filePath = filestart + File.separator + versionName + ".txt";
		try {
			reddatail = FileUtil.readToString(filePath , "UTF-8");
		} catch (IOException | BusinessSystemException e) {
			logger.error("无法从配置路径中读取该版本的历史还款明细");
			throw new BusinessException("该版本历史还款信息读取错误："+e.getMessage());
		}
		ThirdPartyVoucherCommandLog detailHistory = JsonUtils.parse(reddatail, ThirdPartyVoucherCommandLog.class);
		return detailHistory;
	}

	@Override
	public Map<String, Object> getRepayDetailAndVoucher(String tradeUuid , String versionName) {
		Map<String , Object> result = new HashMap<String,Object>();
		ThirdPartyVoucherCommandLog log = redThirdPartVoucherRepayDetail(tradeUuid , versionName);
//		ThirdPartyVoucherCommandLog log =
//				thirdPartVoucherCommandLogService.getThirdPartyVoucherLogByTradeuuid(tradeUuid);
		ThirdPartVoucherDetailModel repayModel =
				JsonUtils.parse(log.getThirdPartVoucherContent(), ThirdPartVoucherDetailModel.class);


		List<ThirdPartVoucherRepayDetailModel> repayDetailModelList = repayModel.getRepayDetailList();
		if (!CollectionUtils.isEmpty(repayDetailModelList)) {
			result.put("historydetail", repayDetailModelList.get(0));
		} else {
			result.put("historydetail", null);
		}

		result.put("voucher",log);
		return result;
	}


	@Override
	public void saveHistoryLogAndUpdateLog(ThirdPartyVoucherCommandLog log , String tradeUuid) {
		//取得历史还款明细并保存格式为 /配置路径/tradeuuid/版本 + time 的文件
//		String logcontent = log.getThirdPartVoucherContent();
//		String detail = logcontent.substring(logcontent.indexOf("[")+1, logcontent.indexOf("]"));
		String detail = JsonUtils.toJSONString(log);
		String filename = "版本"+simpleDateFormat.format(new Date()) + ".txt";
		try {
			FileUtil.saveToFile(detail, savePath +File.separator+tradeUuid+File.separator+filename, "UTF-8");
		} catch (IOException | BusinessSystemException e) {
			logger.error("无法保存该历史还款明细至配置路径中");
			throw new BusinessException("无法保存该历史还款明细至配置路径中："+e.getMessage());
		}
	}

	@Override
	public Map<String, Object> reLoadThirdPartVoucherCommandLog(String traduuid) {
		Map<String, Object> errmap = new HashMap<String, Object>();
			ThirdPartyVoucherCommandLog log =
					thirdPartVoucherCommandLogService.getThirdPartyVoucherLogByTradeuuid(traduuid);
			if (null == log) {
				errmap.put(traduuid,"err:未上传过，无法重新核销！");
			}else {
				//如果没有核销成功
				if (!VoucherLogIssueStatus.HAS_ISSUED.equals(log.getVoucherLogIssueStatus())) {
					//重置第三方凭证中versionNo VoucherLogStatus RetiedTimes
					String versionno = UUIDUtil.randomUUID();
					log.setRetriedTimes(0);
					log.setVoucherLogStatus(null);
					log.setVersionNo(versionno);
					ThirdPartVoucherDetailModel model = JsonUtils.parse(log.getThirdPartVoucherContent(), ThirdPartVoucherDetailModel.class);
					model.setVersionNo(versionno);
					log.setThirdPartVoucherContent(JsonUtils.toJSONString(model));
					log.setVoucherLogIssueStatus(VoucherLogIssueStatus.NOT_ISSUED);
					log.setExecutionStatus(ThirdPartyPayExecStatus.SUCCESS);
					try {
						thirdPartVoucherCommandLogService.update(log);
					}catch (Exception e){
						logger.error("更新commandlog错误");
						throw new BusinessSystemException("更新commandlog错误" + e.getMessage());
					}
				}
				else {
					//已核销成功
					errmap.put(traduuid, "err:该凭证已核销成功");
				}
			}
		return errmap;
	}
}
