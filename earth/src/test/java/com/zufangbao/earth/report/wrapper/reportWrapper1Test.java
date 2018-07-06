package com.zufangbao.earth.report.wrapper;

import java.util.ArrayList;
import java.util.Arrays;
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
public class reportWrapper1Test {
	
	@Autowired
	private SqlCacheManager sqlCacheManager;
	 
	@Test
	public void testSql1(){
		String sqlTest="SELECT tmpc.*, ca.payer_name AS payerName, ca.id_card_num AS idCardNum, ca.bank AS bank, ca.province AS province, ca.city AS city, ca.pay_ac_no AS payAcNo, (SELECT max(asst.asset_recycle_date) FROM asset_set asst WHERE (active_status = 0 or executing_status IN (3,4,5)) AND contract_id = tmpc.cid) AS dueDate, (SELECT sum(asst.asset_interest_value) FROM asset_set asst WHERE active_status = 0 AND contract_id = tmpc.cid ) AS totalInterest, (SELECT CONCAT_WS(\",\", sum(asst.asset_principal_value), sum(asst.asset_interest_value)) FROM asset_set asst WHERE active_status = 0 AND asset_status = 0 AND contract_id = tmpc.cid ) AS restFeeArray FROM (SELECT c.id AS cid, c.unique_id AS uniqueId, c.contract_no AS contractNo, c.begin_date AS beginDate, c.total_amount AS totalPrincipal, c.periods AS periods, c.repayment_way AS repaymentWay, c.interest_rate AS interestRate, c.penalty_interest AS penaltyInterest, c.financial_contract_uuid FROM contract c WHERE c.financial_contract_uuid IN (:financialContractUuids)  AND (   c.state = 0  OR    c.state = 2  OR    c.state = 4  OR    c.state = 5  OR    c.completionStatus = 0  OR    c.completionStatus = 1   )   AND c.unique_id = :uniqueId   AND c.contract_no = :contractNo   AND c.begin_date >= :startDate   AND c.begin_date <= :endDate  ) AS tmpc LEFT JOIN contract_account ca ON tmpc.cid = ca.contract_id AND ca.contract_account_type = 0 AND ca.thru_date = '2900-01-01 00:00:00'  WHERE ca.payer_name = :customerName ";
		Map<String, Object> params=new HashMap<>();
		params.put("financialContractUuids", Arrays.asList(new String[] { "e0f67c64-38ad-48aa-a310-ca610b7f74b0","d8d36e0d-06c6-4b17-b3aa-a1724ffb398e" }));
		params.put("stateAdaptaterList", new ArrayList<Integer>() {
			private static final long serialVersionUID = 1L;
			{
				add(0);
				add(2);
				add(4);
				add(5);
				add(7);
				add(8);
			}
		});
		params.put("contractNo","云信信2016-152-DK(ww222222)号");
		params.put("startDate", "2015-01-01");
		params.put("endDate", "2017-02-15");
		params.put("customerName", "测试员2034");
		params.put("uniqueId", "e3876f95-a930-4e96-9614-ea33ce3422d5");
		String sql=null;
		try {
			 sql=FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper1"), params);
			System.out.println(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(sqlTest, sql);
	}
}
