package com.suidifu.morganstanley.handler.repayment.impl;


import com.suidifu.matryoshka.customize.CustomizeServices;
import com.suidifu.matryoshka.handler.ProductCategoryCacheHandler;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.productCategory.ProductCategory;
import com.suidifu.morganstanley.configuration.MorganStanleyNotifyConfig;
import com.suidifu.morganstanley.configuration.bean.weifang.WeiFangProperties;
import com.suidifu.morganstanley.configuration.bean.yntrust.YntrustFileTask;
import com.suidifu.morganstanley.controller.api.repayment.ImportAssetPackageController;
import com.suidifu.morganstanley.handler.repayment.ImportAssetPackageHandler;
import com.suidifu.morganstanley.model.request.repayment.ContractDetail;
import com.suidifu.morganstanley.model.request.repayment.ImportAssetPackage;
import com.suidifu.morganstanley.model.request.repayment.ImportAssetPackageContent;
import com.suidifu.morganstanley.model.request.repayment.ImportRepaymentPlanDetail;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.suidifu.swift.notifyserver.notifyserver.impl.NotifyJobServerImpl;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.spec.global.GlobalSpec;
import com.zufangbao.gluon.util.CardBinUtil;
import com.zufangbao.sun.api.model.repayment.ImportAssetPackageResponseModel;
import com.zufangbao.sun.entity.bank.Bank;
import com.zufangbao.sun.entity.bankCard.BankCard;
import com.zufangbao.sun.entity.bankCard.Identity;
import com.zufangbao.sun.entity.company.corp.App;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.customer.CustomerPerson;
import com.zufangbao.sun.entity.customer.CustomerStyle;
import com.zufangbao.sun.entity.customer.Sex;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.entity.financial.AssetPackage;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.FinancialContractType;
import com.zufangbao.sun.entity.financial.LoanBatch;
import com.zufangbao.sun.entity.house.House;
import com.zufangbao.sun.entity.repayment.job.ImportAssetDataRedisSpec;
import com.zufangbao.sun.geography.entity.City;
import com.zufangbao.sun.geography.entity.Province;
import com.zufangbao.sun.geography.service.CityService;
import com.zufangbao.sun.geography.service.ProvinceService;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.LedgerBookHandler;
import com.zufangbao.sun.ledgerbook.LedgerTradeParty;
import com.zufangbao.sun.ledgerbookv2.exception.MakeTemplateGenerateShlefException;
import com.zufangbao.sun.ledgerbookv2.handler.LedgerBookV2Handler;
import com.zufangbao.sun.remittance.handler.RemittanceApplicationHandler;
import com.zufangbao.sun.service.AssetPackageService;
import com.zufangbao.sun.service.BankCardService;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.ContractAccountService;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.CustomerService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.HouseService;
import com.zufangbao.sun.service.ImportAssetPackageLogService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.service.PersonService;
import com.zufangbao.sun.utils.AmountUtils;
import com.zufangbao.sun.utils.CheckFormatUtils;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.entity.AssetClearStatus;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.RepaymentWay;
import com.zufangbao.sun.yunxin.entity.datasync.BusinessLogsModel;
import com.zufangbao.sun.yunxin.entity.datasync.OperateMode;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplication;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlan;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlanExecLog;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceStrategyMode;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanValuationHandler;
import com.zufangbao.sun.yunxin.service.*;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittancePlanExecLogService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittancePlanService;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpecUtil;
import com.zufangbao.wellsfargo.exception.BusinessSystemException;
import com.zufangbao.wellsfargo.exception.RemittanceGenerateShelfException;
import com.zufangbao.wellsfargo.yunxin.data.sync.handler.DailyReconciliationHandler;
import com.zufangbao.wellsfargo.yunxin.handler.ImportAssetPackageJobHandler;
import com.zufangbao.wellsfargo.yunxin.handler.RemittancetApplicationHandler;
import java.util.UUID;
import java.util.regex.Pattern;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.CITY_AND_PROVINCE_CODE_DOES_NOT_MATCH;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.CUSTOMER_FEE_CHECK_FAIL;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.EFFECT_DATE_ERROR;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.EFFECT_DATE_INVAILD;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.EXIST_LOAN_CONTRACT_NO;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.EXIST_LOAN_CONTRACT_UNIQUE_ID;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.EXIST_THE_SUBJECT_MATTER;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.EXPIRE_DATE_ERROR;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.FINANCIAL_PRODUCT_CODE_ERROR;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.FIRST_REPAYMENT_DATE_NOT_CORRECT;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.ID_CARD_ERROR;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.LOAN_CONTRACT_NO_OR_UNIQUEID_IS_EMPTY;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.LOAN_CUSTOMER_NAME_ERROR;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.LOAN_CUSTOMER_NO_ERROR;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.LOAN_PERIODS_ERROR;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.LOAN_RATES_ERROR;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.LOAN_SERVICE_FEE_ERROR;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.LOAN_TOTAL_AMOUNT_ERROR;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.LOAN_TOTAL_AMOUNT_NOT_MATCH_REMITTANCE_AMOUNT;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.NOT_SET_APP_IN_FINANCIAL_CONTRACT;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.NO_MATCH_BANK;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.NO_MATCH_CITY;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.NO_MATCH_PROVINCE;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.NO_MATCH_REMITTANCE;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.NO_MATCH_REPAYMENT_WAY;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.OTHER_FEE_ERROR;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.PENALTY_ERROR;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.REPAYMENT_ACCOUNT_ERROR;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.REPAYMENT_INTEREST_ERROR;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.REPAYMENT_PLAN_REPAYMENT_DATE_ERROR;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.REPAYMENT_PLAN_TOTAL_AMOUNT_ERROR;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.REPAYMENT_PLAN_TOTAL_PERIODS_ERROR;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.REPAYMENT_PRINCIPAL_ERROR;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.REPEAT_OUTER_REPAYMENT_PLAN_NO;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.SUCCESS;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.TECH_MAINTENANCE_FEE_ERROR;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.TOTAL_AMOUNT_ERROR;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.TOTAL_CONTRACTS_NUMBER_NOT_MATCH;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.TOTAL_NUMBER_ERROR;
import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.TRUST_CODE_NOT_AGREE_WITH_TRUST_CODE_OF_REMITTANCE;
import static com.zufangbao.sun.entity.repayment.job.ImportAssetDataRedisSpec.FAIL_FLAG;
import static com.zufangbao.sun.entity.repayment.job.ImportAssetDataRedisSpec.SUCCESS_FLAG;

/**
 * @author wangjianhua.
 */
@Component("ImportAssetPackageHandler")
@Log4j2
public class ImportAssetPackageHandlerImpl implements ImportAssetPackageHandler {

    @Autowired
    private ContractService contractService;
    @Autowired
    private LedgerBookHandler ledgerBookHandler;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private BankCardService bankCardService;
    @Autowired
    private HouseService houseService;
    @Autowired
    private ContractAccountService contractAccountService;
    @Autowired
    private BankService bankService;
    @Autowired
    private ProvinceService provinceService;
    @Autowired
    private CityService cityService;
    @Autowired
    private LoanBatchService loanBatchService;
    @Autowired
    private AssetPackageService assetPackageService;
    @Autowired
    private RepaymentPlanService repaymentPlanService;
    @Autowired
    private FinancialContractService financialContractService;
    @Autowired
    private RepaymentPlanExtraChargeService repaymentPlanExtraChargeService;
    @Autowired
    private RepaymentPlanHandler repaymentPlanHandler;
    @Autowired
    private RemittancetApplicationHandler remittancetApplicationHandler;
    @Autowired
    private RepaymentPlanValuationHandler repaymentPlanValuationHandler;
    @Autowired
    private IRemittanceApplicationService iRemittanceApplicationService;
    @Autowired
    private ImportAssetPackageLogService importAssetPackageLogService;

