package com.zufangbao.earth.api.test.post;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class QueryAmountApiPost extends BaseApiTestPost {
   @Test
    public void queryAmount() {
       Map<String,String> requestParams = new HashMap<>();
       requestParams.put("fn","100010");
       requestParams.put("requestNo", UUID.randomUUID().toString());
       requestParams.put("productCode","G31700");
       requestParams.put("accountNo","600000000001");
       try {
           String sr = PostTestUtil.sendPost("http://yunxin.5veda.net/api/query", requestParams, getIdentityInfoMap(requestParams));//192.168.0.204
           System.out.println(sr);
       } catch (Exception e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
   }
}
