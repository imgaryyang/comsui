/**
 * 
 */
package com.suidifu.citigroup.handler.v2;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.service.ContractSnapshotService;
import com.suidifu.matryoshka.service.FinancialContractSnapshotService;
import com.suidifu.matryoshka.service.RepaymentPlanSnapshotService;
import com.suidifu.matryoshka.snapshot.ContractSnapshot;
import com.suidifu.matryoshka.snapshot.FinancialContractSnapshot;
import com.suidifu.matryoshka.snapshot.PaymentPlanSnapshot;
import com.suidifu.matryoshka.snapshot.SandboxDataSet;
import com.zufangbao.gluon.util.CardBinUtil;
import com.zufangbao.sun.api.model.deduct.PaymentChannelAndSignUpInfo;
import com.zufangbao.sun.api.model.remittance.RemittanceDetail;
import com.zufangbao.sun.entity.bank.Bank;
import com.zufangbao.sun.entity.bank.v2.PedestrianBankCode;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationCode;
import com.zufangbao.sun.entity.financial.PaymentChannelSummaryInfo;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.RepurchaseService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.AssetSetActiveStatus;
import com.zufangbao.sun.yunxin.entity.remittance.ExecutionStatus;
import com.zufangbao.sun.yunxin.entity.v2.SignUp;
import com.zufangbao.sun.yunxin.entity.v2.SignUpStatus;
import com.zufangbao.sun.yunxin.handler.PaymentChannelInformationHandler;
import com.zufangbao.sun.yunxin.handler.ValidatorHandler;
import com.zufangbao.sun.yunxin.service.BankService;
import com.zufangbao.sun.yunxin.service.CardBinService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanExtraDataService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationDetailService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import com.zufangbao.sun.yunxin.service.remittance.RemittanceBlackListService;
import com.zufangbao.sun.yunxin.service.v2.PedestrianBankCodeservice;
import com.zufangbao.sun.yunxin.service.v2.SignUpService;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.SEND_RECEIVE_CODE_FOR_REMITTANCE;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpecUtil;
import com.zufangbao.wellsfargo.yunxin.handler.v2.SignUpHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wukai
 *
 */
@Component("normalPreSandboxHandler")
public class NormalSandboxDataSetHandler implements SandboxDataSetHandler{

    @Autowired
    private PedestrianBankCodeservice pedestrianBankCodeservice;

    @Autowired
    private ContractSnapshotService contractService;

    @Autowired
    private FinancialContractSnapshotService financialContractService;
    
    @Autowired
    private FinancialContractService financialContractInDBService;

    @Autowired
    private RepaymentPlanSnapshotService repaymentPlanService;
 
    
    @Autowired
    private RepurchaseService repurchaseService;
    
    @Autowired
    private SignUpService signUpService;
    
	@Autowired 
	private FinancialContractConfigurationService financialContractConfigurationService;
    

	@Autowired
	private BankService bankService;
	
    @Autowired
    private ZhonghangResponseMapSpecUtil zhonghangResponseMapSpecUtil;

    @Autowired
    private SignUpHandler signUpHandler;
    
    @Autowired
    private IRemittanceApplicationDetailService remittanceApplicationDetailService;
    
    @Autowired
    private RepaymentPlanExtraDataService repaymentPlanExtraDataService;
    
    @Autowired
	private PaymentChannelInformationHandler paymentChannelInformationHandler;

    
    @Autowired
    private RepaymentPlanService repaymentPlanServiceInDb;
    
    @Autowired
    private ContractService contractServiceInDb;
    
    
    @Autowired
    private IRemittanceApplicationService iRemittanceApplicationService;
    
    @Autowired
    private RemittanceBlackListService remittanceBlackListService;

	@Autowired
	private ValidatorHandler validatorHandler;

	@Autowired
	private CardBinService cardBinService;
    
    private static final Logger logger = LoggerFactory.getLogger(NormalSandboxDataSetHandler.class);

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
    