    @Autowired
    private ZhonghangResponseMapSpecUtil zhonghangResponseMapSpecUtil;

    @Autowired
    private IRemittancePlanService iRemittancePlanService;

    @Autowired
    private IRemittancePlanExecLogService iRemittancePlanExecLogService;

    @Autowired
    private CashFlowService cashFlowService;

    @Autowired
    private RemittanceApplicationHandler remittanceApplicationHandler;

    @Autowired
    private LedgerBookService ledgerBookService;

    @Autowired
    private LedgerBookV2Handler ledgerBookV2Handler;

    @Autowired
    private ProductCategoryCacheHandler productCategoryCacheHandler;

    @Autowired
    private SandboxDataSetHandler sandboxDataSetHandler;

    @Autowired
    private WeiFangProperties weiFangProperties;

    @Autowired
    private ImportAssetPackageJobHandler importAssetPackageJobHandler;

    @Autowired
    private DailyReconciliationHandler dailyReconciliationHandler;

    @Resource
    private YntrustFileTask yntrustFileTask;

    @Resource
    private CardBinService cardBinService;

    @Autowired
    private PersonService personService;

    @Override
    public ImportAssetPackageResponseModel importAssetPackage(ImportAssetPackage model, boolean not_offline_remittance) throws Exception {
        log.info("#ImportAssetPackageHandlerImpl importAssetPackage : entry method");
        ImportAssetPackageContent requestContent = model.getRequestContentObject();
        long startTime = System.currentTimeMillis();
        FinancialContract financialContract = judgeFinancialProductCodeAndReturnVaildFinancialContract(requestContent);
        log.info("#ImportAssetPackageHandlerImpl importAssetPackage: judgeFinancialProductCodeAndReturnVaildFinancialContract in " + (System.currentTimeMillis() - startTime) + "ms");
        startTime = System.currentTimeMillis();
        LoanBatch loanBatch = saveLoanBatch(financialContract, model.getRequestNo());
        log.info("#ImportAssetPackageHandlerImpl importAssetPackage: saveLoanBatch in " + (System.currentTimeMillis() - startTime) + "ms");
        for (ContractDetail contractDetail : requestContent.getContractDetails()) {
            Map<String, String> detailMap = getStringMap(contractDetail);
            App app = financialContract.getApp();
            if (app == null) {
                log.error("importAssetPackage has error, 信托合同商户未设置!");
                processError(NOT_SET_APP_IN_FINANCIAL_CONTRACT);
            }
            if (CollectionUtils.isEmpty(contractDetail.getRepaymentPlanDetails())) {
                log.warn("importAssetPackage has error, RepaymentPlanDetails is empty");
                continue;
            }
            startTime = System.currentTimeMillis();
            Customer customer = saveCustomer(detailMap, app, financialContract);
            log.info("#ImportAssetPackageHandlerImpl importAssetPackage: saveCustomer in " + (System.currentTimeMillis() - startTime) + "ms");
            startTime = System.currentTimeMillis();
            House house = saveHouse(detailMap, app);
            log.info("#ImportAssetPackageHandlerImpl importAssetPackage: saveHouse in " + (System.currentTimeMillis() - startTime) + "ms");
            startTime = System.currentTimeMillis();
            Contract loan_contract = saveContract(contractDetail, app, customer, house, financialContract, not_offline_remittance);
            log.info("#ImportAssetPackageHandlerImpl importAssetPackage: saveContract in " + (System.currentTimeMillis() - startTime) + "ms");

            String localKeyWord = "[uniqueId=" + loan_contract.getUniqueId() + "]";
            log.info(GloableLogSpec.AuditLogHeaderSpec() + " MORGANSTANLEY:IMPORT_ASSET_PACKAGE# " + localKeyWord);

            // 放款流水生成
            log.info("#begin to Remittance generate shelf !!!");
            try {
                if (ledgerBookV2Handler.checkLedgerbookVersionByledgerBookNo
                        (financialContract.getLedgerBookNo())) {
                    getRemittanceTemplateValue(financialContract, loan_contract,
                            customer, model.getRequestNo());
                }
                log.info("begin into no [Template] generate " +
                        "ImportAssetSetGenerateRemittanceledgerBookShelf!!!");
                ledgerBookHandler.generateImportAssetSetRemittanceShelf(financialContract, loan_contract,
                        customer, model.getRequestNo());
            } catch (Exception e) {
                log.info("#Remittance generate shelf exception : " + e
                        .getMessage());
            }
            log.info("#end Remittance generate shelf !!!");
            startTime = System.currentTimeMillis();
            ContractAccount contractAccount = saveContractAccount(detailMap, loan_contract);
            log.info("#ImportAssetPackageHandlerImpl importAssetPackage: saveContractAccount in " + (System.currentTimeMillis() - startTime) + "ms");
            startTime = System.currentTimeMillis();
            saveAssetPackage(financialContract, loanBatch, loan_contract);
            log.info("#ImportAssetPackageHandlerImpl importAssetPackage: saveAssetPackage in " + (System.currentTimeMillis() - startTime) + "ms");
            startTime = System.currentTimeMillis();
            List<AssetSet> assetSetList = new ArrayList<>();
            assetSetList = save_repayment_plan_list(contractDetail, loan_contract, financialContract);
            log.info("#ImportAssetPackageHandlerImpl importAssetPackage: save_repayment_plan_list in " + (System.currentTimeMillis() - startTime) + "ms");
            startTime = System.currentTimeMillis();
            book_assets_on_ledger_book_and_value_asset_order(financialContract, assetSetList, customer.getCustomerUuid());
            log.info("#ImportAssetPackageHandlerImpl importAssetPackage: book_assets_on_ledger_book_and_value_asset_order in " + (System.currentTimeMillis() - startTime) + "ms");
            recordDailyAsset(financialContract, contractDetail, assetSetList, contractAccount, yntrustFileTask.getRebuildPath());
        }

        return new ImportAssetPackageResponseModel(model.getRequestNo(), loanBatch.getCode());
    }

    private void recordDailyAsset(FinancialContract financialContract, ContractDetail contractDetail, List<AssetSet> assetSetList, ContractAccount contractAccount, String path) {
        if (StringUtils.isEmpty(path)) {
            log.warn("#ImportAssetPackageHandlerImpl recordDailyAsset fail cause empty path");
            return;
        }
        if (CollectionUtils.isEmpty(assetSetList)) {
            log.warn("#ImportAssetPackageHandlerImpl recordDailyAsset fail cause empty assetSetList");
            return;
        }
        Map<Integer, String> periodMap = assetSetList.stream().collect(Collectors.toMap(AssetSet::getCurrentPeriod, AssetSet::getSingleLoanContractNo));
        List<String> repaymentPlanDetailJsonList = contractDetail.getRepaymentPlanDetails().stream().map(repaymentPlanDetail -> JsonUtils.toJSONString(repaymentPlanDetail)).collect(Collectors.toList());
        dailyReconciliationHandler.dailyAssetIncrementRecord(financialContract.getContractNo(), contractDetail.getUniqueId(), contractAccount.getPayerName(),
                contractDetail.getLoanContractNo(), contractAccount.getBank(), contractAccount.getPayAcNo(), contractAccount.getProvince(), contractAccount.getCity(),
                periodMap, repaymentPlanDetailJsonList, path, true, true);
    }

