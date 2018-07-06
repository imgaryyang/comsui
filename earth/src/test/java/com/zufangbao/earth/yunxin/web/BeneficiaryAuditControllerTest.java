package com.zufangbao.earth.yunxin.web;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.util.MockUtils;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.account.AuditStatus;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.financial.PaymentChannelInformation;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.entity.order.RepaymentAuditStatus;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.PaymentChannelInformationService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.audit.AuditJob;
import com.zufangbao.sun.yunxin.entity.audit.AuditJobSource;
import com.zufangbao.sun.yunxin.entity.audit.AuditResultCode;
import com.zufangbao.sun.yunxin.entity.audit.BeneficiaryAuditResult;
import com.zufangbao.sun.yunxin.entity.audit.ClearingStatus;
import com.zufangbao.sun.yunxin.entity.audit.TotalReceivableBills;
import com.zufangbao.sun.yunxin.entity.barclays.ThirdPartyAuditBillStat;
import com.zufangbao.sun.yunxin.entity.model.CashFlowQueryModelV2;
import com.zufangbao.sun.yunxin.entity.model.audit.ClearingCashFlowModeV2;
import com.zufangbao.sun.yunxin.entity.model.audit.PaymentChannelAuditJobCreateModel;
import com.zufangbao.sun.yunxin.entity.model.audit.QueryBeneficiaryAuditModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.ClearingVoucherListQueryModel;
import com.zufangbao.sun.yunxin.service.audit.AuditJobService;
import com.zufangbao.sun.yunxin.service.audit.BeneficiaryAuditResultService;
import com.zufangbao.sun.yunxin.service.audit.ThirdPartyAuditBillStatService;
import com.zufangbao.sun.yunxin.service.audit.TotalReceivableBillsService;
import com.zufangbao.sun.yunxin.entity.audit.AuditJobAndCashFlowSingleSigns;
import com.zufangbao.sun.yunxin.entity.audit.ClearingVoucher;
import com.zufangbao.sun.yunxin.entity.audit.ClearingVoucherStatus;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.JournalVoucher;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.JournalVoucherStatus;
import com.zufangbao.sun.yunxin.enums.voucher.JournalVoucherType;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocument;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.CashFlowHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.TotalReceivableBillsHandler;
import com.zufangbao.sun.yunxin.service.ClearingVoucherService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.JournalVoucherService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentService;
import com.zufangbao.wellsfargo.yunxin.model.LocalQueryResult;
import com.zufangbao.wellsfargo.yunxin.model.OppositeQueryResult;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })
@Rollback(true)
@WebAppConfiguration(value="webapp")
public class BeneficiaryAuditControllerTest {

	@Autowired
	private MockUtils mockUtils;
	@Autowired
	private AuditJobService auditJobService;
	@Autowired
	private CashFlowService cashFlowService;
	@Autowired
	private TotalReceivableBillsService totalReceivableBillsService;
	@Autowired
	private CashFlowHandler cashFlowHandler;
	@Autowired
	private ClearingVoucherService clearingVoucherService;
	@Autowired
	private SourceDocumentService sourceDocumentService;
	@Autowired
	private PaymentChannelInformationService paymentChannelInformationService;
	@Autowired
	private DeductPlanService deductPlanService;
	@Autowired
	private ThirdPartyAuditBillStatService thirdPartyAuditBillStatService;
	@Autowired
	private TotalReceivableBillsHandler totalReceivableBillsHandler;
	@Autowired
	private BeneficiaryAuditResultService beneficiaryAuditResultService;
	@Autowired
	private JournalVoucherService journalVoucherService;
	@BeforeClass
	public static void init(){
		MockUtils.init();
	}

	@AfterClass
	public static void end(){
		MockUtils.end();
	}

