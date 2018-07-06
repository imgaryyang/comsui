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
import com.zufangbao.sun.ledgerbook.LedgerBookHandler;
import com.zufangbao.sun.ledgerbookv2.handler.LedgerBookV2Handler;
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
import com.zufangbao.sun.yunxin.handler.*;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.suidifu.microservice.entity.JournalVoucher;
import com.suidifu.microservice.enume.CounterAccountType;
import com.suidifu.microservice.enume.JournalVoucherCheckingLevel;
import com.suidifu.microservice.enume.JournalVoucherStatus;
import com.suidifu.microservice.enume.JournalVoucherType;
import com.suidifu.microservice.enume.SecondJournalVoucherType;
import com.suidifu.microservice.exception.AssetRefundException;
import com.suidifu.microservice.model.ContractCategory;
import com.suidifu.microservice.model.ReconciliationContext;
import com.suidifu.microservice.service.JournalVoucherService;
import com.suidifu.owlman.microservice.handler.RefundAssetHandler;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * dcc
 */
@Component("refundAssetHandler")
@Log4j2
public class AssetRefundHandlerImpl implements RefundAssetHandler {

  @Resource
  private RepaymentPlanService repaymentPlanService;
  @Resource
  private RepaymentPlanHandler repaymentPlanHandler;
  @Resource
  private RepaymentPlanExtraChargeHandler repaymentPlanExtraChargeHandler;
  @Resource
  private FinancialContractService financialContractService;
  @Resource
  private LedgerBookService ledgerBookService;
  @Resource
  private RepaymentPlanValuationHandler repaymentPlanValuationHandler;
  @Resource
  private UpdateAssetStatusLedgerBookHandler updateAssetStatusLedgerBookHandler;
  @Resource
  private ExtraChargeSnapShotHandler overdueFeeLogHandler;
  @Resource
  private ContractService contractService;
  @Resource
  private RefundOrderService refundOrderService;
  @Resource
  private VirtualAccountService virtualAccountService;
  @Resource
  private VirtualAccountFlowService virtualAccountFlowService;
  @Resource
  private VirtualAccountHandler virtualAccountHandler;
  @Resource
  private JournalVoucherService JournalVoucherService;
  @Resource
  private LedgerBookHandler ledgerBookHandler;
  @Resource
  private SpecialAccountHandler specialAccountHandler;
  @Resource
  private LedgerBookV2Handler ledgerBookV2Handler;
  
  private Integer retryTimes = 3;
  
   @Override
   public void handleAssetRefund(String contractUuid, String refundOrderUuid, AssetRefundMode assetRefundMode,
		int priority) {
	   
	   RefundOrder refundOrder = refundOrderService.queryRefundOrderByRefundOrderUuid(refundOrderUuid);
	   
	   try {
		   
		   if(assetRefundMode == null) return ;
			
			AssetSet repaymentPlan = repaymentPlanService.getUniqueRepaymentPlanByUuid(assetRefundMode.getAssetUuid());
			if(refundOrder == null || repaymentPlan == null) {
				throw new AssetRefundException();
			}
			
			//入账资金校验
			List<RepaymentChargesDetail> amountClassifyList = repaymentPlanHandler.getRepaymentChargesDetailList(repaymentPlan);
			RepaymentChargesDetail actualChargesDetail = amountClassifyList.get(0);
			//校验入账资金与退款金额
			if(actualChargesDetail.checkRefundAmount(assetRefundMode)) {
				//异常 
				throw new InvalidLedgerException();
			}
			
			Contract contract = repaymentPlan.getContract();
	       VirtualAccount virtualAccount = virtualAccountService.create_if_not_exist_virtual_account(contract.getCustomerUuid(),
	       		repaymentPlan.getFinancialContractUuid(), contract.getUuid());
	       ExecutingStatus beforeExectingStatus = repaymentPlan.getExecutingStatus();
	       log.info("beforeExectingStatus ：["+beforeExectingStatus.getChinessName()+"] and contractUuid：["+contractUuid+"] and assetUuid：["+repaymentPlan.getAssetUuid()+"]");
			
	       FinancialContract financialContract = financialContractService.getFinancialContractBy(contract.getFinancialContractUuid());
	       LedgerBook ledgerBook = ledgerBookService.getBookByBookNo(financialContract.getLedgerBookNo());
	       
			//创建退款记录
	       log.info("create refundJournalVoucher ，refundOrderUuid：["+refundOrder.getUuid()+"]");
		   JournalVoucher journalVoucher = create_refund_record(refundOrder, contract, financialContract, repaymentPlan,virtualAccount);
		   log.info("create refundJournalVoucher ，refundOrderUuid：["+refundOrder.getUuid()+"] and refundJournalVoucherUuid：["+journalVoucher.getJournalVoucherUuid()+"]" );
			
			//资产实收回滚, 专户动账
			asset_refund_for_book_ledger(refundOrder, repaymentPlan, ledgerBook, financialContract,journalVoucher.getJournalVoucherUuid());
			
			//虚户余额增加  ，创建虚户流水
			refresh_virtual_account_add_virtual_account_flow(journalVoucher, contract.getCustomerUuid(), ledgerBook.getLedgerBookNo());
			
			//还款计划，贷款合同 状态，退款单状态
			update_asset_and_contract_status(repaymentPlan, beforeExectingStatus,refundOrder,ledgerBook.getLedgerBookNo());
		
	   	} catch (Exception e) {

			if(refundOrder.getRetriedNums() >= retryTimes && refundOrder.getRefundStatus() != RefundStatus.REFUNDED) {
				
				refundOrderService.updateRefundStatusByRefundOrderUuid(refundOrder.getUuid(), RefundStatus.ABNORMAL);
			}else {
				
				refundOrderService.updateRefundStatusByRefundOrderUuid(refundOrder.getUuid(), RefundStatus.REFUNDING);
			}
		
		    e.printStackTrace();
	    }
		
   }

