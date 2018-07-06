package com.zufangbao.earth.yunxin.web.controller;

import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.SettlementOrderVO;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.entity.Result;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.yunxin.excel.SettlementOrderForExportExcel;
import com.zufangbao.gluon.spec.global.GlobalSpec;
import com.zufangbao.sun.entity.company.corp.App;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.excel.ExcelUtil;
import com.zufangbao.sun.yunxin.SettlementStatus;
import com.zufangbao.sun.yunxin.entity.SettlementOrder;
import com.zufangbao.sun.yunxin.entity.excel.SettlementOrderExcel;
import com.zufangbao.sun.yunxin.entity.model.SettlementOrderQueryModel;
import com.zufangbao.sun.yunxin.service.SettlementOrderService;
import com.zufangbao.wellsfargo.yunxin.handler.SettlementOrderHandler;

@Controller
@RequestMapping(SetttlementOrderControllerSpec.NAME)
@MenuSetting("menu-finance")
public class SettlementOrderController extends BaseController{

	private static final Log logger = LogFactory.getLog(SettlementOrderController.class);
	
	@Autowired
	private PrincipalService principalService;

	@Autowired
	private SettlementOrderService settlementOrderService;

	@Autowired
	private SettlementOrderForExportExcel SettlementOrderForExportExcel;

	@Autowired
	private SettlementOrderHandler settlementOrderHandler;
	
	@Autowired
	private PrincipalHandler principalHandler;

	@Autowired
	public FinancialContractService financialContractService;

	@Autowired
    private RepaymentPlanService repaymentPlanService;

	private List<SettlementOrderVO> querySettlementOrderSortByLastModifyTime(SettlementOrderQueryModel settlementOrderQueryModel,Page page){
		com.demo2do.core.persistence.support.Order order = new com.demo2do.core.persistence.support.Order(settlementOrderQueryModel.getSqlOrderDBField(),settlementOrderQueryModel.getOrderSortType());
		
		List<SettlementOrder> settlementOrderList = settlementOrderService.getSettlementOrderListBy(settlementOrderQueryModel, order, page.getBeginIndex(), page.getEveryPage());

        List<SettlementOrderVO> vos = new ArrayList<>();
        for (SettlementOrder settlementOrder : settlementOrderList) {
            AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(settlementOrder.getAssetSetUuid());
            vos.add(new SettlementOrderVO(settlementOrder, assetSet));
        }
		return vos;
	}
	