	@Test
	@Sql("classpath:test/yunxin/check_transaction_record_test.sql")
	public void checkTransactionRecordTest(){

		String deductApplicationUuid = "bba79c41-6345-4fa9-ba52-e8623b1a4d5c";
		String deductUuid = "fe6aec82-3052-459e-86d1-341e60d2958c";

		String expectedRtnXml = "{\"code\":\"0\",\"data\":{\"queryResult\":[{\"businessResultMsg\":\"交易?成功\",\"businessStatus\":\"Success\",\"businessSuccessTime\":1493715785000,\"channelAccountName\":\"operator\",\"channelAccountNo\":\"001053110000001\",\"communicationLastSentTime\":1493716080000,\"destinationAccountName\":\"李杰\",\"destinationAccountNo\":\"6214855712106520\",\"paymentChannelUuid\":\"f1ccca57-7c80-4429-b226-8ad31a729609\",\"sourceMessageUuid\":\"\",\"tradeUuid\":\"mSwVofa6FdCraVOlRLb\",\"transactionAmount\":500.00,\"transactionUuid\":\"fe6aec82-3052-459e-86d1-341e60d2958c\"}]},\"message\":\"成功\"}";
		mockUtils.mockPostRequest("/tradeSchedule/queryStatus", expectedRtnXml);
		Result result = new Result();
		result.data("responsePacket",expectedRtnXml);
		result.data("responseHttpStatus", "" + 200);
		result = result.success();

		LocalQueryResult localQueryResult = new LocalQueryResult(result);
		boolean checkResult = localQueryResult.isSuccess();

		assertEquals(Boolean.FALSE, checkResult);
		assertEquals("核单失败！", localQueryResult.getErrorMsg());

	}

	@Test
	@Sql("classpath:test/yunxin/check_transaction_record_test.sql")
	public void checkTransactionRecordTest2(){

		String deductApplicationUuid = "bba79c41-6345-4fa9-ba52-e8623b1a4d5c";
		String deductUuid = "fe6aec82-3052-459e-86d1-341e60d2958c2";

		String expectedRtnXml = "{\"code\":\"0\",\"data\":{\"queryResult\":[]},\"message\":\"成功\"}";
		mockUtils.mockPostRequest("/tradeSchedule/queryStatus", expectedRtnXml);
		Result result = new Result();
		result.data("responsePacket",expectedRtnXml);
		result.data("responseHttpStatus", "" + 200);
		result = result.success();

		LocalQueryResult localQueryResult = new LocalQueryResult(result);
		boolean checkResult = localQueryResult.isSuccess();

		assertEquals(Boolean.FALSE, checkResult);
		assertEquals("扣款查询,返回报文解析错误！", localQueryResult.getErrorMsg());

	}

	@Test
	@Sql("classpath:test/yunxin/check_counter_transaction_record_test.sql")
	public void checkCountTransactionRecordTest(){

		String paymentChannelUuid = "f1ccca57-7c80-4429-b226-8ad31a729609";
		String tradeUuid = "NMqIqeFdVdWwCTTqh8V";

		String expectedRtnXml = "{\"code\":\"0\",\"data\":{\"queryResult\":{\"businessFailed\":false,\"businessResultMsg\":\"交易?成功\",\"businessSuccess\":true,\"businessSuccessTime\":1493881835000,\"commCode\":\"0\",\"processStatus\":\"SUCCESS\",\"requestStatus\":\"FINISH\"}},\"message\":\"成功\"}";
		mockUtils.mockPostRequest("/tradeSchedule/queryOppositeStatus", expectedRtnXml);
		Result result = new Result();
		result.data("responsePacket",expectedRtnXml);
		result.data("responseHttpStatus", "" + 200);
		result = result.success();

		OppositeQueryResult oppositeQueryResult = new OppositeQueryResult(result);
		boolean checkResult = oppositeQueryResult.isSuccess();

		assertEquals(Boolean.TRUE, checkResult);

	}

	@Test
	@Sql("classpath:test/yunxin/check_counter_transaction_record_test.sql")
	public void checkCountTransactionRecordTest2(){

		String paymentChannelUuid = "f1ccca57-7c80-4429-b226-8ad31a729609";
		String tradeUuid = "NMqIqeFdVdWwCTTqh8Vs";

		String expectedRtnXml = "{\"code\":\"0\",\"data\":{\"queryResult\":{\"businessFailed\":false,\"businessResultMsg\":\"无法查询到该交易，可以重发\",\"businessSuccess\":false,\"commCode\":\"0\",\"requestStatus\":\"NOTRECEIVE\"}},\"message\":\"成功\"}";
		mockUtils.mockPostRequest("/tradeSchedule/queryOppositeStatus", expectedRtnXml);
		Result result = new Result();
		result.data("responsePacket",expectedRtnXml);
		result.data("responseHttpStatus", "" + 200);
		result = result.success();

		OppositeQueryResult oppositeQueryResult = new OppositeQueryResult(result);
		boolean checkResult = oppositeQueryResult.isSuccess();

		assertEquals(Boolean.FALSE, checkResult);
		assertEquals("核单失败！", oppositeQueryResult.getErrorMsg());

	}

