package com.zufangbao.earth.yunxin.handler.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.sun.yunxin.entity.model.api.ContractDetail;
import com.zufangbao.sun.yunxin.entity.model.api.ImportRepaymentPlanDetail;
import com.zufangbao.wellsfargo.yunxin.handler.ImportAssetPackageApiHandler;

/** 
* @author 作者 zhenghangbo
* @version 创建时间：Dec 5, 2016 2:04:51 PM 
* 类说明 
*/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class ImportAssetPackageHandlerTest {
	
	
	
	@Autowired
	private ImportAssetPackageApiHandler importAssetPackageApiHandler;
	
	
	  @Test
	  @Sql("classpath:test/yunxin/assets/checkFirstPeriodAssetRecycleDate.sql")
	  public void checkFirstPeriodAssetRecycleDateContactEffectError(){
		  
		  ContractDetail contractDetail = new ContractDetail();
		  contractDetail.setEffectDate("2016-09-02");
		  contractDetail.setUniqueId("547083b0-0c32-42b4-862e-653e85d944fa");
		  try {
			//  importAssetPackageApiHandler.checkFirstPeriodAssetRecycleDate(contractDetail);
		} catch (ApiException e) {
			assertEquals(23037, e.getCode());
			assertEquals("合同生效日期错误，正确的生效日期为2016-09-01", e.getMessage());
		}
	  }
	  @Test
	  @Sql("classpath:test/yunxin/assets/checkFirstPeriodAssetRecycleDateBeginingLoanDateError.sql")
	  public void checkFirstPeriodAssetRecycleDateBeginingLoanDateError(){
		  
		  ContractDetail contractDetail = new ContractDetail();
		  contractDetail.setEffectDate("2016-09-01");
		  contractDetail.setUniqueId("547083b0-0c32-42b4-862e-653e85d944fa");
		  try {
			//  importAssetPackageApiHandler.checkFirstPeriodAssetRecycleDate(contractDetail);
		} catch (ApiException e) {
			assertEquals(23047, e.getCode());
			assertEquals("未查询到合同对应放款", e.getMessage());
		}
	  }
	  
	  @Test
	  @Sql("classpath:test/yunxin/assets/checkFirstPeriodAssetRecycleDateFirstAssetBeforeRecycleDate.sql")
	  public void checkFirstPeriodAssetRecycleDateFirstAssetBeforeRecycleDate(){
		  
		  
		  ContractDetail contractDetail = new ContractDetail();
		  contractDetail.setEffectDate("2016-09-01");
		  contractDetail.setUniqueId("547083b0-0c32-42b4-862e-653e85d944fa");
		  List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<ImportRepaymentPlanDetail>();
		  ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
		  repaymentPlanDetail1.setRepaymentPrincipal("2000.00");
		  repaymentPlanDetail1.setRepaymentInterest("20.00");
		  repaymentPlanDetail1.setRepaymentDate("2016-08-04");
		  repaymentPlanDetail1.setOtheFee("0.00");
		  repaymentPlanDetail1.setTechMaintenanceFee("0.00");
		  repaymentPlanDetail1.setLoanServiceFee("0.00");
		  repaymentPlanDetails.add(repaymentPlanDetail1);
		  contractDetail.setRepaymentPlanDetails(repaymentPlanDetails);
		  try {
			 // importAssetPackageApiHandler.checkFirstPeriodAssetRecycleDate(contractDetail);
			
		} catch (ApiException e) {
			assertEquals(23037, e.getCode());
			
		}
		  
	  }
	  
	  @Test
	  @Sql("classpath:test/yunxin/assets/checkFirstPeriodAssetRecycleDateNormal.sql")
	  public void checkFirstPeriodAssetRecycleDateNormal(){
		  
		  
		  ContractDetail contractDetail = new ContractDetail();
		  
		  contractDetail.setUniqueId("547083b0-0c32-42b4-862e-653e85d944fa");
		  List<ImportRepaymentPlanDetail> repaymentPlanDetails = new ArrayList<ImportRepaymentPlanDetail>();
		  ImportRepaymentPlanDetail repaymentPlanDetail1 = new ImportRepaymentPlanDetail();
		  repaymentPlanDetail1.setRepaymentPrincipal("2000.00");
		  repaymentPlanDetail1.setRepaymentInterest("20.00");
		  repaymentPlanDetail1.setRepaymentDate("2016-09-04");
		  repaymentPlanDetail1.setOtheFee("0.00");
		  repaymentPlanDetail1.setTechMaintenanceFee("0.00");
		  repaymentPlanDetail1.setLoanServiceFee("0.00");
		  repaymentPlanDetails.add(repaymentPlanDetail1);
		  contractDetail.setRepaymentPlanDetails(repaymentPlanDetails);
		  //importAssetPackageApiHandler.checkFirstPeriodAssetRecycleDate(contractDetail);
			
		  
	  }
	  
	  
}
