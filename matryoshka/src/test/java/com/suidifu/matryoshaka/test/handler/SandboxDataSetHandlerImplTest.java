package com.suidifu.matryoshaka.test.handler;

import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.snapshot.*;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.AssetClearStatus;
import com.zufangbao.sun.yunxin.entity.ExecutingStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.zufangbao.sun.utils.DateUtils.LONG_DATE_FORMAT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by louguanyang on 2017/5/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/context/applicationContext-*.xml",
        "classpath:/local/applicationContext-*.xml",
})
@Transactional()
@Rollback(false)
public class SandboxDataSetHandlerImplTest {

    @Autowired
    private SandboxDataSetHandler sandboxDataSetHandler;

    @Test
    @Sql("classpath:test/yunxin/sandbox/get_sandbox_by_contract_uniqueId_contractNo.sql")
    public void test_get_sandbox_by_contract_uniqueId_contractNo() throws Exception {
        String uniqueId = "ad6b3053-8625-4eb6-a78a-dcabc6132b5d";
        String contractNo = "";
        SandboxDataSet sandboxDataSet = sandboxDataSetHandler.get_sandbox_by_contract_uniqueId_contractNo(uniqueId, contractNo);
        assertNotNull(sandboxDataSet);

        String contractUuid = "be834b15-56a7-4175-b926-64c90869a2f0";
        String financialContractUuid = "b674a323-0c30-4e4b-9eba-b14e05a9d80a";
        String customerUuid = "c714fe88-8ed3-45e9-9807-59ca5bd37ae3";

        ContractSnapshot contractSnapshot = sandboxDataSet.getContractSnapshot();
        assertNotNull(contractSnapshot);
        assertEquals(uniqueId, contractSnapshot.getUniqueId());
        assertEquals(contractUuid, contractSnapshot.getUuid());
        assertEquals(financialContractUuid, contractSnapshot.getFinancialContractUuid());
        assertEquals(DateUtils.asDay("2017-04-21"), contractSnapshot.getBeginDate());
        assertEquals(DateUtils.asDay("2019-02-02"), contractSnapshot.getEndDate());
        assertEquals(customerUuid, contractSnapshot.getCustomerUuid());

        String contractAccountUuid = "ee49f71e318b4976876a9fe367f464d0";
        String id_card_num = "410402198801111211";
        String name = "测试";
        String payAcNo = "621700121007322332325327590";
        String bank = "中国建设银行";
        String province = "上海市";
        String provinceCode = "310000";
        String city = "上海市";
        String cityCode = "310100";
        String standardBankCode = "C10105";

        CustomerAccountSnapshot customerAccountSnapshot = contractSnapshot.getCustomerAccountSnapshot();
        assertNotNull(customerAccountSnapshot);
        assertEquals(customerUuid, customerAccountSnapshot.getCustomerUuid());
        assertEquals(contractUuid, customerAccountSnapshot.getContractUuid());
        assertEquals(contractAccountUuid, customerAccountSnapshot.getContractAccountUuid());
        assertEquals(null, customerAccountSnapshot.getVirtualAccountUuid());
        assertEquals(name, customerAccountSnapshot.getName());
        assertEquals(null, customerAccountSnapshot.getMobile());
        assertEquals(id_card_num, customerAccountSnapshot.getAccount());
        assertEquals(payAcNo, customerAccountSnapshot.getPayAcNo());
        assertEquals(bank, customerAccountSnapshot.getBank().trim());
        assertEquals(null, customerAccountSnapshot.getBankCode());
        assertEquals(province, customerAccountSnapshot.getProvince());
        assertEquals(provinceCode, customerAccountSnapshot.getProvinceCode());
        assertEquals(city, customerAccountSnapshot.getCity());
        assertEquals(cityCode, customerAccountSnapshot.getCityCode());
        assertEquals(standardBankCode, customerAccountSnapshot.getStandardBankCode());

        FinancialContractSnapshot financialContractSnapshot = sandboxDataSet.getFinancialContractSnapshot();
        assertNotNull(contractSnapshot);
        assertEquals("G32000", financialContractSnapshot.getContractNo());
        assertEquals(financialContractUuid, financialContractSnapshot.getFinancialContractUuid());

        List<PaymentPlanSnapshot> paymentPlanSnapshotList = sandboxDataSet.getPaymentPlanSnapshotList();
        assertEquals(2, paymentPlanSnapshotList.size());

        String assetSetUuid1 = "922b2d45-da2c-4b6b-87a2-30a5303a5da0";
        String singleLoanContractNo1 = "ZC1691381619856510976";
        BigDecimal assetInitialValue1 = new BigDecimal("10001.00");
        BigDecimal assetPrincipalValue1 = new BigDecimal("10000.00");
        BigDecimal assetInterestValue1 = new BigDecimal("1.00");
        Date assetRecycleDate1 = DateUtils.asDay("2027-05-16");
        int currentPeriod1 = 2;
        Date createTime1 = DateUtils.parseDate("2017-04-21 17:50:40", LONG_DATE_FORMAT);

        PaymentPlanSnapshot paymentPlanSnapshot1 = paymentPlanSnapshotList.get(0);
        assertEquals(assetSetUuid1, paymentPlanSnapshot1.getAssetUuid());
        assertEquals(singleLoanContractNo1, paymentPlanSnapshot1.getSingleLoanContractNo());
        assertEquals(assetInitialValue1, paymentPlanSnapshot1.getAssetInitialValue());
        assertEquals(assetPrincipalValue1, paymentPlanSnapshot1.getAssetPrincipalValue());
        assertEquals(assetInterestValue1, paymentPlanSnapshot1.getAssetInterestValue());
        assertEquals(assetRecycleDate1, paymentPlanSnapshot1.getAssetRecycleDate());
        assertEquals(currentPeriod1, paymentPlanSnapshot1.getCurrentPeriod());
        assertEquals(financialContractUuid, paymentPlanSnapshot1.getFinancialContractUuid());
        assertEquals(customerUuid, paymentPlanSnapshot1.getCustomerUuid());
        assertEquals(contractUuid, paymentPlanSnapshot1.getContractUuid());
        assertEquals(AssetClearStatus.UNCLEAR, paymentPlanSnapshot1.getAssetStatus());
        assertEquals(ExecutingStatus.UNEXECUTED, paymentPlanSnapshot1.getExecutingStatus());
        assertEquals(false, paymentPlanSnapshot1.isCanBeRollbacked());
        assertEquals(createTime1, paymentPlanSnapshot1.getCreateTime());
        PaymentPlanExtraChargeSnapshot assetSetExtraChargeSnapshot1 =
                paymentPlanSnapshot1.getAssetSetExtraChargeSnapshot();
        assertNotNull(assetSetExtraChargeSnapshot1);

        PaymentPlanSnapshot paymentPlanSnapshot2 = paymentPlanSnapshotList.get(1);
        assertEquals("1dcb05fd-8602-4030-b45b-180b4d0b8281", paymentPlanSnapshot2.getAssetUuid());
    }

