package com.zufangbao.earth.yunxin.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.utils.BeanUtils;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.gluon.spec.global.GlobalMsgSpec.GeneralErrorMsg;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.entity.remittance.ExecutionStatus;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplication;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlanExecLog;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceRefundBill;
import com.zufangbao.sun.yunxin.entity.transfer.TransferBill;
import com.zufangbao.sun.yunxin.entity.transfer.TransferBillQueryModel;
import com.zufangbao.sun.yunxin.entity.transfer.TransferBillShowModel;
import com.zufangbao.sun.yunxin.service.TransferBillService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittancePlanExecLogService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceRefundBillService;
import com.zufangbao.wellsfargo.yunxin.handler.TransferBillHandler;

@Controller()
@RequestMapping("/transfer")
@MenuSetting("menu-capital")
public class TransferBillController  extends BaseController{
	
	@Autowired
	private PrincipalHandler principalHandler;
	
	@Autowired
	private FinancialContractService financialContractService;
	
	@Autowired
	private TransferBillHandler transferBillHandler;
	
	@Autowired
	private TransferBillService transferBillService;
	
	@Autowired
	private IRemittanceApplicationService remittanceApplicationService;
	
	@Autowired
	private IRemittancePlanExecLogService remittancePlanExecLogService;
	
	@Autowired
	private IRemittanceRefundBillService remittanceRefundBillService;
	
	private static final Log logger = LogFactory.getLog(TransferBillController.class);
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	@MenuSetting("submenu-transfer-bill")
    public ModelAndView load(@Secure Principal principal, Page page, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;
    }

	@RequestMapping(value = "/options", method = RequestMethod.GET)
	@MenuSetting("submenu-transfer-bill")
    public @ResponseBody String getOption(@Secure Principal principal) {
		try {
			Map<String, Object> result = new HashMap<String, Object>();

			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

			result.put("queryAppModels", queryAppModels);
			result.put("executionStatus", EnumUtil.getKVList(ExecutionStatus.class));
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			logger.error("TransferBillController #getOption# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("获取配置数据错误");
		}
    }
    
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public @ResponseBody String query(@ModelAttribute TransferBillQueryModel transferBillQueryModel , Page page){
    	try {
			List<TransferBillShowModel> showModelList = transferBillHandler.queryTransferBill(transferBillQueryModel, page);
			int size = transferBillService.queryShowModelCount(transferBillQueryModel);
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("size", size);
			params.put("list", showModelList);
			return jsonViewResolver.sucJsonResult(params);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(GeneralErrorMsg.MSG_SYSTEM_ERROR);
		}
    }
    
    @RequestMapping(value = "{orderUuid}/detail", method = RequestMethod.GET)
    public @ResponseBody String showTransferBillDetail(@PathVariable String orderUuid ){
    	if(StringUtils.isEmpty(orderUuid)){
    		return jsonViewResolver.errorJsonResult("转账单不存在");
    	}
    	try {
    		TransferBill transferBill = transferBillService.queryTransferBillByUuid(orderUuid);
    		if(transferBill == null){
    			return jsonViewResolver.errorJsonResult("转账单不存在");
    		}
    		TransferBillShowModel showModel = new TransferBillShowModel();
			BeanUtils.copyProperties(showModel, transferBill);
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("detailModel", showModel);
    		//放款订单
			RemittanceApplication remittanceApplication = remittanceApplicationService.getRemittanceApplicationByRequestNo(transferBill.getOrderUuid());
			if(remittanceApplication == null){
				return jsonViewResolver.sucJsonResult(params);
			}
			//线上代付单列表
			List<RemittancePlanExecLog> remittancePlanExecLogList = 
					remittancePlanExecLogService.getRemittancePlanExecLogListByApplication(remittanceApplication.getRemittanceApplicationUuid());
			if(CollectionUtils.isNotEmpty(remittancePlanExecLogList)){
				RemittancePlanExecLog remittancePlanExecLog = remittancePlanExecLogList.get(0);
				//代付撤销单
				if(remittancePlanExecLog == null){
					return jsonViewResolver.sucJsonResult(params);
				}
				List<RemittanceRefundBill> refundBills = remittanceRefundBillService.listRemittanceRefundBill(remittancePlanExecLog.getExecReqNo());
				params.put("remittanceRefundBills", refundBills);
				params.put("remittancePlanExecLog", remittancePlanExecLog);
			}
			return jsonViewResolver.sucJsonResult(params);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(GeneralErrorMsg.MSG_SYSTEM_ERROR);
		}
    }
    

}
