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
public class reportWrapper8Test {
	
	@Autowired
	private SqlCacheManager sqlCacheManager;
	 
	@Test
	public void testSql1(){
		String sqlTest="SELECT c.unique_id AS uniqueId, rd.repurchase_doc_uuid AS repurchaseDocUuid, rd.contract_no AS contractNo, rd.repo_start_date AS repoStartDate, rd.repo_end_date AS repoEndDate, rd.app_name AS appName, rd.customer_name AS customerName, rd.amount AS amount, rd.repo_days AS repoDays, rd.creat_time AS createTime, rd.repurchase_status AS repoStatus FROM repurchase_doc rd LEFT JOIN contract c ON c.id = rd.contract_id WHERE rd.financial_contract_uuid IN (:fcList)       AND rd.repurchase_status IN (:repurchaseStatusList)";
		Map<String, Object> params=new HashMap<>();
		
		params.put("fcList", "%5B%22d2812bc5-5057-4a91-b3fd-9019506f0499%22%2C%22eec7e6d8-6123-4a61-bcee-18085334c8ff%22%2C%22d8d36e0d-06c6-4b17-b3aa-a1724ffb398e%22%5D");
		params.put("repurchaseStatusList", "%5B0%2C1%2C2%2C3%5D");

		String sql=null;
		try {
			 sql=FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper8"), params);
			System.out.println(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(sqlTest.trim(), sql.trim());
	}
	
	
	@Test
	public void testSql2(){
		String sqlTest="SELECT c.unique_id AS uniqueId, rd.repurchase_doc_uuid AS repurchaseDocUuid, rd.contract_no AS contractNo, rd.repo_start_date AS repoStartDate, rd.repo_end_date AS repoEndDate, rd.app_name AS appName, rd.customer_name AS customerName, rd.amount AS amount, rd.repo_days AS repoDays, rd.creat_time AS createTime, rd.repurchase_status AS repoStatus FROM repurchase_doc rd LEFT JOIN contract c ON c.id = rd.contract_id WHERE rd.financial_contract_uuid IN (:fcList)  AND rd.app_id =:appId";
		Map<String, Object> params=new HashMap<>();
		
		params.put("fcList", "%5B%22d2812bc5-5057-4a91-b3fd-9019506f0499%22%2C%22eec7e6d8-6123-4a61-bcee-18085334c8ff%22%2C%22d8d36e0d-06c6-4b17-b3aa-a1724ffb398e%22%5D");
		params.put("appId", 3);

		String sql=null;
		try {
			 sql=FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper8"), params);
			System.out.println(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(sqlTest.trim(), sql.trim());
	}
	
	@Test
	public void testSql3(){
		String sqlTest="SELECT c.unique_id AS uniqueId, rd.repurchase_doc_uuid AS repurchaseDocUuid, rd.contract_no AS contractNo, rd.repo_start_date AS repoStartDate, rd.repo_end_date AS repoEndDate, rd.app_name AS appName, rd.customer_name AS customerName, rd.amount AS amount, rd.repo_days AS repoDays, rd.creat_time AS createTime, rd.repurchase_status AS repoStatus FROM repurchase_doc rd LEFT JOIN contract c ON c.id = rd.contract_id WHERE rd.financial_contract_uuid IN (:fcList)      ";
		Map<String, Object> params=new HashMap<>();
		
		params.put("fcList", "%5B%22d2812bc5-5057-4a91-b3fd-9019506f0499%22%2C%22eec7e6d8-6123-4a61-bcee-18085334c8ff%22%2C%22d8d36e0d-06c6-4b17-b3aa-a1724ffb398e%22%5D");

		String sql=null;
		try {
			 sql=FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper8"), params);
			System.out.println(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(sqlTest.trim(), sql.trim());
	}
	
	@Test
	public void testSql4(){
		String sqlTest="SELECT c.unique_id AS uniqueId, rd.repurchase_doc_uuid AS repurchaseDocUuid, rd.contract_no AS contractNo, rd.repo_start_date AS repoStartDate, rd.repo_end_date AS repoEndDate, rd.app_name AS appName, rd.customer_name AS customerName, rd.amount AS amount, rd.repo_days AS repoDays, rd.creat_time AS createTime, rd.repurchase_status AS repoStatus FROM repurchase_doc rd LEFT JOIN contract c ON c.id = rd.contract_id WHERE rd.financial_contract_uuid IN (:fcList)   AND rd.contract_no =:contractNo";
		Map<String, Object> params=new HashMap<>();
		
		params.put("fcList", "%5B%22d2812bc5-5057-4a91-b3fd-9019506f0499%22%2C%22eec7e6d8-6123-4a61-bcee-18085334c8ff%22%2C%22d8d36e0d-06c6-4b17-b3aa-a1724ffb398e%22%5D");
		params.put("contractNo", "1");

		String sql=null;
		try {
			 sql=FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper8"), params);
			System.out.println(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(sqlTest.trim(), sql.trim());
	}
	
	@Test
	public void testSql5(){
		String sqlTest="SELECT c.unique_id AS uniqueId, rd.repurchase_doc_uuid AS repurchaseDocUuid, rd.contract_no AS contractNo, rd.repo_start_date AS repoStartDate, rd.repo_end_date AS repoEndDate, rd.app_name AS appName, rd.customer_name AS customerName, rd.amount AS amount, rd.repo_days AS repoDays, rd.creat_time AS createTime, rd.repurchase_status AS repoStatus FROM repurchase_doc rd LEFT JOIN contract c ON c.id = rd.contract_id WHERE rd.financial_contract_uuid IN (:fcList)    AND rd.customer_name =:customerName   ";
		Map<String, Object> params=new HashMap<>();
		
		params.put("financialContractIds", "%5B38%2C39%2C40%5D");
		params.put("customerName", "13");
		String sql=null;
		try {
			 sql=FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper8"), params);
			System.out.println(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(sqlTest, sql);
	}
	
	@Test
	public void testSql6(){
		String sqlTest="SELECT c.unique_id AS uniqueId, rd.repurchase_doc_uuid AS repurchaseDocUuid, rd.contract_no AS contractNo, rd.repo_start_date AS repoStartDate, rd.repo_end_date AS repoEndDate, rd.app_name AS appName, rd.customer_name AS customerName, rd.amount AS amount, rd.repo_days AS repoDays, rd.creat_time AS createTime, rd.repurchase_status AS repoStatus FROM repurchase_doc rd LEFT JOIN contract c ON c.id = rd.contract_id WHERE rd.financial_contract_uuid IN (:fcList)     AND rd.repo_start_date >=:repoStartDate  AND rd.repo_end_date <=:repoEndDate ";
		Map<String, Object> params=new HashMap<>();
		
		params.put("financialContractIds", "%5B38%2C39%2C40%5D");
		params.put("repoStartDate", "2017-03-03");
		params.put("repoEndDate", "2017-03-04");
		String sql=null;
		try {
			 sql=FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper8"), params);
			System.out.println(sql);
			System.out.println(sqlTest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(sqlTest, sql);
	}
	
}
