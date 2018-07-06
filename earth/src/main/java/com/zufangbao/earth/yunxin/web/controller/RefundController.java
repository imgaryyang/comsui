package com.zufangbao.earth.yunxin.web.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.demo2do.core.utils.BeanUtils;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.global.GlobalMsgSpec.GeneralErrorMsg;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.repayment.order.RefundOrder;
import com.zufangbao.sun.entity.repayment.order.RefundStatus;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.RefundOrderService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.entity.model.RefundOrderQueryModel;
import com.zufangbao.sun.yunxin.enums.voucher.JournalVoucherType;
import com.zufangbao.sun.yunxin.enums.voucher.SecondJournalVoucherType;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.BalanceRefundOrderDetailModel;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.BalanceRefundOrderShowModel;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.JournalVoucherService;
import com.zufangbao.wellsfargo.yunxin.handler.RefundOrderHandler;

@Controller()
@RequestMapping("/refund")
@MenuSetting("menu-capital")
public class RefundController extends BaseController {

	 private static final Log logger = LogFactory.getLog(RefundOrderController.class);

    @Autowired
    private PrincipalHandler principalHandler;
    
    @Autowired
    private FinancialContractService financialContractService;
    
    @Autowired
    private RefundOrderHandler refundOrderHandler;
    
    @Autowired
    private RefundOrderService refundOrderService;
    
    @Autowired
    private JournalVoucherService journalVoucherService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	@MenuSetting("submenu-refund-order")
    public ModelAndView load(@Secure Principal principal, Page page, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;
    }
	
    @RequestMapping(value = "/options", method = RequestMethod.GET)
    public @ResponseBody String getOption(@Secure Principal principal) {
        try {
            HashMap<String, Object> result = new HashMap<String, Object>();
            List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
            List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);
            List<Map<String, Object>> refundStatus = EnumUtil.getKVListExcludes(RefundStatus.class, Arrays.asList(RefundStatus.CANCELLED, RefundStatus.FAIL));
            result.put("queryAppModels", queryAppModels);
            result.put("refundStatus", refundStatus);
            return jsonViewResolver.sucJsonResult(result);
        } catch (Exception e) {
            logger.error("#getOption# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("列表选项获取错误");
        }
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public @ResponseBody String query(@ModelAttribute RefundOrderQueryModel refundOrderQueryModel, Page page) {
    	try {
    		List<BalanceRefundOrderShowModel> showModelList = refundOrderHandler.queryRefundOrder(refundOrderQueryModel, page);
			int size = refundOrderService.queryShowModelCount(refundOrderQueryModel);
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("size", size);
			params.put("list", showModelList);
			return jsonViewResolver.sucJsonResult(params);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(GeneralErrorMsg.MSG_SYSTEM_ERROR);
		}
    }
    
    @RequestMapping(value = "{refundOrderUuid}/detail", method = RequestMethod.GET)
    public @ResponseBody String showRefundOrderDetail(@PathVariable String refundOrderUuid ){
    	if(StringUtils.isEmpty(refundOrderUuid)){
    		return jsonViewResolver.errorJsonResult("余额退款单不存在");
    	}
    	try {
    		Map<String,Object> params = new HashMap<String,Object>();
    		RefundOrder refundOrder = refundOrderService.queryRefundOrderByRefundOrderUuid(refundOrderUuid);
    		if(refundOrder == null){
    			return jsonViewResolver.errorJsonResult("余额退款单不存在");
    		}
    		BalanceRefundOrderDetailModel detail = new BalanceRefundOrderDetailModel();
    		BeanUtils.copyProperties(detail, refundOrder);
    		params.put("detail", detail);
			return jsonViewResolver.sucJsonResult(params);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(GeneralErrorMsg.MSG_SYSTEM_ERROR);
		}
    }
    
    @RequestMapping(value = "/{assetSetUuid}/queryRecord", method = RequestMethod.GET)
    public @ResponseBody String queryByAssetSet(@PathVariable("assetSetUuid") String assetSetUuid, Page page) {
    	try {
    		List<BalanceRefundOrderShowModel> refundRecord = refundOrderHandler.queryRefundRecord(assetSetUuid, page);
			int size = journalVoucherService.countRecordNum(assetSetUuid, JournalVoucherType.REFUND_VOUCHER, SecondJournalVoucherType.ASSET_REFUND_VOUCHER);
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("size", size);
			params.put("list", refundRecord);
			return jsonViewResolver.sucJsonResult(params);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(GeneralErrorMsg.MSG_SYSTEM_ERROR);
		}
    }
    
}