   private void refresh_virtual_account_add_virtual_account_flow(JournalVoucher journalVoucher,String customerUuid,String ledgerBookNo) {
		
		VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(customerUuid);
		String oldVirtualAccountVersion = virtualAccount.getVersion();
		
		//refresh
		VirtualAccount refreshedVirtualAccount = virtualAccountHandler.refreshVirtualAccountBalance(
				ledgerBookNo, virtualAccount.getVirtualAccountNo(),
				oldVirtualAccountVersion);
		//虚户流水
		virtualAccountFlowService.addAccountFlow(journalVoucher.getJournalVoucherNo(), refreshedVirtualAccount,
				journalVoucher.getBookingAmount(), AccountSide.DEBIT,
				VirtualAccountTransactionType.INTER_ACCOUNT_BENEFICIARY, journalVoucher.getJournalVoucherUuid(), refreshedVirtualAccount.getTotalBalance());
		log.info("refresh  VirtualAccount and add virtualAccountFlow , virtualAccountNo is ["
				+ refreshedVirtualAccount.getVirtualAccountNo() + "]");
		
	}
	
	private void update_asset_and_contract_status(AssetSet repaymentPlan,ExecutingStatus beforeExectingStatus,RefundOrder refundOrder,String ledgerBookNo) {
		
		updateAssetStatusLedgerBookHandler.updateAssetsFromLedgerBook(ledgerBookNo, Arrays.asList(repaymentPlan.getAssetUuid()),
				repaymentPlan.getActualRecycleDate(), RepaymentType.NORMAL, ExecutingStatus.PROCESSING);
		
		//退款单状态   已退款
		refundOrder.updateRefundOrderStatus(RefundStatus.REFUNDED);
		refundOrderService.update(refundOrder);
	}
	
	private JournalVoucher create_refund_record(RefundOrder refundOrder,Contract contract,FinancialContract financialContract,
			AssetSet repaymentPlan,VirtualAccount borrowerVirtualAccount) {
		
     	//创建退款记录
		ReconciliationContext context = new ReconciliationContext(refundOrder.getAmount(), contract.getApp().getCompany(),
				JournalVoucherType.REFUND_VOUCHER, financialContract, contract, repaymentPlan,borrowerVirtualAccount,SecondJournalVoucherType.ASSET_REFUND_VOUCHER);
		context.resovleJournalVoucher(AccountSide.DEBIT, CounterAccountType.VirtualAccount,repaymentPlan.getAssetUuid());
		
     	JournalVoucher journalVoucher = new JournalVoucher();
		journalVoucher.createFromContext(context);
		journalVoucher.fill_voucher_and_booking_amount(context.getAssetSet().getAssetUuid(), "",
				refundOrder.getUuid(), context.getBookingAmount(), JournalVoucherStatus.VOUCHER_ISSUED, 
					JournalVoucherCheckingLevel.AUTO_BOOKING, context.getJournalVoucherType());
		ContractCategory contractCategory = extractContractCategory(context.getFinancialContract(),context.getContract(),context.getAssetSet() ,refundOrder.getRefundOrderNo());
		journalVoucher.fillBillContractInfo(contractCategory);
		journalVoucher.setSecondJournalVoucherType(SecondJournalVoucherType.ASSET_REFUND_VOUCHER);
		JournalVoucherService.saveOrUpdate(journalVoucher);
		
		return journalVoucher;
	}

