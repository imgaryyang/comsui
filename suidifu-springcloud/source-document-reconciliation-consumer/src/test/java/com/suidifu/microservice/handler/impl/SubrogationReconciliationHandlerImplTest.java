package com.suidifu.microservice.handler.impl;

import static com.suidifu.owlman.microservice.enumation.SourceDocumentDetailCheckState.CHECK_FAILS;
import static com.suidifu.owlman.microservice.enumation.SourceDocumentDetailCheckState.CHECK_SUCCESS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.suidifu.microservice.SourceDocumentReconciliationApplication;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.service.SourceDocumentDetailService;
import com.suidifu.microservice.service.SourceDocumentService;
import com.suidifu.microservice.service.VoucherService;
import com.suidifu.owlman.microservice.enumation.SourceDocumentExcuteResult;
import com.suidifu.owlman.microservice.enumation.SourceDocumentExcuteStatus;
import com.suidifu.owlman.microservice.handler.SourceDocumentReconciliationHandler;
import com.suidifu.owlman.microservice.model.SourceDocumentDetailReconciliationParameters;
import com.suidifu.owlman.microservice.model.SourceDocumentReconciliationParameters;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.yunxin.entity.api.VoucherSource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/3/5 <br>
 * @time: 21:46 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SourceDocumentReconciliationApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@Slf4j
public class SubrogationReconciliationHandlerImplTest {
    @Autowired
    @Qualifier("subrogationReconciliation")
    private SourceDocumentReconciliationHandler sourceDocumentReconciliation;

    @Resource
    private SourceDocumentService sourceDocumentService;

    @Resource
    private SourceDocumentDetailService sourceDocumentDetailService;

    @Resource
    private FinancialContractService financialContractService;

    @Resource
    private VoucherService voucherService;

    private List<SourceDocumentDetailReconciliationParameters> detailReconciliationParameters;
    private SourceDocumentDetailReconciliationParameters element1;
    private SourceDocumentDetailReconciliationParameters element2;

    private List<SourceDocumentReconciliationParameters> reconciliationParameters;
    private SourceDocumentReconciliationParameters element3;
    private SourceDocumentReconciliationParameters element4;

    @Before
    public void setUp() {
        detailReconciliationParameters = new ArrayList<>();
        reconciliationParameters = new ArrayList<>();
    }

    @After
    public void tearDown() {
        detailReconciliationParameters = null;
        reconciliationParameters = null;
    }

    /**
     * 测试dstJobSourceDocumentReconciliation的方法名和参数类型是否改过
     */
    @Test
    public void testIsTargetMethodNameAndParameterTypeExist() {
        List<String> methodNames = new ArrayList<>();

        methodNames.add("criticalMarker");
        methodNames.add("validateSourceDocumentDetailList");
        methodNames.add("fetchVirtualAccountAndBusinessPaymentVoucherTransfer");
        methodNames.add("sourceDocumentRecoverDetails");
        methodNames.add("unfreezeCapital");

        int methodNameExistCounter = 0;

        Method[] methods = sourceDocumentReconciliation.getClass().getMethods();

        for (Method method : methods) {
            if (methodNames.contains(method.getName())) {
                methodNameExistCounter++;
            }
        }
        assertEquals(5, methodNameExistCounter);
    }

    //
//    bankTransactionNo=UUID.randomUUID().toString()&detail=[{"amount":1000,"interest":0,"lateFee":0,"lateOtherCost":0,"latePenalty":0,"maintenanceCharge":0,"otherCharge":0,"payer":1,"penaltyFee":0,"principal":1000,"repaymentPlanNo":"ZC168444895007657984","serviceCharge":0,"transactionTime":"","uniqueId":" test_0313"}]&financialContractNo=G31700&fn=300003&paymentAccountNo=1001133419006708190&paymentBank=宁波银行&paymentName=上海拍拍贷金融信息服务有限公司&receivableAccountNo=600000000001&requestNo=UUID.randomUUID().toString()&transactionType=0&voucherAmount=1000&voucherType=7