	@Test
	@Sql("classpath:test/yunxin/check_transaction_record_local_test.sql")
	public void checkTransactionRecordLocalTest(){

		String deductApplicationUuid = "bba79c41-6345-4fa9-ba52-e8623b1a4d5c";
		String deductUuid = "fe6aec82-3052-459e-86d1-341e60d29581";

		String expectedRtnXml = "{\"code\":\"0\",\"data\":{\"queryResult\":[{\"businessResultMsg\":\"交易?成功\",\"businessStatus\":\"Failed\",\"businessSuccessTime\":1493715785000,\"channelAccountName\":\"operator\",\"channelAccountNo\":\"001053110000001\",\"communicationLastSentTime\":1493716080000,\"destinationAccountName\":\"李杰\",\"destinationAccountNo\":\"6214855712106520\",\"paymentChannelUuid\":\"f1ccca57-7c80-4429-b226-8ad31a729609\",\"sourceMessageUuid\":\"\",\"tradeUuid\":\"mSwVofa6FdCraVOlRLb\",\"transactionAmount\":500.00,\"transactionUuid\":\"fe6aec82-3052-459e-86d1-341e60d2958c\"}]},\"message\":\"成功\"}";
		mockUtils.mockPostRequest("tradeSchedule/queryStatus", expectedRtnXml);
		Result result = new Result();
		result.data("responsePacket",expectedRtnXml);
		result.data("responseHttpStatus", "" + 200);
		result = result.success();


//		Result result = jpmorganApiHelper.queryTradeSchedulesStatus(deductUuid,null);
		LocalQueryResult localQueryResult = new LocalQueryResult(result);
		boolean checkResult = localQueryResult.isSuccess();

		assertEquals(Boolean.TRUE, checkResult);

	}

	//BeneficiaryAuditController "/clearingCashFlowQueryV2"
	@Test
	@Sql("classpath:test/yunxin/clearingVoucher/clearingCashFlowQueryV2.sql")
	public void clearingCashFlowQueryV2Test(){
		CashFlowQueryModelV2 cashFlowQueryModelV2 = new CashFlowQueryModelV2();
		cashFlowQueryModelV2.setCashFlowNo("999702020206");
		Page page = new Page();
		List<CashFlow> cashFlows= cashFlowService.getClearingCashFlowsByCashFlowQueryModelV2(cashFlowQueryModelV2, page);
		CashFlow cashFlow = cashFlows.get(0);
		assertEquals(cashFlows.size(), 1);
		assertEquals(cashFlow.getBankSequenceNo(), "999702020206");
		assertEquals(cashFlow.getHostAccountNo(), "95200155300001595");
		assertEquals(cashFlow.getCounterAccountNo(), "95130154900000571");
		assertEquals(cashFlow.getTransactionAmount(), new BigDecimal("121200.00"));
		assertEquals(cashFlow.getIssuedAmount(), new BigDecimal("0.00"));
		assertEquals(cashFlow.getAccountSide().getOrdinal(), 1);
	}

