package com.suidifu.matryoshaka.test.service;

import java.math.BigDecimal;

import org.hibernate.SessionFactory;
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

import com.suidifu.matryoshka.service.RepurchaseSnapshotService;
import com.suidifu.matryoshka.snapshot.RepurchaseDocSnapshot;

/**
 * Created by hwr on 17-7-5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={

        "classpath:/local/applicationContext-*.xml",
})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class RepurchaseSnapshotServiceTest extends AbstractTransactionalJUnit4SpringContextTests{
    @Autowired
    private RepurchaseSnapshotService repurchaseService;

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    @Sql("classpath:test/yunxin/repaymentplan/test4getEffectiveRepurchaseDocSnapshotBy.sql")
    public void test_getEffectiveRepurchaseDocSnapshotBy(){

        sessionFactory.getCurrentSession().clear();
        Long contractId = 1L;
        RepurchaseDocSnapshot repurchaseDocSnapshot = repurchaseService.getEffectiveRepurchaseDocSnapshotBy(contractId);
        Assert.assertEquals(repurchaseDocSnapshot.getAmount(),new BigDecimal("2502.00"));
        Assert.assertEquals(repurchaseDocSnapshot.getCustomerUuid(), "57d2f333-de15-4ded-8700-f3fcae0e946c");
    }
}
