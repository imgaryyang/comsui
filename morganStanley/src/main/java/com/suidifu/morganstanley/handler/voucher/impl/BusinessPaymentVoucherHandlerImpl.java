package com.suidifu.morganstanley.handler.voucher.impl;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.morganstanley.handler.voucher.BusinessPaymentVoucherHandler;
import com.suidifu.morganstanley.model.request.voucher.BusinessPaymentVoucher;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.exception.voucher.BankTransactionNoExistException;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.csv.CsvUtils;
import com.zufangbao.sun.yunxin.api.BusinessPaymentVoucherLogService;
import com.zufangbao.sun.yunxin.entity.api.BusinessPaymentVoucherLog;
import com.zufangbao.sun.yunxin.entity.api.VoucherSource;
import com.zufangbao.sun.yunxin.entity.api.VoucherType;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.BusinessPaymentVoucherDetail;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentDetail;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.Voucher;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.CashFlowHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.VoucherHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentDetailService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.VoucherService;
import com.zufangbao.wellsfargo.yunxin.handler.vouchertask.impl.BusinessPaymentVoucherTaskHandlerImpl;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.zufangbao.gluon.spec.earth.v3.ApiResponseCode.*;

/**
 * 商户付款凭证接口
 *
 * @author louguanyang
 */
@Log4j2
@Component("businessPaymentVoucherHandler")
public class BusinessPaymentVoucherHandlerImpl extends BusinessPaymentVoucherTaskHandlerImpl implements BusinessPaymentVoucherHandler {
    //商户付款凭证类型，委托转付，商户担保，回购，差额划拨，代偿
    private static final VoucherType[] BUSINESS_PAYMENT_VOUCHER_TYPE = {
            VoucherType.PAY,
            VoucherType.GUARANTEE,
            VoucherType.REPURCHASE,
            VoucherType.MERCHANT_REFUND,
            VoucherType.ADVANCE};

    @Resource
    private BusinessPaymentVoucherLogService logService;

    @Resource
    private CashFlowHandler cashFlowHandler;

    @Resource
    private FinancialContractService financialContractService;
    @Resource
    private SourceDocumentService sourceDocumentService;

    @Resource
    private SourceDocumentDetailService sourceDocumentDetailService;

    @Resource
    private VoucherService voucherService;

    @Resource
    private CashFlowService cashFlowService;

    @Resource
    private VoucherHandler voucherHandler;

    @Resource
    private RepaymentPlanService repaymentPlanService;

    @Value("${sourceDocumentDetailsFilePath}")
    private String sourceDocumentDetailsFilePath = "";

    private VoucherType getVoucherTypeEnum(BusinessPaymentVoucher model) {
        VoucherType businessPaymentVoucherType = VoucherType.fromValue(model.getVoucherType(), BUSINESS_PAYMENT_VOUCHER_TYPE);
        if (businessPaymentVoucherType == null) {
            throw new ApiException(ApiMessage.NO_SUCH_VOUCHER_TYPE);
        }
        return businessPaymentVoucherType;
    }

    /**
     * 提交商户付款凭证接口
     *
     * @param model 商户付款凭证指令
     */
    public List<CashFlow>  submitBusinessPaymentVoucher(BusinessPaymentVoucher model) {
        FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(model.getFinancialContractNo());
        checkReceivableAccountNo(model.getReceivableAccountNo(), financialContract);
        List<CashFlow> unattachedCashFlows = getUnattachedCashFlows(model);
        if (CollectionUtils.isEmpty(unattachedCashFlows)) {
            log.error("submitBusinessPaymentVoucher fail, unattachedCashFlows is empty");
            throw new ApiException(ApiMessage.NO_SUCH_CASH_FLOW);
        }

        String cashFlowUuid = null;
        Date transactionTime = null;
        if (unattachedCashFlows.size() == 1) {
        	CashFlow cashFlow = unattachedCashFlows.get(0);
            cashFlowUuid = cashFlow.getUuid();
            transactionTime = cashFlow.getTransactionTime();
            connectVoucher(model, cashFlowUuid);
        }
        if (unattachedCashFlows.size()>1){
        	log.warn("unattachedCashFlows size > 1, need connect cash flow");
        }
        String detailsFilePath = sourceDocumentDetailsFilePath + "sourceDocumentDetails-"+ DateUtils.getFullDateTime(new Date()) + "-" + UUID.randomUUID().toString()+".csv";
        String voucherUuid = createVoucher(model, financialContract, cashFlowUuid, transactionTime, detailsFilePath);

        createSourceDocumentDetails(voucherUuid, cashFlowUuid, financialContract, model, detailsFilePath);

        return unattachedCashFlows;
    }

