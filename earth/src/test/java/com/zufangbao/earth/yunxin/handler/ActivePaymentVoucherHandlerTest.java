package com.zufangbao.earth.yunxin.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.earth.yunxin.api.handler.ActivePaymentVoucherHandler;
import com.zufangbao.earth.yunxin.api.model.command.ActivePaymentVoucherCommandModel;
import com.zufangbao.earth.yunxin.api.util.ApiMessageUtil;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.contract.ContractActiveSourceDocumentMapper;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.FileUtils;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.SourceDocumentStatus;
import com.zufangbao.sun.yunxin.entity.model.voucher.*;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.utils.FileUtils;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherCreateAccountInfoModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherCreateAssetInfoModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherCreateBaseModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherCreateModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherCreateSubmitModel;
import com.zufangbao.sun.yunxin.service.RepaymentPlanExtraChargeService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.*;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentDetailService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.VoucherService;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml" })
@Transactional
@Rollback(true)
public class ActivePaymentVoucherHandlerTest {
	
	@Resource
	ActivePaymentVoucherHandler activePaymentVoucherHandler;
	
	@Resource
	ContractService contractService;
	
	@Resource
	RepaymentPlanService repaymentPlanService;
	
	@Resource 
	 private VoucherService voucherService;
	
	@Resource
	private SourceDocumentService sourceDocumentService;
	
	@Resource
	private CashFlowService cashFlowService;

	@Resource
	private SourceDocumentDetailService sourceDocumentDetailService;

	@Resource
	private RepaymentPlanExtraChargeService repaymentPlanExtraChargeService;

