package com.zufangbao.earth.web.controller.update;

import com.zufangbao.earth.update.model.UpdateWrapperModel;
import com.zufangbao.earth.update.wrapper.IUpdateWrapper;
import com.zufangbao.earth.update.wrapper.UpdateSqlCacheManager;
import com.zufangbao.earth.yunxin.api.BaseApiController;
import com.zufangbao.gluon.resolver.JsonViewResolver;
import com.zufangbao.gluon.util.SpringContextUtil;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.wellsfargo.yunxin.data.sync.handler.DataSyncHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zufangbao.earth.update.util.UpdateConfiguration.UPDATE_CODES_WRAPPER_BEAN_MAPPER;
import static com.zufangbao.earth.update.util.UpdateConfiguration.UPDATE_CODES_WRAPPER_FILE_MAPPER;
import static com.zufangbao.earth.yunxin.api.swagger.SwaggerSpec.DATA_TYPE_STRING;
import static com.zufangbao.earth.yunxin.api.swagger.SwaggerSpec.PARAM_TYPE_QUERY;


@Controller
@RequestMapping("/update")
@SuppressWarnings("rawtypes")
@Api(value = "五维金融贷后接口V2.0", description = "五维金融贷后接口V2.0")
public class UpdateDataController extends BaseApiController {

    private static final Log logger = LogFactory.getLog(UpdateDataController.class);
    private static final String PARAMS = "params";

    private static final String PARAM_VALID = "报表编号对应的模板中参数[%s]不能为空";
    private static final String PARAM_PASSED = "参数验证通过";

    @Autowired
    public JsonViewResolver jsonViewResolver;

    @Autowired
    public UpdateSqlCacheManager updateSqlCacheManager;

    @Autowired
    @Qualifier("dataSyncHandler")
    private DataSyncHandler dataSyncHandler;


    @RequestMapping(value = "/getSqlMethod", method = RequestMethod.GET)
    public @ResponseBody
    String GetUpdateSql(@ModelAttribute UpdateWrapperModel updateWrapperModel, HttpServletResponse response) {
        String updateId = updateWrapperModel.getUpdateId();
        try {
            if (StringUtils.isBlank(updateId)
                    || !UPDATE_CODES_WRAPPER_BEAN_MAPPER.containsKey(updateId)
                    || !UPDATE_CODES_WRAPPER_FILE_MAPPER.containsKey(updateId)) {
                return jsonViewResolver.errorJsonResult("无效的sql查询编号！");
            }
            //根据查询编号，获取sql文件名
            String fileName = UPDATE_CODES_WRAPPER_FILE_MAPPER.get(updateId);

            //根据查询编号，获取sql包装类
            Class<? extends IUpdateWrapper> wrapperClass = UPDATE_CODES_WRAPPER_BEAN_MAPPER.get(updateId);

            if (null == fileName || null == wrapperClass) {
                return jsonViewResolver.errorJsonResult("未找到该sql编号对应的服务！");
            }

            //实例化sql包装类
            IUpdateWrapper wrapper = SpringContextUtil.getBean(wrapperClass);

            //通过指定sql包装类，获取sql
            String sql = wrapper.wrap(updateWrapperModel);


            return "返回的sql为：" + sql;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("#获取指定updateId" + updateId + "sql失败！");
            return jsonViewResolver.errorJsonResult("获取指定updateId" + updateId + "失败！");
        }
    }

    @Value("#{config['dataFilePath']}")
    private String dataFilePath = "";

    @RequestMapping(value = "/getCsvSyncJVData", method = RequestMethod.POST)
    @ApiOperation(value = "获取同步数据", notes = "获取同步数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间", paramType = PARAM_TYPE_QUERY, dataType = DATA_TYPE_STRING),
            @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = PARAM_TYPE_QUERY, dataType = DATA_TYPE_STRING)
    })
    public @ResponseBody
    String getCsvSyncJVData(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> data = new HashMap<>();
        List<String> fileNames;
        try {
            String sTime = request.getParameter("startTime");
            if (StringUtils.isEmpty(sTime)) {
                logger.warn("startTime is null");
                data.put("message", "开始时间不能为空");
                return jsonViewResolver.errorJsonResult(data);
            }
            Date startTime = DateUtils.asDay(sTime);
            String eTime = request.getParameter("endTime");
            Date endTime = null;
            if (StringUtils.isNotEmpty(eTime)) {
                endTime = DateUtils.asDay(eTime);
            }
            fileNames = dataSyncHandler.getCsvSyncJVData(startTime, endTime, dataFilePath);

        } catch (Exception e) {
            e.printStackTrace();
            data.put("message", "系统错误");
            return jsonViewResolver.errorJsonResult(data);
        }
        data.put("message", fileNames);
        return jsonViewResolver.sucJsonResult(data);
    }

    @RequestMapping(value = "/test_getCsvSyncJVData", method = RequestMethod.POST)
    @ApiOperation(value = "获取同步数据", notes = "获取同步数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间", paramType = PARAM_TYPE_QUERY, dataType = DATA_TYPE_STRING),
            @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = PARAM_TYPE_QUERY, dataType = DATA_TYPE_STRING)
    })
    public @ResponseBody
    String test_getCsvSyncJVData(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> data = new HashMap<>();
        List<String> fileNames = null;
        try {
            String sTime = request.getParameter("startTime");
            if (StringUtils.isEmpty(sTime)) {
                logger.warn("startTime is null");
                data.put("message", "开始时间不能为空");
                return jsonViewResolver.errorJsonResult(data);
            }
            Date startTime = DateUtils.asDay(sTime);
            String eTime = request.getParameter("endTime");
            Date endTime = null;
            if (StringUtils.isNotEmpty(eTime)) {
                endTime = DateUtils.asDay(eTime);
            }
            fileNames = dataSyncHandler.getCsvSyncJVDataForEveryDay(startTime, endTime, dataFilePath);

        } catch (Exception e) {
            e.printStackTrace();
            data.put("message", "系统错误");
            return jsonViewResolver.errorJsonResult(data);
        }
        data.put("message", fileNames);
        return jsonViewResolver.sucJsonResult(data);
    }
}