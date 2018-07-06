package com.zufangbao.earth.yunxin.web.controller;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.yunxin.handler.SystemOperateLogHandler;
import com.zufangbao.gluon.spec.global.GlobalCodeSpec;
import com.zufangbao.gluon.spec.global.GlobalMsgSpec;
import com.zufangbao.gluon.spec.global.GlobalSpec;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.ledgerbook.*;
import com.zufangbao.sun.entity.order.OrderAssetSet;
import com.zufangbao.sun.ledgerbook.AssetCategory;
import com.zufangbao.sun.ledgerbook.AssetConvertor;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.LedgerBookHandler;
import com.zufangbao.sun.ledgerbook.LedgerItemService;
import com.zufangbao.sun.ledgerbook.LedgerTradeParty;
import com.zufangbao.sun.ledgerbookv2.handler.LedgerBookV2Handler;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.service.OrderService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.excel.ExcelUtil;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.GuaranteeStatus;
import com.zufangbao.sun.yunxin.entity.OrderType;
import com.zufangbao.sun.yunxin.entity.model.GuaranteeOrderExcel;
import com.zufangbao.sun.yunxin.entity.model.GuaranteeOrderModel;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLogRequestParam;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.wellsfargo.exception.OfflineBillCreateException;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.OfflineBillPayDetail;
import com.zufangbao.wellsfargo.yunxin.handler.OrderHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
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

@Controller
@RequestMapping("guarantee/order")
@MenuSetting("menu-finance")
public class GuaranteeController extends BaseController{

	private static final Log logger = LogFactory.getLog(GuaranteeController.class);

	@Autowired
	private OrderService orderService;

	@Autowired
	private RepaymentPlanService repaymentPlanService;

	@Autowired
	private LedgerBookHandler ledgerBookHandler;

	@Autowired
	private LedgerBookService ledgerBookService;

	@Autowired
	private LedgerItemService ledgerItemService;

	@Autowired
	private SystemOperateLogHandler systemOperateLogHandler;

	@Autowired
	private OrderHandler orderHandler;
	@Autowired
	private PrincipalHandler principalHandler;

	@Autowired
	public FinancialContractService financialContractService;

	@Autowired
	private LedgerBookV2Handler ledgerBookV2Handler;

