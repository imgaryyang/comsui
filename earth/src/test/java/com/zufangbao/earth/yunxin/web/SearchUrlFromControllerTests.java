/**
 * 
 */
package com.zufangbao.earth.yunxin.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo2do.core.web.interceptor.MenuSetting;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wukai
 *
 */
public class SearchUrlFromControllerTests {

	private static String extractRequestMappingUrl(String content) {
		
		if(content.indexOf("value")>-1) {
			return content.substring(content.indexOf("=")+1,content.lastIndexOf(")"));
		}
		if(content.indexOf("\"")<0) {
			return content.substring(content.indexOf("(")+1,content.lastIndexOf(")"));
		}
		return content.substring(content.indexOf("\"")+1, content.lastIndexOf("\""));
	}
	
	private static String extractMenuSettingValue(String content) {
		if(content.indexOf("\"")<0) {
			return content.substring(content.indexOf("(")+1,content.lastIndexOf(")"));
		}
		
		return content.substring(content.indexOf("\"")+1, content.lastIndexOf("\""));
	}
	
	private static List<BtnInfo> searchBtnInfos(File file) {
		
		int line = 0;
		
		try {
		
		List<BtnInfo> btnInfos = new ArrayList<BtnInfo>();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		
		String content = null;
		
		String parentMkey = "";
		
		String parentUrl = "";
		
		String url = "";
		
		String preContent = "";
		
		BtnInfo btnInfo = new BtnInfo();
		
		while((content=reader.readLine()) !=null) {
			
			line++;
			
			if("".equals(content)) {
				continue;
			}
			
			content=content.trim();
			
			if("".equals(parentUrl)) {
				
				if(content.startsWith("@RequestMapping")) {
					
					parentUrl=extractRequestMappingUrl(content);
				}
			}
			
			if("".equals(parentMkey)) {
				
				if(content.startsWith("@MenuSetting")) {
					parentMkey=extractMenuSettingValue(content);
				}
			}
			
			if(!"".equals(parentUrl) && !"".equals(parentMkey)) {
				
				if(content.startsWith("@RequestMapping")) {
					
					url = parentUrl+extractRequestMappingUrl(content);
					
					btnInfo.setUrl(url);
					btnInfo.setDescription(preContent);
					btnInfo.setParentMkey(parentMkey);
					btnInfo.setControllerName(file.getName());
					btnInfos.add(btnInfo);
					
				}
				if(content.startsWith("@MenuSetting")) {
					
					btnInfos.get(btnInfos.size()-1).setParentMkey(extractMenuSettingValue(content));
				}
				btnInfo=new BtnInfo();
				
			}
			
			preContent=content;
		}
		
		return btnInfos;
		}catch (Exception e) {
			e.printStackTrace();
			
			System.out.println("file name:"+file.getName()+",line:"+line);
			
			return Collections.emptyList();
		}
		
	}
	
	private static List<BtnInfo> buildBtnInfo(Method method,String controllerName,String parentMkey,String[] parentUrls) {
		
		RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
		
		String[] urls = null;
		
		if(null != requestMapping) {
			urls = requestMapping.value();
		}
		
		if(null == urls || urls.length == 0)  {
			urls = new String[] {"/"};
		}
		List<BtnInfo> btnInfos = new ArrayList<>();
		
		MenuSetting menuSetting = method.getAnnotation(MenuSetting.class);
		
		for (String url : urls) {
			
			for (String parentUrl : parentUrls) {
				
				BtnInfo btnInfo = new BtnInfo();
				
				if(!parentUrl.endsWith("/")) {
					parentUrl+="/";
				}
				if(url.startsWith("/")) {
					url=url.substring(1);
				}
				
				btnInfo.setUrl(parentUrl+url);
				if(menuSetting !=null) {
					btnInfo.setParentMkey(menuSetting.value());
				}else {
					btnInfo.setParentMkey(parentMkey);
				}
				btnInfo.setControllerName(controllerName);
				
				btnInfos.add(btnInfo);
			}
			
		}
		
		return btnInfos;
	}
	
