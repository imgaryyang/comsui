package com.suidifu.bridgewater.api.model;

import com.zufangbao.sun.api.model.remittance.RemittanceCommandModel;
import org.junit.Assert;
import org.junit.Test;

public class RemittanceCommandModelTest {

	private String remittanceStrategy = "0";
	private String requestNo = "requestNo1";
	private String productCode = "productCode1";
	private String uniqueId = "uniqueId1";
	private String contractNo = "contractNo1";
	
	@Test
	public void testIsValid_errorRemittanceStrategy() {
		RemittanceCommandModel model = new RemittanceCommandModel();
		Assert.assertFalse(model.isValid());
		Assert.assertEquals("放款策略［remittanceStrategy］，不存在！", model.getCheckFailedMsg());
		
		model = new RemittanceCommandModel();
		model.setRemittanceStrategy("9999");
		Assert.assertFalse(model.isValid());
		Assert.assertEquals("放款策略［remittanceStrategy］，不存在！", model.getCheckFailedMsg());
	}
	
	@Test
	public void testIsValid_errorRequestNo() {
		RemittanceCommandModel model = new RemittanceCommandModel();
		model.setRemittanceStrategy(remittanceStrategy);
		Assert.assertFalse(model.isValid());
		Assert.assertEquals("请求唯一标识［requestNo］，不能为空！", model.getCheckFailedMsg());
	}
	
	@Test
	public void testIsValid_errorProductCode() {
		RemittanceCommandModel model = new RemittanceCommandModel();
		model.setRemittanceStrategy(remittanceStrategy);
		model.setRequestNo(requestNo);
		Assert.assertFalse(model.isValid());
		Assert.assertEquals("信托产品代码［productCode］，不能为空！", model.getCheckFailedMsg());
	}
	
	@Test
	public void testIsValid_errorUniqueId() {
		RemittanceCommandModel model = new RemittanceCommandModel();
		model.setRemittanceStrategy(remittanceStrategy);
		model.setRequestNo(requestNo);
		model.setProductCode(productCode);
		Assert.assertFalse(model.isValid());
		Assert.assertEquals("贷款合同唯一编号［uniqueId］，不能为空！", model.getCheckFailedMsg());
	}
	
//	@Test
//	public void testIsValid_errorContractNo() {
//		RemittanceCommandModel model = new RemittanceCommandModel();
//		model.setRemittanceStrategy(remittanceStrategy);
//		model.setRequestNo(requestNo);
//		model.setProductCode(productCode);
//		model.setUniqueId(uniqueId);
//		Assert.assertFalse(model.isValid());
//		Assert.assertEquals("贷款合同编号［contractNo］，不能为空！", model.getCheckFailedMsg());
//	}
	
	@Test
	public void testIsValid_errorPlannedRemittanceAmount() {
		RemittanceCommandModel model = new RemittanceCommandModel();
		model.setRemittanceStrategy(remittanceStrategy);
		model.setRequestNo(requestNo);
		model.setProductCode(productCode);
		model.setUniqueId(uniqueId);
		model.setContractNo(contractNo);
		Assert.assertFalse(model.isValid());
		Assert.assertEquals("计划放款金额［plannedRemittanceAmount］，格式错误！", model.getCheckFailedMsg());
		
		model.setPlannedRemittanceAmount("0.00");
		Assert.assertFalse(model.isValid());
		Assert.assertEquals("计划放款金额［plannedRemittanceAmount］，金额需高于0.00！", model.getCheckFailedMsg());
		
		model.setPlannedRemittanceAmount("0.0s");
		Assert.assertFalse(model.isValid());
		Assert.assertEquals("计划放款金额［plannedRemittanceAmount］，格式错误！", model.getCheckFailedMsg());
		
		model.setPlannedRemittanceAmount("0.001");
		Assert.assertFalse(model.isValid());
		Assert.assertEquals("计划放款金额［plannedRemittanceAmount］，小数点后只保留2位！", model.getCheckFailedMsg());
	}
	
	@Test
	public void testIsValid_errorRemittanceDetails() {
		RemittanceCommandModel model = new RemittanceCommandModel();
		model.setRemittanceStrategy(remittanceStrategy);
		model.setRequestNo(requestNo);
		model.setProductCode(productCode);
		model.setUniqueId(uniqueId);
		model.setContractNo(contractNo);
		model.setPlannedRemittanceAmount("100");
		Assert.assertFalse(model.isValid());
		Assert.assertEquals("放款明细列表［remittanceDetails］，格式错误，不能为空列表！", model.getCheckFailedMsg());
	}
	
