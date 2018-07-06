package com.zufangbao.earth.yunxin.web.controller.voucher;

import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.yunxin.api.handler.ActivePaymentVoucherHandler;
import com.zufangbao.earth.yunxin.api.handler.BusinessPaymentVoucherHandler;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherDetailModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherQueryModel;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocumentDetailStatus;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.BusinessPaymentVoucherSession;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.SourceDocumentDetailHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.VoucherHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentDetailService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentService;
import com.zufangbao.wellsfargo.thirdpartvoucher.model.VoucherShowModel;
import com.zufangbao.wellsfargo.yunxin.handler.ActivePaymentVoucherProxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 专户放款凭证
 */
@Controller("remittanceVoucherController")
@RequestMapping("/voucher")
@MenuSetting("menu-capital")
public class RemittanceVoucherController extends BaseController {

    private static final Log logger = LogFactory.getLog(RemittanceVoucherController.class);

    @Autowired
    private SourceDocumentDetailService sourceDocumentDetailService;
    @Autowired
    private SourceDocumentService sourceDocumentService;
    @Autowired
    private SourceDocumentDetailHandler sourceDocumentDetailHandler;
    @Autowired
    private BusinessPaymentVoucherSession businessPaymentVoucherSession;
    @Autowired
    private BusinessPaymentVoucherHandler businessPaymentVoucherHandler;
    @Autowired
    private ActivePaymentVoucherHandler activePaymentVoucherHandler;
    @Autowired
    private ActivePaymentVoucherProxy activePaymentVoucherProxyHandler;
    @Autowired
    private VoucherHandler voucherHandler;
    @Autowired
    private PrincipalHandler principalHandler;


    //专户放款凭证 首页
    @RequestMapping(value = "/remittance", method = RequestMethod.GET)
    @MenuSetting("submenu-remittance-business")
    public ModelAndView showRemittanceVoucherPage(
            @ModelAttribute VoucherQueryModel voucherQueryModel,
            @Secure Principal principal, Page page, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("index");

        return modelAndView;
    }

    //专户放款凭证 首页
    @RequestMapping(value = "/remittance/optionData", method = RequestMethod.GET)
    public @ResponseBody
    String getRemittanceVoucherOptions(
            @Secure Principal principal) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);

        result.put("voucherStatusList",
                EnumUtil.getKVList(SourceDocumentDetailStatus.class));
        result.put("financialContracts", financialContracts);

        return jsonViewResolver.sucJsonResult(result);
    }

    //专户放款凭证 查询
    @RequestMapping(value = "/remittance/query", method = RequestMethod.GET)
    @MenuSetting("submenu-voucher-remittance")
    public @ResponseBody String queryRemittanceVoucher(
            @ModelAttribute VoucherQueryModel voucherQueryModel,
            @Secure Principal principal, Page page, HttpServletRequest request) {
        try {
            int size = voucherHandler.countBusinessVouchers(voucherQueryModel);
            List<VoucherShowModel> showList = voucherHandler
                    .getBusinessVoucherList(voucherQueryModel, page);
            Map<String, Object> data = new HashMap<>();
            data.put("list", showList);
            data.put("size", size);
            return jsonViewResolver.sucJsonResult(data);
        } catch (Exception e) {
            logger.error("#queryVoucher occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    //专户放款凭证 详情
    @RequestMapping(value = "/remittance/detail/{voucherId}/data", method = RequestMethod.GET)
    @MenuSetting("submenu-voucher-remittance")
    public @ResponseBody String getRemittanceVoucherDetailData(
            @PathVariable("voucherId") Long voucherId,
            @Secure Principal principal, HttpServletRequest request) {
        try {
            VoucherDetailModel detail = voucherHandler
                    .getBusinessVoucherDetailModel(voucherId);
            if (detail == null) {
                return jsonViewResolver
                        .errorJsonResult("当前凭证不是商户付款凭证，无法进入详情页!");
            }
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("detail", detail);
            result.put("voucherId", voucherId);
            return jsonViewResolver.sucJsonResult(result);
        } catch (Exception e) {
            logger.error("#getBusinessVoucherDetailData occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }
}
