package com.suidifu.microservice.handler.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.suidifu.microservice.RepaymentOrderReconciliationApplication;
import com.suidifu.microservice.handler.BusinessPaymentVoucherTaskHandler;
import com.suidifu.owlman.microservice.model.SourceDocumentDetailReconciliationParameters;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.service.LedgerBookService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
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
 * @time: 17:16 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RepaymentOrderReconciliationApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@Slf4j
public class BusinessPaymentVoucherTaskHandlerImplTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Resource
    private BusinessPaymentVoucherTaskHandler businessPaymentVoucherTaskHandler;

    @Resource
    private LedgerBookService ledgerBookService;

    private List<SourceDocumentDetailReconciliationParameters> detailReconciliationParameters;
    private SourceDocumentDetailReconciliationParameters element1;
    private SourceDocumentDetailReconciliationParameters element2;

    private String sourceDocumentDetailUuid;
    private String financialContractNo;
    private String ledgerBookNo;
    private Date cashFlowTransactionTime;
    private Boolean isDetailFile;
    private String sourceDocumentUuid;
    private String financialContractUuid;
    private boolean isRepaymentOrder;
    private BigDecimal detailAmount;

    @Before
    public void setUp() {
        detailReconciliationParameters = new ArrayList<>();
        sourceDocumentDetailUuid = "cbb69a99-6087-4c63-bf84-faad38aa4d6a";
    }

    @After
    public void tearDown() {
        detailReconciliationParameters = null;
        sourceDocumentDetailUuid = null;
    }

    @Test
    public void fetchVirtualAccountAndBusinessPaymentVoucherTransfer() throws JsonProcessingException {
        sourceDocumentUuid = "8d379149-2130-41ab-8451-7f8037233a38";
        financialContractUuid = "d2812bc5-5057-4a91-b3fd-9019506f0499";
        ledgerBookNo = "7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58";
        isRepaymentOrder = false;
        detailAmount = new BigDecimal("0.00");
        ledgerBookNo = "7dd4ee73-8dcf-4dbc-94ee-932ff9f48b58";
        LedgerBook ledgerBook = ledgerBookService.getBookByBookNo(ledgerBookNo);

        VirtualAccount virtualAccount = businessPaymentVoucherTaskHandler.
                fetchVirtualAccountAndBusinessPaymentVoucherTransfer(sourceDocumentUuid,
                        detailAmount, ledgerBook, financialContractUuid, isRepaymentOrder);

        OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        log.info("\njson is:\n{}\n", OBJECT_MAPPER.writeValueAsString(virtualAccount));
        assertThat(virtualAccount, notNullValue());
    }

    @Test
    public void unfreezeCapitalAmountOfVoucher() {
    }
}