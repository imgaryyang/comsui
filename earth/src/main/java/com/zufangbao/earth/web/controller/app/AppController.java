package com.zufangbao.earth.web.controller.app;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.yunxin.entity.model.AppQueryModel;
import com.zufangbao.sun.yunxin.entity.model.CreateAppModel;
import com.zufangbao.sun.yunxin.handler.AppHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/app")
@MenuSetting("menu-financial")
public class AppController extends BaseController {

    @Autowired
    private AppHandler appHandler;

    private static final Log logger = LogFactory.getLog(AppController.class);

    // 信托商户管理-列表 查询
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @MenuSetting("submenu-app")
    public @ResponseBody
    String searchApp(@ModelAttribute AppQueryModel queryModel, Page page) {
        try {
            if (queryModel == null) {
                return jsonViewResolver.errorJsonResult("查询错误！");
            }
            Map<String, Object> resultMap = appHandler.searchAppBy(queryModel, page);
            return jsonViewResolver.sucJsonResult(resultMap, SerializerFeature.DisableCircularReferenceDetect);
        } catch (Exception e) {
            logger.error("#AppController searchApp#  occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("查询错误！");
        }
    }

    // 信托商户管理-列表 新增
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @MenuSetting("submenu-app")
    public @ResponseBody
    String createApp(@ModelAttribute CreateAppModel queryModel,
                     @Secure Principal principal, HttpServletRequest request) {
        try {
            String msg = appHandler.createApp(queryModel, principal.getId(), IpUtil.getIpAddress(request));
            if (StringUtils.isNotBlank(msg)) {
                return jsonViewResolver.errorJsonResult(msg);
            }
            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            logger.error("#AppController createApp#  occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误！");
        }
    }

    // 信托商户管理-列表 修改
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @MenuSetting("submenu-app")
    public @ResponseBody
    String modifyApp(@ModelAttribute CreateAppModel queryModel,
                     @Secure Principal principal, HttpServletRequest request) {
        try {
            String msg = appHandler.modifyApp(queryModel, principal.getId(), IpUtil.getIpAddress(request));
            if (StringUtils.isNotBlank(msg)) {
                return jsonViewResolver.errorJsonResult(msg);
            }
            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            logger.error("#AppController modifyApp#  occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误！");
        }
    }

    // 信托商户信息详情
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @MenuSetting("submenu-app")
    public @ResponseBody
    String detail(@RequestParam(value = "appId") String appId) {
        try {
            if (StringUtils.isBlank(appId)) {
                return jsonViewResolver.errorJsonResult("信托商户信息详情获取失败！！！");
            }
            Map<String, Object> data = appHandler.getDetails(appId);
            return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect);
        } catch (Exception e) {
            logger.error("##AppController detail## error!!");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("信托商户信息详情获取失败！！！");
        }
    }

}
