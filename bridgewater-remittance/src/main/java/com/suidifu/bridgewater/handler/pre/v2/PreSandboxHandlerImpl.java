package com.suidifu.bridgewater.handler.pre.v2;

import java.util.List;
import java.util.Map;

import com.zufangbao.sun.api.model.deduct.PaymentChannelAndSignUpInfo;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.PaymentChannelSummaryInfo;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.v2.SignUp;
import com.zufangbao.sun.yunxin.service.CardBinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suidifu.matryoshka.snapshot.SandboxDataSet;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.yunxin.entity.v2.SignUpStatus;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import com.zufangbao.sun.yunxin.service.remittance.RemittanceBlackListService;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.SEND_RECEIVE_CODE_FOR_REMITTANCE;

/**
 * Created by zhenghangbo on 12/05/2017.
 */
@Component("normalPreSandboxHandler")
public class PreSandboxHandlerImpl extends AbstractSandboxDataSetHandler {
	
	@Autowired
	private IRemittanceApplicationService iRemittanceApplicationService;
	
	@Autowired
	private RemittanceBlackListService remittanceBlackListService;

	@Autowired
	private CardBinService cardBinService;

    @Override
    public SandboxDataSet get_sandbox_for_customize_data(Map<String, String> params) {
        return null;
    }

	@Override
	public boolean existSignUpAndPutPaymentInstitutionsInPostRequest(String accName, String accNo, String productCode, String paymentInstitution, Map<String, String> postRequest) {
		return false;
	}

	@Override
    public SandboxDataSet getSandbox_for_signup(String proNo) {
        return null;
    }

	@Override
	public SandboxDataSet getSandbox_for_signup(String accName, String accNo, SignUpStatus signUpStatus, String productCode) {
		return null;
	}

	@Override
	public SandboxDataSet get_sandbox_by_contract_uniqueId_contractNo(String uniqueId, String contractNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SandboxDataSet get_sandbox_for_apply_repurchase_by_contract_uniqueId_contractNo(String uniqueId,
			String contractNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SandboxDataSet get_sandbox_by_repaymentPlanNoList(String financialContractUuid, String contractUniqueId,
			List<String> repaymentPlanNoList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FinancialContract getFinancialContractByProductCode(String productCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Contract getContractByContract_uniqueId_contractNo(String uniqueId, String contractNo) {
		return null;
	}

	@Override
	public AssetSet getActiveRepaymentPlanByRepaymentCode(String repaymentPlanCode) {
		return null;
	}

	@Override
	public String getBankMerId(String financialContractUuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBankSignKey(String financialContractUuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMessageToEarth(Object deductModel) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object conventStandardDeductModel(Map<String, String> delayPostRequestParams) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String creatDeductApplicationReturnUuid(String ipAdderss, Object deductModel, String creatorName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existRequest(String requestNo) {
		return false;
	}

	@Override
	public boolean existDeduct(String deductId) {
		return false;
	}

	@Override
	public String getBankCodeByBankAliasName(String bankAliasName) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean validRemittanceForZhongHang(Map<String, String> requestParam) {

		String contractUniqueId = requestParam.get(SEND_RECEIVE_CODE_FOR_REMITTANCE.UNIQUE_ID);

		//该贷款合同下存在处理中或已成功的放款请求
		if(iRemittanceApplicationService.existsProcessingOrSuccessedRemittanceApplication(contractUniqueId)) {
			throw new ApiException(ApiResponseCode.HAS_EXIST_PROCESSING_OR_SUCCESSED_REMITTANCE);
		}
		
		return true;
	}


	@Override
	public boolean validRemittanceForYunXin(Map<String, String> requestParam) {

		String contractUniqueId = requestParam.get(SEND_RECEIVE_CODE_FOR_REMITTANCE.UNIQUE_ID);
		//放款黑名单中存在该贷款合同uniqueId
		if(remittanceBlackListService.existUniqueId(contractUniqueId)) {
			throw new ApiException(ApiResponseCode.CONTRACT_UNIQUED_ID_EXIST_IN_REMITTANCE_BLACK_LIST);
		}
		//该贷款合同下存在处理中或已成功的放款请求
		if(iRemittanceApplicationService.existsProcessingOrSuccessedRemittanceApplication(contractUniqueId)) {
			throw new ApiException(ApiResponseCode.HAS_EXIST_PROCESSING_OR_SUCCESSED_REMITTANCE);
		}
		
		return true;
	}

	@Override
	public List<PaymentChannelAndSignUpInfo> buildPaymentChannelAndSignUpInfos(List<PaymentChannelSummaryInfo> paymentChannelSummaryInfos, List<SignUp> signUps) {
		return null;
	}

	@Override
	public SandboxDataSet validFinancialContractAndContractForYunxin(Map<String, String> requestParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FinancialContract validFinancialContractForZhongHang(String productCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validateRemittanceDetailsBankCode(Map<String, String> requestParam) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, String> getCardBinMap() {
    	return cardBinService.getCachedCardBins();
	}
}
