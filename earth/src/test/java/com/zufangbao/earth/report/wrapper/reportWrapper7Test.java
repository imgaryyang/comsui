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
public class reportWrapper7Test {
	
	@Autowired
	private SqlCacheManager sqlCacheManager;
	 
	@Test
	public void testSql1(){
		String sqlTest="SELECT sr.settle_order_no AS settleOrderNo, sr.due_date AS dueDate, sr.overdue_days AS overdueDays, sr.overdue_penalty AS overduePenalty, sr.last_modify_time AS modifyTime, sr.settlement_amount AS settlementAmount, sr.`comment` AS `comment`, ast.single_loan_contract_no AS repaymentNo, ast.asset_recycle_date AS recycleDate, ast.settlement_status AS settlementStatus, ast.asset_initial_value AS principalAndInterestAmount, c.unique_id AS uniqueId, c.app_id AS appId FROM settlement_order sr INNER JOIN asset_set ast ON ast.id = sr.asset_set_id INNER JOIN contract c ON c.id = ast.contract_Id WHERE ast.financial_contract_uuid IN (:financialContractIdList)   AND c.app_id =:appId";
		Map<String, Object> params=new HashMap<>();
		
		params.put("financialContractIds", "%5B38%2C39%2C40%5D");
		params.put("appId", "pd");

		String sql=null;
		try {
			 sql=FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper7"), params);
			System.out.println(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(sqlTest.trim(), sql.trim());
	}
	
	
	@Test
	public void testSql2(){
		String sqlTest="SELECT sr.settle_order_no AS settleOrderNo, sr.due_date AS dueDate, sr.overdue_days AS overdueDays, sr.overdue_penalty AS overduePenalty, sr.last_modify_time AS modifyTime, sr.settlement_amount AS settlementAmount, sr.`comment` AS `comment`, ast.single_loan_contract_no AS repaymentNo, ast.asset_recycle_date AS recycleDate, ast.settlement_status AS settlementStatus, ast.asset_initial_value AS principalAndInterestAmount, c.unique_id AS uniqueId, c.app_id AS appId FROM settlement_order sr INNER JOIN asset_set ast ON ast.id = sr.asset_set_id INNER JOIN contract c ON c.id = ast.contract_Id WHERE ast.financial_contract_uuid IN (:financialContractIdList)    ";
		Map<String, Object> params=new HashMap<>();
		
		params.put("financialContractIds", "%5B38%2C39%2C40%5D");

		String sql=null;
		try {
			 sql=FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper7"), params);
			System.out.println(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(sqlTest, sql);
	}
	
	@Test
	public void testSql3(){
		String sqlTest="SELECT sr.settle_order_no AS settleOrderNo, sr.due_date AS dueDate, sr.overdue_days AS overdueDays, sr.overdue_penalty AS overduePenalty, sr.last_modify_time AS modifyTime, sr.settlement_amount AS settlementAmount, sr.`comment` AS `comment`, ast.single_loan_contract_no AS repaymentNo, ast.asset_recycle_date AS recycleDate, ast.settlement_status AS settlementStatus, ast.asset_initial_value AS principalAndInterestAmount, c.unique_id AS uniqueId, c.app_id AS appId FROM settlement_order sr INNER JOIN asset_set ast ON ast.id = sr.asset_set_id INNER JOIN contract c ON c.id = ast.contract_Id WHERE ast.financial_contract_uuid IN (:financialContractIdList)    AND sr.settle_order_no =:settleOrderNo";
		Map<String, Object> params=new HashMap<>();
		
		params.put("financialContractIds", "%5B38%2C39%2C40%5D");
		params.put("settleOrderNo", "ZS");
		String sql=null;
		try {
			 sql=FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper7"), params);
			System.out.println(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(sqlTest, sql);
	}
	
	@Test
	public void testSql4(){
		String sqlTest="SELECT sr.settle_order_no AS settleOrderNo, sr.due_date AS dueDate, sr.overdue_days AS overdueDays, sr.overdue_penalty AS overduePenalty, sr.last_modify_time AS modifyTime, sr.settlement_amount AS settlementAmount, sr.`comment` AS `comment`, ast.single_loan_contract_no AS repaymentNo, ast.asset_recycle_date AS recycleDate, ast.settlement_status AS settlementStatus, ast.asset_initial_value AS principalAndInterestAmount, c.unique_id AS uniqueId, c.app_id AS appId FROM settlement_order sr INNER JOIN asset_set ast ON ast.id = sr.asset_set_id INNER JOIN contract c ON c.id = ast.contract_Id WHERE ast.financial_contract_uuid IN (:financialContractIdList) AND ast.single_loan_contract_no =:singleLoanContractNo   ";
		Map<String, Object> params=new HashMap<>();
		
		params.put("financialContractIds", "%5B38%2C39%2C40%5D");
		params.put("singleLoanContractNo", "QS");
		String sql=null;
		try {
			 sql=FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper7"), params);
			System.out.println(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(sqlTest, sql);
	}
	
	@Test
	public void testSql5(){
		String sqlTest="SELECT sr.settle_order_no AS settleOrderNo, sr.due_date AS dueDate, sr.overdue_days AS overdueDays, sr.overdue_penalty AS overduePenalty, sr.last_modify_time AS modifyTime, sr.settlement_amount AS settlementAmount, sr.`comment` AS `comment`, ast.single_loan_contract_no AS repaymentNo, ast.asset_recycle_date AS recycleDate, ast.settlement_status AS settlementStatus, ast.asset_initial_value AS principalAndInterestAmount, c.unique_id AS uniqueId, c.app_id AS appId FROM settlement_order sr INNER JOIN asset_set ast ON ast.id = sr.asset_set_id INNER JOIN contract c ON c.id = ast.contract_Id WHERE ast.financial_contract_uuid IN (:financialContractIdList)  AND ast.settlement_status =:settlementStatus  ";
		Map<String, Object> params=new HashMap<>();
		
		params.put("financialContractIds", "%5B38%2C39%2C40%5D");
		params.put("settlementStatus", 1);
		String sql=null;
		try {
			 sql=FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper7"), params);
			System.out.println(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(sqlTest, sql);
	}
	
}
