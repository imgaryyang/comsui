package com.suidifu.bridgewater.handler.impl.single.v2;

import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.zufangbao.sun.entity.bank.v2.PedestrianBankCode;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.RepurchaseService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.remittance.ExecutionStatus;
import com.zufangbao.sun.yunxin.service.RepaymentPlanExtraDataService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationDetailService;
import com.zufangbao.sun.yunxin.service.v2.PedestrianBankCodeservice;
import com.zufangbao.sun.yunxin.service.v2.SignUpService;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpecUtil;
import com.zufangbao.wellsfargo.yunxin.handler.v2.SignUpHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 沙盒数据Handler
 * Created by louguanyang on 2017/5/9.
 */
public abstract class AbstractSandboxDataSetHandler implements SandboxDataSetHandler {

    @Autowired
    private ContractService contractService;

    @Autowired
    private PedestrianBankCodeservice pedestrianBankCodeservice;


    @Autowired
    private FinancialContractService financialContractService;

    @Autowired
    private RepaymentPlanService repaymentPlanService;

    @Autowired
    private RepurchaseService repurchaseService;

    @Autowired
    private SignUpService signUpService;

    @Autowired
    private ZhonghangResponseMapSpecUtil zhonghangResponseMapSpecUtil;

    @Autowired
    private SignUpHandler signUpHandler;

    @Autowired
    private IRemittanceApplicationDetailService remittanceApplicationDetailService;

    @Autowired
    private RepaymentPlanExtraDataService repaymentPlanExtraDataService;

    private static final Log LOGGER = LogFactory.getLog(AbstractSandboxDataSetHandler.class);

//    @Override
//    public SandboxDataSet get_sandbox_by_contract_uniqueId_contractNo(String uniqueId, String contractNo) {
//        try {
//            ContractSnapshot contractSnapshot = contractService.get_contractSnapshot_by_uniqueId_or_contractNo(uniqueId, contractNo);
//            String financialContractUuid = contractSnapshot.getFinancialContractUuid();
//            FinancialContractSnapshot financialContractSnapshot = financialContractService.get_financialcontractsnapshot_by_financialcontractuuid(financialContractUuid);
//            List<PaymentPlanSnapshot> assetSetSnapshotList = repaymentPlanService.get_all_unclear_assetSetSnapshot_list(contractSnapshot.getUuid());
//            return new SandboxDataSet(financialContractSnapshot, contractSnapshot,null, assetSetSnapshotList);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    @Override
//    public SandboxDataSet get_sandbox_for_apply_repurchase_by_contract_uniqueId_contractNo(String uniqueId, String contractNo){
//        try {
//            Contract contract = contractService.getContractBy(uniqueId, contractNo);
//            ContractSnapshot contractSnapshot = contractService.getContractSnapshot(contract);
//            String financialContractUuid = contractSnapshot.getFinancialContractUuid();
//            FinancialContractSnapshot financialContractSnapshot = financialContractService.get_financialcontractsnapshot_by_financialcontractuuid(financialContractUuid);
//            RepurchaseDocSnapshot repurchaseDocSnapshot = repurchaseService.getEffectiveRepurchaseDocSnapshotBy(contract.getId());
//            List<PaymentPlanSnapshot> assetSetSnapshotList = repaymentPlanService.get_all_unclear_assetSetSnapshot_list(contractSnapshot.getUuid());
//            return new SandboxDataSet(financialContractSnapshot, contractSnapshot, repurchaseDocSnapshot,assetSetSnapshotList);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    @Override
//    public SandboxDataSet get_sandbox_by_repaymentPlanNoList(String financialContractUuid, String contractUuid, List<String> repaymentPlanNoList) {
//        FinancialContractSnapshot financialContractSnapshot = financialContractService.get_financialcontractsnapshot_by_financialcontractuuid(financialContractUuid);
//        ContractSnapshot contractSnapshot = contractService.get_contractSnapshot_by_contractUuid(contractUuid);
//        List<PaymentPlanSnapshot> assetSetSnapshotList = repaymentPlanService.get_assetSetSnapshot_list_by_no_list(repaymentPlanNoList);
//
//        SandboxDataSet sandboxDataSet = new SandboxDataSet(financialContractSnapshot, contractSnapshot,null,
//                assetSetSnapshotList);
//        try {
//            if (CollectionUtils.isNotEmpty(assetSetSnapshotList)) {
//                PaymentPlanSnapshot paymentPlanSnapshot = assetSetSnapshotList.get(0);
//                String assetUuid = paymentPlanSnapshot.getAssetUuid();
//                RepaymentPlanExtraData planExtraData = repaymentPlanExtraDataService.getRepaymentPlanExtraDataByAssetUuid(assetUuid);
//                if (null != planExtraData) {
//                    int reasonCode = planExtraData.getReasonCode().ordinal();
//                    sandboxDataSet.getExtraData().put("reasonCode", reasonCode + "");
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            LOGGER.warn("get RepaymentPlanExtraData fail, " + e.getMessage());
//        }
//        return sandboxDataSet;
//    }

    public boolean existSignUp(String accName,String accNo, String productCode, int paymentInstitution){

//		return   signUpService.exitSignUp(accName, accNo,SignUpStatus.SUCCESS,productCode, null);
    	return false;

    }


