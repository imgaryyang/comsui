package com.suidifu.citigroup.handler.impl;

import static com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.VALID_BALANCE_NOT_CORRECT_PARAMS;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.citigroup.entity.BalanceEntrySqlMode;
import com.suidifu.citigroup.entity.GeneralBalanceSqlMode;
import com.suidifu.citigroup.exception.CitiGroupRuntimeException;
import com.suidifu.citigroup.exception.ValidatorException;
import com.suidifu.citigroup.handler.GeneralBalanceHandler;
import com.suidifu.citigroup.handler.RemittanceBusinessHandler;
import com.suidifu.citigroup.handler.StandardGeneralBalanceHandler;
import com.suidifu.citigroup.service.BalanceEntryService;
import com.suidifu.citigroup.service.GeneralBalanceService;
import com.suidifu.citigroup.service.TableCacheService;
import com.zufangbao.gluon.spec.citigroup.BalanceRequestModel;
import com.zufangbao.sun.api.model.remittance.RemittanceCommandModel;
import com.zufangbao.sun.api.model.remittance.RemittancePlanInfo;
import com.zufangbao.sun.api.model.remittance.RemittanceResponsePacket;
import com.zufangbao.sun.yunxin.entity.model.SystemTraceLog;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.EVENT_NAME;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.SYSTEM_NAME;

@Component("generalBalanceHandler")
public class GeneralBalanceHandlerImpl implements GeneralBalanceHandler {

	private static Log logger = LogFactory.getLog(GeneralBalanceHandlerImpl.class);

	@Autowired
	private GeneralBalanceService generalBalanceService;

	@Autowired
	private BalanceEntryService balanceEntryService;

	@Autowired
	private RemittanceBusinessHandler remittanceBusinessHandler;

	@Autowired
	private StandardGeneralBalanceHandler standardGeneralBalanceHandler;

	@Autowired
	private TableCacheService tableCacheService;
	
	/**
	 * 放款限额冻结
	 */
	@Override
	public void modifyBankSavingFreezeForFreezing(Map<String, String> receiveParams) {

		String eventKey = StringUtils.EMPTY;
		
		boolean needSendToRemittance = true;

		boolean isSuccess = true;

		String failMessage = StringUtils.EMPTY;

		RemittanceResponsePacket remittanceResponsePacket = null;
		
		SystemTraceLog systemTraceLog = new SystemTraceLog();
		
//		boolean needLog = true;
		
		List<String> logs = new ArrayList<>();

		try {

			remittanceResponsePacket = JsonUtils.parse(
					receiveParams.get(ZhonghangResponseMapSpec.REMITTANCEREQUESTMODEL), RemittanceResponsePacket.class);

			BalanceEntrySqlMode.validateRemittanceRequestParam(remittanceResponsePacket);
			
			eventKey =  "remittanceApplicationUuid:"+ remittanceResponsePacket.getRemittanceApplicationUuid();
			
			systemTraceLog = new SystemTraceLog(EVENT_NAME.CITIGROUP_ACCEPT_REMITTANCE_REQUEST_FOR_FREEZING_START, eventKey,
					null, null, SystemTraceLog.INFO, null, SYSTEM_NAME.REMITTANCE_SYSTEM, SYSTEM_NAME.CITIGROUP);
			
			logger.info(systemTraceLog);
			
			//没有进行限额配置　默认不走限额
			if (!tableCacheService.validFinancialContractIsInBalance(remittanceResponsePacket.getFinancialContractUuid())) {
				
				return;
			}

			GeneralBalanceSqlMode generalBalanceSqlMode = generalBalanceService.getGeneralBalanceSqlModeBy(remittanceResponsePacket.getFinancialContractUuid());
			
			calculateTotalAmount(remittanceResponsePacket);
			
			validGeneralBalanceSqlMode(generalBalanceSqlMode, remittanceResponsePacket);

			// 组装balanceEntrySqlMode
			List<BalanceEntrySqlMode> balanceEntrySqlModes = BalanceEntrySqlMode.combineBalanceEntrySqlModeForFreezing(remittanceResponsePacket);

			// 更新额度
			generalBalanceService.updateBankSavingFreezeForFreezing(remittanceResponsePacket.getFinancialContractUuid(),remittanceResponsePacket.getTotalAmount());

			balanceEntrySqlModes.forEach(b -> b.setGeneralBalanceUuid(generalBalanceSqlMode.getUuid()));

			balanceEntryService.saveBalanceEntry(balanceEntrySqlModes);
			
			systemTraceLog.setEventName(EVENT_NAME.CITIGROUP_ACCEPT_REMITTANCE_REQUEST_FOR_FREEZING_END);

			logger.info(systemTraceLog);

//			needLog = false;
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
			failMessage = ExceptionUtils.getFullStackTrace(e);
			isSuccess = VALID_BALANCE_NOT_CORRECT_PARAMS.getOrDefault(VALID_BALANCE_NOT_CORRECT_PARAMS.keySet().stream().filter(param -> e.getCause().toString().contains(param)).findFirst().get(), false);
			logs.add(failMessage);
		} catch (CitiGroupRuntimeException e) {
			failMessage = e.getMessage();
			needSendToRemittance = false;
			logs.add(failMessage);
		}catch (ValidatorException e) {
			failMessage = e.getMessage();
			isSuccess = false;
			logs.add(failMessage);
		} catch (Exception e) {
			needSendToRemittance = false;
			e.printStackTrace();
			failMessage = ExceptionUtils.getFullStackTrace(e);
			logs.add(failMessage);
		} finally {
			if (CollectionUtils.isNotEmpty(logs)) {
				systemTraceLog = new SystemTraceLog(EVENT_NAME.CITIGROUP_ACCEPT_REMITTANCE_REQUEST_FOR_FREEZING, eventKey, null,
						failMessage, SystemTraceLog.ERROR, null, SYSTEM_NAME.REMITTANCE_SYSTEM, SYSTEM_NAME.CITIGROUP);
				logger.error(systemTraceLog);
			}
			if (needSendToRemittance) {
				standardGeneralBalanceHandler.pushJobToRemittanceForBusinessValidation(remittanceResponsePacket, isSuccess, failMessage);
				
				
			}
		}

	}

