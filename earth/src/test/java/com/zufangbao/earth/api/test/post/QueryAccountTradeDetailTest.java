 package com.zufangbao.earth.api.test.post;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * Created by whb on 17-7-4.
 */

 /**
  * 请使用中的方法进行校验
  * com.zufangbao.earth.yunxin.web
  */
 @Deprecated
public class QueryAccountTradeDetailTest extends BaseApiTestPost{

    /**
     * 没有信托编号
     */
    @Test
    public void testDirectBankApiWrongParameterProductCode(){
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("productCode", "1111");
        requestParams.put("capitalAccountNo", "31050174360000000874");
        requestParams.put("paymentInstitutionName", "2");
        requestParams.put("startTime", "2017-07-10 08:00:00");
        requestParams.put("endTime", "2017-07-11 20:00:00");
        requestParams.put("page", "1");
        JSONObject jsonObject = mockRequest(requestParams);
        assertEquals("30003", jsonObject.getString("code"));
    }

    /**
     * 没有账户号
     */
    @Test
    public void testDirectBankApiWrongParameterCapitalAccountNo(){
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("productCode", "G32000");
        requestParams.put("capitalAccountNo", "");
        requestParams.put("paymentInstitutionName", "2");
        requestParams.put("startTime", "2017-07-10 08:00:00");
        requestParams.put("endTime", "2017-07-11 20:00:00");
        requestParams.put("page", "1");
        JSONObject jsonObject = mockRequest(requestParams);
        assertEquals("38005", jsonObject.getString("code"));
    }

    /**
     * 支付网管非法
     */
    @Test
    public void testDirectBankApiWrongParameterPayment(){
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("productCode", "G32000");
        requestParams.put("capitalAccountNo", "31050174360000000874");
        requestParams.put("paymentInstitutionName", "-1");
        requestParams.put("startTime", "2017-07-10 08:00:00");
        requestParams.put("endTime", "2017-07-11 20:00:00");
        requestParams.put("page", "1");
        JSONObject jsonObject = mockRequest(requestParams);
        assertEquals("38006",jsonObject.getString("code") );
    }

    /**
     * 开始时间为空
     */
    @Test
    public void testDirectBankApiWrongParameterStartTime(){
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("productCode", "G32000");
        requestParams.put("capitalAccountNo", "31050174360000000874");
        requestParams.put("paymentInstitutionName", "2");
        requestParams.put("startTime", "");
        requestParams.put("endTime", "2017-07-11 20:00:00");
        requestParams.put("page", "1");
        JSONObject jsonObject = mockRequest(requestParams);
        assertEquals("38007", jsonObject.getString("code"));
    }

    /**
     * 结束时间为空
     */
    @Test
    public void testDirectBankApiWrongParameterEndTime(){
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("productCode", "G32000");
        requestParams.put("capitalAccountNo", "31050174360000000874");
        requestParams.put("paymentInstitutionName", "2");
        requestParams.put("startTime", "2017-07-10 08:00:00");
        requestParams.put("endTime", "");
        requestParams.put("page", "1");
        JSONObject jsonObject = mockRequest(requestParams);
        assertEquals("38008", jsonObject.getString("code"));
    }

    /**
     * 开始时间大于结束时间
     */
    @Test
    public void testDirectBankApiWrongParameterStartAndEndTime(){
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("productCode", "G32000");
        requestParams.put("capitalAccountNo", "31050174360000000874");
        requestParams.put("paymentInstitutionName", "2");
        requestParams.put("startTime", "2017-07-10 08:00:00");
        requestParams.put("endTime", "2017-07-09 08:00:00");
        requestParams.put("page", "1");
        JSONObject jsonObject = mockRequest(requestParams);
        assertEquals("38002", jsonObject.getString("code"));
    }

    /**
     *页数参数为空
     */
    @Test
    public void testDirectBankApiWrongParameterPage(){
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("productCode", "G32000");
        requestParams.put("capitalAccountNo", "31050174360000000874");
        requestParams.put("paymentInstitutionName", "2");
        requestParams.put("startTime", "2017-07-10 08:00:00");
        requestParams.put("endTime", "2017-07-11 20:00:00");
        requestParams.put("page", "");
        JSONObject jsonObject = mockRequest(requestParams);
        assertEquals("38009", jsonObject.getString("code"));
    }

    /**
     * 页数参数为非法值
     */
    @Test
    public void testDirectBankApiWrongParameterPageMinus1(){
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("productCode", "G32000");
        requestParams.put("capitalAccountNo", "31050174360000000874");
        requestParams.put("paymentInstitutionName", "2");
        requestParams.put("startTime", "2017-07-10 08:00:00");
        requestParams.put("endTime", "2017-07-11 20:00:00");
        requestParams.put("page", "-1");
        JSONObject jsonObject = mockRequest(requestParams);
        assertEquals("38009", jsonObject.getString("code"));
    }