	@RequestMapping("/options")
	@MenuSetting("submenu-guarantee-order")
	public @ResponseBody String getOption(@ModelAttribute GuaranteeOrderModel guaranteeOrderModel, @Secure Principal principal){
		try {
			HashMap<String, Object> result= new HashMap<String, Object>();

			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

			result.put("queryAppModels", queryAppModels);
			result.put("GuaranteeStatus", EnumUtil.getKVMap(GuaranteeStatus.class));
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			logger.error("#showGuaranteeOrdersPage occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("列表选项获取错误");
		}
	}

	@RequestMapping("/search")
	@MenuSetting("submenu-guarantee-order")
	public @ResponseBody String searchGuaranteeOrders(
			@ModelAttribute GuaranteeOrderModel guaranteeOrderModel, Page page) {
		try {
			int size = orderService.count(guaranteeOrderModel);
//			List<Order> pagedOrderList = orderService.getGuaranteeOrders(guaranteeOrderModel, page);
			List<OrderAssetSet> pagedOrderList = orderHandler.getGuaranteeOrderListVO(guaranteeOrderModel, page);
			Map<String, Object> resultData = new HashMap<String, Object>();
			resultData.put("list", pagedOrderList);
			resultData.put("size", size);
			return jsonViewResolver.sucJsonResult(resultData,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			logger.error("#searchGuaranteeOrders occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}
	//担保补足页--统计金额（担保金额）
	@RequestMapping("/amountStatistics")
	@MenuSetting("submenu-guarantee-order")
	public @ResponseBody String guaranteeAmountStatistics(@ModelAttribute GuaranteeOrderModel guaranteeOrderModel) {
		try {
			Map<String, Object> resultData = orderService.guaranteeAmountStatistics(guaranteeOrderModel);
			return jsonViewResolver.sucJsonResult(resultData,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			logger.error("#guaranteeAmountStatistics occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("担保金额统计错误");
		}
	}


	@RequestMapping(value = "/{orderId}/guarantee-detail", method = RequestMethod.GET)
	@MenuSetting("submenu-guarantee-order")
	public @ResponseBody String showGuaranteeDetailPage(@PathVariable("orderId") Long orderId) {
		try {
			Order order = orderService.getOrderById(orderId, OrderType.GUARANTEE);
			if(order == null) {
				return jsonViewResolver.errorJsonResult("担保单不存在！");
			}
			String assetSetUuid = order.getAssetSetUuid();
			AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(assetSetUuid);
			if (assetSet == null){
                return jsonViewResolver.errorJsonResult("担保单数据错误");
            }
			List<OfflineBillPayDetail> payDetails = orderHandler.getOfflineBillList(order);
			Map<String,Object> modelAndView = new HashMap<>();
			modelAndView.put("order", order);
			modelAndView.put("assetSet", assetSet);
			modelAndView.put("payDetails", payDetails);
			return jsonViewResolver.sucJsonResult(modelAndView, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("#showGuaranteeDetailPage occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("查询错误");
		}
	}

	@RequestMapping(value = "/{orderId}/guarantee-lapse", method = RequestMethod.POST)
	@MenuSetting("submenu-payment-payment")
	public @ResponseBody String lapseGuarantee(HttpServletRequest request,
			@PathVariable("orderId") long orderId,
			@Secure Principal principal,
			@RequestParam("comment") String comment) {
		try {
			if(StringUtils.isBlank(comment)){
				return jsonViewResolver.errorJsonResult("请输入备注");
			}

			Order order = orderService.load(Order.class, orderId);
			if(order==null || order.getOrderType()!=OrderType.GUARANTEE){
				return jsonViewResolver.errorJsonResult("没有该担保单");
			}
			AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(order.getAssetSetUuid());
			if(assetSet==null ||!assetSet.guaranteeStatusCanBeLapsed()){
				return jsonViewResolver.errorJsonResult("不符合担保作废的条件");
			}
			assetSet.setGuaranteeStatus(GuaranteeStatus.LAPSE_GUARANTEE);
			repaymentPlanService.save(assetSet);

			SystemOperateLogRequestParam param = new SystemOperateLogRequestParam(
                    principal.getId(), IpUtil.getIpAddress(request),
                    order.getOrderNo()+"，作废原因为【" + comment + "】", LogFunctionType.GUARANTEELAPSE,
					LogOperateType.LAPSE, Order.class, order, null, null);
			systemOperateLogHandler.generateSystemOperateLog(param);

			try {
				LedgerBook ledgerBook = ledgerBookService.getBookByAsset(assetSet);
				AssetCategory assetCategory=AssetConvertor.convertAnMeiTuAssetCategory(assetSet,"",null);
				if (ledgerBookV2Handler.checkLegderBookVersion(ledgerBook)){
					long beginTime = System.currentTimeMillis();
					logger.info("begin to #GuaranteeController#lapseGuarantee" +
							" generate LedgerBookShelf BeginTime:["+beginTime+"]");
					ledgerBookV2Handler.lapse_guarantee_of_asset(ledgerBook, assetSet.getAssetUuid(),assetCategory);
					long endTime = System.currentTimeMillis();
					logger.info("end time:["+endTime+"] Time:["+
							(endTime-beginTime)+"]");
				}
				if (ledgerBookV2Handler.checkLedgerBookVersionV1(ledgerBook)){
					ledgerItemService.lapse_guarantee_of_asset(ledgerBook, assetSet.getAssetUuid(),assetCategory);
					logger.info("lapse receivable_guranttee with assetUuid["+assetSet.getAssetUuid()+"].");
				}
			} catch(Exception e){
				logger.error("occur exception when lapse guaranteeOrder.");
				e.printStackTrace();
			}

			return jsonViewResolver.sucJsonResult();
		} catch(Exception e){
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(GlobalMsgSpec.MSG_FAILURE);
		}

	}

	@RequestMapping(value = "/{orderId}/guarantee-active", method = RequestMethod.POST)
	@MenuSetting("submenu-payment-payment")
	public @ResponseBody String activeGuarantee(HttpServletRequest request,
			@PathVariable("orderId") long orderId,
			@Secure Principal principal) {
		try {
			Order order = orderService.load(Order.class, orderId);
			if(order==null || order.getOrderType()!=OrderType.GUARANTEE){
				return jsonViewResolver.errorJsonResult("没有该担保单");
			}
			AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(order.getAssetSetUuid());
			if(assetSet==null ||!assetSet.isGuaranteeStatusLapsed()){
				return jsonViewResolver.errorJsonResult("不符合担保激活的条件");
			}
			assetSet.setGuaranteeStatus(GuaranteeStatus.WAITING_GUARANTEE);
			repaymentPlanService.save(assetSet);
			SystemOperateLogRequestParam param = new SystemOperateLogRequestParam(
                    principal.getId(), IpUtil.getIpAddress(request),
                    order.getOrderNo(), LogFunctionType.GUARANTEEACTIVE,
					LogOperateType.ACTIVE, Order.class, order, null, null);
			systemOperateLogHandler.generateSystemOperateLog(param);

			try {
				LedgerBook ledgerBook = ledgerBookService.getBookByAsset(assetSet);
				LedgerTradeParty guaranteeLedgerTradeParty = ledgerItemService.getGuranteLedgerTradeParty(assetSet);
				AssetCategory assetCategory=AssetConvertor.convertAnMeiTuLoanAssetToAssetCategory(assetSet);
				//脚本--担保单激活
				if (ledgerBookV2Handler.checkLegderBookVersion(ledgerBook)) {
					logger.info
							("begin to#" +
									"GuaranteeController#activeGuarantee " +
									"[AccountTmple] AssetSet:"+assetSet.getAssetUuid());
					ledgerBookV2Handler.book_receivable_load_guarantee_and_assets_sold_for_repurchase(ledgerBook, assetCategory, guaranteeLedgerTradeParty, assetSet.getAssetInitialValue());
					logger.info("担保单激活脚本");
				}
				if (ledgerBookV2Handler.checkLedgerBookVersionV1(ledgerBook)){
					ledgerBookHandler.book_receivable_load_guarantee_and_assets_sold_for_repurchase(ledgerBook, assetCategory, guaranteeLedgerTradeParty, assetSet.getAssetInitialValue());
					logger.info("book receivable_guranttee with assetUuid["+assetSet.getAssetUuid()+"].");
				}
			} catch(Exception e){
				logger.error("occur exception when active guaranteeOrder.");
				e.printStackTrace();
			}

			return jsonViewResolver.sucJsonResult();
		} catch(Exception e){
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(GlobalMsgSpec.MSG_FAILURE);
		}

	}


//	@RequestMapping("/export-excel")
//	@MenuSetting("submenu-guarantee-order")
	@Deprecated
	public @ResponseBody String exportExcel(@Secure Principal principal,
			HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute GuaranteeOrderModel guaranteeOrderModel) {
		ExportEventLogModel exportEventLogModel = new ExportEventLogModel("6", principal);
		try {
			exportEventLogModel.recordStartLoadDataTime();
			List<GuaranteeOrderExcel> guaranteeOrderExcelList = orderHandler.fetchGuaranteeOrdersThenConvert(guaranteeOrderModel, null);

			exportEventLogModel.recordAfterLoadDataComplete(guaranteeOrderExcelList.size());

			ExcelUtil<GuaranteeOrderExcel> excels = new ExcelUtil<GuaranteeOrderExcel>(GuaranteeOrderExcel.class);
			List<String> csvData = excels.exportDatasToCSV(guaranteeOrderExcelList);
			Map<String, List<String>> csvs = new HashMap<String, List<String>>();
			csvs.put("差额补足单", csvData);
			String fileName = DateUtils.getNowFullDateTime() + "_" + "差额补足单.zip";
			exportZipToClient(response, fileName, GlobalSpec.UTF_8, csvs);

			SystemOperateLogRequestParam param = new SystemOperateLogRequestParam(
                    principal.getId(), IpUtil.getIpAddress(request), null,
                    LogFunctionType.GUARANTEEEXPORT, LogOperateType.EXPORT,
					null, null, null, Collections.emptyList());
			systemOperateLogHandler.generateSystemOperateLog(param);

			exportEventLogModel.recordEndWriteOutTime();
			return jsonViewResolver.sucJsonResult();
		} catch (Exception e) {
			logger.error("Execute Download Guarantee Order Error!:" + e.getMessage());
			e.printStackTrace();
			exportEventLogModel.setErrorMsg(e.getMessage());
			return jsonViewResolver.errorJsonResult(GlobalCodeSpec.CODE_FAILURE);
		} finally {
			logger.info("#export report, record export event log info. " + JsonUtils.toJSONString(exportEventLogModel));
		}
	}

	//导出预览
	@RequestMapping("/preview-export-excel")
	@MenuSetting("submenu-guarantee-order")
	public @ResponseBody String previewExportExcel(@Secure Principal principal,
			HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute GuaranteeOrderModel guaranteeOrderModel) {
		try {
			Page page = new Page(0,10);
			List<GuaranteeOrderExcel> guaranteeOrderExcelList = orderHandler.fetchGuaranteeOrdersThenConvert(guaranteeOrderModel, page);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("list", guaranteeOrderExcelList);
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat);
		}catch (Exception e) {
			logger.error("#previewExportExcel  occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("导出预览失败");
		}
	}

}
