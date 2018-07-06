package com.zufangbao.earth.update.wrapper;


import com.zufangbao.gluon.exception.CommonException;
import com.zufangbao.gluon.spec.global.GlobalCodeSpec;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
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
public class UpdateSqlCacheManager implements ResourceLoaderAware {
	public final static String CONST_XML_ATTRIBUTE_ID = "id";
	public final static String CONST_XML_ELEMENT_SQL_TEMPLATE = "sqltemplate";

	private ResourceLoader resourceLoader;


	/**
	 * sqltemplate xml文件列表
	 */
	private String[] fileNames;


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


	@Cacheable("updateSqlCacheManager")
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String,Map<String,Object>> getSqlParam() throws DocumentException, CommonException, IOException{
		Map<String,Map<String,Object>> sqlMap=new HashMap();
		for (String fileName : fileNames) {
		Resource[] resources = ((ResourcePatternResolver) this.resourceLoader).getResources(fileName);
		for(Resource resource:resources)
		getSqlFromFIle(resource,sqlMap);
		}
		return sqlMap;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String,Object> getSqlFromFIle(Resource resource,Map<String,Map<String,Object>> sqlMaps) throws DocumentException, CommonException{
		String fileName = resource.getFilename();
		try {
			SAXReader saxReader = new SAXReader();
			Document doc = saxReader.read(resource.getInputStream());
			Element root = doc.getRootElement();
			Map<String,Object> sqlMap=new HashMap();
			List<Element> sqltemplates = root.elements(CONST_XML_ELEMENT_SQL_TEMPLATE);
			for (Element sqltemplate : sqltemplates) {
				List<Element> elements =  sqltemplate.elements();
				for(Element template : elements) {
					String sqlId = template.attributeValue(CONST_XML_ATTRIBUTE_ID);
					String sql=template.getTextTrim();
					if(StringUtils.isBlank(sqlId) || sqlMap.containsKey(sqlId)) {
						throw new CommonException(GlobalCodeSpec.CODE_FAILURE, "#SQL管理［IUpdateBaseWrapper］异常，SQL文件［" + fileName + "］，SQLID［" + sqlId + "］");
					}
					sqlMap.put(sqlId, sql);
				}
			}
			if(StringUtils.isBlank(fileName) || sqlMaps.containsKey(fileName)) {
				throw new CommonException(GlobalCodeSpec.CODE_FAILURE, "#SQL管理［IUpdateBaseWrapper］异常，SQL文件［" + fileName + "］");
			}
			sqlMaps.put(fileName, sqlMap);
		return sqlMap;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonException(GlobalCodeSpec.CODE_FAILURE, "#SQL管理［IUpdateBaseWrapper］异常，SQL文件［" + fileName + "］");
		}

	}


}
