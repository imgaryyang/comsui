package com.suidifu.matryoshaka.test.scripts;

import com.suidifu.matryoshka.customize.CustomizeServices;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.zufangbao.sun.utils.JsonUtils;
import org.apache.commons.logging.Log;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by hwr on 17-11-3.
 */
public class WFCheckInterestServices implements CustomizeServices {
    @Override
    public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest, Map<String, String> postRequest, Log logger) {
        logger.info("#WFCheckInterestServices start to get param");
        logger.info("#WFCheckInterestServices param : " + preRequest);
        String jsonInterest = (String) preRequest.getOrDefault("interest", "");
        String jsonPrincipal = (String) preRequest.getOrDefault("principal", "");

        logger.info("#WFCheckInterestServices jsonInterest : " + jsonInterest);
        logger.info("#WFCheckInterestServices jsonPrincipal : " + jsonPrincipal);

        List<BigDecimal> interests = JsonUtils.parseArray(jsonInterest, BigDecimal.class);
        List<BigDecimal> principals = JsonUtils.parseArray(jsonPrincipal, BigDecimal.class);

        if(interests == null){
            logger.info("#WFCheckInterestServices interests is null");
            return false;
        }
        logger.info("#WFCheckInterestServices interests : " + interests);
        if(principals == null){
            logger.info("#WFCheckInterestServices principals is null");
            return false;
        }
        logger.info("#WFCheckInterestServices principals : " + principals);

        logger.info("#WFCheckInterestServices  start to check");
        for(int i = 0;i < interests.size();i ++){

            logger.info("#WFCheckInterestServices old interest[{" + i + "}] : "+ ((BigDecimal)interests.get(i)));
            logger.info("#WFCheckInterestServices old principal[{" + i + "}] : "+ ((BigDecimal)principals.get(i)));

            BigDecimal interest = ((BigDecimal)interests.get(i)).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal principal = ((BigDecimal)principals.get(i)).multiply(new BigDecimal(0.24)).setScale(2, BigDecimal.ROUND_HALF_UP);

            logger.info("#WFCheckInterestServices after calculating, the interest[{" + i + "}] : "+ interest);
            logger.info("#WFCheckInterestServices after calculating, the principal[{" + i + "}] : "+ principal);

            if(interest.compareTo(principal) <= 0 ? false :true){
                logger.info("#WFCheckInterestServices 计划还款利息 > 计划还款本金 * 24%");
                return false;
            }
        }
        return true;
    }
}