    private Map<String, String> getStringMap(ContractDetail contractDetail) {
        Map<String, String> detailMap = new HashMap<>();
        detailMap.put("subjectMatterassetNo", contractDetail.getSubjectMatterassetNo());
        detailMap.put("loanCustomerName", contractDetail.getLoanCustomerName());
        detailMap.put("iDCardNo", contractDetail.getIDCardNo());
        detailMap.put("loanCustomerNo", contractDetail.getLoanCustomerNo());
        detailMap.put("mobile", contractDetail.getMobile());
        detailMap.put("repaymentAccountNo", contractDetail.getRepaymentAccountNo());
        detailMap.put("bankCode", contractDetail.getBankCode());
        detailMap.put("bankOfTheProvince", contractDetail.getBankOfTheProvince());
        detailMap.put("bankOfTheCity", contractDetail.getBankOfTheCity());
        return detailMap;
    }

    /**
     * 获取放款脚本需要的值
     */
    private void getRemittanceTemplateValue(FinancialContract
                                                    financialContract,
                                            Contract contract, Customer
                                                    customer, String requestNo)
            throws
            BusinessSystemException, MakeTemplateGenerateShlefException {
        long startGetBookByBookNo = System.currentTimeMillis();
        if (financialContract == null || contract == null) {
            log.info("#Remittacne generate ledgerBookShlef error by" +
                    RemittanceGenerateShelfException.FINANCIAL_CONTRACT_IS_NULL);
            return;
        }
        LedgerBook ledgerBook = null;
        ledgerBook = ledgerBookService.getBookByBookNo(financialContract.getLedgerBookNo());
        if (ledgerBook == null) {
            log.info("#Remittacne generate ledgerBookShlef error by" + RemittanceGenerateShelfException.LEDGER_BOOK_IS_NULL);
            return;
        }
        log.info("[" + requestNo + "]  get book by book no use time : " + (System.currentTimeMillis() - startGetBookByBookNo));

        String fstParty = financialContract.getCompany() == null ? null : financialContract.getCompany().getUuid();
        LedgerTradeParty ledgerTradeParty = new LedgerTradeParty(fstParty, customer.getCustomerUuid());

        long startEvealRemittanceTemplate = System.currentTimeMillis();
        evealRemittanceTemplate(ledgerBook, contract, ledgerTradeParty);
        log.info("[" + requestNo + "]  eveal remittance template no use time : " + (System.currentTimeMillis() - startEvealRemittanceTemplate));

    }

    /**
     * 生成放款流水脚本
     *
     * @param ledgerBook
     * @param loan_contract
     * @param ledgerTradeParty
     */
    private void evealRemittanceTemplate(LedgerBook ledgerBook, Contract loan_contract, LedgerTradeParty ledgerTradeParty) throws BusinessSystemException, MakeTemplateGenerateShlefException {
        log.info("ledgerbookno:" + ledgerBook.getLedgerBookNo() + "contractUniqueID:" + loan_contract.getUniqueId());
        //根据贷款合同UniqueId 查出放款单
        List<RemittanceApplication> remittanceApplicationslist = iRemittanceApplicationService.getRemittanceApplicationsBy(loan_contract.getUniqueId());
        //放款单为空
        if (CollectionUtils.isEmpty(remittanceApplicationslist)) {
            throw returnBusinessSystemException(RemittanceGenerateShelfException.REMITTANCE_APPLIACTION_NOT_FIND);
        }
        for (RemittanceApplication remittanceApplication : remittanceApplicationslist) {
            List<RemittancePlan> remittancePlanList = iRemittancePlanService.getRemittancePlanListBy(remittanceApplication.getRemittanceApplicationUuid());
            if (CollectionUtils.isEmpty(remittancePlanList)) {
                throw returnBusinessSystemException(RemittanceGenerateShelfException.REMITTANCE_PLAN_NOT_FIND);
            }
            for (RemittancePlan remittancePlan : remittancePlanList) {
                List<RemittancePlanExecLog> remittancePlanExecLogList = iRemittancePlanExecLogService.getRemittancePlanExecLogListBy(remittancePlan.getRemittancePlanUuid());
                if (CollectionUtils.isEmpty(remittancePlanExecLogList)) {
                    throw returnBusinessSystemException(RemittanceGenerateShelfException.REMITTANCE_PLAN_EXECLOG_IS_NULL);
                }
                for (RemittancePlanExecLog remittancePlanExecLog : remittancePlanExecLogList) {
                    CashFlow cashFlow = cashFlowService.getCashFLowByTradeUUid(remittancePlanExecLog.getExecRspNo());
                    if (cashFlow == null) {
                        throw returnBusinessSystemException(RemittanceGenerateShelfException.CASHFLOW_NOT_EXIST);
                    }
                    log.info("放款流水(Cashflow):" + cashFlow);
                    remittanceApplicationHandler.remittance_Application_V(cashFlow, ledgerBook.getLedgerBookNo(), ledgerTradeParty, loan_contract);
                }
            }
        }
    }

    private BusinessSystemException returnBusinessSystemException(String message) {
        return new BusinessSystemException(message);
    }

