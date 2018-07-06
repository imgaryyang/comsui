package com.zufangbao.cucumber.method;

import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.earth.api.test.post.BaseApiTestPost;
import com.zufangbao.earth.api.test.post.PostTestUtil;
import com.zufangbao.sun.yunxin.entity.model.api.modify.RepaymentPlanModifyRequestDataModel;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

public class ModifyRepayment extends BaseApiTestPost{
	


		@Test
		public void modify() {
				String uniqueId = "ad51e819-4638-403c-9c5b-8f2632739052";
				String amount = "1000";
			List<RepaymentPlanModifyRequestDataModel> requestDataList = new ArrayList<>();
			RepaymentPlanModifyRequestDataModel model = new RepaymentPlanModifyRequestDataModel();
			model.setAssetInterest(new BigDecimal("0"));
			model.setAssetPrincipal(new BigDecimal("500"));
			model.setMaintenanceCharge(new BigDecimal("0"));
			model.setServiceCharge(new BigDecimal("0"));
			model.setOtherCharge(new BigDecimal("0"));
			model.setAssetType(0);
			model.setAssetRecycleDate("2017-03-21");
			requestDataList.add(model);

			RepaymentPlanModifyRequestDataModel model2 = new RepaymentPlanModifyRequestDataModel();
			model2.setAssetInterest(new BigDecimal("0"));
			model2.setAssetPrincipal(new BigDecimal("1000"));
			model2.setMaintenanceCharge(new BigDecimal("0"));
			model2.setServiceCharge(new BigDecimal("0"));
			model2.setOtherCharge(new BigDecimal("0"));
			model2.setAssetType(0);
			model2.setAssetRecycleDate("2017-04-26");
			requestDataList.add(model2);

//            RepaymentPlanModifyRequestDataModel model3 = new RepaymentPlanModifyRequestDataModel();
//            model3.setAssetInterest(new BigDecimal("0"));
//            model3.setAssetPrincipal(new BigDecimal("500"));
//            model3.setMaintenanceCharge(new BigDecimal("0"));
//            model3.setServiceCharge(new BigDecimal("0"));
//            model3.setOtherCharge(new BigDecimal("0"));
//            model3.setAssetType(0);
//            model3.setAssetRecycleDate("2017-03-23");
//            requestDataList.add(model3);

			Map<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("fn", "200001");
			requestParams.put("uniqueId", uniqueId);
			requestParams.put("requestReason", "0");
			requestParams.put("requestNo", UUID.randomUUID().toString());
			requestParams.put("requestData", JsonUtils.toJsonString(requestDataList));
			try {
//				String sr = PostTestUtil.sendPost("http://yunxin.zufangbao.cn/api/modify", requestParams, getIdentityInfoMap(requestParams));
				String sr = PostTestUtil.sendPost("http://yunxin.5veda.net/api/modify", requestParams, getIdentityInfoMap(requestParams));
//				String sr = PostTestUtil.sendPost("http://127.0.0.1:9090/api/modify", requestParams, getIdentityInfoMap(requestParams));
//				String sr = PostTestUtil.sendPost("http://192.168.0.204/api/modify", requestParams, getIdentityInfoMap(requestParams));
				System.out.println(sr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


	}
	

	