	@Test
	public void testIsValid_errorRepeatRemittanceDetailNo() {
		RemittanceCommandModel model = new RemittanceCommandModel();
		model.setRemittanceStrategy(remittanceStrategy);
		model.setRequestNo(requestNo);
		model.setProductCode(productCode);
		model.setUniqueId(uniqueId);
		model.setContractNo(contractNo);
		model.setPlannedRemittanceAmount("100");
		model.setRemittanceDetails("[{'detailNo':'detailNo1','recordSn':'1','amount':'10','plannedDate':'2016-08-16 00:00:00','bankCode':'C10102','bankCardNo':'bankCardNo1','bankAccountHolder':'bankAccountHolder1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'bankName1','idNumber':'idNumber1'},{'detailNo':'detailNo1','recordSn':'1','amount':'10','plannedDate':'2016-08-16 00:00:00','bankCode':'C10102','bankCardNo':'bankCardNo1','bankAccountHolder':'bankAccountHolder1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'bankName1','idNumber':'idNumber1'}]");
		Assert.assertFalse(model.isValid());
		Assert.assertEquals("放款明细列表［remittanceDetails］内，明细记录号［detailNo］有重复！", model.getCheckFailedMsg());
	}
	
	@Test
	public void testIsValid_errorFormatRemittanceDetails() {
		RemittanceCommandModel model = buildTestRemittanceCommandModelForTestFormatRemittanceDetails();
		model.setRemittanceDetails("[{'recordSn':'1','amount':'100','plannedDate':'2016-08-16 00:00:00','bankCode':'C10102','bankCardNo':'bankCardNo1','bankAccountHolder':'bankAccountHolder1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'bankName1','idNumber':'idNumber1'}]");
		Assert.assertFalse(model.isValid());
		Assert.assertEquals("放款明细列表［remittanceDetails］，行［0］，明细记录号［detailNo］，不能为空！", model.getCheckFailedMsg());
		
		model = buildTestRemittanceCommandModelForTestFormatRemittanceDetails();
		model.setRemittanceDetails("[{'detailNo':'detailNo1','recordSn':'recordSn','amount':'100','plannedDate':'2016-08-16 00:00:00','bankCode':'C10102','bankCardNo':'bankCardNo1','bankAccountHolder':'bankAccountHolder1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'bankName1','idNumber':'idNumber1'}]");
		Assert.assertFalse(model.isValid());
		Assert.assertEquals("放款明细列表［remittanceDetails］，行［0］，记录序号［recordSn］，格式错误！", model.getCheckFailedMsg());
		
		model = buildTestRemittanceCommandModelForTestFormatRemittanceDetails();
		model.setRemittanceDetails("[{'detailNo':'detailNo1','recordSn':'1','amount':'0.00','plannedDate':'2016-08-16 00:00:00','bankCode':'C10102','bankCardNo':'bankCardNo1','bankAccountHolder':'bankAccountHolder1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'bankName1','idNumber':'idNumber1'}]");
		Assert.assertFalse(model.isValid());
		Assert.assertEquals("放款明细列表［remittanceDetails］，行［0］，明细金额［amount］，金额需高于0.00！", model.getCheckFailedMsg());
		
		model = buildTestRemittanceCommandModelForTestFormatRemittanceDetails();
		model.setRemittanceDetails("[{'detailNo':'detailNo1','recordSn':'1','amount':'0.001','plannedDate':'2016-08-16 00:00:00','bankCode':'C10102','bankCardNo':'bankCardNo1','bankAccountHolder':'bankAccountHolder1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'bankName1','idNumber':'idNumber1'}]");
		Assert.assertFalse(model.isValid());
		Assert.assertEquals("放款明细列表［remittanceDetails］，行［0］，明细金额［amount］，小数点后只保留2位！", model.getCheckFailedMsg());
		
		model = buildTestRemittanceCommandModelForTestFormatRemittanceDetails();
		model.setRemittanceDetails("[{'detailNo':'detailNo1','recordSn':'1','amount':'s','plannedDate':'2016-08-16 00:00:00','bankCode':'C10102','bankCardNo':'bankCardNo1','bankAccountHolder':'bankAccountHolder1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'bankName1','idNumber':'idNumber1'}]");
		Assert.assertFalse(model.isValid());
		Assert.assertEquals("放款明细列表［remittanceDetails］，行［0］，明细金额［amount］，格式错误！", model.getCheckFailedMsg());
		
		model = buildTestRemittanceCommandModelForTestFormatRemittanceDetails();
		model.setRemittanceDetails("[{'detailNo':'detailNo1','recordSn':'1','amount':'100','plannedDate':'2016-08-16 00:00:00','bankCode':'','bankCardNo':'bankCardNo1','bankAccountHolder':'bankAccountHolder1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'bankName1','idNumber':'idNumber1'}]");
		Assert.assertFalse(model.isValid());
		Assert.assertEquals("放款明细列表［remittanceDetails］，行［0］，交易方信息不完整！", model.getCheckFailedMsg());
		
		
		model = buildTestRemittanceCommandModelForTestFormatRemittanceDetails();
		model.setRemittanceDetails("[{'detailNo':'detailNo1','recordSn':'1','amount':'100','plannedDate':'2016-08-16 00:00:00','bankCode':'C10102','bankCardNo':'','bankAccountHolder':'bankAccountHolder1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'bankName1','idNumber':'idNumber1'}]");
		Assert.assertFalse(model.isValid());
		Assert.assertEquals("放款明细列表［remittanceDetails］，行［0］，交易方信息不完整！", model.getCheckFailedMsg());
		
		
		model = buildTestRemittanceCommandModelForTestFormatRemittanceDetails();
		model.setRemittanceDetails("[{'detailNo':'detailNo1','recordSn':'1','amount':'100','plannedDate':'2016-08-16 00:00:00','bankCode':'C10102','bankCardNo':'bankCardNo1','bankAccountHolder':'','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'bankName1','idNumber':'idNumber1'}]");
		Assert.assertFalse(model.isValid());
		Assert.assertEquals("放款明细列表［remittanceDetails］，行［0］，交易方信息不完整！", model.getCheckFailedMsg());
		
		model = buildTestRemittanceCommandModelForTestFormatRemittanceDetails();
		model.setRemittanceDetails("[{'detailNo':'detailNo1','recordSn':'1','amount':'100','plannedDate':'2016-08-16s00:00:00','bankCode':'C10102','bankCardNo':'','bankAccountHolder':'bankAccountHolder1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'bankName1','idNumber':'idNumber1'}]");
		Assert.assertFalse(model.isValid());
		Assert.assertEquals("放款明细列表［remittanceDetails］，行［0］，计划执行日期［plannedDate］，格式错误！", model.getCheckFailedMsg());
	}
	
