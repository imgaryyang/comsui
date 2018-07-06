package com.zufangbao.earth.yunxin.web.controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.suidifu.giotto.handler.FastHandler;
import com.suidifu.hathaway.job.Priority;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.report.util.CSVUtil;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.gluon.api.earth.v3.model.ApiJsonViewResolver;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.resolver.JsonViewResolver;
import com.zufangbao.gluon.spec.global.GlobalSpec;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.account.Account;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.payment.order.PaymentOrderCashFlowShowModel;
import com.zufangbao.sun.entity.payment.order.PaymentOrderQueryModel;
import com.zufangbao.sun.entity.payment.order.PaymentOrderRecordModel;
import com.zufangbao.sun.entity.payment.order.PaymentOrderShowModel;
import com.zufangbao.sun.entity.payment.order.RepaymentOrderVoucherShowModel;
import com.zufangbao.sun.entity.repayment.order.AliveStatus;
import com.zufangbao.sun.entity.repayment.order.PayStatus;
import com.zufangbao.sun.entity.repayment.order.PayWay;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.PaymentOrderStatus;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderItemShowModel;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrderSourceStatus;
import com.zufangbao.sun.entity.repayment.order.RepaymentStatus;
import com.zufangbao.sun.entity.repayment.order.RepaymentWayGroupType;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.PaymentOrderService;
import com.zufangbao.sun.service.RepaymentOrderControllerSpec;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.service.ThirdPartVoucherCommandLogService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.utils.excel.ExcelUtil;
import com.zufangbao.sun.yunxin.entity.RepaymentOrderDetailModel;
import com.zufangbao.sun.yunxin.entity.RepaymentOrderDetailReturnModel;
import com.zufangbao.sun.yunxin.entity.RepaymentRepaymentPlanInformation;
import com.zufangbao.sun.yunxin.entity.RepaymentRepurchaseInformation;
import com.zufangbao.sun.yunxin.entity.excel.RepaymentOrderCSVDataLists;
import com.zufangbao.sun.yunxin.entity.excel.RepaymentOrderRepaymentItemExcelVO;
import com.zufangbao.sun.yunxin.entity.excel.RepaymentOrderRepurchaseItemExcelVO;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.entity.model.repaymentOrder.RepaymentOrderDetailQueryModel;
import com.zufangbao.sun.yunxin.entity.model.repaymentOrder.RepaymentOrderItemModel;
import com.zufangbao.sun.yunxin.entity.model.repaymentOrder.RepaymentOrderQueryModel;
import com.zufangbao.sun.yunxin.entity.model.repaymentOrder.RepaymentOrderQueryModelForMerge;
import com.zufangbao.sun.yunxin.entity.model.repaymentOrder.RepaymentOrderShowModel;
import com.zufangbao.sun.yunxin.handler.RepaymentOrderItemHandler;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;
import com.zufangbao.sun.yunxin.service.repayment.RepaymentOrderItemService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.CashFlowHandler;
import com.zufangbao.wellsfargo.yunxin.handler.PaymentOrderHandler;
import com.zufangbao.wellsfargo.yunxin.handler.PaymentOrderHandlerNoSession;
import com.zufangbao.wellsfargo.yunxin.handler.PaymentOrderHandlerProxy;
import com.zufangbao.wellsfargo.yunxin.handler.RepaymentOrderForSingleContractHandlerProxy;
import com.zufangbao.wellsfargo.yunxin.handler.RepaymentOrderHandler;
import com.zufangbao.wellsfargo.yunxin.handler.RepaymentOrderItemHandlerProxy;
import com.zufangbao.wellsfargo.yunxin.model.RepaymentOrderQueryResult;
import com.zufangbao.wellsfargo.yunxin.model.paymentOrder.PaymentOrderCashFlowInfoModel;
import com.zufangbao.wellsfargo.yunxin.model.paymentOrder.PaymentOrderException;
import com.zufangbao.wellsfargo.yunxin.model.paymentOrder.PaymentOrderSubmitModel;

/**
 * Created by zxj on 2017/6/14.
 */
@Controller("RepaymentOrderController")
@RequestMapping("/repayment-order")
@MenuSetting("menu-finance")
public class RepaymentOrderController extends BaseController {

	@Autowired
	private PrincipalHandler principalHandler;

	@Autowired
	private FastHandler fastHandler;

	@Autowired
	private RepaymentOrderService repaymentOrderService;

	@Autowired
	private PaymentOrderHandlerNoSession paymentOrderHandlerNoSession;

	@Autowired
	private RepaymentOrderItemHandlerProxy repaymentOrderItemHandlerProxy;

	@Autowired
	private JsonViewResolver jsonViewResolver;

	@Autowired
	private PaymentOrderService paymentOrderService;

	@Autowired
	private RepaymentOrderHandler repaymentOrderHandler;

	@Autowired
	private RepaymentOrderItemService repaymentOrderItemService;

	@Autowired
	private SystemOperateLogService systemOperateLogService;

	@Autowired
	private PaymentOrderHandler paymentOrderHandler;

	@Autowired
	private CashFlowHandler cashFlowHandler;
	@Autowired
	private CashFlowService cashFlowService;

	@Autowired
	private PaymentOrderHandlerProxy paymentOrderHandlerProxy;

	@Autowired
	private ApiJsonViewResolver apiJsonViewResolver;
	@Autowired
	private RepaymentOrderForSingleContractHandlerProxy repaymentOrderForSingleContractHandlerProxy;

	private static String savePath;

	@Value("#{config['uploadPath']}")
	private void setSavePath(String uploadPath){
		if(StringUtils.isEmpty(uploadPath)){
			RepaymentOrderController.savePath = getClass().getResource(".").getFile().toString() + "repaymentOrder/" ;
		}else if(uploadPath.endsWith(File.separator)){
			RepaymentOrderController.savePath = uploadPath+ "repaymentOrder"+ File.separator;
		}else{
			RepaymentOrderController.savePath =  uploadPath+ File.separator+ "repaymentOrder"+ File.separator;
		}
	}

	@Value("#{config['yx.api.order_detail_path']}")
	private String YX_API_ORDER_DETAIL_PATH = "";


	@Value("#{config['yx.api.merId']}")
	private String merId;

	private static final Log logger = LogFactory.getLog(RepaymentOrderController.class);

	@Autowired
	private FinancialContractService financialContractService;

	@Autowired
	private ContractService contractService;

	@Autowired
	private RepaymentPlanService repaymentPlanService;

	@Autowired
	private RepaymentOrderItemHandler repaymentOrderItemHandler;
	
	@Autowired
	private ThirdPartVoucherCommandLogService thirdPartVoucherCommandLogService;
	/**
     * 还款订单列表页-optionsData
     */
	@RequestMapping(value = "/repayment/optionsData", method = RequestMethod.GET)
	@MenuSetting("submenu-repayment-order-list")
	public @ResponseBody String getRepaymentOrderOptions(@Secure Principal principal) {
		try {
			Map<String, Object> result = new HashMap<String, Object>();

			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

			result.put("queryAppModels", queryAppModels);
			result.put("repaymentStatus", EnumUtil.getKVList(RepaymentStatus.class));
			result.put("orderSourceStatus", EnumUtil.getKVList(RepaymentOrderSourceStatus.class));
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("获取配置数据错误");
		}
	}
	