    @Override
    public PedestrianBankCode existBank(String bankName, int headQuarter){

    	if (StringUtils.isEmpty(bankName)) {

    		return null;
		}
    	List<PedestrianBankCode> bankList = null;
    	List<PedestrianBankCode> list = null;
    	if (headQuarter==1) {

    		 bankList = signUpHandler.getPedestrianBankCodeByName();
        	if (CollectionUtils.isEmpty(bankList)) {
        		return null;
    		}
        	 list=bankList.stream().filter(bank->bankName.contains(bank.getBankFullName())).collect(Collectors.toList());
        	if (CollectionUtils.isEmpty(list)){
    			return null;
    		}
        	return list.get(0);
		}
    	bankList = pedestrianBankCodeservice.getCachedBankNames();
    	if (CollectionUtils.isEmpty(bankList)) {
    		return null;
		}
    	 list=bankList.stream().filter(bank->bankName.equals(bank.getBankFullName())).collect(Collectors.toList());
    	if (CollectionUtils.isEmpty(list)){
			return null;
		}
    	return list.get(0);
    }

   @Override
   public ExecutionStatus getStatusFromRemittanceApplicationDetail(String accNo, String accName){

		List<Integer> status = remittanceApplicationDetailService.getStatusByAccNoAndName(accNo, accName);
		if (CollectionUtils.isEmpty(status)) {
			return ExecutionStatus.FAIL;
		}
		if (status.contains(ExecutionStatus.SUCCESS.getOrdinal())) {
			return ExecutionStatus.SUCCESS;
		} else if (status.contains(ExecutionStatus.PROCESSING.getOrdinal())) {
			return ExecutionStatus.PROCESSING;
		} else {
			return ExecutionStatus.FAIL;
		}
	}

	@Override
    public String checkRemittanceDetail(Map<String, Object> remittanceDetail) {

       String detailNo = (String) remittanceDetail.get("detailNo");
        if(StringUtils.isEmpty(detailNo)) {
            return "明细记录号［detailNo］，不能为空！";
        }
        try {
            String recordSn = (String) remittanceDetail.get("recordSn");
            int xx = Integer.parseInt(recordSn);
        } catch (NumberFormatException e) {
            return "记录序号［recordSn］，格式错误！";
        }
        try {
            String amount = (String) remittanceDetail.get("amount");
            BigDecimal bdAmount = new BigDecimal(amount);
            if(bdAmount.compareTo(BigDecimal.ZERO) <= 0) {
                return "明细金额［amount］，金额需高于0.00！";
            }
            if(bdAmount.scale() > 2) {
                return "明细金额［amount］，小数点后只保留2位！";
            }
        } catch (Exception e) {
            return "明细金额［amount］，格式错误！";
        }
        String bankAliasName = (String) remittanceDetail.get("bankAliasName");
        if(StringUtils.isEmpty(bankAliasName)){
            return "行别 [bankAliasName] 不能为空！";
        }
        String plannedDate = (String) remittanceDetail.get("plannedDate");
        Date now = new Date();
        Date parsedPlannedDate = StringUtils.isEmpty(plannedDate) ? now : DateUtils.parseDate(plannedDate, "yyyy-MM-dd HH:mm:ss");
        if(parsedPlannedDate == null){
            return "计划执行日期［plannedDate］，格式错误！";
        }
        if(parsedPlannedDate.before(now)){
            return "计划执行日期［plannedDate]不能早于当前时间";
        }
        String bankCardNo = (String) remittanceDetail.get("bankCardNo");
        String bankAccountHolder = (String) remittanceDetail.get("bankAccountHolder");
        if(StringUtils.isEmpty(bankCardNo) || StringUtils.isEmpty(bankAccountHolder)){
            return "交易方信息[bankCardNo, bankAccountHolder]不完整！";
        }

        String originBankCode = (String) remittanceDetail.get("bankCode");
        String bankCode = zhonghangResponseMapSpecUtil.getBankCodeBy(bankAliasName);
        if(bankCode == null){
            return "行别［bankAliasName］，错误！";
        }
        List<PedestrianBankCode> bankList = pedestrianBankCodeservice.getCachedBankNames();
        String subBankName = (String) remittanceDetail.get("bankName"); // 支行名（中文）
//        LOGGER.info("bankName====="+subBankName+"\tPedestrianBankCode size"+bankList.size());
        if(com.zufangbao.sun.utils.StringUtils.isNotEmpty(subBankName)){
            PedestrianBankCode bank = CollectionUtils.isEmpty(bankList) ? null: bankList.stream()
                .filter(a-> org.apache.commons.lang.StringUtils.equals(subBankName,a.getBankFullName()))
                .findFirst().orElse(null);
            if(bank == null){
                return "交易方开户支行名称［bankName］，错误！";
            }
            bankCode = bank.getStdBankCode();
            if(!bank.getBankFullName().contains(ZhonghangResponseMapSpec.HEAD_OFFICE_ENGLISH_ABBR_CN_MAP.get(bankAliasName))){
                return "交易方开户支行名称［bankName］不隶属于 行别 [bankAliasName]，错误！";
            }
        }

        if(StringUtils.isNotEmpty(originBankCode) && !StringUtils.equals(originBankCode,bankCode)){
            return "行别［bankAliasName］或 交易方开户支行名称［bankName］ 和 总行联行号 [bankCode] 不匹配！";
        }

        remittanceDetail.put("bankCode",bankCode);

        return null;
    }

}
