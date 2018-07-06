package com.zufangbao.earth.web.controller.report;


import com.zufangbao.earth.report.exception.ExportException;
import com.zufangbao.gluon.resolver.JsonViewResolver;
import com.zufangbao.gluon.resolver.PageViewResolver;
import com.zufangbao.gluon.spec.global.GlobalCodeSpec;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * 报表基础控制类
 * @author zhanghongbing
 *
 */
public class ReportBaseController {
	
	protected static final String SYS_ERROR="系统错误";
	
	@Autowired
	public PageViewResolver pageViewResolver;
	
	@Autowired
	public JsonViewResolver jsonViewResolver;
	
	@Value("#{config['uploadPath']}")
	public String uploadPath = "";
	
	public String errorJsonResult(Exception e) {
		if(e instanceof ExportException) {
			ExportException exportException = (ExportException) e;
			return jsonViewResolver.jsonResult(exportException.getCode(), exportException.getMsg(), null, null);
		}
		return jsonViewResolver.jsonResult(GlobalCodeSpec.CODE_FAILURE, SYS_ERROR, null, null);
	}
	
	public void exportDiskFileToClient(HttpServletResponse response, String filePath) throws IOException {
		OutputStream out = response.getOutputStream();
		try {
			response.reset();
			response.setContentType("application/octet-stream");

			File file = new File(filePath);
			if(!file.exists()) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			String encodeFileName = getEncodeFileName(file.getName(), "UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=" + encodeFileName);
			out.write(FileUtils.readFileToByteArray(file)); 
			out.flush();
		} finally {
			if(out != null) {
				out.close();
			}
		}
	}
	
	private String getEncodeFileName(String zipFileName, String charset) throws UnsupportedEncodingException {
		String encodeFileName = java.net.URLEncoder.encode(zipFileName, charset);
		encodeFileName = encodeFileName.replaceAll("\\+", "%20");
		return encodeFileName;
	}
	
}
