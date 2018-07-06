package com.suidifu.dowjones.dao.impl;

import com.suidifu.dowjones.Dowjones;
import com.suidifu.dowjones.dao.ContractReportDAO;
import com.suidifu.dowjones.vo.enumeration.RepurchaseMode;
import com.suidifu.dowjones.vo.request.InputParameter;
import com.suidifu.dowjones.vo.request.StaticOverdueRateInputParameter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.*;

// now() = 2017-11-30
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Dowjones.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
public class ContractReportDAOImplTest {

    @Autowired
    private ContractReportDAO contractReportDAO;
    private String TEST_DATA_FINANCIAL_CONTRACT_UUID = "d2812bc5-5057-4a91-b3fd-9019506f0499";
    private Map<String, String> TEST_DATA_UUIDS = new HashMap<String, String>() {
        private static final long serialVersionUID = 8598809188519547139L;

        {
            put("0", "008634e0-9ec1-4272-ba4d-e7458fabe5b7");
            put("1", "0199d0e9-c03e-4b32-a7bd-957915dc84f2");
            put("2", "0070d689-0037-4f4a-8e07-10697627b3f0");
            put("3", "0032bbf7-90ad-44ab-bc71-fda4c75d7a79");
            put("4", "00a566c8-d2e9-41dd-b2fb-f482b777a166");
            put("5", "00d39ba2-5a55-46f6-b047-0fda11097b46");
            put("6", "00b38d38-25c8-41fc-a8c0-d209ec9b2867");
            put("7", "017ce1e5-dee7-47d9-865a-dd0388b5b170");
            put("8", "04e2791b-580a-489c-8a88-4aa6763ce2cd");
            put("9", "01d21596-a159-44f3-91f6-b55e84baa9ee");
            put("10", "0281677a-a466-43cb-9c24-05dac233fa0c");
            put("11", "05139967-10a6-4c45-a975-8f8a59a6e592");
            put("12", "020bd67e-463c-49ab-8534-e5a60f436668");
            put("13", "03d67b4d-06d9-4f35-bcec-a6d299836346");
            put("14", "0502cc08-9fe1-4e62-b6a7-738f0f5da653");
            put("15", "0432c7c0-46d6-42a5-8be6-6c0d6bad65a9");
            put("16", "1319235f-3a5f-481a-986c-a3c8cea75a0c");
            put("17", "495abe68-3b10-4ea0-88c9-4297d3e88ef0");
            put("18", "9b1b181c-59bf-486e-85bf-b279ac83078b");
            put("19", "6e0bc421-f604-4424-83dc-a7bb47d27fca");
        }
    };

    private InputParameter buildInputParameter(String financialContractUuid, String overdueStage,
                                               boolean includeUnconfirmed, boolean includeRepurchase, int periodDays) {
        InputParameter inputParameter = new InputParameter();
        inputParameter.setFinancialContractUuid(financialContractUuid);
        inputParameter.setOverdueStage(overdueStage);
        inputParameter.setIncludeUnconfirmed(includeUnconfirmed);
        inputParameter.setIncludeRepurchase(includeRepurchase);
        inputParameter.setPeriodDays(periodDays);
        return inputParameter;
    }

    private StaticOverdueRateInputParameter buildStaticOverdueRateInputParameter(String financialContractUuid,
                                                                                 String overdueStage, boolean includeUnconfirmed, boolean includeRepurchase, int periodDays, int year,
                                                                                 int month) {
        StaticOverdueRateInputParameter inputParameter = new StaticOverdueRateInputParameter();
        inputParameter.setFinancialContractUuid(financialContractUuid);
        inputParameter.setOverdueStage(overdueStage);
        inputParameter.setIncludeUnconfirmed(includeUnconfirmed);
        inputParameter.setIncludeRepurchase(includeRepurchase);
        inputParameter.setPeriodDays(periodDays);
        inputParameter.setYear(year);
        inputParameter.setMonth(month);
        return inputParameter;
    }

    private void assertArray(String[] a, String... args) {
        Arrays.sort(a);
        Arrays.sort(args);
        Assert.assertTrue(Arrays.equals(a, args));
    }

    private void assertList(List<String> uuids, List<String> args) {
        assertArray((String[]) uuids.toArray(new String[uuids.size()]),
                (String[]) args.toArray(new String[args.size()]));
    }

