package com.zufangbao.earth.yunxin.web.controller;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.handler.InputHistoryHandler;
import com.zufangbao.sun.service.InputHistoryService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inputHistory")
public class InputHistoryController extends BaseController{

    @Autowired
    private InputHistoryService inputHistoryService;
    @Autowired
    private InputHistoryHandler inputHistoryHandler;

    private static final Log logger = LogFactory.getLog(InputHistoryController.class);

    // 获取5条输入历史
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getInputHistories(
            @Secure Principal principal, @RequestParam(value = "relatedUuid" ,required = false) String relatedUuid,
            @RequestParam(value = "whatFor", defaultValue = "-1") Integer whatFor, Page page){
        try{
            page.setEveryPage(5);
            List<Map<String, Object>> inputHistories = inputHistoryService.getInputContentsWithUuid(principal.getId(), relatedUuid, whatFor,page);
            return jsonViewResolver.sucJsonResult("list", inputHistories, SerializerFeature.DisableCircularReferenceDetect);
        }catch (Exception e) {
            logger.error("##getInputHistories## occur error!");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    // 新增输入历史
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addInputHistory(
            @Secure Principal principal, @RequestParam(value = "relatedUuid", required = false) String relatedUuid,
            @RequestParam(value = "content") String content, @RequestParam(value = "whatFor") Integer whatFor) {
        try{
            inputHistoryHandler.createInputHistory(content, principal.getName(),principal.getId(), relatedUuid, whatFor);
            return jsonViewResolver.sucJsonResult();
        }catch (Exception e) {
            logger.error("##addInputHistory## occur error!");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    // 修改输入历史
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editInputHistory(
            @RequestParam(value = "uuid") String uuid,@RequestParam(value = "content") String content) {
        try{
            boolean fine = inputHistoryHandler.editInputHistory(uuid, content);
            return jsonViewResolver.jsonResult(fine);
        }catch (Exception e) {
            logger.error("##editInputHistory## occur error!");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    // 删除输入历史记录
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    public String deleteInputHistory(@RequestParam(value = "uuid") String uuid){
        try{
            boolean fine = inputHistoryHandler.deleteInputHistory(uuid);
            return jsonViewResolver.jsonResult(fine);
        }catch (Exception e) {
            logger.error("##deleteInputHistory## occur error!");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

}
