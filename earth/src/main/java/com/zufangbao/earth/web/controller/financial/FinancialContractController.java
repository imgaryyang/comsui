package com.zufangbao.earth.web.controller.financial;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.web.controller.reapymentInfo.BankCoreCodeMapSpec;
import com.zufangbao.earth.yunxin.handler.AccountTemplateHandler;
import com.zufangbao.earth.yunxin.handler.CreateException;
import com.zufangbao.earth.yunxin.handler.FinancialContractHandler;
import com.zufangbao.earth.yunxin.handler.ModifyException;
import com.zufangbao.earth.yunxin.handler.SystemOperateLogHandler;
import com.zufangbao.gluon.opensdk.HttpClientUtils;
import com.zufangbao.gluon.spec.earth.v3.ApiConstant;
import com.zufangbao.gluon.spec.global.GlobalCodeSpec;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.account.Account;
import com.zufangbao.sun.entity.company.Company;
import com.zufangbao.sun.entity.company.corp.App;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FeeType;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationCode;
import com.zufangbao.sun.entity.financial.FinancialContractDetailModel;
import com.zufangbao.sun.entity.financial.FinancialContractType;
import com.zufangbao.sun.entity.financial.FinancialType;
import com.zufangbao.sun.entity.financial.InputHistory;
import com.zufangbao.sun.entity.financial.PlanRepaymentTimeConfiguration;
import com.zufangbao.sun.entity.financial.TemporaryRepurchaseJson;
import com.zufangbao.sun.entity.repurchase.RepurchaseCycle;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.handler.InputHistoryHandler;
import com.zufangbao.sun.service.AccountService;
import com.zufangbao.sun.service.AppService;
import com.zufangbao.sun.service.CompanyService;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.InputHistoryService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.AssetPackageFormat;
import com.zufangbao.sun.yunxin.entity.model.CreateFinancialContractModel;
import com.zufangbao.sun.yunxin.entity.model.ModifyFinancialContractBasicInfoModel;
import com.zufangbao.sun.yunxin.entity.model.ModifyFinancialContractRemittanceInfoModel;
import com.zufangbao.sun.yunxin.entity.model.ModifyFinancialContractRepaymentInfoModel;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.entity.model.financialcontract.FinancialContractQueryModel;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceStrategyMode;
import com.zufangbao.sun.yunxin.log.FinancialContractLog;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLogRequestParam;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec;

@RestController
@RequestMapping("/financialContract")
@MenuSetting("menu-financial")
public class FinancialContractController extends BaseController {

    @Autowired
    private FinancialContractService financialContractService;
    @Autowired
    private AppService appService;
    @Autowired
    private FinancialContractHandler financialContractHandler;
    @Autowired
    private PrincipalService principalService;
    @Autowired
    private SystemOperateLogHandler systemOperateLogHandler;
    @Autowired
    private ContractService contractService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private PrincipalHandler principalHandler;
    @Autowired
    private InputHistoryService inputHistoryService;
    @Autowired
    private InputHistoryHandler inputHistoryHandler;
    @Autowired
    private FinancialContractConfigurationService financialContractConfigurationService;
    @Autowired
    private AccountTemplateHandler accountTemplateHandler;
    private static final Log logger = LogFactory.getLog(FinancialContractController.class);
    
    @Value("#{config['urlToCitigroupForGetBankSavingLoan']}")
    private String urlToCitigroupForGetBankSavingLoan;

