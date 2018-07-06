package com.suidifu.matryoshaka.test.service;

import com.suidifu.matryoshka.service.ContractSnapshotService;
import com.suidifu.matryoshka.snapshot.ContractSnapshot;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by hwr on 17-7-5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={

        "classpath:/local/applicationContext-*.xml",
})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class ContractServiceTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    private ContractSnapshotService contractService;


    @Test
    @Sql("classpath:test/yunxin/contract/test_get_contractSnapshot_by_contractUuid.sql")
    public void test_get_contractSnapshot_by_contractUuid() {
        String contractUuid = "be834b15-56a7-4175-b926-64c90869a2f0";
        ContractSnapshot contractSnapshot = contractService.get_contractSnapshot_by_contractUuid(contractUuid);
        assertNotNull(contractSnapshot);
        assertEquals("be834b15-56a7-4175-b926-64c90869a2f0", contractSnapshot.getUuid());
        assertEquals("ad6b3053-8625-4eb6-a78a-dcabc6132b5d", contractSnapshot.getUniqueId());
        assertEquals(com.zufangbao.sun.utils.DateUtils.parseDate("2017-04-21"), contractSnapshot.getBeginDate());
        assertEquals(com.zufangbao.sun.utils.DateUtils.parseDate("2019-02-02"), contractSnapshot.getEndDate());
        assertEquals("b674a323-0c30-4e4b-9eba-b14e05a9d80a", contractSnapshot.getFinancialContractUuid());
        assertEquals("c714fe88-8ed3-45e9-9807-59ca5bd37ae3", contractSnapshot.getCustomerUuid());
    }


    @Test
    @Sql("classpath:test/yunxin/contract/test_get_contractSnapshot_by_uniqueId_or_contractNo.sql")
    public void test_get_contractSnapshot_by_uniqueId_or_contractNo() {
        String uniqueId = "ad6b3053-8625-4eb6-a78a-dcabc6132b5d";
        String contractNo = "123456";
        ContractSnapshot contractSnapshot = contractService.get_contractSnapshot_by_uniqueId_or_contractNo(uniqueId,
                contractNo);
        assertNotNull(contractSnapshot);
        assertEquals("be834b15-56a7-4175-b926-64c90869a2f0", contractSnapshot.getUuid());
        assertEquals("ad6b3053-8625-4eb6-a78a-dcabc6132b5d", contractSnapshot.getUniqueId());
        assertEquals(com.zufangbao.sun.utils.DateUtils.parseDate("2017-04-21"), contractSnapshot.getBeginDate());
        assertEquals(com.zufangbao.sun.utils.DateUtils.parseDate("2019-02-02"), contractSnapshot.getEndDate());
        assertEquals("b674a323-0c30-4e4b-9eba-b14e05a9d80a", contractSnapshot.getFinancialContractUuid());
        assertEquals("c714fe88-8ed3-45e9-9807-59ca5bd37ae3", contractSnapshot.getCustomerUuid());
    }

}
