package com.zufangbao.earth.yunxin.handler.deduct.impl;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.earth.yunxin.api.constant.CommandOpsFunctionCodes;
import com.zufangbao.earth.yunxin.handler.deduct.SystemDeductHandler;
import com.zufangbao.gluon.api.bridgewater.deduct.model.DeductDetailInfoModel;
import com.zufangbao.gluon.api.bridgewater.deduct.model.DeductInfoModel;
import com.zufangbao.gluon.opensdk.HttpClientUtils;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.api.model.deduct.DeductApplicationExecutionStatus;
import com.zufangbao.sun.api.model.deduct.RepaymentType;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.log.SystemDeductLog;
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.service.ContractAccountService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.OrderService;
import com.zufangbao.sun.service.SystemDeductLogService;
import com.zufangbao.sun.utils.AmountUtils;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.AssetSetActiveStatus;
import com.zufangbao.sun.yunxin.entity.Dictionary;
import com.zufangbao.sun.yunxin.entity.DictionaryCode;
import com.zufangbao.sun.yunxin.entity.ExecutingSettlingStatus;
import com.zufangbao.sun.yunxin.entity.OrderSource;
import com.zufangbao.sun.yunxin.entity.OrderType;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.model.RepaymentChargesDetail;
import com.zufangbao.sun.yunxin.exception.DictionaryNotExsitException;
import com.zufangbao.sun.yunxin.exception.RepaymentPlanDeductLockedException;
import com.zufangbao.sun.yunxin.handler.SmsQueneHandler;
import com.zufangbao.sun.yunxin.service.DictionaryService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanExtraChargeService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemChargeService;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("systemDeductHandler")
public class SystemDeductHandlerImpl implements SystemDeductHandler{

	@Autowired
	private OrderService orderService;

	@Autowired
	private RepaymentPlanExtraChargeService repaymentPlanExtraChargeService;

	@Autowired
	private SystemDeductLogService systemDeductLogService;

	@Autowired
	private DeductApplicationService deductApplicationService;

	@Autowired
	private DictionaryService dictionaryService;

	@Autowired
	private SmsQueneHandler smsQueneHandler;

	@Autowired
	private ContractAccountService contractAccountService;

	@Autowired
	private RepaymentOrderItemService repaymentOrderItemService;

	@Autowired
	private RepaymentPlanService repaymentPlanService;

	@Autowired
	private RepaymentOrderItemChargeService repaymentOrderItemChargeService;

	@Autowired
	private FinancialContractService financialContractService;

	/** 提前划扣 **/
	private static final String REPAYMENT_TYPE_AHEAD = "0";

	/** 正常还款 **/
	private static final String REPAYMENT_TYPE_NORMAL = "1";

	/** 逾期还款 **/
	private static final String REPAYMENT_TYPE_OVERDUE = "2";

	/** 存在未完成的扣款申请 **/
	private static final String DEDUCT_ERROR_HAS_UNFINISH_DEDUCT_APPLICATION = String.valueOf(ApiResponseCode.HAS_EXIST_DEDUCT_APPLICATION);

	/** 扣款并发错误 **/
	private static final String DEDUCT_ERROR_LOCKED_REPAYMENT_PLAN = String.valueOf(ApiResponseCode.DEDUCT_CONCURRENT_ERROR);

	@Override
	public List<Long> getTodayWaitingDeductOrderOnPlannedRepaymentDate(OrderSource orderSource) {
		List<Order> orders = orderService.getSettlementStatementInNormalProcessing();
		return orders.stream()
				.filter(o -> isAllowedSystemDeduct(o, RepaymentType.NORMAL)
						&& o.getOrderSource() == orderSource)
				.map(Order::getId)
				.collect(Collectors.toList());
	}

	@Override
	public List<Long> getTodayWaitingDeductOrderAfterPlannedRepaymentDate(OrderSource orderSource) {
		List<Order> orders = orderService.getSettlementStatementInNormalProcessing();
		return orders.stream()
				.filter(o -> isAllowedSystemDeduct(o, RepaymentType.OVERDUE)
						&& o.getOrderSource() == orderSource)
				.map(Order::getId)
				.collect(Collectors.toList());
	}

