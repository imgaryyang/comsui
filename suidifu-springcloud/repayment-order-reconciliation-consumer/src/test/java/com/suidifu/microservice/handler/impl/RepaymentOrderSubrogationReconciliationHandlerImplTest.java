package com.suidifu.microservice.handler.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.suidifu.microservice.RepaymentOrderReconciliationApplication;
import com.suidifu.microservice.handler.CashFlowHandler;
import com.suidifu.owlman.microservice.handler.RepaymentOrderReconciliationHandler;
import com.zufangbao.sun.entity.account.AuditStatus;
import com.zufangbao.sun.entity.contract.ContractRepaymentOrderItemMapper;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.PaymentOrderService;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.yunxin.entity.model.RepaymentOrderReconciliationParameters;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/3/14 <br>
 * @time: 19:30 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RepaymentOrderReconciliationApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@Slf4j
public class RepaymentOrderSubrogationReconciliationHandlerImplTest {
    @Resource
    private RepaymentOrderReconciliationHandler repaymentOrderReconciliation;
    @Resource
    private CashFlowHandler cashFlowHandler;

    @Resource
    private FinancialContractService financialContractService;
    @Resource
    private RepaymentOrderItemService repaymentOrderItemService;
    @Resource
    private RepaymentOrderService repaymentOrderService;
    @Resource
    private PaymentOrderService paymentOrderService;
    @Resource
    private CashFlowService cashFlowService;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void fetchVirtualAccountAndBusinessPaymentVoucherTransfer() {
    }

    @Test
    public void repaymentOrderRecoverDetails() {
    }

    @Test
    public void unfreezeCapital() {
    }

    @Test
    public void testAll() {
        List<RepaymentOrder> needWriteOffRepaymentOrderList = repaymentOrderService.getAllNeedRecoverRepaymentOrders();

        if (CollectionUtils.isEmpty(needWriteOffRepaymentOrderList)) {
            log.info("needWriteOffRepaymentOrderList is empty");
            return;
        }

        for (RepaymentOrder repaymentOrder : needWriteOffRepaymentOrderList) {
            if (null == repaymentOrder) {
                continue;
            }

            log.info("repaymentOrder.getOrderUuid() is {}", repaymentOrder.getOrderUuid());
            List<String> cashFlowUuidList = paymentOrderService.
                    getPaymentOrderListByOrderUuid(repaymentOrder.getOrderUuid());

            for (String cashFlowUuid : cashFlowUuidList) {
                log.info("cashFlowUuid is {}", cashFlowUuid);
                CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
                if (cashFlow.getAuditStatus() != AuditStatus.ISSUED)
                    break;
            }

            String financialContractNo = repaymentOrder.getFinancialContractNo();
            log.info("financialContractNo is {}", financialContractNo);
            FinancialContract financialContract = financialContractService.
                    getFinancialContractBy(repaymentOrder.getFinancialContractUuid());
            if (financialContract == null) {
                continue;
            }
            String cashIdentity = cashFlowHandler.getUniqueCashIdentity(repaymentOrder.getOrderUuid());
            String ledgerBookNo = financialContract.getLedgerBookNo();
            log.info("cashIdentity is {}", cashIdentity);
            log.info("ledgerBookNo is {}", ledgerBookNo);
            log.info("repaymentOrder.getOrderUuid() is {}", repaymentOrder.getOrderUuid());

            //1th
            List<RepaymentOrderReconciliationParameters> dataList = new ArrayList<>();
            RepaymentOrderReconciliationParameters data = new RepaymentOrderReconciliationParameters(
                    repaymentOrder.getOrderUuid(), "",
                    financialContractNo, ledgerBookNo, null);
            dataList.add(data);
            boolean flag = repaymentOrderReconciliation.fetchVirtualAccountAndBusinessPaymentVoucherTransfer(dataList);
            log.info("flag is:{}", flag);
            assertThat(flag, is(true));

            //2th
            List<ContractRepaymentOrderItemMapper> detailUUID = repaymentOrderItemService.
                    getCriticalMarkerByOrderUuidPeriodASC(repaymentOrder.getOrderUuid());
            List<RepaymentOrderReconciliationParameters> rawDataList = detailUUID.stream().
                    map(detailUuid -> new RepaymentOrderReconciliationParameters(repaymentOrder.getOrderUuid(),
                            detailUuid.getRepaymentOrderItemUuid(), financialContractNo,
                            ledgerBookNo, cashIdentity)).collect(Collectors.toList());
            flag = repaymentOrderReconciliation.repaymentOrderRecoverDetails(rawDataList);
            log.info("flag is:{}", flag);
            assertThat(flag, is(true));

            //3th
            rawDataList = new ArrayList<>();
            rawDataList.add(new RepaymentOrderReconciliationParameters(repaymentOrder.getOrderUuid(),
                    "", financialContract.getContractNo(),
                    financialContract.getLedgerBookNo(), null));
            flag = repaymentOrderReconciliation.unfreezeCapital(rawDataList);
            log.info("flag is:{}", flag);
            assertThat(flag, is(true));
        }
    }
}