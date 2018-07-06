package com.zufangbao.earth.yunxin.handler.deduct.v2;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.zufangbao.earth.BaseTest;
import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductRequestModel;

public class DeductBusinessHandlerTest extends BaseTest{
	
	@Autowired
	private DeductBusinessHandler deductBusinessHandler;

	@Test
	@Sql("classpath:test/yunxin/deduct/v2/business-acceptance.sql")
	public void testHandleDeductBusiness() {
		
		String preProcessUrl = "zhonghang/deduct-business";
		
		DeductRequestModel deductRequestModel = new DeductRequestModel();
		
		deductBusinessHandler.handleDeductBusiness(preProcessUrl, deductRequestModel);
		
	}
	
	@Test
	public void initData() throws Exception {
		
		DeductRequestModel deductRequestModel = new DeductRequestModel();
		
//		deductRequestModel.set
		
	}

}
