package com.suidifu.matryoshaka.test.scripts;


import com.suidifu.matryoshka.customize.CustomizeServices;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.StringUtils;
import org.apache.commons.logging.Log;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by hwr on 17-11-3.
 */
public class WFCheckPenaltyServices implements CustomizeServices{
    @Override
    public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest, Map<String, String> postRequest, Log logger) {
        logger.info("#WFCheckPenaltyServices start to get param");
        logger.info("#WFCheckPenaltyServices param : " + preRequest);
        String jsonR = (String) preRequest.getOrDefault("r", "");
        if(StringUtils.isEmpty(jsonR)){
            logger.error("#WFCheckPenaltyServices r 利息 is empty");
            return false;
        }
        BigDecimal r =  new BigDecimal(jsonR);
        logger.info("r : "+ r);

        String jsonPenalty = (String) preRequest.getOrDefault("penalty", "");
        String jsonPrincipal = (String) preRequest.getOrDefault("principal", "");

        logger.info("#WFCheckPenaltyServices jsonInterest : " + jsonPenalty);
        logger.info("#WFCheckPenaltyServices jsonPrincipal : " + jsonPrincipal);

        List<BigDecimal> penaltys = JsonUtils.parseArray(jsonPenalty, BigDecimal.class);
        List<BigDecimal> principals = JsonUtils.parseArray(jsonPrincipal, BigDecimal.class);
        if(penaltys == null){
            logger.info("#WFCheckPenaltyServices penaltys is null");
            return false;
        }
        logger.info("#WFCheckPenaltyServices penaltys : " + penaltys);
        if(principals == null){
            logger.info("#WFCheckPenaltyServices principals is null");
            return false;
        }
        logger.info("#WFCheckPenaltyServices principals : " + principals);

        String jsonOverdueDay = (String) preRequest.getOrDefault("overdueDay", "");
        if(StringUtils.isEmpty(jsonOverdueDay)){
            logger.error("#WFCheckPenaltyServices overdueDay is empty");
            return false;
        }
        int overdueDay = Integer.parseInt(jsonOverdueDay);
        logger.info("#WFCheckPenaltyServices overdueDay : " + overdueDay);

        for(int i = 0;i < penaltys.size();i ++){
            logger.info("#WFCheckPenaltyServices old penalty[{" + i + "}] : "+ ((BigDecimal) penaltys.get(i)));
            logger.info("#WFCheckPenaltyServices old principal[{" + i + "}] : "+ ((BigDecimal) principals.get(i)));

            BigDecimal penalty = ((BigDecimal) penaltys.get(i)).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal principal = ((BigDecimal) principals.get(i)).multiply(new BigDecimal(0.24*overdueDay/360).multiply(r.add(new BigDecimal(1)))).setScale(2, BigDecimal.ROUND_HALF_UP);

            logger.info("#WFCheckPenaltyServices after calculating, the penalty[{" + i + "}] : "+ penaltys);
            logger.info("#WFCheckPenaltyServices after calculating, the principal[{" + i + "}] : "+ principal);
            if(penalty.compareTo(principal) <= 0 ? false :true){
                logger.info("#WFCheckPenaltyServices 逾期罚息<=计划还款本金 * 24% * （1+r ） * 逾期天数/360 ");
                return false;
            }
        }
        return true;
    }
}
