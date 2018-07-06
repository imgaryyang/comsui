package com.zufangbao.earth.web.controller.supplier;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.web.controller.app.AppController;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.yunxin.entity.model.CreateSupplierModel;
import com.zufangbao.sun.yunxin.entity.model.SupplierQueryModel;
import com.zufangbao.sun.yunxin.handler.SupplierHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/supplier")
@MenuSetting("menu-financial")
public class SupplierController extends BaseController {
    @Autowired
    private SupplierHandler supplierHandler;

    private static final Log logger = LogFactory.getLog(AppController.class);

    // 供应商管理-列表 查询
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @MenuSetting("submenu-supplier")
    public @ResponseBody
    String searchSupplier(@ModelAttribute SupplierQueryModel queryModel, Page page) {
        try {
            if (queryModel == null) {
                return jsonViewResolver.errorJsonResult("参数错误！");
            }
            Map<String, Object> dataMap = supplierHandler.searchSupplierBy(queryModel, page);
            return jsonViewResolver.sucJsonResult(dataMap, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            logger.error("#SupplierController searchSupplier#  occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("查询错误！");
        }
    }

    // 供应商信息详情
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @MenuSetting("submenu-supplier")
    public @ResponseBody
    String detail(@RequestParam(value = "uuid") String uuid) {
        try {
            if (StringUtils.isBlank(uuid)) {
                return jsonViewResolver.errorJsonResult("供应商信息详情获取失败！！！");
            }
            Map<String, Object> data = supplierHandler.getDetails(uuid);
            return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect);
        } catch (Exception e) {
            logger.error("##SupplierController detail## error!!");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("供应商信息详情获取失败！！！");
        }
    }

    // 供应商管理-列表 新增
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @MenuSetting("submenu-supplier")
    public @ResponseBody
    String createSupplier(@ModelAttribute CreateSupplierModel queryModel,
                          @Secure Principal principal, HttpServletRequest request) {
        try {
            String msg = supplierHandler.createSupplier(queryModel, principal.getId(), IpUtil.getIpAddress(request));
            if (StringUtils.isNotBlank(msg)) {
                return jsonViewResolver.errorJsonResult(msg);
            }
            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            logger.error("#SupplierController createSupplier#  occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误！");
        }
    }

    // 供应商管理-修改
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @MenuSetting("submenu-supplier")
    public @ResponseBody
    String modifySupplier(@ModelAttribute CreateSupplierModel queryModel,
                          @Secure Principal principal, HttpServletRequest request) {
        try {
            String msg = supplierHandler.modifySupplier(queryModel, principal.getId(), IpUtil.getIpAddress(request));
            if (StringUtils.isNotBlank(msg)) {
                return jsonViewResolver.errorJsonResult(msg);
            }
            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            logger.error("#SupplierController modifySupplier#  occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }
}
