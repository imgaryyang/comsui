package com.zufangbao.earth.yunxin.api.handler.impl;

import com.suidifu.hathaway.job.ExecutingStatus;
import com.suidifu.hathaway.job.Job;
import com.suidifu.hathaway.job.service.JobService;
import com.zufangbao.earth.yunxin.api.handler.ActivePaymentVoucherHandler;
import com.zufangbao.earth.yunxin.api.handler.BusinessPaymentVoucherHandler;
import com.zufangbao.earth.yunxin.api.model.command.BusinessPaymentVoucherCommandModel;
import com.zufangbao.earth.yunxin.exception.BusinessPaymentVoucherException;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.exception.GlobalRuntimeException;
import com.zufangbao.gluon.exception.voucher.BankTransactionNoExistException;
import com.zufangbao.sun.entity.account.Account;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.contract.ContractActiveSourceDocumentMapper;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.entity.voucher.CreateBusinessPaymentVoucherModel;
import com.zufangbao.sun.entity.voucher.TemplatesForGuarantee;
import com.zufangbao.sun.entity.voucher.TemplatesForPay;
import com.zufangbao.sun.entity.voucher.TemplatesForRepurchase;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.csv.CsvUtils;
import com.zufangbao.sun.yunxin.api.BusinessPaymentVoucherLogService;
import com.zufangbao.sun.yunxin.entity.api.BusinessPaymentVoucherLog;
import com.zufangbao.sun.yunxin.entity.api.VoucherSource;
import com.zufangbao.sun.yunxin.entity.api.VoucherType;
import com.zufangbao.sun.yunxin.entity.model.voucher.AppAccountModelForVoucherController;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherCreateAccountInfoModel;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.*;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.CashFlowHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.VoucherHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentDetailService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.VoucherService;
import com.zufangbao.wellsfargo.yunxin.handler.vouchertask.impl.BusinessPaymentVoucherTaskHandlerImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.*;

/**
 * 商户付款凭证接口
 *
 * @author louguanyang
 */
@Component("businessPaymentVoucherHandler")
public class BusinessPaymentVoucherHandlerImpl extends BusinessPaymentVoucherTaskHandlerImpl implements BusinessPaymentVoucherHandler {

    private static final Log logger = LogFactory.getLog(BusinessPaymentVoucherHandlerImpl.class);

    @Autowired
    private BusinessPaymentVoucherLogService logService;

    @Autowired
    private CashFlowHandler cashFlowHandler;

    @Autowired
    private FinancialContractService financialContractService;
    @Autowired
    private SourceDocumentService sourceDocumentService;

    @Autowired
    private SourceDocumentDetailService sourceDocumentDetailService;
    @Autowired
    private VoucherService voucherService;

    @Autowired
    private CashFlowService cashFlowService;

    @Autowired
    private VoucherHandler voucherHandler;

    @Autowired
    private RepaymentPlanService repaymentPlanService;

    @Autowired
    private ContractService contractService;
    @Autowired
    private ActivePaymentVoucherHandler activePaymentVoucherHandler;
    @Autowired
    private JobService jobService;

    @Autowired
    private SystemOperateLogService systemOperateLogService;

    @Value("#{config['sourceDocumentDetailsFilePath']}")
    private String sourceDocumentDetailsFilePath;

    @Override
    public List<CashFlow> businessPaymentVoucher(BusinessPaymentVoucherCommandModel model, String ip) {
        checkByRequestNo(model.getRequestNo());
        saveLog(model, ip);
        return submitBusinessPaymentVoucher(model);
    }

    @Override
    public void createBusinessPaymentVoucher(CreateBusinessPaymentVoucherModel model) {
        if (model == null) throw new RuntimeException();
        checkByRequestNo(model.getRequestNo());
        FinancialContract financialContract = financialContractService.getFinancialContractBy(model.getFinancialContractUuid());
        if (null == financialContract) {
            throw new BusinessPaymentVoucherException("信托合同不存在!");
        }
        if (voucherService.countVoucherBySecondNo(model.getCreditSerialNumber()) != 0) {
            throw new BusinessPaymentVoucherException("付款流水号已关联");
        }
        CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(model.getCashFlowUuid());
        if (cashFlow == null) throw new BusinessPaymentVoucherException("所选流水不存在");
        submitBusinessPaymentVoucher(model, financialContract, cashFlow);
    }