    // 获取下拉框选项
    @RequestMapping("/optionData")
    @MenuSetting("submenu-financial-contract")
    public @ResponseBody String showAllData(@Secure Principal principal) {
        try{
            Map<String, Object> data = new HashMap<>();
            List<App> appList = principalService.get_can_access_app_list(principal);
            data.put("appList", appList);
            data.put("financialContractTypeList", EnumUtil.getKVMap(FinancialContractType.class));
            return jsonViewResolver.sucJsonResult(data,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            logger.error("##FinancialContractController-showAllData## get option data error!!");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult(QUERY_ERROR);
        }
    }

    // 查询
    @RequestMapping("/query")
    @MenuSetting("submenu-financial-contract")
    public @ResponseBody String query(@Secure Principal principal,@ModelAttribute FinancialContractQueryModel financialContractQueryModel,Page page) {
        try {
            financialContractQueryModel.setPrincipalId(principal.getId());
            Map<String, Object> data = principalHandler.queryFinancialContractListByPrincipal(financialContractQueryModel, page);
            return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    //信托合同列表
    @RequestMapping("/financialContractList")
    @MenuSetting("submenu-financial-contract")
    public @ResponseBody String getFinancialContractList() {
        try {
            List<FinancialContract> financialContractList = financialContractService.loadAll(FinancialContract.class);
            List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContractList);
            return jsonViewResolver.sucJsonResult("queryAppModels", queryAppModels, SerializerFeature.DisableCircularReferenceDetect);
        } catch (Exception e) {
            logger.error("#getFinancialContractList occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("获取信托合同列表失败！！！");
        }
    }

	// 合作商户列表
	@RequestMapping("/applist")
	@MenuSetting("submenu-financial-contract")
	public @ResponseBody String getAppList() {
		try {
			List<App> appList = appService.loadAll(App.class);
			return jsonViewResolver.sucJsonResult("appList", appService.getKVList(appList),SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("#getAppList occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("获取合作商户列表失败！！！");
		}
	}

	// 信托公司列表
	@RequestMapping("/companyList")
	@MenuSetting("submenu-financial-contract")
	public @ResponseBody String getCompanyList() {
		try {
			List<Company> companyList = companyService.loadAll(Company.class);
			return jsonViewResolver.sucJsonResult("companyList", companyService.getKVList(companyList), SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("#getCompanyList occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("获取信托公司列表失败！！！");
		}
	}

	// 信托合同类型列表
	@RequestMapping("/financialContractType")
	@MenuSetting("submenu-financial-contract")
	public @ResponseBody String getFinancialContractType() {
		try {
			return jsonViewResolver.sucJsonResult("financialContractType", EnumUtil.getKVList(FinancialContractType.class), SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("#getFinancialContractType occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("获取信托合同类型列表失败！！！");
		}
	}

	// 信托类型
	@RequestMapping("/financialType")
	@MenuSetting("submenu-financial-contract")
	public @ResponseBody String getFinancialType() {
		try {
			return jsonViewResolver.sucJsonResult("financialType", EnumUtil.getKVList(FinancialType.class), SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("#getFinancialContractType occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("获取信托合同类型列表失败！！！");

	}
	}
	// 放款模式列表
	@RequestMapping("/remittanceStrategyMode")
	@MenuSetting("submenu-financial-contract")
	public @ResponseBody String getRemittanceStrategyMode() {
		try {
			return jsonViewResolver.sucJsonResult("remittanceStrategyMode", EnumUtil.getKVList(RemittanceStrategyMode.class), SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("#getRemittanceStrategyMode occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("获取放款模式列表失败！！！");
		}
	}

	// 还款类型列表
	@RequestMapping("/assetPackageFormat")
	@MenuSetting("submenu-financial-contract")
	public @ResponseBody String getAssetPackageFormat() {
		try {
			return jsonViewResolver.sucJsonResult("assetPackageFormat", EnumUtil.getKVList(AssetPackageFormat.class), SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("#getAssetPackageFormat occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("获取还款类型列表失败！！！");
		}
	}

	// 不定期回购周期
	@RequestMapping("/repurchaseCycle")
	@MenuSetting("submenu-financial-contract")
	public @ResponseBody String getRepurchaseCycle() {
		try {
			return jsonViewResolver.sucJsonResult("repurchaseCycle", EnumUtil.getKVList(RepurchaseCycle.class), SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("#getRepurchaseCycle occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("获取不定期回购周期失败！！！");
		}
	}

	// 费用名目列表
	@RequestMapping("/feeType")
	@MenuSetting("submenu-financial-contract")
	public @ResponseBody String getFeeType() {
		try {
			return jsonViewResolver.sucJsonResult("feeType", EnumUtil.getKVList(FeeType.class), SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("#getFeeType occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("获取费用名目列表失败！！！");
		}
	}

	// 银行名称列表
	@RequestMapping("/bankNames")
	@MenuSetting("submenu-financial-contract")
	public @ResponseBody String getBankNames() {
		try {
			return jsonViewResolver.sucJsonResult("bankNames", BankCoreCodeMapSpec.coreBankMap, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("#getBankNames occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("获取银行名称列表失败！！！");
		}
	}

    // 商户账户列表
    @RequestMapping(value = "/appAccountList", method = RequestMethod.GET)
    @MenuSetting("submenu-financiael-contract")
    public @ResponseBody String getAppAccountList(@RequestParam String financialContractUuid) {
        try {
            FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
            if(financialContract == null){
                return jsonViewResolver.errorJsonResult("信托合同不存在！");
            }
            List<String> uuids = financialContract.getAppAccountUuidList();
            List<Map<String, Object>> results = getAccountsBriefInfo(uuids);
            return jsonViewResolver.sucJsonResult("appAccountList", results, SerializerFeature.DisableCircularReferenceDetect);
        }catch (Exception e) {
            logger.error("#getAppAccountList occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("获取商户账户列表失败！！！");
        }
    }

    // 新增信托合同
    @RequestMapping(value = "/add-new-financialContract", method = RequestMethod.POST)
    @MenuSetting("submenu-financial-contract")
    public @ResponseBody String addNewFinancialContract(
            @RequestBody String createFinancialContractModelJson,
            @Secure Principal principal, HttpServletRequest request) {
        try {
            CreateFinancialContractModel createFinancialContractModel = JsonUtils.parse(createFinancialContractModelJson, CreateFinancialContractModel.class);
            FinancialContract financialContract = financialContractHandler.configFinancialContract(createFinancialContractModel, principal.getId());
            SystemOperateLogRequestParam param = getSystemOperateLogrequestParam(principal, request, financialContract);
            systemOperateLogHandler.generateSystemOperateLog(param);
            //添加脚本
            accountTemplateHandler.addTemplateByLedgerbookNo
                    (financialContract.getFinancialContractUuid());return jsonViewResolver.sucJsonResult("financialContractUuid", financialContract.getFinancialContractUuid());
        } catch (CreateException e) {
            logger.error("##addNewFinancialContract## occur error!");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult(e.getMessage());
        } catch (Exception e) {
            logger.error("##addNewFinancialContract## occur error!");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("创建信托合同失败！！！");
        }
    }
    
    // 信托合同详情页--数据获取
    @RequestMapping(value = "/detail/data", method = RequestMethod.GET)
    @MenuSetting("submenu-financial-contract")
    public @ResponseBody String showFinancailContractDetailData(HttpServletRequest request,@RequestParam(value="financialContractUuid") String financialContractUuid) {
        try {
            FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
            PlanRepaymentTimeConfiguration planRepaymentTimeLock = financialContractConfigurationService.getPlanRepaymentTimeConfiguration(financialContractUuid);
            String allowModifyRemittanceApplicationContent = financialContractConfigurationService.getFinancialContractConfigContentContent(financialContractUuid,FinancialContractConfigurationCode.ALLOW_MODIFY_REMITTANCE_APPLICATION.getCode());
			String allowNotOverdueAutoConfirm = financialContractConfigurationService.getFinancialContractConfigContentContent(financialContractUuid,FinancialContractConfigurationCode.ALLOW_NOT_OVERDUE_AUTO_CONFIRM.getCode());
			String allowOverdueAutoConfirm = financialContractConfigurationService.getFinancialContractConfigContentContent(financialContractUuid,FinancialContractConfigurationCode.ALLOW_OVERDUE_AUTO_CONFIRM.getCode());
            if(financialContract == null) {
                return jsonViewResolver.errorJsonResult("信托合同不存在！");
            }
            FinancialContractDetailModel rtnfinancialContract = new FinancialContractDetailModel(financialContract);
            Map<String,Object> data = new HashMap<String,Object>();
            data.put("subAccounts", getAccountsBriefInfo(financialContract.getSubAccountUuidList()));
            data.put("appAccounts", getAccountsBriefInfo(financialContract.getAppAccountUuidList()));
            data.put("financialContract", rtnfinancialContract);
            data.put("allowModifyRemittanceApplication", allowModifyRemittanceApplicationContent);
			data.put("allowNotOverdueAutoConfirm", allowNotOverdueAutoConfirm);
			data.put("allowOverdueAutoConfirm", allowOverdueAutoConfirm);
			data.put("planRepaymentTimeLock", planRepaymentTimeLock== null ? "空":planRepaymentTimeLock.getChineseMessage());
			Map<String, Object> paramFromCitigroup = getBankSavingLoanFromCitigroup(financialContractUuid, request);
			 //填充放款限额信息
			data.putAll(paramFromCitigroup);
            return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect);
        } catch (Exception e) {
            logger.error("#showFinancailContractDetailData occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("获取信托合同详情页数据失败！！！");
        }
    }

    private List<Map<String, Object>> getAccountsBriefInfo(List<String> uuids) {
        if (CollectionUtils.isEmpty(uuids)){
            return Collections.emptyList();
        }
        List<Map<String, Object>> accounts = new ArrayList<Map<String, Object>>();
        for (String uuid : uuids) {
            Account account = accountService.getAccountBy(uuid);
            accounts.add(account.getBriefInfo());
        }
        return accounts;
    }

    // 修改信托合同页面 --基础信息 -- 数据获取
    @RequestMapping(value = "/edit-basicInfo/data", method = RequestMethod.GET)
    @MenuSetting("submenu-financiael-contract")
    public @ResponseBody String editBasicInfoData(@RequestParam(value="financialContractUuid") String financialContractUuid) {
        try {
            FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
            if(financialContract == null) {
                return jsonViewResolver.errorJsonResult("信托合同不存在！");
            }
            Map<String, Object> rtnFinancialContract = financialContract.extract_financial_contract_basic_info();
            rtnFinancialContract.put("subAccounts", getAccountsBriefInfo(financialContract.getSubAccountUuidList()));
            return jsonViewResolver.sucJsonResult(rtnFinancialContract, SerializerFeature.DisableCircularReferenceDetect);
        } catch (Exception e) {
            logger.error("#editBasicInfoData occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("获取基础信息失败！！！");
        }
    }

    // 修改信托合同 --基础信息 -- 数据提交
    @RequestMapping(value = "/edit-financialContractBasicInfo/{financialContractUuid}", method = RequestMethod.POST)
    @MenuSetting("submenu-financiael-contract")
    public @ResponseBody String editFinancialContractBasicInfo(
            @PathVariable(value = "financialContractUuid") String financialContractUuid,
            @RequestBody String modifyFinancialContractBasicInfoModelJson,
            @Secure Principal principal, HttpServletRequest request) {
        try {
            ModifyFinancialContractBasicInfoModel modifyFinancialContractBasicInfoModel = JsonUtils.parse(modifyFinancialContractBasicInfoModelJson, ModifyFinancialContractBasicInfoModel.class);
            FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
            if(financialContract == null) {
                return jsonViewResolver.errorJsonResult("信托合同不存在！");
            }
            FinancialContractLog oldFinancialContractLog = new FinancialContractLog(financialContract);
            transferAppIdToNames(oldFinancialContractLog);
            FinancialContract newFinancialContract = financialContractHandler.modifyFinancialContractBasicInfo(modifyFinancialContractBasicInfoModel,financialContract);
            FinancialContractLog editFinancialContractLog = new FinancialContractLog(newFinancialContract);
            transferAppIdToNames(editFinancialContractLog);
            SystemOperateLogRequestParam param = getSystemOperateLogRequestParam(principal, request, oldFinancialContractLog,editFinancialContractLog);
            systemOperateLogHandler.generateSystemOperateLog(param);
            return jsonViewResolver.sucJsonResult();
        } catch (ModifyException e) {
            logger.error("##editFinancialContractBasicInfo## occur error!");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult(e.getMessage());
        }catch (Exception e) {
            logger.error("##editFinancialContractBasicInfo## occur error!");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("修改信托合同基础信息失败！！！");
        }
    }

    // 修改信托合同页面 --放款信息--数据获取
    @RequestMapping(value = "/edit-remittanceInfo/data", method = RequestMethod.GET)
    @MenuSetting("submenu-financiael-contract")
    public @ResponseBody String editRemittanceInfoData( HttpServletRequest request,@RequestParam(value="financialContractUuid") String financialContractUuid) {
        try {
            FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
            if(financialContract == null) {
                return jsonViewResolver.errorJsonResult("信托合同不存在！");
            }
            String allowModifyRemittanceApplicationContent = financialContractConfigurationService.getFinancialContractConfigContentContent(financialContractUuid,FinancialContractConfigurationCode.ALLOW_MODIFY_REMITTANCE_APPLICATION.getCode());
            Map<String, Object> paramFromCitigroup = getBankSavingLoanFromCitigroup(financialContractUuid, request);
            Map<String, Object> rtnFinancialContract = extract_financial_contract_remittance_info(financialContract, allowModifyRemittanceApplicationContent);
            //填充放款限额信息
            rtnFinancialContract.putAll(paramFromCitigroup);
            return jsonViewResolver.sucJsonResult(rtnFinancialContract, SerializerFeature.DisableCircularReferenceDetect);
        }catch (Exception e) {
            logger.error("#editRemittanceInfoData occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("获取放款信息失败！！！");
        }
    }
    
    @SuppressWarnings({ "unchecked", "serial" })
	private Map<String, Object> getBankSavingLoanFromCitigroup(String financialContractUuid, HttpServletRequest request){
    	
    	String merId = request.getHeader(ApiConstant.PARAMS_MER_ID);
		String secret = request.getHeader(ApiConstant.PARAMS_SECRET);
		
		Map<String, String> params = new HashMap<String,String>(){{
			put(ZhonghangResponseMapSpec.FINANCIAL_CONTRACT_UUID,financialContractUuid);
		}};
		
		Map<String, String> headerParams = new HashMap<String,String>(){{
			put("merId", merId);
			put("secret", secret);
		}};
    	
    	Result result = HttpClientUtils.executePostRequest(urlToCitigroupForGetBankSavingLoan, params, headerParams);
    	
    	Map<String, Object> data = result.getData();
    	
    	return null==data?new HashMap<>():getBankSavingLoanFromCitigroupInMap(JsonUtils.parse((String)data.get(HttpClientUtils.DATA_RESPONSE_PACKET)));
    	
    }
    
    @SuppressWarnings("unchecked")
	private Map<String, Object> getBankSavingLoanFromCitigroupInMap(Map<String, Object> responseData){
    	
    	return null==responseData?new HashMap<>():(Map<String, Object>)responseData.get("data");
    	
    }
    
    private Map<String, Object> extract_financial_contract_remittance_info(FinancialContract financialContract, String allowModifyRemittanceApplicationContent) {
        Map<String, Object> rtnFinancialContract = financialContract.extract_brief();
        List<String> uuids = financialContract.getAppAccountUuidList();
        rtnFinancialContract.put("allowModifyRemittanceApplication", Integer.parseInt(allowModifyRemittanceApplicationContent != null ? allowModifyRemittanceApplicationContent : "0"));
        rtnFinancialContract.put("appAccounts", getAccountsBriefInfo(uuids));
        return rtnFinancialContract;
    }

    // 修改信托合同 --放款信息--数据提交
    @RequestMapping(value = "/edit-financialContractRemittanceInfo/{financialContractUuid}", method = RequestMethod.POST)
    @MenuSetting("submenu-financiael-contract")
    public @ResponseBody String editFinancialContractRemittanceInfo(
            @PathVariable(value = "financialContractUuid") String financialContractUuid,
            @RequestBody String modifyFinancialContractRemittanceInfoModelJson,
            @Secure Principal principal, HttpServletRequest request) {
        try {
            ModifyFinancialContractRemittanceInfoModel modifyFinancialContractRemittanceInfoModel = JsonUtils.parse(modifyFinancialContractRemittanceInfoModelJson, ModifyFinancialContractRemittanceInfoModel.class);
            FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);

            if(modifyFinancialContractRemittanceInfoModel == null) {
                return jsonViewResolver.errorJsonResult("参数错误！");
            }
            if(financialContract == null) {
                return jsonViewResolver.errorJsonResult("信托合同不存在！");
            }

            String content = financialContractConfigurationService.getFinancialContractConfigContentContent(financialContractUuid, FinancialContractConfigurationCode.ALLOW_MODIFY_REMITTANCE_APPLICATION.getCode());
            FinancialContractLog oldFinancialContractLog = new FinancialContractLog(financialContract);
            if (content != null) {
                oldFinancialContractLog.setAllowModifyRemittanceApplication(Integer.valueOf(content));
            }

            FinancialContract newFinancialContract = financialContractHandler.modifyFinancialContractRemittanceInfo(modifyFinancialContractRemittanceInfoModel,financialContract);
            FinancialContractLog editFinancialContractLog = new FinancialContractLog(newFinancialContract);
            editFinancialContractLog.setAllowModifyRemittanceApplication(modifyFinancialContractRemittanceInfoModel.getAllowModifyRemittanceApplication());
            //更新信托额度
            financialContractHandler.sendRequestToCitiGroupForModifyBankSavingLoan(modifyFinancialContractRemittanceInfoModel.getBankSavingLoanAddAmount(), newFinancialContract);
            
            SystemOperateLogRequestParam param = getSystemOperateLogRequestParam(principal, request, oldFinancialContractLog,editFinancialContractLog);
            systemOperateLogHandler.generateSystemOperateLog(param);
            return jsonViewResolver.sucJsonResult();
        } catch (ModifyException e) {
            logger.error("##editFinancialContractRemittanceInfo## occur error!");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult(e.getMessage());
        } catch (Exception e) {
            logger.error("##editFinancialContractRemittanceInfo## occur error!");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("修改信托合同放款信息失败！！！");
        }
    }

    private void transferAppIdToNames(FinancialContractLog pLog){
        List<Long> appIds = JsonUtils.parseArray(pLog.getCapitalParty(), Long.class);
        pLog.setCapitalParty(transferAppIdToNames(appIds));

        List<Long> otherParties = JsonUtils.parseArray(pLog.getOtherParty(), Long.class);
        pLog.setOtherParty(transferAppIdToNames(otherParties));
    }

    private String transferAppIdToNames(List<Long> appIds){
        if(CollectionUtils.isEmpty(appIds)){
            return "";
        }
        StringBuilder appNames = new StringBuilder();
        for (Long appId: appIds){
            App aApp = appService.getAppById(appId);
            if (aApp != null){
                if(appNames.length() != 0){
                    appNames.append(",");
                }
                appNames.append(aApp.getName());
            }
        }
        return appNames.toString();
    }

    // 修改信托合同页面 --还款信息--数据获取
    @RequestMapping(value = "/edit-repaymentInfo/data", method = RequestMethod.GET)
    @MenuSetting("submenu-financial-contract")
    public @ResponseBody String editRepaymentInfoData(@RequestParam(value="financialContractUuid") String financialContractUuid) {
        try {
            FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
            if(financialContract == null) {
                return jsonViewResolver.errorJsonResult("信托合同不存在！");
            }
            Map<String, Object> rtnFinancialContract = financialContract.extract_financial_contract_repayment_info();
            Integer modifyFlag = financialContractHandler.getModifyFlag(financialContract);
			String allowNotOverdueAutoConfirm = financialContractConfigurationService.getFinancialContractConfigContentContent(financialContractUuid,FinancialContractConfigurationCode.ALLOW_NOT_OVERDUE_AUTO_CONFIRM.getCode());
			String allowOverdueAutoConfirm = financialContractConfigurationService.getFinancialContractConfigContentContent(financialContractUuid,FinancialContractConfigurationCode.ALLOW_OVERDUE_AUTO_CONFIRM.getCode());
			PlanRepaymentTimeConfiguration planRepaymentTimeLock = financialContractConfigurationService.getPlanRepaymentTimeConfiguration(financialContractUuid);
			Map<String, Integer> planRepaymentTimeMap = PlanRepaymentTimeConfiguration.parsePlanRepaymentTimeTypeToMap(planRepaymentTimeLock);
			rtnFinancialContract.put("modifyFlag", modifyFlag);
			rtnFinancialContract.put("allowNotOverdueAutoConfirm", Integer.parseInt(allowNotOverdueAutoConfirm == null ? "0" : allowNotOverdueAutoConfirm));
			rtnFinancialContract.put("allowOverdueAutoConfirm", allowOverdueAutoConfirm);
			rtnFinancialContract.put("planRepaymentTimeOnline", planRepaymentTimeMap.get(PlanRepaymentTimeConfiguration.ONLINE.getName()));
			rtnFinancialContract.put("planRepaymentTimeOffline", planRepaymentTimeMap.get(PlanRepaymentTimeConfiguration.OFFLINE.getName()));
            return jsonViewResolver.sucJsonResult(rtnFinancialContract, SerializerFeature.DisableCircularReferenceDetect);
        }catch (Exception e) {
            logger.error("#editRepaymentInfoData occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("获取还款信息失败！！！");
        }
    }

    // 修改信托合同 --还款信息--数据提交
    @RequestMapping(value = "/edit-financialContractRepaymentInfo/{financialContractUuid}", method = RequestMethod.POST)
    @MenuSetting("submenu-financial-contract")
    public @ResponseBody String editFinancialContractRepaymentInfo(
            @PathVariable(value = "financialContractUuid") String financialContractUuid,
            @RequestBody String modifyFinancialContractRepaymentInfoModelJson,
            @Secure Principal principal, HttpServletRequest request) {
        try {
            ModifyFinancialContractRepaymentInfoModel modifyFinancialContractRepaymentInfoModel = JsonUtils.parse(modifyFinancialContractRepaymentInfoModelJson, ModifyFinancialContractRepaymentInfoModel.class);
            FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
            if(financialContract == null) {
                return jsonViewResolver.errorJsonResult("信托合同不存在！");
            }
            FinancialContractLog oldFinancialContractLog = new FinancialContractLog(financialContract);
			String allowNotOverdueAutoConfirmContent = financialContractConfigurationService.getFinancialContractConfigContentContent(financialContractUuid, FinancialContractConfigurationCode.ALLOW_NOT_OVERDUE_AUTO_CONFIRM.getCode());
			if (allowNotOverdueAutoConfirmContent != null) {
				oldFinancialContractLog.setAllowNotOverdueAutoConfirm(Integer.valueOf(allowNotOverdueAutoConfirmContent));
			}
			String allowOverdueAutoConfirmContent = financialContractConfigurationService.getFinancialContractConfigContentContent(financialContractUuid, FinancialContractConfigurationCode.ALLOW_OVERDUE_AUTO_CONFIRM.getCode());
			if (StringUtils.isNotBlank(allowOverdueAutoConfirmContent)) {
				oldFinancialContractLog.setAllowOverdueAutoConfirm(Integer.valueOf(allowOverdueAutoConfirmContent));
			}
			PlanRepaymentTimeConfiguration planRepaymentTimeLock = financialContractConfigurationService.getPlanRepaymentTimeConfiguration(financialContractUuid);
			if(planRepaymentTimeLock != null){
				oldFinancialContractLog.setPlanRepaymentTimeLock(planRepaymentTimeLock.getName());
			}

            FinancialContract newFinancialContract = financialContractHandler.modifyFinancialContractRepaymentInfo(modifyFinancialContractRepaymentInfoModel,financialContract);

            FinancialContractLog editFinancialContractLog = new FinancialContractLog(newFinancialContract);
			editFinancialContractLog.setAllowNotOverdueAutoConfirm(modifyFinancialContractRepaymentInfoModel.getAllowNotOverdueAutoConfirm());
			editFinancialContractLog.setAllowOverdueAutoConfirm(modifyFinancialContractRepaymentInfoModel.getAllowOverdueAutoConfirm());
            SystemOperateLogRequestParam param = getSystemOperateLogRequestParam(principal, request, oldFinancialContractLog,editFinancialContractLog);
            systemOperateLogHandler.generateSystemOperateLog(param);
            return jsonViewResolver.sucJsonResult();
        } catch (ModifyException e) {
            logger.error("##editFinancialContractRepaymentInfo## occur error!");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult(e.getMessage());
        }catch (Exception e) {
            logger.error("##editFinancialContractRepaymentInfo## occur error!");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("修改信托合同还款信息失败！！！");
        }

	}

	// 新增临时回购任务
	@Deprecated
	@RequestMapping(value = "/create-temporaryRepurchases/{financialContractUuid}", method = RequestMethod.POST)
	@MenuSetting("submenu-financiael-contract")
	public @ResponseBody String createTemporaryRepurchases(
			@PathVariable(value = "financialContractUuid") String financialContractUuid,
			@RequestBody String temporaryRepurchaseJson,
			@Secure Principal principal, HttpServletRequest request) {
		try {
			FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
			if(financialContract == null) {
				return jsonViewResolver.errorJsonResult("信托合同不存在！");
			}
			if(StringUtils.isBlank(temporaryRepurchaseJson)){
				return jsonViewResolver.errorJsonResult("参数错误！");
			}
			TemporaryRepurchaseJson tr = JsonUtils.parse(temporaryRepurchaseJson, TemporaryRepurchaseJson.class);
			String result = financialContractHandler.createTemporaryRepurchases(principal,request,tr,financialContract);
			if(StringUtils.isNotBlank(result)){
				return jsonViewResolver.errorJsonResult(result);
			}
			return jsonViewResolver.sucJsonResult();
		} catch (ModifyException e) {
			logger.error("##createTemporaryRepurchases## occur error!");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e.getMessage());
		}catch (Exception e) {
			logger.error("##createTemporaryRepurchases## occur error!");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("新增临时回购任务失败！！！");
		}

	}

	// 删除临时回购任务
	@Deprecated
	@RequestMapping(value = "/delete-temporaryRepurchases/{financialContractUuid}", method = RequestMethod.POST)
	@MenuSetting("submenu-financiael-contract")
	public @ResponseBody String deleteTemporaryRepurchases(
			@PathVariable(value = "financialContractUuid") String financialContractUuid,
			@RequestBody String temporaryRepurchaseJson,
			@Secure Principal principal, HttpServletRequest request) {
		try {
			FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
			if(financialContract == null) {
				return jsonViewResolver.errorJsonResult("信托合同不存在！");
			}
			if(StringUtils.isBlank(temporaryRepurchaseJson)){
				return jsonViewResolver.errorJsonResult("参数错误！");
			}
			TemporaryRepurchaseJson deleteTr = JsonUtils.parse(temporaryRepurchaseJson, TemporaryRepurchaseJson.class);
			String result = financialContractHandler.deleteTemporaryRepurchases(principal,request,deleteTr,financialContract);
			if(StringUtils.isNotBlank(result)){
				return jsonViewResolver.errorJsonResult(result);
			}
			return jsonViewResolver.sucJsonResult();
		} catch (ModifyException e) {
			logger.error("##deleteTemporaryRepurchases## occur error!");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e.getMessage());
		}catch (Exception e) {
			logger.error("##deleteTemporaryRepurchases## occur error!");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("删除临时回购任务失败！！！");
		}

	}

	// 修改临时回购任务
	@Deprecated
	@RequestMapping(value = "/edit-temporaryRepurchases/{financialContractUuid}", method = RequestMethod.POST)
	@MenuSetting("submenu-financiael-contract")
	public @ResponseBody String editTemporaryRepurchases(
			@PathVariable(value = "financialContractUuid") String financialContractUuid,
			@RequestBody String temporaryRepurchaseJson,
			@Secure Principal principal, HttpServletRequest request) {
		try {
			FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
			if(financialContract == null) {
				return jsonViewResolver.errorJsonResult("信托合同不存在！");
			}
			if(StringUtils.isBlank(temporaryRepurchaseJson)){
				return jsonViewResolver.errorJsonResult("参数错误！");
			}
			TemporaryRepurchaseJson editTr = JsonUtils.parse(temporaryRepurchaseJson, TemporaryRepurchaseJson.class);
			String result = financialContractHandler.editTemporaryRepurchases(principal,request,editTr,financialContract);
			if(StringUtils.isNotBlank(result)){
				return jsonViewResolver.errorJsonResult(result);
			}
			return jsonViewResolver.sucJsonResult();
		} catch (ModifyException e) {
			logger.error("##editTemporaryRepurchases## occur error!");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e.getMessage());
		}catch (Exception e) {
			logger.error("##editTemporaryRepurchases## occur error!");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("修改临时回购任务失败！！！");
		}

	}

    private SystemOperateLogRequestParam getSystemOperateLogrequestParam(Principal principal,HttpServletRequest request, FinancialContract financialContract) {
        return new SystemOperateLogRequestParam(
                principal.getId(), IpUtil.getIpAddress(request),
                financialContract.getContractNo(),
                LogFunctionType.ADDFINANCIALCONTRACT, LogOperateType.ADD,
                FinancialContract.class, financialContract, null, null);
    }

    // 获取罚息输入历史
    @RequestMapping(value = "/edit-financialContractRepaymentInfo/penaltyHistories", method = RequestMethod.GET)
    @MenuSetting("submenu-financiael-contract")
    public @ResponseBody String getPenaltyHistory(
            @Secure Principal principal, @RequestParam(value = "relatedUuid" ,required = false) String relatedUuid,
            @RequestParam(value = "whatFor", defaultValue = "-1") int whatFor, Page page){
        try{
            Long creatorId = principal.getId();
            List<Map<String, Object>> inputHistories = inputHistoryService.getInputContentsWithUuid(creatorId, relatedUuid, whatFor, page);
            return jsonViewResolver.sucJsonResult("list", inputHistories, SerializerFeature.DisableCircularReferenceDetect);
        }catch (Exception e) {
            logger.error("##getPenaltyHistory## occur error!");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    // 新增罚息输入历史
    @RequestMapping(value = "/edit-financialContractRepaymentInfo/penaltyHistories/add", method = RequestMethod.POST)
    @MenuSetting("submenu-financiael-contract")
    public @ResponseBody String addPenaltyHistory(
            @Secure Principal principal, @RequestParam(value = "relatedUuid", required = false) String relatedUuid,  @RequestParam(value = "content") String content) {
        try{
            inputHistoryHandler.createInputHistory(content, principal.getName(),principal.getId(), relatedUuid, InputHistory.PENALTY);
            return jsonViewResolver.sucJsonResult();
        }catch (Exception e) {
            logger.error("##addPenaltyHistory## occur error!");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    // 修改罚息输入历史
    @RequestMapping(value = "/edit-financialContractRepaymentInfo/penaltyHistories/edit", method = RequestMethod.POST)
    @MenuSetting("submenu-financiael-contract")
    public @ResponseBody String editPenaltyHistory(@RequestParam(value = "uuid") String uuid,  @RequestParam(value = "content") String content) {
        try{
            boolean isSuc = inputHistoryHandler.editInputHistory(uuid, content);
            return jsonViewResolver.jsonResult(isSuc);
        }catch (Exception e) {
            logger.error("##editPenaltyHistory## occur error!");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    // 删除输入历史记录
    @RequestMapping(value = "/edit-financialContractRepaymentInfo/penaltyHistories/del", method = RequestMethod.GET)
    @MenuSetting("submenu-financiael-contract")
    public @ResponseBody String delPenaltyHistory( @Secure Principal principal, @RequestParam(value = "uuid") String uuid){
        try{
            InputHistory inputHistory = inputHistoryService.getEffectiveInputHistoryBy(uuid);
            if(inputHistory == null)
                return jsonViewResolver.errorJsonResult("该记录不存在");
            boolean isuc = inputHistoryService.deleteInputHistory(inputHistory);
            return jsonViewResolver.jsonResult(isuc);
        }catch (Exception e) {
            logger.error("##getPenaltyHistory## occur error!");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    private SystemOperateLogRequestParam getSystemOperateLogRequestParam(Principal principal, HttpServletRequest request, FinancialContractLog oldFinancialContractLog, FinancialContractLog editFinancialContractLog) {
        return new SystemOperateLogRequestParam(
                principal.getId(), IpUtil.getIpAddress(request),
                editFinancialContractLog.getContractNo(),
                LogFunctionType.EDITFINANCIALCONTRACT, LogOperateType.UPDATE,
                FinancialContractLog.class, oldFinancialContractLog,
                editFinancialContractLog, null);
    }

	@RequestMapping(value = "/{financialContractId}/contracts", method = RequestMethod.GET)
	@MenuSetting("submenu-financial-contract")
	public @ResponseBody String getContractsInFinancialContractDetail(@PathVariable Long financialContractId,
			@ModelAttribute CreateFinancialContractModel createFinancialContractModel, Page page) {
		try {
			FinancialContract financialContract = financialContractService.getFinancialContractById(financialContractId);

			List<Contract> contractList = contractService.getContractsByFinancialContract(financialContract, page);
			long contractsSize = contractService.countContractsByFinancialContract(financialContract);

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("list", contractList);
			data.put("size", contractsSize);

			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			logger.error("#getContractsInFinancialContractDetail occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(GlobalCodeSpec.CODE_FAILURE);
		}
	}
}
