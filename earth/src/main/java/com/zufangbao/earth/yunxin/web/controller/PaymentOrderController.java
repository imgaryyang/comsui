package com.zufangbao.earth.yunxin.web.controller;

import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.suidifu.hathaway.job.Priority;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.web.controller.financial.CapitalControllerSpec.URL;
import com.zufangbao.earth.yunxin.handler.SystemOperateLogHandler;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.model.PaymentOrderModel;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.enums.voucher.JournalVoucherType;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLogRequestParam;
import com.zufangbao.wellsfargo.proxy.JournalVoucherRefundHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.PaymentOrderDetailModel;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.PaymentOrderShowModel;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.*;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.JournalVoucherService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.VoucherService;
import com.zufangbao.wellsfargo.yunxin.handler.JournalVoucherHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller()
@RequestMapping(URL.CAPITAL_NAME)
@MenuSetting(URL.CAPITAL_MENU)
public class PaymentOrderController extends BaseController{

	@Autowired
	private JournalVoucherService journalVoucherService;

	@Autowired
	private JournalVoucherHandler journalVoucherHandler;

	@Autowired
	private SystemOperateLogHandler systemOperateLogHandler;

	@Autowired
	private PrincipalHandler principalHandler;

	@Autowired
	private SourceDocumentService sourceDocumentService;

	@Autowired
	private VoucherService voucherService;

	@Autowired
	public FinancialContractService financialContractService;

	@Autowired
	@Qualifier("journalVoucherRefundHandlerProxy")
	private JournalVoucherRefundHandler journalVoucherRefundHandler;

	private static Log logger = LogFactory.getLog(PaymentOrderController.class);

	@RequestMapping(value = "customer-account-manage/payment-order-list/show",method = RequestMethod.GET)
	@MenuSetting("submenu-payment-order-list")
	public ModelAndView showPaymentOrder(@ModelAttribute PaymentOrderModel paymentOrderModel,@Secure Principal principal,Page page,
			HttpServletRequest request){
		ModelAndView modelAndView = new ModelAndView("index");
		return modelAndView;
	}

	@RequestMapping(value = "customer-account-manage/payment-order-list/show/options",method = RequestMethod.GET)
	@MenuSetting("submenu-payment-order-list")
	public @ResponseBody String getOption(@Secure Principal principal){

		HashMap<String, Object> result = new HashMap<String, Object>();
		List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
		List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

		result.put("queryAppModels", queryAppModels);
		result.put("journalVoucherStatus", EnumUtil.getKVList(JournalVoucherStatus.class));
		return jsonViewResolver.sucJsonResult(result);
	}

