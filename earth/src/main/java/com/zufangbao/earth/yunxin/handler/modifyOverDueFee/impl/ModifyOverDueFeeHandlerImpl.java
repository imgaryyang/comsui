package com.zufangbao.earth.yunxin.handler.modifyOverDueFee.impl;

import com.zufangbao.earth.yunxin.handler.modifyOverDueFee.ModifyOverDueFeeHandler;
import com.zufangbao.sun.api.model.deduct.RepaymentType;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.service.OrderService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.model.OverdueChargesModifyModel;
import com.zufangbao.sun.yunxin.handler.ExtraChargeSnapShotHandler;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanExtraChargeHandler;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanValuationHandler;
import com.zufangbao.sun.yunxin.handler.UpdateAssetStatusLedgerBookHandler;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component("modifyOverDueFeeHandler")
public class ModifyOverDueFeeHandlerImpl implements ModifyOverDueFeeHandler {

	@Autowired
	private RepaymentPlanService repaymentPlanService;
	
	@Autowired
	private LedgerBookService ledgerBookService;
	
	@Autowired
	private  FinancialContractService financialContractService;
	
	@Autowired
	private RepaymentPlanExtraChargeHandler repaymentPlanExtraChargeHandler;
	
	@Autowired
	private UpdateAssetStatusLedgerBookHandler updateAssetStatusLedgerBookHandler;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private RepaymentPlanValuationHandler repaymentPlanValuationHandler;
	
	@Autowired
	private ExtraChargeSnapShotHandler	overdueFeeLogHandler;
	
	private void updateAssetStatusIfClear(AssetSet assetSet, String ledgerBookNo) {
		updateAssetStatusLedgerBookHandler.updateAssetsFromLedgerBook(ledgerBookNo, Arrays.asList(assetSet.getAssetUuid()), assetSet.getActualRecycleDate(), RepaymentType.NORMAL, assetSet.getExecutingStatus());
		
	}

	private void updateAssetFairAmount(AssetSet repaymentPlan) {
		BigDecimal assetSetFairAmount = repaymentPlanService.get_principal_interest_and_extra_amount(repaymentPlan);
		repaymentPlan.update_asset_fair_value_and_valution_time(assetSetFairAmount);
		repaymentPlanService.update(repaymentPlan);
	}
	
	private void modifyOverDueFeeInLedgerBook(AssetSet repaymentPlan,
			Map<String, BigDecimal> account_delta_amount_map, FinancialContract financialContract){
		LedgerBook lederBook = ledgerBookService.getBookByBookNo(financialContract.getLedgerBookNo());
		repaymentPlanValuationHandler.modifyOverDueFeeInLedgerBook(repaymentPlan, financialContract, lederBook, account_delta_amount_map);
	}

	@Override
	public void modifyOverdueCharges(
			AssetSet repaymentPlan, OverdueChargesModifyModel overdueChargesDetail) {
		Map<String,BigDecimal> account_amount_map = new HashMap<String,BigDecimal>();
		account_amount_map.put(ChartOfAccount.SND_RECIEVABLE_LOAN_PENALTY, overdueChargesDetail.getOverdueFeePenalty());
		account_amount_map.put(ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION, overdueChargesDetail.getOverdueFeeObligation());
		account_amount_map.put(ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE, overdueChargesDetail.getOverdueFeeService());
		account_amount_map.put(ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE, overdueChargesDetail.getOverdueFeeOther());
		
		repaymentPlanExtraChargeHandler.modifyFeeInAssetSetExtraCharge(repaymentPlan, overdueChargesDetail.getOverdueFeePenalty(), ChartOfAccount.SND_RECIEVABLE_LOAN_PENALTY);
		repaymentPlanExtraChargeHandler.modifyFeeInAssetSetExtraCharge(repaymentPlan, overdueChargesDetail.getOverdueFeeObligation(), ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION);
		repaymentPlanExtraChargeHandler.modifyFeeInAssetSetExtraCharge(repaymentPlan, overdueChargesDetail.getOverdueFeeService(), ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE);
		repaymentPlanExtraChargeHandler.modifyFeeInAssetSetExtraCharge(repaymentPlan, overdueChargesDetail.getOverdueFeeOther(), ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE);
		FinancialContract financialContract = financialContractService.getFinancialContractBy(repaymentPlan.getFinancialContractUuid());
		modifyOverDueFeeInLedgerBook(repaymentPlan,account_amount_map, financialContract);
		updateAssetFairAmount(repaymentPlan);
		updateAssetStatusIfClear(repaymentPlan, financialContract.getLedgerBookNo());
		
		overdueFeeLogHandler.saveOverdueFeeLog(repaymentPlan.getAssetUuid(),repaymentPlan.getSingleLoanContractNo(),overdueChargesDetail);
	}
	
}