	private RemittanceCommandModel buildTestRemittanceCommandModelForTestFormatRemittanceDetails() {
		RemittanceCommandModel model = new RemittanceCommandModel();
		model.setRemittanceStrategy(remittanceStrategy);
		model.setRequestNo(requestNo);
		model.setProductCode(productCode);
		model.setUniqueId(uniqueId);
		model.setContractNo(contractNo);
		model.setPlannedRemittanceAmount("100");
		return model;
	}
	
	@Test
	public void testIsValid_errorComparePlannedRemittanceAmount() {
		RemittanceCommandModel model = new RemittanceCommandModel();
		model.setRemittanceStrategy(remittanceStrategy);
		model.setRequestNo(requestNo);
		model.setProductCode(productCode);
		model.setUniqueId(uniqueId);
		model.setContractNo(contractNo);
		model.setPlannedRemittanceAmount("100.0");
		model.setRemittanceDetails("[{'detailNo':'detailNo1','recordSn':'1','amount':'100.1','plannedDate':'2016-08-16 00:00:00','bankCode':'C10102','bankCardNo':'bankCardNo1','bankAccountHolder':'bankAccountHolder1','bankProvince':'bankProvince1','bankCity':'bankCity1','bankName':'bankName1','idNumber':'idNumber1'}]");
		Assert.assertFalse(model.isValid());
		Assert.assertEquals("计划放款金额［plannedRemittanceAmount］，与明细金额累计不一致！", model.getCheckFailedMsg());
	}
	
}
