package com.zufangbao.earth.yunxin.web.controller;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.persistence.support.Filter;
import com.demo2do.core.utils.DateUtils;
import com.demo2do.core.utils.StringUtils;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.yunxin.excel.DailyYunxinPaymentFlowCheckExcel;
import com.zufangbao.gluon.spec.global.GlobalSpec;
import com.zufangbao.gluon.util.BeanWrapperUtil;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.TransferApplication;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.TransferApplicationService;
import com.zufangbao.sun.yunxin.entity.ExecutingDeductStatus;
import com.zufangbao.sun.yunxin.entity.PaymentWay;
import com.zufangbao.sun.yunxin.entity.model.TransferApplicationQueryModel;
import com.zufangbao.wellsfargo.yunxin.handler.TransferApplicationHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhenghb
 */
@Controller
@RequestMapping(PaymentControllerSpec.NAME)
@MenuSetting("menu-capital")
public class YunxinPaymentController extends BaseController {

	@Autowired
	private TransferApplicationService transferApplicationService;

	@Autowired
	private DailyYunxinPaymentFlowCheckExcel dailyYunxinPaymentFlowCheckExcel;

	@Autowired
	private TransferApplicationHandler transferApplicationHandler;

	@Autowired
	private PrincipalHandler principalHandler;

	private static final Log logger = LogFactory.getLog(YunxinPaymentController.class);

	@RequestMapping(value = PaymentControllerSpec.PAYMENTLIST, method = RequestMethod.GET)
	@MenuSetting("submenu-payment-payment")
	public ModelAndView showOnlinePaymentOrdersPage(@Secure Principal principal) {
		ModelAndView modelAndView = new ModelAndView("finance/payment-success");
		List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
		modelAndView.addObject("paymentWayList", PaymentWay.values());
		modelAndView.addObject("financialContracts",financialContracts);
		modelAndView.addObject("executingDeductStatusList",ExecutingDeductStatus.values());
		return modelAndView;
	}

	@RequestMapping(value = PaymentControllerSpec.PAYMENTDETAIL, method = RequestMethod.GET)
	@MenuSetting("submenu-payment-payment")
	public ModelAndView showOnlinePaymentOrderDetail(
			@PathVariable("transferApplicationId") Long transferApplicationId,
			@Secure Principal principal, Page page) {
		try {
			TransferApplication transferApplication = transferApplicationService.getTransferApplicationById(transferApplicationId);
			if(transferApplication == null) {
				return pageViewResolver.errorSpec("线上支付单不存在！");
			}
			ModelAndView modelAndView = new ModelAndView("finance/payment-detail");
			modelAndView.addObject("transferApplication", transferApplication);
			return modelAndView;
		} catch (Exception e) {
			logger.error("#showOnlinePaymentOrderDetail occur error.");
			e.printStackTrace();
			return pageViewResolver.errorSpec();
		}
	}