    private void book_assets_on_ledger_book_and_value_asset_order(FinancialContract financialContract, List<AssetSet> assetSetList, String customerUuid) throws
            Exception {
        long start1 = System.currentTimeMillis();
        try {
            String companyUuid = financialContract.getCompany() == null ? null : financialContract.getCompany().getUuid();
            LedgerTradeParty party = new LedgerTradeParty(companyUuid + "", customerUuid);
            // 导入资产包双写


            if (ledgerBookV2Handler.checkLedgerbookVersionByledgerBookNo
                    (financialContract.getLedgerBookNo())) {
                log.info(" begin to " +
                        "ImportAssetPackageApiHandlerImpl#book_assets_on_ledger_book_and_value_asset_order !!!");
                long start = System.currentTimeMillis();

                ledgerBookV2Handler.book_loan_assets_v2_preV2(financialContract
                        .getLedgerBookNo(), assetSetList, party);
                log.info("合同号：" + assetSetList.get(0).getContractUuid() + " book_loan_assets[脚本]_used:" + (System.currentTimeMillis() - start));
                log.info("end !!!");
            }
            if (ledgerBookV2Handler.checkLedgerbookVersionByledgerBookNoV1(financialContract.getLedgerBookNo())) {
                long start2 = System.currentTimeMillis();
                ledgerBookHandler.book_loan_assets(financialContract.getLedgerBookNo(), assetSetList, party);
                log.info("合同号：" + assetSetList.get(0).getContractUuid() + "book_loan_assets[程序]used:" + (System.currentTimeMillis() - start2));
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("book_assets_on_ledger_book error.  customerUuid[" + customerUuid + "].");
            throw e;
        }
        try {// 新增还款计划同步
            Contract contract = assetSetList.get(0).getContract();
            BusinessLogsModel model = new BusinessLogsModel(OperateMode.INSERT, null, null, null);
            repaymentPlanHandler.saveBusinessLogs(contract, assetSetList, model);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (AssetSet assetSet : assetSetList) {
            if (assetSet != null && needValuationForImportAssetPackage(assetSet, new Date())) {
                // orderHandler.assetValuationAndCreateOrder(assetSet.getId(),
                // new Date());
                repaymentPlanValuationHandler.valuate_repayment_plan_and_system_create_order(assetSet.getAssetUuid(), new Date());
            }
        }
        log.info("合同号：" + assetSetList.get(0).getContractUuid() + " book_loan_assets all used:" + (System.currentTimeMillis() - start1));

    }

    /**
     * 导入当日或以前的还款计划，需要评估
     */
    private boolean needValuationForImportAssetPackage(AssetSet assetSet, Date valuation_date) {
        if (assetSet.getAssetStatus().ordinal() != AssetClearStatus.UNCLEAR.ordinal()) {
            return false;
        }
        return DateUtils.compareTwoDatesOnDay(assetSet.getAssetRecycleDate(), valuation_date) <= 0;
    }

    private FinancialContract judgeFinancialProductCodeAndReturnVaildFinancialContract(ImportAssetPackageContent requestContent) {
        FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(requestContent.getFinancialProductCode());
        if (financialContract == null) {
            log.error("judgeFinancialProductCodeAndReturnVaildFinancialContract has error, 信托产品代码错误!");
            processError(FINANCIAL_PRODUCT_CODE_ERROR);
        }
        return financialContract;
    }

    private void validateContractDetails(ContractDetail contractDetail, ImportAssetPackageContent requestContent, boolean not_offline_remittance, boolean notNullRemittance) {
        checkContractNoAndUniqueId(contractDetail);
        checkContractAmountAndNumber(contractDetail);
        checkFirstPeriodAssetRecycleDate(contractDetail, not_offline_remittance, notNullRemittance);
        checkTheSubjectMatterNo(contractDetail);
        checkCityAndProvinceCodeIsMatch(contractDetail);
        checkTrustCodeAndRemittanceOfTrustCodeIsMatch(contractDetail, requestContent, not_offline_remittance);
    }

    private void checkTrustCodeAndRemittanceOfTrustCodeIsMatch(ContractDetail contractDetail, ImportAssetPackageContent requestContent, boolean
            not_offline_remittance) {
        if (!not_offline_remittance) {
            log.warn("checkTrustCodeAndRemittanceOfTrustCodeIsMatch, 线下放款模式不需要校验放款信息!");
            return;
        }
        List<RemittanceApplication> remittanceApplications = remittancetApplicationHandler.getAnyRemittanceList(contractDetail.getUniqueId());
        // 非线下放款模式 需要校验放款信息
        if (remittanceApplications == null) {
            String error_msg = "未查询到合同对应放款";
            log.error("checkTrustCodeAndRemittanceOfTrustCodeIsMatch has error, " + error_msg);
            processError(NO_MATCH_REMITTANCE);
        }
        BigDecimal actualTotalAmount = BigDecimal.ZERO;
        for (RemittanceApplication remittanceApplication : remittanceApplications) {
            // 查到放款信息 就校验
            if (!remittanceApplication.getFinancialProductCode().equals(requestContent.getFinancialProductCode())) {
                log.error("checkTrustCodeAndRemittanceOfTrustCodeIsMatch has error, 信托代码与对应放款中信托代码不匹配!");
                processError(TRUST_CODE_NOT_AGREE_WITH_TRUST_CODE_OF_REMITTANCE);
            }
            actualTotalAmount = actualTotalAmount.add(remittanceApplication.getActualTotalAmount());
        }
        // 校验本金是否等于放款成功总额
        if (AmountUtils.notEquals(actualTotalAmount, (new BigDecimal(contractDetail.getLoanTotalAmount())))) {
            String error_msg = "贷款合同[" + contractDetail.getUniqueId() + "]本金和放款成功金额不等!";
            log.error("checkTrustCodeAndRemittanceOfTrustCodeIsMatch has error, " + error_msg);
            processError(LOAN_TOTAL_AMOUNT_NOT_MATCH_REMITTANCE_AMOUNT);
        }
    }

    private void checkContractNoAndUniqueId(ContractDetail contractDetail) {
        String contractNo = contractDetail.getLoanContractNo();
        String uniqueId = contractDetail.getUniqueId();
        Contract exist_contract = contractService.getContractByContractNo(contractNo);
        if (null != exist_contract) {
            processError(EXIST_LOAN_CONTRACT_NO);
        }
        exist_contract = contractService.getContractByUniqueId(uniqueId);
        if (null != exist_contract) {
            processError(EXIST_LOAN_CONTRACT_UNIQUE_ID);
        }
    }

    private void checkTheSubjectMatterNo(ContractDetail contractDetail) {
        if (StringUtils.isEmpty(contractDetail.getSubjectMatterassetNo())) {
            return;
        }
        House house = houseService.getHouseByAddress(contractDetail.getSubjectMatterassetNo());
        if (house != null) {
            processError(EXIST_THE_SUBJECT_MATTER);
        }
    }

    @Override
    public void checkFirstPeriodAssetRecycleDate(ContractDetail contractDetail, boolean not_offline_remittance, boolean notNullRemittance) {

        Date usedContractEffectDate = StringUtils.isEmpty(contractDetail.getEffectDate()) ? null : DateUtils.asDay(contractDetail.getEffectDate());
        if (usedContractEffectDate == null) {
            processError(EFFECT_DATE_ERROR);
        }
        Date firstPeriodAssetRecycleDate = getFirstRepaymentPlanRecycleDate(contractDetail, usedContractEffectDate);
        if (usedContractEffectDate != null) {
            if (isFirstRecycleDateBeforeEffectDate(usedContractEffectDate, firstPeriodAssetRecycleDate) && notNullRemittance) {
                processError(FIRST_REPAYMENT_DATE_NOT_CORRECT);
            }
        }
//        if (isFirstRecycleDateBeforeCurrentDay(firstPeriodAssetRecycleDate) && notNullRemittance) {
//            processError(FIRST_REPAYMENT_DATE_NOT_CORRECT);
//        }
    }

    private boolean isFirstRecycleDateBeforeCurrentDay(Date firstPeriodAssetRecycleDate) {
        return DateUtils.asDay(firstPeriodAssetRecycleDate).compareTo(DateUtils.getToday()) <= 0;
    }

    private void processError(ApiMessage apiMessage) {
        throw new ApiException(apiMessage);
    }

    private Date getBeginingLoanDateInRemittance(ContractDetail contractDetail) {
        return remittancetApplicationHandler.getFirstLoanDateInRemittance(contractDetail.getUniqueId());
    }

    private Date getFirstRepaymentPlanRecycleDate(ContractDetail contractDetail, Date contractEffectDate) {
        if (!CollectionUtils.isEmpty(contractDetail.getRepaymentPlanDetails())) {
            return DateUtils.asDay(contractDetail.getRepaymentPlanDetails().get(0).getRepaymentDate());
        }
        return DateUtils.addMonths(contractEffectDate, 1);
    }

    private boolean isFirstRecycleDateBeforeEffectDate(Date contractEffectDate, Date firstPeriodAssetRecycleDate) {
        return DateUtils.asDay(firstPeriodAssetRecycleDate).before(DateUtils.asDay(contractEffectDate));
    }

    private void checkContractAmountAndNumber(ContractDetail contractDetail) {
        BigDecimal theContractLoanAmount = new BigDecimal(contractDetail.getLoanTotalAmount());
        int allPeriods = contractDetail.getLoanPeriods();
        List<ImportRepaymentPlanDetail> repaymentPlans = contractDetail.getRepaymentPlanDetails();
        if (allPeriods != repaymentPlans.size()) {
            processError(REPAYMENT_PLAN_TOTAL_PERIODS_ERROR);
        }
        Map<String, String> preRequest = new HashMap<>();
        List<String> interestArray = new ArrayList<>();
        List<String> principalArray = new ArrayList<>();
        BigDecimal sum = BigDecimal.ZERO;
        for (ImportRepaymentPlanDetail repaymentPlan : repaymentPlans) {
            interestArray.add(repaymentPlan.getRepaymentInterest());
            principalArray.add(repaymentPlan.getRepaymentPrincipal());
            sum = new BigDecimal(repaymentPlan.getRepaymentPrincipal()).add(sum);
        }
        preRequest.put("interest", JsonUtils.toJSONString(interestArray));
        preRequest.put("principal", JsonUtils.toJSONString(principalArray));
        if (weiFangProperties.isEnable()) {
            ProductCategory productCategory = productCategoryCacheHandler.get("importAssetPackage/weifang/10001", true);
            CustomizeServices services = (CustomizeServices) productCategoryCacheHandler.getScript(productCategory);
            boolean result = services.evaluate(sandboxDataSetHandler, preRequest, new HashMap<>(), LogFactory.getLog(ImportAssetPackageController.class));
            if (!result) {
                processError(CUSTOMER_FEE_CHECK_FAIL);
            }
        }
        if (theContractLoanAmount.compareTo(sum) != 0) {
            processError(REPAYMENT_PLAN_TOTAL_AMOUNT_ERROR);
        }
    }

    private AssetSet saveAssetSet(Contract loan_contract, int currentPeriod, ImportRepaymentPlanDetail repaymentPlan,
                                  String financialContractNo) {
        Date assetRecycleDate = DateUtils.asDay(repaymentPlan.getRepaymentDate());
        BigDecimal assetPrincipalValue = new BigDecimal(repaymentPlan.getRepaymentPrincipal());
        BigDecimal assetInterestValue = new BigDecimal(repaymentPlan.getRepaymentInterest());

        String assetFingerPrint = repaymentPlan.assetFingerPrint();
        String assetExtraFeeFingerPrint = repaymentPlan.assetExtraFeeFingerPrint();

        BigDecimal theAdditionalFee = repaymentPlan.getTheAdditionalFee();
        // financialContractNo+repayScheduleNo转成MD5
        String repayScheduleNo = repaymentPlan.getRepayScheduleNo();
        String repayScheduleNo4MD5 = repaymentPlanService.getRepayScheduleNoMD5(financialContractNo, repayScheduleNo, StringUtils.EMPTY);
        //repaymentPlanService.checkByRepayScheduleNo(repayScheduleNo4MD5);

        AssetSet assetSet = new AssetSet(loan_contract, currentPeriod, assetRecycleDate, assetPrincipalValue,
                assetInterestValue, theAdditionalFee, repayScheduleNo4MD5, repayScheduleNo);

        assetSet.updateFingerPrint(assetFingerPrint, assetExtraFeeFingerPrint);

        saveAssetSetExtraCharge(assetSet, repaymentPlan);
        try {
            repaymentPlanService.saveOrUpdate(assetSet);
        } catch (HibernateException e) {
//            e.printStackTrace();
            // TODO add error log
            throw new ApiException(REPEAT_OUTER_REPAYMENT_PLAN_NO);
        }
        return assetSet;
    }


    private void saveAssetSetExtraCharge(AssetSet assetSet, ImportRepaymentPlanDetail repaymentPlan) {
        repaymentPlanExtraChargeService.createAssetSetExtraCharge(assetSet, repaymentPlan.getBDOtheFee(),
                ChartOfAccount.SND_UNEARNED_LOAN_ASSET_OTHER_FEE);
        repaymentPlanExtraChargeService.createAssetSetExtraCharge(assetSet, repaymentPlan.getBDLoanServiceFee(),
                ChartOfAccount.SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE);
        repaymentPlanExtraChargeService.createAssetSetExtraCharge(assetSet, repaymentPlan.getBDTechMaintenanceFee(),
                ChartOfAccount.SND_UNEARNED_LOAN_ASSET_TECH_FEE);
    }

    private List<AssetSet> save_repayment_plan_list(ContractDetail contractDetail, Contract loan_contract,
                                                    FinancialContract financialContract) {
        List<AssetSet> assetSets = new ArrayList<AssetSet>();
        List<ImportRepaymentPlanDetail> repaymentPlanDetails = contractDetail.getRepaymentPlanDetails();
        for (int index = 0; index < repaymentPlanDetails.size(); index++) {
            ImportRepaymentPlanDetail repaymentPlan = repaymentPlanDetails.get(index);
            int current_period = index + 1;
            AssetSet assetSet = saveAssetSet(loan_contract, current_period, repaymentPlan, financialContract.getContractNo());
            assetSets.add(assetSet);
        }
        return assetSets;
    }

    private void saveAssetPackage(FinancialContract financialContract, LoanBatch loanBatch, Contract loan_contract) {
        AssetPackage assetPackage = new AssetPackage(financialContract, loan_contract, loanBatch);
        assetPackageService.saveOrUpdate(assetPackage);
    }

    private LoanBatch saveLoanBatch(FinancialContract financialContract, String requestBatchNo) {
        LoanBatch loanBatch = loanBatchService.createAndSaveLoanBatch(financialContract, true, requestBatchNo);
        return loanBatch;
    }

    private Customer saveCustomer(Map<String, String> detailMap, App app, FinancialContract fc) {
        Customer customer = new Customer(detailMap, app);

        boolean exist = customerService.existSameIDCardNo(detailMap.get("iDCardNo"));
        if (exist) {
            customer.setStatus(false);
        }
        if (fc.getFinancialContractType() != null && fc.getFinancialContractType() == FinancialContractType.SmallBusinessMicroCredit) {
            customer.setCustomerStyle(CustomerStyle.enterprise);
        }
        customerService.saveOrUpdate(customer);

        // 在保存数据到customer
        CustomerPerson customerPerson = personService.getCustomerPersonBy(customer.getCustomerUuid());
        if (null == customerPerson) {
            customerPerson = new CustomerPerson();
            customerPerson.setCustomerUuid(customer.getCustomerUuid());
            customerPerson.setUuid(UUID.randomUUID().toString());
            customerPerson.setName(customer.getName());
        }
        String account = customer.getAccount();
        if (Pattern.matches(GlobalSpec.IDCard.REGEXP_FOR_LENGTH_18, account) ||
            Pattern.matches(GlobalSpec.IDCard.REGEXP_FOR_LENGTH_15, account)) {
            if (GlobalSpec.IDCard.LENGTH_FOR_18_DIGITAL_ID_CARD == account.length()) {
                String birthday = account.substring(6, 14);
                customerPerson.setBirthday(DateUtils.parseDate(birthday, "yyyyMMdd"));
                String sexFlag = account.substring(16, 17);
                Sex sex = Integer.parseInt(sexFlag) % 2 == 0 ? Sex.woman : Sex.man;
                customerPerson.setSex(sex);
            } else if (GlobalSpec.IDCard.LENGHT_FOR_15_DIGITAL_ID_CARD == account.length()) {
                String birthday = "19" + account.substring(6, 12);
                customerPerson.setBirthday(DateUtils.parseDate(birthday, "yyyyMMdd"));
                String sexFlag = account.substring(14, 15);
                Sex sex = Integer.parseInt(sexFlag) % 2 == 0 ? Sex.woman : Sex.man;
                customerPerson.setSex(sex);
            }
        }
        personService.saveOrUpdate(customerPerson);

        BankCard bankCard = new BankCard(customer.getCustomerUuid(), Identity.customer, detailMap.get("loanCustomerName"), detailMap.get("repaymentAccountNo"),
                null, detailMap.get("bankCode"), null, detailMap.get("bankOfTheProvince"), null, detailMap.get("bankOfTheCity"));
        bankCardService.save(bankCard);

        return customer;
    }

    private House saveHouse(Map<String, String> detailMap, App app) {
        House house = new House(detailMap, app);
        houseService.saveOrUpdate(house);
        return house;
    }

    private Contract saveContract(ContractDetail contractDetail, App app, Customer customer, House house,
                                  FinancialContract financialContract, boolean not_offline_remittance) throws ParseException {
        Contract loan_contract = new Contract();
        Date beginningLoanDate = DateUtils.asDay(contractDetail.getEffectDate());
        contractDetail.copyToContract(app, customer, house, loan_contract, financialContract, beginningLoanDate);
        contractService.saveOrUpdate(loan_contract);
        return loan_contract;
    }

    private ContractAccount saveContractAccount(Map<String, String> detailMap, Contract loan_contract) {
        String bankCode = detailMap.get("bankCode");
        String repaymentAccountNo = detailMap.get("repaymentAccountNo");
        Bank bank = getBank(bankCode, repaymentAccountNo);
        String provinceName = getProvinceName(detailMap);
        String cityName = getCityName(detailMap);
        ContractAccount contractAccount = new ContractAccount(loan_contract, detailMap, bank, provinceName, cityName);
        if (bank == null) {
            String bankCodeZhongHang = zhonghangResponseMapSpecUtil.getBankCodeBy(bankCode);
            String bankNameZhongHang = zhonghangResponseMapSpecUtil.getBankNameBy(bankCode);
            contractAccount.setBank(bankNameZhongHang);
            contractAccount.setBankCode(bankCode);
            contractAccount.setStandardBankCode(bankCodeZhongHang);
        }
        contractAccountService.saveOrUpdate(contractAccount);
        return contractAccount;
    }

    /**
     * 获取银行信息
     *
     * @param bankCode
     * @return 银行
     */
    private Bank getBank(String bankCode, String repaymentAccountNo) {
        if (StringUtils.isEmpty(bankCode)) {
            Map<String, String> cardBinBankCodeMap = cardBinService.getCachedCardBins();
            bankCode = CardBinUtil.getBankCodeViaCardNo(cardBinBankCodeMap, repaymentAccountNo);
        }
        Bank bank = bankService.getCachedBanks().getOrDefault(bankCode, null);
        if (bank == null) {
            String standardBankCode = ZhonghangResponseMapSpec.BANK_CODE_NAME.getOrDefault(bankCode, null);
            if (StringUtils.isEmpty(standardBankCode)) {
                log.error("getBank has error, bank is null and standardBankCode is not found!");
                processError(NO_MATCH_BANK);
            }
        }
        return bank;
    }

    /**
     * 获取 开户行所在城市名称
     *
     * @param detailMap 贷款合同信息相关map
     * @return 开户行所在城市名称; 合同信息为空时，返回空
     */
    private String getCityName(Map<String, String> detailMap) {
        String cityName = StringUtils.EMPTY;
        String bankOfTheCity = detailMap.get("bankOfTheCity");
        if (StringUtils.isEmpty(bankOfTheCity)) {
            log.warn("getCityName has warn, bankOfTheCity is empty!");
            return cityName;
        }
        City city = cityService.getCityByCityCode(bankOfTheCity);
        if (city == null) {
            log.error("getCityName has error, not find in DB!");
            processError(NO_MATCH_CITY);
        }
        return city.getName();
    }

    /**
     * 获取 开户行所在省份名称
     *
     * @param detailMap 贷款合同信息相关Map
     * @return 开户行所在省份名称; 合同信息为空时，返回空
     */
    private String getProvinceName(Map<String, String> detailMap) {
        String provinceName = StringUtils.EMPTY;
        String bankOfTheProvince = detailMap.get("bankOfTheProvince");
        if (StringUtils.isEmpty(bankOfTheProvince)) {
            log.warn("getProvinceName has warn, bankOfTheProvince is empty!");
            return provinceName;
        }
        Province province = provinceService.getProvinceByCode(bankOfTheProvince);
        if (province == null) {
            log.error("getProvinceName has error, not find in DB!");
            processError(NO_MATCH_PROVINCE);
        }
        return province.getName();
    }

    @Override
    public void dataVerification(ImportAssetPackage model, boolean not_offline_remittance, boolean notNullRemittance) {
        // 校验请求数据内容正确性
        validateRequestModelData(model);
        // 校验请求数据逻辑正确性
        vaildateRequestLogicData(model, not_offline_remittance, notNullRemittance);
    }

    private void vaildateRequestLogicData(ImportAssetPackage model, boolean not_offline_remittance, boolean notNullRemittance) {
        ImportAssetPackageContent requestContent = model.getRequestContentObject();
        requestContent.validateTotalAmountAndNumber();

        List<ContractDetail> contractDetails = requestContent.getContractDetails();
        for (ContractDetail contractDetail : contractDetails) {
            validateContractDetails(contractDetail, requestContent, not_offline_remittance, notNullRemittance);
        }
    }

    private void validateRequestModelData(ImportAssetPackage model) {

        ImportAssetPackageContent requestContent = model.getRequestContentObject();
        validateTotalData(requestContent);
        List<ContractDetail> contractDetails = requestContent.getContractDetails();
        for (ContractDetail contractDetail : contractDetails) {
            validateContractData(contractDetail);
            List<ImportRepaymentPlanDetail> repaymentPlans = contractDetail.getRepaymentPlanDetails();
            for (ImportRepaymentPlanDetail repaymentPlanDetail : repaymentPlans) {
                validateRepaymentPlanData(repaymentPlanDetail);
            }
            // 校验合同下还款计划是否有序
            checkRepaymentDetailsRepaymentDate(repaymentPlans);
        }
    }

    private void checkRepaymentDetailsRepaymentDate(List<ImportRepaymentPlanDetail> repaymentPlans) {
        Date maxDate = DateUtils.asDay(repaymentPlans.get(0).getRepaymentDate());
        for (int index = 0; index < repaymentPlans.size(); index++) {
            if (maxDate.after(DateUtils.asDay(repaymentPlans.get(index).getRepaymentDate()))) {
                throw new ApiException(REPAYMENT_PLAN_REPAYMENT_DATE_ERROR);
            }
            maxDate = DateUtils.asDay(repaymentPlans.get(index).getRepaymentDate());
        }
    }

    private void validateTotalData(ImportAssetPackageContent requestContent) {
        if (requestContent.getThisBatchContractsTotalNumber() <= 0) {
            processError(TOTAL_NUMBER_ERROR);
        }
        if (StringUtils.isEmpty(requestContent.getThisBatchContractsTotalAmount())
                || !CheckFormatUtils.checkRMBCurrency(requestContent.getThisBatchContractsTotalAmount())) {
            processError(TOTAL_AMOUNT_ERROR);
        }
        if (CollectionUtils.isEmpty(requestContent.getContractDetails())) {
            processError(TOTAL_CONTRACTS_NUMBER_NOT_MATCH);
        }
        if (StringUtils.isEmpty(requestContent.getFinancialProductCode())) {
            processError(FINANCIAL_PRODUCT_CODE_ERROR);
        }
        for (ContractDetail contractDetail : requestContent.getContractDetails()) {
            if (CollectionUtils.isEmpty(contractDetail.getRepaymentPlanDetails())) {
                processError(REPAYMENT_PLAN_TOTAL_PERIODS_ERROR);
            }
        }
    }

    private void validateContractData(ContractDetail contractDetail) {
        if (StringUtils.isEmpty(contractDetail.getLoanCustomerName())) {
            processError(LOAN_CUSTOMER_NAME_ERROR);
        }
        if (StringUtils.isEmpty(contractDetail.getLoanCustomerNo())) {
            processError(LOAN_CUSTOMER_NO_ERROR);
        }
        if (StringUtils.isEmpty(contractDetail.getIDCardNo()) || !CheckFormatUtils.checkIDCard(contractDetail.getIDCardNo())) {
            processError(ID_CARD_ERROR);
        }
        if (StringUtils.isEmpty(contractDetail.getLoanTotalAmount()) || !CheckFormatUtils.checkRMBCurrency(contractDetail.getLoanTotalAmount())) {
            processError(LOAN_TOTAL_AMOUNT_ERROR);
        }
        if (contractDetail.getLoanPeriods() <= 0) {
            processError(LOAN_PERIODS_ERROR);
        }
        if (StringUtils.isEmpty(contractDetail.getLoanContractNo()) || StringUtils.isEmpty(contractDetail.getUniqueId())) {
            processError(LOAN_CONTRACT_NO_OR_UNIQUEID_IS_EMPTY);
        }
        if (StringUtils.isEmpty(contractDetail.getBankCode())) {
            String bankCardNo = contractDetail.getRepaymentAccountNo();
            Map<String, String> cardBinBankCodeMap = cardBinService.getCachedCardBins();
            String bankCode = CardBinUtil.getBankCodeViaCardNo(cardBinBankCodeMap, bankCardNo);
            contractDetail.setBankCode(bankCode);
        }
        if (StringUtils.isEmpty(contractDetail.getBankCode())) {
            processError(NO_MATCH_BANK);
        }

        /**
         * 应客户要求 ZHONGHANG 放宽省市代码Code 必填校验
         */
//		if (StringUtils.isEmpty(contractDetail.getBankOfTheProvince())) {
//			processError(NO_MATCH_PROVINCE);
//		}
//		if (StringUtils.isEmpty(contractDetail.getBankOfTheCity())) {
//			processError(NO_MATCH_CITY);
//		}
        if (StringUtils.isEmpty(contractDetail.getRepaymentAccountNo())) {
            processError(REPAYMENT_ACCOUNT_ERROR);
        }
        if (!StringUtils.isEmpty(contractDetail.getEffectDate()) && DateUtils.asDay(contractDetail.getEffectDate()) == null) {
            processError(EFFECT_DATE_ERROR);
        }
        if (!StringUtils.isEmpty(contractDetail.getExpiryDate()) && DateUtils.asDay(contractDetail.getExpiryDate()) == null) {
            processError(EXPIRE_DATE_ERROR);
        }
        if (StringUtils.isEmpty(contractDetail.getLoanRates()) || !CheckFormatUtils.checkRMBCurrency(contractDetail.getLoanRates())) {
            processError(LOAN_RATES_ERROR);
        }
        if (contractDetail.getPenalty() != null && !CheckFormatUtils.checkRMBCurrency(contractDetail.getPenalty())) {
            processError(PENALTY_ERROR);
        }
        if (RepaymentWay.fromValue(contractDetail.getRepaymentWay()) == null) {
            processError(NO_MATCH_REPAYMENT_WAY);
        }
    }

    private void validateRepaymentPlanData(ImportRepaymentPlanDetail repaymentPlanDetail) {
        if (StringUtils.isEmpty(repaymentPlanDetail.getRepaymentDate())
                || DateUtils.asDay(repaymentPlanDetail.getRepaymentDate()) == null) {
            processError(REPAYMENT_PLAN_REPAYMENT_DATE_ERROR);
        }
        BigDecimal repaymentPlanTotalFee = BigDecimal.ZERO;
        if (!CheckFormatUtils.checkRMBCurrency(repaymentPlanDetail.getRepaymentPrincipal())) {
            processError(REPAYMENT_PRINCIPAL_ERROR);
        }
        repaymentPlanTotalFee = repaymentPlanTotalFee.add(new BigDecimal(repaymentPlanDetail.getRepaymentPrincipal()));
        if (!CheckFormatUtils.checkRMBCurrency(repaymentPlanDetail.getRepaymentInterest())) {
            processError(REPAYMENT_INTEREST_ERROR);
        }
        repaymentPlanTotalFee = repaymentPlanTotalFee.add(new BigDecimal(repaymentPlanDetail.getRepaymentInterest()));
        if (!CheckFormatUtils.checkRMBCurrency(repaymentPlanDetail.getTechMaintenanceFee())) {
            processError(TECH_MAINTENANCE_FEE_ERROR);
        }
        repaymentPlanTotalFee = repaymentPlanTotalFee.add(new BigDecimal(repaymentPlanDetail.getTechMaintenanceFee()));
        if (!CheckFormatUtils.checkRMBCurrency(repaymentPlanDetail.getLoanServiceFee())) {
            processError(LOAN_SERVICE_FEE_ERROR);
        }
        repaymentPlanTotalFee = repaymentPlanTotalFee.add(new BigDecimal(repaymentPlanDetail.getLoanServiceFee()));
        if (!CheckFormatUtils.checkRMBCurrency(repaymentPlanDetail.getOtheFee())) {
            processError(OTHER_FEE_ERROR);
        }
        repaymentPlanTotalFee = repaymentPlanTotalFee.add(new BigDecimal(repaymentPlanDetail.getOtheFee()));
        if (repaymentPlanTotalFee.compareTo(BigDecimal.ZERO) == 0) {
            processError(REPAYMENT_PLAN_TOTAL_AMOUNT_ERROR);
        }
    }

    private void checkCityAndProvinceCodeIsMatch(ContractDetail contractDetail) {
        String bankOfTheCity = contractDetail.getBankOfTheCity();
        if (StringUtils.isEmpty(bankOfTheCity)) {
            log.warn("checkCityAndProvinceCodeIsMatch#bankOfTheCity is empty.");
            return;
        }
        City city = cityService.getCityByCityCode(bankOfTheCity);
        if (city == null || !city.getProvinceCode().equals(contractDetail.getBankOfTheProvince())) {
            log.error("checkCityAndProvinceCodeIsMatch has error, 城市与省份代码不匹配.");
            processError(CITY_AND_PROVINCE_CODE_DOES_NOT_MATCH);
        }
    }

    @Override
    public List<String> checkContractEffectDate(ImportAssetPackage model, boolean not_offline_remittance, boolean notNullRemittance) {
        ImportAssetPackageContent requestContent = model.getRequestContentObject();
        List<ContractDetail> contractDetails = requestContent.getContractDetails();
        List<String> responseMessages = new ArrayList<>();
        for (ContractDetail contractDetail : contractDetails) {
            String responseMessage = getResponseMessage(not_offline_remittance, notNullRemittance, contractDetail);
            if (responseMessage != null) {
                responseMessages.add(responseMessage);
            }
        }
        return responseMessages;
    }

    private String getResponseMessage(boolean not_offline_remittance, boolean notNullRemittance, ContractDetail contractDetail) {
        Date beginingLoanDate = getBeginingLoanDateInRemittance(contractDetail);
        if (not_offline_remittance && beginingLoanDate == null) {
            processError(NO_MATCH_REMITTANCE);
        }
        String responseMessage = null;
//        if (beginingLoanDate != null) {
//            Date pointContractEffectDate = StringUtils.isEmpty(contractDetail.getEffectDate()) ? null : DateUtils.asDay(contractDetail.getEffectDate());
//            if (pointContractEffectDate == null || (beginingLoanDate.compareTo(pointContractEffectDate) != 0 && notNullRemittance)) {
//                log.warn(contractDetail.getLoanContractNo() + "合同生效日期与放款日期不对应，放款日期为" + DateUtils.format(beginingLoanDate));
//                processError(EFFECT_DATE_INVAILD);
//            }
//        }
        return responseMessage;
    }

    @Override
    public FinancialContract checkImportAssetPackageRequestModel(ImportAssetPackage model, HttpServletRequest request) {
        String requestNo = model.getRequestNo();
        importAssetPackageLogService.checkByRequestNo(model.getRequestNo());
        importAssetPackageLogService.generateImportAssetPackageLog(requestNo, request);

        ImportAssetPackageContent requestContent = model.getRequestContentObject();
        FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(requestContent.getFinancialProductCode());
        if (financialContract == null) {
            processError(FINANCIAL_PRODUCT_CODE_ERROR);
        }
        return financialContract;
    }

    @Resource
    private MorganStanleyNotifyConfig morganStanleyNotifyConfig;

    @Override
    public void executeAsyncImport(NotifyJobServerImpl morganStanleyNotifyServer, HttpServletRequest request, ImportAssetPackage model) {
        String path = morganStanleyNotifyConfig.getDbFilePath();
        String requestUrl = morganStanleyNotifyConfig.getRequestUrl();
        String groupName = morganStanleyNotifyConfig.getAsyncGroupName();
        List<String> keyList = importAssetPackageJobHandler.transAndSaveImportAssetPackageJobByRequestModel(model.getRequestNo(), model.getImportAssetPackageContent(), request, model.getCallbackUrl(), path);
        List<NotifyApplication> jobList = importAssetPackageJobHandler.createAsyncJobs(keyList, requestUrl, groupName);
        morganStanleyNotifyServer.pushJobs(jobList);
    }

    final static Integer importSuccess = Integer.valueOf(SUCCESS_FLAG);
    final static Integer importFail = Integer.valueOf(FAIL_FLAG);

    @Override
    public String asyncImportAssetPackage(String subRequestNo) throws Exception {
        log.info("#ImportAssetPackageHandlerImpl asyncImportAssetPackage: entry menthod");
        String responseMessage = "";
        log.info("#ImportAssetPackageHandlerImpl asyncImportAssetPackage: getDataFromRedis");
        Map<String, Object> data = importAssetPackageJobHandler.getDataFromRedis(subRequestNo);
        Integer resultFlag = importFail;
        String uniqueId = "";
        try {
            String financialProductCode = (String) data.getOrDefault(ImportAssetDataRedisSpec.KEY_FINANCIAL_PRODUCT_CODE, "");
            String detailJson = (String) data.getOrDefault(ImportAssetDataRedisSpec.KEY_DATA, "");
            ContractDetail contractDetail = JsonUtils.parse(detailJson, ContractDetail.class);
            uniqueId = contractDetail.getUniqueId();
            String loanBatch = (String) data.getOrDefault(ImportAssetDataRedisSpec.KEY_LOAN_BATCH_ID, "");
            Long loanBatchId = Long.valueOf(loanBatch);

            // 校验model参数
            FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(financialProductCode);
            if (financialContract == null) {
                log.error("judgeFinancialProductCodeAndReturnVaildFinancialContract has error, 信托产品代码错误!");
                processError(FINANCIAL_PRODUCT_CODE_ERROR);
            }
            boolean notOfflineRemittance = financialContract.getRemittanceStrategyMode() != null && financialContract.getRemittanceStrategyMode() != RemittanceStrategyMode.OFFLINE_REMITTANCE;
            boolean notNullRemittance = financialContract.getRemittanceStrategyMode() != RemittanceStrategyMode.NULL_REMITTANCE;
            // 校验生效日期,生成返回信息
            responseMessage = getResponseMessage(notOfflineRemittance, notNullRemittance, contractDetail);
            singleDetailImportAssetPackage(financialContract, contractDetail, notOfflineRemittance, loanBatchId);
            if (StringUtils.isEmpty(responseMessage)) {
                resultFlag = importSuccess;
                responseMessage = getApiMessageString(SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultFlag = importFail;
            if (e instanceof ApiException) {
                responseMessage = getApiMessageString(((ApiException) e).getCode(), e.getMessage());
            } else {
                responseMessage = e.getMessage();
            }
        } finally {
            importAssetPackageJobHandler.postProcess(data, subRequestNo, resultFlag, responseMessage, uniqueId);
            return responseMessage;
        }
    }

    private String getApiMessageString(ApiMessage apiMessage) {
        return getApiMessageString(apiMessage.getCode(), apiMessage.getMessage());
    }

    private String getApiMessageString(int code, String message) {
        return "{\"code\":" + code + ",\"message\":\"" + message + "\"}";
    }

    private void singleDetailImportAssetPackage(FinancialContract financialContract, ContractDetail contractDetail, boolean not_offline_remittance, Long loanBatchId) throws Exception {
        Map<String, String> detailMap = getStringMap(contractDetail);
        App app = financialContract.getApp();
        if (app == null) {
            log.error("importAssetPackage has error, 信托合同商户未设置!");
            processError(NOT_SET_APP_IN_FINANCIAL_CONTRACT);
        }
        if (CollectionUtils.isEmpty(contractDetail.getRepaymentPlanDetails())) {
            log.warn("importAssetPackage has error, RepaymentPlanDetails is empty");
            return;
        }
        long startTime = System.currentTimeMillis();
        Customer customer = saveCustomer(detailMap, app, financialContract);
        log.info("#ImportAssetPackageHandlerImpl singleDetailImportAssetPackage: saveCustomer in " + (System.currentTimeMillis() - startTime) + "ms");
        startTime = System.currentTimeMillis();
        House house = saveHouse(detailMap, app);
        log.info("#ImportAssetPackageHandlerImpl singleDetailImportAssetPackage: saveHouse in " + (System.currentTimeMillis() - startTime) + "ms");
        startTime = System.currentTimeMillis();
        Contract loan_contract = contractService.getContractByUniqueId(contractDetail.getUniqueId());
        if (null == loan_contract) {
            loan_contract = saveContract(contractDetail, app, customer, house, financialContract, not_offline_remittance);
        }
        log.info("#ImportAssetPackageHandlerImpl singleDetailImportAssetPackage: saveContract in " + (System.currentTimeMillis() - startTime) + "ms");

        String localKeyWord = "[uniqueId=" + loan_contract.getUniqueId() + "]";
        log.info(GloableLogSpec.AuditLogHeaderSpec() + " MORGANSTANLEY:IMPORT_ASSET_PACKAGE# " + localKeyWord);

        // 放款流水生成
        log.info("#begin to Remittance generate shelf !!!");
        try {
            if (ledgerBookV2Handler.checkLedgerbookVersionByledgerBookNo
                    (financialContract.getLedgerBookNo())) {
                getRemittanceTemplateValue(financialContract, loan_contract,
                        customer, "");
            }
            log.info("begin into no [Template] generate " +
                    "ImportAssetSetGenerateRemittanceledgerBookShelf!!!");
            ledgerBookHandler.generateImportAssetSetRemittanceShelf(financialContract, loan_contract,
                    customer, "");
        } catch (Exception e) {
            log.info("#Remittance generate shelf exception : " + e
                    .getMessage());
        }
        log.info("#end Remittance generate shelf !!!");
        startTime = System.currentTimeMillis();
        ContractAccount contractAccount = saveContractAccount(detailMap, loan_contract);
        log.info("#ImportAssetPackageHandlerImpl singleDetailImportAssetPackage: saveContractAccount in " + (System.currentTimeMillis() - startTime) + "ms");
        startTime = System.currentTimeMillis();
        saveAssetPackage(financialContract, loanBatchId, loan_contract);
        log.info("#ImportAssetPackageHandlerImpl singleDetailImportAssetPackage: saveAssetPackage in " + (System.currentTimeMillis() - startTime) + "ms");
        startTime = System.currentTimeMillis();
        List<AssetSet> assetSetList = new ArrayList<>();
        assetSetList = save_repayment_plan_list(contractDetail, loan_contract, financialContract);
        log.info("#ImportAssetPackageHandlerImpl singleDetailImportAssetPackage: save_repayment_plan_list in " + (System.currentTimeMillis() - startTime) + "ms");
        startTime = System.currentTimeMillis();
        book_assets_on_ledger_book_and_value_asset_order(financialContract, assetSetList, customer.getCustomerUuid());
        log.info("#ImportAssetPackageHandlerImpl singleDetailImportAssetPackage: book_assets_on_ledger_book_and_value_asset_order in " + (System.currentTimeMillis() - startTime) + "ms");
        recordDailyAsset(financialContract, contractDetail, assetSetList, contractAccount, yntrustFileTask.getRebuildPath());
    }

    private void saveAssetPackage(FinancialContract financialContract, Long loanBatchId, Contract loan_contract) {
        LoanBatch loanBatch = new LoanBatch();
        loanBatch.setId(loanBatchId);
        AssetPackage assetPackage = new AssetPackage(financialContract, loan_contract, loanBatch);
        assetPackageService.saveOrUpdate(assetPackage);
    }
}