package com.zufangbao.earth.yunxin.task;

import com.demo2do.core.utils.DateUtils;
import com.demo2do.core.utils.SignatureUtils;
import com.suidifu.coffer.GlobalSpec;
import com.suidifu.coffer.GlobalSpec.BankCorpEps;
import com.suidifu.coffer.entity.CashFlowResult;
import com.suidifu.coffer.entity.CashFlowResultModel;
import com.suidifu.coffer.entity.QueryCashFlowModel;
import com.suidifu.coffer.handler.pab.PABDirectBankHandler;
import com.zufangbao.sun.entity.account.Account;
import com.zufangbao.sun.entity.directbank.USBKey;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.directbank.business.CashFlowChannelType;
import com.zufangbao.sun.entity.directbank.business.StrikeBalanceStatus;
import com.zufangbao.sun.ledgerbook.AccountSide;
import com.zufangbao.sun.service.AccountService;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.USBKeyService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component("cashFlowTask")
public class CashFlowTask {

	@Autowired
	private AccountService accountService;
	@Autowired
	private USBKeyService usbKeyService;
	@Autowired
	private PABDirectBankHandler pabDirectBankHandler;
	@Autowired
	private CashFlowService cashFlowService;
	
	private static Log logger = LogFactory.getLog(CashFlowTask.class);

