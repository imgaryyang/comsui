package com.zufangbao.earth.yunxin.handler;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.zufangbao.earth.yunxin.api.handler.RepaymentListApitHandler;
import com.zufangbao.earth.yunxin.api.model.RepaymentListDetail;
import com.zufangbao.earth.yunxin.api.model.query.RepaymentListQueryModel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class RepaymentListApiHandlerTest {
	@Autowired
	private RepaymentListApitHandler repaymentListApiHandler;
	
	@Test
	@Ignore("数据资源没有准备")
	public void testQueryRepaymentList(){
		RepaymentListQueryModel queryModel = new RepaymentListQueryModel();
		queryModel.setFinancialContractNo("21");
		queryModel.setRequestNo("3333");
		queryModel.setQueryStartDate("123");
		queryModel.setQueryEndDate("123123");
		List<RepaymentListDetail> queryRepaymentList = repaymentListApiHandler.queryRepaymentList(queryModel);
		Assert.assertFalse(queryRepaymentList.isEmpty());
		
	}
}
