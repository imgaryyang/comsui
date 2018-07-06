package com.suidifu.microservice.handler.impl;

import com.zufangbao.sun.api.model.deduct.RepaymentType;
import com.zufangbao.sun.api.model.modify.ModifyOverdueParams;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.account.VirtualAccountTransactionType;
import com.zufangbao.sun.entity.account.special.account.AccountTransType;
import com.zufangbao.sun.entity.account.special.account.SpecialAccount;
import com.zufangbao.sun.entity.account.special.account.SpecialAccountBasicType;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.repayment.order.RefundOrder;
import com.zufangbao.sun.entity.repayment.order.RefundStatus;
import com.zufangbao.sun.ledgerbook.AssetCategory;
import com.zufangbao.sun.ledgerbook.AssetConvertor;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.EntryLevel;
import com.zufangbao.sun.ledgerbook.InvalidLedgerException;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.service.RefundOrderService;
import com.zufangbao.sun.service.VirtualAccountFlowService;
import com.zufangbao.sun.service.VirtualAccountService;
import com.zufangbao.sun.utils.AmountUtils;
import com.zufangbao.sun.utils.uuid.UUIDUtil;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.ExecutingStatus;
import com.zufangbao.sun.yunxin.entity.api.AssetRefundMode;
import com.zufangbao.sun.yunxin.entity.datasync.BusinessLogsModel;
import com.zufangbao.sun.yunxin.entity.datasync.OperateMode;
import com.zufangbao.sun.yunxin.entity.model.RepaymentChargesDetail;
import com.zufangbao.sun.yunxin.entity.model.api.ExtraChargeSpec;
import com.zufangbao.sun.yunxin.exception.VirtualAccountNotExsitException;
import com.zufangbao.sun.yunxin.handler.*;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.account.SpecialAccountFlowService;
import com.zufangbao.sun.yunxin.service.account.SpecialAccountService;
import com.suidifu.microservice.exception.AssetRefundException;

import lombok.extern.log4j.Log4j2;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * dcc
 */
@Component("specialAccountHandler")
@Log4j2
public class SpecialAccountHandlerImpl implements SpecialAccountHandler {
	
	@Resource
	private SpecialAccountService specialAccountService;
	
	@Resource
	private SpecialAccountFlowService specialAccountFlowService;
	
	
	@Override
	public void update_asset_refund_financial_contract_account(String financialContractUuid,
			Map<String, BigDecimal> account_amount_map) {
		
		 //还款户 ——> 客户账户
		  int basicAccountTypeOfBeneficiary = SpecialAccountBasicType.BENEFICIARY_ACCOUNT.ordinal();
		  int basicAccountTypeOfIndependent = SpecialAccountBasicType.INDEPENDENT_ACCOUNT.ordinal();
	 	  
		  List<SpecialAccount> trdSpecialAccountsOfBeneficiary = specialAccountService.queryFinancialContractUuidAndBasicAccountType(basicAccountTypeOfBeneficiary, financialContractUuid, EntryLevel.LVL3);
		  List<SpecialAccount> trdSpecialAccountsOfIndependent= specialAccountService.queryFinancialContractUuidAndBasicAccountType(basicAccountTypeOfIndependent, financialContractUuid, EntryLevel.LVL3);
		  if(CollectionUtils.isEmpty(trdSpecialAccountsOfIndependent) || CollectionUtils.isEmpty(trdSpecialAccountsOfBeneficiary)){
	 		 log.info("specialAccount is null , basicAccountType ["+basicAccountTypeOfIndependent+","+basicAccountTypeOfBeneficiary+"] , fstLevelContractUuid ["+financialContractUuid+"]");
				return ;
	 	  }
		  
		  //account_type_code_and_charge_key
		  for(SpecialAccount specialAccount : trdSpecialAccountsOfBeneficiary){
	 		 String account_name_key = ExtraChargeSpec.account_type_code_and_charge_key.get(specialAccount.getAccountTypeCode());
	 		 BigDecimal amount = account_amount_map.get(account_name_key);
	 		 if(AmountUtils.isEmpty(amount)){
	 			 continue;
	 		 }
	 		 //还款户减退款金额
	 		 specialAccountService.updateBalance(specialAccount.getBalance().subtract(amount),specialAccount.getUuid(),specialAccount.getVersion());
	 		 specialAccount = specialAccountService.get(specialAccount.getUuid());
	 		 String batchUuid = UUIDUtil.randomUUID();
	 		 long singleCashFlowstart= System.currentTimeMillis();
	 		 specialAccountFlowService.createSpecialAccountFlow(amount, trdSpecialAccountsOfIndependent.get(0), specialAccount, "客户账户", AccountSide.DEBIT.ordinal(), AccountTransType.REFUND.ordinal() , batchUuid);
	 		 specialAccountFlowService.createSpecialAccountFlow(amount, specialAccount, trdSpecialAccountsOfIndependent.get(0), "还款户", AccountSide.CREDIT.ordinal(), AccountTransType.REFUND.ordinal() , batchUuid);
	 		 long singleCashFlowend= System.currentTimeMillis();
	 		 log.info("insert single specialAccountCashFlow use time :"+(singleCashFlowend-singleCashFlowstart));
	 	 }
	 	 	
	 	 specialAccountService.update_parent_account_balance_according_to_child_accounts(trdSpecialAccountsOfBeneficiary.get(0).getParentAccountUuid());
	 	 	
		
	}
	
 
}