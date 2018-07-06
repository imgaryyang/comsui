package com.suidifu.bridgewater.controller.api.deduct.single.v2;


import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.sun.yunxin.entity.model.SystemTraceLog;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suidifu.bridgewater.controller.BaseApiController;
import com.suidifu.bridgewater.deduct.notify.handler.batch.v2.DeductNotifyJobServer;
import com.suidifu.bridgewater.handler.DeductApplicationBusinessHandler;
import com.suidifu.matryoshka.prePosition.PrePositionHandler;
import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductRequestModel;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.logspec.GloableLogSpec.bridgewater_deduct_standard_function_point;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.util.IpUtil;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * Created by zsh2014 on 17-5-14.
 */
@Controller
@RequestMapping("/pre/api")
public class DeductController extends BaseApiController {
    private static final String DEDUCT = "DEDUCT";
    private static final String PRE_API = "/pre/api/";
    private static final String PRE_API_DEDUCT = "/pre/api/DEDUCT/";

    private static final Log LOGGER = LogFactory.getLog(DeductController.class);

    private static final Log st_logger = LogFactory.getLog("deduct_st_logger");

    @Autowired
    private DeductApplicationBusinessHandler deductApplicationBusinessHandler;

    @Autowired
    @Qualifier("prePositionHandler")
    private PrePositionHandler prePositionHandler;

	@Value("#{config['urlForCitigroupCallBack']}")
	private String urlForCitigroupCallBack;

	@Value("#{config['urlForJpmorganCallback']}")
	private String urlForJpmorganCallback;

    @Value("#{config['urlToCitigroup']}")
    private String urlToCitigroup;

	@Autowired
	private DeductNotifyJobServer deductSendMorganStanleyNotifyJobServer;

	@Value("#{config['notifyserver.groupCacheJobQueueMap_group1']}")
	private String groupNameForDeductBusinessAcceptance;

