/**
 * 
 */
package com.zufangbao.earth.update.test;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zufangbao.earth.update.model.UpdateWrapperModel;
import com.zufangbao.earth.update.wrapper.IUpdateWrapper;

/**
 *线下支付单作废生成脚本Test
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml"
})
@Transactional()
@Rollback(true)
public class updateWrapper7Test {
	@Autowired
	private IUpdateWrapper updateWrapper7;

	@Test
	@Sql("classpath:test/testUpdateWrapper7.sql")
	public void test(){
		try {
			UpdateWrapperModel paramsBean = new UpdateWrapperModel();
			paramsBean.setOfflineBillNo("['XX274E9D75FC35EDF7','XX274F09284716F37C']");
			String sql = updateWrapper7.wrap(paramsBean);
			System.out.println(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
