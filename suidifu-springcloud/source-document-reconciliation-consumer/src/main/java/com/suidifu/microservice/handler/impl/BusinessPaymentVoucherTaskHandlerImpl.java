package com.suidifu.microservice.handler.impl;

import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_CONTRACT_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_FINANCIAL_CONTRACT_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_GUARANTEE_AMOUNT_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_NO_DETAIL_AMOUNT_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_PAYER_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_RECEIVABLE_AMOUNT_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_REPAYMENT_PLAN_NO_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_REPAY_SCHEDULE_NO_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_REPURCHASE_AMOUNT_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_REPURCHASE_DOC_NOT_EXIST_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_ERROR_OF_VOUCHER_TYPE_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_EXIST_PAYMENTING_ASSETSET_CODE;
import static com.zufangbao.gluon.spec.global.GlobalCodeSpec.VoucherErrorCodeSpec.VOUCHER_TRANSACTION_TIME_CODE;
import static com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec.VoucherErrorMsgSpec.VOUCHER_DETAIL_TRANSACTION_TIME_CHECK;
import static com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec.VoucherErrorMsgSpec.VOUCHER_ERROR_OF_CONTRACT_MSG;
import static com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec.VoucherErrorMsgSpec.VOUCHER_ERROR_OF_FINANCIAL_CONTRACT_MSG;
import static com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec.VoucherErrorMsgSpec.VOUCHER_ERROR_OF_GUARANTEE_AMOUNT_MSG;
import static com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec.VoucherErrorMsgSpec.VOUCHER_ERROR_OF_NO_DETAIL_AMOUNT_MSG;
import static com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec.VoucherErrorMsgSpec.VOUCHER_ERROR_OF_PAYER_MSG;
import static com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec.VoucherErrorMsgSpec.VOUCHER_ERROR_OF_RECEIVABLE_AMOUNT_MSG;
import static com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec.VoucherErrorMsgSpec.VOUCHER_ERROR_OF_REPAYMENT_PLAN_NO_MSG;
import static com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec.VoucherErrorMsgSpec.VOUCHER_ERROR_OF_REPAY_SCHEDULE_NO_MSG;
import static com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec.VoucherErrorMsgSpec.VOUCHER_ERROR_OF_REPURCHASE_AMOUNT_MSG;
import static com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec.VoucherErrorMsgSpec.VOUCHER_ERROR_OF_REPURCHASE_DOC_NOT_EXIST_MSG;
import static com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec.VoucherErrorMsgSpec.VOUCHER_ERROR_OF_VOUCHER_TYPE_MSG;
import static com.zufangbao.gluon.spec.global.GlobalErrorMsgSpec.VoucherErrorMsgSpec.VOUCHER_EXIST_PAYMENTING_ASSETSET_MSG;
import static com.zufangbao.sun.yunxin.entity.model.api.ExtraChargeSpec.TOTAL_OVERDUE_FEE;

import com.demo2do.core.entity.Result;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.entity.SourceDocumentDetail;
import com.suidifu.microservice.handler.BusinessPaymentVoucherTaskHandler;
import com.suidifu.microservice.handler.LedgerBookVirtualAccountHandler;
import com.suidifu.microservice.handler.TemporaryDepositDocHandler;
import com.suidifu.microservice.handler.VirtualAccountHandler;
import com.suidifu.microservice.service.SourceDocumentDetailService;
import com.suidifu.microservice.service.SourceDocumentService;
import com.suidifu.microservice.utils.OrikaMapper;
import com.suidifu.owlman.microservice.enumation.SourceDocumentDetailCheckState;
import com.suidifu.owlman.microservice.model.SourceDocumentDetailReconciliationParameters;
import com.suidifu.owlman.microservice.model.SourceDocumentReconciliationParameters;
import com.zufangbao.gluon.exception.GlobalRuntimeException;
import com.zufangbao.sun.entity.account.DepositeAccountInfo;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.customer.CustomerType;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.refund.TemporaryDepositDoc;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.ledgerbook.AccountSide;
import com.zufangbao.sun.ledgerbook.AssetCategory;
import com.zufangbao.sun.ledgerbook.AssetConvertor;
import com.zufangbao.sun.ledgerbook.BankAccountCache;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.LedgerBookHandler;
import com.zufangbao.sun.ledgerbook.LedgerItemService;
import com.zufangbao.sun.ledgerbook.LedgerTradeParty;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.ledgerbookv2.handler.LedgerBookV2Handler;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.CustomerService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.service.RepurchaseService;
import com.zufangbao.sun.service.VirtualAccountService;
import com.zufangbao.sun.utils.AmountUtils;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.OrderPaymentStatus;
import com.zufangbao.sun.yunxin.entity.api.VoucherPayer;
import com.zufangbao.sun.yunxin.entity.api.VoucherSource;
import com.zufangbao.sun.yunxin.entity.api.VoucherType;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.refund.TemporaryDepositDocService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author louguanyang
 */
