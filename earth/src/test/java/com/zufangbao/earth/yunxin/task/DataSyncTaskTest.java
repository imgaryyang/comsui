package com.zufangbao.earth.yunxin.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.zufangbao.earth.yunxin.api.dataSync.task.DataSyncTask;

/** 
* @author 作者 zhenghangbo
* @version 创建时间：Oct 25, 2016 5:43:11 PM 
* 类说明 
*/


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml" })

@TransactionConfiguration(defaultRollback = true)
@Component
public class DataSyncTaskTest {

	@Autowired
	private DataSyncTask dataSyncTask;
	
	
	@Value("#{config['yx.syncCreateRepaymentPlan.url']}")
	private   String SYNC_CREATE_REPAYMENT_PLAN_URL = null;
	

	
	
}
