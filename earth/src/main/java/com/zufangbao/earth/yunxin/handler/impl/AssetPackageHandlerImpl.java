package com.zufangbao.earth.yunxin.handler.impl;


import com.demo2do.core.entity.Result;
import com.zufangbao.earth.exception.BusinessSystemException;
import com.zufangbao.earth.yunxin.handler.AssetPackageHandler;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.sun.entity.bankCard.BankCard;
import com.zufangbao.sun.entity.bankCard.Identity;
import com.zufangbao.sun.entity.company.corp.App;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.contract.RepaymentPlanOperateLog;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.customer.CustomerStyle;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.entity.financial.AssetPackage;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.FinancialContractType;
import com.zufangbao.sun.entity.financial.LoanBatch;
import com.zufangbao.sun.entity.house.House;
import com.zufangbao.sun.entity.tag.TagConfigType;
import com.zufangbao.sun.handler.tag.TagOpsHandler;
import com.zufangbao.sun.ledgerbook.DuplicateAssetsException;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.LedgerItemService;
import com.zufangbao.sun.ledgerbook.LedgerTradeParty;
import com.zufangbao.sun.ledgerbookv2.exception.MakeTemplateGenerateShlefException;
import com.zufangbao.sun.ledgerbookv2.handler.LedgerBookV2Handler;
import com.zufangbao.sun.remittance.handler.RemittanceApplicationHandler;
import com.zufangbao.sun.service.*;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.AssetPackageFormat;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.NFQLoanInformation;
import com.zufangbao.sun.yunxin.entity.NFQRepaymentPlan;
import com.zufangbao.sun.yunxin.entity.datasync.BusinessLogsModel;
import com.zufangbao.sun.yunxin.entity.datasync.OperateMode;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplication;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlan;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlanExecLog;
import com.zufangbao.sun.yunxin.handler.ContractHandler;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import com.zufangbao.sun.yunxin.service.LoanBatchService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittancePlanExecLogService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittancePlanService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.REPEAT_OUTER_REPAYMENT_PLAN_NO;
import static com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec.ModifyAssetErrorMsgSpec.REPEAT_OUTER_REPAYMENT_PLAN_NO_MSG;

/**
 * @author 作者 zhenghangbo
 * @version 创建时间：Dec 14, 2016 4:06:40 PM
 * 类说明
 */
@Component("AssetPackageHandler")
public class AssetPackageHandlerImpl implements AssetPackageHandler {

    @Autowired
    private LedgerBookService ledgerBookService;

    @Autowired
    private FinancialContractService financialContractService;

    @Autowired
    private LoanBatchService loanBatchService;

    @Autowired
    private ContractAccountService contractAccountService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private AssetPackageService assetPackageService;

    @Autowired
    private LedgerItemService ledgerItemService;

    @Autowired
    private ContractHandler contractHandler;

    @Autowired
    private RepaymentPlanHandler repaymentPlanHandler;

    @Autowired
    private HouseService houseService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BankCardService bankCardService;

    @Autowired
    private RepaymentPlanService repaymentPlanService;

    @Autowired
    private com.zufangbao.sun.ledgerbook.LedgerBookHandler anMeiTuLedgerBookHandler;

    @Autowired
    private LedgerBookV2Handler ledgerBookV2Handler;

    @Autowired
    private IRemittanceApplicationService iRemittanceApplicationService;

    @Autowired
    private IRemittancePlanService iRemittancePlanService;

    @Autowired
    private IRemittancePlanExecLogService iRemittancePlanExecLogService;

    @Autowired
    private CashFlowService cashFlowService;

    @Autowired
    private RemittanceApplicationHandler remittanceApplicationHandler;

    @Autowired
    private TagOpsHandler tagOpsHandler;

    private static Log logger = LogFactory.getLog(AssetPackageHandlerImpl.class);

