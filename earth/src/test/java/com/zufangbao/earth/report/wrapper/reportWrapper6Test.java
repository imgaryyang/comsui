package com.zufangbao.earth.report.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class reportWrapper6Test {


	@Autowired
	private SqlCacheManager sqlCacheManager;
	
	
	
	@Test
	public void testSql1(){
		String sqlTest="SELECT c.unique_id AS uniqueId, asst.contract_id AS contractId, asst.asset_recycle_date AS assetRecycleDate, asst.asset_initial_value AS assetInitialValue, asst.actual_recycle_date AS actualRecycleDate, asst.guarantee_status AS guaranteeStatus, asst.single_loan_contract_no AS singleLoanContractNo, tmp_guarantee.* FROM ( SELECT ro.asset_set_id, ro.order_no AS orderNo, ro.due_date AS dueDate, ro.modify_time AS modifyTime, ro.total_rent AS totalRent, ro.financial_contract_id AS financialContractId FROM rent_order ro WHERE ro.order_type = 1 AND ro.financial_contract_id IN (:financialContractIdList) AND ro.order_no =:orderNo  AND ro.due_date >=:dueStartDate AND ro.due_date <=:dueEndDate ORDER BY ro.id DESC ) AS tmp_guarantee INNER JOIN asset_set asst ON asst.id = tmp_guarantee.asset_set_id AND asst.guarantee_status=:guaranteeStatus AND asst.single_loan_contract_no =:singleLoanContractNo AND asst.asset_recycle_date >=:startDate  INNER JOIN contract c ON c.id = asst.contract_id";
		Map<String, Object> params=new HashMap<>();
		
		List<String> financialContractIdList=new ArrayList<String>(){
			{
				add("38");
				add("39");
				add("40");
			}
		};
		
		params.put("financialContractIdList",financialContractIdList);
		params.put("orderNo", "1234567899897");
		params.put("dueStartDate", "2017-12-11");
		params.put("dueEndDate", "2017-12-11");
		params.put("startDate", "2017-12-11");
		params.put("EndDate", "456231656");

		params.put("guaranteeStatus", 1);
		params.put("singleLoanContractNo", "asdfasdfasdad4564123123");
		String sql=null;
		try {
			 sql=FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper6"), params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(sqlTest, sql);
	}
}
