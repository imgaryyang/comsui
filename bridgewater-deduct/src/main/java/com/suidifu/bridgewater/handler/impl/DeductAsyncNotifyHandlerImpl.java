package com.suidifu.bridgewater.handler.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.suidifu.bridgewater.api.util.ApiSignUtils;
import com.zufangbao.gluon.spec.earth.v3.ApiConstant;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationCode;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationContentValue;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.yunxin.entity.Dictionary;
import com.zufangbao.sun.yunxin.entity.DictionaryCode;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.exception.DictionaryNotExsitException;
import com.zufangbao.sun.yunxin.service.DictionaryService;

@Component("deductAsyncNotifyHandler")
public class DeductAsyncNotifyHandlerImpl extends DeductAsyncNotifyHandler {
	
	private static final Log logger = LogFactory.getLog(DeductAsyncNotifyHandlerImpl.class);
	
	@Autowired
	private FinancialContractConfigurationService financialContractConfigurationService;
	
	@Autowired
	private DictionaryService dictionaryService;
	
	@Value("#{config['zhonghang.merId']}")
	private String YX_API_MERID;

	@Value("#{config['zhonghang.secret']}")
	private String YX_API_SECRET;
	
	public String genContextNotifyModel(DeductApplication deductApplication){
		
		Map<String, Object> params = new HashMap<>();
		params.put("requestId", UUID.randomUUID().toString());
		params.put("referenceId", deductApplication.getRequestNo());
		params.put("orderNo",deductApplication.getDeductId());
		params.put("amount",null==deductApplication.getActualDeductTotalAmount()?null:deductApplication.getActualDeductTotalAmount().toString());
		params.put("status",deductApplication.getExecutionStatus().getOrdinal());
		params.put("comment",deductApplication.getExecutionRemark());
		params.put("lastModifiedTime",deductApplication.getLastModifiedTime());
		params.put("paidNoticInfos",new HashMap<String,Object>());

		return JSON.toJSONString(params, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat);
	}


	public Map<String, String> buildHeaderParamsForNotifyDeductResult(
			String content,String financialContractUuid ) {
		Map<String, String> headerParams = new HashMap<String, String>();
		headerParams.put("Content-Type", "application/json");
		headerParams.put("merId", YX_API_MERID);
		headerParams.put(ApiConstant.PARAMS_SECRET,YX_API_SECRET);

		String context = financialContractConfigurationService.getFinancialContractConfigContentContent(financialContractUuid, FinancialContractConfigurationCode.ALLOW_SIGN.getCode());
		if (StringUtils.equals(context, FinancialContractConfigurationContentValue.SIGN)) {
			Dictionary dictionary;
			try {
				dictionary = dictionaryService.getDictionaryByCode(DictionaryCode.PLATFORM_PRI_KEY.getCode());
			} catch (DictionaryNotExsitException e) {
				logger.error(".#DeductApplicationBusinessHandlerImpl get private key fail");
				e.printStackTrace();
				return headerParams;
			}
			String signData = ApiSignUtils.rsaSign(content, dictionary.getContent());
			headerParams.put("sign", signData);
		}

		return headerParams;
	}
	
	
}