    @RequestMapping(value = "/" + DEDUCT + "/{channelCode}/{serviceCode}", method = RequestMethod.POST)
    @ApiOperation(value = "中航扣款前置接口", notes = "中航扣款交易前置接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "channelCode", value = "渠道代码", paramType = "path", dataType = "String",defaultValue = "zhonghang"),
            @ApiImplicitParam(name = "serviceCode", value = "服务代码", paramType = "path", dataType = "String",defaultValue = "ZH-SingleDeduct"),
            @ApiImplicitParam(name = "fn", value = "功能代码",paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "requestNo", value = "请求唯一标识",paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "deductId", value = "扣款唯一编号",paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "financialProductCode", value = "金融产品代码",paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "apiCalledTime", value = "接口调用时间",paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "uniqueId", value = "贷款合同唯一编号",paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "contractNo", value = "贷款合同编号",paramType = "query", dataType = "String"),
            //@ApiImplicitParam(name = "protocolNo", value = "协议号",paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "transType", value = "交易类型",paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "amount", value = "扣款金额",paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "repaymentType", value = "还款类型",paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "mobile", value = "手机号",paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "accountName", value = "账户名",paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "accountNo", value = "账户号",paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "payerName", value = "账户名",paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "payAcNo", value = "账户号",paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "idCardNum", value = "身份证号",paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "bankCode", value = "开户行编号",paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "provinceCode", value = "开户行所在省编号",paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cityCode", value = "开户行所在市编号",paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "notifyUrl", value = "扣款结果回调地址",paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "gateway", value = "网关",paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "repaymentDetails", value = "还款计划明细",paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "batchDeductId", value = "批次号",paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "batchDeductApplicationUuid", value = "批次记录uuid",paramType = "query", dataType = "String"),

    })
    public @ResponseBody String commandDeduct(HttpServletRequest request, HttpServletResponse response) {
        SystemTraceLog systemTraceLog = new SystemTraceLog();

        try {
            long start = System.currentTimeMillis();
            HashMap<String, String> allParameters = getAllParameters(request);

            String callName = getMerchantId(request);

            String request_url = request.getRequestURL().toString();

            String[] preSplit = request_url.split(PRE_API);
            String[] postSplit = request_url.split(PRE_API_DEDUCT);

            String preProcessUrl = preSplit[1];
            String postProcessUrl = postSplit[1];

            String ipAddress = IpUtil.getIpAddress(request);

            String eventKey = "requestNo:"+allParameters.getOrDefault("requestNo", StringUtils.EMPTY);
            systemTraceLog = new SystemTraceLog(ZhonghangResponseMapSpec.EVENT_NAME.DEDUCT_RECV_TRADE_FROM_OUTLIER_SYSTEM, eventKey,
                    allParameters.toString(), "", SystemTraceLog.INFO, ipAddress, ZhonghangResponseMapSpec.SYSTEM_NAME.OUTLIER_SYSTEM, ZhonghangResponseMapSpec.SYSTEM_NAME.DEDUCT);
            LOGGER.info(systemTraceLog);

            HashMap<String, String> delayPostRequestParams = new HashMap<>();
            //调用前置接口处理
            int errorCode = prePositionHandler.prePositionDefaultTaskHandler(preProcessUrl, allParameters, delayPostRequestParams, LOGGER);

            if(errorCode != ApiResponseCode.SUCCESS){
                systemTraceLog.setEventName(ZhonghangResponseMapSpec.EVENT_NAME.DEDUCT_RECV_TRADE_FROM_OUTLIER_SYSTEM_ERROR);
                systemTraceLog.setErrorMsg(delayPostRequestParams.get("errorMsg"));
                LOGGER.info(systemTraceLog);
				return signErrorResult(response, ApiResponseCode.INVALID_PARAMS,delayPostRequestParams.get("errorMsg"));

            }
            systemTraceLog.setEventName(ZhonghangResponseMapSpec.EVENT_NAME.DEDUCT_RECV_TRADE_FROM_OUTLIER_SYSTEM_START);
            LOGGER.info(systemTraceLog);

            DeductRequestModel model = new DeductRequestModel(delayPostRequestParams, urlForCitigroupCallBack, urlForJpmorganCallback);
            postProcessUrl = urlToCitigroup+postProcessUrl;
            try{
                deductApplicationBusinessHandler.saveDeductInfo(model, callName, ipAddress, postProcessUrl);
            }catch (SQLGrammarException e){
                e.printStackTrace();
                throw new ApiException(ApiResponseCode.REPEAT_REQUEST_NO, "扣款请求受理失败,请检查请求中的requestNo等唯一性数据!");
            }

            deductApplicationBusinessHandler.pushJobToCitigroup(model, deductSendMorganStanleyNotifyJobServer, postProcessUrl,groupNameForDeductBusinessAcceptance);

            eventKey = eventKey + "#deductApplicationUuid:"+model.getDeductApplicationUuid();
            systemTraceLog.setEventName(ZhonghangResponseMapSpec.EVENT_NAME.DEDUCT_RECV_TRADE_FROM_OUTLIER_SYSTEM_END);
            systemTraceLog.setEventKey(eventKey);
            systemTraceLog.setUpStreamSystem(ZhonghangResponseMapSpec.SYSTEM_NAME.DEDUCT);
            systemTraceLog.setDownStreamSystem(ZhonghangResponseMapSpec.SYSTEM_NAME.CITIGROUP);
            LOGGER.info(systemTraceLog);
            long end = System.currentTimeMillis();
            LOGGER.info(GloableLogSpec.AuditLogHeaderSpec() + bridgewater_deduct_standard_function_point.SEND_MESSAGE_TO_CITIGROUP + "[SUCCESS,use:" +(end-start)+"]ms");

            return signSucResult(response);

        } catch (Exception e) {
            e.printStackTrace();
            systemTraceLog.setEventName(ZhonghangResponseMapSpec.EVENT_NAME.DEDUCT_RECV_TRADE_FROM_OUTLIER_SYSTEM_ERROR);
            systemTraceLog.setErrorMsg("System error!");
            systemTraceLog.setEventLevel(SystemTraceLog.ERROR);
            LOGGER.error(systemTraceLog);

            return signErrorResult(response,e);
        }
    }

}
