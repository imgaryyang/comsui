package com.zufangbao.earth.yunxin.api.handler.impl;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.yunxin.api.handler.ThirdPartVoucherFrontEndShowHandler;
import com.zufangbao.sun.api.model.deduct.DeductApplicationExecutionStatus;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.service.ContractAccountService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import com.zufangbao.sun.yunxin.entity.model.deduct.ThirdPartVoucherDeductShowModel;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.JournalVoucher;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocument;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentDetail;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.ThirdPartVoucherSourceMapSpec;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.JournalVoucherService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentDetailService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentService;
import com.zufangbao.wellsfargo.thirdpartvoucher.model.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/** 
* @author 作者 zhenghangbo
* @version 创建时间：Nov 23, 2016 4:30:59 PM 
* 类说明 
*/

@Component("ThirdPartVoucherFrontEndShowHandler")
public class ThirdPartVoucherFrontEndShowHandlerImpl implements ThirdPartVoucherFrontEndShowHandler {

	
	@Autowired
	private JournalVoucherService journalVoucherService;
	
	@Autowired
	private GenericDaoSupport genericDaoSupport;
	
	@Autowired
	private SourceDocumentDetailService  sourceDocumentDetailService;
	
	@Autowired
	private SourceDocumentService sourceDocumentService;
	
	@Autowired
	private DeductApplicationService  deductApplicationService;
	
	@Autowired
	private DeductPlanService deductPlanService;
	
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	
	@Autowired
	private ContractAccountService contractAccountService;
	
	@Autowired
	private FinancialContractService financialContractService;
	
	@Autowired
	private LedgerBookStatHandler ledgerBookStatHandler;
	

