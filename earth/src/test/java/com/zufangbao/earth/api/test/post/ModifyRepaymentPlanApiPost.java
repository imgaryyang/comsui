package com.zufangbao.earth.api.test.post;

import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.sun.yunxin.entity.model.api.modify.RepaymentPlanModifyRequestDataModel;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

public class ModifyRepaymentPlanApiPost extends BaseApiTestPost{
	
	class task implements Runnable {

		@Override
		public void run() {
//			File file = new File("/Users/louguanyang/Desktop/test_1000.txt");//测试用json文件地址
//			List<CommandVoucerParam> datas = JsonUtils.parseArray(file, CommandVoucerParam.class); 
//			int fromIndex = 600;
//			int toIndex = 800;
//			List<CommandVoucerParam> subList = datas.subList(fromIndex, toIndex);
//			subList.parallelStream().forEach(param -> {
				long start = System.currentTimeMillis();
				CommandVoucerParam param = new CommandVoucerParam("bd38adb2-76ab-47c7-89ab-db9a0cf606c91", "0");
				String uniqueId = param.unique_id;
				String amount = param.amount;
				List<RepaymentPlanModifyRequestDataModel> requestDataList = createRequestDataList(amount);
				postTest(uniqueId, requestDataList);
				long end = System.currentTimeMillis();
				System.out.println(Thread.currentThread().getName() + "----------> uniqueId:" + uniqueId + ", use" + (end - start) + "ms");
//			});
		}

		private List<RepaymentPlanModifyRequestDataModel> createRequestDataList(String amount) {
			List<RepaymentPlanModifyRequestDataModel> requestDataList = new ArrayList<>();
			RepaymentPlanModifyRequestDataModel model = new RepaymentPlanModifyRequestDataModel();
			model.setAssetInterest(new BigDecimal("20"));
			model.setAssetPrincipal(new BigDecimal("10000"));
			model.setMaintenanceCharge(new BigDecimal("20"));
			model.setServiceCharge(new BigDecimal("20"));
			model.setOtherCharge(new BigDecimal("20"));
//			model.setRepayScheduleNo("1111224231");
			model.setAssetType(0);
			model.setAssetRecycleDate("2018-04-20");

			requestDataList.add(model);

			RepaymentPlanModifyRequestDataModel model2 = new RepaymentPlanModifyRequestDataModel();

			model2.setAssetInterest(new BigDecimal("100"));
			model2.setAssetPrincipal(new BigDecimal("20000"));
			model2.setMaintenanceCharge(new BigDecimal("100"));
			model2.setServiceCharge(new BigDecimal("101"));
			model2.setOtherCharge(new BigDecimal("100"));
//			model2.setRepayScheduleNo("1232322");
			model2.setAssetType(0);
			model2.setAssetRecycleDate("2018-04-22");
//
			requestDataList.add(model2);

//			RepaymentPlanModifyRequestDataModel model3 = new RepaymentPlanModifyRequestDataModel();
//			model3.setAssetInterest(new BigDecimal("100"));
//			model3.setAssetPrincipal(new BigDecimal("150000"));
//			model3.setMaintenanceCharge(new BigDecimal("100"));
//			model3.setServiceCharge(new BigDecimal("100"));
//			model3.setOtherCharge(new BigDecimal("100"));
//			model3.setAssetType(1);
//			model3.setAssetRecycleDate("2017-12-22");
//
//			requestDataList.add(model3);
			return requestDataList;
		}

		private void postTest(String uniqueId, List<RepaymentPlanModifyRequestDataModel> requestDataList) {
			Map<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("fn", "200001");
//			requestParams.put("financialProductCode","overWrite3");
			requestParams.put("uniqueId", uniqueId);
//			requestParams.put("contractNo","云信信2017-1496-DK(20171128000000006326)号");
			requestParams.put("requestReason", "1");
			requestParams.put("requestNo", UUID.randomUUID().toString());
			requestParams.put("requestData", JsonUtils.toJsonString(requestDataList));
			try {
				String sr = PostTestUtil.sendPost(MODIFY_REPAYMENT, requestParams, getIdentityInfoMap(requestParams));
//				String sr = PostTestUtil.sendPost("http://contra.5veda.net/api/v3/modifyRepaymentPlan", requestParams, getIdentityInfoMap(requestParams));
//				String sr = PostTestUtil.sendPost("http://yunxin.5veda.net/api/modify", requestParams, getIdentityInfoMap(requestParams));
//				String sr = PostTestUtil.sendPost("http://192.168.0.128/api/modify", requestParams, getIdentityInfoMap(requestParams));
				System.out.println(Thread.currentThread().getName() + "---------->" + sr );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testApiRepaymentPlan() throws InterruptedException {
		
		List<Thread> threadList = new ArrayList<Thread>();
		
		for (int i = 0; i < 1; i++) {
			Thread thread = new Thread(new task());
			threadList.add(thread);
		}
		threadList.parallelStream().forEach(t -> {
			try {
				t.start();
				t.join();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
}
