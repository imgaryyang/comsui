package com.zufangbao.earth.report.wrapper.async;

import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.earth.report.exception.ExportException;

/**
 * 异步（存盘至指定目录）报表包装接口
 * @author zhanghongbing
 *
 * @param <T> 参数范型
 */
public interface IAsyncReportWrapper<T> {
	
	public void checkParams(T paramsBean) throws ExportException;

	/**
	 * 根据查询参数，包装报表并存放到磁盘
	 * @param paramsBean 请求参数bean
	 * @param zipPathName zip文件存放目录
	 */
	public ExportEventLogModel asyncWrap(T paramsBean, String zipPathName, ExportEventLogModel exportEventLogModel) throws Exception;
	
}