	@Override
	public int countThirdVocuherSize(ThirdPartVoucherQueryModel queryModel){
		if(vaildThirdPartQueryModel(queryModel) == false){
			return 0;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		String querySql = journalVoucherService.gengerateQueryJournalVoucherSentence(queryModel, params);
		return this.genericDaoSupport.count(querySql, params);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ThirdPartVoucherQueryShowModel> getThirdPartVoucherShowModel(ThirdPartVoucherQueryModel queryModel,Page page){
		if(vaildThirdPartQueryModel(queryModel) == false){
			return Collections.EMPTY_LIST;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		String querySql = journalVoucherService.gengerateQueryJournalVoucherSentence(queryModel, params);
		
		List<JournalVoucher> journalVouchers =  new ArrayList<JournalVoucher>();
		if (page == null) {
			journalVouchers =  this.genericDaoSupport.searchForList(querySql, params);
		} else {
			journalVouchers = this.genericDaoSupport.searchForList(querySql, params, page.getBeginIndex(), page.getEveryPage());
     	}
		return castListShowModel(journalVouchers);
	}
	

	@Override
	public ThirdPartVoucherDetailShowModel fetch_detail_show_model(String journalVoucherUuid) {
		/***********prepare cast data*************/
		JournalVoucher journalVoucher = journalVoucherService.getJournalVoucherByVoucherUUID(journalVoucherUuid);
		// FIXME
		SourceDocumentDetail sourceDocumentDetail = sourceDocumentDetailService.getSourceDocumentDetail(journalVoucher.getSourceDocumentUuid());
		SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentBy(journalVoucher.getSourceDocumentIdentity());
		DeductApplication deductApplication = deductApplicationService.getDeductApplicationByDeductApplicationUuid(sourceDocument.getOutlierDocumentUuid());
		DeductPlan deductPlan = deductPlanService.getDeductPlanByDeductApplicationUuid(sourceDocument.getOutlierDocumentUuid(),DeductApplicationExecutionStatus.SUCCESS);
		AssetSet repaymentPlan = repaymentPlanService.getRepaymentPlanByRepaymentCode(sourceDocumentDetail.getRepaymentPlanNo());
		Contract contract  = repaymentPlan.getContract();
		ContractAccount contractAccount = contractAccountService.getTheEfficientContractAccountBy(contract);
		FinancialContract financialContract = financialContractService.getFinancialContractBy(journalVoucher.getRelatedBillContractInfoLv1());
		BigDecimal unrecoveredAssetAmount =  ledgerBookStatHandler.unrecovered_asset_snapshot(financialContract.getLedgerBookNo(), repaymentPlan.getAssetUuid(), contract.getCustomerUuid(), true);

		return castDetailShowModel(journalVoucher,sourceDocumentDetail,sourceDocument,deductApplication,deductPlan,repaymentPlan,contract,contractAccount,financialContract,unrecoveredAssetAmount);
	}
	
	
	
	private ThirdPartVoucherDetailShowModel castDetailShowModel(JournalVoucher journalVoucher, SourceDocumentDetail sourceDocumentDetail,
			SourceDocument sourceDocument, DeductApplication deductApplication, DeductPlan deductPlan,
			AssetSet repaymentPlan, Contract contract, ContractAccount contractAccount, FinancialContract financialContract, BigDecimal unrecoveredAssetAmount) {
		ThirdPartVoucherDetailShowModel thirdPartShowModel = new ThirdPartVoucherDetailShowModel(); 
		thirdPartShowModel.setThirdPartVoucherLoanInformation(new ThirdPartVoucherLoanInformation(repaymentPlan,contract));
		thirdPartShowModel.setThirdPartVoucherVoucherInformation(new ThirdPartVoucherVoucherInformation(journalVoucher,deductApplication,deductPlan));
		thirdPartShowModel.setThirdPartVoucherCustomerInformation(new ThirdPartVoucherCustomerInformation(contract,contractAccount));
		thirdPartShowModel.setThirdPartVoucherFinanceInformation(new ThirdPartVoucherFinanceInformation(deductApplication,deductPlan));
		thirdPartShowModel.setThirdPartVoucherBusinessInformation(new ThirdPartVoucherBusinessInformation(repaymentPlan,contract,journalVoucher,financialContract,unrecoveredAssetAmount));
		return thirdPartShowModel;
	}


	private List<ThirdPartVoucherQueryShowModel> castListShowModel(List<JournalVoucher> journalVouchers) {
		
		journalVouchers.stream().map(jv -> assembleShowModel(jv));
		List<ThirdPartVoucherQueryShowModel> showModels = new ArrayList<ThirdPartVoucherQueryShowModel>();
	    for(JournalVoucher journalVocuher:journalVouchers){
	    	showModels.add(assembleShowModel(journalVocuher));
	    }
		return showModels;
		
	}

	private ThirdPartVoucherQueryShowModel assembleShowModel(JournalVoucher jv) {
		// FIXME
		SourceDocumentDetail sourceDocumentDetail =  sourceDocumentDetailService.getSourceDocumentDetail(jv.getSourceDocumentUuid());
		ThirdPartVoucherQueryShowModel showModel = new ThirdPartVoucherQueryShowModel(jv,sourceDocumentDetail);
		return showModel;
	}

	private boolean vaildThirdPartQueryModel(ThirdPartVoucherQueryModel queryModel) {
        return !(CollectionUtils.isEmpty(queryModel.getCashFlowChannelTypeEnumList())
                || CollectionUtils.isEmpty(queryModel.getDeductVoucherSourceEnumList())
                || CollectionUtils.isEmpty(queryModel.getFinancialContractUuidList())
                || CollectionUtils.isEmpty(queryModel.getJournalVoucherStatusEnumList())
                || CollectionUtils.isEmpty(queryModel.getRepaymentTypeEnumList()));
    }


	@Override
	public List<ThirdPartVoucherDeductShowModel> fetchThirdPartVoucherFrontEndShowModelByDeductInformation(
			String deductId) {
		
		DeductApplication deductApplication = deductApplicationService.getDeductApplicationByDeductId(deductId);
		SourceDocument sourceDocument =sourceDocumentService.getSourceDocumentByDeductInformation(deductApplication.getDeductApplicationUuid());
		
		if(sourceDocument == null){
			return Collections.emptyList();
		}
		List<SourceDocumentDetail> sourceDocumentDetails = sourceDocumentDetailService.getValidDeductSourceDocumentDetailsBySourceDocumentUuid(sourceDocument.getSourceDocumentUuid());
		List<ThirdPartVoucherDeductShowModel> resultShowModels = new ArrayList<ThirdPartVoucherDeductShowModel>();
 		for(SourceDocumentDetail sourceDocumentDetail:sourceDocumentDetails){
			JournalVoucher journalVoucher = journalVoucherService.getInforceJournalVoucher(sourceDocumentDetail.getUuid());
			resultShowModels.add(generateThirdPartVoucherShowModel(journalVoucher,sourceDocumentDetail));
		}
		return resultShowModels;
	}


	private ThirdPartVoucherDeductShowModel generateThirdPartVoucherShowModel(JournalVoucher journalVoucher,
			SourceDocumentDetail sourceDocumentDetail) {
		return new ThirdPartVoucherDeductShowModel(journalVoucher.getJournalVoucherUuid(), journalVoucher.getJournalVoucherNo(),journalVoucher.getSourceDocumentLocalPartyAccount(),
				sourceDocumentDetail.getPaymentBank(),journalVoucher.getSourceDocumentCounterPartyName(),
				journalVoucher.getSourceDocumentCounterPartyAccount(),journalVoucher.getBookingAmount(),"",
				journalVoucher.getThirdJournalVoucherType()==null?"":ThirdPartVoucherSourceMapSpec.repayTypeStringMap.get(journalVoucher.getThirdJournalVoucherType().ordinal()),
				journalVoucher.getSecondJournalVoucherType()==null?"":ThirdPartVoucherSourceMapSpec.voucherSourceStringMap.get(journalVoucher.getSecondJournalVoucherType().ordinal()),
				journalVoucher.getCashFlowChannelType()==null?"":ThirdPartVoucherSourceMapSpec.cashFlowStringMap.get(journalVoucher.getCashFlowChannelType().ordinal()),
				journalVoucher.getStatus()==null?"":ThirdPartVoucherSourceMapSpec.voucherStatusStringMap.get(journalVoucher.getStatus().ordinal()));
	}

}