    @Test
//    @Sql("classpath:test/yunxin/businessPaymentVoucher/scan_sd_and_transfer_by_business_pay_voucher.sql")
    public void testAll() throws JsonProcessingException {
//        element1 = new SourceDocumentDetailReconciliationParameters();
//        element1.setSourceDocumentDetailUuid("776747d4-6b6e-487c-ad9e-45881d70d9ec");
//        element1.setContractUniqueId("6aee97b7-2681-40a0-9e8a-2ae5115e716f");
//        element1.setFinancialContractUuid("2495a5ce-094e-4eb6-9fb9-95454b138427");
//        detailReconciliationParameters.add(element1);
//
//        element2 = new SourceDocumentDetailReconciliationParameters();
//        element2.setSourceDocumentDetailUuid("42f976a6-f2a6-47e8-b12c-272d17b2cddd");
//        element2.setContractUniqueId("7f40f0e3-1166-48a0-8347-70a670c0307b");
//        element2.setFinancialContractUuid("2495a5ce-094e-4eb6-9fb9-95454b138427");
//        detailReconciliationParameters.add(element2);
//
//        //stage 1
//        Map<String, String> map = sourceDocumentReconciliation.criticalMarker(detailReconciliationParameters);
//        log.info("map is:{}", map.toString());
//        assertThat(map, notNullValue());
//        assertThat(map.get(element1.getSourceDocumentDetailUuid()),
//                equalTo("7c5bf740-d0a6-48a4-a618-9845ff67f235"));
//        assertThat(map.get(element2.getSourceDocumentDetailUuid()),
//                equalTo("daf732c7-0225-4bac-b01d-b6b52d1274ec"));
//        initTestData();

//sourceDocumentUuid='e80289b9-2c32-4df0-b0b1-98c0402e54c5'
//detailUuid='932f80a7-33b2-4b85-97e2-7295e28ede85'
//financialContractNo='G31700'
//ledgerBookNo='4a31e987-97aa-42d8-ae10-9c0360110bd1'

        element3 = new SourceDocumentReconciliationParameters();
        element3.setSourceDocumentUuid("e80289b9-2c32-4df0-b0b1-98c0402e54c5");
        element3.setSourceDocumentDetailUuid("932f80a7-33b2-4b85-97e2-7295e28ede85");
        element3.setFinancialContractNo("G31700");
        element3.setLedgerBookNo("4a31e987-97aa-42d8-ae10-9c0360110bd1");
        element3.setSecondType("enum.voucher-type.pay");
        element3.setDetaislFile(true);
        reconciliationParameters.add(element3);
//        //stage 2
//        boolean result = sourceDocumentReconciliation.validateSourceDocumentDetailList(reconciliationParameters);
//        log.info("result is:{}", result);
//        assertThat(result, is(true));
//
//        //stage 3
//        result = sourceDocumentReconciliation.fetchVirtualAccountAndBusinessPaymentVoucherTransfer(
//                reconciliationParameters);
//        log.info("result is:{}", result);
//        assertThat(result, is(true));
//
//        //stage 4
//        result = sourceDocumentReconciliation.sourceDocumentRecoverDetails(reconciliationParameters);
//        log.info("result is:{}", result);
//        assertThat(result, is(true));
//
//        //stage 5
//        result = sourceDocumentReconciliation.unfreezeCapital(reconciliationParameters);
//        log.info("result is:{}", result);
//        assertThat(result, is(true));

        //开始第2步
        boolean result = sourceDocumentReconciliation.validateSourceDocumentDetailList(reconciliationParameters);
        assertThat(result, is(true));
        log.info("\nstage 2 check result is:{}\n", result);
        if (result) {
            log.info(reconciliationParameters.get(0).toString());
            log.info("\nsourceDocumentId is:{},detailUuid is:{},financialContractNo is:{},ledgerBookNo is:{}," +
                            "secondType is:{}\n",
                    reconciliationParameters.get(0).getSourceDocumentUuid(),
                    reconciliationParameters.get(0).getSourceDocumentDetailUuid(),
                    reconciliationParameters.get(0).getFinancialContractNo(),
                    reconciliationParameters.get(0).getLedgerBookNo(),
                    reconciliationParameters.get(0).getSecondType());
        }

        String voucherUuid = "fc2ea351-75a1-476f-91b6-dc5f12761d6c";
        if (!result) {
            voucherService.updateCheckStateVoucherState(voucherUuid, CHECK_FAILS, null);
        }
        //更新凭证校验状态
        voucherService.updateCheckStateVoucherState(voucherUuid, CHECK_SUCCESS, null);

        //开始第3步
        List<SourceDocumentReconciliationParameters> thirdDataList = new ArrayList<>();
        SourceDocumentReconciliationParameters trdData = new SourceDocumentReconciliationParameters(
                element3.getSourceDocumentUuid(), "",
                element3.getFinancialContractNo(), element3.getLedgerBookNo());
        thirdDataList.add(trdData);
        result = sourceDocumentReconciliation.fetchVirtualAccountAndBusinessPaymentVoucherTransfer(thirdDataList);
        assertThat(result, is(true));
        log.info("\nstage 3 check result is:{}\n", result);
        if (result) {
            log.info(reconciliationParameters.get(0).toString());
            log.info("\nsourceDocumentId is:{},detailUuid is:{},financialContractNo is:{},ledgerBookNo is:{}," +
                            "secondType is:{}\n",
                    reconciliationParameters.get(0).getSourceDocumentUuid(),
                    reconciliationParameters.get(0).getSourceDocumentDetailUuid(),
                    reconciliationParameters.get(0).getFinancialContractNo(),
                    reconciliationParameters.get(0).getLedgerBookNo(),
                    reconciliationParameters.get(0).getSecondType());
        }

        //开始第4步
        result = sourceDocumentReconciliation.sourceDocumentRecoverDetails(reconciliationParameters);
        assertThat(result, is(true));
        log.info("\nstage 4 check result is:{}\n", result);
        if (result) {
            log.info(reconciliationParameters.get(0).toString());
            log.info("\nsourceDocumentId is:{},detailUuid is:{},financialContractNo is:{},ledgerBookNo is:{}," +
                            "secondType is:{}\n",
                    reconciliationParameters.get(0).getSourceDocumentUuid(),
                    reconciliationParameters.get(0).getSourceDocumentDetailUuid(),
                    reconciliationParameters.get(0).getFinancialContractNo(),
                    reconciliationParameters.get(0).getLedgerBookNo(),
                    reconciliationParameters.get(0).getSecondType());
        }

        //开始第5步
        List<SourceDocumentReconciliationParameters> fifthDataList = new ArrayList<>();
        fifthDataList.add(new SourceDocumentReconciliationParameters(element3.getSourceDocumentUuid(),
                "", element3.getFinancialContractNo(), element3.getLedgerBookNo()));
        result = sourceDocumentReconciliation.unfreezeCapital(fifthDataList);
        assertThat(result, is(true));
        log.info("\nstage 5 check result is:{}\n", result);
        if (result) {
            log.info(reconciliationParameters.get(0).toString());
            log.info("\nsourceDocumentId is:{},detailUuid is:{},financialContractNo is:{},ledgerBookNo is:{}," +
                            "secondType is:{}\n",
                    reconciliationParameters.get(0).getSourceDocumentUuid(),
                    reconciliationParameters.get(0).getSourceDocumentDetailUuid(),
                    reconciliationParameters.get(0).getFinancialContractNo(),
                    reconciliationParameters.get(0).getLedgerBookNo(),
                    reconciliationParameters.get(0).getSecondType());
        }
    }

