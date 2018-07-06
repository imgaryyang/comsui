package com.zufangbao.earth.zhonghang.api.controller.v2;


import com.alibaba.fastjson.JSON;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.coffer.entity.ccb.sx.SignUpModel;
import com.suidifu.coffer.entity.ccb.sx.SignUpResult;
import com.suidifu.coffer.exception.RequestDataException;
import com.suidifu.coffer.exception.ResponseParseException;
import com.suidifu.coffer.handler.ThirdPartyPayHandler;
import com.suidifu.coffer.handler.sxccb.SxCcbHandler;
import com.suidifu.matryoshka.delayPosition.DelayPositionHandler;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.prePosition.PrePositionHandler;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.zufangbao.earth.yunxin.api.v2.RSABaseApiController;
import com.zufangbao.earth.yunxin.handler.impl.deduct.notify.v2.DeductBusinessNotifyJobServer;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationCode;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.yunxin.entity.v2.SignUp;
import com.zufangbao.sun.yunxin.entity.v2.SignUpCheckLog;
import com.zufangbao.sun.yunxin.entity.v2.SignUpQueryModel;
import com.zufangbao.sun.yunxin.service.v2.SignUpCheckLogService;
import com.zufangbao.sun.yunxin.service.v2.SignUpService;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.TRANSCATION_RESPONSE_CODE;
import com.zufangbao.wellsfargo.api.util.v2.UUIDUtil;
import com.zufangbao.wellsfargo.yunxin.handler.v2.SignUpHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 前置接口API Created by zhanglongfei .
 */
@Controller
@RequestMapping("/pre/api")
@Api(value = "五维金融贷后前置接口", description = "五维金融贷后前置接口")
public class ZhongHangSignUpController extends RSABaseApiController {

    private static final String SIGN_UP = "zhonghang/zhonghang/sign-up";
    private static final String PRE_API = "/pre/api/";

    private static final Log log = LogFactory.getLog(ZhongHangSignUpController.class);

    @Autowired
    private SxCcbHandler sxCcbHandler;

    @Autowired
    private SignUpService signUpService;

    @Autowired
    private SignUpHandler signUpHandler;

    @Autowired
    private ThirdPartyPayHandler tongLianHandler;

    @Autowired
    private FinancialContractConfigurationService financialContractConfigurationService;

    @Autowired
    private DeductBusinessNotifyJobServer deductBusinessNotifyJobServer;

    @Autowired
    @Qualifier("normalProductCategoryCacheHandler")
    private com.suidifu.matryoshka.handler.ProductCategoryCacheHandler productCategoryCacheHandler;

    @Autowired
    @Qualifier("normalDelayPositionHandler")
    private DelayPositionHandler delayPositionHandler;
    @Autowired
    @Qualifier("prePositionHandler")
    private PrePositionHandler prePositionHandler;
    @Autowired
    @Qualifier("normalPreSandboxHandler")
    private SandboxDataSetHandler sandboxDataSetHandler;

    @Value("#{config['zhonghang.signMethod']}")
    private String signMethod;

    @Value("#{config['zhonghang.requestUrl']}")
    private String requestUrl;

    @Value("#{config['zhonghang.signTransType']}")
    private String signTransType;

    @Value("#{config['zhonghang.queryTransType']}")
    private String queryTransType;

    @Value("#{config['zhonghang.cachedJobQueueSize']}")
    private String cachedJobQueueSize;
    @Value("#{config['zhonghang.serverIdentity']}")
    private String serverIdentity;
    @Value("#{config['zhonghang.persistenceMode']}")
    private int persistenceMode;

    @Value("#{config['notifyserver.groupCacheJobQueueMap_group0']}")
    private String groupNameForSignUp;

    @Autowired
    private SignUpCheckLogService signUpCheckLogService;