	@Override
	public List<Long> getTodayWaitingDeductOrderForPrepaymentApplication(OrderSource orderSource) {
		List<Order> orders = orderService.getSettlementStatementInPrepaymentProcessing();
		return orders.stream()
				.filter(o -> isAllowedSystemDeduct(o, RepaymentType.NORMAL)
						&& o.getOrderSource() == orderSource)
				.map(Order::getId)
				.collect(Collectors.toList());
	}

	@Override
	public List<Long> getTodayEffectiveUnexecutedManualNormalOrder() {
		List<Order> orders = orderService.getTodayEffectiveUnexecutedManualNormalOrder();
		//应收日前允许扣款的结算单列表
		List<Long> aheadDeductOrderIds = orders.stream()
				.filter(o -> isAllowedSystemDeduct(o, RepaymentType.ADVANCE))
				.map(Order::getId).collect(Collectors.toList());

		//应收日允许扣款的结算单列表
		List<Long> normalDeductOrderIds = orders.stream()
				.filter(o -> isAllowedSystemDeduct(o, RepaymentType.NORMAL))
				.map(Order::getId).collect(Collectors.toList());
		
		//应收日后允许扣款的结算单列表
		List<Long> overdueDeductOrderIds = orders.stream()
				.filter(o -> isAllowedSystemDeduct(o, RepaymentType.OVERDUE))
				.map(Order::getId).collect(Collectors.toList());
		
		List<Long> totalOrderIds = new ArrayList<Long>();
		totalOrderIds.addAll(aheadDeductOrderIds);
		totalOrderIds.addAll(normalDeductOrderIds);
		totalOrderIds.addAll(overdueDeductOrderIds);

		if (CollectionUtils.isNotEmpty(totalOrderIds)){
			List<Long> orderId = new ArrayList<Long>();
			orderId.add(totalOrderIds.get(0));
			return orderId;
		}
		return totalOrderIds;
	}

	/**
	 * 是否允许系统扣款
	 * @param repaymentType
	 * 	      0：提前划扣 1：计划还款日扣款 2：物理逾期日起扣款
	 */
	private boolean isAllowedSystemDeduct(Order order, RepaymentType repaymentType) {

		if(order == null){
			return false;
		}
		AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(order.getAssetSetUuid());
		FinancialContract financialContract = order.getFinancialContract();
		if(assetSet == null || financialContract == null) {
			return false;
		}
		if(order.getOrderType() != OrderType.NORMAL) {
			return false;
		}
		Date currentDate = new Date();
		switch (repaymentType) {
			case ADVANCE:
				return financialContract.isSysNormalDeductFlag() && assetSet.isAheadDate(currentDate);
			case NORMAL:
				return financialContract.isSysNormalDeductFlag() && assetSet.isAssetRecycleDate(currentDate);
			case OVERDUE:
				return financialContract.isSysOverdueDeductFlag() && assetSet.isOverdueDate(currentDate);
			default:
				return false;
		}
	}

	@Override
	public DeductInfoModel getDeductInfoModelBy(Long orderId) {
		Order order = orderService.load(Order.class, orderId);

		//已关闭结算单，返回空
		if(order.getExecutingSettlingStatus() == ExecutingSettlingStatus.CLOSED) {
			return null;
		}
		
		AssetSet repaymentPlan = repaymentPlanService.getUniqueRepaymentPlanByUuid(order.getAssetSetUuid());
		//无效的还款计划，返回空
		if(repaymentPlan.getActiveStatus() == AssetSetActiveStatus.INVALID) {
			return null;
		}

		//作废、扣款中、回购的还款计划，不允许扣款
		String activeDeductApplicationUuid = repaymentPlan.getActiveDeductApplicationUuid();
		if(!AssetSet.EMPTY_UUID.equals(activeDeductApplicationUuid)) {
			throw new RepaymentPlanDeductLockedException();
		}

		DeductDetailInfoModel deductDetailInfoModel = buildDeductDetailInfoModel(repaymentPlan, order);

		return buildDeductInfoModel(repaymentPlan, order, deductDetailInfoModel);
	}