	//BeneficiaryAuditController "{auditJobUuidList}/reTouchingV2"
	@Test
	@Sql("classpath:test/yunxin/clearingVoucher/createSourceDocumentByOneCashFlows.sql")
	public void createSourceDocumentByOneCashFlowTest(){
		List<String> AuditJobUuidList = new ArrayList<>();
		AuditJobUuidList.add("b2e136c7-f595-4861-86af-f95fcf888d70");
		AuditJobUuidList.add("b8c4033f-28f5-47eb-b1ab-d5315e2ec0cf");
		AuditJobUuidList.add("2d056efa-1359-4dd0-a5fb-006f9c0802cc");
		List<ClearingCashFlowModeV2> cashFlowList = new ArrayList<>();
		ClearingCashFlowModeV2 cashFlowModel = new ClearingCashFlowModeV2();
		cashFlowModel.setCashFlowUuid("635e5285-e3c8-4df2-826b-b58dd2f201b5");
		cashFlowModel.setRelatedAmount(new BigDecimal("40000.00"));
		cashFlowList.add(cashFlowModel);
		cashFlowHandler.relatedClearingCashFlowV2(JsonUtils.toJsonString(AuditJobUuidList), JsonUtils.toJsonString(cashFlowList), "备注");


		ClearingVoucherListQueryModel clearingVoucherListQueryModel = new ClearingVoucherListQueryModel();
		clearingVoucherListQueryModel.setPaymentInstitution("[3]");
		Page page = new Page();
		List<ClearingVoucher> clearingVoucherList = clearingVoucherService.queryClearingVoucherListByQueryModel(clearingVoucherListQueryModel,page);
		assertEquals(clearingVoucherList.size(), 1);

		ClearingVoucher clearingVoucher = clearingVoucherList.get(0);
		assertEquals(clearingVoucher.getCashFlowUuid(), cashFlowModel.getCashFlowUuid());
		assertEquals(clearingVoucher.getVoucherAmount(), new BigDecimal("40000.00"));
		assertEquals(clearingVoucher.getSingleSigns(), AuditJobAndCashFlowSingleSigns.ONE_CASHFLOW);
		assertEquals(clearingVoucher.getSourceAccountSide(), AccountSide.CREDIT);
		assertEquals(clearingVoucher.getPaymentInstitution().getOrdinal(), 3);
		assertEquals(clearingVoucher.getMerchantNo(), "001053110000001");
		assertEquals(clearingVoucher.getPgClearingAccount(), "");
		assertEquals(clearingVoucher.getHostAccountNo(), "95200155300001595");
		assertEquals(clearingVoucher.getCounterAccountNo(), "95130154900000571");
		assertEquals(clearingVoucher.getClearingVoucherStatus(), ClearingVoucherStatus.DOING);


		List<SourceDocument> sourceDocuments = sourceDocumentService.getSourceDocumentListByCashFlowUuid("635e5285-e3c8-4df2-826b-b58dd2f201b5");
		assertEquals(sourceDocuments.size(), 1);
		SourceDocument sourceDocument = sourceDocuments.get(0);

		assertEquals(sourceDocument.getOutlierDocumentUuid(), cashFlowModel.getCashFlowUuid());
		assertEquals(sourceDocument.getFirstOutlierDocType(), SourceDocument.FIRSTOUTLIER_CLEARING);
		assertEquals(sourceDocument.getAuditStatus(), RepaymentAuditStatus.ISSUED);

		String BatchUuid = clearingVoucher.getBatchUuid();
		List<JournalVoucher> journalVouchers =journalVoucherService.getJournalVoucherListByBacthUuid(BatchUuid);
		assertEquals(journalVouchers.size(), 3);

		JournalVoucher journalVoucher1 = journalVouchers.get(0);
		assertEquals(journalVoucher1.getCashFlowUuid(), "635e5285-e3c8-4df2-826b-b58dd2f201b5");
		assertEquals(journalVoucher1.getBillingPlanUuid(), "ed055704-25c1-46fe-ab56-d733d9af0bb0");
		assertEquals(journalVoucher1.getSourceDocumentUuid(), sourceDocument.getUuid());
		assertEquals(journalVoucher1.getJournalVoucherType(), JournalVoucherType.TRANSFER_BILL_BY_CLEARING_CASH_FLOW);
		assertEquals(journalVoucher1.getStatus(), JournalVoucherStatus.VOUCHER_ISSUED);

		CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid("635e5285-e3c8-4df2-826b-b58dd2f201b5");
		assertEquals(cashFlow.getAuditStatus(), AuditStatus.ISSUED);
		assertEquals(cashFlow.getIssuedAmount(), new BigDecimal("40000.00"));

		AuditJob auditJob1 = auditJobService.getAuditJob("b2e136c7-f595-4861-86af-f95fcf888d70");
		assertEquals(auditJob1.getClearingStatus(), ClearingStatus.DOING);
		AuditJob auditJob2 = auditJobService.getAuditJob("2d056efa-1359-4dd0-a5fb-006f9c0802cc");
		assertEquals(auditJob2.getClearingStatus(), ClearingStatus.DOING);

		TotalReceivableBills totalReceivableBills = totalReceivableBillsService.getTotalReceivableBillsByUuid("ed055704-25c1-46fe-ab56-d733d9af0bb0");
		assertEquals(totalReceivableBills.getClearingCashFlowIdentity(),cashFlowModel.getCashFlowUuid());
		List<String> list = new ArrayList<>();
		list.add(cashFlowModel.getCashFlowUuid());
		assertEquals(totalReceivableBills.getClearingCashFlowIdentity(),cashFlowModel.getCashFlowUuid());
		assertEquals(totalReceivableBills.getClearingCashFlowIdentityLists(),list);
		assertEquals(DateUtils.format(totalReceivableBills.getCashFlowClearingTime(), DateUtils.LONG_DATE_FORMAT),"2017-09-07 10:31:19");

	}

