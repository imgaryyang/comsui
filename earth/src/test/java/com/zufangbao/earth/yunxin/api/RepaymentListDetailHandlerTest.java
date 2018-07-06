package com.zufangbao.earth.yunxin.api;


import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.zufangbao.earth.yunxin.api.handler.RepaymentListApitHandler;
import com.zufangbao.earth.yunxin.api.model.RepaymentListDetail;
import com.zufangbao.earth.yunxin.api.model.query.RepaymentListQueryModel;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml" })
@Transactional
public class RepaymentListDetailHandlerTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	private  RepaymentListApitHandler repaymentListApitHandler;
	
	
	@Test
	@Sql("classpath:test/yunxin/api/testQueryRepaymentListDetailForDeductPlan.sql")
	public void testQueryRepaymentListDetailForDeductPlan(){
			
			RepaymentListQueryModel queryModel = new RepaymentListQueryModel();
			queryModel.setRequestNo("1234567");
			queryModel.setFinancialContractNo("G31700");
			queryModel.setQueryStartDate("2016-11-01");
			queryModel.setQueryEndDate("2016-12-01");
			List<RepaymentListDetail>  RepaymentListDetails = repaymentListApitHandler.queryRepaymentList(queryModel);
			Assert.assertEquals(2, RepaymentListDetails.size());
			RepaymentListDetail RepaymentListDetail = RepaymentListDetails.get(0);
			RepaymentListDetail RepaymentListDetail2 = RepaymentListDetails.get(1);
			Assert.assertEquals("2016-11-21", RepaymentListDetail.getDeductDate());
			Assert.assertEquals("ZC27561A5DCB1B70BF,ZC27561A5DCB207334", RepaymentListDetail.getAssetSetNo());
			Assert.assertEquals("60.00", RepaymentListDetail.getDeductAmount());
			Assert.assertEquals("yq-17-0b91787f-d258-451f-8eba-dc4b973e8b370", RepaymentListDetail.getContractNo());
			Assert.assertEquals("1c7f58c3-d724-43f3-aba6-ae18d243e443", RepaymentListDetail.getUniqueId());
			Assert.assertEquals("00000016090", RepaymentListDetail.getDeductAccountNo());
			Assert.assertEquals("测试员", RepaymentListDetail.getDeductAccountName());
			Assert.assertEquals("失败", RepaymentListDetail.getResults());
	
			Assert.assertEquals("2016-11-21", RepaymentListDetail2.getDeductDate());
			Assert.assertEquals("ZC27561A5DCB1B70BF,ZC27561A5DCB207334", RepaymentListDetail2.getAssetSetNo());
			Assert.assertEquals("60.00", RepaymentListDetail2.getDeductAmount());
			Assert.assertEquals("yq-17-0b91787f-d258-451f-8eba-dc4b973e8b370", RepaymentListDetail2.getContractNo());
			Assert.assertEquals("1c7f58c3-d724-43f3-aba6-ae18d243e443", RepaymentListDetail2.getUniqueId());
			Assert.assertEquals("00000016090", RepaymentListDetail2.getDeductAccountNo());
			Assert.assertEquals("测试员", RepaymentListDetail2.getDeductAccountName());
			Assert.assertEquals("成功", RepaymentListDetail2.getResults());
	}
	
	@Test
	@Sql("classpath:test/yunxin/api/testQueryRepaymentListDetailForDeductPlan_sort.sql")
	public void testQueryRepaymentListDetailForDeductPlan_sort(){
			
			RepaymentListQueryModel queryModel = new RepaymentListQueryModel();
			queryModel.setRequestNo("1234567");
			queryModel.setFinancialContractNo("G31700");
			queryModel.setQueryStartDate("2016-11-01");
			queryModel.setQueryEndDate("2016-12-01");
			List<RepaymentListDetail>  RepaymentListDetails = repaymentListApitHandler.queryRepaymentList(queryModel);
			Assert.assertEquals(10, RepaymentListDetails.size());
			RepaymentListDetail RepaymentListDetail = RepaymentListDetails.get(0);
			RepaymentListDetail RepaymentListDetail2 = RepaymentListDetails.get(1);
			RepaymentListDetail RepaymentListDetail3 = RepaymentListDetails.get(2);
			RepaymentListDetail RepaymentListDetail4 = RepaymentListDetails.get(3);
			RepaymentListDetail RepaymentListDetail5 = RepaymentListDetails.get(4);
			Assert.assertEquals("2016-11-02", RepaymentListDetail.getDeductDate());
			Assert.assertEquals("2016-11-02", RepaymentListDetail2.getDeductDate());
			Assert.assertEquals("2016-11-03", RepaymentListDetail3.getDeductDate());
			Assert.assertEquals("2016-11-03", RepaymentListDetail4.getDeductDate());
			Assert.assertEquals("2016-11-15", RepaymentListDetail5.getDeductDate());
	}
	
	
}
