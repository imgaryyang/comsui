package com.zufangbao.earth.yunxin.web.controller;

import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.web.controller.financial.CapitalControllerSpec.URL;
import com.zufangbao.earth.yunxin.handler.SystemOperateLogHandler;
import com.zufangbao.gluon.spec.earth.GlobalSpec4Earth;
import com.zufangbao.gluon.spec.global.GlobalCodeSpec;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.account.BankAccountAdapter;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.customer.CustomerType;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.BankAccountService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.VirtualAccountService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.VirtualAccountStatus;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.entity.model.VirtualAccountModel;
import com.zufangbao.sun.yunxin.entity.model.VirtualAccountShowModel;
import com.zufangbao.sun.yunxin.log.*;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.VirtualAccountDetailModel;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocument;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.VirtualAccountHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller()
@RequestMapping(URL.CAPITAL_NAME)
@MenuSetting(URL.CAPITAL_MENU)
public class VirtualAccountController extends BaseController{

	@Autowired
	private VirtualAccountHandler virtualAccountHandler;

	@Autowired
	private VirtualAccountService virtualAccountService;

	@Autowired
	private PrincipalHandler principalHandler;

	@Autowired
	private SystemOperateLogHandler systemOperateLogHandler;

	@Autowired
	private RecordLogCore recordLogCore;

	@Autowired
	private SystemOperateLogService systemOperateLogService;

	@Autowired
	public FinancialContractService financialContractService;

	private static final Log logger = LogFactory.getLog(VirtualAccountController.class);

	@RequestMapping(value = "customer-account-manage/virtual-account-list/show/options", method = RequestMethod.GET)
	public @ResponseBody String getOption(@Secure Principal principal) {
		try {
			HashMap<String, Object> result = new HashMap<String, Object>();
			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

			result.put("queryAppModels", queryAppModels);
			result.put("customerTypeList", EnumUtil.getKVList(CustomerType.class));
			result.put("virtualAccountStatusList", EnumUtil.getKVList(VirtualAccountStatus.class));
			return jsonViewResolver.sucJsonResult(result);
		} catch(Exception e){
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("列表选项获取错误");
		}

	}