	//BeneficiaryAuditController "{auditJobUuidList}/reTouchingV2"
	@Test
	@Sql("classpath:test/yunxin/clearingVoucher/createSourceDocumentByOneTotalReceivableBills.sql")
	public void createSourceDocumentByOneTotalReceivableBillsTest(){
		List<ClearingCashFlowModeV2> cashFlowList = new ArrayList<>();
		ClearingCashFlowModeV2 cashFlowModel1 = new ClearingCashFlowModeV2();
		cashFlowModel1.setCashFlowUuid("635e5285-e3c8-4df2-826b-b58dd2f201b5");
		cashFlowModel1.setRelatedAmount(new BigDecimal("40000.00"));
		ClearingCashFlowModeV2 cashFlowModel2 = new ClearingCashFlowModeV2();
		cashFlowModel2.setCashFlowUuid("8d81d7a4-f7b6-49a9-b2f9-bb50adae722b");
		cashFlowModel2.setRelatedAmount(new BigDecimal("40000.00"));
		ClearingCashFlowModeV2 cashFlowModel3 = new ClearingCashFlowModeV2();
		cashFlowModel3.setCashFlowUuid("9056aef3-15b6-436b-a9ef-d735ab04e267");
		cashFlowModel3.setRelatedAmount(new BigDecimal("20000.00"));
		cashFlowList.add(cashFlowModel1);cashFlowList.add(cashFlowModel2);cashFlowList.add(cashFlowModel3);
		List<String> AuditJobUuidList = new ArrayList<>();
		AuditJobUuidList.add("b2e136c7-f595-4861-86af-f95fcf888d70");
		cashFlowHandler.relatedClearingCashFlowV2(JsonUtils.toJsonString(AuditJobUuidList), JsonUtils.toJsonString(cashFlowList), "备注");

		ClearingVoucherListQueryModel clearingVoucherListQueryModel = new ClearingVoucherListQueryModel();
		clearingVoucherListQueryModel.setPaymentInstitution("[3]");
		Page page = new Page();
		List<ClearingVoucher> clearingVoucherList = clearingVoucherService.queryClearingVoucherListByQueryModel(clearingVoucherListQueryModel,page);
		assertEquals(clearingVoucherList.size(), 1);

		ClearingVoucher clearingVoucher = clearingVoucherList.get(0);

		assertEquals(clearingVoucher.getCashFlowUuid(), cashFlowModel1.getCashFlowUuid());
		assertEquals(clearingVoucher.getVoucherAmount(), new BigDecimal("100000.00"));
		assertEquals(clearingVoucher.getSingleSigns(), AuditJobAndCashFlowSingleSigns.ONE_AUDITJOB);
		assertEquals(clearingVoucher.getSourceAccountSide(), AccountSide.CREDIT);
		assertEquals(clearingVoucher.getPaymentInstitution().getOrdinal(), 3);
		assertEquals(clearingVoucher.getMerchantNo(), "001053110000001");
		assertEquals(clearingVoucher.getPgClearingAccount(), "");
		assertEquals(clearingVoucher.getHostAccountNo(), "95200155300001595");
		assertEquals(clearingVoucher.getCounterAccountNo(), "95130154900000571");
		assertEquals(clearingVoucher.getClearingVoucherStatus(), ClearingVoucherStatus.DOING);

		List<SourceDocument> sourceDocuments1 = sourceDocumentService.getSourceDocumentListByCashFlowUuid("635e5285-e3c8-4df2-826b-b58dd2f201b5");
		assertEquals(sourceDocuments1.size(), 1);
		SourceDocument sourceDocument1 = sourceDocuments1.get(0);

		assertEquals(sourceDocument1.getOutlierDocumentUuid(), cashFlowModel1.getCashFlowUuid());
		assertEquals(sourceDocument1.getFirstOutlierDocType(), SourceDocument.FIRSTOUTLIER_CLEARING);
		assertEquals(sourceDocument1.getOutlierAmount(), new BigDecimal("40000.00"));
		assertEquals(sourceDocument1.getAuditStatus(), RepaymentAuditStatus.ISSUED);

		List<SourceDocument> sourceDocuments2 = sourceDocumentService.getSourceDocumentListByCashFlowUuid("8d81d7a4-f7b6-49a9-b2f9-bb50adae722b");
		assertEquals(sourceDocuments2.size(), 1);
		SourceDocument sourceDocument2 = sourceDocuments2.get(0);

		assertEquals(sourceDocument2.getOutlierDocumentUuid(), cashFlowModel2.getCashFlowUuid());
		assertEquals(sourceDocument2.getFirstOutlierDocType(), SourceDocument.FIRSTOUTLIER_CLEARING);
		assertEquals(sourceDocument2.getOutlierAmount(), new BigDecimal("40000.00"));
		assertEquals(sourceDocument2.getAuditStatus(), RepaymentAuditStatus.ISSUED);
		List<SourceDocument> sourceDocuments3 = sourceDocumentService.getSourceDocumentListByCashFlowUuid("9056aef3-15b6-436b-a9ef-d735ab04e267");
		assertEquals(sourceDocuments3.size(), 1);
		SourceDocument sourceDocument3 = sourceDocuments3.get(0);

		assertEquals(sourceDocument3.getOutlierDocumentUuid(), cashFlowModel3.getCashFlowUuid());
		assertEquals(sourceDocument3.getFirstOutlierDocType(), SourceDocument.FIRSTOUTLIER_CLEARING);
		assertEquals(sourceDocument3.getOutlierAmount(), new BigDecimal("20000.00"));
		assertEquals(sourceDocument3.getAuditStatus(), RepaymentAuditStatus.ISSUED);

		String BatchUuid = clearingVoucher.getBatchUuid();
		List<JournalVoucher> journalVouchers =journalVoucherService.getJournalVoucherListByBacthUuid(BatchUuid);
		assertEquals(journalVouchers.size(), 3);

		JournalVoucher journalVoucher1 = journalVouchers.get(0);
		assertEquals(journalVoucher1.getCashFlowUuid(), "635e5285-e3c8-4df2-826b-b58dd2f201b5");
		assertEquals(journalVoucher1.getBillingPlanUuid(), "ed055704-25c1-46fe-ab56-d733d9af0bb0");
		assertEquals(journalVoucher1.getSourceDocumentUuid(), sourceDocument1.getUuid());
		assertEquals(journalVoucher1.getJournalVoucherType(), JournalVoucherType.TRANSFER_BILL_BY_CLEARING_CASH_FLOW);
		assertEquals(journalVoucher1.getStatus(), JournalVoucherStatus.VOUCHER_ISSUED);

		CashFlow cashFlow1 = cashFlowService.getCashFlowByCashFlowUuid("635e5285-e3c8-4df2-826b-b58dd2f201b5");
		assertEquals(cashFlow1.getAuditStatus(), AuditStatus.ISSUED);
		assertEquals(cashFlow1.getIssuedAmount(), new BigDecimal("40000.00"));

		CashFlow cashFlow2 = cashFlowService.getCashFlowByCashFlowUuid("8d81d7a4-f7b6-49a9-b2f9-bb50adae722b");
		assertEquals(cashFlow2.getAuditStatus(), AuditStatus.ISSUED);
		assertEquals(cashFlow2.getIssuedAmount(), new BigDecimal("50000.00"));

		CashFlow cashFlow3 = cashFlowService.getCashFlowByCashFlowUuid("9056aef3-15b6-436b-a9ef-d735ab04e267");
		assertEquals(cashFlow3.getAuditStatus(), AuditStatus.ISSUED);
		assertEquals(cashFlow3.getIssuedAmount(), new BigDecimal("20000.00"));

		AuditJob auditJob1 = auditJobService.getAuditJob("b2e136c7-f595-4861-86af-f95fcf888d70");
		assertEquals(auditJob1.getClearingStatus(), ClearingStatus.DOING);

		TotalReceivableBills totalReceivableBills = totalReceivableBillsService.getTotalReceivableBillsByUuid("ed055704-25c1-46fe-ab56-d733d9af0bb0");
		assertEquals(totalReceivableBills.getClearingCashFlowIdentity(),cashFlowModel1.getCashFlowUuid());
		List<String> list = new ArrayList<>();
		list.add(cashFlowModel1.getCashFlowUuid());
		list.add(cashFlowModel2.getCashFlowUuid());
		list.add(cashFlowModel3.getCashFlowUuid());
		assertEquals(totalReceivableBills.getClearingCashFlowIdentity(),cashFlowModel1.getCashFlowUuid());
		assertEquals(totalReceivableBills.getClearingCashFlowIdentityLists(),list);
		assertEquals(DateUtils.format(totalReceivableBills.getCashFlowClearingTime(), DateUtils.LONG_DATE_FORMAT),"2017-09-07 10:31:19");


	}

