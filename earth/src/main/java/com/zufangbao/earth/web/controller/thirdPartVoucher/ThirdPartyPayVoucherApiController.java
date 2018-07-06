	package com.zufangbao.earth.web.controller.thirdPartVoucher;

import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.yunxin.api.handler.ThirdPartVoucherCommandHandler;
import com.zufangbao.earth.yunxin.api.handler.ThirdPartyVoucherBatchHandler;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.handler.ThirdPartyVoucherHandler;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.PaymentChannelInformationService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.barclays.BusinessProcessStatus;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.entity.model.ThirdPartyVoucherBatchQueryModel;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.MatchStatus;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.VerificationStatus;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.VoucherLogIssueStatus;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.ThirdPartyPaymentVoucherHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.ThirdPartyPaymentVoucherService;
import com.zufangbao.wellsfargo.thirdpartvoucher.model.ThirdPartVoucherBatchShowModel;
import com.zufangbao.wellsfargo.thirdpartvoucher.model.ThirdPartyPayAuditResultQueryModel;
import com.zufangbao.wellsfargo.thirdpartvoucher.model.ThirdPartyPayAuditResultShowModel;
import com.zufangbao.wellsfargo.yunxin.handler.ThirdPartyPayAuditResultHandler;
import com.zufangbao.wellsfargo.yunxin.handler.ThirdPartyVoucherCommandLogHandler;
import com.zufangbao.wellsfargo.yunxin.model.ThirdPartyVoucherCommandLogQueryModel;
import com.zufangbao.wellsfargo.yunxin.model.ThirdPartyVoucherCommandLogShowModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("voucher/thirdPartyPayApi")
@MenuSetting("menu-capital")
public class ThirdPartyPayVoucherApiController extends BaseController {

	@Autowired
	private ThirdPartyVoucherCommandLogHandler thirdPartyVoucherCommandLogHandler;

	@Autowired
	private ThirdPartyVoucherBatchHandler thirdPartVoucherBatchHandler;

	@Autowired
	private ThirdPartyPayAuditResultHandler thirdPartyTransactionRecordHandler;
	
	@Autowired
	private PrincipalHandler principalHandler;

	@Autowired
	private ThirdPartVoucherCommandHandler thirdPartVoucherCommandHandler;

	@Autowired
	private ThirdPartyVoucherHandler thirdPartyVoucherHandler;
	@Autowired
	private FinancialContractService financialContractService;
	
	@Autowired
	private ThirdPartyPaymentVoucherHandler thirdPartyPaymentVoucherHandler;
	@Autowired
	private ThirdPartyPaymentVoucherService thirdPartyPaymentVoucherService;
	@Autowired
	private PaymentChannelInformationService paymentChannelInformationService;
     

	private static final Log logger = LogFactory.getLog(ThirdPartyPayVoucherApiController.class);

	@RequestMapping("")
	public @ResponseBody ModelAndView loadAll(@Secure Principal principal) {
		ModelAndView modelAndView = new ModelAndView("index");
		return modelAndView;
	}
	
	//历史第三方支付凭证列表
	@RequestMapping(value = "/list")
	public @ResponseBody String query(@ModelAttribute ThirdPartyVoucherCommandLogQueryModel queryModel,
			@Secure Principal principal, Page page, HttpServletRequest request) {
		try {
			List<ThirdPartyVoucherCommandLogShowModel> showModels = thirdPartyVoucherCommandLogHandler
					.getThirdPartyVoucherCommandLogShowModelList(queryModel, page);
			int size = thirdPartyVoucherCommandLogHandler.countThirdPartyVoucherCommandLogSize(queryModel);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("list", showModels);
			result.put("size", size);
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			logger.error("#ThirdPartyPayVoucherApiController#query query thirdPartyPayApi list error ");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(QUERY_ERROR);
		}
	}
	
	//历史第三方支付凭证详情页
	@RequestMapping(value = "/detail/{voucherUuid}")
	public @ResponseBody String detail(@PathVariable String voucherUuid) {
		try {
			ThirdPartyVoucherCommandLogShowModel showModel = thirdPartyVoucherCommandLogHandler
					.getThirdPartyVoucherCommandLogShowModel(voucherUuid);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("showModel", showModel);
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			logger.error("#ThirdPartyPayVoucherApiController#detail#query thirdPartyPayApi detail error ");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(QUERY_ERROR);
		}
	}
	
