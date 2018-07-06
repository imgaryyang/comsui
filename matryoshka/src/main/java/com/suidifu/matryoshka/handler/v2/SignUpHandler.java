package com.suidifu.matryoshka.handler.v2;

import java.util.List;
import java.util.Map;

import com.suidifu.coffer.entity.ccb.sx.QuerySignModel;
import com.suidifu.coffer.entity.ccb.sx.SignUpModel;
import com.suidifu.coffer.entity.ccb.sx.SignUpResult;
import com.suidifu.coffer.exception.RequestDataException;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.zufangbao.sun.entity.bank.v2.PedestrianBankCode;
import com.zufangbao.sun.yunxin.entity.v2.SignUp;

public interface SignUpHandler {

	public  Map<String, String> genSignUpReturnParam(SignUpResult signUpResult, Map<String, String> allParameters);
	public  NotifyApplication getNotifyApplication(SignUp signUp, String requestUrl, QuerySignModel model,
			Map<String, String> workParams) throws RequestDataException;
	
	public QuerySignModel getQuerySignResult(SignUp signUp, Map<String, String> workParams,String signMethod,String signKey,String queryTransType,String requestUrl,String bankMerId);
	public SignUpModel genSendParam(Map<String, String> allParameters, Map<String, String> workParams,String signKey,String signMethod,String signTransType,String requestUrl,String bankMerId);
	public Map<String, String> genQuerySignUpReturnInfo(SignUp signUp);
	
	public List<PedestrianBankCode> getPedestrianBankCodeByName();
	
}