    /**
     * 准备数据
     */
    private void initTestData() throws JsonProcessingException {
        List<SourceDocument> sourceDocumentList = sourceDocumentService.
                getDepositSourceDocumentListConnectedBy(SourceDocumentExcuteResult.UNSUCCESS,
                        SourceDocumentExcuteStatus.PREPARE);

        for (SourceDocument sourceDocument : sourceDocumentList) {
            log.info("sourceDocumentId is:{}", sourceDocument.getId());

            String sourceDocumentUuid = sourceDocument.getSourceDocumentUuid();
            String outlierSerialGlobalIdentity = sourceDocument.getOutlierSerialGlobalIdentity();
            boolean isBusinessPaymentVoucher = sourceDocumentDetailService.exist(sourceDocumentUuid,
                    VoucherSource.BUSINESS_PAYMENT_VOUCHER.getKey(), outlierSerialGlobalIdentity);

            //过滤未关联凭证的sd
            if (!isBusinessPaymentVoucher) {
                continue;
            }

            String financialContractUuid = sourceDocument.getFinancialContractUuid();
            FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
            if (financialContract == null) {
                continue;
            }

            String financialContractNo = financialContract.getContractNo();
            String ledgerBookNo = financialContract.getLedgerBookNo();

            List<String> detailUUIDs = sourceDocumentDetailService.getDetailUuidsBySourceDocumentUuid(sourceDocumentUuid, outlierSerialGlobalIdentity);
            log.info("detailUUIDs size:{}", detailUUIDs.size());

            reconciliationParameters = detailUUIDs.stream().map(detailUuid ->
                    new SourceDocumentReconciliationParameters(sourceDocumentUuid,
                            detailUuid, financialContractNo, ledgerBookNo)).collect(Collectors.toList());

            //开始第2步
            boolean result = sourceDocumentReconciliation.validateSourceDocumentDetailList(reconciliationParameters);
            log.info("\nstage 2 check result is:{}\n", result);
            if (result) {
                log.info(reconciliationParameters.get(0).toString());
                log.info("\nsourceDocumentId is:{},detailUuid is:{},financialContractNo is:{},ledgerBookNo is:{}," +
                                "secondType is:{}\n",
                        reconciliationParameters.get(0).getSourceDocumentUuid(),
                        reconciliationParameters.get(0).getSourceDocumentDetailUuid(),
                        reconciliationParameters.get(0).getFinancialContractNo(),
                        reconciliationParameters.get(0).getLedgerBookNo(),
                        reconciliationParameters.get(0).getSecondType());
            }

            if (!result) {
                voucherService.updateCheckStateVoucherState(sourceDocument.getVoucherUuid(),
                        CHECK_FAILS, null);
                continue;
            }
            //更新凭证校验状态
            voucherService.updateCheckStateVoucherState(sourceDocument.getVoucherUuid(),
                    CHECK_SUCCESS, null);

            //开始第3步
            List<SourceDocumentReconciliationParameters> thirdDataList = new ArrayList<>();
            SourceDocumentReconciliationParameters trdData = new SourceDocumentReconciliationParameters(sourceDocumentUuid, "",
                    financialContractNo, ledgerBookNo);
            thirdDataList.add(trdData);
            result = sourceDocumentReconciliation.fetchVirtualAccountAndBusinessPaymentVoucherTransfer(thirdDataList);
            log.info("\nstage 3 check result is:{}\n", result);
            if (result) {
                log.info(reconciliationParameters.get(0).toString());
                log.info("\nsourceDocumentId is:{},detailUuid is:{},financialContractNo is:{},ledgerBookNo is:{}," +
                                "secondType is:{}\n",
                        reconciliationParameters.get(0).getSourceDocumentUuid(),
                        reconciliationParameters.get(0).getSourceDocumentDetailUuid(),
                        reconciliationParameters.get(0).getFinancialContractNo(),
                        reconciliationParameters.get(0).getLedgerBookNo(),
                        reconciliationParameters.get(0).getSecondType());
            }

            //开始第4步
            result = sourceDocumentReconciliation.sourceDocumentRecoverDetails(reconciliationParameters);
            log.info("\nstage 4 check result is:{}\n", result);
            if (result) {
                log.info(reconciliationParameters.get(0).toString());
                log.info("\nsourceDocumentId is:{},detailUuid is:{},financialContractNo is:{},ledgerBookNo is:{}," +
                                "secondType is:{}\n",
                        reconciliationParameters.get(0).getSourceDocumentUuid(),
                        reconciliationParameters.get(0).getSourceDocumentDetailUuid(),
                        reconciliationParameters.get(0).getFinancialContractNo(),
                        reconciliationParameters.get(0).getLedgerBookNo(),
                        reconciliationParameters.get(0).getSecondType());
            }

            //开始第5步
            List<SourceDocumentReconciliationParameters> fifthDataList = new ArrayList<>();
            fifthDataList.add(new SourceDocumentReconciliationParameters(sourceDocument.getSourceDocumentUuid(),
                    "", financialContract.getContractNo(),
                    financialContract.getLedgerBookNo()));
            result = sourceDocumentReconciliation.unfreezeCapital(fifthDataList);
            log.info("\nstage 5 check result is:{}\n", result);
            if (result) {
                log.info(reconciliationParameters.get(0).toString());
                log.info("\nsourceDocumentId is:{},detailUuid is:{},financialContractNo is:{},ledgerBookNo is:{}," +
                                "secondType is:{}\n",
                        reconciliationParameters.get(0).getSourceDocumentUuid(),
                        reconciliationParameters.get(0).getSourceDocumentDetailUuid(),
                        reconciliationParameters.get(0).getFinancialContractNo(),
                        reconciliationParameters.get(0).getLedgerBookNo(),
                        reconciliationParameters.get(0).getSecondType());
            }

//            BigDecimal totalIssuedAmount = sourceDocumentDetailService.
//                    getTotalAmountOfSourceDocumentDetail(sourceDocument.getSourceDocumentUuid());
//            sourceDocumentService.update_after_inter_account_transfer(sourceDocument.getSourceDocumentUuid(),
//                    totalIssuedAmount);
//            voucherHandler.refresh_active_voucher(sourceDocument.getVoucherUuid());
        }
    }