    /**
     * 查询是否有和凭证关联的现金流
     *
     * @param model
     * @param cashFlowUuid
     */
    private void connectVoucher(BusinessPaymentVoucher model, String cashFlowUuid) {
        cashFlowService.connectVoucher(cashFlowUuid, model.getBankTransactionNo());
        String bankTransactionNo = cashFlowService.searchBankTransactionNo(cashFlowUuid);
        if (!bankTransactionNo.equals(model.getBankTransactionNo())) {
            throw new ApiException(ApiMessage.NO_SUCH_CASH_FLOW);
        }
    }

    /**
     * 生成新的凭证
     *
     * @param model
     * @param financialContract
     * @param cashFlowUuid
     * @param transactionTime
     * @return
     */
    private String createVoucher(BusinessPaymentVoucher model,
                                 FinancialContract financialContract,
                                 String cashFlowUuid,
                                 Date transactionTime,
                                 String sourceDocumentDetailsFilePath) {
        String financialContractUuid = financialContract.getFinancialContractUuid();
        String voucherSource = VoucherSource.BUSINESS_PAYMENT_VOUCHER.getKey();
        BigDecimal voucherAmount = model.getVoucherAmount();
        String firstNo = model.getRequestNo();
        String secondType = getVoucherTypeEnum(model).getKey();
        String secondNo = model.getBankTransactionNo();
        String receivableAccountNo = model.getReceivableAccountNo();
        String paymentAccountNo = model.getPaymentAccountNo();
        String paymentName = model.getPaymentName();
        String paymentBank = model.getPaymentBank();
        List<BusinessPaymentVoucherDetail> details = model.getBusinessPaymentVoucherDetails();
        Voucher voucher = new Voucher(financialContractUuid, cashFlowUuid, transactionTime, voucherAmount, voucherSource, firstNo, secondType, secondNo,
                receivableAccountNo, paymentAccountNo, paymentName, paymentBank, model.getVoucherAmount());
        voucher.setSourceDocumentDetailsFilePath(sourceDocumentDetailsFilePath);
        voucherService.saveOrUpdate(voucher);
        return voucher.getUuid();
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
            log.error("exception message is:{}", ExceptionUtils.getStackTrace(e));
            throw new ApiException(NO_SUCH_RECEIVABLE_ACCOUNT_NO, e.getMessage());
        }
    }

    private List<CashFlow> getUnattachedCashFlows(BusinessPaymentVoucher model) {
        if (voucherService.countVoucherBySecondNo(model.getBankTransactionNo()) != 0) {
            log.error("BankTransactionNoExistException, bankTransactionNo:{}", model.getBankTransactionNo());
            throw new BankTransactionNoExistException();
        }
        return cashFlowHandler.getUnattachedCashFlowListBy(model.getPaymentAccountNo(), model.getPaymentName(), model.getVoucherAmount());
    }

    private void createSourceDocumentDetails(String voucherUuid, String cashFlowUuid,
                                             FinancialContract financialContract,
                                             BusinessPaymentVoucher model,
                                             String sourceDocumentDetailsFilePath) {
        String financialContractUuid = financialContract.getFinancialContractUuid();
        String secondNo = model.getBankTransactionNo();//打款流水号
        String firstNo = model.getRequestNo();
        String secondType = getVoucherTypeEnum(model).getKey();
        String receivableAccountNo = model.getReceivableAccountNo();
        String paymentAccountNo = model.getPaymentAccountNo();//付款银行账号
        String paymentName = model.getPaymentName();//付款机构名称
        String paymentBank = model.getPaymentBank();//付款银行名称
        String sourceDocumentUuid = sourceDocumentService.getSourceDocumentUuidByCashFlowUuid(cashFlowUuid, financialContractUuid);
        sourceDocumentService.updateSourceDocument(voucherUuid, secondNo, model.getVoucherAmount(), sourceDocumentUuid);
        List<BusinessPaymentVoucherDetail> detailModel = JsonUtils.parseArray(model.getDetail(), BusinessPaymentVoucherDetail.class);
        List<String> detailsJsonString = new ArrayList<>();
        if (detailModel == null) {
            throw new ApiException(ApiMessage.SYSTEM_ERROR);
        }
        for (BusinessPaymentVoucherDetail voucherDetail : detailModel) {
            log.info("传进来的凭证明细:{}", voucherDetail.toString());
           /* String repayScheduleNoMd5 = getRepayScheduleNoMd5(financialContract, voucherDetail.getRepayScheduleNo());
            SourceDocumentDetail sourceDocumentDetail = new SourceDocumentDetail(voucherDetail);
            BeanUtils.copyProperties(model, sourceDocumentDetail);
            sourceDocumentDetail.setFirstNo(model.getRequestNo());
            sourceDocumentDetail.setSecondType(getVoucherTypeEnum(model).getKey());
            sourceDocumentDetail.setSecondNo(model.getBankTransactionNo());
            sourceDocumentDetail.setSourceDocumentUuid(sourceDocumentUuid);
            sourceDocumentDetail.setFinancialContractUuid(financialContractUuid);
            sourceDocumentDetail.setVoucherUuid(voucherUuid);
            sourceDocumentDetail.setRepayScheduleNo(repayScheduleNoMd5);*/

            String reapyScheduleNoMd5 = getRepayScheduleNoMd5(financialContract, voucherDetail.getRepayScheduleNo());
            SourceDocumentDetail sourceDocumentDetail = new SourceDocumentDetail(sourceDocumentUuid, firstNo, secondType, secondNo, receivableAccountNo, paymentAccountNo, paymentName, paymentBank, financialContractUuid, voucherDetail, voucherUuid, reapyScheduleNoMd5);


            log.info("生成凭证明细UUID:{}", sourceDocumentDetail.getUuid());
            detailsJsonString.add(JsonUtils.toJsonString(sourceDocumentDetail));
        }
        CsvUtils.createCSVFile(detailsJsonString, sourceDocumentDetailsFilePath);
        //sourceDocumentDetailService.saveSourceDocumentDetails(details);
    }

    /**
     * 获取MD5加密值
     *
     * @param financialContract financial_contract
     * @param repayScheduleNo   repayScheduleNo
     * @return MD5加密值
     */
    private String getRepayScheduleNoMd5(FinancialContract financialContract, String repayScheduleNo) {
        if (financialContract == null) {
            return "";
        }
        if (StringUtils.isEmpty(financialContract.getContractNo()) ||
                StringUtils.isEmpty(repayScheduleNo)) {
            return "";
        }
        return repaymentPlanService.getRepayScheduleNoMD5(financialContract.getContractNo(), repayScheduleNo, com.zufangbao.sun.utils.StringUtils.EMPTY);
    }

    public void checkByRequestNo(String requestNo) {
        List<BusinessPaymentVoucherLog> result = logService.getLogByRequestNo(requestNo);
        if (CollectionUtils.isNotEmpty(result)) {
            throw new ApiException(REPEAT_REQUEST_NO);
        }
    }

    public void saveLog(BusinessPaymentVoucher model, String ip) {
        BusinessPaymentVoucherLog log = new BusinessPaymentVoucherLog();
        BeanUtils.copyProperties(model, log);
        log.setIp(ip);
        logService.saveOrUpdate(log);
    }

    /**
     * 撤销商户付款凭证接口
     *
     * @param model 商户付款凭证指令
     */
    public void undoBusinessPaymentVoucher(BusinessPaymentVoucher model) {
        FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(model.getFinancialContractNo());
        if (financialContract == null) {
            log.error("找不到信托计划，信托计划信托计划产品代码：{}", model.getFinancialContractNo());
            throw new ApiException(ApiMessage.FINANCIAL_CONTRACT_NOT_EXIST);
        }
        String financialContractUuid = financialContract.getUuid();
        String bankTransactionNo = model.getBankTransactionNo();
        Voucher voucher = voucherService.getValidVoucher(bankTransactionNo, financialContractUuid);
        if (voucher == null) {
            throw new ApiException(ApiMessage.NO_SUCH_VOUCHER);
        }
        String voucherUuid = voucher.getUuid();
        int successCount = sourceDocumentDetailService.countSuccessSourceDocumentDetailList(voucherUuid);
        if (successCount > 0) {
            throw new ApiException(ApiMessage.VOUCHER_CAN_NOT_CANCEL);
        }
        String cashFlowUuid = voucher.getCashFlowUuid();
        voucherHandler.cancelSourceDocumentAndCashFlow(voucherUuid, bankTransactionNo, cashFlowUuid);
        cancelSourceDocumentDetails(voucherUuid);
    }

    private void cancelSourceDocumentDetails(String voucherUuid) {
        voucherService.cancelVoucher(voucherUuid);
        sourceDocumentDetailService.cancelDetails(voucherUuid);
    }
}