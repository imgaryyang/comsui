package com.suidifu.bridgewater.controller;

import com.demo2do.core.entity.Result;
import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.api.model.RemittanceResultBatchPackModel;
import com.suidifu.bridgewater.api.model.RemittanceResultBatchQueryModel;
import com.suidifu.bridgewater.controller.api.remittance.QueryApiRemittanceController;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplication;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.VALID_REMITTANCD_NOT_CORRECT_PARAMS;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/context/applicationContext-*.xml",
		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })

@Transactional()
@TransactionConfiguration(defaultRollback = true)
public class QueryApiRemittanceControllerTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	private QueryApiRemittanceController queryApiRemittanceController;

//	@Autowired
//	private CombineNoticeTask combineNoticeTask;

	@Autowired
	private GenericDaoSupport genericDaoSupport;
	@Autowired
	private IRemittanceApplicationService remittanceApplicationService;
	
	@Test
	@Sql("classpath:test/remittance/test_getRemittanceResultBy.sql")
	public void test_getRemittanceResult() {
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
		String requestJson = JsonUtils.toJsonString(batchPackModel);
		System.out.println(requestJson);
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		String message = queryApiRemittanceController.batchQueryRemittanceResult(batchPackModel, response ,request);
		System.out.println(message);
		Result result = JsonUtils.parse(message, Result.class);
		Assert.assertEquals("成功!", result.getMessage());
	}

	@Test
	public void testRemittance() {

//		CombineNoticeTask task = SpringContextUtil.getBean("combineNoticeTask");

//		combineNoticeTask.execPushApplicationsToOutlier();

		try {

//		String hql = "INSERT INTO t_remittance_application ( remittance_application_uuid, request_no, financial_contract_uuid, financial_contract_id,
// financial_product_code, contract_unique_id, contract_no, planned_total_amount, actual_total_amount, auditor_name, audit_time, notify_url, plan_notify_number,
// actual_notify_number, remittance_strategy, remark, transaction_recipient, execution_status, execution_remark, create_time, creator_name, ip, last_modified_time,
// opposite_receive_date, remittance_id, total_count, actual_count, version_lock, check_request_no, check_status, check_retry_number, check_send_time, int_field_1,
// int_field_2, int_field_3, string_field_1, string_field_2, string_field_3, decimal_field_1, decimal_field_2, decimal_field_3) VALUES
// ('ad52a5f8-270d-4fdf-b62b-0116358b3a8e', '8b3b3abf-c4e1-4038-87fc-c89262era359', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '38', 'G31700',
// 'f16b31fa-5e1c-4079-8c2d-b9f9042661cf', '妹妹你大胆的往前走150', '3000.00', '3000.00', 'auditorName1', '2017-10-15 00:00:00', 'http://www.mocky
// .io/v2/5185415ba171ea3a00704eed', '3', '0', '0', '交易备注', '1', '2', NULL, '2017-10-11 15:54:06', 't_test_zfb', '192.168.0.158', '2017-10-11 15:56:43',
// '2017-10-11 23:54:31', 'wsha9d616a4-fd0d-44f7-a5eb-1e3f29b19e1b', '2', '2', '9c11d931-e316-4941-8ea3-5c5da2dd4762', 'f5e19ed7-2e8b-4060-886a-df36e79117e7', '1',
// '3', '2017-10-11 15:54:06', '0', '0', '0', NULL, NULL, NULL, NULL, NULL, NULL)";

			RemittanceApplication remittanceApplication = new RemittanceApplication();

			remittanceApplication.setRequestNo("8b3b3abf-c4e1-4038-87fc-c892620ba359");

			remittanceApplication.setRemittanceApplicationUuid("ad52a5f8-270d-4fdf-b62b-0116358b3a8h");

			remittanceApplication.setVersionLock("23");

//			genericDaoSupport.executeHQL(hql, new HashedMap());

			genericDaoSupport.save(remittanceApplication);

		} catch (Exception e) {
			e.printStackTrace();

			System.out.println("message:" + "[" + e.getCause() + "]");

			String s = VALID_REMITTANCD_NOT_CORRECT_PARAMS.keySet().stream().filter(param -> e.getCause().toString().contains(param)).findFirst().get();


			String message = VALID_REMITTANCD_NOT_CORRECT_PARAMS.getOrDefault(VALID_REMITTANCD_NOT_CORRECT_PARAMS.keySet().stream().filter(param -> e.getCause()
					.toString().contains(param)).findFirst().get(), "system error");

			boolean b = e.getCause().toString().contains("unique_key_request_no");
			System.out.println(b);

			System.out.println("message =" + message);
		}
	}
	
}