    private void asset_refund_for_book_ledger(RefundOrder refundOrder,AssetSet repaymentPlan,LedgerBook ledgerBook,FinancialContract financialContract,String journalVoucherUuid) {
		
		Map<String, BigDecimal> account_amount_map = refundOrder.detailAccountAmounMap();
		
		//资产实收回滚
		//实收(优先级减实收:银行存款本金，独立账户本金)-    应收 +
		BigDecimal bankSavingAmount = BigDecimal.ZERO;
		BigDecimal bankSavingAmount2 = BigDecimal.ZERO;
		if (ledgerBookV2Handler.checkLegderBookVersion(ledgerBook)){
			bankSavingAmount = ledgerBookV2Handler.handle_refund_receivable_loan_asset(ledgerBook, account_amount_map, repaymentPlan,financialContract,journalVoucherUuid);
		}
		if (ledgerBookV2Handler.checkLedgerBookVersionV1(ledgerBook)){
			bankSavingAmount2 = ledgerBookHandler.handle_refund_receivable_loan_asset(ledgerBook, account_amount_map, repaymentPlan,financialContract,journalVoucherUuid);
		}

		//银行存款 +   独立账户-
		if (ledgerBookV2Handler.checkLegderBookVersion(ledgerBook)) {
			ledgerBookV2Handler.ledger_bank_saving_refund_asset(ledgerBook, repaymentPlan, financialContract, refundOrder,journalVoucherUuid,bankSavingAmount);
		}
		if (ledgerBookV2Handler.checkLedgerBookVersionV1(ledgerBook)){
			ledgerBookHandler.ledger_bank_saving_refund_asset(ledgerBook, repaymentPlan, financialContract, refundOrder,journalVoucherUuid,bankSavingAmount2);
		}

		//信托开关开着    专户动账
		//1为开
		if(true==financialContractService.isSpecialAccountConfigured(financialContract.getFinancialContractUuid())){
			AssetCategory assetCategory = AssetConvertor.convertAnMeiTuLoanAssetToAssetCategory(repaymentPlan);
			if (ledgerBookV2Handler.checkLegderBookVersion(ledgerBook)){
				ledgerBookV2Handler.clearing_assets(ledgerBook, account_amount_map, financialContract.getFinancialContractUuid(), assetCategory, com.zufangbao.sun.ledgerbook.AccountSide.DEBIT);
			}
			if (ledgerBookV2Handler.checkLedgerBookVersionV1(ledgerBook)) {
				ledgerBookHandler.clearing_assets_or_asset_refund(ledgerBook, account_amount_map, financialContract.getFinancialContractUuid(), assetCategory, com.zufangbao.sun.ledgerbook.AccountSide.DEBIT);
			}

			//还款户变动
			specialAccountHandler.update_asset_refund_financial_contract_account(financialContract.getFinancialContractUuid(), account_amount_map);
		}
		
	}

	private  ContractCategory extractContractCategory(FinancialContract financialContract,Contract contract,AssetSet repaymentPlan,String refundOrderNo)
	{
		ContractCategory contractCategory=new ContractCategory();
		contractCategory.setRelatedBillContractInfoLv1(financialContract.getUuid());
		contractCategory.setRelatedBillContractInfoLv2(contract.getUuid());

		contractCategory.setRelatedBillContractNoLv1(financialContract.getContractName());
		contractCategory.setRelatedBillContractNoLv2(contract.getContractNo());

		contractCategory.setRelatedBillContractNoLv4(refundOrderNo);
		contractCategory.setRelatedBillContractInfoLv3(repaymentPlan.getAssetUuid());
		contractCategory.setRelatedBillContractNoLv3(repaymentPlan.getSingleLoanContractNo());
		return contractCategory;
	}
  
}