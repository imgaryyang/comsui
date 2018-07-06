package com.suidifu.matryoshka.handler.v2.impl;


import static com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.TYPE;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.demo2do.core.utils.DateUtils;
import com.suidifu.coffer.GlobalSpec;
import com.suidifu.coffer.entity.ccb.sx.QuerySignModel;
import com.suidifu.coffer.entity.ccb.sx.SignUpModel;
import com.suidifu.coffer.entity.ccb.sx.SignUpResult;
import com.suidifu.coffer.exception.RequestDataException;
import com.suidifu.coffer.handler.sxccb.SxCcbPacketHandler;
import com.suidifu.matryoshka.handler.v2.SignUpHandler;
import com.suidifu.matryoshka.handler.v2.util.UUIDUtil;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.zufangbao.sun.entity.bank.v2.PedestrianBankCode;
import com.zufangbao.sun.yunxin.entity.v2.SignUp;
import com.zufangbao.sun.yunxin.service.v2.PedestrianBankCodeservice;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.TRANSCATION_RESPONSE_CODE;

@Component("signUpHandler4M")
public class SignUpHandlerImpl implements SignUpHandler {

	@Autowired
	private SxCcbPacketHandler sxCcbPacketHandler;

	@Autowired
	private PedestrianBankCodeservice pedestrianBankCodeservice;

	@Override
    public  Map<String, String> genSignUpReturnParam(SignUpResult signUpResult, Map<String, String> allParameters) {
		Map<String, String> param = new HashMap();

		param.put("merId", allParameters.get("merId"));

		param.put("opType", allParameters.get("opType"));

		param.put("proNo", allParameters.get("proNo"));

		param.put("openBranch", null);

		param.put("tranResult", signUpResult == null ? null : signUpResult.getRespCode());

		param.put("comment",

		signUpResult == null ? allParameters.get("comment") : StringUtils.isEmpty(signUpResult.getResponseMessage())?signUpResult.getErrMsg():signUpResult.getResponseMessage() );

		return param;
	}

	@Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public  NotifyApplication getNotifyApplication(SignUp signUp, String requestUrl, QuerySignModel model,
			Map<String, String> workParams) throws RequestDataException {
		NotifyApplication notifyApplication = new NotifyApplication();

		notifyApplication.setHttpJobUuid(UUID.randomUUID().toString());

		notifyApplication.setBusinessId(signUp.getSignUpUuid());

		notifyApplication.setRequestParameters((HashMap) sxCcbPacketHandler.generateQuerySignPacket(model, workParams));

		notifyApplication.setRedundanceMap((HashMap<String, String>)workParams);

		notifyApplication.setRequestUrl(requestUrl);

		notifyApplication.setRequestMethod(NotifyApplication.POST_METHOD);

		notifyApplication.setDelaySecond(10);

		notifyApplication.setRetryTimes(5);

		notifyApplication.setRetryIntervals(new HashMap<Integer, Long>() {
			{
				put(1, 1000L);
				put(2, 1000L);
				put(3, 1000L);
				put(4, 1000L);
				put(5, 1000L);
			}
		});

		return notifyApplication;
	}



	@Override
    public QuerySignModel getQuerySignResult(SignUp signUp, Map<String, String> workParams, String signMethod, String signKey, String queryTransType, String requestUrl, String bankMerId) {

		String orderTime = com.suidifu.coffer.util.DateUtils.getFullDateTime(new Date());

		String orderNumber = signUp.getOrderNumber();

		String accName = signUp.getAccName();

		String accNo = signUp.getAccNo();

		String protocolNo = signUp.getProNo();

		QuerySignModel querySignModel = new QuerySignModel(bankMerId, orderTime, orderNumber, accName, accNo, protocolNo);

		workParams.put("version", GlobalSpec.ElecPayment.SX_CCB_CURRENT_VERSION);

		workParams.put("charset", GlobalSpec.ElecPayment.SX_CCB_DEFAULT_ENCODING);

		workParams.put("signMethod", signMethod);

		workParams.put("signKey", signKey);

		workParams.put("payType", signUp.getPayType());

		workParams.put("transType", queryTransType);

		workParams.put("merId", bankMerId);

		workParams.put("accName", accName);

		workParams.put("accNo", accNo);

		workParams.put("certifId", signUp.getCertifId());

		workParams.put("protocolNo", signUp.getProNo());

		workParams.put("proBegin", DateUtils.format(signUp.getProBeginDate(), "yyyyMMdd"));

		workParams.put("proEnd", DateUtils.format(signUp.getProEndDate(), "yyyyMMdd"));

		workParams.put("transMaxAmount", signUp.getTransMaxAmount().toString());

		workParams.put("requestUrl", requestUrl);

		return querySignModel;
	}