	@Resource
	private FinancialContractService financialContractService;

	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/empty_database.sql")
	public void test_checkRequestNoAndSaveLog() {
		String requestNo = "requestNoTest";
		Integer transactionType = 0;
		Integer voucherType = 0;
		String uniqueId = "uniqueId1";
		String contractNo = "contractNo1";
		String repaymentPlanNo = "repaymentPlanNo1";
		String receivableAccountNo = "receivableAccountNo1";
		String paymentBank = "中国建设银行";
		String bankTransactionNo = "transactionNo1";
		BigDecimal voucherAmount = new BigDecimal(10000.00);
		String paymentName = "payment";
		String paymentAccountNo = "paymentAccountNo1";
		
		ActivePaymentVoucherCommandModel model = new ActivePaymentVoucherCommandModel();
		model.setRequestNo(requestNo);
		model.setTransactionType(transactionType);
		model.setVoucherType(voucherType);
		//model.setUniqueId(uniqueId);
		//model.setContractNo(contractNo);
		model.setReceivableAccountNo(receivableAccountNo);
		model.setPaymentBank(paymentBank);
		model.setBankTransactionNo(bankTransactionNo);
		model.setVoucherAmount(voucherAmount);
		model.setPaymentName(paymentName);
		model.setPaymentAccountNo(paymentAccountNo);
		
		String ip = "127.0.0.1";
		activePaymentVoucherHandler.checkRequestNoAndSaveLog(model, ip);
	}
	
	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/test_checkRequestNoAndSaveLog.sql")
	public void test_checkRequestNoAndSaveLog_repeatRequestNo() {
		String requestNo = "requestNoTest";
		Integer transactionType = 0;
		Integer voucherType = 0;
		String uniqueId = "uniqueId1";
		String contractNo = "contractNo1";
		String repaymentPlanNo = "repaymentPlanNo1";
		String receivableAccountNo = "receivableAccountNo1";
		String paymentBank = "中国建设银行";
		String bankTransactionNo = "transactionNo1";
		BigDecimal voucherAmount = new BigDecimal(10000.00);
		String paymentName = "payment";
		String paymentAccountNo = "paymentAccountNo1";
		
		ActivePaymentVoucherCommandModel model = new ActivePaymentVoucherCommandModel();
		model.setRequestNo(requestNo);
		model.setTransactionType(transactionType);
		model.setVoucherType(voucherType);
		//model.setUniqueId(uniqueId);
		//model.setContractNo(contractNo);
		model.setReceivableAccountNo(receivableAccountNo);
		model.setPaymentBank(paymentBank);
		model.setBankTransactionNo(bankTransactionNo);
		model.setVoucherAmount(voucherAmount);
		model.setPaymentName(paymentName);
		model.setPaymentAccountNo(paymentAccountNo);
		
		String ip = "127.0.0.1";
		try {
			activePaymentVoucherHandler.checkRequestNoAndSaveLog(model, ip);
			Assert.fail();
		} catch (ApiException e) {
			e.printStackTrace();
			Assert.assertEquals("请求编号重复!", ApiMessageUtil.getMessage(e.getCode()));
		}
	}
	
	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/test_submitActivePaymentVoucher.sql")
	public void test_submitActivePaymentVoucher_wrongReceivableAccount() throws IOException {
		
		ActivePaymentVoucherCommandModel model = new ActivePaymentVoucherCommandModel();
		String repaymentPlanNo = "[\"ZC275016985BF712EC\"]";
		String requestNo = "requestNoTest";
		Integer transactionType = 0;
		Integer voucherType = 0;
		String uniqueId = "uniqueId1";
		String contractNo = "contractNo1";
		String receivableAccountNo = "200010";//错误
		String paymentBank = "中国建设银行";
		String bankTransactionNo = "transactionNo1";
		BigDecimal voucherAmount = new BigDecimal(10000.00);
		String paymentName = "payment";
		String paymentAccountNo = "paymentAccountNo1";
		
		List<ActivePaymentVoucherDetail> details = new ArrayList<>();
		ActivePaymentVoucherDetail detail1 = new ActivePaymentVoucherDetail();
		detail1.setRepaymentPlanNo("ZC275016985BF712EF");
		detail1.setAmount(new BigDecimal("10000.00"));
		detail1.setPrincipal(new BigDecimal("10000.00"));
		detail1.setContractNo("云信信2016-241-DK(428522112675736882)");
		detail1.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3f");
		details.add(detail1);
		ActivePaymentVoucherDetail detail2 = new ActivePaymentVoucherDetail();
		detail2.setRepaymentPlanNo("ZC275016985BF712EG");
		detail2.setAmount(new BigDecimal("10000.00"));
		detail2.setPrincipal(new BigDecimal("10000.00"));
		detail2.setContractNo("云信信2016-241-DK(428522112675736882)");
		detail2.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3f");
		details.add(detail2);

		model.setRequestNo(requestNo);
		model.setTransactionType(transactionType);
		model.setVoucherType(voucherType);
		//model.setUniqueId(uniqueId);
		//model.setContractNo(contractNo);
		model.setReceivableAccountNo(receivableAccountNo);
		model.setPaymentBank(paymentBank);
		model.setBankTransactionNo(bankTransactionNo);
		model.setVoucherAmount(voucherAmount);
		model.setPaymentName(paymentName);
		model.setPaymentAccountNo(paymentAccountNo);
		model.setDetail(JsonUtils.toJSONString(details));
		model.setFinancialContractNo("G32001");

		Contract contract = contractService.load(Contract.class, 54340l);
		
		MockMultipartHttpServletRequest fileRequest = new MockMultipartHttpServletRequest();
		try {
			activePaymentVoucherHandler.submitActivePaymentVoucher(model, fileRequest);
			Assert.fail();
		} catch (ApiException e) {
			e.printStackTrace();
			Assert.assertEquals("收款账户错误，收款账户不是贷款合同的回款账户!", ApiMessageUtil.getMessage(e.getCode()));
		}
	}
	
	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/test_submitActivePaymentVoucher.sql")
	public void test_submitActivePaymentVoucher_wrongVoucherAmount() throws IOException {

		ActivePaymentVoucherCommandModel model = new ActivePaymentVoucherCommandModel();
		String repaymentPlanNo = "[\"ZC275016985BF712EF\",\"ZC275016985BF712EG\"]";
		String requestNo = "requestNoTest";
		Integer transactionType = 0;
		Integer voucherType = 0;
		String uniqueId = "uniqueId1";
		String contractNo = "contractNo1";
		String receivableAccountNo = "20001";
		String paymentBank = "中国建设银行";
		String bankTransactionNo = "transactionNo1";
		BigDecimal voucherAmount = new BigDecimal(100000.00);//错误
		String paymentName = "payment";
		String paymentAccountNo = "paymentAccountNo1";
		
		List<ActivePaymentVoucherDetail> details = new ArrayList<>();
		ActivePaymentVoucherDetail detail1 = new ActivePaymentVoucherDetail();
		detail1.setRepaymentPlanNo("ZC275016985BF712EF");
		detail1.setAmount(new BigDecimal("10000.00"));
		detail1.setPrincipal(new BigDecimal("10000.00"));
		detail1.setContractNo("云信信2016-241-DK(428522112675736882)");
		detail1.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3f");
		details.add(detail1);
		ActivePaymentVoucherDetail detail2 = new ActivePaymentVoucherDetail();
		detail2.setRepaymentPlanNo("ZC275016985BF712EG");
		detail2.setAmount(new BigDecimal("10000.00"));
		detail2.setPrincipal(new BigDecimal("10000.00"));
		detail2.setContractNo("云信信2016-241-DK(428522112675736882)");
		detail2.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3f");
		details.add(detail2);

		model.setRequestNo(requestNo);
		model.setTransactionType(transactionType);
		model.setVoucherType(voucherType);
		//model.setUniqueId(uniqueId);
		//model.setContractNo(contractNo);
		model.setReceivableAccountNo(receivableAccountNo);
		model.setPaymentBank(paymentBank);
		model.setBankTransactionNo(bankTransactionNo);
		model.setVoucherAmount(voucherAmount);
		model.setPaymentName(paymentName);
		model.setPaymentAccountNo(paymentAccountNo);
		model.setDetail(JsonUtils.toJSONString(details));
		model.setFinancialContractNo("G32001");

		Contract contract = contractService.load(Contract.class, 54341l);
		
		MockMultipartHttpServletRequest fileRequest = new MockMultipartHttpServletRequest();
		try {
			activePaymentVoucherHandler.submitActivePaymentVoucher(model, fileRequest);
			Assert.fail();
		} catch (ApiException e) {
			Assert.assertEquals("主动付款凭证金额错误，明细总额与凭证金额不一致!", ApiMessageUtil.getMessage(e.getCode()));
		}
	}
	
	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/test_submitActivePaymentVoucher.sql")
	public void test_submitActivePaymentVoucher_wrongRepaymentPlanNo() throws IOException {

		ActivePaymentVoucherCommandModel model = new ActivePaymentVoucherCommandModel();
		String repaymentPlanNo = "[\"ZC275016985BF712\"]";//错误
		String requestNo = "requestNoTest";
		Integer transactionType = 0;
		Integer voucherType = 5;
		String uniqueId = "uniqueId1";
		String contractNo = "contractNo1";
		String receivableAccountNo = "20001";
		String paymentBank = "中国建设银行";
		String bankTransactionNo = "transactionNo1";
		BigDecimal voucherAmount = new BigDecimal("20000.00");
		String paymentName = "payment";
		String paymentAccountNo = "paymentAccountNo1";
		
		List<ActivePaymentVoucherDetail> details = new ArrayList<>();
		ActivePaymentVoucherDetail detail1 = new ActivePaymentVoucherDetail();
		detail1.setRepaymentPlanNo("ZC275016985BF712EFAAA");//错误
		detail1.setAmount(new BigDecimal("10000.00"));
		detail1.setPrincipal(new BigDecimal("10000.00"));
		detail1.setContractNo("云信信2016-241-DK(428522112675736882)");
		detail1.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3f");
		details.add(detail1);
		ActivePaymentVoucherDetail detail2 = new ActivePaymentVoucherDetail();
		detail2.setRepaymentPlanNo("ZC275016985BF712EGBBB");//错误
		detail2.setAmount(new BigDecimal("10000.00"));
		detail2.setPrincipal(new BigDecimal("10000.00"));
		detail2.setContractNo("云信信2016-241-DK(428522112675736882)");
		detail2.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3f");
		details.add(detail2);
		
		model.setRequestNo(requestNo);
		model.setTransactionType(transactionType);
		model.setVoucherType(voucherType);
		//model.setUniqueId(uniqueId);
		//model.setContractNo(contractNo);
		model.setReceivableAccountNo(receivableAccountNo);
		model.setPaymentBank(paymentBank);
		model.setBankTransactionNo(bankTransactionNo);
		model.setVoucherAmount(voucherAmount);
		model.setPaymentName(paymentName);
		model.setPaymentAccountNo(paymentAccountNo);
		model.setDetail(JsonUtils.toJSONString(details));
		model.setFinancialContractNo("G32001");

		Contract contract = contractService.load(Contract.class, 54341l);
		MockMultipartHttpServletRequest fileRequest = new MockMultipartHttpServletRequest();
		try {
			activePaymentVoucherHandler.submitActivePaymentVoucher(model, fileRequest);
			Assert.fail();
		} catch (ApiException e) {
			Assert.assertEquals("不存在该有效还款计划或者还款计划不在贷款合同内", ApiMessageUtil.getMessage(e.getCode()));
		}
	}
	
	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/test_submitActivePaymentVoucher_noCashFlow.sql")
	public void test_submitActivePaymentVoucher_noCashFlow() throws IOException {

		ActivePaymentVoucherCommandModel model = new ActivePaymentVoucherCommandModel();
		String requestNo = "requestNoTest";
		Integer transactionType = 0;
		Integer voucherType = 0;
		String uniqueId = "uniqueId1";
		String contractNo = "contractNo1";
		String receivableAccountNo = "20001";
		String paymentBank = "中国建设银行";
		String bankTransactionNo = "transactionNo1";
		BigDecimal voucherAmount = new BigDecimal("20000.00");
		String paymentName = "payment";
		String paymentAccountNo = "paymentAccountNo1";
		
		List<ActivePaymentVoucherDetail> details = new ArrayList<>();
		ActivePaymentVoucherDetail detail1 = new ActivePaymentVoucherDetail();
		detail1.setRepaymentPlanNo("ZC275016985BF712EF");
		detail1.setAmount(new BigDecimal("10000.00"));
		detail1.setPrincipal(new BigDecimal("10000.00"));
		detail1.setContractNo("云信信2016-241-DK(428522112675736882)");
		//detail1.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3f");
		details.add(detail1);
		ActivePaymentVoucherDetail detail2 = new ActivePaymentVoucherDetail();
		detail2.setRepaymentPlanNo("ZC275016985BF712EG");
		detail2.setAmount(new BigDecimal("10000.00"));
		detail2.setPrincipal(new BigDecimal("10000.00"));
		detail2.setContractNo("云信信2016-241-DK(428522112675736882)");
		//detail2.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3f");
		details.add(detail2);
		
		model.setRequestNo(requestNo);
		model.setTransactionType(transactionType);
		model.setVoucherType(voucherType);
		//model.setUniqueId(uniqueId);
		//model.setContractNo(contractNo);
		model.setReceivableAccountNo(receivableAccountNo);
		model.setPaymentBank(paymentBank);
		model.setBankTransactionNo(bankTransactionNo);
		model.setVoucherAmount(voucherAmount);
		model.setPaymentName(paymentName);
		model.setPaymentAccountNo(paymentAccountNo);
		model.setDetail(JsonUtils.toJSONString(details));
		model.setFinancialContractNo("G32001");

		Contract contract = contractService.load(Contract.class, 54341l);
		
		MockMultipartHttpServletRequest fileRequest = new MockMultipartHttpServletRequest();
		try {
			activePaymentVoucherHandler.submitActivePaymentVoucher(model, fileRequest);
			Assert.fail();
		} catch (ApiException e) {
			Assert.assertEquals("主动付款凭证金额错误，明细金额应不大于还款计划应还未还金额!", ApiMessageUtil.getMessage(e.getCode()));
		}
	}
	
	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/test_submitActivePaymentVoucher_true.sql")
	public void test_submitActivePaymentVoucher() {
		ActivePaymentVoucherCommandModel model = new ActivePaymentVoucherCommandModel();
		String requestNo = "requestNoTest";
		Integer transactionType = 0;
		Integer voucherType = 6;
		String uniqueId = "uniqueId1";
		String contractNo = "contractNo1";
		String receivableAccountNo = "20001";
		String paymentBank = "中国建设银行";
		String bankTransactionNo = "transactionNo1";
		BigDecimal voucherAmount = new BigDecimal("5000.00");
		String paymentName = "payment";
		String paymentAccountNo = "paymentno1";

		List<ActivePaymentVoucherDetail> details = new ArrayList<>();
		ActivePaymentVoucherDetail detail1 = new ActivePaymentVoucherDetail();
		detail1.setRepaymentPlanNo("ZC275016985BF712EF");
		detail1.setAmount(new BigDecimal("5000.00"));
		detail1.setPrincipal(new BigDecimal("0.00"));
		detail1.setContractNo("云信信2016-241-DK(428522112675736882)");
		//detail1.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3f");
		details.add(detail1);

//		ActivePaymentVoucherDetail detail2 = new ActivePaymentVoucherDetail();
//		detail2.setRepaymentPlanNo("ZC275016985BF712EG");
//		detail2.setAmount(new BigDecimal("5000.00"));
//		detail2.setPrincipal(new BigDecimal("5000.00"));
//		detail2.setContractNo("云信信2016-241-DK(428522112675736882)");
//		//detail2.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3f");
//		details.add(detail2);

		model.setRequestNo(requestNo);
		model.setTransactionType(transactionType);
		model.setVoucherType(voucherType);
		//model.setUniqueId(uniqueId);
		//model.setContractNo(contractNo);
		model.setReceivableAccountNo(receivableAccountNo);
		model.setPaymentBank(paymentBank);
		model.setBankTransactionNo(bankTransactionNo);
		model.setVoucherAmount(voucherAmount);
		model.setPaymentName(paymentName);
		model.setPaymentAccountNo(paymentAccountNo);
		model.setDetail(JsonUtils.toJSONString(details));
		model.setFinancialContractNo("G32001");

		Contract contract = contractService.load(Contract.class, 54341l);

		MockMultipartHttpServletRequest fileRequest = new MockMultipartHttpServletRequest();

		try {
			activePaymentVoucherHandler.submitActivePaymentVoucher(model, fileRequest);

			Voucher voucher = voucherService.loadAll(Voucher.class).get(0);
			Assert.assertTrue(null != voucher);
			Assert.assertEquals(voucherAmount, voucher.getAmount());
			Assert.assertEquals(paymentAccountNo, voucher.getPaymentAccountNo());
			Assert.assertEquals(paymentName, voucher.getPaymentName());
			Assert.assertEquals(paymentBank, voucher.getPaymentBank());

			List<CashFlow> cashFlowList = cashFlowService.getCashFlowListBy(paymentAccountNo, paymentName, voucherAmount);
			for(CashFlow cashFlow : cashFlowList) {
				Assert.assertEquals(bankTransactionNo, cashFlow.getStringFieldOne());
			}

			List<SourceDocument> sourceDocuments = sourceDocumentService.loadAll(SourceDocument.class);
			for(SourceDocument sourceDocument : sourceDocuments) {
				Assert.assertEquals(voucher.getUuid(), sourceDocument.getVoucherUuid());
				Assert.assertEquals(voucherAmount, sourceDocument.getPlanBookingAmount());
				Assert.assertEquals(bankTransactionNo, sourceDocument.getOutlierSerialGlobalIdentity());
			}

			List<SourceDocumentDetail> sourceDocumentDetails = sourceDocumentDetailService.loadAll(SourceDocumentDetail.class);
			for(SourceDocumentDetail sourceDocumentDetail : sourceDocumentDetails) {
				if(sourceDocumentDetail.getStatus() != SourceDocumentDetailStatus.INVALID) {
					Assert.assertEquals(voucher.getUuid(), sourceDocumentDetail.getVoucherUuid());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/test_submitActivePaymentVoucher_true2.sql")
	public void test_submitActivePaymentVoucher_twoContract() {
		ActivePaymentVoucherCommandModel model = new ActivePaymentVoucherCommandModel();
		String requestNo = "requestNoTest";
		Integer transactionType = 0;
		Integer voucherType = 6;
		String uniqueId = "uniqueId1";
		String contractNo = "contractNo1";
		String receivableAccountNo = "20001";
		String paymentBank = "中国建设银行";
		String bankTransactionNo = "transactionNo1";
		BigDecimal voucherAmount = new BigDecimal("10000.00");
		String paymentName = "payment";
		String paymentAccountNo = "paymentno1";

		List<ActivePaymentVoucherDetail> details = new ArrayList<>();
		ActivePaymentVoucherDetail detail1 = new ActivePaymentVoucherDetail();
		detail1.setRepaymentPlanNo("ZC275016985BF712EF");
		detail1.setAmount(new BigDecimal("5000.00"));
		detail1.setPrincipal(new BigDecimal("0.00"));
		detail1.setContractNo("云信信2016-241-DK(428522112675736882)");
		//detail1.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3f");
		details.add(detail1);

		ActivePaymentVoucherDetail detail2 = new ActivePaymentVoucherDetail();
		detail2.setRepaymentPlanNo("ZC275016985BF712EB");
		detail2.setAmount(new BigDecimal("5000.00"));
		detail2.setPrincipal(new BigDecimal("0.00"));
		//detail2.setContractNo("云信信2016-241-DK(428522112675736882)");
		detail2.setUniqueId("e568793f-a44c-4362-9e78-0ce433131f3e");
		details.add(detail2);

		model.setRequestNo(requestNo);
		model.setTransactionType(transactionType);
		model.setVoucherType(voucherType);
		//model.setUniqueId(uniqueId);
		//model.setContractNo(contractNo);
		model.setReceivableAccountNo(receivableAccountNo);
		model.setPaymentBank(paymentBank);
		model.setBankTransactionNo(bankTransactionNo);
		model.setVoucherAmount(voucherAmount);
		model.setPaymentName(paymentName);
		model.setPaymentAccountNo(paymentAccountNo);
		model.setDetail(JsonUtils.toJSONString(details));
		model.setFinancialContractNo("G32001");

		Contract contract = contractService.load(Contract.class, 54341l);

		MockMultipartHttpServletRequest fileRequest = new MockMultipartHttpServletRequest();

		try {
			activePaymentVoucherHandler.submitActivePaymentVoucher(model, fileRequest);

			Voucher voucher = voucherService.loadAll(Voucher.class).get(0);
			Assert.assertTrue(null != voucher);
			Assert.assertEquals(voucherAmount, voucher.getAmount());
			Assert.assertEquals(paymentAccountNo, voucher.getPaymentAccountNo());
			Assert.assertEquals(paymentName, voucher.getPaymentName());
			Assert.assertEquals(paymentBank, voucher.getPaymentBank());

			List<CashFlow> cashFlowList = cashFlowService.getCashFlowListBy(paymentAccountNo, paymentName, voucherAmount);
			for(CashFlow cashFlow : cashFlowList) {
				Assert.assertEquals(bankTransactionNo, cashFlow.getStringFieldOne());
			}

			List<SourceDocument> sourceDocuments = sourceDocumentService.loadAll(SourceDocument.class);
			for(SourceDocument sourceDocument : sourceDocuments) {
				Assert.assertEquals(voucher.getUuid(), sourceDocument.getVoucherUuid());
				Assert.assertEquals(new BigDecimal("5000.00"), sourceDocument.getPlanBookingAmount());
				Assert.assertEquals(bankTransactionNo, sourceDocument.getOutlierSerialGlobalIdentity());
			}

			List<SourceDocumentDetail> sourceDocumentDetails = sourceDocumentDetailService.loadAll(SourceDocumentDetail.class);
			for(SourceDocumentDetail sourceDocumentDetail : sourceDocumentDetails) {
				if(sourceDocumentDetail.getStatus() != SourceDocumentDetailStatus.INVALID) {
					Assert.assertEquals(voucher.getUuid(), sourceDocumentDetail.getVoucherUuid());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/empty_database.sql")
	public void test_undoActivePaymentVoucher_financialContractNotExist() {

		String bankTransactionNo = "transactionNo1";
		try {
			activePaymentVoucherHandler.undoActivePaymentVoucher(null, bankTransactionNo);
			Assert.fail();
		} catch (ApiException e) {
			Assert.assertEquals("信托合同不存在", ApiMessageUtil.getMessage(e.getCode()));
		}
	}
	
	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/test_submitActivePaymentVoucher.sql")
	public void test_undoActivePaymentVoucher_noSuchVoucher1() {
		FinancialContract financialContract = financialContractService.load(FinancialContract.class, 38l);
		String bankTransactionNo = "transactionNo1";
		try {
			activePaymentVoucherHandler.undoActivePaymentVoucher(financialContract, bankTransactionNo);
			Assert.fail();
		} catch (ApiException e) {
			Assert.assertEquals("凭证对应流水不存在", ApiMessageUtil.getMessage(e.getCode()));
		}
	}
	
	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/test_undoActivePaymentVoucher_noSuchVoucher2.sql")
	public void test_undoActivePaymentVoucher_noSuchVoucher2() {
		FinancialContract financialContract = financialContractService.load(FinancialContract.class, 38l);
		String bankTransactionNo = "transactionNo1";
		try {
			activePaymentVoucherHandler.undoActivePaymentVoucher(financialContract, bankTransactionNo);
			Assert.fail();
		} catch (ApiException e) {
			e.printStackTrace();
			Assert.assertEquals("凭证对应流水不存在", ApiMessageUtil.getMessage(e.getCode()));
		}
	}
	
	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/test_undoActivePaymentVoucher_voucherCanotCancel.sql")
	public void test_undoActivePaymentVoucher_voucherCanotCancel() {
		FinancialContract financialContract = financialContractService.load(FinancialContract.class, 38l);
		String bankTransactionNo = "transactionNo1";
		try {
			activePaymentVoucherHandler.undoActivePaymentVoucher(financialContract, bankTransactionNo);
			Assert.fail();
		} catch (ApiException e) {
			e.printStackTrace();
			Assert.assertEquals("当前凭证无法撤销!", ApiMessageUtil.getMessage(e.getCode()));
		}
	}
	
	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/test_undoActivePaymentVoucher.sql")
	public void test_undoActivePaymentVoucher() {
		FinancialContract financialContract = financialContractService.load(FinancialContract.class, 38l);
		String bankTransactionNo = "transactionNo1";
		activePaymentVoucherHandler.undoActivePaymentVoucher(financialContract, bankTransactionNo);

//		Voucher voucher = voucherService.loadAll(Voucher.class).get(0);
//		Assert.assertEquals(SourceDocumentDetailStatus.INVALID, voucher.getStatus());
//
		SourceDocument sourceDocument = sourceDocumentService.load(SourceDocument.class, 18l);
		Assert.assertTrue(StringUtils.isEmpty(sourceDocument.getVoucherUuid()));
		Assert.assertTrue(StringUtils.isEmpty(sourceDocument.getOutlierSerialGlobalIdentity()));
//		Assert.assertEquals(BigDecimal.ZERO, sourceDocument.getPlanBookingAmount());

		CashFlow cashFlow = cashFlowService.load(CashFlow.class, 1l);
//		Assert.assertTrue(StringUtils.isEmpty(cashFlow.getStringFieldOne()));

		SourceDocumentDetail sourceDocumentDetail1 = sourceDocumentDetailService.load(SourceDocumentDetail.class, 17l);
		Assert.assertEquals(SourceDocumentDetailStatus.INVALID, sourceDocumentDetail1.getStatus());

		SourceDocumentDetail sourceDocumentDetail2 = sourceDocumentDetailService.load(SourceDocumentDetail.class, 18l);
		Assert.assertEquals(SourceDocumentDetailStatus.INVALID, sourceDocumentDetail2.getStatus());
	}


//	@Test
//	public void test_save_file_to_service() throws IOException{
//
//
//			File dir = new File("../earth/tmp");
//			FileUtils.deleteDirectory(dir);
//			String pathname = "../earth/src/test/resources/test/yunxin/activepaymentvoucher/ceshi.pdf";
//			String fileName = "ceshi.pdf";
//			dir.mkdirs();
//			Assert.assertTrue(FileUtils.sizeOf(dir) == 0);
//			MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
//			File inputFile = new File(pathname);
//			FileInputStream fileInputStream = new FileInputStream(inputFile);
//			MultipartFile file = new MockMultipartFile(fileName, fileName, null, FileCopyUtils.copyToByteArray(fileInputStream));
//
//			request.addFile(file);
//			String sourceDocumentUuid = "123-123-123";
//			String requestNo = "123123123";
//			activePaymentVoucherHandler.save_file_to_service(request, requestNo, voucherNo);
//			Assert.assertTrue(FileUtils.sizeOf(dir) > 0);
//			FileUtils.deleteDirectory(dir);
//
//	}

//	@Test
//	public void test_save_file_to_service_wrongTypeOfFile() throws IOException{
//		try{
//		File dir = new File("../earth/tmp");
//		FileUtils.deleteDirectory(dir);
//		String pathname = "../earth/src/test/resources/test/yunxin/activepaymentvoucher/wrongType.docx";
//		String fileName = "wrongType.docx";
//		dir.mkdirs();
//		Assert.assertTrue(FileUtils.sizeOf(dir) == 0);
//		MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
//		File inputFile = new File(pathname);
//		FileInputStream fileInputStream = new FileInputStream(inputFile);
//		MultipartFile file = new MockMultipartFile(fileName, fileName, null, FileCopyUtils.copyToByteArray(fileInputStream));
//
//		request.addFile(file);
//		String sourceDocumentUuid = "123-123-123";
//		String requestNo = "123123123";
//		activePaymentVoucherHandler.save_file_to_service(request, requestNo, voucherNo);
//		Assert.assertTrue(FileUtils.sizeOf(dir) > 0);
//		FileUtils.deleteDirectory(dir);
//		Assert.fail();
//		}catch(ApiException e){
//			Assert.assertEquals(34001, e.getCode());
//		}
//	}

	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/test_searchAccountInfoByContractNo.sql")
	public void test_searchAccountInfoByContractNo(){
		String contractNo = "2016-78-DK(ZQ2016042522479)";
		VoucherCreateBaseModel model = activePaymentVoucherHandler.searchAccountInfoByContractNo(contractNo);
		List<AssetSet> assetSets = model.getRepaymentPlanList();
		Contract contract = model.getContract();
		ContractAccount contractAccount = model.getContractAccount();
		int size = assetSets.size();
		Assert.assertEquals(1, size);
		Assert.assertNotNull(contract);
		Assert.assertNotNull(contractAccount);
		
	}
	
	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/test_searchAccountInfoByContractNo.sql")
	public void test_searchAccountInfoByContractNo_noContract(){
		String contractNo = "1234567";
		VoucherCreateBaseModel model = activePaymentVoucherHandler.searchAccountInfoByContractNo(contractNo);
		Assert.assertNull(model);
	}
	
	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/test_searchAccountInfoByContractNo.sql")
	public void test_searchAccountInfoByContractNo_noContractAccount(){
		String contractNo = "2016-78-DK(ZQ2016042422395)";
		VoucherCreateBaseModel model = activePaymentVoucherHandler.searchAccountInfoByContractNo(contractNo);
		Assert.assertNull(model);
	}
	

	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/test_searchAccountInfoByContractNo.sql")
	public void test_searchAccountInfoByContractNo_emptyAssetSetList(){
		String contractNo = "2016-78-DK(ZQ2016042522502)";
		VoucherCreateBaseModel model = activePaymentVoucherHandler.searchAccountInfoByContractNo(contractNo);
		Assert.assertNull(model);
	}
	
	
	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/test_searchAccountInfoByContractNo.sql")
	public void test_searchAccountInfoByName(){
		String name = "测试用户1";
		List<ContractAccount> contractAccounts = activePaymentVoucherHandler.searchAccountInfoByName(name);
		int size = contractAccounts.size();
		Assert.assertEquals(2,size);
		
	}
	
	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/test_saveActiveVoucher.sql")
	public void test_saveActiveVoucher() throws IOException{
		VoucherCreateSubmitModel model = new VoucherCreateSubmitModel();
		model.setVoucherType(5);
		model.setPaymentBank("中国银行");
		model.setBankTransactionNo("cash_flow_no_12");
		model.setVoucherAmount(new BigDecimal("2207.00"));
		model.setPaymentName("counter_name");
		model.setPaymentAccountNo("10001");
		Principal principal = new Principal();
		principal.setId(1L);
		principal.setName("zhanghongbin");
		String ip = "123.123.123";
		activePaymentVoucherHandler.save(model, principal, ip);
		List<Voucher> vouchers = voucherService.loadAll(Voucher.class);
		Voucher voucher = vouchers.get(0);
		Assert.assertEquals("2207.00",voucher.getAmount().toString());
		
	}
	
	
	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/test_saveActiveVoucher.sql")
	public void test_saveActiveVoucher_noContract() throws IOException{
		VoucherCreateSubmitModel model = new VoucherCreateSubmitModel();
		model.setVoucherType(5);
		model.setPaymentBank("中国银行");
		model.setBankTransactionNo("cash_flow_no_12");
		model.setVoucherAmount(new BigDecimal("2207.00"));
		model.setPaymentName("counter_name");
		model.setPaymentAccountNo("10001");
		Principal principal = new Principal();
		principal.setId(1L);
		principal.setName("zhanghongbin");
		String ip = "123.123.123";
		try{
		activePaymentVoucherHandler.save(model, principal, ip);
		}catch(RuntimeException e){
			Assert.assertEquals("贷款合同编号错误！", e.getMessage());
		}
		
	}
	
	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/test_saveActiveVoucher.sql")
	public void test_saveActiveVoucher_wrongAmount() throws IOException{
		VoucherCreateSubmitModel model = new VoucherCreateSubmitModel();
		model.setVoucherType(5);
		model.setPaymentBank("中国银行");
		model.setBankTransactionNo("cash_flow_no_12");
		model.setVoucherAmount(new BigDecimal("1000.00"));
		model.setPaymentName("counter_name");
		model.setPaymentAccountNo("10001");
		Principal principal = new Principal();
		principal.setId(1L);
		principal.setName("zhanghongbin");
		String ip = "123.123.123";
		try{
		activePaymentVoucherHandler.save(model, principal, ip);
		}catch(ApiException e){
			Assert.assertEquals("主动付款凭证金额错误，凭证金额应为还款计划总额!  ", ApiMessageUtil.getMessage(e.getCode()));
		}
	}
	
	
	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/test_updateActiveVoucherComment.sql")
	public void test_updateActiveVoucherComment(){
		Long voucherId = 1L;
		String comment = "凭证测试";
		Principal principal = new Principal();
		principal.setId(1l);
		String ip = "127.0.0.1";
		activePaymentVoucherHandler.updateActiveVoucherComment(voucherId, comment,principal, ip);
		SourceDocument sourceDocument = sourceDocumentService.load(SourceDocument.class, 74L);
		Assert.assertEquals(comment, sourceDocument.getOutlierBreif());
		
	}
	
	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/test_updateActiveVoucherComment.sql")
	public void test_updateActiveVoucherComment_invalid(){
		Long voucherId = 2L;
		String comment = "凭证测试";
		try{
			Principal principal = new Principal();
			principal.setId(1l);
			String ip = "127.0.0.1";
		activePaymentVoucherHandler.updateActiveVoucherComment(voucherId, comment, principal, ip);
		}catch(ApiException e){
			Assert.assertEquals("当前凭证已作废", e.getMsg());
		}
		
	}
	
	
	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/test_getActiveVouherResource.sql")
	public void test_getActiveVoucherResource(){
		Long voucherId = 1L;
		List<String> str = activePaymentVoucherHandler.getActiveVoucherResource(voucherId);
		int size = str.size();
		Assert.assertEquals(1, size);
	}
	
	
	@Test
	public void test_uploadSingleFileReturnUUID() throws IOException{

		File dir = new File("../earth/tmp");
		FileUtils.deleteDirectory(dir);
		String pathname = "../earth/src/test/resources/test/yunxin/activepaymentvoucher/ceshi.pdf";
		String fileName = "ceshi.pdf";
		dir.mkdirs();
		Assert.assertTrue(FileUtils.sizeOf(dir) == 0);
		File inputFile = new File(pathname);
		FileInputStream fileInputStream = new FileInputStream(inputFile);
		MultipartFile file = new MockMultipartFile(fileName, fileName, null, FileCopyUtils.copyToByteArray(fileInputStream));
		String str = activePaymentVoucherHandler.uploadSingleFileReturnUUID(file);
		Assert.assertTrue(FileUtils.sizeOf(dir) > 0);
		Assert.assertNotNull(str);
		FileUtils.deleteDirectory(dir);
		
	}
	
	
//	@Test
//	@Sql("classpath:test/yunxin/activepaymentvoucher/test_submitActivePaymentVoucher.sql")
//	public void test_submitActivePaymentVoucherBySubmitModel() throws IOException {
//
//		VoucherCreateSubmitModel model = new VoucherCreateSubmitModel();
//		Integer voucherType = 6;
//		String paymentBank = "中国建设银行";
//		String bankTransactionNo = "transactionNo1";
//		BigDecimal voucherAmount = new BigDecimal("10000.00");
//		String paymentName = "payment";
//		String paymentAccountNo = "paymentno1";
//		String cashFlowUuid = "cashflowuuid1";
//		BigDecimal repaymentPlanAmount = new BigDecimal("10000.00");
//		String financialContractUuid = "b674a323-0c30-4e4b-9eba-b14e05a9d80b";
//		String contractUuid = "3e8711d4-9573-4965-a878-480ee4c1f5fd";
//		String contractUniqueId = "e568793f-a44c-4362-9e78-0ce433131f3f";
//		String resourceUuids = "[\"test_resource_uuid_1\",\"test_resource_uuid_2\"]";
//
//		List<VoucherCreateAssetInfoModel> assetInfoModels = new ArrayList<>();
//		VoucherCreateAssetInfoModel assetInfoModel1 = new VoucherCreateAssetInfoModel();
//		assetInfoModel1.setSingleLoanContractNo("ZC275016985BF712EF");
//		assetInfoModel1.setContractUuid(contractUuid);
//		assetInfoModel1.setContractUniqueId(contractUniqueId);
		////assetInfoModel1.setOtherCharge("18.00");
		////assetInfoModel1.setServiceCharge("11.00");
		////assetInfoModel1.setMaintenanceCharge("12.00");
////		assetInfoModel1.setOtherCharge("1.00");
//		assetInfoModel1.setPrincipalValue("5000.00");
//		assetInfoModels.add(assetInfoModel1);
//
//		VoucherCreateAssetInfoModel assetInfoModel2 = new VoucherCreateAssetInfoModel();
//		assetInfoModel2.setSingleLoanContractNo("ZC275016985BF712EG");
//		assetInfoModel2.setContractUuid(contractUuid);
//		assetInfoModel2.setContractUniqueId(contractUniqueId);
		//assetInfoModel2.setPrincipalValue("5000.00");
//		assetInfoModels.add(assetInfoModel2);
//
//		String assetInfoModelsJson = JsonUtils.toJSONString(assetInfoModels);
//		model.setAssetInfoModels(assetInfoModelsJson);
//
//		model.setVoucherType(voucherType);
//		model.setPaymentBank(paymentBank);
//		model.setBankTransactionNo(bankTransactionNo);
//		model.setVoucherAmount(voucherAmount);
//		model.setPaymentName(paymentName);
//		model.setPaymentAccountNo(paymentAccountNo);
//		model.setCashFlowUuid(cashFlowUuid);
//		model.setRepaymentPlanAmount(repaymentPlanAmount);
		////model.setAssetInfoModels(assetInfoModels);
//		model.setResourceUuids(resourceUuids);
//		model.setFinancialContractUuid(financialContractUuid);
//
//		Principal principal = new Principal();
//		principal.setName("testname");
//		principal.setId(1l);
//
//		String ip = "127.0.0.1";
//
//		List<ContractActiveSourceDocumentMapper> mappers = activePaymentVoucherHandler.submit(principal, model, ip);
//		assertEquals(1,mappers.size());
//		assertEquals(cashFlowUuid,mappers.get(0).getCashFlowUuid());
//		Voucher voucher = voucherService.loadAll(Voucher.class).get(0);
//		Assert.assertTrue(null != voucher);
//		assertEquals(voucherAmount, voucher.getAmount());
//		assertEquals(cashFlowUuid, voucher.getCashFlowUuid());
//
//		CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
//		assertEquals(bankTransactionNo, cashFlow.getStringFieldOne());
//
////		List<SourceDocument> sourceDocuments = sourceDocumentService.loadAll(SourceDocument.class);
////		for(SourceDocument sourceDocument : sourceDocuments) {
////			Assert.assertEquals(voucher.getUuid(), sourceDocument.getVoucherUuid());
////			Assert.assertEquals(voucherAmount, sourceDocument.getPlanBookingAmount());
////			Assert.assertEquals(bankTransactionNo, sourceDocument.getOutlierSerialGlobalIdentity());
////		}
//
//		List<SourceDocumentDetail> sourceDocumentDetails = sourceDocumentDetailService.loadAll(SourceDocumentDetail.class);
//		for(SourceDocumentDetail sourceDocumentDetail : sourceDocumentDetails) {
//			assertEquals(voucher.getUuid(), sourceDocumentDetail.getVoucherUuid());
//		}
//	}

	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/test_submitActivePaymentVoucher_part_issuing.sql")
	public void test_submitActivePaymentVoucherBySubmitModel_2() throws IOException {

		VoucherCreateSubmitModel model = new VoucherCreateSubmitModel();
		Integer voucherType = 6;
		String paymentBank = "中国建设银行";
		String bankTransactionNo = "transactionNo1";
		BigDecimal voucherAmount = new BigDecimal("10000.00");
		String paymentName = "payment";
		String paymentAccountNo = "paymentno1";
		String cashFlowUuid = "cashflowuuid1";
		BigDecimal repaymentPlanAmount = new BigDecimal("10000.00");
		String financialContractUuid = "b674a323-0c30-4e4b-9eba-b14e05a9d80b";
		String contractUuid = "3e8711d4-9573-4965-a878-480ee4c1f5fd";
		String contractUniqueId = "e568793f-a44c-4362-9e78-0ce433131f3f";
		String resourceUuids = "[\"test_resource_uuid_1\",\"test_resource_uuid_2\"]";

		List<VoucherCreateAssetInfoModel> assetInfoModels = new ArrayList<>();
		VoucherCreateAssetInfoModel assetInfoModel1 = new VoucherCreateAssetInfoModel();
		assetInfoModel1.setSingleLoanContractNo("ZC275016985BF712EF");
		assetInfoModel1.setContractUuid(contractUuid);
		assetInfoModel1.setContractUniqueId(contractUniqueId);
		//assetInfoModel1.setOtherCharge("18.00");
		//assetInfoModel1.setServiceCharge("11.00");
		//assetInfoModel1.setMaintenanceCharge("12.00");
//		assetInfoModel1.setOtherCharge("1.00");
		assetInfoModel1.setPrincipalValue("5000.00");
		assetInfoModels.add(assetInfoModel1);

		VoucherCreateAssetInfoModel assetInfoModel2 = new VoucherCreateAssetInfoModel();
		assetInfoModel2.setSingleLoanContractNo("ZC275016985BF712EG");
		assetInfoModel2.setContractUuid(contractUuid);
		assetInfoModel2.setContractUniqueId(contractUniqueId);;
		assetInfoModel2.setPrincipalValue("5000.00");
		assetInfoModels.add(assetInfoModel2);

		String assetInfoModelsJson = JsonUtils.toJSONString(assetInfoModels);
		model.setAssetInfoModels(assetInfoModelsJson);

		model.setVoucherType(voucherType);
		model.setPaymentBank(paymentBank);
		model.setBankTransactionNo(bankTransactionNo);
		model.setVoucherAmount(voucherAmount);
		model.setPaymentName(paymentName);
		model.setPaymentAccountNo(paymentAccountNo);
		model.setCashFlowUuid(cashFlowUuid);
		model.setRepaymentPlanAmount(repaymentPlanAmount);
		//model.setAssetInfoModels(assetInfoModels);
		model.setResourceUuids(resourceUuids);
		model.setFinancialContractUuid(financialContractUuid);

		Principal principal = new Principal();
		principal.setName("testname");
		principal.setId(1l);

		String ip = "127.0.0.1";

		List<ContractActiveSourceDocumentMapper> mappers = null;
		try {
			mappers = activePaymentVoucherHandler.submit(principal, model, ip);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//部分充值不发送消息
		assertEquals(0,mappers.size());
	}

//	@Test
//	@Sql("classpath:test/yunxin/activepaymentvoucher/test_submitActivePaymentVoucher_voucherExist.sql")
//	public void test_submitActivePaymentVoucherBySubmitModel_voucherExist() throws IOException {
//		
//		VoucherCreateSubmitModel model = new VoucherCreateSubmitModel();
//		String voucherNo = "V15849127738454016";
//		Integer voucherType = 6;
//		String paymentBank = "中国建设银行";
//		String bankTransactionNo = "transactionNo1";
//		BigDecimal voucherAmount = new BigDecimal("10000.00");
//		String paymentName = "payment";
//		String paymentAccountNo = "paymentno1";
//		String cashFlowUuid = "cashflowuuid1";
//		BigDecimal repaymentPlanAmount = new BigDecimal("10000.00");
//		String financialContractUuid = "b674a323-0c30-4e4b-9eba-b14e05a9d80b";
//		String contractUuid = "3e8711d4-9573-4965-a878-480ee4c1f5fd";
//		String contractUniqueId = "e568793f-a44c-4362-9e78-0ce433131f3f";
//		String resourceUuids = "[\"test_resource_uuid_3\",\"test_resource_uuid_4\"]";
//		
//		List<VoucherCreateAssetInfoModel> assetInfoModels = new ArrayList<>();
//		VoucherCreateAssetInfoModel assetInfoModel1 = new VoucherCreateAssetInfoModel();
//		assetInfoModel1.setSingleLoanContractNo("ZC275016985BF712EF");
//		assetInfoModel1.setContractUuid(contractUuid);
//		assetInfoModel1.setContractUniqueId(contractUniqueId);
//		assetInfoModel1.setPrincipalValue("5000.00");
//		assetInfoModels.add(assetInfoModel1);
//		
//		VoucherCreateAssetInfoModel assetInfoModel2 = new VoucherCreateAssetInfoModel();
//		assetInfoModel2.setSingleLoanContractNo("ZC275016985BF712EG");
//		assetInfoModel2.setContractUuid(contractUuid);
//		assetInfoModel2.setContractUniqueId(contractUniqueId);
//		assetInfoModel2.setPrincipalValue("5000.00");
//		assetInfoModels.add(assetInfoModel2);
//		
//		String assetInfoModelsJson = JsonUtils.toJSONString(assetInfoModels);
//		model.setAssetInfoModels(assetInfoModelsJson);
//		
//		model.setVoucherType(voucherType);
//		model.setPaymentBank(paymentBank);
//		model.setBankTransactionNo(bankTransactionNo);
//		model.setVoucherAmount(voucherAmount);
//		model.setPaymentName(paymentName);
//		model.setPaymentAccountNo(paymentAccountNo);
//		model.setCashFlowUuid(cashFlowUuid);
//		model.setRepaymentPlanAmount(repaymentPlanAmount);
//		//model.setAssetInfoModels(assetInfoModels);
//		model.setResourceUuids(resourceUuids);
//		model.setVoucherNo(voucherNo);
//		model.setFinancialContractUuid(financialContractUuid);
//		
//		Principal principal = new Principal();
//		principal.setName("testname");
//		principal.setId(1l);
//		
//		String ip = "127.0.0.1";
//		
//		activePaymentVoucherHandler.submit(principal, model, ip);
//		
//		Voucher voucher = voucherService.loadAll(Voucher.class).get(0);
//		Assert.assertEquals(voucherNo, voucher.getVoucherNo());
//		Assert.assertTrue(null != voucher);
//		Assert.assertEquals(voucherAmount, voucher.getAmount());
//		Assert.assertEquals(paymentAccountNo, voucher.getPaymentAccountNo());
//		Assert.assertEquals(paymentName, voucher.getPaymentName());
//		Assert.assertEquals(paymentBank, voucher.getPaymentBank());
//		
//		CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
//		Assert.assertEquals(bankTransactionNo, cashFlow.getStringFieldOne());
//		
////		List<SourceDocument> sourceDocuments = sourceDocumentService.loadAll(SourceDocument.class);
////		for(SourceDocument sourceDocument : sourceDocuments) {
////			Assert.assertEquals(voucher.getUuid(), sourceDocument.getVoucherUuid());
////			Assert.assertEquals(voucherAmount, sourceDocument.getPlanBookingAmount());
////			Assert.assertEquals(bankTransactionNo, sourceDocument.getOutlierSerialGlobalIdentity());
////		}
//		
//		List<SourceDocumentDetail> sourceDocumentDetails = sourceDocumentDetailService.loadAll(SourceDocumentDetail.class);
//		for(SourceDocumentDetail sourceDocumentDetail : sourceDocumentDetails) {
//			if(sourceDocumentDetail.getStatus() != SourceDocumentDetailStatus.INVALID) {
//				Assert.assertEquals(voucher.getUuid(), sourceDocumentDetail.getVoucherUuid());
//			}
//		}
//	}
	
//	@Test
//	@Sql("classpath:test/yunxin/activepaymentvoucher/test_submitActivePaymentVoucher.sql")
//	public void test_saveActivePaymentVoucherBySubmitModel() throws IOException {
//
//		VoucherCreateSubmitModel model = new VoucherCreateSubmitModel();
//		Integer voucherType = 6;
//		String paymentBank = "中国建设银行";
//		String bankTransactionNo = "transactionNo1";
//		BigDecimal voucherAmount = new BigDecimal("10000.00");
//		String paymentName = "payment";
//		String paymentAccountNo = "paymentno1";
//		String cashFlowUuid = "cashflowuuid1";
//		BigDecimal repaymentPlanAmount = new BigDecimal("10000.00");
//		String contractUuid = "3e8711d4-9573-4965-a878-480ee4c1f5fd";
//		String contractUniqueId = "e568793f-a44c-4362-9e78-0ce433131f3f";
//		String resourceUuids = "[\"test_resource_uuid_1\",\"test_resource_uuid_2\"]";
//
//		List<VoucherCreateAssetInfoModel> assetInfoModels = new ArrayList<>();
//		VoucherCreateAssetInfoModel assetInfoModel1 = new VoucherCreateAssetInfoModel();
//		assetInfoModel1.setSingleLoanContractNo("ZC275016985BF712EF");
//		assetInfoModel1.setContractUuid(contractUuid);
//		assetInfoModel1.setContractUniqueId(contractUniqueId);
////		assetInfoModel1.setOtherCharge("1.00");
//		assetInfoModels.add(assetInfoModel1);
//
//		VoucherCreateAssetInfoModel assetInfoModel2 = new VoucherCreateAssetInfoModel();
//		assetInfoModel2.setSingleLoanContractNo("ZC275016985BF712EG");
//		assetInfoModel2.setContractUuid(contractUuid);
//		assetInfoModel2.setContractUniqueId(contractUniqueId);
//		assetInfoModels.add(assetInfoModel2);
//
//		model.setVoucherType(voucherType);
//		model.setPaymentBank(paymentBank);
//		model.setBankTransactionNo(bankTransactionNo);
//		model.setVoucherAmount(voucherAmount);
//		model.setPaymentName(paymentName);
//		model.setPaymentAccountNo(paymentAccountNo);
//		model.setCashFlowUuid(cashFlowUuid);
//		model.setRepaymentPlanAmount(repaymentPlanAmount);
//		//model.setAssetInfoModels(assetInfoModels);
//		model.setResourceUuids(resourceUuids);
//
//		Principal principal = new Principal();
//		principal.setName("testname");
//		principal.setId(1l);
//		String ip = "127.0.0.1";
//		activePaymentVoucherHandler.save(model,principal,ip);
//
//		Voucher voucher = voucherService.loadAll(Voucher.class).get(0);
//		Assert.assertTrue(null != voucher);
//		assertEquals(voucherAmount, voucher.getAmount());
//	}

//	@Test
//	@Sql("classpath:test/yunxin/activepaymentvoucher/test_submitActivePaymentVoucher_voucherExist.sql")
//	public void test_saveActivePaymentVoucherBySubmitModel_voucherExist() throws IOException {
//
//		VoucherCreateSubmitModel model = new VoucherCreateSubmitModel();
//		String voucherNo = "V15849127738454016";
//		Integer voucherType = 6;
//		String contractNo = "云信信2016-241-DK(428522112675736882)";
//		String paymentBank = "中国建设银行";
//		String bankTransactionNo = "transactionNo1";
//		BigDecimal voucherAmount = new BigDecimal("35000.00");
//		String paymentName = "payment";
//		String paymentAccountNo = "paymentno1";
//		String cashFlowUuid = "cashflowuuid1";
//		String contractUuid = "3e8711d4-9573-4965-a878-480ee4c1f5fd";
//		String contractUniqueId = "e568793f-a44c-4362-9e78-0ce433131f3f";
//		BigDecimal repaymentPlanAmount = new BigDecimal("35000.00");
//		String resourceUuids = "[\"test_resource_uuid_3\",\"test_resource_uuid_4\"]";
//
//		List<VoucherCreateAssetInfoModel> assetInfoModels = new ArrayList<>();
//		VoucherCreateAssetInfoModel assetInfoModel1 = new VoucherCreateAssetInfoModel();
//		assetInfoModel1.setSingleLoanContractNo("ZC275016985BF712EF");
//		assetInfoModel1.setContractUuid(contractUuid);
//		assetInfoModel1.setContractUniqueId(contractUniqueId);
////		assetInfoModel1.setOtherCharge("1.00");
//		assetInfoModels.add(assetInfoModel1);
//
//		VoucherCreateAssetInfoModel assetInfoModel2 = new VoucherCreateAssetInfoModel();
//		assetInfoModel2.setSingleLoanContractNo("ZC275016985BF712EG");
//		assetInfoModel2.setContractUuid(contractUuid);
//		assetInfoModel2.setContractUniqueId(contractUniqueId);
//		assetInfoModels.add(assetInfoModel2);
//
//		model.setVoucherType(voucherType);
//		model.setPaymentBank(paymentBank);
//		model.setBankTransactionNo(bankTransactionNo);
//		model.setVoucherAmount(voucherAmount);
//		model.setPaymentName(paymentName);
//		model.setPaymentAccountNo(paymentAccountNo);
//		model.setCashFlowUuid(cashFlowUuid);
//		model.setRepaymentPlanAmount(repaymentPlanAmount);
//		//model.setAssetInfoModels(assetInfoModels);
//		model.setResourceUuids(resourceUuids);
//		model.setVoucherNo(voucherNo);
//
//		Principal principal = new Principal();
//		principal.setName("testname");
//		principal.setId(1l);
//
//		String ip = "127.0.0.1";
//		activePaymentVoucherHandler.save(model,principal,ip);
//
//		Voucher voucher = voucherService.loadAll(Voucher.class).get(0);
//		assertEquals(voucherNo, voucher.getVoucherNo());
//		Assert.assertTrue(null != voucher);
//		assertEquals(voucherAmount, voucher.getAmount());
//		assertEquals(paymentAccountNo, voucher.getPaymentAccountNo());
//		assertEquals(paymentName, voucher.getPaymentName());
//		assertEquals(paymentBank, voucher.getPaymentBank());
//	}

//	@Test
//	@Sql("classpath:test/yunxin/activepaymentvoucher/test_getBaseModelListByContractNo.sql")
//	public void test_getBaseModelListByContractNo() {
//		String contractNo = "99bfb2ef-396e-41ca-9355-8333ad86388f";
//		List<VoucherCreateContractInfoModel> models = activePaymentVoucherHandler.getBaseModelListByContractNo(contractNo);
//		Assert.assertEquals(1, models.size());
//		Assert.assertEquals("拍拍贷测试", models.get(0).getFinancialContractName());
//		Assert.assertEquals("99bfb2ef-396e-41ca-9355-8333ad86388f", models.get(0).getContract().getContractNo());
//	}

	@Test
	@Sql("classpath:test/yunxin/activepaymentvoucher/test_getContractAccountInfoModelListBy.sql")
	public void test_getContractAccountInfoModelListBy() {
		String paymentAccountNo = "6217";
		List<VoucherCreateAccountInfoModel> models = activePaymentVoucherHandler.getContractAccountInfoModelListBy(paymentAccountNo);
		assertEquals(5, models.size());
		for(VoucherCreateAccountInfoModel model : models) {
			assertEquals("韩方园", model.getPaymentName());
		}
	}
	
}
