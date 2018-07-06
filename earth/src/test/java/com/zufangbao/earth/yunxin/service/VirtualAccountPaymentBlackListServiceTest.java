package com.zufangbao.earth.yunxin.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.VirtualAccountPaymentBlackList;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.VirtualAccountPaymentBlackListService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={

"classpath:/local/applicationContext-*.xml" })
@TransactionConfiguration(defaultRollback=true)
@Transactional
public class VirtualAccountPaymentBlackListServiceTest {
	
	@Autowired
	private VirtualAccountPaymentBlackListService virtualAccountPaymentBlackListService;
	
	
	@Test
	public void testVirtualAccountPaymentBlackListService(){
		
		VirtualAccountPaymentBlackList virtualAccountPaymentBlackList = virtualAccountPaymentBlackListService.load(VirtualAccountPaymentBlackList.class, 1L);
	}

}
