package com.suidifu.microservice.service.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.suidifu.microservice.SourceDocumentReconciliationApplicationTests;
import com.suidifu.microservice.entity.JournalVoucher;
import com.suidifu.microservice.service.JournalVoucherService;
import lombok.extern.log4j.Log4j2;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/3/6 <br>
 * @time: 11:50 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SourceDocumentReconciliationApplicationTests.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Log4j2
@ActiveProfiles(value = "test")
public class JournalVoucherServiceImplTest {
    @Autowired
    @Qualifier("journalVoucherService")
    private JournalVoucherService journalVoucherService;

    private String sourceDocumentUuid;
    private String sourceDocumentDetailUuid;
    private String billingPlanUuid;

    @Before
    public void setUp() {
        sourceDocumentUuid = "bdec8f59-2da7-4f7d-85c0-3d24251365c0";
        sourceDocumentDetailUuid = "d51a6292-3821-4f37-91d1-2c677e2e626b";
        billingPlanUuid = "4d135864-d080-4c06-bce9-b451b3e1fe50";
    }

    @After
    public void tearDown() {
        sourceDocumentUuid = null;
        sourceDocumentDetailUuid = null;
        billingPlanUuid = null;
    }

    @Test
    public void getJournalVoucherBySourceDocumentUuidAndType() {
        JournalVoucher journalVoucher = journalVoucherService.
                getJournalVoucherBySourceDocumentUuidAndType(
                        sourceDocumentUuid,
                        sourceDocumentDetailUuid,
                        billingPlanUuid);

        assertThat(journalVoucher, notNullValue());
        assertThat(journalVoucher.getId(), equalTo(116098L));
    }
}