	@Override
    public SignUpModel genSendParam(Map<String, String> allParameters, Map<String, String> workParams, String signMethod, String signKey, String signTransType, String requestUrl, String bankMerId) {

		workParams.put("signMethod", signMethod);

		workParams.put("payType", allParameters.get("payType"));

		workParams.put("transType", signTransType);

		workParams.put("merId", bankMerId);

		workParams.put("signKey", signKey);

		workParams.put("requestUrl", requestUrl);

		Map<String, String> bankInfo = new HashMap<String, String>();

		bankInfo.put("bankCode", allParameters.get("stdBankCode"));

		String bankinfoStr = JSON.toJSONString(bankInfo);

		String orderNumber = UUIDUtil.uuid();

		Map<String, Object> param = new HashMap<>();

		param.put("orderNumber", orderNumber);

		param.put("accName", allParameters.get("accName"));

		param.put("accNo", allParameters.get("accNo"));

		param.put("certifId", allParameters.get("certId"));

		param.put("phoneNo", allParameters.get("phoneNo"));

		param.put("protocolNo", allParameters.get("proNo"));

		param.put("bankInfo", bankinfoStr);

		param.put("proBegin", DateUtils.parseDate(allParameters.get("proBeg"), "yyyyMMdd"));

		param.put("proEnd", DateUtils.parseDate(allParameters.get("proEnd"), "yyyyMMdd"));

		param.put("transMaxAmount", allParameters.get("tranMaxAmt"));

		param.put("orderTime", new Date());// 交易时间

		param.put("operationType", TYPE.get(allParameters.get("opType")));

		allParameters.put("orderNumber", orderNumber);

		return new SignUpModel(param);

	}

	@Override
    public Map<String, String> genQuerySignUpReturnInfo(SignUp signUp) {
		Map<String, String> param = new HashMap();

		param.put("merId", signUp.getMerId());

		param.put("proNo", signUp.getProNo());

		param.put("accName", signUp.getAccName());

		param.put("accNo", signUp.getAccNo());

		param.put("certType", signUp.getCertType());

		param.put("certId", signUp.getCertifId());

		param.put("phoneNo", signUp.getPhoneNo());

		param.put("proBeg", signUp.getProBeginDate() == null ? null : signUp.getProBeginDate().toString());

		param.put("proEnd", signUp.getProEndDate() == null ? null : signUp.getProEndDate().toString());

		param.put("tranMaxAmt", signUp.getTransMaxAmount() == null ? null : signUp.getTransMaxAmount().toString());

		param.put("signTime", signUp.getSignTime() == null ? null : signUp.getSignTime().toString());

		param.put("cancelTime", signUp.getCancelTime() == null ? null : signUp.getCancelTime().toString());

		param.put("remark", signUp.getRemark());

		param.put("tranResult", TRANSCATION_RESPONSE_CODE.SUCCESS);

		param.put("signState", signUp.getSignUpStatus().getChineseMessage());


		return param;

	}

	@Override
	@Cacheable("bankNamesForHeadQuarter")
	public List<PedestrianBankCode> getPedestrianBankCodeByName() {
		List<PedestrianBankCode> bankList = pedestrianBankCodeservice.getCachedBankNames();

		if (CollectionUtils.isEmpty(bankList)) {

			return Collections.EMPTY_LIST;

		}

		return bankList.stream().filter(bank->bank.getHeadQuarter()==1).collect(Collectors.toList());

	}



}
