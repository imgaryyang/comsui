package com.suidifu.bridgewater.controller.api.remittance;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.suidifu.bridgewater.api.model.ModifyRemittanceApplicationModel;
import com.suidifu.bridgewater.controller.BaseApiController;
import com.suidifu.bridgewater.handler.IRemittanceApplicationHandler;
import com.suidifu.bridgewater.handler.RemittanceNotifyJobSender;
import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.logspec.GloableLogSpec.bridgewater_remittance_function_point;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.api.model.remittance.RemittancePlanInfo;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplication;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlan;
import com.zufangbao.sun.yunxin.log.ModifyRemittanceApplicationLog;
import com.zufangbao.sun.yunxin.service.ModifyRemittanceApplicationLogService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationDetailService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittancePlanService;

@Controller
@RequestMapping("/api/v2/modify-remittanceApplication")
public class ModifyRemittanceApplicationController extends BaseApiController {

    @Autowired
    private IRemittanceApplicationHandler iRemittanceApplicationHandler;

    @Autowired
    private ModifyRemittanceApplicationLogService modifyRemittanceApplicationLogService;

	@Autowired
	private RemittanceNotifyJobSender remittanceNotifyJobSender;
	
	@Autowired
	IRemittancePlanService iRemittancePlanService;
	
	@Autowired
	IRemittanceApplicationDetailService iRemittanceApplicationDetailService;
	
	@Autowired
	IRemittanceApplicationService iRemittanceApplicationService;
	
    private static final Log logger = LogFactory.getLog(ModifyRemittanceApplicationController.class);


    @RequestMapping(value = "", method = RequestMethod.POST)
    public @ResponseBody String modifyRemittanceApplication(HttpServletRequest request, HttpServletResponse response,
        @ModelAttribute ModifyRemittanceApplicationModel model){

        String oppositeKeyWord="[requestNo="+model.getRequestNo()+"];";
        logger.info(GloableLogSpec.AuditLogHeaderSpec() +bridgewater_remittance_function_point.RECV_TRADE_REQUEST_FROM_OUTLIER_SYSTEM +oppositeKeyWord+GloableLogSpec.RawData(JSON.toJSONString(model)));
        String ip = IpUtil.getIpAddress(request);
        String merchantId = getMerchantId(request);
        try {
            if(!model.isValid()) {
                return signErrorResult(response, ApiResponseCode.INVALID_PARAMS, model.getErrorMsg());
            }
            List<TradeSchedule> tradeSchedules = iRemittanceApplicationHandler.revokeFailRemittancePlanThenSaveInfo(model,ip, merchantId);
            
            //TODO 限额冻结
            
			pushJob(model.getRemittanceApplicationUuid(),tradeSchedules);

            logger.info(GloableLogSpec.AuditLogHeaderSpec() +bridgewater_remittance_function_point.RECV_TRADE_REQUEST_FROM_OUTLIER_SYSTEM + oppositeKeyWord + "[SUCC]" );
            return signSucResult(response);
        }catch (ApiException apie){
            apie.printStackTrace();
            model.setErrorMsg(""+apie.getCode());
            logger.error(GloableLogSpec.AuditLogHeaderSpec() +bridgewater_remittance_function_point.RECV_TRADE_REQUEST_FROM_OUTLIER_SYSTEM +oppositeKeyWord  +"[ERR:" + apie.getMessage()+"]");
            return signErrorResult(response, apie);
        }catch (Exception e){
            e.printStackTrace();
            model.setErrorMsg("Exception");
            logger.error(GloableLogSpec.AuditLogHeaderSpec() +bridgewater_remittance_function_point.RECV_TRADE_REQUEST_FROM_OUTLIER_SYSTEM +oppositeKeyWord  +"[ERR:" + e.getMessage()+"]");
            return signErrorResult(response, e);
        }finally {
            ModifyRemittanceApplicationLog log = new ModifyRemittanceApplicationLog(model.getRequestNo(),model.getRemittanceId(),
                model.getTradeNo(),model.getFinancialProductCode(),model.getRemittanceTradeNo(), null,//model.getRemittanceDetails(),
                model.getApprover(),model.getApprovedTime(),model.getComment(),model.getErrorMsg(),ip,merchantId);
            modifyRemittanceApplicationLogService.save(log);
        }

    }
    
    private void pushJob(String remittanceApplicationUuid,List<TradeSchedule> tradeSchedules) {
		RemittanceApplication application = iRemittanceApplicationService.getRemittanceApplicationBy(remittanceApplicationUuid);

		List<RemittancePlanInfo> remittancePlanInfos = new ArrayList<>();  
    	for (TradeSchedule tradeSchedule : tradeSchedules) {
    		String remittancePlanUuid = tradeSchedule.getOutlierTransactionUuid();
    		RemittancePlan plan = iRemittancePlanService.getUniqueRemittancePlanByUuid(remittancePlanUuid);
    		RemittancePlanInfo remittancePlanInfo = new RemittancePlanInfo(plan.getRemittancePlanUuid(),plan.getPlannedTotalAmount(),null);
    		remittancePlanInfos.add(remittancePlanInfo);
		}
		
		remittanceNotifyJobSender.pushJobToCitigroupForSecondRemittanceQuotaValidation(remittanceApplicationUuid, application.getFinancialContractUuid(), remittancePlanInfos, tradeSchedules);
	}

}
