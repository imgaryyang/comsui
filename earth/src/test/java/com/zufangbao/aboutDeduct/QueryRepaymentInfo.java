package com.zufangbao.aboutDeduct;

import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.earth.api.test.post.PostTestUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.demo2do.core.entity.Result;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import static com.zufangbao.aboutDeduct.BaseApiTestPost.QUERY_URL_TEST;

/**
 * Created by wubo on 2018/4/9.
 */
public class QueryRepaymentInfo {

    /**
     * 查询该合同下第i期还款计划
     * @param uniqueId
     * @return
     */
    public String query_i_RepaymentPlan(String uniqueId,int i){
        String repaymentNumber = "";
        Map<String, String> requestParams1 = new HashMap<String, String>();
        requestParams1.put("fn", "100001");
        requestParams1.put("contractNo", "");
        requestParams1.put("requestNo", UUID.randomUUID().toString());
        requestParams1.put("uniqueId", uniqueId);
        try {
            String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams1, new BaseApiTestPost().getIdentityInfoMap(requestParams1));
            System.out.println(sr);
            Result result = JsonUtils.parse(sr, Result.class);
            Result responsePacket =  JsonUtils.parse((String) result.get("responsePacket"),Result.class);
            JSONArray jsonArray = (JSONArray) responsePacket.get("repaymentPlanDetails");
            JSONObject jo = jsonArray.getJSONObject(i);
            repaymentNumber = (String) jo.get("repaymentNumber");
            System.out.println("---------"+sr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return repaymentNumber;
    }
}