	/**
	 * 新第三方支付凭证列表
	 * @param queryModel
	 * @param principal
	 * @param page
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/paymentVoucherList")
	public @ResponseBody String queryPaymentVoucherList(@ModelAttribute ThirdPartyVoucherCommandLogQueryModel queryModel,
			@Secure Principal principal, Page page, HttpServletRequest request) {
		try {
			List<ThirdPartyVoucherCommandLogShowModel> showModels = thirdPartyPaymentVoucherHandler.getShowModelList(queryModel, page);
					
			int size = thirdPartyPaymentVoucherService.countThirdPartyPaymentVoucherSize(queryModel);
			
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("list", showModels);
			result.put("size", size);
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			logger.error("#ThirdPartyPayVoucherApiController#query paymentVoucherList list error ");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(QUERY_ERROR);
		}
	}
	
	/**
	 * 新第三方支付凭证详情页
	 * @param voucherUuid
	 * @return
	 */
	@RequestMapping(value = "/paymentVoucherDetail/{voucherUuid}")
	public @ResponseBody String paymentVoucherDetail(@PathVariable String voucherUuid) {
		try {
			ThirdPartyVoucherCommandLogShowModel showModel = thirdPartyPaymentVoucherHandler.getThirdPartyVoucherCommandLogShowModel(voucherUuid);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("showModel", showModel);
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			logger.error("#ThirdPartyPayVoucherApiController#detail#query paymentVoucherDetail error ");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(QUERY_ERROR);
		}
	}
	

