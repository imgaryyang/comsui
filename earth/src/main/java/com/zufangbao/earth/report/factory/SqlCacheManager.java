package com.zufangbao.earth.report.factory;

import com.zufangbao.earth.report.exception.ExportException;
import com.zufangbao.gluon.spec.global.GlobalCodeSpec;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author zhanglongfei
 *
 */

public class SqlCacheManager implements ResourceLoaderAware {
	
	public final static String CONST_XML_ELEMENT_SQL_TEMPLATE = "sqltemplate";
	public final static String CONST_XML_ATTRIBUTE_ID = "id";

	/**
	 * sqltemplate xml文件列表
	 */
	private String[] fileNames;
	
	private ResourceLoader resourceLoader;
	
	public String[] getFileNames() {
		return fileNames;
	}

	public void setFileNames(String[] fileNames) {
		this.fileNames = fileNames;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
	
	@Cacheable("sqlCacheManager")
	public Map<String, String> getCachedSqlMap() throws IOException {
		Map<String, String> sqlCacheMap = new HashMap<String, String>();
		for (String fileName : fileNames) {
			Resource[] resources = ((ResourcePatternResolver) this.resourceLoader).getResources(fileName);
			for (Resource resource : resources) {
				analyseSqlTemplateXML(sqlCacheMap, resource);
			}
		}
		return sqlCacheMap;
	}

	@SuppressWarnings("unchecked")
	private void analyseSqlTemplateXML(Map<String, String> sqlCacheMap, Resource resource) throws ExportException {
		String fileName = resource.getFilename();
		try {
			SAXReader saxReader = new SAXReader();
			Document doc = saxReader.read(resource.getInputStream());
			Element root = doc.getRootElement();
			List<Object> sqltemplates = root.elements(CONST_XML_ELEMENT_SQL_TEMPLATE);
			for (Object sqltemplate : sqltemplates) {
				Element element = (Element) sqltemplate;
				String sqlId = element.attributeValue(CONST_XML_ATTRIBUTE_ID);
				String sqlContent = element.getTextTrim();
				if(StringUtils.isBlank(sqlId) || sqlCacheMap.containsKey(sqlId)) {
					throw new ExportException(GlobalCodeSpec.CODE_FAILURE, "#SQL缓存管理［SqlCacheManager］异常，SQL文件［" + fileName + "］，SQLID［" + sqlId + "］");
				}
				sqlCacheMap.put(sqlId, sqlContent);
			}
		} catch (ExportException ee) {
			throw ee;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExportException(GlobalCodeSpec.CODE_FAILURE, "#SQL缓存管理［SqlCacheManager］异常，SQL文件［" + fileName + "］");
		}

	}

}
