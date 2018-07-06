package com.suidifu.matryoshka.handler.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.service.ContractSnapshotService;
import com.suidifu.matryoshka.service.FinancialContractSnapshotService;
import com.suidifu.matryoshka.service.RepaymentPlanSnapshotService;
import com.suidifu.matryoshka.service.RepurchaseSnapshotService;
import com.suidifu.matryoshka.snapshot.*;
import com.zufangbao.sun.api.model.deduct.PaymentChannelAndSignUpInfo;
import com.zufangbao.sun.entity.bank.v2.PedestrianBankCode;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.PaymentChannelSummaryInfo;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.RepaymentPlanExtraData;
import com.zufangbao.sun.yunxin.entity.remittance.ExecutionStatus;
import com.zufangbao.sun.yunxin.entity.v2.SignUp;
import com.zufangbao.sun.yunxin.entity.v2.SignUpStatus;
import com.zufangbao.sun.yunxin.service.RepaymentPlanExtraDataService;

/**
 * 沙盒数据Handler
 * Created by louguanyang on 2017/5/9.
 */
@Component("sandboxDataSetHandler")
public class SandboxDataSetHandlerImpl implements SandboxDataSetHandler {

    @Autowired
    private ContractSnapshotService contractService;

    @Autowired
    private FinancialContractSnapshotService financialContractService;

    @Autowired
    private RepaymentPlanSnapshotService repaymentPlanService;

    @Autowired
    private RepurchaseSnapshotService repurchaseService;

    @Autowired
    private RepaymentPlanExtraDataService repaymentPlanExtraDataService;

    private static final Log LOGGER = LogFactory.getLog(SandboxDataSetHandlerImpl.class);

    @Override
    public SandboxDataSet get_sandbox_by_contract_uniqueId_contractNo(String uniqueId, String contractNo) {
        try {
            ContractSnapshot contractSnapshot = contractService.get_contractSnapshot_by_uniqueId_or_contractNo(uniqueId, contractNo);
            String financialContractUuid = contractSnapshot.getFinancialContractUuid();
            FinancialContractSnapshot financialContractSnapshot = financialContractService.get_financialcontractsnapshot_by_financialcontractuuid(financialContractUuid);
            List<PaymentPlanSnapshot> assetSetSnapshotList = repaymentPlanService.get_all_unclear_assetSetSnapshot_list(contractSnapshot.getUuid());
            return new SandboxDataSet(financialContractSnapshot, contractSnapshot,null, assetSetSnapshotList);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    明天待确认
    @Override
    public SandboxDataSet get_sandbox_for_apply_repurchase_by_contract_uniqueId_contractNo(String uniqueId, String contractNo){
        try {
            Contract contract = contractService.getContractBy(uniqueId, contractNo);
            ContractSnapshot contractSnapshot = contractService.getContractSnapshot(contract);
            String financialContractUuid = contractSnapshot.getFinancialContractUuid();
            FinancialContractSnapshot financialContractSnapshot = financialContractService.get_financialcontractsnapshot_by_financialcontractuuid(financialContractUuid);
            RepurchaseDocSnapshot repurchaseDocSnapshot = repurchaseService.getEffectiveRepurchaseDocSnapshotBy(contract.getId());
            List<PaymentPlanSnapshot> assetSetSnapshotList = repaymentPlanService.get_all_unclear_assetSetSnapshot_list(contractSnapshot.getUuid());
            return new SandboxDataSet(financialContractSnapshot, contractSnapshot, repurchaseDocSnapshot,assetSetSnapshotList);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public SandboxDataSet get_sandbox_by_repaymentPlanNoList(String financialContractUuid, String contractUuid, List<String> repaymentPlanNoList) {
        FinancialContractSnapshot financialContractSnapshot = financialContractService.get_financialcontractsnapshot_by_financialcontractuuid(financialContractUuid);
        ContractSnapshot contractSnapshot = contractService.get_contractSnapshot_by_contractUuid(contractUuid);
        List<PaymentPlanSnapshot> assetSetSnapshotList = repaymentPlanService.get_assetSetSnapshot_list_by_no_list(repaymentPlanNoList);

        SandboxDataSet sandboxDataSet = new SandboxDataSet(financialContractSnapshot, contractSnapshot,null,
                assetSetSnapshotList);
        try {
            if (CollectionUtils.isNotEmpty(assetSetSnapshotList)) {
                PaymentPlanSnapshot paymentPlanSnapshot = assetSetSnapshotList.get(0);
                String assetUuid = paymentPlanSnapshot.getAssetUuid();
                RepaymentPlanExtraData planExtraData = repaymentPlanExtraDataService.get(assetUuid);
                if (null != planExtraData) {
                    int reasonCode = planExtraData.getReasonCode().ordinal();
                    sandboxDataSet.getExtraData().put("reasonCode", reasonCode + "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.warn("get RepaymentPlanExtraData fail, " + e.getMessage());
        }
        return sandboxDataSet;
    }

	@Override
	public SandboxDataSet get_sandbox_for_customize_data(
			Map<String, String> params) {
		return null;
	}

    @Override
    public boolean existSignUpAndPutPaymentInstitutionsInPostRequest(String accName, String accNo, String productCode, String paymentInstitution, Map<String, String> postRequest) {
        return false;
    }

    @Override
	public PedestrianBankCode existBank(String bankName, int headQuarter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExecutionStatus getStatusFromRemittanceApplicationDetail(
			String accNo, String accName) {
		return null;
	}

	@Override
	public SandboxDataSet getSandbox_for_signup(String proNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SandboxDataSet getSandbox_for_signup(String accName, String accNo,
			SignUpStatus signUpStatus, String productCode) {
		return null;
	}

	@Override
	public String checkRemittanceDetail(Map<String, Object> remittanceDetail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FinancialContract getFinancialContractByProductCode(
			String productCode) {
		return null;
	}

    @Override
    public Contract getContractByContract_uniqueId_contractNo(String uniqueId, String contractNo) {
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
	public AssetSet getActiveRepaymentPlanByRepaymentCode(String repaymentPlanCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existRequest(String requestNo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean existDeduct(String deductId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getBankCodeByBankAliasName(String bankAliasName) {
		// TODO Auto-generated method stub
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
		return true;
	}

	@Override
	public boolean validRemittanceForZhongHang(Map<String, String> requestParam) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validRemittanceForYunXin(Map<String, String> requestParam) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<PaymentChannelAndSignUpInfo> buildPaymentChannelAndSignUpInfos(
			List<PaymentChannelSummaryInfo> paymentChannelSummaryInfos, List<SignUp> signUps) {
		// TODO Auto-generated method stub
		return null;
	}

}
