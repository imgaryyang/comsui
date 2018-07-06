package com.zufangbao.earth.yunxin.handler.impl;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.zufangbao.earth.yunxin.api.handler.ThirdPartVoucherFrontEndShowHandler;
import com.zufangbao.gluon.api.swissre.institutionrecon.ThirdPartVoucherRepayDetailModelCopy;
import com.zufangbao.sun.yunxin.entity.remittance.ExecutionStatus;
import com.zufangbao.sun.yunxin.entity.thirdPartVoucher.ThirdPartyPaymentVoucher;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.ThirdPartyPaymentVoucherHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.ThirdPartyPaymentVoucherService;
import com.zufangbao.wellsfargo.thirdpartvoucher.model.ThirdPartVoucherQueryModel;
import com.zufangbao.wellsfargo.thirdpartvoucher.model.ThirdPartVoucherQueryShowModel;
import com.zufangbao.wellsfargo.yunxin.model.ThirdPartyVoucherCommandLogShowModel;

/** 
* @author 作者 zhenghangbo
* @version 创建时间：Nov 26, 2016 7:46:14 PM 
* 类说明 
*/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class ThirdPartVoucherHandlerTest {

	@Autowired
	private ThirdPartVoucherFrontEndShowHandler thirdPartVoucherHandler;
	
	@Autowired
	private ThirdPartyPaymentVoucherHandler thirdPartyPaymentVoucherHandler;
	
	@Autowired
	private ThirdPartyPaymentVoucherService thirdPartyPaymentVoucherService;
	
	
	
	@Test
	@Sql("classpath:test/yunxin/thirdPartVoucher/testTestGetThirdPartVoucherShowModel.sql")
	public void testTestGetThirdPartVoucherShowModel(){
		
		ThirdPartVoucherQueryModel queryModel = new ThirdPartVoucherQueryModel();
		queryModel.setFinancialContractUuids("[\"d2812bc5-5057-4a91-b3fd-9019506f0499\"]");
		List<ThirdPartVoucherQueryShowModel> showModels = thirdPartVoucherHandler.getThirdPartVoucherShowModel(queryModel,null);
		assertEquals(0, showModels.size());
	}
	
	@Test
	@Sql("classpath:test/yunxin/thirdPartVoucher/testTestGetThirdPartVoucherShowModelNormal.sql")
	public void testTestGetThirdPartVoucherShowModelNormal(){
		
		ThirdPartVoucherQueryModel queryModel = new ThirdPartVoucherQueryModel();
		queryModel.setFinancialContractUuids("[\"d2812bc5-5057-4a91-b3fd-9019506f0499\"]");
		queryModel.setPaymentChannel("[0,4]");
		queryModel.setDeductVoucherSource("[0,1]");
		queryModel.setRepaymentType("[0,1,2]");
		queryModel.setVoucherStatus("[0,1]");
		List<ThirdPartVoucherQueryShowModel> showModels = thirdPartVoucherHandler.getThirdPartVoucherShowModel(queryModel,null);
		assertEquals(1, showModels.size());
		
		ThirdPartVoucherQueryShowModel showModel = showModels.get(0);
		assertEquals("提前划扣", showModel.getDeductType());
		assertEquals("6217000000000000000", showModel.getPayerBankAccountNo());
		assertEquals("account_account_name", showModel.getPayerBankName());
		assertEquals("测试员1", showModel.getPayerName());
		assertEquals("", showModel.getSettlementNo());
		assertEquals("600000000001", showModel.getSpecialAccountNo());
		assertEquals("宝付", showModel.getThirdPartChannel());
		assertEquals("704.00", showModel.getVoucherAmount());
		assertEquals("a8112079-5dd2-47e0-ad1f-c1351bc94166", showModel.getVoucherCode());
		assertEquals("系统线上支付单", showModel.getVoucherSource());
		assertEquals("已核销", showModel.getVoucherStatus());
	}
	
	
	@Test
	@Sql("classpath:test/yunxin/thirdPartVoucher/testTestGetThirdPartVoucherShowModelNormal_allParams.sql")
	public void testTestGetThirdPartVoucherShowModelNormal_allParams(){
		
		ThirdPartVoucherQueryModel queryModel = new ThirdPartVoucherQueryModel();
		queryModel.setFinancialContractUuids("[\"d2812bc5-5057-4a91-b3fd-9019506f0499\"]");
		queryModel.setPaymentChannel("[0,4]");
		queryModel.setDeductVoucherSource("[0,1]");
		queryModel.setRepaymentType("[0,1,2]");
		queryModel.setVoucherStatus("[0,1]");
		queryModel.setPayerName("测试员1");
		queryModel.setSpecialAccountNo("600000000001");
		queryModel.setPayerBankAccountNo("");
		List<ThirdPartVoucherQueryShowModel> showModels = thirdPartVoucherHandler.getThirdPartVoucherShowModel(queryModel,null);
		assertEquals(1, showModels.size());
		
		ThirdPartVoucherQueryShowModel showModel = showModels.get(0);
		assertEquals("提前划扣", showModel.getDeductType());
		assertEquals("6217000000000000000", showModel.getPayerBankAccountNo());
		assertEquals("account_account_name", showModel.getPayerBankName());
		assertEquals("测试员1", showModel.getPayerName());
		assertEquals("", showModel.getSettlementNo());
		assertEquals("600000000001", showModel.getSpecialAccountNo());
		assertEquals("宝付", showModel.getThirdPartChannel());
		assertEquals("704.00", showModel.getVoucherAmount());
		assertEquals("a8112079-5dd2-47e0-ad1f-c1351bc94166", showModel.getVoucherCode());
		assertEquals("系统线上支付单", showModel.getVoucherSource());
		assertEquals("已核销", showModel.getVoucherStatus());
	}
	
	@Test
	@Sql("classpath:test/yunxin/thirdPartVoucher/third_party_voucher_command_log_service2.sql")
	public void getThirdPartyVoucherCommandLogShowModel2Test() {
		
		String voucherUuid = "b4fc7a3c-67e9-498a-ad57-3c4c1056787f";
		
		ThirdPartyPaymentVoucher paymentVoucher = thirdPartyPaymentVoucherService.getThirdPartyPaymentVoucherByVoucherUuid(voucherUuid);
		
		ThirdPartyVoucherCommandLogShowModel result = thirdPartyPaymentVoucherHandler.getThirdPartyVoucherCommandLogShowModel(voucherUuid);
		
		assertEquals(voucherUuid, result.getVoucherUuid());
		assertEquals(paymentVoucher.getTranscationAmount(), result.getTranscationAmount());
		assertEquals("2017-12-11 15:54:21", result.getCreateTime());
		assertEquals("customer_name", result.getCustomerName());
		assertEquals("199b8c74-c905-46c8-a79e-c02409374def", result.getDeductPlanUuid());
		assertEquals(paymentVoucher.getVoucherNo(), result.getDeductId());
		assertEquals("2017-12-11 15:54:21", result.getStatusModifyTime());
		
		assertEquals(null, result.getBankTransactionNo());
		assertEquals(paymentVoucher.getTradeUuid(), result.getChannelRequestNumber());
		assertEquals(paymentVoucher.getContractUniqueId(), result.getContractUniqueId());
		assertEquals(null, result.getExecutionRemark());
		assertEquals(ExecutionStatus.SUCCESS.getChineseMessage(), result.getExecutionStatus());
		assertEquals(paymentVoucher.getFinancialContractNo(), result.getFinancialContractNo());
		assertEquals(paymentVoucher.getFinancialContractUuid(), result.getFinancialContractUuid());
		assertEquals(paymentVoucher.getPaymentAccountNo(), result.getPaymentAccountNo());
		assertEquals(paymentVoucher.getPaymentBankName(), result.getPaymentBank());
		assertEquals(paymentVoucher.getPaymentName(), result.getPaymentName());
		assertEquals(new BigDecimal("2000.00"), result.getPlannedTotalAmount());
		assertEquals(paymentVoucher.getReceivableAccountNo(), result.getReceivableAccountNo());
		assertEquals(paymentVoucher.getTradeUuid(), result.getTradeUuid());
		
		assertEquals(paymentVoucher.getTranscationGateway().getChineseMessage(), result.getTranscationGateway());
		assertEquals(paymentVoucher.getVoucherLogIssueStatus().getChineseMessage(), result.getVoucherLogIssueStatus());
		assertEquals("校验成功", result.getVoucherLogStatus());
		assertEquals(paymentVoucher.getVoucherNo(), result.getVoucherNo());
		assertEquals(paymentVoucher.getVoucherSource().getChineseMessage(), result.getVoucherSource());
		
		assertEquals(2, result.getRepayDetails().size());
		ThirdPartVoucherRepayDetailModelCopy detailModelCopy1 = result.getRepayDetails().get(0);
		ThirdPartVoucherRepayDetailModelCopy detailModelCopy2 = result.getRepayDetails().get(1);
		
		assertEquals(new BigDecimal("1000.00"), detailModelCopy1.getAmount());
		assertEquals("2017-12-31 00:00:00", detailModelCopy1.getAssetRecycleDate());
		assertEquals(new Long("1"), detailModelCopy1.getId());
		assertEquals(new BigDecimal("200"), detailModelCopy1.getInterest());
		assertEquals(new BigDecimal("0"), detailModelCopy1.getLateFee());
		assertEquals(new BigDecimal("0"), detailModelCopy1.getLateOtherCost());
		assertEquals(new BigDecimal("0"), detailModelCopy1.getLatePenalty());
		assertEquals(new BigDecimal("0"), detailModelCopy1.getMaintenanceCharge());
		assertEquals(new BigDecimal("0"), detailModelCopy1.getOtherCharge());
		assertEquals("outer_repayment_plan_no_1", detailModelCopy1.getOuterRepaymentPlanNo());
		assertEquals(new BigDecimal("0"), detailModelCopy1.getPenaltyFee());
		assertEquals(new BigDecimal("800"), detailModelCopy1.getPrincipal());
		assertEquals("ZC133104659475144704", detailModelCopy1.getRepaymentPlanNo());
		assertEquals(new BigDecimal("0"), detailModelCopy1.getServiceCharge());
		assertEquals(new BigDecimal("0"), detailModelCopy1.getSundryAmount());
		
		assertEquals(new BigDecimal("1000.00"), detailModelCopy2.getAmount());
		assertEquals("2017-11-30 00:00:00", detailModelCopy2.getAssetRecycleDate());
		assertEquals(new Long("2"), detailModelCopy2.getId());
		assertEquals(new BigDecimal("0"), detailModelCopy2.getInterest());
		assertEquals(new BigDecimal("0"), detailModelCopy2.getLateFee());
		assertEquals(new BigDecimal("0"), detailModelCopy2.getLateOtherCost());
		assertEquals(new BigDecimal("0"), detailModelCopy2.getLatePenalty());
		assertEquals(new BigDecimal("0"), detailModelCopy2.getMaintenanceCharge());
		assertEquals(new BigDecimal("0"), detailModelCopy2.getOtherCharge());
		assertEquals("outer_repayment_plan_no_2", detailModelCopy2.getOuterRepaymentPlanNo());
		assertEquals(new BigDecimal("0"), detailModelCopy2.getPenaltyFee());
		assertEquals(new BigDecimal("700"), detailModelCopy2.getPrincipal());
		assertEquals("ZC133104659449978880", detailModelCopy2.getRepaymentPlanNo());
		assertEquals(new BigDecimal("300"), detailModelCopy2.getServiceCharge());
		assertEquals(new BigDecimal("0"), detailModelCopy2.getSundryAmount());
	}
	
	
}
