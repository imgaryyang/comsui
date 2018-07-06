package com.suidifu.bridgewater.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.suidifu.bridgewater.api.model.ModifyRemittanceApplicationModel;
import com.suidifu.bridgewater.handler.impl.RemittanceApplicationHandlerImpl;
import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import com.zufangbao.sun.api.model.remittance.RemittanceCommandModel;
import com.zufangbao.sun.api.model.remittance.RemittanceDetail;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplication;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplicationDetail;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlanExecLog;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationDetailService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittancePlanExecLogService;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** 
* @author 作者 zhenghangbo
* @version 创建时间：Dec 5, 2016 11:00:07 AM 
* 类说明 
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/context/applicationContext-*.xml",
		"classpath:/local/applicationContext-*.xml" })
@Transactional
public class RemittanceApplicationHandlerTest {
	
	@Autowired
	private IRemittanceApplicationHandler remittancetApplicationHandler;

	@Autowired
	private IRemittancePlanExecLogService iRemittancePlanExecLogService;

	@Autowired
	private IRemittanceApplicationDetailService iRemittanceApplicationDetailService;

//	@Autowired
//    private RemittanceApplicationHandlerImpl remittancetApplicationHandler;
	
	@Test
	@Sql("classpath:/test/remittance/test_getRemittanceApplicationsBy.sql")
	public void testGetRemittanceApplicationsByUniqueId() {
		String contractUniqueId = "test_contract_unique_id";
		String contractNo = "";
		List<RemittanceApplication> remittanceApplications = remittancetApplicationHandler.getRemittanceApplicationsBy(contractUniqueId,contractNo);
		Assert.assertFalse(CollectionUtils.isEmpty(remittanceApplications));
	}
	
	@Test
	@Sql("classpath:/test/remittance/test_getRemittanceApplicationsBy.sql")
	public void testGetRemittanceApplicationsByContractNo() {
		String contractUniqueId = "";
		String contractNo = "test_contract_no";
		List<RemittanceApplication> remittanceApplications = remittancetApplicationHandler.getRemittanceApplicationsBy(contractUniqueId,contractNo);
		Assert.assertFalse(CollectionUtils.isEmpty(remittanceApplications));
	}
	
	@Test
	@Sql("classpath:/test/remittance/test_getRemittanceApplicationsBy.sql")
	public void testGetRemittanceApplicationsBy_emptyParams() {
		String contractUniqueId = "";
		String contractNo = "";
		List<RemittanceApplication> remittanceApplications = remittancetApplicationHandler.getRemittanceApplicationsBy(contractUniqueId,contractNo);
		Assert.assertTrue(CollectionUtils.isEmpty(remittanceApplications));
	}
	
	@Test
	@Sql("classpath:/test/remittance/test_getRemittanceApplicationsBy.sql")
	public void testGetRemittanceApplicationsBy_noResult() {
		String contractUniqueId = "1234";
		String contractNo = "";
		List<RemittanceApplication> remittanceApplications = remittancetApplicationHandler.getRemittanceApplicationsBy(contractUniqueId,contractNo);
		Assert.assertTrue(CollectionUtils.isEmpty(remittanceApplications));
	}

	@Test
	@Sql("classpath:test/remittance/test4RevokeFailRemittancePlanThenSaveInfo.sql")
	public void test_revokeFailRemittancePlanThenSaveInfo(){

		String requestNo = UUID.randomUUID().toString().replace("-","");
		String tradeNo = UUID.randomUUID().toString().replace("-","");
		String financialProductCode = "G31700";
		String remittanceTradeNo = "9cde18e1-b926-4816-81bc-62b3cc0458fb";

		ModifyRemittanceApplicationModel model = new ModifyRemittanceApplicationModel();

		model.setRequestNo(requestNo);
		model.setTradeNo(tradeNo);
		model.setFinancialProductCode(financialProductCode);
		model.setRemittanceTradeNo(remittanceTradeNo);

		List<RemittanceDetail> remittanceDetails = new ArrayList<RemittanceDetail>();
		RemittanceDetail detail = new RemittanceDetail();
		detail.setDetailNo("detailNo1");
		detail.setRecordSn("1");
		detail.setAmount("1500");
		detail.setPlannedDate("2996-08-20 00: 00: 00");
		detail.setBankCode("C10102");
		detail.setBankCardNo("6214855712106520");
		detail.setBankAccountHolder("测试用户1");
		detail.setBankProvince("bankProvince1");
		detail.setBankCity("bankCity1");
		detail.setBankName("bankName1");
		detail.setIdNumber("idNumber1");
		remittanceDetails.add(detail);
		model.setRemittanceDetails(JSON.toJSONString(remittanceDetails, SerializerFeature.DisableCircularReferenceDetect));

		model.setApprover("1324");
		model.setApprovedTime(DateUtils.today());
		model.setComment("beizhu");
		remittancetApplicationHandler.revokeFailRemittancePlanThenSaveInfo(model,"127.0.0.1","tester");

		List<RemittanceApplicationDetail> remittanceApplicationDetails = iRemittanceApplicationDetailService.loadAll(RemittanceApplicationDetail.class);
		Assert.assertEquals(1, remittanceApplicationDetails.size());
		RemittanceApplicationDetail detail1 = remittanceApplicationDetails.get(0);
		Assert.assertEquals("f9da5a7f-05d0-415a-8b94-8d1d9651a3bc",detail1.getRemittanceApplicationUuid());
		Assert.assertEquals("d2812bc5-5057-4a91-b3fd-9019506f0499",detail1.getFinancialContractUuid());
		Assert.assertEquals(detail1.getPlannedTotalAmount().compareTo(new BigDecimal("1500.00")),0);

		List<RemittancePlanExecLog> remittancePlanExecLogs = iRemittancePlanExecLogService.loadAll(RemittancePlanExecLog.class);
		Assert.assertEquals(1, remittancePlanExecLogs.size());
		RemittancePlanExecLog execLog = remittancePlanExecLogs.get(0);
		Assert.assertEquals(execLog.getPlannedAmount().compareTo(new BigDecimal("1500.00")),0);
	}

	@Test
	public void validateRemittanceDetailsBankCodeTest() {
		RemittanceCommandModel remittanceCommandModel = new RemittanceCommandModel();
		RemittanceDetail remittanceDetail = new RemittanceDetail();
		remittanceDetail.setBankCardNo("6225840100581979");
		List<RemittanceDetail> remittanceDetailList = new ArrayList<>();
		remittanceDetailList.add(remittanceDetail);
		remittanceCommandModel.setRemittanceDetailList(remittanceDetailList);
		RemittanceApplicationHandlerImpl remittanceApplicationHandlerImpl = (RemittanceApplicationHandlerImpl)
				remittancetApplicationHandler;

//        remittanceApplicationHandlerImpl.validateRemittanceDetailsBankCode(remittanceCommandModel);
	}



}