	/**
	 * 放款限额解冻
	 */
	@Override
	public void modifyBankSavingFreezeForUnFreezing(Map<String, String> receiveParams) {
		
		String eventKey = StringUtils.EMPTY;
		
		SystemTraceLog systemTraceLog = new SystemTraceLog();
		try {

			RemittanceResponsePacket remittanceResponsePacket = JsonUtils.parse(receiveParams.get(ZhonghangResponseMapSpec.REMITTANCEREQUESTMODEL), RemittanceResponsePacket.class);

			BalanceEntrySqlMode.validateRemittanceRequestParam(remittanceResponsePacket);
			
			eventKey =  "remittanceApplicationUuid:"+ remittanceResponsePacket.getRemittanceApplicationUuid();
			
			systemTraceLog = new SystemTraceLog(EVENT_NAME.CITIGROUP_ACCEPT_REMITTANCE_REQUEST_FOR_UNFREEZE_START, eventKey,
					null, null, SystemTraceLog.INFO, null, SYSTEM_NAME.REMITTANCE_SYSTEM, SYSTEM_NAME.CITIGROUP);
			
			logger.info(systemTraceLog);
			
			//没有进行限额配置　默认不走限额
			if (!tableCacheService.validFinancialContractIsInBalance(remittanceResponsePacket.getFinancialContractUuid())) {
				
				logger.info("没有进行限额配置　默认不走限额 financialContractUuid[" + remittanceResponsePacket.getFinancialContractUuid() + "]");
				return;
			}
			
			GeneralBalanceSqlMode generalBalanceSqlMode = generalBalanceService.getGeneralBalanceSqlModeBy(remittanceResponsePacket.getFinancialContractUuid());
			
			if (null == generalBalanceSqlMode) {

				logger.error("没有信托[" + remittanceResponsePacket.getFinancialContractUuid() + "]对应的额度信息！");

				return;
			}
			
			List<RemittancePlanInfo> remittancePlanInfosInFreeZing = remittanceResponsePacket.getRemittancePlanInfoList().stream().filter(p-> balanceEntryService.existFreezIngBalanceEntry(p.getRemittancePlanUuid())).collect(Collectors.toList());
			
			remittanceResponsePacket.setRemittancePlanInfoList(remittancePlanInfosInFreeZing);

			List<BalanceEntrySqlMode> balanceEntrySqlModes = BalanceEntrySqlMode.combineBalanceEntrySqlModeForUnFreez(remittanceResponsePacket);

			remittancePlanInfosInFreeZing.stream().forEach(p -> generalBalanceService.updateBankSavingFreeze(remittanceResponsePacket.getFinancialContractUuid(), p.getAmount(), p.getIsSuccess()));

			balanceEntrySqlModes.forEach(balanceEntrySqlMode -> balanceEntrySqlMode.setGeneralBalanceUuid(generalBalanceSqlMode.getUuid()));

			balanceEntryService.saveBalanceEntry(balanceEntrySqlModes);
			
			systemTraceLog.setEventName(EVENT_NAME.CITIGROUP_ACCEPT_REMITTANCE_REQUEST_FOR_UNFREEZE_END);

			logger.info(systemTraceLog);

		} catch (Exception e) {
			e.printStackTrace();
			systemTraceLog = new SystemTraceLog(EVENT_NAME.CITIGROUP_ACCEPT_REMITTANCE_REQUEST_FOR_UNFREEZE, eventKey, null,
					ExceptionUtils.getFullStackTrace(e), SystemTraceLog.ERROR, null, SYSTEM_NAME.REMITTANCE_SYSTEM, SYSTEM_NAME.CITIGROUP);
			logger.error(systemTraceLog);
		}
	}

