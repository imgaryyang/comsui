package com.suidifu.matryoshaka.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.suidifu.matryoshka.service.RepaymentPlanSnapshotService;
import com.suidifu.matryoshka.snapshot.PaymentPlanSnapshot;
import com.zufangbao.sun.yunxin.entity.AssetSet;

/**
 * Created by hwr on 17-7-5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={

        "classpath:/local/applicationContext-*.xml",
})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class RepaymentPlanSnapshotServiceTest extends AbstractTransactionalJUnit4SpringContextTests{
    @Autowired
    private RepaymentPlanSnapshotService repaymentPlanService;

//    @Test
//    @Sql("classpath:test/yunxin/repaymentplan/testGetRepaymentPlanByRepaymentCode.sql")
//    public void testGetRepaymentPlanByRepaymentCode() {
//        AssetSet assetSet1 = repaymentPlanService.getRepaymentPlanByRepaymentCode(null);
//        Assert.assertNull(assetSet1);
//
//        AssetSet assetSet2 = repaymentPlanService.getRepaymentPlanByRepaymentCode("");
//        Assert.assertNull(assetSet2);
//
//        AssetSet assetSet3 = repaymentPlanService.getRepaymentPlanByRepaymentCode("ZC2730FAE4092E0A6E");
//        Assert.assertEquals("ZC2730FAE4092E0A6E", assetSet3.getSingleLoanContractNo());
//    }

    @Test
    @Sql("classpath:test/yunxin/repaymentplan/test_get_all_assetSetSnapshot_list.sql")
    public void test_get_all_assetSetSnapshot_list() {
        // 贷款合同下全部还款计划都是有效的
        String contractUuid = "a0b0400d-5fa8-11e6-b2c2-00163e002839";
        List<PaymentPlanSnapshot> all_assetSetSnapshot_list = repaymentPlanService.get_all_assetSetSnapshot_list(contractUuid);
        assertNotNull(all_assetSetSnapshot_list);
        assertEquals(3, all_assetSetSnapshot_list.size());

        //	贷款合同下存在部分无效的还款计划的
        String contractUuid2 = "9e959510-c936-473a-90e4-204dd7352092";
        List<PaymentPlanSnapshot> all_assetSetSnapshot_list2 = repaymentPlanService.get_all_assetSetSnapshot_list
                (contractUuid2);
        assertNotNull(all_assetSetSnapshot_list2);
        // FIXME
        assertEquals(1, all_assetSetSnapshot_list2.size());
    }

    @Test
    @Sql("classpath:test/yunxin/repaymentplan/test_get_all_unclear_assetSetSnapshot_list.sql")
    public void test_get_all_unclear_assetSetSnapshot_list() {
        // 贷款合同下全部还款计划都是有效的, 3期 一条已还款, 两条未还款
        String contractUuid = "be834b15-56a7-4175-b926-64c90869a2f0";
        List<PaymentPlanSnapshot> all_assetSetSnapshot_list = repaymentPlanService.get_all_unclear_assetSetSnapshot_list(contractUuid);
        assertNotNull(all_assetSetSnapshot_list);
        assertEquals(2, all_assetSetSnapshot_list.size());

        //	贷款合同下存在 6条无效的还款计划， 有效的有5条，一条已还款 4条未还款
        String contractUuid2 = "2f1a18f0-bb62-42a6-b3eb-fb7c1e91cfe3";
        List<PaymentPlanSnapshot> all_assetSetSnapshot_list2 = repaymentPlanService.get_all_unclear_assetSetSnapshot_list
                (contractUuid2);
        assertNotNull(all_assetSetSnapshot_list2);
        // FIXME
        assertEquals(4, all_assetSetSnapshot_list2.size());
    }

    @Test
    @Sql("classpath:test/yunxin/repaymentplan/test_get_assetSetSnapshot_list_by_no_list.sql")
    public void test_get_assetSetSnapshot_list_by_no_list() {
        List<String> repaymentPlanNoList = Arrays.asList("ZC1691381619856510976", "ZC1691381620527599616");
        List<PaymentPlanSnapshot> planSnapshotList = repaymentPlanService
                .get_assetSetSnapshot_list_by_no_list(repaymentPlanNoList);
        Assert.assertEquals(2, planSnapshotList.size());
    }
}