    @Test
    public void criticalMarker() {
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

        Map<String, String> map = sourceDocumentReconciliation.criticalMarker(detailReconciliationParameters);

        assertThat(map, notNullValue());
        assertThat(map.get(element1.getSourceDocumentDetailUuid()),
                equalTo("7c5bf740-d0a6-48a4-a618-9845ff67f235"));
        assertThat(map.get(element2.getSourceDocumentDetailUuid()),
                equalTo("daf732c7-0225-4bac-b01d-b6b52d1274ec"));
    }

    @Test
    public void validateSourceDocumentDetailList() {
        element3 = new SourceDocumentReconciliationParameters();
        element3.setSourceDocumentUuid("47236aca-90ac-4d96-801e-f0c4c551309a");
        element3.setSourceDocumentDetailUuid("806cea0e-d2b9-4f87-bdd8-11c6ac0bcae4");
        element3.setFinancialContractNo("G31700");
        element3.setLedgerBookNo("6aee97b7-2681-40a0-9e8a-2ae5115e716f");
        element3.setSecondType("enum.voucher-type.third_party_deduction_voucher");
        element3.setDetaislFile(true);
        reconciliationParameters.add(element3);

        element4 = new SourceDocumentReconciliationParameters();
        element4.setSourceDocumentUuid("47236aca-90ac-4d96-801e-f0c4c551309a");
        element4.setSourceDocumentDetailUuid("1c534f45-4f35-46af-847d-01d7affd4247");
        element4.setFinancialContractNo("G31700");
        element4.setLedgerBookNo("6aee97b7-2681-40a0-9e8a-2ae5115e716f");
        element4.setSecondType("enum.voucher-type.third_party_deduction_voucher");
        element4.setDetaislFile(true);
        reconciliationParameters.add(element4);

        boolean result = sourceDocumentReconciliation.
                validateSourceDocumentDetailList(reconciliationParameters);

        assertThat(result, is(true));
    }

