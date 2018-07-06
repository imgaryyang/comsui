package com.suidifu.bridgewater.handler.test;

import com.suidifu.matryoshka.prePosition.PrePositionHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

/**
 * Created by qinweichao on 2017/8/8.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/context/applicationContext-*.xml",
        "classpath:/local/applicationContext-*.xml" })
public class PrePositionHandlerTest {
    private static final Log LOGGER = LogFactory.getLog(PrePositionHandlerTest.class);

    @Autowired
    PrePositionHandler prePositionHandler;

    @Test
    public void test(){
        String preProcessUrl = "";
        HashMap<String, String> allParameters = getAllParameters();
        HashMap<String, String> delayPostRequestParams = new HashMap<>();

        prePositionHandler.prePositionDefaultTaskHandler(preProcessUrl, allParameters, delayPostRequestParams, LOGGER);
    }

    private HashMap<String, String> getAllParameters(){
        HashMap<String, String> allParameters = new HashMap<>();

        allParameters.put("fn", "");
        allParameters.put("requestNo", "");
        allParameters.put("deductId", "");
        allParameters.put("financialProductCode", "");
        allParameters.put("apiCalledTime", "");
        allParameters.put("uniqueId", "");
        allParameters.put("contractNo", "");
        allParameters.put("amount", "");
        allParameters.put("repaymentDetails", "");
        allParameters.put("payerName", "");
        allParameters.put("payAcNo", "");
        allParameters.put("idCardNum", "");
        allParameters.put("bankCode", "");
        allParameters.put("provinceCode", "");
        allParameters.put("cityCode", "");
        allParameters.put("mobile", "");
        allParameters.put("gateway", "");
        return  allParameters;
    }
}
