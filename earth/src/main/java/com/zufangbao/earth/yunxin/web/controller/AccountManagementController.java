package com.zufangbao.earth.yunxin.web.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.suidifu.hathaway.job.Priority;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.web.controller.financial.CapitalControllerSpec.URL;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.exception.GlobalRuntimeException;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.account.AccountSide;
import com.zufangbao.sun.entity.account.special.account.AccountTransType;
import com.zufangbao.sun.entity.account.special.account.SpecialAccount;
import com.zufangbao.sun.entity.account.special.account.SpecialAccountBasicType;
import com.zufangbao.sun.entity.account.special.account.SpecialAccountFlow;
import com.zufangbao.sun.entity.account.special.account.SpecialAccountType;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.ledgerbook.EntryLevel;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.yunxin.entity.model.AccountManagementModel;
import com.zufangbao.sun.yunxin.entity.model.AccountManagementShowModel;
import com.zufangbao.sun.yunxin.entity.model.ModifyAccrualAccountModel;
import com.zufangbao.sun.yunxin.entity.model.PaymentOrderModel;
import com.zufangbao.sun.yunxin.entity.model.QueryModifyAccrualAccountShowModel;
import com.zufangbao.sun.yunxin.entity.model.SpecialAccountInitializationModel;
import com.zufangbao.sun.yunxin.entity.model.specialaccount.SpecialAccountModel;
import com.zufangbao.sun.yunxin.entity.model.specialaccount.TransferAccrualAccountModel;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;
import com.zufangbao.sun.yunxin.service.account.SpecialAccountFlowService;
import com.zufangbao.sun.yunxin.service.account.SpecialAccountService;
import com.zufangbao.sun.yunxin.service.account.SpecialAccountTypeService;
import com.zufangbao.wellsfargo.proxy.handler.SpecialAccountProxy;
import com.zufangbao.wellsfargo.yunxin.handler.SpecialAccountTypeHandler;
import com.zufangbao.wellsfargo.yunxin.handler.specialaccount.SpecialAccountFlowHandler;
import com.zufangbao.wellsfargo.yunxin.handler.specialaccount.SpecialAccountHandler;

@Controller()
@RequestMapping(URL.CAPITAL_NAME)
@MenuSetting(URL.CAPITAL_MENU)
public class AccountManagementController extends BaseController{

	@Autowired
	private SpecialAccountFlowService specialAccountFlowService;
	
	@Autowired
	private PrincipalHandler principalHandler;
	
	@Autowired
	private FinancialContractService financialContractService;
	
	@Autowired
	private SpecialAccountFlowHandler specialAccountFlowHandler;
	
	@Autowired
	private SpecialAccountService specialAccountService;
	
	@Autowired
	private SpecialAccountHandler specialAccountHandler;

	@Autowired
	private SpecialAccountProxy specialAccountProxy;
	
	@Autowired
	private SpecialAccountTypeService specialAccountTypeService;
	
	@Autowired
	private SpecialAccountTypeHandler specialAccountTypeHandler;

	@Autowired
	private SystemOperateLogService systemOperateLogService;

	private static Log logger = LogFactory.getLog(AccountManagementController.class);


	@RequestMapping(value = "account-management-list/show",method = RequestMethod.GET)
	@MenuSetting("submenu-account-management-list")
	public ModelAndView showPaymentOrder(@ModelAttribute PaymentOrderModel paymentOrderModel,@Secure Principal principal,Page page,
			HttpServletRequest request){
		ModelAndView modelAndView = new ModelAndView("index");
		return modelAndView;
	}

	@RequestMapping(value = "account-management-list/show/options",method = RequestMethod.GET)
	@MenuSetting("submenu-account-management-list")
	public @ResponseBody String getOption(@Secure Principal principal, Page page){

		HashMap<String, Object> result = new HashMap<String, Object>();
//		List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
//		List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);
		
		List<SpecialAccountModel> fstSpecialAccounts = specialAccountService.getFstSpecialAccountListBy();
		
		result.put("fstSpecialAccounts", fstSpecialAccounts);
		result.put("accountSideList", EnumUtil.getKVList(AccountSide.class));
		result.put("accountTransTypeList", EnumUtil.getKVList(AccountTransType.class));
		return jsonViewResolver.sucJsonResult(result);
	}
	