    @Test
    public void validateSourceDocumentDetailListEmpty() {
        boolean result = sourceDocumentReconciliation.validateSourceDocumentDetailList(reconciliationParameters);

        assertThat(result, is(false));
    }

    @Test
    public void validateSourceDocumentDetailListInValidDetail() {
        element3 = new SourceDocumentReconciliationParameters();
        element3.setSourceDocumentUuid("df937f9856b9436baf915b1f873129a6");
        element3.setSourceDocumentDetailUuid("776747d4-6b6e-487c-ad9e-45881d70d9ec");
        element3.setFinancialContractNo("2495a5ce-094e-4eb6-9fb9-95454b138427");
        element3.setLedgerBookNo("6aee97b7-2681-40a0-9e8a-2ae5115e716f");
        element3.setSecondType("2495a5ce-094e-4eb6-9fb9-95454b138427");
        element3.setDetaislFile(true);
        reconciliationParameters.add(element3);

        element4 = new SourceDocumentReconciliationParameters();
        element4.setSourceDocumentUuid("df937f9856b9436baf915b1f873129a6");
        element4.setSourceDocumentDetailUuid("776747d4-6b6e-487c-ad9e-45881d70d9ec");
        element4.setFinancialContractNo("2495a5ce-094e-4eb6-9fb9-95454b138427");
        element4.setLedgerBookNo("6aee97b7-2681-40a0-9e8a-2ae5115e716f");
        element4.setSecondType("2495a5ce-094e-4eb6-9fb9-95454b138427");
        element4.setDetaislFile(true);
        reconciliationParameters.add(element4);

        boolean result = sourceDocumentReconciliation.
                validateSourceDocumentDetailList(reconciliationParameters);

        assertThat(result, is(false));
    }