	@RequestMapping("customer-account-manage/virtual-account-list/query")
	public @ResponseBody String queryVirtualAccount(@Secure Principal principal,
			@ModelAttribute VirtualAccountModel virtualAccountModel, Page page) {
		try {
			List<VirtualAccount> virtualAccounts = virtualAccountService.getVirtualAccountList(virtualAccountModel, page);
			List<VirtualAccount> counts = virtualAccountService.getVirtualAccountList(virtualAccountModel, null);
			List<VirtualAccountShowModel> virtualAccountShowModelList = virtualAccountHandler.getVirtualAccountList(virtualAccounts);

			Map<String, Object> data = new HashMap<String, Object>();
			data.putIfAbsent("virtualAccountModel", virtualAccountModel);
			data.putIfAbsent("list", virtualAccountShowModelList);
			data.putIfAbsent("size", counts.size());
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}


	/**
	 * 账户详情 详情页
	 * @param principal
	 * @param sourceDocumentUuid
	 * @return
	 */
	@RequestMapping(value = "customer-account-manage/virtual-account-list/detail-data", method = RequestMethod.GET)
	@MenuSetting("submenu-virtual-account-list")
	public @ResponseBody String virtualAccountShowDetail(@Secure Principal principal,@RequestParam("virtualAccountUuid") String virtualAccountUuid) {
		try {
			VirtualAccountDetailModel virtualAccountDetailModel = virtualAccountHandler.createVirtualAccountDetailModel(virtualAccountUuid);

			Map<String,Object> data = new HashMap<String,Object>();
			data.put("accountDepositModel", virtualAccountDetailModel);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	/**
	 *  银行卡 绑定
	 * @param principal
	 * @param
	 * @return
	 */
	@RequestMapping(value = "customer-account-manage/virtual-account-list/bank_card_binding", method = RequestMethod.GET)
	public @ResponseBody String bankCardBinding(@Secure Principal principal,@RequestParam("uuid") String uuid,
			@RequestParam("virtualAccountUuid") String virtualAccountUuid,HttpServletRequest request) {
		try {

			VirtualAccount virtualAccount =  virtualAccountService.getVirtualAccountByVirtualAccountUuid(virtualAccountUuid);

			CustomerType customerType =  EnumUtil.fromOrdinal(CustomerType.class, virtualAccount.getCustomerType());
			BankAccountService bankAccountService=BankAccountService.bankAccountServiceFactory(customerType);

			BankAccountAdapter bankAccountAdapter =  bankAccountService.bindingAccount(uuid, virtualAccountUuid);

			// binding bankCard
			SystemOperateLogRequestParam param = getSystemOperateLogrequestParamBinding(principal, request,virtualAccount,bankAccountAdapter.getAccountNo());
			systemOperateLogHandler.generateSystemOperateLog(param);


			Map<String,Object> data = new HashMap<String,Object>();
			data.put("data", bankAccountAdapter);
			return jsonViewResolver.sucJsonResult(data);

		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	/**
	 * 银行卡 解绑
	 * @param principal
	 * @param uuid
	 * @param virtualAccountUuid
	 * @param customerType
	 * @return
	 */
	@RequestMapping(value = "customer-account-manage/virtual-account-list/bank_card_tiedUp", method = RequestMethod.GET)
	public @ResponseBody String bankCardTiedUp(@Secure Principal principal,@RequestParam("uuid") String uuid,
			@RequestParam("virtualAccountUuid") String virtualAccountUuid,HttpServletRequest request) {
		try {

			VirtualAccount virtualAccount =  virtualAccountService.getVirtualAccountByVirtualAccountUuid(virtualAccountUuid);

			CustomerType customerType =  EnumUtil.fromOrdinal(CustomerType.class, virtualAccount.getCustomerType());
			BankAccountService bankAccountService=BankAccountService.bankAccountServiceFactory(customerType);

			BankAccountAdapter bankAccountAdapter =  bankAccountService.tiedUpAccount(uuid);

			// binding bankCard
			if(bankAccountAdapter != null){

				String recordContent = "解绑银行卡，银行账户号为【" + bankAccountAdapter.getAccountNo() + "】";
                systemOperateLogHandler.operateLog(principal, IpUtil.getIpAddress(request), LogFunctionType.TIEDUPBANKCARD, LogOperateType.ADD
                        ,virtualAccount, bankAccountAdapter.getAccountNo(), recordContent);

				Map<String,Object> data = new HashMap<String,Object>();
				data.put("data", bankAccountAdapter);
				return jsonViewResolver.sucJsonResult(data);
			}else{

				return jsonViewResolver.jsonResult(GlobalSpec4Earth.CODE_ENUM_ALL,"不能解绑");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}


	/**
	 * 银行卡编辑后  保存
	 * @param principal
	 * @param uuid
	 * @param customerType
	 * @return
	 */
	@RequestMapping(value = "customer-account-manage/virtual-account-list/bank_card_update_after", method = RequestMethod.GET)
	public @ResponseBody String updateAfterBankCard(@Secure Principal principal,@RequestParam String bankAccountAdapterJson,
			@RequestParam("virtualAccountUuid") String virtualAccountUuid,@RequestParam("uuid") String uuid,HttpServletRequest request) {

		try {
			VirtualAccount virtualAccount =  virtualAccountService.getVirtualAccountByVirtualAccountUuid(virtualAccountUuid);
			BankAccountAdapter bankAccountAdapter = JsonUtils.parse(bankAccountAdapterJson,BankAccountAdapter.class);

			CustomerType customerType =  EnumUtil.fromOrdinal(CustomerType.class, virtualAccount.getCustomerType());
			BankAccountService bankAccountService=BankAccountService.bankAccountServiceFactory(customerType);

			Boolean isUpdate = bankAccountService.getAccountByVirtualAccountAndAccountNo(virtualAccountUuid, bankAccountAdapter.getAccountNo(),uuid);

			BankAccountAdapter bankAccount = null;

			if(isUpdate){

				bankAccount =  bankAccountService.update(bankAccountAdapter, uuid);
			}

			if(bankAccount != null){
				// update bankCard log
				try {
					BankAccountAdapter origin = bankAccountService.getBankAccountAdapter(uuid);
					SystemOperateLog log = recordLogCore.insertNormalRecordLog(
                            principal.getId(), IpUtil.getIpAddress(request),
                            LogFunctionType.UPDATEBANKCARD,
                            LogOperateType.ADD,
							virtualAccount);
					log.setKeyContent(virtualAccount.getUuid());
					StringBuffer recordContentBuffer = new StringBuffer();
					recordContentBuffer.append("编辑银行卡，银行账户号为【"+ origin.getAccountNo() +"】");
					if(!origin.getAccountNo().equals(bankAccount.getAccountNo())){
                        recordContentBuffer.append("，账户号由【"+origin.getAccountNo()+"】更改为【"+bankAccount.getAccountNo()+"】");
                    }
					if(!origin.getBankName().equals(bankAccount.getBankName())) {
                        recordContentBuffer.append("，开户行由【"+origin.getBankName()+"】更改为【"+bankAccount.getBankName()+"】");
                    }
					log.setRecordContent(recordContentBuffer.toString());
					systemOperateLogService.saveOrUpdate(log);
				} catch (Exception e) {
					//e.printStackTrace();
					logger.info("error operate log cause: " + e.getMessage());

				}
			}else if(isUpdate==Boolean.FALSE){

				return jsonViewResolver.errorJsonResult(GlobalCodeSpec.CODE_FAILURE,"编辑的账户号已存在！");
			}else{
				return jsonViewResolver.errorJsonResult(GlobalCodeSpec.CODE_FAILURE,"不能编辑");
			}

			Map<String,Object> data = new HashMap<String,Object>();
			data.put("data", bankAccount);
			return jsonViewResolver.sucJsonResult(data);

		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}


	/**
	 * 新增 银行卡
	 * @param principal
	 * @param uuid
	 * @param customerType
	 * @return
	 */
	@RequestMapping(value = "customer-account-manage/virtual-account-list/insert_bank_card", method = RequestMethod.GET)
	public @ResponseBody String insertBankCard(@Secure Principal principal,HttpServletRequest request,@RequestParam String bankAccountAdapterJson,
			@RequestParam("virtualAccountUuid") String virtualAccountUuid,
			@RequestParam("contractId") Long contractId,@RequestParam("financialContractUuid") String financialContractUuid){

		try {

			VirtualAccount virtualAccount =  virtualAccountService.getVirtualAccountByVirtualAccountUuid(virtualAccountUuid);
			BankAccountAdapter bankAccountAdapter = JsonUtils.parse(bankAccountAdapterJson,BankAccountAdapter.class);

			CustomerType customerType =  EnumUtil.fromOrdinal(CustomerType.class, virtualAccount.getCustomerType());
			BankAccountService bankAccountService=BankAccountService.bankAccountServiceFactory(customerType);

			BankAccountAdapter bankAccount = bankAccountService.create(bankAccountAdapter, virtualAccountUuid, contractId, financialContractUuid);

			if(bankAccount != null){

				// add bankCard log
				String recordContent = "新增银行卡，银行账户号为【" + bankAccount.getAccountNo() + "】";
                systemOperateLogHandler.operateLog(principal, IpUtil.getIpAddress(request), LogFunctionType.ADDBANKCARD, LogOperateType.ADD
                        ,virtualAccount, bankAccount.getAccountNo(), recordContent);

				Map<String,Object> data = new HashMap<String,Object>();
				data.put("data", bankAccount);
				return jsonViewResolver.sucJsonResult(data);
			}else{

				return jsonViewResolver.errorJsonResult(GlobalCodeSpec.CODE_FAILURE,"新增的账户号已存在！");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	/**
	 * 判断同一个账户下  是否可以新增银行卡
	 * @param principal
	 * @param virtualAccountUuid
	 * @param accountNo
	 * @return
	 */
	@RequestMapping(value = "customer-account-manage/virtual-account-list/is_insert_bank_card", method = RequestMethod.GET)
	public @ResponseBody String isInsertBankCard(@Secure Principal principal,
			@RequestParam("virtualAccountUuid") String virtualAccountUuid,@RequestParam String bankAccountAdapterJson){

		try {

			VirtualAccount virtualAccount =  virtualAccountService.getVirtualAccountByVirtualAccountUuid(virtualAccountUuid);

			CustomerType customerType =  EnumUtil.fromOrdinal(CustomerType.class, virtualAccount.getCustomerType());
			BankAccountService bankAccountService=BankAccountService.bankAccountServiceFactory(customerType);

			BankAccountAdapter bankAccountAdapter = JsonUtils.parse(bankAccountAdapterJson,BankAccountAdapter.class);

			//true 可以新增     false不可新增
			Boolean isInsert = bankAccountService.getAccountByVirtualAccountAndAccountNo(virtualAccountUuid, bankAccountAdapter.getAccountNo());

			Map<String,Object> data = new HashMap<String,Object>();
			data.put("isInsert", isInsert);
			return jsonViewResolver.sucJsonResult(data);

		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}


	private SystemOperateLogRequestParam getSystemOperateLogrequestParamUpdate(Principal principal,
			HttpServletRequest request,VirtualAccount virtualAccount,String accountNo ) {
		SystemOperateLogRequestParam param = new SystemOperateLogRequestParam(principal.getId(),
                IpUtil.getIpAddress(request), "，账户号为[" + accountNo + "]", LogFunctionType.UPDATEBANKCARD,
                LogOperateType.ADD, SourceDocument.class, virtualAccount, null, null);
		return param;

	}

	private SystemOperateLogRequestParam getSystemOperateLogrequestParamBinding(Principal principal,
			HttpServletRequest request,VirtualAccount virtualAccount,String accountNo ) {
		SystemOperateLogRequestParam param = new SystemOperateLogRequestParam(principal.getId(),
                IpUtil.getIpAddress(request), "，账户号为[" + accountNo + "]", LogFunctionType.BINDINGBANKCARD,
                LogOperateType.ADD, SourceDocument.class, virtualAccount, null, null);
		return param;

	}

	private SystemOperateLogRequestParam getSystemOperateLogrequestParamTiedUp(Principal principal,
			HttpServletRequest request,VirtualAccount virtualAccount,String accountNo ) {
		SystemOperateLogRequestParam param = new SystemOperateLogRequestParam(principal.getId(),
                IpUtil.getIpAddress(request), "，账户号为[" + accountNo + "]", LogFunctionType.TIEDUPBANKCARD,
                LogOperateType.ADD, SourceDocument.class, virtualAccount, null, null);
		return param;

	}
}
