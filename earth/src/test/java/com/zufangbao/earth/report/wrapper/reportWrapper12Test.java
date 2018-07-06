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
public class reportWrapper12Test {

	@Autowired
	private SqlCacheManager sqlCacheManager;

	@Test
	public void testSql1() {
		String sqlTest = "SELECT cf.bank_sequence_no AS bankSequenceNo, cf.account_side AS accountSide, cf.transaction_amount AS transactionAmount, cf.balance AS balance, cf.counter_account_no AS counterAccountNo, cf.counter_account_name AS counterAccountName, cf.transaction_time AS transactionTime, cf.remark AS remark FROM cash_flow cf WHERE cf.cash_flow_channel_type = 0 AND cf.transaction_time >=:startTime AND cf.transaction_time <:endTime        ORDER BY cf.transaction_time DESC";
		Map<String, Object> params = new HashMap<>();

		params.put("startTime", "2017-02-14 00:00:00");
		params.put("endTime", "2017-02-16 23:59:59");

		String sql = null;
		try {
			sql = FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper12"), params);
			System.out.println(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(sqlTest.trim(), sql.trim());
	}

	@Test
	public void testSql2() {
		String sqlTest = "SELECT cf.bank_sequence_no AS bankSequenceNo, cf.account_side AS accountSide, cf.transaction_amount AS transactionAmount, cf.balance AS balance, cf.counter_account_no AS counterAccountNo, cf.counter_account_name AS counterAccountName, cf.transaction_time AS transactionTime, cf.remark AS remark FROM cash_flow cf WHERE cf.cash_flow_channel_type = 0 AND cf.transaction_time >=:startTime AND cf.transaction_time <:endTime AND cf.account_side =:accountSide AND cf.audit_status =:auditStatus AND cf.host_account_no =:hostAccountNo AND cf.bank_sequence_no =:bankSequenceNo AND cf.counter_account_no =:counterAccountNo AND cf.counter_account_name =:counterAccountName AND cf.remark LIKE :remark ORDER BY cf.transaction_time DESC";
		Map<String, Object> params = new HashMap<>();

		params.put("hostAccountNo", "600000000001");
		params.put("accountSide", 1);
		params.put("auditStatus", 1);
		params.put("startTime", "2017-02-14 00:00:00");
		params.put("endTime", "2017-02-16 23:59:59");
		params.put("bankSequenceNo", "61bf5c03-f380-11e6-822c-525400dbb013");
		params.put("counterAccountNo", "1001133419006708197");
		params.put("counterAccountName", "上海拍拍贷金融信息服务有限公司");
		params.put("remark", "1005测试");

		String sql = null;
		try {
			sql = FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper12"), params);
			System.out.println(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(sqlTest.trim(), sql.trim());
	}

}