	/**
	 * 放款业务校验
	 */
	@Override
	public void handleRemittanceBusiness(Map<String, String> receiveParams) {

		String ip = null;
		
		String eventKey = null;

		SystemTraceLog systemTraceLog = new SystemTraceLog();
		try {

			RemittanceResponsePacket packet = JsonUtils.parse(receiveParams.get(ZhonghangResponseMapSpec.REMITTANCEREQUESTMODEL), RemittanceResponsePacket.class);

			RemittanceCommandModel remittanceCommandModel = packet.getRemittanceCommandModel();

			String preProcessUrl = packet.getBusinessType();

			eventKey = "checkRequestNo:" + remittanceCommandModel.getCheckRequestNo() + "&remittanceApplicationUuid:"+ remittanceCommandModel.getRemittanceApplicationUuid();

			systemTraceLog = new SystemTraceLog(EVENT_NAME.CITIGROUP_ACCEPT_REMITTANCE_REQUEST_START, eventKey,
					null, null, SystemTraceLog.INFO, ip, SYSTEM_NAME.REMITTANCE_SYSTEM, SYSTEM_NAME.CITIGROUP);

			logger.info(systemTraceLog);
			
			remittanceBusinessHandler.handleRemittanceBusiness(preProcessUrl, remittanceCommandModel, ip,packet.getCallBackUrl());

			systemTraceLog.setEventName(EVENT_NAME.CITIGROUP_ACCEPT_REMITTANCE_REQUEST_END);

			logger.info(systemTraceLog);

		} catch (Exception e) {
			e.printStackTrace();
			systemTraceLog = new SystemTraceLog(EVENT_NAME.CITIGROUP_ACCEPT_REMITTANCE_REQUEST, eventKey, null,
					e.getMessage(), SystemTraceLog.ERROR, ip, SYSTEM_NAME.REMITTANCE_SYSTEM, SYSTEM_NAME.CITIGROUP);
			logger.error(systemTraceLog);
		}
	}

	/**
	 * 插入限额配置Or更新限额
	 */
	@Override
	public void updatOrInsertBankSavingLoan(Map<String, String> receiveParams) {
		
		try {
		BalanceRequestModel balanceRequestModel = JsonUtils.parse(receiveParams.get(ZhonghangResponseMapSpec.BALANCEREQUESTMODEL),BalanceRequestModel.class );
		
		if (null==balanceRequestModel) {
			logger.error("updatOrInsertBankSavingLoan:请求参数为空");
			return;
		}
		
		standardGeneralBalanceHandler.doUpdatOrInsertBankSavingLoan(balanceRequestModel);
		} catch (CitiGroupRuntimeException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
	}

	private void calculateTotalAmount(RemittanceResponsePacket remittanceResponsePacket) {

		List<RemittancePlanInfo> remittancePlanInfos = remittanceResponsePacket.getRemittancePlanInfoList();

		remittancePlanInfos.stream().forEach(p -> remittanceResponsePacket
				.setTotalAmount(p.getAmount().add(remittanceResponsePacket.getTotalAmount())));
	}

	private void validGeneralBalanceSqlMode(GeneralBalanceSqlMode generalBalanceSqlMode,RemittanceResponsePacket remittanceResponsePacket) throws ValidatorException{
		
		String failMessage = StringUtils.EMPTY;
		
		if (null == generalBalanceSqlMode) {

			failMessage = "没有信托[" + remittanceResponsePacket.getFinancialContractUuid() + "]对应的额度信息！";
			
			throw new CitiGroupRuntimeException(failMessage);

		}
		if (remittanceResponsePacket.getTotalAmount().compareTo(generalBalanceSqlMode.getBankSavingLoan()) > 0) {

			failMessage = "放款金额大于现存额度金额！　对应remittanceAppliactionUuid ["
					+ remittanceResponsePacket.getRemittanceApplicationUuid() + "]";
			
			throw new ValidatorException(failMessage);
		}
		
	}
	
}
