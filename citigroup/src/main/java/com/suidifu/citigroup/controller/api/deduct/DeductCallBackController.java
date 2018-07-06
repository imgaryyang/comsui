package com.suidifu.citigroup.controller.api.deduct;

import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.sun.api.model.deduct.RepaymentType;
import com.zufangbao.sun.api.model.modify.AssetSetModifyModel;
import com.zufangbao.sun.yunxin.entity.DeductionStatus;
import com.zufangbao.sun.yunxin.entity.model.SystemTraceLog;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qinweichao on 2017/9/5.
 */

@Controller
@RequestMapping("/deduct/callback")
//@Component("deductCallBackController")
public class DeductCallBackController {

    public static final String ASSET_SET_MODIFY_MODE = "assetsetModifyModel";

    private static final Log logger = LogFactory.getLog(DeductCallBackController.class);

    @Autowired
    private RepaymentPlanHandler RepaymentPlanHandler;

    @RequestMapping(value = "/mofifyAssetset", method = RequestMethod.POST)
    public @ResponseBody String mofifyAssetset(HttpServletResponse httpServletResponse, HttpServletRequest request){
        SystemTraceLog systemTraceLog = new SystemTraceLog();
        String deductApplicationUuid = null;
        Map<String,Object> data = new HashMap<>();
        try{
            String assetSetModifyModelString = request.getParameter(ASSET_SET_MODIFY_MODE);
            AssetSetModifyModel assetSetModifyModel = JsonUtils.parse(assetSetModifyModelString, AssetSetModifyModel.class);
            deductApplicationUuid = assetSetModifyModel.getDeductApplicationUuid();

            String eventKey = "deductApplicationUuid:" + deductApplicationUuid;
            systemTraceLog = new SystemTraceLog(ZhonghangResponseMapSpec.EVENT_NAME.CITIGROUP_RECV_ASSESET_FROM_DEDUCT, eventKey,
                    assetSetModifyModelString, null, SystemTraceLog.INFO, null, ZhonghangResponseMapSpec.SYSTEM_NAME.DEDUCT, ZhonghangResponseMapSpec.SYSTEM_NAME.CITIGROUP);
            logger.info(systemTraceLog);

            if (null == assetSetModifyModel || !assetSetModifyModel.isValid()){
                systemTraceLog.setEventName(ZhonghangResponseMapSpec.EVENT_NAME.CITIGROUP_RECV_TRADE_FROM_DEDUCT_ERROR);
                systemTraceLog.setErrorMsg("assetSetModifyModel is error");
                systemTraceLog.setEventLevel(SystemTraceLog.ERROR);
                logger.info(systemTraceLog);

                data.put("status", Boolean.FALSE);
                return JsonUtils.toJsonString(data);
            }

            List<String> repaymentCodes = assetSetModifyModel.getRepaymentPlanCodeListJsonString();
            DeductionStatus deductionStatus = assetSetModifyModel.parseDeductionStatus();
            RepaymentType repaymentType = assetSetModifyModel.parseRepaymentType();

            systemTraceLog.setEventName(ZhonghangResponseMapSpec.EVENT_NAME.CITIGROUP_RECV_ASSESET_FROM_DEDUCT_START);
            logger.info(systemTraceLog);

            RepaymentPlanHandler.updateAssetSetBySingleLoanContractNo(repaymentCodes, deductionStatus, repaymentType);

            if (DeductionStatus.FAIL == deductionStatus && StringUtils.isNotEmpty(assetSetModifyModel.getDeductApplicationUuid())){
                RepaymentPlanHandler.unlockRepaymentPlans(repaymentCodes, assetSetModifyModel.getDeductApplicationUuid());
            }

            data.put("status", Boolean.TRUE);
            systemTraceLog.setEventName(ZhonghangResponseMapSpec.EVENT_NAME.CITIGROUP_RECV_ASSESET_FROM_DEDUCT_END);
            logger.info(systemTraceLog);

        }catch (Exception e){
            e.printStackTrace();
            systemTraceLog.setEventName(ZhonghangResponseMapSpec.EVENT_NAME.CITIGROUP_RECV_TRADE_FROM_DEDUCT_ERROR);
            systemTraceLog.setEventLevel(SystemTraceLog.ERROR);
            systemTraceLog.setEventName("System error");
            logger.error(systemTraceLog);
            data.put("status", Boolean.FALSE);
        }
        return JsonUtils.toJsonString(data);
    }

}
