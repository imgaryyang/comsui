package com.suidifu.microservice.service.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.suidifu.microservice.SourceDocumentReconciliationApplicationTests;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.service.SourceDocumentService;
import com.suidifu.owlman.microservice.enumation.SourceDocumentExcuteResult;
import com.suidifu.owlman.microservice.enumation.SourceDocumentExcuteStatus;
import java.util.List;
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
 * @time: 13:25 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SourceDocumentReconciliationApplicationTests.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Log4j2
@ActiveProfiles(value = "test")
public class SourceDocumentServiceImplTest {
    @Autowired
    @Qualifier("sourceDocumentService")
    private SourceDocumentService sourceDocumentService;

    private String sourceDocumentDetailUuid;
    private String outlierDocumentUuid;
    private String firstOutlierDocType;

    @Before
    public void setUp() {
        sourceDocumentDetailUuid = "df937f9856b9436baf915b1f873129a6";
        outlierDocumentUuid = "ed0cd216d03b4a889d023ac20a46880e";
        firstOutlierDocType = "batch_pay_record";
    }

    @After
    public void tearDown() {
        sourceDocumentDetailUuid = null;
        outlierDocumentUuid = null;
        firstOutlierDocType = null;
    }

    @Test
    public void getSourceDocumentBy() {
        SourceDocument sourceDocument = sourceDocumentService.
                getSourceDocumentBy(sourceDocumentDetailUuid);

        assertThat(sourceDocument, notNullValue());
        assertThat(sourceDocument.getId(), equalTo(1L));
    }

    @Test
    public void getSourceDocumentByOutlierDocumentUuid() {
        SourceDocument sourceDocument = sourceDocumentService.
                getSourceDocumentByOutlierDocumentUuid(outlierDocumentUuid,
                        firstOutlierDocType);

        assertThat(sourceDocument, notNullValue());
        assertThat(sourceDocument.getId(), equalTo(1L));
    }

    @Test
    public void getDepositSourceDocumentListConnectedBy() {
        List<SourceDocument> sourceDocumentList = sourceDocumentService.
                getDepositSourceDocumentListConnectedBy(SourceDocumentExcuteResult.UNSUCCESS,
                        SourceDocumentExcuteStatus.PREPARE);

        assertThat(sourceDocumentList, notNullValue());
        assertThat(sourceDocumentList.size(), equalTo(716));
    }
}