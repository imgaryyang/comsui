package com.suidifu.microservice.service;

import static org.junit.Assert.assertEquals;

import com.suidifu.microservice.ConsumerTest;
import com.zufangbao.sun.entity.contract.ContractSourceDocumentDetailMapper;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author louguanyang at 2018/3/6 20:44
 * @mail louguanyang@hzsuidifu.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConsumerTest.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Log4j2
@ActiveProfiles(value = "test")
public class SourceDocumentDetailServiceTest {

  @Resource
  private SourceDocumentDetailService sourceDocumentDetailService;

  @Test
  @Sql("classpath:test/sql/voucher/testGetContractUuidAndSourceDocumentDetailUuidListBy.sql")
  public void testGetContractUuidSourceDocumentDetailUuidMapperBy() {

    List<String> sourceDocumentDetailUuidList = new ArrayList<>();

    sourceDocumentDetailUuidList.add("06016b5f-0cb6-40b0-9242-0ccec7441a62");
    sourceDocumentDetailUuidList.add("eff22cef-2b28-4548-93b9-9e7ecd5b097f");
    sourceDocumentDetailUuidList.add("225d4e01-bf37-4d0a-8c33-ddaa5c28f062");
    sourceDocumentDetailUuidList.add("source_document_detail_for_no_unique_id");

    List<ContractSourceDocumentDetailMapper> contractUuidSourceDocumentUuidMappers = sourceDocumentDetailService
        .getContractUuidSourceDocumentDetailUuidMapper(sourceDocumentDetailUuidList);

    assertEquals(4, contractUuidSourceDocumentUuidMappers.size());

    int sucCounter = 0;

    for (ContractSourceDocumentDetailMapper map : contractUuidSourceDocumentUuidMappers) {

      String contractUuidKey = map.getContractUuid();
      String detailUuidKey = map.getSourceDocumentDetailUuid();
      if ("06016b5f-0cb6-40b0-9242-0ccec7441a62".equals(detailUuidKey)) {
        assertEquals("1212", contractUuidKey);
        sucCounter++;
      }
      if ("eff22cef-2b28-4548-93b9-9e7ecd5b097f".equals(detailUuidKey)) {
        assertEquals("d2812bc5-5057-4a91-b3fd-9019506f0499", contractUuidKey);
        sucCounter++;
      }
      if ("225d4e01-bf37-4d0a-8c33-ddaa5c28f062".equals(detailUuidKey)) {
        assertEquals("1217", contractUuidKey);
        sucCounter++;
      }
      if ("source_document_detail_for_no_unique_id".equals(detailUuidKey)) {
        assertEquals("d2812bc5-5057-4a91-b3fd-9019506f0499", contractUuidKey);
        sucCounter++;
      }

    }
    assertEquals(4, sucCounter);

    sourceDocumentDetailUuidList.clear();

    contractUuidSourceDocumentUuidMappers = sourceDocumentDetailService
        .getContractUuidSourceDocumentDetailUuidMapper(sourceDocumentDetailUuidList);

    assertEquals(0, contractUuidSourceDocumentUuidMappers.size());

    contractUuidSourceDocumentUuidMappers = sourceDocumentDetailService
        .getContractUuidSourceDocumentDetailUuidMapper(null);

    assertEquals(0, contractUuidSourceDocumentUuidMappers.size());

  }

}