	//BeneficiaryAuditController "/query"
//	@Test
//	@Sql("classpath:test/yunxin/clearingVoucher/query.sql")
//	public void queryTest(){
//		List<String> strings = new ArrayList<>();
//		strings.add("2");
//		QueryBeneficiaryAuditModel queryModel= new QueryBeneficiaryAuditModel("[1]","2017-11-23 00:00:00","2017-11-23 23:59:59","[2]","[2]","1",null,"001053110000001",true);
//		Page page = new Page();
//		List<AuditJob> auditJobs = auditJobService.query(queryModel, page);
//		assertEquals(auditJobs.size(), 1);
//		AuditJob auditJob = auditJobs.get(0);
//		assertEquals(auditJob.getMerchantNo(), "001053110000001");
//		assertEquals(auditJob.getPgClearingAccount(), "");
//		assertEquals(auditJob.getPaymentInstitution().getOrdinal(), 1);
//	}

	@Test
	@Sql("classpath:test/yunxin/clearingVoucher/createBeneficiaryAudirJob.sql")
	public void createNewAuditJobTrans(){
		PaymentChannelAuditJobCreateModel createModel = new PaymentChannelAuditJobCreateModel();
		createModel.setAccountSide(1);
		createModel.setEndTime("2017-03-23 23:59:59");
		createModel.setPaymentInstitutionName(0);
		createModel.setPgAccount("001053110000001");
		createModel.setPgClearingAccount("");
		createModel.setStartTime("2017-03-23 00:00:00");
		List<PaymentChannelInformation> paymentChannelInformationlist = paymentChannelInformationService.getPaymentChannelInformationBy(createModel.getPaymentInstitutionNameEnum(), createModel.getPgAccount(), createModel.getPgClearingAccount());
		AccountSide accountSide = createModel.getAccountSideEnum();
      AuditJob auditJob = auditJobService.createAuditJob(paymentChannelInformationlist.get(0), createModel.getStartTime_date(), createModel.getEndTime_date(), AuditJobSource.MANUAL,accountSide);
      assertEquals(true,auditJobService.existsRemittanceAuditJob(PaymentInstitutionName.UNIONPAYGZ, createModel.getPgAccount(), null, createModel.getStartTime_date(), createModel.getEndTime_date(), AuditJobSource.MANUAL));
	}

//	@Test
//	@Sql("classpath:test/yunxin/clearingVoucher/showBasicInfoTrans.sql")
//	public void showBasicInfoTrans(){
//		AuditJob auditJob = auditJobService.getAuditJob("audit_job_uuid_1");
//		TotalReceivableBills totalReceivableBills = totalReceivableBillsService.queryTotalReceivableBillsByAuditJob(auditJob);
//		BigDecimal countDeductPlanAmount = deductPlanService.countDeductPlanAmount(auditJob, ClearingStatus.DONE);
//
//		int detailNum = 0; //收入明细
//		BigDecimal detailAmount = BigDecimal.ZERO; //收入金额
//		List<ThirdPartyAuditBillStat> thirdPartyAuditBillStatList = thirdPartyAuditBillStatService.queryThirdPartyAuditBillStat(auditJob.getPaymentInstitution(), auditJob.getMerchantNo(), auditJob.getPgClearingAccount(), auditJob.getStartTime(), auditJob.getEndTime(), null);
//		if(!CollectionUtils.isEmpty(thirdPartyAuditBillStatList)){
//			for (ThirdPartyAuditBillStat thirdPartyAuditBillStat : thirdPartyAuditBillStatList) {
//				detailNum = detailNum+thirdPartyAuditBillStat.getSumCount();
//				detailAmount = thirdPartyAuditBillStat.getSumAmount().add(detailAmount);
//			}
//		}
//		assertEquals(0,new BigDecimal("0").compareTo(countDeductPlanAmount));
//		assertEquals(2,detailNum);
//		assertEquals(0,new BigDecimal("20").compareTo(detailAmount));
//	}

