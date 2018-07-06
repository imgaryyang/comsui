package com.zufangbao.earth.yunxin.api.service;


import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.sun.service.UpdateAssetLogService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml" })
public class UpdateAssetLogServiceImplTest {
	@Autowired
	private UpdateAssetLogService updateAssetLogService;
	
	@Test
	@Sql("classpath:test/yunxin/api/service/testCheckByRequestNo.sql")
	public void testCheckByRequestNo(){
		try{
			String requestNo = "testrequest1";
			updateAssetLogService.checkByRequestNo(requestNo);
		}catch (ApiException e) {
			assertEquals(21002,e.getCode());
		}
	}
	
}