    @Test
    public void fetchVirtualAccountAndBusinessPaymentVoucherTransfer() {
        element3 = new SourceDocumentReconciliationParameters();
        element3.setSourceDocumentUuid("8d379149-2130-41ab-8451-7f8037233a38");
        element3.setLedgerBookNo("7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58");
        reconciliationParameters.add(element3);

        boolean result = sourceDocumentReconciliation.fetchVirtualAccountAndBusinessPaymentVoucherTransfer(
                reconciliationParameters);

        assertThat(result, is(true));
    }

    @Test
    public void fetchVirtualAccountAndBusinessPaymentVoucherTransferEmptyList() {
        boolean result = sourceDocumentReconciliation.fetchVirtualAccountAndBusinessPaymentVoucherTransfer(
                reconciliationParameters);

        assertThat(result, is(false));
    }

    @Test
    public void fetchVirtualAccountAndBusinessPaymentVoucherTransferListSizeGreaterThanOne() {
        element3 = new SourceDocumentReconciliationParameters();
        element3.setSourceDocumentUuid("df937f9856b9436baf915b1f873129a6");
        element3.setSourceDocumentDetailUuid("776747d4-6b6e-487c-ad9e-45881d70d9ec");
        element3.setFinancialContractNo("2495a5ce-094e-4eb6-9fb9-95454b138427");
        element3.setLedgerBookNo("6aee97b7-2681-40a0-9e8a-2ae5115e716f");
        element3.setSecondType("2495a5ce-094e-4eb6-9fb9-95454b138427");
        element3.setDetaislFile(true);
        reconciliationParameters.add(element3);

        element4 = new SourceDocumentReconciliationParameters();
        element4.setSourceDocumentUuid("df937f9856b9436baf915b1f873129a6");
        element4.setSourceDocumentDetailUuid("776747d4-6b6e-487c-ad9e-45881d70d9ec");
        element4.setFinancialContractNo("2495a5ce-094e-4eb6-9fb9-95454b138427");
        element4.setLedgerBookNo("6aee97b7-2681-40a0-9e8a-2ae5115e716f");
        element4.setSecondType("2495a5ce-094e-4eb6-9fb9-95454b138427");
        element4.setDetaislFile(true);
        reconciliationParameters.add(element4);

        boolean result = sourceDocumentReconciliation.fetchVirtualAccountAndBusinessPaymentVoucherTransfer(
                reconciliationParameters);

        assertThat(result, is(false));
    }