	//二级账户 及对应的三级账户
	@RequestMapping(value = "account-management-list/account/name",method = RequestMethod.GET)
	@MenuSetting("submenu-account-management-list")
	public @ResponseBody String getAccountName(@Secure Principal principal,@RequestParam("fstLevelContractUuid") String fstLevelContractUuid){
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		//二级暂存户
		
		SpecialAccount specialAccountForPending = specialAccountService.getOneAccount(fstLevelContractUuid, EntryLevel.LVL2, SpecialAccountBasicType.PENDING);
		//三级账户
		List<SpecialAccount> specialAccountForPendingList = specialAccountService.getSpecialAccountListByParentUuidAndLevel(specialAccountForPending.getUuid(), EntryLevel.LVL3);
		
		//二级放款户
		SpecialAccount specialAccountForRemittance = specialAccountService.getOneAccount(fstLevelContractUuid, EntryLevel.LVL2, SpecialAccountBasicType.REMITTANCE);
		List<SpecialAccount> specialAccountForRemittanceList = specialAccountService.getSpecialAccountListByParentUuidAndLevel(specialAccountForRemittance.getUuid(), EntryLevel.LVL3);
		
		//二级还款户
		SpecialAccount specialAccountForRepayment = specialAccountService.getOneAccount(fstLevelContractUuid, EntryLevel.LVL2, SpecialAccountBasicType.BENEFICIARY_ACCOUNT);
		List<SpecialAccount> specialAccountForRepaymentList = specialAccountService.getSpecialAccountListByParentUuidAndLevel(specialAccountForRepayment.getUuid(), EntryLevel.LVL3);
		
		//二级计提户
		SpecialAccount specialAccountForAccrual = specialAccountService.getOneAccount(fstLevelContractUuid, EntryLevel.LVL2, SpecialAccountBasicType.ACCRUAL);
		List<SpecialAccount> specialAccountForAccrualList = specialAccountService.getSpecialAccountListByParentUuidAndLevel(specialAccountForAccrual.getUuid(), EntryLevel.LVL3);
		
		//二级客户账户
		SpecialAccount specialAccountForAccount = specialAccountService.getOneAccount(fstLevelContractUuid, EntryLevel.LVL2, SpecialAccountBasicType.INDEPENDENT_ACCOUNT);
		List<SpecialAccount> specialAccountForAccountList = specialAccountService.getSpecialAccountListByParentUuidAndLevel(specialAccountForAccount.getUuid(), EntryLevel.LVL3);
				
		
		result.put("specialAccountForPending", specialAccountForPending);
		result.put("specialAccountForPendingList", specialAccountForPendingList);
		
		result.put("specialAccountForRemittance", specialAccountForRemittance);
		result.put("specialAccountForRemittanceList", specialAccountForRemittanceList);
		
		result.put("specialAccountForRepayment", specialAccountForRepayment);
		result.put("specialAccountForRepaymentList", specialAccountForRepaymentList);
		
		result.put("specialAccountForAccrual", specialAccountForAccrual);
		result.put("specialAccountForAccrualList", specialAccountForAccrualList);
		
		result.put("specialAccountForAccount", specialAccountForAccount);
		result.put("specialAccountForAccountList", specialAccountForAccountList);
		return jsonViewResolver.sucJsonResult(result);
	}

	
	
