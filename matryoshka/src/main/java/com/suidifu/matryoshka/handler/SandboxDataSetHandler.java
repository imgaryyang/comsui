package com.suidifu.matryoshka.handler;

import java.util.List;
import java.util.Map;

import com.suidifu.matryoshka.snapshot.SandboxDataSet;
import com.zufangbao.sun.api.model.deduct.PaymentChannelAndSignUpInfo;
import com.zufangbao.sun.entity.bank.v2.PedestrianBankCode;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.PaymentChannelSummaryInfo;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.remittance.ExecutionStatus;
import com.zufangbao.sun.yunxin.entity.v2.SignUp;
import com.zufangbao.sun.yunxin.entity.v2.SignUpStatus;
import com.zufangbao.sun.yunxin.handler.ValidatorHandler;

/**
 * Created by louguanyang on 2017/5/9.
 */
public interface SandboxDataSetHandler {
	
		SandboxDataSet get_sandbox_by_contract_uniqueId_contractNo(String uniqueId, String contractNo);

	    SandboxDataSet get_sandbox_for_apply_repurchase_by_contract_uniqueId_contractNo(String uniqueId, String contractNo);

	    SandboxDataSet get_sandbox_by_repaymentPlanNoList(String financialContractUuid, String contractUniqueId, List<String> repaymentPlanNoList);

	    SandboxDataSet get_sandbox_for_customize_data(Map<String,String> params);

		boolean existSignUpAndPutPaymentInstitutionsInPostRequest(String accName,String accNo, String productCode, String paymentInstitution, Map<String, String> postRequest);

	PedestrianBankCode existBank(String bankName, int headQuarter);

	ExecutionStatus getStatusFromRemittanceApplicationDetail(String accNo, String accName);

	    SandboxDataSet getSandbox_for_signup(String proNo);

	    SandboxDataSet getSandbox_for_signup(String accName, String accNo, SignUpStatus signUpStatus, String productCode);

	    String checkRemittanceDetail(Map<String,Object> remittanceDetail);
	    
	    FinancialContract getFinancialContractByProductCode(String productCode);

	    Contract getContractByContract_uniqueId_contractNo(String uniqueId, String contractNo);

	    AssetSet getActiveRepaymentPlanByRepaymentCode(String repaymentPlanCode);

	    String getBankMerId(String financialContractUuid );

	    String getBankSignKey(String financialContractUuid);

	    public boolean sendMessageToEarth(Object deductModel);

	    public Object conventStandardDeductModel(Map<String, String> delayPostRequestParams);

	    public String creatDeductApplicationReturnUuid(String ipAdderss,Object deductModel,String creatorName);

	    public default Object getPaymentChannelAndSignUpInfoBy(String financialContractUuid,Object signUpList){return null;}

	    public default SandboxDataSet get_sandbox_for_asset_set(String contractUuid,int currentPeriod,int assetSetActiveStatus ) {return null;}

		public boolean existRequest(String requestNo);

        public boolean existDeduct(String deductId);

		default ValidatorHandler getValidatorHandler(){return  null;}

		default Map<String, String> getCardBinMap() {
			throw new UnsupportedOperationException();
		}


	    public String getBankCodeByBankAliasName(String bankAliasName);

	    public SandboxDataSet validFinancialContractAndContractForYunxin(Map<String, String> requestParam);

	    public FinancialContract validFinancialContractForZhongHang(String productCode);

	    public boolean validateRemittanceDetailsBankCode(Map<String, String> requestParam);

	    public boolean validRemittanceForZhongHang(Map<String, String> requestParam);

	    public boolean validRemittanceForYunXin(Map<String, String> requestParam);

	    public List<PaymentChannelAndSignUpInfo> buildPaymentChannelAndSignUpInfos(List<PaymentChannelSummaryInfo> paymentChannelSummaryInfos,List<SignUp> signUps);

	    public default SandboxDataSet validFinancialContractForTransferAccounts(Map<String, String> requestParam) {return null;};

}