    @Ignore
    @Test
    public void sourceDocumentRecoverDetails() throws JsonProcessingException {
        element3 = new SourceDocumentReconciliationParameters();
        element3.setSourceDocumentUuid("47236aca-90ac-4d96-801e-f0c4c551309a");
        element3.setSourceDocumentDetailUuid("806cea0e-d2b9-4f87-bdd8-11c6ac0bcac4");
        element3.setFinancialContractNo("G31700");
        element3.setLedgerBookNo("6aee97b7-2681-40a0-9e8a-2ae5115e716f");
        element3.setSecondType("enum.voucher-type.third_party_deduction_voucher");
        element3.setDetaislFile(true);
        reconciliationParameters.add(element3);

        element4 = new SourceDocumentReconciliationParameters();
        element4.setSourceDocumentUuid("47236aca-90ac-4d96-801e-f0c4c551309a");
        element4.setSourceDocumentDetailUuid("1c534f45-4f35-46af-847d-01d7affd4237");
        element4.setFinancialContractNo("G31700");
        element4.setLedgerBookNo("6aee97b7-2681-40a0-9e8a-2ae5115e716f");
        element4.setSecondType("enum.voucher-type.third_party_deduction_voucher");
        element4.setDetaislFile(true);
        reconciliationParameters.add(element4);

        boolean result = sourceDocumentReconciliation.sourceDocumentRecoverDetails(reconciliationParameters);

        assertThat(result, is(true));
    }

    @Test
    public void sourceDocumentRecoverDetailsEmptyList() throws JsonProcessingException {
        boolean result = sourceDocumentReconciliation.sourceDocumentRecoverDetails(reconciliationParameters);

        assertThat(result, is(false));
    }

    @Test
    public void unfreezeCapital() {
        element3 = new SourceDocumentReconciliationParameters();
        element3.setSourceDocumentUuid("df937f9856b9436baf915b1f873129a6");
        element3.setSourceDocumentDetailUuid("776747d4-6b6e-487c-ad9e-45881d70d9ec");
        element3.setFinancialContractNo("G31700");
        element3.setLedgerBookNo("7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58");
        element3.setSecondType("2495a5ce-094e-4eb6-9fb9-95454b138427");
        element3.setDetaislFile(true);
        reconciliationParameters.add(element3);

        boolean result = sourceDocumentReconciliation.unfreezeCapital(reconciliationParameters);

        assertThat(result, is(true));
    }

    @Test
    public void unfreezeCapitalEmptyList() {
        boolean result = sourceDocumentReconciliation.unfreezeCapital(reconciliationParameters);

        assertThat(result, is(false));
    }

    @Test
    public void unfreezeCapitalListSizeGreaterThanOne() {
        element3 = new SourceDocumentReconciliationParameters();
        element3.setSourceDocumentUuid("df937f9856b9436baf915b1f873129a6");
        element3.setSourceDocumentDetailUuid("776747d4-6b6e-487c-ad9e-45881d70d9ec");
        element3.setFinancialContractNo("2495a5ce-094e-4eb6-9fb9-95454b138427");
        element3.setLedgerBookNo("6aee97b7-2681-40a0-9e8a-2ae5115e716f");
        element3.setSecondType("2495a5ce-094e-4eb6-9fb9-95454b138427");
        element3.setDetaislFile(true);
        reconciliationParameters.add(element3);

        element4 = new SourceDocumentReconciliationParameters();
        element4.setSourceDocumentUuid("df937f9856b9436baf915b1f873129a6");
        element4.setSourceDocumentDetailUuid("776747d4-6b6e-487c-ad9e-45881d70d9ec");
        element4.setFinancialContractNo("2495a5ce-094e-4eb6-9fb9-95454b138427");
        element4.setLedgerBookNo("6aee97b7-2681-40a0-9e8a-2ae5115e716f");
        element4.setSecondType("2495a5ce-094e-4eb6-9fb9-95454b138427");
        element4.setDetaislFile(true);
        reconciliationParameters.add(element4);

        boolean result = sourceDocumentReconciliation.unfreezeCapital(reconciliationParameters);

        assertThat(result, is(false));
    }
}