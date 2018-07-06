package com.suidifu.bridgewater.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.api.model.RemittanceQueryResult;
import com.suidifu.bridgewater.api.model.RemittanceResultBatchPackModel;
import com.suidifu.bridgewater.api.model.RemittanceResultBatchQueryModel;

/**
 * @author chengll
 * 测试批量查询放款结果接口
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/context/applicationContext-*.xml",
		"classpath:/local/applicationContext-*.xml" })
@Transactional
public class RemittanceQueryApiHandlerTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	private IRemittanceQueryApiHandler iRemittanceQueryApiHandler;


	/**
	 * 存在参数为空或者错误的情况,
	 */
	@Test
	@Sql("classpath:/test/remittance/test_getRemittanceResultBy.sql")
	public void testGetRemittanceApplicationsByNullOrErrorParam() {


		//这里测试uniqueId和oriRequestNo 与fn和requestNo值无关
		String fn = "100011";
		String requestNo = UUID.randomUUID().toString();

		//可以设置唯恐的参数
		String uniqueId = "FANT078";
		String oriRequestNo = "9cde18e1-b926-4816-81bc-62b3cc0458fb";
		RemittanceResultBatchQueryModel batchQueryModel = new RemittanceResultBatchQueryModel();
		batchQueryModel.setOriRequestNo(oriRequestNo);
		batchQueryModel.setUniqueId(uniqueId);

		String uniqueId2 = "FANT200";
		String oriRequestNo2 = "f9b542db-2c34-4cd2-848e-3021b2683b6d";
		RemittanceResultBatchQueryModel batchQueryModel2 = new RemittanceResultBatchQueryModel();
		batchQueryModel2.setOriRequestNo(oriRequestNo2);
		batchQueryModel2.setUniqueId(uniqueId2);

		List<RemittanceResultBatchQueryModel> batchQueryModelList = new ArrayList<>();
		batchQueryModelList.add(batchQueryModel);
		batchQueryModelList.add(batchQueryModel2);

		String s = JsonUtils.toJsonString(batchQueryModelList);
		RemittanceResultBatchPackModel batchPackModel = new RemittanceResultBatchPackModel();
		batchPackModel.setRequestNo(requestNo);
		batchPackModel.setFn(fn);
		batchPackModel.setRemittanceResultBatchQueryModels(s);


		List<RemittanceQueryResult> remittanceQueryResultList = iRemittanceQueryApiHandler.apiRemittanceResultBatchQuery(batchPackModel);
		Assert.assertFalse(CollectionUtils.isEmpty(remittanceQueryResultList));
	}




}
