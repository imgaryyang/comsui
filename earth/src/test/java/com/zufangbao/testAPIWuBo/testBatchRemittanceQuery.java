package com.zufangbao.testAPIWuBo;

import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.cucumber.method.PrepaymentCucumberMethod;
import com.zufangbao.earth.api.test.post.BaseApiTestPost;
import com.zufangbao.earth.api.test.post.PostTestUtil;
import com.zufangbao.testAPIWuBo.testAPI.models.RemittanceResultBatchQueryModels;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;


/**
 * Created by juxer on 17-4-17.
 */
@ContextConfiguration(locations = {
        "classpath:/context/applicationContext-*.xml",
        "classpath:/local/applicationContext-*.xml" })
public class testBatchRemittanceQuery extends BaseApiTestPost {
    PrepaymentCucumberMethod prepaymentCucumberMethod = new PrepaymentCucumberMethod();
    String productCode = "G31700";
    String uniqueId1 = "wbfc4cad9e-dfc2-";
    String uniqueId2 = "wb0001";
    String amount = "1800";
    String requestNo1 = UUID.randomUUID().toString();
    String requestNo2 = UUID.randomUUID().toString();
    @Test
    public void makeLoan(){
//        prepaymentCucumberMethod.makeLoan1(productCode,uniqueId1,amount,requestNo1);
//        prepaymentCucumberMethod.makeLoan1(productCode,uniqueId2,amount,requestNo2);
        try {
            Thread.sleep(90000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Test
    public void testBatchRemittanceQuery(){
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("fn","100011");
        requestParams.put("requestNo", UUID.randomUUID().toString());
//        requestParams.put("productCode","W123");
        List<RemittanceResultBatchQueryModels> list = new ArrayList<>();
        RemittanceResultBatchQueryModels remittanceResultBatchQueryModels1 = new RemittanceResultBatchQueryModels();
        remittanceResultBatchQueryModels1.setOriRequestNo("wwwbbb123456700000030");//原放款请求号和商户订单号 二选一 都是非必填 填哪个用哪个查 都填时优先使用商户订单号查询
        remittanceResultBatchQueryModels1.setUniqueId("da01b522-5206-4537-a49c-76400ae7e879");
//        remittanceResultBatchQueryModels1.setRemittanceId("wwwbbb123456700000030");//商户订单号 在计划订单里面能看见
        list.add(remittanceResultBatchQueryModels1);
        String repams = JsonUtils.toJsonString(list);

//        requestParams.put("remittanceResultBatchQueryModels","[{'oriRequestNo':"+"2ad5b73b-f971-4f"+",'uniqueId':"+uniqueId1+"},{'oriRequestNo':"+requestNo2+",'uniqueId':"+uniqueId2+"}]");
        requestParams.put("remittanceResultBatchQueryModels",repams);
        try {
            String sr = PostTestUtil.sendPost("http://192.168.0.128:18084/api/query", requestParams, getIdentityInfoMap(requestParams));
//            String sr = PostTestUtil.sendPost("http://remittance.5veda.net/api/query", requestParams, getIdentityInfoMap(requestParams));
            System.out.println(sr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
