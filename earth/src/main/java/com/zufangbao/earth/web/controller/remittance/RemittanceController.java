package com.zufangbao.earth.web.controller.remittance;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.handler.RemittanceProxyHandler;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.yunxin.handler.remittance.RemittancetPlanHandler;
import com.zufangbao.gluon.exception.CommonException;
import com.zufangbao.gluon.spec.global.GlobalCodeSpec;
import com.zufangbao.gluon.util.BeanWrapperUtil;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.entity.remittance.ExecutionStatus;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplication;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplicationQueryModel;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplicationShowModel;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlan;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlanExecLog;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlanQueryModel;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlanShowModel;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittancePlanExecLogService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittancePlanService;
import com.zufangbao.wellsfargo.yunxin.handler.RemittancetApplicationHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/remittance")
@MenuSetting("menu-data")
public class RemittanceController extends BaseController{
	
	@Autowired
	private IRemittancePlanService iRemittancePlanService;
	
	@Autowired
	private IRemittancePlanExecLogService remittancePlanExecLogService;
	
	@Autowired
	private IRemittanceApplicationService iRemittanceApplicationService;
	
	@Autowired
	private RemittancetPlanHandler remittancetPlanHandler;
	
	@Autowired
	private RemittancetApplicationHandler remittancetApplicationHandler;
	
	@Autowired
	private SystemOperateLogService systemOperateLogService;
	
	@Autowired
	private PrincipalHandler principalHandler;

	@Autowired
	public FinancialContractService financialContractService;

	@Autowired
	private RemittanceProxyHandler remittanceProxyHandler;

	private static final Log logger = LogFactory.getLog(RemittanceController.class);


