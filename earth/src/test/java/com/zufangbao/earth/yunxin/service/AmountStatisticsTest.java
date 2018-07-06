package com.zufangbao.earth.yunxin.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.zufangbao.earth.yunxin.handler.YunxinOfflinePaymentHandler;
import com.zufangbao.sun.handler.DeductApplicationCoreOperationHandler;
import com.zufangbao.sun.service.OrderService;
import com.zufangbao.sun.service.RepurchaseService;
import com.zufangbao.sun.service.TransferApplicationService;
import com.zufangbao.sun.yunxin.entity.deduct.DeductApplicationQeuryModel;
import com.zufangbao.sun.yunxin.entity.model.GuaranteeOrderModel;
import com.zufangbao.sun.yunxin.entity.model.OfflineBillQueryModel;
import com.zufangbao.sun.yunxin.entity.model.OrderQueryModel;
import com.zufangbao.sun.yunxin.entity.model.TransferApplicationQueryModel;
import com.zufangbao.sun.yunxin.entity.model.assetset.AssetSetQueryModel;
import com.zufangbao.sun.yunxin.entity.model.repurchase.RepurchaseQueryModel;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplicationQueryModel;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlanExecLogQueryModel;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlanQueryModel;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceRefundBillQueryModel;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittancePlanExecLogService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittancePlanService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceRefundBillService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional()
@WebAppConfiguration(value="webapp")
public class AmountStatisticsTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	private IRemittanceApplicationService remittanceApplicationService;
	@Autowired
	private IRemittancePlanService remittancePlanService;
	@Autowired
	private IRemittancePlanExecLogService remittancePlanExecLogService;
	@Autowired
	private IRemittanceRefundBillService remittanceRefundBillService;
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private TransferApplicationService transferApplicationService;
	@Autowired
	private YunxinOfflinePaymentHandler yunxinOfflinePaymentHandler;
	@Autowired
	private DeductApplicationCoreOperationHandler deductApplicationHandler;
	@Autowired
	private RepurchaseService repurchaseService;
	@Autowired
	private RepaymentPlanHandler repaymentPlanHandler;
	
	//计划订单
	@Test
	@Sql("classpath:test/yunxin/testAmountStatistics.sql")
	public void testRemittanceApplicationService(){
		RemittanceApplicationQueryModel queryModel = new RemittanceApplicationQueryModel();
		Map<String, Object> result = remittanceApplicationService.amountStatistics(queryModel);
		Assert.assertEquals(Collections.EMPTY_MAP, result);
		
		queryModel = new RemittanceApplicationQueryModel();
		queryModel.setOrderStatus("[6,7]");
		result = remittanceApplicationService.amountStatistics(queryModel);
		Assert.assertEquals(Collections.EMPTY_MAP, result);
		
		queryModel = new RemittanceApplicationQueryModel();
		queryModel.setFinancialContractIds("[1,2]");
		queryModel.setOrderStatus("[2,3]");
		result = remittanceApplicationService.amountStatistics(queryModel);
		Assert.assertEquals(Collections.EMPTY_MAP, result);
		
		queryModel = new RemittanceApplicationQueryModel();
		queryModel.setFinancialContractIds("[1]");
		queryModel.setOrderStatus("[2,3]");
		result = remittanceApplicationService.amountStatistics(queryModel);
		Assert.assertEquals(new BigDecimal("0.07"), result.get("plannedAmount"));
		Assert.assertEquals(new BigDecimal("0.01"), result.get("actualAmount"));
		Assert.assertEquals(new BigDecimal("0.06"), result.get("differenceAmount"));
		
		queryModel = new RemittanceApplicationQueryModel();
		queryModel.setFinancialContractIds("[3]");
		queryModel.setOrderStatus("[2,3]");
		result = remittanceApplicationService.amountStatistics(queryModel);
		Assert.assertEquals(BigDecimal.ZERO, result.get("plannedAmount"));
		Assert.assertEquals(BigDecimal.ZERO, result.get("actualAmount"));
		Assert.assertEquals(BigDecimal.ZERO, result.get("differenceAmount"));
	}
	
	//放款单
	@Test
	@Sql("classpath:test/yunxin/testAmountStatistics.sql")
	public void testRemittancePlanService(){
		RemittancePlanQueryModel queryModel = new RemittancePlanQueryModel();
		Map<String, Object> result = remittancePlanService.amountStatistics(queryModel);
		Assert.assertEquals(Collections.EMPTY_MAP, result);
		
		queryModel = new RemittancePlanQueryModel();
		queryModel.setFinancialContractUuids("[\"db36ecc9-d80c-4350-bd0d-59b1139d550d\",\"db36ecc9-d80c-4350-bd0d-59b1139d550d\"]");
		result = remittancePlanService.amountStatistics(queryModel);
		Assert.assertEquals(Collections.EMPTY_MAP, result);
		
		queryModel = new RemittancePlanQueryModel();
		queryModel.setFinancialContractUuids("[\"db36ecc9-d80c-4350-bd0d-59b1139d550d\"]");
		result = remittancePlanService.amountStatistics(queryModel);
		Assert.assertEquals(new BigDecimal("0.02"), result.get("plannedAmount"));
		
		queryModel = new RemittancePlanQueryModel();
		queryModel.setFinancialContractUuids("[\"ab36ecc9-d80c-4350-bd0d-59b1139d550d\"]");
		result = remittancePlanService.amountStatistics(queryModel);
		Assert.assertEquals(BigDecimal.ZERO, result.get("plannedAmount"));
	}
	
	//线上代付单
	@Test
	@Sql("classpath:test/yunxin/testAmountStatistics.sql")
	public void testRemittancePlanExecLogService(){
		RemittancePlanExecLogQueryModel queryModel = new RemittancePlanExecLogQueryModel();
		Map<String, Object> result = remittancePlanExecLogService.amountStatistics(queryModel);
		Assert.assertEquals(Collections.EMPTY_MAP, result);
		
		queryModel = new RemittancePlanExecLogQueryModel();
		queryModel.setFinancialContractUuids("[\"b674a323-0c30-4e4b-9eba-b14e05a9d80a\",\"a674a323-0c30-4e4b-9eba-b14e05a9d80a\"]");
		queryModel.setReverseStatusOrdinals("[\"0\"]");
		queryModel.setHasProcessing(1);
		result = remittancePlanExecLogService.amountStatistics(queryModel);
		Assert.assertEquals(Collections.EMPTY_MAP, result);
		
		queryModel = new RemittancePlanExecLogQueryModel();
		queryModel.setFinancialContractUuids("[\"b674a323-0c30-4e4b-9eba-b14e05a9d80a\"]");
		queryModel.setReverseStatusOrdinals("[\"0\"]");
		queryModel.setHasProcessing(1);
		result = remittancePlanExecLogService.amountStatistics(queryModel);
		Assert.assertEquals(new BigDecimal("0.07"), result.get("remittanceAmount"));
		
		queryModel = new RemittancePlanExecLogQueryModel();
		queryModel.setFinancialContractUuids("[\"a674a323-0c30-4e4b-9eba-b14e05a9d80a\"]");
		queryModel.setReverseStatusOrdinals("[\"0\"]");
		queryModel.setHasProcessing(1);
		result = remittancePlanExecLogService.amountStatistics(queryModel);
		Assert.assertEquals(BigDecimal.ZERO, result.get("remittanceAmount"));
	}
	
	//代付撤销单
	@Test
	@Sql("classpath:test/yunxin/testAmountStatistics.sql")
	public void testRemittanceRefundBillService(){
		RemittanceRefundBillQueryModel queryModel = new RemittanceRefundBillQueryModel();
		Map<String, Object> result = remittanceRefundBillService.amountStatistics(queryModel);
		Assert.assertEquals(Collections.EMPTY_MAP, result);
		
		queryModel = new RemittanceRefundBillQueryModel();
		queryModel.setFinancialContractUuids("[\"d2812bc5-5057-4a91-b3fd-9019506f0499\",\"a2812bc5-5057-4a91-b3fd-9019506f0499\"]");
		queryModel.setPaymentInstitutionOrdinals("[\"0\",\"1\",\"2\"]");
		result = remittanceRefundBillService.amountStatistics(queryModel);
		Assert.assertEquals(Collections.EMPTY_MAP, result);
		
		queryModel = new RemittanceRefundBillQueryModel();
		queryModel.setFinancialContractUuids("[\"d2812bc5-5057-4a91-b3fd-9019506f0499\"]");
		queryModel.setPaymentInstitutionOrdinals("[\"0\",\"1\",\"2\"]");
		result = remittanceRefundBillService.amountStatistics(queryModel);
		Assert.assertEquals(new BigDecimal("0.02"), result.get("refundAmount"));
		
		queryModel = new RemittanceRefundBillQueryModel();
		queryModel.setFinancialContractUuids("[\"a2812bc5-5057-4a91-b3fd-9019506f0499\"]");
		queryModel.setPaymentInstitutionOrdinals("[\"0\",\"1\",\"2\"]");
		result = remittanceRefundBillService.amountStatistics(queryModel);
		Assert.assertEquals(BigDecimal.ZERO, result.get("refundAmount"));
	}
	
	//还款单
	@Test
	@Sql("classpath:test/yunxin/testAmountStatistics.sql")
	public void  testRepaymentPlanService(){
		AssetSetQueryModel queryModel = new AssetSetQueryModel();
		Map<String, Object> result = repaymentPlanHandler.amountStatistics(queryModel);
		Assert.assertEquals(Collections.EMPTY_MAP, result);
		
		queryModel = new AssetSetQueryModel();
		queryModel.setFinancialContractIds("[\"1\",\"2\"]");
		queryModel.setPaymentStatusOrdinals("[\"0\"]");
		queryModel.setAuditOverDueStatusOrdinals("[\"0\",\"1\",\"2\"]");
		result = repaymentPlanHandler.amountStatistics(queryModel);
		Assert.assertEquals(Collections.EMPTY_MAP, result);
		
		queryModel = new AssetSetQueryModel();
		queryModel.setFinancialContractIds("[\"3\"]");
		queryModel.setPaymentStatusOrdinals("[\"2\"]");
		queryModel.setAuditOverDueStatusOrdinals("[\"0\",\"1\",\"2\"]");
		result = repaymentPlanHandler.amountStatistics(queryModel);
		Assert.assertEquals(Collections.EMPTY_MAP, result);
		
		queryModel = new AssetSetQueryModel();
		queryModel.setFinancialContractIds("[\"1\"]");
		queryModel.setPaymentStatusOrdinals("[\"3\"]");
		queryModel.setAuditOverDueStatusOrdinals("[\"0\",\"1\",\"2\"]");
		result = repaymentPlanHandler.amountStatistics(queryModel);
		Assert.assertEquals(new BigDecimal("4000.60"), result.get("plannedAmount"));
		Assert.assertEquals(new BigDecimal("0"), result.get("fullAmount"));
		Assert.assertEquals(new BigDecimal("4000.60"), result.get("differenceAmount"));
		
		queryModel = new AssetSetQueryModel();
		queryModel.setFinancialContractIds("[\"2\"]");
		queryModel.setPaymentStatusOrdinals("[\"2\"]");
		queryModel.setAuditOverDueStatusOrdinals("[\"0\",\"1\",\"2\"]");
		result = repaymentPlanHandler.amountStatistics(queryModel);
		Assert.assertEquals(BigDecimal.ZERO, result.get("plannedAmount"));
		Assert.assertEquals(BigDecimal.ZERO, result.get("fullAmount"));
		Assert.assertEquals(BigDecimal.ZERO, result.get("differenceAmount"));
		}
	
	//还款单
	@Test
	@Sql("classpath:test/yunxin/testAmountStatistics.sql")
	public void  testOrderService(){
		OrderQueryModel queryModel = new OrderQueryModel();
		Map<String, Object> result = orderService.paymentAmountStatistics(queryModel);
		Assert.assertEquals(Collections.EMPTY_MAP, result);
		
		queryModel = new OrderQueryModel();
		queryModel.setFinancialContractIds("[\"1\",\"2\"]");
		result = orderService.paymentAmountStatistics(queryModel);
		Assert.assertEquals(Collections.EMPTY_MAP, result);
		
		queryModel = new OrderQueryModel();
		queryModel.setFinancialContractIds("[\"3\"]");
		result = orderService.paymentAmountStatistics(queryModel);
		Assert.assertEquals(BigDecimal.ZERO, result.get("plannedAmount"));
		Assert.assertEquals(BigDecimal.ZERO, result.get("penaltyAmount"));
		Assert.assertEquals(BigDecimal.ZERO, result.get("paymentAmount"));
		
		queryModel = new OrderQueryModel();
		queryModel.setFinancialContractIds("[\"1\"]");
		result = orderService.paymentAmountStatistics(queryModel);
		Assert.assertEquals(new BigDecimal("4401.00"), result.get("plannedAmount"));
		Assert.assertEquals(new BigDecimal("405.90"), result.get("penaltyAmount"));
		Assert.assertEquals(new BigDecimal("4806.90"), result.get("paymentAmount"));
		
		queryModel = new OrderQueryModel();
		queryModel.setFinancialContractIds("[\"2\"]");
		result = orderService.paymentAmountStatistics(queryModel);
		Assert.assertEquals(BigDecimal.ZERO, result.get("plannedAmount"));
		Assert.assertEquals(BigDecimal.ZERO, result.get("penaltyAmount"));
		Assert.assertEquals(BigDecimal.ZERO, result.get("paymentAmount"));
	}
	
	//线上支付单
	@Test
	@Sql("classpath:test/yunxin/testAmountStatistics.sql")
	public void  testTransferApplicationService(){
		TransferApplicationQueryModel queryModel = new TransferApplicationQueryModel();
		Map<String, Object> result = transferApplicationService.amountStatistics(queryModel);
		Assert.assertEquals(Collections.EMPTY_MAP, result);
		
		queryModel = new TransferApplicationQueryModel();
		queryModel.setFinancialContractIds("[\"1\",\"2\"]");
		result = transferApplicationService.amountStatistics(queryModel);
		Assert.assertEquals(Collections.EMPTY_MAP, result);
		
		queryModel = new TransferApplicationQueryModel();
		queryModel.setFinancialContractIds("[\"3\"]");
		result = transferApplicationService.amountStatistics(queryModel);
		Assert.assertEquals(BigDecimal.ZERO, result.get("transferAmount"));
		
		queryModel = new TransferApplicationQueryModel();
		queryModel.setFinancialContractIds("[\"1\"]");
		result = transferApplicationService.amountStatistics(queryModel);
		Assert.assertEquals(new BigDecimal("2407.20"), result.get("transferAmount"));
		
		queryModel = new TransferApplicationQueryModel();
		queryModel.setFinancialContractIds("[\"2\"]");
		result = transferApplicationService.amountStatistics(queryModel);
		Assert.assertEquals(BigDecimal.ZERO, result.get("transferAmount"));
	}
	//线下支付单
	@Test
	@Sql("classpath:test/yunxin/testAmountStatistics.sql")
	public void  testYunxinOfflinePaymentHandler(){
		OfflineBillQueryModel queryModel = new OfflineBillQueryModel();
		Map<String, Object> result = yunxinOfflinePaymentHandler.amountStatistics(queryModel);
		Assert.assertEquals(Collections.EMPTY_MAP, result);
		
		queryModel = new OfflineBillQueryModel();
		queryModel.setFinancialContractUuids("[\"2d380fe1-7157-490d-9474-12c5a9901e29\",\"92846f20-87e3-49f4-8f90-fe04a72c0484\"]");
		result = yunxinOfflinePaymentHandler.amountStatistics(queryModel);
		Assert.assertEquals(Collections.EMPTY_MAP, result);
		
		queryModel = new OfflineBillQueryModel();
		queryModel.setFinancialContractUuids("[\"c2812bc5-5057-4a91-b3fd-9019506f0499\"]");
		result = yunxinOfflinePaymentHandler.amountStatistics(queryModel);
		Assert.assertEquals(BigDecimal.ZERO, result.get("paymentAmount"));
		
		queryModel = new OfflineBillQueryModel();
		queryModel.setFinancialContractUuids("[\"2d380fe1-7157-490d-9474-12c5a9901e29\"]");
		result = yunxinOfflinePaymentHandler.amountStatistics(queryModel);
		Assert.assertEquals(new BigDecimal("8910.12"), result.get("paymentAmount"));
		
		queryModel = new OfflineBillQueryModel();
		queryModel.setFinancialContractUuids("[\"92846f20-87e3-49f4-8f90-fe04a72c0484\"]");
		result = yunxinOfflinePaymentHandler.amountStatistics(queryModel);
		Assert.assertEquals(BigDecimal.ZERO, result.get("paymentAmount"));
	}
	
	//扣款订单
	@Test
	@Sql("classpath:test/yunxin/testAmountStatistics.sql")
	public void  testDeductApplicationHandler(){
		DeductApplicationQeuryModel queryModel = new DeductApplicationQeuryModel();
		Map<String, Object> result = deductApplicationHandler.amountStatistics(queryModel);
		Assert.assertEquals(Collections.EMPTY_MAP, result);
		
		queryModel = new DeductApplicationQeuryModel();
		queryModel.setFinancialContractIds("[\"2d380fe1-7157-490d-9474-12c5a9901e29\",\"92846f20-87e3-49f4-8f90-fe04a72c0484\"]");
		queryModel.setExecutionStatus("[\"0\",\"1\",\"2\"]");
		queryModel.setRepaymentType("[\"0\",\"1\",\"2\"]");
		result = deductApplicationHandler.amountStatistics(queryModel);
		Assert.assertEquals(Collections.EMPTY_MAP, result);
		
		queryModel = new DeductApplicationQeuryModel();
		queryModel.setFinancialContractIds("[\"wd380fe1-7157-490d-9474-12c5a9901e29\"]");
		queryModel.setExecutionStatus("[\"0\",\"1\",\"2\"]");
		queryModel.setRepaymentType("[\"0\",\"1\",\"2\"]");
		result = deductApplicationHandler.amountStatistics(queryModel);
		Assert.assertEquals(BigDecimal.ZERO, result.get("plannedAmount"));
		Assert.assertEquals(BigDecimal.ZERO, result.get("actualAmount"));
		Assert.assertEquals(BigDecimal.ZERO, result.get("differenceAmount"));
		
		queryModel = new DeductApplicationQeuryModel();
		queryModel.setFinancialContractIds("[\"2d380fe1-7157-490d-9474-12c5a9901e29\"]");
		queryModel.setExecutionStatus("[\"0\",\"1\",\"2\"]");
		queryModel.setRepaymentType("[\"0\",\"1\",\"2\"]");
		result = deductApplicationHandler.amountStatistics(queryModel);
		Assert.assertEquals(new BigDecimal("0.06"), result.get("plannedAmount"));
		Assert.assertEquals(new BigDecimal("0.02"), result.get("actualAmount"));
		Assert.assertEquals(new BigDecimal("0.04"), result.get("differenceAmount"));
		
		queryModel = new DeductApplicationQeuryModel();
		queryModel.setFinancialContractIds("[\"92846f20-87e3-49f4-8f90-fe04a72c0484\"]");
		queryModel.setExecutionStatus("[\"0\",\"1\",\"2\"]");
		queryModel.setRepaymentType("[\"0\",\"1\",\"2\"]");
		result = deductApplicationHandler.amountStatistics(queryModel);
		Assert.assertEquals(BigDecimal.ZERO, result.get("plannedAmount"));
		Assert.assertEquals(BigDecimal.ZERO, result.get("actualAmount"));
		Assert.assertEquals(BigDecimal.ZERO, result.get("differenceAmount"));
	}
	
	//商户担保
	@Test
	@Sql("classpath:test/yunxin/testAmountStatistics.sql")
	public void  testOrderService_guarantee(){
		GuaranteeOrderModel queryModel = new GuaranteeOrderModel();
		Map<String, Object> result = orderService.guaranteeAmountStatistics(queryModel);
		Assert.assertEquals(Collections.EMPTY_MAP, result);
		
		queryModel = new GuaranteeOrderModel();
		queryModel.setFinancialContractIds("[\"1\",\"2\"]");
		result = orderService.guaranteeAmountStatistics(queryModel);
		Assert.assertEquals(Collections.EMPTY_MAP, result);
		
		queryModel = new GuaranteeOrderModel();
		queryModel.setFinancialContractIds("[\"3\"]");
		result = orderService.guaranteeAmountStatistics(queryModel);
		Assert.assertEquals(BigDecimal.ZERO, result.get("rentAmount"));
		
		queryModel = new GuaranteeOrderModel();
		queryModel.setFinancialContractIds("[\"1\"]");
		result = orderService.guaranteeAmountStatistics(queryModel);
		Assert.assertEquals(new BigDecimal("4806.90"), result.get("rentAmount"));
		
		queryModel = new GuaranteeOrderModel();
		queryModel.setFinancialContractIds("[\"2\"]");
		result = orderService.guaranteeAmountStatistics(queryModel);
		Assert.assertEquals(BigDecimal.ZERO, result.get("rentAmount"));
	}
		
	//回购单
	@Test
	@Sql("classpath:test/yunxin/testAmountStatistics.sql")
	public void  testRepurchaseService(){
		RepurchaseQueryModel queryModel = new RepurchaseQueryModel();
		Map<String, Object> result = repurchaseService.amountStatistics(queryModel);
		Assert.assertEquals(Collections.EMPTY_MAP, result);
		
		queryModel = new RepurchaseQueryModel();
		queryModel.setFinancialContractUuids("[\"2d380fe1-7157-490d-9474-12c5a9901e29\",\"92846f20-87e3-49f4-8f90-fe04a72c0484\"]");
		result = repurchaseService.amountStatistics(queryModel);
		Assert.assertEquals(Collections.EMPTY_MAP, result);
		
		queryModel = new RepurchaseQueryModel();
		queryModel.setFinancialContractUuids("[\"wd380fe1-7157-490d-9474-12c5a9901e29\"]");
		result = repurchaseService.amountStatistics(queryModel);
		Assert.assertEquals(BigDecimal.ZERO, result.get("repurchaseAmount"));
		
		queryModel = new RepurchaseQueryModel();
		queryModel.setFinancialContractUuids("[\"2d380fe1-7157-490d-9474-12c5a9901e29\"]");
		result = repurchaseService.amountStatistics(queryModel);
		Assert.assertEquals(new BigDecimal("5004.00"), result.get("repurchaseAmount"));
		
		queryModel = new RepurchaseQueryModel();
		queryModel.setFinancialContractUuids("[\"92846f20-87e3-49f4-8f90-fe04a72c0484\"]");
		result = repurchaseService.amountStatistics(queryModel);
		Assert.assertEquals(BigDecimal.ZERO, result.get("repurchaseAmount"));
	}
	
}
