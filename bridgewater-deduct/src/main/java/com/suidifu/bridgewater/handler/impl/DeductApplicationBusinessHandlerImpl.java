package com.suidifu.bridgewater.handler.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.Calendar;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.suidifu.bridgewater.api.util.ApiSignUtils;
import com.suidifu.bridgewater.handler.DeductApplicationDetailHandler;
import com.suidifu.bridgewater.handler.IDeductAsyncNotifyHandler;
import com.zufangbao.sun.api.model.modify.AssetSetModifyModel;
import com.zufangbao.gluon.spec.earth.v3.ApiConstant;
import com.zufangbao.sun.entity.financial.*;
import com.zufangbao.sun.service.*;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductRequestLogService;
import com.zufangbao.sun.yunxin.entity.*;
import com.zufangbao.sun.yunxin.entity.Dictionary;
import com.zufangbao.sun.yunxin.entity.api.deduct.*;
import com.zufangbao.sun.yunxin.exception.DictionaryNotExsitException;
import com.zufangbao.sun.yunxin.service.DictionaryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.JDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.entity.Result;
import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.api.model.OverdueDeductResultQueryModel;
import com.suidifu.bridgewater.api.model.ReDeductDataPackage;
import com.suidifu.bridgewater.api.service.batch.v2.DeductPlanCacheService;
import com.suidifu.bridgewater.deduct.notify.handler.batch.v2.DeductNotifyJobServer;
import com.suidifu.bridgewater.handler.DeductApplicationBusinessHandler;
import com.suidifu.bridgewater.handler.DeductPlanBusinessHandler;
import com.suidifu.bridgewater.model.v2.NotifyModel;
import com.suidifu.coffer.entity.cmb.UnionPayBankCodeMap;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.suidifu.swift.notifyserver.notifyserver.PriorityEnum;
import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductRequestModel;
import com.zufangbao.gluon.api.jpmorgan.JpmorganApiHelper;
import com.zufangbao.gluon.api.jpmorgan.model.QueryStatusResult;
import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.logspec.GloableLogSpec.bridgewater_deduct_function_point;
import com.zufangbao.gluon.opensdk.HttpClientUtils;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.util.ApiMessageUtil;
import com.zufangbao.sun.api.model.deduct.DeductApplicationExecutionStatus;
import com.zufangbao.sun.api.model.deduct.DeductApplicationReceiveStatus;
import com.zufangbao.sun.api.model.deduct.DeductApplicationRepaymentDetail;
import com.zufangbao.sun.api.model.deduct.DeductCommandRequestModel;
import com.zufangbao.sun.api.model.deduct.IsTotal;
import com.zufangbao.sun.api.model.deduct.RepaymentDetail;
import com.zufangbao.sun.api.model.deduct.RepaymentType;
import com.zufangbao.sun.entity.bank.Bank;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.geography.entity.City;
import com.zufangbao.sun.geography.entity.Province;
import com.zufangbao.sun.geography.service.CityService;
import com.zufangbao.sun.geography.service.ProvinceService;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationDetailService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.deduct.DeductGatewayMapSpec;
import com.zufangbao.sun.yunxin.entity.model.api.ExtraChargeSpec;
import com.zufangbao.sun.yunxin.entity.remittance.CertificateType;
import com.zufangbao.sun.yunxin.entity.remittance.TransactionRecipient;
import com.zufangbao.sun.yunxin.handler.PaymentChannelInformationHandler;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import com.zufangbao.sun.yunxin.service.BankService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.UnionpayBankConfigService;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec;
import com.zufangbao.wellsfargo.yunxin.handler.ContractApiHandler;

/**
 * @author zhenghangbo
 *
 */
@Component("deductApplicationBusinessHandler")
public class DeductApplicationBusinessHandlerImpl implements DeductApplicationBusinessHandler {

	private static final int SUCCESS = 3;

	private static final double THE_CALIBRATION_VALUE = 0.01;

	@Autowired
	private DeductApplicationService deductApplicationService;

	@Autowired
	private ContractApiHandler contractApiHandler;

	@Autowired
	private FinancialContractService financialContractService;

	@Autowired
	private RepaymentPlanService repaymentPlanService;
	
	@Autowired
	private ContractAccountService contractAccountService;

	@Autowired
	private DeductPlanService deductPlanService;

	@Autowired
	private JpmorganApiHelper jpmorganApiHelper;

	@Autowired
	private GenericDaoSupport genericDaoSupport;

	@Autowired
	private DeductApplicationDetailService deductApplicationDetailService;
	
	@Autowired
	private DeductPlanBusinessHandler deductPlanHandler;
	
	@Autowired
	private UnionpayBankConfigService unionpayBankConfigService;
	
	@Autowired
	private PaymentChannelInformationHandler paymentChannelInformationHandler;
	
	@Autowired
	private LedgerBookStatHandler  ledgerBookStatHandler;
	
	@Autowired
	private RepaymentPlanHandler repaymentPlanHandler;

	@Autowired
	private ContractService contractService;
	
	@Autowired
	private BankService  bankService;
	
	@Autowired
	private  ProvinceService provinceService;
	
	@Autowired
	private  CityService cityService;
	
	@Autowired 
	private DeductPlanCacheService deductPlanCacheService;

	@Autowired
	private FinancialContractConfigurationService financialContractConfigurationService;

	@Autowired
	private DictionaryService dictionaryService;

	@Autowired
	private DeductNotifyJobServer deductSendMorganStanleyNotifyJobServer;

	@Autowired
	DeductApplicationDetailHandler deductAppDetailHandler;

	@Autowired
	DeductRequestLogService deductRequestLogService;

	@Value("#{config['yunxin.notifyUrl']}")
	private String notifyUrl;

	@Value("#{config['planNotifyNumber']}")
	private int planNotifyNumber;

	@Value("#{config['zhonghang.merId']}")
	private String YX_API_MERID;

	@Value("#{config['zhonghang.secret']}")
	private String YX_API_SECRET;

	@Value("#{config['notifyserver.groupCacheJobQueueMap_group3']}")
	private String toCitigroupModifyAsseset;

	@Value("#{config['urlToCitigroupModifyAsseset']}")
	private String urlToCitigroupModifyAsseset;

	@Value("#{config['planNotifyCitigroupNumber']}")
	private int planNotifyCitigroupNumber;

	@Value("#{config['deduct.notifyType']}")
	private String notifyType;

	private static final Log logger = LogFactory.getLog(DeductApplicationBusinessHandlerImpl.class);
	
	
	
	public void checkDeductInfoLogic(DeductCommandRequestModel commandModel,
			String ipAddress, String merchantId,List<RepaymentDetail> repaymentDetails) {
		
				String oppositeKeyWord="[requestNo="+commandModel.getRequestNo()+";";
				//合同校验
				Contract contract = checkContractVaild(commandModel, oppositeKeyWord);
				//信托合同校验
				FinancialContract financialContract = checkFinancialContractVaild(commandModel, oppositeKeyWord, contract);
				
		        checkRequestNoAndDeductIdVaild(commandModel, oppositeKeyWord);
				// 校验还款计划是否在本合同内,还款计划必须有效
				checkRepaymentInContract(contract, repaymentDetails);

				//校验还款计划是否已经存在处理中的扣款申请，
				checkExistDeductApplicationInRepaymentPlans(contract, repaymentDetails);
				
				//校验扣款接口调用时间
				checkRepaymetApiCalledTime(commandModel, financialContract.getLoanOverdueStartDay());
				//如果为提前划扣上一期必须以完成
				ifPreRepayTheBeforeAssetMustClear(commandModel,repaymentDetails);
				
				// 校验还款明细总额是否相等,
				checkRepaymentDetailAmount(commandModel,repaymentDetails);
				
				//校验扣款金额和还款总额是否相等
				checkRepaymentTotalAmountEqualsDeductAmount(commandModel, repaymentDetails);

				//校验应还未还金额(明细)
				checkAccontReceiveAmount(commandModel,repaymentDetails, contract, financialContract);
				
				// 扣款金额校验(总额)
				checkDeductAmount(commandModel, repaymentDetails, contract, financialContract);

	}