    @RequestMapping(value = "/" + SIGN_UP, method = RequestMethod.POST)
    @ApiOperation(value = "签约认证", notes = "签约认证前置接口")
    @ApiImplicitParams({@ApiImplicitParam(name = "merId", value = "商户代码", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "tranType", value = "交易类型", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "opType", value = "操作类型", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "proNo", value = "协议号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "bankAliasName", value = "行别", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "bankFullName", value = "开户行支行名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "payType", value = "接口类型", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "accName", value = "户名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "accNo", value = "银行账号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "certType", value = "证件类型", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "certId", value = "证件号码", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "phoneNo", value = "手机号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "proBeg", value = "协议开始日期", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "proEnd", value = "协议截止日期", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "tranMaxAmt", value = "单笔金额上限", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "productCode", value = "信托产品代码", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "remark", value = "备注", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "paymentInstitution", value = "交易通道", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "contextJson", value = "通道信息配置", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "signData", value = "签名数据", paramType = "query", dataType = "String"),
    })
    public @ResponseBody
    String signUpForProject(HttpServletRequest request, HttpServletResponse response) {
        try {
            HashMap<String, String> allParameters = getAllParameters(request);

            String request_url = request.getRequestURL().toString();

            String[] split = request_url.split(PRE_API);

            String preProcessUrl = split[1];

            HashMap<String, String> DelayPostRequestParams = new HashMap<>();

            Map<String, String> param = new HashMap<>();

            StringBuffer comment = new StringBuffer();

            List<String> proNoList = new ArrayList<>();

            List<Boolean> tranResultList = new ArrayList<>();

            List<String> signUpUuids = new ArrayList<>();

            allParameters.put(ZhonghangResponseMapSpec.TRANRESULT, TRANSCATION_RESPONSE_CODE.FAILURE); //默认请求失败

            // 调用前置接口处理
            int errorCode = prePositionHandler.prePositionDefaultTaskHandler(preProcessUrl, allParameters,
                    DelayPostRequestParams, log);

            if (errorCode != ApiResponseCode.SUCCESS) {

                allParameters.put(ZhonghangResponseMapSpec.COMMENT, DelayPostRequestParams.get("errMsg"));

                Map<String, String> params = signUpHandler.genSignUpReturnParam(allParameters);

                return signResult(params);
            }

            @SuppressWarnings("unchecked")
            List<String> paymentInstitutions = JsonUtils.parse(DelayPostRequestParams.get(ZhonghangResponseMapSpec.PAYMENTINSTITUTIONS), List.class);

            String financialContractUuid = DelayPostRequestParams.get("financialContractUuid");

            if (CollectionUtils.isEmpty(paymentInstitutions)) {

                return genReturnInfo("没有信托对应的支付通道信息!", TRANSCATION_RESPONSE_CODE.FAILURE);
            }

            for (String paymentInstitution : paymentInstitutions) {

                DelayPostRequestParams.put(ZhonghangResponseMapSpec.PAYMENTINSTITUTION, paymentInstitution);

                dealSignUpWithPaymentInstitution(DelayPostRequestParams, financialContractUuid, paymentInstitution, comment, proNoList, tranResultList, signUpUuids);
            }

            List<Boolean> tranResultListInSuccess = tranResultList.stream().filter(t -> t == true).collect(Collectors.toList());

            String transResult = CollectionUtils.isEmpty(tranResultListInSuccess) ? TRANSCATION_RESPONSE_CODE.FAILURE : tranResultListInSuccess.containsAll
                    (tranResultList) ? TRANSCATION_RESPONSE_CODE.SUCCESS : TRANSCATION_RESPONSE_CODE.PART_SUCCESS;

            DelayPostRequestParams.put(ZhonghangResponseMapSpec.TRANRESULT, transResult);

            DelayPostRequestParams.put(ZhonghangResponseMapSpec.PRONOS, JsonUtils.toJsonString(proNoList));

            DelayPostRequestParams.put(ZhonghangResponseMapSpec.COMMENT, comment.toString());

            DelayPostRequestParams.put("signUpUuids", JsonUtils.toJsonString(signUpUuids));

            param = signUpHandler.genSignUpReturnParam(DelayPostRequestParams);

            if (!TRANSCATION_RESPONSE_CODE.SUCCESS.equals(transResult)) {

                SignUpCheckLog signUpCheckLog = new SignUpCheckLog(DelayPostRequestParams);

                signUpCheckLogService.saveOrUpdate(signUpCheckLog);
            }

            return signResult(param);

        } catch (Exception e) {
            log.error("#签约认证 occur error !", e);
            // 返回结果信息为失败，出现异常
            return genReturnInfo("系统错误！", TRANSCATION_RESPONSE_CODE.SYSTEM_ERROR);
        }
    }

    @SuppressWarnings("unchecked")
    private void dealSignUpWithPaymentInstitution(HashMap<String, String> DelayPostRequestParams, String financialContractUuid, String Institution, StringBuffer
            comment, List<String> proNoList, List<Boolean> tranResultList, List<String> signUpUuids) throws RequestDataException, ResponseParseException {

        PaymentInstitutionName paymentInstitution = PaymentInstitutionName.fromValue(Integer.valueOf(Institution));

        Map<String, String> params = new HashMap<>();

        if (null == paymentInstitution) {

            comment.append("没有对应的支付通道配置!");

            tranResultList.add(false);

            return;

        }

        DelayPostRequestParams.put("orderNumber", UUIDUtil.uuid());

        SignUpResult signUpResult = null;

        NotifyApplication job = null;

        SignUp signUp = signUpHandler.getSignUpByOpTypeAndPaymentInstitution(DelayPostRequestParams, paymentInstitution);

        Map<String, String> param = new HashMap<>();

        switch (paymentInstitution) {
            case JIANHANG://建行

                signUpResult = getSignUpResultForJianHang(DelayPostRequestParams, financialContractUuid);

                if (signUpHandler.genSignUpStatus(signUp, signUpResult, DelayPostRequestParams, tranResultList)) {

                    job = signUpHandler.pushJobForJianHang(signUp, DelayPostRequestParams, queryTransType);

                }

                break;

            case TONGLIAN:// 通联

                signUp.setProNo(DelayPostRequestParams.getOrDefault("proNoForRecover", signUp.getProNo()));

                signUpResult = getSignUpResultForTongLian(DelayPostRequestParams, financialContractUuid);


                if (signUpHandler.genSignUpStatus(signUp, signUpResult, DelayPostRequestParams, tranResultList)) {

                    job = signUpHandler.pushJobForTongLian(signUp, DelayPostRequestParams);

                }

                break;

            default:

                comment.append("没有对应的支付通道信息!");

                tranResultList.add(false);

                return;
        }

        signUpService.saveOrUpdateSignUp(signUp);

        if (null != job) {

            job.setGroupName(groupNameForSignUp);

        }

        deductBusinessNotifyJobServer.pushJob(job);

        proNoList.add(signUp.getProNo());

        signUpUuids.add(signUp.getSignUpUuid());

        String commentString = signUpResult == null ? DelayPostRequestParams.get(ZhonghangResponseMapSpec.COMMENT) : (StringUtils.isEmpty(signUpResult
                .getResponseMessage()) ? signUpResult.getErrMsg() : signUpResult.getResponseMessage());

        comment.append("对应通道编号[" + Institution + "] 签约或解约结果：" + commentString);
    }


    @RequestMapping(value = "/sign_up/query", method = RequestMethod.POST)
    public @ResponseBody
    String getSignUp(HttpServletRequest request, HttpServletResponse response,
                     @ModelAttribute SignUpQueryModel signUpQueryModel) {
        try {
            String param = ZhonghangResponseMapSpec.validateParam(signUpQueryModel);

            if (StringUtils.isNotEmpty(param)) {

                return genReturnInfo(param, TRANSCATION_RESPONSE_CODE.FAILURE);

            }

            SignUp signUp = signUpService.getSignUpByAccNoOrProNo(signUpQueryModel.getAccNo(), signUpQueryModel.getProNo(), signUpQueryModel.getProductCode());

            if (null == signUp) {

                return genReturnInfo("没有签约信息！", TRANSCATION_RESPONSE_CODE.FAILURE);

            }

            return signResult(signUpHandler.genQuerySignUpReturnInfo(signUp));
        } catch (Exception e) {

            log.error("#签约查询 occur error !", e);

            return genReturnInfo("系统错误！", TRANSCATION_RESPONSE_CODE.SYSTEM_ERROR);
        }

    }

    @SuppressWarnings("unchecked")
    private String genReturnInfo(String comment, String tranResult) {
        Map<String, String> params = signUpHandler.genSignUpReturnParam(new HashMap() {
            {
                put("tranResult", tranResult);
                put("comment", comment);
            }
        });
        return signResult(params);
    }


    private SignUpResult getSignUpResultForJianHang(Map<String, String> DelayPostRequestParams, String financialContractUuid) throws RequestDataException {


        String message = null;

        String contentJson = DelayPostRequestParams.get("contextJson");

        Map<String, String> content = StringUtils.isEmpty(contentJson) ? signUpHandler.getContextJsonByInsition(financialContractUuid,
                FinancialContractConfigurationCode.SIGN_UP_CHANNEL_INFORMATION_AND_FINANCIALCONTRACT_PAYTYPE.getCode(), String.valueOf(PaymentInstitutionName
                        .JIANHANG.getOrdinal())) : JSON.parseObject(contentJson, Map.class);

        if (MapUtils.isEmpty(content)) {

            DelayPostRequestParams.put("comment", "签约通道[" + DelayPostRequestParams.get("paymentInstitution") + "]没有对应的通道信息配置！");

            return null;

        }
        String signKey = content.get("signKey");

        String bankMerId = content.get("bankMerId");

        log.info("getSignUpResult signKey[" + signKey + "]    bankMerId[" + bankMerId + "]");

        if ((message = valideSignKeyAndBankMerId(bankMerId, signKey)) != null) {

            DelayPostRequestParams.put("comment", message);

            return null;
        }

        Map<String, String> workParams = new HashMap<>();

        DelayPostRequestParams.put("signMethod", signMethod);

        DelayPostRequestParams.put("signTransType", signTransType);

        DelayPostRequestParams.put("requestUrl", requestUrl);

        DelayPostRequestParams.put("signKey", signKey);

        DelayPostRequestParams.put("bankMerId", bankMerId);

        SignUpModel signUpModel = signUpHandler.genSendParamForJianHang(DelayPostRequestParams, workParams, signMethod, signKey, signTransType, requestUrl,
                bankMerId);

        SignUpResult signUpResult = sxCcbHandler.signUpWithBank(signUpModel, workParams);

        return signUpResult;
    }

    private SignUpResult getSignUpResultForTongLian(Map<String, String> DelayPostRequestParams, String financialContractUuid) throws RequestDataException,
            ResponseParseException {

        Map<String, String> workParams = new HashMap<>();

        String contentJson = DelayPostRequestParams.get("contextJson");

        Map<String, String> content = StringUtils.isEmpty(contentJson) ? signUpHandler.getContextJsonByInsition(financialContractUuid,
                FinancialContractConfigurationCode.SIGN_UP_CHANNEL_INFORMATION_AND_FINANCIALCONTRACT_PAYTYPE.getCode(), String.valueOf(PaymentInstitutionName
                        .TONGLIAN.getOrdinal())) : JSON.parseObject(contentJson, Map.class);

        if (MapUtils.isEmpty(content)) {

            DelayPostRequestParams.put("comment", "签约通道[" + DelayPostRequestParams.get("paymentInstitution") + "]没有对应的通道信息配置！");

            return null;

        }

        if (MapUtils.isEmpty(content)) {

            DelayPostRequestParams.put("comment", "签约通道[" + DelayPostRequestParams.get("paymentInstitution") + "]对应的通道信息配置有误！");

            return null;
        }

        DelayPostRequestParams.putAll(content);

        SignUpModel signUpModel = signUpHandler.genSendParamForTongLian(DelayPostRequestParams, workParams);
        SignUpResult signUpResult = tongLianHandler.signUpWithBank(signUpModel, workParams);

        return signUpResult;

    }


    private String valideSignKeyAndBankMerId(String bankMerId, String signKey) {

        String returnString = null;

        if (StringUtils.isEmpty(bankMerId)) {

            returnString = "没有信托合同对应的商户号!";

        }
        if (StringUtils.isEmpty(signKey)) {

            returnString = "没有信托合同对应的银行端signKey!";

        }

        return returnString;

    }

}