    private void checkByRequestNo(String requestNo) throws ApiException {
        List<BusinessPaymentVoucherLog> result = logService.getLogByRequestNo(requestNo);
        if (CollectionUtils.isNotEmpty(result)) {
            throw new ApiException(REPEAT_REQUEST_NO);
        }
    }

    /**
     * 撤销商户付款凭证接口
     *
     * @param model
     */
    private void undoBusinessPaymentVoucher(BusinessPaymentVoucherCommandModel model) {
        FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(model.getFinancialContractNo());
        if (financialContract == null) {
            throw new ApiException(FINANCIAL_CONTRACT_NOT_EXIST, "找不到信托计划，信托计划信托计划产品代码：" + model.getFinancialContractNo());
        }
        String financialContractUuid = financialContract.getUuid();
        String bankTransactionNo = model.getBankTransactionNo();
        Voucher voucher = voucherService.getValidVoucher(bankTransactionNo, financialContractUuid);
        if (voucher == null) {
            throw new ApiException(NO_SUCH_VOUCHER);
        }
        String voucherUuid = voucher.getUuid();
//		int count = sourceDocumentDetailService.countSourceDocumentDetailList(voucherUuid);
//		if(count == 0) {
//			throw new ApiException(NO_SUCH_VOUCHER);
//		}
        int success_count = sourceDocumentDetailService.countSuccessSourceDocumentDetailList(voucherUuid);
        if (success_count > 0) {
            throw new ApiException(VOUCHER_CAN_NOT_CANCEL);
        }
        String cashFlowUuid = voucher.getCashFlowUuid();
        voucherHandler.cancelSourceDocumentAndCashFlow(voucherUuid, bankTransactionNo, cashFlowUuid);
        cancelSourceDocumentDetails(voucherUuid);
    }

    /**
     * 是否存在已核销的还款计划
     *
     * @param details
     * @return
     */
    private boolean existClearSourceDocument(List<SourceDocumentDetail> details) {
        return details.stream().filter(detail -> detail.getStatus() == SourceDocumentDetailStatus.SUCCESS).count() > 0;
    }

    /**
     * 提交商户付款凭证接口
     *
     * @param model
     */
    private List<CashFlow> submitBusinessPaymentVoucher(BusinessPaymentVoucherCommandModel model) {
        FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(model.getFinancialContractNo());
        checkReceivableAccountNo(model.getReceivableAccountNo(), financialContract);
        List<CashFlow> unattached_cashFlow_list = getUnattachedCashFlow(model, financialContract);
        if (CollectionUtils.isEmpty(unattached_cashFlow_list)) {
            throw new ApiException(NO_SUCH_CASH_FLOW);
        }
        String cashFlowUuid = null;
        Date transactionTime = null;
        if (unattached_cashFlow_list.size() == 1) {
            CashFlow cashFlow = unattached_cashFlow_list.get(0);
            cashFlowUuid = cashFlow.getUuid();
            transactionTime = cashFlow.getTransactionTime();
            cashFlowService.connectVoucher(cashFlowUuid, model.getBankTransactionNo());
            String bankTransactionNo = cashFlowService.searchBankTransactionNo(cashFlowUuid);
            if (!bankTransactionNo.equals(model.getBankTransactionNo())) {
                throw new ApiException(NO_SUCH_CASH_FLOW);
            }
        }
//		String sourceDocumentUuid = attachedSourceDocumentReturnUuid(financialContract.getCompany().getId(), model.getBankTransactionNo(), unattached_cashFlow_list);
        String financialContractUuid = financialContract.getFinancialContractUuid();
        String detailsFilePath = sourceDocumentDetailsFilePath + "sourceDocumentDetails-"+DateUtils.getFullDateTime(new Date()) + "-" + UUID.randomUUID().toString()+".csv";
        String voucherUuid = createVoucher(model, financialContract, cashFlowUuid, transactionTime, detailsFilePath);
        createSourceDocumentDetails(model, financialContractUuid, voucherUuid, cashFlowUuid, financialContract, detailsFilePath);
        return unattached_cashFlow_list;
    }