	@RequestMapping(value = "/options")
	public @ResponseBody String options(@Secure Principal principal) {
		try {
			List<FinancialContract> financialContracts = principalHandler
					.get_can_access_financialContract_list(principal);

			Map<String, Object> result = new HashMap<String, Object>();
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);
			result.put("queryAppModels", queryAppModels);
			result.put("paymentInstitutionNameList", EnumUtil.getKVList(paymentChannelInformationService.getAllPaymentInstitutionNames()));
//			result.put("paymentInstitutionNameList", EnumUtil.getKVList(PaymentInstitutionName.class));
			result.put("verificationStatusList", EnumUtil.getKVList(VerificationStatus.class));
			result.put("voucherLogIssueStatusList", EnumUtil.getKVList(VoucherLogIssueStatus.class));
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			logger.error("#ThirdPartyPayVoucherApiController#options#query thirdPartyPayApi options error ");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(QUERY_ERROR);
		}

	}

	@RequestMapping(value = "/list-batchs")
	public @ResponseBody String queryBatchs(
			@ModelAttribute ThirdPartyVoucherBatchQueryModel thirdPartVoucherCommandLogQueryModel,
			@Secure Principal principal, Page page, HttpServletRequest request) {
		try {
			List<ThirdPartVoucherBatchShowModel> list = thirdPartVoucherBatchHandler
					.getThirdPartVoucherBatchShowModelList(thirdPartVoucherCommandLogQueryModel, page);
			int size = thirdPartVoucherBatchHandler.countThirdPartVoucherBatchNum(thirdPartVoucherCommandLogQueryModel);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("list", list);
			result.put("size", size);
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			logger.error("#ThirdPartyPayVoucherApiController#queryBatchs#query batchs list error ");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(QUERY_ERROR);
		}

	}

	@RequestMapping(value = "/detail-batchs/{batchUuid}")
	public @ResponseBody String detailBatch(@PathVariable String batchUuid) {
		try {
			ThirdPartVoucherBatchShowModel thirdPartVoucherBatchShowModel = thirdPartVoucherBatchHandler
					.getThirdPartVoucherCommandLogShowModel(batchUuid);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("batchInfo", thirdPartVoucherBatchShowModel);
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			logger.error("#ThirdPartyPayVoucherApiController#detailBatch#query batchs detail error ");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(QUERY_ERROR);
		}

	}

	@RequestMapping(value = "/list-batchs/options")
	public @ResponseBody String optionsForbatchList(@Secure Principal principal) {
		try {
			List<FinancialContract> financialContracts = principalHandler
					.get_can_access_financialContract_list(principal);

			Map<String, Object> result = new HashMap<String, Object>();
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

			result.put("queryAppModels", queryAppModels);
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			logger.error("#ThirdPartyPayVoucherApiController#optionsForbatchList#list-batchs options error ");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(QUERY_ERROR);
		}

	}

	@RequestMapping(value = "/list-batchs/vouchers/{batchUuid}")
	public @ResponseBody String vouchersForbatchList(@PathVariable String batchUuid, @Secure Principal principal,
			Page page, HttpServletRequest request) {
		try {
			List<ThirdPartyVoucherCommandLogShowModel> showModels = thirdPartyVoucherCommandLogHandler
					.getThirdPartyVoucherCommandLogShowModelListByBatchUuid(batchUuid, page);
			int size = thirdPartyVoucherCommandLogHandler.countThirdPartyVoucherCommandLogSize(batchUuid);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("list", showModels);
			result.put("size", size);
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			logger.error("#ThirdPartyPayVoucherApiController#vouchersForbatchList#query voucher list error ");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(QUERY_ERROR);
		}
	}
	
	@RequestMapping(value = "/list-transaction-record")
	public @ResponseBody String transactionRecordList(@ModelAttribute ThirdPartyPayAuditResultQueryModel queryModel,
			@Secure Principal principal, Page page, HttpServletRequest request) {
		try {
			List<ThirdPartyPayAuditResultShowModel> showModels = thirdPartyTransactionRecordHandler
					.getThirdPartyPayAuditResultShowModelList(queryModel, page);
			int size = thirdPartyTransactionRecordHandler.countThirdPartyPayAuditResultSize(queryModel);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("list", showModels);
			result.put("size", size);
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			logger.error("#ThirdPartyPayVoucherApiController#transactionRecordList#query transactionRecord list error ");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(QUERY_ERROR);
		}
	}
	
	@RequestMapping(value = "/list-transaction-record/options")
	public @ResponseBody String transactionRecordListOptions(@Secure Principal principal) {
		try {
			List<FinancialContract> financialContracts = principalHandler
					.get_can_access_financialContract_list(principal);

			Map<String, Object> result = new HashMap<String, Object>();
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

			result.put("queryAppModels", queryAppModels);
//			result.put("paymentInstitutionNameList", EnumUtil.getKVList(PaymentInstitutionName.class));
			result.put("paymentInstitutionNameList", EnumUtil.getKVList(paymentChannelInformationService.getAllPaymentInstitutionNames()));
			result.put("businessProcessStatus", EnumUtil.getKVList(BusinessProcessStatus.class));
			result.put("matchStatusList", EnumUtil.getKVList(MatchStatus.class));
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			logger.error("#ThirdPartyPayVoucherApiController#options#query thirdPartyPayApi options error ");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(QUERY_ERROR);
		}

	}

	/**
	 * 打开历史明细列表(包含列表、列表第一个明细信息、凭证信息)
	 * @param tradeUuid
	 * @return
	 */
	@RequestMapping(value = "/openRepaymentDetailHistory", method = RequestMethod.GET)
	public @ResponseBody String openRepaymentDetailHistory(String tradeUuid) {
		Map<String,Object> repayDetailList = new HashMap<String,Object>();
		try {
			repayDetailList  = thirdPartVoucherCommandHandler.getThirdPartVoucherRepayDetailList(tradeUuid);
			return jsonViewResolver.sucJsonResult(repayDetailList);

		}catch (Exception e){
			logger.error("查询历史版本列表错误");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("查询列表错误");
		}
	}

	/**
	 * 获取某版本历史明细（包含明细信息、凭证信息）
	 * @param tradeUuid
	 * @return
	 */
	@RequestMapping(value = "/getRepaymentDetailHistory", method = RequestMethod.GET)
	public @ResponseBody String getRepaymentDetailHistory(String tradeUuid, String versionName) {
		Map<String,Object> repayDetail = new HashMap<String,Object>();
		try {
			repayDetail = thirdPartVoucherCommandHandler.getRepayDetailAndVoucher(tradeUuid, versionName);
			return jsonViewResolver.sucJsonResult(repayDetail);
		}catch (Exception e){
			logger.error("查询历史版本错误");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("查询历史版本错误");
		}
	}

	/**
	 * 重新核销
	 * @param tradeUuid
	 * @return
	 */
	@RequestMapping(value = "/reUpload", method = RequestMethod.GET)
	public @ResponseBody String reUpload(String tradeUuid) {
		Map<String, Object> resultMap = thirdPartVoucherCommandHandler.reLoadThirdPartVoucherCommandLog(tradeUuid);
		if (resultMap.size()>0) {
			return jsonViewResolver.errorJsonResult(resultMap);
		}
		return jsonViewResolver.sucJsonResult();
	}
	/**
	 * 作废
	 * @param tradeUuid
	 * @return
	 */
	@RequestMapping(value = "/del", method = RequestMethod.GET)
	public @ResponseBody String del(String tradeUuid) {
		Map<String, Object> resultMap = thirdPartyVoucherHandler.delThirdPartVoucherCommandLog(tradeUuid);
		if (resultMap.size()>0) {
			return jsonViewResolver.errorJsonResult((String) resultMap.get("err"));
		}
		return jsonViewResolver.sucJsonResult();
	}

}