	private DeductInfoModel buildDeductInfoModel(AssetSet assetSet, Order order, DeductDetailInfoModel deductDetailInfoModel) {

		Contract contract = assetSet.getContract();
		ContractAccount contractAccount = contractAccountService.getTheEfficientContractAccountBy(contract);
		//TODO 宝付－招行，使用固定手机号
		String mobile = StringUtils.EMPTY;
		if("C10308".equals(contractAccount.getStandardBankCode())) {
			try {
				Dictionary dictionary = dictionaryService.getDictionaryByCode(DictionaryCode.BAOFU_DEDUCT_DEFAULT_MOBILE.getCode());
				mobile = dictionary.getContent();
			} catch (DictionaryNotExsitException e) {
				mobile = StringUtils.EMPTY;
			}
		}

		FinancialContract financialContract = order.getFinancialContract();

		String financialProductCode = financialContract.getContractNo();
		long loanOverdueStartDay = financialContract.getLoanOverdueStartDay();

		DeductInfoModel deductInfoModel = new DeductInfoModel();
		deductInfoModel.setFn(CommandOpsFunctionCodes.COMMAND_DEDUCT);

		String deductId = UUID.randomUUID().toString();
		deductInfoModel.setRequestNo(deductId);
		deductInfoModel.setDeductId(deductId);
		deductInfoModel.setFinancialProductCode(financialProductCode);
		deductInfoModel.setApiCalledTime(DateUtils.today());
		deductInfoModel.setMobile(mobile);

		String uniqueId = contract.getUniqueId();
		String contractNo = contract.getContractNo();
		if(StringUtils.isNotEmpty(uniqueId)) {
			deductInfoModel.setUniqueId(uniqueId);
		}else {
			deductInfoModel.setContractNo(contractNo);
		}
		deductInfoModel.setAmount(order.getTotalRent());

		int intervalDay = DateUtils.compareTwoDatesOnDay(new Date(), assetSet.getAssetRecycleDate());

		//间隔日为负值，或间隔日小于逾期起始日，记正常扣款
		if(intervalDay < 0){
			deductInfoModel.setRepaymentType(REPAYMENT_TYPE_AHEAD);
		} else if(intervalDay == 0 || intervalDay < loanOverdueStartDay) {
			deductInfoModel.setRepaymentType(REPAYMENT_TYPE_NORMAL);
		}else {
			//否则，记逾期扣款
			deductInfoModel.setRepaymentType(REPAYMENT_TYPE_OVERDUE);
		}

		deductInfoModel.setRepaymentDetails(Arrays.asList(deductDetailInfoModel));
		return deductInfoModel;
	}

	private DeductDetailInfoModel buildDeductDetailInfoModel(AssetSet assetSet, Order order) {
		String repaymentPlanNo = assetSet.getSingleLoanContractNo();
		BigDecimal totalAmount = order.getTotalRent();

		DeductDetailInfoModel deductDetailInfoModel = new DeductDetailInfoModel();
		deductDetailInfoModel.setRepaymentPlanNo(repaymentPlanNo);
		deductDetailInfoModel.setRepaymentAmount(totalAmount);

		RepaymentChargesDetail repaymentChargesDetail = order.getChargesDetailObj();

		deductDetailInfoModel.setRepaymentPrincipal(repaymentChargesDetail.getLoanAssetPrincipal());
		deductDetailInfoModel.setRepaymentInterest(repaymentChargesDetail.getLoanAssetInterest());
		deductDetailInfoModel.setLoanFee(repaymentChargesDetail.getLoanServiceFee());
		deductDetailInfoModel.setTechFee(repaymentChargesDetail.getLoanTechFee());
		deductDetailInfoModel.setOtherFee(repaymentChargesDetail.getLoanOtherFee());
		deductDetailInfoModel.setOverDueFeeDetail(repaymentChargesDetail.getOverDueFeeDetailsMap());

		return deductDetailInfoModel;
	}

	@Override
	public boolean existSuccessOrProcessingDeductApplication(Long orderId) {
		List<String> deductRequestNoList = systemDeductLogService.getDeductRequestNoListBy(orderId);
		if(CollectionUtils.isEmpty(deductRequestNoList)) {
			return false;
		}
		return deductApplicationService.existSuccessOrProcessingDeductionApplication(deductRequestNoList);
	}