	@Test
	@Sql("classpath:test/yunxin/clearingVoucher/showBasicInfoTrans.sql")
	public void queryClearingVoucher(){
		TotalReceivableBills totalReceivableBills = totalReceivableBillsService.getTotalReceivableBillsByUuid("ed055704-25c1-46fe-ab56-d733d9af0bb0");
		ClearingVoucher clearingVoucher = totalReceivableBillsHandler.queryClearingVoucherByTotalReceivableBills(totalReceivableBills, ClearingVoucherStatus.DONE);
		assertEquals("001053110000001",clearingVoucher.getMerchantNo());
		assertEquals(3, clearingVoucher.getPaymentInstitution().ordinal());
		assertEquals("", clearingVoucher.getPgClearingAccount());
		assertEquals("52bd00cd-6633-4e35-8a06-7c7eabfe7522", clearingVoucher.getBatchUuid());
	}

//	@Test
//	@Sql("classpath:test/yunxin/clearingVoucher/showBasicInfoTrans.sql")
//	public void queryLocalOrIssuedAuditResult(){
//		AuditJob auditJob = auditJobService.getAuditJob("audit_job_uuid_1");
//        AuditResultCode auditResultCode = EnumUtil.fromCode(AuditResultCode.class, 1);
//        int count = 0;
//        String pgClearingAccount = auditJob.getPgClearingAccount();
//        Date startTime = auditJob.getStartTime();
//        Date endTime = auditJob.getEndTime();
//
//        PaymentInstitutionName paymentGateway = auditJob.getPaymentInstitution();
//        String merchantNo = auditJob.getMerchantNo();
//
//        List<BeneficiaryAuditResult> remittanceAuditResultList = null;
//		remittanceAuditResultList = beneficiaryAuditResultService.getLocalOrIssuedAuditResult(paymentGateway, startTime, endTime, auditResultCode, new Page(1,12));
//	    count = beneficiaryAuditResultService.countLocalOrIssueAuditResult(paymentGateway, merchantNo, pgClearingAccount, startTime, endTime, auditResultCode);
//	    assertEquals(1, count);
//	    assertEquals(1,remittanceAuditResultList.size());
//	}
	
