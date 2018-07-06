package com.suidifu.bridgewater.handler.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import com.zufangbao.sun.yunxin.handler.DeductPlanCoreOperationHandler;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/context/applicationContext-*.xml",
		"classpath:/local/applicationContext-*.xml" })
@Transactional
public class DeductPlanHandlerTest {
	@Autowired
	DeductPlanCoreOperationHandler deductPlanCoreOperationHandler;
	
	@Test
	public void testGetDeductPlanByDeductApplicaiton(){
		List<DeductApplication> deductApplicationList = new ArrayList<DeductApplication>();
		Map<String,List<DeductPlan>> repayment_code_and_repayment_plan_list = deductPlanCoreOperationHandler.getDeductPlanByDeductApplicaiton(deductApplicationList);
		Assert.assertEquals(0,repayment_code_and_repayment_plan_list.size());
		
		
		
	}
}