    @Test
    @Sql("classpath:test/yunxin/sandbox/test_getSandbox_by_repaymentPlanNoList.sql")
    public void test_getSandbox_by_repaymentPlanNoList() throws Exception {
        List<String> repaymentPlanNoList = Arrays.asList("ZC1691381619856510976", "ZC1691381620527599616");
        String contractUuid = "be834b15-56a7-4175-b926-64c90869a2f0";
        String financialContractUuid = "b674a323-0c30-4e4b-9eba-b14e05a9d80a";
        SandboxDataSet sandboxDataSet = sandboxDataSetHandler.get_sandbox_by_repaymentPlanNoList(financialContractUuid, contractUuid, repaymentPlanNoList);

        String uniqueId = "ad6b3053-8625-4eb6-a78a-dcabc6132b5d";
        String customerUuid = "c714fe88-8ed3-45e9-9807-59ca5bd37ae3";

        ContractSnapshot contractSnapshot = sandboxDataSet.getContractSnapshot();
        assertNotNull(contractSnapshot);
        assertEquals(uniqueId, contractSnapshot.getUniqueId());
        assertEquals(contractUuid, contractSnapshot.getUuid());
        assertEquals(financialContractUuid, contractSnapshot.getFinancialContractUuid());
        assertEquals(DateUtils.asDay("2017-04-21"), contractSnapshot.getBeginDate());
        assertEquals(DateUtils.asDay("2019-02-02"), contractSnapshot.getEndDate());
        assertEquals(customerUuid, contractSnapshot.getCustomerUuid());

        String contractAccountUuid = "ee49f71e318b4976876a9fe367f464d0";
        String id_card_num = "410402198801111211";
        String name = "测试";
        String payAcNo = "621700121007322332325327590";
        String bank = "中国建设银行";
        String province = "上海市";
        String provinceCode = "310000";
        String city = "上海市";
        String cityCode = "310100";
        String standardBankCode = "C10105";

        CustomerAccountSnapshot customerAccountSnapshot = contractSnapshot.getCustomerAccountSnapshot();
        assertNotNull(customerAccountSnapshot);
        assertEquals(customerUuid, customerAccountSnapshot.getCustomerUuid());
        assertEquals(contractUuid, customerAccountSnapshot.getContractUuid());
        assertEquals(contractAccountUuid, customerAccountSnapshot.getContractAccountUuid());
        assertEquals(null, customerAccountSnapshot.getVirtualAccountUuid());
        assertEquals(name, customerAccountSnapshot.getName());
        assertEquals(null, customerAccountSnapshot.getMobile());
        assertEquals(id_card_num, customerAccountSnapshot.getAccount());
        assertEquals(payAcNo, customerAccountSnapshot.getPayAcNo());
        assertEquals(bank, customerAccountSnapshot.getBank().trim());
        assertEquals(null, customerAccountSnapshot.getBankCode());
        assertEquals(province, customerAccountSnapshot.getProvince());
        assertEquals(provinceCode, customerAccountSnapshot.getProvinceCode());
        assertEquals(city, customerAccountSnapshot.getCity());
        assertEquals(cityCode, customerAccountSnapshot.getCityCode());
        assertEquals(standardBankCode, customerAccountSnapshot.getStandardBankCode());

        FinancialContractSnapshot financialContractSnapshot = sandboxDataSet.getFinancialContractSnapshot();
        assertNotNull(contractSnapshot);
        assertEquals("G32000", financialContractSnapshot.getContractNo());
        assertEquals(financialContractUuid, financialContractSnapshot.getFinancialContractUuid());

        List<PaymentPlanSnapshot> paymentPlanSnapshotList = sandboxDataSet.getPaymentPlanSnapshotList();
        assertEquals(2, paymentPlanSnapshotList.size());

        String assetSetUuid1 = "922b2d45-da2c-4b6b-87a2-30a5303a5da0";
        String singleLoanContractNo1 = "ZC1691381619856510976";
        BigDecimal assetInitialValue1 = new BigDecimal("10001.00");
        BigDecimal assetPrincipalValue1 = new BigDecimal("10000.00");
        BigDecimal assetInterestValue1 = new BigDecimal("1.00");
        Date assetRecycleDate1 = DateUtils.asDay("2027-05-16");
        int currentPeriod1 = 2;
        Date createTime1 = DateUtils.parseDate("2017-04-21 17:50:40", LONG_DATE_FORMAT);

        PaymentPlanSnapshot paymentPlanSnapshot1 = paymentPlanSnapshotList.get(0);
        assertEquals(assetSetUuid1, paymentPlanSnapshot1.getAssetUuid());
        assertEquals(singleLoanContractNo1, paymentPlanSnapshot1.getSingleLoanContractNo());
        assertEquals(assetInitialValue1, paymentPlanSnapshot1.getAssetInitialValue());
        assertEquals(assetPrincipalValue1, paymentPlanSnapshot1.getAssetPrincipalValue());
        assertEquals(assetInterestValue1, paymentPlanSnapshot1.getAssetInterestValue());
        assertEquals(assetRecycleDate1, paymentPlanSnapshot1.getAssetRecycleDate());
        assertEquals(currentPeriod1, paymentPlanSnapshot1.getCurrentPeriod());
        assertEquals(financialContractUuid, paymentPlanSnapshot1.getFinancialContractUuid());
        assertEquals(customerUuid, paymentPlanSnapshot1.getCustomerUuid());
        assertEquals(contractUuid, paymentPlanSnapshot1.getContractUuid());
        assertEquals(AssetClearStatus.UNCLEAR, paymentPlanSnapshot1.getAssetStatus());
        assertEquals(ExecutingStatus.UNEXECUTED, paymentPlanSnapshot1.getExecutingStatus());
        assertEquals(false, paymentPlanSnapshot1.isCanBeRollbacked());
        assertEquals(createTime1, paymentPlanSnapshot1.getCreateTime());
        PaymentPlanExtraChargeSnapshot assetSetExtraChargeSnapshot1 =
                paymentPlanSnapshot1.getAssetSetExtraChargeSnapshot();
        assertNotNull(assetSetExtraChargeSnapshot1);

        PaymentPlanSnapshot paymentPlanSnapshot2 = paymentPlanSnapshotList.get(1);
        assertEquals("1dcb05fd-8602-4030-b45b-180b4d0b8281", paymentPlanSnapshot2.getAssetUuid());
    }

}