	private static String convertJavaName(File file) {
		
		return file.getAbsolutePath().substring(file.getAbsolutePath().indexOf("com/zufangbao"),file.getAbsolutePath().lastIndexOf("java")-1).replace("/", ".");
	}
	
	
	private static List<BtnInfo> searchBtnInfos2(File file) {
		
		List<BtnInfo> btnInfos = new ArrayList<BtnInfo>();
		
		try {
			
		String javaName = convertJavaName(file);
		
		System.out.println("javaNmae:"+javaName);
		
		Class<?> clazz = Class.forName(javaName);
		
		RequestMapping requestMapping = (RequestMapping)clazz.getAnnotation(RequestMapping.class);
		
		String[] parentUrls  = null;
		
		if(null != requestMapping) {
			
			parentUrls = requestMapping.value();
		}
		
		if(null == parentUrls) {
			
			parentUrls = new String[] {"/"};
		}
		
		for (String string : parentUrls) {
			
			if(string.startsWith("/api") || string.startsWith("/pre") || string.startsWith("/inner-api/")) {
				return Collections.emptyList();
			}
		}
		
		MenuSetting parentMenuSetting =  ((MenuSetting)clazz.getAnnotation(MenuSetting.class));
		
		String parentMkey = parentMenuSetting == null ? "":parentMenuSetting.value();
		
		Method[] methods = clazz.getDeclaredMethods();
		
		for (Method method : methods) {
		
			btnInfos.addAll(buildBtnInfo(method, file.getName(), parentMkey, parentUrls));
		}
		
		return btnInfos;
		
		}catch (Exception e) {
			
			e.printStackTrace()
			;
			
			System.out.println("file name:"+file.getName());
			
			return Collections.emptyList();
		}
		
		
	}
	
	private static void printAsSql(List<BtnInfo> btnInfos) {
		
		StringBuffer  sql =  new StringBuffer("INSERT INTO `system_button_tmp` (`controller_name`,`bkey`, `name`, `url`, `description`, `parent_mkey`) VALUES ");
		
//		String valueTemplate = " ('modify-contract-basic-info', '编辑信托合同', '/aa/url', '操作：编辑合同基础信息', 'submenu-financial-contract') ";
		String valueTemplate = " ('%s','%s', '%s', '%s', '%s', '%s') ";
		
		int i = 0;
		
		int size = btnInfos.size();
		
		for (BtnInfo btnInfo : btnInfos) {
			
			sql.append(String.format(valueTemplate,btnInfo.getControllerName(), null == btnInfo.getBKey() ? btnInfo.getParentMkey()+i:btnInfo.getBKey(),btnInfo.getDescription(),btnInfo.getUrl(),btnInfo.getDescription(),btnInfo.getParentMkey()));
			
			if(i==size-1) {
				sql.append(";");
			}else {
				sql.append(",");
			}
			i++;
		}
		
		System.out.println(sql);

	}
	
	private static void  searchControllerFiles(List<File> fileList,File  file) throws Exception{
		
		if(file.isDirectory()) {
			
			for(File item : Arrays.asList(file.listFiles())){
				
				searchControllerFiles(fileList, item);
			}
		
		}else {
			
//			if(file.getName().endsWith("Controller.java")) {
			if(file.getName().endsWith("ExportReportController.java") 
					
					|| file.getName().endsWith("PrincipalController.java")
							
							) {
				
				fileList.add(file);
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		List<File> fileList = new ArrayList<>();
		
		File earthController = new File("/Users/wukai/Documents/zufangbao-project/public/yunxin/earth/src/main/java/com/zufangbao/earth/");
		
		searchControllerFiles(fileList, earthController );
		
		System.out.println(fileList.size());
		
		List<BtnInfo> btnInfos = new ArrayList<>();
		
		for (File file : fileList) {
			
//			System.out.println(file.getName());
			
			List<BtnInfo> btnList = searchBtnInfos2(file);
			
			if(CollectionUtils.isEmpty(btnList)) {
				System.out.println("file["+file.getName()+"] has no url");
			}
			
			btnInfos.addAll(btnList);
			
		}
//		
		printAsSql(btnInfos);
		
	}
	
	@Test
	public void testUrlMatch() {
		
		Assert.assertTrue(Pattern.matches("/voucher/business/detail/\\w+/data", "/voucher/business/detail/sdfdfd/data"));
		
		Assert.assertFalse(Pattern.matches("/voucher/business/detail/\\w+/data", "/voucher/business/deta/sdfdfd/data"));
		
		Assert.assertTrue(Pattern.matches("/voucher/business/detail/\\d+/data", "/voucher/business/detail/121231231/data"));
		
		Assert.assertFalse(Pattern.matches("/voucher/business/detail/\\d+/data", "/voucher/business/detail/12123123d1/data"));
		
		
	}
	
	
}


@Data
@NoArgsConstructor
class BtnInfo {
	
	private String controllerName;
	
	private String url;
	
	private String bKey;
	
	private String parentMkey;
	
	private String description;
	
}