	@Override
	public void updateDeductResultAfterDeductProcessing(Long orderId, Serializable logId, Result result) {

		Map<String, Object> data = result.getData();
		String rspHttpStatus = (String) data.getOrDefault(HttpClientUtils.DATA_RESPONSE_HTTP_STATUS, "");
		String rspPacket = (String) data.getOrDefault(HttpClientUtils.DATA_RESPONSE_PACKET, "");

		SystemDeductLog systemDeductLog = systemDeductLogService.load(SystemDeductLog.class, logId);
		systemDeductLog.setSendEndTime(new Date());
		systemDeductLog.setRspHttpStatus(rspHttpStatus);

		Result rspResult = JsonUtils.parse(rspPacket, Result.class);
		if(rspResult != null) {
			systemDeductLog.setRspCode(rspResult.getCode());
			systemDeductLog.setRspMsg(rspResult.getMessage());
		}

		ExecutingSettlingStatus executeStatus = ExecutingSettlingStatus.DOING;

		if(!result.isValid()) {
			executeStatus = ExecutingSettlingStatus.FAIL;
			systemDeductLog.setRspMsg("系统扣款请求受理失败，递交对端失败("+rspHttpStatus+")!");
		}

		systemDeductLogService.update(systemDeductLog);
		//扣款并发，不更新结算单，忽略此次扣款请求
		String deductRspCode = systemDeductLog.getRspCode();
		if (DEDUCT_ERROR_HAS_UNFINISH_DEDUCT_APPLICATION.equals(deductRspCode)
				|| DEDUCT_ERROR_LOCKED_REPAYMENT_PLAN.equals(deductRspCode)) {
			return;
		}
		orderService.updateNomalOrderExecutingStatus(orderId, executeStatus);
	}

	/**
	 * 获取未结清，处理中的结算单（系统扣款）
	 */
	@Override
	public List<Long> getUnclearAndProcessingNormalOrderListForSystemDeduct() {
		//获取未结清，处理中的结算单
		List<Long> orderIds = orderService.getUnclearAndProcessingNormalOrderList();
		List<Long> systemDeductOrderIds = new ArrayList<Long>();
		for (Long orderId : orderIds) {
			List<String> deductRequestNoList = systemDeductLogService.getDeductRequestNoListBy(orderId);
			if(CollectionUtils.isEmpty(deductRequestNoList)) {
				continue;//不存在扣款记录，跳过
			}
			systemDeductOrderIds.add(orderId);
		}
		return systemDeductOrderIds;
	}

	/**
	 * 同步处理中的结算单（系统扣款）
	 * @param orderId 结算单id
	 */
	@Override
	public void syncProcessingNormalOrderForSystemDeduct(Long orderId) {
		Order order = orderService.getOrderById(orderId, OrderType.NORMAL);

		//非处理中结算单，跳过
		if(order.getExecutingSettlingStatus() != ExecutingSettlingStatus.DOING) {
			return;
		}

		List<String> deductRequestNoList = systemDeductLogService.getDeductRequestNoListBy(orderId);
		List<DeductApplication> deductApplications = deductApplicationService.getDeductApplicationListByRequestNoList(deductRequestNoList);
		//扣款申请不存在，置失败
		if(CollectionUtils.isEmpty(deductApplications)) {
			order.updateClearStatus(new Date(), false);
			orderService.update(order);
		}
		
		boolean isAllFinish = deductApplications.stream().allMatch(this::isFinish);
		//所有完成（成功、失败、撤销），开始同步
		if(isAllFinish) {
			boolean isExistSuccess = deductApplications.stream().anyMatch(da -> da.getExecutionStatus() == DeductApplicationExecutionStatus.SUCCESS);
			order.updateClearStatus(new Date(), isExistSuccess);
			orderService.update(order);

			if(isExistSuccess){
				createDeductSuccessSms(isExistSuccess, order);
			}
		}
	}

	private void createDeductSuccessSms(boolean isExistSuccess, Order order) {
		try {
			AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(order.getAssetSetUuid());
			FinancialContract financialContract = order.getFinancialContract();
			String productCode = financialContract.getContractNo();
			//结算单金额，与还款计划总金额一致，生成成功短信（农分期）
			if("G08200".equalsIgnoreCase(productCode) && AmountUtils.equals(assetSet.getAssetFairValue(), order.getTotalRent())) {
				boolean allowedSendStatus = dictionaryService.getSmsAllowSendFlag();
				smsQueneHandler.saveSuccessSmsQuene(assetSet, allowedSendStatus, new Date());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isFinish(DeductApplication deductApplication) {
		DeductApplicationExecutionStatus executionStatus = deductApplication.getExecutionStatus();
		if (executionStatus == DeductApplicationExecutionStatus.SUCCESS) {
			return true;
		}
		if (executionStatus == DeductApplicationExecutionStatus.FAIL) {
			return true;
		}
        return executionStatus == DeductApplicationExecutionStatus.ABANDON;
    }

}