@Log4j2
@Component("businessPaymentVoucherTaskHandler")
public class BusinessPaymentVoucherTaskHandlerImpl implements BusinessPaymentVoucherTaskHandler {
    @Resource
    private BankAccountCache bankAccountCache;
    @Resource
    private LedgerBookStatHandler ledgerBookStatHandler;
    @Resource
    private LedgerBookVirtualAccountHandler ledgerBookVirtualAccountHandler;
    @Resource
    private LedgerBookHandler ledgerBookHandler;
    @Resource
    private VirtualAccountHandler virtualAccountHandler;
    @Resource
    private TemporaryDepositDocHandler temporaryDepositDocHandler;
    @Resource
    private LedgerBookV2Handler ledgerBookV2Handler;
    @Resource
    private CustomerService customerService;
    @Resource
    private ContractService contractService;
    @Resource
    private FinancialContractService financialContractService;
    @Resource
    private RepaymentPlanService repaymentPlanService;
    @Resource
    private SourceDocumentDetailService sourceDocumentDetailService;
    @Resource
    private LedgerBookService ledgerBookService;
    @Resource
    private SourceDocumentService sourceDocumentService;
    @Resource
    private VirtualAccountService virtualAccountService;
    @Resource
    private LedgerItemService ledgerItemService;
    @Resource
    private RepurchaseService repurchaseService;
    @Resource
    private TemporaryDepositDocService temporaryDepositDocService;
    @Resource
    private CashFlowService cashFlowService;

    @Override
    public VirtualAccount fetchVirtualAccountAndBusinessPaymentVoucherTransfer(String sourceDocumentUuid,
                                                                               BigDecimal writeOffAmount,
                                                                               LedgerBook ledgerBook,
                                                                               String financialContractUuid,
                                                                               boolean isRepaymentOrder) {
        FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
        if (financialContract == null) {
            log.info("financialContract is null");
            return null;
        }

        Customer companyCustomer = customerService.getCustomer(financialContract.getApp(), CustomerType.COMPANY);
        if (companyCustomer == null) {
            log.info("companyCustomer is null ");
            return null;
        }

        boolean existBookedSourceDocument = ledgerItemService.
                existBookedSourceDocumentOfIndependtAccountRemittance(ledgerBook.getLedgerBookNo(),
                        sourceDocumentUuid, writeOffAmount);
        log.info("existBookedSourceDocument is:{}", existBookedSourceDocument);

        if (!existBookedSourceDocument) {
            log.info("not exist BookedSourceDocument");
            log.info("\nsourceDocumentUuid is:{}\nwriteOffAmount is:{}\n" +
                            "financialContract is:{}\nledgerBook is:{}\n" +
                            "companyCustomer is:{}\nisRepaymentOrder is:{}\n",
                    sourceDocumentUuid, writeOffAmount,
                    financialContract.toString(), ledgerBook.toString(),
                    companyCustomer.toString(), isRepaymentOrder);

            return bookAndRefreshVirtualAccount(sourceDocumentUuid, writeOffAmount, financialContract, ledgerBook,
                    companyCustomer, isRepaymentOrder);
        }
        return virtualAccountService.getVirtualAccountByCustomerUuid(companyCustomer.getCustomerUuid());
    }

    @Override
    public Map<String, String> getCriticalMarkerV(List<SourceDocumentDetailReconciliationParameters> parametersList) {
        if (CollectionUtils.isEmpty(parametersList)) {
            log.info("\n#sychronizedMarker# sourceDocumentDetails is empty\n");
            return Collections.emptyMap();
        }

        List<String> contractUniqueIds = parametersList.stream().
                map(SourceDocumentDetailReconciliationParameters::getContractUniqueId).
                collect(Collectors.toList());

        List<Contract> contracts = contractService.getContractListByContractUniqueIds(contractUniqueIds);
        Map<String, String> contractUniqueIdAndUuid = new HashMap<>();
        if (CollectionUtils.isNotEmpty(contracts)) {
            contractUniqueIdAndUuid = contracts.stream().collect(
                    Collectors.toMap(Contract::getUniqueId, Contract::getUuid));
        } else {
            log.info("\n#sychronizedMarker# contracts is empty detailReconciliationParameters:{}\n",
                    JsonUtils.toJSONString(parametersList));
        }

        Map<String, String> sourceDocumentDetailUuidContractUuid = new HashMap<>();
        for (SourceDocumentDetailReconciliationParameters parameters : parametersList) {
            if (contractUniqueIdAndUuid.containsKey(parameters.getContractUniqueId())) {
                sourceDocumentDetailUuidContractUuid.put(parameters.getSourceDocumentDetailUuid(),
                        contractUniqueIdAndUuid.get(parameters.getContractUniqueId()));
            } else {
                sourceDocumentDetailUuidContractUuid.put(parameters.getSourceDocumentDetailUuid(),
                        parameters.getFinancialContractUuid());
            }
        }

        return sourceDocumentDetailUuidContractUuid;
    }