	// 计划订单---列表查询选项配置
	@RequestMapping(value="/application/options")
	public @ResponseBody String getRemittanceApplicationListOptions(@Secure Principal principal) {
		try {
			Map<String, Object> result = new HashMap<String, Object>();

			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

			result.put("queryAppModels", queryAppModels);
			result.put("orderStatus", EnumUtil.getKVList(ExecutionStatus.class));
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			logger.error("#getRemittanceApplicationListOptions#  occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("列表选项获取错误");
		}
	}
	
	// 计划订单---列表查询
	@RequestMapping(value = "/application/query")
	@MenuSetting("submenu-remittance-application")
	public @ResponseBody String queryRemittanceApplication(
			@ModelAttribute RemittanceApplicationQueryModel remittanceApplicationQueryModel,
			Page page) {
		try {
			long start_time = System.currentTimeMillis();
			List<RemittanceApplicationShowModel> showModels = remittancetApplicationHandler.queryShowModelList(remittanceApplicationQueryModel, page);
			long end_time = System.currentTimeMillis();
			logger.info("{queryRemittanceApplication}-[queryShowModelList] " + JsonUtils.toJSONString(remittanceApplicationQueryModel)+ " use " + (end_time - start_time) + "ms.");

			start_time = System.currentTimeMillis();
			int total = iRemittanceApplicationService.queryRemittanceApplicationCount(remittanceApplicationQueryModel);
			end_time = System.currentTimeMillis();
			logger.info("{queryRemittanceApplication}-[queryRemittanceApplicationCount] " + JsonUtils.toJSONString(remittanceApplicationQueryModel) + " use " +
					(end_time - start_time) + "ms.");

			Map<String, Object> data = new HashMap<>();
			data.put("remittanceApplicationQueryModel",remittanceApplicationQueryModel);
			data.put("list", showModels);
			data.put("size", total);
			return jsonViewResolver.sucJsonResult(data,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			logger.error("#queryRemittanceApplication#  occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("查询错误");
		}
	}
	
	// 计划订单---统计金额(计划放款金额，实际放款金额，差值（计划放款-实际放款）)
	@RequestMapping(value = "/application/amountStatistics")
	@MenuSetting("submenu-remittance-application")
	public @ResponseBody String applicationAmountStatistics(
			@ModelAttribute RemittanceApplicationQueryModel remittanceApplicationQueryModel) {
		try {
			Map<String, Object> data = iRemittanceApplicationService.amountStatistics(remittanceApplicationQueryModel);
			return jsonViewResolver.sucJsonResult(data,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			logger.error("#applicationAmountStatistics#  occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("放款相关金额统计错误");
		}
	}
	
	// 计划订单---详情页面数据
	@RequestMapping(value="/application/details/{remittanceApplicationUuid}")
	public @ResponseBody String showRemittanceApplicationDetails(
			@PathVariable(value="remittanceApplicationUuid") String remittanceApplicationUuid){
		try {
			Map<String, Object> result = new HashMap<String, Object>();			
			RemittanceApplication remittanceApplication = iRemittanceApplicationService.getUniqueRemittanceApplicationByUuid(remittanceApplicationUuid);
			List<RemittancePlan> remittancePlans = iRemittancePlanService.getRemittancePlanListBy(remittanceApplicationUuid);
			result.put("remittanceApplication", remittanceApplication);
			result.put("remittancePlans", remittancePlans);
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			logger.error("#showRemittanceApplicationDetails# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("详情页基本信息获取错误");
		}
	}
	
	// 计划订单---详情页面重新回调结果
	@RequestMapping(value="/application/details/updateplannotifynumber/{remittanceApplicationUuid}")
	@MenuSetting("submenu-remittance-application")
	public @ResponseBody String updatePlanNotifyNumber(@PathVariable(value="remittanceApplicationUuid") String remittanceApplicationUuid, @Secure Principal principal, HttpServletRequest request, @RequestParam("comment") String comment){
		try{
			boolean isSuc = iRemittanceApplicationService.addPlanNotifyNumber(remittanceApplicationUuid, 1);
			recordSystemOperateLogForNotifyRemittance(principal, request, remittanceApplicationUuid, comment);
			return jsonViewResolver.jsonResult(isSuc);
		} catch (Exception e){
			logger.error("#updatePlanNotifyNumber# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("发生错误，请稍后重试");
		}
	}
	
	private void recordSystemOperateLogForNotifyRemittance(Principal principal, HttpServletRequest request, String remittanceApplicationUuid, String comment) {
		if(StringUtils.isEmpty(remittanceApplicationUuid)) {
			return;
		}
		SystemOperateLog log = new SystemOperateLog();
		log.setUserId(principal.getId());
		log.setIp(IpUtil.getIpAddress(request));
		log.setObjectUuid(remittanceApplicationUuid);
		log.setLogFunctionType(LogFunctionType.ADDCOMMENT);
		log.setLogOperateType(LogOperateType.UPDATE);
		log.setKeyContent(remittanceApplicationUuid);
		log.setRecordContent("重新回调结果: 回调原因为【"+comment+"】");
		log.setOccurTime(new Date());
		systemOperateLogService.save(log);
	}

	// 放款单---列表选项
	@RequestMapping(value = "/plan/options")
	public @ResponseBody String getRemittancePlanListOptions(@Secure Principal principal) {
		try {
			Map<String, Object> result = new HashMap<String, Object>();

			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

			result.put("queryAppModels", queryAppModels);
			result.put("executionStatus", EnumUtil.getKVList(ExecutionStatus.class));
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			logger.error("#getRemittancePlanListOptions# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("列表选项获取错误");
		}
	}
	
	// 放款单---列表查询
	@RequestMapping(value= "/plan/query")
	@MenuSetting("submenu-remittance-plan")
	public @ResponseBody String queryRemittancePlan(@ModelAttribute RemittancePlanQueryModel queryModel, Page page){
		try{
			long start_time = System.currentTimeMillis();
			List<RemittancePlanShowModel> showModels = remittancetPlanHandler.queryShowModelList(queryModel, page);
			long end_time = System.currentTimeMillis();
			logger.info("{queryRemittancePlan}-[queryShowModelList] " + JsonUtils.toJSONString(queryModel)+ " use " + (end_time - start_time) + "ms.");

			start_time = System.currentTimeMillis();
			int total = iRemittancePlanService.queryRemittancePlanCount(queryModel);
			end_time = System.currentTimeMillis();
			logger.info("{queryRemittancePlan}-[queryShowModelList] " + JsonUtils.toJSONString(queryModel)+ " use " + (end_time - start_time) + "ms.");

			Map<String, Object> data = new HashMap<>();
			data.put("list", showModels);
			data.put("size", total);
			return jsonViewResolver.sucJsonResult(data,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
		}catch (Exception e){
			logger.error("#queryRemittancePlan# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("查询错误");
		}
	}
	
	// 放款单---统计金额(执行金额)
	@RequestMapping(value = "/plan/amountStatistics")
	@MenuSetting("submenu-remittance-application")
	public @ResponseBody String planAmountStatistics(
			@ModelAttribute RemittancePlanQueryModel remittancePlanQueryModel) {
		try {
			Map<String, Object> data = iRemittancePlanService.amountStatistics(remittancePlanQueryModel);
			return jsonViewResolver.sucJsonResult(data,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			logger.error("#planAmountStatistics#  occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("执行金额统计错误");
		}
	}

	// 放款单---详情页面数据
	@RequestMapping(value="/plan/details/{remittancePlanUuid}")
	public @ResponseBody String showRemittancePlanDetails(
			@PathVariable(value="remittancePlanUuid") String remittancePlanUuid){
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			RemittancePlan remittancePlan = iRemittancePlanService.getUniqueRemittancePlanByUuid(remittancePlanUuid);
			String remittanceApplicationUuid = remittancePlan.getRemittanceApplicationUuid();
			RemittanceApplication remittanceApplication = iRemittanceApplicationService.getUniqueRemittanceApplicationByUuid(remittanceApplicationUuid);
			Map<String, Object>rtnRemittanceApplication = BeanWrapperUtil.wrapperMap(remittanceApplication, "auditorName", "auditTime", "executionStatus");
			List<RemittancePlanExecLog> remittancePlanExecLogs = remittancePlanExecLogService.getRemittancePlanExecLogListBy(remittancePlanUuid);
			result.put("remittancePlan", remittancePlan);
			result.put("remittanceApplication", rtnRemittanceApplication);
			result.put("remittancePlanExecLogs", remittancePlanExecLogs);
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			logger.error("#showRemittancePlanDetails# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("详情页基本信息获取错误");
		}
	}

	// 放款单---详情页面---失败再放款
	@RequestMapping(value= "/plan/resend", method = RequestMethod.GET)
	@MenuSetting("submenu-remittance-plan")
	public @ResponseBody String reRemittanceForFailedPlan(
			@RequestParam(value="remittancePlanUuid", required = true) String remittancePlanUuid,
			@RequestParam(value="comment") String comment,
			@Secure Principal principal,
			HttpServletRequest request) {
		try {
			if(!validRemittancePlanAmount(remittancePlanUuid)){
				return jsonViewResolver.errorJsonResult("放款金额错误");
			}

			return remittanceProxyHandler.processingRevokeRemittancePlan(principal, request, remittancePlanUuid, comment);

		} catch (CommonException e) {
			logger.error("#reRemittanceForFailedPlan# occur error.");
			return jsonViewResolver.errorJsonResult(e.getErrorMsg());
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(GlobalCodeSpec.CODE_FAILURE);
		}
	}

	// 放款金额<=计划放款金额-放款成功金额-放款中金额，若大于则放款失败；
		private boolean validRemittancePlanAmount(String remittancePlanUuid) throws CommonException{
			RemittancePlan remittancePlan = iRemittancePlanService.getUniqueRemittancePlanByUuid(remittancePlanUuid);
			if(remittancePlan == null){
				throw new CommonException(4, "放款单不存在");
			}
			String remittanceApplicationUuid = remittancePlan.getRemittanceApplicationUuid();
			RemittanceApplication application = iRemittanceApplicationService.getUniqueRemittanceApplicationByUuid(remittanceApplicationUuid);
			if(application == null){
				throw new CommonException(4, "对应的计划订单不存在");
			}
			List<RemittancePlan> remittancePlans = iRemittancePlanService.getRemittancePlanListBy(remittanceApplicationUuid);
			BigDecimal allowMaxRemittanceAmount = application.getPlannedTotalAmount()
				.subtract(getAmountInSuccess(remittancePlans)).subtract(getAmountInFavour(remittancePlans));
	        return allowMaxRemittanceAmount.compareTo(remittancePlan.getPlannedTotalAmount()) >= 0;
	    }

		// 放款中金额
		private BigDecimal getAmountInFavour(List<RemittancePlan> remittancePlans){
			if(CollectionUtils.isEmpty(remittancePlans)){
				return BigDecimal.ZERO;
			}
			return remittancePlans.stream()
				.filter(a-> Arrays.asList(ExecutionStatus.CREATE, ExecutionStatus.PROCESSING, ExecutionStatus.ABNORMAL).contains(a.getExecutionStatus()))
				.map(RemittancePlan::getPlannedTotalAmount)
				.reduce(BigDecimal.ZERO,BigDecimal::add);
		}

		// 放款成功金额
		private BigDecimal getAmountInSuccess(List<RemittancePlan> remittancePlans) {
			if (CollectionUtils.isEmpty(remittancePlans)) {
				return BigDecimal.ZERO;
			}
			return remittancePlans.stream()
				.filter(a -> Collections.singletonList(ExecutionStatus.SUCCESS).contains(a.getExecutionStatus()))
				.map(RemittancePlan::getPlannedTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

		}

		// 放款回调给外部
				@RequestMapping(value="/application/notifyOutlier")
				public @ResponseBody String notifyOutlier(@Secure Principal principal ,@RequestParam(value = "remittanceApplicationUuid") String remittanceApplicationUuid) {
					try {
						return remittanceProxyHandler.notifyOutlier(remittanceApplicationUuid);
					} catch (Exception e) {
						e.printStackTrace();
						return jsonViewResolver.errorJsonResult("回调外部异常");
					}
				}
}
