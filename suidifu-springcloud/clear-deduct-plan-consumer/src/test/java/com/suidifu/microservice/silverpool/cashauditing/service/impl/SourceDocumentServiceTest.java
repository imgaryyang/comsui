package com.suidifu.microservice.silverpool.cashauditing.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import com.suidifu.microservice.entity.SourceDocument;
import com.suidifu.microservice.service.SourceDocumentService;
import com.zufangbao.sun.yunxin.entity.SourceDocumentStatus;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SourceDocumentServiceTest extends AbstractTransactionalJUnit4SpringContextTests{
	@Autowired
	private SourceDocumentService sourceDocumentService;
	
	@Test
	@Sql("classpath:test/yunxin/test_getSourceDocumentByCashFlow.sql")
	public void test_getSourceDocumentByCashFlow() {
		String cashFlowUuid = "15c0350e-ff54-11e6-bf99-00163e002839";
		String firstOutlierDocType = SourceDocument.FIRSTOUTLIER_REMITTANCE;
		SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentByCashFlow(cashFlowUuid,firstOutlierDocType);
		assertNotNull(sourceDocument);
		assertEquals(cashFlowUuid, sourceDocument.getOutlierDocumentUuid());
		assertEquals(firstOutlierDocType, sourceDocument.getFirstOutlierDocType());
		assertEquals(SourceDocumentStatus.SIGNED, sourceDocument.getSourceDocumentStatus());
		
		cashFlowUuid = "25c0350e-ff54-11e6-bf99-00163e002839";
		firstOutlierDocType = SourceDocument.FIRSTOUTLIER_REMITTANCE;
		sourceDocument = sourceDocumentService.getSourceDocumentByCashFlow(cashFlowUuid,firstOutlierDocType);
		assertNull(sourceDocument);
		
		cashFlowUuid = "";
		firstOutlierDocType = SourceDocument.FIRSTOUTLIER_REMITTANCE;
		sourceDocument = sourceDocumentService.getSourceDocumentByCashFlow(cashFlowUuid,firstOutlierDocType);
		assertNull(sourceDocument);
		
		cashFlowUuid = "25c0350e-ff54-11e6-bf99-00163e002839";
		firstOutlierDocType = "";
		sourceDocument = sourceDocumentService.getSourceDocumentByCashFlow(cashFlowUuid,firstOutlierDocType);
		assertNull(sourceDocument);
		
	}
	
}
