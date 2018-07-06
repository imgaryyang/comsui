package com.suidifu.microservice.service;

import static org.junit.Assert.assertEquals;

import com.suidifu.microservice.ConsumerTest;
import com.suidifu.microservice.entity.SourceDocument;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author louguanyang at 2018/3/6 20:32
 * @mail louguanyang@hzsuidifu.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConsumerTest.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Log4j2
@ActiveProfiles(value = "test")
public class SourceDocumentServiceTest {

  @Autowired
  private SourceDocumentService sourceDocumentService;

   @Test
  public void test_getPaymentSourceDocument_noParams() {
    SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentBy("");
    Assert.assertNull(sourceDocument);
  }

  @Test
  @Sql("classpath:test/sql/sourceDocument/testSourceDocument.sql")
  public void test_getPaymentSourceDocument_noResult() {
    SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentBy("wrongUuid");
    Assert.assertNull(sourceDocument);
  }

  @Test
  @Sql("classpath:test/sql/sourceDocument/testSourceDocument.sql")
  public void test_getPaymentSourceDocument() {
    String sourceDocumentUuid = "source_document_uuid_1";
    SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentBy(sourceDocumentUuid);
    assertEquals(sourceDocumentUuid, sourceDocument.getSourceDocumentUuid());
  }

}