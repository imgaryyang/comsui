package com.zufangbao.earth.cucumber.LoanCallBack;


import com.zufangbao.earth.util.MockUtils;
import com.zufangbao.sun.utils.JsonUtils;
import org.apache.commons.collections.map.HashedMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chengll on 17-3-31.
 */
public class TestMockRemiitanceApplication {


    /**
     * 回调成功的json
     *{
     *	"Status": {
     *	"IsSuccess": true,
     *	"ResponseMessage": "本次回调共1条记录。本批次回调成功1条，回调失败0条。",
     *	"ResponseCode": "0000",
     *	"WarningMessage": null
     * },
     *	"FailedIds": []
     * }
     *
     */

    public static void main(String[] args) {
        MockUtils.init();
        MockUtils mockUtils = new MockUtils();
        String jsonResult = "";
        Map<String, Object> map = new HashedMap();

        Map<String, Object> statusMap = new HashedMap();
        statusMap.put("IsSuccess", false);
        statusMap.put("ResponseMessage", "返回消息。");
        statusMap.put("ResponseCode", "0020");
        statusMap.put("WarningMessage",null);

        List<String> failedIds = new ArrayList<>();

        map.put("Status", statusMap);
        map.put("FailedIds", failedIds);
        jsonResult = JsonUtils.toJSONString(map);
        mockUtils.mockPostRequest("/testUrl",jsonResult);
        try {
            Thread.sleep(100000000);
            MockUtils.end();
        } catch (InterruptedException e) {


        }


    }
}
