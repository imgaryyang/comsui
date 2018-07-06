package com.zufangbao.earth.yunxin.api;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.yunxin.api.handler.MutableFeeDetailApiHandler;
import com.zufangbao.earth.yunxin.api.model.query.MutableFeeDetailsResultModel;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.log.MutableFeeDetailLogVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class MutableFeeDetailApiHandlerTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	private MutableFeeDetailApiHandler mutableFeeDetailApiHandler;
	
	@Test
	@Sql("classpath:test/yunxin/mutableFee/test_normalQueryMutableFeeDetails.sql")
	public void test_normalQueryMutableFeeDetails() {
		String singleLoanContractNo = "ZC1561074340376481792";
		MutableFeeDetailsResultModel resultModel = mutableFeeDetailApiHandler.queryMutableFeeDetails(singleLoanContractNo, null);
		List<MutableFeeDetailLogVO> mutableFeeDetailLogs = resultModel.getMutableFeeDetailLogs();
		assertEquals(4,resultModel.getCount());
		assertEquals(0,DateUtils.parseDate("2017-04-10 14:29:52", DateUtils.LONG_DATE_FORMAT).compareTo(mutableFeeDetailLogs.get(0).getEffectiveTime()));
		assertEquals(0,DateUtils.parseDate("2017-04-10 00:00:00", DateUtils.LONG_DATE_FORMAT).compareTo(mutableFeeDetailLogs.get(0).getApprovedTime()));
		assertEquals("MF1492105792654",mutableFeeDetailLogs.get(0).getMutableFeeNo());
		assertEquals("0",mutableFeeDetailLogs.get(0).getReasonCode());
		assertEquals(new BigDecimal("80.00"),mutableFeeDetailLogs.get(0).getOriginalAssetInterestValue());
		assertEquals(new BigDecimal("60.00"),mutableFeeDetailLogs.get(0).getAssetInterestValue());
		assertEquals(0,DateUtils.parseDate("2017-04-10 16:29:52", DateUtils.LONG_DATE_FORMAT).compareTo(mutableFeeDetailLogs.get(0).getCreateTime()));
		assertEquals("FXF",mutableFeeDetailLogs.get(0).getApprover());
		assertEquals("TestInterface",mutableFeeDetailLogs.get(0).getComment());
		int last = mutableFeeDetailLogs.size()-1;
		assertEquals(0,DateUtils.parseDate("2017-04-10 12:12:40", DateUtils.LONG_DATE_FORMAT).compareTo(mutableFeeDetailLogs.get(last).getEffectiveTime()));
		assertEquals(0,DateUtils.parseDate("2017-04-08 00:00:00", DateUtils.LONG_DATE_FORMAT).compareTo(mutableFeeDetailLogs.get(last).getApprovedTime()));
		assertEquals("MF1491797560886",mutableFeeDetailLogs.get(last).getMutableFeeNo());
		assertEquals("0",mutableFeeDetailLogs.get(last).getReasonCode());
		assertEquals(new BigDecimal("20.00"),mutableFeeDetailLogs.get(last).getOriginalAssetInterestValue());
		assertEquals(new BigDecimal("60.00"),mutableFeeDetailLogs.get(last).getAssetInterestValue());
		assertEquals(0,DateUtils.parseDate("2017-04-10 12:12:40", DateUtils.LONG_DATE_FORMAT).compareTo(mutableFeeDetailLogs.get(last).getCreateTime()));
		assertEquals("FXF",mutableFeeDetailLogs.get(last).getApprover());
		assertEquals("TestInterface",mutableFeeDetailLogs.get(last).getComment());
	}
	
	@Test
	@Sql("classpath:test/yunxin/mutableFee/test_pagedQueryMutableFeeDetails.sql")
	public void test_pagedQueryMutableFeeDetails() {
		String singleLoanContractNo = "ZC1561074340376481792";
		Page page = new Page(1, 2);
		MutableFeeDetailsResultModel resultModel = mutableFeeDetailApiHandler.queryMutableFeeDetails(singleLoanContractNo, page);
		List<MutableFeeDetailLogVO> mutableFeeDetailLogs = resultModel.getMutableFeeDetailLogs();
		assertEquals(4,resultModel.getCount());
		assertEquals(0,DateUtils.parseDate("2017-04-10 14:29:52", DateUtils.LONG_DATE_FORMAT).compareTo(mutableFeeDetailLogs.get(0).getEffectiveTime()));
		assertEquals(0,DateUtils.parseDate("2017-04-10 00:00:00", DateUtils.LONG_DATE_FORMAT).compareTo(mutableFeeDetailLogs.get(0).getApprovedTime()));
		assertEquals("MF1492105792654",mutableFeeDetailLogs.get(0).getMutableFeeNo());
		assertEquals("0",mutableFeeDetailLogs.get(0).getReasonCode());
		assertEquals(new BigDecimal("80.00"),mutableFeeDetailLogs.get(0).getOriginalAssetInterestValue());
		assertEquals(new BigDecimal("60.00"),mutableFeeDetailLogs.get(0).getAssetInterestValue());
		assertEquals(0,DateUtils.parseDate("2017-04-10 16:29:52", DateUtils.LONG_DATE_FORMAT).compareTo(mutableFeeDetailLogs.get(0).getCreateTime()));
		assertEquals("FXF",mutableFeeDetailLogs.get(0).getApprover());
		assertEquals("TestInterface",mutableFeeDetailLogs.get(0).getComment());
		int last = mutableFeeDetailLogs.size()-1;
		assertEquals(0,DateUtils.parseDate("2017-04-10 14:29:52", DateUtils.LONG_DATE_FORMAT).compareTo(mutableFeeDetailLogs.get(last).getEffectiveTime()));
		assertEquals(0,DateUtils.parseDate("2017-04-10 00:00:00", DateUtils.LONG_DATE_FORMAT).compareTo(mutableFeeDetailLogs.get(last).getApprovedTime()));
		assertEquals("MF1491905792280",mutableFeeDetailLogs.get(last).getMutableFeeNo());
		assertEquals("0",mutableFeeDetailLogs.get(last).getReasonCode());
		assertEquals(new BigDecimal("80.00"),mutableFeeDetailLogs.get(last).getOriginalAssetInterestValue());
		assertEquals(new BigDecimal("60.00"),mutableFeeDetailLogs.get(last).getAssetInterestValue());
		assertEquals(0,DateUtils.parseDate("2017-04-10 15:29:52", DateUtils.LONG_DATE_FORMAT).compareTo(mutableFeeDetailLogs.get(last).getCreateTime()));
		assertEquals("FXF",mutableFeeDetailLogs.get(last).getApprover());
		assertEquals("TestInterface",mutableFeeDetailLogs.get(last).getComment());
	}
}