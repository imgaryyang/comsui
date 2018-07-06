package com.suidifu.matryoshaka.test.scripts;

import com.suidifu.matryoshka.customize.CustomizeServices;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.snapshot.ContractSnapshot;
import com.suidifu.matryoshka.snapshot.SandboxDataSet;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.StringUtils;
import org.apache.commons.logging.Log;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by hwr on 17-11-3.
 */
public class WFCheckPrepaymentServices implements CustomizeServices {
    @Override
    public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest, Map<String, String> postRequest, Log logger) {
        logger.info("#WFCheckPrepaymentServices start to get param");
        logger.info("#WFCheckPrepaymentServices param : " + preRequest);
        String uniqueId = (String) preRequest.getOrDefault("uniqueId", "");
        String contractNo = (String) preRequest.getOrDefault("contractNo", "");
        logger.info("#WFCheckPrepaymentServices uniqueId,contractNo:" + uniqueId + "," + contractNo);
        if(StringUtils.isEmpty(uniqueId)&&StringUtils.isEmpty(contractNo)){
            logger.error("#WFCheckPrepaymentServices uniqueId,contractNo 至少一个不能为空");
            return false;
        }
        SandboxDataSet sandboxDataSet = sandboxDataSetHandler.get_sandbox_by_contract_uniqueId_contractNo(uniqueId, contractNo);
        if(sandboxDataSet == null){
            logger.error("#WFCheckPrepaymentServices sandboxDataSet is null");
            return false;
        }

        ContractSnapshot contractSnapshot = sandboxDataSet.getContractSnapshot();
        String jsonAssetRecycleDate = (String) preRequest.getOrDefault("assetRecycleDate", "");
        if(StringUtils.isEmpty(jsonAssetRecycleDate)){
            logger.error("#WFCheckPrepaymentServices jsonAssetRecycleDate is empty");
            return false;
        }
        logger.info("#WFCheckPrepaymentServices jsonAssetRecycleDate : " + jsonAssetRecycleDate);

        List<String> assetRecycleDates = JsonUtils.parseArray(jsonAssetRecycleDate, String.class);
        if(contractSnapshot.getBeginDate() == null){
            logger.error("#WFCheckPrepaymentServices BeginDate is empty");
            return false;
        }
        logger.info("#WFCheckPrepaymentServices BeginDate : "+ contractSnapshot.getBeginDate());

        String jsonInterest = (String) preRequest.getOrDefault("interest", "");
        String jsonPrincipal = (String) preRequest.getOrDefault("principal", "");

        logger.info("#WFCheckPrepaymentServices jsonInterest : " + jsonInterest);
        logger.info("#WFCheckPrepaymentServices jsonPrincipal : " + jsonPrincipal);

        List<BigDecimal> interests = JsonUtils.parseArray(jsonInterest, BigDecimal.class);
        List<BigDecimal> principals = JsonUtils.parseArray(jsonPrincipal, BigDecimal.class);
        if(interests == null){
            logger.info("#WFCheckPrepaymentServices interests is null");
            return false;
        }
        if(principals == null){
            logger.info("#WFCheckPrepaymentServices principals is null");
            return false;
        }

        for(int i = 0;i < interests.size();i ++){
            int days = DateUtils.compareTwoDatesOnDay(DateUtils.asDay((String)assetRecycleDates.get(i)), contractSnapshot.getBeginDate());
            logger.info("#WFCheckPrepaymentServices 应计利息天数 days[{" + i + "}] : " + days);

            logger.info("#WFCheckPrepaymentServices old interest[{" + i + "}] : "+ ((BigDecimal) interests.get(i)));
            logger.info("#WFCheckPrepaymentServices old principal[{" + i + "}] : "+ ((BigDecimal) principals.get(i)));

            BigDecimal interest = ((BigDecimal) interests.get(i)).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal principal = ((BigDecimal) principals.get(i)).multiply(new BigDecimal(0.24*days/360)).setScale(2, BigDecimal.ROUND_HALF_UP);

            logger.info("#WFCheckPrepaymentServices after calculating, the interest[{" + i + "}] : "+ interest);
            logger.info("#WFCheckPrepaymentServices after calculating, the principal[{" + i + "}] : "+ principal);
            if(interest.compareTo(principal) == 0 ? false :true){
                logger.info("#WFCheckPrepaymentServices 提前还款利息=提前本金 * 24%* 应计利息天数/360");
                return false;
            }
        }
        return true;
    }
}