    @Override
    public void isDetailValid(SourceDocumentReconciliationParameters parameter,
                              Date cashFlowTransactionTime,
                              Boolean isDetailFile) {
        com.suidifu.owlman.microservice.model.SourceDocumentDetail sourceDocumentDetail = parameter.getSourceDocumentDetail();
        String financialContractNo = parameter.getFinancialContractNo();
        String ledgerBookNo = parameter.getLedgerBookNo();

        SourceDocumentDetail detail = sourceDocumentDetailService.getSourceDocumentDetail(sourceDocumentDetail.getUuid());
        if (detail == null) {
            detail = OrikaMapper.map(sourceDocumentDetail, SourceDocumentDetail.class);
            detail.setRepayScheduleNo(sourceDocumentDetail.getRepayScheduleNo());
            detail.setOuterRepaymentPlanNo(sourceDocumentDetail.getOuterRepaymentPlanNo());
        }

        if (detail.isUncheck()) {
            boolean isAllCheckPass = true;
            String errorMsg = "";
            try {
                log.info("开始检查明细内容的真实性-----> SourceDocumentDetailUuid:{}",
                        detail.getUuid());
                String keyOfVoucherType = detail.getSecondType();
                String contractUniqueId = detail.getContractUniqueId();
                Contract contract = checkContract(contractUniqueId);//校验贷款合同Contract
                FinancialContract financialContract = financialContractService.
                        getFinancialContractBy(contract.getFinancialContractUuid());
                //校验信托计划FinancialContract
                checkFinancialContract(financialContract, financialContractNo);
                String customerUuid = contract.getCustomerUuid();

                String keyOfVoucherSource = detail.getFirstType();
                if (VoucherSource.BUSINESS_PAYMENT_VOUCHER.getKey().equals(keyOfVoucherSource)) {
                    checkTransactionTime(detail, cashFlowTransactionTime,
                            financialContract, contract);
                }

                if (keyOfVoucherType.equals(VoucherType.PAY.getKey()) ||
                        keyOfVoucherType.equals(VoucherType.ADVANCE.getKey()) ||
                        keyOfVoucherType.equals(VoucherType.GUARANTEE.getKey()) ||
                        keyOfVoucherType.equals(VoucherType.MERCHANT_REFUND.getKey())) {
                    String repaymentPlanNo = detail.getRepaymentPlanNo();
                    String repayScheduleNo = detail.getRepayScheduleNo();
                    //校验还款计划AssetSet
                    AssetSet repaymentPlan = checkRepaymentPlan(contractUniqueId,
                            repaymentPlanNo, repayScheduleNo);

                    //商户还款计划编号比还款计划编号优先级高,只要存在正确的商户还款计划编号都接受
                    String singleLoanContractNo = repaymentPlan.getSingleLoanContractNo();
                    if (StringUtils.isNotEmpty(singleLoanContractNo) &&
                            !singleLoanContractNo.equals(repaymentPlanNo)) {
                        //repaymentPlanNo与AssetSet中的singleLoanContractNo不一致时
                        detail.setRepaymentPlanNo(singleLoanContractNo);
                    }

                    //校验明细中是否包含支付中的还款计划
                    checkoutIfPayingAssetSet(repaymentPlan);

                    String assetSetUuid = repaymentPlan.getAssetUuid();
                    checkVoucherAmount(detail, ledgerBookNo, assetSetUuid,
                            customerUuid, contract.getUuid());//校验明细金额
                } else if (keyOfVoucherType.equals(VoucherType.REPURCHASE.getKey())) {
                    RepurchaseDoc repurchaseDoc = checkRepurchaseDoc(contract.getId());
                    detail.setRepaymentPlanNo(repurchaseDoc.getRepurchaseDocUuid());
                    checkVoucherAmount(detail, ledgerBookNo, "", customerUuid,
                            contract.getUuid());//校验明细金额
                }

                checkVoucherPayer(detail.getPayer());//校验付款人类型
            } catch (GlobalRuntimeException e) {
                isAllCheckPass = false;
                errorMsg = e.getMsg();
            } catch (Exception e) {
                isAllCheckPass = false;
                errorMsg = "系统错误";
            }

            if (!isAllCheckPass) {
                detail.setCheckState(SourceDocumentDetailCheckState.CHECK_FAILS);
                detail.setComment(errorMsg);
                log.error("检查明细内容的真实性失败----->SourceDocumentDetailUUID:{}, 失败原因：{}",
                        sourceDocumentDetail.getUuid(), errorMsg);
                sourceDocumentDetailService.saveOrUpdate(detail);
            } else if (!isDetailFile) {
                detail.setCheckState(SourceDocumentDetailCheckState.CHECK_SUCCESS);
                log.info("检查明细内容的真实性成功----->SourceDocumentDetailUUID:{}",
                        sourceDocumentDetail.getUuid());
                sourceDocumentDetailService.saveOrUpdate(detail);
            } else {
                detail.setCheckState(SourceDocumentDetailCheckState.CHECK_SUCCESS);
                log.info("检查明细内容的真实性成功----->SourceDocumentDetailUUID:{}",
                        sourceDocumentDetail.getUuid());
            }
        }

        log.info("\ndetail checkState key is:{},ordinal is:{}\n",
                detail.getCheckState().getKey(),
                detail.getCheckState().ordinal());

        sourceDocumentDetail = OrikaMapper.map(detail,
                com.suidifu.owlman.microservice.model.SourceDocumentDetail.class);
        parameter.setSourceDocumentDetail(sourceDocumentDetail);

        log.info("\nSourceDocumentDetail checkState key is:{},ordinal is:{}\n",
                parameter.getSourceDocumentDetail().getCheckState().getKey(),
                parameter.getSourceDocumentDetail().getCheckState().ordinal());
    }