    @Override
    public Result importAssetPackagesViaExcel(List<NFQLoanInformation> loanInformationList, List<NFQRepaymentPlan> repaymentPlanInformationList,
                                              Long financialContractId, String operatorName, String ipAddress) throws IOException,
            InvalidFormatException, DuplicateAssetsException {
        //根据信托合同id获得信托合同对象--
        FinancialContract financialContract = financialContractService.load(FinancialContract.class, financialContractId);
        //获取当前信托合同资产包的格式--
        AssetPackageFormat assetPackageFormat = financialContract.getAssetPackageFormat();

        Result result = new Result();
        //创建信托合同状态--
        LoanBatch loanBatch = loanBatchService.createAndSaveLoanBatch(financialContract, false, null);
        //根据信托合同中的资产包格式选择
        switch (assetPackageFormat) {
            case AVERAGE_CAPITAL_PLUS_INTEREST:
            case SAW_TOOTH:
                result = import_asset_package_excel_for_yunxin(loanInformationList, repaymentPlanInformationList, financialContract, loanBatch, operatorName, ipAddress);
                break;
            default:
                throw returnBusinessSystemException(OfflineImportAssetPackageSpec.NOT_SUPPORT_THE_ASSET_FORMAT);
        }
        whileSuccRecordLoanBatchCode(result, loanBatch);
        return result;
    }

    private void whileSuccRecordLoanBatchCode(Result result, LoanBatch loanBatch) {
        if (result.isValid()) {
            result.data("loanBatchId", loanBatch.getId());
            result.setMessage("导入成功");
        }
    }

//	public void Testimport_asset_package_excel_for_yunxin(List<NFQLoanInformation> loanInformationList,List<NFQRepaymentPlan> repaymentPlanInformationList,FinancialContract financialContract, LoanBatch loanBatch){
//		try {
//			import_asset_package_excel_for_yunxin(loanInformationList, repaymentPlanInformationList, financialContract, loanBatch, "", "");
//		} catch (InvalidFormatException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (DuplicateAssetsException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	} 

