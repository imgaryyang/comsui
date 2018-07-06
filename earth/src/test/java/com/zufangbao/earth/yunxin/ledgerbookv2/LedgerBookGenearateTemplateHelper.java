/**
 * 
 */
package com.zufangbao.earth.yunxin.ledgerbookv2;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.zufangbao.earth.BaseTest;
import com.zufangbao.sun.ledgerbookv2.enums.EventType;
import com.zufangbao.wellsfargo.silverpool.ledgerbookv2.template.GeneralAccountTemplateHelperForTestWithCache;

/**
 * @author wukai
 * 造模版数据
 */
@Rollback(false)
public class LedgerBookGenearateTemplateHelper extends BaseTest{
	

	@Autowired
	private GeneralAccountTemplateHelperForTestWithCache generalAccountTemplateHelperForTest;
	
	@Before
	public void setUp(){
		generalAccountTemplateHelperForTest.deleteAccountTemplateAndScenarion();
	}

	@Test
	public void createDataForBOOK_LOAN_ASSETS(){
		
		generalAccountTemplateHelperForTest.createTemplateBy("LedgerBookNo", EventType.BOOK_LOAN_ASSETS);
		
		generalAccountTemplateHelperForTest.syncSourceRepository(EventType.BOOK_LOAN_ASSETS);
		
	}
}
