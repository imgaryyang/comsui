package com.zufangbao.earth.update.test;




import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zufangbao.earth.update.model.UpdateWrapperModel;
import com.zufangbao.earth.update.wrapper.IUpdateWrapper;
import com.zufangbao.earth.update.wrapper.UpdateSqlCacheManager;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
"classpath:/local/applicationContext-*.xml"
})
@Transactional()
public class updateWrapper2Test {
	
	@Autowired
	private IUpdateWrapper updateWrapper2;
	
	@Test
	@Sql("classpath:test/testUpdateWrapper2.sql")
	public void test(){
		UpdateWrapperModel model=new UpdateWrapperModel();
		model.setAmount("10000");
		model.setAssetNo("ZC1654732812221087744");
		model.setRepurchaseDocUuid("9560762b-c9e1-4aca-8ada-171f9692c618");
		model.setPayoutTime("2017-12-01");
		model.setRepuchaseAmountBefore("10000");
		model.setRepuchaseAmountAfter("8000");
		model.setBankDebitAmount("1000");
		model.setTotalOrderRent("10000");
		//model.setRepurchasePrincipal("500");
		model.setRepurchaseInterest("500");
		model.setRepurchasePenalty("500");
		//model.setRepurchaseOtherCharges("500");
		model.setExtraChargeSpecKey(ChartOfAccount.TRD_BANK_SAVING_GENERAL_PRINCIPAL);
		
		
		try {
			System.out.println(updateWrapper2.wrap(model));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
