package com.suidifu.microservice.handler.impl;

import static com.zufangbao.sun.ledgerbook.ChartOfAccount.SND_LIABILITIES_INDEPENDENT_INTER_ACCOUNT_REMITTANCE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.suidifu.microservice.SourceDocumentReconciliationApplication;
import com.suidifu.microservice.handler.BusinessPaymentVoucherTaskHandler;
import com.suidifu.microservice.service.SourceDocumentDetailService;
import com.suidifu.microservice.utils.OrikaMapper;
import com.suidifu.owlman.microservice.enumation.SourceDocumentDetailCheckState;
import com.suidifu.owlman.microservice.model.SourceDocumentDetail;
import com.suidifu.owlman.microservice.model.SourceDocumentDetailReconciliationParameters;
import com.zufangbao.gluon.exception.GlobalRuntimeException;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.yunxin.entity.api.VoucherPayer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/3/6 <br>
 * @time: 13:58 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SourceDocumentReconciliationApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@Slf4j
public class BusinessPaymentVoucherTaskHandlerImplTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Resource
    private BusinessPaymentVoucherTaskHandler businessPaymentVoucherTaskHandler;

    @Resource
    private LedgerBookService ledgerBookService;

    @Resource
    private SourceDocumentDetailService sourceDocumentDetailService;

    private List<SourceDocumentDetailReconciliationParameters> detailReconciliationParameters;
    private SourceDocumentDetailReconciliationParameters element1;
    private SourceDocumentDetailReconciliationParameters element2;

    private String financialContractNo;
    private String ledgerBookNo;
    private Date cashFlowTransactionTime;
    private Boolean isDetailFile;

    private String sourceDocumentUuid;
    private String financialContractUuid;
    private boolean isRepaymentOrder;
    private BigDecimal detailAmount;

    private SourceDocumentDetail sourceDocumentDetail;

    public BusinessPaymentVoucherTaskHandlerImplTest() {
    }

    @Before
    public void setUp() {
        cashFlowTransactionTime = new Date();
        isDetailFile = true;
        detailReconciliationParameters = new ArrayList<>();

        sourceDocumentDetail = new SourceDocumentDetail();
        sourceDocumentDetail.setAmount(new BigDecimal("1000"));
        sourceDocumentDetail.setInterest(new BigDecimal("0"));
        sourceDocumentDetail.setLateFee(new BigDecimal("0"));
        sourceDocumentDetail.setLateOtherCost(new BigDecimal("0"));
        sourceDocumentDetail.setLatePenalty(new BigDecimal("0"));
        sourceDocumentDetail.setMaintenanceCharge(new BigDecimal("0"));
        sourceDocumentDetail.setOtherCharge(new BigDecimal("0"));
        sourceDocumentDetail.setPayer(VoucherPayer.fromValue(1));
        sourceDocumentDetail.setPenaltyFee(new BigDecimal("0"));
        sourceDocumentDetail.setPrincipal(new BigDecimal("1000"));
        sourceDocumentDetail.setRepaymentPlanNo("ZC168444895007657984");
        sourceDocumentDetail.setServiceCharge(new BigDecimal("0"));
        sourceDocumentDetail.setContractUniqueId("test_0313");
        sourceDocumentDetail.setPaymentBank("宁波银行");
        sourceDocumentDetail.setPaymentAccountNo("1001133419006708190");
        sourceDocumentDetail.setPaymentName("上海拍拍贷金融信息服务有限公司");
        sourceDocumentDetail.setReceivableAccountNo("600000000001");
        sourceDocumentDetail.setCheckState(SourceDocumentDetailCheckState.CHECK_SUCCESS);
    }

    @After
    public void tearDown() {
        detailReconciliationParameters = null;
        sourceDocumentDetail = null;
    }

    @Test
    public void fetchVirtualAccountAndBusinessPaymentVoucherTransfer() throws JsonProcessingException {
        sourceDocumentUuid = "8d379149-2130-41ab-8451-7f8037233a38";
        financialContractUuid = "d2812bc5-5057-4a91-b3fd-9019506f0499";
        ledgerBookNo = "7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58";
        isRepaymentOrder = false;
        detailAmount = new BigDecimal("0.00");
        ledgerBookNo = "7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58";
        LedgerBook ledgerBook = ledgerBookService.getBookByBookNo(ledgerBookNo);

        VirtualAccount virtualAccount = businessPaymentVoucherTaskHandler.
                fetchVirtualAccountAndBusinessPaymentVoucherTransfer(sourceDocumentUuid,
                        detailAmount, ledgerBook, financialContractUuid, isRepaymentOrder);

        OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        log.info("\njson is:\n{}\n", OBJECT_MAPPER.writeValueAsString(virtualAccount));
        assertThat(virtualAccount, notNullValue());
    }

    @Test
    public void fetchVirtualAccountAndBusinessPaymentVoucherTransferNullFinancialContract() {
        sourceDocumentUuid = "8d379149-2130-41ab-8451-7f8037233a38";
        financialContractUuid = "d2812bc5-5057-4a91-b3fd-9019506f049";
        ledgerBookNo = "7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58";
        isRepaymentOrder = false;
        detailAmount = new BigDecimal("0.00");
        ledgerBookNo = "7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58";
        LedgerBook ledgerBook = ledgerBookService.getBookByBookNo(ledgerBookNo);

        VirtualAccount virtualAccount = businessPaymentVoucherTaskHandler.
                fetchVirtualAccountAndBusinessPaymentVoucherTransfer(sourceDocumentUuid,
                        detailAmount, ledgerBook, financialContractUuid, isRepaymentOrder);

        assertThat(virtualAccount, nullValue());
    }

    @Test
    public void fetchVirtualAccountAndBusinessPaymentVoucherTransferNullCompanyCustomer() {
        sourceDocumentUuid = "8d379149-2130-41ab-8451-7f8037233a38";
        financialContractUuid = "148057c4-1586-40f0-ae45-0ed066d946c0";
        ledgerBookNo = "7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58";
        isRepaymentOrder = false;
        detailAmount = new BigDecimal("0.00");
        ledgerBookNo = "7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58";
        LedgerBook ledgerBook = ledgerBookService.getBookByBookNo(ledgerBookNo);

        VirtualAccount virtualAccount = businessPaymentVoucherTaskHandler.
                fetchVirtualAccountAndBusinessPaymentVoucherTransfer(sourceDocumentUuid,
                        detailAmount, ledgerBook, financialContractUuid, isRepaymentOrder);

        assertThat(virtualAccount, nullValue());
    }

    @Test
    public void getCriticalMarkerV() {
        element1 = new SourceDocumentDetailReconciliationParameters();
        element1.setSourceDocumentDetailUuid("776747d4-6b6e-487c-ad9e-45881d70d9ec");
        element1.setContractUniqueId("6aee97b7-2681-40a0-9e8a-2ae5115e716f");
        element1.setFinancialContractUuid("2495a5ce-094e-4eb6-9fb9-95454b138427");
        detailReconciliationParameters.add(element1);

        element2 = new SourceDocumentDetailReconciliationParameters();
        element2.setSourceDocumentDetailUuid("42f976a6-f2a6-47e8-b12c-272d17b2cddd");
        element2.setContractUniqueId("7f40f0e3-1166-48a0-8347-70a670c0307b");
        element2.setFinancialContractUuid("2495a5ce-094e-4eb6-9fb9-95454b138427");
        detailReconciliationParameters.add(element2);

        Map<String, String> map = businessPaymentVoucherTaskHandler.
                getCriticalMarkerV(detailReconciliationParameters);

        assertThat(map, notNullValue());
        assertThat(map.get(element1.getSourceDocumentDetailUuid()),
                equalTo("7c5bf740-d0a6-48a4-a618-9845ff67f235"));
        assertThat(map.get(element2.getSourceDocumentDetailUuid()),
                equalTo("daf732c7-0225-4bac-b01d-b6b52d1274ec"));
    }

    @Test
    public void getCriticalMarkerVEmpty() {
        Map<String, String> map = businessPaymentVoucherTaskHandler.
                getCriticalMarkerV(detailReconciliationParameters);

        assertThat(map, notNullValue());
        assertThat(map, equalTo(Collections.emptyMap()));
    }

    @Test
    public void getCriticalMarkerVEmptyContract() {
        element1 = new SourceDocumentDetailReconciliationParameters();
        element1.setSourceDocumentDetailUuid("776747d4-6b6e-487c-ad9e-45881d70d9ec");
        element1.setContractUniqueId("6aee97b7-2681-40a0-9e8a-2ae5115e716");
        element1.setFinancialContractUuid("2495a5ce-094e-4eb6-9fb9-95454b138427");
        detailReconciliationParameters.add(element1);

        element2 = new SourceDocumentDetailReconciliationParameters();
        element2.setSourceDocumentDetailUuid("42f976a6-f2a6-47e8-b12c-272d17b2cddd");
        element2.setContractUniqueId("7f40f0e3-1166-48a0-8347-70a670c0307");
        element2.setFinancialContractUuid("2495a5ce-094e-4eb6-9fb9-95454b138427");
        detailReconciliationParameters.add(element2);

        Map<String, String> map = businessPaymentVoucherTaskHandler.
                getCriticalMarkerV(detailReconciliationParameters);

        assertThat(map, notNullValue());
        assertThat(map.get(element1.getSourceDocumentDetailUuid()),
                equalTo(element1.getFinancialContractUuid()));
        assertThat(map.get(element2.getSourceDocumentDetailUuid()),
                equalTo(element2.getFinancialContractUuid()));
    }

    @Test
    public void isDetailValid() {
        sourceDocumentDetail.setSourceDocumentUuid(sourceDocumentUuid);

//        boolean isDetailValid = businessPaymentVoucherTaskHandler.
//                isDetailValid(sourceDocumentDetail, financialContractNo,
//                        ledgerBookNo, cashFlowTransactionTime, isDetailFile);
//
//        assertThat(isDetailValid, is(true));
    }

    @Test
    public void isDetailValidNullDetail() {
        sourceDocumentDetail.setSourceDocumentUuid(sourceDocumentUuid);

//        boolean isDetailValid = businessPaymentVoucherTaskHandler.
//                isDetailValid(sourceDocumentDetail, financialContractNo,
//                        ledgerBookNo, cashFlowTransactionTime, isDetailFile);
//
//        assertThat(isDetailValid, is(false));
    }

    @Test
    public void isDetailValidCheckSuccess() {
        sourceDocumentDetail.setSourceDocumentUuid(sourceDocumentUuid);

//        boolean isDetailValid = businessPaymentVoucherTaskHandler.
//                isDetailValid(sourceDocumentDetail, financialContractNo,
//                        ledgerBookNo, cashFlowTransactionTime, isDetailFile);
//
//        assertThat(isDetailValid, is(true));
    }

    @Test
    public void isDetailValidCheckFails() {
        sourceDocumentDetail.setSourceDocumentUuid(sourceDocumentUuid);

//        boolean isDetailValid = businessPaymentVoucherTaskHandler.
//                isDetailValid(sourceDocumentDetail, financialContractNo,
//                        ledgerBookNo, cashFlowTransactionTime, isDetailFile);
//
//        assertThat(isDetailValid, is(false));
    }

    @Test
    public void copyBean() {
        com.suidifu.microservice.entity.SourceDocumentDetail detail = sourceDocumentDetailService.
                getSourceDocumentDetail(sourceDocumentDetail.getSourceDocumentUuid());
        if (detail == null) {
            detail = OrikaMapper.map(sourceDocumentDetail, com.suidifu.microservice.entity.SourceDocumentDetail.class);
        }

        log.info("detail uuid is:{}", detail.getUuid());
        log.info("sourceDocumentUuid is:{}", detail.getSourceDocumentUuid());
        log.info("amount is:{}", detail.getAmount());
        log.info("interest is:{}", detail.getInterest());
        log.info("lateFee is:{}", detail.getLateFee());
        log.info("lateOtherCost is:{}", detail.getLateOtherCost());
        log.info("latePenalty is:{}", detail.getLatePenalty());
        log.info("maintenanceCharge is:{}", detail.getMaintenanceCharge());
        log.info("otherCharge is:{}", detail.getOtherCharge());
        log.info("payer is:{}", detail.getPayer().ordinal());
        log.info("penaltyFee is:{}", detail.getPenaltyFee());
        log.info("principal is:{}", detail.getPrincipal());
        log.info("repaymentPlanNo is:{}", detail.getRepaymentPlanNo());
        log.info("serviceCharge is:{}", detail.getServiceCharge());
        log.info("uniqueId is:{}", detail.getContractUniqueId());
        log.info("transactionTime is:{}", detail.getActualPaymentTime());
        log.info("paymentBank is:{}", detail.getPaymentBank());
        log.info("paymentAccountNo is:{}", detail.getPaymentAccountNo());
        log.info("paymentName is:{}", detail.getPaymentName());
        log.info("receivableAccountNo is:{}", detail.getReceivableAccountNo());
        log.info("checkState is:{}", detail.getCheckState().getKey());
        log.info("detail.isUncheck is:{}", detail.isUncheck());
    }

    @Test
    public void isDetailValidCheckContract() throws GlobalRuntimeException {
        sourceDocumentDetail.setSourceDocumentUuid(sourceDocumentUuid);

//        try {
//            businessPaymentVoucherTaskHandler.
//                    isDetailValid(sourceDocumentDetail, financialContractNo,
//                            ledgerBookNo, cashFlowTransactionTime, isDetailFile);
//        } catch (GlobalRuntimeException e) {
//            assertThat(e.getCode(), equalTo(VOUCHER_ERROR_OF_CONTRACT_CODE));
//            assertThat(e.getMsg(), equalTo(VOUCHER_ERROR_OF_CONTRACT_MSG));
//        }
    }

    @Test
    public void isDetailValidCheckFinancialContract() throws GlobalRuntimeException {
        financialContractNo = "G31700";
        sourceDocumentDetail.setSourceDocumentUuid(sourceDocumentUuid);

//        try {
//            businessPaymentVoucherTaskHandler.
//                    isDetailValid(sourceDocumentDetail, financialContractNo,
//                            ledgerBookNo, cashFlowTransactionTime, isDetailFile);
//        } catch (GlobalRuntimeException e) {
//            assertThat(e.getCode(), equalTo(VOUCHER_ERROR_OF_FINANCIAL_CONTRACT_CODE));
//            assertThat(e.getMsg(), equalTo(VOUCHER_ERROR_OF_FINANCIAL_CONTRACT_MSG));
//        }
    }

    @Test
    public void isDetailValidCheckFinancialContractNotEqualContractNO() throws GlobalRuntimeException {
        financialContractNo = "G317";
        sourceDocumentDetail.setSourceDocumentUuid(sourceDocumentUuid);

//        try {
//            businessPaymentVoucherTaskHandler.
//                    isDetailValid(sourceDocumentDetail, financialContractNo,
//                            ledgerBookNo, cashFlowTransactionTime, isDetailFile);
//        } catch (GlobalRuntimeException e) {
//            assertThat(e.getCode(), equalTo(VOUCHER_ERROR_OF_FINANCIAL_CONTRACT_CODE));
//            assertThat(e.getMsg(), equalTo(VOUCHER_ERROR_OF_FINANCIAL_CONTRACT_MSG));
//        }
    }

    /**
     * 几个数据库都没有关联数据可以展现此错误，暂停测试
     *
     * @throws GlobalRuntimeException
     */
    @Ignore
    @Test
    public void isDetailValidCheckTransactionTime() throws GlobalRuntimeException {
        financialContractNo = "G31700";
        sourceDocumentDetail.setSourceDocumentUuid(sourceDocumentUuid);

//        try {
//            businessPaymentVoucherTaskHandler.
//                    isDetailValid(sourceDocumentDetail, financialContractNo,
//                            ledgerBookNo, cashFlowTransactionTime, isDetailFile);
//        } catch (GlobalRuntimeException e) {
//            assertThat(e.getCode(), equalTo(VOUCHER_TRANSACTION_TIME_CODE));
//            assertThat(e.getMsg(), containsString(VOUCHER_DETAIL_TRANSACTION_TIME_CHECK));
//        }
    }

    @Test
    public void isDetailValidCheckRepaymentPlan() throws GlobalRuntimeException {
        financialContractNo = "G31700";
        sourceDocumentDetail.setSourceDocumentUuid(sourceDocumentUuid);

//        try {
//            businessPaymentVoucherTaskHandler.
//                    isDetailValid(sourceDocumentDetail, financialContractNo,
//                            ledgerBookNo, cashFlowTransactionTime, isDetailFile);
//        } catch (GlobalRuntimeException e) {
//            assertThat(e.getCode(), equalTo(VOUCHER_ERROR_OF_REPAYMENT_PLAN_NO_CODE));
//            assertThat(e.getMsg(), equalTo(VOUCHER_ERROR_OF_REPAYMENT_PLAN_NO_MSG));
//        }
    }

    /**
     * 几个数据库都没有关联数据可以展现此错误，暂停测试
     *
     * @throws GlobalRuntimeException
     */
    @Ignore
    @Test
    public void isDetailValidCheckoutIfPayingAssetSet() throws GlobalRuntimeException {
        financialContractNo = "G31700";
        sourceDocumentDetail.setSourceDocumentUuid(sourceDocumentUuid);

//        try {
//            businessPaymentVoucherTaskHandler.isDetailValid(sourceDocumentDetail,
//                    financialContractNo,
//                    ledgerBookNo, cashFlowTransactionTime, isDetailFile);
//        } catch (GlobalRuntimeException e) {
//            assertThat(e.getCode(), equalTo(VOUCHER_ERROR_OF_REPAYMENT_PLAN_NO_CODE));
//            assertThat(e.getMsg(), equalTo(VOUCHER_ERROR_OF_REPAYMENT_PLAN_NO_MSG));
//        }
    }

    /**
     * 几个数据库都没有关联数据可以展现此错误，暂停测试
     *
     * @throws GlobalRuntimeException
     */
    @Ignore
    @Test
    public void isDetailValidCheckVoucherAmount() throws GlobalRuntimeException {
        financialContractNo = "G31700";
        sourceDocumentDetail.setSourceDocumentUuid(sourceDocumentUuid);

//        try {
//            businessPaymentVoucherTaskHandler.isDetailValid(sourceDocumentDetail,
//                    financialContractNo, ledgerBookNo, cashFlowTransactionTime, isDetailFile);
//        } catch (GlobalRuntimeException e) {
//            assertThat(e.getCode(), equalTo(VOUCHER_ERROR_OF_VOUCHER_TYPE_CODE));
//            assertThat(e.getMsg(), equalTo(VOUCHER_ERROR_OF_VOUCHER_TYPE_MSG));
//        }
    }

    /**
     * 几个数据库都没有关联数据可以展现此错误，暂停测试
     *
     * @throws GlobalRuntimeException
     */
    @Ignore
    @Test
    public void isDetailValidCheckVoucherPayer() throws GlobalRuntimeException {
        financialContractNo = "G31700";
        sourceDocumentDetail.setSourceDocumentUuid(sourceDocumentUuid);

//        try {
//            businessPaymentVoucherTaskHandler.isDetailValid(sourceDocumentDetail,
//                    financialContractNo, ledgerBookNo, cashFlowTransactionTime, isDetailFile);
//        } catch (GlobalRuntimeException e) {
//            assertThat(e.getCode(), equalTo(VOUCHER_ERROR_OF_PAYER_CODE));
//            assertThat(e.getMsg(), equalTo(VOUCHER_ERROR_OF_PAYER_MSG));
//        }
    }

    /**
     * 几个数据库都没有关联数据可以展现此错误，暂停测试
     */
    @Ignore
    @Test
    public void unfreezeCapitalAmountOfVoucher() {
        sourceDocumentUuid = "df937f9856b9436baf915b1f873129a6";
        financialContractNo = "G31700";
        ledgerBookNo = "7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58";
        LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);
        boolean result = businessPaymentVoucherTaskHandler.
                unfreezeCapitalAmountOfVoucher(sourceDocumentUuid, financialContractNo, book,
                        "", SND_LIABILITIES_INDEPENDENT_INTER_ACCOUNT_REMITTANCE);

        assertThat(result, is(true));
    }

    @Test
    public void unfreezeCapitalAmountOfVoucherNullFinancialContract() {
        sourceDocumentUuid = "df937f9856b9436baf915b1f873129a6";
        financialContractNo = "HC930";
        ledgerBookNo = "6aee97b7-2681-40a0-9e8a-2ae5115e716f";
        LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);

        boolean result = businessPaymentVoucherTaskHandler.
                unfreezeCapitalAmountOfVoucher(sourceDocumentUuid, financialContractNo, book,
                        "", SND_LIABILITIES_INDEPENDENT_INTER_ACCOUNT_REMITTANCE);

        assertThat(result, is(false));
    }

    @Test
    public void unfreezeCapitalAmountOfVoucherNullFinancialContractNullCompanyCustomer() {
        sourceDocumentUuid = "df937f9856b9436baf915b1f873129a6";
        financialContractNo = "HC9300";
        ledgerBookNo = "6aee97b7-2681-40a0-9e8a-2ae5115e716f";
        LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);

        boolean result = businessPaymentVoucherTaskHandler.
                unfreezeCapitalAmountOfVoucher(sourceDocumentUuid, financialContractNo, book,
                        "", SND_LIABILITIES_INDEPENDENT_INTER_ACCOUNT_REMITTANCE);

        assertThat(result, is(false));
    }

    @Test
    public void unfreezeCapitalAmountOfVoucherNullFinancialContractNoFrozenCapital() {
        financialContractNo = "G31700";
        ledgerBookNo = "7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58";
        LedgerBook book = ledgerBookService.getBookByBookNo(ledgerBookNo);

        boolean result = businessPaymentVoucherTaskHandler.
                unfreezeCapitalAmountOfVoucher(sourceDocumentUuid, financialContractNo, book,
                        "", SND_LIABILITIES_INDEPENDENT_INTER_ACCOUNT_REMITTANCE);

        assertThat(result, is(true));
    }
}