    private Result import_asset_package_excel_for_yunxin(List<NFQLoanInformation> loanInformationList, List<NFQRepaymentPlan> repaymentPlanInformationList,
                                                         FinancialContract financialContract, LoanBatch loanBatch, String operatorName, String ipAddress)
            throws IOException, InvalidFormatException, DuplicateAssetsException {
        LedgerBook ledgerBook = null;
        try {
            ledgerBook = ledgerBookService.getBookByBookNo(financialContract.getLedgerBookNo());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        tagOpsHandler.postSingleItemWithMultiTags(loanBatch.getCode(), TagConfigType.LOAN_BATCH.getCode(),Arrays.asList(new String[]{"附件导入"}), false);
        for (NFQLoanInformation nfqLoanInformation : loanInformationList) {
            //获取贷款合同编号--
            String contractNo = nfqLoanInformation.getContractNo();
            //根据贷款合同编号获取到 导入的还款计划集合--
            List<NFQRepaymentPlan> repayment_plan_for_loan_contract = filter_repayment_plan_belong_to_loan_contract(
                    repaymentPlanInformationList, contractNo);
            App app = financialContract.getApp();
            //根据贷款信息&商户信息添加客户--
            Customer customer = saveCustomer(nfqLoanInformation, app, financialContract);
            //保存商户信息--
            House house = saveHouse(app);
            //根据贷款信息，商户，客户信息，商户信息，信托合同 保存贷款合同--
            Contract loan_contract = saveContract(nfqLoanInformation, app, customer, house, financialContract);
            //根据贷款合同和贷款信息 保存贷款人信息--
            saveContractAccount(nfqLoanInformation, loan_contract);
            //信托合同，合同状态，贷款合同，保存资产包--
            saveAssetPackage(financialContract, loanBatch, loan_contract);
            LedgerTradeParty ledgerTradeParty = ledgerItemService.getAssetsImportLedgerTradeParty(financialContract, customer);
            // 改用信托产品编号加密
            // String merId = null;
            // Company company = financialContract.getCompany();
            // if (company != null) {
            // 		merId = tMerConfigService.getMerIdByCompanyId(company.getId());
            // }
//			try {
//				evealRemittanceTemplate(ledgerBook,loan_contract,ledgerTradeParty);
//			} catch (Exception e) {
//				logger.error("生成放款流水出错: " + ExceptionUtils.getCauseErrorMessage(e));
//			}
			String financialContractNo = financialContract.getContractNo();
			List<AssetSet> openAssetSet = save_repayment_plan_list(repayment_plan_for_loan_contract, loan_contract, ledgerTradeParty, ledgerBook,financialContractNo);

			//合同导入，还款计划操作日志
			contractHandler.addRepaymentPlanOperateLog(loan_contract, RepaymentPlanOperateLog.CONTRACT_IMPORT, openAssetSet, null, null, ipAddress, operatorName);
			//新增还款计划同步日志
			repaymentPlanHandler.saveBusinessLogs(loan_contract, openAssetSet, new BusinessLogsModel(OperateMode.INSERT, null, null, null));
		}
		
		return new Result().success();
	}
	private void evealRemittanceTemplate(LedgerBook ledgerBook,Contract loan_contract,LedgerTradeParty ledgerTradeParty){
		logger.info("ledgerbookno:"+ledgerBook.getLedgerBookNo()+"contractUniqueID:"+loan_contract.getUniqueId());
		//根据贷款合同UniqueId 查出放款单
		List<RemittanceApplication> remittanceApplicationslist = iRemittanceApplicationService.getRemittanceApplicationsBy(loan_contract.getUniqueId());
		//放款单为空
		if(CollectionUtils.isEmpty(remittanceApplicationslist)){
			throw returnBusinessSystemException(OfflineImportAssetPackageSpec.REMITTANCE_APPLIACTION_NOT_FIND);
		}
		for (RemittanceApplication remittanceApplication : remittanceApplicationslist) {
			List<RemittancePlan> remittancePlanList = iRemittancePlanService.getRemittancePlanListBy(remittanceApplication.getRemittanceApplicationUuid());
			if(CollectionUtils.isEmpty(remittancePlanList)){
				throw returnBusinessSystemException(OfflineImportAssetPackageSpec.REMITTANCE_PLAN_NOT_FIND);
			}
			for (RemittancePlan remittancePlan : remittancePlanList) {
				List<RemittancePlanExecLog> remittancePlanExecLogList = iRemittancePlanExecLogService.getRemittancePlanExecLogListBy(remittancePlan.getRemittancePlanUuid());
				if(CollectionUtils.isEmpty(remittancePlanExecLogList)){
					throw returnBusinessSystemException(OfflineImportAssetPackageSpec.REMITTANCE_PLAN_EXECLOG_IS_NULL);
				}
				for (RemittancePlanExecLog remittancePlanExecLog : remittancePlanExecLogList) {
					CashFlow cashFlow = cashFlowService.getCashFLowByTradeUUid(remittancePlanExecLog.getExecRspNo());
					if(cashFlow == null){
						throw returnBusinessSystemException(OfflineImportAssetPackageSpec.CASHFLOW_NOT_EXIST);
					}
					logger.info("放款流水(Cashflow):"+cashFlow);
					try {
						remittanceApplicationHandler.remittance_Application_V(cashFlow,ledgerBook.getLedgerBookNo(),ledgerTradeParty,loan_contract);
					} catch (MakeTemplateGenerateShlefException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void checkIsExistContractInsystem(String contractNo) {
		Contract exist_contract = contractService
				.getContractByContractNo(contractNo);
		if (null != exist_contract) {
			throw  returnBusinessSystemException(OfflineImportAssetPackageSpec.LOAN_CONTRACT_HAS_EXIST+contractNo);
		}
	}
	
	private void checkFinancialContractNo(FinancialContract financialContract, NFQLoanInformation nfqLoanInformation) {
		if(!financialContract.getContractNo().equals(nfqLoanInformation.getTrustContractNo())){
			throw  returnBusinessSystemException(OfflineImportAssetPackageSpec.FINANCIAL_CONTRACT_CODE_NOT_SAME);
		}
	}
	private List<NFQRepaymentPlan> filter_repayment_plan_belong_to_loan_contract(
			List<NFQRepaymentPlan> repaymentPlanList, String contractNo) {
		List<NFQRepaymentPlan> repayment_plan_for_loan_contract = new ArrayList<NFQRepaymentPlan>();
		for (NFQRepaymentPlan nfqRepaymentPlan : repaymentPlanList) {
			if (is_repayment_plan_for_loan_contract(contractNo,
					nfqRepaymentPlan)) {
				repayment_plan_for_loan_contract.add(nfqRepaymentPlan);
			}
		}
		return repayment_plan_for_loan_contract;
	}

	private boolean is_repayment_plan_for_loan_contract(String contractNo,
			NFQRepaymentPlan nfqRepaymentPlan) {
		return StringUtils.equals(contractNo, nfqRepaymentPlan.getContractNo());
	}
	private void saveAssetPackage(FinancialContract financialContract, LoanBatch loanBatch, Contract loan_contract) {
		AssetPackage assetPackage = new AssetPackage(financialContract, loan_contract, loanBatch);
		assetPackageService.saveOrUpdate(assetPackage);
	}

	private void saveContractAccount(NFQLoanInformation nfqLoanInformation, Contract loan_contract) {
		
		ContractAccount contractAccount = new ContractAccount(loan_contract, nfqLoanInformation);
		contractAccountService.saveOrUpdate(contractAccount);
	}

	private Contract saveContract(NFQLoanInformation nfqLoanInformation, App app, Customer customer, House house, FinancialContract financialContract) {
		Contract loan_contract = new Contract();
		nfqLoanInformation.copyToContract(app, customer, house, loan_contract, financialContract);
		
		contractService.saveOrUpdate(loan_contract);
		return loan_contract;
	}

	private House saveHouse(App app) {
		House house = new House(app);
		houseService.saveOrUpdate(house);
		return house;
	}

	private Customer saveCustomer(NFQLoanInformation nfqLoanInformation, App app, FinancialContract fc) {
		Customer customer = new Customer(nfqLoanInformation, app);
		
		boolean exist = customerService.existSameIDCardNo(nfqLoanInformation.getCustomerIDNo());
		if (exist) {
			customer.setStatus(false);
		}
        if (fc.getFinancialContractType() != null && fc.getFinancialContractType() == FinancialContractType.SmallBusinessMicroCredit) {
            customer.setCustomerStyle(CustomerStyle.enterprise);
        }
        customerService.saveOrUpdate(customer);

        BankCard bankCard = new BankCard(customer.getCustomerUuid(), Identity.customer, nfqLoanInformation.getCustomerName(), nfqLoanInformation.getCustomerAccountNo(),
                nfqLoanInformation.getCustomerBankName(), nfqLoanInformation.getCustomerBankCode(), nfqLoanInformation.getCustomerBankProvince(), null, nfqLoanInformation.getCustomerBankCity(), null);
        bankCardService.save(bankCard);

        return customer;
    }

    private List<AssetSet> save_repayment_plan_list(List<NFQRepaymentPlan> repayment_plan_for_loan_contract,
                                                    Contract loan_contract, LedgerTradeParty ledgerTradeParty, LedgerBook ledgerBook, String financialContractNo)
            throws DuplicateAssetsException {
        List<AssetSet> assetSets = new ArrayList<AssetSet>();

        Collections.sort(repayment_plan_for_loan_contract, new Comparator<NFQRepaymentPlan>() {

            @Override
            public int compare(NFQRepaymentPlan a, NFQRepaymentPlan b) {
                Date aDate = DateUtils.asDay(a.getAssetRecycleDate());
                Date bDate = DateUtils.asDay(b.getAssetRecycleDate());
                return aDate.before(bDate) ? -1 : 1;
            }
        });

        for (int i = 0; i < repayment_plan_for_loan_contract.size(); i++) {
            NFQRepaymentPlan repaymentPlan = repayment_plan_for_loan_contract.get(i);
            String outerRepaymentPlanNo = repaymentPlan.getOuterRepaymentPlanNo();
            String repayScheduleNo = repaymentPlanService.getRepayScheduleNoMD5(financialContractNo, outerRepaymentPlanNo, com.zufangbao.sun.utils.StringUtils.EMPTY);
//			repaymentPlanService.checkByRepayScheduleNo(repayScheduleNo);
            AssetSet assetSet = saveAssetSet(loan_contract, i, repaymentPlan, repayScheduleNo);
            assetSets.add(assetSet);
//			if (checkVersion(ledgerBook)){
//				ledgerBookV2Handler.book_loan_asset_V2(ledgerBook.getLedgerBookNo(), assetSet, ledgerTradeParty);
//			}

        }
        //已废弃，使用@link ledgerBookV2Handler#book_loan_asset_V2
        anMeiTuLedgerBookHandler.book_loan_assets(ledgerBook.getLedgerBookNo(), assetSets, ledgerTradeParty);
        return assetSets;
    }

    private boolean checkVersion(LedgerBook ledgerBook) {
        if (null == ledgerBook) {
            return false;
        }
        return StringUtils.isNotEmpty(ledgerBook.getLedgerBookVersion());
    }

    private AssetSet saveAssetSet(Contract loan_contract, int i, NFQRepaymentPlan repaymentPlan, String repayScheduleNo) {
        int current_period_num = i + 1;
        AssetSet assetSet = new AssetSet(loan_contract, repaymentPlan, current_period_num, repayScheduleNo);
        try {
            repaymentPlanService.saveOrUpdate(assetSet);
        } catch (HibernateException e) {
            e.printStackTrace();
            throw new ApiException(REPEAT_OUTER_REPAYMENT_PLAN_NO, REPEAT_OUTER_REPAYMENT_PLAN_NO_MSG);
        }

        return assetSet;
    }


    @Override
    public void verifyInputParam(List<NFQLoanInformation> loanInformationList, List<NFQRepaymentPlan> repaymentPlanInformationList,
                                 Long financialContractId) {

        if (CollectionUtils.isEmpty(loanInformationList)
                || CollectionUtils.isEmpty(repaymentPlanInformationList)) {
            throw returnBusinessSystemException(OfflineImportAssetPackageSpec.ASSET_PACKAGE_EXCEL_ERROR);
        }

        FinancialContract financialContract = financialContractService.load(
                FinancialContract.class, financialContractId);
        if (financialContract == null) {
            throw returnBusinessSystemException(OfflineImportAssetPackageSpec.NO_EXIST_THE_FINANCIAL_CONTRACT);
        }

        if (StringUtils.isBlank(financialContract.getContractNo())) {
            throw returnBusinessSystemException(OfflineImportAssetPackageSpec.THE_FINANCIAL_CONTRACT_NO_IS_BLANK);
        }

        AssetPackageFormat assetPackageFormat = financialContract.getAssetPackageFormat();
        if (assetPackageFormat == null) {
            throw returnBusinessSystemException(OfflineImportAssetPackageSpec.THE_FINANCIAL_CONTRACT_NO_ASSET_FORMAT);
        }

        for (NFQLoanInformation nfqLoanInformation : loanInformationList) {
            String contractNo = nfqLoanInformation.getContractNo();
            checkIsExistContractInsystem(contractNo);
            checkFinancialContractNo(financialContract, nfqLoanInformation);

            List<NFQRepaymentPlan> repayment_plan_for_loan_contract = filter_repayment_plan_belong_to_loan_contract(
                    repaymentPlanInformationList, contractNo);
            if (nfqLoanInformation.getPeriodsIntValue() != repayment_plan_for_loan_contract.size()) {
                throw returnBusinessSystemException(OfflineImportAssetPackageSpec.REPAYMENT_PLAN_PERIODS_ERROR + contractNo);
            }
            App app = financialContract.getApp();
            if (app == null) {
                throw returnBusinessSystemException(OfflineImportAssetPackageSpec.FINANCIAL_APP_IS_BLANK);
            }
        }
    }

    private BusinessSystemException returnBusinessSystemException(String message) {
        return new BusinessSystemException(message);
    }
}
