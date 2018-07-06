package com.suidifu.microservice.handler.impl;

import com.zufangbao.sun.api.model.deduct.RepaymentType;
import com.zufangbao.sun.api.model.modify.ModifyOverdueParams;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.account.VirtualAccountTransactionType;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.repayment.order.RefundOrder;
import com.zufangbao.sun.entity.repayment.order.RefundStatus;
import com.zufangbao.sun.ledgerbook.AssetCategory;
import com.zufangbao.sun.ledgerbook.AssetConvertor;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.InvalidLedgerException;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.service.RefundOrderService;
import com.zufangbao.sun.service.VirtualAccountFlowService;
import com.zufangbao.sun.service.VirtualAccountService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.ExecutingStatus;
import com.zufangbao.sun.yunxin.entity.api.AssetRefundMode;
import com.zufangbao.sun.yunxin.entity.datasync.BusinessLogsModel;
import com.zufangbao.sun.yunxin.entity.datasync.OperateMode;
import com.zufangbao.sun.yunxin.entity.model.RepaymentChargesDetail;
import com.zufangbao.sun.yunxin.exception.VirtualAccountNotExsitException;
import com.zufangbao.sun.yunxin.handler.*;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.suidifu.microservice.exception.AssetRefundException;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * dcc
 */
@Component("virtualAccountHandler")
@Log4j2
public class VirtualAccountHandlerImpl implements VirtualAccountHandler {
	
	@Resource
	private VirtualAccountService virtualAccountService;
	
	@Resource
	private LedgerBookVirtualAccountHandler ledgerBookVirtualAccountHandler;
	
	@Override
	public VirtualAccount refreshVirtualAccountBalance(String ledgerBookNo, String virtualAccountNo,
			String oldVirutalAccountVersion) {
		
		VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByVirtualAccountNo(virtualAccountNo);
		if(virtualAccount==null){
			throw new VirtualAccountNotExsitException();
		}
		
		BigDecimal balance = ledgerBookVirtualAccountHandler.get_balance_of_customer(ledgerBookNo, virtualAccount.getOwnerUuid());
		if(balance.compareTo(virtualAccount.getTotalBalance())==0){
			return virtualAccount;
		}
		virtualAccountService.updateBalance(virtualAccount.getVirtualAccountUuid(), balance, oldVirutalAccountVersion, new Date());
		return virtualAccountService.getVirtualAccountByVirtualAccountUuid(virtualAccount.getVirtualAccountUuid());
	
	}

 
}