	@RequestMapping(value = "options", method = RequestMethod.GET)
	public @ResponseBody String getSettlementOrderPageOptions(HttpServletRequest request,
			@ModelAttribute SettlementOrderQueryModel settlementOrderQueryModel,@Secure Principal principal){
		try {
			Map<String, Object> result= new HashMap<String, Object>();

			List<App> appList = principalService.get_can_access_app_list(principal);
			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

			result.put("queryAppModels", queryAppModels);
			result.put("settlementOrderQueryModel", settlementOrderQueryModel);
			result.put("authority", principal.getAuthority());
			result.put("appList", appList);
			result.put("settlementStatusList", EnumUtil.getKVList(SettlementStatus.class));
			return jsonViewResolver.sucJsonResult(result);
		} catch(Exception e){
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("获取配置数据错误");
		}
	}
			
	
	@RequestMapping(value = "query", method = RequestMethod.GET)
	@MenuSetting("submenu-settlement-order")
	public @ResponseBody String querySettlementOrder(HttpServletRequest request,
			@ModelAttribute SettlementOrderQueryModel settlementOrderQueryModel, @Secure Principal principal,
			Page page) {
		try {
			List<SettlementOrderVO> settlementOrderList = querySettlementOrderSortByLastModifyTime(settlementOrderQueryModel, page) ;
			int size = settlementOrderService.countSettlementOrderListBy(settlementOrderQueryModel);
			Map<String, Object> resultData = new HashMap<String, Object>();
			resultData.put("list", settlementOrderList);
			resultData.put("size", size);
			return jsonViewResolver.sucJsonResult(resultData,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e){
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
		
	}

	@RequestMapping(value = SetttlementOrderControllerSpec.SETTLEDETAIL, method = RequestMethod.GET)
	public @ResponseBody String getSettlementOrderDetail(@PathVariable("settlementOrderId") Long settlementOrderId,
			@Secure Principal principal) {
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			SettlementOrder settlementOrder = settlementOrderService.load(SettlementOrder.class, settlementOrderId);
			String assetSetUuid = settlementOrder.getAssetSetUuid();
            AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(assetSetUuid);
			result.put("settlementOrder", settlementOrder);
			result.put("assetSet", assetSet);
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			logger.error("#getSettlementOrderDetail  occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("获取担保清算详情错误");
		}
	}

//	@RequestMapping(value = SetttlementOrderControllerSpec.EXPORTEXCEL, method = RequestMethod.GET)
//	@MenuSetting("submenu-settlement-order")
	@Deprecated
	public @ResponseBody String exportSettlementExcel(
			@Secure Principal principal,
			@ModelAttribute SettlementOrderQueryModel settlementOrderQueryModel,
			HttpServletResponse response) {
		ExportEventLogModel exportEventLogModel = new ExportEventLogModel("7", principal);
		try {
			exportEventLogModel.recordStartLoadDataTime();
			
			List<SettlementOrderExcel> settlementOrderExcels = SettlementOrderForExportExcel.createExcel(settlementOrderQueryModel);
			
			exportEventLogModel.recordAfterLoadDataComplete(settlementOrderExcels.size());
			
			ExcelUtil<SettlementOrderExcel> excelUtil = new ExcelUtil<SettlementOrderExcel>(SettlementOrderExcel.class);
			List<String> csvData = excelUtil.exportDatasToCSV(settlementOrderExcels);
			Map<String, List<String>> csvs = new HashMap<String, List<String>>();
			csvs.put("结清单详情", csvData);
			String fileName = create_download_file_name(new Date());
			exportZipToClient(response, fileName, GlobalSpec.UTF_8, csvs);
			
			exportEventLogModel.recordEndWriteOutTime();
			return jsonViewResolver.sucJsonResult();
		}catch (Exception e) {
			logger.error("#exportSettlementExcel  occur error.");
			e.printStackTrace();
			exportEventLogModel.setErrorMsg(e.getMessage());
			return jsonViewResolver.errorJsonResult("清算汇总表导出失败");
		} finally {
			logger.info("#export report, record export event log info. " + JsonUtils.toJSONString(exportEventLogModel));
		}
	}

	private String create_download_file_name(Date create_date) {
		return DateUtils.format(create_date, "yyyy_MM_dd") + "担保清算单" + ".zip";
	}

	//清算汇总表--导出预览
	@RequestMapping(value = "/settle/preview_export_settlement_excel", method = RequestMethod.GET)
	@MenuSetting("submenu-settlement-order")
	public @ResponseBody String previewExportSettlementExcel(@ModelAttribute SettlementOrderQueryModel settlementOrderQueryModel,
			HttpServletResponse response) {
		try {
			List<SettlementOrderExcel> settlementOrderExcels = SettlementOrderForExportExcel.previewSettlementOrderExcel(settlementOrderQueryModel);

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("list", settlementOrderExcels);
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat);
		}catch (Exception e) {
			logger.error("#previewExportSettlementExcel  occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("清算汇总表导出预览失败");
		}
	}
	
	@RequestMapping(value = "batch-submit", method = RequestMethod.POST)
	@MenuSetting("submenu-settlement-order")
	public @ResponseBody String batchSubmit(HttpServletRequest request,
			@ModelAttribute SettlementOrderQueryModel settlementOrderQueryModel) {
		try {
			logger.info("start batch submit: " + settlementOrderQueryModel.getSettlementOrderUuids());
			settlementOrderHandler.batchSubmit(settlementOrderQueryModel);
			logger.info("end batch submit! ");
			return JsonUtils.toJsonString(new Result().success());
		} catch (Exception e) {
			logger.info("batch submit error: " + e.getMessage());
			e.printStackTrace();
			return JsonUtils.toJsonString(new Result().fail().data("errorMsg", e.getMessage()));
		}
	}
	
	@RequestMapping(value = "batch-settlement", method = RequestMethod.POST)
	@MenuSetting("submenu-settlement-order")
	public @ResponseBody String batchSettlement(HttpServletRequest request,
			@ModelAttribute SettlementOrderQueryModel settlementOrderQueryModel) {
		try {
			logger.info("start batch settlement: " + settlementOrderQueryModel.getSettlementOrderUuids());
			settlementOrderHandler.batchSettlement(settlementOrderQueryModel);
			logger.info("end batch settlement! ");
			return JsonUtils.toJsonString(new Result().success());
		} catch (Exception e) {
			logger.info("batch settlement error: " + e.getMessage());
			e.printStackTrace();
			return JsonUtils.toJsonString(new Result().fail().data("errorMsg", e.getMessage()));
		}
	}

}
