package com.suidifu.bridgewater.handler.impl.single.v2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zufangbao.sun.api.model.deduct.PaymentChannelAndSignUpInfo;
import com.zufangbao.sun.entity.financial.PaymentChannelSummaryInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.demo2do.core.utils.BeanUtils;
import com.suidifu.bridgewater.handler.DeductApplicationBusinessHandler;
import com.suidifu.bridgewater.handler.single.v2.DeductApplicationStandardHandler;
import com.suidifu.bridgewater.model.v2.StandardDeductModel;
import com.suidifu.matryoshka.snapshot.SandboxDataSet;
import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductRequestModel;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.v2.SignUp;
import com.zufangbao.sun.yunxin.entity.v2.SignUpStatus;
import com.zufangbao.sun.yunxin.service.v2.SignUpService;

/**
 * Created by zhenghangbo on 12/05/2017.
 */
@Component("normalPreSandboxHandler")
public class PreSandboxHandlerImpl extends AbstractSandboxDataSetHandler {


	@Autowired
    private DeductApplicationStandardHandler deductApplicationStandardHandler;
	
	@Autowired
	private DeductApplicationBusinessHandler deductApplicationBusinessHandler;
	
	@Autowired 
	private FinancialContractConfigurationService financialContractConfigurationService;
	
    @Autowired
    private SignUpService signUpService;

    @Autowired
	private DeductApplicationService deductApplicationService;

    @Autowired
    private FinancialContractService financialContractService;

    @Autowired
    private ContractService contractService;

    @Value("#{config['urlForEarthCallBack']}")
    private String urlForEarthCallBack;
    
    @Value("#{config['urlForJpmorganCallback']}")
    private String urlForJpmorganCallback;
	
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

        SignUp signUp =  signUpService.getSignUpByProNo(proNo, SignUpStatus.SUCCESS);

        HashMap<String,String> extraData = new HashMap<String,String>();
        extraData.put("signUp", JsonUtils.toJSONString(signUp));
        SandboxDataSet sandboxDataSet = new SandboxDataSet();
        sandboxDataSet.setExtraData(extraData);
        return sandboxDataSet;

    }

	@Override
	public SandboxDataSet getSandbox_for_signup(String accName, String accNo,
			SignUpStatus signUpStatus, String productCode) {
		
		List<SignUp> signUps = signUpService.getSignUps(accName, accNo, signUpStatus, productCode);
		
        HashMap<String,String> extraData = new HashMap<String,String>();
        extraData.put("signUps", JsonUtils.toJSONString(signUps));
        SandboxDataSet sandboxDataSet = new SandboxDataSet();
        sandboxDataSet.setExtraData(extraData);
        return sandboxDataSet;
	}

	@Override
	public boolean sendMessageToEarth(Object deductModel) {
		
		DeductRequestModel model = new DeductRequestModel();
	       
		BeanUtils.copyBean(model, (StandardDeductModel)deductModel);
		
		model.setUrlForMorganStanletCallback(urlForEarthCallBack);
		
		model.setUrlForJpmorganCallback(urlForJpmorganCallback);
    	
    	return deductApplicationBusinessHandler.sendMessageToEarth(model);
    	
	}

	@Override
	public Object conventStandardDeductModel(Map<String, String> delayPostRequestParams) {
		
		 StandardDeductModel deductModel = new StandardDeductModel(delayPostRequestParams);
         
         deductApplicationStandardHandler.dataPreCheckAndFillInformation(deductModel);
         
         return deductModel;
		
	}

	@Override
	public String creatDeductApplicationReturnUuid(String ipAddress, Object deductModel, String creatorName) {
		
//		DeductApplication deductApplication = deductApplicationStandardHandler.createDeductApplicationByNewRule(ipAddress, (StandardDeductModel)deductModel, creatorName); //单独生成deductApplication
//		
//		return deductApplication.getDeductApplicationUuid();
		return null;
	}

	@Override
	public String getBankMerId(String financialContractUuid) {
		
//			String merId = financialContractConfigurationService.getFinancialContractConfigContentContent(financialContractUuid,FinancialContractConfigurationCode.MERID_CODE.getCode());
//		    
//			return merId;
		return null;
	}

	@Override
	public String getBankSignKey(String financialContractUuid) {
		
//		String signKey = financialContractConfigurationService.getFinancialContractConfigContentContent(financialContractUuid,FinancialContractConfigurationCode.SIGN_KEY.getCode());
//	    
//    	return signKey;
		
		return null;
	}

	@Override
	public SandboxDataSet get_sandbox_by_contract_uniqueId_contractNo(
			String uniqueId, String contractNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SandboxDataSet get_sandbox_for_apply_repurchase_by_contract_uniqueId_contractNo(
			String uniqueId, String contractNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SandboxDataSet get_sandbox_by_repaymentPlanNoList(
			String financialContractUuid, String contractUniqueId,
			List<String> repaymentPlanNoList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existRequest(String requestNo) {
		return deductApplicationService.getDeductApplicationByDeductRequestNo(requestNo) != null;
	}

	@Override
	public boolean existDeduct(String deductId) {
		return deductApplicationService.getDeductApplicationByDeductId(deductId) != null;
	}

	@Override
	public String getBankCodeByBankAliasName(String bankAliasName) {
		return null;
	}

	@Override
	public SandboxDataSet validFinancialContractAndContractForYunxin(Map<String, String> requestParam) {
		return null;
	}

	@Override
	public FinancialContract validFinancialContractForZhongHang(String productCode) {
		return null;
	}

	@Override
	public boolean validateRemittanceDetailsBankCode(Map<String, String> requestParam) {
		return false;
	}

	@Override
	public boolean validRemittanceForZhongHang(Map<String, String> requestParam) {
		return false;
	}

	@Override
	public boolean validRemittanceForYunXin(Map<String, String> requestParam) {
		return false;
	}

	@Override
	public List<PaymentChannelAndSignUpInfo> buildPaymentChannelAndSignUpInfos(List<PaymentChannelSummaryInfo> paymentChannelSummaryInfos, List<SignUp> signUps) {
		return null;
	}

	@Override
	public FinancialContract getFinancialContractByProductCode(String productCode) {

		return financialContractService.getUniqueFinancialContractBy(productCode);
	}

	@Override
	public Contract getContractByContract_uniqueId_contractNo(String uniqueId, String contractNo) {
		if (StringUtils.isNotEmpty(uniqueId))
			return contractService.getContractByUniqueId(uniqueId);
		if (StringUtils.isNotEmpty(contractNo))
			return contractService.getContractByContractNo(contractNo);

		return null;
	}

	@Override
	public AssetSet getActiveRepaymentPlanByRepaymentCode(String repaymentPlanCode) {
		// TODO Auto-generated method stub
		return null;
	}

}
