/**
 *
 */
package com.suidifu.microservice.handler.impl;

import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.utils.StringUtils;
import com.suidifu.microservice.handler.DeductApplicationDetailHandler;
import com.suidifu.microservice.model.DeductDataContext;
import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductApplicationDetailInfoModel;
import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductRequestModel;
import com.zufangbao.sun.api.model.deduct.IsTotal;
import com.zufangbao.sun.api.model.deduct.RepaymentDetail;
import com.zufangbao.sun.api.model.deduct.RepaymentType;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplicationDetail;
import com.zufangbao.sun.yunxin.entity.model.api.ExtraChargeSpec;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wukai
 *
 */
@Component("deductApplicationDetailHandler")
public class DeductApplicationDetailHandlerImpl implements DeductApplicationDetailHandler {

	@Autowired
	private RepaymentPlanService repaymentPlanService;

	@Autowired
	private LedgerBookStatHandler ledgerBookStatHandler;

	@Autowired
	private FinancialContractService financialContractService;

	@Autowired
	private ContractService contractService;

	@Override
	public List<DeductApplicationDetail> generateByRepaymentDetailList(DeductRequestModel deductRequestModel,
                                                                       DeductApplication deductApplication, Contract contract, FinancialContract financialContract) {

		List<RepaymentDetail> repaymentDetailInfoList = parseRepaymentDetailJson(deductRequestModel);

		List<DeductApplicationDetail> deductApplicationDetailList = new ArrayList<>();
		List<RepaymentDetail> repaymentDetails = new ArrayList<>();
		for (RepaymentDetail repaymentDetail : repaymentDetailInfoList) {
			repaymentDetail.setRepaymentDetailUuid(UUID.randomUUID().toString());
			repaymentDetails.add(repaymentDetail);
			AssetSet repaymentPlan = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentDetail.getRepaymentPlanNo());

			createSingleDeductApplicationDetail(deductApplicationDetailList,deductApplication, repaymentDetail, ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_PRINCIPLE, IsTotal.DETAIL,
					repaymentDetail.getRepaymentPrincipal(),repaymentPlan);

			createSingleDeductApplicationDetail(deductApplicationDetailList,deductApplication, repaymentDetail, ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_INTEREST, IsTotal.DETAIL,
					repaymentDetail.getRepaymentInterest(),repaymentPlan);

			createSingleDeductApplicationDetail(deductApplicationDetailList,deductApplication, repaymentDetail, ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_TECH_FEE, IsTotal.DETAIL,
					repaymentDetail.getTechFee(),repaymentPlan);

			createSingleDeductApplicationDetail(deductApplicationDetailList,deductApplication, repaymentDetail, ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_LOAN_SERVICE_FEE, IsTotal.DETAIL,
					repaymentDetail.getLoanFee(),repaymentPlan);

			createSingleDeductApplicationDetail(deductApplicationDetailList,deductApplication, repaymentDetail, ChartOfAccount.TRD_RECIEVABLE_LOAN_ASSET_OTHER_FEE, IsTotal.DETAIL,
					repaymentDetail.getOtherFee(),repaymentPlan);

			if(repaymentDetail.getOverDueFeeDetail() != null && repaymentDetail.getOverDueFeeDetail().overDueFeeDetailRule() == 1){

				createSingleDeductApplicationDetail(deductApplicationDetailList,deductApplication, repaymentDetail,
						ChartOfAccount.SND_RECIEVABLE_LOAN_PENALTY, IsTotal.DETAIL,
						repaymentDetail.getOverDueFeeDetail().getPenaltyFee(),repaymentPlan);
				createSingleDeductApplicationDetail(deductApplicationDetailList,deductApplication, repaymentDetail,
						ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION, IsTotal.DETAIL,
						repaymentDetail.getOverDueFeeDetail().getLatePenalty(),repaymentPlan);
				createSingleDeductApplicationDetail(deductApplicationDetailList,deductApplication, repaymentDetail,
						ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE, IsTotal.DETAIL,
						repaymentDetail.getOverDueFeeDetail().getLateFee(),repaymentPlan);
				createSingleDeductApplicationDetail(deductApplicationDetailList,deductApplication, repaymentDetail,
						ChartOfAccount.TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE, IsTotal.DETAIL,
						repaymentDetail.getOverDueFeeDetail().getLateOtherCost(),repaymentPlan);
			}

			createSingleDeductApplicationDetailTotalOverDueFee(deductApplicationDetailList,deductApplication, repaymentDetail, IsTotal.DETAIL, repaymentDetail.getOverDueFeeDetail().getTotalOverdueFee(),repaymentPlan);
			BigDecimal caclAccountReceivableAmount =calcAccountReceivableAmount(deductRequestModel, repaymentDetail, contract, financialContract,repaymentPlan);
			createSingleDeductApplicationDetailTotalReceivableAmount(deductApplicationDetailList,deductApplication, repaymentDetail, IsTotal.TOTAL,
					caclAccountReceivableAmount,repaymentPlan);

			createSingleDeductApplicationDetailTotalAmount(deductApplicationDetailList,deductApplication, repaymentDetail, IsTotal.DETAIL,
					repaymentDetail.getTotalAmount(),repaymentPlan);


		}
		deductRequestModel.setRepayDetailInfo(JsonUtils.toJsonString(repaymentDetails));
		return deductApplicationDetailList;
	}

	@Override
	public void getDeductApplicationDetailInfoModel(DeductDataContext deductDataContext) {

		Contract contract = contractService.getContractByUnigueIdOrContractNo(deductDataContext.getDeductRequestModel().getUniqueId(),
				deductDataContext.getDeductRequestModel().getContractNo());
		FinancialContract financialContract = financialContractService.getFinancialContractBy(deductDataContext.getDeductRequestModel().getFinancialContractUuid());
		DeductRequestModel deductRequestModel  = deductDataContext.getDeductRequestModel();
		List<RepaymentDetail> repaymentDetailInfoList = JsonUtils.parseArray(deductRequestModel.getRepayDetailInfo(),RepaymentDetail.class);

		Map<String, DeductApplicationDetailInfoModel> deductApplicationDetailInfoMap = new HashMap<>();

		for (RepaymentDetail repaymentDetail : repaymentDetailInfoList) {

			AssetSet repaymentPlan = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentDetail.getRepaymentPlanNo());
			if (repaymentPlan == null){
				continue;
			}
			BigDecimal caclAccountReceivableAmount =calcAccountReceivableAmount(deductRequestModel, repaymentDetail, contract, financialContract,repaymentPlan);

			DeductApplicationDetailInfoModel deductApplicationDetailInfoModel = new DeductApplicationDetailInfoModel(repaymentPlan.getAssetUuid(),
					caclAccountReceivableAmount, financialContract.getFinancialContractUuid(), contract.getUniqueId(), repaymentPlan.getSingleLoanContractNo());


			deductApplicationDetailInfoMap.put(repaymentDetail.getRepaymentDetailUuid(), deductApplicationDetailInfoModel);
		}

		deductDataContext.setDeductApplicationDetailInfoMap(deductApplicationDetailInfoMap);

	}

	@Override
	public List<DeductApplicationDetail> generateByRepaymentDetailListV(DeductRequestModel deductRequestModel, DeductApplication deductApplication, String contractUuid, String financialContractUuid) {
		if (StringUtils.isEmpty(financialContractUuid) || StringUtils.isEmpty(contractUuid)){
			return new ArrayList<>();
		}
		FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
		Contract contract = contractService.getContract(contractUuid);

		return generateByRepaymentDetailList( deductRequestModel, deductApplication, contract, financialContract) ;
	}


	private BigDecimal calcAccountReceivableAmount(DeductRequestModel commandModel,
                                                   RepaymentDetail repaymentDetail, Contract contract, FinancialContract financialContract, AssetSet repaymentPlan) {

		if(null == repaymentPlan) {
			return BigDecimal.ZERO;
		}

		boolean isIncludeUnearned = (RepaymentType.fromValue(commandModel.getRepaymentType()) == RepaymentType.ADVANCE)? true:false;

		return ledgerBookStatHandler.unrecovered_asset_snapshot(financialContract.getLedgerBookNo(), repaymentPlan.getAssetUuid(), contract.getCustomer().getCustomerUuid(), isIncludeUnearned);
	}

	private void createSingleDeductApplicationDetailTotalOverDueFee(List<DeductApplicationDetail> deductApplicationDetailList ,
                                                                    DeductApplication deductApplication,
                                                                    RepaymentDetail repaymentDetail, IsTotal isTotal,
                                                                    BigDecimal totalOverdueFee, AssetSet repaymentPlan) {

		if(null == totalOverdueFee) {
			return ;
		}

		//金额小于等于零不生成明细记录
		if(null != totalOverdueFee && totalOverdueFee.compareTo(BigDecimal.ZERO) < 1){
			return ;
		}
		DeductApplicationDetail deductApplicationDetail = generateSingleDeductApplicationDetail(deductApplication,
				repaymentDetail, isTotal, totalOverdueFee, ExtraChargeSpec.TOTAL_OVERDUE_FEE,repaymentPlan);

		deductApplicationDetailList.add(deductApplicationDetail);

	}


	private DeductApplicationDetail generateSingleDeductApplicationDetail(DeductApplication deductApplication,
                                                                          RepaymentDetail repaymentDetail, IsTotal isTotal, BigDecimal totalOverdueFee, String firstAccountName, AssetSet repaymentPlan) {

		DeductApplicationDetail deductApplicationDetail = new DeductApplicationDetail(deductApplication,
				repaymentDetail, isTotal, totalOverdueFee, repaymentDetail.getCurrentPeriod());

		if(null == repaymentPlan) {
			deductApplicationDetail = new DeductApplicationDetail(deductApplication,
					repaymentDetail, isTotal, totalOverdueFee, repaymentDetail.getCurrentPeriod());
		}else {
			deductApplicationDetail = new DeductApplicationDetail(deductApplication,
					repaymentDetail, isTotal, totalOverdueFee, repaymentPlan.getAssetUuid());
		}

		if(null != firstAccountName) {

			deductApplicationDetail.setFirstAccountName(firstAccountName);
		}


		return deductApplicationDetail;
	}

	private void createSingleDeductApplicationDetailTotalReceivableAmount(List<DeductApplicationDetail> deductApplicationDetailList , DeductApplication deductApplication,
                                                                          RepaymentDetail repaymentDetail, IsTotal total, BigDecimal caclAccountReceivableAmount, AssetSet repaymentPlan) {

		if(null == caclAccountReceivableAmount) {
			return;
		}


		if(caclAccountReceivableAmount.compareTo(BigDecimal.ZERO) < 1){
			return ;
		}
		DeductApplicationDetail deductApplicationDetail = generateSingleDeductApplicationDetail(deductApplication, repaymentDetail, total, caclAccountReceivableAmount, ExtraChargeSpec.TOTAL_RECEIVABEL_AMOUNT,repaymentPlan);

		deductApplicationDetailList.add(deductApplicationDetail);
	}

	private void createSingleDeductApplicationDetailTotalAmount(List<DeductApplicationDetail> deductApplicationDetailList , DeductApplication deductApplication,
                                                                RepaymentDetail repaymentDetail, IsTotal total, BigDecimal caclAccountReceivableAmount, AssetSet repaymentPlan) {

		if(null == caclAccountReceivableAmount) {
			return;
		}


		if(caclAccountReceivableAmount.compareTo(BigDecimal.ZERO) < 1){
			return ;
		}
		DeductApplicationDetail deductApplicationDetail = generateSingleDeductApplicationDetail(deductApplication, repaymentDetail, total, caclAccountReceivableAmount, ExtraChargeSpec.TOTAL_AMOUNT,repaymentPlan);

		deductApplicationDetailList.add(deductApplicationDetail);
	}


	private List<RepaymentDetail> parseRepaymentDetailJson(DeductRequestModel deductRequestModel){

		return JsonUtils.parseArray(deductRequestModel.getRepayDetailInfo(),RepaymentDetail.class);
	}
	private void createSingleDeductApplicationDetail(List<DeductApplicationDetail> deductApplicationDetailList, DeductApplication deductApplication,
                                                     RepaymentDetail repaymentDetail, String chartString, IsTotal isTotal, BigDecimal amount, AssetSet repaymentPlan) {
		//金额小于等于零不生成明细记录
		if(null == amount || amount.compareTo(BigDecimal.ZERO) < 1){
			return;
		}
		DeductApplicationDetail deductApplicationDetail = generateSingleDeductApplicationDetail(deductApplication, repaymentDetail, isTotal, amount, null,repaymentPlan);

		deductApplicationDetail.copyTAccount(ChartOfAccount.EntryBook().get(chartString));

		deductApplicationDetailList.add(deductApplicationDetail);
	}

}
