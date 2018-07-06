package com.zufangbao.earth.report.wrapper;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.zufangbao.earth.report.factory.SqlCacheManager;
import com.zufangbao.earth.report.util.FreemarkerUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml"})
@Transactional
@WebAppConfiguration(value="webapp")
public class reportWrapper2Test {
	
	@Autowired
	private SqlCacheManager sqlCacheManager;
	 
	@Test
	public void testSql1(){
		String sqlTest="SELECT ae.asset_recycle_date as assetRecycleDate, ae.asset_interest_value as assetInterestValue, ae.asset_principal_value as assetPrincipalValue FROM asset_set ae WHERE ae.contract_id = :contractId AND ae.active_status=0 ORDER BY ae.current_period ASC";
		Map<String, Object> params=new HashMap<>();
		
		params.put("loanBatchId", 1);
		String sql=null;
		try {
			 sql=FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper2"), params);
			System.out.println(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(sqlTest, sql);
	}
	
	
	@Test
	public void testSql2(){
		String sqlTest="SELECT c.id AS id, c.unique_id AS contractUniqueId, c.contract_no AS contractNo FROM contract c INNER JOIN asset_package ap ON ap.contract_id = c.id WHERE ap.loan_batch_id =:loanBatchId";
		Map<String, Object> params=new HashMap<>();
		params.put("loanBatchId", 1);
		String sql=null;
		try {
			 sql=FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper2_sub"), params);
			System.out.println(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertEquals(sqlTest, sql);
	}
	
}
