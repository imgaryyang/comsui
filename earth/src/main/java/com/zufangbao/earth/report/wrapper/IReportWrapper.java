package com.zufangbao.earth.report.wrapper;

import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.sun.entity.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 报表包装接口
 * @author zhanghongbing
 *
 */
public interface IReportWrapper<T> {

	/**
	 * 根据查询参数，包装报表并输出
	 * @param paramsBean 查询参数bean
	 * @param request 
	 * @param response 
	 * @param exportEventLogModel 
	 * @throws Exception 
	 */
    ExportEventLogModel wrap(T paramsBean, HttpServletRequest request, HttpServletResponse response, ExportEventLogModel exportEventLogModel, Principal principal) throws Exception;
}