    @Override
    public void recoverEachFrozenCapitalAmount(String ledgerBookNo,
                                               FinancialContract financialContract,
                                               String companyCustomerUuid,
                                               String jvUuid,
                                               String sdUuid,
                                               BigDecimal bookingAmount,
                                               String tmpDepositDocUuid,
                                               String sndSecondNo) {//冻结资金 debit
        LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);
        String financialCompayUuid = financialContract.getCompany().getUuid();
        AssetCategory assetCategory = AssetConvertor.convertAssetCategory(tmpDepositDocUuid, sndSecondNo);
        LedgerTradeParty frozenCapitalTradeParty = new LedgerTradeParty(companyCustomerUuid,
                financialContract.getCompany().getUuid());
        String batchUuid = UUID.randomUUID().toString();
        Date bookInDate = new Date();
        ledgerBookHandler.book_single_asset_with_batch_uuid(book, frozenCapitalTradeParty, assetCategory, bookingAmount,
                ChartOfAccount.SND_FROZEN_CAPITAL_VOUCHER, com.zufangbao.sun.ledgerbook.AccountSide.DEBIT, jvUuid, "", sdUuid,
                batchUuid, bookInDate);

        //银行存款 credit
        DepositeAccountInfo accountInfo = bankAccountCache.extractFirstBankAccountFrom(financialContract);
        LedgerTradeParty debitTradePary = new LedgerTradeParty(financialCompayUuid, "");
        ledgerBookHandler.book_single_asset_with_batch_uuid(book, debitTradePary, assetCategory, bookingAmount,
                accountInfo.getDeposite_account_name(), com.zufangbao.sun.ledgerbook.AccountSide.CREDIT, jvUuid, "", sdUuid,
                batchUuid, bookInDate);
    }

    private void checkoutIfPayingAssetSet(AssetSet assetSet) {
        if (assetSet == null) {
            log.error("检查明细内容的真实性失败，还款计划不存在");
            throw checkFailsThrowException(
                    VOUCHER_ERROR_OF_REPAYMENT_PLAN_NO_CODE,
                    VOUCHER_ERROR_OF_REPAYMENT_PLAN_NO_MSG);
        }

        if (assetSet.getOrderPaymentStatus() == OrderPaymentStatus.ORDERPAYMENTING) {
            log.error("检查明细存在支付中的还款计划");
            throw checkFailsThrowException(
                    VOUCHER_EXIST_PAYMENTING_ASSETSET_CODE,
                    VOUCHER_EXIST_PAYMENTING_ASSETSET_MSG);
        }
    }

    private void checkTransactionTime(SourceDocumentDetail detail,
                                      Date cashFlowTransactionTime,
                                      FinancialContract financialContract,
                                      Contract contract) {
        if (financialContract.isRepaymentDayCheck() &&
                detail.getActualPaymentTime() != null) {
            boolean result = detail.checkTransactionTime(cashFlowTransactionTime,
                    financialContract.getRepaymentCheckDays(),
                    contract.getBeginDate());
            if (!result) {
                log.error("检查明细内容的设定还款时间失败");
                throw checkFailsThrowException(
                        VOUCHER_TRANSACTION_TIME_CODE,
                        VOUCHER_DETAIL_TRANSACTION_TIME_CHECK +
                                financialContract.getRepaymentCheckDays());
            }
        }
    }

    private Contract checkContract(String contractUniqueId) {
        Contract contract = contractService.getContractByUniqueId(contractUniqueId);
        if (contract == null) {
            log.error("检查明细内容的真实性失败，贷款合同不存在, 明细contractUniqueId:{}",
                    contractUniqueId);
            throw checkFailsThrowException(VOUCHER_ERROR_OF_CONTRACT_CODE,
                    VOUCHER_ERROR_OF_CONTRACT_MSG);
        }
        return contract;
    }

    private GlobalRuntimeException checkFailsThrowException(int code, String message) {
        Result result = new Result().initialize(String.valueOf(code), message);
        String errorMsg = JsonUtils.toJSONString(Collections.singletonList(result));
        log.error("error_msg:{}", errorMsg);
        return new GlobalRuntimeException(errorMsg);
    }

    private void checkFinancialContract(FinancialContract financialContract,
                                        String financialContractNo) {
        if (financialContract == null ||
                !StringUtils.equals(financialContractNo, financialContract.getContractNo())) {
            log.error("检查明细内容的真实性失败，贷款合同找不到对应的信托计划");
            throw checkFailsThrowException(
                    VOUCHER_ERROR_OF_FINANCIAL_CONTRACT_CODE,
                    VOUCHER_ERROR_OF_FINANCIAL_CONTRACT_MSG);
        }
    }

    private AssetSet checkRepaymentPlan(String contractUniqueId, String repaymentPlanNo, String repayScheduleNo) {
        AssetSet repaymentPlan = repaymentPlanService.getRepaymentPlanByConditions(repayScheduleNo, repaymentPlanNo);
        if (repaymentPlan == null) {
            if (StringUtils.isNotEmpty(repayScheduleNo)) {
                log.error("检查明细内容的真实性失败，还款计划不存在");
                throw checkFailsThrowException(
                        VOUCHER_ERROR_OF_REPAY_SCHEDULE_NO_CODE,
                        VOUCHER_ERROR_OF_REPAY_SCHEDULE_NO_MSG);
            } else {
                log.error("检查明细内容的真实性失败，还款计划不存在");
                throw checkFailsThrowException(
                        VOUCHER_ERROR_OF_REPAYMENT_PLAN_NO_CODE,
                        VOUCHER_ERROR_OF_REPAYMENT_PLAN_NO_MSG);
            }
        }
        String contractUuid = repaymentPlan.getContractUuid();
        Contract contract = contractService.getContract(contractUuid);
        if (contract == null) {
            log.error("检查明细内容的真实性失败，还款计划不存在");
            throw checkFailsThrowException(
                    VOUCHER_ERROR_OF_REPAYMENT_PLAN_NO_CODE,
                    VOUCHER_ERROR_OF_REPAYMENT_PLAN_NO_MSG);
        }
        if (!StringUtils.equals(contractUniqueId, contract.getUniqueId())) {
            log.error("检查明细内容的真实性失败，还款计划不存在");
            throw checkFailsThrowException(
                    VOUCHER_ERROR_OF_REPAYMENT_PLAN_NO_CODE,
                    VOUCHER_ERROR_OF_REPAYMENT_PLAN_NO_MSG);
        }
        return repaymentPlan;
    }

    private RepurchaseDoc checkRepurchaseDoc(Long contractId) {
        RepurchaseDoc repurchaseDoc = repurchaseService.getRepurchaseDocBy(contractId);
        if (repurchaseDoc == null) {
            log.error("检查明细内容的真实性失败，回购单不存在");
            throw checkFailsThrowException(
                    VOUCHER_ERROR_OF_REPURCHASE_DOC_NOT_EXIST_CODE,
                    VOUCHER_ERROR_OF_REPURCHASE_DOC_NOT_EXIST_MSG);
        }
        return repurchaseDoc;
    }

    /**
     * 校验还款人
     */
    private void checkVoucherPayer(VoucherPayer voucherPayer) {
        if (voucherPayer == null) {
            log.error("检查明细内容的真实性失败，还款人填写错误");
            throw checkFailsThrowException(
                    VOUCHER_ERROR_OF_PAYER_CODE,
                    VOUCHER_ERROR_OF_PAYER_MSG);
        }
    }

    /**
     * 检查明细金额
     */
    private void checkVoucherAmount(SourceDocumentDetail sourceDocumentDetail,
                                    String ledgerBookNo, String assetSetUuid,
                                    String customerUuid, String contractUuid) {
        VoucherType voucherType = VoucherType.fromKey(sourceDocumentDetail.getSecondType());
        if (voucherType == null) {
            log.error("检查明细内容的真实性失败，不支持的凭证类型");
            throw checkFailsThrowException(
                    VOUCHER_ERROR_OF_VOUCHER_TYPE_CODE,
                    VOUCHER_ERROR_OF_VOUCHER_TYPE_MSG);
        }

        switch (voucherType) {
            case PAY:
            case ADVANCE:
            case MERCHANT_REFUND:
                checkVoucherAmountOfTypePay(sourceDocumentDetail,
                        ledgerBookNo, assetSetUuid, customerUuid);
                break;
            case GUARANTEE:
                checkVoucherAmountOfGuarantee(sourceDocumentDetail,
                        ledgerBookNo, assetSetUuid);
                break;
            case REPURCHASE:
                checkVoucherAmountOfTypeRepurchase(sourceDocumentDetail,
                        ledgerBookNo, contractUuid);
                break;
            default:
                log.error("检查明细内容的真实性失败，不支持的凭证类型");
                throw checkFailsThrowException(
                        VOUCHER_ERROR_OF_VOUCHER_TYPE_CODE,
                        VOUCHER_ERROR_OF_VOUCHER_TYPE_MSG);
        }
    }

    private void checkVoucherAmountOfTypeRepurchase(SourceDocumentDetail sourceDocumentDetail,
                                                    String ledgerBookNo,
                                                    String contractUuid) {
        BigDecimal repurchaseAmount = ledgerBookStatHandler.get_repurchase_amount(ledgerBookNo, contractUuid);
        if (checkRepurchaseThreeAmount(sourceDocumentDetail.getAmount(),
                sourceDocumentDetail.repurchaseDetailAmount(), repurchaseAmount)) {
            return;
        }
        Map<String, BigDecimal> repurchaseSnapshot = ledgerBookStatHandler.repurchase_snapshot(ledgerBookNo, contractUuid);
        HashMap<String, BigDecimal> repurchaseDetailAmountMap = sourceDocumentDetail.getRepurchaseDocDetailAmountMap();
        List<Result> resultList = new ArrayList<>();
        for (String key : repurchaseDetailAmountMap.keySet()) {
            BigDecimal requestAmount = repurchaseDetailAmountMap.getOrDefault(key, BigDecimal.ZERO);
            if (AmountUtils.equals(BigDecimal.ZERO, requestAmount)) {
                continue;
            }
            BigDecimal amountInDB = repurchaseSnapshot.getOrDefault(key, BigDecimal.ZERO);
            if (requestAmount.compareTo(amountInDB) > 0) {
                String amountName = sourceDocumentDetail.getDetailAmountName(key);
                Integer errorCode = sourceDocumentDetail.getDetailErrorCode(key);
                String errorMsg = String.format("(%s) %s 应为 %s", amountName, requestAmount, amountInDB);
                log.error(String.format("检查回购明细金额失败, sourceDocumentDetail uuid %s, %s",
                        sourceDocumentDetail.getUuid(), errorMsg));
                Result result = new Result().initialize(errorCode + "", errorMsg);
                resultList.add(result);
            }
        }
        if (CollectionUtils.isNotEmpty(resultList)) {
            throw new GlobalRuntimeException(JsonUtils.toJSONString(resultList));
        }
    }

    private boolean checkRepurchaseThreeAmount(BigDecimal amount, BigDecimal detailAmount, BigDecimal amountInDb) {
        if (AmountUtils.isEmpty(amount)) {
            log.error("检查回购明细内容的真实性失败，明细总金额为零");
            throw checkFailsThrowException(
                    VOUCHER_ERROR_OF_NO_DETAIL_AMOUNT_CODE,
                    VOUCHER_ERROR_OF_NO_DETAIL_AMOUNT_MSG);
        }
        if (amount.compareTo(amountInDb) > 0) {
            log.error("检查回购明细金额失败，明细金额大于回购单金额");
            throw checkFailsThrowException(
                    VOUCHER_ERROR_OF_REPURCHASE_AMOUNT_CODE,
                    VOUCHER_ERROR_OF_REPURCHASE_AMOUNT_MSG);
        }
        if (AmountUtils.isEmpty(detailAmount)) {
            if (AmountUtils.equals(amount, amountInDb)) {
                log.info("检查回购明细金额成功,明细总金额等于回购单金额并且明细其他金额,跳过校验其他金额");
                return true;
            } else {
                log.error("检查回购明细内容的真实性失败，明细未传");
                throw checkFailsThrowException(
                        VOUCHER_ERROR_OF_NO_DETAIL_AMOUNT_CODE,
                        VOUCHER_ERROR_OF_NO_DETAIL_AMOUNT_MSG);
            }
        }
        if (AmountUtils.notEquals(amount, detailAmount)) {
            log.error("检查回购明细内容的真实性失败，明细总金额不等于其他金额总和");
            throw checkFailsThrowException(
                    VOUCHER_ERROR_OF_REPURCHASE_AMOUNT_CODE,
                    VOUCHER_ERROR_OF_REPURCHASE_AMOUNT_MSG);
        }
        return false;
    }

    private void checkVoucherAmountOfGuarantee(SourceDocumentDetail sourceDocumentDetail, String ledgerBookNo,
                                               String assetSetUuid) {
        BigDecimal amount = sourceDocumentDetail.getAmount();
        BigDecimal guaranteeAmount = ledgerBookStatHandler.get_gurantee_amount(ledgerBookNo, assetSetUuid);
        if (amount.compareTo(guaranteeAmount) > 0) {
            log.error("检查担保补足明细金额失败，明细金额大于还款计划担保金额");
            throw checkFailsThrowException(
                    VOUCHER_ERROR_OF_GUARANTEE_AMOUNT_CODE,
                    VOUCHER_ERROR_OF_GUARANTEE_AMOUNT_MSG);
        }
    }

    private void checkVoucherAmountOfTypePay(SourceDocumentDetail sourceDocumentDetail, String ledgerBookNo,
                                             String assetSetUuid, String customerUuid) {
        BigDecimal amount = sourceDocumentDetail.getAmount();
        BigDecimal unRecoveredAmount = ledgerBookStatHandler
                .unrecovered_asset_snapshot(ledgerBookNo, assetSetUuid, customerUuid, true);
        if (amount.compareTo(unRecoveredAmount) > 0) {
            log.error("检查明细内容的真实性失败，金额大于未还金额");
            throw checkFailsThrowException(
                    VOUCHER_ERROR_OF_RECEIVABLE_AMOUNT_CODE,
                    VOUCHER_ERROR_OF_RECEIVABLE_AMOUNT_MSG);
        }
        BigDecimal detailAmount = sourceDocumentDetail.detailAmount();
        if (AmountUtils.equals(BigDecimal.ZERO, detailAmount)) {
            if (AmountUtils.equals(amount, unRecoveredAmount)) {
                return;
            } else {
                log.error("检查明细内容的真实性失败，明细未传");
                throw checkFailsThrowException(
                        VOUCHER_ERROR_OF_NO_DETAIL_AMOUNT_CODE,
                        VOUCHER_ERROR_OF_NO_DETAIL_AMOUNT_MSG);
            }
        }
        LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);
        Map<String, BigDecimal> unRecoveredAssetSnapshot = ledgerBookStatHandler
                .get_unrecovered_asset_snapshot(book, assetSetUuid);
        HashMap<String, BigDecimal> detailAmountMap = sourceDocumentDetail.getDetailAmountMap();
        List<Result> resultList = new ArrayList<>();
        for (String key : detailAmountMap.keySet()) {
            if (StringUtils.equals(key, TOTAL_OVERDUE_FEE)) {
                continue;
            }
            BigDecimal requestAmount = detailAmountMap.getOrDefault(key, BigDecimal.ZERO);
            if (AmountUtils.equals(BigDecimal.ZERO, requestAmount)) {
                continue;
            }
            BigDecimal amountInDB = unRecoveredAssetSnapshot.getOrDefault(key, BigDecimal.ZERO);
            if (requestAmount.compareTo(amountInDB) > 0) {
                String amountName = sourceDocumentDetail.getDetailAmountName(key);
                Integer errorCode = sourceDocumentDetail.getDetailErrorCode(key);
                String errorMsg = String.format("(%s) %s 应为 %s", amountName, requestAmount, amountInDB);
                log.error(String.format("检查代偿明细金额失败, sourceDocumentDetail uuid %s, %s",
                        sourceDocumentDetail.getUuid(), errorMsg));
                Result result = new Result().initialize(String.valueOf(errorCode), errorMsg);
                resultList.add(result);
            }
        }
        if (CollectionUtils.isNotEmpty(resultList)) {
            throw new GlobalRuntimeException(JsonUtils.toJSONString(resultList));
        }
    }

    private VirtualAccount bookAndRefreshVirtualAccount(String sourceDocumentUuid,
                                                        BigDecimal writeOffAmount,
                                                        FinancialContract financialContract,
                                                        LedgerBook ledgerBook,
                                                        Customer companyCustomer,
                                                        boolean isRepaymentOrder) {
        long start = System.currentTimeMillis();
        VirtualAccount virtualAccount = virtualAccountService.getVirtualAccountByCustomerUuid(companyCustomer.getCustomerUuid());
        log.info("\ngetVirtualAccountByCustomerUuid used:{}ms\n",
                System.currentTimeMillis() - start);
        String oldVirtualAccountVersion = virtualAccount.getVersion();

        start = System.currentTimeMillis();
        BigDecimal balance = ledgerBookVirtualAccountHandler.getBalanceOfCustomer(ledgerBook.getLedgerBookNo(), companyCustomer.getCustomerUuid());
        log.info("\ngetBalanceOfCustomer used:{}ms\n",
                (System.currentTimeMillis() - start));

        if (balance.compareTo(writeOffAmount) < 0) {
            return null;
        }

        String financialCompanyUuid = financialContract.getCompany().getUuid();
        AssetCategory assetCategory = AssetConvertor.convertEmptyAssetCategory();
        LedgerTradeParty frozenCapitalTradeParty = new LedgerTradeParty(companyCustomer.getCustomerUuid(),
                financialCompanyUuid);
        LedgerTradeParty independentAccountTradeParty = new LedgerTradeParty(companyCustomer.getCustomerUuid(),
                financialCompanyUuid);
        log.info("#begin to BusinessPaymentVoucherTaskHandlerImpl " +
                "#book_compensatory_remittance_virtual_accountV2 [V4]");
        if (ledgerBookV2Handler.checkLegderBookVersion(ledgerBook)) {
            log.info("#begin to BusinessPaymentVoucherTaskHandlerImpl " +
                    "#book_compensatory_remittance_virtual_accountV2 [V4] [AccountTemplate]");

            ledgerBookV2Handler.book_compensatory_remittance_virtual_accountV2(ledgerBook,
                    frozenCapitalTradeParty, independentAccountTradeParty,
                    "", "", sourceDocumentUuid, writeOffAmount, assetCategory,
                    ChartOfAccount.SND_FROZEN_CAPITAL_VOUCHER,
                    ChartOfAccount.SND_LIABILITIES_INDEPENDENT_INTER_ACCOUNT_REMITTANCE);

            log.info("#end to BusinessPaymentVoucherTaskHandlerImpl " +
                    "#book_compensatory_remittance_virtual_accountV2 [V4] [AccountTemplate]");
        }

        if (ledgerBookV2Handler.checkLedgerBookVersionV1(ledgerBook)) {
            ledgerBookHandler.book_compensatory_remittance_virtual_accountV2(ledgerBook,
                    frozenCapitalTradeParty, independentAccountTradeParty,
                    "", "", sourceDocumentUuid, writeOffAmount, assetCategory,
                    ChartOfAccount.SND_FROZEN_CAPITAL_VOUCHER,
                    ChartOfAccount.SND_LIABILITIES_INDEPENDENT_INTER_ACCOUNT_REMITTANCE);
            log.info("#end to " +
                    "BusinessPaymentVoucherTaskHandlerImpl" +
                    "#book_compensatory_remittance_virtual_accountV2 [V4]");
        }
        if (!isRepaymentOrder) {
            SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentBy(sourceDocumentUuid);

            //再次计算账本余额，不能比滞留金额小
            BigDecimal customerBalance = ledgerBookVirtualAccountHandler
                    .getBalanceOfCustomer(ledgerBook.getLedgerBookNo(), companyCustomer.getCustomerUuid());
            if (sourceDocument.getBookingAmount().compareTo(writeOffAmount) > 0
                    && customerBalance.compareTo(sourceDocument.getBookingAmount().subtract(writeOffAmount)) >= 0) {
                //多余资金生成滞留单
                temporaryDepositDocHandler.businessPayCreateTemporaryDepositDoc(sourceDocumentUuid,
                        writeOffAmount, virtualAccount, ledgerBook);
            }
        }

        log.info("\nledgerBookNo is:{}\ncustomerUuid is:{}\n" +
                        "financialContractUuid is:{}\noldVirtualAccountVersion is:{}\n",
                financialContract.getLedgerBookNo(), companyCustomer.getCustomerUuid(),
                financialContract.getFinancialContractUuid(), oldVirtualAccountVersion);

        return virtualAccountHandler.refreshVirtualAccountBalance(
                financialContract.getLedgerBookNo(),
                companyCustomer.getCustomerUuid(),
                financialContract.getFinancialContractUuid(),
                oldVirtualAccountVersion);
    }

    @Override
    public boolean unfreezeCapitalAmountOfVoucher(String sourceDocumentUuid,
                                                  String financialContractNo,
                                                  LedgerBook book,
                                                  String tmpDepositDocUuid,
                                                  String toCreditAccount) {
        FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(financialContractNo);
        if (financialContract == null) {
            log.info("financialContract is null");
            return false;
        }

        Customer companyCustomer = customerService.getCustomer(financialContract.getApp(), CustomerType.COMPANY);
        if (companyCustomer == null) {
            log.info("companyCustomer is null");
            return false;
        }

        BigDecimal balanceOfFrozenCapital = ledgerBookVirtualAccountHandler
                .getBalanceOfFrozenCapital(
                        book.getLedgerBookNo(),
                        companyCustomer.getCustomerUuid(),
                        sourceDocumentUuid,
                        tmpDepositDocUuid);
        log.info("balanceOfFrozenCapital is:{}", balanceOfFrozenCapital);
        if (BigDecimal.ZERO.compareTo(balanceOfFrozenCapital) == 0) {//无冻结资金
            return true;
        }

        TemporaryDepositDoc temporaryDepositDoc = temporaryDepositDocService.getTemporaryDepositDocBy(tmpDepositDocUuid);
        log.info("temporaryDepositDoc is:{}", temporaryDepositDoc);

        String cashFlowIdentity = null;
        if (temporaryDepositDoc != null) {
            log.info("temporaryDepositDoc is:{}", temporaryDepositDoc.toString());
            CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(temporaryDepositDoc.getCashFlowUuid());
            cashFlowIdentity = (cashFlow == null ? null : cashFlow.getCashFlowIdentity());
        }
        log.info("cashFlowIdentity is:{}", cashFlowIdentity);

        AssetCategory assetCategory = AssetConvertor
                .convertTemporayDepositDocAssetCategory(tmpDepositDocUuid, cashFlowIdentity);
        log.info("assetCategory is:{}", assetCategory.toString());

        LedgerTradeParty creditTradeParty = new LedgerTradeParty(companyCustomer.getCustomerUuid(),
                financialContract.getCompany().getUuid());
        log.info("creditTradeParty is:{}", creditTradeParty.toString());

        String batchUuid = UUID.randomUUID().toString();
        Date bookInDate = new Date();

        if (ledgerBookV2Handler.checkLegderBookVersion(book)) {
            log.info("begin to #unfreeze_capital_amount_of_voucher sourceDocumentUuid:[{}] [script]",
                    sourceDocumentUuid);
            ledgerBookV2Handler.book_single_asset_with_batch_uuid(book, creditTradeParty, assetCategory, balanceOfFrozenCapital,
                    toCreditAccount, "", "", sourceDocumentUuid);
            log.info("end #unfreeze_capital_amount_of_voucher sourceDocumentUuid:[{}] [script]",
                    sourceDocumentUuid);
        }

        if (ledgerBookV2Handler.checkLedgerBookVersionV1(book)) {
            log.info("begin to #unfreeze_capital_amount_of_voucher sourceDocumentUuid:[{}] [pro]",
                    sourceDocumentUuid);
            ledgerBookHandler.book_single_asset_with_batch_uuid(book,
                    creditTradeParty, assetCategory, balanceOfFrozenCapital,
                    ChartOfAccount.SND_UNFROZEN_CAPITAL_VOUCHER,
                    AccountSide.DEBIT, "", "",
                    sourceDocumentUuid, batchUuid,
                    bookInDate);
            ledgerBookHandler.book_single_asset_with_batch_uuid(book,
                    creditTradeParty, assetCategory, balanceOfFrozenCapital,
                    toCreditAccount, AccountSide.CREDIT, "", "",
                    sourceDocumentUuid, batchUuid, bookInDate);
            log.info("end #unfreeze_capital_amount_of_voucher sourceDocumentUuid:[{}] [pro]",
                    sourceDocumentUuid);
        }

        //refresh
        String oldVirtualAccountVersion = virtualAccountService
                .getVirtualAccountByCustomerUuid(companyCustomer.getCustomerUuid()).getVersion();
        log.info("oldVirtualAccountVersion is:{}", oldVirtualAccountVersion);

        log.info("\nledgerBookNo is:{}\ncustomerUuid is:{}\n" +
                        "financialContractUuid is:{}\noldVirtualAccountVersion is:{}\n",
                financialContract.getLedgerBookNo(), companyCustomer.getCustomerUuid(),
                financialContract.getFinancialContractUuid(), oldVirtualAccountVersion);

        virtualAccountHandler.refreshVirtualAccountBalance(
                financialContract.getLedgerBookNo(),
                companyCustomer.getCustomerUuid(),
                financialContract.getFinancialContractUuid(),
                oldVirtualAccountVersion);

        return true;
    }
}