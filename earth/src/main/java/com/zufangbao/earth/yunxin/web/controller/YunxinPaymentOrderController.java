package com.zufangbao.earth.yunxin.web.controller;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.util.ExceptionUtils;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.yunxin.handler.deduct.SystemDeductHandler;
import com.zufangbao.earth.yunxin.handler.impl.LogMapRecordContentSpec;
import com.zufangbao.earth.yunxin.web.controller.PaymentControllerSpec.LIST_ORDER;
import com.zufangbao.gluon.spec.earth.MessageTable4Earth;
import com.zufangbao.gluon.spec.global.GlobalCodeSpec;
import com.zufangbao.gluon.util.BeanWrapperUtil;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.entity.order.OrderAssetSet;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.ContractAccountService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.OrderService;
import com.zufangbao.sun.utils.AmountUtils;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.yunxin.entity.*;
import com.zufangbao.sun.yunxin.entity.model.NormalOrderAddModel;
import com.zufangbao.sun.yunxin.entity.model.OrderQueryModel;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.entity.model.RepaymentChargesDetail;
import com.zufangbao.sun.yunxin.entity.model.assetset.NormalOrderShowModel;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;
import com.zufangbao.wellsfargo.yunxin.handler.OrderHandler;
import com.zufangbao.wellsfargo.yunxin.model.order.OrderViewDetail;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping(PaymentControllerSpec.NAME)
@MenuSetting("menu-finance")
public class YunxinPaymentOrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderHandler orderHandler;

    @Autowired
    private PrincipalHandler principalHandler;

    @Autowired
    private RepaymentPlanHandler repaymentPlanHandler;

    @Autowired
    private RepaymentPlanService repaymentPlanService;

    @Autowired
    private ContractAccountService contractAccountService;

    @Autowired
    private FinancialContractService financialContractService;

    @Autowired
    private SystemDeductHandler systemDeductHandler;

    @Autowired
    private SystemOperateLogService systemOperateLogService;

    @Autowired
    private PrincipalService principalService;

    private static final Log logger = LogFactory.getLog(YunxinPaymentController.class);

    @RequestMapping(value = "/order/options", method = RequestMethod.GET)
    public @ResponseBody
    String getListOrdersOptions(@Secure Principal principal, HttpServletRequest request) {
        try {
            Map<String, Object> result = new HashMap<String, Object>();

            List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
            List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

			result.put("queryAppModels", queryAppModels);
			result.put(LIST_ORDER.RT_FINANCIALCONTRACTLIST,financialContracts);
			result.put(LIST_ORDER.RT_EXECUTINGSETTLINGSTATUSLIST, EnumUtil.getKVList(ExecutingSettlingStatus.class));
			result.put(LIST_ORDER.RT_OVERDUE_STATUS_LIST, EnumUtil.getKVList(OverDueStatus.class));
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("获取配置数据异常");
		}
	}
	
	private List<OrderAssetSet> getPagedSettlementOrders(OrderQueryModel orderQueryModel, Page page) {
        List<Order> orders = orderService.listOrder(orderQueryModel, page.getBeginIndex(), page.getEveryPage());
        List<OrderAssetSet> oa = new ArrayList<>();
        for (Order order : orders){
            AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(order.getAssetSetUuid());
            oa.add(new OrderAssetSet(order, assetSet));
        }
        return oa;

    }

	@RequestMapping(value = "/order/query", method = RequestMethod.GET)
	@MenuSetting("submenu-payment-order")
	public @ResponseBody String queryOrders(
			@ModelAttribute OrderQueryModel orderQueryModel, Page page,
			@Secure Principal principal, HttpServletRequest request) {
		try {
			int unpagedOrders = orderService.countOrderList(orderQueryModel);
			List<OrderAssetSet> orderList = getPagedSettlementOrders(orderQueryModel, page);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("size",unpagedOrders );
			data.put("list", orderList);
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			e.printStackTrace();
			int errCode = ExceptionUtils.getErrorCodeFromException(e);
			String message = MessageTable4Earth.getMessage(errCode);
			logger.error("#listBillingPlansBy occur error, code[" + errCode
					+ " ],message[" + message + "]");
			return jsonViewResolver.errorJsonResult(message);
		}
	}
	
	//结算单--统计金额（计划还款本金、计划还款利息、差异罚息、结算金额（计划还款本金+计划还款利息+差异罚息））
	@RequestMapping(value = "/order/paymentAmountStatistics", method = RequestMethod.GET)
	@MenuSetting("submenu-payment-order")
	public @ResponseBody String paymentAmountStatistics(@ModelAttribute OrderQueryModel orderQueryModel) {
		try {
			Map<String, Object> data = orderService.paymentAmountStatistics(orderQueryModel);
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			logger.error("#paymentAmountStatistics# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("结算单相关金额统计错误");
		}
	}

    @RequestMapping(value = "/order/{orderId}/detail", method = RequestMethod.GET)
    public @ResponseBody
    String showOrderDetail(@PathVariable("orderId") Long orderId) {
        try {
            Order order = orderService.getOrderById(orderId, OrderType.NORMAL);
            if (order == null) {
                return jsonViewResolver.errorJsonResult("结算单不存在！");
            }
            OrderViewDetail detail = orderHandler.showOrderDetail(order);
            return jsonViewResolver.sucJsonResult("detail", detail, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            e.printStackTrace();
            int errCode = ExceptionUtils.getErrorCodeFromException(e);
            String message = MessageTable4Earth.getMessage(errCode);
            logger.error("#listBillingPlansBy occur error, code[" + errCode
                    + " ],message[" + message + "]");
            return jsonViewResolver.errorJsonResult("结算单详情数据错误");
        }
    }

    //结算单详情 -费用明细预览
    @RequestMapping(value = "/order/{orderId}/chargesDetail", method = RequestMethod.GET)
    @MenuSetting("submenu-payment-asset")
    public @ResponseBody
    String chargesDetail(@PathVariable("orderId") Long orderId) {
        try {
            Order order = orderService.getOrderById(orderId, OrderType.NORMAL);
            if (order == null) {
                return jsonViewResolver.errorJsonResult("结算单不存在！");
            }
            //order的json中没数据，页面则显示'-'
            if (StringUtils.isBlank(order.getChargesDetail())) {
                return jsonViewResolver.sucJsonResult("data", null);
            }
            RepaymentChargesDetail repaymentChargesDetail = order.getChargesDetailObj();
            return jsonViewResolver.sucJsonResult("data", repaymentChargesDetail);
        } catch (Exception e) {
            e.printStackTrace();
            int errCode = ExceptionUtils.getErrorCodeFromException(e);
            String message = MessageTable4Earth.getMessage(errCode);
            logger.error("#chargesDetail occur error, code[" + errCode
                    + " ],message[" + message + "]");
            return jsonViewResolver.errorJsonResult(message);
        }
    }

    @RequestMapping(value = "/order/{repaymentBillUuid}/pre-edit", method = RequestMethod.GET)
    public ModelAndView preEditOrder(
            @PathVariable("repaymentBillUuid") String repaymentBillUuid,
            @Secure Principal principal, HttpServletRequest request) {
        try {
            Order order = orderService
                    .getOrderByRepaymentBillId(repaymentBillUuid);
            if (order.isEditable()) {
                return pageViewResolver.pageSpec(
                        "yunxin/payment/order-pre-edit", "order", order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageViewResolver.pageSpec("error-modal", "message", "结算单不可编辑！");

    }

	/**
	 * 结算单拆分页面展示
	 */
	@RequestMapping(value = "/order/split", method = RequestMethod.GET)
	public @ResponseBody String showOrderSplitPage(
			String repaymentPlanUuid) {
		try {
			AssetSet repaymentPlan = repaymentPlanService.getUniqueRepaymentPlanByUuid(repaymentPlanUuid);
			Contract contract = repaymentPlan.getContract();
			
			RepaymentChargesDetail planChargesDetail = repaymentPlanHandler.getPlanRepaymentChargesDetail(repaymentPlan);
			RepaymentChargesDetail receivableChargesDetail = repaymentPlanHandler.getReceivableRepaymentChargesDetail(repaymentPlan);
			
			RepaymentChargesDetail preparedDeductChargesDetail = orderService.getPreparedDeductChargesDetailFromNormalOrdersBy(repaymentPlan);
			RepaymentChargesDetail canSplitChargesDetail = new RepaymentChargesDetail(receivableChargesDetail).subtract(preparedDeductChargesDetail);
			
			ContractAccount contractAccount = contractAccountService.getTheEfficientContractAccountBy(contract);
			Map<String, Object> accountInfo = BeanWrapperUtil.wrapperMap(contractAccount, "payerName", "payAcNo", "bank", "province", "city");
			
			List<Order> orders = orderService.getOrderListByAssetSetUuid(repaymentPlan.getAssetUuid());
			List<NormalOrderShowModel> effectiveNormalOrders = new LinkedList<NormalOrderShowModel>();
			for (Order order : orders) {
				if(!order.isNormalOrder()) {
					continue;
				}
				if(!DateUtils.isSameDay(order.getDueDate(), new Date()) && !order.isSucc()) {
					continue;
				}
				effectiveNormalOrders.add(new NormalOrderShowModel(order));
			}
			
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("repaymentPlanNo", repaymentPlan.getSingleLoanContractNo());
			data.put("accountInfo", accountInfo);
			data.put("planChargesDetail", planChargesDetail);
			data.put("effectiveNormalOrders", effectiveNormalOrders);
			data.put("canSplitChargesDetail", canSplitChargesDetail);
			data.put("receivableChargesDetail", receivableChargesDetail);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(GlobalCodeSpec.CODE_FAILURE);
		}
	}
	
	/**
	 * 结算单新增
	 */
	@RequestMapping(value = "/order/add", params = "type=normal", method = RequestMethod.POST)
	public @ResponseBody String addNormalOrder(String repaymentPlanUuid,
			String orderInfo, @Secure Principal principal, HttpServletRequest request) {
		try {
			NormalOrderAddModel normalOrderAddModel = JsonUtils.parse(orderInfo, NormalOrderAddModel.class);
			if(normalOrderAddModel == null || AmountUtils.equals(BigDecimal.ZERO, normalOrderAddModel.getTotalFee())) {
				return jsonViewResolver.errorJsonResult("新增结算单失败，原因［费用明细格式有误］");
			}
			if(!normalOrderAddModel.isValid()) {
				return jsonViewResolver.errorJsonResult(normalOrderAddModel.getCheckFailedMsg());
			}
			AssetSet repaymentPlan = repaymentPlanService.getUniqueRepaymentPlanByUuid(repaymentPlanUuid);
			if(repaymentPlan == null || !repaymentPlanHandler.allowSplitSettlement(repaymentPlan)) {
				return jsonViewResolver.errorJsonResult("新增结算单失败，原因［非可拆分结算单的还款计划］");
			}


            RepaymentChargesDetail receivableChargesDetail = repaymentPlanHandler.getReceivableRepaymentChargesDetail(repaymentPlan);
            RepaymentChargesDetail preparedDeductChargesDetail = orderService.getPreparedDeductChargesDetailFromNormalOrdersBy(repaymentPlan);
            RepaymentChargesDetail canSplitChargesDetail = new RepaymentChargesDetail(receivableChargesDetail).subtract(preparedDeductChargesDetail);
            canSplitChargesDetail = canSplitChargesDetail.subtract(normalOrderAddModel);

            if (canSplitChargesDetail.existInvalidAmount()) {
                return jsonViewResolver.errorJsonResult("新增结算单失败，原因［费用明细超出可拆分金额］");
            }

            Customer customer = repaymentPlan.getContract().getCustomer();
            String financialContractUuid = repaymentPlan.getFinancialContractUuid();
            FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);

            Order normalOrder = new Order(normalOrderAddModel.getTotalFee(), repaymentPlan, customer, financialContract, new Date());
            //标记为手工生成
            normalOrder.setOrderSource(OrderSource.MANUAL);
            normalOrder.setChargesDetail(normalOrderAddModel.toJsonString());

            Serializable normalOrderId = orderService.save(normalOrder);

            try {
                createAndSaveNormalOrderAddLog(principal, request, normalOrder);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("新增结算单，日志记录异常！");
            }

            NormalOrderShowModel newNormalOrder = new NormalOrderShowModel(normalOrder);
            newNormalOrder.setNormalOrderId((Long) normalOrderId);

            Map<String, Object> result = new HashMap<String, Object>();
            result.put("newNormalOrder", newNormalOrder);
            result.put("canSplitChargesDetail", canSplitChargesDetail);
            return jsonViewResolver.sucJsonResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("新增结算单失败，原因［系统错误］");
        }
    }

    private void createAndSaveNormalOrderAddLog(
            Principal principal, HttpServletRequest request, Order normalOrder) {
        SystemOperateLog systemOperateLog = SystemOperateLog.createLog(
                principal.getId(), null, IpUtil.getIpAddress(request),
                LogFunctionType.MANUAL_CREATE_NORMAL_ORDER,
                LogOperateType.ADD, normalOrder.getUuid(),
                normalOrder.getOrderNo());
        String recordContent = LogMapRecordContentSpec.logFunctionTypeMatchRecordContentHeadName
                .get(systemOperateLog.getLogFunctionType()) + "，单号为【" + systemOperateLog.getKeyContent()
                + "】，状态为【" + normalOrder.getExecutingSettlingStatus().getChineseMessage() + "】";
        systemOperateLog.setRecordContent(recordContent);
        systemOperateLogService.save(systemOperateLog);
    }

	/**
	 * 结算单关闭
	 */
	@RequestMapping(value = "/order/close", params = "type=normal", method = RequestMethod.POST)
	public @ResponseBody String closeNormalOrder(@RequestParam("orderId") Long orderId,
			@Secure Principal principal, HttpServletRequest request) {
		try {
			Order order = orderService.getOrderById(orderId, OrderType.NORMAL);
			if(order == null) {
				return jsonViewResolver.errorJsonResult("结算单关闭失败，原因［结算单不存在］");
			}

			String beforeStatus = order.getExecutingSettlingStatus().getChineseMessage();

			if(order.isSucc() || order.getExecutingSettlingStatus() == ExecutingSettlingStatus.CLOSED) {
				return jsonViewResolver.errorJsonResult("结算单关闭失败，原因［该结算单已结算或已关闭］");
			}
			boolean isExistDeduct = systemDeductHandler.existSuccessOrProcessingDeductApplication(orderId);
			if(isExistDeduct) {
				return jsonViewResolver.errorJsonResult("结算单关闭失败，原因［该结算单下存在处理中或成功的扣款申请］");
			}

			order.updateStatus(OrderClearStatus.UNCLEAR, ExecutingSettlingStatus.CLOSED, null);
			orderService.update(order);

			try {
				createAndSaveNormalOrderCloseLog(principal, request, beforeStatus, order);
			} catch (Exception e) {
				//e.printStackTrace();
				logger.error("关闭结算单，日志记录异常！");
			}

			AssetSet repaymentPlan = repaymentPlanService.getUniqueRepaymentPlanByUuid(order.getAssetSetUuid());
			RepaymentChargesDetail receivableChargesDetail = repaymentPlanHandler.getReceivableRepaymentChargesDetail(repaymentPlan);
			RepaymentChargesDetail preparedDeductChargesDetail = orderService.getPreparedDeductChargesDetailFromNormalOrdersBy(repaymentPlan);
			RepaymentChargesDetail canSplitChargesDetail = new RepaymentChargesDetail(receivableChargesDetail).subtract(preparedDeductChargesDetail);
			
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("closedNormalOrder", new NormalOrderShowModel(order));
			result.put("canSplitChargesDetail", canSplitChargesDetail);
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("结算单关闭失败，原因［系统错误］");
		}
	}
	

    private void createAndSaveNormalOrderCloseLog(
            Principal principal, HttpServletRequest request, String beforeStatus, Order normalOrder) {
        SystemOperateLog systemOperateLog = SystemOperateLog.createLog(
                principal.getId(), null, IpUtil.getIpAddress(request),
                LogFunctionType.MANUAL_CLOSE_NORMAL_ORDER,
                LogOperateType.INVALIDATE, normalOrder.getUuid(),
                normalOrder.getOrderNo());
        systemOperateLog.setRecordContent(LogMapRecordContentSpec.logFunctionTypeMatchRecordContentHeadName
                .get(systemOperateLog.getLogFunctionType()) + "单号为【" + systemOperateLog.getKeyContent() + "】，状态为【"
                + beforeStatus + "】变更为【" + normalOrder.getExecutingSettlingStatus().getChineseMessage() + "】"
        );
        systemOperateLogService.save(systemOperateLog);
    }

}
