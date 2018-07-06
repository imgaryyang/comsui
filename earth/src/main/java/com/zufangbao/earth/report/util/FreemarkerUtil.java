package com.zufangbao.earth.report.util;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;

import java.io.StringWriter;
import java.util.Map;

public class FreemarkerUtil {
	
	private final static Version DEFAULT_VERSION = Configuration.VERSION_2_3_25;
	
	private final static String UTF8 = "UTF-8";
	
	private final static String DEFAULT_TEMPLATE_KEY = "template_key";
	
	/**
	 * 根据模版、参数进行加工
	 * @param templateSource 模版资源
	 * @param params 参数
	 */
	public static String process(String templateSource, Map<String, Object> params) throws Exception{
		Configuration cfg = new Configuration(DEFAULT_VERSION);
			
		StringTemplateLoader templateLoader = new StringTemplateLoader();
		templateLoader.putTemplate(DEFAULT_TEMPLATE_KEY, templateSource);
		
		cfg.setTemplateLoader(templateLoader);
		cfg.setDefaultEncoding(UTF8);
		
		Template template = cfg.getTemplate(DEFAULT_TEMPLATE_KEY);
		StringWriter writer = new StringWriter();
		template.process(params, writer);
		return writer.toString();
	}
	
}
