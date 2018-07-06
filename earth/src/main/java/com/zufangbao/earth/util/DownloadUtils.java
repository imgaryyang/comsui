package com.zufangbao.earth.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

/**
 * @author juxer
 */
public class DownloadUtils {
	
	/**
	 * 文件下载
	 * @param originFileName
	 * @param saveFileName
	 * @param savePath
	 * @param response
	 * @return
	 */
	public static void flushFileIntoHttp(String originFileName, String saveFileName, String savePath, HttpServletResponse response)throws IOException {
		//设置响应头，控制浏览器下载该文件
		response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(originFileName, "UTF-8"));
		 //读取要下载的文件，保存到文件输入流
		FileInputStream in = new FileInputStream(savePath + saveFileName);
		OutputStream out = response.getOutputStream();		//创建输出流
		byte buffer[] = new byte[1024];		//创建缓冲区
		int len = 0;
		while((len=in.read(buffer))>0){		//循环将输入流中的内容读取到缓冲区当中
			 out.write(buffer, 0, len);		//输出缓冲区的内容到浏览器，实现文件下载
		}
		in.close();
		out.close();
	}

}