	public void exeQueryIntradayCashFlow() {
		logger.info("start exeQueryIntradayCashFlow...");
		
		List<Account> accountList = accountService.listAccountWithScanCashFlowSwitchOn();
		
		for (Account account : accountList) {
			try {
				if (!BankCorpEps.PAB_CODE.equals(account.getBankCode())) {
					continue;
				}
				String usbUuid = account.getUsbUuid();
				if (StringUtils.isEmpty(usbUuid)) {
					logger.warn("could not find usbkey...");
					continue;
				}
				USBKey usbKey = usbKeyService.getUSBKeyByUUID(usbUuid);
				if (null == usbKey) {
					logger.warn("could not find usbkey...");
					continue;
				}

				//TODO 迁移
				if(isIntradayPageNoNeedReset(new Date())) {
					accountService.updateCashFlowConfig(account, "1");
				}
				
				String nextPageNo = account.getCashFlowConfig().getOrDefault("nextPageNo", "1");
				QueryCashFlowModel queryCashFlowModel = new QueryCashFlowModel(
						account.getAccountNo(), 
						DateUtils.format(new Date(),"yyyyMMdd"), 
						DateUtils.format(new Date(),"yyyyMMdd"), 
						new Date(),
						nextPageNo,
						account.getCashFlowConfig().getOrDefault("intradayPageSize", BankCorpEps.PAB_DEFAULT_PAGESIZE));
						
				Map<String, String> workParms = usbKey.getConfig();
				CashFlowResultModel cashFlowResultModel = pabDirectBankHandler.queryIntradayCashFlow(queryCashFlowModel,
						workParms);
				if(GlobalSpec.DEFAULT_FAIL_CODE.equals(cashFlowResultModel.getCommCode())) {
					logger.warn(cashFlowResultModel.getErrMsg());
					//accountService.updateCashFlowConfig(account, "1");
					continue;
				}
				List<CashFlowResult> cashFlowResultList = cashFlowResultModel.getCashFlowResult();
				if(CollectionUtils.isEmpty(cashFlowResultList)) {
					logger.info(account.getAccountNo() + "intraday cash flow is empty...");
					//accountService.updateCashFlowConfig(account, "1");
					continue;
				}
				
				
				for (CashFlowResult cashFlowResult : cashFlowResultList) {
					try {
						boolean iscashFlowExist = cashFlowService.isCashFlowAlreadyExist(cashFlowResult.getHostAccountNo(), cashFlowResult.getBankSequenceNo(), cashFlowResult.getTransactionAmount(), cashFlowResult.getTransactionTime());
						if(iscashFlowExist) {
							continue;
						}
						
						AccountSide accountSide = com.suidifu.coffer.entity.AccountSide.CREDIT.equals(cashFlowResult.getAccountSide()) ? AccountSide.CREDIT : AccountSide.DEBIT;
						
						StrikeBalanceStatus strikeBalanceStatus = null == cashFlowResult.getStrikeBalanceStatus() ? null : com.suidifu.coffer.entity.StrikeBalanceStatus.NORMAL.equals(cashFlowResult.getStrikeBalanceStatus()) ? StrikeBalanceStatus.NORMAL : StrikeBalanceStatus.STRIKE;
						
						String tradeUuid = StringUtils.EMPTY;
						String remark = cashFlowResult.getRemark();
						if(StringUtils.isNotEmpty(remark)) {
							String regex = "\\((.*?)\\)";
							Pattern pattern = Pattern.compile(regex);
							Matcher matcher = pattern.matcher(remark);
							if(null != matcher && matcher.find(0)) {
								tradeUuid = matcher.group(1);
							}
						}
						
						CashFlow cashFlow = new CashFlow(UUID.randomUUID().toString(), CashFlowChannelType.DirectBank, null, null, cashFlowResult.getHostAccountNo(), cashFlowResult.getHostAccountName(), cashFlowResult.getCounterAccountNo(), cashFlowResult.getCounterAccountName(), cashFlowResult.getCounterAccountAppendix(), cashFlowResult.getCounterBankInfo(), accountSide, cashFlowResult.getTransactionTime(), cashFlowResult.getTransactionAmount(), cashFlowResult.getBalance(), cashFlowResult.getTransactionVoucherNo(), cashFlowResult.getBankSequenceNo(), cashFlowResult.getRemark(), cashFlowResult.getOtherRemark(), strikeBalanceStatus, tradeUuid);
						String flowFlag = getFlag(cashFlowResult);
						cashFlow.setStringFieldTwo(flowFlag);

						cashFlowService.save(cashFlow);
					} catch (Exception e) {
						e.printStackTrace();
						logger.warn("save cash flow failed, host account:" + cashFlowResult.getHostAccountNo() + "bankSequenceNo:" + cashFlowResult.getBankSequenceNo());
					}
					
				}
				
				boolean hasNextPage=cashFlowResultModel.isHasNextPage();
//				if(!hasNextPage) {
//					accountService.updateCashFlowConfig(account, "1");
//					continue;
//				}
				if(hasNextPage) {
					accountService.updateCashFlowConfig(account, String.valueOf(Integer.parseInt(nextPageNo)+1));
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.warn("handle account:" + account.getAccountNo() + "failed...");
			}
		}

		logger.info("end exeQueryIntradayCashFlow...");
	}
	
	
	
	public void exeQueryHistoryCashFlow() {
		
		logger.info("start exeQueryHistoryCashFlow...");
		
		List<Account> accountList = accountService.listAccountWithScanCashFlowSwitchOn();

		for (Account account : accountList) {
			try {
				if (!BankCorpEps.PAB_CODE.equals(account.getBankCode())) {
					continue;
				}
				String usbUuid = account.getUsbUuid();
				if (StringUtils.isEmpty(usbUuid)) {
					logger.warn("could not find usbkey...");
					continue;
				}
				USBKey usbKey = usbKeyService.getUSBKeyByUUID(usbUuid);
				if (null == usbKey) {
					logger.warn("could not find usbkey...");
					continue;
				}
				
				//TODO 迁移
				if(isHistoryPageNoNeedReset(new Date())) {
					accountService.updateCashFlowConfig(account, "1");
				}
				
				String nextPageNo = account.getCashFlowConfig().getOrDefault("nextHistoryPageNo", "1");
				QueryCashFlowModel queryCashFlowModel = new QueryCashFlowModel(
						account.getAccountNo(), 
						DateUtils.format(DateUtils.addDays(new Date(), -1),"yyyyMMdd"),
						DateUtils.format(DateUtils.addDays(new Date(), -1),"yyyyMMdd"),
						new Date(),
						nextPageNo,
						account.getCashFlowConfig().getOrDefault("historyPageSize", BankCorpEps.PAB_DEFAULT_HISTORY_PAGESIZE));
						
				Map<String, String> workParms = usbKey.getConfig();
				CashFlowResultModel cashFlowResultModel = pabDirectBankHandler.queryIntradayCashFlow(queryCashFlowModel,
						workParms);
				if(GlobalSpec.DEFAULT_FAIL_CODE.equals(cashFlowResultModel.getCommCode())) {
					logger.warn(cashFlowResultModel.getErrMsg());
					//accountService.updateCashFlowConfig(account, "1");
					continue;
				}
				List<CashFlowResult> cashFlowResultList = cashFlowResultModel.getCashFlowResult();
				if(CollectionUtils.isEmpty(cashFlowResultList)) {
					logger.info(account.getAccountNo() + "intraday cash flow is empty...");
					//accountService.updateCashFlowConfig(account, "1");
					continue;
				}
				
				for (CashFlowResult cashFlowResult : cashFlowResultList) {
					try {
						boolean iscashFlowExist = cashFlowService.isCashFlowAlreadyExist(cashFlowResult.getHostAccountNo(), cashFlowResult.getBankSequenceNo(), cashFlowResult.getTransactionAmount(), cashFlowResult.getTransactionTime());
						if(iscashFlowExist) {
							continue;
						}
						
						AccountSide accountSide = com.suidifu.coffer.entity.AccountSide.CREDIT.equals(cashFlowResult.getAccountSide()) ? AccountSide.CREDIT : AccountSide.DEBIT;
						
						StrikeBalanceStatus strikeBalanceStatus = null == cashFlowResult.getStrikeBalanceStatus() ? null : com.suidifu.coffer.entity.StrikeBalanceStatus.NORMAL.equals(cashFlowResult.getStrikeBalanceStatus()) ? StrikeBalanceStatus.NORMAL : StrikeBalanceStatus.STRIKE;
						
						String tradeUuid = StringUtils.EMPTY;
						String remark = cashFlowResult.getRemark();
						if(StringUtils.isNotEmpty(remark)) {
							String regex = "\\((.*?)\\)";
							Pattern pattern = Pattern.compile(regex);
							Matcher matcher = pattern.matcher(remark);
							if(null != matcher && matcher.find(0)) {
								tradeUuid = matcher.group(1);
							}
						}
						
						CashFlow cashFlow = new CashFlow(UUID.randomUUID().toString(), CashFlowChannelType.DirectBank, null, null, cashFlowResult.getHostAccountNo(), cashFlowResult.getHostAccountName(), cashFlowResult.getCounterAccountNo(), cashFlowResult.getCounterAccountName(), cashFlowResult.getCounterAccountAppendix(), cashFlowResult.getCounterBankInfo(), accountSide, cashFlowResult.getTransactionTime(), cashFlowResult.getTransactionAmount(), cashFlowResult.getBalance(), cashFlowResult.getTransactionVoucherNo(), cashFlowResult.getBankSequenceNo(), cashFlowResult.getRemark(), cashFlowResult.getOtherRemark(), strikeBalanceStatus, tradeUuid);
						String flowFlag = getFlag(cashFlowResult);
						cashFlow.setStringFieldTwo(flowFlag);

						cashFlowService.save(cashFlow);
					} catch (Exception e) {
						e.printStackTrace();
						logger.warn("save cash flow failed, host account:" + cashFlowResult.getHostAccountNo() + "bankSequenceNo:" + cashFlowResult.getBankSequenceNo());
					}
					
				}
				
				boolean hasNextPage=cashFlowResultModel.isHasNextPage();
//				if(!hasNextPage) {
//					accountService.updateCashFlowConfig(account, "1");
//					continue;
//				}
				if(hasNextPage) {
					accountService.updateCashFlowConfig(account, String.valueOf(Integer.parseInt(nextPageNo)+1));
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.warn("handle account:" + account.getAccountNo() + "failed...");
			}
		}

		logger.info("end exeQueryHistoryCashFlow...");
	}
	
	
	private boolean isIntradayPageNoNeedReset(Date currentTime) {
		String today = DateUtils.format(new Date(), "yyyy-MM-dd");
		Date startDate = DateUtils.parseDate(today + " 5:00:00", "yyyy-MM-dd HH:mm:ss");
		Date endDate = DateUtils.parseDate(today + " 5:10:00", "yyyy-MM-dd HH:mm:ss");

        return currentTime.after(startDate) && currentTime.before(endDate);
    }

    private boolean isHistoryPageNoNeedReset(Date currentTime) {
		String today = DateUtils.format(new Date(), "yyyy-MM-dd");
		Date startDate = DateUtils.parseDate(today + " 3:00:00", "yyyy-MM-dd HH:mm:ss");
		Date endDate = DateUtils.parseDate(today + " 3:10:00", "yyyy-MM-dd HH:mm:ss");

        return currentTime.after(startDate) && currentTime.before(endDate);
    }

	/**
	 * 获取流水标识：付款方户号+付款方人名+收款方户号+收款方人名+金额+时间+摘要，做md5值
	 */
	public String getFlag(CashFlowResult cashFlowResult){
		String hostAccountNo = cashFlowResult.getHostAccountNo()==null?"":cashFlowResult.getHostAccountNo();
		String hostAccountName = cashFlowResult.getHostAccountName()==null?"":cashFlowResult.getHostAccountName();
		String counterAccountNo = cashFlowResult.getCounterAccountNo()==null?"":cashFlowResult.getCounterAccountNo();
		String counterAccountName = cashFlowResult.getCounterAccountName()==null?"":cashFlowResult.getCounterAccountName();
		String transactionAmount = cashFlowResult.getTransactionAmount()==null? BigDecimal.ZERO.toString():cashFlowResult.getTransactionAmount()+"";
		String transactionTime = cashFlowResult.getTransactionTime()==null?"": DateUtils.format(cashFlowResult.getTransactionTime(), "yyyy-MM-dd HH:mm:ss");
		String remark = cashFlowResult.getRemark()==null?"":cashFlowResult.getRemark();

		return SignatureUtils.makeMD5Signature(hostAccountNo, hostAccountName, counterAccountNo, counterAccountName, transactionAmount, transactionTime, remark);
	}

}
