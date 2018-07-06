package com.zufangbao.earth.api.test.post;
/*
                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
         佛祖保佑       永无BUG
*/

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 查询凭证状态接口
 * @author louguanyang on 2017/3/13.
 */

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {
//
//        "classpath:/local/applicationContext-*.xml" })

public class QueryVoucherPostTest extends BaseApiTestPost {

    @Test
    public void testApiQueryVoucher() {
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("fn", "100008");
        requestParams.put("requestNo", UUID.randomUUID().toString());
//        requestParams.put("financialProductCode","G31700");
        requestParams.put("requestNo", UUID.randomUUID().toString());
//        requestParams.put("bankTransactionNo", "810a88a4-21a4-4fb9-99eb-4badbd18d01d");
        requestParams.put("batchRequestNo", "4a83db8d-aa7a-4c18-af99-dd0e7e8a7753");
        try {
            String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
            System.out.println(sr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
