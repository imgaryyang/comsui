package com.zufangbao.earth.yunxin.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.web.controller.financial.CapitalControllerSpec.URL;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.yunxin.entity.model.PaymentOrderModel;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.RefundOrderDetailModel;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.RefundOrderShowModel;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.JournalVoucher;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.JournalVoucherService;
import com.zufangbao.wellsfargo.yunxin.handler.JournalVoucherHandler;

@Controller()
@RequestMapping(URL.CAPITAL_NAME)
@MenuSetting(URL.CAPITAL_MENU)
public class RefundOrderController extends BaseController {
    private static final Log logger = LogFactory.getLog(RefundOrderController.class);

    @Autowired
    private JournalVoucherService journalVoucherService;

    @Autowired
    private JournalVoucherHandler journalVoucherHandler;

    @Autowired
    private PrincipalHandler principalHandler;

    @Autowired
    private PrincipalService principalService;

    @Autowired
    public FinancialContractService financialContractService;

    @RequestMapping(value = "customer-account-manage/refund-order-list/show", method = RequestMethod.GET)
    @MenuSetting("submenu-refund-order-list")
    public ModelAndView showRefundOrder(@ModelAttribute PaymentOrderModel paymentOrderModel, @Secure Principal principal, Page page, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;
    }

    @RequestMapping(value = "customer-account-manage/refund-order-list/show/options", method = RequestMethod.GET)
    public @ResponseBody
    String getOption(@Secure Principal principal) {
        try {
            HashMap<String, Object> result = new HashMap<String, Object>();
            List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
            List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

            result.put("queryAppModels", queryAppModels);
            return jsonViewResolver.sucJsonResult(result);
        } catch (Exception e) {
            logger.error("#getOption# occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("列表选项获取错误");
        }
    }

    /**
     * 支付退款 列表
     *
     * @param principal
     * @param paymentOrderModel
     * @param page
     * @return
     */
    @RequestMapping(value = "customer-account-manage/refund-order-list/query")
    public @ResponseBody
    String queryRefundOrder(@Secure Principal principal,
                            @ModelAttribute PaymentOrderModel paymentOrderModel, Page page) {

        try {
            List<JournalVoucher> journalVoucherList = journalVoucherService.getJournalVoucherRefundList(paymentOrderModel, page);
            int count = journalVoucherService.countJournalVoucherRefund(paymentOrderModel);

            List<RefundOrderShowModel> refundOrderShowModelList = journalVoucherHandler.getRefundOrderShowModels(journalVoucherList);

            Map<String, Object> data = new HashMap<String, Object>();
            data.putIfAbsent("list", refundOrderShowModelList);
            data.putIfAbsent("size", count);
            return jsonViewResolver.sucJsonResult(data);
        } catch (Exception e) {
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    /**
     * 支付退款 详情页
     *
     * @param principal
     * @param journalVoucherUuid
     * @return
     */
    @RequestMapping(value = "customer-account-manage/refund-order-list/detail-data", method = RequestMethod.GET)
    @MenuSetting("submenu-refund-order-list")
    public @ResponseBody
    String paymentOrderDetail(@Secure Principal principal, @RequestParam("journalVoucherUuid") String journalVoucherUuid) {
        try {
            RefundOrderDetailModel refundOrderDetailModel = journalVoucherHandler.getRefundOrderDetail(journalVoucherUuid);
            Map<String, Object> data = new HashMap<String, Object>();
            data.putIfAbsent("refundOrderDetailModel", refundOrderDetailModel);
            return jsonViewResolver.sucJsonResult(data);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }
    
}
