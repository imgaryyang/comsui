package com.suidifu.microservice.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.suidifu.microservice.ConsumerTest;
import com.zufangbao.sun.entity.account.DepositeAccountInfo;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.ledgerbook.BankAccountCache;
import com.zufangbao.sun.ledgerbook.LedgerBookHandler;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.yunxin.entity.model.api.ExtraChargeSpec;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.MapUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

/**
 * @author louguanyang at 2018/3/7 14:44
 * @mail louguanyang@hzsuidifu.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConsumerTest.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Log4j2
@ActiveProfiles(value = "test")
@Transactional
public class BusinessPaymentVoucherTaskHandlerTest {

  @Autowired
  private BusinessPaymentVoucherTaskHandler businessPaymentVoucherTaskHandler;
  @Autowired
  private FinancialContractService financialContractService;
  @Autowired
  private BankAccountCache bankAccountCache;
  @Autowired
  private LedgerBookHandler ledgerBookHandler;

  /**
   * 回购有明细 足额 true
   */
  @Test
  @Sql("classpath:test/sql/thirdPart/business_payment_voucher_task_handler_ww01.sql")
  public void testIsDetailValid() {

    String detailUuid = "source_document_detail_uuid";
    String financialContractNo = "G31700";
    String ledgerBookNo = "ledger_book_no";
    try {

      boolean b = businessPaymentVoucherTaskHandler.isDetailValid(detailUuid, financialContractNo, ledgerBookNo,
          null);
      assertTrue(b);
    } catch (RuntimeException e) {
      assertTrue(false);
    }

  }

  /**
   * 回购有明细 不足额 true
   */
  @Test
  @Sql("classpath:test/sql/thirdPart/business_payment_voucher_task_handler_ww2.sql")
  public void testIsDetailValid2() {

    String detailUuid = "source_document_detail_uuid";
    String financialContractNo = "G31700";
    String ledgerBookNo = "ledger_book_no";
    try {

      boolean b = businessPaymentVoucherTaskHandler.isDetailValid(detailUuid, financialContractNo, ledgerBookNo,
          null);
      assertTrue(b);
    } catch (RuntimeException e) {
      assertTrue(false);
    }

  }

  /**
   * 回购无明细 足额 true
   */
  @Test
  @Sql("classpath:test/sql/thirdPart/business_payment_voucher_task_handler_ww3.sql")
  public void testIsDetailValid3() {

    String detailUuid = "source_document_detail_uuid";
    String financialContractNo = "G31700";
    String ledgerBookNo = "ledger_book_no";
    try {

      boolean b = businessPaymentVoucherTaskHandler.isDetailValid(detailUuid, financialContractNo, ledgerBookNo,
          null);
      assertTrue(b);
    } catch (RuntimeException e) {
      assertTrue(false);
    }
  }

  /**
   * 回购无明细 不足额 false
   */
  @Test
  @Sql("classpath:test/sql/thirdPart/business_payment_voucher_task_handler_ww4.sql")
  public void testIsDetailValid4() {

    String detailUuid = "source_document_detail_uuid";
    String financialContractNo = "G31700";
    String ledgerBookNo = "ledger_book_no";
    try {

      boolean b = businessPaymentVoucherTaskHandler.isDetailValid(detailUuid, financialContractNo, ledgerBookNo,
          null);
      assertTrue(!b);
    } catch (RuntimeException e) {
      assertTrue(false);
    }
  }

  /**
   * 回购无明细金额未零 false
   */
  @Test
  @Sql("classpath:test/sql/thirdPart/business_payment_voucher_task_handler_ww5.sql")
  public void testIsDetailValid5() {

    String detailUuid = "source_document_detail_uuid";
    String financialContractNo = "G31700";
    String ledgerBookNo = "ledger_book_no";
    try {

      boolean b = businessPaymentVoucherTaskHandler.isDetailValid(detailUuid, financialContractNo, ledgerBookNo,
          null);
      assertTrue(!b);
    } catch (RuntimeException e) {
      assertTrue(false);
    }
  }

  /**
   * 回购有明细 足额 true
   */
  @Test
  @Sql("classpath:test/sql/thirdPart/business_payment_voucher_task_handler_ww6.sql")
  public void testIsDetailValid6() {

    Long contractId = new Long("1");
    String contractUuid = "contract_uuid";
    FinancialContract financialContract = financialContractService.load(FinancialContract.class, 1L);
    DepositeAccountInfo depositeAccountInfo = bankAccountCache.extractFirstBankAccountFrom(financialContract);
    BigDecimal amount1 = new BigDecimal("2100");
    BigDecimal amount2 = new BigDecimal("100");
    BigDecimal amount3 = new BigDecimal("200");
    BigDecimal amount4 = new BigDecimal("300");
    Map<String, BigDecimal> bookingAmountDetailTable = new HashMap<String, BigDecimal>() {
      {
        put(ExtraChargeSpec.REPURCHASE_PRINCIPAL, amount1);
        put(ExtraChargeSpec.REPURCHASE_INTEREST, amount2);
        put(ExtraChargeSpec.REPURCHASE_PENALTY, amount3);
        put(ExtraChargeSpec.REPURCHASE_OTHER_FEE, amount4);
      }
    };
    ledgerBookHandler.repurchase_order_write_off(contractUuid, "source_document_detail_uuid",
        "cd0f2929-b4a9-49b5-9712-652465346e74", "", depositeAccountInfo, bookingAmountDetailTable,
        new BigDecimal("2700"), new Date(), "cashFlowIdentity");
  }

  @Test
  public void test_repurchase_list_priority() {
    Map<Integer, String> map = ExtraChargeSpec.repurchase_list_priority;
    assertTrue(MapUtils.isNotEmpty(map));
    assertFalse(CollectionUtils.isEmpty(map.keySet()));
    Integer index = 0;
    for (Integer i : map.keySet()) {
      System.out.println(i + map.get(i));
      assertEquals(index, i);
      index++;
    }
  }
}