	@Test
	@Sql("classpath:test/yunxin/clearingVoucher/showBasicInfoTrans.sql")
	public void queryCounterAuditResult(){
		AuditJob auditJob = auditJobService.getAuditJob("audit_job_uuid_1");
        AuditResultCode auditResultCode = EnumUtil.fromCode(AuditResultCode.class, 3);
        int count = 0;
        String pgClearingAccount = auditJob.getPgClearingAccount();
        Date startTime = auditJob.getStartTime();
        Date endTime = auditJob.getEndTime();

        PaymentInstitutionName paymentGateway = auditJob.getPaymentInstitution();
        String merchantNo = auditJob.getMerchantNo();

        List<BeneficiaryAuditResult> remittanceAuditResultList = null;
        remittanceAuditResultList = beneficiaryAuditResultService.getCounterAuditResult(paymentGateway,merchantNo, pgClearingAccount,  startTime, endTime,auditResultCode, new Page(1,12));
        count = beneficiaryAuditResultService.countCounterAuditResult(paymentGateway ,merchantNo, pgClearingAccount, startTime, endTime, auditResultCode, new Page(1,12));
        BeneficiaryAuditResult beneficiaryAuditResultByUuid = beneficiaryAuditResultService.getBeneficiaryAuditResultByUuid("4a3170af-c844-4164-8ca6-210bea6e3159");
	    assertEquals(1, count);
	    assertEquals(1,remittanceAuditResultList.size());
	    
	}

}
