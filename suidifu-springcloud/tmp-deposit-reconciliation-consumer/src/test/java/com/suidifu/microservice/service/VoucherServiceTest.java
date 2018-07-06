package com.suidifu.microservice.service;

import com.suidifu.microservice.ConsumerTest;
import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.entity.Voucher;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author louguanyang at 2018/3/6 16:46
 * @mail louguanyang@hzsuidifu.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConsumerTest.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Log4j2
@ActiveProfiles(value = "test")
public class VoucherServiceTest {

  @Resource
  private VoucherService voucherService;

  @Test
  public void get_voucher_by_sourceDocument_null() {
    Voucher voucher = voucherService.get_voucher_by_sourceDocument(null);
    Assert.assertNull(voucher);
  }

  @Test
  public void get_voucher_by_sourceDocument_null_2() {
    SourceDocument sourceDocument = new SourceDocument();
    sourceDocument.setSourceDocumentUuid(null);
    Voucher voucher = voucherService.get_voucher_by_sourceDocument(sourceDocument);
    Assert.assertNull(voucher);
  }

  @Test
  public void get_voucher_by_sourceDocument_null_3() {
    SourceDocument sourceDocument = new SourceDocument();
    sourceDocument.setOutlierSerialGlobalIdentity(null);
    Voucher voucher = voucherService.get_voucher_by_sourceDocument(sourceDocument);
    Assert.assertNull(voucher);
  }

  @Test
  @Sql("classpath:test/sql/voucher/get_voucher_by_sourceDocument.sql")
  public void get_voucher_by_sourceDocument() {
    SourceDocument sourceDocument = new SourceDocument();
    sourceDocument.setSourceDocumentUuid("13117319-6f70-4e58-9c86-f3ec2d8d180b");
    sourceDocument.setOutlierSerialGlobalIdentity("2342423234234");
    sourceDocument.setVoucherUuid("19255f56-afd3-11e6-bb75-af34f32af758");
    Voucher voucher = voucherService.get_voucher_by_sourceDocument(sourceDocument);
    Assert.assertNotNull(voucher);
    Assert.assertEquals(2L, voucher.getId());
    Assert.assertEquals("19255f56-afd3-11e6-bb75-af34f32af758", voucher.getUuid());
  }

  @Test
  @Sql("classpath:test/sql/voucher/get_voucher_by_sourceDocument_only_invaild.sql")
  public void get_voucher_by_sourceDocument_only_invaild() {
    SourceDocument sourceDocument = new SourceDocument();
    sourceDocument.setSourceDocumentUuid("13117319-6f70-4e58-9c86-f3ec2d8d180b");
    sourceDocument.setOutlierSerialGlobalIdentity("2342423234234");
    Voucher voucher = voucherService.get_voucher_by_sourceDocument(sourceDocument);
    Assert.assertNull(voucher);
  }

  @Test
  @Sql("classpath:test/sql/voucher/get_voucher_by_sourceDocument_has_invaild.sql")
  public void get_voucher_by_sourceDocument_has_invaild() {
    SourceDocument sourceDocument = new SourceDocument();
    sourceDocument.setSourceDocumentUuid("13117319-6f70-4e58-9c86-f3ec2d8d180b");
    sourceDocument.setOutlierSerialGlobalIdentity("2342423234234");
    sourceDocument.setVoucherUuid("19255f56-afd3-11e6-bb75-af34f32af758");
    Voucher voucher = voucherService.get_voucher_by_sourceDocument(sourceDocument);
    Assert.assertNotNull(voucher);
    Assert.assertEquals(2L, voucher.getId());
    Assert.assertEquals("19255f56-afd3-11e6-bb75-af34f32af758", voucher.getUuid());
  }

}