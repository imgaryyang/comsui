package com.roav.yunxin.test;

import com.demo2do.core.persistence.GenericJdbcSupport;
import com.suidifu.microservice.ReconciliationOrderActiveVoucher;
import com.suidifu.microservice.service.JournalVoucherService;
import com.suidifu.owlman.microservice.handler.RepaymentOrderActiveVoucherReconciliation;
import com.zufangbao.sun.ledgerbook.LedgerItemService;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.service.*;
import com.zufangbao.sun.yunxin.handler.UpdateAssetStatusLedgerBookHandler;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import lombok.extern.log4j.Log4j2;
import org.hibernate.SessionFactory;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

/**
 * Created by MieLongJun on 18-3-6.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,classes = {TestBase.class, ReconciliationOrderActiveVoucher.class})
@Log4j2
@Transactional
@Rollback(false)
public class TestBase {
    @Autowired
    protected JournalVoucherService journalVoucherService;
    @Autowired
    protected LedgerItemService ledgerItemService;
    @Autowired
    protected OrderService orderService;
    @Autowired
    protected VirtualAccountService virtualAccountService;
    @Autowired
    protected VirtualAccountFlowService virtualAccountFlowService;
    @Autowired
    protected LedgerBookStatHandler ledgerBookStatHandler;
    @Autowired
    protected RepaymentOrderItemService repaymentOrderItemService;
    @Autowired
    protected RepaymentOrderActiveVoucherReconciliation repaymentOrderActiveVoucherReconciliation;
    @Autowired
    protected RepaymentOrderService repaymentOrderService;
    @Autowired
    protected SessionFactory sessionFactory;
    @Autowired
    protected GenericJdbcSupport genericJdbcSupport;
    @Autowired
    protected UpdateAssetStatusLedgerBookHandler updateAssetStatusLedgerBookHandler;
    @Autowired
    protected RepaymentPlanService repaymentPlanService;
    @Autowired
    protected ContractService contractService;
//    @Test
//    public void test() {
//        int i = genericJdbcSupport.queryForInt("select count(*) from aaa_test_table ");
//
//        log.info(i+"ssssxxxx");
//    }
}
