package com.zufangbao.earth.web.controller.report;

import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.earth.report.wrapper.async.IAsyncReportWrapper;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.yunxin.entity.ReportJobStatus;
import com.zufangbao.sun.yunxin.service.ReportJobService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AsyncReportJobWriter {

	@Value("#{config['uploadPath']}")
	private String uploadPath = "";
	
	@Autowired
	private  ReportJobService reportJobService;
	
	private static final Log logger = LogFactory.getLog(AsyncReportJobWriter.class);
	
	private String getReportPath() {
		return String.format("%s/report", this.uploadPath);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Async
	public void writeZipFileToDisk(Principal principal, String reportId,
								   String reportJobUuid, Object paramsBean, IAsyncReportWrapper wrapper) {
		ExportEventLogModel exportEventLogModel = new ExportEventLogModel(reportId, principal);
		try {
			logger.info("##export report to disk begin, requestId["+exportEventLogModel.getRequestId()+"], params["+JsonUtils.toJSONString(paramsBean)+"]");
			
			String zipFileName = String.format("/%s/%s.zip",DateUtils.today(), UUID.randomUUID().toString());
			String zipPathName = getReportPath() + zipFileName;
			//通过指定报表包装类，导出报表
			wrapper.asyncWrap(paramsBean, zipPathName, exportEventLogModel);
			
			exportEventLogModel.recordEndWriteOutTime();
			reportJobService.updateStatusAndPath(reportJobUuid, ReportJobStatus.SUCCESS, zipFileName);
		} catch (Exception e) {
			reportJobService.updateStatus(reportJobUuid, ReportJobStatus.FAIL);
			exportEventLogModel.setErrorMsg(e.getMessage());
			e.printStackTrace();
		} finally {
			logger.info("##export report to disk end, requestId["+exportEventLogModel.getRequestId()+"], record export event log info. " + JsonUtils.toJSONString(exportEventLogModel));
		}
	} 
	
}