	@RequestMapping(value = "customer-account-manage/payment-order-list/query")
	public @ResponseBody String queryPaymentOrder(@Secure Principal principal,
			@ModelAttribute PaymentOrderModel paymentOrderModel, Page page) {

		try {
			List<JournalVoucher> journalVoucherList =  journalVoucherService.getJournalVoucherList(paymentOrderModel, page);
			int count = journalVoucherService.count(paymentOrderModel);

			List<PaymentOrderShowModel> paymentOrderShowModelList = journalVoucherHandler.getPaymentOrderShowModels(journalVoucherList);

			Map<String , Object> data = new HashMap<String, Object>();
			data.putIfAbsent("list", paymentOrderShowModelList);
			data.putIfAbsent("size", count);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	/**
	 * 退款操作
	 * @param journalVoucherUuid
	 * @param appendix
	 * @param principal
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "customer-account-manage/payment-order-list/refund", method = RequestMethod.POST)
	public @ResponseBody String	refundPaymentOrder(@RequestParam(value = "journalVoucherUuid")String journalVoucherUuid,
			@RequestParam(value = "appendix",required = false)String appendix,@Secure Principal principal,HttpServletRequest request){
		try {

			JournalVoucher journalVoucher = journalVoucherService.getJournalVoucherByVoucherUUID(journalVoucherUuid);
			JournalVoucher journalVoucherRefund = null;

			if((journalVoucher.getJournalVoucherType() == JournalVoucherType.VIRTUAL_ACCOUNT_TRANSFER_REPAYMENT || journalVoucher.getJournalVoucherType() == JournalVoucherType.VIRTUAL_ACCOUNT_TRANSFER_GUARANTEE)
					&& journalVoucher.getStatus() == JournalVoucherStatus.VOUCHER_ISSUED){
				String contractUuid=journalVoucher.getRelatedBillContractInfoLv2();
				logger.info("发送退款消息，contractUuid：["+contractUuid+"],journalVoucherUuid:["+journalVoucherUuid+"]。");
				journalVoucherRefund = journalVoucherRefundHandler.refund_operate(contractUuid,journalVoucherUuid, appendix,Priority.RealTime.getPriority());
				logger.info("发送退款消息成功，contractUuid：["+contractUuid+"],journalVoucherUuid:["+journalVoucherUuid+"]。");
			}

			if(journalVoucherRefund == null){
				logger.info("journalVoucher is null ");
				return jsonViewResolver.errorJsonResult("退款错误");
			}

			//创建退款单  journalVoucherRefund log
			SystemOperateLogRequestParam param = getSystemOperateLogrequestParamCreateRevoke(principal, request,journalVoucherRefund);
			systemOperateLogHandler.generateSystemOperateLog(param);


			// 作废余额支付单    journalVoucher log
			SystemOperateLogRequestParam paramer = getSystemOperateLogrequestParamRefund(principal, request,journalVoucher,journalVoucherRefund);
			systemOperateLogHandler.generateSystemOperateLog(paramer);


			return jsonViewResolver.sucJsonResult();
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}



	private SystemOperateLogRequestParam getSystemOperateLogrequestParamCreateRevoke(Principal principal,
			HttpServletRequest request,JournalVoucher journalVoucher ) {
		SystemOperateLogRequestParam param = new SystemOperateLogRequestParam(principal.getId(),
                IpUtil.getIpAddress(request), ",退款金额为" + journalVoucher.getBookingAmount() + ",撤销单号为[" + journalVoucher.getJournalVoucherNo() + "]", LogFunctionType.ADDPAYMENTORDER,

                LogOperateType.ADD, JournalVoucher.class, journalVoucher, null, null);
		return param;

	}

	private SystemOperateLogRequestParam getSystemOperateLogrequestParamRefund(Principal principal,
			HttpServletRequest request,JournalVoucher journalVoucher ,JournalVoucher journalVoucherRefund ) {
		SystemOperateLogRequestParam param = new SystemOperateLogRequestParam(principal.getId(),
                IpUtil.getIpAddress(request), ",退款金额为" + journalVoucher.getBookingAmount() + ",退款单号为["
                + journalVoucherRefund.getJournalVoucherNo() + "]", LogFunctionType.REVOKERPAYMENTORDER,
				LogOperateType.ADD, JournalVoucher.class, journalVoucher, null, null);
		return param;

	}

	@RequestMapping(value = "customer-account-manage/payment-order-list/detail", method = RequestMethod.GET)
	@MenuSetting("submenu-payment-order-list")
	public ModelAndView paymentOrderDetail(@Secure Principal principal) {
		try {

			ModelAndView modelAndView = new ModelAndView("index");
			return modelAndView;
		} catch (Exception e) {
			e.printStackTrace();
			return pageViewResolver.errorSpec();
		}
	}

	@RequestMapping(value = "customer-account-manage/payment-order-list/detail-data", method = RequestMethod.GET)
	@MenuSetting("submenu-payment-order-list")
	public @ResponseBody String paymentOrderDetail(@Secure Principal principal,@RequestParam("journalVoucherUuid") String journalVoucherUuid) {
		try {
			PaymentOrderDetailModel paymentOrderDetailModel = journalVoucherHandler.getPaymentOrderDetail(journalVoucherUuid);
			Map<String, Object> data = new HashMap<String, Object>();
			data.putIfAbsent("paymentOrderDetailModel", paymentOrderDetailModel);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}


	/**
	 * 凭证详情链接
	 * @param principal
	 * @param sourceDocumentUuid
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "customer-account-manage/payment-order-list/query-voucher-detail")
	public @ResponseBody String queryPaymentOrder(@Secure Principal principal,@RequestParam("sourceDocumentUuid") String sourceDocumentUuid, Page page) {
		try {

			SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentBy(sourceDocumentUuid);
			Voucher voucher = voucherService.get_voucher_by_sourceDocument(sourceDocument);

			Map<String , Object> data = new HashMap<String, Object>();
			data.putIfAbsent("voucher", voucher);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}
}