    private void assertUuids(String typeCode, List<String> uuids) {
        assertList(getUuids(typeCode), uuids);
    }

    private List<String> getUuids(String typeCode) {
        String[] types = typeCode.split(",");
        List<String> compareUuids = new ArrayList<>();
        for (String type : types) {
            compareUuids.add(TEST_DATA_UUIDS.get(type));
        }
        return compareUuids;
    }

    // 121_1_1.逾期未偿 已逾期_待确认
    @Test
    public void testx1_1() {
        InputParameter inputParameter = buildInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "0", false, true, 30);
        List<String> uuids = contractReportDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        assertUuids("0,4", uuids);

        inputParameter = buildInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "1", false, true, 30);
        uuids = contractReportDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        assertUuids("1,5", uuids);

        inputParameter = buildInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "2", false, true, 30);
        uuids = contractReportDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        assertUuids("2,6", uuids);

        inputParameter = buildInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "3", false, true, 30);
        uuids = contractReportDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        assertUuids("3,7", uuids);

        inputParameter = buildInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "0,1", false, true, 30);
        uuids = contractReportDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        assertUuids("0,4,1,5", uuids);

        inputParameter = buildInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "1,2", false, true, 30);
        uuids = contractReportDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        assertUuids("1,5,2,6", uuids);

        inputParameter = buildInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "2,3", false, true, 30);
        uuids = contractReportDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        assertUuids("2,6,3,7", uuids);

        inputParameter = buildInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "0,1,2", false, true, 30);
        uuids = contractReportDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        assertUuids("0,4,1,5,2,6", uuids);

        inputParameter = buildInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "1,2,3", false, true, 30);
        uuids = contractReportDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        assertUuids("1,5,2,6,3,7", uuids);

        inputParameter = buildInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "0,1,2,3", false, true, 30);
        uuids = contractReportDAO.getContractUuidsByOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        assertUuids("0,4,1,5,2,6,3,7", uuids);

        inputParameter = buildInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "0", true, true, 30);
        uuids = contractReportDAO.getContractUuidsByUnConfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        assertUuids("8,12", uuids);

        inputParameter = buildInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "1", true, true, 30);
        uuids = contractReportDAO.getContractUuidsByUnConfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        assertUuids("9,13", uuids);

        inputParameter = buildInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "2", true, true, 30);
        uuids = contractReportDAO.getContractUuidsByUnConfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        assertUuids("10,14", uuids);

        inputParameter = buildInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "3", true, true, 30);
        uuids = contractReportDAO.getContractUuidsByUnConfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        assertUuids("11,15", uuids);

        inputParameter = buildInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "0,1", true, true, 30);
        uuids = contractReportDAO.getContractUuidsByUnConfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        assertUuids("8,12,9,13", uuids);

        inputParameter = buildInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "1,2", true, true, 30);
        uuids = contractReportDAO.getContractUuidsByUnConfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        assertUuids("9,13,10,14", uuids);

        inputParameter = buildInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "2,3", true, true, 30);
        uuids = contractReportDAO.getContractUuidsByUnConfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        assertUuids("10,14,11,15", uuids);

        inputParameter = buildInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "0,1,2", true, true, 30);
        uuids = contractReportDAO.getContractUuidsByUnConfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        assertUuids("8,12,9,13,10,14", uuids);

        inputParameter = buildInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "1,2,3", true, true, 30);
        uuids = contractReportDAO.getContractUuidsByUnConfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        assertUuids("9,13,10,14,11,15", uuids);

        inputParameter = buildInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "0,1,2,3", true, true, 30);
        uuids = contractReportDAO.getContractUuidsByUnConfirmedAssetSet(inputParameter, RepurchaseMode.M1);
        assertUuids("8,12,9,13,10,14,11,15", uuids);
    }

    // 121_2_1.剩余贷款
    @Test
    public void testx2_1() {
        StaticOverdueRateInputParameter inputParameter = buildStaticOverdueRateInputParameter(
                TEST_DATA_FINANCIAL_CONTRACT_UUID, "0", false, true, 30, 2017, 11);
        List<String> uuids = contractReportDAO.getUnclearContractUuidsBy(inputParameter, RepurchaseMode.M1);
        assertUuids("0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19", uuids);
    }

    // 121_3_1.X月放款本金
    @Test
    public void testx3_1() {
        StaticOverdueRateInputParameter inputParameter = buildStaticOverdueRateInputParameter(
                TEST_DATA_FINANCIAL_CONTRACT_UUID, "0", false, true, 30, 2017, 11);
        BigDecimal aBigDecimal = contractReportDAO.getRemittanceTotalAmountByMonth(inputParameter);
        Assert.assertTrue(aBigDecimal.compareTo(new BigDecimal("0.00")) == 0);

        inputParameter = buildStaticOverdueRateInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "0", false, true, 30,
                2017, 5);
        aBigDecimal = contractReportDAO.getRemittanceTotalAmountByMonth(inputParameter);
        Assert.assertTrue(aBigDecimal.compareTo(new BigDecimal("96.00")) == 0);
    }

    // 121_4_1.X月发放贷款逾期
    @Test
    public void testx4_1() {
        StaticOverdueRateInputParameter inputParameter = buildStaticOverdueRateInputParameter(
                TEST_DATA_FINANCIAL_CONTRACT_UUID, "0", false, true, 30, 2017, 5);
        List<String> uuids = contractReportDAO.getOverdueLoanPrincipalByMonthAndOverdueAssetSet(inputParameter,
                RepurchaseMode.M1);
        assertUuids("0,4", uuids);

        inputParameter = buildStaticOverdueRateInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "1", false, true, 30,
                2017, 5);
        uuids = contractReportDAO.getOverdueLoanPrincipalByMonthAndOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        assertUuids("1,5", uuids);

        inputParameter = buildStaticOverdueRateInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "2", false, true, 30,
                2017, 5);
        uuids = contractReportDAO.getOverdueLoanPrincipalByMonthAndOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        assertUuids("2,6", uuids);

        inputParameter = buildStaticOverdueRateInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "3", false, true, 30,
                2017, 5);
        uuids = contractReportDAO.getOverdueLoanPrincipalByMonthAndOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        assertUuids("3,7", uuids);

        inputParameter = buildStaticOverdueRateInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "0,1", false, true, 30,
                2017, 5);
        uuids = contractReportDAO.getOverdueLoanPrincipalByMonthAndOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        assertUuids("0,4,1,5", uuids);

        inputParameter = buildStaticOverdueRateInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "1,2", false, true, 30,
                2017, 5);
        uuids = contractReportDAO.getOverdueLoanPrincipalByMonthAndOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        assertUuids("1,5,2,6", uuids);

        inputParameter = buildStaticOverdueRateInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "2,3", false, true, 30,
                2017, 5);
        uuids = contractReportDAO.getOverdueLoanPrincipalByMonthAndOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        assertUuids("2,6,3,7", uuids);

        inputParameter = buildStaticOverdueRateInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "0,1,2", false, true,
                30, 2017, 5);
        uuids = contractReportDAO.getOverdueLoanPrincipalByMonthAndOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        assertUuids("0,4,1,5,2,6", uuids);

        inputParameter = buildStaticOverdueRateInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "1,2,3", false, true,
                30, 2017, 5);
        uuids = contractReportDAO.getOverdueLoanPrincipalByMonthAndOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        assertUuids("1,5,2,6,3,7", uuids);

        inputParameter = buildStaticOverdueRateInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "0,1,2,3", false, true,
                30, 2017, 5);
        uuids = contractReportDAO.getOverdueLoanPrincipalByMonthAndOverdueAssetSet(inputParameter, RepurchaseMode.M1);
        assertUuids("0,4,1,5,2,6,3,7", uuids);

        inputParameter = buildStaticOverdueRateInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "0", true, true, 30,
                2017, 5);
        uuids = contractReportDAO.getOverdueLoanPrincipalByMonthAndUnconfirmedAssetSet(inputParameter,
                RepurchaseMode.M1);
        assertUuids("8,12", uuids);

        inputParameter = buildStaticOverdueRateInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "1", true, true, 30,
                2017, 5);
        uuids = contractReportDAO.getOverdueLoanPrincipalByMonthAndUnconfirmedAssetSet(inputParameter,
                RepurchaseMode.M1);
        assertUuids("9,13", uuids);

        inputParameter = buildStaticOverdueRateInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "2", true, true, 30,
                2017, 5);
        uuids = contractReportDAO.getOverdueLoanPrincipalByMonthAndUnconfirmedAssetSet(inputParameter,
                RepurchaseMode.M1);
        assertUuids("10,14", uuids);

        inputParameter = buildStaticOverdueRateInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "3", true, true, 30,
                2017, 5);
        uuids = contractReportDAO.getOverdueLoanPrincipalByMonthAndUnconfirmedAssetSet(inputParameter,
                RepurchaseMode.M1);
        assertUuids("11,15", uuids);

        inputParameter = buildStaticOverdueRateInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "0,1", true, true, 30,
                2017, 5);
        uuids = contractReportDAO.getOverdueLoanPrincipalByMonthAndUnconfirmedAssetSet(inputParameter,
                RepurchaseMode.M1);
        assertUuids("8,12,9,13", uuids);

        inputParameter = buildStaticOverdueRateInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "1,2", true, true, 30,
                2017, 5);
        uuids = contractReportDAO.getOverdueLoanPrincipalByMonthAndUnconfirmedAssetSet(inputParameter,
                RepurchaseMode.M1);
        assertUuids("9,13,10,14", uuids);

        inputParameter = buildStaticOverdueRateInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "2,3", true, true, 30,
                2017, 5);
        uuids = contractReportDAO.getOverdueLoanPrincipalByMonthAndUnconfirmedAssetSet(inputParameter,
                RepurchaseMode.M1);
        assertUuids("10,14,11,15", uuids);

        inputParameter = buildStaticOverdueRateInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "0,1,2", true, true,
                30, 2017, 5);
        uuids = contractReportDAO.getOverdueLoanPrincipalByMonthAndUnconfirmedAssetSet(inputParameter,
                RepurchaseMode.M1);
        assertUuids("8,12,9,13,10,14", uuids);

        inputParameter = buildStaticOverdueRateInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "1,2,3", true, true,
                30, 2017, 5);
        uuids = contractReportDAO.getOverdueLoanPrincipalByMonthAndUnconfirmedAssetSet(inputParameter,
                RepurchaseMode.M1);
        assertUuids("9,13,10,14,11,15", uuids);

        inputParameter = buildStaticOverdueRateInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "0,1,2,3", true, true,
                30, 2017, 5);
        uuids = contractReportDAO.getOverdueLoanPrincipalByMonthAndUnconfirmedAssetSet(inputParameter,
                RepurchaseMode.M1);
        assertUuids("8,12,9,13,10,14,11,15", uuids);

    }

    // 121_5_1.X月发放贷款的剩余贷款
    @Test
    public void testx5_1() {
        StaticOverdueRateInputParameter inputParameter = buildStaticOverdueRateInputParameter(
                TEST_DATA_FINANCIAL_CONTRACT_UUID, "0", false, true, 30, 2017, 5);
        List<String> uuids = contractReportDAO.getRemittanceUnclearLoansBy(inputParameter,
                RepurchaseMode.M1);
        assertUuids("0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15", uuids);
    }

    // 121_6_1.账本流水
    @Test
    public void testx6_1() {
        InputParameter inputParameter = buildInputParameter(TEST_DATA_FINANCIAL_CONTRACT_UUID, "0", false, true, 30);
        List<String> uuids = getUuids("0,1,2,3");
        BigDecimal amount = contractReportDAO.getUnEarnedPrincipalFromLedgerBookBy(inputParameter, uuids);//true已回购计入已还，账本需要计算回购未偿
        Assert.assertTrue(amount.compareTo(new BigDecimal("1000.00")) == 0);


        amount = contractReportDAO.getUnEarnedPrincipalInterestFromLedgerBookBy(inputParameter, uuids);//true已回购计入已还，账本需要计算回购未偿
        Assert.assertTrue(amount.compareTo(new BigDecimal("1100.00")) == 0);

    }


    // 121_7_1.回购单
    @Test
    public void testx7_1() {
        List<String> uuids = getUuids("16,17,18,19");
        BigDecimal amount = contractReportDAO.getUnEarnedPrincipalFromRepurchaseBy(uuids);
        Assert.assertTrue(amount.compareTo(new BigDecimal("5700.00")) == 0);


        amount = contractReportDAO.getUnEarnedPrincipalInterestFromRepurchaseBy(uuids);
        Assert.assertTrue(amount.compareTo(new BigDecimal("5850.00")) == 0);


    }

}