	private void ifPreRepayTheBeforeAssetMustClear(DeductCommandRequestModel commandModel, List<RepaymentDetail> repaymentDetails) {
		if(RepaymentType.fromValue(commandModel.getRepaymentType()) ==  RepaymentType.ADVANCE && repaymentDetails.size() ==1 ){
			AssetSet repaymentPlan = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentDetails.get(0).getRepaymentPlanNo());
			AssetSet beforePeriodRepaymentPlan = repaymentPlanService.getBeforePeriodAndOpenRepaymentPlan(repaymentPlan);
			if(beforePeriodRepaymentPlan == null){
				return;
			}
			if(beforePeriodRepaymentPlan.getAssetStatus() != AssetClearStatus.CLEAR ){
				throw new ApiException(ApiResponseCode.PRE_DEDUCT_BEFORE_PREIODS_MUST_OVER); 
			}
		}
		
	}

	private void checkRequestNoAndDeductIdVaild(DeductCommandRequestModel commandModel, String oppositeKeyWord) {
		// 请求号校验
		if (existRequest(commandModel)) {
			logger.info(GloableLogSpec.AuditLogHeaderSpec() + bridgewater_deduct_function_point.CHECK_AND_SAVE_DEDUCT_INFO_BEFORE_PORCESSING +oppositeKeyWord+"FAILED[FINANCIAL_PRODUCT_CODE_ERROR]");
			throw new ApiException(ApiResponseCode.REPEAT_REQUEST_NO);
		}
		// 商户提交的扣款id唯一性校验
		if (existDeduct(commandModel)) {
			throw new ApiException(ApiResponseCode.REPEAT_DEDUCT_ID);
		}
	}

	private Contract checkContractVaild(DeductCommandRequestModel commandModel, String oppositeKeyWord) {
		Contract contract = contractApiHandler.getContractBy(commandModel.getUniqueId(), commandModel.getContractNo());
		if(contract == null){
			logger.info(GloableLogSpec.AuditLogHeaderSpec() + bridgewater_deduct_function_point.CHECK_AND_SAVE_DEDUCT_INFO_BEFORE_PORCESSING +oppositeKeyWord+"FAILED[CONTRACT_NOT_EXIST]");
			throw new ApiException(ApiResponseCode.CONTRACT_NOT_EXIST);
		}
		return contract;
	}

	private FinancialContract checkFinancialContractVaild(DeductCommandRequestModel commandModel, String oppositeKeyWord,
			Contract contract) {
		FinancialContract financialContract = financialContractService.getFinancialContractBy(contract.getFinancialContractUuid());
		if (!financialContract.getContractNo().equals(commandModel.getFinancialProductCode())) {
			logger.info(GloableLogSpec.AuditLogHeaderSpec() + bridgewater_deduct_function_point.CHECK_AND_SAVE_DEDUCT_INFO_BEFORE_PORCESSING +oppositeKeyWord+"FAILED[FINANCIAL_PRODUCT_CODE_ERROR]");
			throw new ApiException(ApiResponseCode.FINANCIAL_PRODUCT_CODE_ERROR);
		}
		return financialContract;
	}

	private void checkAccontReceiveAmount(DeductCommandRequestModel commandModel,
			List<RepaymentDetail> repaymentDetails, Contract contract, FinancialContract financialContract) {
		for (RepaymentDetail repaymentDetail : repaymentDetails) {

			 Map<String,BigDecimal>  detailAccountReceiveAmountMap = accountReceivableAmountMap(commandModel, repaymentDetail, contract, financialContract);
			 BigDecimal caclTotalAccountReceivableAmount =  calcAccountReceivableAmount(commandModel, repaymentDetail, contract, financialContract);
			 checkDetailAmountLessAndEqualsThanPlanAmount(repaymentDetail.getRepaymentAmount(),caclTotalAccountReceivableAmount);
			 
			 //TODO 农分期历史数据跳过本金利息校验
			 if(!StringUtils.equals("G08200", commandModel.getFinancialProductCode())) {
				 checkDetailAmountLessAndEqualsThanPlanAmount(repaymentDetail.getRepaymentPrincipal(), detailAccountReceiveAmountMap.get(ExtraChargeSpec.LOAN_ASSET_PRINCIPAL_KEY));
				 checkDetailAmountLessAndEqualsThanPlanAmount(repaymentDetail.getRepaymentInterest(), detailAccountReceiveAmountMap.get(ExtraChargeSpec.LOAN_ASSET_INTEREST_KEY));
			 }
			 checkDetailAmountLessAndEqualsThanPlanAmount(repaymentDetail.getTechFee(), detailAccountReceiveAmountMap.get(ExtraChargeSpec.LOAN_TECH_FEE_KEY));
			 checkDetailAmountLessAndEqualsThanPlanAmount(repaymentDetail.getOtherFee(), detailAccountReceiveAmountMap.get(ExtraChargeSpec.LOAN_OTHER_FEE_KEY));
			 checkDetailAmountLessAndEqualsThanPlanAmount(repaymentDetail.getLoanFee(), detailAccountReceiveAmountMap.get(ExtraChargeSpec.LOAN_SERVICE_FEE_KEY));
			 checkDetailAmountLessAndEqualsThanPlanAmount(repaymentDetail.getOverDueFeeDetail().getTotalOverdueFee(), calcOverDueFeeTotalAmount(detailAccountReceiveAmountMap));
		}
		
	}

	private BigDecimal calcOverDueFeeTotalAmount(Map<String,BigDecimal> detailAccountReceiveAmountMap){
		return detailAccountReceiveAmountMap.getOrDefault(ExtraChargeSpec.PENALTY_KEY, BigDecimal.ZERO)
		.add(detailAccountReceiveAmountMap.getOrDefault(ExtraChargeSpec.OVERDUE_FEE_OBLIGATION_KEY, BigDecimal.ZERO))
		.add(detailAccountReceiveAmountMap.getOrDefault(ExtraChargeSpec.OVERDUE_FEE_SERVICE_FEE_KEY, BigDecimal.ZERO))
		.add(detailAccountReceiveAmountMap.getOrDefault(ExtraChargeSpec.OVERDUE_FEE_OTHER_FEE_KEY, BigDecimal.ZERO));
	}
	@Override
	public List<TradeSchedule> checkAndsaveDeductInfoBeforePorcessing(DeductCommandRequestModel commandModel,
			String ipAddress, String merchantId) {

		recordDeductRequestLog(commandModel);
		
		List<RepaymentDetail> repaymentDetails = commandModel.getRepaymentDetailArray();
		checkDeductInfoLogic(commandModel,ipAddress,merchantId,repaymentDetails);

		Contract contract = contractApiHandler.getContractBy(commandModel.getUniqueId(), commandModel.getContractNo());
		FinancialContract financialContract = financialContractService.getFinancialContractBy(contract.getFinancialContractUuid());
		List<TradeSchedule> tradeSchedules = saveDeductInfoBeforeProcessing(commandModel, ipAddress, merchantId,contract, financialContract);

		return tradeSchedules;
	}

	private void recordDeductRequestLog(DeductCommandRequestModel commandModel) {
		String oppositeKeyWord="[requestNo="+commandModel.getRequestNo()+";";
		logger.info(GloableLogSpec.AuditLogHeaderSpec() + bridgewater_deduct_function_point.CHECK_AND_SAVE_DEDUCT_INFO_BEFORE_PORCESSING +oppositeKeyWord+GloableLogSpec.RawData(JSON.toJSONString(commandModel)));
	}

	@Override
	public List<TradeSchedule> saveDeductInfoBeforeProcessing(DeductCommandRequestModel commandModel, String ipAddress,
			String merchantId, Contract contract, FinancialContract financialContract) {
		// 根据拆分规则拆分扣款申请单
		List<TradeSchedule> tradeSchedules = buildTradeScheduleForDeduct(commandModel.getDeductApplicationUuid(),
				commandModel, contract, financialContract);

		saveDeductInfoAndUpdateAssetSetDeductionStatus(commandModel, ipAddress, merchantId, contract, financialContract, tradeSchedules);
		return tradeSchedules;
	}


	private void checkExistDeductApplicationInRepaymentPlans(Contract contract,
			List<RepaymentDetail> repaymentDetails) {
		
		for(RepaymentDetail repaymentDetail:repaymentDetails){
			AssetSet repaymentPlan = repaymentPlanService
					.getRepaymentPlanByRepaymentCode(repaymentDetail.getRepaymentPlanNo());
			if(repaymentPlan.getPaymentStatus() == PaymentStatus.SUCCESS){
				throw new ApiException(ApiResponseCode.REPAYMENT_PLAN_HAS_SUCCESS);
			}
			
			List<DeductApplication> deductApplications = deductApplicationService.getDeductApplicationByRepaymentPlanCodeAndInprocessing(repaymentPlan.getAssetUuid());
			if(!CollectionUtils.isEmpty(deductApplications)){
				throw new ApiException(ApiResponseCode.HAS_EXIST_DEDUCT_APPLICATION);
			}
		}
	}


	private void checkRepaymetApiCalledTime(DeductCommandRequestModel commandModel, int overdueStartDay) {
		int repaymentType = commandModel.getRepaymentType();
		List<RepaymentDetail> repaymentDetails = commandModel.getRepaymentDetailArray();
		checkTheRpaymentDetailsSize(repaymentDetails,repaymentType);

		for (int index = 0; index < repaymentDetails.size(); index++) {
			String repaymentCode = repaymentDetails.get(index).getRepaymentPlanNo();
			AssetSet repaymentPlan = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentCode);
			Date planedRecycleDate = repaymentPlan.getAssetRecycleDate();
			checkApiCalledTime(commandModel, planedRecycleDate, repaymentType, overdueStartDay);
		}

	}
	


	private void checkApiCalledTime(DeductCommandRequestModel commandModel, Date plannedRecycleDate ,int repaymentType, int loanOverdueStartDay ){

		String apiCalledDayStr = commandModel.getApiCalledTime();
		Date apiCalledDay = DateUtils.asDay(apiCalledDayStr);
		
		boolean isBeforePlanedDay = apiCalledDay.before(plannedRecycleDate);
		boolean valid = false;
		
		RepaymentType repaymentTypeEnum = EnumUtil.fromOrdinal(RepaymentType.class, repaymentType);
		switch (repaymentTypeEnum) {
		case ADVANCE:
			valid = isBeforePlanedDay;
			break;
		case NORMAL:
			valid = !isBeforePlanedDay;
			break;
		case OVERDUE:
			if(isBeforePlanedDay) {
				valid = false;
			}else {
				int intervalDay = DateUtils.compareTwoDatesOnDay(apiCalledDay, plannedRecycleDate);
				//间隔日为负值，或间隔日小于逾期起始日，记正常扣款
				if(intervalDay <= 0 || intervalDay < loanOverdueStartDay) {
					valid = false;
				}else {
					//否则，记逾期扣款
					valid = true;
				}
			}
			break;
		default:
			break;
		}
		
		if(valid == false){
			throw new ApiException(ApiResponseCode.API_CALLED_TIME_NOT_MEET_PLAN_RECYCLE_TIME);
		}
	}

	private void checkTheRpaymentDetailsSize(
			List<RepaymentDetail> repaymentDetails, int repaymentType) {
		
		int repaymentDetailSize =  repaymentDetails.size();
		if(RepaymentType.fromValue(repaymentType) ==  RepaymentType.OVERDUE & repaymentDetailSize == 0 ){
			throw new ApiException(ApiResponseCode.REPAYMENT_PLAN_NUMBER_ERROR);
		}
		if((RepaymentType.fromValue(repaymentType) !=  RepaymentType.OVERDUE) && repaymentDetailSize != 1){
			throw new ApiException(ApiResponseCode.REPAYMENT_PLAN_NUMBER_ERROR);
		}
		
	}

	private void checkDeductAmount(DeductCommandRequestModel commandModel, List<RepaymentDetail> repaymentDetails, Contract contract, FinancialContract financialContract) {
		
		boolean isLegal = true;
		
		BigDecimal totalAccountsReceivableAmount  = calcTotalAccountsReceivableAmount(commandModel,repaymentDetails, contract, financialContract);
		
		BigDecimal deductAmount = new BigDecimal(commandModel.getAmount());
		
		switch (RepaymentType.fromValue(commandModel.getRepaymentType())) {
		
		case ADVANCE:
        case NORMAL:
        	isLegal = deductAmountBetweenZeroToTotalAmount(totalAccountsReceivableAmount,deductAmount);
        	break;
        case OVERDUE:
        	isLegal = deductAmountBetweenZeroToTotalAmount(totalAccountsReceivableAmount,deductAmount);
			break;
		}
		if(isLegal == false){
			throw new ApiException(ApiResponseCode.DEDUCT_AMOUNT_ERROR);
		}
	}


	private BigDecimal calcTotalAccountsReceivableAmount(DeductCommandRequestModel commandModel,List<RepaymentDetail> repaymentDetails, Contract contract, FinancialContract financialContract) {
		BigDecimal calcTotalAmount = BigDecimal.ZERO;
		
		for (RepaymentDetail repaymentDetail : repaymentDetails) {
			BigDecimal receivableAmount = calcAccountReceivableAmount(commandModel, repaymentDetail, contract, financialContract);
			calcTotalAmount = calcTotalAmount.add(receivableAmount);
		}
		return calcTotalAmount;
	}
	

	private boolean deductAmountBetweenZeroToTotalAmount(BigDecimal calcTotalAmount, BigDecimal deductAmount) {

		return (deductAmount.compareTo(BigDecimal.ZERO) == 1)
				&& (deductAmount.compareTo(calcTotalAmount.add(new BigDecimal(THE_CALIBRATION_VALUE))) == -1);
	}
	


	@Override
	public void processingAndUpdateDeducInfo_NoRollback(List<TradeSchedule> tradeSchedules,
			String deductApplicationUuid, String requestNo) {

		String localKeyWord="BRIDGEWATER[requestNo="+requestNo+";deductApplicationUuid="+deductApplicationUuid+
				"]";
		String remoteKeyWord="==SENTTO==>>JPMORGAN[";
		for(TradeSchedule trade:tradeSchedules)
		{
			remoteKeyWord+="SourceMessageUuid="+trade.getSourceMessageUuid()+";";
		}
		remoteKeyWord+="]}";
		
		String requestBody = JSON.toJSONString(tradeSchedules, SerializerFeature.DisableCircularReferenceDetect);
		
		logger.info(GloableLogSpec.AuditLogHeaderSpec() +bridgewater_deduct_function_point.SEND_TRADE_SCHEDULES_TO_JPMORGAN +localKeyWord+remoteKeyWord+GloableLogSpec.RawData(requestBody));

		Result result = jpmorganApiHelper.sendBatchTradeSchedules(tradeSchedules, requestBody);

		if (!result.isValid()) {
			logger.error("扣款请求受理失败，递交对端失败!deductApplicationUuid="+deductApplicationUuid);
			logger.info(GloableLogSpec.AuditLogHeaderSpec() +bridgewater_deduct_function_point.SEND_TRADE_SCHEDULES_TO_JPMORGAN+ localKeyWord+ "[扣款请求受理失败，递交对端失败!]"+GloableLogSpec.RawData(JSON.toJSONString(result,SerializerFeature.DisableCircularReferenceDetect)));
			
			updateDeductInfoAfterSendFailBy(deductApplicationUuid,tradeSchedules,ApiMessageUtil.getMessage(ApiResponseCode.SYSTEM_BUSY));
			//更新资产扣款状态为失败
			repaymentPlanHandler.updateAssetSetDeductionStatus(deductApplicationUuid, DeductionStatus.FAIL);

			unlockRepaymentPlans(deductApplicationUuid);

			throw new ApiException(ApiResponseCode.SYSTEM_BUSY);
		}
		
		Result responseResult = parseResult(result);
		if (responseResult != null && !responseResult.isValid()) {
			logger.info(GloableLogSpec.AuditLogHeaderSpec() + bridgewater_deduct_function_point.SEND_TRADE_SCHEDULES_TO_JPMORGAN+localKeyWord+ "[扣款请求受理失败，对端响应"+responseResult.getMessage()+"]"+GloableLogSpec.RawData(JSON.toJSONString(result,SerializerFeature.DisableCircularReferenceDetect)));
			logger.error("扣款请求受理失败，对端响应（" + responseResult.getMessage() + "）!deductApplicationUuid="+deductApplicationUuid);
			updateDeductInfoAfterSendFailBy(deductApplicationUuid,tradeSchedules, "扣款请求受理失败!");
			
			//更新资产扣款状态为失败
			repaymentPlanHandler.updateAssetSetDeductionStatus(deductApplicationUuid, DeductionStatus.FAIL);
			unlockRepaymentPlans(deductApplicationUuid);
			throw new ApiException(ApiResponseCode.SYSTEM_ERROR, "扣款请求受理失败!");
		}
		try {
			updateDeductInfoAndAssetSetDeductionStatus(deductApplicationUuid,tradeSchedules);
		} catch (Exception e) {
			logger.info(GloableLogSpec.AuditLogHeaderSpec() + bridgewater_deduct_function_point.SEND_TRADE_SCHEDULES_TO_JPMORGAN+localKeyWord+ "ERR:[落盘对端处理中失败]");
			e.printStackTrace();
		}
		
		
		logger.info(GloableLogSpec.AuditLogHeaderSpec() +bridgewater_deduct_function_point.SEND_TRADE_SCHEDULES_TO_JPMORGAN+localKeyWord+"扣款请求受理成功");

	}
	
	public void updateDeductInfoAndAssetSetDeductionStatus(String deductApplicationUuid, List<TradeSchedule> tradeSchedules){
		updateDeductInfoAfterSendSuccessBy(deductApplicationUuid,tradeSchedules);
		//更新资产扣款状态为对端处理中
		
		repaymentPlanHandler.updateAssetSetDeductionStatus(deductApplicationUuid, DeductionStatus.OPPOSITE_PROCESSING);
	}
	
	
	/**
	 * 更新交易接收方状态，为对端（交易受理成功）
	 * @param tradeSchedules 
	 * @param deductApplicationUuid
	 */
	private void updateDeductInfoAfterSendSuccessBy(
			String deductApplicationUuid, List<TradeSchedule> tradeSchedules) {
		if(StringUtils.isEmpty(deductApplicationUuid)) {
			return;
		}
		Map<String, Object> paramsForFail = new HashMap<String, Object>();
		paramsForFail.put("executionRemark", "");
		paramsForFail.put("lastModifiedTime", new Date());
		paramsForFail.put("deductApplicationUuid", deductApplicationUuid);
		paramsForFail.put("transactionRecipient", TransactionRecipient.OPPOSITE);
		//暂时一对一
		paramsForFail.put("deductPlanUuid", tradeSchedules.get(0).getOutlierTransactionUuid());
		paramsForFail.put("executionStatus", DeductApplicationExecutionStatus.PROCESSING);
		executeHqlForDeductPlan(paramsForFail);
		executeHqlForDeductAppplication(paramsForFail);
	}

	private void updateDeductInfoAfterSendFailBy(String deductApplicationUuid, List<TradeSchedule> tradeSchedules, String executionRemark) {
		Map<String, Object> paramsForFail = new HashMap<String, Object>();
		paramsForFail.put("executionRemark", executionRemark);
		paramsForFail.put("lastModifiedTime", new Date());
		paramsForFail.put("deductApplicationUuid", deductApplicationUuid);
		paramsForFail.put("transactionRecipient", TransactionRecipient.LOCAL);
		//暂时一对一
		paramsForFail.put("deductPlanUuid", tradeSchedules.get(0).getOutlierTransactionUuid());
		paramsForFail.put("executionStatus", DeductApplicationExecutionStatus.FAIL);
		executeHqlForDeductPlan(paramsForFail);
		paramsForFail.put("executionStatus", DeductApplicationExecutionStatus.FAIL);
		executeHqlForDeductAppplication(paramsForFail);
		
	}

	private int executeHqlForDeductAppplication( Map<String, Object> paramsForFail) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("UPDATE DeductApplication ");
		buffer.append("SET executionStatus =:executionStatus, executionRemark =:executionRemark,transactionRecipient =:transactionRecipient,lastModifiedTime =:lastModifiedTime WHERE deductApplicationUuid =:deductApplicationUuid");
		return genericDaoSupport.executeHQL(buffer.toString(), paramsForFail);
	}
	
	private int executeHqlForDeductPlan(Map<String, Object> paramsForFail) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("UPDATE DeductPlan ");
		buffer.append("SET executionStatus =:executionStatus, executionRemark =:executionRemark,transactionRecipient =:transactionRecipient, lastModifiedTime =:lastModifiedTime WHERE deductApplicationUuid =:deductApplicationUuid and deductPlanUuid =:deductPlanUuid");
		return genericDaoSupport.executeHQL(buffer.toString(), paramsForFail);
	}

	private void saveDeductInfoAndUpdateAssetSetDeductionStatus(DeductCommandRequestModel commandModel, String ipAddress, String merchantId,
			Contract contract, FinancialContract financialContract, List<TradeSchedule> tradeSchedules) {
		//被扣款资产编号
		List<String> repaymentCodes = commandModel.getRepaymentDetailArray().stream().map(fc -> fc.getRepaymentPlanNo()).collect(Collectors.toList());
		
		DeductApplication deductApplication = createAndSaveDeductApplication(commandModel, ipAddress, merchantId, contract,financialContract, repaymentCodes);

		createAndSaveDeductApplicationDetilInfo(commandModel, deductApplication, contract, financialContract);

		for (TradeSchedule tradeSchedule : tradeSchedules) {
			createAndSaveDeductPlan(commandModel, merchantId, contract, financialContract, deductApplication,
					tradeSchedule, deductApplication.getSourceType());
		}
		//更新资产扣款状态为本端处理中,执行状态为处理中
		updateDeductionStatusAndExectingStatus(repaymentCodes);
	}

	private void updateDeductionStatusAndExectingStatus(List<String> repaymentCodes) {
		repaymentPlanService.updateAssetSetDeductionStatusByAssetSetUuid(repaymentCodes, DeductionStatus.LOCAL_PROCESSING);
		repaymentPlanService.updateAssetSetEXecutionStatusByAssetSetUuids(repaymentCodes,ExecutingStatus.PROCESSING);
	}

	private void createAndSaveDeductPlan(DeductCommandRequestModel commandModel, String merchantId, Contract contract,
			FinancialContract financialContract, DeductApplication deductApplication, TradeSchedule tradeSchedule, SourceType sourceType) {
		DeductPlan deductPlan = convertToDeductPlan(tradeSchedule, deductApplication.getDeductApplicationUuid(),
				financialContract.getFinancialContractUuid(), commandModel.getUniqueId(),
				contract.getContractNo(), merchantId,commandModel.getMobile(),tradeSchedule.getRelatedPaymentGateway(), deductApplication.getRepaymentType(), deductApplication.getSourceType());
		deductPlanService.save(deductPlan);
	}

	private DeductApplication createAndSaveDeductApplication(DeductCommandRequestModel commandModel, String ipAddress,
			String merchantId, Contract contract, FinancialContract financialContract, List<String> repaymentCodes) {
		
		DeductApplication deductApplication = new DeductApplication(commandModel, contract, financialContract,
				merchantId, ipAddress, repaymentCodes, notifyUrl,planNotifyNumber);
		deductApplicationService.save(deductApplication);
		return deductApplication;
	}

	private void createAndSaveDeductApplicationDetilInfo(DeductCommandRequestModel commandModel,
			DeductApplication deductApplication, Contract contract, FinancialContract financialContract) {
		List<RepaymentDetail> repaymentDetails = commandModel.getRepaymentDetailArray();

		for (RepaymentDetail repaymentDetail : repaymentDetails) {
			//校验并发
			checkConcurrent(deductApplication, repaymentDetail);
			
			createSingleDeductApplicationDetail(deductApplication, repaymentDetail,
					ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_PRINCIPLE, IsTotal.DETAIL,
					repaymentDetail.getRepaymentPrincipal());
			createSingleDeductApplicationDetail(deductApplication, repaymentDetail,
					ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_INTEREST, IsTotal.DETAIL,
					repaymentDetail.getRepaymentInterest());
			createSingleDeductApplicationDetail(deductApplication, repaymentDetail,
					ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_TECH_FEE, IsTotal.DETAIL, repaymentDetail.getTechFee());
			createSingleDeductApplicationDetail(deductApplication, repaymentDetail,
					ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_LOAN_SERVICE_FEE, IsTotal.DETAIL,
					repaymentDetail.getLoanFee());
			createSingleDeductApplicationDetail(deductApplication, repaymentDetail,
					ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_OTHER_FEE, IsTotal.DETAIL, repaymentDetail.getOtherFee());
			
			if(repaymentDetail.getOverDueFeeDetail().overDueFeeDetailRule() == 1){
				
				createSingleDeductApplicationDetail(deductApplication, repaymentDetail,
						ChartOfAccount.SND_RECIEVABLE_LOAN_PENALTY, IsTotal.DETAIL,
						repaymentDetail.getOverDueFeeDetail().getPenaltyFee());
				createSingleDeductApplicationDetail(deductApplication, repaymentDetail,
						ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION, IsTotal.DETAIL,
						repaymentDetail.getOverDueFeeDetail().getLatePenalty());
				createSingleDeductApplicationDetail(deductApplication, repaymentDetail,
						ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE, IsTotal.DETAIL,
						repaymentDetail.getOverDueFeeDetail().getLateFee());
				createSingleDeductApplicationDetail(deductApplication, repaymentDetail,
						ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE, IsTotal.DETAIL,
						repaymentDetail.getOverDueFeeDetail().getLateOtherCost());
			}
			createSingleDeductApplicationDetailTotalOverDueFee(deductApplication, repaymentDetail, IsTotal.DETAIL, repaymentDetail.getOverDueFeeDetail().getTotalOverdueFee());
			BigDecimal caclAccountReceivableAmount =calcAccountReceivableAmount(commandModel, repaymentDetail, contract, financialContract);
			createSingleDeductApplicationDetailTotalReceivableAmount(deductApplication, repaymentDetail, IsTotal.TOTAL,
					caclAccountReceivableAmount);
		}
	}


	public void checkConcurrent(DeductApplication deductApplication, RepaymentDetail repaymentDetail) {
		AssetSet repaymentPlan = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentDetail.getRepaymentPlanNo());
		
		judgeTheRepaymentPlanIsUnlock(repaymentPlan);
		String newDeductApplicationUuid = lockTheRepayemntPlanToDeduct(deductApplication, repaymentPlan);
		checkTheReapymentPlanIsInDeductLock(repaymentPlan, newDeductApplicationUuid);
	}

	private void checkTheReapymentPlanIsInDeductLock(AssetSet repaymentPlan, String newDeductApplicationUuid) {
		String  updatedActiveDeductApplicationUuid = repaymentPlanService.queryActiveDeductApplicationUuidBySql(repaymentPlan.getAssetUuid());
		if(!updatedActiveDeductApplicationUuid.equals(newDeductApplicationUuid)){
			throw new ApiException(ApiResponseCode.DEDUCT_CONCURRENT_ERROR);
		}
	}

	private String lockTheRepayemntPlanToDeduct(DeductApplication deductApplication, AssetSet repaymentPlan) {
		String newDeductApplicationUuid = deductApplication.getDeductApplicationUuid();
		repaymentPlanService.updateActiveDeductApplicationUuidBySql(repaymentPlan.getAssetUuid(), newDeductApplicationUuid, AssetSet.EMPTY_UUID);
		return newDeductApplicationUuid;
	}

	private void judgeTheRepaymentPlanIsUnlock(AssetSet repaymentPlan) {
		if(!repaymentPlanService.queryActiveDeductApplicationUuidBySql(repaymentPlan.getAssetUuid()).equals(AssetSet.EMPTY_UUID)){
			throw new ApiException(ApiResponseCode.DEDUCT_CONCURRENT_ERROR); 
		}
	}

	
	private void createSingleDeductApplicationDetailTotalReceivableAmount(DeductApplication deductApplication,
			RepaymentDetail repaymentDetail, IsTotal total, BigDecimal caclAccountReceivableAmount) {

		if(caclAccountReceivableAmount.compareTo(BigDecimal.ZERO) < 1){
			return ;
		}
		AssetSet repaymentPlan = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentDetail.getRepaymentPlanNo());
		DeductApplicationDetail deductApplicationDetail = new DeductApplicationDetail(deductApplication,repaymentDetail, total, caclAccountReceivableAmount, repaymentPlan.getAssetUuid());
		deductApplicationDetail.setFirstAccountName(ExtraChargeSpec.TOTAL_RECEIVABEL_AMOUNT);
		deductApplicationDetailService.save(deductApplicationDetail);
	}

	private void createSingleDeductApplicationDetailTotalOverDueFee(
			DeductApplication deductApplication,
			RepaymentDetail repaymentDetail, IsTotal isTotal,
			BigDecimal totalOverdueFee) {
		
		//金额小于等于零不生成明细记录
		if(totalOverdueFee.compareTo(BigDecimal.ZERO) < 1){
			return ;
		}
		AssetSet repaymentPlan = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentDetail.getRepaymentPlanNo());
		DeductApplicationDetail deductApplicationDetail = new DeductApplicationDetail(deductApplication,
				repaymentDetail, isTotal, totalOverdueFee, repaymentPlan.getAssetUuid());
		deductApplicationDetail.setFirstAccountName(ExtraChargeSpec.TOTAL_OVERDUE_FEE);
		deductApplicationDetailService.save(deductApplicationDetail);
		
	}
	
	private void createSingleDeductApplicationDetail(DeductApplication deductApplication,
			RepaymentDetail repaymentDetail, String chartString, IsTotal isTotal, BigDecimal amount) {
		//金额小于等于零不生成明细记录
		if(amount.compareTo(BigDecimal.ZERO) < 1){
			return ;
		}
		AssetSet repaymentPlan = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentDetail.getRepaymentPlanNo());
		DeductApplicationDetail deductApplicationDetail = new DeductApplicationDetail(deductApplication,
				repaymentDetail, isTotal, amount, repaymentPlan.getAssetUuid());
		deductApplicationDetail.copyTAccount(ChartOfAccount.EntryBook().get(chartString));
		deductApplicationDetailService.save(deductApplicationDetail);
	}

	private DeductPlan convertToDeductPlan(TradeSchedule tradeSchedule, String deductApplicationUuid,
			String financialContractUuid, String contractUniqueId, String contractNo, String merchantId,String mobile,Integer paymentGateway, RepaymentType repaymentType, SourceType sourceType) {
		String deductPlanUuid = tradeSchedule.getOutlierTransactionUuid();
		String deductApplicationDetailUuid = tradeSchedule.getRelatedDetailUuid();

		String paymentChannelUuid = tradeSchedule.getFstPaymentChannelUuid();
		int transactionType = tradeSchedule.getEnumAccountSide().ordinal();
		String pgClearingAccount = tradeSchedule.getReckonAccount();
		String cpBankCode = tradeSchedule.getBankCode();
		String cpBankCardNo = tradeSchedule.getDestinationAccountNo();
		String cpBankAccountHolder = tradeSchedule.getDestinationAccountName();
		String cpIdNumber = tradeSchedule.getIdNumber();
		String cpBankProvince = tradeSchedule.getBankProvince();
		String cpBankCity = tradeSchedule.getBankCity();
		String cpBankName = tradeSchedule.getBankName();
		Date plannedPaymentDate = tradeSchedule.getFstEffectiveAbsoluteTime();
		BigDecimal plannedTotalAmount = tradeSchedule.getFstTransactionAmount();
		String executionPrecond = tradeSchedule.getExecutionPrecond();
		
		PaymentInstitutionName gateway = EnumUtil.fromOrdinal(PaymentInstitutionName.class, paymentGateway);
		return new DeductPlan(deductPlanUuid, deductApplicationUuid, deductApplicationDetailUuid, financialContractUuid,
				contractUniqueId, contractNo, gateway, paymentChannelUuid, null, pgClearingAccount, transactionType, null, cpBankCode,
				cpBankCardNo, cpBankAccountHolder, CertificateType.ID_CARD, cpIdNumber, cpBankProvince, cpBankCity,
				cpBankName, plannedPaymentDate, plannedTotalAmount, executionPrecond, merchantId,mobile, repaymentType,sourceType);

	}
	boolean IsVaildPaymentChannel(List<PaymentChannelSummaryInfo> deductChannelInfoList){
		
		if(CollectionUtils.isEmpty(deductChannelInfoList)){
			return false;
		}
		for(PaymentChannelSummaryInfo deductChannelInfo:deductChannelInfoList){
			if(StringUtils.isEmpty(deductChannelInfo.getChannelServiceUuid())){
				return false;
			}
		}
		return true;
		
	}
	
	
	private List<TradeSchedule> buildTradeScheduleForDeduct(String deductApplicationUuid,
			DeductCommandRequestModel commandModel, Contract contract, FinancialContract financialContract) {

		ContractAccount bindContractAccount = contractAccountService.getTheEfficientContractAccountBy(contract);

		ContractAccount contractAccount = new ContractAccount(bindContractAccount.getStandardBankCode(),
			bindContractAccount.getBankCode(), bindContractAccount.getBank(),
			bindContractAccount.getProvinceCode(), bindContractAccount.getCityCode(),
			bindContractAccount.getPayerName(), bindContractAccount.getPayAcNo(), bindContractAccount.getIdCardNum());

		if(!commandModel.repaymentInformationIsAllNull()){
			extractPaymentAccountInfo(commandModel, contractAccount);
		}
		
//		ContractAccount contractAccount = contractAccountService.getTheEfficientContractAccountBy(contract);
		List<TradeSchedule> tradeSchedules = new LinkedList<TradeSchedule>();
		PaymentChannelSummaryInfo deductChannelInfo = getCurrentUsingPaymentChannel(commandModel, financialContract,
				contractAccount.getStandardBankCode());
				
		BigDecimal plannedAmount = new BigDecimal(commandModel.getAmount());

		String fstGatewayRouterInfo = getFstGateWayRouterInfo(contractAccount.getBankCode(),contractAccount.getStandardBankCode(),deductChannelInfo.getClearingNo());
		
		String destinationAccountAppendix = assembleDestinationAccountAppendixMap(contractAccount.getIdCardNum(),commandModel.getMobile());

		String destinationBankInfo = assembleDestinationBankInfoMap(contractAccount.getStandardBankCode(),contractAccount.getProvinceCode(),contractAccount.getCityCode(),contractAccount.getBank());
		
		tradeSchedules.add(new TradeSchedule(com.zufangbao.gluon.api.jpmorgan.enums.AccountSide.DEBIT, contractAccount.getPayerName(),
				contractAccount.getPayAcNo(), destinationAccountAppendix, destinationBankInfo, "",
				UUID.randomUUID().toString(), UUID.randomUUID().toString(), deductChannelInfo.getChannelServiceUuid(), null, plannedAmount,
				deductApplicationUuid, null, null, null, 0,  deductChannelInfo.getPaymentGateway(),
				deductChannelInfo.getPaymentChannelName(), fstGatewayRouterInfo));

		return tradeSchedules;

	}

	private void extractPaymentAccountInfo(DeductCommandRequestModel commandModel,ContractAccount contractAccount) {
		
		if(StringUtils.isNotBlank(commandModel.getPayerName())){
			contractAccount.setPayerName(commandModel.getPayerName());
		}
		if(StringUtils.isNotBlank(commandModel.getPayAcNo())){
			contractAccount.setPayAcNo(commandModel.getPayAcNo());
		}
		if(StringUtils.isNotBlank(commandModel.getBankCode())){
			Bank bank =  bankService.getCachedBanks().get(commandModel.getBankCode());
		    if(bank == null){
		    	throw new ApiException(ApiResponseCode.NO_MATCH_BANK);
		    }
		    contractAccount.setStandardBankCode(commandModel.getBankCode());
			String  unionBankCode = UnionPayBankCodeMap.BANK_CODE_MAP.get(commandModel.getBankCode());
			contractAccount.setBankCode(unionBankCode);
			contractAccount.setBank(bank.getBankName());
		}
		if(StringUtils.isNotBlank(commandModel.getProvinceCode())){
			Province province = provinceService.getProvinceByCode(commandModel.getProvinceCode());
		    if(province == null){
		    	throw new ApiException(ApiResponseCode.NO_MATCH_PROVINCE);
		    }
		    contractAccount.setProvinceCode(commandModel.getProvinceCode());
		}
		if(StringUtils.isNotBlank(commandModel.getCityCode())){
		    City city = cityService.getCityByCityCode(commandModel.getCityCode());
		    if(city == null){
		    	throw new ApiException(ApiResponseCode.NO_MATCH_CITY);
		    }
		    contractAccount.setCityCode(commandModel.getCityCode());
		}

		contractAccount.setIdCardNum(commandModel.getIdCardNum());
	}

	private PaymentChannelSummaryInfo getCurrentUsingPaymentChannel(DeductCommandRequestModel commandModel,
			FinancialContract financialContract, String standardBankCode) {
		List<PaymentChannelSummaryInfo> deductChannelInfoList = paymentChannelInformationHandler.getPaymentChannelServiceUuidsBy(financialContract.getFinancialContractUuid(),  BusinessType.SELF, com.zufangbao.sun.yunxin.entity.remittance.AccountSide.DEBIT,standardBankCode,null);
		
		if(!IsVaildPaymentChannel(deductChannelInfoList)) {
			throw new ApiException(ApiResponseCode.CHANNEL_NOT_FOUND);
		}
		PaymentChannelSummaryInfo deductChannelInfo = getCurrentUsingPaymentChannel(deductChannelInfoList,commandModel);
		if(deductChannelInfo == null){
			throw new ApiException(ApiResponseCode.CHANNEL_NOT_FOUND);
			
		}
		return deductChannelInfo;
	}

	private PaymentChannelSummaryInfo getCurrentUsingPaymentChannel(List<PaymentChannelSummaryInfo> deductChannelInfoList,
			DeductCommandRequestModel commandModel) {
		if(StringUtils.isNotEmpty(commandModel.getGateway())){
			PaymentInstitutionName paymentInstitutionName = DeductGatewayMapSpec.DEDUCT_GATEWAY_MAP.get(commandModel.getGateway());
			for(PaymentChannelSummaryInfo deductChannelInfo:deductChannelInfoList){
				if(deductChannelInfo.getPaymentGateway() == paymentInstitutionName.ordinal()){
					return deductChannelInfo;
				} 
			}
			return  null;
		}
		return deductChannelInfoList.get(0);
		
		
	}


	private String getFstGateWayRouterInfo(String bankCode,String standardBankCode, String clearingNo) {
		
		String debitMode =  unionpayBankConfigService.isUseBatchDeduct(bankCode,standardBankCode)?"batchMode":"realTimeMode";
		 
		
		Map<String,String> fstGateWayRouterInfoMap = new HashMap<String,String>();
		fstGateWayRouterInfoMap.put("debitMode",debitMode );
		if(StringUtils.isNotEmpty(clearingNo)) {
			fstGateWayRouterInfoMap.put("reckonAccount", clearingNo);
		}
		
		return JSON.toJSONString(fstGateWayRouterInfoMap);
	}

	private String assembleDestinationBankInfoMap(String standardBankCode,String provinceCode,String cityCode,String bankName) {
		Map<String, String> destinationBankInfoMap = new HashMap<String, String>();
		destinationBankInfoMap.put("bankCode", standardBankCode);
		destinationBankInfoMap.put("bankProvince", provinceCode);
		destinationBankInfoMap.put("bankCity",cityCode);
		destinationBankInfoMap.put("bankName", bankName);
		String destinationBankInfo = JSON.toJSONString(destinationBankInfoMap,
				SerializerFeature.DisableCircularReferenceDetect);
		return destinationBankInfo;
	}

	private String assembleDestinationAccountAppendixMap(String idCardNum, String mobile) {
		Map<String, String> destinationAccountAppendixMap = new HashMap<String, String>();
		destinationAccountAppendixMap.put("idNumber", idCardNum);
		destinationAccountAppendixMap.put("mobile", mobile);
		String destinationAccountAppendix = JSON.toJSONString(destinationAccountAppendixMap,
				SerializerFeature.DisableCircularReferenceDetect);
		return destinationAccountAppendix;
	}

	private void checkRepaymentInContract(Contract contract, List<RepaymentDetail> repaymentDetails) {
		for (RepaymentDetail repaymentDetail : repaymentDetails) {
			AssetSet repaymentPlan = repaymentPlanService
					.getActiveRepaymentPlanByRepaymentCode(repaymentDetail.getRepaymentPlanNo());
			if (repaymentPlan == null || repaymentPlan.getContract().getUniqueId() != contract.getUniqueId() ) {
				throw new ApiException(ApiResponseCode.REPAYMENT_CODE_NOT_IN_CONTRACT);
			}
			
		}
	}

	private void checkRepaymentTotalAmountEqualsDeductAmount(DeductCommandRequestModel commandModel,
			List<RepaymentDetail> repaymentDetails) {
		BigDecimal calcRepaymentTotalAmount = BigDecimal.ZERO;
		for (RepaymentDetail repaymentDetail : repaymentDetails) {
			calcRepaymentTotalAmount = calcRepaymentTotalAmount.add(repaymentDetail.getRepaymentAmount());
		}
		if (calcRepaymentTotalAmount.compareTo(new BigDecimal(commandModel.getAmount())) !=0 ) {
			throw new ApiException(ApiResponseCode.REPAYMENT_TOTAL_AMOUNT_NOT_EQUALS_DEDUCT_AMOUNT);
		}
	}
	@Override
	public  void checkRepaymentDetailAmount(DeductCommandRequestModel commandModel, List<RepaymentDetail> repaymentDetails) {
		for (RepaymentDetail repaymentDetail : repaymentDetails) {
			//逾期费用明细传明细校验，不传明细不校验
			if( (repaymentDetail.getOverDueFeeDetail().overDueFeeDetailRule() ==1) && !overDueTotalAmountEqualsToDetailSum(repaymentDetail)){
				throw new ApiException(ApiResponseCode.OVERDUE_FEE_ERROR);
			}
			if (repaymentDetail.calcRepaymentDetailTotalAmount().compareTo(repaymentDetail.getRepaymentAmount()) != 0) {
				throw new ApiException(ApiResponseCode.REPAYMENT_DETAILS_AMOUNT_ERROR);
			}
			
			
		}
	}