    /**
     * 银行直接连接第0页
     */
    @Test
    public void testDirectBankApiWrongParameterPage0(){
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("productCode", "G32000");
        requestParams.put("capitalAccountNo", "31050174360000000874");
        requestParams.put("paymentInstitutionName", "2");
        requestParams.put("startTime", "2017-07-10 08:00:00");
        requestParams.put("endTime", "2017-07-11 20:00:00");
        requestParams.put("page", "0");
        JSONObject jsonObject = mockRequest(requestParams);
        assertEquals("38009", jsonObject.getString("code"));
    }
    /**
     * 银行直接连接第1页
     */
    @Test
    public void testDirectBankApiWrongParameterPage1(){
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("productCode", "G32000");
        requestParams.put("capitalAccountNo", "31050174360000000874");
        requestParams.put("paymentInstitutionName", "2");
        requestParams.put("startTime", "2017-07-09 08:00:00");
        requestParams.put("endTime", "2017-07-12 20:00:00");
        requestParams.put("page", "1");
        JSONObject jsonObject = mockRequest(requestParams);
        assertEquals( "0", jsonObject.getString("code"));
        assertEquals(true, jsonObject.getJSONObject("data").getJSONObject("accountTradeDetail").getBoolean("hasNextPage"));
    }
    /**
     * 银行直接连接第11页
     */
    @Test
    public void testDirectBankApiWrongParameterPage11(){
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("productCode", "G32000");
        requestParams.put("capitalAccountNo", "31050174360000000874");
        requestParams.put("paymentInstitutionName", "2");
        requestParams.put("startTime", "2017-07-09 08:00:00");
        requestParams.put("endTime", "2017-07-12 20:00:00");
        requestParams.put("page", "11");
        JSONObject jsonObject = mockRequest(requestParams);
        assertEquals("0", jsonObject.getString("code"));
        assertEquals(false, jsonObject.getJSONObject("data").getJSONObject("accountTradeDetail").getBoolean("hasNextPage"));
    }
    /**
     * 银行直接连接第12页
     */
    @Test
    public void testDirectBankApiWrongParameterPage12(){
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("productCode", "G32000");
        requestParams.put("capitalAccountNo", "31050174360000000874");
        requestParams.put("paymentInstitutionName", "2");
        requestParams.put("startTime", "2017-07-09 08:00:00");
        requestParams.put("endTime", "2017-07-12 20:00:00");
        requestParams.put("page", "12");
        JSONObject jsonObject = mockRequest(requestParams);
        assertEquals("0", jsonObject.getString("code"));
        assertEquals(false, jsonObject.getJSONObject("data").getJSONObject("accountTradeDetail").getBoolean("hasNextPage"));
    }

    /**
     * 第三方连接第0页
     */
    @Test
    public void testThirdPartyApiWrongParameterPage0(){
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("productCode", "G32000");
        requestParams.put("capitalAccountNo", "31050174360000000874");
        requestParams.put("paymentInstitutionName", "3");
        requestParams.put("startTime", "2017-07-10 08:00:00");
        requestParams.put("endTime", "2017-07-11 20:00:00");
        requestParams.put("page", "0");
        JSONObject jsonObject = mockRequest(requestParams);
        assertEquals("38009", jsonObject.getString("code"));
    }
    /**
     * 第三方连接第1页
     */
    @Test
    public void testThirdPartyApiWrongParameterPage1(){
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("productCode", "G32000");
        requestParams.put("capitalAccountNo", "31050174360000000874");
        requestParams.put("paymentInstitutionName", "3");
        requestParams.put("startTime", "2017-07-10 08:00:00");
        requestParams.put("endTime", "2017-07-11 20:00:00");
        requestParams.put("page", "1");
        JSONObject jsonObject = mockRequest(requestParams);
        assertEquals("0", jsonObject.getString("code"));
        assertEquals(true, jsonObject.getJSONObject("data").getJSONObject("accountTradeDetail").getBoolean("hasNextPage"));
    }
    /**
     * 第三方连接第11页
     */
    @Test
    public void testThirdPartyApiWrongParameterPage11(){
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("productCode", "G32000");
        requestParams.put("capitalAccountNo", "31050174360000000874");
        requestParams.put("paymentInstitutionName", "3");
        requestParams.put("startTime", "2017-07-10 08:00:00");
        requestParams.put("endTime", "2017-07-11 20:00:00");
        requestParams.put("page", "11");
        JSONObject jsonObject = mockRequest(requestParams);
        assertEquals("0", jsonObject.getString("code"));
        assertEquals(false, jsonObject.getJSONObject("data").getJSONObject("accountTradeDetail").getBoolean("hasNextPage"));
    }
    /**
     * 第三方连接第12页
     */
    @Test
    public void testThirdPartyApiWrongParameterPage12(){
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("productCode", "G32000");
        requestParams.put("capitalAccountNo", "31050174360000000874");
        requestParams.put("paymentInstitutionName", "3");
        requestParams.put("startTime", "2017-07-10 08:00:00");
        requestParams.put("endTime", "2017-07-11 20:00:00");
        requestParams.put("page", "12");
        JSONObject jsonObject = mockRequest(requestParams);
        assertEquals("0", jsonObject.getString("code"));
        assertEquals(false, jsonObject.getJSONObject("data").getJSONObject("accountTradeDetail").getBoolean("hasNextPage"));
    }

    private JSONObject mockRequest(Map requestParams){
        requestParams.put("fn", "100012");
        requestParams.put("requestNo", UUID.randomUUID().toString());
        try{
            String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
            JSONObject json = JSONObject.parseObject(sr);
            System.out.println(sr);
            return json;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
