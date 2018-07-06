package com.zufangbao.earth.web.controller.report;

import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.earth.report.wrapper.IReportWrapper;
import com.zufangbao.earth.yunxin.handler.LoanBatchHandler;
import com.zufangbao.earth.yunxin.handler.SystemOperateLogHandler;
import com.zufangbao.gluon.util.SpringContextUtil;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.utils.JsonUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.zufangbao.earth.report.factory.ExportConfiguration.REPORT_CODES_PARAMS_CLASS_MAPPER;
import static com.zufangbao.earth.report.factory.ExportConfiguration.REPORT_CODES_WRAPPER_BEAN_MAPPER;

/**
 * 导出业务数据控制器
 *
 * @author zhanghongbing
 */
@Controller
@RequestMapping("/report/export")
public class ExportReportController extends ReportBaseController {
    @Autowired
    private LoanBatchHandler loanBatchHandler;

    private static final Log logger = LogFactory.getLog(ExportReportController.class);

    @Autowired
    private SystemOperateLogHandler systemOperateLogHandler;

    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    String export(@Secure Principal principal,
                  HttpServletRequest request, HttpServletResponse response,
                  String reportId) {
        ExportEventLogModel exportEventLogModel = new ExportEventLogModel(reportId, principal);
        logger.info("##export report begin, requestId[" + exportEventLogModel.getRequestId() + "], params[" + JsonUtils.toJSONString(request.getParameterMap()) + "]");
        try {
            if (StringUtils.isBlank(reportId)
                    || !REPORT_CODES_PARAMS_CLASS_MAPPER.containsKey(reportId)
                    || !REPORT_CODES_WRAPPER_BEAN_MAPPER.containsKey(reportId)) {
                exportEventLogModel.setErrorMsg("无效的报表编号！");
                return jsonViewResolver.errorJsonResult("无效的报表编号！");
            }
            //根据报表编号，获取查询参数类
            Class<?> paramsClass = REPORT_CODES_PARAMS_CLASS_MAPPER.get(reportId);
            //根据报表编号，获取报表包装类
            Class<? extends IReportWrapper<?>> wrapperClass = REPORT_CODES_WRAPPER_BEAN_MAPPER.get(reportId);

            if (paramsClass == null || wrapperClass == null) {
                exportEventLogModel.setErrorMsg("未找到该报表编号对应的服务！");
                return jsonViewResolver.errorJsonResult("未找到该报表编号对应的服务！");
            }

            //将查询参数封装至bean
            Object paramsBean = paramsClass.newInstance();
            BeanUtils.populate(paramsBean, request.getParameterMap());

            //实例化报表包装类
            IReportWrapper wrapper = SpringContextUtil.getBean(wrapperClass);
            //通过指定报表包装类，导出报表
            wrapper.wrap(paramsBean, request, response, exportEventLogModel, principal);

            exportEventLogModel.recordEndWriteOutTime();
            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            e.printStackTrace();
            return errorJsonResult(e);
        } finally {
            logger.info("##export report end, requestId[" + exportEventLogModel.getRequestId() + "], record export event log info. " + JsonUtils.toJSONString(exportEventLogModel));
        }
    }

}
