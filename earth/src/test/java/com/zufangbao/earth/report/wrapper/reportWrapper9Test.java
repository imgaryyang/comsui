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
		"classpath:/local/applicationContext-*.xml", "classpath:/DispatcherServlet.xml" })
@Transactional
@WebAppConfiguration(value="webapp")
public class reportWrapper9Test {

	@Autowired
	private SqlCacheManager sqlCacheManager;

	@Test
	public void testSql1() {
		String sqlTest = "SELECT rpel.exec_req_no as execReqNo, rpel.exec_rsp_no as execRspNo, rpel.planned_amount as plannedAmount, rpel.last_modified_time as lastModifiedTime, rpel.cp_bank_card_no as cpBankCardNo, rpel.cp_id_number as cpIdNumber, rpel.reverse_status as reverseStatus, rpel.execution_status as executionStatus FROM t_remittance_plan_exec_log rpel WHERE rpel.financial_contract_uuid =:financialContractUuid AND rpel.create_time >=:fromDate AND rpel.create_time <=:toDate";
		Map<String, Object> params = new HashMap<>();

		params.put("financialContractUuid", "d2812bc5-5057-4a91-b3fd-9019506f0499");
		params.put("fromDate", "2017-02-02 00:00:00");
		params.put("toDate", "2017-02-08 23:59:59");

		String sql = null;
		try {
			sql = FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper9_rel"), params);
			System.out.println(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(sqlTest.trim(), sql.trim());
	}

	@Test
	public void testSql2() {
		String sqlTest = "SELECT cf.cash_flow_uuid as cashFlowUuid, cf.counter_account_name as counterAccountName, cf.counter_account_no as counterAccountNo, cf.transaction_amount as transactionAmount, cf.transaction_time as transactionTime, cf.trade_uuid as tradeUuid, cf.remark as remark, cf.account_side as accountSide FROM cash_flow cf WHERE cf.host_account_no =:hostAccountNo AND cf.transaction_time >=:fromDate AND cf.transaction_time <=:toDate AND cf.account_side = 0";
		Map<String, Object> params = new HashMap<>();

		params.put("hostAccountNo", "d2812bc5");
		params.put("fromDate", "2017-02-02 00:00:00");
		params.put("toDate", "2017-02-08 23:59:59");

		String sql = null;
		try {
			sql = FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper9_rcf"), params);
			System.out.println(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(sqlTest.trim(), sql.trim());
	}
}