	//专户账户流水列表
	@RequestMapping(value = "account-management-list/query")
	public @ResponseBody String querySpecialAccountFlowList(@Secure Principal principal,
			@ModelAttribute AccountManagementModel model, Page page) {

		try {
			
			List<SpecialAccountFlow> accountFlowList = specialAccountFlowService.querySpecialAccountFlowByModel(model, page);
			
			List<AccountManagementShowModel> showModelList = specialAccountFlowHandler.coverShowModelBySpecialAccountFlow(accountFlowList);

			int count = specialAccountFlowService.countSpecialAccountFlowByModel(model);
			
			Map<String , Object> data = new HashMap<String, Object>();
			data.putIfAbsent("list", showModelList);
			data.putIfAbsent("size", count);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	@RequestMapping(value = "query-special-account-type" ,method = RequestMethod.GET)
	public @ResponseBody String querySpecialAccountType() {
		try{ 
			List<SpecialAccountType> specialAccountTypes = specialAccountTypeService.getSpecialAccountTypeByBasicAccountType(SpecialAccountBasicType.getTotalSpecialAccountBasicType());
			
			SpecialAccountInitializationModel model= specialAccountTypeHandler.createSpecialAccountInitializationModel(specialAccountTypes);
			
			return jsonViewResolver.sucJsonResult("model",model);
		}catch(Exception e){
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e, "系统错误");
		}
	}

	@RequestMapping(value = "pre-transfer-account")
	public @ResponseBody String preTransferAccount(@RequestParam("hostAccountUuid") String hostAccountUuid) {
		try{
			SpecialAccount specialAccount = specialAccountService.get(hostAccountUuid);
			if(specialAccount==null){
				return jsonViewResolver.errorJsonResult("专户账户不存在");
			}
			List<SpecialAccount> specialAccountList = specialAccountService.getAccrualAccountOfTrd(specialAccount.getFstLevelContractUuid());
			Map<String,Object> params = new HashMap<>();
			params.put("accountList",specialAccountList);
			params.put("accountTransTypeList", EnumUtil.getKVListIncludes(AccountTransType.class,Arrays.asList(AccountTransType.ACCRUAL)));
			return jsonViewResolver.sucJsonResult(params);
		}catch (Exception e){
			logger.error("accrual error:"+ ExceptionUtils.getFullStackTrace(e));
			return jsonViewResolver.errorJsonResult(e, "系统错误");
		}
	}

	@RequestMapping(value = "transfer-accrual-account" ,method = RequestMethod.POST)
	public @ResponseBody String transfer_accrual_account(@ModelAttribute TransferAccrualAccountModel transferAccountModel,@Secure Principal principal,HttpServletRequest request){
		try{
			SpecialAccount specialAccount = specialAccountHandler.check(transferAccountModel);
			specialAccountProxy.transfer_revenue_to_accrual_account(transferAccountModel,specialAccount.getFstLevelContractUuid(),
					Priority.High.getPriority());
			//String format
			String recordContent = "专户账户从["+transferAccountModel.getHostAccountUuid()+"]转账至["+transferAccountModel.getCounterAccountUuid()+"]，"
					+ "金额["+transferAccountModel.getAmount()+"],交易类型["+transferAccountModel.getAccountTransType()+"]"+(transferAccountModel.getAccountTransTypeEnum()==null?"":transferAccountModel.getAccountTransTypeEnum().getChineseName());
			SystemOperateLog log = SystemOperateLog.createLog(principal.getId(), recordContent,
					IpUtil.getIpAddress(request), LogFunctionType.TRANSFER_SPECIAL_ACCOUNT, LogOperateType.ADD,
					specialAccount.getUuid(), "");
			systemOperateLogService.save(log);

			return jsonViewResolver.sucJsonResult();
		}catch(GlobalRuntimeException e){
			return jsonViewResolver.errorJsonResult(e, "系统错误");
		}catch (Exception e){
			logger.error("accrual error:"+ ExceptionUtils.getFullStackTrace(e));
			return jsonViewResolver.errorJsonResult(e, "系统错误");
		}
	}

	//查询计提户
	@RequestMapping(value = "{financial_contract_uuid}/query-financial-accrual-account" ,method = RequestMethod.POST)
	public @ResponseBody String query_financial_accrual_account(@PathVariable("financial_contract_uuid") String financialContractUuid){
		try{
			if(StringUtils.isEmpty(financialContractUuid)){
				throw new ApiException("请传入信托合同UUID");
			}
			List<SpecialAccount> specialAccounts = specialAccountService.getAccrualAccount(financialContractUuid);
			
			QueryModifyAccrualAccountShowModel model = specialAccountTypeHandler.createAccrualAccountShowModel(specialAccounts);
			return jsonViewResolver.sucJsonResult("model",model);
		}catch(Exception e){
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e, "系统错误");
		}
	}
	
	//新增计提户
	@RequestMapping(value = "modify-accrual-account" ,method = RequestMethod.POST)
	public @ResponseBody String modify_accrual_account(@RequestParam("accrual_account_list_json") String accrual_account_list_json){
		try{
			List<ModifyAccrualAccountModel> models = modelCheck(accrual_account_list_json);
			SpecialAccountType specialAccountType= specialAccountTypeService.getLv3AccrualAccountType();
			for(ModifyAccrualAccountModel model : models){
				SpecialAccount specialAccount = new SpecialAccount(SpecialAccountBasicType.ACCRUAL.ordinal(), specialAccountType.getAccountTypeCode(), model.getAccountTypeName(), model.getParentAccountUuid(), model.getFinancialContractUuid(), EntryLevel.LVL3.ordinal());
				specialAccountService.saveOrUpdate(specialAccount);
			}
			return jsonViewResolver.sucJsonResult();
		}catch(Exception e){
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e, "系统错误");
		}
	}
	
	private List<ModifyAccrualAccountModel> modelCheck(String accrual_account_list_json){
		List<ModifyAccrualAccountModel> models =  JsonUtils.parseArray(accrual_account_list_json, ModifyAccrualAccountModel.class);
		if(CollectionUtils.isEmpty(models)){
			return Collections.emptyList();
		}
		List<SpecialAccount> specialAccounts = specialAccountService.getAccrualAccountOfTrd(models.get(0).getFinancialContractUuid());
		for(ModifyAccrualAccountModel model: models){
			if(model.isVaild() == false){
				throw new ApiException("传入的某计提户不满足条件");
			}
			for(SpecialAccount specialAccount: specialAccounts){
				if(specialAccount.accountNameIsSame(model.getAccountTypeName())){
					throw new ApiException("存在相同名称的集体子户："+model.getAccountTypeName());
				}
			}
		}
		return models;
	}
	
}
