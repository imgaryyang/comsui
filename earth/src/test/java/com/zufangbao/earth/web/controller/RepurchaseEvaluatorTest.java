package com.zufangbao.earth.web.controller;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.demo2do.core.utils.DateUtils;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.repurchase.RepurchaseCycle;
import com.zufangbao.sun.entity.repurchase.RepurchaseEvaluator;
import com.zufangbao.sun.entity.repurchase.RepurchaseTaskInstruction;
import com.zufangbao.sun.service.FinancialContractService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/local/applicationContext-*.xml" })
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class RepurchaseEvaluatorTest  extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	private FinancialContractService financialContractService;

	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4IsRepurchaseDay_empty.sql")
	public void test_RepurchaseEvaluator__date_null() {
		FinancialContract financialContract = financialContractService.getFinancialContractBy("e2c178ac-be53-4574-b936-c5d40ba00691");
		RepurchaseTaskInstruction result = RepurchaseEvaluator.evaluateRepurchaseTaskInstruction(null, financialContract);
		assertEquals(null, result);
	}
	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4IsRepurchaseDay_empty.sql")
	public void test_RepurchaseEvaluator__fc_null() {
		Date curTime = DateUtils.asDay("2017-05-08");
		RepurchaseTaskInstruction result = RepurchaseEvaluator.evaluateRepurchaseTaskInstruction(curTime, null);
		assertEquals(null, result);
	}
	
	/**
	 * 不定期回购：空，无临时回购任务
	 */
	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4IsRepurchaseDay_empty.sql")
	public void test_RepurchaseEvaluator_empty() {
		Date curTime = DateUtils.asDay("2017-05-08");
		FinancialContract financialContract = financialContractService.getFinancialContractBy("e2c178ac-be53-4574-b936-c5d40ba00691");
		RepurchaseTaskInstruction result = RepurchaseEvaluator.evaluateRepurchaseTaskInstruction(curTime, financialContract);
		assertEquals(null, result);
	}
	
	/**
	 * 不定期回购：空，有临时回购任务但无效
	 */
	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4IsRepurchaseDay_temporary_1.sql")
	public void test_RepurchaseEvaluator_temporary_1() {
		Date curTime = DateUtils.asDay("2017-05-08");
		FinancialContract financialContract = financialContractService.getFinancialContractBy("e2c178ac-be53-4574-b936-c5d40ba00691");
		RepurchaseTaskInstruction result = RepurchaseEvaluator.evaluateRepurchaseTaskInstruction(curTime, financialContract);
		assertEquals(null, result);
	}
	
	/**
	 * 不定期回购：空，有临时回购任务且有效，回购日为2017-05-08，有效期为2017-03-26到2017-05-27
	 */
	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4IsRepurchaseDay_temporary_2.sql")
	public void test_RepurchaseEvaluator_temporary_2() {
		Date curTime = DateUtils.asDay("2017-05-08");
		FinancialContract financialContract = financialContractService.getFinancialContractBy("e2c178ac-be53-4574-b936-c5d40ba00691");
		RepurchaseTaskInstruction result = RepurchaseEvaluator.evaluateRepurchaseTaskInstruction(curTime, financialContract);
		assertEquals("临时回购任务2017-05-08", result.getTaskDescription());
	}
	
	/**
	 * 不定期回购：每月9日（非当日），有临时回购任务但无效
	 */
	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4IsRepurchaseDay_temporary_3.sql")
	public void test_RepurchaseEvaluator_temporary_3() {
		Date curTime = DateUtils.asDay("2017-05-08");
		FinancialContract financialContract = financialContractService.getFinancialContractBy("e2c178ac-be53-4574-b936-c5d40ba00691");
		RepurchaseTaskInstruction result = RepurchaseEvaluator.evaluateRepurchaseTaskInstruction(curTime, financialContract);
		assertEquals(null, result);
	}
	
	/**
	 * 不定期回购：每月8日（当日），有临时回购任务但无效
	 */
	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4IsRepurchaseDay_temporary_4.sql")
	public void test_RepurchaseEvaluator_temporary_4() {
		Date curTime = DateUtils.asDay("2017-05-08");
		FinancialContract financialContract = financialContractService.getFinancialContractBy("e2c178ac-be53-4574-b936-c5d40ba00691");
		RepurchaseTaskInstruction result = RepurchaseEvaluator.evaluateRepurchaseTaskInstruction(curTime, financialContract);
		assertEquals("每月8日", result.getTaskDescription());
	}
	
	/**
	 * 不定期回购：每月8日，有临时回购任务且有效，回购日为2017-05-08，有效期为2017-03-26到2017-05-27
	 */
	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4IsRepurchaseDay_temporary_5.sql")
	public void test_RepurchaseEvaluator_temporary_5() {
		Date curTime = DateUtils.asDay("2017-05-08");
		FinancialContract financialContract = financialContractService.getFinancialContractBy("e2c178ac-be53-4574-b936-c5d40ba00691");
		RepurchaseTaskInstruction result = RepurchaseEvaluator.evaluateRepurchaseTaskInstruction(curTime, financialContract);
		assertEquals("临时回购任务2017-05-08", result.getTaskDescription());
	}
	
	/**
	 * 不定期回购：每周2，周4，有多条临时回购任务，且全无效
	 */
	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4IsRepurchaseDay_week_1.sql")
	public void test_RepurchaseEvaluator_week_1() {
		Date curTime = DateUtils.asDay("2017-05-08");
		FinancialContract financialContract = financialContractService.getFinancialContractBy("e2c178ac-be53-4574-b936-c5d40ba00691");
		RepurchaseTaskInstruction result = RepurchaseEvaluator.evaluateRepurchaseTaskInstruction(curTime, financialContract);
		assertEquals(null, result);
	}
	
	/**
	 * 不定期回购：每周1，周3，有多条临时回购任务，且全无效
	 */
	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4IsRepurchaseDay_week_2.sql")
	public void test_RepurchaseEvaluator_week_2() {
		Date curTime = DateUtils.asDay("2017-05-08");
		FinancialContract financialContract = financialContractService.getFinancialContractBy("e2c178ac-be53-4574-b936-c5d40ba00691");
		RepurchaseTaskInstruction result = RepurchaseEvaluator.evaluateRepurchaseTaskInstruction(curTime, financialContract);
		assertEquals("每周1", result.getTaskDescription());
	}
	
	/**
	 * 不定期回购：每周1，周3，有多条临时回购任务，部分有效，部分无效
	 */
	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4IsRepurchaseDay_week_3.sql")
	public void test_RepurchaseEvaluator_week_3() {
		Date curTime = DateUtils.asDay("2017-05-08");
		FinancialContract financialContract = financialContractService.getFinancialContractBy("e2c178ac-be53-4574-b936-c5d40ba00691");
		RepurchaseTaskInstruction result = RepurchaseEvaluator.evaluateRepurchaseTaskInstruction(curTime, financialContract);
		assertEquals("临时回购任务2017-05-08", result.getTaskDescription());
	}
	
	/**
	 * 不定期回购：每周1，周3，有多条临时回购任务，且全有效
	 */
	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4IsRepurchaseDay_week_4.sql")
	public void test_RepurchaseEvaluator_week_4() {
		Date curTime = DateUtils.asDay("2017-05-08");
		FinancialContract financialContract = financialContractService.getFinancialContractBy("e2c178ac-be53-4574-b936-c5d40ba00691");
		RepurchaseTaskInstruction result = RepurchaseEvaluator.evaluateRepurchaseTaskInstruction(curTime, financialContract);
		assertEquals("临时回购任务2017-05-08", result.getTaskDescription());
	}
	

	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4IsRepurchaseDay_empty.sql")
	public void test_isUnscheduledRepurchaseDay__date_null() {
		List<Integer> days = new ArrayList<Integer>();
		days.add(1);
		RepurchaseTaskInstruction result = RepurchaseEvaluator.isUnscheduledRepurchaseDay(null, days, RepurchaseCycle.MONTH);
		assertEquals(null, result);
	}
	
	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4IsRepurchaseDay_empty.sql")
	public void test_isUnscheduledRepurchaseDay__days_empty() {
		Date curTime = DateUtils.asDay("2017-05-08");
		List<Integer> days = null;
		RepurchaseTaskInstruction result = RepurchaseEvaluator.isUnscheduledRepurchaseDay(curTime, days, RepurchaseCycle.MONTH);
		assertEquals(null, result);
	}
	
	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4IsRepurchaseDay_empty.sql")
	public void test_isUnscheduledRepurchaseDay__RepurchaseCycle_null() {
		Date curTime = DateUtils.asDay("2017-05-08");
		List<Integer> days = new ArrayList<Integer>();
		days.add(1);
		RepurchaseTaskInstruction result = RepurchaseEvaluator.isUnscheduledRepurchaseDay(curTime, days, null);
		assertEquals(null, result);
	}
	
	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4IsRepurchaseDay_empty.sql")
	public void test_isUnscheduledRepurchaseDay__RepurchaseCycle_empty() {
		Date curTime = DateUtils.asDay("2017-05-08");
		List<Integer> days = new ArrayList<Integer>();
		days.add(1);
		RepurchaseTaskInstruction result = RepurchaseEvaluator.isUnscheduledRepurchaseDay(curTime, days, RepurchaseCycle.EMPTY);
		assertEquals(null, result);
	}
}
