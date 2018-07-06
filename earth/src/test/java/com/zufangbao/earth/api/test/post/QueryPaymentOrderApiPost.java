package com.zufangbao.earth.api.test.post;

import com.demo2do.core.utils.JsonUtils;
import org.junit.Test;

import java.util.*;

/**
 * Created by FanT on 2017/8/16.
 */
public class QueryPaymentOrderApiPost extends BaseApiTestPost {
    @Test
    public void queryOrder() {
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("requestNo", UUID.randomUUID().toString());
        requestParams.put("financialContractNo","CS0001");
//        List<String> orderUniqueIdList = new ArrayList<>();
//        orderUniqueIdList.add("fanteng705");
//        JSONArray orderUniqueIdLists = JSONArray.fromObject(orderUniqueIdList);

//        requestParams.put("orderUniqueIdList", JsonUtils.toJsonString(orderUniqueIdList));
//        List<String> paymentNoList = new ArrayList<>();
//        paymentNoList.add("G20171025000031");
//        requestParams.put("paymentNoList", JsonUtils.toJsonString(paymentNoList));
        List<String> paymentUuidList = new ArrayList<>();
        paymentUuidList.add("e1d53552-f975-49ea-be6d-fd0c20984b7d");
        requestParams.put("paymentUuidList", JsonUtils.toJsonString(paymentUuidList));
        requestParams.put("orderUuidList","123213");



        try {
            String sr = PostTestUtil.sendPost("http://192.168.0.128/api/v3/query-paymentOrder", requestParams, getIdentityInfoMap(requestParams));//192.168.0.204
            System.out.println(sr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
