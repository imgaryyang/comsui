package com.zufangbao.earth.yunxin.api;

import com.demo2do.core.utils.DateUtils;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.sun.api.model.modify.ImportAssetPackageContent;
import com.zufangbao.sun.api.model.modify.ImportAssetPackageRequestModel;
import com.zufangbao.sun.api.model.repayment.AssetPackageBatchQueryModel;
import com.zufangbao.sun.api.model.repayment.AssetPackageImportResultModel;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.model.api.ContractDetail;
import com.zufangbao.sun.yunxin.entity.model.api.ImportRepaymentPlanDetail;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.wellsfargo.yunxin.handler.ImportAssetPackageApiHandler;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class ImportAssetPackageApiTest {

	
  @Autowired
  private ImportAssetPackageApiHandler importAssetPackageApiHandler;
  
  @Autowired
  private ContractService contractService;
  
  @Autowired
  private RepaymentPlanService repaymentPlanService;
  
  
  @Test
  @Sql("classpath:test/yunxin/assetPackage/testImportAssetPackageApi.sql")
  public void testImportAssetPackageApi_normal(){
	  
	  ImportAssetPackageContent importAssetPackageContent = new ImportAssetPackageContent();
	  importAssetPackageContent.setThisBatchContractsTotalNumber(1);
	  importAssetPackageContent.setThisBatchContractsTotalAmount("90000");
	  importAssetPackageContent.setFinancialProductCode("G00010");
	  
	  List<ContractDetail> contracts= new ArrayList<ContractDetail>();
	  ContractDetail contractDetail = new ContractDetail();
	  contractDetail.setUniqueId(UUID.randomUUID().toString());
	  contractDetail.setLoanContractNo("TEST500");
	  contractDetail.setLoanCustomerNo("TEST500");
	  contractDetail.setLoanCustomerName("中文");
	  contractDetail.setSubjectMatterassetNo(UUID.randomUUID().toString());
	  contractDetail.setIDCardNo("33068319940303641X");
	  contractDetail.setBankCode("C10102");
	  contractDetail.setBankOfTheProvince("330000");
	  contractDetail.setBankOfTheCity("110100");
	  contractDetail.setRepaymentAccountNo("23456787654323456");
	  contractDetail.setLoanTotalAmount("10000.00");
	  contractDetail.setLoanPeriods(2);
	  contractDetail.setEffectDate("2016-12-01");
	  contractDetail.setExpiryDate("2099-01-01");
	  contractDetail.setLoanRates("0.156");
	  contractDetail.setInterestRateCycle(1);
	  contractDetail.setPenalty("0.0005");
	  contractDetail.setRepaymentWay(1);
	  
	  List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<ImportRepaymentPlanDetail>();
	  
	  ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
	  repaymentPlanDetail1.setRepaymentPrincipal("11000");
	  repaymentPlanDetail1.setRepaymentInterest("0.00");
	  repaymentPlanDetail1.setRepaymentDate("2017-03-01");
	  repaymentPlanDetail1.setOtheFee("0.00");
	  repaymentPlanDetail1.setTechMaintenanceFee("0.00");
	  repaymentPlanDetail1.setLoanServiceFee("0.00");
	  repaymentPlanDetails.add(repaymentPlanDetail1);
	  
	  ImportRepaymentPlanDetail repaymentPlanDetail2 = new ImportRepaymentPlanDetail();
	  repaymentPlanDetail2.setRepaymentPrincipal("79000.00");
	  repaymentPlanDetail2.setRepaymentInterest("0.00");
	  repaymentPlanDetail2.setRepaymentDate("2017-04-01");
	  repaymentPlanDetail2.setOtheFee("0.00");
	  repaymentPlanDetail2.setTechMaintenanceFee("0.00");
	  repaymentPlanDetail2.setLoanServiceFee("0.00");
	  repaymentPlanDetails.add(repaymentPlanDetail2);
	  contractDetail.setRepaymentPlanDetails(repaymentPlanDetails);
	  
	  contracts.add(contractDetail);
	  importAssetPackageContent.setContractDetails(contracts);
	  
	  
	  ImportAssetPackageRequestModel requestModel = new ImportAssetPackageRequestModel();
	  requestModel.setFn("1000002");
	  requestModel.setRequestNo(UUID.randomUUID().toString());
	  requestModel.setImportAssetPackageContent(JsonUtils.toJsonString(importAssetPackageContent));
	  
	  
	  try {
		importAssetPackageApiHandler.importAssetPackage(requestModel, false);
		
		List<Contract>  contractList  = contractService.loadAll(Contract.class);
		Assert.assertEquals(1,contractList.size());
		
		Contract contract = contractList.get(0);
		
		Assert.assertEquals("contractNo1", contract.getContractNo());
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
  }

  
  @Test(expected=NullPointerException.class)
  @Sql("classpath:test/yunxin/assetPackage/testImportAssetPackageApi.sql")
  public void testImportAssetPackageApi_NoContractDetails(){
	  
	  ImportAssetPackageContent importAssetPackageContent = new ImportAssetPackageContent();
	  importAssetPackageContent.setThisBatchContractsTotalNumber(1);
	  importAssetPackageContent.setThisBatchContractsTotalAmount("4000");
	  importAssetPackageContent.setFinancialProductCode("G0000000");
	  
	  List<ContractDetail> contracts= new ArrayList<ContractDetail>();
	  ContractDetail contractDetail = new ContractDetail();
	  contractDetail.setUniqueId("34567890");
	  contractDetail.setLoanContractNo("contractNo1");
	  contractDetail.setLoanCustomerNo("customerNo1");
	  contractDetail.setLoanCustomerName("customerName");
	  contractDetail.setSubjectMatterassetNo("234567");
	  contractDetail.setIDCardNo("33068319940303641X");
	  contractDetail.setBankCode("C10102");
	  contractDetail.setBankOfTheProvince("330000");
	  contractDetail.setBankOfTheCity("110100");
	  contractDetail.setRepaymentAccountNo("23456787654323456");
	  contractDetail.setLoanTotalAmount("4000.00");
	  contractDetail.setLoanPeriods(2);
	  contractDetail.setEffectDate("2016-8-1");
	  contractDetail.setExpiryDate("2099-01-01");
	  contractDetail.setLoanRates("0.156");
	  contractDetail.setInterestRateCycle(1);
	  contractDetail.setPenalty("0.0005");
	  contractDetail.setRepaymentWay(1);
	  
	  List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<ImportRepaymentPlanDetail>();
	  
	  ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
	  repaymentPlanDetail1.setRepaymentPrincipal("2000.00");
	  repaymentPlanDetail1.setRepaymentInterest("20.00");
	  repaymentPlanDetail1.setRepaymentDate("2016-09-04");
	  repaymentPlanDetail1.setOtheFee("0.00");
	  repaymentPlanDetail1.setTechMaintenanceFee("0.00");
	  repaymentPlanDetail1.setLoanServiceFee("0.00");
	  repaymentPlanDetails.add(repaymentPlanDetail1);
	  
	  ImportRepaymentPlanDetail repaymentPlanDetail2 = new ImportRepaymentPlanDetail();
	  repaymentPlanDetail2.setRepaymentPrincipal("2000.00");
	  repaymentPlanDetail2.setRepaymentInterest("20.00");
	  repaymentPlanDetail2.setRepaymentDate("2016-10-04");
	  repaymentPlanDetail2.setOtheFee("0.00");
	  repaymentPlanDetails.add(repaymentPlanDetail2);
	  contractDetail.setRepaymentPlanDetails(repaymentPlanDetails);
	  
	  contracts.add(contractDetail);
	  importAssetPackageContent.setContractDetails(contracts);
	  
	  
	  ImportAssetPackageRequestModel requestModel = new ImportAssetPackageRequestModel();
	  requestModel.setFn("1000002");
	  requestModel.setRequestNo("13456789");
	  requestModel.setImportAssetPackageContent(JsonUtils.toJsonString(importAssetPackageContent));
	  
	  
	  try {
		importAssetPackageApiHandler.importAssetPackage(requestModel, false);
		
		List<Contract>  contractList  = contractService.loadAll(Contract.class);
		Assert.assertEquals(1,contractList.size());
		
		Contract contract = contractList.get(0);
		
		List<AssetSet> assetSetList = repaymentPlanService.loadAll(AssetSet.class);
		Assert.assertEquals("contractNo1", contract.getContractNo());
		Assert.assertEquals(2, assetSetList.size());
		
		AssetSet assetSet1 = assetSetList.get(0);
		Assert.assertEquals("2016-09-04", DateUtils.format(assetSet1.getAssetRecycleDate()));
		
		AssetSet assetSet2 = assetSetList.get(1);
		Assert.assertEquals("2016-10-04", DateUtils.format(assetSet2.getAssetRecycleDate()));
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
  }
  
  
  
  @Test
  @Sql("classpath:test/yunxin/assetPackage/testImportAssetPackageApi.sql")
  public void testImportAssetPackageApi_AmountError(){
	  
	  ImportAssetPackageContent importAssetPackageContent = new ImportAssetPackageContent();
	  importAssetPackageContent.setThisBatchContractsTotalNumber(1);
	  importAssetPackageContent.setThisBatchContractsTotalAmount("4001");
	  importAssetPackageContent.setFinancialProductCode("G0000000");
	  
	  List<ContractDetail> contracts= new ArrayList<ContractDetail>();
	  ContractDetail contractDetail = new ContractDetail();
	  contractDetail.setUniqueId("34567890");
	  contractDetail.setLoanContractNo("contractNo1");
	  contractDetail.setLoanCustomerNo("customerNo1");
	  contractDetail.setLoanCustomerName("customerName");
	  contractDetail.setSubjectMatterassetNo("234567");
	  contractDetail.setIDCardNo("33068319940303641X");
	  contractDetail.setBankCode("C10102");
	  contractDetail.setBankOfTheProvince("330000");
	  contractDetail.setBankOfTheCity("330300");
	  contractDetail.setRepaymentAccountNo("23456787654323456");
	  contractDetail.setLoanTotalAmount("4000.00");
	  contractDetail.setLoanPeriods(2);
	  contractDetail.setEffectDate("2016-8-1");
	  contractDetail.setExpiryDate("2099-01-01");
	  contractDetail.setLoanRates("0.156");
	  contractDetail.setInterestRateCycle(1);
	  contractDetail.setPenalty("0.0005");
	  contractDetail.setRepaymentWay(1);
	  
	  List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<ImportRepaymentPlanDetail>();
	  
	  ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
	  repaymentPlanDetail1.setRepaymentPrincipal("2000.00");
	  repaymentPlanDetail1.setRepaymentInterest("20.00");
	  repaymentPlanDetail1.setRepaymentDate("2016-09-04");
	  repaymentPlanDetail1.setOtheFee("0.00");
	  repaymentPlanDetail1.setTechMaintenanceFee("0.00");
	  repaymentPlanDetail1.setLoanServiceFee("0.00");
	  repaymentPlanDetails.add(repaymentPlanDetail1);
	  
	  ImportRepaymentPlanDetail repaymentPlanDetail2 = new ImportRepaymentPlanDetail();
	  repaymentPlanDetail2.setRepaymentPrincipal("2000.00");
	  repaymentPlanDetail2.setRepaymentInterest("20.00");
	  repaymentPlanDetail2.setRepaymentDate("2016-10-04");
	  repaymentPlanDetail2.setOtheFee("0.00");
	  repaymentPlanDetail2.setTechMaintenanceFee("0.00");
	  repaymentPlanDetail2.setLoanServiceFee("0.00");
	  repaymentPlanDetails.add(repaymentPlanDetail2);
	  contractDetail.setRepaymentPlanDetails(repaymentPlanDetails);
	  
	  contracts.add(contractDetail);
	  importAssetPackageContent.setContractDetails(contracts);
	  
	  
	  ImportAssetPackageRequestModel requestModel = new ImportAssetPackageRequestModel();
	  requestModel.setFn("1000002");
	  requestModel.setRequestNo("13456789");
	  requestModel.setImportAssetPackageContent(JsonUtils.toJsonString(importAssetPackageContent));
	  
	  
	  try {
		importAssetPackageApiHandler.importAssetPackage(requestModel, false);
		
		List<Contract>  contractList  = contractService.loadAll(Contract.class);
		Assert.assertEquals(1,contractList.size());
		Assert.assertEquals("uuid1",contractList.get(0).getFinancialContractUuid());
		
	} catch (ApiException e) {
		e.printStackTrace();
		Assert.assertEquals(23026, e.getCode());
	} catch (Exception e) {
		e.printStackTrace();
	}
	  
  }
    //商户还款计划编号重复 repayScheduleNoMD5值重复异常
	@Test
	@Sql("classpath:test/yunxin/assetPackage/testImportAssetPackageApi_repeatRepaySchduleNo.sql")
	public void testImportAssetPackageApi_repeatRepaySchduleNo(){

		ImportAssetPackageContent importAssetPackageContent = new ImportAssetPackageContent();
		importAssetPackageContent.setThisBatchContractsTotalNumber(1);
		importAssetPackageContent.setThisBatchContractsTotalAmount("4000");
		importAssetPackageContent.setFinancialProductCode("G32000");

		List<ContractDetail> contracts= new ArrayList<ContractDetail>();
		ContractDetail contractDetail = new ContractDetail();
		contractDetail.setUniqueId("34567890");
		contractDetail.setLoanContractNo("contractNo1");
		contractDetail.setLoanCustomerNo("customerNo1");
		contractDetail.setLoanCustomerName("customerName");
		contractDetail.setSubjectMatterassetNo("234567");
		contractDetail.setIDCardNo("33068319940303641X");
		contractDetail.setBankCode("C10102");
		contractDetail.setBankOfTheProvince("330000");
		contractDetail.setBankOfTheCity("330300");
		contractDetail.setRepaymentAccountNo("23456787654323456");
		contractDetail.setLoanTotalAmount("4000.00");
		contractDetail.setLoanPeriods(2);
		contractDetail.setEffectDate("2016-8-1");
		contractDetail.setExpiryDate("2099-01-01");
		contractDetail.setLoanRates("0.156");
		contractDetail.setInterestRateCycle(1);
		contractDetail.setPenalty("0.0005");
		contractDetail.setRepaymentWay(1);

		List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<ImportRepaymentPlanDetail>();

		ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
		repaymentPlanDetail1.setRepaymentPrincipal("2000.00");
		repaymentPlanDetail1.setRepaymentInterest("20.00");
		repaymentPlanDetail1.setRepaymentDate("2016-09-04");
		repaymentPlanDetail1.setOtheFee("0.00");
		repaymentPlanDetail1.setTechMaintenanceFee("0.00");
		repaymentPlanDetail1.setLoanServiceFee("0.00");
		repaymentPlanDetail1.setRepayScheduleNo("outer1");
		repaymentPlanDetails.add(repaymentPlanDetail1);

		ImportRepaymentPlanDetail repaymentPlanDetail2 = new ImportRepaymentPlanDetail();
		repaymentPlanDetail2.setRepaymentPrincipal("2000.00");
		repaymentPlanDetail2.setRepaymentInterest("20.00");
		repaymentPlanDetail2.setRepaymentDate("2016-10-04");
		repaymentPlanDetail2.setOtheFee("0.00");
		repaymentPlanDetail2.setTechMaintenanceFee("0.00");
		repaymentPlanDetail2.setLoanServiceFee("0.00");
		repaymentPlanDetail2.setRepayScheduleNo("outer1");
		repaymentPlanDetails.add(repaymentPlanDetail2);
		contractDetail.setRepaymentPlanDetails(repaymentPlanDetails);

		contracts.add(contractDetail);
		importAssetPackageContent.setContractDetails(contracts);

		ImportAssetPackageRequestModel requestModel = new ImportAssetPackageRequestModel();
		requestModel.setFn("1000002");
		requestModel.setRequestNo("13456789");
		requestModel.setImportAssetPackageContent(JsonUtils.toJsonString(importAssetPackageContent));

		try {
			String repayScheduleNo4MD5 = repaymentPlanService.getRepayScheduleNoMD5("G32000", "outer1", StringUtils.EMPTY);
			System.out.println(repayScheduleNo4MD5);

			importAssetPackageApiHandler.importAssetPackage(requestModel,false);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertEquals("商户还款计划编号重复", e.getMessage());
		}

	}

  @Test
  @Sql("classpath:test/yunxin/assetPackage/test_queryAssetPackageImportResult.sql")
  public void test_queryAssetPackageImportResult(){
	  AssetPackageBatchQueryModel model = new AssetPackageBatchQueryModel();
	  List<AssetPackageImportResultModel> list = importAssetPackageApiHandler.queryAssetPackageImportResult(model);
	  Assert.assertEquals(0,list.size());
	  model.setRequestNo(UUID.randomUUID().toString());
	  model.setUniqueIds("[1,2,3,4]");
	  list = importAssetPackageApiHandler.queryAssetPackageImportResult(model);
	  Assert.assertEquals(4, list.size());
	 
  }
  
  @Test
  @Sql("classpath:test/yunxin/assetPackage/test_queryAssetPackageImportResult.sql")
  public void test_queryAssetPackageImportResultForImportAssetPackage(){
	  AssetPackageBatchQueryModel model = new AssetPackageBatchQueryModel();
	  model.setRequestNo(UUID.randomUUID().toString());
	  model.setUniqueIds("['CONTRACT_UNIQUEID_ID001']");
	  model.setProductCode("G08200");
	  List<AssetPackageImportResultModel> list = importAssetPackageApiHandler.queryAssetPackageImportResult(model);
	  Assert.assertEquals("CONTRACT_NO001", list.get(0).getContractNo());
	  Assert.assertEquals("2017-02-15", list.get(0).getImportTime());
	  Assert.assertEquals("CONTRACT_UNIQUEID_ID001", list.get(0).getUniqueId());
	  Assert.assertEquals(0, list.get(0).getStatus());
	 
  }
  
  @Test
  @Sql("classpath:test/yunxin/assetPackage/test_queryAssetPackageImportResult.sql")
  public void test_queryAssetPackageImportResultForImportAssetPackage_1(){
	  AssetPackageBatchQueryModel model = new AssetPackageBatchQueryModel();
	  model.setRequestNo(UUID.randomUUID().toString());
	  model.setContractNos("['CONTRACT_NO001']");
	  model.setProductCode("G08200");
	  List<AssetPackageImportResultModel> list = importAssetPackageApiHandler.queryAssetPackageImportResult(model);
	  Assert.assertEquals("CONTRACT_NO001", list.get(0).getContractNo());
	  Assert.assertEquals("2017-02-15", list.get(0).getImportTime());
	  Assert.assertEquals("CONTRACT_UNIQUEID_ID001", list.get(0).getUniqueId());
	  Assert.assertEquals(0, list.get(0).getStatus());
	 
  }
  
  @Test
  @Sql("classpath:test/yunxin/assetPackage/test_queryAssetPackageImportResult.sql")
  public void test_queryAssetPackageImportResultForNotImportAssetPackage(){
	  AssetPackageBatchQueryModel model = new AssetPackageBatchQueryModel();
	  model.setRequestNo(UUID.randomUUID().toString());
	  model.setUniqueIds("['CONTRACT_UNIQUEID_ID002']");
	  model.setProductCode("G08200");
	  List<AssetPackageImportResultModel> list = importAssetPackageApiHandler.queryAssetPackageImportResult(model);
	  Assert.assertEquals("CONTRACT_NO002", list.get(0).getContractNo());
	  Assert.assertEquals("",list.get(0).getImportTime());
	  Assert.assertEquals("CONTRACT_UNIQUEID_ID002", list.get(0).getUniqueId());
	  Assert.assertEquals(1, list.get(0).getStatus());
	 
  }
	
  @Test
  @Sql("classpath:test/yunxin/assetPackage/test_queryAssetPackageImportResult.sql")
  public void test_queryAssetPackageImportResult_1(){
	  AssetPackageBatchQueryModel model = new AssetPackageBatchQueryModel();
	  model.setRequestNo(UUID.randomUUID().toString());
	  model.setProductCode("G08200");
	  model.setStartTime("2016-09-01");
	  model.setEndTime("2016-09-01");
	  List<AssetPackageImportResultModel> list = importAssetPackageApiHandler.queryAssetPackageImportResult(model);
	  Assert.assertEquals(4,list.size());
  }
}
