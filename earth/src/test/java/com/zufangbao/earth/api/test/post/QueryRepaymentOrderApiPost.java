package com.zufangbao.earth.api.test.post;

import com.demo2do.core.utils.JsonUtils;
import org.junit.Test;

import java.util.*;

/**
 * Created by FanT on 2017/8/29.
 */
public class QueryRepaymentOrderApiPost extends BaseApiTestPost{
    @Test
    public void queryOrder() {
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("requestNo", UUID.randomUUID().toString());
        requestParams.put("financialProductCode","I02900");
        List<String> orderUniqueIdList = new ArrayList<>();
        orderUniqueIdList.add("PR2018012615374012927");
//        orderUniqueIdList.add("pufa0000000000000000000000041970");
//        orderUniqueIdList.add("pufa0000000000000000000000041969");

//        orderUniqueIdList.add("fanteng417");
        requestParams.put("orderUniqueIds", JsonUtils.toJsonString(orderUniqueIdList));
//        List<String> orderUuIdList = new ArrayList<>();
//        orderUuIdList.add("64dce854-8814-4434-87ee-0472a3259314");
//        orderUuIdList.add("6f4bc3ee-c8cb-4a06-82b8-6e192c4f5436");
//        requestParams.put("orderUuids", JsonUtils.toJsonString(orderUuIdList));



        try {
            String sr = PostTestUtil.sendPost("http://yunxin.5veda.net/api/v2/query-repaymentOrder", requestParams, getIdentityInfoMap(requestParams));//192.168.0.204
            System.out.println(sr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