private boolean overDueTotalAmountEqualsToDetailSum(RepaymentDetail repaymentDetail) {
	return repaymentDetail.getOverDueFeeDetail().calctTotalOverDueFee().compareTo(repaymentDetail.getOverDueFeeDetail().getTotalOverdueFee()) ==0;
}

	private BigDecimal calcAccountReceivableAmount(DeductCommandRequestModel commandModel,
			RepaymentDetail repaymentDetail, Contract contract, FinancialContract financialContract) {
		AssetSet repaymentPlan = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentDetail.getRepaymentPlanNo());
		return ledgerBookStatHandler.unrecovered_asset_snapshot(financialContract.getLedgerBookNo(), repaymentPlan.getAssetUuid(), contract.getCustomer().getCustomerUuid(), true);
	}
	
	private Map<String,BigDecimal> accountReceivableAmountMap(DeductCommandRequestModel commandModel,RepaymentDetail repaymentDetail, Contract contract, FinancialContract financialContract){
		AssetSet repaymentPlan = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentDetail.getRepaymentPlanNo());
		return ledgerBookStatHandler.unrecovered_asset_snapshot(financialContract.getLedgerBookNo(), repaymentPlan.getAssetUuid(), true);
	}

	private void checkDetailAmountLessAndEqualsThanPlanAmount(BigDecimal part1Amount, BigDecimal part2Amount) {
		if(part2Amount == null){
			part2Amount = BigDecimal.ZERO;
		}
		if (part2Amount.compareTo(part1Amount) == -1) {
			throw new ApiException(ApiResponseCode.DEDUCT_AMOUNT_LESS_THAN_RECEIVE_AMOUNT);
		}
	}

	private boolean existDeduct(DeductCommandRequestModel commandModel) {
		return deductApplicationService.getDeductApplicationByDeductId(commandModel.getDeductId()) != null;
	}

	private boolean existRequest(DeductCommandRequestModel commandModel) {
		return deductApplicationService.getDeductApplicationByDeductRequestNo(commandModel.getRequestNo()) != null;
	}

	@Override
	public DeductApplication getDeductApplicationAndcheckData(OverdueDeductResultQueryModel queryModel,
			HttpServletRequest request) {
		
	DeductApplication deductApplication = deductApplicationService
					.getDeductApplicationByDeductId(queryModel.getDeductId());

		if (deductApplication == null) {
			throw new ApiException(ApiResponseCode.NOT_DEDUCT_ID);
		}
		return deductApplication;
	}

	@Override
	public List<QueryStatusResult> requestAndParsingResponse(String deductApplicationUuid) {
		Result result = jpmorganApiHelper.queryTradeSchedulesStatus(null, deductApplicationUuid);

		
		if (!result.isValid()) {
			throw new ApiException(ApiResponseCode.QUERY_SYSTEM_ERROR,"扣款查询接口， 通信失败！！");
		}
		Result responseResult = parseResult(result);
		
		if(responseResult == null || !responseResult.isValid()) {
			throw new ApiException(ApiResponseCode.QUERY_SYSTEM_ERROR,"扣款查询，对端响应失败！！");
		}

		String queryResultStr = String.valueOf(responseResult.get("queryResult"));
		List<QueryStatusResult> queryStatusResults = JSON.parseArray(queryResultStr, QueryStatusResult.class);
		if(queryStatusResults == null) {
			throw new ApiException(ApiResponseCode.QUERY_SYSTEM_ERROR,"扣款查询,返回报文解析错误！！");
		}
		return queryStatusResults;
	}
	
	//TODO
	@Override
	public DeductApplicationExecutionStatus evaluatedDeductApplicationStatusBy(List<DeductPlan> deductPlans, ReDeductDataPackage reDeductPackage) {
		
		if(reDeductPackage.isReDeduct() == true){
			return DeductApplicationExecutionStatus.PROCESSING;
		}
		return evaluateDeductApplicationStatusBy(deductPlans);
	}

	@Override
	public DeductApplicationExecutionStatus evaluateDeductApplicationStatusBy(List<DeductPlan> deductPlans) {
		int successNumber = countStatusOrderNumber(deductPlans,DeductApplicationExecutionStatus.SUCCESS);
		int abandonNumber = countStatusOrderNumber(deductPlans,DeductApplicationExecutionStatus.ABANDON);
		int processingNumber = countStatusOrderNumber(deductPlans,DeductApplicationExecutionStatus.PROCESSING);
		
		if( abandonNumber == deductPlans.size()){
			return  DeductApplicationExecutionStatus.ABANDON;
		}
		else if (successNumber > 0) {
			return DeductApplicationExecutionStatus.SUCCESS;
		}
		else if(processingNumber > 0  ){
			return  DeductApplicationExecutionStatus.PROCESSING;
		}
		else return  DeductApplicationExecutionStatus.FAIL;
	}

	
	private int countStatusOrderNumber(List<DeductPlan> deductPlans,DeductApplicationExecutionStatus status) {
		int number = 0;
		for (DeductPlan deductPlan : deductPlans) {
			if (deductPlan.getExecutionStatus() == status) {
				number++;
			}
		}
		return number;
	}
	
	@Override
	public  BigDecimal calcDeductSuccessAmount(List<QueryStatusResult> results) {
		BigDecimal successAmount = BigDecimal.ZERO;
		for (QueryStatusResult resResult : results) {
			if (resResult.getBusinessStatus().ordinal() == SUCCESS) {
				successAmount = successAmount.add(resResult.getTransactionAmount());
			}
		}
		return successAmount;
	}

	@Override
	public List<String> getDeductApplicationUuidInProcessing() {
		String queryString =  "select deduct_application_uuid from t_deduct_application where create_time <:createTime and execution_status =:executionStatus and transaction_recipient =:transactionRecipient limit 300";
		Map<String ,Object> params = new HashMap<String ,Object>();
		Calendar nowTime = Calendar.getInstance();
		nowTime.add(Calendar.MINUTE, -30);
		params.put("createTime", DateUtils.format(nowTime.getTime(), "yyyy-MM-dd HH:mm:ss"));
		params.put("executionStatus", DeductApplicationExecutionStatus.PROCESSING.ordinal());
		params.put("transactionRecipient", TransactionRecipient.OPPOSITE.ordinal());
		return this.genericDaoSupport.queryForSingleColumnList(queryString, params, String.class);
	}
	
	@Override
	public List<String>  getDeductApplicationUuidInProcessingAndSourceTypeNotRepaymentOrder(){
		String queryString =  "select deduct_application_uuid from t_deduct_application where execution_status =:executionStatus and transaction_recipient =:transactionRecipient "
				+ " and source_type!=:repaymentOrderSourceType limit 300";
		Map<String ,Object> params = new HashMap<String ,Object>();
		params.put("executionStatus", DeductApplicationExecutionStatus.PROCESSING.ordinal());
		params.put("transactionRecipient", TransactionRecipient.OPPOSITE.ordinal());
		params.put("repaymentOrderSourceType", SourceType.REPAYMENTORDER.ordinal());
		return this.genericDaoSupport.queryForSingleColumnList(queryString, params, String.class);
	}


	@Override
	public void updateDeductApplication(String deductApplicationUuid,DeductApplicationExecutionStatus deductApplicationExecutionStatus,
			BigDecimal successAmount, String executionRemark) {
		String queryString  = " UPDATE DeductApplication set executionStatus =:executionStatus,actualDeductTotalAmount =:successAmount, lastModifiedTime =:lastModifiedTime,completeTime =:completeTime, executionRemark =:executionRemark where deductApplicationUuid =:deductAapplicationUuid ";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("executionStatus", deductApplicationExecutionStatus);
		params.put("successAmount", successAmount);
		params.put("executionRemark", executionRemark);
		params.put("lastModifiedTime", new Date());
		params.put("completeTime", new Date());
		params.put("deductAapplicationUuid",deductApplicationUuid);
		this.genericDaoSupport.executeHQL(queryString, params);
	}

	@Override
	public List<TradeSchedule> handleSingleQueryResult(String deductApplicationUuid) {
		Result result = jpmorganApiHelper.queryTradeSchedulesStatus(null, deductApplicationUuid);

		ReDeductDataPackage reDeductDataPackage = handleSingleQueryResultString(deductApplicationUuid, result);
		
		//如果需要重发返回tradeSchedule
		return  handlerFailureDeductPlan(reDeductDataPackage);
	}
	
	private List<TradeSchedule> handlerFailureDeductPlan(ReDeductDataPackage reDeductDataPackage) {
		
		List<TradeSchedule> tradeSchedules = new ArrayList<TradeSchedule>();
 		if(reDeductDataPackage.isReDeduct() == true){
		
		   tradeSchedules.add(convertToTradeSchedule(reDeductDataPackage.getNeedReDeductPlan()));
		   return tradeSchedules;
		}
 		return Collections.EMPTY_LIST;
	}
	




	private TradeSchedule convertToTradeSchedule(DeductPlan deductPlan) {
		
		
		BigDecimal plannedAmount = deductPlan.getPlannedTotalAmount();
		String destinationAccountName = deductPlan.getCpBankAccountHolder();
		String destinationAccountNo = deductPlan.getCpBankCardNo();
		
		String destinationAccountAppendix = assembleDestinationAccountAppendixMap(deductPlan.getCpIdNumber(), deductPlan.getMobile());
		String destinationBankInfo = assembleDestinationBankInfoMap(deductPlan.getCpBankCode(), deductPlan.getCpBankProvince(), deductPlan.getCpBankCity(), deductPlan.getCpBankName());
		
		String outlierTransactionUuid = deductPlan.getDeductPlanUuid();
		String fstPaymentChannelUuid = deductPlan.getPaymentChannelUuid();
		
		String batchUuid = deductPlan.getDeductApplicationUuid();
		
	    String fstGatewayRouterInfo = getFstGateWayRouterInfo(null,deductPlan.getCpBankCode(), deductPlan.getPgClearingAccount());
		
	    return new TradeSchedule(com.zufangbao.gluon.api.jpmorgan.enums.AccountSide.DEBIT, destinationAccountName, destinationAccountNo, destinationAccountAppendix, destinationBankInfo, "", outlierTransactionUuid, "", fstPaymentChannelUuid, null, plannedAmount, batchUuid, null, null, null, fstGatewayRouterInfo);
	}



	
	@Override
	public ReDeductDataPackage handleSingleQueryResultString(String deductApplicationUuid,
			Result result) {
		
		ReDeductDataPackage reDeductPackage = new ReDeductDataPackage();
		if (!result.isValid()) {
			String resultStr = JsonUtils.toJsonString(result);
			logger.info("#扣款结果查询，通讯失败，扣款申请号［" + deductApplicationUuid + "］,结果［" + resultStr + "］");
			return null;
		}
		Result responseResult = parseResult(result);
		if (responseResult != null && responseResult.isValid()) {
			String queryResultStr = String.valueOf(responseResult.get("queryResult"));
			List<QueryStatusResult> queryStatusResults = JSON.parseArray(queryResultStr, QueryStatusResult.class);
			if (queryStatusResults == null) {
				logger.info("#扣款结果查询，结果解析失败［" + queryResultStr + "］");
				return null;
			}
			deductPlanHandler.updateDeductPlanStatusByQueryResult(deductApplicationUuid,queryStatusResults);
			//组装重发包
		    reDeductPackage = deductPlanHandler.castRedeductPackage(deductApplicationUuid, queryStatusResults);
			
			List<DeductPlan> deductPlans = deductPlanService.getDeductPlanByDeductApplicationUuid(deductApplicationUuid);
			
			DeductApplicationExecutionStatus deductApplicationExecutionStatus = evaluatedDeductApplicationStatusBy(deductPlans,reDeductPackage);
			//扣款结束变更扣款单状态，更新资产扣款状态
			updateDeductApplicationNotInporcessing(deductApplicationUuid, queryStatusResults, deductPlans,deductApplicationExecutionStatus);
			//扣款单最终失败解锁
			unlockRepaymentPlanWhileTheDeductApplicationIdFail(deductApplicationUuid, deductApplicationExecutionStatus);
		}
		return reDeductPackage; 
		
	}
	private void unlockRepaymentPlanWhileTheDeductApplicationIdFail(String deductApplicationUuid,
			DeductApplicationExecutionStatus deductApplicationExecutionStatus) {
		if(deductApplicationExecutionStatus == DeductApplicationExecutionStatus.FAIL)
		{	
			unlockRepaymentPlans(deductApplicationUuid);
		}
	}

	private void updateDeductApplicationNotInporcessing(String deductApplicationUuid,
			List<QueryStatusResult> queryStatusResults, List<DeductPlan> deductPlans,
			DeductApplicationExecutionStatus deductApplicationExecutionStatus) {
		if(deductApplicationExecutionStatus != DeductApplicationExecutionStatus.PROCESSING){
			String applicationExecutionRemark = getBusinessResultMessage(deductApplicationExecutionStatus, deductPlans);
			
			BigDecimal successAmount = calcDeductSuccessAmount(queryStatusResults);
			
			updateDeductApplication(deductApplicationUuid,deductApplicationExecutionStatus, successAmount, applicationExecutionRemark);
			
			updateDeducttionStatusAndExecutionStatusWhilePreDeductFail(deductApplicationUuid, deductPlans,
					deductApplicationExecutionStatus);
			
			
		}
	}

	private void updateDeducttionStatusAndExecutionStatusWhilePreDeductFail(String deductApplicationUuid,
			List<DeductPlan> deductPlans, DeductApplicationExecutionStatus deductApplicationExecutionStatus) {
		
		DeductApplication deductApplication = deductApplicationService.getDeductApplicationByDeductApplicationUuid(deductApplicationUuid);
		List<String> repaymentCodes = deductApplication.getRepaymentPlanCodeListJsonString();
		DeductionStatus deductionStatus =  convertDeductApplicationExecutionStatusToDeductionStatus(deductApplicationExecutionStatus);
		//udpate asset_set deduction_status
		repaymentPlanService.updateAssetSetDeductionStatusByAssetSetUuid(repaymentCodes, deductionStatus);
		//update asset_set excution_status( if preDeduct fail excutionStatus is no_operation)
		if(deductPlans.get(0).getRepaymentType() == RepaymentType.ADVANCE &&  deductApplicationExecutionStatus != DeductApplicationExecutionStatus.SUCCESS){
			repaymentPlanService.updateAssetSetEXecutionStatusByAssetSetUuids(repaymentCodes,ExecutingStatus.UNEXECUTED);
		}
	}

	private DeductionStatus convertDeductApplicationExecutionStatusToDeductionStatus(DeductApplicationExecutionStatus deductApplicationExecutionStatus) {
		
		if(deductApplicationExecutionStatus == DeductApplicationExecutionStatus.PROCESSING ||
				deductApplicationExecutionStatus == DeductApplicationExecutionStatus.PART_OF_SUCCESS){
			return DeductionStatus.OPPOSITE_PROCESSING;
		}
		if(deductApplicationExecutionStatus == DeductApplicationExecutionStatus.SUCCESS){
			return DeductionStatus.SUCCESS;
		}
		if(deductApplicationExecutionStatus == DeductApplicationExecutionStatus.FAIL){
			return DeductionStatus.FAIL;
		}
		if(deductApplicationExecutionStatus == DeductApplicationExecutionStatus.ABANDON){
			return DeductionStatus.FAIL;
		}
		if(deductApplicationExecutionStatus == DeductApplicationExecutionStatus.ABNORMAL){
			return DeductionStatus.FAIL;
		}
		return DeductionStatus.FAIL;
		
	}

	public void unlockRepaymentPlans(String deductApplicationUuid) {
		DeductApplication deductApplication = deductApplicationService.getDeductApplicationByDeductApplicationUuid(deductApplicationUuid);
		if(deductApplication.isRepaymentOrderSourceType()){
			//还款订单没有使用扣款锁
			return;
		}
		List<DeductApplicationRepaymentDetail> repaymentDetails = deductApplicationDetailService.getRepaymentDetailsBy(deductApplicationUuid);
			
				for(DeductApplicationRepaymentDetail repaymentDetail:repaymentDetails){
					if(!repaymentPlanService.queryActiveDeductApplicationUuidBySql(repaymentDetail.getAssetSetUuid()).equals(deductApplicationUuid)){
						throw new ApiException(ApiResponseCode.DEDUCT_CONCURRENT_ERROR);
					}
					repaymentPlanService.updateActiveDeductApplicationUuidBySql(repaymentDetail.getAssetSetUuid(), AssetSet.EMPTY_UUID, deductApplicationUuid);
					String activeDeductApplicationUuid = repaymentPlanService.queryActiveDeductApplicationUuidBySql(repaymentDetail.getAssetSetUuid());
					if (!activeDeductApplicationUuid.equals(AssetSet.EMPTY_UUID)) {
						throw new ApiException(ApiResponseCode.DEDUCT_CONCURRENT_ERROR);
					}
				}
				
	}

	private String getBusinessResultMessage(DeductApplicationExecutionStatus deductApplicationExecutionStatus, List<DeductPlan> deductPlans) {
		if(deductApplicationExecutionStatus == DeductApplicationExecutionStatus.SUCCESS){
			return "交易成功";
		}
		if(deductApplicationExecutionStatus == DeductApplicationExecutionStatus.PROCESSING){
			return "处理中";
		}
		
		StringBuffer errorMessage = new StringBuffer();
		for(DeductPlan deductPlan : deductPlans){
			if(deductPlan.getExecutionStatus() ==  DeductApplicationExecutionStatus.FAIL){
				errorMessage.append(DeductGatewayMapSpec.DEDUCT_GATEWAY_NAME_MAP.get(deductPlan.getPaymentGateway())+":"+deductPlan.getExecutionRemark()+" ");
			}
		}
		return errorMessage.toString();
	}
	

	private Result parseResult(Result result) {
		String queryResString = String.valueOf(result.get(HttpClientUtils.DATA_RESPONSE_PACKET));
		Result responseResult = JsonUtils.parse(queryResString, Result.class);
		return responseResult;
	}

	@Override
	public void handleSingleQueryResultForProducts(String deductApplicationUuid) {
		Result result = jpmorganApiHelper.queryTradeSchedulesStatus(null, deductApplicationUuid);
		handleSingleQueryResultStringForProjects(deductApplicationUuid, result);
	}
	@Override
	public void handleSingleQueryResultStringForProjects(String deductApplicationUuid,
			Result result) {

		if (!result.isValid()) {
			String resultStr = JsonUtils.toJsonString(result);
			logger.info("#扣款结果查询，通讯失败，扣款申请号［" + deductApplicationUuid + "］,结果［" + resultStr + "］");
			return;
		}
		Result responseResult = parseResult(result);
		if (responseResult != null && responseResult.isValid()) {
			String queryResultStr = String.valueOf(responseResult.get("queryResult"));
			List<QueryStatusResult> queryStatusResults = JSON.parseArray(queryResultStr, QueryStatusResult.class);
			if (queryStatusResults == null) {
				logger.info("#扣款结果查询，结果解析失败［" + queryResultStr + "］");
				return;
			}
			//deductPlanHandler.updateDeductPlanStatusByQueryResult(deductApplicationUuid,queryStatusResults);
			for (QueryStatusResult queryStatusResult:queryStatusResults) {
				deductPlanHandler.updateDeductPlanStatusByQueryResultV2(deductApplicationUuid, queryStatusResult);
			}

			DeductApplicationExecutionStatus executionStatus = updateDeductApplicationAccordingPartDeductPlan(deductApplicationUuid);

			if (executionStatus == DeductApplicationExecutionStatus.SUCCESS || executionStatus == DeductApplicationExecutionStatus.FAIL || executionStatus == DeductApplicationExecutionStatus.ABANDON ){

				pushJobToCitigroupModifyAsseset(deductApplicationUuid);

				execSingleNotifyFordeductApplication(deductApplicationUuid);
			}

		}

	}
	@Override
	public void updateDeductApplicationNotInporcessingForProjects(String deductApplicationUuid,
			List<QueryStatusResult> queryStatusResults, List<DeductPlan> deductPlans,
			DeductApplicationExecutionStatus deductApplicationExecutionStatus) {
		if(deductApplicationExecutionStatus != DeductApplicationExecutionStatus.PROCESSING){
			String applicationExecutionRemark = getBusinessResultMessage(deductApplicationExecutionStatus, deductPlans);
			
			BigDecimal successAmount = calcDeductSuccessAmount(queryStatusResults);
			
			updateDeductApplication(deductApplicationUuid,deductApplicationExecutionStatus, successAmount, applicationExecutionRemark);
			
			updateDeducttionStatusAndExecutionStatusWhilePreDeductFail(deductApplicationUuid, deductPlans,
					deductApplicationExecutionStatus);
			
			
		}
	}

	/**
	 * 查询放款结果（回调通知用）
	 * 结构：
	 * paidDetails : [
	 * 		{
	 * 			"Result" : "", //放款结果
	 * 			"BankSerialNo" : "", //银行流水号列表
	 * 			"ActExcutedTime" : "", //实际完成时间
	 * 		}
	 * ]
	 */
	
	public boolean dealCacheDeductPlansAndPushNotifyJob(DeductApplication deductApplication, DeductNotifyJobServer deductSingleCallBackNotifyJobServer,String groupName) {
		
		DeductApplicationExecutionStatus executionStatus = DeductApplicationExecutionStatus.FAIL;
		
		BigDecimal actualDeductTotalAmount = new BigDecimal(0);
		
		StringBuffer executionRemark = new StringBuffer();
		
		String deductApplicationUuid = deductApplication.getDeductApplicationUuid();
		
		List<String> deductStringList =  deductPlanCacheService.popAllDeductPlanUuidResult(deductApplicationUuid);
		
		if (CollectionUtils.isEmpty(deductStringList)) {
			
			return false;
			
		}
		
		List<DeductPlan> deductPlanList = deductStringList.stream().map(deductPlan -> JSON.parseObject(deductPlan, DeductPlan.class)).collect(Collectors.toList());
		
		boolean isMultiChannel = isMultiChannel(deductApplication, deductPlanList);//deductApplicationService.isMultiChannelMode(deductApplicationUuid);//transactionUuidList.size()==1?false:true;

		List<DeductPlan> successdeductPlans = deductPlanList.stream().filter(d -> d.getExecutionStatus() == DeductApplicationExecutionStatus.SUCCESS).collect(Collectors.toList());
		
		int  deductPlanSizeCountFromDb = deductPlanService.getDeductPlanCountDeductApplicationUuid(deductApplicationUuid);
		
		//判断任务是否完成

		if(deductPlanService.getDeductPlanByDeductApplicationUuidAndInprocessing(deductApplicationUuid).size()!=0) {

			return false;
		}

		//拆单
		if (!isMultiChannel) {
			
			//是否任务已完成

			if (CollectionUtils.isNotEmpty(successdeductPlans)) {
				
				executionStatus = successdeductPlans.size()==deductPlanList.size()?DeductApplicationExecutionStatus.SUCCESS:DeductApplicationExecutionStatus.PART_OF_SUCCESS;
				for (DeductPlan deductPlan: successdeductPlans) {
					actualDeductTotalAmount = 	actualDeductTotalAmount.add(deductPlan.getActualTotalAmount());
					
					executionRemark.append(DeductApplicationExecutionStatus.SUCCESS==deductPlan.getExecutionStatus()?"":deductPlan.getExecutionRemark());
					
				}
				
			}
			
		}else{
			//多通道

			if (CollectionUtils.isNotEmpty(successdeductPlans) && successdeductPlans.size()==1) {
				executionStatus = DeductApplicationExecutionStatus.SUCCESS;
			
				actualDeductTotalAmount = deductApplication.getActualDeductTotalAmount();
				
			}else{
				
				deductPlanList.stream().map(deductPlan -> executionRemark.append(deductPlan.getExecutionRemark()));
			
			}
			
		}
		
		deductApplication.setActualDeductTotalAmount(actualDeductTotalAmount);
		//deductApplication.setCompleteTime(new Date());
		
		deductApplication.setExecutionStatus(executionStatus);

		if (StringUtils.isNotEmpty(executionRemark.toString())){
			deductApplication.setExecutionRemark(executionRemark.toString());
		}

		return true;
		
		
//		NotifyModel model = new NotifyModel();
//		model.setRequestNo(deductApplication.getRequestNo());
//		model.setExecutionRemark(deductApplication.getExecutionRemark());
//		model.setAmount(null==deductApplication.getActualDeductTotalAmount()?null:deductApplication.getActualDeductTotalAmount().toString());
//		model.setUniqueId( deductApplication.getContractUniqueId());
//		model.setDeductId(deductApplication.getDeductId());
//		model.setExecutionStatus(deductApplication.getExecutionStatus().getOrdinal());
//		model.setFinancialContractUuid(deductApplication.getFinancialContractUuid());
//		return model;
	}
	
	private boolean isMultiChannel(DeductApplication deductApplication,List<DeductPlan> successdeductPlans) {
		
		for (DeductPlan deductPlan : successdeductPlans) {
			
			if(deductApplication.getPlannedDeductTotalAmount().compareTo(deductPlan.getPlannedTotalAmount()) == 0) {
				return true;
			}
			
		}
		return false;
	}
	
	
	public NotifyModel genNotifyModel(DeductApplication deductApplication){
		
		NotifyModel model = new NotifyModel();
		model.setRequestNo(deductApplication.getRequestNo());
		model.setExecutionRemark(deductApplication.getExecutionRemark());
		model.setAmount(null==deductApplication.getActualDeductTotalAmount()?null:deductApplication.getActualDeductTotalAmount().toString());
		model.setUniqueId( deductApplication.getContractUniqueId());
		model.setDeductId(deductApplication.getDeductId());
		model.setExecutionStatus(deductApplication.getExecutionStatus().getOrdinal());
		model.setFinancialContractUuid(deductApplication.getFinancialContractUuid());
		model.setPaidNoticInfos(new HashMap<String,Object>());
		model.setLastModifiedTime(deductApplication.getLastModifiedTime());
		return model;
		
	}
	
	
	private void buildNotifyJobAndPush(DeductApplication deductApplication,DeductNotifyJobServer deductSingleCallBackNotifyJobServer,String groupName){
		
//		String context = genContext(deductApplication);
		
		HashMap<String, String> requestParameters = new HashMap<String,String>(){{
			
//			put("context", context);
			
			put("requestId", UUID.randomUUID().toString());
			put("referenceId", deductApplication.getRequestNo());
			put("orderNo",deductApplication.getDeductId());
			put("amount",null==deductApplication.getActualDeductTotalAmount()?null:deductApplication.getActualDeductTotalAmount().toString());
			put("status",String.valueOf(deductApplication.getExecutionStatus().getOrdinal()));
			put("comment",deductApplication.getExecutionRemark());
			put("paidNoticInfos",null);
			
			
		}};
				
		NotifyApplication job = new NotifyApplication();
		
		job.setBusinessId(deductApplication.getDeductApplicationUuid());
		
		job.setRequestParameters(requestParameters);
		
		job.setHttpJobUuid(UUID.randomUUID().toString());
		
		job.setRequestUrl(deductApplication.getNotifyUrl());
		
		job.setRequestMethod(NotifyApplication.POST_METHOD);
		
		job.setDelaySecond(0);
		
		job.setRetryTimes(0);
		
		job.setPriority(PriorityEnum.FIRST);
		
		job.setGroupName(groupName);
		
		deductSingleCallBackNotifyJobServer.pushJob(job);
	}
	
	
	private String genContext(DeductApplication deductApplication){
		Map<String, Object> params = new HashMap<>();
		params.put("requestId", UUID.randomUUID().toString());
		params.put("referenceId", deductApplication.getRequestNo());
		params.put("orderNo",deductApplication.getDeductId());
		params.put("amount",deductApplication.getActualDeductTotalAmount());
		params.put("status",deductApplication.getExecutionStatus());
		params.put("comment",deductApplication.getExecutionRemark());
		params.put("paidNoticInfos",null);
		
		return JSON.toJSONString(params, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat);
	}

	private String genContextNotifyModel(NotifyModel model){
		Map<String, Object> params = new HashMap<>();
		params.put("requestId", UUID.randomUUID().toString());
		params.put("referenceId", model.getRequestNo());
		params.put("orderNo",model.getDeductId());
		params.put("amount",model.getAmount());
		params.put("status",model.getExecutionStatus());
		params.put("comment",model.getExecutionRemark());
		params.put("lastModifiedTime",model.getLastModifiedTime());
		params.put("paidNoticInfos",model.getPaidNoticInfos());

		return JSON.toJSONString(params, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat);
	}

	@Override
	public List<String> getWaitingNoticeDeductApplication(String financialContractUuid, int pageSize,Date queryStartDate) {
		String queryUuidsSql = "SELECT deduct_application_uuid "
				+ "FROM t_deduct_application "
				+ "WHERE financial_contract_uuid =:financialContractUuid and "
				+"execution_status NOT IN(:executionStatusList)"
				+ " AND create_time >=:queryStartDate"
				+ " AND actual_notify_number < plan_notify_number "
				+ " AND notify_url IS NOT NULL AND (batch_deduct_application_uuid is NULL or batch_deduct_application_uuid ='') AND executed_count=total_count LIMIT :pageSize";

		Map<String, Object> params = new HashMap<String, Object>();
		List<Integer> finishedExecutionStatusList = new ArrayList<Integer>();
		finishedExecutionStatusList.add(DeductApplicationExecutionStatus.ABNORMAL.ordinal());
//		finishedExecutionStatusList.add(DeductApplicationExecutionStatus.PROCESSING.ordinal());
		finishedExecutionStatusList.add(DeductApplicationExecutionStatus.CREATE.ordinal());
		params.put("financialContractUuid", financialContractUuid);
		params.put("queryStartDate", queryStartDate);
		params.put("executionStatusList", finishedExecutionStatusList);
		params.put("pageSize", pageSize);
		return this.genericDaoSupport.queryForSingleColumnList(queryUuidsSql, params, String.class);
	}
	
	@Override
	public List<String> getWaitingNoticeDeductApplication(int pageSize, Date queryStartDate) {
		String queryUuidsSql = "SELECT deduct_application_uuid "
				+ "FROM t_deduct_application "
				+ "WHERE execution_status IN(:executionStatusList)"
				+ " AND create_time >=:queryStartDate"
				+ " AND actual_notify_number < plan_notify_number "
				+ " AND notify_url IS NOT NULL AND (batch_deduct_application_uuid is NULL or batch_deduct_application_uuid ='') "
				+ " LIMIT :pageSize";

		Map<String, Object> params = new HashMap<String, Object>();
		List<Integer> finishedExecutionStatusList = new ArrayList<Integer>();
		finishedExecutionStatusList.add(DeductApplicationExecutionStatus.ABANDON.ordinal());
		finishedExecutionStatusList.add(DeductApplicationExecutionStatus.SUCCESS.ordinal());
		finishedExecutionStatusList.add(DeductApplicationExecutionStatus.FAIL.ordinal());
		
		//还款订单模式：PROCESSING+PART_OF_SUCCESS
		//finishedExecutionStatusList.add(DeductApplicationExecutionStatus.PROCESSING.ordinal());
		finishedExecutionStatusList.add(DeductApplicationExecutionStatus.PART_OF_SUCCESS.ordinal());
		params.put("queryStartDate", queryStartDate);
		params.put("executionStatusList", finishedExecutionStatusList);
		params.put("pageSize", pageSize);
		return this.genericDaoSupport.queryForSingleColumnList(queryUuidsSql, params, String.class);
	}
	
	
	
	
	@Override
	public void doFillInfoByDeductApplicationUuid(String deductApplicationUuid) {
		
		DeductApplication deductApplication = this.deductApplicationService.getDeductApplicationByDeductApplicationUuid(deductApplicationUuid);
		if (null == deductApplication) {
			
			return;
			
		}
		
		String requestParam = deductApplication.getRequestParams();
		
		DeductRequestModel deductModel = JsonUtils.parse(requestParam, DeductRequestModel.class);
		
		boolean isSuccess = sendMessageToEarth(deductModel);
		
		DeductApplicationReceiveStatus receiveStatus = isSuccess?DeductApplicationReceiveStatus.SENDSUCCESS:DeductApplicationReceiveStatus.FAIL;
		
		String executionRemark = isSuccess?deductApplication.getExecutionRemark(): ZhonghangResponseMapSpec.RECEIVE_MESSAGE;
		
		deductApplication.setReceiveStatus(receiveStatus);
		
		deductApplication.setExecutionRemark(executionRemark);
		
		this.deductApplicationService.saveOrUpdate(deductApplication);
	}
	
	@Override
	public boolean sendMessageToEarth(Object deductModel) {
		
		boolean isSuccess  = false;
    	
    	Map<String,Object> params = new HashMap<String,Object>();
    	
    	DeductRequestModel model = (DeductRequestModel)deductModel;
    	
    	params.put(ZhonghangResponseMapSpec.DEDUCTREQUESTMODEL, model);
    	
    	String context = JSON.toJSONString(params, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat);
    	
    	Result result  = HttpClientUtils.executePostRequest(urlToCitigroupModifyAsseset, context, null);
    	
    	if (!result.isValid()) {
    		
			return isSuccess;
		
    	}
    	
    	isSuccess = (Boolean) result.get(ZhonghangResponseMapSpec.RECEIVESTATUS);
    	
    	return isSuccess;
	}

	@Override
	public DeductApplication creatDeductAppilcationByDeductRequestModel(DeductRequestModel model, String creatorName, String IpAddress, int planNotifyNumber, int planPushMorganStanleyNumber) {

		List<RepaymentDetail> repaymentDetails = JsonUtils.parseArray(model.getRepayDetailInfo(), RepaymentDetail.class);
		List<String> repaymentCodes = repaymentDetails.stream().map(fc -> fc.getRepaymentPlanNo()).collect(Collectors.toList());

		DeductApplication deductApplication = new DeductApplication(UUID.randomUUID().toString(),model.getApiCalledTime(),
	                model.getRequestNo(),model.getDeductId(),model.getDeductAmount(),model.getMobile(),
	                DeductGatewayMapSpec.DEDUCT_GATEWAY_MAP.getOrDefault(model.getGateway(), null),creatorName, repaymentCodes, IpAddress,model.getFinancialProductCode(),model.getFinancialContractUuid(),
	                model.getBatchDeductApplicationUuid(), model.getBatchDeductId(), model.getNotifyUrl(),null,DeductApplicationReceiveStatus.CREATE, planNotifyNumber, planPushMorganStanleyNumber, model.getUniqueId(),model.getContractNo(),model.getRepaymentType());
		 
		 model.setDeductApplicationUuid(deductApplication.getDeductApplicationUuid());
		 model.setCheckResponseNo(deductApplication.getCheckResponseNo());

		 deductApplicationService.save(deductApplication);

		 return deductApplication;
		
	}

	@Override
	public void pushJobToCitigroup(DeductRequestModel model, DeductNotifyJobServer deductSendMorganStanleyNotifyJobServer, String requestUrl, String groupName) {
		
		NotifyApplication job = buildNotifyApplication(model,requestUrl,groupName);
		
		deductSendMorganStanleyNotifyJobServer.pushJob(job);
		
		
	}
	
	
	private NotifyApplication buildNotifyApplication(DeductRequestModel model,String requestUrl,String groupName){
		
		NotifyApplication job = new NotifyApplication();
		
		HashMap<String, String> requestParameters = new HashMap<String,String>(){{
			put(ZhonghangResponseMapSpec.DEDUCTREQUESTMODEL, JsonUtils.toJsonString(model));
		}};
		
		job.setBusinessId(model.getDeductApplicationUuid());
		
		job.setRequestParameters(requestParameters);
		
		job.setHttpJobUuid(UUID.randomUUID().toString());
		
		job.setRequestUrl(requestUrl);
		
		job.setRequestMethod(NotifyApplication.POST_METHOD);
		
		job.setDelaySecond(0);
		
		job.setRetryTimes(0);
		
		job.setPriority(PriorityEnum.FIRST);
		
		job.setGroupName(groupName);;
		
		return job;
		
	}

	@Override
	public DeductApplicationExecutionStatus updateDeductApplicationAccordingPartDeductPlan(String deductApplicationUuid) {

		List<DeductPlan> deductPlans = deductPlanService.getDeductPlanByDeductApplicationUuid(deductApplicationUuid);

		List<DeductApplicationExecutionStatus> executionStatusList = deductPlans.stream().map(s -> s.getExecutionStatus()).collect(Collectors.toList());
		DeductApplicationExecutionStatus deductApplicationExecutionStatus = doEvaluateFinalStatus(executionStatusList);
		if (deductPlans.get(0).getDeductMode() == DeductMode.MULTI_CHANNEL_MODE && deductApplicationExecutionStatus == DeductApplicationExecutionStatus.PART_OF_SUCCESS){
			deductApplicationExecutionStatus = DeductApplicationExecutionStatus.SUCCESS;
		}

		if(deductApplicationExecutionStatus == null || deductApplicationExecutionStatus == DeductApplicationExecutionStatus.PROCESSING) {
			return deductApplicationExecutionStatus;
		}

		String applicationExecutionRemark = getBusinessResultMessage(deductApplicationExecutionStatus, deductPlans);

		BigDecimal successAmount = BigDecimal.ZERO;
		for (DeductPlan deductPlan : deductPlans) {
			if (deductPlan.getExecutionStatus() == DeductApplicationExecutionStatus.SUCCESS) {
				successAmount = successAmount.add(deductPlan.getActualTotalAmount());
			}
		}
		
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("deductApplicationUuid", deductApplicationUuid);
		parms.put("successAmount", successAmount);
		parms.put("executionRemark", applicationExecutionRemark);
		parms.put("executionStatus", deductApplicationExecutionStatus.ordinal());

			try {
				String preAccessVersion = getCurrentAccessVersion(deductApplicationUuid);
				parms.put("preAccessVersion", preAccessVersion);
				parms.put("accessVersion", UUID.randomUUID().toString());
				
				String executeSql = "update t_deduct_application set version = :accessVersion, actual_deduct_total_amount =:successAmount,executed_count = executed_count+1 , last_modified_time = NOW(),complete_time = NOW() , execution_status =:executionStatus, execution_remark =:executionRemark where deduct_application_uuid =:deductApplicationUuid and version =:preAccessVersion";
				genericDaoSupport.executeSQL(executeSql, parms);
				
				String validateSql = "select id from t_deduct_application where deduct_application_uuid =:deductApplicationUuid and version =:accessVersion";
				int updatedRows = genericDaoSupport.queryForInt(validateSql, parms);
				
				if (0 >= updatedRows) {
					logger.info(GloableLogSpec.AuditLogHeaderSpec()  + "nothing DeductApplication update and deductApplicationUuid:"+deductApplicationUuid);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		return deductApplicationExecutionStatus;

	}
	
	private String getCurrentAccessVersion(String deductApplicationUuid) {
		
		List<String> resultList = genericDaoSupport.queryForSingleColumnList("SELECT version FROM t_deduct_application WHERE deduct_application_uuid =:deductApplicationUuid",
				"deductApplicationUuid", deductApplicationUuid, String.class);

		if (CollectionUtils.isEmpty(resultList)) {
			return StringUtils.EMPTY;
		}
		return resultList.get(0);
		
	}

	@Override
	public boolean fillInfoIntoDeductCommandRequestModel(DeductCommandRequestModel commandModel) {
		List<RepaymentDetail> details=commandModel.getRepaymentDetailArray();
		if(CollectionUtils.isEmpty(details)){
			return false;
		}
		Contract contract = contractApiHandler.getContractBy(commandModel.getUniqueId(), commandModel.getContractNo());
		if(null==contract) {
			commandModel.setCheckFailedMsg("合同编号错误");
			return false;
		}
		for (RepaymentDetail detail : details) {
			if(!checkRepaymentDetail(commandModel, contract, detail)){
				return false;
			}
		}
		if(!checkAssetSetUniqueness(commandModel, details)) {
			return false;
		}
		
		commandModel.setRepaymentDetailList(details);
		commandModel.setRepaymentDetails(JsonUtils.toJsonString(details));
		return true;
	}

	@Override
	public DeductApplicationExecutionStatus doEvaluateFinalStatus(List<DeductApplicationExecutionStatus> statusList) {
		if (CollectionUtils.isEmpty(statusList)){
			return null;
		}

		DeductApplicationExecutionStatus result;

		Map<DeductApplicationExecutionStatus,Integer> statusCountTable = new HashMap<>();
		for(DeductApplicationExecutionStatus executionStatus : statusList){
			Integer StatusCount=statusCountTable.get(executionStatus);
			if(StatusCount==null) {
				StatusCount = 0;
			}
			StatusCount++;
			statusCountTable.put(executionStatus,StatusCount);
		}
		int successCount=statusCountTable.getOrDefault(DeductApplicationExecutionStatus.SUCCESS, 0);
		int failedCount=statusCountTable.getOrDefault(DeductApplicationExecutionStatus.FAIL,0);
		int processingCount=statusCountTable.getOrDefault(DeductApplicationExecutionStatus.PROCESSING, 0);
		int abandonCount=statusCountTable.getOrDefault(DeductApplicationExecutionStatus.ABANDON, 0);

		if(processingCount>0)
			result=DeductApplicationExecutionStatus.PROCESSING;
		else if(failedCount>0&&successCount==0)
			result=DeductApplicationExecutionStatus.FAIL;
		else if(successCount>0&&successCount<statusList.size())
			result= DeductApplicationExecutionStatus.PART_OF_SUCCESS;
		else if(successCount>0&&successCount==statusList.size())
			result=DeductApplicationExecutionStatus.SUCCESS;
		else if(abandonCount==statusList.size())
			result=DeductApplicationExecutionStatus.ABANDON;
		else
			result=DeductApplicationExecutionStatus.ABNORMAL;

		return  result;
	}

	@Override
	public void execSingleNotifyFordeductApplication(String deductApplicationUuid) {
		IDeductAsyncNotifyHandler iDeductAsyncNotifyHandler = IDeductAsyncNotifyHandler
				.getNotifyHandler(notifyType);
		iDeductAsyncNotifyHandler.processingdeductCallback(deductApplicationUuid);
	}

	@Override
	public void pushJobToCitigroupModifyAsseset(String deductApplicationUuid) {

		DeductApplicationSqlModel deductApplication = deductApplicationService.getDeductApplicationSqlModelByDeducApplicationUuid(deductApplicationUuid);
		DeductionStatus deductionStatus =  convertDeductApplicationExecutionStatusToDeductionStatus(DeductApplicationExecutionStatus.fromOrdinal(deductApplication.getExecutionStatus()));
		if (deductApplication.getSourceType() == 3){
			return;
		}
		AssetSetModifyModel assetSetModifyModel = new AssetSetModifyModel();
		assetSetModifyModel.setRepaymentPlanCodeList(deductApplication.getRepaymentPlanCodeList());
		assetSetModifyModel.setRepaymentType(deductApplication.getRepaymentType());
		assetSetModifyModel.setDeductionStatus(deductionStatus.ordinal());
		assetSetModifyModel.setDeductApplicationUuid(deductApplication.getDeductApplicationUuid());
		HashMap<String, String> requestParameters = new HashMap<String, String>() {{
			put(ZhonghangResponseMapSpec.ASSET_SET_MODIFY_MODE, JsonUtils.toJsonString(assetSetModifyModel));
		}};

		NotifyApplication job = new NotifyApplication();
		job.setBusinessId(assetSetModifyModel.getDeductApplicationUuid());
		job.setRequestParameters(requestParameters);
		job.setHttpJobUuid(UUID.randomUUID().toString());
		job.setRequestUrl(urlToCitigroupModifyAsseset);
		job.setRequestMethod(NotifyApplication.POST_METHOD);
		job.setDelaySecond(0);
		job.setRetryTimes(0);
		job.setPriority(PriorityEnum.FIRST);
		job.setGroupName(toCitigroupModifyAsseset);

		logger.info("start push job to citigroup modify asseset jobString:"+JSON.toJSONString(job));

		deductSendMorganStanleyNotifyJobServer.pushJob(job);


	}

	@Override
	public List<String> getDeductApplicationUuidWaittingToCitigroup() {
		String queryString =  "select deduct_application_uuid from t_deduct_application where last_modified_time <:timeOff and plan_notify_citigroup_number > actual_notify_citigroup_number and receive_status in (:receiveStatus) limit 300";
		Map<String ,Object> params = new HashMap<String ,Object>();
		Calendar nowTime = Calendar.getInstance();
		nowTime.add(Calendar.MINUTE, -30);
		params.put("timeOff", DateUtils.format(nowTime.getTime(), "yyyy-MM-dd HH:mm:ss"));

		List<Integer> receiveStatus = new ArrayList<>();
		receiveStatus.add(DeductApplicationReceiveStatus.CREATE.ordinal());
		//receiveStatus.add(DeductApplicationReceiveStatus.SENDSUCCESS.ordinal());
		receiveStatus.add(DeductApplicationReceiveStatus.TIMEOUT.ordinal());
		params.put("receiveStatus", receiveStatus);

		return this.genericDaoSupport.queryForSingleColumnList(queryString, params, String.class);
	}

	@Override
	public void updateDeductApplicationByStatus(String deductApplicationUuid, DeductApplicationExecutionStatus executionStatus,
												String message, DeductApplicationReceiveStatus receiveStatus, String financialContractUuid, TransactionRecipient transactionRecipient,
												List<String> outlierTransactionUuid, String repaymentCodes, String contractNo, String uniqueId, String customerName) {
		if (StringUtils.isEmpty(deductApplicationUuid)) {
			return;
		}

		Map<String, Object> params = new HashMap<String, Object>();

		try{
			String preAccessVersion = getCurrentAccessVersion(deductApplicationUuid);
			params.put("preAccessVersion", preAccessVersion);
			params.put("accessVersion", UUID.randomUUID().toString());
			params.put("financialContractUuid", financialContractUuid);
			params.put("deductApplicationUuid", deductApplicationUuid);
			params.put("transactionRecipient", transactionRecipient);
			params.put("totalCount", outlierTransactionUuid.size());
			params.put("executionStatus", executionStatus);
			params.put("receiveStatus", receiveStatus);
			params.put("lastModifiedTime", new Date());
			params.put("executionRemark", message);
			params.put("checkResponseNo",UUID.randomUUID().toString());
			params.put("repaymentPlanCodeList", repaymentCodes);
			params.put("customerName", customerName);

			/*String hql = "UPDATE DeductApplication set version =:accessVersion, financialContractUuid =:financialContractUuid, checkResponseNo =:checkResponseNo, repaymentPlanCodeList =:repaymentPlanCodeList, "+
					"executionStatus =:executionStatus, receiveStatus =:receiveStatus, executionRemark =:executionRemark, transactionRecipient =:transactionRecipient, "+
					"totalCount =:totalCount, lastModifiedTime =:lastModifiedTime WHERE deductApplicationUuid =:deductApplicationUuid and version =:preAccessVersion";*/

			StringBuffer hqlBuffer = new StringBuffer("UPDATE DeductApplication set version =:accessVersion, financialContractUuid =:financialContractUuid, checkResponseNo =:checkResponseNo, repaymentPlanCodeList =:repaymentPlanCodeList, ");
			hqlBuffer.append("executionStatus =:executionStatus, receiveStatus =:receiveStatus, executionRemark =:executionRemark, transactionRecipient =:transactionRecipient, totalCount =:totalCount, lastModifiedTime =:lastModifiedTime");
			if (StringUtils.isNotEmpty(contractNo)){
				params.put("contractNo", contractNo);
				hqlBuffer.append(", contractNo =:contractNo ");
			}
			if (StringUtils.isNotEmpty(uniqueId)){
				params.put("uniqueId", uniqueId);
				hqlBuffer.append(", contractUniqueId =:uniqueId ");
			}
			if (StringUtils.isNotEmpty(customerName)){
				params.put("customerName", customerName);
				hqlBuffer.append(", customerName =:customerName ");
			}
			hqlBuffer.append(" WHERE deductApplicationUuid =:deductApplicationUuid and version =:preAccessVersion");
			this.genericDaoSupport.executeHQL(hqlBuffer.toString(),params );

			String validateSql = "select id from t_deduct_application where deduct_application_uuid =:deductApplicationUuid and version =:accessVersion";
			int updatedRows = genericDaoSupport.queryForInt(validateSql, params);

			if (0 >= updatedRows) {
				logger.info(GloableLogSpec.AuditLogHeaderSpec()  + "nothing DeductApplication update and deductApplicationUuid:"+deductApplicationUuid);
			}

			for(String deducePlanUuid:outlierTransactionUuid){

				params.put("deductPlanUuid",deducePlanUuid);

				executeHqlForDeductPlan(params);

			}

		}catch (Exception e){
			e.printStackTrace();
		}


	}

	@Override
	public void updatDeductApplicationToCitigroup(DeductApplication deductApplication, int receiveStatus, String newCheckResponseNo) {
		if (deductApplication == null){
			return;
		}

		Map<String, Object> params = new HashMap<String, Object>();

		try{
			String preAccessVersion = getCurrentAccessVersion(deductApplication.getDeductApplicationUuid());
			params.put("preAccessVersion", preAccessVersion);
			params.put("accessVersion", UUID.randomUUID().toString());
			params.put("deductApplicationUuid", deductApplication.getDeductApplicationUuid());
			params.put("receiveStatus", receiveStatus);
			params.put("newCheckResponseNo", newCheckResponseNo);

			StringBuffer sql = new StringBuffer("UPDATE t_deduct_application  set version = :accessVersion,receive_status =:receiveStatus, last_modified_time =NOW() ");

			if (receiveStatus == DeductApplicationReceiveStatus.CREATE.ordinal()){
				sql.append(",actual_notify_citigroup_number = actual_notify_citigroup_number+1 , check_response_no =:newCheckResponseNo");
			}else if (receiveStatus == DeductApplicationReceiveStatus.TIMEOUT.ordinal() &&
					deductApplication.getActualNotifyCitigroupNumber() >= deductApplication.getPlanNotifyCitigroupNumber()){

				sql.append(",execution_status=:executionStatus, execution_remark=:executionRemark");
				params.put("executionStatus", DeductApplicationExecutionStatus.FAIL.ordinal());
				params.put("executionRemark", "扣款本端受理失败!");

			}

			sql.append(" WHERE deduct_application_uuid = :deductApplicationUuid and version = :preAccessVersion ");

			this.genericDaoSupport.executeSQL(sql.toString(), params);

			String validateSql = "select id from t_deduct_application where deduct_application_uuid =:deductApplicationUuid and version =:accessVersion";
			int updatedRows = genericDaoSupport.queryForInt(validateSql, params);

			if (0 >= updatedRows) {
				logger.info(GloableLogSpec.AuditLogHeaderSpec()  + "nothing DeductApplication update and deductApplicationUuid:"+deductApplication.getDeductApplicationUuid());
			}

		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void updatDeductApplicationCheckResponseNo(String deductApplicationUuid, String newCheckResponseNo, String oldCheckResponseNo) {
		if (StringUtils.isEmpty(deductApplicationUuid) || StringUtils.isEmpty(newCheckResponseNo) || StringUtils.isEmpty(oldCheckResponseNo)){
			return;
		}

		Map<String, Object> params = new HashMap<String, Object>();

		try{
			String preAccessVersion = getCurrentAccessVersion(deductApplicationUuid);
			params.put("preAccessVersion", preAccessVersion);
			params.put("accessVersion", UUID.randomUUID().toString());
			params.put("deductApplicationUuid", deductApplicationUuid);
			params.put("newCheckResponseNo", newCheckResponseNo);
			params.put("oldCheckResponseNo", oldCheckResponseNo);

			String sql = "UPDATE t_deduct_application  set version = :accessVersion,check_response_no =:newCheckResponseNo , last_modified_time =NOW() " +
					"WHERE deduct_application_uuid = :deductApplicationUuid and version = :preAccessVersion and check_response_no =:oldCheckResponseNo ";

			this.genericDaoSupport.executeSQL(sql, params);

			String validateSql = "select id from t_deduct_application where deduct_application_uuid =:deductApplicationUuid and version =:accessVersion";
			int updatedRows = genericDaoSupport.queryForInt(validateSql, params);

			if (0 >= updatedRows) {
				logger.info(GloableLogSpec.AuditLogHeaderSpec()  + "nothing DeductApplication update and deductApplicationUuid:"+deductApplicationUuid);
			}

		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void saveDeductInfo(DeductRequestModel model, String creatorName, String IpAddress, String postProcessUrl) {
		DeductApplication deductApplication = creatDeductAppilcationByDeductRequestModel(model, creatorName, IpAddress, planNotifyNumber, planNotifyCitigroupNumber);

		deductAppDetailHandler.generateByRepaymentDetailList(model, deductApplication);

		DeductRequestLog deductRequestLog = new DeductRequestLog(model, postProcessUrl);
		deductRequestLogService.save(deductRequestLog);

	}

	private Map<String, String> buildHeaderParamsForNotifyDeductResult(
			String content,String financialContractUuid ) {
		Map<String, String> headerParams = new HashMap<String, String>();
		headerParams.put("Content-Type", "application/json");
		headerParams.put("merId", YX_API_MERID);
		headerParams.put(ApiConstant.PARAMS_SECRET,YX_API_SECRET);

		String context = financialContractConfigurationService.getFinancialContractConfigContentContent(financialContractUuid, FinancialContractConfigurationCode.ALLOW_SIGN.getCode());
		if (StringUtils.equals(context, FinancialContractConfigurationContentValue.SIGN)) {
			Dictionary dictionary;
			try {
				dictionary = dictionaryService.getDictionaryByCode(DictionaryCode.PLATFORM_PRI_KEY.getCode());
			} catch (DictionaryNotExsitException e) {
				logger.error(".#DeductApplicationBusinessHandlerImpl get private key fail");
				e.printStackTrace();
				return headerParams;
			}
			String signData = ApiSignUtils.rsaSign(content, dictionary.getContent());
			headerParams.put("sign", signData);
		}

		return headerParams;
	}

	private boolean checkAssetSetUniqueness(DeductCommandRequestModel commandModel, List<RepaymentDetail> details) {
		Set<String> repaymentPlanCodeSet=details.stream().
				collect(Collectors.mapping(RepaymentDetail::getRepaymentPlanNo, Collectors.toSet()));
		if(repaymentPlanCodeSet.size()!=details.size()){		
			commandModel.setCheckFailedMsg("还款明细中有重复还款计划！！！");
			return false;
		}
		return true;
	}

	private boolean checkRepaymentDetail(DeductCommandRequestModel commandModel, Contract contract,
			RepaymentDetail detail) {
		String repayScheduleNo=detail.getRepayScheduleNo();//非MD5的商户还款编号
		String repaymentPlanNo=detail.getRepaymentPlanNo();
		int currentPeriod=detail.getCurrentPeriod();
		AssetSet assetSet=null;
		if(StringUtils.isNotEmpty(repayScheduleNo)){
			FinancialContract financialContract=financialContractService.getFinancialContractBy(contract.getFinancialContractUuid());
			assetSet=repaymentPlanService.getAssetSetByRepayScheduleNoAndFinancialContractNo(financialContract.getContractNo(),repayScheduleNo, AssetSetActiveStatus.OPEN);
			if(null==assetSet){
				commandModel.setCheckFailedMsg("商户还款编号错误");
				return false;
			}
		}else if(StringUtils.isNotEmpty(repaymentPlanNo)){
			assetSet=repaymentPlanService.getActiveRepaymentPlanByRepaymentCode(repaymentPlanNo);
			if(null==assetSet){
				commandModel.setCheckFailedMsg("还款计划编号错误");
				return false;
			}
		}else if(currentPeriod>0){
			assetSet=repaymentPlanService.getAssetSetByContractAndCurrentPeriod(contract, currentPeriod, AssetSetActiveStatus.OPEN);
			if(null==assetSet){
				commandModel.setCheckFailedMsg("期数错误");
				return false;
			}
		}else{
			commandModel.setCheckFailedMsg("商户还款编号、还款计划编号、期数均未填");
			return false;
		} 
		detail.setRepayScheduleNo(assetSet.getOuterRepaymentPlanNo());//两者都为非MD5的商户还款编号
		detail.setRepaymentPlanNo(assetSet.getSingleLoanContractNo());
		detail.setCurrentPeriod(assetSet.getCurrentPeriod());
		return true;
	}
}