    public boolean existSignUpAndPutPaymentInstitutionsInPostRequest(String accName,String accNo, String productCode, String paymentInstitution, Map<String, String> postRequest){
    	
    	List<String> paymentInstitutionList = new ArrayList<>();
    	
    	if (StringUtils.isNotEmpty(paymentInstitution)) {
    		
    		paymentInstitutionList.add(paymentInstitution);
			
		}else{
			
			String financialContractUuid = postRequest.get("financialContractUuid");
			
			List<String> contents = financialContractConfigurationService.getFinancialContractConfigContentContentByCode(financialContractUuid,FinancialContractConfigurationCode.SIGN_UP_CHANNEL_INFORMATION_AND_FINANCIALCONTRACT_PAYTYPE.getCode());

			if (CollectionUtils.isEmpty(contents)) {

				return false;

			}

			for(String content:contents){

				Map<String,String> channel = JsonUtils.parse(content, Map.class);

				if (MapUtils.isNotEmpty(channel)) {

					paymentInstitutionList.add(channel.get(ZhonghangResponseMapSpec.PAYMENTINSTITUTION));

				}
			}
		}
    	
    	postRequest.put(ZhonghangResponseMapSpec.PAYMENTINSTITUTIONS, JsonUtils.toJsonString(paymentInstitutionList));
    	
    	List<Integer> paymentInstitutionListInInteger = paymentInstitutionList.stream().map(p->Integer.valueOf(p)).collect(Collectors.toList());
    	
    	return signUpService.exitSignUpForPaymentInstitutions(accName, accNo, SignUpStatus.SUCCESS, productCode, paymentInstitutionListInInteger);
    }
    
    
    public PedestrianBankCode existBank(String bankName,int headQuarter){
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
    
   public ExecutionStatus getStatusFromRemittanceApplicationDetail(String accNo,String accName){
	   
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


	@Override
	public SandboxDataSet get_sandbox_by_contract_uniqueId_contractNo(
			String uniqueId, String contractNo) {
		
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
	public SandboxDataSet get_sandbox_for_customize_data(
			Map<String, String> params) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		
		List<SignUp> signUpList = this.signUpService.getSignUps(accName, accNo, signUpStatus, productCode);
		
		SandboxDataSet sandboxDataSet = new SandboxDataSet();
		
		Map<String,String> extraData = new HashMap<String,String>();
		
		extraData.put("signUps", JsonUtils.toJsonString(signUpList));
		
		sandboxDataSet.setExtraData(extraData);
		
		return sandboxDataSet;
	}


	@Override
	public FinancialContract getFinancialContractByProductCode(
			String productCode) {
		
		FinancialContract  financialContract = financialContractInDBService.getUniqueFinancialContractBy(productCode);
		
		return financialContract;
	}

	@Override
	public Contract getContractByContract_uniqueId_contractNo(String uniqueId, String contractNo) {
		if (StringUtils.isNotEmpty(uniqueId))
			return contractServiceInDb.getContractByUniqueId(uniqueId);
		if (StringUtils.isNotEmpty(contractNo))
			return contractServiceInDb.getContractByContractNo(contractNo);

		return null;
	}

	@Override
	public AssetSet getActiveRepaymentPlanByRepaymentCode(String repaymentPlanCode) {
		return null;
	}


	@Override
	public String getBankMerId(String financialContractUuid) {
//		 String merId = financialContractConfigurationService.getFinancialContractConfigContentContent(financialContractUuid,FinancialContractConfigurationCode.MERID_CODE.getCode());
//		    
//	     return merId;
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
	public boolean sendMessageToEarth(Object deductModel) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public Object conventStandardDeductModel(
			Map<String, String> delayPostRequestParams) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String creatDeductApplicationReturnUuid(String ipAdderss,
			Object deductModel, String creatorName) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
//	@Cacheable("sign_up_accname_accno_status_productcode")
	public  Object getPaymentChannelAndSignUpInfoBy(String financialContractUuid,Object signUpObject){
		
		if(null == signUpObject){
			
			return Collections.emptyList();
		}
		List<SignUp> signUps = (List<SignUp>) signUpObject;

		List<PaymentChannelAndSignUpInfo> paymentChannelAndSignUpInfoList = new ArrayList<>();

		for (SignUp signUp : signUps) {

			PaymentChannelSummaryInfo paymentChannelSummaryInfo = paymentChannelInformationHandler
					.getHandleredPaymentChannelInfo(
							signUp.getPaymentInstitution(),
							financialContractUuid);

			if (null != paymentChannelSummaryInfo) {

				PaymentChannelAndSignUpInfo paymentChannelAndSignUpInfo = new PaymentChannelAndSignUpInfo(
						paymentChannelSummaryInfo, signUp);

				paymentChannelAndSignUpInfoList
						.add(paymentChannelAndSignUpInfo);
			}

		}
		return paymentChannelAndSignUpInfoList;
		 
	}
	@Override
	public  SandboxDataSet get_sandbox_for_asset_set(String contractUuid,int currentPeriod,int assetSetActiveStatus ) {
		
		 Contract contract = contractServiceInDb.getContract(contractUuid);
		 
		 AssetSetActiveStatus assetSetActiveStatusEnum  = EnumUtil.fromOrdinal(AssetSetActiveStatus.class, assetSetActiveStatus)  ;
		 
		 AssetSet assetSet = repaymentPlanServiceInDb.getAssetSetByContractAndCurrentPeriod(contract, currentPeriod,assetSetActiveStatusEnum );
		 
		 SandboxDataSet sandboxDataSet = new SandboxDataSet();
		 
		 Map<String,String> extraData = new HashMap<String,String>();
		 
		 extraData.put("assetSet", JsonUtils.toJsonString(assetSet));
		 
		 sandboxDataSet.setExtraData(extraData);
		 
		 return sandboxDataSet;
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
	public SandboxDataSet validFinancialContractAndContractForYunxin(Map<String, String> requestParam) {
				
				SandboxDataSet sandboxDataSet = new SandboxDataSet();
				
				Map<String,String> extraData = new HashMap<>();
				

				String contractUniqueId = requestParam.get(SEND_RECEIVE_CODE_FOR_REMITTANCE.UNIQUE_ID);

				
				String productCode =requestParam.get(SEND_RECEIVE_CODE_FOR_REMITTANCE.PRODUCT_CODE);
				
				//信托产品代码存在性校验
				FinancialContract financialContract = financialContractInDBService.getUniqueFinancialContractBy(productCode);
				
				extraData.put(ZhonghangResponseMapSpec.FINANCIAL_CONTRACT, JsonUtils.toJsonString(financialContract));


				Contract contract = contractServiceInDb.getContractByUniqueId(contractUniqueId);
				//存在已生效的贷款合同不进行放款操作
				
				extraData.put(ZhonghangResponseMapSpec.CONTRACT, JsonUtils.toJsonString(contract));
				
				sandboxDataSet.setExtraData(extraData);
				
				return sandboxDataSet;
	}
	
	/**
	 * 转账只对信托进行存在性校验，和贷款合同无关
	 */
	@Override
	public SandboxDataSet validFinancialContractForTransferAccounts(Map<String, String> requestParam) {
		SandboxDataSet sandboxDataSet = new SandboxDataSet();

		Map<String, String> extraData = new HashMap<>();

		String productCode = requestParam.get(SEND_RECEIVE_CODE_FOR_REMITTANCE.PRODUCT_CODE);

		// 信托产品代码存在性校验
		FinancialContract financialContract = financialContractInDBService.getUniqueFinancialContractBy(productCode);

		extraData.put(ZhonghangResponseMapSpec.FINANCIAL_CONTRACT, JsonUtils.toJsonString(financialContract));

		sandboxDataSet.setExtraData(extraData);

		return sandboxDataSet;
	};

	/**
	 * 放款明细内，银行编号校验
	 */
	@Override
	public boolean validateRemittanceDetailsBankCode(Map<String, String> requestParam) {
		Map<String, Bank> bankMap = bankService.getCachedBanks();

		List<RemittanceDetail> remittanceDetails = JsonUtils.parseArray(requestParam.get(SEND_RECEIVE_CODE_FOR_REMITTANCE.REMITTANCE_DETAILS),RemittanceDetail.class);
		for (RemittanceDetail remittanceDetail : remittanceDetails) {
			String bankCode = remittanceDetail.getBankCode();
			String bankCardNo = remittanceDetail.getBankCardNo();
			if (StringUtils.isEmpty(bankCode)) {
				Map<String, String> cardBinBankCodeMap = cardBinService.getCachedCardBins();
				bankCode = CardBinUtil.getBankCodeViaCardNo(cardBinBankCodeMap, bankCardNo);
			}
			if(! bankMap.containsKey(bankCode)){
				logger.error("NormalSandboxDataSetHandler#validateRemittanceDetailsBankCode " +
						"the bank code cannot be found the bank card no is {}", bankCardNo);
				return false;
			}
		}
		return true;
	}


	@Override
	public FinancialContract validFinancialContractForZhongHang(String productCode) {

		//信托产品代码存在性校验
		FinancialContract financialContract = financialContractInDBService.getUniqueFinancialContractBy(productCode);
		
		return financialContract;
	}




	@Override
	public boolean validRemittanceForYunXin(Map<String, String> requestParam) {
		// TODO Auto-generated method stub
		return false;
	}

	public ValidatorHandler getValidatorHandler(){

		return validatorHandler;
	}


	@Override
	public List<PaymentChannelAndSignUpInfo> buildPaymentChannelAndSignUpInfos(
			List<PaymentChannelSummaryInfo> paymentChannelSummaryInfos, List<SignUp> signUps) {
		
		if (CollectionUtils.isEmpty(paymentChannelSummaryInfos)) {
			
			return null;
			
		}
		
		return buildPaymentChannelAndSignUpInfoBySignUpList(paymentChannelSummaryInfos, signUps);
		
	}
	
	
	private List<PaymentChannelAndSignUpInfo>  buildPaymentChannelAndSignUpInfoBySignUpList(List<PaymentChannelSummaryInfo> paymentChannelSummaryInfos,List<SignUp> signUps){
		
		Map<Integer, SignUp> signUpMap = new HashMap<>();
		
		List<PaymentChannelAndSignUpInfo> paymentChannelAndSignUpInfos = new ArrayList<>();
		
		if (CollectionUtils.isNotEmpty(signUps)) {
			
			 signUpMap = signUps.stream().collect(Collectors.toMap(SignUp::getPaymentInstitutionNameInInteger, SignUp->SignUp));
			
		}
		
//		LOGGER.info("signUps"+JsonUtils.toJsonString(signUps)+"signUpMap"+JsonUtils.toJsonString(signUpMap)+"paymentChannelSummaryInfos"+JsonUtils.toJsonString(paymentChannelAndSignUpInfos));
		for(PaymentChannelSummaryInfo paymentChannelSummaryInfo:paymentChannelSummaryInfos){
			
			PaymentChannelAndSignUpInfo paymentChannelAndSignUpInfo = new PaymentChannelAndSignUpInfo(
					paymentChannelSummaryInfo, signUpMap.get(paymentChannelSummaryInfo.getPaymentGateway()));
			
			paymentChannelAndSignUpInfos.add(paymentChannelAndSignUpInfo);
		}
		
		return paymentChannelAndSignUpInfos;
		
	}


	@Override
	public boolean validRemittanceForZhongHang(Map<String, String> requestParam) {
		// TODO Auto-generated method stub
		return false;
	}

}