	@RequestMapping(value = PaymentControllerSpec.PAYMENTQUERY, method = RequestMethod.GET)
	@MenuSetting("submenu-payment-payment")
	public @ResponseBody String paymentShowView(
			HttpServletRequest request,
			@ModelAttribute TransferApplicationQueryModel transferApplicationQueryModel,
			@Secure Principal principal, Page page) {
		try {
			Map<String, Object> data = transferApplicationService.getTransferApplicationListByOnLinePaymentQuery(transferApplicationQueryModel, page);
			List<Object> resultRtn = (List<Object>) data.get("list");
			String[] propertyNames = { "transferApplicationNo", "order.id",
					"order.orderNo", "contractAccount.bank",
					"contractAccount.payerName", "contractAccount.bindId",
					"contractAccount.payAcNo", "amount", "lastModifiedTime",
					"comment", "paymentWayMsg", "executingDeductStatusMsg",
					"id" };
			List<Map<String, Object>> resultList = BeanWrapperUtil.wrapperList(resultRtn, propertyNames);
			data.put("list", resultList);
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	// 线上支付单 -- 统计金额（代扣金额）
	@RequestMapping(value = "/payment/amountStatistics", method = RequestMethod.GET)
	@MenuSetting("submenu-payment-payment")
	public @ResponseBody String amountStatistics(
			@ModelAttribute TransferApplicationQueryModel transferApplicationQueryModel) {
		try {
			Map<String, Object> data = transferApplicationService.amountStatistics(transferApplicationQueryModel);
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			logger.error("###amountStatistics### occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("代扣金额统计错误");
		}
	}

	// 线上支付单 -- 统计数量前5的银行
	@RequestMapping(value = "/payment/dataStatistics", method = RequestMethod.GET)
	@MenuSetting("submenu-payment-payment")
	public @ResponseBody String dataStatistics(
			@ModelAttribute TransferApplicationQueryModel transferApplicationQueryModel) {
		try {
			Map<String, Object> data = transferApplicationHandler.dataStatistics(transferApplicationQueryModel, 5);
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("dataMap", data);
			dataMap.put("size", data.size());
			return jsonViewResolver.sucJsonResult(dataMap, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			logger.error("###dataStatistics### occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	//对账单--导出
	@RequestMapping(value = PaymentControllerSpec.PAYMENTEXPORTEXCEL, method = RequestMethod.POST)
	@MenuSetting("submenu-payment-payment")
	public String paymentExportExcel(
			@RequestParam("startDateString") String startDate,
			@RequestParam("financialContractId") Long financialContractId,
			@Secure Principal principal, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			if (StringUtils.isEmpty(startDate)
					|| DateUtils.asDay(startDate) == null) {
				startDate = DateUtils.today();
			}
            Map<String, List<String>> csvsData = dailyYunxinPaymentFlowCheckExcel.gzUnionpayCsvs(principal, IpUtil.getIpAddress(request), startDate, financialContractId);

			String fileName = create_download_file_name(DateUtils.asDay(startDate));
		    exportZipToClient(response, fileName, GlobalSpec.UTF_8,csvsData );

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//当日还款清单--导出
	@RequestMapping(value = PaymentControllerSpec.DAILYRETURNLISTEXPORTEXCEL, method = RequestMethod.GET)
	@MenuSetting("submenu-payment-payment")
	public @ResponseBody String dailReturnListExcel(
			@RequestParam("startDateString") String startDate,
			@RequestParam("financialContractId") Long financialContractId,
			@Secure Principal principal, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			if (StringUtils.isEmpty(startDate)
					|| DateUtils.asDay(startDate) == null) {
				startDate = DateUtils.today();
			}

            Map<String, List<String>> dailyMapListCsv = dailyYunxinPaymentFlowCheckExcel.dailyReturnListExcel(principal, IpUtil.getIpAddress(request), startDate, financialContractId);

			String fileName = create_daily_return_list_download_file_name(DateUtils.asDay(startDate));
			exportZipToClient(response, fileName, GlobalSpec.UTF_8,dailyMapListCsv );
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String create_download_file_name(Date create_date) {
		return DateUtils.format(create_date, "yyyy_MM_dd") + "扣款流水对账" + ".zip";
	}

	private String create_daily_return_list_download_file_name(Date create_date) {
		return DateUtils.format(create_date, "yyyy_MM_dd") + "每日回款清单" + ".zip";
	}

	public Filter getContractFilter(
			TransferApplicationQueryModel transferApplicationQueryModel,
			Principal principal) {
		Filter filter = new Filter();

		if (transferApplicationQueryModel.getPaymentWayEnum() != null) {
			filter.addEquals("paymentWay",
					transferApplicationQueryModel.getPaymentWayEnum());
		}
		if (transferApplicationQueryModel.getExecutingDeductStatusEnum() != null) {
			filter.addEquals("executingDeductStatus",
					transferApplicationQueryModel
							.getExecutingDeductStatusEnum());
		}
		if (is_where_condition(transferApplicationQueryModel.getAccountName())) {
			filter.addLike("contractAccount.payerName",
					transferApplicationQueryModel.getAccountName());
		}
		if (is_where_condition(transferApplicationQueryModel.getPaymentNo())) {
			filter.addLike("transferApplicationNo",
					transferApplicationQueryModel.getPaymentNo());
		}

		if (is_where_condition(transferApplicationQueryModel.getRepaymentNo())) {
			filter.addLike("order.assetSet.singleLoanContractNo",
					transferApplicationQueryModel.getRepaymentNo());
		}
		if (is_where_condition(transferApplicationQueryModel.getBillingNo())) {
			filter.addLike("order.orderNo",
					transferApplicationQueryModel.getBillingNo());
		}
		if (is_where_condition(transferApplicationQueryModel.getPayAcNo())) {
			filter.addLike("contractAccount.payAcNo",
					transferApplicationQueryModel.getPayAcNo());

		}

		if (is_where_condition(transferApplicationQueryModel
				.getStartDateString())) {
			filter.addGreaterThan(
					"lastModifiedTime",
					DateUtils.addDays(
							transferApplicationQueryModel.getStartDate(), -1));
		}
		if (is_where_condition(transferApplicationQueryModel.getEndDateString())) {
			filter.addLessThan(
					"lastModifiedTime",
					DateUtils.addDays(
							transferApplicationQueryModel.getEndDate(), 1));
		}

		return filter;
	}

	private boolean is_where_condition(String address) {
		return !StringUtils.isEmpty(address);
	}
}
