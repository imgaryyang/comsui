package com.suidifu.matryoshaka.test.service;

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

import com.suidifu.matryoshka.service.FinancialContractSnapshotService;
import com.suidifu.matryoshka.snapshot.FinancialContractSnapshot;
import com.zufangbao.sun.entity.financial.FinancialContractType;

/**
 * Created by hwr on 17-7-5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={

        "classpath:/local/applicationContext-*.xml",
})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class FinancialContractServiceTest extends AbstractTransactionalJUnit4SpringContextTests{
    @Autowired
    private FinancialContractSnapshotService financialContractService;

    @Test
    @Sql("classpath:test/yunxin/test_get_financialcontractsnapshot_by_financialcontractuuid.sql")
    public void test_get_financialcontractsnapshot_by_financialcontractuuid(){
        FinancialContractSnapshot snapshot = financialContractService.get_financialcontractsnapshot_by_financialcontractuuid("8ed6eefe-13d9-4532-8389-8f8c7692b891");
        Assert.assertNotNull(snapshot);
        Assert.assertEquals(snapshot.getFinancialContractUuid(), "8ed6eefe-13d9-4532-8389-8f8c7692b891");
        Assert.assertEquals(snapshot.getContractNo(), "G26700");
        Assert.assertEquals(snapshot.getFinancialContractType(), FinancialContractType.ConsumerLoan);
        Assert.assertFalse(snapshot.isAllowFreewheelingRepayment());
    }

    @Test
    @Sql("classpath:test/yunxin/test_get_financialcontractsnapshot_by_contractno.sql")
    public void test_get_financialcontractsnapshot_by_contractno(){
        FinancialContractSnapshot snapshot = financialContractService.get_financialcontractsnapshot_by_contractno("G26700");
        Assert.assertNotNull(snapshot);
        Assert.assertEquals(snapshot.getFinancialContractUuid(), "8ed6eefe-13d9-4532-8389-8f8c7692b891");
        Assert.assertEquals(snapshot.getContractNo(), "G26700");
        Assert.assertEquals(snapshot.getFinancialContractType(), FinancialContractType.ConsumerLoan);
        Assert.assertTrue(snapshot.isAllowFreewheelingRepayment());
    }
}
