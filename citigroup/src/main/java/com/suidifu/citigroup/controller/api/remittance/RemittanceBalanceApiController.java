package com.suidifu.citigroup.controller.api.remittance;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.citigroup.exception.CitiGroupRuntimeException;
import com.suidifu.citigroup.service.GeneralBalanceService;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.suidifu.swift.notifyserver.notifyserver.NotifyJobServer;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationCode;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec;
import com.zufangbao.wellsfargo.yunxin.cache.FinancialContractConfigurationCacheSpec;

@Controller
@RequestMapping("/balance/")
public class RemittanceBalanceApiController extends BaseController{

	private static Log logger = LogFactory.getLog(RemittanceBalanceApiController.class);
	
	@Autowired
	private GeneralBalanceService generalBalanceService;
	
	@Autowired
	private FinancialContractConfigurationService financialContractConfigurationService;
	
	
	@Autowired
	private NotifyJobServer fileJobServer;
	
	@Value("${urlToUpdatBankSavingLoan}")
	private String urlToUpdatBankSavingLoan;
	
	@RequestMapping(value = "addBankSavingLoan",method = RequestMethod.POST)
	public @ResponseBody String updatBankSavingLoan(HttpServletRequest request, HttpServletResponse response){
		
		try{
			String balanceRequestModelJsonString = request.getParameter(ZhonghangResponseMapSpec.BALANCEREQUESTMODEL);
			
			if (StringUtils.isEmpty(balanceRequestModelJsonString)) {
				logger.error("更新额度失败,传入的参数为空！");
				return jsonViewResolver.errorJsonResult("更新额度失败,传入的参数为空！");
			}
			
			pushJobToCitigroupForUpdateBankSavingLoan(balanceRequestModelJsonString);
			return jsonViewResolver.sucJsonResult();
		
		}catch(Exception e){
			
			logger.error("更新额度 occour error reason is ["+ExceptionUtils.getFullStackTrace(e)+"]");
			
			return genReturnInfoByMessage(e, new CitiGroupRuntimeException(), "更新额度 occour error reason is ["+ExceptionUtils.getFullStackTrace(e)+"]");
		}
	}
	
	/*@RequestMapping(value = "insertGeneralBalance/",method = RequestMethod.POST)
	public @ResponseBody String insertGeneralBalance(HttpServletRequest request, HttpServletResponse response,@ModelAttribute BalanceRequestModel balanceRequestModel){
	
		try {
			
			if (null==balanceRequestModel) {
				return jsonViewResolver.errorJsonResult("插入额度失败，没有参数！");
			}
			
			generalBalanceHandler.insertGeneralBalance(balanceRequestModel);
			
			return jsonViewResolver.sucJsonResult();
			
		} catch (Exception e) {
			
			logger.error("插入额度信息失败 reason is["+ExceptionUtils.getFullStackTrace(e)+"]");
			
			return genReturnInfoByMessage(e, new CitiGroupRuntimeException(), "插入额度信息失败 reason is ["+ExceptionUtils.getFullStackTrace(e)+"]");
		}
	}*/
	
	
	/**
	 * 查询信托所剩余额
	 * @param request
	 * @param response
	 * @param financialContractUuid
	 * @return
	 */
	@RequestMapping(value = "queryBankSavingLoan",method = RequestMethod.POST)
	public @ResponseBody String queryBankSavingLoan(HttpServletRequest request, HttpServletResponse response,@RequestParam(value="financialContractUuid") String financialContractUuid){
	
		try {
			
			if (StringUtils.isEmpty(financialContractUuid)) {
				return jsonViewResolver.errorJsonResult("financialContractUuid不能为空！");
			}
			
			BigDecimal amount = generalBalanceService.getValidBankSavingLoan(financialContractUuid);
			
			String coment = financialContractConfigurationService.getFinancialContractConfigContentContent(financialContractUuid, FinancialContractConfigurationCode.ALLOW_FINANCIAL_CONTRACT_DO_BALANCE.getCode());
			
			@SuppressWarnings("serial")
			Map<String, Object> data = new HashMap<String,Object>(){{
				put("remittancetotalAmount", coment);
				put("bankSavingLoan", amount);
			}};
			
			return jsonViewResolver.sucJsonResult(data);
			
		} catch (Exception e) {
			
			logger.error("插入额度信息失败 reason is["+ExceptionUtils.getFullStackTrace(e)+"]");
			
			return genReturnInfoByMessage(e, new CitiGroupRuntimeException(), "查询额度信息失败 reason is ["+ExceptionUtils.getFullStackTrace(e)+"]");
		}
	}
	
	private String genReturnInfoByMessage(Exception targetException,Exception fatherException,String message){

		if (targetException.getClass().equals(fatherException.getClass())) {
			
			return jsonViewResolver.errorJsonResult(targetException.getMessage());
			
		}
		
		return jsonViewResolver.errorJsonResult(message);
		
	}
	
	
	@SuppressWarnings("serial")
	private void pushJobToCitigroupForUpdateBankSavingLoan(String  balanceRequestModelJsonString) {
		HashMap<String, String> responsePacket = new HashMap<String, String>() {
			{
				put(ZhonghangResponseMapSpec.BALANCEREQUESTMODEL, balanceRequestModelJsonString);

			}
		};
		NotifyApplication notifyApplication = new NotifyApplication();
		notifyApplication.setRequestUrl(urlToUpdatBankSavingLoan);
		notifyApplication.setRequestParameters(responsePacket);

		fileJobServer.pushJob(notifyApplication);
	}
	
}
