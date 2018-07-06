package com.zufangbao.earth.yunxin.api;

import com.zufangbao.earth.yunxin.task.RepurchaseTask;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.RepurchaseService;
import com.zufangbao.sun.utils.DateUtils;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/local/applicationContext-*.xml" })
@Transactional
@Rollback(true)
public class RepurchaseTaskTest extends AbstractTransactionalJUnit4SpringContextTests{
	@Autowired
	private RepurchaseTask repurchaseTask;
	@Autowired
	private RepurchaseService repurchaseService;
	@Autowired
	private FinancialContractService financialContractService;

	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4genRepurchaseDoc.sql")
	public void test_genRepurchaseDoc() {
		repurchaseTask.genRepurchaseDoc();
		RepurchaseDoc doc = repurchaseService.getRepurchaseDocBy(234567L);
		assertEquals(new Long(234567), doc.getContractId());
		assertEquals("Sat Jun 11 00:00:00 CST 2016", doc.getRepoStartDate().toString());
		assertEquals(doc.getRepurchasePrincipal(), new BigDecimal("800.00"));
		assertEquals(doc.getRepurchaseInterest(), new BigDecimal("800.00"));
	}

	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4genRepurchaseDocWithOpenPrepayment.sql")
	public void test_genRepurchaseDoc_withOpenPrepayment() {
		repurchaseTask.genRepurchaseDoc();
		RepurchaseDoc doc = repurchaseService.getRepurchaseDocBy(123456l);
		assertNull(doc);
	}

	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4genRepurchaseDocWithLockedPrepayment.sql")
	public void test_genRepurchaseDoc_withLockedPrepayment() {
		repurchaseTask.genRepurchaseDoc();
		RepurchaseDoc doc = repurchaseService.getRepurchaseDocBy(123456l);
		assertEquals(new Long(123456), doc.getContractId());
		assertEquals("Mon Apr 15 00:00:00 CST 2013", doc.getRepoStartDate().toString());
	}
	
	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4genRepurchaseDoc.sql")
	public void test_genRepurchaseDoc_fail() {
		repurchaseTask.genRepurchaseDoc();
		RepurchaseDoc doc = repurchaseService.getRepurchaseDocBy(234567L);
		assertEquals(new Long(234567), doc.getContractId());
		assertNotEquals("Fri Aug 19 00:00:00 CST 2016", doc.getRepoStartDate().toString());
	}
	
	
	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4getRepurchaseDocUuidByContractId.sql")
	public void test_getRepurchaseUuidDocBy() {
//		repurchaseTask.genRepurchaseDoc();
		String docUuid = repurchaseService.getRepurchaseDocUuidBy(310052L);
		assertEquals(docUuid, "repurchase_doc_uuid_000");
	}
	

	/**
	 * 运行此测试时设置系统时间为15号,为休息日，顺延直至工作日
	 */
	@Test
	@Ignore("已过时")
	@Sql("classpath:test/yunxin/repaymentPlan/test4genRepurchaseDoc4PPDPH.sql")
	public void test_genRepurchaseDoc_ppdPH_j(){
		repurchaseTask.genRepurchaseDoc();
		RepurchaseDoc doc = repurchaseService.getRepurchaseDocBy(234567L);
//		assertEquals(new Long(234567), doc.getContractId());
	}
	
	@Test
	@Ignore("")
	public void aaaaa(){
		/*
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sf.format(cal.getTime());
		System.out.println(date);
		
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 15);
		
		Date date15 = cal.getTime();
		
		date = sf.format(date15);
		System.out.println(date);
		Date date2 = cal.getTime();
		*/
		/*
		Calendar cal = Calendar.getInstance();
		Date tomorrow = DateUtils.addDays(new Date(), 1);
		cal.setTime(tomorrow);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		System.out.println(day);
		*/
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
		for(int i=1; i<366; i++){
			System.out.println(cal.get(Calendar.YEAR)+"年"+cal.get(Calendar.MONTH)+"月"+cal.get(Calendar.DAY_OF_MONTH)+"日 "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND));
			if(!DateUtils.isWeekend(cal.getTime())){
				if(DateUtils.isSameDay(new Date(), cal.getTime())){
					System.out.println("yes");
				}
				System.out.println("no");
			}
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		
	}
	
	/**
	 * 2017-05-08当天有效
	 * 不定期回购：空，无临时回购任务
	 */
	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4IsRepurchaseDay_empty.sql")
	public void test_genRepurchaseDoc_empty() {
		repurchaseTask.genRepurchaseDoc();
		RepurchaseDoc doc = repurchaseService.getRepurchaseDocBy(123456l);
		assertEquals(null, doc);
	}
	
	/**
	 * 2017-05-08当天有效
	 * 不定期回购：空，有临时回购任务但无效
	 */
	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4IsRepurchaseDay_temporary_1.sql")
	public void test_genRepurchaseDoc_temporary_1() {
		repurchaseTask.genRepurchaseDoc();
		RepurchaseDoc doc = repurchaseService.getRepurchaseDocBy(123456l);
		assertEquals(null, doc);
	}
	
