package com.zufangbao.earth.update.test;

import static com.zufangbao.earth.update.util.UpdateConfiguration.UPDATE_CODES_WRAPPER_FILE_MAPPER;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dom4j.DocumentException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.suidifu.hathaway.util.JsonUtils;
import com.zufangbao.earth.update.model.UpdateWrapperModel;
import com.zufangbao.earth.update.wrapper.IUpdateWrapper;
import com.zufangbao.earth.update.wrapper.UpdateSqlCacheManager;
import com.zufangbao.gluon.exception.CommonException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

"classpath:/local/applicationContext-*.xml"})
public class updateWrapper1Test {

	@Autowired
	private IUpdateWrapper updateWrapper1;
	@Autowired
	private UpdateSqlCacheManager updateSqlCacheManager;
	
	@SuppressWarnings({ "unchecked" })
	@Test
	@Sql("classpath:test/testUpdateWrapper1.sql")
	public void testWrapper(){
		
		String second_no = UUID.randomUUID().toString();
		UpdateWrapperModel model=new UpdateWrapperModel();
		//{"bankSequenceNo": "放款流水号","contractId": "多个合同id,用逗号分割","financial_contract_uuid":"信托合同uuid","second_no": "打款流水","comment":"备注"}
		model.setBankSequenceNo("G8698800105783C");
		model.setContractId("376");
		model.setFinancialContractUuid("ae07d211-2c10-43ed-9924-3d7988db6fe4");
		model.setSecondNo(second_no);
		model.setComment("农分期回购关联");
		model.setAmount("66250.96");
		
		
		try {
			System.out.println(updateWrapper1.wrap(model));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testParams() throws DocumentException, CommonException, IOException{
		String params=(String) updateSqlCacheManager.getSqlParam().get(UPDATE_CODES_WRAPPER_FILE_MAPPER.get("1")).get("params");
		Map<String,Object> sqlMap=JsonUtils.parse(params);
		System.out.println(sqlMap.values());
		
	}
	
}
