package com.zufangbao.earth.yunxin.api.handler.impl;

import com.demo2do.core.utils.DateUtils;
import com.zufangbao.earth.yunxin.api.handler.RepaymentListApitHandler;
import com.zufangbao.earth.yunxin.api.model.RepaymentListDetail;
import com.zufangbao.earth.yunxin.api.model.query.RepaymentListQueryModel;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component("RepaymentListApiHandler")
public class RepaymentListApiHandlerImpl implements RepaymentListApitHandler {

	@Autowired
	private FinancialContractService financialContractService;

	@Autowired
	private DeductApplicationService deductApplicationService;
	
	@Autowired 
	private DeductPlanService deductPlanService;
	
	/**
	 *
	 * 生成还款清单列表：  
	 */
	@Override
	public List<RepaymentListDetail> queryRepaymentList(
			RepaymentListQueryModel queryModel) {
		String financialContractNo = queryModel.getFinancialContractNo();
		FinancialContract financialContract = queryFinancialContract(financialContractNo);

		List<RepaymentListDetail> deductRepaymentListDetails = generateDeductRepaymentListDetails(queryModel,financialContract);

		sortRepaymentListDetailByDeductDate(deductRepaymentListDetails);
		
		return deductRepaymentListDetails;
	}

	private List<RepaymentListDetail> generateDeductRepaymentListDetails(RepaymentListQueryModel queryModel,
			FinancialContract financialContract) {
		
		
		List<DeductPlan> pendingDeductPlans = deductPlanService.queryDeductPlanBy(financialContract,queryModel.getQueryStartDate(),queryModel.getQueryEndDate());
		Map<String, DeductApplication> deductApplicationMap = fetch_pending_deductApplication_map(pendingDeductPlans);
		
		List<RepaymentListDetail> deductPaymentListDetails = new ArrayList<RepaymentListDetail>();
		for(DeductPlan deductPlan:pendingDeductPlans){
			DeductApplication deductApplication = deductApplicationMap.get(deductPlan.getDeductApplicationUuid());
			deductPaymentListDetails.add(new RepaymentListDetail(deductPlan,deductApplication));
		}
		return deductPaymentListDetails;
	}

	private Map<String, DeductApplication> fetch_pending_deductApplication_map(List<DeductPlan> pendingDeductPlans) {
		List<String> deductApplicationUuidList = pendingDeductPlans.stream().map(dp -> dp.getDeductApplicationUuid()).collect(Collectors.toList()); 
		List<DeductApplication>  deductApplications = deductApplicationService.getDeductApplicationByUuid(deductApplicationUuidList);
		Map<String,DeductApplication> deductApplicationMap = deductApplications.stream().collect(Collectors.toMap(DeductApplication::getDeductApplicationUuid, da -> da));
		return deductApplicationMap;
	}

	private void sortRepaymentListDetailByDeductDate(
			List<RepaymentListDetail> repaymentListDetails) {
		Collections.sort(repaymentListDetails, new Comparator<RepaymentListDetail>() {
	        @Override
	        public int compare(RepaymentListDetail repaymentList1,RepaymentListDetail repaymentList2) {
	        	return  DateUtils.asDay(repaymentList1.getDeductDate()).compareTo(DateUtils.asDay(repaymentList2.getDeductDate()));
	        }
		
		});
	}

	private FinancialContract queryFinancialContract(String financialContractNo) {
		FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(financialContractNo);
		if (financialContract == null) {
			throw new ApiException(
					ApiResponseCode.FINANCIAL_CONTRACT_NOT_EXIST);
		}
		return financialContract;
	}

}