	/**
	 * 2017-05-08当天有效
	 * 不定期回购：空，有临时回购任务且有效，回购日为2017-05-08，有效期为2017-03-26到2017-05-27（2017-05-08日当天有效）
	 * 临时回购任务触发回购
	 */
	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4IsRepurchaseDay_temporary_2.sql")
	public void test_genRepurchaseDoc_temporary_2() {
		repurchaseTask.genRepurchaseDoc();
		RepurchaseDoc doc = repurchaseService.getRepurchaseDocBy(123456l);
		assertEquals(new Long(123456), doc.getContractId());
		assertEquals("不定期：临时回购任务2017-05-08", doc.getRepurchaseRuleDetail());
	}
	
	/**
	 * 2017-05-08当天有效
	 * 不定期回购：每月9日（非当日），有临时回购任务但无效
	 */
	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4IsRepurchaseDay_temporary_3.sql")
	public void test_genRepurchaseDoc_temporary_3() {
		repurchaseTask.genRepurchaseDoc();
		RepurchaseDoc doc = repurchaseService.getRepurchaseDocBy(123456l);
		assertEquals(null, doc);
	}
	
	/**
	 * 2017-05-08当天有效
	 * 不定期回购：每月8日（当日），有临时回购任务但无效
	 * 不定期每月8日触发回购
	 */
	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4IsRepurchaseDay_temporary_4.sql")
	public void test_genRepurchaseDoc_temporary_4() {
		repurchaseTask.genRepurchaseDoc();
		RepurchaseDoc doc = repurchaseService.getRepurchaseDocBy(123456l);
		assertEquals(new Long(123456), doc.getContractId());
		assertEquals("不定期：每月8日", doc.getRepurchaseRuleDetail());
	}
	
	/**
	 * 2017-05-08当天有效
	 * 不定期回购：每月8日，有临时回购任务且有效，回购日为2017-05-08，有效期为2017-03-26到2017-05-27（2017-05-08日当天有效）
	 * 临时回购任务生效
	 */
	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4IsRepurchaseDay_temporary_5.sql")
	public void test_genRepurchaseDoc_temporary_5() {
		repurchaseTask.genRepurchaseDoc();
		RepurchaseDoc doc = repurchaseService.getRepurchaseDocBy(123456l);
		assertEquals(new Long(123456), doc.getContractId());
		assertEquals("不定期：临时回购任务2017-05-08", doc.getRepurchaseRuleDetail());
	}
	
	/**
	 * 2017-05-08当天有效
	 * 不定期回购：每周2，周4，有多条临时回购任务，且全无效
	 */
	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4IsRepurchaseDay_week_1.sql")
	public void test_genRepurchaseDoc_week_1() {
		repurchaseTask.genRepurchaseDoc();
		RepurchaseDoc doc = repurchaseService.getRepurchaseDocBy(123456l);
		assertEquals(null, doc);
	}
	
	/**
	 * 2017-05-08当天有效
	 * 不定期回购：每周1，周3，有多条临时回购任务，且全无效
	 * 不定期每周1触发回购
	 */
	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4IsRepurchaseDay_week_2.sql")
	public void test_genRepurchaseDoc_week_2() {
		repurchaseTask.genRepurchaseDoc();
		RepurchaseDoc doc = repurchaseService.getRepurchaseDocBy(123456l);
		assertEquals(new Long(123456), doc.getContractId());
		assertEquals("不定期：每周1", doc.getRepurchaseRuleDetail());
	}
	
	/**
	 * 2017-05-08当天有效
	 * 不定期回购：每周1，周3，有多条临时回购任务，部分有效，部分无效
	 * 临时回购任务2017-05-08触发回购
	 */
	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4IsRepurchaseDay_week_3.sql")
	public void test_genRepurchaseDoc_week_3() {
		repurchaseTask.genRepurchaseDoc();
		RepurchaseDoc doc = repurchaseService.getRepurchaseDocBy(123456l);
		assertEquals(new Long(123456), doc.getContractId());
		assertEquals("不定期：临时回购任务2017-05-08", doc.getRepurchaseRuleDetail());
	}
	
	/**
	 * 2017-05-08当天有效
	 * 不定期回购：每周1，周3，有多条临时回购任务，且全有效
	 * 临时回购任务2017-05-08触发回购
	 */
	@Test
	@Sql("classpath:test/yunxin/repaymentPlan/test4IsRepurchaseDay_week_4.sql")
	public void test_genRepurchaseDoc_week_4() {
		repurchaseTask.genRepurchaseDoc();
		RepurchaseDoc doc = repurchaseService.getRepurchaseDocBy(123456l);
		assertEquals(new Long(123456), doc.getContractId());
		assertEquals("不定期：临时回购任务2017-05-08", doc.getRepurchaseRuleDetail());
	}
}