	/**
     * 还款订单列表页
     */
	@RequestMapping(value = "/repayment/query", method = RequestMethod.GET)
	@MenuSetting("submenu-repayment-order-list")
	public @ResponseBody String queryRepaymentOrder(@ModelAttribute RepaymentOrderQueryModel repaymentOrderQueryModel,
			@Secure Principal principal, Page page) {
		try {
			List<RepaymentOrder> repaymentOrderList = repaymentOrderService
					.getRepaymentOrderList(repaymentOrderQueryModel, page);
			int count = repaymentOrderService.count(repaymentOrderQueryModel);

			Map<String, Object> data = new HashMap<>();
			data.putIfAbsent("list", repaymentOrderList);
			data.putIfAbsent("size", count);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}
	
	/**
     * 还款订单详情页
     */
	@RequestMapping(value = "/repayment/detail-data", method = RequestMethod.GET)
	@MenuSetting("menu-repayment-order-detail")
	public @ResponseBody String getRepaymentOrderDetail(@RequestParam("orderUUid") String orderUUid,
			@Secure Principal principal) {
		try {
			RepaymentOrderShowModel model = repaymentOrderService.getRepaymentOrderShowModel(orderUUid);
			Map<String, Object> data = new HashMap<>();
			data.putIfAbsent("model", model);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

    /**
     * 还款订单详情页-查询还款订单明细
     */
   @RequestMapping(value = "/repayment/query-detail", method = RequestMethod.GET)
	@MenuSetting("menu-repayment-order-querydetail")
	public @ResponseBody String queryDetailByCondition(@ModelAttribute RepaymentOrderDetailQueryModel repaymentOrderDetailQueryModel,
			@Secure Principal principal,Page page){
	   try{
			   if(repaymentOrderDetailQueryModel.modelNotAvailability()){
				   throw new ApiException(repaymentOrderDetailQueryModel.getMessage());
			   	}
			   RepaymentOrder repaymentOrder = repaymentOrderService.getRepaymentOrderByUuid(repaymentOrderDetailQueryModel.getOrderUuid());
			 	
			   long startTime= System.currentTimeMillis();
			   
			   List<RepaymentOrderItemShowModel> repaymentOrderItemShowModels = repaymentOrderHandler.builderRepaymentOrderItemShow(repaymentOrderDetailQueryModel, page, repaymentOrder);
			   
			   long end_build_model= System.currentTimeMillis();
			   logger.info(" query repaymentOrder page,end_build_model, orderUuid["+repaymentOrder.getOrderUuid()+"],use["+(end_build_model-startTime)+"]ms");
			   
			   int countForSuccess = repaymentOrderItemService.getRepaymentOrderCountForSuccess(repaymentOrderDetailQueryModel.getOrderUuid());
			   
			   long end_count_suc = System.currentTimeMillis();
	          logger.info(" quer repaymentOrder page,countForSuccess, orderUuid["+repaymentOrder.getOrderUuid()+"],use["+(end_count_suc-end_build_model)+"]ms");
	            
			   int countForFailure = repaymentOrderItemService.getRepaymentOrderCountForFailure(repaymentOrderDetailQueryModel.getOrderUuid());
			   
			   long end_count_fail = System.currentTimeMillis();
             logger.info(" query repaymentOrder page,countForFailure, orderUuid["+repaymentOrder.getOrderUuid()+"],use["+(end_count_fail-end_count_suc)+"]ms");
	       	
             Map<String, Object> data = new HashMap<>();
	       	data.putIfAbsent("list", repaymentOrderItemShowModels);
	       	data.putIfAbsent("size", countForSuccess + countForFailure);
	       	
			   return jsonViewResolver.sucJsonResult(data);
	   }catch (Exception e) {
		   		e.printStackTrace();
		   		return jsonViewResolver.errorJsonResult(e,"系统错误");
	   }
	   
	}

    /**
     * 还款订单详情页-支付订单
     */
    @RequestMapping(value = "/repayment/get-payment-orders", method = RequestMethod.GET)
    @MenuSetting("submenu-repayment-order-list")
    public @ResponseBody String queryPaymentOrders(@RequestParam("orderUuid") String orderUUid, @Secure Principal principal, Page page) {
        try {
            List<PaymentOrder> paymentOrders = paymentOrderService.getPaymentOrderListByOrderUuid(orderUUid, page);
            List<PaymentOrderShowModel> showModels = paymentOrderService.getPaymentOrderShowModels(paymentOrders);
            int count = paymentOrderService.count(orderUUid);

            Map<String, Object> data = new HashMap<>();
            data.putIfAbsent("list", showModels);
            data.putIfAbsent("size", count);
            return jsonViewResolver.sucJsonResult(data);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }
    
    /**
     * 还款订单详情页-相关凭证
     */
    @RequestMapping(value = "/repayment/get-payment-vouchers", method = RequestMethod.GET)
    @MenuSetting("submenu-repayment-order-list")
    public @ResponseBody String queryRepaymentOrderOfVoucher(@RequestParam("orderUuid") String orderUuid) {
        try {
            PaymentOrder paymentOrder = paymentOrderService.getPaymentOrderByOrderUuidAndPayStatus(orderUuid, PayStatus.PAY_SUCCESS);
            
            List<RepaymentOrderVoucherShowModel> showModels = thirdPartVoucherCommandLogService.getRepaymentOrderVoucherShowModelByPaymentOrder(paymentOrder);

            Map<String, Object> data = new HashMap<>();
            data.putIfAbsent("list", showModels);
            return jsonViewResolver.sucJsonResult(data);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }


    /**
     * 支付订单新增页-optionsData
     */
    @RequestMapping(value = "/repayment/detail-info", method = RequestMethod.GET)
    @MenuSetting("submenu-repayment-order-list")
    public @ResponseBody String getRepaymentOrderDetail(@RequestParam("orderUuid") String orderUuid) {
        try {
            RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid(orderUuid);
            
        	BigDecimal successPayingAmount = paymentOrderService.getPaySuccessAndPayingPaymentOrdersAmount(order.getOrderUuid());
    		BigDecimal unPaidAmount = order.getOrderAmount().subtract(successPayingAmount);
            
            String financialContractNo = order.getFinancialContractNo();
            String financialContractUuid = order.getFinancialContractUuid();
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("unPaidAmount", unPaidAmount);
            result.put("financialContractNo", financialContractNo);
            result.put("financialContractUuid", financialContractUuid);
            return jsonViewResolver.sucJsonResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    /**
     * 支付订单新增页-optionsData(可合并)
     */
	@RequestMapping(value = "/repayment/payWay", method = RequestMethod.GET)
	@MenuSetting("submenu-repayment-order-list")
	public @ResponseBody String getPaymentOrderCreateOptions(@Secure Principal principal) {
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("payWay", EnumUtil.getKVList(PayWay.class));
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	/**
     * 支付订单新增页-以用户名查询账户信息
     */
	@RequestMapping(value = "/search-account", method = RequestMethod.GET)
	public @ResponseBody String searchAccount(@Secure Principal principal,
			@RequestParam("financialContractUuid") String financialContractUuid,
			@RequestParam("counterAccountName") String counterAccountName) {
		try {
			List<PaymentOrderCashFlowInfoModel> models = paymentOrderHandlerNoSession
					.searchAccount(financialContractUuid, counterAccountName);
			return jsonViewResolver.sucJsonResult("models", models);
		} catch (PaymentOrderException e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(StringUtils.isEmpty(e.getMessage()) ? "系统错误" : e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");

		}
	}

	/**
     * 支付订单新增页-以账号查询流水
     */
	@RequestMapping(value = "/search-cashflow", method = RequestMethod.GET)
	public @ResponseBody String searchCashFlow(@Secure Principal principal,
			@RequestParam("financialContractUuid") String financialContractUuid,
			@RequestParam("counterAccountNo") String counterAccountNo, @RequestParam("orderUuid") String orderUuid) {
		try {
			List<PaymentOrderCashFlowInfoModel> models = paymentOrderHandlerNoSession
					.searchCashFlow(financialContractUuid, counterAccountNo, orderUuid);
			return jsonViewResolver.sucJsonResult("models", models);
		} catch (PaymentOrderException e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(StringUtils.isEmpty(e.getMessage()) ? "系统错误" : e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");

		}
	}

	/**
     * 支付订单新增页-提交
     */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public @ResponseBody String submit(@Secure Principal principal, @ModelAttribute PaymentOrderSubmitModel model,
			HttpServletRequest request) {
		try {
			String resultString = paymentOrderHandlerProxy.generatePaymentOrder(model.getOrderUuid(), JsonUtils.toJsonString(model), Priority.RealTime.getPriority());
			Result result = JsonUtils.parse(resultString,Result.class);
			if(result.isValid()==false){
				throw new PaymentOrderException(result.getMessage());
			}
			Map<String, Object> dataMap = result.getData();
			if(0==dataMap.size()){
				throw new PaymentOrderException(
						"支付订单生成失败,paymentOrderUuid为empty,orderUuid:[" + model.getOrderUuid() + "].");
			}
			for(String paymentOrderUuid : dataMap.keySet()){
				StringBuilder recordContent = new StringBuilder("支付订单,订单号[").append(model.getOrderUuid())
						.append("],支付编号[" + paymentOrderUuid + "]");
				SystemOperateLog log = SystemOperateLog.createLog(principal.getId(), recordContent.toString(),
						IpUtil.getIpAddress(request), LogFunctionType.ADD_PAYMENT_ORDER, LogOperateType.ADD,
						model.getOrderUuid(), paymentOrderUuid);
				systemOperateLogService.save(log);
			}
			return jsonViewResolver.sucJsonResult("message", result.getMessage());
		}catch (UndeclaredThrowableException ute){
			ute.printStackTrace();
			return jsonViewResolver.errorJsonResult("队列处理超时，请稍后查询结果");
		}catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e.getMessage());
		}
	}
	
	/**
     * 支付订单列表页-optionsData
     */
	@RequestMapping(value = "/payment/optionsData", method = RequestMethod.GET)
	@MenuSetting("submenu-payment-order-pay-list")
	public @ResponseBody String getPaymentOrderOptions(@Secure Principal principal) {
		try {
			List<FinancialContract> financialContracts = principalHandler
					.get_can_access_financialContract_list(principal);

			Map<String, Object> result = new HashMap<String, Object>();
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);
			result.put("queryAppModels", queryAppModels);
			result.put("financialContracts", financialContracts);
			result.put("paymentOrderStatus", EnumUtil.getKVList(PaymentOrderStatus.class));
			result.put("payWays", EnumUtil.getKVList(PayWay.class));
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	/**
     * 支付订单列表页
     */
	@RequestMapping(value = "/payment/query", method = RequestMethod.GET)
	@MenuSetting("submenu-payment-order-pay-list")
	public @ResponseBody String queryPaymentOrder(@ModelAttribute PaymentOrderQueryModel paymentOrderQueryModel,
			@Secure Principal principal, Page page) {
		try {
			List<PaymentOrder> paymentOrders = paymentOrderService.getPaymentOrderList(paymentOrderQueryModel, page);
			List<PaymentOrderShowModel> paymentOrderShowModels = paymentOrderService
					.getPaymentOrderShowModels(paymentOrders);
			int count = paymentOrderService.count(paymentOrderQueryModel);

			Map<String, Object> data = new HashMap<String, Object>();
			data.putIfAbsent("list", paymentOrderShowModels);
			data.putIfAbsent("size", count);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	/**
     * 支付订单详情页
     */
	@RequestMapping(value = "/payment/detail-data", method = RequestMethod.GET)
	@MenuSetting("submenu-payment-order-pay-list")
	public @ResponseBody String getPaymentOrderDetail(@RequestParam("paymentOrderUuid") String paymentOrderUuid,
			@Secure Principal principal) {
		try {
			PaymentOrderShowModel model = paymentOrderService.getPaymentOrderDetail(paymentOrderUuid);
			String orderUuid = model.getOrderUuid();
			RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid(orderUuid);
			boolean canCancelPaymentOrder = order.transferToRepaymentStatus() != RepaymentStatus.RECOVER_END
					&& model.isCanBeLapsed();

			PaymentOrder paymentOrder = paymentOrderService.getPaymenrOrderByUuid(paymentOrderUuid);
			if(paymentOrder == null){
				return jsonViewResolver.errorJsonResult("支付订单不存在");
			}

			//线下转账类型   银行流水
			PaymentOrderCashFlowShowModel showModel= paymentOrderService.getCashFlowShowModels(paymentOrderUuid);

			FinancialContract financialContract = financialContractService.getFinancialContractBy(paymentOrder.getFinancialContractUuid());
			Account account = financialContract.getCapitalAccount();

			Map<String, Boolean> result = isDisPlayUpdateOrSelect(account.getAccountNo(),paymentOrder, orderUuid);
			//修改 和 流水匹配  按钮
			boolean isUpdate = result.get("isUpdate");
			//选择流水 按钮
			boolean isCanSelect = result.get("isCanSelect");

			Map<String, Object> data = new HashMap<String, Object>();
			data.putIfAbsent("model", model);
			data.putIfAbsent("canCancelPaymentOrder", canCancelPaymentOrder);
			data.putIfAbsent("showModel", showModel);
			data.putIfAbsent("isUpdate", isUpdate);
			data.putIfAbsent("isCanSelect", isCanSelect);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}
	

	private Map<String, Boolean> isDisPlayUpdateOrSelect(String accountNo,PaymentOrder paymentOrder,String orderUuid){

		Boolean isUpdate = false;
		Boolean isCanSelect = false;

		Map<String, Boolean> result = new HashMap<String, Boolean>();

		if(paymentOrder.getPayWay() == PayWay.OFFLINE_TRANSFER && paymentOrder.getPayStatus() == PayStatus.IN_PAY && paymentOrder.getAliveStatus() == AliveStatus.CREATE){
			isUpdate = true;
			List<CashFlow> filteredCashFlows = cashFlowService.getFilteredCashFlowsBy(accountNo, paymentOrder.getCounterAccountNo(), paymentOrder.getAmount(), orderUuid);
			List<CashFlow> cashFlowList = new ArrayList<CashFlow>();
			for (CashFlow cashFlow : filteredCashFlows) {
				if(StringUtils.equals(cashFlow.getCounterAccountName(), paymentOrder.getCounterAccountName())){
					cashFlowList.add(cashFlow);
				}
			}
			if(cashFlowList.size() > 1 || (CollectionUtils.isEmpty(cashFlowList) && !CollectionUtils.isEmpty(filteredCashFlows))){
				isCanSelect = true;
			}
		}

		result.put("isUpdate", isUpdate);
		result.put("isCanSelect", isCanSelect);

		return result;
	}

	/**
	 * 支付记录
	 */
	@RequestMapping(value = "/payment/detail-data-record", method = RequestMethod.GET)
	@MenuSetting("submenu-payment-order-pay-list")
	public @ResponseBody String getPaymentOrderRecord(@RequestParam("paymentOrderUuid") String paymentOrderUuid,
			@Secure Principal principal, Page page) {
		try {
			
			List<PaymentOrderRecordModel> list = paymentOrderHandler.getPaymentOrderRecordModelsByPaymentOrder(paymentOrderUuid,page);
			int count = paymentOrderHandler.getCountPaymentOrderRecordModel(paymentOrderUuid);
			
			Map<String, Object> data = new HashMap<String, Object>();
			data.putIfAbsent("list", list);
			data.putIfAbsent("size", count);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}
	

	/**
	 * 作废支付订单
	 */
	@RequestMapping(value = "/cancelPaymentOrder", method = RequestMethod.GET)
	@MenuSetting("submenu-repayment-order-list")
	public @ResponseBody String cancelPaymentOrder(HttpServletRequest request, @Secure Principal principal,
			@RequestParam("paymentOrderUuid") String paymentOrderUuid) {
		try {
			PaymentOrder paymentOrder = paymentOrderHandler.checkIfPaymentOrderCanBeCancelled(paymentOrderUuid);
			paymentOrderHandlerProxy.cancelPaymentOrder(paymentOrder.getOrderUuid(), paymentOrderUuid, Priority.High.getPriority());
			saveCancelPaymentOrderLog(request, principal, paymentOrderUuid);
			return jsonViewResolver.sucJsonResult("message",
					"作废支付订单成功任务已提交");
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(StringUtils.isNotEmpty(e.getMessage()) ? e.getMessage() : "系统错误");
		}
	}

	private void saveCancelPaymentOrderLog(HttpServletRequest request, Principal principal, String paymentOrderUuid) {
		StringBuilder recordContent = new StringBuilder("作废支付订单,订单号为[").append(paymentOrderUuid)
				.append("]");
		SystemOperateLog log = SystemOperateLog.createLog(principal.getId(), recordContent.toString(),
                IpUtil.getIpAddress(request), LogFunctionType.CANCEL_PAYMENT_ORDER, LogOperateType.INVALIDATE,
                paymentOrderUuid, "");
		systemOperateLogService.save(log);
	}


    /**
     * 撤销还款订单
     */
    @RequestMapping(value = "/cancelRepaymentOrder", method = RequestMethod.GET)
    @MenuSetting("submenu-repayment-order-list")
    public @ResponseBody String cancelRepaymentOrder(HttpServletRequest request, @Secure Principal principal,
                                                     @RequestParam("orderUuid") String orderUuid, @RequestParam("remark") String remark) {
        try {
            if (repaymentOrderHandler.cancelRepaymentOrder(orderUuid)) {
                StringBuilder recordContent = new StringBuilder("撤销订单,订单号[").append(orderUuid)
                        .append("],备注[" + remark + "]");
                SystemOperateLog log = SystemOperateLog.createLog(principal.getId(), recordContent.toString(),
                        IpUtil.getIpAddress(request), LogFunctionType.CANCEL_REPAYMENT_ORDER_PAY, LogOperateType.INVALIDATE,
                        orderUuid, "");
                systemOperateLogService.save(log);
                return jsonViewResolver.sucJsonResult("message", "撤销还款订单成功");
            } else {
                return jsonViewResolver.errorJsonResult("撤销还款订单失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

//    /**
//     * 查询可合并还款订单列表
//     */
//	@RequestMapping(value = "/mergeRepayment/query", method = RequestMethod.GET)
//	@MenuSetting("submenu-repayment-order-list")
//	public @ResponseBody String mergeRepayment(@Secure Principal principal, Page page,@ModelAttribute RepaymentOrderQueryModel queryModel) {
//		try {
//			setBasicConditions(queryModel);
//
//			List<RepaymentOrder> repaymentOrderList = repaymentOrderService
//					.getRepaymentOrderList(queryModel, page);
//			int count = repaymentOrderService.count(queryModel);
//
//			Map<String, Object> data = new HashMap<>();
//			data.putIfAbsent("list", repaymentOrderList);
//			data.putIfAbsent("size", count);
//			return jsonViewResolver.sucJsonResult(data);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return jsonViewResolver.errorJsonResult("系统错误");
//		}
//	}
//	private void setBasicConditions(RepaymentOrderQueryModel queryModel) {
//		queryModel.setRepaymentStatus(RepaymentStatus.VERIFICATION_SUCCESS.ordinal());
//		queryModel.setFirstRepaymentWayGroup(RepaymentWayGroupType.ALTER_OFFLINE_REPAYMENT_ORDER_TYPE.ordinal());
//		queryModel.setSourceStatus(RepaymentOrderSourceStatus.ORDINARY.ordinal());
//	}
    
    
    /**
     * 查询可合并还款订单列表(新增主动,他人代偿)
     */
	@RequestMapping(value = "/mergeRepayment/query", method = RequestMethod.GET)
	@MenuSetting("submenu-repayment-order-list")
	public @ResponseBody String mergeRepayment(@Secure Principal principal, Page page,@ModelAttribute RepaymentOrderQueryModelForMerge queryMergeModel) {
		try {
			setBasicConditions(queryMergeModel);

			List<RepaymentOrder> repaymentOrderList = repaymentOrderService
					.getMergeRepaymentOrderList(queryMergeModel, page);
			int count = repaymentOrderService.countMergeRepaymentOrder(queryMergeModel);

			Map<String, Object> data = new HashMap<>();
			data.putIfAbsent("list", repaymentOrderList);
			data.putIfAbsent("size", count);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}
	private void setBasicConditions(RepaymentOrderQueryModelForMerge queryMergeModel) {
		queryMergeModel.setRepaymentStatus(JsonUtils.toJsonString(RepaymentStatus.mergeRepaymentForStatus()));
		queryMergeModel.setFirstRepaymentWayGroup(JsonUtils.toJsonString(RepaymentWayGroupType.getMergeRepaymentType()));
		queryMergeModel.setSourceStatus(RepaymentOrderSourceStatus.ORDINARY.ordinal());
	}
	
	/**
	 * 合并订单
	 */
	@RequestMapping(value = "/mergeRepaymentOrder", method = RequestMethod.GET)
	public @ResponseBody String mergeRepaymentOrder(HttpServletRequest request, @Secure Principal principal,
			@RequestParam("mergeRepaymentOrderUuids") String mergeRepaymentOrderUuids) {
		try {

			String result= repaymentOrderHandler.mergeRepaymentOrderCheck(mergeRepaymentOrderUuids);
			if(StringUtils.isNotEmpty(result)){
				jsonViewResolver.errorJsonResult(result);
			}
			List<String> mergeOrderUuidList = JsonUtils.parseArray(mergeRepaymentOrderUuids, String.class);
			RepaymentOrder mergedOrder = repaymentOrderHandler.mergeRepaymentOrderByUuids(mergeOrderUuidList);
			if (null == mergedOrder) {
				return jsonViewResolver.errorJsonResult("系统原因,合并订单失败!");
			} else {
				StringBuilder mergedOrderContent = new StringBuilder("合并还款订单,生成新订单[").append(mergedOrder.getOrderUuid())
						.append("]");
				SystemOperateLog mergedOrderLog = SystemOperateLog.createLog(principal.getId(), mergedOrderContent.toString(),
						IpUtil.getIpAddress(request), LogFunctionType.MERGE_REPAYMENT_ORDER_PAY, LogOperateType.ADD,
						mergedOrder.getOrderUuid(), "");
				systemOperateLogService.save(mergedOrderLog);

				for (String mergeOrderUuid : mergeOrderUuidList) {
					StringBuilder mergeOrderContent = new StringBuilder("合并订单,订单号[").append(mergeOrderUuid).append("]。生成新订单,订单号[")
							.append(mergedOrder.getOrderUuid()).append("]");
					SystemOperateLog mergeOrderLog = SystemOperateLog.createLog(principal.getId(), mergeOrderContent.toString(),
							IpUtil.getIpAddress(request), LogFunctionType.MERGE_REPAYMENT_ORDER_PAY,
							LogOperateType.LAPSE, mergeOrderUuid, "");
					systemOperateLogService.save(mergeOrderLog);
				}

				return jsonViewResolver.sucJsonResult("message", "合并成功,生成新订单[" + mergedOrder.getOrderUuid() + "]");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e, "系统原因,合并订单失败!");
		}
	}

//	/**
//	 * 合并订单
//	 */
//	@RequestMapping(value = "/mergeRepaymentOrder", method = RequestMethod.GET)
//	public @ResponseBody String mergeRepaymentOrder(HttpServletRequest request, @Secure Principal principal,
//			@RequestParam("mergeRepaymentOrderUuids") String mergeRepaymentOrderUuids) {
//		try {
//			List<String> mergeOrderUuidList = JsonUtils.parseArray(mergeRepaymentOrderUuids, String.class);
//
//			if (CollectionUtils.isEmpty(mergeOrderUuidList)) {
//				return jsonViewResolver.errorJsonResult("待合并订单为空!");
//			}
//
//			RepaymentOrder mergedOrder = repaymentOrderHandler.mergeRepaymentOrderByUuids(mergeOrderUuidList);
//			if (null == mergedOrder) {
//				return jsonViewResolver.errorJsonResult("系统原因,合并订单失败!");
//			} else {
//				StringBuilder mergedOrderContent = new StringBuilder("合并还款订单,生成新订单[").append(mergedOrder.getOrderUuid())
//						.append("]");
//				SystemOperateLog mergedOrderLog = SystemOperateLog.createLog(principal.getId(), mergedOrderContent.toString(),
//						IpUtil.getIpAddress(request), LogFunctionType.MERGE_REPAYMENT_ORDER_PAY, LogOperateType.ADD,
//						mergedOrder.getOrderUuid(), "");
//				systemOperateLogService.save(mergedOrderLog);
//
//				for (String mergeOrderUuid : mergeOrderUuidList) {
//					StringBuilder mergeOrderContent = new StringBuilder("合并订单,订单号[").append(mergeOrderUuid).append("]。生成新订单,订单号[")
//							.append(mergedOrder.getOrderUuid()).append("]");
//					SystemOperateLog mergeOrderLog = SystemOperateLog.createLog(principal.getId(), mergeOrderContent.toString(),
//							IpUtil.getIpAddress(request), LogFunctionType.MERGE_REPAYMENT_ORDER_PAY,
//							LogOperateType.LAPSE, mergeOrderUuid, "");
//					systemOperateLogService.save(mergeOrderLog);
//				}
//
//				return jsonViewResolver.sucJsonResult("message", "合并成功,生成新订单[" + mergedOrder.getOrderUuid() + "]");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return jsonViewResolver.errorJsonResult(e, "系统原因,合并订单失败!");
//		}
//	}

	/**
	 * 拆分订单
	 */
	@RequestMapping(value = "/cancelMergeRepaymentOrder", method = RequestMethod.GET)
	public @ResponseBody String cancelMergeRepaymentOrder(HttpServletRequest request, @Secure Principal principal,
			@RequestParam("orderUuid") String orderUuid, @RequestParam("remark") String remark) {
		try {
			RepaymentOrder mergeOrder = repaymentOrderHandler.cancelMergeRepaymentOrder(orderUuid);
			if (mergeOrder != null) {
				StringBuilder recordContent = new StringBuilder("拆分订单,订单号[").append(orderUuid)
						.append("],备注[" + remark + "]");
				SystemOperateLog mergeOrderLog = SystemOperateLog.createLog(principal.getId(), recordContent.toString(),
						IpUtil.getIpAddress(request), LogFunctionType.CANCEL_MERGE_REPAYMENT_ORDER_PAY,
						LogOperateType.INVALIDATE, orderUuid, "");
				systemOperateLogService.save(mergeOrderLog);
				String mergedOrderUuidListString = mergeOrder.getRepaymentOrdersList();
				List<String> mergedOrderUuidList = JsonUtils.parseArray(mergedOrderUuidListString, String.class);
				if (CollectionUtils.isNotEmpty(mergedOrderUuidList)) {
					for (String mergedOrderUuid : mergedOrderUuidList) {
						RepaymentOrder mergedOrder = repaymentOrderService.getRepaymentOrderByUuid(mergedOrderUuid);
						if (null == mergedOrder)
							continue;
						StringBuilder mergedOrderContent = new StringBuilder("拆分订单[").append(orderUuid)
								.append("],本订单状态回退");
						SystemOperateLog mergedOrderLog = SystemOperateLog.createLog(principal.getId(), mergedOrderContent.toString(),
								IpUtil.getIpAddress(request), LogFunctionType.CANCEL_MERGE_REPAYMENT_ORDER_PAY,
								LogOperateType.ACTIVE, mergedOrderUuid, "");
						systemOperateLogService.save(mergedOrderLog);
					}
				}
				return jsonViewResolver.sucJsonResult("message", "拆分还款订单成功");
			} else {
				return jsonViewResolver.errorJsonResult("拆分还款订单失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e, "系统错误");
		}
	}
    
    /**
     * 还款订单重新核销
     */
    @RequestMapping(value = "/redoReconciliation", method = RequestMethod.GET)
    public @ResponseBody String redoReconciliation(HttpServletRequest request, @Secure Principal principal,
                                                     @RequestParam("orderUuid") String orderUuid, @RequestParam("remark") String remark) {
        try {
            if (repaymentOrderHandler.redoReconciliation(orderUuid)) {
				StringBuilder recordContent = new StringBuilder("重新核销,订单号[").append(orderUuid)
						.append("]")
						.append("],备注[" + remark + "]");
				SystemOperateLog log = SystemOperateLog.createLog(principal.getId(), recordContent.toString(),
						IpUtil.getIpAddress(request), LogFunctionType.REDO_RECONCILIATION, LogOperateType.OPERATE,
						orderUuid, "");
				systemOperateLogService.save(log);
                return jsonViewResolver.sucJsonResult("message", "重新核销成功");
            } else {
                return jsonViewResolver.errorJsonResult("重新核销失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult(e,"系统原因,重新核销失败");

        }
    }

    /**
     * 支付订单详情页     选择流水
     */
    @RequestMapping(value = "/selectCashFlow", method = RequestMethod.GET)
    @MenuSetting("submenu-payment-order-pay-list")
	public @ResponseBody String selectCashFlow(HttpServletRequest request, @Secure Principal principal,
			@RequestParam("paymentOrderUuid") String paymentOrderUuid) {
		try {

			List<PaymentOrderCashFlowShowModel> showModelList = paymentOrderHandler.getCashFlowShowModels(paymentOrderUuid);

			Map<String, Object> data = new HashMap<>();
			data.putIfAbsent("list", showModelList);
			data.putIfAbsent("size", showModelList.size());

			return jsonViewResolver.sucJsonResult(data);

		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(StringUtils.isNotEmpty(e.getMessage()) ? e.getMessage() : "系统错误");
		}
	}

    /**
     * 支付订单详情页     选择流水  确定
     */
    @RequestMapping(value = "/selectCashFlowAfter", method = RequestMethod.GET)
    @MenuSetting("submenu-payment-order-pay-list")
	public @ResponseBody String selectCashFlowAfter(HttpServletRequest request, @Secure Principal principal,
			@RequestParam("paymentOrderUuid") String paymentOrderUuid,@RequestParam("cashFlowUuid") String cashFlowUuid) {
		try {

			PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid(paymentOrderUuid);
			if(paymentOrder == null){
				return jsonViewResolver.errorJsonResult("支付订单不存在");
			}

			RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid(paymentOrder.getOrderUuid());
			if(order == null){
				return jsonViewResolver.errorJsonResult("还款订单不存在");
			}

			CashFlow cashFlow = cashFlowService.getCashFlowByCashFlowUuid(cashFlowUuid);
			if(cashFlow == null){
				return jsonViewResolver.errorJsonResult("对应流水不存在");
			}

			//rpc
			paymentOrderHandler.updatePaymentOrderAndOrder(order.getOrderUuid(), paymentOrderUuid, cashFlowUuid, Priority.High.getPriority());

			StringBuilder recordContent = new StringBuilder("选择流水，流水号[").append(cashFlow.getBankSequenceNo())
					.append("]");
			SystemOperateLog log = SystemOperateLog.createLog(principal.getId(), recordContent.toString(),
					IpUtil.getIpAddress(request), LogFunctionType.SELECT_CASHFLOW_RELATED_PAYMENTORDER, LogOperateType.ACTIVE,
					paymentOrder.getUuid(), "");
			systemOperateLogService.save(log);

			return jsonViewResolver.sucJsonResult();

		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(StringUtils.isNotEmpty(e.getMessage()) ? e.getMessage() : "系统错误");
		}
	}

    /**
     * 支付订单详情页     修改支付单支付失败
     */
    @RequestMapping(value = "/updatePaymentOrderFail", method = RequestMethod.GET)
    @MenuSetting("submenu-payment-order-pay-list")
	public @ResponseBody String updatePaymentOrderFail(HttpServletRequest request, @Secure Principal principal,
			@RequestParam("paymentOrderUuid") String paymentOrderUuid,@RequestParam("paymentOrderStatus") int paymentOrderStatus,
			@RequestParam("remark") String remark) {
		try {

			PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid(paymentOrderUuid);
			if(paymentOrder == null){
				return jsonViewResolver.errorJsonResult("支付订单不存在");
			}

			RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid(paymentOrder.getOrderUuid());
			if(order == null){
				return jsonViewResolver.errorJsonResult("还款订单不存在");
			}

			//修改支付单支付失败  rpc
			paymentOrderHandlerProxy.updatePaymentOrderFail(order.getOrderUuid(), paymentOrderUuid,paymentOrderStatus,remark, Priority.High.getPriority());

			StringBuilder recordContent = new StringBuilder("修改支付订单状态为支付失败，五维支付编号[").append(paymentOrder.getUuid())
					.append("]");
			SystemOperateLog log = SystemOperateLog.createLog(principal.getId(), recordContent.toString(),
					IpUtil.getIpAddress(request), LogFunctionType.UPDATE_PAYMENTORDER_FAIL, LogOperateType.UPDATE,
					paymentOrder.getUuid(), "");
			systemOperateLogService.save(log);

			return jsonViewResolver.sucJsonResult("message", "修改成功！");

		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(StringUtils.isNotEmpty(e.getMessage()) ? e.getMessage() : "系统错误");
		}
	}


    /**
     * 支付订单详情页   流水匹配
     */
    @RequestMapping(value = "/matchCashFlow", method = RequestMethod.GET)
    @MenuSetting("submenu-payment-order-pay-list")
	public @ResponseBody String matchCashFlow(HttpServletRequest request, @Secure Principal principal,
			@RequestParam("paymentOrderUuid") String paymentOrderUuid) {
		try {

			PaymentOrder paymentOrder = paymentOrderService.getActivePaymenrOrderByUuid(paymentOrderUuid);
			if(paymentOrder == null){
				return jsonViewResolver.errorJsonResult("支付订单不存在");
			}

			RepaymentOrder order = repaymentOrderService.getRepaymentOrderByUuid(paymentOrder.getOrderUuid());
			if(order == null){
				return jsonViewResolver.errorJsonResult("还款订单不存在");
			}

			FinancialContract financialContract = financialContractService.getFinancialContractBy(order.getFinancialContractUuid());

			Account account = financialContract.getCapitalAccount();
			if (account == null){
				return jsonViewResolver.errorJsonResult("信托下的专户不存在");
			}

			//rpc
			paymentOrderHandler.matchCashFlowByPaymentOrder(order.getOrderUuid(), paymentOrderUuid, account.getAccountNo(), Priority.High.getPriority());
			
			StringBuilder recordContent = new StringBuilder("操作流水匹配");
			SystemOperateLog log = SystemOperateLog.createLog(principal.getId(), recordContent.toString(),
					IpUtil.getIpAddress(request), LogFunctionType.MATCH_CASHFLOW, LogOperateType.ACTIVE,
					paymentOrder.getUuid(), "");
			systemOperateLogService.save(log);

			return jsonViewResolver.sucJsonResult();

		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(StringUtils.isNotEmpty(e.getMessage()) ? e.getMessage() : "系统错误");
		}
	}

	/**
     * 获取还款订单的订单来源
     * hjl
     * 2017年8月23日
     */
    @RequestMapping(value="/obtainOrderSource", method = RequestMethod.GET)
    public @ResponseBody String obtainOrderSource(HttpServletRequest request,HttpServletResponse response,
    		@RequestParam("orderUuid") String orderUuid,Page page){
    	try{
    		Map<String,Object> repaymentOrderMap = repaymentOrderHandler.buildOrderSourceModelByOrderUuid(orderUuid, page);
			return jsonViewResolver.sucJsonResult(repaymentOrderMap);
    	} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
    	}
    }
    /**
     * 导出明细文件前，查询导出数量
     * hjl
     * 2017年9月29日
     */
    @RequestMapping(value="/queryExportOrderFileNumber" , method=RequestMethod.GET)
    public @ResponseBody String queryExportOrderFileNumber(HttpServletRequest request,HttpServletResponse reponse,RepaymentOrderQueryModel repaymentOrderQueryModel){
    	try{
    		Long detailNumberSum = repaymentOrderService.gainDetailsSumByRepaymentOrderQueryModel(repaymentOrderQueryModel);
    		return jsonViewResolver.sucJsonResult("detailNumberSum", detailNumberSum);
    	}catch (Exception e) {
			return jsonViewResolver.errorJsonResult("查询导出明细总条数报错");
		}
    	 
    }
    /**
     * 还款订单导出文件（重构）
     * hjl
     * 2017年8月25日
     */
    @RequestMapping(value="/exportRepaymentOrderFile", method = RequestMethod.GET)
    public @ResponseBody String exportRepaymentOrderFile(HttpServletRequest request,HttpServletResponse reponse,RepaymentOrderQueryModel repaymentOrderQueryModel, @Secure Principal principal){
    	try{
    		repaymentOrderHandler.evaluateExportRepaymentOrderFile(repaymentOrderQueryModel);
    		
	    	CSVUtil<RepaymentOrderRepaymentItemExcelVO> repaymentItemCSVUtil=new CSVUtil<>(RepaymentOrderRepaymentItemExcelVO.class);
	    	CSVUtil<RepaymentOrderRepurchaseItemExcelVO> repurchaseItemCSVUtil=new CSVUtil<>(RepaymentOrderRepurchaseItemExcelVO.class);
	    	//组建CSV模型和数据
	    	RepaymentOrderCSVDataLists repaymentOrderCSVDataLists=repaymentOrderHandler.batchBuildCSVData(repaymentOrderQueryModel);

			List<String> RepaymentOrderRepaymentItemExcelVOStrList=repaymentItemCSVUtil.buildCsvDatas(repaymentOrderCSVDataLists.getRepaymentOrderRepaymentItemExcelVOs());
			List<String> RepaymentOrderRepurchaseItemExcelVOStrList=repurchaseItemCSVUtil.buildCsvDatas(repaymentOrderCSVDataLists.getRepaymentOrderRepurchaseItemExcelVOs());

			Map<String,List<String>> ExcelVoMap= new HashMap<>();
	    	ExcelVoMap.put("文件导出_还款订单_还款明细", RepaymentOrderRepaymentItemExcelVOStrList);
	    	ExcelVoMap.put("文件导出_还款订单_回购明细", RepaymentOrderRepurchaseItemExcelVOStrList);
	    	try {
				exportZipToClient(reponse,"文件导出_还款订单_"+DateUtils.format(new Date(), DateUtils.LONG_DATE_FORMAT)+".zip",GlobalSpec.UTF_8,ExcelVoMap);
			} catch (IOException e) {
				e.printStackTrace();
				return jsonViewResolver.errorJsonResult("文件生成异常");
			}

	    	StringBuffer selectString = extractSelectString(repaymentOrderQueryModel);
			SystemOperateLog log = new SystemOperateLog(principal.getId(),IpUtil.getIpAddress(request),LogFunctionType.EXPORTREPAYMENTORDER,LogOperateType.EXPORT);
			log.setRecordContent("导出还款订单列表，导出还款明细"+(RepaymentOrderRepaymentItemExcelVOStrList.size()-1)+"条，回购明细"+(RepaymentOrderRepurchaseItemExcelVOStrList.size()-1)+"条。筛选条件："+selectString);
			systemOperateLogService.save(log);

	    	return jsonViewResolver.sucJsonResult();
    	} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e,"系统错误");
		}
    }

    private StringBuffer extractSelectString(RepaymentOrderQueryModel queryModel) {
		StringBuffer selectString = financialContractService.selectFinancialContract(queryModel.getFinancialContractUuidList());
		if (queryModel.getRepaymentStatusEnum() != null) {
			selectString.append("，订单状态【" + queryModel.getRepaymentStatusEnum().getChineseName() + "】");
		}
		if (queryModel.getSourceStatusEnum() != null) {
			selectString.append("，订单来源【" + queryModel.getSourceStatusEnum().getChineseMessage() + "】");
		}
		if (StringUtils.isNotEmpty(queryModel.getStartDateString())) {
			selectString.append("，创建起始时间【" + queryModel.getStartDateString() + "】");
		}
		if (StringUtils.isNotEmpty(queryModel.getEndDateString())) {
			selectString.append("，创建截止时间【" + queryModel.getEndDateString() + "】");
		}
		if (StringUtils.isNotBlank(queryModel.getOrderUuid())) {
			selectString.append("，订单编号【" + queryModel.getOrderUuid() + "】");
		}
		if (queryModel.getOrderAmount() != null) {
			selectString.append("，订单总金额【" + queryModel.getOrderAmount() + "】");
		}
		if (StringUtils.isNotBlank(queryModel.getOrderUniqueId())) {
			selectString.append("，商户订单号【" + queryModel.getOrderUniqueId() + "】");
		}
		if (StringUtils.isNotBlank(queryModel.getFirstCustomerName())) {
			selectString.append("，贷款人【" + queryModel.getFirstCustomerName() + "】");
		}
		return selectString;
	}

    //作废并创建新的明细
    @RequestMapping(value="/{orderUuid}/lapseAndCreateItems",method = RequestMethod.POST)
    public @ResponseBody String lapse_and_create_repayment_item(@PathVariable("orderUuid") String orderUuid, @RequestParam("items") String items,
                                                                @RequestParam("lapsedItemUuid") String lapsedItemUuid, @Secure Principal principal, HttpServletRequest request){
        try{
            logger.info("编辑原订单明细lapsedItemUuid: '"+lapsedItemUuid+"' ,明细所在还款订单orderUuid: '"+orderUuid+"' ,新增编辑订单明细items： '"+items+"'");
            Map<String, Object> OrderEditDataMap=repaymentOrderItemHandler.repaymentOrderItemEditDataCheck(orderUuid, items, lapsedItemUuid);
            List<RepaymentOrderItemModel> itemModels=(List<RepaymentOrderItemModel>) OrderEditDataMap.get(RepaymentOrderControllerSpec.ITEM_EDIT_DATA_KEY.ITEM_MODELS);
            String contractUuid=(String) OrderEditDataMap.get(RepaymentOrderControllerSpec.ITEM_EDIT_DATA_KEY.CONTRACT_UUID);
            logger.info("lapse_and_create_new_item paramters is contractUuid: '"+contractUuid+"' ,orderUuid: '"+orderUuid+"' ,lapsedItemUuid: '"+lapsedItemUuid+"' ,itemModels: '"+itemModels+"'");
            repaymentOrderItemHandlerProxy.lapse_and_create_new_item(contractUuid, orderUuid, lapsedItemUuid, itemModels, Priority.High.getPriority(),principal.getId(), IpUtil.getIpAddress(request));
            return jsonViewResolver.sucJsonResult();
        }catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult(e,"系统错误");
        }
    }

    @RequestMapping(value="/query_unclear_asset",method=RequestMethod.POST)
    public @ResponseBody String query_unclear_asset(@RequestParam("lapsedItemUuid") String lapsedItemUuid){
        try{  
        	  Contract contract=repaymentOrderItemHandler.itemsEditInitialCheck(lapsedItemUuid);
        	  
        	  List<RepaymentOrderItemModel> repaymentOrderItemModels=repaymentOrderItemHandler.buildRepaymentOrderItemModelList(contract);
        	 
        	  return jsonViewResolver.sucJsonResult("itemModel", JsonUtils.toJsonString(repaymentOrderItemModels));
        }catch(Exception e){
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult(e,"系统错误");
        }
    }

    /**
  	 * 还款订单新增--下载模板
  	 */
  	@RequestMapping(value = "/repaymentOrderDownload", method = RequestMethod.GET)
  	public @ResponseBody String downloadFile(HttpServletRequest request, HttpServletResponse response){
  		try{

  			Map<String, List<String>> csvs = new HashMap<String, List<String>>();

  			List<RepaymentRepaymentPlanInformation> repaymentRepaymentPlanInformation = new ArrayList<>();
  			ExcelUtil<RepaymentRepaymentPlanInformation> repaymentExcelUtil = new ExcelUtil<RepaymentRepaymentPlanInformation>(RepaymentRepaymentPlanInformation.class);
  			List<String> repaymentCsvData = repaymentExcelUtil.exportDatasToCSV(repaymentRepaymentPlanInformation);
  			csvs.put("还款明细", repaymentCsvData);

  			List<RepaymentRepurchaseInformation> repaymentRepurchaseInformation = new ArrayList<>();
  			ExcelUtil<RepaymentRepurchaseInformation> repurchaseExcelUtil = new ExcelUtil<RepaymentRepurchaseInformation>(RepaymentRepurchaseInformation.class);
  			List<String> repurchaseCsvData = repurchaseExcelUtil.exportDatasToCSV(repaymentRepurchaseInformation);
  			csvs.put("回购明细", repurchaseCsvData);


  			exportZipToClient(response, "订单明细_模板下载.zip", GlobalSpec.UTF_8, csvs);

  			return jsonViewResolver.sucJsonResult();
  		}catch(Exception e){
  			logger.error("#downloadFile# occur error.");
  			e.printStackTrace();
  			return jsonViewResolver.errorJsonResult("文件下载失败！");
  		}
  	}
	 /**
	  * 还款订单新增--导入还款明细文件
	 */
	@RequestMapping(value = "/importRepaymentPlanRepaymentOrderFile", method = RequestMethod.POST)
	public @ResponseBody String importRepaymentPlanRepaymentOrderDetail(HttpServletRequest request, HttpServletResponse response,MultipartFile file){
		try{
			if(file == null) {
				return jsonViewResolver.errorJsonResult("请选择要导入的订单明细！");
			}

			 ExcelUtil<RepaymentRepaymentPlanInformation> repaymentOrderDetailExcelUtil = new ExcelUtil<>(RepaymentRepaymentPlanInformation.class);
			 List<RepaymentRepaymentPlanInformation> loanInformationList = repaymentOrderDetailExcelUtil.importCSV(file);
			 if(CollectionUtils.isEmpty(loanInformationList)){
				 return jsonViewResolver.errorJsonResult("请选择正确的订单明细！");
			 }




			RepaymentOrderDetailReturnModel returnModel = repaymentOrderHandler.verifyRepaymentPlanInputParam(loanInformationList);

			Map<String, Object> data = new HashMap<>();
			data.putIfAbsent("list", returnModel.getDetailModels());
			data.putIfAbsent("amount", returnModel.getDetailAmount());

			return jsonViewResolver.sucJsonResult(data);
			}catch(NumberFormatException e){
			return jsonViewResolver.errorJsonResult("明细金额格式有误!");
		}
		catch(IOException e){
			logger.error("#importRepurchaseRepaymentOrderDetail error ]: " );
			return jsonViewResolver.errorJsonResult("导入文件格式有误！");
		}catch(Exception e){
			e.printStackTrace();
			String errorMsg = e.getMessage();
			logger.error("#importRepaymentPlanRepaymentOrderDetail occur error ]: " + errorMsg);
			return jsonViewResolver.errorJsonResult(e,"系统错误");
		}
	}

	 /**
	  * 还款订单新增--导入回购明细文件
	 */
	@RequestMapping(value = "/importRepurchaseRepaymentOrderFile", method = RequestMethod.POST)
	public @ResponseBody String importRepurchaseRepaymentOrderDetail(HttpServletRequest request, HttpServletResponse response,MultipartFile file){
		try{
			if(file == null) {
				return jsonViewResolver.errorJsonResult("请选择要导入的订单明细！");
			}

			 ExcelUtil<RepaymentRepurchaseInformation> repaymentOrderRepurchaseDetailExcelUtil = new ExcelUtil<>(RepaymentRepurchaseInformation.class);
			 List<RepaymentRepurchaseInformation> repurchaseDetailList = repaymentOrderRepurchaseDetailExcelUtil.importCSV(file);
			 if(CollectionUtils.isEmpty(repurchaseDetailList)){
				 return jsonViewResolver.errorJsonResult("请选择正确的订单明细！");
			 }

			RepaymentOrderDetailReturnModel returnModel = repaymentOrderHandler.verifyRepurchaseInputParam(repurchaseDetailList);

			Map<String, Object> data = new HashMap<>();
			data.putIfAbsent("list", returnModel.getDetailModels());
			data.putIfAbsent("amount", returnModel.getDetailAmount());

			return jsonViewResolver.sucJsonResult(data);
			}catch(NumberFormatException e){
			return jsonViewResolver.errorJsonResult("明细金额格式有误!");
		}
		catch(IOException e){
			logger.error("#importRepurchaseRepaymentOrderDetail error ]: " );
			return jsonViewResolver.errorJsonResult("导入文件格式有误！");
		}catch(Exception e){
			e.printStackTrace();
			String errorMsg = e.getMessage();
			logger.error("#importRepurchaseRepaymentOrderDetail occur error ]: " + errorMsg);
			return jsonViewResolver.errorJsonResult(e,"系统错误");
		}
	}

	/**
	 * 还款订单新增   提交
	 */
	@RequestMapping(value = "/importRepaymentOrderDetail", method = RequestMethod.POST)
	public @ResponseBody String submitRepaymentOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("data") String repaymentPlanModelsStr,
			@RequestParam("dataRepurchase") String repurchaseModelsStr,
			@RequestParam(value = "remark") String remark,
			@RequestParam(value = "financialContractNo") String financialContractNo,
			@RequestParam(value = "amount") String amount){
		try {

			List<RepaymentOrderDetailModel> detailModels = new ArrayList<RepaymentOrderDetailModel>();
			List<RepaymentOrderDetailModel> repaymentPlanDetailModels = new ArrayList<RepaymentOrderDetailModel>();
			List<RepaymentOrderDetailModel> repurchaseDetailModels = new ArrayList<RepaymentOrderDetailModel>();

			if(StringUtils.isEmpty(repaymentPlanModelsStr) && StringUtils.isEmpty(repurchaseModelsStr)){
				return jsonViewResolver.errorJsonResult("订单明细内容不能为空！");
			}

			if(StringUtils.isNotEmpty(repaymentPlanModelsStr)){
				repaymentPlanDetailModels = JSON.parseArray(repaymentPlanModelsStr, RepaymentOrderDetailModel.class);
				detailModels.addAll(repaymentPlanDetailModels);
			}

			if(StringUtils.isNotEmpty(repurchaseModelsStr)){
				repurchaseDetailModels = JSON.parseArray(repurchaseModelsStr, RepaymentOrderDetailModel.class);
				detailModels.addAll(repurchaseDetailModels);
			}

			RepaymentOrderQueryResult handlerResult = repaymentOrderHandler.repaymentOrderAdd(detailModels, financialContractNo, remark, amount, YX_API_ORDER_DETAIL_PATH, merId,RepaymentOrderSourceStatus.SYSTEM_CREATE);
			RepaymentOrder repaymentOrder = handlerResult.getRepaymentOrder();
			if(repaymentOrder==null){
				return jsonViewResolver.errorJsonResult("还款订单为空！");
			}

			//单合同类型
			if(handlerResult.getGroupType()!=null && handlerResult.getGroupType().singleContractForRepaymentWayGroupType()){
				logger.info("repaymentOrder of singleContract send msg[EasyPay, check and deduct]  start ,orderUuid["+repaymentOrder.getOrderUuid()+"]");
				repaymentOrderForSingleContractHandlerProxy.repaymentOrderSingleForEasyPay(handlerResult.getContractUuid(),repaymentOrder.getOrderUuid(),Priority.High.getPriority());
				logger.info("repaymentOrder of singleContract send msg[EasyPay, check and deduct] end ,orderUuid["+repaymentOrder.getOrderUuid()+"]");
			}
			return jsonViewResolver.sucJsonResult();
		}catch (Exception e) {
			e.printStackTrace();
			String errorMsg = e.getMessage();
			logger.error("#submitRepaymentOrder occur error ]: " + errorMsg);
			return apiJsonViewResolver.errorJsonResult(e,"系统错误");
		}
	}

}