    private void submitBusinessPaymentVoucher(CreateBusinessPaymentVoucherModel model, FinancialContract financialContract, CashFlow cashFlow) {
        cashFlowService.connectVoucher(model.getCashFlowUuid(), model.getCreditSerialNumber());
        String voucherUuid = createVoucher(model, financialContract, cashFlow);
        String sourceDocumentUuid = sourceDocumentService.getSourceDocumentUuidByCashFlowUuid(cashFlow.getCashFlowUuid(), financialContract.getFinancialContractUuid());
        createSourceDocumentDetails(model, financialContract, voucherUuid, sourceDocumentUuid, cashFlow);
    }

    /**
     * 校验收款账户
     *
     * @param receivableAccountNo 收款账号
     * @param financialContract   信托合同
     */
    private void checkReceivableAccountNo(String receivableAccountNo, FinancialContract financialContract) {
        try {
            if (null == financialContract) {
                throw new ApiException(FINANCIAL_CONTRACT_NOT_EXIST);
            }
            if (StringUtils.isEmpty(receivableAccountNo)) {
                return;
            }
            String accountNo = financialContract.getCapitalAccount().getAccountNo();
            if (!StringUtils.equals(receivableAccountNo, accountNo)) {
                throw new ApiException(NO_SUCH_RECEIVABLE_ACCOUNT_NO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(NO_SUCH_RECEIVABLE_ACCOUNT_NO, e.getMessage());
        }
    }

    private String createVoucher(BusinessPaymentVoucherCommandModel model, FinancialContract financialContract,
                                 String cashFlowUuid, Date transactionTime, String sourceDocumentDetailsFilePath) {
        //生成新的凭证
        String financialContractUuid = financialContract.getFinancialContractUuid();
        // TODO  去除sourceDocumentUuid 关联 新增 cashFlowUuid
//		Voucher voucher = model.createBusinessVoucher(financialContractUuid, sourceDocumentUuid);
        Voucher voucher = model.createBusinessVoucher(financialContractUuid, cashFlowUuid, transactionTime);
        if (voucher != null) {
            voucher.setSourceDocumentDetailsFilePath(sourceDocumentDetailsFilePath);
            voucherService.saveOrUpdate(voucher);
            return voucher.getUuid();
        }
        return null;
    }

    private String createVoucher(CreateBusinessPaymentVoucherModel model, FinancialContract financialContract, CashFlow cashFlow) throws BusinessPaymentVoucherException {
        //生成新的凭证
        // TODO  去除sourceDocumentUuid 关联 新增 cashFlowUuid
        String voucherSource = VoucherSource.BUSINESS_PAYMENT_VOUCHER.getKey();

        VoucherType business_payment_voucher_type = model.getVoucherTypeEnum();
        if (business_payment_voucher_type == null) {
            throw new BusinessPaymentVoucherException("凭证类型不存在！");
        }
        String secondType = business_payment_voucher_type.getKey();
        String secondNo = model.getCreditSerialNumber();//付款流水号
        Account account = financialContract.getCapitalAccount();
        if (account == null) {
            throw new BusinessPaymentVoucherException("银行账户不存在");
        }
        String receivableAccountNo = account.getAccountNo();
        String paymentAccountNo = model.getPaymentAccountNo();
        String paymentName = model.getPaymentName();
        String paymentBank = model.getPaymentBank();

        Voucher voucher = new Voucher(financialContract.getFinancialContractUuid(), model.getVoucherAmount(), voucherSource,  model.getRequestNo(), secondType, secondNo,
                receivableAccountNo, paymentAccountNo, paymentName, paymentBank,
                SourceDocumentDetailCheckState.UNCHECKED, cashFlow.getCashFlowUuid(), cashFlow.getTransactionTime(), model.getRemark(), model.getWirteOffTotalAmount());

        voucherService.saveOrUpdate(voucher);
        return voucher.getUuid();
    }

    private List<CashFlow> getUnattachedCashFlow(BusinessPaymentVoucherCommandModel model,
                                                 FinancialContract financialContract) {
        if (voucherService.countVoucherBySecondNo(model.getBankTransactionNo()) != 0) {
            throw new BankTransactionNoExistException();
        }
        return cashFlowHandler.getUnattachedCashFlowListBy(model.getPaymentAccountNo(), model.getPaymentName(), model.getVoucherAmount());
    }

    private void createSourceDocumentDetails(BusinessPaymentVoucherCommandModel model, String financialContractUuid, String voucherUuid, String cashFlowUuid, FinancialContract financialContract, String SourceDocumentDetailsFilePath) {
        String firstNo = model.getRequestNo();
        String secondType = model.getVoucherTypeEnum().getKey();
        String secondNo = model.getBankTransactionNo();//打款流水号
        String receivableAccountNo = model.getReceivableAccountNo();
        String paymentAccountNo = model.getPaymentAccountNo();//付款银行账号
        String paymentName = model.getPaymentName();//付款机构名称
        String paymentBank = model.getPaymentBank();//付款银行名称
        String sourceDocumentUuid = sourceDocumentService.getSourceDocumentUuidByCashFlowUuid(cashFlowUuid, financialContractUuid);
        sourceDocumentService.updateSourceDocument(voucherUuid, secondNo, model.getVoucherAmount(), sourceDocumentUuid);
        List<BusinessPaymentVoucherDetail> detailModel = model.getDetailModel();
        List<String> detailsJsonString = new ArrayList<>();
        for (BusinessPaymentVoucherDetail voucherDetail : detailModel) {
            logger.info("传进来的凭证明细,voucherDetail" + voucherDetail.toString());
            String reapyScheduleNoMd5 = getReapyScheduleNoMd5(financialContract, voucherDetail.getRepayScheduleNo());
            SourceDocumentDetail sourceDocumentDetail = new SourceDocumentDetail(sourceDocumentUuid, firstNo, secondType, secondNo, receivableAccountNo, paymentAccountNo, paymentName, paymentBank, financialContractUuid, voucherDetail, voucherUuid, reapyScheduleNoMd5);
            logger.info("生成凭证明细, sourceDocumentDetailUUID:" + sourceDocumentDetail.getUuid());
            detailsJsonString.add(JsonUtils.toJSONString(sourceDocumentDetail));
        }
        CsvUtils.createCSVFile(detailsJsonString, SourceDocumentDetailsFilePath);
        //sourceDocumentDetailService.saveSourceDocumentDetails(details);
    }

    /**
     * 获取MD5加密值
     *
     * @return
     */
    private String getReapyScheduleNoMd5(FinancialContract financialContract, String repayScheduleNo) {
        if (financialContract == null) {
            return "";
        }
        if (StringUtils.isEmpty(financialContract.getContractNo()) || StringUtils.isEmpty(repayScheduleNo)) {
            return "";
        }
        return repaymentPlanService.getRepayScheduleNoMD5(financialContract.getContractNo(), repayScheduleNo, com.zufangbao.sun.utils.StringUtils.EMPTY);
    }

    private void createSourceDocumentDetails(CreateBusinessPaymentVoucherModel model, FinancialContract financialContract,
                                             String voucherUuid, String sourceDocumentUuid, CashFlow cashFlow) throws BusinessPaymentVoucherException {
        String firstNo = model.getRequestNo();
        VoucherType business_payment_voucher_type = model.getVoucherTypeEnum();
        if (business_payment_voucher_type == null) {
            throw new BusinessPaymentVoucherException("凭证类型不存在！");
        }
        String secondType = business_payment_voucher_type.getKey();
        String secondNo = model.getCreditSerialNumber();//打款流水号
        String financialContractUuid = financialContract.getFinancialContractUuid();
        String receivableAccountNo = financialContract.getCapitalAccount().getAccountNo();
        String paymentAccountNo = model.getPaymentAccountNo();//付款银行账号
        String paymentName = model.getPaymentName();//付款机构名称
        String paymentBank = model.getPaymentBank();//付款银行名称
        sourceDocumentService.updateSourceDocument(voucherUuid, secondNo, model.getVoucherAmount(), sourceDocumentUuid);

        List<SourceDocumentDetail> sourceDocumentDetails = new ArrayList<SourceDocumentDetail>();
        switch (business_payment_voucher_type) {
            case PAY:
            case MERCHANT_REFUND:
            case ADVANCE:
                List<TemplatesForPay> detailForPay = model.parseClass(TemplatesForPay.class);
                for (TemplatesForPay templates : detailForPay) {
                    String contractUniqueId = contractService.getContractUniqueIdByContractNo(templates.getContractNo());
                    Date recycleDate;
                    try {
                        recycleDate = StringUtils.isEmpty(templates.getRecycleDate()) ? cashFlow.getTransactionTime() : org.apache.commons.lang.time.DateUtils.parseDate(templates.getRecycleDate(), new String[]{"yyyyMMdd", "yyyy-MM-dd", "yyyy/MM/dd"});
                    } catch (Exception e) {
                        throw new BusinessPaymentVoucherException("设定还款日期不是合适的日期格式！");
                    }
                    if (null == contractUniqueId) {
                        throw new BusinessPaymentVoucherException("贷款合同不存在！　贷款合同编号为：" + templates.getContractNo());
                    }
                    SourceDocumentDetail sourceDocumentDetail = SourceDocumentDetailImporter
                            .createBusinessPaymentVoucherDetailsWithPayModel(sourceDocumentUuid, contractUniqueId,
                                    templates.getRepaymentNo(), templates.getAmount(), firstNo, secondType, secondNo, receivableAccountNo,
                                    financialContractUuid, paymentAccountNo, paymentName, paymentBank, voucherUuid, templates, recycleDate);
//					sourceDocumentDetailService.saveOrUpdate(sourceDocumentDetail);
                    sourceDocumentDetails.add(sourceDocumentDetail);
                    logger.info("生成凭证明细, sourceDocumentDetailUUID:" + sourceDocumentDetail.getUuid());
                }
                break;
            case GUARANTEE:
                List<TemplatesForGuarantee> detailForGuarantee = model.parseClass(TemplatesForGuarantee.class);
                for (TemplatesForGuarantee templates : detailForGuarantee) {
                    String uuid = repaymentPlanService.getContractUuidByRepaymentPlanNo(templates.getRepaymentNo());
                    String contractUniqueId = contractService.getContractUniqueIdByContractUuid(uuid);
                    if (null == contractUniqueId) {
                        throw new BusinessPaymentVoucherException("还款计划对应的贷款合同不存在！　还款编号为：" + templates.getRepaymentNo());
                    }
                    SourceDocumentDetail sourceDocumentDetail = SourceDocumentDetailImporter
                            .createBusinessPaymentVoucherDetails(sourceDocumentUuid, contractUniqueId,
                                    templates.getRepaymentNo(), templates.getAmount(), firstNo, secondType, secondNo, receivableAccountNo,
                                    financialContractUuid, paymentAccountNo, paymentName, paymentBank, voucherUuid);
//					sourceDocumentDetailService.saveOrUpdate(sourceDocumentDetail);
                    sourceDocumentDetails.add(sourceDocumentDetail);
                    logger.info("生成凭证明细, sourceDocumentDetailUUID:" + sourceDocumentDetail.getUuid());
                }
                break;
            case REPURCHASE:
                List<TemplatesForRepurchase> detailForRepurchase = model.parseClass(TemplatesForRepurchase.class);
                for (TemplatesForRepurchase template : detailForRepurchase) {
                    String contractUniqueId = contractService.getContractUniqueIdByContractNo(template.getContractNo());
                    if (null == contractUniqueId) {
                        throw new BusinessPaymentVoucherException("贷款合同不存在！　贷款合同编号为：" + template.getContractNo());
                    }
                    SourceDocumentDetail sourceDocumentDetail = SourceDocumentDetailImporter
                            .createBusinessPaymentVoucherDetails(sourceDocumentUuid, contractUniqueId,
                                    template.getRepurchaseUuid(), template.getAmount(), firstNo, secondType, secondNo, receivableAccountNo,
                                    financialContractUuid, paymentAccountNo, paymentName, paymentBank, voucherUuid);
					sourceDocumentDetail.setPrincipal(template.getRepurchasetPrincipal());
					sourceDocumentDetail.setInterest(template.getRepurchasetInterest());
					sourceDocumentDetail.setPenaltyFee(template.getRepurchasetPenaltyFee());
					sourceDocumentDetail.setOtherCharge(template.getRepurchasetOtherCharge());
//					sourceDocumentDetailService.saveOrUpdate(sourceDocumentDetail);
					sourceDocumentDetails.add(sourceDocumentDetail);
					logger.info("生成凭证明细, sourceDocumentDetailUUID:" + sourceDocumentDetail.getUuid());
				}
				break;
			default:
				throw new BusinessPaymentVoucherException("凭证类型不存在！");
		}
		if(CollectionUtils.isNotEmpty(sourceDocumentDetails))
			sourceDocumentDetailService.saveSourceDocumentDetails(sourceDocumentDetails);
	}

//	private String attachedSourceDocumentReturnUuid(Long financeCompanyId, String bankTransactionNo, List<CashFlow> cashFlowList) {
//		if(cashFlowList.size() != 1){
//			return null;
//		}
//		SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentBy(financeCompanyId, cashFlowList.get(0).getCashFlowUuid());
//		if(sourceDocument == null) {
//			logger.error("流水对应凭证不存在");
//			throw new ApiException(SYSTEM_BUSY, "流水对应凭证不存在");
//		}
//		sourceDocument.setOutlierSerialGlobalIdentity(bankTransactionNo);
//		sourceDocumentService.saveOrUpdate(sourceDocument);
//		return sourceDocument.getSourceDocumentUuid();
//	}

    private void saveLog(BusinessPaymentVoucherCommandModel model, String ip) {
        String requestNo = model.getRequestNo();
        int transactionType = model.getTransactionType();
        int voucherType = model.getVoucherType();
        BigDecimal voucherAmount = model.getVoucherAmount();
        String financialContractNo = model.getFinancialContractNo();
        String receivableAccountNo = model.getReceivableAccountNo();
        String paymentAccountNo = model.getPaymentAccountNo();
        String paymentName = model.getPaymentAccountNo();
        String paymentBank = model.getPaymentBank();
        String bankTransactionNo = model.getBankTransactionNo();
        BusinessPaymentVoucherLog log = new BusinessPaymentVoucherLog(requestNo, transactionType, voucherType, voucherAmount, financialContractNo, receivableAccountNo, paymentAccountNo, paymentName, paymentBank, bankTransactionNo, ip);
        logService.saveOrUpdate(log);
    }

    @Override
    public void undoBusinessPaymentVoucher(BusinessPaymentVoucherCommandModel model, String ip) {
        checkByRequestNo(model.getRequestNo());
        saveLog(model, ip);
        undoBusinessPaymentVoucher(model);
    }

    @Override
    public void invalidSourceDocument(Long voucherId) {
        Voucher voucher = voucherService.load(Voucher.class, voucherId);
        if (voucher == null) {
            return;
        }
        boolean checkFails = true;
        if(voucher.isActiveVoucher()==false){
        	   checkFails = voucher.isCheckFails();
        }
        if (checkFails) {
            String voucherUuid = voucher.getUuid();
            String bankTransactionNo = voucher.getSecondNo();
            String cashFlowUuid = voucher.getCashFlowUuid();
            voucherHandler.cancelSourceDocumentAndCashFlow(voucherUuid, bankTransactionNo, cashFlowUuid);
            cancelSourceDocumentDetails(voucherUuid);
        }
    }

    private void cancelSourceDocumentDetails(String voucherUuid) {
        voucherService.cancelVoucher(voucherUuid);
        sourceDocumentDetailService.cancelDetails(voucherUuid);
    }

    @Override
    public List<CashFlow> matchCashflow(Long voucherId) {
        try {
            Voucher voucher = voucherService.load(Voucher.class, voucherId);
            String paymentAccountNo = voucher.getPaymentAccountNo();
            String paymentName = voucher.getPaymentName();
            BigDecimal voucherAmount = voucher.getAmount();

            if (BigDecimal.ZERO.compareTo(voucherAmount) == 0) {
                return Collections.emptyList();
            }
            return cashFlowHandler.getUnattachedCashFlowListBy(paymentAccountNo, paymentName, voucherAmount);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public ContractActiveSourceDocumentMapper connectionCashFlow(Long voucherId, String cashFlowUuid) {
        ContractActiveSourceDocumentMapper mapper = null;
        Voucher voucher = voucherService.load(Voucher.class, voucherId);
        CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
        if (voucher == null || cashFlow == null) {
            return null;
        }

        List<SourceDocument> sourceDocuments = sourceDocumentService.getSourceDocumentListByCashFlowUuid(cashFlowUuid);
        String secondNo = voucher.getSecondNo();
        String contractUniqueId = "";
        String voucherUuid = voucher.getUuid();
        if (CollectionUtils.isNotEmpty(sourceDocuments)) {
            for (SourceDocument sourceDocument : sourceDocuments) {
                sourceDocumentService.updateSourceDocument(voucherUuid, secondNo, voucher.getAmount(), sourceDocument.getUuid());
                Contract contract = contractService.getContract(sourceDocument.getRelatedContractUuid());
                if (contract != null) {
                    contractUniqueId = contract.getUniqueId();
                }
                sourceDocumentDetailService.updateSourceDocumentUuid(voucherUuid, sourceDocument.getUuid(), contractUniqueId);
            }
        } else if (VoucherSource.ACTIVE_PAYMENT_VOUCHER.getKey().equals(voucher.getFirstType())) {
            FinancialContract financialContract = financialContractService.getCacheableFinancialContracBy(voucher.getFinancialContractUuid());
            List<SourceDocumentDetail> details = sourceDocumentDetailService.getSourceDocumentDetailList(voucherUuid,voucher.getFirstNo(),null);
            if (CollectionUtils.isNotEmpty(details)) {
                contractUniqueId = details.get(0).getContractUniqueId();
            }
            Contract contract = contractService.getContractByUniqueId(contractUniqueId);
            SourceDocument sourceDocument = activePaymentVoucherHandler.attachedSourceDocument(financialContract, contract, Arrays.asList(cashFlow), secondNo, voucher.getComment(), voucherUuid, voucher.getAmount());
            sourceDocumentDetailService.updateSourceDocumentUuid(voucherUuid, sourceDocument.getUuid(), contractUniqueId);
            if (cashFlow != null && !cashFlow.isIssuing()) {
                //非部分充值则发送消息
                mapper = new ContractActiveSourceDocumentMapper(sourceDocument.getRelatedContractUuid(), sourceDocument.getUuid(), cashFlowUuid);
            }
        }
        Date transactionTime = cashFlow.getTransactionTime();
        cashFlowService.connectVoucher(cashFlowUuid, secondNo);
        voucherService.connectCashFlow(voucherId, cashFlowUuid, transactionTime);
        return mapper;
    }

    @Override
    public List<Map<String, Object>> getAppAccountInfos(AppAccountModelForVoucherController appAccountModel) {
        if (StringUtils.isBlank(appAccountModel.getFinancialContractUuid())) {
            return Collections.emptyList();
        }
        FinancialContract financialContract = financialContractService.getFinancialContractBy(appAccountModel.getFinancialContractUuid());
//		List<AppAccount> appAccounts = appAccountService.getAppAccountsBy(financialContract.getApp().getId(), appAccountModel);
//		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
//		for (AppAccount appAccount : appAccounts){
//			List<CashFlow> cashFlows = cashFlowService.getUnattachedCashFlow(financialContract.getCapitalAccount().getAccountNo(), appAccount.getAccountNo());
//			if(CollectionUtils.isEmpty(cashFlows)) continue;
//			Map<String, Object> result = genAppAccountMap(appAccount);
//			resultList.add(result);
//		}
        if (financialContract == null) return Collections.emptyList();
        Account account = financialContract.getCapitalAccount();
        if (account == null) return Collections.emptyList();
        List<CashFlow> cashFlows = cashFlowService.getUnattachedCashFlow(account.getAccountNo(), appAccountModel.getAccountNo(), false);
        if (CollectionUtils.isEmpty(cashFlows)) {
            return Collections.emptyList();
        }
//		Map<String, List<CashFlow>> accountCashFlowMap = cashFlows.stream().collect(Collectors.groupingBy(CashFlow::getCounterAccountNo));
        return cashFlows.stream()
                .filter(distinctByKey(CashFlow::getCounterAccountNo))
                .map(this::genAppAccountMap)
                .limit(5)
                .collect(Collectors.toList());
    }


    @Override
    @Deprecated
    public List<VoucherCreateAccountInfoModel> getAppAccountInfoClasses(AppAccountModelForVoucherController appAccountModel) {
        if (StringUtils.isBlank(appAccountModel.getFinancialContractUuid())) {
            return Collections.emptyList();
        }
        FinancialContract financialContract = financialContractService.getFinancialContractBy(appAccountModel.getFinancialContractUuid());
        if (financialContract == null) return Collections.emptyList();
        Account account = financialContract.getCapitalAccount();
        if (account == null) return Collections.emptyList();
        return cashFlowService.getVoucherCreateAccountInfoModel(account.getAccountNo(), appAccountModel.getAccountNo());
    }

    private <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    private Map<String, Object> genAppAccountMap(CashFlow cashFlow) {

        Map<String, Object> accountInfoMap = new HashMap<String, Object>();
        //还款方账号
        accountInfoMap.put("paymentAccountNo", cashFlow.getCounterAccountNo());
        //还款方姓名
        accountInfoMap.put("paymentName", cashFlow.getCounterAccountName());
        //账号开户行
        accountInfoMap.put("paymentBank", cashFlow.getCounterBankName());

        return accountInfoMap;
    }

    @Override
    public void deleteJob(String voucherUuid, Principal principal, String ipAddress) {
        Voucher voucher = voucherService.get_voucher_by_uuid(voucherUuid);
        if (voucher == null || voucher.getStatus() != SourceDocumentDetailStatus.UNSUCCESS) {
            throw new GlobalRuntimeException("凭证状态错误，当前凭证不允许重新核销!");
        }

        List<Job> jobs = jobService.getJobListBy(null, voucher.getFirstNo(), null, null);
        if (CollectionUtils.isEmpty(jobs) || jobs.size() != 1) {
            throw new GlobalRuntimeException("job不存在或过多，当前凭证不允许重新核销!");
        }

        Job job = jobs.get(0);
        ExecutingStatus executingStatus = job.getExcutingStatus();
        if (executingStatus != ExecutingStatus.DONE && executingStatus != ExecutingStatus.ABANDON) {
            throw new GlobalRuntimeException("job状态错误，当前凭证不允许重新核销!");
        }

        jobService.delete(job);
        voucher.setCheckState(SourceDocumentDetailCheckState.UNCHECKED);
        voucherService.update(voucher);

        String recordContent = "商户付款凭证重新核销" + "【操作人】" + principal.getUsername() + "，【操作时间】" + DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + ",【IP】" + ipAddress;
        SystemOperateLog log = SystemOperateLog.createLog(principal.getId(), recordContent, ipAddress, LogFunctionType.REDO_CANCEL_AFTER_VERIFICATION, LogOperateType.DELETE, voucherUuid, voucherUuid);
        systemOperateLogService.saveOrUpdate(log);
    }

	@Override
	public boolean checkVoucher(Long voucherId) {
		Voucher voucher = voucherService.load(Voucher.class, voucherId);
		if(voucher.getStatus() == SourceDocumentDetailStatus.SUCCESS){
			return false;
		}
		List<SourceDocumentDetail> sourceDocumentDetails = sourceDocumentDetailService.getSourceDocumentDetailList(voucher.getUuid(), null, null);
		for(SourceDocumentDetail sourceDocumentDetail:sourceDocumentDetails){
			if(sourceDocumentDetail.getStatus() == SourceDocumentDetailStatus.SUCCESS){
				return false;
			}
		}
		return true;
	}
}
