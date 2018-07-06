/**
 * 
 */
package com.zufangbao.earth.yunxin.handler;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * @author hjl
 *	还款订单编辑测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

        "classpath:/local/applicationContext-*.xml" })

@TransactionConfiguration(defaultRollback = true)
@Transactional
public class RepaymentOrderItemEditTest {
	@Test
	public void test(){
		
	}
}
